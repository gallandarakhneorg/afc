/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
	@BeforeEach
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
	@AfterEach
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

		assertTrue(o instanceof Attribute);
		assertEquals(attr,o);
		
		assertNotNull(this.testData.getAttribute(name));
		assertEquals(attr.getType(),this.testData.getAttribute(name).getType());
		assertEquals(attr,this.testData.getAttribute(name));
		
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
		
		assertFalse(this.testData.removeAttribute("Y"));  //$NON-NLS-1$
		// Testing events
		message = this.id+": removing Y";  //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();
		
		assertTrue(this.testData.removeAttribute("C"));  //$NON-NLS-1$
		// Testing events
		message = "removing C";  //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.REMOVAL);
		this.listenerStub.assertNames(message, "C");  //$NON-NLS-1$
		this.listenerStub.assertOldNames(message, "C");  //$NON-NLS-1$
		this.listenerStub.assertValues(message, new AttributeValueImpl(true));
		this.listenerStub.assertOldValues(message, new AttributeValueImpl(true));
		this.listenerStub.reset();

		assertFalse(this.testData.removeAttribute("X"));  //$NON-NLS-1$
		// Testing events
		message = this.id+": removing X";  //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();
		
		assertTrue(this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
	}

	@Test
	public void removeAllAttributes() {
		String message;
		
		assertTrue(this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("F"));  //$NON-NLS-1$

		assertTrue(this.testData.removeAllAttributes());
		message = this.id+": removing all attributes";  //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.REMOVE_ALL);
		this.listenerStub.assertNames(message, new String[]{null});
		this.listenerStub.assertOldNames(message, new String[]{null});
		this.listenerStub.assertValues(message, new AttributeValue[]{null});
		this.listenerStub.assertOldValues(message, new AttributeValue[]{null});
		this.listenerStub.reset();

		assertFalse(this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("F"));  //$NON-NLS-1$

		assertFalse(this.testData.removeAllAttributes());
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();

		assertFalse(this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("F"));  //$NON-NLS-1$
	}
	
	@Test
	public void renameAttribute() {
		String message;
		
		assertTrue(this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		AttributeValue oldValue = this.testData.getAttribute("B");  //$NON-NLS-1$

		assertTrue(this.testData.renameAttribute("B", "ZZZ", false));   //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming B to ZZZ";  //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.RENAME);
		this.listenerStub.assertNames(message, "ZZZ");  //$NON-NLS-1$
		this.listenerStub.assertOldNames(message, "B");  //$NON-NLS-1$
		this.listenerStub.assertValues(message, oldValue);
		this.listenerStub.assertOldValues(message, oldValue);
		this.listenerStub.reset();

		assertTrue(this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		assertEquals(oldValue, this.testData.getAttribute("ZZZ"));  //$NON-NLS-1$

		assertFalse(this.testData.renameAttribute("toto", "XXX", false));   //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming toto to XXX";  //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();

		assertTrue(this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		oldValue = this.testData.getAttribute("F");  //$NON-NLS-1$
		AttributeValue oldValue2 = this.testData.getAttribute("A");  //$NON-NLS-1$

		assertFalse(this.testData.renameAttribute("F", "A", false));   //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming F to A";  //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();

		assertTrue(this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		assertEquals(oldValue, this.testData.getAttribute("F"));  //$NON-NLS-1$
		assertEquals(oldValue2, this.testData.getAttribute("A"));  //$NON-NLS-1$
	}
	
	@Test
	public void renameAttributeOverwrite() {
		String message;
		
		assertTrue(this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		AttributeValue oldValue = this.testData.getAttribute("B");  //$NON-NLS-1$

		assertTrue(this.testData.renameAttribute("B", "ZZZ", true));   //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming B to ZZZ";  //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.RENAME);
		this.listenerStub.assertNames(message, "ZZZ");  //$NON-NLS-1$
		this.listenerStub.assertOldNames(message, "B");  //$NON-NLS-1$
		this.listenerStub.assertValues(message, oldValue);
		this.listenerStub.assertOldValues(message, oldValue);
		this.listenerStub.reset();

		assertTrue(this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		assertEquals(oldValue, this.testData.getAttribute("ZZZ"));  //$NON-NLS-1$

		assertFalse(this.testData.renameAttribute("toto", "XXX", true));   //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming toto to XXX";  //$NON-NLS-1$
		this.listenerStub.assertEmpty(message);
		this.listenerStub.reset();

		assertTrue(this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		oldValue = this.testData.getAttribute("F");  //$NON-NLS-1$
		AttributeValue oldValue2 = this.testData.getAttribute("A");  //$NON-NLS-1$

		assertTrue(this.testData.renameAttribute("F", "A", true));   //$NON-NLS-1$ //$NON-NLS-2$
		// Testing events
		message = this.id+": renaming F to A";  //$NON-NLS-1$
		this.listenerStub.assertTypes(message, AttributeChangeEvent.Type.REMOVAL, AttributeChangeEvent.Type.RENAME);
		this.listenerStub.assertNames(message, "A","A");   //$NON-NLS-1$ //$NON-NLS-2$
		this.listenerStub.assertOldNames(message, "A", "F");   //$NON-NLS-1$ //$NON-NLS-2$
		this.listenerStub.assertValues(message, oldValue2, oldValue);
		this.listenerStub.assertOldValues(message, oldValue2, oldValue);
		this.listenerStub.reset();

		assertTrue(this.testData.hasAttribute("A"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("X"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("B"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("ZZZ"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Y"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("C"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("D"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("Z"));  //$NON-NLS-1$
		assertTrue(this.testData.hasAttribute("E"));  //$NON-NLS-1$
		assertFalse(this.testData.hasAttribute("F"));  //$NON-NLS-1$
		
		assertEquals(oldValue, this.testData.getAttribute("A"));  //$NON-NLS-1$
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
			assertEquals(0,this.eventList.size(), message);
		}

		public void assertTypes(String message, AttributeChangeEvent.Type... desiredTypes) {
			assertEquals(desiredTypes.length, this.eventList.size(), message);
			for(int i=0; i<desiredTypes.length; ++i) {
				assertEquals(desiredTypes[i], this.eventList.get(i).getType(), message+" at index "+i);  //$NON-NLS-1$
			}
		}

		public void assertNames(String message, String... desiredNames) {
			assertEquals(desiredNames.length, this.eventList.size(), message);
			for(int i=0; i<desiredNames.length; ++i) {
				assertEquals(desiredNames[i], this.eventList.get(i).getName(), message+" at index "+i);  //$NON-NLS-1$
			}
		}

		public void assertOldNames(String message, String... desiredNames) {
			assertEquals(desiredNames.length, this.eventList.size(), message);
			for(int i=0; i<desiredNames.length; ++i) {
				assertEquals(desiredNames[i], this.eventList.get(i).getOldName(), message+" at index "+i);  //$NON-NLS-1$
			}
		}

		public void assertValues(String message, AttributeValue... desiredValues) {
			assertEquals(desiredValues.length, this.eventList.size(), message);
			for(int i=0; i<desiredValues.length; ++i) {
				assertEquals(desiredValues[i], this.eventList.get(i).getValue(), message+" at index "+i);  //$NON-NLS-1$
			}
		}

		public void assertOldValues(String message, AttributeValue... desiredValues) {
			assertEquals(desiredValues.length, this.eventList.size(), message);
			for(int i=0; i<desiredValues.length; ++i) {
				assertEquals(desiredValues[i], this.eventList.get(i).getOldValue(), message+" at index "+i);  //$NON-NLS-1$
			}
		}

		public void assertAttributes(String message, Attribute... desiredAttributes) {
			assertEquals(desiredAttributes.length, this.eventList.size(), message);
			for(int i=0; i<desiredAttributes.length; ++i) {
				assertEquals(desiredAttributes[i], this.eventList.get(i).getAttribute(), message+" at index "+i);  //$NON-NLS-1$
			}
		}

	}

}
