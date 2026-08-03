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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
 * @param <COL>
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public abstract class AbstractCollectionTestCase<COL extends Collection<String>> extends AbstractTestCase {

	private static final Random RANDOM = new Random();
	
	protected static final int refCount = 10;

	protected static final int unrefCount = 5;

	protected static List<String> referenceList;

	protected static List<String> unreferenceList;

	protected COL collection;
	
	protected static List<String> reference() {
		synchronized (AbstractCollectionTestCase.class) {
			if (referenceList == null || referenceList.size() != refCount) {
				referenceList = new ArrayList<>(refCount);
				for(int idx = 0; idx < refCount; ++idx) {
					referenceList.add("REF_" + idx);  //$NON-NLS-1$
				}
			}
			assertTrue(referenceList != null && !referenceList.isEmpty());
			return referenceList;
		}
	}

	protected static List<String> unreference() {
		synchronized (AbstractCollectionTestCase.class) {
			if (unreferenceList == null || referenceList.size() != unrefCount) {
				unreferenceList = new ArrayList<>(unrefCount);
				for(int idx = 0; idx < unrefCount; ++idx) {
					unreferenceList.add("UNREF_" + idx);  //$NON-NLS-1$
				}
			}
			assertTrue(unreferenceList != null && !unreferenceList.isEmpty());
			return unreferenceList;
		}
	}

	@BeforeEach
	public void setUp() throws Exception {
		collection = createCollection();
	}
	
	protected abstract COL createCollection();
	
	protected final void initCollectionWith(Collection<String> toAdd) {
		collection.clear();
		collection.addAll(toAdd);
	}
	
	protected final void fillCollectionWith(Collection<String> toAdd) {
		collection.addAll(toAdd);
	}
	
	private static Stream<Arguments> provideRandomNumbers() {
		final var arguments = new ArrayList<Arguments>();
        int count = RANDOM.nextInt(50) + 50;
		for (int i = 0; i < count; ++i) {
        	int value = RANDOM.nextInt(100);
			arguments.add(Arguments.of(value));
		}
		return arguments.stream();
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

	@AfterEach
	public void tearDown() throws Exception {
		collection = null;
	}

	@DisplayName("addAll")
	@Nested
	public class AddAll {

		private List<String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
		}
		
		@DisplayName("#1")
		@Test
		public void test_1() {
	        assertTrue(collection.addAll(theReference));
	        assertEpsilonEquals(theReference, collection);
		}
	}

	@DisplayName("size")
	@Nested
	public class Size {

		private List<String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
	        assertEquals(0, collection.size());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
	        initCollectionWith(theReference);        
	        assertEquals(theReference.size(), collection.size());
	    }
	}

	@DisplayName("isEmpty")
	@Nested
	public class IsEmpty {

		private List<String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(collection.isEmpty());
	    }

		@DisplayName("#2")
		@Test
		public void test_2() {
	        initCollectionWith(theReference);        
	        assertFalse(collection.isEmpty());
	    }
	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		private List<String> theReference;
		private List<String> theUnreference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
			theUnreference = unreference();
			initCollectionWith(theReference);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractCollectionTestCase#provideRandomRefIndexes")
	    public void test_1(Integer index) {
        	assertTrue(collection.contains(theReference.get(index)));
		}
	        
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractCollectionTestCase#provideRandomUnrefIndexes")
	    public void test_2(Integer index) {
        	var elt = theUnreference.get(index);
        	assertFalse(collection.contains(elt));
	    }
	}

	@DisplayName("iterator")
	@Nested
	public class IteratorTest {

		private List<String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
			initCollectionWith(theReference);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
	    	var iter = collection.iterator();
	    	boolean asOne = false;
	    	while (iter.hasNext()) {
	    		var s = iter.next();
	    		asOne = true;
	    		assertTrue(theReference.contains(s));
	    	}
	    	assertTrue(asOne);
	    	assertTrue(theReference.size() >= collection.size());
	    }
	}

	@DisplayName("containsAll")
	@Nested
	public class ContainsAll {

		private List<String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
			initCollectionWith(theReference);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
	        assertTrue(theReference.containsAll(collection));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
	        assertTrue(collection.containsAll(theReference));
		}
	}
    
	@DisplayName("toArray")
	@Nested
	public class ToArray {

		private List<String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
			initCollectionWith(theReference);
		}

		@DisplayName("()")
		@Test
		public void empty_1() {
	    	assertEpsilonEquals(theReference.toArray(),collection.toArray());
		}

		@DisplayName("(Object[]) #1")
		@Test
		public void testToArrayArray_1() {
	        Object[] tab = new Object[theReference.size()];
	    	assertEpsilonEquals(theReference.toArray(),collection.toArray(tab));
	    	assertEpsilonEquals(theReference.toArray(),tab);
		}

		@DisplayName("(Object[]) #2")
		@Test
		public void testToArrayArray_2() {
	        var tab = new Object[theReference.size()/2];
	        Object[] tab2 = collection.toArray(tab);
	        assertEpsilonEquals(theReference.toArray(),tab2);
	    	assertNotEpsilonEquals(tab2,tab);
		}
	}
	

	@DisplayName("Clear")
	@Nested
	public class Clear {

		private List<String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
			initCollectionWith(theReference);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
	    	collection.clear();
	    	assertTrue(collection.isEmpty());
		}
	}
	
	@DisplayName("add")
	@Nested
	public class Add {

		private List<String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
			initCollectionWith(theReference);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractCollectionTestCase#provideRandomNumbers")
	    public void test_1(Integer number) {
	        String newElement = "NEWELT"+number;  //$NON-NLS-1$
	        theReference.add(newElement);
	        assertTrue(collection.add(newElement));
	        assertEquals(theReference.size(), collection.size());
	        assertTrue(collection.contains(newElement));
	    	assertEpsilonEquals(theReference.toArray(),collection.toArray());
		}
	}

	@DisplayName("remove")
	@Nested
	public class Remove {

		private List<String> theReference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
			initCollectionWith(theReference);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.references.AbstractCollectionTestCase#provideRandomRefIndexes")
	    public void test_1(Integer index) {
            String toRemove = theReference.get(index);
            assertTrue(collection.remove(toRemove));
            assertFalse(collection.contains(toRemove));
	        theReference.remove(toRemove);
	    	assertEpsilonEquals(theReference.toArray(),collection.toArray());
        }
	}
	
	@DisplayName("removeAll")
	@Nested
	public class RemoveAll {

		private List<String> theReference;
		private List<String> theUnreference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
			theUnreference = unreference();
			initCollectionWith(theReference);
	        fillCollectionWith(theUnreference);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
	        assertEquals(theReference.size() + theUnreference.size(), collection.size());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
	        assertTrue(collection.removeAll(theReference));
	        assertEquals(theUnreference.size(), collection.size());
	    	assertEpsilonEquals(theUnreference,collection);
		}
	}

	@DisplayName("retainAll")
	@Nested
	public class RetainAll {

		private List<String> theReference;
		private List<String> theUnreference;

		@BeforeEach
		public void setUp() {
			theReference = reference();
			theUnreference = unreference();
			initCollectionWith(theReference);
	        fillCollectionWith(theUnreference);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
	        assertEquals(theReference.size() + theUnreference.size(), collection.size());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
	        assertTrue(collection.retainAll(theReference));
	        assertEquals(theReference.size(), collection.size());
	    	assertEpsilonEquals(theReference,collection);
		}
	}

}
