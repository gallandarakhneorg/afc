/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import java.io.IOException;
import java.util.zip.ZipInputStream;
import javax.activation.MimeType;

import org.arakhne.afc.inputoutput.mime.MimeName;

/** This class defines a set of informations that could distinguish
 * a file content from another one. It is also known as Magic Number
 * on several operating systems.
 *
 * <p>This magic number supports zipped streams. For jar files
 * (a type of zipped file), please use {@link JarMagicNumber}.
 * For zipped archives, please use {@link ZipArchiveMagicNumber}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see JarMagicNumber
 * @see ZipArchiveMagicNumber
 */
public abstract class ZipMagicNumber extends MagicNumber {

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 */
	public ZipMagicNumber(MimeType mimeType) {
		super(mimeType, MimeName.MIME_ZIP.toMimeType());
	}

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param formatVersion is the version of the supported format.
	 */
	public ZipMagicNumber(MimeType mimeType, String formatVersion) {
		super(mimeType, formatVersion, MimeName.MIME_ZIP.toMimeType());
	}

	@Override
	protected void doStreamEncoding(MagicNumberStream stream)  throws IOException {
		stream.setOverridingStream(new ZipInputStream(stream.getInputStream()));
	}

	@Override
	protected void undoStreamEncoding(MagicNumberStream stream)  throws IOException {
		stream.resetOverridingStream();
	}

}
