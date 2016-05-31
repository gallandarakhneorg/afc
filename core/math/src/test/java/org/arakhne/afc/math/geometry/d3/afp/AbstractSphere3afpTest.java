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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractSphere3afpTest<T extends Sphere3afp<?, T, ?, ?, ?, B>,
		B extends RectangularPrism3afp<?, ?, ?, ?, ?, B>> extends AbstractShape3afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createSphere(5, 8, 2, 5);
	}
	
	@Test
	@Override
	public void testClone() {
		T clone = this.shape.clone();
		assertNotNull(clone);
		assertNotSame(this.shape, clone);
		assertEquals(this.shape.getClass(), clone.getClass());
		assertEpsilonEquals(5, clone.getX());
		assertEpsilonEquals(8, clone.getY());
		assertEpsilonEquals(5, clone.getRadius());
	}

	@Test
	@Override
	public void equalsObject() {
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createSphere(0, 0, 5)));
		assertFalse(this.shape.equals(createSphere(5, 8, 6)));
		assertFalse(this.shape.equals(createSegment(5, 8, 6, 10)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createSphere(5, 8, 5)));
	}

	@Test
	@Override
	public void equalsObject_withPathIterator() {
		assertFalse(this.shape.equals(createSphere(0, 0, 5).getPathIterator()));
		assertFalse(this.shape.equals(createSphere(5, 8, 6).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 6, 10).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createSphere(5, 8, 5).getPathIterator()));
	}

	@Test
	@Override
	public void equalsToPathIterator() {
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createSphere(0, 0, 5).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSphere(5, 8, 6).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 6, 10).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createSphere(5, 8, 5).getPathIterator()));
	}

	@Test
	@Override
	public void equalsToShape() {
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createSphere(0, 0, 5)));
		assertFalse(this.shape.equalsToShape((T) createSphere(5, 8, 6)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createSphere(5, 8, 5)));
	}

	@Test
	@Override
	public void isEmpty() {
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@Test
	@Override
	public void clear() {
		this.shape.clear();
		assertEpsilonEquals(0, this.shape.getX());
		assertEpsilonEquals(0, this.shape.getY());
		assertEpsilonEquals(0, this.shape.getRadius());
	}

	@Test
	@Override
	public void containsDoubleDouble() {
		assertFalse(this.shape.contains(0,0));
		assertFalse(this.shape.contains(11,10));
		assertFalse(this.shape.contains(11,50));
		assertFalse(this.shape.contains(9,12));
		assertTrue(this.shape.contains(9,11));
		assertTrue(this.shape.contains(8,12));
		assertTrue(this.shape.contains(3,7));
		assertFalse(this.shape.contains(10,11));
		assertTrue(this.shape.contains(9,10));
		
		this.shape = (T) createSphere(-1,-1,1);
		assertFalse(this.shape.contains(0,0));
	}

	@Test
	@Override
	public void containsPoint3D() {
		assertFalse(this.shape.contains(createPoint(0,0)));
		assertFalse(this.shape.contains(createPoint(11,10)));
		assertFalse(this.shape.contains(createPoint(11,50)));
		assertFalse(this.shape.contains(createPoint(9,12)));
		assertTrue(this.shape.contains(createPoint(9,11)));
		assertTrue(this.shape.contains(createPoint(8,12)));
		assertTrue(this.shape.contains(createPoint(3,7)));
		assertFalse(this.shape.contains(createPoint(10,11)));
		assertTrue(this.shape.contains(createPoint(9,10)));
		
		this.shape = (T) createSphere(-1,-1,1);
		assertFalse(this.shape.contains(createPoint(0,0)));
	}

	@Test
	@Override
	public void getClosestPointTo() {
		Point3D p;
		
		p = this.shape.getClosestPointTo(createPoint(5,8));
		assertNotNull(p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		
		p = this.shape.getClosestPointTo(createPoint(10,10));
		assertNotNull(p);
		assertEpsilonEquals(9.6424, p.getX());
		assertEpsilonEquals(9.8570, p.getY());
		
		p = this.shape.getClosestPointTo(createPoint(4,8));
		assertNotNull(p);
		assertEpsilonEquals(4, p.getX());
		assertEpsilonEquals(8, p.getY());
		
		p = this.shape.getClosestPointTo(createPoint(0,0));
		assertNotNull(p);
		assertEpsilonEquals(2.35, p.getX());
		assertEpsilonEquals(3.76, p.getY());

		p = this.shape.getClosestPointTo(createPoint(5,14));
		assertNotNull(p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(13, p.getY());
	}

	@Override
	public void getFarthestPointTo() {
		Point3D p;
		
		p = this.shape.getFarthestPointTo(createPoint(0, 0));
		assertEpsilonEquals(7.65, p.getX());
		assertEpsilonEquals(12.24, p.getY());
		
		p = this.shape.getFarthestPointTo(createPoint(.5, .1));
		assertEpsilonEquals(7.4748, p.getX());
		assertEpsilonEquals(12.3446, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(-1.2,-3.4));
		assertEpsilonEquals(7.3889, p.getX());
		assertEpsilonEquals(12.3924, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(-1.2,5.6));
		assertEpsilonEquals(9.6628, p.getX());
		assertEpsilonEquals(9.8050, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(7.6,5.6));
		assertEpsilonEquals(1.326, p.getX());
		assertEpsilonEquals(11.3914, p.getY());
	}

	@Override
	public void getDistance() {
		double d;
		d = this.shape.getDistance(createPoint(.5,.5));
		assertEpsilonEquals(3.74643,d);

		d = this.shape.getDistance(createPoint(-1.2,-3.4));
		assertEpsilonEquals(7.9769,d);

		d = this.shape.getDistance(createPoint(-1.2,5.6));
		assertEpsilonEquals(1.6483,d);

		d = this.shape.getDistance(createPoint(7.6,5.6));
		assertEpsilonEquals(0,d);
	}

	@Override
	public void getDistanceSquared() {
		double d;
		d = this.shape.getDistanceSquared(createPoint(.5,.5));
		assertEpsilonEquals(14.03572,d);

		d = this.shape.getDistanceSquared(createPoint(-1.2,-3.4));
		assertEpsilonEquals(63.631,d);

		d = this.shape.getDistanceSquared(createPoint(-1.2,5.6));
		assertEpsilonEquals(2.7169,d);

		d = this.shape.getDistanceSquared(createPoint(7.6,5.6));
		assertEpsilonEquals(0,d);
	}

	@Override
	public void getDistanceL1() {
		double d;
		d = this.shape.getDistanceL1(createPoint(.5,.5));
		assertEpsilonEquals(5.14005,d);

		d = this.shape.getDistanceL1(createPoint(-1.2,-3.4));
		assertEpsilonEquals(10.81872,d);

		d = this.shape.getDistanceL1(createPoint(-1.2,5.6));
		assertEpsilonEquals(2.1322,d);

		d = this.shape.getDistanceL1(createPoint(7.6,5.6));
		assertEpsilonEquals(0,d);
	}

	@Override
	public void getDistanceLinf() {
		double d;
		d = this.shape.getDistanceLinf(createPoint(.5,.5));
		assertEpsilonEquals(3.2125,d);

		d = this.shape.getDistanceLinf(createPoint(-1.2,-3.4));
		assertEpsilonEquals(7.0076,d);

		d = this.shape.getDistanceLinf(createPoint(-1.2,5.6));
		assertEpsilonEquals(1.53716,d);

		d = this.shape.getDistanceLinf(createPoint(7.6,5.6));
		assertEpsilonEquals(0,d);
	}

	@Override
	public void setIT() {
		this.shape.set((T) createSphere(17, 20, 7));
		assertEpsilonEquals(17, this.shape.getX());
		assertEpsilonEquals(20, this.shape.getY());
		assertEpsilonEquals(7, this.shape.getRadius());
	}

	@Override
	public void translateDoubleDouble() {
		this.shape.translate(123.456, -789.123);
		assertEpsilonEquals(128.456, this.shape.getX());
		assertEpsilonEquals(-781.123, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Override
	public void translateVector3D() {
		this.shape.translate(createVector(123.456, -789.123));
		assertEpsilonEquals(128.456, this.shape.getX());
		assertEpsilonEquals(-781.123, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Override
	public void toBoundingBox() {
		B box = this.shape.toBoundingBox();
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(3, box.getMinY());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(13, box.getMaxY());
	}

	@Override
	public void toBoundingBoxB() {
		B box = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(3, box.getMinY());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(13, box.getMaxY());
	}

	@Override
	public void getPathIteratorTransform3D() {
		PathIterator3afp pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 10, 8);
		assertElement(pi, PathElementType.CURVE_TO, 10, 10.76142374915397, 7.761423749153966, 13, 5, 13);
		assertElement(pi, PathElementType.CURVE_TO, 2.238576250846033, 13, 0, 10.76142374915397, 0, 8);
		assertElement(pi, PathElementType.CURVE_TO, 0, 5.238576250846034, 2.238576250846033, 3, 5, 3);
		assertElement(pi, PathElementType.CURVE_TO, 7.761423749153966, 3, 10, 5.238576250846034, 10, 8);
		assertElement(pi, PathElementType.CLOSE, 10, 8);
		assertNoElement(pi);

		Transform3D tr;
		
		tr = new Transform3D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 10, 8);
		assertElement(pi, PathElementType.CURVE_TO, 10, 10.76142374915397, 7.761423749153966, 13, 5, 13);
		assertElement(pi, PathElementType.CURVE_TO, 2.238576250846033, 13, 0, 10.76142374915397, 0, 8);
		assertElement(pi, PathElementType.CURVE_TO, 0, 5.238576250846034, 2.238576250846033, 3, 5, 3);
		assertElement(pi, PathElementType.CURVE_TO, 7.761423749153966, 3, 10, 5.238576250846034, 10, 8);
		assertElement(pi, PathElementType.CLOSE, 10, 8);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(10, -10);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 20, -2);
		assertElement(pi, PathElementType.CURVE_TO, 20, 0.76142374915397, 17.761423749153966, 3, 15, 3);
		assertElement(pi, PathElementType.CURVE_TO, 12.238576250846033, 3, 10, 0.76142374915397, 10, -2);
		assertElement(pi, PathElementType.CURVE_TO, 10, -4.761423749153966, 12.238576250846033, -7, 15, -7);
		assertElement(pi, PathElementType.CURVE_TO, 17.761423749153966, -7, 20, -4.761423749153966, 20, -2);
		assertElement(pi, PathElementType.CLOSE, 20, -2);
		assertNoElement(pi);
	}

	@Override
	public void getPathIterator() {
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 10, 8);
		assertElement(pi, PathElementType.CURVE_TO, 10, 10.76142374915397, 7.761423749153966, 13, 5, 13);
		assertElement(pi, PathElementType.CURVE_TO, 2.238576250846033, 13, 0, 10.76142374915397, 0, 8);
		assertElement(pi, PathElementType.CURVE_TO, 0, 5.238576250846034, 2.238576250846033, 3, 5, 3);
		assertElement(pi, PathElementType.CURVE_TO, 7.761423749153966, 3, 10, 5.238576250846034, 10, 8);
		assertElement(pi, PathElementType.CLOSE, 10, 8);
		assertNoElement(pi);
	}

	@Override
	public void createTransformedShape() {
		Transform3D tr;
		Shape3afp newShape;
		
		newShape = this.shape.createTransformedShape(null);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform3D();
		newShape = this.shape.createTransformedShape(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform3D();
		tr.makeTranslationMatrix(10, -10);
		newShape = this.shape.createTransformedShape(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertTrue(newShape instanceof Path3afp);
		PathIterator3afp pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 20, -2);
		assertElement(pi, PathElementType.CURVE_TO, 20, 0.76142374915397, 17.761423749153966, 3, 15, 3);
		assertElement(pi, PathElementType.CURVE_TO, 12.238576250846033, 3, 10, 0.76142374915397, 10, -2);
		assertElement(pi, PathElementType.CURVE_TO, 10, -4.761423749153966, 12.238576250846033, -7, 15, -7);
		assertElement(pi, PathElementType.CURVE_TO, 17.761423749153966, -7, 20, -4.761423749153966, 20, -2);
		assertElement(pi, PathElementType.CLOSE, 20, -2);
		assertNoElement(pi);
	}

	@Override
	public void containsRectangularPrism3afp() {
		assertFalse(this.shape.contains(createRectangle(-4, -4, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(-5, -5, 10, 10)));
		assertFalse(this.shape.contains(createRectangle(-5, -5, 5.5, 5.5)));
		assertFalse(this.shape.contains(createRectangle(-5, -4, 5.5, 1)));
		assertFalse(this.shape.contains(createRectangle(20, .5, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(-5, -5, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(-1, -100, 1, 200)));
		assertFalse(this.shape.contains(createRectangle(-1, -100, 1.0001, 200)));
		assertFalse(this.shape.contains(createRectangle(-1, 2, 1.0001, 1.0001)));
		assertTrue(this.shape.contains(createRectangle(2, 4, 6, 4)));
	}

	@Override
	public void intersectsRectangularPrism3afp() {
		assertFalse(this.shape.intersects(createRectangle(-4, -4, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(-5, -5, 10, 10)));
		assertFalse(this.shape.intersects(createRectangle(-5, -5, 5.5, 5.5)));
		assertFalse(this.shape.intersects(createRectangle(-5, -4, 5.5, 1)));
		assertFalse(this.shape.intersects(createRectangle(20, .5, 1, 1)));
		assertFalse(this.shape.intersects(createRectangle(-5, -5, 1, 1)));
		assertFalse(this.shape.intersects(createRectangle(-1, -100, 1, 200)));
		assertTrue(this.shape.intersects(createRectangle(-1, -100, 1.0001, 200)));
		assertFalse(this.shape.intersects(createRectangle(-1, 2, 1.0001, 1.0001)));
	}

	@Override
	public void intersectsSphere3afp() {
		assertTrue(this.shape.intersects(createSphere(10, 10, 1)));
		assertFalse(this.shape.intersects(createSphere(0, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(0, .5, 1)));
		assertFalse(this.shape.intersects(createSphere(.5, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(.5, .5, 1)));
		assertFalse(this.shape.intersects(createSphere(2, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(12, 8, 2)));
		assertTrue(this.shape.intersects(createSphere(12, 8, 2.1)));
	}

	@Override
	public void intersectsSegment3afp() {
		assertTrue(this.shape.intersects(createSegment(2, 10, 6, 5)));
		assertTrue(this.shape.intersects(createSegment(2, 10, 8, 14)));
		assertTrue(this.shape.intersects(createSegment(0, 4, 8, 14)));
		assertFalse(this.shape.intersects(createSegment(0, 4, 0, 6)));
		assertFalse(this.shape.intersects(createSegment(0, 4, 0, 12)));
		assertFalse(this.shape.intersects(createSegment(5, 0, 0, 6)));
	}

	@Override
	public void intersectsPath3afp() {
		Path3afp path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, -2);
		path.lineTo(-2, 2);
		path.lineTo(2, 2);
		path.lineTo(2, -2);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));
		
		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 8);
		path.lineTo(0, 14);
		path.lineTo(10, 14);
		path.lineTo(10, 8);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 2);
		path.lineTo(12, 14);
		path.lineTo(0, 14);
		assertTrue(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, 2);
		path.lineTo(-2, 14);
		path.lineTo(12, 14);
		path.lineTo(12, 2);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 0);
		path.lineTo(0, 4);
		path.lineTo(14, 0);
		path.lineTo(14, 4);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-8, -7);
		path.lineTo(24, 14);
		path.lineTo(-16, 14);
		path.lineTo(20, -7);
		path.lineTo(5, 21);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		// EVENB ODD
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, -2);
		path.lineTo(-2, 2);
		path.lineTo(2, 2);
		path.lineTo(2, -2);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 8);
		path.lineTo(0, 14);
		path.lineTo(10, 14);
		path.lineTo(10, 8);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 2);
		path.lineTo(12, 14);
		path.lineTo(0, 14);
		assertTrue(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2);
		path.lineTo(-2, 14);
		path.lineTo(12, 14);
		path.lineTo(12, 2);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 0);
		path.lineTo(0, 4);
		path.lineTo(14, 0);
		path.lineTo(14, 4);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-8, -7);
		path.lineTo(24, 14);
		path.lineTo(-16, 14);
		path.lineTo(20, -7);
		path.lineTo(5, 21);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));
	}

	@Override
	public void intersectsPathIterator3afp() {
		Path3afp path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, -2);
		path.lineTo(-2, 2);
		path.lineTo(2, 2);
		path.lineTo(2, -2);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		
		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 8);
		path.lineTo(0, 14);
		path.lineTo(10, 14);
		path.lineTo(10, 8);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 2);
		path.lineTo(12, 14);
		path.lineTo(0, 14);
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, 2);
		path.lineTo(-2, 14);
		path.lineTo(12, 14);
		path.lineTo(12, 2);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 0);
		path.lineTo(0, 4);
		path.lineTo(14, 0);
		path.lineTo(14, 4);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-8, -7);
		path.lineTo(24, 14);
		path.lineTo(-16, 14);
		path.lineTo(20, -7);
		path.lineTo(5, 21);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		// EVENB ODD
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, -2);
		path.lineTo(-2, 2);
		path.lineTo(2, 2);
		path.lineTo(2, -2);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 8);
		path.lineTo(0, 14);
		path.lineTo(10, 14);
		path.lineTo(10, 8);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 2);
		path.lineTo(12, 14);
		path.lineTo(0, 14);
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2);
		path.lineTo(-2, 14);
		path.lineTo(12, 14);
		path.lineTo(12, 2);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 0);
		path.lineTo(0, 4);
		path.lineTo(14, 0);
		path.lineTo(14, 4);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-8, -7);
		path.lineTo(24, 14);
		path.lineTo(-16, 14);
		path.lineTo(20, -7);
		path.lineTo(5, 21);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
	}

	@Test
	public void staticContainsCirclePoint() {
		assertTrue(Sphere3afp.containsCirclePoint(0, 0, 1, 0, 0));
		assertTrue(Sphere3afp.containsCirclePoint(0, 0, 1, 1, 0));
		assertTrue(Sphere3afp.containsCirclePoint(0, 0, 1, 0, 1));
		assertFalse(Sphere3afp.containsCirclePoint(0, 0, 1, 1, 1));
		assertFalse(Sphere3afp.containsCirclePoint(0, 0, 1, 1.1, 0));
		assertTrue(Sphere3afp.containsCirclePoint(5, 8, 1, 5, 8));
		assertTrue(Sphere3afp.containsCirclePoint(5, 8, 1, 6, 8));
		assertTrue(Sphere3afp.containsCirclePoint(5, 8, 1, 5, 9));
		assertFalse(Sphere3afp.containsCirclePoint(5, 8, 1, 6, 9));
		assertFalse(Sphere3afp.containsCirclePoint(5, 8, 1, 6.1, 8));
	}

	@Test
	public void staticContainsCircleRectangle() {
		assertTrue(Sphere3afp.containsCircleRectangle(0, 0, 1, 0, 0, .5, .5));
		assertFalse(Sphere3afp.containsCircleRectangle(0, 0, 1, 0, 0, 1, 1));
		assertFalse(Sphere3afp.containsCircleRectangle(0, 0, 1, 0, 0, .5, 1));
	}
	
	@Test
	public void staticIntersectsCircleCircle() {
		assertFalse(Sphere3afp.intersectsCircleCircle(
				0, 0, 1,
				10, 10, 1));
		assertTrue(Sphere3afp.intersectsCircleCircle(
				0, 0, 1,
				0, 0, 1));
		assertTrue(Sphere3afp.intersectsCircleCircle(
				0, 0, 1,
				0, .5, 1));
		assertTrue(Sphere3afp.intersectsCircleCircle(
				0, 0, 1,
				.5, 0, 1));
		assertTrue(Sphere3afp.intersectsCircleCircle(
				0, 0, 1,
				.5, .5, 1));
		assertFalse(Sphere3afp.intersectsCircleCircle(
				0, 0, 1,
				2, 0, 1));
	}
	
	@Test
	public void staticIntersectsCircleLine() {
		assertTrue(Sphere3afp.intersectsCircleLine(
				0, 0, 1,
				-5, -5, -4, -4));
		assertTrue(Sphere3afp.intersectsCircleLine(
				0, 0, 1,
				-5, -5, 5, 5));
		assertTrue(Sphere3afp.intersectsCircleLine(
				0, 0, 1,
				-5, -5, .5, .5));
		assertFalse(Sphere3afp.intersectsCircleLine(
				0, 0, 1,
				-5, -5, .5, -4));
		assertFalse(Sphere3afp.intersectsCircleLine(
				0, 0, 1,
				20, .5, 21, 1.5));
	}
	
	@Test
	public void staticIntersectsCircleRectangle() {
		assertFalse(Sphere3afp.intersectsCircleRectangle(
				0, 0, 1,
				-5, -5, -4, -4));
		assertTrue(Sphere3afp.intersectsCircleRectangle(
				0, 0, 1,
				-5, -5, 5, 5));
		assertTrue(Sphere3afp.intersectsCircleRectangle(
				0, 0, 1,
				-5, -5, .5, .5));
		assertFalse(Sphere3afp.intersectsCircleRectangle(
				0, 0, 1,
				-5, -5, .5, -4));
		assertFalse(Sphere3afp.intersectsCircleRectangle(
				0, 0, 1,
				20, .5, 21, 1.5));
	}
	
	@Test
	public void staticIntersectsCircleSegment() {
		assertFalse(Sphere3afp.intersectsCircleSegment(
				0, 0, 1,
				-5, -5, -4, -4));
		assertTrue(Sphere3afp.intersectsCircleSegment(
				0, 0, 1,
				-5, -5, 5, 5));
		assertTrue(Sphere3afp.intersectsCircleSegment(
				0, 0, 1,
				-5, -5, .5, .5));
		assertFalse(Sphere3afp.intersectsCircleSegment(
				0, 0, 1,
				-5, -5, .5, -4));
		assertFalse(Sphere3afp.intersectsCircleSegment(
				0, 0, 1,
				20, .5, 21, 1.5));
		assertTrue(Sphere3afp.intersectsCircleSegment(
				1, 1, 1,
				.5, -1, .5, 4));
	}
	
	@Test
	public void getX() {
		assertEpsilonEquals(5, this.shape.getX());
	}

	@Test
	public void getY() {
		assertEpsilonEquals(8, this.shape.getY());
	}

	@Test
	public void getCenter() {
		Point3D center = this.shape.getCenter();
		assertEpsilonEquals(5, center.getX());
		assertEpsilonEquals(8, center.getY());
	}

	@Test
	public void setCenterPoint3D() {
		this.shape.setCenter(createPoint(123.456, 789.123));
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(789.123, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Test
	public void setCenterDoubleDouble() {
		this.shape.setCenter(123.456, 789.123);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(789.123, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Test
	public void setX() {
		this.shape.setX(123.456);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Test
	public void setY() {
		this.shape.setY(123.456);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(123.456, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Test
	public void getRadius() {
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Test
	public void setRadius() {
		this.shape.setRadius(123.456);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(123.456, this.shape.getRadius());
	}

	@Test
	public void setDoubleDoubleDouble() {
		this.shape.set(123.456, 789.123, 456.789);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(789.123, this.shape.getY());
		assertEpsilonEquals(456.789, this.shape.getRadius());
	}

	@Test
	public void setPoint3DDouble() {
		this.shape.set(createPoint(123.456, 789.123), 456.789);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(789.123, this.shape.getY());
		assertEpsilonEquals(456.789, this.shape.getRadius());
	}

	@Override
	public void intersectsShape3D() {
		assertTrue(this.shape.intersects((Shape3D) createSphere(10, 10, 1)));
		assertTrue(this.shape.intersects((Shape3D) createEllipse(9, 9, 2, 2)));
	}

	@Override
	public void operator_addVector3D() {
		this.shape.operator_add(createVector(123.456, -789.123));
		assertEpsilonEquals(128.456, this.shape.getX());
		assertEpsilonEquals(-781.123, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Override
	public void operator_plusVector3D() {
		T shape = this.shape.operator_plus(createVector(123.456, -789.123));
		assertEpsilonEquals(128.456, shape.getX());
		assertEpsilonEquals(-781.123, shape.getY());
		assertEpsilonEquals(5, shape.getRadius());
	}

	@Override
	public void operator_removeVector3D() {
		this.shape.operator_remove(createVector(123.456, -789.123));
		assertEpsilonEquals(-118.456, this.shape.getX());
		assertEpsilonEquals(797.123, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Override
	public void operator_minusVector3D() {
		T shape = this.shape.operator_minus(createVector(123.456, -789.123));
		assertEpsilonEquals(-118.456, shape.getX());
		assertEpsilonEquals(797.123, shape.getY());
		assertEpsilonEquals(5, shape.getRadius());
	}

	@Override
	public void operator_multiplyTransform3D() {
		Transform3D tr;
		Shape3afp newShape;
		
		newShape = this.shape.operator_multiply(null);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform3D();
		newShape = this.shape.operator_multiply(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform3D();
		tr.makeTranslationMatrix(10, -10);
		newShape = this.shape.operator_multiply(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertTrue(newShape instanceof Path3afp);
		PathIterator3afp pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 20, -2);
		assertElement(pi, PathElementType.CURVE_TO, 20, 0.76142374915397, 17.761423749153966, 3, 15, 3);
		assertElement(pi, PathElementType.CURVE_TO, 12.238576250846033, 3, 10, 0.76142374915397, 10, -2);
		assertElement(pi, PathElementType.CURVE_TO, 10, -4.761423749153966, 12.238576250846033, -7, 15, -7);
		assertElement(pi, PathElementType.CURVE_TO, 17.761423749153966, -7, 20, -4.761423749153966, 20, -2);
		assertElement(pi, PathElementType.CLOSE, 20, -2);
		assertNoElement(pi);
	}

	@Override
	public void operator_andPoint3D() {
		assertFalse(this.shape.operator_and(createPoint(0,0)));
		assertFalse(this.shape.operator_and(createPoint(11,10)));
		assertFalse(this.shape.operator_and(createPoint(11,50)));
		assertFalse(this.shape.operator_and(createPoint(9,12)));
		assertTrue(this.shape.operator_and(createPoint(9,11)));
		assertTrue(this.shape.operator_and(createPoint(8,12)));
		assertTrue(this.shape.operator_and(createPoint(3,7)));
		assertFalse(this.shape.operator_and(createPoint(10,11)));
		assertTrue(this.shape.operator_and(createPoint(9,10)));
	}

	@Override
	public void operator_andShape3D() {
		assertTrue(this.shape.operator_and(createSphere(10, 10, 1)));
		assertTrue(this.shape.operator_and(createEllipse(9, 9, 2, 2)));
	}

	@Override
	public void operator_upToPoint3D() {
		assertEpsilonEquals(3.74643, this.shape.operator_upTo(createPoint(.5,.5)));
		assertEpsilonEquals(7.9769, this.shape.operator_upTo(createPoint(-1.2,-3.4)));
		assertEpsilonEquals(1.6483, this.shape.operator_upTo(createPoint(-1.2,5.6)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(7.6,5.6)));
	}

	@Test
	public void getHorizontalRadius() {
		assertEpsilonEquals(5, this.shape.getRadius());
	}
	
	@Test
	public void getVerticalRadius() {
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Test
	public void setFromCenterDoubleDoubleDoubleDouble() {
		this.shape.setFromCenter(152, 148, 475, -254);
		assertEpsilonEquals(152, this.shape.getX());
		assertEpsilonEquals(148, this.shape.getY());
		assertEpsilonEquals(323, this.shape.getRadius());
	}

	@Test
	public void setFromCornersDoubleDoubleDoubleDouble() {
		this.shape.setFromCorners(-171, 550, 475, -254);
		assertEpsilonEquals(152, this.shape.getX());
		assertEpsilonEquals(148, this.shape.getY());
		assertEpsilonEquals(323, this.shape.getRadius());
	}

	@Test
	public void getMinX() {
		assertEpsilonEquals(0, this.shape.getMinX());
	}

	@Test
	public void setMinX_noSwap() {
		this.shape.setMinX(-41);
		assertEpsilonEquals(-15.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(25.5, this.shape.getRadius());
	}

	@Test
	public void setMinX_swap() {
		this.shape.setMinX(41);
		assertEpsilonEquals(25.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(15.5, this.shape.getRadius());
	}

	@Test
	public void getMaxX() {
		assertEpsilonEquals(10, this.shape.getMaxX());
	}

	@Test
	public void setMaxX_noSwap() {
		this.shape.setMaxX(41);
		assertEpsilonEquals(20.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(20.5, this.shape.getRadius());
	}

	@Test
	public void setMaxX_swap() {
		this.shape.setMaxX(-41);
		assertEpsilonEquals(-20.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(20.5, this.shape.getRadius());
	}

	@Test
	public void getMinY() {
		assertEpsilonEquals(3, this.shape.getMinY());
	}

	@Test
	public void setMinY_noSwap() {
		this.shape.setMinY(-41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(-14, this.shape.getY());
		assertEpsilonEquals(27, this.shape.getRadius());
	}

	@Test
	public void setMinY_swap() {
		this.shape.setMinY(41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(27, this.shape.getY());
		assertEpsilonEquals(14, this.shape.getRadius());
	}

	@Test
	public void getMaxY() {
		assertEpsilonEquals(13, this.shape.getMaxY());
	}

	@Test
	public void setMaxY_noSwap() {
		this.shape.setMaxY(41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(22, this.shape.getY());
		assertEpsilonEquals(19, this.shape.getRadius());
	}

	@Test
	public void setMaxY_swap() {
		this.shape.setMaxY(-41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(-19, this.shape.getY());
		assertEpsilonEquals(22, this.shape.getRadius());
	}

}