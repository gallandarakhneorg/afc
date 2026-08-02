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

package org.arakhne.afc.sizediterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@DisplayName("CollectionSizedIterator")
@SuppressWarnings("all")
public class CollectionSizedIteratorTest {

	private String s1, s2, s3, s4, s5;
	private Collection<String> collection;
	private CollectionSizedIterator<String> iterator;
	
	@BeforeEach
	public void setUp() throws Exception {
		collection = new ArrayList<>();
		collection.add(s1 = "s1");  //$NON-NLS-1$
		collection.add(s2 = "s2");  //$NON-NLS-1$
		collection.add(s3 = "s3");  //$NON-NLS-1$
		collection.add(s4 = "s4");  //$NON-NLS-1$
		collection.add(s5 = "s5");  //$NON-NLS-1$
		iterator = new CollectionSizedIterator<>(collection);
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		iterator = null;
		collection = null;
		s1 = s2 = s3 = s4 = s5 = null;
	}

	@DisplayName("hasNext")
	@Nested
	public class HasNext {

		@DisplayName("#1")
		@Test
		public void hasNext_1() {
			assertTrue(iterator.hasNext());
		}

		@DisplayName("#2")
		@Test
		public void hasNext_2() {
			iterator.next();
			assertTrue(iterator.hasNext());
		}

		@DisplayName("#3")
		@Test
		public void hasNext_3() {
			iterator.next();
			iterator.next();
			assertTrue(iterator.hasNext());
		}

		@DisplayName("#4")
		@Test
		public void hasNext_4() {
			iterator.next();
			iterator.next();
			iterator.next();
			assertTrue(iterator.hasNext());
		}

		@DisplayName("#5")
		@Test
		public void hasNext_5() {
			iterator.next();
			iterator.next();
			iterator.next();
			iterator.next();
			assertTrue(iterator.hasNext());
		}

		@DisplayName("#6")
		@Test
		public void hasNext_6() {
			iterator.next();
			iterator.next();
			iterator.next();
			iterator.next();
			iterator.next();
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("next")
	@Nested
	public class Next {

		@DisplayName("#1")
		@Test
		public void next_1() {
			assertSame(s1, iterator.next());
		}

		@DisplayName("#2")
		@Test
		public void next_2() {
			iterator.next();
			assertSame(s2, iterator.next());
		}

		@DisplayName("#3")
		@Test
		public void next_3() {
			iterator.next();
			iterator.next();
			assertSame(s3, iterator.next());
		}

		@DisplayName("#4")
		@Test
		public void next_4() {
			iterator.next();
			iterator.next();
			iterator.next();
			assertSame(s4, iterator.next());
		}

		@DisplayName("#5")
		@Test
		public void next_5() {
			iterator.next();
			iterator.next();
			iterator.next();
			iterator.next();
			assertSame(s5, iterator.next());
		}

		@DisplayName("#6")
		@Test
		public void next_6() {
			iterator.next();
			iterator.next();
			iterator.next();
			iterator.next();
			iterator.next();
			try {
				iterator.next();
				fail("expecting NoSuchElementException");  //$NON-NLS-1$
			}
			catch(NoSuchElementException exception) {
				// Expected exception
			}
		}
	}

	@DisplayName("remove")
	@Nested
	public class Remove {

		@DisplayName("#1")
		@Test
		public void remove_1() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			iterator.remove();
			
			assertEquals(4, collection.size());
			assertTrue(collection.contains(s1));
			assertFalse(collection.contains(s2));
			assertTrue(collection.contains(s3));
			assertTrue(collection.contains(s4));
			assertTrue(collection.contains(s5));
		}

		@DisplayName("#2")
		@Test
		public void remove_2() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			iterator.remove();
			assertSame(s3, iterator.next());
			assertSame(s4, iterator.next());
			iterator.remove();
			
			assertEquals(3, collection.size());
			assertTrue(collection.contains(s1));
			assertFalse(collection.contains(s2));
			assertTrue(collection.contains(s3));
			assertFalse(collection.contains(s4));
			assertTrue(collection.contains(s5));
		}
	}

