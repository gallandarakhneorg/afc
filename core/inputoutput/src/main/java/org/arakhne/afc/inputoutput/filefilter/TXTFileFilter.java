/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

/** File filter for text document.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class TXTFileFilter extends AbstractFileFilter {

	/** Default extension for the text files.
	 */
	public static final String EXTENSION_TXT = "txt"; //$NON-NLS-1$

	/** Default extension for the text files.
	 */
	public static final String EXTENSION_TEXT = "text"; //$NON-NLS-1$

	/** Construct.
	 */
	public TXTFileFilter() {
		this(true);
	}

	/** Constructor.
	 * @param acceptDirectories is <code>true</code> to
	 *     permit to this file filter to accept directories;
	 *     <code>false</code> if the directories should not
	 *     match.
	 */
	public TXTFileFilter(boolean acceptDirectories) {
		super(
				acceptDirectories,
				Locale.getString(TXTFileFilter.class, "FILE_FILTER_NAME"), //$NON-NLS-1$
				EXTENSION_TXT,
				EXTENSION_TEXT);
	}

	/** Replies if the specified file contains text.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains text,
	 *     otherwise <code>false</code>
	 */
	public static boolean isTextFile(File file) {
		return FileType.isContentType(file, MimeName.MIME_PLAIN_TEXT.getMimeConstant());
	}

	/** Replies if the specified file contains text.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains text,
	 *     otherwise <code>false</code>
	 */
	public static boolean isTextFile(URL file) {
		return FileType.isContentType(file, MimeName.MIME_PLAIN_TEXT.getMimeConstant());
	}

	/** Replies if the specified file contains text.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains text,
	 *     otherwise <code>false</code>
	 */
	public static boolean isTextFile(String file) {
		return FileType.isContentType(file, MimeName.MIME_PLAIN_TEXT.getMimeConstant());
	}

}
