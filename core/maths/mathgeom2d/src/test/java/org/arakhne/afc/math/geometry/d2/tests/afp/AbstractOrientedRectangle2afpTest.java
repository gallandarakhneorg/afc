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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractOrientedRectangle2afpTest<T extends OrientedRectangle2afp<?, T, ?, ?, ?, B>,
		B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractShape2afpTest<T, B> {

	protected final double cx = 6;
	protected final double cy = 9;
	protected final double ux = 0.894427190999916;
	protected final double uy = -0.447213595499958;
	protected final double e1 = 13.999990000000002;
	protected final double vx = 0.447213595499958;
	protected final double vy = 0.894427190999916;
	protected final double e2 = 12.999989999999997;
	
	// Points' names are in the ggb diagram
	protected final double pEx = -12.33574;
	protected final double pEy = 3.63344;
	protected final double pFx = 12.7082;
	protected final double pFy = -8.88853;
	protected final double pGx = 24.33574;
	protected final double pGy = 14.36656;
	protected final double pHx = -0.7082;
	protected final double pHy = 26.88853;

	@Override
	protected final T createShape() {
		return (T) createOrientedRectangle(cx, cy, ux, uy, e1, e2);
	}

    protected Triangle2afp createTestTriangle(double dx, double dy) {
        return createTriangle(dx, dy, dx + 6, dy + 3, dx - 1, dy + 2.5);
    }

    protected MultiShape2afp createTestMultiShape(double dx, double dy) {
        Circle2afp circle = createCircle(dx - 3, dy + 2, 2);
        Triangle2afp triangle = createTestTriangle(dx +1, dy - 1);
        MultiShape2afp multishape = createMultiShape();
        multishape.add(circle);
        multishape.add(triangle);
        return multishape;
    }

    protected Path2afp<?, ?, ?, ?, ?, ?> createNonEmptyPath(double x, double y) {
        Path2afp<?, ?, ?, ?, ?, ?> path = createPath();
        path.moveTo(x, y);
        path.lineTo(x + 1, y + .5);
        path.lineTo(x, y + 1);
        return path;
    }

    protected Parallelogram2afp createTestParallelogram(double dx, double dy) {
        Vector2D r = createVector(4, 1).toUnitVector();
        Vector2D s = createVector(-1, -1).toUnitVector();
        return createParallelogram(dx, dy, r.getX(), r.getY(), 2, s.getX(), s.getY(), 1);
    }

    protected OrientedRectangle2afp createTestOrientedRectangle(double dx, double dy) {
        Vector2D r = createVector(4, 1).toUnitVector();
        return createOrientedRectangle(dx, dy, r.getX(), r.getY(), 2, 1);
    }

    @DisplayName("findsVectorProjectionRAxisVector")
	@Nested
	public class FindsVectorProjectionRAxisVector {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-e1, OrientedRectangle2afp.findsVectorProjectionRAxisVector(ux, uy, pEx - cx, pEy - cy));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(e1, OrientedRectangle2afp.findsVectorProjectionRAxisVector(ux, uy, pFx - cx, pFy - cy));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(e1, OrientedRectangle2afp.findsVectorProjectionRAxisVector(ux, uy, pGx - cx, pGy - cy));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-e1, OrientedRectangle2afp.findsVectorProjectionRAxisVector(ux, uy, pHx - cx, pHy - cy));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-1.34164, OrientedRectangle2afp.findsVectorProjectionRAxisVector(ux, uy, -cx, -cy));
		}

	}

	@DisplayName("findsVectorProjectionSAxisVector")
	@Nested
	public class FindsVectorProjectionSAxisVector {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-e2, OrientedRectangle2afp.findsVectorProjectionSAxisVector(ux, uy, pEx - cx, pEy - cy));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-e2, OrientedRectangle2afp.findsVectorProjectionSAxisVector(ux, uy, pFx - cx, pFy - cy));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(e2, OrientedRectangle2afp.findsVectorProjectionSAxisVector(ux, uy, pGx - cx, pGy - cy));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(e2, OrientedRectangle2afp.findsVectorProjectionSAxisVector(ux, uy, pHx - cx, pHy - cy));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-10.73313, OrientedRectangle2afp.findsVectorProjectionSAxisVector(ux, uy, -cx, -cy));
		}

	}

	@DisplayName("calculatesCenterPointAxisExtents")
	@Nested
	public class CalculatesCenterPointAxisExtents {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			List points = Arrays.asList(
					createPoint(pEx, pEy), createPoint(pGx, pGy),
					createPoint(pFx, pFy), createPoint(pEx, pEy));
			Vector2D R;
			Point2D center;
			Tuple2D extents;
			R = createVector(ux, uy);
			center = createPoint(Double.NaN, Double.NaN);
			extents = createVector(Double.NaN, Double.NaN);
			OrientedRectangle2afp.calculatesCenterPointAxisExtents(points, R, center, extents);
			assertEpsilonEquals(cx, center.getX());
			assertEpsilonEquals(cy, center.getY());
			assertEpsilonEquals(e1, extents.getX());
			assertEpsilonEquals(e2, extents.getY());
		}

	}

	@DisplayName("containsOrientedRectanglePoint")
	@Nested
	public class ContainsOrientedRectanglePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
					0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
					-20, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
					12, -4));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
					14, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
					17, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
					18, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
					21, 8));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
					22, 8));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
					8, 16));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
					-4, 20));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
					-4, 21));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1,e2,
					cx, cy));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
					pEx, pEy));
		}

	}

	@DisplayName("containsOrientedRectangleRectangle")
	@Nested
	public class ContainsOrientedRectangleRectangle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
					0, 0, 2, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
					0, -1, 2, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
					0, -2, 2, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
					0, -3, 2, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
					0, -4, 2, 1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
					0, -5, 2, 1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
					0, -5, 2, 1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1,e2,
					5, 25, 2, 1));
		}

	}

	@DisplayName("findsClosestFarthestPointsPointOrientedRectangle")
	@Nested
	public class FindsClosestFarthestPointsPointOrientedRectangle {

		private Point2D closest;
		private Point2D farthest;

		@BeforeEach
		public void setUp() {
			closest = createPoint(Double.NaN, Double.NaN);
			farthest = createPoint(Double.NaN, Double.NaN);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
					-20, 9,
					cx, cy, ux, uy, e1, e2,
					closest, farthest);
			assertEpsilonEquals(-11.72197, closest.getX());
			assertEpsilonEquals(4.86099, closest.getY());
			assertEpsilonEquals(pGx, farthest.getX());
			assertEpsilonEquals(pGy, farthest.getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
					0, 0,
					cx, cy, ux, uy, e1, e2,
					closest, farthest);
			assertEpsilonEquals(0, closest.getX());
			assertEpsilonEquals(0, closest.getY());
			assertEpsilonEquals(pGx, farthest.getX());
			assertEpsilonEquals(pGy, farthest.getY());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
					5, -10,
					cx, cy, ux, uy, e1, e2,
					closest, farthest);
			assertEpsilonEquals(6.98623, closest.getX());
			assertEpsilonEquals(-6.02754, closest.getY());
			assertEpsilonEquals(pHx, farthest.getX());
			assertEpsilonEquals(pHy, farthest.getY());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
					14, -20,
					cx, cy, ux, uy, e1, e2,
					closest, farthest);
			assertEpsilonEquals(pFx, closest.getX());
			assertEpsilonEquals(pFy, closest.getY());
			assertEpsilonEquals(pHx, farthest.getX());
			assertEpsilonEquals(pHy, farthest.getY());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
					-6, 15,
					cx, cy, ux, uy, e1, e2,
					closest, farthest);
			assertEpsilonEquals(-6, closest.getX());
			assertEpsilonEquals(15, closest.getY());
			assertEpsilonEquals(pFx, farthest.getX());
			assertEpsilonEquals(pFy, farthest.getY());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
					10, 0,
					cx, cy, ux, uy, e1, e2,
					closest, farthest);
			assertEpsilonEquals(10, closest.getX());
			assertEpsilonEquals(0, closest.getY());
			assertEpsilonEquals(pHx, farthest.getX());
			assertEpsilonEquals(pHy, farthest.getY());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
					16, -4,
					cx, cy, ux, uy, e1, e2,
					closest, farthest);
			assertEpsilonEquals(15.32197, closest.getX());
			assertEpsilonEquals(-3.66099, closest.getY());
			assertEpsilonEquals(pHx, farthest.getX());
			assertEpsilonEquals(pHy, farthest.getY());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
					-5, 25,
					cx, cy, ux, uy, e1, e2,
					closest, farthest);
			assertEpsilonEquals(-2.32197, closest.getX());
			assertEpsilonEquals(23.66099, closest.getY());
			assertEpsilonEquals(pFx, farthest.getX());
			assertEpsilonEquals(pFy, farthest.getY());
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
					0, 35,
					cx, cy, ux, uy, e1, e2,
					closest, farthest);
			assertEpsilonEquals(pHx, closest.getX());
			assertEpsilonEquals(pHy, closest.getY());
			assertEpsilonEquals(pFx, farthest.getX());
			assertEpsilonEquals(pFy, farthest.getY());
		}

	}

	@DisplayName("intersectsOrientedRectangleSegment")
	@Nested
	public class IntersectsOrientedRectangleSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					-5, -5, 0, -7));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					-20, 0, -25, 2));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					-10, 15, -11, 17));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					-1, 30, -2, 40));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					10, 30, 15, 40));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					30, 15, 40, 16));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					20, 0, 25, 2));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					12, -15, 12, -16));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					15, -15, 35, 25));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					35, 25, -10, 40));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					-5, -5, 5, 1));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					-10, 15, 0, 10));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					0, 20, 15, 25));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					15, 5, 30, 10));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					-5, -5, -10, 15));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					-10, 15, 15, 25));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					15, 25, 20, 0));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					20, 0, 0, -10));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					15, 25, 0, -10));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					-10, 15, 20, 0));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
					0, 5, 10, 16));
		}

	}

	@DisplayName("intersectsOrientedRectangleCircle")
	@Nested
	public class IntersectsOrientedRectangleCircle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
					0, -3.2, .5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
					0, -3.1, .5));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
					0, -3, .5));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
					6, 2, .5));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
					pEx, pEy, .5));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
					pFx, pFy, .5));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
					-9, 10, .5));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
					2, 10, 50));
		}

	}

	@DisplayName("intersectsOrientedRectangleRectangle")
	@Nested
	public class IntersectsOrientedRectangleRectangle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
					0, -5, 2, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
					0, -4.5, 2, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
					0, -4, 2, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
					4, 4, 2, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
					20, -2, 2, 1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
					-15, -10, 50, 50));
		}

	}

	@DisplayName("intersectsOrientedRectangleEllipse")
	@Nested
	public class IntersectsOrientedRectangleEllipse {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(cx, cy, ux, uy, e1, e2,
					0, -5, 2, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(cx, cy, ux, uy, e1, e2,
					0, -4.5, 2, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(cx, cy, ux, uy, e1, e2,
					0, -4, 2, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(cx, cy, ux, uy, e1, e2,
					4, 4, 2, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(cx, cy, ux, uy, e1, e2,
					20, -2, 2, 1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(cx, cy, ux, uy, e1, e2,
					-15, -10, 50, 50));
		}

	}

	@DisplayName("intersectsOrientedRectangleTriangle")
	@Nested
	public class IntersectsOrientedRectangleTriangle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
					-10, 15, -8, 16, -13, 19));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
					-5, 30, -3, 31, -8, 34));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
					15, 25, 17, 26, 12, 29));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
					40, 15, 42, 16, 37, 19));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
					35, 0, 37, 1, 32, 4));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
					15, -20, 17, -19, 12, -16));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
					-5, -10, -3, -9, -8, -6));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
					-25, -5, -23, -4, -28, -1));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
					-4, -2, -2, -1, -7, -2));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
					-2, 4, 0, 5, -5, 8));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
					20, 5, 22, 6, 17, 9));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
					20, 5, 22, 6, -10, 15));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
					50, 30, 0, -50, -30, 31));
		}

	}

	@DisplayName("intersectsOrientedRectangleOrientedRectangle")
	@Nested
	public class IntersectsOrientedRectangleOrientedRectangle {

		private final double ux2 = -0.9284766908852592;
		private final double uy2 = 0.3713906763541037;
		private final double et1 = 5;
		private final double et2 = 3;
		// D + (-0.9284766908852592,0.3713906763541037) * 5 + (0.3713906763541037,0.9284766908852592) * 3
		// D - (-0.9284766908852592,0.3713906763541037) * 5 + (0.3713906763541037,0.9284766908852592) * 3
		// D - (-0.9284766908852592,0.3713906763541037) * 5 - (0.3713906763541037,0.9284766908852592) * 3
		// D + (-0.9284766908852592,0.3713906763541037) * 5 - (0.3713906763541037,0.9284766908852592) * 3

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1,e2,
					-10, -2, ux2, uy2, et1, et2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1, e2,
					-15, 25, ux2, uy2, et1, et2));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1, e2,
					2, -8, ux2, uy2, et1, et2));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1, e2,
					2, -7, ux2, uy2, et1, et2));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1, e2,
					2, -6, ux2, uy2, et1, et2));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1, e2,
					pEx, pEy, ux2, uy2, et1, et2));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1, e2,
					6, 6, ux2, uy2, et1, et2));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1, e2,
					6, 6, ux2, uy2, 10 * et1, 10 * et2));
		}

	}

	@DisplayName("intersectsOrientedRectangleRoundRectangle")
	@Nested
	public class IntersectsOrientedRectangleRoundRectangle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
					0, 0, 2, 1, .1, .05));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
					-9, 15, 2, 1, .1, .05));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
					-8.7, 15, 2, 1, .1, .05));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
					-8.7, 15, 2, 1, .1, .05));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
					-8.65, 15, 2, 1, .1, .05));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
					-8.64, 15, 2, 1, .1, .05));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
					-8.63, 15, 2, 1, .1, .05));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
					-8.62, 15, 2, 1, .1, .05));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
					-8, 15, 2, 1, .1, .05));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
					10, 25, 2, 1, .1, .05));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
					20, -5, 2, 1, .1, .05));
		}

	}

	@DisplayName("contains(double,double)")
	@Nested
	public class containsDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-20, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(12, -4));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(14, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(17, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(18, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(21, 8));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(22, 8));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(8, 16));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(-4, 20));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-4, 21));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(cx, cy));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(pEx, pEy));
		}

	}

	@DisplayName("contains")
	@Nested
	public class contains {

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(-20, 0)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(12, -4)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(14, 0)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(17, 0)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(18, 0)));
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(21, 8)));
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(22, 8)));
		}

		@DisplayName("(Point2D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(8, 16)));
		}

		@DisplayName("(Point2D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(-4, 20)));
		}

		@DisplayName("(Point2D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(-4, 21)));
		}

		@DisplayName("(Point2D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(cx, cy)));
		}

		@DisplayName("(Point2D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(pEx, pEy)));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createRectangle(0, 0, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createRectangle(0, -1, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createRectangle(0, -2, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(0, -3, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(0, -4, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(0, -5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(0, -5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(5, 25, 2, 1)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createCircle(0, 0, 2)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0, -1, 2)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0, -2, 2)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0, -3, 2)));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		}

		@DisplayName("(Circle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0, -4, 2)));
		}

		@DisplayName("(Circle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		}

		@DisplayName("(Circle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0, -5, 2)));
		}

		@DisplayName("(Circle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0, -5, 2)));
		}

		@DisplayName("(Circle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(5, 25, 2)));
		}

	}

	@DisplayName("clone")
	@Nested
	public class CloneTest {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			T clone = getS().clone();
			assertNotNull(clone);
			assertNotSame(shape, clone);
			assertEquals(getS().getClass(), clone.getClass());
			assertEpsilonEquals(cx, clone.getCenterX());
			assertEpsilonEquals(cy, clone.getCenterY());
			assertEpsilonEquals(ux, clone.getFirstAxisX());
			assertEpsilonEquals(uy, clone.getFirstAxisY());
			assertEpsilonEquals(e1, clone.getFirstAxisExtent());
			assertEpsilonEquals(vx, clone.getSecondAxisX());
			assertEpsilonEquals(vy, clone.getSecondAxisY());
			assertEpsilonEquals(e2, clone.getSecondAxisExtent());
		}

	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsObject {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(new Object()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createOrientedRectangle(0, cy, ux, uy, e1, e2)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createOrientedRectangle(cx, cy, ux, uy, e1, 20)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSegment(5, 8, 6, 10)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(shape));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(createOrientedRectangle(cx, cy, ux, uy, e1, e2)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals((PathIterator2afp) null));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createOrientedRectangle(0, cy, ux, uy, e1, e2).getPathIterator()));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createOrientedRectangle(cx, cy, ux, uy, e1, 20).getPathIterator()));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSegment(5, 8, 6, 10).getPathIterator()));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(getS().getPathIterator()));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(createOrientedRectangle(cx, cy, ux, uy, e1, e2).getPathIterator()));
		}

	}

	@DisplayName("equalsToPathIterator")
	@Nested
	public class EqualsToPathIterator {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToPathIterator(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToPathIterator(createOrientedRectangle(0, cy, ux, uy, e1, e2).getPathIterator()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToPathIterator(createOrientedRectangle(cx, cy, ux, uy, e1, 20).getPathIterator()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToPathIterator(createSegment(5, 8, 6, 10).getPathIterator()));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToPathIterator(getS().getPathIterator()));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToPathIterator(createOrientedRectangle(cx, cy, ux, uy, e1, e2).getPathIterator()));
		}

	}

	@DisplayName("equalsToShape")
	@Nested
	public class EqualsToShape {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) createOrientedRectangle(0, cy, ux, uy, e1, e2)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) createOrientedRectangle(cx, cy, ux, uy, e1, 20)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape(shape));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape((T) createOrientedRectangle(cx, cy, ux, uy, e1, e2)));
		}

	}

	@DisplayName("isEmpty")
	@Nested
	public class IsEmpty {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().isEmpty());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertTrue(getS().isEmpty());
		}

	}

	@DisplayName("clear")
	@Nested
	public class Clear {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertEpsilonEquals(0, getS().getCenterX());
			assertEpsilonEquals(0, getS().getCenterY());
			assertEpsilonEquals(1, getS().getFirstAxisX());
			assertEpsilonEquals(0, getS().getFirstAxisY());
			assertEpsilonEquals(0, getS().getFirstAxisExtent());
			assertEpsilonEquals(0, getS().getSecondAxisX());
			assertEpsilonEquals(1, getS().getSecondAxisY());
			assertEpsilonEquals(0, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("getClosestPointTo")
	@Nested
	public class GetClosestPointTo {

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var closest = getS().getClosestPointTo(createPoint(-20, 9));
			assertEpsilonEquals(-11.72197, closest.getX());
			assertEpsilonEquals(4.86099, closest.getY());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var closest = getS().getClosestPointTo(createPoint(0, 0));
			assertEpsilonEquals(0, closest.getX());
			assertEpsilonEquals(0, closest.getY());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var closest = getS().getClosestPointTo(createPoint(5, -10));
			assertEpsilonEquals(6.98623, closest.getX());
			assertEpsilonEquals(-6.02754, closest.getY());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var closest = getS().getClosestPointTo(createPoint(14, -20));
			assertEpsilonEquals(pFx, closest.getX());
			assertEpsilonEquals(pFy, closest.getY());
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var closest = getS().getClosestPointTo(createPoint(-6, 15));
			assertEpsilonEquals(-6, closest.getX());
			assertEpsilonEquals(15, closest.getY());
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var closest = getS().getClosestPointTo(createPoint(0, 35));
			assertEpsilonEquals(pHx, closest.getX());
			assertEpsilonEquals(pHy, closest.getY());
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var closest = getS().getClosestPointTo(createPoint(10, 0));
			assertEpsilonEquals(10, closest.getX());
			assertEpsilonEquals(0, closest.getY());
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var closest = getS().getClosestPointTo(createPoint(16, -4));
			assertEpsilonEquals(15.32197, closest.getX());
			assertEpsilonEquals(-3.66099, closest.getY());
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var closest = getS().getClosestPointTo(createPoint(-5, 25));
			assertEpsilonEquals(-2.32197, closest.getX());
			assertEpsilonEquals(23.66099, closest.getY());
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-3.01377, -1.02754, getS().getClosestPointTo(createCircle(-5, -5, 1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-12.33574, 3.63344, getS().getClosestPointTo(createCircle(-30, 8, 1)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createCircle(20, 5, 1));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createCircle(12, 10, 1));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-1.81377, -1.62754, getS().getClosestPointTo(createSegment(-5, -5, -3, -4)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-12.33574, 3.63344, getS().getClosestPointTo(createSegment(-30, 8, -28, 9)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createSegment(19, 5, 21, 6));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createSegment(12, 10, 14, 11));
		}

		@DisplayName("(Triangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-0.21377, -2.42754, getS().getClosestPointTo(createTestTriangle(-7, -7)));
		}

		@DisplayName("(Triangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-12.33574, 3.63344, getS().getClosestPointTo(createTestTriangle(-60, 0)));
		}

		@DisplayName("(Triangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createTestTriangle(19, 5));
		}

		@DisplayName("(Triangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createTestTriangle(5, 5));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-1.81377, -1.62754, getS().getClosestPointTo(createRectangle(-5, -5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-12.33574, 3.63344, getS().getClosestPointTo(createRectangle(-30, 5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createRectangle(17, 2, 2, 1));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createRectangle(5, 5, 2, 1));
		}

		@DisplayName("(Ellipse2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-1.98951, -1.53968, getS().getClosestPointTo(createEllipse(-5, -5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-12.33574, 3.63344, getS().getClosestPointTo(createEllipse(-30, 5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createEllipse(17, 2, 2, 1));
		}

		@DisplayName("(Ellipse2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createEllipse(5, 5, 2, 1));
		}

		@DisplayName("(RoundRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-1.84892, -1.60997, getS().getClosestPointTo(createRoundRectangle(-5, -5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-12.33574, 3.63344, getS().getClosestPointTo(createRoundRectangle(-30, 5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createRoundRectangle(17, 2, 2, 1, .2, .1));
		}

		@DisplayName("(RoundRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createRoundRectangle(5, 5, 2, 1, .2, .1));
		}

		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(0.98623, -3.02754, getS().getClosestPointTo(createTestMultiShape(-7, -7)));
		}

		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-12.33574, 3.63344, getS().getClosestPointTo(createTestMultiShape(-30, 0)));
		}

		@DisplayName("(MultiShape2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createTestMultiShape(15, 2));
		}

		@DisplayName("(MultiShape2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createTestMultiShape(5, 5));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-2.41377, -1.32754, getS().getClosestPointTo(createNonEmptyPath(-5, -5)));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-12.33574, 3.63344, getS().getClosestPointTo(createNonEmptyPath(-30, 5)));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createNonEmptyPath(15, 2));
		}

		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createNonEmptyPath(5, 5));
		}

		@DisplayName("(Parallelogram2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-1.37273, -1.84807, getS().getClosestPointTo(createTestParallelogram(-5, -5)));
		}

		@DisplayName("(Parallelogram2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-12.33574, 3.63344, getS().getClosestPointTo(createTestParallelogram(-30, 5)));
		}

		@DisplayName("(Parallelogram2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createTestParallelogram(18, 2));
		}

		@DisplayName("(Parallelogram2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createTestParallelogram(5, 5));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-2.23766, -1.4156, getS().getClosestPointTo(createTestOrientedRectangle(-5, -5)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(-12.33574, 3.63344, getS().getClosestPointTo(createTestOrientedRectangle(-30, 5)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createTestOrientedRectangle(18, 2));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(shape, createTestOrientedRectangle(5, 5));
	    }

	}

	@DisplayName("getFarthestPointTo")
	@Nested
	public class GetFarthestPointTo {

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var farthest = getS().getFarthestPointTo(createPoint(-20, 9));
			assertEpsilonEquals(pGx, farthest.getX());
			assertEpsilonEquals(pGy, farthest.getY());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var farthest = getS().getFarthestPointTo(createPoint(0, 0));
			assertEpsilonEquals(pGx, farthest.getX());
			assertEpsilonEquals(pGy, farthest.getY());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var farthest = getS().getFarthestPointTo(createPoint(5, -10));
			assertEpsilonEquals(pHx, farthest.getX());
			assertEpsilonEquals(pHy, farthest.getY());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var farthest = getS().getFarthestPointTo(createPoint(14, -20));
			assertEpsilonEquals(pHx, farthest.getX());
			assertEpsilonEquals(pHy, farthest.getY());
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var farthest = getS().getFarthestPointTo(createPoint(-6, 15));
			assertEpsilonEquals(pFx, farthest.getX());
			assertEpsilonEquals(pFy, farthest.getY());
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var farthest = getS().getFarthestPointTo(createPoint(0, 35));
			assertEpsilonEquals(pFx, farthest.getX());
			assertEpsilonEquals(pFy, farthest.getY());
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var farthest = getS().getFarthestPointTo(createPoint(10, 0));
			assertEpsilonEquals(pHx, farthest.getX());
			assertEpsilonEquals(pHy, farthest.getY());
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var farthest = getS().getFarthestPointTo(createPoint(16, -4));
			assertEpsilonEquals(pHx, farthest.getX());
			assertEpsilonEquals(pHy, farthest.getY());
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var farthest = getS().getFarthestPointTo(createPoint(-5, 25));
			assertEpsilonEquals(pFx, farthest.getX());
			assertEpsilonEquals(pFy, farthest.getY());
		}

	}

	@DisplayName("translate(double,double)")
	@Nested
	public class TranslateDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().translate(123.456, 789.123);
			assertEpsilonEquals(cx + 123.456, getS().getCenterX());
			assertEpsilonEquals(cy + 789.123, getS().getCenterY());
			assertEpsilonEquals(ux, getS().getFirstAxisX());
			assertEpsilonEquals(uy, getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(vx, getS().getSecondAxisX());
			assertEpsilonEquals(vy, getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("translate(Vector2D)")
	@Nested
	public class TranslateVector2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().translate(createVector(123.456, 789.123));
			assertEpsilonEquals(cx + 123.456, getS().getCenterX());
			assertEpsilonEquals(cy + 789.123, getS().getCenterY());
			assertEpsilonEquals(ux, getS().getFirstAxisX());
			assertEpsilonEquals(uy, getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(vx, getS().getSecondAxisX());
			assertEpsilonEquals(vy, getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("getDistance")
	@Nested
	public class GetDistance {

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9.2551, getS().getDistance(createPoint(-20, 9)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.44135, getS().getDistance(createPoint(5, -10)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.18631, getS().getDistance(createPoint(14, -20)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createPoint(-6, 15)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.14233, getS().getDistance(createPoint(0, 35)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createPoint(10, 0)));
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.75805, getS().getDistance(createPoint(16, -4)));
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.99413, getS().getDistance(createPoint(-5, 25)));
		}

	}

	@DisplayName("getDistanceSquared")
	@Nested
	public class GetDistanceSquared {

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(85.65719, getS().getDistanceSquared(createPoint(-20, 9)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(19.72555, getS().getDistanceSquared(createPoint(5, -10)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(125.13351, getS().getDistanceSquared(createPoint(14, -20)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(-6, 15)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(66.29749, getS().getDistanceSquared(createPoint(0, 35)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(10, 0)));
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.57465, getS().getDistanceSquared(createPoint(16, -4)));
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.96479, getS().getDistanceSquared(createPoint(-5, 25)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(11.84282, getS().getDistanceSquared(createCircle(-5, -5, 1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(295.70086, getS().getDistanceSquared(createCircle(-30, 8, 1)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(20, 5, 1)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(12, 10, 1)));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(7.03568, getS().getDistanceSquared(createSegment(-5, -5, -3, -4)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(274.16887, getS().getDistanceSquared(createSegment(-30, 8, -28, 9)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(19, 5, 21, 6)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(12, 10, 14, 11)));
		}

		@DisplayName("(Triangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(3.09077, getS().getDistanceSquared(createTestTriangle(-7, -7)));
		}

		@DisplayName("(Triangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(1736.31148, getS().getDistanceSquared(createTestTriangle(-60, 0)));
		}

		@DisplayName("(Triangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createTestTriangle(19, 5)));
		}

		@DisplayName("(Triangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createTestTriangle(5, 5)));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(7.03568, getS().getDistanceSquared(createRectangle(-5, -5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(247.2364, getS().getDistanceSquared(createRectangle(-30, 5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(17, 2, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(5, 5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8.49406, getS().getDistanceSquared(createEllipse(-5, -5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(248.79828, getS().getDistanceSquared(createEllipse(-30, 5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createEllipse(17, 2, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createEllipse(5, 5, 2, 1)));
		}

		@DisplayName("(RoundRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(7.31638, getS().getDistanceSquared(createRoundRectangle(-5, -5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(247.51287, getS().getDistanceSquared(createRoundRectangle(-30, 5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(17, 2, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(5, 5, 2, 1, .2, .1)));
		}

		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(4.86323, getS().getDistanceSquared(createTestMultiShape(-7, -7)));
		}

		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(116.39449, getS().getDistanceSquared(createTestMultiShape(-30, 0)));
		}

		@DisplayName("(MultiShape2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(15, 2)));
		}

		@DisplayName("(MultiShape2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(5, 5)));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(12.58059, getS().getDistanceSquared(createNonEmptyPath(-5, -5)));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(281.18147, getS().getDistanceSquared(createNonEmptyPath(-30, 5)));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createNonEmptyPath(15, 2)));
		}

		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createNonEmptyPath(5, 5)));
		}

		@DisplayName("(Parallelogram2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(4.80081, getS().getDistanceSquared(createTestParallelogram(-5, -5)));
		}

		@DisplayName("(Parallelogram2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(232.05334, getS().getDistanceSquared(createTestParallelogram(-30, 5)));
		}

		@DisplayName("(Parallelogram2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createTestParallelogram(18, 2)));
		}

		@DisplayName("(Parallelogram2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createTestParallelogram(5, 5)));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(5.66678, getS().getDistanceSquared(createTestOrientedRectangle(-5, -5)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(240.45186, getS().getDistanceSquared(createTestOrientedRectangle(-30, 5)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createTestOrientedRectangle(18, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createTestOrientedRectangle(5, 5)));
	    }

	}

	@DisplayName("getDistanceL1")
	@Nested
	public class GetDistanceL1 {

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(12.417, getS().getDistanceL1(createPoint(-20, 9)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.95869, getS().getDistanceL1(createPoint(5, -10)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(12.40327, getS().getDistanceL1(createPoint(14, -20)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(-6, 15)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.81967, getS().getDistanceL1(createPoint(0, 35)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(10, 0)));
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.01704, getS().getDistanceL1(createPoint(16, -4)));
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.01704, getS().getDistanceL1(createPoint(-5, 25)));
		}

	}

	@DisplayName("getDistanceLinf")
	@Nested
	public class GetDistanceLinf {

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.278, getS().getDistanceLinf(createPoint(-20, 9)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.97246, getS().getDistanceLinf(createPoint(5, -10)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.11147, getS().getDistanceLinf(createPoint(14, -20)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(-6, 15)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.11147, getS().getDistanceLinf(createPoint(0, 35)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(10, 0)));
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.67803, getS().getDistanceLinf(createPoint(16, -4)));
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.67803, getS().getDistanceLinf(createPoint(-5, 25)));
		}

	}

	@DisplayName("set(IT)")
	@Nested
	public class SetIT {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set((T) createOrientedRectangle(17, 20, 1, 0, 15, 14));
			assertEpsilonEquals(17, getS().getCenterX());
			assertEpsilonEquals(20, getS().getCenterY());
			assertEpsilonEquals(1, getS().getFirstAxisX());
			assertEpsilonEquals(0, getS().getFirstAxisY());
			assertEpsilonEquals(15, getS().getFirstAxisExtent());
			assertEpsilonEquals(0, getS().getSecondAxisX());
			assertEpsilonEquals(1, getS().getSecondAxisY());
			assertEpsilonEquals(14, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("getPathIterator")
	@Nested
	public class GetPathIterator {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
			assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
			assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
			assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
			assertElement(pi, PathElementType.CLOSE, pGx, pGy);
			assertNoElement(pi);
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathIterator2afp pi = getS().getPathIterator(null);
			assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
			assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
			assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
			assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
			assertElement(pi, PathElementType.CLOSE, pGx, pGy);
			assertNoElement(pi);
		}	

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var transform = new Transform2D();
			var pi = getS().getPathIterator(transform);
			assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
			assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
			assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
			assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
			assertElement(pi, PathElementType.CLOSE, pGx, pGy);
			assertNoElement(pi);
		}	

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var transform = new Transform2D();
			transform.setTranslation(18,  -45);
			var pi = getS().getPathIterator(transform);
			assertElement(pi, PathElementType.MOVE_TO, pGx + 18, pGy - 45);
			assertElement(pi, PathElementType.LINE_TO, pHx + 18, pHy - 45);
			assertElement(pi, PathElementType.LINE_TO, pEx + 18, pEy - 45);
			assertElement(pi, PathElementType.LINE_TO, pFx + 18, pFy - 45);
			assertElement(pi, PathElementType.CLOSE, pGx + 18, pGy - 45);
			assertNoElement(pi);
		}

	}

	@DisplayName("createTransformedShape")
	@Nested
	public class CreateTransformedShape {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathIterator2afp pi = getS().createTransformedShape(null).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
			assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
			assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
			assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
			assertElement(pi, PathElementType.CLOSE, pGx, pGy);
			assertNoElement(pi);
		}	

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var transform = new Transform2D();
			var pi = getS().createTransformedShape(transform).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
			assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
			assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
			assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
			assertElement(pi, PathElementType.CLOSE, pGx, pGy);
			assertNoElement(pi);
		}	

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var transform = new Transform2D();
			transform.setTranslation(18,  -45);
			var pi = getS().createTransformedShape(transform).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, pGx + 18, pGy - 45);
			assertElement(pi, PathElementType.LINE_TO, pHx + 18, pHy - 45);
			assertElement(pi, PathElementType.LINE_TO, pEx + 18, pEy - 45);
			assertElement(pi, PathElementType.LINE_TO, pFx + 18, pFy - 45);
			assertElement(pi, PathElementType.CLOSE, pGx + 18, pGy - 45);
			assertNoElement(pi);
		}

	}

	@DisplayName("toBoundingBox")
	@Nested
	public class ToBoundingBox {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertEpsilonEquals(pEx, box.getMinX());
			assertEpsilonEquals(pFy, box.getMinY());
			assertEpsilonEquals(pGx, box.getMaxX());
			assertEpsilonEquals(pHy, box.getMaxY());
		}

	}

	@DisplayName("toBoundingBox(B)")
	@Nested
	public class ToBoundingBoxB {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = createRectangle(0, 0, 0, 0);
			getS().toBoundingBox(box);
			assertEpsilonEquals(pEx, box.getMinX());
			assertEpsilonEquals(pFy, box.getMinY());
			assertEpsilonEquals(pGx, box.getMaxX());
			assertEpsilonEquals(pHy, box.getMaxY());
		}

	}

	@DisplayName("rotate(double)")
	@Nested
	public class RotateDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().rotate(-MathConstants.DEMI_PI);
			assertEpsilonEquals(6, getS().getCenterX());
			assertEpsilonEquals(9, getS().getCenterY());
			assertEpsilonEquals(-4.472135954999580e-01, getS().getFirstAxisX());
			assertEpsilonEquals(-8.944271909999160e-01, getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(8.944271909999160e-01, getS().getSecondAxisX());
			assertEpsilonEquals(-4.472135954999580e-01, getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("getCenter")
	@Nested
	public class GetCenter {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D c = getS().getCenter();
			assertEpsilonEquals(6, c.getX());
			assertEpsilonEquals(9, c.getY());
		}

	}

	@DisplayName("getCenterX")
	@Nested
	public class GetCenterX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6, getS().getCenterX());
		}

	}

	@DisplayName("getCenterY")
	@Nested
	public class GetCenterY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9, getS().getCenterY());
		}

	}

	@DisplayName("setCenter(double,double)")
	@Nested
	public class SetCenterDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setCenter(123.456, -789.123);
			assertEpsilonEquals(123.456, getS().getCenterX());
			assertEpsilonEquals(-789.123, getS().getCenterY());
			assertEpsilonEquals(ux, getS().getFirstAxisX());
			assertEpsilonEquals(uy, getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(vx, getS().getSecondAxisX());
			assertEpsilonEquals(vy, getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("setCenter(Point2D)")
	@Nested
	public class SetCenterPoint2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setCenter(createPoint(123.456, -789.123));
			assertEpsilonEquals(123.456, getS().getCenterX());
			assertEpsilonEquals(-789.123, getS().getCenterY());
			assertEpsilonEquals(ux, getS().getFirstAxisX());
			assertEpsilonEquals(uy, getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(vx, getS().getSecondAxisX());
			assertEpsilonEquals(vy, getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("setCenterX")
	@Nested
	public class SetCenterX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setCenterX(123.456);
			assertEpsilonEquals(123.456, getS().getCenterX());
			assertEpsilonEquals(cy, getS().getCenterY());
			assertEpsilonEquals(ux, getS().getFirstAxisX());
			assertEpsilonEquals(uy, getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(vx, getS().getSecondAxisX());
			assertEpsilonEquals(vy, getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("setCenterY")
	@Nested
	public class SetCenterY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setCenterY(123.456);
			assertEpsilonEquals(cx, getS().getCenterX());
			assertEpsilonEquals(123.456, getS().getCenterY());
			assertEpsilonEquals(ux, getS().getFirstAxisX());
			assertEpsilonEquals(uy, getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(vx, getS().getSecondAxisX());
			assertEpsilonEquals(vy, getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("getFirstAxis")
	@Nested
	public class GetFirstAxis {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D v = getS().getFirstAxis();
			assertEpsilonEquals(ux, v.getX());
			assertEpsilonEquals(uy, v.getY());
		}

	}

	@DisplayName("getFirstAxisX")
	@Nested
	public class GetFirstAxisX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(ux, getS().getFirstAxisX());
		}

	}

	@DisplayName("getFirstAxisY")
	@Nested
	public class GetFirstAxisY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(uy, getS().getFirstAxisY());
		}

	}

	@DisplayName("getSecondAxis")
	@Nested
	public class GetSecondAxis {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D v = getS().getSecondAxis();
			assertEpsilonEquals(vx, v.getX());
			assertEpsilonEquals(vy, v.getY());
		}

	}

	@DisplayName("getSecondAxisX")
	@Nested
	public class GetSecondAxisX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(vx, getS().getSecondAxisX());
		}

	}

	@DisplayName("getSecondAxisY")
	@Nested
	public class GetSecondAxisY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(vy, getS().getSecondAxisY());
		}

	}

	@DisplayName("getFirstAxisExtent")
	@Nested
	public class GetFirstAxisExtent {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
		}

	}

	@DisplayName("setFirstAxisExtent")
	@Nested
	public class SetFirstAxisExtent {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setFirstAxisExtent(123.456);
			assertEpsilonEquals(cx, getS().getCenterX());
			assertEpsilonEquals(cy, getS().getCenterY());
			assertEpsilonEquals(ux, getS().getFirstAxisX());
			assertEpsilonEquals(uy, getS().getFirstAxisY());
			assertEpsilonEquals(123.456, getS().getFirstAxisExtent());
			assertEpsilonEquals(vx, getS().getSecondAxisX());
			assertEpsilonEquals(vy, getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("getSecondAxisExtent")
	@Nested
	public class GetSecondAxisExtent {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("setSecondAxisExtent")
	@Nested
	public class SetSecondAxisExtent {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setSecondAxisExtent(123.456);
			assertEpsilonEquals(cx, getS().getCenterX());
			assertEpsilonEquals(cy, getS().getCenterY());
			assertEpsilonEquals(ux, getS().getFirstAxisX());
			assertEpsilonEquals(uy, getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(vx, getS().getSecondAxisX());
			assertEpsilonEquals(vy, getS().getSecondAxisY());
			assertEpsilonEquals(123.456, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("setFirstAxis(double,double)")
	@Nested
	public class SetFirstAxisDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D newU = createVector(123.456, 456.789).toUnitVector();
			getS().setFirstAxis(newU.getX(), newU.getY());
			assertEpsilonEquals(cx, getS().getCenterX());
			assertEpsilonEquals(cy, getS().getCenterY());
			assertEpsilonEquals(newU.getX(), getS().getFirstAxisX());
			assertEpsilonEquals(newU.getY(), getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(-newU.getY(), getS().getSecondAxisX());
			assertEpsilonEquals(newU.getX(), getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().setFirstAxis(123.456, 456.789));
		}

	}

	@DisplayName("setFirstAxis(Vector2D)")
	@Nested
	public class SetFirstAxisVector2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D newU = createVector(123.456, 456.789).toUnitVector();
			getS().setFirstAxis(newU);
			assertEpsilonEquals(cx, getS().getCenterX());
			assertEpsilonEquals(cy, getS().getCenterY());
			assertEpsilonEquals(newU.getX(), getS().getFirstAxisX());
			assertEpsilonEquals(newU.getY(), getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(-newU.getY(), getS().getSecondAxisX());
			assertEpsilonEquals(newU.getX(), getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().setFirstAxis(createVector(123.456, 456.789)));
		}

	}

	@DisplayName("setFirstAxis(Vector2D,double)")
	@Nested
	public class SetFirstAxisVector2DDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D newU = createVector(123.456, 456.789).toUnitVector();
			getS().setFirstAxis(newU, 159.753);
			assertEpsilonEquals(cx, getS().getCenterX());
			assertEpsilonEquals(cy, getS().getCenterY());
			assertEpsilonEquals(newU.getX(), getS().getFirstAxisX());
			assertEpsilonEquals(newU.getY(), getS().getFirstAxisY());
			assertEpsilonEquals(159.753, getS().getFirstAxisExtent());
			assertEpsilonEquals(-newU.getY(), getS().getSecondAxisX());
			assertEpsilonEquals(newU.getX(), getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("setFirstAxis(double,double,double)")
	@Nested
	public class SetFirstAxisDoubleDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D newU = createVector(123.456, 456.789).toUnitVector();
			getS().setFirstAxis(newU.getX(), newU.getY(), 159.753);
			assertEpsilonEquals(cx, getS().getCenterX());
			assertEpsilonEquals(cy, getS().getCenterY());
			assertEpsilonEquals(newU.getX(), getS().getFirstAxisX());
			assertEpsilonEquals(newU.getY(), getS().getFirstAxisY());
			assertEpsilonEquals(159.753, getS().getFirstAxisExtent());
			assertEpsilonEquals(-newU.getY(), getS().getSecondAxisX());
			assertEpsilonEquals(newU.getX(), getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("setSecondAxis(double,double)")
	@Nested
	public class SetSecondAxisDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D newV = createVector(123.456, 456.789).toUnitVector();
			getS().setSecondAxis(newV.getX(), newV.getY());
			assertEpsilonEquals(cx, getS().getCenterX());
			assertEpsilonEquals(cy, getS().getCenterY());
			assertEpsilonEquals(newV.getY(), getS().getFirstAxisX());
			assertEpsilonEquals(-newV.getX(), getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(newV.getX(), getS().getSecondAxisX());
			assertEpsilonEquals(newV.getY(), getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().setSecondAxis(123.456, 456.789));
		}

	}

	@DisplayName("setSecondAxis(Vector2D)")
	@Nested
	public class SetSecondAxisVector2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D newV = createVector(123.456, 456.789).toUnitVector();
			getS().setSecondAxis(newV);
			assertEpsilonEquals(cx, getS().getCenterX());
			assertEpsilonEquals(cy, getS().getCenterY());
			assertEpsilonEquals(newV.getY(), getS().getFirstAxisX());
			assertEpsilonEquals(-newV.getX(), getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(newV.getX(), getS().getSecondAxisX());
			assertEpsilonEquals(newV.getY(), getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().setSecondAxis(createVector(123.456, 456.789)));
		}

	}

	@DisplayName("setSecondAxis(Vector2D,double)")
	@Nested
	public class SetSecondAxisVector2DDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D newV = createVector(123.456, 456.789).toUnitVector();
			getS().setSecondAxis(newV, 159.753);
			assertEpsilonEquals(cx, getS().getCenterX());
			assertEpsilonEquals(cy, getS().getCenterY());
			assertEpsilonEquals(newV.getY(), getS().getFirstAxisX());
			assertEpsilonEquals(-newV.getX(), getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(newV.getX(), getS().getSecondAxisX());
			assertEpsilonEquals(newV.getY(), getS().getSecondAxisY());
			assertEpsilonEquals(159.753, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("setSecondAxis(double,double,double)")
	@Nested
	public class SetSecondAxisDoubleDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D newV = createVector(123.456, 456.789).toUnitVector();
			getS().setSecondAxis(newV.getX(), newV.getY(), 159.753);
			assertEpsilonEquals(cx, getS().getCenterX());
			assertEpsilonEquals(cy, getS().getCenterY());
			assertEpsilonEquals(newV.getY(), getS().getFirstAxisX());
			assertEpsilonEquals(-newV.getX(), getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(newV.getX(), getS().getSecondAxisX());
			assertEpsilonEquals(newV.getY(), getS().getSecondAxisY());
			assertEpsilonEquals(159.753, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("set(double,double,double,double,double,double)")
	@Nested
	public class SetDoubleDoubleDoubleDoubleDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D newU = createVector(-456.789, 159.753).toUnitVector();
			getS().set(-6, -4, newU.getX(), newU.getY(), 147.369, 159.753);
			assertEpsilonEquals(-6, getS().getCenterX());
			assertEpsilonEquals(-4, getS().getCenterY());
			assertEpsilonEquals(newU.getX(), getS().getFirstAxisX());
			assertEpsilonEquals(newU.getY(), getS().getFirstAxisY());
			assertEpsilonEquals(147.369, getS().getFirstAxisExtent());
			assertEpsilonEquals(-newU.getY(), getS().getSecondAxisX());
			assertEpsilonEquals(newU.getX(), getS().getSecondAxisY());
			assertEpsilonEquals(159.753, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("set(Point2D,Vector2D,double,double)")
	@Nested
	public class SetPoint2DVector2DDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D newU = createVector(-456.789, 159.753).toUnitVector();
			getS().set(createPoint(-6, -4), newU, 147.369, 159.753);
			assertEpsilonEquals(-6, getS().getCenterX());
			assertEpsilonEquals(-4, getS().getCenterY());
			assertEpsilonEquals(newU.getX(), getS().getFirstAxisX());
			assertEpsilonEquals(newU.getY(), getS().getFirstAxisY());
			assertEpsilonEquals(147.369, getS().getFirstAxisExtent());
			assertEpsilonEquals(-newU.getY(), getS().getSecondAxisX());
			assertEpsilonEquals(newU.getX(), getS().getSecondAxisY());
			assertEpsilonEquals(159.753, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("setFromPointCloud")
	@Nested
	public class SetFromPointCloud {

		private final double obrux = 0.8944271909999159;
		private final double obruy = -0.4472135954999579;
		private final double obrvx = 0.4472135954999579;
		private final double obrvy = 0.8944271909999159;

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setFromPointCloud((List) Arrays.asList(
					createPoint(11.7082, -0.94427), createPoint(16.18034, 8),
					createPoint(-1.7082, 16.94427), createPoint(-6.18034, 8)));
			assertEpsilonEquals(5, getS().getCenterX());
			assertEpsilonEquals(8, getS().getCenterY());
			assertEpsilonEquals(obrux, getS().getFirstAxisX());
			assertEpsilonEquals(obruy, getS().getFirstAxisY());
			assertEpsilonEquals(10, getS().getFirstAxisExtent());
			assertEpsilonEquals(obrvx, getS().getSecondAxisX());
			assertEpsilonEquals(obrvy, getS().getSecondAxisY());
			assertEpsilonEquals(5, getS().getSecondAxisExtent());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setFromPointCloud(
					createPoint(11.7082, -0.94427), createPoint(16.18034, 8),
					createPoint(-1.7082, 16.94427), createPoint(-6.18034, 8));
			assertEpsilonEquals(5, getS().getCenterX());
			assertEpsilonEquals(8, getS().getCenterY());
			assertEpsilonEquals(obrux, getS().getFirstAxisX());
			assertEpsilonEquals(obruy, getS().getFirstAxisY());
			assertEpsilonEquals(10, getS().getFirstAxisExtent());
			assertEpsilonEquals(obrvx, getS().getSecondAxisX());
			assertEpsilonEquals(obrvy, getS().getSecondAxisY());
			assertEpsilonEquals(5, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("Orthogonal axes")
	@Nested
	public class OrthogonalAxes {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isOrthogonal(ux, uy, vx, vy));
			getS().setFirstAxis(0.500348, 0.865824);
			assertEpsilonEquals(-0.865824, getS().getSecondAxisX());
			assertEpsilonEquals(0.500348, getS().getSecondAxisY());
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Vector2D.isOrthogonal(ux, uy, vx, vy));
			getS().setSecondAxis(0.500348, 0.865824);
			assertEpsilonEquals(0.865824, getS().getFirstAxisX());
			assertEpsilonEquals(-0.500348, getS().getFirstAxisY());
		}

	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(-5, -5, 0, -7)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(-20, 0, -25, 2)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(-10, 15, -11, 17)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(-1, 30, -2, 40)));
		}

		@DisplayName("(Segment2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(10, 30, 15, 40)));
		}

		@DisplayName("(Segment2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(30, 15, 40, 16)));
		}

		@DisplayName("(Segment2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(20, 0, 25, 2)));
		}

		@DisplayName("(Segment2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(12, -15, 12, -16)));
		}

		@DisplayName("(Segment2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(15, -15, 35, 25)));
		}

		@DisplayName("(Segment2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(35, 25, -10, 40)));
		}

		@DisplayName("(Segment2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-5, -5, 5, 1)));
		}

		@DisplayName("(Segment2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-10, 15, 0, 10)));
		}

		@DisplayName("(Segment2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(0, 20, 15, 25)));
		}

		@DisplayName("(Segment2afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(15, 5, 30, 10)));
		}

		@DisplayName("(Segment2afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-5, -5, -10, 15)));
		}

		@DisplayName("(Segment2afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-10, 15, 15, 25)));
		}

		@DisplayName("(Segment2afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(15, 25, 20, 0)));
		}

		@DisplayName("(Segment2afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(20, 0, 0, -10)));
		}

		@DisplayName("(Segment2afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(15, 25, 0, -10)));
		}

		@DisplayName("(Segment2afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-10, 15, 20, 0)));
		}

		@DisplayName("(Segment2afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(0, 5, 10, 16)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(0, -3.2, .5)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(0, -3.1, .5)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(0, -3, .5)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(6, 2, .5)));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(pEx, pEy, .5)));
		}

		@DisplayName("(Circle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(pFx, pFy, .5)));
		}

		@DisplayName("(Circle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(-9, 10, .5)));
		}

		@DisplayName("(Circle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(2, 10, 50)));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(0, -5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(0, -4.5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		}

		@DisplayName("(Rectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(0, -4, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(4, 4, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(20, -2, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(-15, -10, 50, 50)));
		}

		@DisplayName("(Ellipse2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createEllipse(0, -5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createEllipse(0, -4.5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createEllipse(0, -4, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createEllipse(4, 4, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createEllipse(20, -2, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createEllipse(-15, -10, 50, 50)));
		}

		@DisplayName("(Triangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createTriangle(-10, 15, -8, 16, -13, 19)));
		}

		@DisplayName("(Triangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createTriangle(-5, 30, -3, 31, -8, 34)));
		}

		@DisplayName("(Triangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createTriangle(15, 25, 17, 26, 12, 29)));
		}

		@DisplayName("(Triangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createTriangle(40, 15, 42, 16, 37, 19)));
		}

		@DisplayName("(Triangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createTriangle(35, 0, 37, 1, 32, 4)));
		}

		@DisplayName("(Triangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createTriangle(15, -20, 17, -19, 12, -16)));
		}

		@DisplayName("(Triangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createTriangle(-5, -10, -3, -9, -8, -6)));
		}

		@DisplayName("(Triangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createTriangle(-25, -5, -23, -4, -28, -1)));
		}

		@DisplayName("(Triangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createTriangle(-4, -2, -2, -1, -7, -2)));
		}

		@DisplayName("(Triangle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createTriangle(-2, 4, 0, 5, -5, 8)));
		}

		@DisplayName("(Triangle2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createTriangle(20, 5, 22, 6, 17, 9)));
		}

		@DisplayName("(Triangle2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createTriangle(20, 5, 22, 6, -10, 15)));
		}

		@DisplayName("(Triangle2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createTriangle(50, 30, 0, -50, -30, 31)));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp<?, ?, ?, ?, ?, B> path = createPath();
			path.moveTo(-15,  2);
			path.lineTo(6, -9);
			path.lineTo(19, -9);
			path.lineTo(30, 26);
			path.lineTo(-6, 30);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp<?, ?, ?, ?, ?, B> path = createPath();
			path.moveTo(-15,  2);
			path.lineTo(6, -9);
			path.lineTo(19, -9);
			path.lineTo(30, 26);
			path.lineTo(-6, 30);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("(PathIterator2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp<?, ?, ?, ?, ?, B> path = createPath();
			path.moveTo(-15,  2);
			path.lineTo(6, -9);
			path.lineTo(19, -9);
			path.lineTo(30, 26);
			path.lineTo(-6, 30);
			assertFalse(getS().intersects(path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp<?, ?, ?, ?, ?, B> path = createPath();
			path.moveTo(-15,  2);
			path.lineTo(6, -9);
			path.lineTo(19, -9);
			path.lineTo(30, 26);
			path.lineTo(-6, 30);
			path.closePath();
			assertTrue(getS().intersects(path.getPathIterator()));
		}

		@DisplayName("(Parallelogram2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double ux2 = 0.55914166827779;
			double uy2 = 0.829072128825671;
			double et1 = 10;
			double vx2 = -0.989660599000356;
			double vy2 = -0.143429072318889;
			double et2 = 15;
			assertFalse(getS().intersects(createParallelogram(
					-20, -20, ux2, uy2, et1, vx2, vy2, et2)));
		}

		@DisplayName("(Parallelogram2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double ux2 = 0.55914166827779;
			double uy2 = 0.829072128825671;
			double et1 = 10;
			double vx2 = -0.989660599000356;
			double vy2 = -0.143429072318889;
			double et2 = 15;
			assertFalse(getS().intersects(createParallelogram(
					-40, 20, ux2, uy2, et1, vx2, vy2, et2)));
		}

		@DisplayName("(Parallelogram2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double ux2 = 0.55914166827779;
			double uy2 = 0.829072128825671;
			double et1 = 10;
			double vx2 = -0.989660599000356;
			double vy2 = -0.143429072318889;
			double et2 = 15;
			assertTrue(getS().intersects(createParallelogram(
					-20, -10, ux2, uy2, et1, vx2, vy2, et2)));
		}

		@DisplayName("(Parallelogram2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double ux2 = 0.55914166827779;
			double uy2 = 0.829072128825671;
			double et1 = 10;
			double vx2 = -0.989660599000356;
			double vy2 = -0.143429072318889;
			double et2 = 15;
			assertTrue(getS().intersects(createParallelogram(
					10, -10, ux2, uy2, et1, vx2, vy2, et2)));
		}

		@DisplayName("(Parallelogram2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double ux2 = 0.55914166827779;
			double uy2 = 0.829072128825671;
			double et1 = 10;
			double vx2 = -0.989660599000356;
			double vy2 = -0.143429072318889;
			double et2 = 15;
			assertTrue(getS().intersects(createParallelogram(
					5, 5, ux2, uy2, et1, vx2, vy2, et2)));
		}

		@DisplayName("(RoundRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRoundRectangle(0, 0, 2, 1, .1, .05)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(-9, 15, 2, 1, .1, .05)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(-8.7, 15, 2, 1, .1, .05)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(-8.7, 15, 2, 1, .1, .05)));
		}

		@DisplayName("(RoundRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(-8.65, 15, 2, 1, .1, .05)));
		}

		@DisplayName("(RoundRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(-8.64, 15, 2, 1, .1, .05)));
		}

		@DisplayName("(RoundRectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(-8.63, 15, 2, 1, .1, .05)));
		}

		@DisplayName("(RoundRectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRoundRectangle(-8.62, 15, 2, 1, .1, .05)));
		}

		@DisplayName("(RoundRectangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRoundRectangle(-8, 15, 2, 1, .1, .05)));
		}

		@DisplayName("(RoundRectangle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(10, 25, 2, 1, .1, .05)));
		}

		@DisplayName("(RoundRectangle2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(20, -5, 2, 1, .1, .05)));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double ux2 = -0.9284766908852592;
			double uy2 = 0.3713906763541037;
			double et1 = 5;
			double et2 = 3;
			assertFalse(getS().intersects(createOrientedRectangle(-10, -2, ux2, uy2, et1, et2)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double ux2 = -0.9284766908852592;
			double uy2 = 0.3713906763541037;
			double et1 = 5;
			double et2 = 3;
			assertFalse(getS().intersects(createOrientedRectangle(-15, 25, ux2, uy2, et1, et2)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double ux2 = -0.9284766908852592;
			double uy2 = 0.3713906763541037;
			double et1 = 5;
			double et2 = 3;
			assertFalse(getS().intersects(createOrientedRectangle(2, -8, ux2, uy2, et1, et2)));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double ux2 = -0.9284766908852592;
			double uy2 = 0.3713906763541037;
			double et1 = 5;
			double et2 = 3;
			assertTrue(getS().intersects(createOrientedRectangle(2, -7, ux2, uy2, et1, et2)));
		}

		@DisplayName("(OrientedRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double ux2 = -0.9284766908852592;
			double uy2 = 0.3713906763541037;
			double et1 = 5;
			double et2 = 3;
			assertTrue(getS().intersects(createOrientedRectangle(2, -6, ux2, uy2, et1, et2)));
		}

		@DisplayName("(OrientedRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double ux2 = -0.9284766908852592;
			double uy2 = 0.3713906763541037;
			double et1 = 5;
			double et2 = 3;
			assertTrue(getS().intersects(createOrientedRectangle(pEx, pEy, ux2, uy2, et1, et2)));
		}

		@DisplayName("(OrientedRectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double ux2 = -0.9284766908852592;
			double uy2 = 0.3713906763541037;
			double et1 = 5;
			double et2 = 3;
			assertTrue(getS().intersects(createOrientedRectangle(6, 6, ux2, uy2, et1, et2)));
		}

		@DisplayName("(OrientedRectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			double ux2 = -0.9284766908852592;
			double uy2 = 0.3713906763541037;
			double et1 = 5;
			double et2 = 3;
			assertTrue(getS().intersects(createOrientedRectangle(6, 6, ux2, uy2, 10 * et1, 10 * et2)));
		}

		@DisplayName("(Shape2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects((Shape2D) createCircle(6, 2, .5)));
		}

		@DisplayName("(Shape2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects((Shape2D) createRectangle(4, 4, 2, 1)));
		}

	}

	@DisplayName("this += Vector2D")
	@Nested
	public class OperatorAddVector2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().operator_add(createVector(123.456, 789.123));
			assertEpsilonEquals(cx + 123.456, getS().getCenterX());
			assertEpsilonEquals(cy + 789.123, getS().getCenterY());
			assertEpsilonEquals(ux, getS().getFirstAxisX());
			assertEpsilonEquals(uy, getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(vx, getS().getSecondAxisX());
			assertEpsilonEquals(vy, getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("this + Vector2D")
	@Nested
	public class OperatorPlusVector2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			T shape = getS().operator_plus(createVector(123.456, 789.123));
			assertEpsilonEquals(cx + 123.456, getS().getCenterX());
			assertEpsilonEquals(cy + 789.123, getS().getCenterY());
			assertEpsilonEquals(ux, getS().getFirstAxisX());
			assertEpsilonEquals(uy, getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(vx, getS().getSecondAxisX());
			assertEpsilonEquals(vy, getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("this -= Vector2D")
	@Nested
	public class OperatorRemoveVector2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().operator_remove(createVector(123.456, 789.123));
			assertEpsilonEquals(cx - 123.456, getS().getCenterX());
			assertEpsilonEquals(cy - 789.123, getS().getCenterY());
			assertEpsilonEquals(ux, getS().getFirstAxisX());
			assertEpsilonEquals(uy, getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(vx, getS().getSecondAxisX());
			assertEpsilonEquals(vy, getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("this - Vector2D")
	@Nested
	public class OperatorMinusVector2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			T shape = getS().operator_minus(createVector(123.456, 789.123));
			assertEpsilonEquals(cx - 123.456, getS().getCenterX());
			assertEpsilonEquals(cy - 789.123, getS().getCenterY());
			assertEpsilonEquals(ux, getS().getFirstAxisX());
			assertEpsilonEquals(uy, getS().getFirstAxisY());
			assertEpsilonEquals(e1, getS().getFirstAxisExtent());
			assertEpsilonEquals(vx, getS().getSecondAxisX());
			assertEpsilonEquals(vy, getS().getSecondAxisY());
			assertEpsilonEquals(e2, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("this * Transform2D")
	@Nested
	public class OperatorMultiplyTransform2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathIterator2afp pi = getS().operator_multiply(null).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
			assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
			assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
			assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
			assertElement(pi, PathElementType.CLOSE, pGx, pGy);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var transform = new Transform2D();
			var pi = getS().operator_multiply(transform).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
			assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
			assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
			assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
			assertElement(pi, PathElementType.CLOSE, pGx, pGy);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var transform = new Transform2D();
			transform.setTranslation(18,  -45);
			var pi = getS().operator_multiply(transform).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, pGx + 18, pGy - 45);
			assertElement(pi, PathElementType.LINE_TO, pHx + 18, pHy - 45);
			assertElement(pi, PathElementType.LINE_TO, pEx + 18, pEy - 45);
			assertElement(pi, PathElementType.LINE_TO, pFx + 18, pFy - 45);
			assertElement(pi, PathElementType.CLOSE, pGx + 18, pGy - 45);
			assertNoElement(pi);
	
		}

	}

	@DisplayName("this && Point2D")
	@Nested
	public class OperatorAndPoint2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(-20, 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(12, -4)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(14, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(17, 0)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(18, 0)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(21, 8)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(22, 8)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(8, 16)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(-4, 20)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(-4, 21)));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(cx, cy)));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(pEx, pEy)));
		}

	}

	@DisplayName("this && Shape2D")
	@Nested
	public class OperatorAndShape2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createCircle(6, 2, .5)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createRectangle(4, 4, 2, 1)));
		}

	}

	@DisplayName("this .. Point2D")
	@Nested
	public class OperatorUpToPoint2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9.2551, getS().operator_upTo(createPoint(-20, 9)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(0, 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.44135, getS().operator_upTo(createPoint(5, -10)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.18631, getS().operator_upTo(createPoint(14, -20)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(-6, 15)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.14233, getS().operator_upTo(createPoint(0, 35)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(10, 0)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.75805, getS().operator_upTo(createPoint(16, -4)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.99413, getS().operator_upTo(createPoint(-5, 25)));
		}

	}

	@DisplayName("set(double,double,double,double,double,double,double,double)")
	@Nested
	public class SetDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D newU = createVector(-456.789, 159.753).toUnitVector();
			getS().set(-6, -4, newU.getX(), newU.getY(), 147.369, Math.random(), Math.random(), 159.753);
			assertEpsilonEquals(-6, getS().getCenterX());
			assertEpsilonEquals(-4, getS().getCenterY());
			assertEpsilonEquals(newU.getX(), getS().getFirstAxisX());
			assertEpsilonEquals(newU.getY(), getS().getFirstAxisY());
			assertEpsilonEquals(147.369, getS().getFirstAxisExtent());
			assertEpsilonEquals(-newU.getY(), getS().getSecondAxisX());
			assertEpsilonEquals(newU.getX(), getS().getSecondAxisY());
			assertEpsilonEquals(159.753, getS().getSecondAxisExtent());
		}

	}

	@DisplayName("isCCW")
	@Nested
	public class IsCCW {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().isCCW());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createOrientedRectangle(cx, cy, ux, uy, e1, e2).isCCW());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createOrientedRectangle(
					4.7, 15,
					0.12403, 0.99228, 18.02776, 20).isCCW());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createOrientedRectangle(
					-10, -3,
					-.8944271909999159, .4472135954999579, 2, 1).isCCW());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createOrientedRectangle(
					-10, 7,
					-0.9863939238321437, 0.1643989873053573, 1, 2).isCCW());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createOrientedRectangle(
					0, -6,
					-0.9863939238321437, 0.1643989873053573, 1, 2).isCCW());
		}

	}
	
}