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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.ai.GeomFactory2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathElement2ai;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractGeomFactory2aiTest extends AbstractMathTestCase {

	private GeomFactory2ai<?, ?, ?, ?> factory;

	protected abstract GeomFactory2ai<?, ?, ?, ?> createFactory();

	protected abstract Point2D createPoint(int x, int y);

	protected abstract Vector2D createVector(int x, int y);

	@BeforeEach
	public void setUp() throws Exception {
		factory = createFactory();
	}

	@AfterEach
	public void tearDown() throws Exception {
		factory = null;
	}

	@DisplayName("convertToPoint")
	@Nested
	public class ConvertToPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D p = createPoint(45, 56);
			Point2D p2 = factory.convertToPoint(p);
			assertSame(p, p2);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D p = new InnerComputationPoint2D(45, 56);
			Point2D p2 = factory.convertToPoint(p);
			assertNotSame(p, p2);
			assertEquals(p, p2);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D v = new InnerComputationVector2D(45, 56);
			Point2D p = factory.convertToPoint(v);
			assertNotSame(v, p);
			assertEquals(v, p);
		}

	}

	@DisplayName("convertToVector")
	@Nested
	public class ConvertToVector {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D p = new InnerComputationPoint2D(45, 56);
			Vector2D v = factory.convertToVector(p);
			assertNotSame(p, v);
			assertEquals(p, v);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D v = createVector(45, 56);
			Vector2D v2 = factory.convertToVector(v);
			assertSame(v, v2);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D v = new InnerComputationVector2D(45, 56);
			Vector2D v2 = factory.convertToVector(v);
			assertNotSame(v, v2);
			assertEquals(v, v2);
		}

	}

	@DisplayName("newPoint")
	@Nested
	public class NewPoint {

		@DisplayName("()")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D p = factory.newPoint();
			assertNotNull(p);
			assertEquals(0, p.ix());
			assertEquals(0, p.iy());
			Point2D ref = createPoint(0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}

		@DisplayName("(int,int)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D p = factory.newPoint(15, 48);
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(48, p.iy());
			Point2D ref = createPoint(0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}

		@DisplayName("(double,double)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D p = factory.newPoint(15.56, 48.32);
			assertNotNull(p);
			assertEpsilonEquals(16, p.getX());
			assertEpsilonEquals(48, p.getY());
			Point2D ref = createPoint(0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}

	}

	@DisplayName("newVector")
	@Nested
	public class NewVector {

		@DisplayName("()")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D p = factory.newVector();
			assertNotNull(p);
			assertEquals(0, p.ix());
			assertEquals(0, p.iy());
			Vector2D ref = createVector(0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}

		@DisplayName("(int,int)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D p = factory.newVector(15, 48);
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(48, p.iy());
			Vector2D ref = createVector(0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}

		@DisplayName("(double,double)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D p = factory.newVector(15.45, 48.87);
			assertNotNull(p);
			assertEpsilonEquals(15, p.getX());
			assertEpsilonEquals(49, p.getY());
			Vector2D ref = createVector(0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}

	}

	@DisplayName("newPath")
	@Nested
	public class NewPath {

		@DisplayName("(NON_ZERO)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai<?, ?, ?, ?, ?, ?> path = factory.newPath(PathWindingRule.NON_ZERO);
			assertNotNull(path);
			assertSame(PathWindingRule.NON_ZERO, path.getWindingRule());
			assertEquals(0, path.size());
		}

		@DisplayName("(EVEN_ODD)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2ai<?, ?, ?, ?, ?, ?> path = factory.newPath(PathWindingRule.EVEN_ODD);
			assertNotNull(path);
			assertSame(PathWindingRule.EVEN_ODD, path.getWindingRule());
			assertEquals(0, path.size());
		}
	}

	@DisplayName("newSegment")
	@Nested
	public class NewSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Segment2ai<?, ?, ?, ?, ?, ?> s = factory.newSegment(1,  2,  3,  4);
			assertNotNull(s);
			assertEquals(1, s.getX1());
			assertEquals(2, s.getY1());
			assertEquals(3, s.getX2());
			assertEquals(4, s.getY2());
		}

	}

	@DisplayName("newBox")
	@Nested
	public class NewBox {

		@DisplayName("()")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2ai<?, ?, ?, ?, ?, ?> r = factory.newBox();
			assertNotNull(r);
			assertEquals(0, r.getMinX());
			assertEquals(0, r.getMinY());
			assertEquals(0, r.getMaxX());
			assertEquals(0, r.getMaxY());
		}

		@DisplayName("(double,double,double,double)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2ai<?, ?, ?, ?, ?, ?> r = factory.newBox(1, 2, 3, 4);
			assertNotNull(r);
			assertEquals(1, r.getMinX());
			assertEquals(2, r.getMinY());
			assertEquals(4, r.getMaxX());
			assertEquals(6, r.getMaxY());
		}

	}

	@DisplayName("newMovePathElement")
	@Nested
	public class NewMovePathElement {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathElement2ai element = factory.newMovePathElement(1, 2);
			assertNotNull(element);
			assertSame(PathElementType.MOVE_TO, element.getType());
			assertEquals(0, element.getFromX());
			assertEquals(0, element.getFromY());
			assertEquals(0, element.getCtrlX1());
			assertEquals(0, element.getCtrlY1());
			assertEquals(0, element.getCtrlX2());
			assertEquals(0, element.getCtrlY2());
			assertEquals(1, element.getToX());
			assertEquals(2, element.getToY());
		}

	}

	@DisplayName("newLinePathElement")
	@Nested
	public class NewLinePathElement {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathElement2ai element = factory.newLinePathElement(1, 2, 3, 4);
			assertNotNull(element);
			assertSame(PathElementType.LINE_TO, element.getType());
			assertEquals(1, element.getFromX());
			assertEquals(2, element.getFromY());
			assertEquals(0, element.getCtrlX1());
			assertEquals(0, element.getCtrlY1());
			assertEquals(0, element.getCtrlX2());
			assertEquals(0, element.getCtrlY2());
			assertEquals(3, element.getToX());
			assertEquals(4, element.getToY());
		}
	}

	@DisplayName("newClosePathElement")
	@Nested
	public class NewClosePathElement {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathElement2ai element = factory.newClosePathElement(1, 2, 3, 4);
			assertNotNull(element);
			assertSame(PathElementType.CLOSE, element.getType());
			assertEquals(1, element.getFromX());
			assertEquals(2, element.getFromY());
			assertEquals(0, element.getCtrlX1());
			assertEquals(0, element.getCtrlY1());
			assertEquals(0, element.getCtrlX2());
			assertEquals(0, element.getCtrlY2());
			assertEquals(3, element.getToX());
			assertEquals(4, element.getToY());
		}
	}

	@DisplayName("newCurvePathElement")
	@Nested
	public class NewCurvePathElement {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathElement2ai element = factory.newCurvePathElement(1, 2, 3, 4, 5, 6);
			assertNotNull(element);
			assertSame(PathElementType.QUAD_TO, element.getType());
			assertEquals(1, element.getFromX());
			assertEquals(2, element.getFromY());
			assertEquals(3, element.getCtrlX1());
			assertEquals(4, element.getCtrlY1());
			assertEquals(0, element.getCtrlX2());
			assertEquals(0, element.getCtrlY2());
			assertEquals(5, element.getToX());
			assertEquals(6, element.getToY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathElement2ai element = factory.newCurvePathElement(1, 2, 3, 4, 5, 6, 7, 8);
			assertNotNull(element);
			assertSame(PathElementType.CURVE_TO, element.getType());
			assertEquals(1, element.getFromX());
			assertEquals(2, element.getFromY());
			assertEquals(3, element.getCtrlX1());
			assertEquals(4, element.getCtrlY1());
			assertEquals(5, element.getCtrlX2());
			assertEquals(6, element.getCtrlY2());
			assertEquals(7, element.getToX());
			assertEquals(8, element.getToY());
		}

	}

	@DisplayName("newArcPathElement")
	@Nested
	public class NewArcPathElement {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathElement2ai element = factory.newArcPathElement(1, 2, 3, 4, 5, 6, 7, true, false);
			assertNotNull(element);
			assertSame(PathElementType.ARC_TO, element.getType());
			assertEquals(1, element.getFromX());
			assertEquals(2, element.getFromY());
			assertEquals(3, element.getToX());
			assertEquals(4, element.getToY());
			assertEquals(5, element.getRadiusX());
			assertEquals(6, element.getRadiusY());
			assertEpsilonEquals(7, element.getRotationX());
			assertTrue(element.getLargeArcFlag());
			assertFalse(element.getSweepFlag());
		}

	}

}