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

import java.util.NoSuchElementException;
import java.util.UUID;

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
@DisplayName("SingleIterator")
@SuppressWarnings("all")
public class SingleIteratorTest {

	private String s1;
	private SingleIterator<String> iterator;
	
	@BeforeEach
	public void setUp() throws Exception {
		s1 = UUID.randomUUID().toString();
		iterator = new SingleIterator<>(s1);
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		iterator = null;
		s1 = null;
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
			try {
				iterator.next();
				fail("expecting NoSuchElementException");  //$NON-NLS-1$
			}
			catch(NoSuchElementException exception) {
				// expected exception
			}
		}
	}

	@DisplayName("remove")
	@Nested
	public class Remove {

		@DisplayName("#1")
		@Test
		public void remove() {
			try {
				iterator.remove();
				fail("expecting UnsupportedOperationException");  //$NON-NLS-1$
			}
			catch(UnsupportedOperationException exception) {
				// exepcted exception
			}
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
			iterator.next();
			assertEquals(0, iterator.index());
		}
	}

	@DisplayName("rest")
	@Nested
	public class Rest {

		@DisplayName("#1")
		@Test
		public void rest_1() {
			assertEquals(1, iterator.rest());
		}

		@DisplayName("#2")
		@Test
		public void rest_2() {
			iterator.next();
			assertEquals(0, iterator.rest());
		}
	}

	@DisplayName("totalSize")
	@Nested
	public class TotalSize {

		@DisplayName("#1")
		@Test
		public void totalSize_1() {
			assertEquals(1, iterator.totalSize());
		}

		@DisplayName("#2")
		@Test
		public void totalSize_2() {
			iterator.next();
			assertEquals(1, iterator.totalSize());
		}
	}
	
}
