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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Shape2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractCircle2afpTestCase<T extends Circle2afp<?, T, ?, ?, ?, B>,
		B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractShape2afpTestCase<T, B> {

	@Override
	protected final T createShape() {
		return (T) createCircle(5, 8, 5);
	}

	protected final Path2afp<?, ?, ?, ?, ?, ?> createNonEmptyPath(double x, double y) {
		Path2afp<?, ?, ?, ?, ?, ?> path = createPath();
		path.moveTo(x, y);
		path.lineTo(x + 1, y + .5);
		path.lineTo(x, y + 1);
		return path;
	}

	protected MultiShape2afp<?, ?, ?, ?, ?, ?, ?> createNonEmptyMultishape(double x, double y) {
		MultiShape2afp multishape = createMultiShape();
		multishape.add(createRectangle(x, y, 2, 1));
		return multishape;
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
			assertEpsilonEquals(5, clone.getX());
			assertEpsilonEquals(8, clone.getY());
			assertEpsilonEquals(5, clone.getRadius());
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
			assertFalse(getS().equals(createCircle(0, 0, 5)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createCircle(5, 8, 6)));
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
			assertTrue(getS().equals(createCircle(5, 8, 5)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createCircle(0, 0, 5).getPathIterator()));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createCircle(5, 8, 6).getPathIterator()));
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
			assertTrue(getS().equals(createCircle(5, 8, 5).getPathIterator()));
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
			assertFalse(getS().equalsToPathIterator(createCircle(0, 0, 5).getPathIterator()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToPathIterator(createCircle(5, 8, 6).getPathIterator()));
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
			assertTrue(getS().equalsToPathIterator(createCircle(5, 8, 5).getPathIterator()));
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
			assertFalse(getS().equalsToShape((T) createCircle(0, 0, 5)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) createCircle(5, 8, 6)));
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
			assertTrue(getS().equalsToShape((T) createCircle(5, 8, 5)));
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
			assertEpsilonEquals(0, getS().getX());
			assertEpsilonEquals(0, getS().getY());
			assertEpsilonEquals(0, getS().getRadius());
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
			assertFalse(getS().contains(0,0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(11,10));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(11,50));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(9,12));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(9,11));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(8,12));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(3,7));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(10,11));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(9,10));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var shape = (T) createCircle(-1,-1,1);
			assertFalse(shape.contains(0,0));
		}

	}

	@DisplayName("contains(Point2D)")
	@Nested
	public class ContainsPoint2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(0,0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(11,10)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(11,50)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(9,12)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(9,11)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(8,12)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(3,7)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(10,11)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(9,10)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var shape = (T) createCircle(-1,-1,1);
			assertFalse(shape.contains(createPoint(0,0)));
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
			var p = getS().getClosestPointTo(createPoint(5,8));
			assertNotNull(p);
			assertEpsilonEquals(5, p.getX());
			assertEpsilonEquals(8, p.getY());
		}
			
		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(10,10));
			assertNotNull(p);
			assertEpsilonEquals(9.6424, p.getX());
			assertEpsilonEquals(9.8570, p.getY());
		}
		
		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(4,8));
			assertNotNull(p);
			assertEpsilonEquals(4, p.getX());
			assertEpsilonEquals(8, p.getY());
		}
		
		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(0,0));
			assertNotNull(p);
			assertEpsilonEquals(2.35, p.getX());
			assertEpsilonEquals(3.76, p.getY());
		}
		
		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(5,14));
			assertNotNull(p);
			assertEpsilonEquals(5, p.getX());
			assertEpsilonEquals(13, p.getY());
		}

		@DisplayName("(Ellipse2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1.35389, 11.42139, getS().getClosestPointTo(createEllipse(-10, 20, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(4.6533, 12.98797, getS().getClosestPointTo(createEllipse(3, 20, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(8.86842, 11.16785, getS().getClosestPointTo(createEllipse(20, 20, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(2.80524, 3.50745, getS().getClosestPointTo(createEllipse(-10, -20, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(4.8312, 3.00285, getS().getClosestPointTo(createEllipse(3, -20, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7.44697, 3.63969, getS().getClosestPointTo(createEllipse(20, -20, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createEllipse(5, 5, 2, 1));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1.095656, 11.123475, getS().getClosestPointTo(createCircle(-10, 20, 1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(4.17801, 12.93196, getS().getClosestPointTo(createCircle(3, 20, 1)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(8.90434, 11.12348, getS().getClosestPointTo(createCircle(20, 20, 1)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(2.63889, 3.5926, getS().getClosestPointTo(createCircle(-10, -20, 1)));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(4.64376, 3.01271, getS().getClosestPointTo(createCircle(3, -20, 1)));
		}

		@DisplayName("(Circle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7.36111, 3.5926, getS().getClosestPointTo(createCircle(20, -20, 1)));
		}

		@DisplayName("(Circle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(5, 5, 1));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1.32598, 11.3914, getS().getClosestPointTo(createRectangle(-10, 20, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 13, getS().getClosestPointTo(createRectangle(3, 20, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(8.90434, 11.12348, getS().getClosestPointTo(createRectangle(20, 20, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(2.83092, 3.49499, getS().getClosestPointTo(createRectangle(-10, -20, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 3, getS().getClosestPointTo(createRectangle(3, -20, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7.42821, 3.62921, getS().getClosestPointTo(createRectangle(20, -20, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(5, 5, 2, 1));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1.27031, 11.33008, getS().getClosestPointTo(createSegment(-10, 20, -9, 20.5)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(4.17801, 12.93197, getS().getClosestPointTo(createSegment(3, 20, 4, 20.5)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(8.90434, 11.12348, getS().getClosestPointTo(createSegment(20, 20, 21, 20.5)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(2.73158, 3.54418, getS().getClosestPointTo(createSegment(-10, -20, -9, -19.5)));
		}

		@DisplayName("(Segment2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(4.81830, 3.0033, getS().getClosestPointTo(createSegment(3, -20, 4, -19.5)));
		}

		@DisplayName("(Segment2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7.3611, 3.5926, getS().getClosestPointTo(createSegment(20, -20, 21, -19.5)));
		}

		@DisplayName("(Segment2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(5, 5, 6, 5.5));
		}

		@DisplayName("(Triangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1.270312, 11.33007, getS().getClosestPointTo(createTriangle(-10, 20, -9, 20.5, -10, 20.5)));
		}

		@DisplayName("(Triangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(4.17801, 12.93197, getS().getClosestPointTo(createTriangle(3, 20, 4, 20.5, 3, 20.5)));
		}

		@DisplayName("(Triangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(8.90434, 11.12348, getS().getClosestPointTo(createTriangle(20, 20, 21, 20.5, 20, 20.5)));
		}

		@DisplayName("(Triangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(2.73158, 3.54418, getS().getClosestPointTo(createTriangle(-10, -20, -9, -19.5, -10, -19.5)));
		}

		@DisplayName("(Triangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(4.8183, 3.0033, getS().getClosestPointTo(createTriangle(3, -20, 4, -19.5, 3, -19.5)));
		}

		@DisplayName("(Triangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7.39426, 3.61052, getS().getClosestPointTo(createTriangle(20, -20, 21, -19.5, 20, -19.5)));
		}

		@DisplayName("(Triangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTriangle(5, 5, 6, 5.5, 5, 5.5));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1.270312, 11.33007, getS().getClosestPointTo(createNonEmptyPath(-10, 20)));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(4.17801, 12.93197, getS().getClosestPointTo(createNonEmptyPath(3, 20)));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(8.90434, 11.12348, getS().getClosestPointTo(createNonEmptyPath(20, 20)));
		}

		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(2.73158, 3.54418, getS().getClosestPointTo(createNonEmptyPath(-10, -20)));
		}

		@DisplayName("(Path2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(4.63064, 3.01366, getS().getClosestPointTo(createNonEmptyPath(3, -20)));
		}

		@DisplayName("(Path2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7.42821, 3.62921, getS().getClosestPointTo(createNonEmptyPath(20, -20)));
		}

		@DisplayName("(Path2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createNonEmptyPath(5, 5));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			assertFpPointEquals(1.35713, 11.42484, getS().getClosestPointTo(createOrientedRectangle(-10, 20, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			assertFpPointEquals(3.44488, 12.75201, getS().getClosestPointTo(createOrientedRectangle(3, 20, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			assertFpPointEquals(9.00459, 10.99387, getS().getClosestPointTo(createOrientedRectangle(20, 20, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			assertFpPointEquals(2.68943, 3.5659, getS().getClosestPointTo(createOrientedRectangle(-10, -20, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			assertFpPointEquals(4.87445, 3.00158, getS().getClosestPointTo(createOrientedRectangle(3, -20, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			assertFpPointEquals(7.23607, 3.52786, getS().getClosestPointTo(createOrientedRectangle(20, -20, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			assertClosestPointInBothShapes(getS(), createOrientedRectangle(5, 5, u.getX(), u.getY(), 2, 1));
		}

		@DisplayName("(OrientedRectangle2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			// In multishape.ggb
			assertFpPointEquals(-3.11699, 17.32601, createCircle(-5, 18, 2).getClosestPointTo(
			        createOrientedRectangle(0, 16, -0.624695047554424, 0.780868809443032, 2, 1)));
		}

		@DisplayName("(Parallelogram2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			Vector2D v = createVector(-3, 1).toUnitVector();
			assertFpPointEquals(1.50968, 11.58018, getS().getClosestPointTo(createParallelogram(-10, 20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			Vector2D v = createVector(-3, 1).toUnitVector();
			assertFpPointEquals(3.72717, 12.83528, getS().getClosestPointTo(createParallelogram(3, 20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			Vector2D v = createVector(-3, 1).toUnitVector();
			assertFpPointEquals(8.65872, 11.4079, getS().getClosestPointTo(createParallelogram(20, 20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			Vector2D v = createVector(-3, 1).toUnitVector();
			assertFpPointEquals(2.95889, 3.43559, getS().getClosestPointTo(createParallelogram(-10, -20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			Vector2D v = createVector(-3, 1).toUnitVector();
			assertFpPointEquals(4.783731, 3.00468, getS().getClosestPointTo(createParallelogram(3, -20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			Vector2D v = createVector(-3, 1).toUnitVector();
			assertFpPointEquals(7.23607, 3.52786, getS().getClosestPointTo(createParallelogram(20, -20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			Vector2D v = createVector(-3, 1).toUnitVector();
			assertClosestPointInBothShapes(getS(), createParallelogram(5, 5, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1));
		}

		@DisplayName("(RoundRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1.32598, 11.3914, getS().getClosestPointTo(createRoundRectangle(-10, 20, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(4.91936, 12.99935, getS().getClosestPointTo(createRoundRectangle(3, 20, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(8.89785, 11.13157, getS().getClosestPointTo(createRoundRectangle(20, 20, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(2.83092, 3.49499, getS().getClosestPointTo(createRoundRectangle(-10, -20, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(4.96350, 3.00013, getS().getClosestPointTo(createRoundRectangle(3, -20, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7.42821, 3.62921, getS().getClosestPointTo(createRoundRectangle(20, -20, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void roundrectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(5, 5, 2, 1, .2, .1));
		}

		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1.32598, 11.3914, getS().getClosestPointTo(createNonEmptyMultishape(-10, 20)));
		}

		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 13, getS().getClosestPointTo(createNonEmptyMultishape(3, 20)));
		}

		@DisplayName("(MultiShape2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(8.90434, 11.12348, getS().getClosestPointTo(createNonEmptyMultishape(20, 20)));
		}

		@DisplayName("(MultiShape2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(2.83092, 3.49499, getS().getClosestPointTo(createNonEmptyMultishape(-10, -20)));
		}

		@DisplayName("(MultiShape2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 3, getS().getClosestPointTo(createNonEmptyMultishape(3, -20)));
		}

		@DisplayName("(MultiShape2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7.42821, 3.62921, getS().getClosestPointTo(createNonEmptyMultishape(20, -20)));
		}

		@DisplayName("(MultiShape2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createNonEmptyMultishape(5, 5));
		}

	}

	@DisplayName("getFarthestPointTo(Point2D)")
	@Nested
	public class GetFarthestPointToPoint2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(0, 0));
			assertEpsilonEquals(7.65, p.getX());
			assertEpsilonEquals(12.24, p.getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(.5, .1));
			assertEpsilonEquals(7.4748, p.getX());
			assertEpsilonEquals(12.3446, p.getY());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(-1.2,-3.4));
			assertEpsilonEquals(7.3889, p.getX());
			assertEpsilonEquals(12.3924, p.getY());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(-1.2,5.6));
			assertEpsilonEquals(9.6628, p.getX());
			assertEpsilonEquals(9.8050, p.getY());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(7.6,5.6));
			assertEpsilonEquals(1.326, p.getX());
			assertEpsilonEquals(11.3914, p.getY());
		}

	}

	@DisplayName("getDistance(Point2D)")
	@Nested
	public class GetDistancePoint2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var d = getS().getDistance(createPoint(.5,.5));
			assertEpsilonEquals(3.74643,d);
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var d = getS().getDistance(createPoint(-1.2,-3.4));
			assertEpsilonEquals(7.9769,d);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var d = getS().getDistance(createPoint(-1.2,5.6));
			assertEpsilonEquals(1.6483,d);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var d = getS().getDistance(createPoint(7.6,5.6));
			assertEpsilonEquals(0,d);
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
			var d = getS().getDistanceSquared(createPoint(.5,.5));
			assertEpsilonEquals(14.03572,d);
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var d = getS().getDistanceSquared(createPoint(-1.2,-3.4));
			assertEpsilonEquals(63.631,d);
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var d = getS().getDistanceSquared(createPoint(-1.2,5.6));
			assertEpsilonEquals(2.7169,d);
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var d = getS().getDistanceSquared(createPoint(7.6,5.6));
			assertEpsilonEquals(0,d);
		}

		@DisplayName("(Ellipse2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(167.88585, getS().getDistanceSquared(createEllipse(-10, 20, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(49.50091, getS().getDistanceSquared(createEllipse(3, 20, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(209.16625, getS().getDistanceSquared(createEllipse(20, 20, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(636.36568, getS().getDistanceSquared(createEllipse(-10, -20, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(484.75874, getS().getDistanceSquared(createEllipse(3, -20, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(684.16615, getS().getDistanceSquared(createEllipse(20, -20, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void ellipse_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createEllipse(5, 5, 2, 1)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(174.48753, getS().getDistanceSquared(createCircle(-10, 20, 1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(38.0137, getS().getDistanceSquared(createCircle(3, 20, 1)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(174.48753, getS().getDistanceSquared(createCircle(20, 20, 1)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(663.82288, getS().getDistanceSquared(createCircle(-10, -20, 1)));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(487.14395, getS().getDistanceSquared(createCircle(3, -20, 1)));
		}

		@DisplayName("(Circle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(663.82288, getS().getDistanceSquared(createCircle(20, -20, 1)));
		}

		@DisplayName("(Circle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(5, 5, 1)));
		}
		
		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(161.08194, getS().getDistanceSquared(createRectangle(-10, 20, 2, 1)));
		}
		
		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(49, getS().getDistanceSquared(createRectangle(3, 20, 2, 1)));
		}
		
		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(201.90627, getS().getDistanceSquared(createRectangle(20, 20, 2, 1)));
		}
		
		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(623.33352, getS().getDistanceSquared(createRectangle(-10, -20, 2, 1)));
		}
		
		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(484, getS().getDistanceSquared(createRectangle(3, -20, 2, 1)));
		}
		
		@DisplayName("(Rectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(670.1311, getS().getDistanceSquared(createRectangle(20, -20, 2, 1)));
		}
		
		@DisplayName("(Rectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(5, 5, 2, 1)));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(189.56676, getS().getDistanceSquared(createSegment(-10, 20, -9, 20.5)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(51.34475, getS().getDistanceSquared(createSegment(3, 20, 4, 20.5)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(201.90627, getS().getDistanceSquared(createSegment(20, 20, 21, 20.5)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(668.66452, getS().getDistanceSquared(createSegment(-10, -20, -9, -19.5)));
		}

		@DisplayName("(Segment2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(507.06824, getS().getDistanceSquared(createSegment(3, -20, 4, -19.5)));
		}

		@DisplayName("(Segment2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(716.3524, getS().getDistanceSquared(createSegment(20, -20, 21, -19.5)));
		}

		@DisplayName("(Segment2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(5, 5, 6, 5.5)));
		}
		
		@DisplayName("(Triangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(189.56676, getS().getDistanceSquared(createTriangle(-10, 20, -9, 20.5, -10, 20.5)));
		}
		
		@DisplayName("(Triangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(51.34475, getS().getDistanceSquared(createTriangle(3, 20, 4, 20.5, 3, 20.5)));
		}
		
		@DisplayName("(Triangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(201.90627, getS().getDistanceSquared(createTriangle(20, 20, 21, 20.5, 20, 20.5)));
		}
		
		@DisplayName("(Triangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(668.66452, getS().getDistanceSquared(createTriangle(-10, -20, -9, -19.5, -10, -19.5)));
		}
		
		@DisplayName("(Triangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(507.06824, getS().getDistanceSquared(createTriangle(3, -20, 4, -19.5, 3, -19.5)));
		}
		
		@DisplayName("(Triangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(693.0009, getS().getDistanceSquared(createTriangle(20, -20, 21, -19.5, 20, -19.5)));
		}
		
		@DisplayName("(Triangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void triangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTriangle(5, 5, 6, 5.5, 5, 5.5)));
		}
		
		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(189.56676, getS().getDistanceSquared(createNonEmptyPath(-10, 20)));
		}
		
		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(51.34475, getS().getDistanceSquared(createNonEmptyPath(3, 20)));
		}
		
		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(201.90627, getS().getDistanceSquared(createNonEmptyPath(20, 20)));
		}
		
		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(668.66452, getS().getDistanceSquared(createNonEmptyPath(-10, -20)));
		}
		
		@DisplayName("(Path2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(487.26027, getS().getDistanceSquared(createNonEmptyPath(3, -20)));
		}
		
		@DisplayName("(Path2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(670.1311, getS().getDistanceSquared(createNonEmptyPath(20, -20)));
		}
		
		@DisplayName("(Path2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createNonEmptyPath(5, 5)));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			assertEpsilonEquals(156.72722, getS().getDistanceSquared(createOrientedRectangle(-10, 20, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			assertEpsilonEquals(32.99382, getS().getDistanceSquared(createOrientedRectangle(3, 20, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			assertEpsilonEquals(145.28432, getS().getDistanceSquared(createOrientedRectangle(20, 20, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			assertEpsilonEquals(603.01192, getS().getDistanceSquared(createOrientedRectangle(-10, -20, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			assertEpsilonEquals(450.26347, getS().getDistanceSquared(createOrientedRectangle(3, -20, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			assertEpsilonEquals(663.17402, getS().getDistanceSquared(createOrientedRectangle(20, -20, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void orientedrectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			assertEpsilonEquals(0, getS().getDistanceSquared(createOrientedRectangle(5, 5, u.getX(), u.getY(), 2, 1)));
		}

		@DisplayName("(Parallelogram2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			Vector2D v = createVector(-3, 1).toUnitVector();
			assertEpsilonEquals(157.91489, getS().getDistanceSquared(createParallelogram(-10, 20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			Vector2D v = createVector(-3, 1).toUnitVector();
			assertEpsilonEquals(37.90748, getS().getDistanceSquared(createParallelogram(3, 20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			Vector2D v = createVector(-3, 1).toUnitVector();
			assertEpsilonEquals(138.24702, getS().getDistanceSquared(createParallelogram(20, 20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			Vector2D v = createVector(-3, 1).toUnitVector();
			assertEpsilonEquals(626.93619, getS().getDistanceSquared(createParallelogram(-10, -20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			Vector2D v = createVector(-3, 1).toUnitVector();
			assertEpsilonEquals(475.86981, getS().getDistanceSquared(createParallelogram(3, -20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			Vector2D v = createVector(-3, 1).toUnitVector();
			assertEpsilonEquals(678.34507, getS().getDistanceSquared(createParallelogram(20, -20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(Parallelogram2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void parallelogram_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D u = createVector(2, 1).toUnitVector();
			Vector2D v = createVector(-3, 1).toUnitVector();
			assertEpsilonEquals(0, getS().getDistanceSquared(createParallelogram(5, 5, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		}

		@DisplayName("(RoundRectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rondrectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(162.42896, getS().getDistanceSquared(createRoundRectangle(-10, 20, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rondrectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(49.02258, getS().getDistanceSquared(createRoundRectangle(3, 20, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rondrectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(203.34342, getS().getDistanceSquared(createRoundRectangle(20, 20, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rondrectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(625.92327, getS().getDistanceSquared(createRoundRectangle(-10, -20, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rondrectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(484.03212, getS().getDistanceSquared(createRoundRectangle(3, -20, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rondrectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(672.9238, getS().getDistanceSquared(createRoundRectangle(20, -20, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rondrectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(5, 5, 2, 1, .2, .1)));
		}
		
		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(161.08194, getS().getDistanceSquared(createNonEmptyMultishape(-10, 20)));
		}
		
		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(49, getS().getDistanceSquared(createNonEmptyMultishape(3, 20)));
		}
		
		@DisplayName("(MultiShape2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(201.90627, getS().getDistanceSquared(createNonEmptyMultishape(20, 20)));
		}
		
		@DisplayName("(MultiShape2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(623.33352, getS().getDistanceSquared(createNonEmptyMultishape(-10, -20)));
		}
		
		@DisplayName("(MultiShape2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		}
		
		@DisplayName("(MultiShape2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(484, getS().getDistanceSquared(createNonEmptyMultishape(3, -20)));
		}
		
		@DisplayName("(MultiShape2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(670.1311, getS().getDistanceSquared(createNonEmptyMultishape(20, -20)));
		}
		
		@DisplayName("(MultiShape2afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createNonEmptyMultishape(5, 5)));
		}

	}

	@DisplayName("getDistanceL1(Point2D)")
	@Nested
	public class GetDistanceL1Point2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var d = getS().getDistanceL1(createPoint(.5,.5));
			assertEpsilonEquals(5.14005,d);
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var d = getS().getDistanceL1(createPoint(-1.2,-3.4));
			assertEpsilonEquals(10.81872,d);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var d = getS().getDistanceL1(createPoint(-1.2,5.6));
			assertEpsilonEquals(2.1322,d);
	
			d = getS().getDistanceL1(createPoint(7.6,5.6));
			assertEpsilonEquals(0,d);
		}

	}

	@DisplayName("getDistanceLinf(Point2D)")
	@Nested
	public class GetDistanceLinfPoint2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var d = getS().getDistanceLinf(createPoint(.5,.5));
			assertEpsilonEquals(3.2125,d);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var d = getS().getDistanceLinf(createPoint(-1.2,-3.4));
			assertEpsilonEquals(7.0076,d);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var d = getS().getDistanceLinf(createPoint(-1.2,5.6));
			assertEpsilonEquals(1.53716,d);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var d = getS().getDistanceLinf(createPoint(7.6,5.6));
			assertEpsilonEquals(0,d);
		}

	}

	@DisplayName("set(IT)")
	@Nested
	public class SetIT {

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set((T) createCircle(17, 20, 7));
			assertEpsilonEquals(17, getS().getX());
			assertEpsilonEquals(20, getS().getY());
			assertEpsilonEquals(7, getS().getRadius());
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
			getS().translate(123.456, -789.123);
			assertEpsilonEquals(128.456, getS().getX());
			assertEpsilonEquals(-781.123, getS().getY());
			assertEpsilonEquals(5, getS().getRadius());
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
			getS().translate(createVector(123.456, -789.123));
			assertEpsilonEquals(128.456, getS().getX());
			assertEpsilonEquals(-781.123, getS().getY());
			assertEpsilonEquals(5, getS().getRadius());
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
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(3, box.getMinY());
			assertEpsilonEquals(10, box.getMaxX());
			assertEpsilonEquals(13, box.getMaxY());
		}

	}

	@DisplayName("toBoundingBox")
	@Nested
	public class ToBoundingBoxB {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = createRectangle(0, 0, 0, 0);
			getS().toBoundingBox(box);
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(3, box.getMinY());
			assertEpsilonEquals(10, box.getMaxX());
			assertEpsilonEquals(13, box.getMaxY());
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
			assertElement(pi, PathElementType.MOVE_TO, 10, 8);
			assertElement(pi, PathElementType.CURVE_TO, 10, 10.76142374915397, 7.761423749153966, 13, 5, 13);
			assertElement(pi, PathElementType.CURVE_TO, 2.238576250846033, 13, 0, 10.76142374915397, 0, 8);
			assertElement(pi, PathElementType.CURVE_TO, 0, 5.238576250846034, 2.238576250846033, 3, 5, 3);
			assertElement(pi, PathElementType.CURVE_TO, 7.761423749153966, 3, 10, 5.238576250846034, 10, 8);
			assertElement(pi, PathElementType.CLOSE, 10, 8);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			var pi = getS().getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 10, 8);
			assertElement(pi, PathElementType.CURVE_TO, 10, 10.76142374915397, 7.761423749153966, 13, 5, 13);
			assertElement(pi, PathElementType.CURVE_TO, 2.238576250846033, 13, 0, 10.76142374915397, 0, 8);
			assertElement(pi, PathElementType.CURVE_TO, 0, 5.238576250846034, 2.238576250846033, 3, 5, 3);
			assertElement(pi, PathElementType.CURVE_TO, 7.761423749153966, 3, 10, 5.238576250846034, 10, 8);
			assertElement(pi, PathElementType.CLOSE, 10, 8);
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
			assertElement(pi, PathElementType.MOVE_TO, 20, -2);
			assertElement(pi, PathElementType.CURVE_TO, 20, 0.76142374915397, 17.761423749153966, 3, 15, 3);
			assertElement(pi, PathElementType.CURVE_TO, 12.238576250846033, 3, 10, 0.76142374915397, 10, -2);
			assertElement(pi, PathElementType.CURVE_TO, 10, -4.761423749153966, 12.238576250846033, -7, 15, -7);
			assertElement(pi, PathElementType.CURVE_TO, 17.761423749153966, -7, 20, -4.761423749153966, 20, -2);
			assertElement(pi, PathElementType.CLOSE, 20, -2);
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
			assertElement(pi, PathElementType.MOVE_TO, 10, 8);
			assertElement(pi, PathElementType.CURVE_TO, 10, 10.76142374915397, 7.761423749153966, 13, 5, 13);
			assertElement(pi, PathElementType.CURVE_TO, 2.238576250846033, 13, 0, 10.76142374915397, 0, 8);
			assertElement(pi, PathElementType.CURVE_TO, 0, 5.238576250846034, 2.238576250846033, 3, 5, 3);
			assertElement(pi, PathElementType.CURVE_TO, 7.761423749153966, 3, 10, 5.238576250846034, 10, 8);
			assertElement(pi, PathElementType.CLOSE, 10, 8);
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
			assertTrue(newShape instanceof Path2afp);
			PathIterator2afp pi = getS().getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 20, -2);
			assertElement(pi, PathElementType.CURVE_TO, 20, 0.76142374915397, 17.761423749153966, 3, 15, 3);
			assertElement(pi, PathElementType.CURVE_TO, 12.238576250846033, 3, 10, 0.76142374915397, 10, -2);
			assertElement(pi, PathElementType.CURVE_TO, 10, -4.761423749153966, 12.238576250846033, -7, 15, -7);
			assertElement(pi, PathElementType.CURVE_TO, 17.761423749153966, -7, 20, -4.761423749153966, 20, -2);
			assertElement(pi, PathElementType.CLOSE, 20, -2);
			assertNoElement(pi);
		}

	}

	@DisplayName("contains(Rectangle2afp)")
	@Nested
	public class ContainsRectangle2afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(-4, -4, 1, 1)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(-5, -5, 10, 10)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(-5, -5, 5.5, 5.5)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(-5, -4, 5.5, 1)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(20, .5, 1, 1)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(-5, -5, 1, 1)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(-1, -100, 1, 200)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(-1, -100, 1.0001, 200)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(-1, 2, 1.0001, 1.0001)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createRectangle(2, 4, 6, 4)));
		}

	}

	@DisplayName("contains(Shape2D)")
	@Nested
	public class ContainsShape2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(-4, -4, 1)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(-5, -5, 10)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(-5, -5, 5.5)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(-5, -4, 5.5)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(20, .5, 1)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(-5, -5, 1)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(-1, -100, 1)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(-1, -100, 1.0001)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(-1, 2, 1.0001)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(2, 4, 6)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createCircle(4, 8, 1)));
		}

	}

	@DisplayName("intersects(Rectangle2afp)")
	@Nested
	public class IntersectsRectangle2afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(-4, -4, 1, 1)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(-5, -5, 10, 10)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(-5, -5, 5.5, 5.5)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(-5, -4, 5.5, 1)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(20, .5, 1, 1)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(-5, -5, 1, 1)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(-1, -100, 1, 200)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(-1, -100, 1.0001, 200)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(-1, 2, 1.0001, 1.0001)));
		}

	}

	@DisplayName("intersects(Triangle2afp)")
	@Nested
	public class IntersectsTriangle2afp {

		private Triangle2afp triangle;

		@BeforeEach
		public void setUp() {
			triangle = createTriangle(5, 8, -10, 1, -1, -2);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createCircle(5, 8, 1).intersects(triangle));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createCircle(-10, 1, 1).intersects(triangle));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createCircle(-1, -2, 1).intersects(triangle));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createCircle(2, 0, 1).intersects(triangle));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createCircle(1.9, 0, 1).intersects(triangle));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createCircle(1.8, 0, 1).intersects(triangle));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createCircle(1.7, 0, 1).intersects(triangle));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createCircle(1.6, 0, 1).intersects(triangle));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createCircle(1.5, 0, 1).intersects(triangle));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createCircle(1.4, 0, 1).intersects(triangle));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createCircle(1.3, 0, 1).intersects(triangle));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createCircle(5, 9, 1).intersects(triangle));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createCircle(5, 8.9, 1).intersects(triangle));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createCircle(-4, 1, 1).intersects(triangle));
		}

	}

	@DisplayName("intersects(OrientedRectangle2afp)")
	@Nested
	public class IntersectsOrientedRectangle2afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createOrientedRectangle(
					// Center
					4.518, 7.166,
					// Axis 1
					0.89669, 0.44267,
					// Extent 1
					1.93825,
					// Extent 2
					1.35546)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createOrientedRectangle(
					// Center
					9.886, 3.316,
					// Axis 1
					0.79028, 0.61275,
					// Extent 1
					3.84169,
					// Extent 2
					1.43961)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createOrientedRectangle(
					// Center
					4.518, 7.166,
					// Axis 1
					0.89669, 0.44267,
					// Extent 1
					1.93825,
					// Extent 2
					1.35546)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createOrientedRectangle(
					// Center
					10.216, 5.23,
					// Axis 1
					0.27204, 0.96229,
					// Extent 1
					5.41835,
					// Extent 2
					1.76987)));
		}

	}

	@DisplayName("intersects(Parallelogram2afp)")
	@Nested
	public class IntersectsParallelogram2afp {

		private Parallelogram2afp para;

		@BeforeEach
		public void setUp() {
			para = createParallelogram(
					6, 9,
					2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createCircle(.5, .5, .5).intersects(para));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createCircle(.5, 1.5, .5).intersects(para));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createCircle(.5, 2.5, .5).intersects(para));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createCircle(.5, 3.5, .5).intersects(para));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createCircle(4.5, 3.5, .5).intersects(para));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createCircle(10, -7, .5).intersects(para));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createCircle(10.1, -7, .5).intersects(para));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createCircle(10.2, -7, .5).intersects(para));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createCircle(10, -1, 5).intersects(para));
		}

	}
	
	@DisplayName("intersects(RoundRectangle2afp)")
	@Nested
	public class IntersectsRoundRectangle2afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(0, 0, 1, 1, .2, .4)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(0, 2, 1, 1, .2, .4)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(0, 3, 1, 1, .2, .4)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(0, 4, 1, 1, .2, .4)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(0.1, 4, 1, 1, .2, .4)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRoundRectangle(0.2, 4, 1, 1, .2, .4)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRoundRectangle(0.3, 4, 1, 1, .2, .4)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRoundRectangle(0.4, 4, 1, 1, .2, .4)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRoundRectangle(0.5, 4, 1, 1, .2, .4)));
		}

	}

	@DisplayName("intersects(Circle2afp)")
	@Nested
	public class IntersectsCircle2afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(10, 10, 1)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(0, 0, 1)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(0, .5, 1)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(.5, 0, 1)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(.5, .5, 1)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(2, 0, 1)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(12, 8, 2)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(12, 8, 2.1)));
		}

	}

	@DisplayName("intersects(Ellipse2afp)")
	@Nested
	public class IntersectsEllipse2afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createEllipse(9, 9, 2, 2)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createEllipse(-1, -1, 2, 2)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createEllipse(-1, -.5, 2, 2)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createEllipse(-.5, -1, 2, 2)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createEllipse(-.5, -.5, 2, 2)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createEllipse(1, -1, 2, 2)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createEllipse(10, 6, 4, 4)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createEllipse(9.9, 5.9, 4.2, 4.2)));
		}

	}

	@DisplayName("intersects(Segment2afp)")
	@Nested
	public class IntersectsSegment2afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(2, 10, 6, 5)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(2, 10, 8, 14)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(0, 4, 8, 14)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(0, 4, 0, 6)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(0, 4, 0, 12)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(5, 0, 0, 6)));
		}

	}

	@DisplayName("intersects(Path2afp)")
	@Nested
	public class IntersectsPath2afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-2, -2);
			path.lineTo(-2, 2);
			path.lineTo(2, 2);
			path.lineTo(2, -2);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(0, 8);
			path.lineTo(0, 14);
			path.lineTo(10, 14);
			path.lineTo(10, 8);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(0, 2);
			path.lineTo(12, 14);
			path.lineTo(0, 14);
			assertTrue(getS().intersects(path));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-2, 2);
			path.lineTo(-2, 14);
			path.lineTo(12, 14);
			path.lineTo(12, 2);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(0, 0);
			path.lineTo(0, 4);
			path.lineTo(14, 0);
			path.lineTo(14, 4);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-8, -7);
			path.lineTo(24, 14);
			path.lineTo(-16, 14);
			path.lineTo(20, -7);
			path.lineTo(5, 21);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, -2);
			path.lineTo(-2, 2);
			path.lineTo(2, 2);
			path.lineTo(2, -2);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 8);
			path.lineTo(0, 14);
			path.lineTo(10, 14);
			path.lineTo(10, 8);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 2);
			path.lineTo(12, 14);
			path.lineTo(0, 14);
			assertTrue(getS().intersects(path));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, 2);
			path.lineTo(-2, 14);
			path.lineTo(12, 14);
			path.lineTo(12, 2);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 0);
			path.lineTo(0, 4);
			path.lineTo(14, 0);
			path.lineTo(14, 4);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-8, -7);
			path.lineTo(24, 14);
			path.lineTo(-16, 14);
			path.lineTo(20, -7);
			path.lineTo(5, 21);
			assertFalse(getS().intersects(path));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-2, -2);
			path.lineTo(-2, 2);
			path.lineTo(2, 2);
			path.lineTo(2, -2);
			path.closePath();
			assertFalse(getS().intersects(path));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(0, 8);
			path.lineTo(0, 14);
			path.lineTo(10, 14);
			path.lineTo(10, 8);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(0, 2);
			path.lineTo(12, 14);
			path.lineTo(0, 14);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-2, 2);
			path.lineTo(-2, 14);
			path.lineTo(12, 14);
			path.lineTo(12, 2);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(0, 0);
			path.lineTo(0, 4);
			path.lineTo(14, 0);
			path.lineTo(14, 4);
			path.closePath();
			assertFalse(getS().intersects(path));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-8, -7);
			path.lineTo(24, 14);
			path.lineTo(-16, 14);
			path.lineTo(20, -7);
			path.lineTo(5, 21);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, -2);
			path.lineTo(-2, 2);
			path.lineTo(2, 2);
			path.lineTo(2, -2);
			path.closePath();
			assertFalse(getS().intersects(path));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 8);
			path.lineTo(0, 14);
			path.lineTo(10, 14);
			path.lineTo(10, 8);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 2);
			path.lineTo(12, 14);
			path.lineTo(0, 14);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("#22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, 2);
			path.lineTo(-2, 14);
			path.lineTo(12, 14);
			path.lineTo(12, 2);
			path.closePath();
			assertTrue(getS().intersects(path));
		}

		@DisplayName("#23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 0);
			path.lineTo(0, 4);
			path.lineTo(14, 0);
			path.lineTo(14, 4);
			path.closePath();
			assertFalse(getS().intersects(path));
		}

		@DisplayName("#24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-8, -7);
			path.lineTo(24, 14);
			path.lineTo(-16, 14);
			path.lineTo(20, -7);
			path.lineTo(5, 21);
			path.closePath();
			assertFalse(getS().intersects(path));
		}

	}

	@DisplayName("intersects(PathIterator2afp)")
	@Nested
	public class IntersectsPathIterator2afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-2, -2);
			path.lineTo(-2, 2);
			path.lineTo(2, 2);
			path.lineTo(2, -2);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(0, 8);
			path.lineTo(0, 14);
			path.lineTo(10, 14);
			path.lineTo(10, 8);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(0, 2);
			path.lineTo(12, 14);
			path.lineTo(0, 14);
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-2, 2);
			path.lineTo(-2, 14);
			path.lineTo(12, 14);
			path.lineTo(12, 2);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(0, 0);
			path.lineTo(0, 4);
			path.lineTo(14, 0);
			path.lineTo(14, 4);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-8, -7);
			path.lineTo(24, 14);
			path.lineTo(-16, 14);
			path.lineTo(20, -7);
			path.lineTo(5, 21);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, -2);
			path.lineTo(-2, 2);
			path.lineTo(2, 2);
			path.lineTo(2, -2);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 8);
			path.lineTo(0, 14);
			path.lineTo(10, 14);
			path.lineTo(10, 8);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 2);
			path.lineTo(12, 14);
			path.lineTo(0, 14);
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, 2);
			path.lineTo(-2, 14);
			path.lineTo(12, 14);
			path.lineTo(12, 2);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 0);
			path.lineTo(0, 4);
			path.lineTo(14, 0);
			path.lineTo(14, 4);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-8, -7);
			path.lineTo(24, 14);
			path.lineTo(-16, 14);
			path.lineTo(20, -7);
			path.lineTo(5, 21);
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-2, -2);
			path.lineTo(-2, 2);
			path.lineTo(2, 2);
			path.lineTo(2, -2);
			path.closePath();
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(0, 8);
			path.lineTo(0, 14);
			path.lineTo(10, 14);
			path.lineTo(10, 8);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(0, 2);
			path.lineTo(12, 14);
			path.lineTo(0, 14);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-2, 2);
			path.lineTo(-2, 14);
			path.lineTo(12, 14);
			path.lineTo(12, 2);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(0, 0);
			path.lineTo(0, 4);
			path.lineTo(14, 0);
			path.lineTo(14, 4);
			path.closePath();
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-8, -7);
			path.lineTo(24, 14);
			path.lineTo(-16, 14);
			path.lineTo(20, -7);
			path.lineTo(5, 21);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, -2);
			path.lineTo(-2, 2);
			path.lineTo(2, 2);
			path.lineTo(2, -2);
			path.closePath();
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 8);
			path.lineTo(0, 14);
			path.lineTo(10, 14);
			path.lineTo(10, 8);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 2);
			path.lineTo(12, 14);
			path.lineTo(0, 14);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, 2);
			path.lineTo(-2, 14);
			path.lineTo(12, 14);
			path.lineTo(12, 2);
			path.closePath();
			assertTrue(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 0);
			path.lineTo(0, 4);
			path.lineTo(14, 0);
			path.lineTo(14, 4);
			path.closePath();
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

		@DisplayName("#24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-8, -7);
			path.lineTo(24, 14);
			path.lineTo(-16, 14);
			path.lineTo(20, -7);
			path.lineTo(5, 21);
			path.closePath();
			assertFalse(getS().intersects((PathIterator2afp) path.getPathIterator()));
		}

	}

	@DisplayName("containsCirclePoint")
	@Nested
	public class ContainsCirclePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticContainsCirclePoint_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.containsCirclePoint(0, 0, 1, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticContainsCirclePoint_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.containsCirclePoint(0, 0, 1, 1, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticContainsCirclePoint_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.containsCirclePoint(0, 0, 1, 0, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticContainsCirclePoint_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.containsCirclePoint(0, 0, 1, 1, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticContainsCirclePoint_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.containsCirclePoint(0, 0, 1, 1.1, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticContainsCirclePoint_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.containsCirclePoint(5, 8, 1, 5, 8));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticContainsCirclePoint_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.containsCirclePoint(5, 8, 1, 6, 8));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticContainsCirclePoint_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.containsCirclePoint(5, 8, 1, 5, 9));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticContainsCirclePoint_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.containsCirclePoint(5, 8, 1, 6, 9));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticContainsCirclePoint_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.containsCirclePoint(5, 8, 1, 6.1, 8));
		}

	}

	@DisplayName("containsCircleRectangle")
	@Nested
	public class ContainsCircleRectangle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticContainsCircleRectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.containsCircleRectangle(0, 0, 1, 0, 0, .5, .5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticContainsCircleRectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.containsCircleRectangle(0, 0, 1, 0, 0, 1, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticContainsCircleRectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.containsCircleRectangle(0, 0, 1, 0, 0, .5, 1));
		}

	}
	
	@DisplayName("intersectsCircleCircle")
	@Nested
	public class IntersectsCircleCircle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleCircle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.intersectsCircleCircle(
					0, 0, 1,
					10, 10, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleCircle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.intersectsCircleCircle(
					0, 0, 1,
					0, 0, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleCircle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.intersectsCircleCircle(
					0, 0, 1,
					0, .5, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleCircle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.intersectsCircleCircle(
					0, 0, 1,
					.5, 0, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleCircle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.intersectsCircleCircle(
					0, 0, 1,
					.5, .5, 1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleCircle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.intersectsCircleCircle(
					0, 0, 1,
					2, 0, 1));
		}

	}
	
	@DisplayName("intersectsCircleLine")
	@Nested
	public class IntersectsCircleLine {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleLine_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.intersectsCircleLine(
					0, 0, 1,
					-5, -5, -4, -4));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleLine_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.intersectsCircleLine(
					0, 0, 1,
					-5, -5, 5, 5));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleLine_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.intersectsCircleLine(
					0, 0, 1,
					-5, -5, .5, .5));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleLine_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.intersectsCircleLine(
					0, 0, 1,
					-5, -5, .5, -4));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleLine_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.intersectsCircleLine(
					0, 0, 1,
					20, .5, 21, 1.5));
		}

	}

	@DisplayName("intersectsCircleRectangle")
	@Nested
	public class IntersectsCircleRectangle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleRectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.intersectsCircleRectangle(
					0, 0, 1,
					-5, -5, -4, -4));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleRectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.intersectsCircleRectangle(
					0, 0, 1,
					-5, -5, 5, 5));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleRectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.intersectsCircleRectangle(
					0, 0, 1,
					-5, -5, .5, .5));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleRectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.intersectsCircleRectangle(
					0, 0, 1,
					-5, -5, .5, -4));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleRectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.intersectsCircleRectangle(
					0, 0, 1,
					20, .5, 21, 1.5));
		}

	}

	@DisplayName("intersectsCircleSegment")
	@Nested
	public class IntersectsCircleSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleSegment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.intersectsCircleSegment(
					0, 0, 1,
					-5, -5, -4, -4));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleSegment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.intersectsCircleSegment(
					0, 0, 1,
					-5, -5, 5, 5));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleSegment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.intersectsCircleSegment(
					0, 0, 1,
					-5, -5, .5, .5));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleSegment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.intersectsCircleSegment(
					0, 0, 1,
					-5, -5, .5, -4));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleSegment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2afp.intersectsCircleSegment(
					0, 0, 1,
					20, .5, 21, 1.5));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void staticIntersectsCircleSegment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2afp.intersectsCircleSegment(
					1, 1, 1,
					.5, -1, .5, 4));
		}

	}

	@DisplayName("getX")
	@Nested
	public class GetX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void getX(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getX());
		}

	}

	@DisplayName("getY")
	@Nested
	public class GetY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void getY(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8, getS().getY());
		}

	}

	@DisplayName("getCenter")
	@Nested
	public class GetCenter {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void getY(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D center = getS().getCenter();
			assertEpsilonEquals(5, center.getX());
			assertEpsilonEquals(8, center.getY());
		}

	}

	@DisplayName("setCenter(Point2D)")
	@Nested
	public class SetCenterPoint2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void getY(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setCenter(createPoint(123.456, 789.123));
			assertEpsilonEquals(123.456, getS().getX());
			assertEpsilonEquals(789.123, getS().getY());
			assertEpsilonEquals(5, getS().getRadius());
		}

	}

	@DisplayName("setCenter(double,double)")
	@Nested
	public class SetCenterDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void getY(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setCenter(123.456, 789.123);
			assertEpsilonEquals(123.456, getS().getX());
			assertEpsilonEquals(789.123, getS().getY());
			assertEpsilonEquals(5, getS().getRadius());
		}

	}

	@DisplayName("setX(double)")
	@Nested
	public class SetXDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void getY(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setX(123.456);
			assertEpsilonEquals(123.456, getS().getX());
			assertEpsilonEquals(8, getS().getY());
			assertEpsilonEquals(5, getS().getRadius());
		}

	}

	@DisplayName("setY(double)")
	@Nested
	public class SetYDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void getY(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setY(123.456);
			assertEpsilonEquals(5, getS().getX());
			assertEpsilonEquals(123.456, getS().getY());
			assertEpsilonEquals(5, getS().getRadius());
		}

	}

	@DisplayName("getRadius")
	@Nested
	public class GetRadius {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void getRadius(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getRadius());
		}

	}

	@DisplayName("setRadius")
	@Nested
	public class SetRadius {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void setRadius(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setRadius(123.456);
			assertEpsilonEquals(5, getS().getX());
			assertEpsilonEquals(8, getS().getY());
			assertEpsilonEquals(123.456, getS().getRadius());
		}

	}

	@DisplayName("set(double,double,double)")
	@Nested
	public class SetDoubleDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void set(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(123.456, 789.123, 456.789);
			assertEpsilonEquals(123.456, getS().getX());
			assertEpsilonEquals(789.123, getS().getY());
			assertEpsilonEquals(456.789, getS().getRadius());
		}

	}

	@DisplayName("set(Point2D,double)")
	@Nested
	public class SetPoint2DDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void set(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(createPoint(123.456, 789.123), 456.789);
			assertEpsilonEquals(123.456, getS().getX());
			assertEpsilonEquals(789.123, getS().getY());
			assertEpsilonEquals(456.789, getS().getRadius());
		}

	}

	@DisplayName("intersects(Shape2D)")
	@Nested
	public class IntersectsShape2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects((Shape2D) createCircle(10, 10, 1)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects((Shape2D) createEllipse(9, 9, 2, 2)));
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
			getS().operator_add(createVector(123.456, -789.123));
			assertEpsilonEquals(128.456, getS().getX());
			assertEpsilonEquals(-781.123, getS().getY());
			assertEpsilonEquals(5, getS().getRadius());
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
			T shape = getS().operator_plus(createVector(123.456, -789.123));
			assertEpsilonEquals(128.456, shape.getX());
			assertEpsilonEquals(-781.123, shape.getY());
			assertEpsilonEquals(5, shape.getRadius());
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
			getS().operator_remove(createVector(123.456, -789.123));
			assertEpsilonEquals(-118.456, getS().getX());
			assertEpsilonEquals(797.123, getS().getY());
			assertEpsilonEquals(5, getS().getRadius());
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
			T shape = getS().operator_minus(createVector(123.456, -789.123));
			assertEpsilonEquals(-118.456, shape.getX());
			assertEpsilonEquals(797.123, shape.getY());
			assertEpsilonEquals(5, shape.getRadius());
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
			assertTrue(newShape instanceof Path2afp);
			PathIterator2afp pi = getS().getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 20, -2);
			assertElement(pi, PathElementType.CURVE_TO, 20, 0.76142374915397, 17.761423749153966, 3, 15, 3);
			assertElement(pi, PathElementType.CURVE_TO, 12.238576250846033, 3, 10, 0.76142374915397, 10, -2);
			assertElement(pi, PathElementType.CURVE_TO, 10, -4.761423749153966, 12.238576250846033, -7, 15, -7);
			assertElement(pi, PathElementType.CURVE_TO, 17.761423749153966, -7, 20, -4.761423749153966, 20, -2);
			assertElement(pi, PathElementType.CLOSE, 20, -2);
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
			assertFalse(getS().operator_and(createPoint(0,0)));
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
			assertTrue(getS().operator_and(createPoint(9,11)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(8,12)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(3,7)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(10,11)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(9,10)));
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
			assertTrue(getS().operator_and(createCircle(10, 10, 1)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createEllipse(9, 9, 2, 2)));
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
			assertEpsilonEquals(3.74643, getS().operator_upTo(createPoint(.5,.5)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.9769, getS().operator_upTo(createPoint(-1.2,-3.4)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.6483, getS().operator_upTo(createPoint(-1.2,5.6)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(7.6,5.6)));
		}

	}

	@DisplayName("getVerticalRadius")
	@Nested
	public class GetVerticalRadius {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getVerticalRadius());
		}

	}
	
	@DisplayName("getHorizontalRadius")
	@Nested
	public class GetHorizontalRadius {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getHorizontalRadius());
		}

	}

	@DisplayName("setFromCenter(double,double,double,double)")
	@Nested
	public class SetFromCenterDoubleDoubleDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setFromCenter(152, 148, 475, -254);
			assertEpsilonEquals(152, getS().getX());
			assertEpsilonEquals(148, getS().getY());
			assertEpsilonEquals(323, getS().getRadius());
		}

	}

	@DisplayName("setFromCorners(double,double,double,double)")
	@Nested
	public class SetFromCornersDoubleDoubleDoubleDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setFromCorners(-171, 550, 475, -254);
			assertEpsilonEquals(152, getS().getX());
			assertEpsilonEquals(148, getS().getY());
			assertEpsilonEquals(323, getS().getRadius());
		}

	}

	@DisplayName("getMinX")
	@Nested
	public class GetMinX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getMinX());
		}

	}

	@DisplayName("setMinX")
	@Nested
	public class SetMinX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void setMinX_noSwap(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setMinX(-41);
			assertEpsilonEquals(-15.5, getS().getX());
			assertEpsilonEquals(8, getS().getY());
			assertEpsilonEquals(25.5, getS().getRadius());
		}
	
		@DisplayName("2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void setMinX_swap(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setMinX(41);
			assertEpsilonEquals(25.5, getS().getX());
			assertEpsilonEquals(8, getS().getY());
			assertEpsilonEquals(15.5, getS().getRadius());
		}

	}

	@DisplayName("getMaxX")
	@Nested
	public class GetMaxX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10, getS().getMaxX());
		}

	}

	@DisplayName("setMaxX")
	@Nested
	public class SetMaxX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void setMaxX_noSwap(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setMaxX(41);
			assertEpsilonEquals(20.5, getS().getX());
			assertEpsilonEquals(8, getS().getY());
			assertEpsilonEquals(20.5, getS().getRadius());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void setMaxX_swap(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setMaxX(-41);
			assertEpsilonEquals(-20.5, getS().getX());
			assertEpsilonEquals(8, getS().getY());
			assertEpsilonEquals(20.5, getS().getRadius());
		}

	}

	@DisplayName("getMinY")
	@Nested
	public class GetMinY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, getS().getMinY());
		}

	}

	@DisplayName("setMinY")
	@Nested
	public class SetMinY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void setMinY_noSwap(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setMinY(-41);
			assertEpsilonEquals(5, getS().getX());
			assertEpsilonEquals(-14, getS().getY());
			assertEpsilonEquals(27, getS().getRadius());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void setMinY_swap(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setMinY(41);
			assertEpsilonEquals(5, getS().getX());
			assertEpsilonEquals(27, getS().getY());
			assertEpsilonEquals(14, getS().getRadius());
		}

	}

	@DisplayName("getMaxY")
	@Nested
	public class GetMaxY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(13, getS().getMaxY());
		}

	}

	@DisplayName("setMaxY")
	@Nested
	public class SetMaxY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void setMaxY_noSwap(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setMaxY(41);
			assertEpsilonEquals(5, getS().getX());
			assertEpsilonEquals(22, getS().getY());
			assertEpsilonEquals(19, getS().getRadius());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void setMaxY_swap(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setMaxY(-41);
			assertEpsilonEquals(5, getS().getX());
			assertEpsilonEquals(-19, getS().getY());
			assertEpsilonEquals(22, getS().getRadius());
		}

	}

}