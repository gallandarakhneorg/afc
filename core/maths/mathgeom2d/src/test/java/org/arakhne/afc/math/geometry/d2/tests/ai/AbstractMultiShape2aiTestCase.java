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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;

import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.MultiShape2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.arakhne.afc.math.geometry.d2.ai.Shape2ai;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractMultiShape2aiTestCase<T extends MultiShape2ai<?, T, C, ?, ?, ?, B>,
C extends Shape2ai<?, ?, ?, ?, ?, B>,
B extends Rectangle2ai<?, ?, ?, ?, ?, B>> extends AbstractShape2aiTestCase<T, B> {

	private C firstObject;
	private C secondObject;

	@Override
	protected final T createShape() {
		T shape = (T) createMultiShape();
		firstObject = (C) createRectangle(5, 8, 2, 1);
		secondObject = (C) createCircle(-5, 18, 2);
		shape.add(firstObject);
		shape.add(secondObject);
		return shape;
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
			MultiShape2ai clone = getS().clone();
			PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 5, 8);
			assertElement(pi, PathElementType.LINE_TO, 7, 8);
			assertElement(pi, PathElementType.LINE_TO, 7, 9);
			assertElement(pi, PathElementType.LINE_TO, 5, 9);
			assertElement(pi, PathElementType.CLOSE, 5, 8);
			assertElement(pi, PathElementType.MOVE_TO, -3, 18);
			assertElement(pi, PathElementType.CURVE_TO, -3, 19, -3, 20, -5, 20);
			assertElement(pi, PathElementType.CURVE_TO, -6, 20, -7, 19, -7, 18);
			assertElement(pi, PathElementType.CURVE_TO, -7, 16, -6, 16, -5, 16);
			assertElement(pi, PathElementType.CURVE_TO, -3, 16, -3, 16, -3, 18);
			assertElement(pi, PathElementType.CLOSE, -3, 18);
			assertNoElement(pi);
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
			assertFalse(getS().equals(createMultiShape()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSegment(5, 8, 5, 10)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(getS()));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(getS().clone()));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createMultiShape().getPathIterator()));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSegment(5, 8, 5, 10).getPathIterator()));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(getS().getPathIterator()));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(getS().clone().getPathIterator()));
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
			assertFalse(getS().equalsToPathIterator(createMultiShape().getPathIterator()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToPathIterator(createSegment(5, 8, 5, 10).getPathIterator()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToPathIterator(getS().getPathIterator()));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToPathIterator(getS().clone().getPathIterator()));
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
			assertFalse(getS().equalsToShape((T) createMultiShape()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape(getS()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape(getS().clone()));
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
			PathIterator2ai pi = getS().getPathIterator();
			assertNoElement(pi);
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
			assertFalse(getS().contains(-10, 2));
		}

		@DisplayName("(double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-10, 14));
		}

		@DisplayName("(double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-10, 25));
		}

		@DisplayName("(double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-1, 25));
		}

		@DisplayName("(double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(1, 2));
		}

		@DisplayName("(double,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(12, 2));
		}

		@DisplayName("(double,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(12, 14));
		}

		@DisplayName("(double,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(12, 25));
		}

		@DisplayName("(double,double) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-6, 8));
		}

		@DisplayName("(double,double) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(4, 17));
		}

		@DisplayName("(double,double) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(-4, 19));
		}

		@DisplayName("(double,double) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(6, 8));
		}

		@DisplayName("(Point2D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(-10, 2)));
		}

		@DisplayName("(Point2D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_14(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createPoint(-10, 14)));
		}

		@DisplayName("(Point2D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_15(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createPoint(-10, 25)));
		}

		@DisplayName("(Point2D) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_16(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createPoint(-1, 25)));
		}

		@DisplayName("(Point2D) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_17(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createPoint(1, 2)));
		}

		@DisplayName("(Point2D) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_18(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createPoint(12, 2)));
		}

		@DisplayName("(Point2D) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_19(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createPoint(12, 14)));
		}

		@DisplayName("(Point2D) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_20(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createPoint(12, 25)));
		}

		@DisplayName("(Point2D) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_21(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createPoint(-6, 8)));
		}

		@DisplayName("(Point2D) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_22(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createPoint(4, 17)));
		}

		@DisplayName("(Point2D) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_23(CoordinateSystem2D cs) {
			assertTrue(getS().contains(createPoint(-4, 19)));
		}

		@DisplayName("(Point2D) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_24(CoordinateSystem2D cs) {
			assertTrue(getS().contains(createPoint(6, 8)));
		}

		@DisplayName("(Rectangle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createRectangle(-20, 14, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createRectangle(-2,-10, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createRectangle(-6,16, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createRectangle(4, 8, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			assertTrue(getS().contains(createRectangle(5, 8, 1, 1)));
		}

		@DisplayName("(Shape2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_1(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createCircle(-20, 14, 1)));
		}

		@DisplayName("(Shape2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_2(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createCircle(-2,-10, 1)));
		}

		@DisplayName("(Shape2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_3(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createCircle(-6,16, 1)));
		}

		@DisplayName("(Shape2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_4(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createCircle(4, 8, 1)));
		}

		@DisplayName("(Shape2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_5(CoordinateSystem2D cs) {
			assertFalse(getS().contains(createCircle(5, 8, 1)));
		}

		@DisplayName("(Shape2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_6(CoordinateSystem2D cs) {
			assertTrue(getS().contains(createCircle(-4, 18, 0)));
		}
	}

	@DisplayName("getClosestPointTo")
	@Nested
	public class GetClosestPointTo {

		private Point2D result;

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-10, 2));
			assertEpsilonEquals(-6, result.ix());
			assertEpsilonEquals(16, result.iy());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-10, 14));
			assertEpsilonEquals(-6, result.ix());
			assertEpsilonEquals(16, result.iy());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-10, 25));
			assertEpsilonEquals(-6, result.ix());
			assertEpsilonEquals(20, result.iy());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-1, 25));
			assertEpsilonEquals(-4, result.ix());
			assertEpsilonEquals(20, result.iy());
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(1, 2));
			assertEpsilonEquals(5, result.ix());
			assertEpsilonEquals(8, result.iy());
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(12, 2));
			assertEpsilonEquals(7, result.ix());
			assertEpsilonEquals(8, result.iy());
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(12, 14));
			assertEpsilonEquals(7, result.ix());
			assertEpsilonEquals(9, result.iy());
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(12, 25));
			assertEpsilonEquals(7, result.ix());
			assertEpsilonEquals(9, result.iy());
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-6, 8));
			assertEpsilonEquals(-6, result.ix());
			assertEpsilonEquals(16, result.iy());
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(4, 17));
			assertEpsilonEquals(-3, result.ix());
			assertEpsilonEquals(17, result.iy());
		}

		@DisplayName("(Point2D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-4, 19));
			assertEpsilonEquals(-4, result.ix());
			assertEpsilonEquals(19, result.iy());
		}

		@DisplayName("(Point2D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(6, 8));
			assertEpsilonEquals(6, result.ix());
			assertEpsilonEquals(8, result.iy());
		}

		@DisplayName("(Circle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(-5, 16, getS().getClosestPointTo(createCircle(-5, 9, 2)));
		}

		@DisplayName("(Circle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(5, 9, 2));
		}

		@DisplayName("(Circle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(-8, 17, 2));
		}

		@DisplayName("(Circle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(7, 9, getS().getClosestPointTo(createCircle(15, 10, 2)));
		}

		@DisplayName("(Segment2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(-4, 16, getS().getClosestPointTo(createSegment(-5, 9, 2, 21)));
		}

		@DisplayName("(Segment2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(5, 9, 2, 21));
		}

		@DisplayName("(Segment2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(-8, 17, 2, 21));
		}

		@DisplayName("(Segment2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(7, 9, getS().getClosestPointTo(createSegment(15, 10, 2, 45)));
		}

		@DisplayName("(Rectangle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(-5, 16, getS().getClosestPointTo(createRectangle(-5, 9, 2, 2)));
		}

		@DisplayName("(Rectangle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(5, 9, 2, 2));
		}

		@DisplayName("(Rectangle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(-8, 17, 2, 2));
		}

		@DisplayName("(Rectangle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(7, 9, getS().getClosestPointTo(createRectangle(15, 10, 2, 2)));
		}

		@DisplayName("(MultiShape2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(-4, 16, getS().getClosestPointTo(createTestMultiShape(-5, 9)));
		}

		@DisplayName("(MultiShape2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(7, 9, getS().getClosestPointTo(createTestMultiShape(5, 9)));
		}

		@DisplayName("(MultiShape2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestMultiShape(-8, 17));
		}

		@DisplayName("(MultiShape2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestMultiShape(15, 10));
		}

		@DisplayName("(Path2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(6, 8, getS().getClosestPointTo(createTestPath(-5, 9)));
		}

		@DisplayName("(Path2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(-5, 18, getS().getClosestPointTo(createTestPath(-8, 17)));
		}

		@DisplayName("(Path2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(5, 9));
		}

		@DisplayName("(Path2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(7, 9, getS().getClosestPointTo(createTestPath(15, 10)));
		}

	}

	@DisplayName("getFarthestPointTo")
	@Nested
	public class GetFarthestPointTo {

		private Point2D result;

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(-10, 2));
			assertEpsilonEquals(-4, result.ix());
			assertEpsilonEquals(20, result.iy());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(-10, 14));
			assertEpsilonEquals(7, result.ix());
			assertEpsilonEquals(8, result.iy());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(-10, 25));
			assertEpsilonEquals(7, result.ix());
			assertEpsilonEquals(8, result.iy());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(-1, 25));
			assertEpsilonEquals(7, result.ix());
			assertEpsilonEquals(8, result.iy());
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(1, 2));
			assertEpsilonEquals(-6, result.ix());
			assertEpsilonEquals(20, result.iy());
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(12, 2));
			assertEpsilonEquals(-7, result.ix());
			assertEpsilonEquals(19, result.iy());
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(12, 14));
			assertEpsilonEquals(-7, result.ix());
			assertEpsilonEquals(19, result.iy());
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(12, 25));
			assertEpsilonEquals(-7, result.ix());
			assertEpsilonEquals(17, result.iy());
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(-6, 8));
			assertEpsilonEquals(7, result.ix());
			assertEpsilonEquals(9, result.iy());
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(4, 17));
			assertEpsilonEquals(-7, result.ix());
			assertEpsilonEquals(19, result.iy());
		}

		@DisplayName("(Point2D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(-4, 19));
			assertEpsilonEquals(7, result.ix());
			assertEpsilonEquals(8, result.iy());
		}

		@DisplayName("(Point2D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(6, 8));
			assertEpsilonEquals(-7, result.ix());
			assertEpsilonEquals(19, result.iy());
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
			assertEpsilonEquals(14.5602, getS().getDistance(createPoint(-10, 2)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.4721, getS().getDistance(createPoint(-10, 14)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.4031, getS().getDistance(createPoint(-10, 25)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.831, getS().getDistance(createPoint(-1, 25)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.2111, getS().getDistance(createPoint(1, 2)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.8102, getS().getDistance(createPoint(12, 2)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.0711, getS().getDistance(createPoint(12, 14)));
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16.7631, getS().getDistance(createPoint(12, 25)));
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8, getS().getDistance(createPoint(-6, 8)));
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7, getS().getDistance(createPoint(4, 17)));
		}

		@DisplayName("(Point2D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createPoint(-4, 19)));
		}

		@DisplayName("(Point2D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createPoint(6, 8)));
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
			assertEpsilonEquals(212, getS().getDistanceSquared(createPoint(-10, 2)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(20, getS().getDistanceSquared(createPoint(-10, 14)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(41, getS().getDistanceSquared(createPoint(-10, 25)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(34, getS().getDistanceSquared(createPoint(-1, 25)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(52, getS().getDistanceSquared(createPoint(1, 2)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(61, getS().getDistanceSquared(createPoint(12, 2)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(50, getS().getDistanceSquared(createPoint(12, 14)));
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(281, getS().getDistanceSquared(createPoint(12, 25)));
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(64, getS().getDistanceSquared(createPoint(-6, 8)));
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(49, getS().getDistanceSquared(createPoint(4, 17)));
		}

		@DisplayName("(Point2D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(-4, 19)));
		}

		@DisplayName("(Point2D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(6, 8)));
		}

		@DisplayName("(Circle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25, getS().getDistanceSquared(createCircle(-5, 9, 2)));
		}

		@DisplayName("(Circle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(5, 9, 2)));
		}

		@DisplayName("(Circle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(-8, 17, 2)));
		}

		@DisplayName("(Circle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(36, getS().getDistanceSquared(createCircle(15, 10, 2)));
		}

		@DisplayName("(Segment2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getDistanceSquared(createSegment(-5, 9, 2, 21)));
		}

		@DisplayName("(Segment2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(5, 9, 2, 21)));
		}

		@DisplayName("(Segment2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(-8, 17, 2, 21)));
		}

		@DisplayName("(Segment2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(58, getS().getDistanceSquared(createSegment(15, 10, 2, 45)));
		}

		@DisplayName("(Rectangle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25, getS().getDistanceSquared(createRectangle(-5, 9, 2, 2)));
		}

		@DisplayName("(Rectangle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(5, 9, 2, 2)));
		}

		@DisplayName("(Rectangle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(-8, 17, 2, 2)));
		}

		@DisplayName("(Rectangle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(65, getS().getDistanceSquared(createRectangle(15, 10, 2, 2)));
		}

		@DisplayName("(MultiShape2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(17, getS().getDistanceSquared(createTestMultiShape(-5, 9)));
		}

		@DisplayName("(MultiShape2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceSquared(createTestMultiShape(5, 9)));
		}

		@DisplayName("(MultiShape2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(-8, 17)));
		}

		@DisplayName("(MultiShape2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(15, 10)));
		}

		@DisplayName("(Path2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(-5, 9)));
		}

		@DisplayName("(Path2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9, getS().getDistanceSquared(createTestPath(5, 17)));
		}

		@DisplayName("(Path2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(-8, 17)));
		}

		@DisplayName("(Path2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10, getS().getDistanceSquared(createTestPath(15, 10)));
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
			assertEpsilonEquals(18, getS().getDistanceL1(createPoint(-10, 2)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6, getS().getDistanceL1(createPoint(-10, 14)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9, getS().getDistanceL1(createPoint(-10, 25)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8, getS().getDistanceL1(createPoint(-1, 25)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10, getS().getDistanceL1(createPoint(1, 2)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11, getS().getDistanceL1(createPoint(12, 2)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10, getS().getDistanceL1(createPoint(12, 14)));
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(21, getS().getDistanceL1(createPoint(12, 25)));
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8, getS().getDistanceL1(createPoint(-6, 8)));
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7, getS().getDistanceL1(createPoint(4, 17)));
		}

		@DisplayName("(Point2D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(-4, 19)));
		}

		@DisplayName("(Point2D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(6, 8)));
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
			assertEpsilonEquals(14, getS().getDistanceLinf(createPoint(-10, 2)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, getS().getDistanceLinf(createPoint(-10, 14)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getDistanceLinf(createPoint(-10, 25)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getDistanceLinf(createPoint(-1, 25)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6, getS().getDistanceLinf(createPoint(1, 2)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6, getS().getDistanceLinf(createPoint(12, 2)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getDistanceLinf(createPoint(12, 14)));
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16, getS().getDistanceLinf(createPoint(12, 25)));
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8, getS().getDistanceLinf(createPoint(-6, 8)));
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7, getS().getDistanceLinf(createPoint(4, 17)));
		}

		@DisplayName("(Point2D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(-4, 19)));
		}

		@DisplayName("(Point2D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(6, 8)));
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
			getS().set((T) createMultiShape());
			PathIterator2ai pi = getS().getPathIterator();
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2ai newShape = createMultiShape();
			newShape.add(createRectangle(-6, 48, 5, 7));
			getS().set((T) newShape);
			var pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -6, 48);
			assertElement(pi, PathElementType.LINE_TO, -1, 48);
			assertElement(pi, PathElementType.LINE_TO, -1, 55);
			assertElement(pi, PathElementType.LINE_TO, -6, 55);
			assertElement(pi, PathElementType.CLOSE, -6, 48);
			assertNoElement(pi);
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
			getS().translate(10, -2);
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 7);
			assertElement(pi, PathElementType.LINE_TO, 15, 7);
			assertElement(pi, PathElementType.CLOSE, 15, 6);
			assertElement(pi, PathElementType.MOVE_TO, 7, 16);
			assertElement(pi, PathElementType.CURVE_TO, 7, 17, 6, 18, 5, 18);
			assertElement(pi, PathElementType.CURVE_TO, 3, 18, 3, 17, 3, 16);
			assertElement(pi, PathElementType.CURVE_TO, 3, 14, 3, 14, 5, 14);
			assertElement(pi, PathElementType.CURVE_TO, 6, 14, 7, 14, 7, 16);
			assertElement(pi, PathElementType.CLOSE, 7, 16);
			assertNoElement(pi);
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
			getS().translate(createVector(10, -2));
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 7);
			assertElement(pi, PathElementType.LINE_TO, 15, 7);
			assertElement(pi, PathElementType.CLOSE, 15, 6);
			assertElement(pi, PathElementType.MOVE_TO, 7, 16);
			assertElement(pi, PathElementType.CURVE_TO, 7, 17, 6, 18, 5, 18);
			assertElement(pi, PathElementType.CURVE_TO, 3, 18, 3, 17, 3, 16);
			assertElement(pi, PathElementType.CURVE_TO, 3, 14, 3, 14, 5, 14);
			assertElement(pi, PathElementType.CURVE_TO, 6, 14, 7, 14, 7, 16);
			assertElement(pi, PathElementType.CLOSE, 7, 16);
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
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
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
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
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
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 5, 8);
			assertElement(pi, PathElementType.LINE_TO, 7, 8);
			assertElement(pi, PathElementType.LINE_TO, 7, 9);
			assertElement(pi, PathElementType.LINE_TO, 5, 9);
			assertElement(pi, PathElementType.CLOSE, 5, 8);
			assertElement(pi, PathElementType.MOVE_TO, -3, 18);
			assertElement(pi, PathElementType.CURVE_TO, -3, 19, -3, 20, -5, 20);
			assertElement(pi, PathElementType.CURVE_TO, -6, 20, -7, 19, -7, 18);
			assertElement(pi, PathElementType.CURVE_TO, -7, 16, -6, 16, -5, 16);
			assertElement(pi, PathElementType.CURVE_TO, -3, 16, -3, 16, -3, 18);
			assertElement(pi, PathElementType.CLOSE, -3, 18);
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
			PathIterator2ai pi = getS().getPathIterator(null);
			assertElement(pi, PathElementType.MOVE_TO, 5, 8);
			assertElement(pi, PathElementType.LINE_TO, 7, 8);
			assertElement(pi, PathElementType.LINE_TO, 7, 9);
			assertElement(pi, PathElementType.LINE_TO, 5, 9);
			assertElement(pi, PathElementType.CLOSE, 5, 8);
			assertElement(pi, PathElementType.MOVE_TO, -3, 18);
			assertElement(pi, PathElementType.CURVE_TO, -3, 19, -3, 20, -5, 20);
			assertElement(pi, PathElementType.CURVE_TO, -6, 20, -7, 19, -7, 18);
			assertElement(pi, PathElementType.CURVE_TO, -7, 16, -6, 16, -5, 16);
			assertElement(pi, PathElementType.CURVE_TO, -3, 16, -3, 16, -3, 18);
			assertElement(pi, PathElementType.CLOSE, -3, 18);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var pi = getS().getPathIterator(new Transform2D());
			assertElement(pi, PathElementType.MOVE_TO, 5, 8);
			assertElement(pi, PathElementType.LINE_TO, 7, 8);
			assertElement(pi, PathElementType.LINE_TO, 7, 9);
			assertElement(pi, PathElementType.LINE_TO, 5, 9);
			assertElement(pi, PathElementType.CLOSE, 5, 8);
			assertElement(pi, PathElementType.MOVE_TO, -3, 18);
			assertElement(pi, PathElementType.CURVE_TO, -3, 19, -3, 20, -5, 20);
			assertElement(pi, PathElementType.CURVE_TO, -6, 20, -7, 19, -7, 18);
			assertElement(pi, PathElementType.CURVE_TO, -7, 16, -6, 16, -5, 16);
			assertElement(pi, PathElementType.CURVE_TO, -3, 16, -3, 16, -3, 18);
			assertElement(pi, PathElementType.CLOSE, -3, 18);
			assertNoElement(pi);
		}

		@DisplayName("#")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Transform2D transform = new Transform2D();
			transform.setTranslation(10, -2);
			var pi = getS().getPathIterator(transform);
			assertElement(pi, PathElementType.MOVE_TO, 15, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 7);
			assertElement(pi, PathElementType.LINE_TO, 15, 7);
			assertElement(pi, PathElementType.CLOSE, 15, 6);
			assertElement(pi, PathElementType.MOVE_TO, 7, 16);
			assertElement(pi, PathElementType.CURVE_TO, 7, 17, 6, 18, 5, 18);
			assertElement(pi, PathElementType.CURVE_TO, 4, 18, 3, 17, 3, 16);
			assertElement(pi, PathElementType.CURVE_TO, 3, 15, 4, 14, 5, 14);
			assertElement(pi, PathElementType.CURVE_TO, 6, 14, 7, 15, 7, 16);
			assertElement(pi, PathElementType.CLOSE, 7, 16);
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
			Transform2D transform = new Transform2D();
			transform.setTranslation(10, -2);
			Shape2ai newShape = getS().createTransformedShape(transform);
			PathIterator2ai pi = (PathIterator2ai) newShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 7);
			assertElement(pi, PathElementType.LINE_TO, 15, 7);
			assertElement(pi, PathElementType.CLOSE, 15, 6);
			assertElement(pi, PathElementType.MOVE_TO, 7, 16);
			assertElement(pi, PathElementType.CURVE_TO, 7, 17, 6, 18, 5, 18);
			assertElement(pi, PathElementType.CURVE_TO, 4, 18, 3, 17, 3, 16);
			assertElement(pi, PathElementType.CURVE_TO, 3, 15, 4, 14, 5, 14);
			assertElement(pi, PathElementType.CURVE_TO, 6, 14, 7, 15, 7, 16);
			assertElement(pi, PathElementType.CLOSE, 7, 16);
			assertNoElement(pi);
		}
	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

		@DisplayName("(Rectangle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(-20, 14, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(-2, -10, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(-6, 16, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(4, 8, 2, 2)));
		}

		@DisplayName("(Rectangle2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(-4, 18, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(5, 8, 1, 1)));
		}

		@DisplayName("(Circle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(-20, 14, 1)));
		}

		@DisplayName("(Circle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(-2,- 10, 1)));
		}

		@DisplayName("(Circle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(-6, 16, 1)));
		}

		@DisplayName("(Circle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(4, 8, 1)));
		}

		@DisplayName("(Circle2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(-4, 18, 1)));
		}

		@DisplayName("(Circle2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(5, 8, 1)));
		}

		@DisplayName("(Segment2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(-20, 14, -19, 14)));
		}

		@DisplayName("(Segment2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(-2, -10, -1, -10)));
		}

		@DisplayName("(Segment2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-6, 16, -5, 16)));
		}

		@DisplayName("(Segment2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(4, 8, 5, 8)));
		}

		@DisplayName("(Segment2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-4, 18, -3, 18)));
		}

		@DisplayName("(Segment2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(5, 8, 6, 8)));
		}

		@DisplayName("(Path2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("(Path2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(6, 20);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(6, 20);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("(Path2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("(PathIterator2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(-4, 3);
			path.lineTo(9, 6);
			path.lineTo(8, 14);
			path.lineTo(-9, 11);
			path.lineTo(-8, 21);
			path.lineTo(4, 21);
			assertFalse(getS().intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai path = createPath();
			path.moveTo(-4, 3);
			path.lineTo(9, 6);
			path.lineTo(8, 14);
			path.lineTo(-9, 11);
			path.lineTo(-8, 21);
			path.lineTo(4, 21);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(6, 20);
			assertFalse(getS().intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(6, 20);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			assertFalse(getS().intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(Shape2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects((Shape2D) createCircle(-6, 16, 1)));
		}

		@DisplayName("(Shape2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects((Shape2D) createRectangle(-6, 16, 1, 1)));
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
			getS().operator_add(createVector(10, -2));
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 7);
			assertElement(pi, PathElementType.LINE_TO, 15, 7);
			assertElement(pi, PathElementType.CLOSE, 15, 6);
			assertElement(pi, PathElementType.MOVE_TO, 7, 16);
			assertElement(pi, PathElementType.CURVE_TO, 7, 17, 6, 18, 5, 18);
			assertElement(pi, PathElementType.CURVE_TO, 3, 18, 3, 17, 3, 16);
			assertElement(pi, PathElementType.CURVE_TO, 3, 14, 3, 14, 5, 14);
			assertElement(pi, PathElementType.CURVE_TO, 6, 14, 7, 14, 7, 16);
			assertElement(pi, PathElementType.CLOSE, 7, 16);
			assertNoElement(pi);
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
			T shape = getS().operator_plus(createVector(10, -2));
			PathIterator2ai pi = shape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 7);
			assertElement(pi, PathElementType.LINE_TO, 15, 7);
			assertElement(pi, PathElementType.CLOSE, 15, 6);
			assertElement(pi, PathElementType.MOVE_TO, 7, 16);
			assertElement(pi, PathElementType.CURVE_TO, 7, 17, 6, 18, 5, 18);
			assertElement(pi, PathElementType.CURVE_TO, 3, 18, 3, 17, 3, 16);
			assertElement(pi, PathElementType.CURVE_TO, 3, 14, 3, 14, 5, 14);
			assertElement(pi, PathElementType.CURVE_TO, 6, 14, 7, 14, 7, 16);
			assertElement(pi, PathElementType.CLOSE, 7, 16);
			assertNoElement(pi);
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
			getS().operator_remove(createVector(10, -2));
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -5, 10);
			assertElement(pi, PathElementType.LINE_TO, -3, 10);
			assertElement(pi, PathElementType.LINE_TO, -3, 11);
			assertElement(pi, PathElementType.LINE_TO, -5, 11);
			assertElement(pi, PathElementType.CLOSE, -5, 10);
			assertElement(pi, PathElementType.MOVE_TO, -13, 20);
			assertElement(pi, PathElementType.CURVE_TO, -13, 21, -13, 22, -15, 22);
			assertElement(pi, PathElementType.CURVE_TO, -16, 22, -17, 21, -17, 20);
			assertElement(pi, PathElementType.CURVE_TO, -17, 18, -16, 18, -15, 18);
			assertElement(pi, PathElementType.CURVE_TO, -13, 18, -13, 18, -13, 20);
			assertElement(pi, PathElementType.CLOSE, -13, 20);
			assertNoElement(pi);
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
			T shape = getS().operator_minus(createVector(10, -2));
			PathIterator2ai pi = shape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -5, 10);
			assertElement(pi, PathElementType.LINE_TO, -3, 10);
			assertElement(pi, PathElementType.LINE_TO, -3, 11);
			assertElement(pi, PathElementType.LINE_TO, -5, 11);
			assertElement(pi, PathElementType.CLOSE, -5, 10);
			assertElement(pi, PathElementType.MOVE_TO, -13, 20);
			assertElement(pi, PathElementType.CURVE_TO, -13, 21, -13, 22, -15, 22);
			assertElement(pi, PathElementType.CURVE_TO, -16, 22, -17, 21, -17, 20);
			assertElement(pi, PathElementType.CURVE_TO, -17, 18, -16, 18, -15, 18);
			assertElement(pi, PathElementType.CURVE_TO, -13, 18, -13, 18, -13, 20);
			assertElement(pi, PathElementType.CLOSE, -13, 20);
			assertNoElement(pi);
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
			Transform2D transform = new Transform2D();
			transform.setTranslation(10, -2);
			Shape2ai newShape = getS().operator_multiply(transform);
			PathIterator2ai pi = (PathIterator2ai) newShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 6);
			assertElement(pi, PathElementType.LINE_TO, 17, 7);
			assertElement(pi, PathElementType.LINE_TO, 15, 7);
			assertElement(pi, PathElementType.CLOSE, 15, 6);
			assertElement(pi, PathElementType.MOVE_TO, 7, 16);
			assertElement(pi, PathElementType.CURVE_TO, 7, 17, 6, 18, 5, 18);
			assertElement(pi, PathElementType.CURVE_TO, 4, 18, 3, 17, 3, 16);
			assertElement(pi, PathElementType.CURVE_TO, 3, 15, 4, 14, 5, 14);
			assertElement(pi, PathElementType.CURVE_TO, 6, 14, 7, 15, 7, 16);
			assertElement(pi, PathElementType.CLOSE, 7, 16);
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
			assertFalse(getS().operator_and(createPoint(-10, 2)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(-10, 14)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(-10, 25)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(-1, 25)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(1, 2)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(12, 2)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(12, 14)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(12, 25)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(-6, 8)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(4, 17)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(-4, 19)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(6, 8)));
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
			assertTrue(getS().operator_and(createCircle(-6, 16, 1)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createRectangle(-6, 16, 1, 1)));
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
			assertEpsilonEquals(14.5602, getS().operator_upTo(createPoint(-10, 2)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.4721, getS().operator_upTo(createPoint(-10, 14)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.4031, getS().operator_upTo(createPoint(-10, 25)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.831, getS().operator_upTo(createPoint(-1, 25)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.2111, getS().operator_upTo(createPoint(1, 2)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.8102, getS().operator_upTo(createPoint(12, 2)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.0711, getS().operator_upTo(createPoint(12, 14)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16.7631, getS().operator_upTo(createPoint(12, 25)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8, getS().operator_upTo(createPoint(-6, 8)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7, getS().operator_upTo(createPoint(4, 17)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(-4, 19)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(6, 8)));
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
			Iterator<? extends Point2D> it = getS().getPointIterator();

			assertTrue(it.hasNext());
			var p = it.next();
			assertNotNull(p);
			assertEquals(5, p.ix(), p.toString());
			assertEquals(8, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(6, p.ix(), p.toString());
			assertEquals(8, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(7, p.ix(), p.toString());
			assertEquals(8, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(7, p.ix(), p.toString());
			assertEquals(9, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(6, p.ix(), p.toString());
			assertEquals(9, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(5, p.ix(), p.toString());
			assertEquals(9, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(5, p.ix(), p.toString());
			assertEquals(8, p.iy(), p.toString());

			// Circle points
			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(-5, p.ix(), p.toString());
			assertEquals(20, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(-4, p.ix(), p.toString());
			assertEquals(20, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(-3, p.ix(), p.toString());
			assertEquals(18, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(-3, p.ix(), p.toString());
			assertEquals(19, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(-5, p.ix(), p.toString());
			assertEquals(16, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(-4, p.ix(), p.toString());
			assertEquals(16, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(-3, p.ix(), p.toString());
			assertEquals(17, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(-6, p.ix(), p.toString());
			assertEquals(16, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(-7, p.ix(), p.toString());
			assertEquals(18, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(-7, p.ix(), p.toString());
			assertEquals(17, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(-6, p.ix(), p.toString());
			assertEquals(20, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(-7, p.ix(), p.toString());
			assertEquals(19, p.iy(), p.toString());

			assertFalse(it.hasNext());
		}

	}

	@DisplayName("getFirstShapeContaining")
	@Nested
	public class GetFirstShapeContaining {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(-10, 2)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(-10, 14)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(-10, 25)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(-1, 25)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(1, 2)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(12, 2)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(12, 14)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(12, 25)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(-6, 8)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getFirstShapeContaining(createPoint(4, 17)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(secondObject, getS().getFirstShapeContaining(createPoint(-4, 19)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(firstObject, getS().getFirstShapeContaining(createPoint(6, 8)));
		}
	}

	@DisplayName("getShapesContaining")
	@Nested
	public class GetShapesContaining {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(-10, 2)).isEmpty());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(-10, 14)).isEmpty());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(-10, 25)).isEmpty());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(-1, 25)).isEmpty());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(1, 2)).isEmpty());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(12, 2)).isEmpty());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(12, 14)).isEmpty());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(12, 25)).isEmpty());
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(-6, 8)).isEmpty());
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getShapesContaining(createPoint(4, 17)).isEmpty());
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(Arrays.asList(secondObject), getS().getShapesContaining(createPoint(-4, 19)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(Arrays.asList(firstObject), getS().getShapesContaining(createPoint(6, 8)));
		}
	}

	@DisplayName("getFirstShapeIntersecting")
	@Nested
	public class GetFirstShapeIntersecting {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2D shape2d = getS();
			assertNull(shape2d.getFirstShapeIntersecting(createSegment(-20, 14, -19, 14)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2D shape2d = getS();
			assertNull(shape2d.getFirstShapeIntersecting(createSegment(-2, -10, -1, -10)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2D shape2d = getS();
			assertSame(secondObject, shape2d.getFirstShapeIntersecting(createSegment(-6, 16, -5, 16)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2D shape2d = getS();
			assertSame(firstObject, shape2d.getFirstShapeIntersecting(createSegment(4, 8, 5, 8)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2D shape2d = getS();
			assertSame(secondObject, shape2d.getFirstShapeIntersecting(createSegment(-4, 18, -3, 18)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2D shape2d = getS();
			assertSame(firstObject, shape2d.getFirstShapeIntersecting(createSegment(5, 8, 6, 8)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2D shape2d = getS();

			Path2ai path = createPath();
			path.moveTo(-4, 3);
			path.lineTo(9, 6);
			path.lineTo(8, 14);
			path.lineTo(-9, 11);
			path.lineTo(-8, 21);
			path.lineTo(4, 21);

			assertNull(shape2d.getFirstShapeIntersecting(path));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2D shape2d = getS();

			Path2ai path = createPath();
			path.moveTo(-4, 3);
			path.lineTo(9, 6);
			path.lineTo(8, 14);
			path.lineTo(-9, 11);
			path.lineTo(-8, 21);
			path.lineTo(4, 21);
			path.closePath();

			assertSame(firstObject, shape2d.getFirstShapeIntersecting(path));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2D shape2d = getS();
			assertTrue(shape2d.getShapesIntersecting(createSegment(-20, 14, -19, 14)).isEmpty());
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2D shape2d = getS();
			assertTrue(shape2d.getShapesIntersecting(createSegment(-2, -10, -1, -10)).isEmpty());
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2D shape2d = getS();
			assertEquals(Arrays.asList(secondObject), shape2d.getShapesIntersecting(createSegment(-6, 16, -5, 16)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2D shape2d = getS();
			assertEquals(Arrays.asList(firstObject), shape2d.getShapesIntersecting(createSegment(4, 8, 5, 8)));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2D shape2d = getS();
			assertEquals(Arrays.asList(secondObject), shape2d.getShapesIntersecting(createSegment(-4, 18, -3, 18)));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2D shape2d = getS();
			assertEquals(Arrays.asList(firstObject), shape2d.getShapesIntersecting(createSegment(5, 8, 6, 8)));
		}

	}

	@DisplayName("getBackendDataList")
	@Nested
	public class GetBackendDataList {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertNotNull(getS().getBackendDataList());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(2, getS().getBackendDataList().size());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(firstObject, getS().getBackendDataList().get(0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(secondObject, getS().getBackendDataList().get(1));
		}
	}

	@DisplayName("on GeometryChange")
	@Nested
	public class OnGeometryChange {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEquals(-7, box.getMinX());
			assertEquals(8, box.getMinY());
			assertEquals(7, box.getMaxX());
			assertEquals(20, box.getMaxY());

			firstObject.translate(12, -7);

			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9

			// R': 17;  1; 19;  2 

			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEquals(-7, box.getMinX());
			assertEquals(1, box.getMinY());
			assertEquals(19, box.getMaxX());
			assertEquals(20, box.getMaxY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEquals(-7, box.getMinX());
			assertEquals(8, box.getMinY());
			assertEquals(7, box.getMaxX());
			assertEquals(20, box.getMaxY());

			secondObject.translate(12, -7);

			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9

			// C':  5;  9;  9; 13 

			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEquals(5, box.getMinX());
			assertEquals(8, box.getMinY());
			assertEquals(9, box.getMaxX());
			assertEquals(13, box.getMaxY());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEquals(-7, box.getMinX());
			assertEquals(8, box.getMinY());
			assertEquals(7, box.getMaxX());
			assertEquals(20, box.getMaxY());

			getS().remove(secondObject);
			secondObject.translate(1453,  -451);

			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEquals(5, box.getMinX());
			assertEquals(8, box.getMinY());
			assertEquals(7, box.getMaxX());
			assertEquals(9, box.getMaxY());
		}

	}

	@DisplayName("on BackendDataListChange")
	@Nested
	public class OnBackendDataListChange {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEquals(-7, box.getMinX());
			assertEquals(8, box.getMinY());
			assertEquals(7, box.getMaxX());
			assertEquals(20, box.getMaxY());

			getS().add((C) createCircle(10, 14, 1));

			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9

			// C':  9;  13;  11; 15 

			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEquals(-7, box.getMinX());
			assertEquals(8, box.getMinY());
			assertEquals(11, box.getMaxX());
			assertEquals(20, box.getMaxY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEquals(-7, box.getMinX());
			assertEquals(8, box.getMinY());
			assertEquals(7, box.getMaxX());
			assertEquals(20, box.getMaxY());

			getS().remove(firstObject);

			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9

			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEquals(-7, box.getMinX());
			assertEquals(16, box.getMinY());
			assertEquals(-3, box.getMaxX());
			assertEquals(20, box.getMaxY());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEquals(-7, box.getMinX());
			assertEquals(8, box.getMinY());
			assertEquals(7, box.getMaxX());
			assertEquals(20, box.getMaxY());

			getS().remove(secondObject);

			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9

			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEquals(5, box.getMinX());
			assertEquals(8, box.getMinY());
			assertEquals(7, box.getMaxX());
			assertEquals(9, box.getMaxY());
		}
	}

}