/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.MultiShape2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.arakhne.afc.math.geometry.d2.ai.Shape2ai;

@SuppressWarnings("all")
public abstract class AbstractMultiShape2aiTest<T extends MultiShape2ai<?, T, C, ?, ?, ?, B>,
		C extends Shape2ai<?, ?, ?, ?, ?, B>,
		B extends Rectangle2ai<?, ?, ?, ?, ?, B>> extends AbstractShape2aiTest<T, B> {

	private C firstObject;
	private C secondObject;
	
	@Override
	protected final T createShape() {
		T shape = (T) createMultiShape();
		this.firstObject = (C) createRectangle(5, 8, 2, 1);
		this.secondObject = (C) createCircle(-5, 18, 2);
		shape.add(this.firstObject);
		shape.add(this.secondObject);
		return shape;
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void testClone(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void equalsObject(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createMultiShape()));
		assertFalse(this.shape.equals(createSegment(5, 8, 5, 10)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(this.shape.clone()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void equalsObject_withPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createMultiShape().getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 5, 10).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(this.shape.clone().getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void equalsToPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
		assertFalse(this.shape.equalsToPathIterator(createMultiShape().getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 5, 10).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.clone().getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void equalsToShape(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createMultiShape()));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape(this.shape.clone()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void isEmpty(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void clear(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		PathIterator2ai pi = this.shape.getPathIterator();
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsRectangle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void containsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFalse(this.shape.contains(createCircle(-20, 14, 1)));
        assertFalse(this.shape.contains(createCircle(-2,-10, 1)));
        assertFalse(this.shape.contains(createCircle(-6,16, 1)));
        assertFalse(this.shape.contains(createCircle(4, 8, 1)));
        assertFalse(this.shape.contains(createCircle(5, 8, 1)));
        assertTrue(this.shape.contains(createCircle(-4, 18, 0)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFarthestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistance(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquared(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceL1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceLinf(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setIT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void translateIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void translateVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void toBoundingBox(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void toBoundingBoxB(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2ai pi = this.shape.getPathIterator();
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIteratorTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void createTransformedShape(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsRectangle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsCircle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsSegment2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPath2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPathIterator2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_addVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_plusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_removeVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_minusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_multiplyTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createCircle(-6, 16, 1)));
		assertTrue(this.shape.operator_and(createRectangle(-6, 16, 1, 1)));

	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_upToPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape2D) createCircle(-6, 16, 1)));
		assertTrue(this.shape.intersects((Shape2D) createRectangle(-6, 16, 1, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPointIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;
		Iterator<? extends Point2D> it = this.shape.getPointIterator();
		
		// Rectangle points
		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(5, p.ix(), p.toString());
		assertEquals(8, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(6, p.ix(), p.toString());
		assertEquals(8, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(8, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(9, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(6, p.ix(), p.toString());
		assertEquals(9, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(5, p.ix(), p.toString());
		assertEquals(9, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(5, p.ix(), p.toString());
		assertEquals(8, p.iy(), p.toString());

		// Circle points
		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(-5, p.ix(), p.toString());
		assertEquals(20, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(-4, p.ix(), p.toString());
		assertEquals(20, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(-3, p.ix(), p.toString());
		assertEquals(18, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(-3, p.ix(), p.toString());
		assertEquals(19, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(-5, p.ix(), p.toString());
		assertEquals(16, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(-4, p.ix(), p.toString());
		assertEquals(16, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(-3, p.ix(), p.toString());
		assertEquals(17, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(-6, p.ix(), p.toString());
		assertEquals(16, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(-7, p.ix(), p.toString());
		assertEquals(18, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(-7, p.ix(), p.toString());
		assertEquals(17, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(-6, p.ix(), p.toString());
		assertEquals(20, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(-7, p.ix(), p.toString());
		assertEquals(19, p.iy(), p.toString());

		assertFalse(it.hasNext());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFirstShapeContainingPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
		assertSame(this.secondObject, this.shape.getFirstShapeContaining(createPoint(-4, 19)));
		// Inside rectangle
		assertSame(this.firstObject, this.shape.getFirstShapeContaining(createPoint(6, 8)));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getShapesContainingPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
		assertEquals(Arrays.asList(this.secondObject), this.shape.getShapesContaining(createPoint(-4, 19)));
		// Inside rectangle
		assertEquals(Arrays.asList(this.firstObject), this.shape.getShapesContaining(createPoint(6, 8)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFirstShapeIntersectingShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		MultiShape2D shape2d = this.shape;
		assertNull(shape2d.getFirstShapeIntersecting(createSegment(-20, 14, -19, 14)));
		assertNull(shape2d.getFirstShapeIntersecting(createSegment(-2, -10, -1, -10)));
		assertSame(this.secondObject, shape2d.getFirstShapeIntersecting(createSegment(-6, 16, -5, 16)));
		assertSame(this.firstObject, shape2d.getFirstShapeIntersecting(createSegment(4, 8, 5, 8)));
		assertSame(this.secondObject, shape2d.getFirstShapeIntersecting(createSegment(-4, 18, -3, 18)));
		assertSame(this.firstObject, shape2d.getFirstShapeIntersecting(createSegment(5, 8, 6, 8)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFirstShapeIntersectingShape2D_withOpenPath(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFirstShapeIntersectingShape2D_withClosePath(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		MultiShape2D shape2d = this.shape;
		
		Path2ai path = createPath();
		path.moveTo(-4, 3);
		path.lineTo(9, 6);
		path.lineTo(8, 14);
		path.lineTo(-9, 11);
		path.lineTo(-8, 21);
		path.lineTo(4, 21);
		path.closePath();
		
		assertSame(this.firstObject, shape2d.getFirstShapeIntersecting(path));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getShapesIntersectingShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		MultiShape2D shape2d = this.shape;
		assertTrue(shape2d.getShapesIntersecting(createSegment(-20, 14, -19, 14)).isEmpty());
		assertTrue(shape2d.getShapesIntersecting(createSegment(-2, -10, -1, -10)).isEmpty());
		assertEquals(Arrays.asList(this.secondObject), shape2d.getShapesIntersecting(createSegment(-6, 16, -5, 16)));
		assertEquals(Arrays.asList(this.firstObject), shape2d.getShapesIntersecting(createSegment(4, 8, 5, 8)));
		assertEquals(Arrays.asList(this.secondObject), shape2d.getShapesIntersecting(createSegment(-4, 18, -3, 18)));
		assertEquals(Arrays.asList(this.firstObject), shape2d.getShapesIntersecting(createSegment(5, 8, 6, 8)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getBackendDataList(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertNotNull(this.shape.getBackendDataList());
		assertEquals(2, this.shape.getBackendDataList().size());
		assertSame(this.firstObject, this.shape.getBackendDataList().get(0));
		assertSame(this.secondObject, this.shape.getBackendDataList().get(1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void onGeometryChange_changeFirstObject(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		this.firstObject.translate(12, -7);
		
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void onGeometryChange_changeSecondObject(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		this.secondObject.translate(12, -7);
		
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void onBackendDataListChange_addition(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void onBackendDataListChange_removalFirstObject(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		this.shape.remove(this.firstObject);
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9
		
		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(16, box.getMinY());
		assertEquals(-3, box.getMaxX());
		assertEquals(20, box.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void onBackendDataListChange_removalSecondObject(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		this.shape.remove(this.secondObject);
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9

		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(5, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(9, box.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void noGeometryChangeAfterRemoval(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		this.shape.remove(this.secondObject);
		this.secondObject.translate(1453,  -451);
		
		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(5, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(9, box.getMaxY());
	}

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToCircle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertIntPointEquals(-5, 16, this.shape.getClosestPointTo(createCircle(-5, 9, 2)));
        assertClosestPointInBothShapes(this.shape, createCircle(5, 9, 2));
        assertClosestPointInBothShapes(this.shape, createCircle(-8, 17, 2));
        assertIntPointEquals(7, 9, this.shape.getClosestPointTo(createCircle(15, 10, 2)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredCircle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(25, this.shape.getDistanceSquared(createCircle(-5, 9, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(5, 9, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(-8, 17, 2)));
        assertEpsilonEquals(36, this.shape.getDistanceSquared(createCircle(15, 10, 2)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToSegment2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertIntPointEquals(-4, 16, this.shape.getClosestPointTo(createSegment(-5, 9, 2, 21)));
        assertClosestPointInBothShapes(this.shape, createSegment(5, 9, 2, 21));
        assertClosestPointInBothShapes(this.shape, createSegment(-8, 17, 2, 21));
        assertIntPointEquals(7, 9, this.shape.getClosestPointTo(createSegment(15, 10, 2, 45)));
    }
    
    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredSegment2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(5, this.shape.getDistanceSquared(createSegment(-5, 9, 2, 21)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(5, 9, 2, 21)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(-8, 17, 2, 21)));
        assertEpsilonEquals(58, this.shape.getDistanceSquared(createSegment(15, 10, 2, 45)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToRectangle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertIntPointEquals(-5, 16, this.shape.getClosestPointTo(createRectangle(-5, 9, 2, 2)));
        assertClosestPointInBothShapes(this.shape, createRectangle(5, 9, 2, 2));
        assertClosestPointInBothShapes(this.shape, createRectangle(-8, 17, 2, 2));
        assertIntPointEquals(7, 9, this.shape.getClosestPointTo(createRectangle(15, 10, 2, 2)));
    }
    
    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredRectangle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(25, this.shape.getDistanceSquared(createRectangle(-5, 9, 2, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(5, 9, 2, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(-8, 17, 2, 2)));
        assertEpsilonEquals(65, this.shape.getDistanceSquared(createRectangle(15, 10, 2, 2)));
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
        assertIntPointEquals(-4, 16, this.shape.getClosestPointTo(createTestMultiShape(-5, 9)));
        assertIntPointEquals(7, 9, this.shape.getClosestPointTo(createTestMultiShape(5, 9)));
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(-8, 17));
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(15, 10));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredMultiShape2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(17, this.shape.getDistanceSquared(createTestMultiShape(-5, 9)));
        assertEpsilonEquals(1, this.shape.getDistanceSquared(createTestMultiShape(5, 9)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(-8, 17)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(15, 10)));
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
        assertIntPointEquals(6, 8, this.shape.getClosestPointTo(createTestPath(-5, 9)));
        assertIntPointEquals(-5, 18, this.shape.getClosestPointTo(createTestPath(-8, 17)));
        assertClosestPointInBothShapes(this.shape, createTestPath(5, 9));
        assertIntPointEquals(7, 9, this.shape.getClosestPointTo(createTestPath(15, 10)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredPath2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(-5, 9)));
        assertEpsilonEquals(9, this.shape.getDistanceSquared(createTestPath(5, 17)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(-8, 17)));
        assertEpsilonEquals(10, this.shape.getDistanceSquared(createTestPath(15, 10)));
    }

}