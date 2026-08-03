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

package org.arakhne.afc.math.tree.iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.tree.node.BinaryTreeNode.DefaultBinaryTreeNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** <pre><code>
 * b - child11
 * h - child1211
 * jk - child12121
 * lmn - child12122
 * i - child1212
 * efg - child121
 * - child122
 * cd - child12
 * a - child1
 * qr - child211
 * o - child21
 * st - child222
 * p - child22
 * - child2
 * - root
 * </code></pre>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@DisplayName("PostfixDataDepthFirstTreeIterator")
@SuppressWarnings("all")
public class PostfixDataDepthFirstTreeIteratorTest extends AbstractDataTreeIteratorTest {

	private PostfixDataDepthFirstTreeIterator<Object,DefaultBinaryTreeNode<Object>> iterator;

	@BeforeEach
	@Override
	public void setUp() throws Exception {
		super.setUp();
		iterator = new PostfixDataDepthFirstTreeIterator<>(tree);
	}

	@AfterEach
	@Override
	public void tearDown() throws Exception {
		iterator = null;
		super.tearDown();
	}

	@DisplayName("iterate")
	@Nested
	public class Iterate {

		@DisplayName("#1")
		@Test
		public void iterate_1() {
			assertTrue(iterator.hasNext());
			assertEquals("b", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void iterate_2() {
			for (int i = 1; i < 2; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("h", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void iterate_3() {
			for (int i = 1; i < 3; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("j", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#4")
		@Test
		public void iterate_4() {
			for (int i = 1; i < 4; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("k", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#5")
		@Test
		public void iterate_5() {
			for (int i = 1; i < 5; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("l", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#6")
		@Test
		public void iterate_6() {
			for (int i = 1; i < 6; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("m", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#7")
		@Test
		public void iterate_7() {
			for (int i = 1; i < 7; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("n", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#8")
		@Test
		public void iterate_8() {
			for (int i = 1; i < 8; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("i", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#9")
		@Test
		public void iterate_9() {
			for (int i = 1; i < 9; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("e", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#10")
		@Test
		public void iterate_10() {
			for (int i = 1; i < 10; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("f", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#11")
		@Test
		public void iterate_11() {
			for (int i = 1; i < 11; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("g", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#12")
		@Test
		public void iterate_12() {
			for (int i = 1; i < 12; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("c", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#13")
		@Test
		public void iterate_13() {
			for (int i = 1; i < 13; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("d", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#14")
		@Test
		public void iterate_14() {
			for (int i = 1; i < 14; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("a", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#15")
		@Test
		public void iterate_15() {
			for (int i = 1; i < 15; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("q", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#16")
		@Test
		public void iterate_16() {
			for (int i = 1; i < 16; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("r", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#17")
		@Test
		public void iterate_17() {
			for (int i = 1; i < 17; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("o", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#18")
		@Test
		public void iterate_18() {
			for (int i = 1; i < 18; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("s", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#19")
		@Test
		public void iterate_19() {
			for (int i = 1; i < 19; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("t", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#20")
		@Test
		public void iterate_20() {
			for (int i = 1; i < 20; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertEquals("p", iterator.next());  //$NON-NLS-1$
		}

		@DisplayName("#21")
		@Test
		public void iterate_21() {
			for (int i = 1; i < 21; ++i) {
				iterator.next();
			}
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("remove")
	@Nested
	public class Remove {

		@BeforeEach
		public void setUp() {
			// Move to "l"
			Object last = null;
			for (int i = 0; i < 12; ++i) {
				last = iterator.next();
			}
			assertEquals("c", last);
		}

		@DisplayName("#1")
		@Test
		public void remove_1() {
			iterator.remove();
			var it = tree.dataDepthFirstIterator();
			assertSame("a", it.next());
			assertSame("b", it.next());
			assertSame("d", it.next());
			assertSame("e", it.next());
			assertSame("f", it.next());
			assertSame("g", it.next());
			assertSame("h", it.next());
			assertSame("i", it.next());
			assertSame("j", it.next());
			assertSame("k", it.next());
			assertSame("l", it.next());
			assertSame("m", it.next());
			assertSame("n", it.next());
			assertSame("o", it.next());
			assertSame("q", it.next());
			assertSame("r", it.next());
			assertSame("p", it.next());
			assertSame("s", it.next());
			assertSame("t", it.next());
			assertFalse(it.hasNext());
		}

		@DisplayName("#2")
		@Test
		public void remove_2() {
			iterator.remove();
			assertThrows(NoSuchElementException.class, () -> iterator.remove());
		}
	}
	
}