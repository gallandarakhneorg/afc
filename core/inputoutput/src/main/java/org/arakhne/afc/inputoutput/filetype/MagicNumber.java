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

package org.arakhne.afc.inputoutput.filetype;

import java.io.IOException;

import javax.activation.MimeType;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.inputoutput.mime.MimeName;

/** This clas defines a set of informations that could distinguish
 * a file content from another one. It is also known as Magic Number
 * on several operating systems.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class MagicNumber implements Comparable<MagicNumber> {

	private final MimeType mimeType;

	private final MimeType[] hostMimeTypes;

	private final String formatVersion;

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param hostMimeTypes are the MIME types given by the low-level analyzer and associated to this number.
	 */
	public MagicNumber(MimeType mimeType, MimeType... hostMimeTypes) {
		this(mimeType, null, hostMimeTypes);
	}

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param formatVersion is a string representing the format version supported by this magic number.
	 * @param hostMimeTypes are the MIME types given by the low-level analyzer and associated to this number.
	 */
	public MagicNumber(MimeType mimeType, String formatVersion, MimeType... hostMimeTypes) {
		this.mimeType = mimeType;
		this.hostMimeTypes = (hostMimeTypes == null) ? new MimeType[0] : hostMimeTypes;
		this.formatVersion = formatVersion;
	}

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param hostMimeTypes are the MIME types given by the low-level analyzer and associated to this number.
	 * @throws IllegalArgumentException if one of the MIME types cannot be parsed.
	 */
	public MagicNumber(MimeName mimeType, MimeName... hostMimeTypes) {
		this(mimeType, null, hostMimeTypes);
	}

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param formatVersion is a string representing the format version supported by this magic number.
	 * @param hostMimeTypes are the MIME types given by the low-level analyzer and associated to this number.
	 * @throws IllegalArgumentException if one of the MIME types cannot be parsed.
	 */
	public MagicNumber(MimeName mimeType, String formatVersion, MimeName... hostMimeTypes) {
		this.mimeType = mimeType.toMimeType();
		if (hostMimeTypes == null) {
			this.hostMimeTypes = new MimeType[0];
		} else {
			this.hostMimeTypes = new MimeType[hostMimeTypes.length];
			for (int i = 0; i < hostMimeTypes.length; ++i) {
				this.hostMimeTypes[i] = hostMimeTypes[i].toMimeType();
			}
		}
		this.formatVersion = formatVersion;
	}

	/** Replies the MIME type used by the Java virtal machine for this magic number.
	 *
	 * @return the low-level MIME type.
	 */
	@Pure
	MimeType[] getHostMimeTypes() {
		return this.hostMimeTypes;
	}

	/** Replies the MIME type for this magic number.
	 *
	 * @return the MIME type.
	 */
	@Pure
	public MimeType getMimeType() {
		return this.mimeType;
	}

	/** Replies the format version for this magic number.
	 *
	 * @return the format version or <code>null</code>.
	 */
	@Pure
	public String getFormatVersion() {
		return this.formatVersion;
	}

	/** Invoked before {@link #isContentType(MagicNumberStream)}
	 * to prepare the stream.
	 *
	 * @param stream is the stream to analyse.
	 * @throws IOException in case of IO error.
	 */
	protected void doStreamEncoding(MagicNumberStream stream) throws IOException {
		//
	}

	/** Invoked before {@link #isContentType(MagicNumberStream)}
	 * to unprepare the stream.
	 *
	 * @param stream is the stream to analyse.
	 * @throws IOException in case of IO error.
	 */
	protected void undoStreamEncoding(MagicNumberStream stream) throws IOException {
		//
	}

	/** Replies if the specified stream contains data
	 * that corresponds to this magic number.
	 *
	 * @param stream is the stream to analyse.
	 * @return <code>true</code> if the stream is containing this magic number, otherwise <code>false</code>
	 */
	@Pure
	protected abstract boolean isContentType(MagicNumberStream stream);

	@Pure
	@Override
    public int compareTo(MagicNumber number) {
    	return System.identityHashCode(this) - System.identityHashCode(number);
    }

}
