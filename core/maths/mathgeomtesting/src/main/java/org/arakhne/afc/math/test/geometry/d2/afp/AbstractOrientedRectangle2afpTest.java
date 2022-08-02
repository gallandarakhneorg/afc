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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;

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

	@Override
	protected final T createShape() {
		return (T) createOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsVectorProjectionRAxisVector(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-this.e1, OrientedRectangle2afp.findsVectorProjectionRAxisVector(this.ux, this.uy, this.pEx - this.cx, this.pEy - this.cy));
		assertEpsilonEquals(this.e1, OrientedRectangle2afp.findsVectorProjectionRAxisVector(this.ux, this.uy, this.pFx - this.cx, this.pFy - this.cy));
		assertEpsilonEquals(this.e1, OrientedRectangle2afp.findsVectorProjectionRAxisVector(this.ux, this.uy, this.pGx - this.cx, this.pGy - this.cy));
		assertEpsilonEquals(-this.e1, OrientedRectangle2afp.findsVectorProjectionRAxisVector(this.ux, this.uy, this.pHx - this.cx, this.pHy - this.cy));
		assertEpsilonEquals(-1.34164, OrientedRectangle2afp.findsVectorProjectionRAxisVector(this.ux, this.uy, -this.cx, -this.cy));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsVectorProjectionSAxisVector(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-this.e2, OrientedRectangle2afp.findsVectorProjectionSAxisVector(this.ux, this.uy, this.pEx - this.cx, this.pEy - this.cy));
		assertEpsilonEquals(-this.e2, OrientedRectangle2afp.findsVectorProjectionSAxisVector(this.ux, this.uy, this.pFx - this.cx, this.pFy - this.cy));
		assertEpsilonEquals(this.e2, OrientedRectangle2afp.findsVectorProjectionSAxisVector(this.ux, this.uy, this.pGx - this.cx, this.pGy - this.cy));
		assertEpsilonEquals(this.e2, OrientedRectangle2afp.findsVectorProjectionSAxisVector(this.ux, this.uy, this.pHx - this.cx, this.pHy - this.cy));
		assertEpsilonEquals(-10.73313, OrientedRectangle2afp.findsVectorProjectionSAxisVector(this.ux, this.uy, -this.cx, -this.cy));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCenterPointAxisExtents(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		List points = Arrays.asList(
				createPoint(this.pEx, this.pEy), createPoint(this.pGx, this.pGy),
				createPoint(this.pFx, this.pFy), createPoint(this.pEx, this.pEy));
		Vector2D R;
		Point2D center;
		Tuple2D extents;
		R = createVector(this.ux, this.uy);
		center = createPoint(Double.NaN, Double.NaN);
		extents = createVector(Double.NaN, Double.NaN);
		OrientedRectangle2afp.calculatesCenterPointAxisExtents(points, R, center, extents);
		assertEpsilonEquals(this.cx, center.getX());
		assertEpsilonEquals(this.cy, center.getY());
		assertEpsilonEquals(this.e1, extents.getX());
		assertEpsilonEquals(this.e2, extents.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticContainsOrientedRectanglePoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, 0));
		assertFalse(OrientedRectangle2afp.containsOrientedRectanglePoint(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-20, 0));
		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				12, -4));
		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				14, 0));
		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				17, 0));
		assertFalse(OrientedRectangle2afp.containsOrientedRectanglePoint(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				18, 0));
		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				21, 8));
		assertFalse(OrientedRectangle2afp.containsOrientedRectanglePoint(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				22, 8));
		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				8, 16));
		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-4, 20));
		assertFalse(OrientedRectangle2afp.containsOrientedRectanglePoint(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-4, 21));

		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(this.cx, this.cy, this.ux, this.uy, this.e1,this.e2,
				this.cx, this.cy));

		assertTrue(OrientedRectangle2afp.containsOrientedRectanglePoint(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				this.pEx, this.pEy));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticContainsOrientedRectangleRectangle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(OrientedRectangle2afp.containsOrientedRectangleRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, 0, 2, 1));
		assertTrue(OrientedRectangle2afp.containsOrientedRectangleRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -1, 2, 1));
		assertTrue(OrientedRectangle2afp.containsOrientedRectangleRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -2, 2, 1));
		assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -3, 2, 1));
		assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -4, 2, 1));
		assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -5, 2, 1));
		assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -5, 2, 1));
		assertFalse(OrientedRectangle2afp.containsOrientedRectangleRectangle(this.cx, this.cy, this.ux, this.uy, this.e1,this.e2,
				5, 25, 2, 1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestFarthestPointsPointOrientedRectangle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D closest, farthest;

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				-20, 9,
				this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				closest, farthest);
		assertEpsilonEquals(-11.72197, closest.getX());
		assertEpsilonEquals(4.86099, closest.getY());
		assertEpsilonEquals(this.pGx, farthest.getX());
		assertEpsilonEquals(this.pGy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				0, 0,
				this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				closest, farthest);
		assertEpsilonEquals(0, closest.getX());
		assertEpsilonEquals(0, closest.getY());
		assertEpsilonEquals(this.pGx, farthest.getX());
		assertEpsilonEquals(this.pGy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				5, -10,
				this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				closest, farthest);
		assertEpsilonEquals(6.98623, closest.getX());
		assertEpsilonEquals(-6.02754, closest.getY());
		assertEpsilonEquals(this.pHx, farthest.getX());
		assertEpsilonEquals(this.pHy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				14, -20,
				this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				closest, farthest);
		assertEpsilonEquals(this.pFx, closest.getX());
		assertEpsilonEquals(this.pFy, closest.getY());
		assertEpsilonEquals(this.pHx, farthest.getX());
		assertEpsilonEquals(this.pHy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				-6, 15,
				this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				closest, farthest);
		assertEpsilonEquals(-6, closest.getX());
		assertEpsilonEquals(15, closest.getY());
		assertEpsilonEquals(this.pFx, farthest.getX());
		assertEpsilonEquals(this.pFy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				0, 35,
				this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				closest, farthest);
		assertEpsilonEquals(this.pHx, closest.getX());
		assertEpsilonEquals(this.pHy, closest.getY());
		assertEpsilonEquals(this.pFx, farthest.getX());
		assertEpsilonEquals(this.pFy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				10, 0,
				this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				closest, farthest);
		assertEpsilonEquals(10, closest.getX());
		assertEpsilonEquals(0, closest.getY());
		assertEpsilonEquals(this.pHx, farthest.getX());
		assertEpsilonEquals(this.pHy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				16, -4,
				this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				closest, farthest);
		assertEpsilonEquals(15.32197, closest.getX());
		assertEpsilonEquals(-3.66099, closest.getY());
		assertEpsilonEquals(this.pHx, farthest.getX());
		assertEpsilonEquals(this.pHy, farthest.getY());

		closest = createPoint(Double.NaN, Double.NaN);
		farthest = createPoint(Double.NaN, Double.NaN);
		OrientedRectangle2afp.findsClosestFarthestPointsPointOrientedRectangle(
				-5, 25,
				this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				closest, farthest);
		assertEpsilonEquals(-2.32197, closest.getX());
		assertEpsilonEquals(23.66099, closest.getY());
		assertEpsilonEquals(this.pFx, farthest.getX());
		assertEpsilonEquals(this.pFy, farthest.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsOrientedRectangleSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-5, -5, 0, -7));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-20, 0, -25, 2));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-10, 15, -11, 17));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-1, 30, -2, 40));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				10, 30, 15, 40));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				30, 15, 40, 16));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				20, 0, 25, 2));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				12, -15, 12, -16));

		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				15, -15, 35, 25));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				35, 25, -10, 40));

		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-5, -5, 5, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-10, 15, 0, 10));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, 20, 15, 25));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				15, 5, 30, 10));

		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-5, -5, -10, 15));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-10, 15, 15, 25));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				15, 25, 20, 0));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				20, 0, 0, -10));
		
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				15, 25, 0, -10));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-10, 15, 20, 0));

		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleSegment(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, 5, 10, 16));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsOrientedRectangleCircle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleCircle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -3.2, .5));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleCircle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -3.1, .5));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -3, .5));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				6, 2, .5));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				this.pEx, this.pEy, .5));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				this.pFx, this.pFy, .5));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-9, 10, .5));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleCircle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				2, 10, 50));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsOrientedRectangleRectangle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -5, 2, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -4.5, 2, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -4, 2, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				4, 4, 2, 1));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				20, -2, 2, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-15, -10, 50, 50));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsOrientedRectangleEllipse(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -5, 2, 1));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -4.5, 2, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, -4, 2, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				4, 4, 2, 1));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				20, -2, 2, 1));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleEllipse(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-15, -10, 50, 50));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsOrientedRectangleTriangle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-10, 15, -8, 16, -13, 19));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-5, 30, -3, 31, -8, 34));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				15, 25, 17, 26, 12, 29));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				40, 15, 42, 16, 37, 19));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				35, 0, 37, 1, 32, 4));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				15, -20, 17, -19, 12, -16));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-5, -10, -3, -9, -8, -6));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-25, -5, -23, -4, -28, -1));

		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-4, -2, -2, -1, -7, -2));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-2, 4, 0, 5, -5, 8));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				20, 5, 22, 6, 17, 9));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				20, 5, 22, 6, -10, 15));

		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleTriangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				50, 30, 0, -50, -30, 31));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsOrientedRectangleOrientedRectangle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double ux2 = -0.9284766908852592;
		double uy2 = 0.3713906763541037;
		double et1 = 5;
		double et2 = 3;
		// D + (-0.9284766908852592,0.3713906763541037) * 5 + (0.3713906763541037,0.9284766908852592) * 3
		// D - (-0.9284766908852592,0.3713906763541037) * 5 + (0.3713906763541037,0.9284766908852592) * 3
		// D - (-0.9284766908852592,0.3713906763541037) * 5 - (0.3713906763541037,0.9284766908852592) * 3
		// D + (-0.9284766908852592,0.3713906763541037) * 5 - (0.3713906763541037,0.9284766908852592) * 3
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1,this.e2,
				-10, -2, ux2, uy2, et1, et2));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-15, 25, ux2, uy2, et1, et2));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				2, -8, ux2, uy2, et1, et2));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				2, -7, ux2, uy2, et1, et2));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				2, -6, ux2, uy2, et1, et2));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				this.pEx, this.pEy, ux2, uy2, et1, et2));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				6, 6, ux2, uy2, et1, et2));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				6, 6, ux2, uy2, 10 * et1, 10 * et2));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsOrientedRectangleRoundRectangle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				0, 0, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-9, 15, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-8.7, 15, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-8.7, 15, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-8.65, 15, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-8.64, 15, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-8.63, 15, 2, 1, .1, .05));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-8.62, 15, 2, 1, .1, .05));
		assertTrue(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				-8, 15, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				10, 25, 2, 1, .1, .05));
		assertFalse(OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2,
				20, -5, 2, 1, .1, .05));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
		assertTrue(this.shape.contains(this.cx, this.cy));
		assertTrue(this.shape.contains(this.pEx, this.pEy));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
		assertTrue(this.shape.contains(createPoint(this.cx, this.cy)));
		assertTrue(this.shape.contains(createPoint(this.pEx, this.pEy)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void testClone(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T clone = this.shape.clone();
		assertNotNull(clone);
		assertNotSame(this.shape, clone);
		assertEquals(this.shape.getClass(), clone.getClass());
		assertEpsilonEquals(this.cx, clone.getCenterX());
		assertEpsilonEquals(this.cy, clone.getCenterY());
		assertEpsilonEquals(this.ux, clone.getFirstAxisX());
		assertEpsilonEquals(this.uy, clone.getFirstAxisY());
		assertEpsilonEquals(this.e1, clone.getFirstAxisExtent());
		assertEpsilonEquals(this.vx, clone.getSecondAxisX());
		assertEpsilonEquals(this.vy, clone.getSecondAxisY());
		assertEpsilonEquals(this.e2, clone.getSecondAxisExtent());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void equalsObject(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createOrientedRectangle(0, this.cy, this.ux, this.uy, this.e1, this.e2)));
		assertFalse(this.shape.equals(createOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, 20)));
		assertFalse(this.shape.equals(createSegment(5, 8, 6, 10)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void equalsObject_withPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals((PathIterator2afp) null));
		assertFalse(this.shape.equals(createOrientedRectangle(0, this.cy, this.ux, this.uy, this.e1, this.e2).getPathIterator()));
		assertFalse(this.shape.equals(createOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, 20).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 6, 10).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2).getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void equalsToPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator(null));
		assertFalse(this.shape.equalsToPathIterator(createOrientedRectangle(0, this.cy, this.ux, this.uy, this.e1, this.e2).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, 20).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 6, 10).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2).getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void equalsToShape(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createOrientedRectangle(0, this.cy, this.ux, this.uy, this.e1, this.e2)));
		assertFalse(this.shape.equalsToShape((T) createOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, 20)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2)));
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D closest;

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
		assertEpsilonEquals(this.pFx, closest.getX());
		assertEpsilonEquals(this.pFy, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(-6, 15));
		assertEpsilonEquals(-6, closest.getX());
		assertEpsilonEquals(15, closest.getY());

		closest = this.shape.getClosestPointTo(createPoint(0, 35));
		assertEpsilonEquals(this.pHx, closest.getX());
		assertEpsilonEquals(this.pHy, closest.getY());

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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFarthestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D farthest;

		farthest = this.shape.getFarthestPointTo(createPoint(-20, 9));
		assertEpsilonEquals(this.pGx, farthest.getX());
		assertEpsilonEquals(this.pGy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(0, 0));
		assertEpsilonEquals(this.pGx, farthest.getX());
		assertEpsilonEquals(this.pGy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(5, -10));
		assertEpsilonEquals(this.pHx, farthest.getX());
		assertEpsilonEquals(this.pHy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(14, -20));
		assertEpsilonEquals(this.pHx, farthest.getX());
		assertEpsilonEquals(this.pHy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(-6, 15));
		assertEpsilonEquals(this.pFx, farthest.getX());
		assertEpsilonEquals(this.pFy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(0, 35));
		assertEpsilonEquals(this.pFx, farthest.getX());
		assertEpsilonEquals(this.pFy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(10, 0));
		assertEpsilonEquals(this.pHx, farthest.getX());
		assertEpsilonEquals(this.pHy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(16, -4));
		assertEpsilonEquals(this.pHx, farthest.getX());
		assertEpsilonEquals(this.pHy, farthest.getY());

		farthest = this.shape.getFarthestPointTo(createPoint(-5, 25));
		assertEpsilonEquals(this.pFx, farthest.getX());
		assertEpsilonEquals(this.pFy, farthest.getY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void translateDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.translate(123.456, 789.123);
		assertEpsilonEquals(this.cx + 123.456, this.shape.getCenterX());
		assertEpsilonEquals(this.cy + 789.123, this.shape.getCenterY());
		assertEpsilonEquals(this.ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(this.uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(this.vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(this.vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void translateVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(123.456, 789.123));
		assertEpsilonEquals(this.cx + 123.456, this.shape.getCenterX());
		assertEpsilonEquals(this.cy + 789.123, this.shape.getCenterY());
		assertEpsilonEquals(this.ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(this.uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(this.vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(this.vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistance(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquared(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceL1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceLinf(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setIT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, this.pGx, this.pGy);
		assertElement(pi, PathElementType.LINE_TO, this.pHx, this.pHy);
		assertElement(pi, PathElementType.LINE_TO, this.pEx, this.pEy);
		assertElement(pi, PathElementType.LINE_TO, this.pFx, this.pFy);
		assertElement(pi, PathElementType.CLOSE, this.pGx, this.pGy);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIteratorTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, this.pGx, this.pGy);
		assertElement(pi, PathElementType.LINE_TO, this.pHx, this.pHy);
		assertElement(pi, PathElementType.LINE_TO, this.pEx, this.pEy);
		assertElement(pi, PathElementType.LINE_TO, this.pFx, this.pFy);
		assertElement(pi, PathElementType.CLOSE, this.pGx, this.pGy);
		assertNoElement(pi);

		Transform2D transform;
		
		transform = new Transform2D();
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, this.pGx, this.pGy);
		assertElement(pi, PathElementType.LINE_TO, this.pHx, this.pHy);
		assertElement(pi, PathElementType.LINE_TO, this.pEx, this.pEy);
		assertElement(pi, PathElementType.LINE_TO, this.pFx, this.pFy);
		assertElement(pi, PathElementType.CLOSE, this.pGx, this.pGy);
		assertNoElement(pi);

		transform = new Transform2D();
		transform.setTranslation(18,  -45);
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, this.pGx + 18, this.pGy - 45);
		assertElement(pi, PathElementType.LINE_TO, this.pHx + 18, this.pHy - 45);
		assertElement(pi, PathElementType.LINE_TO, this.pEx + 18, this.pEy - 45);
		assertElement(pi, PathElementType.LINE_TO, this.pFx + 18, this.pFy - 45);
		assertElement(pi, PathElementType.CLOSE, this.pGx + 18, this.pGy - 45);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void createTransformedShape(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi = this.shape.createTransformedShape(null).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, this.pGx, this.pGy);
		assertElement(pi, PathElementType.LINE_TO, this.pHx, this.pHy);
		assertElement(pi, PathElementType.LINE_TO, this.pEx, this.pEy);
		assertElement(pi, PathElementType.LINE_TO, this.pFx, this.pFy);
		assertElement(pi, PathElementType.CLOSE, this.pGx, this.pGy);
		assertNoElement(pi);

		Transform2D transform;
		
		transform = new Transform2D();
		pi = this.shape.createTransformedShape(transform).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, this.pGx, this.pGy);
		assertElement(pi, PathElementType.LINE_TO, this.pHx, this.pHy);
		assertElement(pi, PathElementType.LINE_TO, this.pEx, this.pEy);
		assertElement(pi, PathElementType.LINE_TO, this.pFx, this.pFy);
		assertElement(pi, PathElementType.CLOSE, this.pGx, this.pGy);
		assertNoElement(pi);

		transform = new Transform2D();
		transform.setTranslation(18,  -45);
		pi = this.shape.createTransformedShape(transform).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, this.pGx + 18, this.pGy - 45);
		assertElement(pi, PathElementType.LINE_TO, this.pHx + 18, this.pHy - 45);
		assertElement(pi, PathElementType.LINE_TO, this.pEx + 18, this.pEy - 45);
		assertElement(pi, PathElementType.LINE_TO, this.pFx + 18, this.pFy - 45);
		assertElement(pi, PathElementType.CLOSE, this.pGx + 18, this.pGy - 45);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void toBoundingBox(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertEpsilonEquals(this.pEx, box.getMinX());
		assertEpsilonEquals(this.pFy, box.getMinY());
		assertEpsilonEquals(this.pGx, box.getMaxX());
		assertEpsilonEquals(this.pHy, box.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void toBoundingBoxB(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(this.pEx, box.getMinX());
		assertEpsilonEquals(this.pFy, box.getMinY());
		assertEpsilonEquals(this.pGx, box.getMaxX());
		assertEpsilonEquals(this.pHy, box.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void rotateDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.rotate(-MathConstants.DEMI_PI);
		assertEpsilonEquals(6, this.shape.getCenterX());
		assertEpsilonEquals(9, this.shape.getCenterY());
		assertEpsilonEquals(-4.472135954999580e-01, this.shape.getFirstAxisX());
		assertEpsilonEquals(-8.944271909999160e-01, this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(8.944271909999160e-01, this.shape.getSecondAxisX());
		assertEpsilonEquals(-4.472135954999580e-01, this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCenter(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D c = this.shape.getCenter();
		assertEpsilonEquals(6, c.getX());
		assertEpsilonEquals(9, c.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCenterX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(6, this.shape.getCenterX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCenterY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(9, this.shape.getCenterY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setCenterDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setCenter(123.456, -789.123);
		assertEpsilonEquals(123.456, this.shape.getCenterX());
		assertEpsilonEquals(-789.123, this.shape.getCenterY());
		assertEpsilonEquals(this.ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(this.uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(this.vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(this.vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setCenterPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setCenter(createPoint(123.456, -789.123));
		assertEpsilonEquals(123.456, this.shape.getCenterX());
		assertEpsilonEquals(-789.123, this.shape.getCenterY());
		assertEpsilonEquals(this.ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(this.uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(this.vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(this.vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setCenterX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setCenterX(123.456);
		assertEpsilonEquals(123.456, this.shape.getCenterX());
		assertEpsilonEquals(this.cy, this.shape.getCenterY());
		assertEpsilonEquals(this.ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(this.uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(this.vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(this.vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setCenterY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setCenterY(123.456);
		assertEpsilonEquals(this.cx, this.shape.getCenterX());
		assertEpsilonEquals(123.456, this.shape.getCenterY());
		assertEpsilonEquals(this.ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(this.uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(this.vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(this.vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFirstAxis(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D v = this.shape.getFirstAxis();
		assertEpsilonEquals(this.ux, v.getX());
		assertEpsilonEquals(this.uy, v.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFirstAxisX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(this.ux, this.shape.getFirstAxisX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFirstAxisY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(this.uy, this.shape.getFirstAxisY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getSecondAxis(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D v = this.shape.getSecondAxis();
		assertEpsilonEquals(this.vx, v.getX());
		assertEpsilonEquals(this.vy, v.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getSecondAxisX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(this.vx, this.shape.getSecondAxisX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getSecondAxisY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(this.vy, this.shape.getSecondAxisY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFirstAxisExtent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setFirstAxisExtent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setFirstAxisExtent(123.456);
		assertEpsilonEquals(this.cx, this.shape.getCenterX());
		assertEpsilonEquals(this.cy, this.shape.getCenterY());
		assertEpsilonEquals(this.ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(this.uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(123.456, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(this.vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(this.vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getSecondAxisExtent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setSecondAxisExtent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setSecondAxisExtent(123.456);
		assertEpsilonEquals(this.cx, this.shape.getCenterX());
		assertEpsilonEquals(this.cy, this.shape.getCenterY());
		assertEpsilonEquals(this.ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(this.uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(this.vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(this.vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(123.456, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setFirstAxisDoubleDouble_unitVector(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D newU = createVector(123.456, 456.789).toUnitVector();
		this.shape.setFirstAxis(newU.getX(), newU.getY());
		assertEpsilonEquals(this.cx, this.shape.getCenterX());
		assertEpsilonEquals(this.cy, this.shape.getCenterY());
		assertEpsilonEquals(newU.getX(), this.shape.getFirstAxisX());
		assertEpsilonEquals(newU.getY(), this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(-newU.getY(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newU.getX(), this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setFirstAxisDoubleDouble_notUnitVector(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(AssertionError.class, () -> this.shape.setFirstAxis(123.456, 456.789));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setFirstAxisVector2D_unitVector(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D newU = createVector(123.456, 456.789).toUnitVector();
		this.shape.setFirstAxis(newU);
		assertEpsilonEquals(this.cx, this.shape.getCenterX());
		assertEpsilonEquals(this.cy, this.shape.getCenterY());
		assertEpsilonEquals(newU.getX(), this.shape.getFirstAxisX());
		assertEpsilonEquals(newU.getY(), this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(-newU.getY(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newU.getX(), this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setFirstAxisVector2D_notUnitVector(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(AssertionError.class, () -> this.shape.setFirstAxis(createVector(123.456, 456.789)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setFirstAxisVector2DDouble_unitVector(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D newU = createVector(123.456, 456.789).toUnitVector();
		this.shape.setFirstAxis(newU, 159.753);
		assertEpsilonEquals(this.cx, this.shape.getCenterX());
		assertEpsilonEquals(this.cy, this.shape.getCenterY());
		assertEpsilonEquals(newU.getX(), this.shape.getFirstAxisX());
		assertEpsilonEquals(newU.getY(), this.shape.getFirstAxisY());
		assertEpsilonEquals(159.753, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(-newU.getY(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newU.getX(), this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setFirstAxisDoubleDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D newU = createVector(123.456, 456.789).toUnitVector();
		this.shape.setFirstAxis(newU.getX(), newU.getY(), 159.753);
		assertEpsilonEquals(this.cx, this.shape.getCenterX());
		assertEpsilonEquals(this.cy, this.shape.getCenterY());
		assertEpsilonEquals(newU.getX(), this.shape.getFirstAxisX());
		assertEpsilonEquals(newU.getY(), this.shape.getFirstAxisY());
		assertEpsilonEquals(159.753, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(-newU.getY(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newU.getX(), this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setSecondAxisDoubleDouble_unitVector(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D newV = createVector(123.456, 456.789).toUnitVector();
		this.shape.setSecondAxis(newV.getX(), newV.getY());
		assertEpsilonEquals(this.cx, this.shape.getCenterX());
		assertEpsilonEquals(this.cy, this.shape.getCenterY());
		assertEpsilonEquals(newV.getY(), this.shape.getFirstAxisX());
		assertEpsilonEquals(-newV.getX(), this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(newV.getX(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newV.getY(), this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setSecondAxisDoubleDouble_notUnitVector(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(AssertionError.class, () -> this.shape.setSecondAxis(123.456, 456.789));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setSecondAxisVector2D_unitVector(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D newV = createVector(123.456, 456.789).toUnitVector();
		this.shape.setSecondAxis(newV);
		assertEpsilonEquals(this.cx, this.shape.getCenterX());
		assertEpsilonEquals(this.cy, this.shape.getCenterY());
		assertEpsilonEquals(newV.getY(), this.shape.getFirstAxisX());
		assertEpsilonEquals(-newV.getX(), this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(newV.getX(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newV.getY(), this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setSecondAxisVector2D_notUnitVector(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(AssertionError.class, () -> this.shape.setSecondAxis(createVector(123.456, 456.789)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setSecondAxisVector2DDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D newV = createVector(123.456, 456.789).toUnitVector();
		this.shape.setSecondAxis(newV, 159.753);
		assertEpsilonEquals(this.cx, this.shape.getCenterX());
		assertEpsilonEquals(this.cy, this.shape.getCenterY());
		assertEpsilonEquals(newV.getY(), this.shape.getFirstAxisX());
		assertEpsilonEquals(-newV.getX(), this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(newV.getX(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newV.getY(), this.shape.getSecondAxisY());
		assertEpsilonEquals(159.753, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setSecondAxisDoubleDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D newV = createVector(123.456, 456.789).toUnitVector();
		this.shape.setSecondAxis(newV.getX(), newV.getY(), 159.753);
		assertEpsilonEquals(this.cx, this.shape.getCenterX());
		assertEpsilonEquals(this.cy, this.shape.getCenterY());
		assertEpsilonEquals(newV.getY(), this.shape.getFirstAxisX());
		assertEpsilonEquals(-newV.getX(), this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(newV.getX(), this.shape.getSecondAxisX());
		assertEpsilonEquals(newV.getY(), this.shape.getSecondAxisY());
		assertEpsilonEquals(159.753, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setPoint2DVector2DDoubleVector2DDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setFromPointCloudIterable(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double obrux = 0.8944271909999159;
		double obruy = -0.4472135954999579;
		double obrvx = 0.4472135954999579;
		double obrvy = 0.8944271909999159;

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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setFromPointCloudPoint2DArray(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double obrux = 0.8944271909999159;
		double obruy = -0.4472135954999579;
		double obrvx = 0.4472135954999579;
		double obrvy = 0.8944271909999159;

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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void orthogonalAxes_changeFirstAxis(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Vector2D.isOrthogonal(this.ux, this.uy, this.vx, this.vy));
		this.shape.setFirstAxis(0.500348, 0.865824);
		assertEpsilonEquals(-0.865824, this.shape.getSecondAxisX());
		assertEpsilonEquals(0.500348, this.shape.getSecondAxisY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void orthogonalAxes_changeSecondAxis(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Vector2D.isOrthogonal(this.ux, this.uy, this.vx, this.vy));
		this.shape.setSecondAxis(0.500348, 0.865824);
		assertEpsilonEquals(0.865824, this.shape.getFirstAxisX());
		assertEpsilonEquals(-0.500348, this.shape.getFirstAxisY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createCircle(0, -3.2, .5)));
		assertFalse(this.shape.intersects(createCircle(0, -3.1, .5)));
		assertTrue(this.shape.intersects(createCircle(0, -3, .5)));
		assertTrue(this.shape.intersects(createCircle(6, 2, .5)));
		assertTrue(this.shape.intersects(createCircle(this.pEx, this.pEy, .5)));
		assertTrue(this.shape.intersects(createCircle(this.pFx, this.pFy, .5)));
		assertTrue(this.shape.intersects(createCircle(-9, 10, .5)));
		assertTrue(this.shape.intersects(createCircle(2, 10, 50)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createRectangle(0, -5, 2, 1)));
		assertTrue(this.shape.intersects(createRectangle(0, -4.5, 2, 1)));
		assertTrue(this.shape.intersects(createRectangle(0, -4, 2, 1)));
		assertTrue(this.shape.intersects(createRectangle(4, 4, 2, 1)));
		assertFalse(this.shape.intersects(createRectangle(20, -2, 2, 1)));
		assertTrue(this.shape.intersects(createRectangle(-15, -10, 50, 50)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createEllipse(0, -5, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(0, -4.5, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(0, -4, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(4, 4, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(20, -2, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(-15, -10, 50, 50)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPath2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPathIterator2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsParallelogram2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsOrientedRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double ux2 = -0.9284766908852592;
		double uy2 = 0.3713906763541037;
		double et1 = 5;
		double et2 = 3;
		assertFalse(this.shape.intersects(createOrientedRectangle(-10, -2, ux2, uy2, et1, et2)));
		assertFalse(this.shape.intersects(createOrientedRectangle(-15, 25, ux2, uy2, et1, et2)));
		assertFalse(this.shape.intersects(createOrientedRectangle(2, -8, ux2, uy2, et1, et2)));
		assertTrue(this.shape.intersects(createOrientedRectangle(2, -7, ux2, uy2, et1, et2)));
		assertTrue(this.shape.intersects(createOrientedRectangle(2, -6, ux2, uy2, et1, et2)));
		assertTrue(this.shape.intersects(createOrientedRectangle(this.pEx, this.pEy, ux2, uy2, et1, et2)));
		assertTrue(this.shape.intersects(createOrientedRectangle(6, 6, ux2, uy2, et1, et2)));
		assertTrue(this.shape.intersects(createOrientedRectangle(6, 6, ux2, uy2, 10 * et1, 10 * et2)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape2D) createCircle(6, 2, .5)));
		assertTrue(this.shape.intersects((Shape2D) createRectangle(4, 4, 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_addVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(123.456, 789.123));
		assertEpsilonEquals(this.cx + 123.456, this.shape.getCenterX());
		assertEpsilonEquals(this.cy + 789.123, this.shape.getCenterY());
		assertEpsilonEquals(this.ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(this.uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(this.vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(this.vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_plusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_plus(createVector(123.456, 789.123));
		assertEpsilonEquals(this.cx + 123.456, shape.getCenterX());
		assertEpsilonEquals(this.cy + 789.123, shape.getCenterY());
		assertEpsilonEquals(this.ux, shape.getFirstAxisX());
		assertEpsilonEquals(this.uy, shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, shape.getFirstAxisExtent());
		assertEpsilonEquals(this.vx, shape.getSecondAxisX());
		assertEpsilonEquals(this.vy, shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, shape.getSecondAxisExtent());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_removeVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(123.456, 789.123));
		assertEpsilonEquals(this.cx - 123.456, this.shape.getCenterX());
		assertEpsilonEquals(this.cy - 789.123, this.shape.getCenterY());
		assertEpsilonEquals(this.ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(this.uy, this.shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, this.shape.getFirstAxisExtent());
		assertEpsilonEquals(this.vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(this.vy, this.shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, this.shape.getSecondAxisExtent());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_minusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_minus(createVector(123.456, 789.123));
		assertEpsilonEquals(this.cx - 123.456, shape.getCenterX());
		assertEpsilonEquals(this.cy - 789.123, shape.getCenterY());
		assertEpsilonEquals(this.ux, shape.getFirstAxisX());
		assertEpsilonEquals(this.uy, shape.getFirstAxisY());
		assertEpsilonEquals(this.e1, shape.getFirstAxisExtent());
		assertEpsilonEquals(this.vx, shape.getSecondAxisX());
		assertEpsilonEquals(this.vy, shape.getSecondAxisY());
		assertEpsilonEquals(this.e2, shape.getSecondAxisExtent());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_multiplyTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi = this.shape.operator_multiply(null).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, this.pGx, this.pGy);
		assertElement(pi, PathElementType.LINE_TO, this.pHx, this.pHy);
		assertElement(pi, PathElementType.LINE_TO, this.pEx, this.pEy);
		assertElement(pi, PathElementType.LINE_TO, this.pFx, this.pFy);
		assertElement(pi, PathElementType.CLOSE, this.pGx, this.pGy);
		assertNoElement(pi);

		Transform2D transform;
		
		transform = new Transform2D();
		pi = this.shape.operator_multiply(transform).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, this.pGx, this.pGy);
		assertElement(pi, PathElementType.LINE_TO, this.pHx, this.pHy);
		assertElement(pi, PathElementType.LINE_TO, this.pEx, this.pEy);
		assertElement(pi, PathElementType.LINE_TO, this.pFx, this.pFy);
		assertElement(pi, PathElementType.CLOSE, this.pGx, this.pGy);
		assertNoElement(pi);

		transform = new Transform2D();
		transform.setTranslation(18,  -45);
		pi = this.shape.operator_multiply(transform).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, this.pGx + 18, this.pGy - 45);
		assertElement(pi, PathElementType.LINE_TO, this.pHx + 18, this.pHy - 45);
		assertElement(pi, PathElementType.LINE_TO, this.pEx + 18, this.pEy - 45);
		assertElement(pi, PathElementType.LINE_TO, this.pFx + 18, this.pFy - 45);
		assertElement(pi, PathElementType.CLOSE, this.pGx + 18, this.pGy - 45);
		assertNoElement(pi);

	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
		assertTrue(this.shape.operator_and(createPoint(this.cx, this.cy)));
		assertTrue(this.shape.operator_and(createPoint(this.pEx, this.pEy)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createCircle(6, 2, .5)));
		assertTrue(this.shape.operator_and(createRectangle(4, 4, 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_upToPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void isCCW(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.isCCW());
		assertTrue(createOrientedRectangle(this.cx, this.cy, this.ux, this.uy, this.e1, this.e2).isCCW());
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

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(-3.01377, -1.02754, this.shape.getClosestPointTo(createCircle(-5, -5, 1)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createCircle(-30, 8, 1)));
        assertClosestPointInBothShapes(this.shape, createCircle(20, 5, 1));
        assertClosestPointInBothShapes(this.shape, createCircle(12, 10, 1));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(11.84282, this.shape.getDistanceSquared(createCircle(-5, -5, 1)));
        assertEpsilonEquals(295.70086, this.shape.getDistanceSquared(createCircle(-30, 8, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(20, 5, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(12, 10, 1)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(-1.81377, -1.62754, this.shape.getClosestPointTo(createSegment(-5, -5, -3, -4)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createSegment(-30, 8, -28, 9)));
        assertClosestPointInBothShapes(this.shape, createSegment(19, 5, 21, 6));
        assertClosestPointInBothShapes(this.shape, createSegment(12, 10, 14, 11));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(7.03568, this.shape.getDistanceSquared(createSegment(-5, -5, -3, -4)));
        assertEpsilonEquals(274.16887, this.shape.getDistanceSquared(createSegment(-30, 8, -28, 9)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(19, 5, 21, 6)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(12, 10, 14, 11)));
    }

    protected Triangle2afp createTestTriangle(double dx, double dy) {
        return createTriangle(dx, dy, dx + 6, dy + 3, dx - 1, dy + 2.5);
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(-0.21377, -2.42754, this.shape.getClosestPointTo(createTestTriangle(-7, -7)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createTestTriangle(-60, 0)));
        assertClosestPointInBothShapes(this.shape, createTestTriangle(19, 5));
        assertClosestPointInBothShapes(this.shape, createTestTriangle(5, 5));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(3.09077, this.shape.getDistanceSquared(createTestTriangle(-7, -7)));
        assertEpsilonEquals(1736.31148, this.shape.getDistanceSquared(createTestTriangle(-60, 0)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(19, 5)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(5, 5)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(-1.81377, -1.62754, this.shape.getClosestPointTo(createRectangle(-5, -5, 2, 1)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createRectangle(-30, 5, 2, 1)));
        assertClosestPointInBothShapes(this.shape, createRectangle(17, 2, 2, 1));
        assertClosestPointInBothShapes(this.shape, createRectangle(5, 5, 2, 1));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(7.03568, this.shape.getDistanceSquared(createRectangle(-5, -5, 2, 1)));
        assertEpsilonEquals(247.2364, this.shape.getDistanceSquared(createRectangle(-30, 5, 2, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(17, 2, 2, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(5, 5, 2, 1)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(-1.98951, -1.53968, this.shape.getClosestPointTo(createEllipse(-5, -5, 2, 1)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createEllipse(-30, 5, 2, 1)));
        assertClosestPointInBothShapes(this.shape, createEllipse(17, 2, 2, 1));
        assertClosestPointInBothShapes(this.shape, createEllipse(5, 5, 2, 1));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(8.49406, this.shape.getDistanceSquared(createEllipse(-5, -5, 2, 1)));
        assertEpsilonEquals(248.79828, this.shape.getDistanceSquared(createEllipse(-30, 5, 2, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(17, 2, 2, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(5, 5, 2, 1)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(-1.84892, -1.60997, this.shape.getClosestPointTo(createRoundRectangle(-5, -5, 2, 1, .2, .1)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createRoundRectangle(-30, 5, 2, 1, .2, .1)));
        assertClosestPointInBothShapes(this.shape, createRoundRectangle(17, 2, 2, 1, .2, .1));
        assertClosestPointInBothShapes(this.shape, createRoundRectangle(5, 5, 2, 1, .2, .1));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToMultiShape2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(0.98623, -3.02754, this.shape.getClosestPointTo(createTestMultiShape(-7, -7)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createTestMultiShape(-30, 0)));
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(15, 2));
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(5, 5));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredMultiShape2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToPath2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(-2.41377, -1.32754, this.shape.getClosestPointTo(createNonEmptyPath(-5, -5)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createNonEmptyPath(-30, 5)));
        assertClosestPointInBothShapes(this.shape, createNonEmptyPath(15, 2));
        assertClosestPointInBothShapes(this.shape, createNonEmptyPath(5, 5));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredPath2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToParallelogram2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(-1.37273, -1.84807, this.shape.getClosestPointTo(createTestParallelogram(-5, -5)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createTestParallelogram(-30, 5)));
        assertClosestPointInBothShapes(this.shape, createTestParallelogram(18, 2));
        assertClosestPointInBothShapes(this.shape, createTestParallelogram(5, 5));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredParallelogram2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(4.80081, this.shape.getDistanceSquared(createTestParallelogram(-5, -5)));
        assertEpsilonEquals(232.05334, this.shape.getDistanceSquared(createTestParallelogram(-30, 5)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestParallelogram(18, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestParallelogram(5, 5)));
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
        assertFpPointEquals(-2.23766, -1.4156, this.shape.getClosestPointTo(createTestOrientedRectangle(-5, -5)));
        assertFpPointEquals(-12.33574, 3.63344, this.shape.getClosestPointTo(createTestOrientedRectangle(-30, 5)));
        assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(18, 2));
        assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(5, 5));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredOrientedRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(5.66678, this.shape.getDistanceSquared(createTestOrientedRectangle(-5, -5)));
        assertEpsilonEquals(240.45186, this.shape.getDistanceSquared(createTestOrientedRectangle(-30, 5)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(18, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(5, 5)));
    }
	
}