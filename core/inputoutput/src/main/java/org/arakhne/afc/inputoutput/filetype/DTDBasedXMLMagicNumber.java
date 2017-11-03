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

package org.arakhne.afc.inputoutput.filetype;

import javax.activation.MimeType;

/** This class defines a set of informations that could distinguish
 * a file content from another one. It is also known as Magic Number
 * on several operating systems.
 *
 * <p>This magic number supports XML files and permits to
 * test the system and user Identifiers extracted from the XML
 * content.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class DTDBasedXMLMagicNumber extends XMLMagicNumber {

	private final String systemId;

	private final String publicId;

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param systemId is the DTD system ID associated to this magic number.
	 * @param publicId is the DTD system ID associated to this magic number.
	 */
	public DTDBasedXMLMagicNumber(MimeType mimeType, String systemId, String publicId) {
		super(mimeType, null);
		this.systemId = systemId;
		this.publicId = publicId;
	}

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param formatVersion is the version of the supported format.
	 * @param systemId is the DTD system ID associated to this magic number.
	 * @param publicId is the DTD system ID associated to this magic number.
	 */
	public DTDBasedXMLMagicNumber(MimeType mimeType, String formatVersion, String systemId, String publicId) {
		super(mimeType, null, formatVersion);
		this.systemId = systemId;
		this.publicId = publicId;
	}

	/** Replies if the specified stream contains data
	 * that corresponds to this magic number.
	 *
	 * @param schemaId is the ID of the XSL schema associated to this magic number.
	 * @param schemaVersion is the ID of the XSL schema associated to this magic number.
	 * @param systemId is the DTD system ID associated to this magic number.
	 * @param publicId is the DTD system ID associated to this magic number.
	 * @return <code>true</code> if this magic number is corresponding to the given
	 *     XML document, otherwise <code>false</code>
	 */
	@Override
	protected boolean isContentType(String schemaId, String schemaVersion, String systemId, String publicId) {
		return (this.publicId != null && this.publicId.equalsIgnoreCase(publicId))
				|| (this.systemId != null && this.systemId.equalsIgnoreCase(systemId));
	}

}
