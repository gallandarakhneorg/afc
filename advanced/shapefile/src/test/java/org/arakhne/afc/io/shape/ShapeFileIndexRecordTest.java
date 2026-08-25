/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.io.shape;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.io.shape.ShapeFileIndexRecord;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("ShapeFileIndexRecord")
@SuppressWarnings("all")
public class ShapeFileIndexRecordTest extends AbstractIoShapeTestCase {

	private int offset, length;
	private ShapeFileIndexRecord record;
	
	@BeforeEach
	public void setUp() throws Exception {
		offset = 10;
		length = 16;
		record = new ShapeFileIndexRecord(offset, length, false, -1);
	}

	@AfterEach
	public void tearDown() throws Exception {
		record = null;
	}

	@DisplayName("getRecordContentLength")
	@Nested
	public class GetRecordContentLength {

		@Test
		public void testRecordContentLength() {
			assertEquals(
					((length%2)!=0 ? length+1 : length) - 8,
					record.getRecordContentLength());
		}
	}

	@DisplayName("getEntireRecordLength")
	@Nested
	public class GetEntireRecordLength {

		@Test
		public void testEntireRecordLength() {
			assertEquals(
					((length%2)!=0 ? length+1 : length),
					record.getEntireRecordLength());
		}
	}

	@DisplayName("getOffsetInFile")
	@Nested
	public class GetOffsetInFile {

		@Test
		public void testGetOffsetInFile() {
			assertEquals(
					((offset%2)!=0 ? offset+1 : offset),
					record.getOffsetInFile());
		}
	}

	@DisplayName("getOffsetInContent")
	@Nested
	public class GetOffsetInContent {

		@Test
		public void testGetOffsetInContent() {
			assertEquals(
					((offset%2)!=0 ? offset+1 : offset) - 100,
					record.getOffsetInContent());
		}
	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsTest {

		private ShapeFileIndexRecord sameRecord;
		private ShapeFileIndexRecord sameOffsetLengthPlus2;
		private ShapeFileIndexRecord sameOffsetLengthMinus2;

		private ShapeFileIndexRecord offsetPlus2SameLength;
		private ShapeFileIndexRecord offsetPlus2LengthPlus2;
		private ShapeFileIndexRecord offsetPlus2LengthMinus2;

		private ShapeFileIndexRecord offsetMinus2SameLength;
		private ShapeFileIndexRecord offsetMinus2LengthPlus2;
		private ShapeFileIndexRecord offsetMinus2LengthMinus2;

		@BeforeEach
		public void setUp() {
			sameRecord = new ShapeFileIndexRecord(offset, length, false, -1);
			sameOffsetLengthPlus2 = new ShapeFileIndexRecord(offset, length + 2, false, -1);
			sameOffsetLengthMinus2 = new ShapeFileIndexRecord(offset, length - 2, false, -1);

			offsetPlus2SameLength = new ShapeFileIndexRecord(offset + 2, length, false, -1);
			offsetPlus2LengthPlus2 = new ShapeFileIndexRecord(offset + 2, length + 2, false, -1);
			offsetPlus2LengthMinus2 = new ShapeFileIndexRecord(offset + 2, length - 2, false, -1);

			offsetMinus2SameLength = new ShapeFileIndexRecord(offset - 2, length, false, -1);
			offsetMinus2LengthPlus2 = new ShapeFileIndexRecord(offset - 2, length + 2, false, -1);
			offsetMinus2LengthMinus2 = new ShapeFileIndexRecord(offset - 2, length - 2, false, -1);
		}

		@DisplayName("equals null")
		@Test
		public void testEqualsObject_equalsNull() {
			assertFalse(record.equals(null));
		}

		@DisplayName("equals different type")
		@Test
		public void testEqualsObject_equalsDifferentType() {
			assertFalse(record.equals(new Object()));
		}

		@DisplayName("equals itself")
		@Test
		public void testEqualsObject_equalsItself() {
			assertTrue(record.equals(record));
		}

		@DisplayName("equals same offset and length")
		@Test
		public void testEqualsObject_equalsSameOffsetAndLength() {
			assertTrue(record.equals(sameRecord));
		}

		@DisplayName("not equals same offset length+2")
		@Test
		public void testEqualsObject_notEqualsSameOffsetLengthPlus2() {
			assertFalse(record.equals(sameOffsetLengthPlus2));
		}

		@DisplayName("not equals same offset length-2")
		@Test
		public void testEqualsObject_notEqualsSameOffsetLengthMinus2() {
			assertFalse(record.equals(sameOffsetLengthMinus2));
		}

		@DisplayName("not equals offset+2 same length")
		@Test
		public void testEqualsObject_notEqualsOffsetPlus2SameLength() {
			assertFalse(record.equals(offsetPlus2SameLength));
		}

		@DisplayName("not equals offset+2 length+2")
		@Test
		public void testEqualsObject_notEqualsOffsetPlus2LengthPlus2() {
			assertFalse(record.equals(offsetPlus2LengthPlus2));
		}

		@DisplayName("not equals offset+2 length-2")
		@Test
		public void testEqualsObject_notEqualsOffsetPlus2LengthMinus2() {
			assertFalse(record.equals(offsetPlus2LengthMinus2));
		}

		@DisplayName("not equals offset-2 same length")
		@Test
		public void testEqualsObject_notEqualsOffsetMinus2SameLength() {
			assertFalse(record.equals(offsetMinus2SameLength));
		}

		@DisplayName("not equals offset-2 length+2")
		@Test
		public void testEqualsObject_notEqualsOffsetMinus2LengthPlus2() {
			assertFalse(record.equals(offsetMinus2LengthPlus2));
		}

		@DisplayName("not equals offset-2 length-2")
		@Test
		public void testEqualsObject_notEqualsOffsetMinus2LengthMinus2() {
			assertFalse(record.equals(offsetMinus2LengthMinus2));
		}
	}

