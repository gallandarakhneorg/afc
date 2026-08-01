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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.channels.UnsupportedAddressTypeException;
import java.util.Arrays;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.MultiShape3D;
import org.arakhne.afc.math.geometry.base.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;
import org.arakhne.afc.math.geometry.d3.afp.Shape3afp;
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
		firstObject = (C) createAlignedBox(5, 8, 0, 2, 1, 0);
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
			/*PathIterator3afp pi = (PathIterator3afp) clone.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 5, 8, 0);
			assertElement(pi, PathElementType.LINE_TO, 7, 8, 0);
			assertElement(pi, PathElementType.LINE_TO, 7, 9, 0);
			assertElement(pi, PathElementType.LINE_TO, 5, 9, 0);
			assertElement(pi, PathElementType.CLOSE, 5, 8, 0);
			assertElement(pi, PathElementType.MOVE_TO, -3, 18, 0);
			assertElement(pi, PathElementType.CURVE_TO, -3, 19.10457, 0, -3.89543, 20, 0, -5, 20, 0);
			assertElement(pi, PathElementType.CURVE_TO, -6.10457, 20, 0, -7, 19.10457, 0, -7, 18, 0);
			assertElement(pi, PathElementType.CURVE_TO, -7, 16.89543, 0, -6.10457, 16, 0, -5, 16, 0);
			assertElement(pi, PathElementType.CURVE_TO, -3.89543, 16, 0, -3, 16.89543, 0, -3, 18, 0);
			assertElement(pi, PathElementType.CLOSE, -3, 18, 0);
			assertNoElement(pi);*/
			fail("TODO");
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
			/* TODO PathIterator3afp pi = getS().getPathIterator();
			assertNoElement(pi);*/
			fail("Todo");
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

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(MultiShape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
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

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
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

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
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
			/* TODO PathIterator3afp pi = getS().getPathIterator();
			assertNoElement(pi);
			MultiShape3afp newShape = createMultiShape();
			newShape.add(createAlignedBox(-6, 48, 0, 5, 7, 0));
			getS().set((T) newShape);
			pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -6, 48);
			assertElement(pi, PathElementType.LINE_TO, -1, 48);
			assertElement(pi, PathElementType.LINE_TO, -1, 55);
			assertElement(pi, PathElementType.LINE_TO, -6, 55);
			assertElement(pi, PathElementType.CLOSE, -6, 48);
			assertNoElement(pi);*/
			fail("TODO");
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
			getS().translate(10, -2, 0);
			/* TODO PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 7);
			assertElement(pi, PathElementType.LINE_TO, 15, 7);
			assertElement(pi, PathElementType.CLOSE, 15, 6);
			assertElement(pi, PathElementType.MOVE_TO, 7, 16);
			assertElement(pi, PathElementType.CURVE_TO, 7, 17.10457, 6.10457, 18, 5, 18);
			assertElement(pi, PathElementType.CURVE_TO, 3.89543, 18, 3, 17.10457, 3, 16);
			assertElement(pi, PathElementType.CURVE_TO, 3, 14.89543, 3.89543, 14, 5, 14);
			assertElement(pi, PathElementType.CURVE_TO, 6.10457, 14, 7, 14.89543, 7, 16);
			assertElement(pi, PathElementType.CLOSE, 7, 16);
			assertNoElement(pi);
			*/
			fail("TODO");
		}

		@DisplayName("(Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void vector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(createVector(10, -2, 0));
			/* TODO PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 7);
			assertElement(pi, PathElementType.LINE_TO, 15, 7);
			assertElement(pi, PathElementType.CLOSE, 15, 6);
			assertElement(pi, PathElementType.MOVE_TO, 7, 16);
			assertElement(pi, PathElementType.CURVE_TO, 7, 17.10457, 6.10457, 18, 5, 18);
			assertElement(pi, PathElementType.CURVE_TO, 3.89543, 18, 3, 17.10457, 3, 16);
			assertElement(pi, PathElementType.CURVE_TO, 3, 14.89543, 3.89543, 14, 5, 14);
			assertElement(pi, PathElementType.CURVE_TO, 6.10457, 14, 7, 14.89543, 7, 16);
			assertElement(pi, PathElementType.CLOSE, 7, 16);
			assertNoElement(pi);*/
			fail("TODO");
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
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
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
			assertEpsilonEquals(8, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(20, box.getMaxZ());
		}
	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

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
			assertTrue(getS().intersects(createAlignedBox(4.75, 8, 0, .5, .5, 0)));
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
			assertTrue(getS().intersects(createAlignedBox(5.5, 8.5, 0, .5, .5, 0)));
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
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().intersects(path));
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void path_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().intersects(path));
			path.closePath();
			assertTrue(getS().intersects(path));
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
			path.closePath();
			assertTrue(getS().intersects((PathIterator3afp) path.getPathIterator()));
		}

		@DisplayName("(Path3Ieratorafp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void pathiterator_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().intersects((PathIterator3afp) path.getPathIterator()));
			path.closePath();
			assertTrue(getS().intersects((PathIterator3afp) path.getPathIterator()));
		}

		@DisplayName("(Path3Ieratorafp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void pathiterator_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().intersects((PathIterator3afp) path.getPathIterator()));
			path.closePath();
			assertTrue(getS().intersects((PathIterator3afp) path.getPathIterator()));
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
			assertTrue(getS().intersects((Shape3D) path));
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
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
			getS().operator_add(createVector(10, -2, 0));
			/* TODO PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 6, 0);
			assertElement(pi, PathElementType.LINE_TO, 17, 6, 0);
			assertElement(pi, PathElementType.LINE_TO, 17, 7, 0);
			assertElement(pi, PathElementType.LINE_TO, 15, 7, 0);
			assertElement(pi, PathElementType.CLOSE, 15, 6, 0);
			assertElement(pi, PathElementType.MOVE_TO, 7, 16, 0);
			assertElement(pi, PathElementType.CURVE_TO, 7, 17.10457, 0, 6.10457, 18, 0, 5, 18, 0);
			assertElement(pi, PathElementType.CURVE_TO, 3.89543, 18, 0, 3, 17.10457, 0, 3, 16, 0);
			assertElement(pi, PathElementType.CURVE_TO, 3, 14.89543, 0, 3.89543, 14, 0, 5, 14, 0);
			assertElement(pi, PathElementType.CURVE_TO, 6.10457, 14, 0, 7, 14.89543, 0, 7, 16, 0);
			assertElement(pi, PathElementType.CLOSE, 7, 16, 0);
			assertNoElement(pi);*/
			fail("TODO");
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
			T shape = getS().operator_plus(createVector(10, -2, 0));
			/* TODO PathIterator3afp pi = shape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 6, 0);
			assertElement(pi, PathElementType.LINE_TO, 17, 6, 0);
			assertElement(pi, PathElementType.LINE_TO, 17, 7, 0);
			assertElement(pi, PathElementType.LINE_TO, 15, 7, 0);
			assertElement(pi, PathElementType.CLOSE, 15, 6, 0);
			assertElement(pi, PathElementType.MOVE_TO, 7, 16, 0);
			assertElement(pi, PathElementType.CURVE_TO, 7, 17.10457, 0, 6.10457, 18, 0, 5, 18, 0);
			assertElement(pi, PathElementType.CURVE_TO, 3.89543, 18, 0, 3, 17.10457, 0, 3, 16, 0);
			assertElement(pi, PathElementType.CURVE_TO, 3, 14.89543, 0, 3.89543, 14, 0, 5, 14, 0);
			assertElement(pi, PathElementType.CURVE_TO, 6.10457, 14, 0, 7, 14.89543, 0, 7, 16, 0);
			assertElement(pi, PathElementType.CLOSE, 7, 16, 0);
			assertNoElement(pi);*/
			fail("TODO");
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
			getS().operator_remove(createVector(10, -2, 0));
			/*TODO PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -5, 10, 0);
			assertElement(pi, PathElementType.LINE_TO, -3, 10, 0);
			assertElement(pi, PathElementType.LINE_TO, -3, 11, 0);
			assertElement(pi, PathElementType.LINE_TO, -5, 11, 0);
			assertElement(pi, PathElementType.CLOSE, -5, 10, 0);
			assertElement(pi, PathElementType.MOVE_TO, -13, 20, 0);
			assertElement(pi, PathElementType.CURVE_TO, -13, 21.10457, 0, -13.89543, 22, 0, -15, 22, 0);
			assertElement(pi, PathElementType.CURVE_TO, -16.10457, 22, 0, -17, 21.10457, 0, -17, 20, 0);
			assertElement(pi, PathElementType.CURVE_TO, -17, 18.89543, 0, -16.10457, 18, 0, -15, 18, 0);
			assertElement(pi, PathElementType.CURVE_TO, -13.89543, 18, 0, -13, 18.89543, 0, -13, 20, 0);
			assertElement(pi, PathElementType.CLOSE, -13, 20, 0);
			assertNoElement(pi);*/
			fail("TODO");
		}
	}

	@DisplayName("this -= Vector3D")
	@Nested
	public class OperatorMinusVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			T shape = getS().operator_minus(createVector(10, -2, 0));
			/* TODO PathIterator3afp pi = shape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -5, 10, 0);
			assertElement(pi, PathElementType.LINE_TO, -3, 10, 0);
			assertElement(pi, PathElementType.LINE_TO, -3, 11, 0);
			assertElement(pi, PathElementType.LINE_TO, -5, 11, 0);
			assertElement(pi, PathElementType.CLOSE, -5, 10, 0);
			assertElement(pi, PathElementType.MOVE_TO, -13, 20, 0);
			assertElement(pi, PathElementType.CURVE_TO, -13, 21.10457, 0, -13.89543, 22, 0, -15, 22, 0);
			assertElement(pi, PathElementType.CURVE_TO, -16.10457, 22, 0, -17, 21.10457, 0, -17, 20, 0);
			assertElement(pi, PathElementType.CURVE_TO, -17, 18.89543, 0, -16.10457, 18, 0, -15, 18, 0);
			assertElement(pi, PathElementType.CURVE_TO, -13.89543, 18, 0, -13, 18.89543, 0, -13, 20, 0);
			assertElement(pi, PathElementType.CLOSE, -13, 20, 0);
			assertNoElement(pi);*/
			fail("TODO");
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

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createSphere(4.75, 8, 0, .5)));
			Path3afp path = createPath();
			path.moveTo(-6, 2, 0);
			path.lineTo(10, 6, 0);
			path.lineTo(8, 14, 0);
			path.lineTo(-4, 12, 0);
			path.lineTo(-12, 22, 0);
			path.lineTo(6, 20, 0);
			assertFalse(getS().operator_and(path));
			path.closePath();
			assertTrue(getS().operator_and(path));
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
			assertSame(firstObject, shape3d.getFirstShapeIntersecting(path));
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
			assertEpsilonEquals(8, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(20, box.getMaxZ());
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
			assertEpsilonEquals(1, box.getMinZ());
			assertEpsilonEquals(19, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(20, box.getMaxZ());
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
			assertEpsilonEquals(8, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(20, box.getMaxZ());
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
			assertEpsilonEquals(8, box.getMinZ());
			assertEpsilonEquals(9, box.getMaxX());
			assertEpsilonEquals(13, box.getMaxY());
			assertEpsilonEquals(13, box.getMaxZ());
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
			assertEpsilonEquals(8, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(20, box.getMaxZ());
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
			assertEpsilonEquals(8, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(9, box.getMaxY());
			assertEpsilonEquals(9, box.getMaxZ());
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
			assertEpsilonEquals(8, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(20, box.getMaxZ());
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
			assertEpsilonEquals(8, box.getMinZ());
			assertEpsilonEquals(11, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(20, box.getMaxZ());
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
			assertEpsilonEquals(8, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(20, box.getMaxZ());
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
			assertEpsilonEquals(16, box.getMinZ());
			assertEpsilonEquals(-3, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(20, box.getMaxZ());
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
			assertEpsilonEquals(8, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
			assertEpsilonEquals(20, box.getMaxZ());
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
			assertEpsilonEquals(8, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(9, box.getMaxY());
			assertEpsilonEquals(9, box.getMaxZ());
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
