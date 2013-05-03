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
import org.arakhne.afc.math.discrete.object2d.Rectangle2i.Side;
import org.arakhne.afc.math.generic.PathElementType;
import org.arakhne.afc.math.matrix.Transform2D;


/**
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Rectangle2iTest extends AbstractShape2iTestCase<Rectangle2i> {

	@Override
	protected Rectangle2i createShape() {
		return new Rectangle2i(5, 8, 10, 5);
	}
	
	@Override
	public void testIsEmpty() {
		assertFalse(this.r.isEmpty());
		this.r.clear();
		assertTrue(this.r.isEmpty());
	}

	@Override
	public void testClone() {
		Rectangle2i b = this.r.clone();

		assertNotSame(b, this.r);
		assertEquals(this.r.getMinX(), b.getMinX());
		assertEquals(this.r.getMinY(), b.getMinY());
		assertEquals(this.r.getMaxX(), b.getMaxX());
		assertEquals(this.r.getMaxY(), b.getMaxY());
		
		b.set(this.r.getMinX()+1, this.r.getMinY()+1,
				this.r.getWidth()+1, this.r.getHeight()+1);

		assertNotEquals(this.r.getMinX(), b.getMinX());
		assertNotEquals(this.r.getMinY(), b.getMinY());
		assertNotEquals(this.r.getMaxX(), b.getMaxX());
		assertNotEquals(this.r.getMaxY(), b.getMaxY());
	}

	@Override
	public void testClear() {
		this.r.clear();
		assertEquals(0, this.r.getMinX());
		assertEquals(0, this.r.getMinY());
		assertEquals(0, this.r.getMaxX());
		assertEquals(0, this.r.getMaxY());
	}

	@Override
	public void testTranslateIntInt() {
		this.r.translate(3,  4);
		assertEquals(8, this.r.getMinX());
		assertEquals(12, this.r.getMinY());
		assertEquals(18, this.r.getMaxX());
		assertEquals(17, this.r.getMaxY());
	}

	/**
	 */
	@Override
	public void testToBoundingBox() {
		Rectangle2i r = this.r.toBoundingBox();
		assertNotSame(this.r, r);
		assertEpsilonEquals(this.r.getMinX(), r.getMinX());
		assertEpsilonEquals(this.r.getMinY(), r.getMinY());
		assertEpsilonEquals(this.r.getMaxX(), r.getMaxX());
		assertEpsilonEquals(this.r.getMaxY(), r.getMaxY());
	}

	/**
	 */
	public void testGetPointIterator() {
		Iterator<Point2i> iterator = this.r.getPointIterator();
		Point2i p;
		
		int[] coords;
		
		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.x());
			assertEquals(8, p.y());
		}

		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.x());
			assertEquals(y, p.y());
		}

		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.x());
			assertEquals(13, p.y());
		}

		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.x());
			assertEquals(y, p.y());
		}

		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testGetPointIteratorSide_Top() {
		Iterator<Point2i> iterator = this.r.getPointIterator(Side.TOP);
		Point2i p;
		
		int[] coords;
		
		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.x());
			assertEquals(8, p.y());
		}

		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.x());
			assertEquals(y, p.y());
		}

		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.x());
			assertEquals(13, p.y());
		}

		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.x());
			assertEquals(y, p.y());
		}

		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testGetPointIteratorSide_Right() {
		Iterator<Point2i> iterator = this.r.getPointIterator(Side.RIGHT);
		Point2i p;
		
		int[] coords;
		
		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.x());
			assertEquals(y, p.y());
		}

		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.x());
			assertEquals(13, p.y());
		}

		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.x());
			assertEquals(y, p.y());
		}

		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.x());
			assertEquals(8, p.y());
		}

		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testGetPointIteratorSide_Bottom() {
		Iterator<Point2i> iterator = this.r.getPointIterator(Side.BOTTOM);
		Point2i p;
		
		int[] coords;
		
		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.x());
			assertEquals(13, p.y());
		}

		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.x());
			assertEquals(y, p.y());
		}

		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.x());
			assertEquals(8, p.y());
		}

		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.x());
			assertEquals(y, p.y());
		}

		assertFalse(iterator.hasNext());
	}

	/**
	 */
	public void testGetPointIteratorSide_Left() {
		Iterator<Point2i> iterator = this.r.getPointIterator(Side.LEFT);
		Point2i p;
		
		int[] coords;
		
		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.x());
			assertEquals(y, p.y());
		}

		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.x());
			assertEquals(8, p.y());
		}

		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.x());
			assertEquals(y, p.y());
		}

		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.x());
			assertEquals(13, p.y());
		}

		assertFalse(iterator.hasNext());
	}

	@Override
	public void testDistancePoint2D() {
		assertEpsilonEquals(0f, this.r.distance(new Point2i(5,8)));
		assertEpsilonEquals(0f, this.r.distance(new Point2i(10,10)));
		assertEpsilonEquals(1f, this.r.distance(new Point2i(4,8)));
		assertEpsilonEquals(9.433981132f, this.r.distance(new Point2i(0,0)));
	}

	/**
	 */
	@Override
	public void testDistanceSquaredPoint2D() {
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2i(5,8)));
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2i(10,10)));
		assertEpsilonEquals(1f, this.r.distanceSquared(new Point2i(4,8)));
		assertEpsilonEquals(89f, this.r.distanceSquared(new Point2i(0,0)));
	}

	/**
	 */
	@Override
	public void testDistanceL1Point2D() {
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2i(5,8)));
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2i(10,10)));
		assertEpsilonEquals(1f, this.r.distanceL1(new Point2i(4,8)));
		assertEpsilonEquals(13f, this.r.distanceL1(new Point2i(0,0)));
	}
	
	/**
	 */
	@Override
	public void testDistanceLinfPoint2D() {
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2i(5,8)));
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2i(10,10)));
		assertEpsilonEquals(1f, this.r.distanceLinf(new Point2i(4,8)));
		assertEpsilonEquals(8f, this.r.distanceLinf(new Point2i(0,0)));
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
		assertEquals(5, p.x());
		assertEquals(8, p.y());
		
		p = this.r.getClosestPointTo(new Point2i(0,0));
		assertNotNull(p);
		assertEquals(5, p.x());
		assertEquals(8, p.y());
	}

	/**
	 */
	public void testContainsIntInt() {
		assertFalse(this.r.contains(0,0));
		assertTrue(this.r.contains(11,10));
		assertFalse(this.r.contains(11,50));
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
	}

	/**
	 */
	public void testIntersectsRectangle2i() {
		assertFalse(this.r.intersects(new Rectangle2i(0,0,1,1)));
		assertFalse(this.r.intersects(new Rectangle2i(0,0,8,1)));
		assertFalse(this.r.intersects(new Rectangle2i(0,0,8,6)));
		assertTrue(this.r.intersects(new Rectangle2i(0,0,100,100)));
		assertTrue(this.r.intersects(new Rectangle2i(7,10,1,1)));
		assertFalse(this.r.intersects(new Rectangle2i(16,0,100,100)));
	}

	/**
	 */
	public void testIntersectsSegment2i() {
		assertFalse(this.r.intersects(new Segment2i(0,0,1,1)));
		assertFalse(this.r.intersects(new Segment2i(0,0,8,1)));
		assertFalse(this.r.intersects(new Segment2i(0,0,8,6)));
		assertTrue(this.r.intersects(new Segment2i(0,0,100,100)));
		assertTrue(this.r.intersects(new Segment2i(7,10,1,1)));
		assertFalse(this.r.intersects(new Segment2i(16,0,100,100)));
	}

	/**
	 */
	public void testGetPathIteratorVoid() {
		PathIterator2i pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5,8);
		assertElement(pi, PathElementType.LINE_TO, 15,8);
		assertElement(pi, PathElementType.LINE_TO, 15,13);
		assertElement(pi, PathElementType.LINE_TO, 5,13);
		assertElement(pi, PathElementType.LINE_TO, 5,8);
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
		assertElement(pi, PathElementType.MOVE_TO, 5,8);
		assertElement(pi, PathElementType.LINE_TO, 15,8);
		assertElement(pi, PathElementType.LINE_TO, 15,13);
		assertElement(pi, PathElementType.LINE_TO, 5,13);
		assertElement(pi, PathElementType.LINE_TO, 5,8);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(3.4f, 4.5f);
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 8,12);
		assertElement(pi, PathElementType.LINE_TO, 18,12);
		assertElement(pi, PathElementType.LINE_TO, 18,17);
		assertElement(pi, PathElementType.LINE_TO, 8,17);
		assertElement(pi, PathElementType.LINE_TO, 8,12);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, -2,9);
		assertElement(pi, PathElementType.LINE_TO, 4,16);
		assertElement(pi, PathElementType.LINE_TO, 1,19);
		assertElement(pi, PathElementType.LINE_TO, -5,12);
		assertElement(pi, PathElementType.LINE_TO, -2,9);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

}