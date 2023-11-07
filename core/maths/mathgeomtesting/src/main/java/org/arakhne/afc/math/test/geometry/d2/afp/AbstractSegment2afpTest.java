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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.GeomConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp.UncertainIntersection;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;

@SuppressWarnings("all")
public abstract class AbstractSegment2afpTest<T extends Segment2afp<?, T, ?, ?, ?, B>,
B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractShape2afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createSegment(0, 0, 1, 1);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCcw(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double x1 = -100;
		double y1 = -100;
		double x2 = 100;
		double y2 = 100;

		assertEquals(0, Segment2afp.ccw(x1, y1, x2, y2, x1, y1, 0));
		assertEquals(0, Segment2afp.ccw(x2, y2, x1, y1, x1, y1, 0));

		assertEquals(0, Segment2afp.ccw(x1, y1, x2, y2, x2, y2, 0));
		assertEquals(0, Segment2afp.ccw(x2, y2, x1, y1, x2, y2, 0));

		assertEquals(0, Segment2afp.ccw(x1, y1, x2, y2, 0, 0, 0));
		assertEquals(0, Segment2afp.ccw(x2, y2, x1, y1, 0, 0, 0));

		assertEquals(-1, Segment2afp.ccw(x1, y1, x2, y2, -200, -200, 0));
		assertEquals(1, Segment2afp.ccw(x2, y2, x1, y1, -200, -200, 0));

		assertEquals(1, Segment2afp.ccw(x1, y1, x2, y2, 200, 200, 0));
		assertEquals(-1, Segment2afp.ccw(x2, y2, x1, y1, 200, 200, 0));

		assertEquals(-1, Segment2afp.ccw(x1, y1, x2, y2, -200, 200, 0));
		assertEquals(1, Segment2afp.ccw(x2, y2, x1, y1, -200, 200, 0));

		assertEquals(1, Segment2afp.ccw(x1, y1, x2, y2, 200, -200, 0));
		assertEquals(-1, Segment2afp.ccw(x2, y2, x1, y1, 200, -200, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointSegmentPoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsClosestPointSegmentPoint(0, 0, 1, 1, 0, 0, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsClosestPointSegmentPoint(0, 0, 1, 1, .75, .75, result);
		assertEpsilonEquals(.75, result.getX());
		assertEpsilonEquals(.75, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsClosestPointSegmentPoint(0, 0, 1, 1, -10, -50, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsClosestPointSegmentPoint(0, 0, 1, 1, 200, -50, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(1, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsClosestPointSegmentPoint(0, 0, 1, 1, 0, 1, result);
		assertEpsilonEquals(.5, result.getX());
		assertEpsilonEquals(.5, result.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointSegmentSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;
		double dist;

		result = createPoint(Double.NaN, Double.NaN);
		dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, -2, 3, -1, 1, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
		assertEpsilonEquals(2, dist);

		result = createPoint(Double.NaN, Double.NaN);
		dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, -1, 1, -3, 2, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
		assertEpsilonEquals(2, dist);

		result = createPoint(Double.NaN, Double.NaN);
		dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, 1, 0, -1, 0, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
		assertEpsilonEquals(0, dist);

		result = createPoint(Double.NaN, Double.NaN);
		dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, -2, 0, 2, 1, result);
		assertEpsilonEquals(0.66667, result.getX());
		assertEpsilonEquals(0.66667, result.getY());
		assertEpsilonEquals(0, dist);

		result = createPoint(Double.NaN, Double.NaN);
		dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, 0, -1, -3, 0, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
		assertEpsilonEquals(.9, dist);

		result = createPoint(Double.NaN, Double.NaN);
		dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, 0, 4, 3, 3, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(1, result.getY());
		assertEpsilonEquals(6.4, dist);

		result = createPoint(Double.NaN, Double.NaN);
		dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, 1, 0, 4, 1, result);
		assertEpsilonEquals(.5, result.getX());
		assertEpsilonEquals(.5, result.getY());
		assertEpsilonEquals(.5, dist);

		result = createPoint(Double.NaN, Double.NaN);
		dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, 1, 0, 3, -1, result);
		assertEpsilonEquals(.5, result.getX());
		assertEpsilonEquals(.5, result.getY());
		assertEpsilonEquals(.5, dist);

		result = createPoint(Double.NaN, Double.NaN);
		dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, 2, 0, 1, -1, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
		assertEpsilonEquals(2, dist);

		result = createPoint(Double.NaN, Double.NaN);
		dist = Segment2afp.findsClosestPointSegmentSegment(0, 0, 1, 1, 3, -1, 1, .4, result);
		assertEpsilonEquals(.7, result.getX());
		assertEpsilonEquals(.7, result.getY());
		assertEpsilonEquals(.18, dist);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsFarthestPointSegmentSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, -2, 3, -1, 1, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(1, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, -1, 1, -3, 2, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(1, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, 1, 0, -1, 0, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(1, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, -2, 0, 2, 1, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, 0, -1, -3, 0, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(1, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, 0, 4, 3, 3, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, 1, 0, 4, 1, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(1, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, 1, 0, 3, -1, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(1, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, 2, 0, 1, -1, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(1, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentSegment(0, 0, 1, 1, 3, -1, 1, .4, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsCircleShadowSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				5, -4, -1, -5));
		assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				-7, -3, -5, -1));
		assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				11, -2, 10, -1));
		assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				3, 5, 1, 6));
		assertEquals(-1, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				5, .5, 6, -1));
		assertEquals(-2, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				5, 2, 6, -1));
		assertEquals(-1, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				5, 2, 6, .5));
		assertEquals(GeomConstants.SHAPE_INTERSECTS, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				.5, .5, 3, 0));
		assertEquals(GeomConstants.SHAPE_INTERSECTS, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				0, 2, 3, 0));
		assertEquals(GeomConstants.SHAPE_INTERSECTS, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				.5, 4, .5, -1));
		assertEquals(-2, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				1, 3, 3, 0));

		assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				-1, -5, 5, -4));
		assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				-5, -1, -7, 3));
		assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				10, -1, 11, -2));
		assertEquals(0, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				1, 6, 3, 5));
		assertEquals(1, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				6, -1, 5, .5));
		assertEquals(2, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				6, -1, 5, 2));
		assertEquals(1, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				6, .5, 5, 2));
		assertEquals(GeomConstants.SHAPE_INTERSECTS, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				3, 0, .5, .5));
		assertEquals(GeomConstants.SHAPE_INTERSECTS, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				3, 0, 0, 2));
		assertEquals(GeomConstants.SHAPE_INTERSECTS, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				.5, -1, .5, 4));
		assertEquals(2, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				1, 1, 1,
				3, 0, 1, 3));
		// One side of the paralleogram in "parallelogram.ggb"
		assertEquals(-2, Segment2afp.calculatesCrossingsCircleShadowSegment(
				0,
				0, 2, 1,
				-5.18034, 9, 12.7082, -8.88854));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsEllipseShadowSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						5, -4, -1, -5));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						-7, 3, -5, -1));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						11, -2, 10, -1));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						3, 5, 1, 6));
		assertEquals(
				-1,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						5, .5, 6, -1));
		assertEquals(
				-2,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						5, 2, 6, -1));
		assertEquals(
				-1,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						5, 2, 6, .5));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						.5, .5, 3, 0));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						0, 1, 3, 0));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						0, 1, 3, 0));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						.5, 2, .5, -1));
		assertEquals(
				-2,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 1, 1,
						1, 1, 7, -5));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						4, -3, 1, 2,
						1, 1, 7, -5));
		assertEquals(
				-2,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						4, -3, 1, 2,
						4.2, 0, 7, -5));

		assertEquals(
				0,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						-1, -5, 5, -4));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						-5, -1, -7, 3));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						10, -1, 11, -2));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						1, 6, 3, 5));
		assertEquals(
				1,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						6, -1, 5, .5));
		assertEquals(
				2,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						6, -1, 5, 2));
		assertEquals(
				1,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						6, .5, 5, 2));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						3, 0, .5, .5));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						3, 0, 0, 1));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						3, 0, 0, 1));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 2, 1,
						.5, -1, .5, 2));
		assertEquals(
				2,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						0, 0, 1, 1,
						7, -5, 1, 1));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						4, -3, 1, 2,
						7, -5, 1, 1));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						4, -3, 1, 2,
						7, -5, 4, 0));
		assertEquals(
				2,
				Segment2afp.calculatesCrossingsEllipseShadowSegment(
						0,
						4, -3, 1, 2,
						7, -5, 4.2, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPointShadowSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(
				1,
				Segment2afp.calculatesCrossingsPointShadowSegment(
						0, 0,
						10, -1, 10, 1));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsPointShadowSegment(
						0, 0,
						10, -1, 10, -.5));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsPointShadowSegment(
						0, 0,
						-10, -1, -10, 1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPointShadowSegmentWithoutEquality(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(
				1,
				Segment2afp.calculatesCrossingsPointShadowSegmentWithoutEquality(
						0, 0,
						10, -1, 10, 1));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsPointShadowSegmentWithoutEquality(
						0, 0,
						10, -1, 10, -.5));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsPointShadowSegmentWithoutEquality(
						0, 0,
						-10, -1, -10, 1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsRectangleShadowSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(
				2,
				Segment2afp.calculatesCrossingsRectangleShadowSegment(
						0,
						0, 0, 1, 1,
						10, -5, 10, 5));
		assertEquals(
				1,
				Segment2afp.calculatesCrossingsRectangleShadowSegment(
						0,
						0, 0, 1, 1,
						10, -5, 10, .5));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsRectangleShadowSegment(
						0,
						0, 0, 1, 1,
						10, -5, 10, -1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsTriangleShadowSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(
				1,
				Segment2afp.calculatesCrossingsTriangleShadowSegment(
						0,
						5, 8, -10, 1, -1, -2,
						10, -5, 10, 5));
		assertEquals(
				-1,
				Segment2afp.calculatesCrossingsTriangleShadowSegment(
						0,
						5, 8, -10, 1, -1, -2,
						10, 5, 10, -5));
		assertEquals(
				-1,
				Segment2afp.calculatesCrossingsTriangleShadowSegment(
						0,
						5, 8, -10, 1, -1, -2,
						10, 5, 0, -4));
		assertEquals(
				1,
				Segment2afp.calculatesCrossingsTriangleShadowSegment(
						0,
						5, 8, -10, 1, -1, -2,
						0, -4, 10, 5));
		assertEquals(
				2,
				Segment2afp.calculatesCrossingsTriangleShadowSegment(
						0,
						5, 8, -10, 1, -1, -2,
						0, -4, 8, 10));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsTriangleShadowSegment(
						0,
						5, 8, -10, 1, -1, -2,
						-20, 0, 8, 10));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsTriangleShadowSegment(
						0,
						5, 8, -10, 1, -1, -2,
						8, 10, -8, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsRoundRectangleShadowSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(
				2,
				Segment2afp.calculatesCrossingsRoundRectangleShadowSegment(
						0,
						0, 0, 1, 1, .1, .2,
						10, -5, 10, 5));
		assertEquals(
				1,
				Segment2afp.calculatesCrossingsRoundRectangleShadowSegment(
						0,
						0, 0, 1, 1, .1, .2,
						10, -5, 10, .5));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsRoundRectangleShadowSegment(
						0,
						0, 0, 1, 1, .1, .2,
						10, -5, 10, -1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsSegmentShadowSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// 0011
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						10, -5, 10, -4));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						10, 5, 10, 4));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						-5, .5, 0, .6));

		assertEquals(
				1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						10, -1, 11, .6));
		assertEquals(
				2,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						10, -1, 11, 2));
		assertEquals(
				1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						10, .5, 11, 2));

		assertEquals(
				-1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						10, 2, 11, .6));
		assertEquals(
				-2,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						10, 2, 11, -1));
		assertEquals(
				-1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						10, .6, 11, -1));

		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						0, .5, .25, .5));

		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						.75, .5, 1, .5));

		assertEquals(
				1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						5, -5, .75, .5));

		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						5, -5, 0, 1));

		assertEquals(
				2,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						5, -5, 1, 1));

		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 0, 1, 1,
						-2, 1, 5, -5));

		// 1100

		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						10, -5, 10, -4));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						10, 5, 10, 4));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						-5, .5, 0, .6));

		assertEquals(
				1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						10, -1, 11, .6));
		assertEquals(
				2,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						10, -1, 11, 2));
		assertEquals(
				1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						10, .5, 11, 2));

		assertEquals(
				-1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						10, 2, 11, .6));
		assertEquals(
				-2,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						10, 2, 11, -1));
		assertEquals(
				-1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						10, .6, 11, -1));

		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						0, .5, .25, .5));

		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						.75, .5, 1, .5));

		assertEquals(
				1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						5, -5, .75, .5));

		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						5, -5, 0, 1));

		assertEquals(
				2,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						5, -5, 1, 1));

		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 0, 0,
						-2, 1, 5, -5));

		// 0110

		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 1, 1, 0,
						10, -5, 10, -4));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 1, 1, 0,
						10, 5, 10, 4));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 1, 1, 0,
						-5, .5, 0, .6));

		assertEquals(
				1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 1, 1, 0,
						10, -1, 11, .6));
		assertEquals(
				2,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 1, 1, 0,
						10, -1, 11, 2));
		assertEquals(
				1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 1, 1, 0,
						10, .5, 11, 2));

		assertEquals(
				-1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 1, 1, 0,
						10, 2, 11, .6));
		assertEquals(
				-2,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 1, 1, 0,
						10, 2, 11, -1));
		assertEquals(
				-1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 1, 1, 0,
						10, .6, 11, -1));

		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 1, 1, 0,
						0, .5, .25, .5));

		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 1, 1, 0,
						.75, .5, 1, .5));

		assertEquals(
				1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 1, 1, 0,
						5, -.01, .75, .5));

		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 1, 1, 0,
						20, -5, -1, 1));

		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						0, 1, 1, 0,
						5, 10, .25, .5));

		// 1001

		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 0, 0, 1,
						10, -5, 10, -4));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 0, 0, 1,
						10, 5, 10, 4));
		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 0, 0, 1,
						-5, .5, 0, .6));

		assertEquals(
				1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 0, 0, 1,
						10, -1, 11, .6));
		assertEquals(
				2,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 0, 0, 1,
						10, -1, 11, 2));
		assertEquals(
				1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 0, 0, 1,
						10, .5, 11, 2));

		assertEquals(
				-1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 0, 0, 1,
						10, 2, 11, .6));
		assertEquals(
				-2,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 0, 0, 1,
						10, 2, 11, -1));
		assertEquals(
				-1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 0, 0, 1,
						10, .6, 11, -1));

		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 0, 0, 1,
						0, .5, .25, .5));

		assertEquals(
				0,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 0, 0, 1,
						.75, .5, 1, .5));

		assertEquals(
				1,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 0, 0, 1,
						20, -5, .75, .5));

		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 0, 0, 1,
						20, -5, 0, 1));

		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 0, 0, 1,
						5, 10, .25, .5));

		// Others

		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						7, -5, 1, 1,
						4, -3, 1, 1));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						4, -3, 1, 1,
						7, -5, 1, 1));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 4, -3,
						7, -5, 1, 1));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						4, -3, 1, 1,
						1, 1, 7, -5));
		assertEquals(
				GeomConstants.SHAPE_INTERSECTS,
				Segment2afp.calculatesCrossingsSegmentShadowSegment(
						0,
						1, 1, 4, -3,
						1, 1, 7, -5));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsFarthestPointSegmentPoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;

		p = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentPoint(0, 0, 1, 1, 0, 0, p);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());

		p = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentPoint(0, 0, 1, 1, .5, .5, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentPoint(0, 0, 1, 1, 1, 1, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentPoint(0, 0, 1, 1, 2, 2, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentPoint(0, 0, 1, 1, -2, 2, p);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());

		p = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentPoint(0, 0, 1, 1, 0.1, 1.2, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = createPoint(Double.NaN, Double.NaN);
		Segment2afp.findsFarthestPointSegmentPoint(0, 0, 1, 1, 10.1, -.2, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsLineLineIntersection(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;

		result = createPoint(Double.NaN, Double.NaN);
		assertTrue(Segment2afp.findsLineLineIntersection(
				1000, 1.5325000286102295, 2500, 1.5325000286102295,
				1184.001080023255, 1.6651813832907332, 1200.7014393876193, 1.372326130924099,
				result));
		assertEpsilonEquals(1191.567365026541, result.getX());
		assertEpsilonEquals(1.532500028610229, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		assertTrue(Segment2afp.findsLineLineIntersection(
				100, 50, 100, 60,
				90, 55, 2000, 55,
				result));
		assertEpsilonEquals(100, result.getX());
		assertEpsilonEquals(55, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		assertFalse(Segment2afp.findsLineLineIntersection(
				100, 50, 100, 60,
				200, 0, 200, 10,
				result));
		assertNaN(result.getX());
		assertNaN(result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		assertTrue(Segment2afp.findsLineLineIntersection(
				100, -50, 100, -60,
				90, 55, 2000, 55,
				result));
		assertEpsilonEquals(100, result.getX());
		assertEpsilonEquals(55, result.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesLineLineIntersectionFactor(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.1277115766843605, Segment2afp.calculatesLineLineIntersectionFactor(
				1000, 1.5325000286102295, 2500, 1.5325000286102295,
				1184.001080023255, 1.6651813832907332, 1200.7014393876193, 1.372326130924099));

		assertEpsilonEquals(.5, Segment2afp.calculatesLineLineIntersectionFactor(
				100, 50, 100, 60,
				90, 55, 2000, 55));

		assertNaN(Segment2afp.calculatesLineLineIntersectionFactor(
				100, 50, 100, 60,
				200, 0, 200, 10));

		assertEpsilonEquals(-10.5, Segment2afp.calculatesLineLineIntersectionFactor(
				100, -50, 100, -60,
				90, 55, 2000, 55));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsProjectedPointPointLine(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.3076923076923077, Segment2afp.findsProjectedPointPointLine(
				2, 1,
				0, 0, 3, -2));

		assertEpsilonEquals(.6666666666666666, Segment2afp.findsProjectedPointPointLine(
				2, 1,
				0, 0, 3, 0));

		assertEpsilonEquals(-.7, Segment2afp.findsProjectedPointPointLine(
				2, -1,
				0, 0, -3, 1));

		assertEpsilonEquals(14.4, Segment2afp.findsProjectedPointPointLine(
				2, 150,
				0, 0, -3, 1));

		assertEpsilonEquals(.5, Segment2afp.findsProjectedPointPointLine(
				.5, .5,
				0, 0, 1, 1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesRelativeDistanceLinePoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-1.941450686788302, Segment2afp.calculatesRelativeDistanceLinePoint(
				0, 0, 3, -2,
				2, 1));
		assertEpsilonEquals(1.941450686788302, Segment2afp.calculatesRelativeDistanceLinePoint(
				3, -2, 0, 0,
				2, 1));

		assertEpsilonEquals(-1, Segment2afp.calculatesRelativeDistanceLinePoint(
				0, 0, 3, 0,
				2, 1));
		assertEpsilonEquals(1, Segment2afp.calculatesRelativeDistanceLinePoint(
				3, 0, 0, 0,
				2, 1));

		assertEpsilonEquals(-.3162277660168379, Segment2afp.calculatesRelativeDistanceLinePoint(
				0, 0, -3, 1,
				2, -1));
		assertEpsilonEquals(.3162277660168379, Segment2afp.calculatesRelativeDistanceLinePoint(
				-3, 1, 0, 0,
				2, -1));

		assertEpsilonEquals(142.9349502396107, Segment2afp.calculatesRelativeDistanceLinePoint(
				0, 0, -3, 1,
				2, 150));
		assertEpsilonEquals(-142.9349502396107, Segment2afp.calculatesRelativeDistanceLinePoint(
				-3, 1, 0, 0,
				2, 150));

		assertEpsilonEquals(0, Segment2afp.calculatesRelativeDistanceLinePoint(
				0, 0, 1, 1,
				.5, .5));
		assertEpsilonEquals(0, Segment2afp.calculatesRelativeDistanceLinePoint(
				1, 1, 0, 0,
				.5, .5));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsSegmentSegmentIntersection(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;

		result = createPoint(Double.NaN, Double.NaN);
		assertTrue(Segment2afp.findsSegmentSegmentIntersection(
				1000, 1.5325000286102295, 2500, 1.5325000286102295,
				1184.001080023255, 1.6651813832907332, 1200.7014393876193, 1.372326130924099,
				result));
		assertEpsilonEquals(1191.567365026541, result.getX());
		assertEpsilonEquals(1.532500028610229, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		assertTrue(Segment2afp.findsSegmentSegmentIntersection(
				100, 50, 100, 60,
				90, 55, 2000, 55,
				result));
		assertEpsilonEquals(100, result.getX());
		assertEpsilonEquals(55, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		assertFalse(Segment2afp.findsSegmentSegmentIntersection(
				100, 50, 100, 60,
				200, 0, 200, 10,
				result));
		assertNaN(result.getX());
		assertNaN(result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		assertFalse(Segment2afp.findsSegmentSegmentIntersection(
				-100, 50, -100, 60,
				90, 55, 2000, 55,
				result));
		assertNaN(result.getX());
		assertNaN(result.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesSegmentSegmentIntersectionFactor(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.1277115766843605, Segment2afp.calculatesSegmentSegmentIntersectionFactor(
				1000, 1.5325000286102295, 2500, 1.5325000286102295,
				1184.001080023255, 1.6651813832907332, 1200.7014393876193, 1.372326130924099));

		assertEpsilonEquals(.5, Segment2afp.calculatesSegmentSegmentIntersectionFactor(
				100, 50, 100, 60,
				90, 55, 2000, 55));

		assertNaN(Segment2afp.calculatesSegmentSegmentIntersectionFactor(
				100, 50, 100, 60,
				200, 0, 200, 10));

		assertNaN(Segment2afp.calculatesSegmentSegmentIntersectionFactor(
				100, -50, 100, -60,
				90, 55, 2000, 55));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsSideLinePoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Segment2afp.findsSideLinePoint(0, 0, 1, 1, 0, 0, 0.1));
		assertEquals(0, Segment2afp.findsSideLinePoint(0, 0, 1, 1, 1, 1, 0.1));
		assertEquals(0, Segment2afp.findsSideLinePoint(0, 0, 1, 1, .25, .25, 0.1));
		assertEquals(1, Segment2afp.findsSideLinePoint(0, 0, 1, 1, 0.2, 0, 0.1));
		assertEquals(1, Segment2afp.findsSideLinePoint(0, 0, 1, 1, 120, 0, 0.1));
		assertEquals(0, Segment2afp.findsSideLinePoint(0, 0, 1, 1, -20.05, -20, 0.1));
		assertEquals(-1, Segment2afp.findsSideLinePoint(0, 0, 1, 1, 0, 0.2, 0.1));
		assertEquals(-1, Segment2afp.findsSideLinePoint(0, 0, 1, 1, 0, 120, 0.1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsUncertainIntersectionSegmentSegmentWithEnds(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
				0, 0, 1, 1,
				0, .5, 1, .5));
		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
				0, 0, 1, 1,
				0, 0, 1, 1));
		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
				0, 0, 1, 1,
				0, 0, 2, 2));
		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
				0, 0, 1, 1,
				0, 0, .5, .5));
		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
				0, 0, 1, 1,
				-3, -3, .5, .5));
		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
				0, 0, 1, 1,
				-3, -3, 0, 0));
		assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
				0, 0, 1, 1,
				-3, -3, -1, -1));

		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
				0, 0, 1, 1,
				-3, 0, 4, 0));

		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
				0, 0, 1, 1,
				-3, -1, 4, -1));

		assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
				0, 0, 1, 1,
				-3, -1, -1, -1));

		assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
				0, 0, 1, 1,
				-3, 0, -2, 1));

		assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
				0, 0, 1, 1,
				10, 0, 9, -1));

		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
				7, -5, 1, 1,
				4, -3, 1, 1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsUncertainIntersectionSegmentSegmentWithoutEnds(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				0, .5, 1, .5));
		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				0, 0, 1, 1));
		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithEnds(
				0, 0, 1, 1,
				0, 0, 2, 2));
		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				0, 0, .5, .5));
		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				-3, -3, .5, .5));
		assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				-3, -3, 0, 0));
		assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				-3, -3, -1, -1));

		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				-3, 0, 4, 0));

		assertSame(UncertainIntersection.PERHAPS, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				-3, -1, 4, -1));

		assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				-3, -1, -1, -1));

		assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				-3, 0, -2, 1));

		assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				10, 0, 9, -1));

		assertSame(UncertainIntersection.NO, Segment2afp.findsUncertainIntersectionSegmentSegmentWithoutEnds(
				7, -5, 1, 1,
				4, -3, 1, 1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesDistanceLinePoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1.941450686788302, Segment2afp.calculatesDistanceLinePoint(
				0, 0, 3, -2,
				2, 1));
		assertEpsilonEquals(1.941450686788302, Segment2afp.calculatesDistanceLinePoint(
				3, -2, 0, 0,
				2, 1));

		assertEpsilonEquals(1, Segment2afp.calculatesDistanceLinePoint(
				0, 0, 3, 0,
				2, 1));
		assertEpsilonEquals(1, Segment2afp.calculatesDistanceLinePoint(
				3, 0, 0, 0,
				2, 1));

		assertEpsilonEquals(.3162277660168379, Segment2afp.calculatesDistanceLinePoint(
				0, 0, -3, 1,
				2, -1));
		assertEpsilonEquals(.3162277660168379, Segment2afp.calculatesDistanceLinePoint(
				-3, 1, 0, 0,
				2, -1));

		assertEpsilonEquals(142.9349502396107, Segment2afp.calculatesDistanceLinePoint(
				0, 0, -3, 1,
				2, 150));
		assertEpsilonEquals(142.9349502396107, Segment2afp.calculatesDistanceLinePoint(
				-3, 1, 0, 0,
				2, 150));

		assertEpsilonEquals(0, Segment2afp.calculatesDistanceLinePoint(
				0, 0, 1, 1,
				.5, .5));
		assertEpsilonEquals(0, Segment2afp.calculatesDistanceLinePoint(
				1, 1, 0, 0,
				.5, .5));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesDistanceSegmentPoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1.941450686788302, Segment2afp.calculatesDistanceSegmentPoint(
				0, 0, 3, -2,
				2, 1));
		assertEpsilonEquals(1.941450686788302, Segment2afp.calculatesDistanceSegmentPoint(
				3, -2, 0, 0,
				2, 1));

		assertEpsilonEquals(1, Segment2afp.calculatesDistanceSegmentPoint(
				0, 0, 3, 0,
				2, 1));
		assertEpsilonEquals(1, Segment2afp.calculatesDistanceSegmentPoint(
				3, 0, 0, 0,
				2, 1));

		assertEpsilonEquals(2.23606797749979, Segment2afp.calculatesDistanceSegmentPoint(
				0, 0, -3, 1,
				2, -1));
		assertEpsilonEquals(2.23606797749979, Segment2afp.calculatesDistanceSegmentPoint(
				-3, 1, 0, 0,
				2, -1));

		assertEpsilonEquals(149.0838690133845, Segment2afp.calculatesDistanceSegmentPoint(
				0, 0, -3, 1,
				2, 150));
		assertEpsilonEquals(149.0838690133845, Segment2afp.calculatesDistanceSegmentPoint(
				-3, 1, 0, 0,
				2, 150));

		assertEpsilonEquals(0, Segment2afp.calculatesDistanceSegmentPoint(
				0, 0, 1, 1,
				.5, .5));
		assertEpsilonEquals(0, Segment2afp.calculatesDistanceSegmentPoint(
				1, 1, 0, 0,
				.5, .5));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesDistanceSquaredLinePoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(3.769230769230769, Segment2afp.calculatesDistanceSquaredLinePoint(
				0, 0, 3, -2,
				2, 1));
		assertEpsilonEquals(3.769230769230769, Segment2afp.calculatesDistanceSquaredLinePoint(
				3, -2, 0, 0,
				2, 1));

		assertEpsilonEquals(1, Segment2afp.calculatesDistanceSquaredLinePoint(
				0, 0, 3, 0,
				2, 1));
		assertEpsilonEquals(1, Segment2afp.calculatesDistanceSquaredLinePoint(
				3, 0, 0, 0,
				2, 1));

		assertEpsilonEquals(.09999999999999996, Segment2afp.calculatesDistanceSquaredLinePoint(
				0, 0, -3, 1,
				2, -1));
		assertEpsilonEquals(.09999999999999996, Segment2afp.calculatesDistanceSquaredLinePoint(
				-3, 1, 0, 0,
				2, -1));

		assertEpsilonEquals(20430.39999999979, Segment2afp.calculatesDistanceSquaredLinePoint(
				0, 0, -3, 1,
				2, 150));
		assertEpsilonEquals(20430.39999999979, Segment2afp.calculatesDistanceSquaredLinePoint(
				-3, 1, 0, 0,
				2, 150));

		assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredLinePoint(
				0, 0, 1, 1,
				.5, .5));
		assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredLinePoint(
				1, 1, 0, 0,
				.5, .5));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesDistanceSquaredSegmentPoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(3.769230769230769, Segment2afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 3, -2,
				2, 1));
		assertEpsilonEquals(3.769230769230769, Segment2afp.calculatesDistanceSquaredSegmentPoint(
				3, -2, 0, 0,
				2, 1));

		assertEpsilonEquals(1, Segment2afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 3, 0,
				2, 1));
		assertEpsilonEquals(1, Segment2afp.calculatesDistanceSquaredSegmentPoint(
				3, 0, 0, 0,
				2, 1));

		assertEpsilonEquals(5, Segment2afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, -3, 1,
				2, -1));
		assertEpsilonEquals(5, Segment2afp.calculatesDistanceSquaredSegmentPoint(
				-3, 1, 0, 0,
				2, -1));

		assertEpsilonEquals(22225.99999999998, Segment2afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, -3, 1,
				2, 150));
		assertEpsilonEquals(22225.99999999998, Segment2afp.calculatesDistanceSquaredSegmentPoint(
				-3, 1, 0, 0,
				2, 150));

		assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 1, 1,
				.5, .5));
		assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredSegmentPoint(
				1, 1, 0, 0,
				.5, .5));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesDistanceSquaredSegmentSegment_v1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// A->B   &   C->D
		assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -2, 3, -1, 1));
		assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -1, 1, -3, 2));
		assertEpsilonEquals(.9, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 0, -1, -3, 0));
		assertEpsilonEquals(6.4, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 0, 4, 3, 3));
		assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 1, 0, 4, 1));
		assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 1, 0, 3, -1));
		assertEpsilonEquals(0.18, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 3, -1, 1, .4));
		// Intersecting segment
		assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -2, 0, 2, 1));
		// Parallel
		assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 2, 0, 1, -1));
		assertEpsilonEquals(16, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -4, 0, -5, -1));
		assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 0, 1, -1, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesDistanceSquaredSegmentSegment_v2(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// A->B   &   D->C
		assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -1, 1, -2, 3));
		assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -3, 2, -1, 1));
		assertEpsilonEquals(.9, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -3, 0, 0, -1));
		assertEpsilonEquals(6.4, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 3, 3, 0, 4));
		assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 4, 1, 1, 0));
		assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 3, -1, 1, 0));
		assertEpsilonEquals(0.18, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 1, .4, 3, -1));
		// Intersecting segment
		assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 2, 1, -2, 0));
		// Parallel
		assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, 1, -1, 2, 0));
		assertEpsilonEquals(16, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -5, -1, -4, 0));
		assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(0, 0, 1, 1, -1, 0, 0, 1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesDistanceSquaredSegmentSegment_v3(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// B->A   &   D->C
		assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -1, 1, -2, 3));
		assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -3, 2, -1, 1));
		assertEpsilonEquals(.9, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -3, 0, 0, -1));
		assertEpsilonEquals(6.4, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 3, 3, 0, 4));
		assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 4, 1, 1, 0));
		assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 3, -1, 1, 0));
		assertEpsilonEquals(0.18, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 1, .4, 3, -1));
		// Intersecting segment
		assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 2, 1, -2, 0));
		// Parallel
		assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 1, -1, 2, 0));
		assertEpsilonEquals(16, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -5, -1, -4, 0));
		assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -1, 0, 0, 1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesDistanceSquaredSegmentSegment_v4(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// B->A   &   C->D
		assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -2, 3, -1, 1));
		assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -1, 1, -3, 2));
		assertEpsilonEquals(.9, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 0, -1, -3, 0));
		assertEpsilonEquals(6.4, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 0, 4, 3, 3));
		assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 1, 0, 4, 1));
		assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 1, 0, 3, -1));
		assertEpsilonEquals(0.18, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 3, -1, 1, .4));
		// Intersecting segment
		assertEpsilonEquals(0, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -2, 0, 2, 1));
		// Parallel
		assertEpsilonEquals(2, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 2, 0, 1, -1));
		assertEpsilonEquals(16, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, -4, 0, -5, -1));
		assertEpsilonEquals(.5, Segment2afp.calculatesDistanceSquaredSegmentSegment(1, 1, 0, 0, 0, 1, -1, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticInterpolates(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.interpolates(1., 2., 3., 4., 0., result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(2, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.interpolates(1., 2., 3., 4., .25, result);
		assertEpsilonEquals(1.5, result.getX());
		assertEpsilonEquals(2.5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.interpolates(1., 2., 3., 4., .5, result);
		assertEpsilonEquals(2, result.getX());
		assertEpsilonEquals(3., result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.interpolates(1., 2., 3., 4., .75, result);
		assertEpsilonEquals(2.5, result.getX());
		assertEpsilonEquals(3.5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Segment2afp.interpolates(1., 2., 3., 4., 1., result);
		assertEpsilonEquals(3, result.getX());
		assertEpsilonEquals(4, result.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsLineLine(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment2afp.intersectsLineLine(
				0, 0, 1, 1,
				0, 0, 1, 1));
		assertTrue(Segment2afp.intersectsLineLine(
				0, 0, 1, 1,
				0, 0, 2, 2));
		assertTrue(Segment2afp.intersectsLineLine(
				0, 0, 1, 1,
				0, 0, .5, .5));
		assertTrue(Segment2afp.intersectsLineLine(
				0, 0, 1, 1,
				-3, -3, .5, .5));
		assertTrue(Segment2afp.intersectsLineLine(
				0, 0, 1, 1,
				-3, -3, 0, 0));
		assertTrue(Segment2afp.intersectsLineLine(
				0, 0, 1, 1,
				-3, -3, -1, -1));

		assertTrue(Segment2afp.intersectsLineLine(
				0, 0, 1, 1,
				-3, 0, 4, 0));

		assertFalse(Segment2afp.intersectsLineLine(
				0, 0, 1, 1,
				-3, 0, -2, 1));

		assertFalse(Segment2afp.intersectsLineLine(
				0, 0, 1, 1,
				10, 0, 9, -1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsSegmentLine(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment2afp.intersectsSegmentLine(
				0, 0, 1, 1,
				0, 0, 1, 1));
		assertTrue(Segment2afp.intersectsSegmentLine(
				0, 0, 1, 1,
				0, 0, 2, 2));
		assertTrue(Segment2afp.intersectsSegmentLine(
				0, 0, 1, 1,
				0, 0, .5, .5));
		assertTrue(Segment2afp.intersectsSegmentLine(
				0, 0, 1, 1,
				-3, -3, .5, .5));
		assertTrue(Segment2afp.intersectsSegmentLine(
				0, 0, 1, 1,
				-3, -3, 0, 0));
		assertTrue(Segment2afp.intersectsSegmentLine(
				0, 0, 1, 1,
				-3, -3, -1, -1));

		assertTrue(Segment2afp.intersectsSegmentLine(
				0, 0, 1, 1,
				-3, 0, 4, 0));

		assertFalse(Segment2afp.intersectsSegmentLine(
				0, 0, 1, 1,
				-3, 0, -2, 1));

		assertFalse(Segment2afp.intersectsSegmentLine(
				0, 0, 1, 1,
				10, 0, 9, -1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsSegmentSegmentWithEnds(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment2afp.intersectsSegmentSegmentWithEnds(
				0, 0, 1, 1,
				0, .5, 1, .5));
		assertTrue(Segment2afp.intersectsSegmentSegmentWithEnds(
				0, 0, 1, 1,
				0, 0, 1, 1));
		assertTrue(Segment2afp.intersectsSegmentSegmentWithEnds(
				0, 0, 1, 1,
				0, 0, 2, 2));
		assertTrue(Segment2afp.intersectsSegmentSegmentWithEnds(
				0, 0, 1, 1,
				0, 0, .5, .5));
		assertTrue(Segment2afp.intersectsSegmentSegmentWithEnds(
				0, 0, 1, 1,
				-3, -3, .5, .5));
		assertTrue(Segment2afp.intersectsSegmentSegmentWithEnds(
				0, 0, 1, 1,
				-3, -3, 0, 0));
		assertFalse(Segment2afp.intersectsSegmentSegmentWithEnds(
				0, 0, 1, 1,
				-3, -3, -1, -1));

		assertTrue(Segment2afp.intersectsSegmentSegmentWithEnds(
				0, 0, 1, 1,
				-3, 0, 4, 0));

		assertFalse(Segment2afp.intersectsSegmentSegmentWithEnds(
				0, 0, 1, 1,
				-3, -1, 4, -1));

		assertFalse(Segment2afp.intersectsSegmentSegmentWithEnds(
				0, 0, 1, 1,
				-3, -1, -1, -1));

		assertFalse(Segment2afp.intersectsSegmentSegmentWithEnds(
				0, 0, 1, 1,
				-3, 0, -2, 1));

		assertFalse(Segment2afp.intersectsSegmentSegmentWithEnds(
				0, 0, 1, 1,
				10, 0, 9, -1));

		assertTrue(
				Segment2afp.intersectsSegmentSegmentWithEnds(
						7, -5, 1, 1,
						4, -3, 1, 1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsSegmentSegmentWithoutEnds(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment2afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				0, .5, 1, .5));
		assertTrue(Segment2afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				0, 0, 1, 1));
		assertTrue(Segment2afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				0, 0, 2, 2));
		assertTrue(Segment2afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				0, 0, .5, .5));
		assertTrue(Segment2afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				-3, -3, .5, .5));
		assertFalse(Segment2afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				-3, -3, 0, 0));
		assertFalse(Segment2afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				-3, -3, -1, -1));

		assertFalse(Segment2afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				-3, 0, 4, 0));

		assertFalse(Segment2afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				-3, -1, 4, -1));

		assertFalse(Segment2afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				-3, -1, -1, -1));

		assertFalse(Segment2afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				-3, 0, -2, 1));

		assertFalse(Segment2afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 1, 1,
				10, 0, 9, -1));

		assertFalse(
				Segment2afp.intersectsSegmentSegmentWithoutEnds(
						7, -5, 1, 1,
						4, -3, 1, 1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIsCollinearLines(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment2afp.isCollinearLines(0, 0, 1, 1, 0, 0, 1, 1));
		assertTrue(Segment2afp.isCollinearLines(0, 0, 1, 1, 1, 1, 0, 0));
		assertTrue(Segment2afp.isCollinearLines(0, 0, 1, 1, 0, 0, -1, -1));
		assertTrue(Segment2afp.isCollinearLines(0, 0, 1, 1, -2, -2, -3, -3));
		assertFalse(Segment2afp.isCollinearLines(0, 0, 1, 1, 5, 0, 6, 1));
		assertFalse(Segment2afp.isCollinearLines(0, 0, 1, 1, 154, -124, -2, 457));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIsParallelLines(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment2afp.isParallelLines(0, 0, 1, 1, 0, 0, 1, 1));
		assertTrue(Segment2afp.isParallelLines(0, 0, 1, 1, 1, 1, 0, 0));
		assertTrue(Segment2afp.isParallelLines(0, 0, 1, 1, 0, 0, -1, -1));
		assertTrue(Segment2afp.isParallelLines(0, 0, 1, 1, -2, -2, -3, -3));
		assertTrue(Segment2afp.isParallelLines(0, 0, 1, 1, 5, 0, 6, 1));
		assertFalse(Segment2afp.isParallelLines(0, 0, 1, 1, 154, -124, -2, 457));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIsPointClosedToLine(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment2afp.isPointClosedToLine(0, 0, 1, 1, 0, 0, 0.1));
		assertTrue(Segment2afp.isPointClosedToLine(0, 0, 1, 1, 1, 1, 0.1));
		assertTrue(Segment2afp.isPointClosedToLine(0, 0, 1, 1, .25, .25, 0.1));
		assertFalse(Segment2afp.isPointClosedToLine(0, 0, 1, 1, 0.2, 0, 0.1));
		assertFalse(Segment2afp.isPointClosedToLine(0, 0, 1, 1, 120, 0, 0.1));
		assertTrue(Segment2afp.isPointClosedToLine(0, 0, 1, 1, -20.05, -20, 0.1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIsPointClosedToSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment2afp.isPointClosedToSegment(0, 0, 1, 1, 0, 0, 0.1));
		assertTrue(Segment2afp.isPointClosedToSegment(0, 0, 1, 1, 1, 1, 0.1));
		assertTrue(Segment2afp.isPointClosedToSegment(0, 0, 1, 1, .25, .25, 0.1));
		assertFalse(Segment2afp.isPointClosedToSegment(0, 0, 1, 1, 0.2, 0, 0.1));
		assertFalse(Segment2afp.isPointClosedToSegment(0, 0, 1, 1, 120, 0, 0.1));
		assertFalse(Segment2afp.isPointClosedToSegment(0, 0, 1, 1, -20.05, -20, 0.1));
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
		assertEpsilonEquals(0, clone.getX1());
		assertEpsilonEquals(0, clone.getY1());
		assertEpsilonEquals(1, clone.getX2());
		assertEpsilonEquals(1, clone.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsObject(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createSegment(0, 0, 5, 0)));
		assertFalse(this.shape.equals(createSegment(0, 0, 2, 2)));
		assertFalse(this.shape.equals(createCircle(5, 8, 6)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createSegment(0, 0, 1, 1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsObject_withPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createSegment(0, 0, 5, 0).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(0, 0, 2, 2).getPathIterator()));
		assertFalse(this.shape.equals(createCircle(5, 8, 6).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createSegment(0, 0, 1, 1).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsToPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
		assertFalse(this.shape.equalsToPathIterator(createSegment(0, 0, 5, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(0, 0, 2, 2).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createCircle(5, 8, 6).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createSegment(0, 0, 1, 1).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsToShape(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createSegment(0, 0, 5, 0)));
		assertFalse(this.shape.equalsToShape((T) createSegment(0, 0, 2, 2)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createSegment(0, 0, 1, 1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void isEmpty(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void clear(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		assertEpsilonEquals(0, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(0, this.shape.getX2());
		assertEpsilonEquals(0, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getP1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p = this.shape.getP1();
		assertNotNull(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getP2(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p = this.shape.getP2();
		assertNotNull(p);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setP1DoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setP1(123.456, -789.159);
		assertEpsilonEquals(123.456, this.shape.getX1());
		assertEpsilonEquals(-789.159, this.shape.getY1());
		assertEpsilonEquals(1, this.shape.getX2());
		assertEpsilonEquals(1, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setP1Point2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setP1(createPoint(123.456, -789.159));
		assertEpsilonEquals(123.456, this.shape.getX1());
		assertEpsilonEquals(-789.159, this.shape.getY1());
		assertEpsilonEquals(1, this.shape.getX2());
		assertEpsilonEquals(1, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setP2DoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setP2(123.456, -789.159);
		assertEpsilonEquals(0, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(123.456, this.shape.getX2());
		assertEpsilonEquals(-789.159, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setP2Point2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setP2(createPoint(123.456, -789.159));
		assertEpsilonEquals(0, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(123.456, this.shape.getX2());
		assertEpsilonEquals(-789.159, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getX1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getX1());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getX2(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1, this.shape.getX2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getY1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getY1());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getY2(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void length(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(Math.sqrt(2), this.shape.getLength());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void lengthSquared(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2, this.shape.getLengthSquared());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(123.456, 456.789, 789.123, 159.753);
		assertEpsilonEquals(123.456, this.shape.getX1());
		assertEpsilonEquals(456.789, this.shape.getY1());
		assertEpsilonEquals(789.123, this.shape.getX2());
		assertEpsilonEquals(159.753, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setPoint2DPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(createPoint(123.456, 456.789), createPoint(789.123, 159.753));
		assertEpsilonEquals(123.456, this.shape.getX1());
		assertEpsilonEquals(456.789, this.shape.getY1());
		assertEpsilonEquals(789.123, this.shape.getX2());
		assertEpsilonEquals(159.753, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setX1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setX1(123.456);
		assertEpsilonEquals(123.456, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(1, this.shape.getX2());
		assertEpsilonEquals(1, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setX2() {
		this.shape.setX2(123.456);
		assertEpsilonEquals(0, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(123.456, this.shape.getX2());
		assertEpsilonEquals(1, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setY1() {
		this.shape.setY1(123.456);
		assertEpsilonEquals(0, this.shape.getX1());
		assertEpsilonEquals(123.456, this.shape.getY1());
		assertEpsilonEquals(1, this.shape.getX2());
		assertEpsilonEquals(1, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setY2() {
		this.shape.setY2(123.456);
		assertEpsilonEquals(0, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(1, this.shape.getX2());
		assertEpsilonEquals(123.456, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void transformTransform2D() {
		Segment2afp s;
		Transform2D tr;

		tr = new Transform2D();
		s = this.shape.clone();
		s.transform(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(1, s.getX2());
		assertEpsilonEquals(1, s.getY2());

		tr = new Transform2D();
		tr.setTranslation(3.4, 4.5);
		s = this.shape.clone();
		s.transform(tr);
		assertEpsilonEquals(3.4, s.getX1());
		assertEpsilonEquals(4.5, s.getY1());
		assertEpsilonEquals(4.4, s.getX2());
		assertEpsilonEquals(5.5, s.getY2());

		tr = new Transform2D();
		tr.setRotation(MathConstants.PI);
		s = this.shape.clone();
		s.transform(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(-1, s.getX2());
		assertEpsilonEquals(-1, s.getY2());

		tr = new Transform2D();
		tr.setRotation(MathConstants.QUARTER_PI);
		s = this.shape.clone();
		s.transform(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getX2());
		assertEpsilonEquals(1.414213562, s.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void clipToRectangle() {
		this.shape.set(20, 45, 43, 15);
		assertTrue(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(20, this.shape.getX1());
		assertEpsilonEquals(45, this.shape.getY1());
		assertEpsilonEquals(43, this.shape.getX2());
		assertEpsilonEquals(15, this.shape.getY2());

		this.shape.set(20, 55, 43, 15);
		assertTrue(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(23.45, this.shape.getX1());
		assertEpsilonEquals(49.0, this.shape.getY1());
		assertEpsilonEquals(43, this.shape.getX2());
		assertEpsilonEquals(15, this.shape.getY2());

		this.shape.set(20, 0, 43, 15);
		assertTrue(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(38.4, this.shape.getX1());
		assertEpsilonEquals(12.0, this.shape.getY1());
		assertEpsilonEquals(43, this.shape.getX2());
		assertEpsilonEquals(15, this.shape.getY2());

		this.shape.set(0, 45, 43, 15);
		assertTrue(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(10, this.shape.getX1());
		assertEpsilonEquals(38.02326, this.shape.getY1());
		assertEpsilonEquals(43, this.shape.getX2());
		assertEpsilonEquals(15, this.shape.getY2());

		this.shape.set(20, 45, 60, 15);
		assertTrue(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(20, this.shape.getX1());
		assertEpsilonEquals(45, this.shape.getY1());
		assertEpsilonEquals(50, this.shape.getX2());
		assertEpsilonEquals(22.5, this.shape.getY2());

		this.shape.set(5, 45, 30, 55);
		assertTrue(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(10, this.shape.getX1());
		assertEpsilonEquals(47, this.shape.getY1());
		assertEpsilonEquals(15, this.shape.getX2());
		assertEpsilonEquals(49, this.shape.getY2());

		this.shape.set(40, 55, 60, 15);
		assertTrue(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(43, this.shape.getX1());
		assertEpsilonEquals(49, this.shape.getY1());
		assertEpsilonEquals(50, this.shape.getX2());
		assertEpsilonEquals(35, this.shape.getY2());

		this.shape.set(40, 0, 60, 40);
		assertTrue(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(46, this.shape.getX1());
		assertEpsilonEquals(12, this.shape.getY1());
		assertEpsilonEquals(50, this.shape.getX2());
		assertEpsilonEquals(20, this.shape.getY2());

		this.shape.set(0, 40, 20, 0);
		assertTrue(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(10, this.shape.getX1());
		assertEpsilonEquals(20, this.shape.getY1());
		assertEpsilonEquals(14, this.shape.getX2());
		assertEpsilonEquals(12, this.shape.getY2());

		this.shape.set(0, 45, 100, 15);
		assertTrue(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(10, this.shape.getX1());
		assertEpsilonEquals(42, this.shape.getY1());
		assertEpsilonEquals(50, this.shape.getX2());
		assertEpsilonEquals(30, this.shape.getY2());

		this.shape.set(20, 100, 43, 0);
		assertTrue(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(31.73, this.shape.getX1());
		assertEpsilonEquals(49, this.shape.getY1());
		assertEpsilonEquals(40.24, this.shape.getX2());
		assertEpsilonEquals(12, this.shape.getY2());

		this.shape.set(20, 100, 43, 101);
		assertFalse(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(20, this.shape.getX1());
		assertEpsilonEquals(100, this.shape.getY1());
		assertEpsilonEquals(43, this.shape.getX2());
		assertEpsilonEquals(101, this.shape.getY2());

		this.shape.set(100, 45, 102, 15);
		assertFalse(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(100, this.shape.getX1());
		assertEpsilonEquals(45, this.shape.getY1());
		assertEpsilonEquals(102, this.shape.getX2());
		assertEpsilonEquals(15, this.shape.getY2());

		this.shape.set(20, 0, 43, -2);
		assertFalse(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(20, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(43, this.shape.getX2());
		assertEpsilonEquals(-2, this.shape.getY2());

		this.shape.set(-100, 45, -48, 15);
		assertFalse(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(-100, this.shape.getX1());
		assertEpsilonEquals(45, this.shape.getY1());
		assertEpsilonEquals(-48, this.shape.getX2());
		assertEpsilonEquals(15, this.shape.getY2());

		this.shape.set(-100, 60, -98, 61);
		assertFalse(this.shape.clipToRectangle(10, 12, 50, 49));
		assertEpsilonEquals(-100, this.shape.getX1());
		assertEpsilonEquals(60, this.shape.getY1());
		assertEpsilonEquals(-98, this.shape.getX2());
		assertEpsilonEquals(61, this.shape.getY2());
	}

	@Override
	public void containsDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.contains(0, 0));
		assertTrue(this.shape.contains(.5, .5));
		assertTrue(this.shape.contains(1, 1));
		assertFalse(this.shape.contains(2.3, 4.5));
		assertFalse(this.shape.contains(2, 2));
	}

	@Override
	public void containsPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.contains(createPoint(0, 0)));
		assertTrue(this.shape.contains(createPoint(.5, .5)));
		assertTrue(this.shape.contains(createPoint(1, 1)));
		assertFalse(this.shape.contains(createPoint(2.3, 4.5)));
		assertFalse(this.shape.contains(createPoint(2, 2)));
	}

	@Override
	public void getClosestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;

		p = this.shape.getClosestPointTo(createPoint(0, 0));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = this.shape.getClosestPointTo(createPoint(.5, .5));
		assertEpsilonEquals(.5, p.getX());
		assertEpsilonEquals(.5, p.getY());

		p = this.shape.getClosestPointTo(createPoint(1, 1));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());

		p = this.shape.getClosestPointTo(createPoint(2, 2));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());

		p = this.shape.getClosestPointTo(createPoint(-2, 2));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = this.shape.getClosestPointTo(createPoint(0.1, 1.2));
		assertEpsilonEquals(0.65, p.getX());
		assertEpsilonEquals(0.65, p.getY());

		p = this.shape.getClosestPointTo(createPoint(10.1, -.2));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());
	}

	@Override
	public void getFarthestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;

		p = this.shape.getFarthestPointTo(createPoint(0, 0));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(.5, .5));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(1, 1));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(2, 2));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(-2, 2));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(0.1, 1.2));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(10.1, -.2));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
	}

	@Override
	public void getDistance(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(0, 0)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(.5, .5)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(1, 1)));

		assertEpsilonEquals(3.733630941, this.shape.getDistance(createPoint(2.3, 4.5)));

		assertEpsilonEquals(1.414213562, this.shape.getDistance(createPoint(2, 2)));
	}

	@Override
	public void getDistanceSquared(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(0, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(.5, .5)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(1, 1)));

		assertEpsilonEquals(13.94, this.shape.getDistanceSquared(createPoint(2.3, 4.5)));

		assertEpsilonEquals(2, this.shape.getDistanceSquared(createPoint(2, 2)));
	}

	@Override
	public void getDistanceL1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(0, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(.5, .5)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(1, 1)));

		assertEpsilonEquals(4.8, this.shape.getDistanceL1(createPoint(2.3, 4.5)));

		assertEpsilonEquals(2, this.shape.getDistanceL1(createPoint(2, 2)));
	}

	@Override
	public void getDistanceLinf(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(0, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(.5, .5)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(1, 1)));

		assertEpsilonEquals(3.5, this.shape.getDistanceLinf(createPoint(2.3, 4.5)));

		assertEpsilonEquals(1, this.shape.getDistanceLinf(createPoint(2, 2)));
	}

	@Override
	public void setIT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createSegment(123.456, 456.789, 789.123, 159.753));
		assertEpsilonEquals(123.456, this.shape.getX1());
		assertEpsilonEquals(456.789, this.shape.getY1());
		assertEpsilonEquals(789.123, this.shape.getX2());
		assertEpsilonEquals(159.753, this.shape.getY2());
	}

	@Override
	public void getPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0,0);
		assertElement(pi, PathElementType.LINE_TO, 1,1);
		assertNoElement(pi);
	}

	@Override
	public void getPathIteratorTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Transform2D tr;
		PathIterator2afp pi;

		tr = new Transform2D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0,0);
		assertElement(pi, PathElementType.LINE_TO, 1,1);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(3.4, 4.5);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 3.4, 4.5);
		assertElement(pi, PathElementType.LINE_TO, 4.4, 5.5);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 0, 1.414213562);
		assertNoElement(pi);
	}

	@Override
	public void createTransformedShape(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Segment2afp s;
		Transform2D tr;

		tr = new Transform2D();    	
		s = (Segment2afp) this.shape.createTransformedShape(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(1, s.getX2());
		assertEpsilonEquals(1, s.getY2());

		tr = new Transform2D();
		tr.setTranslation(3.4, 4.5);
		s = (Segment2afp) this.shape.createTransformedShape(tr);
		assertEpsilonEquals(3.4, s.getX1());
		assertEpsilonEquals(4.5, s.getY1());
		assertEpsilonEquals(4.4, s.getX2());
		assertEpsilonEquals(5.5, s.getY2());

		tr = new Transform2D();
		tr.setRotation(MathConstants.PI);
		s = (Segment2afp) this.shape.createTransformedShape(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(-1, s.getX2());
		assertEpsilonEquals(-1, s.getY2());

		tr = new Transform2D();
		tr.setRotation(MathConstants.QUARTER_PI);
		s = (Segment2afp) this.shape.createTransformedShape(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getX2());
		assertEpsilonEquals(1.414213562, s.getY2());
	}

	@Override
	public void translateDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.translate(3.4, 4.5);
		assertEpsilonEquals(3.4, this.shape.getX1());
		assertEpsilonEquals(4.5, this.shape.getY1());
		assertEpsilonEquals(4.4, this.shape.getX2());
		assertEpsilonEquals(5.5, this.shape.getY2());
	}

	@Override
	public void translateVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(3.4, 4.5));
		assertEpsilonEquals(3.4, this.shape.getX1());
		assertEpsilonEquals(4.5, this.shape.getY1());
		assertEpsilonEquals(4.4, this.shape.getX2());
		assertEpsilonEquals(5.5, this.shape.getY2());
	}

	@Override
	public void toBoundingBox(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B bb = this.shape.toBoundingBox();
		assertEpsilonEquals(0, bb.getMinX());
		assertEpsilonEquals(0, bb.getMinY());
		assertEpsilonEquals(1, bb.getMaxX());
		assertEpsilonEquals(1, bb.getMaxY());
	}

	@Override
	public void toBoundingBoxB(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B bb = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(bb);
		assertEpsilonEquals(0, bb.getMinX());
		assertEpsilonEquals(0, bb.getMinY());
		assertEpsilonEquals(1, bb.getMaxX());
		assertEpsilonEquals(1, bb.getMaxY());
	}

	@Override
	public void containsRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createRectangle(0, 0, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(0, 0, 0, 0)));
		assertFalse(this.shape.contains(createRectangle(10, 10, 1, 1)));
		this.shape.set(10, 15, 10, 18);
		assertFalse(this.shape.contains(createRectangle(10, 16, 0, 1)));
	}

	@Override
	public void containsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createCircle(0, 0, 1)));
		assertFalse(this.shape.contains(createCircle(0, 0, 0)));
		assertFalse(this.shape.contains(createCircle(10, 10, 1)));
	}

	@Override
	public void intersectsRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(20, 45, 43, 15);
		assertTrue(this.shape.intersects(createRectangle(10, 12, 40, 37)));

		this.shape.set(20, 55, 43, 15);
		assertTrue(this.shape.intersects(createRectangle(10, 12, 40, 37)));

		this.shape.set(20, 0, 43, 15);
		assertTrue(this.shape.intersects(createRectangle(10, 12, 40, 37)));

		this.shape.set(0, 45, 43, 15);
		assertTrue(this.shape.intersects(createRectangle(10, 12, 40, 37)));

		this.shape.set(20, 45, 60, 15);
		assertTrue(this.shape.intersects(createRectangle(10, 12, 40, 37)));

		this.shape.set(5, 45, 30, 55);
		assertTrue(this.shape.intersects(createRectangle(10, 12, 40, 37)));

		this.shape.set(40, 55, 60, 15);
		assertTrue(this.shape.intersects(createRectangle(10, 12, 40, 37)));

		this.shape.set(40, 0, 60, 40);
		assertTrue(this.shape.intersects(createRectangle(10, 12, 40, 37)));

		this.shape.set(0, 40, 20, 0);
		assertTrue(this.shape.intersects(createRectangle(10, 12, 40, 37)));

		this.shape.set(0, 45, 100, 15);
		assertTrue(this.shape.intersects(createRectangle(10, 12, 40, 37)));

		this.shape.set(20, 100, 43, 0);
		assertTrue(this.shape.intersects(createRectangle(10, 12, 40, 37)));

		this.shape.set(20, 100, 43, 101);
		assertFalse(this.shape.intersects(createRectangle(10, 12, 40, 37)));

		this.shape.set(100, 45, 102, 15);
		assertFalse(this.shape.intersects(createRectangle(10, 12, 40, 37)));

		this.shape.set(20, 0, 43, -2);
		assertFalse(this.shape.intersects(createRectangle(10, 12, 40, 37)));

		this.shape.set(-100, 45, -48, 15);
		assertFalse(this.shape.intersects(createRectangle(10, 12, 50, 49)));

		this.shape.set(-100, 60, -98, 61);
		assertFalse(this.shape.intersects(createRectangle(10, 12, 40, 37)));
	}

	@Override
	public void intersectsCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createCircle(10, 10, 1)));
		assertTrue(this.shape.intersects(createCircle(0, 0, 1)));
		assertTrue(this.shape.intersects(createCircle(0, .5, 1)));
		assertTrue(this.shape.intersects(createCircle(.5, 0, 1)));
		assertTrue(this.shape.intersects(createCircle(.5, .5, 1)));
		assertFalse(this.shape.intersects(createCircle(2, 0, 1)));
		assertFalse(this.shape.intersects(createCircle(12, 8, 2)));
		assertFalse(this.shape.intersects(createCircle(12, 8, 2.1)));
		assertFalse(this.shape.intersects(createCircle(2, 1, 1)));
		assertTrue(this.shape.intersects(createCircle(2, 1, 1.1)));
		this.shape.set(0, 0, 3, 0);
		assertFalse(this.shape.intersects(createCircle(2, 1, 1)));
		assertTrue(this.shape.intersects(createCircle(2, 1, 1.1)));
	}

	@Override
	public void intersectsEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(0, 0, 2, 1);
		assertFalse(this.shape.intersects(createEllipseFromCorners(5, -4, -1, -5)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(-7, 3, -5, -1)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(11, -2, 10, -1)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(3, 5, 1, 6)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(5, .5, 6, -1)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(5, 2, 6, -1)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(5, 2, 6, .5)));
		assertTrue(this.shape.intersects(createEllipseFromCorners(.5, .5, 3, 0)));
		assertTrue(this.shape.intersects(createEllipseFromCorners(0, 1, 3, 0)));
		assertTrue(this.shape.intersects(createEllipseFromCorners(0, 1, 3, 0)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(.5, 2, .5, -1)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(-1, -5, 5, -4)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(-5, -1, -7, 3)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(10, -1, 11, -2)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(1, 6, 3, 5)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(6, -1, 5, .5)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(6, -1, 5, 2)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(6, .5, 5, 2)));
		assertTrue(this.shape.intersects(createEllipseFromCorners(3, 0, .5, .5)));
		assertTrue(this.shape.intersects(createEllipseFromCorners(3, 0, 0, 1)));
		assertTrue(this.shape.intersects(createEllipseFromCorners(3, 0, 0, 1)));
		assertFalse(this.shape.intersects(createEllipseFromCorners(.5, -1, .5, 2)));
	}

	@Override
	public void intersectsSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createSegment(10, -5, 10, -4)));
		assertFalse(this.shape.intersects(createSegment(10, 5, 10, 4)));
		assertFalse(this.shape.intersects(createSegment(-5, .5, 0, .6)));
		assertFalse(this.shape.intersects(createSegment(10, -1, 11, .6)));
		assertFalse(this.shape.intersects(createSegment(10, -1, 11, 2)));
		assertFalse(this.shape.intersects(createSegment(10, .5, 11, 2)));
		assertFalse(this.shape.intersects(createSegment(10, 2, 11, .6)));
		assertFalse(this.shape.intersects(createSegment(10, 2, 11, -1)));
		assertFalse(this.shape.intersects(createSegment(10, .6, 11, -1)));
		assertFalse(this.shape.intersects(createSegment(0, .5, .25, .5)));
		assertFalse(this.shape.intersects(createSegment(.75, .5, 1, .5)));
		assertFalse(this.shape.intersects(createSegment(5, -5, .75, .5)));
		assertTrue(this.shape.intersects(createSegment(5, -5, 0, 1)));
		assertTrue(this.shape.intersects(createSegment(5, -5, 1, 1)));
		assertFalse(this.shape.intersects(createSegment(-2, 1, 5, -5)));
	}

	@Override
	public void intersectsTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
		assertTrue(createSegment(-6, -2, -4, 0).intersects(triangle));
		assertTrue(createSegment(-6, -2, 2, 0).intersects(triangle));
		assertFalse(createSegment(-6, -2, 14, -4).intersects(triangle));
		assertTrue(createSegment(-2, 2, 4, 0).intersects(triangle));
		assertTrue(createSegment(-2, 2, 0, 0).intersects(triangle));
		assertTrue(createSegment(-4, -2, -6, 6).intersects(triangle));
		assertTrue(createSegment(6, 7, -6, 6).intersects(triangle));
		assertTrue(createSegment(0, 5, -6, 6).intersects(triangle));
		assertFalse(createSegment(-5, 5, -6, 6).intersects(triangle));

		assertFalse(createSegment(-4, -2, 2, -2).intersects(triangle));
		assertFalse(createSegment(-1, -2, 5, 8).intersects(triangle));
	}

	@Override
	public void intersectsPath2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2afp p;

		p = createPath();
		p.moveTo(-2, -2);
		p.lineTo(-2, 2);
		p.lineTo(2, 2);
		p.lineTo(2, -2);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-2, -2);
		p.lineTo(0, 0);
		p.lineTo(-2, 2);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertFalse(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-2, -2);
		p.lineTo(2, 2);
		p.lineTo(-2, 2);
		assertTrue(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-2, -2);
		p.lineTo(-2, 2);
		p.lineTo(2, -2);
		assertTrue(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-2, 2);
		p.lineTo(1, 0);
		p.lineTo(2, 1);
		assertTrue(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-2, 2);
		p.lineTo(2, 1);
		p.lineTo(1, 0);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));
	}

	@Override
	public void intersectsPathIterator2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2afp<?, ?, ?, ?, ?, B> p;

		p = createPath();
		p.moveTo(-2, -2);
		p.lineTo(-2, 2);
		p.lineTo(2, 2);
		p.lineTo(2, -2);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-2, -2);
		p.lineTo(0, 0);
		p.lineTo(-2, 2);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertFalse(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-2, -2);
		p.lineTo(2, 2);
		p.lineTo(-2, 2);
		assertTrue(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-2, -2);
		p.lineTo(-2, 2);
		p.lineTo(2, -2);
		assertTrue(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-2, 2);
		p.lineTo(1, 0);
		p.lineTo(2, 1);
		assertTrue(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-2, 2);
		p.lineTo(2, 1);
		p.lineTo(1, 0);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));
	}

	@Override
	public void intersectsOrientedRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createOrientedRectangle(
				7, 3.5,
				-0.99839, -0.05671,
				8.06684, 1.81085)));
		assertFalse(this.shape.intersects(createOrientedRectangle(
				7, 3.5,
				-0.7471, -0.66471,
				9.43417, 1.81085)));
		assertTrue(this.shape.intersects(createOrientedRectangle(
				7, 3.5,
				-0.81997, -0.57241,
				6.94784, 1.81085)));
		assertTrue(this.shape.intersects(createOrientedRectangle(
				7, 3.5,
				-0.84335, -0.53736,
				8.80456, 1.81085)));
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
		assertFalse(createSegment(0, 0, 1, 1).intersects(para));
		assertTrue(createSegment(5, 5, 4, 6).intersects(para));
		assertTrue(createSegment(2, -2, 5, 0).intersects(para));
		assertFalse(createSegment(-20, -5, -10, 6).intersects(para));
		assertFalse(createSegment(-5, 0, -10, 16).intersects(para));
		assertTrue(createSegment(-10, 1, 10, 20).intersects(para));
	}

	@Override
	public void intersectsRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		RoundRectangle2afp<?, ?, ?, ?, ?, B> box = createRoundRectangle(0, 0, 1, 1, .2, .4);
		assertTrue(this.shape.intersects(box));
		assertTrue(createSegment(-5, -5, 1, 1).intersects(box));
		assertTrue(createSegment(.5, .5, 5, 5).intersects(box));
		assertTrue(createSegment(.5, .5, 5, .6).intersects(box));
		assertTrue(createSegment(-5, -5, 5, 5).intersects(box));
		assertFalse(createSegment(-5, -5, -4, -4).intersects(box));
		assertFalse(createSegment(-5, -5, 4, -4).intersects(box));
		assertFalse(createSegment(5, -5, 6, 5).intersects(box));
		assertFalse(createSegment(5, -5, 5, 5).intersects(box));
		assertFalse(createSegment(-1, -1, 0.01, 0.01).intersects(box));
		assertTrue(createSegment(-1, -1, 0.1, 0.2).intersects(box));
	}

	@Override
	public void intersectsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape2D) createCircle(0, 0, 1)));
		assertFalse(this.shape.intersects((Shape2D) createEllipseFromCorners(5, 2, 6, .5)));
	}

	@Override
	public void operator_addVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(3.4, 4.5));
		assertEpsilonEquals(3.4, this.shape.getX1());
		assertEpsilonEquals(4.5, this.shape.getY1());
		assertEpsilonEquals(4.4, this.shape.getX2());
		assertEpsilonEquals(5.5, this.shape.getY2());
	}

	@Override
	public void operator_plusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_plus(createVector(3.4, 4.5));
		assertNotSame(shape, this.shape);
		assertEpsilonEquals(3.4, shape.getX1());
		assertEpsilonEquals(4.5, shape.getY1());
		assertEpsilonEquals(4.4, shape.getX2());
		assertEpsilonEquals(5.5, shape.getY2());
	}

	@Override
	public void operator_removeVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(3.4, 4.5));
		assertEpsilonEquals(-3.4, this.shape.getX1());
		assertEpsilonEquals(-4.5, this.shape.getY1());
		assertEpsilonEquals(-2.4, this.shape.getX2());
		assertEpsilonEquals(-3.5, this.shape.getY2());
	}

	@Override
	public void operator_minusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_minus(createVector(3.4, 4.5));
		assertEpsilonEquals(-3.4, shape.getX1());
		assertEpsilonEquals(-4.5, shape.getY1());
		assertEpsilonEquals(-2.4, shape.getX2());
		assertEpsilonEquals(-3.5, shape.getY2());
	}

	@Override
	public void operator_multiplyTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Segment2afp s;
		Transform2D tr;

		tr = new Transform2D();    	
		s = (Segment2afp) this.shape.operator_multiply(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(1, s.getX2());
		assertEpsilonEquals(1, s.getY2());

		tr = new Transform2D();
		tr.setTranslation(3.4, 4.5);
		s = (Segment2afp) this.shape.operator_multiply(tr);
		assertEpsilonEquals(3.4, s.getX1());
		assertEpsilonEquals(4.5, s.getY1());
		assertEpsilonEquals(4.4, s.getX2());
		assertEpsilonEquals(5.5, s.getY2());

		tr = new Transform2D();
		tr.setRotation(MathConstants.PI);
		s = (Segment2afp) this.shape.operator_multiply(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(-1, s.getX2());
		assertEpsilonEquals(-1, s.getY2());

		tr = new Transform2D();
		tr.setRotation(MathConstants.QUARTER_PI);
		s = (Segment2afp) this.shape.operator_multiply(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getX2());
		assertEpsilonEquals(1.414213562, s.getY2());
	}

	@Override
	public void operator_andPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createPoint(0, 0)));
		assertTrue(this.shape.operator_and(createPoint(.5, .5)));
		assertTrue(this.shape.operator_and(createPoint(1, 1)));
		assertFalse(this.shape.operator_and(createPoint(2.3, 4.5)));
		assertFalse(this.shape.operator_and(createPoint(2, 2)));
	}

	@Override
	public void operator_andShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createCircle(0, 0, 1)));
		assertFalse(this.shape.operator_and(createEllipseFromCorners(5, 2, 6, .5)));
	}

	@Override
	public void operator_upToPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(0, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(.5, .5)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(1, 1)));

		assertEpsilonEquals(3.733630941, this.shape.operator_upTo(createPoint(2.3, 4.5)));

		assertEpsilonEquals(1.414213562, this.shape.operator_upTo(createPoint(2, 2)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void issue15(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Segment2afp segment = createSegment(-20, -20, 20, 20);
		Path2afp path = createPath();
		path.moveTo(5, 5);
		path.lineTo(5, -5);
		path.lineTo(-5, -5);
		path.lineTo(-5, 5);
		path.lineTo(5, 5);
		assertTrue(path.intersects(segment));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(19.22967, this.shape.getDistanceSquared(createCircle(-5, 2, 1)));
		assertEpsilonEquals(324, this.shape.getDistanceSquared(createCircle(1, 20, 1)));
		assertEpsilonEquals(1.25736, this.shape.getDistanceSquared(createCircle(-1, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(0, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(0, .6, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(0,  0,  this.shape.getClosestPointTo(createCircle(-5, 2, 1)));
		assertFpPointEquals(1,  1,  this.shape.getClosestPointTo(createCircle(1, 20, 1)));
		assertFpPointEquals(.5,  .5,  this.shape.getClosestPointTo(createCircle(-1, 2, 1)));
		assertClosestPointInBothShapes(this.shape, createCircle(0, 1, 1));
		assertClosestPointInBothShapes(this.shape, createCircle(0, .6, 1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createRectangle(-10, 5, 2, 1)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createRectangle(-.5, 5, 2, 1)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createRectangle(5, 5, 2, 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createRectangle(-10, -3, 2, 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createRectangle(-.5, -3, 2, 1)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createRectangle(5, -3, 2, 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createRectangle(-2.5, -.5, 2, 1)));
		assertClosestPointInBothShapes(this.shape, createRectangle(-1.5, -.5, 2, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(-1, .25, 5, .5));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(89, this.shape.getDistanceSquared(createRectangle(-10, 5, 2, 1)));
		assertEpsilonEquals(16, this.shape.getDistanceSquared(createRectangle(-.5, 5, 2, 1)));
		assertEpsilonEquals(32, this.shape.getDistanceSquared(createRectangle(5, 5, 2, 1)));
		assertEpsilonEquals(68, this.shape.getDistanceSquared(createRectangle(-10, -3, 2, 1)));
		assertEpsilonEquals(4, this.shape.getDistanceSquared(createRectangle(-.5, -3, 2, 1)));
		assertEpsilonEquals(25, this.shape.getDistanceSquared(createRectangle(5, -3, 2, 1)));
		assertEpsilonEquals(.25, this.shape.getDistanceSquared(createRectangle(-2.5, -.5, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(-1.5, -.5, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(-1, .25, 5, .5)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(-1, .1, 5, .5)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(-1, .4, 5, .5)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createSegment(-2, 3, -1, 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createSegment(-1, 1, -3, 2)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createSegment(1, 0, -1, 0)));
		assertClosestPointInBothShapes(this.shape, createSegment(-2, 0, 2, 1));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createSegment(0, -1, -3, 0)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createSegment(0, 4, 3, 3)));
		assertFpPointEquals(.5, .5, this.shape.getClosestPointTo(createSegment(1, 0, 4, 1)));
		assertFpPointEquals(.5, .5, this.shape.getClosestPointTo(createSegment(1, 0, 3, -1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createSegment(2, 0, 1, -1)));
		assertFpPointEquals(.7, .7, this.shape.getClosestPointTo(createSegment(3, -1, 1, .4)));
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2, this.shape.getDistanceSquared(createSegment(-2, 3, -1, 1)));
		assertEpsilonEquals(2, this.shape.getDistanceSquared(createSegment(-1, 1, -3, 2)));
		assertEpsilonEquals(.5, this.shape.getDistanceSquared(createSegment(0, 1, -1, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(-2, 0, 2, 1)));
		assertEpsilonEquals(.9, this.shape.getDistanceSquared(createSegment(0, -1, -3, 0)));
		assertEpsilonEquals(6.4, this.shape.getDistanceSquared(createSegment(0, 4, 3, 3)));
		assertEpsilonEquals(.5, this.shape.getDistanceSquared(createSegment(1, 0, 4, 1)));
		assertEpsilonEquals(.5, this.shape.getDistanceSquared(createSegment(1, 0, 3, -1)));
		assertEpsilonEquals(2, this.shape.getDistanceSquared(createSegment(2, 0, 1, -1)));
		assertEpsilonEquals(0.18, this.shape.getDistanceSquared(createSegment(3, -1, 1, .4)));
	}
	
	protected Triangle2afp createTestTriangle(double x, double y) {
		return createTriangle(x, y, x + 1, y, x, y + 0.5);
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestTriangle(-10, 5)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestTriangle(-.5, 5)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestTriangle(.5, 5)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestTriangle(5, 5)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestTriangle(-10, -5)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestTriangle(-.5, -5)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestTriangle(.5, -5)));
		assertFpPointEquals(.25, .25, this.shape.getClosestPointTo(createTestTriangle(5, -5)));
		assertClosestPointInBothShapes(this.shape, createTestTriangle(0, .5));
		assertClosestPointInBothShapes(this.shape, createTestTriangle(0, .1));
		assertFpPointEquals(.75, .75, this.shape.getClosestPointTo(createTestTriangle(1, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(106, this.shape.getDistanceSquared(createTestTriangle(-10, 5)));
		assertEpsilonEquals(16.25, this.shape.getDistanceSquared(createTestTriangle(-.5, 5)));
		assertEpsilonEquals(16, this.shape.getDistanceSquared(createTestTriangle(.5, 5)));
		assertEpsilonEquals(32, this.shape.getDistanceSquared(createTestTriangle(5, 5)));
		assertEpsilonEquals(106, this.shape.getDistanceSquared(createTestTriangle(-10, -5)));
		assertEpsilonEquals(20.5, this.shape.getDistanceSquared(createTestTriangle(-.5, -5)));
		assertEpsilonEquals(20.5, this.shape.getDistanceSquared(createTestTriangle(.5, -5)));
		assertEpsilonEquals(45.125, this.shape.getDistanceSquared(createTestTriangle(5, -5)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(0, .5)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(0, .1)));
		assertEpsilonEquals(0.125, this.shape.getDistanceSquared(createTestTriangle(1, 0)));
	}
	
	protected MultiShape2afp createTestMultiShape(double x, double y) {
		MultiShape2afp multishape = createMultiShape();
		multishape.add(createCircle(x - 5, y + 4, 1));
		multishape.add(createSegment(x + 4, y + 2, x + 8, y - 1));
		return multishape;
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToMultiShape2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		MultiShape2afp multishape;
		
		multishape = createTestMultiShape(0, 0);
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(multishape));

		multishape = createTestMultiShape(5, 5);
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(multishape));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredMultiShape2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		MultiShape2afp multishape;

		multishape = createTestMultiShape(0, 0);
		assertEpsilonEquals(10, this.shape.getDistanceSquared(multishape));

		multishape = createTestMultiShape(5, 5);
		assertEpsilonEquals(49.87548, this.shape.getDistanceSquared(multishape));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToOrientedRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-10, 5, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createOrientedRectangle(-.5, 5, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createOrientedRectangle(5, 5, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-10, -3, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-.5, -3, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createOrientedRectangle(5, -3, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-1.5, -.5, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-1, .25, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-1, .1, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-1, .4, u.getX(), u.getY(), 2, 1)));
		//
		u = createVector(-1, -2).toUnitVector();
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-10, 5, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createOrientedRectangle(-.5, 5, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createOrientedRectangle(5, 5, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-10, -3, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-.5, -3, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createOrientedRectangle(5, -3, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-1.5, -.5, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-1, .25, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-1, .1, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-1, .4, u.getX(), u.getY(), 1, 2)));
		//
		u = createVector(2, -1).toUnitVector();
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-10, 5, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createOrientedRectangle(-.5, 5, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createOrientedRectangle(5, 5, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-10, -3, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-.5, -3, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createOrientedRectangle(5, -3, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-1.5, -.5, u.getX(), u.getY(), 2, 1)));
		assertClosestPointInBothShapes(this.shape, createOrientedRectangle(-1, .25, u.getX(), u.getY(), 2, 1));
		assertClosestPointInBothShapes(this.shape, createOrientedRectangle(-1, .1, u.getX(), u.getY(), 2, 1));
		assertClosestPointInBothShapes(this.shape, createOrientedRectangle(-1, .4, u.getX(), u.getY(), 2, 1));
		//
		u = createVector(1, 2).toUnitVector();
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-10, 5, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createOrientedRectangle(-.5, 5, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createOrientedRectangle(5, 5, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-10, -3, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-.5, -3, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createOrientedRectangle(5, -3, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-1.5, -.5, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-1, .25, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-1, .1, u.getX(), u.getY(), 1, 2)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createOrientedRectangle(-1, .4, u.getX(), u.getY(), 1, 2)));
	}
		
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredOrientedRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
		assertEpsilonEquals(84.27864, this.shape.getDistanceSquared(createOrientedRectangle(-10, 5, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(4.91424, this.shape.getDistanceSquared(createOrientedRectangle(-.5, 5, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(19.06687, this.shape.getDistanceSquared(createOrientedRectangle(5, 5, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(69.27864, this.shape.getDistanceSquared(createOrientedRectangle(-10, -3, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(3.63622, this.shape.getDistanceSquared(createOrientedRectangle(-.5, -3, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(11.95604, this.shape.getDistanceSquared(createOrientedRectangle(5, -3, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(.013932, this.shape.getDistanceSquared(createOrientedRectangle(-1.5, -.5, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createOrientedRectangle(-1, .25, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createOrientedRectangle(-1, .1, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createOrientedRectangle(-1, .4, u.getX(), u.getY(), 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createRoundRectangle(-10, 5, 2, 1, .2, .1)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createRoundRectangle(-.5, 5, 2, 1, .2, .1)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createRoundRectangle(5, 5, 2, 1, .2, .1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createRoundRectangle(-10, -3, 2, 1, .2, .1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createRoundRectangle(-.5, -3, 2, 1, .2, .1)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createRoundRectangle(5, -3, 2, 1, .2, .1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createRoundRectangle(-2.5, -.5, 2, 1, .2, .1)));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(-1.5, -.5, 2, 1, .2, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(-1, .25, 5, .5, .2, .1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(89.85237, this.shape.getDistanceSquared(createRoundRectangle(-10, 5, 2, 1, .2, .1)));
		assertEpsilonEquals(16, this.shape.getDistanceSquared(createRoundRectangle(-.5, 5, 2, 1, .2, .1)));
		assertEpsilonEquals(32.61464, this.shape.getDistanceSquared(createRoundRectangle(5, 5, 2, 1, .2, .1)));
		assertEpsilonEquals(68.38273, this.shape.getDistanceSquared(createRoundRectangle(-10, -3, 2, 1, .2, .1)));
		assertEpsilonEquals(4, this.shape.getDistanceSquared(createRoundRectangle(-.5, -3, 2, 1, .2, .1)));
		assertEpsilonEquals(25.49554, this.shape.getDistanceSquared(createRoundRectangle(5, -3, 2, 1, .2, .1)));
		assertEpsilonEquals(.25, this.shape.getDistanceSquared(createRoundRectangle(-2.5, -.5, 2, 1, .2, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(-1.5, -.5, 2, 1, .2, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(-1, .25, 5, .5, .2, .1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createEllipse(-10, 5, 2, 1)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createEllipse(5, 5, 2, 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createEllipse(-10, -5, 2, 1)));
		assertFpPointEquals(0.3866, 0.3866, this.shape.getClosestPointTo(createEllipse(5, -5, 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(93.35943, this.shape.getDistanceSquared(createEllipse(-10, 5, 2, 1)));
		assertEpsilonEquals(35.14211, this.shape.getDistanceSquared(createEllipse(5, 5, 2, 1)));
		assertEpsilonEquals(83.64829, this.shape.getDistanceSquared(createEllipse(-10, -5, 2, 1)));
		assertEpsilonEquals(44.0121, this.shape.getDistanceSquared(createEllipse(5, -5, 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToParallelogram2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
		Vector2D<?, ?> v = createVector(2, -3).toUnitVector();
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createParallelogram(-10, 5, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createParallelogram(5, 5, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createParallelogram(-10, -3, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertFpPointEquals(.69146, .69146, this.shape.getClosestPointTo(createParallelogram(5, -3, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredParallelogram2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D<?, ?> u = createVector(-2, 1).toUnitVector();
		Vector2D<?, ?> v = createVector(2, -3).toUnitVector();
		assertEpsilonEquals(69.33711, this.shape.getDistanceSquared(createParallelogram(-10, 5, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertEpsilonEquals(24.15281, this.shape.getDistanceSquared(createParallelogram(5, 5, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertEpsilonEquals(80.96075, this.shape.getDistanceSquared(createParallelogram(-10, -3, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertEpsilonEquals(7.72232, this.shape.getDistanceSquared(createParallelogram(5, -3, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
	}

	protected Path2afp createTestPath(double dx, double dy, boolean close) {
		Path2afp path = createPath();
		path.moveTo(dx + 3, dy);
		path.lineTo(dx + 1, dy + 3);
		path.lineTo(dx - 2, dy + 1);
		path.lineTo(dx - 1, dy - 1);
		if (close) {
			path.closePath();
		}
		return path;
	}
	
	protected Path2afp createTestPath(double dx, double dy, boolean close, PathWindingRule rule) {
		Path2afp path = createPath(rule);
		path.moveTo(dx + 3, dy - 1);
		path.lineTo(dx + 1, dy + 2);
		path.lineTo(dx - 1, dy + 1);
		path.lineTo(dx, dy - 1);
		path.lineTo(dx + 3, dy + 2);
		path.lineTo(dx, dy + 3);
		path.lineTo(dx - 2, dy + 1);
		path.lineTo(dx - 1, dy - 2);
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
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestPath(0, 0, false)));
		assertClosestPointInBothShapes(this.shape, createTestPath(0, 0, true));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestPath(.5, 0, false)));
		assertClosestPointInBothShapes(this.shape, createTestPath(.5, 0, true));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestPath(-.5, 0, false)));
		assertClosestPointInBothShapes(this.shape, createTestPath(-.5, 0, true));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestPath(0, .5, false)));
		assertClosestPointInBothShapes(this.shape, createTestPath(0, .5, true));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestPath(0, -.5, false)));
		assertClosestPointInBothShapes(this.shape, createTestPath(0, -.5, true));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestPath(.5, -.5, false)));
		assertClosestPointInBothShapes(this.shape, createTestPath(.5, -.5, true));
		//
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestPath(0, 0, false, PathWindingRule.EVEN_ODD)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestPath(0, 0, true, PathWindingRule.EVEN_ODD)));
		assertClosestPointInBothShapes(this.shape, createTestPath(.5, 0, false, PathWindingRule.EVEN_ODD));
		assertClosestPointInBothShapes(this.shape, createTestPath(.5, 0, true, PathWindingRule.EVEN_ODD));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestPath(-.5, 0, false, PathWindingRule.EVEN_ODD)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestPath(-.5, 0, true, PathWindingRule.EVEN_ODD)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestPath(0, .5, false, PathWindingRule.EVEN_ODD)));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestPath(0, .5, true, PathWindingRule.EVEN_ODD)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestPath(0, -.5, false, PathWindingRule.EVEN_ODD)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestPath(0, -.5, true, PathWindingRule.EVEN_ODD)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestPath(.5, -.5, false, PathWindingRule.EVEN_ODD)));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestPath(.5, -.5, true, PathWindingRule.EVEN_ODD)));
		//
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestPath(0, 0, false, PathWindingRule.NON_ZERO)));
		assertClosestPointInBothShapes(this.shape, createTestPath(0, 0, true, PathWindingRule.NON_ZERO));
		assertClosestPointInBothShapes(this.shape, createTestPath(.5, 0, false, PathWindingRule.NON_ZERO));
		assertClosestPointInBothShapes(this.shape, createTestPath(.5, 0, true, PathWindingRule.NON_ZERO));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestPath(-.5, 0, false, PathWindingRule.NON_ZERO)));
		assertClosestPointInBothShapes(this.shape, createTestPath(-.5, 0, true, PathWindingRule.NON_ZERO));
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestPath(0, .5, false, PathWindingRule.NON_ZERO)));
		assertClosestPointInBothShapes(this.shape, createTestPath(0, .5, true, PathWindingRule.NON_ZERO));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestPath(0, -.5, false, PathWindingRule.NON_ZERO)));
		assertClosestPointInBothShapes(this.shape, createTestPath(0, -.5, true, PathWindingRule.NON_ZERO));
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createTestPath(.5, -.5, false, PathWindingRule.NON_ZERO)));
		assertClosestPointInBothShapes(this.shape, createTestPath(.5, -.5, true, PathWindingRule.NON_ZERO));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredPath2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1.23077, this.shape.getDistanceSquared(createTestPath(0, 0, false)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(0, 0, true)));
		assertEpsilonEquals(.8, this.shape.getDistanceSquared(createTestPath(.5, 0, false)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(.5, 0, true)));
		assertEpsilonEquals(.48077, this.shape.getDistanceSquared(createTestPath(-.5, 0, false)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(-.5, 0, true)));
		assertEpsilonEquals(1.25, this.shape.getDistanceSquared(createTestPath(0, .5, false)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(0, .5, true)));
		assertEpsilonEquals(.69231, this.shape.getDistanceSquared(createTestPath(0, -.5, false)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(0, -.5, true)));
		assertEpsilonEquals(.94231, this.shape.getDistanceSquared(createTestPath(.5, -.5, false)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(.5, -.5, true)));
		//
		assertEpsilonEquals(.2, this.shape.getDistanceSquared(createTestPath(0, 0, false, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(.2, this.shape.getDistanceSquared(createTestPath(0, 0, true, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(.5, 0, false, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(.5, 0, true, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(.019231, this.shape.getDistanceSquared(createTestPath(-.5, 0, false, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(.019231, this.shape.getDistanceSquared(createTestPath(-.5, 0, true, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(.05, this.shape.getDistanceSquared(createTestPath(0, .5, false, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(.05, this.shape.getDistanceSquared(createTestPath(0, .5, true, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(.076923, this.shape.getDistanceSquared(createTestPath(0, -.5, false, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(.076923, this.shape.getDistanceSquared(createTestPath(0, -.5, true, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(.05, this.shape.getDistanceSquared(createTestPath(.5, -.5, false, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(.05, this.shape.getDistanceSquared(createTestPath(.5, -.5, true, PathWindingRule.EVEN_ODD)));
		//
		assertEpsilonEquals(.2, this.shape.getDistanceSquared(createTestPath(0, 0, false, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(0, 0, true, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(.5, 0, false, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(.5, 0, true, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(.019231, this.shape.getDistanceSquared(createTestPath(-.5, 0, false, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(-.5, 0, true, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(.05, this.shape.getDistanceSquared(createTestPath(0, .5, false, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(0, .5, true, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(.076923, this.shape.getDistanceSquared(createTestPath(0, -.5, false, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(0, -.5, true, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(.05, this.shape.getDistanceSquared(createTestPath(.5, -.5, false, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(.5, -.5, true, PathWindingRule.NON_ZERO)));
	}

}