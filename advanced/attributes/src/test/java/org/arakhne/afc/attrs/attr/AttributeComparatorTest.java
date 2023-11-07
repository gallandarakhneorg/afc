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

package org.arakhne.afc.attrs.attr;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.arakhne.afc.testtools.AbstractTestCase;

@SuppressWarnings("all")
public class AttributeComparatorTest extends AbstractTestCase {

	@Test
	public void testCompareValues() throws AttributeException {
		double base_d = Math.random();
		long base_l = (long)base_d;
		AttributeValueImpl attr1 = new AttributeValueImpl(base_d);		
		AttributeValueImpl attr2 = new AttributeValueImpl(base_d+1);
		AttributeValueImpl attr3 = new AttributeValueImpl(base_d-1);
		
		AttributeValueImpl attr4 = new AttributeValueImpl(base_l);		
		AttributeValueImpl attr5 = new AttributeValueImpl(attr1.getInteger());
		
		AttributeValueImpl attr6 = new AttributeValueImpl("bonjour");  //$NON-NLS-1$
		
		AttributeComparator comp = new AttributeComparator();
			
		//----------- attr1 -> *
		assertEquals(0, comp.compare(attr1,attr1));
		assertStrictlyNegative(comp.compare(attr1,attr2));
		assertStrictlyPositive(comp.compare(attr1,attr3));
		assertPositive(comp.compare(attr1,attr4));
		assertPositive(comp.compare(attr1,attr5));
		assertStrictlyNegative(comp.compare(attr1,attr6));

		//----------- attr2 -> *
		assertStrictlyPositive(comp.compare(attr2,attr1));
		assertEquals(0, comp.compare(attr2,attr2));
		assertStrictlyPositive(comp.compare(attr2,attr3));
		assertStrictlyPositive(comp.compare(attr2,attr4));
		assertStrictlyPositive(comp.compare(attr2,attr5));
		assertStrictlyNegative(comp.compare(attr2,attr6));

		//----------- attr3 -> *
		assertStrictlyNegative(comp.compare(attr3,attr1));
		assertStrictlyNegative(comp.compare(attr3,attr2));
		assertEquals(0, comp.compare(attr3,attr3));
		assertStrictlyNegative(comp.compare(attr3,attr4));
		assertStrictlyNegative(comp.compare(attr3,attr5));
		assertStrictlyNegative(comp.compare(attr3,attr6));
		

		//----------- attr4 -> *
		assertNegative(comp.compare(attr4,attr1));
		assertStrictlyNegative(comp.compare(attr4,attr2));
		assertStrictlyPositive(comp.compare(attr4,attr3));
		assertEquals(0, comp.compare(attr4,attr4));
		assertEquals(0,comp.compare(attr4,attr5));
		assertStrictlyNegative(comp.compare(attr4,attr6));

		//----------- attr5 -> *
		assertNegative(comp.compare(attr5,attr1));
		assertStrictlyNegative(comp.compare(attr5,attr2));
		assertStrictlyPositive(comp.compare(attr5,attr3));
		assertEquals(0,comp.compare(attr5,attr4));
		assertEquals(0, comp.compare(attr5,attr5));
		assertStrictlyNegative(comp.compare(attr5,attr6));

		//----------- attr6 -> *
		assertStrictlyPositive(comp.compare(attr6,attr1));
		assertStrictlyPositive(comp.compare(attr6,attr2));
		assertStrictlyPositive(comp.compare(attr6,attr3));
		assertStrictlyPositive(comp.compare(attr6,attr4));
		assertStrictlyPositive(comp.compare(attr6,attr5));
		assertEquals(0, comp.compare(attr6,attr6));
	}

	@Test
	public void compare() {
		AttributeComparator comp = new AttributeComparator();
		
		for(int i=5; i<50; ++i) {
			String name1 = randomString();
			String name2 = randomString();
			String msg = "("+name1+"<=>"+name2+")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int cmpResult = name1.compareTo(name2);
			
			Attribute attr1 = new AttributeImpl(name1,1);
			Attribute attr2 = new AttributeImpl(name2,1);
			Attribute attr3 = new AttributeImpl(name1,1);
			Attribute attr4 = new AttributeImpl(name1,2);
			
			assertEquals(0,comp.compare(attr1, attr1), msg);
			assertEquals(cmpResult,comp.compare(attr1, attr2), msg);
			assertEquals(0,comp.compare(attr1, attr3), msg);
			assertStrictlyNegative(comp.compare(attr1, attr4), msg);
			
			assertEquals(-cmpResult,comp.compare(attr2, attr1), msg);
			assertEquals(0,comp.compare(attr2, attr2), msg);
			assertEquals(-cmpResult,comp.compare(attr2, attr3), msg);
			assertEquals(-cmpResult,comp.compare(attr2, attr4), msg);

			assertEquals(0,comp.compare(attr3, attr1), msg);
			assertEquals(cmpResult,comp.compare(attr3, attr2), msg);
			assertEquals(0,comp.compare(attr3, attr3), msg);
			assertStrictlyNegative(comp.compare(attr3, attr4), msg);

			assertStrictlyPositive(comp.compare(attr4, attr1), msg);
			assertEquals(cmpResult,comp.compare(attr4, attr2), msg);
			assertStrictlyPositive(comp.compare(attr4, attr3), msg);
			assertEquals(0,comp.compare(attr4, attr4), msg);
		}
	}

}
