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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/** This class defines a set of informations that could distinguish
 * a file content from another one. It is also known as Magic Number
 * on several operating systems.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class MagicNumberStream extends InputStream {

	private final URL url;

	private final BufferedMagicNumberStream mainStream;

	private BufferedMagicNumberStream overridingStream;

	/** Constructor.
	 * @param url is the url of the input stream to read.
	 * @param is is the input stream.
	 */
	MagicNumberStream(URL url, InputStream is) {
		this.mainStream = new BufferedMagicNumberStream(is);
		this.url = url;
	}

	/** Replies the URL of the stream to test.
	 *
	 * @return the url of the stream to test.
	 */
	public URL getURL() {
		return this.url;
	}

	/** Set the input stream which may be used in place
	 * of the original input stream.
	 *
	 * @param newis is the stream to read.
	 */
	public void setOverridingStream(InputStream newis) {
		this.overridingStream = new BufferedMagicNumberStream(newis);
	}

	/**
	 * Cancel any previous call to {@link #setOverridingStream(InputStream)}.
	 */
	public void resetOverridingStream() {
		this.overridingStream = null;
	}

	private BufferedMagicNumberStream getStream() {
		return (this.overridingStream != null) ? this.overridingStream : this.mainStream;
	}

	/** Replies the stream read by the magic number API.
	 * The replied input stream is the stream linked to
	 * the file to test, or the stream passed as parameter
	 * of {@link #setOverridingStream(InputStream)} if invoked.
	 *
	 * @return the stream read by the magic number API.
	 */
	public InputStream getInputStream() {
		return getStream().getStream();
	}

	/** Replies the bytes at the specified offset.
	 *
	 * @param offset is the position of the first byte to read.
	 * @param length is the cout of bytes to read.
	 * @return the array of red bytes.
	 * @throws IOException in case of problems
	 */
	public byte[] read(int offset, int length) throws IOException {
		return getStream().read(offset, length);
	}

	/** Replies a byte at the specified offset.
	 *
	 * @param offset is the position of the byte to read.
	 * @return the byte.
	 * @throws IOException in case of problems
	 */
	public byte read(int offset) throws IOException {
		return getStream().read(offset);
	}

	@Override
	public int read() throws IOException {
		return getStream().read();
	}

	/** Replies the bytes until the next end of the first line (inclusive).
	 *
	 * @param offset is the position of the byte to read.
	 * @return the bytes.
	 * @throws IOException in case of problems
	 */
	public byte[] readLine(int offset) throws IOException {
		return getStream().readLine(offset);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		getStream().close();
	}

}
