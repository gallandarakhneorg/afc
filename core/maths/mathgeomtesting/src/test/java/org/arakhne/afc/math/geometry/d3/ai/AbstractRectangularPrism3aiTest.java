/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3.ai;

import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Test;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.ai.RectangularPrism3ai.Side;

@SuppressWarnings("all")
public abstract class AbstractRectangularPrism3aiTest<T extends RectangularPrism3ai<?, T, ?, ?, ?, T>>
		extends AbstractPrism3aiTest<T, T> {

	@Override
	protected final T createShape() {
		return createRectangularPrism(5, 8, 0, 10, 5, 0);
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
		assertFalse(this.shape.equals(createRectangularPrism(0, 0, 0, 5, 5, 0)));
		assertFalse(this.shape.equals(createRectangularPrism(5, 8, 0, 10, 6, 0)));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 10, 5, 0)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createRectangularPrism(5, 8, 0, 10, 5, 0)));
	}

	@Test
	@Override
	public void equalsObject_withPathIterator() {
		assertFalse(this.shape.equals(createRectangularPrism(0, 0, 0, 5, 5, 0).getPathIterator()));
		assertFalse(this.shape.equals(createRectangularPrism(5, 8, 0, 10, 6, 0).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 10, 5, 0).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createRectangularPrism(5, 8, 0, 10, 5, 0).getPathIterator()));
	}

	@Test
	@Override
	public void equalsToShape() {
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape(createRectangularPrism(0, 0, 0, 5, 5, 0)));
		assertFalse(this.shape.equalsToShape(createRectangularPrism(5, 8, 0, 10, 6, 0)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape(createRectangularPrism(5, 8, 0, 10, 5, 0)));
	}

	@Test
	@Override
	public void equalsToPathIterator() {
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createRectangularPrism(0, 0, 0, 5, 5, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createRectangularPrism(5, 8, 0, 10, 6, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 0, 10, 5, 0).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createRectangularPrism(5, 8, 0, 10, 5, 0).getPathIterator()));
	}

	@Override
	@Test
	@Ignore
	public void getPointIterator() {
		Iterator<? extends Point3D> iterator = this.shape.getPointIterator();
		Point3D p;
		
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
	@Ignore
	public void getPointIteratorSide_Top() {
		Iterator<? extends Point3D> iterator = this.shape.getPointIterator(Side.TOP);
		Point3D p;
		
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
	@Ignore
	public void getPointIteratorSide_Right() {
		Iterator<? extends Point3D> iterator = this.shape.getPointIterator(Side.RIGHT);
		Point3D p;
		
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
	@Ignore
	public void getPointIteratorSide_Bottom() {
		Iterator<? extends Point3D> iterator = this.shape.getPointIterator(Side.BOTTOM);
		Point3D p;
		
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
	@Ignore
	public void getPointIteratorSide_Left() {
		Iterator<? extends Point3D> iterator = this.shape.getPointIterator(Side.LEFT);
		Point3D p;
		
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
	@Ignore
	public void getDistance() {
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(5,8, 0)));
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(10,10, 0)));
		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(4,8, 0)));
		assertEpsilonEquals(9.433981132f, this.shape.getDistance(createPoint(0,0, 0)));
	}

	@Test
	@Override
	@Ignore
	public void getDistanceSquared() {
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(5,8, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(10,10, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(4,8, 0)));
		assertEpsilonEquals(89f, this.shape.getDistanceSquared(createPoint(0,0, 0)));
	}

	@Test
	@Override
	public void getDistanceL1() {
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(5,8, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(10,10, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(4,8, 0)));
		assertEpsilonEquals(13f, this.shape.getDistanceL1(createPoint(0,0, 0)));
	}
	
	@Test
	@Override
	@Ignore
	public void getDistanceLinf() {
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(5,8, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(10,10, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(4,8, 0)));
		assertEpsilonEquals(8f, this.shape.getDistanceLinf(createPoint(0,0, 0)));
	}
	
	@Test
	@Override
	public void getClosestPointTo() {
		Point3D p;
		
		p = this.shape.getClosestPointTo(createPoint(5,8, 0));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = this.shape.getClosestPointTo(createPoint(10,10, 0));
		assertNotNull(p);
		assertEquals(10, p.ix());
		assertEquals(10, p.iy());
		
		p = this.shape.getClosestPointTo(createPoint(4,8, 0));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = this.shape.getClosestPointTo(createPoint(0,0, 0));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
	}

	@Test
	@Override
	public void getFarthestPointTo() {
		Point3D p;
		
		p = this.shape.getFarthestPointTo(createPoint(5,8, 0));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(10,10, 0));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(4,8, 0));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(0,0, 0));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(24,0, 0));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(13, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(0,32, 0));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(8, p.iy());
	}

	@Test
	@Override
	public void containsIntInt() {
		assertFalse(this.shape.contains(0,0, 0));
		assertTrue(this.shape.contains(11,10, 0));
		assertFalse(this.shape.contains(11,50, 0));
	}

	@Test
	@Override
	public void containsRectangularPrism3ai() {
		assertFalse(this.shape.contains(createRectangularPrism(0,0, 0,1,1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(0,0, 0,8,1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(0,0, 0,8,6, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(0,0, 0,100,100, 0)));
		assertTrue(this.shape.contains(createRectangularPrism(7,10, 0,1,1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(16,0, 0,100,100, 0)));
	}

	@Test
	@Override
	@Ignore
	public void intersectsRectangularPrism3ai() {
		assertFalse(this.shape.intersects(createRectangularPrism(0,0, 0,1,1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(0,0, 0,8,1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(0,0, 0,8,6, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(0,0, 0,100,100, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(7,10, 0,1,1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(16,0, 0,100,100, 0)));
	}

	@Override
	public void intersectsSphere3ai() {
		assertFalse(this.shape.intersects(createSphere(0,0,0,1)));
		assertFalse(this.shape.intersects(createSphere(0,0,0,8)));
		assertTrue(this.shape.intersects(createSphere(0,0,0,100)));
		assertTrue(this.shape.intersects(createSphere(7,10,0,1)));
		assertFalse(this.shape.intersects(createSphere(16,0,0,5)));
		assertFalse(this.shape.intersects(createSphere(5,15,0,1)));
	}

	@Test
	@Override
	public void intersectsSegment3ai() {
		assertFalse(this.shape.intersects(createSegment(0,0,0,1,1,0)));
		assertFalse(this.shape.intersects(createSegment(0,0,0,8,1,0)));
		assertFalse(this.shape.intersects(createSegment(0,0,0,8,6,0)));
		assertTrue(this.shape.intersects(createSegment(0,0,0,100,100,0)));
		assertTrue(this.shape.intersects(createSegment(7,10,0,1,1,0)));
		assertFalse(this.shape.intersects(createSegment(16,0,0,100,100,0)));
	}

	@Override
	@Test
	@Ignore
	public void intersectsPath3ai() {
		Path3ai path = createPath();
		path.moveTo(0, 0, 0);
		path.lineTo(2, 2, 0);
		path.quadTo(3, 0, 0, 4, 3, 0);
		path.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
		path.closePath();
		this.shape = createRectangularPrism(0, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangularPrism(4, 3, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangularPrism(2, 2, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangularPrism(2, 1, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangularPrism(3, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangularPrism(-1, -1, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangularPrism(4, -3, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangularPrism(-3, 4, 0, 1, 1, 0);
		assertFalse(this.shape.intersects(path));
		this.shape = createRectangularPrism(6, -5, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangularPrism(4, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangularPrism(5, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createRectangularPrism(0, -3, 0, 1, 1, 0);
		assertFalse(this.shape.intersects(path));
		this.shape = createRectangularPrism(0, -3, 0, 2, 1, 0);
		assertFalse(this.shape.intersects(path));
	}

	@Test
	@Override
	@Ignore
	public void getPathIterator() {
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5,8, 0);
		assertElement(pi, PathElementType.LINE_TO, 15,8, 0);
		assertElement(pi, PathElementType.LINE_TO, 15,13, 0);
		assertElement(pi, PathElementType.LINE_TO, 5,13, 0);
		assertElement(pi, PathElementType.CLOSE, 5,8, 0);
		assertNoElement(pi);
	}

	@Test
	@Override
	@Ignore
	public void getPathIteratorTransform3D() {
		Transform3D tr;
		PathIterator3ai pi;
		
		tr = new Transform3D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 5,8, 0);
		assertElement(pi, PathElementType.LINE_TO, 15,8, 0);
		assertElement(pi, PathElementType.LINE_TO, 15,13, 0);
		assertElement(pi, PathElementType.LINE_TO, 5,13, 0);
		assertElement(pi, PathElementType.CLOSE, 5,8, 0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4f, 4.5f, 0);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 8,13, 0);
		assertElement(pi, PathElementType.LINE_TO, 18,13, 0);
		assertElement(pi, PathElementType.LINE_TO, 18,18, 0);
		assertElement(pi, PathElementType.LINE_TO, 8,18, 0);
		assertElement(pi, PathElementType.CLOSE, 8,13, 0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, -2,9, 0);
		assertElement(pi, PathElementType.LINE_TO, 5,16, 0);
		assertElement(pi, PathElementType.LINE_TO, 1,20, 0);
		assertElement(pi, PathElementType.LINE_TO, -6,13, 0);
		assertElement(pi, PathElementType.CLOSE, -2,9, 0);
		assertNoElement(pi);
	}

	@Test
	@Override
	@Ignore
	public void createTransformedShape() {
		Transform3D tr;
		PathIterator3ai pi;
		
		tr = new Transform3D();
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5,8);
		assertElement(pi, PathElementType.LINE_TO, 15,8);
		assertElement(pi, PathElementType.LINE_TO, 15,13);
		assertElement(pi, PathElementType.LINE_TO, 5,13);
		assertElement(pi, PathElementType.CLOSE, 5,8);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4f, 4.5f, 0);
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 8,13);
		assertElement(pi, PathElementType.LINE_TO, 18,13);
		assertElement(pi, PathElementType.LINE_TO, 18,18);
		assertElement(pi, PathElementType.LINE_TO, 8,18);
		assertElement(pi, PathElementType.CLOSE, 8,13);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);		
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -2,9);
		assertElement(pi, PathElementType.LINE_TO, 5,16);
		assertElement(pi, PathElementType.LINE_TO, 1,20);
		assertElement(pi, PathElementType.LINE_TO, -6,13);
		assertElement(pi, PathElementType.CLOSE, -2,9);
		assertNoElement(pi);
	}

	@Test
	@Override
	public void setIT() {
		this.shape.set(createRectangularPrism(10, 12, 0, 14, 16, 0));
		assertEquals(10, this.shape.getMinX());
		assertEquals(12, this.shape.getMinY());
		assertEquals(24, this.shape.getMaxX());
		assertEquals(28, this.shape.getMaxY());
	}

	@Test
	@Override
	@Ignore
	public void containsPoint3D() {
		assertFalse(this.shape.contains(createPoint(0,0, 0)));
		assertTrue(this.shape.contains(createPoint(11,10, 0)));
		assertFalse(this.shape.contains(createPoint(11,50, 0)));
	}

	@Test
	@Override
	public void translateVector3D() {
		this.shape.translate(createVector(3, 4, 0));
		assertEquals(8, this.shape.getMinX());
		assertEquals(12, this.shape.getMinY());
		assertEquals(18, this.shape.getMaxX());
		assertEquals(17, this.shape.getMaxY());
	}

	@Test
	public void staticComputeClosestPoint() {
		Point3D p;
		
		p = createPoint(0, 0, 0);
		RectangularPrism3ai.computeClosestPoint(5, 8, 0, 15, 13, 0, 5, 8, 0, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = createPoint(0, 0, 0);
		RectangularPrism3ai.computeClosestPoint(5, 8, 0, 15, 13, 0, 10, 10, 0, p);
		assertNotNull(p);
		assertEquals(10, p.ix());
		assertEquals(10, p.iy());
		
		p = createPoint(0, 0, 0);
		RectangularPrism3ai.computeClosestPoint(5, 8, 0, 15, 13, 0, 4, 8, 0, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = createPoint(0, 0, 0);
		RectangularPrism3ai.computeClosestPoint(5, 8, 0, 15, 13, 0, 0, 0, 0, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
	}

	@Test
	public void staticComputeFarthestPoint() {
		Point3D p;
		
		p = createPoint(0, 0, 0);
		RectangularPrism3ai.computeFarthestPoint(5, 8, 0, 15, 13, 0, 5, 8, 0, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = createPoint(0, 0, 0);
		RectangularPrism3ai.computeFarthestPoint(5, 8, 0, 15, 13, 0, 10, 10, 0, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = createPoint(0, 0, 0);
		RectangularPrism3ai.computeFarthestPoint(5, 8, 0, 15, 13, 0, 4, 8, 0, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = createPoint(0, 0, 0);
		RectangularPrism3ai.computeFarthestPoint(5, 8, 0, 15, 13, 0, 0, 0, 0, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());

		p = createPoint(0, 0, 0);
		RectangularPrism3ai.computeFarthestPoint(5, 8, 0, 15, 13, 0, 24, 0, 0, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(13, p.iy());

		p = createPoint(0, 0, 0);
		RectangularPrism3ai.computeFarthestPoint(5, 8, 0, 15, 13, 0, 0, 32, 0, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(8, p.iy());
	}
	
	@Test
	@Ignore
	public void staticIntersectsRectangleRectangle() {
		assertFalse(RectangularPrism3ai.intersectsRectangleRectangle(5, 8, 0, 15, 13, 0, 0, 0, 0, 1, 1, 0));
		assertFalse(RectangularPrism3ai.intersectsRectangleRectangle(5, 8, 0, 15, 13, 0, 0, 0, 0, 8, 1, 0));
		assertFalse(RectangularPrism3ai.intersectsRectangleRectangle(5, 8, 0, 15, 13, 0, 0, 0, 0, 8, 6, 0));
		assertTrue(RectangularPrism3ai.intersectsRectangleRectangle(5, 8, 0, 15, 13, 0, 0, 0, 0, 100, 100, 0));
		assertTrue(RectangularPrism3ai.intersectsRectangleRectangle(5, 8, 0, 15, 13, 0, 7, 10, 0, 8, 11, 0));
		assertFalse(RectangularPrism3ai.intersectsRectangleRectangle(5, 8, 0, 15, 13, 0, 16, 0, 0, 116, 100, 0));
	}
	
	@Test
	public void staticIntersectsRectangleSegment() {
		assertFalse(RectangularPrism3ai.intersectsRectangleSegment(5, 8, 0, 15, 13, 0, 0, 0, 0, 1, 1, 0));
		assertFalse(RectangularPrism3ai.intersectsRectangleSegment(5, 8, 0, 15, 13, 0, 0, 0, 0, 8, 1, 0));
		assertFalse(RectangularPrism3ai.intersectsRectangleSegment(5, 8, 0, 15, 13, 0, 0, 0, 0, 8, 6, 0));
		assertTrue(RectangularPrism3ai.intersectsRectangleSegment(5, 8, 0, 15, 13, 0, 0, 0, 0, 100, 100, 0));
		assertTrue(RectangularPrism3ai.intersectsRectangleSegment(5, 8, 0, 15, 13, 0, 7, 10, 0, 8, 11, 0));
		assertFalse(RectangularPrism3ai.intersectsRectangleSegment(5, 8, 0, 15, 13, 0, 16, 0, 0, 116, 100, 0));
	}

	@Override
	@Test
	public void inflate() {
		this.shape.inflate(1, 2, 0, 3, 4, 0);
		assertEquals(4, this.shape.getMinX());
		assertEquals(6, this.shape.getMinY());
		assertEquals(18, this.shape.getMaxX());
		assertEquals(17, this.shape.getMaxY());
	}

	@Test
	public void setUnion() {
		this.shape.setUnion(createRectangularPrism(0, 0, 0, 12, 1, 0));
		assertEquals(0, this.shape.getMinX());
		assertEquals(0, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
	}

	@Test
	public void createUnion() {
		T union = this.shape.createUnion(createRectangularPrism(0, 0, 0, 12, 1, 0));
		assertNotSame(this.shape, union);
		assertEquals(0, union.getMinX());
		assertEquals(0, union.getMinY());
		assertEquals(15, union.getMaxX());
		assertEquals(13, union.getMaxY());
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
	}

	@Test
	public void setIntersection_noIntersection() {
		this.shape.setIntersection(createRectangularPrism(0, 0, 0, 12, 1, 0));
		assertTrue(this.shape.isEmpty());
	}

	@Test
	public void setIntersection_intersection() {
		this.shape.setIntersection(createRectangularPrism(0, 0, 0, 7, 10, 0));
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(7, this.shape.getMaxX());
		assertEquals(10, this.shape.getMaxY());
	}

	@Test
	public void createIntersection_noIntersection() {
		T box = this.shape.createIntersection(createRectangularPrism(0, 0, 0, 12, 1, 0));
		assertNotSame(this.shape, box);
		assertTrue(box.isEmpty());
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
	}

	@Test
	public void createIntersection_intersection() {
		//createRectangularPrism(5, 8, 10, 5);
		T box = this.shape.createIntersection(createRectangularPrism(0, 0, 0, 7, 10, 0));
		assertNotSame(this.shape, box);
		assertEquals(5, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(10, box.getMaxY());
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
	}

	@Override
	public void intersectsPathIterator3ai() {
		Path3ai path = createPath();
		path.moveTo(0, 0, 0);
		path.lineTo(2, 2, 0);
		path.quadTo(3, 0, 0, 4, 3, 0);
		path.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
		path.closePath();
		this.shape = createRectangularPrism(0, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createRectangularPrism(4, 3, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createRectangularPrism(2, 2, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createRectangularPrism(2, 1, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createRectangularPrism(3, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createRectangularPrism(-1, -1, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createRectangularPrism(4, -3, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createRectangularPrism(-3, 4, 0, 1, 1, 0);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createRectangularPrism(6, -5, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createRectangularPrism(4, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createRectangularPrism(5, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createRectangularPrism(0, -3, 0, 1, 1, 0);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createRectangularPrism(0, -3, 0, 2, 1, 0);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
	}

	@Override
	public void intersectsShape3D() {
		assertTrue(this.shape.intersects((Shape3D) createSphere(0,0, 0,100)));
		assertTrue(this.shape.intersects((Shape3D) createRectangularPrism(7,10, 0,1,1, 0)));
	}

	@Override
	public void operator_addVector3D() {
		this.shape.operator_add(createVector(3, 4, 0));
		assertEquals(8, this.shape.getMinX());
		assertEquals(12, this.shape.getMinY());
		assertEquals(18, this.shape.getMaxX());
		assertEquals(17, this.shape.getMaxY());
	}

	@Override
	public void operator_plusVector3D() {
		T r = this.shape.operator_plus(createVector(3, 4, 0));
		assertEquals(8, r.getMinX());
		assertEquals(12, r.getMinY());
		assertEquals(18, r.getMaxX());
		assertEquals(17, r.getMaxY());
	}

	@Override
	public void operator_removeVector3D() {
		this.shape.operator_remove(createVector(3, 4, 0));
		assertEquals(2, this.shape.getMinX());
		assertEquals(4, this.shape.getMinY());
		assertEquals(12, this.shape.getMaxX());
		assertEquals(9, this.shape.getMaxY());
	}

	@Override
	public void operator_minusVector3D() {
		T r = this.shape.operator_minus(createVector(3, 4, 0));
		assertEquals(2, r.getMinX());
		assertEquals(4, r.getMinY());
		assertEquals(12, r.getMaxX());
		assertEquals(9, r.getMaxY());
	}

	@Override
	public void operator_multiplyTransform3D() {
		Transform3D tr;
		PathIterator3ai pi;
		
		tr = new Transform3D();
		pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5,8, 0);
		assertElement(pi, PathElementType.LINE_TO, 15,8, 0);
		assertElement(pi, PathElementType.LINE_TO, 15,13, 0);
		assertElement(pi, PathElementType.LINE_TO, 5,13, 0);
		assertElement(pi, PathElementType.CLOSE, 5,8, 0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4f, 4.5f, 0);
		pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 8,13, 0);
		assertElement(pi, PathElementType.LINE_TO, 18,13, 0);
		assertElement(pi, PathElementType.LINE_TO, 18,18, 0);
		assertElement(pi, PathElementType.LINE_TO, 8,18, 0);
		assertElement(pi, PathElementType.CLOSE, 8,13, 0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);		
		pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -2,9, 0);
		assertElement(pi, PathElementType.LINE_TO, 5,16, 0);
		assertElement(pi, PathElementType.LINE_TO, 1,20, 0);
		assertElement(pi, PathElementType.LINE_TO, -6,13, 0);
		assertElement(pi, PathElementType.CLOSE, -2,9, 0);
		assertNoElement(pi);
	}

	@Override
	public void operator_andPoint3D() {
		assertFalse(this.shape.operator_and(createPoint(0,0, 0)));
		assertTrue(this.shape.operator_and(createPoint(11,10, 0)));
		assertFalse(this.shape.operator_and(createPoint(11,50, 0)));
	}

	@Override
	public void operator_andShape3D() {
		assertTrue(this.shape.operator_and(createSphere(0,0, 0,100)));
		assertTrue(this.shape.operator_and(createRectangularPrism(7,10, 0,1,1, 0)));
	}

	@Override
	public void operator_upToPoint3D() {
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(5,8, 0)));
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(10,10, 0)));
		assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(4,8, 0)));
		assertEpsilonEquals(9.433981132f, this.shape.operator_upTo(createPoint(0,0, 0)));
	}

}
