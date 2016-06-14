/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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
import java.util.regex.Pattern;

import org.arakhne.afc.inputoutput.filetype.FileType;
import org.arakhne.afc.inputoutput.filetype.MagicNumber;
import org.arakhne.afc.inputoutput.filetype.MagicNumberStream;
import org.arakhne.afc.inputoutput.mime.MimeName;
import org.arakhne.afc.vmutil.locale.Locale;

/** File filter for Windows configuration files.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class INIFileFilter extends AbstractFileFilter {

	/** Default extension for the Windows configuration files.
	 */
	public static final String EXTENSION_INI = "ini"; //$NON-NLS-1$

	/** Construct.
	 */
	public INIFileFilter() {
		this(true);
	}

	/**
	 * @param acceptDirectories is <code>true</code> to
	 *     permit to this file filter to accept directories;
	 *     <code>false</code> if the directories should not
	 *     match.
	 */
	public INIFileFilter(boolean acceptDirectories) {
		super(
				acceptDirectories,
				Locale.getString(INIFileFilter.class, "FILE_FILTER_NAME"), //$NON-NLS-1$
				EXTENSION_INI);
	}

	/** Replies if the specified file contains JPEG picture.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains JPEG picture,
	 *     otherwise <code>false</code>
	 */
	public static boolean isINIFile(File file) {
		return FileType.isContentType(file, MimeName.MIME_INIT.getMimeConstant());
	}

	/** Replies if the specified file contains JPEG picture.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains JPEG picture,
	 *     otherwise <code>false</code>
	 */
	public static boolean isINIFile(URL file) {
		return FileType.isContentType(file, MimeName.MIME_INIT.getMimeConstant());
	}

	/** Replies if the specified file contains JPEG picture.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains JPEG picture,
	 *     otherwise <code>false</code>
	 */
	public static boolean isINIFile(String file) {
		return FileType.isContentType(file, MimeName.MIME_INIT.getMimeConstant());
	}

	static {
		//Register MIME file contents
		FileType.addContentType(new BinaryINIMagicNumber());
	}

	/** This clas defines a set of informations that could distinguish
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
	private static class BinaryINIMagicNumber extends MagicNumber {

		private static final String REGEX = "^[\\x20-\\xFF\t\n\r\n]*$"; //$NON-NLS-1$

		private static final String HEADER = "^[\n\r\t\f]*\\[[^\\]]+\\][\n\r\t\f]*$"; //$NON-NLS-1$

		private static final String VAR = "^[\n\r\t\f]*[a-zA-Z0-9_.\\\\/:\\|]+[\n\r\t\f]*=.*$"; //$NON-NLS-1$

		private static final String EMPTY = "^[\n\r\t\f]*$"; //$NON-NLS-1$

		private static final String COMMENT = "^;"; //$NON-NLS-1$

		/** Constructor.
		 */
		BinaryINIMagicNumber() {
			super(
					MimeName.MIME_INIT,
					MimeName.MIME_OCTET_STREAM);
		}

		/** Replies if the specified stream contains data
		 * that corresponds to this magic number.
		 */
		@Override
		@SuppressWarnings("checkstyle:magicnumber")
		protected final boolean isContentType(MagicNumberStream stream) {
			String string;
			boolean found;
			int offset = 0;
			boolean istext = false;

			for (int i = 0; i < 5; ++i) {

				found = false;
				try {
					final byte[] line = stream.readLine(offset);
					if (line == null) {
						return true;
					}
					string = new String(line);
					offset += line.length;

					// Check if text.
					if (Pattern.matches(REGEX, string)) {
						if (istext || Pattern.matches(EMPTY, string) || Pattern.matches(COMMENT, string)
								|| Pattern.matches(HEADER, string) || Pattern.matches(VAR, string)) {
							found = true;
						}
						istext = string.endsWith("\\"); //$NON-NLS-1$
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

	}

}
