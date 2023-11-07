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

package org.arakhne.afc.math.test.geometry.d3.afp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp.LineIntersection;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp.SegmentIntersection;
import org.arakhne.afc.math.geometry.d3.d.Point3d;
import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractSegment3afpTest<T extends Segment3afp<?, T, ?, ?, ?, ?, B>,
			B extends AlignedBox3afp<?, ?, ?, ?, ?, ?, B>> extends AbstractShape3afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createSegment(0, 0, 0, 1, 1, 1);
	}

	@DisplayName("calculatesClosestPointToPoint(double,double,double,double,double,double,double,double,double,Point3D)")
	@Test
	public void staticCalculatesClosestPointTo() {
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.calculatesClosestPointToPoint(0, 0, 0, 1, 1, 1, 0, 0, 0, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
		assertEpsilonEquals(0, result.getZ());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.calculatesClosestPointToPoint(0, 0, 0, 1, 1, 1, .75, .75, .75, result);
		assertEpsilonEquals(.75, result.getX());
		assertEpsilonEquals(.75, result.getY());
		assertEpsilonEquals(.75, result.getZ());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.calculatesClosestPointToPoint(0, 0, 0, 1, 1, 1, -10, -50, 0, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
		assertEpsilonEquals(0, result.getZ());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.calculatesClosestPointToPoint(0, 0, 0, 1, 1, 1, 200, -50, 0, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(1, result.getY());
		assertEpsilonEquals(1, result.getZ());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.calculatesClosestPointToPoint(0, 0, 0, 1, 1, 1, 0, 1, 0, result);
		assertEpsilonEquals(.3333333333, result.getX());
		assertEpsilonEquals(.3333333333, result.getY());
		assertEpsilonEquals(.3333333333, result.getZ());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.calculatesClosestPointToPoint(0, 0, 5, 1, -2, 1, 0, 1, 0, result);
		assertEpsilonEquals(0.857142857142, result.getX());
		assertEpsilonEquals(-1.7142857142857142, result.getY());
		assertEpsilonEquals(1.5714285714285716, result.getZ());
	}

	@DisplayName("calculatesFarthestPointTo(double,double,double,double,double,double,double,double,double,Point3D)")
	@Test
	public void calculatesFarthestPointTo() {
		Point3D p;

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.calculatesFarthestPointTo(0, 0, 0, 1, 1, 0, 0, 0, 0, p);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.calculatesFarthestPointTo(0, 0, 0, 1, 1, 0, .5, .5, 0, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.calculatesFarthestPointTo(0, 0, 0, 1, 1, 0, 1, 1, 0, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.calculatesFarthestPointTo(0, 0, 0, 1, 1, 0, 2, 2, 0, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.calculatesFarthestPointTo(0, 0, 0, 1, 1, 0, -2, 2, 0, p);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.calculatesFarthestPointTo(0, 0, 0, 1, 1, 0, 0.1, 1.2, 0, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Segment3afp.calculatesFarthestPointTo(0, 0, 0, 1, 1, 0, 10.1, -.2, 0, p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	@DisplayName("calculatesLineLineIntersection(double,double,double,double,double,double,double,double,double,double,double,double,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesLineLineIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(
				Segment3afp.calculatesLineLineIntersection(
				0, 0, 1, 2, 2, 1,
				0, 2, 1, 2, 0, 1,
				result));
		assertEpsilonEquals(new Point3d(1., 1., 1.), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(
				Segment3afp.calculatesLineLineIntersection(
				100, 50, 0, 100, 60, 0,
				90, 55, 0, 2000, 55, 0,
				result));
		assertEpsilonEquals(new Point3d(100., 55., 0.), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertFalse(Segment3afp.calculatesLineLineIntersection(
				100, 50, 0, 100, 60, 0,
				200, 0, 0, 200, 10, 0,
				result));

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(
				Segment3afp.calculatesLineLineIntersection(
				100, -50, 0, 100, -60, 0,
				90, 55, 0, 2000, 55, 0,
				result));
		assertEpsilonEquals(new Point3d(100, 55, 0), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(
				Segment3afp.calculatesLineLineIntersection(
				1000, 1.5325000286102295, 0, 2500, 1.5325000286102295, 0,
				1184.001080023255, 1.6651813832907332, 0, 1200.7014393876193, 1.372326130924099, 0,
				result));
		assertEpsilonEquals(new Point3d(1191.567365026, 1.53250002861, 0), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertFalse(Segment3afp.calculatesLineLineIntersection(
				100, 50, 1, 100, 60, 5,
				90, 55, 0, 2000, 55, -10,
				result));
	}

	protected void assertEpsilonEquals(boolean colinear, double position1, double position2, double position3, double position4, LineIntersection intersection) {
		assertNotNull(intersection, "Intersection must exist");
		assertEquals(colinear, intersection.colinear(), "invalid colinear flag");
		assertEpsilonEquals(new Point2d(position1, position2), new Point2d(intersection.position1(), intersection.position2()), "position1 or position2 is invalid");
		if (colinear) {
			assertEpsilonEquals(new Point2d(position3, position4), new Point2d(intersection.position3(), intersection.position4()), "position3 or position4 is invalid");
		}
	}
	
	protected void assertEpsilonEquals(boolean colinear, double position1, double position2, SegmentIntersection intersection) {
		assertNotNull(intersection, "Intersection must exist");
		assertEquals(colinear, intersection.colinear(), "invalid colinear flag");
		assertEpsilonEquals(new Point2d(position1, position2), new Point2d(intersection.position1(), intersection.position2()), "position1 or position2 is invalid");
	}

	@DisplayName("calculatesLineLineIntersectionFactors(double,double,double,double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesLineLineIntersectionFactors(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(false,
				.5, .00523560209,
				Double.NaN, Double.NaN,
				Segment3afp.calculatesLineLineIntersectionFactors(
				100, 50, 0, 100, 60, 0,
				90, 55, 0, 2000, 55, 0));

		assertNull(Segment3afp.calculatesLineLineIntersectionFactors(
				100, 50, 0, 100, 60, 0,
				200, 0, 0, 200, 10, 0));

		assertEpsilonEquals(false,
				-10.5,  .00523560209,
				Double.NaN,  Double.NaN,
				Segment3afp.calculatesLineLineIntersectionFactors(
				100, -50, 0, 100, -60, 0,
				90, 55, 0, 2000, 55, 0));

		assertEpsilonEquals(false,
				.1277115766843605, .453061208936,
				Double.NaN,  Double.NaN,
				Segment3afp.calculatesLineLineIntersectionFactors(
				1000, 1.5325000286102295, 0, 2500, 1.5325000286102295, 0,
				1184.001080023255, 1.6651813832907332, 0, 1200.7014393876193, 1.372326130924099, 0));

		assertNull(Segment3afp.calculatesLineLineIntersectionFactors(
				100, 50, 1, 100, 60, 5,
				90, 55, 0, 2000, 55, -10));

		assertEpsilonEquals(true,
				0.,  1.,
				2., 5.,
				Segment3afp.calculatesLineLineIntersectionFactors(
				100, 50, 1, 100, 60, 5,
				100, 70, 9, 100, 100, 21));
	}

	@DisplayName("calculatesProjectedPointOnLine(double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticCalculatesProjectedPointOnLine() {
		assertEpsilonEquals(.3076923076923077, Segment3afp.calculatesProjectedPointOnLine(
				2, 1, 0,
				0, 0, 0, 3, -2, 0));

		assertEpsilonEquals(.6666666666666666, Segment3afp.calculatesProjectedPointOnLine(
				2, 1, 0,
				0, 0, 0, 3, 0, 0));

		assertEpsilonEquals(-.7, Segment3afp.calculatesProjectedPointOnLine(
				2, -1, 0,
				0, 0, 0, -3, 1, 0));

		assertEpsilonEquals(14.4, Segment3afp.calculatesProjectedPointOnLine(
				2, 150, 0,
				0, 0, 0, -3, 1, 0));

		assertEpsilonEquals(.5, Segment3afp.calculatesProjectedPointOnLine(
				.5, .5, 0,
				0, 0, 0, 1, 1, 0));

		assertEpsilonEquals(.5, Segment3afp.calculatesProjectedPointOnLine(
				.5, .5, 15,
				0, 0, 0, 1, 1, 0));

		assertEpsilonEquals(.5, Segment3afp.calculatesProjectedPointOnLine(
				.5, .5, -15,
				0, 0, 0, 1, 1, 0));

		assertEpsilonEquals(.18421052631578946, Segment3afp.calculatesProjectedPointOnLine(
				.5, .5, 5,
				0, 0, 6, 1, 1, 0));

		assertEpsilonEquals(1., Segment3afp.calculatesProjectedPointOnLine(
				.5, .5, 15,
				0, 2, 0, 1, 1, 0));

		assertEpsilonEquals(1.06756756, Segment3afp.calculatesProjectedPointOnLine(
				.5, .5, -15,
				0, 7, 0, 1, 1, 0));

		assertEpsilonEquals(.737623762376, Segment3afp.calculatesProjectedPointOnLine(
				.5, .5, 5,
				0, 9, 6, 1, 1, 0));
	}

	@DisplayName("calculatesRelativeDistanceLinePoint(double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticCalculatesRelativeDistanceLinePoint() {
		assertEpsilonEquals(-1.941450686788302, Segment3afp.calculatesRelativeDistanceLinePoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 0));
		assertEpsilonEquals(1.941450686788302, Segment3afp.calculatesRelativeDistanceLinePoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(-1, Segment3afp.calculatesRelativeDistanceLinePoint(
				0, 0, 0, 3, 0, 0,
				2, 1, 0));
		assertEpsilonEquals(1, Segment3afp.calculatesRelativeDistanceLinePoint(
				3, 0, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(-.3162277660168379, Segment3afp.calculatesRelativeDistanceLinePoint(
				0, 0, 0, -3, 1, 0,
				2, -1, 0));
		assertEpsilonEquals(.3162277660168379, Segment3afp.calculatesRelativeDistanceLinePoint(
				-3, 1, 0, 0, 0, 0,
				2, -1, 0));

		assertEpsilonEquals(142.9349502396107, Segment3afp.calculatesRelativeDistanceLinePoint(
				0, 0, 0, -3, 1, 0,
				2, 150, 0));
		assertEpsilonEquals(-142.9349502396107, Segment3afp.calculatesRelativeDistanceLinePoint(
				-3, 1, 0, 0, 0, 0,
				2, 150, 0));

		assertEpsilonEquals(0, Segment3afp.calculatesRelativeDistanceLinePoint(
				0, 0, 0, 1, 1, 0,
				.5, .5, 0));
		assertEpsilonEquals(0, Segment3afp.calculatesRelativeDistanceLinePoint(
				1, 1, 0, 0, 0, 0,
				.5, .5, 0));
	}

	@DisplayName("calculatesSegmentSegmentIntersection(double,double,double,double,double,double,double,double,double,double,double,double,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesSegmentSegmentIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(
				Segment3afp.calculatesSegmentSegmentIntersection(
				0, 0, 1, 2, 2, 1,
				0, 2, 1, 2, 0, 1,
				result));
		assertEpsilonEquals(new Point3d(1., 1., 1.), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(
				Segment3afp.calculatesSegmentSegmentIntersection(
				100, 50, 0, 100, 60, 0,
				90, 55, 0, 2000, 55, 0,
				result));
		assertEpsilonEquals(new Point3d(100., 55., 0.), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertFalse(Segment3afp.calculatesSegmentSegmentIntersection(
				100, 50, 0, 100, 60, 0,
				200, 0, 0, 200, 10, 0,
				result));

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertFalse(
				Segment3afp.calculatesSegmentSegmentIntersection(
				100, -50, 0, 100, -60, 0,
				90, 55, 0, 2000, 55, 0,
				result));

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(
				Segment3afp.calculatesSegmentSegmentIntersection(
				1000, 1.5325000286102295, 0, 2500, 1.5325000286102295, 0,
				1184.001080023255, 1.6651813832907332, 0, 1200.7014393876193, 1.372326130924099, 0,
				result));
		assertEpsilonEquals(new Point3d(1191.567365026, 1.53250002861, 0), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertFalse(Segment3afp.calculatesSegmentSegmentIntersection(
				100, 50, 1, 100, 60, 5,
				90, 55, 0, 2000, 55, -10,
				result));
	}

	@DisplayName("calculatesSegmentSegmentIntersectionFactors(double,double,double,double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesSegmentSegmentIntersectionFactors(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(false,
				.5, .00523560209,
				Segment3afp.calculatesSegmentSegmentIntersectionFactors(
				100, 50, 0, 100, 60, 0,
				90, 55, 0, 2000, 55, 0));

		assertNull(Segment3afp.calculatesSegmentSegmentIntersectionFactors(
				100, 50, 0, 100, 60, 0,
				200, 0, 0, 200, 10, 0));

		assertNull(Segment3afp.calculatesSegmentSegmentIntersectionFactors(
				100, -50, 0, 100, -60, 0,
				90, 55, 0, 2000, 55, 0));

		assertEpsilonEquals(false,
				.1277115766843605, .453061208936,
				Segment3afp.calculatesSegmentSegmentIntersectionFactors(
				1000, 1.5325000286102295, 0, 2500, 1.5325000286102295, 0,
				1184.001080023255, 1.6651813832907332, 0, 1200.7014393876193, 1.372326130924099, 0));

		assertNull(Segment3afp.calculatesSegmentSegmentIntersectionFactors(
				100, 50, 1, 100, 60, 5,
				90, 55, 0, 2000, 55, -10));

		assertEpsilonEquals(true,
				0., 0.,
				Segment3afp.calculatesSegmentSegmentIntersectionFactors(
				20, 10, 5, 40, 20, 10,
				20, 10, 5, 10, 5, 2.5));

		assertEpsilonEquals(true,
				0., 0.,
				Segment3afp.calculatesSegmentSegmentIntersectionFactors(
				20, 10, 5, 40, 20, 10,
				10, 5, 2.5, 20, 10, 5));

		assertNull(Segment3afp.calculatesSegmentSegmentIntersectionFactors(
				20, 10, 5, 40, 20, 10,
				19, 9, 4, 9, 4, 1.5));

		assertEpsilonEquals(false,
				.075, .25,
				Segment3afp.calculatesSegmentSegmentIntersectionFactors(
				20, 10, 5, 40, 10, 10,
				25, 10, 6, 11, 10, 3.5));
	}

	@DisplayName("calculatesDistanceLinePoint(double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticCalculatesDistanceLinePoint() {
		assertEpsilonEquals(1.941450686788302, Segment3afp.calculatesDistanceLinePoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 0));
		assertEpsilonEquals(1.941450686788302, Segment3afp.calculatesDistanceLinePoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 15));
		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 15));

		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
				0, 0, 0, 3, -2, 0,
				2, 1, -15));
		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
				3, -2, 0, 0, 0, 0,
				2, 1, -15));

		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 15));
		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 15));

		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
				0, 0, 0, 3, -2, 0,
				2, 1, -15));
		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
				3, -2, 0, 0, 0, 0,
				2, 1, -15));

		assertEpsilonEquals(1, Segment3afp.calculatesDistanceLinePoint(
				0, 0, 0, 3, 0, 0,
				2, 1, 0));
		assertEpsilonEquals(1, Segment3afp.calculatesDistanceLinePoint(
				3, 0, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(.3162277660168379, Segment3afp.calculatesDistanceLinePoint(
				0, 0, 0, -3, 1, 0,
				2, -1, 0));
		assertEpsilonEquals(.3162277660168379, Segment3afp.calculatesDistanceLinePoint(
				-3, 1, 0, 0, 0, 0,
				2, -1, 0));

		assertEpsilonEquals(142.9349502396107, Segment3afp.calculatesDistanceLinePoint(
				0, 0, 0, -3, 1, 0,
				2, 150, 0));
		assertEpsilonEquals(142.9349502396107, Segment3afp.calculatesDistanceLinePoint(
				-3, 1, 0, 0, 0, 0,
				2, 150, 0));

		assertEpsilonEquals(0, Segment3afp.calculatesDistanceLinePoint(
				0, 0, 0, 1, 1, 0,
				.5, .5, 0));
		assertEpsilonEquals(0, Segment3afp.calculatesDistanceLinePoint(
				1, 1, 0, 0, 0, 0,
				.5, .5, 0));

		assertEpsilonEquals(10.20616823515927, Segment3afp.calculatesDistanceLinePoint(
				6, -5.6, 2.458, 1.7, 4.6, -5,
				.5, .5, 9.1));
		assertEpsilonEquals(10.20616823515927, Segment3afp.calculatesDistanceLinePoint(
				6, -5.6, 2.458, 1.7, 4.6, -5,
				.5, .5, 9.1));
	}

	@DisplayName("calculatesDistanceSegmentPoint(double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticCalculatesDistanceSegmentPoint() {
		assertEpsilonEquals(1.941450686788302, Segment3afp.calculatesDistanceSegmentPoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 0));
		assertEpsilonEquals(1.941450686788302, Segment3afp.calculatesDistanceSegmentPoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 15));
		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 15));

		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
				0, 0, 0, 3, -2, 0,
				2, 1, -15));
		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
				3, -2, 0, 0, 0, 0,
				2, 1, -15));

		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 15));
		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 15));

		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
				0, 0, 0, 3, -2, 0,
				2, 1, -15));
		assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
				3, -2, 0, 0, 0, 0,
				2, 1, -15));

		assertEpsilonEquals(1, Segment3afp.calculatesDistanceSegmentPoint(
				0, 0, 0, 3, 0, 0,
				2, 1, 0));
		assertEpsilonEquals(1, Segment3afp.calculatesDistanceSegmentPoint(
				3, 0, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(2.23606797749979, Segment3afp.calculatesDistanceSegmentPoint(
				0, 0, 0, -3, 1, 0,
				2, -1, 0));
		assertEpsilonEquals(2.23606797749979, Segment3afp.calculatesDistanceSegmentPoint(
				-3, 1, 0, 0, 0, 0,
				2, -1, 0));

		assertEpsilonEquals(149.0838690133845, Segment3afp.calculatesDistanceSegmentPoint(
				0, 0, 0, -3, 1, 0,
				2, 150, 0));
		assertEpsilonEquals(149.0838690133845, Segment3afp.calculatesDistanceSegmentPoint(
				-3, 1, 0, 0, 0, 0,
				2, 150, 0));

		assertEpsilonEquals(0, Segment3afp.calculatesDistanceSegmentPoint(
				0, 0, 0, 1, 1, 0,
				.5, .5, 0));
		assertEpsilonEquals(0, Segment3afp.calculatesDistanceSegmentPoint(
				1, 1, 0, 0, 0, 0,
				.5, .5, 0));

		assertEpsilonEquals(16.186414056238647, Segment3afp.calculatesDistanceSegmentPoint(
				0, 0, 0, 3, -2, 0,
				-6, 1, 15));
		assertEpsilonEquals(16.186414056238647, Segment3afp.calculatesDistanceSegmentPoint(
				3, -2, 0, 0, 0, 0,
				-6, 1, 15));

		assertEpsilonEquals(16.186414056238647, Segment3afp.calculatesDistanceSegmentPoint(
				0, 0, 0, 3, -2, 0,
				-6, 1, -15));
		assertEpsilonEquals(16.186414056238647, Segment3afp.calculatesDistanceSegmentPoint(
				3, -2, 0, 0, 0, 0,
				-6, 1, -15));

		assertEpsilonEquals(16.186414056238647, Segment3afp.calculatesDistanceSegmentPoint(
				0, 0, 0, 3, -2, 0,
				-6, 1, 15));
		assertEpsilonEquals(16.186414056238647, Segment3afp.calculatesDistanceSegmentPoint(
				3, -2, 0, 0, 0, 0,
				-6, 1, 15));

		assertEpsilonEquals(16.186414056238647, Segment3afp.calculatesDistanceSegmentPoint(
				0, 0, 0, 3, -2, 0,
				-6, 1, -15));
		assertEpsilonEquals(16.186414056238647, Segment3afp.calculatesDistanceSegmentPoint(
				3, -2, 0, 0, 0, 0,
				-6, 1, -15));
	}

	@DisplayName("calculatesDistanceSquaredLinePoint(double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticCalculatesDistanceSquaredLinePoint() {
		assertEpsilonEquals(3.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 0));
		assertEpsilonEquals(3.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 15));
		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 15));

		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
				0, 0, 0, 3, -2, 0,
				2, 1, -15));
		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
				3, -2, 0, 0, 0, 0,
				2, 1, -15));

		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 15));
		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 15));

		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
				0, 0, 0, 3, -2, 0,
				2, 1, -15));
		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
				3, -2, 0, 0, 0, 0,
				2, 1, -15));

		assertEpsilonEquals(1, Segment3afp.calculatesDistanceSquaredLinePoint(
				0, 0, 0, 3, 0, 0,
				2, 1, 0));
		assertEpsilonEquals(1, Segment3afp.calculatesDistanceSquaredLinePoint(
				3, 0, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(0.1, Segment3afp.calculatesDistanceSquaredLinePoint(
				0, 0, 0, -3, 1, 0,
				2, -1, 0));
		assertEpsilonEquals(0.1, Segment3afp.calculatesDistanceSquaredLinePoint(
				-3, 1, 0, 0, 0, 0,
				2, -1, 0));

		assertEpsilonEquals(20430.4, Segment3afp.calculatesDistanceSquaredLinePoint(
				0, 0, 0, -3, 1, 0,
				2, 150, 0));
		assertEpsilonEquals(20430.4, Segment3afp.calculatesDistanceSquaredLinePoint(
				-3, 1, 0, 0, 0, 0,
				2, 150, 0));

		assertEpsilonEquals(0, Segment3afp.calculatesDistanceSquaredLinePoint(
				0, 0, 0, 1, 1, 0,
				.5, .5, 0));
		assertEpsilonEquals(0, Segment3afp.calculatesDistanceSquaredLinePoint(
				1, 1, 0, 0, 0, 0,
				.5, .5, 0));


		assertEpsilonEquals(104.165869939, Segment3afp.calculatesDistanceSquaredLinePoint(
				6, -5.6, 2.458, 1.7, 4.6, -5,
				.5, .5, 9.1));
		assertEpsilonEquals(104.165869939, Segment3afp.calculatesDistanceSquaredLinePoint(
				6, -5.6, 2.458, 1.7, 4.6, -5,
				.5, .5, 9.1));
	}

	@DisplayName("calculatesDistanceSquaredSegmentPoint(double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticCalculatesDistanceSquaredSegmentPoint() {
		assertEpsilonEquals(3.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 0));
		assertEpsilonEquals(3.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 15));
		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 15));

		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 0, 3, -2, 0,
				2, 1, -15));
		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
				3, -2, 0, 0, 0, 0,
				2, 1, -15));

		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 0, 3, -2, 0,
				2, 1, 15));
		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
				3, -2, 0, 0, 0, 0,
				2, 1, 15));

		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 0, 3, -2, 0,
				2, 1, -15));
		assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
				3, -2, 0, 0, 0, 0,
				2, 1, -15));

		assertEpsilonEquals(1, Segment3afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 0, 3, 0, 0,
				2, 1, 0));
		assertEpsilonEquals(1, Segment3afp.calculatesDistanceSquaredSegmentPoint(
				3, 0, 0, 0, 0, 0,
				2, 1, 0));

		assertEpsilonEquals(5., Segment3afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 0, -3, 1, 0,
				2, -1, 0));
		assertEpsilonEquals(5., Segment3afp.calculatesDistanceSquaredSegmentPoint(
				-3, 1, 0, 0, 0, 0,
				2, -1, 0));

		assertEpsilonEquals(22226., Segment3afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 0, -3, 1, 0,
				2, 150, 0));
		assertEpsilonEquals(22226., Segment3afp.calculatesDistanceSquaredSegmentPoint(
				-3, 1, 0, 0, 0, 0,
				2, 150, 0));

		assertEpsilonEquals(0, Segment3afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 0, 1, 1, 0,
				.5, .5, 0));
		assertEpsilonEquals(0, Segment3afp.calculatesDistanceSquaredSegmentPoint(
				1, 1, 0, 0, 0, 0,
				.5, .5, 0));

		assertEpsilonEquals(262., Segment3afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 0, 3, -2, 0,
				-6, 1, 15));
		assertEpsilonEquals(262., Segment3afp.calculatesDistanceSquaredSegmentPoint(
				3, -2, 0, 0, 0, 0,
				-6, 1, 15));

		assertEpsilonEquals(262., Segment3afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 0, 3, -2, 0,
				-6, 1, -15));
		assertEpsilonEquals(262., Segment3afp.calculatesDistanceSquaredSegmentPoint(
				3, -2, 0, 0, 0, 0,
				-6, 1, -15));

		assertEpsilonEquals(262., Segment3afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 0, 3, -2, 0,
				-6, 1, 15));
		assertEpsilonEquals(262., Segment3afp.calculatesDistanceSquaredSegmentPoint(
				3, -2, 0, 0, 0, 0,
				-6, 1, 15));

		assertEpsilonEquals(262., Segment3afp.calculatesDistanceSquaredSegmentPoint(
				0, 0, 0, 3, -2, 0,
				-6, 1, -15));
		assertEpsilonEquals(262., Segment3afp.calculatesDistanceSquaredSegmentPoint(
				3, -2, 0, 0, 0, 0,
				-6, 1, -15));
	}

	@DisplayName("interpolate(double,double,double,double,double,double,double,Point3D)")
	@Test
	public void staticInterpolate() {
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

	@DisplayName("intersectsLineLine(double,double,double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIntersectsLineLine() {
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

	@DisplayName("intersectsSegmentLineWithEnds(double,double,double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIntersectsSegmentLineWithEnds() {
		assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 1, 1, 0));
		assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 2, 2, 0));
		assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, .5, .5, 0));
		assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, .5, .5, 0));
		assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, 0, 0, 0));
		assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, -1, -1, 0));

		assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, 4, 0, 0));
		assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
				-3, 0, 0, 4, 0, 0,
				0, 0, 0, 1, 1, 0));
		assertFalse(Segment3afp.intersectsSegmentLineWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, -2, 1, 0));
		assertFalse(Segment3afp.intersectsSegmentLineWithEnds(
				0, 0, 0, 1, 1, 0,
				10, 0, 0, 9, -1, 0));
	}

	@DisplayName("intersectsSegmentLineWithoutEnds(double,double,double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIntersectsSegmentLineWithoutEnds() {
		assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 1, 1, 0));
		assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 2, 2, 0));
		assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, .5, .5, 0));
		assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, .5, .5, 0));
		assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, 0, 0, 0));
		assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, -1, -1, 0));

		assertFalse(Segment3afp.intersectsSegmentLineWithoutEnds(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, 4, 0, 0));
		assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
				-3, 0, 0, 4, 0, 0,
				0, 0, 0, 1, 1, 0));
		assertFalse(Segment3afp.intersectsSegmentLineWithoutEnds(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, -2, 1, 0));
		assertFalse(Segment3afp.intersectsSegmentLineWithoutEnds(
				0, 0, 0, 1, 1, 0,
				10, 0, 0, 9, -1, 0));
	}

	@DisplayName("intersectsSegmentSegmentWithEnds(double,double,double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIntersectsSegmentSegmentWithEnds() {
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

	@DisplayName("intersectsSegmentSegmentWithoutEnds(double,double,double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIntersectsSegmentSegmentWithoutEnds() {
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

	@DisplayName("isColinearLines(double,double,double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIsCollinearLines() {
		assertTrue(Segment3afp.isColinearLines(0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0));
		assertTrue(Segment3afp.isColinearLines(0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0));
		assertTrue(Segment3afp.isColinearLines(0, 0, 0, 1, 1, 0, 0, 0, 0, -1, -1, 0));
		assertTrue(Segment3afp.isColinearLines(0, 0, 0, 1, 1, 0, -2, -2, 0, -3, -3, 0));
		assertFalse(Segment3afp.isColinearLines(0, 0, 0, 1, 1, 0, 5, 0, 0, 6, 1, 0));
		assertFalse(Segment3afp.isColinearLines(0, 0, 0, 1, 1, 0, 154, -124, 0, -2, 457, 0));
	}

	@DisplayName("isParallelLines(double,double,double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIsParallelLines() {
		assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0));
		assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0));
		assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 0, 0, 0, -1, -1, 0));
		assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, -2, -2, 0, -3, -3, 0));
		assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 5, 0, 0, 6, 1, 0));
		assertFalse(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 154, -124, 0, -2, 457, 0));
	}

	@DisplayName("isPointClosedToLine(double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIsPointClosedToLine() {
		assertTrue(Segment3afp.isPointClosedToLine(0, 0, 0, 1, 1, 0, 0, 0, 0, 0.1));
		assertTrue(Segment3afp.isPointClosedToLine(0, 0, 0, 1, 1, 0, 1, 1, 0, 0.1));
		assertTrue(Segment3afp.isPointClosedToLine(0, 0, 0, 1, 1, 0, .25, .25, 0, 0.1));
		assertFalse(Segment3afp.isPointClosedToLine(0, 0, 0, 1, 1, 0, 0.2, 0, 0, 0.1));
		assertFalse(Segment3afp.isPointClosedToLine(0, 0, 0, 1, 1, 0, 120, 0, 0, 0.1));
		assertTrue(Segment3afp.isPointClosedToLine(0, 0, 0, 1, 1, 0, -20.05, -20, 0, 0.1));
	}

	@DisplayName("isPointClosedToSegment(double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIsPointClosedToSegment() {
		assertTrue(Segment3afp.isPointClosedToSegment(0, 0, 0, 1, 1, 0, 0, 0, 0, 0.1));
		assertTrue(Segment3afp.isPointClosedToSegment(0, 0, 0, 1, 1, 0, 1, 1, 0, 0.1));
		assertTrue(Segment3afp.isPointClosedToSegment(0, 0, 0, 1, 1, 0, .25, .25, 0, 0.1));
		assertFalse(Segment3afp.isPointClosedToSegment(0, 0, 0, 1, 1, 0, 0.2, 0, 0, 0.1));
		assertFalse(Segment3afp.isPointClosedToSegment(0, 0, 0, 1, 1, 0, 120, 0, 0, 0.1));
		assertFalse(Segment3afp.isPointClosedToSegment(0, 0, 0, 1, 1, 0, -20.05, -20, 0, 0.1));
	}

	@DisplayName("clone")
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

	@DisplayName("equals(Object)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createSegment(0, 0, 0, 5, 0, 0)));
		assertFalse(this.shape.equals(createSegment(0, 0, 0, 2, 2, 0)));
		assertFalse(this.shape.equals(createSphere(5, 8, 0, 6)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createSegment(0, 0, 0, 1, 1, 1)));
	}

	@DisplayName("equals(Object) with path iterator")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsObject_withPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createSegment(0, 0, 0, 5, 0, 0).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(0, 0, 0, 2, 2, 0).getPathIterator()));
		assertFalse(this.shape.equals(createSphere(5, 8, 0, 6).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createSegment(0, 0, 0, 1, 1, 1).getPathIterator()));
	}

	@DisplayName("equalsTo(PathIterator3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsToPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator3afp) null));
		assertFalse(this.shape.equalsToPathIterator(createSegment(0, 0, 0, 5, 0, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(0, 0, 0, 2, 2, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createAlignedBox(5, 8, 0, 6, 14, 2).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createSegment(0, 0, 0, 1, 1, 1).getPathIterator()));
	}

	@DisplayName("equalsToShape(Shape3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsToShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createSegment(0, 0, 0, 5, 0, 0)));
		assertFalse(this.shape.equalsToShape((T) createSegment(0, 0, 0, 2, 2, 0)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createSegment(0, 0, 0, 1, 1, 1)));
	}

	@DisplayName("isEmpty")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void isEmpty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@DisplayName("clear")
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

	@DisplayName("getP1")
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

	@DisplayName("getP2")
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

	@DisplayName("setP1(double,double,double)")
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

	@DisplayName("setP1(Point3D)")
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

	@DisplayName("setP3(double,double,double)")
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

	@DisplayName("setP2(Point3D)")
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

	@DisplayName("getX1")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getX1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getX1());
	}

	@DisplayName("getX2")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getX2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1, this.shape.getX2());
	}
	
	@DisplayName("getY1")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getY1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    assertEpsilonEquals(0, this.shape.getY1());
	}
	
	@DisplayName("getY2")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getY2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    assertEpsilonEquals(1, this.shape.getY2());
	}

	@DisplayName("getZ1")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getZ1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getZ1());
	}

	@DisplayName("getZ2")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getZ2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1, this.shape.getZ2());
	}

	@DisplayName("getLength")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getLength(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(Math.sqrt(3), this.shape.getLength());
	}

	@DisplayName("getLengthSquared")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getLengthSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(3, this.shape.getLengthSquared());
	}

	@DisplayName("set(double,double,double,double,double,double)")
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

	@DisplayName("set(Point3D,Point3D)")
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

	@DisplayName("setX1(double)")
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

	@DisplayName("setX2(double)")
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
	
	@DisplayName("setY1(double)")
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
	
	@DisplayName("setY2(double)")
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

	@DisplayName("setZ1(double)")
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

	@DisplayName("setZ2(double)")
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
	
	protected static Quaternion4d newAxisAngleZ(double angle) {
		final Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(0,  0,  1, angle);
		return q;
	}

	@DisplayName("transform(Transform3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void transformTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;
		Transform3D tr;

		tr = new Transform3D();    	
		s = (Segment3afp) this.shape.clone();
		s.transform(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(1, s.getX2());
		assertEpsilonEquals(1, s.getY2());
		assertEpsilonEquals(1, s.getZ2());

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4, 4.5, 1.3);
		s = (Segment3afp) this.shape.clone();
		s.transform(tr);
		assertEpsilonEquals(3.4, s.getX1());
		assertEpsilonEquals(4.5, s.getY1());
		assertEpsilonEquals(1.3, s.getZ1());
		assertEpsilonEquals(4.4, s.getX2());
		assertEpsilonEquals(5.5, s.getY2());
		assertEpsilonEquals(2.3, s.getZ2());

		tr = new Transform3D();
		tr.makeRotationMatrix(newAxisAngleZ(MathConstants.PI));
		s = (Segment3afp) this.shape.clone();
		s.transform(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(-1, s.getX2());
		assertEpsilonEquals(-1, s.getY2());
		assertEpsilonEquals(1, s.getZ2());

		tr = new Transform3D();
		tr.makeRotationMatrix(newAxisAngleZ(MathConstants.QUARTER_PI));
		s = (Segment3afp) this.shape.clone();
		s.transform(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(0, s.getX2());
		assertEpsilonEquals(1.41421356, s.getY2());
		assertEpsilonEquals(1, s.getZ2());
	}

	@DisplayName("contains(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void containsDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.contains(0, 0, 0));
		assertTrue(this.shape.contains(.5, .5, .5));
		assertTrue(this.shape.contains(1, 1, 1));
		assertFalse(this.shape.contains(2.3, 4.5, 0));
		assertFalse(this.shape.contains(2, 2, 0));
	}

	@DisplayName("contains(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void containsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.contains(createPoint(0, 0, 0)));
		assertTrue(this.shape.contains(createPoint(.5, .5, .5)));
		assertTrue(this.shape.contains(createPoint(1, 1, 1)));
		assertFalse(this.shape.contains(createPoint(2.3, 4.5, 0)));
		assertFalse(this.shape.contains(createPoint(2, 2, 0)));
		assertFalse(this.shape.contains(createPoint(-1, -1, -1)));
	}

	@DisplayName("getClosestPointTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getClosestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;

		p = this.shape.getClosestPointTo(createPoint(0, 0, 0));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(.5, .5, .5));
		assertEpsilonEquals(.5, p.getX());
		assertEpsilonEquals(.5, p.getY());
		assertEpsilonEquals(.5, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(1, 1, 1));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());
		assertEpsilonEquals(1, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(2, 2, 0));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());
		assertEpsilonEquals(1, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(-2, 2, 0));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(0.1, 1.2, .5));
		assertEpsilonEquals(0.6, p.getX());
		assertEpsilonEquals(0.6, p.getY());
		assertEpsilonEquals(0.6, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(10.1, -.2, 0));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());
		assertEpsilonEquals(1, p.getZ());
	}

	@DisplayName("getFarthestPointTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;

		p = this.shape.getFarthestPointTo(createPoint(0, 0, 0));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());
		assertEpsilonEquals(1, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(.5, .5, .5));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(1, 1, 1));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(2, 2, 0));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(-2, 2, 0));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());
		assertEpsilonEquals(1, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(0.1, 1.2, 0));
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(1, p.getY());
		assertEpsilonEquals(1, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(10.1, -.2, 0));
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	@DisplayName("getDistance(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(0, 0, 0)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(.5, .5, .5)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(1, 1, 1)));

		assertEpsilonEquals(3.86522951, this.shape.getDistance(createPoint(2.3, 4.5, 0)));

		assertEpsilonEquals(1.732050807, this.shape.getDistance(createPoint(2, 2, 0)));
	}

	@DisplayName("getDistanceSquared(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(0, 0, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(.5, .5, .5)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(1, 1, 1)));

		assertEpsilonEquals(14.94, this.shape.getDistanceSquared(createPoint(2.3, 4.5, 0)));

		assertEpsilonEquals(3, this.shape.getDistanceSquared(createPoint(2, 2, 0)));
	}

	@DisplayName("getDistanceL1(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceL1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(0, 0, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(.5, .5, .5)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(1, 1, 1)));

		assertEpsilonEquals(4.8, this.shape.getDistanceL1(createPoint(2.3, 4.5, 1)));

		assertEpsilonEquals(2, this.shape.getDistanceL1(createPoint(2, 2, 1)));
	}

	@DisplayName("getDistanceLinf(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceLinf(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(0, 0, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(.5, .5, .5)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(1, 1, 1)));

		assertEpsilonEquals(3.5, this.shape.getDistanceLinf(createPoint(2.3, 4.5, 0)));

		assertEpsilonEquals(1, this.shape.getDistanceLinf(createPoint(2, 2, 0)));
	}

	@DisplayName("set(Shape3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void setIT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createSegment(123.456, 456.789, 458.1, 789.123, 159.753, 145.36));
		assertEpsilonEquals(123.456, this.shape.getX1());
		assertEpsilonEquals(456.789, this.shape.getY1());
		assertEpsilonEquals(458.1, this.shape.getZ1());
		assertEpsilonEquals(789.123, this.shape.getX2());
		assertEpsilonEquals(159.753, this.shape.getY2());
		assertEpsilonEquals(145.36, this.shape.getZ2());
	}

	@DisplayName("getPathIterator")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 1);
		assertNoElement(pi);
	}

	@DisplayName("getPathIterator(Transform3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getPathIteratorTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr;
		PathIterator3afp pi;

		tr = new Transform3D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 1);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4, 4.5, 1.3);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 3.4, 4.5, 1.3);
		assertElement(pi, PathElementType.LINE_TO, 4.4, 5.5, 2.3);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(newAxisAngleZ(MathConstants.QUARTER_PI));
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 0, 1.414213562, 1);
		assertNoElement(pi);
	}

	@DisplayName("createTransformedShape(Transform3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
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
		tr.makeTranslationMatrix(3.4, 4.5, 1.3);
		s = (Segment3afp) this.shape.createTransformedShape(tr);
		assertEpsilonEquals(3.4, s.getX1());
		assertEpsilonEquals(4.5, s.getY1());
		assertEpsilonEquals(1.3, s.getZ1());
		assertEpsilonEquals(4.4, s.getX2());
		assertEpsilonEquals(5.5, s.getY2());
		assertEpsilonEquals(2.3, s.getZ2());

		tr = new Transform3D();
		tr.makeRotationMatrix(newAxisAngleZ(MathConstants.PI));
		s = (Segment3afp) this.shape.createTransformedShape(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(-1, s.getX2());
		assertEpsilonEquals(-1, s.getY2());
		assertEpsilonEquals(1, s.getZ2());

		tr = new Transform3D();
		tr.makeRotationMatrix(newAxisAngleZ(MathConstants.QUARTER_PI));
		s = (Segment3afp) this.shape.createTransformedShape(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(0, s.getX2());
		assertEpsilonEquals(1.41421356, s.getY2());
		assertEpsilonEquals(1, s.getZ2());
	}

	@DisplayName("translate(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void translateDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(3.4, 4.5, 1.3);
		assertEpsilonEquals(3.4, this.shape.getX1());
		assertEpsilonEquals(4.5, this.shape.getY1());
		assertEpsilonEquals(1.3, this.shape.getZ1());
		assertEpsilonEquals(4.4, this.shape.getX2());
		assertEpsilonEquals(5.5, this.shape.getY2());
		assertEpsilonEquals(2.3, this.shape.getZ2());
	}

	@DisplayName("translate(Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(3.4, 4.5, 1.3));
		assertEpsilonEquals(3.4, this.shape.getX1());
		assertEpsilonEquals(4.5, this.shape.getY1());
		assertEpsilonEquals(1.3, this.shape.getZ1());
		assertEpsilonEquals(4.4, this.shape.getX2());
		assertEpsilonEquals(5.5, this.shape.getY2());
		assertEpsilonEquals(2.3, this.shape.getZ2());
	}

	@DisplayName("toBoundingBox")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void toBoundingBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B bb = this.shape.toBoundingBox();
		assertEpsilonEquals(0, bb.getMinX());
		assertEpsilonEquals(0, bb.getMinY());
		assertEpsilonEquals(0, bb.getMinZ());
		assertEpsilonEquals(1, bb.getMaxX());
		assertEpsilonEquals(1, bb.getMaxY());
		assertEpsilonEquals(1, bb.getMaxZ());
	}

	@DisplayName("toBoundingBox(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void toBoundingBoxB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B bb = createAlignedBox(0, 0, 0, 0, 0, 0);
		this.shape.toBoundingBox(bb);
		assertEpsilonEquals(0, bb.getMinX());
		assertEpsilonEquals(0, bb.getMinY());
		assertEpsilonEquals(0, bb.getMinZ());
		assertEpsilonEquals(1, bb.getMaxX());
		assertEpsilonEquals(1, bb.getMaxY());
		assertEpsilonEquals(1, bb.getMaxZ());
	}

	@DisplayName("contains(AlignedBox3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void containsAlignedBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createAlignedBox(0, 0, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(0, 0, 0, 0, 0, 0)));
		assertFalse(this.shape.contains(createAlignedBox(10, 10, 0, 1, 1, 0)));

		this.shape.set(10, 15, 0, 10, 18, 0);
		assertTrue(this.shape.contains(createAlignedBox(10, 16, 0, 0, 1, 0)));
	}

	@DisplayName("intersects(AlignedBox3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsAlignedBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(20, 45, 0, 43, 15, 0);
		assertTrue(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));

		this.shape.set(20, 55, 0, 43, 15, 0);
		assertTrue(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));

		this.shape.set(20, 0, 0, 43, 15, 0);
		assertTrue(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));

		this.shape.set(0, 45, 0, 43, 15, 0);
		assertTrue(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));

		this.shape.set(20, 45, 0, 60, 15, 0);
		assertTrue(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));

		this.shape.set(5, 45, 0, 30, 55, 0);
		assertTrue(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));

		this.shape.set(40, 55, 0, 60, 15, 0);
		assertTrue(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));

		this.shape.set(40, 0, 0, 60, 40, 0);
		assertTrue(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));

		this.shape.set(0, 40, 0, 20, 0, 0);
		assertTrue(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));

		this.shape.set(0, 45, 0, 100, 15, 0);
		assertTrue(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));

		this.shape.set(20, 100, 0, 43, 0, 0);
		assertTrue(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));

		this.shape.set(20, 100, 0, 43, 101, 0);
		assertFalse(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));

		this.shape.set(100, 45, 0, 102, 15, 0);
		assertFalse(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));

		this.shape.set(20, 0, 0, 43, -2, 0);
		assertFalse(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));

		this.shape.set(-100, 45, 0, -48, 15, 0);
		assertFalse(this.shape.intersects(createAlignedBox(10, 12, 0, 50, 49, 0)));

		this.shape.set(-100, 60, 0, -98, 61, 0);
		assertFalse(this.shape.intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
	}

	@DisplayName("intersects(Sphere3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
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

	@DisplayName("intersects(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		this.shape.set(0, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(createSegment(0, .5, 0, 1, .5, 0)));

		this.shape.set(0, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(createSegment(0, 0, 0, 1, 1, 0)));

		this.shape.set(0, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(createSegment(0, 0, 0, 2, 2, 0)));

		this.shape.set(0, 0, 0, 1, 1, 0);
		assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, .5, .5, 0));

		this.shape.set(0, 0, 0, 1, 1, 0);
		assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, .5, .5, 0));

		this.shape.set(0, 0, 0, 1, 1, 0);
		assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, 0, 0, 0));

		this.shape.set(0, 0, 0, 1, 1, 0);
		assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, -3, 0, -1, -1, 0));

		this.shape.set(0, 0, 0, 1, 1, 0);
		assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, 4, 0, 0));
		
		this.shape.set(0, 0, 0, 1, 1, 0);
		assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, -1, 0, 4, -1, 0));

		this.shape.set(0, 0, 0, 1, 1, 0);
		assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, -1, 0, -1, -1, 0));

		this.shape.set(0, 0, 0, 1, 1, 0);
		assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				-3, 0, 0, -2, 1, 0));

		this.shape.set(0, 0, 0, 1, 1, 0);
		assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
				0, 0, 0, 1, 1, 0,
				10, 0, 0, 9, -1, 0));

		assertTrue(
				Segment3afp.intersectsSegmentSegmentWithEnds(
						7, -5, 0, 1, 1, 0,
						4, -3, 0, 1, 1, 0));
	}

	@DisplayName("intersects(Path3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
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

	@DisplayName("intersects(PathIterator3afp)")
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsPathIterator3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp<?, ?, ?, ?, ?, ?, B> p;

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

	@DisplayName("intersects(Shape3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape3D) createSphere(0, 0, 0, 1)));
	}

	@DisplayName("s += Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(3.4, 4.5, 1.3));
		assertEpsilonEquals(3.4, this.shape.getX1());
		assertEpsilonEquals(4.5, this.shape.getY1());
		assertEpsilonEquals(1.3, this.shape.getZ1());
		assertEpsilonEquals(4.4, this.shape.getX2());
		assertEpsilonEquals(5.5, this.shape.getY2());
		assertEpsilonEquals(2.3, this.shape.getZ2());
	}

	@DisplayName("s + Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_plus(createVector(3.4, 4.5, 1.3));
		assertNotSame(shape, this.shape);
		assertEpsilonEquals(3.4, shape.getX1());
		assertEpsilonEquals(4.5, shape.getY1());
		assertEpsilonEquals(1.3, shape.getZ1());
		assertEpsilonEquals(4.4, shape.getX2());
		assertEpsilonEquals(5.5, shape.getY2());
		assertEpsilonEquals(2.3, shape.getZ2());
	}

	@DisplayName("s -= Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(3.4, 4.5, 1.3));
		assertEpsilonEquals(-3.4, this.shape.getX1());
		assertEpsilonEquals(-4.5, this.shape.getY1());
		assertEpsilonEquals(-1.3, this.shape.getZ1());
		assertEpsilonEquals(-2.4, this.shape.getX2());
		assertEpsilonEquals(-3.5, this.shape.getY2());
		assertEpsilonEquals(-.3, this.shape.getZ2());
	}

	@DisplayName("s - Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_minus(createVector(3.4, 4.5, 1.3));
		assertEpsilonEquals(-3.4, shape.getX1());
		assertEpsilonEquals(-4.5, shape.getY1());
		assertEpsilonEquals(-1.3, shape.getZ1());
		assertEpsilonEquals(-2.4, shape.getX2());
		assertEpsilonEquals(-3.5, shape.getY2());
		assertEpsilonEquals(-.3, shape.getZ2());
	}

	@DisplayName("s * Transform3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_multiplyTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;
		Transform3D tr;

		tr = new Transform3D();    	
		s = (Segment3afp) this.shape.operator_multiply(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(1, s.getX2());
		assertEpsilonEquals(1, s.getY2());
		assertEpsilonEquals(1, s.getZ2());

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4, 4.5, 1.3);
		s = (Segment3afp) this.shape.operator_multiply(tr);
		assertEpsilonEquals(3.4, s.getX1());
		assertEpsilonEquals(4.5, s.getY1());
		assertEpsilonEquals(1.3, s.getZ1());
		assertEpsilonEquals(4.4, s.getX2());
		assertEpsilonEquals(5.5, s.getY2());
		assertEpsilonEquals(2.3, s.getZ2());

		tr = new Transform3D();
		tr.makeRotationMatrix(newAxisAngleZ(MathConstants.PI));
		s = (Segment3afp) this.shape.operator_multiply(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(-1, s.getX2());
		assertEpsilonEquals(-1, s.getY2());
		assertEpsilonEquals(1, s.getZ2());

		tr = new Transform3D();
		tr.makeRotationMatrix(newAxisAngleZ(MathConstants.QUARTER_PI));
		s = (Segment3afp) this.shape.operator_multiply(tr);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(0, s.getX2());
		assertEpsilonEquals(1.41421356, s.getY2());
		assertEpsilonEquals(1, s.getZ2());
	}

	@DisplayName("s && Point3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_andPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createPoint(0, 0, 0)));
		assertTrue(this.shape.operator_and(createPoint(.5, .5, .5)));
		assertTrue(this.shape.operator_and(createPoint(1, 1, 1)));
		assertFalse(this.shape.operator_and(createPoint(2.3, 4.5, 0)));
		assertFalse(this.shape.operator_and(createPoint(2, 2, 0)));
	}

	@DisplayName("s && Shape3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createSphere(0, 0, 0, 1)));
	}

	@DisplayName("s .. Point3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_upToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(.5, .5, .5)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(1, 1, 1)));

		assertEpsilonEquals(3.86522961, this.shape.operator_upTo(createPoint(2.3, 4.5, 0)));

		assertEpsilonEquals(1.7320508087, this.shape.operator_upTo(createPoint(2, 2, 0)));
	}

	@DisplayName("Issue #15")
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

	@DisplayName("calculatesLinePlaneIntersection(double,double,double,double,double,double,double,double,double,double,Point3D)")
	@Test
	public void staticCalculatesLinePlaneIntersection() {
		Point3D<?, ?, ?> result;
		
		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(Segment3afp.calculatesLinePlaneIntersection(
				-1, 4, 5, 5, -5, -6,
				0, 1, 0, -2,
				result));
		assertEpsilonEquals(createPoint(.33333333333, 2, 2.55555555555), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(Segment3afp.calculatesLinePlaneIntersection(
				-1, 4, 5, 5, -5, -6,
				0, -1, 0, -2,
				result));
		assertEpsilonEquals(createPoint(3, -2, -2.33333333333), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(Segment3afp.calculatesLinePlaneIntersection(
				-1, 4, 5, 5, -5, -6,
				1, 0, 0, -5,
				result));
		assertEpsilonEquals(createPoint(5, -5, -6), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		assertTrue(Segment3afp.calculatesLinePlaneIntersection(
				-1, 4, 5, 5, -5, -6,
				0, 0, 1, -10,
				result));
		assertEpsilonEquals(createPoint(-3.727272727272, 8.0909090909, 10), result);
	}

	@DisplayName("clipToBox(double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void clipToBoxDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		this.shape.set(-1, 4, 5, 5, -5, -6);

		assertTrue(this.shape.clipToBox(0, 0, 0, 2, 2, 2));
		assertEpsilonEquals(createPoint(.636363636363, 1.545454545454, 2), this.shape.getP1());
		assertEpsilonEquals(createPoint(1.666666666666, 0, 0.11111111111), this.shape.getP2());
	}

	@DisplayName("getDirection")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDirection(CoordinateSystem3D cs) {
		Vector3D<?, ?, ?> v;
		
		v = this.shape.getDirection();
		assertEpsilonEquals(new Vector3d(.577350269, .577350269, .577350269), v);
	}

	@DisplayName("getSegmentVector")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getSegmentVector(CoordinateSystem3D cs) {
		Vector3D<?, ?, ?> v;
		
		v = this.shape.getSegmentVector();
		assertEpsilonEquals(new Vector3d(1, 1, 1), v);
	}

	@DisplayName("rotate(double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void rotateDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;
		Quaternion4d q;

		q = newAxisAngleZ(MathConstants.PI);
		s = (Segment3afp) this.shape.clone();
		s.rotate(q.getX(), q.getY(), q.getZ(), q.getW());
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(-1, s.getX2());
		assertEpsilonEquals(-1, s.getY2());
		assertEpsilonEquals(1, s.getZ2());

		q = newAxisAngleZ(MathConstants.QUARTER_PI);
		s = (Segment3afp) this.shape.clone();
		s.rotate(q.getX(), q.getY(), q.getZ(), q.getW());
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(0, s.getX2());
		assertEpsilonEquals(1.41421356, s.getY2());
		assertEpsilonEquals(1, s.getZ2());
	}

	@DisplayName("rotate(Quaternion)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void rotateQuaternion(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;
		Quaternion4d q;

		q = newAxisAngleZ(MathConstants.PI);
		s = (Segment3afp) this.shape.clone();
		s.rotate(q);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(-1, s.getX2());
		assertEpsilonEquals(-1, s.getY2());
		assertEpsilonEquals(1, s.getZ2());

		q = newAxisAngleZ(MathConstants.QUARTER_PI);
		s = (Segment3afp) this.shape.clone();
		s.rotate(q);
		assertEpsilonEquals(0, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(0, s.getX2());
		assertEpsilonEquals(1.41421356, s.getY2());
		assertEpsilonEquals(1, s.getZ2());
	}

	@DisplayName("rotate(double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void rotateDoubleDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;
		Quaternion4d q;

		q = newAxisAngleZ(MathConstants.PI);
		s = (Segment3afp) this.shape.clone();
		s.rotate(q.getX(), q.getY(), q.getZ(), q.getW(), 1, 0, 1);
		assertEpsilonEquals(2, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(1, s.getX2());
		assertEpsilonEquals(-1, s.getY2());
		assertEpsilonEquals(1, s.getZ2());

		q = newAxisAngleZ(MathConstants.QUARTER_PI);
		s = (Segment3afp) this.shape.clone();
		s.rotate(q.getX(), q.getY(), q.getZ(), q.getW(), 1, 0, 1);
		assertEpsilonEquals(.2929, s.getX1());
		assertEpsilonEquals(-.7071, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(.2929, s.getX2());
		assertEpsilonEquals(.7071, s.getY2());
		assertEpsilonEquals(1, s.getZ2());
	}

	@DisplayName("rotate(Quaternion,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void rotateQuaternionPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;
		Quaternion4d q;

		q = newAxisAngleZ(MathConstants.PI);
		s = (Segment3afp) this.shape.clone();
		s.rotate(q, createPoint(1, 0, 1));
		assertEpsilonEquals(2, s.getX1());
		assertEpsilonEquals(0, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(1, s.getX2());
		assertEpsilonEquals(-1, s.getY2());
		assertEpsilonEquals(1, s.getZ2());

		q = newAxisAngleZ(MathConstants.QUARTER_PI);
		s = (Segment3afp) this.shape.clone();
		s.rotate(q, createPoint(1, 0, 1));
		assertEpsilonEquals(.2929, s.getX1());
		assertEpsilonEquals(-.7071, s.getY1());
		assertEpsilonEquals(0, s.getZ1());
		assertEpsilonEquals(.2929, s.getX2());
		assertEpsilonEquals(.7071, s.getY2());
		assertEpsilonEquals(1, s.getZ2());
	}

	@DisplayName("calcultesDistanceSquaredSegmentSegment(double,double,double,double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void calcultesDistanceSquaredSegmentSegmentDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(25., Segment3afp.calculatesDistanceSquaredSegmentSegment(
				this.shape.getX1(), this.shape.getY1(), this.shape.getZ1(),
				this.shape.getX2(), this.shape.getY2(), this.shape.getZ2(),
				1, 1, 6, 100, 100, 100));

		assertEpsilonEquals(3., Segment3afp.calculatesDistanceSquaredSegmentSegment(
				this.shape.getX1(), this.shape.getY1(), this.shape.getZ1(),
				this.shape.getX2(), this.shape.getY2(), this.shape.getZ2(),
				-1, -1, -1, -100, -100, -100));

		assertEpsilonEquals(0., Segment3afp.calculatesDistanceSquaredSegmentSegment(
				this.shape.getX1(), this.shape.getY1(), this.shape.getZ1(),
				this.shape.getX2(), this.shape.getY2(), this.shape.getZ2(),
				-1, 1, 0, 1, -1, 0));

		assertEpsilonEquals(0., Segment3afp.calculatesDistanceSquaredSegmentSegment(
				this.shape.getX1(), this.shape.getY1(), this.shape.getZ1(),
				this.shape.getX2(), this.shape.getY2(), this.shape.getZ2(),
				0, 1, 0, 1, 0, 1));

		assertEpsilonEquals(2028., Segment3afp.calculatesDistanceSquaredSegmentSegment(
				this.shape.getX1(), this.shape.getY1(), this.shape.getZ1(),
				this.shape.getX2(), this.shape.getY2(), this.shape.getZ2(),
				-27, -27, -27, -26, -26, -26));
	}

	@DisplayName("calcultesDistanceSegmentSegment(double,double,double,double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void calcultesDistanceSegmentSegmentDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(5., Segment3afp.calculatesDistanceSegmentSegment(
				this.shape.getX1(), this.shape.getY1(), this.shape.getZ1(),
				this.shape.getX2(), this.shape.getY2(), this.shape.getZ2(),
				1, 1, 6, 100, 100, 100));

		assertEpsilonEquals(1.732050808, Segment3afp.calculatesDistanceSegmentSegment(
				this.shape.getX1(), this.shape.getY1(), this.shape.getZ1(),
				this.shape.getX2(), this.shape.getY2(), this.shape.getZ2(),
				-1, -1, -1, -100, -100, -100));

		assertEpsilonEquals(0., Segment3afp.calculatesDistanceSegmentSegment(
				this.shape.getX1(), this.shape.getY1(), this.shape.getZ1(),
				this.shape.getX2(), this.shape.getY2(), this.shape.getZ2(),
				-1, 1, 0, 1, -1, 0));

		assertEpsilonEquals(0., Segment3afp.calculatesDistanceSegmentSegment(
				this.shape.getX1(), this.shape.getY1(), this.shape.getZ1(),
				this.shape.getX2(), this.shape.getY2(), this.shape.getZ2(),
				0, 1, 0, 1, 0, 1));

		assertEpsilonEquals(45.033320997, Segment3afp.calculatesDistanceSegmentSegment(
				this.shape.getX1(), this.shape.getY1(), this.shape.getZ1(),
				this.shape.getX2(), this.shape.getY2(), this.shape.getZ2(),
				-27, -27, -27, -26, -26, -26));
	}

}
