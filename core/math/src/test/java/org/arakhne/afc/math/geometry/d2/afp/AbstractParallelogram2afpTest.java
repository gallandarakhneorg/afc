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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractParallelogram2afpTest<T extends Parallelogram2afp<?, T, ?, ?, B>,
B extends Rectangle2afp<?, ?, ?, ?, B>> extends AbstractShape2afpTest<T, B> {

	protected final double cx = 6;
	protected final double cy = 9;
	protected final double ux = 2.425356250363330e-01;   
	protected final double uy = 9.701425001453320e-01;
	protected final double e1 = 9.219544457292887;
	protected final double vx = -7.071067811865475e-01;
	protected final double vy = 7.071067811865475e-01;
	protected final double e2 = 1.264911064067352e+01;

	// Points' names are in the ggb diagram
	private double pEx = 12.7082;
	private double pEy = -8.88854;
	private double pFx = 17.18034;
	private double pFy = 9;
	private double pGx = -0.7082;
	private double pGy = 26.88854;
	private double pHx = -5.18034;
	private double pHy = 9;

	@Override
	protected final T createShape() {
		return (T) createParallelogram(cx, cy, ux, uy, e1, vx, vy, e2);
	}

	@Test
	public void staticComputeOrthogonalAxis() {
		double obrux = 0.8944271909999159;
		double obruy = -0.4472135954999579;
		double obre1 = 13.99999;
		double obrvx = 0.4472135954999579;
		double obrvy = 0.8944271909999159;
		double obre2 = 12.99999;
		List<Point2D> points = Arrays.<Point2D>asList(
				createPoint(11.7082, -0.94427), createPoint(16.18034, 8),
				createPoint(-1.7082, 16.94427), createPoint(-6.18034, 8));
		Vector2D R, S;
		R = createVector(Double.NaN, Double.NaN);
		S = createVector(Double.NaN, Double.NaN);
		Parallelogram2afp.computeOrthogonalAxes(points, R, S);
		assertEpsilonEquals(obrux, R.getX());
		assertEpsilonEquals(obruy, R.getY());
		assertEpsilonEquals(obrvx, S.getX());
		assertEpsilonEquals(obrvy, S.getY());
	}	
	
	@Test
	public void staticProjectVectorOnParallelogramRAxis() {
		assertEpsilonEquals(-e1, Parallelogram2afp.projectVectorOnParallelogramRAxis(ux, uy, vx, vy, pEx - cx, pEy - cy));
		assertEpsilonEquals(e1, Parallelogram2afp.projectVectorOnParallelogramRAxis(ux, uy, vx, vy, pFx - cx, pFy - cy));
		assertEpsilonEquals(e1, Parallelogram2afp.projectVectorOnParallelogramRAxis(ux, uy, vx, vy, pGx - cx, pGy - cy));
		assertEpsilonEquals(-e1, Parallelogram2afp.projectVectorOnParallelogramRAxis(ux, uy, vx, vy, pHx - cx, pHy - cy));
		assertEpsilonEquals(-12.36932, Parallelogram2afp.projectVectorOnParallelogramRAxis(ux, uy, vx, vy, -cx, -cy));
	}

	@Test
	public void staticProjectVectorOnParallelogramSAxis() {
		assertEpsilonEquals(-e2, Parallelogram2afp.projectVectorOnParallelogramSAxis(ux, uy, vx, vy, pEx - cx, pEy - cy));
		assertEpsilonEquals(-e2, Parallelogram2afp.projectVectorOnParallelogramSAxis(ux, uy, vx, vy, pFx - cx, pFy - cy));
		assertEpsilonEquals(e2, Parallelogram2afp.projectVectorOnParallelogramSAxis(ux, uy, vx, vy, pGx - cx, pGy - cy));
		assertEpsilonEquals(e2, Parallelogram2afp.projectVectorOnParallelogramSAxis(ux, uy, vx, vy, pHx - cx, pHy - cy));
		assertEpsilonEquals(4.24264, Parallelogram2afp.projectVectorOnParallelogramSAxis(ux, uy, vx, vy, -cx, -cy));
	}

	@Test
	public void staticComputeCenterExtents() {
		List<Point2D> points = Arrays.<Point2D>asList(
				createPoint(pEx, pEy), createPoint(pGx, pGy),
				createPoint(pFx, pFy), createPoint(pEx, pEy));
		Vector2D R, S;
		Point2D center;
		Tuple2D extents;
		R = createVector(ux, uy);
		S = createVector(vx, vy);
		center = createPoint(Double.NaN, Double.NaN);
		extents = createVector(Double.NaN, Double.NaN);
		Parallelogram2afp.computeCenterExtents(points, R, S, center, extents);
		assertEpsilonEquals(cx, center.getX());
		assertEpsilonEquals(cy, center.getY());
		assertEpsilonEquals(e1, extents.getX());
		assertEpsilonEquals(e2, extents.getY());
	}

	@Test
	public void staticComputeClosestFarthestPoints() {
		Point2D closest, farthest;

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				-20, 9,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(pHx, closest.getX());
		assertEpsilonEquals(pHy, closest.getY());
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				0, 0,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(1.90983, closest.getX());
		assertEpsilonEquals(1.90983, closest.getY());
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				5, -10,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(9.40983, closest.getX());
		assertEpsilonEquals(-5.59017, closest.getY());
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				14, -20,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(pEx, closest.getX());
		assertEpsilonEquals(pEy, closest.getY());
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				-6, 15,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(-3.81679, closest.getX());
		assertEpsilonEquals(14.4542, closest.getY());
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				0, 10,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(0, closest.getX());
		assertEpsilonEquals(10, closest.getY());
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				10, 0,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(10, closest.getX());
		assertEpsilonEquals(0, closest.getY());
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				15, -4,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(13.99326, closest.getX());
		assertEpsilonEquals(-3.74832, closest.getY());
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				-5, 25,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(-1.40503, closest.getX());
		assertEpsilonEquals(24.10126, closest.getY());
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				0, 20,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(0, closest.getX());
		assertEpsilonEquals(20, closest.getY());
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				10, 10,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(10, closest.getX());
		assertEpsilonEquals(10, closest.getY());
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				20, 0,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(15.22856, closest.getX());
		assertEpsilonEquals(1.19286, closest.getY());
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				-3, 35,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(pGx, closest.getX());
		assertEpsilonEquals(pGy, closest.getY());
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				5, 35,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(pGx, closest.getX());
		assertEpsilonEquals(pGy, closest.getY());
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				20, 15,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(15.59017, closest.getX());
		assertEpsilonEquals(10.59017, closest.getY());
		assertEpsilonEquals(pHx, farthest.getX());
		assertEpsilonEquals(pHy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				35, 10,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(pFx, closest.getX());
		assertEpsilonEquals(pFy, closest.getY());
		assertEpsilonEquals(pHx, farthest.getX());
		assertEpsilonEquals(pHy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(
				-8, 29,
				cx, cy, ux, uy, e1, vx, vy, e2,
				closest, farthest);
		assertEpsilonEquals(pGx, closest.getX());
		assertEpsilonEquals(pGy, closest.getY());
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		Parallelogram2afp.computeClosestFarthestPoints(0, 0,
				4.7, 15,
				0.12403, 0.99228,
				18.02776,
				-0.44721, 0.89443,
				20,
				closest, farthest);
		assertEpsilonEquals(0.81573, closest.getX());
		assertEpsilonEquals(0.40786, closest.getY());
		assertEpsilonEquals(-2.0082, farthest.getX());
		assertEpsilonEquals(50.77719, farthest.getY());
	}

	@Test
	public void staticContainsParallelogramPoint() {
		assertFalse(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 0));
		assertFalse(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				-20, 0));
		assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				12, -4));
		assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				14, 0));
		assertFalse(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				15, 0));
		assertFalse(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				20, 8));
		assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				8, 16));
		assertFalse(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				-4, 20));
		assertFalse(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				-5, 12));

		assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 6));
		assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 7));
		assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 8));
		assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 9));
		assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 10));
		assertFalse(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 27));

		assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				cx, cy));

		assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
				16, 8));
	}

	@Test
	public void staticContainsParallelogramRectangle() {
		assertFalse(Parallelogram2afp.containsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 0, 1, 1));
		assertFalse(Parallelogram2afp.containsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 1, 1, 1));
		assertFalse(Parallelogram2afp.containsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 2, 1, 1));
		assertFalse(Parallelogram2afp.containsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 3, 1, 1));
		assertTrue(Parallelogram2afp.containsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 4, 1, 1));
		assertTrue(Parallelogram2afp.containsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 5, 1, 1));
		assertTrue(Parallelogram2afp.containsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 6, 1, 1));
	}

	@Test
	public void staticIntersectsParallelogramSegment() {
		assertFalse(Parallelogram2afp.intersectsParallelogramSegment(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 0, 1, 1));
		assertTrue(Parallelogram2afp.intersectsParallelogramSegment(cx, cy, ux, uy, e1, vx, vy, e2,
				5, 5, 4, 6));
		assertTrue(Parallelogram2afp.intersectsParallelogramSegment(cx, cy, ux, uy, e1, vx, vy, e2,
				2, -2, 5, 0));
		assertFalse(Parallelogram2afp.intersectsParallelogramSegment(cx, cy, ux, uy, e1, vx, vy, e2,
				-20, -5, -10, 6));
		assertFalse(Parallelogram2afp.intersectsParallelogramSegment(cx, cy, ux, uy, e1, vx, vy, e2,
				-5, 0, -10, 16));
		assertTrue(Parallelogram2afp.intersectsParallelogramSegment(cx, cy, ux, uy, e1, vx, vy, e2,
				-10, 1, 10, 20));
	}

	@Test
	public void staticIntersectsParallelogramCircle() {
		assertFalse(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
				.5, .5, .5));
		assertFalse(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
				.5, 1.5, .5));
		assertFalse(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
				.5, 2.5, .5));
		assertTrue(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
				.5, 3.5, .5));
		assertTrue(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
				4.5, 3.5, .5));

		assertFalse(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
				10, -7, .5));
		assertFalse(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
				10.1, -7, .5));
		assertTrue(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
				10.2, -7, .5));

		assertTrue(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
				10, -1, 5));
	}

	@Test
	public void staticIntersectsParallelogramEllipse() {
		assertFalse(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 0, 2, 1));
		assertFalse(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 1, 2, 1));
		assertTrue(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 2, 2, 1));
		assertTrue(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 3, 2, 1));
		assertTrue(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 4, 2, 1));
		assertTrue(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
				1, 3, 2, 1));

		assertTrue(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
				5, 5, 2, 1));

		assertFalse(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
				0.1, 1, 2, 1));
		assertFalse(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
				0.2, 1, 2, 1));
		assertTrue(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
				0.3, 1, 2, 1));
		assertTrue(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
				0.4, 1, 2, 1));

		assertFalse(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
				-7, 7.5, 2, 1));
	}

	@Test
	public void staticIntersectsParallelogramTriangle() {
		assertTrue(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
				-5, 15, -3, 16, -8, 19));
		assertTrue(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
				-5, 15, -8, 19, -3, 16));
		assertFalse(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, -5, 2, -4, -3, -1));
		assertFalse(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, -5, -3, -1, 2, -4));
		assertFalse(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
				20, 0, 22, 1, 17, 4));
		assertFalse(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
				20, 0, 17, 4, 22, 1));
		assertFalse(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
				17.18034, 9, 19.18034, 10, 14.18034, 13));
		assertFalse(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
				17.18034, 9, 14.18034, 13, 19.18034, 10));
		assertTrue(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 10, 2, 11, -3, 14));
		assertTrue(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 10, -3, 14, 2, 11));
		assertTrue(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 20, 2, 21, -3, 24));
	}

	@Test
	public void staticIntersectsParallelogramParallelogram() {
		double ux2 = -0.9284766908852592;
		double uy2 = 0.3713906763541037;
		double et1 = 5;
		double vx2 = 0.3713906763541037;
		double vy2 = 0.9284766908852592;
		double et2 = 3;
		// P + (-0.9284766908852592,0.3713906763541037) * 5 + (0.3713906763541037,0.9284766908852592) * 3
		// P - (-0.9284766908852592,0.3713906763541037) * 5 + (0.3713906763541037,0.9284766908852592) * 3
		// P - (-0.9284766908852592,0.3713906763541037) * 5 - (0.3713906763541037,0.9284766908852592) * 3
		// P + (-0.9284766908852592,0.3713906763541037) * 5 - (0.3713906763541037,0.9284766908852592) * 3
		assertFalse(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
				-10, 0,
				ux2, uy2, et1, vx2, vy2, et2));
		assertFalse(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
				-15, 25,
				ux2, uy2, et1, vx2, vy2, et2));
		assertFalse(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
				2, -6,
				ux2, uy2, et1, vx2, vy2, et2));
		assertFalse(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
				2, -5,
				ux2, uy2, et1, vx2, vy2, et2));
		assertTrue(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
				2, -4,
				ux2, uy2, et1, vx2, vy2, et2));
		assertTrue(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
				pEx, pEy,
				ux2, uy2, et1, vx2, vy2, et2));
		assertTrue(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
				6, 6,
				ux2, uy2, et1, vx2, vy2, et2));
		assertTrue(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
				6, 6,
				ux2, uy2, 10 * et1, vx2, vy2, 10 * et2));
	}

	@Test
	public void staticIntersectsParallelogramRectangle() {
		assertFalse(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 0, 1, 1));
		assertTrue(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 2, 1, 1));
		assertTrue(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				-5.5, 8.5, 1, 1));
		assertFalse(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				-6, 16, 1, 1));
		assertFalse(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				146, 16, 1, 1));
		assertTrue(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				12, 14, 1, 1));
		assertTrue(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 8, 1, 1));
		assertTrue(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				10, -1, 1, 1));
		assertTrue(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				-15, -10, 35, 40));
		assertTrue(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				-4.79634, 14.50886, 1, 1));
	}

	@Test
	public void staticIntersectsParallelogramRoundRectangle() {
		assertFalse(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 0, 1, 1, .1, .05));
		assertTrue(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 2, 1, 1, .1, .05));
		assertTrue(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				-5.5, 8.5, 1, 1, .1, .05));
		assertFalse(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				-6, 16, 1, 1, .1, .05));
		assertFalse(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				146, 16, 1, 1, .1, .05));
		assertTrue(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				12, 14, 1, 1, .1, .05));
		assertTrue(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				0, 8, 1, 1, .1, .05));
		assertTrue(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				10, -1, 1, 1, .1, .05));
		assertTrue(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				-15, -10, 35, 40, .1, .05));
		assertFalse(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
				-4.79634, 14.50886, 1, 1, .1, .05));
	}

	@Override
	public void testClone() {
		T clone = this.shape.clone();
		assertNotNull(clone);
		assertNotSame(this.shape, clone);
		assertEquals(this.shape.getClass(), clone.getClass());
		assertEpsilonEquals(cx, clone.getCenterX());
		assertEpsilonEquals(cy, clone.getCenterY());
		assertEpsilonEquals(ux, clone.getFirstAxisX());
		assertEpsilonEquals(uy, clone.getFirstAxisY());
		assertEpsilonEquals(e1, clone.getFirstAxisExtent());
		assertEpsilonEquals(vx, clone.getSecondAxisX());
		assertEpsilonEquals(vy, clone.getSecondAxisY());
		assertEpsilonEquals(e2, clone.getSecondAxisExtent());
	}

	@Override
	public void equalsObject() {
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createParallelogram(0, cy, ux, uy, e1, vx, vy, e2)));
		assertFalse(this.shape.equals(createParallelogram(cx, cy, ux, uy, e1, vx, vy, 20)));
		assertFalse(this.shape.equals(createSegment(5, 8, 6, 10)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createParallelogram(cx, cy, ux, uy, e1, vx, vy, e2)));
	}

	@Override
	public void equalsObject_withPathIterator() {
		assertFalse(this.shape.equals((PathIterator2afp) null));
		assertFalse(this.shape.equals(createParallelogram(0, cy, ux, uy, e1, vx, vy, e2).getPathIterator()));
		assertFalse(this.shape.equals(createParallelogram(cx, cy, ux, uy, e1, vx, vy, 20).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 6, 10).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createParallelogram(cx, cy, ux, uy, e1, vx, vy, e2).getPathIterator()));
	}

	@Override
	public void equalsToPathIterator() {
		assertFalse(this.shape.equalsToPathIterator(null));
		assertFalse(this.shape.equalsToPathIterator(createParallelogram(0, cy, ux, uy, e1, vx, vy, e2).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createParallelogram(cx, cy, ux, uy, e1, vx, vy, 20).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 6, 10).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createParallelogram(cx, cy, ux, uy, e1, vx, vy, e2).getPathIterator()));
	}

	@Override
	public void equalsToShape() {
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createParallelogram(0, cy, ux, uy, e1, vx, vy, e2)));
		assertFalse(this.shape.equalsToShape((T) createParallelogram(cx, cy, ux, uy, e1, vx, vy, 20)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createParallelogram(cx, cy, ux, uy, e1, vx, vy, e2)));
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
		assertEpsilonEquals(0, this.shape.getCenterX());
		assertEpsilonEquals(0, this.shape.getCenterY());
		assertEpsilonEquals(1, this.shape.getFirstAxisX());
		assertEpsilonEquals(0, this.shape.getFirstAxisY());
		assertEpsilonEquals(0, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(0, this.shape.getSecondAxisX());
		assertEpsilonEquals(1, this.shape.getSecondAxisY());
		assertEpsilonEquals(0, this.shape.getSecondAxisExtent());
	}

	@Override
	public void containsDoubleDouble() {
		assertFalse(this.shape.contains(0, 0));
		assertFalse(this.shape.contains(-20, 0));
		assertTrue(this.shape.contains(12, -4));
		assertTrue(this.shape.contains(14, 0));
		assertFalse(this.shape.contains(15, 0));
		assertFalse(this.shape.contains(20, 8));
		assertTrue(this.shape.contains(8, 16));
		assertFalse(this.shape.contains(-4, 20));
		assertFalse(this.shape.contains(-5, 12));
		assertTrue(this.shape.contains(0, 6));
		assertTrue(this.shape.contains(0, 7));
		assertTrue(this.shape.contains(0, 8));
		assertTrue(this.shape.contains(0, 9));
		assertTrue(this.shape.contains(0, 10));
		assertFalse(this.shape.contains(0, 27));
		assertTrue(this.shape.contains(cx, cy));
		assertTrue(this.shape.contains(16, 8));
	}

	@Override
	public void containsPoint2D() {
		assertFalse(this.shape.contains(createPoint(0, 0)));
		assertFalse(this.shape.contains(createPoint(-20, 0)));
		assertTrue(this.shape.contains(createPoint(12, -4)));
		assertTrue(this.shape.contains(createPoint(14, 0)));
		assertFalse(this.shape.contains(createPoint(15, 0)));
		assertFalse(this.shape.contains(createPoint(20, 8)));
		assertTrue(this.shape.contains(createPoint(8, 16)));
		assertFalse(this.shape.contains(createPoint(-4, 20)));
		assertFalse(this.shape.contains(createPoint(-5, 12)));
		assertTrue(this.shape.contains(createPoint(0, 6)));
		assertTrue(this.shape.contains(createPoint(0, 7)));
		assertTrue(this.shape.contains(createPoint(0, 8)));
		assertTrue(this.shape.contains(createPoint(0, 9)));
		assertTrue(this.shape.contains(createPoint(0, 10)));
		assertFalse(this.shape.contains(createPoint(0, 27)));
		assertTrue(this.shape.contains(createPoint(cx, cy)));
		assertTrue(this.shape.contains(createPoint( 16, 8)));
	}

	@Override
	public void getClosestPointTo() {
		Point2D closest;

		closest = this.shape.getClosestPointTo(createPoint(-20, 9));
		assertEpsilonEquals(pHx, closest.getX());
		assertEpsilonEquals(pHy, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(0, 0));
		assertEpsilonEquals(1.90983, closest.getX());
		assertEpsilonEquals(1.90983, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(5, -10));
		assertEpsilonEquals(9.40983, closest.getX());
		assertEpsilonEquals(-5.59017, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(14, -20));
		assertEpsilonEquals(pEx, closest.getX());
		assertEpsilonEquals(pEy, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(-6, 15));
		assertEpsilonEquals(-3.81679, closest.getX());
		assertEpsilonEquals(14.4542, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(0, 10));
		assertEpsilonEquals(0, closest.getX());
		assertEpsilonEquals(10, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(10, 0));
		assertEpsilonEquals(10, closest.getX());
		assertEpsilonEquals(0, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(15, -4));
		assertEpsilonEquals(13.99326, closest.getX());
		assertEpsilonEquals(-3.74832, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(-5, 25));
		assertEpsilonEquals(-1.40503, closest.getX());
		assertEpsilonEquals(24.10126, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(0, 20));
		assertEpsilonEquals(0, closest.getX());
		assertEpsilonEquals(20, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(10, 10));
		assertEpsilonEquals(10, closest.getX());
		assertEpsilonEquals(10, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(20, 0));
		assertEpsilonEquals(15.22856, closest.getX());
		assertEpsilonEquals(1.19286, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(-3, 35));
		assertEpsilonEquals(pGx, closest.getX());
		assertEpsilonEquals(pGy, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(5, 35));
		assertEpsilonEquals(pGx, closest.getX());
		assertEpsilonEquals(pGy, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(20, 15));
		assertEpsilonEquals(15.59017, closest.getX());
		assertEpsilonEquals(10.59017, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(35, 10));
		assertEpsilonEquals(pFx, closest.getX());
		assertEpsilonEquals(pFy, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(-8, 29));
		assertEpsilonEquals(pGx, closest.getX());
		assertEpsilonEquals(pGy, closest.getY());
	}

	@Override
	public void getFarthestPointTo() {
		Point2D farthest;

		farthest = this.shape.getFarthestPointTo(createPoint(-20, 9));
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(0, 0));
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(5, -10));
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(14, -20));
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(-6, 15));
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(0, 10));
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(10, 0));
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(15, -4));
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(-5, 25));
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(0, 20));
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(10, 10));
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(20, 0));
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(-3, 35));
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(5, 35));
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(20, 15));
		assertEpsilonEquals(pHx, farthest.getX());
		assertEpsilonEquals(pHy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(35, 10));
		assertEpsilonEquals(pHx, farthest.getX());
		assertEpsilonEquals(pHy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(-8, 29));
		assertEpsilonEquals(pEx, farthest.getX());
		assertEpsilonEquals(pEy, farthest.getY());
	}

	@Override
	public void translateDoubleDouble() {
		this.shape.translate(123.456, 789.123);
		assertEpsilonEquals(cx + 123.456, this.shape.getCenterX());
		assertEpsilonEquals(cy + 789.123, this.shape.getCenterY());
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Override
	public void translateVector2D() {
		this.shape.translate(createVector(123.456, 789.123));
		assertEpsilonEquals(cx + 123.456, this.shape.getCenterX());
		assertEpsilonEquals(cy + 789.123, this.shape.getCenterY());
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Override
	public void getDistance() {
		assertEpsilonEquals(14.81966, this.shape.getDistance(createPoint(-20, 9)));
		assertEpsilonEquals(2.7009, this.shape.getDistance(createPoint(0, 0)));
		assertEpsilonEquals(6.23644, this.shape.getDistance(createPoint(5, -10)));
		assertEpsilonEquals(11.1863, this.shape.getDistance(createPoint(14, -20)));
		assertEpsilonEquals(2.25040, this.shape.getDistance(createPoint(-6, 15)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(0, 10)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(10, 0)));
		assertEpsilonEquals(1.03772, this.shape.getDistance(createPoint(15, -4)));
		assertEpsilonEquals(3.70561, this.shape.getDistance(createPoint(-5, 25)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(0, 20)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(10, 10)));
		assertEpsilonEquals(4.91829, this.shape.getDistance(createPoint(20, 0)));
		assertEpsilonEquals(8.42901, this.shape.getDistance(createPoint(-3, 35)));
		assertEpsilonEquals(9.91864, this.shape.getDistance(createPoint(5, 35)));
		assertEpsilonEquals(6.23644, this.shape.getDistance(createPoint(20, 15)));
		assertEpsilonEquals(17.8477, this.shape.getDistance(createPoint(35, 10)));
		assertEpsilonEquals(7.59135, this.shape.getDistance(createPoint(-8, 29)));
	}

	@Override
	public void getDistanceSquared() {
		assertEpsilonEquals(219.62232, this.shape.getDistanceSquared(createPoint(-20, 9)));
		assertEpsilonEquals(7.29486, this.shape.getDistanceSquared(createPoint(0, 0)));
		assertEpsilonEquals(38.89318, this.shape.getDistanceSquared(createPoint(5, -10)));
		assertEpsilonEquals(125.13319, this.shape.getDistanceSquared(createPoint(14, -20)));
		assertEpsilonEquals(5.0643, this.shape.getDistanceSquared(createPoint(-6, 15)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(0, 10)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(10, 0)));
		assertEpsilonEquals(1.07686, this.shape.getDistanceSquared(createPoint(15, -4)));
		assertEpsilonEquals(13.73155, this.shape.getDistanceSquared(createPoint(-5, 25)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(0, 20)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(10, 10)));
		assertEpsilonEquals(24.18958, this.shape.getDistanceSquared(createPoint(20, 0)));
		assertEpsilonEquals(71.04805, this.shape.getDistanceSquared(createPoint(-3, 35)));
		assertEpsilonEquals(98.37931, this.shape.getDistanceSquared(createPoint(5, 35)));
		assertEpsilonEquals(38.89318, this.shape.getDistanceSquared(createPoint(20, 15)));
		assertEpsilonEquals(318.54029, this.shape.getDistanceSquared(createPoint(35, 10)));
		assertEpsilonEquals(57.62859, this.shape.getDistanceSquared(createPoint(-8, 29)));
	}

	@Override
	public void getDistanceL1() {
		assertEpsilonEquals(14.81966, this.shape.getDistanceL1(createPoint(-20, 9)));
		assertEpsilonEquals(3.81966, this.shape.getDistanceL1(createPoint(0, 0)));
		assertEpsilonEquals(8.81966, this.shape.getDistanceL1(createPoint(5, -10)));
		assertEpsilonEquals(12.40325, this.shape.getDistanceL1(createPoint(14, -20)));
		assertEpsilonEquals(2.72901, this.shape.getDistanceL1(createPoint(-6, 15)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(0, 10)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(10, 0)));
		assertEpsilonEquals(1.25842, this.shape.getDistanceL1(createPoint(15, -4)));
		assertEpsilonEquals(4.49371, this.shape.getDistanceL1(createPoint(-5, 25)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(0, 20)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(10, 10)));
		assertEpsilonEquals(5.9643, this.shape.getDistanceL1(createPoint(20, 0)));
		assertEpsilonEquals(10.40326, this.shape.getDistanceL1(createPoint(-3, 35)));
		assertEpsilonEquals(13.81966, this.shape.getDistanceL1(createPoint(5, 35)));
		assertEpsilonEquals(8.81966, this.shape.getDistanceL1(createPoint(20, 15)));
		assertEpsilonEquals(18.81966, this.shape.getDistanceL1(createPoint(35, 10)));
		assertEpsilonEquals(9.40326, this.shape.getDistanceL1(createPoint(-8, 29)));
	}

	@Override
	public void getDistanceLinf() {
		assertEpsilonEquals(14.81966, this.shape.getDistanceLinf(createPoint(-20, 9)));
		assertEpsilonEquals(1.90983, this.shape.getDistanceLinf(createPoint(0, 0)));
		assertEpsilonEquals(4.40983, this.shape.getDistanceLinf(createPoint(5, -10)));
		assertEpsilonEquals(11.11146, this.shape.getDistanceLinf(createPoint(14, -20)));
		assertEpsilonEquals(2.18321, this.shape.getDistanceLinf(createPoint(-6, 15)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(0, 10)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(10, 0)));
		assertEpsilonEquals(1.00674, this.shape.getDistanceLinf(createPoint(15, -4)));
		assertEpsilonEquals(3.59497, this.shape.getDistanceLinf(createPoint(-5, 25)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(0, 20)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(10, 10)));
		assertEpsilonEquals(4.77144, this.shape.getDistanceLinf(createPoint(20, 0)));
		assertEpsilonEquals(8.11146, this.shape.getDistanceLinf(createPoint(-3, 35)));
		assertEpsilonEquals(8.11146, this.shape.getDistanceLinf(createPoint(5, 35)));
		assertEpsilonEquals(4.40983, this.shape.getDistanceLinf(createPoint(20, 15)));
		assertEpsilonEquals(17.81966, this.shape.getDistanceLinf(createPoint(35, 10)));
		assertEpsilonEquals(7.2918, this.shape.getDistanceLinf(createPoint(-8, 29)));
	}

	@Override
	public void setIT() {
		this.shape.set((T) createParallelogram(17, 20, 1, 0, 15, 0, 1, 14));
		assertEpsilonEquals(17, this.shape.getCenterX());
		assertEpsilonEquals(20, this.shape.getCenterY());
		assertEpsilonEquals(1, this.shape.getFirstAxisX());
		assertEpsilonEquals(0, this.shape.getFirstAxisY());
		assertEpsilonEquals(15, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(0, this.shape.getSecondAxisX());
		assertEpsilonEquals(1, this.shape.getSecondAxisY());
		assertEpsilonEquals(14, this.shape.getSecondAxisExtent());
	}

	@Override
	public void getPathIterator() {
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
		assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
		assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
		assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
		assertElement(pi, PathElementType.CLOSE, pGx, pGy);
		assertNoElement(pi);
	}

	@Override
	public void getPathIteratorTransform2D() {
		PathIterator2afp pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
		assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
		assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
		assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
		assertElement(pi, PathElementType.CLOSE, pGx, pGy);
		assertNoElement(pi);

		Transform2D transform;
		
		transform = new Transform2D();
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
		assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
		assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
		assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
		assertElement(pi, PathElementType.CLOSE, pGx, pGy);
		assertNoElement(pi);

		transform = new Transform2D();
		transform.setTranslation(18,  -45);
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, pGx + 18, pGy - 45);
		assertElement(pi, PathElementType.LINE_TO, pHx + 18, pHy - 45);
		assertElement(pi, PathElementType.LINE_TO, pEx + 18, pEy - 45);
		assertElement(pi, PathElementType.LINE_TO, pFx + 18, pFy - 45);
		assertElement(pi, PathElementType.CLOSE, pGx + 18, pGy - 45);
		assertNoElement(pi);
	}

	@Override
	public void createTransformedShape() {
		PathIterator2afp pi = this.shape.createTransformedShape(null).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
		assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
		assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
		assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
		assertElement(pi, PathElementType.CLOSE, pGx, pGy);
		assertNoElement(pi);

		Transform2D transform;
		
		transform = new Transform2D();
		pi = this.shape.createTransformedShape(transform).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
		assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
		assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
		assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
		assertElement(pi, PathElementType.CLOSE, pGx, pGy);
		assertNoElement(pi);

		transform = new Transform2D();
		transform.setTranslation(18,  -45);
		pi = this.shape.createTransformedShape(transform).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, pGx + 18, pGy - 45);
		assertElement(pi, PathElementType.LINE_TO, pHx + 18, pHy - 45);
		assertElement(pi, PathElementType.LINE_TO, pEx + 18, pEy - 45);
		assertElement(pi, PathElementType.LINE_TO, pFx + 18, pFy - 45);
		assertElement(pi, PathElementType.CLOSE, pGx + 18, pGy - 45);
		assertNoElement(pi);
	}

	@Override
	public void toBoundingBox() {
		B box = this.shape.toBoundingBox();
		assertEpsilonEquals(pHx, box.getMinX());
		assertEpsilonEquals(pEy, box.getMinY());
		assertEpsilonEquals(pFx, box.getMaxX());
		assertEpsilonEquals(pGy, box.getMaxY());
	}

	@Override
	public void toBoundingBoxB() {
		B box = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(pHx, box.getMinX());
		assertEpsilonEquals(pEy, box.getMinY());
		assertEpsilonEquals(pFx, box.getMaxX());
		assertEpsilonEquals(pGy, box.getMaxY());
	}

	@Override
	public void containsRectangle2afp() {
		assertFalse(this.shape.contains(createRectangle(0, 0, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(0, 1, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(0, 2, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(0, 3, 1, 1)));
		assertTrue(this.shape.contains(createRectangle(0, 4, 1, 1)));
		assertTrue(this.shape.contains(createRectangle(0, 5, 1, 1)));
		assertTrue(this.shape.contains(createRectangle(0, 6, 1, 1)));
	}

	@Test
	public void rotateDouble() {
		this.shape.rotate(-MathConstants.DEMI_PI);
		assertEpsilonEquals(6, this.shape.getCenterX());
		assertEpsilonEquals(9, this.shape.getCenterY());
		assertEpsilonEquals(9.701400000000000e-01, this.shape.getFirstAxisX());
		assertEpsilonEquals(-2.425400000000000e-01, this.shape.getFirstAxisY());
		assertEpsilonEquals(9.21954, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(7.071100000000000e-01, this.shape.getSecondAxisX());
		assertEpsilonEquals(7.071100000000000e-01, this.shape.getSecondAxisY());
		assertEpsilonEquals(12.64911, this.shape.getSecondAxisExtent());
	}

	@Test
	public void getCenter() {
		Point2D c = this.shape.getCenter();
		assertEpsilonEquals(6, c.getX());
		assertEpsilonEquals(9, c.getY());
	}

	@Test
	public void getCenterX() {
		assertEpsilonEquals(6, this.shape.getCenterX());
	}

	@Test
	public void getCenterY() {
		assertEpsilonEquals(9, this.shape.getCenterY());
	}

	@Test
	public void setCenterDoubleDouble() {
		this.shape.setCenter(123.456, -789.123);
		assertEpsilonEquals(123.456, this.shape.getCenterX());
		assertEpsilonEquals(-789.123, this.shape.getCenterY());
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setCenterPoint2D() {
		this.shape.setCenter(createPoint(123.456, -789.123));
		assertEpsilonEquals(123.456, this.shape.getCenterX());
		assertEpsilonEquals(-789.123, this.shape.getCenterY());
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setCenterX() {
		this.shape.setCenterX(123.456);
		assertEpsilonEquals(123.456, this.shape.getCenterX());
		assertEpsilonEquals(cy, this.shape.getCenterY());
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setCenterY() {
		this.shape.setCenterY(123.456);
		assertEpsilonEquals(cx, this.shape.getCenterX());
		assertEpsilonEquals(123.456, this.shape.getCenterY());
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Test
	public void getFirstAxis() {
		Vector2D v = this.shape.getFirstAxis();
		assertEpsilonEquals(ux, v.getX());
		assertEpsilonEquals(uy, v.getY());
	}

	@Test
	public void getFirstAxisX() {
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
	}

	@Test
	public void getFirstAxisY() {
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
	}

	@Test
	public void getSecondAxis() {
		Vector2D v = this.shape.getSecondAxis();
		assertEpsilonEquals(vx, v.getX());
		assertEpsilonEquals(vy, v.getY());
	}

	@Test
	public void getSecondAxisX() {
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
	}

	@Test
	public void getSecondAxisY() {
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
	}

	@Test
	public void getFirstAxisExtent() {
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
	}

	@Test
	public void setFirstAxisExtent() {
		this.shape.setFirstAxisExtent(123.456);
		assertEpsilonEquals(cx, this.shape.getCenterX());
		assertEpsilonEquals(cy, this.shape.getCenterY());
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(123.456, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Test
	public void getSecondAxisExtent() {
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setSecondAxisExtent() {
		this.shape.setSecondAxisExtent(123.456);
		assertEpsilonEquals(cx, this.shape.getCenterX());
		assertEpsilonEquals(cy, this.shape.getCenterY());
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(123.456, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setFirstAxisDoubleDouble_unitVector() {
		Vector2D newU = createVector(123.456, 456.789).toUnitVector();
		this.shape.setFirstAxis(newU.getX(), newU.getY());
		assertEpsilonEquals(cx, this.shape.getCenterX());
		assertEpsilonEquals(cy, this.shape.getCenterY());
		assertEpsilonEquals(newU.getX(), this.shape.getFirstAxisX());
		assertEpsilonEquals(newU.getY(), this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Test(expected = AssertionError.class)
	public void setFirstAxisDoubleDouble_notUnitVector() {
		this.shape.setFirstAxis(123.456, 456.789);
	}

	@Test
	public void setFirstAxisVector2D_unitVector() {
		Vector2D newU = createVector(123.456, 456.789).toUnitVector();
		this.shape.setFirstAxis(newU);
		assertEpsilonEquals(cx, this.shape.getCenterX());
		assertEpsilonEquals(cy, this.shape.getCenterY());
		assertEpsilonEquals(newU.getX(), this.shape.getFirstAxisX());
		assertEpsilonEquals(newU.getY(), this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Test(expected = AssertionError.class)
	public void setFirstAxisVector2D_notUnitVector() {
		this.shape.setFirstAxis(createVector(123.456, 456.789));
	}

	@Test
	public void setFirstAxisVector2DDouble_unitVector() {
		Vector2D newU = createVector(123.456, 456.789).toUnitVector();
		this.shape.setFirstAxis(newU, 159.753);
		assertEpsilonEquals(cx, this.shape.getCenterX());
		assertEpsilonEquals(cy, this.shape.getCenterY());
		assertEpsilonEquals(newU.getX(), this.shape.getFirstAxisX());
		assertEpsilonEquals(newU.getY(), this.shape.getFirstAxisY());
		assertEpsilonEquals(159.753, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setFirstAxisDoubleDoubleDouble() {
		Vector2D newU = createVector(123.456, 456.789).toUnitVector();
		this.shape.setFirstAxis(newU.getX(), newU.getY(), 159.753);
		assertEpsilonEquals(cx, this.shape.getCenterX());
		assertEpsilonEquals(cy, this.shape.getCenterY());
		assertEpsilonEquals(newU.getX(), this.shape.getFirstAxisX());
		assertEpsilonEquals(newU.getY(), this.shape.getFirstAxisY());
		assertEpsilonEquals(159.753, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setSecondAxisDoubleDouble_unitVector() {
		Vector2D newV = createVector(123.456, 456.789).toUnitVector();
		this.shape.setSecondAxis(newV.getX(), newV.getY());
		assertEpsilonEquals(cx, this.shape.getCenterX());
		assertEpsilonEquals(cy, this.shape.getCenterY());
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(newV.getX(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newV.getY(), this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Test(expected = AssertionError.class)
	public void setSecondAxisDoubleDouble_notUnitVector() {
		this.shape.setSecondAxis(123.456, 456.789);
	}

	@Test
	public void setSecondAxisVector2D_unitVector() {
		Vector2D newV = createVector(123.456, 456.789).toUnitVector();
		this.shape.setSecondAxis(newV);
		assertEpsilonEquals(cx, this.shape.getCenterX());
		assertEpsilonEquals(cy, this.shape.getCenterY());
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(newV.getX(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newV.getY(), this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Test(expected = AssertionError.class)
	public void setSecondAxisVector2D_notUnitVector() {
		this.shape.setSecondAxis(createVector(123.456, 456.789));
	}

	@Test
	public void setSecondAxisVector2DDouble() {
		Vector2D newV = createVector(123.456, 456.789).toUnitVector();
		this.shape.setSecondAxis(newV, 159.753);
		assertEpsilonEquals(cx, this.shape.getCenterX());
		assertEpsilonEquals(cy, this.shape.getCenterY());
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(newV.getX(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newV.getY(), this.shape.getSecondAxisY());
		assertEpsilonEquals(159.753, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setSecondAxisDoubleDoubleDouble() {
		Vector2D newV = createVector(123.456, 456.789).toUnitVector();
		this.shape.setSecondAxis(newV.getX(), newV.getY(), 159.753);
		assertEpsilonEquals(cx, this.shape.getCenterX());
		assertEpsilonEquals(cy, this.shape.getCenterY());
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(newV.getX(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newV.getY(), this.shape.getSecondAxisY());
		assertEpsilonEquals(159.753, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		Vector2D newU = createVector(-456.789, 159.753).toUnitVector();
		Vector2D newV = createVector(123.456, 456.789).toUnitVector();
		this.shape.set(-6, -4, newU.getX(), newU.getY(), 147.369, newV.getX(), newV.getY(), 159.753);
		assertEpsilonEquals(-6, this.shape.getCenterX());
		assertEpsilonEquals(-4, this.shape.getCenterY());
		assertEpsilonEquals(newU.getX(), this.shape.getFirstAxisX());
		assertEpsilonEquals(newU.getY(), this.shape.getFirstAxisY());
		assertEpsilonEquals(147.369, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(newV.getX(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newV.getY(), this.shape.getSecondAxisY());
		assertEpsilonEquals(159.753, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setPoint2DVector2DDoubleVector2DDouble() {
		Vector2D newU = createVector(-456.789, 159.753).toUnitVector();
		Vector2D newV = createVector(123.456, 456.789).toUnitVector();
		this.shape.set(createPoint(-6, -4), newU, 147.369, newV, 159.753);
		assertEpsilonEquals(-6, this.shape.getCenterX());
		assertEpsilonEquals(-4, this.shape.getCenterY());
		assertEpsilonEquals(newU.getX(), this.shape.getFirstAxisX());
		assertEpsilonEquals(newU.getY(), this.shape.getFirstAxisY());
		assertEpsilonEquals(147.369, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(newV.getX(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newV.getY(), this.shape.getSecondAxisY());
		assertEpsilonEquals(159.753, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setFromPointCloudIterable() {
		double obrux = 0.8944271909999159;
		double obruy = -0.4472135954999579;
		double obre1 = 13.99999;
		double obrvx = 0.4472135954999579;
		double obrvy = 0.8944271909999159;
		double obre2 = 12.99999;

		this.shape.setFromPointCloud(Arrays.asList(
				createPoint(11.7082, -0.94427), createPoint(16.18034, 8),
				createPoint(-1.7082, 16.94427), createPoint(-6.18034, 8)));
		
		assertEpsilonEquals(5, this.shape.getCenterX());
		assertEpsilonEquals(8, this.shape.getCenterY());
		assertEpsilonEquals(obrux, this.shape.getFirstAxisX());
		assertEpsilonEquals(obruy, this.shape.getFirstAxisY());
		assertEpsilonEquals(10, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(obrvx, this.shape.getSecondAxisX());
		assertEpsilonEquals(obrvy, this.shape.getSecondAxisY());
		assertEpsilonEquals(5, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setFromPointCloudPoint2DArray() {
		double obrux = 0.8944271909999159;
		double obruy = -0.4472135954999579;
		double obre1 = 13.99999;
		double obrvx = 0.4472135954999579;
		double obrvy = 0.8944271909999159;
		double obre2 = 12.99999;

		this.shape.setFromPointCloud(
				createPoint(11.7082, -0.94427), createPoint(16.18034, 8),
				createPoint(-1.7082, 16.94427), createPoint(-6.18034, 8));
		
		assertEpsilonEquals(5, this.shape.getCenterX());
		assertEpsilonEquals(8, this.shape.getCenterY());
		assertEpsilonEquals(obrux, this.shape.getFirstAxisX());
		assertEpsilonEquals(obruy, this.shape.getFirstAxisY());
		assertEpsilonEquals(10, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(obrvx, this.shape.getSecondAxisX());
		assertEpsilonEquals(obrvy, this.shape.getSecondAxisY());
		assertEpsilonEquals(5, this.shape.getSecondAxisExtent());
	}

	@Override
	public void intersectsCircle2afp() {
		assertFalse(this.shape.intersects(createCircle(.5, .5, .5)));
		assertFalse(this.shape.intersects(createCircle(.5, 1.5, .5)));
		assertFalse(this.shape.intersects(createCircle(.5, 2.5, .5)));
		assertTrue(this.shape.intersects(createCircle(.5, 3.5, .5)));
		assertTrue(this.shape.intersects(createCircle(4.5, 3.5, .5)));
		assertFalse(this.shape.intersects(createCircle(10, -7, .5)));
		assertFalse(this.shape.intersects(createCircle(10.1, -7, .5)));
		assertTrue(this.shape.intersects(createCircle(10.2, -7, .5)));
		assertTrue(this.shape.intersects(createCircle(10, -1, 5)));
	}

	@Override
	public void intersectsEllipse2afp() {
		assertFalse(this.shape.intersects(createEllipse(0, 0, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(0, 1, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(0, 2, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(0, 3, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(0, 4, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(1, 3, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(5, 5, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(0.1, 1, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(0.2, 1, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(0.3, 1, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(0.4, 1, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(-7, 7.5, 2, 1)));
	}

	@Override
	public void intersectsSegment2afp() {
		assertFalse(this.shape.intersects(createSegment(0, 0, 1, 1)));
		assertTrue(this.shape.intersects(createSegment(5, 5, 4, 6)));
		assertTrue(this.shape.intersects(createSegment(2, -2, 5, 0)));
		assertFalse(this.shape.intersects(createSegment(-20, -5, -10, 6)));
		assertFalse(this.shape.intersects(createSegment(-5, 0, -10, 16)));
		assertTrue(this.shape.intersects(createSegment(-10, 1, 10, 20)));
	}

	@Override
	public void intersectsPath2afp() {
		Path2afp<?, ?, ?, ?, B> path = createPath();
		path.moveTo(-15,  2);
		path.lineTo(6, -9);
		path.lineTo(19, -9);
		path.lineTo(20, 26);
		path.lineTo(-6, 30);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));
	}

	@Override
	public void intersectsPathIterator2afp() {
		Path2afp<?, ?, ?, ?, B> path = createPath();
		path.moveTo(-15,  2);
		path.lineTo(6, -9);
		path.lineTo(19, -9);
		path.lineTo(20, 26);
		path.lineTo(-6, 30);
		assertFalse(this.shape.intersects(path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects(path.getPathIterator()));
	}

	@Override
	public void intersectsTriangle2afp() {
		assertTrue(this.shape.intersects(createTriangle(-5, 15, -3, 16, -8, 19)));
		assertTrue(this.shape.intersects(createTriangle(-5, 15, -8, 19, -3, 16)));
		assertFalse(this.shape.intersects(createTriangle(0, -5, 2, -4, -3, -1)));
		assertFalse(this.shape.intersects(createTriangle(0, -5, -3, -1, 2, -4)));
		assertFalse(this.shape.intersects(createTriangle(20, 0, 22, 1, 17, 4)));
		assertFalse(this.shape.intersects(createTriangle(20, 0, 17, 4, 22, 1)));
		assertFalse(this.shape.intersects(createTriangle(17.18034, 9, 19.18034, 10, 14.18034, 13)));
		assertFalse(this.shape.intersects(createTriangle(17.18034, 9, 14.18034, 13, 19.18034, 10)));
		assertTrue(this.shape.intersects(createTriangle(0, 10, 2, 11, -3, 14)));
		assertTrue(this.shape.intersects(createTriangle(0, 10, -3, 14, 2, 11)));
		assertTrue(this.shape.intersects(createTriangle(0, 20, 2, 21, -3, 24)));
	}

	@Override
	public void intersectsRectangle2afp() {
		assertFalse(this.shape.intersects(createRectangle(0, 0, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(0, 2, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(-5.5, 8.5, 1, 1)));
		assertFalse(this.shape.intersects(createRectangle(-6, 16, 1, 1)));
		assertFalse(this.shape.intersects(createRectangle(146, 16, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(12, 14, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(0, 8, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(10, -1, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(-15, -10, 35, 40)));
	}

	@Override
	public void intersectsParallelogram2afp() {
		double ux2 = -0.9284766908852592;
		double uy2 = 0.3713906763541037;
		double et1 = 5;
		double vx2 = 0.3713906763541037;
		double vy2 = 0.9284766908852592;
		double et2 = 3;
		assertFalse(this.shape.intersects(createParallelogram(-10, 0,
				ux2, uy2, et1, vx2, vy2, et2)));
		assertFalse(this.shape.intersects(createParallelogram(-15, 25,
				ux2, uy2, et1, vx2, vy2, et2)));
		assertFalse(this.shape.intersects(createParallelogram(2, -6,
				ux2, uy2, et1, vx2, vy2, et2)));
		assertFalse(this.shape.intersects(createParallelogram(2, -5,
				ux2, uy2, et1, vx2, vy2, et2)));
		assertTrue(this.shape.intersects(createParallelogram(2, -4,
				ux2, uy2, et1, vx2, vy2, et2)));
		assertTrue(this.shape.intersects(createParallelogram(pEx, pEy,
				ux2, uy2, et1, vx2, vy2, et2)));
		assertTrue(this.shape.intersects(createParallelogram(6, 6,
				ux2, uy2, et1, vx2, vy2, et2)));
		assertTrue(this.shape.intersects(createParallelogram(6, 6,
				ux2, uy2, 10 * et1, vx2, vy2, 10 * et2)));
	}

	@Override
	public void intersectsRoundRectangle2afp() {
		assertFalse(this.shape.intersects(createRoundRectangle(0, 0, 1, 1, .1, .05)));
		assertTrue(this.shape.intersects(createRoundRectangle(0, 2, 1, 1, .1, .05)));
		assertTrue(this.shape.intersects(createRoundRectangle(-5.5, 8.5, 1, 1, .1, .05)));
		assertFalse(this.shape.intersects(createRoundRectangle(-6, 16, 1, 1, .1, .05)));
		assertFalse(this.shape.intersects(createRoundRectangle(146, 16, 1, 1, .1, .05)));
		assertTrue(this.shape.intersects(createRoundRectangle(12, 14, 1, 1, .1, .05)));
		assertTrue(this.shape.intersects(createRoundRectangle(0, 8, 1, 1, .1, .05)));
		assertTrue(this.shape.intersects(createRoundRectangle(10, -1, 1, 1, .1, .05)));
		assertTrue(this.shape.intersects(createRoundRectangle(-15, -10, 35, 40, .1, .05)));
		assertFalse(this.shape.intersects(createRoundRectangle(-4.79634, 14.50886, 1, 1, .1, .05)));
	}

	@Override
	public void intersectsOrientedRectangle2afp() {
		OrientedRectangle2afp rectangle = createOrientedRectangle(
				6, 9,
				0.894427190999916, -0.447213595499958, 13.999990000000002,
				12.999989999999997);
		double ux2 = 0.55914166827779;
		double uy2 = 0.829072128825671;
		double et1 = 10;
		double vx2 = -0.989660599000356;
		double vy2 = -0.143429072318889;
		double et2 = 15;
		assertFalse(createParallelogram(
				-20, -20, ux2, uy2, et1, vx2, vy2, et2).intersects(rectangle));
		assertFalse(createParallelogram(
				-40, 20, ux2, uy2, et1, vx2, vy2, et2).intersects(rectangle));
		assertTrue(createParallelogram(
				-20, -10, ux2, uy2, et1, vx2, vy2, et2).intersects(rectangle));
		assertTrue(createParallelogram(
				10, -10, ux2, uy2, et1, vx2, vy2, et2).intersects(rectangle));
		assertTrue(createParallelogram(
				5, 5, ux2, uy2, et1, vx2, vy2, et2).intersects(rectangle));
	}

}