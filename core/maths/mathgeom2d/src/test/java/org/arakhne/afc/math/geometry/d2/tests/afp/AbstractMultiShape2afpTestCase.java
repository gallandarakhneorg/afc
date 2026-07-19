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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.MultiShape2D;
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
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;


@SuppressWarnings("all")
public abstract class AbstractMultiShape2afpTestCase<T extends MultiShape2afp<?, T, C, ?, ?, ?, B>,
		C extends Shape2afp<?, ?, ?, ?, ?, B>,
		B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractShape2afpTestCase<T, B> {

	protected C firstObject;
	
	protected C secondObject;

	@Override
	protected final T createShape() {
		T shape = (T) createMultiShape();
		this.firstObject = (C) createRectangle(5, 8, 2, 1);
		this.secondObject = (C) createCircle(-5, 18, 2);
		shape.add(this.firstObject);
		shape.add(this.secondObject);
		return shape;
	}

	protected Triangle2afp createTestTriangle(double dx, double dy) {
		return createTriangle(dx, dy, dx + 5, dy + 2, dx - 2, dy + 3);
	}

	protected MultiShape2afp createTestMultiShape(double dx, double dy) {
		MultiShape2afp shape = createMultiShape();
		shape.add(createCircle(dx - 1, dy + 5, .5));
		shape.add(createCircle(dx + 3, dy - 1, 1));
		return shape;
	}

	protected Path2afp createSimpleTestPath(double dx, double dy, boolean close) {
		Path2afp path = createPath();
		path.moveTo(dx, dy);
		path.lineTo(dx + 16, dy + 4);
		path.lineTo(dx + 14, dy + 12);
		path.lineTo(dx + 2, dy + 10);
		path.lineTo(dx - 6, dy + 20);
		path.lineTo(dx + 12, dy + 18);
		if (close) {
			path.closePath();
		}
		return path;
	}

	protected OrientedRectangle2afp createTestOrientedRectangle(double dx, double dy) {
		Vector2D<?, ?> u = createVector(-4, 5).toUnitVector();
		return createOrientedRectangle(dx, dy, u.getX(), u.getY(), 2, 1);
	}

	protected Parallelogram2afp createTestParallelogram(double dx, double dy) {
		Vector2D<?, ?> u = createVector(-4, 5).toUnitVector();
		Vector2D<?, ?> v = createVector(1, -.5).toUnitVector();
		return createParallelogram(dx, dy, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1);
	}

	@DisplayName("clone")
	@Nested
	public class CloneTest {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void testClone(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2afp clone = getS().clone();
			PathIterator2afp pi = (PathIterator2afp) clone.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 5, 8);
			assertElement(pi, PathElementType.LINE_TO, 7, 8);
			assertElement(pi, PathElementType.LINE_TO, 7, 9);
			assertElement(pi, PathElementType.LINE_TO, 5, 9);
			assertElement(pi, PathElementType.CLOSE, 5, 8);
			assertElement(pi, PathElementType.MOVE_TO, -3, 18);
			assertElement(pi, PathElementType.CURVE_TO, -3, 19.10457, -3.89543, 20, -5, 20);
			assertElement(pi, PathElementType.CURVE_TO, -6.10457, 20, -7, 19.10457, -7, 18);
			assertElement(pi, PathElementType.CURVE_TO, -7, 16.89543, -6.10457, 16, -5, 16);
			assertElement(pi, PathElementType.CURVE_TO, -3.89543, 16, -3, 16.89543, -3, 18);
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
			PathIterator2afp pi = getS().getPathIterator();
			assertNoElement(pi);
		}

	}

	@DisplayName("contains(double,double)")
	@Nested
	public class ContainsDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-10, 2));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-10, 14));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-10, 25));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-1, 25));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(1, 2));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(12, 2));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(12, 14));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(12, 25));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-6, 8));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(4, 17));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(-4, 19));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(6, 8.25));
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
			assertFalse(getS().contains(createPoint(-10, 2)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(-10, 14)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(-10, 25)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(-1, 25)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(1, 2)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(12, 2)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(12, 14)));
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(12, 25)));
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(-6, 8)));
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(4, 17)));
		}

		@DisplayName("(Point2D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(-4, 19)));
		}

		@DisplayName("(Point2D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(6, 8.25)));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(-20, 14, .5, .5)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(-2,-10, .5, .5)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(-6,16, .5, .5)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(4.75, 8, .5, .5)));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createRectangle(-4, 18, .5, .5)));
		}

		@DisplayName("(Rectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createRectangle(5.5, 8.5, .5, .5)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(-20, 14, .5)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(-2,-10, .5)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(-6,16, .5)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(4.75, 8, .5)));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createCircle(-4, 18, .5)));
		}

		@DisplayName("(Circle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createCircle(5.5, 8.5, .5)));
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
			assertEpsilonEquals(-5.59655, result.getX());
			assertEpsilonEquals(16.09104, result.getY());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-10, 14));
			assertEpsilonEquals(-6.56174, result.getX());
			assertEpsilonEquals(16.75061, result.getY());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-10, 25));
			assertEpsilonEquals(-6.16248, result.getX());
			assertEpsilonEquals(19.62747, result.getY());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-1, 25));
			assertEpsilonEquals(-4.00772, result.getX());
			assertEpsilonEquals(19.73649, result.getY());
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(1, 2));
			assertEpsilonEquals(5, result.getX());
			assertEpsilonEquals(8, result.getY());
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(12, 2));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(8, result.getY());
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(12, 14));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(9, result.getY());
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(12, 25));
			assertEpsilonEquals(-3.15064, result.getX());
			assertEpsilonEquals(18.7615, result.getY());
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-6, 8));
			assertEpsilonEquals(-5.19901, result.getX());
			assertEpsilonEquals(16.00993, result.getY());
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(4, 17));
			assertEpsilonEquals(-3.01223, result.getX());
			assertEpsilonEquals(17.77914, result.getY());
		}

		@DisplayName("(Point2D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-4, 19));
			assertEpsilonEquals(-4, result.getX());
			assertEpsilonEquals(19, result.getY());
		}

		@DisplayName("(Point2D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(6, 8.25));
			assertEpsilonEquals(6, result.getX());
			assertEpsilonEquals(8.25, result.getY());
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createCircle(0, 0, 1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createCircle(2, 8, 1)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(-3.21115, 17.10557, getS().getClosestPointTo(createCircle(-1, 16, 1)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(-4, 17, 1));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(6, 9.5, 1));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createSegment(0, 0, 1, 1)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createSegment(1, 0, 0, 1)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(8, 8, 4, 10));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 9, getS().getClosestPointTo(createSegment(0, 12, 4, 10)));
		}

		@DisplayName("(Segment2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(-3, 18, getS().getClosestPointTo(createSegment(0, 12, -2, 18)));
		}

		@DisplayName("(Segment2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(-6, 20, -2, 18));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createRectangle(0, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(6.5, 9, getS().getClosestPointTo(createRectangle(6, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(-4.36754, 16.10263, getS().getClosestPointTo(createRectangle(-4, 14, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(-6, 18, 1, 1));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(5.9, 8.5, 1, 1));
		}

		@DisplayName("(RoundRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createRoundRectangle(0, 0, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(6.5, 9, getS().getClosestPointTo(createRoundRectangle(6, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(-4.33118, 16.11514, getS().getClosestPointTo(createRoundRectangle(-4, 14, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(-6, 18, 1, 1, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(5.9, 8.5, 1, 1, .1, .1));
		}

		@DisplayName("(Ellipse2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createEllipse(0, 0, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7, 9, getS().getClosestPointTo(createEllipse(6, 12, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(-4.20901390964965, 16.16306205742484, getS().getClosestPointTo(createEllipse(-4, 14, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createEllipse(-6, 18, 2, 1));
		}

		@DisplayName("(Ellipse2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createEllipse(5.9, 8.5, 2, 1));
		}

		@DisplayName("(Triangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createTestTriangle(0, 0)));
		}

		@DisplayName("(Triangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 9, getS().getClosestPointTo(createTestTriangle(2, 10)));
		}

		@DisplayName("(Triangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestTriangle(8, 6));
		}

		@DisplayName("(Triangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(-3.3359, 19.1094, getS().getClosestPointTo(createTestTriangle(0, 18)));
		}

		@DisplayName("(Triangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestTriangle(-10, 16));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createTestOrientedRectangle(0, 0)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7, 8, getS().getClosestPointTo(createTestOrientedRectangle(9, 6)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 9, getS().getClosestPointTo(createTestOrientedRectangle(2, 12)));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(-3.11699, 17.32601, getS().getClosestPointTo(createTestOrientedRectangle(0, 16)));
		}

		@DisplayName("(OrientedRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestOrientedRectangle(-7, 18));
		}

		@DisplayName("(Parallelogram2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createTestParallelogram(0, 0)));
		}

		@DisplayName("(Parallelogram2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(6.85618, 8, getS().getClosestPointTo(createTestParallelogram(9, 5)));
		}

		@DisplayName("(Parallelogram2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 9, getS().getClosestPointTo(createTestParallelogram(2, 12)));
		}

		@DisplayName("(Parallelogram2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(-3.00001, 18.00627, getS().getClosestPointTo(createTestParallelogram(0, 16)));
		}

		@DisplayName("(Parallelogram2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestParallelogram(-7, 18));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		    assertClosestPointInBothShapes(getS(), createSimpleTestPath(0, 0, false));
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
			assertFpPointEquals(-6.56174, 16.75061, getS().getClosestPointTo(createSimpleTestPath(-6, 2, false)));
		}

		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSimpleTestPath(-6, 2, true));
		}

		@DisplayName("(Path2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(-3.8906, 16.3359, getS().getClosestPointTo(createSimpleTestPath(-3, 15, false)));
		}

		@DisplayName("(Path2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(-3.3359, 16.8906, getS().getClosestPointTo(createSimpleTestPath(-3, 15, true)));
		}

		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 9, getS().getClosestPointTo(createTestMultiShape(5, 5)));
		}

		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(-3.21115, 18.89443, getS().getClosestPointTo(createTestMultiShape(0, 15)));
		}

		@DisplayName("(MultiShape2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestMultiShape(-10, 20));
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
			assertEpsilonEquals(-4.40345, result.getX());
			assertEpsilonEquals(19.90896, result.getY());
		}
		
		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(-10, 14));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(8, result.getY());
		}
		
		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(-10, 25));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(8, result.getY());
		}
		
		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(-1, 25));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(8, result.getY());
		}
		
		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(1, 2));
			assertEpsilonEquals(-5.70225, result.getX());
			assertEpsilonEquals(19.87266, result.getY());
		}
		
		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(12, 2));
			assertEpsilonEquals(-6.4564, result.getX());
			assertEpsilonEquals(19.37073, result.getY());
		}
		
		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(12, 14));
			assertEpsilonEquals(-6.94683, result.getX());
			assertEpsilonEquals(18.45808, result.getY());
		}
		
		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(12, 25));
			assertEpsilonEquals(-6.84936, result.getX());
			assertEpsilonEquals(17.2385, result.getY());
		}
		
		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(-6, 8));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(9, result.getY());
		}
		
		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(4, 17));
			assertEpsilonEquals(-6.98777, result.getX());
			assertEpsilonEquals(18.22086, result.getY());
		}
		
		@DisplayName("(Point2D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(-4, 19));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(8, result.getY());
		}
		
		@DisplayName("(Point2D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(6, 8.25));
			assertEpsilonEquals(-6.49669, result.getX());
			assertEpsilonEquals(19.32662, result.getY());
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
			assertEpsilonEquals(14.76305, getS().getDistance(createPoint(-10, 2)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.40312, getS().getDistance(createPoint(-10, 14)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.60233, getS().getDistance(createPoint(-10, 25)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.06226, getS().getDistance(createPoint(-1, 25)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.21110, getS().getDistance(createPoint(1, 2)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.81025, getS().getDistance(createPoint(12, 2)));
		}

		@DisplayName("(Point2D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.07107, getS().getDistance(createPoint(12, 14)));
		}

		@DisplayName("(Point2D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16.38478, getS().getDistance(createPoint(12, 25)));
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.04988, getS().getDistance(createPoint(-6, 8)));
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.05538, getS().getDistance(createPoint(4, 17)));
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
			assertEpsilonEquals(0, getS().getDistance(createPoint(6, 8.25)));
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
			assertEpsilonEquals(217.94778, getS().getDistanceSquared(createPoint(-10, 2)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(19.38749, getS().getDistanceSquared(createPoint(-10, 14)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(43.5907, getS().getDistanceSquared(createPoint(-10, 25)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(36.75092, getS().getDistanceSquared(createPoint(-1, 25)));
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
			assertEpsilonEquals(268.46089, getS().getDistanceSquared(createPoint(12, 25)));
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(64.8005, getS().getDistanceSquared(createPoint(-6, 8)));
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(49.77843, getS().getDistanceSquared(createPoint(4, 17)));
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
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(6, 8.25)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(71.13204, getS().getDistanceSquared(createCircle(0, 0, 1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, getS().getDistanceSquared(createCircle(2, 8, 1)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.16719, getS().getDistanceSquared(createCircle(-1, 16, 1)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(-4, 17, 1)));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(6, 9.5, 1)));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(65, getS().getDistanceSquared(createSegment(0, 0, 1, 1)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(74, getS().getDistanceSquared(createSegment(1, 0, 0, 1)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(8, 8, 4, 10)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, getS().getDistanceSquared(createSegment(0, 12, 4, 10)));
		}

		@DisplayName("(Segment2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceSquared(createSegment(0, 12, -2, 18)));
		}

		@DisplayName("(Segment2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(-6, 20, -2, 18)));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(65, getS().getDistanceSquared(createRectangle(0, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9, getS().getDistanceSquared(createRectangle(6, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.35089, getS().getDistanceSquared(createRectangle(-4, 14, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(-6, 18, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(5.9, 8.5, 1, 1)));
		}

		@DisplayName("(RoundRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(65.59024, getS().getDistanceSquared(createRoundRectangle(0, 0, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9, getS().getDistanceSquared(createRoundRectangle(6, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.41462, getS().getDistanceSquared(createRoundRectangle(-4, 14, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(-6, 18, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(5.9, 8.5, 1, 1, .1, .1)));
		}

		@DisplayName("(Ellipse2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(61.90769, getS().getDistanceSquared(createEllipse(0, 0, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9, getS().getDistanceSquared(createEllipse(6, 12, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.95879649834575, getS().getDistanceSquared(createEllipse(-4, 14, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createEllipse(-6, 18, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createEllipse(5.9, 8.5, 2, 1)));
		}

		@DisplayName("(Triangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(35.28, getS().getDistanceSquared(createTestTriangle(0, 0)));
		}

		@DisplayName("(Triangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.17241, getS().getDistanceSquared(createTestTriangle(2, 10)));
		}

		@DisplayName("(Triangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestTriangle(8, 6)));
		}

		@DisplayName("(Triangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.66669, getS().getDistanceSquared(createTestTriangle(0, 18)));
		}

		@DisplayName("(Triangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestTriangle(-10, 16)));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(63.70229, getS().getDistanceSquared(createTestOrientedRectangle(0, 0)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.65793, getS().getDistanceSquared(createTestOrientedRectangle(9, 6)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.91372, getS().getDistanceSquared(createTestOrientedRectangle(2, 12)));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.33227, getS().getDistanceSquared(createTestOrientedRectangle(0, 16)));
		}

		@DisplayName("(OrientedRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestOrientedRectangle(-7, 18)));
		}

		@DisplayName("(Parallelogram2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(76.08541, getS().getDistanceSquared(createTestParallelogram(0, 0)));
		}

		@DisplayName("(Parallelogram2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.98218, getS().getDistanceSquared(createTestParallelogram(9, 5)));
		}

		@DisplayName("(Parallelogram2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.71523, getS().getDistanceSquared(createTestParallelogram(2, 12)));
		}

		@DisplayName("(Parallelogram2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.73307, getS().getDistanceSquared(createTestParallelogram(0, 16)));
		}

		@DisplayName("(Parallelogram2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestParallelogram(-7, 18)));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSimpleTestPath(0, 0, false)));
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
			assertEpsilonEquals(0.93567, getS().getDistanceSquared(createSimpleTestPath(-6, 2, false)));
		}

		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSimpleTestPath(-6, 2, true)));
		}

		@DisplayName("(Path2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		}

		@DisplayName("(Path2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.57779, getS().getDistanceSquared(createSimpleTestPath(-3, 15, false)));
		}

		@DisplayName("(Path2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.76412, getS().getDistanceSquared(createSimpleTestPath(-3, 15, true)));
		}

		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.83579, getS().getDistanceSquared(createTestMultiShape(5, 5)));
		}

		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.88932, getS().getDistanceSquared(createTestMultiShape(0, 15)));
		}

		@DisplayName("(MultiShape2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(-10, 20)));
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
			assertEpsilonEquals(18.49449, getS().getDistanceL1(createPoint(-10, 2)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.18887, getS().getDistanceL1(createPoint(-10, 14)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9.21006, getS().getDistanceL1(createPoint(-10, 25)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.27123, getS().getDistanceL1(createPoint(-1, 25)));
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
			assertEpsilonEquals(8.81092, getS().getDistanceL1(createPoint(-6, 8)));
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.79137, getS().getDistanceL1(createPoint(4, 17)));
		}

		@DisplayName("(Point2D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(-4, 19)));
		}

		@DisplayName("(Point2D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(6, 8.25)));
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
			assertEpsilonEquals(14.09104, getS().getDistanceLinf(createPoint(-10, 2)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.43826, getS().getDistanceLinf(createPoint(-10, 14)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.37253, getS().getDistanceLinf(createPoint(-10, 25)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.26351, getS().getDistanceLinf(createPoint(-1, 25)));
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
			assertEpsilonEquals(15.15064, getS().getDistanceLinf(createPoint(12, 25)));
		}

		@DisplayName("(Point2D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.00993, getS().getDistanceLinf(createPoint(-6, 8)));
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.01223, getS().getDistanceLinf(createPoint(4, 17)));
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
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(6, 8.25)));
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
			getS().set((T) createMultiShape());
			PathIterator2afp pi = getS().getPathIterator();
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set((T) createMultiShape());
			PathIterator2afp pi = getS().getPathIterator();
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			MultiShape2afp newShape = createMultiShape();
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

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 5, 8);
			assertElement(pi, PathElementType.LINE_TO, 7, 8);
			assertElement(pi, PathElementType.LINE_TO, 7, 9);
			assertElement(pi, PathElementType.LINE_TO, 5, 9);
			assertElement(pi, PathElementType.CLOSE, 5, 8);
			assertElement(pi, PathElementType.MOVE_TO, -3, 18);
			assertElement(pi, PathElementType.CURVE_TO, -3, 19.10457, -3.89543, 20, -5, 20);
			assertElement(pi, PathElementType.CURVE_TO, -6.10457, 20, -7, 19.10457, -7, 18);
			assertElement(pi, PathElementType.CURVE_TO, -7, 16.89543, -6.10457, 16, -5, 16);
			assertElement(pi, PathElementType.CURVE_TO, -3.89543, 16, -3, 16.89543, -3, 18);
			assertElement(pi, PathElementType.CLOSE, -3, 18);
			assertNoElement(pi);
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
			getS().translate(10, -2);
			PathIterator2afp pi = getS().getPathIterator();
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
			PathIterator2afp pi = getS().getPathIterator();
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
			assertElement(pi, PathElementType.LINE_TO, 7, 8);
			assertElement(pi, PathElementType.LINE_TO, 7, 9);
			assertElement(pi, PathElementType.LINE_TO, 5, 9);
			assertElement(pi, PathElementType.CLOSE, 5, 8);
			assertElement(pi, PathElementType.MOVE_TO, -3, 18);
			assertElement(pi, PathElementType.CURVE_TO, -3, 19.10457, -3.89543, 20, -5, 20);
			assertElement(pi, PathElementType.CURVE_TO, -6.10457, 20, -7, 19.10457, -7, 18);
			assertElement(pi, PathElementType.CURVE_TO, -7, 16.89543, -6.10457, 16, -5, 16);
			assertElement(pi, PathElementType.CURVE_TO, -3.89543, 16, -3, 16.89543, -3, 18);
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
			assertElement(pi, PathElementType.CURVE_TO, -3, 19.10457, -3.89543, 20, -5, 20);
			assertElement(pi, PathElementType.CURVE_TO, -6.10457, 20, -7, 19.10457, -7, 18);
			assertElement(pi, PathElementType.CURVE_TO, -7, 16.89543, -6.10457, 16, -5, 16);
			assertElement(pi, PathElementType.CURVE_TO, -3.89543, 16, -3, 16.89543, -3, 18);
			assertElement(pi, PathElementType.CLOSE, -3, 18);
			assertNoElement(pi);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
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
			assertElement(pi, PathElementType.CURVE_TO, 7, 17.10457, 6.10457, 18, 5, 18);
			assertElement(pi, PathElementType.CURVE_TO, 3.89543, 18, 3, 17.10457, 3, 16);
			assertElement(pi, PathElementType.CURVE_TO, 3, 14.89543, 3.89543, 14, 5, 14);
			assertElement(pi, PathElementType.CURVE_TO, 6.10457, 14, 7, 14.89543, 7, 16);
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
			Shape2afp newShape = getS().createTransformedShape(transform);
			PathIterator2afp pi = (PathIterator2afp) newShape.getPathIterator();
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
			assertFalse(getS().intersects(createRectangle(-20, 14, .5, .5)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(-2, -10, .5, .5)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(-6, 16, .5, .5)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(4.75, 8, .5, .5)));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(-4, 18, .5, .5)));
		}

		@DisplayName("(Rectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(5.5, 8.5, .5, .5)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(-20, 14, .5)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(-2,- 10, .5)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(-6, 16, .5)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(4.75, 8, .5)));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(-4, 18, .5)));
		}

		@DisplayName("(Circle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(5.5, 8.5, .5)));
		}

		@DisplayName("(Triangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createTriangle(-20, 14, -19.5, 14, -20, 14.5)));
		}

		@DisplayName("(Triangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createTriangle(-2, -10, -1.5, -10, -2, -9.5)));
		}

		@DisplayName("(Triangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createTriangle(-6, 16, -5.5, 16, -6, 16.5)));
		}

		@DisplayName("(Triangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createTriangle(4.75, 8, 5.25, 8, 4.75, 8.5)));
		}

		@DisplayName("(Triangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createTriangle(-4, 18, -3.5, 18, -4, 18.5)));
		}

		@DisplayName("(Triangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createTriangle(5.5, 8.5, 6, 8.5, 5.5, 9)));
		}

		@DisplayName("(Ellipse2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createEllipse(-20, 14, .5, .5)));
		}

		@DisplayName("(Ellipse2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createEllipse(-2, -10, .5, .5)));
		}

		@DisplayName("(Ellipse2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createEllipse(-6, 16, .5, .5)));
		}

		@DisplayName("(Ellipse2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createEllipse(4.75, 8, .5, .5)));
		}

		@DisplayName("(Ellipse2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createEllipse(-4, 18, .5, .5)));
		}

		@DisplayName("(Ellipse2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createEllipse(5.5, 8.5, .5, .5)));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(-20, 14, -19.5, 14)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(-2, -10, -1.5, -10)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-6, 16, -5.5, 16.5)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(4.75, 8, 5.25, 8)));
		}

		@DisplayName("(Segment2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-4, 18, -3.5, 18)));
		}

		@DisplayName("(Segment2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(5.5, 8.5, 6, 8.5)));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(6, 20);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("(Path2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
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

		@DisplayName("(Path2afp) #6")
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

		@DisplayName("(PathIterator2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(6, 20);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}
	
		@DisplayName("(PathIterator2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(6, 20);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #6")
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
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createOrientedRectangle(-20, 14, 1, 0, .5, .5)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createOrientedRectangle(-2, -10, 1, 0, .5, .5)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createOrientedRectangle(-6, 16, 1, 0, .5, .5)));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createOrientedRectangle(4.75, 8, 1, 0, .5, .5)));
		}

		@DisplayName("(OrientedRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createOrientedRectangle(-4, 18, 1, 0, .5, .5)));
		}

		@DisplayName("(OrientedRectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createOrientedRectangle(5.5, 8.5, 1, 0, .5, .5)));
		}

		@DisplayName("(Parallelogram2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createParallelogram(-20, 14, 1, 0, .5, 0, 1, .5)));
		}

		@DisplayName("(Parallelogram2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createParallelogram(-2, -10, 1, 0, .5, 0, 1, .5)));
		}

		@DisplayName("(Parallelogram2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createParallelogram(-6, 16, 1, 0, .5, 0, 1, .5)));
		}

		@DisplayName("(Parallelogram2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createParallelogram(4.75, 8, 1, 0, .5, 0, 1, .5)));
		}

		@DisplayName("(Parallelogram2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createParallelogram(-4, 18, 1, 0, .5, 0, 1, .5)));
		}

		@DisplayName("(Parallelogram2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createParallelogram(5.5, 8.5, 1, 0, .5, 0, 1, .5)));
		}

		@DisplayName("(RoundRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(-20, 14, .5, .5, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(-2, -10, .5, .5, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRoundRectangle(-6, 16, .5, .5, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRoundRectangle(4.75, 8, .5, .5, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRoundRectangle(-4, 18, .5, .5, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRoundRectangle(5.5, 8.5, .5, .5, .1, .1)));
		}

		@DisplayName("(Shape2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects((Shape2D) createCircle(4.75, 8, .5)));
		}

		@DisplayName("(Shape2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			assertFalse(getS().intersects((Shape2D) path));
		}

		@DisplayName("(Shape2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			path.closePath();
			assertTrue(getS().intersects((Shape2D) path));
		}

	}

	@DisplayName("this += Vecto2D")
	@Nested
	public class OperatorAddVector2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().operator_add(createVector(10, -2));
			PathIterator2afp pi = getS().getPathIterator();
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
			PathIterator2afp pi = shape.getPathIterator();
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
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -5, 10);
			assertElement(pi, PathElementType.LINE_TO, -3, 10);
			assertElement(pi, PathElementType.LINE_TO, -3, 11);
			assertElement(pi, PathElementType.LINE_TO, -5, 11);
			assertElement(pi, PathElementType.CLOSE, -5, 10);
			assertElement(pi, PathElementType.MOVE_TO, -13, 20);
			assertElement(pi, PathElementType.CURVE_TO, -13, 21.10457, -13.89543, 22, -15, 22);
			assertElement(pi, PathElementType.CURVE_TO, -16.10457, 22, -17, 21.10457, -17, 20);
			assertElement(pi, PathElementType.CURVE_TO, -17, 18.89543, -16.10457, 18, -15, 18);
			assertElement(pi, PathElementType.CURVE_TO, -13.89543, 18, -13, 18.89543, -13, 20);
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
			PathIterator2afp pi = shape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -5, 10);
			assertElement(pi, PathElementType.LINE_TO, -3, 10);
			assertElement(pi, PathElementType.LINE_TO, -3, 11);
			assertElement(pi, PathElementType.LINE_TO, -5, 11);
			assertElement(pi, PathElementType.CLOSE, -5, 10);
			assertElement(pi, PathElementType.MOVE_TO, -13, 20);
			assertElement(pi, PathElementType.CURVE_TO, -13, 21.10457, -13.89543, 22, -15, 22);
			assertElement(pi, PathElementType.CURVE_TO, -16.10457, 22, -17, 21.10457, -17, 20);
			assertElement(pi, PathElementType.CURVE_TO, -17, 18.89543, -16.10457, 18, -15, 18);
			assertElement(pi, PathElementType.CURVE_TO, -13.89543, 18, -13, 18.89543, -13, 20);
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
			Shape2afp newShape = getS().operator_multiply(transform);
			PathIterator2afp pi = (PathIterator2afp) newShape.getPathIterator();
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
			assertTrue(getS().operator_and(createPoint(6, 8.25)));
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
			assertTrue(getS().operator_and(createCircle(4.75, 8, .5)));
			Path2afp path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			assertFalse(getS().operator_and(path));
			path.closePath();
			assertTrue(getS().operator_and(path));
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
			assertEpsilonEquals(14.76305, getS().operator_upTo(createPoint(-10, 2)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.40312, getS().operator_upTo(createPoint(-10, 14)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.60233, getS().operator_upTo(createPoint(-10, 25)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.06226, getS().operator_upTo(createPoint(-1, 25)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.21110, getS().operator_upTo(createPoint(1, 2)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.07107, getS().operator_upTo(createPoint(12, 14)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16.38478, getS().operator_upTo(createPoint(12, 25)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.81025, getS().operator_upTo(createPoint(12, 2)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.04988, getS().operator_upTo(createPoint(-6, 8)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.05538, getS().operator_upTo(createPoint(4, 17)));
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
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(6, 8.25)));
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
			assertSame(firstObject, getS().getFirstShapeContaining(createPoint(6, 8.25)));
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
			assertEquals(Arrays.asList(firstObject), getS().getShapesContaining(createPoint(6, 8.25)));
		}

	}

	@DisplayName("getFirstShapeIntersecting")
	@Nested
	public class GetFirstShapeIntersecting {

		protected MultiShape2afp getMS() {
			return (MultiShape2afp) getS();
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertSame(firstObject, getMS().getFirstShapeIntersecting(createCircle(4.75, 8, .5)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			assertNull(getMS().getFirstShapeIntersecting(path));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			path.closePath();
			assertSame(firstObject, getMS().getFirstShapeIntersecting(path));
		}

	}
	
	@DisplayName("getShapesIntersecting")
	@Nested
	public class GetShapesIntersecting {

		protected MultiShape2afp getMS() {
			return (MultiShape2afp) getS();
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(Arrays.asList(firstObject), getMS().getShapesIntersecting(createCircle(4.75, 8, .5)));
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			assertTrue(getMS().getShapesIntersecting(path).isEmpty());
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp path = createPath();
			path.moveTo(-6, 2);
			path.lineTo(10, 6);
			path.lineTo(8, 14);
			path.lineTo(-4, 12);
			path.lineTo(-12, 22);
			path.lineTo(6, 20);
			path.closePath();
			assertEquals(Arrays.asList(firstObject, secondObject), getMS().getShapesIntersecting(path));
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
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
	
			firstObject.translate(12, -7);
			
			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9
			
			// R': 17;  1; 19;  2 
			
			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(1, box.getMinY());
			assertEpsilonEquals(19, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());

			secondObject.translate(12, -7);
			
			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9
			
			// C':  5;  9;  9; 13 

			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(5, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(9, box.getMaxX());
			assertEpsilonEquals(13, box.getMaxY());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());

			getS().remove(secondObject);
			secondObject.translate(1453,  -451);
			
			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(5, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(9, box.getMaxY());
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
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
	
			getS().add((C) createCircle(10, 14, 1));
			
			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9
			
			// C':  9;  13;  11; 15 
	
			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(11, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
	
			getS().remove(firstObject);
			
			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9
			
			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(16, box.getMinY());
			assertEpsilonEquals(-3, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
		}
	
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(-7, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(20, box.getMaxY());
	
			getS().remove(secondObject);
			
			// C:  -7; 16; -3; 20
			// R:   5;  8;  7;  9
	
			box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(5, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(9, box.getMaxY());
		}

	}

}
