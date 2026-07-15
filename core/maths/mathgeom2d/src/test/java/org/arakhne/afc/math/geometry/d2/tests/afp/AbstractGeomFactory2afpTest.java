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
import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.afp.GeomFactory2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathElement2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractGeomFactory2afpTest extends AbstractMathTestCase {

	private GeomFactory2afp<?, ?, ?, ?> factory;
	
	protected abstract GeomFactory2afp<?, ?, ?, ?> createFactory();
	
	protected abstract Point2D createPoint(double x, double y);

	protected abstract Vector2D createVector(double x, double y);

	@BeforeEach
	public void setUp() throws Exception {
		this.factory = createFactory();
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		this.factory = null;
	}

	@DisplayName("convertToPoint")
	public class ConvertToPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void convertToPointPoint2D_expectedPointType(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D p = createPoint(45, 56);
			Point2D p2 = factory.convertToPoint(p);
			assertSame(p, p2);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void convertToPointPoint2D_notExpectedPointType(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D p = new InnerComputationPoint2D(45, 56);
			Point2D p2 = factory.convertToPoint(p);
			assertNotSame(p, p2);
			assertEquals(p, p2);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void convertToPointVector2D(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D v = new InnerComputationVector2D(45, 56);
			Point2D p = factory.convertToPoint(v);
			assertNotSame(v, p);
			assertEquals(v, p);
		}

	}
	
	@DisplayName("convertToVector")
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
		public void convertToVectorVector2D_expectedVectorType(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D v = createVector(45, 56);
			Vector2D v2 = factory.convertToVector(v);
			assertSame(v, v2);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void convertToVectorVector2D_notExpectedVectorType(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D v = new InnerComputationVector2D(45, 56);
			Vector2D v2 = factory.convertToVector(v);
			assertNotSame(v, v2);
			assertEquals(v, v2);
		}

	}
	
	@DisplayName("newPoint")
	public class NewPoint {

		@DisplayName("()")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void newPoint(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D p = factory.newPoint();
			assertNotNull(p);
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
			Point2D ref = createPoint(0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}
	
		@DisplayName("(int,int)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void newPointIntInt(CoordinateSystem2D cs) {
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
		public void newPointDoubleDouble(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D p = factory.newPoint(15.34, 48.56);
			assertNotNull(p);
			assertEpsilonEquals(15.34, p.getX());
			assertEpsilonEquals(48.56, p.getY());
			Point2D ref = createPoint(0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}

	}

	@DisplayName("newVector")
	public class NewVector {

		@DisplayName("()")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void newVector(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D p = factory.newVector();
			assertNotNull(p);
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
			Vector2D ref = createVector(0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}
	
		@DisplayName("(int,int)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void newVectorIntInt(CoordinateSystem2D cs) {
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
		public void newVectorDoubleDouble(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D p = factory.newVector(15.45, 48.67);
			assertNotNull(p);
			assertEpsilonEquals(15.45, p.getX());
			assertEpsilonEquals(48.67, p.getY());
			Vector2D ref = createVector(0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}

	}

	@DisplayName("newPath")
	public class NewPath {

		@DisplayName("NON_ZERO")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void newPath_NONZERO(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp<?, ?, ?, ?, ?, ?> path = factory.newPath(PathWindingRule.NON_ZERO);
			assertNotNull(path);
			assertSame(PathWindingRule.NON_ZERO, path.getWindingRule());
			assertEquals(0, path.size());
		}

		@DisplayName("EVEN_ODD")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void newPath_EVENODD(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp<?, ?, ?, ?, ?, ?> path = factory.newPath(PathWindingRule.EVEN_ODD);
			assertNotNull(path);
			assertSame(PathWindingRule.EVEN_ODD, path.getWindingRule());
			assertEquals(0, path.size());
		}

	}

	@DisplayName("newBox")
	public class NewBox {

		@DisplayName("()")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void newBox(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp<?, ?, ?, ?, ?, ?> r = factory.newBox();
			assertNotNull(r);
			assertEpsilonEquals(0, r.getMinX());
			assertEpsilonEquals(0, r.getMinY());
			assertEpsilonEquals(0, r.getMaxX());
			assertEpsilonEquals(0, r.getMaxY());
		}
	
		@DisplayName("(double,double,double,double)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void newBoxNumberNumberNumberNumber(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp<?, ?, ?, ?, ?, ?> r = factory.newBox(1, 2, 3, 4);
			assertNotNull(r);
			assertEpsilonEquals(1, r.getMinX());
			assertEpsilonEquals(2, r.getMinY());
			assertEpsilonEquals(4, r.getMaxX());
			assertEpsilonEquals(6, r.getMaxY());
		}

	}

	@DisplayName("newPathElement")
	public class NewPathElement {

		@DisplayName("Move")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void newMovePathElement(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathElement2afp element = factory.newMovePathElement(1, 2);
			assertNotNull(element);
			assertSame(PathElementType.MOVE_TO, element.getType());
			assertEpsilonEquals(0, element.getFromX());
			assertEpsilonEquals(0, element.getFromY());
			assertEpsilonEquals(0, element.getCtrlX1());
			assertEpsilonEquals(0, element.getCtrlY1());
			assertEpsilonEquals(0, element.getCtrlX2());
			assertEpsilonEquals(0, element.getCtrlY2());
			assertEpsilonEquals(1, element.getToX());
			assertEpsilonEquals(2, element.getToY());
		}
		
		@DisplayName("Line")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void newLinePathElement(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathElement2afp element = factory.newLinePathElement(1, 2, 3, 4);
			assertNotNull(element);
			assertSame(PathElementType.LINE_TO, element.getType());
			assertEpsilonEquals(1, element.getFromX());
			assertEpsilonEquals(2, element.getFromY());
			assertEpsilonEquals(0, element.getCtrlX1());
			assertEpsilonEquals(0, element.getCtrlY1());
			assertEpsilonEquals(0, element.getCtrlX2());
			assertEpsilonEquals(0, element.getCtrlY2());
			assertEpsilonEquals(3, element.getToX());
			assertEpsilonEquals(4, element.getToY());
		}
	
		@DisplayName("Close")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void newClosePathElement(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathElement2afp element = factory.newClosePathElement(1, 2, 3, 4);
			assertNotNull(element);
			assertSame(PathElementType.CLOSE, element.getType());
			assertEpsilonEquals(1, element.getFromX());
			assertEpsilonEquals(2, element.getFromY());
			assertEpsilonEquals(0, element.getCtrlX1());
			assertEpsilonEquals(0, element.getCtrlY1());
			assertEpsilonEquals(0, element.getCtrlX2());
			assertEpsilonEquals(0, element.getCtrlY2());
			assertEpsilonEquals(3, element.getToX());
			assertEpsilonEquals(4, element.getToY());
		}
	
		@DisplayName("Quad")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void newCurvePathElement_quad(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathElement2afp element = factory.newCurvePathElement(1, 2, 3, 4, 5, 6);
			assertNotNull(element);
			assertSame(PathElementType.QUAD_TO, element.getType());
			assertEpsilonEquals(1, element.getFromX());
			assertEpsilonEquals(2, element.getFromY());
			assertEpsilonEquals(3, element.getCtrlX1());
			assertEpsilonEquals(4, element.getCtrlY1());
			assertEpsilonEquals(0, element.getCtrlX2());
			assertEpsilonEquals(0, element.getCtrlY2());
			assertEpsilonEquals(5, element.getToX());
			assertEpsilonEquals(6, element.getToY());
		}
	
		@DisplayName("Curve")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void newCurvePathElement_curve(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathElement2afp element = factory.newCurvePathElement(1, 2, 3, 4, 5, 6, 7, 8);
			assertNotNull(element);
			assertSame(PathElementType.CURVE_TO, element.getType());
			assertEpsilonEquals(1, element.getFromX());
			assertEpsilonEquals(2, element.getFromY());
			assertEpsilonEquals(3, element.getCtrlX1());
			assertEpsilonEquals(4, element.getCtrlY1());
			assertEpsilonEquals(5, element.getCtrlX2());
			assertEpsilonEquals(6, element.getCtrlY2());
			assertEpsilonEquals(7, element.getToX());
			assertEpsilonEquals(8, element.getToY());
		}
		
		@DisplayName("Arc")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void newArcPathElement(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathElement2afp element = factory.newArcPathElement(1, 2, 3, 4, 5, 6, 7, true, false);
			assertNotNull(element);
			assertSame(PathElementType.ARC_TO, element.getType());
			assertEpsilonEquals(1, element.getFromX());
			assertEpsilonEquals(2, element.getFromY());
			assertEpsilonEquals(3, element.getToX());
			assertEpsilonEquals(4, element.getToY());
			assertEpsilonEquals(5, element.getRadiusX());
			assertEpsilonEquals(6, element.getRadiusY());
			assertEpsilonEquals(7, element.getRotationX());
			assertTrue(element.getLargeArcFlag());
			assertFalse(element.getSweepFlag());
		}

	}

}