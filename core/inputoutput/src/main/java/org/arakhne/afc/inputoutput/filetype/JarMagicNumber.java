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

package org.arakhne.afc.inputoutput.filetype;

import java.io.InputStream;
import java.net.URL;

import javax.activation.MimeType;

import org.arakhne.afc.inputoutput.mime.MimeName;
import org.arakhne.afc.vmutil.FileSystem;

/** This class defines a set of informations that could distinguish
 * a file content from another one. It is also known as Magic Number
 * on several operating systems.
 *
 * <p>This magic number supports Jar files (ie Zip file with manifest).
 * For general zipped file, please see {@link ZipMagicNumber}.
 * For zip archive of files, please see {@link ZipArchiveMagicNumber}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see ZipMagicNumber
 * @see ZipArchiveMagicNumber
 */
public class JarMagicNumber extends MagicNumber {

	private static final String METAINF_PATH = "META-INF/MANIFEST.MF"; //$NON-NLS-1$

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 */
	public JarMagicNumber(MimeType mimeType) {
		super(mimeType, MimeName.MIME_ZIP.toMimeType());
	}

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param formatVersion is the version of the supported format.
	 */
	public JarMagicNumber(MimeType mimeType, String formatVersion) {
		super(mimeType, formatVersion, MimeName.MIME_ZIP.toMimeType());
	}

	@Override
	protected final boolean isContentType(MagicNumberStream stream) {
		try {
			URL url = stream.getURL();
			url = FileSystem.toJarURL(url, METAINF_PATH);
			try (InputStream manifest = url.openStream()) {
				return isManifestMatch(manifest);
			}
		} catch (Exception e) {
			return false;
		}
	}

	/** Invoked to test the content of the Jar file manifest.
	 *
	 * @param manifestContent is the content of the manifest
	 * @return <code>true</code> if the manifest is matching, otherwise <code>false</code>
	 */
	public boolean isManifestMatch(InputStream manifestContent) {
		return true;
	}

}
