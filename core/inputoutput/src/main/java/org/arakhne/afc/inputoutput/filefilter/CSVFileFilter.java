/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.inputoutput.filefilter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.arakhne.afc.inputoutput.filetype.FileType;
import org.arakhne.afc.inputoutput.filetype.MagicNumber;
import org.arakhne.afc.inputoutput.filetype.MagicNumberStream;
import org.arakhne.afc.inputoutput.mime.MimeName;
import org.arakhne.afc.vmutil.locale.Locale;

/** File filter for the Comma-separated Values files.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class CSVFileFilter extends AbstractFileFilter {

	/** Default extension for the Comma-separated Values files.
	 */
	public static final String EXTENSION_CSV = "csv"; //$NON-NLS-1$

	/** Construct.
	 */
	public CSVFileFilter() {
		this(true);
	}

	/** Constructor.
	 * @param acceptDirectories is <code>true</code> to
	 *     permit to this file filter to accept directories;
	 *     <code>false</code> if the directories should not
	 *     match.
	 */
	public CSVFileFilter(boolean acceptDirectories) {
		super(
				acceptDirectories,
				Locale.getString(CSVFileFilter.class, "FILE_FILTER_NAME"), //$NON-NLS-1$
				EXTENSION_CSV);
	}

	/** Replies if the specified file contains CSV picture.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains CSV picture,
	 *     otherwise <code>false</code>
	 */
	public static boolean isCSVFile(File file) {
		return FileType.isContentType(file, MimeName.MIME_CSV.getMimeConstant());
	}

	/** Replies if the specified file contains CSV picture.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains CSV picture,
	 *     otherwise <code>false</code>
	 */
	public static boolean isCSVFile(URL file) {
		return FileType.isContentType(file, MimeName.MIME_CSV.getMimeConstant());
	}

	/** Replies if the specified file contains CSV picture.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains CSV picture,
	 *     otherwise <code>false</code>
	 */
	public static boolean isCSVFile(String file) {
		return FileType.isContentType(file, MimeName.MIME_CSV.getMimeConstant());
	}

	static {
		//Register MIME file contents
		FileType.addContentType(new BinaryCSVMagicNumber());
	}

	/** This class defines a set of informations that could distinguish
	 * a file content from another one. It is also known as Magic Number
	 * on several operating systems.
	 *
	 * <p>This magic number supports the text content.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class BinaryCSVMagicNumber extends MagicNumber {

		private static final String REGEX = "^[\\x20-\\xFF\t\n\r\n]*$"; //$NON-NLS-1$

		private static final String[] SEPARATORS = {",", ";", "\t"}; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

		private static final String[] QSEPARATORS = {",", ";", "\t", " "};  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		/** Constructor.
		 */
		BinaryCSVMagicNumber() {
			super(MimeName.MIME_CSV, MimeName.MIME_OCTET_STREAM);
		}

		/** Replies if the specified stream contains data
		 * that corresponds to this magic number.
		 */
		@Override
		protected final boolean isContentType(MagicNumberStream stream) {
			String string;
			boolean found;
			int offset = 0;

			final Pattern pattern = Pattern.compile(REGEX);

			for (int i = 0; i < 3; ++i) {
				found = false;
				try {
					final byte[] line = stream.readLine(offset);
					if (line == null) {
						return true;
					}
					string = new String(line);
					offset += line.length;

					// Check if text.
					final Matcher matcher = pattern.matcher(string);
					if (matcher.matches()) {
						for (int j = 0; !found && j < QSEPARATORS.length; ++j) {
							if (matchSeparator(QSEPARATORS[j], true, string)) {
								found = true;
							}
						}
						if (!found) {
							for (int j = 0; !found && j < SEPARATORS.length; ++j) {
								if (matchSeparator(SEPARATORS[j], false, string)) {
									found = true;
								}
							}
						}
					}
				} catch (IOException e) {
					//
				}

				if (!found) {
					return false;
				}
			}

			return true;
		}

		/** Replies if the given string is matching the given separators.
		 *
		 * @param separator is the separator description
		 * @param quoted indicates if the quote may be tested.
		 * @param str is the text to test.
		 * @return <code>true</code> if matching separator, <code>false</code> otherwise
		 */
		private static boolean matchSeparator(String separator, boolean quoted, String str) {
			final String regex = "^[\n\r\t ]*" //$NON-NLS-1$
					+ (quoted ? "\"" : "") //$NON-NLS-1$//$NON-NLS-2$
					+ "[^" //$NON-NLS-1$
					+ separator + "]*" //$NON-NLS-1$
					+ (quoted ? "\"" : "") //$NON-NLS-1$//$NON-NLS-2$
					+ "([\n\r\t ]*[" //$NON-NLS-1$
					+ separator + "][\n\r\t ]*" //$NON-NLS-1$
					+ (quoted ? "\"" : "") //$NON-NLS-1$//$NON-NLS-2$
					+ "[^" //$NON-NLS-1$
					+ separator + "]*" //$NON-NLS-1$
					+ (quoted ? "\"" : "") //$NON-NLS-1$//$NON-NLS-2$
					+ ")*"; //$NON-NLS-1$
			return Pattern.matches(regex, str);
		}

	}

}
