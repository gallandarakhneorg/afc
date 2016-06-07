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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d3.MultiShape3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;

@SuppressWarnings("all")
public abstract class AbstractMultiShape3afpTest<T extends MultiShape3afp<?, T, C, ?, ?, ?, B>,
		C extends Shape3afp<?, ?, ?, ?, ?, B>,
		B extends RectangularPrism3afp<?, ?, ?, ?, ?, B>> extends AbstractShape3afpTest<T, B> {

	protected C firstObject;
	
	protected C secondObject;

	@Override
	protected final T createShape() {
		T shape = (T) createMultiShape();
		firstObject = (C) createRectangularPrism(5, 8, 0, 2, 1, 0);
		secondObject = (C) createSphere(-5, 18, 0, 2);
		shape.add(firstObject);
		shape.add(secondObject);
		return shape;
	}

	@Override
	public void testClone() {
		MultiShape3afp clone = this.shape.clone();
		PathIterator3afp pi = (PathIterator3afp) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 9, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 9, 0);
		assertElement(pi, PathElementType.CLOSE, 5, 8, 0);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19.10457, 0, -3.89543, 20, 0, -5, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -6.10457, 20, 0, -7, 19.10457, 0, -7, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16.89543, 0, -6.10457, 16, 0, -5, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3.89543, 16, 0, -3, 16.89543, 0, -3, 18, 0);
		assertElement(pi, PathElementType.CLOSE, -3, 18, 0);
		assertNoElement(pi);
	}

	@Override
	public void equalsObject() {
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createMultiShape()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 5, 10, 0)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(this.shape.clone()));
	}

	@Override
	public void equalsObject_withPathIterator() {
		assertFalse(this.shape.equals(createMultiShape().getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 5, 10, 0).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(this.shape.clone().getPathIterator()));
	}

	@Override
	public void equalsToPathIterator() {
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createMultiShape().getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 0, 5, 10, 0).getPathIterator()));
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
		PathIterator3afp pi = this.shape.getPathIterator();
		assertNoElement(pi);
	}

	@Override
	public void containsDoubleDouble() {
		// Outside bounding box
		assertFalse(this.shape.contains(-10, 2, 0));
		assertFalse(this.shape.contains(-10, 14, 0));
		assertFalse(this.shape.contains(-10, 25, 0));
		assertFalse(this.shape.contains(-1, 25, 0));
		assertFalse(this.shape.contains(1, 2, 0));
		assertFalse(this.shape.contains(12, 2, 0));
		assertFalse(this.shape.contains(12, 14, 0));
		assertFalse(this.shape.contains(12, 25, 0));
		// Inside bounding box - outside subshape
		assertFalse(this.shape.contains(-6, 8, 0));
		assertFalse(this.shape.contains(4, 17, 0));
		// Inside circle
		assertTrue(this.shape.contains(-4, 19, 0));
		// Inside rectangle
		assertTrue(this.shape.contains(6, 8.25, 0));
	}

	@Override
	public void containsPoint3D() {
		// Outside bounding box
		assertFalse(this.shape.contains(createPoint(-10, 2, 0)));
		assertFalse(this.shape.contains(createPoint(-10, 14, 0)));
		assertFalse(this.shape.contains(createPoint(-10, 25, 0)));
		assertFalse(this.shape.contains(createPoint(-1, 25, 0)));
		assertFalse(this.shape.contains(createPoint(1, 2, 0)));
		assertFalse(this.shape.contains(createPoint(12, 2, 0)));
		assertFalse(this.shape.contains(createPoint(12, 14, 0)));
		assertFalse(this.shape.contains(createPoint(12, 25, 0)));
		// Inside bounding box - outside subshape
		assertFalse(this.shape.contains(createPoint(-6, 8, 0)));
		assertFalse(this.shape.contains(createPoint(4, 17, 0)));
		// Inside circle
		assertTrue(this.shape.contains(createPoint(-4, 19, 0)));
		// Inside rectangle
		assertTrue(this.shape.contains(createPoint(6, 8.25, 0)));
	}

	@Override
	public void containsRectangularPrism3afp() {
		// Outside
		assertFalse(this.shape.contains(createRectangularPrism(-20, 14, 0, .5, .5, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(-2,-10, 0, .5, .5, 0)));
		// Intersecting
		assertFalse(this.shape.contains(createRectangularPrism(-6, 16, 0, .5, .5, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(4.75, 8, 0, .5, .5, 0)));
		// Inside
		assertTrue(this.shape.contains(createRectangularPrism(-4, 18, 0, .5, .5, 0)));
		assertTrue(this.shape.contains(createRectangularPrism(5.5, 8.5, 0, .5, .5, 0)));
	}

	@Override
	public void getClosestPointTo() {
		Point3D result;
		// Outside bounding box
		result = this.shape.getClosestPointTo(createPoint(-10, 2, 0));
		assertEpsilonEquals(-5.59655, result.getX());
		assertEpsilonEquals(16.09104, result.getY());
		result = this.shape.getClosestPointTo(createPoint(-10, 14, 0));
		assertEpsilonEquals(-6.56174, result.getX());
		assertEpsilonEquals(16.75061, result.getY());
		result = this.shape.getClosestPointTo(createPoint(-10, 25, 0));
		assertEpsilonEquals(-6.16248, result.getX());
		assertEpsilonEquals(19.62747, result.getY());
		result = this.shape.getClosestPointTo(createPoint(-1, 25, 0));
		assertEpsilonEquals(-4.00772, result.getX());
		assertEpsilonEquals(19.73649, result.getY());
		result = this.shape.getClosestPointTo(createPoint(1, 2, 0));
		assertEpsilonEquals(5, result.getX());
		assertEpsilonEquals(8, result.getY());
		result = this.shape.getClosestPointTo(createPoint(12, 2, 0));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(8, result.getY());
		result = this.shape.getClosestPointTo(createPoint(12, 14, 0));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(9, result.getY());
		result = this.shape.getClosestPointTo(createPoint(12, 25, 0));
		assertEpsilonEquals(-3.15064, result.getX());
		assertEpsilonEquals(18.7615, result.getY());
		// Inside bounding box - outside subshape
		result = this.shape.getClosestPointTo(createPoint(-6, 8, 0));
		assertEpsilonEquals(-5.19901, result.getX());
		assertEpsilonEquals(16.00993, result.getY());
		result = this.shape.getClosestPointTo(createPoint(4, 17, 0));
		assertEpsilonEquals(-3.01223, result.getX());
		assertEpsilonEquals(17.77914, result.getY());
		// Inside circle
		result = this.shape.getClosestPointTo(createPoint(-4, 19, 0));
		assertEpsilonEquals(-4, result.getX());
		assertEpsilonEquals(19, result.getY());
		// Inside rectangle
		result = this.shape.getClosestPointTo(createPoint(6, 8.25, 0));
		assertEpsilonEquals(6, result.getX());
		assertEpsilonEquals(8.25, result.getY());
	}

	@Override
	public void getFarthestPointTo() {
		Point3D result;
		// Outside bounding box
		result = this.shape.getFarthestPointTo(createPoint(-10, 2, 0));
		assertEpsilonEquals(-4.40345, result.getX());
		assertEpsilonEquals(19.90896, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(-10, 14, 0));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(8, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(-10, 25, 0));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(8, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(-1, 25, 0));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(8, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(1, 2, 0));
		assertEpsilonEquals(-5.70225, result.getX());
		assertEpsilonEquals(19.87266, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(12, 2, 0));
		assertEpsilonEquals(-6.4564, result.getX());
		assertEpsilonEquals(19.37073, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(12, 14, 0));
		assertEpsilonEquals(-6.94683, result.getX());
		assertEpsilonEquals(18.45808, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(12, 25, 0));
		assertEpsilonEquals(-6.84936, result.getX());
		assertEpsilonEquals(17.2385, result.getY());
		// Inside bounding box - outside subshape
		result = this.shape.getFarthestPointTo(createPoint(-6, 8, 0));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(9, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(4, 17, 0));
		assertEpsilonEquals(-6.98777, result.getX());
		assertEpsilonEquals(18.22086, result.getY());
		// Inside circle
		result = this.shape.getFarthestPointTo(createPoint(-4, 19, 0));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(8, result.getY());
		// Inside rectangle
		result = this.shape.getFarthestPointTo(createPoint(6, 8.25, 0));
		assertEpsilonEquals(-6.49669, result.getX());
		assertEpsilonEquals(19.32662, result.getY());
	}

	@Override
	public void getDistance() {
		// Outside bounding box
		assertEpsilonEquals(14.76305, this.shape.getDistance(createPoint(-10, 2, 0)));
		assertEpsilonEquals(4.40312, this.shape.getDistance(createPoint(-10, 14, 0)));
		assertEpsilonEquals(6.60233, this.shape.getDistance(createPoint(-10, 25, 0)));
		assertEpsilonEquals(6.06226, this.shape.getDistance(createPoint(-1, 25, 0)));
		assertEpsilonEquals(7.21110, this.shape.getDistance(createPoint(1, 2, 0)));
		assertEpsilonEquals(7.81025, this.shape.getDistance(createPoint(12, 2, 0)));
		assertEpsilonEquals(7.07107, this.shape.getDistance(createPoint(12, 14, 0)));
		assertEpsilonEquals(16.38478, this.shape.getDistance(createPoint(12, 25, 0)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(8.04988, this.shape.getDistance(createPoint(-6, 8, 0)));
		assertEpsilonEquals(7.05538, this.shape.getDistance(createPoint(4, 17, 0)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(-4, 19, 0)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(6, 8.25, 0)));
	}

	@Override
	public void getDistanceSquared() {
		// Outside bounding box
		assertEpsilonEquals(217.94778, this.shape.getDistanceSquared(createPoint(-10, 2, 0)));
		assertEpsilonEquals(19.38749, this.shape.getDistanceSquared(createPoint(-10, 14, 0)));
		assertEpsilonEquals(43.5907, this.shape.getDistanceSquared(createPoint(-10, 25, 0)));
		assertEpsilonEquals(36.75092, this.shape.getDistanceSquared(createPoint(-1, 25, 0)));
		assertEpsilonEquals(52, this.shape.getDistanceSquared(createPoint(1, 2, 0)));
		assertEpsilonEquals(61, this.shape.getDistanceSquared(createPoint(12, 2, 0)));
		assertEpsilonEquals(50, this.shape.getDistanceSquared(createPoint(12, 14, 0)));
		assertEpsilonEquals(268.46089, this.shape.getDistanceSquared(createPoint(12, 25, 0)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(64.8005, this.shape.getDistanceSquared(createPoint(-6, 8, 0)));
		assertEpsilonEquals(49.77843, this.shape.getDistanceSquared(createPoint(4, 17, 0)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(-4, 19, 0)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(6, 8.25, 0)));
	}

	@Override
	public void getDistanceL1() {
		// Outside bounding box
		assertEpsilonEquals(18.49449, this.shape.getDistanceL1(createPoint(-10, 2, 0)));
		assertEpsilonEquals(6.18887, this.shape.getDistanceL1(createPoint(-10, 14, 0)));
		assertEpsilonEquals(9.21006, this.shape.getDistanceL1(createPoint(-10, 25, 0)));
		assertEpsilonEquals(8.27123, this.shape.getDistanceL1(createPoint(-1, 25, 0)));
		assertEpsilonEquals(10, this.shape.getDistanceL1(createPoint(1, 2, 0)));
		assertEpsilonEquals(11, this.shape.getDistanceL1(createPoint(12, 2, 0)));
		assertEpsilonEquals(10, this.shape.getDistanceL1(createPoint(12, 14, 0)));
		assertEpsilonEquals(21, this.shape.getDistanceL1(createPoint(12, 25, 0)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(8.81092, this.shape.getDistanceL1(createPoint(-6, 8, 0)));
		assertEpsilonEquals(7.79137, this.shape.getDistanceL1(createPoint(4, 17, 0)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(-4, 19, 0)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(6, 8.25, 0)));
	}

	@Override
	public void getDistanceLinf() {
		// Outside bounding box
		assertEpsilonEquals(14.09104, this.shape.getDistanceLinf(createPoint(-10, 2, 0)));
		assertEpsilonEquals(3.43826, this.shape.getDistanceLinf(createPoint(-10, 14, 0)));
		assertEpsilonEquals(5.37253, this.shape.getDistanceLinf(createPoint(-10, 25, 0)));
		assertEpsilonEquals(5.26351, this.shape.getDistanceLinf(createPoint(-1, 25, 0)));
		assertEpsilonEquals(6, this.shape.getDistanceLinf(createPoint(1, 2, 0)));
		assertEpsilonEquals(6, this.shape.getDistanceLinf(createPoint(12, 2, 0)));
		assertEpsilonEquals(5, this.shape.getDistanceLinf(createPoint(12, 14, 0)));
		assertEpsilonEquals(15.15064, this.shape.getDistanceLinf(createPoint(12, 25, 0)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(8.00993, this.shape.getDistanceLinf(createPoint(-6, 8, 0)));
		assertEpsilonEquals(7.01223, this.shape.getDistanceLinf(createPoint(4, 17, 0)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(-4, 19, 0)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(6, 8.25, 0)));
	}

	@Override
	public void setIT() {
		this.shape.set((T) createMultiShape());
		PathIterator3afp pi = this.shape.getPathIterator();
		assertNoElement(pi);
		MultiShape3afp newShape = createMultiShape();
		newShape.add(createRectangularPrism(-6, 48, 0, 5, 7, 0));
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
	public void translateDoubleDouble() {
		this.shape.translate(10, -2, 0);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 7);
		assertElement(pi, PathElementType.LINE_TO, 15, 7);
		assertElement(pi, PathElementType.CLOSE, 15, 6);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17.10457, 6.10457, 18, 5, 18);
		assertElement(pi, PathElementType.CURVE_TO, 3.89543, 18, 3, 17.10457, 3, 16);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14.89543, 3.89543, 14, 5, 14);
		assertElement(pi, PathElementType.CURVE_TO, 6.10457, 14, 7, 14.89543, 7, 16);
		assertElement(pi, PathElementType.CLOSE, 7, 16);
		assertNoElement(pi);
	}

	@Override
	public void translateVector3D() {
		this.shape.translate(createVector(10, -2, 0));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 6);
		assertElement(pi, PathElementType.LINE_TO, 17, 7);
		assertElement(pi, PathElementType.LINE_TO, 15, 7);
		assertElement(pi, PathElementType.CLOSE, 15, 6);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17.10457, 6.10457, 18, 5, 18);
		assertElement(pi, PathElementType.CURVE_TO, 3.89543, 18, 3, 17.10457, 3, 16);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14.89543, 3.89543, 14, 5, 14);
		assertElement(pi, PathElementType.CURVE_TO, 6.10457, 14, 7, 14.89543, 7, 16);
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
		B box = createRectangularPrism(0, 0, 0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(8, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
		assertEpsilonEquals(20, box.getMaxZ());
	}

	@Override
	public void getPathIterator() {
		PathIterator3afp pi = (PathIterator3afp) this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 9, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 9, 0);
		assertElement(pi, PathElementType.CLOSE, 5, 8, 0);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19.10457, 0, -3.89543, 20, 0, -5, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -6.10457, 20, 0, -7, 19.10457, 0, -7, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16.89543, 0, -6.10457, 16, 0, -5, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3.89543, 16, 0, -3, 16.89543, 0, -3, 18, 0);
		assertElement(pi, PathElementType.CLOSE, -3, 18, 0);
		assertNoElement(pi);
	}

	@Override
	public void getPathIteratorTransform3D() {
		PathIterator3afp pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 5, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 9, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 9, 0);
		assertElement(pi, PathElementType.CLOSE, 5, 8, 0);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19.10457, 0, -3.89543, 20, 0, -5, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -6.10457, 20, 0, -7, 19.10457, 0, -7, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16.89543, 0, -6.10457, 16, 0, -5, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3.89543, 16, 0, -3, 16.89543, 0, -3, 18, 0);
		assertElement(pi, PathElementType.CLOSE, -3, 18, 0);
		assertNoElement(pi);

		pi = this.shape.getPathIterator(new Transform3D());
		assertElement(pi, PathElementType.MOVE_TO, 5, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 9, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 9, 0);
		assertElement(pi, PathElementType.CLOSE, 5, 8, 0);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19.10457, 0, -3.89543, 20, 0, -5, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -6.10457, 20, 0, -7, 19.10457, 0, -7, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16.89543, 0, -6.10457, 16, 0, -5, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3.89543, 16, 0, -3, 16.89543, 0, -3, 18, 0);
		assertElement(pi, PathElementType.CLOSE, -3, 18, 0);
		assertNoElement(pi);

		Transform3D transform = new Transform3D();
		transform.setTranslation(10, -2, 0);
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 15, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 7, 0);
		assertElement(pi, PathElementType.LINE_TO, 15, 7, 0);
		assertElement(pi, PathElementType.CLOSE, 15, 6, 0);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17.10457, 0, 6.10457, 18, 0, 5, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3.89543, 18, 0, 3, 17.10457, 0, 3, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14.89543, 0, 3.89543, 14, 0, 5, 14, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6.10457, 14, 0, 7, 14.89543, 0, 7, 16, 0);
		assertElement(pi, PathElementType.CLOSE, 7, 16, 0);
		assertNoElement(pi);
	}

	@Override
	public void createTransformedShape() {
		Transform3D transform = new Transform3D();
		transform.setTranslation(10, -2, 0);
		Shape3afp newShape = this.shape.createTransformedShape(transform);
		PathIterator3afp pi = (PathIterator3afp) newShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 7, 0);
		assertElement(pi, PathElementType.LINE_TO, 15, 7, 0);
		assertElement(pi, PathElementType.CLOSE, 15, 6, 0);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17.10457, 0, 6.10457, 18, 0, 5, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3.89543, 18, 0, 3, 17.10457, 0, 3, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14.89543, 0, 3.89543, 14, 0, 5, 14, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6.10457, 14, 0, 7, 14.89543, 0, 7, 16, 0);
		assertElement(pi, PathElementType.CLOSE, 7, 16, 0);
		assertNoElement(pi);
	}

	@Override
	public void intersectsRectangularPrism3afp() {
		// Outside
		assertFalse(this.shape.intersects(createRectangularPrism(-20, 14, 0, .5, .5, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(-2, -10, 0, .5, .5, 0)));
		// Intersecting
		assertTrue(this.shape.intersects(createRectangularPrism(-6, 16, 0, .5, .5, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(4.75, 8, 0, .5, .5, 0)));
		// Inside
		assertTrue(this.shape.intersects(createRectangularPrism(-4, 18, 0, .5, .5, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(5.5, 8.5, 0, .5, .5, 0)));
	}

	@Override
	public void intersectsSphere3afp() {
		// Outside
		assertFalse(this.shape.intersects(createSphere(-20, 14, 0, .5)));
		assertFalse(this.shape.intersects(createSphere(-2,- 10, 0, .5)));
		// Intersecting
		assertTrue(this.shape.intersects(createSphere(-6, 16, 0, .5)));
		assertTrue(this.shape.intersects(createSphere(4.75, 8, 0, .5)));
		// Inside
		assertTrue(this.shape.intersects(createSphere(-4, 18, 0, .5)));
		assertTrue(this.shape.intersects(createSphere(5.5, 8.5, 0, .5)));
	}

	@Override
	public void intersectsSegment3afp() {
		// Outside
		assertFalse(this.shape.intersects(createSegment(-20, 14, 0, -19.5, 14, 0)));
		assertFalse(this.shape.intersects(createSegment(-2, -10, 0, -1.5, -10, 0)));
		// Intersecting
		assertTrue(this.shape.intersects(createSegment(-6, 16, 0, -5.5, 16.5, 0)));
		assertTrue(this.shape.intersects(createSegment(4.75, 8, 0, 5.25, 8, 0)));
		// Inside
		assertTrue(this.shape.intersects(createSegment(-4, 18, 0, -3.5, 18, 0)));
		assertTrue(this.shape.intersects(createSegment(5.5, 8.5, 0, 6, 8.5, 0)));
	}

	@Override
	public void intersectsPath3afp() {
		Path3afp path = createPath();
		path.moveTo(-6, 2, 0);
		path.lineTo(10, 6, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-4, 12, 0);
		path.lineTo(-12, 22, 0);
		path.lineTo(6, 20, 0);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));
		
		path = createPath();
		path.moveTo(-6, 2, 0);
		path.lineTo(10, 6, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-4, 12, 0);
		path.lineTo(6, 20, 0);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath();
		path.moveTo(-6, 2, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-4, 12, 0);
		path.lineTo(-12, 22, 0);
		path.lineTo(6, 20, 0);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));
	}

	@Override
	public void intersectsPathIterator3afp() {
		Path3afp path = createPath();
		path.moveTo(-6, 2, 0);
		path.lineTo(10, 6, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-4, 12, 0);
		path.lineTo(-12, 22, 0);
		path.lineTo(6, 20, 0);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		
		path = createPath();
		path.moveTo(-6, 2, 0);
		path.lineTo(10, 6, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-4, 12, 0);
		path.lineTo(6, 20, 0);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath();
		path.moveTo(-6, 2, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-4, 12, 0);
		path.lineTo(-12, 22, 0);
		path.lineTo(6, 20, 0);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
	}

	@Override
	public void intersectsShape3D() {
		assertTrue(this.shape.intersects((Shape3D) createSphere(4.75, 8, 0, .5)));
		Path3afp path = createPath();
		path.moveTo(-6, 2, 0);
		path.lineTo(10, 6, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-4, 12, 0);
		path.lineTo(-12, 22, 0);
		path.lineTo(6, 20, 0);
		assertFalse(this.shape.intersects((Shape3D) path));
		path.closePath();
		assertTrue(this.shape.intersects((Shape3D) path));
	}

	@Override
	public void operator_addVector3D() {
		this.shape.operator_add(createVector(10, -2, 0));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 7, 0);
		assertElement(pi, PathElementType.LINE_TO, 15, 7, 0);
		assertElement(pi, PathElementType.CLOSE, 15, 6, 0);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17.10457, 0, 6.10457, 18, 0, 5, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3.89543, 18, 0, 3, 17.10457, 0, 3, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14.89543, 0, 3.89543, 14, 0, 5, 14, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6.10457, 14, 0, 7, 14.89543, 0, 7, 16, 0);
		assertElement(pi, PathElementType.CLOSE, 7, 16, 0);
		assertNoElement(pi);
	}

	@Override
	public void operator_plusVector3D() {
		T shape = this.shape.operator_plus(createVector(10, -2, 0));
		PathIterator3afp pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 7, 0);
		assertElement(pi, PathElementType.LINE_TO, 15, 7, 0);
		assertElement(pi, PathElementType.CLOSE, 15, 6, 0);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17.10457, 0, 6.10457, 18, 0, 5, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3.89543, 18, 0, 3, 17.10457, 0, 3, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14.89543, 0, 3.89543, 14, 0, 5, 14, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6.10457, 14, 0, 7, 14.89543, 0, 7, 16, 0);
		assertElement(pi, PathElementType.CLOSE, 7, 16, 0);
		assertNoElement(pi);
	}

	@Override
	public void operator_removeVector3D() {
		this.shape.operator_remove(createVector(10, -2, 0));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -5, 10, 0);
		assertElement(pi, PathElementType.LINE_TO, -3, 10, 0);
		assertElement(pi, PathElementType.LINE_TO, -3, 11, 0);
		assertElement(pi, PathElementType.LINE_TO, -5, 11, 0);
		assertElement(pi, PathElementType.CLOSE, -5, 10, 0);
		assertElement(pi, PathElementType.MOVE_TO, -13, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -13, 21.10457, 0, -13.89543, 22, 0, -15, 22, 0);
		assertElement(pi, PathElementType.CURVE_TO, -16.10457, 22, 0, -17, 21.10457, 0, -17, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -17, 18.89543, 0, -16.10457, 18, 0, -15, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -13.89543, 18, 0, -13, 18.89543, 0, -13, 20, 0);
		assertElement(pi, PathElementType.CLOSE, -13, 20, 0);
		assertNoElement(pi);
	}

	@Override
	public void operator_minusVector3D() {
		T shape = this.shape.operator_minus(createVector(10, -2, 0));
		PathIterator3afp pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -5, 10, 0);
		assertElement(pi, PathElementType.LINE_TO, -3, 10, 0);
		assertElement(pi, PathElementType.LINE_TO, -3, 11, 0);
		assertElement(pi, PathElementType.LINE_TO, -5, 11, 0);
		assertElement(pi, PathElementType.CLOSE, -5, 10, 0);
		assertElement(pi, PathElementType.MOVE_TO, -13, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -13, 21.10457, 0, -13.89543, 22, 0, -15, 22, 0);
		assertElement(pi, PathElementType.CURVE_TO, -16.10457, 22, 0, -17, 21.10457, 0, -17, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -17, 18.89543, 0, -16.10457, 18, 0, -15, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -13.89543, 18, 0, -13, 18.89543, 0, -13, 20, 0);
		assertElement(pi, PathElementType.CLOSE, -13, 20, 0);
		assertNoElement(pi);
	}

	@Override
	public void operator_multiplyTransform3D() {
		Transform3D transform = new Transform3D();
		transform.setTranslation(10, -2, 0);
		Shape3afp newShape = this.shape.operator_multiply(transform);
		PathIterator3afp pi = (PathIterator3afp) newShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 7, 0);
		assertElement(pi, PathElementType.LINE_TO, 15, 7, 0);
		assertElement(pi, PathElementType.CLOSE, 15, 6, 0);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17.10457, 0, 6.10457, 18, 0, 5, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3.89543, 18, 0, 3, 17.10457, 0, 3, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14.89543, 0, 3.89543, 14, 0, 5, 14, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6.10457, 14, 0, 7, 14.89543, 0, 7, 16, 0);
		assertElement(pi, PathElementType.CLOSE, 7, 16, 0);
		assertNoElement(pi);
	}

	@Override
	public void operator_andPoint3D() {
		assertFalse(this.shape.operator_and(createPoint(-10, 2, 0)));
		assertFalse(this.shape.operator_and(createPoint(-10, 14, 0)));
		assertFalse(this.shape.operator_and(createPoint(-10, 25, 0)));
		assertFalse(this.shape.operator_and(createPoint(-1, 25, 0)));
		assertFalse(this.shape.operator_and(createPoint(1, 2, 0)));
		assertFalse(this.shape.operator_and(createPoint(12, 2, 0)));
		assertFalse(this.shape.operator_and(createPoint(12, 14, 0)));
		assertFalse(this.shape.operator_and(createPoint(12, 25, 0)));
		assertFalse(this.shape.operator_and(createPoint(-6, 8, 0)));
		assertFalse(this.shape.operator_and(createPoint(4, 17, 0)));
		assertTrue(this.shape.operator_and(createPoint(-4, 19, 0)));
		assertTrue(this.shape.operator_and(createPoint(6, 8.25, 0)));
	}

	@Override
	public void operator_andShape3D() {
		assertTrue(this.shape.operator_and(createSphere(4.75, 8, 0, .5)));
		Path3afp path = createPath();
		path.moveTo(-6, 2, 0);
		path.lineTo(10, 6, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-4, 12, 0);
		path.lineTo(-12, 22, 0);
		path.lineTo(6, 20, 0);
		assertFalse(this.shape.operator_and(path));
		path.closePath();
		assertTrue(this.shape.operator_and(path));
	}

	@Override
	public void operator_upToPoint3D() {
		assertEpsilonEquals(14.76305, this.shape.operator_upTo(createPoint(-10, 2, 0)));
		assertEpsilonEquals(4.40312, this.shape.operator_upTo(createPoint(-10, 14, 0)));
		assertEpsilonEquals(6.60233, this.shape.operator_upTo(createPoint(-10, 25, 0)));
		assertEpsilonEquals(6.06226, this.shape.operator_upTo(createPoint(-1, 25, 0)));
		assertEpsilonEquals(7.21110, this.shape.operator_upTo(createPoint(1, 2, 0)));
		assertEpsilonEquals(7.81025, this.shape.operator_upTo(createPoint(12, 2, 0)));
		assertEpsilonEquals(7.07107, this.shape.operator_upTo(createPoint(12, 14, 0)));
		assertEpsilonEquals(16.38478, this.shape.operator_upTo(createPoint(12, 25, 0)));
		assertEpsilonEquals(8.04988, this.shape.operator_upTo(createPoint(-6, 8, 0)));
		assertEpsilonEquals(7.05538, this.shape.operator_upTo(createPoint(4, 17, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(-4, 19, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(6, 8.25, 0)));
	}

	@Test
	public void getFirstShapeContainingPoint2D() {
		// Outside bounding box
		assertNull(this.shape.getFirstShapeContaining(createPoint(-10, 2, 0)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(-10, 14, 0)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(-10, 25, 0)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(-1, 25, 0)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(1, 2, 0)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(12, 2, 0)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(12, 14, 0)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(12, 25, 0)));
		// Inside bounding box - outside subshape
		assertNull(this.shape.getFirstShapeContaining(createPoint(-6, 8, 0)));
		assertNull(this.shape.getFirstShapeContaining(createPoint(4, 17, 0)));
		// Inside circle
		assertSame(secondObject, this.shape.getFirstShapeContaining(createPoint(-4, 19, 0)));
		// Inside rectangle
		assertSame(firstObject, this.shape.getFirstShapeContaining(createPoint(6, 8.25, 0)));
	}
	
	@Test
	public void getShapesContainingPoint2D() {
		// Outside bounding box
		assertTrue(this.shape.getShapesContaining(createPoint(-10, 2, 0)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(-10, 14, 0)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(-10, 25, 0)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(-1, 25, 0)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(1, 2, 0)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(12, 2, 0)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(12, 14, 0)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(12, 25, 0)).isEmpty());
		// Inside bounding box - outside subshape
		assertTrue(this.shape.getShapesContaining(createPoint(-6, 8, 0)).isEmpty());
		assertTrue(this.shape.getShapesContaining(createPoint(4, 17, 0)).isEmpty());
		// Inside circle
		assertEquals(Arrays.asList(secondObject), this.shape.getShapesContaining(createPoint(-4, 19, 0)));
		// Inside rectangle
		assertEquals(Arrays.asList(firstObject), this.shape.getShapesContaining(createPoint(6, 8.25, 0)));
	}

	@Test
	public void getFirstShapeIntersectingShape3D() {
		MultiShape3D shape3d = this.shape;
		
		assertSame(firstObject, shape3d.getFirstShapeIntersecting(createSphere(4.75, 8, 0, .5)));
		
		Path3afp path = createPath();
		path.moveTo(-6, 2, 0);
		path.lineTo(10, 6, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-4, 12, 0);
		path.lineTo(-12, 22, 0);
		path.lineTo(6, 20, 0);

		assertNull(shape3d.getFirstShapeIntersecting(path));
		path.closePath();
		assertSame(firstObject, shape3d.getFirstShapeIntersecting(path));
	}
	
	@Test
	public void getShapesIntersectingShape3D() {
		MultiShape3D shape3d = this.shape;
		
		assertEquals(Arrays.asList(firstObject), shape3d.getShapesIntersecting(createSphere(4.75, 8, 0, .5)));
		
		Path3afp path = createPath();
		path.moveTo(-6, 2, 0);
		path.lineTo(10, 6, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-4, 12, 0);
		path.lineTo(-12, 22, 0);
		path.lineTo(6, 20, 0);

		assertTrue(shape3d.getShapesIntersecting(path).isEmpty());
		path.closePath();
		assertEquals(Arrays.asList(firstObject, secondObject), shape3d.getShapesIntersecting(path));
	}

	@Test
	public void getBackendDataList() {
		assertNotNull(this.shape.getBackendDataList());
		assertEquals(2, this.shape.getBackendDataList().size());
		assertSame(firstObject, this.shape.getBackendDataList().get(0));
		assertSame(secondObject, this.shape.getBackendDataList().get(1));
	}

	@Test
	@Ignore
	public void onGeometryChange_changeFirstObject() {
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(8, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
		assertEpsilonEquals(20, box.getMaxZ());

		firstObject.translate(12, -7, 0);
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9
		
		// R': 17;  1; 19;  2 
		
		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(1, box.getMinY());
		assertEpsilonEquals(1, box.getMinZ());
		assertEpsilonEquals(19, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
		assertEpsilonEquals(20, box.getMaxZ());
	}
	
	@Test
	@Ignore
	public void onGeometryChange_changeSecondObject() {
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(8, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
		assertEpsilonEquals(20, box.getMaxZ());

		secondObject.translate(12, -7, 0);
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9
		
		// C':  5;  9;  9; 13 

		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(8, box.getMinZ());
		assertEpsilonEquals(9, box.getMaxX());
		assertEpsilonEquals(13, box.getMaxY());
		assertEpsilonEquals(13, box.getMaxZ());
	}

	@Test
	@Ignore
	public void onBackendDataListChange_addition() {
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(8, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
		assertEpsilonEquals(20, box.getMaxZ());

		this.shape.add((C) createSphere(10, 14, 0, 1));
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9
		
		// C':  9;  13;  11; 15 

		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(8, box.getMinZ());
		assertEpsilonEquals(11, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
		assertEpsilonEquals(20, box.getMaxZ());
	}

	@Test
	@Ignore
	public void onBackendDataListChange_removalFirstObject() {
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(8, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
		assertEpsilonEquals(20, box.getMaxZ());

		this.shape.remove(firstObject);
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9
		
		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(16, box.getMinY());
		assertEpsilonEquals(16, box.getMinZ());
		assertEpsilonEquals(-3, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
		assertEpsilonEquals(20, box.getMaxZ());
	}

	@Test
	@Ignore
	public void onBackendDataListChange_removalSecondObject() {
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(8, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
		assertEpsilonEquals(20, box.getMaxZ());

		this.shape.remove(secondObject);
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9

		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(8, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(9, box.getMaxY());
		assertEpsilonEquals(9, box.getMaxZ());
	}

	@Test
	@Ignore
	public void noGeometryChangeAfterRemoval() {
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(8, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
		assertEpsilonEquals(20, box.getMaxZ());

		this.shape.remove(secondObject);
		secondObject.translate(1453, -451, 0);
		
		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(8, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(9, box.getMaxY());
		assertEpsilonEquals(9, box.getMaxZ());
	}

}
