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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai.Side;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractRectangle2aiTest<T extends Rectangle2ai<?, T, ?, ?, T>> extends AbstractRectangularShape2aiTest<T, T> {

	@Override
	protected final T createShape() {
		return createRectangle(5, 8, 10, 5);
	}

	@Test
	@Override
	public void testClone() {
		T clone = this.shape.clone();
		assertNotNull(clone);
		assertNotSame(this.shape, clone);
		assertEquals(this.shape.getClass(), clone.getClass());
		assertEpsilonEquals(5, clone.getMinX());
		assertEpsilonEquals(8, clone.getMinY());
		assertEpsilonEquals(15, clone.getMaxX());
		assertEpsilonEquals(13, clone.getMaxY());
	}

	@Test
	@Override
	public void equalsObject() {
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createRectangle(0, 0, 5, 5)));
		assertFalse(this.shape.equals(createRectangle(5, 8, 10, 6)));
		assertFalse(this.shape.equals(createSegment(5, 8, 10, 5)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createRectangle(5, 8, 10, 5)));
	}

	@Test
	@Override
	public void equalsObject_withPathIterator() {
		assertFalse(this.shape.equals(createRectangle(0, 0, 5, 5).getPathIterator()));
		assertFalse(this.shape.equals(createRectangle(5, 8, 10, 6).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 10, 5).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createRectangle(5, 8, 10, 5).getPathIterator()));
	}

	@Test
	@Override
	public void equalsToShape() {
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape(createRectangle(0, 0, 5, 5)));
		assertFalse(this.shape.equalsToShape(createRectangle(5, 8, 10, 6)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape(createRectangle(5, 8, 10, 5)));
	}

	@Test
	@Override
	public void equalsToPathIterator() {
		assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
		assertFalse(this.shape.equalsToPathIterator(createRectangle(0, 0, 5, 5).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createRectangle(5, 8, 10, 6).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 10, 5).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createRectangle(5, 8, 10, 5).getPathIterator()));
	}

	@Test
	public void getPointIterator() {
		Iterator<? extends Point2D> iterator = this.shape.getPointIterator();
		Point2D p;
		
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

	@Test
	public void getPointIteratorSide_Top() {
		Iterator<? extends Point2D> iterator = this.shape.getPointIterator(Side.TOP);
		Point2D p;
		
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

	@Test
	public void getPointIteratorSide_Right() {
		Iterator<? extends Point2D> iterator = this.shape.getPointIterator(Side.RIGHT);
		Point2D p;
		
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

	@Test
	public void getPointIteratorSide_Bottom() {
		Iterator<? extends Point2D> iterator = this.shape.getPointIterator(Side.BOTTOM);
		Point2D p;
		
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

	@Test
	public void getPointIteratorSide_Left() {
		Iterator<? extends Point2D> iterator = this.shape.getPointIterator(Side.LEFT);
		Point2D p;
		
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
	public void getDistance() {
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(5,8)));
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(10,10)));
		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(4,8)));
		assertEpsilonEquals(9.433981132f, this.shape.getDistance(createPoint(0,0)));
	}

	@Test
	@Override
	public void getDistanceSquared() {
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(5,8)));
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(10,10)));
		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(4,8)));
		assertEpsilonEquals(89f, this.shape.getDistanceSquared(createPoint(0,0)));
	}

	@Test
	@Override
	public void getDistanceL1() {
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(5,8)));
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(10,10)));
		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(4,8)));
		assertEpsilonEquals(13f, this.shape.getDistanceL1(createPoint(0,0)));
	}
	
	@Test
	@Override
	public void getDistanceLinf() {
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(5,8)));
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(10,10)));
		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(4,8)));
		assertEpsilonEquals(8f, this.shape.getDistanceLinf(createPoint(0,0)));
	}
	
	@Test
	@Override
	public void getClosestPointTo() {
		Point2D p;
		
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
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = this.shape.getClosestPointTo(createPoint(0,0));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
	}

	@Test
	@Override
	public void getFarthestPointTo() {
		Point2D p;
		
		p = this.shape.getFarthestPointTo(createPoint(5,8));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(10,10));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(4,8));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(0,0));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(24,0));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(13, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(0,32));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(8, p.iy());
	}

	@Test
	@Override
	public void containsIntInt() {
		assertFalse(this.shape.contains(0,0));
		assertTrue(this.shape.contains(11,10));
		assertFalse(this.shape.contains(11,50));
	}

	@Test
	@Override
	public void containsRectangle2ai() {
		assertFalse(this.shape.contains(createRectangle(0,0,1,1)));
		assertFalse(this.shape.contains(createRectangle(0,0,8,1)));
		assertFalse(this.shape.contains(createRectangle(0,0,8,6)));
		assertFalse(this.shape.contains(createRectangle(0,0,100,100)));
		assertTrue(this.shape.contains(createRectangle(7,10,1,1)));
		assertFalse(this.shape.contains(createRectangle(16,0,100,100)));
	}

	@Test
	@Override
	public void intersectsRectangle2ai() {
		assertFalse(this.shape.intersects(createRectangle(0,0,1,1)));
		assertFalse(this.shape.intersects(createRectangle(0,0,8,1)));
		assertFalse(this.shape.intersects(createRectangle(0,0,8,6)));
		assertTrue(this.shape.intersects(createRectangle(0,0,100,100)));
		assertTrue(this.shape.intersects(createRectangle(7,10,1,1)));
		assertFalse(this.shape.intersects(createRectangle(16,0,100,100)));
	}

	@Override
	public void intersectsCircle2ai() {
		assertFalse(this.shape.intersects(createCircle(0,0,1)));
		assertFalse(this.shape.intersects(createCircle(0,0,8)));
		assertTrue(this.shape.intersects(createCircle(0,0,100)));
		assertTrue(this.shape.intersects(createCircle(7,10,1)));
		assertFalse(this.shape.intersects(createCircle(16,0,5)));
		assertFalse(this.shape.intersects(createCircle(5,15,1)));
	}

	@Test
	@Override
	public void intersectsSegment2ai() {
		assertFalse(this.shape.intersects(createSegment(0,0,1,1)));
		assertFalse(this.shape.intersects(createSegment(0,0,8,1)));
		assertFalse(this.shape.intersects(createSegment(0,0,8,6)));
		assertTrue(this.shape.intersects(createSegment(0,0,100,100)));
		assertTrue(this.shape.intersects(createSegment(7,10,1,1)));
		assertFalse(this.shape.intersects(createSegment(16,0,100,100)));
	}

	@Test
	public void intersectsPath2ai() {
		Path2ai path = createPath();
		path.moveTo(0, 0);
		path.lineTo(2, 2);
		path.quadTo(3, 0, 4, 3);
		path.curveTo(5, -1, 6, 5, 7, -5);
		path.closePath();
		this.shape = createRectangle(0, 0, 1, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangle(4, 3, 1, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangle(2, 2, 1, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangle(2, 1, 1, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangle(3, 0, 1, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangle(-1, -1, 1, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangle(4, -3, 1, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangle(-3, 4, 1, 1);
		assertFalse(this.shape.intersects(path));
		this.shape = createRectangle(6, -5, 1, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangle(4, 0, 1, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangle(5, 0, 1, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangle(0, -3, 1, 1);
		assertFalse(this.shape.intersects(path));
		this.shape = createRectangle(0, -3, 2, 1);
		assertFalse(this.shape.intersects(path));
	}

	@Test
	@Override
	public void getPathIterator() {
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5,8);
		assertElement(pi, PathElementType.LINE_TO, 15,8);
		assertElement(pi, PathElementType.LINE_TO, 15,13);
		assertElement(pi, PathElementType.LINE_TO, 5,13);
		assertElement(pi, PathElementType.LINE_TO, 5,8);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	@Test
	@Override
	public void getPathIteratorTransform2D() {
		Transform2D tr;
		PathIterator2ai pi;
		
		tr = new Transform2D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 5,8);
		assertElement(pi, PathElementType.LINE_TO, 15,8);
		assertElement(pi, PathElementType.LINE_TO, 15,13);
		assertElement(pi, PathElementType.LINE_TO, 5,13);
		assertElement(pi, PathElementType.LINE_TO, 5,8);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(3.4f, 4.5f);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 8,13);
		assertElement(pi, PathElementType.LINE_TO, 18,13);
		assertElement(pi, PathElementType.LINE_TO, 18,18);
		assertElement(pi, PathElementType.LINE_TO, 8,18);
		assertElement(pi, PathElementType.LINE_TO, 8,13);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, -2,9);
		assertElement(pi, PathElementType.LINE_TO, 5,16);
		assertElement(pi, PathElementType.LINE_TO, 1,20);
		assertElement(pi, PathElementType.LINE_TO, -6,13);
		assertElement(pi, PathElementType.LINE_TO, -2,9);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	@Test
	@Override
	public void createTransformedShape() {
		Transform2D tr;
		PathIterator2ai pi;
		
		tr = new Transform2D();
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5,8);
		assertElement(pi, PathElementType.LINE_TO, 15,8);
		assertElement(pi, PathElementType.LINE_TO, 15,13);
		assertElement(pi, PathElementType.LINE_TO, 5,13);
		assertElement(pi, PathElementType.LINE_TO, 5,8);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(3.4f, 4.5f);
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 8,13);
		assertElement(pi, PathElementType.LINE_TO, 18,13);
		assertElement(pi, PathElementType.LINE_TO, 18,18);
		assertElement(pi, PathElementType.LINE_TO, 8,18);
		assertElement(pi, PathElementType.LINE_TO, 8,13);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);		
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -2,9);
		assertElement(pi, PathElementType.LINE_TO, 5,16);
		assertElement(pi, PathElementType.LINE_TO, 1,20);
		assertElement(pi, PathElementType.LINE_TO, -6,13);
		assertElement(pi, PathElementType.LINE_TO, -2,9);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	@Test
	@Override
	public void setIT() {
		this.shape.set(createRectangle(10, 12, 14, 16));
		assertEquals(10, this.shape.getMinX());
		assertEquals(12, this.shape.getMinY());
		assertEquals(24, this.shape.getMaxX());
		assertEquals(28, this.shape.getMaxY());
	}

	@Test
	@Override
	public void containsPoint2D() {
		assertFalse(this.shape.contains(createPoint(0,0)));
		assertTrue(this.shape.contains(createPoint(11,10)));
		assertFalse(this.shape.contains(createPoint(11,50)));
	}

	@Test
	@Override
	public void translateVector2D() {
		this.shape.translate(createVector(3, 4));
		assertEquals(8, this.shape.getMinX());
		assertEquals(12, this.shape.getMinY());
		assertEquals(18, this.shape.getMaxX());
		assertEquals(17, this.shape.getMaxY());
	}

	@Test
	public void staticComputeClosestPoint() {
		Point2D p;
		
		p = createPoint(0, 0);
		Rectangle2ai.computeClosestPoint(5, 8, 15, 13, 5, 8, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = createPoint(0, 0);
		Rectangle2ai.computeClosestPoint(5, 8, 15, 13, 10, 10, p);
		assertNotNull(p);
		assertEquals(10, p.ix());
		assertEquals(10, p.iy());
		
		p = createPoint(0, 0);
		Rectangle2ai.computeClosestPoint(5, 8, 15, 13, 4, 8, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = createPoint(0, 0);
		Rectangle2ai.computeClosestPoint(5, 8, 15, 13, 0, 0, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
	}

	@Test
	public void staticComputeFarthestPoint() {
		Point2D p;
		
		p = createPoint(0, 0);
		Rectangle2ai.computeFarthestPoint(5, 8, 15, 13, 5, 8, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = createPoint(0, 0);
		Rectangle2ai.computeFarthestPoint(5, 8, 15, 13, 10, 10, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = createPoint(0, 0);
		Rectangle2ai.computeFarthestPoint(5, 8, 15, 13, 4, 8, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = createPoint(0, 0);
		Rectangle2ai.computeFarthestPoint(5, 8, 15, 13, 0, 0, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());

		p = createPoint(0, 0);
		Rectangle2ai.computeFarthestPoint(5, 8, 15, 13, 24, 0, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(13, p.iy());

		p = createPoint(0, 0);
		Rectangle2ai.computeFarthestPoint(5, 8, 15, 13, 0, 32, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(8, p.iy());
	}
	
	@Test
	public void staticIntersectsRectangleRectangle() {
		assertFalse(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 0, 0, 1, 1));
		assertFalse(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 0, 0, 8, 1));
		assertFalse(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 0, 0, 8, 6));
		assertTrue(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 0, 0, 100, 100));
		assertTrue(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 7, 10, 8, 11));
		assertFalse(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 16, 0,116, 100));
	}
	
	@Test
	public void staticIntersectsRectangleSegment() {
		assertFalse(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 0, 0, 1, 1));
		assertFalse(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 0, 0, 8, 1));
		assertFalse(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 0, 0, 8, 6));
		assertTrue(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 0, 0, 100, 100));
		assertTrue(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 7, 10, 8, 11));
		assertFalse(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 16, 0, 116, 100));
	}

	@Test
	public void inflate() {
		this.shape.inflate(1, 2, 3, 4);
		assertEquals(4, this.shape.getMinX());
		assertEquals(6, this.shape.getMinY());
		assertEquals(18, this.shape.getMaxX());
		assertEquals(17, this.shape.getMaxY());
	}

}