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

package org.arakhne.afc.io.dbase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.AttributeType;

/** Describes the supported types in DBase fields.
 *
 * <pre>
 * -----------------------------------------------------------
 * 			Field Type (1 byte)
 * -----------------------------------------------------------
 *  Char. Size    Type     Content
 * -----------------------------------------------------------
 *  C     1..n    STRING   ASCII fill with spaces (not final
 *                         \0 character).
 *                         n=64kb (using deci. counter)
 *                         n=32kb (using deci. counter)
 *                         n=254
 *  D        8    DATE     [0-9]{8} with in format YYYYMMDD
 *  F     1..n    NUMERIC  [-.0-9]+, variable position
 *                         of floating position
 *  N     1..n    NUMERIC  [-.0-9]+, fixed position,
 *                         no floating position
 *  L        1    LOGICAL  [TtFfYyNn ?]
 *  M       10    MEMORY   10 digits representing the start
 *                         block position in the .dbt file,
 *                         or 10 spaces if no entry in memo
 *  V       10    VARIABLE data in .dbv file,
 *                         4 bytes: start pos in memory
 *                         4 bytes: block size
 *                         1 byte:  subtype
 *                         1 byte:  reserved (0x1A)
 *                         10 spaces if no entry in .dbv
 *  P       10    PICTURE  binary data in .ftp file (same
 *                         structure as M type)
 *  B       10    BINARY   binary data in .dbt file (same
 *                         structure as M type)
 *  G       10    GENERAL  OLE objects (same
 *                         structure as M type)
 *  2        2    NUMERIC  short integer +/- 32762
 *  4        4    NUMERIC  long integer +/- 2147483647
 *  8        8    NUMERIC  signed double IEEE
 * -----------------------------------------------------------
 * </pre>
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public enum DBaseFieldType {
	/** OLE Object. */
	GENERAL('G'),
	/** String. */
	STRING('C'),
	/** Date. */
	DATE('D'),
	/** Floating number. */
	FLOATING_NUMBER('F'),
	/** No-floating number. */
	NUMBER('N'),
	/** Boolean. */
	BOOLEAN('L'),
	/** 2 byte integer. */
	INTEGER_2BYTES('2'),
	/** 4 byte integer. */
	INTEGER_4BYTES('4'),
	/** IEEE signer double. */
	DOUBLE('8'),
	/** Memory block. */
	MEMORY('M'),
	/** Variable block. */
	VARIABLE('V'),
	/** Picture block. */
	PICTURE('P'),
	/** Binary block. */
	BINARY('B');

	private final byte byteCode;

	/** Constructor.
	 *
	 * @param code the field type code.
	 */
	DBaseFieldType(char code) {
		this.byteCode = (byte) code;
	}

	/** Replies the field type that corresponds to the specified byte.
	 *
	 * @param abyte is a byte that describes the dBase field type.
	 * @return the corresponding type.
	 * @throws DBaseFileException  if the given byte is unknown.
	 */
	@Pure
	public static DBaseFieldType fromByte(byte abyte) throws DBaseFileException {
		for (final DBaseFieldType type : DBaseFieldType.values()) {
			if (type.byteCode == abyte) {
				return type;
			}
		}
		throw new InvalidDBaseFieldTypeException(abyte);
	}

	/** Replies the byte representation of this type.
	 *
	 * @return the byte used by dBase to represent this type of field.
	 */
	@Pure
	public byte toByte() {
		return this.byteCode;
	}

	/** Replies the attribute type corresponding to the field type.
	 *
	 * @return the associated attribute type.
	 */
	@Pure
	public AttributeType toAttributeType() {
		switch (this) {
		case STRING:
			return AttributeType.STRING;

		case DATE:
			return AttributeType.DATE;

		case BOOLEAN:
			return AttributeType.BOOLEAN;

		case FLOATING_NUMBER:
		case NUMBER:
		case DOUBLE:
			return AttributeType.REAL;

		case INTEGER_2BYTES:
		case INTEGER_4BYTES:
			return AttributeType.INTEGER;

		case GENERAL:
		case MEMORY:
		case VARIABLE:
		case PICTURE:
		case BINARY:
			return AttributeType.OBJECT;
		default:
			throw new IllegalStateException();
		}
	}

	/** Replies the field type corresponding to the attribute type.
	 *
	 * @param type is the attribute type to convert
	 * @return the dBase field type that corresponds to the given attribute type.
	 * @throws DBaseFileException if the given type is unknown.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity"})
	public static DBaseFieldType fromAttributeType(AttributeType type) throws DBaseFileException {
		switch (type) {
		case UUID:
		case URL:
		case URI:
		case POINT:
		case POINT3D:
		case POLYLINE:
		case POLYLINE3D:
		case STRING:
		case ENUMERATION:
		case INET_ADDRESS:
		case TYPE:
			return DBaseFieldType.STRING;
		case DATE:
			return DBaseFieldType.DATE;
		case BOOLEAN:
			return DBaseFieldType.BOOLEAN;
		case REAL:
		case INTEGER:
		case TIMESTAMP:
			return DBaseFieldType.NUMBER;
		case OBJECT:
			return DBaseFieldType.BINARY;
		default:
			throw new InvalidDBaseFieldTypeException(type);
		}
	}

	/** Replies the size in bytes of a field that corresponds to this type.
	 *
	 * @return the size in bytes of a field that corresponds to this type.
	 */
	@Pure
	public int getFieldSize() {
		return getFieldSize(null);
	}

	/** Replies the size in bytes of a field that corresponds to this type.
	 *
	 * @param value the value.
	 * @return the size in bytes of a field that corresponds to this type.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:magicnumber"})
	public int getFieldSize(String value) {
		int size = 0;
		switch (this) {
		case VARIABLE:
		case GENERAL:
		case PICTURE:
		case MEMORY:
		case BINARY:
			return 10;
		case BOOLEAN:
			return 1;
		case DOUBLE:
		case DATE:
			return 8;
		case INTEGER_2BYTES:
			return 2;
		case INTEGER_4BYTES:
			return 4;
		case STRING:
			size = (value == null) ? 0 : Math.min(0xFFFF, value.length());
			break;
		case FLOATING_NUMBER:
		case NUMBER:
			size = (value == null) ? 1 : Math.min(0xFF, value.length() + 1);
			if (size < 2) {
				// To put a zero digit and a sign at least
				size = 2;
			}
			break;
		default:
			throw new IllegalStateException();
		}
		return size;
	}

	/** Replies the position of the decimal point of a field that corresponds to this type.
	 *
	 * @return the position of the decimal point.
	 */
	@Pure
	public int getDecimalPointPosition() {
		return getDecimalPointPosition(null);
	}

	/** Replies the position of the decimal point of a field that corresponds to this type.
	 *
	 * @param value the value.
	 * @return the position of the decimal point
	 */
	@Pure
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity"})
	public int getDecimalPointPosition(String value) {
		int position = 0;
		switch (this) {
		case VARIABLE:
		case GENERAL:
		case PICTURE:
		case MEMORY:
		case BINARY:
		case BOOLEAN:
		case DOUBLE:
		case DATE:
		case INTEGER_2BYTES:
		case INTEGER_4BYTES:
		case STRING:
			break;
		case FLOATING_NUMBER:
		case NUMBER:
			if (value != null) {
				final int idx = value.indexOf('.');
				if (idx != -1) {
					// Remove .0 part of the number
					final Pattern pattern = Pattern.compile("[.,]0*$"); //$NON-NLS-1$
					final Matcher matcher = pattern.matcher(value);
					if (!matcher.find()) {
						position = value.length() - idx - 1;
					}
				}
			}
			break;
		default:
			throw new IllegalStateException();
		}
		return position;
	}

}

