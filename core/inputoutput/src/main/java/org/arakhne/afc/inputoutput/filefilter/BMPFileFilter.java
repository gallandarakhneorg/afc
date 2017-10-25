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
import java.net.URL;

import org.arakhne.afc.inputoutput.filetype.FileType;
import org.arakhne.afc.inputoutput.mime.MimeName;
import org.arakhne.afc.vmutil.locale.Locale;

/** File filter for the Microsoft/IBM bitmap files.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class BMPFileFilter extends AbstractFileFilter {

	/** Default extension for the Microsoft/IBM bitmap files.
	 */
	public static final String EXTENSION_BMP = "bmp"; //$NON-NLS-1$

	/** Construct.
	 */
	public BMPFileFilter() {
		this(true);
	}

	/**
	 * @param acceptDirectories is <code>true</code> to
	 *     permit to this file filter to accept directories;
	 *     <code>false</code> if the directories should not
	 *     match.
	 */
	public BMPFileFilter(boolean acceptDirectories) {
		super(
				acceptDirectories,
				Locale.getString(BMPFileFilter.class, "FILE_FILTER_NAME"), //$NON-NLS-1$
				EXTENSION_BMP);
	}

	/** Replies if the specified file contains BMP picture.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains BMP picture,
	 *     otherwise <code>false</code>
	 */
	public static boolean isBMPFile(File file) {
		return FileType.isContentType(file, MimeName.MIME_BMP.getMimeConstant());
	}

	/** Replies if the specified file contains BMP picture.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains BMP picture,
	 *     otherwise <code>false</code>
	 */
	public static boolean isBMPFile(URL file) {
		return FileType.isContentType(file, MimeName.MIME_BMP.getMimeConstant());
	}

	/** Replies if the specified file contains BMP picture.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains BMP picture,
	 *     otherwise <code>false</code>
	 */
	public static boolean isBMPFile(String file) {
		return FileType.isContentType(file, MimeName.MIME_BMP.getMimeConstant());
	}

}
