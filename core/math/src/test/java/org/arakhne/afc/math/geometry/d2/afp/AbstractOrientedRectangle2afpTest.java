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
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractOrientedRectangle2afpTest<T extends OrientedRectangle2afp<?, T, ?, ?, ?, B>,
		B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractShape2afpTest<T, B> {

	protected final double cx = 6;
	protected final double cy = 9;
	protected final double ux = 0.894427190999916;
	protected final double uy = -0.447213595499958;
	protected final double e1 = 13.999990000000002;
	protected final double vx = 0.447213595499958;
	protected final double vy = 0.894427190999916;
	protected final double e2 = 12.999989999999997;
	
	// Points' names are in the ggb diagram
	protected final double pEx = -12.33574;
	protected final double pEy = 3.63344;
	protected final double pFx = 12.7082;
	protected final double pFy = -8.88853;
	protected final double pGx = 24.33574;
	protected final double pGy = 14.36656;
	protected final double pHx = -0.7082;
	protected final double pHy = 26.88853;

	
	// Natural major axis for the box corners
	private final double mjx = -vx;
	private final double mjy = -vy;
	
	// Natural minor axis for the box corners
	private final double mnx = vy;
	private final double mny = -vx;

	@Override
	protected final T createShape() {
		return (T) createOrientedRectangle(cx, cy, ux, uy, e1, e2);
	}

	@Test
	public void staticFindsVectorProjectionRAxisVector() {
		assertEpsilonEquals(-e1, OrientedRectangle2afp.findsVectorProjectionRAxisVector(ux, uy, pEx - cx, pEy - cy));
		assertEpsilonEquals(e1, OrientedRectangle2afp.findsVectorProjectionRAxisVector(ux, uy, pFx - cx, pFy - cy));
		assertEpsilonEquals(e1, OrientedRectangle2afp.findsVectorProjectionRAxisVector(ux, uy, pGx - cx, pGy - cy));
		assertEpsilonEquals(-e1, OrientedRectangle2afp.findsVectorProjectionRAxisVector(ux, uy, pHx - cx, pHy - cy));
		assertEpsilonEquals(-1.34164, OrientedRectangle2afp.findsVectorProjectionRAxisVector(ux, uy, -cx, -cy));
	}

	@Test
	public void staticFindsVectorProjectionSAxisVector() {
		assertEpsilonEquals(-e2, OrientedRectangle2afp.findsVectorProjectionSAxisVector(ux, uy, pEx - cx, pEy - cy));
		assertEpsilonEquals(-e2, OrientedRectangle2afp.findsVectorProjectionSAxisVector(ux, uy, pFx - cx, pFy - cy));
		assertEpsilonEquals(e2, OrientedRectangle2afp.findsVectorProjectionSAxisVector(ux, uy, pGx - cx, pGy - cy));
		assertEpsilonEquals(e2, OrientedRectangle2afp.findsVectorProjectionSAxisVector(ux, uy, pHx - cx, pHy - cy));
		assertEpsilonEquals(-10.73313, OrientedRectangle2afp.findsVectorProjectionSAxisVector(ux, uy, -cx, -cy));
	}

	@Test
	public void staticCalculatesCenterPointAxisExtents() {
		List points = Arrays.asList(
				createPoint(pEx, pEy), createPoint(pGx, pGy),
				createPoint(pFx, pFy), createPoint(pEx, pEy));
		Vector2D R;
		Point2D center;
		Tuple2D extents;
		R = createVector(ux, uy);
		center = createPoint(Double.NaN, Double.NaN);
		extents = createVector(Double.NaN, Double.NaN);
		OrientedRectangle2afp.calculatesCenterPointAxisExtents(points, R, center, extents);
		assertEpsilonEquals(cx, center.getX());
		assertEpsilonEquals(cy, center.getY());
		assertEpsilonEquals(e1, extents.getX());
		assertEpsilonEquals(e2, extents.getY());
	}

	@Test
	public void staticContainsOrientedRectanglePoint() {
		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
				0, 0));
		assertFalse(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
				-20, 0));
		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
				12, -4));
		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
				14, 0));
		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
				17, 0));
		assertFalse(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
				18, 0));
		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
				21, 8));
		assertFalse(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
				22, 8));
		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
				8, 16));
		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
				-4, 20));
		assertFalse(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
				-4, 21));

		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1,e2,
				cx, cy));

		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(cx, cy, ux, uy, e1, e2,
				pEx, pEy));
	}

	@Test
	public void staticContainsOrientedRectangleRectangle() {
		assertTrue(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
				0, 0, 2, 1));
		assertTrue(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
				0, -1, 2, 1));
		assertTrue(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
				0, -2, 2, 1));
		assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
				0, -3, 2, 1));
		assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
				0, -4, 2, 1));
		assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
				0, -5, 2, 1));
		assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
				0, -5, 2, 1));
		assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(cx, cy, ux, uy, e1,e2,
				5, 25, 2, 1));
	}

	@Test
	public void staticFindsClosestFarthestPointsPointOrientedRectangle() {
		Point2D closest, farthest;

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				-20, 9,
				cx, cy, ux, uy, e1, e2,
				closest, farthest);
		assertEpsilonEquals(-11.72197, closest.getX());
		assertEpsilonEquals(4.86099, closest.getY());
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				0, 0,
				cx, cy, ux, uy, e1, e2,
				closest, farthest);
		assertEpsilonEquals(0, closest.getX());
		assertEpsilonEquals(0, closest.getY());
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				5, -10,
				cx, cy, ux, uy, e1, e2,
				closest, farthest);
		assertEpsilonEquals(6.98623, closest.getX());
		assertEpsilonEquals(-6.02754, closest.getY());
		assertEpsilonEquals(pHx, farthest.getX());
		assertEpsilonEquals(pHy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				14, -20,
				cx, cy, ux, uy, e1, e2,
				closest, farthest);
		assertEpsilonEquals(pFx, closest.getX());
		assertEpsilonEquals(pFy, closest.getY());
		assertEpsilonEquals(pHx, farthest.getX());
		assertEpsilonEquals(pHy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				-6, 15,
				cx, cy, ux, uy, e1, e2,
				closest, farthest);
		assertEpsilonEquals(-6, closest.getX());
		assertEpsilonEquals(15, closest.getY());
		assertEpsilonEquals(pFx, farthest.getX());
		assertEpsilonEquals(pFy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				0, 35,
				cx, cy, ux, uy, e1, e2,
				closest, farthest);
		assertEpsilonEquals(pHx, closest.getX());
		assertEpsilonEquals(pHy, closest.getY());
		assertEpsilonEquals(pFx, farthest.getX());
		assertEpsilonEquals(pFy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				10, 0,
				cx, cy, ux, uy, e1, e2,
				closest, farthest);
		assertEpsilonEquals(10, closest.getX());
		assertEpsilonEquals(0, closest.getY());
		assertEpsilonEquals(pHx, farthest.getX());
		assertEpsilonEquals(pHy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				16, -4,
				cx, cy, ux, uy, e1, e2,
				closest, farthest);
		assertEpsilonEquals(15.32197, closest.getX());
		assertEpsilonEquals(-3.66099, closest.getY());
		assertEpsilonEquals(pHx, farthest.getX());
		assertEpsilonEquals(pHy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				-5, 25,
				cx, cy, ux, uy, e1, e2,
				closest, farthest);
		assertEpsilonEquals(-2.32197, closest.getX());
		assertEpsilonEquals(23.66099, closest.getY());
		assertEpsilonEquals(pFx, farthest.getX());
		assertEpsilonEquals(pFy, farthest.getY());
	}

	@Test
	public void staticIntersectsOrientedRectangleSegment() {
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				-5, -5, 0, -7));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				-20, 0, -25, 2));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				-10, 15, -11, 17));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				-1, 30, -2, 40));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				10, 30, 15, 40));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				30, 15, 40, 16));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				20, 0, 25, 2));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				12, -15, 12, -16));

		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				15, -15, 35, 25));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				35, 25, -10, 40));

		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				-5, -5, 5, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				-10, 15, 0, 10));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				0, 20, 15, 25));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				15, 5, 30, 10));

		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				-5, -5, -10, 15));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				-10, 15, 15, 25));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				15, 25, 20, 0));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				20, 0, 0, -10));
		
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				15, 25, 0, -10));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				-10, 15, 20, 0));

		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(cx, cy, ux, uy, e1, e2,
				0, 5, 10, 16));
	}

	@Test
	public void staticIntersectsOrientedRectangleCircle() {
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
				0, -3.2, .5));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
				0, -3.1, .5));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
				0, -3, .5));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
				6, 2, .5));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
				pEx, pEy, .5));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
				pFx, pFy, .5));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
				-9, 10, .5));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(cx, cy, ux, uy, e1, e2,
				2, 10, 50));
	}

	@Test
	public void staticIntersectsOrientedRectangleRectangle() {
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
				0, -5, 2, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
				0, -4.5, 2, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
				0, -4, 2, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
				4, 4, 2, 1));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
				20, -2, 2, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(cx, cy, ux, uy, e1, e2,
				-15, -10, 50, 50));
	}

	@Test
	public void staticIntersectsOrientedRectangleEllipse() {
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(cx, cy, ux, uy, e1, e2,
				0, -5, 2, 1));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(cx, cy, ux, uy, e1, e2,
				0, -4.5, 2, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(cx, cy, ux, uy, e1, e2,
				0, -4, 2, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(cx, cy, ux, uy, e1, e2,
				4, 4, 2, 1));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(cx, cy, ux, uy, e1, e2,
				20, -2, 2, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(cx, cy, ux, uy, e1, e2,
				-15, -10, 50, 50));
	}

	@Test
	public void staticIntersectsOrientedRectangleTriangle() {
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
				-10, 15, -8, 16, -13, 19));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
				-5, 30, -3, 31, -8, 34));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
				15, 25, 17, 26, 12, 29));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
				40, 15, 42, 16, 37, 19));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
				35, 0, 37, 1, 32, 4));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
				15, -20, 17, -19, 12, -16));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
				-5, -10, -3, -9, -8, -6));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
				-25, -5, -23, -4, -28, -1));

		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
				-4, -2, -2, -1, -7, -2));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
				-2, 4, 0, 5, -5, 8));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
				20, 5, 22, 6, 17, 9));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
				20, 5, 22, 6, -10, 15));

		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(cx, cy, ux, uy, e1, e2,
				50, 30, 0, -50, -30, 31));
	}

	@Test
	public void staticIntersectsOrientedRectangleOrientedRectangle() {
		double ux2 = -0.9284766908852592;
		double uy2 = 0.3713906763541037;
		double et1 = 5;
		double vx2 = 0.3713906763541037;
		double vy2 = 0.9284766908852592;
		double et2 = 3;
		// D + (-0.9284766908852592,0.3713906763541037) * 5 + (0.3713906763541037,0.9284766908852592) * 3
		// D - (-0.9284766908852592,0.3713906763541037) * 5 + (0.3713906763541037,0.9284766908852592) * 3
		// D - (-0.9284766908852592,0.3713906763541037) * 5 - (0.3713906763541037,0.9284766908852592) * 3
		// D + (-0.9284766908852592,0.3713906763541037) * 5 - (0.3713906763541037,0.9284766908852592) * 3
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1,e2,
				-10, -2, ux2, uy2, et1, et2));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1, e2,
				-15, 25, ux2, uy2, et1, et2));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1, e2,
				2, -8, ux2, uy2, et1, et2));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1, e2,
				2, -7, ux2, uy2, et1, et2));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1, e2,
				2, -6, ux2, uy2, et1, et2));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1, e2,
				pEx, pEy, ux2, uy2, et1, et2));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1, e2,
				6, 6, ux2, uy2, et1, et2));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(cx, cy, ux, uy, e1, e2,
				6, 6, ux2, uy2, 10 * et1, 10 * et2));
	}

	@Test
	public void staticIntersectsOrientedRectangleRoundRectangle() {
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
				0, 0, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
				-9, 15, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
				-8.7, 15, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
				-8.7, 15, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
				-8.65, 15, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
				-8.64, 15, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
				-8.63, 15, 2, 1, .1, .05));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
				-8.62, 15, 2, 1, .1, .05));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
				-8, 15, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
				10, 25, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(cx, cy, ux, uy, e1, e2,
				20, -5, 2, 1, .1, .05));
	}

	@Override
	public void containsDoubleDouble() {
		assertTrue(this.shape.contains(0, 0));
		assertFalse(this.shape.contains(-20, 0));
		assertTrue(this.shape.contains(12, -4));
		assertTrue(this.shape.contains(14, 0));
		assertTrue(this.shape.contains(17, 0));
		assertFalse(this.shape.contains(18, 0));
		assertTrue(this.shape.contains(21, 8));
		assertFalse(this.shape.contains(22, 8));
		assertTrue(this.shape.contains(8, 16));
		assertTrue(this.shape.contains(-4, 20));
		assertFalse(this.shape.contains(-4, 21));
		assertTrue(this.shape.contains(cx, cy));
		assertTrue(this.shape.contains(pEx, pEy));
	}

	@Override
	public void containsPoint2D() {
		assertTrue(this.shape.contains(createPoint(0, 0)));
		assertFalse(this.shape.contains(createPoint(-20, 0)));
		assertTrue(this.shape.contains(createPoint(12, -4)));
		assertTrue(this.shape.contains(createPoint(14, 0)));
		assertTrue(this.shape.contains(createPoint(17, 0)));
		assertFalse(this.shape.contains(createPoint(18, 0)));
		assertTrue(this.shape.contains(createPoint(21, 8)));
		assertFalse(this.shape.contains(createPoint(22, 8)));
		assertTrue(this.shape.contains(createPoint(8, 16)));
		assertTrue(this.shape.contains(createPoint(-4, 20)));
		assertFalse(this.shape.contains(createPoint(-4, 21)));
		assertTrue(this.shape.contains(createPoint(cx, cy)));
		assertTrue(this.shape.contains(createPoint(pEx, pEy)));
	}

	@Override
	public void containsRectangle2afp() {
		assertTrue(this.shape.contains(createRectangle(0, 0, 2, 1)));
		assertTrue(this.shape.contains(createRectangle(0, -1, 2, 1)));
		assertTrue(this.shape.contains(createRectangle(0, -2, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(0, -3, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(0, -4, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(0, -5, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(0, -5, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(5, 25, 2, 1)));

	}

	@Override
	public void containsShape2D() {
		assertTrue(this.shape.contains(createCircle(0, 0, 2)));
		assertFalse(this.shape.contains(createCircle(0, -1, 2)));
		assertFalse(this.shape.contains(createCircle(0, -2, 2)));
		assertFalse(this.shape.contains(createCircle(0, -3, 2)));
		assertFalse(this.shape.contains(createCircle(0, -4, 2)));
		assertFalse(this.shape.contains(createCircle(0, -5, 2)));
		assertFalse(this.shape.contains(createCircle(0, -5, 2)));
		assertFalse(this.shape.contains(createCircle(5, 25, 2)));
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
		assertFalse(this.shape.equals(createOrientedRectangle(0, cy, ux, uy, e1, e2)));
		assertFalse(this.shape.equals(createOrientedRectangle(cx, cy, ux, uy, e1, 20)));
		assertFalse(this.shape.equals(createSegment(5, 8, 6, 10)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createOrientedRectangle(cx, cy, ux, uy, e1, e2)));
	}

	@Override
	public void equalsObject_withPathIterator() {
		assertFalse(this.shape.equals((PathIterator2afp) null));
		assertFalse(this.shape.equals(createOrientedRectangle(0, cy, ux, uy, e1, e2).getPathIterator()));
		assertFalse(this.shape.equals(createOrientedRectangle(cx, cy, ux, uy, e1, 20).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 6, 10).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createOrientedRectangle(cx, cy, ux, uy, e1, e2).getPathIterator()));
	}

	@Override
	public void equalsToPathIterator() {
		assertFalse(this.shape.equalsToPathIterator(null));
		assertFalse(this.shape.equalsToPathIterator(createOrientedRectangle(0, cy, ux, uy, e1, e2).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createOrientedRectangle(cx, cy, ux, uy, e1, 20).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 6, 10).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createOrientedRectangle(cx, cy, ux, uy, e1, e2).getPathIterator()));
	}

	@Override
	public void equalsToShape() {
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createOrientedRectangle(0, cy, ux, uy, e1, e2)));
		assertFalse(this.shape.equalsToShape((T) createOrientedRectangle(cx, cy, ux, uy, e1, 20)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createOrientedRectangle(cx, cy, ux, uy, e1, e2)));
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
	public void getClosestPointTo() {
		Point2D closest, farthest;

		closest = this.shape.getClosestPointTo(createPoint(-20, 9));
		assertEpsilonEquals(-11.72197, closest.getX());
		assertEpsilonEquals(4.86099, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(0, 0));
		assertEpsilonEquals(0, closest.getX());
		assertEpsilonEquals(0, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(5, -10));
		assertEpsilonEquals(6.98623, closest.getX());
		assertEpsilonEquals(-6.02754, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(14, -20));
		assertEpsilonEquals(pFx, closest.getX());
		assertEpsilonEquals(pFy, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(-6, 15));
		assertEpsilonEquals(-6, closest.getX());
		assertEpsilonEquals(15, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(0, 35));
		assertEpsilonEquals(pHx, closest.getX());
		assertEpsilonEquals(pHy, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(10, 0));
		assertEpsilonEquals(10, closest.getX());
		assertEpsilonEquals(0, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(16, -4));
		assertEpsilonEquals(15.32197, closest.getX());
		assertEpsilonEquals(-3.66099, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(-5, 25));
		assertEpsilonEquals(-2.32197, closest.getX());
		assertEpsilonEquals(23.66099, closest.getY());
	}

	@Override
	public void getFarthestPointTo() {
		Point2D farthest;

		farthest = this.shape.getFarthestPointTo(createPoint(-20, 9));
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(0, 0));
		assertEpsilonEquals(pGx, farthest.getX());
		assertEpsilonEquals(pGy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(5, -10));
		assertEpsilonEquals(pHx, farthest.getX());
		assertEpsilonEquals(pHy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(14, -20));
		assertEpsilonEquals(pHx, farthest.getX());
		assertEpsilonEquals(pHy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(-6, 15));
		assertEpsilonEquals(pFx, farthest.getX());
		assertEpsilonEquals(pFy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(0, 35));
		assertEpsilonEquals(pFx, farthest.getX());
		assertEpsilonEquals(pFy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(10, 0));
		assertEpsilonEquals(pHx, farthest.getX());
		assertEpsilonEquals(pHy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(16, -4));
		assertEpsilonEquals(pHx, farthest.getX());
		assertEpsilonEquals(pHy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(-5, 25));
		assertEpsilonEquals(pFx, farthest.getX());
		assertEpsilonEquals(pFy, farthest.getY());
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
		assertEpsilonEquals(9.2551, this.shape.getDistance(createPoint(-20, 9)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(0, 0)));
		assertEpsilonEquals(4.44135, this.shape.getDistance(createPoint(5, -10)));
		assertEpsilonEquals(11.18631, this.shape.getDistance(createPoint(14, -20)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(-6, 15)));
		assertEpsilonEquals(8.14233, this.shape.getDistance(createPoint(0, 35)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(10, 0)));
		assertEpsilonEquals(.75805, this.shape.getDistance(createPoint(16, -4)));
		assertEpsilonEquals(2.99413, this.shape.getDistance(createPoint(-5, 25)));
	}

	@Override
	public void getDistanceSquared() {
		assertEpsilonEquals(85.65719, this.shape.getDistanceSquared(createPoint(-20, 9)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(0, 0)));
		assertEpsilonEquals(19.72555, this.shape.getDistanceSquared(createPoint(5, -10)));
		assertEpsilonEquals(125.13351, this.shape.getDistanceSquared(createPoint(14, -20)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(-6, 15)));
		assertEpsilonEquals(66.29749, this.shape.getDistanceSquared(createPoint(0, 35)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(10, 0)));
		assertEpsilonEquals(.57465, this.shape.getDistanceSquared(createPoint(16, -4)));
		assertEpsilonEquals(8.96479, this.shape.getDistanceSquared(createPoint(-5, 25)));
	}

	@Override
	public void getDistanceL1() {
		assertEpsilonEquals(12.417, this.shape.getDistanceL1(createPoint(-20, 9)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(0, 0)));
		assertEpsilonEquals(5.95869, this.shape.getDistanceL1(createPoint(5, -10)));
		assertEpsilonEquals(12.40327, this.shape.getDistanceL1(createPoint(14, -20)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(-6, 15)));
		assertEpsilonEquals(8.81967, this.shape.getDistanceL1(createPoint(0, 35)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(10, 0)));
		assertEpsilonEquals(1.01704, this.shape.getDistanceL1(createPoint(16, -4)));
		assertEpsilonEquals(4.01704, this.shape.getDistanceL1(createPoint(-5, 25)));
	}

	@Override
	public void getDistanceLinf() {
		assertEpsilonEquals(8.278, this.shape.getDistanceLinf(createPoint(-20, 9)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(0, 0)));
		assertEpsilonEquals(3.97246, this.shape.getDistanceLinf(createPoint(5, -10)));
		assertEpsilonEquals(11.11147, this.shape.getDistanceLinf(createPoint(14, -20)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(-6, 15)));
		assertEpsilonEquals(8.11147, this.shape.getDistanceLinf(createPoint(0, 35)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(10, 0)));
		assertEpsilonEquals(0.67803, this.shape.getDistanceLinf(createPoint(16, -4)));
		assertEpsilonEquals(2.67803, this.shape.getDistanceLinf(createPoint(-5, 25)));
	}

	@Override
	public void setIT() {
		this.shape.set((T) createOrientedRectangle(17, 20, 1, 0, 15, 14));
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
		assertEpsilonEquals(pEx, box.getMinX());
		assertEpsilonEquals(pFy, box.getMinY());
		assertEpsilonEquals(pGx, box.getMaxX());
		assertEpsilonEquals(pHy, box.getMaxY());
	}

	@Override
	public void toBoundingBoxB() {
		B box = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(pEx, box.getMinX());
		assertEpsilonEquals(pFy, box.getMinY());
		assertEpsilonEquals(pGx, box.getMaxX());
		assertEpsilonEquals(pHy, box.getMaxY());
	}

	@Test
	public void rotateDouble() {
		this.shape.rotate(-MathConstants.DEMI_PI);
		assertEpsilonEquals(6, this.shape.getCenterX());
		assertEpsilonEquals(9, this.shape.getCenterY());
		assertEpsilonEquals(-4.472135954999580e-01, this.shape.getFirstAxisX());
		assertEpsilonEquals(-8.944271909999160e-01, this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(8.944271909999160e-01, this.shape.getSecondAxisX());
		assertEpsilonEquals(-4.472135954999580e-01, this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
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
		assertEpsilonEquals(-newU.getY(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newU.getX(), this.shape.getSecondAxisY());
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
		assertEpsilonEquals(-newU.getY(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newU.getX(), this.shape.getSecondAxisY());
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
		assertEpsilonEquals(-newU.getY(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newU.getX(), this.shape.getSecondAxisY());
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
		assertEpsilonEquals(-newU.getY(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newU.getX(), this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setSecondAxisDoubleDouble_unitVector() {
		Vector2D newV = createVector(123.456, 456.789).toUnitVector();
		this.shape.setSecondAxis(newV.getX(), newV.getY());
		assertEpsilonEquals(cx, this.shape.getCenterX());
		assertEpsilonEquals(cy, this.shape.getCenterY());
		assertEpsilonEquals(newV.getY(), this.shape.getFirstAxisX());
		assertEpsilonEquals(-newV.getX(), this.shape.getFirstAxisY());
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
		assertEpsilonEquals(newV.getY(), this.shape.getFirstAxisX());
		assertEpsilonEquals(-newV.getX(), this.shape.getFirstAxisY());
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
		assertEpsilonEquals(newV.getY(), this.shape.getFirstAxisX());
		assertEpsilonEquals(-newV.getX(), this.shape.getFirstAxisY());
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
		assertEpsilonEquals(newV.getY(), this.shape.getFirstAxisX());
		assertEpsilonEquals(-newV.getX(), this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(newV.getX(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newV.getY(), this.shape.getSecondAxisY());
		assertEpsilonEquals(159.753, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setDoubleDoubleDoubleDoubleDoubleDouble() {
		Vector2D newU = createVector(-456.789, 159.753).toUnitVector();
		this.shape.set(-6, -4, newU.getX(), newU.getY(), 147.369, 159.753);
		assertEpsilonEquals(-6, this.shape.getCenterX());
		assertEpsilonEquals(-4, this.shape.getCenterY());
		assertEpsilonEquals(newU.getX(), this.shape.getFirstAxisX());
		assertEpsilonEquals(newU.getY(), this.shape.getFirstAxisY());
		assertEpsilonEquals(147.369, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(-newU.getY(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newU.getX(), this.shape.getSecondAxisY());
		assertEpsilonEquals(159.753, this.shape.getSecondAxisExtent());
	}

	@Test
	public void setPoint2DVector2DDoubleVector2DDouble() {
		Vector2D newU = createVector(-456.789, 159.753).toUnitVector();
		this.shape.set(createPoint(-6, -4), newU, 147.369, 159.753);
		assertEpsilonEquals(-6, this.shape.getCenterX());
		assertEpsilonEquals(-4, this.shape.getCenterY());
		assertEpsilonEquals(newU.getX(), this.shape.getFirstAxisX());
		assertEpsilonEquals(newU.getY(), this.shape.getFirstAxisY());
		assertEpsilonEquals(147.369, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(-newU.getY(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newU.getX(), this.shape.getSecondAxisY());
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

		this.shape.setFromPointCloud((List) Arrays.asList(
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

	@Test
	public void orthogonalAxes_changeFirstAxis() {
		assertTrue(Vector2D.isOrthogonal(ux, uy, vx, vy));
		this.shape.setFirstAxis(0.500348, 0.865824);
		assertEpsilonEquals(-0.865824, this.shape.getSecondAxisX());
		assertEpsilonEquals(0.500348, this.shape.getSecondAxisY());
	}
	
	@Test
	public void orthogonalAxes_changeSecondAxis() {
		assertTrue(Vector2D.isOrthogonal(ux, uy, vx, vy));
		this.shape.setSecondAxis(0.500348, 0.865824);
		assertEpsilonEquals(0.865824, this.shape.getFirstAxisX());
		assertEpsilonEquals(-0.500348, this.shape.getFirstAxisY());
	}

	@Override
	public void intersectsSegment2afp() {
		assertFalse(this.shape.intersects(createSegment(-5, -5, 0, -7)));
		assertFalse(this.shape.intersects(createSegment(-20, 0, -25, 2)));
		assertFalse(this.shape.intersects(createSegment(-10, 15, -11, 17)));
		assertFalse(this.shape.intersects(createSegment(-1, 30, -2, 40)));
		assertFalse(this.shape.intersects(createSegment(10, 30, 15, 40)));
		assertFalse(this.shape.intersects(createSegment(30, 15, 40, 16)));
		assertFalse(this.shape.intersects(createSegment(20, 0, 25, 2)));
		assertFalse(this.shape.intersects(createSegment(12, -15, 12, -16)));
		assertFalse(this.shape.intersects(createSegment(15, -15, 35, 25)));
		assertFalse(this.shape.intersects(createSegment(35, 25, -10, 40)));
		assertTrue(this.shape.intersects(createSegment(-5, -5, 5, 1)));
		assertTrue(this.shape.intersects(createSegment(-10, 15, 0, 10)));
		assertTrue(this.shape.intersects(createSegment(0, 20, 15, 25)));
		assertTrue(this.shape.intersects(createSegment(15, 5, 30, 10)));
		assertTrue(this.shape.intersects(createSegment(-5, -5, -10, 15)));
		assertTrue(this.shape.intersects(createSegment(-10, 15, 15, 25)));
		assertTrue(this.shape.intersects(createSegment(15, 25, 20, 0)));
		assertTrue(this.shape.intersects(createSegment(20, 0, 0, -10)));
		assertTrue(this.shape.intersects(createSegment(15, 25, 0, -10)));
		assertTrue(this.shape.intersects(createSegment(-10, 15, 20, 0)));
		assertTrue(this.shape.intersects(createSegment(0, 5, 10, 16)));
	}

	@Override
	public void intersectsCircle2afp() {
		assertFalse(this.shape.intersects(createCircle(0, -3.2, .5)));
		assertFalse(this.shape.intersects(createCircle(0, -3.1, .5)));
		assertTrue(this.shape.intersects(createCircle(0, -3, .5)));
		assertTrue(this.shape.intersects(createCircle(6, 2, .5)));
		assertTrue(this.shape.intersects(createCircle(pEx, pEy, .5)));
		assertTrue(this.shape.intersects(createCircle(pFx, pFy, .5)));
		assertTrue(this.shape.intersects(createCircle(-9, 10, .5)));
		assertTrue(this.shape.intersects(createCircle(2, 10, 50)));
	}

	@Override
	public void intersectsRectangle2afp() {
		assertFalse(this.shape.intersects(createRectangle(0, -5, 2, 1)));
		assertTrue(this.shape.intersects(createRectangle(0, -4.5, 2, 1)));
		assertTrue(this.shape.intersects(createRectangle(0, -4, 2, 1)));
		assertTrue(this.shape.intersects(createRectangle(4, 4, 2, 1)));
		assertFalse(this.shape.intersects(createRectangle(20, -2, 2, 1)));
		assertTrue(this.shape.intersects(createRectangle(-15, -10, 50, 50)));
	}

	@Override
	public void intersectsEllipse2afp() {
		assertFalse(this.shape.intersects(createEllipse(0, -5, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(0, -4.5, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(0, -4, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(4, 4, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(20, -2, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(-15, -10, 50, 50)));
	}

	@Override
	public void intersectsTriangle2afp() {
		assertFalse(this.shape.intersects(createTriangle(-10, 15, -8, 16, -13, 19)));
		assertFalse(this.shape.intersects(createTriangle(-5, 30, -3, 31, -8, 34)));
		assertFalse(this.shape.intersects(createTriangle(15, 25, 17, 26, 12, 29)));
		assertFalse(this.shape.intersects(createTriangle(40, 15, 42, 16, 37, 19)));
		assertFalse(this.shape.intersects(createTriangle(35, 0, 37, 1, 32, 4)));
		assertFalse(this.shape.intersects(createTriangle(15, -20, 17, -19, 12, -16)));
		assertFalse(this.shape.intersects(createTriangle(-5, -10, -3, -9, -8, -6)));
		assertFalse(this.shape.intersects(createTriangle(-25, -5, -23, -4, -28, -1)));
		assertTrue(this.shape.intersects(createTriangle(-4, -2, -2, -1, -7, -2)));
		assertTrue(this.shape.intersects(createTriangle(-2, 4, 0, 5, -5, 8)));
		assertTrue(this.shape.intersects(createTriangle(20, 5, 22, 6, 17, 9)));
		assertTrue(this.shape.intersects(createTriangle(20, 5, 22, 6, -10, 15)));
		assertTrue(this.shape.intersects(createTriangle(50, 30, 0, -50, -30, 31)));
	}

	@Override
	public void intersectsPath2afp() {
		Path2afp<?, ?, ?, ?, ?, B> path = createPath();
		path.moveTo(-15,  2);
		path.lineTo(6, -9);
		path.lineTo(19, -9);
		path.lineTo(30, 26);
		path.lineTo(-6, 30);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));
	}

	@Override
	public void intersectsPathIterator2afp() {
		Path2afp<?, ?, ?, ?, ?, B> path = createPath();
		path.moveTo(-15,  2);
		path.lineTo(6, -9);
		path.lineTo(19, -9);
		path.lineTo(30, 26);
		path.lineTo(-6, 30);
		assertFalse(this.shape.intersects(path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects(path.getPathIterator()));
	}

	@Override
	public void intersectsParallelogram2afp() {
		double ux2 = 0.55914166827779;
		double uy2 = 0.829072128825671;
		double et1 = 10;
		double vx2 = -0.989660599000356;
		double vy2 = -0.143429072318889;
		double et2 = 15;
		assertFalse(this.shape.intersects(createParallelogram(
				-20, -20, ux2, uy2, et1, vx2, vy2, et2)));
		assertFalse(this.shape.intersects(createParallelogram(
				-40, 20, ux2, uy2, et1, vx2, vy2, et2)));
		assertTrue(this.shape.intersects(createParallelogram(
				-20, -10, ux2, uy2, et1, vx2, vy2, et2)));
		assertTrue(this.shape.intersects(createParallelogram(
				10, -10, ux2, uy2, et1, vx2, vy2, et2)));
		assertTrue(this.shape.intersects(createParallelogram(
				5, 5, ux2, uy2, et1, vx2, vy2, et2)));
	}

	@Override
	public void intersectsRoundRectangle2afp() {
		assertTrue(this.shape.intersects(createRoundRectangle(0, 0, 2, 1, .1, .05)));
		assertFalse(this.shape.intersects(createRoundRectangle(-9, 15, 2, 1, .1, .05)));
		assertFalse(this.shape.intersects(createRoundRectangle(-8.7, 15, 2, 1, .1, .05)));
		assertFalse(this.shape.intersects(createRoundRectangle(-8.7, 15, 2, 1, .1, .05)));
		assertFalse(this.shape.intersects(createRoundRectangle(-8.65, 15, 2, 1, .1, .05)));
		assertFalse(this.shape.intersects(createRoundRectangle(-8.64, 15, 2, 1, .1, .05)));
		assertFalse(this.shape.intersects(createRoundRectangle(-8.63, 15, 2, 1, .1, .05)));
		assertTrue(this.shape.intersects(createRoundRectangle(-8.62, 15, 2, 1, .1, .05)));
		assertTrue(this.shape.intersects(createRoundRectangle(-8, 15, 2, 1, .1, .05)));
		assertFalse(this.shape.intersects(createRoundRectangle(10, 25, 2, 1, .1, .05)));
		assertFalse(this.shape.intersects(createRoundRectangle(20, -5, 2, 1, .1, .05)));
	}

	@Override
	public void intersectsOrientedRectangle2afp() {
		double ux2 = -0.9284766908852592;
		double uy2 = 0.3713906763541037;
		double et1 = 5;
		double vx2 = 0.3713906763541037;
		double vy2 = 0.9284766908852592;
		double et2 = 3;
		assertFalse(this.shape.intersects(createOrientedRectangle(-10, -2, ux2, uy2, et1, et2)));
		assertFalse(this.shape.intersects(createOrientedRectangle(-15, 25, ux2, uy2, et1, et2)));
		assertFalse(this.shape.intersects(createOrientedRectangle(2, -8, ux2, uy2, et1, et2)));
		assertTrue(this.shape.intersects(createOrientedRectangle(2, -7, ux2, uy2, et1, et2)));
		assertTrue(this.shape.intersects(createOrientedRectangle(2, -6, ux2, uy2, et1, et2)));
		assertTrue(this.shape.intersects(createOrientedRectangle(pEx, pEy, ux2, uy2, et1, et2)));
		assertTrue(this.shape.intersects(createOrientedRectangle(6, 6, ux2, uy2, et1, et2)));
		assertTrue(this.shape.intersects(createOrientedRectangle(6, 6, ux2, uy2, 10 * et1, 10 * et2)));
	}

	@Override
	public void intersectsShape2D() {
		assertTrue(this.shape.intersects((Shape2D) createCircle(6, 2, .5)));
		assertTrue(this.shape.intersects((Shape2D) createRectangle(4, 4, 2, 1)));
	}

	@Override
	public void operator_addVector2D() {
		this.shape.operator_add(createVector(123.456, 789.123));
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
	public void operator_plusVector2D() {
		T shape = this.shape.operator_plus(createVector(123.456, 789.123));
		assertEpsilonEquals(cx + 123.456, shape.getCenterX());
		assertEpsilonEquals(cy + 789.123, shape.getCenterY());
		assertEpsilonEquals(ux, shape.getFirstAxisX());
		assertEpsilonEquals(uy, shape.getFirstAxisY());
		assertEpsilonEquals(e1, shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, shape.getSecondAxisX());
		assertEpsilonEquals(vy, shape.getSecondAxisY());
		assertEpsilonEquals(e2, shape.getSecondAxisExtent());
	}

	@Override
	public void operator_removeVector2D() {
		this.shape.operator_remove(createVector(123.456, 789.123));
		assertEpsilonEquals(cx - 123.456, this.shape.getCenterX());
		assertEpsilonEquals(cy - 789.123, this.shape.getCenterY());
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
	}

	@Override
	public void operator_minusVector2D() {
		T shape = this.shape.operator_minus(createVector(123.456, 789.123));
		assertEpsilonEquals(cx - 123.456, shape.getCenterX());
		assertEpsilonEquals(cy - 789.123, shape.getCenterY());
		assertEpsilonEquals(ux, shape.getFirstAxisX());
		assertEpsilonEquals(uy, shape.getFirstAxisY());
		assertEpsilonEquals(e1, shape.getFirstAxisExtent());
		assertEpsilonEquals(vx, shape.getSecondAxisX());
		assertEpsilonEquals(vy, shape.getSecondAxisY());
		assertEpsilonEquals(e2, shape.getSecondAxisExtent());
	}

	@Override
	public void operator_multiplyTransform2D() {
		PathIterator2afp pi = this.shape.operator_multiply(null).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
		assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
		assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
		assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
		assertElement(pi, PathElementType.CLOSE, pGx, pGy);
		assertNoElement(pi);

		Transform2D transform;
		
		transform = new Transform2D();
		pi = this.shape.operator_multiply(transform).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
		assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
		assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
		assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
		assertElement(pi, PathElementType.CLOSE, pGx, pGy);
		assertNoElement(pi);

		transform = new Transform2D();
		transform.setTranslation(18,  -45);
		pi = this.shape.operator_multiply(transform).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, pGx + 18, pGy - 45);
		assertElement(pi, PathElementType.LINE_TO, pHx + 18, pHy - 45);
		assertElement(pi, PathElementType.LINE_TO, pEx + 18, pEy - 45);
		assertElement(pi, PathElementType.LINE_TO, pFx + 18, pFy - 45);
		assertElement(pi, PathElementType.CLOSE, pGx + 18, pGy - 45);
		assertNoElement(pi);

	}

	@Override
	public void operator_andPoint2D() {
		assertTrue(this.shape.operator_and(createPoint(0, 0)));
		assertFalse(this.shape.operator_and(createPoint(-20, 0)));
		assertTrue(this.shape.operator_and(createPoint(12, -4)));
		assertTrue(this.shape.operator_and(createPoint(14, 0)));
		assertTrue(this.shape.operator_and(createPoint(17, 0)));
		assertFalse(this.shape.operator_and(createPoint(18, 0)));
		assertTrue(this.shape.operator_and(createPoint(21, 8)));
		assertFalse(this.shape.operator_and(createPoint(22, 8)));
		assertTrue(this.shape.operator_and(createPoint(8, 16)));
		assertTrue(this.shape.operator_and(createPoint(-4, 20)));
		assertFalse(this.shape.operator_and(createPoint(-4, 21)));
		assertTrue(this.shape.operator_and(createPoint(cx, cy)));
		assertTrue(this.shape.operator_and(createPoint(pEx, pEy)));
	}

	@Override
	public void operator_andShape2D() {
		assertTrue(this.shape.operator_and(createCircle(6, 2, .5)));
		assertTrue(this.shape.operator_and(createRectangle(4, 4, 2, 1)));
	}

	@Override
	public void operator_upToPoint2D() {
		assertEpsilonEquals(9.2551, this.shape.operator_upTo(createPoint(-20, 9)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(0, 0)));
		assertEpsilonEquals(4.44135, this.shape.operator_upTo(createPoint(5, -10)));
		assertEpsilonEquals(11.18631, this.shape.operator_upTo(createPoint(14, -20)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(-6, 15)));
		assertEpsilonEquals(8.14233, this.shape.operator_upTo(createPoint(0, 35)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(10, 0)));
		assertEpsilonEquals(.75805, this.shape.operator_upTo(createPoint(16, -4)));
		assertEpsilonEquals(2.99413, this.shape.operator_upTo(createPoint(-5, 25)));
	}

	@Test
	public void setDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		Vector2D newU = createVector(-456.789, 159.753).toUnitVector();
		this.shape.set(-6, -4, newU.getX(), newU.getY(), 147.369, Math.random(), Math.random(), 159.753);
		assertEpsilonEquals(-6, this.shape.getCenterX());
		assertEpsilonEquals(-4, this.shape.getCenterY());
		assertEpsilonEquals(newU.getX(), this.shape.getFirstAxisX());
		assertEpsilonEquals(newU.getY(), this.shape.getFirstAxisY());
		assertEpsilonEquals(147.369, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(-newU.getY(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newU.getX(), this.shape.getSecondAxisY());
		assertEpsilonEquals(159.753, this.shape.getSecondAxisExtent());
	}
	
	@Test
	public void isCCW() {
		assertTrue(this.shape.isCCW());
		assertTrue(createOrientedRectangle(cx, cy, ux, uy, e1, e2).isCCW());
		assertTrue(createOrientedRectangle(
				4.7, 15,
				0.12403, 0.99228, 18.02776, 20).isCCW());
		assertTrue(createOrientedRectangle(
				-10, -3,
				-.8944271909999159, .4472135954999579, 2, 1).isCCW());
		assertTrue(createOrientedRectangle(
				-10, 7,
				-0.9863939238321437, 0.1643989873053573, 1, 2).isCCW());
		assertTrue(createOrientedRectangle(
				0, -6,
				-0.9863939238321437, 0.1643989873053573, 1, 2).isCCW());
	}

    @Test
    public void getClosestPointToCircle2afp() {
        assertFpPointEquals(-3.01377, -1.02754, this.shape.getClosestPointTo(createCircle(-5, -5, 1)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createCircle(-30, 8, 1)));
        assertClosestPointInBothShapes(this.shape, createCircle(20, 5, 1));
        assertClosestPointInBothShapes(this.shape, createCircle(12, 10, 1));
    }

    @Test
    public void getDistanceSquaredCircle2afp() {
        assertEpsilonEquals(11.84282, this.shape.getDistanceSquared(createCircle(-5, -5, 1)));
        assertEpsilonEquals(295.70086, this.shape.getDistanceSquared(createCircle(-30, 8, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(20, 5, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(12, 10, 1)));
    }

    @Test
    public void getClosestPointToSegment2afp() {
        assertFpPointEquals(-1.81377, -1.62754, this.shape.getClosestPointTo(createSegment(-5, -5, -3, -4)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createSegment(-30, 8, -28, 9)));
        assertClosestPointInBothShapes(this.shape, createSegment(19, 5, 21, 6));
        assertClosestPointInBothShapes(this.shape, createSegment(12, 10, 14, 11));
    }

    @Test
    public void getDistanceSquaredSegment2afp() {
        assertEpsilonEquals(7.03568, this.shape.getDistanceSquared(createSegment(-5, -5, -3, -4)));
        assertEpsilonEquals(274.16887, this.shape.getDistanceSquared(createSegment(-30, 8, -28, 9)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(19, 5, 21, 6)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(12, 10, 14, 11)));
    }

    protected Triangle2afp createTestTriangle(double dx, double dy) {
        return createTriangle(dx, dy, dx + 6, dy + 3, dx - 1, dy + 2.5);
    }

    @Test
    public void getClosestPointToTriangle2afp() {
        assertFpPointEquals(-0.21377, -2.42754, this.shape.getClosestPointTo(createTestTriangle(-7, -7)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createTestTriangle(-60, 0)));
        assertClosestPointInBothShapes(this.shape, createTestTriangle(19, 5));
        assertClosestPointInBothShapes(this.shape, createTestTriangle(5, 5));
    }

    @Test
    public void getDistanceSquaredTriangle2afp() {
        assertEpsilonEquals(3.09077, this.shape.getDistanceSquared(createTestTriangle(-7, -7)));
        assertEpsilonEquals(1736.31148, this.shape.getDistanceSquared(createTestTriangle(-60, 0)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(19, 5)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(5, 5)));
    }

    @Test
    public void getClosestPointToRectangle2afp() {
        assertFpPointEquals(-1.81377, -1.62754, this.shape.getClosestPointTo(createRectangle(-5, -5, 2, 1)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createRectangle(-30, 5, 2, 1)));
        assertClosestPointInBothShapes(this.shape, createRectangle(17, 2, 2, 1));
        assertClosestPointInBothShapes(this.shape, createRectangle(5, 5, 2, 1));
    }

    @Test
    public void getDistanceSquaredRectangle2afp() {
        assertEpsilonEquals(7.03568, this.shape.getDistanceSquared(createRectangle(-5, -5, 2, 1)));
        assertEpsilonEquals(247.2364, this.shape.getDistanceSquared(createRectangle(-30, 5, 2, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(17, 2, 2, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(5, 5, 2, 1)));
    }

    @Test
    public void getClosestPointToEllipse2afp() {
        assertFpPointEquals(-1.98951, -1.53968, this.shape.getClosestPointTo(createEllipse(-5, -5, 2, 1)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createEllipse(-30, 5, 2, 1)));
        assertClosestPointInBothShapes(this.shape, createEllipse(17, 2, 2, 1));
        assertClosestPointInBothShapes(this.shape, createEllipse(5, 5, 2, 1));
    }

    @Test
    public void getDistanceSquaredEllipse2afp() {
        assertEpsilonEquals(8.49406, this.shape.getDistanceSquared(createEllipse(-5, -5, 2, 1)));
        assertEpsilonEquals(248.79828, this.shape.getDistanceSquared(createEllipse(-30, 5, 2, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(17, 2, 2, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(5, 5, 2, 1)));
    }

    @Test
    public void getClosestPointToRoundRectangle2afp() {
        assertFpPointEquals(-1.84892, -1.60997, this.shape.getClosestPointTo(createRoundRectangle(-5, -5, 2, 1, .2, .1)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createRoundRectangle(-30, 5, 2, 1, .2, .1)));
        assertClosestPointInBothShapes(this.shape, createRoundRectangle(17, 2, 2, 1, .2, .1));
        assertClosestPointInBothShapes(this.shape, createRoundRectangle(5, 5, 2, 1, .2, .1));
    }

    @Test
    public void getDistanceSquaredRoundRectangle2afp() {
        assertEpsilonEquals(7.31638, this.shape.getDistanceSquared(createRoundRectangle(-5, -5, 2, 1, .2, .1)));
        assertEpsilonEquals(247.51287, this.shape.getDistanceSquared(createRoundRectangle(-30, 5, 2, 1, .2, .1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(17, 2, 2, 1, .2, .1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(5, 5, 2, 1, .2, .1)));
    }

    protected MultiShape2afp createTestMultiShape(double dx, double dy) {
        Circle2afp circle = createCircle(dx - 3, dy + 2, 2);
        Triangle2afp triangle = createTestTriangle(dx +1, dy - 1);
        MultiShape2afp multishape = createMultiShape();
        multishape.add(circle);
        multishape.add(triangle);
        return multishape;
    }

    @Test
    public void getClosestPointToMultiShape2afp() {
        assertFpPointEquals(0.98623, -3.02754, this.shape.getClosestPointTo(createTestMultiShape(-7, -7)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createTestMultiShape(-30, 0)));
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(15, 2));
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(5, 5));
    }

    @Test
    public void getDistanceSquaredMultiShape2afp() {
        assertEpsilonEquals(4.86323, this.shape.getDistanceSquared(createTestMultiShape(-7, -7)));
        assertEpsilonEquals(116.39449, this.shape.getDistanceSquared(createTestMultiShape(-30, 0)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(15, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(5, 5)));
    }

    protected Path2afp<?, ?, ?, ?, ?, ?> createNonEmptyPath(double x, double y) {
        Path2afp<?, ?, ?, ?, ?, ?> path = createPath();
        path.moveTo(x, y);
        path.lineTo(x + 1, y + .5);
        path.lineTo(x, y + 1);
        return path;
    }

    @Test
    public void getClosestPointToPath2afp() {
        assertFpPointEquals(-2.41377, -1.32754, this.shape.getClosestPointTo(createNonEmptyPath(-5, -5)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createNonEmptyPath(-30, 5)));
        assertClosestPointInBothShapes(this.shape, createNonEmptyPath(15, 2));
        assertClosestPointInBothShapes(this.shape, createNonEmptyPath(5, 5));
    }

    @Test
    public void getDistanceSquaredPath2afp() {
        assertEpsilonEquals(12.58059, this.shape.getDistanceSquared(createNonEmptyPath(-5, -5)));
        assertEpsilonEquals(281.18147, this.shape.getDistanceSquared(createNonEmptyPath(-30, 5)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createNonEmptyPath(15, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createNonEmptyPath(5, 5)));
    }

    protected Parallelogram2afp createTestParallelogram(double dx, double dy) {
        Vector2D r = createVector(4, 1).toUnitVector();
        Vector2D s = createVector(-1, -1).toUnitVector();
        return createParallelogram(dx, dy, r.getX(), r.getY(), 2, s.getX(), s.getY(), 1);
    }

    @Test
    public void getClosestPointToParallelogram2afp() {
        assertFpPointEquals(-1.37273, -1.84807, this.shape.getClosestPointTo(createTestParallelogram(-5, -5)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createTestParallelogram(-30, 5)));
        assertClosestPointInBothShapes(this.shape, createTestParallelogram(18, 2));
        assertClosestPointInBothShapes(this.shape, createTestParallelogram(5, 5));
    }

    @Test
    public void getDistanceSquaredParallelogram2afp() {
        assertEpsilonEquals(4.80081, this.shape.getDistanceSquared(createTestParallelogram(-5, -5)));
        assertEpsilonEquals(232.05334, this.shape.getDistanceSquared(createTestParallelogram(-30, 5)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestParallelogram(18, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestParallelogram(5, 5)));
    }
    
    protected OrientedRectangle2afp createTestOrientedRectangle(double dx, double dy) {
        Vector2D r = createVector(4, 1).toUnitVector();
        return createOrientedRectangle(dx, dy, r.getX(), r.getY(), 2, 1);
    }

    @Test
    public void getClosestPointToOrientedRectangle2afp() {
        assertFpPointEquals(-2.23766, -1.4156, this.shape.getClosestPointTo(createTestOrientedRectangle(-5, -5)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createTestOrientedRectangle(-30, 5)));
        assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(18, 2));
        assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(5, 5));
    }

    @Test
    public void getDistanceSquaredOrientedRectangle2afp() {
        assertEpsilonEquals(5.66678, this.shape.getDistanceSquared(createTestOrientedRectangle(-5, -5)));
        assertEpsilonEquals(240.45186, this.shape.getDistanceSquared(createTestOrientedRectangle(-30, 5)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(18, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(5, 5)));
    }
	
}