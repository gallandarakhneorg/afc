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

package org.arakhne.afc.nodefx.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.arakhne.afc.nodefx.Drawer;
import org.arakhne.afc.nodefx.DrawerReference;
import org.arakhne.afc.nodefx.Drawers;
import org.arakhne.afc.nodefx.tests.mocks.ContDrawer1;
import org.arakhne.afc.nodefx.tests.mocks.MyDrawer1;
import org.arakhne.afc.nodefx.tests.mocks.MyDrawer2;
import org.arakhne.afc.nodefx.tests.mocks.MyDrawer3;
import org.arakhne.afc.nodefx.tests.mocks.MyDrawer4;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Drawers")
@SuppressWarnings("all")
public class DrawersTest {

	private static List<Drawer<?>> drawers;

	public static Class<Drawer<?>>[] DRAWERS = new Class[] {
			ContDrawer1.class,
			MyDrawer1.class,
			MyDrawer2.class,
			MyDrawer3.class,
			MyDrawer4.class,
	};
	
	private static List<Drawer<?>> createDrawers() {
		List<Drawer<?>> drawers = new ArrayList<>();

		for(final Class<Drawer<?>> type : DRAWERS) {
			try {
				final Drawer<?> drawer = (Drawer<?>) type.getConstructor().newInstance();
				drawers.add(drawer);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

		return drawers;
	}
	
	@BeforeAll
	public static void setUp() {
		drawers = createDrawers();
		Drawers.setBackedDrawers(drawers);
	}

	@AfterAll
	public static void tearDown() {
		Drawers.setBackedDrawers(null);
		drawers = null;
	}
	
	@DisplayName("getAllDrawers")
	@Nested
	public class GetAllDrawers {

		private Iterator<Drawer<?>> iterator1;
		private Drawer<?> d1;
		private Drawer<?> d2;
		private Drawer<?> d3;
		private Drawer<?> d4;
		private Drawer<?> d5;
		private Set<Class<? extends Drawer<?>>> types;

		@BeforeEach
		public void setUp() {
			iterator1 = Drawers.getAllDrawers();
			d1 = iterator1.next();
			d2 = iterator1.next();
			d3 = iterator1.next();
			d4 = iterator1.next();
			d5 = iterator1.next();
			
			types = new HashSet<>();
			types.add(MyDrawer1.class);
			types.add(MyDrawer2.class);
			types.add(MyDrawer3.class);
			types.add(MyDrawer4.class);
			types.add(ContDrawer1.class);
		}

		@DisplayName("#1")
		@Test
		public void getAllDrawers_assertion1() {
			assertNotNull(iterator1);
		}

		@DisplayName("#2")
		@Test
		public void getAllDrawers_assertion2() {
			assertTrue(Drawers.getAllDrawers().hasNext());
		}

		@DisplayName("#3")
		@Test
		public void getAllDrawers_assertion3() {
			Iterator<Drawer<?>> iter = Drawers.getAllDrawers();
			iter.next();
			assertTrue(iter.hasNext());
		}

		@DisplayName("#4")
		@Test
		public void getAllDrawers_assertion4() {
			Iterator<Drawer<?>> iter = Drawers.getAllDrawers();
			iter.next();
			iter.next();
			assertTrue(iter.hasNext());
		}

		@DisplayName("#5")
		@Test
		public void getAllDrawers_assertion5() {
			Iterator<Drawer<?>> iter = Drawers.getAllDrawers();
			iter.next();
			iter.next();
			iter.next();
			assertTrue(iter.hasNext());
		}

		@DisplayName("#6")
		@Test
		public void getAllDrawers_assertion6() {
			Iterator<Drawer<?>> iter = Drawers.getAllDrawers();
			iter.next();
			iter.next();
			iter.next();
			iter.next();
			assertTrue(iter.hasNext());
		}

		@DisplayName("#7")
		@Test
		public void getAllDrawers_assertion7() {
			Iterator<Drawer<?>> iter = Drawers.getAllDrawers();
			iter.next();
			iter.next();
			iter.next();
			iter.next();
			iter.next();
			assertFalse(iter.hasNext());
		}

		@DisplayName("#8")
		@Test
		public void getAllDrawers_assertion8() {
			assertNotNull(d1);
		}

		@DisplayName("#9")
		@Test
		public void getAllDrawers_assertion9() {
			assertNotNull(d2);
		}

		@DisplayName("#10")
		@Test
		public void getAllDrawers_assertion10() {
			assertNotNull(d3);
		}

		@DisplayName("#11")
		@Test
		public void getAllDrawers_assertion11() {
			assertNotNull(d4);
		}

		@DisplayName("#12")
		@Test
		public void getAllDrawers_assertion12() {
			assertNotNull(d5);
		}

		@DisplayName("#13")
		@Test
		public void getAllDrawers_assertion13() {
			assertTrue(types.remove(d1.getClass()));
		}

		@DisplayName("#14")
		@Test
		public void getAllDrawers_assertion14() {
			assertTrue(types.remove(d2.getClass()));
		}

		@DisplayName("#15")
		@Test
		public void getAllDrawers_assertion15() {
			assertTrue(types.remove(d3.getClass()));
		}

		@DisplayName("#16")
		@Test
		public void getAllDrawers_assertion16() {
			assertTrue(types.remove(d4.getClass()));
		}

		@DisplayName("#17")
		@Test
		public void getAllDrawers_assertion17() {
			assertTrue(types.remove(d5.getClass()));
		}
	}

	@DisplayName("getDrawerFor")
	@Nested
	public class GetDrawerFor {

		@DisplayName("Class")
		@Nested
		public class WithClass {

			@DisplayName("String")
			@Nested
			public class WithString {

				@DisplayName("#1")
				@Test
				public void getDrawerForClass_assertion1() {
					Drawer<String> d1 = Drawers.getDrawerFor(String.class);
					assertNotNull(d1);
				}

				@DisplayName("#2")
				@Test
				public void getDrawerForClass_assertion2() {
					Drawer<String> d1 = Drawers.getDrawerFor(String.class);
					assertTrue(d1 instanceof MyDrawer1);
				}
			}

			@DisplayName("Integer")
			@Nested
			public class WithInteger {

				@DisplayName("#1")
				@Test
				public void getDrawerForClass_assertion1() {
					Drawer<? extends Number> d1 = Drawers.getDrawerFor(Integer.class);
					assertNotNull(d1);
				}

				@DisplayName("#2")
				@Test
				public void getDrawerForClass_assertion2() {
					Drawer<? extends Number> d1 = Drawers.getDrawerFor(Integer.class);
					assertTrue(d1 instanceof MyDrawer2);
				}
			}

			@DisplayName("Double")
			@Nested
			public class WithDouble {

				@DisplayName("#1")
				@Test
				public void getDrawerForClass_assertion1() {
					Drawer<? extends Number> d1 = Drawers.getDrawerFor(Double.class);
					assertNotNull(d1);
				}

				@DisplayName("#2")
				@Test
				public void getDrawerForClass_assertion2() {
					Drawer<? extends Number> d1 = Drawers.getDrawerFor(Double.class);
					assertTrue(d1 instanceof MyDrawer3);
				}
			}

			@DisplayName("Number")
			@Nested
			public class WithNumber {

				@DisplayName("#1")
				@Test
				public void getDrawerForClass_assertion1() {
					Drawer d1 = Drawers.getDrawerFor(Number.class);
					assertNull(d1);
				}
			}
		}

		@DisplayName("\"\"")
		@Nested
		public class WithEmptyString {

			@DisplayName("#1")
			@Test
			public void getDrawerForObject_assertion1() {
				Drawer<? super String> d1 = Drawers.getDrawerFor(""); //$NON-NLS-1$
				assertNotNull(d1);
			}

			@DisplayName("#2")
			@Test
			public void getDrawerForObject_assertion2() {
				Drawer<? super String> d1 = Drawers.getDrawerFor(""); //$NON-NLS-1$
				assertTrue(d1 instanceof MyDrawer1);
			}
		}

		@DisplayName("int")
		@Nested
		public class WithInt {

			@DisplayName("#1")
			@Test
			public void getDrawerForObject_assertion1() {
				Drawer<? super Integer> d1 = Drawers.getDrawerFor(1);
				assertNotNull(d1);
			}

			@DisplayName("#2")
			@Test
			public void getDrawerForObject_assertion2() {
				Drawer<? super Integer> d1 = Drawers.getDrawerFor(1);
				assertTrue(d1 instanceof MyDrawer2);
			}
		}

		@DisplayName("double")
		@Nested
		public class WithDoubleValue {

			@DisplayName("#1")
			@Test
			public void getDrawerForObject_assertion1() {
				Drawer<? super Double> d1 = Drawers.getDrawerFor(1d);
				assertNotNull(d1);
			}

			@DisplayName("#2")
			@Test
			public void getDrawerForObject_assertion2() {
				Drawer<? super Double> d1 = Drawers.getDrawerFor(1d);
				assertTrue(d1 instanceof MyDrawer3);
			}
		}

		@DisplayName("float")
		@Nested
		public class WithFloat {

			@DisplayName("#1")
			@Test
			public void getDrawerForObject_assertion1() {
				Drawer d1 = Drawers.getDrawerFor(1f);
				assertNull(d1);
			}
		}

		@DisplayName("Object")
		@Nested
		public class WithObject {

			@DisplayName("#1")
			@Test
			public void getDrawerForObject_assertion1() {
				DrawableObject obj = new DrawableObject();
				Drawer d1 = Drawers.getDrawerFor(obj);
				assertNull(d1);
			}

			@DisplayName("#2")
			@Test
			public void getDrawerForObject_assertion2() {
				DrawableObject obj = new DrawableObject();
				obj.setDrawer(new MyDrawer1());
				Drawer d1 = Drawers.getDrawerFor(obj);
				assertNotNull(d1);
			}

			@DisplayName("#3")
			@Test
			public void getDrawerForObject_assertion3() {
				DrawableObject obj = new DrawableObject();
				obj.setDrawer(new MyDrawer1());
				Drawer d1 = Drawers.getDrawerFor(obj);
				assertTrue(d1 instanceof MyDrawer1);
			}
		}
	}

	private static class DrawableObject implements DrawerReference<String> {

		private Drawer<? super String> drawer;
	
		DrawableObject() {
			//
		}

		@Override
		public Drawer<? super String> getDrawer() {
			return this.drawer;
		}

		@Override
		public void setDrawer(Drawer<? super String> drawer) {
			this.drawer = drawer;
		}

	}
	
}
