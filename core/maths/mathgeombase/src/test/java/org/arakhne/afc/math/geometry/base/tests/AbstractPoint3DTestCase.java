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

package org.arakhne.afc.math.geometry.base.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Tuple3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;

@SuppressWarnings("all")
public abstract class AbstractPoint3DTestCase<P extends Point3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>,
		TT extends Tuple3D>
		extends AbstractTuple3DTestCase<TT> {
	
	public abstract P createPoint(double x, double y, double z);

	public abstract V createVector(double x, double y, double z);
	
	@DisplayName("isCollinearPoints")
	@Nested
	public class IsCollinearPoints {

		@DisplayName("#1")
		@Test
		public final void test_1() {
			assertTrue(Point3D.isCollinearPoints(0, 0, 0, 0, 0, 0, 0, 0, 0));
		}

		@DisplayName("#2")
		@Test
		public final void test_2() {
			assertTrue(Point3D.isCollinearPoints(-6, -4, 0, -1, 3, 0, 4, 10, 0));
		}

		@DisplayName("#3")
		@Test
		public final void test_3() {
			assertFalse(Point3D.isCollinearPoints(0, 0, 0, 1, 1, 0, 1, -5, 0));
		}

	}

	@DisplayName("getDistancePointPoint")
	@Nested
	public class GetDistancePointPoint {

		@DisplayName("#1")
		@Test
		public final void test_1() {
			assertEpsilonEquals(0, Point3D.getDistancePointPoint(0, 0, 0, 0, 0, 0));
		}

		@DisplayName("#2")
		@Test
		public final void test_2() {
			assertEpsilonEquals(Math.sqrt(5), Point3D.getDistancePointPoint(0, 0, 0, 1, 2, 0));
		}

		@DisplayName("#3")
		@Test
		public final void test_3() {
			assertEpsilonEquals(Math.sqrt(2), Point3D.getDistancePointPoint(0, 0, 0, 1, 1, 0));
		}

	}

	@DisplayName("getDistanceSquaredPointPoint")
	@Nested
	public class GetDistanceSquaredPointPoint {

		@DisplayName("#1")
		@Test
		public final void test_1() {
			assertEpsilonEquals(0, Point3D.getDistanceSquaredPointPoint(0, 0, 0, 0, 0, 0));
		}

		@DisplayName("#2")
		@Test
		public final void test_2() {
			assertEpsilonEquals(5, Point3D.getDistanceSquaredPointPoint(0, 0, 0, 1, 2, 0));
		}


		@DisplayName("#3")
		@Test
		public final void test_3() {
			assertEpsilonEquals(2, Point3D.getDistanceSquaredPointPoint(0, 0, 0, 1, 1, 0));
		}

	}

	@DisplayName("getDistanceL1PointPoint")
	@Nested
	public class GetDistanceL1PointPoint {

		@DisplayName("#1")
		@Test
		public final void test_1() {
			assertEpsilonEquals(4, Point3D.getDistanceL1PointPoint(1.0, 2.0, 0, 3.0, 0, 0));
		}

		@DisplayName("#2")
		@Test
		public final void test_2() {
			assertEpsilonEquals(0, Point3D.getDistanceL1PointPoint(1.0, 2.0, 0, 1 ,2, 0));
		}

		@DisplayName("#3")
		@Test
		public final void test_3() {
			assertEpsilonEquals(0, Point3D.getDistanceL1PointPoint(1, 2, 0, 1.0, 2.0, 0));
		}

		@DisplayName("#4")
		@Test
		public final void test_4() {
			assertEpsilonEquals(4, Point3D.getDistanceL1PointPoint(1.0, 2.0, 0, -1, 0, 0));
		}

	}

	@DisplayName("getDistanceLinfPointPoint")
	@Nested
	public class GetDistanceLibfPointPoint {

		@DisplayName("#1")
		@Test
		public final void test_1() {
			assertEpsilonEquals(2, Point3D.getDistanceLinfPointPoint(1.0, 2.0, 0, 3.0, 0, 0));
		}

		@DisplayName("#2")
		@Test
		public final void test_2() {
			assertEpsilonEquals(0, Point3D.getDistanceLinfPointPoint(1.0, 2.0, 0, 1, 2, 0));
		}

		@DisplayName("#3")
		@Test
		public final void test_3() {
			assertEpsilonEquals(1, Point3D.getDistanceLinfPointPoint(1, 2., 1.0, 0, 2.0, 0));
		}

		@DisplayName("#4")
		@Test
		public final void test_4() {
			assertEpsilonEquals(2, Point3D.getDistanceLinfPointPoint(1.0, 2.0, 0, -1, 0, 0));
		}

	}

	@DisplayName("getDistanceSquared(Point3d)")
	@Nested
	public class GetDistanceSquared {

		private Point3D point;
		private Point3D point2;
		private Point3D point3;
		private Point3D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(0, 0, 0);
			this.point2 = createPoint(0, 0, 0);
			this.point3 = createPoint(1, 2, 0);
			this.point4 = createPoint(1, 1, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, point.getDistanceSquared(point2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			assertEpsilonEquals(5, point.getDistanceSquared(point3));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			assertEpsilonEquals(2, point.getDistanceSquared(point4));
		}

	}
	
	@DisplayName("getDistance(Point3D)")
	@Nested
	public class GetDistance {

		private Point3D point;
		private Point3D point2;
		private Point3D point3;
		private Point3D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(0, 0, 0);
			this.point2 = createPoint(0, 0, 0);
			this.point3 = createPoint(1, 2, 0);
			this.point4 = createPoint(1, 1, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, point.getDistance(point2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(5), point.getDistance(point3));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(2), point.getDistance(point4));
		}

	}

	@DisplayName("getDistanceL1(Point3D)")
	@Nested
	public class GetDistanceL1 {

		private Point3D point;
		private Point3D point2;
		private Point3D point3;
		private Point3D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(3, 0, 0);
			this.point3 = createPoint(1, 2, 0);
			this.point4 = createPoint(-1, 0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, point.getDistanceL1(point2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, point.getDistanceL1(point));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, point.getDistanceL1(point3));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, point.getDistanceL1(point4));
		}

	}

	@DisplayName("getDistanceLinf(Point3d)")
	@Nested
	public class GetDistanceLinf {

		private Point3D point;
		private Point3D point2;
		private Point3D point3;
		private Point3D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(3, 0, 0);
			this.point3 = createPoint(1, 2, 0);
			this.point4 = createPoint(-1, 0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, point.getDistanceLinf(point2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, point.getDistanceLinf(point));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, point.getDistanceLinf(point3));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, point.getDistanceLinf(point4));
		}

	}

	@DisplayName("getIdistanceL1(Point3d)")
	@Nested
	public class GetIdistanceL1 {

		private Point3D point;
		private Point3D point2;
		private Point3D point3;
		private Point3D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(3, 0, 0);
			this.point3 = createPoint(1, 2, 0);
			this.point4 = createPoint(-1, 0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(4, point.getIdistanceL1(point2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(0, point.getIdistanceL1(point));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(0, point.getIdistanceL1(point3));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(4, point.getIdistanceL1(point4));
		}

	}

	@DisplayName("getIdistanceLinf(Point3d)")
	@Nested
	public class GetIdistanceLinf {

		private Point3D point;
		private Point3D point2;
		private Point3D point3;
		private Point3D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(3, 0, 0);
			this.point3 = createPoint(1, 2, 0);
			this.point4 = createPoint(-1, 0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(2, point.getIdistanceLinf(point2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(0, point.getIdistanceLinf(point));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(0, point.getIdistanceLinf(point3));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(2, point.getIdistanceLinf(point4));
		}

	}

	@DisplayName("toUnmodifiable")
	@Nested
	public class ToUnmodifiable {

		private Point3D origin;

		@BeforeEach
		public void setUp() {
			this.origin = createPoint(2, 3, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Point3D immutable = origin.toUnmodifiable();
			assertEpsilonEquals(origin, immutable);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeMutable(origin);
			Point3D immutable = origin.toUnmodifiable();
			assertEpsilonEquals(origin, immutable);
			assertEpsilonEquals(origin, immutable);
		}

	}


	@DisplayName("clone")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void testClonePoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D origin = createPoint(23, 45, 0);
		Tuple3D clone = origin.clone();
		assertNotNull(clone);
		assertNotSame(origin, clone);
		assertEpsilonEquals(origin.getX(), clone.getX());
		assertEpsilonEquals(origin.getY(), clone.getY());
	}

	@DisplayName("p + Vector3D")
	@Nested
	public class OpertorPlusVector3D {

		private Point3D point;
		private Point3D point2;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(3, 0, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = point.operator_plus(vector1);
			assertFpPointEquals(1, 2, 0, r);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = point.operator_plus(vector2);
			assertFpPointEquals(2, 4, 0, r);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = point.operator_plus(vector3);
			assertFpPointEquals(2, -3, 0, r);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = point2.operator_plus(vector1);
			assertFpPointEquals(3, 0, 0, r);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = point2.operator_plus(vector2);
			assertFpPointEquals(4, 2, 0, r);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = point2.operator_plus(vector3);
			assertFpPointEquals(4, -5, 0, r);
		}

	}

	@DisplayName("p - Vector3D")
	@Nested
	public class OpertorMinusVector3D {

		private Point3D point;
		private Point3D point2;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(3, 0, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = point.operator_minus(vector1);
			assertFpPointEquals(1, 2, 0, r);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = point.operator_minus(vector2);
			assertFpPointEquals(0, 0, 0, r);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = point.operator_minus(vector3);
			assertFpPointEquals(0, 7, 0, r);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = point2.operator_minus(vector1);
			assertFpPointEquals(3, 0, 0, r);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = point2.operator_minus(vector2);
			assertFpPointEquals(2, -2, 0, r);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = point2.operator_minus(vector3);
			assertFpPointEquals(2, 5, 0, r);
		}

	}

	@DisplayName("p - Point3D")
	@Nested
	public class OpertorMinusPoint3D {

		private Point3D point;
		private Point3D point2;
		private Point3D vector;
		private Point3D vector2;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(0, 0, 0);
			this.point2 = createPoint(1, 0, 0);
			this.vector = createPoint(-1.2, -1.2, 0);
			this.vector2 = createPoint(2.0, 1.5, 0);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var newVector = point.operator_minus(vector);
			assertFpVectorEquals(1.2, 1.2, 0, newVector);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var newVector = point2.operator_minus(vector2);
			assertFpVectorEquals(-1.0, -1.5, 0, newVector); 
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var newVector = point.operator_minus(vector);
			assertFpVectorEquals(1, 1, 0, newVector);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var newVector = point2.operator_minus(vector2);
			assertFpVectorEquals(-1, -2, 0, newVector); 
		}

	}

	@DisplayName("p == Tuple3D")
	@Nested
	public class OpertorEqualsTuple3D {

		private Point3D point;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(49, -2, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(point.operator_equals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(point.operator_equals(point));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(point.operator_equals(createPoint(49, -3, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(point.operator_equals(createPoint(0, 0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(point.operator_equals(createPoint(49, -2, 0)));
		}

	}

	@DisplayName("p != Tuple3D")
	@Nested
	public class OpertorNotEqualsTuple3D {

		private Point3D point;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(49, -2, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(point.operator_notEquals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(point.operator_notEquals(point));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(point.operator_notEquals(createPoint(49, -3, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(point.operator_notEquals(createPoint(0, 0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(point.operator_notEquals(createPoint(49, -2, 0)));
		}

	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsObject {

		private Point3D point;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(49, -2, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(point.equals((Object) null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(point.equals((Object) point));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(point.equals((Object) createPoint(49, -3, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(point.equals((Object) createPoint(0, 0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(point.equals((Object) createPoint(49, -2, 0)));
		}

	}

	@DisplayName("p .. Point3D")
	@Nested
	public class OperatorUpToPoint3D {

		private Point3D point;
		private Point3D point2;
		private Point3D point3;
		private Point3D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(0, 0, 0);
			this.point2 = createPoint(0, 0, 0);
			this.point3 = createPoint(1, 2, 0);
			this.point4 = createPoint(1, 1, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, point.operator_upTo(point2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(5), point.operator_upTo(point3));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(2), point.operator_upTo(point4));
		}

	}

	@DisplayName("p ?: Point3D")
	@Nested
	public class OperatorElvisPoint3D {

		private Point3D orig1;
		private Point3D orig2;
		private Point3D param;

		@BeforeEach
		public void setUp() {
			this.orig1 = createPoint(45, -78, 0);
			this.orig2 = createPoint(0, 0, 0);
			this.param = createPoint(-5, -1.4, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = orig1.operator_elvis(null);
			assertSame(orig1, result);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = orig1.operator_elvis(orig1);
			assertSame(orig1, result);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = orig1.operator_elvis(param);
			assertSame(orig1, result);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = orig2.operator_elvis(null);
			assertNull(result);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = orig2.operator_elvis(orig2);
			assertSame(orig2, result);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = orig2.operator_elvis(param);
			assertSame(param, result);
		}

	}

	@DisplayName("add(Point3D,Vector3D)")
	@Nested
	public class AddPoint3DVector3D {

		private Point3D point;
		private Point3D point2;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(3, 0, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
			assumeMutable(this.point);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(point, vector1);
			assertFpPointEquals(1, 2, 0, point);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(point, vector2);
			assertFpPointEquals(2, 4, 0, point);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(point, vector3);
			assertFpPointEquals(3, -1, 0, point);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(point2, vector1);
			assertFpPointEquals(3, 0, 0, point);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(point2, vector2);
			assertFpPointEquals(4, 2, 0, point);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(point2, vector3);
			assertFpPointEquals(4, -5, 0, point);
		}

	}

	@DisplayName("add(Vector3D,Point3D)")
	@Nested
	public class AddVector3DPoint3D {

		private Point3D point;
		private Point3D point2;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(3, 0, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
			assumeMutable(this.point);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(vector1, point);
			assertFpPointEquals(1, 2, 0, point);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(vector2, point);
			assertFpPointEquals(2, 4, 0, point);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(vector3, point);
			assertFpPointEquals(3, -1, 0, point);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(vector1, point2);
			assertFpPointEquals(3, 0, 0, point);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(vector2, point2);
			assertFpPointEquals(4, 2, 0, point);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(vector3, point2);
			assertFpPointEquals(4, -5, 0, point);
		}

	}

	@DisplayName("add(Vector3D)")
	@Nested
	public class AddVector3D {

		private Point3D point;
		private Point3D point2;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(3, 0, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
			assumeMutable(this.point);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(vector1);
			assertFpPointEquals(1, 2, 0, point);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(vector2);
			assertFpPointEquals(2, 4, 0, point);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(vector3);
			assertFpPointEquals(3, -1, 0, point);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(vector1);
			assertFpPointEquals(3, -1, 0, point);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(vector2);
			assertFpPointEquals(4, 1, 0, point);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.add(vector3);
			assertFpPointEquals(5, -4, 0, point);
		}

	}

	@DisplayName("scaleAdd(double,Vector3D,Point3D)")
	@Nested
	public class ScaleAddDoubleVector3DPoint3D {

		private Point3D point;
		private Point3D point2;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(3, 0, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
			assumeMutable(this.point);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, vector1, point);
			assertFpPointEquals(1, 2, 0, point);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, vector2, point);
			assertFpPointEquals(-1.5, -3, 0, point);
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, vector3, point);
			assertFpPointEquals(1, -15.5, 0, point);
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, vector1, point2);
			assertFpPointEquals(3, 0, 0, point);
		}

		@DisplayName("With double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, vector2, point2);
			assertFpPointEquals(5.5, 5, 0, point);
		}

		@DisplayName("With double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, vector3, point2);
			assertFpPointEquals(0.5, 12.5, 0, point);
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, vector1, point);
			assertIntPointEquals(1, 2, 0, point);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2.5, vector2, point);
			assertIntPointEquals(-1, -3, 0, point);
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2.5, vector3, point);
			assertIntPointEquals(2, -15, 0, point);
		}

		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2.5, vector1, point2);
			assertIntPointEquals(3, 0, 0, point);
		}

		@DisplayName("With int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2.5, vector2, point2);
			assertIntPointEquals(6, 5, 0, point);
		}

		@DisplayName("With int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2.5, vector3, point2);
			assertIntPointEquals(1, 13, 0, point);
		}

	}

	@DisplayName("scaleAdd(int,Vector3D,Point3D)")
	@Nested
	public class ScaleAddIntVector3DPoint3D {

		private Point3D point;
		private Point3D point2;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(3, 0, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
			assumeMutable(this.point);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, vector1, point);
			assertFpPointEquals(1, 2, 0, point);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, vector2, point);
			assertFpPointEquals(-1, -2, 0, point);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, vector3, point);
			assertFpPointEquals(1, -12, 0, point);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, vector1, point2);
			assertFpPointEquals(3, 0, 0, point);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, vector2, point2);
			assertFpPointEquals(5, 4, 0, point);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, vector3, point2);
			assertFpPointEquals(1, 10, 0, point);
		}

	}

	@DisplayName("scaleAdd(int,Point3D,Vector3D)")
	@Nested
	public class ScaleAddIntPoint3DVector3D {

		private Point3D point;
		private Point3D point2;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(3, 0, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
			assumeMutable(this.point);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, point, vector1);
			assertFpPointEquals(2, 4, 0, point);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, point, vector2);
			assertFpPointEquals(-3, -6, 0, point);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, point, vector3);
			assertFpPointEquals(-5, -17, 0, point);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, point2, vector1);
			assertFpPointEquals(-6, 0, 0, point);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, point2, vector2);
			assertFpPointEquals(7, 2, 0, point);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, point2, vector3);
			assertFpPointEquals(-5, -5, 0, point);
		}

	}

	@DisplayName("scaleAdd(double,Point3D,Vector3D)")
	@Nested
	public class ScaleAddDoublePoint3DVector3D {

		private Point3D point;
		private Point3D point2;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(3, 0, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
			assumeMutable(this.point);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, point, vector1);
			assertFpPointEquals(2.5, 5, 0, point);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, point, vector2);
			assertFpPointEquals(-5.25, -10.5, 0, point);
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, point2, vector1);
			assertFpPointEquals(-7.5, 0, 0, point);
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, point2, vector2);
			assertFpPointEquals(8.5, 2, 0, point);
		}

		@DisplayName("With double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, point2, vector3);
			assertFpPointEquals(-6.5, -5, 0, point);
		}

		@DisplayName("With double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, point, vector3);
			assertFpPointEquals(-12.125, -31.25, 0, point);
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, point, vector1);
			assertIntPointEquals(3, 5, 0, point);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, point, vector2);
			assertIntPointEquals(-6, -10, 0, point);
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, point, vector3);
			assertIntPointEquals(-14, -30, 0, point);
		}

		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, point2, vector1);
			assertIntPointEquals(-7, 0, 0, point);
		}

		@DisplayName("With int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, point2, vector2);
			assertIntPointEquals(9, 2, 0, point);
		}

		@DisplayName("With int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, point2, vector3);
			assertIntPointEquals(-6, -5, 0, point);
		}

	}

	@DisplayName("scaleAdd(int,Vector3D)")
	@Nested
	public class ScaleAddIntVector3D {

		private Point3D point;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
			assumeMutable(this.point);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, vector1);
			assertFpPointEquals(2, 4, 0, point);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, vector2);
			assertFpPointEquals(-3, -6, 0, point);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, vector3);
			assertFpPointEquals(-5, -17, 0, point);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, vector1);
			assertFpPointEquals(10, 34, 0, point);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, vector2);
			assertFpPointEquals(21, 70, 0, point);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, vector3);
			assertFpPointEquals(-41, -145, 0, point);
		}

	}

	@DisplayName("scaleAdd(double,Vector3D)")
	@Nested
	public class ScaleAddDoubleVector3D {

		private Point3D point;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
			assumeMutable(this.point);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, vector1);
			assertFpPointEquals(2.5, 5, 0, point);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, vector2);
			assertFpPointEquals(-5.25, -10.5, 0, point);
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, vector3);
			assertFpPointEquals(-12.125, -31.25, 0, point);
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, vector1);
			assertFpPointEquals(30.312, 78.125, 0, point);
		}

		@DisplayName("With double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, vector2);
			assertFpPointEquals(76.781, 197.312, 0, point);
		}

		@DisplayName("With double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, vector3);
			assertFpPointEquals(-190.95, -498.28, 0, point);
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, vector1);
			assertIntPointEquals(3, 5, 0, point);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, vector2);
			assertIntPointEquals(-6, -10, 0, point);
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, vector3);
			assertIntPointEquals(-14, -30, 0, point);
		}

		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, vector1);
			assertIntPointEquals(35, 75, 0, point);
		}

		@DisplayName("With int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, vector2);
			assertIntPointEquals(89, 190, 0, point);
		}

		@DisplayName("With int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, vector3);
			assertIntPointEquals(-221, -480, 0, point);
		}

	}

	@DisplayName("sub(Point3D,Vector3D)")
	@Nested
	public class SubPoint3DVector3D {

		private Point3D point;
		private Point3D point2;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.point2 = createPoint(-1, 2, -3);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
			assumeMutable(this.point);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.sub(point, vector1);
			assertFpPointEquals(1, 2, 0, point);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.sub(point, vector2);
			assertFpPointEquals(0, 0, 0, point);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.sub(point, vector3);
			assertFpPointEquals(-1, 5, 0, point);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.sub(point2, vector1);
			assertFpPointEquals(3, 0, 0, point);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.sub(point2, vector2);
			assertFpPointEquals(2, -2, 0, point);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.sub(point2, vector3);
			assertFpPointEquals(2, 5, 0, point);
		}
	}

	@DisplayName("sub(Vector3D)")
	@Nested
	public class SubVector3D {

		private Point3D point;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
			assumeMutable(this.point);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.sub(vector1);
			assertFpPointEquals(1, 2, 0, point);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.sub(vector2);
			assertFpPointEquals(0, 0, 0, point);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.sub(vector3);
			assertFpPointEquals(-1, 5, 0, point);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.sub(vector1);
			assertFpPointEquals(-1, 5, 0, point);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.sub(vector2);
			assertFpPointEquals(-2, 3, 0, point);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.sub(vector3);
			assertFpPointEquals(-3, 8, 0, point);
		}

	}

	@DisplayName("p += Vector3D")
	@Nested
	public class OperatorAddVector3D {

		private Point3D point;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
			assumeMutable(this.point);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.operator_add(vector1);
			assertFpPointEquals(1, 2, 0, point);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.operator_add(vector2);
			assertFpPointEquals(2, 4, 0, point);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.operator_add(vector3);
			assertFpPointEquals(3, -1, 0, point);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.operator_add(vector1);
			assertFpPointEquals(3, -1, 0, point);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.operator_add(vector2);
			assertFpPointEquals(4, 1, 0, point);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.operator_add(vector3);
			assertFpPointEquals(5, -4, 0, point);
		}

	}

	@DisplayName("p -= Vector3D")
	@Nested
	public class OperatorRemoveVector3D {

		private Point3D point;
		private Vector3D vector1;
		private Vector3D vector2;
		private Vector3D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2, 0);
			this.vector1 = createVector(0, 0, 0);
			this.vector2 = createVector(1, 2, 0);
			this.vector3 = createVector(1, -5, 0);
			assumeMutable(this.point);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.operator_remove(vector1);
			assertFpPointEquals(1, 2, 0, point);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.operator_remove(vector2);
			assertFpPointEquals(0, 0, 0, point);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.operator_remove(vector3);
			assertFpPointEquals(-1, 5, 0, point);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.operator_remove(vector1);
			assertFpPointEquals(-1, 5, 0, point);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.operator_remove(vector2);
			assertFpPointEquals(-2, 3, 0, point);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			point.operator_remove(vector3);
			assertFpPointEquals(-3, 8, 0, point);
		}

	}

}
