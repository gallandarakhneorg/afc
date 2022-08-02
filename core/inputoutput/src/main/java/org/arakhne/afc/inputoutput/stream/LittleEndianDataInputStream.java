/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.inputoutput.stream;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.arakhne.afc.inputoutput.endian.EndianNumbers;

/**
 * This class permits to read a stream with Little Endian encoding.
 *
 * @author $Author: olamotte$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("checkstyle:magicnumber")
public class LittleEndianDataInputStream extends DataInputStream {

	/** Constructor.
	 * @param inputStream is the input stream to read from.
	 */
	public LittleEndianDataInputStream(InputStream inputStream) {
		super(inputStream);
	}

	/** Replies the input stream that was passed to the constructor.
	 *
	 * @return the input stream that was passed to the constructor;
	 */
	public InputStream getTargetInputStream() {
		return this.in;
	}

	/**
	 * Read a Big Endian double on 8 bytes.
	 *
	 * @return the value red from the input stream
	 * @throws IOException on error.
	 */
	public double readBEDouble() throws IOException {
		return Double.longBitsToDouble(readBELong());
	}

	/**
	 * Read a Big Endian float on 4 bytes.
	 *
	 * @return the value red from the input stream
	 * @throws IOException on error.
	 */
	public float readBEFloat() throws IOException {
		return Float.intBitsToFloat(readBEInt());
	}

	/**
	 * Read an Big Endian int on 4 bytes.
	 *
	 * <p>According to the contract of {@link DataInput#readInt()} this function is equivalent to.
	 *
	 * @return the value red from the input stream
	 * @throws IOException on error.
	 */
	public int readBEInt() throws IOException {
		return EndianNumbers.toBEInt(read(), read(), read(), read());
	}

	/**
	 * Read a Big Endian long on 8 bytes.
	 *
	 * <p>According to the contract of {@link DataInput#readLong()} this function is equivalent to.
	 *
	 * @return the value red from the input stream
	 * @throws IOException on error.
	 */
	public long readBELong() throws IOException {
		return EndianNumbers.toBELong(read(), read(), read(), read(), read(), read(), read(), read());
	}

	/**
	 * Read a Big Endian short on 2 bytes.
	 *
	 * <p>According to the contract of {@link DataInput#readShort()} this function is equivalent to.
	 *
	 * @return the value red from the input stream
	 * @throws IOException on error.
	 */
	public short readBEShort() throws IOException {
		return EndianNumbers.toBEShort(read(), read());
	}

	/**
	 * Read a Little Endian double on 8 bytes.
	 *
	 * @return the value red from the input stream
	 * @throws IOException on error.
	 */
	public double readLEDouble() throws IOException {
		return Double.longBitsToDouble(readLELong());
	}

	/**
	 * Read a Little Endian float on 4 bytes.
	 *
	 * @return the value red from the input stream
	 * @throws IOException on error.
	 */
	public float readLEFloat() throws IOException {
		return Float.intBitsToFloat(readLEInt());
	}

	/**
	 * Read an Little Endian int on 4 bytes.
	 *
	 * @return the value red from the input stream
	 * @throws IOException on error.
	 */
	public int readLEInt() throws IOException {
		return EndianNumbers.toLEInt(read(), read(), read(), read());
	}

	/**
	 * Read a Little Endian long on 8 bytes.
	 *
	 * @return the value red from the input stream
	 * @throws IOException on error.
	 */
	public long readLELong() throws IOException {
		return EndianNumbers.toLELong(read(), read(), read(), read(), read(), read(), read(), read());
	}

	/**
	 * Read a Little Endian short on 2 bytes.
	 *
	 * @return the value red from the input stream
	 * @throws IOException on error.
	 */
	public short readLEShort() throws IOException {
		return EndianNumbers.toLEShort(read(), read());
	}

	/**
	 * Skip a double (8 bytes).
	 *
	 * @throws IOException on error.
	 */
	public void skipDouble() throws IOException {
		this.skipBytes(8);
	}

	/**
	 * Skip a float (4 bytes).
	 *
	 * @throws IOException on error.
	 */
	public void skipFloat() throws IOException {
		this.skipBytes(4);
	}

	/**
	 * Skip an integer (4 bytes).
	 *
	 * @throws IOException on error.
	 */
	public void skipInt() throws IOException {
		this.skipBytes(4);
	}

	/**
	 * Skip an integer (8 bytes).
	 *
	 * @throws IOException on error.
	 */
	public void skipLong() throws IOException {
		this.skipBytes(8);
	}

	/**
	 * Skip a short (2 bytes).
	 *
	 * @throws IOException on error.
	 */
	public void skipShort() throws IOException {
		this.skipBytes(2);
	}

}
