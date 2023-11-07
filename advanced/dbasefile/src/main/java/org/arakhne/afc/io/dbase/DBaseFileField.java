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

package org.arakhne.afc.io.dbase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.vmutil.Resources;

/**
 * This class permits to store a field of a DBF file.
 *
 * <p>No warranty for the files that are not generated
 * by an ESRI software.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class DBaseFileField {

	/** List of values that describes an unset record value.
	 * The strings inside this list are assumed to be
	 * case insensitive.
	 */
	private static final Set<String> UNSET_VALUES = new TreeSet<>(new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return Integer.MIN_VALUE;
			}
			if (o2 == null) {
				return Integer.MAX_VALUE;
			}
			return o1.compareToIgnoreCase(o2);
		}
	});

	static {
		try {
			final String resourceName = DBaseFileField.class.getCanonicalName().replace('.', '/')
					+ "_unsetValues.txt"; //$NON-NLS-1$
			final URL url = Resources.getResource(resourceName);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = reader.readLine();
			while (line != null) {
				line = line.trim();
				if (!"".equals(line)) { //$NON-NLS-1$
					UNSET_VALUES.add(line);
				}
				line = reader.readLine();
			}
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	private final String name;

	private final DBaseFieldType type;

	private int length;

	private int decimal;

	private int columnIndex;

	/** Constructor.
	 *
	 * @param name is the name of the field
	 * @param type is the dBase type of the field
	 */
	DBaseFileField(String name, DBaseFieldType type) {
		this(name, type, type.getFieldSize(), type.getDecimalPointPosition());
	}

	/** Constructor.
	 *
	 * @param name is the name of the field
	 * @param type is the dBase type of the field
	 * @param length is the size of the field
	 * @param decimal_pos is the position of the decimal point, if any
	 * @param columnIndex is the index of the column that corresponds to this field in the dBase file.
	 */
	public DBaseFileField(String name, DBaseFieldType type, int length, int decimal_pos, int columnIndex) {
		this.name = name;
		this.type = type;
		this.length = length;
		this.decimal = decimal_pos;
		this.columnIndex = columnIndex;
	}

	/** Constructor.
	 *
	 * @param name is the name of the field
	 * @param type is the dBase type of the field
	 * @param length is the size of the field
	 * @param decimal_pos is the position of the decimal point, if any
	 */
	public DBaseFileField(String name, DBaseFieldType type, int length, int decimal_pos) {
		this.name = name;
		this.type = type;
		this.length = length;
		this.decimal = decimal_pos;
		this.columnIndex = -1;
	}

	/** Replies if the given value corresponds to an unset value
	 * inside the dBase file.
	 *
	 * @param value is the value to test.
	 * @return {@code true} if the given value stands for "unset value";
	 *     otherwise {@code false}.
	 */
	@Pure
	public static boolean isUnsetValue(String value) {
		return UNSET_VALUES.contains(value.trim());
	}

	/** Replies the index of this column.
	 *
	 * @return the index of this column.
	 */
	@Pure
	public int getColumnIndex() {
		return this.columnIndex;
	}

	/** Set the index of this column.
	 *
	 * @param columnIndex the index.
	 */
	void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	@Pure
	@Override
	public String toString() {
		return this.name;
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DBaseFileField) {
			return this.name.equals(((DBaseFileField) obj).getName());
		}
		if (obj instanceof CharSequence) {
			return this.name.equals(obj);
		}
		return false;
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.name != null ? this.name.hashCode() : 0);
        return result;
	}

	/** Replies the name of the field.
	 *
	 * @return the name of the field.
	 */
	@Pure
	public String getName() {
		return this.name;
	}

	/** Replies the type of the field.
	 *
	 * @return the type of the field.
	 */
	@Pure
	public DBaseFieldType getType() {
		return this.type;
	}

	/** Replies the length of the field.
	 *
	 * @return the length of the field.
	 */
	@Pure
	public int getLength() {
		if (this.type == DBaseFieldType.STRING) {
			return this.length + this.decimal;
		}
		return this.length;
	}

	/** Replies the position of the decimal point for this field.
	 *
	 * @return the position of the decimal point for this field.
	 */
	@Pure
	public int getDecimalPointPosition() {
		if (this.type == DBaseFieldType.STRING) {
			return 0;
		}
		return this.decimal;
	}

	/** Replies the attribute type corresponding to the field type.
	 *
	 * @return the attribute type corresponding to the field type.
	 */
	@Pure
	public AttributeType getAttributeType() {
		return getType().toAttributeType();
	}

	/** Update the sizes with the given ones if and only if
	 * they are greater than the existing ones.
	 * This test is done according to the type of the field.
	 *
	 * @param fieldLength is the size of this field
	 * @param decimalPointPosition is the position of the decimal point.
	 */
	@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	void updateSizes(int fieldLength, int decimalPointPosition) {
		switch (this.type) {
		case STRING:
			if (fieldLength > this.length) {
				this.length = fieldLength;
			}
			break;
		case FLOATING_NUMBER:
		case NUMBER:
			if (decimalPointPosition > this.decimal) {
				final int intPart = this.length - this.decimal;
				this.decimal = decimalPointPosition;
				this.length = intPart + this.decimal;
			}
			if (fieldLength > this.length) {
				this.length = fieldLength;
			}
			break;
			//$CASES-OMITTED$
		default:
			// Do nothing
		}
	}

}
