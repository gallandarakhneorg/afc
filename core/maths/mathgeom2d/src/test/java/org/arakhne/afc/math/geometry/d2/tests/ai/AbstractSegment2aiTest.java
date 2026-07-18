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

package org.arakhne.afc.math.geometry.d2.tests.ai;

import static org.arakhne.afc.math.geometry.base.GeomConstants.SHAPE_INTERSECTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractSegment2aiTest<T extends Segment2ai<?, T, ?, ?, ?, B>,
B extends Rectangle2ai<?, ?, ?, ?, ?, B>> extends AbstractShape2aiTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createSegment(0, 0, 10, 5);
	}

	protected MultiShape2ai createTestMultiShape(int dx, int dy) {
		MultiShape2ai multishape = createMultiShape();
		Segment2ai segment = createSegment(dx - 5, dy - 4, dx - 8, dy - 1);
		Rectangle2ai rectangle = createRectangle(dx + 2, dy + 1, 3, 2);
		multishape.add(segment);
		multishape.add(rectangle);
		return multishape;
	}

	protected Path2ai createTestPath(int dx, int dy) {
		Path2ai path = createPath();
		path.moveTo(dx + 5, dy - 5);
		path.lineTo(dx + 20, dy + 5);
		path.lineTo(dx + 0, dy + 20);
		path.lineTo(dx - 5, dy);
		return path;
	}

	@DisplayName("clone")
	@Nested
	public class CloneTest {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			T clone = shape.clone();
			assertNotNull(clone);
			assertNotSame(shape, clone);
			assertEquals(shape.getClass(), clone.getClass());
			assertEpsilonEquals(0, clone.getX1());
			assertEpsilonEquals(0, clone.getY1());
			assertEpsilonEquals(10, clone.getX2());
			assertEpsilonEquals(5, clone.getY2());
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
			assertFalse(shape.equals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equals(new Object()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equals(createSegment(0, 0, 5, 5)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equals(createSegment(0, 0, 10, 6)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equals(createRectangle(0, 0, 10, 5)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.equals(shape));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.equals(createSegment(0, 0, 10, 5)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equals(createSegment(0, 0, 5, 5).getPathIterator()));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equals(createSegment(0, 0, 10, 6).getPathIterator()));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equals(createRectangle(0, 0, 10, 5).getPathIterator()));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.equals(shape.getPathIterator()));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.equals(createSegment(0, 0, 10, 5).getPathIterator()));
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
			assertFalse(shape.equalsToShape(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equalsToShape((T) createSegment(0, 0, 10, 6)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.equalsToShape(shape));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.equalsToShape((T) createSegment(0, 0, 10, 5)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equalsToShape((T) createSegment(0, 0, 5, 5)));
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
			assertFalse(shape.equalsToPathIterator((PathIterator2ai) null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equalsToPathIterator(createSegment(0, 0, 5, 5).getPathIterator()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equalsToPathIterator(createSegment(0, 0, 10, 6).getPathIterator()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equalsToPathIterator(createRectangle(0, 0, 10, 5).getPathIterator()));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.equalsToPathIterator(shape.getPathIterator()));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.equalsToPathIterator(createSegment(0, 0, 10, 5).getPathIterator()));
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
			assertFalse(shape.isEmpty());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.clear();
			assertTrue(shape.isEmpty());
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
			shape.clear();
			assertEquals(0, shape.getX1());
			assertEquals(0, shape.getY1());
			assertEquals(0, shape.getX2());
			assertEquals(0, shape.getY2());
		}
	}

	@DisplayName("getDistance")
	@Nested
	public class GetDistance {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, shape.getDistance(createPoint(0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, shape.getDistance(createPoint(1, 1)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.828427125f, shape.getDistance(createPoint(2, 4)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, shape.getDistance(createPoint(2, 2)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.071067812f, shape.getDistance(createPoint(-5, 5)));
		}
	}

	@DisplayName("getDistanceSquared")
	@Nested
	public class GetDistanceSquared {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, shape.getDistanceSquared(createPoint(0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, shape.getDistanceSquared(createPoint(1, 1)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8f, shape.getDistanceSquared(createPoint(2, 4)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, shape.getDistanceSquared(createPoint(2, 2)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(50f, shape.getDistanceSquared(createPoint(-5, 5)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, shape.getDistanceSquared(createCircle(0, 0, 2)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(125, shape.getDistanceSquared(createCircle(0, 15, 2)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25, shape.getDistanceSquared(createCircle(15, 0, 2)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, shape.getDistanceSquared(createCircle(4, 5, 2)));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, shape.getDistanceSquared(createSegment(0, 0, 5, -2)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(89, shape.getDistanceSquared(createSegment(0, 15, 5, 13)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(40, shape.getDistanceSquared(createSegment(15, 0, 20, 13)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, shape.getDistanceSquared(createSegment(4, 5, 9, 3)));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, shape.getDistanceSquared(createRectangle(0, 0, 3, 2)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(58, shape.getDistanceSquared(createRectangle(2, 11, 3, 2)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, shape.getDistanceSquared(createRectangle(2, 3, 3, 2)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, shape.getDistanceSquared(createRectangle(3, 3, 3, 2)));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(180, shape.getDistanceSquared(createRectangle(15, -10, 3, 2)));
		}

		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, shape.getDistanceSquared(createTestMultiShape(0, 0)));
		}

		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(146, shape.getDistanceSquared(createTestMultiShape(0, 15)));
		}

		@DisplayName("(MultiShape2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(13, shape.getDistanceSquared(createTestMultiShape(15, 0)));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, shape.getDistanceSquared(createTestPath(0, 0)));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(45, shape.getDistanceSquared(createTestPath(0, 15)));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, shape.getDistanceSquared(createTestPath(15, 0)));
		}
	}

	@DisplayName("getDistanceL1")
	@Nested
	public class GetDistanceL1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, shape.getDistanceL1(createPoint(0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, shape.getDistanceL1(createPoint(1, 1)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4f, shape.getDistanceL1(createPoint(2, 4)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, shape.getDistanceL1(createPoint(2, 2)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10f, shape.getDistanceL1(createPoint(-5, 5)));
		}
	}

	@DisplayName("getDistanceLinf")
	@Nested
	public class GetDistanceLinf {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, shape.getDistanceLinf(createPoint(0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, shape.getDistanceLinf(createPoint(1, 1)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2f, shape.getDistanceLinf(createPoint(2, 4)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, shape.getDistanceLinf(createPoint(2, 2)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5f, shape.getDistanceLinf(createPoint(-5, 5)));
		}
	}

	@DisplayName("translate(int,int)")
	@Nested
	public class TranslateIntInt {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.translate(3, 4);
			assertEquals(3, shape.getX1());
			assertEquals(4, shape.getY1());
			assertEquals(13, shape.getX2());
			assertEquals(9, shape.getY2());
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
			shape.translate(createVector(3, 4));
			assertEquals(3, shape.getX1());
			assertEquals(4, shape.getY1());
			assertEquals(13, shape.getX2());
			assertEquals(9, shape.getY2());
		}
	}

	@DisplayName("set(int,int,int,int)")
	@Nested
	public class SetIntIntIntInt {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.set(3, 4, 5, 6);
			assertEquals(3, shape.getX1());
			assertEquals(4, shape.getY1());
			assertEquals(5, shape.getX2());
			assertEquals(6, shape.getY2());
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
			shape.set(createPoint(3,  4), createPoint(5, 6));
			assertEpsilonEquals(3, shape.getX1());
			assertEpsilonEquals(4, shape.getY1());
			assertEpsilonEquals(5, shape.getX2());
			assertEpsilonEquals(6, shape.getY2());
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
			B bb = shape.toBoundingBox();
			assertEpsilonEquals(0, bb.getMinX());
			assertEpsilonEquals(0, bb.getMinY());
			assertEpsilonEquals(10, bb.getMaxX());
			assertEpsilonEquals(5, bb.getMaxY());
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
			assertTrue(shape.contains(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.contains(createPoint(10, 5)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createPoint(1, 1)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createPoint(2, 4)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createPoint(2, 2)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.contains(createPoint(1, 0)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createPoint(5, 3)));
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.contains(createPoint(5, 2)));
		}

		@DisplayName("(int,int) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.contains(0, 0));
		}

		@DisplayName("(int,int) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.contains(10, 5));
		}

		@DisplayName("(int,int) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(1, 1));
		}

		@DisplayName("(int,int) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(2, 4));
		}

		@DisplayName("(int,int) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(2, 2));
		}

		@DisplayName("(int,int) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.contains(1, 0));
		}

		@DisplayName("(int,int) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(5, 3));
		}

		@DisplayName("(int,int) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.contains(5, 2));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createRectangle(0,0,1,1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createRectangle(0,0,8,1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createRectangle(0,0,8,6)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createRectangle(0,0,100,100)));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createRectangle(7,10,1,1)));
		}

		@DisplayName("(Rectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createRectangle(16,0,100,100)));
		}

		@DisplayName("(Rectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createRectangle(0,3,3,10)));
		}

		@DisplayName("(Rectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape = (T) createSegment(0, 0, 10, 0);
			assertFalse(shape.contains(createRectangle(0,0,2,0)));
		}

		@DisplayName("(Shape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createCircle(0,0,1)));
		}

		@DisplayName("(Shape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createCircle(0,0,8)));
		}

		@DisplayName("(Shape2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createCircle(0,0,6)));
		}

		@DisplayName("(Shape2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createCircle(0,0,100)));
		}

		@DisplayName("(Shape2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		}

		@DisplayName("(Shape2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createCircle(7,10,1)));
		}

		@DisplayName("(Shape2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createCircle(16,0,100)));
		}

		@DisplayName("(Shape2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createCircle(0,3,3)));
		}

		@DisplayName("(Shape2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createCircle(0,3,10)));
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
			var p = shape.getClosestPointTo(createPoint(0,0));
			assertIntPointEquals(0, 0, p);
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getClosestPointTo(createPoint(1,1));
			assertIntPointEquals(1, 0, p);
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getClosestPointTo(createPoint(2,2));
			assertIntPointEquals(2, 1, p);
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getClosestPointTo(createPoint(-2,2));
			assertIntPointEquals(0, 0, p);
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getClosestPointTo(createPoint(0,1));
			assertIntPointEquals(0, 0, p);
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getClosestPointTo(createPoint(10,-1));
			assertIntPointEquals(7, 3, p);
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getClosestPointTo(createPoint(2,4));
			assertIntPointEquals(4, 2, p);
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(shape, createCircle(0, 0, 2));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(6, 3, shape.getClosestPointTo(createCircle(0, 15, 2)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(10, 5, shape.getClosestPointTo(createCircle(15, 0, 2)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(6, 3, shape.getClosestPointTo(createCircle(4, 5, 2)));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(shape, createSegment(0, 0, 5, -2));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(10, 5, shape.getClosestPointTo(createSegment(0, 15, 5, 13)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(10, 5, shape.getClosestPointTo(createSegment(15, 0, 20, 13)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(7, 3, shape.getClosestPointTo(createSegment(4, 5, 9, 3)));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(shape, createRectangle(0, 0, 3, 2));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(8, 4, shape.getClosestPointTo(createRectangle(2, 11, 3, 2)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(5, 2, shape.getClosestPointTo(createRectangle(2, 3, 3, 2)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(shape, createRectangle(3, 3, 3, 2));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(9, 4, shape.getClosestPointTo(createRectangle(15, -10, 3, 2)));
		}

		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(shape, createTestMultiShape(0, 0));
		}

		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(0, 0, shape.getClosestPointTo(createTestMultiShape(0, 15)));
		}

		@DisplayName("(MultiShape2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(5, 2, shape.getClosestPointTo(createTestMultiShape(15, 0)));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(shape, createTestPath(0, 0));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(8, 4, shape.getClosestPointTo(createTestPath(0, 15)));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(10, 5, shape.getClosestPointTo(createTestPath(15, 0)));
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
			var p = shape.getFarthestPointTo(createPoint(0,0));
			assertEquals(10, p.ix());
			assertEquals(5, p.iy());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getFarthestPointTo(createPoint(1,1));
			assertEquals(10, p.ix());
			assertEquals(5, p.iy());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getFarthestPointTo(createPoint(2,2));
			assertEquals(10, p.ix());
			assertEquals(5, p.iy());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getFarthestPointTo(createPoint(-2,2));
			assertEquals(10, p.ix());
			assertEquals(5, p.iy());
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getFarthestPointTo(createPoint(0,1));
			assertEquals(10, p.ix());
			assertEquals(5, p.iy());
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getFarthestPointTo(createPoint(10,-1));
			assertEquals(0, p.ix());
			assertEquals(0, p.iy());
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getFarthestPointTo(createPoint(2,4));
			assertEquals(10, p.ix());
			assertEquals(5, p.iy());
		}
	}

	@DisplayName("getPathIterator")
	@Nested
	public class GetPathIterator {

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathIterator2ai pi = shape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 10, 5);
			assertNoElement(pi);
		}
	}

	@DisplayName("getPathIterator(Transform2D)")
	@Nested
	public class GetPathIteratorTransform2D {

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			var pi = shape.getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 0,0);
			assertElement(pi, PathElementType.LINE_TO, 10,5);
			assertNoElement(pi);
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeTranslationMatrix(3.4f, 4.5f);
			var pi = shape.getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 3, 5);
			assertElement(pi, PathElementType.LINE_TO, 13, 10);
			assertNoElement(pi);
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeRotationMatrix(MathConstants.QUARTER_PI);
			var pi = shape.getPathIterator(tr); 
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 4, 11);
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
			var s = (T) shape.createTransformedShape(tr);
			assertEquals(0, s.getX1());
			assertEquals(0, s.getY1());
			assertEquals(10, s.getX2());
			assertEquals(5, s.getY2());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setTranslation(3.4f, 4.5f);
			var s = (T) shape.createTransformedShape(tr);
			assertEquals(3, s.getX1());
			assertEquals(5, s.getY1());
			assertEquals(13, s.getX2());
			assertEquals(10, s.getY2());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setRotation(MathConstants.PI);
			var s = (T) shape.createTransformedShape(tr);
			assertEquals(0, s.getX1());
			assertEquals(0, s.getY1());
			assertEquals(-10, s.getX2());
			assertEquals(-5, s.getY2());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setRotation(MathConstants.QUARTER_PI);
			var s = (T) shape.createTransformedShape(tr);
			assertEquals(0, s.getX1());
			assertEquals(0, s.getY1());
			assertEquals(4, s.getX2());
			assertEquals(11, s.getY2());
		}
	}

	@DisplayName("transform(Transform2D)")
	@Nested
	public class TransformTransform2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			var s = shape.clone();
			s.transform(tr);
			assertEquals(0, s.getX1());
			assertEquals(0, s.getY1());
			assertEquals(10, s.getX2());
			assertEquals(5, s.getY2());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			var tr = new Transform2D();
			tr.makeTranslationMatrix(3.4f, 4.5f);
			var s = shape.clone();
			s.transform(tr);
			assertEquals(3, s.getX1());
			assertEquals(5, s.getY1());
			assertEquals(13, s.getX2());
			assertEquals(10, s.getY2());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			var tr = new Transform2D();
			tr.makeRotationMatrix(MathConstants.PI);
			var s = shape.clone();
			s.transform(tr);
			assertEquals(0, s.getX1());
			assertEquals(0, s.getY1());
			assertEquals(-10, s.getX2());
			assertEquals(-5, s.getY2());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			var tr = new Transform2D();
			tr.makeRotationMatrix(MathConstants.QUARTER_PI);
			var s = shape.clone();
			s.transform(tr);
			assertEquals(0, s.getX1());
			assertEquals(0, s.getY1());
			assertEquals(4, s.getX2());
			assertEquals(11, s.getY2());
		}
	}

	@DisplayName("getPointIterator")
	@Nested
	public class GetPointIterator {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D p;
			Iterator<? extends Point2D> iterator = shape.getPointIterator();

			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(0, p.ix());
			assertEquals(0, p.iy());

			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(1, p.ix());
			assertEquals(0, p.iy());

			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(2, p.ix());
			assertEquals(1, p.iy());

			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(3, p.ix());
			assertEquals(1, p.iy());

			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(4, p.ix());
			assertEquals(2, p.iy());

			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(2, p.iy());

			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(6, p.ix());
			assertEquals(3, p.iy());

			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(7, p.ix());
			assertEquals(3, p.iy());

			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(8, p.ix());
			assertEquals(4, p.iy());

			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(9, p.ix());
			assertEquals(4, p.iy());

			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(10, p.ix());
			assertEquals(5, p.iy());

			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("intersectsSegmentSegment")
	@Nested
	public class IntersectsSegmentSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2ai.intersectsSegmentSegment(0, 0, 10, 5, 0, 0, 10, 5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2ai.intersectsSegmentSegment(0, 0, 10, 5, 0, 0, 5, 2));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2ai.intersectsSegmentSegment(0, 0, 10, 5, 0, 1, 5, 3));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2ai.intersectsSegmentSegment(0, 0, 10, 5, 0, 2, 5, 4));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2ai.intersectsSegmentSegment(0, 0, 10, 5, 5, 0, 4, 3));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2ai.intersectsSegmentSegment(0, 0, 10, 5, -1, 5, -1, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2ai.intersectsSegmentSegment(5, 3, 7, 5, 6, 2, 6, 5));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2ai.intersectsSegmentSegment(5, 3, 7, 5, 9, 4, 6, 6));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment2ai.intersectsSegmentSegment(5, 3, 7, 5, 9, 4, 6, 7));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment2ai.intersectsSegmentSegment(5, 3, 7, 5, 6, 4, 6, 8));
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
			assertEquals(0, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, -1, -1, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 4, -2, 4, 10));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(2, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 6, -2, 6, 10));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 5, -2, 5, 10));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 6, 10, 6, -2));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 5, 10, 5, -2));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(2, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 10, -5, 127, 345));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 127, 345, 10, -5));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 127, 3, 200, 345));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 127, 345, 200, 3));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 10, 1, 12, 3));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 12, 3, 10, 1));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 10, 3, 12, 5));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 12, 5, 10, 3));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2ai.calculatesCrossingsPointShadowSegment(0, 4, -1, 7, -5, 0, 0));
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
			int[] data = new int[] {
					-1, -1, 0, 0, 				0,
					-1, 0, 10, 0,				0,
					10, -2, 12, 4,				1,
					12, 4, 50, 10,				1,
					10, -2, 50, 10,				2,
					10, 3, 12, 4,				0,
					12, 4, 50, 5,				0,
					12, 3, 50, 5,				0,
					12, 5, 50, 5,				0,
					12, 4, 50, 10,				1,
					12, 3, 50, 10,				1,
					12, 5, 50, 10,				1,
					0, 5, 3, 7,					0,
					6, 2, 6, 4,					SHAPE_INTERSECTS,
					6, 4, 6, 8,					SHAPE_INTERSECTS,
					7, 4, 7, 8,					SHAPE_INTERSECTS,
					5, 4, 5, 8,					SHAPE_INTERSECTS,
					4, 4, 6, 6,					SHAPE_INTERSECTS,
					6, 6, 8, 4,					SHAPE_INTERSECTS,
					5, 4, 7, 4,					SHAPE_INTERSECTS,
					0, 4, 7, 4,					SHAPE_INTERSECTS,
					6, 6, 12, 8,				0,
					6, 2, 12, -3,				0,
			};

			String label;
			for(int i=0; i<data.length;) {
				int x1 = data[i++];
				int y1 = data[i++];
				int x2 = data[i++];
				int y2 = data[i++];
				int crossing = data[i++];

				label = x1+";"+y1+";"+x2+";"+y2;   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				assertEquals(crossing, Segment2ai.calculatesCrossingsRectangleShadowSegment(0, 5, 3, 7, 5, x1, y1, x2, y2), label);

				label = x2+";"+y2+";"+x1+";"+y1;   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				if (crossing!=SHAPE_INTERSECTS) {
					crossing = -crossing;
				}
				assertEquals(crossing, Segment2ai.calculatesCrossingsRectangleShadowSegment(0, 5, 3, 7, 5, x2, y2, x1, y1), label);
			}
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
			assertEquals(0, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ -1, -1, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ -1, 0, 10, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 10, -2, 12, 4));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 12, 4, 50, 10));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(2, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 10, -2, 50, 10));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 10, 3, 12, 4));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 12, 4, 50, 5));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 12, 3, 50, 5));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1,
					Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 2, 6, 3));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 2, 6, 5));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 2, 6, 4));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 4, 6, 8));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 3, 6, 8));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 7, 4, 7, 8));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0,
					Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 5, 4, 5, 8));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0,
					Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 4, 4, 6, 6));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0,
					Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 4, 3, 6, 5));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 8, 4, 6, 6));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1,
					Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 10, 4, 6, 8));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1,
					Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 8, 10, 4));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 5, 4, 100, 6));
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
			assertEquals(0, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ -1, -1, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ -1, 0, 10, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 10, -2, 12, 4));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 12, 4, 50, 10));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(2, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 10, -2, 50, 10));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 10, 3, 12, 4));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 12, 4, 50, 5));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 12, 3, 50, 5));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 6, 2, 6, 3));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 6, 2, 6, 5));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 7, 2, 7, 4));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 6, 2, 6, 4));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 6, 4, 6, 8));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 6, 3, 6, 8));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 7, 4, 7, 8));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 5, 4, 5, 8));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 4, 4, 6, 6));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 4, 3, 6, 5));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 8, 4, 6, 6));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 10, 4, 6, 8));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 6, 8, 10, 4));
		}

		@DisplayName("#22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 7, 8, 10, 12));
		}

		@DisplayName("#23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1,
					Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 10, 12, 7, 8));
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
			shape.set((T) createSegment(10, 12, 14, 16));
			assertEquals(10, shape.getX1());
			assertEquals(12, shape.getY1());
			assertEquals(14, shape.getX2());
			assertEquals(16, shape.getY2());
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
			shape.setX1(145);
			assertEquals(145, shape.getX1());
			assertEquals(0, shape.getY1());
			assertEquals(10, shape.getX2());
			assertEquals(5, shape.getY2());
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
			shape.setY1(145);
			assertEquals(0, shape.getX1());
			assertEquals(145, shape.getY1());
			assertEquals(10, shape.getX2());
			assertEquals(5, shape.getY2());
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
			shape.setX2(145);
			assertEquals(0, shape.getX1());
			assertEquals(0, shape.getY1());
			assertEquals(145, shape.getX2());
			assertEquals(5, shape.getY2());
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
			shape.setY2(145);
			assertEquals(0, shape.getX1());
			assertEquals(0, shape.getY1());
			assertEquals(10, shape.getX2());
			assertEquals(145, shape.getY2());
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
			assertEquals(0, shape.getX1());
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
			assertEquals(0, shape.getY1());
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
			assertEquals(10, shape.getX2());
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
			assertEquals(5, shape.getY2());
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
			Point2D p = shape.getP1();
			assertEquals(0, p.ix());
			assertEquals(0, p.iy());
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
			Point2D p = shape.getP2();
			assertEquals(10, p.ix());
			assertEquals(5, p.iy());
		}
	}

	@DisplayName("findsClosestPointSegmentPoint")
	@Nested
	public class FindsClosestPointSegmentPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Segment2ai.findsClosestPointSegmentPoint(0, 0, 10, 5, 0, 0, p);
			assertEquals(0, p.ix());
			assertEquals(0, p.iy());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Segment2ai.findsClosestPointSegmentPoint(0, 0, 10, 5, 1, 1, p);
			assertEquals(1, p.ix());
			assertEquals(0, p.iy());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Segment2ai.findsClosestPointSegmentPoint(0, 0, 10, 5, 2, 2, p);
			assertEquals(2, p.ix());
			assertEquals(1, p.iy());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Segment2ai.findsClosestPointSegmentPoint(0, 0, 10, 5, -2, 2, p);
			assertEquals(0, p.ix());
			assertEquals(0, p.iy());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Segment2ai.findsClosestPointSegmentPoint(0, 0, 10, 5, 0, 1, p);
			assertEquals(0, p.ix());
			assertEquals(0, p.iy());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Segment2ai.findsClosestPointSegmentPoint(0, 0, 10, 5, 10, -1, p);
			assertEquals(7, p.ix());
			assertEquals(3, p.iy());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Segment2ai.findsClosestPointSegmentPoint(0, 0, 10, 5, 2, 4, p);
			assertEquals(4, p.ix());
			assertEquals(2, p.iy());
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
			var p = createPoint(0, 0);
			Segment2ai.findsFarthestPointSegmentPoint(0, 0, 10, 5, 0, 0, p);
			assertIntPointEquals(10, 5, p);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Segment2ai.findsFarthestPointSegmentPoint(0, 0, 10, 5, 1, 1, p);
			assertIntPointEquals(10, 5, p);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Segment2ai.findsFarthestPointSegmentPoint(0, 0, 10, 5, 2, 2, p);
			assertIntPointEquals(10, 5, p);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Segment2ai.findsFarthestPointSegmentPoint(0, 0, 10, 5, -2, 2, p);
			assertIntPointEquals(10, 5, p);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Segment2ai.findsFarthestPointSegmentPoint(0, 0, 10, 5, 0, 1, p);
			assertIntPointEquals(10, 5, p);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Segment2ai.findsFarthestPointSegmentPoint(0, 0, 10, 5, 10, -1, p);
			assertIntPointEquals(0, 0, p);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Segment2ai.findsFarthestPointSegmentPoint(0, 0, 10, 5, 2, 4, p);
			assertIntPointEquals(10, 5, p);
		}
	}

	@DisplayName("findsClosestPointsSegmentSegment")
	@Nested
	public class FindsClosestPointsSegmentSegment {

		private Point2D p1;
		private Point2D p2;

		@BeforeEach
		public void setUp() {
			p1 = createPoint(0, 0);
			p2 = createPoint(0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2ai.findsClosestPointsSegmentSegment(0, 0, 10, 5, 0, 0, 1, 15, p1, p2));
			assertIntPointEquals(0, 0, p1);
			assertIntPointEquals(0, 0, p2);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, Segment2ai.findsClosestPointsSegmentSegment(0, 0, 10, 5, 0, 2, 1, 15, p1, p2));
			assertIntPointEquals(0, 0, p1);
			assertIntPointEquals(0, 2, p2);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(58, Segment2ai.findsClosestPointsSegmentSegment(0, 0, 10, 5, 0, 15, 5, 11, p1, p2));
			assertIntPointEquals(8, 4, p1);
			assertIntPointEquals(5, 11, p2);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, Segment2ai.findsClosestPointsSegmentSegment(0, 0, 10, 5, 0, 2, 10, 17, p1, p2));
			assertIntPointEquals(0, 0, p1);
			assertIntPointEquals(0, 2, p2);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2ai.findsClosestPointsSegmentSegment(0, 0, 10, 5, 0, 15, 10, 0, p1, p2));
			assertIntPointEquals(8, 4, p1);
			assertIntPointEquals(7, 4, p2);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment2ai.findsClosestPointsSegmentSegment(2, 2, 3, 1, 3, -1, 8, 8, p1, p2));
			assertIntPointEquals(3, 1, p1);
			assertIntPointEquals(4, 1, p2);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2ai.findsClosestPointsSegmentSegment(0, 0, 2, 2, 3, -1, 1, 1, p1, p2));
			assertIntPointEquals(1, 1, p1);
			assertIntPointEquals(1, 1, p2);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(89, Segment2ai.findsClosestPointsSegmentSegment(0, 0, 10, 5, 0, 15, 5, 13, p1, p2));
			assertIntPointEquals(10, 5, p1);
			assertIntPointEquals(5, 13, p2);
		}
	}

	@DisplayName("findsClosestPointSegmentRectangle")
	@Nested
	public class FindsClosestPointSegmentRectangle {

		private Point2D p1;

		@BeforeEach
		public void setUp() {
			p1 = createPoint(0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment2ai.findsClosestPointSegmentRectangle(0, 0, 10, 5, 0, 0, 5, 2, p1));
			assertIntPointEquals(2, 1, p1);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			p1 = createPoint(0, 0);
			assertEpsilonEquals(0, Segment2ai.findsClosestPointSegmentRectangle(0, 0, 10, 5, 0, 2, 5, 2, p1));
			assertIntPointEquals(4, 2, p1);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			p1 = createPoint(0, 0);
			assertEpsilonEquals(34, Segment2ai.findsClosestPointSegmentRectangle(0, 0, 10, 5, 15, 0, 5, 2, p1));
			assertIntPointEquals(10, 5, p1);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			p1 = createPoint(0, 0);
			assertEpsilonEquals(125, Segment2ai.findsClosestPointSegmentRectangle(0, 0, 10, 5, 0, 15, 5, 2, p1));
			assertIntPointEquals(10, 5, p1);
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
			assertEquals(0, Segment2ai.findsSideLinePoint(0, 0, 1, 1, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2ai.findsSideLinePoint(0, 0, 1, 1, 1, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2ai.findsSideLinePoint(0, 0, 1, 1, 2, 2));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, Segment2ai.findsSideLinePoint(0, 0, 1, 1, 120, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Segment2ai.findsSideLinePoint(0, 0, 1, 1, -20, -20));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Segment2ai.findsSideLinePoint(0, 0, 1, 1, 0, 1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Segment2ai.findsSideLinePoint(0, 0, 1, 1, 0, 120));
		}
	}

	@DisplayName("setP1")
	@Nested
	public class SetP1 {

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setP1(createPoint(145, 654));
			assertEquals(145, shape.getX1());
			assertEquals(654, shape.getY1());
			assertEquals(10, shape.getX2());
			assertEquals(5, shape.getY2());
		}

		@DisplayName("(int,int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setP1(145, 654);
			assertEquals(145, shape.getX1());
			assertEquals(654, shape.getY1());
			assertEquals(10, shape.getX2());
			assertEquals(5, shape.getY2());
		}
	}

	@DisplayName("setP2")
	@Nested
	public class SetP2 {

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setP2(createPoint(145, 654));
			assertEquals(0, shape.getX1());
			assertEquals(0, shape.getY1());
			assertEquals(145, shape.getX2());
			assertEquals(654, shape.getY2());
		}

		@DisplayName("(int,int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setP2(145, 654);
			assertEquals(0, shape.getX1());
			assertEquals(0, shape.getY1());
			assertEquals(145, shape.getX2());
			assertEquals(654, shape.getY2());
		}
	}

	@DisplayName("toBoundingBox(B)")
	@Nested
	public class ToBoundingBoxB {

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B bb = createRectangle(0, 0, 0, 0);
			shape.toBoundingBox(bb);
			assertEpsilonEquals(0, bb.getMinX());
			assertEpsilonEquals(0, bb.getMinY());
			assertEpsilonEquals(10, bb.getMaxX());
			assertEpsilonEquals(5, bb.getMaxY());
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
			assertTrue(shape.clipToRectangle(3, 1, 7, 6));
			assertEquals(3, shape.getX1());
			assertEquals(1, shape.getY1());
			assertEquals(7, shape.getX2());
			assertEquals(3, shape.getY2());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape = createShape();
			assertTrue(shape.clipToRectangle(8, 3, 11, 7));
			assertEquals(8, shape.getX1());
			assertEquals(4, shape.getY1());
			assertEquals(10, shape.getX2());
			assertEquals(5, shape.getY2());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape = createShape();
			assertFalse(shape.clipToRectangle(0, 3, 5, 4));
			assertEquals(0, shape.getX1());
			assertEquals(0, shape.getY1());
			assertEquals(10, shape.getX2());
			assertEquals(5, shape.getY2());
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
			assertTrue(shape.intersects(createRectangle(0,0,1,1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createRectangle(0,0,8,1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createRectangle(0,0,8,6)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createRectangle(0,0,100,100)));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createRectangle(7,10,1,1)));
		}

		@DisplayName("(Rectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createRectangle(16,0,100,100)));
		}

		@DisplayName("(Rectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createRectangle(0,3,3,10)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createCircle(0,0,1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createCircle(0,0,8)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createCircle(0,0,8)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createCircle(0,0,100)));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createCircle(7,10,1)));
		}

		@DisplayName("(Circle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createCircle(16,0,100)));
		}

		@DisplayName("(Circle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createCircle(0,3,3)));
		}

		@DisplayName("(Circle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createCircle(0,3,1)));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createSegment(0,0,1,10)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createSegment(0,0,8,0)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createSegment(0,0,8,-1)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createSegment(0,0,100,-100)));
		}

		@DisplayName("(Segment2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createSegment(7,10,1,3)));
		}

		@DisplayName("(Segment2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createSegment(16,0,100,3)));
		}

		@DisplayName("(Segment2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createSegment(0,3,3,3)));
		}

		@DisplayName("(Segment2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createSegment(0,3,5,3)));
		}

		@DisplayName("(Segment2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createSegment(0,3,6,3)));
		}

		@DisplayName("(Segment2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createSegment(0,3,1,2)));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(0, 0, 1, 1).intersects(path));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(4, 3, 1, 1).intersects(path));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(2, 2, 1, 1).intersects(path));
		}

		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(2, 1, 1, 1).intersects(path));
		}

		@DisplayName("(Path2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(3, 0, 1, 1).intersects(path));
		}

		@DisplayName("(Path2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(-1, -1, 1, 1).intersects(path));
		}

		@DisplayName("(Path2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(4, -3, 1, 1).intersects(path));
		}

		@DisplayName("(Path2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(-3, 4, 1, 1).intersects(path));
		}

		@DisplayName("(Path2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(6, -5, 1, 1).intersects(path));
		}

		@DisplayName("(Path2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(4, 0, 1, 1).intersects(path));
		}

		@DisplayName("(Path2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(5, 0, 1, 1).intersects(path));
		}

		@DisplayName("(Path2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertFalse(createSegment(-4, -4, -3, -3).intersects(path));
		}

		@DisplayName("(Path2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertFalse(createSegment(-1, 0, 2, 3).intersects(path));
		}

		@DisplayName("(Path2afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertFalse(createSegment(7, 1, 18, 14).intersects(path));
		}

		@DisplayName("(PathIterator2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(0, 0, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(4, 3, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(2, 2, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(2, 1, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(3, 0, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(-1, -1, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(4, -3, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(-3, 4, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(6, -5, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(4, 0, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(createSegment(5, 0, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertFalse(createSegment(-4, -4, -3, -3).intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertFalse(createSegment(-1, 0, 2, 3).intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertFalse(createSegment(7, 1, 18, 14).intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(Shape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects((Shape2D) createCircle(16,0,100)));
		}

		@DisplayName("(Shape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects((Shape2D) createRectangle(0,0,100,100)));
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
			shape.operator_add(createVector(3, 4));
			assertEquals(3, shape.getX1());
			assertEquals(4, shape.getY1());
			assertEquals(13, shape.getX2());
			assertEquals(9, shape.getY2());
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
			T r = shape.operator_plus(createVector(3, 4));
			assertEquals(3, r.getX1());
			assertEquals(4, r.getY1());
			assertEquals(13, r.getX2());
			assertEquals(9, r.getY2());
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
			shape.operator_remove(createVector(3, 4));
			assertEquals(-3, shape.getX1());
			assertEquals(-4, shape.getY1());
			assertEquals(7, shape.getX2());
			assertEquals(1, shape.getY2());
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
			T r = shape.operator_minus(createVector(3, 4));
			assertEquals(-3, r.getX1());
			assertEquals(-4, r.getY1());
			assertEquals(7, r.getX2());
			assertEquals(1, r.getY2());
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
			var s = (T) shape.operator_multiply(tr);
			assertEquals(0, s.getX1());
			assertEquals(0, s.getY1());
			assertEquals(10, s.getX2());
			assertEquals(5, s.getY2());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setTranslation(3.4f, 4.5f);
			var s = (T) shape.operator_multiply(tr);
			assertEquals(3, s.getX1());
			assertEquals(5, s.getY1());
			assertEquals(13, s.getX2());
			assertEquals(10, s.getY2());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setRotation(MathConstants.PI);
			var s = (T) shape.operator_multiply(tr);
			assertEquals(0, s.getX1());
			assertEquals(0, s.getY1());
			assertEquals(-10, s.getX2());
			assertEquals(-5, s.getY2());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.setRotation(MathConstants.QUARTER_PI);
			var s = (T) shape.operator_multiply(tr);
			assertEquals(0, s.getX1());
			assertEquals(0, s.getY1());
			assertEquals(4, s.getX2());
			assertEquals(11, s.getY2());
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
			assertTrue(shape.operator_and(createPoint(0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.operator_and(createPoint(10, 5)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.operator_and(createPoint(1, 1)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.operator_and(createPoint(2, 4)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.operator_and(createPoint(2, 2)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.operator_and(createPoint(1, 0)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.operator_and(createPoint(5, 3)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.operator_and(createPoint(5, 2)));
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
			assertTrue(shape.operator_and(createCircle(16,0,100)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.operator_and(createRectangle(0,0,100,100)));
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
			assertEpsilonEquals(0f, shape.operator_upTo(createPoint(0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, shape.operator_upTo(createPoint(1, 1)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.828427125f, shape.operator_upTo(createPoint(2, 4)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, shape.operator_upTo(createPoint(2, 2)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.071067812f, shape.operator_upTo(createPoint(-5, 5)));
		}
	}

}