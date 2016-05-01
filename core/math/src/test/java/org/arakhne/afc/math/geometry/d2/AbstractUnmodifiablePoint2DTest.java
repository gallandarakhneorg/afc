/**
 * 
 * fr.utbm.v3g.core.math.Tuple2dTest.java
 *
 * Copyright (c) 2008-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 *
 * Primary author : Olivier LAMOTTE (olivier.lamotte@utbm.fr) - 2015
 *
 */
package org.arakhne.afc.math.geometry.d2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractUnmodifiablePoint2DTest extends AbstractUnmodifiableTuple2DTest {
	
	protected abstract Point2D createPoint(double x, double y);

	protected abstract Vector2D createVector(double x, double y);

	@Override
	protected Tuple2D createTuple(int x, int y) {
		return createPoint(x, y);
	}

	@Test
	public void getDistanceSquaredPoint2D() {
		Point2D point = createPoint(0, 0);
		Point2D point2 = createPoint(0, 0);
		Point2D point3 = createPoint(1, 2);
		Point2D point4 = createPoint(1, 1);
		assertEpsilonEquals(0, point.getDistanceSquared(point2));
		assertEpsilonEquals(5, point.getDistanceSquared(point3));
		assertEpsilonEquals(2, point.getDistanceSquared(point4));
	}
	
	@Test
	public void getDistancePoint2D() {
		Point2D point = createPoint(0, 0);
		Point2D point2 = createPoint(0, 0);
		Point2D point3 = createPoint(1, 2);
		Point2D point4 = createPoint(1, 1);
		assertEpsilonEquals(0, point.getDistance(point2));
		assertEpsilonEquals(Math.sqrt(5), point.getDistance(point3));
		assertEpsilonEquals(Math.sqrt(2), point.getDistance(point4));
	}

	@Test
	public void getDistanceL1Point2D() {
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
	public void getDistanceLinfPoint2D() {
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
	public void getIdistanceL1Point2D() {
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
	public void getIdistanceLinfPoint2D() {
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
	public void addPoint2DVector2D() {
		Point2D point = createPoint(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.add(point, vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addVector2DPoint2D() {
		Point2D point = createPoint(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.add(vector1, point);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addVector2D() {
		Point2D point = createPoint(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.add(vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void scaleAddDoubleVector2DPoint2D() {
		Point2D point = createPoint(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2.5, vector1, point);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void scaleAddIntVector2DPoint2D() {
		Point2D point = createPoint(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2, vector1, point);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void scaleAddIntPoint2DVector2D() {
		Point2D point = createPoint(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2, point, vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void scaleAddDoublePoint2DVector2D() {
		Point2D point = createPoint(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2.5, point, vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void scaleAddIntVector2D() {
		Point2D point = createPoint(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2, vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void scaleAddDoubleVector2D() {
		Point2D point = createPoint(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.scaleAdd(2.5, vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void subPoint2DVector2D() {
		Point2D point = createPoint(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.sub(point, vector1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void subVector2D() {
		Point2D point = createPoint(1, 2);
		Vector2D vector1 = createVector(0, 0);
		point.sub(vector1);
	}

	@Test
	public void toUnmodifiable() {
		Point2D origin = createPoint(2, 3);
		Point2D immutable = origin.toUnmodifiable();
		assertSame(origin, immutable);
	}
	
}
