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

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/** This class defines is a buffered input stream which provides
 * additional reading functions which hare very useful for
 * a {@code MagicNumberStream}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see MagicNumberStream
 */
final class BufferedMagicNumberStream extends FilterInputStream {

	private static final int BUFFER_SIZE = 256;

	private byte[] buffer = new byte[0];

	private int pos;

	/**
	 * @param is is the input stream.
	 */
	BufferedMagicNumberStream(InputStream is) {
		super(is);
		this.pos = 0;
	}

	/** Replies the stream buffered by this object.
	 * This buffered stream is the stream pass as parameter of
	 * the constructor.
	 *
	 * @return the buffered stream.
	 */
	public InputStream getStream() {
		return this.in;
	}

	/** Replies the count of characters available for reading.
	 */
	private int ensureBuffer(int offset, int length) throws IOException {
		final int lastPos = offset + length - 1;
		final int desiredSize = ((lastPos / BUFFER_SIZE) + 1) * BUFFER_SIZE;
		final int currentSize = this.buffer.length;

		if (desiredSize > currentSize) {
			final byte[] readBuffer = new byte[desiredSize - currentSize];
			final int count = this.in.read(readBuffer);

			if (count > 0) {
				final byte[] newBuffer = new byte[currentSize + count];
				System.arraycopy(this.buffer, 0, newBuffer, 0, currentSize);
				System.arraycopy(readBuffer, 0, newBuffer, currentSize, count);
				this.buffer = newBuffer;
			}
			return (lastPos < this.buffer.length) ? length : length - (lastPos - this.buffer.length + 1);
		}
		return length;
	}

	/** Replies the bytes at the specified offset.
	 *
	 * @param offset is the position of the first byte to read.
	 * @param length is the count of bytes to read.
	 * @return the array of red bytes.
	 * @throws IOException in case of problems
	 */
	public byte[] read(int offset, int length) throws IOException {
		if (ensureBuffer(offset, length) >= length) {
			final byte[] array = new byte[length];
			System.arraycopy(this.buffer, offset, array, 0, length);
			this.pos = offset + length;
			return array;
		}
		throw new EOFException();
	}

	/** Replies a byte at the specified offset.
	 *
	 * @param offset is the position of the byte to read.
	 * @return the byte.
	 * @throws IOException in case of problems
	 */
	public byte read(int offset) throws IOException {
		if (ensureBuffer(offset, 1) > 0) {
			this.pos = offset + 1;
			return this.buffer[offset];
		}
		throw new EOFException();
	}

	@Override
	public int read() throws IOException {
		if (ensureBuffer(this.pos, 1) > 0) {
			final int c = this.buffer[this.pos];
			++this.pos;
			return c;
		}
		return -1;
	}

	/** Replies the bytes until the next end of the first line (inclusive).
	 *
	 * @param offset is the position of the byte to read.
	 * @return the bytes; or <code>null</code> if EOF
	 * @throws IOException in case of problems
	 */
	public byte[] readLine(int offset) throws IOException {
		int lastIndex = -1;
		int localOffset = offset;
		int read;
		do {
			read = ensureBuffer(localOffset, BUFFER_SIZE);
			if (read <= 0) {
				// EOF
				this.pos = this.buffer.length;
				return null;
			}

			// NOT EOF, search for end of line
			final int end = localOffset + read;
			for (int idx = localOffset; (lastIndex == -1) && (idx < this.buffer.length) && (idx < end); ++idx) {
				if ((this.buffer[idx] == '\n') || (this.buffer[idx] == '\r')) {
					lastIndex = idx;
				}
			}
			localOffset += read;
		}
		while (lastIndex == -1);
		this.pos = lastIndex;
		return Arrays.copyOfRange(this.buffer, offset, lastIndex);
	}

}
