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

/** File filter for 3D Object file document.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class OBJ3DFileFilter extends AbstractFileFilter {

	/** Default extension for the 3D Object files.
	 */
	public static final String EXTENSION_OBJ = "obj"; //$NON-NLS-1$

	/** Construct.
	 */
	public OBJ3DFileFilter() {
		this(true);
	}

	/**
	 * @param acceptDirectories is <code>true</code> to
	 *     permit to this file filter to accept directories;
	 *     <code>false</code> if the directories should not
	 *     match.
	 */
	public OBJ3DFileFilter(boolean acceptDirectories) {
		super(
				acceptDirectories,
				Locale.getString(OBJ3DFileFilter.class, "FILE_FILTER_NAME"), //$NON-NLS-1$
				EXTENSION_OBJ);
	}

	/** Replies if the specified file contains <code>.obj</code> data.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains <code>.obj</code> data,
	 *     otherwise <code>false</code>
	 */
	public static boolean isOBJFile(File file) {
		return FileType.isContentType(file, MimeName.MIME_WAVEFRONT_OBJ.getMimeConstant());
	}

	/** Replies if the specified file contains <code>.obj</code> data.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains <code>.obj</code> data,
	 *     otherwise <code>false</code>
	 */
	public static boolean isOBJFile(URL file) {
		return FileType.isContentType(file, MimeName.MIME_WAVEFRONT_OBJ.getMimeConstant());
	}

	/** Replies if the specified file contains <code>.obj</code> data.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains <code>.obj</code> data,
	 *     otherwise <code>false</code>
	 */
	public static boolean isOBJFile(String file) {
		return FileType.isContentType(file, MimeName.MIME_WAVEFRONT_OBJ.getMimeConstant());
	}

	static {
		//Register MIME file contents
		FileType.addContentType(new OBJMagicNumber());
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
	 */
	private static class OBJMagicNumber extends MagicNumber {

		private static final String NUMBER = "^[ \n\r\t\f]*((vt)|(vn)|p|l|v|f)([ \n\r\t\f]+" //$NON-NLS-1$
				+ "[0-9.+-e]+(\\/[0-9.+-e]+){0,2})+[ \n\r\t\f]*$"; //$NON-NLS-1$

		private static final String IDENTIFIER = "^[ \n\r\t\f]*(o|g)[ \n\r\t\f]+"; //$NON-NLS-1$

		private static final String ONOFF = "^[ \n\r\t\f]*(s)[ \n\r\t\f]+((on)|(off))[ \n\r\t\f]*$"; //$NON-NLS-1$

		private static final String COMMENT = "^#"; //$NON-NLS-1$

		/** Constructor.
		 */
		OBJMagicNumber() {
			super(
					MimeName.MIME_WAVEFRONT_OBJ,
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

			boolean foundOneOBJPrimitive = false;

			final Pattern number = Pattern.compile(NUMBER);
			final Pattern identifier = Pattern.compile(IDENTIFIER);
			final Pattern onoff = Pattern.compile(ONOFF);
			final Pattern comment = Pattern.compile(COMMENT);

			int i = 0;
			while (i < 20) {
				found = false;
				try {
					final byte[] line = stream.readLine(offset);
					if (line == null) {
						return foundOneOBJPrimitive;
					}
					string = new String(line).trim();
					offset += line.length + 1;

					if (string.length() > 0) {
						// Check if comment.
						if (comment.matcher(string).find()) {
							foundOneOBJPrimitive = true;
							found = true;
						} else if (number.matcher(string).find()) {
							foundOneOBJPrimitive = true;
							found = true;
						} else if (identifier.matcher(string).find()) {
							foundOneOBJPrimitive = true;
							found = true;
						} else if (onoff.matcher(string).find()) {
							foundOneOBJPrimitive = true;
							found = true;
						}
					} else {
						// force to skip white lines
						--i;
						found = true;
					}
				} catch (IOException e) {
					return true;
				}

				if (!found) {
					return false;
				}
				++i;
			}
			return foundOneOBJPrimitive;
		}

	}

}
