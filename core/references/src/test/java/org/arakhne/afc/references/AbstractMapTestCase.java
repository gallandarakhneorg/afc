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

package org.arakhne.afc.references;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import org.arakhne.afc.testtools.AbstractTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @param <K>
 * @param <V>
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public abstract class AbstractMapTestCase<MAP extends Map<String, String>> extends AbstractTestCase {

	protected static final Random RANDOM = new Random();

	protected static final int refCount = 100;

	protected static final int unrefCount = 5;
	
	protected static Map<String, String> referenceMap;

	protected static Map<String, String> unreferenceMap;

	protected MAP map;
	
	protected Map<String, String> reference() {
		synchronized (AbstractCollectionTestCase.class) {
			if (referenceMap == null || referenceMap.size() != refCount) {
				referenceMap = new HashMap<>(refCount);
				for(int idx = 0; idx < refCount; ++idx) {
					referenceMap.put(createKeyInstance("in/"), createValueInstance("in/"));  //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			assertTrue(referenceMap != null && !referenceMap.isEmpty());
			return referenceMap;
		}
	}

	protected Map<String, String> unreference() {
		synchronized (AbstractCollectionTestCase.class) {
			if (unreferenceMap == null || referenceMap.size() != unrefCount) {
				unreferenceMap = new HashMap<>(unrefCount);
				for(int idx = 0; idx < unrefCount; ++idx) {
					unreferenceMap.put(createKeyInstance("out/"), createValueInstance("out/"));  //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			assertTrue(unreferenceMap != null && !unreferenceMap.isEmpty());
			return unreferenceMap;
		}
	}

	@BeforeEach
	public void setUp() throws Exception {
		map = createMap();
	}
	
	private static Stream<Arguments> provideRandomRefIndexes() {
		final var arguments = new ArrayList<Arguments>();
        int count = RANDOM.nextInt(50) + 50;
		for (int i = 0; i < count; ++i) {
        	int index = RANDOM.nextInt(refCount);
			arguments.add(Arguments.of(index));
		}
		return arguments.stream();
	}

	private static Stream<Arguments> provideRandomUnrefIndexes() {
		final var arguments = new ArrayList<Arguments>();
        var count = RANDOM.nextInt(5) + 5;
		for (int i = 0; i < count; ++i) {
        	int index = RANDOM.nextInt(unrefCount);
			arguments.add(Arguments.of(index));
		}
		return arguments.stream();
	}

	protected abstract String createKeyInstance(String prefix);
	
	protected abstract String createValueInstance(String prefix);

	protected abstract MAP createMap();
	
	protected void initMapWith(Map<String, String> toAdd, Map<String, String> reference) {
		map.clear();
		map.putAll(toAdd);
	}
	
	protected void fillMapWith(Map<String, String> toAdd) {
		map.putAll(toAdd);
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		map = null;
	}
	
	/** Replies the key at the given index.
	 */
	public static <KK> KK key(Map<KK,?> map, int index) {
		int i = 0; 
		for(KK key : map.keySet()) {
			if (i==index) return key;
			++i;
		}
		throw new IndexOutOfBoundsException();
	}
	
	/** Replies the value at the given index.
	 */
	public static <VV> VV value(Map<?,VV> map, int index) {
		int i = 0; 
		for(VV value : map.values()) {
			if (i==index) return value;
			++i;
		}
		throw new IndexOutOfBoundsException();
	}

	@DisplayName("size")
	@Nested
	public class Size {

		private Map<String, String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
		}
		
		@DisplayName("#1")
		@Test
	    public void test_1() {
	        assertEquals(0, map.size());
	    }

		@DisplayName("#2")
		@Test
	    public void test_2() {
	        initMapWith(theReference, theReference);        
	        assertEquals(theReference.size(), map.size());
	    }
	}

	@DisplayName("isEmpty")
	@Nested
	public class IsEmpty {

		private Map<String, String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
		}

		@DisplayName("#1")
		@Test
	    public void test_1() {
			assertTrue(map.isEmpty());
	    }

		@DisplayName("#2")
		@Test
	    public void test_2() {
	        initMapWith(theReference, theReference);        
	        assertFalse(map.isEmpty());
	    }
	}

	@DisplayName("entrySet")
	@Nested
	public class EntrySet {

		private Map<String, String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
		}

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	var entries = map.entrySet();
	    	assertTrue(entries.isEmpty());
		}

		@DisplayName("#2")
		@Test
	    public void test_2() {
	    	initMapWith(theReference, theReference);
	    	var entries = map.entrySet();
	    	assertFalse(entries.isEmpty());
	    	assertEpsilonEquals(theReference.entrySet(), entries);
	    }
	}

	@DisplayName("containsKey")
	@Nested
	public class ContainsKey {

		private Map<String, String> theReference;
		private Map<String, String> theUnreference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
			theUnreference = unreference();
	    	initMapWith(theReference, theReference);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractMapTestCase#provideRandomRefIndexes")
	    public void test_1(Integer index) {
        	assertTrue(map.containsKey(key(theReference,index)),"#"+index);  //$NON-NLS-1$
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractMapTestCase#provideRandomUnrefIndexes")
	    public void test_2(Integer index) {
	        var elt = key(theUnreference, index);
        	assertFalse(map.containsKey(elt),"#"+index);  //$NON-NLS-1$
		}
	}

	@DisplayName("containsValue")
	@Nested
	public class ContainsValue {

		private Map<String, String> theReference;
		private Map<String, String> theUnreference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
			theUnreference = unreference();
	    	initMapWith(theReference, theReference);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractMapTestCase#provideRandomRefIndexes")
	    public void test_1(Integer index) {
	        assertTrue(map.containsValue(value(theReference,index)),"#"+index);  //$NON-NLS-1$
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractMapTestCase#provideRandomUnrefIndexes")
	    public void test_2(Integer index) {
        	var elt = value(theUnreference, index);
        	assertFalse(map.containsValue(elt),"#"+index);  //$NON-NLS-1$
		}
	}

	@DisplayName("get")
	@Nested
	public class Get {

		private Map<String, String> theReference;
		private Map<String, String> theUnreference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
			theUnreference = unreference();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractMapTestCase#provideRandomRefIndexes")
	    public void test_1(Integer index) {
        	assertNull(map.get(key(theReference,index)), "#"+index);  //$NON-NLS-1$
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractMapTestCase#provideRandomUnrefIndexes")
	    public void test_2(Integer index) {
	        var elt = key(theUnreference, index);
	        assertNull(map.get(elt), "#"+index);  //$NON-NLS-1$
		}
	        
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractMapTestCase#provideRandomRefIndexes")
	    public void test_3(Integer index) {
	        initMapWith(theReference, theReference);
        	var elt = key(theReference, index);
        	assertEquals(theReference.get(elt), map.get(elt), "#"+index);  //$NON-NLS-1$
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractMapTestCase#provideRandomUnrefIndexes")
	    public void test_4(Integer index) {
	        initMapWith(theReference, theReference);
	        var elt = key(theUnreference, index);
        	assertNull(map.get(elt), "#"+index);  //$NON-NLS-1$
	    }
	}

	@DisplayName("put")
	@Nested
	public class Put {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractMapTestCase#provideRandomRefIndexes")
	    public void test_1(Integer index) {
        	var key = createKeyInstance("tmp/");  //$NON-NLS-1$
        	var value = createValueInstance("tmp/");  //$NON-NLS-1$
        	map.put(key, value);
        	assertSame(value, map.get(key));
	    }
	}

	@DisplayName("remove")
	@Nested
	public class Remove {

		private Map<String, String> theReference;
		private Map<String, String> theUnreference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
			theUnreference = unreference();
	        initMapWith(theReference, theReference);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractMapTestCase#provideRandomRefIndexes")
	    public void test_1(Integer index) {
        	var elt = key(theReference, index);
        	assertSame(theReference.get(elt), map.remove(elt), "#"+index);  //$NON-NLS-1$
        	theReference.remove(elt);
        	assertNull(map.get(elt), "#"+index);  //$NON-NLS-1$
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractMapTestCase#provideRandomUnrefIndexes")
	    public void test_2(Integer index) {
        	var elt = key(theUnreference, index);
        	assertNull(map.remove(elt), "#"+index);  //$NON-NLS-1$
	    }
	}

	@DisplayName("putAll")
	@Nested
	public class PutAll {

		private Map<String, String> theReference;
		private Map<String, String> theUnreference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
			theUnreference = unreference();
	        map.putAll(theReference);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractMapTestCase#provideRandomRefIndexes")
	    public void test_1(Integer index) {
        	var elt = key(theReference, index);
        	assertEquals(theReference.get(elt), map.get(elt), "#"+index);  //$NON-NLS-1$
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractMapTestCase#provideRandomUnrefIndexes")
	    public void test_2(Integer index) {
        	var elt = key(theUnreference, index);
        	assertNull(map.get(elt), "#"+index);  //$NON-NLS-1$
		}
    }

	@DisplayName("clear")
	@Nested
	public class Clear {

		private Map<String, String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
		}

		@DisplayName("#1")
		@Test
	    public void test_1() {
			assertEquals(0, map.size());
			assertTrue(map.isEmpty());
		}
	    	
		@DisplayName("#2")
		@Test
	    public void test_2() {
	    	initMapWith(theReference, theReference);
	    	assertEquals(theReference.size(), map.size());
	    	assertFalse(map.isEmpty());
		}

		@DisplayName("#3")
		@Test
	    public void test_3() {
	    	initMapWith(theReference, theReference);
	    	map.clear();
	    	assertEquals(0, map.size());
	    	assertTrue(map.isEmpty());
	    }
	}

	@DisplayName("keySet")
	@Nested
	public class KeySet {

		private Map<String, String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
		}

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	var keys = map.keySet();
	    	assertTrue(keys.isEmpty());
		}

		@DisplayName("#2")
		@Test
	    public void test_2() {
	    	initMapWith(theReference, theReference);
	    	var keys = map.keySet();
	    	assertFalse(keys.isEmpty());
	    	assertEpsilonEquals(theReference.keySet(), keys);
	    }
	}

	@DisplayName("values")
	@Nested
	public class Values {

		private Map<String, String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
		}

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	var values = map.values();
	    	assertTrue(values.isEmpty());
		}

		@DisplayName("#2")
		@Test
	    public void test_2() {
	    	initMapWith(theReference, theReference);	
	    	var values = map.values();
	    	assertFalse(values.isEmpty());
	    	assertEpsilonEquals(theReference.values(), values);
	    }
	}

}
