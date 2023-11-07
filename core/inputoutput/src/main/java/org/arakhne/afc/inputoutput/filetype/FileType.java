/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.inputoutput.filetype;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.activation.FileTypeMap;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import org.arakhne.afc.inputoutput.mime.MimeName;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** An utility class that permits to detect the type of a file.
 * This class determines the file type according to the file content
 * (Unix approach) and not according to
 * the filename extension (Windows&reg; approach). A MIME constant
 * is replied for the detected type.
 *
 * <p>This class implements equivalent functionalities as
 * the {@code file} Linux command.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class FileType {

	private static final String IMAGE = "image"; //$NON-NLS-1$

	private FileType() {
		//
	}

	/** Be sure that the default FileTypeMap is a
	 * {@link ContentFileTypeMap}.
	 *
	 * @return the default content type manager.
	 */
	public static ContentFileTypeMap ensureContentTypeManager() {
		FileTypeMap defaultMap = FileTypeMap.getDefaultFileTypeMap();
		if (!(defaultMap instanceof ContentFileTypeMap)) {
			defaultMap = new ContentFileTypeMap(defaultMap);
			FileTypeMap.setDefaultFileTypeMap(defaultMap);
		}
		return (ContentFileTypeMap) defaultMap;
	}

	/** Register a MIME type of the given file.
	 *
	 * @param magicNumber is the string that identify the type of the content.
	 */
	public static void addContentType(MagicNumber magicNumber) {
		final ContentFileTypeMap map = ensureContentTypeManager();
		map.addContentType(magicNumber);
	}

	/** Replies the MIME type of the given file.
	 *
	 * @param filename is the name of the file to test.
	 * @return the MIME type of the given file.
	 */
	public static String getContentType(File filename) {
		final ContentFileTypeMap map = ensureContentTypeManager();
		return map.getContentType(filename);
	}

	/** Replies the MIME type of the given file.
	 *
	 * @param filename is the name of the file to test.
	 * @return the MIME type of the given file.
	 */
	public static String getContentType(String filename) {
		final ContentFileTypeMap map = ensureContentTypeManager();
		return map.getContentType(filename);
	}

	/** Replies the MIME type of the given file.
	 *
	 * @param filename is the name of the file to test.
	 * @return the MIME type of the given file.
	 */
	public static String getContentType(URL filename) {
		final ContentFileTypeMap map = ensureContentTypeManager();
		return map.getContentType(filename);
	}

	/** Replies the version of the format of the given file.
	 *
	 * @param filename is the name of the file to test.
	 * @return the format version for the given file.
	 */
	public static String getFormatVersion(File filename) {
		final ContentFileTypeMap map = ensureContentTypeManager();
		return map.getFormatVersion(filename);
	}

	/** Replies the version of the format of the given file.
	 *
	 * @param filename is the name of the file to test.
	 * @return the format version for the given file.
	 */
	public static String getFormatVersion(String filename) {
		final ContentFileTypeMap map = ensureContentTypeManager();
		return map.getFormatVersion(filename);
	}

	/** Replies the version of the format of the given file.
	 *
	 * @param filename is the name of the file to test.
	 * @return the format version for the given file.
	 */
	public static String getFormatVersion(URL filename) {
		final ContentFileTypeMap map = ensureContentTypeManager();
		return map.getFormatVersion(filename);
	}

	/** Replies if the specified MIME type corresponds to an image.
	 *
	 * @param mime is the MIME type to test.
	 * @return {@code true} if the given MIME type is corresponding to an image,
	 *     otherwise {@code false}
	 */
	public static boolean isImage(String mime) {
		try {
			final MimeType type = new MimeType(mime);
			return IMAGE.equalsIgnoreCase(type.getPrimaryType());
		} catch (MimeTypeParseException e) {
			//
		}
		return false;
	}

	/** Replies if the given file is compatible with the given MIME type.
	 *
	 * @param filename is the name of the file to test.
	 * @param desiredMimeType is the desired MIME type.
	 * @return {@code true} if the given file has type equal to the given MIME type,
	 *     otherwise {@code false}.
	 */
	public static boolean isContentType(File filename, String desiredMimeType) {
		final ContentFileTypeMap map = ensureContentTypeManager();
		return map.isContentType(filename, desiredMimeType);
	}

	/** Replies if the given file is compatible with the given MIME type.
	 *
	 * @param filename is the name of the file to test.
	 * @param desiredMimeType is the desired MIME type.
	 * @return {@code true} if the given file has type equal to the given MIME type,
	 *     otherwise {@code false}.
	 */
	public static boolean isContentType(String filename, String desiredMimeType) {
		final ContentFileTypeMap map = ensureContentTypeManager();
		return map.isContentType(filename, desiredMimeType);
	}

	/** Replies if the given file is compatible with the given MIME type.
	 *
	 * @param filename is the name of the file to test.
	 * @param desiredMimeType is the desired MIME type.
	 * @return {@code true} if the given file has type equal to the given MIME type,
	 *     otherwise {@code false}.
	 */
	public static boolean isContentType(URL filename, String desiredMimeType) {
		final ContentFileTypeMap map = ensureContentTypeManager();
		return map.isContentType(filename, desiredMimeType);
	}

	/** Map that contains the file content type definitions.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class ContentFileTypeMap extends FileTypeMap {

		private final SoftReference<FileTypeMap> parent;

		private final Map<String, Collection<MagicNumber>> typedNumbers = new TreeMap<>();

		private final Collection<MagicNumber> defaultNumbers = new ArrayList<>();

		/** Constructor.
		 * @param parent is the parent of this content type map, which will be invoked if this map does not know the answer.
		 */
		public ContentFileTypeMap(FileTypeMap parent) {
			this.parent = new SoftReference<>(parent);
		}

		/** Constructor.
		 */
		public ContentFileTypeMap() {
			this.parent = new SoftReference<>(FileTypeMap.getDefaultFileTypeMap());
		}

		@Override
		public String getContentType(File filename) {
			if (!filename.exists()) {
				return null;
			}
			try {
				final MimeType type = getMimeType(filename.toURI().toURL());
				if (type != null) {
					return type.toString();
				}
			} catch (Exception e) {
				//
			}
			final FileTypeMap lparent = this.parent.get();
			if (lparent != null) {
				return lparent.getContentType(filename);
			}
			return MimeName.MIME_OCTET_STREAM.getMimeConstant();
		}

		@Override
		public String getContentType(String filename) {
			return getContentType(new File(filename));
		}

		/** Replies the mime type of the specified url.
		 *
		 * @param url is the location of the file to test.
		 * @return the MIME type of the given file.
		 */
		public String getContentType(URL url) {
			try {
				final MimeType type = getMimeType(url);
				if (type != null) {
					return type.toString();
				}
			} catch (Exception e) {
				//
			}
			return MimeName.MIME_OCTET_STREAM.getMimeConstant();
		}

		/** Replies if the given file has the given MIME type.
		 *
		 * @param filename is the filename of the file to read.
		 * @param mimeType is the restriction type.
		 * @return {@code true} if the file has the given MIME type,
		 *     otherwise {@code false}
		 */
		public boolean isContentType(File filename, String mimeType) {
			assert filename != null : AssertMessages.notNullParameter(0);
			assert mimeType != null && !mimeType.isEmpty() : AssertMessages.notNullParameter(1);
			if (!filename.exists()) {
				return false;
			}
			try {
				final MimeType mtype = new MimeType(mimeType);
				if (isMimeType(filename.toURI().toURL(), mtype)) {
					return true;
				}
			} catch (Exception e) {
				// silently ignore errors
			}
			final FileTypeMap lparent = this.parent.get();
			if (lparent != null) {
				final String mime = lparent.getContentType(filename);
				return mimeType.equalsIgnoreCase(mime);
			}
			return false;
		}

		/** Replies if the given file has the given MIME type.
		 *
		 * @param filename is the filename of the file to read.
		 * @param mimeType is the restriction type.
		 * @return {@code true} if the file has the given MIME type,
		 *     otherwise {@code false}
		 */
		public boolean isContentType(String filename, String mimeType) {
			return isContentType(new File(filename), mimeType);
		}

		/** Replies if the given file has the given MIME type.
		 *
		 * @param filename is the filename of the file to read.
		 * @param mimeType is the restriction type.
		 * @return {@code true} if the file has the given MIME type,
		 *     otherwise {@code false}
		 */
		public boolean isContentType(URL filename, String mimeType) {
			assert filename != null : AssertMessages.notNullParameter(0);
			assert mimeType != null && !mimeType.isEmpty() : AssertMessages.notNullParameter(1);
			try {
				final MimeType mType = new MimeType(mimeType);
				if (isMimeType(filename, mType)) {
					return true;
				}
			} catch (Exception e) {
				//
			}
			return false;
		}

		private Set<MagicNumber> buildDecoderSet(String lowType) {
			final Set<MagicNumber> allDecoders = new TreeSet<>();
			final Collection<MagicNumber> magicNumbers = this.typedNumbers.get(lowType);
			if (magicNumbers != null) {
				allDecoders.addAll(magicNumbers);
			}
			allDecoders.addAll(this.defaultNumbers);
			return allDecoders;
		}

		/** Replies the mime type of the specified url.
		 *
		 * @see #isMimeType(URL, MimeType)
		 */
		private MimeType getMimeType(URL url) throws Exception {
			assert url != null : AssertMessages.notNullParameter(0);
			final URLConnection connection = url.openConnection();
			String lowType = connection.getContentType();

			if (MimeName.MIME_UNKNOW.isMimeConstant(lowType)) {
				lowType = MimeName.MIME_OCTET_STREAM.getMimeConstant();
			}

			boolean found;
			InputStream stream = connection.getInputStream();
			for (final MagicNumber magicNumber : buildDecoderSet(lowType)) {
				try {
					final MagicNumberStream mns;
					if (stream == null) {
						try (InputStream localstream = url.openStream()) {
							mns = new MagicNumberStream(url, localstream);
						}
					} else {
						mns = new MagicNumberStream(url, stream);
						stream = null;
					}
					magicNumber.doStreamEncoding(mns);
					found = magicNumber.isContentType(mns);
					magicNumber.undoStreamEncoding(mns);
					mns.close();
					if (found) {
						return magicNumber.getMimeType();
					}
				} catch (IOException e) {
					//
				}
			}

			try {
				if (lowType != null) {
					return new MimeType(lowType);
				}
			} catch (MimeTypeParseException e) {
				//
			}
			return null;
		}

		/** Replies if the given URL is of the given MIME type.
		 *
		 * @see #getMimeType(URL)
		 */
		private boolean isMimeType(URL url, MimeType mimeType) throws Exception {
			assert url != null : AssertMessages.notNullParameter(0);
			assert mimeType != null : AssertMessages.notNullParameter(1);

			final String baseType = mimeType.getBaseType();
			assert baseType != null && !baseType.isEmpty();

			final URLConnection connection = url.openConnection();
			String lowType = connection.getContentType();
			if (MimeName.MIME_UNKNOW.isMimeConstant(lowType)) {
				lowType = MimeName.MIME_OCTET_STREAM.getMimeConstant();
			}
			if (baseType.equalsIgnoreCase(lowType)) {
				return true;
			}

			InputStream stream = connection.getInputStream();
			boolean found;
			for (final MagicNumber magicNumber : buildDecoderSet(lowType)) {
				if (baseType.equalsIgnoreCase(magicNumber.getMimeType().getBaseType())) {
					try {
						final MagicNumberStream mns;
						if (stream == null) {
							try (InputStream localstream = url.openStream()) {
								mns = new MagicNumberStream(url, localstream);
							}
						} else {
							mns = new MagicNumberStream(url, stream);
							stream = null;
						}
						magicNumber.doStreamEncoding(mns);
						found = magicNumber.isContentType(mns);
						magicNumber.undoStreamEncoding(mns);
						mns.close();
						if (found) {
							return true;
						}
					} catch (IOException e) {
						//
					}
				}
			}

			return false;
		}

		/** Replies the format version of the specified url.
		 *
		 * @param url is the name of the file to test.
		 * @return the MIME type of the given file.
		 */
		public String getFormatVersion(URL url) {
			assert url != null : AssertMessages.notNullParameter();

			String lowType;
			InputStream stream;
			try {
				final URLConnection connection = url.openConnection();
				lowType = connection.getContentType();
				stream = connection.getInputStream();
			} catch (IOException e) {
				return null;
			}

			if (MimeName.MIME_UNKNOW.isMimeConstant(lowType)) {
				lowType = MimeName.MIME_OCTET_STREAM.getMimeConstant();
			}

			boolean found;
			for (final MagicNumber magicNumber : buildDecoderSet(lowType)) {
				try {
					final MagicNumberStream mns;
					if (stream == null) {
						try (InputStream localstream = url.openStream()) {
							mns = new MagicNumberStream(url, localstream);
						}
					} else {
						mns = new MagicNumberStream(url, stream);
						stream = null;
					}
					magicNumber.doStreamEncoding(mns);
					found = magicNumber.isContentType(mns);
					magicNumber.undoStreamEncoding(mns);
					mns.close();
					if (found) {
						return magicNumber.getFormatVersion();
					}
				} catch (IOException e) {
					//
				}
			}

			return null;
		}

		/** Replies the version of the format of the given file.
		 *
		 * @param filename is the name of the file to test.
		 * @return the format version for the given file.
		 */
		public String getFormatVersion(File filename) {
			assert filename != null : AssertMessages.notNullParameter();
			if (!filename.exists()) {
				return null;
			}
			try {
				return getFormatVersion(filename.toURI().toURL());
			} catch (Exception e) {
				//
			}
			return null;
		}

		/** Replies the version of the format of the given file.
		 *
		 * @param filename is the name of the file to test.
		 * @return the format version for the given file.
		 */
		public String getFormatVersion(String filename) {
			assert filename != null && !filename.isEmpty() : AssertMessages.notNullParameter();
			return getFormatVersion(new File(filename));
		}

		/** Register a MIME type of the given file.
		 *
		 * @param magicNumber is the string that identify the type of the content.
		 */
		public void addContentType(MagicNumber magicNumber) {
			assert magicNumber != null : AssertMessages.notNullParameter();
			boolean isTyped = false;
			final MimeType[] types = magicNumber.getHostMimeTypes();
			if (types != null) {
				for (final MimeType type : types) {
					if (type != null) {
						final String m = type.getBaseType();
						Collection<MagicNumber> col = this.typedNumbers.get(m);
						if (col == null) {
							col = new ArrayList<>();
							this.typedNumbers.put(m, col);
						}
						col.add(magicNumber);
						isTyped = true;
					}
				}
			}
			if (!isTyped) {
				this.defaultNumbers.add(magicNumber);
			}
		}

	}

}

