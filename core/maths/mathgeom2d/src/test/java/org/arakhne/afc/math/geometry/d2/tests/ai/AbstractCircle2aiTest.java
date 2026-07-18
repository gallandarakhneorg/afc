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
import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.Circle2ai;
import org.arakhne.afc.math.geometry.d2.ai.GeomFactory2ai;
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
public abstract class AbstractCircle2aiTest<T extends Circle2ai<?, T, ?, ?, ?, B>,
B extends Rectangle2ai<?, ?, ?, ?, ?, B>> extends AbstractShape2aiTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createCircle(5, 8, 5);
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
			assertEquals(0, getS().getX());
			assertEquals(0, getS().getY());
			assertEquals(0, getS().getRadius());
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
			getS().translate(3,  4);
			assertEquals(8, getS().getX());
			assertEquals(12, getS().getY());
			assertEquals(5, getS().getRadius());
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
			Rectangle2ai<?, ?, ?, ?, ?, ?> r1 = getS().toBoundingBox();
			assertEquals(0, r1.getMinX());
			assertEquals(3, r1.getMinY());
			assertEquals(10, r1.getMaxX());
			assertEquals(13, r1.getMaxY());
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
			Iterator<? extends Point2D> iterator = getS().getPointIterator();
			Point2D p;

			int[] coords = new int[]{
					// octant 1
					5,13,
					6,13,
					7,13,
					8,12,
					// octant 2
					10,8,
					10,9,
					10,10,
					9,11,
					// octant 3
					5,3,
					6,3,
					7,3,
					8,4,
					// octant 4 (the first point is skipped because already returns in octant 2)
					10,7,
					10,6,
					9,5,
					// octant 5 (the first point is skipped because already returns in octant 4)
					4,3,
					3,3,
					2,4,
					// octant 6 
					0,8,
					0,7,
					0,6,
					1,5,
					// octant 7 (the first point is skipped because already returns in octant 1)
					4,13,
					3,13,
					2,12,
					// octant 8 (the first point is skipped because already returns in octant 5)
					0,9,
					0,10,
					1,11,
			};

			for(int i=0; i<coords.length; i+=2) {
				int x = coords[i];
				int y = coords[i+1];
				assertTrue(iterator.hasNext(), "("+x+";"+y+")");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				p = iterator.next();
				assertNotNull(p);
				assertEquals(x, p.ix(), "(>"+x+"<;"+y+")!=("+p.ix()+";"+p.iy()+")");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				assertEquals(y, p.iy(), "("+x+";>"+y+"<)!=("+p.ix()+";"+p.iy()+")");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}

			assertFalse(iterator.hasNext());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Circle2ai<?, ?, ?, ?, ?, ?> circle = createCircle(4, 6, 3);
			Iterator<? extends Point2D> iterator = circle.getPointIterator();
			Point2D p;

			int[] coords = new int[]{
					// octant 1
					4,9,
					5,9,
					6,8,
					// octant 2
					7,6,
					7,7,
					// 6,8, skipped because already returned
					// octant 3
					4,3,
					5,3,
					6,4,
					// octant 4 (the first point is skipped because already returns in octant 2)
					7,5,
					// 6,4, skipped because already returned
					// octant 5 (the first point is skipped because already returns in octant 4)
					3,3,
					2,4,
					// octant 6 
					1,6,
					1,5,
					// 2,4, skipped because already returned
					// octant 7 (the first point is skipped because already returns in octant 1)
					3,9,
					2,8,
					// octant 8 (the first point is skipped because already returns in octant 5)
					1,7,
					// 2,8, skipped because already returned
			};

			for(int i=0; i<coords.length; i+=2) {
				int x = coords[i];
				int y = coords[i+1];
				assertTrue(iterator.hasNext(), "("+x+";"+y+")");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				p = iterator.next();
				assertNotNull(p);
				assertEquals(x, p.ix(), "(>"+x+"<;"+y+")!=("+p.ix()+";"+p.iy()+")");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				assertEquals(y, p.iy(), "("+x+";>"+y+"<)!=("+p.ix()+";"+p.iy()+")");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}

			assertFalse(iterator.hasNext());
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
			assertEquals(5, p.ix());
			assertEquals(8, p.iy());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(10,10));
			assertNotNull(p);
			assertEquals(10, p.ix());
			assertEquals(10, p.iy());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(4,8));
			assertNotNull(p);
			assertEquals(4, p.ix());
			assertEquals(8, p.iy());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(0,0));
			assertNotNull(p);
			assertEquals(3, p.ix());
			assertEquals(3, p.iy());
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(5,14));
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(13, p.iy());
		}

		@DisplayName("(Circle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(0, 10, getS().getClosestPointTo(createCircle(-10, 12, 2)));
		}

		@DisplayName("(Circle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(8, 12, getS().getClosestPointTo(createCircle(30, 14, 2)));
		}

		@DisplayName("(Circle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(9, 3, 2));
		}

		@DisplayName("(Circle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(4, 7, 2));
		}

		@DisplayName("(Segment2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(7, 3, getS().getClosestPointTo(createSegment(-1, -1, 15, 2)));
		}

		@DisplayName("(Segment2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(0, 6, getS().getClosestPointTo(createSegment(-5, 5, -10, 18)));
		}

		@DisplayName("(Segment2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(-5, 5, 15, 13));
		}

		@DisplayName("(Segment2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(6, 4, 15, 13));
		}

		@DisplayName("(Segment2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(6, 4, 4, 9));
		}

		@DisplayName("(Rectangle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(3, 3, getS().getClosestPointTo(createRectangle(-1, -1, 3, 2)));
		}

		@DisplayName("(Rectangle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(-1, 4, 3, 2));
		}

		@DisplayName("(Rectangle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(7, 13, getS().getClosestPointTo(createRectangle(15, 15, 3, 2)));
		}

		@DisplayName("(Rectangle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(2, 5, 3, 2));
		}

		@DisplayName("(MultiShape2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestMultiShape(0, 0));
		}

		@DisplayName("(MultiShape2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(3, 3, getS().getClosestPointTo(createTestMultiShape(-15, -20)));
		}

		@DisplayName("(MultiShape2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(3, 13, getS().getClosestPointTo(createTestMultiShape(0, 32)));
		}

		@DisplayName("(Path2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(0, 0));
		}

		@DisplayName("(Path2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(5, 3, getS().getClosestPointTo(createTestPath(-15, -20)));
		}

		@DisplayName("(Path2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(5, 13, getS().getClosestPointTo(createTestPath(0, 32)));
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
			var p = getS().getFarthestPointTo(createPoint(5,8));
			assertNotNull(p);
			assertEquals(3, p.ix());
			assertEquals(3, p.iy());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(10,10));
			assertNotNull(p);
			assertEquals(0, p.ix());
			assertEquals(6, p.iy());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(4,8));
			assertNotNull(p);
			assertEquals(10, p.ix());
			assertEquals(6, p.iy());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(0,0));
			assertNotNull(p);
			assertEquals(7, p.ix());
			assertEquals(13, p.iy());
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(5,14));
			assertNotNull(p);
			assertEquals(3, p.ix());
			assertEquals(3, p.iy());
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
			assertFalse(getS().contains(0,0));
		}

		@DisplayName("(double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(11,10));
		}

		@DisplayName("(double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(11,50));
		}

		@DisplayName("(double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(9,12));
		}

		@DisplayName("(double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(9,11));
		}

		@DisplayName("(double,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(8,12));
		}

		@DisplayName("(double,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(3,7));
		}

		@DisplayName("(double,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(10,11));
		}

		@DisplayName("(double,double) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(9,10));
		}

		@DisplayName("(double,double) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var s = (T) createCircle(-1,-1,1);
			assertFalse(s.contains(0,0));
		}

		@DisplayName("(Rectangle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(0,0,1,1)));
		}

		@DisplayName("(Rectangle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(0,0,8,1)));
		}

		@DisplayName("(Rectangle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(0,0,8,6)));
		}

		@DisplayName("(Rectangle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(0,0,100,100)));
		}

		@DisplayName("(Rectangle2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createRectangle(7,10,1,1)));
		}

		@DisplayName("(Rectangle2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(16,0,100,100)));
		}

		@DisplayName("(Rectangle2ai) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(9,11,5,5)));
		}

		@DisplayName("(Shape2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0,0,1)));
		}

		@DisplayName("(Shape2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0,0,8)));
		}

		@DisplayName("(Shape2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0,0,8)));
		}

		@DisplayName("(Shape2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0,0,100)));
		}

		@DisplayName("(Shape2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createCircle(7,10,1)));
		}

		@DisplayName("(Shape2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(16,0,100)));
		}

		@DisplayName("(Shape2ai) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(9,11,5)));
		}

		@DisplayName("(Point2D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(0,0)));
		}

		@DisplayName("(Point2D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(11,10)));
		}

		@DisplayName("(Point2D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(11,50)));
		}

		@DisplayName("(Point2D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(9,12)));
		}

		@DisplayName("(Point2D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(9,11)));
		}

		@DisplayName("(Point2D) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(8,12)));
		}

		@DisplayName("(Point2D) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(3,7)));
		}

		@DisplayName("(Point2D) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(10,11)));
		}

		@DisplayName("(Point2D) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(9,10)));
		}

		@DisplayName("(Point2D) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var s = (T) createCircle(-1,-1,1);
			assertFalse(s.contains(createPoint(0,0)));
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
			assertFalse(getS().intersects(createRectangle(0,0,1,1)));
		}

		@DisplayName("(Rectangle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(0,0,8,1)));
		}

		@DisplayName("(Rectangle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(0,0,8,6)));
		}

		@DisplayName("(Rectangle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(0,0,100,100)));
		}

		@DisplayName("(Rectangle2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(7,10,1,1)));
		}

		@DisplayName("(Rectangle2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(16,0,100,100)));
		}

		@DisplayName("(Segment2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(0,0,1,1)));
		}

		@DisplayName("(Segment2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(0,0,8,1)));
		}

		@DisplayName("(Segment2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(0,0,8,6)));
		}

		@DisplayName("(Segment2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(0,0,100,100)));
		}

		@DisplayName("(Segment2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(7,10,1,1)));
		}

		@DisplayName("(Segment2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(16,0,100,100)));
		}

		@DisplayName("(Segment2ai) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(8,13,10,11)));
		}

		@DisplayName("(Path2ai) #1")
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
			var s = (T) createCircle(0, 0, 1);
			assertTrue(s.intersects(path));
		}

		@DisplayName("(Path2ai) #2")
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
			var s = (T) createCircle(4, 3, 1);
			assertTrue(s.intersects(path));
		}

		@DisplayName("(Path2ai) #3")
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
			var s = (T) createCircle(2, 2, 1);
			assertTrue(s.intersects(path));
		}

		@DisplayName("(Path2ai) #4")
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
			var s = (T) createCircle(2, 1, 1);
			assertTrue(s.intersects(path));
		}

		@DisplayName("(Path2ai) #5")
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
			var s = (T) createCircle(3, 0, 1);
			assertTrue(s.intersects(path));
		}

		@DisplayName("(Path2ai) #6")
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
			var s = (T) createCircle(-1, -1, 1);
			assertFalse(s.intersects(path));
		}

		@DisplayName("(Path2ai) #7")
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
			var s = (T) createCircle(4, -3, 1);
			assertTrue(s.intersects(path));
		}

		@DisplayName("(Path2ai) #8")
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
			var s = (T) createCircle(-3, 4, 1);
			assertFalse(s.intersects(path));
		}

		@DisplayName("(Path2ai) #9")
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
			var s = (T) createCircle(6, -5, 1);
			assertTrue(s.intersects(path));
		}

		@DisplayName("(Path2ai) #10")
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
			var s = (T) createCircle(4, 0, 1);
			assertTrue(s.intersects(path));
		}

		@DisplayName("(Path2ai) #11")
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
			var s = (T) createCircle(5, 0, 1);
			assertTrue(s.intersects(path));
		}

		@DisplayName("(Path2ai) #12")
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
			var s = (T) createCircle(6, 2, 1);
			assertTrue(s.intersects(path));
		}

		@DisplayName("(Path2ai) #13")
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
			var s = (T) createCircle(-5, 0, 3);
			assertFalse(s.intersects(path));
		}

		@DisplayName("(Circle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(0,0,1)));
		}

		@DisplayName("(Circle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(0,0,8)));
		}

		@DisplayName("(Circle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(0,0,100)));
		}

		@DisplayName("(Circle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(7,10,1)));
		}

		@DisplayName("(Circle2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(16,0,5)));
		}

		@DisplayName("(Circle2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(5,15,1)));
		}

		@DisplayName("(PathIterator2ai) #1")
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
			var s = (T) createCircle(0, 0, 1);
			assertTrue(s.intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #2")
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
			var s = (T) createCircle(4, 3, 1);
			assertTrue(s.intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #3")
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
			var s = (T) createCircle(2, 2, 1);
			assertTrue(s.intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #4")
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
			var s = (T) createCircle(2, 1, 1);
			assertTrue(s.intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #5")
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
			var s = (T) createCircle(3, 0, 1);
			assertTrue(s.intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #6")
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
			var s = (T) createCircle(-1, -1, 1);
			assertFalse(s.intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #7")
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
			var s = (T) createCircle(4, -3, 1);
			assertTrue(s.intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #8")
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
			var s = (T) createCircle(-3, 4, 1);
			assertFalse(s.intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #9")
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
			var s = (T) createCircle(6, -5, 1);
			assertTrue(s.intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #10")
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
			var s = (T) createCircle(4, 0, 1);
			assertTrue(s.intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #11")
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
			var s = (T) createCircle(5, 0, 1);
			assertTrue(s.intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #12")
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
			var s = (T) createCircle(6, 2, 1);
			assertTrue(s.intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #13")
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
			var s = (T) createCircle(-5, 0, 3);
			assertFalse(s.intersects((PathIterator2ai) path.getPathIterator()));
		}

		@DisplayName("(Shape2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects((Shape2D) createCircle(0,0,100)));
		}

		@DisplayName("(Shape2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects((Shape2D) createRectangle(0,0,100,100)));
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
			PathIterator2ai<?> pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 10,8);
			assertElement(pi, PathElementType.CURVE_TO, 10,10, 7,13, 5,13);
			assertElement(pi, PathElementType.CURVE_TO, 2,13, 0,10, 0,8);
			assertElement(pi, PathElementType.CURVE_TO, 0,5, 2,3, 5,3);
			assertElement(pi, PathElementType.CURVE_TO, 7,3, 10,5, 10,8);
			assertElement(pi, PathElementType.CLOSE, 10, 8);
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
			assertElement(pi, PathElementType.MOVE_TO, 10,8);
			assertElement(pi, PathElementType.CURVE_TO, 10,10, 7,13, 5,13);
			assertElement(pi, PathElementType.CURVE_TO, 2,13, 0,10, 0,8);
			assertElement(pi, PathElementType.CURVE_TO, 0,5, 2,3, 5,3);
			assertElement(pi, PathElementType.CURVE_TO, 7,3, 10,5, 10,8);
			assertElement(pi, PathElementType.CLOSE, 10,8);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeTranslationMatrix(3.4f, 4.5f);
			var pi = getS().getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 13,13);
			assertElement(pi, PathElementType.CURVE_TO, 13,16, 11,18, 8,18);
			assertElement(pi, PathElementType.CURVE_TO, 5,18, 3,16, 3,13);
			assertElement(pi, PathElementType.CURVE_TO, 3,10, 5,8, 8,8);
			assertElement(pi, PathElementType.CURVE_TO, 11,8, 13,10, 13,13);
			assertElement(pi, PathElementType.CLOSE, 13,13);
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
			assertElement(pi, PathElementType.MOVE_TO, 1,13);
			assertElement(pi, PathElementType.CURVE_TO, -1,15, -4,15, -6,13);
			assertElement(pi, PathElementType.CURVE_TO, -8,11, -8,8, -6,6);
			assertElement(pi, PathElementType.CURVE_TO, -4,4, -1,4, 1,6);
			assertElement(pi, PathElementType.CURVE_TO, 4,8, 4,11, 1,13);
			assertElement(pi, PathElementType.CLOSE, 1,13);
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
			var pi = getS().createTransformedShape(tr).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 10,8);
			assertElement(pi, PathElementType.CURVE_TO, 10,10, 7,13, 5,13);
			assertElement(pi, PathElementType.CURVE_TO, 2,13, 0,10, 0,8);
			assertElement(pi, PathElementType.CURVE_TO, 0,5, 2,3, 5,3);
			assertElement(pi, PathElementType.CURVE_TO, 7,3, 10,5, 10,8);
			assertElement(pi, PathElementType.CLOSE, 10,8);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeTranslationMatrix(3.4f, 4.5f);
			var pi = getS().createTransformedShape(tr).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 13,13);
			assertElement(pi, PathElementType.CURVE_TO, 13,16, 11,18, 8,18);
			assertElement(pi, PathElementType.CURVE_TO, 5,18, 3,16, 3,13);
			assertElement(pi, PathElementType.CURVE_TO, 3,10, 5,8, 8,8);
			assertElement(pi, PathElementType.CURVE_TO, 11,8, 13,10, 13,13);
			assertElement(pi, PathElementType.CLOSE, 13,13);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeRotationMatrix(MathConstants.QUARTER_PI);
			var pi = getS().createTransformedShape(tr).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 1,13);
			assertElement(pi, PathElementType.CURVE_TO, -1,15, -4,15, -6,13);
			assertElement(pi, PathElementType.CURVE_TO, -8,11, -8,8, -6,6);
			assertElement(pi, PathElementType.CURVE_TO, -4,4, -1,4, 1,6);
			assertElement(pi, PathElementType.CURVE_TO, 4,8, 4,11, 1,13);
			assertElement(pi, PathElementType.CLOSE, 1,13);
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
			getS().set((T) createCircle(17, 20, 7));
			assertEquals(17, getS().getX());
			assertEquals(20, getS().getY());
			assertEquals(7, getS().getRadius());
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
			assertEpsilonEquals(0f, getS().getDistance(createPoint(5,8)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().getDistance(createPoint(10,10)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().getDistance(createPoint(4,8)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.242640687f, getS().getDistance(createPoint(0,0)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, getS().getDistance(createPoint(5,14)));
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
			assertEpsilonEquals(0f, getS().getDistanceSquared(createPoint(5,8)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().getDistanceSquared(createPoint(10,10)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().getDistanceSquared(createPoint(4,8)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(18f, getS().getDistanceSquared(createPoint(0,0)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, getS().getDistanceSquared(createPoint(5,14)));
		}

		@DisplayName("(Circle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(81, getS().getDistanceSquared(createCircle(-10, 12, 2)));
		}

		@DisplayName("(Circle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(441, getS().getDistanceSquared(createCircle(30, 14, 2)));
		}

		@DisplayName("(Circle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(9, 3, 2)));
		}

		@DisplayName("(Circle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(4, 7, 2)));
		}

		@DisplayName("(Segment2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getDistanceSquared(createSegment(-1, -1, 15, 2)));
		}

		@DisplayName("(Segment2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25, getS().getDistanceSquared(createSegment(-5, 5, -10, 18)));
		}

		@DisplayName("(Segment2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(-5, 5, 15, 13)));
		}

		@DisplayName("(Segment2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(6, 4, 15, 13)));
		}

		@DisplayName("(Segment2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(6, 4, 4, 9)));
		}

		@DisplayName("(Rectangle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getDistanceSquared(createRectangle(-1, -1, 3, 2)));
		}

		@DisplayName("(Rectangle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(-1, 4, 3, 2)));
		}

		@DisplayName("(Rectangle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(68, getS().getDistanceSquared(createRectangle(15, 15, 3, 2)));
		}

		@DisplayName("(Rectangle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(2, 5, 3, 2)));
		}

		@DisplayName("(MultiShape2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(0, 0)));
		}

		@DisplayName("(MultiShape2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(569, getS().getDistanceSquared(createTestMultiShape(-15, -20)));
		}

		@DisplayName("(MultiShape2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(289, getS().getDistanceSquared(createTestMultiShape(0, 32)));
		}

		@DisplayName("(Path2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(0, 0)));
		}

		@DisplayName("(Path2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(324, getS().getDistanceSquared(createTestPath(-15, -20)));
		}

		@DisplayName("(Path2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(196, getS().getDistanceSquared(createTestPath(0, 32)));
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
			assertEpsilonEquals(0f, getS().getDistanceL1(createPoint(5,8)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().getDistanceL1(createPoint(10,10)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().getDistanceL1(createPoint(4,8)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6f, getS().getDistanceL1(createPoint(0,0)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, getS().getDistanceL1(createPoint(5,14)));
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
			assertEpsilonEquals(0f, getS().getDistanceLinf(createPoint(5,8)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().getDistanceLinf(createPoint(10,10)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().getDistanceLinf(createPoint(4,8)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3f, getS().getDistanceLinf(createPoint(0,0)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, getS().getDistanceLinf(createPoint(5,14)));
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
			B r = createRectangle(0, 0, 0, 0);
			getS().toBoundingBox(r);
			assertEquals(0, r.getMinX());
			assertEquals(3, r.getMinY());
			assertEquals(10, r.getMaxX());
			assertEquals(13, r.getMaxY());
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
			getS().translate(createVector(3, 4));
			assertEquals(8, getS().getX());
			assertEquals(12, getS().getY());
			assertEquals(5, getS().getRadius());
		}
	}

	@DisplayName("findsClosestPointCirclePoint")
	@Nested
	public class FindsClosestPointCirclePoint {

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
			Circle2ai.findsClosestPointCirclePoint(5, 8, 5, 5, 8, p);
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(8, p.iy());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Circle2ai.findsClosestPointCirclePoint(5, 8, 5, 10, 10, p);
			assertNotNull(p);
			assertEquals(10, p.ix());
			assertEquals(10, p.iy());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Circle2ai.findsClosestPointCirclePoint(5, 8, 5, 4, 8, p);
			assertNotNull(p);
			assertEquals(4, p.ix());
			assertEquals(8, p.iy());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Circle2ai.findsClosestPointCirclePoint(5, 8, 5, 0, 0, p);
			assertNotNull(p);
			assertEquals(3, p.ix());
			assertEquals(3, p.iy());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Circle2ai.findsClosestPointCirclePoint(5, 8, 5, 5, 14, p);
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(13, p.iy());
		}
	}

	@DisplayName("findsFarthestPointCirclePoint")
	@Nested
	public class FindsFarthestPointCirclePoint {

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
			Circle2ai.findsFarthestPointCirclePoint(5, 8, 5, 5, 8, p);
			assertNotNull(p);
			assertEquals(3, p.ix());
			assertEquals(3, p.iy());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			p = createPoint(0, 0);
			Circle2ai.findsFarthestPointCirclePoint(5, 8, 5, 10, 10, p);
			assertNotNull(p);
			assertEquals(0, p.ix());
			assertEquals(6, p.iy());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			p = createPoint(0, 0);
			Circle2ai.findsFarthestPointCirclePoint(5, 8, 5, 4, 8, p);
			assertNotNull(p);
			assertEquals(10, p.ix());
			assertEquals(6, p.iy());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			p = createPoint(0, 0);
			Circle2ai.findsFarthestPointCirclePoint(5, 8, 5, 0, 0, p);
			assertNotNull(p);
			assertEquals(7, p.ix());
			assertEquals(13, p.iy());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			p = createPoint(0, 0);
			Circle2ai.findsFarthestPointCirclePoint(5, 8, 5, 5, 14, p);
			assertNotNull(p);
			assertEquals(3, p.ix());
			assertEquals(3, p.iy());
		}
	}

	@DisplayName("containsCirclePoint")
	@Nested
	public class ContainsCirclePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCirclePoint(5, 8, 5, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCirclePoint(5, 8, 5, 11, 10));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCirclePoint(5, 8, 5, 11, 50));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCirclePoint(5, 8, 5, 9, 12));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.containsCirclePoint(5, 8, 5, 9, 11));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.containsCirclePoint(5, 8, 5, 8, 12));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.containsCirclePoint(5, 8, 5, 3, 7));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCirclePoint(5, 8, 5, 10, 11));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.containsCirclePoint(5, 8, 5, 9, 10));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCirclePoint(-1, -1, 1, 0, 0));
		}
	}

	@DisplayName("containsCircleQuadrantPoint")
	@Nested
	public class ContainsCircleQuadrantPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 0, 9, 11));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 1, 9, 11));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 2, 9, 11));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 3, 9, 11));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 0, 8, 12));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 1, 8, 12));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 2, 8, 12));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 3, 8, 12));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 0, 3, 7));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 1, 3, 7));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 2, 3, 7));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 3, 3, 7));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 0, 9, 10));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 1, 9, 10));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 2, 9, 10));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.containsCircleQuadrantPoint(5, 8, 5, 3, 9, 10));
		}
	}

	@DisplayName("newPointIterator")
	@Nested
	public class NewPointIterator {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Iterator iterator = Circle2ai.newPointIterator(5, 8, 5, 0, 8, (GeomFactory2ai) getS().getGeomFactory());
			Point2D p;

			int[] coords = new int[] {
					5, 13,
					6, 13,
					7, 13,
					8, 12,
					10, 8,
					10, 9,
					10, 10,
					9, 11,
					5, 3,
					6, 3,
					7, 3,
					8, 4,
					10, 7,
					10, 6,
					9, 5,
					4, 3,
					3, 3,
					2, 4,
					0, 8,
					0, 7,
					0, 6,
					1, 5,
					4, 13,
					3, 13,
					2, 12,
					0, 9,
					0, 10,
					1, 11,
			};

			for(int i = 0; i < coords.length; i += 2) {
				int x = coords[i];
				int y = coords[i + 1];
				assertTrue(iterator.hasNext());
				p = (Point2D) iterator.next();
				assertNotNull(p);
				assertEquals(x, p.ix());
				assertEquals(y, p.iy());
			}

			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("intersectsCircleCircle")
	@Nested
	public class IntersectsCircleCircle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.intersectsCircleCircle(5, 8, 5, 0, 0, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.intersectsCircleCircle(5, 8, 5, 0, 0, 8));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.intersectsCircleCircle(5, 8, 5, 0, 0, 100));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.intersectsCircleCircle(5, 8, 5, 7, 10, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.intersectsCircleCircle(5, 8, 5, 16, 0, 5));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.intersectsCircleCircle(5, 8, 5, 5, 15, 1));
		}
	}

	@DisplayName("intersectsCircleRectangle")
	@Nested
	public class IntersectsCircleRectangle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.intersectsCircleRectangle(0, 0, 1, 5, 8, 15, 13));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.intersectsCircleRectangle(0, 0, 8, 5, 8, 15, 13));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.intersectsCircleRectangle(0, 0, 100, 5, 8, 15, 13));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.intersectsCircleRectangle(7, 10, 1, 5, 8, 15, 13));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.intersectsCircleRectangle(16, 0, 5, 5, 8, 15, 13));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.intersectsCircleRectangle(5, 15, 1, 5, 8, 15, 13));
		}
	}

	@DisplayName("intersectsCircleSegment")
	@Nested
	public class IntersectsCircleSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.intersectsCircleSegment(5, 8, 5, 0, 0, 1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.intersectsCircleSegment(5, 8, 5, 0, 0, 8, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.intersectsCircleSegment(5, 8, 5, 0, 0, 8, 6));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.intersectsCircleSegment(5, 8, 5, 0, 0, 100, 100));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Circle2ai.intersectsCircleSegment(5, 8, 5, 7, 10, 1, 1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.intersectsCircleSegment(5, 8, 5, 16, 0, 100, 100));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Circle2ai.intersectsCircleSegment(5, 8, 5, 8, 13, 10, 11));
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
			getS().operator_add(createVector(3, 4));
			assertEquals(8, getS().getX());
			assertEquals(12, getS().getY());
			assertEquals(5, getS().getRadius());
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
			T r = getS().operator_plus(createVector(3, 4));
			assertEquals(8, r.getX());
			assertEquals(12, r.getY());
			assertEquals(5, r.getRadius());
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
			getS().operator_remove(createVector(3, 4));
			assertEquals(2, getS().getX());
			assertEquals(4, getS().getY());
			assertEquals(5, getS().getRadius());
		}
	}

	@DisplayName("this -= Vector2D")
	@Nested
	public class OperatorMinusVector2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			T r = getS().operator_minus(createVector(3, 4));
			assertEquals(2, r.getX());
			assertEquals(4, r.getY());
			assertEquals(5, r.getRadius());
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
			var pi = getS().operator_multiply(tr).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 10,8);
			assertElement(pi, PathElementType.CURVE_TO, 10,10, 7,13, 5,13);
			assertElement(pi, PathElementType.CURVE_TO, 2,13, 0,10, 0,8);
			assertElement(pi, PathElementType.CURVE_TO, 0,5, 2,3, 5,3);
			assertElement(pi, PathElementType.CURVE_TO, 7,3, 10,5, 10,8);
			assertElement(pi, PathElementType.CLOSE, 10,8);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeTranslationMatrix(3.4f, 4.5f);
			var pi = getS().operator_multiply(tr).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 13,13);
			assertElement(pi, PathElementType.CURVE_TO, 13,16, 11,18, 8,18);
			assertElement(pi, PathElementType.CURVE_TO, 5,18, 3,16, 3,13);
			assertElement(pi, PathElementType.CURVE_TO, 3,10, 5,8, 8,8);
			assertElement(pi, PathElementType.CURVE_TO, 11,8, 13,10, 13,13);
			assertElement(pi, PathElementType.CLOSE, 13,13);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeRotationMatrix(MathConstants.QUARTER_PI);
			var pi = getS().operator_multiply(tr).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 1,13);
			assertElement(pi, PathElementType.CURVE_TO, -1,15, -4,15, -6,13);
			assertElement(pi, PathElementType.CURVE_TO, -8,11, -8,8, -6,6);
			assertElement(pi, PathElementType.CURVE_TO, -4,4, -1,4, 1,6);
			assertElement(pi, PathElementType.CURVE_TO, 4,8, 4,11, 1,13);
			assertElement(pi, PathElementType.CLOSE, 1,13);
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
			assertTrue(getS().operator_and(createCircle(0,0,100)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createRectangle(0,0,100,100)));
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
			assertEpsilonEquals(0f, getS().operator_upTo(createPoint(5,8)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().operator_upTo(createPoint(10,10)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().operator_upTo(createPoint(4,8)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.242640687f, getS().operator_upTo(createPoint(0,0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, getS().operator_upTo(createPoint(5,14)));
		}
	}

}