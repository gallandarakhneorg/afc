/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.test.geometry.d2.ai;

import static org.arakhne.afc.math.geometry.GeomConstants.SHAPE_INTERSECTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;

@SuppressWarnings("all")
public abstract class AbstractSegment2aiTest<T extends Segment2ai<?, T, ?, ?, ?, B>,
		B extends Rectangle2ai<?, ?, ?, ?, ?, B>> extends AbstractShape2aiTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createSegment(0, 0, 10, 5);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void testClone(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T clone = this.shape.clone();
		assertNotNull(clone);
		assertNotSame(this.shape, clone);
		assertEquals(this.shape.getClass(), clone.getClass());
		assertEpsilonEquals(0, clone.getX1());
		assertEpsilonEquals(0, clone.getY1());
		assertEpsilonEquals(10, clone.getX2());
		assertEpsilonEquals(5, clone.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsObject(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createSegment(0, 0, 5, 5)));
		assertFalse(this.shape.equals(createSegment(0, 0, 10, 6)));
		assertFalse(this.shape.equals(createRectangle(0, 0, 10, 5)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createSegment(0, 0, 10, 5)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsObject_withPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createSegment(0, 0, 5, 5).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(0, 0, 10, 6).getPathIterator()));
		assertFalse(this.shape.equals(createRectangle(0, 0, 10, 5).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createSegment(0, 0, 10, 5).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsToShape(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createSegment(0, 0, 5, 5)));
		assertFalse(this.shape.equalsToShape((T) createSegment(0, 0, 10, 6)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createSegment(0, 0, 10, 5)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsToPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
		assertFalse(this.shape.equalsToPathIterator(createSegment(0, 0, 5, 5).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(0, 0, 10, 6).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createRectangle(0, 0, 10, 5).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createSegment(0, 0, 10, 5).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void isEmpty(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void clear(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(0, this.shape.getX2());
		assertEquals(0, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getDistance(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(1, 1)));
		
		assertEpsilonEquals(2.828427125f, this.shape.getDistance(createPoint(2, 4)));

		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(2, 2)));

		assertEpsilonEquals(7.071067812f, this.shape.getDistance(createPoint(-5, 5)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getDistanceSquared(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(1, 1)));
		
		assertEpsilonEquals(8f, this.shape.getDistanceSquared(createPoint(2, 4)));

		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(2, 2)));

		assertEpsilonEquals(50f, this.shape.getDistanceSquared(createPoint(-5, 5)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getDistanceL1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(1, 1)));
		
		assertEpsilonEquals(4f, this.shape.getDistanceL1(createPoint(2, 4)));

		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(2, 2)));

		assertEpsilonEquals(10f, this.shape.getDistanceL1(createPoint(-5, 5)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getDistanceLinf(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(1, 1)));
		
		assertEpsilonEquals(2f, this.shape.getDistanceLinf(createPoint(2, 4)));

		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(2, 2)));

		assertEpsilonEquals(5f, this.shape.getDistanceLinf(createPoint(-5, 5)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void translateIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.translate(3, 4);
		assertEquals(3, this.shape.getX1());
		assertEquals(4, this.shape.getY1());
		assertEquals(13, this.shape.getX2());
		assertEquals(9, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void translateVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(3, 4));
		assertEquals(3, this.shape.getX1());
		assertEquals(4, this.shape.getY1());
		assertEquals(13, this.shape.getX2());
		assertEquals(9, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setIntIntIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(3, 4, 5, 6);
		assertEquals(3, this.shape.getX1());
		assertEquals(4, this.shape.getY1());
		assertEquals(5, this.shape.getX2());
		assertEquals(6, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setPoint2DPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(createPoint(3,  4), createPoint(5, 6));
		assertEpsilonEquals(3, this.shape.getX1());
		assertEpsilonEquals(4, this.shape.getY1());
		assertEpsilonEquals(5, this.shape.getX2());
		assertEpsilonEquals(6, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void toBoundingBox(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B bb = this.shape.toBoundingBox();
		assertEpsilonEquals(0, bb.getMinX());
		assertEpsilonEquals(0, bb.getMinY());
		assertEpsilonEquals(10, bb.getMaxX());
		assertEpsilonEquals(5, bb.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void containsPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.contains(createPoint(0, 0)));
		assertTrue(this.shape.contains(createPoint(10, 5)));
		
		assertFalse(this.shape.contains(createPoint(1, 1)));
		assertFalse(this.shape.contains(createPoint(2, 4)));

		assertFalse(this.shape.contains(createPoint(2, 2)));

		assertTrue(this.shape.contains(createPoint(1, 0)));

		assertFalse(this.shape.contains(createPoint(5, 3)));
		assertTrue(this.shape.contains(createPoint(5, 2)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void containsIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.contains(0, 0));
		assertTrue(this.shape.contains(10, 5));
		
		assertFalse(this.shape.contains(1, 1));
		assertFalse(this.shape.contains(2, 4));

		assertFalse(this.shape.contains(2, 2));

		assertTrue(this.shape.contains(1, 0));

		assertFalse(this.shape.contains(5, 3));
		assertTrue(this.shape.contains(5, 2));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getClosestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;
		
		p = this.shape.getClosestPointTo(createPoint(0,0));
		assertIntPointEquals(0, 0, p);

		p = this.shape.getClosestPointTo(createPoint(1,1));
		assertIntPointEquals(1, 0, p);

		p = this.shape.getClosestPointTo(createPoint(2,2));
		assertIntPointEquals(2, 1, p);

		p = this.shape.getClosestPointTo(createPoint(-2,2));
		assertIntPointEquals(0, 0, p);

		p = this.shape.getClosestPointTo(createPoint(0,1));
		assertIntPointEquals(0, 0, p);

		p = this.shape.getClosestPointTo(createPoint(10,-1));
		assertIntPointEquals(7, 3, p);

		p = this.shape.getClosestPointTo(createPoint(2,4));
		assertIntPointEquals(4, 2, p);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getFarthestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 10, 5);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getPathIteratorTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
    public void createTransformedShape(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void transformTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
    
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
    public void getPointIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
    
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void staticIntersectsSegmentSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
    
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void staticCalculatesCrossingsPointShadowSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    	assertEquals(0, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, -1, -1, 0, 0));
    	assertEquals(0, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 4, -2, 4, 10));
    	assertEquals(2, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 6, -2, 6, 10));
    	assertEquals(SHAPE_INTERSECTS, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 5, -2, 5, 10));
    	assertEquals(-2, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 6, 10, 6, -2));
    	assertEquals(SHAPE_INTERSECTS, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 5, 10, 5, -2));
    	assertEquals(2, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 10, -5, 127, 345));
    	assertEquals(-2, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 127, 345, 10, -5));
    	assertEquals(1, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 127, 3, 200, 345));
    	assertEquals(-1, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 127, 345, 200, 3));
    	assertEquals(1, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 10, 1, 12, 3));
    	assertEquals(-1, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 12, 3, 10, 1));
    	assertEquals(1, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 10, 3, 12, 5));
    	assertEquals(-1, Segment2ai.calculatesCrossingsPointShadowSegment(0, 5, 3, 12, 5, 10, 3));
    	assertEquals(0, Segment2ai.calculatesCrossingsPointShadowSegment(0, 4, -1, 7, -5, 0, 0));
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void staticCalculatesCrossingsRectangleShadowSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
    		    		
    		label = x1+";"+y1+";"+x2+";"+y2;   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    		assertEquals(crossing, Segment2ai.calculatesCrossingsRectangleShadowSegment(0, 5, 3, 7, 5, x1, y1, x2, y2), label);

    		label = x2+";"+y2+";"+x1+";"+y1;   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    		if (crossing!=SHAPE_INTERSECTS) {
    			crossing = -crossing;
    		}
    		assertEquals(crossing, Segment2ai.calculatesCrossingsRectangleShadowSegment(0, 5, 3, 7, 5, x2, y2, x1, y1), label);
    	}
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void staticCalculatesCrossingsSegmentShadowSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    	assertEquals(0, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ -1, -1, 0, 0));
    	assertEquals(0, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ -1, 0, 10, 0));
    	assertEquals(1, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 10, -2, 12, 4));
    	assertEquals(1, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 12, 4, 50, 10));
    	assertEquals(2, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 10, -2, 50, 10));
    	assertEquals(0, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 10, 3, 12, 4));
    	assertEquals(0, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 12, 4, 50, 5));
    	assertEquals(0, Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 12, 3, 50, 5));
    	assertEquals(1,
    			Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 2, 6, 3));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 2, 6, 5));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 2, 6, 4));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 4, 6, 8));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 3, 6, 8));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 7, 4, 7, 8));
    	assertEquals(0,
    			Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 5, 4, 5, 8));
    	assertEquals(0,
    			Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 4, 4, 6, 6));
    	assertEquals(0,
    			Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 4, 3, 6, 5));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 8, 4, 6, 6));
    	assertEquals(1,
    			Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 10, 4, 6, 8));
    	assertEquals(-1,
    			Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 6, 8, 10, 4));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsSegmentShadowSegment(0, /**/ 5, 3, 7, 5, /**/ 5, 4, 100, 6));
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void staticCalculatesCrossingsCircleShadowSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    	assertEquals(0, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ -1, -1, 0, 0));
    	assertEquals(0, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ -1, 0, 10, 0));
    	assertEquals(1, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 10, -2, 12, 4));
    	assertEquals(1, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 12, 4, 50, 10));
    	assertEquals(2, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 10, -2, 50, 10));
    	assertEquals(0, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 10, 3, 12, 4));
    	assertEquals(0, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 12, 4, 50, 5));
    	assertEquals(0, Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 12, 3, 50, 5));
    	assertEquals(1,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 6, 2, 6, 3));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 6, 2, 6, 5));
    	assertEquals(1,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 7, 2, 7, 4));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 6, 2, 6, 4));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 6, 4, 6, 8));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 6, 3, 6, 8));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 7, 4, 7, 8));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 5, 4, 5, 8));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 4, 4, 6, 6));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 4, 3, 6, 5));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 8, 4, 6, 6));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 10, 4, 6, 8));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 6, 8, 10, 4));
    	assertEquals(1,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 7, 8, 10, 12));
    	assertEquals(-1,
    			Segment2ai.calculatesCrossingsCircleShadowSegment(0, /**/ 4, 6, 3, /**/ 10, 12, 7, 8));
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void setIT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createSegment(10, 12, 14, 16));
		assertEquals(10, this.shape.getX1());
		assertEquals(12, this.shape.getY1());
		assertEquals(14, this.shape.getX2());
		assertEquals(16, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setX1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setX1(145);
		assertEquals(145, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setY1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setY1(145);
		assertEquals(0, this.shape.getX1());
		assertEquals(145, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setX2(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setX2(145);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(145, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setY2(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setY2(145);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(145, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getX1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.shape.getX1());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getY1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.shape.getY1());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getX2(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(10, this.shape.getX2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getY2(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getP1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p = this.shape.getP1();
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getP2(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p = this.shape.getP2();
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointSegmentPoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;
		
		p = createPoint(0, 0);
		Segment2ai.findsClosestPointSegmentPoint(0, 0, 10, 5, 0, 0, p);
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = createPoint(0, 0);
		Segment2ai.findsClosestPointSegmentPoint(0, 0, 10, 5, 1, 1, p);
		assertEquals(1, p.ix());
		assertEquals(0, p.iy());

		p = createPoint(0, 0);
		Segment2ai.findsClosestPointSegmentPoint(0, 0, 10, 5, 2, 2, p);
		assertEquals(2, p.ix());
		assertEquals(1, p.iy());

		p = createPoint(0, 0);
		Segment2ai.findsClosestPointSegmentPoint(0, 0, 10, 5, -2, 2, p);
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = createPoint(0, 0);
		Segment2ai.findsClosestPointSegmentPoint(0, 0, 10, 5, 0, 1, p);
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = createPoint(0, 0);
		Segment2ai.findsClosestPointSegmentPoint(0, 0, 10, 5, 10, -1, p);
		assertEquals(7, p.ix());
		assertEquals(3, p.iy());

		p = createPoint(0, 0);
		Segment2ai.findsClosestPointSegmentPoint(0, 0, 10, 5, 2, 4, p);
		assertEquals(4, p.ix());
		assertEquals(2, p.iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsFarthestPointSegmentPoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;
		
		p = createPoint(0, 0);
		Segment2ai.findsFarthestPointSegmentPoint(0, 0, 10, 5, 0, 0, p);
		assertIntPointEquals(10, 5, p);

		p = createPoint(0, 0);
		Segment2ai.findsFarthestPointSegmentPoint(0, 0, 10, 5, 1, 1, p);
		assertIntPointEquals(10, 5, p);

		p = createPoint(0, 0);
		Segment2ai.findsFarthestPointSegmentPoint(0, 0, 10, 5, 2, 2, p);
		assertIntPointEquals(10, 5, p);

		p = createPoint(0, 0);
		Segment2ai.findsFarthestPointSegmentPoint(0, 0, 10, 5, -2, 2, p);
		assertIntPointEquals(10, 5, p);

		p = createPoint(0, 0);
		Segment2ai.findsFarthestPointSegmentPoint(0, 0, 10, 5, 0, 1, p);
		assertIntPointEquals(10, 5, p);

		p = createPoint(0, 0);
		Segment2ai.findsFarthestPointSegmentPoint(0, 0, 10, 5, 10, -1, p);
		assertIntPointEquals(0, 0, p);

		p = createPoint(0, 0);
		Segment2ai.findsFarthestPointSegmentPoint(0, 0, 10, 5, 2, 4, p);
		assertIntPointEquals(10, 5, p);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointsSegmentSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        Point2D p1;
        Point2D p2;

        p1 = createPoint(0, 0);
        p2 = createPoint(0, 0);
        assertEpsilonEquals(0, Segment2ai.findsClosestPointsSegmentSegment(0, 0, 10, 5, 0, 0, 1, 15, p1, p2));
        assertIntPointEquals(0, 0, p1);
        assertIntPointEquals(0, 0, p2);
	    
        p1 = createPoint(0, 0);
        p2 = createPoint(0, 0);
        assertEpsilonEquals(4, Segment2ai.findsClosestPointsSegmentSegment(0, 0, 10, 5, 0, 2, 1, 15, p1, p2));
        assertIntPointEquals(0, 0, p1);
        assertIntPointEquals(0, 2, p2);

        p1 = createPoint(0, 0);
        p2 = createPoint(0, 0);
        assertEpsilonEquals(58, Segment2ai.findsClosestPointsSegmentSegment(0, 0, 10, 5, 0, 15, 5, 11, p1, p2));
        assertIntPointEquals(8, 4, p1);
        assertIntPointEquals(5, 11, p2);

        p1 = createPoint(0, 0);
        p2 = createPoint(0, 0);
        assertEpsilonEquals(4, Segment2ai.findsClosestPointsSegmentSegment(0, 0, 10, 5, 0, 2, 10, 17, p1, p2));
        assertIntPointEquals(0, 0, p1);
        assertIntPointEquals(0, 2, p2);

        p1 = createPoint(0, 0);
        p2 = createPoint(0, 0);
        assertEpsilonEquals(0, Segment2ai.findsClosestPointsSegmentSegment(0, 0, 10, 5, 0, 15, 10, 0, p1, p2));
        assertIntPointEquals(8, 4, p1);
        assertIntPointEquals(7, 4, p2);

        p1 = createPoint(0, 0);
        p2 = createPoint(0, 0);
        assertEpsilonEquals(1, Segment2ai.findsClosestPointsSegmentSegment(2, 2, 3, 1, 3, -1, 8, 8, p1, p2));
        assertIntPointEquals(3, 1, p1);
        assertIntPointEquals(4, 1, p2);

        p1 = createPoint(0, 0);
        p2 = createPoint(0, 0);
        assertEpsilonEquals(0, Segment2ai.findsClosestPointsSegmentSegment(0, 0, 2, 2, 3, -1, 1, 1, p1, p2));
        assertIntPointEquals(1, 1, p1);
        assertIntPointEquals(1, 1, p2);

        p1 = createPoint(0, 0);
        p2 = createPoint(0, 0);
        assertEpsilonEquals(89, Segment2ai.findsClosestPointsSegmentSegment(0, 0, 10, 5, 0, 15, 5, 13, p1, p2));
        assertIntPointEquals(10, 5, p1);
        assertIntPointEquals(5, 13, p2);
	}

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void staticFindsClosestPointSegmentRectangle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        Point2D p1;

        p1 = createPoint(0, 0);
        assertEpsilonEquals(0, Segment2ai.findsClosestPointSegmentRectangle(0, 0, 10, 5, 0, 0, 5, 2, p1));
        assertIntPointEquals(2, 1, p1);
        
        p1 = createPoint(0, 0);
        assertEpsilonEquals(0, Segment2ai.findsClosestPointSegmentRectangle(0, 0, 10, 5, 0, 2, 5, 2, p1));
        assertIntPointEquals(4, 2, p1);

        p1 = createPoint(0, 0);
        assertEpsilonEquals(34, Segment2ai.findsClosestPointSegmentRectangle(0, 0, 10, 5, 15, 0, 5, 2, p1));
        assertIntPointEquals(10, 5, p1);

        p1 = createPoint(0, 0);
        assertEpsilonEquals(125, Segment2ai.findsClosestPointSegmentRectangle(0, 0, 10, 5, 0, 15, 5, 2, p1));
        assertIntPointEquals(10, 5, p1);
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsSideLinePoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEquals(0, Segment2ai.findsSideLinePoint(0, 0, 1, 1, 0, 0));
        assertEquals(0, Segment2ai.findsSideLinePoint(0, 0, 1, 1, 1, 1));
        assertEquals(0, Segment2ai.findsSideLinePoint(0, 0, 1, 1, 2, 2));
        assertEquals(1, Segment2ai.findsSideLinePoint(0, 0, 1, 1, 120, 0));
        assertEquals(0, Segment2ai.findsSideLinePoint(0, 0, 1, 1, -20, -20));
        assertEquals(-1, Segment2ai.findsSideLinePoint(0, 0, 1, 1, 0, 1));
        assertEquals(-1, Segment2ai.findsSideLinePoint(0, 0, 1, 1, 0, 120));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setP1Point2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setP1(createPoint(145, 654));
		assertEquals(145, this.shape.getX1());
		assertEquals(654, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setP2Point2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setP2(createPoint(145, 654));
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(145, this.shape.getX2());
		assertEquals(654, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setP1IntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setP1(145, 654);
		assertEquals(145, this.shape.getX1());
		assertEquals(654, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setP2IntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setP2(145, 654);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(145, this.shape.getX2());
		assertEquals(654, this.shape.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void toBoundingBoxB(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B bb = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(bb);
		assertEpsilonEquals(0, bb.getMinX());
		assertEpsilonEquals(0, bb.getMinY());
		assertEpsilonEquals(10, bb.getMaxX());
		assertEpsilonEquals(5, bb.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsRectangle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createRectangle(0,0,1,1)));
		assertFalse(this.shape.contains(createRectangle(0,0,8,1)));
		assertFalse(this.shape.contains(createRectangle(0,0,8,6)));
		assertFalse(this.shape.contains(createRectangle(0,0,100,100)));
		assertFalse(this.shape.contains(createRectangle(7,10,1,1)));
		assertFalse(this.shape.contains(createRectangle(16,0,100,100)));
		assertFalse(this.shape.contains(createRectangle(0,3,3,10)));
		
		this.shape = (T) createSegment(0, 0, 10, 0);
		assertFalse(this.shape.contains(createRectangle(0,0,2,0)));
	}

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void containsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFalse(this.shape.contains(createCircle(0,0,1)));
        assertFalse(this.shape.contains(createCircle(0,0,8)));
        assertFalse(this.shape.contains(createCircle(0,0,6)));
        assertFalse(this.shape.contains(createCircle(0,0,100)));
        assertFalse(this.shape.contains(createCircle(7,10,1)));
        assertFalse(this.shape.contains(createCircle(16,0,100)));
        assertFalse(this.shape.contains(createCircle(0,3,3)));
        assertFalse(this.shape.contains(createCircle(0,3,10)));
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void clipToRectangle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsRectangle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createRectangle(0,0,1,1)));
		assertTrue(this.shape.intersects(createRectangle(0,0,8,1)));
		assertTrue(this.shape.intersects(createRectangle(0,0,8,6)));
		assertTrue(this.shape.intersects(createRectangle(0,0,100,100)));
		assertFalse(this.shape.intersects(createRectangle(7,10,1,1)));
		assertFalse(this.shape.intersects(createRectangle(16,0,100,100)));
		assertFalse(this.shape.intersects(createRectangle(0,3,3,10)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsCircle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createCircle(0,0,1)));
		assertTrue(this.shape.intersects(createCircle(0,0,8)));
		assertTrue(this.shape.intersects(createCircle(0,0,8)));
		assertTrue(this.shape.intersects(createCircle(0,0,100)));
		assertFalse(this.shape.intersects(createCircle(7,10,1)));
		assertTrue(this.shape.intersects(createCircle(16,0,100)));
		assertTrue(this.shape.intersects(createCircle(0,3,3)));
		assertFalse(this.shape.intersects(createCircle(0,3,1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void intersectsSegment2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPath2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPathIterator2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape2D) createCircle(16,0,100)));
		assertTrue(this.shape.intersects((Shape2D) createRectangle(0,0,100,100)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_addVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(3, 4));
		assertEquals(3, this.shape.getX1());
		assertEquals(4, this.shape.getY1());
		assertEquals(13, this.shape.getX2());
		assertEquals(9, this.shape.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_plusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T r = this.shape.operator_plus(createVector(3, 4));
		assertEquals(3, r.getX1());
		assertEquals(4, r.getY1());
		assertEquals(13, r.getX2());
		assertEquals(9, r.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_removeVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(3, 4));
		assertEquals(-3, this.shape.getX1());
		assertEquals(-4, this.shape.getY1());
		assertEquals(7, this.shape.getX2());
		assertEquals(1, this.shape.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_minusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T r = this.shape.operator_minus(createVector(3, 4));
		assertEquals(-3, r.getX1());
		assertEquals(-4, r.getY1());
		assertEquals(7, r.getX2());
		assertEquals(1, r.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_multiplyTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createCircle(16,0,100)));
		assertTrue(this.shape.operator_and(createRectangle(0,0,100,100)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_upToPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(0, 0)));
		assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(1, 1)));
		assertEpsilonEquals(2.828427125f, this.shape.operator_upTo(createPoint(2, 4)));
		assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(2, 2)));
		assertEpsilonEquals(7.071067812f, this.shape.operator_upTo(createPoint(-5, 5)));
	}

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToCircle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertClosestPointInBothShapes(this.shape, createCircle(0, 0, 2));
        assertIntPointEquals(6, 3, this.shape.getClosestPointTo(createCircle(0, 15, 2)));
        assertIntPointEquals(10, 5, this.shape.getClosestPointTo(createCircle(15, 0, 2)));
        assertIntPointEquals(6, 3, this.shape.getClosestPointTo(createCircle(4, 5, 2)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredCircle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(0, 0, 2)));
        assertEpsilonEquals(125, this.shape.getDistanceSquared(createCircle(0, 15, 2)));
        assertEpsilonEquals(25, this.shape.getDistanceSquared(createCircle(15, 0, 2)));
        assertEpsilonEquals(1, this.shape.getDistanceSquared(createCircle(4, 5, 2)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToSegment2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertClosestPointInBothShapes(this.shape, createSegment(0, 0, 5, -2));
        assertIntPointEquals(10, 5, this.shape.getClosestPointTo(createSegment(0, 15, 5, 13)));
        assertIntPointEquals(10, 5, this.shape.getClosestPointTo(createSegment(15, 0, 20, 13)));
        assertIntPointEquals(7, 3, this.shape.getClosestPointTo(createSegment(4, 5, 9, 3)));
    }
    
    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredSegment2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(0, 0, 5, -2)));
        assertEpsilonEquals(89, this.shape.getDistanceSquared(createSegment(0, 15, 5, 13)));
        assertEpsilonEquals(40, this.shape.getDistanceSquared(createSegment(15, 0, 20, 13)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(4, 5, 9, 3)));
    }
    
    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToRectangle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertClosestPointInBothShapes(this.shape, createRectangle(0, 0, 3, 2));
        assertIntPointEquals(8, 4, this.shape.getClosestPointTo(createRectangle(2, 11, 3, 2)));
        assertIntPointEquals(5, 2, this.shape.getClosestPointTo(createRectangle(2, 3, 3, 2)));
        assertClosestPointInBothShapes(this.shape, createRectangle(3, 3, 3, 2));
        assertIntPointEquals(9, 4, this.shape.getClosestPointTo(createRectangle(15, -10, 3, 2)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredRectangle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(0, 0, 3, 2)));
        assertEpsilonEquals(58, this.shape.getDistanceSquared(createRectangle(2, 11, 3, 2)));
        assertEpsilonEquals(1, this.shape.getDistanceSquared(createRectangle(2, 3, 3, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(3, 3, 3, 2)));
        assertEpsilonEquals(180, this.shape.getDistanceSquared(createRectangle(15, -10, 3, 2)));
    }
    
    protected MultiShape2ai createTestMultiShape(int dx, int dy) {
        MultiShape2ai multishape = createMultiShape();
        Segment2ai segment = createSegment(dx - 5, dy - 4, dx - 8, dy - 1);
        Rectangle2ai rectangle = createRectangle(dx + 2, dy + 1, 3, 2);
        multishape.add(segment);
        multishape.add(rectangle);
        return multishape;
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToMultiShape2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(0, 0));
        assertIntPointEquals(0, 0, this.shape.getClosestPointTo(createTestMultiShape(0, 15)));
        assertIntPointEquals(5, 2, this.shape.getClosestPointTo(createTestMultiShape(15, 0)));
    }
        
    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredMultiShape2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(0, 0)));
        assertEpsilonEquals(146, this.shape.getDistanceSquared(createTestMultiShape(0, 15)));
        assertEpsilonEquals(13, this.shape.getDistanceSquared(createTestMultiShape(15, 0)));
    }

    protected Path2ai createTestPath(int dx, int dy) {
        Path2ai path = createPath();
        path.moveTo(dx + 5, dy - 5);
        path.lineTo(dx + 20, dy + 5);
        path.lineTo(dx + 0, dy + 20);
        path.lineTo(dx - 5, dy);
        return path;
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToPath2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertClosestPointInBothShapes(this.shape, createTestPath(0, 0));
        assertIntPointEquals(8, 4, this.shape.getClosestPointTo(createTestPath(0, 15)));
        assertIntPointEquals(10, 5, this.shape.getClosestPointTo(createTestPath(15, 0)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredPath2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(0, 0)));
        assertEpsilonEquals(45, this.shape.getDistanceSquared(createTestPath(0, 15)));
        assertEpsilonEquals(1, this.shape.getDistanceSquared(createTestPath(15, 0)));
    }

}