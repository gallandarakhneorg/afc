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
package org.arakhne.afc.math.geometry.d3.ai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractSphere3aiTest<T extends Sphere3ai<?, T, ?, ?, ?, B>,
		B extends RectangularPrism3ai<?, ?, ?, ?, ?, B>> extends AbstractShape3aiTest<T, B> {

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
		assertEpsilonEquals(2, clone.getZ());
		assertEpsilonEquals(5, clone.getRadius());
	}
	
	@Test
	@Override
	public void equalsObject() {
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createSphere(0, 0, 0, 5)));
		assertFalse(this.shape.equals(createSphere(5, 8, 2, 6)));
		assertFalse(this.shape.equals(createSegment(5, 8, 2, 6, 3, 10)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createSphere(5, 8, 2, 5)));
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
	public void equalsToShape() {
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createSphere(0, 0, 5)));
		assertFalse(this.shape.equalsToShape((T) createSphere(5, 8, 6)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createSphere(5, 8, 5)));
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
	public void isEmpty() {
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@Test
	@Override
	public void clear() {
		this.shape.clear();
		assertEquals(0, this.shape.getX());
		assertEquals(0, this.shape.getY());
		assertEquals(0, this.shape.getZ());
		assertEquals(0, this.shape.getRadius());
	}

	@Test
	@Override
	public void translateIntInt() {
		this.shape.translate(3, 4, 2);
		assertEquals(8, this.shape.getX());
		assertEquals(12, this.shape.getY());
		assertEquals(4, this.shape.getZ());
		assertEquals(5, this.shape.getRadius());
	}

	@Test
	@Override
	public void toBoundingBox() {
		RectangularPrism3ai<?, ?, ?, ?, ?, ?> r1 = this.shape.toBoundingBox();
		assertEquals(0, r1.getMinX());
		assertEquals(3, r1.getMinY());
		assertEquals(-3, r1.getMinZ());
		assertEquals(10, r1.getMaxX());
		assertEquals(13, r1.getMaxY());
		assertEquals(7, r1.getMaxZ());
	}

	@Test
	public void getPointIterator() {
		Iterator<? extends Point3D> iterator = this.shape.getPointIterator();
		Point3D p;
		
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
			assertTrue("("+x+";"+y+")", iterator.hasNext()); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
			p = iterator.next();
			assertNotNull(p);
			assertEquals("(>"+x+"<;"+y+")!=("+p.ix()+";"+p.iy()+")", x, p.ix()); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
			assertEquals("("+x+";>"+y+"<)!=("+p.ix()+";"+p.iy()+")", y, p.iy()); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
		}
		
		assertFalse(iterator.hasNext());
	}

	@Test
	public void getPointIterator_small() {
		Sphere3ai<?, ?, ?, ?, ?, ?> circle = createSphere(4, 6, 3);
		Iterator<? extends Point3D> iterator = circle.getPointIterator();
		Point3D p;
		
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
			assertTrue("("+x+";"+y+")", iterator.hasNext()); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
			p = iterator.next();
			assertNotNull(p);
			assertEquals("(>"+x+"<;"+y+")!=("+p.ix()+";"+p.iy()+")", x, p.ix()); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
			assertEquals("("+x+";>"+y+"<)!=("+p.ix()+";"+p.iy()+")", y, p.iy()); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
		}
		
		assertFalse(iterator.hasNext());
	}

	@Test
	public void getClosestPointTo() {
		Point3D p;
		
		p = this.shape.getClosestPointTo(createPoint(5,8));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = this.shape.getClosestPointTo(createPoint(10,10));
		assertNotNull(p);
		assertEquals(10, p.ix());
		assertEquals(10, p.iy());
		
		p = this.shape.getClosestPointTo(createPoint(4,8));
		assertNotNull(p);
		assertEquals(4, p.ix());
		assertEquals(8, p.iy());
		
		p = this.shape.getClosestPointTo(createPoint(0,0));
		assertNotNull(p);
		assertEquals(3, p.ix());
		assertEquals(3, p.iy());

		p = this.shape.getClosestPointTo(createPoint(5,14));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(13, p.iy());
	}

	@Test
	public void getFarthestPointTo() {
		Point3D p;
		
		p = this.shape.getFarthestPointTo(createPoint(5,8));
		assertNotNull(p);
		assertEquals(3, p.ix());
		assertEquals(3, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(10,10));
		assertNotNull(p);
		assertEquals(0, p.ix());
		assertEquals(6, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(4,8));
		assertNotNull(p);
		assertEquals(10, p.ix());
		assertEquals(6, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(0,0));
		assertNotNull(p);
		assertEquals(7, p.ix());
		assertEquals(13, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(5,14));
		assertNotNull(p);
		assertEquals(3, p.ix());
		assertEquals(3, p.iy());
	}
	
	@Test
	public void containsIntInt() {
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
	public void containsRectangularPrism3ai() {
		assertFalse(this.shape.contains(createRectangle(0,0,1,1)));
		assertFalse(this.shape.contains(createRectangle(0,0,8,1)));
		assertFalse(this.shape.contains(createRectangle(0,0,8,6)));
		assertFalse(this.shape.contains(createRectangle(0,0,100,100)));
		assertTrue(this.shape.contains(createRectangle(7,10,1,1)));
		assertFalse(this.shape.contains(createRectangle(16,0,100,100)));
		assertFalse(this.shape.contains(createRectangle(9,11,5,5)));
	}

	@Test
	public void intersectsRectangularPrism3ai() {
		assertFalse(this.shape.intersects(createRectangle(0,0,1,1)));
		assertFalse(this.shape.intersects(createRectangle(0,0,8,1)));
		assertTrue(this.shape.intersects(createRectangle(0,0,8,6)));
		assertTrue(this.shape.intersects(createRectangle(0,0,100,100)));
		assertTrue(this.shape.intersects(createRectangle(7,10,1,1)));
		assertFalse(this.shape.intersects(createRectangle(16,0,100,100)));
	}

	@Test
	public void intersectsSegment3ai() {
		assertFalse(this.shape.intersects(createSegment(0,0,1,1)));
		assertFalse(this.shape.intersects(createSegment(0,0,8,1)));
		assertTrue(this.shape.intersects(createSegment(0,0,8,6)));
		assertTrue(this.shape.intersects(createSegment(0,0,100,100)));
		assertTrue(this.shape.intersects(createSegment(7,10,1,1)));
		assertFalse(this.shape.intersects(createSegment(16,0,100,100)));
		assertFalse(this.shape.intersects(createSegment(8,13,10,11)));
	}

	@Test
	public void intersectsPath3ai() {
		Path3ai path = createPath();
		path.moveTo(0, 0);
		path.lineTo(2, 2);
		path.quadTo(3, 0, 4, 3);
		path.curveTo(5, -1, 6, 5, 7, -5);
		path.closePath();
		this.shape = (T) createSphere(0, 0, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(4, 3, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(2, 2, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(2, 1, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(3, 0, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(-1, -1, 1);
		assertFalse(this.shape.intersects(path));
		this.shape = (T) createSphere(4, -3, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(-3, 4, 1);
		assertFalse(this.shape.intersects(path));
		this.shape = (T) createSphere(6, -5, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(4, 0, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(5, 0, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(6, 2, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(-5, 0, 3);
		assertFalse(this.shape.intersects(path));
	}

	@Test
	public void intersectsSphere3ai() {
		assertFalse(this.shape.intersects(createSphere(0,0,1)));
		assertTrue(this.shape.intersects(createSphere(0,0,8)));
		assertTrue(this.shape.intersects(createSphere(0,0,100)));
		assertTrue(this.shape.intersects(createSphere(7,10,1)));
		assertFalse(this.shape.intersects(createSphere(16,0,5)));
		assertFalse(this.shape.intersects(createSphere(5,15,1)));
	}

	@Test
	public void getPathIterator() {
		PathIterator3ai<?> pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 10,8);
		assertElement(pi, PathElementType.CURVE_TO, 10,10, 7,13, 5,13);
		assertElement(pi, PathElementType.CURVE_TO, 2,13, 0,10, 0,8);
		assertElement(pi, PathElementType.CURVE_TO, 0,5, 2,3, 5,3);
		assertElement(pi, PathElementType.CURVE_TO, 7,3, 10,5, 10,8);
		assertElement(pi, PathElementType.CLOSE, 10, 8);
		assertNoElement(pi);
	}

	@Test
	public void getPathIteratorTransform3D() {
		Transform3D tr;
		PathIterator3ai<?> pi;
		
		tr = new Transform3D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 10,8);
		assertElement(pi, PathElementType.CURVE_TO, 10,10, 7,13, 5,13);
		assertElement(pi, PathElementType.CURVE_TO, 2,13, 0,10, 0,8);
		assertElement(pi, PathElementType.CURVE_TO, 0,5, 2,3, 5,3);
		assertElement(pi, PathElementType.CURVE_TO, 7,3, 10,5, 10,8);
		assertElement(pi, PathElementType.CLOSE, 10,8);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4f, 4.5f);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 13,13);
		assertElement(pi, PathElementType.CURVE_TO, 13,16, 11,18, 8,18);
		assertElement(pi, PathElementType.CURVE_TO, 5,18, 3,16, 3,13);
		assertElement(pi, PathElementType.CURVE_TO, 3,10, 5,8, 8,8);
		assertElement(pi, PathElementType.CURVE_TO, 11,8, 13,10, 13,13);
		assertElement(pi, PathElementType.CLOSE, 13,13);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 1,13);
		assertElement(pi, PathElementType.CURVE_TO, -1,15, -4,15, -6,13);
		assertElement(pi, PathElementType.CURVE_TO, -8,11, -8,8, -6,6);
		assertElement(pi, PathElementType.CURVE_TO, -4,4, -1,4, 1,6);
		assertElement(pi, PathElementType.CURVE_TO, 4,8, 4,11, 1,13);
		assertElement(pi, PathElementType.CLOSE, 1,13);
		assertNoElement(pi);
	}

	@Test
	public void createTransformedShape() {
		Transform3D tr;
		PathIterator3ai<?> pi;
		
		tr = new Transform3D();
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 10,8);
		assertElement(pi, PathElementType.CURVE_TO, 10,10, 7,13, 5,13);
		assertElement(pi, PathElementType.CURVE_TO, 2,13, 0,10, 0,8);
		assertElement(pi, PathElementType.CURVE_TO, 0,5, 2,3, 5,3);
		assertElement(pi, PathElementType.CURVE_TO, 7,3, 10,5, 10,8);
		assertElement(pi, PathElementType.CLOSE, 10,8);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4f, 4.5f);
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 13,13);
		assertElement(pi, PathElementType.CURVE_TO, 13,16, 11,18, 8,18);
		assertElement(pi, PathElementType.CURVE_TO, 5,18, 3,16, 3,13);
		assertElement(pi, PathElementType.CURVE_TO, 3,10, 5,8, 8,8);
		assertElement(pi, PathElementType.CURVE_TO, 11,8, 13,10, 13,13);
		assertElement(pi, PathElementType.CLOSE, 13,13);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 1,13);
		assertElement(pi, PathElementType.CURVE_TO, -1,15, -4,15, -6,13);
		assertElement(pi, PathElementType.CURVE_TO, -8,11, -8,8, -6,6);
		assertElement(pi, PathElementType.CURVE_TO, -4,4, -1,4, 1,6);
		assertElement(pi, PathElementType.CURVE_TO, 4,8, 4,11, 1,13);
		assertElement(pi, PathElementType.CLOSE, 1,13);
		assertNoElement(pi);
	}

	@Test
	public void setIT() {
		this.shape.set((T) createSphere(17, 20, 7));
		assertEquals(17, this.shape.getX());
		assertEquals(20, this.shape.getY());
		assertEquals(7, this.shape.getRadius());
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
	public void getDistance() {
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(5,8)));
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(10,10)));
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(4,8)));
		assertEpsilonEquals(4.242640687f, this.shape.getDistance(createPoint(0,0)));
		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(5,14)));
	}

	@Test
	@Override
	public void getDistanceSquared() {
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(5,8)));
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(10,10)));
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(4,8)));
		assertEpsilonEquals(18f, this.shape.getDistanceSquared(createPoint(0,0)));
		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(5,14)));
	}

	@Test
	@Override
	public void getDistanceL1() {
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(5,8)));
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(10,10)));
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(4,8)));
		assertEpsilonEquals(6f, this.shape.getDistanceL1(createPoint(0,0)));
		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(5,14)));
	}

	@Test
	@Override
	public void getDistanceLinf() {
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(5,8)));
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(10,10)));
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(4,8)));
		assertEpsilonEquals(3f, this.shape.getDistanceLinf(createPoint(0,0)));
		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(5,14)));
	}

	@Test
	@Override
	public void toBoundingBoxB() {
		B r = (B) createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(r);
		assertEquals(0, r.getMinX());
		assertEquals(3, r.getMinY());
		assertEquals(10, r.getMaxX());
		assertEquals(13, r.getMaxY());
	}

	@Test
	@Override
	public void translateVector3D() {
		this.shape.translate(createVector(3, 4));
		assertEquals(8, this.shape.getX());
		assertEquals(12, this.shape.getY());
		assertEquals(5, this.shape.getRadius());
	}

	@Test
	public void staticComputeClosestPointTo() {
		Point3D p;
		
		p = createPoint(0, 0);
		Sphere3ai.computeClosestPointTo(5, 8, 5, 5, 8, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = createPoint(0, 0);
		Sphere3ai.computeClosestPointTo(5, 8, 5, 10, 10, p);
		assertNotNull(p);
		assertEquals(10, p.ix());
		assertEquals(10, p.iy());
		
		p = createPoint(0, 0);
		Sphere3ai.computeClosestPointTo(5, 8, 5, 4, 8, p);
		assertNotNull(p);
		assertEquals(4, p.ix());
		assertEquals(8, p.iy());
		
		p = createPoint(0, 0);
		Sphere3ai.computeClosestPointTo(5, 8, 5, 0, 0, p);
		assertNotNull(p);
		assertEquals(3, p.ix());
		assertEquals(3, p.iy());

		p = createPoint(0, 0);
		Sphere3ai.computeClosestPointTo(5, 8, 5, 5, 14, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(13, p.iy());
	}

	@Test
	public void staticComputeFarthestPointTo() {
		Point3D p;
		
		p = createPoint(0, 0);
		Sphere3ai.computeFarthestPointTo(5, 8, 5, 5, 8, p);
		assertNotNull(p);
		assertEquals(3, p.ix());
		assertEquals(3, p.iy());
		
		p = createPoint(0, 0);
		Sphere3ai.computeFarthestPointTo(5, 8, 5, 10, 10, p);
		assertNotNull(p);
		assertEquals(0, p.ix());
		assertEquals(6, p.iy());
		
		p = createPoint(0, 0);
		Sphere3ai.computeFarthestPointTo(5, 8, 5, 4, 8, p);
		assertNotNull(p);
		assertEquals(10, p.ix());
		assertEquals(6, p.iy());
		
		p = createPoint(0, 0);
		Sphere3ai.computeFarthestPointTo(5, 8, 5, 0, 0, p);
		assertNotNull(p);
		assertEquals(7, p.ix());
		assertEquals(13, p.iy());

		p = createPoint(0, 0);
		Sphere3ai.computeFarthestPointTo(5, 8, 5, 5, 14, p);
		assertNotNull(p);
		assertEquals(3, p.ix());
		assertEquals(3, p.iy());
	}
	
	@Test
	public void staticContainsIntIntIntIntInt() {
		assertFalse(Sphere3ai.contains(5, 8, 5, 0, 0));
		assertFalse(Sphere3ai.contains(5, 8, 5, 11, 10));
		assertFalse(Sphere3ai.contains(5, 8, 5, 11, 50));
		assertFalse(Sphere3ai.contains(5, 8, 5, 9, 12));
		assertTrue(Sphere3ai.contains(5, 8, 5, 9, 11));
		assertTrue(Sphere3ai.contains(5, 8, 5, 8, 12));
		assertTrue(Sphere3ai.contains(5, 8, 5, 3, 7));
		assertFalse(Sphere3ai.contains(5, 8, 5, 10, 11));
		assertTrue(Sphere3ai.contains(5, 8, 5, 9, 10));
		assertFalse(Sphere3ai.contains(-1, -1, 1, 0, 0));
	}
	
	@Test
	public void staticContainsIntIntIntIntIntInt() {
		assertTrue(Sphere3ai.contains(5, 8, 5, 0, 9, 11));
		assertFalse(Sphere3ai.contains(5, 8, 5, 1, 9, 11));
		assertFalse(Sphere3ai.contains(5, 8, 5, 2, 9, 11));
		assertFalse(Sphere3ai.contains(5, 8, 5, 3, 9, 11));
		
		assertTrue(Sphere3ai.contains(5, 8, 5, 0, 8, 12));
		assertFalse(Sphere3ai.contains(5, 8, 5, 1, 8, 12));
		assertFalse(Sphere3ai.contains(5, 8, 5, 2, 8, 12));
		assertFalse(Sphere3ai.contains(5, 8, 5, 3, 8, 12));
		
		assertFalse(Sphere3ai.contains(5, 8, 5, 0, 3, 7));
		assertFalse(Sphere3ai.contains(5, 8, 5, 1, 3, 7));
		assertTrue(Sphere3ai.contains(5, 8, 5, 2, 3, 7));
		assertFalse(Sphere3ai.contains(5, 8, 5, 3, 3, 7));

		assertTrue(Sphere3ai.contains(5, 8, 5, 0, 9, 10));
		assertFalse(Sphere3ai.contains(5, 8, 5, 1, 9, 10));
		assertFalse(Sphere3ai.contains(5, 8, 5, 2, 9, 10));
		assertFalse(Sphere3ai.contains(5, 8, 5, 3, 9, 10));
	}
	
	@Test
	public void staticGetPointIterator() {
		Iterator iterator = Sphere3ai.getPointIterator(5, 8, 5, 0, 8, (GeomFactory3ai) this.shape.getGeomFactory());
		Point3D p;
		
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
			p = (Point3D) iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(y, p.iy());
		}

		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void staticIntersectsCircleCircle() {
		assertFalse(Sphere3ai.intersectsCircleCircle(5, 8, 5, 0, 0, 1));
		assertTrue(Sphere3ai.intersectsCircleCircle(5, 8, 5, 0, 0, 8));
		assertTrue(Sphere3ai.intersectsCircleCircle(5, 8, 5, 0, 0, 100));
		assertTrue(Sphere3ai.intersectsCircleCircle(5, 8, 5, 7, 10, 1));
		assertFalse(Sphere3ai.intersectsCircleCircle(5, 8, 5, 16, 0, 5));
		assertFalse(Sphere3ai.intersectsCircleCircle(5, 8, 5, 5, 15, 1));
	}
	
	@Test
	public void staticIntersectsCircleRectangle() {
		assertFalse(Sphere3ai.intersectsCircleRectangle(0, 0, 1, 5, 8, 15, 13));
		assertFalse(Sphere3ai.intersectsCircleRectangle(0, 0, 8, 5, 8, 15, 13));
		assertTrue(Sphere3ai.intersectsCircleRectangle(0, 0, 100, 5, 8, 15, 13));
		assertTrue(Sphere3ai.intersectsCircleRectangle(7, 10, 1, 5, 8, 15, 13));
		assertFalse(Sphere3ai.intersectsCircleRectangle(16, 0, 5, 5, 8, 15, 13));
		assertFalse(Sphere3ai.intersectsCircleRectangle(5, 15, 1, 5, 8, 15, 13));
	}
	
	@Test
	public void staticIntersectsCircleSegment() {
		assertFalse(Sphere3ai.intersectsCircleSegment(5, 8, 5, 0, 0, 1, 1));
		assertFalse(Sphere3ai.intersectsCircleSegment(5, 8, 5, 0, 0, 8, 1));
		assertTrue(Sphere3ai.intersectsCircleSegment(5, 8, 5, 0, 0, 8, 6));
		assertTrue(Sphere3ai.intersectsCircleSegment(5, 8, 5, 0, 0, 100, 100));
		assertTrue(Sphere3ai.intersectsCircleSegment(5, 8, 5, 7, 10, 1, 1));
		assertFalse(Sphere3ai.intersectsCircleSegment(5, 8, 5, 16, 0, 100, 100));
		assertFalse(Sphere3ai.intersectsCircleSegment(5, 8, 5, 8, 13, 10, 11));
	}

	@Override
	public void intersectsPathIterator3ai() {
		Path3ai path = createPath();
		path.moveTo(0, 0);
		path.lineTo(2, 2);
		path.quadTo(3, 0, 4, 3);
		path.curveTo(5, -1, 6, 5, 7, -5);
		path.closePath();
		this.shape = (T) createSphere(0, 0, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(4, 3, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(2, 2, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(2, 1, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(3, 0, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(-1, -1, 1);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(4, -3, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(-3, 4, 1);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(6, -5, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(4, 0, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(5, 0, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(6, 2, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(-5, 0, 3);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
	}

	@Override
	public void intersectsShape3D() {
		assertTrue(this.shape.intersects((Shape3D) createSphere(0,0,100)));
		assertTrue(this.shape.intersects((Shape3D) createRectangle(0,0,100,100)));
	}

	@Override
	public void operator_addVector3D() {
		this.shape.operator_add(createVector(3, 4));
		assertEquals(8, this.shape.getX());
		assertEquals(12, this.shape.getY());
		assertEquals(5, this.shape.getRadius());
	}

	@Override
	public void operator_plusVector3D() {
		T r = this.shape.operator_plus(createVector(3, 4));
		assertEquals(8, r.getX());
		assertEquals(12, r.getY());
		assertEquals(5, r.getRadius());
	}

	@Override
	public void operator_removeVector3D() {
		this.shape.operator_remove(createVector(3, 4));
		assertEquals(2, this.shape.getX());
		assertEquals(4, this.shape.getY());
		assertEquals(5, this.shape.getRadius());
	}

	@Override
	public void operator_minusVector3D() {
		T r = this.shape.operator_minus(createVector(3, 4));
		assertEquals(2, r.getX());
		assertEquals(4, r.getY());
		assertEquals(5, r.getRadius());
	}

	@Override
	public void operator_multiplyTransform3D() {
		Transform3D tr;
		PathIterator3ai<?> pi;
		
		tr = new Transform3D();
		pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 10,8);
		assertElement(pi, PathElementType.CURVE_TO, 10,10, 7,13, 5,13);
		assertElement(pi, PathElementType.CURVE_TO, 2,13, 0,10, 0,8);
		assertElement(pi, PathElementType.CURVE_TO, 0,5, 2,3, 5,3);
		assertElement(pi, PathElementType.CURVE_TO, 7,3, 10,5, 10,8);
		assertElement(pi, PathElementType.CLOSE, 10,8);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4f, 4.5f);
		pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 13,13);
		assertElement(pi, PathElementType.CURVE_TO, 13,16, 11,18, 8,18);
		assertElement(pi, PathElementType.CURVE_TO, 5,18, 3,16, 3,13);
		assertElement(pi, PathElementType.CURVE_TO, 3,10, 5,8, 8,8);
		assertElement(pi, PathElementType.CURVE_TO, 11,8, 13,10, 13,13);
		assertElement(pi, PathElementType.CLOSE, 13,13);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 1,13);
		assertElement(pi, PathElementType.CURVE_TO, -1,15, -4,15, -6,13);
		assertElement(pi, PathElementType.CURVE_TO, -8,11, -8,8, -6,6);
		assertElement(pi, PathElementType.CURVE_TO, -4,4, -1,4, 1,6);
		assertElement(pi, PathElementType.CURVE_TO, 4,8, 4,11, 1,13);
		assertElement(pi, PathElementType.CLOSE, 1,13);
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
		assertTrue(this.shape.operator_and(createSphere(0,0,100)));
		assertTrue(this.shape.operator_and(createRectangle(0,0,100,100)));
	}

	@Override
	public void operator_upToPoint3D() {
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(5,8)));
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(10,10)));
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(4,8)));
		assertEpsilonEquals(4.242640687f, this.shape.operator_upTo(createPoint(0,0)));
		assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(5,14)));
	}

}