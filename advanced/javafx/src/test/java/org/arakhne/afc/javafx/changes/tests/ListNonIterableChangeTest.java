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

package org.arakhne.afc.javafx.changes.tests;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.arakhne.afc.javafx.changes.ListNonIterableChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ListNonIterableChange")
@SuppressWarnings("all")
public class ListNonIterableChangeTest {

	private ObservableList<String> list;

	@BeforeEach
	public void setUp() {
		this.list = FXCollections.observableArrayList("a", "b", "c");
	}

	private static final class TestChange extends ListNonIterableChange<String> {

		TestChange(int from, int to, ObservableList<String> list) {
			super(from, to, list);
		}

		@Override
		public List<String> getRemoved() {
			return Collections.emptyList();
		}

		@Override
		public boolean wasUpdated() {
			return false;
		}
		
		@Override
		public int[] getPermutation() {
			return super.getPermutation();
		}
	}

	@DisplayName("initial state")
	@Nested
	public class InitialState {

		private TestChange change;

		@BeforeEach
		public void setUp() {
			change = new TestChange(1, 3, list);
		}

		@DisplayName("next returns true first time")
		@Test
		public void next_firstCall_returnsTrue() {
			assertTrue(change.next());
		}

		@DisplayName("next returns false second time")
		@Test
		public void next_secondCall_returnsFalse() {
			change.next();
			assertFalse(change.next());
		}

		@DisplayName("getFrom throws before next")
		@Test
		public void getFrom_beforeNext_throws() {
			assertThrows(IllegalStateException.class, () -> change.getFrom());
		}

		@DisplayName("getTo throws before next")
		@Test
		public void getTo_beforeNext_throws() {
			assertThrows(IllegalStateException.class, () -> change.getTo());
		}

		@DisplayName("getPermutation throws before next")
		@Test
		public void getPermutation_beforeNext_throws() {
			assertThrows(IllegalStateException.class, () -> change.getPermutation(0));
		}

		@DisplayName("checkState throws before next")
		@Test
		public void checkState_beforeNext_throws() {
			assertThrows(IllegalStateException.class, () -> change.checkState());
		}
	}

	@DisplayName("after next")
	@Nested
	public class AfterNext {

		private TestChange change;

		@BeforeEach
		public void setUp() {
			change = new TestChange(1, 3, list);
			change.next();
		}

		@DisplayName("getFrom returns constructor value")
		@Test
		public void getFrom_returnsFrom() {
			assertEquals(1, change.getFrom());
		}

		@DisplayName("getTo returns constructor value")
		@Test
		public void getTo_returnsTo() {
			assertEquals(3, change.getTo());
		}

		@DisplayName("getPermutation is empty")
		@Test
		public void getPermutation_empty() {
			assertArrayEquals(new int[0], change.getPermutation());
		}

		@DisplayName("checkState does not throw")
		@Test
		public void checkState_noThrow() {
			change.checkState();
		}
	}

	@DisplayName("reset")
	@Nested
	public class Reset {

		private TestChange change;

		@BeforeEach
		public void setUp() {
			change = new TestChange(2, 2, list);
			change.next();
			change.reset();
		}

		@DisplayName("state invalid after reset")
		@Test
		public void reset_invalidatesState() {
			assertThrows(IllegalStateException.class, () -> change.getFrom());
		}

		@DisplayName("next returns true again after reset")
		@Test
		public void reset_nextTrueAgain() {
			assertTrue(change.next());
		}

		@DisplayName("values preserved after reset and next")
		@Test
		public void reset_preservesFromTo() {
			change.next();
			assertEquals(2, change.getFrom());
			assertEquals(2, change.getTo());
		}
	}
}
