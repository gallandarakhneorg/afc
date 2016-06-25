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

package org.arakhne.afc.math.geometry.d3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Assume;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractPoint3DTest<P extends Point3D<? super P, ? super V>, V extends Vector3D<? super V, ? super P>,
		TT extends Tuple3D>
		extends AbstractTuple3DTest<TT> {
	
	public abstract P createPoint(double x, double y, double z);

	public abstract V createVector(double x, double y, double z);
	
	@Test
	public final void staticIsCollinearPoints() {
		assertTrue(Point3D.isCollinearPoints(0, 0, 0, 0, 0, 0, 0, 0, 0));
		assertTrue(Point3D.isCollinearPoints(-6, -4, 0, -1, 3, 0, 4, 10, 0));
		assertFalse(Point3D.isCollinearPoints(0, 0, 0, 1, 1, 0, 1, -5, 0));
	}

	@Test
	public final void staticGetDistancePointPoint() {
		assertEpsilonEquals(0, Point3D.getDistancePointPoint(0, 0, 0, 0, 0, 0));
		assertEpsilonEquals(Math.sqrt(5), Point3D.getDistancePointPoint(0, 0, 0, 1, 2, 0));
		assertEpsilonEquals(Math.sqrt(2), Point3D.getDistancePointPoint(0, 0, 0, 1, 1, 0));
	}

	@Test
	public final void staticGetDistanceSquaredPointPoint() {
		assertEpsilonEquals(0, Point3D.getDistanceSquaredPointPoint(0, 0, 0, 0, 0, 0));
		assertEpsilonEquals(5, Point3D.getDistanceSquaredPointPoint(0, 0, 0, 1, 2, 0));
		assertEpsilonEquals(2, Point3D.getDistanceSquaredPointPoint(0, 0, 0, 1, 1, 0));
	}

	@Test
	public final void staticGetDistanceL1PointPoint() {
		assertEpsilonEquals(4, Point3D.getDistanceL1PointPoint(1.0, 2.0, 0, 3.0, 0, 0));
		assertEpsilonEquals(0, Point3D.getDistanceL1PointPoint(1.0, 2.0, 0, 1 ,2, 0));
		assertEpsilonEquals(0, Point3D.getDistanceL1PointPoint(1, 2, 0, 1.0, 2.0, 0));
		assertEpsilonEquals(4, Point3D.getDistanceL1PointPoint(1.0, 2.0, 0, -1, 0, 0));
	}

	@Test
	public final void staticGetDistanceLinfPointPoint() {
		assertEpsilonEquals(2, Point3D.getDistanceLinfPointPoint(1.0, 2.0, 0, 3.0, 0, 0));
		assertEpsilonEquals(0, Point3D.getDistanceLinfPointPoint(1.0, 2.0, 0, 1, 2, 0));
		assertEpsilonEquals(0, Point3D.getDistanceLinfPointPoint(1, 2, 1.0, 0, 2.0, 0));
		assertEpsilonEquals(2, Point3D.getDistanceLinfPointPoint(1.0, 2.0, 0, -1, 0, 0));
	}

	@Test
	public final void getDistanceSquaredPoint3D() {
		Point3D point = createPoint(0, 0, 0);
		Point3D point2 = createPoint(0, 0, 0);
		Point3D point3 = createPoint(1, 2, 0);
		Point3D point4 = createPoint(1, 1, 0);
		assertEpsilonEquals(0, point.getDistanceSquared(point2));
		assertEpsilonEquals(5, point.getDistanceSquared(point3));
		assertEpsilonEquals(2, point.getDistanceSquared(point4));
	}
	
	@Test
	public final void getDistancePoint3D() {
		Point3D point = createPoint(0, 0, 0);
		Point3D point2 = createPoint(0, 0, 0);
		Point3D point3 = createPoint(1, 2, 0);
		Point3D point4 = createPoint(1, 1, 0);
		assertEpsilonEquals(0, point.getDistance(point2));
		assertEpsilonEquals(Math.sqrt(5), point.getDistance(point3));
		assertEpsilonEquals(Math.sqrt(2), point.getDistance(point4));
	}

	@Test
	public final void getDistanceL1Point3D() {
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Point3D point3 = createPoint(1, 2, 0);
		Point3D point4 = createPoint(-1, 0, 0);
		assertEpsilonEquals(4, point.getDistanceL1(point2));
		assertEpsilonEquals(0, point.getDistanceL1(point));
		assertEpsilonEquals(0, point.getDistanceL1(point3));
		assertEpsilonEquals(4, point.getDistanceL1(point4));
	}

	@Test
	public final void getDistanceLinfPoint3D() {
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Point3D point3 = createPoint(1, 2, 0);
		Point3D point4 = createPoint(-1, 0, 0);
		assertEpsilonEquals(2, point.getDistanceLinf(point2));
		assertEpsilonEquals(0, point.getDistanceLinf(point));
		assertEpsilonEquals(0, point.getDistanceLinf(point3));
		assertEpsilonEquals(2, point.getDistanceLinf(point4));
	}

	@Test
	public final void getIdistanceL1Point3D() {
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Point3D point3 = createPoint(1, 2, 0);
		Point3D point4 = createPoint(-1, 0, 0);
		assertEquals(4, point.getIdistanceL1(point2));
		assertEquals(0, point.getIdistanceL1(point));
		assertEquals(0, point.getIdistanceL1(point3));
		assertEquals(4, point.getIdistanceL1(point4));
	}

	@Test
	public final void getIdistanceLinfPoint3D() {
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Point3D point3 = createPoint(1, 2, 0);
		Point3D point4 = createPoint(-1, 0, 0);
		assertEquals(2, point.getIdistanceLinf(point2));
		assertEquals(0, point.getIdistanceLinf(point));
		assertEquals(0, point.getIdistanceLinf(point3));
		assertEquals(2, point.getIdistanceLinf(point4));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void toUnmodifiable_exception() {
		Point3D origin = createPoint(2, 3, 0);
		Point3D immutable = origin.toUnmodifiable();
		assertEpsilonEquals(origin, immutable);
		immutable.add(1, 2, 0);
	}

	@Test
	public final void toUnmodifiable_changeInOrigin() {
		Point3D origin = createPoint(2, 3, 0);
		assumeMutable(origin);
		Point3D immutable = origin.toUnmodifiable();
		assertEpsilonEquals(origin, immutable);
		assertEpsilonEquals(origin, immutable);
	}

	@Test
	public final void testClonePoint() {
		Point3D origin = createPoint(23, 45, 0);
		Tuple3D clone = origin.clone();
		assertNotNull(clone);
		assertNotSame(origin, clone);
		assertEpsilonEquals(origin.getX(), clone.getX());
		assertEpsilonEquals(origin.getY(), clone.getY());
	}

	@Test
	public final void operator_plusVector3D() {
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		Point3D r;
		
		r = point.operator_plus(vector1);
		assertFpPointEquals(1, 2, 0, r);

		r = point.operator_plus(vector2);
		assertFpPointEquals(2, 4, 0, r);

		r = point.operator_plus(vector3);
		assertFpPointEquals(2, -3, 0, r);

		r = point2.operator_plus(vector1);
		assertFpPointEquals(3, 0, 0, r);

		r = point2.operator_plus(vector2);
		assertFpPointEquals(4, 2, 0, r);

		r = point2.operator_plus(vector3);
		assertFpPointEquals(4, -5, 0, r);
	}

	@Test
	public final void operator_minusVector3D() {
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		Point3D r;
		
		r = point.operator_minus(vector1);
		assertFpPointEquals(1, 2, 0, r);

		r = point.operator_minus(vector2);
		assertFpPointEquals(0, 0, 0, r);

		r = point.operator_minus(vector3);
		assertFpPointEquals(0, 7, 0, r);

		r = point2.operator_minus(vector1);
		assertFpPointEquals(3, 0, 0, r);

		r = point2.operator_minus(vector2);
		assertFpPointEquals(2, -2, 0, r);

		r = point2.operator_minus(vector3);
		assertFpPointEquals(2, 5, 0, r);
	}

	@Test
	public final void operator_minusPoint3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point3D point = createPoint(0, 0, 0);
		Point3D point2 = createPoint(1, 0, 0);
		Point3D vector = createPoint(-1.2, -1.2, 0);
		Point3D vector2 = createPoint(2.0, 1.5, 0);
		Vector3D newVector;

		newVector = point.operator_minus(vector);
		assertFpVectorEquals(1.2, 1.2, 0, newVector);

		newVector = point2.operator_minus(vector2);
		assertFpVectorEquals(-1.0, -1.5, 0, newVector); 
	}

	@Test
	public final void operator_minusPoint3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point3D point = createPoint(0, 0, 0);
		Point3D point2 = createPoint(1, 0, 0);
		Point3D vector = createPoint(-1.2, -1.2, 0);
		Point3D vector2 = createPoint(2.0, 1.5, 0);
		Vector3D newVector;

		newVector = point.operator_minus(vector);
		assertFpVectorEquals(1, 1, 0, newVector);

		newVector = point2.operator_minus(vector2);
		assertFpVectorEquals(-1, -2, 0, newVector); 
	}

	@Test
	public final void operator_equalsTuple3D() {
		Point3D point = createPoint(49, -2, 0);
		assertFalse(point.operator_equals(null));
		assertTrue(point.operator_equals(point));
		assertFalse(point.operator_equals(createPoint(49, -3, 0)));
		assertFalse(point.operator_equals(createPoint(0, 0, 0)));
		assertTrue(point.operator_equals(createPoint(49, -2, 0)));
	}

	@Test
	public final void operator_notEqualsTuple3D() {
		Point3D point = createPoint(49, -2, 0);
		assertTrue(point.operator_notEquals(null));
		assertFalse(point.operator_notEquals(point));
		assertTrue(point.operator_notEquals(createPoint(49, -3, 0)));
		assertTrue(point.operator_notEquals(createPoint(0, 0, 0)));
		assertFalse(point.operator_notEquals(createPoint(49, -2, 0)));
	}

	@Test
	public final void testEqualsObject() {
		Point3D point = createPoint(49, -2, 0);
		assertFalse(point.equals((Object) null));
		assertTrue(point.equals((Object) point));
		assertFalse(point.equals((Object) createPoint(49, -3, 0)));
		assertFalse(point.equals((Object) createPoint(0, 0, 0)));
		assertTrue(point.equals((Object) createPoint(49, -2, 0)));
	}

	@Test
	public final void operator_upToPoint3D() {
		Point3D point = createPoint(0, 0, 0);
		Point3D point2 = createPoint(0, 0, 0);
		Point3D point3 = createPoint(1, 2, 0);
		Point3D point4 = createPoint(1, 1, 0);
		assertEpsilonEquals(0, point.operator_upTo(point2));
		assertEpsilonEquals(Math.sqrt(5), point.operator_upTo(point3));
		assertEpsilonEquals(Math.sqrt(2), point.operator_upTo(point4));
	}

	@Test
	public final void operator_elvisPoint3D() {
		Point3D orig1 = createPoint(45, -78, 0);
		Point3D orig2 = createPoint(0, 0, 0);
		Point3D param = createPoint(-5, -1.4, 0);
		Point3D result;
		
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
	public abstract void operator_andShape3D();

	@Test
	public abstract void operator_upToShape3D();

	@Test
	public void addPoint3DVector3D() {
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.add(point, vector1);
		assertFpPointEquals(1, 2, 0, point);

		point.add(point, vector2);
		assertFpPointEquals(2, 4, 0, point);

		point.add(point, vector3);
		assertFpPointEquals(3, -1, 0, point);

		point.add(point2, vector1);
		assertFpPointEquals(3, 0, 0, point);

		point.add(point2, vector2);
		assertFpPointEquals(4, 2, 0, point);

		point.add(point2, vector3);
		assertFpPointEquals(4, -5, 0, point);
	}

	@Test
	public void addVector3DPoint3D() {
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.add(vector1, point);
		assertFpPointEquals(1, 2, 0, point);

		point.add(vector2, point);
		assertFpPointEquals(2, 4, 0, point);

		point.add(vector3, point);
		assertFpPointEquals(3, -1, 0, point);

		point.add(vector1, point2);
		assertFpPointEquals(3, 0, 0, point);

		point.add(vector2, point2);
		assertFpPointEquals(4, 2, 0, point);

		point.add(vector3, point2);
		assertFpPointEquals(4, -5, 0, point);
	}

	@Test
	public void addVector3D() {
		Point3D point = createPoint(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.add(vector1);
		assertFpPointEquals(1, 2, 0, point);

		point.add(vector2);
		assertFpPointEquals(2, 4, 0, point);

		point.add(vector3);
		assertFpPointEquals(3, -1, 0, point);

		point.add(vector1);
		assertFpPointEquals(3, -1, 0, point);

		point.add(vector2);
		assertFpPointEquals(4, 1, 0, point);

		point.add(vector3);
		assertFpPointEquals(5, -4, 0, point);
	}

	@Test
	public void scaleAddDoubleVector3DPoint3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.scaleAdd(2.5, vector1, point);
		assertFpPointEquals(1, 2, 0, point);

		point.scaleAdd(-2.5, vector2, point);
		assertFpPointEquals(-1.5, -3, 0, point);

		point.scaleAdd(2.5, vector3, point);
		assertFpPointEquals(1, -15.5, 0, point);

		point.scaleAdd(-2.5, vector1, point2);
		assertFpPointEquals(3, 0, 0, point);

		point.scaleAdd(2.5, vector2, point2);
		assertFpPointEquals(5.5, 5, 0, point);

		point.scaleAdd(-2.5, vector3, point2);
		assertFpPointEquals(0.5, 12.5, 0, point);
	}

	@Test
	public void scaleAddDoubleVector3DPoint3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.scaleAdd(2.5, vector1, point);
		assertIntPointEquals(1, 2, 0, point);

		point.scaleAdd(-2.5, vector2, point);
		assertIntPointEquals(-1, -3, 0, point);

		point.scaleAdd(2.5, vector3, point);
		assertIntPointEquals(2, -15, 0, point);

		point.scaleAdd(-2.5, vector1, point2);
		assertIntPointEquals(3, 0, 0, point);

		point.scaleAdd(2.5, vector2, point2);
		assertIntPointEquals(6, 5, 0, point);

		point.scaleAdd(-2.5, vector3, point2);
		assertIntPointEquals(1, 13, 0, point);
	}

	@Test
	public void scaleAddIntVector3DPoint3D() {
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.scaleAdd(2, vector1, point);
		assertFpPointEquals(1, 2, 0, point);

		point.scaleAdd(-2, vector2, point);
		assertFpPointEquals(-1, -2, 0, point);

		point.scaleAdd(2, vector3, point);
		assertFpPointEquals(1, -12, 0, point);

		point.scaleAdd(-2, vector1, point2);
		assertFpPointEquals(3, 0, 0, point);

		point.scaleAdd(2, vector2, point2);
		assertFpPointEquals(5, 4, 0, point);

		point.scaleAdd(-2, vector3, point2);
		assertFpPointEquals(1, 10, 0, point);
	}

	@Test
	public void scaleAddIntPoint3DVector3D() {
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.scaleAdd(2, point, vector1);
		assertFpPointEquals(2, 4, 0, point);

		point.scaleAdd(-2, point, vector2);
		assertFpPointEquals(-3, -6, 0, point);

		point.scaleAdd(2, point, vector3);
		assertFpPointEquals(-5, -17, 0, point);

		point.scaleAdd(-2, point2, vector1);
		assertFpPointEquals(-6, 0, 0, point);

		point.scaleAdd(2, point2, vector2);
		assertFpPointEquals(7, 2, 0, point);

		point.scaleAdd(-2, point2, vector3);
		assertFpPointEquals(-5, -5, 0, point);
	}

	@Test
	public void scaleAddDoublePoint3DVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.scaleAdd(2.5, point, vector1);
		assertFpPointEquals(2.5, 5, 0, point);

		point.scaleAdd(-2.5, point, vector2);
		assertFpPointEquals(-5.25, -10.5, 0, point);

		point.scaleAdd(2.5, point, vector3);
		assertFpPointEquals(-12.125, -31.25, 0, point);

		point.scaleAdd(-2.5, point2, vector1);
		assertFpPointEquals(-7.5, 0, 0, point);

		point.scaleAdd(2.5, point2, vector2);
		assertFpPointEquals(8.5, 2, 0, point);

		point.scaleAdd(-2.5, point2, vector3);
		assertFpPointEquals(-6.5, -5, 0, point);
	}

	@Test
	public void scaleAddDoublePoint3DVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.scaleAdd(2.5, point, vector1);
		assertIntPointEquals(3, 5, 0, point);

		point.scaleAdd(-2.5, point, vector2);
		assertIntPointEquals(-6, -10, 0, point);

		point.scaleAdd(2.5, point, vector3);
		assertIntPointEquals(-14, -30, 0, point);

		point.scaleAdd(-2.5, point2, vector1);
		assertIntPointEquals(-7, 0, 0, point);

		point.scaleAdd(2.5, point2, vector2);
		assertIntPointEquals(9, 2, 0, point);

		point.scaleAdd(-2.5, point2, vector3);
		assertIntPointEquals(-6, -5, 0, point);
	}

	@Test
	public void scaleAddIntVector3D() {
		Point3D point = createPoint(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.scaleAdd(2, vector1);
		assertFpPointEquals(2, 4, 0, point);

		point.scaleAdd(-2, vector2);
		assertFpPointEquals(-3, -6, 0, point);

		point.scaleAdd(2, vector3);
		assertFpPointEquals(-5, -17, 0, point);

		point.scaleAdd(-2, vector1);
		assertFpPointEquals(10, 34, 0, point);

		point.scaleAdd(2, vector2);
		assertFpPointEquals(21, 70, 0, point);

		point.scaleAdd(-2, vector3);
		assertFpPointEquals(-41, -145, 0, point);
	}

	@Test
	public void scaleAddDoubleVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point3D point = createPoint(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.scaleAdd(2.5, vector1);
		assertFpPointEquals(2.5, 5, 0, point);

		point.scaleAdd(-2.5, vector2);
		assertFpPointEquals(-5.25, -10.5, 0, point);

		point.scaleAdd(2.5, vector3);
		assertFpPointEquals(-12.125, -31.25, 0, point);

		point.scaleAdd(-2.5, vector1);
		assertFpPointEquals(30.312, 78.125, 0, point);

		point.scaleAdd(2.5, vector2);
		assertFpPointEquals(76.781, 197.312, 0, point);

		point.scaleAdd(-2.5, vector3);
		assertFpPointEquals(-190.95, -498.28, 0, point);
	}

	@Test
	public void scaleAddDoubleVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point3D point = createPoint(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.scaleAdd(2.5, vector1);
		assertIntPointEquals(3, 5, 0, point);

		point.scaleAdd(-2.5, vector2);
		assertIntPointEquals(-6, -10, 0, point);

		point.scaleAdd(2.5, vector3);
		assertIntPointEquals(-14, -30, 0, point);

		point.scaleAdd(-2.5, vector1);
		assertIntPointEquals(35, 75, 0, point);

		point.scaleAdd(2.5, vector2);
		assertIntPointEquals(89, 190, 0, point);

		point.scaleAdd(-2.5, vector3);
		assertIntPointEquals(-221, -480, 0, point);
	}

	@Test
	public void subPoint3DVector3D() {
		Point3D point = createPoint(1, 2, 0);
		Point3D point2 = createPoint(3, 0, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.sub(point, vector1);
		assertFpPointEquals(1, 2, 0, point);

		point.sub(point, vector2);
		assertFpPointEquals(0, 0, 0, point);

		point.sub(point, vector3);
		assertFpPointEquals(-1, 5, 0, point);

		point.sub(point2, vector1);
		assertFpPointEquals(3, 0, 0, point);

		point.sub(point2, vector2);
		assertFpPointEquals(2, -2, 0, point);

		point.sub(point2, vector3);
		assertFpPointEquals(2, 5, 0, point);
	}

	@Test
	public void subVector3D() {
		Point3D point = createPoint(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.sub(vector1);
		assertFpPointEquals(1, 2, 0, point);

		point.sub(vector2);
		assertFpPointEquals(0, 0, 0, point);

		point.sub(vector3);
		assertFpPointEquals(-1, 5, 0, point);

		point.sub(vector1);
		assertFpPointEquals(-1, 5, 0, point);

		point.sub(vector2);
		assertFpPointEquals(-2, 3, 0, point);

		point.sub(vector3);
		assertFpPointEquals(-3, 8, 0, point);
	}

	@Test
	public void operator_addVector3D() {
		Point3D point = createPoint(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.operator_add(vector1);
		assertFpPointEquals(1, 2, 0, point);

		point.operator_add(vector2);
		assertFpPointEquals(2, 4, 0, point);

		point.operator_add(vector3);
		assertFpPointEquals(3, -1, 0, point);

		point.operator_add(vector1);
		assertFpPointEquals(3, -1, 0, point);

		point.operator_add(vector2);
		assertFpPointEquals(4, 1, 0, point);

		point.operator_add(vector3);
		assertFpPointEquals(5, -4, 0, point);
	}

	@Test
	public void operator_removeVector3D() {
		Point3D point = createPoint(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		Vector3D vector2 = createVector(1, 2, 0);
		Vector3D vector3 = createVector(1, -5, 0);
		
		point.operator_remove(vector1);
		assertFpPointEquals(1, 2, 0, point);

		point.operator_remove(vector2);
		assertFpPointEquals(0, 0, 0, point);

		point.operator_remove(vector3);
		assertFpPointEquals(-1, 5, 0, point);

		point.operator_remove(vector1);
		assertFpPointEquals(-1, 5, 0, point);

		point.operator_remove(vector2);
		assertFpPointEquals(-2, 3, 0, point);

		point.operator_remove(vector3);
		assertFpPointEquals(-3, 8, 0, point);
	}

}