	@DisplayName("compareTo")
	@Nested
	public class CompareTo {

		private ShapeFileIndexRecord sameRecord;
		private ShapeFileIndexRecord sameOffsetLengthPlus2;
		private ShapeFileIndexRecord sameOffsetLengthMinus2;

		private ShapeFileIndexRecord offsetPlus2SameLength;
		private ShapeFileIndexRecord offsetPlus2LengthPlus2;
		private ShapeFileIndexRecord offsetPlus2LengthMinus2;

		private ShapeFileIndexRecord offsetMinus2SameLength;
		private ShapeFileIndexRecord offsetMinus2LengthPlus2;
		private ShapeFileIndexRecord offsetMinus2LengthMinus2;

		@BeforeEach
		public void setUp() {
			sameRecord = new ShapeFileIndexRecord(offset, length, false, -1);
			sameOffsetLengthPlus2 = new ShapeFileIndexRecord(offset, length + 2, false, -1);
			sameOffsetLengthMinus2 = new ShapeFileIndexRecord(offset, length - 2, false, -1);

			offsetPlus2SameLength = new ShapeFileIndexRecord(offset + 2, length, false, -1);
			offsetPlus2LengthPlus2 = new ShapeFileIndexRecord(offset + 2, length + 2, false, -1);
			offsetPlus2LengthMinus2 = new ShapeFileIndexRecord(offset + 2, length - 2, false, -1);

			offsetMinus2SameLength = new ShapeFileIndexRecord(offset - 2, length, false, -1);
			offsetMinus2LengthPlus2 = new ShapeFileIndexRecord(offset - 2, length + 2, false, -1);
			offsetMinus2LengthMinus2 = new ShapeFileIndexRecord(offset - 2, length - 2, false, -1);
		}

		@DisplayName("compareTo null")
		@Test
		public void testCompareTo_compareToNull() {
			assertStrictlyNegative(record.compareTo(null));
		}

		@DisplayName("compareTo itself")
		@Test
		public void testCompareTo_compareToItself() {
			assertZero(record.compareTo(record));
		}

		@DisplayName("compareTo same offset and length")
		@Test
		public void testCompareTo_compareToSameOffsetAndLength() {
			assertZero(record.compareTo(sameRecord));
		}

		@DisplayName("compareTo same offset length+2")
		@Test
		public void testCompareTo_compareToSameOffsetLengthPlus2() {
			assertStrictlyNegative(record.compareTo(sameOffsetLengthPlus2));
		}

		@DisplayName("compareTo same offset length-2")
		@Test
		public void testCompareTo_compareToSameOffsetLengthMinus2() {
			assertStrictlyPositive(record.compareTo(sameOffsetLengthMinus2));
		}

		@DisplayName("compareTo offset+2 same length")
		@Test
		public void testCompareTo_compareToOffsetPlus2SameLength() {
			assertStrictlyNegative(record.compareTo(offsetPlus2SameLength));
		}

		@DisplayName("compareTo offset+2 length+2")
		@Test
		public void testCompareTo_compareToOffsetPlus2LengthPlus2() {
			assertStrictlyNegative(record.compareTo(offsetPlus2LengthPlus2));
		}

		@DisplayName("compareTo offset+2 length-2")
		@Test
		public void testCompareTo_compareToOffsetPlus2LengthMinus2() {
			assertStrictlyNegative(record.compareTo(offsetPlus2LengthMinus2));
		}

		@DisplayName("compareTo offset-2 same length")
		@Test
		public void testCompareTo_compareToOffsetMinus2SameLength() {
			assertStrictlyPositive(record.compareTo(offsetMinus2SameLength));
		}

