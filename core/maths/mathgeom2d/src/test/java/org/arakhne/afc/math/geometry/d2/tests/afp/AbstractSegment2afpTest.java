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

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.base.GeomConstants;
import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp.UncertainIntersection;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractSegment2afpTest<T extends Segment2afp<?, T, ?, ?, ?, B>,
B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractShape2afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createSegment(0, 0, 1, 1);
	}

	
	protected Triangle2afp createTestTriangle(double x, double y) {
		return createTriangle(x, y, x + 1, y, x, y + 0.5);
	}
	
	protected MultiShape2afp createTestMultiShape(double x, double y) {
		MultiShape2afp multishape = createMultiShape();
		multishape.add(createCircle(x - 5, y + 4, 1));
		multishape.add(createSegment(x + 4, y + 2, x + 8, y - 1));
		return multishape;
	}

	protected Path2afp createTestPath(double dx, double dy, boolean close) {
		Path2afp path = createPath();
		path.moveTo(dx + 3, dy);
		path.lineTo(dx + 1, dy + 3);
		path.lineTo(dx - 2, dy + 1);
		path.lineTo(dx - 1, dy - 1);
		if (close) {
			path.closePath();
		}
		return path;
	}
	
	protected Path2afp createTestPath(double dx, double dy, boolean close, PathWindingRule rule) {
		Path2afp path = createPath(rule);
		path.moveTo(dx + 3, dy - 1);
		path.lineTo(dx + 1, dy + 2);
		path.lineTo(dx - 1, dy + 1);
		path.lineTo(dx, dy - 1);
		path.lineTo(dx + 3, dy + 2);
		path.lineTo(dx, dy + 3);
		path.lineTo(dx - 2, dy + 1);
		path.lineTo(dx - 1, dy - 2);
		if (close) {
			path.closePath();
		}
		return path;
	}

	@DisplayName("ccw")
	@Nested
	public class CCW {

		private double x1;
		private double y1;
		private double x2;
		private double y2;

		@BeforeEach
		public void setUp() {
			this.x1 = -100;
			this.y1 = -100;
			this.x2 = 100;
			this.y2 = 100;
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.ccw(x1, y1, x2, y2, x1, y1, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.ccw(x2, y2, x1, y1, x1, y1, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.ccw(x1, y1, x2, y2, x2, y2, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.ccw(x2, y2, x1, y1, x2, y2, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.ccw(x1, y1, x2, y2, 0, 0, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.ccw(x2, y2, x1, y1, 0, 0, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Segment2afp.ccw(x1, y1, x2, y2, -200, -200, 0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2afp.ccw(x2, y2, x1, y1, -200, -200, 0));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2afp.ccw(x1, y1, x2, y2, 200, 200, 0));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Segment2afp.ccw(x2, y2, x1, y1, 200, 200, 0));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Segment2afp.ccw(x1, y1, x2, y2, -200, 200, 0));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2afp.ccw(x2, y2, x1, y1, -200, 200, 0));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2afp.ccw(x1, y1, x2, y2, 200, -200, 0));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Segment2afp.ccw(x2, y2, x1, y1, 200, -200, 0));
		}

	}

	@DisplayName("findsClosestPointSegmentPoint")
	@Nested
	public class FindsClosestPointSegmentPoint {

		private Point2D result;
		
		@BeforeEach
		public void seUp() {
			this.result = createPoint(Double.NaN, Double.NaN);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsClosestPointSegmentPoint(0, 0, 1, 1, 0, 0, result);
			assertEpsilonEquals(0, result.getX());
			assertEpsilonEquals(0, result.getY());
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsClosestPointSegmentPoint(0, 0, 1, 1, .75, .75, result);
			assertEpsilonEquals(.75, result.getX());
			assertEpsilonEquals(.75, result.getY());
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsClosestPointSegmentPoint(0, 0, 1, 1, -10, -50, result);
			assertEpsilonEquals(0, result.getX());
			assertEpsilonEquals(0, result.getY());
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsClosestPointSegmentPoint(0, 0, 1, 1, 200, -50, result);
			assertEpsilonEquals(1, result.getX());
			assertEpsilonEquals(1, result.getY());
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsClosestPointSegmentPoint(0, 0, 1, 1, 0, 1, result);
			assertEpsilonEquals(.5, result.getX());
			assertEpsilonEquals(.5, result.getY());
		}

	}

	@DisplayName("findsClosestPointSegmentSegment")
	@Nested
	public class FindsClosestPointSegmentSegment {

		private Point2D result;
		
		private double dist;

		@BeforeEach
		public void seUp() {
			this.result = createPoint(Double.NaN, Double.NaN);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, -2, 3, -1, 1, result);
			assertEpsilonEquals(0, result.getX());
			assertEpsilonEquals(0, result.getY());
			assertEpsilonEquals(2, dist);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, -1, 1, -3, 2, result);
			assertEpsilonEquals(0, result.getX());
			assertEpsilonEquals(0, result.getY());
			assertEpsilonEquals(2, dist);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, 1, 0, -1, 0, result);
			assertEpsilonEquals(0, result.getX());
			assertEpsilonEquals(0, result.getY());
			assertEpsilonEquals(0, dist);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, -2, 0, 2, 1, result);
			assertEpsilonEquals(0.66667, result.getX());
			assertEpsilonEquals(0.66667, result.getY());
			assertEpsilonEquals(0, dist);
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, 0, -1, -3, 0, result);
			assertEpsilonEquals(0, result.getX());
			assertEpsilonEquals(0, result.getY());
			assertEpsilonEquals(.9, dist);
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, 0, 4, 3, 3, result);
			assertEpsilonEquals(1, result.getX());
			assertEpsilonEquals(1, result.getY());
			assertEpsilonEquals(6.4, dist);
		}
		
		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, 1, 0, 4, 1, result);
			assertEpsilonEquals(.5, result.getX());
			assertEpsilonEquals(.5, result.getY());
			assertEpsilonEquals(.5, dist);
		}
		
		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, 1, 0, 3, -1, result);
			assertEpsilonEquals(.5, result.getX());
			assertEpsilonEquals(.5, result.getY());
			assertEpsilonEquals(.5, dist);
		}
		
		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, 2, 0, 1, -1, result);
			assertEpsilonEquals(0, result.getX());
			assertEpsilonEquals(0, result.getY());
			assertEpsilonEquals(2, dist);
		}
		
		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
			dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, 3, -1, 1, .4, result);
			assertEpsilonEquals(.7, result.getX());
			assertEpsilonEquals(.7, result.getY());
			assertEpsilonEquals(.18, dist);
		}

	}

	@DisplayName("findsFarthestPointSegmentSegment")
	@Nested
	public class FindsFarthestPointSegmentSegment {

		private Point2D result;
		
		private double dist;

		@BeforeEach
		public void seUp() {
			this.result = createPoint(Double.NaN, Double.NaN);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, -2, 3, -1, 1, result);
			assertEpsilonEquals(1, result.getX());
			assertEpsilonEquals(1, result.getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, -1, 1, -3, 2, result);
			assertEpsilonEquals(1, result.getX());
			assertEpsilonEquals(1, result.getY());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, 1, 0, -1, 0, result);
			assertEpsilonEquals(1, result.getX());
			assertEpsilonEquals(1, result.getY());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, -2, 0, 2, 1, result);
			assertEpsilonEquals(0, result.getX());
			assertEpsilonEquals(0, result.getY());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, 0, -1, -3, 0, result);
			assertEpsilonEquals(1, result.getX());
			assertEpsilonEquals(1, result.getY());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, 0, 4, 3, 3, result);
			assertEpsilonEquals(0, result.getX());
			assertEpsilonEquals(0, result.getY());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, 1, 0, 4, 1, result);
			assertEpsilonEquals(1, result.getX());
			assertEpsilonEquals(1, result.getY());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, 1, 0, 3, -1, result);
			assertEpsilonEquals(1, result.getX());
			assertEpsilonEquals(1, result.getY());
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, 2, 0, 1, -1, result);
			assertEpsilonEquals(1, result.getX());
			assertEpsilonEquals(1, result.getY());
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, 3, -1, 1, .4, result);
			assertEpsilonEquals(0, result.getX());
			assertEpsilonEquals(0, result.getY());
		}

	}

	@DisplayName("calculatesCrossingsCircleShadowSegment")
	@Nested
	public class CalculatesCrossingsCircleShadowSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					5, -4, -1, -5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					-7, -3, -5, -1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					11, -2, 10, -1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					3, 5, 1, 6));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					5, .5, 6, -1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					5, 2, 6, -1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					5, 2, 6, .5));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(GeomConstants.SHAPE_INTERSECTS, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					.5, .5, 3, 0));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(GeomConstants.SHAPE_INTERSECTS, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					0, 2, 3, 0));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(GeomConstants.SHAPE_INTERSECTS, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					.5, 4, .5, -1));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					1, 3, 3, 0));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					-1, -5, 5, -4));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					-5, -1, -7, 3));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					10, -1, 11, -2));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					1, 6, 3, 5));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					6, -1, 5, .5));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(2, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					6, -1, 5, 2));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					6, .5, 5, 2));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(GeomConstants.SHAPE_INTERSECTS, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					3, 0, .5, .5));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(GeomConstants.SHAPE_INTERSECTS, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					3, 0, 0, 2));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(GeomConstants.SHAPE_INTERSECTS, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					.5, -1, .5, 4));
		}

		@DisplayName("#22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(2, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					1, 1, 1,
					3, 0, 1, 3));
		}

		@DisplayName("#23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Segment2afp.calculatesCrossingsCircleShadowSegment(
					0,
					0, 2, 1,
					-5.18034, 9, 12.7082, -8.88854));
		}

	}

	@DisplayName("calculatesCrossingsEllipseShadowSegment")
	@Nested
	public class CalculatesCrossingsEllipseShadowSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							5, -4, -1, -5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							-7, 3, -5, -1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							11, -2, 10, -1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							3, 5, 1, 6));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-1,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							5, .5, 6, -1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-2,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							5, 2, 6, -1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-1,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							5, 2, 6, .5));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							.5, .5, 3, 0));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							0, 1, 3, 0));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							0, 1, 3, 0));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							.5, 2, .5, -1));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-2,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 1, 1,
							1, 1, 7, -5));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							4, -3, 1, 2,
							1, 1, 7, -5));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-2,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							4, -3, 1, 2,
							4.2, 0, 7, -5));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							-1, -5, 5, -4));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							-5, -1, -7, 3));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							10, -1, 11, -2));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							1, 6, 3, 5));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							6, -1, 5, .5));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					2,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							6, -1, 5, 2));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							6, .5, 5, 2));
		}

		@DisplayName("#22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							3, 0, .5, .5));
		}

		@DisplayName("#23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							3, 0, 0, 1));
		}

		@DisplayName("#24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							3, 0, 0, 1));
		}

		@DisplayName("#25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_25(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 2, 1,
							.5, -1, .5, 2));
		}

		@DisplayName("#26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_26(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					2,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							0, 0, 1, 1,
							7, -5, 1, 1));
		}

		@DisplayName("#27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_27(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							4, -3, 1, 2,
							7, -5, 1, 1));
		}

		@DisplayName("#28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_28(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							4, -3, 1, 2,
							7, -5, 4, 0));
		}

		@DisplayName("#29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_29(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					2,
					Segment2afp.calculatesCrossingsEllipseShadowSegment(
							0,
							4, -3, 1, 2,
							7, -5, 4.2, 0));
		}

	}

	@DisplayName("calculatesCrossingsPointShadowSegment")
	@Nested
	public class CalculatesCrossingsPointShadowSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsPointShadowSegment(
							0, 0,
							10, -1, 10, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsPointShadowSegment(
							0, 0,
							10, -1, 10, -.5));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsPointShadowSegment(
							0, 0,
							-10, -1, -10, 1));
		}

	}

	@DisplayName("calculatesCrossingsPointShadowSegmentWithoutEquality")
	@Nested
	public class CalculatesCrossingsPointShadowSegmentWithoutEquality {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsPointShadowSegmentWithoutEquality(
							0, 0,
							10, -1, 10, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsPointShadowSegmentWithoutEquality(
							0, 0,
							10, -1, 10, -.5));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsPointShadowSegmentWithoutEquality(
							0, 0,
							-10, -1, -10, 1));
		}

	}

	@DisplayName("calculatesCrossingsRectangleShadowSegment")
	@Nested
	public class CalculatesCrossingsRectangleShadowSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					2,
					Segment2afp.calculatesCrossingsRectangleShadowSegment(
							0,
							0, 0, 1, 1,
							10, -5, 10, 5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsRectangleShadowSegment(
							0,
							0, 0, 1, 1,
							10, -5, 10, .5));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsRectangleShadowSegment(
							0,
							0, 0, 1, 1,
							10, -5, 10, -1));
		}

	}

	@DisplayName("calculatesCrossingsTriangleShadowSegment")
	@Nested
	public class CalculatesCrossingsTriangleShadowSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsTriangleShadowSegment(
							0,
							5, 8, -10, 1, -1, -2,
							10, -5, 10, 5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-1,
					Segment2afp.calculatesCrossingsTriangleShadowSegment(
							0,
							5, 8, -10, 1, -1, -2,
							10, 5, 10, -5));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-1,
					Segment2afp.calculatesCrossingsTriangleShadowSegment(
							0,
							5, 8, -10, 1, -1, -2,
							10, 5, 0, -4));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsTriangleShadowSegment(
							0,
							5, 8, -10, 1, -1, -2,
							0, -4, 10, 5));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					2,
					Segment2afp.calculatesCrossingsTriangleShadowSegment(
							0,
							5, 8, -10, 1, -1, -2,
							0, -4, 8, 10));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsTriangleShadowSegment(
							0,
							5, 8, -10, 1, -1, -2,
							-20, 0, 8, 10));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsTriangleShadowSegment(
							0,
							5, 8, -10, 1, -1, -2,
							8, 10, -8, 0));
		}

	}

	@DisplayName("calculatesCrossingsRoundRectangleShadowSegment")
	@Nested
	public class CalculatesCrossingsRoundRectangleShadowSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					2,
					Segment2afp.calculatesCrossingsRoundRectangleShadowSegment(
							0,
							0, 0, 1, 1, .1, .2,
							10, -5, 10, 5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsRoundRectangleShadowSegment(
							0,
							0, 0, 1, 1, .1, .2,
							10, -5, 10, .5));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsRoundRectangleShadowSegment(
							0,
							0, 0, 1, 1, .1, .2,
							10, -5, 10, -1));
		}

	}

	@DisplayName("calculatesCrossingsSegmentShadowSegment")
	@Nested
	public class CalculatesCrossingsSegmentShadowSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							10, -5, 10, -4));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							10, 5, 10, 4));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							-5, .5, 0, .6));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							10, -1, 11, .6));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					2,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							10, -1, 11, 2));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							10, .5, 11, 2));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							10, 2, 11, .6));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-2,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							10, 2, 11, -1));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							10, .6, 11, -1));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							0, .5, .25, .5));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							.75, .5, 1, .5));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							5, -5, .75, .5));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							5, -5, 0, 1));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					2,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							5, -5, 1, 1));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 0, 1, 1,
							-2, 1, 5, -5));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							10, -5, 10, -4));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							10, 5, 10, 4));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							-5, .5, 0, .6));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							10, -1, 11, .6));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					2,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							10, -1, 11, 2));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							10, .5, 11, 2));
		}

		@DisplayName("#22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							10, 2, 11, .6));
		}

		@DisplayName("#23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-2,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							10, 2, 11, -1));
		}

		@DisplayName("#24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							10, .6, 11, -1));
		}

		@DisplayName("#25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_25(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							0, .5, .25, .5));
		}

		@DisplayName("#26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_26(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							.75, .5, 1, .5));
		}

		@DisplayName("#27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_27(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							5, -5, .75, .5));
		}

		@DisplayName("#28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_28(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							5, -5, 0, 1));
		}

		@DisplayName("#29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_29(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					2,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							5, -5, 1, 1));
		}

		@DisplayName("#30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_30(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 0, 0,
							-2, 1, 5, -5));
		}

		@DisplayName("#31")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_31(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 1, 1, 0,
							10, -5, 10, -4));
		}

		@DisplayName("#32")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_32(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 1, 1, 0,
							10, 5, 10, 4));
		}

		@DisplayName("#33")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_33(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 1, 1, 0,
							-5, .5, 0, .6));
		}

		@DisplayName("#34")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_34(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 1, 1, 0,
							10, -1, 11, .6));
		}

		@DisplayName("#35")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_35(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					2,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 1, 1, 0,
							10, -1, 11, 2));
		}

		@DisplayName("#36")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_36(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 1, 1, 0,
							10, .5, 11, 2));
		}

		@DisplayName("#37")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_37(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 1, 1, 0,
							10, 2, 11, .6));
		}

		@DisplayName("#38")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_38(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-2,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 1, 1, 0,
							10, 2, 11, -1));
		}

		@DisplayName("#39")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_39(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 1, 1, 0,
							10, .6, 11, -1));
		}

		@DisplayName("#40")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_40(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 1, 1, 0,
							0, .5, .25, .5));
		}

		@DisplayName("#41")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_41(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 1, 1, 0,
							.75, .5, 1, .5));
		}

		@DisplayName("#42")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_42(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 1, 1, 0,
							5, -.01, .75, .5));
		}

		@DisplayName("#43")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_43(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 1, 1, 0,
							20, -5, -1, 1));
		}

		@DisplayName("#44")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_44(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							0, 1, 1, 0,
							5, 10, .25, .5));
		}

		@DisplayName("#45")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_45(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 0, 0, 1,
							10, -5, 10, -4));
		}

		@DisplayName("#46")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_46(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 0, 0, 1,
							10, 5, 10, 4));
		}

		@DisplayName("#47")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_47(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 0, 0, 1,
							-5, .5, 0, .6));
		}

		@DisplayName("#48")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_48(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 0, 0, 1,
							10, -1, 11, .6));
		}

		@DisplayName("#49")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_49(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					2,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 0, 0, 1,
							10, -1, 11, 2));
		}

		@DisplayName("#50")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_50(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 0, 0, 1,
							10, .5, 11, 2));
		}

		@DisplayName("#51")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_51(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 0, 0, 1,
							10, 2, 11, .6));
		}

		@DisplayName("#52")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_52(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-2,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 0, 0, 1,
							10, 2, 11, -1));
		}

		@DisplayName("#53")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_53(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					-1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 0, 0, 1,
							10, .6, 11, -1));
		}

		@DisplayName("#54")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_54(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 0, 0, 1,
							0, .5, .25, .5));
		}

		@DisplayName("#55")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_55(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					0,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 0, 0, 1,
							.75, .5, 1, .5));
		}

		@DisplayName("#56")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_56(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					1,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 0, 0, 1,
							20, -5, .75, .5));
		}

		@DisplayName("#57")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_57(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 0, 0, 1,
							20, -5, 0, 1));
		}

		@DisplayName("#58")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_58(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 0, 0, 1,
							5, 10, .25, .5));
		}

		@DisplayName("#59")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_59(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							7, -5, 1, 1,
							4, -3, 1, 1));
		}

		@DisplayName("#60")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_60(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							4, -3, 1, 1,
							7, -5, 1, 1));
		}

		@DisplayName("#61")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_61(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 4, -3,
							7, -5, 1, 1));
		}

		@DisplayName("#62")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_62(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							4, -3, 1, 1,
							1, 1, 7, -5));
		}

		@DisplayName("#63")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_63(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(
					GeomConstants.SHAPE_INTERSECTS,
					Segment2afp.calculatesCrossingsSegmentShadowSegment(
							0,
							1, 1, 4, -3,
							1, 1, 7, -5));
		}

	}

	@DisplayName("findsFarthestPointSegmentPoint")
	@Nested
	public class FindsFarthestPointSegmentPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(Double.NaN, Double.NaN);
			Segment2afp.findsFarthestPointSegmentPoint(0, 0, 1, 1, 0, 0, p);
			assertEpsilonEquals(1, p.getX());
			assertEpsilonEquals(1, p.getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(Double.NaN, Double.NaN);
			Segment2afp.findsFarthestPointSegmentPoint(0, 0, 1, 1, .5, .5, p);
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(Double.NaN, Double.NaN);
			Segment2afp.findsFarthestPointSegmentPoint(0, 0, 1, 1, 1, 1, p);
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(Double.NaN, Double.NaN);
			Segment2afp.findsFarthestPointSegmentPoint(0, 0, 1, 1, 2, 2, p);
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(Double.NaN, Double.NaN);
			Segment2afp.findsFarthestPointSegmentPoint(0, 0, 1, 1, -2, 2, p);
			assertEpsilonEquals(1, p.getX());
			assertEpsilonEquals(1, p.getY());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(Double.NaN, Double.NaN);
			Segment2afp.findsFarthestPointSegmentPoint(0, 0, 1, 1, 0.1, 1.2, p);
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(Double.NaN, Double.NaN);
			Segment2afp.findsFarthestPointSegmentPoint(0, 0, 1, 1, 10.1, -.2, p);
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
		}

	}

	@DisplayName("findsLineLineIntersection")
	@Nested
	public class FindsLineLineIntersection {

		private Point2D result;

		@BeforeEach
		public void setUp() {
			result = createPoint(Double.NaN, Double.NaN);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.findsLineLineIntersection(
					1000, 1.5325000286102295, 2500, 1.5325000286102295,
					1184.001080023255, 1.6651813832907332, 1200.7014393876193, 1.372326130924099,
					result));
			assertEpsilonEquals(1191.567365026541, result.getX());
			assertEpsilonEquals(1.532500028610229, result.getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.findsLineLineIntersection(
					100, 50, 100, 60,
					90, 55, 2000, 55,
					result));
			assertEpsilonEquals(100, result.getX());
			assertEpsilonEquals(55, result.getY());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.findsLineLineIntersection(
					100, 50, 100, 60,
					200, 0, 200, 10,
					result));
			assertNaN(result.getX());
			assertNaN(result.getY());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.findsLineLineIntersection(
					100, -50, 100, -60,
					90, 55, 2000, 55,
					result));
			assertEpsilonEquals(100, result.getX());
			assertEpsilonEquals(55, result.getY());
		}

	}

	@DisplayName("findsIntersectionLineLineFactor")
	@Nested
	public class FindsIntersectionLineLineFactor {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.1277115766843605, Segment2afp.findsIntersectionLineLineFactor(
					1000, 1.5325000286102295, 2500, 1.5325000286102295,
					1184.001080023255, 1.6651813832907332, 1200.7014393876193, 1.372326130924099));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.findsIntersectionLineLineFactor(
					100, 50, 100, 60,
					90, 55, 2000, 55));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertNaN(Segment2afp.findsIntersectionLineLineFactor(
					100, 50, 100, 60,
					200, 0, 200, 10));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-10.5, Segment2afp.findsIntersectionLineLineFactor(
					100, -50, 100, -60,
					90, 55, 2000, 55));
		}

	}

	@DisplayName("findsProjectedPointPointLine")
	@Nested
	public class FindsProjectedPointPointLine {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.3076923076923077, Segment2afp.findsProjectedPointPointLine(
					2, 1,
					0, 0, 3, -2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.6666666666666666, Segment2afp.findsProjectedPointPointLine(
					2, 1,
					0, 0, 3, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-.7, Segment2afp.findsProjectedPointPointLine(
					2, -1,
					0, 0, -3, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(14.4, Segment2afp.findsProjectedPointPointLine(
					2, 150,
					0, 0, -3, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.findsProjectedPointPointLine(
					.5, .5,
					0, 0, 1, 1));
		}

	}

	@DisplayName("calculatesRelativeDistanceLinePoint")
	@Nested
	public class CalculatesRelativeDistanceLinePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-1.941450686788302, Segment2afp.calculatesRelativeDistanceLinePoint(
					0, 0, 3, -2,
					2, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.941450686788302, Segment2afp.calculatesRelativeDistanceLinePoint(
					3, -2, 0, 0,
					2, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-1, Segment2afp.calculatesRelativeDistanceLinePoint(
					0, 0, 3, 0,
					2, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment2afp.calculatesRelativeDistanceLinePoint(
					3, 0, 0, 0,
					2, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-.3162277660168379, Segment2afp.calculatesRelativeDistanceLinePoint(
					0, 0, -3, 1,
					2, -1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.3162277660168379, Segment2afp.calculatesRelativeDistanceLinePoint(
					-3, 1, 0, 0,
					2, -1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(142.9349502396107, Segment2afp.calculatesRelativeDistanceLinePoint(
					0, 0, -3, 1,
					2, 150));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-142.9349502396107, Segment2afp.calculatesRelativeDistanceLinePoint(
					-3, 1, 0, 0,
					2, 150));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2afp.calculatesRelativeDistanceLinePoint(
					0, 0, 1, 1,
					.5, .5));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2afp.calculatesRelativeDistanceLinePoint(
					1, 1, 0, 0,
					.5, .5));
		}

	}

	@DisplayName("findsSegmentSegmentIntersection")
	@Nested
	public class FindsSegmentSegmentIntersection {

		private Point2D result;

		@BeforeEach
		public void setUp() {
			result = createPoint(Double.NaN, Double.NaN);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.findsSegmentSegmentIntersection(
					1000, 1.5325000286102295, 2500, 1.5325000286102295,
					1184.001080023255, 1.6651813832907332, 1200.7014393876193, 1.372326130924099,
					result));
			assertEpsilonEquals(1191.567365026541, result.getX());
			assertEpsilonEquals(1.532500028610229, result.getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.findsSegmentSegmentIntersection(
					100, 50, 100, 60,
					200, 0, 200, 10,
					result));
			assertNaN(result.getX());
			assertNaN(result.getY());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.findsSegmentSegmentIntersection(
					-100, 50, -100, 60,
					90, 55, 2000, 55,
					result));
			assertNaN(result.getX());
			assertNaN(result.getY());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.findsSegmentSegmentIntersection(
					100, 50, 100, 60,
					90, 55, 2000, 55,
					result));
			assertEpsilonEquals(100, result.getX());
			assertEpsilonEquals(55, result.getY());
		}

	}

	@DisplayName("findsIntersectionSegmentSegmentFactor")
	@Nested
	public class FindsIntersectionSegmentSegmentFactor {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.1277115766843605, Segment2afp.findsIntersectionSegmentSegmentFactor(
					1000, 1.5325000286102295, 2500, 1.5325000286102295,
					1184.001080023255, 1.6651813832907332, 1200.7014393876193, 1.372326130924099));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.findsIntersectionSegmentSegmentFactor(
					100, 50, 100, 60,
					90, 55, 2000, 55));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertNaN(Segment2afp.findsIntersectionSegmentSegmentFactor(
					100, 50, 100, 60,
					200, 0, 200, 10));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertNaN(Segment2afp.findsIntersectionSegmentSegmentFactor(
					100, -50, 100, -60,
					90, 55, 2000, 55));
		}

	}

	@DisplayName("findsSideLinePoint")
	@Nested
	public class FindsSideLinePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.findsSideLinePoint(0, 0, 1, 1, 0, 0, 0.1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.findsSideLinePoint(0, 0, 1, 1, 1, 1, 0.1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.findsSideLinePoint(0, 0, 1, 1, .25, .25, 0.1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2afp.findsSideLinePoint(0, 0, 1, 1, 0.2, 0, 0.1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2afp.findsSideLinePoint(0, 0, 1, 1, 120, 0, 0.1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2afp.findsSideLinePoint(0, 0, 1, 1, -20.05, -20, 0.1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Segment2afp.findsSideLinePoint(0, 0, 1, 1, 0, 0.2, 0.1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Segment2afp.findsSideLinePoint(0, 0, 1, 1, 0, 120, 0.1));
		}

	}

	@DisplayName("findsUncertainIntersectionSegmentSegmentWithEnds")
	@Nested
	public class FindsUncertainIntersectionSegmentSegmentWithEnds {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
					0, 0, 1, 1,
					0, .5, 1, .5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
					0, 0, 1, 1,
					0, 0, 1, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
					0, 0, 1, 1,
					0, 0, 2, 2));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
					0, 0, 1, 1,
					0, 0, .5, .5));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
					0, 0, 1, 1,
					-3, -3, .5, .5));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
					0, 0, 1, 1,
					-3, -3, 0, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
					0, 0, 1, 1,
					-3, -3, -1, -1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
					0, 0, 1, 1,
					-3, 0, 4, 0));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
					0, 0, 1, 1,
					-3, -1, 4, -1));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
					0, 0, 1, 1,
					-3, -1, -1, -1));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
					0, 0, 1, 1,
					-3, 0, -2, 1));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
					0, 0, 1, 1,
					10, 0, 9, -1));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
					7, -5, 1, 1,
					4, -3, 1, 1));
		}

	}

	@DisplayName("findsUncertainIntersectionSegmentSegmentWithoutEnds")
	@Nested
	public class FindsUncertainIntersectionSegmentSegmentWithoutEnds {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					0, .5, 1, .5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					0, 0, 1, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
					0, 0, 1, 1,
					0, 0, 2, 2));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					0, 0, .5, .5));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					-3, -3, .5, .5));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					-3, -3, 0, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					-3, -3, -1, -1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					-3, 0, 4, 0));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					-3, -1, 4, -1));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					-3, -1, -1, -1));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					-3, 0, -2, 1));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					10, 0, 9, -1));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
					7, -5, 1, 1,
					4, -3, 1, 1));
		}

	}

	@DisplayName("calculatesDistanceLinePoint")
	@Nested
	public class CalculatesDistanceLinePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.941450686788302, Segment2afp.calculatesDistanceLinePoint(
					0, 0, 3, -2,
					2, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.941450686788302, Segment2afp.calculatesDistanceLinePoint(
					3, -2, 0, 0,
					2, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment2afp.calculatesDistanceLinePoint(
					0, 0, 3, 0,
					2, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment2afp.calculatesDistanceLinePoint(
					3, 0, 0, 0,
					2, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.3162277660168379, Segment2afp.calculatesDistanceLinePoint(
					0, 0, -3, 1,
					2, -1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.3162277660168379, Segment2afp.calculatesDistanceLinePoint(
					-3, 1, 0, 0,
					2, -1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(142.9349502396107, Segment2afp.calculatesDistanceLinePoint(
					0, 0, -3, 1,
					2, 150));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(142.9349502396107, Segment2afp.calculatesDistanceLinePoint(
					-3, 1, 0, 0,
					2, 150));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2afp.calculatesDistanceLinePoint(
					0, 0, 1, 1,
					.5, .5));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2afp.calculatesDistanceLinePoint(
					1, 1, 0, 0,
					.5, .5));
		}

	}

	@DisplayName("calculatesDistanceSegmentPoint")
	@Nested
	public class CalculatesDistanceSegmentPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.941450686788302, Segment2afp.calculatesDistanceSegmentPoint(
					0, 0, 3, -2,
					2, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.941450686788302, Segment2afp.calculatesDistanceSegmentPoint(
					3, -2, 0, 0,
					2, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment2afp.calculatesDistanceSegmentPoint(
					0, 0, 3, 0,
					2, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment2afp.calculatesDistanceSegmentPoint(
					3, 0, 0, 0,
					2, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.23606797749979, Segment2afp.calculatesDistanceSegmentPoint(
					0, 0, -3, 1,
					2, -1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.23606797749979, Segment2afp.calculatesDistanceSegmentPoint(
					-3, 1, 0, 0,
					2, -1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(149.0838690133845, Segment2afp.calculatesDistanceSegmentPoint(
					0, 0, -3, 1,
					2, 150));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(149.0838690133845, Segment2afp.calculatesDistanceSegmentPoint(
					-3, 1, 0, 0,
					2, 150));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2afp.calculatesDistanceSegmentPoint(
					0, 0, 1, 1,
					.5, .5));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2afp.calculatesDistanceSegmentPoint(
					1, 1, 0, 0,
					.5, .5));
		}

	}

	@DisplayName("calculatesDistanceSquaredLinePoint")
	@Nested
	public class CalculatesDistanceSquaredLinePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.769230769230769, Segment2afp.calculatesDistanceSquaredLinePoint(
					0, 0, 3, -2,
					2, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.769230769230769, Segment2afp.calculatesDistanceSquaredLinePoint(
					3, -2, 0, 0,
					2, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment2afp.calculatesDistanceSquaredLinePoint(
					0, 0, 3, 0,
					2, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment2afp.calculatesDistanceSquaredLinePoint(
					3, 0, 0, 0,
					2, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.09999999999999996, Segment2afp.calculatesDistanceSquaredLinePoint(
					0, 0, -3, 1,
					2, -1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.09999999999999996, Segment2afp.calculatesDistanceSquaredLinePoint(
					-3, 1, 0, 0,
					2, -1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(20430.39999999979, Segment2afp.calculatesDistanceSquaredLinePoint(
					0, 0, -3, 1,
					2, 150));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(20430.39999999979, Segment2afp.calculatesDistanceSquaredLinePoint(
					-3, 1, 0, 0,
					2, 150));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredLinePoint(
					0, 0, 1, 1,
					.5, .5));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredLinePoint(
					1, 1, 0, 0,
					.5, .5));
		}

	}

	@DisplayName("calculatesDistanceSquaredSegmentPoint")
	@Nested
	public class CalculatesDistanceSquaredSegmentPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.769230769230769, Segment2afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 3, -2,
					2, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.769230769230769, Segment2afp.calculatesDistanceSquaredSegmentPoint(
					3, -2, 0, 0,
					2, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment2afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 3, 0,
					2, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment2afp.calculatesDistanceSquaredSegmentPoint(
					3, 0, 0, 0,
					2, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, Segment2afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, -3, 1,
					2, -1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, Segment2afp.calculatesDistanceSquaredSegmentPoint(
					-3, 1, 0, 0,
					2, -1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(22225.99999999998, Segment2afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, -3, 1,
					2, 150));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(22225.99999999998, Segment2afp.calculatesDistanceSquaredSegmentPoint(
					-3, 1, 0, 0,
					2, 150));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 1, 1,
					.5, .5));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredSegmentPoint(
					1, 1, 0, 0,
					.5, .5));
		}

	}

	@DisplayName("calculatesDistanceSquaredSegmentSegment")
	@Nested
	public class CalculatesDistanceSquaredSegmentSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -2, 3, -1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -1, 1, -3, 2));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.9, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 0, -1, -3, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.4, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 0, 4, 3, 3));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 1, 0, 4, 1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 1, 0, 3, -1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.18, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 3, -1, 1, .4));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -2, 0, 2, 1));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 2, 0, 1, -1));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -4, 0, -5, -1));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 0, 1, -1, 0));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -1, 1, -2, 3));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -3, 2, -1, 1));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.9, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -3, 0, 0, -1));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.4, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 3, 3, 0, 4));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 4, 1, 1, 0));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 3, -1, 1, 0));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.18, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 1, .4, 3, -1));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 2, 1, -2, 0));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 1, -1, 2, 0));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -5, -1, -4, 0));
		}

		@DisplayName("#22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -1, 0, 0, 1));
		}

		@DisplayName("#23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -1, 1, -2, 3));
		}

		@DisplayName("#24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -3, 2, -1, 1));
		}

		@DisplayName("#25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_25(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.9, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -3, 0, 0, -1));
		}

		@DisplayName("#26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_26(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.4, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 3, 3, 0, 4));
		}

		@DisplayName("#27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_27(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 4, 1, 1, 0));
		}

		@DisplayName("#28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_28(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 3, -1, 1, 0));
		}

		@DisplayName("#29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_29(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.18, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 1, .4, 3, -1));
		}

		@DisplayName("#30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_30(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 2, 1, -2, 0));
		}

		@DisplayName("#31")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_31(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 1, -1, 2, 0));
		}

		@DisplayName("#32")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_32(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -5, -1, -4, 0));
		}

		@DisplayName("#33")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_33(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -1, 0, 0, 1));
		}

		@DisplayName("#34")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_34(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -2, 3, -1, 1));
		}

		@DisplayName("#35")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_35(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -1, 1, -3, 2));
		}

		@DisplayName("#36")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_36(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.9, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 0, -1, -3, 0));
		}

		@DisplayName("#37")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_37(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.4, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 0, 4, 3, 3));
		}

		@DisplayName("#38")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_38(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 1, 0, 4, 1));
		}

		@DisplayName("#39")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_39(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 1, 0, 3, -1));
		}

		@DisplayName("#40")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_40(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.18, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 3, -1, 1, .4));
		}

		@DisplayName("#41")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_41(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -2, 0, 2, 1));
		}

		@DisplayName("#42")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_42(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 2, 0, 1, -1));
		}

		@DisplayName("#43")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_43(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -4, 0, -5, -1));
		}

		@DisplayName("#44")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_44(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 0, 1, -1, 0));
		}

	}

	@DisplayName("interpolates")
	@Nested
	public class Interpolates {

		private Point2D result;

		@BeforeEach
		public void setUp() {
			result = createPoint(Double.NaN, Double.NaN);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.interpolates(1., 2., 3., 4., 0., result);
			assertEpsilonEquals(1, result.getX());
			assertEpsilonEquals(2, result.getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.interpolates(1., 2., 3., 4., .25, result);
			assertEpsilonEquals(1.5, result.getX());
			assertEpsilonEquals(2.5, result.getY());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.interpolates(1., 2., 3., 4., .5, result);
			assertEpsilonEquals(2, result.getX());
			assertEpsilonEquals(3., result.getY());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.interpolates(1., 2., 3., 4., .75, result);
			assertEpsilonEquals(2.5, result.getX());
			assertEpsilonEquals(3.5, result.getY());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp.interpolates(1., 2., 3., 4., 1., result);
			assertEpsilonEquals(3, result.getX());
			assertEpsilonEquals(4, result.getY());
		}

	}

	@DisplayName("intersectsLineLine")
	@Nested
	public class IntersectsLineLine {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsLineLine(
					0, 0, 1, 1,
					0, 0, 1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsLineLine(
					0, 0, 1, 1,
					0, 0, 2, 2));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsLineLine(
					0, 0, 1, 1,
					0, 0, .5, .5));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsLineLine(
					0, 0, 1, 1,
					-3, -3, .5, .5));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsLineLine(
					0, 0, 1, 1,
					-3, -3, 0, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsLineLine(
					0, 0, 1, 1,
					-3, -3, -1, -1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsLineLine(
					0, 0, 1, 1,
					-3, 0, 4, 0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsLineLine(
					0, 0, 1, 1,
					-3, 0, -2, 1));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsLineLine(
					0, 0, 1, 1,
					10, 0, 9, -1));
		}

	}

	@DisplayName("intersectsSegmentLine")
	@Nested
	public class IntersectsSegmentLine {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentLine(
					0, 0, 1, 1,
					0, 0, 1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentLine(
					0, 0, 1, 1,
					0, 0, 2, 2));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentLine(
					0, 0, 1, 1,
					0, 0, .5, .5));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentLine(
					0, 0, 1, 1,
					-3, -3, .5, .5));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentLine(
					0, 0, 1, 1,
					-3, -3, 0, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentLine(
					0, 0, 1, 1,
					-3, -3, -1, -1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentLine(
					0, 0, 1, 1,
					-3, 0, 4, 0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsSegmentLine(
					0, 0, 1, 1,
					-3, 0, -2, 1));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsSegmentLine(
					0, 0, 1, 1,
					10, 0, 9, -1));
		}

	}

	@DisplayName("intersectsSegmentSegmentWithEnds")
	@Nested
	public class IntersectsSegmentSegmentWithEnds {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentSegmentWithEnds(
					0, 0, 1, 1,
					0, .5, 1, .5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentSegmentWithEnds(
					0, 0, 1, 1,
					0, 0, 1, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentSegmentWithEnds(
					0, 0, 1, 1,
					0, 0, 2, 2));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentSegmentWithEnds(
					0, 0, 1, 1,
					0, 0, .5, .5));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentSegmentWithEnds(
					0, 0, 1, 1,
					-3, -3, .5, .5));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentSegmentWithEnds(
					0, 0, 1, 1,
					-3, -3, 0, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsSegmentSegmentWithEnds(
					0, 0, 1, 1,
					-3, -3, -1, -1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentSegmentWithEnds(
					0, 0, 1, 1,
					-3, 0, 4, 0));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsSegmentSegmentWithEnds(
					0, 0, 1, 1,
					-3, -1, 4, -1));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsSegmentSegmentWithEnds(
					0, 0, 1, 1,
					-3, -1, -1, -1));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsSegmentSegmentWithEnds(
					0, 0, 1, 1,
					-3, 0, -2, 1));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsSegmentSegmentWithEnds(
					0, 0, 1, 1,
					10, 0, 9, -1));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment2afp.intersectsSegmentSegmentWithEnds(
							7, -5, 1, 1,
							4, -3, 1, 1));
		}

	}

	@DisplayName("intersectsSegmentSegmentWithoutEnds")
	@Nested
	public class iIntersectsSegmentSegmentWithoutEnds {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					0, .5, 1, .5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					0, 0, 1, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					0, 0, 2, 2));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					0, 0, .5, .5));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					-3, -3, .5, .5));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					-3, -3, 0, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					-3, -3, -1, -1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					-3, 0, 4, 0));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					-3, -1, 4, -1));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					-3, -1, -1, -1));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					-3, 0, -2, 1));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 1, 1,
					10, 0, 9, -1));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(
					Segment2afp.intersectsSegmentSegmentWithoutEnds(
							7, -5, 1, 1,
							4, -3, 1, 1));
		}

	}

	@DisplayName("isCollinearLines")
	@Nested
	public class IsCollinearLines {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isCollinearLines(0, 0, 1, 1, 0, 0, 1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isCollinearLines(0, 0, 1, 1, 1, 1, 0, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isCollinearLines(0, 0, 1, 1, 0, 0, -1, -1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isCollinearLines(0, 0, 1, 1, -2, -2, -3, -3));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.isCollinearLines(0, 0, 1, 1, 5, 0, 6, 1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.isCollinearLines(0, 0, 1, 1, 154, -124, -2, 457));
		}

	}

	@DisplayName("isParallelLines")
	@Nested
	public class IsParallelLines {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isParallelLines(0, 0, 1, 1, 0, 0, 1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isParallelLines(0, 0, 1, 1, 1, 1, 0, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isParallelLines(0, 0, 1, 1, 0, 0, -1, -1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isParallelLines(0, 0, 1, 1, -2, -2, -3, -3));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isParallelLines(0, 0, 1, 1, 5, 0, 6, 1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.isParallelLines(0, 0, 1, 1, 154, -124, -2, 457));
		}

	}

	@DisplayName("isPointCloseToLine")
	@Nested
	public class IsPointCloseToLine {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isPointCloseToLine(0, 0, 1, 1, 0, 0, 0.1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isPointCloseToLine(0, 0, 1, 1, 1, 1, 0.1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isPointCloseToLine(0, 0, 1, 1, .25, .25, 0.1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.isPointCloseToLine(0, 0, 1, 1, 0.2, 0, 0.1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.isPointCloseToLine(0, 0, 1, 1, 120, 0, 0.1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isPointCloseToLine(0, 0, 1, 1, -20.05, -20, 0.1));
		}

	}

	@DisplayName("isPointCloseToSegment")
	@Nested
	public class IsPointCloseToSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isPointCloseToSegment(0, 0, 1, 1, 0, 0, 0.1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isPointCloseToSegment(0, 0, 1, 1, 1, 1, 0.1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2afp.isPointCloseToSegment(0, 0, 1, 1, .25, .25, 0.1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.isPointCloseToSegment(0, 0, 1, 1, 0.2, 0, 0.1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.isPointCloseToSegment(0, 0, 1, 1, 120, 0, 0.1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2afp.isPointCloseToSegment(0, 0, 1, 1, -20.05, -20, 0.1));
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
			assertNotSame(getS(), clone);
			assertEquals(getS().getClass(), clone.getClass());
			assertEpsilonEquals(0, clone.getX1());
			assertEpsilonEquals(0, clone.getY1());
			assertEpsilonEquals(1, clone.getX2());
			assertEpsilonEquals(1, clone.getY2());
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
			assertFalse(getS().equals(createSegment(0, 0, 5, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSegment(0, 0, 2, 2)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createCircle(5, 8, 6)));
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
			assertTrue(getS().equals(createSegment(0, 0, 1, 1)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSegment(0, 0, 5, 0).getPathIterator()));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSegment(0, 0, 2, 2).getPathIterator()));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createCircle(5, 8, 6).getPathIterator()));
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
			assertTrue(getS().equals(createSegment(0, 0, 1, 1).getPathIterator()));
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
			assertFalse(getS().equalsToPathIterator(createSegment(0, 0, 5, 0).getPathIterator()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToPathIterator(createSegment(0, 0, 2, 2).getPathIterator()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToPathIterator(createCircle(5, 8, 6).getPathIterator()));
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
			assertTrue(getS().equalsToPathIterator(createSegment(0, 0, 1, 1).getPathIterator()));
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
			assertFalse(getS().equalsToShape((T) createSegment(0, 0, 5, 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) createSegment(0, 0, 2, 2)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape(getS()));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape((T) createSegment(0, 0, 1, 1)));
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
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getX2());
			assertEpsilonEquals(0, getS().getY2());
		}

	}

	@DisplayName("getP1")
	@Nested
	public class GetP1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D p = getS().getP1();
			assertNotNull(p);
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
		}

	}

	@DisplayName("getP2")
	@Nested
	public class GetP2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D p = getS().getP2();
			assertNotNull(p);
			assertEpsilonEquals(1, p.getX());
			assertEpsilonEquals(1, p.getY());
		}

	}

	@DisplayName("setP1(double,double)")
	@Nested
	public class SetP1DoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setP1(123.456, -789.159);
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(-789.159, getS().getY1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
		}

	}

	@DisplayName("setP1(Point2D)")
	@Nested
	public class SetP1Point2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setP1(createPoint(123.456, -789.159));
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(-789.159, getS().getY1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
		}

	}

	@DisplayName("setP2(double,double)")
	@Nested
	public class SetP2DoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setP2(123.456, -789.159);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(123.456, getS().getX2());
			assertEpsilonEquals(-789.159, getS().getY2());
		}

	}

	@DisplayName("setP2(Point2D)")
	@Nested
	public class SetP2Point2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setP2(createPoint(123.456, -789.159));
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(123.456, getS().getX2());
			assertEpsilonEquals(-789.159, getS().getY2());
		}

	}

	@DisplayName("getX1")
	@Nested
	public class GetX1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getX1());
		}

	}

	@DisplayName("getX2")
	@Nested
	public class GetX2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getX2());
		}

	}

	@DisplayName("getY1")
	@Nested
	public class GetY1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getY1());
		}

	}

	@DisplayName("getY2")
	@Nested
	public class GetY2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getY2());
		}

	}

	@DisplayName("getLength")
	@Nested
	public class GetLength {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(2), getS().getLength());
		}

	}

	@DisplayName("getLengthSquared")
	@Nested
	public class GetLengthSquared {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, getS().getLengthSquared());
		}

	}

	@DisplayName("set(double,double,double,double)")
	@Nested
	public class SetDoubleDoubleDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(123.456, 456.789, 789.123, 159.753);
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(456.789, getS().getY1());
			assertEpsilonEquals(789.123, getS().getX2());
			assertEpsilonEquals(159.753, getS().getY2());
		}

	}

	@DisplayName("set(Point2D,Point2D)")
	@Nested
	public class SetPoint2DPoint2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(createPoint(123.456, 456.789), createPoint(789.123, 159.753));
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(456.789, getS().getY1());
			assertEpsilonEquals(789.123, getS().getX2());
			assertEpsilonEquals(159.753, getS().getY2());
		}

	}

	@DisplayName("setX1")
	@Nested
	public class SetX1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setX1(123.456);
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
		}

	}

	@DisplayName("setX2")
	@Nested
	public class SetX2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setX2(123.456);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(123.456, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
		}

	}

	@DisplayName("setY1")
	@Nested
	public class SetY1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setY1(123.456);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(123.456, getS().getY1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
		}

	}

	@DisplayName("setY2")
	@Nested
	public class SetY2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setY2(123.456);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(123.456, getS().getY2());
		}

	}

	@DisplayName("transform(Transform2D)")
	@Nested
	public class TransformTranform2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			var s = getS().clone();
			s.transform(tr);
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(1, s.getX2());
			assertEpsilonEquals(1, s.getY2());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setTranslation(3.4, 4.5);
			var s = getS().clone();
			s.transform(tr);
			assertEpsilonEquals(3.4, s.getX1());
			assertEpsilonEquals(4.5, s.getY1());
			assertEpsilonEquals(4.4, s.getX2());
			assertEpsilonEquals(5.5, s.getY2());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setRotation(MathConstants.PI);
			var s = getS().clone();
			s.transform(tr);
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(-1, s.getX2());
			assertEpsilonEquals(-1, s.getY2());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setRotation(MathConstants.QUARTER_PI);
			var s = getS().clone();
			s.transform(tr);
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getX2());
			assertEpsilonEquals(1.414213562, s.getY2());
		}

	}

	@DisplayName("clipToRectangle")
	@Nested
	public class ClipToRectangle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(20, 45, 43, 15);
			assertTrue(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(20, getS().getX1());
			assertEpsilonEquals(45, getS().getY1());
			assertEpsilonEquals(43, getS().getX2());
			assertEpsilonEquals(15, getS().getY2());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(20, 55, 43, 15);
			assertTrue(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(23.45, getS().getX1());
			assertEpsilonEquals(49.0, getS().getY1());
			assertEpsilonEquals(43, getS().getX2());
			assertEpsilonEquals(15, getS().getY2());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(20, 0, 43, 15);
			assertTrue(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(38.4, getS().getX1());
			assertEpsilonEquals(12.0, getS().getY1());
			assertEpsilonEquals(43, getS().getX2());
			assertEpsilonEquals(15, getS().getY2());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 45, 43, 15);
			assertTrue(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(10, getS().getX1());
			assertEpsilonEquals(38.02326, getS().getY1());
			assertEpsilonEquals(43, getS().getX2());
			assertEpsilonEquals(15, getS().getY2());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(20, 45, 60, 15);
			assertTrue(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(20, getS().getX1());
			assertEpsilonEquals(45, getS().getY1());
			assertEpsilonEquals(50, getS().getX2());
			assertEpsilonEquals(22.5, getS().getY2());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(5, 45, 30, 55);
			assertTrue(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(10, getS().getX1());
			assertEpsilonEquals(47, getS().getY1());
			assertEpsilonEquals(15, getS().getX2());
			assertEpsilonEquals(49, getS().getY2());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(40, 55, 60, 15);
			assertTrue(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(43, getS().getX1());
			assertEpsilonEquals(49, getS().getY1());
			assertEpsilonEquals(50, getS().getX2());
			assertEpsilonEquals(35, getS().getY2());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(40, 0, 60, 40);
			assertTrue(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(46, getS().getX1());
			assertEpsilonEquals(12, getS().getY1());
			assertEpsilonEquals(50, getS().getX2());
			assertEpsilonEquals(20, getS().getY2());
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 40, 20, 0);
			assertTrue(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(10, getS().getX1());
			assertEpsilonEquals(20, getS().getY1());
			assertEpsilonEquals(14, getS().getX2());
			assertEpsilonEquals(12, getS().getY2());
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 45, 100, 15);
			assertTrue(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(10, getS().getX1());
			assertEpsilonEquals(42, getS().getY1());
			assertEpsilonEquals(50, getS().getX2());
			assertEpsilonEquals(30, getS().getY2());
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(20, 100, 43, 0);
			assertTrue(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(31.73, getS().getX1());
			assertEpsilonEquals(49, getS().getY1());
			assertEpsilonEquals(40.24, getS().getX2());
			assertEpsilonEquals(12, getS().getY2());
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(20, 100, 43, 101);
			assertFalse(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(20, getS().getX1());
			assertEpsilonEquals(100, getS().getY1());
			assertEpsilonEquals(43, getS().getX2());
			assertEpsilonEquals(101, getS().getY2());
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(100, 45, 102, 15);
			assertFalse(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(100, getS().getX1());
			assertEpsilonEquals(45, getS().getY1());
			assertEpsilonEquals(102, getS().getX2());
			assertEpsilonEquals(15, getS().getY2());
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(20, 0, 43, -2);
			assertFalse(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(20, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(43, getS().getX2());
			assertEpsilonEquals(-2, getS().getY2());
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(-100, 45, -48, 15);
			assertFalse(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(-100, getS().getX1());
			assertEpsilonEquals(45, getS().getY1());
			assertEpsilonEquals(-48, getS().getX2());
			assertEpsilonEquals(15, getS().getY2());
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(-100, 60, -98, 61);
			assertFalse(getS().clipToRectangle(10, 12, 50, 49));
			assertEpsilonEquals(-100, getS().getX1());
			assertEpsilonEquals(60, getS().getY1());
			assertEpsilonEquals(-98, getS().getX2());
			assertEpsilonEquals(61, getS().getY2());
		}

	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		@DisplayName("(double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(0, 0));
		}

		@DisplayName("(double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(.5, .5));
		}

		@DisplayName("(double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(1, 1));
		}

		@DisplayName("(double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(2.3, 4.5));
			assertFalse(getS().contains(2, 2));
		}
	
		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(0, 0)));
		}
		
		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(.5, .5)));
		}
		
		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(1, 1)));
		}
		
		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(2.3, 4.5)));
		}
		
		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(2, 2)));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(0, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(0, 0, 0, 0)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(10, 10, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 15, 10, 18);
			assertFalse(getS().contains(createRectangle(10, 16, 0, 1)));
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
			assertFalse(getS().contains(createCircle(0, 0, 0)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(10, 10, 1)));
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
			var p = getS().getClosestPointTo(createPoint(0, 0));
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(.5, .5));
			assertEpsilonEquals(.5, p.getX());
			assertEpsilonEquals(.5, p.getY());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(1, 1));
			assertEpsilonEquals(1, p.getX());
			assertEpsilonEquals(1, p.getY());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(2, 2));
			assertEpsilonEquals(1, p.getX());
			assertEpsilonEquals(1, p.getY());
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(-2, 2));
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(0.1, 1.2));
			assertEpsilonEquals(0.65, p.getX());
			assertEpsilonEquals(0.65, p.getY());
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(10.1, -.2));
			assertEpsilonEquals(1, p.getX());
			assertEpsilonEquals(1, p.getY());
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0,  0,  getS().getClosestPointTo(createCircle(-5, 2, 1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1,  1,  getS().getClosestPointTo(createCircle(1, 20, 1)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(.5,  .5,  getS().getClosestPointTo(createCircle(-1, 2, 1)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(0, 1, 1));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(0, .6, 1));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createRectangle(-10, 5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createRectangle(-.5, 5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createRectangle(5, 5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createRectangle(-10, -3, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createRectangle(-.5, -3, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createRectangle(5, -3, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createRectangle(-2.5, -.5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(-1.5, -.5, 2, 1));
		}

		@DisplayName("(Rectangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(-1, .25, 5, .5));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createSegment(-2, 3, -1, 1)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createSegment(-1, 1, -3, 2)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createSegment(1, 0, -1, 0)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(-2, 0, 2, 1));
		}

		@DisplayName("(Segment2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createSegment(0, -1, -3, 0)));
		}

		@DisplayName("(Segment2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createSegment(0, 4, 3, 3)));
		}

		@DisplayName("(Segment2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(.5, .5, getS().getClosestPointTo(createSegment(1, 0, 4, 1)));
		}

		@DisplayName("(Segment2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(.5, .5, getS().getClosestPointTo(createSegment(1, 0, 3, -1)));
		}

		@DisplayName("(Segment2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createSegment(2, 0, 1, -1)));
		}

		@DisplayName("(Segment2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(.7, .7, getS().getClosestPointTo(createSegment(3, -1, 1, .4)));
		}

		@DisplayName("(Triangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triange_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestTriangle(-10, 5)));
		}

		@DisplayName("(Triangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triange_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestTriangle(-.5, 5)));
		}

		@DisplayName("(Triangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triange_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestTriangle(.5, 5)));
		}

		@DisplayName("(Triangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triange_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestTriangle(5, 5)));
		}

		@DisplayName("(Triangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triange_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestTriangle(-10, -5)));
		}

		@DisplayName("(Triangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triange_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestTriangle(-.5, -5)));
		}

		@DisplayName("(Triangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triange_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestTriangle(.5, -5)));
		}

		@DisplayName("(Triangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triange_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(.25, .25, getS().getClosestPointTo(createTestTriangle(5, -5)));
		}

		@DisplayName("(Triangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triange_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestTriangle(0, .5));
		}

		@DisplayName("(Triangle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triange_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestTriangle(0, .1));
		}

		@DisplayName("(Triangle2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triange_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(.75, .75, getS().getClosestPointTo(createTestTriangle(1, 0)));
		}

		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var multishape = createTestMultiShape(0, 0);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(multishape));
		}

		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var multishape = createTestMultiShape(5, 5);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(multishape));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-10, 5, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createOrientedRectangle(-.5, 5, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createOrientedRectangle(5, 5, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-10, -3, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-.5, -3, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createOrientedRectangle(5, -3, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-1.5, -.5, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-1, .25, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-1, .1, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-1, .4, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-1, -2).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-10, 5, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-1, -2).toUnitVector();
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createOrientedRectangle(-.5, 5, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-1, -2).toUnitVector();
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createOrientedRectangle(5, 5, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-1, -2).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-10, -3, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-1, -2).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-.5, -3, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-1, -2).toUnitVector();
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createOrientedRectangle(5, -3, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-1, -2).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-1.5, -.5, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-1, -2).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-1, .25, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-1, -2).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-1, .1, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-1, -2).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-1, .4, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(2, -1).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-10, 5, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(2, -1).toUnitVector();
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createOrientedRectangle(-.5, 5, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(2, -1).toUnitVector();
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createOrientedRectangle(5, 5, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(2, -1).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-10, -3, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_25(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(2, -1).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-.5, -3, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_26(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(2, -1).toUnitVector();
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createOrientedRectangle(5, -3, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_27(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(2, -1).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-1.5, -.5, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_28(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(2, -1).toUnitVector();
			assertClosestPointInBothShapes(getS(), createOrientedRectangle(-1, .25, u.getX(), u.getY(), 2, 1));
		}

		@DisplayName("(OrientedRectangle2afp) #29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_29(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(2, -1).toUnitVector();
			assertClosestPointInBothShapes(getS(), createOrientedRectangle(-1, .1, u.getX(), u.getY(), 2, 1));
		}

		@DisplayName("(OrientedRectangle2afp) #30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_30(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(2, -1).toUnitVector();
			assertClosestPointInBothShapes(getS(), createOrientedRectangle(-1, .4, u.getX(), u.getY(), 2, 1));
		}

		@DisplayName("(OrientedRectangle2afp) #31")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_31(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(1, 2).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-10, 5, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #32")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_32(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(1, 2).toUnitVector();
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createOrientedRectangle(-.5, 5, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #33")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_33(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(1, 2).toUnitVector();
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createOrientedRectangle(5, 5, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #34")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_34(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(1, 2).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-10, -3, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #35")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_35(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(1, 2).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-.5, -3, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #36")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_36(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(1, 2).toUnitVector();
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createOrientedRectangle(5, -3, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #37")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_37(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(1, 2).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-1.5, -.5, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #38")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_38(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(1, 2).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-1, .25, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #39")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_39(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(1, 2).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-1, .1, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #40")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_40(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(1, 2).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createOrientedRectangle(-1, .4, u.getX(), u.getY(), 1, 2)));
		}

		@DisplayName("(RoundRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createRoundRectangle(-10, 5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createRoundRectangle(-.5, 5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createRoundRectangle(5, 5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createRoundRectangle(-10, -3, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createRoundRectangle(-.5, -3, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createRoundRectangle(5, -3, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createRoundRectangle(-2.5, -.5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(-1.5, -.5, 2, 1, .2, .1));
		}

		@DisplayName("(RoundRectangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(-1, .25, 5, .5, .2, .1));
		}

		@DisplayName("(Ellipse2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createEllipse(-10, 5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createEllipse(5, 5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createEllipse(-10, -5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0.3866, 0.3866, getS().getClosestPointTo(createEllipse(5, -5, 2, 1)));
		}

		@DisplayName("(Parallelogram2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			Vector2D<?, ?> v = createVector(2, -3).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createParallelogram(-10, 5, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			Vector2D<?, ?> v = createVector(2, -3).toUnitVector();
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createParallelogram(5, 5, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			Vector2D<?, ?> v = createVector(2, -3).toUnitVector();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createParallelogram(-10, -3, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			Vector2D<?, ?> v = createVector(2, -3).toUnitVector();
			assertFpPointEquals(.69146, .69146, getS().getClosestPointTo(createParallelogram(5, -3, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestPath(0, 0, false)));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(0, 0, true));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestPath(.5, 0, false)));
		}

		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(.5, 0, true));
		}

		@DisplayName("(Path2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestPath(-.5, 0, false)));
		}

		@DisplayName("(Path2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(-.5, 0, true));
		}

		@DisplayName("(Path2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestPath(0, .5, false)));
		}

		@DisplayName("(Path2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(0, .5, true));
		}

		@DisplayName("(Path2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestPath(0, -.5, false)));
		}

		@DisplayName("(Path2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(0, -.5, true));
		}

		@DisplayName("(Path2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestPath(.5, -.5, false)));
		}

		@DisplayName("(Path2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(.5, -.5, true));
		}

		@DisplayName("(Path2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestPath(0, 0, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestPath(0, 0, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(.5, 0, false, PathWindingRule.EVEN_ODD));
		}

		@DisplayName("(Path2afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(.5, 0, true, PathWindingRule.EVEN_ODD));
		}

		@DisplayName("(Path2afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestPath(-.5, 0, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestPath(-.5, 0, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestPath(0, .5, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestPath(0, .5, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestPath(0, -.5, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestPath(0, -.5, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestPath(.5, -.5, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestPath(.5, -.5, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_25(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestPath(0, 0, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_26(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(0, 0, true, PathWindingRule.NON_ZERO));
		}

		@DisplayName("(Path2afp) #27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_27(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(.5, 0, false, PathWindingRule.NON_ZERO));
		}

		@DisplayName("(Path2afp) #28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_28(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(.5, 0, true, PathWindingRule.NON_ZERO));
		}

		@DisplayName("(Path2afp) #29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_29(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestPath(-.5, 0, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_30(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(-.5, 0, true, PathWindingRule.NON_ZERO));
		}

		@DisplayName("(Path2afp) #31")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_31(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestPath(0, .5, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #32")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_32(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(0, .5, true, PathWindingRule.NON_ZERO));
		}

		@DisplayName("(Path2afp) #33")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_33(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestPath(0, -.5, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #34")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_34(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(0, -.5, true, PathWindingRule.NON_ZERO));
		}

		@DisplayName("(Path2afp) #35")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_35(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createTestPath(.5, -.5, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #36")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_36(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(.5, -.5, true, PathWindingRule.NON_ZERO));
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
			var p = getS().getFarthestPointTo(createPoint(0, 0));
			assertEpsilonEquals(1, p.getX());
			assertEpsilonEquals(1, p.getY());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(.5, .5));
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(1, 1));
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(2, 2));
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(-2, 2));
			assertEpsilonEquals(1, p.getX());
			assertEpsilonEquals(1, p.getY());
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(0.1, 1.2));
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(10.1, -.2));
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
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
			assertEpsilonEquals(0, getS().getDistance(createPoint(.5, .5)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createPoint(1, 1)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.733630941, getS().getDistance(createPoint(2.3, 4.5)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.414213562, getS().getDistance(createPoint(2, 2)));
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
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(.5, .5)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(1, 1)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(13.94, getS().getDistanceSquared(createPoint(2.3, 4.5)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, getS().getDistanceSquared(createPoint(2, 2)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(19.22967, getS().getDistanceSquared(createCircle(-5, 2, 1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(324, getS().getDistanceSquared(createCircle(1, 20, 1)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.25736, getS().getDistanceSquared(createCircle(-1, 2, 1)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(0, 1, 1)));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(0, .6, 1)));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(89, getS().getDistanceSquared(createRectangle(-10, 5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16, getS().getDistanceSquared(createRectangle(-.5, 5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(32, getS().getDistanceSquared(createRectangle(5, 5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(68, getS().getDistanceSquared(createRectangle(-10, -3, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, getS().getDistanceSquared(createRectangle(-.5, -3, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25, getS().getDistanceSquared(createRectangle(5, -3, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.25, getS().getDistanceSquared(createRectangle(-2.5, -.5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(-1.5, -.5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(-1, .25, 5, .5)));
		}

		@DisplayName("(Rectangle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(-1, .1, 5, .5)));
		}

		@DisplayName("(Rectangle2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(-1, .4, 5, .5)));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, getS().getDistanceSquared(createSegment(-2, 3, -1, 1)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, getS().getDistanceSquared(createSegment(-1, 1, -3, 2)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, getS().getDistanceSquared(createSegment(0, 1, -1, 0)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(-2, 0, 2, 1)));
		}

		@DisplayName("(Segment2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.9, getS().getDistanceSquared(createSegment(0, -1, -3, 0)));
		}

		@DisplayName("(Segment2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.4, getS().getDistanceSquared(createSegment(0, 4, 3, 3)));
		}

		@DisplayName("(Segment2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, getS().getDistanceSquared(createSegment(1, 0, 4, 1)));
		}

		@DisplayName("(Segment2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, getS().getDistanceSquared(createSegment(1, 0, 3, -1)));
		}

		@DisplayName("(Segment2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, getS().getDistanceSquared(createSegment(2, 0, 1, -1)));
		}

		@DisplayName("(Segment2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.18, getS().getDistanceSquared(createSegment(3, -1, 1, .4)));
		}

		@DisplayName("(Triangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(106, getS().getDistanceSquared(createTestTriangle(-10, 5)));
		}

		@DisplayName("(Triangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16.25, getS().getDistanceSquared(createTestTriangle(-.5, 5)));
		}

		@DisplayName("(Triangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16, getS().getDistanceSquared(createTestTriangle(.5, 5)));
		}

		@DisplayName("(Triangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(32, getS().getDistanceSquared(createTestTriangle(5, 5)));
		}

		@DisplayName("(Triangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(106, getS().getDistanceSquared(createTestTriangle(-10, -5)));
		}

		@DisplayName("(Triangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(20.5, getS().getDistanceSquared(createTestTriangle(-.5, -5)));
		}

		@DisplayName("(Triangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(20.5, getS().getDistanceSquared(createTestTriangle(.5, -5)));
		}

		@DisplayName("(Triangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(45.125, getS().getDistanceSquared(createTestTriangle(5, -5)));
		}

		@DisplayName("(Triangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestTriangle(0, .5)));
		}

		@DisplayName("(Triangle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestTriangle(0, .1)));
		}

		@DisplayName("(Triangle2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.125, getS().getDistanceSquared(createTestTriangle(1, 0)));
		}

		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var multishape = createTestMultiShape(0, 0);
			assertEpsilonEquals(10, getS().getDistanceSquared(multishape));
		}

		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var multishape = createTestMultiShape(5, 5);
			assertEpsilonEquals(49.87548, getS().getDistanceSquared(multishape));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertEpsilonEquals(84.27864, getS().getDistanceSquared(createOrientedRectangle(-10, 5, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertEpsilonEquals(4.91424, getS().getDistanceSquared(createOrientedRectangle(-.5, 5, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertEpsilonEquals(19.06687, getS().getDistanceSquared(createOrientedRectangle(5, 5, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertEpsilonEquals(69.27864, getS().getDistanceSquared(createOrientedRectangle(-10, -3, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertEpsilonEquals(3.63622, getS().getDistanceSquared(createOrientedRectangle(-.5, -3, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertEpsilonEquals(11.95604, getS().getDistanceSquared(createOrientedRectangle(5, -3, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertEpsilonEquals(.013932, getS().getDistanceSquared(createOrientedRectangle(-1.5, -.5, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertEpsilonEquals(0, getS().getDistanceSquared(createOrientedRectangle(-1, .25, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertEpsilonEquals(0, getS().getDistanceSquared(createOrientedRectangle(-1, .1, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			assertEpsilonEquals(0, getS().getDistanceSquared(createOrientedRectangle(-1, .4, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(RoundRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(89.85237, getS().getDistanceSquared(createRoundRectangle(-10, 5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16, getS().getDistanceSquared(createRoundRectangle(-.5, 5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(32.61464, getS().getDistanceSquared(createRoundRectangle(5, 5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(68.38273, getS().getDistanceSquared(createRoundRectangle(-10, -3, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, getS().getDistanceSquared(createRoundRectangle(-.5, -3, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25.49554, getS().getDistanceSquared(createRoundRectangle(5, -3, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.25, getS().getDistanceSquared(createRoundRectangle(-2.5, -.5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(-1.5, -.5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(-1, .25, 5, .5, .2, .1)));
		}

		@DisplayName("(Ellipse2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(93.35943, getS().getDistanceSquared(createEllipse(-10, 5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(35.14211, getS().getDistanceSquared(createEllipse(5, 5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(83.64829, getS().getDistanceSquared(createEllipse(-10, -5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(44.0121, getS().getDistanceSquared(createEllipse(5, -5, 2, 1)));
		}

		@DisplayName("(Parallelogram2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			Vector2D<?, ?> v = createVector(2, -3).toUnitVector();
			assertEpsilonEquals(69.33711, getS().getDistanceSquared(createParallelogram(-10, 5, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			Vector2D<?, ?> v = createVector(2, -3).toUnitVector();
			assertEpsilonEquals(24.15281, getS().getDistanceSquared(createParallelogram(5, 5, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			Vector2D<?, ?> v = createVector(2, -3).toUnitVector();
			assertEpsilonEquals(80.96075, getS().getDistanceSquared(createParallelogram(-10, -3, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
			Vector2D<?, ?> v = createVector(2, -3).toUnitVector();
			assertEpsilonEquals(7.72232, getS().getDistanceSquared(createParallelogram(5, -3, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.23077, getS().getDistanceSquared(createTestPath(0, 0, false)));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(0, 0, true)));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.8, getS().getDistanceSquared(createTestPath(.5, 0, false)));
		}

		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(.5, 0, true)));
		}

		@DisplayName("(Path2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.48077, getS().getDistanceSquared(createTestPath(-.5, 0, false)));
		}

		@DisplayName("(Path2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(-.5, 0, true)));
		}

		@DisplayName("(Path2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.25, getS().getDistanceSquared(createTestPath(0, .5, false)));
		}

		@DisplayName("(Path2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(0, .5, true)));
		}

		@DisplayName("(Path2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.69231, getS().getDistanceSquared(createTestPath(0, -.5, false)));
		}

		@DisplayName("(Path2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(0, -.5, true)));
		}

		@DisplayName("(Path2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.94231, getS().getDistanceSquared(createTestPath(.5, -.5, false)));
		}

		@DisplayName("(Path2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(.5, -.5, true)));
		}

		@DisplayName("(Path2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.2, getS().getDistanceSquared(createTestPath(0, 0, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.2, getS().getDistanceSquared(createTestPath(0, 0, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(.5, 0, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(.5, 0, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.019231, getS().getDistanceSquared(createTestPath(-.5, 0, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.019231, getS().getDistanceSquared(createTestPath(-.5, 0, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.05, getS().getDistanceSquared(createTestPath(0, .5, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.05, getS().getDistanceSquared(createTestPath(0, .5, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.076923, getS().getDistanceSquared(createTestPath(0, -.5, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.076923, getS().getDistanceSquared(createTestPath(0, -.5, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.05, getS().getDistanceSquared(createTestPath(.5, -.5, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.05, getS().getDistanceSquared(createTestPath(.5, -.5, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_25(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.2, getS().getDistanceSquared(createTestPath(0, 0, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_26(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(0, 0, true, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_27(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(.5, 0, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_28(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(.5, 0, true, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_29(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.019231, getS().getDistanceSquared(createTestPath(-.5, 0, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_30(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(-.5, 0, true, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #31")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_31(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.05, getS().getDistanceSquared(createTestPath(0, .5, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #32")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_32(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(0, .5, true, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #33")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_33(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.076923, getS().getDistanceSquared(createTestPath(0, -.5, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #34")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_34(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(0, -.5, true, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #35")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_35(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.05, getS().getDistanceSquared(createTestPath(.5, -.5, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #36")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_36(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(.5, -.5, true, PathWindingRule.NON_ZERO)));
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
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(.5, .5)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(1, 1)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.8, getS().getDistanceL1(createPoint(2.3, 4.5)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, getS().getDistanceL1(createPoint(2, 2)));
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
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(.5, .5)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(1, 1)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.5, getS().getDistanceLinf(createPoint(2.3, 4.5)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceLinf(createPoint(2, 2)));
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
			getS().set((T) createSegment(123.456, 456.789, 789.123, 159.753));
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(456.789, getS().getY1());
			assertEpsilonEquals(789.123, getS().getX2());
			assertEpsilonEquals(159.753, getS().getY2());
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
			assertElement(pi, PathElementType.MOVE_TO, 0,0);
			assertElement(pi, PathElementType.LINE_TO, 1,1);
			assertNoElement(pi);
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
			var tr = new Transform2D();
			var pi = getS().getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 0,0);
			assertElement(pi, PathElementType.LINE_TO, 1,1);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeTranslationMatrix(3.4, 4.5);
			var pi = getS().getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 3.4, 4.5);
			assertElement(pi, PathElementType.LINE_TO, 4.4, 5.5);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeRotationMatrix(MathConstants.QUARTER_PI);
			var pi = getS().getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 0, 1.414213562);
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
			var tr = new Transform2D();    	
			var s = (Segment2afp) getS().createTransformedShape(tr);
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(1, s.getX2());
			assertEpsilonEquals(1, s.getY2());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setTranslation(3.4, 4.5);
			var s = (Segment2afp) getS().createTransformedShape(tr);
			assertEpsilonEquals(3.4, s.getX1());
			assertEpsilonEquals(4.5, s.getY1());
			assertEpsilonEquals(4.4, s.getX2());
			assertEpsilonEquals(5.5, s.getY2());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setRotation(MathConstants.PI);
			var s = (Segment2afp) getS().createTransformedShape(tr);
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(-1, s.getX2());
			assertEpsilonEquals(-1, s.getY2());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setRotation(MathConstants.QUARTER_PI);
			var s = (Segment2afp) getS().createTransformedShape(tr);
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getX2());
			assertEpsilonEquals(1.414213562, s.getY2());
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
			getS().translate(3.4, 4.5);
			assertEpsilonEquals(3.4, getS().getX1());
			assertEpsilonEquals(4.5, getS().getY1());
			assertEpsilonEquals(4.4, getS().getX2());
			assertEpsilonEquals(5.5, getS().getY2());
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
			getS().translate(createVector(3.4, 4.5));
			assertEpsilonEquals(3.4, getS().getX1());
			assertEpsilonEquals(4.5, getS().getY1());
			assertEpsilonEquals(4.4, getS().getX2());
			assertEpsilonEquals(5.5, getS().getY2());
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
			B bb = getS().toBoundingBox();
			assertEpsilonEquals(0, bb.getMinX());
			assertEpsilonEquals(0, bb.getMinY());
			assertEpsilonEquals(1, bb.getMaxX());
			assertEpsilonEquals(1, bb.getMaxY());
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
			B bb = createRectangle(0, 0, 0, 0);
			getS().toBoundingBox(bb);
			assertEpsilonEquals(0, bb.getMinX());
			assertEpsilonEquals(0, bb.getMinY());
			assertEpsilonEquals(1, bb.getMaxX());
			assertEpsilonEquals(1, bb.getMaxY());
		}

	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(20, 45, 43, 15);
			assertTrue(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(20, 55, 43, 15);
			assertTrue(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(20, 0, 43, 15);
			assertTrue(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 45, 43, 15);
			assertTrue(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(20, 45, 60, 15);
			assertTrue(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Rectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(5, 45, 30, 55);
			assertTrue(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Rectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(40, 55, 60, 15);
			assertTrue(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Rectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(40, 0, 60, 40);
			assertTrue(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Rectangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 40, 20, 0);
			assertTrue(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Rectangle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 45, 100, 15);
			assertTrue(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Rectangle2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(20, 100, 43, 0);
			assertTrue(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Rectangle2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(20, 100, 43, 101);
			assertFalse(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Rectangle2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(100, 45, 102, 15);
			assertFalse(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Rectangle2afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(20, 0, 43, -2);
			assertFalse(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Rectangle2afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(-100, 45, -48, 15);
			assertFalse(getS().intersects(createRectangle(10, 12, 50, 49)));
		}

		@DisplayName("(Rectangle2afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void restangle_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(-100, 60, -98, 61);
			assertFalse(getS().intersects(createRectangle(10, 12, 40, 37)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(10, 10, 1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(0, 0, 1)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(0, .5, 1)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(.5, 0, 1)));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(.5, .5, 1)));
		}

		@DisplayName("(Circle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(2, 0, 1)));
		}

		@DisplayName("(Circle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(12, 8, 2)));
		}

		@DisplayName("(Circle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(12, 8, 2.1)));
		}

		@DisplayName("(Circle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(2, 1, 1)));
		}

		@DisplayName("(Circle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(2, 1, 1.1)));
		}

		@DisplayName("(Circle2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 3, 0);
			assertFalse(getS().intersects(createCircle(2, 1, 1)));
		}

		@DisplayName("(Circle2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 3, 0);
			assertTrue(getS().intersects(createCircle(2, 1, 1.1)));
		}

		@DisplayName("(Ellipse2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(5, -4, -1, -5)));
		}

		@DisplayName("(Ellipse2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(-7, 3, -5, -1)));
		}

		@DisplayName("(Ellipse2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(11, -2, 10, -1)));
		}

		@DisplayName("(Ellipse2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(3, 5, 1, 6)));
		}

		@DisplayName("(Ellipse2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(5, .5, 6, -1)));
		}

		@DisplayName("(Ellipse2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(5, 2, 6, -1)));
		}

		@DisplayName("(Ellipse2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(5, 2, 6, .5)));
		}

		@DisplayName("(Ellipse2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertTrue(getS().intersects(createEllipseFromCorners(.5, .5, 3, 0)));
		}

		@DisplayName("(Ellipse2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertTrue(getS().intersects(createEllipseFromCorners(0, 1, 3, 0)));
		}

		@DisplayName("(Ellipse2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertTrue(getS().intersects(createEllipseFromCorners(0, 1, 3, 0)));
		}

		@DisplayName("(Ellipse2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(.5, 2, .5, -1)));
		}

		@DisplayName("(Ellipse2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(-1, -5, 5, -4)));
		}

		@DisplayName("(Ellipse2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(-5, -1, -7, 3)));
		}

		@DisplayName("(Ellipse2afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(10, -1, 11, -2)));
		}

		@DisplayName("(Ellipse2afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(1, 6, 3, 5)));
		}

		@DisplayName("(Ellipse2afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(6, -1, 5, .5)));
		}

		@DisplayName("(Ellipse2afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(6, -1, 5, 2)));
		}

		@DisplayName("(Ellipse2afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(6, .5, 5, 2)));
		}

		@DisplayName("(Ellipse2afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertTrue(getS().intersects(createEllipseFromCorners(3, 0, .5, .5)));
		}

		@DisplayName("(Ellipse2afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertTrue(getS().intersects(createEllipseFromCorners(3, 0, 0, 1)));
		}

		@DisplayName("(Ellipse2afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertTrue(getS().intersects(createEllipseFromCorners(3, 0, 0, 1)));
		}

		@DisplayName("(Ellipse2afp) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 2, 1);
			assertFalse(getS().intersects(createEllipseFromCorners(.5, -1, .5, 2)));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(10, -5, 10, -4)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(10, 5, 10, 4)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(-5, .5, 0, .6)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(10, -1, 11, .6)));
		}

		@DisplayName("(Segment2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(10, -1, 11, 2)));
		}

		@DisplayName("(Segment2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(10, .5, 11, 2)));
		}

		@DisplayName("(Segment2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(10, 2, 11, .6)));
		}

		@DisplayName("(Segment2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(10, 2, 11, -1)));
		}

		@DisplayName("(Segment2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(10, .6, 11, -1)));
		}

		@DisplayName("(Segment2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(0, .5, .25, .5)));
		}

		@DisplayName("(Segment2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(.75, .5, 1, .5)));
		}

		@DisplayName("(Segment2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(5, -5, .75, .5)));
		}

		@DisplayName("(Segment2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(5, -5, 0, 1)));
		}

		@DisplayName("(Segment2afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(5, -5, 1, 1)));
		}

		@DisplayName("(Segment2afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(-2, 1, 5, -5)));
		}

		@DisplayName("(Triangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertTrue(createSegment(-6, -2, -4, 0).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertTrue(createSegment(-6, -2, 2, 0).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertFalse(createSegment(-6, -2, 14, -4).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertTrue(createSegment(-2, 2, 4, 0).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertTrue(createSegment(-2, 2, 0, 0).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertTrue(createSegment(-4, -2, -6, 6).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertTrue(createSegment(6, 7, -6, 6).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertTrue(createSegment(0, 5, -6, 6).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertFalse(createSegment(-5, 5, -6, 6).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertFalse(createSegment(-4, -2, 2, -2).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertFalse(createSegment(-1, -2, 5, 8).intersects(triangle));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(-2, 2);
			p.lineTo(2, 2);
			p.lineTo(2, -2);
			assertFalse(getS().intersects(p));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(-2, 2);
			p.lineTo(2, 2);
			p.lineTo(2, -2);
			p.closePath();
			assertTrue(getS().intersects(p));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(0, 0);
			p.lineTo(-2, 2);
			assertFalse(getS().intersects(p));
		}

		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(0, 0);
			p.lineTo(-2, 2);
			p.closePath();
			assertFalse(getS().intersects(p));
		}

		@DisplayName("(Path2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(2, 2);
			p.lineTo(-2, 2);
			assertTrue(getS().intersects(p));
		}

		@DisplayName("(Path2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(2, 2);
			p.lineTo(-2, 2);
			p.closePath();
			assertTrue(getS().intersects(p));
		}

		@DisplayName("(Path2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(-2, 2);
			p.lineTo(2, -2);
			assertTrue(getS().intersects(p));
		}

		@DisplayName("(Path2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(-2, 2);
			p.lineTo(2, -2);
			p.closePath();
			assertTrue(getS().intersects(p));
		}

		@DisplayName("(Path2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, 2);
			p.lineTo(1, 0);
			p.lineTo(2, 1);
			assertTrue(getS().intersects(p));
		}

		@DisplayName("(Path2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, 2);
			p.lineTo(1, 0);
			p.lineTo(2, 1);
			p.closePath();
			assertTrue(getS().intersects(p));
		}

		@DisplayName("(Path2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, 2);
			p.lineTo(2, 1);
			p.lineTo(1, 0);
			assertFalse(getS().intersects(p));
		}

		@DisplayName("(Path2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, 2);
			p.lineTo(2, 1);
			p.lineTo(1, 0);
			p.closePath();
			assertTrue(getS().intersects(p));
		}

		@DisplayName("(PathIterator2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(-2, 2);
			p.lineTo(2, 2);
			p.lineTo(2, -2);
			assertFalse(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(-2, 2);
			p.lineTo(2, 2);
			p.lineTo(2, -2);
			p.closePath();
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(0, 0);
			p.lineTo(-2, 2);
			assertFalse(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(0, 0);
			p.lineTo(-2, 2);
			p.closePath();
			assertFalse(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(2, 2);
			p.lineTo(-2, 2);
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(2, 2);
			p.lineTo(-2, 2);
			p.closePath();
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(-2, 2);
			p.lineTo(2, -2);
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2);
			p.lineTo(-2, 2);
			p.lineTo(2, -2);
			p.closePath();
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, 2);
			p.lineTo(1, 0);
			p.lineTo(2, 1);
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, 2);
			p.lineTo(1, 0);
			p.lineTo(2, 1);
			p.closePath();
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, 2);
			p.lineTo(2, 1);
			p.lineTo(1, 0);
			assertFalse(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, 2);
			p.lineTo(2, 1);
			p.lineTo(1, 0);
			p.closePath();
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createOrientedRectangle(
					7, 3.5,
					-0.99839, -0.05671,
					8.06684, 1.81085)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createOrientedRectangle(
					7, 3.5,
					-0.7471, -0.66471,
					9.43417, 1.81085)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createOrientedRectangle(
					7, 3.5,
					-0.81997, -0.57241,
					6.94784, 1.81085)));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createOrientedRectangle(
					7, 3.5,
					-0.84335, -0.53736,
					8.80456, 1.81085)));
		}

		@DisplayName("(Parallelogram2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9,
					2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertFalse(createSegment(0, 0, 1, 1).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9,
					2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertTrue(createSegment(5, 5, 4, 6).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9,
					2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertTrue(createSegment(2, -2, 5, 0).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9,
					2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertFalse(createSegment(-20, -5, -10, 6).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9,
					2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertFalse(createSegment(-5, 0, -10, 16).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9,
					2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertTrue(createSegment(-10, 1, 10, 20).intersects(para));
		}

		@DisplayName("(RoundRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			RoundRectangle2afp<?, ?, ?, ?, ?, B> box = createRoundRectangle(0, 0, 1, 1, .2, .4);
			assertTrue(getS().intersects(box));
		}

		@DisplayName("(RoundRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			RoundRectangle2afp<?, ?, ?, ?, ?, B> box = createRoundRectangle(0, 0, 1, 1, .2, .4);
			assertTrue(createSegment(-5, -5, 1, 1).intersects(box));
		}

		@DisplayName("(RoundRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			RoundRectangle2afp<?, ?, ?, ?, ?, B> box = createRoundRectangle(0, 0, 1, 1, .2, .4);
			assertTrue(createSegment(.5, .5, 5, 5).intersects(box));
		}

		@DisplayName("(RoundRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			RoundRectangle2afp<?, ?, ?, ?, ?, B> box = createRoundRectangle(0, 0, 1, 1, .2, .4);
			assertTrue(createSegment(.5, .5, 5, .6).intersects(box));
		}

		@DisplayName("(RoundRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			RoundRectangle2afp<?, ?, ?, ?, ?, B> box = createRoundRectangle(0, 0, 1, 1, .2, .4);
			assertTrue(createSegment(-5, -5, 5, 5).intersects(box));
		}

		@DisplayName("(RoundRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			RoundRectangle2afp<?, ?, ?, ?, ?, B> box = createRoundRectangle(0, 0, 1, 1, .2, .4);
			assertFalse(createSegment(-5, -5, -4, -4).intersects(box));
		}

		@DisplayName("(RoundRectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			RoundRectangle2afp<?, ?, ?, ?, ?, B> box = createRoundRectangle(0, 0, 1, 1, .2, .4);
			assertFalse(createSegment(-5, -5, 4, -4).intersects(box));
		}

		@DisplayName("(RoundRectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			RoundRectangle2afp<?, ?, ?, ?, ?, B> box = createRoundRectangle(0, 0, 1, 1, .2, .4);
			assertFalse(createSegment(5, -5, 6, 5).intersects(box));
		}

		@DisplayName("(RoundRectangle2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			RoundRectangle2afp<?, ?, ?, ?, ?, B> box = createRoundRectangle(0, 0, 1, 1, .2, .4);
			assertFalse(createSegment(5, -5, 5, 5).intersects(box));
		}

		@DisplayName("(RoundRectangle2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			RoundRectangle2afp<?, ?, ?, ?, ?, B> box = createRoundRectangle(0, 0, 1, 1, .2, .4);
			assertFalse(createSegment(-1, -1, 0.01, 0.01).intersects(box));
		}

		@DisplayName("(RoundRectangle2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			RoundRectangle2afp<?, ?, ?, ?, ?, B> box = createRoundRectangle(0, 0, 1, 1, .2, .4);
			assertTrue(createSegment(-1, -1, 0.1, 0.2).intersects(box));
		}

		@DisplayName("(Shape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects((Shape2D) createCircle(0, 0, 1)));
		}

		@DisplayName("(Shape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects((Shape2D) createEllipseFromCorners(5, 2, 6, .5)));
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
			getS().operator_add(createVector(3.4, 4.5));
			assertEpsilonEquals(3.4, getS().getX1());
			assertEpsilonEquals(4.5, getS().getY1());
			assertEpsilonEquals(4.4, getS().getX2());
			assertEpsilonEquals(5.5, getS().getY2());
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
			T shape = getS().operator_plus(createVector(3.4, 4.5));
			assertNotSame(shape, getS());
			assertEpsilonEquals(3.4, shape.getX1());
			assertEpsilonEquals(4.5, shape.getY1());
			assertEpsilonEquals(4.4, shape.getX2());
			assertEpsilonEquals(5.5, shape.getY2());
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
			getS().operator_remove(createVector(3.4, 4.5));
			assertEpsilonEquals(-3.4, getS().getX1());
			assertEpsilonEquals(-4.5, getS().getY1());
			assertEpsilonEquals(-2.4, getS().getX2());
			assertEpsilonEquals(-3.5, getS().getY2());
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
			T shape = getS().operator_minus(createVector(3.4, 4.5));
			assertEpsilonEquals(-3.4, shape.getX1());
			assertEpsilonEquals(-4.5, shape.getY1());
			assertEpsilonEquals(-2.4, shape.getX2());
			assertEpsilonEquals(-3.5, shape.getY2());
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
			var tr = new Transform2D();    	
			var s = (Segment2afp) getS().operator_multiply(tr);
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(1, s.getX2());
			assertEpsilonEquals(1, s.getY2());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setTranslation(3.4, 4.5);
			var s = (Segment2afp) getS().operator_multiply(tr);
			assertEpsilonEquals(3.4, s.getX1());
			assertEpsilonEquals(4.5, s.getY1());
			assertEpsilonEquals(4.4, s.getX2());
			assertEpsilonEquals(5.5, s.getY2());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setRotation(MathConstants.PI);
			var s = (Segment2afp) getS().operator_multiply(tr);
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(-1, s.getX2());
			assertEpsilonEquals(-1, s.getY2());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setRotation(MathConstants.QUARTER_PI);
			var s = (Segment2afp) getS().operator_multiply(tr);
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getX2());
			assertEpsilonEquals(1.414213562, s.getY2());
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
			assertTrue(getS().operator_and(createPoint(.5, .5)));
			assertTrue(getS().operator_and(createPoint(1, 1)));
			assertFalse(getS().operator_and(createPoint(2.3, 4.5)));
			assertFalse(getS().operator_and(createPoint(2, 2)));
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
			assertTrue(getS().operator_and(createCircle(0, 0, 1)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createEllipseFromCorners(5, 2, 6, .5)));
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
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(.5, .5)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(1, 1)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.733630941, getS().operator_upTo(createPoint(2.3, 4.5)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.414213562, getS().operator_upTo(createPoint(2, 2)));
		}

	}

	@DisplayName("Issues")
	@Nested
	public class Issues {

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void issue_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2afp segment = createSegment(-20, -20, 20, 20);
			Path2afp path = createPath();
			path.moveTo(5, 5);
			path.lineTo(5, -5);
			path.lineTo(-5, -5);
			path.lineTo(-5, 5);
			path.lineTo(5, 5);
			assertTrue(path.intersects(segment));
		}

	}

}