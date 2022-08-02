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

package org.arakhne.afc.math.test.geometry.d2.afp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.MultiShape2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Shape2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;


@SuppressWarnings("all")
public abstract class AbstractMultiShape2afpTest<T extends MultiShape2afp<?, T, C, ?, ?, ?, B>,
		C extends Shape2afp<?, ?, ?, ?, ?, B>,
		B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractShape2afpTest<T, B> {

	protected C firstObject;
	
	protected C secondObject;

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
		MultiShape2afp clone = this.shape.clone();
		PathIterator2afp pi = (PathIterator2afp) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 9);
		assertElement(pi, PathElementType.LINE_TO, 5, 9);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19.10457, -3.89543, 20, -5, 20);
		assertElement(pi, PathElementType.CURVE_TO, -6.10457, 20, -7, 19.10457, -7, 18);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16.89543, -6.10457, 16, -5, 16);
		assertElement(pi, PathElementType.CURVE_TO, -3.89543, 16, -3, 16.89543, -3, 18);
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
		PathIterator2afp pi = this.shape.getPathIterator();
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsDoubleDouble(CoordinateSystem2D cs) {
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
		assertTrue(this.shape.contains(6, 8.25));
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
		assertTrue(this.shape.contains(createPoint(6, 8.25)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Outside
		assertFalse(this.shape.contains(createRectangle(-20, 14, .5, .5)));
		assertFalse(this.shape.contains(createRectangle(-2,-10, .5, .5)));
		// Intersecting
		assertFalse(this.shape.contains(createRectangle(-6,16, .5, .5)));
		assertFalse(this.shape.contains(createRectangle(4.75, 8, .5, .5)));
		// Inside
		assertTrue(this.shape.contains(createRectangle(-4, 18, .5, .5)));
		assertTrue(this.shape.contains(createRectangle(5.5, 8.5, .5, .5)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createCircle(-20, 14, .5)));
		assertFalse(this.shape.contains(createCircle(-2,-10, .5)));
		assertFalse(this.shape.contains(createCircle(-6,16, .5)));
		assertFalse(this.shape.contains(createCircle(4.75, 8, .5)));
		assertTrue(this.shape.contains(createCircle(-4, 18, .5)));
		assertTrue(this.shape.contains(createCircle(5.5, 8.5, .5)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;
		// Outside bounding box
		result = this.shape.getClosestPointTo(createPoint(-10, 2));
		assertEpsilonEquals(-5.59655, result.getX());
		assertEpsilonEquals(16.09104, result.getY());
		result = this.shape.getClosestPointTo(createPoint(-10, 14));
		assertEpsilonEquals(-6.56174, result.getX());
		assertEpsilonEquals(16.75061, result.getY());
		result = this.shape.getClosestPointTo(createPoint(-10, 25));
		assertEpsilonEquals(-6.16248, result.getX());
		assertEpsilonEquals(19.62747, result.getY());
		result = this.shape.getClosestPointTo(createPoint(-1, 25));
		assertEpsilonEquals(-4.00772, result.getX());
		assertEpsilonEquals(19.73649, result.getY());
		result = this.shape.getClosestPointTo(createPoint(1, 2));
		assertEpsilonEquals(5, result.getX());
		assertEpsilonEquals(8, result.getY());
		result = this.shape.getClosestPointTo(createPoint(12, 2));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(8, result.getY());
		result = this.shape.getClosestPointTo(createPoint(12, 14));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(9, result.getY());
		result = this.shape.getClosestPointTo(createPoint(12, 25));
		assertEpsilonEquals(-3.15064, result.getX());
		assertEpsilonEquals(18.7615, result.getY());
		// Inside bounding box - outside subshape
		result = this.shape.getClosestPointTo(createPoint(-6, 8));
		assertEpsilonEquals(-5.19901, result.getX());
		assertEpsilonEquals(16.00993, result.getY());
		result = this.shape.getClosestPointTo(createPoint(4, 17));
		assertEpsilonEquals(-3.01223, result.getX());
		assertEpsilonEquals(17.77914, result.getY());
		// Inside circle
		result = this.shape.getClosestPointTo(createPoint(-4, 19));
		assertEpsilonEquals(-4, result.getX());
		assertEpsilonEquals(19, result.getY());
		// Inside rectangle
		result = this.shape.getClosestPointTo(createPoint(6, 8.25));
		assertEpsilonEquals(6, result.getX());
		assertEpsilonEquals(8.25, result.getY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFarthestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;
		// Outside bounding box
		result = this.shape.getFarthestPointTo(createPoint(-10, 2));
		assertEpsilonEquals(-4.40345, result.getX());
		assertEpsilonEquals(19.90896, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(-10, 14));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(8, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(-10, 25));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(8, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(-1, 25));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(8, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(1, 2));
		assertEpsilonEquals(-5.70225, result.getX());
		assertEpsilonEquals(19.87266, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(12, 2));
		assertEpsilonEquals(-6.4564, result.getX());
		assertEpsilonEquals(19.37073, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(12, 14));
		assertEpsilonEquals(-6.94683, result.getX());
		assertEpsilonEquals(18.45808, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(12, 25));
		assertEpsilonEquals(-6.84936, result.getX());
		assertEpsilonEquals(17.2385, result.getY());
		// Inside bounding box - outside subshape
		result = this.shape.getFarthestPointTo(createPoint(-6, 8));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(9, result.getY());
		result = this.shape.getFarthestPointTo(createPoint(4, 17));
		assertEpsilonEquals(-6.98777, result.getX());
		assertEpsilonEquals(18.22086, result.getY());
		// Inside circle
		result = this.shape.getFarthestPointTo(createPoint(-4, 19));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(8, result.getY());
		// Inside rectangle
		result = this.shape.getFarthestPointTo(createPoint(6, 8.25));
		assertEpsilonEquals(-6.49669, result.getX());
		assertEpsilonEquals(19.32662, result.getY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistance(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Outside bounding box
		assertEpsilonEquals(14.76305, this.shape.getDistance(createPoint(-10, 2)));
		assertEpsilonEquals(4.40312, this.shape.getDistance(createPoint(-10, 14)));
		assertEpsilonEquals(6.60233, this.shape.getDistance(createPoint(-10, 25)));
		assertEpsilonEquals(6.06226, this.shape.getDistance(createPoint(-1, 25)));
		assertEpsilonEquals(7.21110, this.shape.getDistance(createPoint(1, 2)));
		assertEpsilonEquals(7.81025, this.shape.getDistance(createPoint(12, 2)));
		assertEpsilonEquals(7.07107, this.shape.getDistance(createPoint(12, 14)));
		assertEpsilonEquals(16.38478, this.shape.getDistance(createPoint(12, 25)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(8.04988, this.shape.getDistance(createPoint(-6, 8)));
		assertEpsilonEquals(7.05538, this.shape.getDistance(createPoint(4, 17)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(-4, 19)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(6, 8.25)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquared(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Outside bounding box
		assertEpsilonEquals(217.94778, this.shape.getDistanceSquared(createPoint(-10, 2)));
		assertEpsilonEquals(19.38749, this.shape.getDistanceSquared(createPoint(-10, 14)));
		assertEpsilonEquals(43.5907, this.shape.getDistanceSquared(createPoint(-10, 25)));
		assertEpsilonEquals(36.75092, this.shape.getDistanceSquared(createPoint(-1, 25)));
		assertEpsilonEquals(52, this.shape.getDistanceSquared(createPoint(1, 2)));
		assertEpsilonEquals(61, this.shape.getDistanceSquared(createPoint(12, 2)));
		assertEpsilonEquals(50, this.shape.getDistanceSquared(createPoint(12, 14)));
		assertEpsilonEquals(268.46089, this.shape.getDistanceSquared(createPoint(12, 25)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(64.8005, this.shape.getDistanceSquared(createPoint(-6, 8)));
		assertEpsilonEquals(49.77843, this.shape.getDistanceSquared(createPoint(4, 17)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(-4, 19)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(6, 8.25)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceL1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Outside bounding box
		assertEpsilonEquals(18.49449, this.shape.getDistanceL1(createPoint(-10, 2)));
		assertEpsilonEquals(6.18887, this.shape.getDistanceL1(createPoint(-10, 14)));
		assertEpsilonEquals(9.21006, this.shape.getDistanceL1(createPoint(-10, 25)));
		assertEpsilonEquals(8.27123, this.shape.getDistanceL1(createPoint(-1, 25)));
		assertEpsilonEquals(10, this.shape.getDistanceL1(createPoint(1, 2)));
		assertEpsilonEquals(11, this.shape.getDistanceL1(createPoint(12, 2)));
		assertEpsilonEquals(10, this.shape.getDistanceL1(createPoint(12, 14)));
		assertEpsilonEquals(21, this.shape.getDistanceL1(createPoint(12, 25)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(8.81092, this.shape.getDistanceL1(createPoint(-6, 8)));
		assertEpsilonEquals(7.79137, this.shape.getDistanceL1(createPoint(4, 17)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(-4, 19)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(6, 8.25)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceLinf(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Outside bounding box
		assertEpsilonEquals(14.09104, this.shape.getDistanceLinf(createPoint(-10, 2)));
		assertEpsilonEquals(3.43826, this.shape.getDistanceLinf(createPoint(-10, 14)));
		assertEpsilonEquals(5.37253, this.shape.getDistanceLinf(createPoint(-10, 25)));
		assertEpsilonEquals(5.26351, this.shape.getDistanceLinf(createPoint(-1, 25)));
		assertEpsilonEquals(6, this.shape.getDistanceLinf(createPoint(1, 2)));
		assertEpsilonEquals(6, this.shape.getDistanceLinf(createPoint(12, 2)));
		assertEpsilonEquals(5, this.shape.getDistanceLinf(createPoint(12, 14)));
		assertEpsilonEquals(15.15064, this.shape.getDistanceLinf(createPoint(12, 25)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(8.00993, this.shape.getDistanceLinf(createPoint(-6, 8)));
		assertEpsilonEquals(7.01223, this.shape.getDistanceLinf(createPoint(4, 17)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(-4, 19)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(6, 8.25)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setIT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createMultiShape());
		PathIterator2afp pi = this.shape.getPathIterator();
		assertNoElement(pi);
		MultiShape2afp newShape = createMultiShape();
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
	public void translateDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.translate(10, -2);
		PathIterator2afp pi = this.shape.getPathIterator();
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void translateVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(10, -2));
		PathIterator2afp pi = this.shape.getPathIterator();
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
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 9);
		assertElement(pi, PathElementType.LINE_TO, 5, 9);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19.10457, -3.89543, 20, -5, 20);
		assertElement(pi, PathElementType.CURVE_TO, -6.10457, 20, -7, 19.10457, -7, 18);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16.89543, -6.10457, 16, -5, 16);
		assertElement(pi, PathElementType.CURVE_TO, -3.89543, 16, -3, 16.89543, -3, 18);
		assertElement(pi, PathElementType.CLOSE, -3, 18);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIteratorTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 9);
		assertElement(pi, PathElementType.LINE_TO, 5, 9);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19.10457, -3.89543, 20, -5, 20);
		assertElement(pi, PathElementType.CURVE_TO, -6.10457, 20, -7, 19.10457, -7, 18);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16.89543, -6.10457, 16, -5, 16);
		assertElement(pi, PathElementType.CURVE_TO, -3.89543, 16, -3, 16.89543, -3, 18);
		assertElement(pi, PathElementType.CLOSE, -3, 18);
		assertNoElement(pi);

		pi = this.shape.getPathIterator(new Transform2D());
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 8);
		assertElement(pi, PathElementType.LINE_TO, 7, 9);
		assertElement(pi, PathElementType.LINE_TO, 5, 9);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19.10457, -3.89543, 20, -5, 20);
		assertElement(pi, PathElementType.CURVE_TO, -6.10457, 20, -7, 19.10457, -7, 18);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16.89543, -6.10457, 16, -5, 16);
		assertElement(pi, PathElementType.CURVE_TO, -3.89543, 16, -3, 16.89543, -3, 18);
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
		assertElement(pi, PathElementType.CURVE_TO, 7, 17.10457, 6.10457, 18, 5, 18);
		assertElement(pi, PathElementType.CURVE_TO, 3.89543, 18, 3, 17.10457, 3, 16);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14.89543, 3.89543, 14, 5, 14);
		assertElement(pi, PathElementType.CURVE_TO, 6.10457, 14, 7, 14.89543, 7, 16);
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
		Shape2afp newShape = this.shape.createTransformedShape(transform);
		PathIterator2afp pi = (PathIterator2afp) newShape.getPathIterator();
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Outside
		assertFalse(this.shape.intersects(createRectangle(-20, 14, .5, .5)));
		assertFalse(this.shape.intersects(createRectangle(-2, -10, .5, .5)));
		// Intersecting
		assertTrue(this.shape.intersects(createRectangle(-6, 16, .5, .5)));
		assertTrue(this.shape.intersects(createRectangle(4.75, 8, .5, .5)));
		// Inside
		assertTrue(this.shape.intersects(createRectangle(-4, 18, .5, .5)));
		assertTrue(this.shape.intersects(createRectangle(5.5, 8.5, .5, .5)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Outside
		assertFalse(this.shape.intersects(createCircle(-20, 14, .5)));
		assertFalse(this.shape.intersects(createCircle(-2,- 10, .5)));
		// Intersecting
		assertTrue(this.shape.intersects(createCircle(-6, 16, .5)));
		assertTrue(this.shape.intersects(createCircle(4.75, 8, .5)));
		// Inside
		assertTrue(this.shape.intersects(createCircle(-4, 18, .5)));
		assertTrue(this.shape.intersects(createCircle(5.5, 8.5, .5)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Outside
		assertFalse(this.shape.intersects(createTriangle(-20, 14, -19.5, 14, -20, 14.5)));
		assertFalse(this.shape.intersects(createTriangle(-2, -10, -1.5, -10, -2, -9.5)));
		// Intersecting
		assertTrue(this.shape.intersects(createTriangle(-6, 16, -5.5, 16, -6, 16.5)));
		assertTrue(this.shape.intersects(createTriangle(4.75, 8, 5.25, 8, 4.75, 8.5)));
		// Inside
		assertTrue(this.shape.intersects(createTriangle(-4, 18, -3.5, 18, -4, 18.5)));
		assertTrue(this.shape.intersects(createTriangle(5.5, 8.5, 6, 8.5, 5.5, 9)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Outside
		assertFalse(this.shape.intersects(createEllipse(-20, 14, .5, .5)));
		assertFalse(this.shape.intersects(createEllipse(-2, -10, .5, .5)));
		// Intersecting
		assertTrue(this.shape.intersects(createEllipse(-6, 16, .5, .5)));
		assertTrue(this.shape.intersects(createEllipse(4.75, 8, .5, .5)));
		// Inside
		assertTrue(this.shape.intersects(createEllipse(-4, 18, .5, .5)));
		assertTrue(this.shape.intersects(createEllipse(5.5, 8.5, .5, .5)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Outside
		assertFalse(this.shape.intersects(createSegment(-20, 14, -19.5, 14)));
		assertFalse(this.shape.intersects(createSegment(-2, -10, -1.5, -10)));
		// Intersecting
		assertTrue(this.shape.intersects(createSegment(-6, 16, -5.5, 16.5)));
		assertTrue(this.shape.intersects(createSegment(4.75, 8, 5.25, 8)));
		// Inside
		assertTrue(this.shape.intersects(createSegment(-4, 18, -3.5, 18)));
		assertTrue(this.shape.intersects(createSegment(5.5, 8.5, 6, 8.5)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPath2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2afp path = createPath();
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
	public void intersectsPathIterator2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2afp path = createPath();
		path.moveTo(-6, 2);
		path.lineTo(10, 6);
		path.lineTo(8, 14);
		path.lineTo(-4, 12);
		path.lineTo(-12, 22);
		path.lineTo(6, 20);
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		
		path = createPath();
		path.moveTo(-6, 2);
		path.lineTo(10, 6);
		path.lineTo(8, 14);
		path.lineTo(-4, 12);
		path.lineTo(6, 20);
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator2afp) path.getPathIterator()));

		path = createPath();
		path.moveTo(-6, 2);
		path.lineTo(8, 14);
		path.lineTo(-4, 12);
		path.lineTo(-12, 22);
		path.lineTo(6, 20);
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsOrientedRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Outside
		assertFalse(this.shape.intersects(createOrientedRectangle(-20, 14, 1, 0, .5, .5)));
		assertFalse(this.shape.intersects(createOrientedRectangle(-2, -10, 1, 0, .5, .5)));
		// Intersecting
		assertTrue(this.shape.intersects(createOrientedRectangle(-6, 16, 1, 0, .5, .5)));
		assertTrue(this.shape.intersects(createOrientedRectangle(4.75, 8, 1, 0, .5, .5)));
		// Inside
		assertTrue(this.shape.intersects(createOrientedRectangle(-4, 18, 1, 0, .5, .5)));
		assertTrue(this.shape.intersects(createOrientedRectangle(5.5, 8.5, 1, 0, .5, .5)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsParallelogram2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Outside
		assertFalse(this.shape.intersects(createParallelogram(-20, 14, 1, 0, .5, 0, 1, .5)));
		assertFalse(this.shape.intersects(createParallelogram(-2, -10, 1, 0, .5, 0, 1, .5)));
		// Intersecting
		assertTrue(this.shape.intersects(createParallelogram(-6, 16, 1, 0, .5, 0, 1, .5)));
		assertTrue(this.shape.intersects(createParallelogram(4.75, 8, 1, 0, .5, 0, 1, .5)));
		// Inside
		assertTrue(this.shape.intersects(createParallelogram(-4, 18, 1, 0, .5, 0, 1, .5)));
		assertTrue(this.shape.intersects(createParallelogram(5.5, 8.5, 1, 0, .5, 0, 1, .5)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Outside
		assertFalse(this.shape.intersects(createRoundRectangle(-20, 14, .5, .5, .1, .1)));
		assertFalse(this.shape.intersects(createRoundRectangle(-2, -10, .5, .5, .1, .1)));
		// Intersecting
		assertTrue(this.shape.intersects(createRoundRectangle(-6, 16, .5, .5, .1, .1)));
		assertTrue(this.shape.intersects(createRoundRectangle(4.75, 8, .5, .5, .1, .1)));
		// Inside
		assertTrue(this.shape.intersects(createRoundRectangle(-4, 18, .5, .5, .1, .1)));
		assertTrue(this.shape.intersects(createRoundRectangle(5.5, 8.5, .5, .5, .1, .1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape2D) createCircle(4.75, 8, .5)));
		Path2afp path = createPath();
		path.moveTo(-6, 2);
		path.lineTo(10, 6);
		path.lineTo(8, 14);
		path.lineTo(-4, 12);
		path.lineTo(-12, 22);
		path.lineTo(6, 20);
		assertFalse(this.shape.intersects((Shape2D) path));
		path.closePath();
		assertTrue(this.shape.intersects((Shape2D) path));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_addVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(10, -2));
		PathIterator2afp pi = this.shape.getPathIterator();
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_plusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_plus(createVector(10, -2));
		PathIterator2afp pi = shape.getPathIterator();
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_removeVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(10, -2));
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -5, 10);
		assertElement(pi, PathElementType.LINE_TO, -3, 10);
		assertElement(pi, PathElementType.LINE_TO, -3, 11);
		assertElement(pi, PathElementType.LINE_TO, -5, 11);
		assertElement(pi, PathElementType.CLOSE, -5, 10);
		assertElement(pi, PathElementType.MOVE_TO, -13, 20);
		assertElement(pi, PathElementType.CURVE_TO, -13, 21.10457, -13.89543, 22, -15, 22);
		assertElement(pi, PathElementType.CURVE_TO, -16.10457, 22, -17, 21.10457, -17, 20);
		assertElement(pi, PathElementType.CURVE_TO, -17, 18.89543, -16.10457, 18, -15, 18);
		assertElement(pi, PathElementType.CURVE_TO, -13.89543, 18, -13, 18.89543, -13, 20);
		assertElement(pi, PathElementType.CLOSE, -13, 20);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_minusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_minus(createVector(10, -2));
		PathIterator2afp pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -5, 10);
		assertElement(pi, PathElementType.LINE_TO, -3, 10);
		assertElement(pi, PathElementType.LINE_TO, -3, 11);
		assertElement(pi, PathElementType.LINE_TO, -5, 11);
		assertElement(pi, PathElementType.CLOSE, -5, 10);
		assertElement(pi, PathElementType.MOVE_TO, -13, 20);
		assertElement(pi, PathElementType.CURVE_TO, -13, 21.10457, -13.89543, 22, -15, 22);
		assertElement(pi, PathElementType.CURVE_TO, -16.10457, 22, -17, 21.10457, -17, 20);
		assertElement(pi, PathElementType.CURVE_TO, -17, 18.89543, -16.10457, 18, -15, 18);
		assertElement(pi, PathElementType.CURVE_TO, -13.89543, 18, -13, 18.89543, -13, 20);
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
		Shape2afp newShape = this.shape.operator_multiply(transform);
		PathIterator2afp pi = (PathIterator2afp) newShape.getPathIterator();
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
		assertTrue(this.shape.operator_and(createPoint(6, 8.25)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createCircle(4.75, 8, .5)));
		Path2afp path = createPath();
		path.moveTo(-6, 2);
		path.lineTo(10, 6);
		path.lineTo(8, 14);
		path.lineTo(-4, 12);
		path.lineTo(-12, 22);
		path.lineTo(6, 20);
		assertFalse(this.shape.operator_and(path));
		path.closePath();
		assertTrue(this.shape.operator_and(path));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_upToPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(14.76305, this.shape.operator_upTo(createPoint(-10, 2)));
		assertEpsilonEquals(4.40312, this.shape.operator_upTo(createPoint(-10, 14)));
		assertEpsilonEquals(6.60233, this.shape.operator_upTo(createPoint(-10, 25)));
		assertEpsilonEquals(6.06226, this.shape.operator_upTo(createPoint(-1, 25)));
		assertEpsilonEquals(7.21110, this.shape.operator_upTo(createPoint(1, 2)));
		assertEpsilonEquals(7.81025, this.shape.operator_upTo(createPoint(12, 2)));
		assertEpsilonEquals(7.07107, this.shape.operator_upTo(createPoint(12, 14)));
		assertEpsilonEquals(16.38478, this.shape.operator_upTo(createPoint(12, 25)));
		assertEpsilonEquals(8.04988, this.shape.operator_upTo(createPoint(-6, 8)));
		assertEpsilonEquals(7.05538, this.shape.operator_upTo(createPoint(4, 17)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(-4, 19)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(6, 8.25)));
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
		assertSame(this.firstObject, this.shape.getFirstShapeContaining(createPoint(6, 8.25)));
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
		assertEquals(Arrays.asList(this.firstObject), this.shape.getShapesContaining(createPoint(6, 8.25)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFirstShapeIntersectingShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		MultiShape2D shape2d = this.shape;
		
		assertSame(this.firstObject, shape2d.getFirstShapeIntersecting(createCircle(4.75, 8, .5)));
		
		Path2afp path = createPath();
		path.moveTo(-6, 2);
		path.lineTo(10, 6);
		path.lineTo(8, 14);
		path.lineTo(-4, 12);
		path.lineTo(-12, 22);
		path.lineTo(6, 20);

		assertNull(shape2d.getFirstShapeIntersecting(path));
		path.closePath();
		assertSame(this.firstObject, shape2d.getFirstShapeIntersecting(path));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getShapesIntersectingShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		MultiShape2D shape2d = this.shape;
		
		assertEquals(Arrays.asList(this.firstObject), shape2d.getShapesIntersecting(createCircle(4.75, 8, .5)));
		
		Path2afp path = createPath();
		path.moveTo(-6, 2);
		path.lineTo(10, 6);
		path.lineTo(8, 14);
		path.lineTo(-4, 12);
		path.lineTo(-12, 22);
		path.lineTo(6, 20);

		assertTrue(shape2d.getShapesIntersecting(path).isEmpty());
		path.closePath();
		assertEquals(Arrays.asList(this.firstObject, this.secondObject), shape2d.getShapesIntersecting(path));
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
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());

		this.firstObject.translate(12, -7);
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9
		
		// R': 17;  1; 19;  2 
		
		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(1, box.getMinY());
		assertEpsilonEquals(19, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void onGeometryChange_changeSecondObject(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());

		this.secondObject.translate(12, -7);
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9
		
		// C':  5;  9;  9; 13 

		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(9, box.getMaxX());
		assertEpsilonEquals(13, box.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void onBackendDataListChange_addition(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());

		this.shape.add((C) createCircle(10, 14, 1));
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9
		
		// C':  9;  13;  11; 15 

		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(11, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void onBackendDataListChange_removalFirstObject(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());

		this.shape.remove(this.firstObject);
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9
		
		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(16, box.getMinY());
		assertEpsilonEquals(-3, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void onBackendDataListChange_removalSecondObject(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());

		this.shape.remove(this.secondObject);
		
		// C:  -7; 16; -3; 20
		// R:   5;  8;  7;  9

		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(9, box.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void noGeometryChangeAfterRemoval(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());

		this.shape.remove(this.secondObject);
		this.secondObject.translate(1453,  -451);
		
		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(9, box.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createCircle(0, 0, 1)));
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createCircle(2, 8, 1)));
		assertFpPointEquals(-3.21115, 17.10557, this.shape.getClosestPointTo(createCircle(-1, 16, 1)));
		assertClosestPointInBothShapes(this.shape, createCircle(-4, 17, 1));
		assertClosestPointInBothShapes(this.shape, createCircle(6, 9.5, 1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(71.13204, this.shape.getDistanceSquared(createCircle(0, 0, 1)));
		assertEpsilonEquals(4, this.shape.getDistanceSquared(createCircle(2, 8, 1)));
		assertEpsilonEquals(2.16719, this.shape.getDistanceSquared(createCircle(-1, 16, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(-4, 17, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(6, 9.5, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createSegment(0, 0, 1, 1)));
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createSegment(1, 0, 0, 1)));
		assertClosestPointInBothShapes(this.shape, createSegment(8, 8, 4, 10));
		assertFpPointEquals(5, 9, this.shape.getClosestPointTo(createSegment(0, 12, 4, 10)));
		assertFpPointEquals(-3, 18, this.shape.getClosestPointTo(createSegment(0, 12, -2, 18)));
		assertClosestPointInBothShapes(this.shape, createSegment(-6, 20, -2, 18));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(65, this.shape.getDistanceSquared(createSegment(0, 0, 1, 1)));
		assertEpsilonEquals(74, this.shape.getDistanceSquared(createSegment(1, 0, 0, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(8, 8, 4, 10)));
		assertEpsilonEquals(2, this.shape.getDistanceSquared(createSegment(0, 12, 4, 10)));
		assertEpsilonEquals(1, this.shape.getDistanceSquared(createSegment(0, 12, -2, 18)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(-6, 20, -2, 18)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createRectangle(0, 0, 1, 1)));
		assertFpPointEquals(6.5, 9, this.shape.getClosestPointTo(createRectangle(6, 12, 1, 1)));
		assertFpPointEquals(-4.36754, 16.10263, this.shape.getClosestPointTo(createRectangle(-4, 14, 1, 1)));
		assertClosestPointInBothShapes(this.shape, createRectangle(-6, 18, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(5.9, 8.5, 1, 1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(65, this.shape.getDistanceSquared(createRectangle(0, 0, 1, 1)));
		assertEpsilonEquals(9, this.shape.getDistanceSquared(createRectangle(6, 12, 1, 1)));
		assertEpsilonEquals(1.35089, this.shape.getDistanceSquared(createRectangle(-4, 14, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(-6, 18, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(5.9, 8.5, 1, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createRoundRectangle(0, 0, 1, 1, .1, .1)));
		assertFpPointEquals(6.5, 9, this.shape.getClosestPointTo(createRoundRectangle(6, 12, 1, 1, .1, .1)));
		assertFpPointEquals(-4.33118, 16.11514, this.shape.getClosestPointTo(createRoundRectangle(-4, 14, 1, 1, .1, .1)));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(-6, 18, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(5.9, 8.5, 1, 1, .1, .1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(65.59024, this.shape.getDistanceSquared(createRoundRectangle(0, 0, 1, 1, .1, .1)));
		assertEpsilonEquals(9, this.shape.getDistanceSquared(createRoundRectangle(6, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(1.41462, this.shape.getDistanceSquared(createRoundRectangle(-4, 14, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(-6, 18, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(5.9, 8.5, 1, 1, .1, .1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createEllipse(0, 0, 2, 1)));
		assertFpPointEquals(7, 9, this.shape.getClosestPointTo(createEllipse(6, 12, 2, 1)));
		assertFpPointEquals(-4.20901390964965, 16.16306205742484, this.shape.getClosestPointTo(createEllipse(-4, 14, 2, 1)));
		assertClosestPointInBothShapes(this.shape, createEllipse(-6, 18, 2, 1));
		assertClosestPointInBothShapes(this.shape, createEllipse(5.9, 8.5, 2, 1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(61.90769, this.shape.getDistanceSquared(createEllipse(0, 0, 2, 1)));
		assertEpsilonEquals(9, this.shape.getDistanceSquared(createEllipse(6, 12, 2, 1)));
		assertEpsilonEquals(1.95879649834575, this.shape.getDistanceSquared(createEllipse(-4, 14, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(-6, 18, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(5.9, 8.5, 2, 1)));
	}

	protected Triangle2afp createTestTriangle(double dx, double dy) {
		return createTriangle(dx, dy, dx + 5, dy + 2, dx - 2, dy + 3);
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createTestTriangle(0, 0)));
		assertFpPointEquals(5, 9, this.shape.getClosestPointTo(createTestTriangle(2, 10)));
		assertClosestPointInBothShapes(this.shape, createTestTriangle(8, 6));
		assertFpPointEquals(-3.3359, 19.1094, this.shape.getClosestPointTo(createTestTriangle(0, 18)));
		assertClosestPointInBothShapes(this.shape, createTestTriangle(-10, 16));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(35.28, this.shape.getDistanceSquared(createTestTriangle(0, 0)));
		assertEpsilonEquals(4.17241, this.shape.getDistanceSquared(createTestTriangle(2, 10)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(8, 6)));
		assertEpsilonEquals(4.66669, this.shape.getDistanceSquared(createTestTriangle(0, 18)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(-10, 16)));
	}
	
	protected OrientedRectangle2afp createTestOrientedRectangle(double dx, double dy) {
		Vector2D<?, ?> u = createVector(-4, 5).toUnitVector();
		return createOrientedRectangle(dx, dy, u.getX(), u.getY(), 2, 1);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToOrientedRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createTestOrientedRectangle(0, 0)));
		assertFpPointEquals(7, 8, this.shape.getClosestPointTo(createTestOrientedRectangle(9, 6)));
		assertFpPointEquals(5, 9, this.shape.getClosestPointTo(createTestOrientedRectangle(2, 12)));
		assertFpPointEquals(-3.11699, 17.32601, this.shape.getClosestPointTo(createTestOrientedRectangle(0, 16)));
		assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(-7, 18));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredOrientedRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(63.70229, this.shape.getDistanceSquared(createTestOrientedRectangle(0, 0)));
		assertEpsilonEquals(0.65793, this.shape.getDistanceSquared(createTestOrientedRectangle(9, 6)));
		assertEpsilonEquals(4.91372, this.shape.getDistanceSquared(createTestOrientedRectangle(2, 12)));
		assertEpsilonEquals(1.33227, this.shape.getDistanceSquared(createTestOrientedRectangle(0, 16)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(-7, 18)));
	}

	protected Parallelogram2afp createTestParallelogram(double dx, double dy) {
		Vector2D<?, ?> u = createVector(-4, 5).toUnitVector();
		Vector2D<?, ?> v = createVector(1, -.5).toUnitVector();
		return createParallelogram(dx, dy, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToParallelogram2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createTestParallelogram(0, 0)));
		assertFpPointEquals(6.85618, 8, this.shape.getClosestPointTo(createTestParallelogram(9, 5)));
		assertFpPointEquals(5, 9, this.shape.getClosestPointTo(createTestParallelogram(2, 12)));
		assertFpPointEquals(-3.00001, 18.00627, this.shape.getClosestPointTo(createTestParallelogram(0, 16)));
		assertClosestPointInBothShapes(this.shape, createTestParallelogram(-7, 18));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredParallelogram2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(76.08541, this.shape.getDistanceSquared(createTestParallelogram(0, 0)));
		assertEpsilonEquals(0.98218, this.shape.getDistanceSquared(createTestParallelogram(9, 5)));
		assertEpsilonEquals(1.71523, this.shape.getDistanceSquared(createTestParallelogram(2, 12)));
		assertEpsilonEquals(0.73307, this.shape.getDistanceSquared(createTestParallelogram(0, 16)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestParallelogram(-7, 18)));
	}
	
	protected Path2afp createSimpleTestPath(double dx, double dy, boolean close) {
		Path2afp path = createPath();
		path.moveTo(dx, dy);
		path.lineTo(dx + 16, dy + 4);
		path.lineTo(dx + 14, dy + 12);
		path.lineTo(dx + 2, dy + 10);
		path.lineTo(dx - 6, dy + 20);
		path.lineTo(dx + 12, dy + 18);
		if (close) {
			path.closePath();
		}
		return path;
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToPath2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    assertClosestPointInBothShapes(this.shape, createSimpleTestPath(0, 0, false));
	    assertClosestPointInBothShapes(this.shape, createSimpleTestPath(0, 0, true));
		assertFpPointEquals(-6.56174, 16.75061, this.shape.getClosestPointTo(createSimpleTestPath(-6, 2, false)));
		assertClosestPointInBothShapes(this.shape, createSimpleTestPath(-6, 2, true));
		assertFpPointEquals(-3.8906, 16.3359, this.shape.getClosestPointTo(createSimpleTestPath(-3, 15, false)));
		assertFpPointEquals(-3.3359, 16.8906, this.shape.getClosestPointTo(createSimpleTestPath(-3, 15, true)));
	}
		
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredPath2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSimpleTestPath(0, 0, false)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSimpleTestPath(0, 0, true)));
		assertEpsilonEquals(0.93567, this.shape.getDistanceSquared(createSimpleTestPath(-6, 2, false)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSimpleTestPath(-6, 2, true)));
		assertEpsilonEquals(2.57779, this.shape.getDistanceSquared(createSimpleTestPath(-3, 15, false)));
		assertEpsilonEquals(1.76412, this.shape.getDistanceSquared(createSimpleTestPath(-3, 15, true)));
	}

	protected MultiShape2afp createTestMultiShape(double dx, double dy) {
		MultiShape2afp shape = createMultiShape();
		shape.add(createCircle(dx - 1, dy + 5, .5));
		shape.add(createCircle(dx + 3, dy - 1, 1));
		return shape;
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToMultiShape2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 9, this.shape.getClosestPointTo(createTestMultiShape(5, 5)));
		assertFpPointEquals(-3.21115, 18.89443, this.shape.getClosestPointTo(createTestMultiShape(0, 15)));
		assertClosestPointInBothShapes(this.shape, createTestMultiShape(-10, 20));
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredMultiShape2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0.83579, this.shape.getDistanceSquared(createTestMultiShape(5, 5)));
		assertEpsilonEquals(3.88932, this.shape.getDistanceSquared(createTestMultiShape(0, 15)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(-10, 20)));
	}

}
