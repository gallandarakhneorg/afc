/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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
import java.util.jar.JarInputStream;

import org.arakhne.afc.inputoutput.filetype.FileType;
import org.arakhne.afc.inputoutput.filetype.MagicNumber;
import org.arakhne.afc.inputoutput.filetype.MagicNumberStream;
import org.arakhne.afc.inputoutput.mime.MimeName;
import org.arakhne.afc.vmutil.locale.Locale;

/** File filter for Java archive file document.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class JARFileFilter extends AbstractFileFilter {

	/** Default extension for the Java archive files.
	 */
	public static final String EXTENSION_JAR = "jar"; //$NON-NLS-1$

	/** Construct.
	 */
	public JARFileFilter() {
		this(true);
	}

	/** Constructor.
	 * @param acceptDirectories is <code>true</code> to
	 *     permit to this file filter to accept directories;
	 *     <code>false</code> if the directories should not
	 *     match.
	 */
	public JARFileFilter(boolean acceptDirectories) {
		super(
				acceptDirectories,
				Locale.getString(JARFileFilter.class, "FILE_FILTER_NAME"), //$NON-NLS-1$
				EXTENSION_JAR);
	}

	/** Replies if the specified file contains JAR file.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains JAR file,
	 *     otherwise <code>false</code>
	 */
	public static boolean isJARFile(File file) {
		return FileType.isContentType(file, MimeName.MIME_JAR.getMimeConstant());
	}

	/** Replies if the specified file contains JAR file.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains JAR file,
	 *     otherwise <code>false</code>
	 */
	public static boolean isJARFile(URL file) {
		return FileType.isContentType(file, MimeName.MIME_JAR.getMimeConstant());
	}

	/** Replies if the specified file contains JAR file.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains JAR file,
	 *     otherwise <code>false</code>
	 */
	public static boolean isJARFile(String file) {
		return FileType.isContentType(file, MimeName.MIME_JAR.getMimeConstant());
	}

	static {
		//Register MIME file contents
		FileType.addContentType(new JarMagicNumber());
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
	private static class JarMagicNumber extends MagicNumber {

		/** Constructor.
		 */
		JarMagicNumber() {
			super(
					MimeName.MIME_JAR,
					MimeName.MIME_OCTET_STREAM);
		}

		/** Replies if the specified stream contains data
		 * that corresponds to this magic number.
		 */
		@Override
		protected final boolean isContentType(MagicNumberStream stream) {
			try (JarInputStream jis = new JarInputStream(stream, false)) {
				return jis.getManifest() != null;
			} catch (IOException e) {
				//
			}
			return false;
		}

	}

}
