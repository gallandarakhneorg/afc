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

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.collection.AttributeChangeEvent;
import org.arakhne.afc.attrs.collection.AttributeChangeListener;
import org.arakhne.afc.attrs.collection.AttributeCollection;

/**
 * Test of AbstractAttributeProvider.
 * 
 * @param <T>
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractAttributeCollectionTest<T extends AttributeCollection> extends AbstractAttributeProviderTest<T> {

	private Attribute[] newValues;
	
	/**
	 */
	protected ListenerStub listenerStub;
	
	/**
	 * @param id
	 */
	public AbstractAttributeCollectionTest(String id) {
		super(id);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		this.newValues = new Attribute[] {
			new AttributeImpl("A",false),	 //$NON-NLS-1$
			new AttributeImpl("D","34"),	 //$NON-NLS-1$ //$NON-NLS-2$
			new AttributeImpl("Z",17f),	 //$NON-NLS-1$
		};
		
		this.listenerStub = new ListenerStub();
		this.testData.addAttributeChangeListener(this.listenerStub);
	}
	
	@Override
	public void tearDown() throws Exception {
		this.newValues = null;
		this.listenerStub.reset();
		this.listenerStub = null;
		super.tearDown();
	}
	
	private void runSetAttributeValue(Class<?>[] types, Object[] parameters, Attribute attr) throws Exception {
		String name = attr.getName();
		
		boolean attrExists = this.testData.hasAttribute(name);
		AttributeValue oldValue = null;
		if (attrExists) {
			oldValue = this.testData.getAttribute(name);
		}
	
		Method method = this.testData.getClass().getMethod("setAttribute", types); //$NON-NLS-1$
		Object o = method.invoke(this.testData, parameters);

		assertTrue(this.id, o instanceof Attribute);
		assertEquals(this.id, attr,o);
		
		assertNotNull(this.id, this.testData.getAttribute(name));
		assertEquals(this.id, attr.getType(),this.testData.getAttribute(name).getType());
		assertEquals(this.id, attr,this.testData.getAttribute(name));
		
		// Test events
		String message = this.id+": set attribute "+name; //$NON-NLS-1$
		this.listenerStub.assertNames(message, name);
		this.listenerStub.assertValues(message, attr);
		if (!attrExists) {
			this.listenerStub.assertTypes(message,
					AttributeChangeEvent.Type.ADDITION);
			this.listenerStub.assertOldNames(message, new String[]{null});
			this.listenerStub.assertOldValues(message, new AttributeValue[]{null});
		}
		else {
			this.listenerStub.assertTypes(message,
					AttributeChangeEvent.Type.VALUE_UPDATE);
			this.listenerStub.assertOldNames(message, name);
			this.listenerStub.assertOldValues(message, oldValue);
		}
		
		this.listenerStub.reset();
	}
	
	private void runSetAttributeValue(Class<?> type, Object parameter, Attribute attr) throws Exception {
		runSetAttributeValue(
				new Class<?>[] {String.class, type},
				new Object[] {attr.getName(), parameter}, attr);
	}

	/**
	 * @throws Exception
	 */
	public void testSetAttributeAttribute() throws Exception {
		for (Attribute attr : this.newValues) {
			runSetAttributeValue(
					new Class<?>[] {Attribute.class},
					new Object[] {attr},
					attr);
		}
	}

	/**
	 * @throws Exception
	 */
	public void testSetAttributeStringAttributeValue() throws Exception {
		for (Attribute attr : this.newValues) {
			runSetAttributeValue(
					AttributeValue.class,
					attr,
					attr);
		}
	}

	/**
	 * @throws Exception
	 */
	public void testSetAttributeStringBoolean() throws Exception {
		Attribute attr = new AttributeImpl("A", false); //$NON-NLS-1$
		runSetAttributeValue(
				boolean.class,
				attr.getBoolean(),
				attr);
		attr = new AttributeImpl("X", false); //$NON-NLS-1$
		runSetAttributeValue(
				boolean.class,
				attr.getBoolean(),
				attr);
	}

	/**
	 * @throws Exception
	 */
	public void testSetAttributeStringInt() throws Exception {
		Attribute attr = new AttributeImpl("E", 34); //$NON-NLS-1$
		runSetAttributeValue(
				int.class,
				(int)attr.getInteger(),
				attr);
		attr = new AttributeImpl("X", 34); //$NON-NLS-1$
		runSetAttributeValue(
				int.class,
				(int)attr.getInteger(),
				attr);
	}

	/**
	 * @throws Exception
	 */
	public void testSetAttributeStringLong() throws Exception {
		Attribute attr = new AttributeImpl("E", 34); //$NON-NLS-1$
		runSetAttributeValue(
				long.class,
				attr.getInteger(),
				attr);
		attr = new AttributeImpl("X", 34); //$NON-NLS-1$
		runSetAttributeValue(
				long.class,
				attr.getInteger(),
				attr);
	}

	/**
	 * @throws Exception
	 */
	public void testSetAttributeStringFloat() throws Exception {
		Attribute attr = new AttributeImpl("E", 34f); //$NON-NLS-1$
		runSetAttributeValue(
				float.class,
				(float)attr.getReal(),
				attr);
		attr = new AttributeImpl("X", 34f); //$NON-NLS-1$
		runSetAttributeValue(
				float.class,
				(float)attr.getReal(),
				attr);
	}

	/**
	 * @throws Exception
	 */
	public void testSetAttributeStringDouble() throws Exception {
		Attribute attr = new AttributeImpl("E", 34.); //$NON-NLS-1$
		runSetAttributeValue(
				double.class,
				attr.getReal(),
				attr);
		attr = new AttributeImpl("X", 34.); //$NON-NLS-1$
		runSetAttributeValue(
				double.class,
				attr.getReal(),
				attr);
	}
	
	/**
	 * @throws Exception
	 */
	public void testSetAttributeStringString() throws Exception {
		Attribute attr = new AttributeImpl("E", "Toto"); //$NON-NLS-1$ //$NON-NLS-2$
		runSetAttributeValue(
				String.class,
				attr.getString(),
				attr);
		attr = new AttributeImpl("X", "Titi et Rominet"); //$NON-NLS-1$ //$NON-NLS-2$
		runSetAttributeValue(
				String.class,
				attr.getString(),
				attr);
	}

	/**
	 */
	public void testRemoveAttributeString() {
		String message;
		
		assertFalse(this.id, this.testData.removeAttribute("Y")); //$NON-NLS-1$
		// Testing events
		message = this.id+": removing Y"; //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();
		
		assertTrue(this.id, this.testData.removeAttribute("C")); //$NON-NLS-1$
		// Testing events
		message = "removing C"; //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.REMOVAL);
		this.listenerStub.assertNames(message, "C"); //$NON-NLS-1$
		this.listenerStub.assertOldNames(message, "C"); //$NON-NLS-1$
		this.listenerStub.assertValues(message, new AttributeValueImpl(true));
		this.listenerStub.assertOldValues(message, new AttributeValueImpl(true));
		this.listenerStub.reset();

		assertFalse(this.id, this.testData.removeAttribute("X")); //$NON-NLS-1$
		// Testing events
		message = this.id+": removing X"; //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();
		
		assertTrue(this.id, this.testData.hasAttribute("A")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("B")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("C")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F")); //$NON-NLS-1$
		
	}

	/**
	 */
	public void testRemoveAllAttributes() {
		String message;
		
		assertTrue(this.id, this.testData.hasAttribute("A")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("B")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F")); //$NON-NLS-1$

		assertTrue(this.id, this.testData.removeAllAttributes());
		message = this.id+": removing all attributes"; //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.REMOVE_ALL);
		this.listenerStub.assertNames(message, new String[]{null});
		this.listenerStub.assertOldNames(message, new String[]{null});
		this.listenerStub.assertValues(message, new AttributeValue[]{null});
		this.listenerStub.assertOldValues(message, new AttributeValue[]{null});
		this.listenerStub.reset();

		assertFalse(this.id, this.testData.hasAttribute("A")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("C")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("D")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("E")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("F")); //$NON-NLS-1$

		assertFalse(this.id, this.testData.removeAllAttributes());
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();

		assertFalse(this.id, this.testData.hasAttribute("A")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("C")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("D")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("E")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("F")); //$NON-NLS-1$
	}
	
	/**
	 */
	public void testRenameAttribute() {
		String message;
		
		assertTrue(this.id, this.testData.hasAttribute("A")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("B")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F")); //$NON-NLS-1$
		
		AttributeValue oldValue = this.testData.getAttribute("B"); //$NON-NLS-1$

		assertTrue(this.id, this.testData.renameAttribute("B", "ZZZ", false)); //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming B to ZZZ"; //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.RENAME);
		this.listenerStub.assertNames(message, "ZZZ"); //$NON-NLS-1$
		this.listenerStub.assertOldNames(message, "B"); //$NON-NLS-1$
		this.listenerStub.assertValues(message, oldValue);
		this.listenerStub.assertOldValues(message, oldValue);
		this.listenerStub.reset();

		assertTrue(this.id, this.testData.hasAttribute("A")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("ZZZ")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F")); //$NON-NLS-1$
		
		assertEquals(this.id, oldValue, this.testData.getAttribute("ZZZ")); //$NON-NLS-1$

		assertFalse(this.id, this.testData.renameAttribute("toto", "XXX", false)); //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming toto to XXX"; //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();

		assertTrue(this.id, this.testData.hasAttribute("A")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("ZZZ")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F")); //$NON-NLS-1$
		
		oldValue = this.testData.getAttribute("F"); //$NON-NLS-1$
		AttributeValue oldValue2 = this.testData.getAttribute("A"); //$NON-NLS-1$

		assertFalse(this.id, this.testData.renameAttribute("F", "A", false)); //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming F to A"; //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();

		assertTrue(this.id, this.testData.hasAttribute("A")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("ZZZ")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F")); //$NON-NLS-1$
		
		assertEquals(this.id, oldValue, this.testData.getAttribute("F")); //$NON-NLS-1$
		assertEquals(this.id, oldValue2, this.testData.getAttribute("A")); //$NON-NLS-1$
	}
	
	/**
	 */
	public void testRenameAttributeOverwrite() {
		String message;
		
		assertTrue(this.id, this.testData.hasAttribute("A")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("B")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F")); //$NON-NLS-1$
		
		AttributeValue oldValue = this.testData.getAttribute("B"); //$NON-NLS-1$

		assertTrue(this.id, this.testData.renameAttribute("B", "ZZZ", true)); //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming B to ZZZ"; //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.RENAME);
		this.listenerStub.assertNames(message, "ZZZ"); //$NON-NLS-1$
		this.listenerStub.assertOldNames(message, "B"); //$NON-NLS-1$
		this.listenerStub.assertValues(message, oldValue);
		this.listenerStub.assertOldValues(message, oldValue);
		this.listenerStub.reset();

		assertTrue(this.id, this.testData.hasAttribute("A")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("ZZZ")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F")); //$NON-NLS-1$
		
		assertEquals(this.id, oldValue, this.testData.getAttribute("ZZZ")); //$NON-NLS-1$

		assertFalse(this.id, this.testData.renameAttribute("toto", "XXX", true)); //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming toto to XXX"; //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();

		assertTrue(this.id, this.testData.hasAttribute("A")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("ZZZ")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F")); //$NON-NLS-1$
		
		oldValue = this.testData.getAttribute("F"); //$NON-NLS-1$
		AttributeValue oldValue2 = this.testData.getAttribute("A"); //$NON-NLS-1$

		assertTrue(this.id, this.testData.renameAttribute("F", "A", true)); //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming F to A"; //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.REMOVAL, AttributeChangeEvent.Type.RENAME);
		this.listenerStub.assertNames(message, "A","A"); //$NON-NLS-1$ //$NON-NLS-2$
		this.listenerStub.assertOldNames(message, "A", "F"); //$NON-NLS-1$ //$NON-NLS-2$
		this.listenerStub.assertValues(message, oldValue2, oldValue);
		this.listenerStub.assertOldValues(message, oldValue2, oldValue);
		this.listenerStub.reset();

		assertTrue(this.id, this.testData.hasAttribute("A")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("ZZZ")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z")); //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E")); //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("F")); //$NON-NLS-1$
		
		assertEquals(this.id, oldValue, this.testData.getAttribute("A")); //$NON-NLS-1$
	}
	
	/**
	 * Stub for AttributeChangeListener.
	 * 
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected class ListenerStub implements AttributeChangeListener {

		private final ArrayList<AttributeChangeEvent> eventList = new ArrayList<AttributeChangeEvent>();

		/**
		 */
		public void reset() {
			this.eventList.clear();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onAttributeChangeEvent(AttributeChangeEvent event) {
			this.eventList.add(event);
		}
		
		/**
		 * @param message
		 */
		public void assertEmpty(String message) {
			assertEquals(message, 0,this.eventList.size());
		}

		/**
		 * @param message
		 * @param desiredTypes
		 */
		public void assertTypes(String message, AttributeChangeEvent.Type... desiredTypes) {
			assertEquals(message, desiredTypes.length, this.eventList.size());
			for(int i=0; i<desiredTypes.length; ++i) {
				assertEquals(message+" at index "+i, desiredTypes[i], this.eventList.get(i).getType()); //$NON-NLS-1$
			}
		}

		/**
		 * @param message
		 * @param desiredNames
		 */
		public void assertNames(String message, String... desiredNames) {
			assertEquals(message, desiredNames.length, this.eventList.size());
			for(int i=0; i<desiredNames.length; ++i) {
				assertEquals(message+" at index "+i, desiredNames[i], this.eventList.get(i).getName()); //$NON-NLS-1$
			}
		}

		/**
		 * @param message
		 * @param desiredNames
		 */
		public void assertOldNames(String message, String... desiredNames) {
			assertEquals(message, desiredNames.length, this.eventList.size());
			for(int i=0; i<desiredNames.length; ++i) {
				assertEquals(message+" at index "+i, desiredNames[i], this.eventList.get(i).getOldName()); //$NON-NLS-1$
			}
		}

		/**
		 * @param message
		 * @param desiredValues
		 */
		public void assertValues(String message, AttributeValue... desiredValues) {
			assertEquals(message, desiredValues.length, this.eventList.size());
			for(int i=0; i<desiredValues.length; ++i) {
				assertEquals(message+" at index "+i, desiredValues[i], this.eventList.get(i).getValue()); //$NON-NLS-1$
			}
		}

		/**
		 * @param message
		 * @param desiredValues
		 */
		public void assertOldValues(String message, AttributeValue... desiredValues) {
			assertEquals(message, desiredValues.length, this.eventList.size());
			for(int i=0; i<desiredValues.length; ++i) {
				assertEquals(message+" at index "+i, desiredValues[i], this.eventList.get(i).getOldValue()); //$NON-NLS-1$
			}
		}

		/**
		 * @param message
		 * @param desiredAttributes
		 */
		public void assertAttributes(String message, Attribute... desiredAttributes) {
			assertEquals(message, desiredAttributes.length, this.eventList.size());
			for(int i=0; i<desiredAttributes.length; ++i) {
				assertEquals(message+" at index "+i, desiredAttributes[i], this.eventList.get(i).getAttribute()); //$NON-NLS-1$
			}
		}

	}

}
