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
package org.arakhne.afc.attrs.collection;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.arakhne.afc.attrs.AbstractAttrTestCase;
import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.collection.AbstractAttributeProvider;
import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.attrs.collection.MultiAttributeCollection;

/**
 * Test of MultiAttributeProvider.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class MultiAttributeCollectionTest extends AbstractAttrTestCase {

	private MultiAttributeCollection provider;
	private HeapAttributeCollection subprovider1;
	private HeapAttributeCollection subprovider2;
	private HeapAttributeCollection subprovider3;
	private AttributeContainerStub subcontainer4;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.provider = new MultiAttributeCollection();
		this.subprovider1 = new HeapAttributeCollection();
		this.subprovider2 = new HeapAttributeCollection();
		this.subprovider3 = new HeapAttributeCollection();
		this.subcontainer4 = new AttributeContainerStub();
		
		this.subprovider1.setAttribute("A", true); //$NON-NLS-1$
		this.subprovider1.setAttribute("B", 1); //$NON-NLS-1$
		this.subprovider1.setAttribute("C", new URL("http://www.multiagent.fr")); //$NON-NLS-1$ //$NON-NLS-2$
		this.subprovider1.setAttribute("E", new URL("http://www.multiagent.fr")); //$NON-NLS-1$ //$NON-NLS-2$
		this.subprovider1.setAttribute("Z1", "Z1"); //$NON-NLS-1$ //$NON-NLS-2$

		this.subprovider2.setAttribute("A", true); //$NON-NLS-1$
		this.subprovider2.setAttribute("B", 1.); //$NON-NLS-1$
		this.subprovider2.setAttribute("D", "abc"); //$NON-NLS-1$ //$NON-NLS-2$
		this.subprovider2.setAttribute("E", 1); //$NON-NLS-1$
		this.subprovider2.setAttribute("Z2", "Z2"); //$NON-NLS-1$ //$NON-NLS-2$

		this.subprovider3.setAttribute("A", false); //$NON-NLS-1$
		this.subprovider3.setAttribute("B", 1); //$NON-NLS-1$
		this.subprovider3.setAttribute("C", new URL("http://www.multiagent.fr")); //$NON-NLS-1$ //$NON-NLS-2$
		this.subprovider3.setAttribute("D", "abc"); //$NON-NLS-1$ //$NON-NLS-2$
		this.subprovider3.setAttribute("E", true); //$NON-NLS-1$
		this.subprovider3.setAttribute("Z3", "Z3"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.subcontainer4.provider.setAttribute("A", true); //$NON-NLS-1$
		this.subcontainer4.provider.setAttribute("Z4", "Z4"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.provider.addAttributeContainer(this.subprovider1);
		this.provider.addAttributeContainer(this.subprovider2);
		this.provider.addAttributeContainer(this.subprovider3);
		this.provider.addAttributeContainer(this.subcontainer4);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void tearDown() throws Exception {
		this.provider = null;
		this.subprovider1 = this.subprovider2 = this.subprovider3 = null;
		this.subcontainer4 = null;
		super.tearDown();
	}
	
	private static Attribute makeUninitialized(String name, AttributeType type) {
		return new AttributeImpl(name, type);
	}

	/**
	 */
	public void testGetAttributeContainerCount() {
		assertEquals(4, this.provider.getAttributeContainerCount());
	}	

	/**
	 */
	public void testRemoveAllAttributes() {
		assertTrue(this.provider.removeAllAttributes());
		assertEquals(0, this.subprovider1.getAttributeCount());
		assertEquals(0, this.subprovider2.getAttributeCount());
		assertEquals(0, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(2, this.provider.getAttributeCount());

		assertEpsilonEquals(Collections.emptyList(), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Collections.emptyList(), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Collections.emptyList(), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());

		assertFalse(this.provider.removeAllAttributes());

		assertEpsilonEquals(Collections.emptyList(), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Collections.emptyList(), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Collections.emptyList(), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());
	}

	/**
	 * @throws Exception
	 */
	public void testRemoveAttributeString() throws Exception {
		//
		// Remove A
		//
		assertTrue(this.provider.removeAttribute("A")); //$NON-NLS-1$
		assertEquals(4, this.subprovider1.getAttributeCount());
		assertEquals(4, this.subprovider2.getAttributeCount());
		assertEquals(5, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(9, this.provider.getAttributeCount());		
		
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1.), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true), //$NON-NLS-1$
				new AttributeImpl("Z3", "Z3") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.INTEGER), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());

		//
		// Failure on remove of ZZZ
		//
		assertFalse(this.provider.removeAttribute("ZZZ")); //$NON-NLS-1$

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1.), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true), //$NON-NLS-1$
				new AttributeImpl("Z3", "Z3") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.INTEGER), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());

		//
		// Remove Z3
		//
		assertTrue(this.provider.removeAttribute("Z3")); //$NON-NLS-1$
		assertEquals(4, this.subprovider1.getAttributeCount());
		assertEquals(4, this.subprovider2.getAttributeCount());
		assertEquals(4, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(8, this.provider.getAttributeCount());

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("B", 1.), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true) //$NON-NLS-1$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.INTEGER), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());
	}

	/**
	 * @throws Exception
	 */
	public void testRenameAttributeStringString() throws Exception {
		//
		// Rename A to ZZZ
		//
		assertTrue(this.provider.renameAttribute("A", "ZZZ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", false), //$NON-NLS-1$
				new AttributeImpl("B", 1.), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true), //$NON-NLS-1$
				new AttributeImpl("Z3", "Z3") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.INTEGER), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());

		//
		// Rename TOTOZZZ to A
		//
		assertFalse(this.provider.renameAttribute("TOTOZZZ", "A")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", false), //$NON-NLS-1$
				new AttributeImpl("B", 1.), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true), //$NON-NLS-1$
				new AttributeImpl("Z3", "Z3") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.INTEGER), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());

		//
		// Rename Z4 to C
		//
		assertFalse(this.provider.renameAttribute("TOTOZZZ", "A")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", false), //$NON-NLS-1$
				new AttributeImpl("B", 1.), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true), //$NON-NLS-1$
				new AttributeImpl("Z3", "Z3") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.INTEGER), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());
	}

	/**
	 * @throws Exception
	 */
	public void testRenameAttributeStringStringBoolean() throws Exception {
		//
		// Rename A to ZZZ
		//
		assertTrue(this.provider.renameAttribute("A", "ZZZ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", false), //$NON-NLS-1$
				new AttributeImpl("B", 1.), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true), //$NON-NLS-1$
				new AttributeImpl("Z3", "Z3") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.INTEGER), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());

		//
		// Rename TOTOZZZ to A
		//
		assertFalse(this.provider.renameAttribute("TOTOZZZ", "A")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", false), //$NON-NLS-1$
				new AttributeImpl("B", 1.), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true), //$NON-NLS-1$
				new AttributeImpl("Z3", "Z3") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.INTEGER), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());

		//
		// Rename Z4 to C
		//
		assertFalse(this.provider.renameAttribute("TOTOZZZ", "A")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", false), //$NON-NLS-1$
				new AttributeImpl("B", 1.), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true), //$NON-NLS-1$
				new AttributeImpl("Z3", "Z3") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.INTEGER), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());
	}

	/**
	 * @throws Exception
	 */
	public void testSetAttributeStringAttributeValue() throws Exception {
		//
		// Set ZZZ
		//
		assertEquals(
				new AttributeImpl("ZZZ", "xyz"), //$NON-NLS-1$ //$NON-NLS-2$
				this.provider.setAttribute("ZZZ", new AttributeValueImpl("xyz"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(6, this.subprovider1.getAttributeCount());
		assertEquals(6, this.subprovider2.getAttributeCount());
		assertEquals(7, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("A", false), //$NON-NLS-1$
				new AttributeImpl("B", 1.), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true), //$NON-NLS-1$
				new AttributeImpl("Z3", "Z3") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.INTEGER), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());

		//
		// Set B
		//
		assertEquals(
				new AttributeImpl("B", "def"), //$NON-NLS-1$ //$NON-NLS-2$
				this.provider.setAttribute("B", new AttributeValueImpl("def"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(6, this.subprovider1.getAttributeCount());
		assertEquals(6, this.subprovider2.getAttributeCount());
		assertEquals(7, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("B", "def"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("B", "def"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("A", false), //$NON-NLS-1$
				new AttributeImpl("B", "def"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true), //$NON-NLS-1$
				new AttributeImpl("Z3", "Z3") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());
	}

	/**
	 * @throws Exception
	 */
	public void testSetAttributeAttribute() throws Exception {
		//
		// Set ZZZ
		//
		assertEquals(
				new AttributeImpl("ZZZ", "xyz"), //$NON-NLS-1$ //$NON-NLS-2$
				this.provider.setAttribute(new AttributeImpl("ZZZ", "xyz"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(6, this.subprovider1.getAttributeCount());
		assertEquals(6, this.subprovider2.getAttributeCount());
		assertEquals(7, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("A", false), //$NON-NLS-1$
				new AttributeImpl("B", 1.), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true), //$NON-NLS-1$
				new AttributeImpl("Z3", "Z3") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.INTEGER), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());

		//
		// Set B
		//
		assertEquals(
				new AttributeImpl("B", "def"), //$NON-NLS-1$ //$NON-NLS-2$
				this.provider.setAttribute(new AttributeImpl("B", "def"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(6, this.subprovider1.getAttributeCount());
		assertEquals(6, this.subprovider2.getAttributeCount());
		assertEquals(7, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(10, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("B", "def"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("B", "def"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("ZZZ", "xyz"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("A", false), //$NON-NLS-1$
				new AttributeImpl("B", "def"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true), //$NON-NLS-1$
				new AttributeImpl("Z3", "Z3") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("ZZZ", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("A", AttributeType.BOOLEAN), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());
	}

	/**
	 * @throws Exception
	 */
	public void testSetAttributeTypeStringAttributeType() throws Exception {
		//
		// Set type of A to STRING
		//
		assertEquals(
				makeUninitialized("A", AttributeType.STRING), //$NON-NLS-1$
				this.provider.setAttributeType("A", AttributeType.STRING)); //$NON-NLS-1$

		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(9, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", Boolean.TRUE.toString()), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", Boolean.TRUE.toString()), //$NON-NLS-1$
				new AttributeImpl("B", 1), //$NON-NLS-1$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", Boolean.FALSE.toString()), //$NON-NLS-1$
				new AttributeImpl("B", 1.), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true), //$NON-NLS-1$
				new AttributeImpl("Z3", "Z3") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.INTEGER), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());

		//
		// Set type of B to STRING
		//
		assertEquals(
				makeUninitialized("B", AttributeType.STRING), //$NON-NLS-1$
				this.provider.setAttributeType("B", AttributeType.STRING)); //$NON-NLS-1$

		assertEquals(5, this.subprovider1.getAttributeCount());
		assertEquals(5, this.subprovider2.getAttributeCount());
		assertEquals(6, this.subprovider3.getAttributeCount());
		assertEquals(2, this.subcontainer4.getAttributeCount());
		assertEquals(9, this.provider.getAttributeCount());		

		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", Boolean.TRUE.toString()), //$NON-NLS-1$
				new AttributeImpl("B", Long.toString(1)), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("Z1", "Z1") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider1.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", Boolean.TRUE.toString()), //$NON-NLS-1$
				new AttributeImpl("B", Double.toString(1.)), //$NON-NLS-1$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", 1), //$NON-NLS-1$
				new AttributeImpl("Z2", "Z2") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider2.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", Boolean.FALSE.toString()), //$NON-NLS-1$
				new AttributeImpl("B", Long.toString(1)), //$NON-NLS-1$
				new AttributeImpl("C", new URL("http://www.multiagent.fr")), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("D", "abc"), //$NON-NLS-1$ //$NON-NLS-2$
				new AttributeImpl("E", true), //$NON-NLS-1$
				new AttributeImpl("Z3", "Z3") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subprovider3.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				new AttributeImpl("A", true), //$NON-NLS-1$
				new AttributeImpl("Z4", "Z4") //$NON-NLS-1$ //$NON-NLS-2$
		), this.subcontainer4.getAllAttributes());
		assertEpsilonEquals(Arrays.asList(
				makeUninitialized("A", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("B", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("C", AttributeType.URL), //$NON-NLS-1$
				makeUninitialized("D", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("E", AttributeType.OBJECT), //$NON-NLS-1$
				makeUninitialized("Z1", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z2", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z3", AttributeType.STRING), //$NON-NLS-1$
				makeUninitialized("Z4", AttributeType.STRING) //$NON-NLS-1$
		), this.provider.getAllAttributes());
	}

	/**
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	private static class AttributeContainerStub extends AbstractAttributeProvider {

		/** Attribute provider wrapped by this stub.
		 */
		public final HeapAttributeCollection provider = new HeapAttributeCollection();
		
		/**
		 */
		public AttributeContainerStub() {
			//
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void freeMemory() {
			this.provider.freeMemory();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<String> getAllAttributeNames() {
			return this.provider.getAllAttributeNames();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<Attribute> getAllAttributes() {
			return this.provider.getAllAttributes();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Map<AttributeType, Collection<Attribute>> getAllAttributesByType() {
			return this.provider.getAllAttributesByType();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public AttributeValue getAttribute(String name) {
			return this.provider.getAttribute(name);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public AttributeValue getAttribute(String name, AttributeValue defaultValue) {
			return this.provider.getAttribute(name, defaultValue);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getAttributeCount() {
			return this.provider.getAttributeCount();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Attribute getAttributeObject(String name) {
			return this.provider.getAttributeObject(name);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasAttribute(String name) {
			return this.provider.hasAttribute(name);
		}

		@Override
		public void toMap(Map<String, Object> mapToFill) {
			this.provider.toMap(mapToFill);
		}
		
	}
	
}
