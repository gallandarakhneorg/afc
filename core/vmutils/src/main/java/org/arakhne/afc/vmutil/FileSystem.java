/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.vmutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.locale.Locale;

/** An utility class that permits to deal with filenames.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings({"checkstyle:methodcount"})
public final class FileSystem {

	static {
		URLHandlerUtil.installArakhneHandlers();

		final String validChars = "[^\\\\/:*?\"<>|]"; //$NON-NLS-1$
		final String bslashChar = "\\\\"; //$NON-NLS-1$

		final StringBuilder pattern = new StringBuilder();
		pattern.append("^"); //$NON-NLS-1$
		pattern.append("(([a-zA-Z][:|]"); //$NON-NLS-1$
		pattern.append(validChars);
		pattern.append("*)|("); //$NON-NLS-1$
		pattern.append(validChars);
		pattern.append("+"); //$NON-NLS-1$
		pattern.append(bslashChar);
		pattern.append(validChars);
		pattern.append("+)|("); //$NON-NLS-1$
		pattern.append(bslashChar);
		pattern.append("))?("); //$NON-NLS-1$
		pattern.append(bslashChar);
		pattern.append(validChars);
		pattern.append("*)*"); //$NON-NLS-1$
		pattern.append("$"); //$NON-NLS-1$
		//"^([A-Za-z]:)?([^\\\\/:*?\"<>|]*\\\\)*[^\\\\/:*?\"<>|]*$";
		WINDOW_NATIVE_FILENAME_PATTERN = pattern.toString();
	}

	/** Character used to specify a file extension.
	 */
	public static final char EXTENSION_SEPARATOR_CHAR = '.';

	/** String which is representing the current directory in a relative path.
	 */
	public static final String CURRENT_DIRECTORY = "."; //$NON-NLS-1$

	/** String which is representing the parent directory in a relative path.
	 */
	public static final String PARENT_DIRECTORY = ".."; //$NON-NLS-1$

	/** Character used to separate paths on an URL.
	 */
	public static final char URL_PATH_SEPARATOR_CHAR = '/';

	/** Character used to separate paths on an URL.
	 */
	public static final String URL_PATH_SEPARATOR = "/"; //$NON-NLS-1$

	/** String used to specify a file extension.
	 */
	public static final String EXTENSION_SEPARATOR = "."; //$NON-NLS-1$

	/** Prefix used to join in a Jar URL the jar filename and the inside-jar filename.
	 */
	public static final String JAR_URL_FILE_ROOT = "!/"; //$NON-NLS-1$

	/** Character that is used for separating components of a path on Windows operating systems.
	 */
	public static final char WINDOWS_SEPARATOR_CHAR = '\\';

	/** Character that is used for separating components of a path on Windows operating systems.
	 */
	public static final String WINDOWS_SEPARATOR_STRING = "\\"; //$NON-NLS-1$

	/** Character that is used for separating components of a path on Unix operating systems.
	 */
	public static final String UNIX_SEPARATOR_STRING = URL_PATH_SEPARATOR;

	/** Regular expression pattern which corresponds to Windows native filename.
	 */
	private static final String WINDOW_NATIVE_FILENAME_PATTERN;

	private static final Random RANDOM = new Random();

	private static final DeleteOnExitHook DELETE_ON_EXIT_HOOK = new DeleteOnExitHook();

	private static Boolean isFileCompatibleWithURL;

	private static final int BUFFER_SIZE = 4096;

	private static final char[] FILE_PREFIX = {'f', 'i', 'l', 'e', ':', '/', '/'};

	private FileSystem() {
		//
	}

	/** Replace the HTML entities by the current charset characters.
	 *
	 * @param string the string to decode.
	 * @return decoded string or {@code s}.
	 */
	private static String decodeHTMLEntities(String string) {
		if (string == null) {
			return null;
		}
		try {
			return URLDecoder.decode(string, Charset.defaultCharset().displayName());
		} catch (UnsupportedEncodingException exception) {
			return string;
		}
	}

	/** Replace the special characters by HTML entities.
	 *
	 * @param string the string to decode.
	 * @return decoded string or {@code s}.
	 */
	private static String encodeHTMLEntities(String string) {
		if (string == null) {
			return null;
		}
		try {
			return URLEncoder.encode(string, Charset.defaultCharset().displayName());
		} catch (UnsupportedEncodingException exception) {
			return string;
		}
	}

	/** Decode the given file to obtain a string representation
	 * which is compatible with the URL standard.
	 * This function was introduced to have a work around
	 * on the '\' character on Windows operating system.
	 *
	 * @param file the file.
	 * @return the string representation of the file.
	 * @since 6.2
	 */
	private static String fromFileStandardToURLStandard(File file) {
		if (file == null) {
			return null;
		}
		return fromFileStandardToURLStandard(file.getPath());
	}

	/** Decode the given file to obtain a string representation
	 * which is compatible with the URL standard.
	 * This function was introduced to have a work around
	 * on the '\' character on Windows operating system.
	 *
	 * @param file the file.
	 * @return the string representation of the file.
	 * @since 6.2
	 */
	private static String fromFileStandardToURLStandard(String file) {
		if (file == null) {
			return null;
		}
		if (isFileCompatibleWithURL == null) {
			isFileCompatibleWithURL = Boolean.valueOf(
					URL_PATH_SEPARATOR.equals(File.separator));
		}
		String filePath = file;
		if (!isFileCompatibleWithURL) {
			filePath = filePath.replaceAll(
					Pattern.quote(File.separator),
					Matcher.quoteReplacement(URL_PATH_SEPARATOR));
		}
		// Add root slash for Windows paths that are starting with a disk id.
		if (Pattern.matches("^[a-zA-Z][:|].*$", filePath)) { //$NON-NLS-1$
			filePath = URL_PATH_SEPARATOR + filePath;
		}
		return filePath;
	}

	/** Replies if the given URL has a jar scheme.
	 *
	 * @param url the URL to test.
	 * @return <code>true</code> if the given URL uses a jar scheme.
	 */
	@Pure
	@Inline(value = "URISchemeType.JAR.isURL($1)", imported = {URISchemeType.class})
	public static boolean isJarURL(URL url) {
		return URISchemeType.JAR.isURL(url);
	}

	/** Replies the jar part of the jar-scheme URL.
	 *
	 * <p>If the input URL is {@code jar:file:/path1/archive.jar!/path2/file},
	 * the output of this function is {@code file:/path1/archive.jar}.
	 *
	 * @param url the URL to test.
	 * @return the URL of the jar file in the given URL, or <code>null</code>
	 *     if the given URL does not use jar scheme.
	 */
	@Pure
	public static URL getJarURL(URL url) {
		if (!isJarURL(url)) {
			return null;
		}
		String path = url.getPath();
		final int idx = path.lastIndexOf(JAR_URL_FILE_ROOT);
		if (idx >= 0) {
			path = path.substring(0, idx);
		}
		try {
			return new URL(path);
		} catch (MalformedURLException exception) {
			return null;
		}
	}

	/** Replies the file part of the jar-scheme URL.
	 *
	 * <p>If the input URL is {@code jar:file:/path1/archive.jar!/path2/file},
	 * the output of this function is {@code /path2/file}.
	 *
	 * @param url the URL to test.
	 * @return the file in the given URL, or <code>null</code>
	 *     if the given URL does not use jar scheme.
	 */
	@Pure
	public static File getJarFile(URL url) {
		if (isJarURL(url)) {
			final String path = url.getPath();
			final int idx = path.lastIndexOf(JAR_URL_FILE_ROOT);
			if (idx >= 0) {
				return new File(decodeHTMLEntities(path.substring(idx + 1)));
			}
		}
		return null;
	}

	/** Replies the jar-schemed URL composed of the two given components.
	 *
	 * <p>If the inputs are {@code /path1/archive.jar} and @{code /path2/file},
	 * the output of this function is {@code jar:file:/path1/archive.jar!/path2/file}.
	 *
	 * @param jarFile is the URL to the jar file.
	 * @param insideFile is the name of the file inside the jar.
	 * @return the jar-schemed URL.
	 * @throws MalformedURLException when the URL is malformed.
	 */
	@Pure
	public static URL toJarURL(File jarFile, File insideFile) throws MalformedURLException {
		if (jarFile == null || insideFile == null) {
			return null;
		}
		return toJarURL(jarFile, fromFileStandardToURLStandard(insideFile));
	}

	/** Replies the jar-schemed URL composed of the two given components.
	 *
	 * <p>If the inputs are {@code /path1/archive.jar} and @{code /path2/file},
	 * the output of this function is {@code jar:file:/path1/archive.jar!/path2/file}.
	 *
	 * @param jarFile is the URL to the jar file.
	 * @param insideFile is the name of the file inside the jar.
	 * @return the jar-schemed URL.
	 * @throws MalformedURLException when the URL is malformed.
	 */
	@Pure
	@Inline("toJarURL(($1).toURI().toURL(), ($2))")
	public static URL toJarURL(File jarFile, String insideFile) throws MalformedURLException {
		return toJarURL(jarFile.toURI().toURL(), insideFile);
	}

	/** Replies the jar-schemed URL composed of the two given components.
	 *
	 * <p>If the inputs are {@code file:/path1/archive.jar} and @{code /path2/file},
	 * the output of this function is {@code jar:file:/path1/archive.jar!/path2/file}.
	 *
	 * @param jarFile is the URL to the jar file.
	 * @param insideFile is the name of the file inside the jar.
	 * @return the jar-schemed URL.
	 * @throws MalformedURLException when the URL is malformed.
	 */
	@Pure
	public static URL toJarURL(URL jarFile, File insideFile) throws MalformedURLException {
		if (jarFile == null || insideFile == null) {
			return null;
		}
		return toJarURL(jarFile, fromFileStandardToURLStandard(insideFile));
	}

	/** Replies the jar-schemed URL composed of the two given components.
	 *
	 * <p>If the inputs are {@code file:/path1/archive.jar} and @{code /path2/file},
	 * the output of this function is {@code jar:file:/path1/archive.jar!/path2/file}.
	 *
	 * @param jarFile is the URL to the jar file.
	 * @param insideFile is the name of the file inside the jar.
	 * @return the jar-schemed URL.
	 * @throws MalformedURLException when the URL is malformed.
	 */
	@Pure
	public static URL toJarURL(URL jarFile, String insideFile) throws MalformedURLException {
		if (jarFile == null || insideFile == null) {
			return null;
		}
		final StringBuilder buf = new StringBuilder();
		buf.append("jar:"); //$NON-NLS-1$
		buf.append(jarFile.toExternalForm());
		buf.append(JAR_URL_FILE_ROOT);
		final String path = fromFileStandardToURLStandard(insideFile);
		if (path.startsWith(URL_PATH_SEPARATOR)) {
			buf.append(path.substring(URL_PATH_SEPARATOR.length()));
		} else {
			buf.append(path);
		}
		return new URL(buf.toString());
	}

	/** Replies if the current operating system uses case-sensitive filename.
	 *
	 * @return <code>true</code> if the filenames on the current file system are case sensitive,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public static boolean isCaseSensitiveFilenameSystem() {
		switch (OperatingSystem.getCurrentOS()) {
		case AIX:
		case ANDROID:
		case BSD:
		case FREEBSD:
		case NETBSD:
		case OPENBSD:
		case LINUX:
		case SOLARIS:
		case HPUX:
			return true;
		case MACOSX:
		case WIN:
		case OTHER:
		default:
			return false;
		}
	}

	/** Replies the character used to separate the basename and the file extension.
	 *
	 * @return the character used to separate the basename and the file extension.
	 */
	@Pure
	@Inline(value = "EXTENSION_SEPARATOR_CHAR", constantExpression = true)
	public static char getFileExtensionCharacter() {
		return EXTENSION_SEPARATOR_CHAR;
	}

	/** Replies the dirname of the specified file.
	 *
	 * @param filename is the name to parse.
	 * @return the dirname of the specified file.
	 * @see #shortBasename(File)
	 * @see #largeBasename(File)
	 * @see #basename(File)
	 * @see #extension(File)
	 */
	@Pure
	public static URL dirname(File filename) {
		if (filename == null) {
			return null;
		}
		String parent = fromFileStandardToURLStandard(filename.getParent());
		try {
			if (parent == null || "".equals(parent)) { //$NON-NLS-1$
				if (filename.isAbsolute()) {
					return null;
				}
				return new URL(URISchemeType.FILE.name(), "", CURRENT_DIRECTORY); //$NON-NLS-1$
			}
			// Treat Windows specific.
			if (Pattern.matches("^" + URL_PATH_SEPARATOR + "?[a-zA-Z][:|]$", parent)) { //$NON-NLS-1$ //$NON-NLS-2$
				parent += URL_PATH_SEPARATOR;
			}
			return new URL(URISchemeType.FILE.name(), "", parent); //$NON-NLS-1$
		} catch (MalformedURLException exception) {
			return null;
		}
	}

	/** Replies the dirname of the specified file.
	 *
	 * @param filename is the name to parse.
	 * @return the dirname of the specified file.
	 * @see #shortBasename(URL)
	 * @see #largeBasename(URL)
	 * @see #basename(URL)
	 * @see #extension(URL)
	 */
	@Pure
	@SuppressWarnings("checkstyle:npathcomplexity")
	public static URL dirname(URL filename) {
		if (filename == null) {
			return null;
		}

		URL prefix = null;
		String path;
		if (isJarURL(filename)) {
			prefix = getJarURL(filename);
			path = fromFileStandardToURLStandard(getJarFile(filename));
		} else {
			path = filename.getPath();
		}

		if ("".equals(path)) { //$NON-NLS-1$
			return null;
		}

		int idx = path.lastIndexOf(URL_PATH_SEPARATOR_CHAR);
		if (idx == path.length() - 1) {
			idx = path.lastIndexOf(URL_PATH_SEPARATOR_CHAR, path.length() - 2);
		}

		if (idx < 0) {
			if (URISchemeType.getSchemeType(filename).isFileBasedScheme()) {
				path = CURRENT_DIRECTORY;
			} else {
				path = URL_PATH_SEPARATOR;
			}
		} else {
			path = path.substring(0, idx + 1);
		}

		try {
			if (prefix != null) {
				return toJarURL(prefix, path);
			}
			return new URI(filename.getProtocol(),
					filename.getUserInfo(),
					filename.getHost(),
					filename.getPort(),
					decodeHTMLEntities(path),
					null,
					null).toURL();
		} catch (Throwable exception) {
			//
		}

		try {
			return new URL(
					filename.getProtocol(),
					filename.getHost(),
					path);
		} catch (Throwable exception) {
			//
		}
		return null;
	}

	/** Replies the basename of the specified file with the extension.
	 *
	 * <p>Caution: This function does not support URL format.
	 *
	 * @param filename is the name to parse.
	 * @return the basename of the specified file with the extension.
	 */
	@Pure
	public static String largeBasename(String filename) {
		if (filename == null) {
			return null;
		}
		if (isWindowsNativeFilename(filename)) {
			return largeBasename(normalizeWindowsNativeFilename(filename));
		}

		try {
			return largeBasename(new URL(filename));
		} catch (Exception exception) {
			// No log
		}
		int end = filename.length();
		int idx;
		do {
			end--;
			idx = filename.lastIndexOf(File.separatorChar, end);
		}
		while (idx >= 0 && end >= 0 && idx >= end);
		if (idx < 0) {
			if (end < filename.length() - 1) {
				return filename.substring(0, end + 1);
			}
			return filename;
		}
		return filename.substring(idx + 1, end + 1);
	}

	/** Replies the basename of the specified file with the extension.
	 *
	 * @param filename is the name to parse.
	 * @return the basename of the specified file with the extension.
	 */
	@Pure
	@Inline("((($1) == null) ? null : ($1).getName())")
	public static String largeBasename(File filename) {
		if (filename == null) {
			return null;
		}
		return filename.getName();
	}

	/** Replies the basename of the specified file with the extension.
	 *
	 * @param filename is the name to parse.
	 * @return the basename of the specified file with the extension.
	 */
	@Pure
	public static String largeBasename(URL filename) {
		if (filename == null) {
			return null;
		}
		final String fullPath = filename.getPath();
		assert !isWindowsNativeFilename(fullPath);
		int idx;
		int end = fullPath.length();
		do {
			end--;
			idx = fullPath.lastIndexOf(URL_PATH_SEPARATOR_CHAR, end);
		}
		while (idx >= 0 && end >= 0 && idx >= end);
		final String result;
		if (idx < 0) {
			if (end < fullPath.length() - 1) {
				result = fullPath.substring(0, end + 1);
			} else {
				result = fullPath;
			}
		} else {
			result = fullPath.substring(idx + 1, end + 1);
		}
		return decodeHTMLEntities(result);
	}

	/** Reply the basename of the specified file without the last extension.
	 *
	 * <p>Caution: This function does not support URL format.
	 *
	 * @param filename is the name to parse.
	 * @return the basename of the specified file without the last extension.
	 * @see #shortBasename(String)
	 * @see #largeBasename(String)
	 */
	@Pure
	public static String basename(String filename) {
		if (filename == null) {
			return null;
		}
		if (isWindowsNativeFilename(filename)) {
			return basename(normalizeWindowsNativeFilename(filename));
		}

		try {
			return basename(new URL(filename));
		} catch (Exception exception) {
			// No log
		}
		int end = filename.length();
		int idx;
		do {
			end--;
			idx = filename.lastIndexOf(File.separatorChar, end);
		}
		while (idx >= 0 && end >= 0 && idx >= end);
		final String basename;
		if (idx < 0) {
			if (end < filename.length() - 1) {
				basename = filename.substring(0, end + 1);
			} else {
				basename = filename;
			}
		} else {
			basename = filename.substring(idx + 1, end + 1);
		}
		idx = basename.lastIndexOf(getFileExtensionCharacter());
		if (idx < 0) {
			return basename;
		}
		return basename.substring(0, idx);
	}

	/** Reply the basename of the specified file without the last extension.
	 *
	 * @param filename is the name to parse.
	 * @return the basename of the specified file without the last extension.
	 * @see #shortBasename(File)
	 * @see #largeBasename(File)
	 * @see #dirname(File)
	 * @see #extension(File)
	 */
	@Pure
	public static String basename(File filename) {
		if (filename == null) {
			return null;
		}
		final String largeBasename = filename.getName();
		final int idx = largeBasename.lastIndexOf(getFileExtensionCharacter());
		if (idx <= 0) {
			return largeBasename;
		}
		return largeBasename.substring(0, idx);
	}

	/** Reply the basename of the specified file without the last extension.
	 *
	 * @param filename is the name to parse.
	 * @return the basename of the specified file without the last extension.
	 * @see #shortBasename(URL)
	 * @see #largeBasename(URL)
	 * @see #dirname(URL)
	 * @see #extension(URL)
	 */
	@Pure
	public static String basename(URL filename) {
		if (filename == null) {
			return null;
		}
		final String largeBasename = filename.getPath();
		assert !isWindowsNativeFilename(largeBasename);
		int end = largeBasename.length();
		int idx;
		do {
			end--;
			idx = largeBasename.lastIndexOf(URL_PATH_SEPARATOR_CHAR, end);
		}
		while (idx >= 0 && end >= 0 && idx >= end);
		String basename;
		if (idx < 0) {
			if (end < largeBasename.length() - 1) {
				basename = largeBasename.substring(0, end + 1);
			} else {
				basename = largeBasename;
			}
		} else {
			basename = largeBasename.substring(idx + 1, end + 1);
		}
		idx = basename.lastIndexOf(getFileExtensionCharacter());
		if (idx >= 0) {
			basename = basename.substring(0, idx);
		}
		return decodeHTMLEntities(basename);
	}

	/** Reply the basename of the specified file without all the extensions.
	 *
	 * <p>Caution: This function does not support URL format.
	 *
	 * @param filename is the name to parse.
	 * @return the basename of the specified file without all the extensions.
	 */
	@Pure
	public static String shortBasename(String filename) {
		if (filename == null) {
			return null;
		}
		if (isWindowsNativeFilename(filename)) {
			return shortBasename(normalizeWindowsNativeFilename(filename));
		}

		try {
			return shortBasename(new URL(filename));
		} catch (Exception exception) {
			// No log
		}
		final String normalizedFilename = fromFileStandardToURLStandard(filename);
		int idx;
		int end = normalizedFilename.length();
		do {
			end--;
			idx = normalizedFilename.lastIndexOf(URL_PATH_SEPARATOR_CHAR, end);
		}
		while (idx >= 0 && end >= 0 && idx >= end);
		final String basename;
		if (idx < 0) {
			if (end < normalizedFilename.length() - 1) {
				basename = normalizedFilename.substring(0, end + 1);
			} else {
				basename = normalizedFilename;
			}
		} else {
			basename = normalizedFilename.substring(idx + 1, end + 1);
		}
		idx = basename.indexOf(getFileExtensionCharacter());
		if (idx < 0) {
			return basename;
		}
		return basename.substring(0, idx);
	}

	/** Reply the basename of the specified file without all the extensions.
	 *
	 * @param filename is the name to parse.
	 * @return the basename of the specified file without all the extensions.
	 */
	@Pure
	public static String shortBasename(File filename) {
		if (filename == null) {
			return null;
		}
		final String largeBasename = filename.getName();
		final int idx = largeBasename.indexOf(getFileExtensionCharacter());
		if (idx < 0) {
			return largeBasename;
		}
		return largeBasename.substring(0, idx);
	}

	/** Reply the basename of the specified file without all the extensions.
	 *
	 * @param filename is the name to parse.
	 * @return the basename of the specified file without all the extensions.
	 */
	@Pure
	public static String shortBasename(URL filename) {
		if (filename == null) {
			return null;
		}
		final String largeBasename = filename.getPath();
		assert !isWindowsNativeFilename(largeBasename);
		int idx;
		int end = largeBasename.length();
		do {
			end--;
			idx = largeBasename.lastIndexOf(URL_PATH_SEPARATOR_CHAR, end);
		}
		while (idx >= 0 && end >= 0 && idx >= end);
		String basename;
		if (idx < 0) {
			if (end < largeBasename.length() - 1) {
				basename = largeBasename.substring(0, end + 1);
			} else {
				basename = largeBasename;
			}
		} else {
			basename = largeBasename.substring(idx + 1, end + 1);
		}

		idx = basename.indexOf(getFileExtensionCharacter());
		if (idx >= 0) {
			basename = basename.substring(0, idx);
		}
		return decodeHTMLEntities(basename);
	}

	/** Reply the extension of the specified file.
	 *
	 * @param filename is the name to parse.
	 * @return the extension of the specified file
	 * @see #shortBasename(File)
	 * @see #largeBasename(File)
	 * @see #basename(File)
	 * @see #dirname(File)
	 * @see #extensions(File)
	 */
	@Pure
	public static String extension(File filename) {
		if (filename == null) {
			return null;
		}
		final String largeBasename = largeBasename(filename);
		final int idx = largeBasename.lastIndexOf(getFileExtensionCharacter());
		if (idx <= 0) {
			return ""; //$NON-NLS-1$
		}
		return largeBasename.substring(idx);
	}

	/** Reply the extension of the specified file.
	 *
	 * @param filename is the name to parse.
	 * @return the extension of the specified file
	 * @since 7.0
	 * @see #shortBasename(File)
	 * @see #largeBasename(File)
	 * @see #basename(File)
	 * @see #dirname(File)
	 * @see #extensions(File)
	 */
	@Pure
	public static String extension(String filename) {
		if (filename == null) {
			return null;
		}
		final String largeBasename = largeBasename(filename);
		final int idx = largeBasename.lastIndexOf(getFileExtensionCharacter());
		if (idx <= 0) {
			return ""; //$NON-NLS-1$
		}
		return largeBasename.substring(idx);
	}

	/** Reply the extension of the specified file.
	 *
	 * @param filename is the name to parse.
	 * @return the extension of the specified file
	 * @see #shortBasename(URL)
	 * @see #largeBasename(URL)
	 * @see #basename(URL)
	 * @see #dirname(URL)
	 * @see #extensions(URL)
	 */
	@Pure
	public static String extension(URL filename) {
		if (filename == null) {
			return null;
		}
		final String largeBasename = largeBasename(filename);
		final int idx = largeBasename.lastIndexOf(getFileExtensionCharacter());
		if (idx <= 0) {
			return ""; //$NON-NLS-1$
		}
		return decodeHTMLEntities(largeBasename.substring(idx));
	}

	/** Reply all the extensions of the specified file.
	 *
	 * @param filename is the name to parse.
	 * @return the extensions of the specified file
	 */
	@Pure
	public static String[] extensions(File filename) {
		if (filename == null) {
			return new String[0];
		}
		final String largeBasename = largeBasename(filename);
		final String[] parts = largeBasename.split(Pattern.quote(Character.toString(getFileExtensionCharacter())));
		if (parts.length <= 1) {
			return new String[0];
		}
		final String[] result = new String[parts.length - 1];
		System.arraycopy(parts, 1, result, 0, result.length);
		return result;
	}

	/** Reply all the extensions of the specified file.
	 *
	 * @param filename is the name to parse.
	 * @return the extensions of the specified file
	 * @since 7.0
	 */
	@Pure
	public static String[] extensions(String filename) {
		if (filename == null) {
			return new String[0];
		}
		final String largeBasename = largeBasename(filename);
		final String[] parts = largeBasename.split(Pattern.quote(Character.toString(getFileExtensionCharacter())));
		if (parts.length <= 1) {
			return new String[0];
		}
		final String[] result = new String[parts.length - 1];
		System.arraycopy(parts, 1, result, 0, result.length);
		return result;
	}

	/** Reply all the extensions of the specified file.
	 *
	 * @param filename is the name to parse.
	 * @return the extensions of the specified file
	 */
	@Pure
	public static String[] extensions(URL filename) {
		if (filename == null) {
			return new String[0];
		}
		final String largeBasename = largeBasename(filename);
		final String[] parts = largeBasename.split(Pattern.quote(Character.toString(getFileExtensionCharacter())));
		if (parts.length <= 1) {
			return new String[0];
		}
		final String[] result = new String[parts.length - 1];
		for (int i = 0; i < result.length; ++i) {
			result[i] = decodeHTMLEntities(parts[i + 1]);
		}
		return result;
	}

	/** Replies the parts of a path.
	 *
	 * @param filename is the name to parse.
	 * @return the parts of a path.
	 */
	@Pure
	public static String[] split(File filename) {
		if (filename == null) {
			return new String[0];
		}
		return filename.getPath().split(Pattern.quote(File.separator));
	}

	/** Replies the parts of a path.
	 *
	 * <p>If the input is {@code "http://www.arakhne.org/path/to/file.x.z.z"}, the replied paths
	 * are: {@code ""}, {@code "path"}, {@code "to"}, and {@code "file.x.z.z"}.
	 *
	 * <p>If the input is {@code "jar:file:/path1/archive.jar!/path2/file"}, the replied paths
	 * are: {@code ""}, {@code "path2"}, and {@code "file"}.
	 *
	 * @param filename is the name to parse.
	 * @return the parts of a path.
	 */
	@Pure
	public static String[] split(URL filename) {
		if (filename == null) {
			return new String[0];
		}
		if (isJarURL(filename)) {
			return split(getJarFile(filename));
		}
		final String path = filename.getPath();
		String[] tab = path.split(Pattern.quote(URL_PATH_SEPARATOR));
		if (tab.length >= 2 && "".equals(tab[0]) && Pattern.matches("^[a-zA-Z][:|]$", tab[1])) { //$NON-NLS-1$ //$NON-NLS-2$
			tab = Arrays.copyOfRange(tab, 1, tab.length);
			for (int i = 1; i < tab.length; ++i) {
				tab[i] = decodeHTMLEntities(tab[i]);
			}
		} else {
			for (int i = 0; i < tab.length; ++i) {
				tab[i] = decodeHTMLEntities(tab[i]);
			}
		}
		return tab;
	}

	/** Join the parts of a path and append them to the given File.
	 *
	 * @param fileBase is the file to put as prefix.
	 * @param elements are the path's elements to join.
	 * @return the result of the join of the path's elements.
	 */
	@Pure
	public static File join(File fileBase, String... elements) {
		if (fileBase == null) {
			return null;
		}
		final StringBuilder buf = new StringBuilder(fileBase.getPath());
		boolean empty;
		for (final String elt : elements) {
			empty = elt == null || elt.length() == 0;
			if (!empty) {
				assert elt != null;
				if (!elt.startsWith(File.separator)
						&& buf.length() >= 0
						&& buf.charAt(buf.length() - 1) != File.separatorChar) {
					buf.append(File.separatorChar);
				}
				buf.append(elt);
			}
		}
		return new File(buf.toString());
	}

	/** Join the parts of a path and append them to the given File.
	 *
	 * @param fileBase is the file to put as prefix.
	 * @param elements are the path's elements to join.
	 * @return the result of the join of the path's elements.
	 */
	@Pure
	public static File join(File fileBase, File... elements) {
		if (fileBase == null) {
			return null;
		}
		final StringBuilder buf = new StringBuilder(fileBase.getPath());
		for (final File elt : elements) {
			if (!elt.isAbsolute() && buf.length() >= 0 && buf.charAt(buf.length() - 1) != File.separatorChar) {
				buf.append(File.separatorChar);
			}
			buf.append(elt.getPath());
		}
		return new File(buf.toString());
	}

	/** Join the parts of a path and append them to the given URL.
	 *
	 * @param urlBase is the url to put as prefix.
	 * @param elements are the path's elements to join.
	 * @return the result of the join of the path's elements.
	 */
	@Pure
	public static URL join(URL urlBase, String... elements) {
		if (urlBase == null) {
			return null;
		}
		final StringBuilder buf = new StringBuilder(decodeHTMLEntities(urlBase.getPath().replaceFirst(
				Pattern.quote(URL_PATH_SEPARATOR) + "+$", "")));  //$NON-NLS-1$//$NON-NLS-2$
		boolean empty;
		for (final String elt : elements) {
			empty = elt == null || elt.length() == 0;
			if (!empty) {
				assert elt != null;
				if (!elt.startsWith(File.separator)
						&& (buf.length() == 0
						|| buf.charAt(buf.length() - 1) != URL_PATH_SEPARATOR_CHAR)) {
					buf.append(URL_PATH_SEPARATOR_CHAR);
				}
				buf.append(elt);
			}
		}
		try {
			if (isJarURL(urlBase)) {
				return new URL(
						urlBase.getProtocol(),
						urlBase.getHost(),
						urlBase.getPort(),
						buf.toString());
			}
			return new URI(
					urlBase.getProtocol(),
					urlBase.getUserInfo(),
					urlBase.getHost(),
					urlBase.getPort(),
					buf.toString(),
					decodeHTMLEntities(urlBase.getQuery()),
					urlBase.getRef()).toURL();
		} catch (Throwable exception) {
			//
		}
		try {
			return new URL(
					urlBase.getProtocol(),
					urlBase.getHost(),
					buf.toString());
		} catch (Throwable exception) {
			return null;
		}
	}

	/** Join the parts of a path and append them to the given URL.
	 *
	 * @param urlBase is the url to put as prefix.
	 * @param elements are the path's elements to join.
	 * @return the result of the join of the path's elements.
	 */
	@Pure
	public static URL join(URL urlBase, File... elements) {
		if (urlBase == null) {
			return null;
		}
		final StringBuilder buf = new StringBuilder(urlBase.getPath());
		for (final File elt : elements) {
			if (!elt.isAbsolute() && (buf.length() == 0 || buf.charAt(buf.length() - 1) != URL_PATH_SEPARATOR_CHAR)) {
				buf.append(URL_PATH_SEPARATOR_CHAR);
			}
			buf.append(fromFileStandardToURLStandard(elt));
		}
		try {
			if (isJarURL(urlBase)) {
				return new URL(
						urlBase.getProtocol(),
						urlBase.getHost(),
						urlBase.getPort(),
						buf.toString());
			}
			return new URI(
					urlBase.getProtocol(),
					urlBase.getUserInfo(),
					urlBase.getHost(),
					urlBase.getPort(),
					decodeHTMLEntities(buf.toString()),
					decodeHTMLEntities(urlBase.getQuery()),
					urlBase.getRef()).toURL();
		} catch (Throwable exception) {
			//
		}
		try {
			return new URL(
					urlBase.getProtocol(),
					urlBase.getHost(),
					buf.toString());
		} catch (Throwable exception) {
			return null;
		}
	}

	/** Replies if the specified file has the specified extension.
	 *
	 * <p>The test is dependent of the case-sensitive attribute of operating system.
	 *
	 * @param filename is the filename to parse
	 * @param extension is the extension to test.
	 * @return <code>true</code> if the given filename has the given extension,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public static boolean hasExtension(File filename, String extension) {
		if (filename == null) {
			return false;
		}
		assert extension != null;
		String extent = extension;
		if (!"".equals(extent) && !extent.startsWith(EXTENSION_SEPARATOR)) { //$NON-NLS-1$
			extent = EXTENSION_SEPARATOR + extent;
		}
		final String ext = extension(filename);
		if (ext == null) {
			return false;
		}
		if (isCaseSensitiveFilenameSystem()) {
			return ext.equals(extent);
		}
		return ext.equalsIgnoreCase(extent);
	}

	/** Replies if the specified file has the specified extension.
	 *
	 * <p>The test is dependent of the case-sensitive attribute of operating system.
	 *
	 * @param filename is the filename to parse
	 * @param extension is the extension to test.
	 * @return <code>true</code> if the given filename has the given extension,
	 *     otherwise <code>false</code>
	 * @since 7.0
	 */
	@Pure
	public static boolean hasExtension(String filename, String extension) {
		if (filename == null) {
			return false;
		}
		assert extension != null;
		String extent = extension;
		if (!"".equals(extent) && !extent.startsWith(EXTENSION_SEPARATOR)) { //$NON-NLS-1$
			extent = EXTENSION_SEPARATOR + extent;
		}
		final String ext = extension(filename);
		if (ext == null) {
			return false;
		}
		if (isCaseSensitiveFilenameSystem()) {
			return ext.equals(extent);
		}
		return ext.equalsIgnoreCase(extent);
	}

	/** Replies if the specified file has the specified extension.
	 *
	 * <p>The test is dependent of the case-sensitive attribute of operating system.
	 *
	 * @param filename is the filename to parse
	 * @param extension is the extension to test.
	 * @return <code>true</code> if the given filename has the given extension,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public static boolean hasExtension(URL filename, String extension) {
		if (filename == null) {
			return false;
		}
		assert extension != null;
		String extent = extension;
		if (!"".equals(extent) && !extent.startsWith(EXTENSION_SEPARATOR)) { //$NON-NLS-1$
			extent = EXTENSION_SEPARATOR + extent;
		}
		final String ext = extension(filename);
		if (ext == null) {
			return false;
		}
		if (isCaseSensitiveFilenameSystem()) {
			return ext.equals(extent);
		}
		return ext.equalsIgnoreCase(extent);
	}

	/** Remove the extension from the specified filename.
	 *
	 * @param filename is the filename to parse.
	 * @return the filename without the extension.
	 */
	@Pure
	public static File removeExtension(File filename) {
		if (filename == null) {
			return null;
		}
		final File dir = filename.getParentFile();
		final String name = filename.getName();
		final int idx = name.lastIndexOf(getFileExtensionCharacter());
		if (idx < 0) {
			return filename;
		}
		return new File(dir, name.substring(0, idx));
	}

	/** Remove the extension from the specified filename.
	 *
	 * @param filename is the filename to parse.
	 * @return the filename without the extension.
	 */
	@Pure
	public static URL removeExtension(URL filename) {
		if (filename == null) {
			return null;
		}
		final String path = filename.getPath().replaceFirst(Pattern.quote(URL_PATH_SEPARATOR)
				+ "+$", ""); //$NON-NLS-1$ //$NON-NLS-2$
		int idx = path.lastIndexOf(URL_PATH_SEPARATOR);
		final StringBuilder buf = new StringBuilder((idx < 0) ? "" : //$NON-NLS-1$
				decodeHTMLEntities(path.substring(0, idx + 1)));
		final String largeBasename = decodeHTMLEntities(path.substring(idx + 1));
		idx = largeBasename.lastIndexOf(getFileExtensionCharacter());
		if (idx < 0) {
			return filename;
		}
		buf.append(largeBasename.substring(0, idx));
		try {
			if (isJarURL(filename)) {
				return new URL(
						filename.getProtocol(),
						filename.getHost(),
						filename.getPort(),
						buf.toString());
			}
			return new URI(
					filename.getProtocol(),
					filename.getUserInfo(),
					filename.getHost(),
					filename.getPort(),
					buf.toString(),
					decodeHTMLEntities(filename.getQuery()),
					filename.getRef()).toURL();
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			//
		}
		try {
			return new URL(
					filename.getProtocol(),
					filename.getHost(),
					buf.toString());
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			return null;
		}
	}

	/** Replace the extension of the specified filename by the given extension.
	 * If the filename has no extension, the specified one will be added.
	 *
	 * @param filename is the filename to parse.
	 * @param extension is the extension to remove if it is existing.
	 * @return the filename without the extension.
	 */
	@Pure
	public static File replaceExtension(File filename, String extension) {
		if (filename == null) {
			return null;
		}
		if (extension == null) {
			return filename;
		}
		final File dir = filename.getParentFile();
		final String name = filename.getName();
		final int idx = name.lastIndexOf(getFileExtensionCharacter());
		final StringBuilder n = new StringBuilder();
		if (idx < 0) {
			n.append(name);
		} else {
			n.append(name.substring(0, idx));
		}
		if (!name.endsWith(EXTENSION_SEPARATOR) && !extension.startsWith(EXTENSION_SEPARATOR)) {
			n.append(EXTENSION_SEPARATOR);
		}
		n.append(extension);
		return new File(dir, n.toString());
	}

	/** Replace the extension of the specified filename by the given extension.
	 * If the filename has no extension, the specified one will be added.
	 *
	 * @param filename is the filename to parse.
	 * @param extension is the extension to remove if it is existing.
	 * @return the filename without the extension.
	 */
	@Pure
	@SuppressWarnings("checkstyle:npathcomplexity")
	public static URL replaceExtension(URL filename, String extension) {
		if (filename == null) {
			return null;
		}
		if (extension == null) {
			return filename;
		}
		String path = filename.getPath().replaceFirst(Pattern.quote(URL_PATH_SEPARATOR)
				+ "+$", ""); //$NON-NLS-1$ //$NON-NLS-2$
		if (!path.isEmpty()) {
			int idx = path.lastIndexOf(URL_PATH_SEPARATOR);
			final StringBuilder buf = new StringBuilder((idx < 0) ? "" : //$NON-NLS-1$
					decodeHTMLEntities(path.substring(0, idx + 1)));
			final String largeBasename = decodeHTMLEntities(path.substring(idx + 1));
			idx = largeBasename.lastIndexOf(getFileExtensionCharacter());
			if (idx < 0) {
				buf.append(largeBasename);
			} else {
				buf.append(largeBasename.substring(0, idx));
			}
			if (!"".equals(extension) && !extension.startsWith(EXTENSION_SEPARATOR)) { //$NON-NLS-1$
				buf.append(EXTENSION_SEPARATOR);
			}
			buf.append(extension);
			path = buf.toString();
		}
		try {
			if (isJarURL(filename)) {
				return new URL(
						filename.getProtocol(),
						filename.getHost(),
						filename.getPort(),
						path);
			}
			return new URI(
					filename.getProtocol(),
					filename.getUserInfo(),
					filename.getHost(),
					filename.getPort(),
					path,
					encodeHTMLEntities(filename.getQuery()),
					filename.getRef()).toURL();
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			//
		}
		try {
			return new URL(
					filename.getProtocol(),
					filename.getHost(),
					path);
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			return null;
		}
	}

	/** Add the extension of to specified filename.
	 * If the filename has already the given extension, the filename is not changed.
	 * If the filename has no extension or an other extension, the specified one is added.
	 *
	 * @param filename is the filename to parse.
	 * @param extension is the extension to remove if it is existing.
	 * @return the filename with the extension.
	 * @since 6.0
	 */
	@Pure
	public static File addExtension(File filename, String extension) {
		if (filename != null && !hasExtension(filename, extension)) {
			String extent = extension;
			if (!"".equals(extent) && !extent.startsWith(EXTENSION_SEPARATOR)) { //$NON-NLS-1$
				extent = EXTENSION_SEPARATOR + extent;
			}
			return new File(filename.getParentFile(), filename.getName() + extent);
		}
		return filename;
	}

	/** Add the extension of to specified filename.
	 * If the filename has already the given extension, the filename is not changed.
	 * If the filename has no extension or an other extension, the specified one is added.
	 *
	 * @param filename is the filename to parse.
	 * @param extension is the extension to remove if it is existing.
	 * @return the filename with the extension.
	 * @since 6.0
	 */
	@Pure
	public static URL addExtension(URL filename, String extension) {
		if (filename != null && !hasExtension(filename, extension)) {
			final String basename = largeBasename(filename);
			if (!basename.isEmpty()) {
				final StringBuilder buf = new StringBuilder(decodeHTMLEntities(
						filename.getPath()).replaceFirst(Pattern.quote(URL_PATH_SEPARATOR)
						+ "+$", "")); //$NON-NLS-1$ //$NON-NLS-2$
				if (!"".equals(extension) && !extension.startsWith(EXTENSION_SEPARATOR)) { //$NON-NLS-1$
					buf.append(EXTENSION_SEPARATOR);
				}
				buf.append(extension);
				final String path = buf.toString();
				try {
					if (isJarURL(filename)) {
						return new URL(
								filename.getProtocol(),
								filename.getHost(),
								filename.getPort(),
								path);
					}
					return new URI(
							filename.getProtocol(),
							filename.getUserInfo(),
							filename.getHost(),
							filename.getPort(),
							path,
							encodeHTMLEntities(filename.getQuery()),
							filename.getRef()).toURL();
				} catch (AssertionError e) {
					throw e;
				} catch (Throwable exception) {
					//
				}
				try {
					return new URL(
							filename.getProtocol(),
							filename.getHost(),
							path);
				} catch (AssertionError e) {
					throw e;
				} catch (Throwable exception) {
					return null;
				}
			}
		}
		return filename;
	}

	/** Delete the given directory and all its subdirectories.
	 * If the given {@code file} is a directory, its
	 * content and the {@code file} itself are recursivelly removed.
	 *
	 * @param file is the file to delete.
	 * @throws IOException when IO error occurs
	 * @since 6.0
	 * @see File#delete() for the deletion on a file only.
	 * @see File#mkdir() to create a directory.
	 * @see File#mkdirs() to create a directory and all its parents.
	 */
	public static void delete(File file) throws IOException {
		if (file != null) {
			final LinkedList<File> candidates = new LinkedList<>();
			candidates.add(file);
			File fl;
			File[] children;
			while (!candidates.isEmpty()) {
				fl = candidates.getFirst();
				if (fl.isDirectory()) {
					children = fl.listFiles();
					if (children != null && children.length > 0) {
						// Non empty directory
						for (final File c : children) {
							candidates.push(c);
						}
					} else {
						// empty directory
						candidates.removeFirst();
						fl.delete();
					}
				} else {
					// not a directory
					candidates.removeFirst();
					fl.delete();
				}
			}
		}
	}

	/** Delete the given directory and all its subdirectories when the JVM is exiting.
	 * If the given {@code file} is a directory, its
	 * content and the {@code file} itself are recursivelly removed.
	 *
	 * <p>To cancel this action, see {@link #undeleteOnExit(File)}.
	 *
	 * @param file is the file to delete.
	 * @throws IOException when cannot register the hook.
	 * @since 6.0
	 * @see File#deleteOnExit() for the deletion on a file only.
	 * @see File#mkdir() to create a directory.
	 * @see File#mkdirs() to create a directory and all its parents.
	 */
	public static void deleteOnExit(File file) throws IOException {
		if (file != null) {
			DELETE_ON_EXIT_HOOK.add(file);
		}
	}

	/** Cancel the deletion of the given directory and all its subdirectories when the JVM is exiting.
	 *
	 * @param file is the file to undelete.
	 * @throws IOException when the file cannot  be registered.
	 * @since 6.0
	 * @see #deleteOnExit(File)
	 * @see File#deleteOnExit() for the deletion on a file only.
	 * @see File#mkdir() to create a directory.
	 * @see File#mkdirs() to create a directory and all its parents.
	 */
	public static void undeleteOnExit(File file) throws IOException {
		if (file != null) {
			DELETE_ON_EXIT_HOOK.remove(file);
		}
	}

	/** Copy the first file into the second file.
	 *
	 * <p>The content of the second file will be lost.
	 * This copy function allows to do a copy between two different
	 * partitions.
	 *
	 * <p>If the {@code out} parameter is a directory, the output file
	 * is a file with the same basename as the input and inside
	 * the {@code out} directory.
	 *
	 * @param in is the file to copy.
	 * @param out is the target file
	 * @throws IOException in case of error.
	 * @since 6.0
	 * @see #copy(URL, File)
	 */
	public static void copy(File in, File out) throws IOException {
		assert in != null;
		assert out != null;

		File outFile = out;
		if (out.isDirectory()) {
			outFile = new File(out, largeBasename(in));
		}

		try (FileInputStream fis = new FileInputStream(in)) {
			try (FileOutputStream fos = new FileOutputStream(outFile)) {
				copy(fis, (int) in.length(), fos);
			}
		}
	}

	/** Copy the first file into the second file.
	 *
	 * <p>The content of the second file will be lost.
	 * This copy function allows to do a copy between two different
	 * partitions.
	 *
	 * @param in is the file to copy.
	 * @param out is the target file
	 * @throws IOException in case of error.
	 * @since 6.0
	 * @see #copy(File, File)
	 */
	public static void copy(URL in, File out) throws IOException {
		assert in != null;
		assert out != null;

		File outFile = out;
		if (out.isDirectory()) {
			outFile = new File(out, largeBasename(in));
		}

		final URLConnection connection = in.openConnection();
		try (FileOutputStream fos = new FileOutputStream(outFile)) {
			copy(
					connection.getInputStream(),
					connection.getContentLength(),
					fos);
		}
	}

	/** Copy the first file into the second file.
	 *
	 * <p>The content of the second file will be lost.
	 * This copy function allows to do a copy between two different
	 * partitions.
	 *
	 * @param in is the input stream to read.
	 * @param inSize is the total size of the input stream.
	 * @param out is the output stream.
	 * @throws IOException when copy error occurs.
	 * @since 6.2
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public static void copy(InputStream in, int inSize, FileOutputStream out) throws IOException {
		assert in != null;
		assert out != null;
		try (ReadableByteChannel inChannel = Channels.newChannel(in)) {
			try (FileChannel outChannel = out.getChannel()) {
				// apparently has trouble copying large files on Windows
				if (inSize < 0 || OperatingSystem.WIN.isCurrentOS()) {
					// magic number for Windows, 64Mb - 32Kb
					final int maxCount = (64 * 1024 * 1024) - (32 * 1024);
					long position = 0;
					long copied = 1;
					while ((inSize >= 0 && position < inSize) || (inSize < 0 && copied > 0)) {
						copied = outChannel.transferFrom(inChannel, position, maxCount);
						position += copied;
					}
				} else {
					outChannel.transferFrom(inChannel, 0, inSize);
				}
			}
		}
	}

	/** Replies the user home directory.
	 *
	 * @return the home directory of the current user.
	 * @throws FileNotFoundException when the home directory does not exist.
	 */
	@Pure
	public static File getUserHomeDirectory() throws FileNotFoundException {
		final String userHome = System.getProperty("user.home"); //$NON-NLS-1$
		if (userHome != null && !userHome.isEmpty()) {
			final File file = new File(userHome);
			if (file.isDirectory()) {
				return file;
			}
		}
		if (OperatingSystem.ANDROID.isCurrentOS()) {
			return join(File.listRoots()[0], Android.HOME_DIRECTORY);
		}
		throw new FileNotFoundException();
	}

	/** Replies the user home directory.
	 *
	 * @return the home directory of the current user.
	 */
	@Pure
	public static String getUserHomeDirectoryName() {
		final String userHome = System.getProperty("user.home"); //$NON-NLS-1$
		if ((userHome == null || userHome.isEmpty()) && (OperatingSystem.ANDROID.isCurrentOS())) {
			return join(File.listRoots()[0], Android.HOME_DIRECTORY).toString();
		}
		return userHome;
	}

	/** Replies the user configuration directory for the specified software.
	 *
	 * <p>On Unix operating systems, the user directory for a
	 * software is by default {@code $HOME/.software} where {@code software}
	 * is the given parameter (case-sensitive). On Windows&reg; operating systems, the user
	 * directory for a software is by default
	 * {@code C:<span>\</span>Documents and Settings<span>\</span>userName<span>\</span>Local Settings<span>\</span>
	 * Application Data<span>\</span>software}
	 * where {@code userName} is the login of the current user and {@code software}
	 * is the given parameter (case-insensitive).
	 *
	 * @param software is the name of the concerned software.
	 * @return the configuration directory of the software for the current user.
	 */
	@Pure
	public static File getUserConfigurationDirectoryFor(String software) {
		if (software == null || "".equals(software)) { //$NON-NLS-1$
			throw new IllegalArgumentException();
		}
		try {
			final File userHome = getUserHomeDirectory();
			final OperatingSystem os = OperatingSystem.getCurrentOS();
			if (os == OperatingSystem.ANDROID) {
				return join(userHome, "Android", Android.DATA_DIRECTORY, //$NON-NLS-1$
						Android.makeAndroidApplicationName(software));
			} else if (os.isUnixCompliant()) {
				return new File(new File(userHome, ".config"), software); //$NON-NLS-1$
			} else if (os == OperatingSystem.WIN) {
				final String userName = System.getProperty("user.name"); //$NON-NLS-1$
				if (userName != null && !"".equals(userName)) { //$NON-NLS-1$
					return join(
							new File("C:"), //$NON-NLS-1$
							"Documents and Settings", //$NON-NLS-1$
							userName,
							"Local Settings", "Application Data", //$NON-NLS-1$ //$NON-NLS-2$
							software);
				}
			}
			return new File(userHome, software);
		} catch (FileNotFoundException exception) {
			//
		}
		return null;
	}

	/** Replies the user configuration directory for the specified software.
	 *
	 * <p>On Unix operating systems, the user directory for a
	 * software is by default {@code $HOME/.software} where {@code software}
	 * is the given parameter (case-sensitive). On Windows&reg; operating systems, the user
	 * directory for a software is by default
	 * {@code C:<span>\</span>Documents and Settings<span>\</span>userName<span>\</span>Local Settings<span>\</span>
	 * Application Data<span>\</span>software}
	 * where {@code userName} is the login of the current user and {@code software}
	 * is the given parameter (case-insensitive).
	 *
	 * @param software is the name of the concerned software.
	 * @return the configuration directory of the software for the current user.
	 */
	@Pure
	public static String getUserConfigurationDirectoryNameFor(String software) {
		final File directory = getUserConfigurationDirectoryFor(software);
		if (directory != null) {
			return directory.getAbsolutePath();
		}
		return null;
	}

	/** Replies the system configuration directory for the specified software.
	 *
	 * <p>On Unix operating systems, the system directory for a
	 * software is by default {@code /etc/software} where {@code software}
	 * is the given parameter (case-sensitive). On Windows&reg; operating systems, the user
	 * directory for a software is by default
	 * {@code C:<span>\</span>Program Files<span>\</span>software}
	 * where {@code software} is the given parameter (case-insensitive).
	 *
	 * @param software is the name of the concerned software.
	 * @return the configuration directory of the software for the current user.
	 */
	@Pure
	public static File getSystemConfigurationDirectoryFor(String software) {
		if (software == null || "".equals(software)) { //$NON-NLS-1$
			throw new IllegalArgumentException();
		}
		final OperatingSystem os = OperatingSystem.getCurrentOS();
		if (os == OperatingSystem.ANDROID) {
			return join(File.listRoots()[0], Android.CONFIGURATION_DIRECTORY,
					Android.makeAndroidApplicationName(software));
		} else if (os.isUnixCompliant()) {
			final File[] roots = File.listRoots();
			return join(roots[0], "etc", software); //$NON-NLS-1$
		} else if (os == OperatingSystem.WIN) {
			File pfDirectory;
			for (final File root : File.listRoots()) {
				pfDirectory = new File(root, "Program Files"); //$NON-NLS-1$
				if (pfDirectory.isDirectory()) {
					return new File(root, software);
				}
			}
		}
		return null;
	}

	/** Replies the user configuration directory for the specified software.
	 *
	 * <p>On Unix operating systems, the system directory for a
	 * software is by default {@code /etc/software} where {@code software}
	 * is the given parameter (case-sensitive). On Windows&reg; operating systems, the user
	 * directory for a software is by default
	 * {@code C:<span>\</span>Program Files<span>\</span>software}
	 * where {@code software} is the given parameter (case-insensitive).
	 *
	 * @param software is the name of the concerned software.
	 * @return the configuration directory of the software for the current user.
	 */
	@Pure
	public static String getSystemConfigurationDirectoryNameFor(String software) {
		final File directory = getSystemConfigurationDirectoryFor(software);
		if (directory != null) {
			return directory.getAbsolutePath();
		}
		return null;
	}

	/** Replies the system shared library directory for the specified software.
	 *
	 * <p>On Unix operating systems, the system directory for a
	 * software is by default {@code /usr/lib/software} where {@code software}
	 * is the given parameter (case-sensitive). On Windows&reg; operating systems, the user
	 * directory for a software is by default
	 * {@code C:<span>\</span>Program Files<span>\</span>software}
	 * where {@code software} is the given parameter (case-insensitive).
	 *
	 * @param software is the name of the concerned software.
	 * @return the configuration directory of the software for the current user.
	 */
	@Pure
	public static File getSystemSharedLibraryDirectoryFor(String software) {
		if (software == null || "".equals(software)) { //$NON-NLS-1$
			throw new IllegalArgumentException();
		}
		final OperatingSystem os = OperatingSystem.getCurrentOS();
		if (os == OperatingSystem.ANDROID) {
			return join(File.listRoots()[0], Android.DATA_DIRECTORY,
					Android.makeAndroidApplicationName(software));
		} else if (os.isUnixCompliant()) {
			final File[] roots = File.listRoots();
			return join(roots[0], "usr", "lib", software); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (os == OperatingSystem.WIN) {
			File pfDirectory;
			for (final File root : File.listRoots()) {
				pfDirectory = new File(root, "Program Files"); //$NON-NLS-1$
				if (pfDirectory.isDirectory()) {
					return new File(root, software);
				}
			}
		}
		return null;
	}

	/** Replies the system shared library directory for the specified software.
	 *
	 * <p>On Unix operating systems, the system directory for a
	 * software is by default {@code /usr/lib/software} where {@code software}
	 * is the given parameter (case-sensitive). On Windows&reg; operating systems, the user
	 * directory for a software is by default
	 * {@code C:<span>\</span>Program Files<span>\</span>software}
	 * where {@code software} is the given parameter (case-insensitive).
	 *
	 * @param software is the name of the concerned software.
	 * @return the configuration directory of the software for the current user.
	 */
	@Pure
	public static String getSystemSharedLibraryDirectoryNameFor(String software) {
		final File f = getSystemSharedLibraryDirectoryFor(software);
		if (f == null) {
			return null;
		}
		return f.getAbsolutePath();
	}

	/** Convert a string which represents a local file into a File.
	 *
	 * <p>This function supports the naming standards coming for the different
	 * operating systems.
	 *
	 * @param filename the filename.
	 * @return the file, or <code>null</code> if the given filename is <code>null</code> or empty.
	 * @since 13.0
	 */
	@Pure
	public static File convertStringToFile(String filename) {
		if (filename == null || "".equals(filename)) { //$NON-NLS-1$
			return null;
		}
		if (isWindowsNativeFilename(filename)) {
			return normalizeWindowsNativeFilename(filename);
		}
		// Test for malformed filenames.
		return new File(extractLocalPath(filename).replaceAll(
				Pattern.quote(UNIX_SEPARATOR_STRING),
				Matcher.quoteReplacement(File.separator)));
	}

	/** Convert an URL which represents a local file or a resource into a File.
	 *
	 * @param url is the URL to convert.
	 * @return the file.
	 * @throws IllegalArgumentException is the URL was malformed.
	 */
	@Pure
	@SuppressWarnings("checkstyle:npathcomplexity")
	public static File convertURLToFile(URL url) {
		URL theUrl = url;
		if (theUrl == null) {
			return null;
		}
		if (URISchemeType.RESOURCE.isURL(theUrl)) {
			theUrl = Resources.getResource(decodeHTMLEntities(theUrl.getFile()));
			if (theUrl == null) {
				theUrl = url;
			}
		}
		URI uri;
		try {
			// this is the step that can fail, and so
			// it should be this step that should be fixed
			uri = theUrl.toURI();
		} catch (URISyntaxException e) {
			// OK if we are here, then obviously the URL did
			// not comply with RFC 2396. This can only
			// happen if we have illegal unescaped characters.
			// If we have one unescaped character, then
			// the only automated fix we can apply, is to assume
			// all characters are unescaped.
			// If we want to construct a URI from unescaped
			// characters, then we have to use the component
			// constructors:
			try {
				uri = new URI(theUrl.getProtocol(), theUrl.getUserInfo(),
						theUrl.getHost(), theUrl.getPort(),
						decodeHTMLEntities(theUrl.getPath()),
						decodeHTMLEntities(theUrl.getQuery()),
						theUrl.getRef());
			} catch (URISyntaxException e1) {
				// The URL is broken beyond automatic repair
				throw new IllegalArgumentException(Locale.getString("E1", theUrl)); //$NON-NLS-1$
			}

		}
		if (uri != null && URISchemeType.FILE.isURI(uri)) {
			final String auth = uri.getAuthority();
			String path = uri.getPath();
			if (path == null) {
				path = uri.getRawPath();
			}
			if (path == null) {
				path = uri.getSchemeSpecificPart();
			}
			if (path == null) {
				path = uri.getRawSchemeSpecificPart();
			}
			if (path != null) {
				if (auth == null || "".equals(auth)) { //$NON-NLS-1$
					// absolute filename in URI
					path = decodeHTMLEntities(path);
				} else {
					// relative filename in URI, extract it directly
					path = decodeHTMLEntities(auth + path);
				}
				if (Pattern.matches("^" + Pattern.quote(URL_PATH_SEPARATOR) //$NON-NLS-1$
						+ "[a-zA-Z][:|].*$", path)) { //$NON-NLS-1$
					path = path.substring(URL_PATH_SEPARATOR.length());
				}
				return new File(path);
			}
		}
		throw new IllegalArgumentException(Locale.getString("E2", theUrl)); //$NON-NLS-1$
	}

	/** Convert a string to an URL according to several rules.
	 *
	 * <p>The rules are (the first succeeded is replied):
	 * <ul>
	 * <li>if {@code urlDescription} is <code>null</code> or empty, return <code>null</code>;</li>
	 * <li>try to build an {@link URL} with {@code urlDescription} as parameter;</li>
	 * <li>if {@code allowResourceSearch} is <code>true</code> and
	 * {@code urlDescription} starts with {@code "resource:"}, call
	 * {@link Resources#getResource(String)} with the rest of the string as parameter;</li>
	 * <li>if {@code allowResourceSearch} is <code>true</code>, call
	 * {@link Resources#getResource(String)} with the {@code urlDescription} as
	 * parameter;</li>
	 * <li>assuming that the {@code urlDescription} is
	 * a filename, call {@link File#toURI()} to retreive an URI and then
	 * {@link URI#toURL()};</li>
	 * <li>If everything else failed, return <code>null</code>.</li>
	 * </ul>
	 *
	 * @param urlDescription is a string which is describing an URL.
	 * @param allowResourceSearch indicates if the convertion must take into account the Java resources.
	 * @return the URL.
	 * @throws IllegalArgumentException is the string could not be formatted to URL.
	 * @see Resources#getResource(String)
	 */
	@Pure
	public static URL convertStringToURL(String urlDescription, boolean allowResourceSearch) {
		return convertStringToURL(urlDescription, allowResourceSearch, true, true);
	}

	/** Convert a string to an URL according to several rules.
	 *
	 * <p>The rules are (the first succeeded is replied):
	 * <ul>
	 * <li>if {@code urlDescription} is <code>null</code> or empty, return <code>null</code>;</li>
	 * <li>try to build an {@link URL} with {@code urlDescription} as parameter;</li>
	 * <li>if {@code allowResourceSearch} is <code>true</code> and
	 * {@code urlDescription} starts with {@code "resource:"}, call
	 * {@link Resources#getResource(String)} with the rest of the string as parameter;</li>
	 * <li>if {@code allowResourceSearch} is <code>true</code>, call
	 * {@link Resources#getResource(String)} with the {@code urlDescription} as
	 * parameter;</li>
	 * <li>if {@code repliesFileURL} is <code>true</code> and
	 * assuming that the {@code urlDescription} is
	 * a filename, call {@link File#toURI()} to retreive an URI and then
	 * {@link URI#toURL()};</li>
	 * <li>If everything else failed, return <code>null</code>.</li>
	 * </ul>
	 *
	 * @param urlDescription is a string which is describing an URL.
	 * @param allowResourceSearch indicates if the convertion must take into account the Java resources.
	 * @param repliesFileURL indicates if urlDescription is allowed to be a filename.
	 * @return the URL.
	 * @throws IllegalArgumentException is the string could not be formatted to URL.
	 * @see Resources#getResource(String)
	 */
	@Pure
	public static URL convertStringToURL(String urlDescription, boolean allowResourceSearch, boolean repliesFileURL) {
		return convertStringToURL(urlDescription, allowResourceSearch, repliesFileURL, true);
	}

	/** Convert a string to an URL according to several rules.
	 *
	 * <p>The rules are (the first succeeded is replied):
	 * <ul>
	 * <li>if {@code urlDescription} is <code>null</code> or empty, return <code>null</code>;</li>
	 * <li>try to build an {@link URL} with {@code urlDescription} as parameter;</li>
	 * <li>if {@code allowResourceSearch} is <code>true</code> and
	 * {@code urlDescription} starts with {@code "resource:"}, call
	 * {@link Resources#getResource(String)} with the rest of the string as parameter;</li>
	 * <li>if {@code allowResourceSearch} is <code>true</code>, call
	 * {@link Resources#getResource(String)} with the {@code urlDescription} as
	 * parameter;</li>
	 * <li>if {@code repliesFileURL} is <code>true</code> and
	 * assuming that the {@code urlDescription} is
	 * a filename, call {@link File#toURI()} to retreive an URI and then
	 * {@link URI#toURL()};</li>
	 * <li>If everything else failed, return <code>null</code>.</li>
	 * </ul>
	 *
	 * @param urlDescription is a string which is describing an URL.
	 * @param allowResourceSearch indicates if the convertion must take into account the Java resources.
	 * @param repliesFileURL indicates if urlDescription is allowed to be a filename.
	 * @param supportWindowsPaths indicates if Windows paths should be treated in particular way.
	 * @return the URL.
	 * @throws IllegalArgumentException is the string could not be formatted to URL.
	 * @see Resources#getResource(String)
	 */
	@Pure
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity",
			"checkstyle:nestedifdepth"})
	static URL convertStringToURL(String urlDescription, boolean allowResourceSearch,
			boolean repliesFileURL, boolean supportWindowsPaths) {
		URL url = null;

		if (urlDescription != null && urlDescription.length() > 0)  {

			if (supportWindowsPaths && isWindowsNativeFilename(urlDescription)) {
				final File file = normalizeWindowsNativeFilename(urlDescription);
				if (file != null) {
					return convertFileToURL(file);
				}
			}

			if (URISchemeType.RESOURCE.isScheme(urlDescription)) {
				if (allowResourceSearch) {
					final String resourceName = urlDescription.substring(9);
					url = Resources.getResource(resourceName);
				}
			} else if (URISchemeType.FILE.isScheme(urlDescription)) {
				final File file = new File(URISchemeType.FILE.removeScheme(urlDescription));
				try {
					url = new URL(URISchemeType.FILE.name(), "", //$NON-NLS-1$
							fromFileStandardToURLStandard(file));
				} catch (MalformedURLException e) {
					//
				}
			} else {
				try {
					url = new URL(urlDescription);
				} catch (MalformedURLException exception) {
					// ignore error
				}
			}

			if (url == null) {
				if (allowResourceSearch) {
					url = Resources.getResource(urlDescription);
				}

				if (url == null && URISchemeType.RESOURCE.isScheme(urlDescription)) {
					return null;
				}

				if (url == null && repliesFileURL) {
					final String urlPart = URISchemeType.removeAnyScheme(urlDescription);
					// Try to parse a malformed JAR url:
					// jar:{malformed-url}!/{entry}
					if (URISchemeType.JAR.isScheme(urlDescription)) {
						final int idx = urlPart.indexOf(JAR_URL_FILE_ROOT);
						if (idx > 0) {
							final URL jarURL = convertStringToURL(urlPart.substring(0, idx), allowResourceSearch);
							if (jarURL != null) {
								try {
									url = toJarURL(jarURL, urlPart.substring(idx + 2));
								} catch (MalformedURLException exception) {
									//
								}
							}
						}
					}

					// Standard local file
					if (url == null) {
						try {
							final File file = new File(urlPart);
							url = new URL(URISchemeType.FILE.name(), "", //$NON-NLS-1$
									fromFileStandardToURLStandard(file));
						} catch (MalformedURLException e) {
							// ignore error
						}
					}
				}
			}
		}

		return url;
	}

	/**
	 * Make the given filename absolute from the given root if it is not already absolute.
	 *
	 * <table border="1" width="100%" summary="Cases">
	 * <thead>
	 * <tr>
	 * <td>{@code filename}</td><td>{@code current}</td><td>Result</td>
	 * </tr>
	 * </thead>
	 * <tr>
	 * <td><code>null</code></td>
	 * <td><code>null</code></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>null</code></td>
	 * <td><code>/myroot</code></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>/path/to/file</code></td>
	 * <td><code>null</code></td>
	 * <td><code>/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>path/to/file</code></td>
	 * <td><code>null</code></td>
	 * <td><code>path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>/path/to/file</code></td>
	 * <td><code>/myroot</code></td>
	 * <td><code>/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>path/to/file</code></td>
	 * <td><code>/myroot</code></td>
	 * <td><code>/myroot/path/to/file</code></td>
	 * </tr>
	 * </table>
	 *
	 * @param filename is the name to make absolute.
	 * @param current is the current directory which permits to make absolute.
	 * @return an absolute filename.
	 */
	@Pure
	public static File makeAbsolute(File filename, File current) {
		if (filename == null) {
			return null;
		}
		if (current != null && !filename.isAbsolute()) {
			try {
				return new File(current.getCanonicalFile(), filename.getPath());
			} catch (IOException exception) {
				return new File(current.getAbsoluteFile(), filename.getPath());
			}
		}
		return filename;
	}

	/**
	 * Make the given filename absolute from the given root if it is not already absolute.
	 *
	 * <table border="1" width="100%" summary="Cases">
	 * <thead>
	 * <tr>
	 * <td>{@code filename}</td><td>{@code current}</td><td>Result</td>
	 * </tr>
	 * </thead>
	 * <tr>
	 * <td><code>null</code></td>
	 * <td><code>null</code></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>null</code></td>
	 * <td><code>/myroot</code></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>file:/path/to/file</code></td>
	 * <td><code>null</code></td>
	 * <td><code>file:/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>file:path/to/file</code></td>
	 * <td><code>null</code></td>
	 * <td><code>file:path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>file:/path/to/file</code></td>
	 * <td><code>/myroot</code></td>
	 * <td><code>file:/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>file:path/to/file</code></td>
	 * <td><code>/myroot</code></td>
	 * <td><code>file:/myroot/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>http://host.com/path/to/file</code></td>
	 * <td><code>null</code></td>
	 * <td><code>http://host.com/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>http://host.com/path/to/file</code></td>
	 * <td><code>/myroot</code></td>
	 * <td><code>http://host.com/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>ftp://host.com/path/to/file</code></td>
	 * <td><code>null</code></td>
	 * <td><code>ftp://host.com/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>ftp://host.com/path/to/file</code></td>
	 * <td><code>/myroot</code></td>
	 * <td><code>ftp://host.com/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>ssh://host.com/path/to/file</code></td>
	 * <td><code>null</code></td>
	 * <td><code>ssh://host.com/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>ssh://host.com/path/to/file</code></td>
	 * <td><code>/myroot</code></td>
	 * <td><code>ssh://host.com/path/to/file</code></td>
	 * </tr>
	 * </table>
	 *
	 * @param filename is the name to make absolute.
	 * @param current is the current directory which permits to make absolute.
	 * @return an absolute filename.
	 */
	@Pure
	public static URL makeAbsolute(URL filename, File current) {
		try {
			return makeAbsolute(filename, current == null ? null : current.toURI().toURL());
		} catch (MalformedURLException exception) {
			//
		}
		return filename;
	}

	/**
	 * Make the given filename absolute from the given root if it is not already absolute.
	 *
	 * <table border="1" width="100%" summary="Cases">
	 * <thead>
	 * <tr>
	 * <td>{@code filename}</td><td>{@code current}</td><td>Result</td>
	 * </tr>
	 * </thead>
	 * <tr>
	 * <td><code>null</code></td>
	 * <td><code>null</code></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>null</code></td>
	 * <td><code>file:/myroot</code></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>null</code></td>
	 * <td><code>http://host.com/myroot</code></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>file:path/to/file</code></td>
	 * <td><code>null</code></td>
	 * <td><code>file:path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>file:path/to/file</code></td>
	 * <td><code>file:/myroot</code></td>
	 * <td><code>file:/myroot/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>file:path/to/file</code></td>
	 * <td><code>http://host.com/myroot</code></td>
	 * <td><code>http://host.com/myroot/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>file:/path/to/file</code></td>
	 * <td><code>null</code></td>
	 * <td><code>file:/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>file:/path/to/file</code></td>
	 * <td><code>file:/myroot</code></td>
	 * <td><code>file:/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>file:/path/to/file</code></td>
	 * <td><code>http://host.com/myroot</code></td>
	 * <td><code>file:/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>http://host2.com/path/to/file</code></td>
	 * <td><code>null</code></td>
	 * <td><code>http://host2.com/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>http://host2.com/path/to/file</code></td>
	 * <td><code>file:/myroot</code></td>
	 * <td><code>http://host2.com/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>http://host2.com/path/to/file</code></td>
	 * <td><code>http://host.com/myroot</code></td>
	 * <td><code>http://host2.com/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>ftp://host2.com/path/to/file</code></td>
	 * <td><code>null</code></td>
	 * <td><code>ftp://host2.com/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>ftp://host2.com/path/to/file</code></td>
	 * <td><code>file:/myroot</code></td>
	 * <td><code>ftp://host2.com/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>ftp://host2.com/path/to/file</code></td>
	 * <td><code>http://host.com/myroot</code></td>
	 * <td><code>ftp://host2.com/path/to/file</code></td>
	 * </tr>
	 * </table>
	 *
	 * @param filename is the name to make absolute.
	 * @param current is the current directory which permits to make absolute.
	 * @return an absolute filename.
	 */
	@Pure
	@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	public static URL makeAbsolute(URL filename, URL current) {
		if (filename == null) {
			return null;
		}
		final URISchemeType scheme = URISchemeType.getSchemeType(filename);
		switch (scheme) {
		case JAR:
			try {
				URL jarUrl = getJarURL(filename);
				jarUrl = makeAbsolute(jarUrl, current);
				final File jarFile = getJarFile(filename);
				return toJarURL(jarUrl, jarFile);
			} catch (MalformedURLException exception) {
				// Ignore error
			}
			break;
		case FILE:
			final File file = new File(filename.getFile());
			if (!file.isAbsolute() && current != null) {
				return join(current, file);
			}
			break;
		case UNSUPPORTED:
		case TELNET:
		case FTP:
		case HTTP:
		case HTTPS:
		case MAILTO:
		case NEWS:
		case RESOURCE:
		case SSH:
		default:
			// do not change the URL
		}
		return filename;
	}

	/**
	 * Make the given filename absolute from the given root if it is not already absolute.
	 *
	 * <table border="1" width="100%" summary="Cases">
	 * <thead>
	 * <tr>
	 * <td>{@code filename}</td><td>{@code current}</td><td>Result</td>
	 * </tr>
	 * </thead>
	 * <tr>
	 * <td><code>null</code></td>
	 * <td><code>null</code></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>null</code></td>
	 * <td><code>file:/myroot</code></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>null</code></td>
	 * <td><code>http://host.com/myroot</code></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>path/to/file</code></td>
	 * <td><code>null</code></td>
	 * <td><code>file:path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>path/to/file</code></td>
	 * <td><code>file:/myroot</code></td>
	 * <td><code>file:/myroot/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>path/to/file</code></td>
	 * <td><code>http://host.com/myroot</code></td>
	 * <td><code>http://host.com/myroot/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>/path/to/file</code></td>
	 * <td><code>null</code></td>
	 * <td><code>file:/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>/path/to/file</code></td>
	 * <td><code>file:/myroot</code></td>
	 * <td><code>file:/path/to/file</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>/path/to/file</code></td>
	 * <td><code>http://host.com/myroot</code></td>
	 * <td><code>file:/path/to/file</code></td>
	 * </tr>
	 * </table>
	 *
	 * @param filename is the name to make absolute.
	 * @param current is the current directory which permits to make absolute.
	 * @return an absolute filename.
	 * @since 5.0
	 */
	@Pure
	public static URL makeAbsolute(File filename, URL current) {
		if (filename != null) {
			if (!filename.isAbsolute() && current != null) {
				return join(current, filename);
			}
			try {
				return new URL(URISchemeType.FILE.toString() + fromFileStandardToURLStandard(filename.getAbsolutePath()));
			} catch (MalformedURLException exception) {
				// ignore error
			}
		}
		return null;
	}

	/** Replies the parent URL for the given URL.
	 *
	 * @param url the URL.
	 * @return the parent URL
	 * @throws MalformedURLException if the parent URL cannot be built.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public static URL getParentURL(URL url) throws MalformedURLException {
		if (url == null) {
			return url;
		}
		String path = url.getPath();
		final String prefix;
		final String parentStr;

		switch (URISchemeType.getSchemeType(url)) {
		case JAR:
			final int index = path.indexOf(JAR_URL_FILE_ROOT);
			assert index > 0;
			prefix = path.substring(0, index + 1);
			path = path.substring(index + 1);
			parentStr = URL_PATH_SEPARATOR;
			break;
		case FILE:
			prefix = null;
			parentStr = ".." + URL_PATH_SEPARATOR; //$NON-NLS-1$
			break;
		case FTP:
		case HTTP:
		case HTTPS:
		case MAILTO:
		case NEWS:
		case RESOURCE:
		case SSH:
		case TELNET:
		case UNSUPPORTED:
		default:
			prefix = null;
			parentStr = URL_PATH_SEPARATOR;
		}

		if (path == null || "".equals(path)) { //$NON-NLS-1$
			path = parentStr;
		}
		int index = path.lastIndexOf(URL_PATH_SEPARATOR_CHAR);
		if (index == -1) {
			path = parentStr;
		} else if (index == path.length() - 1) {
			index = path.lastIndexOf(URL_PATH_SEPARATOR_CHAR, index - 1);
			if (index == -1) {
				path = parentStr;
			} else {
				path = path.substring(0, index + 1);
			}
		} else {
			path = path.substring(0, index + 1);
		}

		if (prefix != null) {
			path = prefix + path;
		}

		return new URL(url.getProtocol(), url.getHost(), url.getPort(), path);
	}

	/** Test if the given filename is a local filename and extract
	 * the path component.
	 *
	 * @param filename the filename.
	 * @return the path.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	private static String extractLocalPath(String filename) {
		if (filename == null) {
			return null;
		}
		final int max = Math.min(FILE_PREFIX.length, filename.length());
		final int inner = max - 2;
		if (inner <= 0) {
			return filename;
		}
		boolean foundInner = false;
		boolean foundFull = false;
		for (int i = 0; i < max; ++i) {
			final char c = Character.toLowerCase(filename.charAt(i));
			if (FILE_PREFIX[i] != c) {
				foundFull = false;
				foundInner = i >= inner;
				break;
			}
			foundFull = true;
		}
		String fn;
		if (foundFull) {
			fn = filename.substring(FILE_PREFIX.length);
		} else if (foundInner) {
			fn = filename.substring(inner);
		} else {
			fn = filename;
		}
		if (Pattern.matches("^(" + Pattern.quote(URL_PATH_SEPARATOR) + "|" //$NON-NLS-1$ //$NON-NLS-2$
				+ Pattern.quote(WINDOWS_SEPARATOR_STRING) + ")[a-zA-Z][:|].*$", fn)) { //$NON-NLS-1$
			fn = fn.substring(1);
		}
		return fn;
	}

	/** Replies if the given string contains a Windows&reg; native long filename.
	 *
	 * <p>Long filenames (LFN), spelled "long file names" by Microsoft Corporation,
	 * are Microsoft's way of implementing filenames longer than the 8.3,
	 * or short-filename, naming scheme used in Microsoft DOS in their modern
	 * FAT and NTFS filesystems. Because these filenames can be longer than the
	 * 8.3 filename, they can be more descriptive. Another advantage of this
	 * scheme is that it allows for use of *nix files ending in (e.g. .jpeg,
	 * .tiff, .html, and .xhtml) rather than specialized shortened names
	 * (e.g. .jpg, .tif, .htm, .xht).
	 *
	 * <p>The long filename system allows a maximum length of 255 UTF-16 characters,
	 * including spaces and non-alphanumeric characters; excluding the following
	 * characters, which have special meaning within the command interpreter or
	 * the operating system kernel: <code>\</code> <code>/</code> <code>:</code>
	 * <code>*</code> <code>?</code> <code>"</code> <code>&lt;</code>
	 * <code>&gt;</code> <code>|</code>
	 *
	 * @param filename the filename to test.
	 * @return <code>true</code> if the given filename is a long filename,
	 *     otherwise <code>false</code>
	 * @see #normalizeWindowsNativeFilename(String)
	 */
	@Pure
	public static boolean isWindowsNativeFilename(String filename) {
		final String fn = extractLocalPath(filename);
		if (fn == null || fn.length() == 0) {
			return false;
		}
		final Pattern pattern = Pattern.compile(WINDOW_NATIVE_FILENAME_PATTERN);
		final Matcher matcher = pattern.matcher(fn);
		return matcher.matches();
	}

	/** Normalize the given string contains a Windows&reg; native long filename
	 * and replies a Java-standard version.
	 *
	 * <p>Long filenames (LFN), spelled "long file names" by Microsoft Corporation,
	 * are Microsoft's way of implementing filenames longer than the 8.3,
	 * or short-filename, naming scheme used in Microsoft DOS in their modern
	 * FAT and NTFS filesystems. Because these filenames can be longer than the
	 * 8.3 filename, they can be more descriptive. Another advantage of this
	 * scheme is that it allows for use of *nix files ending in (e.g. .jpeg,
	 * .tiff, .html, and .xhtml) rather than specialized shortened names
	 * (e.g. .jpg, .tif, .htm, .xht).
	 *
	 * <p>The long filename system allows a maximum length of 255 UTF-16 characters,
	 * including spaces and non-alphanumeric characters; excluding the following
	 * characters, which have special meaning within the command interpreter or
	 * the operating system kernel: <code>\</code> <code>/</code> <code>:</code>
	 * <code>*</code> <code>?</code> <code>"</code> <code>&lt;</code>
	 * <code>&gt;</code> <code>|</code>
	 *
	 * @param filename the filename to test.
	 * @return the normalized path or <code>null</code> if not a windows native path.
	 * @see #isWindowsNativeFilename(String)
	 */
	@Pure
	public static File normalizeWindowsNativeFilename(String filename) {
		final String fn = extractLocalPath(filename);
		if (fn != null && fn.length() > 0) {
			final Pattern pattern = Pattern.compile(WINDOW_NATIVE_FILENAME_PATTERN);
			final Matcher matcher = pattern.matcher(fn);
			if (matcher.find()) {
				return new File(fn.replace(WINDOWS_SEPARATOR_CHAR, File.separatorChar));
			}
		}
		return null;
	}

	/** Replies an URL for the given file and translate it into a
	 * resource URL if the given file is inside the classpath.
	 *
	 * @param file is the filename to translate.
	 * @return the URL which is corresponding to file, or <code>null</code> if
	 *     the url cannot be computed.
	 */
	@Pure
	public static URL convertFileToURL(File file) {
		if (file == null) {
			return null;
		}
		try {
			File thefile = file;
			if (isWindowsNativeFilename(file.toString())) {
				thefile = normalizeWindowsNativeFilename(file.toString());
				if (thefile == null) {
					thefile = file;
				}
			}
			final URL url;
			if (thefile.isAbsolute()) {
				url = thefile.toURI().toURL();
			} else {
				url = new URL("file:" + thefile.toString()); //$NON-NLS-1$
			}
			return toShortestURL(url);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/** Replies an URL for the given url and translate it into a
	 * resource URL if the given file is inside the classpath.
	 *
	 * @param url is the URL to make shortest.
	 * @return the URL which is corresponding to file, or <code>null</code> if
	 *     the url cannot be computed.
	 * @since 4.0
	 */
	@Pure
	public static URL toShortestURL(URL url) {
		if (url == null) {
			return null;
		}
		final String endPattern = URL_PATH_SEPARATOR + "$"; //$NON-NLS-1$
		final String shorterUrl = url.toExternalForm().replaceAll(endPattern, ""); //$NON-NLS-1$
		String sp;
		final Iterator<URL> classpath = ClasspathUtil.getClasspath();
		URL path;

		while (classpath.hasNext()) {
			path = classpath.next();
			sp = path.toExternalForm().replaceAll(endPattern, ""); //$NON-NLS-1$
			if (shorterUrl.startsWith(sp)) {
				final StringBuilder buffer = new StringBuilder("resource:"); //$NON-NLS-1$
				buffer.append(shorterUrl.substring(sp.length()).replaceAll(
						"^" + URL_PATH_SEPARATOR, "")); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					return new URL(buffer.toString());
				} catch (MalformedURLException e) {
					//
				}
			}
		}

		try {
			return new URL(shorterUrl);
		} catch (MalformedURLException e) {
			return url;
		}
	}

	/**
	 * Make the given filename relative to the given root path.
	 *
	 * @param filenameToMakeRelative is the name to make relative.
	 * @param rootPath is the root path from which the relative path will be set.
	 * @return a relative filename.
	 * @throws IOException when is is impossible to retreive canonical paths.
	 */
	@Pure
	public static File makeRelative(File filenameToMakeRelative, File rootPath) throws IOException {
		return makeRelative(filenameToMakeRelative, rootPath, true);
	}

	/**
	 * Make the given filename relative to the given root path.
	 *
	 * @param filenameToMakeRelative is the name to make relative.
	 * @param rootPath is the root path from which the relative path will be set.
	 * @param appendCurrentDirectorySymbol indicates if "./" should be append at the
	 *     begining of the relative filename.
	 * @return a relative filename.
	 * @throws IOException when is is impossible to retreive canonical paths.
	 */
	private static File makeRelative(File filenameToMakeRelative, File rootPath,
			boolean appendCurrentDirectorySymbol) throws IOException {

		if (filenameToMakeRelative == null || rootPath == null) {
			throw new IllegalArgumentException();
		}

		if (!filenameToMakeRelative.isAbsolute()) {
			return filenameToMakeRelative;
		}
		if (!rootPath.isAbsolute()) {
			return filenameToMakeRelative;
		}

		final File root = rootPath.getCanonicalFile();
		final File dir = filenameToMakeRelative.getParentFile().getCanonicalFile();

		final String[] parts1 = split(dir);
		final String[] parts2 = split(root);

		final String relPath = makeRelative(parts1, parts2, filenameToMakeRelative.getName());

		if (appendCurrentDirectorySymbol) {
			return new File(CURRENT_DIRECTORY, relPath);
		}
		return new File(relPath);
	}

	/**
	 * Make the given filename relative to the given root path.
	 *
	 * @param filenameToMakeRelative is the name to make relative.
	 * @param rootPath is the root path from which the relative path will be set.
	 * @return a relative filename.
	 * @throws IOException when is is impossible to retreive canonical paths.
	 * @since 6.0
	 */
	@Pure
	public static File makeRelative(File filenameToMakeRelative, URL rootPath) throws IOException {
		if (filenameToMakeRelative == null || rootPath == null) {
			throw new IllegalArgumentException();
		}

		if (!filenameToMakeRelative.isAbsolute()) {
			return filenameToMakeRelative;
		}

		final File dir = filenameToMakeRelative.getParentFile().getCanonicalFile();

		final String[] parts1 = split(dir);
		final String[] parts2 = split(rootPath);

		final String relPath = makeRelative(parts1, parts2, filenameToMakeRelative.getName());

		return new File(CURRENT_DIRECTORY, relPath);
	}

	/**
	 * Make the given filename relative to the given root path.
	 *
	 * @param filenameToMakeRelative is the name to make relative.
	 * @param rootPath is the root path from which the relative path will be set.
	 * @return a relative filename.
	 * @throws IOException when is is impossible to retreive canonical paths.
	 * @since 6.0
	 */
	@Pure
	public static File makeRelative(URL filenameToMakeRelative, URL rootPath) throws IOException {
		if (filenameToMakeRelative == null || rootPath == null) {
			throw new IllegalArgumentException();
		}

		final String basename = largeBasename(filenameToMakeRelative);
		final URL dir = dirname(filenameToMakeRelative);

		final String[] parts1 = split(dir);
		final String[] parts2 = split(rootPath);

		final String relPath = makeRelative(parts1, parts2, basename);

		return new File(CURRENT_DIRECTORY, relPath);
	}

	@SuppressWarnings("checkstyle:npathcomplexity")
	private static String makeRelative(String[] parts1, String[] parts2, String basename) {
		int firstDiff = -1;

		for (int i = 0; firstDiff < 0 && i < parts1.length && i < parts2.length; ++i) {
			if (!parts1[i].equals(parts2[i])) {
				firstDiff = i;
			}
		}

		final StringBuilder result = new StringBuilder();
		if (firstDiff < 0) {
			firstDiff = Math.min(parts1.length, parts2.length);
		}

		for (int i = firstDiff; i < parts2.length; ++i) {
			if (result.length() > 0) {
				result.append(File.separator);
			}
			result.append(PARENT_DIRECTORY);
		}

		for (int i = firstDiff; i < parts1.length; ++i) {
			if (result.length() > 0) {
				result.append(File.separator);
			}
			result.append(parts1[i]);
		}

		if (result.length() > 0) {
			result.append(File.separator);
		}
		result.append(basename);

		return result.toString();
	}

	/** Make the given URL canonical.
	 *
	 * <p>A canonical pathname is both absolute and unique. This method maps
	 * the pathname to its unique form.  This typically involves removing redundant names
	 * such as <tt>"."</tt> and <tt>".."</tt> from the pathname.
	 *
	 * @param url is the URL to make canonical
	 * @return the canonical form of the given URL.
	 * @since 6.0
	 */
	@Pure
	public static URL makeCanonicalURL(URL url) {
		if (url != null) {
			final String[] pathComponents = url.getPath().split(Pattern.quote(URL_PATH_SEPARATOR));

			final List<String> canonicalPath = new LinkedList<>();
			for (final String component : pathComponents) {
				if (!CURRENT_DIRECTORY.equals(component)) {
					if (PARENT_DIRECTORY.equals(component)) {
						if (!canonicalPath.isEmpty()) {
							canonicalPath.remove(canonicalPath.size() - 1);
						} else {
							canonicalPath.add(component);
						}
					} else {
						canonicalPath.add(component);
					}
				}
			}

			final StringBuilder newPathBuffer = new StringBuilder();
			boolean isFirst = true;
			for (final String component : canonicalPath) {
				if (!isFirst) {
					newPathBuffer.append(URL_PATH_SEPARATOR_CHAR);
				} else {
					isFirst = false;
				}
				newPathBuffer.append(component);
			}

			try {
				return new URI(
						url.getProtocol(),
						url.getUserInfo(),
						url.getHost(),
						url.getPort(),
						newPathBuffer.toString(),
						url.getQuery(),
						url.getRef()).toURL();
			} catch (MalformedURLException | URISyntaxException exception) {
				//
			}

			try {
				return new URL(
						url.getProtocol(),
						url.getHost(),
						newPathBuffer.toString());
			} catch (Throwable exception) {
				//
			}
		}
		return url;
	}

	/**
	 * Create a zip file from the given input file.
	 *
	 * @param input the name of the file to compress.
	 * @param output the name of the ZIP file to create.
	 * @throws IOException when ziiping is failing.
	 * @since 6.2
	 */
	public static void zipFile(File input, File output) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(output)) {
			zipFile(input, fos);
		}
	}


	/**
	 * Create a zip file from the given input file.
	 * If the input file is a directory, the content of the directory is zipped.
	 * If the input file is a standard file, it is zipped.
	 *
	 * @param input the name of the file to compress.
	 * @param output the name of the ZIP file to create.
	 * @throws IOException when ziiping is failing.
	 * @since 6.2
	 */
	@SuppressWarnings("checkstyle:npathcomplexity")
	public static void zipFile(File input, OutputStream output) throws IOException {

		try (ZipOutputStream zos = new ZipOutputStream(output)) {
			if (input == null) {
				return;
			}

			final LinkedList<File> candidates = new LinkedList<>();
			candidates.add(input);

			final byte[] buffer = new byte[BUFFER_SIZE];
			int len;
			File file;
			File relativeFile;
			String zipFilename;

			final File rootDirectory = (input.isDirectory()) ? input : input.getParentFile();

			while (!candidates.isEmpty()) {
				file = candidates.removeFirst();
				assert file != null;

				if (file.getAbsoluteFile().equals(rootDirectory.getAbsoluteFile())) {
					relativeFile = null;
				} else {
					relativeFile = makeRelative(file, rootDirectory, false);
				}

				if (file.isDirectory()) {
					if (relativeFile != null) {
						zipFilename = fromFileStandardToURLStandard(relativeFile) + URL_PATH_SEPARATOR;
						final ZipEntry zipEntry = new ZipEntry(zipFilename);
						zos.putNextEntry(zipEntry);
						zos.closeEntry();
					}
					candidates.addAll(Arrays.asList(file.listFiles()));
				} else if (relativeFile != null) {
					try (FileInputStream fis = new FileInputStream(file)) {
						zipFilename = fromFileStandardToURLStandard(relativeFile);
						final ZipEntry zipEntry = new ZipEntry(zipFilename);
						zos.putNextEntry(zipEntry);
						while ((len = fis.read(buffer)) > 0) {
							zos.write(buffer, 0, len);
						}
						zos.closeEntry();
					}
				}
			}
		}
	}

	/**
	 * Unzip the given stream and write out the file in the output.
	 * If the input file is a directory, the content of the directory is zipped.
	 * If the input file is a standard file, it is zipped.
	 *
	 * @param input the ZIP file to uncompress.
	 * @param output the uncompressed file to create.
	 * @throws IOException when uncompressing is failing.
	 * @since 6.2
	 */
	public static void unzipFile(InputStream input, File output) throws IOException {
		if (output == null) {
			return;
		}
		output.mkdirs();
		if (!output.isDirectory()) {
			throw new IOException(Locale.getString("E3", output)); //$NON-NLS-1$
		}

		try (ZipInputStream zis = new ZipInputStream(input)) {
			final byte[] buffer = new byte[BUFFER_SIZE];
			int len;
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				final String name = zipEntry.getName();
				final File outFile = new File(output, name).getCanonicalFile();
				if (zipEntry.isDirectory()) {
					outFile.mkdirs();
				} else {
					outFile.getParentFile().mkdirs();
					try (FileOutputStream fos = new FileOutputStream(outFile)) {
						while ((len = zis.read(buffer)) > 0) {
							fos.write(buffer, 0, len);
						}
					}
				}
				zipEntry = zis.getNextEntry();
			}
		}
	}

	/**
	 * Unzip a file into the output directory.
	 *
	 * @param input the ZIP file to uncompress.
	 * @param output the uncompressed file to create.
	 * @throws IOException when uncompressing is failing.
	 * @since 6.2
	 */
	public static void unzipFile(File input, File output) throws IOException {
		try (FileInputStream fis = new FileInputStream(input)) {
			unzipFile(fis, output);
		}
	}

	/** Create an empty directory in the default temporary-file directory, using
	 * the given prefix and suffix to generate its name.  Invoking this method
	 * is equivalent to invoking <code>{@link #createTempDirectory(java.lang.String,
	 * java.lang.String, java.io.File)
	 * createTempDirectory(prefix,&nbsp;suffix,&nbsp;null)}</code>.
	 *
	 * @param  prefix is the prefix string to be used in generating the file's
	 *                    name; must be at least three characters long
	 *
	 * @param  suffix is the suffix string to be used in generating the file's
	 *                    name; may be <code>null</code>, in which case the
	 *                    suffix <code>".tmp"</code> will be used
	 * @return  An abstract pathname denoting a newly-created empty file
	 * @throws  IllegalArgumentException
	 *          If the <code>prefix</code> argument contains fewer than three
	 *          characters
	 * @throws  IOException  If a file could not be created
	 * @throws  SecurityException
	 *          If a security manager exists and its <code>{@link
	 *          java.lang.SecurityManager#checkWrite(java.lang.String)}</code>
	 *          method does not allow a file to be created
	 * @since 6.2
	 */
	public static File createTempDirectory(String prefix, String suffix) throws IOException {
		return createTempDirectory(prefix, suffix, null);
	}

	/** Creates a new empty directory in the specified directory, using the
	 * given prefix and suffix strings to generate its name.  If this method
	 * returns successfully then it is guaranteed that:
	 * <ol>
	 * <li> The directory denoted by the returned abstract pathname did not exist
	 *      before this method was invoked, and
	 * <li> Neither this method nor any of its variants will return the same
	 *      abstract pathname again in the current invocation of the virtual
	 *      machine.
	 * </ol>
	 *
	 * <p>This method provides only part of a temporary-file facility.  To arrange
	 * for a file created by this method to be deleted automatically, use the
	 * <code>{@link #deleteOnExit}</code> method.
	 *
	 * <p>The <code>prefix</code> argument must be at least three characters
	 * long.  It is recommended that the prefix be a short, meaningful string
	 * such as <code>"hjb"</code> or <code>"mail"</code>.  The
	 * <code>suffix</code> argument may be <code>null</code>, in which case the
	 * suffix <code>".tmp"</code> will be used.
	 *
	 * <p>To create the new directory, the prefix and the suffix may first be
	 * adjusted to fit the limitations of the underlying platform.  If the
	 * prefix is too long then it will be truncated, but its first three
	 * characters will always be preserved.  If the suffix is too long then it
	 * too will be truncated, but if it begins with a period character
	 * (<code>'.'</code>) then the period and the first three characters
	 * following it will always be preserved.  Once these adjustments have been
	 * made the name of the new file will be generated by concatenating the
	 * prefix, five or more internally-generated characters, and the suffix.
	 *
	 * <p>If the <code>directory</code> argument is <code>null</code> then the
	 * system-dependent default temporary-file directory will be used.  The
	 * default temporary-file directory is specified by the system property
	 * <code>java.io.tmpdir</code>.  On UNIX systems the default value of this
	 * property is typically <code>"/tmp"</code> or <code>"/var/tmp"</code>; on
	 * Microsoft Windows systems it is typically <code>"C:\\WINNT\\TEMP"</code>.  A different
	 * value may be given to this system property when the Java virtual machine
	 * is invoked, but programmatic changes to this property are not guaranteed
	 * to have any effect upon the temporary directory used by this method.
	 *
	 * @param  prefix is the prefix string to be used in generating the file's
	 *                    name; must be at least three characters long
	 *
	 * @param  suffix is the suffix string to be used in generating the file's
	 *                    name; may be <code>null</code>, in which case the
	 *                    suffix <code>".tmp"</code> will be used
	 * @param  directory is the directory in which the file is to be created, or
	 *                    <code>null</code> if the default temporary-file
	 *                    directory is to be used
	 * @return  An abstract pathname denoting a newly-created empty file
	 * @throws  IllegalArgumentException
	 *          If the <code>prefix</code> argument contains fewer than three
	 *          characters
	 * @throws  IOException  If a file could not be created
	 * @throws  SecurityException
	 *          If a security manager exists and its <code>{@link
	 *          java.lang.SecurityManager#checkWrite(java.lang.String)}</code>
	 *          method does not allow a file to be created
	 * @since 6.2
	 */
	public static File createTempDirectory(String prefix, String suffix, File directory) throws IOException {
		if (prefix == null) {
			throw new NullPointerException();
		}
		if (prefix.length() < 3) {
			throw new IllegalArgumentException(Locale.getString("E4", 3, prefix)); //$NON-NLS-1$
		}
		final String string = (suffix == null) ? ".tmp" : suffix; //$NON-NLS-1$
		final File targetDirectory;
		if (directory == null) {
			targetDirectory = new File(System.getProperty("java.io.tmpdir")); //$NON-NLS-1$
		} else {
			targetDirectory = directory;
		}
		File filename;
		do {
			long index = RANDOM.nextLong();
			if (index == Long.MIN_VALUE) {
				// corner case
				index = 0;
			} else {
				index = Math.abs(index);
			}
			final StringBuilder buffer = new StringBuilder();
			buffer.append(prefix);
			buffer.append(Long.toString(index));
			buffer.append(string);
			filename = new File(targetDirectory, buffer.toString());
		}
		while (!filename.mkdirs());
		return filename;
	}

	/** Hook to recursively delete files on JVM exit.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 6.0
	 */
	private static class DeleteOnExitHook extends Thread {

		private List<File> filesToDelete;

		/** Construct the hook.
		 */
		DeleteOnExitHook() {
			setName("DeleteOnExitHook"); //$NON-NLS-1$
		}

		@Override
		public void run() {
			synchronized (this) {
				if (this.filesToDelete != null) {
					for (final File f : this.filesToDelete) {
						try {
							delete(f);
						} catch (IOException e) {
							// Ignore error
						}
					}
					this.filesToDelete.clear();
					this.filesToDelete = null;
				}
			}
		}

		/** Add a file to delete.
		 *
		 * @param file the file to delete.
		 */
		public void add(File file) {
			assert file != null;
			synchronized (this) {
				if (this.filesToDelete == null) {
					this.filesToDelete = new LinkedList<>();
					Runtime.getRuntime().addShutdownHook(this);
				}
				this.filesToDelete.add(file);
			}
		}

		/** Remove a file to delete.
		 *
		 * @param file the file.
		 */
		public void remove(File file) {
			synchronized (this) {
				if (this.filesToDelete != null) {
					this.filesToDelete.remove(file);
					if (this.filesToDelete.isEmpty()) {
						this.filesToDelete = null;
						Runtime.getRuntime().removeShutdownHook(this);
					}
				}
			}
		}

	}

}
