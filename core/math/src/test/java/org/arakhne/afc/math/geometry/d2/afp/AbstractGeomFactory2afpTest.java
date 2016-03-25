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
package org.arakhne.afc.math.geometry.d2.afp;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.arakhne.afc.math.geometry.d2.ImmutablePoint2D;
import org.arakhne.afc.math.geometry.d2.ImmutableVector2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractGeomFactory2afpTest extends AbstractMathTestCase {

	private GeomFactory2afp<?, ?, ?> factory;
	
	@Rule
	public CoordinateSystem2DTestRule csTestRule = new CoordinateSystem2DTestRule();

	protected abstract GeomFactory2afp<?, ?, ?> createFactory();
	
	protected abstract Point2D createPoint(double x, double y);

	protected abstract Vector2D createVector(double x, double y);

	@Before
	public void setUp() throws Exception {
		this.factory = createFactory();
	}
	
	@After
	public void tearDown() throws Exception {
		this.factory = null;
	}
	
	@Test
	public void convertToPointPoint2D_expectedPointType() {
		Point2D p = createPoint(45, 56);
		Point2D p2 = this.factory.convertToPoint(p);
		assertSame(p, p2);
	}
	
	@Test
	public void convertToPointPoint2D_notExpectedPointType() {
		Point2D p = new ImmutablePoint2D(45, 56);
		Point2D p2 = this.factory.convertToPoint(p);
		assertNotSame(p, p2);
		assertEquals(p, p2);
	}

	@Test
	public void convertToVectorPoint2D() {
		Point2D p = new ImmutablePoint2D(45, 56);
		Vector2D v = this.factory.convertToVector(p);
		assertNotSame(p, v);
		assertEquals(p, v);
	}

	@Test
	public void convertToPointVector2D() {
		Vector2D v = new ImmutableVector2D(45, 56);
		Point2D p = this.factory.convertToPoint(v);
		assertNotSame(v, p);
		assertEquals(v, p);
	}

	@Test
	public void convertToVectorVector2D_expectedVectorType() {
		Vector2D v = createVector(45, 56);
		Vector2D v2 = this.factory.convertToVector(v);
		assertSame(v, v2);
	}
	
	@Test
	public void convertToVectorVector2D_notExpectedVectorType() {
		Vector2D v = new ImmutableVector2D(45, 56);
		Vector2D v2 = this.factory.convertToVector(v);
		assertNotSame(v, v2);
		assertEquals(v, v2);
	}

	@Test
	public void newPoint() {
		Point2D p = this.factory.newPoint();
		assertNotNull(p);
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());
		Point2D ref = createPoint(0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@Test
	public void newPointIntInt() {
		Point2D p = this.factory.newPoint(15, 48);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(48, p.iy());
		Point2D ref = createPoint(0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@Test
	public void newVector() {
		Vector2D p = this.factory.newVector();
		assertNotNull(p);
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());
		Vector2D ref = createVector(0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@Test
	public void newVectorIntInt() {
		Vector2D p = this.factory.newVector(15, 48);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(48, p.iy());
		Vector2D ref = createVector(0, 0);
		assertEquals(ref.getClass(), p.getClass());
	}

	@Test
	public void newPath_NONZERO() {
		Path2afp<?, ?, ?, ?, ?> path = this.factory.newPath(PathWindingRule.NON_ZERO);
		assertNotNull(path);
		assertSame(PathWindingRule.NON_ZERO, path.getWindingRule());
		assertEquals(0, path.size());
	}
	
	@Test
	public void newPath_EVENODD() {
		Path2afp<?, ?, ?, ?, ?> path = this.factory.newPath(PathWindingRule.EVEN_ODD);
		assertNotNull(path);
		assertSame(PathWindingRule.EVEN_ODD, path.getWindingRule());
		assertEquals(0, path.size());
	}

	@Test
	public void newBox() {
		Rectangle2afp<?, ?, ?, ?, ?> r = this.factory.newBox();
		assertNotNull(r);
		assertEpsilonEquals(0, r.getMinX());
		assertEpsilonEquals(0, r.getMinY());
		assertEpsilonEquals(0, r.getMaxX());
		assertEpsilonEquals(0, r.getMaxY());
	}

	@Test
	public void newBoxNumberNumberNumberNumber() {
		Rectangle2afp<?, ?, ?, ?, ?> r = this.factory.newBox(1, 2, 3, 4);
		assertNotNull(r);
		assertEpsilonEquals(1, r.getMinX());
		assertEpsilonEquals(2, r.getMinY());
		assertEpsilonEquals(4, r.getMaxX());
		assertEpsilonEquals(6, r.getMaxY());
	}

	@Test
	public void newMovePathElement() {
		PathElement2afp element = this.factory.newMovePathElement(1, 2);
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
	
	@Test
	public void newLinePathElement() {
		PathElement2afp element = this.factory.newLinePathElement(1, 2, 3, 4);
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

	@Test
	public void newClosePathElement() {
		PathElement2afp element = this.factory.newClosePathElement(1, 2, 3, 4);
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

	@Test
	public void newCurvePathElement_quad() {
		PathElement2afp element = this.factory.newCurvePathElement(1, 2, 3, 4, 5, 6);
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

	@Test
	public void newCurvePathElement_curve() {
		PathElement2afp element = this.factory.newCurvePathElement(1, 2, 3, 4, 5, 6, 7, 8);
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

	
}