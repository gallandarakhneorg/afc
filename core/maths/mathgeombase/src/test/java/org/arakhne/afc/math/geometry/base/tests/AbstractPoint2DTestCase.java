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

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractPoint2DTestCase<P extends Point2D<? super P, ? super V>, V extends Vector2D<? super V, ? super P>,
		TT extends Tuple2D>
		extends AbstractTuple2DTestCase<TT> {
	
	public abstract P createPoint(double x, double y);

	public abstract V createVector(double x, double y);
	
	@DisplayName("isCollinearPoints")
	@Nested
	public class IsCollinearPoints {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Point2D.isCollinearPoints(0, 0, 0, 0, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Point2D.isCollinearPoints(-6, -4, -1, 3, 4, 10));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Point2D.isCollinearPoints(0, 0, 1, 1, 1, -5));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertInlineParameterUsage(Point2D.class, "isCollinearPoints", //$NON-NLS-1$
					double.class, double.class, double.class, double.class, double.class, double.class);
		}

	}

	@DisplayName("getDistancePointPoint")
	@Nested
	public class GetDistancePointPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Point2D.getDistancePointPoint(0, 0, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(5), Point2D.getDistancePointPoint(0, 0, 1, 2));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(2), Point2D.getDistancePointPoint(0, 0, 1, 1));
		}

	}


	@DisplayName("getDistanceSquaredPointPoint")
	@Nested
	public class GetDistanceSquaredPointPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Point2D.getDistanceSquaredPointPoint(0, 0, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, Point2D.getDistanceSquaredPointPoint(0, 0, 1, 2));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Point2D.getDistanceSquaredPointPoint(0, 0, 1, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertInlineParameterUsage(Point2D.class, "getDistancePointPoint", double.class, double.class, double.class, double.class); //$NON-NLS-1$
		}
	
	}

	@DisplayName("getDistanceL1PointPoint")
	@Nested
	public class GetDistanceL1PointPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, Point2D.getDistanceL1PointPoint(1.0, 2.0, 3.0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Point2D.getDistanceL1PointPoint(1.0, 2.0, 1 ,2));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Point2D.getDistanceL1PointPoint(1, 2, 1.0, 2.0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, Point2D.getDistanceL1PointPoint(1.0, 2.0, -1, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertInlineParameterUsage(Point2D.class, "getDistanceL1PointPoint", double.class, double.class, double.class, double.class); //$NON-NLS-1$
		}

	}

	@DisplayName("getDistanceLinfPointPoint")
	@Nested
	public class GetDistanceLinfPointPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Point2D.getDistanceLinfPointPoint(1.0,2.0,3.0,0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Point2D.getDistanceLinfPointPoint(1.0,2.0,1,2));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Point2D.getDistanceLinfPointPoint(1,2,1.0,2.0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Point2D.getDistanceLinfPointPoint(1.0,2.0,-1,0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertInlineParameterUsage(Point2D.class, "getDistanceLinfPointPoint", double.class, double.class, double.class, double.class); //$NON-NLS-1$
		}

	}

	@DisplayName("getDistanceSquared(Point2D)")
	@Nested
	public class GetDistanceSquaredPoint2D {

		private Point2D point;
		private Point2D point2;
		private Point2D point3;
		private Point2D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(0, 0);
			this.point2 = createPoint(0, 0);
			this.point3 = createPoint(1, 2);
			this.point4 = createPoint(1, 1);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, point.getDistanceSquared(point2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, point.getDistanceSquared(point3));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, point.getDistanceSquared(point4));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertInlineParameterUsage(Point2D.class, "getDistanceSquaredPointPoint", double.class, double.class, double.class, double.class); //$NON-NLS-1$
		}

	}

	@DisplayName("getDistance(Point2D)")
	@Nested
	public class GetDistancePoint2D {

		private Point2D point;
		private Point2D point2;
		private Point2D point3;
		private Point2D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(0, 0);
			this.point2 = createPoint(0, 0);
			this.point3 = createPoint(1, 2);
			this.point4 = createPoint(1, 1);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, point.getDistance(point2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(5), point.getDistance(point3));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(2), point.getDistance(point4));
		}

	}

	@DisplayName("getDistanceL1(Point2D)")
	@Nested
	public class GetDistanceL1Point2D {

		private Point2D point;
		private Point2D point2;
		private Point2D point3;
		private Point2D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(0, 0);
			this.point2 = createPoint(3, 0);
			this.point3 = createPoint(1, 2);
			this.point4 = createPoint(-1, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, point.getDistanceL1(point2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, point.getDistanceL1(point));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, point.getDistanceL1(point3));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, point.getDistanceL1(point4));
		}

	}

	@DisplayName("getDistanceLinf(Point2D)")
	@Nested
	public class GetDistanceLinfPoint2D {

		private Point2D point;
		private Point2D point2;
		private Point2D point3;
		private Point2D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(0, 0);
			this.point2 = createPoint(3, 0);
			this.point3 = createPoint(1, 2);
			this.point4 = createPoint(-1, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, point.getDistanceLinf(point2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, point.getDistanceLinf(point));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, point.getDistanceLinf(point3));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, point.getDistanceLinf(point4));
		}

	}

	@DisplayName("getIdistanceL1(Point2D)")
	@Nested
	public class GetIdistanceL1Point2D {

		private Point2D point;
		private Point2D point2;
		private Point2D point3;
		private Point2D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(0, 0);
			this.point2 = createPoint(3, 0);
			this.point3 = createPoint(1, 2);
			this.point4 = createPoint(-1, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(3, point.getIdistanceL1(point2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, point.getIdistanceL1(point));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(3, point.getIdistanceL1(point3));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, point.getIdistanceL1(point4));
		}

	}

	@DisplayName("getIdistanceLinf(Point2D)")
	@Nested
	public class GetIdistanceLinfPoint2D {

		private Point2D point;
		private Point2D point2;
		private Point2D point3;
		private Point2D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(0, 0);
			this.point2 = createPoint(3, 0);
			this.point3 = createPoint(1, 2);
			this.point4 = createPoint(-1, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(3, point.getIdistanceLinf(point2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, point.getIdistanceLinf(point));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(2, point.getIdistanceLinf(point3));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, point.getIdistanceLinf(point4));
		}

	}

	@DisplayName("ToUnmodifiable")
	@Nested
	public class ToUnmodifiable {

		private Point2D origin;

		@BeforeEach
		public void setUp() {
			this.origin = createPoint(2, 3);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeMutable(origin);
			Point2D immutable = origin.toUnmodifiable();
			assertNotSame(origin, immutable);
			assertEpsilonEquals(origin, immutable);
		}

	}

	@DisplayName("clone")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void testClonePoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D origin = createPoint(23, 45);
		Tuple2D clone = origin.clone();
		assertNotNull(clone);
		assertNotSame(origin, clone);
		assertEpsilonEquals(origin.getX(), clone.getX());
		assertEpsilonEquals(origin.getY(), clone.getY());
	}

	@DisplayName("this + Vector2D")
	@Nested
	public class OperatorPlusVector2D {

		private Point2D point;
		private Point2D point2;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.point2 = createPoint(3, 0);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = point.operator_plus(vector1);
			assertFpPointEquals(1, 2, r);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = point.operator_plus(vector2);
			assertFpPointEquals(2, 4, r);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = point.operator_plus(vector3);
			assertFpPointEquals(2, -3, r);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = point2.operator_plus(vector1);
			assertFpPointEquals(3, 0, r);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = point2.operator_plus(vector2);
			assertFpPointEquals(4, 2, r);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = point2.operator_plus(vector3);
			assertFpPointEquals(4, -5, r);
		}

	}

	@DisplayName("this + double")
	@Nested
	public class OperatorPlusDouble {

		private Point2D point;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assumeFalse(isIntCoordinates());
	        assertFpPointEquals(46.6, 47.6, point.operator_plus(45.6));
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assumeTrue(isIntCoordinates());
	        assertIntPointEquals(47, 48, point.operator_plus(45.6));
	    }

	}

	@DisplayName("this - Vector2D")
	@Nested
	public class OperatorMinusVector2D {

		private Point2D point;
		private Point2D point2;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.point2 = createPoint(3, 0);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = point.operator_minus(vector1);
			assertFpPointEquals(1, 2, r);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = point.operator_minus(vector2);
			assertFpPointEquals(0, 0, r);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = point.operator_minus(vector3);
			assertFpPointEquals(0, 7, r);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = point2.operator_minus(vector1);
			assertFpPointEquals(3, 0, r);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = point2.operator_minus(vector2);
			assertFpPointEquals(2, -2, r);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var r = point2.operator_minus(vector3);
			assertFpPointEquals(2, 5, r);
		}

	}

	@DisplayName("this - Point2D")
	@Nested
	public class OperatorMinusPoint2D {

		private Point2D point;
		private Point2D point2;
		private Point2D point3;
		private Point2D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.point2 = createPoint(1, 0);
			this.point3 = createPoint(-1.2, -1.2);
			this.point4 = createPoint(2., 1.5);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var newVector = point.operator_minus(point);
			assertFpVectorEquals(0., 0., newVector);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var newVector = point.operator_minus(point2);
			assertFpVectorEquals(0, 2, newVector); 
		}
	
		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var newVector = point.operator_minus(point3);
			assertFpVectorEquals(2.2, 3.2, newVector); 
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var newVector = point.operator_minus(point4);
			assertFpVectorEquals(-1.0, .5, newVector); 
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var newVector = point.operator_minus(point);
			assertFpVectorEquals(0., 0., newVector);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var newVector = point.operator_minus(point2);
			assertFpVectorEquals(0, 2, newVector); 
		}
	
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var newVector = point.operator_minus(point3);
			assertFpVectorEquals(2., -3., newVector); 
		}

		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var newVector = point.operator_minus(point4);
			assertFpVectorEquals(-1., 0., newVector); 
		}

	}

	@DisplayName("this - double")
	@Nested
	public class OperatorMinusDouble {

		private Point2D point;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assumeTrue(isIntCoordinates());
	        assertIntPointEquals(-45, -44, point.operator_minus(45.6));
	    }
	
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assumeFalse(isIntCoordinates());
	        assertFpPointEquals(-44.6, -43.6, point.operator_minus(45.6));
	    }

	}

	@DisplayName("this == Point2D")
	@Nested
	public class OperatorEqualsPoint2D {

		private Point2D point;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(49, -2);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(point.operator_equals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(point.operator_equals(point));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(point.operator_equals(createPoint(49, -3)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(point.operator_equals(createPoint(0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(point.operator_equals(createPoint(49, -2)));
		}

	}

	@DisplayName("this != Point2D")
	@Nested
	public class OperatorNotEqualsPoint2D {

		private Point2D point;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(49, -2);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(point.operator_notEquals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(point.operator_notEquals(point));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(point.operator_notEquals(createPoint(49, -3)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(point.operator_notEquals(createPoint(0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(point.operator_notEquals(createPoint(49, -2)));
		}
	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsObject {

		private Point2D point;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(49, -2);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(point.equals((Object) null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(point.equals((Object) point));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(point.equals((Object) createPoint(49, -3)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(point.equals((Object) createPoint(0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(point.equals((Object) createPoint(49, -2)));
		}
	}

	@DisplayName("this .. Point2D")
	@Nested
	public class OperatorUpToPoint2D {

		private Point2D point;
		private Point2D point2;
		private Point2D point3;
		private Point2D point4;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(0, 0);
			this.point2 = createPoint(0, 0);
			this.point3 = createPoint(1, 2);
			this.point4 = createPoint(1, 1);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, point.operator_upTo(point2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(5), point.operator_upTo(point3));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(2), point.operator_upTo(point4));
		}

	}

	@DisplayName("this ?: Point2D")
	@Nested
	public class OperatorElvisPoint2D {

		private Point2D orig1 = createPoint(45, -78);
		private Point2D orig2 = createPoint(0, 0);
		private Point2D param = createPoint(-5, -1.4);

		@BeforeEach
		public void setUp() {
			this.orig1 = createPoint(45, -78);
			this.orig2 = createPoint(0, 0);
			this.param = createPoint(-5, -1.4);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var result = orig1.operator_elvis(null);
			assertSame(orig1, result);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var result = orig1.operator_elvis(orig1);
			assertSame(orig1, result);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var result = orig1.operator_elvis(param);
			assertSame(orig1, result);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var result = orig2.operator_elvis(null);
			assertNull(result);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var result = orig2.operator_elvis(orig2);
			assertSame(orig2, result);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var result = orig2.operator_elvis(param);
			assertSame(param, result);
		}

	}

	@DisplayName("add(Point2D, Vector2D)")
	@Nested
	public class AddPoint2DVector2D {

		private Point2D point;
		private Point2D point2;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.point2 = createPoint(3, 0);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
			assumeMutable(this.point);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(point, vector1);
			assertFpPointEquals(1, 2, point);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(point, vector2);
			assertFpPointEquals(2, 4, point);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(point, vector3);
			assertFpPointEquals(2, -3, point);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(point2, vector1);
			assertFpPointEquals(3, 0, point);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(point2, vector2);
			assertFpPointEquals(4, 2, point);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(point2, vector3);
			assertFpPointEquals(4, -5, point);
		}

	}

	@DisplayName("add(Vector2D, Point2D)")
	@Nested
	public class AddVector2DPoint2D {

		private Point2D point;
		private Point2D point2;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.point2 = createPoint(3, 0);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
			assumeMutable(this.point);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(vector1, point);
			assertFpPointEquals(1, 2, point);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(vector2, point);
			assertFpPointEquals(2, 4, point);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(vector3, point);
			assertFpPointEquals(2, -3, point);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(vector1, point2);
			assertFpPointEquals(3, 0, point);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(vector2, point2);
			assertFpPointEquals(4, 2, point);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(vector3, point2);
			assertFpPointEquals(4, -5, point);
		}

	}

	@DisplayName("add(Vector2D)")
	@Nested
	public class AddVector2D {

		private Point2D point;
		private Point2D point2;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.point2 = createPoint(3, 0);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
			assumeMutable(this.point);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(vector1);
			assertFpPointEquals(1, 2, point);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(vector2);
			assertFpPointEquals(2, 4, point);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(vector3);
			assertFpPointEquals(2, -3, point);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(vector1);
			assertFpPointEquals(1, 2, point);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(vector2);
			assertFpPointEquals(2, 4, point);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.add(vector3);
			assertFpPointEquals(2, -3, point);
		}

	}

	@DisplayName("scaleAdd(double,Vector2D,Point2D)")
	@Nested
	public class ScaleAddDoubleVector2DPoint2D {

		private Point2D point;
		private Point2D point2;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;

		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.point2 = createPoint(3, 0);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
			assumeMutable(this.point);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, vector1, point);
			assertFpPointEquals(1, 2, point);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, vector2, point);
			assertFpPointEquals(-1.5, -3, point);
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, vector3, point);
			assertFpPointEquals(3.5, -10.5, point);
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, vector1, point2);
			assertFpPointEquals(3, 0, point);
		}

		@DisplayName("With double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, vector2, point2);
			assertFpPointEquals(5.5, 5, point);
		}

		@DisplayName("With double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, vector3, point2);
			assertFpPointEquals(0.5, 12.5, point);
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, vector1, point);
			assertIntPointEquals(1, 2, point);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, vector2, point);
			assertIntPointEquals(-1, -3, point);
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, vector3, point);
			assertIntPointEquals(2, -15, point);
		}

		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, vector1, point2);
			assertIntPointEquals(3, 0, point);
		}

		@DisplayName("With int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, vector2, point2);
			assertIntPointEquals(6, 5, point);
		}

		@DisplayName("With int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, vector3, point2);
			assertIntPointEquals(1, 13, point);
		}

	}

	@DisplayName("scaleAdd(int,Vector2D,Point2D)")
	@Nested
	public class ScaleAddIntVector2DPoint2D {
	
		private Point2D point;
		private Point2D point2;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;
	
		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.point2 = createPoint(3, 0);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
			assumeMutable(this.point);
		}
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, vector1, point);
			assertFpPointEquals(1, 2, point);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, vector2, point);
			assertFpPointEquals(-1, -2, point);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, vector3, point);
			assertFpPointEquals(3, -8, point);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, vector1, point2);
			assertFpPointEquals(3, 0, point);
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, vector2, point2);
			assertFpPointEquals(5, 4, point);
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, vector3, point2);
			assertFpPointEquals(1, 10, point);
		}

	}

	@DisplayName("scaleAdd(int,Point2D,Vector2D)")
	@Nested
	public class ScaleAddIntPoint2DVector2D {
	
		private Point2D point;
		private Point2D point2;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;
	
		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.point2 = createPoint(3, 0);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
			assumeMutable(this.point);
		}
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, point, vector1);
			assertFpPointEquals(2, 4, point);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, point, vector2);
			assertFpPointEquals(-1, -2, point);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, point, vector3);
			assertFpPointEquals(3, -1, point);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, point2, vector1);
			assertFpPointEquals(-6, 0, point);
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, point2, vector2);
			assertFpPointEquals(7, 2, point);
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, point2, vector3);
			assertFpPointEquals(-5, -5, point);
		}

	}

	@DisplayName("scaleAdd(double,Point2D,Vector2D)")
	@Nested
	public class ScaleAddDoublePoint2DVector2D {
	
		private Point2D point;
		private Point2D point2;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;
	
		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.point2 = createPoint(3, 0);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
			assumeMutable(this.point);
		}
	
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, point, vector1);
			assertFpPointEquals(2.5, 5, point);
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, point, vector2);
			assertFpPointEquals(-1.5, -3, point);
		}
		
		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, point, vector3);
			assertFpPointEquals(3.5, 0, point);
		}
		
		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, point2, vector1);
			assertFpPointEquals(-7.5, 0, point);
		}
		
		@DisplayName("With double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, point2, vector2);
			assertFpPointEquals(8.5, 2, point);
		}
		
		@DisplayName("With double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, point2, vector3);
			assertFpPointEquals(-6.5, -5, point);
		}
		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, point, vector1);
			assertIntPointEquals(3, 5, point);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, point, vector2);
			assertIntPointEquals(-6, -10, point);
		}
		
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, point, vector3);
			assertIntPointEquals(-14, -30, point);
		}
		
		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, point2, vector1);
			assertIntPointEquals(-7, 0, point);
		}
		
		@DisplayName("With int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, point2, vector2);
			assertIntPointEquals(9, 2, point);
		}
		
		@DisplayName("With int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, point2, vector3);
			assertIntPointEquals(-6, -5, point);
		}

	}

	@DisplayName("scaleAdd(int,Vector2D)")
	@Nested
	public class ScaleAddIntVector2D {
	
		private Point2D point;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;
	
		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
			assumeMutable(this.point);
		}
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, vector1);
			assertFpPointEquals(2, 4, point);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, vector2);
			assertFpPointEquals(-1, -2, point);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, vector3);
			assertFpPointEquals(3, -1, point);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, vector1);
			assertFpPointEquals(-2, -4, point);
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(2, vector2);
			assertFpPointEquals(3, 6, point);
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.scaleAdd(-2, vector3);
			assertFpPointEquals(-1, -9, point);
		}


	}

	@DisplayName("scaleAdd(double,Vector2D)")
	@Nested
	public class ScaleAddDoubleVector2D {
	
		private Point2D point;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;
	
		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
			assumeMutable(this.point);
		}
	
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, vector1);
			assertFpPointEquals(2.5, 5, point);
		}
		
		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, vector2);
			assertFpPointEquals(-1.5, -3, point);
		}
		
		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, vector3);
			assertFpPointEquals(3.5, 0, point);
		}
		
		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, vector1);
			assertFpPointEquals(-2.5, -5, point);
		}
		
		@DisplayName("With double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(2.5, vector2);
			assertFpPointEquals(3.5, 7, point);
		}
		
		@DisplayName("With double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			point.scaleAdd(-2.5, vector3);
			assertFpPointEquals(-1.5, -10, point);
		}
		
		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, vector1);
			assertIntPointEquals(3, 5, point);
		}
		
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, vector2);
			assertIntPointEquals(-6, -10, point);
		}
		
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, vector3);
			assertIntPointEquals(-14, -30, point);
		}
		
		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, vector1);
			assertIntPointEquals(35, 75, point);
		}
		
		@DisplayName("With int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(2.5, vector2);
			assertIntPointEquals(89, 190, point);
		}
		
		@DisplayName("With int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			point.scaleAdd(-2.5, vector3);
			assertIntPointEquals(-221, -480, point);
		}

	}

	@DisplayName("sub(Point3D,Vector2D)")
	@Nested
	public class SubPoint3DVector2D {
	
		private Point2D point;
		private Point2D point2;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;
	
		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.point2 = createPoint(3, 0);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
			assumeMutable(this.point);
		}
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.sub(point, vector1);
			assertFpPointEquals(1, 2, point);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.sub(point, vector2);
			assertFpPointEquals(0, 0, point);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.sub(point, vector3);
			assertFpPointEquals(0, 7, point);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.sub(point2, vector1);
			assertFpPointEquals(3, 0, point);
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.sub(point2, vector2);
			assertFpPointEquals(2, -2, point);
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.sub(point2, vector3);
			assertFpPointEquals(2, 5, point);
		}

	}

	@DisplayName("sub(Vector2D)")
	@Nested
	public class SubVector2D {
	
		private Point2D point;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;
	
		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
			assumeMutable(this.point);
		}
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.sub(vector1);
			assertFpPointEquals(1, 2, point);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.sub(vector2);
			assertFpPointEquals(0, 0, point);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.sub(vector3);
			assertFpPointEquals(0, 7, point);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.sub(vector1);
			assertFpPointEquals(1, 2, point);
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.sub(vector2);
			assertFpPointEquals(0, 0, point);
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.sub(vector3);
			assertFpPointEquals(0, 7, point);
		}

	}

	@DisplayName("this += Vector2D")
	@Nested
	public class OperatorAddVector2D {
	
		private Point2D point;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;
	
		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
			assumeMutable(this.point);
		}
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.operator_add(vector1);
			assertFpPointEquals(1, 2, point);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.operator_add(vector2);
			assertFpPointEquals(2, 4, point);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.operator_add(vector3);
			assertFpPointEquals(2, -3, point);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.operator_add(vector1);
			assertFpPointEquals(1, 2, point);
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.operator_add(vector2);
			assertFpPointEquals(2, 4, point);
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.operator_add(vector3);
			assertFpPointEquals(2, -3, point);
		}

	}

	@DisplayName("this -= Vector2D")
	@Nested
	public class OperatorRemoveVector2D {
	
		private Point2D point;
		private Vector2D vector1;
		private Vector2D vector2;
		private Vector2D vector3;
	
		@BeforeEach
		public void setUp() {
			this.point = createPoint(1, 2);
			this.vector1 = createVector(0, 0);
			this.vector2 = createVector(1, 2);
			this.vector3 = createVector(1, -5);
			assumeMutable(this.point);
		}
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.operator_remove(vector1);
			assertFpPointEquals(1, 2, point);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.operator_remove(vector2);
			assertFpPointEquals(0, 0, point);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.operator_remove(vector3);
			assertFpPointEquals(0, 7, point);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.operator_remove(vector1);
			assertFpPointEquals(1, 2, point);
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.operator_remove(vector2);
			assertFpPointEquals(0, 0, point);
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			point.operator_remove(vector3);
			assertFpPointEquals(0, 7, point);
		}

	}

	@DisplayName("turn(double)")
	@Nested
	public class TurnDouble {
	
		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(1, 0);
			p.turn(Math.PI/2);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, 1);
			p.turn(Math.PI/2);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(-1, 0);
			p.turn(Math.PI/2);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, -1);
			p.turn(Math.PI/2);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("With double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(1, 0);
			p.turn(-Math.PI/2);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("With double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, -1);
			p.turn(-Math.PI/2);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("With double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(-1, 0);
			p.turn(-Math.PI/2);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("With double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, 1);
			p.turn(-Math.PI/2);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("With double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(12, 0);
			p.turn(Math.PI/6);
			assertFpPointEquals(10.392304, 6, p);
		}

		@DisplayName("With double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(12, 0);
			p.turn(-Math.PI/6);
			assertFpPointEquals(10.39230, -6, p);
		}

		@DisplayName("With double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(-4, 18);
			p.turn(Math.PI/11);
			assertFpPointEquals(-8.90916, 16.14394, p);
		}

		@DisplayName("With double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(-4, 18);
			p.turn(-Math.PI/11);
			assertFpPointEquals(1.23321, 18.39780, p);
		}

		@DisplayName("With double coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(1, 0));
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("With double coords #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(0, 1));
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("With double coords #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(-1, 0));
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("With double coords #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(0, -1));
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("With double coords #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(1, 0));
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("With double coords #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(0, -1));
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("With double coords #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(-1, 0));
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("With double coords #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(0, 1));
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("With double coords #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, 0);
			p.turn(Math.PI/6, createPoint(12, 0));
			assertFpPointEquals(10.392304, 6, p);
		}

		@DisplayName("With double coords #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, 0);
			p.turn(-Math.PI/6, createPoint(12, 0));
			assertFpPointEquals(10.39230, -6, p);
		}

		@DisplayName("With double coords #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, 0);
			p.turn(Math.PI/11, createPoint(-4, 18));
			assertFpPointEquals(-8.90916, 16.14394, p);
		}

		@DisplayName("With double coords #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			var p = createPoint(0, 0);
			p.turn(-Math.PI/11, createPoint(-4, 18));
			assertFpPointEquals(1.23321, 18.39780, p);
		}

		@DisplayName("With int coords #1")
		@EnumSource(CoordinateSystem2D.class)
		public void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var p = createPoint(1, 0);
			p.turn(Math.PI/2);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("With int coords #2")
		@EnumSource(CoordinateSystem2D.class)
		public void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var p = createPoint(0, 1);
			p.turn(Math.PI/2);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("With int coords #3")
		@EnumSource(CoordinateSystem2D.class)
		public void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var p = createPoint(-1, 0);
			p.turn(Math.PI/2);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("With int coords #4")
		@EnumSource(CoordinateSystem2D.class)
		public void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var p = createPoint(0, -1);
			p.turn(Math.PI/2);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("With int coords #5")
		@EnumSource(CoordinateSystem2D.class)
		public void int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var p = createPoint(1, 0);
			p.turn(-Math.PI/2);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("With int coords #6")
		@EnumSource(CoordinateSystem2D.class)
		public void int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var p = createPoint(0, -1);
			p.turn(-Math.PI/2);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("With int coords #7")
		@EnumSource(CoordinateSystem2D.class)
		public void int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var p = createPoint(-1, 0);
			p.turn(-Math.PI/2);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("With int coords #8")
		@EnumSource(CoordinateSystem2D.class)
		public void int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var p = createPoint(0, 1);
			p.turn(-Math.PI/2);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("With int coords #9")
		@EnumSource(CoordinateSystem2D.class)
		public void int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var p = createPoint(12, 0);
			p.turn(Math.PI/6);
			assertIntPointEquals(10, 6, p);
		}

		@DisplayName("With int coords #10")
		@EnumSource(CoordinateSystem2D.class)
		public void int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var p = createPoint(12, 0);
			p.turn(-Math.PI/6);
			assertIntPointEquals(10, -6, p);
		}

		@DisplayName("With int coords #11")
		@EnumSource(CoordinateSystem2D.class)
		public void int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var p = createPoint(-4, 18);
			p.turn(Math.PI/11);
			assertIntPointEquals(-9, 16, p);
		}

		@DisplayName("With int coords #12")
		@EnumSource(CoordinateSystem2D.class)
		public void int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			var p = createPoint(-4, 18);
			p.turn(-Math.PI/11);
			assertIntPointEquals(1, 18, p);
		}

	}

	@DisplayName("turn(double,Point2D,Point2D)")
	@Nested
	public class TurnDoublePoint {

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(1, 0));
			assertIntPointEquals(0, 1, p);
		}
	
		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(0, 1));
			assertIntPointEquals(-1, 0, p);
			assertIntPointEquals(0, 1, p);
		}
	
		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(-1, 0));
			assertIntPointEquals(0, -1, p);
			assertIntPointEquals(0, 1, p);
		}
	
		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(0, -1));
			assertIntPointEquals(1, 0, p);
			assertIntPointEquals(0, 1, p);
		}
	
		@DisplayName("With int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(1, 0));
			assertIntPointEquals(0, -1, p);
			assertIntPointEquals(0, 1, p);
		}
	
		@DisplayName("With int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(0, -1));
			assertIntPointEquals(-1, 0, p);
			assertIntPointEquals(0, 1, p);
		}
	
		@DisplayName("With int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(-1, 0));
			assertIntPointEquals(0, 1, p);
			assertIntPointEquals(0, 1, p);
		}
	
		@DisplayName("With int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(0, 1));
			assertIntPointEquals(1, 0, p);
			assertIntPointEquals(0, 1, p);
		}
	
		@DisplayName("With int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/6, createPoint(12, 0));
			assertIntPointEquals(10, 6, p);
			assertIntPointEquals(0, 1, p);
		}
	
		@DisplayName("With int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/6, createPoint(12, 0));
			assertIntPointEquals(10, -6, p);
			assertIntPointEquals(0, 1, p);
		}
	
		@DisplayName("With int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/11, createPoint(-4, 18));
			assertIntPointEquals(-9, 16, p);
			assertIntPointEquals(0, 1, p);
		}
	
		@DisplayName("With int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/11, createPoint(-4, 18));
			assertIntPointEquals(1, 18, p);
		}

	}

	@DisplayName("turn(double,Point2D,Point2D)")
	@Nested
	public class TurnDoublePoint2DPoint2D {
	
		private Point2D origin;
		private Point2D p;

		@BeforeEach
		public void setUp() {
			this.origin = createPoint(0, 0);
			this.p = createPoint(0, 0);
		}

		@DisplayName("With double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			p.turn(Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("With double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			p.turn(Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("With double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			p.turn(Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("With double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			p.turn(Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("With double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			p.turn(-Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("With double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			p.turn(-Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("With double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			p.turn(-Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("With double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			p.turn(-Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("With double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			p.turn(Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.392304, 6, p);
		}

		@DisplayName("With double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			p.turn(-Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.39230, -6, p);
		}

		@DisplayName("With double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			p.turn(Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-8.90916, 16.14394, p);
		}

		@DisplayName("With double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			p.turn(-Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(1.23321, 18.39780, p);
		}

		@DisplayName("With int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(-33, 58, p);
		}

		@DisplayName("With int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-34, 57, p);
		}

		@DisplayName("With int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(-33, 56, p);
		}

		@DisplayName("With int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-32, 57, p);
		}

		@DisplayName("With int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(-57, -34, p);
		}

		@DisplayName("With int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-58, -33, p);
		}

		@DisplayName("With int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(-57, -32, p);
		}

		@DisplayName("With int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-56, -33, p);
		}

		@DisplayName("With int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.36345, 30.1077, p);
		}

		@DisplayName("With int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(-1.63655, -26.89230, p);
		}

		@DisplayName("With int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-7.35118, 29.30799, p);
		}

		@DisplayName("With int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-3.97039, 6.20592, p);
		}

		@DisplayName("With int coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("With int coords #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("With int coords #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("With int coords #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("With int coords #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("With int coords #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("With int coords #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("With int coords #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("With int coords #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, 6, p);
		}

		@DisplayName("With int coords #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, -6, p);
		}

		@DisplayName("With int coords #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-9, 16, p);
		}

		@DisplayName("With int coords #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(1, 18, p);
		}

		@DisplayName("With int coords #25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_25(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(-33, 58, p);
		}

		@DisplayName("With int coords #26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_26(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-34, 57, p);
		}

		@DisplayName("With int coords #27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_27(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(-33, 56, p);
		}

		@DisplayName("With int coords #28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_28(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-32, 57, p);
		}

		@DisplayName("With int coords #29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_29(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(-57, -34, p);
		}

		@DisplayName("With int coords #30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_30(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-58, -33, p);
		}

		@DisplayName("With int coords #31")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_31(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(-57, -32, p);
		}

		@DisplayName("With int coords #32")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_32(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-56, -33, p);
		}

		@DisplayName("With int coords #33")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_33(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, 30, p);
		}

		@DisplayName("With int coords #34")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_34(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(-2, -27, p);
		}

		@DisplayName("With int coords #35")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_35(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-7, 29, p);
		}

		@DisplayName("With int coords #36")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void int_36(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turn(-Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-4, 6, p);
		}

	}

	@DisplayName("turnLeft(double)")
	@Nested
	public class TurnLeftDouble {
	
		@DisplayName("Left-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void lh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(1, 0);
			p.turnLeft(Math.PI/2);
			assertFpPointEquals(0, -1, p);
		}
	
		@DisplayName("Left-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void lh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, 1);
			p.turnLeft(Math.PI/2);
			assertFpPointEquals(1, 0, p);
		}
		
		@DisplayName("Left-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void lh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-1, 0);
			p.turnLeft(Math.PI/2);
			assertFpPointEquals(0, 1, p);
		}
		
		@DisplayName("Left-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void lh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, -1);
			p.turnLeft(Math.PI/2);
			assertFpPointEquals(-1, 0, p);
		}
		
		@DisplayName("Left-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void lh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(1, 0);
			p.turnLeft(-Math.PI/2);
			assertFpPointEquals(0, 1, p);
		}
		
		@DisplayName("Left-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void lh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, -1);
			p.turnLeft(-Math.PI/2);
			assertFpPointEquals(1, 0, p);
		}
		
		@DisplayName("Left-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void lh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-1, 0);
			p.turnLeft(-Math.PI/2);
			assertFpPointEquals(0, -1, p);
		}
		
		@DisplayName("Left-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void lh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, 1);
			p.turnLeft(-Math.PI/2);
			assertFpPointEquals(-1, 0, p);
		}
		
		@DisplayName("Left-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void lh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(12, 0);
			p.turnLeft(Math.PI/6);
			assertFpPointEquals(10.392304, -6, p);
		}
		
		@DisplayName("Left-handed with double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void lh_double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(12, 0);
			p.turnLeft(-Math.PI/6);
			assertFpPointEquals(10.39230, 6, p);
		}
		
		@DisplayName("Left-handed with double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void lh_double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-4, 18);
			p.turnLeft(Math.PI/11);
			assertFpPointEquals(1.23321, 18.39780, p);
		}
		
		@DisplayName("Left-handed with double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void lh_double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-4, 18);
			p.turnLeft(-Math.PI/11);
			assertFpPointEquals(-8.90916, 16.14394, p);
		}

		@DisplayName("Right-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(1, 0);
			p.turnLeft(Math.PI/2);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 1);
			p.turnLeft(Math.PI/2);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-1, 0);
			p.turnLeft(Math.PI/2);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, -1);
			p.turnLeft(Math.PI/2);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(1, 0);
			p.turnLeft(-Math.PI/2);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, -1);
			p.turnLeft(-Math.PI/2);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-1, 0);
			p.turnLeft(-Math.PI/2);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 1);
			p.turnLeft(-Math.PI/2);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(12, 0);
			p.turnLeft(Math.PI/6);
			assertFpPointEquals(10.392304, 6, p);
		}

		@DisplayName("Right-handed with double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(12, 0);
			p.turnLeft(-Math.PI/6);
			assertFpPointEquals(10.39230, -6, p);
		}

		@DisplayName("Right-handed with double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-4, 18);
			p.turnLeft(Math.PI/11);
			assertFpPointEquals(-8.90916, 16.14394, p);
		}

		@DisplayName("Right-handed with double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-4, 18);
			p.turnLeft(-Math.PI/11);
			assertFpPointEquals(1.23321, 18.39780, p);
		}

		@DisplayName("Left-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(1, 0);
			p.turnLeft(Math.PI/2);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, 1);
			p.turnLeft(Math.PI/2);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-1, 0);
			p.turnLeft(Math.PI/2);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, -1);
			p.turnLeft(Math.PI/2);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(1, 0);
			p.turnLeft(-Math.PI/2);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, -1);
			p.turnLeft(-Math.PI/2);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-1, 0);
			p.turnLeft(-Math.PI/2);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, 1);
			p.turnLeft(-Math.PI/2);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(12, 0);
			p.turnLeft(Math.PI/6);
			assertIntPointEquals(10, -6, p);
		}

		@DisplayName("Left-handed with int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(12, 0);
			p.turnLeft(-Math.PI/6);
			assertIntPointEquals(10, 6, p);
		}

		@DisplayName("Left-handed with int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-4, 18);
			p.turnLeft(Math.PI/11);
			assertIntPointEquals(1, 18, p);
		}

		@DisplayName("Left-handed with int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-4, 18);
			p.turnLeft(-Math.PI/11);
			assertIntPointEquals(-9, 16, p);
		}

		@DisplayName("Right-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(1, 0);
			p.turnLeft(Math.PI/2);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 1);
			p.turnLeft(Math.PI/2);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-1, 0);
			p.turnLeft(Math.PI/2);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, -1);
			p.turnLeft(Math.PI/2);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(1, 0);
			p.turnLeft(-Math.PI/2);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, -1);
			p.turnLeft(-Math.PI/2);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-1, 0);
			p.turnLeft(-Math.PI/2);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 1);
			p.turnLeft(-Math.PI/2);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(12, 0);
			p.turnLeft(Math.PI/6);
			assertIntPointEquals(10, 6, p);
		}

		@DisplayName("Right-handed with int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(12, 0);
			p.turnLeft(-Math.PI/6);
			assertIntPointEquals(10, -6, p);
		}

		@DisplayName("Right-handed with int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-4, 18);
			p.turnLeft(Math.PI/11);
			assertIntPointEquals(-9, 16, p);
		}

		@DisplayName("Right-handed with int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-4, 18);
			p.turnLeft(-Math.PI/11);
			assertIntPointEquals(1, 18, p);
		}

	}

	@DisplayName("turnLeft(double,Point2D)")
	@Nested
	public class TurnLeftDoublePoint2D {
	
		@DisplayName("Left-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(1, 0));
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, 1));
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(-1, 0));
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, -1));
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(1, 0));
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, -1));
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(-1, 0));
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, 1));
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/6, createPoint(12, 0));
			assertFpPointEquals(10.392304, -6, p);
		}

		@DisplayName("Left-handed with double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/6, createPoint(12, 0));
			assertFpPointEquals(10.39230, 6, p);
		}

		@DisplayName("Left-handed with double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/11, createPoint(-4, 18));
			assertFpPointEquals(1.23321, 18.39780, p);
		}

		@DisplayName("Left-handed with double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/11, createPoint(-4, 18));
			assertFpPointEquals(-8.90916, 16.14394, p);
		}
	
		@DisplayName("Right-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(1, 0));
			assertFpPointEquals(0, 1, p);
		}
		
		@DisplayName("Right-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, 1));
			assertFpPointEquals(-1, 0, p);
		}
		
		@DisplayName("Right-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(-1, 0));
			assertFpPointEquals(0, -1, p);
		}
		
		@DisplayName("Right-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, -1));
			assertFpPointEquals(1, 0, p);
		}
		
		@DisplayName("Right-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(1, 0));
			assertFpPointEquals(0, -1, p);
		}
		
		@DisplayName("Right-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, -1));
			assertFpPointEquals(-1, 0, p);
		}
		
		@DisplayName("Right-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(-1, 0));
			assertFpPointEquals(0, 1, p);
		}
		
		@DisplayName("Right-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, 1));
			assertFpPointEquals(1, 0, p);
		}
		
		@DisplayName("Right-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/6, createPoint(12, 0));
			assertFpPointEquals(10.392304, 6, p);
		}
		
		@DisplayName("Right-handed with double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/6, createPoint(12, 0));
			assertFpPointEquals(10.39230, -6, p);
		}
		
		@DisplayName("Right-handed with double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/11, createPoint(-4, 18));
			assertFpPointEquals(-8.90916, 16.14394, p);
		}
		
		@DisplayName("Right-handed with double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/11, createPoint(-4, 18));
			assertFpPointEquals(1.23321, 18.39780, p);
		}

		@DisplayName("Left-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(1, 0));
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, 1));
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(-1, 0));
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, -1));
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(1, 0));
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, -1));
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(-1, 0));
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, 1));
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/6, createPoint(12, 0));
			assertIntPointEquals(10, -6, p);
		}

		@DisplayName("Left-handed with int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/6, createPoint(12, 0));
			assertIntPointEquals(10, 6, p);
		}

		@DisplayName("Left-handed with int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/11, createPoint(-4, 18));
			assertIntPointEquals(1, 18, p);
		}

		@DisplayName("Left-handed with int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/11, createPoint(-4, 18));
			assertIntPointEquals(-9, 16, p);
		}

		@DisplayName("Right-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(1, 0));
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, 1));
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(-1, 0));
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, -1));
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(1, 0));
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, -1));
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(-1, 0));
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, 1));
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/6, createPoint(12, 0));
			assertIntPointEquals(10, 6, p);
		}

		@DisplayName("Right-handed with int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/6, createPoint(12, 0));
			assertIntPointEquals(10, -6, p);
		}

		@DisplayName("Right-handed with int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/11, createPoint(-4, 18));
			assertIntPointEquals(-9, 16, p);
		}

		@DisplayName("Right-handed with int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/11, createPoint(-4, 18));
			assertIntPointEquals(1, 18, p);
		}

	}

	@DisplayName("turnLeft(double,Point2D,Point2D)")
	@Nested
	public class TurnLeftDoublePoint2DPoint2D {
	
		private Point2D origin;
		private Point2D p;

		@BeforeEach
		public void setUp() {
			this.origin = createPoint(0, 0);
			this.p = createPoint(0, 0);
		}

		@DisplayName("Left-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.392304, -6, p);
		}

		@DisplayName("Left-handed with double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.39230, 6, p);
		}

		@DisplayName("Left-handed with double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(1.23321, 18.39780, p);
		}

		@DisplayName("Left-handed with double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-8.90916, 16.14394, p);
		}

		@DisplayName("Right-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.392304, 6, p);
		}

		@DisplayName("Right-handed with double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.39230, -6, p);
		}

		@DisplayName("Right-handed with double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-8.90916, 16.14394, p);
		}

		@DisplayName("Right-handed with double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(1.23321, 18.39780, p);
		}

		@DisplayName("Left-handed with double coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(-57, -34, p);
		}

		@DisplayName("Left-handed with double coords #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-56, -33, p);
		}

		@DisplayName("Left-handed with double coords #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(-57, -32, p);
		}

		@DisplayName("Left-handed with double coords #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-58, -33, p);
		}

		@DisplayName("Left-handed with double coords #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(-33, 58, p);
		}

		@DisplayName("Left-handed with double coords #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-32, 57, p);
		}

		@DisplayName("Left-handed with double coords #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(-33, 56, p);
		}

		@DisplayName("Left-handed with double coords #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-34, 57, p);
		}

		@DisplayName("Left-handed with double coords #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(-1.63655, -26.89230, p);
		}

		@DisplayName("Left-handed with double coords #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.36345, 30.1077, p);
		}

		@DisplayName("Left-handed with double coords #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-3.97039, 6.20592, p);
		}

		@DisplayName("Left-handed with double coords #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-7.35118, 29.30799, p);
		}

		@DisplayName("Right-handed with double coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(-33, 58, p);
		}

		@DisplayName("Right-handed with double coords #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-34, 57, p);
		}

		@DisplayName("Right-handed with double coords #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(-33, 56, p);
		}

		@DisplayName("Right-handed with double coords #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-32, 57, p);
		}

		@DisplayName("Right-handed with double coords #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(-57, -34, p);
		}

		@DisplayName("Right-handed with double coords #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-58, -33, p);
		}

		@DisplayName("Right-handed with double coords #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(-57, -32, p);
		}

		@DisplayName("Right-handed with double coords #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-56, -33, p);
		}

		@DisplayName("Right-handed with double coords #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.36345, 30.1077, p);
		}

		@DisplayName("Right-handed with double coords #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(-1.63655, -26.89230, p);
		}

		@DisplayName("Right-handed with double coords #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-7.35118, 29.30799, p);
		}

		@DisplayName("Right-handed with double coords #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-3.97039, 6.20592, p);
		}

		@DisplayName("Left-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, -6, p);
		}

		@DisplayName("Left-handed with int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, 6, p);
		}

		@DisplayName("Left-handed with int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(1, 18, p);
		}

		@DisplayName("Left-handed with int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-9, 16, p);
		}

		@DisplayName("Right-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, 6, p);
		}

		@DisplayName("Right-handed with int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, -6, p);
		}

		@DisplayName("Right-handed with int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-9, 16, p);
		}

		@DisplayName("Right-handed with int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(1, 18, p);
		}

		@DisplayName("Left-handed with int coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(-57, -34, p);
		}

		@DisplayName("Left-handed with int coords #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-56, -33, p);
		}

		@DisplayName("Left-handed with int coords #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(-57, -32, p);
		}

		@DisplayName("Left-handed with int coords #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-58, -33, p);
		}

		@DisplayName("Left-handed with int coords #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(-33, 58, p);
		}

		@DisplayName("Left-handed with int coords #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-32, 57, p);
		}

		@DisplayName("Left-handed with int coords #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(-33, 56, p);
		}

		@DisplayName("Left-handed with int coords #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-34, 57, p);
		}

		@DisplayName("Left-handed with int coords #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(-2, -27, p);
		}

		@DisplayName("Left-handed with int coords #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, 30, p);
		}

		@DisplayName("Left-handed with int coords #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-4, 6, p);
		}

		@DisplayName("Left-handed with int coords #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-7, 29, p);
		}

		@DisplayName("Right-handed with int coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);			
			p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(-33, 58, p);
		}

		@DisplayName("Right-handed with int coords #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-34, 57, p);
		}

		@DisplayName("Right-handed with int coords #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(-33, 56, p);
		}

		@DisplayName("Right-handed with int coords #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-32, 57, p);
		}

		@DisplayName("Right-handed with int coords #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(-57, -34, p);
		}

		@DisplayName("Right-handed with int coords #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-58, -33, p);
		}

		@DisplayName("Right-handed with int coords #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(-57, -32, p);
		}

		@DisplayName("Right-handed with int coords #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-56, -33, p);
		}

		@DisplayName("Right-handed with int coords #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, 30, p);
		}

		@DisplayName("Right-handed with int coords #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(-2, -27, p);
		}

		@DisplayName("Right-handed with int coords #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-7, 29, p);
		}

		@DisplayName("Right-handed with int coords #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-4, 6, p);
		}

	}

	@DisplayName("turnRight(double)")
	@Nested
	public class TurnRightDouble {
	
		@DisplayName("Left-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(1, 0);
			p.turnRight(Math.PI/2);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, 1);
			p.turnRight(Math.PI/2);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-1, 0);
			p.turnRight(Math.PI/2);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, -1);
			p.turnRight(Math.PI/2);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(1, 0);
			p.turnRight(-Math.PI/2);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, -1);
			p.turnRight(-Math.PI/2);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-1, 0);
			p.turnRight(-Math.PI/2);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, 1);
			p.turnRight(-Math.PI/2);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(12, 0);
			p.turnRight(Math.PI/6);
			assertFpPointEquals(10.39230, 6, p);
		}

		@DisplayName("Left-handed with double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(12, 0);
			p.turnRight(-Math.PI/6);
			assertFpPointEquals(10.392304, -6, p);
		}

		@DisplayName("Left-handed with double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-4, 18);
			p.turnRight(Math.PI/11);
			assertFpPointEquals(-8.90916, 16.14394, p);
		}

		@DisplayName("Left-handed with double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-4, 18);
			p.turnRight(-Math.PI/11);
			assertFpPointEquals(1.23321, 18.39780, p);
		}

		@DisplayName("Right-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(1, 0);
			p.turnRight(Math.PI/2);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 1);
			p.turnRight(Math.PI/2);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-1, 0);
			p.turnRight(Math.PI/2);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, -1);
			p.turnRight(Math.PI/2);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(1, 0);
			p.turnRight(-Math.PI/2);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, -1);
			p.turnRight(-Math.PI/2);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-1, 0);
			p.turnRight(-Math.PI/2);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 1);
			p.turnRight(-Math.PI/2);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(12, 0);
			p.turnRight(Math.PI/6);
			assertFpPointEquals(10.39230, -6, p);
		}

		@DisplayName("Right-handed with double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(12, 0);
			p.turnRight(-Math.PI/6);
			assertFpPointEquals(10.392304, 6, p);
		}

		@DisplayName("Right-handed with double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-4, 18);
			p.turnRight(Math.PI/11);
			assertFpPointEquals(1.23321, 18.39780, p);
		}

		@DisplayName("Right-handed with double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-4, 18);
			p.turnRight(-Math.PI/11);
			assertFpPointEquals(-8.90916, 16.14394, p);
		}

		@DisplayName("Left-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(1, 0);
			p.turnRight(Math.PI/2);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, 1);
			p.turnRight(Math.PI/2);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-1, 0);
			p.turnRight(Math.PI/2);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, -1);
			p.turnRight(Math.PI/2);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(1, 0);
			p.turnRight(-Math.PI/2);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, -1);
			p.turnRight(-Math.PI/2);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-1, 0);
			p.turnRight(-Math.PI/2);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(0, 1);
			p.turnRight(-Math.PI/2);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(12, 0);
			p.turnRight(Math.PI/6);
			assertIntPointEquals(10, 6, p);
		}

		@DisplayName("Left-handed with int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(12, 0);
			p.turnRight(-Math.PI/6);
			assertIntPointEquals(10, -6, p);
		}

		@DisplayName("Left-handed with int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-4, 18);
			p.turnRight(Math.PI/11);
			assertIntPointEquals(-9, 16, p);
		}

		@DisplayName("Left-handed with int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
			var p = createPoint(-4, 18);
			p.turnRight(-Math.PI/11);
			assertIntPointEquals(1, 18, p);
		}

		@DisplayName("Right-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(1, 0);
			p.turnRight(Math.PI/2);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 1);
			p.turnRight(Math.PI/2);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-1, 0);
			p.turnRight(Math.PI/2);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, -1);
			p.turnRight(Math.PI/2);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(1, 0);
			p.turnRight(-Math.PI/2);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, -1);
			p.turnRight(-Math.PI/2);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-1, 0);
			p.turnRight(-Math.PI/2);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 1);
			p.turnRight(-Math.PI/2);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(12, 0);
			p.turnRight(Math.PI/6);
			assertIntPointEquals(10, -6, p);
		}

		@DisplayName("Right-handed with int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(12, 0);
			p.turnRight(-Math.PI/6);
			assertIntPointEquals(10, 6, p);
		}

		@DisplayName("Right-handed with int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-4, 18);
			p.turnRight(Math.PI/11);
			assertIntPointEquals(1, 18, p);
		}

		@DisplayName("Right-handed with int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(-4, 18);
			p.turnRight(-Math.PI/11);
			assertIntPointEquals(-9, 16, p);
		}

	}

	@DisplayName("turnRight(double,Point2D)")
	@Nested
	public class TurnRightDoublePoint2D {

		@DisplayName("Left-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(1, 0));
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, 1));
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(-1, 0));
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, -1));
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(1, 0));
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, -1));
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(-1, 0));
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, 1));
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 0);
			p.turnRight(Math.PI/6, createPoint(12, 0));
			assertFpPointEquals(10.392304, 6, p);
		}

		@DisplayName("Left-handed with double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 0);
			p.turnRight(-Math.PI/6, createPoint(12, 0));
			assertFpPointEquals(10.39230, -6, p);
		}

		@DisplayName("Left-handed with double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 0);
			p.turnRight(Math.PI/11, createPoint(-4, 18));
			assertFpPointEquals(-8.90916, 16.14394, p);
		}

		@DisplayName("Left-handed with double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			var p = createPoint(0, 0);
			p.turnRight(-Math.PI/11, createPoint(-4, 18));
			assertFpPointEquals(1.23321, 18.39780, p);
		}

		@DisplayName("Right-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(1, 0));
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, 1));
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(-1, 0));
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, -1));
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(1, 0));
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, -1));
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(-1, 0));
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, 1));
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/6, createPoint(12, 0));
			assertFpPointEquals(10.392304, -6, p);
		}

		@DisplayName("Right-handed with double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/6, createPoint(12, 0));
			assertFpPointEquals(10.39230, 6, p);
		}

		@DisplayName("Right-handed with double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/11, createPoint(-4, 18));
			assertFpPointEquals(1.23321, 18.39780, p);
		}

		@DisplayName("Right-handed with double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/11, createPoint(-4, 18));
			assertFpPointEquals(-8.90916, 16.14394, p);
		}

		@DisplayName("Left-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(1, 0));
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, 1));
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(-1, 0));
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, -1));
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(1, 0));
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, -1));
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(-1, 0));
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, 1));
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/6, createPoint(12, 0));
			assertIntPointEquals(10, 6, p);
		}

		@DisplayName("Left-handed with int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/6, createPoint(12, 0));
			assertIntPointEquals(10, -6, p);
		}

		@DisplayName("Left-handed with int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/11, createPoint(-4, 18));
			assertIntPointEquals(-9, 16, p);
		}

		@DisplayName("Left-handed with int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/11, createPoint(-4, 18));
			assertIntPointEquals(1, 18, p);
		}

		@DisplayName("Right-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(1, 0));
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, 1));
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(-1, 0));
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, -1));
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(1, 0));
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, -1));
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(-1, 0));
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, 1));
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/6, createPoint(12, 0));
			assertIntPointEquals(10, -6, p);
		}

		@DisplayName("Right-handed with int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/6, createPoint(12, 0));
			assertIntPointEquals(10, 6, p);
		}

		@DisplayName("Right-handed with int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/11, createPoint(-4, 18));
			assertIntPointEquals(1, 18, p);
		}

		@DisplayName("Right-handed with int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/11, createPoint(-4, 18));
			assertIntPointEquals(-9, 16, p);
		}

	}

	@DisplayName("turnRight(double,Point2D,Point2D)")
	@Nested
	public class TurnRightDoublePoint2DPoint2D {

		@DisplayName("Left-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.39230, 6, p);
		}

		@DisplayName("Left-handed with double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.392304, -6, p);
		}

		@DisplayName("Left-handed with double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-8.90916, 16.14394, p);
		}

		@DisplayName("Left-handed with double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(1.23321, 18.39780, p);
		}

		@DisplayName("Right-handed with double coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with double coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with double coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with double coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with double coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with double coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with double coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with double coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with double coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.39230, -6, p);
		}

		@DisplayName("Right-handed with double coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.392304, 6, p);
		}

		@DisplayName("Right-handed with double coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(1.23321, 18.39780, p);
		}

		@DisplayName("Right-handed with double coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-8.90916, 16.14394, p);
		}

		@DisplayName("Left-handed with double coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(-33, 58, p);
		}

		@DisplayName("Left-handed with double coords #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-34, 57, p);
		}

		@DisplayName("Left-handed with double coords #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(-33, 56, p);
		}

		@DisplayName("Left-handed with double coords #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-32, 57, p);
		}

		@DisplayName("Left-handed with double coords #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(-57, -34, p);
		}

		@DisplayName("Left-handed with double coords #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-58, -33, p);
		}

		@DisplayName("Left-handed with double coords #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(-57, -32, p);
		}

		@DisplayName("Left-handed with double coords #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-56, -33, p);
		}

		@DisplayName("Left-handed with double coords #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.36345, 30.1077, p);
		}

		@DisplayName("Left-handed with double coords #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(-1.63655, -26.89230, p);
		}

		@DisplayName("Left-handed with double coords #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-7.35118, 29.30799, p);
		}

		@DisplayName("Left-handed with double coords #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_double_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-3.97039, 6.20592, p);
		}

		@DisplayName("Right-handed with double coords #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(-57, -34, p);
		}

		@DisplayName("Right-handed with double coords #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-56, -33, p);
		}

		@DisplayName("Right-handed with double coords #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(-57, -32, p);
		}

		@DisplayName("Right-handed with double coords #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-58, -33, p);
		}

		@DisplayName("Right-handed with double coords #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
			assertFpPointEquals(-33, 58, p);
		}

		@DisplayName("Right-handed with double coords #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
			assertFpPointEquals(-32, 57, p);
		}

		@DisplayName("Right-handed with double coords #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
			assertFpPointEquals(-33, 56, p);
		}

		@DisplayName("Right-handed with double coords #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
			assertFpPointEquals(-34, 57, p);
		}

		@DisplayName("Right-handed with double coords #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(-1.63655, -26.89230, p);
		}

		@DisplayName("Right-handed with double coords #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
			assertFpPointEquals(10.36345, 30.1077, p);
		}

		@DisplayName("Right-handed with double coords #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-3.97039, 6.20592, p);
		}

		@DisplayName("Right-handed with double coords #25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_double_25(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
			assertFpPointEquals(-7.35118, 29.30799, p);
		}

		@DisplayName("Left-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Left-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Left-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Left-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Left-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, 6, p);
		}

		@DisplayName("Left-handed with int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, -6, p);
		}

		@DisplayName("Left-handed with int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-9, 16, p);
		}

		@DisplayName("Left-handed with int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(1, 18, p);
		}

		@DisplayName("Right-handed with int coords #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with int coords #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with int coords #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with int coords #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with int coords #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(0, 1, p);
		}

		@DisplayName("Right-handed with int coords #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("Right-handed with int coords #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(0, -1, p);
		}

		@DisplayName("Right-handed with int coords #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-1, 0, p);
		}

		@DisplayName("Right-handed with int coords #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, -6, p);
		}

		@DisplayName("Right-handed with int coords #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, 6, p);
		}

		@DisplayName("Right-handed with int coords #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(1, 18, p);
		}

		@DisplayName("Right-handed with int coords #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(0, 0);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-9, 16, p);
		}

		@DisplayName("Left-handed with int coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(-33, 58, p);
		}

		@DisplayName("Left-handed with int coords #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-34, 57, p);
		}

		@DisplayName("Left-handed with int coords #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(-33, 56, p);
		}

		@DisplayName("Left-handed with int coords #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-32, 57, p);
		}

		@DisplayName("Left-handed with int coords #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(-57, -34, p);
		}

		@DisplayName("Left-handed with int coords #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-58, -33, p);
		}

		@DisplayName("Left-handed with int coords #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(-57, -32, p);
		}

		@DisplayName("Left-handed with int coords #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-56, -33, p);
		}

		@DisplayName("Left-handed with int coords #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, 30, p);
		}

		@DisplayName("Left-handed with int coords #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(-2, -27, p);
		}

		@DisplayName("Left-handed with int coords #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-7, 29, p);
		}

		@DisplayName("Left-handed with int coords #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lh_int_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-4, 6, p);
		}

		@DisplayName("Right-handed with int coords #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(-57, -34, p);
		}

		@DisplayName("Right-handed with int coords #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-56, -33, p);
		}

		@DisplayName("Right-handed with int coords #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(-57, -32, p);
		}

		@DisplayName("Right-handed with int coords #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-58, -33, p);
		}

		@DisplayName("Right-handed with int coords #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
			assertIntPointEquals(-33, 58, p);
		}

		@DisplayName("Right-handed with int coords #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
			assertIntPointEquals(-32, 57, p);
		}

		@DisplayName("Right-handed with int coords #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
			assertIntPointEquals(-33, 56, p);
		}

		@DisplayName("Right-handed with int coords #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
			assertIntPointEquals(-34, 57, p);
		}

		@DisplayName("Right-handed with int coords #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(-2, -27, p);
		}

		@DisplayName("Right-handed with int coords #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
			assertIntPointEquals(10, 30, p);
		}

		@DisplayName("Right-handed with int coords #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-4, 6, p);
		}

		@DisplayName("Right-handed with int coords #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rh_int_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());		
			assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
			Point2D origin = createPoint(-45, 12);
			Point2D p = createPoint(0, 0);
			p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
			assertIntPointEquals(-7, 29, p);
		}

	}

}
