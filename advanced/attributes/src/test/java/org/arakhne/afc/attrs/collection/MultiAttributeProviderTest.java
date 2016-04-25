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
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.attrs.collection;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.arakhne.afc.attrs.AbstractAttrTestCase;
import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.attrs.collection.MultiAttributeProvider;

/**
 * Test of MultiAttributeContainer.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@SuppressWarnings("all")
public class MultiAttributeProviderTest extends AbstractAttrTestCase {

	private MultiAttributeProvider container;
	private HeapAttributeCollection subcontainer1;
	private HeapAttributeCollection subcontainer2;
	private HeapAttributeCollection subcontainer3;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.container = new MultiAttributeProvider();
		this.subcontainer1 = new HeapAttributeCollection();
		this.subcontainer2 = new HeapAttributeCollection();
		this.subcontainer3 = new HeapAttributeCollection();
		
		this.subcontainer1.setAttribute("A", true); //$NON-NLS-1$
		this.subcontainer1.setAttribute("B", 1); //$NON-NLS-1$
		this.subcontainer1.setAttribute("C", new URL("http://www.multiagent.fr")); //$NON-NLS-1$ //$NON-NLS-2$
		this.subcontainer1.setAttribute("E", new URL("http://www.multiagent.fr")); //$NON-NLS-1$ //$NON-NLS-2$
		this.subcontainer1.setAttribute("Z1", "Z1"); //$NON-NLS-1$ //$NON-NLS-2$

		this.subcontainer2.setAttribute("A", true); //$NON-NLS-1$
		this.subcontainer2.setAttribute("B", 1.); //$NON-NLS-1$
		this.subcontainer2.setAttribute("D", "abc"); //$NON-NLS-1$ //$NON-NLS-2$
		this.subcontainer2.setAttribute("E", 1); //$NON-NLS-1$
		this.subcontainer2.setAttribute("Z2", "Z2"); //$NON-NLS-1$ //$NON-NLS-2$

		this.subcontainer3.setAttribute("A", false); //$NON-NLS-1$
		this.subcontainer3.setAttribute("B", 1); //$NON-NLS-1$
		this.subcontainer3.setAttribute("C", new URL("http://www.multiagent.fr")); //$NON-NLS-1$ //$NON-NLS-2$
		this.subcontainer3.setAttribute("D", "abc"); //$NON-NLS-1$ //$NON-NLS-2$
		this.subcontainer3.setAttribute("E", true); //$NON-NLS-1$
		this.subcontainer3.setAttribute("Z3", "Z3"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.container.addAttributeContainer(this.subcontainer1);
		this.container.addAttributeContainer(this.subcontainer2);
		this.container.addAttributeContainer(this.subcontainer3);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void tearDown() throws Exception {
		this.container = null;
		this.subcontainer1 = this.subcontainer2 = this.subcontainer3 = null;
		super.tearDown();
	}
	
	/**
	 */
	public void testGetAttributeCount() {
		assertEquals(8, this.container.getAttributeCount());
	}
	
	/**
	 */
	public void testGetAttributeContainerCount() {
		assertEquals(3, this.container.getAttributeContainerCount());
	}	

	/**
	 */
	public void testGetAllAttributeNames() {
		Collection<String> names = this.container.getAllAttributeNames();
		assertNotNull(names);
		assertEquals(8, names.size());
		assertEpsilonEquals(Arrays.asList(
				"A", //$NON-NLS-1$
				"B", //$NON-NLS-1$
				"C", //$NON-NLS-1$
				"D", //$NON-NLS-1$
				"E", //$NON-NLS-1$
				"Z1", //$NON-NLS-1$
				"Z2", //$NON-NLS-1$
				"Z3" //$NON-NLS-1$
				), names);
	}
	
	private static void assertUninitialized(AttributeType type, AttributeValue v) {
		assertNotNull(v);
		assertEquals(type, v.getType());
		assertFalse(v.isAssigned());
	}

	private static Attribute makeUninitialized(String name, AttributeType type) {
		return new AttributeImpl(name, type);
	}

	/**
	 */
	public void testHasAttributeString() {
		assertTrue(this.container.hasAttribute("A")); //$NON-NLS-1$
		assertTrue(this.container.hasAttribute("B")); //$NON-NLS-1$
		assertTrue(this.container.hasAttribute("C")); //$NON-NLS-1$
		assertTrue(this.container.hasAttribute("D")); //$NON-NLS-1$
		assertTrue(this.container.hasAttribute("E")); //$NON-NLS-1$
		assertFalse(this.container.hasAttribute("F")); //$NON-NLS-1$
		assertTrue(this.container.hasAttribute("Z1")); //$NON-NLS-1$
		assertTrue(this.container.hasAttribute("Z2")); //$NON-NLS-1$
		assertTrue(this.container.hasAttribute("Z3")); //$NON-NLS-1$
		assertFalse(this.container.hasAttribute("Z4")); //$NON-NLS-1$
	}

	/**
	 */
	public void testGetAttributeString() {
		assertUninitialized(AttributeType.BOOLEAN,
				this.container.getAttribute("A")); //$NON-NLS-1$
		assertEquals(new AttributeValueImpl(1),
				this.container.getAttribute("B")); //$NON-NLS-1$
		assertUninitialized(AttributeType.URL,
				this.container.getAttribute("C")); //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("D")); //$NON-NLS-1$
		assertUninitialized(AttributeType.OBJECT,
				this.container.getAttribute("E")); //$NON-NLS-1$
		assertNull(this.container.getAttribute("F")); //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("Z1")); //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("Z2")); //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("Z3")); //$NON-NLS-1$
		assertNull(this.container.getAttribute("Z4")); //$NON-NLS-1$
	}

	/**
	 */
	public void testGetAttributeStringAttributeValue() {
		AttributeValue defaultValue = new AttributeValueImpl(456);
		assertUninitialized(AttributeType.BOOLEAN,
				this.container.getAttribute("A", defaultValue)); //$NON-NLS-1$
		assertEquals(new AttributeValueImpl(1),
				this.container.getAttribute("B", defaultValue)); //$NON-NLS-1$
		assertUninitialized(AttributeType.URL,
				this.container.getAttribute("C", defaultValue)); //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("D", defaultValue)); //$NON-NLS-1$
		assertUninitialized(AttributeType.OBJECT,
				this.container.getAttribute("E", defaultValue)); //$NON-NLS-1$
		assertSame(defaultValue, this.container.getAttribute("F", defaultValue)); //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("Z1", defaultValue)); //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("Z2", defaultValue)); //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttribute("Z3", defaultValue)); //$NON-NLS-1$
		assertSame(defaultValue, this.container.getAttribute("Z4", defaultValue)); //$NON-NLS-1$
	}

	/**
	 */
	public void testGetAttributeObjectString() {
		assertUninitialized(AttributeType.BOOLEAN,
				this.container.getAttributeObject("A")); //$NON-NLS-1$
		assertEquals(new AttributeImpl("B",1), //$NON-NLS-1$
				this.container.getAttributeObject("B")); //$NON-NLS-1$
		assertUninitialized(AttributeType.URL,
				this.container.getAttributeObject("C")); //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttributeObject("D")); //$NON-NLS-1$
		assertUninitialized(AttributeType.OBJECT,
				this.container.getAttributeObject("E")); //$NON-NLS-1$
		assertNull(this.container.getAttribute("F")); //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttributeObject("Z1")); //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttributeObject("Z2")); //$NON-NLS-1$
		assertUninitialized(AttributeType.STRING,
				this.container.getAttributeObject("Z3")); //$NON-NLS-1$
		assertNull(this.container.getAttributeObject("Z4")); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public void testGetAllAttributes() throws Exception {
		Collection<Attribute> attrs = this.container.getAllAttributes();
		assertNotNull(attrs);
		assertEquals(8, attrs.size());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING) //$NON-NLS-1$
		), attrs);
	}

	/**
	 */
	public void testGetAllAttributesByType() {
		Map<AttributeType,Collection<Attribute>> attrsbytype = this.container.getAllAttributesByType();
		assertNotNull(attrsbytype);
		assertEquals(5, attrsbytype.size());
		
		Collection<Attribute> attrs;
		
		attrs = attrsbytype.get(AttributeType.BOOLEAN);
		assertNotNull(attrs);
		assertEquals(1, attrs.size());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.BOOLEAN) //$NON-NLS-1$
		), attrs);
		
		attrs = attrsbytype.get(AttributeType.COLOR);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.DATE);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.IMAGE);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.INTEGER);
		assertNotNull(attrs);
		assertEquals(1, attrs.size());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1) //$NON-NLS-1$
		), attrs);

		attrs = attrsbytype.get(AttributeType.OBJECT);
		assertNotNull(attrs);
		assertEquals(1, attrs.size());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("E", AttributeType.OBJECT) //$NON-NLS-1$
		), attrs);

		attrs = attrsbytype.get(AttributeType.POINT);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.POINT3D);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.POLYLINE);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.POLYLINE3D);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.REAL);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.STRING);
		assertNotNull(attrs);
		assertEquals(4, attrs.size());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING) //$NON-NLS-1$
		), attrs);

		attrs = attrsbytype.get(AttributeType.TIMESTAMP);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.URI);
		assertNull(attrs);

		attrs = attrsbytype.get(AttributeType.URL);
		assertNotNull(attrs);
		assertEquals(1, attrs.size());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("C", AttributeType.URL) //$NON-NLS-1$
		), attrs);

		attrs = attrsbytype.get(AttributeType.UUID);
		assertNull(attrs);
	}

}
