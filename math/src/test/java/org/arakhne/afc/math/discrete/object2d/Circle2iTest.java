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
package org.arakhne.afc.math.discrete.object2d;

import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.generic.PathElementType;
import org.arakhne.afc.math.matrix.Transform2D;


/**
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Circle2iTest extends AbstractShape2iTestCase<Circle2i> {

	@Override
	protected Circle2i createShape() {
		return new Circle2i(5, 8, 5);
	}
	
	@Override
	public void testIsEmpty() {
		assertFalse(this.r.isEmpty());
		this.r.clear();
		assertTrue(this.r.isEmpty());
	}

	@Override
	public void testClone() {
		Circle2i b = this.r.clone();

		assertNotSame(b, this.r);
		assertEquals(this.r.getX(), b.getX());
		assertEquals(this.r.getY(), b.getY());
		assertEquals(this.r.getRadius(), b.getRadius());
		
		b.set(this.r.getX()+1, this.r.getY()+1,
				this.r.getRadius()+1);

		assertNotEquals(this.r.getX(), b.getX());
		assertNotEquals(this.r.getY(), b.getY());
		assertNotEquals(this.r.getRadius(), b.getRadius());
	}

	@Override
	public void testClear() {
		this.r.clear();
		assertEquals(0, this.r.getX());
		assertEquals(0, this.r.getY());
		assertEquals(0, this.r.getRadius());
	}

	@Override
	public void testTranslateIntInt() {
		this.r.translate(3,  4);
		assertEquals(8, this.r.getX());
		assertEquals(12, this.r.getY());
		assertEquals(5, this.r.getRadius());
	}

	/**
	 */
	@Override
	public void testToBoundingBox() {
		Rectangle2i r = this.r.toBoundingBox();
		assertEquals(0, r.getMinX());
		assertEquals(3, r.getMinY());
		assertEquals(10, r.getMaxX());
		assertEquals(13, r.getMaxY());
	}

	/**
	 */
	public void testGetPointIterator_big() {
		Iterator<Point2i> iterator = this.r.getPointIterator();
		Point2i p;
		
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
			assertEquals("(>"+x+"<;"+y+")!=("+p.x()+";"+p.y()+")", x, p.x()); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
			assertEquals("("+x+";>"+y+"<)!=("+p.x()+";"+p.y()+")", y, p.y()); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
		}
		
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public static void testGetPointIterator_small() {
		Circle2i circle = new Circle2i(4, 6, 3);
		Iterator<Point2i> iterator = circle.getPointIterator();
		Point2i p;
		
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
			assertEquals("(>"+x+"<;"+y+")!=("+p.x()+";"+p.y()+")", x, p.x()); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
			assertEquals("("+x+";>"+y+"<)!=("+p.x()+";"+p.y()+")", y, p.y()); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
		}
		
		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testGetClosestPointToPoint2D() {
		Point2i p;
		
		p = this.r.getClosestPointTo(new Point2i(5,8));
		assertNotNull(p);
		assertEquals(5, p.x());
		assertEquals(8, p.y());
		
		p = this.r.getClosestPointTo(new Point2i(10,10));
		assertNotNull(p);
		assertEquals(10, p.x());
		assertEquals(10, p.y());
		
		p = this.r.getClosestPointTo(new Point2i(4,8));
		assertNotNull(p);
		assertEquals(4, p.x());
		assertEquals(8, p.y());
		
		p = this.r.getClosestPointTo(new Point2i(0,0));
		assertNotNull(p);
		assertEquals(3, p.x());
		assertEquals(3, p.y());

		p = this.r.getClosestPointTo(new Point2i(5,14));
		assertNotNull(p);
		assertEquals(5, p.x());
		assertEquals(13, p.y());
	}

	@Override
	public void testDistancePoint2D() {
		assertEpsilonEquals(0f, this.r.distance(new Point2i(5,8)));
		assertEpsilonEquals(0f, this.r.distance(new Point2i(10,10)));
		assertEpsilonEquals(0f, this.r.distance(new Point2i(4,8)));
		assertEpsilonEquals(4.242640687f, this.r.distance(new Point2i(0,0)));
		assertEpsilonEquals(1f, this.r.distance(new Point2i(5,14)));
	}

	/**
	 */
	@Override
	public void testDistanceSquaredPoint2D() {
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2i(5,8)));
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2i(10,10)));
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2i(4,8)));
		assertEpsilonEquals(18f, this.r.distanceSquared(new Point2i(0,0)));
		assertEpsilonEquals(1f, this.r.distanceSquared(new Point2i(5,14)));
	}

	/**
	 */
	@Override
	public void testDistanceL1Point2D() {
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2i(5,8)));
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2i(10,10)));
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2i(4,8)));
		assertEpsilonEquals(6f, this.r.distanceL1(new Point2i(0,0)));
		assertEpsilonEquals(1f, this.r.distanceL1(new Point2i(5,14)));
	}
	
	/**
	 */
	@Override
	public void testDistanceLinfPoint2D() {
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2i(5,8)));
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2i(10,10)));
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2i(4,8)));
		assertEpsilonEquals(3f, this.r.distanceLinf(new Point2i(0,0)));
		assertEpsilonEquals(1f, this.r.distanceLinf(new Point2i(5,14)));
	}
	
	/**
	 */
	public void testContainsIntInt() {
		assertFalse(this.r.contains(0,0));
		assertFalse(this.r.contains(11,10));
		assertFalse(this.r.contains(11,50));
		assertFalse(this.r.contains(9,12));
		assertTrue(this.r.contains(9,11));
		assertTrue(this.r.contains(8,12));
		assertTrue(this.r.contains(3,7));
		assertFalse(this.r.contains(10,11));
		assertTrue(this.r.contains(9,10));
		
		this.r = new Circle2i(-1,-1,1);
		assertFalse(this.r.contains(0,0));
	}

	/**
	 */
	public void testContainsRectangle2i() {
		assertFalse(this.r.contains(new Rectangle2i(0,0,1,1)));
		assertFalse(this.r.contains(new Rectangle2i(0,0,8,1)));
		assertFalse(this.r.contains(new Rectangle2i(0,0,8,6)));
		assertFalse(this.r.contains(new Rectangle2i(0,0,100,100)));
		assertTrue(this.r.contains(new Rectangle2i(7,10,1,1)));
		assertFalse(this.r.contains(new Rectangle2i(16,0,100,100)));
		assertFalse(this.r.contains(new Rectangle2i(9,11,5,5)));
	}

	/**
	 */
	public void testIntersectsRectangle2i() {
		assertFalse(this.r.intersects(new Rectangle2i(0,0,1,1)));
		assertFalse(this.r.intersects(new Rectangle2i(0,0,8,1)));
		assertTrue(this.r.intersects(new Rectangle2i(0,0,8,6)));
		assertTrue(this.r.intersects(new Rectangle2i(0,0,100,100)));
		assertTrue(this.r.intersects(new Rectangle2i(7,10,1,1)));
		assertFalse(this.r.intersects(new Rectangle2i(16,0,100,100)));
	}

	/**
	 */
	public void testIntersectsSegment2i() {
		assertFalse(this.r.intersects(new Segment2i(0,0,1,1)));
		assertFalse(this.r.intersects(new Segment2i(0,0,8,1)));
		assertTrue(this.r.intersects(new Segment2i(0,0,8,6)));
		assertTrue(this.r.intersects(new Segment2i(0,0,100,100)));
		assertTrue(this.r.intersects(new Segment2i(7,10,1,1)));
		assertFalse(this.r.intersects(new Segment2i(16,0,100,100)));
		assertFalse(this.r.intersects(new Segment2i(8,13,10,11)));
	}

	/**
	 */
	public void testIntersectsCircle2i() {
		assertFalse(this.r.intersects(new Circle2i(0,0,1)));
		assertTrue(this.r.intersects(new Circle2i(0,0,8)));
		assertTrue(this.r.intersects(new Circle2i(0,0,100)));
		assertTrue(this.r.intersects(new Circle2i(7,10,1)));
		assertFalse(this.r.intersects(new Circle2i(16,0,5)));
		assertFalse(this.r.intersects(new Circle2i(5,15,1)));
	}

	/**
	 */
	public void testGetPathIteratorVoid() {
		PathIterator2i pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 10,8);
		assertElement(pi, PathElementType.CURVE_TO, 10,10, 7,13, 5,13);
		assertElement(pi, PathElementType.CURVE_TO, 2,13, 0,10, 0,8);
		assertElement(pi, PathElementType.CURVE_TO, 0,5, 2,3, 5,3);
		assertElement(pi, PathElementType.CURVE_TO, 7,3, 10,5, 10,8);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	/**
	 */
	public void testGetPathIteratorTransform2D() {
		Transform2D tr;
		PathIterator2i pi;
		
		tr = new Transform2D();
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 10,8);
		assertElement(pi, PathElementType.CURVE_TO, 10,10, 7,13, 5,13);
		assertElement(pi, PathElementType.CURVE_TO, 2,13, 0,10, 0,8);
		assertElement(pi, PathElementType.CURVE_TO, 0,5, 2,3, 5,3);
		assertElement(pi, PathElementType.CURVE_TO, 7,3, 10,5, 10,8);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(3.4f, 4.5f);
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 13,12);
		assertElement(pi, PathElementType.CURVE_TO, 13,14, 10,17, 8,17);
		assertElement(pi, PathElementType.CURVE_TO, 5,17, 3,14, 3,12);
		assertElement(pi, PathElementType.CURVE_TO, 3,9, 5,7, 8,7);
		assertElement(pi, PathElementType.CURVE_TO, 10,7, 13,9, 13,12);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 1,12);
		assertElement(pi, PathElementType.CURVE_TO, 0,14, -4,14, -5,12);
		assertElement(pi, PathElementType.CURVE_TO, -7,10, -7,7, -5,5);
		assertElement(pi, PathElementType.CURVE_TO, -3,3, 0,3, 1,5);
		assertElement(pi, PathElementType.CURVE_TO, 2,7, 3,10, 1,12);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

}