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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Shape2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp.TriangleFeature;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractTriangle2afpTest<T extends Triangle2afp<?, T, ?, ?, ?, B>,
B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractShape2afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createTriangle(5, 8, -10, 1, -1, -2);
	}


	protected MultiShape2afp createTestMultiShape(double x, double y) {
		MultiShape2afp multishape = createMultiShape();
		multishape.add(createCircle(x - 5, y + 4, 1));
		multishape.add(createSegment(x + 4, y + 2, x + 8, y - 1));
		return multishape;
	}

	protected Path2afp createSimpleTestPath(double dx, double dy, boolean close) {
		Path2afp path = createPath();
		path.moveTo(dx - 2, dy - 4);
		path.lineTo(dx - 14, dy + 2);
		path.lineTo(dx, dy + 12);
		path.lineTo(dx + 12, dy + 8);
		if (close) {
			path.closePath();
		}
		return path;
	}

	protected Path2afp createComplexTestPath(double dx, double dy, boolean close, PathWindingRule rule) {
		Path2afp path = createPath(rule);
		path.moveTo(dx, dy - 4);
		path.lineTo(dx - 14, dy + 2);
		path.lineTo(dx, dy + 8);
		path.lineTo(dx + 8, dy + 10);
		path.lineTo(dx - 2, dy - 4);
		path.lineTo(dx - 18, dy + 2);
		path.lineTo(dx, dy + 10);
		path.lineTo(dx + 12, dy + 12);
		if (close) {
			path.closePath();
		}
		return path;
	}

	@DisplayName("findsClosestFeatureTrianglePoint")
	@Nested
	public class FindsClosestFeatureTrianglePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertSame(TriangleFeature.THIRD_CORNER,
					Triangle2afp.findsClosestFeatureTrianglePoint(-10, 5, -9, 5, -10, 5.5, -12, 8));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertSame(TriangleFeature.SECOND_SEGMENT,
					Triangle2afp.findsClosestFeatureTrianglePoint(-10, 5, -9, 5, -10, 5.5, -7, 10));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertSame(TriangleFeature.SECOND_CORNER,
					Triangle2afp.findsClosestFeatureTrianglePoint(-10, 5, -9, 5, -10, 5.5, 0, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertSame(TriangleFeature.FIRST_SEGMENT,
					Triangle2afp.findsClosestFeatureTrianglePoint(-10, 5, -9, 5, -10, 5.5, -10, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertSame(TriangleFeature.FIRST_CORNER,
					Triangle2afp.findsClosestFeatureTrianglePoint(-10, 5, -9, 5, -10, 5.5, -12, 1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertSame(TriangleFeature.THIRD_SEGMENT,
					Triangle2afp.findsClosestFeatureTrianglePoint(-10, 5, -9, 5, -10, 5.5, -12, 5.5));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertSame(TriangleFeature.INSIDE,
					Triangle2afp.findsClosestFeatureTrianglePoint(-10, 5, -9, 5, -10, 5.5, -9.9, 5.1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertSame(TriangleFeature.INSIDE,
					Triangle2afp.findsClosestFeatureTrianglePoint(-10, 5, -9, 5, -10, 5.5, -9.9, 5.1));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertSame(TriangleFeature.INSIDE,
					Triangle2afp.findsClosestFeatureTrianglePoint(-10, 5, -9, 5, -10, 5.5, -9.9, 5.1));
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
			assertTrue(Triangle2afp.isCCW(5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.isCCW(-10, 1, -1, -2, 5, 8));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.isCCW(-1, -2, 5, 8, -10, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.isCCW(5, 8, -1, -2, -10, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.isCCW(-1, -2, -10, 1, 5, 8));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.isCCW(-10, 1, 5, 8, -1, -2));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.isCCW(-6, 8, -4, 2, -6, 0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.isCCW(-6, 8, -6, 0, -4, 2));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().set(5, 8, -1, -2, -10, 1);
			assertFalse(getS().isCCW());
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().isCCW());
		}

	}

	@DisplayName("containsTrianglePoint")
	@Nested
	public class ContainsTrianglePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.containsTrianglePoint(5, 8, -10, 1, -1, -2, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.containsTrianglePoint(5, 8, -10, 1, -1, -2, 11, 10));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.containsTrianglePoint(5, 8, -10, 1, -1, -2, 11, 50));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.containsTrianglePoint(5, 8, -10, 1, -1, -2, 9, 12));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.containsTrianglePoint(5, 8, -10, 1, -1, -2, 9, 11));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.containsTrianglePoint(5, 8, -10, 1, -1, -2, 0, 6));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.containsTrianglePoint(5, 8, -10, 1, -1, -2, 8, 12));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.containsTrianglePoint(5, 8, -10, 1, -1, -2, 3, 7));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.containsTrianglePoint(5, 8, -10, 1, -1, -2, 10, 11));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.containsTrianglePoint(5, 8, -10, 1, -1, -2, 9, 10));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.containsTrianglePoint(5, 8, -10, 1, -1, -2, -4, 2));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.containsTrianglePoint(5, 8, -10, 1, -1, -2, -8, -5));
		}

	}

	@DisplayName("containsTriangleRectangle")
	@Nested
	public class ContainsTriangleRectangle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.containsTriangleRectangle(5, 8, -10, 1, -1, -2, 0, 0, 1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.containsTriangleRectangle(5, 8, -10, 1, -1, -2, -2, 1, 1, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.containsTriangleRectangle(5, 8, -10, 1, -1, -2, -30, 20, 1, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.containsTriangleRectangle(5, 8, -10, 1, -1, -2, -10.5, 0.5, 1, 1));
		}

	}

	@DisplayName("findsClosestFarthestPointsTrianglePoint")
	@Nested
	public class FindsClosestFarthestPointsTrianglePoint {

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
			Triangle2afp.findsClosestFarthestPointsTrianglePoint(5, 8, -10, 1, -1, -2, 0, 0, closest, farthest);
			assertEpsilonEquals(0, closest.getX());
			assertEpsilonEquals(0, closest.getY());
			assertEpsilonEquals(-10, farthest.getX());
			assertEpsilonEquals(1, farthest.getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Triangle2afp.findsClosestFarthestPointsTrianglePoint(5, 8, -10, 1, -1, -2, 9, 12, closest, farthest);
			assertEpsilonEquals(5, closest.getX());
			assertEpsilonEquals(8, closest.getY());
			assertEpsilonEquals(-10, farthest.getX());
			assertEpsilonEquals(1, farthest.getY());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Triangle2afp.findsClosestFarthestPointsTrianglePoint(5, 8, -10, 1, -1, -2, 0, 6, closest, farthest);
			assertEpsilonEquals(0.12774, closest.getX());
			assertEpsilonEquals(5.72628, closest.getY());
			assertEpsilonEquals(-10, farthest.getX());
			assertEpsilonEquals(1, farthest.getY());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Triangle2afp.findsClosestFarthestPointsTrianglePoint(5, 8, -10, 1, -1, -2, -20, 1, closest, farthest);
			assertEpsilonEquals(-10, closest.getX());
			assertEpsilonEquals(1, closest.getY());
			assertEpsilonEquals(5, farthest.getX());
			assertEpsilonEquals(8, farthest.getY());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Triangle2afp.findsClosestFarthestPointsTrianglePoint(5, 8, -10, 1, -1, -2, -6, -1, closest, farthest);
			assertEpsilonEquals(-5.8, closest.getX());
			assertEpsilonEquals(-0.4, closest.getY());
			assertEpsilonEquals(5, farthest.getX());
			assertEpsilonEquals(8, farthest.getY());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Triangle2afp.findsClosestFarthestPointsTrianglePoint(5, 8, -10, 1, -1, -2, -1, -6, closest, farthest);
			assertEpsilonEquals(-1, closest.getX());
			assertEpsilonEquals(-2, closest.getY());
			assertEpsilonEquals(5, farthest.getX());
			assertEpsilonEquals(8, farthest.getY());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Triangle2afp.findsClosestFarthestPointsTrianglePoint(5, 8, -10, 1, -1, -2, 6, 2, closest, farthest);
			assertEpsilonEquals(2.61765, closest.getX());
			assertEpsilonEquals(4.02941, closest.getY());
			assertEpsilonEquals(-10, farthest.getX());
			assertEpsilonEquals(1, farthest.getY());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Triangle2afp.findsClosestFarthestPointsTrianglePoint(5, 8, -10, 1, -1, -2, 5, 9, closest, farthest);
			assertEpsilonEquals(5, closest.getX());
			assertEpsilonEquals(8, closest.getY());
			assertEpsilonEquals(-10, farthest.getX());
			assertEpsilonEquals(1, farthest.getY());
		}

	}

	@DisplayName("calculatesSquaredDistanceTrianglePoint")
	@Nested
	public class CalculatesSquaredDistanceTrianglePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, Triangle2afp.calculatesSquaredDistanceTrianglePoint(5, 8, -10, 1, -1, -2, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(32, Triangle2afp.calculatesSquaredDistanceTrianglePoint(5, 8, -10, 1, -1, -2, 9, 12));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(.09124, Triangle2afp.calculatesSquaredDistanceTrianglePoint(5, 8, -10, 1, -1, -2, 0, 6));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(100, Triangle2afp.calculatesSquaredDistanceTrianglePoint(5, 8, -10, 1, -1, -2, -20, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0.40001, Triangle2afp.calculatesSquaredDistanceTrianglePoint(5, 8, -10, 1, -1, -2, -6, -1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(16, Triangle2afp.calculatesSquaredDistanceTrianglePoint(5, 8, -10, 1, -1, -2, -1, -6));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(15.55876, Triangle2afp.calculatesSquaredDistanceTrianglePoint(5, 8, -10, 1, -1, -2, 6, 2));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(1, Triangle2afp.calculatesSquaredDistanceTrianglePoint(5, 8, -10, 1, -1, -2, 5, 9));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(3.97445, Triangle2afp.calculatesSquaredDistanceTrianglePoint(
					-10, 7, -4, 6, -10, 6,
					-3.156934306569343, 4.193430656934306));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, Triangle2afp.calculatesSquaredDistanceTrianglePoint(
					4, 16, 7, 19, 3, 17,
					5, 18));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, Triangle2afp.calculatesSquaredDistanceTrianglePoint(
					4, 16, 3, 17, 7, 19,
					5, 18));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, Triangle2afp.calculatesSquaredDistanceTrianglePoint(
					6, 10, 8, 11.5, 7.5, 8.4,
					7.5, 8.4));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, Triangle2afp.calculatesSquaredDistanceTrianglePoint(
					6, 10, 7.5, 8.4, 8, 11.5,
					7.5, 8.4));
		}

	}

	@DisplayName("intersectsTriangleCircle")
	@Nested
	public class IntersectsTriangleCircle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleCircle(5, 8, -10, 1, -1, -2, 5, 8, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleCircle(5, 8, -10, 1, -1, -2, -10, 1, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleCircle(5, 8, -10, 1, -1, -2, -1, -2, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleCircle(5, 8, -10, 1, -1, -2, 2, 0, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleCircle(5, 8, -10, 1, -1, -2, 1.9, 0, 1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleCircle(5, 8, -10, 1, -1, -2, 1.8, 0, 1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleCircle(5, 8, -10, 1, -1, -2, 1.7, 0, 1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleCircle(5, 8, -10, 1, -1, -2, 1.6, 0, 1));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleCircle(5, 8, -10, 1, -1, -2, 1.5, 0, 1));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleCircle(5, 8, -10, 1, -1, -2, 1.4, 0, 1));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleCircle(5, 8, -10, 1, -1, -2, 1.3, 0, 1));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleCircle(5, 8, -10, 1, -1, -2, 5, 9, 1));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleCircle(5, 8, -10, 1, -1, -2, 5, 8.9, 1));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleCircle(5, 8, -10, 1, -1, -2, -4, 1, 1));
		}

	}

	@DisplayName("intersectsTriangleEllipse")
	@Nested
	public class IntersectsTriangleEllipse {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleEllipse(5, 8, -10, 1, -1, -2, 5, 8, 2, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleEllipse(5, 8, -10, 1, -1, -2, -10, 1, 2, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleEllipse(5, 8, -10, 1, -1, -2, -1, -2, 2, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleEllipse(5, 8, -10, 1, -1, -2, 1, 0, 2, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleEllipse(5, 8, -10, 1, -1, -2, 0.9, 0, 2, 1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleEllipse(5, 8, -10, 1, -1, -2, 0.8, 0, 2, 1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleEllipse(5, 8, -10, 1, -1, -2, 0.7, 0, 2, 1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleEllipse(5, 8, -10, 1, -1, -2, 0.6, 0, 2, 1));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleEllipse(5, 8, -10, 1, -1, -2, 0.5, 0, 2, 1));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleEllipse(5, 8, -10, 1, -1, -2, -1.12464, -2.86312, 2, 1));
		}

	}

	@DisplayName("intersectsTriangleSegment")
	@Nested
	public class IntersectsTriangleSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleSegment(5, 8, -10, 1, -1, -2, -6, -2, -4, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleSegment(5, 8, -10, 1, -1, -2, -6, -2, 2, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleSegment(5, 8, -10, 1, -1, -2, -6, -2, 14, -4));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleSegment(5, 8, -10, 1, -1, -2, -2, 2, 4, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleSegment(5, 8, -10, 1, -1, -2, -2, 2, 0, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleSegment(5, 8, -10, 1, -1, -2, -4, -2, -6, 6));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleSegment(5, 8, -10, 1, -1, -2, 6, 7, -6, 6));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleSegment(5, 8, -10, 1, -1, -2, 0, 5, -6, 6));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleSegment(5, 8, -10, 1, -1, -2, -5, 5, -6, 6));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleSegment(5, 8, -10, 1, -1, -2, -4, -2, 2, -2));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleSegment(5, 8, -10, 1, -1, -2, -1, -2, 5, 8));
		}

	}

	@DisplayName("intersectsTriangleTriangle")
	@Nested
	public class IntersectsTriangleTriangle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					-8, 6.5, -4, 6, -7, 11));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					-8, -2, -10, -4, -6, -6));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					6, 2, 8, -2, 16, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					0, -4, -2, -6, 2, -8));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					8, 14, 8, 12, 12, 12));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					-16, 2, -16, 0, -14, 2));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					-16, 2, -12, 6, -12, 8));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					-6, 8, -6, 0, -4, 2));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					-6, 8, -8, 6, -4, 2));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					-6, 8, -8, 6, -4, -4));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					-6, 8, -8, 6, 4, 2));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					0, 4, -6, 0, 2, -2));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					-16, 2, -12, -6, -12, 8));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					-16, 0, -10, 1, -14, 2));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(5, 8, -10, 1, -1, -2,
					-1, -2, -10, 1, -14, 2));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(
					-8, 6.5, -4, 6, -7, 11, 5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(
					-8, -2, -10, -4, -6, -6, 5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(
					6, 2, 8, -2, 16, 0, 5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(
					0, -4, -2, -6, 2, -8, 5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(
					8, 14, 8, 12, 12, 12, 5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(
					-16, 2, -16, 0, -14, 2, 5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(
					-16, 2, -12, 6, -12, 8, 5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleTriangle(
					-6, 8, -6, 0, -4, 2, 5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleTriangle(
					-6, 8, -8, 6, -4, 2, 5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_25(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleTriangle(
					-6, 8, -8, 6, -4, -4, 5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_26(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleTriangle(
					-6, 8, -8, 6, 4, 2, 5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_27(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleTriangle(
					0, 4, -6, 0, 2, -2, 5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_28(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(
					-16, 2, -12, -6, -12, 8, 5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_29(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(
					-16, 0, -10, 1, -14, 2, 5, 8, -10, 1, -1, -2));
		}

		@DisplayName("#30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_30(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleTriangle(
					-1, -2, -10, 1, -14, 2, 5, 8, -10, 1, -1, -2));
		}

	}

	@DisplayName("intersectsTriangleRectangle")
	@Nested
	public class IntersectsTriangleRectangle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleRectangle(5, 8, -10, 1, -1, -2, -6, -2, 1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleRectangle(5, 8, -10, 1, -1, -2, -6, 6, 1, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleRectangle(5, 8, -10, 1, -1, -2, 6, 6, 1, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleRectangle(5, 8, -10, 1, -1, -2, -16, 0, 1, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleRectangle(5, 8, -10, 1, -1, -2, 12, 12, 1, 1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleRectangle(5, 8, -10, 1, -1, -2, 0, -6, 1, 1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleRectangle(5, 8, -10, 1, -1, -2, -4, 2, 1, 1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleRectangle(5, 8, -10, 1, -1, -2, -4, 4, 1, 1));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleRectangle(5, 8, -10, 1, -1, -2, 0, 6, 1, 1));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(Triangle2afp.intersectsTriangleRectangle(5, 8, -10, 1, -1, -2, 2, 4, 1, 1));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(Triangle2afp.intersectsTriangleRectangle(5, 8, -10, 1, -1, -2, 5, 8, 1, 1));
		}

	}

	@DisplayName("getX1")
	@Nested
	public class GetX1 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(5, getS().getX1());
		}

	}

	@DisplayName("getY1")
	@Nested
	public class GetY1 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(8, getS().getY1());
		}

	}

	@DisplayName("getX2")
	@Nested
	public class GetX2 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(-10, getS().getX2());
		}

	}

	@DisplayName("getY2")
	@Nested
	public class GetY2 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(1, getS().getY2());
		}

	}

	@DisplayName("getX3")
	@Nested
	public class GetX3 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(-1, getS().getX3());
		}

	}

	@DisplayName("getY3")
	@Nested
	public class GetY3 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(-2, getS().getY3());
		}

	}

	@DisplayName("getP1")
	@Nested
	public class GetP1 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Point2D p = getS().getP1();
			assertNotNull(p);
			assertEpsilonEquals(5, p.getX());
			assertEpsilonEquals(8, p.getY());
		}

	}

	@DisplayName("getP2")
	@Nested
	public class GetP2 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Point2D p = getS().getP2();
			assertNotNull(p);
			assertEpsilonEquals(-10, p.getX());
			assertEpsilonEquals(1, p.getY());
		}
	}

	@DisplayName("getP3")
	@Nested
	public class GetP3 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Point2D p = getS().getP3();
			assertNotNull(p);
			assertEpsilonEquals(-1, p.getX());
			assertEpsilonEquals(-2, p.getY());
		}

	}

	@DisplayName("setP1(Point2D)")
	@Nested
	public class SetP1Point2D {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().setP1(createPoint(123.456, 456.789));
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(456.789, getS().getY1());
			assertEpsilonEquals(-10, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(-1, getS().getX3());
			assertEpsilonEquals(-2, getS().getY3());
		}

	}

	@DisplayName("setP1(double,double)")
	@Nested
	public class SetP1DoubleDouble {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().setP1(123.456, 456.789);
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(456.789, getS().getY1());
			assertEpsilonEquals(-10, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(-1, getS().getX3());
			assertEpsilonEquals(-2, getS().getY3());
		}

	}

	@DisplayName("setP2(Point2D)")
	@Nested
	public class SetP2Point2D {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().setP2(createPoint(123.456, 456.789));
			assertEpsilonEquals(5, getS().getX1());
			assertEpsilonEquals(8, getS().getY1());
			assertEpsilonEquals(123.456, getS().getX2());
			assertEpsilonEquals(456.789, getS().getY2());
			assertEpsilonEquals(-1, getS().getX3());
			assertEpsilonEquals(-2, getS().getY3());
		}

	}

	@DisplayName("setP2(double,double)")
	@Nested
	public class SetP2DoubleDouble {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().setP2(123.456, 456.789);
			assertEpsilonEquals(5, getS().getX1());
			assertEpsilonEquals(8, getS().getY1());
			assertEpsilonEquals(123.456, getS().getX2());
			assertEpsilonEquals(456.789, getS().getY2());
			assertEpsilonEquals(-1, getS().getX3());
			assertEpsilonEquals(-2, getS().getY3());
		}

	}

	@DisplayName("setP3(Point2D)")
	@Nested
	public class SetP3Point2D {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().setP3(createPoint(123.456, 456.789));
			assertEpsilonEquals(5, getS().getX1());
			assertEpsilonEquals(8, getS().getY1());
			assertEpsilonEquals(-10, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(123.456, getS().getX3());
			assertEpsilonEquals(456.789, getS().getY3());
		}

	}

	@DisplayName("setP3(double,double)")
	@Nested
	public class SetP3DoubleDouble {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().setP3(123.456, 456.789);
			assertEpsilonEquals(5, getS().getX1());
			assertEpsilonEquals(8, getS().getY1());
			assertEpsilonEquals(-10, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(123.456, getS().getX3());
			assertEpsilonEquals(456.789, getS().getY3());
		}

	}

	@DisplayName("setX1")
	@Nested
	public class SetX1 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().setX1(123.456);
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(8, getS().getY1());
			assertEpsilonEquals(-10, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(-1, getS().getX3());
			assertEpsilonEquals(-2, getS().getY3());
		}

	}

	@DisplayName("setY1")
	@Nested
	public class SetY1 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().setY1(123.456);
			assertEpsilonEquals(5, getS().getX1());
			assertEpsilonEquals(123.456, getS().getY1());
			assertEpsilonEquals(-10, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(-1, getS().getX3());
			assertEpsilonEquals(-2, getS().getY3());
		}
	}

	@DisplayName("setX2")
	@Nested
	public class SetX2 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().setX2(123.456);
			assertEpsilonEquals(5, getS().getX1());
			assertEpsilonEquals(8, getS().getY1());
			assertEpsilonEquals(123.456, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(-1, getS().getX3());
			assertEpsilonEquals(-2, getS().getY3());
		}

	}

	@DisplayName("setY2")
	@Nested
	public class SetY2 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().setY2(123.456);
			assertEpsilonEquals(5, getS().getX1());
			assertEpsilonEquals(8, getS().getY1());
			assertEpsilonEquals(-10, getS().getX2());
			assertEpsilonEquals(123.456, getS().getY2());
			assertEpsilonEquals(-1, getS().getX3());
			assertEpsilonEquals(-2, getS().getY3());
		}

	}

	@DisplayName("setX3")
	@Nested
	public class SetX3 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().setX3(123.456);
			assertEpsilonEquals(5, getS().getX1());
			assertEpsilonEquals(8, getS().getY1());
			assertEpsilonEquals(-10, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(123.456, getS().getX3());
			assertEpsilonEquals(-2, getS().getY3());
		}

	}

	@DisplayName("setY3")
	@Nested
	public class SetY3 {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().setY3(123.456);
			assertEpsilonEquals(5, getS().getX1());
			assertEpsilonEquals(8, getS().getY1());
			assertEpsilonEquals(-10, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(-1, getS().getX3());
			assertEpsilonEquals(123.456, getS().getY3());
		}

	}

	@DisplayName("set(double,double,double,double,double,double)")
	@Nested
	public class SetDoubleDoubleDoubleDoubleDoubleDouble {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().set(1.2, 3.4, 5.6, 7.8, 9.1, 3.2);
			assertEpsilonEquals(1.2, getS().getX1());
			assertEpsilonEquals(3.4, getS().getY1());
			assertEpsilonEquals(5.6, getS().getX2());
			assertEpsilonEquals(7.8, getS().getY2());
			assertEpsilonEquals(9.1, getS().getX3());
			assertEpsilonEquals(3.2, getS().getY3());
		}

	}

	@DisplayName("clone")
	@Nested
	public class CloneTest {

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			T clone = getS().clone();
			assertNotNull(clone);
			assertNotSame(getS(), clone);
			assertEquals(getS().getClass(), clone.getClass());
			assertEpsilonEquals(5, clone.getX1());
			assertEpsilonEquals(8, clone.getY1());
			assertEpsilonEquals(-10, clone.getX2());
			assertEpsilonEquals(1, clone.getY2());
			assertEpsilonEquals(-1, clone.getX3());
			assertEpsilonEquals(-2, clone.getY3());
		}
	}

	@DisplayName("equals(Object}")
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
			assertFalse(getS().equals(createTriangle(5, 8, -10, 1, -1, -3)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().equals(createTriangle(-1, -2, 5, 8, -10, 1)));
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
			assertTrue(getS().equals(getS()));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().equals(createTriangle(5, 8, -10, 1, -1, -2)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().equals(createTriangle(5, 8, -10, 1, -1, -3).getPathIterator()));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().equals(createTriangle(-1, -2, 5, 8, -10, 1).getPathIterator()));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().equals(createSegment(5, 8, 6, 10).getPathIterator()));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().equals(getS().getPathIterator()));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().equals(createTriangle(5, 8, -10, 1, -1, -2).getPathIterator()));
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
			assertFalse(getS().equalsToPathIterator((PathIterator2ai) null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().equalsToPathIterator(createTriangle(5, 8, -10, 1, -1, -3).getPathIterator()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().equalsToPathIterator(createTriangle(-1, -2, 5, 8, -10, 1).getPathIterator()));
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
			assertTrue(getS().equalsToPathIterator(createTriangle(5, 8, -10, 1, -1, -2).getPathIterator()));
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

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			getS().clear();
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getX2());
			assertEpsilonEquals(0, getS().getY2());
			assertEpsilonEquals(0, getS().getX3());
			assertEpsilonEquals(0, getS().getY3());
		}

	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().contains(0,0));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(11,50));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(9,12));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(9,11));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(0,6));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(8,12));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().contains(3,7));
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(10,11));
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(9,10));
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().contains(-4,2));
		}

		@DisplayName("(Point2D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(-8,-5));
		}

		@DisplayName("(Point2D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().contains(createPoint(0,0)));
		}

		@DisplayName("(Point2D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createPoint(11,10)));
		}

		@DisplayName("(Point2D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createPoint(11,50)));
		}

		@DisplayName("(Point2D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createPoint(9,12)));
		}

		@DisplayName("(Point2D) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createPoint(9,11)));
		}

		@DisplayName("(Point2D) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createPoint(0,6)));
		}

		@DisplayName("(Point2D) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createPoint(8,12)));
		}

		@DisplayName("(Point2D) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().contains(createPoint(3,7)));
		}

		@DisplayName("(Point2D) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createPoint(10,11)));
		}

		@DisplayName("(Point2D) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createPoint(9,10)));
		}

		@DisplayName("(Point2D) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().contains(createPoint(-4,2)));
		}

		@DisplayName("(Point2D) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createPoint(-8,-5)));
		}

		@DisplayName("(Point2D) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(11,10));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createRectangle(0, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().contains(createRectangle(-2, 1, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createRectangle(-30, 20, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createRectangle(-10.5, 0.5, 1, 1)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createCircle(0, 0, 1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().contains(createCircle(-2, 1, 1)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createCircle(-30, 20, 1)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().contains(createCircle(-10.5, 0.5, 1)));
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
			var closest = getS().getClosestPointTo(createPoint(0, 0));
			assertEpsilonEquals(0, closest.getX());
			assertEpsilonEquals(0, closest.getY());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var closest = getS().getClosestPointTo(createPoint(9, 12));
			assertEpsilonEquals(5, closest.getX());
			assertEpsilonEquals(8, closest.getY());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var closest = getS().getClosestPointTo(createPoint(0, 6));
			assertEpsilonEquals(0.12774, closest.getX());
			assertEpsilonEquals(5.72628, closest.getY());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var closest = getS().getClosestPointTo(createPoint(-20, 1));
			assertEpsilonEquals(-10, closest.getX());
			assertEpsilonEquals(1, closest.getY());
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var closest = getS().getClosestPointTo(createPoint(-6, -1));
			assertEpsilonEquals(-5.8, closest.getX());
			assertEpsilonEquals(-0.4, closest.getY());
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var closest = getS().getClosestPointTo(createPoint(-1, -6));
			assertEpsilonEquals(-1, closest.getX());
			assertEpsilonEquals(-2, closest.getY());
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var closest = getS().getClosestPointTo(createPoint(6, 2));
			assertEpsilonEquals(2.61765, closest.getX());
			assertEpsilonEquals(4.02941, closest.getY());
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-7.7, 2.07, getS().getClosestPointTo(createCircle(-10, 7, 1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-1, -2, getS().getClosestPointTo(createCircle(0, -10, 1)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createCircle(-1, 5, 1));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-3.15693, 4.19343, getS().getClosestPointTo(createSegment(-10, 7, -4, 6)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-1, -2, getS().getClosestPointTo(createSegment(0, -4, -1, -8)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createSegment(-6, -2, 2, 0));
		}

		@DisplayName("(Triangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-3.15693, 4.19343, getS().getClosestPointTo(createTriangle(-10, 7, -4, 6, -10, 6)));
		}

		@DisplayName("(Triangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-1, -2, getS().getClosestPointTo(createTriangle(0, -4, -1, -8, 0, -8)));
		}

		@DisplayName("(Triangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createTriangle(-6, -2, 2, 0, -6, 0));
		}

		@DisplayName("(Ellipse2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-6.24279, 2.75337, getS().getClosestPointTo(createEllipse(-10, 7, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-1, -2, getS().getClosestPointTo(createEllipse(0, -4, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createEllipse(-5, -2, 2, 1));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-6.05839, 2.83942, getS().getClosestPointTo(createRectangle(-10, 7, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-1, -2, getS().getClosestPointTo(createRectangle(0, -4, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createRectangle(-5, -2, 2, 1));
		}

		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-1.5146, 4.95985, getS().getClosestPointTo(createTestMultiShape(-10, 7)));
		}

		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createTestMultiShape(0, -4));
		}

		@DisplayName("(MultiShape2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-4.6, -0.8, getS().getClosestPointTo(createTestMultiShape(0, -6)));
		}

		@DisplayName("(MultiShape2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createTestMultiShape(-5, -2));
		}

		@DisplayName("(RoundRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-6.96782, 2.41502, getS().getClosestPointTo(createRoundRectangle(-10, 7, 1, 1, .2, .4)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-1, -2, getS().getClosestPointTo(createRoundRectangle(0, -5, 1, 1, .2, .4)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createRoundRectangle(-4, -2, 1, 1, .2, .4));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Vector2D u = createVector(-3, .5).toUnitVector();
			assertFpPointEquals(-7.97973, 1.94279, getS().getClosestPointTo(createOrientedRectangle(-10, 7, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Vector2D u = createVector(-3, .5).toUnitVector();
			assertFpPointEquals(-1, -2, getS().getClosestPointTo(createOrientedRectangle(0, -6, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Vector2D u = createVector(-3, .5).toUnitVector();
			assertClosestPointInBothShapes(getS(), createOrientedRectangle(-5, -2, u.getX(), u.getY(), 1, 2));
		}

		@DisplayName("(Parallelogram2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Vector2D u = createVector(-3, .5).toUnitVector();
			Vector2D v = createVector(5, .1).toUnitVector();
			assertFpPointEquals(-5.2964, 3.19501, getS().getClosestPointTo(createParallelogram(-10, 7, u.getX(), u.getY(), 1, v.getX(), v.getY(), 2)));
		}

		@DisplayName("(Parallelogram2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Vector2D u = createVector(-3, .5).toUnitVector();
			Vector2D v = createVector(5, .1).toUnitVector();
			assertFpPointEquals(-1, -2, getS().getClosestPointTo(createParallelogram(0, -6, u.getX(), u.getY(), 1, v.getX(), v.getY(), 2)));
		}

		@DisplayName("(Parallelogram2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Vector2D u = createVector(-3, .5).toUnitVector();
			Vector2D v = createVector(5, .1).toUnitVector();
			assertClosestPointInBothShapes(getS(), createParallelogram(-5, -1, u.getX(), u.getY(), 1, v.getX(), v.getY(), 2));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-10, 1, getS().getClosestPointTo(createSimpleTestPath(0, 0, false)));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createSimpleTestPath(0, 0, true));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-10, 1, getS().getClosestPointTo(createSimpleTestPath(2, -2, false)));
		}

		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createSimpleTestPath(2, -2, true));
		}

		@DisplayName("(Path2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createSimpleTestPath(4, -6, false));
		}

		@DisplayName("(Path2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createSimpleTestPath(4, -6, true));
		}

		@DisplayName("(Path2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-10, 1, getS().getClosestPointTo(createSimpleTestPath(-20, -10, false)));
		}

		@DisplayName("(Path2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-10, 1, getS().getClosestPointTo(createSimpleTestPath(-20, -10, true)));
		}

		@DisplayName("(Path2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-1, -2, getS().getClosestPointTo(createComplexTestPath(0, 0, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-1, -2, getS().getClosestPointTo(createComplexTestPath(0, 0, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-10, 1, getS().getClosestPointTo(createComplexTestPath(-12, 0, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createComplexTestPath(-12, 0, true, PathWindingRule.EVEN_ODD));
		}

		@DisplayName("(Path2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-10, 1, getS().getClosestPointTo(createComplexTestPath(-16, 2, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-10, 1, getS().getClosestPointTo(createComplexTestPath(-16, 2, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-1, -2, getS().getClosestPointTo(createComplexTestPath(0, 0, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createComplexTestPath(0, 0, true, PathWindingRule.NON_ZERO));
		}

		@DisplayName("(Path2afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-10, 1, getS().getClosestPointTo(createComplexTestPath(-12, 0, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertClosestPointInBothShapes(getS(), createComplexTestPath(-12, 0, true, PathWindingRule.NON_ZERO));
		}

		@DisplayName("(Path2afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-10, 1, getS().getClosestPointTo(createComplexTestPath(-16, 2, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFpPointEquals(-10, 1, getS().getClosestPointTo(createComplexTestPath(-16, 2, true, PathWindingRule.NON_ZERO)));
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
			var farthest = getS().getFarthestPointTo(createPoint(0, 0));
			assertEpsilonEquals(-10, farthest.getX());
			assertEpsilonEquals(1, farthest.getY());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var farthest = getS().getFarthestPointTo(createPoint(9, 12));
			assertEpsilonEquals(-10, farthest.getX());
			assertEpsilonEquals(1, farthest.getY());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var farthest = getS().getFarthestPointTo(createPoint(0, 6));
			assertEpsilonEquals(-10, farthest.getX());
			assertEpsilonEquals(1, farthest.getY());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var farthest = getS().getFarthestPointTo(createPoint(-20, 1));
			assertEpsilonEquals(5, farthest.getX());
			assertEpsilonEquals(8, farthest.getY());
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var farthest = getS().getFarthestPointTo(createPoint(-6, -1));
			assertEpsilonEquals(5, farthest.getX());
			assertEpsilonEquals(8, farthest.getY());
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var farthest = getS().getFarthestPointTo(createPoint(-1, -6));
			assertEpsilonEquals(5, farthest.getX());
			assertEpsilonEquals(8, farthest.getY());
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var farthest = getS().getFarthestPointTo(createPoint(6, 2));
			assertEpsilonEquals(-10, farthest.getX());
			assertEpsilonEquals(1, farthest.getY());
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
			assertEpsilonEquals(0, getS().getDistance(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(5.65685, getS().getDistance(createPoint(9, 12)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0.30206, getS().getDistance(createPoint(0, 6)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(10, getS().getDistance(createPoint(-20, 1)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0.63246, getS().getDistance(createPoint(-6, -1)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(4, getS().getDistance(createPoint(-1, -6)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(3.94446, getS().getDistance(createPoint(6, 2)));
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
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(32, getS().getDistanceSquared(createPoint(9, 12)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(.09124, getS().getDistanceSquared(createPoint(0, 6)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(100, getS().getDistanceSquared(createPoint(-20, 1)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0.40001, getS().getDistanceSquared(createPoint(-6, -1)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(16, getS().getDistanceSquared(createPoint(-1, -6)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(15.55876, getS().getDistanceSquared(createPoint(6, 2)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(19.68785, getS().getDistanceSquared(createCircle(-10, 7, 1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(49.87548, getS().getDistanceSquared(createCircle(0, -10, 1)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(-1, 5, 1)));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(3.97445, getS().getDistanceSquared(createSegment(-10, 7, -4, 6)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(5, getS().getDistanceSquared(createSegment(0, -4, -1, -8)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(-6, -2, 2, 0)));
		}

		@DisplayName("(Triangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(3.97445, getS().getDistanceSquared(createTriangle(-10, 7, -4, 6, -10, 6)));
		}

		@DisplayName("(Triangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(5, getS().getDistanceSquared(createTriangle(0, -4, -1, -8, 0, -8)));
		}

		@DisplayName("(Triangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createTriangle(-6, -2, 2, 0, -6, 0)));
		}

		@DisplayName("(Ellipse2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(23.49915, getS().getDistanceSquared(createEllipse(-10, 7, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(2.84801, getS().getDistanceSquared(createEllipse(0, -4, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createEllipse(-5, -2, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(21.08029, getS().getDistanceSquared(createRectangle(-10, 7, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(2, getS().getDistanceSquared(createRectangle(0, -4, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(-5, -2, 2, 1)));
		}

		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(1.31752, getS().getDistanceSquared(createTestMultiShape(-10, 7)));
		}

		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(0, -4)));
		}

		@DisplayName("(MultiShape2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0.070178, getS().getDistanceSquared(createTestMultiShape(0, -6)));
		}

		@DisplayName("(MultiShape2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(-5, -2)));
		}

		@DisplayName("(RoundRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(25.8999, getS().getDistanceSquared(createRoundRectangle(-10, 7, 1, 1, .2, .4)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(5.37295, getS().getDistanceSquared(createRoundRectangle(0, -5, 1, 1, .2, .4)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(-4, -2, 1, 1, .2, .4)));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Vector2D u = createVector(-3, .5).toUnitVector();
			assertEpsilonEquals(10.3834, getS().getDistanceSquared(createOrientedRectangle(-10, 7, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Vector2D u = createVector(-3, .5).toUnitVector();
			assertEpsilonEquals(3.58731, getS().getDistanceSquared(createOrientedRectangle(0, -6, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Vector2D u = createVector(-3, .5).toUnitVector();
			assertEpsilonEquals(0, getS().getDistanceSquared(createOrientedRectangle(-5, -2, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(Parallelogram2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Vector2D u = createVector(-3, .5).toUnitVector();
			Vector2D v = createVector(5, .1).toUnitVector();
			assertEpsilonEquals(16.49686, getS().getDistanceSquared(createParallelogram(-10, 7, u.getX(), u.getY(), 1, v.getX(), v.getY(), 2)));
		}

		@DisplayName("(Parallelogram2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Vector2D u = createVector(-3, .5).toUnitVector();
			Vector2D v = createVector(5, .1).toUnitVector();
			assertEpsilonEquals(14.70804, getS().getDistanceSquared(createParallelogram(0, -6, u.getX(), u.getY(), 1, v.getX(), v.getY(), 2)));
		}

		@DisplayName("(Parallelogram2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Vector2D u = createVector(-3, .5).toUnitVector();
			Vector2D v = createVector(5, .1).toUnitVector();
			assertEpsilonEquals(0, getS().getDistanceSquared(createParallelogram(-5, -1, u.getX(), u.getY(), 1, v.getX(), v.getY(), 2)));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(.8, getS().getDistanceSquared(createSimpleTestPath(0, 0, false)));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createSimpleTestPath(0, 0, true)));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0.12162, getS().getDistanceSquared(createSimpleTestPath(2, -2, false)));
		}

		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createSimpleTestPath(2, -2, true)));
		}

		@DisplayName("(Path2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createSimpleTestPath(4, -6, false)));
		}

		@DisplayName("(Path2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createSimpleTestPath(4, -6, false)));
		}

		@DisplayName("(Path2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(4.9, getS().getDistanceSquared(createSimpleTestPath(-20, -10, true)));
		}

		@DisplayName("(Path2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(4.9, getS().getDistanceSquared(createSimpleTestPath(-20, -10, true)));
		}

		@DisplayName("(Path2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0.12162, getS().getDistanceSquared(createComplexTestPath(0, 0, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0.12162, getS().getDistanceSquared(createComplexTestPath(0, 0, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0.12162, getS().getDistanceSquared(createComplexTestPath(-12, 0, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createComplexTestPath(-12, 0, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(22.71622, getS().getDistanceSquared(createComplexTestPath(-16, 2, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(9, getS().getDistanceSquared(createComplexTestPath(-16, 2, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0.12162, getS().getDistanceSquared(createComplexTestPath(0, 0, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createComplexTestPath(0, 0, true, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0.12162, getS().getDistanceSquared(createComplexTestPath(-12, 0, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().getDistanceSquared(createComplexTestPath(-12, 0, true, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(22.71622, getS().getDistanceSquared(createComplexTestPath(-16, 2, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(9, getS().getDistanceSquared(createComplexTestPath(-16, 2, true, PathWindingRule.NON_ZERO)));
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
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(8, getS().getDistanceL1(createPoint(9, 12)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0.40146, getS().getDistanceL1(createPoint(0, 6)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(10, getS().getDistanceL1(createPoint(-20, 1)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(.8, getS().getDistanceL1(createPoint(-6, -1)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(4, getS().getDistanceL1(createPoint(-1, -6)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(5.41176, getS().getDistanceL1(createPoint(6, 2)));
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
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(4, getS().getDistanceLinf(createPoint(9, 12)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(.27372, getS().getDistanceLinf(createPoint(0, 6)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(10, getS().getDistanceLinf(createPoint(-20, 1)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(.6, getS().getDistanceLinf(createPoint(-6, -1)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(4, getS().getDistanceLinf(createPoint(-1, -6)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(3.38235, getS().getDistanceLinf(createPoint(6, 2)));
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
			getS().set((T) createTriangle(17, 20, 7, 45, 7, -4));
			assertEpsilonEquals(17, getS().getX1());
			assertEpsilonEquals(20, getS().getY1());
			assertEpsilonEquals(7, getS().getX2());
			assertEpsilonEquals(45, getS().getY2());
			assertEpsilonEquals(7, getS().getX3());
			assertEpsilonEquals(-4, getS().getY3());
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
			getS().translate(2, -3);
			assertEpsilonEquals(7, getS().getX1());
			assertEpsilonEquals(5, getS().getY1());
			assertEpsilonEquals(-8, getS().getX2());
			assertEpsilonEquals(-2, getS().getY2());
			assertEpsilonEquals(1, getS().getX3());
			assertEpsilonEquals(-5, getS().getY3());
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
			getS().translate(createVector(2, -3));
			assertEpsilonEquals(7, getS().getX1());
			assertEpsilonEquals(5, getS().getY1());
			assertEpsilonEquals(-8, getS().getX2());
			assertEpsilonEquals(-2, getS().getY2());
			assertEpsilonEquals(1, getS().getX3());
			assertEpsilonEquals(-5, getS().getY3());
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
			assertEpsilonEquals(-10, box.getMinX());
			assertEpsilonEquals(-2, box.getMinY());
			assertEpsilonEquals(5, box.getMaxX());
			assertEpsilonEquals(8, box.getMaxY());
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
			assertEpsilonEquals(-10, box.getMinX());
			assertEpsilonEquals(-2, box.getMinY());
			assertEpsilonEquals(5, box.getMaxX());
			assertEpsilonEquals(8, box.getMaxY());
		}
	}

	@DisplayName("getPathIterator(Transform2D)")
	@Nested
	public class GetPathIteratorTransform2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			PathIterator2afp pi = getS().getPathIterator(null);
			assertElement(pi, PathElementType.MOVE_TO, 5, 8);
			assertElement(pi, PathElementType.LINE_TO, -10, 1);
			assertElement(pi, PathElementType.LINE_TO, -1, -2);
			assertElement(pi, PathElementType.CLOSE, 5, 8);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var tr = new Transform2D();
			var pi = getS().getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 5, 8);
			assertElement(pi, PathElementType.LINE_TO, -10, 1);
			assertElement(pi, PathElementType.LINE_TO, -1, -2);
			assertElement(pi, PathElementType.CLOSE, 5, 8);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var tr = new Transform2D();
			tr.makeTranslationMatrix(10, -10);
			var pi = getS().getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 15, -2);
			assertElement(pi, PathElementType.LINE_TO, 0, -9);
			assertElement(pi, PathElementType.LINE_TO, 9, -12);
			assertElement(pi, PathElementType.CLOSE, 15, -2);
			assertNoElement(pi);
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
			assertElement(pi, PathElementType.MOVE_TO, 5, 8);
			assertElement(pi, PathElementType.LINE_TO, -10, 1);
			assertElement(pi, PathElementType.LINE_TO, -1, -2);
			assertElement(pi, PathElementType.CLOSE, 5, 8);
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
			var newShape = getS().createTransformedShape(null);
			assertNotNull(newShape);
			assertNotSame(getS(), newShape);
			assertEquals(getS(), newShape);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var tr = new Transform2D();
			var newShape = getS().createTransformedShape(tr);
			assertNotNull(newShape);
			assertNotSame(getS(), newShape);
			assertEquals(getS(), newShape);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var tr = new Transform2D();
			tr.makeTranslationMatrix(10, -10);
			var newShape = getS().createTransformedShape(tr);
			assertNotNull(newShape);
			assertNotSame(getS(), newShape);
			assertTrue(newShape instanceof Triangle2afp);
			PathIterator2afp pi = getS().getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 15, -2);
			assertElement(pi, PathElementType.LINE_TO, 0, -9);
			assertElement(pi, PathElementType.LINE_TO, 9, -12);
			assertElement(pi, PathElementType.CLOSE, 15, -2);
			assertNoElement(pi);
		}
	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createRectangle(-6, -2, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createRectangle(-6, 6, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createRectangle(6, 6, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createRectangle(-16, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createRectangle(12, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createRectangle(0, -6, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createRectangle(-4, 2, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createRectangle(-4, 4, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createRectangle(0, 6, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createRectangle(2, 4, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createRectangle(5, 8, 1, 1)));
		}

		@DisplayName("(Triangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createTriangle(
					-8, 6.5, -4, 6, -7, 11)));
		}

		@DisplayName("(Triangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createTriangle(
					-8, -2, -10, -4, -6, -6)));
		}

		@DisplayName("(Triangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createTriangle(
					6, 2, 8, -2, 16, 0)));
		}

		@DisplayName("(Triangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createTriangle(
					0, -4, -2, -6, 2, -8)));
		}

		@DisplayName("(Triangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createTriangle(
					8, 14, 8, 12, 12, 12)));
		}

		@DisplayName("(Triangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createTriangle(
					-16, 2, -16, 0, -14, 2)));
		}

		@DisplayName("(Triangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createTriangle(
					-16, 2, -12, 6, -12, 8)));
		}

		@DisplayName("(Triangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createTriangle(
					-6, 8, -6, 0, -4, 2)));
		}

		@DisplayName("(Triangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createTriangle(
					-6, 8, -8, 6, -4, 2)));
		}

		@DisplayName("(Triangle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createTriangle(
					-6, 8, -8, 6, -4, -4)));
		}

		@DisplayName("(Triangle2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createTriangle(
					-6, 8, -8, 6, 4, 2)));
		}

		@DisplayName("(Triangle2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createTriangle(
					0, 4, -6, 0, 2, -2)));
		}

		@DisplayName("(Triangle2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createTriangle(
					-16, 2, -12, -6, -12, 8)));
		}

		@DisplayName("(Triangle2afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createTriangle(
					-16, 0, -10, 1, -14, 2)));
		}

		@DisplayName("(Triangle2afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createTriangle(
					-1, -2, -10, 1, -14, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			OrientedRectangle2afp rectangle = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertFalse(createTriangle(-10, 15, -8, 16, -13, 19).intersects(rectangle));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			OrientedRectangle2afp rectangle = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertFalse(createTriangle(-5, 30, -3, 31, -8, 34).intersects(rectangle));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			OrientedRectangle2afp rectangle = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertFalse(createTriangle(15, 25, 17, 26, 12, 29).intersects(rectangle));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			OrientedRectangle2afp rectangle = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertFalse(createTriangle(40, 15, 42, 16, 37, 19).intersects(rectangle));
		}

		@DisplayName("(OrientedRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			OrientedRectangle2afp rectangle = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertFalse(createTriangle(35, 0, 37, 1, 32, 4).intersects(rectangle));
		}

		@DisplayName("(OrientedRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			OrientedRectangle2afp rectangle = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertFalse(createTriangle(15, -20, 17, -19, 12, -16).intersects(rectangle));
		}

		@DisplayName("(OrientedRectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			OrientedRectangle2afp rectangle = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertFalse(createTriangle(-5, -10, -3, -9, -8, -6).intersects(rectangle));
		}

		@DisplayName("(OrientedRectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			OrientedRectangle2afp rectangle = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertFalse(createTriangle(-25, -5, -23, -4, -28, -1).intersects(rectangle));
		}

		@DisplayName("(OrientedRectangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			OrientedRectangle2afp rectangle = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertTrue(createTriangle(-4, -2, -2, -1, -7, -2).intersects(rectangle));
		}

		@DisplayName("(OrientedRectangle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			OrientedRectangle2afp rectangle = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertTrue(createTriangle(-2, 4, 0, 5, -5, 8).intersects(rectangle));
		}

		@DisplayName("(OrientedRectangle2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			OrientedRectangle2afp rectangle = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertTrue(createTriangle(20, 5, 22, 6, 17, 9).intersects(rectangle));
		}

		@DisplayName("(OrientedRectangle2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			OrientedRectangle2afp rectangle = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertTrue(createTriangle(20, 5, 22, 6, -10, 15).intersects(rectangle));
		}

		@DisplayName("(OrientedRectangle2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			OrientedRectangle2afp rectangle = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertTrue(createTriangle(50, 30, 0, -50, -30, 31).intersects(rectangle));
		}

		@DisplayName("(Parallelogram2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Parallelogram2afp para = createParallelogram(6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertTrue(createTriangle(-5, 15, -3, 16, -8, 19).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Parallelogram2afp para = createParallelogram(6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertTrue(createTriangle(-5, 15, -8, 19, -3, 16).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Parallelogram2afp para = createParallelogram(6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertFalse(createTriangle(0, -5, 2, -4, -3, -1).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Parallelogram2afp para = createParallelogram(6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertFalse(createTriangle(0, -5, -3, -1, 2, -4).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Parallelogram2afp para = createParallelogram(6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertFalse(createTriangle(20, 0, 22, 1, 17, 4).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Parallelogram2afp para = createParallelogram(6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertFalse(createTriangle(20, 0, 17, 4, 22, 1).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Parallelogram2afp para = createParallelogram(6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertFalse(createTriangle(17.18034, 9, 19.18034, 10, 14.18034, 13).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Parallelogram2afp para = createParallelogram(6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertFalse(createTriangle(17.18034, 9, 14.18034, 13, 19.18034, 10).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Parallelogram2afp para = createParallelogram(6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertTrue(createTriangle(0, 10, 2, 11, -3, 14).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Parallelogram2afp para = createParallelogram(6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertTrue(createTriangle(0, 10, -3, 14, 2, 11).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Parallelogram2afp para = createParallelogram(6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertTrue(createTriangle(0, 20, 2, 21, -3, 24).intersects(para));
		}

		@DisplayName("(RoundRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createRoundRectangle(0, 0, 1, 1, .2, .4)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createRoundRectangle(0, 2, 1, 1, .2, .4)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createRoundRectangle(0, 3, 1, 1, .2, .4)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createRoundRectangle(0, 4, 1, 1, .2, .4)));
		}

		@DisplayName("(RoundRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createRoundRectangle(0, 5, 1, 1, .2, .4)));
		}

		@DisplayName("(RoundRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createRoundRectangle(0, 6, 1, 1, .2, .4)));
		}

		@DisplayName("(RoundRectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createRoundRectangle(0, 6.05, 1, 1, .2, .4)));
		}

		@DisplayName("(RoundRectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createRoundRectangle(0, 6.06, 1, 1, .2, .4)));
		}

		@DisplayName("(RoundRectangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createRoundRectangle(4.5, 8, 1, 1, .2, .4)));
		}

		@DisplayName("(Circe2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createCircle(5, 8, 1)));
		}

		@DisplayName("(Circe2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createCircle(-10, 1, 1)));
		}

		@DisplayName("(Circe2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createCircle(-1, -2, 1)));
		}

		@DisplayName("(Circe2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createCircle(2, 0, 1)));
		}

		@DisplayName("(Circe2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createCircle(1.9, 0, 1)));
		}

		@DisplayName("(Circe2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createCircle(1.8, 0, 1)));
		}

		@DisplayName("(Circe2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createCircle(1.7, 0, 1)));
		}

		@DisplayName("(Circe2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createCircle(1.6, 0, 1)));
		}

		@DisplayName("(Circe2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createCircle(1.5, 0, 1)));
		}

		@DisplayName("(Circe2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createCircle(1.4, 0, 1)));
		}

		@DisplayName("(Circe2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createCircle(1.3, 0, 1)));
		}

		@DisplayName("(Circe2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createCircle(5, 9, 1)));
		}

		@DisplayName("(Circe2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createCircle(5, 8.9, 1)));
		}

		@DisplayName("(Circe2afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createCircle(-4, 1, 1)));
		}

		@DisplayName("(Ellipse2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createEllipse(5, 8, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createEllipse(-10, 1, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createEllipse(-1, -2, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createEllipse(1, 0, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createEllipse(0.9, 0, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createEllipse(0.8, 0, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createEllipse(0.7, 0, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createEllipse(0.6, 0, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createEllipse(0.5, 0, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createEllipse(-1.12464, -2.86312, 2, 1)));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createSegment(-6, -2, -4, 0)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createSegment(-6, -2, 2, 0)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createSegment(-6, -2, 14, -4)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createSegment(-2, 2, 4, 0)));
		}

		@DisplayName("(Segment2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createSegment(-2, 2, 0, 0)));
		}

		@DisplayName("(Segment2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createSegment(-4, -2, -6, 6)));
		}

		@DisplayName("(Segment2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createSegment(6, 7, -6, 6)));
		}

		@DisplayName("(Segment2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects(createSegment(0, 5, -6, 6)));
		}

		@DisplayName("(Segment2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createSegment(-5, 5, -6, 6)));
		}

		@DisplayName("(Segment2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createSegment(-4, -2, 2, -2)));
		}

		@DisplayName("(Segment2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().intersects(createSegment(-1, -2, 5, 8)));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Path2afp path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(2, 2);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Path2afp path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(2, 2);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(0, 8);
			assertTrue(getS().intersects(path));
		}

		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(0, 8);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("(Path2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-18, 2);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(6, 10);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(Path2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-18, 2);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(6, 10);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(Path2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(2, 2);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(2, 2);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("(Path2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(0, 8);
			assertTrue(getS().intersects(path));
		}

		@DisplayName("(Path2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(0, 8);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("(Path2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-18, 2);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(6, 10);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-18, 2);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(6, 10);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("(PathIterator2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Path2afp path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(2, 2);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			Path2afp path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(2, 2);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(0, 8);
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(0, 8);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-18, 2);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(6, 10);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-18, 2);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(6, 10);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(2, 2);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(2, 2);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(0, 8);
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-8, 0);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(0, 8);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-18, 2);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(6, 10);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-18, 2);
			path.lineTo(-2, -2);
			path.lineTo(2, -2);
			path.lineTo(6, 10);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(Shape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects((Shape2D) createCircle(5, 8, 1)));
		}

		@DisplayName("(Shape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().intersects((Shape2D) createEllipse(-10, 1, 2, 1)));
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
			getS().operator_add(createVector(2, -3));
			assertEpsilonEquals(7, getS().getX1());
			assertEpsilonEquals(5, getS().getY1());
			assertEpsilonEquals(-8, getS().getX2());
			assertEpsilonEquals(-2, getS().getY2());
			assertEpsilonEquals(1, getS().getX3());
			assertEpsilonEquals(-5, getS().getY3());
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
			T shape = getS().operator_plus(createVector(2, -3));
			assertEpsilonEquals(7, shape.getX1());
			assertEpsilonEquals(5, shape.getY1());
			assertEpsilonEquals(-8, shape.getX2());
			assertEpsilonEquals(-2, shape.getY2());
			assertEpsilonEquals(1, shape.getX3());
			assertEpsilonEquals(-5, shape.getY3());
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
			getS().operator_remove(createVector(2, -3));
			assertEpsilonEquals(3, getS().getX1());
			assertEpsilonEquals(11, getS().getY1());
			assertEpsilonEquals(-12, getS().getX2());
			assertEpsilonEquals(4, getS().getY2());
			assertEpsilonEquals(-3, getS().getX3());
			assertEpsilonEquals(1, getS().getY3());
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
			T shape = getS().operator_minus(createVector(2, -3));
			assertEpsilonEquals(3, shape.getX1());
			assertEpsilonEquals(11, shape.getY1());
			assertEpsilonEquals(-12, shape.getX2());
			assertEpsilonEquals(4, shape.getY2());
			assertEpsilonEquals(-3, shape.getX3());
			assertEpsilonEquals(1, shape.getY3());
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
			var newShape = getS().operator_multiply(null);
			assertNotNull(newShape);
			assertNotSame(getS(), newShape);
			assertEquals(getS(), newShape);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var tr = new Transform2D();
			var newShape = getS().operator_multiply(tr);
			assertNotNull(newShape);
			assertNotSame(getS(), newShape);
			assertEquals(getS(), newShape);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			var tr = new Transform2D();
			tr.makeTranslationMatrix(10, -10);
			var newShape = getS().operator_multiply(tr);
			assertNotNull(newShape);
			assertNotSame(getS(), newShape);
			assertTrue(newShape instanceof Triangle2afp);
			PathIterator2afp pi = getS().getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 15, -2);
			assertElement(pi, PathElementType.LINE_TO, 0, -9);
			assertElement(pi, PathElementType.LINE_TO, 9, -12);
			assertElement(pi, PathElementType.CLOSE, 15, -2);
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
			assertTrue(getS().operator_and(createPoint(0,0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().operator_and(createPoint(11,10)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().operator_and(createPoint(11,50)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().operator_and(createPoint(9,12)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().operator_and(createPoint(9,11)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().operator_and(createPoint(0,6)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().operator_and(createPoint(8,12)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().operator_and(createPoint(3,7)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().operator_and(createPoint(10,11)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().operator_and(createPoint(9,10)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().operator_and(createPoint(-4,2)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertFalse(getS().operator_and(createPoint(-8,-5)));
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
			assertTrue(getS().operator_and(createCircle(5, 8, 1)));
		}
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertTrue(getS().operator_and(createEllipse(-10, 1, 2, 1)));
		}
	}

	@DisplayName("this .. Point2D")
	@Nested
	public class OperatorupToPoint2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(5.65685, getS().operator_upTo(createPoint(9, 12)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0.30206, getS().operator_upTo(createPoint(0, 6)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(10, getS().operator_upTo(createPoint(-20, 1)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(0.63246, getS().operator_upTo(createPoint(-6, -1)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(4, getS().operator_upTo(createPoint(-1, -6)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			assertEpsilonEquals(3.94446, getS().operator_upTo(createPoint(6, 2)));
		}
	}

}