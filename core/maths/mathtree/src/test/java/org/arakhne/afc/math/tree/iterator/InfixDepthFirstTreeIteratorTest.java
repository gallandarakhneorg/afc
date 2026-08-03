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
 * child11
 * child1
 * child1211
 * child121
 * child12121
 * child1212
 * child12122
 * child12
 * child122
 * root
 * child211
 * child21
 * child2
 * child22
 * child222
 * </code></pre>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@DisplayName("InfixDepthFirstTreeIterator")
@SuppressWarnings("all")
public class InfixDepthFirstTreeIteratorTest extends AbstractTreeIteratorTest {

	private InfixDepthFirstTreeIterator<DefaultBinaryTreeNode<Object>> iterator;

	@BeforeEach
	@Override
	public void setUp() throws Exception {
		super.setUp();
		iterator = new InfixDepthFirstTreeIterator<>(tree);
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
			assertSame(child11, iterator.next());
		}

		@DisplayName("#2")
		@Test
		public void iterate_2() {
			for (int i = 1; i < 2; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(child1, iterator.next());
		}

		@DisplayName("#3")
		@Test
		public void iterate_3() {
			for (int i = 1; i < 3; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(child1211, iterator.next());
		}

		@DisplayName("#4")
		@Test
		public void iterate_4() {
			for (int i = 1; i < 4; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(child121, iterator.next());
		}

		@DisplayName("#5")
		@Test
		public void iterate_5() {
			for (int i = 1; i < 5; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(child12121, iterator.next());
		}

		@DisplayName("#6")
		@Test
		public void iterate_6() {
			for (int i = 1; i < 6; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(child1212, iterator.next());
		}

		@DisplayName("#7")
		@Test
		public void iterate_7() {
			for (int i = 1; i < 7; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(child12122, iterator.next());
		}

		@DisplayName("#8")
		@Test
		public void iterate_8() {
			for (int i = 1; i < 8; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(child12, iterator.next());
		}

		@DisplayName("#9")
		@Test
		public void iterate_9() {
			for (int i = 1; i < 9; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(child122, iterator.next());
		}

		@DisplayName("#10")
		@Test
		public void iterate_10() {
			for (int i = 1; i < 10; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(root, iterator.next());
		}

		@DisplayName("#11")
		@Test
		public void iterate_11() {
			for (int i = 1; i < 11; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(child211, iterator.next());
		}

		@DisplayName("#12")
		@Test
		public void iterate_12() {
			for (int i = 1; i < 12; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(child21, iterator.next());
		}

		@DisplayName("#13")
		@Test
		public void iterate_13() {
			for (int i = 1; i < 13; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(child2, iterator.next());
		}

		@DisplayName("#14")
		@Test
		public void iterate_14() {
			for (int i = 1; i < 14; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(child22, iterator.next());
		}

		@DisplayName("#15")
		@Test
		public void iterate_15() {
			for (int i = 1; i < 15; ++i) {
				iterator.next();
			}
			assertTrue(iterator.hasNext());
			assertSame(child222, iterator.next());
		}

		@DisplayName("#16")
		@Test
		public void iterate_16() {
			for (int i = 1; i < 16; ++i) {
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
			Object last = null;
			for (int i = 0; i < 12; ++i) {
				last = iterator.next();
			}
			assertSame(child21, last);
		}
		
		@DisplayName("#1")
		@Test
		public void remove_1() {
			iterator.remove();
			assertSame(child2, iterator.next());
			assertSame(child22, iterator.next());
			assertSame(child222, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#2")
		@Test
		public void remove_2() {
			iterator.remove();
			assertThrows(NoSuchElementException.class, () -> iterator.remove());
		}

		@DisplayName("#3")
		@Test
		public void remove_3() {
			iterator.remove();
			var it = tree.depthFirstIterator();
			assertSame(root, it.next());
			assertSame(child1, it.next());
			assertSame(child11, it.next());
			assertSame(child12, it.next());
			assertSame(child121, it.next());
			assertSame(child1211, it.next());
			assertSame(child1212, it.next());
			assertSame(child12121, it.next());
			assertSame(child12122, it.next());
			assertSame(child122, it.next());
			assertSame(child2, it.next());
			assertSame(child22, it.next());
			assertSame(child222, it.next());
			assertFalse(it.hasNext());
		}
	}

}