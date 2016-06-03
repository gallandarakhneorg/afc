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

package org.arakhne.afc.math.geometry.d3.afp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;

@SuppressWarnings("all")
public abstract class AbstractRectangularPrism3afpTest<T extends RectangularPrism3afp<?, T, ?, ?, ?, B>,
		B extends RectangularPrism3afp<?, ?, ?, ?, ?, B>> extends AbstractPrism3afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createRectangularPrism(5, 8, 2, 5, 10, 8);
	}

	@Test
	public void staticIntersectsRectangleRectangle() {
		assertFalse(RectangularPrism3afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 0, 1, 1));
		assertFalse(RectangularPrism3afp.intersectsRectangleRectangle(0, 0, 1, 1, 5, 8, 10, 18));

		assertFalse(RectangularPrism3afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 20, 1, 22));
		assertFalse(RectangularPrism3afp.intersectsRectangleRectangle(0, 20, 1, 22, 5, 8, 10, 18));

		assertFalse(RectangularPrism3afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 0, 5, 100));
		assertFalse(RectangularPrism3afp.intersectsRectangleRectangle(0, 0, 5, 100, 5, 8, 10, 18));

		assertTrue(RectangularPrism3afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 0, 5.1, 100));
		assertTrue(RectangularPrism3afp.intersectsRectangleRectangle(0, 0, 5.1, 100, 5, 8, 10, 18));

		assertTrue(RectangularPrism3afp.intersectsRectangleRectangle(5, 8, 10, 18, 6, 9, 9.5, 15));
		assertTrue(RectangularPrism3afp.intersectsRectangleRectangle(6, 9, 9.5, 15, 5, 8, 10, 18));

		assertTrue(RectangularPrism3afp.intersectsRectangleRectangle(5, 8, 10, 18, 6, 9, 9.5, 15));
		assertTrue(RectangularPrism3afp.intersectsRectangleRectangle(6, 9, 9.5, 15, 5, 8, 10, 18));
	}

	@Test
	public void staticIntersectsRectangleLine() {
		assertTrue(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 20, 45, 43, 15));
		assertTrue(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 20, 55, 43, 15));
		assertTrue(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 20, 0, 43, 15));
		assertTrue(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 0, 45, 43, 15));
		assertTrue(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 20, 45, 60, 15));
		assertFalse(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 5, 45, 30, 55));
		assertFalse(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 40, 55, 60, 15));
		assertFalse(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 40, 0, 60, 40));
		assertTrue(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 0, 40, 20, 0));
		assertTrue(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 0, 45, 100, 15));
		assertTrue(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 20, 100, 43, 0));
		assertFalse(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 20, 100, 43, 101));
		assertFalse(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 100, 45, 102, 15));
		assertFalse(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 20, 0, 43, -2));
		assertFalse(RectangularPrism3afp.intersectsRectangleLine(10, 12, 50, 49, -100, 45, -48, 15));
		assertFalse(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, -100, 60, -98, 61));
		assertTrue(RectangularPrism3afp.intersectsRectangleLine(10, 12, 40, 37, 0, 30, 9, 21));
	}

	@Test
	public void staticIntersectsRectangleSegment() {
		assertTrue(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 45, 43, 15));
		assertTrue(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 55, 43, 15));
		assertTrue(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 0, 43, 15));
		assertTrue(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 45, 43, 15));
		assertTrue(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 45, 60, 15));
		assertFalse(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 5, 45, 30, 55));
		assertFalse(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 40, 55, 60, 15));
		assertFalse(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 40, 0, 60, 40));
		assertTrue(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 40, 20, 0));
		assertTrue(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 45, 100, 15));
		assertTrue(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 100, 43, 0));
		assertFalse(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 100, 43, 101));
		assertFalse(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 100, 45, 102, 15));
		assertFalse(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 0, 43, -2));
		assertFalse(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 50, 49, -100, 45, -48, 15));
		assertFalse(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, -100, 60, -98, 61));
		assertFalse(RectangularPrism3afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 30, 9, 21));
	}

	@Test
	public void staticContainsRectangleRectangle() {
		assertFalse(RectangularPrism3afp.containsRectangleRectangle(5, 8, 10, 18, 0, 0, 1, 1));
		assertFalse(RectangularPrism3afp.containsRectangleRectangle(0, 0, 1, 1, 5, 8, 10, 18));

		assertFalse(RectangularPrism3afp.containsRectangleRectangle(5, 8, 10, 18, 0, 20, 1, 22));
		assertFalse(RectangularPrism3afp.containsRectangleRectangle(0, 20, 1, 22, 5, 8, 10, 18));

		assertFalse(RectangularPrism3afp.containsRectangleRectangle(5, 8, 10, 18, 0, 0, 5, 100));
		assertFalse(RectangularPrism3afp.containsRectangleRectangle(0, 0, 5, 100, 5, 8, 10, 18));

		assertFalse(RectangularPrism3afp.containsRectangleRectangle(5, 8, 10, 18, 0, 0, 5.1, 100));
		assertFalse(RectangularPrism3afp.containsRectangleRectangle(0, 0, 5.1, 100, 5, 8, 10, 18));

		assertTrue(RectangularPrism3afp.containsRectangleRectangle(5, 8, 10, 18, 6, 9, 9.5, 15));
		assertFalse(RectangularPrism3afp.containsRectangleRectangle(6, 9, 9.5, 15, 5, 8, 10, 18));
	}

	@Test
	public void staticContainsRectanglePoint() {
		assertFalse(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 20, 45));
		assertFalse(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 20, 55));
		assertFalse(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 20, 0));
		assertFalse(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 0, 45));
		assertFalse(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 5, 45));
		assertFalse(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 40, 55));
		assertFalse(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 40, 0));
		assertFalse(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 0, 40));
		assertFalse(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 20, 100));
		assertFalse(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 100, 45));
		assertFalse(RectangularPrism3afp.containsRectanglePoint(10, 12, 50, 49, -100, 45));
		assertFalse(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, -100, 60));
		assertTrue(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 10, 12));
		assertTrue(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 40, 12));
		assertTrue(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 40, 37));
		assertTrue(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 10, 37));
		assertTrue(RectangularPrism3afp.containsRectanglePoint(10, 12, 40, 37, 35, 24));
	}

	@Test
	@Override
	public void equalsObject() {
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createRectangle(0, 8, 5, 12)));
		assertFalse(this.shape.equals(createRectangle(5, 8, 5, 0)));
		assertFalse(this.shape.equals(createSegment(5, 8, 5, 10)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createRectangle(5, 8, 5, 10)));
	}

	@Test
	@Override
	public void equalsObject_withPathIterator() {
		assertFalse(this.shape.equals(createRectangle(0, 8, 5, 12).getPathIterator()));
		assertFalse(this.shape.equals(createRectangle(5, 8, 5, 0).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 5, 10).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createRectangle(5, 8, 5, 10).getPathIterator()));
	}

	@Test
	@Override
	public void equalsToPathIterator() {
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createRectangle(0, 8, 5, 12).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createRectangle(5, 8, 5, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 5, 10).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createRectangle(5, 8, 5, 10).getPathIterator()));
	}

	@Test
	@Override
	public void equalsToShape() {
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createRectangle(0, 8, 5, 12)));
		assertFalse(this.shape.equalsToShape((T) createRectangle(5, 8, 5, 0)));
		assertTrue(this.shape.equalsToShape((T) this.shape));
		assertTrue(this.shape.equalsToShape((T) createRectangle(5, 8, 5, 10)));
	}

	@Test
	public void addPoint3D() {
		this.shape.add(createPoint(123.456, 456.789));
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());

		this.shape.add(createPoint(-123.456, 456.789));
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());

		this.shape.add(createPoint(-123.456, -456.789));
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(-456.789, this.shape.getMinY());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
	}

	@Test
	public void addDoubleDouble() {
		this.shape.add(123.456, 456.789);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());

		this.shape.add(-123.456, 456.789);
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());

		this.shape.add(-123.456, -456.789);
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(-456.789, this.shape.getMinY());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
	}
	
	@Test
	public void setUnion() {
		this.shape.setUnion(createRectangle(0, 0, 12, 1));
		assertEpsilonEquals(0, this.shape.getMinX());
		assertEpsilonEquals(0, this.shape.getMinY());
		assertEpsilonEquals(12, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@Test
	public void createUnion() {
		B union = this.shape.createUnion(createRectangle(0, 0, 12, 1));
		assertNotSame(this.shape, union);
		assertEpsilonEquals(0, union.getMinX());
		assertEpsilonEquals(0, union.getMinY());
		assertEpsilonEquals(12, union.getMaxX());
		assertEpsilonEquals(18, union.getMaxY());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@Test
	public void setIntersection_noIntersection() {
		this.shape.setIntersection(createRectangle(0, 0, 12, 1));
		assertTrue(this.shape.isEmpty());
	}

	@Test
	public void setIntersection_intersection() {
		this.shape.setIntersection(createRectangle(0, 0, 7, 10));
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(7, this.shape.getMaxX());
		assertEpsilonEquals(10, this.shape.getMaxY());
	}

	@Test
	public void createIntersection_noIntersection() {
		B box = this.shape.createIntersection(createRectangle(0, 0, 12, 1));
		assertNotSame(this.shape, box);
		assertTrue(box.isEmpty());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@Test
	public void createIntersection_intersection() {
		B box = this.shape.createIntersection(createRectangle(0, 0, 7, 10));
		assertNotSame(this.shape, box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(10, box.getMaxY());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@Test
	public void avoidCollisionWithRectangularPrism3afpVector3D() {
		B r = createRectangle(0, 0, 7, 10);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector3D v = createVector(Double.NaN, Double.NaN);
		this.shape.avoidCollisionWith(r, v);
		
		assertEpsilonEquals(2, v.getX());
		assertEpsilonEquals(0, v.getY());
		assertEpsilonEquals(7, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(12, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertFalse(this.shape.intersects(r));
		assertFalse(r.intersects(this.shape));
	}

	@Test
	public void avoidCollisionWithRectangularPrism3afpVector3DVector3D_nullDisplacement() {
		B r = createRectangle(0, 0, 7, 10);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector3D v = createVector(Double.NaN, Double.NaN);
		this.shape.avoidCollisionWith(r, null, v);
		
		assertEpsilonEquals(2, v.getX());
		assertEpsilonEquals(0, v.getY());
		assertEpsilonEquals(7, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(12, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertFalse(this.shape.intersects(r));
		assertFalse(r.intersects(this.shape));
	}

	@Test
	public void avoidCollisionWithRectangularPrism3afpVector3DVector3D_noDisplacement() {
		B r = createRectangle(0, 0, 7, 10);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector3D v1 = createVector(0, 0);
		Vector3D v2 = createVector(Double.NaN, Double.NaN);
		this.shape.avoidCollisionWith(r, v1, v2);
		
		assertEpsilonEquals(2, v2.getX());
		assertEpsilonEquals(0, v2.getY());
		assertEpsilonEquals(7, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(12, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertFalse(this.shape.intersects(r));
		assertFalse(r.intersects(this.shape));
	}

	@Test
	public void avoidCollisionWithRectangularPrism3afpVector3DVector3D_givenDisplacement() {
		B r = createRectangle(0, 0, 7, 10);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector3D v1 = createVector(-4, 4);
		Vector3D v2 = createVector(Double.NaN, Double.NaN);
		this.shape.avoidCollisionWith(r, v1, v2);
		
		assertEpsilonEquals(-2, v1.getX());
		assertEpsilonEquals(2, v1.getY());
		assertEpsilonEquals(-2, v2.getX());
		assertEpsilonEquals(2, v2.getY());
		assertEpsilonEquals(3, this.shape.getMinX());
		assertEpsilonEquals(10, this.shape.getMinY());
		assertEpsilonEquals(8, this.shape.getMaxX());
		assertEpsilonEquals(20, this.shape.getMaxY());
		assertFalse(this.shape.intersects(r));
		assertFalse(r.intersects(this.shape));
	}

	@Override
	public void getClosestPointTo() {
		Point3D p;
		
		p = this.shape.getClosestPointTo(createPoint(0, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getClosestPointTo(createPoint(100, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getClosestPointTo(createPoint(100, 100));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getClosestPointTo(createPoint(0, 100));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getClosestPointTo(createPoint(0, 10));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());

		p = this.shape.getClosestPointTo(createPoint(7, 0));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getClosestPointTo(createPoint(154, 17));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());

		p = this.shape.getClosestPointTo(createPoint(9, 154));
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getClosestPointTo(createPoint(8, 18));
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getClosestPointTo(createPoint(7, 12));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
	}

	@Override
	public void getFarthestPointTo() {
		Point3D p;
		
		p = this.shape.getFarthestPointTo(createPoint(0, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(100, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(100, 100));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(0, 100));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(0, 10));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(7, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(154, 17));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(9, 154));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(8, 18));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(7, 12));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
	}

	@Override
	public void getDistance() {
		assertEpsilonEquals(9.43398, this.shape.getDistance(createPoint(0, 0)));
		assertEpsilonEquals(90.35486, this.shape.getDistance(createPoint(100, 0)));
		assertEpsilonEquals(121.75385, this.shape.getDistance(createPoint(100, 100)));
		assertEpsilonEquals(82.1523, this.shape.getDistance(createPoint(0, 100)));
		assertEpsilonEquals(5, this.shape.getDistance(createPoint(0, 10)));
		assertEpsilonEquals(8, this.shape.getDistance(createPoint(7, 0)));
		assertEpsilonEquals(144, this.shape.getDistance(createPoint(154, 17)));
		assertEpsilonEquals(136, this.shape.getDistance(createPoint(9, 154)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(8, 18)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(7, 12)));
	}

	@Override
	public void getDistanceSquared() {
		assertEpsilonEquals(88.99998, this.shape.getDistanceSquared(createPoint(0, 0)));
		assertEpsilonEquals(8164, this.shape.getDistanceSquared(createPoint(100, 0)));
		assertEpsilonEquals(14823.99999, this.shape.getDistanceSquared(createPoint(100, 100)));
		assertEpsilonEquals(6749, this.shape.getDistanceSquared(createPoint(0, 100)));
		assertEpsilonEquals(25, this.shape.getDistanceSquared(createPoint(0, 10)));
		assertEpsilonEquals(64, this.shape.getDistanceSquared(createPoint(7, 0)));
		assertEpsilonEquals(20736, this.shape.getDistanceSquared(createPoint(154, 17)));
		assertEpsilonEquals(18496, this.shape.getDistanceSquared(createPoint(9, 154)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(8, 18)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(7, 12)));
	}

	@Override
	public void getDistanceL1() {
		assertEpsilonEquals(13, this.shape.getDistanceL1(createPoint(0, 0)));
		assertEpsilonEquals(98, this.shape.getDistanceL1(createPoint(100, 0)));
		assertEpsilonEquals(172, this.shape.getDistanceL1(createPoint(100, 100)));
		assertEpsilonEquals(87, this.shape.getDistanceL1(createPoint(0, 100)));
		assertEpsilonEquals(5, this.shape.getDistanceL1(createPoint(0, 10)));
		assertEpsilonEquals(8, this.shape.getDistanceL1(createPoint(7, 0)));
		assertEpsilonEquals(144, this.shape.getDistanceL1(createPoint(154, 17)));
		assertEpsilonEquals(136, this.shape.getDistanceL1(createPoint(9, 154)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(8, 18)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(7, 12)));
	}

	@Override
	public void getDistanceLinf() {
		assertEpsilonEquals(8, this.shape.getDistanceLinf(createPoint(0, 0)));
		assertEpsilonEquals(90, this.shape.getDistanceLinf(createPoint(100, 0)));
		assertEpsilonEquals(90, this.shape.getDistanceLinf(createPoint(100, 100)));
		assertEpsilonEquals(82, this.shape.getDistanceLinf(createPoint(0, 100)));
		assertEpsilonEquals(5, this.shape.getDistanceLinf(createPoint(0, 10)));
		assertEpsilonEquals(8, this.shape.getDistanceLinf(createPoint(7, 0)));
		assertEpsilonEquals(144, this.shape.getDistanceLinf(createPoint(154, 17)));
		assertEpsilonEquals(136, this.shape.getDistanceLinf(createPoint(9, 154)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(8, 18)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(7, 12)));
	}

	@Override
	public void setIT() {
		this.shape.set((T) createRectangle(123.456, 456.789, 789.123, 159.753));
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(456.789, this.shape.getMinY());
		assertEpsilonEquals(912.579, this.shape.getMaxX());
		assertEpsilonEquals(616.542, this.shape.getMaxY());
	}

	@Override
	public void getPathIterator() {
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 18);
		assertElement(pi, PathElementType.LINE_TO, 5, 18);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertNoElement(pi);
	}

	@Override
	public void getPathIteratorTransform3D() {
		PathIterator3afp pi;
		
		pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 18);
		assertElement(pi, PathElementType.LINE_TO, 5, 18);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertNoElement(pi);

		pi = this.shape.getPathIterator(new Transform3D());
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 18);
		assertElement(pi, PathElementType.LINE_TO, 5, 18);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertNoElement(pi);

		Transform3D tr;
		tr = new Transform3D();
		tr.setTranslation(123.456, 456.789);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 128.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 474.789);
		assertElement(pi, PathElementType.LINE_TO, 128.456, 474.789);
		assertElement(pi, PathElementType.CLOSE, 128.456, 464.789);
		assertNoElement(pi);
	}

	@Override
	public void createTransformedShape() {
		Transform3D tr;
		tr = new Transform3D();
		tr.setTranslation(123.456, 456.789);
		PathIterator3afp pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 128.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 474.789);
		assertElement(pi, PathElementType.LINE_TO, 128.456, 474.789);
		assertElement(pi, PathElementType.CLOSE, 128.456, 464.789);
		assertNoElement(pi);
	}

	@Override
	public void containsRectangularPrism3afp() {
		assertFalse(this.shape.contains(createRectangle(0, 0, 1, 1)));
		assertFalse(createRectangle(0, 0, 1, 1).contains(this.shape));

		assertFalse(this.shape.contains(createRectangle(0, 20, 1, 2)));
		assertFalse(createRectangle(0, 20, 1, 2).contains(this.shape));

		assertFalse(this.shape.contains(createRectangle(0, 0, 5, 100)));
		assertFalse(createRectangle(0, 0, 5, 100).contains(this.shape));

		assertFalse(this.shape.contains(createRectangle(0, 0, 5.1, 100)));
		assertFalse(createRectangle(0, 0, 5.1, 100).contains(this.shape));

		assertTrue(this.shape.contains(createRectangle(6, 9, .5, 9)));
		assertFalse(createRectangle(6, 9, .5, 9).contains(this.shape));
	}

	@Override
	public void intersectsRectangularPrism3afp() {
		assertFalse(this.shape.intersects(createRectangle(0, 0, 1, 1)));
		assertFalse(createRectangle(0, 0, 1, 1).intersects(this.shape));

		assertFalse(this.shape.intersects(createRectangle(0, 20, 1, 2)));
		assertFalse(createRectangle(0, 20, 1, 2).intersects(this.shape));

		assertFalse(this.shape.intersects(createRectangle(0, 0, 5, 100)));
		assertFalse(createRectangle(0, 0, 5, 100).intersects(this.shape));

		assertTrue(this.shape.intersects(createRectangle(0, 0, 5.1, 100)));
		assertTrue(createRectangle(0, 0, 5.1, 100).intersects(this.shape));

		assertTrue(this.shape.intersects(createRectangle(6, 9, .5, 9)));
		assertTrue(createRectangle(6, 9, .5, 9).intersects(this.shape));

		assertTrue(this.shape.intersects(createRectangle(0, 0, 5.1, 8.1)));
		assertTrue(createRectangle(0, 0, 5.1, 8.1).intersects(this.shape));
	}

	@Override
	public void intersectsSphere3afp() {
		assertTrue(createRectangle(0, 0, .5, .5).intersects(createCircle(0, 0, 1)));
		assertTrue(createRectangle(0, 0, 1, 1).intersects(createCircle(0, 0, 1)));
		assertTrue(createRectangle(0, 0, .5, 1).intersects(createCircle(0, 0, 1)));
		assertFalse(createRectangle(0, 0, 1, 1).intersects(createCircle(2, 0, 1)));
	}

	@Override
	public void intersectsSegment3afp() {
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 45, 43, 15)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 55, 43, 15)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 0, 43, 15)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(0, 45, 43, 15)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 45, 60, 15)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(5, 45, 30, 55)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(40, 55, 60, 15)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(40, 0, 60, 40)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(0, 40, 20, 0)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(0, 45, 100, 15)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 100, 43, 0)));
		assertFalse(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 100, 43, 101)));
		assertFalse(createRectangle(10, 12, 40, 37).intersects(createSegment(100, 45, 102, 15)));
		assertFalse(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 0, 43, -2)));
		assertFalse(createRectangle(10, 12, 50, 49).intersects(createSegment(-100, 45, -48, 15)));
		assertFalse(createRectangle(10, 12, 40, 37).intersects(createSegment(-100, 60, -98, 61)));
	}

	@Override
	public void intersectsPath3afp() {
		Path3afp p;
		
		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(-20, 20);
		p.lineTo(20, 20);
		p.lineTo(20, -20);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));
		
		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(5, 8);
		p.lineTo(-20, 20);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertFalse(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(20, 20);
		p.lineTo(-20, 20);
		assertTrue(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(-20, 20);
		p.lineTo(20, -20);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertFalse(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, 20);
		p.lineTo(10, 8);
		p.lineTo(20, 18);
		assertTrue(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, 20);
		p.lineTo(20, 18);
		p.lineTo(10, 8);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));
	}

	@Override
	public void intersectsPathIterator3afp() {
		Path3afp<?, ?, ?, ?, ?, B> p;
		
		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(-20, 20);
		p.lineTo(20, 20);
		p.lineTo(20, -20);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));
		
		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(5, 8);
		p.lineTo(-20, 20);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertFalse(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(20, 20);
		p.lineTo(-20, 20);
		assertTrue(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(-20, 20);
		p.lineTo(20, -20);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertFalse(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, 20);
		p.lineTo(10, 8);
		p.lineTo(20, 18);
		assertTrue(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, 20);
		p.lineTo(20, 18);
		p.lineTo(10, 8);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));
	}

	@Override
	public void containsDoubleDouble() {
		this.shape.set(10, 12, 30, 25);
		assertFalse(this.shape.contains(20, 45));
		assertFalse(this.shape.contains(20, 55));
		assertFalse(this.shape.contains(20, 0));
		assertFalse(this.shape.contains(0, 45));
		assertFalse(this.shape.contains(5, 45));
		assertFalse(this.shape.contains(40, 55));
		assertFalse(this.shape.contains(40, 0));
		assertFalse(this.shape.contains(0, 40));
		assertFalse(this.shape.contains(20, 100));
		assertFalse(this.shape.contains(100, 45));
		assertFalse(this.shape.contains(-100, 45));
		assertFalse(this.shape.contains(-100, 60));
		assertTrue(this.shape.contains(10, 12));
		assertTrue(this.shape.contains(40, 12));
		assertTrue(this.shape.contains(40, 37));
		assertTrue(this.shape.contains(10, 37));
		assertTrue(this.shape.contains(35, 24));
	}

	@Override
	public void containsPoint3D() {
		this.shape.set(10, 12, 30, 25);
		assertFalse(this.shape.contains(createPoint(20, 45)));
		assertFalse(this.shape.contains(createPoint(20, 55)));
		assertFalse(this.shape.contains(createPoint(20, 0)));
		assertFalse(this.shape.contains(createPoint(0, 45)));
		assertFalse(this.shape.contains(createPoint(5, 45)));
		assertFalse(this.shape.contains(createPoint(40, 55)));
		assertFalse(this.shape.contains(createPoint(40, 0)));
		assertFalse(this.shape.contains(createPoint(0, 40)));
		assertFalse(this.shape.contains(createPoint(20, 100)));
		assertFalse(this.shape.contains(createPoint(100, 45)));
		assertFalse(this.shape.contains(createPoint(-100, 45)));
		assertFalse(this.shape.contains(createPoint(-100, 60)));
		assertTrue(this.shape.contains(createPoint(10, 12)));
		assertTrue(this.shape.contains(createPoint(40, 12)));
		assertTrue(this.shape.contains(createPoint(40, 37)));
		assertTrue(this.shape.contains(createPoint(10, 37)));
		assertTrue(this.shape.contains(createPoint(35, 24)));
	}

	@Test
	public void inflate() {
		this.shape.inflate(1, 2, 3, 4);
		assertEpsilonEquals(4, this.shape.getMinX());
		assertEpsilonEquals(6, this.shape.getMinY());
		assertEpsilonEquals(13, this.shape.getMaxX());
		assertEpsilonEquals(22, this.shape.getMaxY());
	}

	@Override
	public void intersectsShape3D() {
		assertTrue(createRectangle(0, 0, 5.1, 100).intersects((Shape3D) this.shape));
		assertTrue(createRectangle(.25, .25, .5, .5).intersects((Shape3D) createEllipse(0, 0, 1, 1)));
	}

	@Override
	public void operator_addVector3D() {
		this.shape.operator_add(createVector(123.456, 456.789));
		assertEpsilonEquals(128.456, this.shape.getMinX());
		assertEpsilonEquals(464.789, this.shape.getMinY());
		assertEpsilonEquals(133.456, this.shape.getMaxX());
		assertEpsilonEquals(474.789, this.shape.getMaxY());
	}

	@Override
	public void operator_plusVector3D() {
		T shape = this.shape.operator_plus(createVector(123.456, 456.789));
		assertEpsilonEquals(128.456, shape.getMinX());
		assertEpsilonEquals(464.789, shape.getMinY());
		assertEpsilonEquals(133.456, shape.getMaxX());
		assertEpsilonEquals(474.789, shape.getMaxY());
	}

	@Override
	public void operator_removeVector3D() {
		this.shape.operator_remove(createVector(123.456, 456.789));
		assertEpsilonEquals(-118.456, this.shape.getMinX());
		assertEpsilonEquals(-448.789, this.shape.getMinY());
		assertEpsilonEquals(-113.456, this.shape.getMaxX());
		assertEpsilonEquals(-438.789, this.shape.getMaxY());
	}

	@Override
	public void operator_minusVector3D() {
		T shape = this.shape.operator_minus(createVector(123.456, 456.789));
		assertEpsilonEquals(-118.456, shape.getMinX());
		assertEpsilonEquals(-448.789, shape.getMinY());
		assertEpsilonEquals(-113.456, shape.getMaxX());
		assertEpsilonEquals(-438.789, shape.getMaxY());
	}

	@Override
	public void operator_multiplyTransform3D() {
		Transform3D tr;
		tr = new Transform3D();
		tr.setTranslation(123.456, 456.789);
		PathIterator3afp pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 128.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 474.789);
		assertElement(pi, PathElementType.LINE_TO, 128.456, 474.789);
		assertElement(pi, PathElementType.CLOSE, 128.456, 464.789);
		assertNoElement(pi);
	}

	@Override
	public void operator_andPoint3D() {
		this.shape.set(10, 12, 30, 25);
		assertFalse(this.shape.operator_and(createPoint(20, 45)));
		assertFalse(this.shape.operator_and(createPoint(20, 55)));
		assertFalse(this.shape.operator_and(createPoint(20, 0)));
		assertFalse(this.shape.operator_and(createPoint(0, 45)));
		assertFalse(this.shape.operator_and(createPoint(5, 45)));
		assertFalse(this.shape.operator_and(createPoint(40, 55)));
		assertFalse(this.shape.operator_and(createPoint(40, 0)));
		assertFalse(this.shape.operator_and(createPoint(0, 40)));
		assertFalse(this.shape.operator_and(createPoint(20, 100)));
		assertFalse(this.shape.operator_and(createPoint(100, 45)));
		assertFalse(this.shape.operator_and(createPoint(-100, 45)));
		assertFalse(this.shape.operator_and(createPoint(-100, 60)));
		assertTrue(this.shape.operator_and(createPoint(10, 12)));
		assertTrue(this.shape.operator_and(createPoint(40, 12)));
		assertTrue(this.shape.operator_and(createPoint(40, 37)));
		assertTrue(this.shape.operator_and(createPoint(10, 37)));
		assertTrue(this.shape.operator_and(createPoint(35, 24)));
	}

	@Override
	public void operator_andShape3D() {
		assertTrue(createRectangle(0, 0, 5.1, 100).operator_and(this.shape));
		assertTrue(createRectangle(.25, .25, .5, .5).operator_and(createEllipse(0, 0, 1, 1)));
	}

	@Override
	public void operator_upToPoint3D() {
		assertEpsilonEquals(9.43398, this.shape.operator_upTo(createPoint(0, 0)));
		assertEpsilonEquals(90.35486, this.shape.operator_upTo(createPoint(100, 0)));
		assertEpsilonEquals(121.75385, this.shape.operator_upTo(createPoint(100, 100)));
		assertEpsilonEquals(82.1523, this.shape.operator_upTo(createPoint(0, 100)));
		assertEpsilonEquals(5, this.shape.operator_upTo(createPoint(0, 10)));
		assertEpsilonEquals(8, this.shape.operator_upTo(createPoint(7, 0)));
		assertEpsilonEquals(144, this.shape.operator_upTo(createPoint(154, 17)));
		assertEpsilonEquals(136, this.shape.operator_upTo(createPoint(9, 154)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(8, 18)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(7, 12)));
	}

	@Test
	public void getCenter() {
		Point3D p = this.shape.getCenter();
		assertNotNull(p);
		assertEpsilonEquals(7.5, p.getX());
		assertEpsilonEquals(13, p.getY());
	}

	@Test
	public void getCenterX() {
		assertEpsilonEquals(7.5, this.shape.getCenterX());
	}

	@Test
	public void getCenterY() {
		assertEpsilonEquals(13, this.shape.getCenterY());
	}

	@Test
	public void setCenterDoubleDouble() {
		this.shape.setCenter(145,  -47);
		assertEpsilonEquals(142.5, this.shape.getMinX());
		assertEpsilonEquals(-52, this.shape.getMinY());
		assertEpsilonEquals(147.5, this.shape.getMaxX());
		assertEpsilonEquals(-42, this.shape.getMaxY());
	}

	@Test
	public void setCenterXDouble() {
		this.shape.setCenterX(145);
		assertEpsilonEquals(142.5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(147.5, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@Test
	public void setCenterYDouble() {
		this.shape.setCenterY(-47);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(-52, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(-42, this.shape.getMaxY());
	}

	@Test
	public void getFirstAxis() {
		Vector3D v = this.shape.getFirstAxis();
		assertNotNull(v);
		assertEpsilonEquals(1, v.getX());
		assertEpsilonEquals(0, v.getY());
	}

	@Test
	public void getFirstAxisX() {
		assertEpsilonEquals(1, this.shape.getFirstAxisX());
	}

	@Test
	public void getFirstAxisY() {
		assertEpsilonEquals(0, this.shape.getFirstAxisY());
	}

	@Test
	public void getSecondAxis() {
		Vector3D v = this.shape.getSecondAxis();
		assertNotNull(v);
		assertEpsilonEquals(0, v.getX());
		assertEpsilonEquals(1, v.getY());
	}

	@Test
	public void getSecondAxisX() {
		assertEpsilonEquals(0, this.shape.getSecondAxisX());
	}

	@Test
	public void getSecondAxisY() {
		assertEpsilonEquals(1, this.shape.getSecondAxisY());
	}

	@Test
	public void getFirstAxisExtent() {
		assertEpsilonEquals(2.5, this.shape.getFirstAxisExtent());
	}

	@Test
	public void setFirstAxisExtent() {
		this.shape.setFirstAxisExtent(124);
		assertEpsilonEquals(-116.5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(131.5, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@Test
	public void getSecondAxisExtent() {
		assertEpsilonEquals(5, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setSecondAxisExtent() {
		this.shape.setSecondAxisExtent(124);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(-111, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(137, this.shape.getMaxY());
	}

	@Test
	public void setFirstAxisDoubleDoubleDouble() {
		Vector3D v = createVector(1,  1).toUnitVector();
		this.shape.setFirstAxis(v.getX(), v.getY(), 5);
		assertEpsilonEquals(3.96447, this.shape.getMinX());
		assertEpsilonEquals(9.46446, this.shape.getMinY());
		assertEpsilonEquals(11.03553, this.shape.getMaxX());
		assertEpsilonEquals(16.53553, this.shape.getMaxY());
	}

	@Test
	public void setSecondAxisDoubleDoubleDouble() {
		Vector3D v = createVector(-1,  1).toUnitVector();
		this.shape.setSecondAxis(v.getX(), v.getY(), 6);
		assertEpsilonEquals(3.25736, this.shape.getMinX());
		assertEpsilonEquals(8.75736, this.shape.getMinY());
		assertEpsilonEquals(11.74264, this.shape.getMaxX());
		assertEpsilonEquals(17.24264, this.shape.getMaxY());
	}

	@Test
	public void setDoubleDoubleDoubleDoubleDoubleDouble() {
		this.shape.set(1, 2, -1, 0, 5, 6);
		assertEpsilonEquals(-4, this.shape.getMinX());
		assertEpsilonEquals(-4, this.shape.getMinY());
		assertEpsilonEquals(6, this.shape.getMaxX());
		assertEpsilonEquals(8, this.shape.getMaxY());

		Vector3D v = createVector(1,  1).toUnitVector();
		this.shape.set(1, 2, v.getX(), v.getY(), 5, 6);
		assertEpsilonEquals(-3.24264, this.shape.getMinX());
		assertEpsilonEquals(-2.24264, this.shape.getMinY());
		assertEpsilonEquals(5.24264, this.shape.getMaxX());
		assertEpsilonEquals(6.24264, this.shape.getMaxY());
	}

}
