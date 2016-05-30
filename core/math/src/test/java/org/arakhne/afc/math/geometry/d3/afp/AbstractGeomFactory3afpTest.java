/* 
 * $Id$
 * 
 * Copyright (C) 2011 Janus Core Developers
 * Copyright (C) 2012-13 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.d3.afp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3DTestRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Point3DStub;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.Vector3DStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractGeomFactory3afpTest extends AbstractMathTestCase {

	private GeomFactory3afp<?, ?, ?, ?> factory;
	
	@Rule
	public CoordinateSystem3DTestRule csTestRule = new CoordinateSystem3DTestRule();

	protected abstract GeomFactory3afp<?, ?, ?, ?> createFactory();
	
	protected abstract Point3D createPoint(double x, double y, double z);

	protected abstract Vector3D createVector(double x, double y, double z);

	@Before
	public void setUp() throws Exception {
		this.factory = createFactory();
	}
	
	@After
	public void tearDown() throws Exception {
		this.factory = null;
	}
	
	@Test
	public void convertToPointPoint3D_expectedPointType() {
		Point3D p = createPoint(45, 56, 72);
		Point3D p2 = this.factory.convertToPoint(p);
		assertSame(p, p2);
	}
	
	@Test
	public void convertToPointPoint3D_notExpectedPointType() {
		Point3D p = new Point3DStub(45, 56, 72);
		Point3D p2 = this.factory.convertToPoint(p);
		assertNotSame(p, p2);
		assertEquals(p, p2);
	}

	@Test
	public void convertToVectorPoint3D() {
		Point3D p = new Point3DStub(45, 56, 72);
		Vector3D v = this.factory.convertToVector(p);
		assertNotSame(p, v);
		assertEquals(p, v);
	}

	@Test
	public void convertToPointVector3D() {
		Vector3D v = new Vector3DStub(45, 56, 72);
		Point3D p = this.factory.convertToPoint(v);
		assertNotSame(v, p);
		assertEquals(v, p);
	}

	@Test
	public void convertToVectorVector3D_expectedVectorType() {
		Vector3D v = createVector(45, 56, 72);
		Vector3D v2 = this.factory.convertToVector(v);
		assertSame(v, v2);
	}
	
	@Test
	public void convertToVectorVector2D_notExpectedVectorType() {
		Vector3D v = new Vector3DStub(45, 56, 72);
		Vector3D v2 = this.factory.convertToVector(v);
		assertNotSame(v, v2);
		assertEquals(v, v2);
	}

	@Test
	public void newPoint() {
		Point3D p = this.factory.newPoint();
		assertNotNull(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());
		Point3D ref = createPoint(0, 0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@Test
	public void newPointIntIntInt() {
		Point3D p = this.factory.newPoint(15, 48, 6);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(48, p.iy());
		assertEquals(6, p.iz());
		Point3D ref = createPoint(0, 0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@Test
	public void newPointDoubleDoubleDouble() {
		Point3D p = this.factory.newPoint(15.34, 48.56, 6.42);
		assertNotNull(p);
		assertEpsilonEquals(15.34, p.getX());
		assertEpsilonEquals(48.56, p.getY());
		assertEpsilonEquals(48.56, p.getZ());
		Point3D ref = createPoint(0, 0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@Test
	public void newVector() {
		Vector3D p = this.factory.newVector();
		assertNotNull(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		Vector3D ref = createVector(0, 0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@Test
	public void newVectorIntIntInt() {
		Vector3D p = this.factory.newVector(15, 48, 6);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(48, p.iy());
		assertEquals(6, p.iz());
		Vector3D ref = createVector(0, 0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@Test
	public void newVectorDoubleDouble() {
		Vector3D p = this.factory.newVector(15.45, 48.67, 6.42);
		assertNotNull(p);
		assertEpsilonEquals(15.45, p.getX());
		assertEpsilonEquals(48.67, p.getY());
		assertEpsilonEquals(6.42, p.getZ());
		Vector3D ref = createVector(0, 0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@Test
	public void newPath_NONZERO() {
		Path3afp<?, ?, ?, ?, ?, ?> path = this.factory.newPath(PathWindingRule.NON_ZERO);
		assertNotNull(path);
		assertSame(PathWindingRule.NON_ZERO, path.getWindingRule());
		assertEquals(0, path.size());
	}
	
	@Test
	public void newPath_EVENODD() {
		Path3afp<?, ?, ?, ?, ?, ?> path = this.factory.newPath(PathWindingRule.EVEN_ODD);
		assertNotNull(path);
		assertSame(PathWindingRule.EVEN_ODD, path.getWindingRule());
		assertEquals(0, path.size());
	}

	@Test
	public void newBox() {
		RectangularPrism3afp<?, ?, ?, ?, ?, ?> r = this.factory.newBox();
		assertNotNull(r);
		assertEpsilonEquals(0, r.getMinX());
		assertEpsilonEquals(0, r.getMinY());
		assertEpsilonEquals(0, r.getMinZ());
		assertEpsilonEquals(0, r.getMaxX());
		assertEpsilonEquals(0, r.getMaxY());
		assertEpsilonEquals(0, r.getMaxZ());
	}

	@Test
	public void newBoxNumberNumberNumberNumber() {
		RectangularPrism3afp<?, ?, ?, ?, ?, ?> r = this.factory.newBox(1, 2, 3, 4, 5, 6);
		assertNotNull(r);
		assertEpsilonEquals(1, r.getMinX());
		assertEpsilonEquals(2, r.getMinY());
		assertEpsilonEquals(3, r.getMinZ());
		assertEpsilonEquals(5, r.getMaxX());
		assertEpsilonEquals(7, r.getMaxY());
		assertEpsilonEquals(9, r.getMaxZ());
	}

	@Test
	public void newMovePathElement() {
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
		assertEpsilonEquals(2, element.getToZ());
	}
	
	@Test
	public void newLinePathElement() {
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

	@Test
	public void newClosePathElement() {
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

	@Test
	public void newCurvePathElement_quad() {
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

	@Test
	public void newCurvePathElement_curve() {
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