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

package org.arakhne.afc.inputoutput.filetype;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.activation.MimeType;

import org.arakhne.afc.inputoutput.mime.MimeName;

/** This class defines a set of informations that could distinguish
 * a file content from another one. It is also known as Magic Number
 * on several operating systems.
 *
 * <p>This magic number supports zip archive of files. For a single
 * zipped file, please use {@link ZipMagicNumber}. For a jar file,
 * plase use {@link JarMagicNumber}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see ZipMagicNumber
 * @see JarMagicNumber
 */
public abstract class ZipArchiveMagicNumber extends MagicNumber {

	private final File innerFile;

	/**
	 * @param mimeType is the MIME type associated to this magic number.
	 */
	public ZipArchiveMagicNumber(MimeType mimeType) {
		this(mimeType, (File) null);
	}

	/**
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param formatVersion is the version of the supported format.
	 * @param innerFile is the file to open from the inside of the Zip archive
	 *     to test the type. The filename is relative to the root of the zip file
	 *     content.
	 */
	public ZipArchiveMagicNumber(MimeType mimeType, String formatVersion, File innerFile) {
		super(mimeType, formatVersion,
				MimeName.MIME_ZIP.toMimeType(),
				MimeName.MIME_OCTET_STREAM.toMimeType());
		this.innerFile = innerFile;
	}

	/**
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param innerFile is the file to open from the inside of the Zip archive
	 *     to test the type. The filename is relative to the root of the zip file
	 *     content.
	 */
	public ZipArchiveMagicNumber(MimeType mimeType, File innerFile) {
		super(mimeType,
				MimeName.MIME_ZIP.toMimeType(),
				MimeName.MIME_OCTET_STREAM.toMimeType());
		this.innerFile = innerFile;
	}

	/**
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param formatVersion is the version of the supported format.
	 */
	public ZipArchiveMagicNumber(MimeType mimeType, String formatVersion) {
		this(mimeType, formatVersion, (File) null);
	}

	@Override
	protected void doStreamEncoding(MagicNumberStream stream)  throws IOException {
		stream.setOverridingStream(new ZipInputStream(stream.getInputStream()));
	}

	@Override
	protected void undoStreamEncoding(MagicNumberStream stream)  throws IOException {
		stream.resetOverridingStream();
	}

	@SuppressWarnings("resource")
	@Override
	protected final boolean isContentType(MagicNumberStream stream) {
		try (InputStream is = stream.getInputStream()) {
			if (is instanceof ZipInputStream) {
				final ZipInputStream zis = (ZipInputStream) is;
				if (this.innerFile != null) {
					final String strInner = this.innerFile.toString();
					InputStream dataStream = null;
					ZipEntry zipEntry = zis.getNextEntry();
					while (zipEntry != null && dataStream == null) {
						if (strInner.equals(zipEntry.getName())) {
							dataStream = zis;
						} else {
							zipEntry = zis.getNextEntry();
						}
					}
					if (dataStream != null) {
						return isContentType(zis, zipEntry, dataStream);
					}
				} else {
					return isContentType(zis, null, null);
				}
			}
		} catch (Exception e) {
			//
		}
		return false;
	}

	/** Replies if the specified stream contains data
	 * that corresponds to this magic number.
	 *
	 * @param zipStream is the stream of the Zip archive
	 * @param zipEntry is the zip entry that is corresponding to the innerFile given as parameter
	 *     of the constructor, or <code>null</code> not significant.
	 * @param zipEntryStream is the stream on the data of the given zip entry, or <code>null</code> not significant.
	 * @return <code>true</code> if the stream is containing this magic number, otherwise <code>false</code>
	 */
	protected abstract boolean isContentType(ZipInputStream zipStream, ZipEntry zipEntry, InputStream zipEntryStream);

}
