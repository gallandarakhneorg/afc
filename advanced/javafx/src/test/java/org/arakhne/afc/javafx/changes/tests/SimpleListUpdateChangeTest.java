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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.arakhne.afc.javafx.changes.SimpleListUpdateChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("SimpleListUpdateChange")
@SuppressWarnings("all")
public class SimpleListUpdateChangeTest {

	private ObservableList<String> list;

	@BeforeEach
	public void setUp() {
		this.list = FXCollections.observableArrayList("a", "b", "c");
	}

	@DisplayName("constructor (position, list)")
	@Nested
	public class ConstructorWithPosition {

		private SimpleListUpdateChange<String> change;

		@BeforeEach
		public void setUp() {
			change = new SimpleListUpdateChange<>(1, list);
		}

		@DisplayName("from equals position after next")
		@Test
		public void getFrom_afterNext() {
			change.next();
			assertEquals(1, change.getFrom());
		}

		@DisplayName("to equals position+1 after next")
		@Test
		public void getTo_afterNext() {
			change.next();
			assertEquals(2, change.getTo());
		}

		@DisplayName("throws before next")
		@Test
		public void throwsBeforeNext() {
			assertThrows(IllegalStateException.class, () -> change.getFrom());
			assertThrows(IllegalStateException.class, () -> change.getTo());
		}
	}

	@DisplayName("constructor (from, to, list)")
	@Nested
	public class ConstructorWithFromTo {

		private SimpleListUpdateChange<String> change;

		@BeforeEach
		public void setUp() {
			change = new SimpleListUpdateChange<>(0, 3, list);
		}

		@DisplayName("from preserved after next")
		@Test
		public void getFrom_afterNext() {
			change.next();
			assertEquals(0, change.getFrom());
		}

		@DisplayName("to preserved after next")
		@Test
		public void getTo_afterNext() {
			change.next();
			assertEquals(3, change.getTo());
		}
	}

	@DisplayName("change iteration lifecycle")
	@Nested
	public class IterationLifecycle {

		private SimpleListUpdateChange<String> change;

		@BeforeEach
		public void setUp() {
			change = new SimpleListUpdateChange<>(0, 1, list);
		}

		@DisplayName("next true then false")
		@Test
		public void nextSequence() {
			assertTrue(change.next());
			assertFalse(change.next());
		}

		@DisplayName("reset re-enables next")
		@Test
		public void resetThenNext() {
			change.next();
			change.reset();
			assertTrue(change.next());
		}
	}

	@DisplayName("semantic flags")
	@Nested
	public class SemanticFlags {

		private SimpleListUpdateChange<String> change;

		@BeforeEach
		public void setUp() {
			change = new SimpleListUpdateChange<>(2, list);
			change.next();
		}

		@DisplayName("wasUpdated true")
		@Test
		public void wasUpdated_true() {
			assertTrue(change.wasUpdated());
		}

		@DisplayName("removed list empty")
		@Test
		public void removed_empty() {
			List<String> removed = change.getRemoved();
			assertTrue(removed.isEmpty());
		}

		@DisplayName("list reference is preserved")
		@Test
		public void listReference_preserved() {
			assertSame(list, change.getList());
		}
	}
}