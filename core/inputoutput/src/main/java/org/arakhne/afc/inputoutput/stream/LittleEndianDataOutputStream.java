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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.arakhne.afc.inputoutput.endian.EndianNumbers;

/**
 * This class permits to write a stream with little endian data output.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class LittleEndianDataOutputStream extends DataOutputStream {

	/** Constructor.
	 * @param out is the stream to write inside
	 */
	public LittleEndianDataOutputStream(OutputStream out) {
		super(out);
	}

	/**
	 * Writes a Big Endian double on 8 bytes.
	 *
	 * @param value is the value to write
	 * @throws IOException on error.
	 */
	public void writeBEDouble(double value) throws IOException {
		writeBELong(Double.doubleToLongBits(value));
	}

	/** Writes a Big Endian float on 4 bytes.
	 *
	 * @param value is the value to write
	 * @throws IOException on error.
	 */
	public void writeBEFloat(float value) throws IOException {
		writeBEInt(Float.floatToIntBits(value));
	}

	/**
	 * Writes a Big Endian int on 4 bytes.
	 *
	 * @param value is the value to write
	 * @throws IOException on error.
	 */
	public void writeBEInt(int value) throws IOException {
		for (final byte b : EndianNumbers.parseBEInt(value)) {
			writeByte(b);
		}
	}

	/**
	 * Writes a Big Endian long on 8 bytes.
	 *
	 * @param value is the value to write
	 * @throws IOException on error.
	 */
	public void writeBELong(long value) throws IOException {
		for (final byte b : EndianNumbers.parseBELong(value)) {
			writeByte(b);
		}
	}

	/**
	 * Writes a Big Endian short on 2 bytes.
	 *
	 * @param value is the value to write
	 * @throws IOException on error.
	 */
	public void writeBEShort(short value) throws IOException {
		for (final byte b : EndianNumbers.parseBEShort(value)) {
			writeByte(b);
		}
	}

	/**
	 * Writes a Little Endian double on 8 bytes.
	 *
	 * @param value is the value to write
	 * @throws IOException on error.
	 */
	public void writeLEDouble(double value) throws IOException {
		writeLELong(Double.doubleToLongBits(value));
	}

	/** Writes a Little Endian float on 4 bytes.
	 *
	 * @param value is the value to write
	 * @throws IOException on error.
	 */
	public void writeLEFloat(float value) throws IOException {
		writeLEInt(Float.floatToIntBits(value));
	}

	/**
	 * Writes a Little Endian int on 4 bytes.
	 *
	 * @param value is the value to write
	 * @throws IOException on error.
	 */
	public void writeLEInt(int value) throws IOException {
		for (final byte b : EndianNumbers.parseLEInt(value)) {
			writeByte(b);
		}
	}

	/**
	 * Writes a Little Endian long on 8 bytes.
	 *
	 * @param value is the value to write
	 * @throws IOException on error.
	 */
	public void writeLELong(long value) throws IOException {
		for (final byte b : EndianNumbers.parseLELong(value)) {
			writeByte(b);
		}
	}

	/**
	 * Writes a Little Endian short on 2 bytes.
	 *
	 * @param value is the value to write
	 * @throws IOException on error.
	 */
	public void writeLEShort(short value) throws IOException {
		for (final byte b : EndianNumbers.parseLEShort(value)) {
			writeByte(b);
		}
	}

}
