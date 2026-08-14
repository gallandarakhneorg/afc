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

package org.arakhne.afc.math.geometry.d3.tests.afp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractPath3dPointCollectionTestCase<P extends Point3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>,
		B extends AlignedBox3afp<?, ?, P, V, Q, B>> extends AbstractMathTestCase {
	
	/** Is the shape to test.
	 */
	protected Path3afp<?, ?, P, V, Q, B> shape;
	
	/** Is the collection to test.
	 */
	protected Collection<P> collection;
	
	/** Shape factory.
	 */
	protected TestShapeFactory3d<P, V, Q, B> factory;

	protected abstract TestShapeFactory3d<P, V, Q, B> createFactory();

	@BeforeEach
	public void setUp() throws Exception {
		factory = createFactory();
		shape = factory.createPath();
		shape.moveTo(1, 1, 0);
		shape.lineTo(2, 2, 0);
		shape.quadTo(3, 0, 0, 4, 3, 0);
		shape.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
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
		assertEquals(coords.length/3, shape.size());
		for(int i=0, j=0; i<shape.size(); ++i) {
			Point3D p = shape.getPointAt(i);
			assertEpsilonEquals(coords[j++], p.getX());
			assertEpsilonEquals(coords[j++], p.getY());
			assertEpsilonEquals(coords[j++], p.getZ());
		}
	}
	
	private Point3D createPoint(double x, double y, double z) {
		return new InnerComputationPoint3D(x, y, z);
	}
	
	@DisplayName("size")
	@Nested
	public class Size {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	assertEquals(7, collection.size());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	shape.removeLast();
	    	assertEquals(7, collection.size());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	shape.removeLast();
	    	shape.removeLast();
	    	assertEquals(4, collection.size());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	shape.removeLast();
	    	shape.removeLast();
	    	shape.clear();
	    	assertEquals(0, collection.size());
	    }
	}

	@DisplayName("isEmpty")
	@Nested
	public class IsEmpty {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	assertFalse(collection.isEmpty());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	shape.removeLast();
	    	assertFalse(collection.isEmpty());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	shape.removeLast();
	    	shape.removeLast();
	    	assertFalse(collection.isEmpty());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	shape.removeLast();
	    	shape.removeLast();
	    	shape.clear();
	    	assertTrue(collection.isEmpty());
	    }
	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	assertFalse(collection.contains(new Object()));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	assertTrue(collection.contains(factory.createPoint(2, 2, 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	assertTrue(collection.contains(factory.createPoint(6, 5, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	assertFalse(collection.contains(factory.createPoint(-1, 6, 0)));
		}
    }

	@DisplayName("iterator")
	@Nested
	public class IteratorTest {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	Iterator<P> iterator = collection.iterator();
	    	assertTrue(iterator.hasNext());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	Iterator<P> iterator = collection.iterator();
	    	var p = iterator.next();
	    	assertEpsilonEquals(1, p.getX());
	    	assertEpsilonEquals(1, p.getY());
	    	assertEpsilonEquals(0, p.getZ());
	    	assertTrue(iterator.hasNext());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	Iterator<P> iterator = collection.iterator();
	    	var p = iterator.next();
	    	p = iterator.next();
	    	assertEpsilonEquals(2, p.getX());
	    	assertEpsilonEquals(2, p.getY());
	    	assertEpsilonEquals(0, p.getZ());
	    	assertTrue(iterator.hasNext());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	Iterator<P> iterator = collection.iterator();
	    	var p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	assertEpsilonEquals(3, p.getX());
	    	assertEpsilonEquals(3, p.getX());
	    	assertEpsilonEquals(0, p.getZ());
	    	assertTrue(iterator.hasNext());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	Iterator<P> iterator = collection.iterator();
	    	var p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	assertEpsilonEquals(4, p.getX());
	    	assertEpsilonEquals(3, p.getY());
	    	assertEpsilonEquals(0, p.getZ());
	    	assertTrue(iterator.hasNext());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	Iterator<P> iterator = collection.iterator();
	    	var p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	assertEpsilonEquals(5, p.getX());
	    	assertEpsilonEquals(-1, p.getY());
	    	assertEpsilonEquals(0, p.getZ());
	    	assertTrue(iterator.hasNext());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	Iterator<P> iterator = collection.iterator();
	    	var p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	assertEpsilonEquals(6, p.getX());
	    	assertEpsilonEquals(5, p.getY());
	    	assertEpsilonEquals(0, p.getZ());
	    	assertTrue(iterator.hasNext());
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	Iterator<P> iterator = collection.iterator();
	    	var p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	p = iterator.next();
	    	assertEpsilonEquals(7, p.getX());
	    	assertEpsilonEquals(-5, p.getY());
	    	assertEpsilonEquals(0, p.getZ());
	    	assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("toArray")
	@Nested
	public class ToArray {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	Object[] tab = collection.toArray();
	    	assertEquals(7, tab.length);
	    	assertEpsilonEquals(createPoint(1, 1, 0), (Point3D) tab[0]);
	    	assertEpsilonEquals(createPoint(2, 2, 0), (Point3D) tab[1]);
	    	assertEpsilonEquals(createPoint(3, 0, 0), (Point3D) tab[2]);
	    	assertEpsilonEquals(createPoint(4, 3, 0), (Point3D) tab[3]);
	    	assertEpsilonEquals(createPoint(5, -1, 0), (Point3D) tab[4]);
	    	assertEpsilonEquals(createPoint(6, 5, 0), (Point3D) tab[5]);
	    	assertEpsilonEquals(createPoint(7, -5, 0), (Point3D) tab[6]);
	    }

		@DisplayName("(Point3D[]) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void pointarray_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	Point3D[] tab = new Point3D[5];
	    	Point3D[] tab2 = collection.toArray(tab);
	    	assertSame(tab, tab2);
	    	assertEquals(5, tab.length);
	    	assertEpsilonEquals(1, tab[0].getX());
	    	assertEpsilonEquals(1, tab[0].getY());
	    	assertEpsilonEquals(0, tab[0].getZ());
	    	assertEpsilonEquals(2, tab[1].getX());
	    	assertEpsilonEquals(2, tab[1].getY());
	    	assertEpsilonEquals(0, tab[1].getZ());
	    	assertEpsilonEquals(3, tab[2].getX());
	    	assertEpsilonEquals(0, tab[2].getY());
	    	assertEpsilonEquals(0, tab[2].getZ());
	    	assertEpsilonEquals(4, tab[3].getX());
	    	assertEpsilonEquals(3, tab[3].getY());
	    	assertEpsilonEquals(0, tab[3].getZ());
	    	assertEpsilonEquals(5, tab[4].getX());
	    	assertEpsilonEquals(-1, tab[4].getY());
	    	assertEpsilonEquals(0, tab[4].getZ());
	    }
	}

	@DisplayName("add")
	@Nested
	public class Add {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	assertTrue(collection.add(factory.createPoint(123, 456, 0)));
	    	assertCoords(1, 1, 0, 2, 2, 0, 3, 0, 0, 4, 3, 0, 5, -1, 0, 6, 5, 0, 7, -5, 0, 123, 456, 0);
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	assertTrue(collection.add(factory.createPoint(123, 456, 0)));
	    	shape.clear();
	    	assertCoords();
	    	assertTrue(collection.add(factory.createPoint(123, 456, 0)));
	    	assertCoords(123, 456, 0);
	    	assertTrue(collection.add(factory.createPoint(789, 1011, 0)));
	    	assertCoords(123, 456, 0, 789, 1011, 0);
	    }
	}

	@DisplayName("remove")
	@Nested
	public class Remove {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	assertFalse(collection.remove(new Object()));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	assertTrue(collection.remove(factory.createPoint(2, 2, 0)));
	    	assertCoords(1, 1, 0, 3, 0, 0, 4, 3, 0, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	collection.remove(factory.createPoint(2, 2, 0));
	    	assertTrue(collection.remove(factory.createPoint(6, 5, 0)));
	    	assertCoords(1, 1, 0, 3, 0, 0, 4, 3, 0, 5, -1, 0, 7, -5, 0);
	    }
	}

	@DisplayName("containsAll")
	@Nested
	public class ContainsAll {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	assertTrue(collection.containsAll(
	    			Arrays.asList(factory.createPoint(1, 1, 0), factory.createPoint(6, 5, 0))));
	    }

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	assertFalse(collection.containsAll(
	    			Arrays.asList(factory.createPoint(1, 1, 0), factory.createPoint(6, 6, 0))));
	    }
	}

	@DisplayName("addAll")
	@Nested
	public class AllAll {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	collection.addAll(
	    			Arrays.asList(factory.createPoint(123, 456, 0), factory.createPoint(789, 1011, 0)));
	    	assertCoords(1, 1, 0, 2, 2, 0, 3, 0, 0, 4, 3, 0, 5, -1, 0, 6, 5, 0, 7, -5, 0, 123, 456, 0, 789, 1011, 0);
		}
    }

	@DisplayName("removeAll")
	@Nested
	public class RemoveAll {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	collection.removeAll(
	    			Arrays.asList(factory.createPoint(123, 456, 0), factory.createPoint(2, 2, 0)));
	    	assertCoords(1, 1, 0, 3, 0, 0, 4, 3, 0, 5, -1, 0, 6, 5, 0, 7, -5, 0);
	    }
	}

	@DisplayName("retainAll(Collection)")
	@Nested
	public class RetainAll {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(Throwable.class, () -> {
	    		collection.retainAll(Collections.emptyList());
			});
		}
    }

	@DisplayName("clear")
	@Nested
	public class Clear {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    	collection.clear();
	    	assertCoords();
	    }
	}

}
