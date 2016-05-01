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
package org.arakhne.afc.math.geometry.d2.afp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.junit.Test;

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
		assertTrue(this.shape.equalsToShape((T) this.shape));
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

}