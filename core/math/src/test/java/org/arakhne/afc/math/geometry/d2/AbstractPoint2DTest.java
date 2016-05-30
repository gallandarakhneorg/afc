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

package org.arakhne.afc.math.geometry.d2;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
import static org.junit.Assert.*;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractPoint2DTest<P extends Point2D<? super P, ? super V>, V extends Vector2D<? super V, ? super P>,
		TT extends Tuple2D>
		extends AbstractTuple2DTest<P, TT> {
	
	public abstract P createPoint(double x, double y);

	public abstract V createVector(double x, double y);
	
	@Test
	public final void staticIsCollinearPoints() {
		assertTrue(Point2D.isCollinearPoints(0, 0, 0, 0, 0, 0));
		assertTrue(Point2D.isCollinearPoints(-6, -4, -1, 3, 4, 10));
		assertFalse(Point2D.isCollinearPoints(0, 0, 1, 1, 1, -5));
		//
		assertInlineParameterUsage(Point2D.class, "isCollinearPoints",
				double.class, double.class, double.class, double.class, double.class, double.class);
	}

	@Test
	public final void staticGetDistancePointPoint() {
		assertEpsilonEquals(0, Point2D.getDistancePointPoint(0, 0, 0, 0));
		assertEpsilonEquals(Math.sqrt(5), Point2D.getDistancePointPoint(0, 0, 1, 2));
		assertEpsilonEquals(Math.sqrt(2), Point2D.getDistancePointPoint(0, 0, 1, 1));
	}

	@Test
	public final void staticGetDistanceSquaredPointPoint() {
		assertEpsilonEquals(0, Point2D.getDistanceSquaredPointPoint(0, 0, 0, 0));
		assertEpsilonEquals(5, Point2D.getDistanceSquaredPointPoint(0, 0, 1, 2));
		assertEpsilonEquals(2, Point2D.getDistanceSquaredPointPoint(0, 0, 1, 1));
		//
		assertInlineParameterUsage(Point2D.class, "getDistancePointPoint", double.class, double.class, double.class, double.class);
	}

	@Test
	public final void staticGetDistanceL1PointPoint() {
		assertEpsilonEquals(4, Point2D.getDistanceL1PointPoint(1.0, 2.0, 3.0, 0));
		assertEpsilonEquals(0, Point2D.getDistanceL1PointPoint(1.0, 2.0, 1 ,2));
		assertEpsilonEquals(0, Point2D.getDistanceL1PointPoint(1, 2, 1.0, 2.0));
		assertEpsilonEquals(4, Point2D.getDistanceL1PointPoint(1.0, 2.0, -1, 0));
		//
		assertInlineParameterUsage(Point2D.class, "getDistanceL1PointPoint", double.class, double.class, double.class, double.class);
	}

	@Test
	public final void staticGetDistanceLinfPointPoint() {
		assertEpsilonEquals(2, Point2D.getDistanceLinfPointPoint(1.0,2.0,3.0,0));
		assertEpsilonEquals(0, Point2D.getDistanceLinfPointPoint(1.0,2.0,1,2));
		assertEpsilonEquals(0, Point2D.getDistanceLinfPointPoint(1,2,1.0,2.0));
		assertEpsilonEquals(2, Point2D.getDistanceLinfPointPoint(1.0,2.0,-1,0));
		//
		assertInlineParameterUsage(Point2D.class, "getDistanceLinfPointPoint", double.class, double.class, double.class, double.class);
	}

	@Test
	public final void getDistanceSquaredPoint2D() {
		Point2D point = createPoint(0, 0);
		Point2D point2 = createPoint(0, 0);
		Point2D point3 = createPoint(1, 2);
		Point2D point4 = createPoint(1, 1);
		assertEpsilonEquals(0, point.getDistanceSquared(point2));
		assertEpsilonEquals(5, point.getDistanceSquared(point3));
		assertEpsilonEquals(2, point.getDistanceSquared(point4));
		//
		assertInlineParameterUsage(Point2D.class, "getDistanceSquaredPointPoint", double.class, double.class, double.class, double.class);
	}
	
	@Test
	public final void getDistancePoint2D() {
		Point2D point = createPoint(0, 0);
		Point2D point2 = createPoint(0, 0);
		Point2D point3 = createPoint(1, 2);
		Point2D point4 = createPoint(1, 1);
		assertEpsilonEquals(0, point.getDistance(point2));
		assertEpsilonEquals(Math.sqrt(5), point.getDistance(point3));
		assertEpsilonEquals(Math.sqrt(2), point.getDistance(point4));
	}

	@Test
	public final void getDistanceL1Point2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Point2D point3 = createPoint(1, 2);
		Point2D point4 = createPoint(-1, 0);
		assertEpsilonEquals(4, point.getDistanceL1(point2));
		assertEpsilonEquals(0, point.getDistanceL1(point));
		assertEpsilonEquals(0, point.getDistanceL1(point3));
		assertEpsilonEquals(4, point.getDistanceL1(point4));
	}

	@Test
	public final void getDistanceLinfPoint2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Point2D point3 = createPoint(1, 2);
		Point2D point4 = createPoint(-1, 0);
		assertEpsilonEquals(2, point.getDistanceLinf(point2));
		assertEpsilonEquals(0, point.getDistanceLinf(point));
		assertEpsilonEquals(0, point.getDistanceLinf(point3));
		assertEpsilonEquals(2, point.getDistanceLinf(point4));
	}

	@Test
	public final void getIdistanceL1Point2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Point2D point3 = createPoint(1, 2);
		Point2D point4 = createPoint(-1, 0);
		assertEquals(4, point.getIdistanceL1(point2));
		assertEquals(0, point.getIdistanceL1(point));
		assertEquals(0, point.getIdistanceL1(point3));
		assertEquals(4, point.getIdistanceL1(point4));
	}

	@Test
	public final void getIdistanceLinfPoint2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Point2D point3 = createPoint(1, 2);
		Point2D point4 = createPoint(-1, 0);
		assertEquals(2, point.getIdistanceLinf(point2));
		assertEquals(0, point.getIdistanceLinf(point));
		assertEquals(0, point.getIdistanceLinf(point3));
		assertEquals(2, point.getIdistanceLinf(point4));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void toUnmodifiable_exception() {
		Point2D origin = createPoint(2, 3);
		Point2D immutable = origin.toUnmodifiable();
		assertEpsilonEquals(origin, immutable);
		immutable.add(1, 2);
	}

	@Test
	public final void toUnmodifiable_changeInOrigin() {
		Point2D origin = createPoint(2, 3);
		assumeMutable(origin);
		Point2D immutable = origin.toUnmodifiable();
		assertEpsilonEquals(origin, immutable);
		assertEpsilonEquals(origin, immutable);
	}

	@Test
	public final void testClonePoint() {
		Point2D origin = createPoint(23, 45);
		Tuple2D clone = origin.clone();
		assertNotNull(clone);
		assertNotSame(origin, clone);
		assertEpsilonEquals(origin.getX(), clone.getX());
		assertEpsilonEquals(origin.getY(), clone.getY());
	}

	@Test
	public final void operator_plusVector2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		Point2D r;
		
		r = point.operator_plus(vector1);
		assertFpPointEquals(1, 2, r);

		r = point.operator_plus(vector2);
		assertFpPointEquals(2, 4, r);

		r = point.operator_plus(vector3);
		assertFpPointEquals(2, -3, r);

		r = point2.operator_plus(vector1);
		assertFpPointEquals(3, 0, r);

		r = point2.operator_plus(vector2);
		assertFpPointEquals(4, 2, r);

		r = point2.operator_plus(vector3);
		assertFpPointEquals(4, -5, r);
	}

	@Test
	public final void operator_minusVector2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		Point2D r;
		
		r = point.operator_minus(vector1);
		assertFpPointEquals(1, 2, r);

		r = point.operator_minus(vector2);
		assertFpPointEquals(0, 0, r);

		r = point.operator_minus(vector3);
		assertFpPointEquals(0, 7, r);

		r = point2.operator_minus(vector1);
		assertFpPointEquals(3, 0, r);

		r = point2.operator_minus(vector2);
		assertFpPointEquals(2, -2, r);

		r = point2.operator_minus(vector3);
		assertFpPointEquals(2, 5, r);
	}

	@Test
	public final void operator_minusPoint2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point2D point = createPoint(0, 0);
		Point2D point2 = createPoint(1, 0);
		Point2D vector = createPoint(-1.2, -1.2);
		Point2D vector2 = createPoint(2.0, 1.5);
		Vector2D newVector;

		newVector = point.operator_minus(vector);
		assertFpVectorEquals(1.2, 1.2, newVector);

		newVector = point2.operator_minus(vector2);
		assertFpVectorEquals(-1.0, -1.5, newVector); 
	}

	@Test
	public final void operator_minusPoint2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point2D point = createPoint(0, 0);
		Point2D point2 = createPoint(1, 0);
		Point2D vector = createPoint(-1.2, -1.2);
		Point2D vector2 = createPoint(2.0, 1.5);
		Vector2D newVector;

		newVector = point.operator_minus(vector);
		assertFpVectorEquals(1, 1, newVector);

		newVector = point2.operator_minus(vector2);
		assertFpVectorEquals(-1, -2, newVector); 
	}

	@Test
	public final void operator_equalsTuple2D() {
		Point2D point = createPoint(49, -2);
		assertFalse(point.operator_equals(null));
		assertTrue(point.operator_equals(point));
		assertFalse(point.operator_equals(createPoint(49, -3)));
		assertFalse(point.operator_equals(createPoint(0, 0)));
		assertTrue(point.operator_equals(createPoint(49, -2)));
	}

	@Test
	public final void operator_notEqualsTuple2D() {
		Point2D point = createPoint(49, -2);
		assertTrue(point.operator_notEquals(null));
		assertFalse(point.operator_notEquals(point));
		assertTrue(point.operator_notEquals(createPoint(49, -3)));
		assertTrue(point.operator_notEquals(createPoint(0, 0)));
		assertFalse(point.operator_notEquals(createPoint(49, -2)));
	}

	@Test
	public final void testEqualsObject() {
		Point2D point = createPoint(49, -2);
		assertFalse(point.equals((Object) null));
		assertTrue(point.equals((Object) point));
		assertFalse(point.equals((Object) createPoint(49, -3)));
		assertFalse(point.equals((Object) createPoint(0, 0)));
		assertTrue(point.equals((Object) createPoint(49, -2)));
	}

	@Test
	public final void operator_upToPoint2D() {
		Point2D point = createPoint(0, 0);
		Point2D point2 = createPoint(0, 0);
		Point2D point3 = createPoint(1, 2);
		Point2D point4 = createPoint(1, 1);
		assertEpsilonEquals(0, point.operator_upTo(point2));
		assertEpsilonEquals(Math.sqrt(5), point.operator_upTo(point3));
		assertEpsilonEquals(Math.sqrt(2), point.operator_upTo(point4));
	}

	@Test
	public final void operator_elvisPoint2D() {
		Point2D orig1 = createPoint(45, -78);
		Point2D orig2 = createPoint(0, 0);
		Point2D param = createPoint(-5, -1.4);
		Point2D result;
		
		result = orig1.operator_elvis(null);
		assertSame(orig1, result);

		result = orig1.operator_elvis(orig1);
		assertSame(orig1, result);

		result = orig1.operator_elvis(param);
		assertSame(orig1, result);

		result = orig2.operator_elvis(null);
		assertNull(result);

		result = orig2.operator_elvis(orig2);
		assertSame(orig2, result);

		result = orig2.operator_elvis(param);
		assertSame(param, result);
	}

	@Test
	public abstract void operator_andShape2D();

	@Test
	public abstract void operator_upToShape2D();

	@Test
	public void addPoint2DVector2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.add(point, vector1);
		assertFpPointEquals(1, 2, point);

		point.add(point, vector2);
		assertFpPointEquals(2, 4, point);

		point.add(point, vector3);
		assertFpPointEquals(3, -1, point);

		point.add(point2, vector1);
		assertFpPointEquals(3, 0, point);

		point.add(point2, vector2);
		assertFpPointEquals(4, 2, point);

		point.add(point2, vector3);
		assertFpPointEquals(4, -5, point);
	}

	@Test
	public void addVector2DPoint2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.add(vector1, point);
		assertFpPointEquals(1, 2, point);

		point.add(vector2, point);
		assertFpPointEquals(2, 4, point);

		point.add(vector3, point);
		assertFpPointEquals(3, -1, point);

		point.add(vector1, point2);
		assertFpPointEquals(3, 0, point);

		point.add(vector2, point2);
		assertFpPointEquals(4, 2, point);

		point.add(vector3, point2);
		assertFpPointEquals(4, -5, point);
	}

	@Test
	public void addVector2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.add(vector1);
		assertFpPointEquals(1, 2, point);

		point.add(vector2);
		assertFpPointEquals(2, 4, point);

		point.add(vector3);
		assertFpPointEquals(3, -1, point);

		point.add(vector1);
		assertFpPointEquals(3, -1, point);

		point.add(vector2);
		assertFpPointEquals(4, 1, point);

		point.add(vector3);
		assertFpPointEquals(5, -4, point);
	}

	@Test
	public void scaleAddDoubleVector2DPoint2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.scaleAdd(2.5, vector1, point);
		assertFpPointEquals(1, 2, point);

		point.scaleAdd(-2.5, vector2, point);
		assertFpPointEquals(-1.5, -3, point);

		point.scaleAdd(2.5, vector3, point);
		assertFpPointEquals(1, -15.5, point);

		point.scaleAdd(-2.5, vector1, point2);
		assertFpPointEquals(3, 0, point);

		point.scaleAdd(2.5, vector2, point2);
		assertFpPointEquals(5.5, 5, point);

		point.scaleAdd(-2.5, vector3, point2);
		assertFpPointEquals(0.5, 12.5, point);
	}

	@Test
	public void scaleAddDoubleVector2DPoint2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.scaleAdd(2.5, vector1, point);
		assertIntPointEquals(1, 2, point);

		point.scaleAdd(-2.5, vector2, point);
		assertIntPointEquals(-1, -3, point);

		point.scaleAdd(2.5, vector3, point);
		assertIntPointEquals(2, -15, point);

		point.scaleAdd(-2.5, vector1, point2);
		assertIntPointEquals(3, 0, point);

		point.scaleAdd(2.5, vector2, point2);
		assertIntPointEquals(6, 5, point);

		point.scaleAdd(-2.5, vector3, point2);
		assertIntPointEquals(1, 13, point);
	}

	@Test
	public void scaleAddIntVector2DPoint2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.scaleAdd(2, vector1, point);
		assertFpPointEquals(1, 2, point);

		point.scaleAdd(-2, vector2, point);
		assertFpPointEquals(-1, -2, point);

		point.scaleAdd(2, vector3, point);
		assertFpPointEquals(1, -12, point);

		point.scaleAdd(-2, vector1, point2);
		assertFpPointEquals(3, 0, point);

		point.scaleAdd(2, vector2, point2);
		assertFpPointEquals(5, 4, point);

		point.scaleAdd(-2, vector3, point2);
		assertFpPointEquals(1, 10, point);
	}

	@Test
	public void scaleAddIntPoint2DVector2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.scaleAdd(2, point, vector1);
		assertFpPointEquals(2, 4, point);

		point.scaleAdd(-2, point, vector2);
		assertFpPointEquals(-3, -6, point);

		point.scaleAdd(2, point, vector3);
		assertFpPointEquals(-5, -17, point);

		point.scaleAdd(-2, point2, vector1);
		assertFpPointEquals(-6, 0, point);

		point.scaleAdd(2, point2, vector2);
		assertFpPointEquals(7, 2, point);

		point.scaleAdd(-2, point2, vector3);
		assertFpPointEquals(-5, -5, point);
	}

	@Test
	public void scaleAddDoublePoint2DVector2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.scaleAdd(2.5, point, vector1);
		assertFpPointEquals(2.5, 5, point);

		point.scaleAdd(-2.5, point, vector2);
		assertFpPointEquals(-5.25, -10.5, point);

		point.scaleAdd(2.5, point, vector3);
		assertFpPointEquals(-12.125, -31.25, point);

		point.scaleAdd(-2.5, point2, vector1);
		assertFpPointEquals(-7.5, 0, point);

		point.scaleAdd(2.5, point2, vector2);
		assertFpPointEquals(8.5, 2, point);

		point.scaleAdd(-2.5, point2, vector3);
		assertFpPointEquals(-6.5, -5, point);
	}

	@Test
	public void scaleAddDoublePoint2DVector2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.scaleAdd(2.5, point, vector1);
		assertIntPointEquals(3, 5, point);

		point.scaleAdd(-2.5, point, vector2);
		assertIntPointEquals(-6, -10, point);

		point.scaleAdd(2.5, point, vector3);
		assertIntPointEquals(-14, -30, point);

		point.scaleAdd(-2.5, point2, vector1);
		assertIntPointEquals(-7, 0, point);

		point.scaleAdd(2.5, point2, vector2);
		assertIntPointEquals(9, 2, point);

		point.scaleAdd(-2.5, point2, vector3);
		assertIntPointEquals(-6, -5, point);
	}

	@Test
	public void scaleAddIntVector2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.scaleAdd(2, vector1);
		assertFpPointEquals(2, 4, point);

		point.scaleAdd(-2, vector2);
		assertFpPointEquals(-3, -6, point);

		point.scaleAdd(2, vector3);
		assertFpPointEquals(-5, -17, point);

		point.scaleAdd(-2, vector1);
		assertFpPointEquals(10, 34, point);

		point.scaleAdd(2, vector2);
		assertFpPointEquals(21, 70, point);

		point.scaleAdd(-2, vector3);
		assertFpPointEquals(-41, -145, point);
	}

	@Test
	public void scaleAddDoubleVector2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.scaleAdd(2.5, vector1);
		assertFpPointEquals(2.5, 5, point);

		point.scaleAdd(-2.5, vector2);
		assertFpPointEquals(-5.25, -10.5, point);

		point.scaleAdd(2.5, vector3);
		assertFpPointEquals(-12.125, -31.25, point);

		point.scaleAdd(-2.5, vector1);
		assertFpPointEquals(30.312, 78.125, point);

		point.scaleAdd(2.5, vector2);
		assertFpPointEquals(76.781, 197.312, point);

		point.scaleAdd(-2.5, vector3);
		assertFpPointEquals(-190.95, -498.28, point);
	}

	@Test
	public void scaleAddDoubleVector2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.scaleAdd(2.5, vector1);
		assertIntPointEquals(3, 5, point);

		point.scaleAdd(-2.5, vector2);
		assertIntPointEquals(-6, -10, point);

		point.scaleAdd(2.5, vector3);
		assertIntPointEquals(-14, -30, point);

		point.scaleAdd(-2.5, vector1);
		assertIntPointEquals(35, 75, point);

		point.scaleAdd(2.5, vector2);
		assertIntPointEquals(89, 190, point);

		point.scaleAdd(-2.5, vector3);
		assertIntPointEquals(-221, -480, point);
	}

	@Test
	public void subPoint2DVector2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.sub(point, vector1);
		assertFpPointEquals(1, 2, point);

		point.sub(point, vector2);
		assertFpPointEquals(0, 0, point);

		point.sub(point, vector3);
		assertFpPointEquals(-1, 5, point);

		point.sub(point2, vector1);
		assertFpPointEquals(3, 0, point);

		point.sub(point2, vector2);
		assertFpPointEquals(2, -2, point);

		point.sub(point2, vector3);
		assertFpPointEquals(2, 5, point);
	}

	@Test
	public void subVector2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.sub(vector1);
		assertFpPointEquals(1, 2, point);

		point.sub(vector2);
		assertFpPointEquals(0, 0, point);

		point.sub(vector3);
		assertFpPointEquals(-1, 5, point);

		point.sub(vector1);
		assertFpPointEquals(-1, 5, point);

		point.sub(vector2);
		assertFpPointEquals(-2, 3, point);

		point.sub(vector3);
		assertFpPointEquals(-3, 8, point);
	}

	@Test
	public void operator_addVector2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		Point2D r;
		
		point.operator_add(vector1);
		assertFpPointEquals(1, 2, point);

		point.operator_add(vector2);
		assertFpPointEquals(2, 4, point);

		point.operator_add(vector3);
		assertFpPointEquals(3, -1, point);

		point.operator_add(vector1);
		assertFpPointEquals(3, -1, point);

		point.operator_add(vector2);
		assertFpPointEquals(4, 1, point);

		point.operator_add(vector3);
		assertFpPointEquals(5, -4, point);
	}

	@Test
	public void operator_removeVector2D() {
		Point2D point = createPoint(1, 2);
		Point2D point2 = createPoint(3, 0);
		Vector2D vector1 = createVector(0, 0);
		Vector2D vector2 = createVector(1, 2);
		Vector2D vector3 = createVector(1, -5);
		
		point.operator_remove(vector1);
		assertFpPointEquals(1, 2, point);

		point.operator_remove(vector2);
		assertFpPointEquals(0, 0, point);

		point.operator_remove(vector3);
		assertFpPointEquals(-1, 5, point);

		point.operator_remove(vector1);
		assertFpPointEquals(-1, 5, point);

		point.operator_remove(vector2);
		assertFpPointEquals(-2, 3, point);

		point.operator_remove(vector3);
		assertFpPointEquals(-3, 8, point);
	}

	@Test
	public void turnDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		
		Point2D p;
		
		p = createPoint(1, 0);
		p.turn(Math.PI/2);
		assertFpPointEquals(0, 1, p);

		p = createPoint(0, 1);
		p.turn(Math.PI/2);
		assertFpPointEquals(-1, 0, p);

		p = createPoint(-1, 0);
		p.turn(Math.PI/2);
		assertFpPointEquals(0, -1, p);

		p = createPoint(0, -1);
		p.turn(Math.PI/2);
		assertFpPointEquals(1, 0, p);

		p = createPoint(1, 0);
		p.turn(-Math.PI/2);
		assertFpPointEquals(0, -1, p);

		p = createPoint(0, -1);
		p.turn(-Math.PI/2);
		assertFpPointEquals(-1, 0, p);

		p = createPoint(-1, 0);
		p.turn(-Math.PI/2);
		assertFpPointEquals(0, 1, p);

		p = createPoint(0, 1);
		p.turn(-Math.PI/2);
		assertFpPointEquals(1, 0, p);

		p = createPoint(12, 0);
		p.turn(Math.PI/6);
		assertFpPointEquals(10.392304, 6, p);

		p = createPoint(12, 0);
		p.turn(-Math.PI/6);
		assertFpPointEquals(10.39230, -6, p);

		p = createPoint(-4, 18);
		p.turn(Math.PI/11);
		assertFpPointEquals(-8.90916, 16.14394, p);

		p = createPoint(-4, 18);
		p.turn(-Math.PI/11);
		assertFpPointEquals(1.23321, 18.39780, p);
	}

	@Test
	public void turnDoublePoint2D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		
		Point2D p = createPoint(0, 0);
		
		p.turn(Math.PI/2, createPoint(1, 0));
		assertFpPointEquals(0, 1, p);

		p.turn(Math.PI/2, createPoint(0, 1));
		assertFpPointEquals(-1, 0, p);

		p.turn(Math.PI/2, createPoint(-1, 0));
		assertFpPointEquals(0, -1, p);

		p.turn(Math.PI/2, createPoint(0, -1));
		assertFpPointEquals(1, 0, p);

		p.turn(-Math.PI/2, createPoint(1, 0));
		assertFpPointEquals(0, -1, p);

		p.turn(-Math.PI/2, createPoint(0, -1));
		assertFpPointEquals(-1, 0, p);

		p.turn(-Math.PI/2, createPoint(-1, 0));
		assertFpPointEquals(0, 1, p);

		p.turn(-Math.PI/2, createPoint(0, 1));
		assertFpPointEquals(1, 0, p);

		p.turn(Math.PI/6, createPoint(12, 0));
		assertFpPointEquals(10.392304, 6, p);

		p.turn(-Math.PI/6, createPoint(12, 0));
		assertFpPointEquals(10.39230, -6, p);

		p.turn(Math.PI/11, createPoint(-4, 18));
		assertFpPointEquals(-8.90916, 16.14394, p);

		p.turn(-Math.PI/11, createPoint(-4, 18));
		assertFpPointEquals(1.23321, 18.39780, p);
	}

	@Test
	public void turnDoublePoint2DPoint2D_origin_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		
		Point2D origin = createPoint(0, 0);
		Point2D p = createPoint(0, 0);
		
		p.turn(Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(0, 1, p);

		p.turn(Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-1, 0, p);

		p.turn(Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(0, -1, p);

		p.turn(Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(1, 0, p);

		p.turn(-Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(0, -1, p);

		p.turn(-Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-1, 0, p);

		p.turn(-Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(0, 1, p);

		p.turn(-Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(1, 0, p);

		p.turn(Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.392304, 6, p);

		p.turn(-Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.39230, -6, p);

		p.turn(Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-8.90916, 16.14394, p);

		p.turn(-Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(1.23321, 18.39780, p);
	}

	@Test
	public void turnDoublePoint2DPoint2D_aroundP_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		
		Point2D origin = createPoint(-45, 12);
		Point2D p = createPoint(0, 0);
		
		p.turn(Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(-33, 58, p);

		p.turn(Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-34, 57, p);

		p.turn(Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(-33, 56, p);

		p.turn(Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-32, 57, p);

		p.turn(-Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(-57, -34, p);

		p.turn(-Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-58, -33, p);

		p.turn(-Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(-57, -32, p);

		p.turn(-Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-56, -33, p);

		p.turn(Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.36345, 30.1077, p);

		p.turn(-Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(-1.63655, -26.89230, p);

		p.turn(Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-7.35118, 29.30799, p);

		p.turn(-Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-3.97039, 6.20592, p);
	}

	@Test
	public void turnLeftDouble_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		Point2D p;
		
		p = createPoint(1, 0);
		p.turnLeft(Math.PI/2);
		assertFpPointEquals(0, -1, p);

		p = createPoint(0, 1);
		p.turnLeft(Math.PI/2);
		assertFpPointEquals(1, 0, p);

		p = createPoint(-1, 0);
		p.turnLeft(Math.PI/2);
		assertFpPointEquals(0, 1, p);

		p = createPoint(0, -1);
		p.turnLeft(Math.PI/2);
		assertFpPointEquals(-1, 0, p);

		p = createPoint(1, 0);
		p.turnLeft(-Math.PI/2);
		assertFpPointEquals(0, 1, p);

		p = createPoint(0, -1);
		p.turnLeft(-Math.PI/2);
		assertFpPointEquals(1, 0, p);

		p = createPoint(-1, 0);
		p.turnLeft(-Math.PI/2);
		assertFpPointEquals(0, -1, p);

		p = createPoint(0, 1);
		p.turnLeft(-Math.PI/2);
		assertFpPointEquals(-1, 0, p);

		p = createPoint(12, 0);
		p.turnLeft(Math.PI/6);
		assertFpPointEquals(10.392304, -6, p);

		p = createPoint(12, 0);
		p.turnLeft(-Math.PI/6);
		assertFpPointEquals(10.39230, 6, p);

		p = createPoint(-4, 18);
		p.turnLeft(Math.PI/11);
		assertFpPointEquals(1.23321, 18.39780, p);

		p = createPoint(-4, 18);
		p.turnLeft(-Math.PI/11);
		assertFpPointEquals(-8.90916, 16.14394, p);
	}

	@Test
	public void turnLeftDouble_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D p;
		
		p = createPoint(1, 0);
		p.turnLeft(Math.PI/2);
		assertFpPointEquals(0, 1, p);

		p = createPoint(0, 1);
		p.turnLeft(Math.PI/2);
		assertFpPointEquals(-1, 0, p);

		p = createPoint(-1, 0);
		p.turnLeft(Math.PI/2);
		assertFpPointEquals(0, -1, p);

		p = createPoint(0, -1);
		p.turnLeft(Math.PI/2);
		assertFpPointEquals(1, 0, p);

		p = createPoint(1, 0);
		p.turnLeft(-Math.PI/2);
		assertFpPointEquals(0, -1, p);

		p = createPoint(0, -1);
		p.turnLeft(-Math.PI/2);
		assertFpPointEquals(-1, 0, p);

		p = createPoint(-1, 0);
		p.turnLeft(-Math.PI/2);
		assertFpPointEquals(0, 1, p);

		p = createPoint(0, 1);
		p.turnLeft(-Math.PI/2);
		assertFpPointEquals(1, 0, p);

		p = createPoint(12, 0);
		p.turnLeft(Math.PI/6);
		assertFpPointEquals(10.392304, 6, p);

		p = createPoint(12, 0);
		p.turnLeft(-Math.PI/6);
		assertFpPointEquals(10.39230, -6, p);

		p = createPoint(-4, 18);
		p.turnLeft(Math.PI/11);
		assertFpPointEquals(-8.90916, 16.14394, p);

		p = createPoint(-4, 18);
		p.turnLeft(-Math.PI/11);
		assertFpPointEquals(1.23321, 18.39780, p);
	}

	@Test
	public void turnLeftDoublePoint2D_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D p = createPoint(0, 0);
		
		p.turnLeft(Math.PI/2, createPoint(1, 0));
		assertFpPointEquals(0, -1, p);

		p.turnLeft(Math.PI/2, createPoint(0, 1));
		assertFpPointEquals(1, 0, p);

		p.turnLeft(Math.PI/2, createPoint(-1, 0));
		assertFpPointEquals(0, 1, p);

		p.turnLeft(Math.PI/2, createPoint(0, -1));
		assertFpPointEquals(-1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(1, 0));
		assertFpPointEquals(0, 1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, -1));
		assertFpPointEquals(1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(-1, 0));
		assertFpPointEquals(0, -1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, 1));
		assertFpPointEquals(-1, 0, p);

		p.turnLeft(Math.PI/6, createPoint(12, 0));
		assertFpPointEquals(10.392304, -6, p);

		p.turnLeft(-Math.PI/6, createPoint(12, 0));
		assertFpPointEquals(10.39230, 6, p);

		p.turnLeft(Math.PI/11, createPoint(-4, 18));
		assertFpPointEquals(1.23321, 18.39780, p);

		p.turnLeft(-Math.PI/11, createPoint(-4, 18));
		assertFpPointEquals(-8.90916, 16.14394, p);
	}

	@Test
	public void turnLeftDoublePoint2D_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D p = createPoint(0, 0);
		
		p.turnLeft(Math.PI/2, createPoint(1, 0));
		assertFpPointEquals(0, 1, p);

		p.turnLeft(Math.PI/2, createPoint(0, 1));
		assertFpPointEquals(-1, 0, p);

		p.turnLeft(Math.PI/2, createPoint(-1, 0));
		assertFpPointEquals(0, -1, p);

		p.turnLeft(Math.PI/2, createPoint(0, -1));
		assertFpPointEquals(1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(1, 0));
		assertFpPointEquals(0, -1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, -1));
		assertFpPointEquals(-1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(-1, 0));
		assertFpPointEquals(0, 1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, 1));
		assertFpPointEquals(1, 0, p);

		p.turnLeft(Math.PI/6, createPoint(12, 0));
		assertFpPointEquals(10.392304, 6, p);

		p.turnLeft(-Math.PI/6, createPoint(12, 0));
		assertFpPointEquals(10.39230, -6, p);

		p.turnLeft(Math.PI/11, createPoint(-4, 18));
		assertFpPointEquals(-8.90916, 16.14394, p);

		p.turnLeft(-Math.PI/11, createPoint(-4, 18));
		assertFpPointEquals(1.23321, 18.39780, p);
	}

	@Test
	public void turnLeftDoublePoint2DPoint2D_origin_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		Point2D p = createPoint(0, 0);
		
		p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(0, -1, p);

		p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(1, 0, p);

		p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(0, 1, p);

		p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(0, 1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(0, -1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-1, 0, p);

		p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.392304, -6, p);

		p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.39230, 6, p);

		p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(1.23321, 18.39780, p);

		p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-8.90916, 16.14394, p);
	}

	@Test
	public void turnLeftDoublePoint2DPoint2D_origin_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		Point2D p = createPoint(0, 0);
		
		p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(0, 1, p);

		p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-1, 0, p);

		p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(0, -1, p);

		p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(0, -1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(0, 1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(1, 0, p);

		p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.392304, 6, p);

		p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.39230, -6, p);

		p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-8.90916, 16.14394, p);

		p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(1.23321, 18.39780, p);
	}

	@Test
	public void turnLeftDoublePoint2DPoint2D_aroundP_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		Point2D p = createPoint(0, 0);
		
		p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(-57, -34, p);

		p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-56, -33, p);

		p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(-57, -32, p);

		p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-58, -33, p);

		p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(-33, 58, p);

		p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-32, 57, p);

		p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(-33, 56, p);

		p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-34, 57, p);

		p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(-1.63655, -26.89230, p);

		p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.36345, 30.1077, p);

		p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-3.97039, 6.20592, p);

		p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-7.35118, 29.30799, p);
	}

	@Test
	public void turnLeftDoublePoint2DPoint2D_aroundP_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		Point2D p = createPoint(0, 0);
		
		p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(-33, 58, p);

		p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-34, 57, p);

		p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(-33, 56, p);

		p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-32, 57, p);

		p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(-57, -34, p);

		p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-58, -33, p);

		p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(-57, -32, p);

		p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-56, -33, p);

		p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.36345, 30.1077, p);

		p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(-1.63655, -26.89230, p);

		p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-7.35118, 29.30799, p);

		p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-3.97039, 6.20592, p);
	}

	@Test
	public void turnRightDouble_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		Point2D p;
		
		p = createPoint(1, 0);
		p.turnRight(Math.PI/2);
		assertFpPointEquals(0, 1, p);

		p = createPoint(0, 1);
		p.turnRight(Math.PI/2);
		assertFpPointEquals(-1, 0, p);

		p = createPoint(-1, 0);
		p.turnRight(Math.PI/2);
		assertFpPointEquals(0, -1, p);

		p = createPoint(0, -1);
		p.turnRight(Math.PI/2);
		assertFpPointEquals(1, 0, p);

		p = createPoint(1, 0);
		p.turnRight(-Math.PI/2);
		assertFpPointEquals(0, -1, p);

		p = createPoint(0, -1);
		p.turnRight(-Math.PI/2);
		assertFpPointEquals(-1, 0, p);

		p = createPoint(-1, 0);
		p.turnRight(-Math.PI/2);
		assertFpPointEquals(0, 1, p);

		p = createPoint(0, 1);
		p.turnRight(-Math.PI/2);
		assertFpPointEquals(1, 0, p);

		p = createPoint(12, 0);
		p.turnRight(Math.PI/6);
		assertFpPointEquals(10.39230, 6, p);

		p = createPoint(12, 0);
		p.turnRight(-Math.PI/6);
		assertFpPointEquals(10.392304, -6, p);

		p = createPoint(-4, 18);
		p.turnRight(Math.PI/11);
		assertFpPointEquals(-8.90916, 16.14394, p);

		p = createPoint(-4, 18);
		p.turnRight(-Math.PI/11);
		assertFpPointEquals(1.23321, 18.39780, p);
	}

	@Test
	public void turnRightDouble_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D p;
		
		p = createPoint(1, 0);
		p.turnRight(Math.PI/2);
		assertFpPointEquals(0, -1, p);

		p = createPoint(0, 1);
		p.turnRight(Math.PI/2);
		assertFpPointEquals(1, 0, p);

		p = createPoint(-1, 0);
		p.turnRight(Math.PI/2);
		assertFpPointEquals(0, 1, p);

		p = createPoint(0, -1);
		p.turnRight(Math.PI/2);
		assertFpPointEquals(-1, 0, p);

		p = createPoint(1, 0);
		p.turnRight(-Math.PI/2);
		assertFpPointEquals(0, 1, p);

		p = createPoint(0, -1);
		p.turnRight(-Math.PI/2);
		assertFpPointEquals(1, 0, p);

		p = createPoint(-1, 0);
		p.turnRight(-Math.PI/2);
		assertFpPointEquals(0, -1, p);

		p = createPoint(0, 1);
		p.turnRight(-Math.PI/2);
		assertFpPointEquals(-1, 0, p);

		p = createPoint(12, 0);
		p.turnRight(Math.PI/6);
		assertFpPointEquals(10.39230, -6, p);

		p = createPoint(12, 0);
		p.turnRight(-Math.PI/6);
		assertFpPointEquals(10.392304, 6, p);

		p = createPoint(-4, 18);
		p.turnRight(Math.PI/11);
		assertFpPointEquals(1.23321, 18.39780, p);

		p = createPoint(-4, 18);
		p.turnRight(-Math.PI/11);
		assertFpPointEquals(-8.90916, 16.14394, p);
	}

	@Test
	public void turnRightDoublePoint2D_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D p = createPoint(0, 0);
		
		p.turnRight(Math.PI/2, createPoint(1, 0));
		assertFpPointEquals(0, 1, p);

		p.turnRight(Math.PI/2, createPoint(0, 1));
		assertFpPointEquals(-1, 0, p);

		p.turnRight(Math.PI/2, createPoint(-1, 0));
		assertFpPointEquals(0, -1, p);

		p.turnRight(Math.PI/2, createPoint(0, -1));
		assertFpPointEquals(1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(1, 0));
		assertFpPointEquals(0, -1, p);

		p.turnRight(-Math.PI/2, createPoint(0, -1));
		assertFpPointEquals(-1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(-1, 0));
		assertFpPointEquals(0, 1, p);

		p.turnRight(-Math.PI/2, createPoint(0, 1));
		assertFpPointEquals(1, 0, p);

		p.turnRight(Math.PI/6, createPoint(12, 0));
		assertFpPointEquals(10.392304, 6, p);

		p.turnRight(-Math.PI/6, createPoint(12, 0));
		assertFpPointEquals(10.39230, -6, p);

		p.turnRight(Math.PI/11, createPoint(-4, 18));
		assertFpPointEquals(-8.90916, 16.14394, p);

		p.turnRight(-Math.PI/11, createPoint(-4, 18));
		assertFpPointEquals(1.23321, 18.39780, p);
	}

	@Test
	public void turnRightDoublePoint2D_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D p = createPoint(0, 0);
		
		p.turnRight(Math.PI/2, createPoint(1, 0));
		assertFpPointEquals(0, -1, p);

		p.turnRight(Math.PI/2, createPoint(0, 1));
		assertFpPointEquals(1, 0, p);

		p.turnRight(Math.PI/2, createPoint(-1, 0));
		assertFpPointEquals(0, 1, p);

		p.turnRight(Math.PI/2, createPoint(0, -1));
		assertFpPointEquals(-1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(1, 0));
		assertFpPointEquals(0, 1, p);

		p.turnRight(-Math.PI/2, createPoint(0, -1));
		assertFpPointEquals(1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(-1, 0));
		assertFpPointEquals(0, -1, p);

		p.turnRight(-Math.PI/2, createPoint(0, 1));
		assertFpPointEquals(-1, 0, p);

		p.turnRight(Math.PI/6, createPoint(12, 0));
		assertFpPointEquals(10.392304, -6, p);

		p.turnRight(-Math.PI/6, createPoint(12, 0));
		assertFpPointEquals(10.39230, 6, p);

		p.turnRight(Math.PI/11, createPoint(-4, 18));
		assertFpPointEquals(1.23321, 18.39780, p);

		p.turnRight(-Math.PI/11, createPoint(-4, 18));
		assertFpPointEquals(-8.90916, 16.14394, p);
	}

	@Test
	public void turnRightDoublePoint2DPoint2D_origin_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		Point2D p = createPoint(0, 0);
		
		p.turnRight(Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(0, 1, p);

		p.turnRight(Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-1, 0, p);

		p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(0, -1, p);

		p.turnRight(Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(0, -1, p);

		p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(0, 1, p);

		p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(1, 0, p);

		p.turnRight(Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.39230, 6, p);

		p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.392304, -6, p);

		p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-8.90916, 16.14394, p);

		p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(1.23321, 18.39780, p);
	}

	@Test
	public void turnRightDoublePoint2DPoint2D_origin_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		Point2D p = createPoint(0, 0);
		
		p.turnRight(Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(0, -1, p);

		p.turnRight(Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(1, 0, p);

		p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(0, 1, p);

		p.turnRight(Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(0, 1, p);

		p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(0, -1, p);

		p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-1, 0, p);

		p.turnRight(Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.39230, -6, p);

		p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.392304, 6, p);

		p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(1.23321, 18.39780, p);

		p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-8.90916, 16.14394, p);
	}

	@Test
	public void turnRightDoublePoint2DPoint2D_aroundP_iffp_leftHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		Point2D p = createPoint(0, 0);
		
		p.turnRight(Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(-33, 58, p);

		p.turnRight(Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-34, 57, p);

		p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(-33, 56, p);

		p.turnRight(Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-32, 57, p);

		p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(-57, -34, p);

		p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-58, -33, p);

		p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(-57, -32, p);

		p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-56, -33, p);

		p.turnRight(Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.36345, 30.1077, p);

		p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(-1.63655, -26.89230, p);

		p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-7.35118, 29.30799, p);

		p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-3.97039, 6.20592, p);
	}

	@Test
	public void turnRightDoublePoint2DPoint2D_aroundP_iffp_rightHanded() {
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		Point2D p = createPoint(0, 0);
		
		p.turnRight(Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(-57, -34, p);

		p.turnRight(Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-56, -33, p);

		p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(-57, -32, p);

		p.turnRight(Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-58, -33, p);

		p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
		assertFpPointEquals(-33, 58, p);

		p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
		assertFpPointEquals(-32, 57, p);

		p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
		assertFpPointEquals(-33, 56, p);

		p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
		assertFpPointEquals(-34, 57, p);

		p.turnRight(Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(-1.63655, -26.89230, p);

		p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
		assertFpPointEquals(10.36345, 30.1077, p);

		p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-3.97039, 6.20592, p);

		p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
		assertFpPointEquals(-7.35118, 29.30799, p);
	}

	@Test
	public void turnDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		
		Point2D p;
		
		p = createPoint(1, 0);
		p.turn(Math.PI/2);
		assertIntPointEquals(0, 1, p);

		p = createPoint(0, 1);
		p.turn(Math.PI/2);
		assertIntPointEquals(-1, 0, p);

		p = createPoint(-1, 0);
		p.turn(Math.PI/2);
		assertIntPointEquals(0, -1, p);

		p = createPoint(0, -1);
		p.turn(Math.PI/2);
		assertIntPointEquals(1, 0, p);

		p = createPoint(1, 0);
		p.turn(-Math.PI/2);
		assertIntPointEquals(0, -1, p);

		p = createPoint(0, -1);
		p.turn(-Math.PI/2);
		assertIntPointEquals(-1, 0, p);

		p = createPoint(-1, 0);
		p.turn(-Math.PI/2);
		assertIntPointEquals(0, 1, p);

		p = createPoint(0, 1);
		p.turn(-Math.PI/2);
		assertIntPointEquals(1, 0, p);

		p = createPoint(12, 0);
		p.turn(Math.PI/6);
		assertIntPointEquals(10, 6, p);

		p = createPoint(12, 0);
		p.turn(-Math.PI/6);
		assertIntPointEquals(10, -6, p);

		p = createPoint(-4, 18);
		p.turn(Math.PI/11);
		assertIntPointEquals(-9, 16, p);

		p = createPoint(-4, 18);
		p.turn(-Math.PI/11);
		assertIntPointEquals(1, 18, p);
	}

	@Test
	public void turnDoublePoint2D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		
		Point2D p = createPoint(0, 0);
		
		p.turn(Math.PI/2, createPoint(1, 0));
		assertIntPointEquals(0, 1, p);

		p.turn(Math.PI/2, createPoint(0, 1));
		assertIntPointEquals(-1, 0, p);

		p.turn(Math.PI/2, createPoint(-1, 0));
		assertIntPointEquals(0, -1, p);

		p.turn(Math.PI/2, createPoint(0, -1));
		assertIntPointEquals(1, 0, p);

		p.turn(-Math.PI/2, createPoint(1, 0));
		assertIntPointEquals(0, -1, p);

		p.turn(-Math.PI/2, createPoint(0, -1));
		assertIntPointEquals(-1, 0, p);

		p.turn(-Math.PI/2, createPoint(-1, 0));
		assertIntPointEquals(0, 1, p);

		p.turn(-Math.PI/2, createPoint(0, 1));
		assertIntPointEquals(1, 0, p);

		p.turn(Math.PI/6, createPoint(12, 0));
		assertIntPointEquals(10, 6, p);

		p.turn(-Math.PI/6, createPoint(12, 0));
		assertIntPointEquals(10, -6, p);

		p.turn(Math.PI/11, createPoint(-4, 18));
		assertIntPointEquals(-9, 16, p);

		p.turn(-Math.PI/11, createPoint(-4, 18));
		assertIntPointEquals(1, 18, p);
	}

	@Test
	public void turnDoublePoint2DPoint2D_origin_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		
		Point2D origin = createPoint(0, 0);
		Point2D p = createPoint(0, 0);
		
		p.turn(Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(0, 1, p);

		p.turn(Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-1, 0, p);

		p.turn(Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(0, -1, p);

		p.turn(Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(1, 0, p);

		p.turn(-Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(0, -1, p);

		p.turn(-Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-1, 0, p);

		p.turn(-Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(0, 1, p);

		p.turn(-Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(1, 0, p);

		p.turn(Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, 6, p);

		p.turn(-Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, -6, p);

		p.turn(Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-9, 16, p);

		p.turn(-Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(1, 18, p);
	}

	@Test
	public void turnDoublePoint2DPoint2D_aroundP_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		
		Point2D origin = createPoint(-45, 12);
		Point2D p = createPoint(0, 0);
		
		p.turn(Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(-33, 58, p);

		p.turn(Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-34, 57, p);

		p.turn(Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(-33, 56, p);

		p.turn(Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-32, 57, p);

		p.turn(-Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(-57, -34, p);

		p.turn(-Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-58, -33, p);

		p.turn(-Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(-57, -32, p);

		p.turn(-Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-56, -33, p);

		p.turn(Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, 30, p);

		p.turn(-Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(-2, -27, p);

		p.turn(Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-7, 29, p);

		p.turn(-Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-4, 6, p);
	}

	@Test
	public void turnLeftDouble_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		Point2D p;
		
		p = createPoint(1, 0);
		p.turnLeft(Math.PI/2);
		assertIntPointEquals(0, -1, p);

		p = createPoint(0, 1);
		p.turnLeft(Math.PI/2);
		assertIntPointEquals(1, 0, p);

		p = createPoint(-1, 0);
		p.turnLeft(Math.PI/2);
		assertIntPointEquals(0, 1, p);

		p = createPoint(0, -1);
		p.turnLeft(Math.PI/2);
		assertIntPointEquals(-1, 0, p);

		p = createPoint(1, 0);
		p.turnLeft(-Math.PI/2);
		assertIntPointEquals(0, 1, p);

		p = createPoint(0, -1);
		p.turnLeft(-Math.PI/2);
		assertIntPointEquals(1, 0, p);

		p = createPoint(-1, 0);
		p.turnLeft(-Math.PI/2);
		assertIntPointEquals(0, -1, p);

		p = createPoint(0, 1);
		p.turnLeft(-Math.PI/2);
		assertIntPointEquals(-1, 0, p);

		p = createPoint(12, 0);
		p.turnLeft(Math.PI/6);
		assertIntPointEquals(10, -6, p);

		p = createPoint(12, 0);
		p.turnLeft(-Math.PI/6);
		assertIntPointEquals(10, 6, p);

		p = createPoint(-4, 18);
		p.turnLeft(Math.PI/11);
		assertIntPointEquals(1, 18, p);

		p = createPoint(-4, 18);
		p.turnLeft(-Math.PI/11);
		assertIntPointEquals(-9, 16, p);
	}

	@Test
	public void turnLeftDouble_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D p;
		
		p = createPoint(1, 0);
		p.turnLeft(Math.PI/2);
		assertIntPointEquals(0, 1, p);

		p = createPoint(0, 1);
		p.turnLeft(Math.PI/2);
		assertIntPointEquals(-1, 0, p);

		p = createPoint(-1, 0);
		p.turnLeft(Math.PI/2);
		assertIntPointEquals(0, -1, p);

		p = createPoint(0, -1);
		p.turnLeft(Math.PI/2);
		assertIntPointEquals(1, 0, p);

		p = createPoint(1, 0);
		p.turnLeft(-Math.PI/2);
		assertIntPointEquals(0, -1, p);

		p = createPoint(0, -1);
		p.turnLeft(-Math.PI/2);
		assertIntPointEquals(-1, 0, p);

		p = createPoint(-1, 0);
		p.turnLeft(-Math.PI/2);
		assertIntPointEquals(0, 1, p);

		p = createPoint(0, 1);
		p.turnLeft(-Math.PI/2);
		assertIntPointEquals(1, 0, p);

		p = createPoint(12, 0);
		p.turnLeft(Math.PI/6);
		assertIntPointEquals(10, 6, p);

		p = createPoint(12, 0);
		p.turnLeft(-Math.PI/6);
		assertIntPointEquals(10, -6, p);

		p = createPoint(-4, 18);
		p.turnLeft(Math.PI/11);
		assertIntPointEquals(-9, 16, p);

		p = createPoint(-4, 18);
		p.turnLeft(-Math.PI/11);
		assertIntPointEquals(1, 18, p);
	}

	@Test
	public void turnLeftDoublePoint2D_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D p = createPoint(0, 0);
		
		p.turnLeft(Math.PI/2, createPoint(1, 0));
		assertIntPointEquals(0, -1, p);

		p.turnLeft(Math.PI/2, createPoint(0, 1));
		assertIntPointEquals(1, 0, p);

		p.turnLeft(Math.PI/2, createPoint(-1, 0));
		assertIntPointEquals(0, 1, p);

		p.turnLeft(Math.PI/2, createPoint(0, -1));
		assertIntPointEquals(-1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(1, 0));
		assertIntPointEquals(0, 1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, -1));
		assertIntPointEquals(1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(-1, 0));
		assertIntPointEquals(0, -1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, 1));
		assertIntPointEquals(-1, 0, p);

		p.turnLeft(Math.PI/6, createPoint(12, 0));
		assertIntPointEquals(10, -6, p);

		p.turnLeft(-Math.PI/6, createPoint(12, 0));
		assertIntPointEquals(10, 6, p);

		p.turnLeft(Math.PI/11, createPoint(-4, 18));
		assertIntPointEquals(1, 18, p);

		p.turnLeft(-Math.PI/11, createPoint(-4, 18));
		assertIntPointEquals(-9, 16, p);
	}

	@Test
	public void turnLeftDoublePoint2D_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D p = createPoint(0, 0);
		
		p.turnLeft(Math.PI/2, createPoint(1, 0));
		assertIntPointEquals(0, 1, p);

		p.turnLeft(Math.PI/2, createPoint(0, 1));
		assertIntPointEquals(-1, 0, p);

		p.turnLeft(Math.PI/2, createPoint(-1, 0));
		assertIntPointEquals(0, -1, p);

		p.turnLeft(Math.PI/2, createPoint(0, -1));
		assertIntPointEquals(1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(1, 0));
		assertIntPointEquals(0, -1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, -1));
		assertIntPointEquals(-1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(-1, 0));
		assertIntPointEquals(0, 1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, 1));
		assertIntPointEquals(1, 0, p);

		p.turnLeft(Math.PI/6, createPoint(12, 0));
		assertIntPointEquals(10, 6, p);

		p.turnLeft(-Math.PI/6, createPoint(12, 0));
		assertIntPointEquals(10, -6, p);

		p.turnLeft(Math.PI/11, createPoint(-4, 18));
		assertIntPointEquals(-9, 16, p);

		p.turnLeft(-Math.PI/11, createPoint(-4, 18));
		assertIntPointEquals(1, 18, p);
	}

	@Test
	public void turnLeftDoublePoint2DPoint2D_origin_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		Point2D p = createPoint(0, 0);
		
		p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(0, -1, p);

		p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(1, 0, p);

		p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(0, 1, p);

		p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(0, 1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(0, -1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-1, 0, p);

		p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, -6, p);

		p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, 6, p);

		p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(1, 18, p);

		p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-9, 16, p);
	}

	@Test
	public void turnLeftDoublePoint2DPoint2D_origin_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		Point2D p = createPoint(0, 0);
		
		p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(0, 1, p);

		p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-1, 0, p);

		p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(0, -1, p);

		p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(0, -1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-1, 0, p);

		p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(0, 1, p);

		p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(1, 0, p);

		p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, 6, p);

		p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, -6, p);

		p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-9, 16, p);

		p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(1, 18, p);
	}

	@Test
	public void turnLeftDoublePoint2DPoint2D_aroundP_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		Point2D p = createPoint(0, 0);
		
		p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(-57, -34, p);

		p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-56, -33, p);

		p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(-57, -32, p);

		p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-58, -33, p);

		p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(-33, 58, p);

		p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-32, 57, p);

		p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(-33, 56, p);

		p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-34, 57, p);

		p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(-2, -27, p);

		p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, 30, p);

		p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-4, 6, p);

		p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-7, 29, p);
	}

	@Test
	public void turnLeftDoublePoint2DPoint2D_aroundP_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		Point2D p = createPoint(0, 0);
		
		p.turnLeft(Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(-33, 58, p);

		p.turnLeft(Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-34, 57, p);

		p.turnLeft(Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(-33, 56, p);

		p.turnLeft(Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-32, 57, p);

		p.turnLeft(-Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(-57, -34, p);

		p.turnLeft(-Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-58, -33, p);

		p.turnLeft(-Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(-57, -32, p);

		p.turnLeft(-Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-56, -33, p);

		p.turnLeft(Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, 30, p);

		p.turnLeft(-Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(-2, -27, p);

		p.turnLeft(Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-7, 29, p);

		p.turnLeft(-Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-4, 6, p);
	}

	@Test
	public void turnRightDouble_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		Point2D p;
		
		p = createPoint(1, 0);
		p.turnRight(Math.PI/2);
		assertIntPointEquals(0, 1, p);

		p = createPoint(0, 1);
		p.turnRight(Math.PI/2);
		assertIntPointEquals(-1, 0, p);

		p = createPoint(-1, 0);
		p.turnRight(Math.PI/2);
		assertIntPointEquals(0, -1, p);

		p = createPoint(0, -1);
		p.turnRight(Math.PI/2);
		assertIntPointEquals(1, 0, p);

		p = createPoint(1, 0);
		p.turnRight(-Math.PI/2);
		assertIntPointEquals(0, -1, p);

		p = createPoint(0, -1);
		p.turnRight(-Math.PI/2);
		assertIntPointEquals(-1, 0, p);

		p = createPoint(-1, 0);
		p.turnRight(-Math.PI/2);
		assertIntPointEquals(0, 1, p);

		p = createPoint(0, 1);
		p.turnRight(-Math.PI/2);
		assertIntPointEquals(1, 0, p);

		p = createPoint(12, 0);
		p.turnRight(Math.PI/6);
		assertIntPointEquals(10, 6, p);

		p = createPoint(12, 0);
		p.turnRight(-Math.PI/6);
		assertIntPointEquals(10, -6, p);

		p = createPoint(-4, 18);
		p.turnRight(Math.PI/11);
		assertIntPointEquals(-9, 16, p);

		p = createPoint(-4, 18);
		p.turnRight(-Math.PI/11);
		assertIntPointEquals(1, 18, p);
	}

	@Test
	public void turnRightDouble_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D p;
		
		p = createPoint(1, 0);
		p.turnRight(Math.PI/2);
		assertIntPointEquals(0, -1, p);

		p = createPoint(0, 1);
		p.turnRight(Math.PI/2);
		assertIntPointEquals(1, 0, p);

		p = createPoint(-1, 0);
		p.turnRight(Math.PI/2);
		assertIntPointEquals(0, 1, p);

		p = createPoint(0, -1);
		p.turnRight(Math.PI/2);
		assertIntPointEquals(-1, 0, p);

		p = createPoint(1, 0);
		p.turnRight(-Math.PI/2);
		assertIntPointEquals(0, 1, p);

		p = createPoint(0, -1);
		p.turnRight(-Math.PI/2);
		assertIntPointEquals(1, 0, p);

		p = createPoint(-1, 0);
		p.turnRight(-Math.PI/2);
		assertIntPointEquals(0, -1, p);

		p = createPoint(0, 1);
		p.turnRight(-Math.PI/2);
		assertIntPointEquals(-1, 0, p);

		p = createPoint(12, 0);
		p.turnRight(Math.PI/6);
		assertIntPointEquals(10, -6, p);

		p = createPoint(12, 0);
		p.turnRight(-Math.PI/6);
		assertIntPointEquals(10, 6, p);

		p = createPoint(-4, 18);
		p.turnRight(Math.PI/11);
		assertIntPointEquals(1, 18, p);

		p = createPoint(-4, 18);
		p.turnRight(-Math.PI/11);
		assertIntPointEquals(-9, 16, p);
	}

	@Test
	public void turnRightDoublePoint2D_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D p = createPoint(0, 0);
		
		p.turnRight(Math.PI/2, createPoint(1, 0));
		assertIntPointEquals(0, 1, p);

		p.turnRight(Math.PI/2, createPoint(0, 1));
		assertIntPointEquals(-1, 0, p);

		p.turnRight(Math.PI/2, createPoint(-1, 0));
		assertIntPointEquals(0, -1, p);

		p.turnRight(Math.PI/2, createPoint(0, -1));
		assertIntPointEquals(1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(1, 0));
		assertIntPointEquals(0, -1, p);

		p.turnRight(-Math.PI/2, createPoint(0, -1));
		assertIntPointEquals(-1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(-1, 0));
		assertIntPointEquals(0, 1, p);

		p.turnRight(-Math.PI/2, createPoint(0, 1));
		assertIntPointEquals(1, 0, p);

		p.turnRight(Math.PI/6, createPoint(12, 0));
		assertIntPointEquals(10, 6, p);

		p.turnRight(-Math.PI/6, createPoint(12, 0));
		assertIntPointEquals(10, -6, p);

		p.turnRight(Math.PI/11, createPoint(-4, 18));
		assertIntPointEquals(-9, 16, p);

		p.turnRight(-Math.PI/11, createPoint(-4, 18));
		assertIntPointEquals(1, 18, p);
	}

	@Test
	public void turnRightDoublePoint2D_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Assume.assumeFalse(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D p = createPoint(0, 0);
		
		p.turnRight(Math.PI/2, createPoint(1, 0));
		assertIntPointEquals(0, -1, p);

		p.turnRight(Math.PI/2, createPoint(0, 1));
		assertIntPointEquals(1, 0, p);

		p.turnRight(Math.PI/2, createPoint(-1, 0));
		assertIntPointEquals(0, 1, p);

		p.turnRight(Math.PI/2, createPoint(0, -1));
		assertIntPointEquals(-1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(1, 0));
		assertIntPointEquals(0, 1, p);

		p.turnRight(-Math.PI/2, createPoint(0, -1));
		assertIntPointEquals(1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(-1, 0));
		assertIntPointEquals(0, -1, p);

		p.turnRight(-Math.PI/2, createPoint(0, 1));
		assertIntPointEquals(-1, 0, p);

		p.turnRight(Math.PI/6, createPoint(12, 0));
		assertIntPointEquals(10, -6, p);

		p.turnRight(-Math.PI/6, createPoint(12, 0));
		assertIntPointEquals(10, 6, p);

		p.turnRight(Math.PI/11, createPoint(-4, 18));
		assertIntPointEquals(1, 18, p);

		p.turnRight(-Math.PI/11, createPoint(-4, 18));
		assertIntPointEquals(-9, 16, p);
	}

	@Test
	public void turnRightDoublePoint2DPoint2D_origin_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		Point2D p = createPoint(0, 0);
		
		p.turnRight(Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(0, 1, p);

		p.turnRight(Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-1, 0, p);

		p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(0, -1, p);

		p.turnRight(Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(0, -1, p);

		p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(0, 1, p);

		p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(1, 0, p);

		p.turnRight(Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, 6, p);

		p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, -6, p);

		p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-9, 16, p);

		p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(1, 18, p);
	}

	@Test
	public void turnRightDoublePoint2DPoint2D_origin_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		Point2D p = createPoint(0, 0);
		
		p.turnRight(Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(0, -1, p);

		p.turnRight(Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(1, 0, p);

		p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(0, 1, p);

		p.turnRight(Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(0, 1, p);

		p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(1, 0, p);

		p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(0, -1, p);

		p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-1, 0, p);

		p.turnRight(Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, -6, p);

		p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, 6, p);

		p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(1, 18, p);

		p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-9, 16, p);
	}

	@Test
	public void turnRightDoublePoint2DPoint2D_aroundP_ifi_leftHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		Point2D p = createPoint(0, 0);
		
		p.turnRight(Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(-33, 58, p);

		p.turnRight(Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-34, 57, p);

		p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(-33, 56, p);

		p.turnRight(Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-32, 57, p);

		p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(-57, -34, p);

		p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-58, -33, p);

		p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(-57, -32, p);

		p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-56, -33, p);

		p.turnRight(Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, 30, p);

		p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(-2, -27, p);

		p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-7, 29, p);

		p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-4, 6, p);
	}

	@Test
	public void turnRightDoublePoint2DPoint2D_aroundP_ifi_rightHanded() {
		Assume.assumeTrue(isIntCoordinates());		
		Assume.assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		Point2D p = createPoint(0, 0);
		
		p.turnRight(Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(-57, -34, p);

		p.turnRight(Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-56, -33, p);

		p.turnRight(Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(-57, -32, p);

		p.turnRight(Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-58, -33, p);

		p.turnRight(-Math.PI/2, createPoint(1, 0), origin);
		assertIntPointEquals(-33, 58, p);

		p.turnRight(-Math.PI/2, createPoint(0, -1), origin);
		assertIntPointEquals(-32, 57, p);

		p.turnRight(-Math.PI/2, createPoint(-1, 0), origin);
		assertIntPointEquals(-33, 56, p);

		p.turnRight(-Math.PI/2, createPoint(0, 1), origin);
		assertIntPointEquals(-34, 57, p);

		p.turnRight(Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(-2, -27, p);

		p.turnRight(-Math.PI/6, createPoint(12, 0), origin);
		assertIntPointEquals(10, 30, p);

		p.turnRight(Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-4, 6, p);

		p.turnRight(-Math.PI/11, createPoint(-4, 18), origin);
		assertIntPointEquals(-7, 29, p);
	}

}
