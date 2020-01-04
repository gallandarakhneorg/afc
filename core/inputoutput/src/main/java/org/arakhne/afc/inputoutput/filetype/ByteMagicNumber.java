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

import javax.activation.MimeType;

import org.arakhne.afc.inputoutput.mime.MimeName;

/** This class defines a set of informations that could distinguish
 * a file content from another one. It is also known as Magic Number
 * on several operating systems.
 *
 * <p>This magic number supports the byte-stream content and
 * permits to test a portion of the bytes.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class ByteMagicNumber extends MagicNumber {

	private final String content;

	private final int offset;

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param formatVersion is the version of the supported format.
	 * @param content is the magic string inside the file.
	 * @param offset is the position of the magic string inside the file.
	 */
	public ByteMagicNumber(MimeType mimeType, String formatVersion, String content, int offset) {
		super(mimeType, formatVersion, MimeName.MIME_OCTET_STREAM.toMimeType());
		this.content = content;
		this.offset = offset;
	}

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param content is the magic string inside the file.
	 * @param offset is the position of the magic string inside the file.
	 */
	public ByteMagicNumber(MimeType mimeType, String content, int offset) {
		super(mimeType, MimeName.MIME_OCTET_STREAM.toMimeType());
		this.content = content;
		this.offset = offset;
	}

	@Override
	protected final boolean isContentType(MagicNumberStream stream) {
		try {
			final byte[] byteContent = stream.read(this.offset, this.content.length());
			return this.content.equals(new String(byteContent));
		} catch (Exception e) {
			return false;
		}
	}

}
