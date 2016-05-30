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
package org.arakhne.afc.math.geometry.d3.ai;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.MultiShape2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractMultiShape3aiTest<T extends MultiShape3ai<?, T, C, ?, ?, ?, B>,
		C extends Shape3ai<?, ?, ?, ?, ?, B>,
		B extends RectangularPrism3ai<?, ?, ?, ?, ?, B>> extends AbstractShape3aiTest<T, B> {

	private C firstObject;
	private C secondObject;
	
	@Override
	protected final T createShape() {
		T shape = (T) createMultiShape();
		firstObject = (C) createRectangle(5, 8, 2, 1);
		secondObject = (C) createCircle(-5, 18, 2);
		shape.add(firstObject);
		shape.add(secondObject);
		return shape;
	}

	@Override
	public void testClone() {
		MultiShape2ai clone = this.shape.clone();
		PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 9);
		assertElement(pi, PathElementType.LINE_TO, 5, 9);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19, -3, 20, -5, 20);
		assertElement(pi, PathElementType.CURVE_TO, -6, 20, -7, 19, -7, 18);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16, -6, 16, -5, 16);
		assertElement(pi, PathElementType.CURVE_TO, -3, 16, -3, 16, -3, 18);
		assertElement(pi, PathElementType.CLOSE, -3, 18);
		assertNoElement(pi);
	}

	@Override
	public void equalsObject() {
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createMultiShape()));
		assertFalse(this.shape.equals(createSegment(5, 8, 5, 10)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(this.shape.clone()));
	}

	@Override
	public void equalsObject_withPathIterator() {
		assertFalse(this.shape.equals(createMultiShape().getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 5, 10).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(this.shape.clone().getPathIterator()));
	}

	@Override
	public void equalsToPathIterator() {
		assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
		assertFalse(this.shape.equalsToPathIterator(createMultiShape().getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 5, 10).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.clone().getPathIterator()));
	}

	@Override
	public void equalsToShape() {
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createMultiShape()));
		assertTrue(this.shape.equalsToShape((T) this.shape));
		assertTrue(this.shape.equalsToShape((T) this.shape.clone()));
	}

	@Override
	public void isEmpty() {
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@Override
	public void clear() {
		this.shape.clear();
		PathIterator2ai pi = this.shape.getPathIterator();
		assertNoElement(pi);
	}

	@Override
	public void containsIntInt() {
		// Outside bounding box
		assertFalse(this.shape.contains(-10, 2));
		assertFalse(this.shape.contains(-10, 14));
		assertFalse(this.shape.contains(-10, 25));
		assertFalse(this.shape.contains(-1, 25));
		assertFalse(this.shape.contains(1, 2));
		assertFalse(this.shape.contains(12, 2));
		assertFalse(this.shape.contains(12, 14));
		assertFalse(this.shape.contains(12, 25));
		// Inside bounding box - outside subshape
		assertFalse(this.shape.contains(-6, 8));
		assertFalse(this.shape.contains(4, 17));
		// Inside circle
		assertTrue(this.shape.contains(-4, 19));
		// Inside rectangle
		assertTrue(this.shape.contains(6, 8));
	}

	@Override
	public void containsPoint2D() {
		// Outside bounding box
		assertFalse(this.shape.contains(createPoint(-10, 2)));
		assertFalse(this.shape.contains(createPoint(-10, 14)));
		assertFalse(this.shape.contains(createPoint(-10, 25)));
		assertFalse(this.shape.contains(createPoint(-1, 25)));
		assertFalse(this.shape.contains(createPoint(1, 2)));
		assertFalse(this.shape.contains(createPoint(12, 2)));
		assertFalse(this.shape.contains(createPoint(12, 14)));
		assertFalse(this.shape.contains(createPoint(12, 25)));
		// Inside bounding box - outside subshape
		assertFalse(this.shape.contains(createPoint(-6, 8)));
		assertFalse(this.shape.contains(createPoint(4, 17)));
		// Inside circle
		assertTrue(this.shape.contains(createPoint(-4, 19)));
		// Inside rectangle
		assertTrue(this.shape.contains(createPoint(6, 8)));
	}

	@Override
	public void containsRectangle2ai() {
		// Outside
		assertFalse(this.shape.contains(createRectangle(-20, 14, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(-2,-10, 1, 1)));
		// Intersecting
		assertFalse(this.shape.contains(createRectangle(-6,16, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(4, 8, 1, 1)));
		// Inside
		assertTrue(this.shape.contains(createRectangle(5, 8, 1, 1)));
	}

	@Override
	public void getClosestPointTo() {
		Point2D result;
		// Outside bounding box
		result = this.shape.getClosestPointTo(createPoint(-10, 2));
		assertEpsilonEquals(-6, result.ix());
		assertEpsilonEquals(16, result.iy());
		result = this.shape.getClosestPointTo(createPoint(-10, 14));
		assertEpsilonEquals(-6, result.ix());
		assertEpsilonEquals(16, result.iy());
		result = this.shape.getClosestPointTo(createPoint(-10, 25));
		assertEpsilonEquals(-6, result.ix());
		assertEpsilonEquals(20, result.iy());
		result = this.shape.getClosestPointTo(createPoint(-1, 25));
		assertEpsilonEquals(-4, result.ix());
		assertEpsilonEquals(20, result.iy());
		result = this.shape.getClosestPointTo(createPoint(1, 2));
		assertEpsilonEquals(5, result.ix());
		assertEpsilonEquals(8, result.iy());
		result = this.shape.getClosestPointTo(createPoint(12, 2));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(8, result.iy());
		result = this.shape.getClosestPointTo(createPoint(12, 14));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(9, result.iy());
		result = this.shape.getClosestPointTo(createPoint(12, 25));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(9, result.iy());
		// Inside bounding box - outside subshape
		result = this.shape.getClosestPointTo(createPoint(-6, 8));
		assertEpsilonEquals(-6, result.ix());
		assertEpsilonEquals(16, result.iy());
		result = this.shape.getClosestPointTo(createPoint(4, 17));
		assertEpsilonEquals(-3, result.ix());
		assertEpsilonEquals(17, result.iy());
		// Inside circle
		result = this.shape.getClosestPointTo(createPoint(-4, 19));
		assertEpsilonEquals(-4, result.ix());
		assertEpsilonEquals(19, result.iy());
		// Inside rectangle
		result = this.shape.getClosestPointTo(createPoint(6, 8));
		assertEpsilonEquals(6, result.ix());
		assertEpsilonEquals(8, result.iy());
	}

	@Override
	public void getFarthestPointTo() {
		Point2D result;
		// Outside bounding box
		result = this.shape.getFarthestPointTo(createPoint(-10, 2));
		assertEpsilonEquals(-4, result.ix());
		assertEpsilonEquals(20, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(-10, 14));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(8, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(-10, 25));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(8, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(-1, 25));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(8, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(1, 2));
		assertEpsilonEquals(-6, result.ix());
		assertEpsilonEquals(20, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(12, 2));
		assertEpsilonEquals(-7, result.ix());
		assertEpsilonEquals(19, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(12, 14));
		assertEpsilonEquals(-7, result.ix());
		assertEpsilonEquals(19, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(12, 25));
		assertEpsilonEquals(-7, result.ix());
		assertEpsilonEquals(17, result.iy());
		// Inside bounding box - outside subshape
		result = this.shape.getFarthestPointTo(createPoint(-6, 8));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(9, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(4, 17));
		assertEpsilonEquals(-7, result.ix());
		assertEpsilonEquals(19, result.iy());
		// Inside circle
		result = this.shape.getFarthestPointTo(createPoint(-4, 19));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(8, result.iy());
		// Inside rectangle
		result = this.shape.getFarthestPointTo(createPoint(6, 8));
		assertEpsilonEquals(-7, result.ix());
		assertEpsilonEquals(19, result.iy());
	}

	@Override
	public void getDistance() {
		// Outside bounding box
		assertEpsilonEquals(14.5602, this.shape.getDistance(createPoint(-10, 2)));
		assertEpsilonEquals(4.4721, this.shape.getDistance(createPoint(-10, 14)));
		assertEpsilonEquals(6.4031, this.shape.getDistance(createPoint(-10, 25)));
		assertEpsilonEquals(5.831, this.shape.getDistance(createPoint(-1, 25)));
		assertEpsilonEquals(7.2111, this.shape.getDistance(createPoint(1, 2)));
		assertEpsilonEquals(7.8102, this.shape.getDistance(createPoint(12, 2)));
		assertEpsilonEquals(7.0711, this.shape.getDistance(createPoint(12, 14)));
		assertEpsilonEquals(16.7631, this.shape.getDistance(createPoint(12, 25)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(8, this.shape.getDistance(createPoint(-6, 8)));
		assertEpsilonEquals(7, this.shape.getDistance(createPoint(4, 17)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(-4, 19)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(6, 8)));
	}

	@Override
	public void getDistanceSquared() {
		// Outside bounding box
		assertEpsilonEquals(212, this.shape.getDistanceSquared(createPoint(-10, 2)));
		assertEpsilonEquals(20, this.shape.getDistanceSquared(createPoint(-10, 14)));
		assertEpsilonEquals(41, this.shape.getDistanceSquared(createPoint(-10, 25)));
		assertEpsilonEquals(34, this.shape.getDistanceSquared(createPoint(-1, 25)));
		assertEpsilonEquals(52, this.shape.getDistanceSquared(createPoint(1, 2)));
		assertEpsilonEquals(61, this.shape.getDistanceSquared(createPoint(12, 2)));
		assertEpsilonEquals(50, this.shape.getDistanceSquared(createPoint(12, 14)));
		assertEpsilonEquals(281, this.shape.getDistanceSquared(createPoint(12, 25)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(64, this.shape.getDistanceSquared(createPoint(-6, 8)));
		assertEpsilonEquals(49, this.shape.getDistanceSquared(createPoint(4, 17)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(-4, 19)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(6, 8)));
	}

	@Override
	public void getDistanceL1() {
		// Outside bounding box
		assertEpsilonEquals(18, this.shape.getDistanceL1(createPoint(-10, 2)));
		assertEpsilonEquals(6, this.shape.getDistanceL1(createPoint(-10, 14)));
		assertEpsilonEquals(9, this.shape.getDistanceL1(createPoint(-10, 25)));
		assertEpsilonEquals(8, this.shape.getDistanceL1(createPoint(-1, 25)));
		assertEpsilonEquals(10, this.shape.getDistanceL1(createPoint(1, 2)));
		assertEpsilonEquals(11, this.shape.getDistanceL1(createPoint(12, 2)));
		assertEpsilonEquals(10, this.shape.getDistanceL1(createPoint(12, 14)));
		assertEpsilonEquals(21, this.shape.getDistanceL1(createPoint(12, 25)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(8, this.shape.getDistanceL1(createPoint(-6, 8)));
		assertEpsilonEquals(7, this.shape.getDistanceL1(createPoint(4, 17)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(-4, 19)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(6, 8)));
	}

	@Override
	public void getDistanceLinf() {
		// Outside bounding box
		assertEpsilonEquals(14, this.shape.getDistanceLinf(createPoint(-10, 2)));
		assertEpsilonEquals(4, this.shape.getDistanceLinf(createPoint(-10, 14)));
		assertEpsilonEquals(5, this.shape.getDistanceLinf(createPoint(-10, 25)));
		assertEpsilonEquals(5, this.shape.getDistanceLinf(createPoint(-1, 25)));
		assertEpsilonEquals(6, this.shape.getDistanceLinf(createPoint(1, 2)));
		assertEpsilonEquals(6, this.shape.getDistanceLinf(createPoint(12, 2)));
		assertEpsilonEquals(5, this.shape.getDistanceLinf(createPoint(12, 14)));
		assertEpsilonEquals(16, this.shape.getDistanceLinf(createPoint(12, 25)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(8, this.shape.getDistanceLinf(createPoint(-6, 8)));
		assertEpsilonEquals(7, this.shape.getDistanceLinf(createPoint(4, 17)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(-4, 19)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(6, 8)));
	}

	@Override
	public void setIT() {
		this.shape.set((T) createMultiShape());
		PathIterator2ai pi = this.shape.getPathIterator();
		assertNoElement(pi);
		MultiShape2ai newShape = createMultiShape();
		newShape.add(createRectangle(-6, 48, 5, 7));
		this.shape.set((T) newShape);
		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -6, 48);
		assertElement(pi, PathElementType.LINE_TO, -1, 48);
		assertElement(pi, PathElementType.LINE_TO, -1, 55);
		assertElement(pi, PathElementType.LINE_TO, -6, 55);
		assertElement(pi, PathElementType.CLOSE, -6, 48);
		assertNoElement(pi);
	}

	@Override
	public void translateIntInt() {
		this.shape.translate(10, -2);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 7);
		assertElement(pi, PathElementType.LINE_TO, 15, 7);
		assertElement(pi, PathElementType.CLOSE, 15, 6);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17, 6, 18, 5, 18);
		assertElement(pi, PathElementType.CURVE_TO, 3, 18, 3, 17, 3, 16);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14, 3, 14, 5, 14);
		assertElement(pi, PathElementType.CURVE_TO, 6, 14, 7, 14, 7, 16);
		assertElement(pi, PathElementType.CLOSE, 7, 16);
		assertNoElement(pi);
	}

	@Override
	public void translateVector2D() {
		this.shape.translate(createVector(10, -2));
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 7);
		assertElement(pi, PathElementType.LINE_TO, 15, 7);
		assertElement(pi, PathElementType.CLOSE, 15, 6);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17, 6, 18, 5, 18);
		assertElement(pi, PathElementType.CURVE_TO, 3, 18, 3, 17, 3, 16);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14, 3, 14, 5, 14);
		assertElement(pi, PathElementType.CURVE_TO, 6, 14, 7, 14, 7, 16);
		assertElement(pi, PathElementType.CLOSE, 7, 16);
		assertNoElement(pi);
	}

	@Override
	public void toBoundingBox() {
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
	}

	@Override
	public void toBoundingBoxB() {
		B box = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
	}

	@Override
	public void getPathIterator() {
		PathIterator2ai pi = (PathIterator2ai) this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 9);
		assertElement(pi, PathElementType.LINE_TO, 5, 9);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19, -3, 20, -5, 20);
		assertElement(pi, PathElementType.CURVE_TO, -6, 20, -7, 19, -7, 18);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16, -6, 16, -5, 16);
		assertElement(pi, PathElementType.CURVE_TO, -3, 16, -3, 16, -3, 18);
		assertElement(pi, PathElementType.CLOSE, -3, 18);
		assertNoElement(pi);
	}

	@Override
	public void getPathIteratorTransform2D() {
		PathIterator2ai pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 9);
		assertElement(pi, PathElementType.LINE_TO, 5, 9);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19, -3, 20, -5, 20);
		assertElement(pi, PathElementType.CURVE_TO, -6, 20, -7, 19, -7, 18);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16, -6, 16, -5, 16);
		assertElement(pi, PathElementType.CURVE_TO, -3, 16, -3, 16, -3, 18);
		assertElement(pi, PathElementType.CLOSE, -3, 18);
		assertNoElement(pi);

		pi = this.shape.getPathIterator(new Transform2D());
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 9);
		assertElement(pi, PathElementType.LINE_TO, 5, 9);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19, -3, 20, -5, 20);
		assertElement(pi, PathElementType.CURVE_TO, -6, 20, -7, 19, -7, 18);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16, -6, 16, -5, 16);
		assertElement(pi, PathElementType.CURVE_TO, -3, 16, -3, 16, -3, 18);
		assertElement(pi, PathElementType.CLOSE, -3, 18);
		assertNoElement(pi);

		Transform2D transform = new Transform2D();
		transform.setTranslation(10, -2);
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 15, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 7);
		assertElement(pi, PathElementType.LINE_TO, 15, 7);
		assertElement(pi, PathElementType.CLOSE, 15, 6);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17, 6, 18, 5, 18);
		assertElement(pi, PathElementType.CURVE_TO, 4, 18, 3, 17, 3, 16);
		assertElement(pi, PathElementType.CURVE_TO, 3, 15, 4, 14, 5, 14);
		assertElement(pi, PathElementType.CURVE_TO, 6, 14, 7, 15, 7, 16);
		assertElement(pi, PathElementType.CLOSE, 7, 16);
		assertNoElement(pi);
	}

	@Override
	public void createTransformedShape() {
		Transform2D transform = new Transform2D();
		transform.setTranslation(10, -2);
		Shape2ai newShape = this.shape.createTransformedShape(transform);
		PathIterator2ai pi = (PathIterator2ai) newShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 7);
		assertElement(pi, PathElementType.LINE_TO, 15, 7);
		assertElement(pi, PathElementType.CLOSE, 15, 6);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17, 6, 18, 5, 18);
		assertElement(pi, PathElementType.CURVE_TO, 4, 18, 3, 17, 3, 16);
		assertElement(pi, PathElementType.CURVE_TO, 3, 15, 4, 14, 5, 14);
		assertElement(pi, PathElementType.CURVE_TO, 6, 14, 7, 15, 7, 16);
		assertElement(pi, PathElementType.CLOSE, 7, 16);
		assertNoElement(pi);
	}

	@Override
	public void intersectsRectangle2ai() {
		// Outside
		assertFalse(this.shape.intersects(createRectangle(-20, 14, 1, 1)));
		assertFalse(this.shape.intersects(createRectangle(-2, -10, 1, 1)));
		// Intersecting
		assertTrue(this.shape.intersects(createRectangle(-6, 16, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(4, 8, 2, 2)));
		// Inside
		assertTrue(this.shape.intersects(createRectangle(-4, 18, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(5, 8, 1, 1)));
	}

	@Override
	public void intersectsCircle2ai() {
		// Outside
		assertFalse(this.shape.intersects(createCircle(-20, 14, 1)));
		assertFalse(this.shape.intersects(createCircle(-2,- 10, 1)));
		// Intersecting
		assertTrue(this.shape.intersects(createCircle(-6, 16, 1)));
		assertTrue(this.shape.intersects(createCircle(4, 8, 1)));
		// Inside
		assertTrue(this.shape.intersects(createCircle(-4, 18, 1)));
		assertTrue(this.shape.intersects(createCircle(5, 8, 1)));
	}

	@Override
	public void intersectsSegment2ai() {
		// Outside
		assertFalse(this.shape.intersects(createSegment(-20, 14, -19, 14)));
		assertFalse(this.shape.intersects(createSegment(-2, -10, -1, -10)));
		// Intersecting
		assertTrue(this.shape.intersects(createSegment(-6, 16, -5, 16)));
		assertTrue(this.shape.intersects(createSegment(4, 8, 5, 8)));
		// Inside
		assertTrue(this.shape.intersects(createSegment(-4, 18, -3, 18)));
		assertTrue(this.shape.intersects(createSegment(5, 8, 6, 8)));
	}

	@Override
	public void intersectsPath2ai() {
		Path2ai path = createPath();
		path.moveTo(-6, 2);
		path.lineTo(10, 6);
		path.lineTo(8, 14);
		path.lineTo(-4, 12);
		path.lineTo(-12, 22);
		path.lineTo(6, 20);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));
		
		path = createPath();
		path.moveTo(-6, 2);
		path.lineTo(10, 6);
		path.lineTo(8, 14);
		path.lineTo(-4, 12);
		path.lineTo(6, 20);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath();
		path.moveTo(-6, 2);
		path.lineTo(8, 14);
		path.lineTo(-4, 12);
		path.lineTo(-12, 22);
		path.lineTo(6, 20);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));
	}

	@Override
	public void intersectsPathIterator2ai() {
		Path2ai path = createPath();
		path.moveTo(-4, 3);
		path.lineTo(9, 6);
		path.lineTo(8, 14);
		path.lineTo(-9, 11);
		path.lineTo(-8, 21);
		path.lineTo(4, 21);
		assertFalse(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
		
		path = createPath();
		path.moveTo(-6, 2);
		path.lineTo(10, 6);
		path.lineTo(8, 14);
		path.lineTo(-4, 12);
		path.lineTo(6, 20);
		assertFalse(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator2ai) path.getPathIterator()));

		path = createPath();
		path.moveTo(-6, 2);
		path.lineTo(8, 14);
		path.lineTo(-4, 12);
		path.lineTo(-12, 22);
		path.lineTo(6, 20);
		assertFalse(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
	}

	@Override
	public void operator_addVector2D() {
		this.shape.operator_add(createVector(10, -2));
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 7);
		assertElement(pi, PathElementType.LINE_TO, 15, 7);
		assertElement(pi, PathElementType.CLOSE, 15, 6);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17, 6, 18, 5, 18);
		assertElement(pi, PathElementType.CURVE_TO, 3, 18, 3, 17, 3, 16);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14, 3, 14, 5, 14);
		assertElement(pi, PathElementType.CURVE_TO, 6, 14, 7, 14, 7, 16);
		assertElement(pi, PathElementType.CLOSE, 7, 16);
		assertNoElement(pi);
	}

	@Override
	public void operator_plusVector2D() {
		T shape = this.shape.operator_plus(createVector(10, -2));
		PathIterator2ai pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 7);
		assertElement(pi, PathElementType.LINE_TO, 15, 7);
		assertElement(pi, PathElementType.CLOSE, 15, 6);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17, 6, 18, 5, 18);
		assertElement(pi, PathElementType.CURVE_TO, 3, 18, 3, 17, 3, 16);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14, 3, 14, 5, 14);
		assertElement(pi, PathElementType.CURVE_TO, 6, 14, 7, 14, 7, 16);
		assertElement(pi, PathElementType.CLOSE, 7, 16);
		assertNoElement(pi);
	}

	@Override
	public void operator_removeVector2D() {
		this.shape.operator_remove(createVector(10, -2));
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -5, 10);
		assertElement(pi, PathElementType.LINE_TO, -3, 10);
		assertElement(pi, PathElementType.LINE_TO, -3, 11);
		assertElement(pi, PathElementType.LINE_TO, -5, 11);
		assertElement(pi, PathElementType.CLOSE, -5, 10);
		assertElement(pi, PathElementType.MOVE_TO, -13, 20);
		assertElement(pi, PathElementType.CURVE_TO, -13, 21, -13, 22, -15, 22);
		assertElement(pi, PathElementType.CURVE_TO, -16, 22, -17, 21, -17, 20);
		assertElement(pi, PathElementType.CURVE_TO, -17, 18, -16, 18, -15, 18);
		assertElement(pi, PathElementType.CURVE_TO, -13, 18, -13, 18, -13, 20);
		assertElement(pi, PathElementType.CLOSE, -13, 20);
		assertNoElement(pi);
	}

	@Override
	public void operator_minusVector2D() {
		T shape = this.shape.operator_minus(createVector(10, -2));
		PathIterator2ai pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -5, 10);
		assertElement(pi, PathElementType.LINE_TO, -3, 10);
		assertElement(pi, PathElementType.LINE_TO, -3, 11);
		assertElement(pi, PathElementType.LINE_TO, -5, 11);
		assertElement(pi, PathElementType.CLOSE, -5, 10);
		assertElement(pi, PathElementType.MOVE_TO, -13, 20);
		assertElement(pi, PathElementType.CURVE_TO, -13, 21, -13, 22, -15, 22);
		assertElement(pi, PathElementType.CURVE_TO, -16, 22, -17, 21, -17, 20);
		assertElement(pi, PathElementType.CURVE_TO, -17, 18, -16, 18, -15, 18);
		assertElement(pi, PathElementType.CURVE_TO, -13, 18, -13, 18, -13, 20);
		assertElement(pi, PathElementType.CLOSE, -13, 20);
		assertNoElement(pi);
	}

	@Override
	public void operator_multiplyTransform2D() {
		Transform2D transform = new Transform2D();
		transform.setTranslation(10, -2);
		Shape2ai newShape = this.shape.operator_multiply(transform);
		PathIterator2ai pi = (PathIterator2ai) newShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 7);
		assertElement(pi, PathElementType.LINE_TO, 15, 7);
		assertElement(pi, PathElementType.CLOSE, 15, 6);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17, 6, 18, 5, 18);
		assertElement(pi, PathElementType.CURVE_TO, 4, 18, 3, 17, 3, 16);
		assertElement(pi, PathElementType.CURVE_TO, 3, 15, 4, 14, 5, 14);
		assertElement(pi, PathElementType.CURVE_TO, 6, 14, 7, 15, 7, 16);
		assertElement(pi, PathElementType.CLOSE, 7, 16);
		assertNoElement(pi);
	}

	@Override
	public void operator_andPoint2D() {
		assertFalse(this.shape.operator_and(createPoint(-10, 2)));
		assertFalse(this.shape.operator_and(createPoint(-10, 14)));
		assertFalse(this.shape.operator_and(createPoint(-10, 25)));
		assertFalse(this.shape.operator_and(createPoint(-1, 25)));
		assertFalse(this.shape.operator_and(createPoint(1, 2)));
		assertFalse(this.shape.operator_and(createPoint(12, 2)));
		assertFalse(this.shape.operator_and(createPoint(12, 14)));
		assertFalse(this.shape.operator_and(createPoint(12, 25)));
		assertFalse(this.shape.operator_and(createPoint(-6, 8)));
		assertFalse(this.shape.operator_and(createPoint(4, 17)));
		assertTrue(this.shape.operator_and(createPoint(-4, 19)));
		assertTrue(this.shape.operator_and(createPoint(6, 8)));
	}

	@Override
	public void operator_andShape2D() {
		assertTrue(this.shape.operator_and(createCircle(-6, 16, 1)));
		assertTrue(this.shape.operator_and(createRectangle(-6, 16, 1, 1)));

	}

	@Override
	public void operator_upToPoint2D() {
		assertEpsilonEquals(14.5602, this.shape.operator_upTo(createPoint(-10, 2)));
		assertEpsilonEquals(4.4721, this.shape.operator_upTo(createPoint(-10, 14)));
		assertEpsilonEquals(6.4031, this.shape.operator_upTo(createPoint(-10, 25)));
		assertEpsilonEquals(5.831, this.shape.operator_upTo(createPoint(-1, 25)));
		assertEpsilonEquals(7.2111, this.shape.operator_upTo(createPoint(1, 2)));
		assertEpsilonEquals(7.8102, this.shape.operator_upTo(createPoint(12, 2)));
		assertEpsilonEquals(7.0711, this.shape.operator_upTo(createPoint(12, 14)));
		assertEpsilonEquals(16.7631, this.shape.operator_upTo(createPoint(12, 25)));
		assertEpsilonEquals(8, this.shape.operator_upTo(createPoint(-6, 8)));
		assertEpsilonEquals(7, this.shape.operator_upTo(createPoint(4, 17)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(-4, 19)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(6, 8)));
	}
	
	@Override
	public void intersectsShape2D() {
		assertTrue(this.shape.intersects((Shape2D) createCircle(-6, 16, 1)));
		assertTrue(this.shape.intersects((Shape2D) createRectangle(-6, 16, 1, 1)));
	}

	@Test
	public void getPointIterator() {
		Point2D p;
		Iterator<? extends Point2D> it = this.shape.getPointIterator();
		
		// Rectangle points
		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 5, p.ix());
		assertEquals(p.toString(), 8, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 6, p.ix());
		assertEquals(p.toString(), 8, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), 8, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), 9, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 6, p.ix());
		assertEquals(p.toString(), 9, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 5, p.ix());
		assertEquals(p.toString(), 9, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 5, p.ix());
		assertEquals(p.toString(), 8, p.iy());

		// Circle points
		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), -5, p.ix());
		assertEquals(p.toString(), 20, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), -4, p.ix());
		assertEquals(p.toString(), 20, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), -3, p.ix());
		assertEquals(p.toString(), 18, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), -3, p.ix());
		assertEquals(p.toString(), 19, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), -5, p.ix());
		assertEquals(p.toString(), 16, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), -4, p.ix());
		assertEquals(p.toString(), 16, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), -3, p.ix());
		assertEquals(p.toString(), 17, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), -6, p.ix());
		assertEquals(p.toString(), 16, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), -7, p.ix());
		assertEquals(p.toString(), 18, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), -7, p.ix());
		assertEquals(p.toString(), 17, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), -6, p.ix());
		assertEquals(p.toString(), 20, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), -7, p.ix());
		assertEquals(p.toString(), 19, p.iy());

		assertFalse(it.hasNext());
	}

	@Test
	public void getFirstShapeContainingPoint2D() {
		// Outside bounding box
		assertNull(this.shape.getFirstShapeContaining(createPoint(-10, 2)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(-10, 14)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(-10, 25)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(-1, 25)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(1, 2)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(12, 2)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(12, 14)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(12, 25)));
		// Inside bounding box - outside subshape
		assertNull(this.shape.getFirstShapeContaining(createPoint(-6, 8)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(4, 17)));
		// Inside circle
		assertSame(secondObject, this.shape.getFirstShapeContaining(createPoint(-4, 19)));
		// Inside rectangle
		assertSame(firstObject, this.shape.getFirstShapeContaining(createPoint(6, 8)));
	}
	
	@Test
	public void getShapesContainingPoint2D() {
		// Outside bounding box
		assertTrue(this.shape.getShapesContaining(createPoint(-10, 2)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(-10, 14)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(-10, 25)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(-1, 25)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(1, 2)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(12, 2)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(12, 14)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(12, 25)).isEmpty());
		// Inside bounding box - outside subshape
		assertTrue(this.shape.getShapesContaining(createPoint(-6, 8)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(4, 17)).isEmpty());
		// Inside circle
		assertEquals(Arrays.asList(secondObject), this.shape.getShapesContaining(createPoint(-4, 19)));
		// Inside rectangle
		assertEquals(Arrays.asList(firstObject), this.shape.getShapesContaining(createPoint(6, 8)));
	}

	@Test
	public void getFirstShapeIntersectingShape2D() {
		MultiShape2D shape2d = this.shape;
		assertNull(shape2d.getFirstShapeIntersecting(createSegment(-20, 14, -19, 14)));
		assertNull(shape2d.getFirstShapeIntersecting(createSegment(-2, -10, -1, -10)));
		assertSame(secondObject, shape2d.getFirstShapeIntersecting(createSegment(-6, 16, -5, 16)));
		assertSame(firstObject, shape2d.getFirstShapeIntersecting(createSegment(4, 8, 5, 8)));
		assertSame(secondObject, shape2d.getFirstShapeIntersecting(createSegment(-4, 18, -3, 18)));
		assertSame(firstObject, shape2d.getFirstShapeIntersecting(createSegment(5, 8, 6, 8)));
	}

	@Test
	public void getFirstShapeIntersectingShape2D_withOpenPath() {
		MultiShape2D shape2d = this.shape;
		
		Path2ai path = createPath();
		path.moveTo(-4, 3);
		path.lineTo(9, 6);
		path.lineTo(8, 14);
		path.lineTo(-9, 11);
		path.lineTo(-8, 21);
		path.lineTo(4, 21);
		
		assertNull(shape2d.getFirstShapeIntersecting(path));
	}

	@Test
	public void getFirstShapeIntersectingShape2D_withClosePath() {
		MultiShape2D shape2d = this.shape;
		
		Path2ai path = createPath();
		path.moveTo(-4, 3);
		path.lineTo(9, 6);
		path.lineTo(8, 14);
		path.lineTo(-9, 11);
		path.lineTo(-8, 21);
		path.lineTo(4, 21);
		path.closePath();
		
		assertSame(firstObject, shape2d.getFirstShapeIntersecting(path));
	}

	@Test
	public void getShapesIntersectingShape2D() {
		MultiShape2D shape2d = this.shape;
		assertTrue(shape2d.getShapesIntersecting(createSegment(-20, 14, -19, 14)).isEmpty());
		assertTrue(shape2d.getShapesIntersecting(createSegment(-2, -10, -1, -10)).isEmpty());
		assertEquals(Arrays.asList(secondObject), shape2d.getShapesIntersecting(createSegment(-6, 16, -5, 16)));
		assertEquals(Arrays.asList(firstObject), shape2d.getShapesIntersecting(createSegment(4, 8, 5, 8)));
		assertEquals(Arrays.asList(secondObject), shape2d.getShapesIntersecting(createSegment(-4, 18, -3, 18)));
		assertEquals(Arrays.asList(firstObject), shape2d.getShapesIntersecting(createSegment(5, 8, 6, 8)));
	}

	@Test
	public void getBackendDataList() {
		assertNotNull(this.shape.getBackendDataList());
		assertEquals(2, this.shape.getBackendDataList().size());
		assertSame(firstObject, this.shape.getBackendDataList().get(0));
		assertSame(secondObject, this.shape.getBackendDataList().get(1));
	}

	@Test
	public void onGeometryChange_changeFirstObject() {
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		firstObject.translate(12, -7);
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9
		
		// R': 17;  1; 19;  2 
		
		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(1, box.getMinY());
		assertEquals(19, box.getMaxX());
		assertEquals(20, box.getMaxY());
	}
	
	@Test
	public void onGeometryChange_changeSecondObject() {
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		secondObject.translate(12, -7);
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9
		
		// C':  5;  9;  9; 13 

		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(5, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(9, box.getMaxX());
		assertEquals(13, box.getMaxY());
	}

	@Test
	public void onBackendDataListChange_addition() {
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		this.shape.add((C) createCircle(10, 14, 1));
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9
		
		// C':  9;  13;  11; 15 

		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(11, box.getMaxX());
		assertEquals(20, box.getMaxY());
	}

	@Test
	public void onBackendDataListChange_removalFirstObject() {
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		this.shape.remove(firstObject);
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9
		
		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(16, box.getMinY());
		assertEquals(-3, box.getMaxX());
		assertEquals(20, box.getMaxY());
	}

	@Test
	public void onBackendDataListChange_removalSecondObject() {
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		this.shape.remove(secondObject);
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9

		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(5, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(9, box.getMaxY());
	}

	@Test
	public void noGeometryChangeAfterRemoval() {
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		this.shape.remove(secondObject);
		secondObject.translate(1453,  -451);
		
		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(5, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(9, box.getMaxY());
	}

}