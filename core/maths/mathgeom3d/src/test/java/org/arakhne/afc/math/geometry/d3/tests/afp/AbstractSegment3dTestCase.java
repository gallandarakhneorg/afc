/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d3.tests.afp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.base.GeomConstants;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.MultiShape3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Shape3D;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp.LineIntersection;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp.SegmentIntersection;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.arakhne.afc.math.geometry.d3.general.Shape3DType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractSegment3dTestCase<T extends Segment3afp<?, T, ?, ?, ?, ?, B>,
			B extends AlignedBox3afp<?, ?, ?, ?, ?, B>> extends AbstractShape3dTestCase<T, B> {

	@Override
	protected final T createShape() {
		return (T) createSegment(0, 0, 0, 1, 1, 1);
	}

	@DisplayName("getType")
	@Nested
	public class GetType {

		@DisplayName("()")
		@Test
		public final void getType() {
			assertSame(Shape3DType.SEGMENT, getS().getType());
		}
	
		@DisplayName("(Class)")
		@Test
		public final void getType_Class() {
			assertSame(Shape3DType.SEGMENT, getS().getType(Shape3DType.class));
		}
	}

	@DisplayName("findsClosestPointToPoint")
	@Nested
	public class FindsClosestPointToPoint {
		
		private Point3D result;

		@BeforeEach
		public void setUp() {
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToPoint(0, 0, 0, 1, 1, 1, 0, 0, 0, result);
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToPoint(0, 0, 0, 1, 1, 1, .75, .75, .75, result);
			assertEpsilonEquals(createPoint(.75, .75, .75), result);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToPoint(0, 0, 0, 1, 1, 1, -10, -50, 0, result);
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToPoint(0, 0, 0, 1, 1, 1, 200, -50, 0, result);
			assertEpsilonEquals(createPoint(1,1,1), result);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToPoint(0, 0, 0, 1, 1, 1, 0, 1, 0, result);
			assertEpsilonEquals(createPoint(.3333333333, .3333333333, .3333333333), result);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToPoint(0, 0, 5, 1, -2, 1, 0, 1, 0, result);
			assertEpsilonEquals(createPoint(0.857142857142, -1.7142857142857142, 1.5714285714285716), result);
		}

	}

	@DisplayName("getClosestPointTo")
	@Nested
	public class GetClosestPointTo {
		
		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// box contains segment start point
			var p = getS().getClosestPointTo(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1));
			assertEpsilonEquals(createPoint(.5, .5, .5), p);
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// far positive box -> closest on segment is end point
			var p = getS().getClosestPointTo(createAlignedBoxFromPoints(10, 10, 10, 12, 12, 12));
			assertEpsilonEquals(createPoint(1, 1, 1), p);
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// far negative box -> closest on segment is start point
			var p = getS().getClosestPointTo(createAlignedBoxFromPoints(-3, -3, -3, -1, -1, -1));
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// box around the middle of the segment
			var p = getS().getClosestPointTo(createAlignedBoxFromPoints(0.4, 0.4, 0.4, 0.6, 0.6, 0.6));
			assertEpsilonEquals(createPoint(0.5, 0.5, 0.5), p);
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// box touching end point
			var p = getS().getClosestPointTo(createAlignedBoxFromPoints(1, 1, 1, 1.2, 1.2, 1.2));
			assertEpsilonEquals(createPoint(1, 1, 1), p);
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// box near start point but not containing it
			var p = getS().getClosestPointTo(createAlignedBoxFromPoints(-0.3, -0.3, -0.3, -0.1, -0.1, -0.1));
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// box offset from line, closest point is interior projection t=0.5
			var p = getS().getClosestPointTo(createAlignedBoxFromPoints(0.5, 0.5, 0.9, 0.5, 0.5, 0.9));
			assertEpsilonEquals(createPoint(0.633333333333, 0.633333333333, 0.633333333333), p);
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// one-point box exactly on segment
			var p = getS().getClosestPointTo(createAlignedBoxFromPoints(0.25, 0.25, 0.25, 0.25, 0.25, 0.25));
			assertEpsilonEquals(createPoint(0.25, 0.25, 0.25), p);	
		}
	
		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// identical segment -> zero distance, midpoint is a valid closest point
			var p = getS().getClosestPointTo(createSegment(0, 0, 0, 1, 1, 1));
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}
		
		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// parallel shifted segment -> closest point can be any projection; midpoint is expected by implementation
			var p = getS().getClosestPointTo(createSegment(0, 1, 0, 1, 2, 1));
			assertEpsilonEquals(createPoint(0.333333333333, 0.333333333333, 0.333333333333), p);
		}
		
		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// segment entirely in positive octant, far from end -> closest should be end point
			var p = getS().getClosestPointTo(createSegment(2, 2, 2, 3, 3, 3));
			assertEpsilonEquals(createPoint(1, 1, 1), p);
		}
		
		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// segment entirely in negative octant, far from start -> closest should be start point
			var p = getS().getClosestPointTo(createSegment(-3, -3, -3, -1, -1, -1));
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}
		
		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// orthogonal crossing through midpoint
			var p = getS().getClosestPointTo(createSegment(.5, .5, -2, .5, .5, 2));
			assertEpsilonEquals(createPoint(.5, .5, .5), p);
		}
		
		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// touches at start point
			var p = getS().getClosestPointTo(createSegment(0, 0, 0, 0, -1, 0));
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}
		
		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// touches at end point
			var p = getS().getClosestPointTo(createSegment(1, 1, 1, 2, 1, 1));
			assertEpsilonEquals(createPoint(1, 1, 1), p);
		}
		
		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// degenerate other segment (single point) near interior projection t=0.2
			var p = getS().getClosestPointTo(createSegment(.2, .2, .2, .2, .2, .2));
			assertEpsilonEquals(createPoint(.2, .2, .2), p);
		}
		
		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// degenerate other segment (single point) away from line -> projection then clamp
			var p = getS().getClosestPointTo(createSegment(.5, .5, .9, .5, .5, .9));
			assertEpsilonEquals(createPoint(.633333333333, .633333333333, .633333333333), p);
		}
		
		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// sphere centered at segment start, radius 1
			var p = getS().getClosestPointTo(createSphere(0, 0, 0, 1));
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}
		
		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// sphere centered at segment end
			var p = getS().getClosestPointTo(createSphere(1, 1, 1, 0.5));
			assertEpsilonEquals(createPoint(1, 1, 1), p);
		}
		
		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// center on segment interior
			var p = getS().getClosestPointTo(createSphere(0.5, 0.5, 0.5, 0.25));
			assertEpsilonEquals(createPoint(0.5, 0.5, 0.5), p);
		}
		
		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// center outside line, orthogonal projection falls on segment (t = 0.5)
			var p = getS().getClosestPointTo(createSphere(0.5, 0.5, 1.0, 0.2));
			assertEpsilonEquals(createPoint(0.66666666666667, 0.66666666666667, 0.66666666666667), p);
		}
		
		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// far in positive direction -> closest endpoint is (1,1,1)
			var p = getS().getClosestPointTo(createSphere(4, 4, 4, 1));
			assertEpsilonEquals(createPoint(1, 1, 1), p);
		}
		
		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// far in negative direction -> closest endpoint is (0,0,0)
			var p = getS().getClosestPointTo(createSphere(-3, -3, -3, 2));
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}
		
		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// tangent-like case near start, still clamped to start
			var p = getS().getClosestPointTo(createSphere(-0.2, -0.2, -0.2, 0.1));
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}
		
		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// big sphere enclosing the whole segment: center projection on segment
			var p = getS().getClosestPointTo(createSphere(0.2, 0.2, 0.2, 10));
			assertEpsilonEquals(createPoint(0.2, 0.2, 0.2), p);
		}
		
		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// sphere centered at segment start, radius 1
			var pt = createPath()
					.moveTo(0, 0, 0)
					.lineTo(1, 1, 1);
			var p = getS().getClosestPointTo(pt);
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}
		
		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// identical segment but reversed order
			var pt = createPath()
					.moveTo(1, 1, 1)
					.lineTo(0, 0, 0);
			var p = getS().getClosestPointTo(pt);
			assertEpsilonEquals(createPoint(1, 1, 1), p);
		}
		
		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// polyline far in positive octant -> closest at segment end
			var pt = createPath()
					.moveTo(3, 3, 3)
					.lineTo(4, 4, 4)
					.lineTo(5, 5, 5);
			var p = getS().getClosestPointTo(pt);
			assertEpsilonEquals(createPoint(1, 1, 1), p);
		}
		
		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// polyline far in negative octant -> closest at segment start
			var pt = createPath()
					.moveTo(-5, -5, -5)
					.lineTo(-4, -4, -4)
					.lineTo(-3, -3, -3);
			var p = getS().getClosestPointTo(pt);
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}
		
		@DisplayName("(Path3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// orthogonal crossing through segment midpoint
			var pt = createPath()
					.moveTo(.5, .5, -2)
					.lineTo(.5, .5, 2);
			var p = getS().getClosestPointTo(pt);
			assertEpsilonEquals(createPoint(.5, .5, .5), p);
		}
		
		@DisplayName("(Path3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// closest point at start due to polyline touching start
			var pt = createPath()
					.moveTo(0, 0, 0)
					.lineTo(0, -1, 0)
					.lineTo(0, -2, 0);
			var p = getS().getClosestPointTo(pt);
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}
		
		@DisplayName("(Path3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// closest point at end due to polyline touching end
			var pt = createPath()
					.moveTo(1, 1, 1)
					.lineTo(2, 1, 1)
					.lineTo(2, 2, 1);
			var p = getS().getClosestPointTo(pt);
			assertEpsilonEquals(createPoint(1, 1, 1), p);
		}
		
		@DisplayName("(Path3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// skew polyline near the middle; projection should be interior
			var pt = createPath()
					.moveTo(.5, .5, .9)
					.lineTo(.5, .8, .9)
					.lineTo(.8, .8, .9);
			var p = getS().getClosestPointTo(pt);
			assertEpsilonEquals(createPoint(.833333333333, .833333333333, .833333333333), p);
		}
		
		@DisplayName("(Path3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// polyline with multiple segments, nearest occurs on second segment
			var pt = createPath()
					.moveTo(2, 0, 0)
					.lineTo(2, 0, 2)
					.lineTo(0.2, 0.2, 0.2)
					.lineTo(0.2, 1.2, 0.2);
			var p = getS().getClosestPointTo(pt);
			assertEpsilonEquals(createPoint(0.2, 0.2, 0.2), p);
		}
		
		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp shp = createMultiShape();
			shp.add(createSphere(-3, -3, -3, 2));
			shp.add(createSegment(0, 1, 0, 1, 2, 1));
			var p = getS().getClosestPointTo(shp);
			assertEpsilonEquals(createPoint(0.3333333333333, 0.3333333333333, 0.3333333333333), p);
		}
		
		@DisplayName("(Shape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shp = createAlignedBoxFromPoints(0.5, 0.5, 0.9, 0.5, 0.5, 0.9);
			var p = getS().getClosestPointTo(shp);
			assertEpsilonEquals(createPoint(0.633333333333, 0.633333333333, 0.633333333333), p);
		}
		
		@DisplayName("(Shape3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shp = createSegment(0, 1, 0, 1, 2, 1);
			var p = getS().getClosestPointTo(shp);
			assertEpsilonEquals(createPoint(0.333333333333, 0.333333333333, 0.333333333333), p);
		}
		
		@DisplayName("(Shape3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shp = createSphere(0.5, 0.5, 1.0, 0.2);
			var p = getS().getClosestPointTo(shp);
			assertEpsilonEquals(createPoint(0.66666666666667, 0.66666666666667, 0.66666666666667), p);
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(0, 0, 0));
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
			assertEpsilonEquals(0, p.getZ());
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(.5, .5, .5));
			assertEpsilonEquals(.5, p.getX());
			assertEpsilonEquals(.5, p.getY());
			assertEpsilonEquals(.5, p.getZ());
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(1, 1, 1));
			assertEpsilonEquals(1, p.getX());
			assertEpsilonEquals(1, p.getY());
			assertEpsilonEquals(1, p.getZ());
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(2, 2, 0));
			assertEpsilonEquals(1, p.getX());
			assertEpsilonEquals(1, p.getY());
			assertEpsilonEquals(1, p.getZ());
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(-2, 2, 0));
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
			assertEpsilonEquals(0, p.getZ());
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(0.1, 1.2, .5));
			assertEpsilonEquals(0.6, p.getX());
			assertEpsilonEquals(0.6, p.getY());
			assertEpsilonEquals(0.6, p.getZ());
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().getClosestPointTo(createPoint(10.1, -.2, 0));
			assertEpsilonEquals(1, p.getX());
			assertEpsilonEquals(1, p.getY());
			assertEpsilonEquals(1, p.getZ());
		}
	}

	@DisplayName("findsClosestPointToSegment")
	@Nested
	public class FindsClosestPointToSegment {

		private InnerComputationPoint3D r1;
		
		private InnerComputationPoint3D r2;

		@BeforeEach
		public void setUp() {
			r1 = new InnerComputationPoint3D();
			r2 = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToSegment(
					0, 0, 0, 1, 0.5, -5,
					-2, 1, -5, 10, 10, 10,
					r1, r2);
			assertEpsilonEquals(createPoint(0.63287744704,0.316438723518,-3.16438723518), r1);
			assertEpsilonEquals(createPoint(-0.5872888174,2.059533387,-3.23411102172), r2);
		}

		@DisplayName("#2 - intersecting segments (same closest point)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToSegment(
					-1, 0, 0, 1, 0, 0,
					0, -1, 0, 0, 1, 0,
					r1, r2);
			assertEpsilonEquals(createPoint(0, 0, 0), r1);
			assertEpsilonEquals(createPoint(0, 0, 0), r2);
		}

		@DisplayName("#3 - skew orthogonal supports")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToSegment(
					-2, 0, 0, 2, 0, 0,
					0, -2, 1, 0, 2, 1,
					r1, r2);
			assertEpsilonEquals(createPoint(0, 0, 0), r1);
			assertEpsilonEquals(createPoint(0, 0, 1), r2);
		}

		@DisplayName("#4 - parallel separated segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToSegment(
					0, 0, 0, 10, 0, 0,
					0, 3, 4, 10, 3, 4,
					r1, r2);
			assertEpsilonEquals(createPoint(0, 0, 0), r1);
			assertEpsilonEquals(createPoint(0, 3, 4), r2);
		}

		@DisplayName("#5 - colinear overlapping segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToSegment(
					0, 0, 0, 4, 0, 0,
					2, 0, 0, 6, 0, 0,
					r1, r2);
			// Any point in overlap [2,4] is valid.
			assertEpsilonEquals(createPoint(2, 0, 0), r1);
			assertEpsilonEquals(createPoint(2, 0, 0), r2);
		}

		@DisplayName("#6 - colinear disjoint segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToSegment(
					0, 0, 0, 1, 0, 0,
					3, 0, 0, 4, 0, 0,
					r1, r2);
			assertEpsilonEquals(createPoint(1, 0, 0), r1);
			assertEpsilonEquals(createPoint(3, 0, 0), r2);
		}

		@DisplayName("#7 - endpoint to interior projection")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToSegment(
					0, 0, 0, 1, 0, 0,
					3, 4, 0, 3, 5, 0,
					r1, r2);
			assertEpsilonEquals(createPoint(1, 0, 0), r1);
			assertEpsilonEquals(createPoint(3, 4, 0), r2);
		}

		@DisplayName("#8 - first segment degenerate (point)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToSegment(
					0, 3, 4, 0, 3, 4,
					-5, 0, 0, 5, 0, 0,
					r1, r2);
			assertEpsilonEquals(createPoint(0, 3, 4), r1);
			assertEpsilonEquals(createPoint(0, 0, 0), r2);
		}

		@DisplayName("#9 - second segment degenerate (point)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToSegment(
					-5, 0, 0, 5, 0, 0,
					0, 3, 4, 0, 3, 4,
					r1, r2);
			assertEpsilonEquals(createPoint(0, 0, 0), r1);
			assertEpsilonEquals(createPoint(0, 3, 4), r2);
		}

		@DisplayName("#10 - both segments degenerate (two points)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToSegment(
					1, 2, 3, 1, 2, 3,
					4, 6, 3, 4, 6, 3,
					r1, r2);
			assertEpsilonEquals(createPoint(1, 2, 3), r1);
			assertEpsilonEquals(createPoint(4, 6, 3), r2);
		}

		@DisplayName("#11 - symmetry when swapping segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);

			Segment3afp.findsClosestPointToSegment(
					-2, 0, 0, 2, 0, 0,
					0, -2, 1, 0, 2, 1,
					r1, r2);
			final var a = createPoint(r1.getX(), r1.getY(), r1.getZ());
			final var b = createPoint(r2.getX(), r2.getY(), r2.getZ());

			Segment3afp.findsClosestPointToSegment(
					0, -2, 1, 0, 2, 1,
					-2, 0, 0, 2, 0, 0,
					r1, r2);

			assertEpsilonEquals(b, r1);
			assertEpsilonEquals(a, r2);
		}

		@DisplayName("#12 - colinear overlapping segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			// Derived from #5 with reversed second segment
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToSegment(
					0, 0, 0, 4, 0, 0,
					6, 0, 0, 2, 0, 0,
					r1, r2);
			// Any point in overlap [2,4] is valid.
			assertEpsilonEquals(createPoint(2, 0, 0), r1);
			assertEpsilonEquals(createPoint(2, 0, 0), r2);
		}

		@DisplayName("#13 - colinear overlapping segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			// Derived from #5 with reversed first segment
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToSegment(
					4, 0, 0, 0, 0, 0,
					2, 0, 0, 6, 0, 0,
					r1, r2);
			// Any point in overlap [2,4] is valid.
			assertEpsilonEquals(createPoint(4, 0, 0), r1);
			assertEpsilonEquals(createPoint(4, 0, 0), r2);
		}

		@DisplayName("#14 - colinear overlapping segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			// Derived from #5 with reversed both segments
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsClosestPointToSegment(
					4, 0, 0, 0, 0, 0,
					6, 0, 0, 2, 0, 0,
					r1, r2);
			// Any point in overlap [2,4] is valid.
			assertEpsilonEquals(createPoint(4, 0, 0), r1);
			assertEpsilonEquals(createPoint(4, 0, 0), r2);
		}

	}

	@DisplayName("findsFarthestPointToPoint")
	@Nested
	public class FindsFarthestPointToPoint {

		private InnerComputationPoint3D p;

		@BeforeEach
		public void setUp() {
			p = new InnerComputationPoint3D();
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsFarthestPointToPoint(0, 0, 0, 1, 1, 0, 0, 0, 0, p);
			assertEpsilonEquals(createPoint(1, 1, 0), p);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsFarthestPointToPoint(0, 0, 0, 1, 1, 0, .5, .5, 0, p);
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsFarthestPointToPoint(0, 0, 0, 1, 1, 0, 1, 1, 0, p);
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsFarthestPointToPoint(0, 0, 0, 1, 1, 0, 2, 2, 0, p);
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsFarthestPointToPoint(0, 0, 0, 1, 1, 0, -2, 2, 0, p);
			assertEpsilonEquals(createPoint(1, 1, 0), p);
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsFarthestPointToPoint(0, 0, 0, 1, 1, 0, 0.1, 1.2, 0, p);
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}
		
		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsFarthestPointToPoint(0, 0, 0, 1, 1, 0, 10.1, -.2, 0, p);
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}

		@DisplayName("#8 - non-zero Z, point at first endpoint => farthest is second endpoint")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsFarthestPointToPoint(1, 2, 3, 4, 6, 9, 1, 2, 3, p);
			assertEpsilonEquals(createPoint(4, 6, 9), p);
		}

		@DisplayName("#9 - non-zero Z, point at second endpoint => farthest is first endpoint")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsFarthestPointToPoint(1, 2, 3, 4, 6, 9, 4, 6, 9, p);
			assertEpsilonEquals(createPoint(1, 2, 3), p);
		}

		@DisplayName("#10 - point strictly closer to first endpoint => farthest is second")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// d(P,A)^2 = 1, d(P,B)^2 = 26
			Segment3afp.findsFarthestPointToPoint(0, 0, 0, 0, 0, 5, 0, 0, 1, p);
			assertEpsilonEquals(createPoint(0, 0, 5), p);
		}

		@DisplayName("#11 - point strictly closer to second endpoint => farthest is first")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// d(P,A)^2 = 26, d(P,B)^2 = 1
			Segment3afp.findsFarthestPointToPoint(0, 0, 0, 0, 0, 5, 0, 0, 4, p);
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}

		@DisplayName("#12 - general 3D segment, point near first endpoint => farthest is second")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// A=(2,-1,4), B=(5,3,-2), P=(3,-1,4)
			// d(P,A)^2=1 ; d(P,B)^2=1+16+36=53
			Segment3afp.findsFarthestPointToPoint(2, -1, 4, 5, 3, -2, 3, -1, 4, p);
			assertEpsilonEquals(createPoint(5, 3, -2), p);
		}

		@DisplayName("#13 - general 3D segment, point near second endpoint => farthest is first")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// A=(2,-1,4), B=(5,3,-2), P=(4,3,-2)
			// d(P,B)^2=1 ; d(P,A)^2=4+16+36=56
			Segment3afp.findsFarthestPointToPoint(2, -1, 4, 5, 3, -2, 4, 3, -2, p);
			assertEpsilonEquals(createPoint(2, -1, 4), p);
		}

		@DisplayName("#14 - degenerate segment (A==B) => unique farthest point is A/B")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.findsFarthestPointToPoint(7, -3, 2, 7, -3, 2, 100, 50, -20, p);
			assertEpsilonEquals(createPoint(7, -3, 2), p);
		}

		@DisplayName("#15 - very large coordinates (numeric stability)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// P equals A, so farthest must be B
			Segment3afp.findsFarthestPointToPoint(
					1_000_000_000d, -1_000_000_000d, 3_000_000_000d,
					1_000_000_100d, -1_000_000_050d, 3_000_000_020d,
					1_000_000_000d, -1_000_000_000d, 3_000_000_000d,
					p);
			assertEpsilonEquals(createPoint(1_000_000_100d, -1_000_000_050d, 3_000_000_020d), p);
		}

	}

	@DisplayName("findsIntersectionLineLine")
	@Nested
	public class FindsIntersectionLineLine {

		private InnerComputationPoint3D result;

		@BeforeEach
		public void setUp() {
			result = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionLineLine(
					0, 0, 1, 2, 2, 1,
					0, 2, 1, 2, 0, 1,
					result));
			assertEpsilonEquals(createPoint(1., 1., 1.), result);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionLineLine(
					100, 50, 0, 100, 60, 0,
					90, 55, 0, 2000, 55, 0,
					result));
			assertEpsilonEquals(createPoint(100., 55., 0.), result);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.findsIntersectionLineLine(
					100, 50, 0, 100, 60, 0,
					200, 0, 0, 200, 10, 0,
					result));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionLineLine(
					100, -50, 0, 100, -60, 0,
					90, 55, 0, 2000, 55, 0,
					result));
			assertEpsilonEquals(createPoint(100, 55, 0), result);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionLineLine(
					1000, 1.5325000286102295, 0, 2500, 1.5325000286102295, 0,
					1184.001080023255, 1.6651813832907332, 0, 1200.7014393876193, 1.372326130924099, 0,
					result));
			assertEpsilonEquals(createPoint(1191.567365026, 1.53250002861, 0), result);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.findsIntersectionLineLine(
					100, 50, 1, 100, 60, 5,
					90, 55, 0, 2000, 55, -10,
					result));
		}

		@DisplayName("#7 - same line definitions (colinear)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionLineLine(
					0, 0, 0, 2, 2, 2,
					1, 1, 1, 3, 3, 3,
					result));
			// For colinear infinite intersections, many implementations return one point on both lines.
			assertEpsilonZero(Segment3afp.calculatesDistanceLinePoint(
					0, 0, 0, 2, 2, 2,
					result.getX(), result.getY(), result.getZ()));
			assertEpsilonZero(Segment3afp.calculatesDistanceLinePoint(
					1, 1, 1, 3, 3, 3,
					result.getX(), result.getY(), result.getZ()));
		}

		@DisplayName("#8 - parallel distinct lines")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.findsIntersectionLineLine(
					0, 0, 0, 1, 0, 0,
					0, 1, 0, 1, 1, 0,
					result));
		}

		@DisplayName("#9 - skew lines (non-coplanar)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.findsIntersectionLineLine(
					0, 0, 0, 1, 0, 0,
					0, 1, 1, 0, 2, 1,
					result));
		}

		@DisplayName("#10 - 3D intersection not axis-aligned")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(
					Segment3afp.findsIntersectionLineLine(
					1, 2, 3, 3, 2, 2,
					5, 0, 1, 4, 1, 1,
					result));
		}

		@DisplayName("#11 - intersection at first line first point")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionLineLine(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, -1, 2, 0,
					result));
			assertEpsilonEquals(createPoint(0., 0., 0.), result);
		}

		@DisplayName("#12 - reverse first line endpoints (invariance)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionLineLine(
					2, 2, 1, 0, 0, 1,
					0, 2, 1, 2, 0, 1,
					result));
			assertEpsilonEquals(createPoint(1., 1., 1.), result);
		}

		@DisplayName("#13 - reverse second line endpoints (invariance)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionLineLine(
					0, 0, 1, 2, 2, 1,
					2, 0, 1, 0, 2, 1,
					result));
			assertEpsilonEquals(createPoint(1., 1., 1.), result);
		}

		@DisplayName("#14 - both lines reversed (invariance)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionLineLine(
					2, 2, 1, 0, 0, 1,
					2, 0, 1, 0, 2, 1,
					result));
			assertEpsilonEquals(createPoint(1., 1., 1.), result);
		}

		@DisplayName("#15 - degenerate first line (point) lying on second line")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionLineLine(
					1, 1, 1, 1, 1, 1,
					0, 0, 0, 2, 2, 2,
					result));
			assertEpsilonEquals(createPoint(1., 1., 1.), result);
		}

		@DisplayName("#16 - degenerate first line (point) not on second line")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.findsIntersectionLineLine(
					1, 1, 2, 1, 1, 2,
					0, 0, 0, 2, 2, 2,
					result));
		}

	}

	@DisplayName("calculatesIntersectionFactorsLineLine")
	@Nested
	public class CalculatesIntersectionFactorsLineLine {

		/** Test if line-line intersection is valid.
		 *
		 * @param colinear indicates if it is expected to have the two lines colinear.
		 * @param position1 the expected position factor of the intersection point on the first geometric element. The value is in [0;1] if it is
		 *     located on the first segment.
		 * @param position2 if elements are not colinear, it is the expected position factor of the intersection point on the second geometric
		 *     element. The value is in [0;1] if it is located on the second segment.
		 *     If the elements are colinear, it is the position factor of the second point of the first geometric element. The value
		 *     is in [0;1] if it is located on the first segment.
		 * @param position3 if the elements are colinear, it is the expected position factor of the first point of the second geometric element.
		 *     The value is in [0;1] if it is located on the first segment.
		 * @param position4 if the elements are colinear, it is the expected position factor of the second point of the second geometric element.
		 *     The value is in [0;1] if it is located on the first segment.
		 * @param intersection the intersection definition to be tested.
		 */
		protected final void assertIntersectionEpsilonEquals(boolean colinear, double position1, double position2, double position3, double position4, LineIntersection intersection) {
			assertNotNull(intersection, "Intersection must exist");
			assertEquals(colinear, intersection.colinear(), "invalid colinear flag");
			assertEpsilonEquals(new InnerComputationPoint2D(position1, position2), new InnerComputationPoint2D(intersection.position1(), intersection.position2()), "position1 or position2 is invalid");
			if (colinear) {
				assertEpsilonEquals(new InnerComputationPoint2D(position3, position4), new InnerComputationPoint2D(intersection.position3(), intersection.position4()), "position3 or position4 is invalid");
			}
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertIntersectionEpsilonEquals(false,
					.5, .00523560209,
					Double.NaN, Double.NaN,
					Segment3afp.calculatesIntersectionFactorsLineLine(
					100, 50, 0, 100, 60, 0,
					90, 55, 0, 2000, 55, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(Segment3afp.calculatesIntersectionFactorsLineLine(
					100, 50, 0, 100, 60, 0,
					200, 0, 0, 200, 10, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertIntersectionEpsilonEquals(false,
					-10.5,  .00523560209,
					Double.NaN,  Double.NaN,
					Segment3afp.calculatesIntersectionFactorsLineLine(
					100, -50, 0, 100, -60, 0,
					90, 55, 0, 2000, 55, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertIntersectionEpsilonEquals(false,
					.1277115766843605, .453061208936,
					Double.NaN,  Double.NaN,
					Segment3afp.calculatesIntersectionFactorsLineLine(
					1000, 1.5325000286102295, 0, 2500, 1.5325000286102295, 0,
					1184.001080023255, 1.6651813832907332, 0, 1200.7014393876193, 1.372326130924099, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(Segment3afp.calculatesIntersectionFactorsLineLine(
					100, 50, 1, 100, 60, 5,
					90, 55, 0, 2000, 55, -10));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertIntersectionEpsilonEquals(true,
					0.,  1.,
					2., 5.,
					Segment3afp.calculatesIntersectionFactorsLineLine(
					100, 50, 1, 100, 60, 5,
					100, 70, 9, 100, 100, 21));
		}

		@DisplayName("#7 - 3D intersection at origin")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertIntersectionEpsilonEquals(false,
					0., 0.,
					Double.NaN, Double.NaN,
					Segment3afp.calculatesIntersectionFactorsLineLine(
					0, 0, 0,  1, 1, 1,
					0, 0, 0,  1, -1, 0));
		}

		@DisplayName("#8 - 3D intersection at non-zero factors")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// L1: (5,0,1)+t*(-1,1,0), L2: (1,2,3)+u*(12,0,-12)
			// intersection for t=2, u=0.166666667 => point (3,2,1)
			assertIntersectionEpsilonEquals(false,
					2., 0.16666666666667,
					Double.NaN, Double.NaN,
					Segment3afp.calculatesIntersectionFactorsLineLine(
					5, 0, 1,  4, 1, 1,
					1, 2, 3,  13, 2, -9));
		}

		@DisplayName("#9 - skew lines (non-coplanar) => no intersection")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(Segment3afp.calculatesIntersectionFactorsLineLine(
					0, 0, 0,  1, 0, 0,
					0, 1, 1,  0, 2, 1));
		}

		@DisplayName("#10 - parallel distinct lines => no intersection")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(Segment3afp.calculatesIntersectionFactorsLineLine(
					0, 0, 0,  1, 0, 0,
					0, 1, 0,  1, 1, 0));
		}

		@DisplayName("#11 - colinear overlapping direction")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// First line support points define parameter on first element:
			// P1=(0,0,0), P2=(2,2,2) => direction (2,2,2)
			// second line endpoints at (4,4,4) and (8,8,8) => factors 2 and 4 on first line
			assertIntersectionEpsilonEquals(true,
					0., 1.,
					2., 4.,
					Segment3afp.calculatesIntersectionFactorsLineLine(
					0, 0, 0,  2, 2, 2,
					4, 4, 4,  8, 8, 8));
		}

		@DisplayName("#12 - colinear opposite direction")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// same support line, reversed second definition; factors still map on first line
			assertIntersectionEpsilonEquals(true,
					0., 1.,
					4., 2.,
					Segment3afp.calculatesIntersectionFactorsLineLine(
					0, 0, 0,  2, 2, 2,
					8, 8, 8,  4, 4, 4));
		}

		@DisplayName("#13 - colinear with negative factors on first line")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// second line points behind first origin on same support
			assertIntersectionEpsilonEquals(true,
					0., 1.,
					-3., -1.,
					Segment3afp.calculatesIntersectionFactorsLineLine(
					0, 0, 0,  1, 1, 1,
					-3, -3, -3,  -1, -1, -1));
		}

		@DisplayName("#14 - intersection with first factor in [0,1], second outside")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// intersection point belongs to first segment support at t=0.0833333,
			// but on second line at u=0.6666666666
			assertIntersectionEpsilonEquals(false,
					0.08333333333333, 0.6666666666667,
					Double.NaN, Double.NaN,
					Segment3afp.calculatesIntersectionFactorsLineLine(
					0, 0, 0,  4, 0, 0,
					-7, 1, 0,  4, -0.5, 0));
		}

		@DisplayName("#15 - same geometric intersection, first line reversed")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Derived from #8 with first point pair swapped
			assertIntersectionEpsilonEquals(false,
					-1, 0.166666666667,
					Double.NaN, Double.NaN,
					Segment3afp.calculatesIntersectionFactorsLineLine(
					4, 1, 1,  5, 0, 1,
					1, 2, 3,  13, 2, -9));
		}

		@DisplayName("#16 - same geometric intersection, second line reversed")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Derived from #8 with second point pair swapped
			assertIntersectionEpsilonEquals(false,
					2, 0.833333333333333,
					Double.NaN, Double.NaN,
					Segment3afp.calculatesIntersectionFactorsLineLine(
					5, 0, 1,  4, 1, 1,
					13, 2, -9,  1, 2, 3));
		}

		@DisplayName("#17 - same geometric intersection, both lines reversed")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Derived from #8 with both point pairs swapped
			assertIntersectionEpsilonEquals(false,
					-1, 0.833333333333333,
					Double.NaN, Double.NaN,
					Segment3afp.calculatesIntersectionFactorsLineLine(
					4, 1, 1,  5, 0, 1,
					13, 2, -9,  1, 2, 3));
		}

	}

	@DisplayName("getDistance")
	@Nested
	public class GetDistance {

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shp = createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3);
		    assertEpsilonEquals(1.732050808, getS().getDistance(shp));
		}

		@DisplayName("(Shape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void shape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    var shp = createSegment(1, 1, 0, 2, 2, 0);
			assertEpsilonEquals(0.816496581, getS().getDistance(shp));
		}

		@DisplayName("(Shape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void shape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shp = createSphere(-1, -1, -1, 1.5);
			assertEpsilonEquals(0.232050808, getS().getDistance(shp));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.0622577483, getS().getDistance(createAlignedBoxFromPoints(5, 8, 0, 10, 18, 10)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.732050808, getS().getDistance(createSegment(2, 2, 2, 5, 18, 10)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(0, getS().getDistance(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(0, getS().getDistance(createSegment(0.2, 0.2, 0.2, 0.8, 0.8, 0.8)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(0, getS().getDistance(createSegment(0, 0, 0, 0, 0, 1)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(0, getS().getDistance(createSegment(1, 1, 1, 2, 0, 0)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(0, getS().getDistance(createSegment(0, 1, 0, 1, 0, 1)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(0.816496581, getS().getDistance(createSegment(0, 0, 1, 1, 1, 2)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(1.732050808, getS().getDistance(createSegment(2, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(1.732050808, getS().getDistance(createSegment(-2, -2, -2, -1, -1, -1)));
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(0.707106781, getS().getDistance(createSegment(0, 1, 0, 1, 1, 0)));
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(1.414213562, getS().getDistance(createSegment(0, 2, 0, 0, 2, 1)));
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.732050808, getS().getDistance(createSegment(2, 2, 2, 5, 18, 10)));
		}

		@DisplayName("(Segment3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Segment3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSegment(0, 0, 0, -1, -1, -1)));
		}

		@DisplayName("(Segment3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSegment(1, 1, 1, 2, 2, 2)));
		}

		@DisplayName("(Segment3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.816496581, getS().getDistance(createSegment(1, 1, 0, 2, 2, 0)));
		}

		@DisplayName("(Segment3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSegment(0.5, 0.5, 0.5, 0.5, 0.5, -1)));
		}

		@DisplayName("(Segment3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistance(createSegment(2, 0, 0, 2, 1, 1)));
		}

		@DisplayName("(Segment3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSegment(0.05, 0.05, 0.05, 1.05, 1.05, 1.05)));
		}

		@DisplayName("(Segment3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.118033989, getS().getDistance(createSegment(2, 0.5, 0.5, 2, 1.5, 0.5)));
		}

		@DisplayName("(Segment3afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.732050808, getS().getDistance(createSegment(-1, -1, -1, -2, -2, -2)));
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.732050808, getS().getDistance(createSphere(2, 2, 2, 1)));
		}

		@DisplayName("(Sphere3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSphere(0, 0, 0, 0)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSphere(1, 1, 1, 0)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSphere(0.5, 0.5, 0.5, 0)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSphere(0.5, 0.5, 0.5, 5)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSphere(0, 0, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSphere(1, 1, 1, 1)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSphere(0.5, 0.5, 0.5, 1)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.866025404, getS().getDistance(createSphere(-0.5, -0.5, -0.5, 0)));
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSphere(0.5, 1.5, 0.5, 1)));
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSphere(0.5, 0.5, 0.5, 0.5)));
		}

		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, getS().getDistance(createSphere(4, 1, 1, 0)));
		}

		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.732050808, getS().getDistance(createSphere(-1, -1, -1, 0)));
		}

		@DisplayName("(Sphere3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSphere(0.3, 0.3, 0.3, 0.1)));
		}

		@DisplayName("(Sphere3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.632993162, getS().getDistance(createSphere(2, 0, 0, 0)));
		}

		@DisplayName("(Sphere3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.464101615, getS().getDistance(createSphere(3, 3, 3, 3)));
		}

		@DisplayName("(Sphere3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSphere(2, 2, 2, 2)));
		}

		@DisplayName("(Sphere3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.232050808, getS().getDistance(createSphere(-1, -1, -1, 1.5)));
		}

		@DisplayName("(Sphere3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.816496581, getS().getDistance(createSphere(0.5, 0.5, 1.5, 0)));
		}

		@DisplayName("(Sphere3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createSphere(0, 0, 0, 1)));
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath()
					.moveTo(createPoint(2, 2, 2))
					.lineTo(createPoint(5, 4, 3));
		    assertEpsilonEquals(1.732050808, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 2: Path containing only a point at segment start
			var path = createPath()
					.moveTo(createPoint(0, 0, 0));
			assertNaN(getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 3: Path containing only a point at segment end
			var path = createPath()
					.moveTo(createPoint(1, 1, 1));
			assertNaN(getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 4: Path containing only a point at segment midpoint
			var path = createPath()
					.moveTo(createPoint(0.5, 0.5, 0.5));
			assertNaN(getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 5: Path line segment intersecting with segment
			var path = createPath()
					.moveTo(createPoint(-1, 0, 0))
					.lineTo(createPoint(2, 2, 2));
			assertEpsilonEquals(0.34299717, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 6: Path with multiple line segments, one intersecting
			var path = createPath()
					.moveTo(createPoint(3, 3, 3))
					.lineTo(createPoint(4, 4, 4))
					.lineTo(createPoint(0.5, 0.5, 0.5));
			assertEpsilonEquals(0, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 7: Path completely opposite of segment
			var path = createPath()
					.moveTo(createPoint(-2, -2, -2))
					.lineTo(createPoint(-1, -1, -1));
			assertEpsilonEquals(1.732050808, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 8: Path parallel to segment but offset
			var path = createPath()
					.moveTo(createPoint(1, 0, 0))
					.lineTo(createPoint(2, 1, 1));
			assertEpsilonEquals(0.816496581, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 9: Closed path forming a triangle around segment endpoint
			var path = createPath()
					.moveTo(createPoint(1, 1, 1))
					.lineTo(createPoint(2, 1, 1))
					.lineTo(createPoint(1.5, 2, 1))
					.closePath();
			assertEpsilonEquals(0, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 10: Closed rectangular path containing segment
			var path = createPath()
					.moveTo(createPoint(-1, -1, -1))
					.lineTo(createPoint(2, -1, -1))
					.lineTo(createPoint(2, 2, 2))
					.lineTo(createPoint(-1, 2, 2))
					.closePath();
			assertEpsilonEquals(1, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 11: Path with quadratic curve passing through segment
			var path = createPath()
					.moveTo(createPoint(-1, -1, -1))
					.quadTo(createPoint(0.5, 0.5, 0.5), createPoint(2, 2, 2));
			assertEpsilonEquals(0, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 12: Path with quadratic curve not intersecting segment
			var path = createPath()
					.moveTo(createPoint(3, 0, 0))
					.quadTo(createPoint(4, 1, 0), createPoint(5, 2, 0));
			assertEpsilonEquals(2.449493007, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 13: Path with cubic bezier curve passing near segment
			var path = createPath()
					.moveTo(createPoint(-1, -1, -1))
					.curveTo(createPoint(0, 0, 0), createPoint(1, 1, 1), createPoint(2, 2, 2));
			assertEpsilonEquals(0, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 14: Path with cubic bezier curve away from segment
			var path = createPath()
					.moveTo(createPoint(3, 3, 3))
					.curveTo(createPoint(4, 4, 4), createPoint(5, 5, 5), createPoint(6, 6, 6));
			assertEpsilonEquals(3.464101615, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 15: Complex path with multiple segments and curves
			var path = createPath()
					.moveTo(createPoint(2, 2, 2))
					.lineTo(createPoint(3, 3, 3))
					.quadTo(createPoint(2, 2, 2), createPoint(1, 1, 1))
					.curveTo(createPoint(0.5, 0.5, 0.5), createPoint(0.25, 0.25, 0.25), createPoint(0, 0, 0));
			assertEpsilonEquals(0, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 16: Path with Z-axis variation
			var path = createPath()
					.moveTo(createPoint(0, 0, 2))
					.lineTo(createPoint(1, 1, 2));
			assertEpsilonEquals(1, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 17: Path skew in 3D space
			var path = createPath()
					.moveTo(createPoint(2, 0, 0))
					.lineTo(createPoint(2, 0, 2));
			assertEpsilonEquals(1.414213562, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 18: Closed diamond path with segment endpoint at vertex
			var path = createPath()
					.moveTo(createPoint(1, 1, 1))
					.lineTo(createPoint(2, 1, 1))
					.lineTo(createPoint(1.5, 1.5, 1.5))
					.lineTo(createPoint(1, 2, 1))
					.closePath();
			assertEpsilonEquals(0, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 19: Path segment tangent to the segment's projection
			var path = createPath()
					.moveTo(createPoint(0.5, 1, 0))
					.lineTo(createPoint(0.5, 1, 1));
			assertEpsilonEquals(0.353553391, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 20: Closed path forming spiral around segment
			var path = createPath()
					.moveTo(createPoint(0, 0, 0))
					.quadTo(createPoint(1, 0, 0), createPoint(1, 1, 0))
					.quadTo(createPoint(1, 1, 1), createPoint(0, 1, 1))
					.closePath();
			assertEpsilonEquals(0, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 21: Path with single line segment at start point
			var path = createPath()
					.moveTo(createPoint(0, 0, 0))
					.lineTo(createPoint(0.5, 0, 0));
			assertEpsilonEquals(0, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 22: Path with quadratic curve control point on segment
			var path = createPath()
					.moveTo(createPoint(-1, -1, -1))
					.quadTo(createPoint(0.5, 0.5, 0.5), createPoint(1, 0, 0));
			assertEpsilonEquals(0.171498585, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 23: Path perpendicular to segment in XY plane
			var path = createPath()
					.moveTo(createPoint(2, 0, 0.5))
					.lineTo(createPoint(2, 2, 0.5));
			assertEpsilonEquals(1.118033989, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 24: Path with bezier curve approximating segment
			var path = createPath()
					.moveTo(createPoint(0, 0, 0))
					.curveTo(createPoint(0.33, 0.33, 0.33), createPoint(0.67, 0.67, 0.67), createPoint(1, 1, 1));
			assertEpsilonEquals(0, getS().getDistance(path));
		}

		@DisplayName("(Path3afp) #25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 25: Path forming star with segment at center vertex
			var path = createPath()
					.moveTo(createPoint(0.5, 0.5, 0.5))
					.lineTo(createPoint(1.5, 0.5, 0.5))
					.lineTo(createPoint(0.5, 0.5, 0.5))
					.lineTo(createPoint(0.5, 1.5, 0.5))
					.closePath();
			assertEpsilonEquals(0, getS().getDistance(path));
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3D shp = createMultiShape();
			shp.add(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3));
			shp.add(createSegment(1, 1, 0, 2, 2, 0));
			shp.add(createSphere(-1, -1, -1, 1.5));		
		    assertEpsilonEquals(0.232050808, getS().getDistance(shp));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createPoint(.5, .5, .5)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createPoint(1, 1, 1)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.86522951, getS().getDistance(createPoint(2.3, 4.5, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.732050807, getS().getDistance(createPoint(2, 2, 0)));
		}

	}

	@DisplayName("getDistanceSquared")
	@Nested
	public class GetDistanceSquared {

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shp = createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3);
		    assertEpsilonEquals(3, getS().getDistanceSquared(shp));
		}

		@DisplayName("(Shape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void shape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    var shp = createSegment(1, 1, 0, 2, 2, 0);
			assertEpsilonEquals(0.66666666667, getS().getDistanceSquared(shp));
		}

		@DisplayName("(Shape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void shape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var shp = createSphere(-1, -1, -1, 1.5);
			assertEpsilonEquals(0.0538475772934, getS().getDistanceSquared(shp));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(65, getS().getDistanceSquared(createAlignedBoxFromPoints(5, 8, 0, 10, 18, 10)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // Box contains the whole segment -> distance 0
		    assertEpsilonEquals(0, getS().getDistanceSquared(
		            createAlignedBoxFromPoints(-1, -1, -1, 2, 2, 2)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // Box touches the start point (0,0,0) -> distance 0
		    assertEpsilonEquals(0, getS().getDistanceSquared(
		            createAlignedBoxFromPoints(0, 0, 0, 0.5, 0.5, 0.5)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // Box touches the end point (1,1,1) -> distance 0
		    assertEpsilonEquals(0, getS().getDistanceSquared(
		            createAlignedBoxFromPoints(0.5, 0.5, 0.5, 1, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // Box is a point on the segment (0.3,0.3,0.3) -> distance 0
		    assertEpsilonEquals(0, getS().getDistanceSquared(
		            createAlignedBoxFromPoints(0.3, 0.3, 0.3, 0.3, 0.3, 0.3)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // Box lies beyond the end point, closest segment point is (1,1,1)
		    // distance² = (2-1)²+(2-1)²+(2-1)² = 3
		    assertEpsilonEquals(3, getS().getDistanceSquared(
		            createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // Box lies before the start point, closest segment point is (0,0,0)
		    // distance² = (-1-0)²+(-1-0)²+(-1-0)² = 3
		    assertEpsilonEquals(3, getS().getDistanceSquared(
		            createAlignedBoxFromPoints(-2, -2, -2, -1, -1, -1)));
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // Box offset only in x beyond the end: closest segment point (1,1,1)
		    // box point (2,1,1) diff (1,0,0) -> distance² = 1
		    assertEpsilonEquals(1, getS().getDistanceSquared(
		            createAlignedBoxFromPoints(2, 0, 0, 3, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // Box beyond end in y (point (0.5,2,0.5)): projection t=1 -> closest (1,1,1)
		    // distance² = (0.5-1)² + (2-1)² + (0.5-1)² = 0.25+1+0.25 = 1.5
		    assertEpsilonEquals(1.5, getS().getDistanceSquared(
		            createAlignedBoxFromPoints(0.5, 2, 0.5, 0.5, 2, 0.5)));
		}

		@DisplayName("(AlignedBox3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // Box closest to an interior point: point (0.5,0.5,0.8) -> t=0.6 -> closest (0.6,0.6,0.6)
		    // distance² = (0.5-0.6)² + (0.5-0.6)² + (0.8-0.6)² = 0.01+0.01+0.04 = 0.06
		    assertEpsilonEquals(0.06, getS().getDistanceSquared(
		            createAlignedBoxFromPoints(0.5, 0.5, 0.8, 0.5, 0.5, 0.8)));
		}

		@DisplayName("(AlignedBox3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void alignedbox_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // Another interior case: point (0.2,0.5,0.8) -> t=0.5 -> closest (0.5,0.5,0.5)
		    // distance² = (0.2-0.5)² + (0.5-0.5)² + (0.8-0.5)² = 0.09+0+0.09 = 0.18
		    assertEpsilonEquals(0.18, getS().getDistanceSquared(
		            createAlignedBoxFromPoints(0.2, 0.5, 0.8, 0.2, 0.5, 0.8)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, getS().getDistanceSquared(createSegment(2, 2, 2, 5, 18, 10)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(0.2, 0.2, 0.2, 0.8, 0.8, 0.8)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(0, 0, 0, 0, 0, 1)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(1, 1, 1, 2, 0, 0)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(0, 1, 0, 1, 0, 1)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(2.0 / 3.0, getS().getDistanceSquared(createSegment(0, 0, 1, 1, 1, 2)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(3, getS().getDistanceSquared(createSegment(2, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(3, getS().getDistanceSquared(createSegment(-2, -2, -2, -1, -1, -1)));
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(0.5, getS().getDistanceSquared(createSegment(0, 1, 0, 1, 1, 0)));
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(2, getS().getDistanceSquared(createSegment(0, 2, 0, 0, 2, 1)));
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, getS().getDistanceSquared(createSegment(2, 2, 2, 5, 18, 10)));
		}

		@DisplayName("(Segment3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Segment3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(0, 0, 0, -1, -1, -1)));
		}

		@DisplayName("(Segment3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(1, 1, 1, 2, 2, 2)));
		}

		@DisplayName("(Segment3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.66666666667, getS().getDistanceSquared(createSegment(1, 1, 0, 2, 2, 0)));
		}

		@DisplayName("(Segment3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(0.5, 0.5, 0.5, 0.5, 0.5, -1)));
		}

		@DisplayName("(Segment3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceSquared(createSegment(2, 0, 0, 2, 1, 1)));
		}

		@DisplayName("(Segment3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(0.05, 0.05, 0.05, 1.05, 1.05, 1.05)));
		}

		@DisplayName("(Segment3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.25, getS().getDistanceSquared(createSegment(2, 0.5, 0.5, 2, 1.5, 0.5)));
		}

		@DisplayName("(Segment3afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, getS().getDistanceSquared(createSegment(-1, -1, -1, -2, -2, -2)));
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.53589838486, getS().getDistanceSquared(createSphere(2, 2, 2, 1)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(0, 0, 0, 0)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(1, 1, 1, 0)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(0.5, 0.5, 0.5, 0)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(0.5, 0.5, 0.5, 5)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(0, 0, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(1, 1, 1, 1)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(0.5, 0.5, 0.5, 1)));
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.75, getS().getDistanceSquared(createSphere(-0.5, -0.5, -0.5, 0)));
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(0.5, 1.5, 0.5, 1)));
		}

		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(0.5, 0.5, 0.5, 0.5)));
		}

		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9, getS().getDistanceSquared(createSphere(4, 1, 1, 0)));
		}

		@DisplayName("(Sphere3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, getS().getDistanceSquared(createSphere(-1, -1, -1, 0)));
		}

		@DisplayName("(Sphere3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(0.3, 0.3, 0.3, 0.1)));
		}

		@DisplayName("(Sphere3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.6666666666666, getS().getDistanceSquared(createSphere(2, 0, 0, 0)));
		}

		@DisplayName("(Sphere3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.21539030917, getS().getDistanceSquared(createSphere(3, 3, 3, 3)));
		}

		@DisplayName("(Sphere3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(2, 2, 2, 2)));
		}

		@DisplayName("(Sphere3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.0538475772934, getS().getDistanceSquared(createSphere(-1, -1, -1, 1.5)));
		}

		@DisplayName("(Sphere3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.6666666666666, getS().getDistanceSquared(createSphere(0.5, 0.5, 1.5, 0)));
		}

		@DisplayName("(Sphere3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void sphere_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(0, 0, 0, 1)));
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = createPath()
					.moveTo(createPoint(2, 2, 2))
					.lineTo(createPoint(5, 4, 3));
		    assertEpsilonEquals(3, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 2: Path containing only a point at segment start
			var path = createPath()
					.moveTo(createPoint(0, 0, 0));
			assertNaN(getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 3: Path containing only a point at segment end
			var path = createPath()
					.moveTo(createPoint(1, 1, 1));
			assertNaN(getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 4: Path containing only a point at segment midpoint
			var path = createPath()
					.moveTo(createPoint(0.5, 0.5, 0.5));
			assertNaN(getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 5: Path line segment intersecting with segment
			var path = createPath()
					.moveTo(createPoint(-1, 0, 0))
					.lineTo(createPoint(2, 2, 2));
			assertEpsilonEquals(0.117647058824, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 6: Path with multiple line segments, one intersecting
			var path = createPath()
					.moveTo(createPoint(3, 3, 3))
					.lineTo(createPoint(4, 4, 4))
					.lineTo(createPoint(0.5, 0.5, 0.5));
			assertEpsilonEquals(0, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 7: Path completely opposite of segment
			var path = createPath()
					.moveTo(createPoint(-2, -2, -2))
					.lineTo(createPoint(-1, -1, -1));
			assertEpsilonEquals(3, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 8: Path parallel to segment but offset
			var path = createPath()
					.moveTo(createPoint(1, 0, 0))
					.lineTo(createPoint(2, 1, 1));
			assertEpsilonEquals(0.6666666666666667, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 9: Closed path forming a triangle around segment endpoint
			var path = createPath()
					.moveTo(createPoint(1, 1, 1))
					.lineTo(createPoint(2, 1, 1))
					.lineTo(createPoint(1.5, 2, 1))
					.closePath();
			assertEpsilonEquals(0, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 10: Closed rectangular path containing segment
			var path = createPath()
					.moveTo(createPoint(-1, -1, -1))
					.lineTo(createPoint(2, -1, -1))
					.lineTo(createPoint(2, 2, 2))
					.lineTo(createPoint(-1, 2, 2))
					.closePath();
			assertEpsilonEquals(1, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 11: Path with quadratic curve passing through segment
			var path = createPath()
					.moveTo(createPoint(-1, -1, -1))
					.quadTo(createPoint(0.5, 0.5, 0.5), createPoint(2, 2, 2));
			assertEpsilonEquals(0, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 12: Path with quadratic curve not intersecting segment
			var path = createPath()
					.moveTo(createPoint(3, 0, 0))
					.quadTo(createPoint(4, 1, 0), createPoint(5, 2, 0));
			assertEpsilonEquals(6.0000159923, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 13: Path with cubic bezier curve passing near segment
			var path = createPath()
					.moveTo(createPoint(-1, -1, -1))
					.curveTo(createPoint(0, 0, 0), createPoint(1, 1, 1), createPoint(2, 2, 2));
			assertEpsilonEquals(0, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 14: Path with cubic bezier curve away from segment
			var path = createPath()
					.moveTo(createPoint(3, 3, 3))
					.curveTo(createPoint(4, 4, 4), createPoint(5, 5, 5), createPoint(6, 6, 6));
			assertEpsilonEquals(12, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 15: Complex path with multiple segments and curves
			var path = createPath()
					.moveTo(createPoint(2, 2, 2))
					.lineTo(createPoint(3, 3, 3))
					.quadTo(createPoint(2, 2, 2), createPoint(1, 1, 1))
					.curveTo(createPoint(0.5, 0.5, 0.5), createPoint(0.25, 0.25, 0.25), createPoint(0, 0, 0));
			assertEpsilonEquals(0, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 16: Path with Z-axis variation
			var path = createPath()
					.moveTo(createPoint(0, 0, 2))
					.lineTo(createPoint(1, 1, 2));
			assertEpsilonEquals(1, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 17: Path skew in 3D space
			var path = createPath()
					.moveTo(createPoint(2, 0, 0))
					.lineTo(createPoint(2, 0, 2));
			assertEpsilonEquals(2, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 18: Closed diamond path with segment endpoint at vertex
			var path = createPath()
					.moveTo(createPoint(1, 1, 1))
					.lineTo(createPoint(2, 1, 1))
					.lineTo(createPoint(1.5, 1.5, 1.5))
					.lineTo(createPoint(1, 2, 1))
					.closePath();
			assertEpsilonEquals(0, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 19: Path segment tangent to the segment's projection
			var path = createPath()
					.moveTo(createPoint(0.5, 1, 0))
					.lineTo(createPoint(0.5, 1, 1));
			assertEpsilonEquals(0.125, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 20: Closed path forming spiral around segment
			var path = createPath()
					.moveTo(createPoint(0, 0, 0))
					.quadTo(createPoint(1, 0, 0), createPoint(1, 1, 0))
					.quadTo(createPoint(1, 1, 1), createPoint(0, 1, 1))
					.closePath();
			assertEpsilonEquals(0, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 21: Path with single line segment at start point
			var path = createPath()
					.moveTo(createPoint(0, 0, 0))
					.lineTo(createPoint(0.5, 0, 0));
			assertEpsilonEquals(0, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 22: Path with quadratic curve control point on segment
			var path = createPath()
					.moveTo(createPoint(-1, -1, -1))
					.quadTo(createPoint(0.5, 0.5, 0.5), createPoint(1, 0, 0));
			assertEpsilonEquals(0.0294117647059, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 23: Path perpendicular to segment in XY plane
			var path = createPath()
					.moveTo(createPoint(2, 0, 0.5))
					.lineTo(createPoint(2, 2, 0.5));
			assertEpsilonEquals(1.25, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 24: Path with bezier curve approximating segment
			var path = createPath()
					.moveTo(createPoint(0, 0, 0))
					.curveTo(createPoint(0.33, 0.33, 0.33), createPoint(0.67, 0.67, 0.67), createPoint(1, 1, 1));
			assertEpsilonEquals(0, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path3afp) #25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void path_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Test 25: Path forming star with segment at center vertex
			var path = createPath()
					.moveTo(createPoint(0.5, 0.5, 0.5))
					.lineTo(createPoint(1.5, 0.5, 0.5))
					.lineTo(createPoint(0.5, 0.5, 0.5))
					.lineTo(createPoint(0.5, 1.5, 0.5))
					.closePath();
			assertEpsilonEquals(0, getS().getDistanceSquared(path));
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3D shp = createMultiShape();
			shp.add(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3));
			shp.add(createSegment(1, 1, 0, 2, 2, 0));
			shp.add(createSphere(-1, -1, -1, 1.5));		
		    assertEpsilonEquals(0.0538475772934, getS().getDistanceSquared(shp));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(.5, .5, .5)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(1, 1, 1)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(14.94, getS().getDistanceSquared(createPoint(2.3, 4.5, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, getS().getDistanceSquared(createPoint(2, 2, 0)));
		}

	}

	@DisplayName("calculatesIntersectionFactorsSegmentSegment")
	@Nested
	public class CalculatesIntersectionFactorsSegmentSegment {

		/** Test if line-line intersection is valid.
		 *
		 * @param colinear indicates if it is expected to have the two lines colinear.
		 * @param position1 the position factor of the intersection point on the first geometric element. The value is in [0;1]
		 *     if it is located on the first segment.
		 * @param position2 if elements are not colinear, it is the position factor of the intersection point on the second
		 *     geometric element. The value is in [0;1] if it is located on the second segment.
		 *     If the elements are colinear, it is the position factor of the second point of intersection on the first
		 *     geometric element. The value is in [0;1] if it is located on the first segment.
		 * @param intersection the intersection definition to be tested.
		 */
		protected final void assertIntersectionEpsilonEquals(boolean colinear, double position1, double position2, SegmentIntersection intersection) {
			assertNotNull(intersection, "Intersection must exist");
			assertEquals(colinear, intersection.colinear(), "invalid colinear flag");
			assertEpsilonEquals(new InnerComputationPoint2D(position1, position2), new InnerComputationPoint2D(intersection.position1(), intersection.position2()), "position1 or position2 is invalid");
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertIntersectionEpsilonEquals(false,
					.5, .00523560209,
					Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					100, 50, 0, 100, 60, 0,
					90, 55, 0, 2000, 55, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					100, 50, 0, 100, 60, 0,
					200, 0, 0, 200, 10, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					100, -50, 0, 100, -60, 0,
					90, 55, 0, 2000, 55, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertIntersectionEpsilonEquals(false,
					.1277115766843605, .453061208936,
					Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					1000, 1.5325000286102295, 0, 2500, 1.5325000286102295, 0,
					1184.001080023255, 1.6651813832907332, 0, 1200.7014393876193, 1.372326130924099, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(Segment3afp.calculatesIntersectionFactorsLineLine(
					100, 50, 1, 100, 60, 5,
					90, 55, 0, 2000, 55, -10));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					100, 50, 1, 100, 60, 5,
					100, 70, 9, 100, 100, 21));
		}

		@DisplayName("#7 - 3D intersection at origin")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertIntersectionEpsilonEquals(false,
					0., 0.,
					Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					0, 0, 0,  1, 1, 1,
					0, 0, 0,  1, -1, 0));
		}

		@DisplayName("#8 - 3D intersection at non-zero factors")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					5, 0, 1,  4, 1, 1,
					1, 2, 3,  13, 2, -9));
		}

		@DisplayName("#9 - skew lines (non-coplanar) => no intersection")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					0, 0, 0,  1, 0, 0,
					0, 1, 1,  0, 2, 1));
		}

		@DisplayName("#10 - parallel distinct lines => no intersection")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					0, 0, 0,  1, 0, 0,
					0, 1, 0,  1, 1, 0));
		}

		@DisplayName("#11 - colinear overlapping direction")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// First line support points define parameter on first element:
			// P1=(0,0,0), P2=(2,2,2) => direction (2,2,2)
			// second line endpoints at (4,4,4) and (8,8,8) => factors 2 and 4 on first line
			assertNull(Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					0, 0, 0,  2, 2, 2,
					4, 4, 4,  8, 8, 8));
		}

		@DisplayName("#12 - colinear opposite direction")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// same support line, reversed second definition; factors still map on first line
			assertNull(Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					0, 0, 0,  2, 2, 2,
					8, 8, 8,  4, 4, 4));
		}

		@DisplayName("#13 - colinear with negative factors on first line")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// second line points behind first origin on same support
			assertNull(Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					0, 0, 0,  1, 1, 1,
					-3, -3, -3,  -1, -1, -1));
		}

		@DisplayName("#14 - intersection with first factor in [0,1], second outside")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// intersection point belongs to first segment support at t=0.0833333,
			// but on second line at u=0.6666666666
			assertIntersectionEpsilonEquals(false,
					0.08333333333333, 0.6666666666667,
					Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					0, 0, 0,  4, 0, 0,
					-7, 1, 0,  4, -0.5, 0));
		}

		@DisplayName("#15 - same geometric intersection, first line reversed")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Derived from #8 with first point pair swapped
			assertNull(Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					4, 1, 1,  5, 0, 1,
					1, 2, 3,  13, 2, -9));
		}

		@DisplayName("#16 - same geometric intersection, second line reversed")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Derived from #8 with second point pair swapped
			assertNull(Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					5, 0, 1,  4, 1, 1,
					13, 2, -9,  1, 2, 3));
		}

		@DisplayName("#17 - same geometric intersection, both lines reversed")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Derived from #8 with both point pairs swapped
			assertNull(Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					4, 1, 1,  5, 0, 1,
					13, 2, -9,  1, 2, 3));
		}

		@DisplayName("#18 - colinear A-B C-D")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					0, 0, 0,  1, 1, 1,
					-.4, -.4, -.4,  -.1, -.1, -.1));
		}

		@DisplayName("#19 - colinear A-C-B-D")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertIntersectionEpsilonEquals(true,
					0, .1,
					Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					0, 0, 0,  1, 1, 1,
					-.4, -.4, -.4,  .1, .1, .1));
		}

		@DisplayName("#20 - colinear A-C-D-B")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertIntersectionEpsilonEquals(true,
					0, 1,
					Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					0, 0, 0,  1, 1, 1,
					-.4, -.4, -.4,  1.6, 1.6, 1.6));
		}

		@DisplayName("#21 - colinear C-A-B-D")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertIntersectionEpsilonEquals(true,
					.4, .7,
					Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					0, 0, 0,  1, 1, 1,
					.4, .4, .4,  .7, .7, .7));
		}

		@DisplayName("#22 - colinear C-A-D-B")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertIntersectionEpsilonEquals(true,
					.4, 1,
					Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					0, 0, 0,  1, 1, 1,
					.4, .4, .4,  1.1, 1.1, 1.1));
		}

		@DisplayName("#23 - colinear C-B A-B")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(Segment3afp.calculatesIntersectionFactorsSegmentSegment(
					0, 0, 0,  1, 1, 1,
					1.4, 1.4, 1.4,  1.6, 1.6, 1.6));
		}
	
	}

	@DisplayName("findsProjectedPointOnLine")
	@Nested
	public class FindsProjectedPointOnLine {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.3076923076923077, Segment3afp.findsProjectedPointOnLine(
					2, 1, 0,
					0, 0, 0, 3, -2, 0));
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.6666666666666666, Segment3afp.findsProjectedPointOnLine(
					2, 1, 0,
					0, 0, 0, 3, 0, 0));
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-.7, Segment3afp.findsProjectedPointOnLine(
					2, -1, 0,
					0, 0, 0, -3, 1, 0));
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(14.4, Segment3afp.findsProjectedPointOnLine(
					2, 150, 0,
					0, 0, 0, -3, 1, 0));
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment3afp.findsProjectedPointOnLine(
					.5, .5, 0,
					0, 0, 0, 1, 1, 0));
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment3afp.findsProjectedPointOnLine(
					.5, .5, 15,
					0, 0, 0, 1, 1, 0));
		}
		
		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, Segment3afp.findsProjectedPointOnLine(
					.5, .5, -15,
					0, 0, 0, 1, 1, 0));
		}
		
		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.18421052631578946, Segment3afp.findsProjectedPointOnLine(
					.5, .5, 5,
					0, 0, 6, 1, 1, 0));
		}
		
		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1., Segment3afp.findsProjectedPointOnLine(
					.5, .5, 15,
					0, 2, 0, 1, 1, 0));
		}
		
		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.06756756, Segment3afp.findsProjectedPointOnLine(
					.5, .5, -15,
					0, 7, 0, 1, 1, 0));
		}
		
		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.737623762376, Segment3afp.findsProjectedPointOnLine(
					.5, .5, 5,
					0, 9, 6, 1, 1, 0));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    double result = Segment3afp.findsProjectedPointOnLine(
		            1, 2, 3,   // point
		            0, 0, 0,   // s1
		            0, 0, 0);  // s2 (same as s1)
		    assertTrue(Double.isNaN(result));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line along X-axis, point (0,2,3) projects onto S1 (0,0,0) -> t = 0
		    assertEpsilonEquals(0.0, Segment3afp.findsProjectedPointOnLine(
		            0, 2, 3,
		            0, 0, 0,
		            1, 0, 0));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line from (0,0,0) to (2,2,2), point (1,0,0)
		    // t = (1·2 + 0·2 + 0·2) / (2^2+2^2+2^2) = 2/12 = 1/6 ≈ 0.1666666667
		    assertEpsilonEquals(1.0 / 6.0, Segment3afp.findsProjectedPointOnLine(
		            1, 0, 0,
		            0, 0, 0,
		            2, 2, 2));
		}

	}

	@DisplayName("findsIntersectionSegmentSegment")
	@Nested
	public class FindsIntersectionSegmentSegment {
		
		public InnerComputationPoint3D result;

		@BeforeEach
		public void setUp() {
			result = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionSegmentSegment(
					0, 0, 1, 2, 2, 1,
					0, 2, 1, 2, 0, 1,
					result));
			assertEpsilonEquals(createPoint(1., 1., 1.), result);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionSegmentSegment(
					100, 50, 0, 100, 60, 0,
					90, 55, 0, 2000, 55, 0,
					result));
			assertEpsilonEquals(createPoint(100., 55., 0.), result);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.findsIntersectionSegmentSegment(
					100, 50, 0, 100, 60, 0,
					200, 0, 0, 200, 10, 0,
					result));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(
					Segment3afp.findsIntersectionSegmentSegment(
					100, -50, 0, 100, -60, 0,
					90, 55, 0, 2000, 55, 0,
					result));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionSegmentSegment(
					1000, 1.5325000286102295, 0, 2500, 1.5325000286102295, 0,
					1184.001080023255, 1.6651813832907332, 0, 1200.7014393876193, 1.372326130924099, 0,
					result));
			assertEpsilonEquals(createPoint(1191.567365026, 1.53250002861, 0), result);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.findsIntersectionSegmentSegment(
					100, 50, 1, 100, 60, 5,
					90, 55, 0, 2000, 55, -10,
					result));
		}

		@DisplayName("#7 - touching at one endpoint")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionSegmentSegment(
					0, 0, 0, 1, 1, 1,
					1, 1, 1, 2, 0, 0,
					result));
			assertEpsilonEquals(createPoint(1., 1., 1.), result);
		}

		@DisplayName("#8 - skew segments (closest points but no intersection)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(
					Segment3afp.findsIntersectionSegmentSegment(
					-2, 0, 0, 2, 0, 0,
					0, -2, 1, 0, 2, 1,
					result));
		}

		@DisplayName("#9 - colinear overlapping segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionSegmentSegment(
					0, 0, 0, 4, 0, 0,
					2, 0, 0, 6, 0, 0,
					result));
			assertEpsilonEquals(createPoint(2., 0., 0.), result);
		}

		@DisplayName("#10 - colinear disjoint segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(
					Segment3afp.findsIntersectionSegmentSegment(
					0, 0, 0, 1, 0, 0,
					3, 0, 0, 4, 0, 0,
					result));
		}

		@DisplayName("#11 - parallel non-colinear segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(
					Segment3afp.findsIntersectionSegmentSegment(
					0, 0, 0, 5, 0, 0,
					0, 1, 0, 5, 1, 0,
					result));
		}

		@DisplayName("#12 - proper 3D interior intersection")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// S1: (0,0,0)->(2,2,2), S2: (2,0,2)->(0,2,0), intersection at (1,1,1)
			assertTrue(
					Segment3afp.findsIntersectionSegmentSegment(
					0, 0, 0, 2, 2, 2,
					2, 0, 2, 0, 2, 0,
					result));
			assertEpsilonEquals(createPoint(1., 1., 1.), result);
		}

		@DisplayName("#13 - line intersection exists but outside second segment")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Infinite lines cross at (2,0,0), but second segment is around x=10..11
			assertFalse(
					Segment3afp.findsIntersectionSegmentSegment(
					0, 0, 0, 5, 0, 0,
					10, -1, 0, 11, 1, 0,
					result));
		}

		@DisplayName("#14 - reverse first segment endpoints (invariance)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionSegmentSegment(
					2, 2, 1, 0, 0, 1,
					0, 2, 1, 2, 0, 1,
					result));
			assertEpsilonEquals(createPoint(1., 1., 1.), result);
		}

		@DisplayName("#15 - reverse second segment endpoints (invariance)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionSegmentSegment(
					0, 0, 1, 2, 2, 1,
					2, 0, 1, 0, 2, 1,
					result));
			assertEpsilonEquals(createPoint(1., 1., 1.), result);
		}

		@DisplayName("#16 - degenerate first segment point on second segment")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.findsIntersectionSegmentSegment(
					1, 1, 1, 1, 1, 1,
					0, 0, 0, 2, 2, 2,
					result));
			assertEpsilonEquals(createPoint(1., 1., 1.), result);
		}

		@DisplayName("#17 - both segments degenerate at different points")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(
					Segment3afp.findsIntersectionSegmentSegment(
					1, 2, 3, 1, 2, 3,
					4, 6, 3, 4, 6, 3,
					result));
		}

	}

	@DisplayName("calculatesDistanceLinePoint")
	@Nested
	public class CalculatesDistanceLinePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.941450686788302, Segment3afp.calculatesDistanceLinePoint(
					0, 0, 0, 3, -2, 0,
					2, 1, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.941450686788302, Segment3afp.calculatesDistanceLinePoint(
					3, -2, 0, 0, 0, 0,
					2, 1, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
					0, 0, 0, 3, -2, 0,
					2, 1, 15));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
					3, -2, 0, 0, 0, 0,
					2, 1, 15));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
					0, 0, 0, 3, -2, 0,
					2, 1, -15));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
					3, -2, 0, 0, 0, 0,
					2, 1, -15));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
					0, 0, 0, 3, -2, 0,
					2, 1, 15));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
					3, -2, 0, 0, 0, 0,
					2, 1, 15));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
					0, 0, 0, 3, -2, 0,
					2, 1, -15));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceLinePoint(
					3, -2, 0, 0, 0, 0,
					2, 1, -15));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment3afp.calculatesDistanceLinePoint(
					0, 0, 0, 3, 0, 0,
					2, 1, 0));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment3afp.calculatesDistanceLinePoint(
					3, 0, 0, 0, 0, 0,
					2, 1, 0));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.3162277660168379, Segment3afp.calculatesDistanceLinePoint(
					0, 0, 0, -3, 1, 0,
					2, -1, 0));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.3162277660168379, Segment3afp.calculatesDistanceLinePoint(
					-3, 1, 0, 0, 0, 0,
					2, -1, 0));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(142.9349502396107, Segment3afp.calculatesDistanceLinePoint(
					0, 0, 0, -3, 1, 0,
					2, 150, 0));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(142.9349502396107, Segment3afp.calculatesDistanceLinePoint(
					-3, 1, 0, 0, 0, 0,
					2, 150, 0));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceLinePoint(
					0, 0, 0, 1, 1, 0,
					.5, .5, 0));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceLinePoint(
					1, 1, 0, 0, 0, 0,
					.5, .5, 0));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10.20616823515927, Segment3afp.calculatesDistanceLinePoint(
					6, -5.6, 2.458, 1.7, 4.6, -5,
					.5, .5, 9.1));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10.20616823515927, Segment3afp.calculatesDistanceLinePoint(
					6, -5.6, 2.458, 1.7, 4.6, -5,
					.5, .5, 9.1));
		}

		@DisplayName("#21 - point on 3D line (distance = 0)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceLinePoint(
					0, 0, 0, 2, 4, 6,
					1, 2, 3));
		}

		@DisplayName("#22 - reverse endpoints invariant (same as #21)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceLinePoint(
					2, 4, 6, 0, 0, 0,
					1, 2, 3));
		}

		@DisplayName("#23 - axis-aligned line along Z")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line: x=0,y=0 (z free), point: (3,4,5) => distance sqrt(3^2+4^2)=5
			assertEpsilonEquals(5, Segment3afp.calculatesDistanceLinePoint(
					0, 0, -2, 0, 0, 8,
					3, 4, 5));
		}

		@DisplayName("#24 - reverse endpoints invariant (same as #23)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, Segment3afp.calculatesDistanceLinePoint(
					0, 0, 8, 0, 0, -2,
					3, 4, 5));
		}

		@DisplayName("#25 - line parallel to Y, shifted in XZ")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line x=1,z=2 ; point (4,5,6) => sqrt((4-1)^2 + (6-2)^2) = 5
			assertEpsilonEquals(5, Segment3afp.calculatesDistanceLinePoint(
					1, -10, 2, 1, 10, 2,
					4, 5, 6));
		}

		@DisplayName("#26 - line through origin direction (1,1,1)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_26(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// For point (1,-1,0): projection on (1,1,1) is 0, so distance = sqrt(1^2+(-1)^2+0^2)=sqrt(2)
			assertEpsilonEquals(1.4142135623730951, Segment3afp.calculatesDistanceLinePoint(
					0, 0, 0, 1, 1, 1,
					1, -1, 0));
		}

		@DisplayName("#27 - reverse endpoints invariant (same as #26)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_27(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.4142135623730951, Segment3afp.calculatesDistanceLinePoint(
					1, 1, 1, 0, 0, 0,
					1, -1, 0));
		}

		@DisplayName("#28 - large coordinates numeric stability")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_28(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line along X at y=1e9,z=-1e9 ; point differs by (0,3,4) in yz => distance 5
			assertEpsilonEquals(5, Segment3afp.calculatesDistanceLinePoint(
					1_000_000_000d, 1_000_000_000d, -1_000_000_000d,
					1_000_000_010d, 1_000_000_000d, -1_000_000_000d,
					1_000_000_007d, 1_000_000_003d, -999_999_996d));
		}

		@DisplayName("#29 - degenerate segment (A==B) behaves as point distance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_29(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// If API handles degenerate direction vector safely, expected distance to point A:
			// sqrt((4-1)^2 + (6-2)^2 + (3-3)^2) = 5
			assertEpsilonEquals(5, Segment3afp.calculatesDistanceLinePoint(
					1, 2, 3, 1, 2, 3,
					4, 6, 3));
		}

		@DisplayName("#30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_30(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, Segment3afp.calculatesDistanceLinePoint(
					0, 0, 0, 3, -2, 0,
					9, -6, 3));
		}

	}

	@DisplayName("calculatesDistanceSquaredLinePoint")
	@Nested
	public class CalculatesDistanceSquaredLinePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
					0, 0, 0, 3, -2, 0,
					2, 1, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
					3, -2, 0, 0, 0, 0,
					2, 1, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
					0, 0, 0, 3, -2, 0,
					2, 1, 15));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
					3, -2, 0, 0, 0, 0,
					2, 1, 15));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
					0, 0, 0, 3, -2, 0,
					2, 1, -15));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
					3, -2, 0, 0, 0, 0,
					2, 1, -15));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
					0, 0, 0, 3, -2, 0,
					2, 1, 15));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
					3, -2, 0, 0, 0, 0,
					2, 1, 15));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
					0, 0, 0, 3, -2, 0,
					2, 1, -15));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredLinePoint(
					3, -2, 0, 0, 0, 0,
					2, 1, -15));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment3afp.calculatesDistanceSquaredLinePoint(
					0, 0, 0, 3, 0, 0,
					2, 1, 0));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment3afp.calculatesDistanceSquaredLinePoint(
					3, 0, 0, 0, 0, 0,
					2, 1, 0));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.1, Segment3afp.calculatesDistanceSquaredLinePoint(
					0, 0, 0, -3, 1, 0,
					2, -1, 0));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.1, Segment3afp.calculatesDistanceSquaredLinePoint(
					-3, 1, 0, 0, 0, 0,
					2, -1, 0));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(20430.4, Segment3afp.calculatesDistanceSquaredLinePoint(
					0, 0, 0, -3, 1, 0,
					2, 150, 0));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(20430.4, Segment3afp.calculatesDistanceSquaredLinePoint(
					-3, 1, 0, 0, 0, 0,
					2, 150, 0));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceSquaredLinePoint(
					0, 0, 0, 1, 1, 0,
					.5, .5, 0));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceSquaredLinePoint(
					1, 1, 0, 0, 0, 0,
					.5, .5, 0));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(104.165870044, Segment3afp.calculatesDistanceSquaredLinePoint(
					6, -5.6, 2.458, 1.7, 4.6, -5,
					.5, .5, 9.1));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(104.165870044, Segment3afp.calculatesDistanceSquaredLinePoint(
					6, -5.6, 2.458, 1.7, 4.6, -5,
					.5, .5, 9.1));
		}

		@DisplayName("#21 - point on 3D line (distance = 0)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceSquaredLinePoint(
					0, 0, 0, 2, 4, 6,
					1, 2, 3));
		}

		@DisplayName("#22 - reverse endpoints invariant (same as #21)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceSquaredLinePoint(
					2, 4, 6, 0, 0, 0,
					1, 2, 3));
		}

		@DisplayName("#23 - axis-aligned line along Z")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line: x=0,y=0 (z free), point: (3,4,5) => distance sqrt(3^2+4^2)=5
			assertEpsilonEquals(25, Segment3afp.calculatesDistanceSquaredLinePoint(
					0, 0, -2, 0, 0, 8,
					3, 4, 5));
		}

		@DisplayName("#24 - reverse endpoints invariant (same as #23)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25, Segment3afp.calculatesDistanceSquaredLinePoint(
					0, 0, 8, 0, 0, -2,
					3, 4, 5));
		}

		@DisplayName("#25 - line parallel to Y, shifted in XZ")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line x=1,z=2 ; point (4,5,6) => sqrt((4-1)^2 + (6-2)^2) = 5
			assertEpsilonEquals(25, Segment3afp.calculatesDistanceSquaredLinePoint(
					1, -10, 2, 1, 10, 2,
					4, 5, 6));
		}

		@DisplayName("#26 - line through origin direction (1,1,1)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_26(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// For point (1,-1,0): projection on (1,1,1) is 0, so distance = sqrt(1^2+(-1)^2+0^2)=sqrt(2)
			assertEpsilonEquals(2, Segment3afp.calculatesDistanceSquaredLinePoint(
					0, 0, 0, 1, 1, 1,
					1, -1, 0));
		}

		@DisplayName("#27 - reverse endpoints invariant (same as #26)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_27(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Segment3afp.calculatesDistanceSquaredLinePoint(
					1, 1, 1, 0, 0, 0,
					1, -1, 0));
		}

		@DisplayName("#28 - large coordinates numeric stability")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_28(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line along X at y=1e9,z=-1e9 ; point differs by (0,3,4) in yz => distance 5
			assertEpsilonEquals(25, Segment3afp.calculatesDistanceSquaredLinePoint(
					1_000_000_000d, 1_000_000_000d, -1_000_000_000d,
					1_000_000_010d, 1_000_000_000d, -1_000_000_000d,
					1_000_000_007d, 1_000_000_003d, -999_999_996d));
		}

		@DisplayName("#29 - degenerate segment (A==B) behaves as point distance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_29(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// If API handles degenerate direction vector safely, expected distance to point A:
			// sqrt((4-1)^2 + (6-2)^2 + (3-3)^2) = 5
			assertEpsilonEquals(25, Segment3afp.calculatesDistanceSquaredLinePoint(
					1, 2, 3, 1, 2, 3,
					4, 6, 3));
		}

		@DisplayName("#30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_30(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9, Segment3afp.calculatesDistanceSquaredLinePoint(
					0, 0, 0, 3, -2, 0,
					9, -6, 3));
		}

	}

	@DisplayName("calculatesDistanceSegmentPoint")
	@Nested
	public class CalculatesDistanceSegmentPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.941450686788302, Segment3afp.calculatesDistanceSegmentPoint(
					0, 0, 0, 3, -2, 0,
					2, 1, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.941450686788302, Segment3afp.calculatesDistanceSegmentPoint(
					3, -2, 0, 0, 0, 0,
					2, 1, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
					0, 0, 0, 3, -2, 0,
					2, 1, 15));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
					3, -2, 0, 0, 0, 0,
					2, 1, 15));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
					0, 0, 0, 3, -2, 0,
					2, 1, -15));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
					3, -2, 0, 0, 0, 0,
					2, 1, -15));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
					0, 0, 0, 3, -2, 0,
					2, 1, 15));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
					3, -2, 0, 0, 0, 0,
					2, 1, 15));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
					0, 0, 0, 3, -2, 0,
					2, 1, -15));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.12511919851314, Segment3afp.calculatesDistanceSegmentPoint(
					3, -2, 0, 0, 0, 0,
					2, 1, -15));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment3afp.calculatesDistanceSegmentPoint(
					0, 0, 0, 3, 0, 0,
					2, 1, 0));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment3afp.calculatesDistanceSegmentPoint(
					3, 0, 0, 0, 0, 0,
					2, 1, 0));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.2360679775, Segment3afp.calculatesDistanceSegmentPoint(
					0, 0, 0, -3, 1, 0,
					2, -1, 0));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.2360679775, Segment3afp.calculatesDistanceSegmentPoint(
					-3, 1, 0, 0, 0, 0,
					2, -1, 0));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(149.0838690134, Segment3afp.calculatesDistanceSegmentPoint(
					0, 0, 0, -3, 1, 0,
					2, 150, 0));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(149.0838690134, Segment3afp.calculatesDistanceSegmentPoint(
					-3, 1, 0, 0, 0, 0,
					2, 150, 0));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceSegmentPoint(
					0, 0, 0, 1, 1, 0,
					.5, .5, 0));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceSegmentPoint(
					1, 1, 0, 0, 0, 0,
					.5, .5, 0));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10.20616823515927, Segment3afp.calculatesDistanceSegmentPoint(
					6, -5.6, 2.458, 1.7, 4.6, -5,
					.5, .5, 9.1));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10.20616823515927, Segment3afp.calculatesDistanceSegmentPoint(
					6, -5.6, 2.458, 1.7, 4.6, -5,
					.5, .5, 9.1));
		}

		@DisplayName("#21 - point on 3D line (distance = 0)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceSegmentPoint(
					0, 0, 0, 2, 4, 6,
					1, 2, 3));
		}

		@DisplayName("#22 - reverse endpoints invariant (same as #21)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceSegmentPoint(
					2, 4, 6, 0, 0, 0,
					1, 2, 3));
		}

		@DisplayName("#23 - axis-aligned line along Z")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line: x=0,y=0 (z free), point: (3,4,5) => distance sqrt(3^2+4^2)=5
			assertEpsilonEquals(5, Segment3afp.calculatesDistanceSegmentPoint(
					0, 0, -2, 0, 0, 8,
					3, 4, 5));
		}

		@DisplayName("#24 - reverse endpoints invariant (same as #23)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, Segment3afp.calculatesDistanceSegmentPoint(
					0, 0, 8, 0, 0, -2,
					3, 4, 5));
		}

		@DisplayName("#25 - line parallel to Y, shifted in XZ")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line x=1,z=2 ; point (4,5,6) => sqrt((4-1)^2 + (6-2)^2) = 5
			assertEpsilonEquals(5, Segment3afp.calculatesDistanceSegmentPoint(
					1, -10, 2, 1, 10, 2,
					4, 5, 6));
		}

		@DisplayName("#26 - line through origin direction (1,1,1)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_26(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// For point (1,-1,0): projection on (1,1,1) is 0, so distance = sqrt(1^2+(-1)^2+0^2)=sqrt(2)
			assertEpsilonEquals(1.4142135623730951, Segment3afp.calculatesDistanceSegmentPoint(
					0, 0, 0, 1, 1, 1,
					1, -1, 0));
		}

		@DisplayName("#27 - reverse endpoints invariant (same as #26)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_27(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.4142135623730951, Segment3afp.calculatesDistanceSegmentPoint(
					1, 1, 1, 0, 0, 0,
					1, -1, 0));
		}

		@DisplayName("#28 - large coordinates numeric stability")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_28(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line along X at y=1e9,z=-1e9 ; point differs by (0,3,4) in yz => distance 5
			assertEpsilonEquals(5, Segment3afp.calculatesDistanceSegmentPoint(
					1_000_000_000d, 1_000_000_000d, -1_000_000_000d,
					1_000_000_010d, 1_000_000_000d, -1_000_000_000d,
					1_000_000_007d, 1_000_000_003d, -999_999_996d));
		}

		@DisplayName("#29 - degenerate segment (A==B) behaves as point distance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_29(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// If API handles degenerate direction vector safely, expected distance to point A:
			// sqrt((4-1)^2 + (6-2)^2 + (3-3)^2) = 5
			assertEpsilonEquals(5, Segment3afp.calculatesDistanceSegmentPoint(
					1, 2, 3, 1, 2, 3,
					4, 6, 3));
		}

		@DisplayName("#30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_30(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7.8102496759, Segment3afp.calculatesDistanceSegmentPoint(
					0, 0, 0, 3, -2, 0,
					9, -6, 3));
		}

	}

	@DisplayName("calculatesDistanceSquaredSegmentPoint")
	@Nested
	public class CalculatesDistanceSquaredSegmentPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 0, 3, -2, 0,
					2, 1, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					3, -2, 0, 0, 0, 0,
					2, 1, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 0, 3, -2, 0,
					2, 1, 15));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					3, -2, 0, 0, 0, 0,
					2, 1, 15));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 0, 3, -2, 0,
					2, 1, -15));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					3, -2, 0, 0, 0, 0,
					2, 1, -15));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 0, 3, -2, 0,
					2, 1, 15));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					3, -2, 0, 0, 0, 0,
					2, 1, 15));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 0, 3, -2, 0,
					2, 1, -15));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(228.769230769, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					3, -2, 0, 0, 0, 0,
					2, 1, -15));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 0, 3, 0, 0,
					2, 1, 0));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					3, 0, 0, 0, 0, 0,
					2, 1, 0));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 0, -3, 1, 0,
					2, -1, 0));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					-3, 1, 0, 0, 0, 0,
					2, -1, 0));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(22226, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 0, -3, 1, 0,
					2, 150, 0));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(22226, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					-3, 1, 0, 0, 0, 0,
					2, 150, 0));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 0, 1, 1, 0,
					.5, .5, 0));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					1, 1, 0, 0, 0, 0,
					.5, .5, 0));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(104.165870044, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					6, -5.6, 2.458, 1.7, 4.6, -5,
					.5, .5, 9.1));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(104.165870044, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					6, -5.6, 2.458, 1.7, 4.6, -5,
					.5, .5, 9.1));
		}

		@DisplayName("#21 - point on 3D line (distance = 0)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 0, 2, 4, 6,
					1, 2, 3));
		}

		@DisplayName("#22 - reverse endpoints invariant (same as #21)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					2, 4, 6, 0, 0, 0,
					1, 2, 3));
		}

		@DisplayName("#23 - axis-aligned line along Z")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line: x=0,y=0 (z free), point: (3,4,5) => distance sqrt(3^2+4^2)=5
			assertEpsilonEquals(25, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, -2, 0, 0, 8,
					3, 4, 5));
		}

		@DisplayName("#24 - reverse endpoints invariant (same as #23)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 8, 0, 0, -2,
					3, 4, 5));
		}

		@DisplayName("#25 - line parallel to Y, shifted in XZ")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line x=1,z=2 ; point (4,5,6) => sqrt((4-1)^2 + (6-2)^2) = 5
			assertEpsilonEquals(25, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					1, -10, 2, 1, 10, 2,
					4, 5, 6));
		}

		@DisplayName("#26 - line through origin direction (1,1,1)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_26(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// For point (1,-1,0): projection on (1,1,1) is 0, so distance = sqrt(1^2+(-1)^2+0^2)=sqrt(2)
			assertEpsilonEquals(2, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 0, 1, 1, 1,
					1, -1, 0));
		}

		@DisplayName("#27 - reverse endpoints invariant (same as #26)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_27(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					1, 1, 1, 0, 0, 0,
					1, -1, 0));
		}

		@DisplayName("#28 - large coordinates numeric stability")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_28(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line along X at y=1e9,z=-1e9 ; point differs by (0,3,4) in yz => distance 5
			assertEpsilonEquals(25, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					1_000_000_000d, 1_000_000_000d, -1_000_000_000d,
					1_000_000_010d, 1_000_000_000d, -1_000_000_000d,
					1_000_000_007d, 1_000_000_003d, -999_999_996d));
		}

		@DisplayName("#29 - degenerate segment (A==B) behaves as point distance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_29(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// If API handles degenerate direction vector safely, expected distance to point A:
			// sqrt((4-1)^2 + (6-2)^2 + (3-3)^2) = 5
			assertEpsilonEquals(25, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					1, 2, 3, 1, 2, 3,
					4, 6, 3));
		}

		@DisplayName("#30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_30(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(61, Segment3afp.calculatesDistanceSquaredSegmentPoint(
					0, 0, 0, 3, -2, 0,
					9, -6, 3));
		}

	}

	@DisplayName("calculatesDistanceSegmentSegment")
	@Nested
	public class CalculatesDistanceSegmentSegment{
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5., Segment3afp.calculatesDistanceSegmentSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					1, 1, 6, 100, 100, 100));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.732050808, Segment3afp.calculatesDistanceSegmentSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					-1, -1, -1, -100, -100, -100));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Segment3afp.calculatesDistanceSegmentSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					-1, 1, 0, 1, -1, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Segment3afp.calculatesDistanceSegmentSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					0, 1, 0, 1, 0, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(45.033320997, Segment3afp.calculatesDistanceSegmentSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					-27, -27, -27, -26, -26, -26));
		}

		@DisplayName("#6 - identical segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Segment3afp.calculatesDistanceSegmentSegment(
					0, 0, 0,  2, 2, 2,
					0, 0, 0,  2, 2, 2));
		}

		@DisplayName("#7 - collinear overlapping segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Segment3afp.calculatesDistanceSegmentSegment(
					0, 0, 0,  4, 0, 0,
					2, 0, 0,  6, 0, 0));
		}

		@DisplayName("#8 - collinear disjoint segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// distance between [0,1] and [3,4] on X axis = 2
			assertEpsilonEquals(2., Segment3afp.calculatesDistanceSegmentSegment(
					0, 0, 0,  1, 0, 0,
					3, 0, 0,  4, 0, 0));
		}

		@DisplayName("#9 - parallel non-collinear segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// two X-parallel segments offset by y=3, z=4 => distance 5
			assertEpsilonEquals(5., Segment3afp.calculatesDistanceSegmentSegment(
					0, 0, 0,   10, 0, 0,
					0, 3, 4,   10, 3, 4));
		}

		@DisplayName("#10 - perpendicular crossing in same plane")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Segment3afp.calculatesDistanceSegmentSegment(
					-1, 0, 0,   1, 0, 0,
					0, -1, 0,   0, 1, 0));
		}

		@DisplayName("#11 - skew segments (closest points in interiors)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// S1 on X axis, S2 on Y axis at z=1, crossing projections at origin => min distance = 1
			assertEpsilonEquals(1., Segment3afp.calculatesDistanceSegmentSegment(
					-2, 0, 0,   2, 0, 0,
					0, -2, 1,   0, 2, 1));
		}

		@DisplayName("#12 - touching at one endpoint")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Segment3afp.calculatesDistanceSegmentSegment(
					0, 0, 0,   1, 1, 1,
					1, 1, 1,   2, 0, 0));
		}

		@DisplayName("#13 - one degenerate segment (point) to segment")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// point (0,3,4) to X-axis segment [-5,5] => distance 5
			assertEpsilonEquals(5., Segment3afp.calculatesDistanceSegmentSegment(
					0, 3, 4,   0, 3, 4,
					-5, 0, 0,   5, 0, 0));
		}

		@DisplayName("#14 - both degenerate segments (point-point distance)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// distance between (1,2,3) and (4,6,3) = 5
			assertEpsilonEquals(5., Segment3afp.calculatesDistanceSegmentSegment(
					1, 2, 3,   1, 2, 3,
					4, 6, 3,   4, 6, 3));
		}

		@DisplayName("#15 - nearest points are endpoints")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// closest is between (1,0,0) and (3,4,0): sqrt(20)
			assertEpsilonEquals(4.47213595499958, Segment3afp.calculatesDistanceSegmentSegment(
					0, 0, 0,   1, 0, 0,
					3, 4, 0,   3, 5, 0));
		}

		@DisplayName("#16 - endpoint order invariance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// same geometry as #11 but with both segments reversed
			assertEpsilonEquals(1., Segment3afp.calculatesDistanceSegmentSegment(
					2, 0, 0,   -2, 0, 0,
					0, 2, 1,   0, -2, 1));
		}

	}

	@DisplayName("calculatesDistanceSquaredSegmentSegment")
	@Nested
	public class CalculatesDistanceSquaredSegmentSegment{
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25., Segment3afp.calculatesDistanceSquaredSegmentSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					1, 1, 6, 100, 100, 100));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, Segment3afp.calculatesDistanceSquaredSegmentSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					-1, -1, -1, -100, -100, -100));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Segment3afp.calculatesDistanceSquaredSegmentSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					-1, 1, 0, 1, -1, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Segment3afp.calculatesDistanceSquaredSegmentSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					0, 1, 0, 1, 0, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2028, Segment3afp.calculatesDistanceSquaredSegmentSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					-27, -27, -27, -26, -26, -26));
		}

		@DisplayName("#6 - identical segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Segment3afp.calculatesDistanceSquaredSegmentSegment(
					0, 0, 0,  2, 2, 2,
					0, 0, 0,  2, 2, 2));
		}

		@DisplayName("#7 - collinear overlapping segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Segment3afp.calculatesDistanceSquaredSegmentSegment(
					0, 0, 0,  4, 0, 0,
					2, 0, 0,  6, 0, 0));
		}

		@DisplayName("#8 - collinear disjoint segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// distance between [0,1] and [3,4] on X axis = 2
			assertEpsilonEquals(4., Segment3afp.calculatesDistanceSquaredSegmentSegment(
					0, 0, 0,  1, 0, 0,
					3, 0, 0,  4, 0, 0));
		}

		@DisplayName("#9 - parallel non-collinear segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// two X-parallel segments offset by y=3, z=4 => distance 5
			assertEpsilonEquals(25., Segment3afp.calculatesDistanceSquaredSegmentSegment(
					0, 0, 0,   10, 0, 0,
					0, 3, 4,   10, 3, 4));
		}

		@DisplayName("#10 - perpendicular crossing in same plane")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Segment3afp.calculatesDistanceSquaredSegmentSegment(
					-1, 0, 0,   1, 0, 0,
					0, -1, 0,   0, 1, 0));
		}

		@DisplayName("#11 - skew segments (closest points in interiors)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// S1 on X axis, S2 on Y axis at z=1, crossing projections at origin => min distance = 1
			assertEpsilonEquals(1., Segment3afp.calculatesDistanceSquaredSegmentSegment(
					-2, 0, 0,   2, 0, 0,
					0, -2, 1,   0, 2, 1));
		}

		@DisplayName("#12 - touching at one endpoint")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Segment3afp.calculatesDistanceSquaredSegmentSegment(
					0, 0, 0,   1, 1, 1,
					1, 1, 1,   2, 0, 0));
		}

		@DisplayName("#13 - one degenerate segment (point) to segment")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// point (0,3,4) to X-axis segment [-5,5] => distance 5
			assertEpsilonEquals(25., Segment3afp.calculatesDistanceSquaredSegmentSegment(
					0, 3, 4,   0, 3, 4,
					-5, 0, 0,   5, 0, 0));
		}

		@DisplayName("#14 - both degenerate segments (point-point distance)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// distance between (1,2,3) and (4,6,3) = 5
			assertEpsilonEquals(25., Segment3afp.calculatesDistanceSquaredSegmentSegment(
					1, 2, 3,   1, 2, 3,
					4, 6, 3,   4, 6, 3));
		}

		@DisplayName("#15 - nearest points are endpoints")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// closest is between (1,0,0) and (3,4,0): sqrt(20)
			assertEpsilonEquals(20, Segment3afp.calculatesDistanceSquaredSegmentSegment(
					0, 0, 0,   1, 0, 0,
					3, 4, 0,   3, 5, 0));
		}

		@DisplayName("#16 - endpoint order invariance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// same geometry as #11 but with both segments reversed
			assertEpsilonEquals(1., Segment3afp.calculatesDistanceSquaredSegmentSegment(
					2, 0, 0,   -2, 0, 0,
					0, 2, 1,   0, -2, 1));
		}

	}

	@DisplayName("interpolate")
	@Nested
	public class Interpolate {

		private InnerComputationPoint3D result;

		@BeforeEach
		public void setUp() {
			result = new InnerComputationPoint3D();
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.interpolate(1., 2., 0, 3., 4., 0, 0., result);
			assertEpsilonEquals(createPoint(1, 2, 0), result);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.interpolate(1., 2., 0, 3., 4., 0, .25, result);
			assertEpsilonEquals(createPoint(1.5, 2.5, 0), result);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.interpolate(1., 2., 0, 3., 4., 0, .5, result);
			assertEpsilonEquals(createPoint(2, 3, 0), result);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.interpolate(1., 2., 0, 3., 4., 0, .75, result);
			assertEpsilonEquals(createPoint(2.5, 3.5, 0), result);
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_51(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Segment3afp.interpolate(1., 2., 0, 3., 4., 0, 1., result);
			assertEpsilonEquals(createPoint(3, 4, 0), result);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    Segment3afp.interpolate(1., 2., 3., 4., 5., 6., 0.5, result);
		    assertEpsilonEquals(createPoint(2.5, 3.5, 4.5), result);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    Segment3afp.interpolate(-5., -3., 0., 5., 3., 0., 0.0, result);
		    assertEpsilonEquals(createPoint(-5., -3., 0.), result);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    Segment3afp.interpolate(-5., -3., 0., 5., 3., 0., 1.0, result);
		    assertEpsilonEquals(createPoint(5., 3., 0.), result);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    Segment3afp.interpolate(2., 4., 6., 4., 8., 12., -0.5, result);
		    // extrapolation before p1: p1 + (-0.5)*(p2-p1) = (1, 2, 3)
		    assertEpsilonEquals(createPoint(1., 2., 3.), result);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    Segment3afp.interpolate(0., 0., 0., 2., 2., 2., 1.5, result);
		    // extrapolation after p2: 0 + 1.5*2 = 3
		    assertEpsilonEquals(createPoint(3., 3., 3.), result);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    Segment3afp.interpolate(1., 1., 1., 1., 1., 1., 0.5, result);
		    assertEpsilonEquals(createPoint(1., 1., 1.), result);
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    Segment3afp.interpolate(-10., 0., 5., -10., 0., 5., 0.75, result);
		    assertEpsilonEquals(createPoint(-10., 0., 5.), result);
		}

	}

	@DisplayName("intersectsLineLine")
	@Nested
	public class IntersectsLineLine {

		private InnerComputationPoint3D result;

		@BeforeEach
		public void setUp() {
			result = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsLineLine(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, 1, 1, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsLineLine(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, 2, 2, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsLineLine(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, .5, .5, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsLineLine(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, .5, .5, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsLineLine(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, 0, 0, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsLineLine(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, -1, -1, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsLineLine(
					0, 0, 0, 1, 1, 0,
					-3, 0, 0, 4, 0, 0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsLineLine(
					0, 0, 0, 1, 1, 0,
					-3, 0, 0, -2, 1, 0));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsLineLine(
					0, 0, 0, 1, 1, 0,
					10, 0, 0, 9, -1, 0));
		}

		@DisplayName("#10 - Intersecting at interior point (not origin)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: (0,0)-(2,2), line2: (0,2)-(2,0) intersect at (1,1)
		    assertTrue(Segment3afp.intersectsLineLine(
		            0, 0, 0, 2, 2, 0,
		            0, 2, 0, 2, 0, 0));
		}

		@DisplayName("#11 - Parallel distinct lines (no intersection)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // horizontal lines at y=0 and y=1
		    assertFalse(Segment3afp.intersectsLineLine(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 1, 1, 0));
		}

		@DisplayName("#12 - Coincident lines (same infinite line)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // same line y=x, different segments
		    assertTrue(Segment3afp.intersectsLineLine(
		            0, 0, 0, 1, 1, 0,
		            2, 2, 0, 3, 3, 0));
		}

		@DisplayName("#13 - Intersecting in 3D (coplanar with non‑zero z)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: x‑axis, line2: vertical segment crossing at (0.5,0,0)
		    assertTrue(Segment3afp.intersectsLineLine(
		            0, 0, 0, 1, 0, 0,
		            0.5, -1, 0, 0.5, 1, 0));
		}

		@DisplayName("#14 - Skew lines in 3D (non‑coplanar, no intersection)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1 along x‑axis at y=0,z=0; line2 vertical in z at x=0,y=1
		    assertFalse(Segment3afp.intersectsLineLine(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 0, 1, 1));
		}

		@DisplayName("#15 - Perpendicular lines intersecting at a point")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // vertical line x=0, horizontal line y=0.5
		    assertTrue(Segment3afp.intersectsLineLine(
		            0, 0, 0, 0, 1, 0,
		            -1, 0.5, 0, 1, 0.5, 0));
		}

		@DisplayName("#16 - Coincident lines with opposite direction")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line y=x, one segment from (0,0) to (1,1), the other from (-1,-1) to (0,0)
		    assertTrue(Segment3afp.intersectsLineLine(
		            0, 0, 0, 1, 1, 0,
		            -1, -1, 0, 0, 0, 0));
		}

		@DisplayName("#17 - Degenerate line (point) lying on another line")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point at origin, line through origin
		    assertTrue(Segment3afp.intersectsLineLine(
		            0, 0, 0, 0, 0, 0,
		            0, 0, 0, 1, 1, 0));
		}

		@DisplayName("#18 - Degenerate line (point) not on another line")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point at (1,1), line from (0,0) to (1,0)
		    assertFalse(Segment3afp.intersectsLineLine(
		            1, 1, 0, 1, 1, 0,
		            0, 0, 0, 1, 0, 0));
		}

	}

	@DisplayName("intersectsSegmentLineWithEnds")
	@Nested
	public class IntersectsSegmentLineWithEnds {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, 1, 1, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, 2, 2, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, .5, .5, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, .5, .5, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, 0, 0, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, -1, -1, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
					0, 0, 0, 1, 1, 0,
					-3, 0, 0, 4, 0, 0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
					-3, 0, 0, 4, 0, 0,
					0, 0, 0, 1, 1, 0));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentLineWithEnds(
					0, 0, 0, 1, 1, 0,
					-3, 0, 0, -2, 1, 0));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentLineWithEnds(
					0, 0, 0, 1, 1, 0,
					10, 0, 0, 9, -1, 0));
		}

		@DisplayName("#11 - Intersection at interior point")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (0,0) to (2,2), line through (0,2)-(2,0) intersects at (1,1)
		    assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
		            0, 0, 0, 2, 2, 0,
		            0, 2, 0, 2, 0, 0));
		}

		@DisplayName("#12 - Intersection at segment endpoint")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (0,0) to (1,0); line through (0,0)-(0,1) shares endpoint (0,0)
		    assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
		            0, 0, 0, 1, 0, 0,
		            0, 0, 0, 0, 1, 0));
		}

		@DisplayName("#13 - Parallel distinct (no intersection)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // horizontal segment y=0, line y=1 (parallel)
		    assertFalse(Segment3afp.intersectsSegmentLineWithEnds(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 1, 1, 0));
		}

		@DisplayName("#14 - Infinite line intersects outside segment")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (0,0) to (1,0); vertical line x=2 crosses at x=2 (outside)
		    assertFalse(Segment3afp.intersectsSegmentLineWithEnds(
		            0, 0, 0, 1, 0, 0,
		            2, -1, 0, 2, 1, 0));
		}

		@DisplayName("#15 - Segment is a point lying on the line")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point (1,1), line through (0,0)-(2,2)
		    assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
		            1, 1, 0, 1, 1, 0,
		            0, 0, 0, 2, 2, 0));
		}

		@DisplayName("#16 - Segment is a point not on the line")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point (1,0) and line along x-axis, actually (1,0) is on x-axis - choose a point off line
		    // point (0,1), line along x-axis
		    assertFalse(Segment3afp.intersectsSegmentLineWithEnds(
		            0, 1, 0, 0, 1, 0,
		            0, 0, 0, 1, 0, 0));
		}

		@DisplayName("#17 - Skew lines in 3D (no intersection)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment along x-axis (y=0,z=0); line parallel to z-axis at x=0,y=1 (skew)
		    assertFalse(Segment3afp.intersectsSegmentLineWithEnds(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 0, 1, 1));
		}

		@DisplayName("#18 - 3D coplanar intersection (point inside segment)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (0,0,0) to (2,0,0); line crossing at (1,0,0) in same plane
		    assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
		            0, 0, 0, 2, 0, 0,
		            1, -1, 0, 1, 1, 0));
		}

		@DisplayName("#19 - Collinear overlapping (line contains a portion of segment)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (1,0) to (3,0); line through (0,0)-(2,0) overlaps from x=1 to 2
		    assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
		            1, 0, 0, 3, 0, 0,
		            0, 0, 0, 2, 0, 0));
		}

		@DisplayName("#20 - Segment completely inside infinite line")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (0,0) to (1,0); line through (-1,0)-(2,0) contains it
		    assertTrue(Segment3afp.intersectsSegmentLineWithEnds(
		            0, 0, 0, 1, 0, 0,
		            -1, 0, 0, 2, 0, 0));
		}

		@DisplayName("#21 - No intersection in 3D (line and segment not coplanar)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (0,0,0) to (1,0,0); line from (0,1,0) to (0,1,1) - perpendicular but disjoint
		    assertFalse(Segment3afp.intersectsSegmentLineWithEnds(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 0, 1, 1));
		}
		
	}

	@DisplayName("intersectsSegmentLineWithoutEnds")
	@Nested
	public class IntersectsSegmentLineWithoutEnds {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
				0, 0, 0, 1, 1, 0,
				0, 0, 0, 1, 1, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, 2, 2, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, .5, .5, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, .5, .5, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, 0, 0, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, -1, -1, 0));
	
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentLineWithoutEnds(
					0, 0, 0, 1, 1, 0,
					-3, 0, 0, 4, 0, 0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
					-3, 0, 0, 4, 0, 0,
					0, 0, 0, 1, 1, 0));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentLineWithoutEnds(
					0, 0, 0, 1, 1, 0,
					-3, 0, 0, -2, 1, 0));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentLineWithoutEnds(
					0, 0, 0, 1, 1, 0,
					10, 0, 0, 9, -1, 0));
		}

		@DisplayName("#11 - Intersection strictly inside the segment")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (0,0) to (2,2); line through (0,2)-(2,0) intersects at (1,1) which is interior
		    assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
		            0, 0, 0, 2, 2, 0,
		            0, 2, 0, 2, 0, 0));
		}

		@DisplayName("#12 - Intersection at segment start (x1,y1) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (0,0) to (1,0); vertical line x=0 intersects at (0,0) which is start
		    assertFalse(Segment3afp.intersectsSegmentLineWithoutEnds(
		            0, 0, 0, 1, 0, 0,
		            0, -1, 0, 0, 1, 0));
		}

		@DisplayName("#13 - Intersection at segment end (x2,y2) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (0,0) to (1,0); vertical line x=1 intersects at (1,0) which is end
		    assertFalse(Segment3afp.intersectsSegmentLineWithoutEnds(
		            0, 0, 0, 1, 0, 0,
		            1, -1, 0, 1, 1, 0));
		}

		@DisplayName("#14 - Segment is a point lying on the line - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point (1,1) on line y=x; since segment is degenerate, no interior -> false
		    assertFalse(Segment3afp.intersectsSegmentLineWithoutEnds(
		            1, 1, 0, 1, 1, 0,
		            0, 0, 0, 2, 2, 0));
		}

		@DisplayName("#15 - Segment is a point not on the line - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point (0,1) off the x-axis line
		    assertFalse(Segment3afp.intersectsSegmentLineWithoutEnds(
		            0, 1, 0, 0, 1, 0,
		            0, 0, 0, 1, 0, 0));
		}

		@DisplayName("#16 - Collinear overlap with interior - true")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (0,0) to (2,0); line along x-axis from (-1,0) to (3,0)
		    // overlap includes interior points (e.g., (1,0)) -> true
		    assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
		            0, 0, 0, 2, 0, 0,
		            -1, 0, 0, 3, 0, 0));
		}

		@DisplayName("#17 - Collinear touch at endpoint only - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (0,0) to (1,0); line from (-2,0) to (0,0) intersects only at (0,0) (start) -> false
		    assertFalse(Segment3afp.intersectsSegmentLineWithoutEnds(
		            0, 0, 0, 1, 0, 0,
		            -2, 0, 0, 0, 0, 0));
		}

		@DisplayName("#18 - Parallel distinct - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // horizontal segment y=0, line y=1 (parallel)
		    assertFalse(Segment3afp.intersectsSegmentLineWithoutEnds(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 1, 1, 0));
		}

		@DisplayName("#19 - Skew lines in 3D - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment along x-axis (y=0,z=0); line parallel to z-axis at x=0,y=1 (skew)
		    assertFalse(Segment3afp.intersectsSegmentLineWithoutEnds(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 0, 1, 1));
		}

		@DisplayName("#20 - 3D interior intersection - true")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (0,0,0) to (2,2,2); line through (0,2,0)-(2,0,2) intersects at (1,1,1) (interior)
		    assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
		            0, 0, 0, 2, 2, 2,
		            0, 2, 0, 2, 0, 2));
		}

		@DisplayName("#21 - Intersection outside segment (line crosses extension) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (0,0) to (1,0); vertical line x=2 crosses at x=2 (outside) -> false
		    assertFalse(Segment3afp.intersectsSegmentLineWithoutEnds(
		            0, 0, 0, 1, 0, 0,
		            2, -1, 0, 2, 1, 0));
		}

		@DisplayName("#22 - Line intersects segment exactly at interior but also at an endpoint? (interior takes precedence) - true")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment from (0,0) to (2,0); line along x-axis from (0,0) to (1,0) only overlaps at (0,0) and (1,0)?
		    // Actually if line is subsegment of segment, it overlaps with interior (0.5,0) if line covers that range.
		    // Let's test line from (0,0) to (1,0): the overlap is exactly the line segment [0,1], which is a subsegment
		    // of the segment [0,2]; it includes interior points (e.g., 0.5) -> true.
		    assertTrue(Segment3afp.intersectsSegmentLineWithoutEnds(
		            0, 0, 0, 2, 0, 0,
		            0, 0, 0, 1, 0, 0));
		}

	}

	@DisplayName("intersectsSegmentSegmentWithEnds")
	@Nested
	public class IntersectsSegmentSegmentWithEnds {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
					0, 0, 0, 1, 1, 0,
					0, .5, 0, 1, .5, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, 1, 1, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, 2, 2, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, .5, .5, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, .5, .5, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, 0, 0, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, -1, -1, 0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
					0, 0, 0, 1, 1, 0,
					-3, 0, 0, 4, 0, 0));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
					0, 0, 0, 1, 1, 0,
					-3, -1, 0, 4, -1, 0));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
					0, 0, 0, 1, 1, 0,
					-3, -1, 0, -1, -1, 0));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
					0, 0, 0, 1, 1, 0,
					-3, 0, 0, -2, 1, 0));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
					0, 0, 0, 1, 1, 0,
					10, 0, 0, 9, -1, 0));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(
					Segment3afp.intersectsSegmentSegmentWithEnds(
							7, -5, 0, 1, 1, 0,
							4, -3, 0, 1, 1, 0));
		}

		@DisplayName("#14 - Endpoint touch (non-collinear): segment1 endpoint lies on segment2 interior")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1: (0,0)-(1,1), seg2: (0,1)-(1,0) intersect at (0.5,0.5) which is interior of both - already covered.
		    // Let's do a touch: seg1: (0,0)-(1,0) horizontal, seg2: (1,0)-(1,1) vertical, share (1,0) which is endpoint of both.
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
		            0, 0, 0, 1, 0, 0,
		            1, 0, 0, 1, 1, 0));
		}

		@DisplayName("#15 - Endpoint of one segment lies on the interior of the other (T-junction)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1 horizontal from (0,0) to (2,0); seg2 from (1,-1) to (1,0) - touches at (1,0) which is interior of seg1 and endpoint of seg2
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
		            0, 0, 0, 2, 0, 0,
		            1, -1, 0, 1, 0, 0));
		}

		@DisplayName("#16 - Segments are collinear and touch at exactly one endpoint")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1: (0,0)-(1,0); seg2: (1,0)-(2,0) - share (1,0)
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
		            0, 0, 0, 1, 0, 0,
		            1, 0, 0, 2, 0, 0));
		}

		@DisplayName("#17 - Collinear but disjoint (already have #7, but add another with gap)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1: (0,0)-(1,0); seg2: (2,0)-(3,0) - gap between 1 and 2
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
		            0, 0, 0, 1, 0, 0,
		            2, 0, 0, 3, 0, 0));
		}

		@DisplayName("#18 - One segment is a point lying on the interior of the other")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point (0.5,0,0) lies on segment from (0,0) to (1,0)
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
		            0.5, 0, 0, 0.5, 0, 0,
		            0, 0, 0, 1, 0, 0));
		}

		@DisplayName("#19 - One segment is a point lying on the endpoint of the other")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point (1,0,0) is endpoint of segment from (0,0) to (1,0)
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
		            1, 0, 0, 1, 0, 0,
		            0, 0, 0, 1, 0, 0));
		}

		@DisplayName("#20 - One segment is a point not on the other segment")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point (0,1,0) off the segment from (0,0) to (1,0)
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
		            0, 1, 0, 0, 1, 0,
		            0, 0, 0, 1, 0, 0));
		}

		@DisplayName("#21 - Both segments degenerate (points) - same point")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
		            0, 0, 0, 0, 0, 0,
		            0, 0, 0, 0, 0, 0));
		}

		@DisplayName("#22 - Both segments degenerate (points) - different points")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
		            0, 0, 0, 0, 0, 0,
		            1, 0, 0, 1, 0, 0));
		}

		@DisplayName("#23 - Skew lines in 3D (non-coplanar, no intersection)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_23(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1 along x-axis from (0,0,0) to (1,0,0); seg2 along z-axis at x=0,y=1, from (0,1,0) to (0,1,1)
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 0, 1, 1));
		}

		@DisplayName("#24 - 3D coplanar intersection (interior) - with non-zero z")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_24(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // similar to test #1 but all z=1
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
		            0, 0, 1, 1, 1, 1,
		            0, 0.5, 1, 1, 0.5, 1));
		}

		@DisplayName("#25 - 3D coplanar, no intersection (parallel but distinct)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_25(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1: x-axis at z=0, y=0; seg2: parallel x-axis at z=1, y=0 (distinct planes? Actually z differs, so parallel but not coplanar? They are parallel but not coplanar - that's skew? Actually parallel lines are always coplanar? Two parallel lines are coplanar (they define a plane). So they are coplanar but distinct, no intersection.
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithEnds(
		            0, 0, 0, 1, 0, 0,
		            0, 0, 1, 1, 0, 1));
		}

		@DisplayName("#26 - Intersection at endpoint in 3D (non-collinear)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_26(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1: (0,0,0)-(1,0,0); seg2: (1,0,0)-(1,1,0) - share (1,0,0)
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
		            0, 0, 0, 1, 0, 0,
		            1, 0, 0, 1, 1, 0));
		}

		@DisplayName("#27 - Intersection at one point that is interior to both in 3D")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_27(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1: (0,0,0)-(2,2,0); seg2: (0,2,0)-(2,0,0) intersect at (1,1,0)
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithEnds(
		            0, 0, 0, 2, 2, 0,
		            0, 2, 0, 2, 0, 0));
		}
	
	}

	@DisplayName("intersectsSegmentSegmentWithoutEnds")
	@Nested
	public class IntersectsSegmentSegmentWithoutEnds {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 0, 1, 1, 0,
					0, .5, 0, 1, .5, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, 1, 1, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, 2, 2, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 0, 1, 1, 0,
					0, 0, 0, .5, .5, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, .5, .5, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, 0, 0, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 0, 1, 1, 0,
					-3, -3, 0, -1, -1, 0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 0, 1, 1, 0,
					-3, 0, 0, 4, 0, 0));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 0, 1, 1, 0,
					-3, -1, 0, 4, -1, 0));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 0, 1, 1, 0,
					-3, -1, 0, -1, -1, 0));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 0, 1, 1, 0,
					-3, 0, 0, -2, 1, 0));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
					0, 0, 0, 1, 1, 0,
					10, 0, 0, 9, -1, 0));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(
					Segment3afp.intersectsSegmentSegmentWithoutEnds(
							7, -5, 0, 1, 1, 0,
							4, -3, 0, 1, 1, 0));
		}

		@DisplayName("#14 - Interior crossing (not at endpoints)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_interiorCrossing(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1: (0,0)-(2,2), seg2: (0,2)-(2,0) intersect at (1,1) which is interior of both
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 0, 2, 2, 0,
		            0, 2, 0, 2, 0, 0));
		}

		@DisplayName("#15 - Collinear overlap with interior")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_collinearOverlap(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1: (0,0)-(3,0), seg2: (1,0)-(2,0) - overlap has interior points
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 0, 3, 0, 0,
		            1, 0, 0, 2, 0, 0));
		}

		@DisplayName("#16 - One segment contained in the other (interior overlap)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_oneContained(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1: (0,0)-(10,0), seg2: (2,0)-(5,0) - seg2 is completely inside seg1
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 0, 10, 0, 0,
		            2, 0, 0, 5, 0, 0));
		}

		@DisplayName("#17 - Endpoint touch on interior of other (T‑junction) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_touchInterior(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1 horizontal: (0,0)-(2,0); seg2 vertical: (1,0)-(1,1) - touches at (1,0) which is interior of seg1 but endpoint of seg2
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 0, 2, 0, 0,
		            1, 0, 0, 1, 1, 0));
		}

		@DisplayName("#18 - Shared endpoint (collinear) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_sharedEndpointCollinear(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1: (0,0)-(1,0); seg2: (1,0)-(2,0) - share (1,0)
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 0, 1, 0, 0,
		            1, 0, 0, 2, 0, 0));
		}

		@DisplayName("#19 - Shared endpoint (non‑collinear) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_sharedEndpointNonCollinear(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1: (0,0)-(1,0); seg2: (1,0)-(1,1) - share (1,0)
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 0, 1, 0, 0,
		            1, 0, 0, 1, 1, 0));
		}

		@DisplayName("#20 - Degenerate segment (point) on interior - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_pointOnInterior(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point (1,0) lies on seg2: (0,0)-(2,0)
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            1, 0, 0, 1, 0, 0,
		            0, 0, 0, 2, 0, 0));
		}

		@DisplayName("#21 - Degenerate segment (point) on endpoint - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_pointOnEndpoint(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point (2,0) is endpoint of seg2: (0,0)-(2,0)
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            2, 0, 0, 2, 0, 0,
		            0, 0, 0, 2, 0, 0));
		}

		@DisplayName("#22 - Degenerate segment not on other - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_pointOffSegment(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point (0,1) not on seg2: (0,0)-(1,0)
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 1, 0, 0, 1, 0,
		            0, 0, 0, 1, 0, 0));
		}

		@DisplayName("#23 - Both degenerate (same point) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_bothDegenerateSame(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 0, 0, 0, 0,
		            0, 0, 0, 0, 0, 0));
		}

		@DisplayName("#24 - Both degenerate (different points) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_bothDegenerateDifferent(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 0, 0, 0, 0,
		            1, 0, 0, 1, 0, 0));
		}

		@DisplayName("#25 - Parallel distinct - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_parallelDistinct(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1 horizontal y=0, seg2 horizontal y=1
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 1, 1, 0));
		}

		@DisplayName("#26 - Collinear disjoint (gap) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_collinearDisjoint(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1: (0,0)-(1,0); seg2: (2,0)-(3,0)
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 0, 1, 0, 0,
		            2, 0, 0, 3, 0, 0));
		}

		@DisplayName("#27 - Skew lines in 3D - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_skew3D(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1 along x‑axis: (0,0,0)-(1,0,0); seg2 along z‑axis at x=0,y=1: (0,1,0)-(0,1,1)
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 0, 1, 1));
		}

		@DisplayName("#28 - 3D interior crossing (coplanar, non‑zero z) - true")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3DinteriorCrossing(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // same as #14 but with z=5
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 5, 2, 2, 5,
		            0, 2, 5, 2, 0, 5));
		}

		@DisplayName("#29 - 3D endpoint touch (endpoint on interior of other) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3DendpointTouch(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1: (0,0,0)-(2,0,0); seg2: (1,0,0)-(1,1,0) touches at (1,0,0) which is interior of seg1, endpoint of seg2
		    assertFalse(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 0, 2, 0, 0,
		            1, 0, 0, 1, 1, 0));
		}

		@DisplayName("#30 - Collinear overlap with interior in 3D - true")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3DcollinearOverlap(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1: (0,0,1)-(3,0,1); seg2: (1,0,1)-(2,0,1)
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 1, 3, 0, 1,
		            1, 0, 1, 2, 0, 1));
		}

		@DisplayName("#31 - Segments intersect at one point interior of both but one is perpendicular - true")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_perpendicularInterior(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // seg1 horizontal: (0,0)-(2,0); seg2 vertical: (1,-1)-(1,1) - intersect at (1,0) which is interior of both
		    assertTrue(Segment3afp.intersectsSegmentSegmentWithoutEnds(
		            0, 0, 0, 2, 0, 0,
		            1, -1, 0, 1, 1, 0));
		}

	}

	@DisplayName("isColinearLines")
	@Nested
	public class IsCollinearLines {
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.isColinearLines(0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0));
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.isColinearLines(0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0));
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			assertTrue(Segment3afp.isColinearLines(0, 0, 0, 1, 1, 0, 0, 0, 0, -1, -1, 0));
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			assertTrue(Segment3afp.isColinearLines(0, 0, 0, 1, 1, 0, -2, -2, 0, -3, -3, 0));
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			assertFalse(Segment3afp.isColinearLines(0, 0, 0, 1, 1, 0, 5, 0, 0, 6, 1, 0));
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			assertFalse(Segment3afp.isColinearLines(0, 0, 0, 1, 1, 0, 154, -124, 0, -2, 457, 0));
		}

		@DisplayName("#7 - Same line in 3D with non-zero z")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line along (1,1,1) through origin
		    assertTrue(Segment3afp.isColinearLines(
		            0, 0, 0, 1, 1, 1,
		            2, 2, 2, 3, 3, 3));
		}

		@DisplayName("#8 - Same line in 3D, different points, opposite direction")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertFalse(Segment3afp.isColinearLines(
		            1, 2, 3, 3, 2, 4,
		            5, 2, 5, -1, 2, 1)); 
		}

		@DisplayName("#9 - Parallel distinct in 3D (same direction, different offset) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: x-axis (y=0,z=0), line2: x-axis shifted to y=1,z=0 (parallel distinct)
		    assertFalse(Segment3afp.isColinearLines(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 1, 1, 0));
		}

		@DisplayName("#10 - Non-parallel intersecting in 3D (coplanar, not collinear) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: x-axis (0,0,0)-(1,0,0), line2: z-axis at x=0 (0,0,0)-(0,0,1) - intersect at origin but not collinear
		    assertFalse(Segment3afp.isColinearLines(
		            0, 0, 0, 1, 0, 0,
		            0, 0, 0, 0, 0, 1));
		}

		@DisplayName("#11 - Skew lines in 3D (non-coplanar) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: x-axis (0,0,0)-(1,0,0), line2: line through (0,1,0) with direction (0,1,1) - skew
		    assertFalse(Segment3afp.isColinearLines(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 0, 2, 1));
		}

		@DisplayName("#12 - Degenerate line (point) on another line in 3D - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point (2,3,4) lies on line through (0,0,0)-(1,1,1)? Actually (2,3,4) is not on that line.
		    // We need a point on the line: line through (1,2,3) with direction (2,0,1). Choose point (3,2,4).
		    assertFalse(Segment3afp.isColinearLines(
		            3, 2, 4, 3, 2, 4,   // degenerate point
		            1, 2, 3, 3, 2, 4)); // line through (1,2,3)-(3,2,4)
		}

		@DisplayName("#13 - Degenerate line (point) not on line - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertFalse(Segment3afp.isColinearLines(
		            0, 1, 0, 0, 1, 0,
		            0, 0, 0, 1, 0, 0));
		}

		@DisplayName("#14 - Both lines degenerate, same point - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertFalse(Segment3afp.isColinearLines(
		            1, 2, 3, 1, 2, 3,
		            1, 2, 3, 1, 2, 3));
		}

		@DisplayName("#15 - Both lines degenerate, different points - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertFalse(Segment3afp.isColinearLines(
		            0, 0, 0, 0, 0, 0,
		            1, 0, 0, 1, 0, 0));
		}

		@DisplayName("#16 - Lines that are collinear but described with non-unit direction and offset - true")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: (0,0,0)-(2,4,6), line2: (1,2,3)-(3,6,9) - both along (1,2,3)
		    assertTrue(Segment3afp.isColinearLines(
		            0, 0, 0, 2, 4, 6,
		            1, 2, 3, 3, 6, 9));
		}

		@DisplayName("#17 - Parallel lines with same direction but different offset in 3D - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: (0,0,0)-(1,1,1), line2: (0,1,0)-(1,2,1) - both direction (1,1,1) but not through same point
		    assertFalse(Segment3afp.isColinearLines(
		            0, 0, 0, 1, 1, 1,
		            0, 1, 0, 1, 2, 1));
		}

		@DisplayName("#18 - Lines that intersect at a point but are not parallel (non-collinear) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: (0,0,0)-(2,0,0), line2: (1,0,0)-(1,1,0) - intersect at (1,0,0) but not collinear
		    assertFalse(Segment3afp.isColinearLines(
		            0, 0, 0, 2, 0, 0,
		            1, 0, 0, 1, 1, 0));
		}

	}

	@DisplayName("isParallelLines")
	@Nested
	public class IsParallelLines {
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0));
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0));
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 0, 0, 0, -1, -1, 0));
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, -2, -2, 0, -3, -3, 0));
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			assertTrue(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 5, 0, 0, 6, 1, 0));
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			assertFalse(Segment3afp.isParallelLines(0, 0, 0, 1, 1, 0, 154, -124, 0, -2, 457, 0));
		}

		@DisplayName("#7 - Same line in 3D with non-zero z")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line along (1,1,1) through origin
		    assertTrue(Segment3afp.isParallelLines(
		            0, 0, 0, 1, 1, 1,
		            2, 2, 2, 3, 3, 3));
		}

		@DisplayName("#8 - Same line in 3D, different points, opposite direction")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertFalse(Segment3afp.isParallelLines(
		            1, 2, 3, 3, 2, 4,
		            5, 2, 5, -1, 2, 1)); 
		}

		@DisplayName("#9 - Parallel distinct in 3D (same direction, different offset) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: x-axis (y=0,z=0), line2: x-axis shifted to y=1,z=0 (parallel distinct)
		    assertTrue(Segment3afp.isParallelLines(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 1, 1, 0));
		}

		@DisplayName("#10 - Non-parallel intersecting in 3D (coplanar, not collinear) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: x-axis (0,0,0)-(1,0,0), line2: z-axis at x=0 (0,0,0)-(0,0,1) - intersect at origin but not collinear
		    assertFalse(Segment3afp.isParallelLines(
		            0, 0, 0, 1, 0, 0,
		            0, 0, 0, 0, 0, 1));
		}

		@DisplayName("#11 - Skew lines in 3D (non-coplanar) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: x-axis (0,0,0)-(1,0,0), line2: line through (0,1,0) with direction (0,1,1) - skew
		    assertFalse(Segment3afp.isParallelLines(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 0, 2, 1));
		}

		@DisplayName("#12 - Degenerate line (point) on another line in 3D - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point (2,3,4) lies on line through (0,0,0)-(1,1,1)? Actually (2,3,4) is not on that line.
		    // We need a point on the line: line through (1,2,3) with direction (2,0,1). Choose point (3,2,4).
		    assertFalse(Segment3afp.isParallelLines(
		            3, 2, 4, 3, 2, 4,   // degenerate point
		            1, 2, 3, 3, 2, 4)); // line through (1,2,3)-(3,2,4)
		}

		@DisplayName("#13 - Degenerate line (point) not on line - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertFalse(Segment3afp.isParallelLines(
		            0, 1, 0, 0, 1, 0,
		            0, 0, 0, 1, 0, 0));
		}

		@DisplayName("#14 - Both lines degenerate, same point - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertFalse(Segment3afp.isParallelLines(
		            1, 2, 3, 1, 2, 3,
		            1, 2, 3, 1, 2, 3));
		}

		@DisplayName("#15 - Both lines degenerate, different points - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertFalse(Segment3afp.isParallelLines(
		            0, 0, 0, 0, 0, 0,
		            1, 0, 0, 1, 0, 0));
		}

		@DisplayName("#16 - Lines that are collinear but described with non-unit direction and offset - true")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: (0,0,0)-(2,4,6), line2: (1,2,3)-(3,6,9) - both along (1,2,3)
		    assertTrue(Segment3afp.isColinearLines(
		            0, 0, 0, 2, 4, 6,
		            1, 2, 3, 3, 6, 9));
		}

		@DisplayName("#17 - Parallel lines with same direction but different offset in 3D - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: (0,0,0)-(1,1,1), line2: (0,1,0)-(1,2,1) - both direction (1,1,1) but not through same point
		    assertTrue(Segment3afp.isParallelLines(
		            0, 0, 0, 1, 1, 1,
		            0, 1, 0, 1, 2, 1));
		}

		@DisplayName("#18 - Lines that intersect at a point but are not parallel (non-collinear) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: (0,0,0)-(2,0,0), line2: (1,0,0)-(1,1,0) - intersect at (1,0,0) but not collinear
		    assertFalse(Segment3afp.isParallelLines(
		            0, 0, 0, 2, 0, 0,
		            1, 0, 0, 1, 1, 0));
		}

		@DisplayName("#19 - Parallel lines with same direction, different z-offset")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // both lines along (1,0,0) but at different z heights
		    assertTrue(Segment3afp.isParallelLines(
		            0, 0, 0, 1, 0, 0,
		            0, 0, 5, 1, 0, 5));
		}

		@DisplayName("#20 - Parallel lines with scaled direction and different offset")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // direction (2,4,6) and (1,2,3) - scalar multiples
		    assertTrue(Segment3afp.isParallelLines(
		            0, 0, 0, 2, 4, 6,
		            1, 1, 1, 2, 3, 4));
		}

		@DisplayName("#21 - Non-parallel lines in 3D with similar but not proportional directions")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1 direction (1,0,0), line2 direction (1,1,0) - not parallel
		    assertFalse(Segment3afp.isParallelLines(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 1, 2, 0));
		}

		@DisplayName("#22 - Non-parallel lines with one direction zero (degenerate point) - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // first line is a point, second line is x-axis - a point has no direction, so often considered parallel
		    assertFalse(Segment3afp.isParallelLines(
		            0, 0, 0, 0, 0, 0,
		            0, 0, 0, 1, 0, 0));
		}

		@DisplayName("#23 - Both lines are degenerate points - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_23(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // two points - no direction
		    assertFalse(Segment3afp.isParallelLines(
		            1, 2, 3, 1, 2, 3,
		            4, 5, 6, 4, 5, 6));
		}

		@DisplayName("#24 - Non-parallel lines that are coplanar and intersect (2D case in 3D)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_24(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: y=x in z=0 plane; line2: y=-x in z=0 plane - intersect at (0,0,0) but not parallel
		    assertFalse(Segment3afp.isParallelLines(
		            0, 0, 0, 1, 1, 0,
		            0, 0, 0, 1, -1, 0));
		}

		@DisplayName("#25 - Non-parallel skew lines with directions not proportional")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_25(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1: (0,0,0)-(1,0,0), line2: (0,1,0)-(0,2,1) - directions (1,0,0) and (0,1,1) not parallel
		    assertFalse(Segment3afp.isParallelLines(
		            0, 0, 0, 1, 0, 0,
		            0, 1, 0, 0, 2, 1));
		}

		@DisplayName("#26 - Parallel lines with large coordinate values")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_26(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // both lines with direction (1e6, 2e6, 3e6)
		    assertTrue(Segment3afp.isParallelLines(
		            0, 0, 0, 1e6, 2e6, 3e6,
		            1, 1, 1, 1e6+1, 2e6+1, 3e6+1));
		}

		@DisplayName("#27 - Parallel lines with negative direction")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_27(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // line1 direction (1,1,0), line2 direction (-2,-2,0) - scalar multiple
		    assertTrue(Segment3afp.isParallelLines(
		            0, 0, 0, 1, 1, 0,
		            5, 5, 0, 3, 3, 0));
		}

		@DisplayName("#28 - Non-parallel lines with zero length on one side (point) but point not on line - false")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_28(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point at (0,1,0) and x-axis - point is not on line, but isParallelLines typically returns false for degenerate
		    assertFalse(Segment3afp.isParallelLines(
		            0, 1, 0, 0, 1, 0,
		            0, 0, 0, 1, 0, 0));
		}

		@DisplayName("#29 - Lines that are collinear (share infinite points) - always parallel")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_29(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // same line, overlapping
		    assertTrue(Segment3afp.isParallelLines(
		            0, 0, 0, 2, 2, 0,
		            1, 1, 0, 3, 3, 0));
		}

	}

	@DisplayName("isPointCloseToLine")
	@Nested
	public class IsPointCloseToLine {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.isPointCloseToLine(0, 0, 0, 1, 1, 0, 0, 0, 0, 0.1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.isPointCloseToLine(0, 0, 0, 1, 1, 0, 1, 1, 0, 0.1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.isPointCloseToLine(0, 0, 0, 1, 1, 0, .25, .25, 0, 0.1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.isPointCloseToLine(0, 0, 0, 1, 1, 0, 0.2, 0, 0, 0.1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.isPointCloseToLine(0, 0, 0, 1, 1, 0, 120, 0, 0, 0.1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.isPointCloseToLine(0, 0, 0, 1, 1, 0, -20.05, -20, 0, 0.1));
		}

		@DisplayName("#7 - 3D point on segment, zero distance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertTrue(Segment3afp.isPointCloseToLine(
		            0, 0, 0, 10, 0, 0,
		            5, 0, 0,
		            GeomConstants.COLINEAR_LINE_EPSILON));
		}

		@DisplayName("#8 - 3D point at exact distance (interior projection)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment along x, point at (5, 3, 4) -> distance = sqrt(9+16)=5
		    assertTrue(Segment3afp.isPointCloseToLine(
		            0, 0, 0, 10, 0, 0,
		            5, 3, 4,
		            5.0 + GeomConstants.COLINEAR_LINE_EPSILON));
		}

		@DisplayName("#9 - 3D point just outside distance (interior projection)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // distance = 5.1, hitDistance = 5.0
		    assertFalse(Segment3afp.isPointCloseToLine(
		            0, 0, 0, 10, 0, 0,
		            5, 3, 4.1,
		            5.0));
		}

		@DisplayName("#10 - Projection before start, point within distance to start")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment (0,0)-(10,0), point (-3, 4) -> distance to start = 5
		    assertTrue(Segment3afp.isPointCloseToLine(
		            0, 0, 0, 10, 0, 0,
		            -3, 4, 0,
		            5.0));
		}

		@DisplayName("#11 - Projection before start")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // distance to start = 5, hitDistance = 4.9
		    assertTrue(Segment3afp.isPointCloseToLine(
		            0, 0, 0, 10, 0, 0,
		            -3, 4, 0,
		            4.9));
		}

		@DisplayName("#12 - Projection after end, point within distance to end")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment (0,0)-(10,0), point (13, 0) -> distance to end = 3
		    assertTrue(Segment3afp.isPointCloseToLine(
		            0, 0, 0, 10, 0, 0,
		            13, 0, 0,
		            3.0));
		}

		@DisplayName("#13 - Projection after end")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertTrue(Segment3afp.isPointCloseToLine(
		            0, 0, 0, 10, 0, 0,
		            13, 0, 0,
		            2.9));
		}

		@DisplayName("#14 - Degenerate segment (point), point at same location")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertTrue(Segment3afp.isPointCloseToLine(
		            5, 5, 5, 5, 5, 5,
		            5, 5, 5,
		            GeomConstants.COLINEAR_LINE_EPSILON));
		}

		@DisplayName("#15 - Degenerate segment, point at exact distance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertTrue(Segment3afp.isPointCloseToLine(
		            0, 0, 0, 0, 0, 0,
		            3, 4, 0,
		            5.0 + GeomConstants.COLINEAR_LINE_EPSILON));
		}

		@DisplayName("#16 - Degenerate segment, point just outside")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertFalse(Segment3afp.isPointCloseToLine(
		            0, 0, 0, 0, 0, 0,
		            3, 4, 0,
		            4.9));
		}

		@DisplayName("#17 - Negative hitDistance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertThrows(AssertionError.class, () -> {
		    		Segment3afp.isPointCloseToLine(
		            0, 0, 0, 10, 0, 0,
		            5, 0, 0,
		            -1.0);
		    });
		}

		@DisplayName("#18 - Large coordinates, point near segment")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    double base = 1_000_000;
		    assertTrue(Segment3afp.isPointCloseToLine(
		            base, 0, 0, base+10, 0, 0,
		            base+5, 3, 0,
		            3.1));
		}

		@DisplayName("#19 - Large coordinates, point far")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    double base = 1_000_000;
		    assertFalse(Segment3afp.isPointCloseToLine(
		            base, 0, 0, base+10, 0, 0,
		            base+5, 100, 0,
		            10.0));
		}

		@DisplayName("#20 - Point exactly on the line extension beyond start, within distance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point (-2,0) lies on line extension before start (0,0), distance to start = 2
		    assertTrue(Segment3afp.isPointCloseToLine(
		            0, 0, 0, 10, 0, 0,
		            -2, 0, 0,
		            2.0));
		}

		@DisplayName("#21 - Point exactly on the line extension beyond end, within distance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertTrue(Segment3afp.isPointCloseToLine(
		            0, 0, 0, 10, 0, 0,
		            12, 0, 0,
		            2.0));
		}

		@DisplayName("#22 - 3D point with projection outside but distance to start")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment (0,0,0)-(10,0,0), point (-1,0,3) -> distance to start = sqrt(1+9)=√10≈3.162
		    assertTrue(Segment3afp.isPointCloseToLine(
		            0, 0, 0, 10, 0, 0,
		            -1, 0, 3,
		            3.17));
		}

	}

	@DisplayName("isPointCloseToSegment")
	@Nested
	public class IsPointCloseToSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.isPointCloseToSegment(0, 0, 0, 1, 1, 0, 0, 0, 0, 0.1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.isPointCloseToSegment(0, 0, 0, 1, 1, 0, 1, 1, 0, 0.1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.isPointCloseToSegment(0, 0, 0, 1, 1, 0, .25, .25, 0, 0.1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.isPointCloseToSegment(0, 0, 0, 1, 1, 0, 0.2, 0, 0, 0.1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.isPointCloseToSegment(0, 0, 0, 1, 1, 0, 120, 0, 0, 0.1));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Segment3afp.isPointCloseToSegment(0, 0, 0, 1, 1, 0, -20.05, -20, 0, 0.1));
		}

		@DisplayName("#7 - 3D point on segment, zero distance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertTrue(Segment3afp.isPointCloseToSegment(
		            0, 0, 0, 10, 0, 0,
		            5, 0, 0,
		            GeomConstants.COLINEAR_LINE_EPSILON));
		}

		@DisplayName("#8 - 3D point at exact distance (interior projection)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment along x, point at (5, 3, 4) -> distance = sqrt(9+16)=5
		    assertTrue(Segment3afp.isPointCloseToSegment(
		            0, 0, 0, 10, 0, 0,
		            5, 3, 4,
		            5.0 + GeomConstants.COLINEAR_LINE_EPSILON));
		}

		@DisplayName("#9 - 3D point just outside distance (interior projection)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // distance = 5.1, hitDistance = 5.0
		    assertFalse(Segment3afp.isPointCloseToSegment(
		            0, 0, 0, 10, 0, 0,
		            5, 3, 4.1,
		            5.0));
		}

		@DisplayName("#10 - Projection before start, point within distance to start")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment (0,0)-(10,0), point (-3, 4) -> distance to start = 5
		    assertTrue(Segment3afp.isPointCloseToSegment(
		            0, 0, 0, 10, 0, 0,
		            -3, 4, 0,
		            5.0 + GeomConstants.COLINEAR_LINE_EPSILON));
		}

		@DisplayName("#11 - Projection before start")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // distance to start = 5, hitDistance = 4.9
		    assertFalse(Segment3afp.isPointCloseToSegment(
		            0, 0, 0, 10, 0, 0,
		            -3, 4, 0,
		            4.9));
		}

		@DisplayName("#12 - Projection after end, point within distance to end")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment (0,0)-(10,0), point (13, 0) -> distance to end = 3
		    assertTrue(Segment3afp.isPointCloseToSegment(
		            0, 0, 0, 10, 0, 0,
		            13, 0, 0,
		            3.0 + GeomConstants.COLINEAR_LINE_EPSILON));
		}

		@DisplayName("#13 - Projection after end")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertFalse(Segment3afp.isPointCloseToSegment(
		            0, 0, 0, 10, 0, 0,
		            13, 0, 0,
		            2.9));
		}

		@DisplayName("#14 - Degenerate segment (point), point at same location")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertTrue(Segment3afp.isPointCloseToSegment(
		            5, 5, 5, 5, 5, 5,
		            5, 5, 5,
		            GeomConstants.COLINEAR_LINE_EPSILON));
		}

		@DisplayName("#15 - Degenerate segment, point at exact distance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertTrue(Segment3afp.isPointCloseToSegment(
		            0, 0, 0, 0, 0, 0,
		            3, 4, 0,
		            5.0 + GeomConstants.COLINEAR_LINE_EPSILON));
		}

		@DisplayName("#16 - Degenerate segment, point just outside")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertFalse(Segment3afp.isPointCloseToSegment(
		            0, 0, 0, 0, 0, 0,
		            3, 4, 0,
		            4.9));
		}

		@DisplayName("#17 - Negative hitDistance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertThrows(AssertionError.class, () -> {
		    		Segment3afp.isPointCloseToSegment(
		            0, 0, 0, 10, 0, 0,
		            5, 0, 0,
		            -1.0);
		    });
		}

		@DisplayName("#18 - Large coordinates, point near segment")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    double base = 1_000_000;
		    assertTrue(Segment3afp.isPointCloseToSegment(
		            base, 0, 0, base+10, 0, 0,
		            base+5, 3, 0,
		            3.1));
		}

		@DisplayName("#19 - Large coordinates, point far")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    double base = 1_000_000;
		    assertFalse(Segment3afp.isPointCloseToSegment(
		            base, 0, 0, base+10, 0, 0,
		            base+5, 100, 0,
		            10.0));
		}

		@DisplayName("#20 - Point exactly on the line extension beyond start, within distance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // point (-2,0) lies on line extension before start (0,0), distance to start = 2
		    assertTrue(Segment3afp.isPointCloseToSegment(
		            0, 0, 0, 10, 0, 0,
		            -2, 0, 0,
		            2.0 + GeomConstants.COLINEAR_LINE_EPSILON));
		}

		@DisplayName("#21 - Point exactly on the line extension beyond end, within distance")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertTrue(Segment3afp.isPointCloseToLine(
		            0, 0, 0, 10, 0, 0,
		            12, 0, 0,
		            2.0));
		}

		@DisplayName("#22 - 3D point with projection outside but distance to start")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
		    CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    // segment (0,0,0)-(10,0,0), point (-1,0,3) -> distance to start = sqrt(1+9)=√10≈3.162
		    assertTrue(Segment3afp.isPointCloseToSegment(
		            0, 0, 0, 10, 0, 0,
		            -1, 0, 3,
		            3.17));
		}

	}

	@DisplayName("clone")
	@Nested
	public class CloneTest {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			T clone = getS().clone();
			assertNotNull(clone);
			assertNotSame(getS(), clone);
			assertEquals(getS().getClass(), clone.getClass());
			assertEpsilonEquals(0, clone.getX1());
			assertEpsilonEquals(0, clone.getY1());
			assertEpsilonEquals(0, clone.getZ1());
			assertEpsilonEquals(1, clone.getX2());
			assertEpsilonEquals(1, clone.getY2());
			assertEpsilonEquals(1, clone.getZ2());
		}
	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsObject {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(new Object()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSegment(0, 0, 0, 5, 0, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSegment(0, 0, 0, 2, 2, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(getS()));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSphere(5, 8, 0, 6)));
		}
	}

	@DisplayName("equalsToShape")
	@Nested
	public class EqualsToShape {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) createSegment(0, 0, 0, 5, 0, 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) createSegment(0, 0, 0, 2, 2, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape(getS()));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape((T) createSegment(0, 0, 0, 1, 1, 1)));
		}
	}

	@DisplayName("isEmpty")
	@Nested
	public class IsEmpty {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().isEmpty());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertTrue(getS().isEmpty());
		}
	}

	@DisplayName("clear")
	@Nested
	public class Clear {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getZ1());
			assertEpsilonEquals(0, getS().getX2());
			assertEpsilonEquals(0, getS().getY2());
			assertEpsilonEquals(0, getS().getZ2());
		}
	}

	@DisplayName("getP1")
	@Nested
	public class GetP1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Point3D p = getS().getP1();
			assertNotNull(p);
			assertEpsilonEquals(createPoint(0, 0, 0), p);
		}
	}

	@DisplayName("getP2")
	@Nested
	public class GetP2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Point3D p = getS().getP2();
			assertNotNull(p);
			assertEpsilonEquals(createPoint(1, 1, 1), p);
		}
	}

	@DisplayName("setP1")
	@Nested
	public class SetP1 {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP1(123.456, -789.159, -1);
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(-789.159, getS().getY1());
			assertEpsilonEquals(-1, getS().getZ1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(1, getS().getZ2());
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP1(createPoint(123.456, -789.159, -1));
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(-789.159, getS().getY1());
			assertEpsilonEquals(-1, getS().getZ1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(1, getS().getZ2());
		}
	}

	@DisplayName("setP2")
	@Nested
	public class SetP2 {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP2(123.456, -789.159, -1);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getZ1());
			assertEpsilonEquals(123.456, getS().getX2());
			assertEpsilonEquals(-789.159, getS().getY2());
			assertEpsilonEquals(-1, getS().getZ2());
		}
	
		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP2(createPoint(123.456, -789.159, -1));
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getZ1());
			assertEpsilonEquals(123.456, getS().getX2());
			assertEpsilonEquals(-789.159, getS().getY2());
			assertEpsilonEquals(-1, getS().getZ2());
		}
	}

	@DisplayName("getX1")
	@Nested
	public class GetX1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getX1());
		}
	}

	@DisplayName("getX2")
	@Nested
	public class GetX2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getX2());
		}
	}
	
	@DisplayName("getY1")
	@Nested
	public class GetY1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(0, getS().getY1());
		}
	}
	
	@DisplayName("getY2")
	@Nested
	public class GetY2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    assertEpsilonEquals(1, getS().getY2());
		}
	}

	@DisplayName("getZ1")
	@Nested
	public class GetZ1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getZ1());
		}
	}

	@DisplayName("getZ2")
	@Nested
	public class GetZ2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getZ2());
		}
	}

	@DisplayName("getLength")
	@Nested
	public class GetLength {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(Math.sqrt(3), getS().getLength());
		}
	}

	@DisplayName("getLengthSquared")
	@Nested
	public class GetLengthSquared {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, getS().getLengthSquared());
		}
	}

	@DisplayName("set")
	@Nested
	public class Set {

		@DisplayName("(double,double,double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledoubledoubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(123.456, 456.789, 456.123, 789.123, 159.753, 789.456);
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(456.789, getS().getY1());
			assertEpsilonEquals(456.123, getS().getZ1());
			assertEpsilonEquals(789.123, getS().getX2());
			assertEpsilonEquals(159.753, getS().getY2());
			assertEpsilonEquals(789.456, getS().getZ2());
		}
	
		@DisplayName("(Point3D,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(createPoint(123.456, 456.789, 456.123), createPoint(789.123, 159.753, 789.456));
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(456.789, getS().getY1());
			assertEpsilonEquals(456.123, getS().getZ1());
			assertEpsilonEquals(789.123, getS().getX2());
			assertEpsilonEquals(159.753, getS().getY2());
			assertEpsilonEquals(789.456, getS().getZ2());
		}
	}

	@DisplayName("setX1")
	@Nested
	public class SetX1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setX1(123.456);
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getZ1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(1, getS().getZ2());
		}
	}

	@DisplayName("setX2")
	@Nested
	public class SetX2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setX2(123.456);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getZ1());
			assertEpsilonEquals(123.456, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(1, getS().getZ2());
		}
	}
	
	@DisplayName("setY1")
	@Nested
	public class SetY1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    getS().setY1(123.456);
		    assertEpsilonEquals(0, getS().getX1());
		    assertEpsilonEquals(123.456, getS().getY1());
		    assertEpsilonEquals(0, getS().getZ1());
		    assertEpsilonEquals(1, getS().getX2());
		    assertEpsilonEquals(1, getS().getY2());
		    assertEpsilonEquals(1, getS().getZ2());
		}
	}
	
	@DisplayName("setY2")
	@Nested
	public class SetY2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		    getS().setY2(123.456);
		    assertEpsilonEquals(0, getS().getX1());
		    assertEpsilonEquals(0, getS().getY1());
		    assertEpsilonEquals(0, getS().getZ1());
		    assertEpsilonEquals(1, getS().getX2());
		    assertEpsilonEquals(123.456, getS().getY2());
		    assertEpsilonEquals(1, getS().getZ2());
		}
	}

	@DisplayName("setZ1")
	@Nested
	public class SetZ1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setZ1(123.456);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(123.456, getS().getZ1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(1, getS().getZ2());
		}
	}

	@DisplayName("setZ2")
	@Nested
	public class SetZ2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setZ2(123.456);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getZ1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(123.456, getS().getZ2());
		}
	}
	
	@DisplayName("transform")
	@Nested
	public class Transform {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();    	
			var s = (Segment3afp) getS().clone();
			s.transform(tr);
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(1, s.getX2());
			assertEpsilonEquals(1, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.makeTranslationMatrix(3.4, 4.5, 1.3);
			var s = (Segment3afp) getS().clone();
			s.transform(tr);
			assertEpsilonEquals(3.4, s.getX1());
			assertEpsilonEquals(4.5, s.getY1());
			assertEpsilonEquals(1.3, s.getZ1());
			assertEpsilonEquals(4.4, s.getX2());
			assertEpsilonEquals(5.5, s.getY2());
			assertEpsilonEquals(2.3, s.getZ2());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.makeRotationMatrix(newAxisAngleZ(MathConstants.PI));
			var s = (Segment3afp) getS().clone();
			s.transform(tr);
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(-1, s.getX2());
			assertEpsilonEquals(-1, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.makeRotationMatrix(newAxisAngleZ(MathConstants.QUARTER_PI));
			var s = (Segment3afp) getS().clone();
			s.transform(tr);
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(0, s.getX2());
			assertEpsilonEquals(1.41421356, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}
	}

	@DisplayName("createTransformedShape")
	@Nested
	public class CreateTransformedShape {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();    	
			var s = (Segment3afp) getS().createTransformedShape(tr);
			assertNotSame(getS(), s);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getZ1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(1, getS().getZ2());
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(1, s.getX2());
			assertEpsilonEquals(1, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.makeTranslationMatrix(3.4, 4.5, 1.3);
			var s = (Segment3afp) getS().createTransformedShape(tr);
			assertNotSame(getS(), s);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getZ1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(1, getS().getZ2());
			assertEpsilonEquals(3.4, s.getX1());
			assertEpsilonEquals(4.5, s.getY1());
			assertEpsilonEquals(1.3, s.getZ1());
			assertEpsilonEquals(4.4, s.getX2());
			assertEpsilonEquals(5.5, s.getY2());
			assertEpsilonEquals(2.3, s.getZ2());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.makeRotationMatrix(newAxisAngleZ(MathConstants.PI));
			var s = (Segment3afp) getS().createTransformedShape(tr);
			assertNotSame(getS(), s);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getZ1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(1, getS().getZ2());
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(-1, s.getX2());
			assertEpsilonEquals(-1, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.makeRotationMatrix(newAxisAngleZ(MathConstants.QUARTER_PI));
			var s = (Segment3afp) getS().createTransformedShape(tr);
			assertNotSame(getS(), s);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getZ1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(1, getS().getZ2());
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(0, s.getX2());
			assertEpsilonEquals(1.41421356, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}
	}

	@DisplayName("s * Transform3D")
	@Nested
	public class OperatorMultiplyTransform3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();    	
			var s = (Segment3afp) getS().operator_multiply(tr);
			assertNotSame(getS(), s);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getZ1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(1, getS().getZ2());
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(1, s.getX2());
			assertEpsilonEquals(1, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.makeTranslationMatrix(3.4, 4.5, 1.3);
			var s = (Segment3afp) getS().operator_multiply(tr);
			assertNotSame(getS(), s);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getZ1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(1, getS().getZ2());
			assertEpsilonEquals(3.4, s.getX1());
			assertEpsilonEquals(4.5, s.getY1());
			assertEpsilonEquals(1.3, s.getZ1());
			assertEpsilonEquals(4.4, s.getX2());
			assertEpsilonEquals(5.5, s.getY2());
			assertEpsilonEquals(2.3, s.getZ2());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.makeRotationMatrix(newAxisAngleZ(MathConstants.PI));
			var s = (Segment3afp) getS().operator_multiply(tr);
			assertNotSame(getS(), s);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getZ1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(1, getS().getZ2());
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(-1, s.getX2());
			assertEpsilonEquals(-1, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.makeRotationMatrix(newAxisAngleZ(MathConstants.QUARTER_PI));
			var s = (Segment3afp) getS().operator_multiply(tr);
			assertNotSame(getS(), s);
			assertEpsilonEquals(0, getS().getX1());
			assertEpsilonEquals(0, getS().getY1());
			assertEpsilonEquals(0, getS().getZ1());
			assertEpsilonEquals(1, getS().getX2());
			assertEpsilonEquals(1, getS().getY2());
			assertEpsilonEquals(1, getS().getZ2());
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(0, s.getX2());
			assertEpsilonEquals(1.41421356, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}
	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBox(0, 0, 0, 1, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBox(0, 0, 0, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBox(10, 10, 0, 1, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(10, 15, 0, 10, 18, 0);
			assertTrue(getS().contains(createAlignedBox(10, 16, 0, 0, 1, 0)));
		}

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(0, 0, 0));
		}

		@DisplayName("(double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(.5, .5, .5));
		}

		@DisplayName("(double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(1, 1, 1));
		}

		@DisplayName("(double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(2.3, 4.5, 0));
		}

		@DisplayName("(double,double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(2, 2, 0));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(.5, .5, .5)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(1, 1, 1)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(2.3, 4.5, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(2, 2, 0)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(-1, -1, -1)));
		}
	}

	@DisplayName("getFarthestPointTo")
	@Nested
	public class getFarthestPointTo {
		
		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(0, 0, 0));
			assertEpsilonEquals(1, p.getX());
			assertEpsilonEquals(1, p.getY());
			assertEpsilonEquals(1, p.getZ());
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(.5, .5, .5));
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
			assertEpsilonEquals(0, p.getZ());
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(1, 1, 1));
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
			assertEpsilonEquals(0, p.getZ());
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(2, 2, 0));
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
			assertEpsilonEquals(0, p.getZ());
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(-2, 2, 0));
			assertEpsilonEquals(1, p.getX());
			assertEpsilonEquals(1, p.getY());
			assertEpsilonEquals(1, p.getZ());
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(0.1, 1.2, 0));
			assertEpsilonEquals(1, p.getX());
			assertEpsilonEquals(1, p.getY());
			assertEpsilonEquals(1, p.getZ());
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().getFarthestPointTo(createPoint(10.1, -.2, 0));
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
			assertEpsilonEquals(0, p.getZ());
		}
	}

	@DisplayName("getDistanceL1")
	@Nested
	public class GetDistanceL1 {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(.5, .5, .5)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(1, 1, 1)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.8, getS().getDistanceL1(createPoint(2.3, 4.5, 1)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, getS().getDistanceL1(createPoint(2, 2, 1)));
		}
	}

	@DisplayName("getDistanceLinf")
	@Nested
	public class GetDistanceLinf {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(.5, .5, .5)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(1, 1, 1)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.5, getS().getDistanceLinf(createPoint(2.3, 4.5, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceLinf(createPoint(2, 2, 0)));
		}
	}

	@DisplayName("set(IT)")
	@Nested
	public class SetIT {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set((T) createSegment(123.456, 456.789, 458.1, 789.123, 159.753, 145.36));
			assertEpsilonEquals(123.456, getS().getX1());
			assertEpsilonEquals(456.789, getS().getY1());
			assertEpsilonEquals(458.1, getS().getZ1());
			assertEpsilonEquals(789.123, getS().getX2());
			assertEpsilonEquals(159.753, getS().getY2());
			assertEpsilonEquals(145.36, getS().getZ2());
		}
	}

	@DisplayName("translate")
	@Nested
	public class Translate {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(3.4, 4.5, 1.3);
			assertEpsilonEquals(3.4, getS().getX1());
			assertEpsilonEquals(4.5, getS().getY1());
			assertEpsilonEquals(1.3, getS().getZ1());
			assertEpsilonEquals(4.4, getS().getX2());
			assertEpsilonEquals(5.5, getS().getY2());
			assertEpsilonEquals(2.3, getS().getZ2());
		}
	
		@DisplayName("(Vector3D)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(createVector(3.4, 4.5, 1.3));
			assertEpsilonEquals(3.4, getS().getX1());
			assertEpsilonEquals(4.5, getS().getY1());
			assertEpsilonEquals(1.3, getS().getZ1());
			assertEpsilonEquals(4.4, getS().getX2());
			assertEpsilonEquals(5.5, getS().getY2());
			assertEpsilonEquals(2.3, getS().getZ2());
		}
	}

	@DisplayName("toBoundingBox")
	@Nested
	public class ToBoundingBox {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B bb = getS().toBoundingBox();
			assertEpsilonEquals(0, bb.getMinX());
			assertEpsilonEquals(0, bb.getMinY());
			assertEpsilonEquals(0, bb.getMinZ());
			assertEpsilonEquals(1, bb.getMaxX());
			assertEpsilonEquals(1, bb.getMaxY());
			assertEpsilonEquals(1, bb.getMaxZ());
		}
	
		@DisplayName("(box) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B bb = createAlignedBox(0, 0, 0, 0, 0, 0);
			getS().toBoundingBox(bb);
			assertEpsilonEquals(0, bb.getMinX());
			assertEpsilonEquals(0, bb.getMinY());
			assertEpsilonEquals(0, bb.getMinZ());
			assertEpsilonEquals(1, bb.getMaxX());
			assertEpsilonEquals(1, bb.getMaxY());
			assertEpsilonEquals(1, bb.getMaxZ());
		}
	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(20, 45, 0, 43, 15, 0);
			assertTrue(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(20, 55, 0, 43, 15, 0);
			assertTrue(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(20, 0, 0, 43, 15, 0);
			assertTrue(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 45, 0, 43, 15, 0);
			assertTrue(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(20, 45, 0, 60, 15, 0);
			assertTrue(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(5, 45, 0, 30, 55, 0);
			assertTrue(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(40, 55, 0, 60, 15, 0);
			assertTrue(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(40, 0, 0, 60, 40, 0);
			assertTrue(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}

		@DisplayName("(AlignedBox3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 40, 0, 20, 0, 0);
			assertTrue(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}

		@DisplayName("(AlignedBox3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 45, 0, 100, 15, 0);
			assertTrue(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}

		@DisplayName("(AlignedBox3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(20, 100, 0, 43, 0, 0);
			assertTrue(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}

		@DisplayName("(AlignedBox3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(20, 100, 0, 43, 101, 0);
			assertFalse(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}

		@DisplayName("(AlignedBox3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(100, 45, 0, 102, 15, 0);
			assertFalse(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}

		@DisplayName("(AlignedBox3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(20, 0, 0, 43, -2, 0);
			assertFalse(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}

		@DisplayName("(AlignedBox3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(-100, 45, 0, -48, 15, 0);
			assertFalse(getS().intersects(createAlignedBox(10, 12, 0, 50, 49, 0)));
		}

		@DisplayName("(AlignedBox3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(-100, 60, 0, -98, 61, 0);
			assertFalse(getS().intersects(createAlignedBox(10, 12, 0, 40, 37, 0)));
		}
	
		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(10, 10, 0, 1)));
		}
		
		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(0, 0, 0, 1)));
		}
		
		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(0, .5, 0, 1)));
		}
		
		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(.5, 0, 0, 1)));
		}
		
		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(.5, .5, 0, 1)));
		}
		
		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(2, 0, 0, 1)));
		}
		
		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(12, 8, 0, 2)));
		}
		
		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(12, 8, 0, 2.1)));
		}
		
		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(2, 2, 2, 1)));
		}
		
		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(2, 2, 2, 1.7)));
		}
		
		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(2, 2, 2, 1.8)));
		}
		
		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 3, 0, 0);
			assertTrue(getS().intersects(createSphere(2, 1, 0, 1)));
		}
		
		@DisplayName("(Sphere3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 3, 0, 0);
			assertFalse(getS().intersects(createSphere(2, 1, -GeomConstants.UNIT_VECTOR_EPSILON, 1)));
		}
		
		@DisplayName("(Sphere3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 3, 0, 0);
			assertTrue(getS().intersects(createSphere(2, 1, 0, 1.1)));
		}
	
		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 1, 1, 0);
			assertTrue(getS().intersects(createSegment(0, .5, 0, 1, .5, 0)));
		}
		
		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 1, 1, 0);
			assertTrue(getS().intersects(createSegment(0, 0, 0, 1, 1, 0)));
		}
		
		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 1, 1, 0);
			assertTrue(getS().intersects(createSegment(0, 0, 0, 2, 2, 0)));
		}
		
		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 1, 1, 0);
			assertTrue(getS().intersects(createSegment(0, 0, 0, .5, .5, 0)));
		}
		
		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 1, 1, 0);
			assertTrue(getS().intersects(createSegment(-3, -3, 0, .5, .5, 0)));
		}
		
		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 1, 1, 0);
			assertTrue(getS().intersects(createSegment(-3, -3, 0, 0, 0, 0)));
		}
		
		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 1, 1, 0);
			assertTrue(getS().intersects(createSegment(-3, -3, 0, -1, -1, 0)));
		}
		
		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 1, 1, 0);
			assertTrue(getS().intersects(createSegment(-3, 0, 0, 4, 0, 0)));
		}
		
		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 1, 1, 0);
			assertTrue(getS().intersects(createSegment(-3, -1, 0, 4, -1, 0)));
		}
		
		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 1, 1, 0);
			assertTrue(getS().intersects(createSegment(-3, -1, 0, -1, -1, 0)));
		}
		
		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 1, 1, 0);
			assertTrue(getS().intersects(createSegment(-3, 0, 0, -2, 1, 0)));
		}
		
		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(0, 0, 0, 1, 1, 0);
			assertTrue(getS().intersects(createSegment(10, 0, 0, 9, -1, 0)));
		}
		
		@DisplayName("(Segment3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(7, -5, 0, 1, 1, 0);
			assertTrue(getS().intersects(createSegment(4, -3, 0, 1, 1, 0)));
		}
	
		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(-2, 2, 0);
			p.lineTo(2, 2, 0);
			p.lineTo(2, -2, 0);
			assertFalse(getS().intersects(p));
			p.closePath();
			assertTrue(getS().intersects(p));
		}
		
		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(-2, 2, 0);
			p.lineTo(2, 2, 0);
			p.lineTo(2, -2, 0);
			p.closePath();
			assertTrue(getS().intersects(p));
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(0, 0, 0);
			p.lineTo(-2, 2, 0);
			assertFalse(getS().intersects(p));
		}
		
		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(0, 0, 0);
			p.lineTo(-2, 2, 0);
			p.closePath();
			assertFalse(getS().intersects(p));
		}

		@DisplayName("(Path3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(2, 2, 0);
			p.lineTo(-2, 2, 0);
			assertTrue(getS().intersects(p));
		}
		
		@DisplayName("(Path3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(2, 2, 0);
			p.lineTo(-2, 2, 0);
			p.closePath();
			assertTrue(getS().intersects(p));
		}

		@DisplayName("(Path3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(-2, 2, 0);
			p.lineTo(2, -2, 0);
			assertTrue(getS().intersects(p));
		}
		
		@DisplayName("(Path3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(-2, 2, 0);
			p.lineTo(2, -2, 0);
			p.closePath();
			assertTrue(getS().intersects(p));
		}

		@DisplayName("(Path3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, 2, 0);
			p.lineTo(1, 0, 0);
			p.lineTo(2, 1, 0);
			assertTrue(getS().intersects(p));
		}
		
		@DisplayName("(Path3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, 2, 0);
			p.lineTo(1, 0, 0);
			p.lineTo(2, 1, 0);
			p.closePath();
			assertTrue(getS().intersects(p));
		}

		@DisplayName("(Path3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, 2, 0);
			p.lineTo(2, 1, 0);
			p.lineTo(1, 0, 0);
			assertFalse(getS().intersects(p));
		}
	
		@DisplayName("(Path3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, 2, 0);
			p.lineTo(2, 1, 0);
			p.lineTo(1, 0, 0);
			p.closePath();
			assertTrue(getS().intersects(p));
		}

		@DisplayName("(PathIterator3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_1(CoordinateSystem3D cs) {
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(-2, 2, 0);
			p.lineTo(2, 2, 0);
			p.lineTo(2, -2, 0);
			assertFalse(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_2(CoordinateSystem3D cs) {
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(-2, 2, 0);
			p.lineTo(2, 2, 0);
			p.lineTo(2, -2, 0);
			p.closePath();
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_3(CoordinateSystem3D cs) {
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(0, 0, 0);
			p.lineTo(-2, 2, 0);
			assertFalse(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_4(CoordinateSystem3D cs) {
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(0, 0, 0);
			p.lineTo(-2, 2, 0);
			p.closePath();
			assertFalse(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_5(CoordinateSystem3D cs) {
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(2, 2, 0);
			p.lineTo(-2, 2, 0);
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_6(CoordinateSystem3D cs) {
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(2, 2, 0);
			p.lineTo(-2, 2, 0);
			p.closePath();
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_7(CoordinateSystem3D cs) {
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(-2, 2, 0);
			p.lineTo(2, -2, 0);
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_8(CoordinateSystem3D cs) {
			var p = createPath();
			p.moveTo(-2, -2, 0);
			p.lineTo(-2, 2, 0);
			p.lineTo(2, -2, 0);
			p.closePath();
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_9(CoordinateSystem3D cs) {
			var p = createPath();
			p.moveTo(-2, 2, 0);
			p.lineTo(1, 0, 0);
			p.lineTo(2, 1, 0);
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_10(CoordinateSystem3D cs) {
			var p = createPath();
			p.moveTo(-2, 2, 0);
			p.lineTo(1, 0, 0);
			p.lineTo(2, 1, 0);
			p.closePath();
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_11(CoordinateSystem3D cs) {
			var p = createPath();
			p.moveTo(-2, 2, 0);
			p.lineTo(2, 1, 0);
			p.lineTo(1, 0, 0);
			assertFalse(getS().intersects(p.getPathIterator()));
		}
	
		@DisplayName("(PathIterator3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_12(CoordinateSystem3D cs) {
			var p = createPath();
			p.moveTo(-2, 2, 0);
			p.lineTo(2, 1, 0);
			p.lineTo(1, 0, 0);
			p.closePath();
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects((Shape3D) createSphere(0, 0, 0, 1)));
		}
	}

	@DisplayName("s += Vector3D")
	@Nested
	public class OperatorAddVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_add(createVector(3.4, 4.5, 1.3));
			assertEpsilonEquals(3.4, getS().getX1());
			assertEpsilonEquals(4.5, getS().getY1());
			assertEpsilonEquals(1.3, getS().getZ1());
			assertEpsilonEquals(4.4, getS().getX2());
			assertEpsilonEquals(5.5, getS().getY2());
			assertEpsilonEquals(2.3, getS().getZ2());
		}
	}

	@DisplayName("s + Vector3D")
	@Nested
	public class OperatorPlusVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			T shape = getS().operator_plus(createVector(3.4, 4.5, 1.3));
			assertNotSame(shape, getS());
			assertEpsilonEquals(3.4, shape.getX1());
			assertEpsilonEquals(4.5, shape.getY1());
			assertEpsilonEquals(1.3, shape.getZ1());
			assertEpsilonEquals(4.4, shape.getX2());
			assertEpsilonEquals(5.5, shape.getY2());
			assertEpsilonEquals(2.3, shape.getZ2());
		}
	}

	@DisplayName("s -= Vector3D")
	@Nested
	public class OperatorRemoveVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_remove(createVector(3.4, 4.5, 1.3));
			assertEpsilonEquals(-3.4, getS().getX1());
			assertEpsilonEquals(-4.5, getS().getY1());
			assertEpsilonEquals(-1.3, getS().getZ1());
			assertEpsilonEquals(-2.4, getS().getX2());
			assertEpsilonEquals(-3.5, getS().getY2());
			assertEpsilonEquals(-.3, getS().getZ2());
		}
	}

	@DisplayName("s - Vector3D")
	@Nested
	public class OperatorMinusVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			T shape = getS().operator_minus(createVector(3.4, 4.5, 1.3));
			assertEpsilonEquals(-3.4, shape.getX1());
			assertEpsilonEquals(-4.5, shape.getY1());
			assertEpsilonEquals(-1.3, shape.getZ1());
			assertEpsilonEquals(-2.4, shape.getX2());
			assertEpsilonEquals(-3.5, shape.getY2());
			assertEpsilonEquals(-.3, shape.getZ2());
		}
	}

	@DisplayName("s && Point3D")
	@Nested
	public class OperatorAndPoint3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(0, 0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(.5, .5, .5)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(1, 1, 1)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(2.3, 4.5, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(2, 2, 0)));
		}
	}

	@DisplayName("s && Shape3D")
	@Nested
	public class OperatorAndShape3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createSphere(0, 0, 0, 1)));
		}
	}

	@DisplayName("s .. Point3D")
	@Nested
	public class OperatorUpToPoint3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(0, 0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(.5, .5, .5)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(1, 1, 1)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.86522961, getS().operator_upTo(createPoint(2.3, 4.5, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.7320508087, getS().operator_upTo(createPoint(2, 2, 0)));
		}
	}

	@DisplayName("findsIntersectionLinePlane")
	@Nested
	public class FindsIntersectionLinePlane {

		public InnerComputationPoint3D result;

		@BeforeEach
		public void setUp() {
			result = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.findsIntersectionLinePlane(
					-1, 4, 5, 5, -5, -6,
					0, 1, 0, -2,
					result));
			assertEpsilonEquals(createPoint(.33333333333, 2, 2.55555555555), result);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.findsIntersectionLinePlane(
					-1, 4, 5, 5, -5, -6,
					0, -1, 0, -2,
					result));
			assertEpsilonEquals(createPoint(3, -2, -2.33333333333), result);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.findsIntersectionLinePlane(
					-1, 4, 5, 5, -5, -6,
					1, 0, 0, -5,
					result));
			assertEpsilonEquals(createPoint(5, -5, -6), result);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Segment3afp.findsIntersectionLinePlane(
					-1, 4, 5, 5, -5, -6,
					0, 0, 1, -10,
					result));
			assertEpsilonEquals(createPoint(-3.727272727272, 8.0909090909, 10), result);
		}

		@DisplayName("#5 - intersection at first line point (f=0)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// plane x + y + z - 8 = 0, and S1=(1,2,5) is on plane
			assertTrue(Segment3afp.findsIntersectionLinePlane(
					1, 2, 5, 4, 0, 1,
					1, 1, 1, -8,
					result));
			assertEpsilonEquals(createPoint(1, 2, 5), result);
		}

		@DisplayName("#6 - intersection at second line point (f=1)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// plane z - 7 = 0, and S2 has z=7
			assertTrue(Segment3afp.findsIntersectionLinePlane(
					1, 2, 3, 4, 5, 7,
					0, 0, 1, -7,
					result));
			assertEpsilonEquals(createPoint(4, 5, 7), result);
		}

		@DisplayName("#7 - line parallel to plane, no intersection")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line has constant z=1, plane z=3 -> parallel disjoint
			assertFalse(Segment3afp.findsIntersectionLinePlane(
					0, 0, 1, 1, 2, 1,
					0, 0, 1, -3,
					result));
		}

		@DisplayName("#8 - line included in plane")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// both line points satisfy z=1 plane
			assertTrue(Segment3afp.findsIntersectionLinePlane(
					0, 0, 1, 2, -3, 1,
					0, 0, 1, -1,
					result));
			assertEpsilonEquals(createPoint(0, 0, 1), result);
		}

		@DisplayName("#9 - oblique line / oblique plane")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line: (0,0,0)+f*(2,1,1), plane x+y+z-6=0 => 4f=6 => f=1.5 => (3,1.5,1.5)
			assertTrue(Segment3afp.findsIntersectionLinePlane(
					0, 0, 0, 2, 1, 1,
					1, 1, 1, -6,
					result));
			assertEpsilonEquals(createPoint(3., 1.5, 1.5), result);
		}

		@DisplayName("#10 - same geometry as #9 with reversed line endpoints")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// same line as #9
			assertTrue(Segment3afp.findsIntersectionLinePlane(
					2, 1, 1, 0, 0, 0,
					1, 1, 1, -6,
					result));
			assertEpsilonEquals(createPoint(3., 1.5, 1.5), result);
		}

		@DisplayName("#11 - large coordinates numeric stability")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line along x from 1e9 to 1e9+10, plane x-(1e9+3)=0 => intersection at x=1e9+3
			assertTrue(Segment3afp.findsIntersectionLinePlane(
					1_000_000_000d, 2, -4, 1_000_000_010d, 2, -4,
					1, 0, 0, -1_000_000_003d,
					result));
			assertEpsilonEquals(createPoint(1_000_000_003d, 2, -4), result);
		}

	}

	@DisplayName("calculatesIntersectionFactorLinePlane")
	@Nested
	public class CalculatesIntersectionFactorLinePlane {
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10., Segment3afp.calculatesIntersectionFactorLinePlane(
					0, 0, 0, 1, 0, 0,
					1, 0, 0, -10));
		}

		@DisplayName("#2 - reversed line direction")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// S1=(1,0,0), S2=(0,0,0), plane x=10 => 1 + f*(-1) = 10 => f=-9
			assertEpsilonEquals(-9., Segment3afp.calculatesIntersectionFactorLinePlane(
					1, 0, 0, 0, 0, 0,
					1, 0, 0, -10));
		}

		@DisplayName("#3 - intersection between endpoints (0<f<1)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line: z from -1 to +1, plane z=0 => f=0.5
			assertEpsilonEquals(.5, Segment3afp.calculatesIntersectionFactorLinePlane(
					0, 0, -1, 0, 0, 1,
					0, 0, 1, 0));
		}

		@DisplayName("#4 - intersection at S1 (f=0)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// S1 on plane y=2
			assertEpsilonEquals(0., Segment3afp.calculatesIntersectionFactorLinePlane(
					1, 2, 3, 4, 5, 6,
					0, 1, 0, -2));
		}

		@DisplayName("#5 - intersection at S2 (f=1)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// S2 on plane z=7
			assertEpsilonEquals(1., Segment3afp.calculatesIntersectionFactorLinePlane(
					1, 2, 3, 4, 5, 7,
					0, 0, 1, -7));
		}

		@DisplayName("#6 - oblique plane, oblique line")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// plane: x+y+z-6=0
			// line: S1=(0,0,0), S2=(2,1,1) => I(f)=(2f,f,f), sum=4f => 4f-6=0 => f=1.5
			assertEpsilonEquals(1.5, Segment3afp.calculatesIntersectionFactorLinePlane(
					0, 0, 0, 2, 1, 1,
					1, 1, 1, -6));
		}

		@DisplayName("#7 - intersection behind S1 (f<0)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line: x grows from 5 to 6, plane x=3 => 5+f*(1)=3 => f=-2
			assertEpsilonEquals(-2., Segment3afp.calculatesIntersectionFactorLinePlane(
					5, 0, 0, 6, 0, 0,
					1, 0, 0, -3));
		}

		@DisplayName("#8 - parallel line, no intersection")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line direction orthogonal to plane normal (parallel to plane), plane z=3, line z=1
			assertNaN(Segment3afp.calculatesIntersectionFactorLinePlane(
					0, 0, 1, 1, 0, 1,
					0, 0, 1, -3));
		}

		@DisplayName("#9 - line included in plane")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// line lies entirely in plane z=1
			assertNaN(Segment3afp.calculatesIntersectionFactorLinePlane(
					0, 0, 1, 2, -3, 1,
					0, 0, 1, -1));
		}

		@DisplayName("#10 - scaled plane coefficients invariant")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Same plane as x=10: (1,0,0,-10) scaled by 5 -> (5,0,0,-50), same f
			assertEpsilonEquals(10., Segment3afp.calculatesIntersectionFactorLinePlane(
					0, 0, 0, 1, 0, 0,
					5, 0, 0, -50));
		}

	}

	@DisplayName("clipToBox")
	@Nested
	public class ClipToBox {

		@DisplayName("(double,double,double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(-1, 4, 5, 5, -5, -6);
			assertTrue(getS().clipToBox(0, 0, 0, 2, 2, 2));
			assertEpsilonEquals(createPoint(.636363636363, 1.545454545454, 2), getS().getP1());
			assertEpsilonEquals(createPoint(1.666666666666, 0, 0.11111111111), getS().getP2());
		}
	}

	@DisplayName("getDirection")
	@Nested
	public class GetDirection {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var v = getS().getDirection();
			assertEpsilonEquals(new Vector3d(.577350269, .577350269, .577350269), v);
		}
	}

	@DisplayName("getSegmentVector")
	@Nested
	public class GetSegmentVector {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var v = getS().getSegmentVector();
			assertEpsilonEquals(new Vector3d(1, 1, 1), v);
		}
	}

	@DisplayName("rotate")
	@Nested
	public class Rotate {

		@DisplayName("(double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = newAxisAngleZ(MathConstants.PI);
			var s = (Segment3afp) getS().clone();
			s.rotate(q.getX(), q.getY(), q.getZ(), q.getW());
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(-1, s.getX2());
			assertEpsilonEquals(-1, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}

		@DisplayName("(double,double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledoubledouble_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = newAxisAngleZ(MathConstants.QUARTER_PI);
			var s = (Segment3afp) getS().clone();
			s.rotate(q.getX(), q.getY(), q.getZ(), q.getW());
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(0, s.getX2());
			assertEpsilonEquals(1.41421356, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}

		@DisplayName("(Quaternion) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternion_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = newAxisAngleZ(MathConstants.PI);
			var s = (Segment3afp) getS().clone();
			s.rotate(q);
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(-1, s.getX2());
			assertEpsilonEquals(-1, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}

		@DisplayName("(Quaternion) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternion_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = newAxisAngleZ(MathConstants.QUARTER_PI);
			var s = (Segment3afp) getS().clone();
			s.rotate(q);
			assertEpsilonEquals(0, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(0, s.getX2());
			assertEpsilonEquals(1.41421356, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}
	
		@DisplayName("(double,double,double,double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledoubledoubledoubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = newAxisAngleZ(MathConstants.PI);
			var s = (Segment3afp) getS().clone();
			s.rotate(q.getX(), q.getY(), q.getZ(), q.getW(), 1, 0, 1);
			assertEpsilonEquals(2, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(1, s.getX2());
			assertEpsilonEquals(-1, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}
		
		@DisplayName("(double,double,double,double,double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledoubledoubledoubledoubledouble_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = newAxisAngleZ(MathConstants.QUARTER_PI);
			var s = (Segment3afp) getS().clone();
			s.rotate(q.getX(), q.getY(), q.getZ(), q.getW(), 1, 0, 1);
			assertEpsilonEquals(.2929, s.getX1());
			assertEpsilonEquals(-.7071, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(.2929, s.getX2());
			assertEpsilonEquals(.7071, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}
	
		@DisplayName("(Quaternion,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternionpoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = newAxisAngleZ(MathConstants.PI);
			var s = (Segment3afp) getS().clone();
			s.rotate(q, createPoint(1, 0, 1));
			assertEpsilonEquals(2, s.getX1());
			assertEpsilonEquals(0, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(1, s.getX2());
			assertEpsilonEquals(-1, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}
		
		@DisplayName("(Quaternion,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternionpoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = newAxisAngleZ(MathConstants.QUARTER_PI);
			var s = (Segment3afp) getS().clone();
			s.rotate(q, createPoint(1, 0, 1));
			assertEpsilonEquals(.2929, s.getX1());
			assertEpsilonEquals(-.7071, s.getY1());
			assertEpsilonEquals(0, s.getZ1());
			assertEpsilonEquals(.2929, s.getX2());
			assertEpsilonEquals(.7071, s.getY2());
			assertEpsilonEquals(1, s.getZ2());
		}
	}

}