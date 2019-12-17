/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.test.geometry.d3.afp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.GeomFactory3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathElement3afp;
import org.arakhne.afc.math.geometry.d3.afp.RectangularPrism3afp;
import org.arakhne.afc.math.test.AbstractMathTestCase;
import org.arakhne.afc.math.test.geometry.Point3DStub;
import org.arakhne.afc.math.test.geometry.Vector3DStub;

@SuppressWarnings("all")
public abstract class AbstractGeomFactory3afpTest extends AbstractMathTestCase {

	private GeomFactory3afp<?, ?, ?, ?> factory;

	protected abstract GeomFactory3afp<?, ?, ?, ?> createFactory();
	
	protected abstract Point3D createPoint(double x, double y, double z);

	protected abstract Vector3D createVector(double x, double y, double z);

	@BeforeEach
	public void setUp() throws Exception {
		this.factory = createFactory();
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		this.factory = null;
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void convertToPointPoint3D_expectedPointType(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p = createPoint(45, 56, 72);
		Point3D p2 = this.factory.convertToPoint(p);
		assertSame(p, p2);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void convertToPointPoint3D_notExpectedPointType(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p = new Point3DStub(45, 56, 72);
		Point3D p2 = this.factory.convertToPoint(p);
		assertNotSame(p, p2);
		assertEquals(p, p2);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void convertToVectorPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p = new Point3DStub(45, 56, 72);
		Vector3D v = this.factory.convertToVector(p);
		assertNotSame(p, v);
		assertEquals(p, v);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void convertToPointVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D v = new Vector3DStub(45, 56, 72);
		Point3D p = this.factory.convertToPoint(v);
		assertNotSame(v, p);
		assertEquals(v, p);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void convertToVectorVector3D_expectedVectorType(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D v = createVector(45, 56, 72);
		Vector3D v2 = this.factory.convertToVector(v);
		assertSame(v, v2);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void convertToVectorVector2D_notExpectedVectorType(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D v = new Vector3DStub(45, 56, 72);
		Vector3D v2 = this.factory.convertToVector(v);
		assertNotSame(v, v2);
		assertEquals(v, v2);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void newPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p = this.factory.newPoint();
		assertNotNull(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());
		Point3D ref = createPoint(0, 0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void newPointIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p = this.factory.newPoint(15, 48, 6);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(48, p.iy());
		assertEquals(6, p.iz());
		Point3D ref = createPoint(0, 0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void newPointDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p = this.factory.newPoint(15.34, 48.56, 6.42);
		assertNotNull(p);
		assertEpsilonEquals(15.34, p.getX());
		assertEpsilonEquals(48.56, p.getY());
		assertEpsilonEquals(6.42, p.getZ());
		Point3D ref = createPoint(0, 0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void newVector(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D p = this.factory.newVector();
		assertNotNull(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		Vector3D ref = createVector(0, 0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void newVectorIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D p = this.factory.newVector(15, 48, 6);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(48, p.iy());
		assertEquals(6, p.iz());
		Vector3D ref = createVector(0, 0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void newVectorDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Vector3D p = this.factory.newVector(15.45, 48.67, 6.42);
		assertNotNull(p);
		assertEpsilonEquals(15.45, p.getX());
		assertEpsilonEquals(48.67, p.getY());
		assertEpsilonEquals(6.42, p.getZ());
		Vector3D ref = createVector(0, 0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void newPath_NONZERO(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp<?, ?, ?, ?, ?, ?> path = this.factory.newPath(PathWindingRule.NON_ZERO);
		assertNotNull(path);
		assertSame(PathWindingRule.NON_ZERO, path.getWindingRule());
		assertEquals(0, path.size());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void newPath_EVENODD(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp<?, ?, ?, ?, ?, ?> path = this.factory.newPath(PathWindingRule.EVEN_ODD);
		assertNotNull(path);
		assertSame(PathWindingRule.EVEN_ODD, path.getWindingRule());
		assertEquals(0, path.size());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void newBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		RectangularPrism3afp<?, ?, ?, ?, ?, ?> r = this.factory.newBox();
		assertNotNull(r);
		assertEpsilonEquals(0, r.getMinX());
		assertEpsilonEquals(0, r.getMinY());
		assertEpsilonEquals(0, r.getMinZ());
		assertEpsilonEquals(0, r.getMaxX());
		assertEpsilonEquals(0, r.getMaxY());
		assertEpsilonEquals(0, r.getMaxZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void newBoxNumberNumberNumberNumber(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		RectangularPrism3afp<?, ?, ?, ?, ?, ?> r = this.factory.newBox(1, 2, 3, 4, 5, 6);
		assertNotNull(r);
		assertEpsilonEquals(1, r.getMinX());
		assertEpsilonEquals(2, r.getMinY());
		assertEpsilonEquals(3, r.getMinZ());
		assertEpsilonEquals(5, r.getMaxX());
		assertEpsilonEquals(7, r.getMaxY());
		assertEpsilonEquals(9, r.getMaxZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void newMovePathElement(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathElement3afp element = this.factory.newMovePathElement(1, 2, 3);
		assertNotNull(element);
		assertSame(PathElementType.MOVE_TO, element.getType());
		assertEpsilonEquals(0, element.getFromX());
		assertEpsilonEquals(0, element.getFromY());
		assertEpsilonEquals(0, element.getFromZ());
		assertEpsilonEquals(0, element.getCtrlX1());
		assertEpsilonEquals(0, element.getCtrlY1());
		assertEpsilonEquals(0, element.getCtrlZ1());
		assertEpsilonEquals(0, element.getCtrlX2());
		assertEpsilonEquals(0, element.getCtrlY2());
		assertEpsilonEquals(0, element.getCtrlZ2());
		assertEpsilonEquals(1, element.getToX());
		assertEpsilonEquals(2, element.getToY());
		assertEpsilonEquals(3, element.getToZ());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void newLinePathElement(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathElement3afp element = this.factory.newLinePathElement(1, 2, 3, 4, 5, 6);
		assertNotNull(element);
		assertSame(PathElementType.LINE_TO, element.getType());
		assertEpsilonEquals(1, element.getFromX());
		assertEpsilonEquals(2, element.getFromY());
		assertEpsilonEquals(3, element.getFromZ());
		assertEpsilonEquals(0, element.getCtrlX1());
		assertEpsilonEquals(0, element.getCtrlY1());
		assertEpsilonEquals(0, element.getCtrlZ1());
		assertEpsilonEquals(0, element.getCtrlX2());
		assertEpsilonEquals(0, element.getCtrlY2());
		assertEpsilonEquals(0, element.getCtrlZ2());
		assertEpsilonEquals(4, element.getToX());
		assertEpsilonEquals(5, element.getToY());
		assertEpsilonEquals(6, element.getToZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void newClosePathElement(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathElement3afp element = this.factory.newClosePathElement(1, 2, 3, 4, 5, 6);
		assertNotNull(element);
		assertSame(PathElementType.CLOSE, element.getType());
		assertEpsilonEquals(1, element.getFromX());
		assertEpsilonEquals(2, element.getFromY());
		assertEpsilonEquals(3, element.getFromZ());
		assertEpsilonEquals(0, element.getCtrlX1());
		assertEpsilonEquals(0, element.getCtrlY1());
		assertEpsilonEquals(0, element.getCtrlZ1());
		assertEpsilonEquals(0, element.getCtrlX2());
		assertEpsilonEquals(0, element.getCtrlY2());
		assertEpsilonEquals(0, element.getCtrlZ2());
		assertEpsilonEquals(4, element.getToX());
		assertEpsilonEquals(5, element.getToY());
		assertEpsilonEquals(6, element.getToZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void newCurvePathElement_quad(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathElement3afp element = this.factory.newCurvePathElement(1, 2, 3, 4, 5, 6, 7, 8, 9);
		assertNotNull(element);
		assertSame(PathElementType.QUAD_TO, element.getType());
		assertEpsilonEquals(1, element.getFromX());
		assertEpsilonEquals(2, element.getFromY());
		assertEpsilonEquals(3, element.getFromZ());
		assertEpsilonEquals(4, element.getCtrlX1());
		assertEpsilonEquals(5, element.getCtrlY1());
		assertEpsilonEquals(6, element.getCtrlZ1());
		assertEpsilonEquals(0, element.getCtrlX2());
		assertEpsilonEquals(0, element.getCtrlY2());
		assertEpsilonEquals(0, element.getCtrlZ2());
		assertEpsilonEquals(7, element.getToX());
		assertEpsilonEquals(8, element.getToY());
		assertEpsilonEquals(9, element.getToZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void newCurvePathElement_curve(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathElement3afp element = this.factory.newCurvePathElement(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
		assertNotNull(element);
		assertSame(PathElementType.CURVE_TO, element.getType());
		assertEpsilonEquals(1, element.getFromX());
		assertEpsilonEquals(2, element.getFromY());
		assertEpsilonEquals(3, element.getFromZ());
		assertEpsilonEquals(4, element.getCtrlX1());
		assertEpsilonEquals(5, element.getCtrlY1());
		assertEpsilonEquals(6, element.getCtrlZ1());
		assertEpsilonEquals(7, element.getCtrlX2());
		assertEpsilonEquals(8, element.getCtrlY2());
		assertEpsilonEquals(9, element.getCtrlZ2());
		assertEpsilonEquals(10, element.getToX());
		assertEpsilonEquals(11, element.getToY());
		assertEpsilonEquals(12, element.getToZ());
	}

}
