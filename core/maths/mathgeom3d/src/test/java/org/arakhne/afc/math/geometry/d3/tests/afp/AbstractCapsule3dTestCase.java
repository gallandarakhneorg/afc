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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Shape3D;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.Capsule3afp;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.general.Shape3DType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractCapsule3dTestCase<T extends Capsule3afp<?, T, ?, ?, ?, ?, B>,
		B extends AlignedBox3afp<?, ?, ?, ?, ?, B>> extends AbstractShape3dTestCase<T, B> {

	@Override
	protected final T createShape() {
		return (T) createCapsule(5, 8, 9, 1, 2, 3, 5);
	}

	@DisplayName("getType")
	@Nested
	public class GetType {

		@DisplayName("(Class) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void type_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(Shape3DType.CAPSULE, getS().getType(Shape3DType.class));
		}
	
		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(Shape3DType.CAPSULE, getS().getType());
		}

	}

	@DisplayName("set")
	@Nested
	public class Set {

		@DisplayName("(double,double,double,double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledoubledoubledoubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(1, 2, 3, 4, 5, 6, 7);
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP1());
			assertEpsilonEquals(createPoint(4, 5, 6), getS().getP2());
			assertEpsilonEquals(7, getS().getRadius());
		}

		@DisplayName("(Point3D,Point3D,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointdouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(createPoint(1, 2, 3), createPoint(4, 5, 6), 7);
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP1());
			assertEpsilonEquals(createPoint(4, 5, 6), getS().getP2());
			assertEpsilonEquals(7, getS().getRadius());
		}
	}

	@DisplayName("getX1")
	@Nested
	public class GetX1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getX1());
		}
	}

	@DisplayName("getY1")
	@Nested
	public class GetY1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8, getS().getY1());
		}
	}

	@DisplayName("getZ1")
	@Nested
	public class GetZ1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9, getS().getZ1());
		}
	}

	@DisplayName("getX2")
	@Nested
	public class GetX2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getX2());
		}
	}

	@DisplayName("getY2")
	@Nested
	public class GetY2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, getS().getY2());
		}
	}

	@DisplayName("getZ2")
	@Nested
	public class GetZ2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, getS().getZ2());
		}
	}

	@DisplayName("getRadius")
	@Nested
	public class GetRadius {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getRadius());
		}
	}

	@DisplayName("getP1")
	@Nested
	public class GetP1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
		}
	}

	@DisplayName("getP2")
	@Nested
	public class GetP2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
		}
	}

	@DisplayName("getInnerLength")
	@Nested
	public class GetInnerLength {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9.3808315196, getS().getInnerLength());
		}
	}

	@DisplayName("getInnerLengthSquared")
	@Nested
	public class GetInnerLengthSquared {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(88., getS().getInnerLengthSquared());
		}
	}

	@DisplayName("getOuterLength")
	@Nested
	public class GetOuterLength {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(19.3808315196, getS().getOuterLength());
		}
	}

	@DisplayName("setX1")
	@Nested
	public class SetX1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setX1(123.456);
			assertEpsilonEquals(createPoint(123.456, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}
	}

	@DisplayName("setY1")
	@Nested
	public class SetY1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setY1(123.456);
			assertEpsilonEquals(createPoint(5, 123.456, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}
	}

	@DisplayName("setZ1")
	@Nested
	public class SetZ1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setZ1(123.456);
			assertEpsilonEquals(createPoint(5, 8, 123.456), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}
	}

	@DisplayName("setX2")
	@Nested
	public class SetX2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setX2(123.456);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(123.456, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}
	}

	@DisplayName("setY2")
	@Nested
	public class SetY2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setY2(123.456);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 123.456, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}
	}

	@DisplayName("setZ2")
	@Nested
	public class SetZ2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setZ2(123.456);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 123.456), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}
	}

	@DisplayName("setRadius")
	@Nested
	public class SetRadius {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setRadius(123.456);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(123.456, getS().getRadius());
		}
	}

	@DisplayName("setP1")
	@Nested
	public class SetP1 {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP1(123.456, 951.357, 456.9631);
			assertEpsilonEquals(createPoint(123.456, 951.357, 456.9631), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP1(createPoint(123.456, 951.357, 456.9631));
			assertEpsilonEquals(createPoint(123.456, 951.357, 456.9631), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}
	}

	@DisplayName("setP2")
	@Nested
	public class SetP2 {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP2(123.456, 951.357, 456.9631);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(123.456, 951.357, 456.9631), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP2(createPoint(123.456, 951.357, 456.9631));
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(123.456, 951.357, 456.9631), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}
	}

	@DisplayName("clear")
	@Nested
	public class Clear {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getP1());
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getP2());
			assertEpsilonEquals(0, getS().getRadius());
		}

	}

	@DisplayName("toBoundingBox")
	@Nested
	public class ToBoundingBox {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var box = getS().toBoundingBox();
			assertEpsilonEquals(createPoint(-4, -3, -2), createPoint(box.getMinX(), box.getMinY(), box.getMinZ()));
			assertEpsilonEquals(createPoint(10, 13, 14), createPoint(box.getMaxX(), box.getMaxY(), box.getMaxZ()));
		}

		@DisplayName("(box) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var box = createAlignedBox(0, 0, 0, 0, 0, 0);
			getS().toBoundingBox(box);
			assertEpsilonEquals(createPoint(-4, -3, -2), createPoint(box.getMinX(), box.getMinY(), box.getMinZ()));
			assertEpsilonEquals(createPoint(10, 13, 14), createPoint(box.getMaxX(), box.getMaxY(), box.getMaxZ()));
		}

	}

	@DisplayName("clone")
	@Nested
	public class CloneTest {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			T clone = getS().clone();
			assertNotNull(clone);
			assertNotSame(getS(), clone);
			assertEquals(getS().getClass(), clone.getClass());
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, clone.getRadius());
		}

	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsObject {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(new Object()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSphere(0, 0, 0, 5)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createCapsule(5, 8, 9, 5, 2, 3, 5)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(createCapsule(5, 8, 9, 1, 2, 3, 5)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(createCapsule(1, 2, 3, 5, 8, 9, 5)));
		}
	}

	@DisplayName("equalsToShape")
	@Nested
	public class EqualsToShape {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) createCapsule(5, 8, 9, 5, 2, 3, 5)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape((T) createCapsule(5, 8, 9, 1, 2, 3, 5)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape((T) createCapsule(1, 2, 3, 5, 8, 9, 5)));
		}

	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(5, 8, 9));
		}

		@DisplayName("(double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(0, 0, 0));
		}

		@DisplayName("(double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-10, 0, 0));
		}

		@DisplayName("(double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_4(CoordinateSystem3D cs) {
			// point on segment endpoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Exactly on one endpoint of the inner segment => inside
			assertTrue(getS().contains(1, 2, 3));
		}

		@DisplayName("(double,double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_5(CoordinateSystem3D cs) {
			// point on other segment endpoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Exactly on the second endpoint of the inner segment => inside
			assertTrue(getS().contains(7, 10, 11));
		}

		@DisplayName("(double,double,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_6(CoordinateSystem3D cs) {
			// point on cylindrical side surface
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Point at exactly radius distance from the segment => boundary, inside
			assertTrue(getS().contains(4, 8, 7));
		}

		@DisplayName("(double,double,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_7(CoordinateSystem3D cs) {
			// point just outside cylindrical side
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Slightly farther than radius from the segment => outside
			assertFalse(getS().contains(5, 8, 14.001));
		}

		@DisplayName("(double,double,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_8(CoordinateSystem3D cs) {
			// point in spherical cap near first endpoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Outside segment projection but inside first end-cap sphere => inside
			assertTrue(getS().contains(0.5, 2, 3));
		}

		@DisplayName("(double,double,double) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_9(CoordinateSystem3D cs) {
			// point outside spherical cap near first endpoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Outside first end-cap sphere => outside
			assertFalse(getS().contains(-4.1, 2, 3));
		}

		@DisplayName("(double,double,double) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_10(CoordinateSystem3D cs) {
			// point just inside cylindrical side
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Slightly smaller than radius from the segment => inside
			assertTrue(getS().contains(5, 8, 13.999));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(5, 8, 9)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(-10, 0, 0)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			// point on segment endpoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Exactly on one endpoint of the inner segment => inside
			assertTrue(getS().contains(createPoint(1, 2, 3)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			// point on other segment endpoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Exactly on the second endpoint of the inner segment => inside
			assertTrue(getS().contains(createPoint(7, 10, 11)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			// point on cylindrical side surface
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Point at exactly radius distance from the segment => boundary, inside
			assertTrue(getS().contains(createPoint(4, 8, 7)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			// point just outside cylindrical side
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Slightly farther than radius from the segment => outside
			assertFalse(getS().contains(createPoint(5, 8, 14.001)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			// point in spherical cap near first endpoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Outside segment projection but inside first end-cap sphere => inside
			assertTrue(getS().contains(createPoint(0.5, 2, 3)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			// point outside spherical cap near first endpoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Outside first end-cap sphere => outside
			assertFalse(getS().contains(createPoint(-4.1, 2, 3)));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(5, 8, 9, 6, 9, 10)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBoxFromPoints(10, 0, 0, 11, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_3(CoordinateSystem3D cs) {
			// single point box on segment
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(5, 8, 9, 5, 8, 9)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_4(CoordinateSystem3D cs) {
			// box crossing capsule boundary
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBoxFromPoints(0, 0, 0, 6, 9, 10)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_5(CoordinateSystem3D cs) {
			// box in first spherical cap
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(0.6, 1.6, 2.6, 1.4, 2.4, 3.4)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(-0.5, 1.5, 2.5, 0.4, 2.4, 3.4)));
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_7(CoordinateSystem3D cs) {
			// tangent box (touching boundary) should be inside
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(4, 8, 7, 4.2, 8.2, 7.2)));
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(4, 8, 6.999, 4.1, 8.1, 7.099)));
		}

		@DisplayName("(AlignedBox3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_9(CoordinateSystem3D cs) {
			// box outside near first cap
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBoxFromPoints(-5.5, 1.5, 2.5, -4.1, 2.4, 3.4)));
		}

		@DisplayName("(AlignedBox3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_10(CoordinateSystem3D cs) {
			// very small box just outside boundary
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(0, 8, 6.999, 0.1, 8.1, 7.099)));
		}

		@DisplayName("(AlignedBox3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_11(CoordinateSystem3D cs) {
			// inside the cylinder but not in the spheres that are composed the capsule
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(0, 8, 6.999, 0.1, 8.1, 7.099)));
		}

		@DisplayName("(AlignedBox3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_12(CoordinateSystem3D cs) {
			// just outside the capsule
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(0, 8, 6.999, 0.1, 8.1, 7.099)));
		}

		@DisplayName("(AlignedBox3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_13(CoordinateSystem3D cs) {
			// just outside the capsule
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBoxFromPoints(-1, 8, 6.999, -0.9, 8.1, 7.099)));
		}

		@DisplayName("(AlignedBox3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_14(CoordinateSystem3D cs) {
			// just outside the capsule
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBoxFromPoints(-1.1, 8, 6.999, -1.0, 8.1, 7.099)));
		}
	}

	@DisplayName("transform")
	@Nested
	public class Transform {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			getS().transform(transform);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// translation
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setTranslation(10, -4, 7);
			getS().transform(transform);
			assertEpsilonEquals(createPoint(15, 4, 16), getS().getP1());
			assertEpsilonEquals(createPoint(11, -2, 10), getS().getP2());
			// translation must not change radius
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// uniform scale x2
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setDiagonal(2, 2, 2, 1);
			getS().transform(transform);
			assertEpsilonEquals(createPoint(10, 16, 18), getS().getP1());
			assertEpsilonEquals(createPoint(2, 4, 6), getS().getP2());
			assertEpsilonEquals(10, getS().getRadius()); // radius scales by uniform factor
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// uniform scale x0.5
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setDiagonal(.5, .5, .5, 1.);
			getS().transform(transform);
			assertEpsilonEquals(createPoint(2.5, 4, 4.5), getS().getP1());
			assertEpsilonEquals(createPoint(.5, 1, 1.5), getS().getP2());
			assertEpsilonEquals(2.5, getS().getRadius());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// rotation around Z by +90deg
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setRotation(factory.createAxisAngle(0., 0., 1., Math.PI / 2.));
			getS().transform(transform);
			// (x,y,z) -> (-y,x,z)
			assertEpsilonEquals(createPoint(-8, 5, 9), getS().getP1());
			assertEpsilonEquals(createPoint(-2, 1, 3), getS().getP2());
			// pure rotation keeps radius
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// translation + uniform scale
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.translate(1, 2, 3);
			transform.scale(3, 3, 3);
			getS().transform(transform);
			// Assuming same multiplication convention as Transform3D helpers used above.
			// If your API applies scale then translate, swap expected values accordingly.
			assertEpsilonEquals(createPoint(16, 26, 30), getS().getP1());
			assertEpsilonEquals(createPoint(4, 8, 12), getS().getP2());
			assertEpsilonEquals(15, getS().getRadius());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// mirror on X axis
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setDiagonal(-1, 1, 1, 1);
			getS().transform(transform);
			assertEpsilonEquals(createPoint(-5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(-1, 2, 3), getS().getP2());
			// abs(scale)=1
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			// translation + uniform scale
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.scale(3, 3, 3);
			transform.translate(1, 2, 3);
			getS().transform(transform);
			// Assuming same multiplication convention as Transform3D helpers used above.
			// If your API applies scale then translate, swap expected values accordingly.
			assertEpsilonEquals(createPoint(16, 26, 30), getS().getP1());
			assertEpsilonEquals(createPoint(4, 8, 12), getS().getP2());
			assertEpsilonEquals(15, getS().getRadius());
		}
	}

	@DisplayName("createTransformedShape")
	@Nested
	public class CreateTransformedShape {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			var shape = (Capsule3afp) getS().createTransformedShape(transform);
			
			assertNotSame(getS(), shape);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());

			assertEpsilonEquals(createPoint(5, 8, 9), shape.getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), shape.getP2());
			assertEpsilonEquals(5, shape.getRadius());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// translation
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setTranslation(10, -4, 7);
			var shape = (Capsule3afp) getS().createTransformedShape(transform);
			
			assertNotSame(getS(), shape);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());

			assertEpsilonEquals(createPoint(15, 4, 16), shape.getP1());
			assertEpsilonEquals(createPoint(11, -2, 10), shape.getP2());
			// translation must not change radius
			assertEpsilonEquals(5, shape.getRadius());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// uniform scale x2
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setDiagonal(2, 2, 2, 1);
			var shape = (Capsule3afp) getS().createTransformedShape(transform);
			
			assertNotSame(getS(), shape);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());

			assertEpsilonEquals(createPoint(10, 16, 18), shape.getP1());
			assertEpsilonEquals(createPoint(2, 4, 6), shape.getP2());
			// radius scales by uniform factor
			assertEpsilonEquals(10, shape.getRadius());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// uniform scale x0.5
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setDiagonal(.5, .5, .5, 1.);
			var shape = (Capsule3afp) getS().createTransformedShape(transform);
			
			assertNotSame(getS(), shape);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());

			assertEpsilonEquals(createPoint(2.5, 4, 4.5), shape.getP1());
			assertEpsilonEquals(createPoint(.5, 1, 1.5), shape.getP2());
			assertEpsilonEquals(2.5, shape.getRadius());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// rotation around Z by +90deg
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setRotation(factory.createAxisAngle(0., 0., 1., Math.PI / 2.));
			var shape = (Capsule3afp) getS().createTransformedShape(transform);
			
			assertNotSame(getS(), shape);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());

			assertEpsilonEquals(createPoint(-8, 5, 9), shape.getP1());
			assertEpsilonEquals(createPoint(-2, 1, 3), shape.getP2());
			// pure rotation keeps radius
			assertEpsilonEquals(5, shape.getRadius());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// translation + uniform scale
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.translate(1, 2, 3);
			transform.scale(3, 3, 3);
			var shape = (Capsule3afp) getS().createTransformedShape(transform);
			
			assertNotSame(getS(), shape);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());

			// Assuming same multiplication convention as Transform3D helpers used above.
			// If your API applies scale then translate, swap expected values accordingly.
			assertEpsilonEquals(createPoint(16, 26, 30), shape.getP1());
			assertEpsilonEquals(createPoint(4, 8, 12), shape.getP2());
			assertEpsilonEquals(15, shape.getRadius());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// mirror on X axis
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setDiagonal(-1, 1, 1, 1);
			var shape = (Capsule3afp) getS().createTransformedShape(transform);
			
			assertNotSame(getS(), shape);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());

			assertEpsilonEquals(createPoint(-5, 8, 9), shape.getP1());
			assertEpsilonEquals(createPoint(-1, 2, 3), shape.getP2());
			// abs(scale)=1
			assertEpsilonEquals(5, shape.getRadius());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			// translation + uniform scale
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.scale(3, 3, 3);
			transform.translate(1, 2, 3);
			var shape = (Capsule3afp) getS().createTransformedShape(transform);
			
			assertNotSame(getS(), shape);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());

			// Assuming same multiplication convention as Transform3D helpers used above.
			// If your API applies scale then translate, swap expected values accordingly.
			assertEpsilonEquals(createPoint(16, 26, 30), shape.getP1());
			assertEpsilonEquals(createPoint(4, 8, 12), shape.getP2());
			assertEpsilonEquals(15, shape.getRadius());
		}
	}

	@DisplayName("translate")
	@Nested
	public class Translate {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(0, 0, 0);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("(double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(-2, -3, -4);
			assertEpsilonEquals(createPoint(3, 5, 5), getS().getP1());
			assertEpsilonEquals(createPoint(-1, -1, -1), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("(double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(2.5, -1.5, 0.5);
			assertEpsilonEquals(createPoint(7.5, 6.5, 9.5), getS().getP1());
			assertEpsilonEquals(createPoint(3.5, 0.5, 3.5), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("(double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(1, 2, 3);
			getS().translate(4, 5, 6);
			assertEpsilonEquals(createPoint(10, 15, 18), getS().getP1());
			assertEpsilonEquals(createPoint(6, 9, 12), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("(double,double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(10, -4, 7);
			getS().translate(-10, 4, -7);
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("(double,double,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(1e6, -1e6, 1e6);
			assertEpsilonEquals(createPoint(1000005, -999992, 1000009), getS().getP1());
			assertEpsilonEquals(createPoint(1000001, -999998, 1000003), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("(Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(createVector(0, 0, 0));
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("(Vector3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vector_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(createVector(-2, -3, -4));
			assertEpsilonEquals(createPoint(3, 5, 5), getS().getP1());
			assertEpsilonEquals(createPoint(-1, -1, -1), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("(Vector3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vector_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(createVector(2.5, -1.5, 0.5));
			assertEpsilonEquals(createPoint(7.5, 6.5, 9.5), getS().getP1());
			assertEpsilonEquals(createPoint(3.5, 0.5, 3.5), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("(Vector3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vector_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(createVector(1, 2, 3));
			getS().translate(createVector(4, 5, 6));
			assertEpsilonEquals(createPoint(10, 15, 18), getS().getP1());
			assertEpsilonEquals(createPoint(6, 9, 12), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("(Vector3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vector_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(createVector(10, -4, 7));
			getS().translate(createVector(-10, 4, -7));
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("(Vector3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vector_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(createVector(1e6, -1e6, 1e6));
			assertEpsilonEquals(createPoint(1000005, -999992, 1000009), getS().getP1());
			assertEpsilonEquals(createPoint(1000001, -999998, 1000003), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}
	}

	@DisplayName("getFarthestPointTo")
	@Nested
	public class GetFarthestPointTo {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(-1.13200716356,-1.19801074533,-0.19801074533),
					getS().getFarthestPointTo(createPoint(5, 8, 9)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453),
					getS().getFarthestPointTo(createPoint(1, 2, 3)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query far in +X: farthest point should be on opposite cap side (near P2 - radius dirX)
			assertEpsilonEquals(createPoint(-4, 2, 3), getS().getFarthestPointTo(createPoint(100, 2, 3)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query far in -X: farthest point should be on opposite cap side (near P1 + radius dirX)
			assertEpsilonEquals(createPoint(10, 8, 9), getS().getFarthestPointTo(createPoint(-100, 8, 9)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query on segment midpoint: expect one extreme surface point along capsule axis
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453),
					getS().getFarthestPointTo(createPoint(3, 5, 6)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query very far in +Z
			assertEpsilonEquals(createPoint(0.98996997532, 1.98495496298, -1.99996730444),
					getS().getFarthestPointTo(createPoint(3, 5, 1000)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query very far in -Z
			assertEpsilonEquals(createPoint(5.0099107395, 8.0148661092, 13.999968078),
					getS().getFarthestPointTo(createPoint(3, 5, -1000)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly at capsule center should still return a valid surface extreme
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453),
					getS().getFarthestPointTo(createPoint(3, 5, 6)));
		}
	}

	@DisplayName("findsClosestPointToCapsulePathIterator")
	@Nested
	public class FindsClosestPointToCapsulePathIterator {

		private Point3D<?, ?, ?> resultOnCapsule;
		private Point3D<?, ?, ?> resultOnPath;

		@BeforeEach
		public void setUp() {
			resultOnCapsule = new InnerComputationPoint3D();
			resultOnPath = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.lineTo(23, 20, 27);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
			assertEpsilonEquals(createPoint(20, 20, 20), resultOnPath);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-23., -20., -27.);
			path.lineTo(-20., -20., -20.);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(-1.7536395823, -0.8847652767, -0.0158909711), resultOnCapsule);
			assertEpsilonEquals(createPoint(-20., -20., -20.), resultOnPath);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.quadTo(24., 22., 23., 30., 40., 50.);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
			assertEpsilonEquals(createPoint(20., 20., 20.), resultOnPath);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.curveTo(25., 20., 25., 27., 26., 30., 35., 40., 45.);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
			assertEpsilonEquals(createPoint(20., 20., 20.), resultOnPath);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(2.9, 4.9, -100.);
			path.lineTo(3.1, 5.1, 100.);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(3.0060069303, 5.0060069303, 6.006930291), resultOnCapsule);
			assertEpsilonEquals(createPoint(3.0060069303, 5.0060069303, 6.006930291), resultOnPath);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(3.0, 5.0, 6.0);
			path.lineTo(3.2, 5.2, 6.2);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(3.0,5.0,6.0), resultOnCapsule);
			assertEpsilonEquals(createPoint(3.0,5.0,6.0), resultOnPath);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(4.0, 8.0, 7.0);
			path.lineTo(4.0, 8.0, 20.0);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(4.0,8.0,8.538461538), resultOnCapsule);
			assertEpsilonEquals(createPoint(4.0,8.0,8.538461538), resultOnPath);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(9., 14., 15.);
			path.lineTo(11., 17., 18.);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453), resultOnCapsule);
			assertEpsilonEquals(createPoint(9., 14., 15.), resultOnPath);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-3., -4., -5.);
			path.lineTo(-6., -8.5, -9.5);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(-0.8569533818, -0.7854300727, -0.7139067635), resultOnCapsule);
			assertEpsilonEquals(createPoint(-3., -4., -5.), resultOnPath);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(1000000., 1000000., 1000000.);
			path.lineTo(1000010., 1000005., 1000002.);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(7.8867580817, 10.8867494214, 11.8867465347), resultOnCapsule);
			assertEpsilonEquals(createPoint(1000000., 1000000., 1000000.), resultOnPath);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(23., 20., 27.);
			path.lineTo(20., 20., 20.);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
			assertEpsilonEquals(createPoint(20., 20., 20.), resultOnPath);
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.lineTo(23., 20., 27.);
			path.lineTo(25., 30., 32.);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
			assertEpsilonEquals(createPoint(20., 20., 20.), resultOnPath);
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.quadTo(10., 10., 10., 2.9, 4.9, -100.);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(8.89734244, 10.7195789435, 7.4460977265), resultOnCapsule);
			assertEpsilonEquals(createPoint(14.3439902666, 14.52026851, 5.27447473709), resultOnPath);
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.curveTo(15., 15., 15., 10., 10., 10., 3.0, 5.0, 6.0);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(3.0,5.0,6.0), resultOnCapsule);
			assertEpsilonEquals(createPoint(3.0,5.0,6.0), resultOnPath);
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(8.0, 8.0, 9.0);
			path.lineTo(9.0, 9.0, 9.0);
			path.lineTo(8.5, 8.0, 10.0);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(8.0,8.0,9.0), resultOnCapsule);
			assertEpsilonEquals(createPoint(8.0,8.0,9.0), resultOnPath);
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-2.0, 2.0, 3.0);
			path.lineTo(-3.0, 1.0, 3.0);
			path.lineTo(-2.5, 2.0, 2.0);
			Capsule3afp.findsClosestPointToCapsulePathIterator(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					path.getPathIterator(),
					resultOnCapsule, resultOnPath);
			assertEpsilonEquals(createPoint(-2.0,2.0,3.0), resultOnCapsule);
			assertEpsilonEquals(createPoint(-2.0,2.0,3.0), resultOnPath);
		}
	}

	@DisplayName("findsClosestPointToCapsuleAlignedBox")
	@Nested
	public class FindsClosestPointToCapsuleAlignedBox {

		private Point3D<?, ?, ?> resultOnCapsule;
		private Point3D<?, ?, ?> resultOnBox;

		@BeforeEach
		public void setUp() {
			resultOnCapsule = new InnerComputationPoint3D();
			resultOnBox = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Capsule3afp.findsClosestPointToCapsuleAlignedBox(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					20., 20., 20., 23, 20, 27,
					resultOnCapsule, resultOnBox);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
			assertEpsilonEquals(createPoint(20, 20, 20), resultOnBox);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far opposite box
			Capsule3afp.findsClosestPointToCapsuleAlignedBox(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					-23., -20., -27., -20., -20., -20.,
					resultOnCapsule, resultOnBox);
			assertEpsilonEquals(createPoint(-1.7536395823, -0.8847652767, -0.0158909711), resultOnCapsule);
			assertEpsilonEquals(createPoint(-20., -20., -20.), resultOnBox);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate box (point) = point query
			Capsule3afp.findsClosestPointToCapsuleAlignedBox(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					20., 20., 20., 20., 20., 20.,
					resultOnCapsule, resultOnBox);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
			assertEpsilonEquals(createPoint(20., 20., 20.), resultOnBox);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Box intersects capsule cylinder region
			Capsule3afp.findsClosestPointToCapsuleAlignedBox(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					2.5, 4.5, 5.5, 3.5, 5.5, 6.5,
					resultOnCapsule, resultOnBox);
			assertEpsilonEquals(createPoint(3.0,5.0,6.0), resultOnCapsule);
			assertEpsilonEquals(createPoint(3.0,5.0,6.0), resultOnBox);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Box fully inside capsule
			Capsule3afp.findsClosestPointToCapsuleAlignedBox(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					3.0, 5.0, 6.0, 3.2, 5.2, 6.2,
					resultOnCapsule, resultOnBox);
			assertEpsilonEquals(createPoint(3.0666666666666664,5.1,6.1), resultOnCapsule);
			assertEpsilonEquals(createPoint(3.0666666666666664,5.1,6.1), resultOnBox);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Box tangent on lateral side (touching)
			Capsule3afp.findsClosestPointToCapsuleAlignedBox(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					4.0, 8.0, 7.0, 4.1, 8.1, 7.1,
					resultOnCapsule, resultOnBox);
			assertEpsilonEquals(createPoint(4.1,8.0,7.1), resultOnCapsule);
			assertEpsilonEquals(createPoint(4.1,8.0,7.1), resultOnBox);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P1 cap, outside
			Capsule3afp.findsClosestPointToCapsuleAlignedBox(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					9., 14., 15., 10., 15., 16.,
					resultOnCapsule, resultOnBox);
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453), resultOnCapsule);
			assertEpsilonEquals(createPoint(9., 14., 15.), resultOnBox);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P2 cap, outside
			Capsule3afp.findsClosestPointToCapsuleAlignedBox(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					-4., -5., -6., -3., -4., -5.,
					resultOnCapsule, resultOnBox);
			assertEpsilonEquals(createPoint(-0.8569533818, -0.7854300727, -0.7139067635), resultOnCapsule);
			assertEpsilonEquals(createPoint(-3., -4., -5.), resultOnBox);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Slim box crossing axis neighborhood (intersection)
			Capsule3afp.findsClosestPointToCapsuleAlignedBox(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					2.9, 4.9, -100., 3.1, 5.1, 100.,
					resultOnCapsule, resultOnBox);
			assertEpsilonEquals(createPoint(3.0,5.0,6.0), resultOnCapsule);
			assertEpsilonEquals(createPoint(3.0,5.0,6.0), resultOnBox);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Reversed min/max input order equivalent geometry (if API normalizes internally)
			assertThrows(AssertionError.class, () -> Capsule3afp.findsClosestPointToCapsuleAlignedBox(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					23., 20., 27., 20., 20., 20.,
					resultOnCapsule, resultOnBox));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far box (stability)
			Capsule3afp.findsClosestPointToCapsuleAlignedBox(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					1e6, 1e6, 1e6, 1e6 + 10., 1e6 + 5., 1e6 + 2.,
					resultOnCapsule, resultOnBox);
			assertEpsilonEquals(createPoint(7.886758081746495,10.886749421428949,11.886746534656433), resultOnCapsule);
			assertEpsilonEquals(createPoint(1e6, 1e6, 1e6), resultOnBox);
		}
	}

	@DisplayName("findsClosestPointToCapsuleTriangle")
	@Nested
	public class FindsClosestPointToCapsuleTriangle {

		private Point3D<?, ?, ?> resultOnCapsule;
		private Point3D<?, ?, ?> resultOnTriangle;

		@BeforeEach
		public void setUp() {
			resultOnCapsule = new InnerComputationPoint3D();
			resultOnTriangle = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Capsule3afp.findsClosestPointToCapsuleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					20., 20., 20., 23, 20, 27, 21, 30, 35,
					EPSILON,
					resultOnCapsule, resultOnTriangle);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
			assertEpsilonEquals(createPoint(20, 20, 20), resultOnTriangle);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far triangle on opposite side
			Capsule3afp.findsClosestPointToCapsuleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					-20., -20., -20., -23., -20., -27., -21., -30., -35.,
					EPSILON,
					resultOnCapsule, resultOnTriangle);
			assertEpsilonEquals(createPoint(-1.7536395823, -0.8847652767, -0.0158909711), resultOnCapsule);
			assertEpsilonEquals(createPoint(-20., -20., -20.), resultOnTriangle);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle intersects capsule
			Capsule3afp.findsClosestPointToCapsuleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					3., 5., -10., 3., 5., 20., 6., 8., 9.,
					EPSILON,
					resultOnCapsule, resultOnTriangle);
			assertEpsilonEquals(createPoint(3.0, 5.0, 6.0), resultOnCapsule);
			assertEpsilonEquals(createPoint(3.0, 5.0, 6.0), resultOnTriangle);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate triangle = point (20,20,20)
			Capsule3afp.findsClosestPointToCapsuleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					20., 20., 20., 20., 20., 20., 20., 20., 20.,
					EPSILON,
					resultOnCapsule, resultOnTriangle);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
			assertEpsilonEquals(createPoint(20., 20., 20.), resultOnTriangle);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate triangle = segment near capsule flank
			Capsule3afp.findsClosestPointToCapsuleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					10., 8., 9., 12., 8., 9., 12., 8., 9.,
					EPSILON,
					resultOnCapsule, resultOnTriangle);
			assertEpsilonEquals(createPoint(10., 8., 9.), resultOnCapsule);
			assertEpsilonEquals(createPoint(10., 8., 9.), resultOnTriangle);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle fully inside capsule
			Capsule3afp.findsClosestPointToCapsuleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					3., 5., 6., 3.5, 5.2, 6.2, 2.8, 4.9, 6.1,
					EPSILON,
					resultOnCapsule, resultOnTriangle);
			assertEpsilonEquals(createPoint(3., 5., 6.), resultOnCapsule);
			assertEpsilonEquals(createPoint(3., 5., 6.), resultOnTriangle);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle tangent to capsule at a boundary point
			Capsule3afp.findsClosestPointToCapsuleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					4., 8., 7., 4., 9., 7., 4., 8., 8.,
					EPSILON,
					resultOnCapsule, resultOnTriangle);
			assertEpsilonEquals(createPoint(4., 8., 8.), resultOnCapsule);
			assertEpsilonEquals(createPoint(4., 8., 8.), resultOnTriangle);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle near P1 cap
			Capsule3afp.findsClosestPointToCapsuleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					9., 14., 15., 10., 15., 16., 9., 15., 15.,
					EPSILON,
					resultOnCapsule, resultOnTriangle);
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453), resultOnCapsule);
			assertEpsilonEquals(createPoint(9., 14., 15.), resultOnTriangle);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle near P2 cap
			Capsule3afp.findsClosestPointToCapsuleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					-3., -4., -5., -4., -5., -6., -2.5, -4.5, -5.2,
					EPSILON,
					resultOnCapsule, resultOnTriangle);
			assertEpsilonEquals(createPoint(-0.8569533818, -0.7854300727, -0.7139067635), resultOnCapsule);
			assertEpsilonEquals(createPoint(-3., -4., -5.), resultOnTriangle);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Vertex order permutation (same geometry as #1)
			Capsule3afp.findsClosestPointToCapsuleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					21., 30., 35., 23., 20., 27., 20., 20., 20.,
					EPSILON,
					resultOnCapsule, resultOnTriangle);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
			assertEpsilonEquals(createPoint(20., 20., 20.), resultOnTriangle);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far triangle (numeric stability)
			Capsule3afp.findsClosestPointToCapsuleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					1e6, 1e6, 1e6, 1e6 + 10., 1e6, 1e6, 1e6, 1e6 + 10., 1e6,
					EPSILON,
					resultOnCapsule, resultOnTriangle);
			assertEpsilonEquals(createPoint(7.8867580817, 10.8867494214, 11.8867465347), resultOnCapsule);
			assertEpsilonEquals(createPoint(1e6, 1e6, 1e6), resultOnTriangle);
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			// intersects only sphere at P1 (cap), not cylinder nor P2 cap
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P1=(5,8,9), far from axis around middle and far from P2 cap
			Capsule3afp.findsClosestPointToCapsuleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					8.0, 8.0, 9.0,
					9.0, 15.0, 9.0,
					15, 8.0, 10.0,
					EPSILON,
					resultOnCapsule, resultOnTriangle);
			assertEpsilonEquals(createPoint(8.0,8.0,9.0), resultOnCapsule);
			assertEpsilonEquals(createPoint(8.0,8.0,9.0), resultOnTriangle);
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			// intersects only sphere at P2 (cap), not cylinder nor P1 cap
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P2=(1,2,3), far from axis around middle and far from P1 cap
			Capsule3afp.findsClosestPointToCapsuleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					-20.0, 2.0, 3.0,
					-3.0, 1.0, 3.0,
					-2.5, -10.0, 2.0,
					EPSILON,
					resultOnCapsule, resultOnTriangle);
			assertEpsilonEquals(createPoint(-3.0,1.0,3.0), resultOnCapsule);
			assertEpsilonEquals(createPoint(-3.0,1.0,3.0), resultOnTriangle);
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			// intersects only capsule cylinder (middle), not the two spherical caps
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Around midpoint M=(3,5,6), plane x=3 crossing lateral tube region
			// Vertices chosen away from both endpoints to avoid cap intersections.
			Capsule3afp.findsClosestPointToCapsuleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					5, 15, 6.0,
					-0.4, 18.0, 6.0,
					-0.4, 7.8, 5.8,
					EPSILON,
					resultOnCapsule, resultOnTriangle);
			assertEpsilonEquals(createPoint(-0.4, 7.8, 5.8), resultOnCapsule);
			assertEpsilonEquals(createPoint(-0.4, 7.8, 5.8), resultOnTriangle);
		}
	}

	@DisplayName("findsClosestPointToCapsuleSegment")
	@Nested
	public class FindsClosestPointToCapsuleSegment {

		private Point3D<?, ?, ?> resultOnCapsule;
		private Point3D<?, ?, ?> resultOnSegment;

		@BeforeEach
		public void setUp() {
			resultOnCapsule = new InnerComputationPoint3D();
			resultOnSegment = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Capsule3afp.findsClosestPointToCapsuleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					20., 20., 20., 23, 20, 27,
					resultOnCapsule, resultOnSegment);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
			assertEpsilonEquals(createPoint(20, 20, 20), resultOnSegment);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far segment on opposite side
			Capsule3afp.findsClosestPointToCapsuleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					-23., -20., -27., -20., -20., -20.,
					resultOnCapsule, resultOnSegment);
			assertEpsilonEquals(createPoint(-1.7536395823, -0.8847652767, -0.0158909711), resultOnCapsule);
			assertEpsilonEquals(createPoint(-20., -20., -20.), resultOnSegment);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment crossing the capsule: closest distance must be zero
			Capsule3afp.findsClosestPointToCapsuleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					3., 5., -100., 3., 5., 100.,
					resultOnCapsule, resultOnSegment);
			assertEpsilonEquals(createPoint(3.0, 5.0, 6.), resultOnCapsule);
			assertEpsilonEquals(createPoint(3.0, 5.0, 6.), resultOnSegment);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate segment (point) -> equivalent to point query
			Capsule3afp.findsClosestPointToCapsuleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					20., 20., 20., 20., 20., 20.,
					resultOnCapsule, resultOnSegment);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
			assertEpsilonEquals(createPoint(20., 20., 20.), resultOnSegment);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment endpoint at P1 center: nearest capsule point is on cap around P1
			Capsule3afp.findsClosestPointToCapsuleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					5., 8., 9., 8., 8., 9.,
					resultOnCapsule, resultOnSegment);
			assertEpsilonEquals(createPoint(5., 8., 9.), resultOnCapsule);
			assertEpsilonEquals(createPoint(5., 8., 9.), resultOnSegment);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far segment (numeric stability)
			Capsule3afp.findsClosestPointToCapsuleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					1e6, 1e6, 1e6, 1e6 + 10., 1e6 + 5., 1e6 + 2.,
					resultOnCapsule, resultOnSegment);
			assertEpsilonEquals(createPoint(7.8867580817, 10.8867494214, 11.8867465347), resultOnCapsule);
			assertEpsilonEquals(createPoint(1000000, 1000000, 1000000), resultOnSegment);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment fully inside capsule
			Capsule3afp.findsClosestPointToCapsuleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					3., 5., 6., 4., 6., 7.,
					resultOnCapsule, resultOnSegment);
			assertEpsilonEquals(createPoint(3., 5., 6.), resultOnCapsule);
			assertEpsilonEquals(createPoint(3., 5., 6.), resultOnSegment);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment tangent (touches capsule at one point)
			Capsule3afp.findsClosestPointToCapsuleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					4., 8., 7., 4., 8., 20.,
					resultOnCapsule, resultOnSegment);
			assertEpsilonEquals(createPoint(4.0, 8.0, 8.5384615385), resultOnCapsule);
			assertEpsilonEquals(createPoint(4.0, 8.0, 8.5384615385), resultOnSegment);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query segment endpoint near capsule flank
			Capsule3afp.findsClosestPointToCapsuleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					10., 8., 9., 12., 8., 9.,
					resultOnCapsule, resultOnSegment);
			assertEpsilonEquals(createPoint(10., 8., 9.), resultOnCapsule);
			assertEpsilonEquals(createPoint(10., 8., 9.), resultOnSegment);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P1 side
			Capsule3afp.findsClosestPointToCapsuleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					9., 14., 15., 11., 17., 18.,
					resultOnCapsule, resultOnSegment);
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453), resultOnCapsule);
			assertEpsilonEquals(createPoint(9., 14., 15.), resultOnSegment);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P2 side
			Capsule3afp.findsClosestPointToCapsuleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					-3., -4., -5., -6., -8.5, -9.5,
					resultOnCapsule, resultOnSegment);
			assertEpsilonEquals(createPoint(-0.8569533818, -0.7854300727, -0.7139067635), resultOnCapsule);
			assertEpsilonEquals(createPoint(-3., -4., -5.), resultOnSegment);
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Reversed segment endpoints should not change geometric distance
			Capsule3afp.findsClosestPointToCapsuleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					23., 20., 27., 20., 20., 20.,
					resultOnCapsule, resultOnSegment);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
			assertEpsilonEquals(createPoint(20., 20., 20.), resultOnSegment);
		}
	}

	@DisplayName("findsClosestPointToCapsuleCapsule")
	@Nested
	public class FindsClosestPointToCapsuleCapsule {

		private Point3D<?, ?, ?> resultOnCapsule1;
		private Point3D<?, ?, ?> resultOnCapsule2;

		@BeforeEach
		public void setUp() {
			resultOnCapsule1 = new InnerComputationPoint3D();
			resultOnCapsule2 = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Capsule3afp.findsClosestPointToCapsuleCapsule(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					20., 20., 20., 23, 20, 27, 1.,
					resultOnCapsule1, resultOnCapsule2);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule1);
			assertEpsilonEquals(createPoint(19.3223690728, 19.4578952583, 19.5030706534), resultOnCapsule2);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far segment on opposite side
			Capsule3afp.findsClosestPointToCapsuleCapsule(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					-23., -20., -27., -20., -20., -20., 1.,
					resultOnCapsule1, resultOnCapsule2);
			assertEpsilonEquals(createPoint(-1.7536395823, -0.8847652767, -0.0158909711), resultOnCapsule1);
			assertEpsilonEquals(createPoint(-19.4492720835, -19.4230469447, -19.3968218058), resultOnCapsule2);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment crossing the capsule: closest distance must be zero
			Capsule3afp.findsClosestPointToCapsuleCapsule(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					3., 5., -100., 3., 5., 100., 1.,
					resultOnCapsule1, resultOnCapsule2);
			assertEpsilonEquals(createPoint(3.0, 5.0, 6.), resultOnCapsule1);
			assertEpsilonEquals(createPoint(3.0, 5.0, 6.), resultOnCapsule2);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate segment (point) -> equivalent to point query
			Capsule3afp.findsClosestPointToCapsuleCapsule(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					20., 20., 20., 20., 20., 20., 1.,
					resultOnCapsule1, resultOnCapsule2);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule1);
			assertEpsilonEquals(createPoint(19.3223690728, 19.4578952583, 19.5030706534), resultOnCapsule2);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment endpoint at P1 center: nearest capsule point is on cap around P1
			Capsule3afp.findsClosestPointToCapsuleCapsule(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					5., 8., 9., 8., 8., 9., 1.,
					resultOnCapsule1, resultOnCapsule2);
			assertEpsilonEquals(createPoint(5., 8., 9.), resultOnCapsule1);
			assertEpsilonEquals(createPoint(5., 8., 9.), resultOnCapsule2);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far segment (numeric stability)
			Capsule3afp.findsClosestPointToCapsuleCapsule(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					1e6, 1e6, 1e6, 1e6 + 10., 1e6 + 5., 1e6 + 2., 1.,
					resultOnCapsule1, resultOnCapsule2);
			assertEpsilonEquals(createPoint(7.8867580817, 10.8867494214, 11.8867465347), resultOnCapsule1);
			assertEpsilonEquals(createPoint(999999.4226483837,999999.4226501157,999999.4226506931), resultOnCapsule2);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment fully inside capsule
			Capsule3afp.findsClosestPointToCapsuleCapsule(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					3., 5., 6., 4., 6., 7., 1.,
					resultOnCapsule1, resultOnCapsule2);
			assertEpsilonEquals(createPoint(3., 5., 6.), resultOnCapsule1);
			assertEpsilonEquals(createPoint(3., 5., 6.), resultOnCapsule2);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment tangent (touches capsule at one point)
			Capsule3afp.findsClosestPointToCapsuleCapsule(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					4., 8., 7., 4., 8., 20., 1.,
					resultOnCapsule1, resultOnCapsule2);
			assertEpsilonEquals(createPoint(4.0, 8.0, 8.5384615385), resultOnCapsule1);
			assertEpsilonEquals(createPoint(4.0, 8.0, 8.5384615385), resultOnCapsule2);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query segment endpoint near capsule flank
			Capsule3afp.findsClosestPointToCapsuleCapsule(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					10., 8., 9., 12., 8., 9., 1.,
					resultOnCapsule1, resultOnCapsule2);
			assertEpsilonEquals(createPoint(10., 8., 9.), resultOnCapsule1);
			assertEpsilonEquals(createPoint(10., 8., 9.), resultOnCapsule2);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P1 side
			Capsule3afp.findsClosestPointToCapsuleCapsule(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					9., 14., 15., 11., 17., 18., 1.,
					resultOnCapsule1, resultOnCapsule2);
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453), resultOnCapsule1);
			assertEpsilonEquals(createPoint(8.5735985673, 13.3603978509, 14.3603978509), resultOnCapsule2);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P2 side
			Capsule3afp.findsClosestPointToCapsuleCapsule(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					-3., -4., -5., -6., -8.5, -9.5, 1.,
					resultOnCapsule1, resultOnCapsule2);
			assertEpsilonEquals(createPoint(-0.8569533818, -0.7854300727, -0.7139067635), resultOnCapsule1);
			assertEpsilonEquals(createPoint(-2.6286093236, -3.4429139855, -4.2572186473), resultOnCapsule2);
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Reversed segment endpoints should not change geometric distance
			Capsule3afp.findsClosestPointToCapsuleCapsule(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					23., 20., 27., 20., 20., 20., 1.,
					resultOnCapsule1, resultOnCapsule2);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule1);
			assertEpsilonEquals(createPoint(19.3223690728, 19.4578952583, 19.5030706534), resultOnCapsule2);
		}
	}

	@DisplayName("findsClosestPointToCapsuleSphere")
	@Nested
	public class FindsClosestPointToCapsuleSphere {

		private Point3D<?, ?, ?> resultOnCapsule;
		private Point3D<?, ?, ?> resultOnSphere;

		@BeforeEach
		public void setUp() {
			resultOnCapsule = new InnerComputationPoint3D();
			resultOnSphere = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Capsule3afp.findsClosestPointToCapsuleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					20., 20., 20., 6.,
					resultOnCapsule, resultOnSphere);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
			assertEpsilonEquals(createPoint(15.9342144369, 16.7473715495, 17.0184239204), resultOnSphere);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Capsule3afp.findsClosestPointToCapsuleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					-20., -20., -20., 6.,
					resultOnCapsule, resultOnSphere);
			assertEpsilonEquals(createPoint(-1.7536395823, -0.8847652767, -0.0158909711), resultOnCapsule);
			assertEpsilonEquals(createPoint(-16.6956325013, -16.538281668, -16.3809308347), resultOnSphere);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere center exactly at P1
			Capsule3afp.findsClosestPointToCapsuleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					getS().getX1(), getS().getY1(), getS().getZ1(), 2.,
					resultOnCapsule, resultOnSphere);
			assertEpsilonEquals(createPoint(5, 8, 9), resultOnCapsule);
			assertEpsilonEquals(createPoint(5, 8, 9), resultOnSphere);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere far on +X axis
			Capsule3afp.findsClosestPointToCapsuleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					100., 2., 3., 10.,
					resultOnCapsule, resultOnSphere);
			assertEpsilonEquals(createPoint(9.9801739499, 7.6854626979, 8.6854626979), resultOnCapsule);
			assertEpsilonEquals(createPoint(90.0396521, 2.6290746042, 3.6290746042), resultOnSphere);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Intersecting configuration (distance between surfaces = 0 expected if clamped)
			Capsule3afp.findsClosestPointToCapsuleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					6., 8., 9., 4.,
					resultOnCapsule, resultOnSphere);
			assertEpsilonEquals(createPoint(6, 8, 9), resultOnCapsule);
			assertEpsilonEquals(createPoint(6, 8, 9), resultOnSphere);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere tangent externally to the capsule (distance between surfaces = 0)
			Capsule3afp.findsClosestPointToCapsuleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					20., 20., 20., 17.1359436212 - 5.,
					resultOnCapsule, resultOnSphere);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
			assertEpsilonEquals(createPoint(11.7763092718, 13.4210474174, 13.969293466), resultOnSphere);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere center at P2
			Capsule3afp.findsClosestPointToCapsuleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					getS().getX2(), getS().getY2(), getS().getZ2(), 3.,
					resultOnCapsule, resultOnSphere);
			assertEpsilonEquals(createPoint(1, 2, 3), resultOnCapsule);
			assertEpsilonEquals(createPoint(1, 2, 3), resultOnSphere);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far query sphere center (stability + expected large separation)
			Capsule3afp.findsClosestPointToCapsuleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					1000., 1000., 1000., 1.,
					resultOnCapsule, resultOnSphere);
			assertEpsilonEquals(createPoint(7.893532618, 10.884808399, 11.8819003261), resultOnCapsule);
			assertEpsilonEquals(createPoint(999.4212934764, 999.4230383202, 999.4236199348), resultOnSphere);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Zero-radius sphere (point sphere) => should match capsule-point closest result
			Capsule3afp.findsClosestPointToCapsuleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					20., 20., 20., 0.,
					resultOnCapsule, resultOnSphere);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
			assertEpsilonEquals(createPoint(20., 20., 20.), resultOnSphere);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere that fully contains capsule endpoint region; closest surfaces overlap
			Capsule3afp.findsClosestPointToCapsuleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					5., 8., 9., 50.,
					resultOnCapsule, resultOnSphere);
			assertEpsilonEquals(createPoint(5.0, 8.0, 9.0), resultOnCapsule);
			assertEpsilonEquals(createPoint(5.0, 8.0, 9.0), resultOnSphere);
		}
	}

	@DisplayName("findsClosestPointToCapsulePoint")
	@Nested
	public class FindsClosestPointToCapsulePoint {

		private Point3D<?, ?, ?> result;

		@BeforeEach
		public void setUp() {
			result = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Capsule3afp.findsClosestPointToCapsulePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					20., 20., 20.,
					result);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), result);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly at P1 center: closest point on surface is along -axis direction from P1
			Capsule3afp.findsClosestPointToCapsulePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					getS().getX1(), getS().getY1(), getS().getZ1(),
					result);
			assertEpsilonEquals(createPoint(getS().getX1(), getS().getY1(), getS().getZ1()), result);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly at P2 center: closest point on surface is along +axis direction from P2
			Capsule3afp.findsClosestPointToCapsulePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					result);
			assertEpsilonEquals(createPoint(getS().getX2(), getS().getY2(), getS().getZ2()), result);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query on segment midpoint; closest point must be at radius distance from axis
			Capsule3afp.findsClosestPointToCapsulePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					3., 5., 6.,
					result);
			assertEpsilonEquals(createPoint(3, 5, 6), result);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query clearly inside capsule: returned closest point is on boundary and at positive distance
			Capsule3afp.findsClosestPointToCapsulePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					3., 5., 7.,
					result);
			assertEpsilonEquals(createPoint(3, 5, 7), result);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far opposite direction from #1 to cover other cap
			Capsule3afp.findsClosestPointToCapsulePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					-20., -20., -20.,
					result);
			assertEpsilonEquals(createPoint(-1.7536395823, -0.8847652767, -0.0158909711), result);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on boundary (known point from #1 expected): closest point should be itself.
			Capsule3afp.findsClosestPointToCapsulePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					8.3881546359, 10.7105237087, 11.4846467330,
					result);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), result);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on the segment line, beyond P1 side
			Capsule3afp.findsClosestPointToCapsulePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					9., 14., 15.,
					result);
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453), result);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on the segment line, beyond P2 side
			Capsule3afp.findsClosestPointToCapsulePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					-3., -4., -5.,
					result);
			assertEpsilonEquals(createPoint(-0.8569533818, -0.7854300727, -0.7139067635), result);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Huge coordinates: numerical stability smoke test
			Capsule3afp.findsClosestPointToCapsulePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					1e9, -1e9, 1e9,
					result);
			assertEpsilonEquals(createPoint(7.8867513373, 5.1132486252, 11.8867513257), result);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Returned point should always be on/in capsule boundary set (inside by convention)
			Capsule3afp.findsClosestPointToCapsulePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getRadius(),
					30.25, -12.5, 6.75,
					result);
			assertEpsilonEquals(createPoint(7.534369947, 2.7878880771, 6.6058652915), result);
		}
	}
	
	@DisplayName("getClosestPointTo")
	@Nested
	public class GetClosestPointTo {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getClosestPointTo(createPoint(20., 20., 20.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), result);
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly at P1 center: closest point on surface is along -axis direction from P1
			var result = getS().getClosestPointTo(createPoint(getS().getX1(), getS().getY1(), getS().getZ1()));
			assertEpsilonEquals(createPoint(getS().getX1(), getS().getY1(), getS().getZ1()), result);
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly at P2 center: closest point on surface is along +axis direction from P2
			var result = getS().getClosestPointTo(createPoint(getS().getX2(), getS().getY2(), getS().getZ2()));
			assertEpsilonEquals(createPoint(getS().getX2(), getS().getY2(), getS().getZ2()), result);
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query on segment midpoint; closest point must be at radius distance from axis
			var result = getS().getClosestPointTo(createPoint(3., 5., 6.));
			assertEpsilonEquals(createPoint(3, 5, 6), result);
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query clearly inside capsule: returned closest point is on boundary and at positive distance
			var result = getS().getClosestPointTo(createPoint(3., 5., 7.));
			assertEpsilonEquals(createPoint(3, 5, 7), result);
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far opposite direction from #1 to cover other cap
			var result = getS().getClosestPointTo(createPoint(-20., -20., -20.));
			assertEpsilonEquals(createPoint(-1.7536395823, -0.8847652767, -0.0158909711), result);
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on boundary (known point from #1 expected): closest point should be itself.
			var result = getS().getClosestPointTo(createPoint(8.3881546359, 10.7105237087, 11.4846467330));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), result);
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on the segment line, beyond P1 side
			var result = getS().getClosestPointTo(createPoint(9., 14., 15.));
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453), result);
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on the segment line, beyond P2 side
			var result = getS().getClosestPointTo(createPoint(-3., -4., -5.));
			assertEpsilonEquals(createPoint(-0.8569533818, -0.7854300727, -0.7139067635), result);
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Huge coordinates: numerical stability smoke test
			var result = getS().getClosestPointTo(createPoint(1e9, -1e9, 1e9));
			assertEpsilonEquals(createPoint(7.8867513373, 5.1132486252, 11.8867513257), result);
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Returned point should always be on/in capsule boundary set (inside by convention)
			var result = getS().getClosestPointTo(createPoint(30.25, -12.5, 6.75));
			assertEpsilonEquals(createPoint(7.534369947, 2.7878880771, 6.6058652915), result);
		}

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultOnCapsule = getS().getClosestPointTo(createTriangle(20., 20., 20., 23, 20, 27, 21, 30, 35));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
		}

		@DisplayName("(Triangle3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far triangle on opposite side
			var resultOnCapsule = getS().getClosestPointTo(createTriangle(-20., -20., -20., -23., -20., -27., -21., -30., -35.));
			assertEpsilonEquals(createPoint(-1.7536395823, -0.8847652767, -0.0158909711), resultOnCapsule);
		}

		@DisplayName("(Triangle3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle intersects capsule
			var resultOnCapsule = getS().getClosestPointTo(createTriangle(3., 5., -10., 3., 5., 20., 6., 8., 9.));
			assertEpsilonEquals(createPoint(3.0, 5.0, 6.0), resultOnCapsule);
		}

		@DisplayName("(Triangle3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate triangle = point (20,20,20)
			var resultOnCapsule = getS().getClosestPointTo(createTriangle(20., 20., 20., 20., 20., 20., 20., 20., 20.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
		}

		@DisplayName("(Triangle3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate triangle = segment near capsule flank
			var resultOnCapsule = getS().getClosestPointTo(createTriangle(10., 8., 9., 12., 8., 9., 12., 8., 9.));
			assertEpsilonEquals(createPoint(10., 8., 9.), resultOnCapsule);
		}

		@DisplayName("(Triangle3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle fully inside capsule
			var resultOnCapsule = getS().getClosestPointTo(createTriangle(3., 5., 6., 3.5, 5.2, 6.2, 2.8, 4.9, 6.1));
			assertEpsilonEquals(createPoint(3., 5., 6.), resultOnCapsule);
		}

		@DisplayName("(Triangle3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle tangent to capsule at a boundary point
			var resultOnCapsule = getS().getClosestPointTo(createTriangle(4., 8., 7., 4., 9., 7., 4., 8., 8.));
			assertEpsilonEquals(createPoint(4., 8., 8.), resultOnCapsule);
		}

		@DisplayName("(Triangle3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle near P1 cap
			var resultOnCapsule = getS().getClosestPointTo(createTriangle(9., 14., 15., 10., 15., 16., 9., 15., 15.));
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453), resultOnCapsule);
		}

		@DisplayName("(Triangle3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle near P2 cap
			var resultOnCapsule = getS().getClosestPointTo(createTriangle(-3., -4., -5., -4., -5., -6., -2.5, -4.5, -5.2));
			assertEpsilonEquals(createPoint(-0.8569533818, -0.7854300727, -0.7139067635), resultOnCapsule);
		}

		@DisplayName("(Triangle3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Vertex order permutation (same geometry as #1)
			var resultOnCapsule = getS().getClosestPointTo(createTriangle(21., 30., 35., 23., 20., 27., 20., 20., 20.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
		}

		@DisplayName("(Triangle3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far triangle (numeric stability)
			var resultOnCapsule = getS().getClosestPointTo(createTriangle(1e6, 1e6, 1e6, 1e6 + 10., 1e6, 1e6, 1e6, 1e6 + 10., 1e6));
			assertEpsilonEquals(createPoint(7.8867580817, 10.8867494214, 11.8867465347), resultOnCapsule);
		}

		@DisplayName("(Triangle3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_12(CoordinateSystem3D cs) {
			// intersects only sphere at P1 (cap), not cylinder nor P2 cap
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P1=(5,8,9), far from axis around middle and far from P2 cap
			var resultOnCapsule = getS().getClosestPointTo(createTriangle(8.0, 8.0, 9.0, 9.0, 15.0, 9.0, 15, 8.0, 10.0));
			assertEpsilonEquals(createPoint(8.0,8.0,9.0), resultOnCapsule);
		}

		@DisplayName("(Triangle3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_13(CoordinateSystem3D cs) {
			// intersects only sphere at P2 (cap), not cylinder nor P1 cap
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P2=(1,2,3), far from axis around middle and far from P1 cap
			var resultOnCapsule = getS().getClosestPointTo(createTriangle(-20.0, 2.0, 3.0, -3.0, 1.0, 3.0, -2.5, -10.0, 2.0));
			assertEpsilonEquals(createPoint(-3.0,1.0,3.0), resultOnCapsule);
		}

		@DisplayName("(Triangle3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_14(CoordinateSystem3D cs) {
			// intersects only capsule cylinder (middle), not the two spherical caps
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Around midpoint M=(3,5,6), plane x=3 crossing lateral tube region
			// Vertices chosen away from both endpoints to avoid cap intersections.
			var resultOnCapsule = getS().getClosestPointTo(createTriangle(5, 15, 6.0, -0.4, 18.0, 6.0, -0.4, 7.8, 5.8));
			assertEpsilonEquals(createPoint(-0.4, 7.8, 5.8), resultOnCapsule);
		}

		@DisplayName("(Capsule3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultOnCapsule1 = getS().getClosestPointTo(createCapsule(20., 20., 20., 23, 20, 27, 1.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule1);
		}

		@DisplayName("(Capsule3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far segment on opposite side
			var resultOnCapsule1 = getS().getClosestPointTo(createCapsule(-23., -20., -27., -20., -20., -20., 1.));
			assertEpsilonEquals(createPoint(-1.7536395823, -0.8847652767, -0.0158909711), resultOnCapsule1);
		}

		@DisplayName("(Capsule3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment crossing the capsule: closest distance must be zero
			var resultOnCapsule1 = getS().getClosestPointTo(createCapsule(3., 5., -100., 3., 5., 100., 1.));
			assertEpsilonEquals(createPoint(3.0, 5.0, 6.), resultOnCapsule1);
		}

		@DisplayName("(Capsule3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate segment (point) -> equivalent to point query
			var resultOnCapsule1 = getS().getClosestPointTo(createCapsule(20., 20., 20., 20., 20., 20., 1.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule1);
		}

		@DisplayName("(Capsule3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment endpoint at P1 center: nearest capsule point is on cap around P1
			var resultOnCapsule1 = getS().getClosestPointTo(createCapsule(5., 8., 9., 8., 8., 9., 1.));
			assertEpsilonEquals(createPoint(5., 8., 9.), resultOnCapsule1);
		}

		@DisplayName("(Capsule3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far segment (numeric stability)
			var resultOnCapsule1 = getS().getClosestPointTo(createCapsule(1e6, 1e6, 1e6, 1e6 + 10., 1e6 + 5., 1e6 + 2., 1.));
			assertEpsilonEquals(createPoint(7.8867580817, 10.8867494214, 11.8867465347), resultOnCapsule1);
		}

		@DisplayName("(Capsule3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment fully inside capsule
			var resultOnCapsule1 = getS().getClosestPointTo(createCapsule(3., 5., 6., 4., 6., 7., 1.));
			assertEpsilonEquals(createPoint(3., 5., 6.), resultOnCapsule1);
		}

		@DisplayName("(Capsule3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment tangent (touches capsule at one point)
			var resultOnCapsule1 = getS().getClosestPointTo(createCapsule(4., 8., 7., 4., 8., 20., 1.));
			assertEpsilonEquals(createPoint(4.0, 8.0, 8.5384615385), resultOnCapsule1);
		}

		@DisplayName("(Capsule3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query segment endpoint near capsule flank
			var resultOnCapsule1 = getS().getClosestPointTo(createCapsule(10., 8., 9., 12., 8., 9., 1.));
			assertEpsilonEquals(createPoint(10., 8., 9.), resultOnCapsule1);
		}

		@DisplayName("(Capsule3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P1 side
			var resultOnCapsule1 = getS().getClosestPointTo(createCapsule(9., 14., 15., 11., 17., 18., 1.));
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453), resultOnCapsule1);
		}

		@DisplayName("(Capsule3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P2 side
			var resultOnCapsule1 = getS().getClosestPointTo(createCapsule(-3., -4., -5., -6., -8.5, -9.5, 1.));
			assertEpsilonEquals(createPoint(-0.8569533818, -0.7854300727, -0.7139067635), resultOnCapsule1);
		}

		@DisplayName("(Capsule3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Reversed segment endpoints should not change geometric distance
			var resultOnCapsule1 = getS().getClosestPointTo(createCapsule(23., 20., 27., 20., 20., 20., 1.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule1);
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultOnCapsule = getS().getClosestPointTo(createSphere(20., 20., 20., 6.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultOnCapsule = getS().getClosestPointTo(createSphere(-20., -20., -20., 6.));
			assertEpsilonEquals(createPoint(-1.7536395823, -0.8847652767, -0.0158909711), resultOnCapsule);
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere center exactly at P1
			var resultOnCapsule = getS().getClosestPointTo(createSphere(getS().getX1(), getS().getY1(), getS().getZ1(), 2.));
			assertEpsilonEquals(createPoint(5, 8, 9), resultOnCapsule);
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere far on +X axis
			var resultOnCapsule = getS().getClosestPointTo(createSphere(100., 2., 3., 10.));
			assertEpsilonEquals(createPoint(9.9801739499, 7.6854626979, 8.6854626979), resultOnCapsule);
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Intersecting configuration (distance between surfaces = 0 expected if clamped)
			var resultOnCapsule = getS().getClosestPointTo(createSphere(6., 8., 9., 4.));
			assertEpsilonEquals(createPoint(6, 8, 9), resultOnCapsule);
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere tangent externally to the capsule (distance between surfaces = 0)
			var resultOnCapsule = getS().getClosestPointTo(createSphere(20., 20., 20., 17.1359436212 - 5.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere center at P2
			var resultOnCapsule = getS().getClosestPointTo(createSphere(getS().getX2(), getS().getY2(), getS().getZ2(), 3.));
			assertEpsilonEquals(createPoint(1, 2, 3), resultOnCapsule);
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far query sphere center (stability + expected large separation)
			var resultOnCapsule = getS().getClosestPointTo(createSphere(1000., 1000., 1000., 1.));
			assertEpsilonEquals(createPoint(7.893532618, 10.884808399, 11.8819003261), resultOnCapsule);
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Zero-radius sphere (point sphere) => should match capsule-point closest result
			var resultOnCapsule = getS().getClosestPointTo(createSphere(20., 20., 20., 0.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere that fully contains capsule endpoint region; closest surfaces overlap
			var resultOnCapsule = getS().getClosestPointTo(createSphere(5., 8., 9., 50.));
			assertEpsilonEquals(createPoint(5.0, 8.0, 9.0), resultOnCapsule);
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultOnCapsule = getS().getClosestPointTo(createAlignedBoxFromPoints(20., 20., 20., 23, 20, 27));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far opposite box
			var resultOnCapsule = getS().getClosestPointTo(createAlignedBoxFromPoints(-23., -20., -27., -20., -20., -20.));
			assertEpsilonEquals(createPoint(-1.7536395823, -0.8847652767, -0.0158909711), resultOnCapsule);
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate box (point) = point query
			var resultOnCapsule = getS().getClosestPointTo(createAlignedBoxFromPoints(20., 20., 20., 20., 20., 20.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Box intersects capsule cylinder region
			var resultOnCapsule = getS().getClosestPointTo(createAlignedBoxFromPoints(2.5, 4.5, 5.5, 3.5, 5.5, 6.5));
			assertEpsilonEquals(createPoint(3.0,5.0,6.0), resultOnCapsule);
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Box fully inside capsule
			var resultOnCapsule = getS().getClosestPointTo(createAlignedBoxFromPoints(3.0, 5.0, 6.0, 3.2, 5.2, 6.2));
			assertEpsilonEquals(createPoint(3.0666666666666664,5.1,6.1), resultOnCapsule);
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Box tangent on lateral side (touching)
			var resultOnCapsule = getS().getClosestPointTo(createAlignedBoxFromPoints(4.0, 8.0, 7.0, 4.1, 8.1, 7.1));
			assertEpsilonEquals(createPoint(4.1,8.0,7.1), resultOnCapsule);
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P1 cap, outside
			var resultOnCapsule = getS().getClosestPointTo(createAlignedBoxFromPoints(9., 14., 15., 10., 15., 16.));
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453), resultOnCapsule);
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P2 cap, outside
			var resultOnCapsule = getS().getClosestPointTo(createAlignedBoxFromPoints(-4., -5., -6., -3., -4., -5.));
			assertEpsilonEquals(createPoint(-0.8569533818, -0.7854300727, -0.7139067635), resultOnCapsule);
		}

		@DisplayName("(AlignedBox3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Slim box crossing axis neighborhood (intersection)
			var resultOnCapsule = getS().getClosestPointTo(createAlignedBoxFromPoints(2.9, 4.9, -100., 3.1, 5.1, 100.));
			assertEpsilonEquals(createPoint(3.0,5.0,6.0), resultOnCapsule);
		}

		@DisplayName("(AlignedBox3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far box (stability)
			var resultOnCapsule = getS().getClosestPointTo(createAlignedBoxFromPoints(1e6, 1e6, 1e6, 1e6 + 10., 1e6 + 5., 1e6 + 2.));
			assertEpsilonEquals(createPoint(7.886758081746495,10.886749421428949,11.886746534656433), resultOnCapsule);
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultOnCapsule = getS().getClosestPointTo(createSegment(20., 20., 20., 23, 20, 27));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far segment on opposite side
			var resultOnCapsule = getS().getClosestPointTo(createSegment(-23., -20., -27., -20., -20., -20.));
			assertEpsilonEquals(createPoint(-1.7536395823, -0.8847652767, -0.0158909711), resultOnCapsule);
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment crossing the capsule: closest distance must be zero
			var resultOnCapsule = getS().getClosestPointTo(createSegment(3., 5., -100., 3., 5., 100.));
			assertEpsilonEquals(createPoint(3.0, 5.0, 6.), resultOnCapsule);
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate segment (point) -> equivalent to point query
			var resultOnCapsule = getS().getClosestPointTo(createSegment(20., 20., 20., 20., 20., 20.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment endpoint at P1 center: nearest capsule point is on cap around P1
			var resultOnCapsule = getS().getClosestPointTo(createSegment(5., 8., 9., 8., 8., 9.));
			assertEpsilonEquals(createPoint(5., 8., 9.), resultOnCapsule);
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far segment (numeric stability)
			var resultOnCapsule = getS().getClosestPointTo(createSegment(1e6, 1e6, 1e6, 1e6 + 10., 1e6 + 5., 1e6 + 2.));
			assertEpsilonEquals(createPoint(7.8867580817, 10.8867494214, 11.8867465347), resultOnCapsule);
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment fully inside capsule
			var resultOnCapsule = getS().getClosestPointTo(createSegment(3., 5., 6., 4., 6., 7.));
			assertEpsilonEquals(createPoint(3., 5., 6.), resultOnCapsule);
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment tangent (touches capsule at one point)
			var resultOnCapsule = getS().getClosestPointTo(createSegment(4., 8., 7., 4., 8., 20.));
			assertEpsilonEquals(createPoint(4.0, 8.0, 8.5384615385), resultOnCapsule);
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query segment endpoint near capsule flank
			var resultOnCapsule = getS().getClosestPointTo(createSegment(10., 8., 9., 12., 8., 9.));
			assertEpsilonEquals(createPoint(10., 8., 9.), resultOnCapsule);
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P1 side
			var resultOnCapsule = getS().getClosestPointTo(createSegment(9., 14., 15., 11., 17., 18.));
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453), resultOnCapsule);
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P2 side
			var resultOnCapsule = getS().getClosestPointTo(createSegment(-3., -4., -5., -6., -8.5, -9.5));
			assertEpsilonEquals(createPoint(-0.8569533818, -0.7854300727, -0.7139067635), resultOnCapsule);
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Reversed segment endpoints should not change geometric distance
			var resultOnCapsule = getS().getClosestPointTo(createSegment(23., 20., 27., 20., 20., 20.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.lineTo(23, 20, 27);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-23., -20., -27.);
			path.lineTo(-20., -20., -20.);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(-1.7536395823, -0.8847652767, -0.0158909711), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.quadTo(24., 22., 23., 30., 40., 50.);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.484646733), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.curveTo(25., 20., 25., 27., 26., 30., 35., 40., 45.);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(2.9, 4.9, -100.);
			path.lineTo(3.1, 5.1, 100.);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(3.0060069303, 5.0060069303, 6.006930291), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(3.0, 5.0, 6.0);
			path.lineTo(3.2, 5.2, 6.2);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(3.0,5.0,6.0), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(4.0, 8.0, 7.0);
			path.lineTo(4.0, 8.0, 20.0);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(4.0,8.0,8.538461538), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(9., 14., 15.);
			path.lineTo(11., 17., 18.);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(7.1320071636, 11.1980107453, 12.1980107453), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-3., -4., -5.);
			path.lineTo(-6., -8.5, -9.5);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(-0.8569533818, -0.7854300727, -0.7139067635), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(1000000., 1000000., 1000000.);
			path.lineTo(1000010., 1000005., 1000002.);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(7.8867580817, 10.8867494214, 11.8867465347), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(23., 20., 27.);
			path.lineTo(20., 20., 20.);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.lineTo(23., 20., 27.);
			path.lineTo(25., 30., 32.);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.quadTo(10., 10., 10., 2.9, 4.9, -100.);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(8.89734244, 10.7195789435, 7.4460977265), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.curveTo(15., 15., 15., 10., 10., 10., 3.0, 5.0, 6.0);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(3.0,5.0,6.0), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(8.0, 8.0, 9.0);
			path.lineTo(9.0, 9.0, 9.0);
			path.lineTo(8.5, 8.0, 10.0);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(8.0,8.0,9.0), resultOnCapsule);
		}

		@DisplayName("(Path3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-2.0, 2.0, 3.0);
			path.lineTo(-3.0, 1.0, 3.0);
			path.lineTo(-2.5, 2.0, 2.0);
			var resultOnCapsule = getS().getClosestPointTo(path);
			assertEpsilonEquals(createPoint(-2.0,2.0,3.0), resultOnCapsule);
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp multishape = createMultiShape();
			multishape.add(createSphere(20, 20, 20, 1));
			multishape.add(createSegment(-17, -5, 5, -1, 5, -1));
			var resultOnCapsule = getS().getClosestPointTo(multishape);
			assertEpsilonEquals(createPoint(-1.74986286145, 4.3141451545, -0.47605903964), resultOnCapsule);
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultOnCapsule = getS().getClosestPointTo((Shape3D) createSphere(100., 2., 3., 10.));
			assertEpsilonEquals(createPoint(9.9801739499, 7.6854626979, 8.6854626979), resultOnCapsule);
		}

		@DisplayName("(Shape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultOnCapsule = getS().getClosestPointTo((Shape3D) createSegment(20., 20., 20., 20., 20., 20.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
		}

		@DisplayName("(Shape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultOnCapsule = getS().getClosestPointTo((Shape3D) createTriangle(20., 20., 20., 20., 20., 20., 20., 20., 20.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule);
		}

		@DisplayName("(Shape3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultOnCapsule1 = getS().getClosestPointTo((Shape3D) createCapsule(20., 20., 20., 20., 20., 20., 1.));
			assertEpsilonEquals(createPoint(8.3881546359, 10.7105237087, 11.4846467330), resultOnCapsule1);
		}
	}

	@DisplayName("getDistance")
	@Nested
	public class GetDistance {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(17.1359436212, getS().getDistance(createPoint(20, 20, 20)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly at P1 center: closest point on surface is along -axis direction from P1
			assertEpsilonEquals(0., getS().getDistance(createPoint(getS().getX1(), getS().getY1(), getS().getZ1())));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly at P2 center: closest point on surface is along +axis direction from P2
			assertEpsilonEquals(0., getS().getDistance(createPoint(getS().getX2(), getS().getY2(), getS().getZ2())));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query on segment midpoint; closest point must be at radius distance from axis
			assertEpsilonEquals(0., getS().getDistance(createPoint(3, 5, 6)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query clearly inside capsule: returned closest point is on boundary and at positive distance
			assertEpsilonEquals(0., getS().getDistance(createPoint(3, 5, 7)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far opposite direction from #1 to cover other cap
			assertEpsilonEquals(33.1313519299, getS().getDistance(createPoint(-20., -20., -20.)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on boundary (known point from #1 expected): closest point should be itself.
			assertEpsilonEquals(0., getS().getDistance(createPoint(8.3881546359, 10.7105237087, 11.4846467330)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on the segment line, beyond P1 side
			assertEpsilonEquals(4.3808315196, getS().getDistance(createPoint(9., 14., 15.)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on the segment line, beyond P2 side
			assertEpsilonEquals(5.7703296143, getS().getDistance(createPoint(-3., -4., -5.)));
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Huge coordinates: numerical stability smoke test
			assertEpsilonEquals(1732050799.1047757, getS().getDistance(createPoint(1e9, -1e9, 1e9)));
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Returned point should always be on/in capsule boundary set (inside by convention)
			assertEpsilonEquals(27.3813831896, getS().getDistance(createPoint(30.25, -12.5, 6.75)));
		}

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(17.1359436212, getS().getDistance(createTriangle(20., 20., 20., 23, 20, 27, 21, 30, 35)));
		}

		@DisplayName("(Triangle3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far triangle on opposite side
			assertEpsilonEquals(33.1313519299, getS().getDistance(createTriangle(-20., -20., -20., -23., -20., -27., -21., -30., -35.)));
		}

		@DisplayName("(Triangle3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle intersects capsule
			assertEpsilonEquals(0., getS().getDistance(createTriangle(3., 5., -10., 3., 5., 20., 6., 8., 9.)));
		}

		@DisplayName("(Triangle3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate triangle = point (20,20,20)
			assertEpsilonEquals(17.1359436212, getS().getDistance(createTriangle(20., 20., 20., 20., 20., 20., 20., 20., 20.)));
		}

		@DisplayName("(Triangle3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate triangle = segment near capsule flank
			assertEpsilonEquals(0., getS().getDistance(createTriangle(10., 8., 9., 12., 8., 9., 12., 8., 9.)));
		}

		@DisplayName("(Triangle3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle fully inside capsule
			assertEpsilonEquals(0., getS().getDistance(createTriangle(3., 5., 6., 3.5, 5.2, 6.2, 2.8, 4.9, 6.1)));
		}

		@DisplayName("(Triangle3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle tangent to capsule at a boundary point
			assertEpsilonEquals(0., getS().getDistance(createTriangle(4., 8., 7., 4., 9., 7., 4., 8., 8.)));
		}

		@DisplayName("(Triangle3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle near P1 cap
			assertEpsilonEquals(4.3808315196, getS().getDistance(createTriangle(9., 14., 15., 10., 15., 16., 9., 15., 15.)));
		}

		@DisplayName("(Triangle3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle near P2 cap
			assertEpsilonEquals(5.7703296143, getS().getDistance(createTriangle(-3., -4., -5., -4., -5., -6., -2.5, -4.5, -5.2)));
		}

		@DisplayName("(Triangle3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Vertex order permutation (same geometry as #1)
			assertEpsilonEquals(17.1359436212, getS().getDistance(createTriangle(21., 30., 35., 23., 20., 27., 20., 20., 20.)));
		}

		@DisplayName("(Triangle3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far triangle (numeric stability)
			assertEpsilonEquals(1732033.105865457, getS().getDistance(createTriangle(1e6, 1e6, 1e6, 1e6 + 10., 1e6, 1e6, 1e6, 1e6 + 10., 1e6)));
		}

		@DisplayName("(Triangle3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_12(CoordinateSystem3D cs) {
			// intersects only sphere at P1 (cap), not cylinder nor P2 cap
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P1=(5,8,9), far from axis around middle and far from P2 cap
			assertEpsilonEquals(0., getS().getDistance(createTriangle(8.0, 8.0, 9.0, 9.0, 15.0, 9.0, 15, 8.0, 10.0)));
		}

		@DisplayName("(Triangle3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_13(CoordinateSystem3D cs) {
			// intersects only sphere at P2 (cap), not cylinder nor P1 cap
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P2=(1,2,3), far from axis around middle and far from P1 cap
			assertEpsilonEquals(0., getS().getDistance(createTriangle(-20.0, 2.0, 3.0, -3.0, 1.0, 3.0, -2.5, -10.0, 2.0)));
		}

		@DisplayName("(Triangle3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_14(CoordinateSystem3D cs) {
			// intersects only capsule cylinder (middle), not the two spherical caps
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Around midpoint M=(3,5,6), plane x=3 crossing lateral tube region
			// Vertices chosen away from both endpoints to avoid cap intersections.
			assertEpsilonEquals(0., getS().getDistance(createTriangle(5, 15, 6.0, -0.4, 18.0, 6.0, -0.4, 7.8, 5.8)));
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.1359436212, getS().getDistance(createSphere(20., 20., 20., 6.)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(27.1313519299, getS().getDistance(createSphere(-20., -20., -20., 6.)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere center exactly at P1
			assertEpsilonEquals(0., getS().getDistance(createSphere(getS().getX1(), getS().getY1(), getS().getZ1(), 2.)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere far on +X axis
			assertEpsilonEquals(80.378194573, getS().getDistance(createSphere(100., 2., 3., 10.)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Intersecting configuration (distance between surfaces = 0 expected if clamped)
			assertEpsilonEquals(0., getS().getDistance(createSphere(6., 8., 9., 4.)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere tangent externally to the capsule (distance between surfaces = 0)
			assertEpsilonEquals(5., getS().getDistance(createSphere(20., 20., 20., 17.1359436212 - 5.)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere center at P2
			assertEpsilonEquals(0., getS().getDistance(createSphere(getS().getX2(), getS().getY2(), getS().getZ2(), 3.)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far query sphere center (stability + expected large separation)
			assertEpsilonEquals(1713.3516219785, getS().getDistance(createSphere(1000., 1000., 1000., 1.)));
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Zero-radius sphere (point sphere) => should match capsule-point closest result
			assertEpsilonEquals(17.1359436212, getS().getDistance(createSphere(20., 20., 20., 0.)));
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere that fully contains capsule endpoint region; closest surfaces overlap
			assertEpsilonEquals(0., getS().getDistance(createSphere(5., 8., 9., 50.)));
		}

		@DisplayName("(Capsule3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16.1359436212, getS().getDistance(createCapsule(20., 20., 20., 23, 20, 27, 1.)));
		}

		@DisplayName("(Capsule3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far segment on opposite side
			assertEpsilonEquals(32.1313519299, getS().getDistance(createCapsule(-23., -20., -27., -20., -20., -20., 1.)));
		}

		@DisplayName("(Capsule3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment crossing the capsule: closest distance must be zero
			assertEpsilonEquals(0., getS().getDistance(createCapsule(3., 5., -100., 3., 5., 100., 1.)));
		}

		@DisplayName("(Capsule3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate segment (point) -> equivalent to point query
			assertEpsilonEquals(16.1359436212, getS().getDistance(createCapsule(20., 20., 20., 20., 20., 20., 1.)));
		}

		@DisplayName("(Capsule3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment endpoint at P1 center: nearest capsule point is on cap around P1
			assertEpsilonEquals(0., getS().getDistance(createCapsule(5., 8., 9., 8., 8., 9., 1.)));
		}

		@DisplayName("(Capsule3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far segment (numeric stability)
			assertEpsilonEquals(1732032.105865457, getS().getDistance(createCapsule(1e6, 1e6, 1e6, 1e6 + 10., 1e6 + 5., 1e6 + 2., 1.)));
		}

		@DisplayName("(Capsule3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment fully inside capsule
			assertEpsilonEquals(0., getS().getDistance(createCapsule(3., 5., 6., 4., 6., 7., 1.)));
			assertEpsilonEquals(0., getS().getDistanceSquared(createCapsule(3., 5., 6., 4., 6., 7., 1.)));
		}

		@DisplayName("(Capsule3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment tangent (touches capsule at one point)
			assertEpsilonEquals(0., getS().getDistance(createCapsule(4., 8., 7., 4., 8., 20., 1.)));
		}

		@DisplayName("(Capsule3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query segment endpoint near capsule flank
			assertEpsilonEquals(0., getS().getDistance(createCapsule(10., 8., 9., 12., 8., 9., 1.)));
		}

		@DisplayName("(Capsule3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P1 side
			assertEpsilonEquals(3.3808315196, getS().getDistance(createCapsule(9., 14., 15., 11., 17., 18., 1.)));
		}

		@DisplayName("(Capsule3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P2 side
			assertEpsilonEquals(4.7703296143, getS().getDistance(createCapsule(-3., -4., -5., -6., -8.5, -9.5, 1.)));
		}

		@DisplayName("(Capsule3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Reversed segment endpoints should not change geometric distance
			assertEpsilonEquals(16.1359436212, getS().getDistance(createCapsule(23., 20., 27., 20., 20., 20., 1.)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(17.1359436212, getS().getDistance(createSegment(20., 20., 20., 23, 20, 27)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far segment on opposite side
			assertEpsilonEquals(33.1313519299, getS().getDistance(createSegment(-23., -20., -27., -20., -20., -20.)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment crossing the capsule: closest distance must be zero
			assertEpsilonEquals(0., getS().getDistance(createSegment(3., 5., -100., 3., 5., 100.)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate segment (point) -> equivalent to point query
			assertEpsilonEquals(17.1359436212, getS().getDistance(createSegment(20., 20., 20., 20., 20., 20.)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment endpoint at P1 center: nearest capsule point is on cap around P1
			assertEpsilonEquals(0., getS().getDistance(createSegment(5., 8., 9., 8., 8., 9.)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far segment (numeric stability)
			assertEpsilonEquals(1732033.105865457, getS().getDistance(createSegment(1e6, 1e6, 1e6, 1e6 + 10., 1e6 + 5., 1e6 + 2.)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment fully inside capsule
			assertEpsilonEquals(0., getS().getDistance(createSegment(3., 5., 6., 4., 6., 7.)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment tangent (touches capsule at one point)
			assertEpsilonEquals(0., getS().getDistance(createSegment(4., 8., 7., 4., 8., 20.)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query segment endpoint near capsule flank
			assertEpsilonEquals(0., getS().getDistance(createSegment(10., 8., 9., 12., 8., 9.)));
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P1 side
			assertEpsilonEquals(4.3808315196, getS().getDistance(createSegment(9., 14., 15., 11., 17., 18.)));
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P2 side
			assertEpsilonEquals(5.7703296143, getS().getDistance(createSegment(-3., -4., -5., -6., -8.5, -9.5)));
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Reversed segment endpoints should not change geometric distance
			assertEpsilonEquals(17.1359436212, getS().getDistance(createSegment(23., 20., 27., 20., 20., 20.)));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(17.1359436212, getS().getDistance(createAlignedBoxFromPoints(20., 20., 20., 23, 20, 27)));
		}
		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far opposite box
			assertEpsilonEquals(33.1313519299, getS().getDistance(createAlignedBoxFromPoints(-23., -20., -27., -20., -20., -20.)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate box (point) = point query
			assertEpsilonEquals(17.1359436212, getS().getDistance(createAlignedBoxFromPoints(20., 20., 20., 20., 20., 20.)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Box intersects capsule cylinder region
			assertEpsilonEquals(0., getS().getDistance(createAlignedBoxFromPoints(2.5, 4.5, 5.5, 3.5, 5.5, 6.5)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Box fully inside capsule
			assertEpsilonEquals(0., getS().getDistance(createAlignedBoxFromPoints(3.0, 5.0, 6.0, 3.2, 5.2, 6.2)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Box tangent on lateral side (touching)
			assertEpsilonEquals(0., getS().getDistance(createAlignedBoxFromPoints(4.0, 8.0, 7.0, 4.1, 8.1, 7.1)));
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P1 cap, outside
			assertEpsilonEquals(4.3808315196, getS().getDistance(createAlignedBoxFromPoints(9., 14., 15., 10., 15., 16.)));
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P2 cap, outside
			assertEpsilonEquals(5.7703296143, getS().getDistance(createAlignedBoxFromPoints(-4., -5., -6., -3., -4., -5.)));
		}

		@DisplayName("(AlignedBox3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Slim box crossing axis neighborhood (intersection)
			assertEpsilonEquals(0., getS().getDistance(createAlignedBoxFromPoints(2.9, 4.9, -100., 3.1, 5.1, 100.)));
		}

		@DisplayName("(AlignedBox3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far box (stability)
			assertEpsilonEquals(1732033.105865457, getS().getDistance(createAlignedBoxFromPoints(1e6, 1e6, 1e6, 1e6 + 10., 1e6 + 5., 1e6 + 2.)));
		}


		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.lineTo(23, 20, 27);
			assertEpsilonEquals(17.1359436212, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-23., -20., -27.);
			path.lineTo(-20., -20., -20.);
			assertEpsilonEquals(33.1313519299, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.quadTo(24., 22., 23., 30., 40., 50.);
			assertEpsilonEquals(17.1359436212, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.curveTo(25., 20., 25., 27., 26., 30., 35., 40., 45.);
			assertEpsilonEquals(17.1359436212, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(2.9, 4.9, -100.);
			path.lineTo(3.1, 5.1, 100.);
			assertEpsilonEquals(0., getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(3.0, 5.0, 6.0);
			path.lineTo(3.2, 5.2, 6.2);
			assertEpsilonEquals(0., getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(4.0, 8.0, 7.0);
			path.lineTo(4.0, 8.0, 20.0);
			assertEpsilonEquals(0., getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(9., 14., 15.);
			path.lineTo(11., 17., 18.);
			assertEpsilonEquals(4.3808315196, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-3., -4., -5.);
			path.lineTo(-6., -8.5, -9.5);
			assertEpsilonEquals(5.7703296143, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(1000000., 1000000., 1000000.);
			path.lineTo(1000010., 1000005., 1000002.);
			assertEpsilonEquals(1732033.105865457, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(23., 20., 27.);
			path.lineTo(20., 20., 20.);
			assertEpsilonEquals(17.1359436212, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.lineTo(23., 20., 27.);
			path.lineTo(25., 30., 32.);
			assertEpsilonEquals(17.1359436212, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.quadTo(10., 10., 10., 2.9, 4.9, -100.);
			assertEpsilonEquals(6.9876433883, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.curveTo(15., 15., 15., 10., 10., 10., 3.0, 5.0, 6.0);
			assertEpsilonEquals(0., getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(8.0, 8.0, 9.0);
			path.lineTo(9.0, 9.0, 9.0);
			path.lineTo(8.5, 8.0, 10.0);
			assertEpsilonEquals(0., getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-2.0, 2.0, 3.0);
			path.lineTo(-3.0, 1.0, 3.0);
			path.lineTo(-2.5, 2.0, 2.0);
			assertEpsilonEquals(0., getS().getDistance(path));
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp multishape = createMultiShape();
			multishape.add(createSphere(20, 20, 20, 1));
			multishape.add(createSegment(-17, -5, 5, -1, 5, -1));
			assertEpsilonEquals(0.269279469, getS().getDistance(multishape));
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(17.1359436212, getS().getDistance((Shape3D) createTriangle(20., 20., 20., 20., 20., 20., 20., 20., 20.)));
		}

		@DisplayName("(Shape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(33.1313519299, getS().getDistance((Shape3D) createSegment(-23., -20., -27., -20., -20., -20.)));
		}

		@DisplayName("(Shape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1713.3516219785, getS().getDistance((Shape3D) createSphere(1000., 1000., 1000., 1.)));
		}

		@DisplayName("(Shape3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16.1359436212, getS().getDistance((Shape3D) createCapsule(20., 20., 20., 20., 20., 20., 1.)));
		}
	}

	@DisplayName("getDistanceSquared")
	@Nested
	public class GetDistanceSquared {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared(createPoint(20, 20, 20)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly at P1 center: closest point on surface is along -axis direction from P1
			assertEpsilonEquals(0., getS().getDistanceSquared(createPoint(getS().getX1(), getS().getY1(), getS().getZ1())));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly at P2 center: closest point on surface is along +axis direction from P2
			assertEpsilonEquals(0., getS().getDistanceSquared(createPoint(getS().getX2(), getS().getY2(), getS().getZ2())));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query on segment midpoint; closest point must be at radius distance from axis
			assertEpsilonEquals(0., getS().getDistanceSquared(createPoint(3, 5, 6)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query clearly inside capsule: returned closest point is on boundary and at positive distance
			assertEpsilonEquals(0., getS().getDistanceSquared(createPoint(3, 5, 7)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far opposite direction from #1 to cover other cap
			assertEpsilonEquals(1097.6864807012, getS().getDistanceSquared(createPoint(-20., -20., -20.)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on boundary (known point from #1 expected): closest point should be itself.
			assertEpsilonEquals(0., getS().getDistanceSquared(createPoint(8.3881546359, 10.7105237087, 11.4846467330)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on the segment line, beyond P1 side
			assertEpsilonEquals(19.1916848035, getS().getDistanceSquared(createPoint(9., 14., 15.)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on the segment line, beyond P2 side
			assertEpsilonEquals(33.2967038573, getS().getDistanceSquared(createPoint(-3., -4., -5.)));
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Huge coordinates: numerical stability smoke test
			assertEpsilonEquals(2.999999970679492e18, getS().getDistanceSquared(createPoint(1e9, -1e9, 1e9)));
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Returned point should always be on/in capsule boundary set (inside by convention)
			assertEpsilonEquals(749.7401453766, getS().getDistanceSquared(createPoint(30.25, -12.5, 6.75)));
		}

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared(createTriangle(20., 20., 20., 23, 20, 27, 21, 30, 35)));
		}

		@DisplayName("(Triangle3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far triangle on opposite side
			assertEpsilonEquals(1097.6864807012, getS().getDistanceSquared(createTriangle(-20., -20., -20., -23., -20., -27., -21., -30., -35.)));
		}

		@DisplayName("(Triangle3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle intersects capsule
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(3., 5., -10., 3., 5., 20., 6., 8., 9.)));
		}

		@DisplayName("(Triangle3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate triangle = point (20,20,20)
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared(createTriangle(20., 20., 20., 20., 20., 20., 20., 20., 20.)));
		}

		@DisplayName("(Triangle3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate triangle = segment near capsule flank
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(10., 8., 9., 12., 8., 9., 12., 8., 9.)));
		}

		@DisplayName("(Triangle3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle fully inside capsule
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(3., 5., 6., 3.5, 5.2, 6.2, 2.8, 4.9, 6.1)));
		}

		@DisplayName("(Triangle3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle tangent to capsule at a boundary point
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(4., 8., 7., 4., 9., 7., 4., 8., 8.)));
		}

		@DisplayName("(Triangle3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle near P1 cap
			assertEpsilonEquals(19.1916848035, getS().getDistanceSquared(createTriangle(9., 14., 15., 10., 15., 16., 9., 15., 15.)));
		}

		@DisplayName("(Triangle3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle near P2 cap
			assertEpsilonEquals(33.2967038573, getS().getDistanceSquared(createTriangle(-3., -4., -5., -4., -5., -6., -2.5, -4.5, -5.2)));
		}

		@DisplayName("(Triangle3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Vertex order permutation (same geometry as #1)
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared(createTriangle(21., 30., 35., 23., 20., 27., 20., 20., 20.)));
		}

		@DisplayName("(Triangle3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far triangle (numeric stability)
			assertEpsilonEquals(2.9999386798139414E12, getS().getDistanceSquared(createTriangle(1e6, 1e6, 1e6, 1e6 + 10., 1e6, 1e6, 1e6, 1e6 + 10., 1e6)));
		}

		@DisplayName("(Triangle3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_12(CoordinateSystem3D cs) {
			// intersects only sphere at P1 (cap), not cylinder nor P2 cap
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P1=(5,8,9), far from axis around middle and far from P2 cap
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(8.0, 8.0, 9.0, 9.0, 15.0, 9.0, 15, 8.0, 10.0)));
		}

		@DisplayName("(Triangle3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_13(CoordinateSystem3D cs) {
			// intersects only sphere at P2 (cap), not cylinder nor P1 cap
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P2=(1,2,3), far from axis around middle and far from P1 cap
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(-20.0, 2.0, 3.0, -3.0, 1.0, 3.0, -2.5, -10.0, 2.0)));
		}

		@DisplayName("(Triangle3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_14(CoordinateSystem3D cs) {
			// intersects only capsule cylinder (middle), not the two spherical caps
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Around midpoint M=(3,5,6), plane x=3 crossing lateral tube region
			// Vertices chosen away from both endpoints to avoid cap intersections.
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(5, 15, 6.0, -0.4, 18.0, 6.0, -0.4, 7.8, 5.8)));
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.1359436212, getS().getDistance(createSphere(20., 20., 20., 6.)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(27.1313519299, getS().getDistance(createSphere(-20., -20., -20., 6.)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere center exactly at P1
			assertEpsilonEquals(0., getS().getDistance(createSphere(getS().getX1(), getS().getY1(), getS().getZ1(), 2.)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere far on +X axis
			assertEpsilonEquals(80.378194573, getS().getDistance(createSphere(100., 2., 3., 10.)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Intersecting configuration (distance between surfaces = 0 expected if clamped)
			assertEpsilonEquals(0., getS().getDistance(createSphere(6., 8., 9., 4.)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere tangent externally to the capsule (distance between surfaces = 0)
			assertEpsilonEquals(5., getS().getDistance(createSphere(20., 20., 20., 17.1359436212 - 5.)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere center at P2
			assertEpsilonEquals(0., getS().getDistance(createSphere(getS().getX2(), getS().getY2(), getS().getZ2(), 3.)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far query sphere center (stability + expected large separation)
			assertEpsilonEquals(1713.3516219785, getS().getDistance(createSphere(1000., 1000., 1000., 1.)));
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Zero-radius sphere (point sphere) => should match capsule-point closest result
			assertEpsilonEquals(17.1359436212, getS().getDistance(createSphere(20., 20., 20., 0.)));
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Sphere that fully contains capsule endpoint region; closest surfaces overlap
			assertEpsilonEquals(0., getS().getDistance(createSphere(5., 8., 9., 50.)));
		}

		@DisplayName("(Capsule3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(260.3686765459, getS().getDistanceSquared(createCapsule(20., 20., 20., 23, 20, 27, 1.)));
		}

		@DisplayName("(Capsule3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far segment on opposite side
			assertEpsilonEquals(1032.4237768415, getS().getDistanceSquared(createCapsule(-23., -20., -27., -20., -20., -20., 1.)));
		}

		@DisplayName("(Capsule3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment crossing the capsule: closest distance must be zero
			assertEpsilonEquals(0., getS().getDistanceSquared(createCapsule(3., 5., -100., 3., 5., 100., 1.)));
		}

		@DisplayName("(Capsule3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate segment (point) -> equivalent to point query
			assertEpsilonEquals(260.3686765459, getS().getDistanceSquared(createCapsule(20., 20., 20., 20., 20., 20., 1.)));
		}

		@DisplayName("(Capsule3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment endpoint at P1 center: nearest capsule point is on cap around P1
			assertEpsilonEquals(0., getS().getDistanceSquared(createCapsule(5., 8., 9., 8., 8., 9., 1.)));
		}

		@DisplayName("(Capsule3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far segment (numeric stability)
			assertEpsilonEquals(2.99993521574873E12, getS().getDistanceSquared(createCapsule(1e6, 1e6, 1e6, 1e6 + 10., 1e6 + 5., 1e6 + 2., 1.)));
		}

		@DisplayName("(Capsule3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment fully inside capsule
			assertEpsilonEquals(0., getS().getDistanceSquared(createCapsule(3., 5., 6., 4., 6., 7., 1.)));
		}

		@DisplayName("(Capsule3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment tangent (touches capsule at one point)
			assertEpsilonEquals(0., getS().getDistanceSquared(createCapsule(4., 8., 7., 4., 8., 20., 1.)));
		}

		@DisplayName("(Capsule3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query segment endpoint near capsule flank
			assertEpsilonEquals(0., getS().getDistanceSquared(createCapsule(10., 8., 9., 12., 8., 9., 1.)));
		}

		@DisplayName("(Capsule3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P1 side
			assertEpsilonEquals(11.4300217642, getS().getDistanceSquared(createCapsule(9., 14., 15., 11., 17., 18., 1.)));
		}

		@DisplayName("(Capsule3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P2 side
			assertEpsilonEquals(22.7560446288, getS().getDistanceSquared(createCapsule(-3., -4., -5., -6., -8.5, -9.5, 1.)));
		}

		@DisplayName("(Capsule3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Reversed segment endpoints should not change geometric distance
			assertEpsilonEquals(260.3686765459, getS().getDistanceSquared(createCapsule(23., 20., 27., 20., 20., 20., 1.)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared(createSegment(20., 20., 20., 23, 20, 27)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far segment on opposite side
			assertEpsilonEquals(1097.6864807012, getS().getDistanceSquared(createSegment(-23., -20., -27., -20., -20., -20.)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment crossing the capsule: closest distance must be zero
			assertEpsilonEquals(0., getS().getDistanceSquared(createSegment(3., 5., -100., 3., 5., 100.)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate segment (point) -> equivalent to point query
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared(createSegment(20., 20., 20., 20., 20., 20.)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment endpoint at P1 center: nearest capsule point is on cap around P1
			assertEpsilonEquals(0., getS().getDistanceSquared(createSegment(5., 8., 9., 8., 8., 9.)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far segment (numeric stability)
			assertEpsilonEquals(2.9999386798139414E12, getS().getDistanceSquared(createSegment(1e6, 1e6, 1e6, 1e6 + 10., 1e6 + 5., 1e6 + 2.)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment fully inside capsule
			assertEpsilonEquals(0., getS().getDistanceSquared(createSegment(3., 5., 6., 4., 6., 7.)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment tangent (touches capsule at one point)
			assertEpsilonEquals(0., getS().getDistanceSquared(createSegment(4., 8., 7., 4., 8., 20.)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query segment endpoint near capsule flank
			assertEpsilonEquals(0., getS().getDistanceSquared(createSegment(10., 8., 9., 12., 8., 9.)));
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P1 side
			assertEpsilonEquals(19.1916848035, getS().getDistanceSquared(createSegment(9., 14., 15., 11., 17., 18.)));
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Segment along capsule axis but outside on P2 side
			assertEpsilonEquals(33.2967038573, getS().getDistanceSquared(createSegment(-3., -4., -5., -6., -8.5, -9.5)));
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Reversed segment endpoints should not change geometric distance
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared(createSegment(23., 20., 27., 20., 20., 20.)));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared(createAlignedBoxFromPoints(20., 20., 20., 23, 20, 27)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far opposite box
			assertEpsilonEquals(1097.6864807012, getS().getDistanceSquared(createAlignedBoxFromPoints(-23., -20., -27., -20., -20., -20.)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Degenerate box (point) = point query
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared(createAlignedBoxFromPoints(20., 20., 20., 20., 20., 20.)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Box intersects capsule cylinder region
			assertEpsilonEquals(0., getS().getDistanceSquared(createAlignedBoxFromPoints(2.5, 4.5, 5.5, 3.5, 5.5, 6.5)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Box fully inside capsule
			assertEpsilonEquals(0., getS().getDistanceSquared(createAlignedBoxFromPoints(3.0, 5.0, 6.0, 3.2, 5.2, 6.2)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Box tangent on lateral side (touching)
			assertEpsilonEquals(0., getS().getDistanceSquared(createAlignedBoxFromPoints(4.0, 8.0, 7.0, 4.1, 8.1, 7.1)));
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P1 cap, outside
			assertEpsilonEquals(19.1916848035, getS().getDistanceSquared(createAlignedBoxFromPoints(9., 14., 15., 10., 15., 16.)));
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Near P2 cap, outside
			assertEpsilonEquals(33.2967038573, getS().getDistanceSquared(createAlignedBoxFromPoints(-4., -5., -6., -3., -4., -5.)));
		}

		@DisplayName("(AlignedBox3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Slim box crossing axis neighborhood (intersection)
			assertEpsilonEquals(0., getS().getDistanceSquared(createAlignedBoxFromPoints(2.9, 4.9, -100., 3.1, 5.1, 100.)));
		}

		@DisplayName("(AlignedBox3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Very far box (stability)
			assertEpsilonEquals(2.9999386798139414E12, getS().getDistanceSquared(createAlignedBoxFromPoints(1e6, 1e6, 1e6, 1e6 + 10., 1e6 + 5., 1e6 + 2.)));
		}


		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.lineTo(23, 20, 27);
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-23., -20., -27.);
			path.lineTo(-20., -20., -20.);
			assertEpsilonEquals(1097.6864807012, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.quadTo(24., 22., 23., 30., 40., 50.);
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.curveTo(25., 20., 25., 27., 26., 30., 35., 40., 45.);
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(2.9, 4.9, -100.);
			path.lineTo(3.1, 5.1, 100.);
			assertEpsilonEquals(0., getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(3.0, 5.0, 6.0);
			path.lineTo(3.2, 5.2, 6.2);
			assertEpsilonEquals(0., getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(4.0, 8.0, 7.0);
			path.lineTo(4.0, 8.0, 20.0);
			assertEpsilonEquals(0., getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(9., 14., 15.);
			path.lineTo(11., 17., 18.);
			assertEpsilonEquals(19.1916848035, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-3., -4., -5.);
			path.lineTo(-6., -8.5, -9.5);
			assertEpsilonEquals(33.2967038573, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(1000000., 1000000., 1000000.);
			path.lineTo(1000010., 1000005., 1000002.);
			assertEpsilonEquals(2.9999386798139414E12, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(23., 20., 27.);
			path.lineTo(20., 20., 20.);
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.lineTo(23., 20., 27.);
			path.lineTo(25., 30., 32.);
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.quadTo(10., 10., 10., 2.9, 4.9, -100.);
			assertEpsilonEquals(48.8271601221, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(20., 20., 20.);
			path.curveTo(15., 15., 15., 10., 10., 10., 3.0, 5.0, 6.0);
			assertEpsilonEquals(0., getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(8.0, 8.0, 9.0);
			path.lineTo(9.0, 9.0, 9.0);
			path.lineTo(8.5, 8.0, 10.0);
			assertEpsilonEquals(0., getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-2.0, 2.0, 3.0);
			path.lineTo(-3.0, 1.0, 3.0);
			path.lineTo(-2.5, 2.0, 2.0);
			assertEpsilonEquals(0., getS().getDistanceSquared(path));
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp multishape = createMultiShape();
			multishape.add(createSphere(20, 20, 20, 1));
			multishape.add(createSegment(-17, -5, 5, -1, 5, -1));
			assertEpsilonEquals(0.072511432426, getS().getDistanceSquared(multishape));
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(293.6405637882, getS().getDistanceSquared((Shape3D) createTriangle(20., 20., 20., 20., 20., 20., 20., 20., 20.)));
		}

		@DisplayName("(Shape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1097.6864807012, getS().getDistanceSquared((Shape3D) createSegment(-23., -20., -27., -20., -20., -20.)));
		}

		@DisplayName("(Shape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(260.3686765459, getS().getDistanceSquared((Shape3D) createCapsule(20., 20., 20., 20., 20., 20., 1.)));
		}

		@DisplayName("(Shape3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1713.3516219785, getS().getDistance((Shape3D) createSphere(1000., 1000., 1000., 1.)));
		}
	}

	@DisplayName("getDistanceL1")
	@Nested
	public class GetDistanceL1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(29.4166749224, getS().getDistanceL1(createPoint(20., 20., 20.)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceL1(createPoint(getS().getX1(), getS().getY1(), getS().getZ1())));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceL1(createPoint(getS().getX2(), getS().getY2(), getS().getZ2())));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceL1(createPoint(3., 5., 6.)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceL1(createPoint(3., 5., 7.)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(57.3457041699, getS().getDistanceL1(createPoint(-20., -20., -20.)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceL1(createPoint(8.3881546359, 10.7105237087, 11.4846467330)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.4719713458, getS().getDistanceL1(createPoint(9., 14., 15.)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9.643709782, getS().getDistanceL1(createPoint(-3., -4., -5.)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2999999985.339746, getS().getDistanceL1(createPoint(1e9, -1e9, 1e9)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(38.1476528386, getS().getDistanceL1(createPoint(30.25, -12.5, 6.75)));
		}
	}

	@DisplayName("getDistanceLinf")
	@Nested
	public class GetDistanceLinf {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.6118453641, getS().getDistanceLinf(createPoint(20., 20., 20.)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceLinf(createPoint(getS().getX1(), getS().getY1(), getS().getZ1())));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceLinf(createPoint(getS().getX2(), getS().getY2(), getS().getZ2())));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceLinf(createPoint(3., 5., 6.)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceLinf(createPoint(3., 5., 7.)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(19.9841090289, getS().getDistanceLinf(createPoint(-20., -20., -20.)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceLinf(createPoint(8.3881546359, 10.7105237087, 11.4846467330)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.8019892547, getS().getDistanceLinf(createPoint(9., 14., 15.)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.2860932365, getS().getDistanceLinf(createPoint(-3., -4., -5.)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1000000005.1132486, getS().getDistanceLinf(createPoint(1e9, -1e9, 1e9)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(22.715630053, getS().getDistanceLinf(createPoint(30.25, -12.5, 6.75)));
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
			fail("Todo");
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Capsule3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(PathIterator3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Shape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}
	}

	@DisplayName("s += Vector3D")
	@Nested
	public class OperatorAddVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_add(createVector(0, 0, 0));
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_add(createVector(-2, -3, -4));
			assertEpsilonEquals(createPoint(3, 5, 5), getS().getP1());
			assertEpsilonEquals(createPoint(-1, -1, -1), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_add(createVector(2.5, -1.5, 0.5));
			assertEpsilonEquals(createPoint(7.5, 6.5, 9.5), getS().getP1());
			assertEpsilonEquals(createPoint(3.5, 0.5, 3.5), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_add(createVector(1, 2, 3));
			getS().operator_add(createVector(4, 5, 6));
			assertEpsilonEquals(createPoint(10, 15, 18), getS().getP1());
			assertEpsilonEquals(createPoint(6, 9, 12), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_add(createVector(10, -4, 7));
			getS().operator_add(createVector(-10, 4, -7));
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_add(createVector(1e6, -1e6, 1e6));
			assertEpsilonEquals(createPoint(1000005, -999992, 1000009), getS().getP1());
			assertEpsilonEquals(createPoint(1000001, -999998, 1000003), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}
	}

	@DisplayName("s + Vector3D")
	@Nested
	public class OperatorPlusVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getS().operator_plus(createVector(0, 0, 0));
			assertEpsilonEquals(createPoint(5, 8, 9), r.getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), r.getP2());
			assertEpsilonEquals(5, r.getRadius());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getS().operator_plus(createVector(-2, -3, -4));
			assertEpsilonEquals(createPoint(3, 5, 5), r.getP1());
			assertEpsilonEquals(createPoint(-1, -1, -1), r.getP2());
			assertEpsilonEquals(5, r.getRadius());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getS().operator_plus(createVector(2.5, -1.5, 0.5));
			assertEpsilonEquals(createPoint(7.5, 6.5, 9.5), r.getP1());
			assertEpsilonEquals(createPoint(3.5, 0.5, 3.5), r.getP2());
			assertEpsilonEquals(5, r.getRadius());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getS().operator_plus(createVector(1, 2, 3));
			var r1 = r.operator_plus(createVector(4, 5, 6));
			assertEpsilonEquals(createPoint(10, 15, 18), r1.getP1());
			assertEpsilonEquals(createPoint(6, 9, 12), r1.getP2());
			assertEpsilonEquals(5, r1.getRadius());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getS().operator_plus(createVector(10, -4, 7));
			var r1 = r.operator_plus(createVector(-10, 4, -7));
			assertEpsilonEquals(createPoint(5, 8, 9), r1.getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), r1.getP2());
			assertEpsilonEquals(5, r1.getRadius());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getS().operator_plus(createVector(1e6, -1e6, 1e6));
			assertEpsilonEquals(createPoint(1000005, -999992, 1000009), r.getP1());
			assertEpsilonEquals(createPoint(1000001, -999998, 1000003), r.getP2());
			assertEpsilonEquals(5, r.getRadius());
		}
	}

	@DisplayName("s -= Vector3D")
	@Nested
	public class OperatorRemoveVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_remove(createVector(0, 0, 0));
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_remove(createVector(-2, -3, -4));
			assertEpsilonEquals(createPoint(7.0, 11.0, 13.0), getS().getP1());
			assertEpsilonEquals(createPoint(3.0, 5.0, 7.0), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_remove(createVector(2.5, -1.5, 0.5));
			assertEpsilonEquals(createPoint(2.5, 9.5, 8.5), getS().getP1());
			assertEpsilonEquals(createPoint(-1.5, 3.5, 2.5), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_remove(createVector(1, 2, 3));
			getS().operator_remove(createVector(4, 5, 6));
			assertEpsilonEquals(createPoint(0.0, 1.0, 0.0), getS().getP1());
			assertEpsilonEquals(createPoint(-4.0, -5.0, -6.0), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_remove(createVector(10, -4, 7));
			getS().operator_remove(createVector(-10, 4, -7));
			assertEpsilonEquals(createPoint(5, 8, 9), getS().getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_remove(createVector(1e6, -1e6, 1e6));
			assertEpsilonEquals(createPoint(-999995.0, 1000008.0, -999991.0), getS().getP1());
			assertEpsilonEquals(createPoint(-999999.0, 1000002.0, -999997.0), getS().getP2());
			assertEpsilonEquals(5, getS().getRadius());
		}
	}

	@DisplayName("s - Vector3D")
	@Nested
	public class OperatorMinusVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getS().operator_minus(createVector(0, 0, 0));
			assertEpsilonEquals(createPoint(5, 8, 9), r.getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), r.getP2());
			assertEpsilonEquals(5, r.getRadius());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getS().operator_minus(createVector(-2, -3, -4));
			assertEpsilonEquals(createPoint(7.0, 11.0, 13.0), r.getP1());
			assertEpsilonEquals(createPoint(3.0, 5.0, 7.0), r.getP2());
			assertEpsilonEquals(5, r.getRadius());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getS().operator_minus(createVector(2.5, -1.5, 0.5));
			assertEpsilonEquals(createPoint(2.5, 9.5, 8.5), r.getP1());
			assertEpsilonEquals(createPoint(-1.5, 3.5, 2.5), r.getP2());
			assertEpsilonEquals(5, r.getRadius());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getS().operator_minus(createVector(1, 2, 3));
			var r1 = r.operator_minus(createVector(4, 5, 6));
			assertEpsilonEquals(createPoint(0.0, 1.0, 0.0), r1.getP1());
			assertEpsilonEquals(createPoint(-4.0, -5.0, -6.0), r1.getP2());
			assertEpsilonEquals(5, r1.getRadius());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getS().operator_minus(createVector(10, -4, 7));
			var r1 = r.operator_minus(createVector(-10, 4, -7));
			assertEpsilonEquals(createPoint(5, 8, 9), r1.getP1());
			assertEpsilonEquals(createPoint(1, 2, 3), r1.getP2());
			assertEpsilonEquals(5, r1.getRadius());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getS().operator_minus(createVector(1e6, -1e6, 1e6));
			assertEpsilonEquals(createPoint(-999995.0, 1000008.0, -999991.0), r.getP1());
			assertEpsilonEquals(createPoint(-999999.0, 1000002.0, -999997.0), r.getP2());
			assertEpsilonEquals(5, r.getRadius());
		}
	}

	@DisplayName("s && Point3D")
	@Nested
	public class OperatorAndPoint3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(5, 8, 9)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(0, 0, 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(-10, 0, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// point on segment endpoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Exactly on one endpoint of the inner segment => inside
			assertTrue(getS().operator_and(createPoint(1, 2, 3)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// point on other segment endpoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Exactly on the second endpoint of the inner segment => inside
			assertTrue(getS().operator_and(createPoint(7, 10, 11)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// point on cylindrical side surface
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Point at exactly radius distance from the segment => boundary, inside
			assertTrue(getS().operator_and(createPoint(4, 8, 7)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// point just outside cylindrical side
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Slightly farther than radius from the segment => outside
			assertFalse(getS().operator_and(createPoint(5, 8, 14.001)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			// point in spherical cap near first endpoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Outside segment projection but inside first end-cap sphere => inside
			assertTrue(getS().operator_and(createPoint(0.5, 2, 3)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			// point outside spherical cap near first endpoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Outside first end-cap sphere => outside
			assertFalse(getS().operator_and(createPoint(-4.1, 2, 3)));
		}
	}

	@DisplayName("s && Shape3D")
	@Nested
	public class OperatorAndShape3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}
	}

	@DisplayName("s .. Point3D")
	@Nested
	public class OperatorUpToPoint3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(17.1359436212, getS().operator_upTo(createPoint(20, 20, 20)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly at P1 center: closest point on surface is along -axis direction from P1
			assertEpsilonEquals(0., getS().operator_upTo(createPoint(getS().getX1(), getS().getY1(), getS().getZ1())));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly at P2 center: closest point on surface is along +axis direction from P2
			assertEpsilonEquals(0., getS().operator_upTo(createPoint(getS().getX2(), getS().getY2(), getS().getZ2())));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query on segment midpoint; closest point must be at radius distance from axis
			assertEpsilonEquals(0., getS().operator_upTo(createPoint(3, 5, 6)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query clearly inside capsule: returned closest point is on boundary and at positive distance
			assertEpsilonEquals(0., getS().operator_upTo(createPoint(3, 5, 7)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Far opposite direction from #1 to cover other cap
			assertEpsilonEquals(33.1313519299, getS().operator_upTo(createPoint(-20., -20., -20.)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on boundary (known point from #1 expected): closest point should be itself.
			assertEpsilonEquals(0., getS().operator_upTo(createPoint(8.3881546359, 10.7105237087, 11.4846467330)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on the segment line, beyond P1 side
			assertEpsilonEquals(4.3808315196, getS().operator_upTo(createPoint(9., 14., 15.)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Query exactly on the segment line, beyond P2 side
			assertEpsilonEquals(5.7703296143, getS().operator_upTo(createPoint(-3., -4., -5.)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Huge coordinates: numerical stability smoke test
			assertEpsilonEquals(1732050799.1047757, getS().operator_upTo(createPoint(1e9, -1e9, 1e9)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Returned point should always be on/in capsule boundary set (inside by convention)
			assertEpsilonEquals(27.3813831896, getS().operator_upTo(createPoint(30.25, -12.5, 6.75)));
		}
	}

}
