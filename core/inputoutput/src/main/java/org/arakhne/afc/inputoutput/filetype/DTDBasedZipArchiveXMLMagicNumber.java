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

import java.io.File;

import javax.activation.MimeType;

/** This class defines a set of informations that could distinguish
 * a file content from another one. It is also known as Magic Number
 * on several operating systems.
 *
 * <p>This magic number supports an XML file inside a zip archive.
 * It permits to test the DTD public and system identifiers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class DTDBasedZipArchiveXMLMagicNumber extends ZipArchiveXMLMagicNumber {

	private final String systemId;

	private final String publicId;

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param systemId is the DTD system ID associated to this magic number.
	 * @param publicId is the DTD system ID associated to this magic number.
	 * @param formatVersion is the version of the supported format.
	 * @param innerFile is the file to open from the inside of the Zip archive
	 *     to test the type. The filename is relative to the root of the zip file
	 *     content.
	 */
	public DTDBasedZipArchiveXMLMagicNumber(MimeType mimeType, String systemId, String publicId,
			String formatVersion, File innerFile) {
		super(mimeType, formatVersion, innerFile, null);
		this.systemId = systemId;
		this.publicId = publicId;
	}

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param systemId is the DTD system ID associated to this magic number.
	 * @param publicId is the DTD system ID associated to this magic number.
	 * @param innerFile is the file to open from the inside of the Zip archive
	 *     to test the type. The filename is relative to the root of the zip file
	 *     content.
	 */
	public DTDBasedZipArchiveXMLMagicNumber(MimeType mimeType, String systemId, String publicId, File innerFile) {
		super(mimeType, innerFile, null);
		this.systemId = systemId;
		this.publicId = publicId;
	}

	/** {@inheritDoc}
	 */
	@Override
	protected boolean isContentType(String schemaId, String schemaVersion, String systemId, String publicId) {
		return (this.publicId != null && this.publicId.equalsIgnoreCase(publicId))
				|| (this.systemId != null && this.systemId.equalsIgnoreCase(systemId));
	}

}
