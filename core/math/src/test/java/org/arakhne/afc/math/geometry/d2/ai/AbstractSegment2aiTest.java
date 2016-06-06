/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.ai;

import static org.arakhne.afc.math.MathConstants.SHAPE_INTERSECTS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;

import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractSegment2aiTest<T extends Segment2ai<?, T, ?, ?, ?, B>,
		B extends Rectangle2ai<?, ?, ?, ?, ?, B>> extends AbstractShape2aiTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createSegment(0, 0, 10, 5);
	}
	
	@Test
	@Override
	public void testClone() {
		T clone = this.shape.clone();
		assertNotNull(clone);
		assertNotSame(this.shape, clone);
		assertEquals(this.shape.getClass(), clone.getClass());
		assertEpsilonEquals(0, clone.getX1());
		assertEpsilonEquals(0, clone.getY1());
		assertEpsilonEquals(10, clone.getX2());
		assertEpsilonEquals(5, clone.getY2());
	}

	@Test
	@Override
	public void equalsObject() {
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createSegment(0, 0, 5, 5)));
		assertFalse(this.shape.equals(createSegment(0, 0, 10, 6)));
		assertFalse(this.shape.equals(createRectangle(0, 0, 10, 5)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createSegment(0, 0, 10, 5)));
	}

	@Test
	@Override
	public void equalsObject_withPathIterator() {
		assertFalse(this.shape.equals(createSegment(0, 0, 5, 5).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(0, 0, 10, 6).getPathIterator()));
		assertFalse(this.shape.equals(createRectangle(0, 0, 10, 5).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createSegment(0, 0, 10, 5).getPathIterator()));
	}

	@Test
	@Override
	public void equalsToShape() {
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createSegment(0, 0, 5, 5)));
		assertFalse(this.shape.equalsToShape((T) createSegment(0, 0, 10, 6)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createSegment(0, 0, 10, 5)));
	}

	@Test
	@Override
	public void equalsToPathIterator() {
		assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
		assertFalse(this.shape.equalsToPathIterator(createSegment(0, 0, 5, 5).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(0, 0, 10, 6).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createRectangle(0, 0, 10, 5).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createSegment(0, 0, 10, 5).getPathIterator()));
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
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(0, this.shape.getX2());
		assertEquals(0, this.shape.getY2());
	}

	@Test
	@Override
	public void getDistance() {
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(1, 1)));
		
		assertEpsilonEquals(2.828427125f, this.shape.getDistance(createPoint(2, 4)));

		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(2, 2)));

		assertEpsilonEquals(7.071067812f, this.shape.getDistance(createPoint(-5, 5)));
	}

	@Test
	@Override
	public void getDistanceSquared() {
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(1, 1)));
		
		assertEpsilonEquals(8f, this.shape.getDistanceSquared(createPoint(2, 4)));

		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(2, 2)));

		assertEpsilonEquals(50f, this.shape.getDistanceSquared(createPoint(-5, 5)));
	}

	@Test
	@Override
	public void getDistanceL1() {
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(1, 1)));
		
		assertEpsilonEquals(4f, this.shape.getDistanceL1(createPoint(2, 4)));

		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(2, 2)));

		assertEpsilonEquals(10f, this.shape.getDistanceL1(createPoint(-5, 5)));
	}

	@Test
	@Override
	public void getDistanceLinf() {
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(1, 1)));
		
		assertEpsilonEquals(2f, this.shape.getDistanceLinf(createPoint(2, 4)));

		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(2, 2)));

		assertEpsilonEquals(5f, this.shape.getDistanceLinf(createPoint(-5, 5)));
	}

	@Test
	@Override
	public void translateIntInt() {
		this.shape.translate(3, 4);
		assertEquals(3, this.shape.getX1());
		assertEquals(4, this.shape.getY1());
		assertEquals(13, this.shape.getX2());
		assertEquals(9, this.shape.getY2());
	}

	@Test
	@Override
	public void translateVector2D() {
		this.shape.translate(createVector(3, 4));
		assertEquals(3, this.shape.getX1());
		assertEquals(4, this.shape.getY1());
		assertEquals(13, this.shape.getX2());
		assertEquals(9, this.shape.getY2());
	}

	@Test
	public void setIntIntIntInt() {
		this.shape.set(3, 4, 5, 6);
		assertEquals(3, this.shape.getX1());
		assertEquals(4, this.shape.getY1());
		assertEquals(5, this.shape.getX2());
		assertEquals(6, this.shape.getY2());
	}

	@Test
	public void setPoint2DPoint2D() {
		this.shape.set(createPoint(3,  4), createPoint(5, 6));
		assertEpsilonEquals(3, this.shape.getX1());
		assertEpsilonEquals(4, this.shape.getY1());
		assertEpsilonEquals(5, this.shape.getX2());
		assertEpsilonEquals(6, this.shape.getY2());
	}

	@Test
	@Override
	public void toBoundingBox() {
		B bb = this.shape.toBoundingBox();
		assertEpsilonEquals(0, bb.getMinX());
		assertEpsilonEquals(0, bb.getMinY());
		assertEpsilonEquals(10, bb.getMaxX());
		assertEpsilonEquals(5, bb.getMaxY());
	}

	@Test
	@Override
	public void containsPoint2D() {
		assertTrue(this.shape.contains(createPoint(0, 0)));
		assertTrue(this.shape.contains(createPoint(10, 5)));
		
		assertFalse(this.shape.contains(createPoint(1, 1)));
		assertFalse(this.shape.contains(createPoint(2, 4)));

		assertFalse(this.shape.contains(createPoint(2, 2)));

		assertTrue(this.shape.contains(createPoint(1, 0)));

		assertFalse(this.shape.contains(createPoint(5, 3)));
		assertTrue(this.shape.contains(createPoint(5, 2)));
	}

	@Test
	@Override
	public void containsIntInt() {
		assertTrue(this.shape.contains(0, 0));
		assertTrue(this.shape.contains(10, 5));
		
		assertFalse(this.shape.contains(1, 1));
		assertFalse(this.shape.contains(2, 4));

		assertFalse(this.shape.contains(2, 2));

		assertTrue(this.shape.contains(1, 0));

		assertFalse(this.shape.contains(5, 3));
		assertTrue(this.shape.contains(5, 2));
	}

	@Test
	@Override
	public void getClosestPointTo() {
		Point2D p;
		
		p = this.shape.getClosestPointTo(createPoint(0,0));
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = this.shape.getClosestPointTo(createPoint(1,1));
		assertEquals(2, p.ix());
		assertEquals(1, p.iy());

		p = this.shape.getClosestPointTo(createPoint(2,2));
		assertEquals(2, p.ix());
		assertEquals(1, p.iy());

		p = this.shape.getClosestPointTo(createPoint(-2,2));
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = this.shape.getClosestPointTo(createPoint(0,1));
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = this.shape.getClosestPointTo(createPoint(10,-1));
		assertEquals(7, p.ix());
		assertEquals(3, p.iy());

		p = this.shape.getClosestPointTo(createPoint(2,4));
		assertEquals(4, p.ix());
		assertEquals(2, p.iy());
	}

	@Test
	@Override
	public void getFarthestPointTo() {
		Point2D p;
		
		p = this.shape.getFarthestPointTo(createPoint(0,0));
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(1,1));
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(2,2));
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(-2,2));
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(0,1));
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(10,-1));
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(2,4));
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());
	}

	@Test
	@Override
	public void getPathIterator() {
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 10, 5);
		assertNoElement(pi);
	}

	@Test
	@Override
	public void getPathIteratorTransform2D() {
		Transform2D tr;
		PathIterator2ai pi;
		
		tr = new Transform2D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0,0);
		assertElement(pi, PathElementType.LINE_TO, 10,5);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(3.4f, 4.5f);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 3, 5);
		assertElement(pi, PathElementType.LINE_TO, 13, 10);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		pi = this.shape.getPathIterator(tr); 
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 4, 11);
		assertNoElement(pi);
	}

	@Test
	@Override
    public void createTransformedShape() {
    	T s;
    	Transform2D tr;
    	
    	tr = new Transform2D();    	
    	s = (T) this.shape.createTransformedShape(tr);
		assertEquals(0, s.getX1());
		assertEquals(0, s.getY1());
		assertEquals(10, s.getX2());
		assertEquals(5, s.getY2());

    	tr = new Transform2D();
    	tr.setTranslation(3.4f, 4.5f);
    	s = (T) this.shape.createTransformedShape(tr);
		assertEquals(3, s.getX1());
		assertEquals(5, s.getY1());
		assertEquals(13, s.getX2());
		assertEquals(10, s.getY2());

    	tr = new Transform2D();
    	tr.setRotation(MathConstants.PI);
    	s = (T) this.shape.createTransformedShape(tr);
		assertEquals(0, s.getX1());
		assertEquals(0, s.getY1());
		assertEquals(-10, s.getX2());
		assertEquals(-5, s.getY2());

    	tr = new Transform2D();
    	tr.setRotation(MathConstants.QUARTER_PI);
    	s = (T) this.shape.createTransformedShape(tr);
		assertEquals(0, s.getX1());
		assertEquals(0, s.getY1());
		assertEquals(4, s.getX2());
		assertEquals(11, s.getY2());
    }

	@Test
    public void transformTransform2D() {
    	T s;
    	Transform2D tr;
    	
    	tr = new Transform2D();
    	s = this.shape.clone();
    	s.transform(tr);
		assertEquals(0, s.getX1());
		assertEquals(0, s.getY1());
		assertEquals(10, s.getX2());
		assertEquals(5, s.getY2());

    	tr = new Transform2D();
    	tr.makeTranslationMatrix(3.4f, 4.5f);
    	s = this.shape.clone();
    	s.transform(tr);
    	assertEquals(3, s.getX1());
    	assertEquals(5, s.getY1());
    	assertEquals(13, s.getX2());
    	assertEquals(10, s.getY2());

    	tr = new Transform2D();
    	tr.makeRotationMatrix(MathConstants.PI);
    	s = this.shape.clone();
    	s.transform(tr);
    	assertEquals(0, s.getX1());
    	assertEquals(0, s.getY1());
    	assertEquals(-10, s.getX2());
    	assertEquals(-5, s.getY2());

    	tr = new Transform2D();
    	tr.makeRotationMatrix(MathConstants.QUARTER_PI);
    	s = this.shape.clone();
    	s.transform(tr);
    	assertEquals(0, s.getX1());
    	assertEquals(0, s.getY1());
    	assertEquals(4, s.getX2());
    	assertEquals(11, s.getY2());
    }
    
	@Test
	@Override
    public void getPointIterator() {
    	Point2D p;
    	Iterator<? extends Point2D> iterator = this.shape.getPointIterator();

    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertNotNull(p);
    	assertEquals(0, p.ix());
    	assertEquals(0, p.iy());

    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertNotNull(p);
    	assertEquals(1, p.ix());
    	assertEquals(0, p.iy());

    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertNotNull(p);
    	assertEquals(2, p.ix());
    	assertEquals(1, p.iy());

    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertNotNull(p);
    	assertEquals(3, p.ix());
    	assertEquals(1, p.iy());

    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertNotNull(p);
    	assertEquals(4, p.ix());
    	assertEquals(2, p.iy());

    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertNotNull(p);
    	assertEquals(5, p.ix());
    	assertEquals(2, p.iy());

    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertNotNull(p);
    	assertEquals(6, p.ix());
    	assertEquals(3, p.iy());

    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertNotNull(p);
    	assertEquals(7, p.ix());
    	assertEquals(3, p.iy());

    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertNotNull(p);
    	assertEquals(8, p.ix());
    	assertEquals(4, p.iy());

    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertNotNull(p);
    	assertEquals(9, p.ix());
    	assertEquals(4, p.iy());

    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertNotNull(p);
    	assertEquals(10, p.ix());
    	assertEquals(5, p.iy());

    	assertFalse(iterator.hasNext());
    }
    
	@Test
    public void staticIntersectsSegmentSegment() {
    	assertTrue(Segment2ai.intersectsSegmentSegment(0, 0, 10, 5, 0, 0, 10, 5));
    	assertTrue(Segment2ai.intersectsSegmentSegment(0, 0, 10, 5, 0, 0, 5, 2));
    	assertFalse(Segment2ai.intersectsSegmentSegment(0, 0, 10, 5, 0, 1, 5, 3));
    	assertFalse(Segment2ai.intersectsSegmentSegment(0, 0, 10, 5, 0, 2, 5, 4));
    	assertTrue(Segment2ai.intersectsSegmentSegment(0, 0, 10, 5, 5, 0, 4, 3));
    	assertFalse(Segment2ai.intersectsSegmentSegment(0, 0, 10, 5, -1, 5, -1, 0));
    	assertTrue(Segment2ai.intersectsSegmentSegment(5, 3, 7, 5, 6, 2, 6, 5));
    	assertTrue(Segment2ai.intersectsSegmentSegment(5, 3, 7, 5, 9, 4, 6, 6));
    	assertFalse(Segment2ai.intersectsSegmentSegment(5, 3, 7, 5, 9, 4, 6, 7));
    	assertTrue(Segment2ai.intersectsSegmentSegment(5, 3, 7, 5, 6, 4, 6, 8));
    }
    
	@Test
    public void staticComputeCrossingsFromPoint() {
    	assertEquals(0, Segment2ai.computeCrossingsFromPoint(0, 5, 3, -1, -1, 0, 0));
    	assertEquals(0, Segment2ai.computeCrossingsFromPoint(0, 5, 3, 4, -2, 4, 10));
    	assertEquals(2, Segment2ai.computeCrossingsFromPoint(0, 5, 3, 6, -2, 6, 10));
    	assertEquals(SHAPE_INTERSECTS, Segment2ai.computeCrossingsFromPoint(0, 5, 3, 5, -2, 5, 10));
    	assertEquals(-2, Segment2ai.computeCrossingsFromPoint(0, 5, 3, 6, 10, 6, -2));
    	assertEquals(SHAPE_INTERSECTS, Segment2ai.computeCrossingsFromPoint(0, 5, 3, 5, 10, 5, -2));
    	assertEquals(2, Segment2ai.computeCrossingsFromPoint(0, 5, 3, 10, -5, 127, 345));
    	assertEquals(-2, Segment2ai.computeCrossingsFromPoint(0, 5, 3, 127, 345, 10, -5));
    	assertEquals(1, Segment2ai.computeCrossingsFromPoint(0, 5, 3, 127, 3, 200, 345));
    	assertEquals(-1, Segment2ai.computeCrossingsFromPoint(0, 5, 3, 127, 345, 200, 3));
    	assertEquals(1, Segment2ai.computeCrossingsFromPoint(0, 5, 3, 10, 1, 12, 3));
    	assertEquals(-1, Segment2ai.computeCrossingsFromPoint(0, 5, 3, 12, 3, 10, 1));
    	assertEquals(1, Segment2ai.computeCrossingsFromPoint(0, 5, 3, 10, 3, 12, 5));
    	assertEquals(-1, Segment2ai.computeCrossingsFromPoint(0, 5, 3, 12, 5, 10, 3));
    	assertEquals(0, Segment2ai.computeCrossingsFromPoint(0, 4, -1, 7, -5, 0, 0));
    }

	@Test
    public void staticComputeCrossingsFromRect() {
    	int[] data = new int[] {
    			-1, -1, 0, 0, 				0,
    			-1, 0, 10, 0,				0,
    			10, -2, 12, 4,				1,
    			12, 4, 50, 10,				1,
    			10, -2, 50, 10,				2,
    			10, 3, 12, 4,				0,
    			12, 4, 50, 5,				0,
    			12, 3, 50, 5,				0,
    			12, 5, 50, 5,				0,
    			12, 4, 50, 10,				1,
    			12, 3, 50, 10,				1,
    			12, 5, 50, 10,				1,
    			0, 5, 3, 7,					0,
    			6, 2, 6, 4,					SHAPE_INTERSECTS,
    			6, 4, 6, 8,					SHAPE_INTERSECTS,
    			7, 4, 7, 8,					SHAPE_INTERSECTS,
    			5, 4, 5, 8,					SHAPE_INTERSECTS,
    			4, 4, 6, 6,					SHAPE_INTERSECTS,
    			6, 6, 8, 4,					SHAPE_INTERSECTS,
    			5, 4, 7, 4,					SHAPE_INTERSECTS,
    			0, 4, 7, 4,					SHAPE_INTERSECTS,
    			6, 6, 12, 8,				0,
    			6, 2, 12, -3,				0,
    	};
    	
    	String label;
    	for(int i=0; i<data.length;) {
    		int x1 = data[i++];
    		int y1 = data[i++];
    		int x2 = data[i++];
    		int y2 = data[i++];
    		int crossing = data[i++];
    		    		
    		label = x1+";"+y1+";"+x2+";"+y2;  
    		assertEquals(label, crossing, Segment2ai.computeCrossingsFromRect(0, 5, 3, 7, 5, x1, y1, x2, y2));

    		label = x2+";"+y2+";"+x1+";"+y1;  
    		if (crossing!=SHAPE_INTERSECTS) {
    			crossing = -crossing;
    		}
    		assertEquals(label, crossing, Segment2ai.computeCrossingsFromRect(0, 5, 3, 7, 5, x2, y2, x1, y1));
    	}
    }

	@Test
    public void staticComputeCrossingsFromSegment() {
    	assertEquals(0, Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ -1, -1, 0, 0));
    	assertEquals(0, Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ -1, 0, 10, 0));
    	assertEquals(1, Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 10, -2, 12, 4));
    	assertEquals(1, Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 12, 4, 50, 10));
    	assertEquals(2, Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 10, -2, 50, 10));
    	assertEquals(0, Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 10, 3, 12, 4));
    	assertEquals(0, Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 12, 4, 50, 5));
    	assertEquals(0, Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 12, 3, 50, 5));
    	assertEquals(1,
    			Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 2, 6, 3));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 2, 6, 5));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 2, 6, 4));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 4, 6, 8));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 3, 6, 8));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 7, 4, 7, 8));
    	assertEquals(0,
    			Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 5, 4, 5, 8));
    	assertEquals(0,
    			Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 4, 4, 6, 6));
    	assertEquals(0,
    			Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 4, 3, 6, 5));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 8, 4, 6, 6));
    	assertEquals(1,
    			Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 10, 4, 6, 8));
    	assertEquals(-1,
    			Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 8, 10, 4));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromSegment(0, /**/ 5, 3, 7, 5, /**/ 5, 4, 100, 6));
    }

	@Test
    public void staticComputeCrossingsFromCircle() {
    	assertEquals(0, Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ -1, -1, 0, 0));
    	assertEquals(0, Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ -1, 0, 10, 0));
    	assertEquals(1, Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 10, -2, 12, 4));
    	assertEquals(1, Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 12, 4, 50, 10));
    	assertEquals(2, Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 10, -2, 50, 10));
    	assertEquals(0, Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 10, 3, 12, 4));
    	assertEquals(0, Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 12, 4, 50, 5));
    	assertEquals(0, Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 12, 3, 50, 5));
    	assertEquals(1,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 6, 2, 6, 3));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 6, 2, 6, 5));
    	assertEquals(1,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 7, 2, 7, 4));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 6, 2, 6, 4));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 6, 4, 6, 8));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 6, 3, 6, 8));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 7, 4, 7, 8));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 5, 4, 5, 8));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 4, 4, 6, 6));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 4, 3, 6, 5));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 8, 4, 6, 6));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 10, 4, 6, 8));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 6, 8, 10, 4));
    	assertEquals(1,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 7, 8, 10, 12));
    	assertEquals(-1,
    			Segment2ai.computeCrossingsFromCircle(0, /**/ 4, 6, 3, /**/ 10, 12, 7, 8));
    }

	@Test
	@Override
	public void setIT() {
		this.shape.set((T) createSegment(10, 12, 14, 16));
		assertEquals(10, this.shape.getX1());
		assertEquals(12, this.shape.getY1());
		assertEquals(14, this.shape.getX2());
		assertEquals(16, this.shape.getY2());
	}

	@Test
	public void setX1() {
		this.shape.setX1(145);
		assertEquals(145, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@Test
	public void setY1() {
		this.shape.setY1(145);
		assertEquals(0, this.shape.getX1());
		assertEquals(145, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@Test
	public void setX2() {
		this.shape.setX2(145);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(145, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@Test
	public void setY2() {
		this.shape.setY2(145);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(145, this.shape.getY2());
	}

	@Test
	public void getX1() {
		assertEquals(0, this.shape.getX1());
	}

	@Test
	public void getY1() {
		assertEquals(0, this.shape.getY1());
	}

	@Test
	public void getX2() {
		assertEquals(10, this.shape.getX2());
	}

	@Test
	public void getY2() {
		assertEquals(5, this.shape.getY2());
	}

	@Test
	public void getP1() {
		Point2D p = this.shape.getP1();
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());
	}

	@Test
	public void getP2() {
		Point2D p = this.shape.getP2();
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());
	}

	@Test
	public void staticComputeClosestPointTo() {
		Point2D p;
		
		p = createPoint(0, 0);
		Segment2ai.computeClosestPointTo(0, 0, 10, 5, 0, 0, p);
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = createPoint(0, 0);
		Segment2ai.computeClosestPointTo(0, 0, 10, 5, 1, 1, p);
		assertEquals(2, p.ix());
		assertEquals(1, p.iy());

		p = createPoint(0, 0);
		Segment2ai.computeClosestPointTo(0, 0, 10, 5, 2, 2, p);
		assertEquals(2, p.ix());
		assertEquals(1, p.iy());

		p = createPoint(0, 0);
		Segment2ai.computeClosestPointTo(0, 0, 10, 5, -2, 2, p);
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = createPoint(0, 0);
		Segment2ai.computeClosestPointTo(0, 0, 10, 5, 0, 1, p);
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = createPoint(0, 0);
		Segment2ai.computeClosestPointTo(0, 0, 10, 5, 10, -1, p);
		assertEquals(7, p.ix());
		assertEquals(3, p.iy());

		p = createPoint(0, 0);
		Segment2ai.computeClosestPointTo(0, 0, 10, 5, 2, 4, p);
		assertEquals(4, p.ix());
		assertEquals(2, p.iy());
	}

	@Test
	public void staticComputeFarthestPointTo() {
		Point2D p;
		
		p = createPoint(0, 0);
		Segment2ai.computeFarthestPointTo(0, 0, 10, 5, 0, 0, p);
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = createPoint(0, 0);
		Segment2ai.computeFarthestPointTo(0, 0, 10, 5, 1, 1, p);
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = createPoint(0, 0);
		Segment2ai.computeFarthestPointTo(0, 0, 10, 5, 2, 2, p);
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = createPoint(0, 0);
		Segment2ai.computeFarthestPointTo(0, 0, 10, 5, -2, 2, p);
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = createPoint(0, 0);
		Segment2ai.computeFarthestPointTo(0, 0, 10, 5, 0, 1, p);
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = createPoint(0, 0);
		Segment2ai.computeFarthestPointTo(0, 0, 10, 5, 10, -1, p);
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = createPoint(0, 0);
		Segment2ai.computeFarthestPointTo(0, 0, 10, 5, 2, 4, p);
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());
	}

	@Test
	public void staticComputeSideLinePoint() {
		assertEquals(-1, Segment2ai.computeSideLinePoint(0, 0, 10, 5, 7, 0));
		assertEquals(1, Segment2ai.computeSideLinePoint(0, 0, 10, 5, 4, 4));
	}

	@Test
	public void setP1Point2D() {
		this.shape.setP1(createPoint(145, 654));
		assertEquals(145, this.shape.getX1());
		assertEquals(654, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@Test
	public void setP2Point2D() {
		this.shape.setP2(createPoint(145, 654));
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(145, this.shape.getX2());
		assertEquals(654, this.shape.getY2());
	}

	@Test
	public void setP1IntInt() {
		this.shape.setP1(145, 654);
		assertEquals(145, this.shape.getX1());
		assertEquals(654, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@Test
	public void setP2IntInt() {
		this.shape.setP2(145, 654);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(145, this.shape.getX2());
		assertEquals(654, this.shape.getY2());
	}

	@Test
	public void toBoundingBoxB() {
		B bb = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(bb);
		assertEpsilonEquals(0, bb.getMinX());
		assertEpsilonEquals(0, bb.getMinY());
		assertEpsilonEquals(10, bb.getMaxX());
		assertEpsilonEquals(5, bb.getMaxY());
	}

	@Test
	public void containsRectangle2ai() {
		assertFalse(this.shape.contains(createRectangle(0,0,1,1)));
		assertFalse(this.shape.contains(createRectangle(0,0,8,1)));
		assertFalse(this.shape.contains(createRectangle(0,0,8,6)));
		assertFalse(this.shape.contains(createRectangle(0,0,100,100)));
		assertFalse(this.shape.contains(createRectangle(7,10,1,1)));
		assertFalse(this.shape.contains(createRectangle(16,0,100,100)));
		assertFalse(this.shape.contains(createRectangle(0,3,3,10)));
		
		this.shape = (T) createSegment(0, 0, 10, 0);
		assertTrue(this.shape.contains(createRectangle(0,0,2,0)));
	}

    @Test
    @Ignore
    public void containsShape2D() {
        assertFalse(this.shape.contains(createCircle(0,0,1)));
        assertFalse(this.shape.contains(createCircle(0,0,8)));
        assertFalse(this.shape.contains(createCircle(0,0,6)));
        assertFalse(this.shape.contains(createCircle(0,0,100)));
        assertFalse(this.shape.contains(createCircle(7,10,1)));
        assertFalse(this.shape.contains(createCircle(16,0,100)));
        assertFalse(this.shape.contains(createCircle(0,3,3)));
        assertTrue(this.shape.contains(createCircle(0,3,10)));
    }

    @Test
	public void clipToRectangle() {
		assertTrue(this.shape.clipToRectangle(3, 1, 7, 6));
		assertEquals(3, this.shape.getX1());
		assertEquals(1, this.shape.getY1());
		assertEquals(7, this.shape.getX2());
		assertEquals(3, this.shape.getY2());
		
		this.shape = createShape();
		assertTrue(this.shape.clipToRectangle(8, 3, 11, 7));
		assertEquals(8, this.shape.getX1());
		assertEquals(4, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());

		this.shape = createShape();
		assertFalse(this.shape.clipToRectangle(0, 3, 5, 4));
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@Test
	public void intersectsRectangle2ai() {
		assertTrue(this.shape.intersects(createRectangle(0,0,1,1)));
		assertTrue(this.shape.intersects(createRectangle(0,0,8,1)));
		assertTrue(this.shape.intersects(createRectangle(0,0,8,6)));
		assertTrue(this.shape.intersects(createRectangle(0,0,100,100)));
		assertFalse(this.shape.intersects(createRectangle(7,10,1,1)));
		assertFalse(this.shape.intersects(createRectangle(16,0,100,100)));
		assertFalse(this.shape.intersects(createRectangle(0,3,3,10)));
	}

	@Test
	public void intersectsCircle2ai() {
		assertTrue(this.shape.intersects(createCircle(0,0,1)));
		assertTrue(this.shape.intersects(createCircle(0,0,8)));
		assertTrue(this.shape.intersects(createCircle(0,0,8)));
		assertTrue(this.shape.intersects(createCircle(0,0,100)));
		assertFalse(this.shape.intersects(createCircle(7,10,1)));
		assertTrue(this.shape.intersects(createCircle(16,0,100)));
		assertTrue(this.shape.intersects(createCircle(0,3,3)));
		assertFalse(this.shape.intersects(createCircle(0,3,1)));
	}

	@Test
	@Override
	public void intersectsSegment2ai() {
		assertTrue(this.shape.intersects(createSegment(0,0,1,10)));
		assertTrue(this.shape.intersects(createSegment(0,0,8,0)));
		assertTrue(this.shape.intersects(createSegment(0,0,8,-1)));
		assertTrue(this.shape.intersects(createSegment(0,0,100,-100)));
		assertFalse(this.shape.intersects(createSegment(7,10,1,3)));
		assertFalse(this.shape.intersects(createSegment(16,0,100,3)));
		assertFalse(this.shape.intersects(createSegment(0,3,3,3)));
		assertFalse(this.shape.intersects(createSegment(0,3,5,3)));
		assertTrue(this.shape.intersects(createSegment(0,3,6,3)));
		assertFalse(this.shape.intersects(createSegment(0,3,1,2)));
	}
	
	@Test
	public void intersectsPath2ai() {
		Path2ai path = createPath();
		path.moveTo(0, 0);
		path.lineTo(2, 2);
		path.quadTo(3, 0, 4, 3);
		path.curveTo(5, -1, 6, 5, 7, -5);
		path.closePath();
		
		assertTrue(createSegment(0, 0, 1, 1).intersects(path));
		assertTrue(createSegment(4, 3, 1, 1).intersects(path));
		assertTrue(createSegment(2, 2, 1, 1).intersects(path));
		assertTrue(createSegment(2, 1, 1, 1).intersects(path));
		assertTrue(createSegment(3, 0, 1, 1).intersects(path));
		assertTrue(createSegment(-1, -1, 1, 1).intersects(path));
		assertTrue(createSegment(4, -3, 1, 1).intersects(path));
		assertTrue(createSegment(-3, 4, 1, 1).intersects(path));
		assertTrue(createSegment(6, -5, 1, 1).intersects(path));
		assertTrue(createSegment(4, 0, 1, 1).intersects(path));
		assertTrue(createSegment(5, 0, 1, 1).intersects(path));
		assertFalse(createSegment(-4, -4, -3, -3).intersects(path));
		assertFalse(createSegment(-1, 0, 2, 3).intersects(path));
		assertFalse(createSegment(7, 1, 18, 14).intersects(path));
	}


	@Override
	public void intersectsPathIterator2ai() {
		Path2ai path = createPath();
		path.moveTo(0, 0);
		path.lineTo(2, 2);
		path.quadTo(3, 0, 4, 3);
		path.curveTo(5, -1, 6, 5, 7, -5);
		path.closePath();
		
		assertTrue(createSegment(0, 0, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		assertTrue(createSegment(4, 3, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		assertTrue(createSegment(2, 2, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		assertTrue(createSegment(2, 1, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		assertTrue(createSegment(3, 0, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		assertTrue(createSegment(-1, -1, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		assertTrue(createSegment(4, -3, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		assertTrue(createSegment(-3, 4, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		assertTrue(createSegment(6, -5, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		assertTrue(createSegment(4, 0, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		assertTrue(createSegment(5, 0, 1, 1).intersects((PathIterator2ai) path.getPathIterator()));
		assertFalse(createSegment(-4, -4, -3, -3).intersects((PathIterator2ai) path.getPathIterator()));
		assertFalse(createSegment(-1, 0, 2, 3).intersects((PathIterator2ai) path.getPathIterator()));
		assertFalse(createSegment(7, 1, 18, 14).intersects((PathIterator2ai) path.getPathIterator()));
	}

	@Override
	public void intersectsShape2D() {
		assertTrue(this.shape.intersects((Shape2D) createCircle(16,0,100)));
		assertTrue(this.shape.intersects((Shape2D) createRectangle(0,0,100,100)));
	}

	@Override
	public void operator_addVector2D() {
		this.shape.operator_add(createVector(3, 4));
		assertEquals(3, this.shape.getX1());
		assertEquals(4, this.shape.getY1());
		assertEquals(13, this.shape.getX2());
		assertEquals(9, this.shape.getY2());
	}

	@Override
	public void operator_plusVector2D() {
		T r = this.shape.operator_plus(createVector(3, 4));
		assertEquals(3, r.getX1());
		assertEquals(4, r.getY1());
		assertEquals(13, r.getX2());
		assertEquals(9, r.getY2());
	}

	@Override
	public void operator_removeVector2D() {
		this.shape.operator_remove(createVector(3, 4));
		assertEquals(-3, this.shape.getX1());
		assertEquals(-4, this.shape.getY1());
		assertEquals(7, this.shape.getX2());
		assertEquals(1, this.shape.getY2());
	}

	@Override
	public void operator_minusVector2D() {
		T r = this.shape.operator_minus(createVector(3, 4));
		assertEquals(-3, r.getX1());
		assertEquals(-4, r.getY1());
		assertEquals(7, r.getX2());
		assertEquals(1, r.getY2());
	}

	@Override
	public void operator_multiplyTransform2D() {
    	T s;
    	Transform2D tr;
    	
    	tr = new Transform2D();    	
    	s = (T) this.shape.operator_multiply(tr);
		assertEquals(0, s.getX1());
		assertEquals(0, s.getY1());
		assertEquals(10, s.getX2());
		assertEquals(5, s.getY2());

    	tr = new Transform2D();
    	tr.setTranslation(3.4f, 4.5f);
    	s = (T) this.shape.operator_multiply(tr);
		assertEquals(3, s.getX1());
		assertEquals(5, s.getY1());
		assertEquals(13, s.getX2());
		assertEquals(10, s.getY2());

    	tr = new Transform2D();
    	tr.setRotation(MathConstants.PI);
    	s = (T) this.shape.operator_multiply(tr);
		assertEquals(0, s.getX1());
		assertEquals(0, s.getY1());
		assertEquals(-10, s.getX2());
		assertEquals(-5, s.getY2());

    	tr = new Transform2D();
    	tr.setRotation(MathConstants.QUARTER_PI);
    	s = (T) this.shape.operator_multiply(tr);
		assertEquals(0, s.getX1());
		assertEquals(0, s.getY1());
		assertEquals(4, s.getX2());
		assertEquals(11, s.getY2());
	}

	@Override
	public void operator_andPoint2D() {
		assertTrue(this.shape.operator_and(createPoint(0, 0)));
		assertTrue(this.shape.operator_and(createPoint(10, 5)));
		
		assertFalse(this.shape.operator_and(createPoint(1, 1)));
		assertFalse(this.shape.operator_and(createPoint(2, 4)));

		assertFalse(this.shape.operator_and(createPoint(2, 2)));

		assertTrue(this.shape.operator_and(createPoint(1, 0)));

		assertFalse(this.shape.operator_and(createPoint(5, 3)));
		assertTrue(this.shape.operator_and(createPoint(5, 2)));
	}

	@Override
	public void operator_andShape2D() {
		assertTrue(this.shape.operator_and(createCircle(16,0,100)));
		assertTrue(this.shape.operator_and(createRectangle(0,0,100,100)));
	}

	@Override
	public void operator_upToPoint2D() {
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(0, 0)));
		assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(1, 1)));
		assertEpsilonEquals(2.828427125f, this.shape.operator_upTo(createPoint(2, 4)));
		assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(2, 2)));
		assertEpsilonEquals(7.071067812f, this.shape.operator_upTo(createPoint(-5, 5)));
	}

	@Test
    @Ignore
	public void getDistanceSquaredRectangle2ai() {
		assert(false);
	}

	@Test
    @Ignore
	public void getDistanceSquaredCircle2ai() {
		assert(false);
	}

	@Test
    @Ignore
	public void getDistanceSquaredSegment2ai() {
		assert(false);
	}
	
	@Test
    @Ignore
	public void getDistanceSquaredPath2ai() {
		assert(false);
	}

	@Test
    @Ignore
	public void getClosestPointToRectangle2ai() {
		assert(false);
	}

	@Test
    @Ignore
	public void getClosestPointToCircle2ai() {
		assert(false);
	}

	@Test
    @Ignore
	public void getClosestPointToSegment2ai() {
		assert(false);
	}
	
	@Test
    @Ignore
	public void getClosestPointToMultiShape2ai() {
		assert(false);
	}
		
	@Test
    @Ignore
	public void getClosestPointToPath2ai() {
		assert(false);
	}

}