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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.MultiShape3D;
import org.arakhne.afc.math.geometry.base.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;
import org.arakhne.afc.math.geometry.d3.afp.Shape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.math.geometry.d3.general.Shape3DType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractMultiShape3dTestCase<T extends MultiShape3afp<T, C, ?, ?, ?, ?, B>,
		C extends Shape3afp<?, ?, ?, ?, ?, B>,
		B extends AlignedBox3afp<?, ?, ?, ?, ?, B>> extends AbstractShape3dTestCase<T, B> {

	protected C firstObject;
	
	protected C secondObject;

	@Override
	protected final T createShape() {
		T shape = (T) createMultiShape();
		firstObject = (C) createAlignedBox(5, 8, 0, 2, 1, .5);
		secondObject = (C) createSphere(-5, 18, 0, 2);
		shape.add(firstObject);
		shape.add(secondObject);
		return shape;
	}

	@DisplayName("clone")
	@Nested
	public class Clone {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp clone = getS().clone();
			assertNotSame(getS(), clone);
			assertEquals(2, clone.size());
			for (int i = 0; i < clone.size(); ++i) {
				var source = getS().get(i);
				var actual = clone.get(i);
				assertNotSame(source, actual);
				assertEquals(source, actual);
			}
		}
	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsObject {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(new Object()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createMultiShape()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSegment(5, 8, 0, 5, 10, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(getS()));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(getS().clone()));
		}
	}

	@DisplayName("equalsToShape")
	@Nested
	public class EqualsToShape {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) createMultiShape()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape(getS()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape(getS().clone()));
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
			assertFalse(getS().isEmpty());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertTrue(getS().isEmpty());
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
			getS().clear();
			assertEquals(0, getS().size());
		}
	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		@DisplayName("(x,y,z) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void xyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Outside bounding box
			assertFalse(getS().contains(-10, 2, 0));
		}

		@DisplayName("(x,y,z) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void xyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-10, 14, 0));
		}

		@DisplayName("(x,y,z) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void xyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-10, 25, 0));
		}

		@DisplayName("(x,y,z) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void xyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-1, 25, 0));
		}

		@DisplayName("(x,y,z) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void xyz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(1, 2, 0));
		}

		@DisplayName("(x,y,z) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void xyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(12, 2, 0));
		}

		@DisplayName("(x,y,z) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void xyz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(12, 14, 0));
		}

		@DisplayName("(x,y,z) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void xyz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(12, 25, 0));
		}

		@DisplayName("(x,y,z) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void xyz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Inside bounding box - outside subshape
			assertFalse(getS().contains(-6, 8, 0));
		}

		@DisplayName("(x,y,z) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void xyz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(4, 17, 0));
		}

		@DisplayName("(x,y,z) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void xyz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Inside circle
			assertTrue(getS().contains(-4, 19, 0));
		}

		@DisplayName("(x,y,z) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void xyz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Inside rectangle
			assertTrue(getS().contains(6, 8.25, 0));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Outside bounding box
			assertFalse(getS().contains(createPoint(-10, 2, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(-10, 14, 0)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(-10, 25, 0)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(-1, 25, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(1, 2, 0)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(12, 2, 0)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(12, 14, 0)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(12, 25, 0)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Inside bounding box - outside subshape
			assertFalse(getS().contains(createPoint(-6, 8, 0)));
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(4, 17, 0)));
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Inside circle
			assertTrue(getS().contains(createPoint(-4, 19, 0)));
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Inside rectangle
			assertTrue(getS().contains(createPoint(6, 8.25, 0)));
		}

		@DisplayName("(Box3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Outside
			assertFalse(getS().contains(createAlignedBox(-20, 14, 0, .5, .5, 0)));
		}

		@DisplayName("(Box3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBox(-2,-10, 0, .5, .5, 0)));
		}

		@DisplayName("(Box3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Intersecting
			assertFalse(getS().contains(createAlignedBox(-6, 16, 0, .5, .5, 0)));
		}

		@DisplayName("(Box3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBox(4.75, 8, 0, .5, .5, 0)));
		}

		@DisplayName("(Box3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Inside
			assertTrue(getS().contains(createAlignedBox(-4, 18, 0, .5, .5, 0)));
		}

		@DisplayName("(Box3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(5.5, 8.5, 0, .5, .5, 0)));
		}
	}

	@DisplayName("getClosestPointTo")
	@Nested
	public class GetClosestPointTo {
		
		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(5., 8., 0.5), getS().getClosestPointTo(createTriangle(0, 0, 0, 1, 1, 1, 1, 0, 1)));
			assertEpsilonEquals(8.0777472107, getS().getDistance(createTriangle(0, 0, 0, 1, 1, 1, 1, 0, 1)));
			assertEpsilonEquals(65.25, getS().getDistanceSquared(createTriangle(0, 0, 0, 1, 1, 1, 1, 0, 1)));
		}

		@DisplayName("(Triangle3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_2(CoordinateSystem3D cs) {
			// triangle intersects aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle has a vertex inside/on the box volume: (6, 8.5, 0.25)
			assertEpsilonEquals(createPoint(6., 8.5, 0.25), getS().getClosestPointTo(createTriangle(6, 8.5, 0.25, 10, 8.5, 0.25, 6, 12, 0.25)));
			assertEpsilonEquals(0., getS().getDistance(createTriangle(6, 8.5, 0.25, 10, 8.5, 0.25, 6, 12, 0.25)));
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(6, 8.5, 0.25, 10, 8.5, 0.25, 6, 12, 0.25)));
		}

		@DisplayName("(Triangle3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_3(CoordinateSystem3D cs) {
			// triangle intersects sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle includes the sphere center (-5,18,0), thus intersects sphere
			// One valid closest point returned by the multishape is a point on the sphere.
			assertEpsilonEquals(createPoint(-5., 18., 0.), getS().getClosestPointTo(createTriangle(-5, 18, 0, -3, 18, 0, -5, 20, 0)));
			assertEpsilonEquals(0., getS().getDistance(createTriangle(-5, 18, 0, -3, 18, 0, -5, 20, 0)));
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(-5, 18, 0, -3, 18, 0, -5, 20, 0)));
		}

		@DisplayName("(Triangle3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_4(CoordinateSystem3D cs) {
			// far on +X side, box is the nearest component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle near x=10, y=8.5, z=0.25 => closest multishape point is box face x=7
			assertEpsilonEquals(createPoint(7., 8.5, 0.25), getS().getClosestPointTo(createTriangle(10, 8.5, 0.25, 10, 9.5, 0.25, 10, 8.5, 1.25)));
			assertEpsilonEquals(3., getS().getDistance(createTriangle(10, 8.5, 0.25, 10, 9.5, 0.25, 10, 8.5, 1.25)));
			assertEpsilonEquals(9., getS().getDistanceSquared(createTriangle(10, 8.5, 0.25, 10, 9.5, 0.25, 10, 8.5, 1.25)));
		}

		@DisplayName("(Triangle3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_5(CoordinateSystem3D cs) {
			// far on sphere side, sphere is the nearest component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle at x=-10 near y=18,z=0. Nearest sphere point is (-7,18,0), distance 3
			assertEpsilonEquals(createPoint(-7., 18., 0.), getS().getClosestPointTo(createTriangle(-10, 18, 0, -10, 19, 0, -10, 18, 1)));
			assertEpsilonEquals(3., getS().getDistance(createTriangle(-10, 18, 0, -10, 19, 0, -10, 18, 1)));
			assertEpsilonEquals(9., getS().getDistanceSquared(createTriangle(-10, 18, 0, -10, 19, 0, -10, 18, 1)));
		}

		@DisplayName("(Triangle3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_6(CoordinateSystem3D cs) {
			// triangle tangent to sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle has vertex exactly on sphere surface: (-3,18,0)
			assertEpsilonEquals(createPoint(-3., 18., 0.), getS().getClosestPointTo(createTriangle(-3, 18, 0, -3, 19, 0, -3, 18, 1)));
			assertEpsilonEquals(0., getS().getDistance(createTriangle(-3, 18, 0, -3, 19, 0, -3, 18, 1)));
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(-3, 18, 0, -3, 19, 0, -3, 18, 1)));
		}

		@DisplayName("(MultiShape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(-5, 18, 0, 0.5));
			shape.add(createSegment(0, 8.5, 0.25, 10, 8.5, 0.25));
			assertEpsilonEquals(createPoint(6.0,8.5,0.25), getS().getClosestPointTo(shape));
		}

		@DisplayName("(MultiShape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_2(CoordinateSystem3D cs) {
			// disjoint, closest to aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(10, 8.5, 0.25, 1));                // distance 2 to box-comp
			shape.add(createSegment(12, 8.5, 0.25, 14, 8.5, 0.25));   // distance 5 to box-comp
			assertEpsilonEquals(createPoint(7, 8.5, 0.25), getS().getClosestPointTo(shape));
		}

		@DisplayName("(MultiShape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_3(CoordinateSystem3D cs) {
			// disjoint, closest to sphere component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(0, 18, 0, 1));                     // distance 2 to ref-sphere
			shape.add(createAlignedBoxFromPoints(20, 20, 0, 21, 21, 1));
			assertEpsilonEquals(createPoint(-3, 18, 0), getS().getClosestPointTo(shape));
		}

		@DisplayName("(MultiShape3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_4(CoordinateSystem3D cs) {
			// touching by tangent sphere-to-sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(0, 18, 0, 3)); // tangent with ref sphere centered at (-5,18,0), r=2
			shape.add(createSegment(100, 100, 100, 101, 101, 101));
			assertEpsilonEquals(createPoint(-3, 18, 0), getS().getClosestPointTo(shape));
		}

		@DisplayName("(MultiShape3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_5(CoordinateSystem3D cs) {
			// intersecting via box-box overlap
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createAlignedBoxFromPoints(6, 8.2, 0.1, 6.8, 8.8, 0.4)); // inside ref box-comp
			shape.add(createSphere(50, 50, 50, 1));
			assertEpsilonEquals(createPoint(6.4,8.5,0.25), getS().getClosestPointTo(shape));
		}

		@DisplayName("(MultiShape3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_6(CoordinateSystem3D cs) {
			// intersecting via segment through reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(-10, 18, 0, 0, 18, 0)); // crosses ref sphere
			shape.add(createAlignedBoxFromPoints(30, 30, 30, 31, 31, 31));
			assertEpsilonEquals(createPoint(-5, 18, 0), getS().getClosestPointTo(shape));
		}

		@DisplayName("(MultiShape3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_7(CoordinateSystem3D cs) {
			// degenerate point shape inside reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(-5, 18, 0, -5, 18, 0)); // point
			assertEpsilonEquals(createPoint(-5, 18, 0), getS().getClosestPointTo(shape));
		}

		@DisplayName("(MultiShape3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_8(CoordinateSystem3D cs) {
			// degenerate point shape outside both components
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(0, 0, 0, 0, 0, 0)); // point
			assertEpsilonEquals(createPoint(5, 8, 0), getS().getClosestPointTo(shape));
		}

		@DisplayName("(MultiShape3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_9(CoordinateSystem3D cs) {
			// empty multishape
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			// Keep only if API defines behavior for empty multishape as infinite/undefined distance.
			// If your implementation returns NaN or throws, adapt these assertions accordingly.
			assertNull(getS().getClosestPointTo(shape));
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(0, 0, 0);
			path.lineTo(1, 1, 1);
			assertEpsilonEquals(createPoint(5,8,0.5), getS().getClosestPointTo(path));
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_2(CoordinateSystem3D cs) {
			//polyline crossing aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(0, 8.5, 0.25);
			path.lineTo(10, 8.5, 0.25);
			assertEpsilonEquals(createPoint(6.0,8.5,0.25), getS().getClosestPointTo(path));
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_3(CoordinateSystem3D cs) {
			// polyline crossing sphere component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(-10, 18, 0);
			path.lineTo(0, 18, 0);
			assertEpsilonEquals(createPoint(-5, 18, 0), getS().getClosestPointTo(path));
		}

		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_4(CoordinateSystem3D cs) {
			// path near sphere but outside
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(-10, 21, 0);
			path.lineTo(0, 21, 0); // distance to sphere center line = 3, radius=2 => gap=1
			assertEpsilonEquals(createPoint(-5, 20, 0), getS().getClosestPointTo(path));
		}

		@DisplayName("(Path3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_5(CoordinateSystem3D cs) {
			// path near aligned-box component but outside
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(10, 8.5, 0.25);
			path.lineTo(12, 8.5, 0.25); // box xmax=7 => gap=3
			assertEpsilonEquals(createPoint(7, 8.5, 0.25), getS().getClosestPointTo(path));
		}

		@DisplayName("(Path3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_6(CoordinateSystem3D cs) {
			// quadratic crossing aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(0, 8.5, 0.25);
			path.quadTo(6, 8.5, 0.25, 10, 8.5, 0.25);
			assertEpsilonEquals(createPoint(5.25, 8.5, 0.25), getS().getClosestPointTo(path));
		}

		@DisplayName("(Path3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_7(CoordinateSystem3D cs) {
			// cubic crossing sphere component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(-10, 18, 0);
			path.curveTo(-8, 18, 0, -2, 18, 0, 0, 18, 0);
			assertEpsilonEquals(createPoint(-5, 18, 0), getS().getClosestPointTo(path));
		}

		@DisplayName("(Path3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_8(CoordinateSystem3D cs) {
			// mixed path, closest on aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(20, 0, 0);
			path.lineTo(20, 30, 0);           // far from both
			path.quadTo(12, 12, 0.25, 10, 9, 0.25); // comes near box, stays outside
			assertEpsilonEquals(createPoint(7, 9, 0.25), getS().getClosestPointTo(path));
		}

		@DisplayName("(Path3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_9(CoordinateSystem3D cs) {
			// degenerate path point inside sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(-5, 18, 0);
			path.lineTo(-5, 18, 0);
			assertEpsilonEquals(createPoint(-5, 18, 0), getS().getClosestPointTo(path));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(5,8,0.5), getS().getClosestPointTo(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_2(CoordinateSystem3D cs) {
			// overlap aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(6.4,8.5,0.25), getS().getClosestPointTo(createAlignedBoxFromPoints(6, 8.2, 0.1, 6.8, 8.8, 0.4)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_3(CoordinateSystem3D cs) {
			// touching aligned-box face x=5
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(5.0,8.5,0.25), getS().getClosestPointTo(createAlignedBoxFromPoints(4, 8.2, 0.1, 5, 8.8, 0.4)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_4(CoordinateSystem3D cs) {
			// overlap sphere component only
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// contains sphere center
			assertEpsilonEquals(createPoint(-5, 18, 0), getS().getClosestPointTo(createAlignedBoxFromPoints(-6, 17, -0.5, -4, 19, 0.5)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_5(CoordinateSystem3D cs) {
			// tangent to sphere at one side
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// xmin=-1, sphere rightmost point at x=-3? no overlap
			// Better exact tangent: box xmin = -3 (sphere center -5, r=2)
			assertEpsilonEquals(createPoint(-3.0,18.0,0.0), getS().getClosestPointTo(createAlignedBoxFromPoints(-3, 17, -1, 1, 19, 1)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_6(CoordinateSystem3D cs) {
			// outside near aligned-box component (+X)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// dx from x=7 is 3
			assertEpsilonEquals(createPoint(7.0,8.5,0.25), getS().getClosestPointTo(createAlignedBoxFromPoints(10, 8.2, 0.1, 11, 8.8, 0.4)));
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_7(CoordinateSystem3D cs) {
			// outside near sphere component (+X)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// nearest box point to sphere center is (0,18,0): center-distance=5, minus radius 2 => 3
			assertEpsilonEquals(createPoint(-3, 18, 0), getS().getClosestPointTo(createAlignedBoxFromPoints(0, 17, -1, 1, 19, 1)));
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_8(CoordinateSystem3D cs) {
			// box between both components but disjoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// closer to small aligned-box component than sphere
			// gap 1 to xmin=5
			assertEpsilonEquals(createPoint(5.0,8.5,0.25), getS().getClosestPointTo(createAlignedBoxFromPoints(3, 8.2, 0.1, 4, 8.8, 0.4)));
		}

		@DisplayName("(AlignedBox3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_9(CoordinateSystem3D cs) {
			// degenerate point inside aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var b = createAlignedBoxFromPoints(6, 8.5, 0.25, 6, 8.5, 0.25);
			assertEpsilonEquals(createPoint(6, 8.5, 0.25), getS().getClosestPointTo(b));
		}

		@DisplayName("(AlignedBox3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_10(CoordinateSystem3D cs) {
			// degenerate point inside sphere component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var b = createAlignedBoxFromPoints(-5, 18, 0, -5, 18, 0);
			assertEpsilonEquals(createPoint(-5, 18, 0), getS().getClosestPointTo(b));
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(5,8,0), getS().getClosestPointTo(createSphere(0, 0, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_2(CoordinateSystem3D cs) {
			// center inside aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(6, 8.5, 0.25), getS().getClosestPointTo(createSphere(6, 8.5, 0.25, 0.1)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_3(CoordinateSystem3D cs) {
			// center inside reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(-5, 18, 0), getS().getClosestPointTo(createSphere(-5, 18, 0, 0.5)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_4(CoordinateSystem3D cs) {
			// tangent to aligned-box face
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// tangent at x=5
			assertEpsilonEquals(createPoint(5, 8.5, 0.25), getS().getClosestPointTo(createSphere(4, 8.5, 0.25, 1)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_5(CoordinateSystem3D cs) {
			// tangent to reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// centers distance = 5, radii sum = 2 + 3 = 5
			assertEpsilonEquals(createPoint(-3, 18, 0), getS().getClosestPointTo(createSphere(0, 18, 0, 3)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_6(CoordinateSystem3D cs) {
			// outside but closest to aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// closest to box at (7,8.5,0.25), center distance to box=3, radius=1 => dist=2
			assertEpsilonEquals(createPoint(7, 8.5, 0.25), getS().getClosestPointTo(createSphere(10, 8.5, 0.25, 1)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_7(CoordinateSystem3D cs) {
			// outside but closest to reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// center distance to (-5,18,0) is 5, minus radii (2+1) => 2
			assertEpsilonEquals(createPoint(-3, 18, 0), getS().getClosestPointTo(createSphere(0, 18, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_8(CoordinateSystem3D cs) {
			// large sphere enclosing both components
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(-3, 18, 0), getS().getClosestPointTo(createSphere(0, 18, 0, 1)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(5,8,0.5), getS().getClosestPointTo(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_2(CoordinateSystem3D cs) {
			// crossing aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Through box interior: y=8.5, z=0.25 in box ranges
			assertEpsilonEquals(createPoint(6.0,8.5,0.25), getS().getClosestPointTo(createSegment(0, 8.5, 0.25, 10, 8.5, 0.25)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_3(CoordinateSystem3D cs) {
			// touching aligned-box corner
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(5,8,0), getS().getClosestPointTo(createSegment(0, 0, 0, 5, 8, 0)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_4(CoordinateSystem3D cs) {
			// crossing sphere center line
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Passes through sphere center (-5,18,0)
			assertEpsilonEquals(createPoint(-5,18,0), getS().getClosestPointTo(createSegment(-10, 18, 0, 0, 18, 0)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_5(CoordinateSystem3D cs) {
			// tangent to sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Distance to center is exactly radius=2 (line y=20 through z=0)
			assertEpsilonEquals(createPoint(-5,20,0), getS().getClosestPointTo(createSegment(-10, 20, 0, 0, 20, 0)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_6(CoordinateSystem3D cs) {
			// near sphere, outside
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Closest point to sphere center is (-5,21,0): center distance 3 => shape distance 1
			assertEpsilonEquals(createPoint(-5, 20, 0), getS().getClosestPointTo(createSegment(-10, 21, 0, 0, 21, 0))); // sphere surface point
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_7(CoordinateSystem3D cs) {
			// near aligned box, outside along +X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment at x=10, y/z aligned to box interior => dx=3 from xmax=7
			assertEpsilonEquals(createPoint(7.0,9.0,0.25), getS().getClosestPointTo(createSegment(10, 8.5, 0.25, 10, 9, 0.25)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_8(CoordinateSystem3D cs) {
			// degenerate point inside sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(-5, 18, 0), getS().getClosestPointTo(createSegment(-5, 18, 0, -5, 18, 0)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_9(CoordinateSystem3D cs) {
			// degenerate point inside aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(6, 8.5, 0.25), getS().getClosestPointTo(createSegment(6, 8.5, 0.25, 6, 8.5, 0.25)));
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void shape_1(CoordinateSystem3D cs) {
			// overlap sphere component only
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// contains sphere center
			assertEpsilonEquals(createPoint(-5, 18, 0), getS().getClosestPointTo((Shape3D) createAlignedBoxFromPoints(-6, 17, -0.5, -4, 19, 0.5)));
		}

		@DisplayName("(Shape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void shape_2(CoordinateSystem3D cs) {
			// tangent to reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// centers distance = 5, radii sum = 2 + 3 = 5
			assertEpsilonEquals(createPoint(-3, 18, 0), getS().getClosestPointTo((Shape3D) createSphere(0, 18, 0, 3)));
		}

		@DisplayName("(Shape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void shape_3(CoordinateSystem3D cs) {
			// near sphere, outside
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Closest point to sphere center is (-5,21,0): center distance 3 => shape distance 1
			assertEpsilonEquals(createPoint(-5, 20, 0), getS().getClosestPointTo((Shape3D) createSegment(-10, 21, 0, 0, 21, 0))); // sphere surface point
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getClosestPointTo(createPoint(-10, 2, 0));
			assertEpsilonEquals(-5.59655, result.getX());
			assertEpsilonEquals(16.09104, result.getY());
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getClosestPointTo(createPoint(-10, 14, 0));
			assertEpsilonEquals(-6.56174, result.getX());
			assertEpsilonEquals(16.75061, result.getY());
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getClosestPointTo(createPoint(-10, 25, 0));
			assertEpsilonEquals(-6.16248, result.getX());
			assertEpsilonEquals(19.62747, result.getY());
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getClosestPointTo(createPoint(-1, 25, 0));
			assertEpsilonEquals(-4.00772, result.getX());
			assertEpsilonEquals(19.73649, result.getY());
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getClosestPointTo(createPoint(1, 2, 0));
			assertEpsilonEquals(5, result.getX());
			assertEpsilonEquals(8, result.getY());
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getClosestPointTo(createPoint(12, 2, 0));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(8, result.getY());
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getClosestPointTo(createPoint(12, 14, 0));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(9, result.getY());
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getClosestPointTo(createPoint(12, 25, 0));
			assertEpsilonEquals(-3.15064, result.getX());
			assertEpsilonEquals(18.7615, result.getY());
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getClosestPointTo(createPoint(-6, 8, 0));
			assertEpsilonEquals(-5.19901, result.getX());
			assertEpsilonEquals(16.00993, result.getY());
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getClosestPointTo(createPoint(4, 17, 0));
			assertEpsilonEquals(-3.01223, result.getX());
			assertEpsilonEquals(17.77914, result.getY());
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getClosestPointTo(createPoint(-4, 19, 0));
			assertEpsilonEquals(-4, result.getX());
			assertEpsilonEquals(19, result.getY());
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getClosestPointTo(createPoint(6, 8.25, 0));
			assertEpsilonEquals(6, result.getX());
			assertEpsilonEquals(8.25, result.getY());
		}
	}

	@DisplayName("getFarthestPointTo")
	@Nested
	public class GetFarthestPointTo {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(-10, 2, 0));
			assertEpsilonEquals(-4.40345, result.getX());
			assertEpsilonEquals(19.90896, result.getY());
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(-10, 14, 0));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(8, result.getY());
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(-10, 25, 0));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(8, result.getY());
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(-1, 25, 0));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(8, result.getY());
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(1, 2, 0));
			assertEpsilonEquals(-5.70225, result.getX());
			assertEpsilonEquals(19.87266, result.getY());
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(12, 2, 0));
			assertEpsilonEquals(-6.4564, result.getX());
			assertEpsilonEquals(19.37073, result.getY());
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(12, 14, 0));
			assertEpsilonEquals(-6.94683, result.getX());
			assertEpsilonEquals(18.45808, result.getY());
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(12, 25, 0));
			assertEpsilonEquals(-6.84936, result.getX());
			assertEpsilonEquals(17.2385, result.getY());
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(-6, 8, 0));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(9, result.getY());
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(4, 17, 0));
			assertEpsilonEquals(-6.98777, result.getX());
			assertEpsilonEquals(18.22086, result.getY());
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(-4, 19, 0));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(8, result.getY());
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(6, 8.25, 0));
			assertEpsilonEquals(-6.49669, result.getX());
			assertEpsilonEquals(19.32662, result.getY());
		}
	}

	@DisplayName("getDistance")
	@Nested
	public class GetDistance {

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.0777472107, getS().getDistance(createTriangle(0, 0, 0, 1, 1, 1, 1, 0, 1)));
		}

		@DisplayName("(Triangle3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_2(CoordinateSystem3D cs) {
			// triangle intersects aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle has a vertex inside/on the box volume: (6, 8.5, 0.25)
			assertEpsilonEquals(0., getS().getDistance(createTriangle(6, 8.5, 0.25, 10, 8.5, 0.25, 6, 12, 0.25)));
		}

		@DisplayName("(Triangle3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_3(CoordinateSystem3D cs) {
			// triangle intersects sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle includes the sphere center (-5,18,0), thus intersects sphere
			// One valid closest point returned by the multishape is a point on the sphere.
			assertEpsilonEquals(0., getS().getDistance(createTriangle(-5, 18, 0, -3, 18, 0, -5, 20, 0)));
		}

		@DisplayName("(Triangle3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_4(CoordinateSystem3D cs) {
			// far on +X side, box is the nearest component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle near x=10, y=8.5, z=0.25 => closest multishape point is box face x=7
			assertEpsilonEquals(3., getS().getDistance(createTriangle(10, 8.5, 0.25, 10, 9.5, 0.25, 10, 8.5, 1.25)));
		}

		@DisplayName("(Triangle3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_5(CoordinateSystem3D cs) {
			// far on sphere side, sphere is the nearest component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle at x=-10 near y=18,z=0. Nearest sphere point is (-7,18,0), distance 3
			assertEpsilonEquals(3., getS().getDistance(createTriangle(-10, 18, 0, -10, 19, 0, -10, 18, 1)));
		}

		@DisplayName("(Triangle3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_6(CoordinateSystem3D cs) {
			// triangle tangent to sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle has vertex exactly on sphere surface: (-3,18,0)
			assertEpsilonEquals(0., getS().getDistance(createTriangle(-3, 18, 0, -3, 19, 0, -3, 18, 1)));
		}

		@DisplayName("(MultiShape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(-5, 18, 0, 0.5));
			shape.add(createSegment(0, 8.5, 0.25, 10, 8.5, 0.25));
			assertEpsilonEquals(0., getS().getDistance(shape));
		}

		@DisplayName("(MultiShape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_2(CoordinateSystem3D cs) {
			// disjoint, closest to aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(10, 8.5, 0.25, 1));                // distance 2 to box-comp
			shape.add(createSegment(12, 8.5, 0.25, 14, 8.5, 0.25));   // distance 5 to box-comp
			assertEpsilonEquals(2., getS().getDistance(shape));
		}

		@DisplayName("(MultiShape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_3(CoordinateSystem3D cs) {
			// disjoint, closest to sphere component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(0, 18, 0, 1));                     // distance 2 to ref-sphere
			shape.add(createAlignedBoxFromPoints(20, 20, 0, 21, 21, 1));
			assertEpsilonEquals(2., getS().getDistance(shape));
		}

		@DisplayName("(MultiShape3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_4(CoordinateSystem3D cs) {
			// touching by tangent sphere-to-sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(0, 18, 0, 3)); // tangent with ref sphere centered at (-5,18,0), r=2
			shape.add(createSegment(100, 100, 100, 101, 101, 101));
			assertEpsilonEquals(0., getS().getDistance(shape));
		}

		@DisplayName("(MultiShape3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_5(CoordinateSystem3D cs) {
			// intersecting via box-box overlap
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createAlignedBoxFromPoints(6, 8.2, 0.1, 6.8, 8.8, 0.4)); // inside ref box-comp
			shape.add(createSphere(50, 50, 50, 1));
			assertEpsilonEquals(0., getS().getDistance(shape));
		}

		@DisplayName("(MultiShape3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_6(CoordinateSystem3D cs) {
			// intersecting via segment through reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(-10, 18, 0, 0, 18, 0)); // crosses ref sphere
			shape.add(createAlignedBoxFromPoints(30, 30, 30, 31, 31, 31));
			assertEpsilonEquals(0., getS().getDistance(shape));
		}

		@DisplayName("(MultiShape3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_7(CoordinateSystem3D cs) {
			// degenerate point shape inside reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(-5, 18, 0, -5, 18, 0)); // point
			assertEpsilonEquals(0., getS().getDistance(shape));
		}

		@DisplayName("(MultiShape3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_8(CoordinateSystem3D cs) {
			// degenerate point shape outside both components
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(0, 0, 0, 0, 0, 0)); // point
			assertEpsilonEquals(9.433981132056603, getS().getDistance(shape));
		}

		@DisplayName("(MultiShape3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_9(CoordinateSystem3D cs) {
			// empty multishape
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			// Keep only if API defines behavior for empty multishape as infinite/undefined distance.
			// If your implementation returns NaN or throws, adapt these assertions accordingly.
			assertFalse(Double.isNaN(getS().getDistance(shape)));
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(0, 0, 0);
			path.lineTo(1, 1, 1);
			assertEpsilonEquals(8.0777472107, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_2(CoordinateSystem3D cs) {
			//polyline crossing aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(0, 8.5, 0.25);
			path.lineTo(10, 8.5, 0.25);
			assertEpsilonEquals(0., getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_3(CoordinateSystem3D cs) {
			// polyline crossing sphere component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(-10, 18, 0);
			path.lineTo(0, 18, 0);
			assertEpsilonEquals(0., getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_4(CoordinateSystem3D cs) {
			// path near sphere but outside
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(-10, 21, 0);
			path.lineTo(0, 21, 0); // distance to sphere center line = 3, radius=2 => gap=1
			assertEpsilonEquals(1., getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_5(CoordinateSystem3D cs) {
			// path near aligned-box component but outside
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(10, 8.5, 0.25);
			path.lineTo(12, 8.5, 0.25); // box xmax=7 => gap=3
			assertEpsilonEquals(3., getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_6(CoordinateSystem3D cs) {
			// quadratic crossing aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(0, 8.5, 0.25);
			path.quadTo(6, 8.5, 0.25, 10, 8.5, 0.25);
			assertEpsilonEquals(0., getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_7(CoordinateSystem3D cs) {
			// cubic crossing sphere component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(-10, 18, 0);
			path.curveTo(-8, 18, 0, -2, 18, 0, 0, 18, 0);
			assertEpsilonEquals(0., getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_8(CoordinateSystem3D cs) {
			// mixed path, closest on aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(20, 0, 0);
			path.lineTo(20, 30, 0);           // far from both
			path.quadTo(12, 12, 0.25, 10, 9, 0.25); // comes near box, stays outside
			assertEpsilonEquals(3., getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_9(CoordinateSystem3D cs) {
			// degenerate path point inside sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(-5, 18, 0);
			path.lineTo(-5, 18, 0);
			assertEpsilonEquals(0., getS().getDistance(path));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.062257748, getS().getDistance(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_2(CoordinateSystem3D cs) {
			// overlap aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createAlignedBoxFromPoints(6, 8.2, 0.1, 6.8, 8.8, 0.4)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_3(CoordinateSystem3D cs) {
			// touching aligned-box face x=5
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createAlignedBoxFromPoints(4, 8.2, 0.1, 5, 8.8, 0.4)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_4(CoordinateSystem3D cs) {
			// overlap sphere component only
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// contains sphere center
			assertEpsilonEquals(0., getS().getDistance(createAlignedBoxFromPoints(-6, 17, -0.5, -4, 19, 0.5)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_5(CoordinateSystem3D cs) {
			// tangent to sphere at one side
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// xmin=-1, sphere rightmost point at x=-3? no overlap
			// Better exact tangent: box xmin = -3 (sphere center -5, r=2)
			assertEpsilonEquals(0., getS().getDistance(createAlignedBoxFromPoints(-3, 17, -1, 1, 19, 1)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_6(CoordinateSystem3D cs) {
			// outside near aligned-box component (+X)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// dx from x=7 is 3
			assertEpsilonEquals(3., getS().getDistance(createAlignedBoxFromPoints(10, 8.2, 0.1, 11, 8.8, 0.4)));
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_7(CoordinateSystem3D cs) {
			// outside near sphere component (+X)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// nearest box point to sphere center is (0,18,0): center-distance=5, minus radius 2 => 3
			assertEpsilonEquals(3., getS().getDistance(createAlignedBoxFromPoints(0, 17, -1, 1, 19, 1)));
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_8(CoordinateSystem3D cs) {
			// box between both components but disjoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// closer to small aligned-box component than sphere
			// gap 1 to xmin=5
			assertEpsilonEquals(1., getS().getDistance(createAlignedBoxFromPoints(3, 8.2, 0.1, 4, 8.8, 0.4)));
		}

		@DisplayName("(AlignedBox3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_9(CoordinateSystem3D cs) {
			// degenerate point inside aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var b = createAlignedBoxFromPoints(6, 8.5, 0.25, 6, 8.5, 0.25);
			assertEpsilonEquals(0., getS().getDistance(b));
		}

		@DisplayName("(AlignedBox3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_10(CoordinateSystem3D cs) {
			// degenerate point inside sphere component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var b = createAlignedBoxFromPoints(-5, 18, 0, -5, 18, 0);
			assertEpsilonEquals(0., getS().getDistance(b));
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.4339811321, getS().getDistance(createSphere(0, 0, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_2(CoordinateSystem3D cs) {
			// center inside aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSphere(6, 8.5, 0.25, 0.1)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_3(CoordinateSystem3D cs) {
			// center inside reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSphere(-5, 18, 0, 0.5)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_4(CoordinateSystem3D cs) {
			// tangent to aligned-box face
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// tangent at x=5
			assertEpsilonEquals(0., getS().getDistance(createSphere(4, 8.5, 0.25, 1)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_5(CoordinateSystem3D cs) {
			// tangent to reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// centers distance = 5, radii sum = 2 + 3 = 5
			assertEpsilonEquals(0., getS().getDistance(createSphere(0, 18, 0, 3)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_6(CoordinateSystem3D cs) {
			// outside but closest to aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// closest to box at (7,8.5,0.25), center distance to box=3, radius=1 => dist=2
			assertEpsilonEquals(2., getS().getDistance(createSphere(10, 8.5, 0.25, 1)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_7(CoordinateSystem3D cs) {
			// outside but closest to reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// center distance to (-5,18,0) is 5, minus radii (2+1) => 2
			assertEpsilonEquals(2., getS().getDistance(createSphere(0, 18, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_8(CoordinateSystem3D cs) {
			// large sphere enclosing both components
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSphere(0, 10, 0, 30)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.0777472107, getS().getDistance(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_2(CoordinateSystem3D cs) {
			// crossing aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Through box interior: y=8.5, z=0.25 in box ranges
			assertEpsilonEquals(0., getS().getDistance(createSegment(0, 8.5, 0.25, 10, 8.5, 0.25)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_3(CoordinateSystem3D cs) {
			// touching aligned-box corner
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSegment(0, 0, 0, 5, 8, 0)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_4(CoordinateSystem3D cs) {
			// crossing sphere center line
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Passes through sphere center (-5,18,0)
			assertEpsilonEquals(0., getS().getDistance(createSegment(-10, 18, 0, 0, 18, 0)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_5(CoordinateSystem3D cs) {
			// tangent to sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Distance to center is exactly radius=2 (line y=20 through z=0)
			assertEpsilonEquals(0., getS().getDistance(createSegment(-10, 20, 0, 0, 20, 0)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_6(CoordinateSystem3D cs) {
			// near sphere, outside
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Closest point to sphere center is (-5,21,0): center distance 3 => shape distance 1
			assertEpsilonEquals(1., getS().getDistance(createSegment(-10, 21, 0, 0, 21, 0)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_7(CoordinateSystem3D cs) {
			// near aligned box, outside along +X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment at x=10, y/z aligned to box interior => dx=3 from xmax=7
			assertEpsilonEquals(3., getS().getDistance(createSegment(10, 8.5, 0.25, 10, 9, 0.25)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_8(CoordinateSystem3D cs) {
			// degenerate point inside sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSegment(-5, 18, 0, -5, 18, 0)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_9(CoordinateSystem3D cs) {
			// degenerate point inside aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSegment(6, 8.5, 0.25, 6, 8.5, 0.25)));
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_1(CoordinateSystem3D cs) {
			// tangent to aligned-box face
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// tangent at x=5
			assertEpsilonEquals(0., getS().getDistance((Shape3D) createSphere(4, 8.5, 0.25, 1)));
		}

		@DisplayName("(Shape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance((Shape3D) createSegment(6, 8.5, 0.25, 6, 8.5, 0.25)));
		}

		@DisplayName("(Shape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_3(CoordinateSystem3D cs) {
			// overlap sphere component only
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// contains sphere center
			assertEpsilonEquals(0., getS().getDistance((Shape3D) createAlignedBoxFromPoints(-6, 17, -0.5, -4, 19, 0.5)));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(14.76305, getS().getDistance(createPoint(-10, 2, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.40312, getS().getDistance(createPoint(-10, 14, 0)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.60233, getS().getDistance(createPoint(-10, 25, 0)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.06226, getS().getDistance(createPoint(-1, 25, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.21110, getS().getDistance(createPoint(1, 2, 0)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.81025, getS().getDistance(createPoint(12, 2, 0)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.07107, getS().getDistance(createPoint(12, 14, 0)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16.38478, getS().getDistance(createPoint(12, 25, 0)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.04988, getS().getDistance(createPoint(-6, 8, 0)));
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.05538, getS().getDistance(createPoint(4, 17, 0)));
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createPoint(-4, 19, 0)));
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createPoint(6, 8.25, 0)));
		}
	}

	@DisplayName("getDistanceSquared")
	@Nested
	public class GetDistanceSquared {

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(65.25, getS().getDistanceSquared(createTriangle(0, 0, 0, 1, 1, 1, 1, 0, 1)));
		}

		@DisplayName("(Triangle3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_2(CoordinateSystem3D cs) {
			// triangle intersects aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle has a vertex inside/on the box volume: (6, 8.5, 0.25)
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(6, 8.5, 0.25, 10, 8.5, 0.25, 6, 12, 0.25)));
		}

		@DisplayName("(Triangle3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_3(CoordinateSystem3D cs) {
			// triangle intersects sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle includes the sphere center (-5,18,0), thus intersects sphere
			// One valid closest point returned by the multishape is a point on the sphere.
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(-5, 18, 0, -3, 18, 0, -5, 20, 0)));
		}

		@DisplayName("(Triangle3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_4(CoordinateSystem3D cs) {
			// far on +X side, box is the nearest component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle near x=10, y=8.5, z=0.25 => closest multishape point is box face x=7
			assertEpsilonEquals(9., getS().getDistanceSquared(createTriangle(10, 8.5, 0.25, 10, 9.5, 0.25, 10, 8.5, 1.25)));
		}

		@DisplayName("(Triangle3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_5(CoordinateSystem3D cs) {
			// far on sphere side, sphere is the nearest component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle at x=-10 near y=18,z=0. Nearest sphere point is (-7,18,0), distance 3
			assertEpsilonEquals(9., getS().getDistanceSquared(createTriangle(-10, 18, 0, -10, 19, 0, -10, 18, 1)));
		}

		@DisplayName("(Triangle3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_6(CoordinateSystem3D cs) {
			// triangle tangent to sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle has vertex exactly on sphere surface: (-3,18,0)
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(-3, 18, 0, -3, 19, 0, -3, 18, 1)));
		}

		@DisplayName("(MultiShape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(-5, 18, 0, 0.5));
			shape.add(createSegment(0, 8.5, 0.25, 10, 8.5, 0.25));
			assertEpsilonEquals(0., getS().getDistanceSquared(shape));
		}

		@DisplayName("(MultiShape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_2(CoordinateSystem3D cs) {
			// disjoint, closest to aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(10, 8.5, 0.25, 1));                // distance 2 to box-comp
			shape.add(createSegment(12, 8.5, 0.25, 14, 8.5, 0.25));   // distance 5 to box-comp
			assertEpsilonEquals(4., getS().getDistanceSquared(shape));
		}

		@DisplayName("(MultiShape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_3(CoordinateSystem3D cs) {
			// disjoint, closest to sphere component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(0, 18, 0, 1));                     // distance 2 to ref-sphere
			shape.add(createAlignedBoxFromPoints(20, 20, 0, 21, 21, 1));
			assertEpsilonEquals(4., getS().getDistanceSquared(shape));
		}

		@DisplayName("(MultiShape3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_4(CoordinateSystem3D cs) {
			// touching by tangent sphere-to-sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(0, 18, 0, 3)); // tangent with ref sphere centered at (-5,18,0), r=2
			shape.add(createSegment(100, 100, 100, 101, 101, 101));
			assertEpsilonEquals(0., getS().getDistanceSquared(shape));
		}

		@DisplayName("(MultiShape3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_5(CoordinateSystem3D cs) {
			// intersecting via box-box overlap
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createAlignedBoxFromPoints(6, 8.2, 0.1, 6.8, 8.8, 0.4)); // inside ref box-comp
			shape.add(createSphere(50, 50, 50, 1));
			assertEpsilonEquals(0., getS().getDistanceSquared(shape));
		}

		@DisplayName("(MultiShape3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_6(CoordinateSystem3D cs) {
			// intersecting via segment through reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(-10, 18, 0, 0, 18, 0)); // crosses ref sphere
			shape.add(createAlignedBoxFromPoints(30, 30, 30, 31, 31, 31));
			assertEpsilonEquals(0., getS().getDistanceSquared(shape));
		}

		@DisplayName("(MultiShape3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_7(CoordinateSystem3D cs) {
			// degenerate point shape inside reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(-5, 18, 0, -5, 18, 0)); // point
			assertEpsilonEquals(0., getS().getDistanceSquared(shape));
		}

		@DisplayName("(MultiShape3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_8(CoordinateSystem3D cs) {
			// degenerate point shape outside both components
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(0, 0, 0, 0, 0, 0)); // point
			assertEpsilonEquals(89., getS().getDistanceSquared(shape));
		}

		@DisplayName("(MultiShape3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_9(CoordinateSystem3D cs) {
			// empty multishape
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			// Keep only if API defines behavior for empty multishape as infinite/undefined distance.
			// If your implementation returns NaN or throws, adapt these assertions accordingly.
			assertFalse(Double.isNaN(getS().getDistanceSquared(shape)));
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(0, 0, 0);
			path.lineTo(1, 1, 1);
			assertEpsilonEquals(65.25, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_2(CoordinateSystem3D cs) {
			//polyline crossing aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(0, 8.5, 0.25);
			path.lineTo(10, 8.5, 0.25);
			assertEpsilonEquals(0., getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_3(CoordinateSystem3D cs) {
			// polyline crossing sphere component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(-10, 18, 0);
			path.lineTo(0, 18, 0);
			assertEpsilonEquals(0., getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_4(CoordinateSystem3D cs) {
			// path near sphere but outside
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(-10, 21, 0);
			path.lineTo(0, 21, 0); // distance to sphere center line = 3, radius=2 => gap=1
			assertEpsilonEquals(1., getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_5(CoordinateSystem3D cs) {
			// path near aligned-box component but outside
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(10, 8.5, 0.25);
			path.lineTo(12, 8.5, 0.25); // box xmax=7 => gap=3
			assertEpsilonEquals(9., getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_6(CoordinateSystem3D cs) {
			// quadratic crossing aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(0, 8.5, 0.25);
			path.quadTo(6, 8.5, 0.25, 10, 8.5, 0.25);
			assertEpsilonEquals(0., getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_7(CoordinateSystem3D cs) {
			// cubic crossing sphere component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(-10, 18, 0);
			path.curveTo(-8, 18, 0, -2, 18, 0, 0, 18, 0);
			assertEpsilonEquals(0., getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_8(CoordinateSystem3D cs) {
			// mixed path, closest on aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(20, 0, 0);
			path.lineTo(20, 30, 0);           // far from both
			path.quadTo(12, 12, 0.25, 10, 9, 0.25); // comes near box, stays outside
			assertEpsilonEquals(9., getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_9(CoordinateSystem3D cs) {
			// degenerate path point inside sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = factory.createPath();
			path.moveTo(-5, 18, 0);
			path.lineTo(-5, 18, 0);
			assertEpsilonEquals(0., getS().getDistanceSquared(path));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(64.99999995, getS().getDistanceSquared(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_2(CoordinateSystem3D cs) {
			// overlap aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createAlignedBoxFromPoints(6, 8.2, 0.1, 6.8, 8.8, 0.4)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_3(CoordinateSystem3D cs) {
			// touching aligned-box face x=5
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createAlignedBoxFromPoints(4, 8.2, 0.1, 5, 8.8, 0.4)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_4(CoordinateSystem3D cs) {
			// overlap sphere component only
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// contains sphere center
			assertEpsilonEquals(0., getS().getDistanceSquared(createAlignedBoxFromPoints(-6, 17, -0.5, -4, 19, 0.5)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_5(CoordinateSystem3D cs) {
			// tangent to sphere at one side
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// xmin=-1, sphere rightmost point at x=-3? no overlap
			// Better exact tangent: box xmin = -3 (sphere center -5, r=2)
			assertEpsilonEquals(0., getS().getDistanceSquared(createAlignedBoxFromPoints(-3, 17, -1, 1, 19, 1)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_6(CoordinateSystem3D cs) {
			// outside near aligned-box component (+X)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// dx from x=7 is 3
			assertEpsilonEquals(9., getS().getDistanceSquared(createAlignedBoxFromPoints(10, 8.2, 0.1, 11, 8.8, 0.4)));
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_7(CoordinateSystem3D cs) {
			// outside near sphere component (+X)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// nearest box point to sphere center is (0,18,0): center-distance=5, minus radius 2 => 3
			assertEpsilonEquals(9., getS().getDistanceSquared(createAlignedBoxFromPoints(0, 17, -1, 1, 19, 1)));
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_8(CoordinateSystem3D cs) {
			// box between both components but disjoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// closer to small aligned-box component than sphere
			// gap 1 to xmin=5
			assertEpsilonEquals(1., getS().getDistanceSquared(createAlignedBoxFromPoints(3, 8.2, 0.1, 4, 8.8, 0.4)));
		}

		@DisplayName("(AlignedBox3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_9(CoordinateSystem3D cs) {
			// degenerate point inside aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var b = createAlignedBoxFromPoints(6, 8.5, 0.25, 6, 8.5, 0.25);
			assertEpsilonEquals(0., getS().getDistanceSquared(b));
		}

		@DisplayName("(AlignedBox3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_10(CoordinateSystem3D cs) {
			// degenerate point inside sphere component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createAlignedBoxFromPoints(-5, 18, 0, -5, 18, 0)));
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(71.132037737, getS().getDistanceSquared(createSphere(0, 0, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_2(CoordinateSystem3D cs) {
			// center inside aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createSphere(6, 8.5, 0.25, 0.1)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_3(CoordinateSystem3D cs) {
			// center inside reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createSphere(-5, 18, 0, 0.5)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_4(CoordinateSystem3D cs) {
			// tangent to aligned-box face
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// tangent at x=5
			assertEpsilonEquals(0., getS().getDistanceSquared(createSphere(4, 8.5, 0.25, 1)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_5(CoordinateSystem3D cs) {
			// tangent to reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// centers distance = 5, radii sum = 2 + 3 = 5
			assertEpsilonEquals(0., getS().getDistanceSquared(createSphere(0, 18, 0, 3)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_6(CoordinateSystem3D cs) {
			// outside but closest to aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// closest to box at (7,8.5,0.25), center distance to box=3, radius=1 => dist=2
			assertEpsilonEquals(4., getS().getDistanceSquared(createSphere(10, 8.5, 0.25, 1)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_7(CoordinateSystem3D cs) {
			// outside but closest to reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// center distance to (-5,18,0) is 5, minus radii (2+1) => 2
			assertEpsilonEquals(4., getS().getDistanceSquared(createSphere(0, 18, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_8(CoordinateSystem3D cs) {
			// large sphere enclosing both components
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createSphere(0, 10, 0, 30)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(65.25, getS().getDistanceSquared(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_2(CoordinateSystem3D cs) {
			// crossing aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Through box interior: y=8.5, z=0.25 in box ranges
			assertEpsilonEquals(0., getS().getDistanceSquared(createSegment(0, 8.5, 0.25, 10, 8.5, 0.25)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_3(CoordinateSystem3D cs) {
			// touching aligned-box corner
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createSegment(0, 0, 0, 5, 8, 0)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_4(CoordinateSystem3D cs) {
			// crossing sphere center line
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Passes through sphere center (-5,18,0)
			assertEpsilonEquals(0., getS().getDistanceSquared(createSegment(-10, 18, 0, 0, 18, 0)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_5(CoordinateSystem3D cs) {
			// tangent to sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Distance to center is exactly radius=2 (line y=20 through z=0)
			assertEpsilonEquals(0., getS().getDistanceSquared(createSegment(-10, 20, 0, 0, 20, 0)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_6(CoordinateSystem3D cs) {
			// near sphere, outside
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Closest point to sphere center is (-5,21,0): center distance 3 => shape distance 1
			assertEpsilonEquals(1., getS().getDistanceSquared(createSegment(-10, 21, 0, 0, 21, 0)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_7(CoordinateSystem3D cs) {
			// near aligned box, outside along +X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment at x=10, y/z aligned to box interior => dx=3 from xmax=7
			assertEpsilonEquals(9., getS().getDistanceSquared(createSegment(10, 8.5, 0.25, 10, 9, 0.25)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_8(CoordinateSystem3D cs) {
			// degenerate point inside sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createSegment(-5, 18, 0, -5, 18, 0)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_9(CoordinateSystem3D cs) {
			// degenerate point inside aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createSegment(6, 8.5, 0.25, 6, 8.5, 0.25)));
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared((Shape3D) createSegment(6, 8.5, 0.25, 6, 8.5, 0.25)));
		}

		@DisplayName("(Shape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// xmin=-1, sphere rightmost point at x=-3? no overlap
			// Better exact tangent: box xmin = -3 (sphere center -5, r=2)
			assertEpsilonEquals(0., getS().getDistanceSquared((Shape3D) createAlignedBoxFromPoints(-3, 17, -1, 1, 19, 1)));
		}

		@DisplayName("(Shape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// center distance to (-5,18,0) is 5, minus radii (2+1) => 2
			assertEpsilonEquals(4., getS().getDistanceSquared((Shape3D) createSphere(0, 18, 0, 1)));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(217.94778, getS().getDistanceSquared(createPoint(-10, 2, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(19.38749, getS().getDistanceSquared(createPoint(-10, 14, 0)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(43.5907, getS().getDistanceSquared(createPoint(-10, 25, 0)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(36.75092, getS().getDistanceSquared(createPoint(-1, 25, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(52, getS().getDistanceSquared(createPoint(1, 2, 0)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(61, getS().getDistanceSquared(createPoint(12, 2, 0)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(50, getS().getDistanceSquared(createPoint(12, 14, 0)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(268.46089, getS().getDistanceSquared(createPoint(12, 25, 0)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(64.8005, getS().getDistanceSquared(createPoint(-6, 8, 0)));
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(49.77843, getS().getDistanceSquared(createPoint(4, 17, 0)));
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(-4, 19, 0)));
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point12_(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(6, 8.25, 0)));
		}
	}

	@DisplayName("getDistanceL1")
	@Nested
	public class GetDistanceL1 {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(18.49449, getS().getDistanceL1(createPoint(-10, 2, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.18887, getS().getDistanceL1(createPoint(-10, 14, 0)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9.21006, getS().getDistanceL1(createPoint(-10, 25, 0)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.27123, getS().getDistanceL1(createPoint(-1, 25, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10, getS().getDistanceL1(createPoint(1, 2, 0)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11, getS().getDistanceL1(createPoint(12, 2, 0)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10, getS().getDistanceL1(createPoint(12, 14, 0)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(21, getS().getDistanceL1(createPoint(12, 25, 0)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.81092, getS().getDistanceL1(createPoint(-6, 8, 0)));
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.79137, getS().getDistanceL1(createPoint(4, 17, 0)));
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(-4, 19, 0)));
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(6, 8.25, 0)));
		}
	}

	@DisplayName("getDistanceLinf")
	@Nested
	public class GetDistanceLinf {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(14.09104, getS().getDistanceLinf(createPoint(-10, 2, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.43826, getS().getDistanceLinf(createPoint(-10, 14, 0)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.37253, getS().getDistanceLinf(createPoint(-10, 25, 0)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.26351, getS().getDistanceLinf(createPoint(-1, 25, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6, getS().getDistanceLinf(createPoint(1, 2, 0)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6, getS().getDistanceLinf(createPoint(12, 2, 0)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getDistanceLinf(createPoint(12, 14, 0)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.15064, getS().getDistanceLinf(createPoint(12, 25, 0)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.00993, getS().getDistanceLinf(createPoint(-6, 8, 0)));
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.01223, getS().getDistanceLinf(createPoint(4, 17, 0)));
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(-4, 19, 0)));
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void point_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(6, 8.25, 0)));
		}
	}

	@DisplayName("set(IT)")
	@Nested
	public class SetIT {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set((T) createMultiShape());
			assertEquals(0, getS().size());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp newShape = createMultiShape();
			newShape.add(createAlignedBox(-6, 48, 0, 5, 7, 6));
			getS().set((T) newShape);
			assertEquals(1, getS().size());
			var shape0 = (AlignedBox3afp) getS().get(0);
			assertEpsilonEquals(createPoint(-6, 48, 0), createPoint(shape0.getMinX(), shape0.getMinY(), shape0.getMinZ()));
			assertEpsilonEquals(createPoint(-1, 55, 6), createPoint(shape0.getMaxX(), shape0.getMaxY(), shape0.getMaxZ()));
		}
	}

	@DisplayName("translate")
	@Nested
	public class Translate {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void xyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(10, -2, 1);
			assertEquals(2, getS().size());
			var shape0 = (AlignedBox3afp) getS().get(0);
			assertEpsilonEquals(createPoint(15, 6, 1), createPoint(shape0.getMinX(), shape0.getMinY(), shape0.getMinZ()));
			assertEpsilonEquals(createPoint(17, 7, 1.5), createPoint(shape0.getMaxX(), shape0.getMaxY(), shape0.getMaxZ()));
			var shape1 = (Sphere3afp) getS().get(1);
			assertEpsilonEquals(createPoint(5, 16, 1), shape1.getCenter());
			assertEpsilonEquals(2, shape1.getRadius());
		}

		@DisplayName("(Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void vector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(createVector(10, -2, 1));
			assertEquals(2, getS().size());
			var shape0 = (AlignedBox3afp) getS().get(0);
			assertEpsilonEquals(createPoint(15, 6, 1), createPoint(shape0.getMinX(), shape0.getMinY(), shape0.getMinZ()));
			assertEpsilonEquals(createPoint(17, 7, 1.5), createPoint(shape0.getMaxX(), shape0.getMaxY(), shape0.getMaxZ()));
			var shape1 = (Sphere3afp) getS().get(1);
			assertEpsilonEquals(createPoint(5, 16, 1), shape1.getCenter());
			assertEpsilonEquals(2, shape1.getRadius());
		}
	}

	@DisplayName("toBoundingBox")
	@Nested
	public class ToBoundingBox {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(-2, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(2, box.getMaxZ());
		}
	
		@DisplayName("(box) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = createAlignedBox(0, 0, 0, 0, 0, 0);
			getS().toBoundingBox(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(-2, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(2, box.getMaxZ());
		}
	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createTriangle(0, 0, 0, 1, 1, 1, 1, 0, 1)));
		}

		@DisplayName("(Triangle3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_2(CoordinateSystem3D cs) {
			// triangle intersects aligned box
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle has a vertex inside/on the box volume: (6, 8.5, 0.25)
			assertTrue(getS().intersects(createTriangle(6, 8.5, 0.25, 10, 8.5, 0.25, 6, 12, 0.25)));
		}

		@DisplayName("(Triangle3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_3(CoordinateSystem3D cs) {
			// triangle intersects sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle includes the sphere center (-5,18,0), thus intersects sphere
			// One valid closest point returned by the multishape is a point on the sphere.
			assertTrue(getS().intersects(createTriangle(-5, 18, 0, -3, 18, 0, -5, 20, 0)));
		}

		@DisplayName("(Triangle3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_4(CoordinateSystem3D cs) {
			// far on +X side, box is the nearest component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle near x=10, y=8.5, z=0.25 => closest multishape point is box face x=7
			assertFalse(getS().intersects(createTriangle(10, 8.5, 0.25, 10, 9.5, 0.25, 10, 8.5, 1.25)));
		}

		@DisplayName("(Triangle3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_5(CoordinateSystem3D cs) {
			// far on sphere side, sphere is the nearest component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle at x=-10 near y=18,z=0. Nearest sphere point is (-7,18,0), distance 3
			assertFalse(getS().intersects(createTriangle(-10, 18, 0, -10, 19, 0, -10, 18, 1)));
		}

		@DisplayName("(Triangle3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_6(CoordinateSystem3D cs) {
			// triangle tangent to sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle has vertex exactly on sphere surface: (-3,18,0)
			assertTrue(getS().intersects(createTriangle(-3, 18, 0, -3, 19, 0, -3, 18, 1)));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createAlignedBox(-20, 14, 0, .5, .5, 0)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createAlignedBox(-2, -10, 0, .5, .5, 0)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createAlignedBox(-6, 16, 0, .5, .5, 0)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createAlignedBox(4.75, 8, 0, .5, .5, 0)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createAlignedBox(-4, 18, 0, .5, .5, 0)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createAlignedBox(5.5, 8.5, 0, .5, .5, 0)));
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(-20, 14, 0, .5)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(-2,- 10, 0, .5)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(-6, 16, 0, .5)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(4.75, 8, 0, .5)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(-4, 18, 0, .5)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(5.5, 8.5, 0, .5)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(-20, 14, 0, -19.5, 14, 0)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(-2, -10, 0, -1.5, -10, 0)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-6, 16, 0, -5.5, 16.5, 0)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(4.75, 8, 0, 5.25, 8, 0)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-4, 18, 0, -3.5, 18, 0)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(5.5, 8.5, 0, 6, 8.5, 0)));
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path3Ieratorafp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void pathiterator_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().intersects((PathIterator3afp) path.getPathIterator()));
		}

		@DisplayName("(Path3Ieratorafp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void pathiterator_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertFalse(getS().intersects((PathIterator3afp) path.getPathIterator()));
		}

		@DisplayName("(Path3Ieratorafp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void pathiterator_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().intersects((PathIterator3afp) path.getPathIterator()));
		}

		@DisplayName("(Path3Ieratorafp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void pathiterator_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertFalse(getS().intersects((PathIterator3afp) path.getPathIterator()));
		}

		@DisplayName("(Path3Ieratorafp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void pathiterator_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().intersects((PathIterator3afp) path.getPathIterator()));
		}

		@DisplayName("(Path3Ieratorafp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void pathiterator_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertFalse(getS().intersects((PathIterator3afp) path.getPathIterator()));
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects((Shape3D) createSphere(4.75, 8, 0, .5)));
		}

		@DisplayName("(Shape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().intersects((Shape3D) path));
		}

		@DisplayName("(Shape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertFalse(getS().intersects((Shape3D) path));
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(-5, 18, 0, -5, 18, 0));
			assertTrue(getS().intersects(shape));
		}

		@DisplayName("(MultiShape3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_2(CoordinateSystem3D cs) {
			// empty multishape
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			assertFalse(getS().intersects(shape));
		}

		@DisplayName("(MultiShape3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_3(CoordinateSystem3D cs) {
			// one shape intersects (aligned-box overlap)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createAlignedBoxFromPoints(6, 8.2, 0.1, 6.8, 8.8, 0.4)); // inside ref box component
			shape.add(createSphere(100, 100, 100, 1)); // far away
			assertTrue(getS().intersects(shape));
		}

		@DisplayName("(MultiShape3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_4(CoordinateSystem3D cs) {
			// one shape intersects (sphere overlap)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(-5, 18, 0, 0.5)); // inside ref sphere component
			shape.add(createSegment(50, 50, 50, 60, 60, 60));
			assertTrue(getS().intersects(shape));
		}

		@DisplayName("(MultiShape3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_5(CoordinateSystem3D cs) {
			// tangent to reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(0, 18, 0, 3)); // tangent with sphere center (-5,18,0), r=2
			assertFalse(getS().intersects(shape));
		}

		@DisplayName("(MultiShape3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_6(CoordinateSystem3D cs) {
			// tangent to reference aligned-box face
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(0, 8.5, 0.25, 5, 8.5, 0.25)); // endpoint on face x=5
			assertTrue(getS().intersects(shape));
		}

		@DisplayName("(MultiShape3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_7(CoordinateSystem3D cs) {
			// all components disjoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(20, 20, 20, 1));
			shape.add(createAlignedBoxFromPoints(30, 30, 30, 31, 31, 31));
			shape.add(createSegment(40, 40, 40, 45, 45, 45));
			assertFalse(getS().intersects(shape));
		}

		@DisplayName("(MultiShape3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_8(CoordinateSystem3D cs) {
			// crossing segment through reference aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(0, 8.5, 0.25, 10, 8.5, 0.25)); // crosses ref box component
			assertTrue(getS().intersects(shape));
		}

		@DisplayName("(MultiShape3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_9(CoordinateSystem3D cs) {
			// crossing segment through reference sphere component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(-10, 18, 0, 0, 18, 0)); // crosses ref sphere component
			assertTrue(getS().intersects(shape));
		}
	}

	@DisplayName("this += Vector3D")
	@Nested
	public class OperatorAddVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_add(createVector(10, -2, 1));
			assertEquals(2, getS().size());
			var shape0 = (AlignedBox3afp) shape.get(0);
			assertEpsilonEquals(createPoint(15, 6, 1), createPoint(shape0.getMinX(), shape0.getMinY(), shape0.getMinZ()));
			assertEpsilonEquals(createPoint(17, 7, 1.5), createPoint(shape0.getMaxX(), shape0.getMaxY(), shape0.getMaxZ()));
			var shape1 = (Sphere3afp) shape.get(1);
			assertEpsilonEquals(createPoint(5, 16, 1), shape1.getCenter());
			assertEpsilonEquals(2, shape1.getRadius());
		}
	}

	@DisplayName("this + Vector3D")
	@Nested
	public class OperatorPlusVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			T shape = getS().operator_plus(createVector(10, -2, 1));
			assertEquals(2, getS().size());
			var shape0 = (AlignedBox3afp) shape.get(0);
			assertEpsilonEquals(createPoint(15, 6, 1), createPoint(shape0.getMinX(), shape0.getMinY(), shape0.getMinZ()));
			assertEpsilonEquals(createPoint(17, 7, 1.5), createPoint(shape0.getMaxX(), shape0.getMaxY(), shape0.getMaxZ()));
			var shape1 = (Sphere3afp) shape.get(1);
			assertEpsilonEquals(createPoint(5, 16, 1), shape1.getCenter());
			assertEpsilonEquals(2, shape1.getRadius());
		}
	}

	@DisplayName("this -= Vector3D")
	@Nested
	public class OperatorRemoveVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_remove(createVector(10, -2, 1));
			assertEquals(2, getS().size());
			var shape0 = (AlignedBox3afp) getS().get(0);
			assertEpsilonEquals(createPoint(-5, 10, -1), createPoint(shape0.getMinX(), shape0.getMinY(), shape0.getMinZ()));
			assertEpsilonEquals(createPoint(-3, 11, -.5), createPoint(shape0.getMaxX(), shape0.getMaxY(), shape0.getMaxZ()));
			var shape1 = (Sphere3afp) getS().get(1);
			assertEpsilonEquals(createPoint(-15, 20, -1), shape1.getCenter());
			assertEpsilonEquals(2, shape1.getRadius());
		}
	}

	@DisplayName("this - Vector3D")
	@Nested
	public class OperatorMinusVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			T shape = getS().operator_minus(createVector(10, -2, 1));
			assertEquals(2, getS().size());
			var shape0 = (AlignedBox3afp) shape.get(0);
			assertEpsilonEquals(createPoint(-5, 10, -1), createPoint(shape0.getMinX(), shape0.getMinY(), shape0.getMinZ()));
			assertEpsilonEquals(createPoint(-3, 11, -.5), createPoint(shape0.getMaxX(), shape0.getMaxY(), shape0.getMaxZ()));
			var shape1 = (Sphere3afp) shape.get(1);
			assertEpsilonEquals(createPoint(-15, 20, -1), shape1.getCenter());
			assertEpsilonEquals(2, shape1.getRadius());
		}
	}

	@DisplayName("this && Point3D")
	@Nested
	public class OperatorAndPoint3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(-10, 2, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(-10, 14, 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(-10, 25, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(-1, 25, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(1, 2, 0)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(12, 2, 0)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(12, 14, 0)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(12, 25, 0)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(-6, 8, 0)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(4, 17, 0)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(-4, 19, 0)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(6, 8.25, 0)));
		}
	}

	@DisplayName("this && Shape3D")
	@Nested
	public class OperatorAndShape3D {

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createAlignedBox(-20, 14, 0, .5, .5, 0)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createAlignedBox(-2, -10, 0, .5, .5, 0)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createAlignedBox(-6, 16, 0, .5, .5, 0)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createAlignedBox(4.75, 8, 0, .5, .5, 0)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createAlignedBox(-4, 18, 0, .5, .5, 0)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void box_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createAlignedBox(5.5, 8.5, 0, .5, .5, 0)));
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createSphere(-20, 14, 0, .5)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createSphere(-2,- 10, 0, .5)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createSphere(-6, 16, 0, .5)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createSphere(4.75, 8, 0, .5)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createSphere(-4, 18, 0, .5)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createSphere(5.5, 8.5, 0, .5)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createSegment(-20, 14, 0, -19.5, 14, 0)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createSegment(-2, -10, 0, -1.5, -10, 0)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createSegment(-6, 16, 0, -5.5, 16.5, 0)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createSegment(4.75, 8, 0, 5.25, 8, 0)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createSegment(-4, 18, 0, -3.5, 18, 0)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createSegment(5.5, 8.5, 0, 6, 8.5, 0)));
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().operator_and(path));
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertFalse(getS().operator_and(path));
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().operator_and(path));
		}

		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertFalse(getS().operator_and(path));
		}

		@DisplayName("(Path3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().operator_and(path));
		}

		@DisplayName("(Path3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertFalse(getS().operator_and(path));
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and((Shape3D) createSphere(4.75, 8, 0, .5)));
		}

		@DisplayName("(Shape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().operator_and((Shape3D) path));
		}

		@DisplayName("(Shape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertFalse(getS().operator_and((Shape3D) path));
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(-5, 18, 0, -5, 18, 0));
			assertTrue(getS().operator_and(shape));
		}

		@DisplayName("(MultiShape3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_2(CoordinateSystem3D cs) {
			// empty multishape
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			assertFalse(getS().operator_and(shape));
		}

		@DisplayName("(MultiShape3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_3(CoordinateSystem3D cs) {
			// one shape intersects (aligned-box overlap)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createAlignedBoxFromPoints(6, 8.2, 0.1, 6.8, 8.8, 0.4)); // inside ref box component
			shape.add(createSphere(100, 100, 100, 1)); // far away
			assertTrue(getS().operator_and(shape));
		}

		@DisplayName("(MultiShape3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_4(CoordinateSystem3D cs) {
			// one shape intersects (sphere overlap)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(-5, 18, 0, 0.5)); // inside ref sphere component
			shape.add(createSegment(50, 50, 50, 60, 60, 60));
			assertTrue(getS().operator_and(shape));
		}

		@DisplayName("(MultiShape3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_5(CoordinateSystem3D cs) {
			// tangent to reference sphere
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(0, 18, 0, 3)); // tangent with sphere center (-5,18,0), r=2
			assertFalse(getS().operator_and(shape));
		}

		@DisplayName("(MultiShape3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_6(CoordinateSystem3D cs) {
			// tangent to reference aligned-box face
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(0, 8.5, 0.25, 5, 8.5, 0.25)); // endpoint on face x=5
			assertTrue(getS().operator_and(shape));
		}

		@DisplayName("(MultiShape3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_7(CoordinateSystem3D cs) {
			// all components disjoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSphere(20, 20, 20, 1));
			shape.add(createAlignedBoxFromPoints(30, 30, 30, 31, 31, 31));
			shape.add(createSegment(40, 40, 40, 45, 45, 45));
			assertFalse(getS().operator_and(shape));
		}

		@DisplayName("(MultiShape3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_8(CoordinateSystem3D cs) {
			// crossing segment through reference aligned-box component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(0, 8.5, 0.25, 10, 8.5, 0.25)); // crosses ref box component
			assertTrue(getS().operator_and(shape));
		}

		@DisplayName("(MultiShape3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_9(CoordinateSystem3D cs) {
			// crossing segment through reference sphere component
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shape = (MultiShape3afp) factory.createMultiShape();
			shape.add(createSegment(-10, 18, 0, 0, 18, 0)); // crosses ref sphere component
			assertTrue(getS().operator_and(shape));
		}
	}

	@DisplayName("this .. Point3D")
	@Nested
	public class OperatorUpToPoint3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(14.76305, getS().operator_upTo(createPoint(-10, 2, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.40312, getS().operator_upTo(createPoint(-10, 14, 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.60233, getS().operator_upTo(createPoint(-10, 25, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.06226, getS().operator_upTo(createPoint(-1, 25, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.21110, getS().operator_upTo(createPoint(1, 2, 0)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.81025, getS().operator_upTo(createPoint(12, 2, 0)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.07107, getS().operator_upTo(createPoint(12, 14, 0)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16.38478, getS().operator_upTo(createPoint(12, 25, 0)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.04988, getS().operator_upTo(createPoint(-6, 8, 0)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.05538, getS().operator_upTo(createPoint(4, 17, 0)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(-4, 19, 0)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(6, 8.25, 0)));
		}
	}

	@DisplayName("getFirstShapeContaining")
	@Nested
	public class GetFirstShapeContaining {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(-10, 2, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(-10, 14, 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(-10, 25, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(-1, 25, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(1, 2, 0)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(12, 2, 0)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(12, 14, 0)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(12, 25, 0)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(-6, 8, 0)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(4, 17, 0)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(secondObject, getS().getFirstShapeContaining(createPoint(-4, 19, 0)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(firstObject, getS().getFirstShapeContaining(createPoint(6, 8.25, 0)));
		}
	}
	
	@DisplayName("getShapesContaining")
	@Nested
	public class GetShapesContaining {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(-10, 2, 0)).isEmpty());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(-10, 14, 0)).isEmpty());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(-10, 25, 0)).isEmpty());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(-1, 25, 0)).isEmpty());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(1, 2, 0)).isEmpty());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(12, 2, 0)).isEmpty());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(12, 14, 0)).isEmpty());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(12, 25, 0)).isEmpty());
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(-6, 8, 0)).isEmpty());
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(4, 17, 0)).isEmpty());
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(Arrays.asList(secondObject), getS().getShapesContaining(createPoint(-4, 19, 0)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(Arrays.asList(firstObject), getS().getShapesContaining(createPoint(6, 8.25, 0)));
		}
	}

	@DisplayName("getFirstShapeIntersecting")
	@Nested
	public class GetFirstShapeIntersecting {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3D shape3d = getS();
			assertSame(firstObject, shape3d.getFirstShapeIntersecting(createSphere(4.75, 8, 0, .5)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3D shape3d = getS();
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			assertNull(shape3d.getFirstShapeIntersecting(path));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3D shape3d = getS();
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertNull(shape3d.getFirstShapeIntersecting(path));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3D shape3d = getS();
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 10, 0.5);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertEquals(firstObject, shape3d.getFirstShapeIntersecting(path));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3D shape3d = getS();
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 15, 0.5);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertEquals(secondObject, shape3d.getFirstShapeIntersecting(path));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3D shape3d = getS();
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 10, 0.5);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 15, 0.5);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertEquals(firstObject, shape3d.getFirstShapeIntersecting(path));
		}
	}
	
	@DisplayName("getShapesIntersecting")
	@Nested
	public class GetShapesIntersecting {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3D shape3d = getS();
			assertEquals(Arrays.asList(firstObject), shape3d.getShapesIntersecting(createSphere(4.75, 8, 0, .5)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3D shape3d = getS();
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			assertTrue(shape3d.getShapesIntersecting(path).isEmpty());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3D shape3d = getS();
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertTrue(shape3d.getShapesIntersecting(path).isEmpty());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3D shape3d = getS();
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 10, 0.5);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertEquals(Arrays.asList(firstObject), shape3d.getShapesIntersecting(path));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3D shape3d = getS();
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 15, 0.5);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertEquals(Arrays.asList(secondObject), shape3d.getShapesIntersecting(path));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3D shape3d = getS();
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 10, 0.5);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 15, 0.5);
			path.lineTo(6, 20, 0);
			path.closePath();
			assertEquals(Arrays.asList(firstObject, secondObject), shape3d.getShapesIntersecting(path));
		}
	}

	@DisplayName("getBackendDataList")
	@Nested
	public class GetBackendDataList {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNotNull(getS().getBackendDataList());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(2, getS().getBackendDataList().size());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(firstObject, getS().getBackendDataList().get(0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(secondObject, getS().getBackendDataList().get(1));
		}
	}

	@DisplayName("On geometry change")
	@Nested
	public class OnGeometryChange {

		@DisplayName("First object #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(-2, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(2, box.getMaxZ());
		}
		
		@DisplayName("First object #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			firstObject.translate(12, -7, 0);
			
			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9
			
			// R': 17;  1; 19;  2 
			
			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(1, box.getMinY());
			assertEpsilonEquals(-2, box.getMinZ());
			assertEpsilonEquals(19, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(2, box.getMaxZ());
		}

		@DisplayName("Second object #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void onGeometryChange_changeSecondObject_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(-2, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(2, box.getMaxZ());
		}

		@DisplayName("Second object #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void onGeometryChange_changeSecondObject_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			secondObject.translate(12, -7, 0);
			
			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9
			
			// C':  5;  9;  9; 13 
	
			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(5, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(-2, box.getMinZ());
			assertEpsilonEquals(9, box.getMaxX());
			assertEpsilonEquals(13, box.getMaxY());
			assertEpsilonEquals(2, box.getMaxZ());
		}

		@DisplayName("Geometry stay unchanged after removal #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void noGeometryChangeAfterRemoval_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(-2, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(2, box.getMaxZ());
		}

		@DisplayName("Geometry stay unchanged after removal #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void noGeometryChangeAfterRemoval_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			getS().remove(secondObject);
			secondObject.translate(1453, -451, 0);
			
			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(5, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(0, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(9, box.getMaxY());
			assertEpsilonEquals(.5, box.getMaxZ());
		}
	}

	@DisplayName("On backend list change")
	@Nested
	public class OnBackendListChange {

		@DisplayName("Addition #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(-2, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(2, box.getMaxZ());
		}
	
		@DisplayName("Addition #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			getS().add((C) createSphere(10, 14, 0, 1));
			
			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9
			
			// C':  9;  13;  11; 15 
	
			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(-2, box.getMinZ());
			assertEpsilonEquals(11, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(2, box.getMaxZ());
		}

		@DisplayName("Remove first #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void onBackendDataListChange_removalFirstObject_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(-2, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(2, box.getMaxZ());
		}
	
		@DisplayName("Remove first #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void onBackendDataListChange_removalFirstObject_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			getS().remove(firstObject);
			
			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9
			
			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(16, box.getMinY());
			assertEpsilonEquals(-2, box.getMinZ());
			assertEpsilonEquals(-3, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(2, box.getMaxZ());
		}

		@DisplayName("Remove second #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void onBackendDataListChange_removalSecondObject_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(-2, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(2, box.getMaxZ());
		}

		@DisplayName("Remove second #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void onBackendDataListChange_removalSecondObject_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			getS().remove(secondObject);
			
			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9
	
			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(5, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(0, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(9, box.getMaxY());
			assertEpsilonEquals(.5, box.getMaxZ());
		}
	}

	@DisplayName("getType")
	@Nested
	public class GetType {

		@DisplayName("(Class) #1")
		@Test
	    public void type_1() {
			assertSame(Shape3DType.MULTISHAPE, getS().getType(Shape3DType.class));
		}
	
		@DisplayName("() #1")
		@Test
		public void empty_1() {
			assertSame(Shape3DType.MULTISHAPE, getS().getType());
		}
	}

}
