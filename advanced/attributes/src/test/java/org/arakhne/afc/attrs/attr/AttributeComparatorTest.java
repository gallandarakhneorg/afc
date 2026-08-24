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

package org.arakhne.afc.attrs.attr;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.arakhne.afc.testtools.AbstractTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AttributeComparator")
@SuppressWarnings("all")
public class AttributeComparatorTest extends AbstractTestCase {

	@DisplayName("Value comparison")
	@Nested
	public class ValueComparison {

		private double base_d;
		private long base_l;
		private AttributeValueImpl attr1;
		private AttributeValueImpl attr2;
		private AttributeValueImpl attr3;
		private AttributeValueImpl attr4;
		private AttributeValueImpl attr5;
		private AttributeValueImpl attr6;
		private AttributeComparator comp;

		@BeforeEach
		public void setUp() throws AttributeException {
			base_d = Math.random();
			base_l = (long) base_d;
			attr1 = new AttributeValueImpl(base_d);
			attr2 = new AttributeValueImpl(base_d + 1);
			attr3 = new AttributeValueImpl(base_d - 1);

			attr4 = new AttributeValueImpl(base_l);
			attr5 = new AttributeValueImpl(attr1.getInteger());

			attr6 = new AttributeValueImpl("bonjour"); //$NON-NLS-1$

			comp = new AttributeComparator();
		}

		@DisplayName("testCompareValues_1")
		@Test
		public void testCompareValues_1() {
			assertEquals(0, comp.compare(attr1, attr1));
		}

		@DisplayName("testCompareValues_2")
		@Test
		public void testCompareValues_2() {
			assertStrictlyNegative(comp.compare(attr1, attr2));
		}

		@DisplayName("testCompareValues_3")
		@Test
		public void testCompareValues_3() {
			assertStrictlyPositive(comp.compare(attr1, attr3));
		}

		@DisplayName("testCompareValues_4")
		@Test
		public void testCompareValues_4() {
			assertPositive(comp.compare(attr1, attr4));
		}

		@DisplayName("testCompareValues_5")
		@Test
		public void testCompareValues_5() {
			assertPositive(comp.compare(attr1, attr5));
		}

		@DisplayName("testCompareValues_6")
		@Test
		public void testCompareValues_6() {
			assertStrictlyNegative(comp.compare(attr1, attr6));
		}

		@DisplayName("testCompareValues_7")
		@Test
		public void testCompareValues_7() {
			assertStrictlyPositive(comp.compare(attr2, attr1));
		}

		@DisplayName("testCompareValues_8")
		@Test
		public void testCompareValues_8() {
			assertEquals(0, comp.compare(attr2, attr2));
		}

		@DisplayName("testCompareValues_9")
		@Test
		public void testCompareValues_9() {
			assertStrictlyPositive(comp.compare(attr2, attr3));
		}

		@DisplayName("testCompareValues_10")
		@Test
		public void testCompareValues_10() {
			assertStrictlyPositive(comp.compare(attr2, attr4));
		}

		@DisplayName("testCompareValues_11")
		@Test
		public void testCompareValues_11() {
			assertStrictlyPositive(comp.compare(attr2, attr5));
		}

		@DisplayName("testCompareValues_12")
		@Test
		public void testCompareValues_12() {
			assertStrictlyNegative(comp.compare(attr2, attr6));
		}

		@DisplayName("testCompareValues_13")
		@Test
		public void testCompareValues_13() {
			assertStrictlyNegative(comp.compare(attr3, attr1));
		}

		@DisplayName("testCompareValues_14")
		@Test
		public void testCompareValues_14() {
			assertStrictlyNegative(comp.compare(attr3, attr2));
		}

		@DisplayName("testCompareValues_15")
		@Test
		public void testCompareValues_15() {
			assertEquals(0, comp.compare(attr3, attr3));
		}

		@DisplayName("testCompareValues_16")
		@Test
		public void testCompareValues_16() {
			assertStrictlyNegative(comp.compare(attr3, attr4));
		}

		@DisplayName("testCompareValues_17")
		@Test
		public void testCompareValues_17() {
			assertStrictlyNegative(comp.compare(attr3, attr5));
		}

		@DisplayName("testCompareValues_18")
		@Test
		public void testCompareValues_18() {
			assertStrictlyNegative(comp.compare(attr3, attr6));
		}

		@DisplayName("testCompareValues_19")
		@Test
		public void testCompareValues_19() {
			assertNegative(comp.compare(attr4, attr1));
		}

		@DisplayName("testCompareValues_20")
		@Test
		public void testCompareValues_20() {
			assertStrictlyNegative(comp.compare(attr4, attr2));
		}

		@DisplayName("testCompareValues_21")
		@Test
		public void testCompareValues_21() {
			assertStrictlyPositive(comp.compare(attr4, attr3));
		}

		@DisplayName("testCompareValues_22")
		@Test
		public void testCompareValues_22() {
			assertEquals(0, comp.compare(attr4, attr4));
		}

		@DisplayName("testCompareValues_23")
		@Test
		public void testCompareValues_23() {
			assertEquals(0, comp.compare(attr4, attr5));
		}

		@DisplayName("testCompareValues_24")
		@Test
		public void testCompareValues_24() {
			assertStrictlyNegative(comp.compare(attr4, attr6));
		}

