/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.attrs.attr;

import org.arakhne.afc.attrs.AbstractAttrTestCase;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeValueComparator;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;

/**
 * Test of AttributeValueComparator.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AttributeValueComparatorTest extends AbstractAttrTestCase {

	/**
	 * @throws AttributeException
	 */
	public static void testCompare() throws AttributeException {
		double base_d = Math.random();
		long base_l = (long)base_d;
		AttributeValueImpl attr1 = new AttributeValueImpl(base_d);		
		AttributeValueImpl attr2 = new AttributeValueImpl(base_d+1);
		AttributeValueImpl attr3 = new AttributeValueImpl(base_d-1);
		
		AttributeValueImpl attr4 = new AttributeValueImpl(base_l);		
		AttributeValueImpl attr5 = new AttributeValueImpl(attr1.getInteger());
		
		AttributeValueImpl attr6 = new AttributeValueImpl("bonjour"); //$NON-NLS-1$
		
		AttributeValueComparator comp = new AttributeValueComparator();
			
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

}
