/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.nodefx;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import org.arakhne.afc.nodefx.tests.ContDrawer1;
import org.arakhne.afc.nodefx.tests.MyDrawer1;
import org.arakhne.afc.nodefx.tests.MyDrawer2;
import org.arakhne.afc.nodefx.tests.MyDrawer3;
import org.arakhne.afc.nodefx.tests.MyDrawer4;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@SuppressWarnings("all")
public class DrawersTest {

	@Test
	public void getAllDrawers() {
		Iterator<Drawer<?>> iterator1 = Drawers.getAllDrawers();
		assertNotNull(iterator1);
		
		assertTrue(iterator1.hasNext());
		Drawer<?> d1 = iterator1.next();
		assertTrue(iterator1.hasNext());
		Drawer<?> d2 = iterator1.next();
		assertTrue(iterator1.hasNext());
		Drawer<?> d3 = iterator1.next();
		assertTrue(iterator1.hasNext());
		Drawer<?> d4 = iterator1.next();
		assertTrue(iterator1.hasNext());
		Drawer<?> d5 = iterator1.next();
		assertFalse(iterator1.hasNext());

		assertNotNull(d1);
		assertNotNull(d2);
		assertNotNull(d3);
		assertNotNull(d4);
		assertNotNull(d5);
		
		Set<Class<? extends Drawer<?>>> types = new HashSet<>();
		types.add(MyDrawer1.class);
		types.add(MyDrawer2.class);
		types.add(MyDrawer3.class);
		types.add(MyDrawer4.class);
		types.add(ContDrawer1.class);
		
		assertTrue(types.remove(d1.getClass()));
		assertTrue(types.remove(d2.getClass()));
		assertTrue(types.remove(d3.getClass()));
		assertTrue(types.remove(d4.getClass()));
		assertTrue(types.remove(d5.getClass()));
	}

	@Test
	public void getDrawerForClass_01() {
		Drawer<String> d1 = Drawers.getDrawerFor(String.class);
		assertNotNull(d1);
		assertTrue(d1 instanceof MyDrawer1);
	}

	@Test
	public void getDrawerForClass_02() {
		Drawer<? extends Number> d1 = Drawers.getDrawerFor(Integer.class);
		assertNotNull(d1);
		assertTrue(d1 instanceof MyDrawer2);
	}

	@Test
	public void getDrawerForClass_03() {
		Drawer<? extends Number> d1 = Drawers.getDrawerFor(Double.class);
		assertNotNull(d1);
		assertTrue(d1 instanceof MyDrawer3);
	}

	@Test
	public void getDrawerForClass_04() {
		Drawer d1 = Drawers.getDrawerFor(Number.class);
		assertNull(d1);
	}

	@Test
	public void getDrawerForObject_01() {
		Drawer<? super String> d1 = Drawers.getDrawerFor(""); //$NON-NLS-1$
		assertNotNull(d1);
		assertTrue(d1 instanceof MyDrawer1);
	}

	@Test
	public void getDrawerForObject_02() {
		Drawer<? super Integer> d1 = Drawers.getDrawerFor(1);
		assertNotNull(d1);
		assertTrue(d1 instanceof MyDrawer2);
	}

	@Test
	public void getDrawerForObject_03() {
		Drawer<? super Double> d1 = Drawers.getDrawerFor(1d);
		assertNotNull(d1);
		assertTrue(d1 instanceof MyDrawer3);
	}

	@Test
	public void getDrawerForObject_04() {
		Drawer d1 = Drawers.getDrawerFor(1f);
		assertNull(d1);
	}

	@Test
	public void getDrawerForObject_05() {
		DrawableObject obj = new DrawableObject();
		Drawer d1 = Drawers.getDrawerFor(obj);
		assertNull(d1);

		obj.setDrawer(new MyDrawer1());
		d1 = Drawers.getDrawerFor(obj);
		assertNotNull(d1);
		assertTrue(d1 instanceof MyDrawer1);
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
