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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.base.CrossingComputationType;
import org.arakhne.afc.math.geometry.base.GeomConstants;
import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Path2D.ArcType;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.BasicPathShadow2ai;
import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.arakhne.afc.math.geometry.d2.ai.Shape2ai;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractPath2aiTestCase<T extends Path2ai<?, T, ?, ?, ?, B>,
B extends Rectangle2ai<?, ?, ?, ?, ?, B>> extends AbstractShape2aiTestCase<T, B> {

	@Override
	protected final T createShape() {
		T path = (T) createPath();
		path.moveTo(0, 0);
		path.lineTo(2, 2);
		path.quadTo(3, 0, 4, 3);
		path.curveTo(5, -1, 6, 5, 7, -5);
		path.closePath();
		return path;
	}

	protected Path2ai createTestPath() {
		Path2ai path = createPath();
		path.moveTo(3, -20);
		path.lineTo(10, -5);
		path.lineTo(5, 25);
		path.lineTo(-4, 0);
		return path;
	}

	protected Path2ai createTestPath(PathWindingRule rule) {
		Path2ai path = createPath(rule);
		path.moveTo(3, -20);
		path.lineTo(10, -5);
		path.lineTo(5, 25);
		path.lineTo(-4, 0);
		path.closePath();
		return path;
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
			PathIterator2ai pi = clone.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
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
			assertFalse(getS().equals(createPath()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createRectangle(5, 8, 10, 6)));
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
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(getS().equals(path));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createPath().getPathIterator()));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createRectangle(5, 8, 10, 6).getPathIterator()));
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
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(getS().equals(path.getPathIterator()));
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
			assertFalse(getS().equalsToShape((T) createPath()));
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
			T path = (T) createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(getS().equalsToShape(path));
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
			assertFalse(getS().equalsToPathIterator(createPath().getPathIterator()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToPathIterator(createRectangle(5, 8, 10, 6).getPathIterator()));
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
			Path2ai path = createPath();
			path.moveTo(0, 0);
			path.lineTo(2, 2);
			path.quadTo(3, 0, 4, 3);
			path.curveTo(5, -1, 6, 5, 7, -5);
			path.closePath();
			assertTrue(getS().equalsToPathIterator(path.getPathIterator()));
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
			getS().moveTo(1, 2);
			assertTrue(getS().isEmpty());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			assertTrue(getS().isEmpty());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			assertFalse(getS().isEmpty());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			getS().closePath();
			assertFalse(getS().isEmpty());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			assertTrue(getS().isEmpty());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			assertTrue(getS().isEmpty());
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			assertTrue(getS().isEmpty());
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().closePath();
			assertTrue(getS().isEmpty());
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			assertTrue(getS().isEmpty());
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			assertTrue(getS().isEmpty());
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			assertTrue(getS().isEmpty());
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			assertFalse(getS().isEmpty());
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
			assertEquals(0, getS().size());
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
			assertEquals(0, p.ix(), p.toString());
			assertEquals(0, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(1, p.ix(), p.toString());
			assertEquals(1, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(2, p.ix(), p.toString());
			assertEquals(2, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(3, p.ix(), p.toString());
			assertEquals(1, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(4, p.ix(), p.toString());
			assertEquals(2, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(4, p.ix(), p.toString());
			assertEquals(3, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(4, p.ix(), p.toString());
			assertEquals(2, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(5, p.ix(), p.toString());
			assertEquals(2, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(5, p.ix(), p.toString());
			assertEquals(1, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(6, p.ix(), p.toString());
			assertEquals(1, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(6, p.ix(), p.toString());
			assertEquals(0, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(6, p.ix(), p.toString());
			assertEquals(-1, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(7, p.ix(), p.toString());
			assertEquals(-1, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(7, p.ix(), p.toString());
			assertEquals(-2, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(7, p.ix(), p.toString());
			assertEquals(-3, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(7, p.ix(), p.toString());
			assertEquals(-4, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(7, p.ix(), p.toString());
			assertEquals(-5, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(6, p.ix(), p.toString());
			assertEquals(-4, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(5, p.ix(), p.toString());
			assertEquals(-4, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(4, p.ix(), p.toString());
			assertEquals(-3, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(3, p.ix(), p.toString());
			assertEquals(-2, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(2, p.ix(), p.toString());
			assertEquals(-1, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(1, p.ix(), p.toString());
			assertEquals(-1, p.iy(), p.toString());

			assertTrue(it.hasNext());
			p = it.next();
			assertNotNull(p);
			assertEquals(0, p.ix(), p.toString());
			assertEquals(0, p.iy(), p.toString());

			assertFalse(it.hasNext());
		}
	}

	@DisplayName("calculatesCrossingsPathIteratorPointShadow")
	@Nested
	public class CalculatesCrossingsPathIteratorPointShadow {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2ai.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), -2, 1, null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2ai.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 0, -3, null));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS,
					Path2ai.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 4, 3, null));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2ai.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 3, 0, null));
		}
	}

	@DisplayName("calculatesCrossingsPathIteratorRectangleShadow")
	@Nested
	public class CalculatesCrossingsPathIteratorRectangleShadow {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
					0,
					getS().getPathIterator(),
					-2, 1, -1, 2,
					null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
					0,
					getS().getPathIterator(),
					0, 1, 3, 6,
					null));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
					0,
					getS().getPathIterator(),
					3, -1, 8, 0,
					null));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
					0,
					getS().getPathIterator(),
					3, -1, 4, 0,
					null));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
					0,
					getS().getPathIterator(),
					3, -1, 5, 0,
					null));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
					0,
					getS().getPathIterator(),
					0, -4, 3, -3,
					null));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
					0,
					getS().getPathIterator(),
					0, -4, 4, -3,
					null));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
					0,
					getS().getPathIterator(),
					0, -4, 3, -2,
					null));
		}
	}

	@DisplayName("calculatesCrossingsPathIteratorSegmentShadow")
	@Nested
	public class CalculatesCrossingsPathIteratorSegmentShadow {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
					0,
					getS().getPathIterator(),
					-2, 1, -1, 2,
					null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
					0,
					getS().getPathIterator(),
					0, 1, 3, 6,
					null));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
					0,
					getS().getPathIterator(),
					3, -1, 8, 0,
					null));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
					0,
					getS().getPathIterator(),
					3, -1, 4, 0,
					null));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
					0,
					getS().getPathIterator(),
					3, -1, 5, 0,
					null));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
					0,
					getS().getPathIterator(),
					0, -4, 3, -3,
					null));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
					0,
					getS().getPathIterator(),
					0, -4, 4, -3,
					null));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
					0,
					getS().getPathIterator(),
					0, -4, 3, -2,
					null));
		}
	}

	@DisplayName("calculatesCrossingsPathIteratorCircleShadow")
	@Nested
	public class CalculatesCrossingsPathIteratorCircleShadow {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2ai.calculatesCrossingsPathIteratorCircleShadow(
					0,
					getS().getPathIterator(),
					-2, 1, 1,
					null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorCircleShadow(
					0,
					getS().getPathIterator(),
					-2, 1, 2,
					null));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorCircleShadow(
					0,
					getS().getPathIterator(),
					0, 1, 3,
					null));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorCircleShadow(
					0,
					getS().getPathIterator(),
					3, -1, 8,
					null));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorCircleShadow(
					0,
					getS().getPathIterator(),
					3, -1, 1,
					null));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2ai.calculatesCrossingsPathIteratorCircleShadow(
					0,
					getS().getPathIterator(),
					4, -1, 0,
					null));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2ai.calculatesCrossingsPathIteratorCircleShadow(
					0,
					getS().getPathIterator(),
					20, 0, 2,
					null));
		}
	}

	@DisplayName("calculatesCrossingsPathIteratorPathShadow")
	@Nested
	public class CalculatesCrossingsPathIteratorPathShadow {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			assertEquals(1, Path2ai.calculatesCrossingsPathIteratorPathShadow(
					0,
					(PathIterator2ai) path2.getPathIterator(),
					new BasicPathShadow2ai(path1),
					CrossingComputationType.STANDARD));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			path1.closePath();
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
					0,
					(PathIterator2ai) path2.getPathIterator(),
					new BasicPathShadow2ai(path1),
					CrossingComputationType.STANDARD));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			path2.closePath();
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
					0,
					(PathIterator2ai) path2.getPathIterator(),
					new BasicPathShadow2ai(path1),
					CrossingComputationType.STANDARD));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			path1.closePath();
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			path2.closePath();
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
					0,
					(PathIterator2ai) path2.getPathIterator(),
					new BasicPathShadow2ai(path1),
					CrossingComputationType.STANDARD));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
					0,
					(PathIterator2ai) path2.getPathIterator(),
					new BasicPathShadow2ai(path1),
					CrossingComputationType.AUTO_CLOSE));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			path1.closePath();
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
					0,
					(PathIterator2ai) path2.getPathIterator(),
					new BasicPathShadow2ai(path1),
					CrossingComputationType.AUTO_CLOSE));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			path2.closePath();
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
					0,
					(PathIterator2ai) path2.getPathIterator(),
					new BasicPathShadow2ai(path1),
					CrossingComputationType.AUTO_CLOSE));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			path1.closePath();
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			path2.closePath();
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
					0,
					(PathIterator2ai) path2.getPathIterator(),
					new BasicPathShadow2ai(path1),
					CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			assertEquals(0, Path2ai.calculatesCrossingsPathIteratorPathShadow(
					0,
					(PathIterator2ai) path2.getPathIterator(),
					new BasicPathShadow2ai(path1),
					CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			path1.closePath();
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
					0,
					(PathIterator2ai) path2.getPathIterator(),
					new BasicPathShadow2ai(path1),
					CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			path2.closePath();
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
					0,
					(PathIterator2ai) path2.getPathIterator(),
					new BasicPathShadow2ai(path1),
					CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			path1.closePath();
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			path2.closePath();
			assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
					0,
					(PathIterator2ai) path2.getPathIterator(),
					new BasicPathShadow2ai(path1),
					CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		}
	}

	@DisplayName("containsPoint")
	@Nested
	public class ContainsPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.containsPoint(getS().getPathIterator(), 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.containsPoint(getS().getPathIterator(), 4, 3));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.containsPoint(getS().getPathIterator(), 2, 2));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.containsPoint(getS().getPathIterator(), 2, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.containsPoint(getS().getPathIterator(), 4, 2));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.containsPoint(getS().getPathIterator(), 4, 3));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2ai.containsPoint(getS().getPathIterator(), -1, -1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2ai.containsPoint(getS().getPathIterator(), 6, 2));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.containsPoint(getS().getPathIterator(), 3, -2));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2ai.containsPoint(getS().getPathIterator(), 2, -2));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.containsPoint(getS().getPathIterator(), 0, 0));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.containsPoint(getS().getPathIterator(), 4, 3));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.containsPoint(getS().getPathIterator(), 2, 2));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.containsPoint(getS().getPathIterator(), 2, 1));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.containsPoint(getS().getPathIterator(), 4, 2));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2ai.containsPoint(getS().getPathIterator(), -1, -1));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2ai.containsPoint(getS().getPathIterator(), 6, 2));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.containsPoint(getS().getPathIterator(), 3, -2));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2ai.containsPoint(getS().getPathIterator(), 2, -2));
		}

	}

	@DisplayName("intersectsRectangle")
	@Nested
	public class IntersectsRectangle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.intersectsRectangle(getS().getPathIterator(), 0, 0, 1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.intersectsRectangle(getS().getPathIterator(), 4, 3, 1, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.intersectsRectangle(getS().getPathIterator(), 2, 2, 1, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.intersectsRectangle(getS().getPathIterator(), 2, 1, 1, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.intersectsRectangle(getS().getPathIterator(), 3, 0, 1, 1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.intersectsRectangle(getS().getPathIterator(), -1, -1, 1, 1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.intersectsRectangle(getS().getPathIterator(), 4, -3, 1, 1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2ai.intersectsRectangle(getS().getPathIterator(), -3, 4, 1, 1));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.intersectsRectangle(getS().getPathIterator(), 6, -5, 1, 1));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.intersectsRectangle(getS().getPathIterator(), 4, 0, 1, 1));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.intersectsRectangle(getS().getPathIterator(), 5, 0, 1, 1));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2ai.intersectsRectangle(getS().getPathIterator(), 0, -3, 1, 1));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2ai.intersectsRectangle(getS().getPathIterator(), 0, -3, 2, 1));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2ai.intersectsRectangle(getS().getPathIterator(), 0, -3, 3, 1));
		}

	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

		@DisplayName("(Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(0, 0));
			assertEquals(0, p.ix(), p.toString());
			assertEquals(0, p.iy(), p.toString());
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(-1, -4));
			assertEquals(0, p.ix(), p.toString());
			assertEquals(0, p.iy(), p.toString());
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(4, 0));
			assertEquals(4, p.ix(), p.toString());
			assertEquals(0, p.iy(), p.toString());
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(4, 2));
			assertEquals(4, p.ix(), p.toString());
			assertEquals(2, p.iy(), p.toString());
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(4, -1));
			assertEquals(4, p.ix(), p.toString());
			assertEquals(-1, p.iy(), p.toString());
		}

		@DisplayName("(Point2D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(2, -3));
			assertEquals(3, p.ix(), p.toString());
			assertEquals(-2, p.iy(), p.toString());
		}

		@DisplayName("(Rectangle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(0, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(4, 3, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(2, 2, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(2, 1, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(3, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(-1, -1, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(4, -3, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(-3, 4, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(6, -5, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(4, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(5, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(0, -3, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(0, -3, 2, 1)));
		}

		@DisplayName("(Circle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(0, 0, 1)));
		}

		@DisplayName("(Circle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(4, 3, 1)));
		}

		@DisplayName("(Circle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(2, 2, 1)));
		}

		@DisplayName("(Circle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(2, 1, 1)));
		}

		@DisplayName("(Circle2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(3, 0, 1)));
		}

		@DisplayName("(Circle2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(-1, -1, 1)));
		}

		@DisplayName("(Circle2ai) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(4, -3, 1)));
		}

		@DisplayName("(Circle2ai) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(-3, 4, 1)));
		}

		@DisplayName("(Circle2ai) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(6, -5, 1)));
		}

		@DisplayName("(Circle2ai) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(4, 0, 1)));
		}

		@DisplayName("(Circle2ai) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(5, 0, 1)));
		}

		@DisplayName("(Circle2ai) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createCircle(6, 2, 1)));
		}

		@DisplayName("(Circle2ai) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createCircle(-5, 0, 3)));
		}

		@DisplayName("(Segment2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(0, 0, 1, 1)));
		}

		@DisplayName("(Segment2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(4, 3, 1, 1)));
		}

		@DisplayName("(Segment2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(2, 2, 1, 1)));
		}

		@DisplayName("(Segment2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(2, 1, 1, 1)));
		}

		@DisplayName("(Segment2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(3, 0, 1, 1)));
		}

		@DisplayName("(Segment2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-1, -1, 1, 1)));
		}

		@DisplayName("(Segment2ai) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(4, -3, 1, 1)));
		}

		@DisplayName("(Segment2ai) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-3, 4, 1, 1)));
		}

		@DisplayName("(Segment2ai) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(6, -5, 1, 1)));
		}

		@DisplayName("(Segment2ai) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(4, 0, 1, 1)));
		}

		@DisplayName("(Segment2ai) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(5, 0, 1, 1)));
		}

		@DisplayName("(Segment2ai) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(-4, -4, -3, -3)));
		}

		@DisplayName("(Segment2ai) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(-1, 0, 2, 3)));
		}

		@DisplayName("(Segment2ai) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(7, 1, 18, 14)));
		}

		@DisplayName("(Path2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			assertFalse(path1.intersects(path2));
			assertFalse(path2.intersects(path1));
		}

		@DisplayName("(Path2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			path1.closePath();
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			assertTrue(path1.intersects(path2));
			assertTrue(path2.intersects(path1));
		}

		@DisplayName("(Path2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			path2.closePath();
			assertTrue(path1.intersects(path2));
			assertTrue(path2.intersects(path1));
		}

		@DisplayName("(Path2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			path1.closePath();
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			path2.closePath();
			assertTrue(path1.intersects(path2));
			assertTrue(path2.intersects(path1));
		}

		@DisplayName("(PathIterator2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			assertFalse(path1.intersects((PathIterator2ai) path2.getPathIterator()));
			assertFalse(path2.intersects((PathIterator2ai) path1.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			path1.closePath();
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			assertTrue(path1.intersects((PathIterator2ai) path2.getPathIterator()));
			assertTrue(path2.intersects((PathIterator2ai) path1.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			path2.closePath();
			assertTrue(path1.intersects((PathIterator2ai) path2.getPathIterator()));
			assertTrue(path2.intersects((PathIterator2ai) path1.getPathIterator()));
		}

		@DisplayName("(PathIterator2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void pathiterator_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path1 = createPath();
			path1.moveTo(-33, 98);
			path1.lineTo(-35, 98);
			path1.lineTo(-35, 101);
			path1.lineTo(-33, 101);
			path1.closePath();
			var path2 = createPath();
			path2.moveTo(-33, 99);
			path2.lineTo(-31, 99);
			path2.lineTo(-31, 103);
			path2.lineTo(-34, 103);
			path2.closePath();
			assertTrue(path1.intersects((PathIterator2ai) path2.getPathIterator()));
			assertTrue(path2.intersects((PathIterator2ai) path1.getPathIterator()));
		}

		@DisplayName("(Shape2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects((Shape2D) createCircle(3, 0, 1)));
		}

		@DisplayName("(Shape2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects((Shape2D) createRectangle(-1, -1, 1, 1)));
		}

	}

	@DisplayName("getCurrentX")
	@Nested
	public class GetCurrentX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(7, getS().getCurrentX());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().lineTo(148, 752);
			assertEquals(148, getS().getCurrentX());
		}

	}

	@DisplayName("getCurrentY")
	@Nested
	public class GetCurrentY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-5, getS().getCurrentY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().lineTo(148, 752);
			assertEquals(752, getS().getCurrentY());
		}
	}

	@DisplayName("getCurrentPoint")
	@Nested
	public class GetCurrentPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(7, -5, getS().getCurrentPoint());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().lineTo(148, 752);
			assertIntPointEquals(148, 752, getS().getCurrentPoint());
		}
	}

	@DisplayName("getFarthestPointTo")
	@Nested
	public class GetFarthestPointTo {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(0, 0));
			assertEquals(7, p.ix(), p.toString());
			assertEquals(-5, p.iy(), p.toString());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(-1, -4)); // remember: path is closed
			assertEquals(4, p.ix(), p.toString());
			assertEquals(3, p.iy(), p.toString());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(4, 0));
			assertEquals(7, p.ix(), p.toString());
			assertEquals(-5, p.iy(), p.toString());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(4, 2));
			assertEquals(7, p.ix(), p.toString());
			assertEquals(-5, p.iy(), p.toString());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(4, -1));
			assertEquals(7, p.ix(), p.toString());
			assertEquals(-5, p.iy(), p.toString());
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
			assertEpsilonEquals(0f, getS().getDistance(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().getDistance(createPoint(1, 0)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.071067812f, getS().getDistance(createPoint(-5, -5)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3f, getS().getDistance(createPoint(4, 6)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, getS().getDistance(createPoint(7, 0)));
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
			assertEpsilonEquals(0f, getS().getDistanceSquared(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().getDistanceSquared(createPoint(1, 0)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(50f, getS().getDistanceSquared(createPoint(-5, -5)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9f, getS().getDistanceSquared(createPoint(4, 6)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, getS().getDistanceSquared(createPoint(7, 0)));
		}

		@DisplayName("(Circle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, getS().getDistanceSquared(createCircle(-2, 1, 1)));
		}

		@DisplayName("(Circle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(-2, 1, 2)));
		}

		@DisplayName("(Circle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(0, 1, 3)));
		}

		@DisplayName("(Circle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(3, -1, 8)));
		}

		@DisplayName("(Circle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(3, -1, 1)));
		}

		@DisplayName("(Circle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(4, -1, 0)));
		}

		@DisplayName("(Circle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(144, getS().getDistanceSquared(createCircle(20, 0, 2)));
		}

		@DisplayName("(Segment2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, getS().getDistanceSquared(createSegment(-2, 1, 0, 2)));
		}

		@DisplayName("(Segment2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getDistanceSquared(createSegment(-2, 1, 0, 3)));
		}

		@DisplayName("(Segment2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceSquared(createSegment(0, 1, 2, 3)));
		}

		@DisplayName("(Segment2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(3, -1, 8, 8)));
		}

		@DisplayName("(Segment2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(3, -1, 1, 1)));
		}

		@DisplayName("(Segment2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(4, -1, 0, 0)));
		}

		@DisplayName("(Segment2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void segment_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(170, getS().getDistanceSquared(createSegment(20, 0, 25, 4)));
		}

		@DisplayName("(Rectangle2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, getS().getDistanceSquared(createRectangle(-2, 1, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceSquared(createRectangle(-2, 1, 2, 2)));
		}

		@DisplayName("(Rectangle2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(0, 1, 3, 3)));
		}

		@DisplayName("(Rectangle2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(3, -1, 8, 8)));
		}

		@DisplayName("(Rectangle2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(3, -1, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(4, -1, 0, 0)));
		}

		@DisplayName("(Rectangle2afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(170, getS().getDistanceSquared(createRectangle(20, 0, 2, 2)));
		}

		@DisplayName("(MultiShape2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(-2, 1)));
		}

		@DisplayName("(MultiShape2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceSquared(createTestMultiShape(-2, 2)));
		}

		@DisplayName("(MultiShape2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(0, 1)));
		}

		@DisplayName("(MultiShape2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(3, -1)));
		}

		@DisplayName("(MultiShape2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceSquared(createTestMultiShape(4, -1)));
		}

		@DisplayName("(MultiShape2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25, getS().getDistanceSquared(createTestMultiShape(20, 0)));
		}

		@DisplayName("(Path2afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(-2, 1)));
		}

		@DisplayName("(Path2afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(-2, 2)));
		}

		@DisplayName("(Path2afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(0, 1)));
		}

		@DisplayName("(Path2afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(3, -1)));
		}

		@DisplayName("(Path2afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestPath(4, -1)));
		}

		@DisplayName("(Path2afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(65, getS().getDistanceSquared(createTestPath(20, 0)));
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
			assertEpsilonEquals(0f, getS().getDistanceL1(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().getDistanceL1(createPoint(1, 0)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10f, getS().getDistanceL1(createPoint(-5, -5)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3f, getS().getDistanceL1(createPoint(4, 6)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, getS().getDistanceL1(createPoint(7, 0)));
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
			assertEpsilonEquals(0f, getS().getDistanceLinf(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().getDistanceLinf(createPoint(1, 0)));
		}

		@DisplayName("(Point2D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5f, getS().getDistanceLinf(createPoint(-5, -5)));
		}

		@DisplayName("(Point2D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3f, getS().getDistanceLinf(createPoint(4, 6)));
		}

		@DisplayName("(Point2D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, getS().getDistanceLinf(createPoint(7, 0)));
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
			getS().translate(3, 4);
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 3, 4);
			assertElement(pi, PathElementType.LINE_TO, 5, 6);
			assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
			assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
			assertElement(pi, PathElementType.CLOSE, 3, 4);
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
			getS().translate(createVector(3, 4));
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 3, 4);
			assertElement(pi, PathElementType.LINE_TO, 5, 6);
			assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
			assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
			assertElement(pi, PathElementType.CLOSE, 3, 4);
			assertNoElement(pi);
		}
	}

	@DisplayName("setWindingRule")
	@Nested
	public class SetWindingRule {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(PathWindingRule.NON_ZERO, getS().getWindingRule());
			for(PathWindingRule rule : PathWindingRule.values()) {
				getS().setWindingRule(rule);
				assertEquals(rule, getS().getWindingRule());
			}
		}
	}

	@DisplayName("add(Iterator)")
	@Nested
	public class AddIterator {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai p2 = createPath();
			p2.moveTo(3, 4);
			p2.lineTo(5, 6);

			getS().add(p2.getPathIterator());

			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertElement(pi, PathElementType.MOVE_TO, 3, 4);
			assertElement(pi, PathElementType.LINE_TO, 5, 6);
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
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
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
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var tr = new Transform2D();
			tr.makeTranslationMatrix(3, 4);
			var pi = getS().getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 3, 4);
			assertElement(pi, PathElementType.LINE_TO, 5, 6);
			assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
			assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
			assertElement(pi, PathElementType.CLOSE, 3, 4);
			assertNoElement(pi);
		}
	}

	@DisplayName("getPathIterator(double)")
	@Nested
	public class GetPathIteratorDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathIterator2ai pi = getS().getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO);
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.LINE_TO, 3, 1);
			assertElement(pi, PathElementType.LINE_TO, 4, 2);
			assertElement(pi, PathElementType.LINE_TO, 4, 3);
			assertElement(pi, PathElementType.LINE_TO, 4, 2);
			assertElement(pi, PathElementType.LINE_TO, 5, 2);
			assertElement(pi, PathElementType.LINE_TO, 5, 1);
			assertElement(pi, PathElementType.LINE_TO, 6, 1);
			assertElement(pi, PathElementType.LINE_TO, 6, 0);
			assertElement(pi, PathElementType.LINE_TO, 6, -1);
			assertElement(pi, PathElementType.LINE_TO, 7, -1);
			assertElement(pi, PathElementType.LINE_TO, 7, -2);
			assertElement(pi, PathElementType.LINE_TO, 7, -3);
			assertElement(pi, PathElementType.LINE_TO, 7, -4);
			assertElement(pi, PathElementType.LINE_TO, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
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
			Transform2D tr = new Transform2D();
			tr.makeTranslationMatrix(3, 4);

			Path2ai clone = getS().clone();
			clone.transform(tr);

			PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 3, 4);
			assertElement(pi, PathElementType.LINE_TO, 5, 6);
			assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
			assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
			assertElement(pi, PathElementType.CLOSE, 3, 4);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Transform2D tr2 = new Transform2D();
			tr2.makeRotationMatrix(-MathConstants.DEMI_PI);

			Path2ai clone = getS().clone();
			clone.transform(tr2);

			PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, -2);
			assertElement(pi, PathElementType.QUAD_TO, 0, -3, 3, -4);
			assertElement(pi, PathElementType.CURVE_TO, -1, -5, 5, -6, -5, -7);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Transform2D tr = new Transform2D();
			tr.makeTranslationMatrix(3, 4);
			Transform2D tr2 = new Transform2D();
			tr2.makeRotationMatrix(-MathConstants.DEMI_PI);
			Transform2D tr3 = new Transform2D();
			tr3.mul(tr, tr2);

			Path2ai clone = getS().clone();
			clone.transform(tr3);

			PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 3, 4);
			assertElement(pi, PathElementType.LINE_TO, 5, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 1, 6, 0);
			assertElement(pi, PathElementType.CURVE_TO, 2, -1, 8, -2, -2, -3);
			assertElement(pi, PathElementType.CLOSE, 3, 4);
			assertNoElement(pi);
		}

	}

	@DisplayName("createTransformedShape")
	@Nested
	public class CreateTransformedShape{

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Transform2D tr = new Transform2D();
			tr.makeTranslationMatrix(3, 4);

			Shape2ai clone = getS().createTransformedShape(tr);

			assertTrue(clone instanceof Path2ai);

			PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 3, 4);
			assertElement(pi, PathElementType.LINE_TO, 5, 6);
			assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
			assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
			assertElement(pi, PathElementType.CLOSE, 3, 4);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Transform2D tr2 = new Transform2D();
			tr2.makeRotationMatrix(-MathConstants.DEMI_PI);

			Path2ai clone = (Path2ai) getS().createTransformedShape(tr2);

			PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, -2);
			assertElement(pi, PathElementType.QUAD_TO, 0, -3, 3, -4);
			assertElement(pi, PathElementType.CURVE_TO, -1, -5, 5, -6, -5, -7);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Transform2D tr = new Transform2D();
			tr.makeTranslationMatrix(3, 4);
			Transform2D tr2 = new Transform2D();
			tr2.makeRotationMatrix(-MathConstants.DEMI_PI);
			Transform2D tr3 = new Transform2D();
			tr3.mul(tr, tr2);

			Path2ai clone = (Path2ai) getS().createTransformedShape(tr3);

			PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 3, 4);
			assertElement(pi, PathElementType.LINE_TO, 5, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 1, 6, 0);
			assertElement(pi, PathElementType.CURVE_TO, 2, -1, 8, -2, -2, -3);
			assertElement(pi, PathElementType.CLOSE, 3, 4);
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
			assertTrue(getS().contains(0, 0));
		}

		@DisplayName("(double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(4, 3));
		}

		@DisplayName("(double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(2, 2));
		}

		@DisplayName("(double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(2, 1));
		}

		@DisplayName("(double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(4, 2));
		}

		@DisplayName("(double,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-1, -1));
		}

		@DisplayName("(double,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(6, 2));
		}

		@DisplayName("(double,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(3, -2));
		}

		@DisplayName("(double,double) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(2, -2));
		}

		@DisplayName("(Rectangle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(0, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(4, 3, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(2, 2, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(2, 1, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(3, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createRectangle(3, 0, 1, 0)));
		}

		@DisplayName("(Rectangle2ai) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(3, 0, 2, 1)));
		}

		@DisplayName("(Rectangle2ai) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createRectangle(3, 0, 2, 0)));
		}

		@DisplayName("(Rectangle2ai) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(-1, -1, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(4, -3, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createRectangle(5, -3, 0, 1)));
		}

		@DisplayName("(Rectangle2ai) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(-3, 4, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(6, -5, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(4, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createRectangle(4, 0, 1, 0)));
		}

		@DisplayName("(Rectangle2ai) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(5, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(5, 0, 1, 0)));
		}

		@DisplayName("(Rectangle2ai) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(5, 0, 0, 1)));
		}

		@DisplayName("(Rectangle2ai) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createRectangle(5, 0, 0, 0)));
		}

		@DisplayName("(Rectangle2ai) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(5, 0, 2, 1)));
		}

		@DisplayName("(Rectangle2ai) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(6, 0, 1, 1)));
		}

		@DisplayName("(Shape2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0, 0, 1)));
		}

		@DisplayName("(Shape2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(4, 3, 1)));
		}

		@DisplayName("(Shape2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(2, 2, 1)));
		}

		@DisplayName("(Shape2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(2, 1, 1)));
		}

		@DisplayName("(Shape2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(3, 0, 1)));
		}

		@DisplayName("(Shape2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(3, 0, 2)));
		}

		@DisplayName("(Shape2ai) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(-1, -1, 1)));
		}

		@DisplayName("(Shape2ai) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(4, -3, 1)));
		}

		@DisplayName("(Shape2ai) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(5, -3, 1)));
		}

		@DisplayName("(Shape2ai) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(-3, 4, 1)));
		}

		@DisplayName("(Shape2ai) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(6, -5, 1)));
		}

		@DisplayName("(Shape2ai) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createCircle(4, 0, 1)));
		}

		@DisplayName("(Shape2ai) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(5, 0, 1)));
		}

		@DisplayName("(Shape2ai) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createCircle(5, 0, 0)));
		}

		@DisplayName("(Shape2ai) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(5, 0, 2)));
		}

		@DisplayName("(Shape2ai) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void shape_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(6, 0, 1)));
		}

		@DisplayName("(Point2D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(4, 3)));
		}

		@DisplayName("(Point2D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(2, 2)));
		}

		@DisplayName("(Point2D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint( 2, 1)));
		}

		@DisplayName("(Point2D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(4, 2)));
		}

		@DisplayName("(Point2D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(-1, -1)));
		}

		@DisplayName("(Point2D) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(6, 2)));
		}

		@DisplayName("(Point2D) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(3, -2)));
		}

		@DisplayName("(Point2D) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(2, -2)));
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
			assertEquals(0, bb.getMinX());
			assertEquals(-5, bb.getMinY());
			assertEquals(7, bb.getMaxX());
			assertEquals(3, bb.getMaxY());
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
			assertEquals(0, bb.getMinX());
			assertEquals(-5, bb.getMinY());
			assertEquals(7, bb.getMaxX());
			assertEquals(3, bb.getMaxY());
		}
	}

	@DisplayName("toBoundingBoxWithCtrlPoints")
	@Nested
	public class ToBoundingBoxWithCtrlPoints {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B bb = getS().toBoundingBoxWithCtrlPoints();
			assertEquals(0, bb.getMinX());
			assertEquals(-5, bb.getMinY());
			assertEquals(7, bb.getMaxX());
			assertEquals(5, bb.getMaxY());
		}
	}

	@DisplayName("removeLast")
	@Nested
	public class RemoveLast {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().removeLast();
			var pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().removeLast();
			getS().removeLast();
			var pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertNoElement(pi);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().removeLast();
			getS().removeLast();
			getS().removeLast();
			var pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertNoElement(pi);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().removeLast();
			getS().removeLast();
			getS().removeLast();
			getS().removeLast();
			var pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().removeLast();
			getS().removeLast();
			getS().removeLast();
			getS().removeLast();
			getS().removeLast();
			var pi = getS().getPathIterator();
			assertNoElement(pi);
		}
	}

	@DisplayName("setLastPoint")
	@Nested
	public class SetLastPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setLastPoint(123, 789);
			var pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 123, 789);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);

			getS().setLastPoint(createPoint(123, 789));

			pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 123, 789);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

	}

	@DisplayName("remove(int,int)")
	@Nested
	public class RemoveIntInt {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().remove(2, 2);
			var pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().remove(2, 2);
			getS().remove(4, 3);
			var pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().remove(2, 2);
			getS().remove(4, 3);
			getS().remove(6, 5);
			var pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().remove(2, 2);
			getS().remove(4, 3);
			getS().remove(6, 5);
			getS().remove(6, 5);
			var pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}
	}

	@DisplayName("calculatesDrawableElementBoundingBox")
	@Nested
	public class CalculatesDrawableElementBoundingBox {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = createRectangle(0, 0, 0, 0);
			assertTrue(Path2ai.calculatesDrawableElementBoundingBox(getS().getPathIterator(), box));
			assertEquals(0, box.getMinX());
			assertEquals(-5, box.getMinY());
			assertEquals(7, box.getMaxX());
			assertEquals(3, box.getMaxY());
		}

	}

	@DisplayName("findsClosestPointPathIteratorPoint")
	@Nested
	public class FindsClosestPointPathIteratorPoint {

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
			Path2ai.findsClosestPointPathIteratorPoint(getS().getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 0, 0, p);
			assertEquals(0, p.ix(), p.toString());
			assertEquals(0, p.iy(), p.toString());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai.findsClosestPointPathIteratorPoint(getS().getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), -1, -4, p);
			assertEquals( 0, p.ix(), p.toString());
			assertEquals(0, p.iy(), p.toString());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai.findsClosestPointPathIteratorPoint(getS().getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 4, 0, p);
			assertEquals(4, p.ix(), p.toString());
			assertEquals(0, p.iy(), p.toString());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai.findsClosestPointPathIteratorPoint(getS().getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 4, 2, p);
			assertEquals(4, p.ix(), p.toString());
			assertEquals(2, p.iy(), p.toString());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai.findsClosestPointPathIteratorPoint(getS().getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 4, -1, p);
			assertEquals(4, p.ix(), p.toString());
			assertEquals(-1, p.iy(), p.toString());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai.findsClosestPointPathIteratorPoint(getS().getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 2, -3, p);
			assertEquals(3, p.ix(), p.toString());
			assertEquals(-2, p.iy(), p.toString());
		}
	}

	@DisplayName("findsClosestPointPathIteratorPathIterator")
	@Nested
	public class FindsClosestPointPathIteratorPathIterator {

		private Point2D result;

		@BeforeEach
		public void setUp() {
			result = createPoint(0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai.findsClosestPointPathIteratorPathIterator(
					(PathIterator2ai) getS().getFlatteningPathIterator(),
					(PathIterator2ai) createTestPath().getPathIterator(),
					result);
			assertIntPointEquals(7, -1, result);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai.findsClosestPointPathIteratorPathIterator(
					(PathIterator2ai) getS().getFlatteningPathIterator(),
					(PathIterator2ai) createTestPath(PathWindingRule.EVEN_ODD).getPathIterator(),
					result);
			assertIntPointEquals(7, -1, result);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai.findsClosestPointPathIteratorPathIterator(
					(PathIterator2ai) getS().getFlatteningPathIterator(),
					(PathIterator2ai) createTestPath(PathWindingRule.NON_ZERO).getPathIterator(),
					result);
			assertIntPointEquals(7, -1, result);
		}
	}

	@DisplayName("findsFarthestPointPathIteratorPoint")
	@Nested
	public class FindsFarthestPointPathIteratorPoint {

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
			Path2ai.findsFarthestPointPathIteratorPoint(getS().getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 0, 0, p);
			assertEquals(7, p.ix(), p.toString());
			assertEquals(-5, p.iy(), p.toString());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai.findsFarthestPointPathIteratorPoint(getS().getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), -1, -4, p);
			assertEquals(4, p.ix(), p.toString());
			assertEquals(3, p.iy(), p.toString());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai.findsFarthestPointPathIteratorPoint(getS().getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 4, 0, p);
			assertEquals(7, p.ix(), p.toString());
			assertEquals(-5, p.iy(), p.toString());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai.findsFarthestPointPathIteratorPoint(getS().getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 4, 2, p);
			assertEquals(7, p.ix(), p.toString());
			assertEquals(-5, p.iy(), p.toString());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai.findsFarthestPointPathIteratorPoint(getS().getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 4, -1, p);
			assertEquals(7, p.ix(), p.toString());
			assertEquals(-5, p.iy(), p.toString());
		}
	}

	@DisplayName("moveTo")
	@Nested
	public class MoveTo {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.moveTo(15, 145);

			PathIterator2ai<?> pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 145);
			assertNoElement(pi);

			tmpShape = createPath();
			tmpShape.moveTo(15, 145);
			tmpShape.moveTo(-15, -954);

			pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -15, -954);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.moveTo(createPoint(15, 145));

			PathIterator2ai<?> pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 145);
			assertNoElement(pi);

			tmpShape = createPath();
			tmpShape.moveTo(createPoint(15, 145));
			tmpShape.moveTo(createPoint(-15, -954));

			pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -15, -954);
			assertNoElement(pi);
		}

	}

	@DisplayName("lineTo")
	@Nested
	public class LineTo {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.lineTo(15, 145);
			});
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.moveTo(15, 145);
			tmpShape.lineTo(189, -45);

			PathIterator2ai<?> pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 145);
			assertElement(pi, PathElementType.LINE_TO, 189, -45);
			assertNoElement(pi);

			tmpShape = createPath();
			tmpShape.moveTo(15, 145);
			tmpShape.lineTo(189, -45);
			tmpShape.lineTo(-5, 0);

			pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 145);
			assertElement(pi, PathElementType.LINE_TO, 189, -45);
			assertElement(pi, PathElementType.LINE_TO, -5, 0);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.lineTo(createPoint(15, 145));
			});
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.moveTo(15, 145);
			tmpShape.lineTo(createPoint(189, -45));

			PathIterator2ai<?> pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 145);
			assertElement(pi, PathElementType.LINE_TO, 189, -45);
			assertNoElement(pi);

			tmpShape = createPath();
			tmpShape.moveTo(15, 145);
			tmpShape.lineTo(createPoint(189, -45));
			tmpShape.lineTo(createPoint(-5, 0));

			pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 15, 145);
			assertElement(pi, PathElementType.LINE_TO, 189, -45);
			assertElement(pi, PathElementType.LINE_TO, -5, 0);
			assertNoElement(pi);
		}

	}

	@DisplayName("quadTo")
	@Nested
	public class QuadTo {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			assertThrows(IllegalStateException.class, () -> {
				tmpShape.quadTo(15, 145, 50, 20);
			});
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.moveTo(4, 6);
			tmpShape.quadTo(15, 145, 50, 20);

			PathIterator2ai<?> pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 4, 6);
			assertElement(pi, PathElementType.QUAD_TO, 15, 145, 50, 20);
			assertNoElement(pi);

			tmpShape = createPath();
			tmpShape.moveTo(4, 6);
			tmpShape.quadTo(15, 145, 50, 20);
			tmpShape.quadTo(-42, 0, -47, -60);

			pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 4, 6);
			assertElement(pi, PathElementType.QUAD_TO, 15, 145, 50, 20);
			assertElement(pi, PathElementType.QUAD_TO, -42, 0, -47, -60);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.quadTo(createPoint(15, 145), createPoint(50, 20));
			});
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.moveTo(4, 6);
			tmpShape.quadTo(createPoint(15, 145), createPoint(50, 20));

			PathIterator2ai<?> pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 4, 6);
			assertElement(pi, PathElementType.QUAD_TO, 15, 145, 50, 20);
			assertNoElement(pi);

			tmpShape = createPath();
			tmpShape.moveTo(4, 6);
			tmpShape.quadTo(createPoint(15, 145), createPoint(50, 20));
			tmpShape.quadTo(createPoint(-42, 0), createPoint(-47, -60));

			pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 4, 6);
			assertElement(pi, PathElementType.QUAD_TO, 15, 145, 50, 20);
			assertElement(pi, PathElementType.QUAD_TO, -42, 0, -47, -60);
			assertNoElement(pi);
		}

	}

	@DisplayName("curveTo")
	@Nested
	public class CurveTo {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.curveTo(15, 145, 50, 20, 0, 0);
			});
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.moveTo(4, 6);
			tmpShape.curveTo(15, 145, 50, 20, 0, 0);

			PathIterator2ai<?> pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 4, 6);
			assertElement(pi, PathElementType.CURVE_TO, 15, 145, 50, 20, 0, 0);
			assertNoElement(pi);

			tmpShape = createPath();
			tmpShape.moveTo(4, 6);
			tmpShape.curveTo(15, 145, 50, 20, 0, 0);
			tmpShape.curveTo(-42, 0, -47, -60, 1, 2);

			pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 4, 6);
			assertElement(pi, PathElementType.CURVE_TO, 15, 145, 50, 20, 0, 0);
			assertElement(pi, PathElementType.CURVE_TO, -42, 0, -47, -60, 1, 2);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.curveTo(createPoint(15, 145), createPoint(50, 20), createPoint(0, 0));
			});
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.moveTo(4, 6);
			tmpShape.curveTo(createPoint(15, 145), createPoint(50, 20), createPoint(0, 0));

			PathIterator2ai<?> pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 4, 6);
			assertElement(pi, PathElementType.CURVE_TO, 15, 145, 50, 20, 0, 0);
			assertNoElement(pi);

			tmpShape = createPath();
			tmpShape.moveTo(4, 6);
			tmpShape.curveTo(createPoint(15, 145), createPoint(50, 20), createPoint(0, 0));
			tmpShape.curveTo(createPoint(-42, 0), createPoint(-47, -60), createPoint(1, 2));

			pi = tmpShape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 4, 6);
			assertElement(pi, PathElementType.CURVE_TO, 15, 145, 50, 20, 0, 0);
			assertElement(pi, PathElementType.CURVE_TO, -42, 0, -47, -60, 1, 2);
			assertNoElement(pi);
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
			assertEpsilonEquals(98, getS().getLengthSquared());
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
			assertEpsilonEquals(9.899494937, getS().getLength());
		}
	}

	@DisplayName("getCoordAt")
	@Nested
	public class GetCoordAt {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(7, getS().size());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, getS().getCoordAt(0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, getS().getCoordAt(1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(2, getS().getCoordAt(2));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(2, getS().getCoordAt(3));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(3, getS().getCoordAt(4));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, getS().getCoordAt(5));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(4, getS().getCoordAt(6));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(3, getS().getCoordAt(7));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(5, getS().getCoordAt(8));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, getS().getCoordAt(9));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(6, getS().getCoordAt(10));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(5, getS().getCoordAt(11));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(7, getS().getCoordAt(12));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-5, getS().getCoordAt(13));
		}
	}

	@DisplayName("toCollection")
	@Nested
	public class ToCollection {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Collection<Point2D> expected = Arrays.asList(
					createPoint(0, 0),
					createPoint(2, 2),
					createPoint(3, 0),
					createPoint(4, 3),
					createPoint(5, -1),
					createPoint(6, 5),
					createPoint(7, -5));
			Collection<?> points = getS().toCollection();
			assertCollectionEquals(expected, points);
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
			T newPath = (T) createPath();
			newPath.moveTo(14, -5);
			newPath.lineTo(1, 6);
			newPath.quadTo(-5, 1, 10, -1);
			newPath.curveTo(18, 19, -50, 51, 1, 0);

			getS().set(newPath);

			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 14, -5);
			assertElement(pi, PathElementType.LINE_TO, 1, 6);
			assertElement(pi, PathElementType.QUAD_TO, -5, 1, 10, -1);
			assertElement(pi, PathElementType.CURVE_TO, 18, 19, -50, 51, 1, 0);
			assertNoElement(pi);
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
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 3, 4);
			assertElement(pi, PathElementType.LINE_TO, 5, 6);
			assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
			assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
			assertElement(pi, PathElementType.CLOSE, 3, 4);
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
			T r = getS().operator_plus(createVector(3, 4));
			PathIterator2ai pi = r.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 3, 4);
			assertElement(pi, PathElementType.LINE_TO, 5, 6);
			assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
			assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
			assertElement(pi, PathElementType.CLOSE, 3, 4);
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
			getS().operator_remove(createVector(3, 4));
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -3, -4);
			assertElement(pi, PathElementType.LINE_TO, -1, -2);
			assertElement(pi, PathElementType.QUAD_TO, 0, -4, 1, -1);
			assertElement(pi, PathElementType.CURVE_TO, 2, -5, 3, 1, 4, -9);
			assertElement(pi, PathElementType.CLOSE, -3, -4);
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
			T r = getS().operator_minus(createVector(3, 4));
			PathIterator2ai pi = r.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -3, -4);
			assertElement(pi, PathElementType.LINE_TO, -1, -2);
			assertElement(pi, PathElementType.QUAD_TO, 0, -4, 1, -1);
			assertElement(pi, PathElementType.CURVE_TO, 2, -5, 3, 1, 4, -9);
			assertElement(pi, PathElementType.CLOSE, -3, -4);
			assertNoElement(pi);
		}
	}

	@DisplayName("this * Trasform2D")
	@Nested
	public class OperatorMultiplyTransform2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Transform2D tr = new Transform2D();
			tr.makeTranslationMatrix(3, 4);
			Transform2D tr2 = new Transform2D();
			tr2.makeRotationMatrix(-MathConstants.DEMI_PI);
			Transform2D tr3 = new Transform2D();
			tr3.mul(tr, tr2);

			Path2ai clone = (Path2ai) getS().operator_multiply(tr3);

			PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 3, 4);
			assertElement(pi, PathElementType.LINE_TO, 5, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 1, 6, 0);
			assertElement(pi, PathElementType.CURVE_TO, 2, -1, 8, -2, -2, -3);
			assertElement(pi, PathElementType.CLOSE, 3, 4);
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
			assertTrue(getS().operator_and(createPoint(4, 3)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(2, 2)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint( 2, 1)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(4, 2)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(-1, -1)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(6, 2)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(3, -2)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(2, -2)));
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
			assertTrue(getS().operator_and(createCircle(3, 0, 1)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createRectangle(-1, -1, 1, 1)));
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
			assertEpsilonEquals(0f, getS().operator_upTo(createPoint(0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0f, getS().operator_upTo(createPoint(1, 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.071067812f, getS().operator_upTo(createPoint(-5, -5)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3f, getS().operator_upTo(createPoint(4, 6)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1f, getS().operator_upTo(createPoint(7, 0)));
		}
	}

	@DisplayName("isCurved")
	@Nested
	public class IsCurved {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().isCurved());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertFalse(getS().isCurved());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			assertFalse(getS().isCurved());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			assertFalse(getS().isCurved());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			assertFalse(getS().isCurved());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			getS().closePath();
			assertFalse(getS().isCurved());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().closePath();
			assertFalse(getS().isCurved());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().curveTo(7, 8, 9, 10, 11, 12);
			assertTrue(getS().isCurved());
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().quadTo(7, 8, 9, 10);
			assertTrue(getS().isCurved());
		}
	}

	@DisplayName("isMultiParts")
	@Nested
	public class IsMultiParts {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			getS().closePath();
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().curveTo(7, 8, 9, 10, 11, 12);
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().lineTo(3, 4);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			getS().quadTo(7, 8, 9, 10);
			assertTrue(getS().isMultiParts());
		}
	}

	@DisplayName("isPolygon")
	@Nested
	public class IsPolygon {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().isPolygon());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertFalse(getS().isPolygon());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			assertFalse(getS().isPolygon());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			assertFalse(getS().isPolygon());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			assertFalse(getS().isPolygon());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			getS().closePath();
			assertTrue(getS().isPolygon());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().closePath();
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().curveTo(7, 8, 9, 10, 11, 12);
			assertFalse(getS().isPolygon());
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().curveTo(7, 8, 9, 10, 11, 12);
			getS().closePath();
			assertTrue(getS().isPolygon());
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().quadTo(7, 8, 9, 10);
			assertFalse(getS().isPolygon());
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().quadTo(7, 8, 9, 10);
			getS().closePath();
			assertTrue(getS().isPolygon());
		}
	}

	@DisplayName("isPolyline")
	@Nested
	public class IsPolyline {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().isPolyline());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertFalse(getS().isPolyline());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			assertFalse(getS().isPolyline());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			assertFalse(getS().isPolyline());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			assertTrue(getS().isPolyline());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			getS().closePath();
			assertFalse(getS().isPolyline());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().curveTo(7, 8, 9, 10, 11, 12);
			getS().closePath();
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().quadTo(7, 8, 9, 10);
			getS().closePath();
			assertFalse(getS().isPolyline());
		}
	}

	@DisplayName("arcTo")
	@Nested
	public class ArcTo {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(5, 5, 20, 10, 0, 1, ArcType.ARC_ONLY);
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertElement(pi, PathElementType.CURVE_TO, 6, 1, 12, 7, 20, 10);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(5, 5, 20, 10, 0, 1, ArcType.LINE_THEN_ARC);
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertElement(pi, PathElementType.CURVE_TO, 6, 1, 12, 7, 20, 10);
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(5, 5, 20, 10, 0, 1, ArcType.MOVE_THEN_ARC);
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertElement(pi, PathElementType.CURVE_TO, 6, 1, 12, 7, 20, 10);
			assertNoElement(pi);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(5, 5, 20, 10, .25, 1, ArcType.ARC_ONLY);
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertElement(pi, PathElementType.CURVE_TO, 9, 4, 14, 8, 20, 10);
			assertNoElement(pi);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(5, 5, 20, 10, .25, 1, ArcType.LINE_THEN_ARC);
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 6, 0);
			assertElement(pi, PathElementType.CURVE_TO, 9, 4, 14, 8, 20, 10);
			assertNoElement(pi);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(5, 5, 20, 10, .25, 1, ArcType.MOVE_THEN_ARC);
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertElement(pi, PathElementType.MOVE_TO, 6, 0);
			assertElement(pi, PathElementType.CURVE_TO, 9, 4, 14, 8, 20, 10);
			assertNoElement(pi);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(createPoint(5, 5), createPoint(20, 10), 0, 1, ArcType.ARC_ONLY);
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertElement(pi, PathElementType.CURVE_TO, 6, 1, 12, 7, 20, 10);
			assertNoElement(pi);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(5, 5, 20, 10);
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertElement(pi, PathElementType.CURVE_TO, 6, 1, 12, 7, 20, 10);
			assertNoElement(pi);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(createPoint(5, 5), createPoint(20, 10));
			PathIterator2ai pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 2, 2);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertElement(pi, PathElementType.CURVE_TO, 6, 1, 12, 7, 20, 10);
			assertNoElement(pi);
		}

	}

	@DisplayName("getClosestPointTo")
	@Nested
	public class GetClosestPointTo {

		@DisplayName("(Circle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(0, 0, getS().getClosestPointTo(createCircle(-2, 1, 1)));
		}

		@DisplayName("(Circle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(-2, 1, 2));
		}

		@DisplayName("(Circle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(0, 1, 3));
		}

		@DisplayName("(Circle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(3, -1, 8));
		}

		@DisplayName("(Circle2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(3, -1, 1));
		}

		@DisplayName("(Circle2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(4, -1, 0));
		}

		@DisplayName("(Circle2ai) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(6, 0, getS().getClosestPointTo(createCircle(20, 0, 2)));
		}

		@DisplayName("(Circle2ai) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void circle_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(1, 1, getS().getClosestPointTo(createSegment(-2, 1, 0, 2)));
			assertIntPointEquals(0, 0, getS().getClosestPointTo(createSegment(-2, 1, 0, 3)));
			assertIntPointEquals(0, 0, getS().getClosestPointTo(createSegment(0, 1, 2, 3)));
			assertClosestPointInBothShapes(getS(), createSegment(3, -1, 8, 8));
			assertClosestPointInBothShapes(getS(), createSegment(3, -1, 1, 1));
			assertClosestPointInBothShapes(getS(), createSegment(4, -1, 0, 0));
			assertIntPointEquals(7, -1, getS().getClosestPointTo(createSegment(20, 0, 25, 4)));
		}

		@DisplayName("(Rectangle2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(0, 0, getS().getClosestPointTo(createRectangle(-2, 1, 1, 1)));
		}

		@DisplayName("(Rectangle2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(0, 0, getS().getClosestPointTo(createRectangle(-2, 1, 2, 2)));
		}

		@DisplayName("(Rectangle2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(0, 1, 3, 3));
		}

		@DisplayName("(Rectangle2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(3, -1, 8, 8));
		}

		@DisplayName("(Rectangle2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(3, -1, 1, 1));
		}

		@DisplayName("(Rectangle2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(4, -1, 0, 0));
		}

		@DisplayName("(Rectangle2ai) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void rectangle_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(7, -1, getS().getClosestPointTo(createRectangle(20, 0, 2, 2)));
		}

		@DisplayName("(MultiShape2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestMultiShape(-2, 1));
		}

		@DisplayName("(MultiShape2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(2, 2, getS().getClosestPointTo(createTestMultiShape(-2, 2)));
		}

		@DisplayName("(MultiShape2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestMultiShape(0, 1));
		}

		@DisplayName("(MultiShape2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestMultiShape(3, -1));
		}

		@DisplayName("(MultiShape2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(5, 2, getS().getClosestPointTo(createTestMultiShape(4, -1)));
		}

		@DisplayName("(MultiShape2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void multishape_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(7, -1, getS().getClosestPointTo(createTestMultiShape(20, 0)));
		}

		@DisplayName("(Path2ai) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(-2, 1));
		}

		@DisplayName("(Path2ai) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(-2, 2));
		}

		@DisplayName("(Path2ai) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(0, 1));
		}

		@DisplayName("(Path2ai) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(3, -1));
		}

		@DisplayName("(Path2ai) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestPath(4, -1));
		}

		@DisplayName("(Path2ai) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void path_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertIntPointEquals(7, -1, getS().getClosestPointTo(createTestPath(20, 0)));
		}

	}

}