		@DisplayName("testCompareValues_25")
		@Test
		public void testCompareValues_25() {
			assertNegative(comp.compare(attr5, attr1));
		}

		@DisplayName("testCompareValues_26")
		@Test
		public void testCompareValues_26() {
			assertStrictlyNegative(comp.compare(attr5, attr2));
		}

		@DisplayName("testCompareValues_27")
		@Test
		public void testCompareValues_27() {
			assertStrictlyPositive(comp.compare(attr5, attr3));
		}

		@DisplayName("testCompareValues_28")
		@Test
		public void testCompareValues_28() {
			assertEquals(0, comp.compare(attr5, attr4));
		}

		@DisplayName("testCompareValues_29")
		@Test
		public void testCompareValues_29() {
			assertEquals(0, comp.compare(attr5, attr5));
		}

		@DisplayName("testCompareValues_30")
		@Test
		public void testCompareValues_30() {
			assertStrictlyNegative(comp.compare(attr5, attr6));
		}

		@DisplayName("testCompareValues_31")
		@Test
		public void testCompareValues_31() {
			assertStrictlyPositive(comp.compare(attr6, attr1));
		}

		@DisplayName("testCompareValues_32")
		@Test
		public void testCompareValues_32() {
			assertStrictlyPositive(comp.compare(attr6, attr2));
		}

		@DisplayName("testCompareValues_33")
		@Test
		public void testCompareValues_33() {
			assertStrictlyPositive(comp.compare(attr6, attr3));
		}

		@DisplayName("testCompareValues_34")
		@Test
		public void testCompareValues_34() {
			assertStrictlyPositive(comp.compare(attr6, attr4));
		}

		@DisplayName("testCompareValues_35")
		@Test
		public void testCompareValues_35() {
			assertStrictlyPositive(comp.compare(attr6, attr5));
		}

		@DisplayName("testCompareValues_36")
		@Test
		public void testCompareValues_36() {
			assertEquals(0, comp.compare(attr6, attr6));
		}
	}

	@DisplayName("Attribute comparison")
	@Nested
	public class AttributeComparison {

		private String name1;
		private String name2;
		private String msg;
		private int cmpResult;

		private Attribute attr1;
		private Attribute attr2;
		private Attribute attr3;
		private Attribute attr4;

		private AttributeComparator comp;

		@BeforeEach
		public void setUp() {
			comp = new AttributeComparator();

			name1 = randomString();
			name2 = randomString();
			msg = "(" + name1 + "<=>" + name2 + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			cmpResult = name1.compareTo(name2);

			attr1 = new AttributeImpl(name1, 1);
			attr2 = new AttributeImpl(name2, 1);
			attr3 = new AttributeImpl(name1, 1);
			attr4 = new AttributeImpl(name1, 2);
		}

		@DisplayName("compare_1")
		@Test
		public void compare_1() {
			assertEquals(0, comp.compare(attr1, attr1), msg);
		}

		@DisplayName("compare_2")
		@Test
		public void compare_2() {
			assertEquals(cmpResult, comp.compare(attr1, attr2), msg);
		}

		@DisplayName("compare_3")
		@Test
		public void compare_3() {
			assertEquals(0, comp.compare(attr1, attr3), msg);
		}

		@DisplayName("compare_4")
		@Test
		public void compare_4() {
			assertStrictlyNegative(comp.compare(attr1, attr4), msg);
		}

		@DisplayName("compare_5")
		@Test
		public void compare_5() {
			assertEquals(-cmpResult, comp.compare(attr2, attr1), msg);
		}

		@DisplayName("compare_6")
		@Test
		public void compare_6() {
			assertEquals(0, comp.compare(attr2, attr2), msg);
		}

		@DisplayName("compare_7")
		@Test
		public void compare_7() {
			assertEquals(-cmpResult, comp.compare(attr2, attr3), msg);
		}

		@DisplayName("compare_8")
		@Test
		public void compare_8() {
			assertEquals(-cmpResult, comp.compare(attr2, attr4), msg);
		}

		@DisplayName("compare_9")
		@Test
		public void compare_9() {
			assertEquals(0, comp.compare(attr3, attr1), msg);
		}

		@DisplayName("compare_10")
		@Test
		public void compare_10() {
			assertEquals(cmpResult, comp.compare(attr3, attr2), msg);
		}

		@DisplayName("compare_11")
		@Test
		public void compare_11() {
			assertEquals(0, comp.compare(attr3, attr3), msg);
		}

		@DisplayName("compare_12")
		@Test
		public void compare_12() {
			assertStrictlyNegative(comp.compare(attr3, attr4), msg);
		}

		@DisplayName("compare_13")
		@Test
		public void compare_13() {
			assertStrictlyPositive(comp.compare(attr4, attr1), msg);
		}

		@DisplayName("compare_14")
		@Test
		public void compare_14() {
			assertEquals(cmpResult, comp.compare(attr4, attr2), msg);
		}

		@DisplayName("compare_15")
		@Test
		public void compare_15() {
			assertStrictlyPositive(comp.compare(attr4, attr3), msg);
		}

		@DisplayName("compare_16")
		@Test
		public void compare_16() {
			assertEquals(0, comp.compare(attr4, attr4), msg);
		}
	}
}
