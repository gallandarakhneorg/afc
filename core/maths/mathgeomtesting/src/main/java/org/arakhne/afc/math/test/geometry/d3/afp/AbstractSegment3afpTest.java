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

package org.arakhne.afc.math.test.geometry.d3.afp;

import static org.arakhne.afc.math.geometry.GeomConstants.SHAPE_INTERSECTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;
import org.arakhne.afc.math.geometry.d3.afp.RectangularPrism3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp.UncertainIntersection;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;

@SuppressWarnings("all")
public abstract class AbstractSegment3afpTest<T extends Segment3afp<?, T, ?, ?, ?, B>,
			B extends RectangularPrism3afp<?, ?, ?, ?, ?, B>> extends AbstractShape3afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createSegment(0, 0, 0, 1, 1, 1);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticCcw(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double x1 = -100;
		double y1 = -100;
		double z1 = -100;
		double x2 = 100;
		double y2 = 100;
		double z2 = 100;

		assertEquals(0, Segment3afp.ccw(x1, y1, z1, x2, y2, z2, x1, y1, z1, 0));
		assertEquals(0, Segment3afp.ccw(x2, y2, z2, x1, y1, z1, x1, y1, z1, 0));

		assertEquals(0, Segment3afp.ccw(x1, y1, z1, x2, y2, z2, x2, y2, z2, 0));
		assertEquals(0, Segment3afp.ccw(x2, y2, z2, x1, y1, z1, x2, y2, z2, 0));

		assertEquals(0, Segment3afp.ccw(x1, y1, z1, x2, y2, z2, 0, 0, 0, 0));
		assertEquals(0, Segment3afp.ccw(x2, y2, z2, x1, y1, z1, 0, 0, 0, 0));

		assertEquals(-1, Segment3afp.ccw(x1, y1, z1, x2, y2, z2, -200, -200, -200, 0));
		assertEquals(1, Segment3afp.ccw(x2, y2, z2, x1, y1, z1, -200, -200, -200, 0));

		assertEquals(1, Segment3afp.ccw(x1, y1, z1, x2, y2, z2, 200, 200, 200, 0));
		assertEquals(-1, Segment3afp.ccw(x2, y2, z2, x1, y1, z1, 200, 200, 200, 0));

		assertEquals(-1, Segment3afp.ccw(x1, y1, z1, x2, y2, z2, -200, 200, 200, 0));
		assertEquals(1, Segment3afp.ccw(x2, y2, z2, x1, y1, z1, -200, 200, 200, 0));

		assertEquals(1, Segment3afp.ccw(x1, y1, z1, x2, y2, z2, 200, -200, -200, 0));
		assertEquals(-1, Segment3afp.ccw(x2, y2, z2, x1, y1, z1, 200, -200, -200, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticComputeClosestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.computeClosestPointToPoint(0, 0, 0, 1, 1, 1, 0, 0, 0, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
		assertEpsilonEquals(0, result.getZ());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.computeClosestPointToPoint(0, 0, 0, 1, 1, 1, .75, .75, .75, result);
		assertEpsilonEquals(.75, result.getX());
		assertEpsilonEquals(.75, result.getY());
		assertEpsilonEquals(.75, result.getZ());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.computeClosestPointToPoint(0, 0, 0, 1, 1, 1, -10, -50, 0, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
		assertEpsilonEquals(0, result.getZ());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.computeClosestPointToPoint(0, 0, 0, 1, 1, 1, 200, -50, 0, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(1, result.getY());
		assertEpsilonEquals(1, result.getZ());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.computeClosestPointToPoint(0, 0, 0, 1, 1, 1, 0, 1, 0, result);
		assertEpsilonEquals(.5, result.getX());
		assertEpsilonEquals(.5, result.getY());
		assertEpsilonEquals(.5, result.getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticComputeCrossingsFromCircle(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				5, -4, 0, -1, -5, 0));
		assertEquals(0, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				-7, -3, 0, -5, -1, 0));
		assertEquals(0, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				11, -2, 0, 10, -1, 0));
		assertEquals(0, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				3, 5, 0, 1, 6, 0));
		assertEquals(-1, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				5, .5, 0, 6, -1, 0));
		assertEquals(-2, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				5, 2, 0, 6, -1, 0));
		assertEquals(-1, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				5, 2, 0, 6, .5, 0));
		assertEquals(SHAPE_INTERSECTS, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				.5, .5, 0, 3, 0, 0));
		assertEquals(SHAPE_INTERSECTS, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				0, 2, 0, 3, 0, 0));
		assertEquals(SHAPE_INTERSECTS, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				.5, 4, 0, .5, -1, 0));
		assertEquals(-2, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				1, 3, 0, 3, 0, 0));

		assertEquals(0, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				-1, -5, 0, 5, -4, 0));
		assertEquals(0, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				-5, -1, 0, -7, 3, 0));
		assertEquals(0, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				10, -1, 0, 11, -2, 0));
		assertEquals(0, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				1, 6, 0, 3, 5, 0));
		assertEquals(1, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				6, -1, 0, 5, .5, 0));
		assertEquals(2, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				6, -1, 0, 5, 2, 0));
		assertEquals(1, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				6, .5, 0, 5, 2, 0));
		assertEquals(SHAPE_INTERSECTS, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				3, 0, 0, .5, .5, 0));
		assertEquals(SHAPE_INTERSECTS, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				3, 0, 0, 0, 2, 0));
		assertEquals(SHAPE_INTERSECTS, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				.5, -1, 0, .5, 4, 0));
		assertEquals(2, Segment3afp.computeCrossingsFromSphere(
				0,
				1, 1, 1, 1,
				3, 0, 0, 1, 3, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeCrossingsFromPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(
				1,
				Segment3afp.computeCrossingsFromPoint(
						0, 0, 0,
						10, -1, 0, 10, 1, 0));
		assertEquals(
				0,
				Segment3afp.computeCrossingsFromPoint(
						0, 0, 0,
						10, -1, 0, 10, -.5, 0));
		assertEquals(
				0,
				Segment3afp.computeCrossingsFromPoint(
						0, 0, 0,
						-10, -1, 0, -10, 1, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeCrossingsFromPointWithoutEquality(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(
				1,
				Segment3afp.computeCrossingsFromPointWithoutEquality(
						0, 0, 0,
						10, -1, 0, 10, 1, 0));
		assertEquals(
				0,
				Segment3afp.computeCrossingsFromPointWithoutEquality(
						0, 0, 0,
						10, -1, 0, 10, -.5, 0));
		assertEquals(
				0,
				Segment3afp.computeCrossingsFromPointWithoutEquality(
						0, 0, 0,
						-10, -1, 0, -10, 1, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticComputeCrossingsFromRect(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(
				2,
				Segment3afp.computeCrossingsFromRect(
						0,
						0, 0, 0, 1, 1, 0,
						10, -5, 0, 10, 5, 0));
		assertEquals(
				1,
				Segment3afp.computeCrossingsFromRect(
						0,
						0, 0, 0, 1, 1, 0,
						10, -5, 0, 10, .5, 0));
		assertEquals(
				0,
				Segment3afp.computeCrossingsFromRect(
						0,
						0, 0, 0, 1, 1, 0,
						10, -5, 0, 10, -1, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeCrossingsFromSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		// 0011
		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						10, -5, 0, 10, -4, 0));
		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						10, 5, 0, 10, 4, 0));
		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						-5, .5, 0, 0, .6, 0));

		assertEquals(
				1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						10, -1, 0, 11, .6, 0));
		assertEquals(
				2,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						10, -1, 0, 11, 2, 0));
		assertEquals(
				1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						10, .5, 0, 11, 2, 0));

		assertEquals(
				-1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						10, 2, 0, 11, .6, 0));
		assertEquals(
				-2,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						10, 2, 0, 11, -1, 0));
		assertEquals(
				-1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						10, .6, 0, 11, -1, 0));

		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						0, .5, 0, .25, .5, 0));

		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						.75, .5, 0, 1, .5, 0));

		assertEquals(
				1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						5, -5, 0, .75, .5, 0));

		assertEquals(
				SHAPE_INTERSECTS,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						5, -5, 0, 0, 1, 0));

		assertEquals(
				2,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						5, -5, 0, 1, 1, 0));

		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 0, 0, 1, 1, 0,
						-2, 1, 0, 5, -5, 0));

		// 1100

		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						10, -5, 0, 10, -4, 0));
		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						10, 5, 0, 10, 4, 0));
		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						-5, .5, 0, 0, .6, 0));

		assertEquals(
				1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						10, -1, 0, 11, .6, 0));
		assertEquals(
				2,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						10, -1, 0, 11, 2, 0));
		assertEquals(
				1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						10, .5, 0, 11, 2, 0));

		assertEquals(
				-1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						10, 2, 0, 11, .6, 0));
		assertEquals(
				-2,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						10, 2, 0, 11, -1, 0));
		assertEquals(
				-1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						10, .6, 0, 11, -1, 0));

		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						0, .5, 0, .25, .5, 0));

		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						.75, .5, 0, 1, .5, 0));

		assertEquals(
				1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						5, -5, 0, .75, .5, 0));

		assertEquals(
				SHAPE_INTERSECTS,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						5, -5, 0, 0, 1, 0));

		assertEquals(
				2,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						5, -5, 0, 1, 1, 0));

		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 0, 0, 0,
						-2, 1, 0, 5, -5, 0));

		// 0110

		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 1, 0, 1, 0, 0,
						10, -5, 0, 10, -4, 0));
		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 1, 0, 1, 0, 0,
						10, 5, 0, 10, 4, 0));
		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 1, 0, 1, 0, 0,
						-5, .5, 0, 0, .6, 0));

		assertEquals(
				1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 1, 0, 1, 0, 0,
						10, -1, 0, 11, .6, 0));
		assertEquals(
				2,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 1, 0, 1, 0, 0,
						10, -1, 0, 11, 2, 0));
		assertEquals(
				1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 1, 0, 1, 0, 0,
						10, .5, 0, 11, 2, 0));

		assertEquals(
				-1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 1, 0, 1, 0, 0,
						10, 2, 0, 11, .6, 0));
		assertEquals(
				-2,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 1, 0, 1, 0, 0,
						10, 2, 0, 11, -1, 0));
		assertEquals(
				-1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 1, 0, 1, 0, 0,
						10, .6, 0, 11, -1, 0));

		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 1, 0, 1, 0, 0,
						0, .5, 0, .25, .5, 0));

		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 1, 0, 1, 0, 0,
						.75, .5, 0, 1, .5, 0));

		assertEquals(
				1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 1, 0, 1, 0, 0,
						5, -.01, 0, .75, .5, 0));

		assertEquals(
				SHAPE_INTERSECTS,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 1, 0, 1, 0, 0,
						20, -5, 0, -1, 1, 0));

		assertEquals(
				SHAPE_INTERSECTS,
				Segment3afp.computeCrossingsFromSegment(
						0,
						0, 1, 0, 1, 0, 0,
						5, 10, 0, .25, .5, 0));

		// 1001

		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 0, 0, 0, 1, 0,
						10, -5, 0, 10, -4, 0));
		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 0, 0, 0, 1, 0,
						10, 5, 0, 10, 4, 0));
		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 0, 0, 0, 1, 0,
						-5, .5, 0, 0, .6, 0));

		assertEquals(
				1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 0, 0, 0, 1, 0,
						10, -1, 0, 11, .6, 0));
		assertEquals(
				2,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 0, 0, 0, 1, 0,
						10, -1, 0, 11, 2, 0));
		assertEquals(
				1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 0, 0, 0, 1, 0,
						10, .5, 0, 11, 2, 0));

		assertEquals(
				-1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 0, 0, 0, 1, 0,
						10, 2, 0, 11, .6, 0));
		assertEquals(
				-2,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 0, 0, 0, 1, 0,
						10, 2, 0, 11, -1, 0));
		assertEquals(
				-1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 0, 0, 0, 1, 0,
						10, .6, 0, 11, -1, 0));

		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 0, 0, 0, 1, 0,
						0, .5, 0, .25, .5, 0));

		assertEquals(
				0,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 0, 0, 0, 1, 0,
						.75, .5, 0, 1, .5, 0));

		assertEquals(
				1,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 0, 0, 0, 1, 0,
						20, -5, 0, .75, .5, 0));

		assertEquals(
				SHAPE_INTERSECTS,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 0, 0, 0, 1, 0,
						20, -5, 0, 0, 1, 0));

		assertEquals(
				SHAPE_INTERSECTS,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 0, 0, 0, 1, 0,
						5, 10, 0, .25, .5, 0));

		// Others

		assertEquals(
				SHAPE_INTERSECTS,
				Segment3afp.computeCrossingsFromSegment(
						0,
						7, -5, 0, 1, 1, 0,
						4, -3, 0, 1, 1, 0));
		assertEquals(
				SHAPE_INTERSECTS,
				Segment3afp.computeCrossingsFromSegment(
						0,
						4, -3, 0, 1, 1, 0,
						7, -5, 0, 1, 1, 0));
		assertEquals(
				SHAPE_INTERSECTS,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 4, -3, 0,
						7, -5, 0, 1, 1, 0));
		assertEquals(
				SHAPE_INTERSECTS,
				Segment3afp.computeCrossingsFromSegment(
						0,
						4, -3, 0, 1, 1, 0,
						1, 1, 0, 7, -5, 0));
		assertEquals(
				SHAPE_INTERSECTS,
				Segment3afp.computeCrossingsFromSegment(
						0,
						1, 1, 0, 4, -3, 0,
						1, 1, 0, 7, -5, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.computeFarthestPointTo(0, 0, 0, 1, 1, 0, 0, 0, 0, p);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.computeFarthestPointTo(0, 0, 0, 1, 1, 0, .5, .5, 0, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.computeFarthestPointTo(0, 0, 0, 1, 1, 0, 1, 1, 0, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.computeFarthestPointTo(0, 0, 0, 1, 1, 0, 2, 2, 0, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.computeFarthestPointTo(0, 0, 0, 1, 1, 0, -2, 2, 0, p);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.computeFarthestPointTo(0, 0, 0, 1, 1, 0, 0.1, 1.2, 0, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.computeFarthestPointTo(0, 0, 0, 1, 1, 0, 10.1, -.2, 0, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeLineLineIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(Segment3afp.computeLineLineIntersection(
				1000, 1.5325000286102295, 0, 2500, 1.5325000286102295, 0,
				1184.001080023255, 1.6651813832907332, 0, 1200.7014393876193, 1.372326130924099, 0,
				result));
		assertEpsilonEquals(1191.567365026541, result.getX());
		assertEpsilonEquals(1.532500028610229, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(Segment3afp.computeLineLineIntersection(
				100, 50, 0, 100, 60, 0,
				90, 55, 0, 2000, 55, 0,
				result));
		assertEpsilonEquals(100, result.getX());
		assertEpsilonEquals(55, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertFalse(Segment3afp.computeLineLineIntersection(
				100, 50, 0, 100, 60, 0,
				200, 0, 0, 200, 10, 0,
				result));
		assertNaN(result.getX());
		assertNaN(result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(Segment3afp.computeLineLineIntersection(
				100, -50, 0, 100, -60, 0,
				90, 55, 0, 2000, 55, 0,
				result));
		assertEpsilonEquals(100, result.getX());
		assertEpsilonEquals(55, result.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeLineLineIntersectionFactor(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.1277115766843605, Segment3afp.computeLineLineIntersectionFactor(
				1000, 1.5325000286102295, 0, 2500, 1.5325000286102295, 0,
				1184.001080023255, 1.6651813832907332, 0, 1200.7014393876193, 1.372326130924099, 0));

		assertEpsilonEquals(.5, Segment3afp.computeLineLineIntersectionFactor(
				100, 50, 0, 100, 60, 0,
				90, 55, 0, 2000, 55, 0));

		assertNaN(Segment3afp.computeLineLineIntersectionFactor(
				100, 50, 0, 100, 60, 0,
				200, 0, 0, 200, 10, 0));

		assertEpsilonEquals(-10.5, Segment3afp.computeLineLineIntersectionFactor(
				100, -50, 0, 100, -60, 0,
				90, 55, 0, 2000, 55, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeProjectedPointOnLine(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.3076923076923077, Segment3afp.computeProjectedPointOnLine(
				2, 1, 0,
				0, 0, 0, 3, -2, 0));

		assertEpsilonEquals(.6666666666666666, Segment3afp.computeProjectedPointOnLine(
				2, 1, 0,
				0, 0, 0, 3, 0, 0));

		assertEpsilonEquals(-.7, Segment3afp.computeProjectedPointOnLine(
				2, -1, 0,
				0, 0, 0, -3, 1, 0));

		assertEpsilonEquals(14.4, Segment3afp.computeProjectedPointOnLine(
				2, 150, 0,
				0, 0, 0, -3, 1, 0));

		assertEpsilonEquals(.5, Segment3afp.computeProjectedPointOnLine(
				.5, .5, 0,
				0, 0, 0, 1, 1, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeRelativeDistanceLinePoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-1.941450686788302, Segment3afp.computeRelativeDistanceLinePoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 0));
		assertEpsilonEquals(1.941450686788302, Segment3afp.computeRelativeDistanceLinePoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(-1, Segment3afp.computeRelativeDistanceLinePoint(
				0, 0, 0, 3, 0, 0,
				2, 1, 0));
		assertEpsilonEquals(1, Segment3afp.computeRelativeDistanceLinePoint(
				3, 0, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(-.3162277660168379, Segment3afp.computeRelativeDistanceLinePoint(
				0, 0, 0, -3, 1, 0,
				2, -1, 0));
		assertEpsilonEquals(.3162277660168379, Segment3afp.computeRelativeDistanceLinePoint(
				-3, 1, 0, 0, 0, 0,
				2, -1, 0));

		assertEpsilonEquals(142.9349502396107, Segment3afp.computeRelativeDistanceLinePoint(
				0, 0, 0, -3, 1, 0,
				2, 150, 0));
		assertEpsilonEquals(-142.9349502396107, Segment3afp.computeRelativeDistanceLinePoint(
				-3, 1, 0, 0, 0, 0,
				2, 150, 0));

		assertEpsilonEquals(0, Segment3afp.computeRelativeDistanceLinePoint(
				0, 0, 0, 1, 1, 0,
				.5, .5, 0));
		assertEpsilonEquals(0, Segment3afp.computeRelativeDistanceLinePoint(
				1, 1, 0, 0, 0, 0,
				.5, .5, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticComputeSegmentSegmentIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(Segment3afp.computeSegmentSegmentIntersection(
				1000, 1.5325000286102295, 0, 2500, 1.5325000286102295, 0,
				1184.001080023255, 1.6651813832907332, 0, 1200.7014393876193, 1.372326130924099, 0,
				result));
		assertEpsilonEquals(1191.567365026541, result.getX());
		assertEpsilonEquals(1.532500028610229, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(Segment3afp.computeSegmentSegmentIntersection(
				100, 50, 0, 100, 60, 0,
				90, 55, 0, 2000, 55, 0,
				result));
		assertEpsilonEquals(100, result.getX());
		assertEpsilonEquals(55, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertFalse(Segment3afp.computeSegmentSegmentIntersection(
				100, 50, 0, 100, 60, 0,
				200, 0, 0, 200, 10, 0,
				result));
		assertNaN(result.getX());
		assertNaN(result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertFalse(Segment3afp.computeSegmentSegmentIntersection(
				-100, 50, 0, -100, 60, 0,
				90, 55, 0, 2000, 55, 0,
				result));
		assertNaN(result.getX());
		assertNaN(result.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticComputeSegmentSegmentIntersectionFactor(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.1277115766843605, Segment3afp.computeSegmentSegmentIntersectionFactor(
				1000, 1.5325000286102295, 0, 2500, 1.5325000286102295, 0,
				1184.001080023255, 1.6651813832907332, 0, 1200.7014393876193, 1.372326130924099, 0));

		assertEpsilonEquals(.5, Segment3afp.computeSegmentSegmentIntersectionFactor(
				100, 50, 0, 100, 60, 0,
				90, 55, 0, 2000, 55, 0));

		assertNaN(Segment3afp.computeSegmentSegmentIntersectionFactor(
				100, 50, 0, 100, 60, 0,
				200, 0, 0, 200, 10, 0));

		assertNaN(Segment3afp.computeSegmentSegmentIntersectionFactor(
				100, -50, 0, 100, -60, 0,
				90, 55, 0, 2000, 55, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeSideLinePoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Segment3afp.computeSideLinePoint(0, 0, 0, 1, 1, 0, 0, 0, 0, 0.1));
		assertEquals(0, Segment3afp.computeSideLinePoint(0, 0, 0, 1, 1, 0, 1, 1, 0, 0.1));
		assertEquals(0, Segment3afp.computeSideLinePoint(0, 0, 0, 1, 1, 0, .25, .25, 0, 0.1));
		assertEquals(1, Segment3afp.computeSideLinePoint(0, 0, 0, 1, 1, 0, 0.2, 0, 0, 0.1));
		assertEquals(1, Segment3afp.computeSideLinePoint(0, 0, 0, 1, 1, 0, 120, 0, 0, 0.1));
		assertEquals(0, Segment3afp.computeSideLinePoint(0, 0, 0, 1, 1, 0, -20.05, -20, 0, 0.1));
		assertEquals(-1, Segment3afp.computeSideLinePoint(0, 0, 0, 1, 1, 0, 0, 0.2, 0, 0.1));
		assertEquals(-1, Segment3afp.computeSideLinePoint(0, 0, 0, 1, 1, 0, 0, 120, 0, 0.1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticGetNoSegmentSegmentWithEndsIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithEndsIntersection(
				0, 0, 0, 1, 1, 0,
				0, .5, 0, 1, .5, 0));
		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithEndsIntersection(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 1, 1, 0));
		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithEndsIntersection(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 2, 2, 0));
		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithEndsIntersection(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, .5, .5, 0));
		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithEndsIntersection(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, .5, .5, 0));
		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithEndsIntersection(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, 0, 0, 0));
		assertSame(UncertainIntersection.NO, Segment3afp.getNoSegmentSegmentWithEndsIntersection(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, -1, -1, 0));

		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithEndsIntersection(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, 4, 0, 0));

		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithEndsIntersection(
				0, 0, 0, 1, 1, 0,
				-3, -1, 0, 4, -1, 0));

		assertSame(UncertainIntersection.NO, Segment3afp.getNoSegmentSegmentWithEndsIntersection(
				0, 0, 0, 1, 1, 0,
				-3, -1, 0, -1, -1, 0));

		assertSame(UncertainIntersection.NO, Segment3afp.getNoSegmentSegmentWithEndsIntersection(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, -2, 1, 0));

		assertSame(UncertainIntersection.NO, Segment3afp.getNoSegmentSegmentWithEndsIntersection(
				0, 0, 0, 1, 1, 0,
				10, 0, 0, 9, -1, 0));

		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithEndsIntersection(
				7, -5, 0, 1, 1, 0,
				4, -3, 0, 1, 1, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticGetNoSegmentSegmentWithoutEndsIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithoutEndsIntersection(
				0, 0, 0, 1, 1, 0,
				0, .5, 0, 1, .5, 0));
		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithoutEndsIntersection(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 1, 1, 0));
		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithEndsIntersection(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 2, 2, 0));
		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithoutEndsIntersection(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, .5, .5, 0));
		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithoutEndsIntersection(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, .5, .5, 0));
		assertSame(UncertainIntersection.NO, Segment3afp.getNoSegmentSegmentWithoutEndsIntersection(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, 0, 0, 0));
		assertSame(UncertainIntersection.NO, Segment3afp.getNoSegmentSegmentWithoutEndsIntersection(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, -1, -1, 0));

		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithoutEndsIntersection(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, 4, 0, 0));

		assertSame(UncertainIntersection.PERHAPS, Segment3afp.getNoSegmentSegmentWithoutEndsIntersection(
				0, 0, 0, 1, 1, 0,
				-3, -1, 0, 4, -1, 0));

		assertSame(UncertainIntersection.NO, Segment3afp.getNoSegmentSegmentWithoutEndsIntersection(
				0, 0, 0, 1, 1, 0,
				-3, -1, 0, -1, -1, 0));

		assertSame(UncertainIntersection.NO, Segment3afp.getNoSegmentSegmentWithoutEndsIntersection(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, -2, 1, 0));

		assertSame(UncertainIntersection.NO, Segment3afp.getNoSegmentSegmentWithoutEndsIntersection(
				0, 0, 0, 1, 1, 0,
				10, 0, 0, 9, -1, 0));

		assertSame(UncertainIntersection.NO, Segment3afp.getNoSegmentSegmentWithoutEndsIntersection(
				7, -5, 0, 1, 1, 0,
				4, -3, 0, 1, 1, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeDistanceLinePoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1.941450686788302, Segment3afp.computeDistanceLinePoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 0));
		assertEpsilonEquals(1.941450686788302, Segment3afp.computeDistanceLinePoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(1, Segment3afp.computeDistanceLinePoint(
				0, 0, 0, 3, 0, 0,
				2, 1, 0));
		assertEpsilonEquals(1, Segment3afp.computeDistanceLinePoint(
				3, 0, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(.3162277660168379, Segment3afp.computeDistanceLinePoint(
				0, 0, 0, -3, 1, 0,
				2, -1, 0));
		assertEpsilonEquals(.3162277660168379, Segment3afp.computeDistanceLinePoint(
				-3, 1, 0, 0, 0, 0,
				2, -1, 0));

		assertEpsilonEquals(142.9349502396107, Segment3afp.computeDistanceLinePoint(
				0, 0, 0, -3, 1, 0,
				2, 150, 0));
		assertEpsilonEquals(142.9349502396107, Segment3afp.computeDistanceLinePoint(
				-3, 1, 0, 0, 0, 0,
				2, 150, 0));

		assertEpsilonEquals(0, Segment3afp.computeDistanceLinePoint(
				0, 0, 0, 1, 1, 0,
				.5, .5, 0));
		assertEpsilonEquals(0, Segment3afp.computeDistanceLinePoint(
				1, 1, 0, 0, 0, 0,
				.5, .5, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeDistanceSegmentPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1.941450686788302, Segment3afp.computeDistanceSegmentPoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 0));
		assertEpsilonEquals(1.941450686788302, Segment3afp.computeDistanceSegmentPoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(1, Segment3afp.computeDistanceSegmentPoint(
				0, 0, 0, 3, 0, 0,
				2, 1, 0));
		assertEpsilonEquals(1, Segment3afp.computeDistanceSegmentPoint(
				3, 0, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(2.23606797749979, Segment3afp.computeDistanceSegmentPoint(
				0, 0, 0, -3, 1, 0,
				2, -1, 0));
		assertEpsilonEquals(2.23606797749979, Segment3afp.computeDistanceSegmentPoint(
				-3, 1, 0, 0, 0, 0,
				2, -1, 0));

		assertEpsilonEquals(149.0838690133845, Segment3afp.computeDistanceSegmentPoint(
				0, 0, 0, -3, 1, 0,
				2, 150, 0));
		assertEpsilonEquals(149.0838690133845, Segment3afp.computeDistanceSegmentPoint(
				-3, 1, 0, 0, 0, 0,
				2, 150, 0));

		assertEpsilonEquals(0, Segment3afp.computeDistanceSegmentPoint(
				0, 0, 0, 1, 1, 0,
				.5, .5, 0));
		assertEpsilonEquals(0, Segment3afp.computeDistanceSegmentPoint(
				1, 1, 0, 0, 0, 0,
				.5, .5, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeDistanceSquaredLinePoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(3.769230769230769, Segment3afp.computeDistanceSquaredLinePoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 0));
		assertEpsilonEquals(3.769230769230769, Segment3afp.computeDistanceSquaredLinePoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(1, Segment3afp.computeDistanceSquaredLinePoint(
				0, 0, 0, 3, 0, 0,
				2, 1, 0));
		assertEpsilonEquals(1, Segment3afp.computeDistanceSquaredLinePoint(
				3, 0, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(.09999999999999996, Segment3afp.computeDistanceSquaredLinePoint(
				0, 0, 0, -3, 1, 0,
				2, -1, 0));
		assertEpsilonEquals(.09999999999999996, Segment3afp.computeDistanceSquaredLinePoint(
				-3, 1, 0, 0, 0, 0,
				2, -1, 0));

		assertEpsilonEquals(20430.39999999979, Segment3afp.computeDistanceSquaredLinePoint(
				0, 0, 0, -3, 1, 0,
				2, 150, 0));
		assertEpsilonEquals(20430.39999999979, Segment3afp.computeDistanceSquaredLinePoint(
				-3, 1, 0, 0, 0, 0,
				2, 150, 0));

		assertEpsilonEquals(0, Segment3afp.computeDistanceSquaredLinePoint(
				0, 0, 0, 1, 1, 0,
				.5, .5, 0));
		assertEpsilonEquals(0, Segment3afp.computeDistanceSquaredLinePoint(
				1, 1, 0, 0, 0, 0,
				.5, .5, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticComputeDistanceSquaredSegmentPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(3.769230769230769, Segment3afp.computeDistanceSquaredSegmentPoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 0));
		assertEpsilonEquals(3.769230769230769, Segment3afp.computeDistanceSquaredSegmentPoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(1, Segment3afp.computeDistanceSquaredSegmentPoint(
				0, 0, 0, 3, 0, 0,
				2, 1, 0));
		assertEpsilonEquals(1, Segment3afp.computeDistanceSquaredSegmentPoint(
				3, 0, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(5, Segment3afp.computeDistanceSquaredSegmentPoint(
				0, 0, 0, -3, 1, 0,
				2, -1, 0));
		assertEpsilonEquals(5, Segment3afp.computeDistanceSquaredSegmentPoint(
				-3, 1, 0, 0, 0, 0,
				2, -1, 0));

		assertEpsilonEquals(22225.99999999998, Segment3afp.computeDistanceSquaredSegmentPoint(
				0, 0, 0, -3, 1, 0,
				2, 150, 0));
		assertEpsilonEquals(22225.99999999998, Segment3afp.computeDistanceSquaredSegmentPoint(
				-3, 1, 0, 0, 0, 0,
				2, 150, 0));

		assertEpsilonEquals(0, Segment3afp.computeDistanceSquaredSegmentPoint(
				0, 0, 0, 1, 1, 0,
				.5, .5, 0));
		assertEpsilonEquals(0, Segment3afp.computeDistanceSquaredSegmentPoint(
				1, 1, 0, 0, 0, 0,
				.5, .5, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticInterpolate(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.interpolate(1., 2., 0, 3., 4., 0, 0., result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(2, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.interpolate(1., 2., 0, 3., 4., 0, .25, result);
		assertEpsilonEquals(1.5, result.getX());
		assertEpsilonEquals(2.5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.interpolate(1., 2., 0, 3., 4., 0, .5, result);
		assertEpsilonEquals(2, result.getX());
		assertEpsilonEquals(3., result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.interpolate(1., 2., 0, 3., 4., 0, .75, result);
		assertEpsilonEquals(2.5, result.getX());
		assertEpsilonEquals(3.5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.interpolate(1., 2., 0, 3., 4., 0, 1., result);
		assertEpsilonEquals(3, result.getX());
		assertEpsilonEquals(4, result.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticIntersectsLineLine(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment3afp.intersectsLineLine(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 1, 1, 0));
		assertTrue(Segment3afp.intersectsLineLine(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 2, 2, 0));
		assertTrue(Segment3afp.intersectsLineLine(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, .5, .5, 0));
		assertTrue(Segment3afp.intersectsLineLine(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, .5, .5, 0));
		assertTrue(Segment3afp.intersectsLineLine(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, 0, 0, 0));
		assertTrue(Segment3afp.intersectsLineLine(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, -1, -1, 0));

		assertTrue(Segment3afp.intersectsLineLine(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, 4, 0, 0));

		assertFalse(Segment3afp.intersectsLineLine(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, -2, 1, 0));

		assertFalse(Segment3afp.intersectsLineLine(
				0, 0, 0, 1, 1, 0,
				10, 0, 0, 9, -1, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticIntersectsSegmentLine(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment3afp.intersectsSegmentLine(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 1, 1, 0));
		assertTrue(Segment3afp.intersectsSegmentLine(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 2, 2, 0));
		assertTrue(Segment3afp.intersectsSegmentLine(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, .5, .5, 0));
		assertTrue(Segment3afp.intersectsSegmentLine(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, .5, .5, 0));
		assertTrue(Segment3afp.intersectsSegmentLine(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, 0, 0, 0));
		assertTrue(Segment3afp.intersectsSegmentLine(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, -1, -1, 0));

		assertTrue(Segment3afp.intersectsSegmentLine(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, 4, 0, 0));
		assertFalse(Segment3afp.intersectsSegmentLine(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, -2, 1, 0));
		assertFalse(Segment3afp.intersectsSegmentLine(
				0, 0, 0, 1, 1, 0,
				10, 0, 0, 9, -1, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticIntersectsSegmentSegmentWithEnds(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				0, .5, 0, 1, .5, 0));
		assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 1, 1, 0));
		assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 2, 2, 0));
		assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, .5, .5, 0));
		assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, .5, .5, 0));
		assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, 0, 0, 0));
		assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, -1, -1, 0));
		assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, 4, 0, 0));
		assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, -1, 0, 4, -1, 0));
		assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, -1, 0, -1, -1, 0));
		assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, -2, 1, 0));
		assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				10, 0, 0, 9, -1, 0));
		assertTrue(
				Segment3afp.intersectsSegmentSegmentWithEnds(
						7, -5, 0, 1, 1, 0,
						4, -3, 0, 1, 1, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticIntersectsSegmentSegmentWithoutEnds(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 0, 1, 1, 0,
				0, .5, 0, 1, .5, 0));
		assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 1, 1, 0));
		assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 2, 2, 0));
		assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, .5, .5, 0));
		assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, .5, .5, 0));
		assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, 0, 0, 0));
		assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, -1, -1, 0));

		assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, 4, 0, 0));

		assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 0, 1, 1, 0,
				-3, -1, 0, 4, -1, 0));

		assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 0, 1, 1, 0,
				-3, -1, 0, -1, -1, 0));

		assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, -2, 1, 0));

		assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
				0, 0, 0, 1, 1, 0,
				10, 0, 0, 9, -1, 0));

		assertFalse(
				Segment3afp.intersectsSegmentSegmentWithoutEnds(
						7, -5, 0, 1, 1, 0,
						4, -3, 0, 1, 1, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticIsCollinearLines(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment3afp.isCollinearLines(0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0));
		assertTrue(Segment3afp.isCollinearLines(0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0));
		assertTrue(Segment3afp.isCollinearLines(0, 0, 0, 1, 1, 0, 0, 0, 0, -1, -1, 0));
		assertTrue(Segment3afp.isCollinearLines(0, 0, 0, 1, 1, 0, -2, -2, 0, -3, -3, 0));
		assertFalse(Segment3afp.isCollinearLines(0, 0, 0, 1, 1, 0, 5, 0, 0, 6, 1, 0));
		assertFalse(Segment3afp.isCollinearLines(0, 0, 0, 1, 1, 0, 154, -124, 0, -2, 457, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticIsParallelLines(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0));
		assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0));
		assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 0, 0, 0, -1, -1, 0));
		assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, -2, -2, 0, -3, -3, 0));
		assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 5, 0, 0, 6, 1, 0));
		assertFalse(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 154, -124, 0, -2, 457, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticIsPointClosedToLine(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment3afp.isPointClosedToLine(0, 0, 0, 1, 1, 0, 0, 0, 0, 0.1));
		assertTrue(Segment3afp.isPointClosedToLine(0, 0, 0, 1, 1, 0, 1, 1, 0, 0.1));
		assertTrue(Segment3afp.isPointClosedToLine(0, 0, 0, 1, 1, 0, .25, .25, 0, 0.1));
		assertFalse(Segment3afp.isPointClosedToLine(0, 0, 0, 1, 1, 0, 0.2, 0, 0, 0.1));
		assertFalse(Segment3afp.isPointClosedToLine(0, 0, 0, 1, 1, 0, 120, 0, 0, 0.1));
		assertTrue(Segment3afp.isPointClosedToLine(0, 0, 0, 1, 1, 0, -20.05, -20, 0, 0.1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticIsPointClosedToSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(Segment3afp.isPointClosedToSegment(0, 0, 0, 1, 1, 0, 0, 0, 0, 0.1));
		assertTrue(Segment3afp.isPointClosedToSegment(0, 0, 0, 1, 1, 0, 1, 1, 0, 0.1));
		assertTrue(Segment3afp.isPointClosedToSegment(0, 0, 0, 1, 1, 0, .25, .25, 0, 0.1));
		assertFalse(Segment3afp.isPointClosedToSegment(0, 0, 0, 1, 1, 0, 0.2, 0, 0, 0.1));
		assertFalse(Segment3afp.isPointClosedToSegment(0, 0, 0, 1, 1, 0, 120, 0, 0, 0.1));
		assertFalse(Segment3afp.isPointClosedToSegment(0, 0, 0, 1, 1, 0, -20.05, -20, 0, 0.1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void testClone(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T clone = this.shape.clone();
		assertNotNull(clone);
		assertNotSame(this.shape, clone);
		assertEquals(this.shape.getClass(), clone.getClass());
		assertEpsilonEquals(0, clone.getX1());
		assertEpsilonEquals(0, clone.getY1());
		assertEpsilonEquals(0, clone.getZ1());
		assertEpsilonEquals(1, clone.getX2());
		assertEpsilonEquals(1, clone.getY2());
		assertEpsilonEquals(1, clone.getZ2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void equalsObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createSegment(0, 0, 0, 5, 0, 0)));
		assertFalse(this.shape.equals(createSegment(0, 0, 0, 2, 2, 0)));
		assertFalse(this.shape.equals(createSphere(5, 8, 0, 6)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createSegment(0, 0, 0, 1, 1, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void equalsObject_withPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createSegment(0, 0, 0, 5, 0, 0).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(0, 0, 0, 2, 2, 0).getPathIterator()));
		assertFalse(this.shape.equals(createSphere(5, 8, 0, 6).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createSegment(0, 0, 0, 1, 1, 0).getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void equalsToPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createSegment(0, 0, 0, 5, 0, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(0, 0, 0, 2, 2, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSphere(5, 8, 0, 6).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createSegment(0, 0, 0, 1, 1, 0).getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void equalsToShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createSegment(0, 0, 0, 5, 0, 0)));
		assertFalse(this.shape.equalsToShape((T) createSegment(0, 0, 0, 2, 2, 0)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createSegment(0, 0, 0, 1, 1, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void isEmpty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void clear(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		assertEpsilonEquals(0, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(0, this.shape.getZ1());
		assertEpsilonEquals(0, this.shape.getX2());
		assertEpsilonEquals(0, this.shape.getY2());
		assertEpsilonEquals(0, this.shape.getZ2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getP1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p = this.shape.getP1();
		assertNotNull(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getP2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p = this.shape.getP2();
		assertNotNull(p);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());
		assertEpsilonEquals(1, p.getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setP1DoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setP1(123.456, -789.159, -1);
		assertEpsilonEquals(123.456, this.shape.getX1());
		assertEpsilonEquals(-789.159, this.shape.getY1());
		assertEpsilonEquals(-1, this.shape.getZ1());
		assertEpsilonEquals(1, this.shape.getX2());
		assertEpsilonEquals(1, this.shape.getY2());
		assertEpsilonEquals(1, this.shape.getZ2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setP1Point3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setP1(createPoint(123.456, -789.159, -1));
		assertEpsilonEquals(123.456, this.shape.getX1());
		assertEpsilonEquals(-789.159, this.shape.getY1());
		assertEpsilonEquals(-1, this.shape.getZ1());
		assertEpsilonEquals(1, this.shape.getX2());
		assertEpsilonEquals(1, this.shape.getY2());
		assertEpsilonEquals(1, this.shape.getZ2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setP2DoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setP2(123.456, -789.159, -1);
		assertEpsilonEquals(0, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(0, this.shape.getZ1());
		assertEpsilonEquals(123.456, this.shape.getX2());
		assertEpsilonEquals(-789.159, this.shape.getY2());
		assertEpsilonEquals(-1, this.shape.getZ2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setP2Point3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setP2(createPoint(123.456, -789.159, -1));
		assertEpsilonEquals(0, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(0, this.shape.getZ1());
		assertEpsilonEquals(123.456, this.shape.getX2());
		assertEpsilonEquals(-789.159, this.shape.getY2());
		assertEpsilonEquals(-1, this.shape.getZ2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getX1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getX1());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getX2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1, this.shape.getX2());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getY1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    assertEpsilonEquals(0, this.shape.getY1());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getY2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    assertEpsilonEquals(1, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getZ1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getZ1());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getZ2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1, this.shape.getZ2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void length(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(Math.sqrt(2), this.shape.getLength());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void lengthSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2, this.shape.getLengthSquared());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(123.456, 456.789, 456.123, 789.123, 159.753, 789.456);
		assertEpsilonEquals(123.456, this.shape.getX1());
		assertEpsilonEquals(456.789, this.shape.getY1());
		assertEpsilonEquals(456.123, this.shape.getZ1());
		assertEpsilonEquals(789.123, this.shape.getX2());
		assertEpsilonEquals(159.753, this.shape.getY2());
		assertEpsilonEquals(789.456, this.shape.getZ2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPoint3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(createPoint(123.456, 456.789, 456.123), createPoint(789.123, 159.753, 789.456));
		assertEpsilonEquals(123.456, this.shape.getX1());
		assertEpsilonEquals(456.789, this.shape.getY1());
		assertEpsilonEquals(456.123, this.shape.getZ1());
		assertEpsilonEquals(789.123, this.shape.getX2());
		assertEpsilonEquals(159.753, this.shape.getY2());
		assertEpsilonEquals(789.456, this.shape.getZ2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setX1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setX1(123.456);
		assertEpsilonEquals(123.456, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(0, this.shape.getZ1());
		assertEpsilonEquals(1, this.shape.getX2());
		assertEpsilonEquals(1, this.shape.getY2());
		assertEpsilonEquals(1, this.shape.getZ2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setX2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setX2(123.456);
		assertEpsilonEquals(0, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(0, this.shape.getZ1());
		assertEpsilonEquals(123.456, this.shape.getX2());
		assertEpsilonEquals(1, this.shape.getY2());
		assertEpsilonEquals(1, this.shape.getZ2());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setY1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    this.shape.setY1(123.456);
	    assertEpsilonEquals(0, this.shape.getX1());
	    assertEpsilonEquals(123.456, this.shape.getY1());
	    assertEpsilonEquals(0, this.shape.getZ1());
	    assertEpsilonEquals(1, this.shape.getX2());
	    assertEpsilonEquals(1, this.shape.getY2());
	    assertEpsilonEquals(1, this.shape.getZ2());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setY2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    this.shape.setY2(123.456);
	    assertEpsilonEquals(0, this.shape.getX1());
	    assertEpsilonEquals(0, this.shape.getY1());
	    assertEpsilonEquals(0, this.shape.getZ1());
	    assertEpsilonEquals(1, this.shape.getX2());
	    assertEpsilonEquals(123.456, this.shape.getY2());
	    assertEpsilonEquals(1, this.shape.getZ2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setZ1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setZ1(123.456);
		assertEpsilonEquals(0, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(123.456, this.shape.getZ1());
		assertEpsilonEquals(1, this.shape.getX2());
		assertEpsilonEquals(1, this.shape.getY2());
		assertEpsilonEquals(1, this.shape.getZ2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setZ2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setZ2(123.456);
		assertEpsilonEquals(0, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(0, this.shape.getZ1());
		assertEpsilonEquals(1, this.shape.getX2());
		assertEpsilonEquals(1, this.shape.getY2());
		assertEpsilonEquals(123.456, this.shape.getZ2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void transformTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;
		Transform3D tr;

		tr = new Transform3D();
		s = this.shape.clone();
		s.transform(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(1, s.getX2());
		assertEpsilonEquals(1, s.getY2());
		assertEpsilonEquals(1, s.getZ2());

		tr = new Transform3D();
		tr.setTranslation(3.4, 4.5, 0);
		s = this.shape.clone();
		s.transform(tr);
		assertEpsilonEquals(3.4, s.getX1());
		assertEpsilonEquals(4.5, s.getY1());
		assertEpsilonEquals(4.4, s.getX2());
		assertEpsilonEquals(5.5, s.getY2());

		tr = new Transform3D();
		tr.setRotation(MathConstants.PI);
		s = this.shape.clone();
		s.transform(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(-1, s.getX2());
		assertEpsilonEquals(-1, s.getY2());

		tr = new Transform3D();
		tr.setRotation(MathConstants.QUARTER_PI);
		s = this.shape.clone();
		s.transform(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getX2());
		assertEpsilonEquals(1.414213562, s.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void clipToRectangle(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(20, 45, 0, 43, 15, 0);
		assertTrue(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(20, this.shape.getX1());
		assertEpsilonEquals(45, this.shape.getY1());
		assertEpsilonEquals(43, this.shape.getX2());
		assertEpsilonEquals(15, this.shape.getY2());

		this.shape.set(20, 55, 0, 43, 15, 0);
		assertTrue(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(23.45, this.shape.getX1());
		assertEpsilonEquals(49.0, this.shape.getY1());
		assertEpsilonEquals(43, this.shape.getX2());
		assertEpsilonEquals(15, this.shape.getY2());

		this.shape.set(20, 0, 0, 43, 15, 0);
		assertTrue(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(38.4, this.shape.getX1());
		assertEpsilonEquals(12.0, this.shape.getY1());
		assertEpsilonEquals(43, this.shape.getX2());
		assertEpsilonEquals(15, this.shape.getY2());

		this.shape.set(0, 45, 0, 43, 15, 0);
		assertTrue(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(10, this.shape.getX1());
		assertEpsilonEquals(38.02326, this.shape.getY1());
		assertEpsilonEquals(43, this.shape.getX2());
		assertEpsilonEquals(15, this.shape.getY2());

		this.shape.set(20, 45, 0, 60, 15, 0);
		assertTrue(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(20, this.shape.getX1());
		assertEpsilonEquals(45, this.shape.getY1());
		assertEpsilonEquals(50, this.shape.getX2());
		assertEpsilonEquals(22.5, this.shape.getY2());

		this.shape.set(5, 45, 0, 30, 55, 0);
		assertTrue(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(10, this.shape.getX1());
		assertEpsilonEquals(47, this.shape.getY1());
		assertEpsilonEquals(15, this.shape.getX2());
		assertEpsilonEquals(49, this.shape.getY2());

		this.shape.set(40, 55, 0, 60, 15, 0);
		assertTrue(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(43, this.shape.getX1());
		assertEpsilonEquals(49, this.shape.getY1());
		assertEpsilonEquals(50, this.shape.getX2());
		assertEpsilonEquals(35, this.shape.getY2());

		this.shape.set(40, 0, 0, 60, 40, 0);
		assertTrue(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(46, this.shape.getX1());
		assertEpsilonEquals(12, this.shape.getY1());
		assertEpsilonEquals(50, this.shape.getX2());
		assertEpsilonEquals(20, this.shape.getY2());

		this.shape.set(0, 40, 0, 20, 0, 0);
		assertTrue(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(10, this.shape.getX1());
		assertEpsilonEquals(20, this.shape.getY1());
		assertEpsilonEquals(14, this.shape.getX2());
		assertEpsilonEquals(12, this.shape.getY2());

		this.shape.set(0, 45, 0, 100, 15, 0);
		assertTrue(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(10, this.shape.getX1());
		assertEpsilonEquals(42, this.shape.getY1());
		assertEpsilonEquals(50, this.shape.getX2());
		assertEpsilonEquals(30, this.shape.getY2());

		this.shape.set(20, 100, 0, 43, 0, 0);
		assertTrue(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(31.73, this.shape.getX1());
		assertEpsilonEquals(49, this.shape.getY1());
		assertEpsilonEquals(40.24, this.shape.getX2());
		assertEpsilonEquals(12, this.shape.getY2());

		this.shape.set(20, 100, 0, 43, 101, 0);
		assertFalse(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(20, this.shape.getX1());
		assertEpsilonEquals(100, this.shape.getY1());
		assertEpsilonEquals(43, this.shape.getX2());
		assertEpsilonEquals(101, this.shape.getY2());

		this.shape.set(100, 45, 0, 102, 15, 0);
		assertFalse(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(100, this.shape.getX1());
		assertEpsilonEquals(45, this.shape.getY1());
		assertEpsilonEquals(102, this.shape.getX2());
		assertEpsilonEquals(15, this.shape.getY2());

		this.shape.set(20, 0, 0, 43, -2, 0);
		assertFalse(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(20, this.shape.getX1());
		assertEpsilonEquals(0, this.shape.getY1());
		assertEpsilonEquals(43, this.shape.getX2());
		assertEpsilonEquals(-2, this.shape.getY2());

		this.shape.set(-100, 45, 0, -48, 15, 0);
		assertFalse(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(-100, this.shape.getX1());
		assertEpsilonEquals(45, this.shape.getY1());
		assertEpsilonEquals(-48, this.shape.getX2());
		assertEpsilonEquals(15, this.shape.getY2());

		this.shape.set(-100, 60, 0, -98, 61, 0);
		assertFalse(this.shape.clipToRectangle(10, 12, 0, 50, 49, 0));
		assertEpsilonEquals(-100, this.shape.getX1());
		assertEpsilonEquals(60, this.shape.getY1());
		assertEpsilonEquals(-98, this.shape.getX2());
		assertEpsilonEquals(61, this.shape.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void containsDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.contains(0, 0, 0));
		assertTrue(this.shape.contains(.5, .5, 0));
		assertTrue(this.shape.contains(1, 1, 0));
		assertFalse(this.shape.contains(2.3, 4.5, 0));
		assertFalse(this.shape.contains(2, 2, 0));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void containsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.contains(createPoint(0, 0, 0)));
		assertTrue(this.shape.contains(createPoint(.5, .5, 0)));
		assertTrue(this.shape.contains(createPoint(1, 1, 0)));
		assertFalse(this.shape.contains(createPoint(2.3, 4.5, 0)));
		assertFalse(this.shape.contains(createPoint(2, 2, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getClosestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;

		p = this.shape.getClosestPointTo(createPoint(0, 0, 0));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = this.shape.getClosestPointTo(createPoint(.5, .5, 0));
		assertEpsilonEquals(.5, p.getX());
		assertEpsilonEquals(.5, p.getY());

		p = this.shape.getClosestPointTo(createPoint(1, 1, 0));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());

		p = this.shape.getClosestPointTo(createPoint(2, 2, 0));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());

		p = this.shape.getClosestPointTo(createPoint(-2, 2, 0));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = this.shape.getClosestPointTo(createPoint(0.1, 1.2, 0));
		assertEpsilonEquals(0.65, p.getX());
		assertEpsilonEquals(0.65, p.getY());

		p = this.shape.getClosestPointTo(createPoint(10.1, -.2, 0));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;

		p = this.shape.getFarthestPointTo(createPoint(0, 0, 0));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(.5, .5, 0));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(1, 1, 0));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(2, 2, 0));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(-2, 2, 0));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(0.1, 1.2, 0));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(10.1, -.2, 0));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(0, 0, 0)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(.5, .5, 0)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(1, 1, 0)));

		assertEpsilonEquals(3.733630941, this.shape.getDistance(createPoint(2.3, 4.5, 0)));

		assertEpsilonEquals(1.414213562, this.shape.getDistance(createPoint(2, 2, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(0, 0, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(.5, .5, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(1, 1, 0)));

		assertEpsilonEquals(13.94, this.shape.getDistanceSquared(createPoint(2.3, 4.5, 0)));

		assertEpsilonEquals(2, this.shape.getDistanceSquared(createPoint(2, 2, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceL1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(0, 0, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(.5, .5, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(1, 1, 0)));

		assertEpsilonEquals(4.8, this.shape.getDistanceL1(createPoint(2.3, 4.5, 0)));

		assertEpsilonEquals(2, this.shape.getDistanceL1(createPoint(2, 2, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceLinf(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(0, 0, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(.5, .5, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(1, 1, 0)));

		assertEpsilonEquals(3.5, this.shape.getDistanceLinf(createPoint(2.3, 4.5, 0)));

		assertEpsilonEquals(1, this.shape.getDistanceLinf(createPoint(2, 2, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setIT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createSegment(123.456, 456.789, 0, 789.123, 159.753, 0));
		assertEpsilonEquals(123.456, this.shape.getX1());
		assertEpsilonEquals(456.789, this.shape.getY1());
		assertEpsilonEquals(789.123, this.shape.getX2());
		assertEpsilonEquals(159.753, this.shape.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getPathIteratorTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr;
		PathIterator3afp pi;

		tr = new Transform3D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4, 4.5, 0);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 3.4, 4.5, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.4, 5.5, 0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 0, 1.414213562, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void createTransformedShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;
		Transform3D tr;

		tr = new Transform3D();    	
		s = (Segment3afp) this.shape.createTransformedShape(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(1, s.getX2());
		assertEpsilonEquals(1, s.getY2());
		assertEpsilonEquals(1, s.getZ2());

		tr = new Transform3D();
		tr.setTranslation(3.4, 4.5, 0);
		s = (Segment3afp) this.shape.createTransformedShape(tr);
		assertEpsilonEquals(3.4, s.getX1());
		assertEpsilonEquals(4.5, s.getY1());
		assertEpsilonEquals(4.4, s.getX2());
		assertEpsilonEquals(5.5, s.getY2());

		tr = new Transform3D();
		tr.setRotation(MathConstants.PI);
		s = (Segment3afp) this.shape.createTransformedShape(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(-1, s.getX2());
		assertEpsilonEquals(-1, s.getY2());
		assertEpsilonEquals(-1, s.getZ2());

		tr = new Transform3D();
		tr.setRotation(MathConstants.QUARTER_PI);
		s = (Segment3afp) this.shape.createTransformedShape(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getX2());
		assertEpsilonEquals(1.414213562, s.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void translateDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(3.4, 4.5, 0);
		assertEpsilonEquals(3.4, this.shape.getX1());
		assertEpsilonEquals(4.5, this.shape.getY1());
		assertEpsilonEquals(4.4, this.shape.getX2());
		assertEpsilonEquals(5.5, this.shape.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(3.4, 4.5, 0));
		assertEpsilonEquals(3.4, this.shape.getX1());
		assertEpsilonEquals(4.5, this.shape.getY1());
		assertEpsilonEquals(4.4, this.shape.getX2());
		assertEpsilonEquals(5.5, this.shape.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void toBoundingBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B bb = this.shape.toBoundingBox();
		assertEpsilonEquals(0, bb.getMinX());
		assertEpsilonEquals(0, bb.getMinY());
		assertEpsilonEquals(1, bb.getMaxX());
		assertEpsilonEquals(1, bb.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void toBoundingBoxB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B bb = createRectangularPrism(0, 0, 0, 0, 0, 0);
		this.shape.toBoundingBox(bb);
		assertEpsilonEquals(0, bb.getMinX());
		assertEpsilonEquals(0, bb.getMinY());
		assertEpsilonEquals(0, bb.getMinZ());
		assertEpsilonEquals(1, bb.getMaxX());
		assertEpsilonEquals(1, bb.getMaxY());
		assertEpsilonEquals(1, bb.getMaxZ());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void containsRectangularPrism3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createRectangularPrism(0, 0, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(0, 0, 0, 0, 0, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(10, 10, 0, 1, 1, 0)));

		this.shape.set(10, 15, 0, 10, 18, 0);
		assertTrue(this.shape.contains(createRectangularPrism(10, 16, 0, 0, 1, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsRectangularPrism3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(20, 45, 0, 43, 15, 0);
		assertTrue(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));

		this.shape.set(20, 55, 0, 43, 15, 0);
		assertTrue(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));

		this.shape.set(20, 0, 0, 43, 15, 0);
		assertTrue(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));

		this.shape.set(0, 45, 0, 43, 15, 0);
		assertTrue(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));

		this.shape.set(20, 45, 0, 60, 15, 0);
		assertTrue(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));

		this.shape.set(5, 45, 0, 30, 55, 0);
		assertTrue(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));

		this.shape.set(40, 55, 0, 60, 15, 0);
		assertTrue(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));

		this.shape.set(40, 0, 0, 60, 40, 0);
		assertTrue(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));

		this.shape.set(0, 40, 0, 20, 0, 0);
		assertTrue(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));

		this.shape.set(0, 45, 0, 100, 15, 0);
		assertTrue(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));

		this.shape.set(20, 100, 0, 43, 0, 0);
		assertTrue(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));

		this.shape.set(20, 100, 0, 43, 101, 0);
		assertFalse(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));

		this.shape.set(100, 45, 0, 102, 15, 0);
		assertFalse(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));

		this.shape.set(20, 0, 0, 43, -2, 0);
		assertFalse(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));

		this.shape.set(-100, 45, 0, -48, 15, 0);
		assertFalse(this.shape.intersects(createRectangularPrism(10, 12, 0, 50, 49, 0)));

		this.shape.set(-100, 60, 0, -98, 61, 0);
		assertFalse(this.shape.intersects(createRectangularPrism(10, 12, 0, 40, 37, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createSphere(10, 10, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(0, 0, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(0, .5, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(.5, 0, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(.5, .5, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(2, 0, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(12, 8, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(12, 8, 0, 2.1)));
		assertFalse(this.shape.intersects(createSphere(2, 1, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(2, 1, 0, 1.1)));
		this.shape.set(0, 0, 0, 3, 0, 0);
		assertFalse(this.shape.intersects(createSphere(2, 1, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(2, 1, 0, 1.1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createSegment(10, -5, 0, 10, -4, 0)));
		assertFalse(this.shape.intersects(createSegment(10, 5, 0, 10, 4, 0)));
		assertFalse(this.shape.intersects(createSegment(-5, .5, 0, 0, .6, 0)));
		assertFalse(this.shape.intersects(createSegment(10, -1, 0, 11, .6, 0)));
		assertFalse(this.shape.intersects(createSegment(10, -1, 0, 11, 2, 0)));
		assertFalse(this.shape.intersects(createSegment(10, .5, 0, 11, 2, 0)));
		assertFalse(this.shape.intersects(createSegment(10, 2, 0, 11, .6, 0)));
		assertFalse(this.shape.intersects(createSegment(10, 2, 0, 11, -1, 0)));
		assertFalse(this.shape.intersects(createSegment(10, .6, 0, 11, -1, 0)));
		assertFalse(this.shape.intersects(createSegment(0, .5, 0, .25, .5, 0)));
		assertFalse(this.shape.intersects(createSegment(.75, .5, 0, 1, .5, 0)));
		assertFalse(this.shape.intersects(createSegment(5, -5, 0, .75, .5, 0)));
		assertTrue(this.shape.intersects(createSegment(5, -5, 0, 0, 1, 0)));
		assertTrue(this.shape.intersects(createSegment(5, -5, 0, 1, 1, 0)));
		assertFalse(this.shape.intersects(createSegment(-2, 1, 0, 5, -5, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsPath3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp p;

		p = createPath();
		p.moveTo(-2, -2, 0);
		p.lineTo(-2, 2, 0);
		p.lineTo(2, 2, 0);
		p.lineTo(2, -2, 0);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-2, -2, 0);
		p.lineTo(0, 0, 0);
		p.lineTo(-2, 2, 0);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertFalse(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-2, -2, 0);
		p.lineTo(2, 2, 0);
		p.lineTo(-2, 2, 0);
		assertTrue(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-2, -2, 0);
		p.lineTo(-2, 2, 0);
		p.lineTo(2, -2, 0);
		assertTrue(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-2, 2, 0);
		p.lineTo(1, 0, 0);
		p.lineTo(2, 1, 0);
		assertTrue(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-2, 2, 0);
		p.lineTo(2, 1, 0);
		p.lineTo(1, 0, 0);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsPathIterator3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp<?, ?, ?, ?, ?, B> p;

		p = createPath();
		p.moveTo(-2, -2, 0);
		p.lineTo(-2, 2, 0);
		p.lineTo(2, 2, 0);
		p.lineTo(2, -2, 0);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-2, -2, 0);
		p.lineTo(0, 0, 0);
		p.lineTo(-2, 2, 0);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertFalse(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-2, -2, 0);
		p.lineTo(2, 2, 0);
		p.lineTo(-2, 2, 0);
		assertTrue(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-2, -2, 0);
		p.lineTo(-2, 2, 0);
		p.lineTo(2, -2, 0);
		assertTrue(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-2, 2, 0);
		p.lineTo(1, 0, 0);
		p.lineTo(2, 1, 0);
		assertTrue(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-2, 2, 0);
		p.lineTo(2, 1, 0);
		p.lineTo(1, 0, 0);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape3D) createSphere(0, 0, 0, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(3.4, 4.5, 0));
		assertEpsilonEquals(3.4, this.shape.getX1());
		assertEpsilonEquals(4.5, this.shape.getY1());
		assertEpsilonEquals(4.4, this.shape.getX2());
		assertEpsilonEquals(5.5, this.shape.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_plus(createVector(3.4, 4.5, 0));
		assertNotSame(shape, this.shape);
		assertEpsilonEquals(3.4, shape.getX1());
		assertEpsilonEquals(4.5, shape.getY1());
		assertEpsilonEquals(4.4, shape.getX2());
		assertEpsilonEquals(5.5, shape.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(3.4, 4.5, 0));
		assertEpsilonEquals(-3.4, this.shape.getX1());
		assertEpsilonEquals(-4.5, this.shape.getY1());
		assertEpsilonEquals(-2.4, this.shape.getX2());
		assertEpsilonEquals(-3.5, this.shape.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_minus(createVector(3.4, 4.5, 0));
		assertEpsilonEquals(-3.4, shape.getX1());
		assertEpsilonEquals(-4.5, shape.getY1());
		assertEpsilonEquals(-2.4, shape.getX2());
		assertEpsilonEquals(-3.5, shape.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_multiplyTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;
		Transform3D tr;

		tr = new Transform3D();    	
		s = (Segment3afp) this.shape.operator_multiply(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(1, s.getX2());
		assertEpsilonEquals(1, s.getY2());

		tr = new Transform3D();
		tr.setTranslation(3.4, 4.5, 0);
		s = (Segment3afp) this.shape.operator_multiply(tr);
		assertEpsilonEquals(3.4, s.getX1());
		assertEpsilonEquals(4.5, s.getY1());
		assertEpsilonEquals(4.4, s.getX2());
		assertEpsilonEquals(5.5, s.getY2());

		tr = new Transform3D();
		tr.setRotation(MathConstants.PI);
		s = (Segment3afp) this.shape.operator_multiply(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(-1, s.getX2());
		assertEpsilonEquals(-1, s.getY2());

		tr = new Transform3D();
		tr.setRotation(MathConstants.QUARTER_PI);
		s = (Segment3afp) this.shape.operator_multiply(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getX2());
		assertEpsilonEquals(1.414213562, s.getY2());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_andPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createPoint(0, 0, 0)));
		assertTrue(this.shape.operator_and(createPoint(.5, .5, 0)));
		assertTrue(this.shape.operator_and(createPoint(1, 1, 0)));
		assertFalse(this.shape.operator_and(createPoint(2.3, 4.5, 0)));
		assertFalse(this.shape.operator_and(createPoint(2, 2, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createSphere(0, 0, 0, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_upToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(.5, .5, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(1, 1, 0)));

		assertEpsilonEquals(3.733630941, this.shape.operator_upTo(createPoint(2.3, 4.5, 0)));

		assertEpsilonEquals(1.414213562, this.shape.operator_upTo(createPoint(2, 2, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void issue15(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp segment = createSegment(-20, -20, 0, 20, 20, 0);
		Path3afp path = createPath();
		path.moveTo(5, 5, 0);
		path.lineTo(5, -5, 0);
		path.lineTo(-5, -5, 0);
		path.lineTo(-5, 5, 0);
		path.lineTo(5, 5, 0);
		assertTrue(path.intersects(segment));
	}

}
