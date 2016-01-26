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
package org.arakhne.afc.math.geometry.d2.discrete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.continuous.Transform2D;
import org.arakhne.afc.math.geometry.d2.discrete.Circle2i;
import org.arakhne.afc.math.geometry.d2.discrete.PathIterator2i;
import org.arakhne.afc.math.geometry.d2.discrete.Point2i;
import org.arakhne.afc.math.geometry.d2.discrete.Rectangle2i;
import org.arakhne.afc.math.geometry.d2.discrete.Segment2i;
import org.arakhne.afc.math.geometry.d2.discrete.Rectangle2i.Side;
import org.junit.Test;

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
	
	@Test
	@Override
	public void isEmpty() {
		assertFalse(this.r.isEmpty());
		this.r.clear();
		assertTrue(this.r.isEmpty());
	}

	@Test
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

	@Test
	@Override
	public void clear() {
		this.r.clear();
		assertEquals(0, this.r.getMinX());
		assertEquals(0, this.r.getMinY());
		assertEquals(0, this.r.getMaxX());
		assertEquals(0, this.r.getMaxY());
	}

	@Test
	@Override
	public void translateIntInt() {
		this.r.translate(3,  4);
		assertEquals(8, this.r.getMinX());
		assertEquals(12, this.r.getMinY());
		assertEquals(18, this.r.getMaxX());
		assertEquals(17, this.r.getMaxY());
	}

	/**
	 */
	@Test
	@Override
	public void toBoundingBox() {
		Rectangle2i r1 = this.r.toBoundingBox();
		assertSame(this.r, r1);
	}

	/**
	 */
	@Test
	public void getPointIterator() {
		Iterator<Point2i> iterator = this.r.getPointIterator();
		Point2i p;
		
		int[] coords;
		
		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(8, p.iy());
		}

		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(y, p.iy());
		}

		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(13, p.iy());
		}

		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(y, p.iy());
		}

		assertFalse(iterator.hasNext());
	}

	/**
	 */
	@Test
	public void getPointIteratorSide_Top() {
		Iterator<Point2i> iterator = this.r.getPointIterator(Side.TOP);
		Point2i p;
		
		int[] coords;
		
		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(8, p.iy());
		}

		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(y, p.iy());
		}

		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(13, p.iy());
		}

		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(y, p.iy());
		}

		assertFalse(iterator.hasNext());
	}

	/**
	 */
	@Test
	public void getPointIteratorSide_Right() {
		Iterator<Point2i> iterator = this.r.getPointIterator(Side.RIGHT);
		Point2i p;
		
		int[] coords;
		
		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(y, p.iy());
		}

		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(13, p.iy());
		}

		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(y, p.iy());
		}

		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(8, p.iy());
		}

		assertFalse(iterator.hasNext());
	}

	/**
	 */
	@Test
	public void getPointIteratorSide_Bottom() {
		Iterator<Point2i> iterator = this.r.getPointIterator(Side.BOTTOM);
		Point2i p;
		
		int[] coords;
		
		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(13, p.iy());
		}

		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(y, p.iy());
		}

		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(8, p.iy());
		}

		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(y, p.iy());
		}

		assertFalse(iterator.hasNext());
	}

	/**
	 */
	@Test
	public void getPointIteratorSide_Left() {
		Iterator<Point2i> iterator = this.r.getPointIterator(Side.LEFT);
		Point2i p;
		
		int[] coords;
		
		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(y, p.iy());
		}

		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(8, p.iy());
		}

		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(y, p.iy());
		}

		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(13, p.iy());
		}

		assertFalse(iterator.hasNext());
	}

	@Test
	@Override
	public void distancePoint2D() {
		assertEpsilonEquals(0f, this.r.distance(new Point2i(5,8)));
		assertEpsilonEquals(0f, this.r.distance(new Point2i(10,10)));
		assertEpsilonEquals(1f, this.r.distance(new Point2i(4,8)));
		assertEpsilonEquals(9.433981132f, this.r.distance(new Point2i(0,0)));
	}

	/**
	 */
	@Test
	@Override
	public void distanceSquaredPoint2D() {
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2i(5,8)));
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2i(10,10)));
		assertEpsilonEquals(1f, this.r.distanceSquared(new Point2i(4,8)));
		assertEpsilonEquals(89f, this.r.distanceSquared(new Point2i(0,0)));
	}

	/**
	 */
	@Test
	@Override
	public void distanceL1Point2D() {
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2i(5,8)));
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2i(10,10)));
		assertEpsilonEquals(1f, this.r.distanceL1(new Point2i(4,8)));
		assertEpsilonEquals(13f, this.r.distanceL1(new Point2i(0,0)));
	}
	
	/**
	 */
	@Test
	@Override
	public void distanceLinfPoint2D() {
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2i(5,8)));
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2i(10,10)));
		assertEpsilonEquals(1f, this.r.distanceLinf(new Point2i(4,8)));
		assertEpsilonEquals(8f, this.r.distanceLinf(new Point2i(0,0)));
	}
	
	/**
	 */
	@Test
	public void getClosestPointToPoint2D() {
		Point2i p;
		
		p = this.r.getClosestPointTo(new Point2i(5,8));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = this.r.getClosestPointTo(new Point2i(10,10));
		assertNotNull(p);
		assertEquals(10, p.ix());
		assertEquals(10, p.iy());
		
		p = this.r.getClosestPointTo(new Point2i(4,8));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = this.r.getClosestPointTo(new Point2i(0,0));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
	}

	/**
	 */
	@Test
	public void getFarthestPointToPoint2D() {
		Point2i p;
		
		p = this.r.getFarthestPointTo(new Point2i(5,8));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = this.r.getFarthestPointTo(new Point2i(10,10));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = this.r.getFarthestPointTo(new Point2i(4,8));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = this.r.getFarthestPointTo(new Point2i(0,0));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());

		p = this.r.getFarthestPointTo(new Point2i(24,0));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(13, p.iy());

		p = this.r.getFarthestPointTo(new Point2i(0,32));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
	}

	/**
	 */
	@Test
	public void containsIntInt() {
		assertFalse(this.r.contains(0,0));
		assertTrue(this.r.contains(11,10));
		assertFalse(this.r.contains(11,50));
	}

	/**
	 */
	@Test
	public void containsRectangle2i() {
		assertFalse(this.r.contains(new Rectangle2i(0,0,1,1)));
		assertFalse(this.r.contains(new Rectangle2i(0,0,8,1)));
		assertFalse(this.r.contains(new Rectangle2i(0,0,8,6)));
		assertFalse(this.r.contains(new Rectangle2i(0,0,100,100)));
		assertTrue(this.r.contains(new Rectangle2i(7,10,1,1)));
		assertFalse(this.r.contains(new Rectangle2i(16,0,100,100)));
	}

	/**
	 */
	@Test
	public void intersectsRectangle2i() {
		assertFalse(this.r.intersects(new Rectangle2i(0,0,1,1)));
		assertFalse(this.r.intersects(new Rectangle2i(0,0,8,1)));
		assertFalse(this.r.intersects(new Rectangle2i(0,0,8,6)));
		assertTrue(this.r.intersects(new Rectangle2i(0,0,100,100)));
		assertTrue(this.r.intersects(new Rectangle2i(7,10,1,1)));
		assertFalse(this.r.intersects(new Rectangle2i(16,0,100,100)));
	}

	/**
	 */
	@Test
	public void intersectsSegment2i() {
		assertFalse(this.r.intersects(new Segment2i(0,0,1,1)));
		assertFalse(this.r.intersects(new Segment2i(0,0,8,1)));
		assertFalse(this.r.intersects(new Segment2i(0,0,8,6)));
		assertTrue(this.r.intersects(new Segment2i(0,0,100,100)));
		assertTrue(this.r.intersects(new Segment2i(7,10,1,1)));
		assertFalse(this.r.intersects(new Segment2i(16,0,100,100)));
	}

	/**
	 */
	@Test
	public void getPathIteratorVoid() {
		PathIterator2i pi = this.r.getPathIteratorDiscrete();
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
	@Test
	public void getPathIteratorTransform2D() {
		Transform2D tr;
		PathIterator2i pi;
		
		tr = new Transform2D();
		pi = this.r.getPathIteratorDiscrete(tr);
		assertElement(pi, PathElementType.MOVE_TO, 5,8);
		assertElement(pi, PathElementType.LINE_TO, 15,8);
		assertElement(pi, PathElementType.LINE_TO, 15,13);
		assertElement(pi, PathElementType.LINE_TO, 5,13);
		assertElement(pi, PathElementType.LINE_TO, 5,8);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(3.4f, 4.5f);
		pi = this.r.getPathIteratorDiscrete(tr);
		assertElement(pi, PathElementType.MOVE_TO, 8,12);
		assertElement(pi, PathElementType.LINE_TO, 18,12);
		assertElement(pi, PathElementType.LINE_TO, 18,17);
		assertElement(pi, PathElementType.LINE_TO, 8,17);
		assertElement(pi, PathElementType.LINE_TO, 8,12);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		
		pi = this.r.getPathIteratorDiscrete(tr);
		assertElement(pi, PathElementType.MOVE_TO, -2,9);
		assertElement(pi, PathElementType.LINE_TO, 4,16);
		assertElement(pi, PathElementType.LINE_TO, 1,19);
		assertElement(pi, PathElementType.LINE_TO, -5,12);
		assertElement(pi, PathElementType.LINE_TO, -2,9);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	/**
	 */
	@Test
	public void setShape2i() {
		this.r.set(new Circle2i(10, 12, 14));
		assertEquals(-4, this.r.getMinX());
		assertEquals(-2, this.r.getMinY());
		assertEquals(24, this.r.getMaxX());
		assertEquals(26, this.r.getMaxY());
	}

}