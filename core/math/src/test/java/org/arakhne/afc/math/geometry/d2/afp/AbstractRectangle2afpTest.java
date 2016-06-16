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

package org.arakhne.afc.math.geometry.d2.afp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;

@SuppressWarnings("all")
public abstract class AbstractRectangle2afpTest<T extends Rectangle2afp<?, T, ?, ?, ?, B>,
		B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractRectangularShape2afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createRectangle(5, 8, 5, 10);
	}

	@Test
	public void staticIntersectsRectangleRectangle() {
		assertFalse(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 0, 1, 1));
		assertFalse(Rectangle2afp.intersectsRectangleRectangle(0, 0, 1, 1, 5, 8, 10, 18));

		assertFalse(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 20, 1, 22));
		assertFalse(Rectangle2afp.intersectsRectangleRectangle(0, 20, 1, 22, 5, 8, 10, 18));

		assertFalse(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 0, 5, 100));
		assertFalse(Rectangle2afp.intersectsRectangleRectangle(0, 0, 5, 100, 5, 8, 10, 18));

		assertTrue(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 0, 5.1, 100));
		assertTrue(Rectangle2afp.intersectsRectangleRectangle(0, 0, 5.1, 100, 5, 8, 10, 18));

		assertTrue(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 6, 9, 9.5, 15));
		assertTrue(Rectangle2afp.intersectsRectangleRectangle(6, 9, 9.5, 15, 5, 8, 10, 18));

		assertTrue(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 6, 9, 9.5, 15));
		assertTrue(Rectangle2afp.intersectsRectangleRectangle(6, 9, 9.5, 15, 5, 8, 10, 18));
	}

	@Test
	public void staticIntersectsRectangleLine() {
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 45, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 55, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 0, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 0, 45, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 45, 60, 15));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 5, 45, 30, 55));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 40, 55, 60, 15));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 40, 0, 60, 40));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 0, 40, 20, 0));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 0, 45, 100, 15));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 100, 43, 0));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 100, 43, 101));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 100, 45, 102, 15));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 0, 43, -2));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 50, 49, -100, 45, -48, 15));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, -100, 60, -98, 61));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 0, 30, 9, 21));
	}

	@Test
	public void staticIntersectsRectangleSegment() {
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 45, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 55, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 0, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 45, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 45, 60, 15));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 5, 45, 30, 55));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 40, 55, 60, 15));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 40, 0, 60, 40));
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 40, 20, 0));
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 45, 100, 15));
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 100, 43, 0));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 100, 43, 101));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 100, 45, 102, 15));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 0, 43, -2));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 50, 49, -100, 45, -48, 15));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, -100, 60, -98, 61));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 30, 9, 21));
	}

	@Test
	public void staticReducesCohenSutherlandZoneRectangleSegment() {
		Point2D p1 = createPoint(0, 0);
		Point2D p2 = createPoint(0, 0);
		
		assertEquals(0,
				Rectangle2afp.reducesCohenSutherlandZoneRectangleSegment(10, 12, 40, 37, 20, 45, 43, 15,
				MathUtil.getCohenSutherlandCode(20, 45, 0, 12, 40, 37),
				MathUtil.getCohenSutherlandCode(43, 15, 0, 12, 40, 37),
				p1, p2));
		assertFpPointEquals(26.13333, 37, p1);
		assertFpPointEquals(40, 18.91304, p2);

		assertEquals(0, 
				Rectangle2afp.reducesCohenSutherlandZoneRectangleSegment(10, 12, 40, 37, 20, 55, 43, 15,
				MathUtil.getCohenSutherlandCode(20, 55, 0, 12, 40, 37),
				MathUtil.getCohenSutherlandCode(43, 15, 0, 12, 40, 37),
				p1, p2));
		assertFpPointEquals(30.35, 37, p1);
		assertFpPointEquals(40, 20.21739, p2);
	}

	@Test
	public void staticContainsRectangleRectangle() {
		assertFalse(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 0, 0, 1, 1));
		assertFalse(Rectangle2afp.containsRectangleRectangle(0, 0, 1, 1, 5, 8, 10, 18));

		assertFalse(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 0, 20, 1, 22));
		assertFalse(Rectangle2afp.containsRectangleRectangle(0, 20, 1, 22, 5, 8, 10, 18));

		assertFalse(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 0, 0, 5, 100));
		assertFalse(Rectangle2afp.containsRectangleRectangle(0, 0, 5, 100, 5, 8, 10, 18));

		assertFalse(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 0, 0, 5.1, 100));
		assertFalse(Rectangle2afp.containsRectangleRectangle(0, 0, 5.1, 100, 5, 8, 10, 18));

		assertTrue(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 6, 9, 9.5, 15));
		assertFalse(Rectangle2afp.containsRectangleRectangle(6, 9, 9.5, 15, 5, 8, 10, 18));
	}

	@Test
	public void staticContainsRectanglePoint() {
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 20, 45));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 20, 55));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 20, 0));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 0, 45));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 5, 45));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 40, 55));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 40, 0));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 0, 40));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 20, 100));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 100, 45));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 50, 49, -100, 45));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, -100, 60));
		assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 10, 12));
		assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 40, 12));
		assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 40, 37));
		assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 10, 37));
		assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 35, 24));
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
		assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
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
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createRectangle(5, 8, 5, 10)));
	}

	@Test
	public void addPoint2D() {
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
	public void avoidCollisionWithRectangle2afpVector2D() {
		B r = createRectangle(0, 0, 7, 10);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector2D v = createVector(Double.NaN, Double.NaN);
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
	public void avoidCollisionWithRectangle2afpVector2DVector2D_nullDisplacement() {
		B r = createRectangle(0, 0, 7, 10);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector2D v = createVector(Double.NaN, Double.NaN);
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
	public void avoidCollisionWithRectangle2afpVector2DVector2D_noDisplacement() {
		B r = createRectangle(0, 0, 7, 10);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector2D v1 = createVector(0, 0);
		Vector2D v2 = createVector(Double.NaN, Double.NaN);
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
	public void avoidCollisionWithRectangle2afpVector2DVector2D_givenDisplacement() {
		B r = createRectangle(0, 0, 7, 10);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector2D v1 = createVector(-4, 4);
		Vector2D v2 = createVector(Double.NaN, Double.NaN);
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

	@Test
	public void staticFindsClosestPointRectanglePoint() {
		Point2D p;
		
		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 0, 0, p);
		assertFpPointEquals(5, 8, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 100, 0, p);
		assertFpPointEquals(10, 8, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 100, 100, p);
		assertFpPointEquals(10, 18, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 0, 100, p);
		assertFpPointEquals(5, 18, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 0, 10, p);
		assertFpPointEquals(5, 10, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 7, 0, p);
		assertFpPointEquals(7, 8, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 154, 17, p);
		assertFpPointEquals(10, 17, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 9, 154, p);
		assertFpPointEquals(9, 18, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 8, 18, p);
		assertFpPointEquals(8, 18, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 7, 12, p);
		assertFpPointEquals(7, 12, p);
	}

	@Override
	public void getClosestPointTo() {
		Point2D p;
		
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
		Point2D p;
		
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

	@Test
	public void staticCalculatesDistanceSquaredRectanglePoint() {
		assertEpsilonEquals(88.99998, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 0, 0));
		assertEpsilonEquals(8164, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 100, 0));
		assertEpsilonEquals(14823.99999, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 100, 100));
		assertEpsilonEquals(6749, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 0, 100));
		assertEpsilonEquals(25, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 0, 10));
		assertEpsilonEquals(64, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 7, 0));
		assertEpsilonEquals(20736, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 154, 17));
		assertEpsilonEquals(18496, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 9, 154));
		assertEpsilonEquals(0, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 8, 18));
		assertEpsilonEquals(0, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 7, 12));
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
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 18);
		assertElement(pi, PathElementType.LINE_TO, 5, 18);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertNoElement(pi);
	}

	@Override
	public void getPathIteratorTransform2D() {
		PathIterator2afp pi;
		
		pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 18);
		assertElement(pi, PathElementType.LINE_TO, 5, 18);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertNoElement(pi);

		pi = this.shape.getPathIterator(new Transform2D());
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 18);
		assertElement(pi, PathElementType.LINE_TO, 5, 18);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertNoElement(pi);

		Transform2D tr;
		tr = new Transform2D();
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
		Transform2D tr;
		tr = new Transform2D();
		tr.setTranslation(123.456, 456.789);
		PathIterator2afp pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 128.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 474.789);
		assertElement(pi, PathElementType.LINE_TO, 128.456, 474.789);
		assertElement(pi, PathElementType.CLOSE, 128.456, 464.789);
		assertNoElement(pi);
	}

	@Override
	public void containsRectangle2afp() {
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
	public void containsShape2D() {
		assertFalse(this.shape.contains(createCircle(0, 0, 1)));
		assertFalse(this.shape.contains(createCircle(0, 20, 1)));
		assertFalse(this.shape.contains(createCircle(0, 0, 5)));
		assertFalse(this.shape.contains(createCircle(0, 0, 5.1)));
		assertTrue(this.shape.contains(createCircle(6, 9, .5)));
	}

	@Override
	public void intersectsRectangle2afp() {
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
	public void intersectsTriangle2afp() {
		Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
		assertFalse(createRectangle(-6, -2, 1, 1).intersects(triangle));
		assertFalse(createRectangle(-6, 6, 1, 1).intersects(triangle));
		assertFalse(createRectangle(6, 6, 1, 1).intersects(triangle));
		assertFalse(createRectangle(-16, 0, 1, 1).intersects(triangle));
		assertFalse(createRectangle(12, 12, 1, 1).intersects(triangle));
		assertFalse(createRectangle(0, -6, 1, 1).intersects(triangle));
		assertTrue(createRectangle(-4, 2, 1, 1).intersects(triangle));
		assertTrue(createRectangle(-4, 4, 1, 1).intersects(triangle));
		assertTrue(createRectangle(0, 6, 1, 1).intersects(triangle));
		assertTrue(createRectangle(2, 4, 1, 1).intersects(triangle));
		assertFalse(createRectangle(5, 8, 1, 1).intersects(triangle));
	}

	@Override
	public void intersectsCircle2afp() {
		assertTrue(createRectangle(0, 0, .5, .5).intersects(createCircle(0, 0, 1)));
		assertTrue(createRectangle(0, 0, 1, 1).intersects(createCircle(0, 0, 1)));
		assertTrue(createRectangle(0, 0, .5, 1).intersects(createCircle(0, 0, 1)));
		assertFalse(createRectangle(0, 0, 1, 1).intersects(createCircle(2, 0, 1)));
	}

	@Override
	public void intersectsEllipse2afp() {
		assertTrue(createRectangle(0, 0, 1, 1).intersects(createEllipse(0, 0, 1, 1)));
		assertFalse(createRectangle(-5, -5, 1, 1).intersects(createEllipse(0, 0, 1, 1)));
		assertTrue(createRectangle(.5, .5, 5, 5).intersects(createEllipse(0, 0, 1, 1)));
		assertTrue(createRectangle(.5, .5, 5, .6).intersects(createEllipse(0, 0, 1, 1)));
		assertFalse(createRectangle(-5, -5, 5, 5).intersects(createEllipse(0, 0, 1, 1)));
		assertFalse(createRectangle(-9, -9, 4, 4).intersects(createEllipse(0, 0, 1, 1)));
		assertFalse(createRectangle(-5, -9, 4, 4).intersects(createEllipse(0, 0, 1, 1)));
		assertFalse(createRectangle(5, -5, 6, 5).intersects(createEllipse(0, 0, 1, 1)));
		assertFalse(createRectangle(-5, -5, 0, 0).intersects(createEllipse(0, 0, 1, 1)));
		assertFalse(createRectangle(-5, -5, .1, .1).intersects(createEllipse(0, 0, 1, 1)));
		assertTrue(createRectangle(.25, .25, .5, .5).intersects(createEllipse(0, 0, 1, 1)));
	}

	@Override
	public void intersectsSegment2afp() {
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
	public void intersectsPath2afp() {
		Path2afp p;
		
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
	public void intersectsPathIterator2afp() {
		Path2afp<?, ?, ?, ?, ?, B> p;
		
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
	public void intersectsOrientedRectangle2afp() {
		OrientedRectangle2afp obr = createOrientedRectangle(
				6, 9,
				0.894427190999916, -0.447213595499958, 13.999990000000002,
				12.999989999999997);
		assertFalse(createRectangle(0, -5, 2, 1).intersects(obr));
		assertTrue(createRectangle(0, -4.5, 2, 1).intersects(obr));
		assertTrue(createRectangle(0, -4, 2, 1).intersects(obr));
		assertTrue(createRectangle(4, 4, 2, 1).intersects(obr));
		assertFalse(createRectangle(20, -2, 2, 1).intersects(obr));
		assertTrue(createRectangle(-15, -10, 50, 50).intersects(obr));
	}

	@Test
	@Override
	public void intersectsParallelogram2afp() {
		Parallelogram2afp para = createParallelogram(
				6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
				-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
		assertFalse(createRectangle(0, 0, 1, 1).intersects(para));
		assertTrue(createRectangle(0, 2, 1, 1).intersects(para));
		assertTrue(createRectangle(-5.5, 8.5, 1, 1).intersects(para));
		assertFalse(createRectangle(-6, 16, 1, 1).intersects(para));
		assertFalse(createRectangle(146, 16, 1, 1).intersects(para));
		assertTrue(createRectangle(12, 14, 1, 1).intersects(para));
		assertTrue(createRectangle(0, 8, 1, 1).intersects(para));
		assertTrue(createRectangle(10, -1, 1, 1).intersects(para));
		assertTrue(createRectangle(-15, -10, 35, 40).intersects(para));
	}

	@Override
	public void intersectsRoundRectangle2afp() {
		assertFalse(this.shape.intersects(createRoundRectangle(0, 0, 1, 1, .1, .2)));
		assertFalse(createRoundRectangle(0, 0, 1, 1, .1, .2).intersects(this.shape));

		assertFalse(this.shape.intersects(createRoundRectangle(0, 20, 1, 2, .1, .2)));
		assertFalse(createRoundRectangle(0, 20, 1, 2, .1, .2).intersects(this.shape));

		assertFalse(this.shape.intersects(createRoundRectangle(0, 0, 5, 100, .1, .2)));
		assertFalse(createRoundRectangle(0, 0, 5, 100, .1, .2).intersects(this.shape));

		assertTrue(this.shape.intersects(createRoundRectangle(0, 0, 5.1, 100, .1, .2)));
		assertTrue(createRoundRectangle(0, 0, 5.1, 100, .1, .2).intersects(this.shape));

		assertTrue(this.shape.intersects(createRoundRectangle(6, 9, .5, 9, .1, .2)));
		assertTrue(createRoundRectangle(6, 9, .5, 9, .1, .2).intersects(this.shape));

		assertTrue(this.shape.intersects(createRoundRectangle(0, 0, 5.1, 8.1, .1, .2)));
		assertTrue(createRoundRectangle(0, 0, 5.1, 8.1, .1, .2).intersects(this.shape));

		assertFalse(this.shape.intersects(createRoundRectangle(0, 0, 5.01, 8.01, .1, .2)));
		assertFalse(createRoundRectangle(0, 0, 5.01, 8.01, .1, .2).intersects(this.shape));
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
	public void containsPoint2D() {
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

	@Override
	@Test
	public void inflate() {
		this.shape.inflate(1, 2, 3, 4);
		assertEpsilonEquals(4, this.shape.getMinX());
		assertEpsilonEquals(6, this.shape.getMinY());
		assertEpsilonEquals(13, this.shape.getMaxX());
		assertEpsilonEquals(22, this.shape.getMaxY());
	}

	@Override
	public void intersectsShape2D() {
		assertTrue(createRectangle(0, 0, 5.1, 100).intersects((Shape2D) this.shape));
		assertTrue(createRectangle(.25, .25, .5, .5).intersects((Shape2D) createEllipse(0, 0, 1, 1)));
	}

	@Override
	public void operator_addVector2D() {
		this.shape.operator_add(createVector(123.456, 456.789));
		assertEpsilonEquals(128.456, this.shape.getMinX());
		assertEpsilonEquals(464.789, this.shape.getMinY());
		assertEpsilonEquals(133.456, this.shape.getMaxX());
		assertEpsilonEquals(474.789, this.shape.getMaxY());
	}

	@Override
	public void operator_plusVector2D() {
		T shape = this.shape.operator_plus(createVector(123.456, 456.789));
		assertEpsilonEquals(128.456, shape.getMinX());
		assertEpsilonEquals(464.789, shape.getMinY());
		assertEpsilonEquals(133.456, shape.getMaxX());
		assertEpsilonEquals(474.789, shape.getMaxY());
	}

	@Override
	public void operator_removeVector2D() {
		this.shape.operator_remove(createVector(123.456, 456.789));
		assertEpsilonEquals(-118.456, this.shape.getMinX());
		assertEpsilonEquals(-448.789, this.shape.getMinY());
		assertEpsilonEquals(-113.456, this.shape.getMaxX());
		assertEpsilonEquals(-438.789, this.shape.getMaxY());
	}

	@Override
	public void operator_minusVector2D() {
		T shape = this.shape.operator_minus(createVector(123.456, 456.789));
		assertEpsilonEquals(-118.456, shape.getMinX());
		assertEpsilonEquals(-448.789, shape.getMinY());
		assertEpsilonEquals(-113.456, shape.getMaxX());
		assertEpsilonEquals(-438.789, shape.getMaxY());
	}

	@Override
	public void operator_multiplyTransform2D() {
		Transform2D tr;
		tr = new Transform2D();
		tr.setTranslation(123.456, 456.789);
		PathIterator2afp pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 128.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 474.789);
		assertElement(pi, PathElementType.LINE_TO, 128.456, 474.789);
		assertElement(pi, PathElementType.CLOSE, 128.456, 464.789);
		assertNoElement(pi);
	}

	@Override
	public void operator_andPoint2D() {
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
	public void operator_andShape2D() {
		assertTrue(createRectangle(0, 0, 5.1, 100).operator_and(this.shape));
		assertTrue(createRectangle(.25, .25, .5, .5).operator_and(createEllipse(0, 0, 1, 1)));
	}

	@Override
	public void operator_upToPoint2D() {
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
		Point2D p = this.shape.getCenter();
		assertNotNull(p);
		assertEpsilonEquals(7.5, p.getX());
		assertEpsilonEquals(13, p.getY());
	}

	@Override
	@Test
	public void getCenterX() {
		assertEpsilonEquals(7.5, this.shape.getCenterX());
	}

	@Override
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
		Vector2D v = this.shape.getFirstAxis();
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
		Vector2D v = this.shape.getSecondAxis();
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
		Vector2D v = createVector(1,  1).toUnitVector();
		this.shape.setFirstAxis(v.getX(), v.getY(), 5);
		assertEpsilonEquals(3.96447, this.shape.getMinX());
		assertEpsilonEquals(9.46446, this.shape.getMinY());
		assertEpsilonEquals(11.03553, this.shape.getMaxX());
		assertEpsilonEquals(16.53553, this.shape.getMaxY());
	}

	@Test
	public void setSecondAxisDoubleDoubleDouble() {
		Vector2D v = createVector(-1,  1).toUnitVector();
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

		Vector2D v = createVector(1,  1).toUnitVector();
		this.shape.set(1, 2, v.getX(), v.getY(), 5, 6);
		assertEpsilonEquals(-3.24264, this.shape.getMinX());
		assertEpsilonEquals(-2.24264, this.shape.getMinY());
		assertEpsilonEquals(5.24264, this.shape.getMaxX());
		assertEpsilonEquals(6.24264, this.shape.getMaxY());
	}

	@Test
	public void isCCW() {
		assertTrue(this.shape.isCCW());
		assertTrue(createRectangle(
				4.7, 15,
				18.02776, 20).isCCW());
		assertTrue(createRectangle(
				-10, -3,
				2, 1).isCCW());
		assertTrue(createRectangle(
				-10, 7,
				1, 2).isCCW());
	}

	@Override
	@Test
	public void getClosestPointToCircle2afp() {
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createCircle(0, 0, 1)));
		assertFpPointEquals(5, 14, this.shape.getClosestPointTo(createCircle(-2, 14, 1)));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createCircle(-1, 21, 1)));
		assertFpPointEquals(7, 18, this.shape.getClosestPointTo(createCircle(7, 21, 1)));
		assertClosestPointInBothShapes(this.shape, createCircle(9, 13, 1));
		assertFpPointEquals(8, 8, this.shape.getClosestPointTo(createCircle(8, 4, 1)));
		assertFpPointEquals(10, 8, this.shape.getClosestPointTo(createCircle(20, 0, 1)));
		assertFpPointEquals(10, 14, this.shape.getClosestPointTo(createCircle(19, 14, 1)));
		assertFpPointEquals(10, 18, this.shape.getClosestPointTo(createCircle(18, 21, 1)));
		assertClosestPointInBothShapes(this.shape, createCircle(5, 18, 1));
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createCircle(4.3, 7, 1)));
	}

	@Override
	@Test
	public void getDistanceSquaredCircle2afp() {
		assertEpsilonEquals(71.13204, this.shape.getDistanceSquared(createCircle(0, 0, 1)));
		assertEpsilonEquals(36, this.shape.getDistanceSquared(createCircle(-2, 14, 1)));
		assertEpsilonEquals(32.58359, this.shape.getDistanceSquared(createCircle(-1, 21, 1)));
		assertEpsilonEquals(4, this.shape.getDistanceSquared(createCircle(7, 21, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(9, 13, 1)));
		assertEpsilonEquals(9, this.shape.getDistanceSquared(createCircle(8, 4, 1)));
		assertEpsilonEquals(139.3875, this.shape.getDistanceSquared(createCircle(20, 0, 1)));
		assertEpsilonEquals(64, this.shape.getDistanceSquared(createCircle(19, 14, 1)));
		assertEpsilonEquals(56.91199, this.shape.getDistanceSquared(createCircle(18, 21, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(5, 18, 1)));
		assertEpsilonEquals(0.04869, this.shape.getDistanceSquared(createCircle(4.3, 7, 1)));
	}

	@Test
	public void staticFindsClosestPointRectangleRectangle() {
		Point2D result;
		
		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 0, 0, 1, 1, result);
		assertFpPointEquals(5, 8, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 0, 12, 1, 13, result);
		assertFpPointEquals(5, 12.5, result);
		
		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 0, 21, 1, 22, result);
		assertFpPointEquals(5, 18, result);
		
		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 7, 0, 8, 1, result);
		assertFpPointEquals(7.5, 8, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 8, 12, 9, 13, result);
		assertFpPointEquals(8.5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 9, 21, 10, 22, result);
		assertFpPointEquals(9.5, 18, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 15, 0, 16, 1, result);
		assertFpPointEquals(10, 8, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 16, 12, 17, 13, result);
		assertFpPointEquals(10, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 17, 21, 18, 22, result);
		assertFpPointEquals(10, 18, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 1, 12, 11, 13, result);
		assertFpPointEquals(6, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 1, 12, 11, 32, result);
		assertFpPointEquals(6, 18, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 8, 0, 9, 20, result);
		assertFpPointEquals(8.5, 10, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 8, 0, 18, 20, result);
		assertFpPointEquals(10, 10, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 6, 10, 7, 11, result);
		assertFpPointEquals(6.5, 10.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4, 12, 5, 13, result);
		assertFpPointEquals(5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.1, 12, 5.1, 13, result);
		assertFpPointEquals(5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.2, 12, 5.2, 13, result);
		assertFpPointEquals(5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.3, 12, 5.3, 13, result);
		assertFpPointEquals(5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.4, 12, 5.4, 13, result);
		assertFpPointEquals(5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.5, 12, 5.5, 13, result);
		assertFpPointEquals(5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.6, 12, 5.6, 13, result);
		assertFpPointEquals(5.1, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.7, 12, 5.7, 13, result);
		assertFpPointEquals(5.2, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.8, 12, 5.8, 13, result);
		assertFpPointEquals(5.3, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.9, 12, 5.9, 13, result);
		assertFpPointEquals(5.4, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 5, 12, 6, 13, result);
		assertFpPointEquals(5.5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 5.1, 12, 6.1, 13, result);
		assertFpPointEquals(5.6, 12.5, result);
	}

	@Override
	@Test
	public void getClosestPointToRectangle2afp() {
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createRectangle(0, 0, 1, 1)));
		assertFpPointEquals(5, 12.5, this.shape.getClosestPointTo(createRectangle(0, 12, 1, 1)));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createRectangle(0, 21, 1, 1)));
		assertFpPointEquals(7.5, 8, this.shape.getClosestPointTo(createRectangle(7, 0, 1, 1)));
		assertClosestPointInBothShapes(this.shape, createRectangle(8, 12, 1, 1));
		assertFpPointEquals(9.5, 18, this.shape.getClosestPointTo(createRectangle(9, 21, 1, 1)));
		assertFpPointEquals(10, 8, this.shape.getClosestPointTo(createRectangle(15, 0, 1, 1)));
		assertFpPointEquals(10, 12.5, this.shape.getClosestPointTo(createRectangle(16, 12, 1, 1)));
		assertFpPointEquals(10, 18, this.shape.getClosestPointTo(createRectangle(17, 21, 1, 1)));
		assertFpPointEquals(6, 12.5, this.shape.getClosestPointTo(createRectangle(1, 12, 10, 1)));
		assertClosestPointInBothShapes(this.shape, createRectangle(1, 12, 10, 20));
		assertClosestPointInBothShapes(this.shape, createRectangle(8, 0, 1, 20));
		assertClosestPointInBothShapes(this.shape, createRectangle(8, 0, 10, 20));
		assertClosestPointInBothShapes(this.shape, createRectangle(6, 10, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.1, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.2, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.3, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.4, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.5, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.6, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.7, 12, 1, 1));
		assertClosestPointInBothShapes( this.shape, createRectangle(4.8, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.9, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(5, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(5.1, 12, 1, 1));
	}

	@Override
	@Test
	public void getDistanceSquaredRectangle2afp() {
		assertEpsilonEquals(65, this.shape.getDistanceSquared(createRectangle(0, 0, 1, 1)));
		assertEpsilonEquals(16, this.shape.getDistanceSquared(createRectangle(0, 12, 1, 1)));
		assertEpsilonEquals(25, this.shape.getDistanceSquared(createRectangle(0, 21, 1, 1)));
		assertEpsilonEquals(49, this.shape.getDistanceSquared(createRectangle(7, 0, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(8, 12, 1, 1)));
		assertEpsilonEquals(9, this.shape.getDistanceSquared(createRectangle(9, 21, 1, 1)));
		assertEpsilonEquals(74, this.shape.getDistanceSquared(createRectangle(15, 0, 1, 1)));
		assertEpsilonEquals(36, this.shape.getDistanceSquared(createRectangle(16, 12, 1, 1)));
		assertEpsilonEquals(58, this.shape.getDistanceSquared(createRectangle(17, 21, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(1, 12, 10, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(1, 12, 10, 20)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(8, 0, 1, 20)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(8, 0, 10, 20)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(6, 10, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.1, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.2, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.3, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.4, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.5, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.6, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.7, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.8, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.9, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(5, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(5.1, 12, 1, 1)));
	}

	@Test
	public void staticFindsClosestPointRectangleSegment() {
		Point2D<?, ?> result;
		
		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 0, 0, 2, 1, result);
		assertFpPointEquals(5, 8, result);
		
		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 0, 0, 1, 2, result);
		assertFpPointEquals(5, 8, result);
		
		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 1, 10, 4, 14, result);
		assertFpPointEquals(5, 14, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 1, 10, 6, 14, result);
		assertFpPointEquals(5, 13.2, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 1, 10, 18, 14, result);
		assertFpPointEquals(5, 10.94118, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 7, 10, 18, 14, result);
		assertFpPointEquals(7, 10, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 17, 10, 18, 14, result);
		assertFpPointEquals(10, 10, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 0, 9, 6, result);
		assertFpPointEquals(9, 8, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 0, 9, 16, result);
		assertFpPointEquals(7.5, 8, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 0, 9, 21, result);
		assertFpPointEquals(7.14286, 8, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 10, 9, 21, result);
		assertFpPointEquals(6, 10, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 19, 9, 21, result);
		assertFpPointEquals(6, 18, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 8, 20, 14, 8, result);
		assertFpPointEquals(9, 18, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 10.593538844843389, 12.775717435385788, 9.484138452392932, 14.439818024061475, result);
		assertFpPointEquals(10, 13.66603, result);
	}

	@Override
	@Test
	public void getClosestPointToSegment2afp() {
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createSegment(0, 0, 2, 1)));
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createSegment(0, 0, 1, 2)));
		assertFpPointEquals(5, 14, this.shape.getClosestPointTo(createSegment(1, 10, 4, 14)));
		assertClosestPointInBothShapes(this.shape, createSegment(1, 10, 6, 14));
		assertClosestPointInBothShapes(this.shape, createSegment(1, 10, 18, 14));
		assertClosestPointInBothShapes(this.shape, createSegment(7, 10, 18, 14));
		assertFpPointEquals(10, 10, this.shape.getClosestPointTo(createSegment(17, 10, 18, 14)));
		assertFpPointEquals(9, 8, this.shape.getClosestPointTo(createSegment(6, 0, 9, 6)));
		assertClosestPointInBothShapes(this.shape, createSegment(6, 0, 9, 16));
		assertClosestPointInBothShapes(this.shape, createSegment(6, 0, 9, 21));
		assertClosestPointInBothShapes(this.shape, createSegment(6, 10, 9, 21));
		assertFpPointEquals(6, 18, this.shape.getClosestPointTo(createSegment(6, 19, 9, 21)));
		assertClosestPointInBothShapes(this.shape, createSegment(8, 20, 14, 8));
	}

	@Override
	@Test
	public void getDistanceSquaredSegment2afp() {
		assertEpsilonEquals(58, this.shape.getDistanceSquared(createSegment(0, 0, 2, 1)));
		assertEpsilonEquals(52, this.shape.getDistanceSquared(createSegment(0, 0, 1, 2)));
		assertEpsilonEquals(1, this.shape.getDistanceSquared(createSegment(1, 10, 4, 14)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(1, 10, 6, 14)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(1, 10, 18, 14)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(7, 10, 18, 14)));
		assertEpsilonEquals(49, this.shape.getDistanceSquared(createSegment(17, 10, 18, 14)));
		assertEpsilonEquals(4, this.shape.getDistanceSquared(createSegment(6, 0, 9, 6)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(6, 0, 9, 16)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(6, 0, 9, 21)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(6, 10, 9, 21)));
		assertEpsilonEquals(1, this.shape.getDistanceSquared(createSegment(6, 19, 9, 21)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(8, 20, 14, 8)));
	}

	protected MultiShape2afp createTestMultiShape(double x, double y) {
		MultiShape2afp multishape = createMultiShape();
		multishape.add(createCircle(x - 5, y + 4, 1));
		multishape.add(createSegment(x + 4, y + 2, x + 8, y - 1));
		return multishape;
	}

	@Override
	@Test
	public void getClosestPointToMultiShape2afp() {
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createTestMultiShape(-10, 7)));
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createTestMultiShape(0, -4)));
		assertClosestPointInBothShapes(this.shape, createTestMultiShape(4, 6));
		assertFpPointEquals(5, 10, this.shape.getClosestPointTo(createTestMultiShape(8, 6)));
		assertClosestPointInBothShapes(this.shape, createTestMultiShape(9, 6));
	}

	@Override
	@Test
	public void getDistanceSquaredMultiShape2afp() {
		assertEpsilonEquals(53, this.shape.getDistanceSquared(createTestMultiShape(-10, 7)));
		assertEpsilonEquals(101, this.shape.getDistanceSquared(createTestMultiShape(0, -4)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(4, 6)));
		assertEpsilonEquals(1, this.shape.getDistanceSquared(createTestMultiShape(8, 6)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(9, 6)));
	}
	
	protected Triangle2afp createTestTriangle(double dx,  double dy) {
		return createTriangle(dx, dy, dx + 3, dy + 3, dx - 1, dy + 1);
	}

	@Override
	@Test
	public void getClosestPointToTriangle2afp() {
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createTestTriangle(0, 4)));
		assertFpPointEquals(10, 8, this.shape.getClosestPointTo(createTestTriangle(10, 6)));
		assertClosestPointInBothShapes(this.shape, createTestTriangle(4, 16));
		assertFpPointEquals(6, 18, this.shape.getClosestPointTo(createTestTriangle(6, 19)));
		assertClosestPointInBothShapes(this.shape, createTestTriangle(10.5, 17.1));
	}

	@Override
	@Test
	public void getDistanceSquaredTriangle2afp() {
		assertEpsilonEquals(5, this.shape.getDistanceSquared(createTestTriangle(0, 4)));
		assertEpsilonEquals(.2, this.shape.getDistanceSquared(createTestTriangle(10, 6)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(4, 16)));
		assertEpsilonEquals(1, this.shape.getDistanceSquared(createTestTriangle(6, 19)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(10.5, 17.1)));
	}

	protected Path2afp createSimpleTestPath(double dx, double dy, boolean close) {
		Path2afp path = createPath();
		path.moveTo(dx + 8, dy + 4);
		path.lineTo(dx - 2, dy + 10);
		path.lineTo(dx + 6, dy + 26);
		path.lineTo(dx + 18, dy + 18);
		if (close) {
			path.closePath();
		}
		return path;
	}

	protected Path2afp createComplexTestPath(double dx, double dy, boolean close, PathWindingRule rule) {
		Path2afp path = createPath(rule);
		path.moveTo(dx, dy);
		path.lineTo(dx - 12, dy + 8);
		path.lineTo(dx - 8, dy + 18);
		path.lineTo(dx + 4, dy + 18);
		path.lineTo(dx - 2, dy);
		path.lineTo(dx - 16, dy + 8);
		path.lineTo(dx - 8, dy + 24);
		path.lineTo(dx + 6, dy + 20);
		if (close) {
			path.closePath();
		}
		return path;
	}
	@Override
	@Test
	public void getClosestPointToPath2afp() {
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createSimpleTestPath(0, 0, false)));
		assertClosestPointInBothShapes(this.shape, createSimpleTestPath(0, 0, true));
		assertClosestPointInBothShapes(this.shape, createSimpleTestPath(0, 10, false));
		assertClosestPointInBothShapes(this.shape, createSimpleTestPath(0, 10, true));
		assertFpPointEquals(10, 12, this.shape.getClosestPointTo(createSimpleTestPath(14, 2, false)));
		assertFpPointEquals(10, 12, this.shape.getClosestPointTo(createSimpleTestPath(14, 2, true)));
		//
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createComplexTestPath(12, 2, false, PathWindingRule.EVEN_ODD)));
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createComplexTestPath(12, 2, true, PathWindingRule.EVEN_ODD)));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createComplexTestPath(3, 8, false, PathWindingRule.EVEN_ODD)));
		assertClosestPointInBothShapes(this.shape, createComplexTestPath(3, 8, true, PathWindingRule.EVEN_ODD));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createComplexTestPath(-2, 8, false, PathWindingRule.EVEN_ODD)));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createComplexTestPath(-2, 8, true, PathWindingRule.EVEN_ODD)));
		//
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createComplexTestPath(12, 2, false, PathWindingRule.NON_ZERO)));
		assertClosestPointInBothShapes(this.shape, createComplexTestPath(12, 2, true, PathWindingRule.NON_ZERO));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createComplexTestPath(3, 8, false, PathWindingRule.NON_ZERO)));
		assertClosestPointInBothShapes(this.shape, createComplexTestPath(3, 8, true, PathWindingRule.NON_ZERO));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createComplexTestPath(-2, 8, false, PathWindingRule.NON_ZERO)));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createComplexTestPath(-2, 8, true, PathWindingRule.NON_ZERO)));
	}

	@Override
	@Test
	public void getDistanceSquaredPath2afp() {
		assertEpsilonEquals(3.55882, this.shape.getDistanceSquared(createSimpleTestPath(0, 0, false)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSimpleTestPath(0, 0, true)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSimpleTestPath(0, 10, false)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSimpleTestPath(0, 10, true)));
		assertEpsilonEquals(4, this.shape.getDistanceSquared(createSimpleTestPath(14, 2, false)));
		assertEpsilonEquals(4, this.shape.getDistanceSquared(createSimpleTestPath(14, 2, true)));
		//
		assertEpsilonEquals(1.23077, this.shape.getDistanceSquared(createComplexTestPath(12, 2, false, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(1.23077, this.shape.getDistanceSquared(createComplexTestPath(12, 2, true, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(.4, this.shape.getDistanceSquared(createComplexTestPath(3, 8, false, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createComplexTestPath(3, 8, true, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(28.9, this.shape.getDistanceSquared(createComplexTestPath(-2, 8, false, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(14.6789, this.shape.getDistanceSquared(createComplexTestPath(-2, 8, true, PathWindingRule.EVEN_ODD)));
		//
		assertEpsilonEquals(1.23077, this.shape.getDistanceSquared(createComplexTestPath(12, 2, false, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createComplexTestPath(12, 2, true, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(.4, this.shape.getDistanceSquared(createComplexTestPath(3, 8, false, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createComplexTestPath(3, 8, true, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(28.9, this.shape.getDistanceSquared(createComplexTestPath(-2, 8, false, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(14.6789, this.shape.getDistanceSquared(createComplexTestPath(-2, 8, true, PathWindingRule.NON_ZERO)));
	}

	@Override
	@Test
	public void getClosestPointToEllipse2afp() {
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createEllipse(0, 0, 2, 1)));
		assertFpPointEquals(5, 12.5, this.shape.getClosestPointTo(createEllipse(2, 12, 2, 1)));
		assertClosestPointInBothShapes(this.shape, createEllipse(6, 16, 2, 1));
		assertFpPointEquals(10, 8, this.shape.getClosestPointTo(createEllipse(9.897519745562938, 7.003543789189412, 2, 1)));
		assertClosestPointInBothShapes(this.shape, createEllipse(9.5, 9.5, 2, 1));
	}

	@Override
	@Test
	public void getDistanceSquaredEllipse2afp() {
		assertEpsilonEquals(61.90769, this.shape.getDistanceSquared(createEllipse(0, 0, 2, 1)));
		assertEpsilonEquals(1, this.shape.getDistanceSquared(createEllipse(2, 12, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(6, 16, 2, 1)));
		assertEpsilonEquals(0.047502, this.shape.getDistanceSquared(createEllipse(9.897519745562938, 7.003543789189412, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(9.5, 9.5, 2, 1)));
	}

	@Override
	@Test
	public void getClosestPointToRoundRectangle2afp() {
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createRoundRectangle(0, 0, 1, 1, .1, .1)));
		assertFpPointEquals(5, 12.5, this.shape.getClosestPointTo(createRoundRectangle(0, 12, 1, 1, .1, .1)));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createRoundRectangle(0, 21, 1, 1, .1, .1)));
		assertFpPointEquals(7.5, 8, this.shape.getClosestPointTo(createRoundRectangle(7, 0, 1, 1, .1, .1)));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(8, 12, 1, 1, .1, .1));
		assertFpPointEquals(9.5, 18, this.shape.getClosestPointTo(createRoundRectangle(9, 21, 1, 1, .1, .1)));
		assertFpPointEquals(10, 8, this.shape.getClosestPointTo(createRoundRectangle(15, 0, 1, 1, .1, .1)));
		assertFpPointEquals(10, 12.5, this.shape.getClosestPointTo(createRoundRectangle(16, 12, 1, 1, .1, .1)));
		assertFpPointEquals(10, 18, this.shape.getClosestPointTo(createRoundRectangle(17, 21, 1, 1, .1, .1)));
		assertFpPointEquals(6, 12.5, this.shape.getClosestPointTo(createRoundRectangle(1, 12, 10, 1, .1, .1)));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(1, 12, 10, 20, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(8, 0, 1, 20, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(8, 0, 10, 20, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(6, 10, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.1, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.2, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.3, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.4, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.5, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.6, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.7, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.8, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.9, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(5, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(5.1, 12, 1, 1, .1, .1));
	}

	@Override
	@Test
	public void getDistanceSquaredRoundRectangle2afp() {
		assertEpsilonEquals(65.59024, this.shape.getDistanceSquared(createRoundRectangle(0, 0, 1, 1, .1, .1)));
		assertEpsilonEquals(16, this.shape.getDistanceSquared(createRoundRectangle(0, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(25.40199, this.shape.getDistanceSquared(createRoundRectangle(0, 21, 1, 1, .1, .1)));
		assertEpsilonEquals(49, this.shape.getDistanceSquared(createRoundRectangle(7, 0, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(8, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(9, this.shape.getDistanceSquared(createRoundRectangle(9, 21, 1, 1, .1, .1)));
		assertEpsilonEquals(74.68163, this.shape.getDistanceSquared(createRoundRectangle(15, 0, 1, 1, .1, .1)));
		assertEpsilonEquals(36, this.shape.getDistanceSquared(createRoundRectangle(16, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(58.48055, this.shape.getDistanceSquared(createRoundRectangle(17, 21, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(1, 12, 10, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(1, 12, 10, 20, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(8, 0, 1, 20, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(8, 0, 10, 20, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(6, 10, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.1, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.2, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.3, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.4, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.5, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.6, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.7, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.8, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.9, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(5, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(5.1, 12, 1, 1, .1, .1)));
	}

	protected Parallelogram2afp createTestParallelogram(double cx, double cy) {
		Vector2D u = createVector(5, 1).toUnitVector();
		Vector2D v = createVector(4, -6).toUnitVector();
		return createParallelogram(cx, cy, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1);
	}
	
	private Point2D<?, ?> runFindsClosestPointRectangleParallelogram(double cx, double cy) {
		Parallelogram2afp p = createTestParallelogram(cx, cy);
		Point2D<?, ?> result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleParallelogram(5, 8, 10, 18,
				p.getCenterX(), p.getCenterY(), p.getFirstAxisX(), p.getFirstAxisY(), p.getFirstAxisExtent(),
				p.getSecondAxisX(), p.getSecondAxisY(), p.getSecondAxisExtent(), result);
		return result;
	}

	@Test
	public void staticFindsClosestPointRectangleParallelogram() {
		assertFpPointEquals(5, 8, runFindsClosestPointRectangleParallelogram(0, 0));
		assertFpPointEquals(5, 9.56018, runFindsClosestPointRectangleParallelogram(2, 10));
		assertFpPointEquals(10, 18, runFindsClosestPointRectangleParallelogram(14, 18));
		assertFpPointEquals(9.48414, 14.43982, runFindsClosestPointRectangleParallelogram(12, 14));
		assertFpPointEquals(10, 8, runFindsClosestPointRectangleParallelogram(14, 2));
		// In multishape.ggb
		Point2D<?, ?> result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleParallelogram(
				5, 8, 7, 9,
				9, 5,
				-0.624695047554424, 0.780868809443031, 2,
				0.894427190999917, -0.447213595499958, 1,
				result);
		assertFpPointEquals(6.85618, 8, result);
	}

	@Override
	@Test
	public void getClosestPointToParallelogram2afp() {
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createTestParallelogram(0, 0)));
		assertFpPointEquals(5, 9.56018, this.shape.getClosestPointTo(createTestParallelogram(2, 10)));
		assertFpPointEquals(10, 18, this.shape.getClosestPointTo(createTestParallelogram(14, 18)));
		assertClosestPointInBothShapes(this.shape, createTestParallelogram(12, 14));
		assertFpPointEquals(10, 8, this.shape.getClosestPointTo(createTestParallelogram(14, 2)));
	}

	@Override
	@Test
	public void getDistanceSquaredParallelogram2afp() {
		assertEpsilonEquals(58.82387, this.shape.getDistanceSquared(createTestParallelogram(0, 0)));
		assertEpsilonEquals(0.23439, this.shape.getDistanceSquared(createTestParallelogram(2, 10)));
		assertEpsilonEquals(2.39611, this.shape.getDistanceSquared(createTestParallelogram(14, 18)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestParallelogram(12, 14)));
		assertEpsilonEquals(33.11829, this.shape.getDistanceSquared(createTestParallelogram(14, 2)));
	}

	protected OrientedRectangle2afp createTestOrientedRectangle(double cx, double cy) {
		Vector2D u = createVector(5, 1).toUnitVector();
		return createOrientedRectangle(cx, cy, u.getX(), u.getY(), 2, 1);
	}

	@Override
	@Test
	public void getClosestPointToOrientedRectangle2afp() {
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createTestOrientedRectangle(0, 0)));
		assertFpPointEquals(5, 9.41165, this.shape.getClosestPointTo(createTestOrientedRectangle(2, 10)));
		assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(12, 8));
		assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(8, 12));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createTestOrientedRectangle(6, 20)));
	}

	@Override
	@Test
	public void getDistanceSquaredOrientedRectangle2afp() {
		assertEpsilonEquals(54.38454, this.shape.getDistanceSquared(createTestOrientedRectangle(0, 0)));
		assertEpsilonEquals(0.71018, this.shape.getDistanceSquared(createTestOrientedRectangle(2, 10)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(12, 8)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(8, 12)));
		assertEpsilonEquals(0.58529, this.shape.getDistanceSquared(createTestOrientedRectangle(6, 20)));
	}

}