	@DisplayName("index")
	@Nested
	public class Index {

		@DisplayName("#1")
		@Test
		public void index_1() {
			assertEquals(-1, iterator.index());
		}

		@DisplayName("#2")
		@Test
		public void index_2() {
			assertSame(s1, iterator.next());
			assertEquals(0, iterator.index());
		}

		@DisplayName("#3")
		@Test
		public void index_3() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			assertEquals(1, iterator.index());
		}

		@DisplayName("#4")
		@Test
		public void index_4() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			assertSame(s3, iterator.next());
			assertEquals(2, iterator.index());
		}

		@DisplayName("#5")
		@Test
		public void index_5() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			assertSame(s3, iterator.next());
			assertSame(s4, iterator.next());
			assertEquals(3, iterator.index());
		}

		@DisplayName("#6")
		@Test
		public void index_6() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			assertSame(s3, iterator.next());
			assertSame(s4, iterator.next());
			assertSame(s5, iterator.next());
			assertEquals(4, iterator.index());
		}
	}

	@DisplayName("rest")
	@Nested
	public class Rest {

		@DisplayName("#1")
		@Test
		public void rest_1() {
			assertEquals(5, iterator.rest());
		}

		@DisplayName("#2")
		@Test
		public void rest_2() {
			assertSame(s1, iterator.next());
			assertEquals(4, iterator.rest());
		}

		@DisplayName("#3")
		@Test
		public void rest_3() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			assertEquals(3, iterator.rest());
		}

		@DisplayName("#4")
		@Test
		public void rest_4() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			assertSame(s3, iterator.next());
			assertEquals(2, iterator.rest());
		}

		@DisplayName("#5")
		@Test
		public void rest_5() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			assertSame(s3, iterator.next());
			assertSame(s4, iterator.next());
			assertEquals(1, iterator.rest());
		}

		@DisplayName("#6")
		@Test
		public void rest_6() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			assertSame(s3, iterator.next());
			assertSame(s4, iterator.next());
			assertSame(s5, iterator.next());
			assertEquals(0, iterator.rest());
		}
	}

	@DisplayName("totalSize")
	@Nested
	public class TotalSize {

		@DisplayName("#1")
		@Test
		public void totalSize_1() {
			assertEquals(5, iterator.totalSize());
		}

		@DisplayName("#2")
		@Test
		public void totalSize_2() {
			assertSame(s1, iterator.next());
			assertEquals(5, iterator.totalSize());
		}

		@DisplayName("#3")
		@Test
		public void totalSize_3() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			assertEquals(5, iterator.totalSize());
		}

		@DisplayName("#4")
		@Test
		public void totalSize_4() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			iterator.remove();
			assertEquals(4, iterator.totalSize());
		}

		@DisplayName("#5")
		@Test
		public void totalSize_5() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			iterator.remove();
			assertSame(s3, iterator.next());
			assertEquals(4, iterator.totalSize());
		}

		@DisplayName("#6")
		@Test
		public void totalSize_6() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			iterator.remove();
			assertSame(s3, iterator.next());
			assertSame(s4, iterator.next());
			assertEquals(4, iterator.totalSize());
		}

		@DisplayName("#7")
		@Test
		public void totalSize_7() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			iterator.remove();
			assertSame(s3, iterator.next());
			assertSame(s4, iterator.next());
			iterator.remove();
			assertEquals(3, iterator.totalSize());
		}

		@DisplayName("#8")
		@Test
		public void totalSize_8() {
			assertSame(s1, iterator.next());
			assertSame(s2, iterator.next());
			iterator.remove();
			assertSame(s3, iterator.next());
			assertSame(s4, iterator.next());
			iterator.remove();
			assertSame(s5, iterator.next());
			assertEquals(3, iterator.totalSize());
		}
	}

}
