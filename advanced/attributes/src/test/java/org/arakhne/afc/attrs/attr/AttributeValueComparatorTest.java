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
import org.junit.jupiter.api.Test;

@DisplayName("AttributeValueComparator")
@SuppressWarnings("all")
public class AttributeValueComparatorTest extends AbstractTestCase {

	private double base_d;
	private long base_l;
	private AttributeValueImpl attr1;		
	private AttributeValueImpl attr2;
	private AttributeValueImpl attr3;
	
	private AttributeValueImpl attr4;		
	private AttributeValueImpl attr5;
	
	private AttributeValueImpl attr6;
	
	private AttributeValueComparator comp;

	@BeforeEach
	public void setUp() throws Exception {
		base_d = Math.random();
		base_l = (long)base_d;
		attr1 = new AttributeValueImpl(base_d);		
		attr2 = new AttributeValueImpl(base_d+1);
		attr3 = new AttributeValueImpl(base_d-1);
		
		attr4 = new AttributeValueImpl(base_l);		
		attr5 = new AttributeValueImpl(attr1.getInteger());
		
		attr6 = new AttributeValueImpl("bonjour");  //$NON-NLS-1$
		
		comp = new AttributeValueComparator();
	}
	
	@DisplayName("attr1 <-> attr1")
	@Test
	public void attr1_attr1() throws AttributeException {
		assertEquals(0, comp.compare(attr1,attr1));
	}
	
	@DisplayName("attr1 <-> attr2")
	@Test
	public void attr1_attr2() throws AttributeException {
		assertStrictlyNegative(comp.compare(attr1,attr2));
	}
	
	@DisplayName("attr1 <-> attr3")
	@Test
	public void attr1_attr3() throws AttributeException {
		assertStrictlyPositive(comp.compare(attr1,attr3));
	}
	
	@DisplayName("attr1 <-> attr4")
	@Test
	public void attr1_attr4() throws AttributeException {
		assertPositive(comp.compare(attr1,attr4));
	}
	
	@DisplayName("attr1 <-> attr5")
	@Test
	public void attr1_attr5() throws AttributeException {
		assertPositive(comp.compare(attr1,attr5));
	}
	
	@DisplayName("attr1 <-> attr6")
	@Test
	public void attr1_attr6() throws AttributeException {
		assertStrictlyNegative(comp.compare(attr1,attr6));
	}
	
	@DisplayName("attr2 <-> attr1")
	@Test
	public void attr2_attr1() throws AttributeException {
		assertStrictlyPositive(comp.compare(attr2,attr1));
	}
	
	@DisplayName("attr2 <-> attr2")
	@Test
	public void attr2_attr2() throws AttributeException {
		assertEquals(0, comp.compare(attr2,attr2));
	}
	
	@DisplayName("attr2 <-> attr3")
	@Test
	public void attr2_attr3() throws AttributeException {
		assertStrictlyPositive(comp.compare(attr2,attr3));
	}
	
	@DisplayName("attr2 <-> attr4")
	@Test
	public void attr2_attr4() throws AttributeException {
		assertStrictlyPositive(comp.compare(attr2,attr4));
	}
	
	@DisplayName("attr2 <-> attr5")
	@Test
	public void attr2_attr5() throws AttributeException {
		assertStrictlyPositive(comp.compare(attr2,attr5));
	}
	
	@DisplayName("attr2 <-> attr6")
	@Test
	public void attr2_attr6() throws AttributeException {
		assertStrictlyNegative(comp.compare(attr2,attr6));
	}
	
	@DisplayName("attr3 <-> attr1")
	@Test
	public void attr3_attr1() throws AttributeException {
		assertStrictlyNegative(comp.compare(attr3,attr1));
	}
	
	@DisplayName("attr3 <-> attr2")
	@Test
	public void attr3_attr2() throws AttributeException {
		assertStrictlyNegative(comp.compare(attr3,attr2));
	}
	
	@DisplayName("attr3 <-> attr3")
	@Test
	public void attr3_attr3() throws AttributeException {
		assertEquals(0, comp.compare(attr3,attr3));
	}
	
