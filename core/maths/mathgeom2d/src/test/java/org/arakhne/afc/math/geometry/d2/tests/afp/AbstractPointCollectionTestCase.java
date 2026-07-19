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

package org.arakhne.afc.math.geometry.d2.tests.afp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public abstract class AbstractPointCollectionTestCase<P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2afp<?, ?, ?, P, V, B>> extends AbstractMathTestCase {
	
	/** Is the shape to test.
	 */
	protected Path2afp<?, ?, ?, P, V, B> shape;
	
	/** Is the collection to test.
	 */
	protected Collection<P> collection;
	
	/** Shape factory.
	 */
	protected TestShapeFactory<P, V, B> factory;

	protected abstract TestShapeFactory<P, V, B> createFactory();

	@BeforeEach
	public void setUp() throws Exception {
		factory = createFactory();
		shape = factory.createPath(PathWindingRule.EVEN_ODD);
		shape.moveTo(1, 1);
		shape.lineTo(2, 2);
		shape.quadTo(3, 0, 4, 3);
		shape.curveTo(5, -1, 6, 5, 7, -5);
		shape.closePath();
		collection = shape.toCollection();
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		shape = null;
		collection = null;
		factory = null;
	}
	
	private void assertCoords(double... coords) {
		assertEquals(coords.length/2, shape.size());
		for(int i=0, j=0; i<shape.size(); ++i) {
			Point2D p = shape.getPointAt(i);
			assertEpsilonEquals(coords[j++], p.getX());
			assertEpsilonEquals(coords[j++], p.getY());
		}
	}
	
	@DisplayName("size")
	@Nested
	public class Size {

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	assertEquals(7, collection.size());
		}

		@DisplayName("#2")
		@Test
	    public void test_2() {
	    	shape.removeLast();
	    	assertEquals(7, collection.size());
		}

		@DisplayName("#3")
		@Test
	    public void test_3() {
	    	shape.removeLast();
	    	shape.removeLast();
	    	assertEquals(4, collection.size());
		}

		@DisplayName("#4")
		@Test
	    public void test_4() {
	    	shape.clear();
	    	assertEquals(0, collection.size());
	    }

	}

	@DisplayName("isEmpty")
	@Nested
	public class IsEmpty {

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	assertFalse(collection.isEmpty());
		}

		@DisplayName("#2")
		@Test
	    public void test_2() {
	    	shape.removeLast();
	    	assertFalse(collection.isEmpty());
		}

		@DisplayName("#3")
		@Test
	    public void test_3() {
	    	shape.removeLast();
	    	shape.removeLast();
	    	assertFalse(collection.isEmpty());
		}

		@DisplayName("#4")
		@Test
	    public void test_4() {
	    	shape.clear();
	    	assertTrue(collection.isEmpty());
		}

    }

	@DisplayName("contains(Object)")
	@Nested
	public class ContainsObject {

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	assertFalse(collection.contains(new Object()));
		}

		@DisplayName("#2")
		@Test
	    public void test_2() {
	    	assertTrue(collection.contains(factory.createPoint(2, 2)));
		}

		@DisplayName("#3")
		@Test
	    public void test_3() {
	    	assertTrue(collection.contains(factory.createPoint(6, 5)));
		}

		@DisplayName("#4")
		@Test
	    public void test_4() {
	    	assertFalse(collection.contains(factory.createPoint(-1, 6)));
	    }

	}

	@DisplayName("iterator")
	@Nested
	public class Iterator {

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	var iterator = collection.iterator();
	    	assertTrue(iterator.hasNext());
		}

		@DisplayName("#2")
		@Test
	    public void test_2() {
	    	var iterator = collection.iterator();
	    	var p = iterator.next();
	    	assertEpsilonEquals(1, p.getX());
	    	assertEpsilonEquals(1, p.getY());
	    	assertTrue(iterator.hasNext());
		}

		@DisplayName("#3")
		@Test
	    public void test_3() {
	    	var iterator = collection.iterator();
	    	var p = iterator.next();
	    	p = iterator.next();
	    	assertEpsilonEquals(2, p.getX());
	    	assertEpsilonEquals(2, p.getY());
	    	assertTrue(iterator.hasNext());
		}

		@DisplayName("#4")
		@Test
	    public void test_4() {
	    	var iterator = collection.iterator();
	    	var p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	assertEpsilonEquals(3, p.getX());
	    	assertEpsilonEquals(0, p.getY());
	    	assertTrue(iterator.hasNext());
		}

		@DisplayName("#5")
		@Test
	    public void test_5() {
	    	var iterator = collection.iterator();
	    	var p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	assertEpsilonEquals(4, p.getX());
	    	assertEpsilonEquals(3, p.getY());
	    	assertTrue(iterator.hasNext());
		}

		@DisplayName("#6")
		@Test
	    public void test_6() {
	    	var iterator = collection.iterator();
	    	var p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	assertEpsilonEquals(5, p.getX());
	    	assertEpsilonEquals(-1, p.getY());
	    	assertTrue(iterator.hasNext());
		}

		@DisplayName("#7")
		@Test
	    public void test_7() {
	    	var iterator = collection.iterator();
	    	var p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	assertEpsilonEquals(6, p.getX());
	    	assertEpsilonEquals(5, p.getY());
	    	assertTrue(iterator.hasNext());
		}

		@DisplayName("#8")
		@Test
	    public void test_8() {
	    	var iterator = collection.iterator();
	    	var p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	assertEpsilonEquals(7, p.getX());
	    	assertEpsilonEquals(-5, p.getY());
	    	assertFalse(iterator.hasNext());
	    }

	}

	@DisplayName("toArray")
	@Nested
	public class ToArray {

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	Object[] tab = collection.toArray();
	    	assertEquals(7, tab.length);
	    	assertTrue(tab[0] instanceof Point2D);
	    	assertEpsilonEquals(1, ((Point2D) tab[0]).getX());
	    	assertEpsilonEquals(1, ((Point2D) tab[0]).getY());
	    	assertTrue(tab[1] instanceof Point2D);
	    	assertEpsilonEquals(2, ((Point2D) tab[1]).getX());
	    	assertEpsilonEquals(2, ((Point2D) tab[1]).getY());
	    	assertTrue(tab[2] instanceof Point2D);
	    	assertEpsilonEquals(3, ((Point2D) tab[2]).getX());
	    	assertEpsilonEquals(0, ((Point2D) tab[2]).getY());
	    	assertTrue(tab[3] instanceof Point2D);
	    	assertEpsilonEquals(4, ((Point2D) tab[3]).getX());
	    	assertEpsilonEquals(3, ((Point2D) tab[3]).getY());
	    	assertTrue(tab[4] instanceof Point2D);
	    	assertEpsilonEquals(5, ((Point2D) tab[4]).getX());
	    	assertEpsilonEquals(-1, ((Point2D) tab[4]).getY());
	    	assertTrue(tab[5] instanceof Point2D);
	    	assertEpsilonEquals(6, ((Point2D) tab[5]).getX());
	    	assertEpsilonEquals(5, ((Point2D) tab[5]).getY());
	    	assertTrue(tab[6] instanceof Point2D);
	    	assertEpsilonEquals(7, ((Point2D) tab[6]).getX());
	    	assertEpsilonEquals(-5, ((Point2D) tab[6]).getY());
	    }

	}

	@DisplayName("toArray(Object[])")
	@Nested
	public class ToArrayArray {

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	Point2D[] tab = new Point2D[5];
	    	Point2D[] tab2 = collection.toArray(tab);
	    	assertSame(tab, tab2);
	    	assertEquals(5, tab.length);
	    	assertEpsilonEquals(1, tab[0].getX());
	    	assertEpsilonEquals(1, tab[0].getY());
	    	assertEpsilonEquals(2, tab[1].getX());
	    	assertEpsilonEquals(2, tab[1].getY());
	    	assertEpsilonEquals(3, tab[2].getX());
	    	assertEpsilonEquals(0, tab[2].getY());
	    	assertEpsilonEquals(4, tab[3].getX());
	    	assertEpsilonEquals(3, tab[3].getY());
	    	assertEpsilonEquals(5, tab[4].getX());
	    	assertEpsilonEquals(-1, tab[4].getY());
	    }

	}

	@DisplayName("add(T)")
	@Nested
	public class AddT {

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	assertTrue(collection.add(factory.createPoint(123, 456)));
	    	assertCoords(1, 1, 2, 2, 3, 0, 4, 3, 5, -1, 6, 5, 7, -5, 123, 456);
		}

		@DisplayName("#2")
		@Test
	    public void test_2() {
			collection.add(factory.createPoint(123, 456));
	    	shape.clear();
	    	assertCoords();
		}

		@DisplayName("#3")
		@Test
	    public void test_3() {
	    	shape.clear();
	    	assertTrue(collection.add(factory.createPoint(123, 456)));
	    	assertCoords(123, 456);
		}

		@DisplayName("#4")
		@Test
	    public void test_4() {
	    	shape.clear();
	    	assertTrue(collection.add(factory.createPoint(123, 456)));
	    	assertTrue(collection.add(factory.createPoint(789, 1011)));
	    	assertCoords(123, 456, 789, 1011);
	    }

	}

	@DisplayName("remove(Object)")
	@Nested
	public class RemoveObject {

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	assertFalse(collection.remove(new Object()));
		}

		@DisplayName("#2")
		@Test
	    public void test_2() {
	    	assertTrue(collection.remove(factory.createPoint(2, 2)));
	    	assertCoords(1, 1, 3, 0, 4, 3, 5, -1, 6, 5, 7, -5);
		}

		@DisplayName("#3")
		@Test
	    public void test_3() {
			collection.remove(factory.createPoint(2, 2));
			assertTrue(collection.remove(factory.createPoint(6, 5)));
	    	assertCoords(1, 1, 3, 0, 4, 3);
	    }

	}

	@DisplayName("containsAll")
	@Nested
	public class ContainsAll {

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	assertTrue(collection.containsAll(
	    			Arrays.asList(factory.createPoint(1, 1), factory.createPoint(6, 5))));
		}

		@DisplayName("#2")
		@Test
	    public void test_2() {
	    	assertFalse(collection.containsAll(
	    			Arrays.asList(factory.createPoint(1, 1), factory.createPoint(6, 6))));
		}

    }

	@DisplayName("addAll")
	@Nested
	public class AddAll {

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	collection.addAll(
	    			Arrays.asList(factory.createPoint(123, 456), factory.createPoint(789, 1011)));
	    	assertCoords(1, 1, 2, 2, 3, 0, 4, 3, 5, -1, 6, 5, 7, -5, 123, 456, 789, 1011);
	    }

	}

	@DisplayName("removeAll")
	@Nested
	public class RemoveAll {

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	collection.removeAll(
	    			Arrays.asList(factory.createPoint(123, 456), factory.createPoint(2, 2)));
	    	assertCoords(1, 1, 3, 0, 4, 3, 5, -1, 6, 5, 7, -5);
	    }

	}

	@DisplayName("retainAll")
	@Nested
	public class RetainAll {

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	try {
	    		collection.retainAll(Collections.emptyList());
	    		fail("Expecting an exception");  //$NON-NLS-1$
	    	}
	    	catch(Throwable e1) {
	    		e1.equals(e1);
	    		// Expecting an exception
	    	}
		}

	}

	@DisplayName("clear")
	@Nested
	public class Clear {

		@DisplayName("#1")
		@Test
	    public void test_1() {
	    	collection.clear();
	    	assertCoords();
		}

	}
    
}