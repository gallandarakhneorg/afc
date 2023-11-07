/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.test.geometry.d3.ai;

import static org.arakhne.afc.math.geometry.GeomConstants.SHAPE_INTERSECTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.ai.Path3ai;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;
import org.arakhne.afc.math.geometry.d3.ai.AlignedBox3ai;
import org.arakhne.afc.math.geometry.d3.ai.Segment3ai;
import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;

@SuppressWarnings("all")
public abstract class AbstractSegment3aiTest<T extends Segment3ai<?, T, ?, ?, ?, ?, B>,
		B extends AlignedBox3ai<?, ?, ?, ?, ?, ?, B>> extends AbstractShape3aiTest<T, B> {

	protected static Quaternion4d newAxisAngleZ(double angle) {
		final Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(0, 0, 1, angle);
		return q;
	}
	
	@Override
	protected final T createShape() {
		return (T) createSegment(0, 0, 0, 10, 5, 0);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void testClone(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createSegment(0, 0, 0, 5, 5, 0)));
		assertFalse(this.shape.equals(createSegment(0, 0, 0, 10, 6, 0)));
		assertFalse(this.shape.equals(createAlignedBox(0, 0, 0, 10, 5, 0)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createSegment(0, 0, 0, 10, 5, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsObject_withPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createSegment(0, 0, 0, 5, 5, 0).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(0, 0, 0, 10, 6, 0).getPathIterator()));
		assertFalse(this.shape.equals(createAlignedBox(0, 0, 0, 10, 5, 0).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createSegment(0, 0, 0, 10, 5, 0).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsToShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createSegment(0, 0, 0, 5, 5, 0)));
		assertFalse(this.shape.equalsToShape((T) createSegment(0, 0, 0, 10, 6, 0)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createSegment(0, 0, 0, 10, 5, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsToPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createSegment(0, 0, 0, 5, 5, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(0, 0, 0, 10, 6, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createAlignedBox(0, 0, 0, 10, 5, 0).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createSegment(0, 0, 0, 10, 5, 0).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void isEmpty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void clear(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(0, this.shape.getX2());
		assertEquals(0, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(0, 0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(1, 1, 0)));
		
		assertEpsilonEquals(2.828427125f, this.shape.getDistance(createPoint(2, 4, 0)));

		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(2, 2, 0)));

		assertEpsilonEquals(7.071067812f, this.shape.getDistance(createPoint(-5, 5, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(0, 0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(1, 1, 0)));
		
		assertEpsilonEquals(8f, this.shape.getDistanceSquared(createPoint(2, 4, 0)));

		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(2, 2, 0)));

		assertEpsilonEquals(50f, this.shape.getDistanceSquared(createPoint(-5, 5, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceL1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(0, 0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(1, 1, 0)));
		
		assertEpsilonEquals(4f, this.shape.getDistanceL1(createPoint(2, 4, 0)));

		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(2, 2, 0)));

		assertEpsilonEquals(10f, this.shape.getDistanceL1(createPoint(-5, 5, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceLinf(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(0, 0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(1, 1, 0)));
		
		assertEpsilonEquals(2f, this.shape.getDistanceLinf(createPoint(2, 4, 0)));

		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(2, 2, 0)));

		assertEpsilonEquals(5f, this.shape.getDistanceLinf(createPoint(-5, 5, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void translateIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(3, 4, 0);
		assertEquals(3, this.shape.getX1());
		assertEquals(4, this.shape.getY1());
		assertEquals(13, this.shape.getX2());
		assertEquals(9, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(3, 4, 0));
		assertEquals(3, this.shape.getX1());
		assertEquals(4, this.shape.getY1());
		assertEquals(13, this.shape.getX2());
		assertEquals(9, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setIntIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(3, 4, 0, 5, 6, 0);
		assertEquals(3, this.shape.getX1());
		assertEquals(4, this.shape.getY1());
		assertEquals(5, this.shape.getX2());
		assertEquals(6, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPoint3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(createPoint(3, 4, 0), createPoint(5, 6, 0));
		assertEpsilonEquals(3, this.shape.getX1());
		assertEpsilonEquals(4, this.shape.getY1());
		assertEpsilonEquals(5, this.shape.getX2());
		assertEpsilonEquals(6, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void toBoundingBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B bb = this.shape.toBoundingBox();
		assertEpsilonEquals(0, bb.getMinX());
		assertEpsilonEquals(0, bb.getMinY());
		assertEpsilonEquals(10, bb.getMaxX());
		assertEpsilonEquals(5, bb.getMaxY());
	}

	@Override
	public void containsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.contains(createPoint(0, 0, 0)));
		assertTrue(this.shape.contains(createPoint(10, 5, 0)));
		
		assertFalse(this.shape.contains(createPoint(1, 1, 0)));
		assertFalse(this.shape.contains(createPoint(2, 4, 0)));

		assertFalse(this.shape.contains(createPoint(2, 2, 0)));

		assertTrue(this.shape.contains(createPoint(1, 0, 0)));

		assertFalse(this.shape.contains(createPoint(5, 3, 0)));
		assertTrue(this.shape.contains(createPoint(5, 2, 0)));
	}

	@Override
	public void containsIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.contains(0, 0, 0));
		assertTrue(this.shape.contains(10, 5, 0));
		
		assertFalse(this.shape.contains(1, 1, 0));
		assertFalse(this.shape.contains(2, 4, 0));

		assertFalse(this.shape.contains(2, 2, 0));

		assertTrue(this.shape.contains(1, 0, 0));

		assertFalse(this.shape.contains(5, 3, 0));
		assertTrue(this.shape.contains(5, 2, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getClosestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = this.shape.getClosestPointTo(createPoint(0,0, 0));
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = this.shape.getClosestPointTo(createPoint(1,1, 0));
		assertEquals(2, p.ix());
		assertEquals(1, p.iy());

		p = this.shape.getClosestPointTo(createPoint(2,2, 0));
		assertEquals(2, p.ix());
		assertEquals(1, p.iy());

		p = this.shape.getClosestPointTo(createPoint(-2,2, 0));
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = this.shape.getClosestPointTo(createPoint(0,1, 0));
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = this.shape.getClosestPointTo(createPoint(10,-1, 0));
		assertEquals(7, p.ix());
		assertEquals(3, p.iy());

		p = this.shape.getClosestPointTo(createPoint(2,4, 0));
		assertEquals(4, p.ix());
		assertEquals(2, p.iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = this.shape.getFarthestPointTo(createPoint(0,0, 0));
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(1,1, 0));
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(2,2, 0));
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(-2,2, 0));
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(0,1, 0));
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(10,-1, 0));
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(2,4, 0));
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());
	}

	@Override
	public void getPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 10, 5, 0);
		assertNoElement(pi);
	}

	@Override
	public void getPathIteratorTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr;
		PathIterator3ai pi;
		
		tr = new Transform3D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0,0, 0);
		assertElement(pi, PathElementType.LINE_TO, 10,5, 0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4f, 4.5f, 0);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 3, 5, 0);
		assertElement(pi, PathElementType.LINE_TO, 13, 10, 0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(newAxisAngleZ(MathConstants.QUARTER_PI));
		pi = this.shape.getPathIterator(tr); 
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 4, 11, 0);
		assertNoElement(pi);
	}

	@Override
    public void createTransformedShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
    	T s;
    	Transform3D tr;
    	
    	tr = new Transform3D();    	
    	s = (T) this.shape.createTransformedShape(tr);
		assertEquals(0, s.getX1());
		assertEquals(0, s.getY1());
		assertEquals(10, s.getX2());
		assertEquals(5, s.getY2());

    	tr = new Transform3D();
    	tr.setTranslation(3.4f, 4.5f, 0);
    	s = (T) this.shape.createTransformedShape(tr);
		assertEquals(3, s.getX1());
		assertEquals(5, s.getY1());
		assertEquals(13, s.getX2());
		assertEquals(10, s.getY2());

    	tr = new Transform3D();
    	tr.setRotation(newAxisAngleZ(MathConstants.PI));
    	s = (T) this.shape.createTransformedShape(tr);
		assertEquals(0, s.getX1());
		assertEquals(0, s.getY1());
		assertEquals(-10, s.getX2());
		assertEquals(-5, s.getY2());

    	tr = new Transform3D();
    	tr.setRotation(newAxisAngleZ(MathConstants.QUARTER_PI));
    	s = (T) this.shape.createTransformedShape(tr);
		assertEquals(0, s.getX1());
		assertEquals(0, s.getY1());
		assertEquals(4, s.getX2());
		assertEquals(11, s.getY2());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
    public void transformTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
    	T s;
    	Transform3D tr;
    	
    	tr = new Transform3D();
    	s = this.shape.clone();
    	s.transform(tr);
		assertEquals(0, s.getX1());
		assertEquals(0, s.getY1());
		assertEquals(10, s.getX2());
		assertEquals(5, s.getY2());

    	tr = new Transform3D();
    	tr.makeTranslationMatrix(3.4f, 4.5f, 0);
    	s = this.shape.clone();
    	s.transform(tr);
    	assertEquals(3, s.getX1());
    	assertEquals(5, s.getY1());
    	assertEquals(13, s.getX2());
    	assertEquals(10, s.getY2());

    	tr = new Transform3D();
    	tr.makeRotationMatrix(newAxisAngleZ(MathConstants.PI));
    	s = this.shape.clone();
    	s.transform(tr);
    	assertEquals(0, s.getX1());
    	assertEquals(0, s.getY1());
    	assertEquals(-10, s.getX2());
    	assertEquals(-5, s.getY2());

    	tr = new Transform3D();
    	tr.makeRotationMatrix(newAxisAngleZ(MathConstants.QUARTER_PI));
    	s = this.shape.clone();
    	s.transform(tr);
    	assertEquals(0, s.getX1());
    	assertEquals(0, s.getY1());
    	assertEquals(4, s.getX2());
    	assertEquals(11, s.getY2());
    }
    
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
    public void getPointIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
    	Point3D p;
    	Iterator<? extends Point3D> iterator = this.shape.getPointIterator();

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
	@EnumSource(CoordinateSystem3D.class)
    public void staticIntersectsSegmentSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
    	assertTrue(Segment3ai.intersectsSegmentSegment(0, 0, 0, 10, 5, 0, 0, 0, 0, 10, 5, 0));
    	assertTrue(Segment3ai.intersectsSegmentSegment(0, 0, 0, 10, 5, 0, 0, 0, 0, 5, 2, 0));
    	assertFalse(Segment3ai.intersectsSegmentSegment(0, 0, 0, 10, 5, 0, 0, 1, 0, 5, 3, 0));
    	assertFalse(Segment3ai.intersectsSegmentSegment(0, 0, 0, 10, 5, 0, 0, 2, 0, 5, 4, 0));
    	assertTrue(Segment3ai.intersectsSegmentSegment(0, 0, 0, 10, 5, 0, 5, 0, 0, 4, 3, 0));
    	assertFalse(Segment3ai.intersectsSegmentSegment(0, 0, 0, 10, 5, 0, -1, 5, 0, -1, 0, 0));
    	assertTrue(Segment3ai.intersectsSegmentSegment(5, 3, 0, 7, 5, 0, 6, 2, 0, 6, 5, 0));
    	assertTrue(Segment3ai.intersectsSegmentSegment(5, 3, 0, 7, 5, 0, 9, 4, 0, 6, 6, 0));
    	assertFalse(Segment3ai.intersectsSegmentSegment(5, 3, 0, 7, 5, 0, 9, 4, 0, 6, 7, 0));
    	assertTrue(Segment3ai.intersectsSegmentSegment(5, 3, 0, 7, 5, 0, 6, 4, 0, 6, 8, 0));
    }
    
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void staticComputeCrossingsFromPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
    	assertEquals(0, Segment3ai.computeCrossingsFromPoint(0, 5, 3, 0, -1, -1, 0, 0, 0, 0));
    	assertEquals(0, Segment3ai.computeCrossingsFromPoint(0, 5, 3, 0, 4, -2, 0, 4, 10, 0));
    	assertEquals(2, Segment3ai.computeCrossingsFromPoint(0, 5, 3, 0, 6, -2, 0, 6, 10, 0));
    	assertEquals(SHAPE_INTERSECTS, Segment3ai.computeCrossingsFromPoint(0, 5, 3, 0, 5, -2, 0, 5, 10, 0));
    	assertEquals(-2, Segment3ai.computeCrossingsFromPoint(0, 5, 3, 0, 6, 10, 0, 6, -2, 0));
    	assertEquals(SHAPE_INTERSECTS, Segment3ai.computeCrossingsFromPoint(0, 5, 3, 0, 5, 10, 0, 5, -2, 0));
    	assertEquals(2, Segment3ai.computeCrossingsFromPoint(0, 5, 3, 0, 10, -5, 0, 127, 345, 0));
    	assertEquals(-2, Segment3ai.computeCrossingsFromPoint(0, 5, 3, 0, 127, 345, 0, 10, -5, 0));
    	assertEquals(1, Segment3ai.computeCrossingsFromPoint(0, 5, 3, 0, 127, 3, 0, 200, 345, 0));
    	assertEquals(-1, Segment3ai.computeCrossingsFromPoint(0, 5, 3, 0, 127, 345, 0, 200, 3, 0));
    	assertEquals(1, Segment3ai.computeCrossingsFromPoint(0, 5, 3, 0, 10, 1, 0, 12, 3, 0));
    	assertEquals(-1, Segment3ai.computeCrossingsFromPoint(0, 5, 3, 0, 12, 3, 0, 10, 1, 0));
    	assertEquals(1, Segment3ai.computeCrossingsFromPoint(0, 5, 3, 0, 10, 3, 0, 12, 5, 0));
    	assertEquals(-1, Segment3ai.computeCrossingsFromPoint(0, 5, 3, 0, 12, 5, 0, 10, 3, 0));
    	assertEquals(0, Segment3ai.computeCrossingsFromPoint(0, 4, -1, 0, 7, -5, 0, 0, 0, 0));
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void staticComputeCrossingsFromRect(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
    	int[] data = new int[] {
    			-1, -1, 0, 0, 0, 0, 				0,
    			-1, 0, 0, 10, 0, 0,				    0,
    			10, -2, 0, 12, 4, 0,				1,
    			12, 4, 0, 50, 10, 0,				1,
    			10, -2, 0, 50, 10, 0,				2,
    			10, 3, 0, 12, 4, 0,				    0,
    			12, 4, 0, 50, 5, 0,				    0,
    			12, 3, 0, 50, 5, 0,				    0,
    			12, 5, 0, 50, 5, 0,				    0,
    			12, 4, 0, 50, 10, 0,				1,
    			12, 3, 0, 50, 10, 0,				1,
    			12, 5, 0, 50, 10, 0,				1,
    			0, 5, 0, 3, 7, 0,					0,
    			6, 2, 0, 6, 4, 0,					SHAPE_INTERSECTS,
    			6, 4, 0, 6, 8, 0,					SHAPE_INTERSECTS,
    			7, 4, 0, 7, 8, 0,					SHAPE_INTERSECTS,
    			5, 4, 0, 5, 8, 0,					SHAPE_INTERSECTS,
    			4, 4, 0, 6, 6, 0,					SHAPE_INTERSECTS,
    			6, 6, 0, 8, 4, 0,					SHAPE_INTERSECTS,
    			5, 4, 0, 7, 4, 0,					SHAPE_INTERSECTS,
    			0, 4, 0, 7, 4, 0,					SHAPE_INTERSECTS,
    			6, 6, 0, 12, 8, 0,                  0,
    			6, 2, 0, 12, -3, 0, 				0,
    	};
    	
    	String label;
    	for(int i=0; i<data.length;) {
    		int x1 = data[i++];
    		int y1 = data[i++];
    		int z1 = data[i++];
    		int x2 = data[i++];
    		int y2 = data[i++];
    		int z2 = data[i++];
    		int crossing = data[i++];
    		    		
    		label = x1+";"+y1+";"+z1+";"+x2+";"+y2+";"+z2;  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
    		assertEquals(crossing, Segment3ai.computeCrossingsFromRect(0, 5, 3, 0, 7, 5, 0, x1, y1, z2, x2, y2, z2), label);

    		label = x2+";"+y2+";"+z2+";"+x1+";"+y1+";"+z1;  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
    		if (crossing!=SHAPE_INTERSECTS) {
    			crossing = -crossing;
    		}
    		assertEquals(crossing, Segment3ai.computeCrossingsFromRect(0, 5, 3, 0, 7, 5, 0, x2, y2, z2, x1, y1, z1), label);
    	}
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void staticComputeCrossingsFromSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
    	assertEquals(0, Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ -1, -1, 0, 0, 0, 0));
    	assertEquals(0, Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ -1, 0, 0, 10, 0, 0));
    	assertEquals(1, Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 10, -2, 0, 12, 4, 0));
    	assertEquals(1, Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 12, 4, 0, 50, 10, 0));
    	assertEquals(2, Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 10, -2, 0, 50, 10, 0));
    	assertEquals(0, Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 10, 3, 0, 12, 4, 0));
    	assertEquals(0, Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 12, 4, 0, 50, 5, 0));
    	assertEquals(0, Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 12, 3, 0, 50, 5, 0));
    	assertEquals(1,
    			Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 6, 2, 0, 6, 3, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 6, 2, 0, 6, 5, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 6, 2, 0, 6, 4, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 6, 4, 0, 6, 8, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 6, 3, 0, 6, 8, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 7, 4, 0, 7, 8, 0));
    	assertEquals(0,
    			Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 5, 4, 0, 5, 8, 0));
    	assertEquals(0,
    			Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 4, 4, 0, 6, 6, 0));
    	assertEquals(0,
    			Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 4, 3, 0, 6, 5, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 8, 4, 0, 6, 6, 0));
    	assertEquals(1,
    			Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 10, 4, 0, 6, 8, 0));
    	assertEquals(-1,
    			Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 6, 8, 0, 10, 4, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSegment(0, /**/ 5, 3, 0, 7, 5, 0, /**/ 5, 4, 0, 100, 6, 0));
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void staticcomputeCrossingsFromSphere(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
    	assertEquals(0, Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ -1, -1, 0, 0, 0, 0));
    	assertEquals(0, Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ -1, 0, 0, 10, 0, 0));
    	assertEquals(1, Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 10, -2, 0, 12, 4, 0));
    	assertEquals(1, Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 12, 4, 0, 50, 10, 0));
    	assertEquals(2, Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 10, -2, 0, 50, 10, 0));
    	assertEquals(0, Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 10, 3, 0, 12, 4, 0));
    	assertEquals(0, Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 12, 4, 0, 50, 5, 0));
    	assertEquals(0, Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 12, 3, 0, 50, 5, 0));
    	assertEquals(1,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 6, 2, 0, 6, 3, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 6, 2, 0, 6, 5, 0));
    	assertEquals(1,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 7, 2, 0, 7, 4, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 6, 2, 0, 6, 4, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 6, 4, 0, 6, 8, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 6, 3, 0, 6, 8, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 7, 4, 0, 7, 8, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 5, 4, 0, 5, 8, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 4, 4, 0, 6, 6, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 4, 3, 0, 6, 5, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 8, 4, 0, 6, 6, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 10, 4, 0, 6, 8, 0));
    	assertEquals(SHAPE_INTERSECTS,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 6, 8, 0, 10, 4, 0));
    	assertEquals(1,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 7, 8, 0, 10, 12, 0));
    	assertEquals(-1,
    			Segment3ai.computeCrossingsFromSphere(0, /**/ 4, 6, 0, 3, /**/ 10, 12, 0, 7, 8, 0));
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void setIT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createSegment(10, 12, 0, 14, 16, 0));
		assertEquals(10, this.shape.getX1());
		assertEquals(12, this.shape.getY1());
		assertEquals(14, this.shape.getX2());
		assertEquals(16, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setX1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setX1(145);
		assertEquals(145, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setY1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setY1(145);
		assertEquals(0, this.shape.getX1());
		assertEquals(145, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setX2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setX2(145);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(145, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setY2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setY2(145);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(145, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getX1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.shape.getX1());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getY1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.shape.getY1());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getX2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(10, this.shape.getX2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getY2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getP1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p = this.shape.getP1();
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getP2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p = this.shape.getP2();
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeClosestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = createPoint(0, 0, 0);
		Segment3ai.computeClosestPointToPoint(0, 0, 0, 10, 5, 0, 0, 0, 0, p);
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = createPoint(0, 0, 0);
		Segment3ai.computeClosestPointToPoint(0, 0, 0, 10, 5, 0, 1, 1, 0, p);
		assertEquals(2, p.ix());
		assertEquals(1, p.iy());

		p = createPoint(0, 0, 0);
		Segment3ai.computeClosestPointToPoint(0, 0, 0, 10, 5, 0, 2, 2, 0, p);
		assertEquals(2, p.ix());
		assertEquals(1, p.iy());

		p = createPoint(0, 0, 0);
		Segment3ai.computeClosestPointToPoint(0, 0, 0, 10, 5, 0, -2, 2, 0, p);
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = createPoint(0, 0, 0);
		Segment3ai.computeClosestPointToPoint(0, 0, 0, 10, 5, 0, 0, 1, 0, p);
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = createPoint(0, 0, 0);
		Segment3ai.computeClosestPointToPoint(0, 0, 0, 10, 5, 0, 10, -1, 0, p);
		assertEquals(7, p.ix());
		assertEquals(3, p.iy());

		p = createPoint(0, 0, 0);
		Segment3ai.computeClosestPointToPoint(0, 0, 0, 10, 5, 0, 2, 4, 0, p);
		assertEquals(4, p.ix());
		assertEquals(2, p.iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = createPoint(0, 0, 0);
		Segment3ai.computeFarthestPointTo(0, 0, 0, 10, 5, 0, 0, 0, 0, p);
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = createPoint(0, 0, 0);
		Segment3ai.computeFarthestPointTo(0, 0, 0, 10, 5, 0, 1, 1, 0, p);
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = createPoint(0, 0, 0);
		Segment3ai.computeFarthestPointTo(0, 0, 0, 10, 5, 0, 2, 2, 0, p);
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = createPoint(0, 0, 0);
		Segment3ai.computeFarthestPointTo(0, 0, 0, 10, 5, 0, -2, 2, 0, p);
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = createPoint(0, 0, 0);
		Segment3ai.computeFarthestPointTo(0, 0, 0, 10, 5, 0, 0, 1, 0, p);
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());

		p = createPoint(0, 0, 0);
		Segment3ai.computeFarthestPointTo(0, 0, 0, 10, 5, 0, 10, -1, 0, p);
		assertEquals(0, p.ix());
		assertEquals(0, p.iy());

		p = createPoint(0, 0, 0);
		Segment3ai.computeFarthestPointTo(0, 0, 0, 10, 5, 0, 2, 4, 0, p);
		assertEquals(10, p.ix());
		assertEquals(5, p.iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeSideLinePoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(-1, Segment3ai.computeSideLinePoint(0, 0, 0, 10, 5, 0, 7, 0, 0));
		assertEquals(1, Segment3ai.computeSideLinePoint(0, 0, 0, 10, 5, 0, 4, 4, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setP1Point3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setP1(createPoint(145, 654, 0));
		assertEquals(145, this.shape.getX1());
		assertEquals(654, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setP2Point3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setP2(createPoint(145, 654, 0));
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(145, this.shape.getX2());
		assertEquals(654, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setP1IntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setP1(145, 654, 0);
		assertEquals(145, this.shape.getX1());
		assertEquals(654, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setP2IntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setP2(145, 654, 0);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(145, this.shape.getX2());
		assertEquals(654, this.shape.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void toBoundingBoxB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B bb = createAlignedBox(0, 0, 0, 0, 0, 0);
		this.shape.toBoundingBox(bb);
		assertEpsilonEquals(0, bb.getMinX());
		assertEpsilonEquals(0, bb.getMinY());
		assertEpsilonEquals(10, bb.getMaxX());
		assertEpsilonEquals(5, bb.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void containsAlignedBox3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createAlignedBox(0,0, 0,1,1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(0,0, 0,8,1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(0,0, 0,8,6, 0)));
		assertFalse(this.shape.contains(createAlignedBox(0,0, 0,100,100, 0)));
		assertFalse(this.shape.contains(createAlignedBox(7,10, 0,1,1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(16,0, 0,100,100, 0)));
		assertFalse(this.shape.contains(createAlignedBox(0,3, 0,3,10, 0)));
		
		this.shape = (T) createSegment(0, 0, 0, 10, 0, 0);
		assertTrue(this.shape.contains(createAlignedBox(0,0, 0,2,0, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void clipToRectangle(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.clipToRectangle(3, 1, 0, 7, 6, 0));
		assertEquals(3, this.shape.getX1());
		assertEquals(1, this.shape.getY1());
		assertEquals(7, this.shape.getX2());
		assertEquals(3, this.shape.getY2());
		
		this.shape = createShape();
		assertTrue(this.shape.clipToRectangle(8, 3, 0, 11, 7, 0));
		assertEquals(8, this.shape.getX1());
		assertEquals(4, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());

		this.shape = createShape();
		assertFalse(this.shape.clipToRectangle(0, 3, 0, 5, 4, 0));
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsAlignedBox3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createAlignedBox(0,0, 0,1,1, 0)));
		assertTrue(this.shape.intersects(createAlignedBox(0,0, 0,8,1, 0)));
		assertTrue(this.shape.intersects(createAlignedBox(0,0, 0,8,6, 0)));
		assertTrue(this.shape.intersects(createAlignedBox(0,0, 0,100,100, 0)));
		assertFalse(this.shape.intersects(createAlignedBox(7,10, 0,1,1, 0)));
		assertFalse(this.shape.intersects(createAlignedBox(16,0, 0,100,100, 0)));
		assertFalse(this.shape.intersects(createAlignedBox(0,3, 0,3,10, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSphere3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createSphere(0,0,0,1)));
		assertTrue(this.shape.intersects(createSphere(0,0,0,8)));
		assertTrue(this.shape.intersects(createSphere(0,0,0,8)));
		assertTrue(this.shape.intersects(createSphere(0,0,0,100)));
		assertFalse(this.shape.intersects(createSphere(7,10,0,1)));
		assertTrue(this.shape.intersects(createSphere(16,0,0,100)));
		assertTrue(this.shape.intersects(createSphere(0,3,0,3)));
		assertFalse(this.shape.intersects(createSphere(0,3,0,1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsSegment3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createSegment(0,0,0,1,10,0)));
		assertTrue(this.shape.intersects(createSegment(0,0,0,8,0,0)));
		assertTrue(this.shape.intersects(createSegment(0,0,0,8,-1,0)));
		assertTrue(this.shape.intersects(createSegment(0,0,0,100,-100,0)));
		assertFalse(this.shape.intersects(createSegment(7,10,0,1,3,0)));
		assertFalse(this.shape.intersects(createSegment(16,0,0,100,3,0)));
		assertFalse(this.shape.intersects(createSegment(0,3,0,3,3,0)));
		assertFalse(this.shape.intersects(createSegment(0,3,0,5,3,0)));
		assertTrue(this.shape.intersects(createSegment(0,3,0,6,3,0)));
		assertFalse(this.shape.intersects(createSegment(0,3,0,1,2,0)));
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void intersectsPath3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai path = createPath();
		path.moveTo(0, 0, 0);
		path.lineTo(2, 2, 0);
		path.quadTo(3, 0, 0, 4, 3, 0);
		path.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
		path.closePath();
		
		assertTrue(createSegment(0, 0, 0, 1, 1, 0).intersects(path));
		assertTrue(createSegment(4, 3, 0, 1, 1, 0).intersects(path));
		assertTrue(createSegment(2, 2, 0, 1, 1, 0).intersects(path));
		assertTrue(createSegment(2, 1, 0, 1, 1, 0).intersects(path));
		assertTrue(createSegment(3, 0, 0, 1, 1, 0).intersects(path));
		assertTrue(createSegment(-1, -1, 0, 1, 1, 0).intersects(path));
		assertTrue(createSegment(4, -3, 0, 1, 1, 0).intersects(path));
		assertTrue(createSegment(-3, 4, 0, 1, 1, 0).intersects(path));
		assertTrue(createSegment(6, -5, 0, 1, 1, 0).intersects(path));
		assertTrue(createSegment(4, 0, 0, 1, 1, 0).intersects(path));
		assertTrue(createSegment(5, 0, 0, 1, 1, 0).intersects(path));
		assertFalse(createSegment(-4, -4, 0, -3, -3, 0).intersects(path));
		assertFalse(createSegment(-1, 0, 0, 2, 3, 0).intersects(path));
		assertFalse(createSegment(7, 1, 0, 18, 14, 0).intersects(path));
	}


	@Override
	public void intersectsPathIterator3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai path = createPath();
		path.moveTo(0, 0, 0);
		path.lineTo(2, 2, 0);
		path.quadTo(3, 0, 0, 4, 3, 0);
		path.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
		path.closePath();
		
		assertTrue(createSegment(0, 0, 0, 1, 1, 0).intersects((PathIterator3ai) path.getPathIterator()));
		assertTrue(createSegment(4, 3, 0, 1, 1, 0).intersects((PathIterator3ai) path.getPathIterator()));
		assertTrue(createSegment(2, 2, 0, 1, 1, 0).intersects((PathIterator3ai) path.getPathIterator()));
		assertTrue(createSegment(2, 1, 0, 1, 1, 0).intersects((PathIterator3ai) path.getPathIterator()));
		assertTrue(createSegment(3, 0, 0, 1, 1, 0).intersects((PathIterator3ai) path.getPathIterator()));
		assertTrue(createSegment(-1, -1, 0, 1, 1, 0).intersects((PathIterator3ai) path.getPathIterator()));
		assertTrue(createSegment(4, -3, 0, 1, 1, 0).intersects((PathIterator3ai) path.getPathIterator()));
		assertTrue(createSegment(-3, 4, 0, 1, 1, 0).intersects((PathIterator3ai) path.getPathIterator()));
		assertTrue(createSegment(6, -5, 0, 1, 1, 0).intersects((PathIterator3ai) path.getPathIterator()));
		assertTrue(createSegment(4, 0, 0, 1, 1, 0).intersects((PathIterator3ai) path.getPathIterator()));
		assertTrue(createSegment(5, 0, 0, 1, 1, 0).intersects((PathIterator3ai) path.getPathIterator()));
		assertFalse(createSegment(-4, -4, 0, -3, -3, 0).intersects((PathIterator3ai) path.getPathIterator()));
		assertFalse(createSegment(-1, 0, 0, 2, 3, 0).intersects((PathIterator3ai) path.getPathIterator()));
		assertFalse(createSegment(7, 1, 0, 18, 14, 0).intersects((PathIterator3ai) path.getPathIterator()));
	}

	@Override
	public void intersectsShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape3D) createSphere(16,0, 0,100)));
		assertTrue(this.shape.intersects((Shape3D) createAlignedBox(0,0, 0,100,100, 0)));
	}

	@Override
	public void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(3, 4, 0));
		assertEquals(3, this.shape.getX1());
		assertEquals(4, this.shape.getY1());
		assertEquals(13, this.shape.getX2());
		assertEquals(9, this.shape.getY2());
	}

	@Override
	public void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T r = this.shape.operator_plus(createVector(3, 4, 0));
		assertEquals(3, r.getX1());
		assertEquals(4, r.getY1());
		assertEquals(13, r.getX2());
		assertEquals(9, r.getY2());
	}

	@Override
	public void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(3, 4, 0));
		assertEquals(-3, this.shape.getX1());
		assertEquals(-4, this.shape.getY1());
		assertEquals(7, this.shape.getX2());
		assertEquals(1, this.shape.getY2());
	}

	@Override
	public void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T r = this.shape.operator_minus(createVector(3, 4, 0));
		assertEquals(-3, r.getX1());
		assertEquals(-4, r.getY1());
		assertEquals(7, r.getX2());
		assertEquals(1, r.getY2());
	}

	@Override
	public void operator_multiplyTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
    	T s;
    	Transform3D tr;
    	
    	tr = new Transform3D();    	
    	s = (T) this.shape.operator_multiply(tr);
		assertEquals(0, s.getX1());
		assertEquals(0, s.getY1());
		assertEquals(10, s.getX2());
		assertEquals(5, s.getY2());

    	tr = new Transform3D();
    	tr.setTranslation(3.4f, 4.5f, 0);
    	s = (T) this.shape.operator_multiply(tr);
		assertEquals(3, s.getX1());
		assertEquals(5, s.getY1());
		assertEquals(13, s.getX2());
		assertEquals(10, s.getY2());

    	tr = new Transform3D();
    	tr.setRotation(newAxisAngleZ(MathConstants.PI));
    	s = (T) this.shape.operator_multiply(tr);
		assertEquals(0, s.getX1());
		assertEquals(0, s.getY1());
		assertEquals(-10, s.getX2());
		assertEquals(-5, s.getY2());

    	tr = new Transform3D();
    	tr.setRotation(newAxisAngleZ(MathConstants.QUARTER_PI));
    	s = (T) this.shape.operator_multiply(tr);
		assertEquals(0, s.getX1());
		assertEquals(0, s.getY1());
		assertEquals(4, s.getX2());
		assertEquals(11, s.getY2());
	}

	@Override
	public void operator_andPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createPoint(0, 0, 0)));
		assertTrue(this.shape.operator_and(createPoint(10, 5, 0)));
		
		assertFalse(this.shape.operator_and(createPoint(1, 1, 0)));
		assertFalse(this.shape.operator_and(createPoint(2, 4, 0)));

		assertFalse(this.shape.operator_and(createPoint(2, 2, 0)));

		assertTrue(this.shape.operator_and(createPoint(1, 0, 0)));

		assertFalse(this.shape.operator_and(createPoint(5, 3, 0)));
		assertTrue(this.shape.operator_and(createPoint(5, 2, 0)));
	}

	@Override
	public void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createSphere(16,0, 0,100)));
		assertTrue(this.shape.operator_and(createAlignedBox(0,0, 0,100,100, 0)));
	}

	@Override
	public void operator_upToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(1, 1, 0)));
		assertEpsilonEquals(2.828427125f, this.shape.operator_upTo(createPoint(2, 4, 0)));
		assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(2, 2, 0)));
		assertEpsilonEquals(7.071067812f, this.shape.operator_upTo(createPoint(-5, 5, 0)));
	}

}