	@DisplayName("attr3 <-> attr4")
	@Test
	public void attr3_attr4() throws AttributeException {
		assertStrictlyNegative(comp.compare(attr3,attr4));
	}
	
	@DisplayName("attr3 <-> attr5")
	@Test
	public void attr3_attr5() throws AttributeException {
		assertStrictlyNegative(comp.compare(attr3,attr5));
	}
	
	@DisplayName("attr3 <-> attr6")
	@Test
	public void attr3_attr6() throws AttributeException {
		assertStrictlyNegative(comp.compare(attr3,attr6));
	}
	
	@DisplayName("attr4 <-> attr1")
	@Test
	public void attr4_attr1() throws AttributeException {
		assertNegative(comp.compare(attr4,attr1));
	}
	
	@DisplayName("attr4 <-> attr2")
	@Test
	public void attr4_attr2() throws AttributeException {
		assertStrictlyNegative(comp.compare(attr4,attr2));
	}
	
	@DisplayName("attr4 <-> attr3")
	@Test
	public void attr4_attr3() throws AttributeException {
		assertStrictlyPositive(comp.compare(attr4,attr3));
	}
	
	@DisplayName("attr4 <-> attr4")
	@Test
	public void attr4_attr4() throws AttributeException {
		assertEquals(0, comp.compare(attr4,attr4));
	}
	
	@DisplayName("attr4 <-> attr5")
	@Test
	public void attr4_attr5() throws AttributeException {
		assertEquals(0,comp.compare(attr4,attr5));
	}
	
	@DisplayName("attr4 <-> attr6")
	@Test
	public void attr4_attr6() throws AttributeException {
		assertStrictlyNegative(comp.compare(attr4,attr6));
	}
	
	@DisplayName("attr5 <-> attr1")
	@Test
	public void attr5_attr1() throws AttributeException {
		assertNegative(comp.compare(attr5,attr1));
	}
	
	@DisplayName("attr5 <-> attr2")
	@Test
	public void attr5_attr2() throws AttributeException {
		assertStrictlyNegative(comp.compare(attr5,attr2));
	}
	
	@DisplayName("attr5 <-> attr3")
	@Test
	public void attr5_attr3() throws AttributeException {
		assertStrictlyPositive(comp.compare(attr5,attr3));
	}
	
	@DisplayName("attr5 <-> attr4")
	@Test
	public void attr5_attr4() throws AttributeException {
		assertEquals(0,comp.compare(attr5,attr4));
	}
	
	@DisplayName("attr5 <-> attr5")
	@Test
	public void attr5_attr5() throws AttributeException {
		assertEquals(0, comp.compare(attr5,attr5));
	}
	
	@DisplayName("attr5 <-> attr6")
	@Test
	public void attr5_attr6() throws AttributeException {
		assertStrictlyNegative(comp.compare(attr5,attr6));
	}
	
	@DisplayName("attr6 <-> attr1")
	@Test
	public void attr6_attr1() throws AttributeException {
		assertStrictlyPositive(comp.compare(attr6,attr1));
	}
	
	@DisplayName("attr6 <-> attr2")
	@Test
	public void attr6_attr2() throws AttributeException {
		assertStrictlyPositive(comp.compare(attr6,attr2));
	}
	
	@DisplayName("attr6 <-> attr3")
	@Test
	public void attr6_attr3() throws AttributeException {
		assertStrictlyPositive(comp.compare(attr6,attr3));
	}
	
	@DisplayName("attr6 <-> attr4")
	@Test
	public void attr6_attr4() throws AttributeException {
		assertStrictlyPositive(comp.compare(attr6,attr4));
	}
	
	@DisplayName("attr6 <-> attr5")
	@Test
	public void attr6_attr5() throws AttributeException {
		assertStrictlyPositive(comp.compare(attr6,attr5));
	}
	
	@DisplayName("attr6 <-> attr6")
	@Test
	public void attr6_attr6() throws AttributeException {
		assertEquals(0, comp.compare(attr6,attr6));
	}

}
