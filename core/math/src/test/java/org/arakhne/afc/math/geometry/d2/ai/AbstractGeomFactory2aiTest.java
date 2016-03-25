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
package org.arakhne.afc.math.geometry.d2.ai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
public abstract class AbstractGeomFactory2aiTest extends AbstractMathTestCase {

	private GeomFactory2ai<?, ?, ?> factory;
	
	@Rule
	public CoordinateSystem2DTestRule csTestRule = new CoordinateSystem2DTestRule();

	protected abstract GeomFactory2ai<?, ?, ?> createFactory();
	
	protected abstract Point2D createPoint(int x, int y);

	protected abstract Vector2D createVector(int x, int y);

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
		Path2ai<?, ?, ?, ?, ?> path = this.factory.newPath(PathWindingRule.NON_ZERO);
		assertNotNull(path);
		assertSame(PathWindingRule.NON_ZERO, path.getWindingRule());
		assertEquals(0, path.size());
	}
	
	@Test
	public void newPath_EVENODD() {
		Path2ai<?, ?, ?, ?, ?> path = this.factory.newPath(PathWindingRule.EVEN_ODD);
		assertNotNull(path);
		assertSame(PathWindingRule.EVEN_ODD, path.getWindingRule());
		assertEquals(0, path.size());
	}

	@Test
	public void newSegment() {
		Segment2ai<?, ?, ?, ?, ?> s = this.factory.newSegment(1,  2,  3,  4);
		assertNotNull(s);
		assertEquals(1, s.getX1());
		assertEquals(2, s.getY1());
		assertEquals(3, s.getX2());
		assertEquals(4, s.getY2());
	}
	
	@Test
	public void newBox() {
		Rectangle2ai<?, ?, ?, ?, ?> r = this.factory.newBox();
		assertNotNull(r);
		assertEquals(0, r.getMinX());
		assertEquals(0, r.getMinY());
		assertEquals(0, r.getMaxX());
		assertEquals(0, r.getMaxY());
	}

	@Test
	public void newBoxIntIntIntInt() {
		Rectangle2ai<?, ?, ?, ?, ?> r = this.factory.newBox(1, 2, 3, 4);
		assertNotNull(r);
		assertEquals(1, r.getMinX());
		assertEquals(2, r.getMinY());
		assertEquals(4, r.getMaxX());
		assertEquals(6, r.getMaxY());
	}

	@Test
	public void newMovePathElement() {
		PathElement2ai element = this.factory.newMovePathElement(1, 2);
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
	
	@Test
	public void newLinePathElement() {
		PathElement2ai element = this.factory.newLinePathElement(1, 2, 3, 4);
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

	@Test
	public void newClosePathElement() {
		PathElement2ai element = this.factory.newClosePathElement(1, 2, 3, 4);
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

	@Test
	public void newCurvePathElement_quad() {
		PathElement2ai element = this.factory.newCurvePathElement(1, 2, 3, 4, 5, 6);
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

	@Test
	public void newCurvePathElement_curve() {
		PathElement2ai element = this.factory.newCurvePathElement(1, 2, 3, 4, 5, 6, 7, 8);
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