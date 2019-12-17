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

package org.arakhne.afc.attrs.collection;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;

@SuppressWarnings("all")
public abstract class AbstractAttributeCollectionTest<T extends AttributeCollection> extends AbstractAttributeProviderTest<T> {

	private Attribute[] newValues;
	
	protected ListenerStub listenerStub;
	
	public AbstractAttributeCollectionTest(String id) {
		super(id);
	}
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		this.newValues = new Attribute[] {
			new AttributeImpl("A",false),	  //$NON-NLS-1$
			new AttributeImpl("D","34"),	   //$NON-NLS-1$ //$NON-NLS-2$
			new AttributeImpl("Z",17f),	  //$NON-NLS-1$
		};
		
		this.listenerStub = new ListenerStub();
		this.testData.addAttributeChangeListener(this.listenerStub);
	}
	
	@Override
	@After
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
	
		Method method = this.testData.getClass().getMethod("setAttribute", types);  //$NON-NLS-1$
		Object o = method.invoke(this.testData, parameters);

		assertTrue(this.id, o instanceof Attribute);
		assertEquals(this.id, attr,o);
		
		assertNotNull(this.id, this.testData.getAttribute(name));
		assertEquals(this.id, attr.getType(),this.testData.getAttribute(name).getType());
		assertEquals(this.id, attr,this.testData.getAttribute(name));
		
		// Test events
		String message = this.id+": set attribute "+name;  //$NON-NLS-1$
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

	@Test
	public void setAttributeAttribute() throws Exception {
		for (Attribute attr : this.newValues) {
			runSetAttributeValue(
					new Class<?>[] {Attribute.class},
					new Object[] {attr},
					attr);
		}
	}

	@Test
	public void setAttributeStringAttributeValue() throws Exception {
		for (Attribute attr : this.newValues) {
			runSetAttributeValue(
					AttributeValue.class,
					attr,
					attr);
		}
	}

	@Test
	public void setAttributeStringBoolean() throws Exception {
		Attribute attr = new AttributeImpl("A", false);  //$NON-NLS-1$
		runSetAttributeValue(
				boolean.class,
				attr.getBoolean(),
				attr);
		attr = new AttributeImpl("X", false);  //$NON-NLS-1$
		runSetAttributeValue(
				boolean.class,
				attr.getBoolean(),
				attr);
	}

	@Test
	public void setAttributeStringInt() throws Exception {
		Attribute attr = new AttributeImpl("E", 34);  //$NON-NLS-1$
		runSetAttributeValue(
				int.class,
				(int)attr.getInteger(),
				attr);
		attr = new AttributeImpl("X", 34);  //$NON-NLS-1$
		runSetAttributeValue(
				int.class,
				(int)attr.getInteger(),
				attr);
	}

	@Test
	public void setAttributeStringLong() throws Exception {
		Attribute attr = new AttributeImpl("E", 34);  //$NON-NLS-1$
		runSetAttributeValue(
				long.class,
				attr.getInteger(),
				attr);
		attr = new AttributeImpl("X", 34);  //$NON-NLS-1$
		runSetAttributeValue(
				long.class,
				attr.getInteger(),
				attr);
	}

	@Test
	public void setAttributeStringFloat() throws Exception {
		Attribute attr = new AttributeImpl("E", 34f);  //$NON-NLS-1$
		runSetAttributeValue(
				float.class,
				(float)attr.getReal(),
				attr);
		attr = new AttributeImpl("X", 34f);  //$NON-NLS-1$
		runSetAttributeValue(
				float.class,
				(float)attr.getReal(),
				attr);
	}

	@Test
	public void setAttributeStringDouble() throws Exception {
		Attribute attr = new AttributeImpl("E", 34.);  //$NON-NLS-1$
		runSetAttributeValue(
				double.class,
				attr.getReal(),
				attr);
		attr = new AttributeImpl("X", 34.);  //$NON-NLS-1$
		runSetAttributeValue(
				double.class,
				attr.getReal(),
				attr);
	}
	
	@Test
	public void setAttributeStringString() throws Exception {
		Attribute attr = new AttributeImpl("E", "Toto");   //$NON-NLS-1$ //$NON-NLS-2$
		runSetAttributeValue(
				String.class,
				attr.getString(),
				attr);
		attr = new AttributeImpl("X", "Titi et Rominet");   //$NON-NLS-1$ //$NON-NLS-2$
		runSetAttributeValue(
				String.class,
				attr.getString(),
				attr);
	}

	@Test
	public void removeAttributeString() {
		String message;
		
		assertFalse(this.id, this.testData.removeAttribute("Y"));  //$NON-NLS-1$
		// Testing events
		message = this.id+": removing Y";  //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();
		
		assertTrue(this.id, this.testData.removeAttribute("C"));  //$NON-NLS-1$
		// Testing events
		message = "removing C";  //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.REMOVAL);
		this.listenerStub.assertNames(message, "C");  //$NON-NLS-1$
		this.listenerStub.assertOldNames(message, "C");  //$NON-NLS-1$
		this.listenerStub.assertValues(message, new AttributeValueImpl(true));
		this.listenerStub.assertOldValues(message, new AttributeValueImpl(true));
		this.listenerStub.reset();

		assertFalse(this.id, this.testData.removeAttribute("X"));  //$NON-NLS-1$
		// Testing events
		message = this.id+": removing X";  //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();
		
		assertTrue(this.id, this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
	}

	@Test
	public void removeAllAttributes() {
		String message;
		
		assertTrue(this.id, this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F"));  //$NON-NLS-1$

		assertTrue(this.id, this.testData.removeAllAttributes());
		message = this.id+": removing all attributes";  //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.REMOVE_ALL);
		this.listenerStub.assertNames(message, new String[]{null});
		this.listenerStub.assertOldNames(message, new String[]{null});
		this.listenerStub.assertValues(message, new AttributeValue[]{null});
		this.listenerStub.assertOldValues(message, new AttributeValue[]{null});
		this.listenerStub.reset();

		assertFalse(this.id, this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("F"));  //$NON-NLS-1$

		assertFalse(this.id, this.testData.removeAllAttributes());
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();

		assertFalse(this.id, this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("F"));  //$NON-NLS-1$
	}
	
	@Test
	public void renameAttribute() {
		String message;
		
		assertTrue(this.id, this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		AttributeValue oldValue = this.testData.getAttribute("B");  //$NON-NLS-1$

		assertTrue(this.id, this.testData.renameAttribute("B", "ZZZ", false));   //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming B to ZZZ";  //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.RENAME);
		this.listenerStub.assertNames(message, "ZZZ");  //$NON-NLS-1$
		this.listenerStub.assertOldNames(message, "B");  //$NON-NLS-1$
		this.listenerStub.assertValues(message, oldValue);
		this.listenerStub.assertOldValues(message, oldValue);
		this.listenerStub.reset();

		assertTrue(this.id, this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		assertEquals(this.id, oldValue, this.testData.getAttribute("ZZZ"));  //$NON-NLS-1$

		assertFalse(this.id, this.testData.renameAttribute("toto", "XXX", false));   //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming toto to XXX";  //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();

		assertTrue(this.id, this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		oldValue = this.testData.getAttribute("F");  //$NON-NLS-1$
		AttributeValue oldValue2 = this.testData.getAttribute("A");  //$NON-NLS-1$

		assertFalse(this.id, this.testData.renameAttribute("F", "A", false));   //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming F to A";  //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();

		assertTrue(this.id, this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		assertEquals(this.id, oldValue, this.testData.getAttribute("F"));  //$NON-NLS-1$
		assertEquals(this.id, oldValue2, this.testData.getAttribute("A"));  //$NON-NLS-1$
	}
	
	@Test
	public void renameAttributeOverwrite() {
		String message;
		
		assertTrue(this.id, this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		AttributeValue oldValue = this.testData.getAttribute("B");  //$NON-NLS-1$

		assertTrue(this.id, this.testData.renameAttribute("B", "ZZZ", true));   //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming B to ZZZ";  //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.RENAME);
		this.listenerStub.assertNames(message, "ZZZ");  //$NON-NLS-1$
		this.listenerStub.assertOldNames(message, "B");  //$NON-NLS-1$
		this.listenerStub.assertValues(message, oldValue);
		this.listenerStub.assertOldValues(message, oldValue);
		this.listenerStub.reset();

		assertTrue(this.id, this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		assertEquals(this.id, oldValue, this.testData.getAttribute("ZZZ"));  //$NON-NLS-1$

		assertFalse(this.id, this.testData.renameAttribute("toto", "XXX", true));   //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming toto to XXX";  //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();

		assertTrue(this.id, this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		oldValue = this.testData.getAttribute("F");  //$NON-NLS-1$
		AttributeValue oldValue2 = this.testData.getAttribute("A");  //$NON-NLS-1$

		assertTrue(this.id, this.testData.renameAttribute("F", "A", true));   //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming F to A";  //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.REMOVAL, AttributeChangeEvent.Type.RENAME);
		this.listenerStub.assertNames(message, "A","A");   //$NON-NLS-1$ //$NON-NLS-2$
		this.listenerStub.assertOldNames(message, "A", "F");   //$NON-NLS-1$ //$NON-NLS-2$
		this.listenerStub.assertValues(message, oldValue2, oldValue);
		this.listenerStub.assertOldValues(message, oldValue2, oldValue);
		this.listenerStub.reset();

		assertTrue(this.id, this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.id, this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertFalse(this.id, this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		assertEquals(this.id, oldValue, this.testData.getAttribute("A"));  //$NON-NLS-1$
	}
	
	protected class ListenerStub implements AttributeChangeListener {

		private final ArrayList<AttributeChangeEvent> eventList = new ArrayList<>();

		public void reset() {
			this.eventList.clear();
		}
		
		@Override
		public void onAttributeChangeEvent(AttributeChangeEvent event) {
			this.eventList.add(event);
		}
		
		public void assertEmpty(String message) {
			assertEquals(message, 0,this.eventList.size());
		}

		public void assertTypes(String message, AttributeChangeEvent.Type... desiredTypes) {
			assertEquals(message, desiredTypes.length, this.eventList.size());
			for(int i=0; i<desiredTypes.length; ++i) {
				assertEquals(message+" at index "+i, desiredTypes[i], this.eventList.get(i).getType());  //$NON-NLS-1$
			}
		}

		public void assertNames(String message, String... desiredNames) {
			assertEquals(message, desiredNames.length, this.eventList.size());
			for(int i=0; i<desiredNames.length; ++i) {
				assertEquals(message+" at index "+i, desiredNames[i], this.eventList.get(i).getName());  //$NON-NLS-1$
			}
		}

		public void assertOldNames(String message, String... desiredNames) {
			assertEquals(message, desiredNames.length, this.eventList.size());
			for(int i=0; i<desiredNames.length; ++i) {
				assertEquals(message+" at index "+i, desiredNames[i], this.eventList.get(i).getOldName());  //$NON-NLS-1$
			}
		}

		public void assertValues(String message, AttributeValue... desiredValues) {
			assertEquals(message, desiredValues.length, this.eventList.size());
			for(int i=0; i<desiredValues.length; ++i) {
				assertEquals(message+" at index "+i, desiredValues[i], this.eventList.get(i).getValue());  //$NON-NLS-1$
			}
		}

		public void assertOldValues(String message, AttributeValue... desiredValues) {
			assertEquals(message, desiredValues.length, this.eventList.size());
			for(int i=0; i<desiredValues.length; ++i) {
				assertEquals(message+" at index "+i, desiredValues[i], this.eventList.get(i).getOldValue());  //$NON-NLS-1$
			}
		}

		public void assertAttributes(String message, Attribute... desiredAttributes) {
			assertEquals(message, desiredAttributes.length, this.eventList.size());
			for(int i=0; i<desiredAttributes.length; ++i) {
				assertEquals(message+" at index "+i, desiredAttributes[i], this.eventList.get(i).getAttribute());  //$NON-NLS-1$
			}
		}

	}

}
