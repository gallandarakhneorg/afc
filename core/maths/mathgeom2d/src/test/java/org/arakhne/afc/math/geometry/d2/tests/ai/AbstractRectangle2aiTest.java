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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai.Side;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractRectangle2aiTest<T extends Rectangle2ai<?, T, ?, ?, ?, T>>
extends AbstractRectangularShape2aiTest<T, T> {

	@Override
	protected final T createShape() {
		return createRectangle(5, 8, 10, 5);
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

	@DisplayName("reducesCohenSutherlandZoneRectangleSegment")
	@Nested
	public class ReducesCohenSutherlandZoneRectangleSegment {

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
			assertEquals(0,
					Rectangle2ai.reducesCohenSutherlandZoneRectangleSegment(10, 12, 40, 37, 20, 45, 43, 15,
							MathUtil.getCohenSutherlandCode(20, 45, 0, 12, 40, 37),
							MathUtil.getCohenSutherlandCode(43, 15, 0, 12, 40, 37),
							p1, p2));
			assertIntPointEquals(26, 37, p1);
			assertIntPointEquals(40, 19, p2);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, 
					Rectangle2ai.reducesCohenSutherlandZoneRectangleSegment(10, 12, 40, 37, 20, 55, 43, 15,
							MathUtil.getCohenSutherlandCode(20, 55, 0, 12, 40, 37),
							MathUtil.getCohenSutherlandCode(43, 15, 0, 12, 40, 37),
							p1, p2));
			assertIntPointEquals(30, 37, p1);
			assertIntPointEquals(40, 21, p2);
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
			T clone = shape.clone();
			assertNotNull(clone);
			assertNotSame(shape, clone);
			assertEquals(shape.getClass(), clone.getClass());
			assertEpsilonEquals(5, clone.getMinX());
			assertEpsilonEquals(8, clone.getMinY());
			assertEpsilonEquals(15, clone.getMaxX());
			assertEpsilonEquals(13, clone.getMaxY());
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
			assertFalse(shape.equals(createRectangle(0, 0, 5, 5)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equals(createRectangle(5, 8, 10, 6)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equals(createSegment(5, 8, 10, 5)));
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
			assertTrue(shape.equals(createRectangle(5, 8, 10, 5)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equals(createRectangle(0, 0, 5, 5).getPathIterator()));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equals(createRectangle(5, 8, 10, 6).getPathIterator()));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equals(createSegment(5, 8, 10, 5).getPathIterator()));
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
			assertTrue(shape.equals(createRectangle(5, 8, 10, 5).getPathIterator()));
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
			assertFalse(shape.equalsToShape(createRectangle(0, 0, 5, 5)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equalsToShape(createRectangle(5, 8, 10, 6)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.equalsToShape(shape));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.equalsToShape(createRectangle(5, 8, 10, 5)));
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
			assertFalse(shape.equalsToPathIterator(createRectangle(0, 0, 5, 5).getPathIterator()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equalsToPathIterator(createRectangle(5, 8, 10, 6).getPathIterator()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.equalsToPathIterator(createSegment(5, 8, 10, 5).getPathIterator()));
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
			assertTrue(shape.equalsToPathIterator(createRectangle(5, 8, 10, 5).getPathIterator()));
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
			Iterator<? extends Point2D> iterator = shape.getPointIterator();
			Point2D p;

			int[] coords;

			coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
			for(int x : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(x, p.ix());
				assertEquals(8, p.iy());
			}

			coords = new int[] {9,10,11,12,13};
			for(int y : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(15, p.ix());
				assertEquals(y, p.iy());
			}

			coords = new int[] {14,13,12,11,10,9,8,7,6,5};
			for(int x : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(x, p.ix());
				assertEquals(13, p.iy());
			}

			coords = new int[] {12,11,10,9};
			for(int y : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(5, p.ix());
				assertEquals(y, p.iy());
			}

			assertFalse(iterator.hasNext());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Iterator<? extends Point2D> iterator = shape.getPointIterator(Side.TOP);
			Point2D p;

			int[] coords;

			coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
			for(int x : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(x, p.ix());
				assertEquals(8, p.iy());
			}

			coords = new int[] {9,10,11,12,13};
			for(int y : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(15, p.ix());
				assertEquals(y, p.iy());
			}

			coords = new int[] {14,13,12,11,10,9,8,7,6,5};
			for(int x : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(x, p.ix());
				assertEquals(13, p.iy());
			}

			coords = new int[] {12,11,10,9};
			for(int y : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(5, p.ix());
				assertEquals(y, p.iy());
			}

			assertFalse(iterator.hasNext());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Iterator<? extends Point2D> iterator = shape.getPointIterator(Side.RIGHT);
			Point2D p;

			int[] coords;

			coords = new int[] {9,10,11,12,13};
			for(int y : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(15, p.ix());
				assertEquals(y, p.iy());
			}

			coords = new int[] {14,13,12,11,10,9,8,7,6,5};
			for(int x : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(x, p.ix());
				assertEquals(13, p.iy());
			}

			coords = new int[] {12,11,10,9};
			for(int y : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(5, p.ix());
				assertEquals(y, p.iy());
			}

			coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
			for(int x : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(x, p.ix());
				assertEquals(8, p.iy());
			}

			assertFalse(iterator.hasNext());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Iterator<? extends Point2D> iterator = shape.getPointIterator(Side.BOTTOM);
			Point2D p;

			int[] coords;

			coords = new int[] {14,13,12,11,10,9,8,7,6,5};
			for(int x : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(x, p.ix());
				assertEquals(13, p.iy());
			}

			coords = new int[] {12,11,10,9};
			for(int y : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(5, p.ix());
				assertEquals(y, p.iy());
			}

			coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
			for(int x : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(x, p.ix());
				assertEquals(8, p.iy());
			}

			coords = new int[] {9,10,11,12,13};
			for(int y : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(15, p.ix());
				assertEquals(y, p.iy());
			}

			assertFalse(iterator.hasNext());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Iterator<? extends Point2D> iterator = shape.getPointIterator(Side.LEFT);
			Point2D p;

			int[] coords;

			coords = new int[] {12,11,10,9};
			for(int y : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(5, p.ix());
				assertEquals(y, p.iy());
			}

			coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
			for(int x : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(x, p.ix());
				assertEquals(8, p.iy());
			}

			coords = new int[] {9,10,11,12,13};
			for(int y : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(15, p.ix());
				assertEquals(y, p.iy());
			}

			coords = new int[] {14,13,12,11,10,9,8,7,6,5};
			for(int x : coords) {
				assertTrue(iterator.hasNext());
				p = iterator.next();
				assertNotNull(p);
				assertEquals(x, p.ix());
				assertEquals(13, p.iy());
			}

			assertFalse(iterator.hasNext());
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
			assertEpsilonEquals(0f, shape.getDistance(createPoint(5,8)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, shape.getDistance(createPoint(10,10)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, shape.getDistance(createPoint(4,8)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9.433981132f, shape.getDistance(createPoint(0,0)));
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
			assertEpsilonEquals(0f, shape.getDistanceSquared(createPoint(5,8)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, shape.getDistanceSquared(createPoint(10,10)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, shape.getDistanceSquared(createPoint(4,8)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(89f, shape.getDistanceSquared(createPoint(0,0)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(52, shape.getDistanceSquared(createCircle(0, 0, 2)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25, shape.getDistanceSquared(createCircle(11, 20, 2)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, shape.getDistanceSquared(createCircle(2, 10, 2)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, shape.getDistanceSquared(createCircle(16, 14, 2)));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, shape.getDistanceSquared(createCircle(11, 10, 2)));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(45, shape.getDistanceSquared(createRectangle(0, 0, 2, 2)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(49, shape.getDistanceSquared(createRectangle(11, 20, 2, 2)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, shape.getDistanceSquared(createRectangle(2, 10, 2, 2)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, shape.getDistanceSquared(createRectangle(15, 13, 2, 2)));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, shape.getDistanceSquared(createRectangle(11, 10, 2, 2)));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(45, shape.getDistanceSquared(createSegment(0, 0, 2, 2)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, shape.getDistanceSquared(createSegment(0, 0, 18, 8)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, shape.getDistanceSquared(createSegment(18, 8, 15, 14)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, shape.getDistanceSquared(createSegment(6, 10, 13, 12)));
		}

		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25, shape.getDistanceSquared(createTestMultiShape(0, 0)));
		}

		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, shape.getDistanceSquared(createTestMultiShape(18, 8)));
		}

		@DisplayName("(MultiShape2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, shape.getDistanceSquared(createTestMultiShape(6, 10)));
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
			assertEpsilonEquals(729, shape.getDistanceSquared(createTestPath(47, 8)));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, shape.getDistanceSquared(createTestPath(6, 10)));
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
			assertEpsilonEquals(0f, shape.getDistanceL1(createPoint(5,8)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, shape.getDistanceL1(createPoint(10,10)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, shape.getDistanceL1(createPoint(4,8)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(13f, shape.getDistanceL1(createPoint(0,0)));
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
			assertEpsilonEquals(0f, shape.getDistanceLinf(createPoint(5,8)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, shape.getDistanceLinf(createPoint(10,10)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, shape.getDistanceLinf(createPoint(4,8)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8f, shape.getDistanceLinf(createPoint(0,0)));
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
			var p = shape.getClosestPointTo(createPoint(5,8));
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(8, p.iy());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getClosestPointTo(createPoint(10,10));
			assertNotNull(p);
			assertEquals(10, p.ix());
			assertEquals(10, p.iy());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getClosestPointTo(createPoint(4,8));
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(8, p.iy());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getClosestPointTo(createPoint(0,0));
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(8, p.iy());
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(5, 8, shape.getClosestPointTo(createCircle(0, 0, 2)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(11, 13, shape.getClosestPointTo(createCircle(11, 20, 2)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(5, 10, shape.getClosestPointTo(createCircle(2, 10, 2)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(shape, createCircle(16, 14, 2));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(shape, createCircle(11, 10, 2));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(5, 8, shape.getClosestPointTo(createRectangle(0, 0, 2, 2)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(12, 13, shape.getClosestPointTo(createRectangle(11, 20, 2, 2)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(5, 11, shape.getClosestPointTo(createRectangle(2, 10, 2, 2)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(shape, createRectangle(15, 13, 2, 2));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(shape, createRectangle(11, 10, 2, 2));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(5, 8, shape.getClosestPointTo(createSegment(0, 0, 2, 2)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(15, 8, shape.getClosestPointTo(createSegment(0, 0, 18, 8)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(15, 13, shape.getClosestPointTo(createRectangle(18, 8, 15, 14)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(shape, createRectangle(6, 10, 13, 12));
		}

		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(5, 8, shape.getClosestPointTo(createTestMultiShape(0, 0)));
		}

		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(10, 8, shape.getClosestPointTo(createTestMultiShape(18, 8)));
		}

		@DisplayName("(MultiShape2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(shape, createTestMultiShape(6, 10));
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
			assertIntPointEquals(15, 8, shape.getClosestPointTo(createTestPath(47, 8)));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(shape, createTestPath(6, 10));
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
			var p = shape.getFarthestPointTo(createPoint(5,8));
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(13, p.iy());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getFarthestPointTo(createPoint(10,10));
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(13, p.iy());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getFarthestPointTo(createPoint(4,8));
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(13, p.iy());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getFarthestPointTo(createPoint(0,0));
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(13, p.iy());
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getFarthestPointTo(createPoint(24,0));
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(13, p.iy());
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = shape.getFarthestPointTo(createPoint(0,32));
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(8, p.iy());
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
			assertFalse(shape.contains(0,0));
		}

		@DisplayName("(double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.contains(11,10));
		}

		@DisplayName("(double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(11,50));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createPoint(0,0)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.contains(createPoint(11,10)));
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createPoint(11,50)));
		}

		@DisplayName("(Rectangle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createRectangle(0,0,1,1)));
		}

		@DisplayName("(Rectangle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createRectangle(0,0,8,1)));
		}

		@DisplayName("(Rectangle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createRectangle(0,0,8,6)));
		}

		@DisplayName("(Rectangle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createRectangle(0,0,100,100)));
		}

		@DisplayName("(Rectangle2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.contains(createRectangle(7,10,1,1)));
		}

		@DisplayName("(Rectangle2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createRectangle(16,0,100,100)));
		}

		@DisplayName("(Shape2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createCircle(0,0,1)));
		}

		@DisplayName("(Shape2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createCircle(0,0,8)));
		}

		@DisplayName("(Shape2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createCircle(0,0,6)));
		}

		@DisplayName("(Shape2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createCircle(0,0,100)));
		}

		@DisplayName("(Shape2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.contains(createCircle(7,10,1)));
		}

		@DisplayName("(Shape2ai) #")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.contains(createCircle(16,0,100)));
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
			assertFalse(shape.intersects(createRectangle(0,0,1,1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createRectangle(0,0,8,1)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createRectangle(0,0,8,6)));
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
			assertTrue(shape.intersects(createRectangle(7,10,1,1)));
		}

		@DisplayName("(Rectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createRectangle(16,0,100,100)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createCircle(0,0,1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createCircle(0,0,8)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createCircle(0,0,100)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createCircle(7,10,1)));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createCircle(16,0,5)));
		}

		@DisplayName("(Circle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createCircle(5,15,1)));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createSegment(0,0,1,1)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createSegment(0,0,8,1)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createSegment(0,0,8,6)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createSegment(0,0,100,100)));
		}

		@DisplayName("(Segment2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects(createSegment(7,10,1,1)));
		}

		@DisplayName("(Segment2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.intersects(createSegment(16,0,100,100)));
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
			shape = createRectangle(0, 0, 1, 1);
			assertTrue(shape.intersects(path));
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
			shape = createRectangle(4, 3, 1, 1);
			assertTrue(shape.intersects(path));
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
			shape = createRectangle(2, 2, 1, 1);
			assertTrue(shape.intersects(path));
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
			shape = createRectangle(2, 1, 1, 1);
			assertTrue(shape.intersects(path));
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
			shape = createRectangle(3, 0, 1, 1);
			assertTrue(shape.intersects(path));
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
			shape = createRectangle(-1, -1, 1, 1);
			assertTrue(shape.intersects(path));
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
			shape = createRectangle(4, -3, 1, 1);
			assertTrue(shape.intersects(path));
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
			shape = createRectangle(-3, 4, 1, 1);
			assertFalse(shape.intersects(path));
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
			shape = createRectangle(6, -5, 1, 1);
			assertTrue(shape.intersects(path));
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
			shape = createRectangle(4, 0, 1, 1);
			assertTrue(shape.intersects(path));
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
			shape = createRectangle(5, 0, 1, 1);
			assertTrue(shape.intersects(path));
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
			shape = createRectangle(0, -3, 1, 1);
			assertFalse(shape.intersects(path));
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
			shape = createRectangle(0, -3, 2, 1);
			assertFalse(shape.intersects(path));
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
			shape = createRectangle(0, 0, 1, 1);
			assertTrue(shape.intersects((PathIterator2ai) path.getPathIterator()));
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
			shape = createRectangle(4, 3, 1, 1);
			assertTrue(shape.intersects((PathIterator2ai) path.getPathIterator()));
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
			shape = createRectangle(2, 2, 1, 1);
			assertTrue(shape.intersects((PathIterator2ai) path.getPathIterator()));
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
			shape = createRectangle(2, 1, 1, 1);
			assertTrue(shape.intersects((PathIterator2ai) path.getPathIterator()));
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
			shape = createRectangle(3, 0, 1, 1);
			assertTrue(shape.intersects((PathIterator2ai) path.getPathIterator()));
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
			shape = createRectangle(-1, -1, 1, 1);
			assertTrue(shape.intersects((PathIterator2ai) path.getPathIterator()));
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
			shape = createRectangle(4, -3, 1, 1);
			assertTrue(shape.intersects((PathIterator2ai) path.getPathIterator()));
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
			shape = createRectangle(-3, 4, 1, 1);
			assertFalse(shape.intersects((PathIterator2ai) path.getPathIterator()));
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
			shape = createRectangle(6, -5, 1, 1);
			assertTrue(shape.intersects((PathIterator2ai) path.getPathIterator()));
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
			shape = createRectangle(4, 0, 1, 1);
			assertTrue(shape.intersects((PathIterator2ai) path.getPathIterator()));
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
			shape = createRectangle(5, 0, 1, 1);
			assertTrue(shape.intersects((PathIterator2ai) path.getPathIterator()));
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
			shape = createRectangle(0, -3, 1, 1);
			assertFalse(shape.intersects((PathIterator2ai) path.getPathIterator()));
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
			shape = createRectangle(0, -3, 2, 1);
			assertFalse(shape.intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(Shape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects((Shape2D) createCircle(0,0,100)));
		}

		@DisplayName("(Shape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(shape.intersects((Shape2D) createRectangle(7,10,1,1)));
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
			PathIterator2ai pi = shape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 5,8);
			assertElement(pi, PathElementType.LINE_TO, 15,8);
			assertElement(pi, PathElementType.LINE_TO, 15,13);
			assertElement(pi, PathElementType.LINE_TO, 5,13);
			assertElement(pi, PathElementType.CLOSE, 5,8);
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
			var pi = shape.getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 5,8);
			assertElement(pi, PathElementType.LINE_TO, 15,8);
			assertElement(pi, PathElementType.LINE_TO, 15,13);
			assertElement(pi, PathElementType.LINE_TO, 5,13);
			assertElement(pi, PathElementType.CLOSE, 5,8);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeTranslationMatrix(3.4f, 4.5f);
			var pi = shape.getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 8,13);
			assertElement(pi, PathElementType.LINE_TO, 18,13);
			assertElement(pi, PathElementType.LINE_TO, 18,18);
			assertElement(pi, PathElementType.LINE_TO, 8,18);
			assertElement(pi, PathElementType.CLOSE, 8,13);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeRotationMatrix(MathConstants.QUARTER_PI);
			var pi = shape.getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, -2,9);
			assertElement(pi, PathElementType.LINE_TO, 5,16);
			assertElement(pi, PathElementType.LINE_TO, 1,20);
			assertElement(pi, PathElementType.LINE_TO, -6,13);
			assertElement(pi, PathElementType.CLOSE, -2,9);
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
			var pi = shape.createTransformedShape(tr).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 5,8);
			assertElement(pi, PathElementType.LINE_TO, 15,8);
			assertElement(pi, PathElementType.LINE_TO, 15,13);
			assertElement(pi, PathElementType.LINE_TO, 5,13);
			assertElement(pi, PathElementType.CLOSE, 5,8);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeTranslationMatrix(3.4f, 4.5f);
			var pi = shape.createTransformedShape(tr).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 8,13);
			assertElement(pi, PathElementType.LINE_TO, 18,13);
			assertElement(pi, PathElementType.LINE_TO, 18,18);
			assertElement(pi, PathElementType.LINE_TO, 8,18);
			assertElement(pi, PathElementType.CLOSE, 8,13);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeRotationMatrix(MathConstants.QUARTER_PI);		
			var pi = shape.createTransformedShape(tr).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -2,9);
			assertElement(pi, PathElementType.LINE_TO, 5,16);
			assertElement(pi, PathElementType.LINE_TO, 1,20);
			assertElement(pi, PathElementType.LINE_TO, -6,13);
			assertElement(pi, PathElementType.CLOSE, -2,9);
			assertNoElement(pi);
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
			shape.set(createRectangle(10, 12, 14, 16));
			assertEquals(10, shape.getMinX());
			assertEquals(12, shape.getMinY());
			assertEquals(24, shape.getMaxX());
			assertEquals(28, shape.getMaxY());
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
			assertEquals(8, shape.getMinX());
			assertEquals(12, shape.getMinY());
			assertEquals(18, shape.getMaxX());
			assertEquals(17, shape.getMaxY());
		}

	}

	@DisplayName("findsClosestPointRectanglePoint")
	@Nested
	public class FindsClosestPointRectanglePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Rectangle2ai.findsClosestPointRectanglePoint(5, 8, 15, 13, 5, 8, p);
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(8, p.iy());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Rectangle2ai.findsClosestPointRectanglePoint(5, 8, 15, 13, 10, 10, p);
			assertNotNull(p);
			assertEquals(10, p.ix());
			assertEquals(10, p.iy());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Rectangle2ai.findsClosestPointRectanglePoint(5, 8, 15, 13, 4, 8, p);
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(8, p.iy());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4s(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Rectangle2ai.findsClosestPointRectanglePoint(5, 8, 15, 13, 0, 0, p);
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(8, p.iy());
		}

	}

	@DisplayName("findsClosestPointRectangleRectangle")
	@Nested
	public class FindsClosestPointRectangleRectangle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Rectangle2ai.findsClosestPointRectangleRectangle(5, 8, 15, 13, 5, 8, 7, 9, p);
			assertNotNull(p);
			assertEquals(6, p.ix());
			assertEquals(8, p.iy());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Rectangle2ai.findsClosestPointRectangleRectangle(5, 8, 15, 13, 10, 10, 12, 12, p);
			assertNotNull(p);
			assertEquals(11, p.ix());
			assertEquals(11, p.iy());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Rectangle2ai.findsClosestPointRectangleRectangle(5, 8, 15, 13, 4, 8, 6, 10, p);
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(9, p.iy());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Rectangle2ai.findsClosestPointRectangleRectangle(5, 8, 15, 13, 0, 0, 2, 2, p);
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(8, p.iy());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Rectangle2ai.findsClosestPointRectangleRectangle(5, 8, 15, 13, 7, 20, 50, 32, p);
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(13, p.iy());
		}

	}

	@DisplayName("findsClosestPointRectangleSegment")
	@Nested
	public class FindsClosestPointRectangleSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Rectangle2ai.findsClosestPointRectangleSegment(5, 8, 15, 13, 5, 8, 7, 9, p);
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(8, p.iy());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Rectangle2ai.findsClosestPointRectangleSegment(5, 8, 15, 13, 10, 10, 12, 12, p);
			assertNotNull(p);
			assertEquals(10, p.ix());
			assertEquals(10, p.iy());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Rectangle2ai.findsClosestPointRectangleSegment(5, 8, 15, 13, 4, 8, 6, 10, p);
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(9, p.iy());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Rectangle2ai.findsClosestPointRectangleSegment(5, 8, 15, 13, 0, 0, 2, 2, p);
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(8, p.iy());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(0, 0);
			Rectangle2ai.findsClosestPointRectangleSegment(5, 8, 15, 13, 7, 20, 50, 32, p);
			assertNotNull(p);
			assertEquals(7, p.ix());
			assertEquals(13, p.iy());
		}
	}

	@DisplayName("findsFarthestPointRectanglePoint")
	@Nested
	public class FindsFarthestPointRectanglePoint {

		private Point2D p;

		@BeforeEach
		public void setUp() {
			p = createPoint(0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2ai.findsFarthestPointRectanglePoint(5, 8, 15, 13, 5, 8, p);
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(13, p.iy());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			p = createPoint(0, 0);
			Rectangle2ai.findsFarthestPointRectanglePoint(5, 8, 15, 13, 10, 10, p);
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(13, p.iy());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			p = createPoint(0, 0);
			Rectangle2ai.findsFarthestPointRectanglePoint(5, 8, 15, 13, 4, 8, p);
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(13, p.iy());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			p = createPoint(0, 0);
			Rectangle2ai.findsFarthestPointRectanglePoint(5, 8, 15, 13, 0, 0, p);
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(13, p.iy());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			p = createPoint(0, 0);
			Rectangle2ai.findsFarthestPointRectanglePoint(5, 8, 15, 13, 24, 0, p);
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(13, p.iy());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			p = createPoint(0, 0);
			Rectangle2ai.findsFarthestPointRectanglePoint(5, 8, 15, 13, 0, 32, p);
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(8, p.iy());
		}
	}

	@DisplayName("intersectsRectangleRectangle")
	@Nested
	public class IntersectsRectangleRectangle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 0, 0, 1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 0, 0, 8, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 0, 0, 8, 6));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 0, 0, 100, 100));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 7, 10, 8, 11));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 16, 0,116, 100));
		}
	}

	@DisplayName("intersectsRectangleSegment")
	@Nested
	public class IntersectsRectangleSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 0, 0, 1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 0, 0, 8, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 0, 0, 8, 6));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 0, 0, 100, 100));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 7, 10, 8, 11));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 16, 0, 116, 100));
		}

	}

	@DisplayName("inflate")
	@Nested
	public class Inflate {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.inflate(1, 2, 3, 4);
			assertEquals(4, shape.getMinX());
			assertEquals(6, shape.getMinY());
			assertEquals(18, shape.getMaxX());
			assertEquals(17, shape.getMaxY());
		}

	}

	@DisplayName("setUnion")
	@Nested
	public class SetUnion {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setUnion(createRectangle(0, 0, 12, 1));
			assertEquals(0, shape.getMinX());
			assertEquals(0, shape.getMinY());
			assertEquals(15, shape.getMaxX());
			assertEquals(13, shape.getMaxY());
		}

	}

	@DisplayName("createUnion")
	@Nested
	public class CreateUnion {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			T union = shape.createUnion(createRectangle(0, 0, 12, 1));
			assertNotSame(shape, union);
			assertEquals(0, union.getMinX());
			assertEquals(0, union.getMinY());
			assertEquals(15, union.getMaxX());
			assertEquals(13, union.getMaxY());
			assertEquals(5, shape.getMinX());
			assertEquals(8, shape.getMinY());
			assertEquals(15, shape.getMaxX());
			assertEquals(13, shape.getMaxY());
		}

	}

	@DisplayName("setIntersection")
	@Nested
	public class setIntersection {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setIntersection(createRectangle(0, 0, 12, 1));
			assertTrue(shape.isEmpty());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setIntersection(createRectangle(0, 0, 7, 10));
			assertEquals(5, shape.getMinX());
			assertEquals(8, shape.getMinY());
			assertEquals(7, shape.getMaxX());
			assertEquals(10, shape.getMaxY());
		}

	}

	@DisplayName("createIntersection")
	@Nested
	public class CreateIntersection {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			T box = shape.createIntersection(createRectangle(0, 0, 12, 1));
			assertNotSame(shape, box);
			assertTrue(box.isEmpty());
			assertEquals(5, shape.getMinX());
			assertEquals(8, shape.getMinY());
			assertEquals(15, shape.getMaxX());
			assertEquals(13, shape.getMaxY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			T box = shape.createIntersection(createRectangle(0, 0, 7, 10));
			assertNotSame(shape, box);
			assertEquals(5, box.getMinX());
			assertEquals(8, box.getMinY());
			assertEquals(7, box.getMaxX());
			assertEquals(10, box.getMaxY());
			assertEquals(5, shape.getMinX());
			assertEquals(8, shape.getMinY());
			assertEquals(15, shape.getMaxX());
			assertEquals(13, shape.getMaxY());
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
			assertEquals(8, shape.getMinX());
			assertEquals(12, shape.getMinY());
			assertEquals(18, shape.getMaxX());
			assertEquals(17, shape.getMaxY());
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
			assertEquals(8, r.getMinX());
			assertEquals(12, r.getMinY());
			assertEquals(18, r.getMaxX());
			assertEquals(17, r.getMaxY());
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
			assertEquals(2, shape.getMinX());
			assertEquals(4, shape.getMinY());
			assertEquals(12, shape.getMaxX());
			assertEquals(9, shape.getMaxY());
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
			assertEquals(2, r.getMinX());
			assertEquals(4, r.getMinY());
			assertEquals(12, r.getMaxX());
			assertEquals(9, r.getMaxY());
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
			var pi = shape.operator_multiply(tr).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 5,8);
			assertElement(pi, PathElementType.LINE_TO, 15,8);
			assertElement(pi, PathElementType.LINE_TO, 15,13);
			assertElement(pi, PathElementType.LINE_TO, 5,13);
			assertElement(pi, PathElementType.CLOSE, 5,8);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeTranslationMatrix(3.4f, 4.5f);
			var pi = shape.operator_multiply(tr).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 8,13);
			assertElement(pi, PathElementType.LINE_TO, 18,13);
			assertElement(pi, PathElementType.LINE_TO, 18,18);
			assertElement(pi, PathElementType.LINE_TO, 8,18);
			assertElement(pi, PathElementType.CLOSE, 8,13);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeRotationMatrix(MathConstants.QUARTER_PI);		
			var pi = shape.operator_multiply(tr).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -2,9);
			assertElement(pi, PathElementType.LINE_TO, 5,16);
			assertElement(pi, PathElementType.LINE_TO, 1,20);
			assertElement(pi, PathElementType.LINE_TO, -6,13);
			assertElement(pi, PathElementType.CLOSE, -2,9);
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
			assertFalse(shape.operator_and(createPoint(0,0)));
			assertTrue(shape.operator_and(createPoint(11,10)));
			assertFalse(shape.operator_and(createPoint(11,50)));
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
			assertTrue(shape.operator_and(createCircle(0,0,100)));
			assertTrue(shape.operator_and(createRectangle(7,10,1,1)));
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
			assertEpsilonEquals(0f, shape.operator_upTo(createPoint(5,8)));
			assertEpsilonEquals(0f, shape.operator_upTo(createPoint(10,10)));
			assertEpsilonEquals(1f, shape.operator_upTo(createPoint(4,8)));
			assertEpsilonEquals(9.433981132f, shape.operator_upTo(createPoint(0,0)));
		}
	}

}