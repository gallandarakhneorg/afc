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

package org.arakhne.afc.vmutil.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class JsonBufferTest {

	private JsonBuffer buffer;
	
	@BeforeEach
	public void setUp() {
		this.buffer = new JsonBuffer();
	}

	@AfterEach
	public void tearDown() {
		this.buffer = null;
	}

	@Test
	public void add_String() {
		this.buffer.add("xyz", "myvalue"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("{\n\t\"xyz\": \"myvalue\"\n}", this.buffer.toString()); //$NON-NLS-1$
	}

	@Test
	public void add_Integer() {
		this.buffer.add("xyz", 234); //$NON-NLS-1$
		assertEquals("{\n\t\"xyz\": 234\n}", this.buffer.toString()); //$NON-NLS-1$
	}

	@Test
	public void add_Double() {
		this.buffer.add("xyz", 234.56); //$NON-NLS-1$
		assertEquals("{\n\t\"xyz\": "+ Double.toString(234.56) + "\n}", this.buffer.toString()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void add_Boolean() {
		this.buffer.add("xyz", true); //$NON-NLS-1$
		assertEquals("{\n\t\"xyz\": true\n}", this.buffer.toString()); //$NON-NLS-1$
	}

	@Test
	public void add_Object() {
		Object obj = new Object();
		this.buffer.add("xyz", obj); //$NON-NLS-1$
		assertEquals("{\n\t\"xyz\": \"" + obj.toString() + "\"\n}", this.buffer.toString()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void add_Iterable() {
		Object obj = new Object();
		List<Object> col = Arrays.asList("myvalue", 123, 456.78, obj, true); //$NON-NLS-1$
		this.buffer.add("xyz", col); //$NON-NLS-1$
		assertEquals("{\n\t\"xyz\": [\n\t\t" //$NON-NLS-1$
				+ "\"myvalue\",\n\t\t123,\n\t\t" //$NON-NLS-1$
				+ Double.toString(456.78)
				+ ",\n\t\t\"" + obj.toString() //$NON-NLS-1$
				+ "\",\n\t\ttrue\n\t]\n}", this.buffer.toString()); //$NON-NLS-1$
	}

	@Test
	public void add_Map() {
		Object obj = new Object();
		Map<Object, Object> map = new TreeMap<>(); // Use a TreeSet for sorting the keys
		map.put("k1", "myvalue"); //$NON-NLS-1$ //$NON-NLS-2$
		map.put("k2",  123); //$NON-NLS-1$
		map.put("k3", 456.78); //$NON-NLS-1$
		map.put("k4", obj); //$NON-NLS-1$
		map.put("k5", true); //$NON-NLS-1$
		this.buffer.add("xyz", map); //$NON-NLS-1$
		assertEquals("{\n\t\"xyz\": {\n\t\t" //$NON-NLS-1$
				+ "\"k1\": \"myvalue\",\n\t\t\"k2\": 123,\n\t\t\"k3\": " //$NON-NLS-1$
				+ Double.toString(456.78)
				+ ",\n\t\t\"k4\": \"" + obj.toString() //$NON-NLS-1$
				+ "\",\n\t\t\"k5\": true\n\t}\n}", this.buffer.toString()); //$NON-NLS-1$
	}
	
	@Test
	public void add_JsonBuffer() {
		JsonableObject obj = (buffer) -> {
			buffer.add("abc", 345.6); //$NON-NLS-1$
			buffer.add("def", "myvalue"); //$NON-NLS-1$ //$NON-NLS-2$
		};
		this.buffer.add("xyz", obj); //$NON-NLS-1$
		assertEquals("{\n\t\"xyz\": {\n\t\t\"abc\": 345.6,\n\t\t\"def\": \"myvalue\"\n\t}\n}", this.buffer.toString()); //$NON-NLS-1$
	}

	@Test
	public void add_JsonableObject_01() {
		Object obj = new Object();
		List<Object> col = Arrays.asList("myvalue", 123, 456.78, obj, true); //$NON-NLS-1$
		JsonBuffer subbuffer = new JsonBuffer();
		subbuffer.add("abc", col); //$NON-NLS-1$
		this.buffer.add("xyz", subbuffer); //$NON-NLS-1$
		assertEquals("{\n\t\"xyz\": {\n\t\t\"abc\": [\n\t\t\t" //$NON-NLS-1$
				+ "\"myvalue\",\n\t\t\t123,\n\t\t\t" //$NON-NLS-1$
				+ Double.toString(456.78)
				+ ",\n\t\t\t\"" + obj.toString() //$NON-NLS-1$
				+ "\",\n\t\t\ttrue\n\t\t]\n\t}\n}", this.buffer.toString()); //$NON-NLS-1$
	}

	@Test
	public void add_JsonBuffer_02() {
		Object obj = new Object();
		Map<Object, Object> map = new TreeMap<>(); // Use a TreeSet for sorting the keys
		map.put("k1", "myvalue"); //$NON-NLS-1$ //$NON-NLS-2$
		map.put("k2",  123); //$NON-NLS-1$
		map.put("k3", 456.78); //$NON-NLS-1$
		map.put("k4", obj); //$NON-NLS-1$
		map.put("k5", true); //$NON-NLS-1$
		JsonBuffer subbuffer = new JsonBuffer();
		subbuffer.add("abc", map); //$NON-NLS-1$
		this.buffer.add("xyz", subbuffer); //$NON-NLS-1$
		assertEquals("{\n\t\"xyz\": {\n\t\t\"abc\": {\n\t\t\t" //$NON-NLS-1$
				+ "\"k1\": \"myvalue\",\n\t\t\t\"k2\": 123,\n\t\t\t\"k3\": " //$NON-NLS-1$
				+ Double.toString(456.78)
				+ ",\n\t\t\t\"k4\": \"" + obj.toString() //$NON-NLS-1$
				+ "\",\n\t\t\t\"k5\": true\n\t\t}\n\t}\n}", this.buffer.toString()); //$NON-NLS-1$
	}

	@Test
	public void toString_Static_01() {
		String actual = JsonBuffer.toString("k1", "myvalue"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("{\n\t" //$NON-NLS-1$
				+ "\"k1\": \"myvalue\"\n}", actual); //$NON-NLS-1$
	}

	@Test
	public void toString_Static_02() {
		Object obj = new Object();
		String actual = JsonBuffer.toString(
				"k1", "myvalue", //$NON-NLS-1$ //$NON-NLS-2$
				"k2",  123, //$NON-NLS-1$
				"k3", 456.78, //$NON-NLS-1$
				"k4", obj, //$NON-NLS-1$
				"k5", true); //$NON-NLS-1$
		assertEquals("{\n\t" //$NON-NLS-1$
				+ "\"k1\": \"myvalue\",\n\t\"k2\": 123,\n\t\"k3\": " //$NON-NLS-1$
				+ Double.toString(456.78)
				+ ",\n\t\"k4\": \"" + obj.toString() //$NON-NLS-1$
				+ "\",\n\t\"k5\": true\n}", actual); //$NON-NLS-1$
	}

}
