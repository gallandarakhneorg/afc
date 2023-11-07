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

package org.arakhne.afc.math.test.geometry.d2.afp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Ellipse2afp;
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
public abstract class AbstractEllipse2afpTest<T extends Ellipse2afp<?, T, ?, ?, ?, B>,
		B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractShape2afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createEllipse(5, 8, 5, 10);
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
		assertEpsilonEquals(5, clone.getMinX());
		assertEpsilonEquals(8, clone.getMinY());
		assertEpsilonEquals(10, clone.getMaxX());
		assertEpsilonEquals(18, clone.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsObject(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createEllipse(0, 0, 5, 10)));
		assertFalse(this.shape.equals(createEllipse(5, 8, 6, 10)));
		assertFalse(this.shape.equals(createSegment(5, 8, 6, 10)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createEllipse(5, 8, 5, 10)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsObject_withPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createEllipse(0, 0, 5, 10).getPathIterator()));
		assertFalse(this.shape.equals(createEllipse(5, 8, 6, 10).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 6, 10).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createEllipse(5, 8, 5, 10).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsToPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
		assertFalse(this.shape.equalsToPathIterator(createEllipse(0, 0, 5, 10).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createEllipse(5, 8, 6, 10).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 6, 10).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createEllipse(5, 8, 5, 10).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsToShape(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createEllipse(0, 0, 5, 10)));
		assertFalse(this.shape.equalsToShape((T) createEllipse(5, 8, 6, 10)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createEllipse(5, 8, 5, 10)));
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
		assertEpsilonEquals(0, this.shape.getMinX());
		assertEpsilonEquals(0, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMaxX());
		assertEpsilonEquals(0, this.shape.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(0,0));
		assertFalse(this.shape.contains(11,10));
		assertFalse(this.shape.contains(11,50));
		assertTrue(this.shape.contains(9,12));
		assertTrue(this.shape.contains(9,11));
		assertTrue(this.shape.contains(8,12));
		assertFalse(this.shape.contains(3,7));
		assertFalse(this.shape.contains(10,12));
		assertFalse(this.shape.contains(10,11));
		assertTrue(this.shape.contains(9,10));
		assertFalse(this.shape.contains(9.5,9.5));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createPoint(0,0)));
		assertFalse(this.shape.contains(createPoint(11,10)));
		assertFalse(this.shape.contains(createPoint(11,50)));
		assertTrue(this.shape.contains(createPoint(9,12)));
		assertTrue(this.shape.contains(createPoint(9,11)));
		assertTrue(this.shape.contains(createPoint(8,12)));
		assertFalse(this.shape.contains(createPoint(3,7)));
		assertFalse(this.shape.contains(createPoint(10,12)));
		assertFalse(this.shape.contains(createPoint(10,11)));
		assertTrue(this.shape.contains(createPoint(9,10)));
		assertFalse(this.shape.contains(createPoint(9.5,9.5)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Values computed with GeoGebra tool
		Point2D result;
		
		// Lower / Lower
		result = this.shape.getClosestPointTo(createPoint(0, 0));
		assertFpPointEquals(6.58303, 8.34848, result);

		// Lower / Upper
		result = this.shape.getClosestPointTo(createPoint(0, 24));
		assertFpPointEquals(6.39777, 17.4878, result);

		// Upper / Lower
		result = this.shape.getClosestPointTo(createPoint(24, 0));
		assertFpPointEquals(9.08189, 9.12824, result);

		// Upper / Upper
		result = this.shape.getClosestPointTo(createPoint(24, 24));
		assertFpPointEquals(9.2587, 16.55357, result);

		// On x axis (positive)
		result = this.shape.getClosestPointTo(createPoint(18, 13));
		assertFpPointEquals(10, 13, result);

		// On x axis (negative)
		result = this.shape.getClosestPointTo(createPoint(0, 13));
		assertFpPointEquals(5, 13, result);

		// On y axis (positive)
		result = this.shape.getClosestPointTo(createPoint(7.5, 24));
		assertFpPointEquals(7.5, 18, result);

		// On y axis (negative)
		result = this.shape.getClosestPointTo(createPoint(7.5, 0));
		assertFpPointEquals(7.5, 8, result);

		// Inside
		result = this.shape.getClosestPointTo(createPoint(6, 11));
		assertFpPointEquals(6, 11, result);
		
		//
		result = this.shape.getClosestPointTo(createPoint(10, 10));
		assertFpPointEquals(9.55546, 10.15389, result);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFarthestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Values computed with GeoGebra tool
		Point2D result;
		
		// Lower / Lower
		result = this.shape.getFarthestPointTo(createPoint(0, 0));
		assertFpPointEquals(8.05329, 17.92645, result);

		// Lower / Upper
		result = this.shape.getFarthestPointTo(createPoint(0, 24));
		assertFpPointEquals(8.12711, 8.08913, result);

		// Upper / Lower
		result = this.shape.getFarthestPointTo(createPoint(24, 0));
		assertFpPointEquals(6.31519, 17.75919, result);

		// Upper / Upper
		result = this.shape.getFarthestPointTo(createPoint(24, 24));
		assertFpPointEquals(6.16141, 8.28223, result);

		// On x axis (positive)
		result = this.shape.getFarthestPointTo(createPoint(18, 13));
		assertFpPointEquals(5, 13, result);

		// On x axis (negative)
		result = this.shape.getFarthestPointTo(createPoint(0, 13));
		assertFpPointEquals(10, 13, result);

		// On y axis (positive)
		result = this.shape.getFarthestPointTo(createPoint(7.5, 24));
		assertFpPointEquals(7.5, 8, result);

		// On y axis (negative)
		result = this.shape.getFarthestPointTo(createPoint(7.5, 0));
		assertFpPointEquals(7.5, 18, result);

		// Inside
		result = this.shape.getFarthestPointTo(createPoint(6, 11));
		assertFpPointEquals(7.82555, 17.97659, result);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistance(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Values computed with GeoGebra tool

		// Lower / Lower
		assertEpsilonEquals(10.63171, this.shape.getDistance(createPoint(0, 0)));

		// Lower / Upper
		assertEpsilonEquals(9.12909, this.shape.getDistance(createPoint(0, 24)));

		// Upper / Lower
		assertEpsilonEquals(17.48928, this.shape.getDistance(createPoint(24, 0)));

		// Upper / Upper
		assertEpsilonEquals(16.5153, this.shape.getDistance(createPoint(24, 24)));

		// On x axis (positive)
		assertEpsilonEquals(8, this.shape.getDistance(createPoint(18, 13)));

		// On x axis (negative)
		assertEpsilonEquals(5, this.shape.getDistance(createPoint(0, 13)));

		// On y axis (positive)
		assertEpsilonEquals(6, this.shape.getDistance(createPoint(7.5, 24)));

		// On y axis (negative)
		assertEpsilonEquals(8, this.shape.getDistance(createPoint(7.5, 0)));

		// Inside
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(6, 11)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquared(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Values computed with GeoGebra tool

		// Lower / Lower
		assertEpsilonEquals(113.03335, this.shape.getDistanceSquared(createPoint(0, 0)));

		// Lower / Upper
		assertEpsilonEquals(83.34011, this.shape.getDistanceSquared(createPoint(0, 24)));

		// Upper / Lower
		assertEpsilonEquals(305.87484, this.shape.getDistanceSquared(createPoint(24, 0)));

		// Upper / Upper
		assertEpsilonEquals(272.75517, this.shape.getDistanceSquared(createPoint(24, 24)));

		// On x axis (positive)
		assertEpsilonEquals(64, this.shape.getDistanceSquared(createPoint(18, 13)));

		// On x axis (negative)
		assertEpsilonEquals(25, this.shape.getDistanceSquared(createPoint(0, 13)));

		// On y axis (positive)
		assertEpsilonEquals(36, this.shape.getDistanceSquared(createPoint(7.5, 24)));

		// On y axis (negative)
		assertEpsilonEquals(64, this.shape.getDistanceSquared(createPoint(7.5, 0)));

		// Inside
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(6, 11)));
		
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceL1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Values computed with GeoGebra tool

		// Lower / Lower
		assertEpsilonEquals(14.93151, this.shape.getDistanceL1(createPoint(0, 0)));

		// Lower / Upper
		assertEpsilonEquals(12.90997, this.shape.getDistanceL1(createPoint(0, 24)));

		// Upper / Lower
		assertEpsilonEquals(24.04635, this.shape.getDistanceL1(createPoint(24, 0)));

		// Upper / Upper
		assertEpsilonEquals(22.18773, this.shape.getDistanceL1(createPoint(24, 24)));

		// On x axis (positive)
		assertEpsilonEquals(8, this.shape.getDistanceL1(createPoint(18, 13)));

		// On x axis (negative)
		assertEpsilonEquals(5, this.shape.getDistanceL1(createPoint(0, 13)));

		// On y axis (positive)
		assertEpsilonEquals(6, this.shape.getDistanceL1(createPoint(7.5, 24)));

		// On y axis (negative)
		assertEpsilonEquals(8, this.shape.getDistanceL1(createPoint(7.5, 0)));

		// Inside
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(6, 11)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceLinf(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Values computed with GeoGebra tool

		// Lower / Lower
		assertEpsilonEquals(8.34848, this.shape.getDistanceLinf(createPoint(0, 0)));

		// Lower / Upper
		assertEpsilonEquals(6.5122, this.shape.getDistanceLinf(createPoint(0, 24)));

		// Upper / Lower
		assertEpsilonEquals(14.91811, this.shape.getDistanceLinf(createPoint(24, 0)));

		// Upper / Upper
		assertEpsilonEquals(14.7413, this.shape.getDistanceLinf(createPoint(24, 24)));

		// On x axis (positive)
		assertEpsilonEquals(8, this.shape.getDistanceLinf(createPoint(18, 13)));

		// On x axis (negative)
		assertEpsilonEquals(5, this.shape.getDistanceLinf(createPoint(0, 13)));

		// On y axis (positive)
		assertEpsilonEquals(6, this.shape.getDistanceLinf(createPoint(7.5, 24)));

		// On y axis (negative)
		assertEpsilonEquals(8, this.shape.getDistanceLinf(createPoint(7.5, 0)));

		// Inside
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(6, 11)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setIT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createEllipse(17, 20, 7, 45));
		assertEpsilonEquals(17, this.shape.getMinX());
		assertEpsilonEquals(20, this.shape.getMinY());
		assertEpsilonEquals(24, this.shape.getMaxX());
		assertEpsilonEquals(65, this.shape.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 10, 13);
		assertElement(pi, PathElementType.CURVE_TO, 10, 15.76142374915397, 8.880711874576983, 18, 7.5, 18);
		assertElement(pi, PathElementType.CURVE_TO, 6.119288125423017, 18, 5, 15.76142374915397, 5, 13);
		assertElement(pi, PathElementType.CURVE_TO, 5, 10.23857625084603, 6.119288125423017, 8, 7.5, 8);
		assertElement(pi, PathElementType.CURVE_TO, 8.880711874576983, 8, 10, 10.23857625084603, 10, 13);
		assertElement(pi, PathElementType.CLOSE, 10, 13);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIteratorTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 10, 13);
		assertElement(pi, PathElementType.CURVE_TO, 10, 15.76142374915397, 8.880711874576983, 18, 7.5, 18);
		assertElement(pi, PathElementType.CURVE_TO, 6.119288125423017, 18, 5, 15.76142374915397, 5, 13);
		assertElement(pi, PathElementType.CURVE_TO, 5, 10.23857625084603, 6.119288125423017, 8, 7.5, 8);
		assertElement(pi, PathElementType.CURVE_TO, 8.880711874576983, 8, 10, 10.23857625084603, 10, 13);
		assertElement(pi, PathElementType.CLOSE, 10, 13);
		assertNoElement(pi);

		Transform2D tr;
		
		tr = new Transform2D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 10, 13);
		assertElement(pi, PathElementType.CURVE_TO, 10, 15.76142374915397, 8.880711874576983, 18, 7.5, 18);
		assertElement(pi, PathElementType.CURVE_TO, 6.119288125423017, 18, 5, 15.76142374915397, 5, 13);
		assertElement(pi, PathElementType.CURVE_TO, 5, 10.23857625084603, 6.119288125423017, 8, 7.5, 8);
		assertElement(pi, PathElementType.CURVE_TO, 8.880711874576983, 8, 10, 10.23857625084603, 10, 13);
		assertElement(pi, PathElementType.CLOSE, 10, 13);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(10, -10);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 20, 3);
		assertElement(pi, PathElementType.CURVE_TO, 20, 5.76142374915397, 18.880711874576983, 8, 17.5, 8);
		assertElement(pi, PathElementType.CURVE_TO, 16.119288125423017, 8, 15, 5.76142374915397, 15, 3);
		assertElement(pi, PathElementType.CURVE_TO, 15, 0.23857625084603, 16.119288125423017, -2, 17.5, -2);
		assertElement(pi, PathElementType.CURVE_TO, 18.880711874576983, -2, 20, 0.23857625084603, 20, 3);
		assertElement(pi, PathElementType.CLOSE, 20, 3);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void createTransformedShape(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Transform2D tr;
		Shape2afp newShape;
		
		newShape = this.shape.createTransformedShape(null);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform2D();
		newShape = this.shape.createTransformedShape(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform2D();
		tr.makeTranslationMatrix(10, -10);
		newShape = this.shape.createTransformedShape(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertTrue(newShape instanceof Path2afp);
		PathIterator2afp pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 20, 3);
		assertElement(pi, PathElementType.CURVE_TO, 20, 5.76142374915397, 18.880711874576983, 8, 17.5, 8);
		assertElement(pi, PathElementType.CURVE_TO, 16.119288125423017, 8, 15, 5.76142374915397, 15, 3);
		assertElement(pi, PathElementType.CURVE_TO, 15, 0.23857625084603, 16.119288125423017, -2, 17.5, -2);
		assertElement(pi, PathElementType.CURVE_TO, 18.880711874576983, -2, 20, 0.23857625084603, 20, 3);
		assertElement(pi, PathElementType.CLOSE, 20, 3);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void translateDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.translate(123.456, -789.123);
		assertEpsilonEquals(128.456, this.shape.getMinX());
		assertEpsilonEquals(-781.123, this.shape.getMinY());
		assertEpsilonEquals(133.456, this.shape.getMaxX());
		assertEpsilonEquals(-771.123, this.shape.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void translateVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(123.456, -789.123));
		assertEpsilonEquals(128.456, this.shape.getMinX());
		assertEpsilonEquals(-781.123, this.shape.getMinY());
		assertEpsilonEquals(133.456, this.shape.getMaxX());
		assertEpsilonEquals(-771.123, this.shape.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void toBoundingBox(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(18, box.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void toBoundingBoxB(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(18, box.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createRectangle(0, 0, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(4, 6, 100, 100)));
		assertFalse(this.shape.contains(createRectangle(4, 6, 4, 6)));
		assertFalse(this.shape.contains(createRectangle(5, 8, 2.5, 5)));
		assertTrue(this.shape.contains(createRectangle(6, 10, 2, 6)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createCircle(0, 0, 1)));
		assertFalse(this.shape.contains(createCircle(4, 6, 100)));
		assertFalse(this.shape.contains(createCircle(4, 6, 4)));
		assertFalse(this.shape.contains(createCircle(5, 8, 2.5)));
		assertFalse(this.shape.contains(createCircle(6, 10, 2)));
		assertTrue(this.shape.contains(createCircle(7.4, 11, 2)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createRectangle(0, 0, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(4, 6, 100, 100)));
		assertTrue(this.shape.intersects(createRectangle(4, 6, 4, 6)));
		assertTrue(this.shape.intersects(createRectangle(5, 8, 2.5, 5)));
		assertTrue(this.shape.intersects(createRectangle(6, 10, 2, 6)));
		assertFalse(this.shape.intersects(createRectangle(5, 8, .1, .1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createCircle(0, 0, 1)));
		assertTrue(this.shape.intersects(createCircle(7.5, 7, 2)));
		assertFalse(this.shape.intersects(createCircle(3, 8, 2)));
		assertFalse(this.shape.intersects(createCircle(4, 8, 2)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createEllipse(0, 0, 1, 2)));
		assertFalse(this.shape.intersects(createEllipse(100, 0, 1, 2)));
		assertFalse(this.shape.intersects(createEllipse(100, 100, 1, 2)));
		assertFalse(this.shape.intersects(createEllipse(0, 100, 1, 2)));
		assertFalse(this.shape.intersects(createEllipse(0, 8, 5, 10)));
		assertTrue(this.shape.intersects(createEllipse(0.1, 8, 5, 10)));
		assertTrue(this.shape.intersects(createEllipse(8, 10, .5, .2)));
		assertFalse(this.shape.intersects(createEllipse(2, 6, 4, 2)));
		assertTrue(this.shape.intersects(createEllipse(2.5, 3, 5, 10)));
		assertFalse(this.shape.intersects(createEllipse(1, -1, 5, 10)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createSegment(5, -4, -1, -5)));
		assertFalse(this.shape.intersects(createSegment(5, -4, 7, 6)));
		assertFalse(this.shape.intersects(createSegment(5, -4, 14, 13)));
		assertFalse(this.shape.intersects(createSegment(5, -4, 11, 13)));
		assertTrue(this.shape.intersects(createSegment(5, -4, 11, 18)));
		assertFalse(this.shape.intersects(createSegment(0, 8, 50, 8)));
		assertFalse(this.shape.intersects(createSegment(5, 0, 5, 50)));
		assertTrue(this.shape.intersects(createSegment(5, -4, 6, 12)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
		assertFalse(createEllipse(5, 8, 2, 1).intersects(triangle));
		assertTrue(createEllipse(-10, 1, 2, 1).intersects(triangle));
		assertTrue(createEllipse(-1, -2, 2, 1).intersects(triangle));
		
		assertFalse(createEllipse(1, 0, 2, 1).intersects(triangle));
		assertFalse(createEllipse(0.9, 0, 2, 1).intersects(triangle));
		assertFalse(createEllipse(0.8, 0, 2, 1).intersects(triangle));
		assertFalse(createEllipse(0.7, 0, 2, 1).intersects(triangle));
		assertFalse(createEllipse(0.6, 0, 2, 1).intersects(triangle));
		assertTrue(createEllipse(0.5, 0, 2, 1).intersects(triangle));

		assertFalse(createEllipse(-1.12464, -2.86312, 2, 1).intersects(triangle));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPath2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
		p.lineTo(20, 8);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPathIterator2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
		p.lineTo(20, 8);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsOrientedRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		OrientedRectangle2afp rectangle = createOrientedRectangle(
				6, 9,
				0.894427190999916, -0.447213595499958, 13.999990000000002,
				12.999989999999997);
		assertFalse(createEllipse(0, -5, 2, 1).intersects(rectangle));
		assertFalse(createEllipse(0, -4.5, 2, 1).intersects(rectangle));
		assertTrue(createEllipse(0, -4, 2, 1).intersects(rectangle));
		assertTrue(createEllipse(4, 4, 2, 1).intersects(rectangle));
		assertFalse(createEllipse(20, -2, 2, 1).intersects(rectangle));
		assertTrue(createEllipse(-15, -10, 50, 50).intersects(rectangle));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void intersectsParallelogram2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Parallelogram2afp para = createParallelogram(
				6, 9,
				2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
				-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
		assertFalse(createEllipse(0, 0, 2, 1).intersects(para));
		assertFalse(createEllipse(0, 1, 2, 1).intersects(para));
		assertTrue(createEllipse(0, 2, 2, 1).intersects(para));
		assertTrue(createEllipse(0, 3, 2, 1).intersects(para));
		assertTrue(createEllipse(0, 4, 2, 1).intersects(para));
		assertTrue(createEllipse(1, 3, 2, 1).intersects(para));
		assertTrue(createEllipse(5, 5, 2, 1).intersects(para));
		assertFalse(createEllipse(0.1, 1, 2, 1).intersects(para));
		assertFalse(createEllipse(0.2, 1, 2, 1).intersects(para));
		assertTrue(createEllipse(0.3, 1, 2, 1).intersects(para));
		assertTrue(createEllipse(0.4, 1, 2, 1).intersects(para));
		assertFalse(createEllipse(-7, 7.5, 2, 1).intersects(para));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createRoundRectangle(0, 0, 2, 2, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(0, 20, 2, 2, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(20, 20, 2, 2, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(20, 0, 2, 2, .1, .2)));

		assertFalse(this.shape.intersects(createRoundRectangle(0, 12, 2, 2, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(20, 12, 2, 2, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(6, 0, 2, 2, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(6, 20, 2, 2, .1, .2)));

		assertTrue(this.shape.intersects(createRoundRectangle(6, 10, 2, 2, .1, .2)));
		assertTrue(this.shape.intersects(createRoundRectangle(4, 12, 2, 2, .1, .2)));
		assertTrue(this.shape.intersects(createRoundRectangle(9, 12, 2, 2, .1, .2)));
		assertTrue(this.shape.intersects(createRoundRectangle(6, 7, 2, 2, .1, .2)));
		assertTrue(this.shape.intersects(createRoundRectangle(6, 17, 2, 2, .1, .2)));

		assertFalse(this.shape.intersects(createRoundRectangle(4, 7, 2, 2, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(4, 17, 2, 2, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(9, 7, 2, 2, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(9, 17, 2, 2, .1, .2)));

		assertFalse(this.shape.intersects(createRoundRectangle(3, 6, 2, 2, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(3.1, 6.1, 2, 2, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(3.2, 6.2, 2, 2, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(3.2, 6.3, 2, 2, .1, .2)));

		assertFalse(this.shape.intersects(createRoundRectangle(4.3, 6.4, 2, 2, .1, .2)));
        assertFalse(this.shape.intersects(createRoundRectangle(4.3, 6.5, 2, 2, .1, .2)));
        assertFalse(this.shape.intersects(createRoundRectangle(4.3, 6.6, 2, 2, .1, .2)));
        assertTrue(this.shape.intersects(createRoundRectangle(4.3, 6.7, 2, 2, .1, .2)));
        assertTrue(this.shape.intersects(createRoundRectangle(4.3, 6.8, 2, 2, .1, .2)));
        assertTrue(this.shape.intersects(createRoundRectangle(4.3, 6.9, 2, 2, .1, .2)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointShallowEllipsePoint_horizontalAxisGreater(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Values computed with GeoGebra tool
		Point2D result;
		
		// Lower / Lower
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(0, 0, 5, 8, 10, 5, result);
		assertFpPointEquals(6, 9, result);

		// Lower / Upper
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(0, 24, 5, 8, 10, 5, result);
		assertFpPointEquals(6.33945, 12.20297, result);

		// Upper / Lower
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(24, 0, 5, 8, 10, 5, result);
		assertFpPointEquals(14.48365, 9.39355, result);

		// Upper / Upper
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(24, 24, 5, 8, 10, 5, result);
		assertFpPointEquals(14.24203, 11.82337, result);

		// On x axis (positive)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(18, 10.5, 5, 8, 10, 5, result);
		assertFpPointEquals(15, 10.5, result);

		// On x axis (negative)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(0, 10.5, 5, 8, 10, 5, result);
		assertFpPointEquals(5, 10.5, result);

		// On y axis (positive)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(10, 24, 5, 8, 10, 5, result);
		assertFpPointEquals(10, 13, result);

		// On y axis (negative)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(10, 0, 5, 8, 10, 5, result);
		assertFpPointEquals(10, 8, result);

		// Inside
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(6, 11, 5, 8, 10, 5, result);
		assertFpPointEquals(5.42383, 11.50731, result);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointShallowEllipsePoint_verticalAxisGreater(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Values computed with GeoGebra tool
		Point2D result;
		
		// Lower / Lower
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(0, 0, 5, 8, 5, 10, result);
		assertFpPointEquals(6.58303, 8.34848, result);

		// Lower / Upper
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(0, 24, 5, 8, 5, 10, result);
		assertFpPointEquals(6.39777, 17.4878, result);

		// Upper / Lower
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(24, 0, 5, 8, 5, 10, result);
		assertFpPointEquals(9.08189, 9.12824, result);

		// Upper / Upper
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(24, 24, 5, 8, 5, 10, result);
		assertFpPointEquals(9.2587, 16.55357, result);

		// On x axis (positive)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(18, 13, 5, 8, 5, 10, result);
		assertFpPointEquals(10, 13, result);

		// On x axis (negative)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(0, 13, 5, 8, 5, 10, result);
		assertFpPointEquals(5, 13, result);

		// On y axis (positive)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(7.5, 24, 5, 8, 5, 10, result);
		assertFpPointEquals(7.5, 18, result);

		// On y axis (negative)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(7.5, 0, 5, 8, 5, 10, result);
		assertFpPointEquals(7.5, 8, result);

		// Inside
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointShallowEllipsePoint(6, 11, 5, 8, 5, 10, result);
		assertFpPointEquals(5.25055, 10.81828, result);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointSolidEllipsePoint_horizontalAxisGreater(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Values computed with GeoGebra tool
		Point2D result;
		
		// Lower / Lower
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(0, 0, 5, 8, 10, 5, result);
		assertFpPointEquals(6, 9, result);

		// Lower / Upper
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(0, 24, 5, 8, 10, 5, result);
		assertFpPointEquals(6.33945, 12.20297, result);

		// Upper / Lower
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(24, 0, 5, 8, 10, 5, result);
		assertFpPointEquals(14.48365, 9.39355, result);

		// Upper / Upper
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(24, 24, 5, 8, 10, 5, result);
		assertFpPointEquals(14.24203, 11.82337, result);

		// On x axis (positive)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(18, 10.5, 5, 8, 10, 5, result);
		assertFpPointEquals(15, 10.5, result);

		// On x axis (negative)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(0, 10.5, 5, 8, 10, 5, result);
		assertFpPointEquals(5, 10.5, result);

		// On y axis (positive)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(10, 24, 5, 8, 10, 5, result);
		assertFpPointEquals(10, 13, result);

		// On y axis (negative)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(10, 0, 5, 8, 10, 5, result);
		assertFpPointEquals(10, 8, result);

		// Inside
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(6, 11, 5, 8, 10, 5, result);
		assertFpPointEquals(6, 11, result);

		//
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(9.897519745562938, 7.003543789189412, 2, 1, 10, 8, result);
		assertFpPointEquals(9.897519745562938, 7.003543789189412, result);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointSolidEllipsePoint_verticalAxisGreater(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Values computed with GeoGebra tool
		Point2D result;
		
		// Lower / Lower
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(0, 0, 5, 8, 5, 10, result);
		assertFpPointEquals(6.58303, 8.34848, result);

		// Lower / Upper
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(0, 24, 5, 8, 5, 10, result);
		assertFpPointEquals(6.39777, 17.4878, result);

		// Upper / Lower
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(24, 0, 5, 8, 5, 10, result);
		assertFpPointEquals(9.08189, 9.12824, result);

		// Upper / Upper
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(24, 24, 5, 8, 5, 10, result);
		assertFpPointEquals(9.2587, 16.55357, result);

		// On x axis (positive)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(18, 13, 5, 8, 5, 10, result);
		assertFpPointEquals(10, 13, result);

		// On x axis (negative)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(0, 13, 5, 8, 5, 10, result);
		assertFpPointEquals(5, 13, result);

		// On y axis (positive)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(7.5, 24, 5, 8, 5, 10, result);
		assertFpPointEquals(7.5, 18, result);

		// On y axis (negative)
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(7.5, 0, 5, 8, 5, 10, result);
		assertFpPointEquals(7.5, 8, result);

		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(7.5, 7, 5, 8, 5, 10, result);
		assertFpPointEquals(7.5, 8, result);

		// Inside
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(6, 11, 5, 8, 5, 10, result);
		assertFpPointEquals(6, 11, result);

		// Outside / touching the bounding box of the ellipse
		result = createPoint(0, 0);
		Ellipse2afp.findsClosestPointSolidEllipsePoint(3, 8, 5, 8, 5, 10, result);
		assertFpPointEquals(5.75656, 9.41648, result);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticComputeFarthestPointToShallowEllipse_verticalAxisGreater(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// Values computed with GeoGebra tool
		Point2D result;
		
		// Lower / Lower
		result = createPoint(0, 0);
		Ellipse2afp.findsFarthestPointShallowEllipsePoint(0, 0, 5, 8, 5, 10, result);
		assertFpPointEquals(8.05329, 17.92645, result);

		// Lower / Upper
		result = createPoint(0, 0);
		Ellipse2afp.findsFarthestPointShallowEllipsePoint(0, 24, 5, 8, 5, 10, result);
		assertFpPointEquals(8.12711, 8.08913, result);

		// Upper / Lower
		result = createPoint(0, 0);
		Ellipse2afp.findsFarthestPointShallowEllipsePoint(24, 0, 5, 8, 5, 10, result);
		assertFpPointEquals(6.31519, 17.75919, result);

		// Upper / Upper
		result = createPoint(0, 0);
		Ellipse2afp.findsFarthestPointShallowEllipsePoint(24, 24, 5, 8, 5, 10, result);
		assertFpPointEquals(6.16141, 8.28223, result);

		// On x axis (positive)
		result = createPoint(0, 0);
		Ellipse2afp.findsFarthestPointShallowEllipsePoint(18, 13, 5, 8, 5, 10, result);
		assertFpPointEquals(5, 13, result);

		// On x axis (negative)
		result = createPoint(0, 0);
		Ellipse2afp.findsFarthestPointShallowEllipsePoint(0, 13, 5, 8, 5, 10, result);
		assertFpPointEquals(10, 13, result);

		// On y axis (positive)
		result = createPoint(0, 0);
		Ellipse2afp.findsFarthestPointShallowEllipsePoint(7.5, 24, 5, 8, 5, 10, result);
		assertFpPointEquals(7.5, 8, result);

		// On y axis (negative)
		result = createPoint(0, 0);
		Ellipse2afp.findsFarthestPointShallowEllipsePoint(7.5, 0, 5, 8, 5, 10, result);
		assertFpPointEquals(7.5, 18, result);

		// Inside
		result = createPoint(0, 0);
		Ellipse2afp.findsFarthestPointShallowEllipsePoint(6, 11, 5, 8, 5, 10, result);
		assertFpPointEquals(7.82555, 17.97659, result);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticContainsEllipsePoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Ellipse2afp.containsEllipsePoint(0, 0, 2, 1, 1, 0.5));
		assertTrue(Ellipse2afp.containsEllipsePoint(0, 0, 2, 1, 1.5, 0.7));
		assertTrue(Ellipse2afp.containsEllipsePoint(0, 0, 2, 1, 0.3, 0.3));
		assertTrue(Ellipse2afp.containsEllipsePoint(0, 0, 2, 1, 2, 0.5));
		assertTrue(Ellipse2afp.containsEllipsePoint(0, 0, 2, 1, 1, 1));
		assertFalse(Ellipse2afp.containsEllipsePoint(0, 0, 2, 1, 0, 0));
		assertFalse(Ellipse2afp.containsEllipsePoint(0, 0, 2, 1, 0, 1));
		assertFalse(Ellipse2afp.containsEllipsePoint(0, 0, 2, 1, 1, 10));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticContainsEllipseRectangle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(Ellipse2afp.containsEllipseRectangle(0, 0, 1, 1,
				0, 0, 1, 1));
		assertFalse(Ellipse2afp.containsEllipseRectangle(0, 0, 1, 1,
				-5, -5, -4, -4));
		assertFalse(Ellipse2afp.containsEllipseRectangle(0, 0, 1, 1,
				.5, .5, 5.5, 5.5));
		assertFalse(Ellipse2afp.containsEllipseRectangle(0, 0, 1, 1,
				.5, .5, 5.5, 1.1));
		assertFalse(Ellipse2afp.containsEllipseRectangle(0, 0, 1, 1,
				-5, -5, 0, 0));
		assertFalse(Ellipse2afp.containsEllipseRectangle(0, 0, 1, 1,
				-9, -9, -5, -5));
		assertFalse(Ellipse2afp.containsEllipseRectangle(0, 0, 1, 1,
				-5, -9, -1, -5));
		assertFalse(Ellipse2afp.containsEllipseRectangle(0, 0, 1, 1,
				5, -5, 11, 0));
		assertFalse(Ellipse2afp.containsEllipseRectangle(0, 0, 1, 1,
				-5, -5, -5, -5));
		assertFalse(Ellipse2afp.containsEllipseRectangle(0, 0, 1, 1,
				-5, -5, -4.9, -4.9));
		assertTrue(Ellipse2afp.containsEllipseRectangle(0, 0, 1, 1,
				.25, .25, .75, .75));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsEllipseEllipse(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Ellipse2afp.intersectsEllipseEllipse(0, 0, 1, 1,
				0, 0, 1, 1));
		assertFalse(Ellipse2afp.intersectsEllipseEllipse(0, 0, 1, 1,
				-5, -5, 6, 6));
        assertTrue(Ellipse2afp.intersectsEllipseEllipse(0, 0, 1, 1,
                -5, -5, 6.1, 6.1));
		assertFalse(Ellipse2afp.intersectsEllipseEllipse(0, 0, 1, 1,
				.5, .5, 4.5, 4.5));
		assertTrue(Ellipse2afp.intersectsEllipseEllipse(0, 0, 1, 1,
				.5, .5, 4.5, .5));
		assertTrue(Ellipse2afp.intersectsEllipseEllipse(0, 0, 1, 1,
				-5, -5, 10, 10));
		assertFalse(Ellipse2afp.intersectsEllipseEllipse(0, 0, 1, 1,
				-5, -5, 1, 1));
		assertFalse(Ellipse2afp.intersectsEllipseEllipse(0, 0, 1, 1,
				-5, -5, 9, 1));
		assertFalse(Ellipse2afp.intersectsEllipseEllipse(0, 0, 1, 1,
				5, -5, 1, 10));
		assertFalse(Ellipse2afp.intersectsEllipseEllipse(0, 0, 1, 1,
				-5, -5, 5, 5));
		assertFalse(Ellipse2afp.intersectsEllipseEllipse(0, 0, 1, 1,
				-5, -5, 5.1, 5.1));
		assertFalse(Ellipse2afp.intersectsEllipseEllipse(5, 8, 5, 10,
				1, -1, 5, 10));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsEllipseLine(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Ellipse2afp.intersectsEllipseLine(0, 0, 1, 1,
				0, 0, 1, 1));
		assertTrue(Ellipse2afp.intersectsEllipseLine(0, 0, 1, 1,
				-5, -5, 1, 1));
		assertTrue(Ellipse2afp.intersectsEllipseLine(0, 0, 1, 1,
				.5, .5, 5, 5));
		assertTrue(Ellipse2afp.intersectsEllipseLine(0, 0, 1, 1,
				.5, .5, 5, .6));
		assertTrue(Ellipse2afp.intersectsEllipseLine(0, 0, 1, 1,
				-5, -5, 5, 5));
		assertTrue(Ellipse2afp.intersectsEllipseLine(0, 0, 1, 1,
				-5, -5, -4, -4));
		assertFalse(Ellipse2afp.intersectsEllipseLine(0, 0, 1, 1,
				-5, -5, 4, -4));
		assertFalse(Ellipse2afp.intersectsEllipseLine(0, 0, 1, 1,
				5, -5, 6, 5));
		assertTrue(Ellipse2afp.intersectsEllipseLine(0, 0, 1, 1,
				-5, -5, .0, .0));
		assertTrue(Ellipse2afp.intersectsEllipseLine(0, 0, 1, 1,
				-5, -5, .1, .1));
		assertTrue(Ellipse2afp.intersectsEllipseLine(0, 0, 1, 1,
				-5, -5, .4, .3));

		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.7773438, -3.0272121, 6.7890625, -3.1188917));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.7890625, -3.1188917, 6.8007812, -3.2118688));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.8007812, -3.2118688, 6.8125, -3.3061523));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.8125, -3.3061523, 6.8242188, -3.401752));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.8242188, -3.401752, 6.8359375, -3.4986773));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.8359375, -3.4986773, 6.8476562, -3.5969372));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.8476562, -3.5969372, 6.859375, -3.6965408));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.859375, -3.6965408, 6.8710938, -3.7974977));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.8710938, -3.7974977, 6.8828125, -3.8998175));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.8828125, -3.8998175, 6.8945312, -4.003509));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.8945312, -4.003509, 6.90625, -4.1085815));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.90625, -4.1085815, 6.9179688, -4.2150445));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.9179688, -4.2150445, 6.9296875, -4.3229074));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.9296875, -4.3229074, 6.9414062, -4.4321795));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.9414062, -4.4321795, 6.953125, -4.5428696));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.953125, -4.5428696, 6.9648438, -4.6549873));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.9648438, -4.6549873, 6.9765625, -4.7685423));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.9765625, -4.7685423, 6.9882812, -4.8835435));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				6, -5, 1, 2, 6.9882812, -4.8835435, 7, -5));

		assertTrue(Ellipse2afp.intersectsEllipseLine(
				0, 0, 1, 2, .5, -1, .5, 2));
		assertTrue(Ellipse2afp.intersectsEllipseLine(
				0, 0, 1, 2, .5, -1, .5, -.5));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsEllipseRectangle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Ellipse2afp.intersectsEllipseRectangle(0, 0, 1, 1,
				0, 0, 1, 1));
		assertTrue(Ellipse2afp.intersectsEllipseRectangle(0, 0, 1, 1,
				-5, -5, 1, 1));
		assertTrue(Ellipse2afp.intersectsEllipseRectangle(0, 0, 1, 1,
				.5, .5, 5, 5));
		assertTrue(Ellipse2afp.intersectsEllipseRectangle(0, 0, 1, 1,
				.5, .5, 5, .6));
		assertTrue(Ellipse2afp.intersectsEllipseRectangle(0, 0, 1, 1,
				-5, -5, 5, 5));
		assertFalse(Ellipse2afp.intersectsEllipseRectangle(0, 0, 1, 1,
				-5, -5, -4, -4));
		assertFalse(Ellipse2afp.intersectsEllipseRectangle(0, 0, 1, 1,
				-5, -5, 4, -4));
		assertFalse(Ellipse2afp.intersectsEllipseRectangle(0, 0, 1, 1,
				5, -5, 6, 5));
		assertFalse(Ellipse2afp.intersectsEllipseRectangle(0, 0, 1, 1,
				-5, -5, .0, .0));
		assertFalse(Ellipse2afp.intersectsEllipseRectangle(0, 0, 1, 1,
				-5, -5, .1, .1));
		assertTrue(Ellipse2afp.intersectsEllipseRectangle(5, 8, .2, .4,
				5, 8, 5.05, 8.1));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsEllipseSegment_noIntersectionAtTouching(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				0, 0, 1, 1,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				-5, -5, 1, 1,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				.5, .5, 5, 5,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				.5, .5, 5, .6,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				-5, -5, 5, 5,
				false));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				-5, -5, -4, -4,
				false));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				-5, -5, 4, -4,
				false));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				5, -5, 6, 5,
				false));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				-5, -5, .0, .0,
				false));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				-5, -5, .1, .1,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				-5, -5, .4, .3,
				false));
		
		assertFalse(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.7773438, -3.0272121, 6.7890625, -3.1188917,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.7890625, -3.1188917, 6.8007812, -3.2118688,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8007812, -3.2118688, 6.8125, -3.3061523,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8125, -3.3061523, 6.8242188, -3.401752,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8242188, -3.401752, 6.8359375, -3.4986773,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8359375, -3.4986773, 6.8476562, -3.5969372,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8476562, -3.5969372, 6.859375, -3.6965408,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.859375, -3.6965408, 6.8710938, -3.7974977,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8710938, -3.7974977, 6.8828125, -3.8998175,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8828125, -3.8998175, 6.8945312, -4.003509,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8945312, -4.003509, 6.90625, -4.1085815,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.90625, -4.1085815, 6.9179688, -4.2150445,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.9179688, -4.2150445, 6.9296875, -4.3229074,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.9296875, -4.3229074, 6.9414062, -4.4321795,
				false));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.9414062, -4.4321795, 6.953125, -4.5428696,
				false));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.953125, -4.5428696, 6.9648438, -4.6549873,
				false));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.9648438, -4.6549873, 6.9765625, -4.7685423,
				false));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.9765625, -4.7685423, 6.9882812, -4.8835435,
				false));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.9882812, -4.8835435, 7, -5,
				false));

		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				0, 0, 1, 2, .5, -1, .5, 2,
				false));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(
				0, 0, 1, 2, .5, -1, .5, -.5,
				false));

		assertFalse(Ellipse2afp.intersectsEllipseSegment(5, 8, 5, 10, 0, 8, 50, 8, false));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(5, 8, 5, 10, 5, 0, 5, 50, false));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsEllipseSegment_intersectionAtTouching(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				0, 0, 1, 1,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				-5, -5, 1, 1,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				.5, .5, 5, 5,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				.5, .5, 5, .6,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				-5, -5, 5, 5,
				true));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				-5, -5, -4, -4,
				true));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				-5, -5, 4, -4,
				true));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				5, -5, 6, 5,
				true));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				-5, -5, .0, .0,
				true));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				-5, -5, .1, .1,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(0, 0, 1, 1,
				-5, -5, .4, .3,
				true));
		
		assertFalse(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.7773438, -3.0272121, 6.7890625, -3.1188917,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.7890625, -3.1188917, 6.8007812, -3.2118688,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8007812, -3.2118688, 6.8125, -3.3061523,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8125, -3.3061523, 6.8242188, -3.401752,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8242188, -3.401752, 6.8359375, -3.4986773,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8359375, -3.4986773, 6.8476562, -3.5969372,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8476562, -3.5969372, 6.859375, -3.6965408,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.859375, -3.6965408, 6.8710938, -3.7974977,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8710938, -3.7974977, 6.8828125, -3.8998175,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8828125, -3.8998175, 6.8945312, -4.003509,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.8945312, -4.003509, 6.90625, -4.1085815,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.90625, -4.1085815, 6.9179688, -4.2150445,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.9179688, -4.2150445, 6.9296875, -4.3229074,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.9296875, -4.3229074, 6.9414062, -4.4321795,
				true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.9414062, -4.4321795, 6.953125, -4.5428696,
				true));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.953125, -4.5428696, 6.9648438, -4.6549873,
				true));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.9648438, -4.6549873, 6.9765625, -4.7685423,
				true));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.9765625, -4.7685423, 6.9882812, -4.8835435,
				true));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(
				6, -5, 1, 2, 6.9882812, -4.8835435, 7, -5,
				true));

		assertTrue(Ellipse2afp.intersectsEllipseSegment(
				0, 0, 1, 2, .5, -1, .5, 2,
				true));
		assertFalse(Ellipse2afp.intersectsEllipseSegment(
				0, 0, 1, 2, .5, -1, .5, -.5,
				true));

		assertTrue(Ellipse2afp.intersectsEllipseSegment(5, 8, 5, 10, 0, 8, 50, 8, true));
		assertTrue(Ellipse2afp.intersectsEllipseSegment(5, 8, 5, 10, 5, 0, 5, 50, true));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsEllipseCircle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(Ellipse2afp.intersectsEllipseCircle(5, 8, 5, 10, 0, 0, 1));
		assertTrue(Ellipse2afp.intersectsEllipseCircle(5, 8, 5, 10, 7.5, 7, 2));
		assertFalse(Ellipse2afp.intersectsEllipseCircle(5, 8, 5, 10, 3, 8, 2));
		assertFalse(Ellipse2afp.intersectsEllipseCircle(5, 8, 5, 10, 4, 8, 2));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(123.456, 789.123, 456.789, 123.456);
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(789.123, this.shape.getMinY());
		assertEpsilonEquals(580.245, this.shape.getMaxX());
		assertEpsilonEquals(912.579, this.shape.getMaxY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setPoint2DPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(createPoint(123.456, 789.123), createPoint(456.789, 123.456));
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(123.456, this.shape.getMinY());
		assertEpsilonEquals(456.789, this.shape.getMaxX());
		assertEpsilonEquals(789.123, this.shape.getMaxY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setWidth(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setWidth(123.456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(128.456, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setHeight(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setHeight(123.456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(131.456, this.shape.getMaxY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class) 
	public void setFromCornersDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCorners(123.456, 789.123, 456.789, 159.357);
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(159.357, this.shape.getMinY());
		assertEpsilonEquals(456.789, this.shape.getMaxX());
		assertEpsilonEquals(789.123, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setFromCornersPoint2DPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCorners(createPoint(123.456, 789.123), createPoint(456.789, 159.357));
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(159.357, this.shape.getMinY());
		assertEpsilonEquals(456.789, this.shape.getMaxX());
		assertEpsilonEquals(789.123, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setFromCenterDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCenter(123.456, 789.123, 456.789, 159.357);
		assertEpsilonEquals(-209.877, this.shape.getMinX());
		assertEpsilonEquals(159.357, this.shape.getMinY());
		assertEpsilonEquals(456.789, this.shape.getMaxX());
		assertEpsilonEquals(1418.889, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setFromCenterPoint2DPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCenter(createPoint(123.456, 789.123), createPoint(456.789, 159.357));
		assertEpsilonEquals(-209.877, this.shape.getMinX());
		assertEpsilonEquals(159.357, this.shape.getMinY());
		assertEpsilonEquals(456.789, this.shape.getMaxX());
		assertEpsilonEquals(1418.889, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getMinX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getMinX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setMinX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setMinX(0);
		assertEpsilonEquals(0, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		this.shape.setMinX(456);
		assertEpsilonEquals(456, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(456, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCenterX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(7.5, this.shape.getCenterX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getMaxX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(10, this.shape.getMaxX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setMaxX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxX(1456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(1456, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		this.shape.setMaxX(0);
		assertEpsilonEquals(0, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getMinY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(8, this.shape.getMinY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setMinY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setMinY(0);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(0, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		this.shape.setMinY(456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(456, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(456, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCenterY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(13, this.shape.getCenterY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getMaxY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(18, this.shape.getMaxY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setMaxY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxY(1456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(1456, this.shape.getMaxY());
		this.shape.setMaxY(0);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(0, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(0, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getWidth(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getWidth());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getHeight(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(10, this.shape.getHeight());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void inflateDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.inflate(15, -10, -20, 20);
		assertEpsilonEquals(-10, this.shape.getMinX());
		assertEpsilonEquals(18, this.shape.getMinY());
		assertEpsilonEquals(-10, this.shape.getMaxX());
		assertEpsilonEquals(38, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getHorizontalRadius_verticalAxisGreater(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2.5, this.shape.getHorizontalRadius());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getVerticalRadius_verticalAxisGreater(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getVerticalRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getMinFocusPoint_verticalAxisGreater(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p = this.shape.getMinFocusPoint();
		assertNotNull(p);
		assertEpsilonEquals(7.5, p.getX());
		assertEpsilonEquals(8.66987, p.getY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getMaxFocusPoint_verticalAxisGreater(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p = this.shape.getMaxFocusPoint();
		assertNotNull(p);
		assertEpsilonEquals(7.5, p.getX());
		assertEpsilonEquals(17.33013, p.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getHorizontalRadius_horizontalAxisGreater(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(5, 8, 10, 5);
		assertEpsilonEquals(5, this.shape.getHorizontalRadius());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getVerticalRadius_horizontalAxisGreater(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(5, 8, 10, 5);
		assertEpsilonEquals(2.5, this.shape.getVerticalRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getMinFocusPoint_horizontalAxisGreater(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(5, 8, 10, 5);
		Point2D p = this.shape.getMinFocusPoint();
		assertNotNull(p);
		assertEpsilonEquals(5.66987, p.getX());
		assertEpsilonEquals(10.5, p.getY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getMaxFocusPoint_horizontalAxisGreater(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(5, 8, 10, 5);
		Point2D p = this.shape.getMaxFocusPoint();
		assertNotNull(p);
		assertEpsilonEquals(14.3301, p.getX());
		assertEpsilonEquals(10.5, p.getY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape2D) createCircle(7.5, 7, 2)));
		assertTrue(this.shape.intersects((Shape2D) createEllipse(0.1, 8, 5, 10)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_addVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(123.456, -789.123));
		assertEpsilonEquals(128.456, this.shape.getMinX());
		assertEpsilonEquals(-781.123, this.shape.getMinY());
		assertEpsilonEquals(133.456, this.shape.getMaxX());
		assertEpsilonEquals(-771.123, this.shape.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_plusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_plus(createVector(123.456, -789.123));
		assertEpsilonEquals(128.456, shape.getMinX());
		assertEpsilonEquals(-781.123, shape.getMinY());
		assertEpsilonEquals(133.456, shape.getMaxX());
		assertEpsilonEquals(-771.123, shape.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_removeVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(123.456, -789.123));
		assertEpsilonEquals(-118.456, this.shape.getMinX());
		assertEpsilonEquals(797.123, this.shape.getMinY());
		assertEpsilonEquals(-113.456, this.shape.getMaxX());
		assertEpsilonEquals(807.123, this.shape.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_minusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_minus(createVector(123.456, -789.123));
		assertEpsilonEquals(-118.456, shape.getMinX());
		assertEpsilonEquals(797.123, shape.getMinY());
		assertEpsilonEquals(-113.456, shape.getMaxX());
		assertEpsilonEquals(807.123, shape.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_multiplyTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Transform2D tr;
		Shape2afp newShape;
		
		newShape = this.shape.operator_multiply(null);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform2D();
		newShape = this.shape.operator_multiply(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform2D();
		tr.makeTranslationMatrix(10, -10);
		newShape = this.shape.operator_multiply(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertTrue(newShape instanceof Path2afp);
		PathIterator2afp pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 20, 3);
		assertElement(pi, PathElementType.CURVE_TO, 20, 5.76142374915397, 18.880711874576983, 8, 17.5, 8);
		assertElement(pi, PathElementType.CURVE_TO, 16.119288125423017, 8, 15, 5.76142374915397, 15, 3);
		assertElement(pi, PathElementType.CURVE_TO, 15, 0.23857625084603, 16.119288125423017, -2, 17.5, -2);
		assertElement(pi, PathElementType.CURVE_TO, 18.880711874576983, -2, 20, 0.23857625084603, 20, 3);
		assertElement(pi, PathElementType.CLOSE, 20, 3);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.operator_and(createPoint(0,0)));
		assertFalse(this.shape.operator_and(createPoint(11,10)));
		assertFalse(this.shape.operator_and(createPoint(11,50)));
		assertTrue(this.shape.operator_and(createPoint(9,12)));
		assertTrue(this.shape.operator_and(createPoint(9,11)));
		assertTrue(this.shape.operator_and(createPoint(8,12)));
		assertFalse(this.shape.operator_and(createPoint(3,7)));
		assertFalse(this.shape.operator_and(createPoint(10,12)));
		assertFalse(this.shape.operator_and(createPoint(10,11)));
		assertTrue(this.shape.operator_and(createPoint(9,10)));
		assertFalse(this.shape.operator_and(createPoint(9.5,9.5)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createCircle(7.5, 7, 2)));
		assertTrue(this.shape.operator_and(createEllipse(0.1, 8, 5, 10)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_upToPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(10.63171, this.shape.operator_upTo(createPoint(0, 0)));
		assertEpsilonEquals(9.12909, this.shape.operator_upTo(createPoint(0, 24)));
		assertEpsilonEquals(17.48928, this.shape.operator_upTo(createPoint(24, 0)));
		assertEpsilonEquals(16.5153, this.shape.operator_upTo(createPoint(24, 24)));
		assertEpsilonEquals(8, this.shape.operator_upTo(createPoint(18, 13)));
		assertEpsilonEquals(5, this.shape.operator_upTo(createPoint(0, 13)));
		assertEpsilonEquals(6, this.shape.operator_upTo(createPoint(7.5, 24)));
		assertEpsilonEquals(8, this.shape.operator_upTo(createPoint(7.5, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(6, 11)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(6.58303, 8.34848, this.shape.getClosestPointTo(createCircle(0, 0, 1)));
		assertFpPointEquals(8.77919, 17.29589, this.shape.getClosestPointTo(createCircle(12, 20, 1)));
		assertFpPointEquals(9, 9, this.shape.getClosestPointTo(createCircle(10.5, 8, 1)));
		assertClosestPointInBothShapes(this.shape, createCircle(6, 11, 1));
		assertClosestPointInBothShapes(this.shape, createCircle(10, 10, 1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(92.76993, this.shape.getDistanceSquared(createCircle(0, 0, 1)));
		assertEpsilonEquals(10.27493, this.shape.getDistanceSquared(createCircle(12, 20, 1)));
		assertEpsilonEquals(0.64445, this.shape.getDistanceSquared(createCircle(10.5, 8, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(6, 11, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(10, 10, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(6.60692, 8.32992, this.shape.getClosestPointTo(createSegment(0, 0, 1, 1)));
		assertFpPointEquals(6.49572, 8.42117, this.shape.getClosestPointTo(createSegment(0, 1, 1, 0)));
		assertFpPointEquals(8.00989, 8.1051, this.shape.getClosestPointTo(createSegment(4, 6, 16, 11)));
		assertFpPointEquals(8.63363, 17.45641, this.shape.getClosestPointTo(createSegment(7, 20, 16, 11)));
		assertClosestPointInBothShapes(this.shape, createSegment(7, 20, 6, 12));
		assertClosestPointInBothShapes(this.shape, createSegment(7, 20, 10, 0));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(85.1653, this.shape.getDistanceSquared(createSegment(0, 0, 1, 1)));
		assertEpsilonEquals(97.26811, this.shape.getDistanceSquared(createSegment(0, 1, 1, 0)));
		assertEpsilonEquals(0.16073, this.shape.getDistanceSquared(createSegment(4, 6, 16, 11)));
		assertEpsilonEquals(0.41402, this.shape.getDistanceSquared(createSegment(7, 20, 16, 11)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(7, 20, 6, 12)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(7, 20, 10, 0)));
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(6.72184, 8.24838, this.shape.getClosestPointTo(createEllipse(0, 0, 2, 1)));
		assertFpPointEquals(6.26802, 17.35074, this.shape.getClosestPointTo(createEllipse(1, 20, 2, 1)));
		assertFpPointEquals(9.86508, 14.62044, this.shape.getClosestPointTo(createEllipse(15, 15, 2, 1)));
		assertFpPointEquals(8.88619, 8.839, this.shape.getClosestPointTo(createEllipse(9, 8, 2, 1)));
		assertClosestPointInBothShapes(this.shape, createEllipse(9, 9, 2, 1));
		assertClosestPointInBothShapes(this.shape, createEllipse(6, 11, 2, 1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(79.70281, this.shape.getDistanceSquared(createEllipse(0, 0, 2, 1)));
		assertEpsilonEquals(19.93072, this.shape.getDistanceSquared(createEllipse(1, 20, 2, 1)));
		assertEpsilonEquals(27.10531, this.shape.getDistanceSquared(createEllipse(15, 15, 2, 1)));
		assertEpsilonEquals(0.05784, this.shape.getDistanceSquared(createEllipse(9, 8, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(9, 9, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(6, 11, 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(6.72506, 8.24628, this.shape.getClosestPointTo(createRectangle(0, 0, 2, 1)));
		assertFpPointEquals(6.22081, 17.29589, this.shape.getClosestPointTo(createRectangle(1, 20, 2, 1)));
		assertFpPointEquals(9.91272, 14.30964, this.shape.getClosestPointTo(createRectangle(15, 15, 2, 1)));
		assertFpPointEquals(9, 9, this.shape.getClosestPointTo(createRectangle(9, 8, 2, 1)));
		assertClosestPointInBothShapes(this.shape, createRectangle(9, 9, 2, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(6, 11, 2, 1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(74.83474, this.shape.getDistanceSquared(createRectangle(0, 0, 2, 1)));
		assertEpsilonEquals(17.68581, this.shape.getDistanceSquared(createRectangle(1, 20, 2, 1)));
		assertEpsilonEquals(26.35703, this.shape.getDistanceSquared(createRectangle(15, 15, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(9, 8, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(9, 9, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(6, 11, 2, 1)));
	}
	
	protected Triangle2afp createTestTriangle(double dx, double dy) {
		return createTriangle(dx, dy, dx + 6, dy + 3, dx - 1, dy + 2.5);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(7.60851, 8.00471, this.shape.getClosestPointTo(createTestTriangle(6, 4)));
		assertFpPointEquals(6.44422, 8.46775, this.shape.getClosestPointTo(createTestTriangle(0, 5)));
		assertClosestPointInBothShapes(this.shape, createTestTriangle(4, 14));
		assertFpPointEquals(9.12342, 16.80236, this.shape.getClosestPointTo(createTestTriangle(18, 22)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1.72933, this.shape.getDistanceSquared(createTestTriangle(6, 4)));
		assertEpsilonEquals(0.41609, this.shape.getDistanceSquared(createTestTriangle(0, 5)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(4, 14)));
		assertEpsilonEquals(105.80901, this.shape.getDistanceSquared(createTestTriangle(18, 22)));
	}

	protected MultiShape2afp createTestMultiShape(double dx, double dy) {
		Circle2afp circle = createCircle(dx - 3, dy + 2, 2);
		Triangle2afp triangle = createTestTriangle(dx +1, dy - 1);
		MultiShape2afp multishape = createMultiShape();
		multishape.add(circle);
		multishape.add(triangle);
		return multishape;
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToMultiShape2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(6.22081, 8.70411, this.shape.getClosestPointTo(createTestMultiShape(6, 4)));
		assertFpPointEquals(7.223799841106517, 8.030608297899144, this.shape.getClosestPointTo(createTestMultiShape(0, 5)));
		assertClosestPointInBothShapes(this.shape, createTestMultiShape(4, 14));
		assertFpPointEquals(8.603213137685831, 17.48683441764339, this.shape.getClosestPointTo(createTestMultiShape(18, 22)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredMultiShape2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(4.86401, this.shape.getDistanceSquared(createTestMultiShape(6, 4)));
		assertEpsilonEquals(1.112239832577874, this.shape.getDistanceSquared(createTestMultiShape(0, 5)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(4, 14)));
		assertEpsilonEquals(50.82386, this.shape.getDistanceSquared(createTestMultiShape(18, 22)));
	}

	protected OrientedRectangle2afp createTestOrientedRectangle(double dx, double dy) {
		Vector2D r = createVector(4, 1).toUnitVector();
		return createOrientedRectangle(dx, dy, r.getX(), r.getY(), 2, 1);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToOrientedRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(7.56154, 8.00152, this.shape.getClosestPointTo(createTestOrientedRectangle(6, 4)));
		assertFpPointEquals(5.93629, 9.09882, this.shape.getClosestPointTo(createTestOrientedRectangle(0, 5)));
		assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(4, 14));
		assertFpPointEquals(9.19246, 16.67999, this.shape.getClosestPointTo(createTestOrientedRectangle(18, 22)));
	}
		
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredOrientedRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(6.5022, this.shape.getDistanceSquared(createTestOrientedRectangle(6, 4)));
		assertEpsilonEquals(24.95386, this.shape.getDistanceSquared(createTestOrientedRectangle(0, 5)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(4, 14)));
		assertEpsilonEquals(65.48578, this.shape.getDistanceSquared(createTestOrientedRectangle(18, 22)));
	}

	protected Parallelogram2afp createTestParallelogram(double dx, double dy) {
		Vector2D r = createVector(4, 1).toUnitVector();
		Vector2D s = createVector(-1, -1).toUnitVector();
		return createParallelogram(dx, dy, r.getX(), r.getY(), 2, s.getX(), s.getY(), 1);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToParallelogram2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(7.79323, 8.03451, this.shape.getClosestPointTo(createTestParallelogram(6, 4)));
		assertFpPointEquals(6.12038, 8.83028, this.shape.getClosestPointTo(createTestParallelogram(0, 5)));
		assertClosestPointInBothShapes(this.shape, createTestParallelogram(4, 14));
		assertFpPointEquals(9.0662, 16.89719, this.shape.getClosestPointTo(createTestParallelogram(18, 22)));
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredParallelogram2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(8.78906, this.shape.getDistanceSquared(createTestParallelogram(6, 4)));
		assertEpsilonEquals(19.02116, this.shape.getDistanceSquared(createTestParallelogram(0, 5)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestParallelogram(4, 14)));
		assertEpsilonEquals(54.81199, this.shape.getDistanceSquared(createTestParallelogram(18, 22)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(6.71823, 8.25075, this.shape.getClosestPointTo(createRoundRectangle(0, 0, 2, 1, .1, .1)));
		assertFpPointEquals(6.22177, 17.29703, this.shape.getClosestPointTo(createRoundRectangle(1, 20, 2, 1, .1, .1)));
		assertFpPointEquals(9.90516, 14.36881, this.shape.getClosestPointTo(createRoundRectangle(15, 15, 2, 1, .1, .1)));
		assertFpPointEquals(8.99554, 8.99332, this.shape.getClosestPointTo(createRoundRectangle(9, 8, 2, 1, .1, .1)));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(9, 9, 2, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(6, 11, 2, 1, .1, .1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(75.50109, this.shape.getDistanceSquared(createRoundRectangle(0, 0, 2, 1, .1, .1)));
		assertEpsilonEquals(18.0315, this.shape.getDistanceSquared(createRoundRectangle(1, 20, 2, 1, .1, .1)));
		assertEpsilonEquals(26.48855, this.shape.getDistanceSquared(createRoundRectangle(15, 15, 2, 1, .1, .1)));
		assertEpsilonEquals(0.00159, this.shape.getDistanceSquared(createRoundRectangle(9, 8, 2, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(9, 9, 2, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(6, 11, 2, 1, .1, .1)));
	}

	protected Path2afp createTestPath(double dx, double dy) {
		Path2afp path = createPath();
		path.moveTo(dx, dy);
		path.lineTo(dx - 16, dy + 12);
		path.lineTo(dx - 5, dy + 21);
		path.lineTo(dx + 10, dy + 15);
		path.lineTo(dx - 8, dy + 1);
		path.lineTo(dx - 20, dy + 12);
		path.lineTo(dx - 2, dy + 28);
		path.lineTo(dx + 20, dy + 20);
		return path;
	}

	protected Path2afp createTestPath(double dx, double dy, PathWindingRule rule) {
		Path2afp path = createPath(rule);
		path.moveTo(dx, dy);
		path.lineTo(dx - 16, dy + 12);
		path.lineTo(dx - 5, dy + 21);
		path.lineTo(dx + 10, dy + 15);
		path.lineTo(dx - 8, dy + 1);
		path.lineTo(dx - 20, dy + 12);
		path.lineTo(dx - 2, dy + 28);
		path.lineTo(dx + 20, dy + 20);
		path.closePath();
		return path;
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToPath2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(6.52689, 17.60708, this.shape.getClosestPointTo(createTestPath(12, 0)));
		assertFpPointEquals(6.52689, 17.60708, this.shape.getClosestPointTo(createTestPath(12, 0, PathWindingRule.EVEN_ODD)));
		assertClosestPointInBothShapes(this.shape, createTestPath(12, 0, PathWindingRule.NON_ZERO));
		//
		assertFpPointEquals(8.35958, 8.3034, this.shape.getClosestPointTo(createTestPath(8, 0)));
		assertFpPointEquals(8.35958, 8.3034, this.shape.getClosestPointTo(createTestPath(8, 0, PathWindingRule.EVEN_ODD)));
		assertClosestPointInBothShapes(this.shape, createTestPath(8, 0, PathWindingRule.NON_ZERO));
		//
		assertFpPointEquals(6.64042, 17.6966, this.shape.getClosestPointTo(createTestPath(2, 8)));
		assertClosestPointInBothShapes(this.shape, createTestPath(2, 8, PathWindingRule.EVEN_ODD));
		assertClosestPointInBothShapes(this.shape, createTestPath(2, 8, PathWindingRule.NON_ZERO));
		//
		assertClosestPointInBothShapes(this.shape, createTestPath(2, 6));
		assertClosestPointInBothShapes(this.shape, createTestPath(2, 6, PathWindingRule.EVEN_ODD));
		assertClosestPointInBothShapes(this.shape, createTestPath(2, 6, PathWindingRule.NON_ZERO));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredPath2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5.41207, this.shape.getDistanceSquared(createTestPath(12, 0)));
		assertEpsilonEquals(5.41207, this.shape.getDistanceSquared(createTestPath(12, 0, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(12, 0, PathWindingRule.NON_ZERO)));
		//
		assertEpsilonEquals(0.40027, this.shape.getDistanceSquared(createTestPath(8, 0)));
		assertEpsilonEquals(0.40027, this.shape.getDistanceSquared(createTestPath(8, 0, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(8, 0, PathWindingRule.NON_ZERO)));
		//
		assertEpsilonEquals(0.80243, this.shape.getDistanceSquared(createTestPath(2, 8)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(2, 8, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(2, 8, PathWindingRule.NON_ZERO)));
		//
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(2, 6)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(2, 6, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(2, 6, PathWindingRule.NON_ZERO)));
	}

}