		@DisplayName("compareTo offset-2 length+2")
		@Test
		public void testCompareTo_compareToOffsetMinus2LengthPlus2() {
			assertStrictlyPositive(record.compareTo(offsetMinus2LengthPlus2));
		}

		@DisplayName("compareTo offset-2 length-2")
		@Test
		public void testCompareTo_compareToOffsetMinus2LengthMinus2() {
			assertStrictlyPositive(record.compareTo(offsetMinus2LengthMinus2));
		}
	}

	@DisplayName("this <=> record")
	@Nested
	public class OperatorSpaceShipRecord {

		private ShapeFileIndexRecord sameRecord;
		private ShapeFileIndexRecord sameOffsetLengthPlus2;
		private ShapeFileIndexRecord sameOffsetLengthMinus2;

		private ShapeFileIndexRecord offsetPlus2SameLength;
		private ShapeFileIndexRecord offsetPlus2LengthPlus2;
		private ShapeFileIndexRecord offsetPlus2LengthMinus2;

		private ShapeFileIndexRecord offsetMinus2SameLength;
		private ShapeFileIndexRecord offsetMinus2LengthPlus2;
		private ShapeFileIndexRecord offsetMinus2LengthMinus2;

		@BeforeEach
		public void setUp() {
			sameRecord = new ShapeFileIndexRecord(offset, length, false, -1);
			sameOffsetLengthPlus2 = new ShapeFileIndexRecord(offset, length + 2, false, -1);
			sameOffsetLengthMinus2 = new ShapeFileIndexRecord(offset, length - 2, false, -1);

			offsetPlus2SameLength = new ShapeFileIndexRecord(offset + 2, length, false, -1);
			offsetPlus2LengthPlus2 = new ShapeFileIndexRecord(offset + 2, length + 2, false, -1);
			offsetPlus2LengthMinus2 = new ShapeFileIndexRecord(offset + 2, length - 2, false, -1);

			offsetMinus2SameLength = new ShapeFileIndexRecord(offset - 2, length, false, -1);
			offsetMinus2LengthPlus2 = new ShapeFileIndexRecord(offset - 2, length + 2, false, -1);
			offsetMinus2LengthMinus2 = new ShapeFileIndexRecord(offset - 2, length - 2, false, -1);
		}

		@DisplayName("compareTo null")
		@Test
		public void testCompareTo_compareToNull() {
			assertStrictlyNegative(record.compareTo(null));
		}

		@DisplayName("compareTo itself")
		@Test
		public void testCompareTo_compareToItself() {
			assertZero(record.operator_spaceship(record));
		}

		@DisplayName("compareTo same offset and length")
		@Test
		public void testCompareTo_compareToSameOffsetAndLength() {
			assertZero(record.operator_spaceship(sameRecord));
		}

		@DisplayName("compareTo same offset length+2")
		@Test
		public void testCompareTo_compareToSameOffsetLengthPlus2() {
			assertStrictlyNegative(record.operator_spaceship(sameOffsetLengthPlus2));
		}

		@DisplayName("compareTo same offset length-2")
		@Test
		public void testCompareTo_compareToSameOffsetLengthMinus2() {
			assertStrictlyPositive(record.operator_spaceship(sameOffsetLengthMinus2));
		}

		@DisplayName("compareTo offset+2 same length")
		@Test
		public void testCompareTo_compareToOffsetPlus2SameLength() {
			assertStrictlyNegative(record.operator_spaceship(offsetPlus2SameLength));
		}

		@DisplayName("compareTo offset+2 length+2")
		@Test
		public void testCompareTo_compareToOffsetPlus2LengthPlus2() {
			assertStrictlyNegative(record.operator_spaceship(offsetPlus2LengthPlus2));
		}

		@DisplayName("compareTo offset+2 length-2")
		@Test
		public void testCompareTo_compareToOffsetPlus2LengthMinus2() {
			assertStrictlyNegative(record.operator_spaceship(offsetPlus2LengthMinus2));
		}

		@DisplayName("compareTo offset-2 same length")
		@Test
		public void testCompareTo_compareToOffsetMinus2SameLength() {
			assertStrictlyPositive(record.operator_spaceship(offsetMinus2SameLength));
		}

		@DisplayName("compareTo offset-2 length+2")
		@Test
		public void testCompareTo_compareToOffsetMinus2LengthPlus2() {
			assertStrictlyPositive(record.operator_spaceship(offsetMinus2LengthPlus2));
		}

		@DisplayName("compareTo offset-2 length-2")
		@Test
		public void testCompareTo_compareToOffsetMinus2LengthMinus2() {
			assertStrictlyPositive(record.operator_spaceship(offsetMinus2LengthMinus2));
		}
	}
}
