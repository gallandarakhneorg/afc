/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.MultiShape3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.ai.MultiShape3ai;
import org.arakhne.afc.math.geometry.d3.ai.Path3ai;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;
import org.arakhne.afc.math.geometry.d3.ai.RectangularPrism3ai;
import org.arakhne.afc.math.geometry.d3.ai.Shape3ai;

@SuppressWarnings("all")
public abstract class AbstractMultiShape3aiTest<T extends MultiShape3ai<?, T, C, ?, ?, ?, B>,
		C extends Shape3ai<?, ?, ?, ?, ?, B>,
		B extends RectangularPrism3ai<?, ?, ?, ?, ?, B>> extends AbstractShape3aiTest<T, B> {

	private C firstObject;
	private C secondObject;
	
	@Override
	protected final T createShape() {
		T shape = (T) createMultiShape();
		this.firstObject = (C) createRectangularPrism(5, 8, 0, 2, 1, 0);
		this.secondObject = (C) createSphere(-5, 18, 0, 2);
		shape.add(this.firstObject);
		shape.add(this.secondObject);
		return shape;
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void testClone(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		MultiShape3ai clone = this.shape.clone();
		PathIterator3ai pi = (PathIterator3ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 9, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 9, 0);
		assertElement(pi, PathElementType.CLOSE, 5, 8, 0);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19, 0, -3, 20, 0, -5, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -6, 20, 0, -7, 19, 0, -7, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16, 0, -6, 16, 0, -5, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3, 16, 0, -3, 16, 0, -3, 18, 0);
		assertElement(pi, PathElementType.CLOSE, -3, 18, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void equalsObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createMultiShape()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 5, 10, 0)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(this.shape.clone()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void equalsObject_withPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createMultiShape().getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 5, 10, 0).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(this.shape.clone().getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void equalsToPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createMultiShape().getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 0, 5, 10, 0).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.clone().getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void equalsToShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createMultiShape()));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape(this.shape.clone()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void isEmpty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void clear(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		PathIterator3ai pi = this.shape.getPathIterator();
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void containsIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
		assertTrue(this.shape.contains(6, 8, 0));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void containsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
		assertTrue(this.shape.contains(createPoint(6, 8, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void containsRectangularPrism3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		// Outside
		assertFalse(this.shape.contains(createRectangularPrism(-20, 14, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(-2,-10, 0, 1, 1, 0)));
		// Intersecting
		assertFalse(this.shape.contains(createRectangularPrism(-6,16, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(4, 8, 0, 1, 1, 0)));
		// Inside
		assertTrue(this.shape.contains(createRectangularPrism(5, 8, 0, 1, 1, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getClosestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;
		// Outside bounding box
		result = this.shape.getClosestPointTo(createPoint(-10, 2, 0));
		assertEpsilonEquals(-6, result.ix());
		assertEpsilonEquals(16, result.iy());
		result = this.shape.getClosestPointTo(createPoint(-10, 14, 0));
		assertEpsilonEquals(-6, result.ix());
		assertEpsilonEquals(16, result.iy());
		result = this.shape.getClosestPointTo(createPoint(-10, 25, 0));
		assertEpsilonEquals(-6, result.ix());
		assertEpsilonEquals(20, result.iy());
		result = this.shape.getClosestPointTo(createPoint(-1, 25, 0));
		assertEpsilonEquals(-4, result.ix());
		assertEpsilonEquals(20, result.iy());
		result = this.shape.getClosestPointTo(createPoint(1, 2, 0));
		assertEpsilonEquals(5, result.ix());
		assertEpsilonEquals(8, result.iy());
		result = this.shape.getClosestPointTo(createPoint(12, 2, 0));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(8, result.iy());
		result = this.shape.getClosestPointTo(createPoint(12, 14, 0));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(9, result.iy());
		result = this.shape.getClosestPointTo(createPoint(12, 25, 0));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(9, result.iy());
		// Inside bounding box - outside subshape
		result = this.shape.getClosestPointTo(createPoint(-6, 8, 0));
		assertEpsilonEquals(-6, result.ix());
		assertEpsilonEquals(16, result.iy());
		result = this.shape.getClosestPointTo(createPoint(4, 17, 0));
		assertEpsilonEquals(-3, result.ix());
		assertEpsilonEquals(17, result.iy());
		// Inside circle
		result = this.shape.getClosestPointTo(createPoint(-4, 19, 0));
		assertEpsilonEquals(-4, result.ix());
		assertEpsilonEquals(19, result.iy());
		// Inside rectangle
		result = this.shape.getClosestPointTo(createPoint(6, 8, 0));
		assertEpsilonEquals(6, result.ix());
		assertEpsilonEquals(8, result.iy());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;
		// Outside bounding box
		result = this.shape.getFarthestPointTo(createPoint(-10, 2, 0));
		assertEpsilonEquals(-4, result.ix());
		assertEpsilonEquals(20, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(-10, 14, 0));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(8, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(-10, 25, 0));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(8, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(-1, 25, 0));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(8, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(1, 2, 0));
		assertEpsilonEquals(-6, result.ix());
		assertEpsilonEquals(20, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(12, 2, 0));
		assertEpsilonEquals(-7, result.ix());
		assertEpsilonEquals(19, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(12, 14, 0));
		assertEpsilonEquals(-7, result.ix());
		assertEpsilonEquals(19, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(12, 25, 0));
		assertEpsilonEquals(-7, result.ix());
		assertEpsilonEquals(17, result.iy());
		// Inside bounding box - outside subshape
		result = this.shape.getFarthestPointTo(createPoint(-6, 8, 0));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(9, result.iy());
		result = this.shape.getFarthestPointTo(createPoint(4, 17, 0));
		assertEpsilonEquals(-7, result.ix());
		assertEpsilonEquals(19, result.iy());
		// Inside circle
		result = this.shape.getFarthestPointTo(createPoint(-4, 19, 0));
		assertEpsilonEquals(7, result.ix());
		assertEpsilonEquals(8, result.iy());
		// Inside rectangle
		result = this.shape.getFarthestPointTo(createPoint(6, 8, 0));
		assertEpsilonEquals(-7, result.ix());
		assertEpsilonEquals(19, result.iy());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		// Outside bounding box
		assertEpsilonEquals(14.5602, this.shape.getDistance(createPoint(-10, 2, 0)));
		assertEpsilonEquals(4.4721, this.shape.getDistance(createPoint(-10, 14, 0)));
		assertEpsilonEquals(6.4031, this.shape.getDistance(createPoint(-10, 25, 0)));
		assertEpsilonEquals(5.831, this.shape.getDistance(createPoint(-1, 25, 0)));
		assertEpsilonEquals(7.2111, this.shape.getDistance(createPoint(1, 2, 0)));
		assertEpsilonEquals(7.8102, this.shape.getDistance(createPoint(12, 2, 0)));
		assertEpsilonEquals(7.0711, this.shape.getDistance(createPoint(12, 14, 0)));
		assertEpsilonEquals(16.7631, this.shape.getDistance(createPoint(12, 25, 0)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(8, this.shape.getDistance(createPoint(-6, 8, 0)));
		assertEpsilonEquals(7, this.shape.getDistance(createPoint(4, 17, 0)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(-4, 19, 0)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(6, 8, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		// Outside bounding box
		assertEpsilonEquals(212, this.shape.getDistanceSquared(createPoint(-10, 2, 0)));
		assertEpsilonEquals(20, this.shape.getDistanceSquared(createPoint(-10, 14, 0)));
		assertEpsilonEquals(41, this.shape.getDistanceSquared(createPoint(-10, 25, 0)));
		assertEpsilonEquals(34, this.shape.getDistanceSquared(createPoint(-1, 25, 0)));
		assertEpsilonEquals(52, this.shape.getDistanceSquared(createPoint(1, 2, 0)));
		assertEpsilonEquals(61, this.shape.getDistanceSquared(createPoint(12, 2, 0)));
		assertEpsilonEquals(50, this.shape.getDistanceSquared(createPoint(12, 14, 0)));
		assertEpsilonEquals(281, this.shape.getDistanceSquared(createPoint(12, 25, 0)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(64, this.shape.getDistanceSquared(createPoint(-6, 8, 0)));
		assertEpsilonEquals(49, this.shape.getDistanceSquared(createPoint(4, 17, 0)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(-4, 19, 0)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(6, 8, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceL1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		// Outside bounding box
		assertEpsilonEquals(18, this.shape.getDistanceL1(createPoint(-10, 2, 0)));
		assertEpsilonEquals(6, this.shape.getDistanceL1(createPoint(-10, 14, 0)));
		assertEpsilonEquals(9, this.shape.getDistanceL1(createPoint(-10, 25, 0)));
		assertEpsilonEquals(8, this.shape.getDistanceL1(createPoint(-1, 25, 0)));
		assertEpsilonEquals(10, this.shape.getDistanceL1(createPoint(1, 2, 0)));
		assertEpsilonEquals(11, this.shape.getDistanceL1(createPoint(12, 2, 0)));
		assertEpsilonEquals(10, this.shape.getDistanceL1(createPoint(12, 14, 0)));
		assertEpsilonEquals(21, this.shape.getDistanceL1(createPoint(12, 25, 0)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(8, this.shape.getDistanceL1(createPoint(-6, 8, 0)));
		assertEpsilonEquals(7, this.shape.getDistanceL1(createPoint(4, 17, 0)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(-4, 19, 0)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(6, 8, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceLinf(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		// Outside bounding box
		assertEpsilonEquals(14, this.shape.getDistanceLinf(createPoint(-10, 2, 0)));
		assertEpsilonEquals(4, this.shape.getDistanceLinf(createPoint(-10, 14, 0)));
		assertEpsilonEquals(5, this.shape.getDistanceLinf(createPoint(-10, 25, 0)));
		assertEpsilonEquals(5, this.shape.getDistanceLinf(createPoint(-1, 25, 0)));
		assertEpsilonEquals(6, this.shape.getDistanceLinf(createPoint(1, 2, 0)));
		assertEpsilonEquals(6, this.shape.getDistanceLinf(createPoint(12, 2, 0)));
		assertEpsilonEquals(5, this.shape.getDistanceLinf(createPoint(12, 14, 0)));
		assertEpsilonEquals(16, this.shape.getDistanceLinf(createPoint(12, 25, 0)));
		// Inside bounding box - outside subshape
		assertEpsilonEquals(8, this.shape.getDistanceLinf(createPoint(-6, 8, 0)));
		assertEpsilonEquals(7, this.shape.getDistanceLinf(createPoint(4, 17, 0)));
		// Inside circle
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(-4, 19, 0)));
		// Inside rectangle
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(6, 8, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setIT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createMultiShape());
		PathIterator3ai pi = this.shape.getPathIterator();
		assertNoElement(pi);
		MultiShape3ai newShape = createMultiShape();
		newShape.add(createRectangularPrism(-6, 48, 0, 5, 7, 0));
		this.shape.set((T) newShape);
		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -6, 48, 0);
		assertElement(pi, PathElementType.LINE_TO, -1, 48, 0);
		assertElement(pi, PathElementType.LINE_TO, -1, 55, 0);
		assertElement(pi, PathElementType.LINE_TO, -6, 55, 0);
		assertElement(pi, PathElementType.CLOSE, -6, 48, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void translateIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(10, -2, 0);
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 7, 0);
		assertElement(pi, PathElementType.LINE_TO, 15, 7, 0);
		assertElement(pi, PathElementType.CLOSE, 15, 6, 0);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17, 0, 6, 18, 0, 5, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 18, 0, 3, 17, 0, 3, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14, 0, 3, 14, 0, 5, 14, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6, 14, 0, 7, 14, 0, 7, 16, 0);
		assertElement(pi, PathElementType.CLOSE, 7, 16, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(10, -2, 0));
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 7, 0);
		assertElement(pi, PathElementType.LINE_TO, 15, 7, 0);
		assertElement(pi, PathElementType.CLOSE, 15, 6, 0);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17, 0, 6, 18, 0, 5, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 18, 0, 3, 17, 0, 3, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14, 0, 3, 14, 0, 5, 14, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6, 14, 0, 7, 14, 0, 7, 16, 0);
		assertElement(pi, PathElementType.CLOSE, 7, 16, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void toBoundingBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void toBoundingBoxB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = createRectangularPrism(0, 0, 0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 9, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 9, 0);
		assertElement(pi, PathElementType.CLOSE, 5, 8, 0);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19, 0, -3, 20, 0, -5, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -6, 20, 0, -7, 19, 0, -7, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16, 0, -6, 16, 0, -5, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3, 16, 0, -3, 16, 0, -3, 18, 0);
		assertElement(pi, PathElementType.CLOSE, -3, 18, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getPathIteratorTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3ai pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 5, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 9, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 9, 0);
		assertElement(pi, PathElementType.CLOSE, 5, 8, 0);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19, 0, -3, 20, 0, -5, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -6, 20, 0, -7, 19, 0, -7, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16, 0, -6, 16, 0, -5, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3, 16, 0, -3, 16, 0, -3, 18, 0);
		assertElement(pi, PathElementType.CLOSE, -3, 18, 0);
		assertNoElement(pi);

		pi = this.shape.getPathIterator(new Transform3D());
		assertElement(pi, PathElementType.MOVE_TO, 5, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, 9, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 9, 0);
		assertElement(pi, PathElementType.CLOSE, 5, 8, 0);
		assertElement(pi, PathElementType.MOVE_TO, -3, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3, 19, 0, -3, 20, 0, -5, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -6, 20, 0, -7, 19, 0, -7, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -7, 16, 0, -6, 16, 0, -5, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, -3, 16, 0, -3, 16, 0, -3, 18, 0);
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
		assertElement(pi, PathElementType.CURVE_TO, 7, 17, 0, 6, 18, 0, 5, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, 4, 18, 0, 3, 17, 0, 3, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 15, 0, 4, 14, 0, 5, 14, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6, 14, 0, 7, 15, 0, 7, 16, 0);
		assertElement(pi, PathElementType.CLOSE, 7, 16, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void createTransformedShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D transform = new Transform3D();
		transform.setTranslation(10, -2, 0);
		Shape3ai newShape = this.shape.createTransformedShape(transform);
		PathIterator3ai pi = (PathIterator3ai) newShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 7, 0);
		assertElement(pi, PathElementType.LINE_TO, 15, 7, 0);
		assertElement(pi, PathElementType.CLOSE, 15, 6, 0);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17, 0, 6, 18, 0, 5, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, 4, 18, 0, 3, 17, 0, 3, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 15, 0, 4, 14, 0, 5, 14, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6, 14, 0, 7, 15, 0, 7, 16, 0);
		assertElement(pi, PathElementType.CLOSE, 7, 16, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsRectangularPrism3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		// Outside
		assertFalse(this.shape.intersects(createRectangularPrism(-20, 14, 0, 1, 1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(-2, -10, 0, 1, 1, 0)));
		// Intersecting
		assertTrue(this.shape.intersects(createRectangularPrism(-6, 16, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(4, 8, 0, 2, 2, 0)));
		// Inside
		assertTrue(this.shape.intersects(createRectangularPrism(-4, 18, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(5, 8, 0, 1, 1, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSphere3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		// Outside
		assertFalse(this.shape.intersects(createSphere(-20, 14, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(-2,- 10, 0, 1)));
		// Intersecting
		assertTrue(this.shape.intersects(createSphere(-6, 16, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(4, 8, 0, 1)));
		// Inside
		assertTrue(this.shape.intersects(createSphere(-4, 18, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(5, 8, 0, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSegment3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		// Outside
		assertFalse(this.shape.intersects(createSegment(-20, 14, 0, -19, 14, 0)));
		assertFalse(this.shape.intersects(createSegment(-2, -10, 0, -1, -10, 0)));
		// Intersecting
		assertTrue(this.shape.intersects(createSegment(-6, 16, 0, -5, 16, 0)));
		assertTrue(this.shape.intersects(createSegment(4, 8, 0, 5, 8, 0)));
		// Inside
		assertTrue(this.shape.intersects(createSegment(-4, 18, 0, -3, 18, 0)));
		assertTrue(this.shape.intersects(createSegment(5, 8, 0, 6, 8, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsPath3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai path = createPath();
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsPathIterator3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai path = createPath();
		path.moveTo(-4, 3, 0);
		path.lineTo(9, 6, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-9, 11, 0);
		path.lineTo(-8, 21, 0);
		path.lineTo(4, 21, 0);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		
		path = createPath();
		path.moveTo(-6, 2, 0);
		path.lineTo(10, 6, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-4, 12, 0);
		path.lineTo(6, 20, 0);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));

		path = createPath();
		path.moveTo(-6, 2, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-4, 12, 0);
		path.lineTo(-12, 22, 0);
		path.lineTo(6, 20, 0);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(10, -2, 0));
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 7, 0);
		assertElement(pi, PathElementType.LINE_TO, 15, 7, 0);
		assertElement(pi, PathElementType.CLOSE, 15, 6, 0);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17, 0, 6, 18, 0, 5, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 18, 0, 3, 17, 0, 3, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14, 0, 3, 14, 0, 5, 14, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6, 14, 0, 7, 14, 0, 7, 16, 0);
		assertElement(pi, PathElementType.CLOSE, 7, 16, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_plus(createVector(10, -2, 0));
		PathIterator3ai pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 7, 0);
		assertElement(pi, PathElementType.LINE_TO, 15, 7, 0);
		assertElement(pi, PathElementType.CLOSE, 15, 6, 0);
		assertElement(pi, PathElementType.MOVE_TO, 7, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 7, 17, 0, 6, 18, 0, 5, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 18, 0, 3, 17, 0, 3, 16, 0);
		assertElement(pi, PathElementType.CURVE_TO, 3, 14, 0, 3, 14, 0, 5, 14, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6, 14, 0, 7, 14, 0, 7, 16, 0);
		assertElement(pi, PathElementType.CLOSE, 7, 16, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(10, -2, 0));
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -5, 10, 0);
		assertElement(pi, PathElementType.LINE_TO, -3, 10, 0);
		assertElement(pi, PathElementType.LINE_TO, -3, 11, 0);
		assertElement(pi, PathElementType.LINE_TO, -5, 11, 0);
		assertElement(pi, PathElementType.CLOSE, -5, 10, 0);
		assertElement(pi, PathElementType.MOVE_TO, -13, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -13, 21, 0, -13, 22, 0, -15, 22, 0);
		assertElement(pi, PathElementType.CURVE_TO, -16, 22, 0, -17, 21, 0, -17, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -17, 18, 0, -16, 18, 0, -15, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -13, 18, 0, -13, 18, 0, -13, 20, 0);
		assertElement(pi, PathElementType.CLOSE, -13, 20, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_minus(createVector(10, -2, 0));
		PathIterator3ai pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -5, 10, 0);
		assertElement(pi, PathElementType.LINE_TO, -3, 10, 0);
		assertElement(pi, PathElementType.LINE_TO, -3, 11, 0);
		assertElement(pi, PathElementType.LINE_TO, -5, 11, 0);
		assertElement(pi, PathElementType.CLOSE, -5, 10, 0);
		assertElement(pi, PathElementType.MOVE_TO, -13, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -13, 21, 0, -13, 22, 0, -15, 22, 0);
		assertElement(pi, PathElementType.CURVE_TO, -16, 22, 0, -17, 21, 0, -17, 20, 0);
		assertElement(pi, PathElementType.CURVE_TO, -17, 18, 0, -16, 18, 0, -15, 18, 0);
		assertElement(pi, PathElementType.CURVE_TO, -13, 18, -13, 18, -13, 20, 0);
		assertElement(pi, PathElementType.CLOSE, -13, 20);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_multiplyTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D transform = new Transform3D();
		transform.setTranslation(10, -2, 0);
		Shape3ai newShape = this.shape.operator_multiply(transform);
		PathIterator3ai pi = (PathIterator3ai) newShape.getPathIterator();
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
	@EnumSource(CoordinateSystem3D.class)
	public void operator_andPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
		assertTrue(this.shape.operator_and(createPoint(6, 8, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createSphere(-6, 16, 0, 1)));
		assertTrue(this.shape.operator_and(createRectangularPrism(-6, 16, 0, 1, 1, 0)));

	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_upToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(14.5602, this.shape.operator_upTo(createPoint(-10, 2, 0)));
		assertEpsilonEquals(4.4721, this.shape.operator_upTo(createPoint(-10, 14, 0)));
		assertEpsilonEquals(6.4031, this.shape.operator_upTo(createPoint(-10, 25, 0)));
		assertEpsilonEquals(5.831, this.shape.operator_upTo(createPoint(-1, 25, 0)));
		assertEpsilonEquals(7.2111, this.shape.operator_upTo(createPoint(1, 2, 0)));
		assertEpsilonEquals(7.8102, this.shape.operator_upTo(createPoint(12, 2, 0)));
		assertEpsilonEquals(7.0711, this.shape.operator_upTo(createPoint(12, 14, 0)));
		assertEpsilonEquals(16.7631, this.shape.operator_upTo(createPoint(12, 25, 0)));
		assertEpsilonEquals(8, this.shape.operator_upTo(createPoint(-6, 8, 0)));
		assertEpsilonEquals(7, this.shape.operator_upTo(createPoint(4, 17, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(-4, 19, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(6, 8, 0)));
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape3D) createSphere(-6, 16, 0, 1)));
		assertTrue(this.shape.intersects((Shape3D) createRectangularPrism(-6, 16, 0, 1, 1, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void getPointIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		Iterator<? extends Point3D> it = this.shape.getPointIterator();
		
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
	@EnumSource(CoordinateSystem3D.class)
	public void getFirstShapeContainingPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
		assertSame(this.secondObject, this.shape.getFirstShapeContaining(createPoint(-4, 19, 0)));
		// Inside rectangle
		assertSame(this.firstObject, this.shape.getFirstShapeContaining(createPoint(6, 8, 0)));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getShapesContainingPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
		assertEquals(Arrays.asList(this.secondObject), this.shape.getShapesContaining(createPoint(-4, 19, 0)));
		// Inside rectangle
		assertEquals(Arrays.asList(this.firstObject), this.shape.getShapesContaining(createPoint(6, 8, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getFirstShapeIntersectingShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		MultiShape3D shape2d = this.shape;
		assertNull(shape2d.getFirstShapeIntersecting(createSegment(-20, 14, 0, -19, 14, 0)));
		assertNull(shape2d.getFirstShapeIntersecting(createSegment(-2, -10, 0, -1, -10, 0)));
		assertSame(this.secondObject, shape2d.getFirstShapeIntersecting(createSegment(-6, 16, 0, -5, 16, 0)));
		assertSame(this.firstObject, shape2d.getFirstShapeIntersecting(createSegment(4, 8, 0, 5, 8, 0)));
		assertSame(this.secondObject, shape2d.getFirstShapeIntersecting(createSegment(-4, 18, 0, -3, 18, 0)));
		assertSame(this.firstObject, shape2d.getFirstShapeIntersecting(createSegment(5, 8, 0, 6, 8, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void getFirstShapeIntersectingShape3D_withOpenPath(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		MultiShape3D shape2d = this.shape;
		
		Path3ai path = createPath();
		path.moveTo(-4, 3, 0);
		path.lineTo(9, 6, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-9, 11, 0);
		path.lineTo(-8, 21, 0);
		path.lineTo(4, 21, 0);
		
		assertNull(shape2d.getFirstShapeIntersecting(path));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void getFirstShapeIntersectingShape3D_withClosePath(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		MultiShape3D shape2d = this.shape;
		
		Path3ai path = createPath();
		path.moveTo(-4, 3, 0);
		path.lineTo(9, 6, 0);
		path.lineTo(8, 14, 0);
		path.lineTo(-9, 11, 0);
		path.lineTo(-8, 21, 0);
		path.lineTo(4, 21, 0);
		path.closePath();
		
		assertSame(this.firstObject, shape2d.getFirstShapeIntersecting(path));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getShapesIntersectingShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		MultiShape3D shape2d = this.shape;
		assertTrue(shape2d.getShapesIntersecting(createSegment(-20, 14, 0, -19, 14, 0)).isEmpty());
		assertTrue(shape2d.getShapesIntersecting(createSegment(-2, -10, 0, -1, -10, 0)).isEmpty());
		assertEquals(Arrays.asList(this.secondObject), shape2d.getShapesIntersecting(createSegment(-6, 16, 0, -5, 16, 0)));
		assertEquals(Arrays.asList(this.firstObject), shape2d.getShapesIntersecting(createSegment(4, 8, 0, 5, 8, 0)));
		assertEquals(Arrays.asList(this.secondObject), shape2d.getShapesIntersecting(createSegment(-4, 18, 0, -3, 18, 0)));
		assertEquals(Arrays.asList(this.firstObject), shape2d.getShapesIntersecting(createSegment(5, 8, 0, 6, 8, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getBackendDataList(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertNotNull(this.shape.getBackendDataList());
		assertEquals(2, this.shape.getBackendDataList().size());
		assertSame(this.firstObject, this.shape.getBackendDataList().get(0));
		assertSame(this.secondObject, this.shape.getBackendDataList().get(1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void onGeometryChange_changeFirstObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		this.firstObject.translate(12, -7, 0);
		
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
	@EnumSource(CoordinateSystem3D.class)
	public void onGeometryChange_changeSecondObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		this.secondObject.translate(12, -7, 0);
		
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
	@EnumSource(CoordinateSystem3D.class)
	public void onBackendDataListChange_addition(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		this.shape.add((C) createSphere(10, 14, 0, 1));
		
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
	@EnumSource(CoordinateSystem3D.class)
	public void onBackendDataListChange_removalFirstObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
	@EnumSource(CoordinateSystem3D.class)
	public void onBackendDataListChange_removalSecondObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
	@EnumSource(CoordinateSystem3D.class)
	public void noGeometryChangeAfterRemoval(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(-7, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(20, box.getMaxY());

		this.shape.remove(this.secondObject);
		this.secondObject.translate(1453,  -451, 0);
		
		box = this.shape.toBoundingBox();
		assertNotNull(box);
		assertEquals(5, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(9, box.getMaxY());
	}

}
