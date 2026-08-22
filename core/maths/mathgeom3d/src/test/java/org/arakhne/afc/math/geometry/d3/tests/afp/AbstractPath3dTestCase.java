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

import static org.arakhne.afc.math.geometry.base.GeomConstants.DISTANCE_EPSILON;
import static org.arakhne.afc.math.geometry.base.GeomConstants.SPLINE_APPROXIMATION_RATIO;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Shape3D;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathElement3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;
import org.arakhne.afc.math.geometry.d3.d.Shape3d;
import org.arakhne.afc.math.geometry.d3.general.Shape3DType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("all")
public abstract class AbstractPath3dTestCase<T extends Path3afp<T, ?, ?, ?, ?, B>, B extends AlignedBox3afp<?, ?, ?, ?, ?, B>>
extends AbstractShape3dTestCase<T, B> {

	@Override
	protected T createShape() {
		T path = (T) createPath();
		path.moveTo(0, 0, 0);
		path.lineTo(1, .5, -5);
		//b=Curve((1-t)^(2) QA + 2 (1-t) t QB + t^(2) QC, t,0,1)
		path.quadTo(3, 0, 2, 4, 3, -2);
		//a=Curve(
		//	x(CA) (1-t)^(3)+3 x(CB) (1-t)^(2) t+3 x(CC) (1-t) t^(2)+x(CD) t^(3),
		//	y(CA) (1-t)^(3)+3 y(CB) (1-t)^(2) t+3 y(CC) (1-t) t^(2)+y(CD) t^(3),
		//	z(CA) (1-t)^(3)+3 z(CB) (1-t)^(2) t+3 z(CC) (1-t) t^(2)+z(CD) t^(3),
		//	t,0,1)
		path.curveTo(5, -1, 3, 6, 5, 5, 7, -5, 2);
		return path;
	}

	private static Stream<Arguments> proposeArguments7Points() {
		final List<Arguments> args = new ArrayList<>();
		for (final CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			for (int i = 0; i < 100; ++i) {
				Point3D p1 = randomPoint3d();
				Point3D p2 = randomPoint3d();
				Point3D p3 = randomPoint3d();
				Point3D p4 = randomPoint3d();
				Point3D p5 = randomPoint3d();
				Point3D p6 = randomPoint3d();
				Point3D p7 = randomPoint3d();
				args.add(Arguments.of(cs, p1, p2, p3, p4, p5, p6, p7));
			}
		}
		return args.stream();
	}

	private static Stream<Arguments> proposeArguments3Coords() {
		final List<Arguments> args = new ArrayList<>();
		for (final CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			for (int i = 0; i < 100; ++i) {
				double dx = getRandom().nextDouble() * 20.;
				double dy = getRandom().nextDouble() * 20.;
				double dz = getRandom().nextDouble() * 20.;
				args.add(Arguments.of(cs, dx, dy, dz));
			}
		}
		return args.stream();
	}

	@DisplayName("getType")
	@Nested
	public class GetType {
		@DisplayName("(Class)")
		@Test
		public final void getType_Class() {
			assertSame(Shape3DType.PATH, getS().getType(Shape3DType.class));
		}

		@DisplayName("()")
		@Test
		public final void getType() {
			assertSame(Shape3DType.PATH, getS().getType());
		}
	}

	@DisplayName("calculatesControlPointBoundingBox")
	@Nested
	public class CalculatesControlPointBoundingBox {

		private B box;

		@BeforeEach
		public void setUp() {
			this.box = createAlignedBox(0, 0, 0, 0, 0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.calculatesControlPointBoundingBox(getS().getPathIterator(), box);
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(-5, box.getMinY());
			assertEpsilonEquals(-5, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(5, box.getMaxY());
			assertEpsilonEquals(5, box.getMaxZ());
		}

		@DisplayName("#2 - empty path => no control point")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();
			assertFalse(Path3afp.calculatesControlPointBoundingBox(path.getPathIterator(), box));
		}

		@DisplayName("#3 - single MOVE_TO")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();
			path.moveTo(2, -3, 4);
			assertFalse(Path3afp.calculatesControlPointBoundingBox(path.getPathIterator(), box));
		}

		@DisplayName("#4 - move + line")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();
			path.moveTo(1, 2, 3);
			path.lineTo(4, -1, 6);

			assertTrue(Path3afp.calculatesControlPointBoundingBox(path.getPathIterator(), box));
			assertEpsilonEquals(1, box.getMinX());
			assertEpsilonEquals(-1, box.getMinY());
			assertEpsilonEquals(3, box.getMinZ());
			assertEpsilonEquals(4, box.getMaxX());
			assertEpsilonEquals(2, box.getMaxY());
			assertEpsilonEquals(6, box.getMaxZ());
		}

		@DisplayName("#5 - quad curve with control point outside endpoints")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();
			path.moveTo(0, 0, 0);
			path.quadTo(10, -5, 8, 2, 1, 3);

			assertTrue(Path3afp.calculatesControlPointBoundingBox(path.getPathIterator(), box));
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(-5, box.getMinY());
			assertEpsilonEquals(0, box.getMinZ());
			assertEpsilonEquals(10, box.getMaxX());
			assertEpsilonEquals(1, box.getMaxY());
			assertEpsilonEquals(8, box.getMaxZ());
		}

		@DisplayName("#6 - cubic curve with two controls")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();
			path.moveTo(1, 1, 1);
			path.curveTo(3, 10, -2, -4, -6, 7, 2, 0, 5);

			assertTrue(Path3afp.calculatesControlPointBoundingBox(path.getPathIterator(), box));
			assertEpsilonEquals(-4, box.getMinX());
			assertEpsilonEquals(-6, box.getMinY());
			assertEpsilonEquals(-2, box.getMinZ());
			assertEpsilonEquals(3, box.getMaxX());
			assertEpsilonEquals(10, box.getMaxY());
			assertEpsilonEquals(7, box.getMaxZ());
		}

		@DisplayName("#7 - closePath does not add new control point")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();
			path.moveTo(0, 0, 0);
			path.lineTo(2, 3, 4);
			path.closePath();

			assertTrue(Path3afp.calculatesControlPointBoundingBox(path.getPathIterator(), box));
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(0, box.getMinY());
			assertEpsilonEquals(0, box.getMinZ());
			assertEpsilonEquals(2, box.getMaxX());
			assertEpsilonEquals(3, box.getMaxY());
			assertEpsilonEquals(4, box.getMaxZ());
		}

		@DisplayName("#8 - multiple subpaths")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();

			path.moveTo(1, 1, 1);
			path.lineTo(2, 2, 2);

			path.moveTo(-10, 5, 0);
			path.quadTo(-8, -7, 9, -6, 4, -3);

			assertTrue(Path3afp.calculatesControlPointBoundingBox(path.getPathIterator(), box));
			assertEpsilonEquals(-10, box.getMinX());
			assertEpsilonEquals(-7, box.getMinY());
			assertEpsilonEquals(-3, box.getMinZ());
			assertEpsilonEquals(2, box.getMaxX());
			assertEpsilonEquals(5, box.getMaxY());
			assertEpsilonEquals(9, box.getMaxZ());
		}

		@DisplayName("#9 - only move commands")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();
			path.moveTo(5, 0, 1);
			path.moveTo(-2, 7, -4);
			path.moveTo(3, -6, 2);
			assertFalse(Path3afp.calculatesControlPointBoundingBox(path.getPathIterator(), box));
		}

	}

	@DisplayName("calculatesDrawableElementBoundingBox")
	@Nested
	public class CalculatesDrawableElementBoundingBox {

		private B box;

		@BeforeEach
		public void setUp() {
			this.box = createAlignedBox(0, 0, 0, 0, 0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.calculatesDrawableElementBoundingBox(getS().getPathIterator(), box);
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(-5, box.getMinY());
			assertEpsilonEquals(-5, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(3, box.getMaxY());
			assertEpsilonEquals(3.421875, box.getMaxZ());
		}

		@DisplayName("#2 - empty path => no drawable element")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();
			assertFalse(Path3afp.calculatesDrawableElementBoundingBox(path.getPathIterator(), box));
		}

		@DisplayName("#3 - only moveTo commands => no drawable element")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();
			path.moveTo(1, 2, 3);
			path.moveTo(-4, 5, -6);
			assertFalse(Path3afp.calculatesDrawableElementBoundingBox(path.getPathIterator(), box));
		}

		@DisplayName("#4 - single line segment")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();
			path.moveTo(1, 2, 3);
			path.lineTo(4, -1, 6);

			assertTrue(Path3afp.calculatesDrawableElementBoundingBox(path.getPathIterator(), box));
			assertEpsilonEquals(1, box.getMinX());
			assertEpsilonEquals(-1, box.getMinY());
			assertEpsilonEquals(3, box.getMinZ());
			assertEpsilonEquals(4, box.getMaxX());
			assertEpsilonEquals(2, box.getMaxY());
			assertEpsilonEquals(6, box.getMaxZ());
		}

		@DisplayName("#5 - closePath creates drawable segment")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();
			path.moveTo(0, 0, 0);
			path.lineTo(2, 1, 3);
			path.closePath();

			assertTrue(Path3afp.calculatesDrawableElementBoundingBox(path.getPathIterator(), box));
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(0, box.getMinY());
			assertEpsilonEquals(0, box.getMinZ());
			assertEpsilonEquals(2, box.getMaxX());
			assertEpsilonEquals(1, box.getMaxY());
			assertEpsilonEquals(3, box.getMaxZ());
		}

		@DisplayName("#6 - quad curve stays inside endpoint-control bbox")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();
			path.moveTo(0, 0, 0);
			path.quadTo(10, -5, 8, 2, 1, 3);

			assertTrue(Path3afp.calculatesDrawableElementBoundingBox(path.getPathIterator(), box));

			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(-2.25, box.getMinY());
			assertEpsilonEquals(0, box.getMinZ());
			assertEpsilonEquals(5.5, box.getMaxX());
			assertEpsilonEquals(1., box.getMaxY());
			assertEpsilonEquals(4.921875, box.getMaxZ());
		}

		@DisplayName("#7 - cubic curve with strong control points")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();
			path.moveTo(1, 1, 1);
			path.curveTo(3, 10, -2, -4, -6, 7, 2, 0, 5);

			assertTrue(Path3afp.calculatesDrawableElementBoundingBox(path.getPathIterator(), box));

			assertEpsilonEquals(-0.515625, box.getMinX());
			assertEpsilonEquals(-1.3876953125, box.getMinY());
			assertEpsilonEquals(0.38330078125, box.getMinZ());
			assertEpsilonEquals(2, box.getMaxX());
			assertEpsilonEquals(3.83203125, box.getMaxY());
			assertEpsilonEquals(5.29638671875, box.getMaxZ());
		}

		@DisplayName("#8 - multiple subpaths with one drawable only")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final Path3afp<?, ?, ?, ?, ?, ?> path = createPath();
			path.moveTo(100, 100, 100); // non-drawable alone
			path.moveTo(-2, -3, -4);
			path.lineTo(5, 6, 7);

			assertTrue(Path3afp.calculatesDrawableElementBoundingBox(path.getPathIterator(), box));
			assertEpsilonEquals(-2, box.getMinX());
			assertEpsilonEquals(-3, box.getMinY());
			assertEpsilonEquals(-4, box.getMinZ());
			assertEpsilonEquals(5, box.getMaxX());
			assertEpsilonEquals(6, box.getMaxY());
			assertEpsilonEquals(7, box.getMaxZ());
		}

	}

	@DisplayName("getClosestPointTo")
	@Nested
	public class GetClosestPointTo {

		private Point3D result;

		@BeforeEach
		public void setUp() {
			this.result = null;
		}

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getClosestPointTo(createTriangle(-2, 2, 2, -1, -5, 3, -4, 5, 6)));
		}

		@DisplayName("(Triangle3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_2(CoordinateSystem3D cs) {
			// triangle containing path start point
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Path starts at (0,0,0), triangle contains that point => distance = 0
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getClosestPointTo(createTriangle(0, 0, 0, 2, 0, 0, 0, 2, 0)));
		}

		@DisplayName("(Triangle3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_3(CoordinateSystem3D cs) {
			// triangle containing path end point
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Path ends at (7,-5,2), triangle contains that point => distance = 0
			assertEpsilonEquals(createPoint(7, -5, 2), getS().getClosestPointTo(createTriangle(7, -5, 2, 9, -5, 2, 7, -3, 2)));
		}

		@DisplayName("(Triangle3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_4(CoordinateSystem3D cs) {
			// far triangle near start side, closest to path start
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle in plane x=-3, nearest to path should be close to/start at (0,0,0)
			// nearest triangle point to (0,0,0): (-3,0,0), distance = 3
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getClosestPointTo(createTriangle(-3, 0, 0, -3, 1, 0, -3, 0, 1)));
		}

		@DisplayName("(Triangle3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_5(CoordinateSystem3D cs) {
			// far triangle near end side, closest to path end
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle in plane x=10, nearest to path should be end (7,-5,2)
			// nearest triangle point: (10,-5,2), distance = 3
			assertEpsilonEquals(createPoint(7, -5, 2), getS().getClosestPointTo(createTriangle(10, -5, 2, 10, -4, 2, 10, -5, 3)));
		}

		@DisplayName("(Box3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(-2, 1, 0, .1, .1, .1));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Box3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(-2, 1, 5, .1, .1, .1));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Box3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(-2, 1, -5, .1, .1, .1));
			assertEpsilonEquals(createPoint(0.88,0.44,-4.4), result);
		}

		@DisplayName("(Box3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(1, 0, 0, .1, .1, .1));
			assertEpsilonEquals(createPoint(0.04,0.02,-0.2), result);
		}

		@DisplayName("(Box3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(1, 0, 5, .1, .1, .1));
			assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);
		}

		@DisplayName("(Box3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(1, 0, -5, .1, .1, .1));
			assertEpsilonEquals(createPoint(0.9733333333,0.48666666666,-4.866666666), result);
		}

		@DisplayName("(Box3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(1, 1, 0, .1, .1, .1));
			assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result);
		}

		@DisplayName("(Box3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(1, 1, 5, .1, .1, .1));
			assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);
		}

		@DisplayName("(Box3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(1, 1, -5, .1, .1, .1));
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("(Box3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(3, 0, 0, .1, .1, .1));
			assertEpsilonEquals(createPoint(2.75, 0.875, -0.75), result);
		}

		@DisplayName("(Box3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(3, 0, 5, .1, .1, .1));
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
		}

		@DisplayName("(Box3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(3, 0, -5, .1, .1, .1));
			assertEpsilonEquals(createPoint(1.2048693242,0.47026090456,-4.3325225245), result);
		}

		@DisplayName("(Box3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(1, -4, 0, .1, .1, .1));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Box3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(1, -4, 5, .1, .1, .1));
			assertEpsilonEquals(createPoint(6.68780989707,-2.4513686609,2.74548041205), result);
		}

		@DisplayName("(Box3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(1, -4, -5, .1, .1, .1));
			assertEpsilonEquals(createPoint(0.89714285714,0.448571428571,-4.4857142857), result);
		}

		@DisplayName("(Box3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createAlignedBox(-2, 1, 0, 3, .1, .1));
			assertEpsilonEquals(createPoint(0.019801980198,0.009900990099,-0.09900990099), result);
		}

		@DisplayName("(Box3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_17(CoordinateSystem3D cs) {
			result = getS().getClosestPointTo(createAlignedBox(-2, 1, 0, 3, .1, .1));
			assertEpsilonEquals(createPoint(0.019801980198,0.009900990099,-0.09900990099), result);
		}

		@DisplayName("(Box3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_18(CoordinateSystem3D cs) {
			result = getS().getClosestPointTo(createAlignedBox(-2, 1, 0, 5, .1, .1));
			assertEpsilonEquals(createPoint(3.109375,1.2421875,-0.546875), result);
		}

		@DisplayName("(Box3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_19(CoordinateSystem3D cs) {
			result = getS().getClosestPointTo(createAlignedBox(-2, 1, 0, 7, .1, .1));
			assertEpsilonEquals(createPoint(3.109375,1.2421875,-0.546875), result);
		}

		@DisplayName("(Box3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_20(CoordinateSystem3D cs) {
			result = getS().getClosestPointTo(createAlignedBox(-2, 1, 0, 9, .1, .1));
			assertEpsilonEquals(createPoint(3.109375,1.2421875,-0.546875), result);
		}

		@DisplayName("(Box3afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_21(CoordinateSystem3D cs) {
			result = getS().getClosestPointTo(createAlignedBox(-2, 1, 0, 15, .1, .1));
			assertEpsilonEquals(createPoint(3.109375,1.2421875,-0.546875), result);
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-2, 1, 0));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-2, 1, 5));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-2, 1, -5));
			assertEpsilonEquals(createPoint(0.8952380952380953, 0.44761904761904764, -4.476190476190476), result);
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(1, 0, 0));
			assertEpsilonEquals(createPoint(0.0380952380952381, 0.01904761904761905, -0.190476190476190), result);
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(1, 0, 5));
			assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(1, 0, -5));
			assertEpsilonEquals(createPoint(0.9904761904761905, 0.49523809523809526, -4.9523809523809526), result);
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(1, 1, 0));
			assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result);
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(1, 1, 5));
			assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(1, 1, -5));
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(3, 0, 0));
			assertEpsilonEquals(createPoint(2.75, 0.875, -0.75), result);
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(3, 0, 5));
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(3, 0, -5));
			assertEpsilonEquals(createPoint(1.178117105233741,0.4741442911757473,-4.419682979722328), result);
		}

		@DisplayName("(Point3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(1, -4, 0));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Point3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(1, -4, 5));
			assertEpsilonEquals(createPoint(6.699003011013108, -2.529020888903435, 2.7233273740365576), result);
		}

		@DisplayName("(Point3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(1, -4, -5));
			assertEpsilonEquals(createPoint(0.9142857142857143, 0.45714285714285713, -4.571428571428571), result);
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo((Shape3d) createSphere(-2, 1, 0, .1));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Shape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo((Shape3d) createSegment(-2, 1, 0, 10, 10, 10));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Shape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			result = getS().getClosestPointTo(p);
			assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);
		}

		@DisplayName("(Capsule3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(-2, 1, 0, .1));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(-2, 1, 5, .1));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(-2, 1, -5, .1));
			assertEpsilonEquals(createPoint(0.8952380952380953, 0.44761904761904764, -4.476190476190476), result);
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(1, 0, 0, .1));
			assertEpsilonEquals(createPoint(0.0380952380952381, 0.01904761904761905, -0.190476190476190), result);
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(1, 0, 5, .1));
			assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(1, 0, -5, .1));
			assertEpsilonEquals(createPoint(0.9904761904761905, 0.49523809523809526, -4.9523809523809526), result);
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(1, 1, 0, .1));
			assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result);
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(1, 1, 5, .1));
			assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(1, 1, -5, .1));
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(3, 0, 0, .1));
			assertEpsilonEquals(createPoint(2.75, 0.875, -0.75), result);
		}

		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(3, 0, 5, .1));
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
		}

		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(3, 0, -5, .1));
			assertEpsilonEquals(createPoint(1.178117105233741,0.4741442911757473,-4.419682979722328), result);
		}

		@DisplayName("(Sphere3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(1, -4, 0, .1));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Sphere3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(1, -4, 5, .1));
			assertEpsilonEquals(createPoint(6.699003011013108, -2.529020888903435, 2.7233273740365576), result);
		}

		@DisplayName("(Sphere3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(1, -4, -5, .1));
			assertEpsilonEquals(createPoint(0.9142857142857143, 0.45714285714285713, -4.571428571428571), result);
		}

		@DisplayName("(Sphere3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Inside Sphere - usually the closest point to the sphere
			result = getS().getClosestPointTo(createSphere(-2, 1, 0, 3));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Sphere3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(-2, 1, 0, 3));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Sphere3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(-2, 1, 0, 5));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Sphere3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(-2, 1, 0, 7));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Sphere3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(-2, 1, 0, 9));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Sphere3afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSphere(-2, 1, 0, 15));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(-2, 1, 0, 10, 10, 10));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(-2, 1, 5, 10, 10, 10));
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(-2, 1, -5, 10, 10, 10));
			assertEpsilonEquals(createPoint(0.6328774470367391, 0.31643872351836955, -3.1643872351836952), result);
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(1, 0, 0, 10, 10, 10));
			assertEpsilonEquals(createPoint(0.0380952380952381, 0.01904761904761905, -0.190476190476190), result);
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(1, 0, 5, 10, 10, 10));
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(1, 0, -5, 10, 10, 10));
			assertEpsilonEquals(createPoint(1.202170841513377, 0.47065261978031625, -4.34131435506932), result);
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(1, 1, 0, 10, 10, 10));
			assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result);
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(1, 1, 5, 10, 10, 10));
			assertEpsilonEquals(createPoint(5.53286881, 1.2138319933, 3.022991748), result);
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(1, 1, -5, 10, 10, 10));
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(3, 0, 0, 10, 10, 10));
			assertEpsilonEquals(createPoint(4.840444885643851, 1.4436409922956546, 1.4300824432249362), result);
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(3, 0, 5, 10, 10, 10));
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(3, 0, -5, 10, 10, 10));
			assertEpsilonEquals(createPoint(4.154833414196007, 2.4774372270884744, -1.2548641941817131), result);
		}

		@DisplayName("(Segment3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(1, -4, 0, 10, 10, 10));
			assertEpsilonEquals(createPoint(5.489624176405976, 1.240859889746265, 2.965332235207968), result);
		}

		@DisplayName("(Segment3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(1, -4, 5, 10, 10, 10));
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
		}

		@DisplayName("(Segment3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createSegment(1, -4, -5, 10, 10, 10));
			assertEpsilonEquals(createPoint(4.6789346310202795, 1.5210313226361163, 0.9085389126696517), result);
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp ms;
			Path3afp p;
			ms = createMultiShape();
			p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			ms.add(p);
			ms.add(createSegment(-2, 1, 0, 10, 10, 10));
			result = getS().getClosestPointTo(ms);
			assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);
		}

		@DisplayName("(MultiShape3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp ms;
			Path3afp p;
			ms = createMultiShape();
			p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			p.lineTo(4, 6, 12);
			ms.add(p);
			ms.add(createSegment(-2, 1, 0, 10, 10, 10));
			result = getS().getClosestPointTo(ms);
			assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);
		}

		@DisplayName("(MultiShape3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp ms;
			Path3afp p;
			ms = createMultiShape();
			p = createPath();
			p.moveTo(-2, -10, -4);
			p.quadTo(10, 10, 10, 4, 6, 12);
			ms.add(p);
			ms.add(createSegment(-2, 1, 0, 10, 10, 10));
			result = getS().getClosestPointTo(ms);
			assertEpsilonEquals(createPoint(5.66793695624968, 1.1294144023439496, 3.2030826083329074), result);
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			result = getS().getClosestPointTo(p);
			assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			p.lineTo(4, 6, 12);
			result = getS().getClosestPointTo(p);
			assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			p.lineTo(4, 6, 12);
			p.closePath();
			result = getS().getClosestPointTo(p);
			assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);
		}

		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			p.lineTo(4, 6, 12);
			p.closePath();
			p = createPath();
			p.moveTo(-2, -10, -4);
			p.quadTo(10, 10, 10, 4, 6, 12);
			result = getS().getClosestPointTo(p);
			assertEpsilonEquals(createPoint(5.66793695624968, 1.1294144023439496, 3.2030826083329074), result);
		}
	}

	@DisplayName("findsClosestPointToPoint(PathIterator3afp,double,double,double,Point3D) close path")
	@Nested
	public class FindsClosestPointToPoint {

		private Point3D result;

		@BeforeEach
		public void setUp() {
			this.result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		}

		@DisplayName("Open path #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("Open path #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 5, result);
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("Open path #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, -5, result);
			assertEpsilonEquals(createPoint(0.8952380952380953, 0.44761904761904764, -4.476190476190476), result);
		}

		@DisplayName("Open path #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
			assertEpsilonEquals(createPoint(0.0380952380952381, 0.01904761904761905, -0.190476190476190), result);
		}

		@DisplayName("Open path #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 5, result);
			assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);
		}

		@DisplayName("Open path #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, -5, result);
			assertEpsilonEquals(createPoint(0.9904761904761905, 0.49523809523809526, -4.9523809523809526), result);
		}

		@DisplayName("Open path #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 0, result);
			assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result);
		}

		@DisplayName("Open path #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 5, result);
			assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);
		}

		@DisplayName("Open path #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, -5, result);
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("Open path #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
			assertEpsilonEquals(createPoint(2.75, 0.875, -0.75), result);
		}

		@DisplayName("Open path #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 5, result);
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
		}

		@DisplayName("Open path #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, -5, result);
			assertEpsilonEquals(createPoint(1.178117105233741,0.4741442911757473,-4.419682979722328), result);
		}

		@DisplayName("Open path #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("Open path #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 5, result);
			assertEpsilonEquals(createPoint(6.699003011013108, -2.529020888903435, 2.7233273740365576), result);
		}

		@DisplayName("Open path #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, -5, result);
			assertEpsilonEquals(createPoint(0.9142857142857143, 0.45714285714285713, -4.571428571428571), result);
		}

		@DisplayName("Close path #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("Close path #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 5, result);
			assertEpsilonEquals(createPoint(0, 0, 0), result);
		}

		@DisplayName("Close path #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, -5, result);
			assertEpsilonEquals(createPoint(0.8952380952380953, 0.44761904761904764, -4.476190476190476), result);
		}

		@DisplayName("Close path #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
			assertEpsilonEquals(createPoint(0.6282051282051286, -0.4487179487179489, 0.17948717948717952), result);
		}

		@DisplayName("Close path #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 5, result);
			assertEpsilonEquals(createPoint(1.5256410256410255, -1.0897435897435899, 0.4358974358974359), result);
		}

		@DisplayName("Close path #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, -5, result);
			assertEpsilonEquals(createPoint(0.9904761904761905, 0.49523809523809526, -4.9523809523809526), result);
		}

		@DisplayName("Close path #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 0, result);
			assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result);
		}

		@DisplayName("Close path #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 5, result);
			assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);
		}

		@DisplayName("Close path #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, -5, result);
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("Close path #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
			assertEpsilonEquals(createPoint(2.75, 0.875, -0.75), result);
		}

		@DisplayName("Close path #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 5, result);
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
		}

		@DisplayName("Close path #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, -5, result);
			assertEpsilonEquals(createPoint(1.178117105233741,0.4741442911757473,-4.419682979722328), result);
		}

		@DisplayName("Close path #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
			assertEpsilonEquals(createPoint(2.4230769230769234, -1.7307692307692308, 0.6923076923076923), result);
		}

		@DisplayName("Close path #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 5, result);
			assertEpsilonEquals(createPoint(3.3205128205128203, -2.371794871794872, 0.9487179487179487), result);
		}

		@DisplayName("Close path #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			result = createPoint(Double.NaN, Double.NaN, Double.NaN);
			Path3afp.findsClosestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, -5, result);
			assertEpsilonEquals(createPoint(0.9142857142857143, 0.45714285714285713, -4.571428571428571), result);
		}

	}

	@DisplayName("findsClosestPointToSegment(PathIterator3afp,double,double,double,double,double,double,Point3D)")
	@Nested
	public class FindsClosestPointToSegment {

		private Point3D result1;

		private Point3D result2;

		@BeforeEach
		public void setUp() {
			this.result1 = createPoint(Double.NaN, Double.NaN, Double.NaN);;
			this.result2 = createPoint(Double.NaN, Double.NaN, Double.NaN);;
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, 1, 0, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(0, 0, 0), result1);
			assertEpsilonEquals(createPoint(-1.4461538462, 1.4153846154, 0.4615384615), result2);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, 1, 5, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(0, 0, 0), result1);
			assertEpsilonEquals(createPoint(-2, 1, 5), result2);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, 1, -5, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(0.6328774470367391, 0.31643872351836955, -3.1643872351836952), result1);
			assertEpsilonEquals(createPoint(-0.5872888174, 2.059533387, -3.23411102172), result2);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, 0, 0, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(0.0380952380952381, 0.01904761904761905, -0.190476190476190), result1);
			assertEpsilonEquals(createPoint(1, 0, 0), result2);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, 0, 5, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result1);
			assertEpsilonEquals(createPoint(2.9701152913, 2.1890169903, 6.09450849515), result2);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, 0, -5, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(1.202170841513377, 0.47065261978031625, -4.34131435506932), result1);
			assertEpsilonEquals(createPoint(1.36368752711, 0.404097252349, -4.393854121), result2);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, 1, 0, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result1);
			assertEpsilonEquals(createPoint(1, 1, 0), result2);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, 1, 5, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(5.53286881, 1.2138319933, 3.022991748), result1);
			assertEpsilonEquals(createPoint(2.58030691858, 2.58030691858, 5.8779482881), result2);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, 1, -5, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(1, .5, -5), result1);
			assertEpsilonEquals(createPoint(1, 1, -5), result2);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					3, 0, 0, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(4.840444885643851, 1.4436409922956546, 1.4300824432249362), result1);
			assertEpsilonEquals(createPoint(4.170049959369438, 1.67149994195634, 1.67149994195634), result2);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					3, 0, 5, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result1);
			assertEpsilonEquals(createPoint(3.86871408045977, 1.2410201149425286, 5.6205100574712645), result2);
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					3, 0, -5, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(4.154833414196007, 2.4774372270884744, -1.2548641941817131), result1);
			assertEpsilonEquals(createPoint(4.666435034231865, 2.3806214774740937, -1.4290677837888595), result2);
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, -4, 0, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(5.489624176405976, 1.240859889746265, 2.965332235207968), result1);
			assertEpsilonEquals(createPoint(4.424105585054724, 1.32638646564068, 3.8045617611719145), result2);
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, -4, 5, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result1);
			assertEpsilonEquals(createPoint(4.141245860927152, 0.8863824503311264, 6.745136589403973), result2);
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToSegment(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, -4, -5, 10, 10, 10,
					result1, result2);
			assertEpsilonEquals(createPoint(4.6789346310202795, 1.5210313226361163, 0.9085389126696517), result1);
			assertEpsilonEquals(createPoint(4.5683195318231, 1.550719271724823, 0.9471992197051673), result2);
		}
	}

	@DisplayName("findsFarthestPointToPoint(PathIterator3afp,double,double,double,Point3D) close path")
	@Nested
	public class FindsFarthestPointToPoint {

		private Point3D result;

		@BeforeEach
		public void setUp() {
			this.result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		}

		@DisplayName("Open path #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Open path #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 5, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Open path #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, -5, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Open path #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Open path #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 5, result);
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("Open path #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, -5, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Open path #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 0, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Open path #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 5, result);
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("Open path #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, -5, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Open path #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Open path #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 5, result);
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("Open path #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, -5, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Open path #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
			assertEpsilonEquals(createPoint(4, 3, -2), result);
		}

		@DisplayName("Open path #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 5, result);
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("Open path #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void open_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, -5, result);
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
		}

		@DisplayName("Close path #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Close path #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 5, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Close path #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, -5, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Close path #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Close path #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 5, result);
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("Close path #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, -5, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Close path #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 0, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Close path #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 5, result);
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("Close path #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, -5, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Close path #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Close path #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 5, result);
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("Close path #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, -5, result);
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("Close path #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
			assertEpsilonEquals(createPoint(4, 3, -2), result);
		}

		@DisplayName("Close path #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 5, result);
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("Close path #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void close_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path3afp.findsFarthestPointToPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, -5, result);
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
		}

	}

	@DisplayName("calculatesLength")
	@Nested
	public class CalculatesLength {

		@DisplayName("Open path")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void openPath(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().clone();
			assertEpsilonEquals(25.40382315, Path3afp.calculatesLength(p.getPathIterator()));
		}

		@DisplayName("Close path")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void closePath(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getS().clone();
			p.closePath();
			assertEpsilonEquals(34.23558402124097, Path3afp.calculatesLength(p.getPathIterator()));
		}

	}

	@DisplayName("add")
	@Nested
	public class Add {

		@DisplayName("(Iterator) Open path")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void openPath(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp<?, ?, ?, ?, ?, B> p2 = createPath();
			p2.moveTo(7, -5, 2);
			p2.lineTo(4, 6, 18);
			p2.lineTo(0, 8, 7);
			p2.lineTo(5, -3, 8);
			p2.closePath();

			Iterator<? extends PathElement3afp> iterator = p2.getPathIterator();
			iterator.next();
			getS().add(iterator);

			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertElement(pi, PathElementType.LINE_TO, 4, 6, 18);
			assertElement(pi, PathElementType.LINE_TO, 0, 8, 7);
			assertElement(pi, PathElementType.LINE_TO, 5, -3, 8);
			assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("(Iterator) close-after path")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void addIterator_closeAfter(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp<?, ?, ?, ?, ?, B> p2 = createPath();
			p2.moveTo(7, -5, -6);
			p2.lineTo(4, 6, 18);
			p2.lineTo(0, 8, 7);
			p2.lineTo(5, -3, 8);
			p2.closePath();

			Iterator<? extends PathElement3afp> iterator = p2.getPathIterator();
			iterator.next();

			getS().add(iterator);

			getS().closePath();

			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertElement(pi, PathElementType.LINE_TO, 4, 6, 18);
			assertElement(pi, PathElementType.LINE_TO, 0, 8, 7);
			assertElement(pi, PathElementType.LINE_TO, 5, -3, 8);
			assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("(Iterator) close-before path")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void addIterator_closeBefore(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();

			Path3afp<?, ?, ?, ?, ?, B> p2 = createPath();
			p2.moveTo(7, -5, 2);
			p2.lineTo(4, 6, 18);
			p2.lineTo(0, 8, 7);
			p2.lineTo(5, -3, 8);
			p2.closePath();

			Iterator<? extends PathElement3afp> iterator = p2.getPathIterator();
			iterator.next();

			getS().add(iterator);

			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 4, 6, 18);
			assertElement(pi, PathElementType.LINE_TO, 0, 8, 7);
			assertElement(pi, PathElementType.LINE_TO, 5, -3, 8);
			assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
			assertNoElement(pi);
		}
	}

	@DisplayName("curveTo")
	@Nested
	public class CurveTo {

		@DisplayName("(x,y,z, x,y,z, x,y,z) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledoubledoubledoubledoubledoubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.curveTo(15, 145, 0, 50, 20, 0, 0, 0, 0);
			});
		}

		@DisplayName("(x,y,z, x,y,z, x,y,z) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledoubledoubledoubledoubledoubledoubledouble_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().curveTo(123.456, 456.789, 0, 789.123, 159.753, -18, 456.852, 963.789, 24);
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertElement(pi, PathElementType.CURVE_TO, 123.456, 456.789, 0, 789.123, 159.753, -18, 456.852, 963.789, 24);
			assertNoElement(pi);
		}

		@DisplayName("(Point3D,Point3D,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void Point3DPoint3DPoint3D_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.curveTo(createPoint(15, 145, 0), createPoint(50, 20, 0), createPoint(0, 0, 0));
			});
		}

		@DisplayName("(Point3D,Point3D,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void Point3DPoint3DPoint3D_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().curveTo(createPoint(123.456, 456.789, 0), createPoint(789.123, 159.753, -5), createPoint(456.852, 963.789, 45));
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertElement(pi, PathElementType.CURVE_TO, 123.456, 456.789, 0, 789.123, 159.753, -5, 456.852, 963.789, 45);
			assertNoElement(pi);
		}
	}

	@DisplayName("getCoordAt")
	@Nested
	public class GetCoordAt {

		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(0)==0);
		}

		@DisplayName("(int) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(1)==0);
		}

		@DisplayName("(int) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(2)==0);
		}

		@DisplayName("(int) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(3)==1);
		}

		@DisplayName("(int) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(4)==.5);
		}

		@DisplayName("(int) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(5)==-5);
		}

		@DisplayName("(int) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(6)==3);
		}

		@DisplayName("(int) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(7)==0);
		}

		@DisplayName("(int) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(8)==2);
		}

		@DisplayName("(int) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(9)==4);
		}

		@DisplayName("(int) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(10)==3);
		}

		@DisplayName("(int) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(11)==-2);
		}

		@DisplayName("(int) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(12)==5);
		}

		@DisplayName("(int) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(13)==-1);
		}

		@DisplayName("(int) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(14)==3);
		}

		@DisplayName("(int) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(15)==6);
		}

		@DisplayName("(int) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(16)==5);
		}

		@DisplayName("(int) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(17)==5);
		}

		@DisplayName("(int) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(18)==7);
		}

		@DisplayName("(int) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(19)==-5);
		}

		@DisplayName("(int) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(20)==2);
		}
	}

	@DisplayName("getPointAt")
	@Nested
	public class GetPointAt {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getPointAt(0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, .5, -5), getS().getPointAt(1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(3, 0, 2), getS().getPointAt(2));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(4, 3, -2), getS().getPointAt(3));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(5, -1, 3), getS().getPointAt(4));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(6, 5, 5), getS().getPointAt(5));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(7, -5, 2), getS().getPointAt(6));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> {
				getS().getPointAt(7);
			});
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getPointAt(0));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(createPoint(1, .5, -5), getS().getPointAt(1));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(createPoint(3, 0, 2), getS().getPointAt(2));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(createPoint(4, 3, -2), getS().getPointAt(3));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(createPoint(5, -1, 3), getS().getPointAt(4));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(createPoint(6, 5, 5), getS().getPointAt(5));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(createPoint(7, -5, 2), getS().getPointAt(6));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void int_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertThrows(AssertionError.class, () -> {
				getS().getPointAt(7);
			});
		}
	}

	@DisplayName("size")
	@Nested
	public class Size {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(7, getS().size());
		}
	}

	@DisplayName("getPathElementCount")
	@Nested
	public class GetPathElementCount {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(4, getS().getPathElementCount());
		}
	}

	@DisplayName("getPathElementTypeAt")
	@Nested
	public class GetPathElementTypeAt {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PathElementType.MOVE_TO, getS().getPathElementTypeAt(0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PathElementType.LINE_TO, getS().getPathElementTypeAt(1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PathElementType.QUAD_TO, getS().getPathElementTypeAt(2));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PathElementType.CURVE_TO, getS().getPathElementTypeAt(3));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> {
				getS().getPathElementTypeAt(4);
			});
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertSame(PathElementType.MOVE_TO, getS().getPathElementTypeAt(0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertSame(PathElementType.LINE_TO, getS().getPathElementTypeAt(1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertSame(PathElementType.QUAD_TO, getS().getPathElementTypeAt(2));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertSame(PathElementType.CURVE_TO, getS().getPathElementTypeAt(3));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertSame(PathElementType.CLOSE, getS().getPathElementTypeAt(4));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertThrows(AssertionError.class, () -> {
				getS().getPathElementTypeAt(5);
			});
		}
	}

	@DisplayName("toBoundingBox")
	@Nested
	public class ToBoundingBox {

		@DisplayName("(box) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var box = getS().getGeomFactory().newBox();
			getS().toBoundingBox(box);
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(7, box.getMaxX());

			assertEpsilonEquals(-5, box.getMinY());
			assertEpsilonEquals(3, box.getMaxY());

			assertEpsilonEquals(-5, box.getMinZ());
			assertEpsilonEquals(3.421875, box.getMaxZ());
		}

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			B box = getS().toBoundingBox();
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(7, box.getMaxX());

			assertEpsilonEquals(-5, box.getMinY());
			assertEpsilonEquals(3, box.getMaxY());

			assertEpsilonEquals(-5, box.getMinZ());
			assertEpsilonEquals(3.421875, box.getMaxZ());
		}
	}

	@DisplayName("getPathIterator")
	@Nested
	public class GetPathIterator {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertNoElement(pi);
		}

		@DisplayName("() #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void empty_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			var pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("(Transform3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transform_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Transform3D transform = new Transform3D();
			var pi = getS().getPathIterator(null);
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertNoElement(pi);
		}

		@DisplayName("(Transform3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transform_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Transform3D transform = new Transform3D();
			transform.setIdentity();
			var pi = getS().getPathIterator(transform);
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertNoElement(pi);
		}

		@DisplayName("(Transform3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transform_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Transform3D transform = new Transform3D();
			transform.setIdentity();
			transform.setTranslation(14, -5, 1.5);
			var pi = getS().getPathIterator(transform);
			assertElement(pi, PathElementType.MOVE_TO, 14, -5, 1.5);
			assertElement(pi, PathElementType.LINE_TO, 15, -4.5, -3.5);
			assertElement(pi, PathElementType.QUAD_TO, 17, -5, 3.5, 18, -2, -.5);
			assertElement(pi, PathElementType.CURVE_TO, 19, -6, 4.5, 20, 0, 6.5, 21, -10, 3.5);
			assertNoElement(pi);
		}

		@DisplayName("(Transform3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transform_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Transform3D transform = new Transform3D();
			var pi = getS().getPathIterator(null);
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("(Transform3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transform_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Transform3D transform = new Transform3D();
			transform.setIdentity();
			var pi = getS().getPathIterator(transform);
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("(Transform3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transform_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Transform3D transform = new Transform3D();
			transform.setIdentity();
			transform.setTranslation(14, -5, 1.5);
			var pi = getS().getPathIterator(transform);
			assertElement(pi, PathElementType.MOVE_TO, 14, -5, 1.5);
			assertElement(pi, PathElementType.LINE_TO, 15, -4.5, -3.5);
			assertElement(pi, PathElementType.QUAD_TO, 17, -5, 3.5, 18, -2, -.5);
			assertElement(pi, PathElementType.CURVE_TO, 19, -6, 4.5, 20, 0, 6.5, 21, -10, 3.5);
			assertElement(pi, PathElementType.CLOSE, 14, -5, 1.5);
			assertNoElement(pi);
		}

		@DisplayName("(double) open path #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var pi = getS().getPathIterator(SPLINE_APPROXIMATION_RATIO);
			assertElement(pi, PathElementType.MOVE_TO, 0.0, 0.0, 0.0);
			assertElement(pi, PathElementType.LINE_TO, 1.0, 0.5, -5.0);
			// quadTo(3, 0, 2, 4, 3, -2)
			assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.4296875, -3.421875);
			assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.46875, -2.1875);
			assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.6171875, -1.296875);
			assertElement(pi, PathElementType.LINE_TO, 2.75, 0.875, -0.75);
			assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.2421875, -0.546875);
			assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.71875, -0.6875);
			assertElement(pi, PathElementType.LINE_TO, 3.734375, 2.3046875, -1.171875);
			assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0, -2.0);
			// curveTo(5, -1, 3, 6, 5, 5, 7, -5, 2)
			assertElement(pi, PathElementType.LINE_TO, 4.25, 2.15625, -0.796875);
			assertElement(pi, PathElementType.LINE_TO, 4.625, 1.546875, 0.734375);
			assertElement(pi, PathElementType.LINE_TO, 5.0, 1.3671875, 1.9453125);
			assertElement(pi, PathElementType.LINE_TO, 5.375, 1.3125, 2.8125);
			assertElement(pi, PathElementType.LINE_TO, 5.75, 1.078125, 3.3125);
			assertElement(pi, PathElementType.LINE_TO, 6.125, 0.359375, 3.421875);
			assertElement(pi, PathElementType.LINE_TO, 6.5, -1.1484375, 3.1171875);
			assertElement(pi, PathElementType.LINE_TO, 6.875, -3.75, 2.375);
			assertElement(pi, PathElementType.LINE_TO, 7.0, -5.0, 2.0);
			assertNoElement(pi);
		}

		@DisplayName("(double) close path #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			var pi = getS().getPathIterator(SPLINE_APPROXIMATION_RATIO);
			assertElement(pi, PathElementType.MOVE_TO, 0.0, 0.0, 0.0);
			assertElement(pi, PathElementType.LINE_TO, 1.0, 0.5, -5.0);
			// quadTo(3, 0, 2, 4, 3, -2)
			assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.4296875, -3.421875);
			assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.46875, -2.1875);
			assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.6171875, -1.296875);
			assertElement(pi, PathElementType.LINE_TO, 2.75, 0.875, -0.75);
			assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.2421875, -0.546875);
			assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.71875, -0.6875);
			assertElement(pi, PathElementType.LINE_TO, 3.734375, 2.3046875, -1.171875);
			assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0, -2.0);
			// curveTo(5, -1, 3, 6, 5, 5, 7, -5, 2)
			assertElement(pi, PathElementType.LINE_TO, 4.25, 2.15625, -0.796875);
			assertElement(pi, PathElementType.LINE_TO, 4.625, 1.546875, 0.734375);
			assertElement(pi, PathElementType.LINE_TO, 5.0, 1.3671875, 1.9453125);
			assertElement(pi, PathElementType.LINE_TO, 5.375, 1.3125, 2.8125);
			assertElement(pi, PathElementType.LINE_TO, 5.75, 1.078125, 3.3125);
			assertElement(pi, PathElementType.LINE_TO, 6.125, 0.359375, 3.421875);
			assertElement(pi, PathElementType.LINE_TO, 6.5, -1.1484375, 3.1171875);
			assertElement(pi, PathElementType.LINE_TO, 6.875, -3.75, 2.375);
			assertElement(pi, PathElementType.LINE_TO, 7.0, -5.0, 2.0);
			// closePath
			assertElement(pi, PathElementType.CLOSE, 0., 0., 0.);
			assertNoElement(pi);
		}

		@DisplayName("(Transform3D, double) no-transform #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformdouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var pi = getS().getPathIterator(null, SPLINE_APPROXIMATION_RATIO);
			assertElement(pi, PathElementType.MOVE_TO, 0.0, 0.0, 0.0);
			assertElement(pi, PathElementType.LINE_TO, 1.0, 0.5, -5.0);
			// quadTo(3, 0, 2, 4, 3, -2)
			assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.4296875, -3.421875);
			assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.46875, -2.1875);
			assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.6171875, -1.296875);
			assertElement(pi, PathElementType.LINE_TO, 2.75, 0.875, -0.75);
			assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.2421875, -0.546875);
			assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.71875, -0.6875);
			assertElement(pi, PathElementType.LINE_TO, 3.734375, 2.3046875, -1.171875);
			assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0, -2.0);
			// curveTo(5, -1, 3, 6, 5, 5, 7, -5, 2)
			assertElement(pi, PathElementType.LINE_TO, 4.25, 2.15625, -0.796875);
			assertElement(pi, PathElementType.LINE_TO, 4.625, 1.546875, 0.734375);
			assertElement(pi, PathElementType.LINE_TO, 5.0, 1.3671875, 1.9453125);
			assertElement(pi, PathElementType.LINE_TO, 5.375, 1.3125, 2.8125);
			assertElement(pi, PathElementType.LINE_TO, 5.75, 1.078125, 3.3125);
			assertElement(pi, PathElementType.LINE_TO, 6.125, 0.359375, 3.421875);
			assertElement(pi, PathElementType.LINE_TO, 6.5, -1.1484375, 3.1171875);
			assertElement(pi, PathElementType.LINE_TO, 6.875, -3.75, 2.375);
			assertElement(pi, PathElementType.LINE_TO, 7.0, -5.0, 2.0);
			assertNoElement(pi);
		}

		@DisplayName("(Transform3D,double) identity-transform #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformdouble_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var pi = getS().getPathIterator(new Transform3D(), SPLINE_APPROXIMATION_RATIO);
			assertElement(pi, PathElementType.MOVE_TO, 0.0, 0.0, 0.0);
			assertElement(pi, PathElementType.LINE_TO, 1.0, 0.5, -5.0);
			// quadTo(3, 0, 2, 4, 3, -2)
			assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.4296875, -3.421875);
			assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.46875, -2.1875);
			assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.6171875, -1.296875);
			assertElement(pi, PathElementType.LINE_TO, 2.75, 0.875, -0.75);
			assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.2421875, -0.546875);
			assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.71875, -0.6875);
			assertElement(pi, PathElementType.LINE_TO, 3.734375, 2.3046875, -1.171875);
			assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0, -2.0);
			// curveTo(5, -1, 3, 6, 5, 5, 7, -5, 2)
			assertElement(pi, PathElementType.LINE_TO, 4.25, 2.15625, -0.796875);
			assertElement(pi, PathElementType.LINE_TO, 4.625, 1.546875, 0.734375);
			assertElement(pi, PathElementType.LINE_TO, 5.0, 1.3671875, 1.9453125);
			assertElement(pi, PathElementType.LINE_TO, 5.375, 1.3125, 2.8125);
			assertElement(pi, PathElementType.LINE_TO, 5.75, 1.078125, 3.3125);
			assertElement(pi, PathElementType.LINE_TO, 6.125, 0.359375, 3.421875);
			assertElement(pi, PathElementType.LINE_TO, 6.5, -1.1484375, 3.1171875);
			assertElement(pi, PathElementType.LINE_TO, 6.875, -3.75, 2.375);
			assertElement(pi, PathElementType.LINE_TO, 7.0, -5.0, 2.0);
			assertNoElement(pi);
		}

		@DisplayName("(Transform3D,double) translation #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformdouble_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Transform3D transform = new Transform3D();
			transform.setTranslation(10, 5, -2);
			var pi = getS().getPathIterator(transform, SPLINE_APPROXIMATION_RATIO);
			assertElement(pi, PathElementType.MOVE_TO, 10.0, 5.0, -2.0);
			assertElement(pi, PathElementType.LINE_TO, 11.0, 5.5, -7.0);
			// quadTo(3, 0, 2, 4, 3, -2)
			assertElement(pi, PathElementType.LINE_TO, 11.484375, 5.4296875, -5.421875);
			assertElement(pi, PathElementType.LINE_TO, 11.9375, 5.46875, -4.1875);
			assertElement(pi, PathElementType.LINE_TO, 12.359375, 5.6171875, -3.296875);
			assertElement(pi, PathElementType.LINE_TO, 12.75, 5.875, -2.75);
			assertElement(pi, PathElementType.LINE_TO, 13.109375, 6.2421875, -2.546875);
			assertElement(pi, PathElementType.LINE_TO, 13.4375, 6.71875, -2.6875);
			assertElement(pi, PathElementType.LINE_TO, 13.734375, 7.3046875, -3.171875);
			assertElement(pi, PathElementType.LINE_TO, 14.0, 8.0, -4.0);
			// curveTo(5, -1, 3, 6, 5, 5, 7, -5, 2)
			assertElement(pi, PathElementType.LINE_TO, 14.25, 7.15625, -2.796875);
			assertElement(pi, PathElementType.LINE_TO, 14.625, 6.546875, -1.26563);
			assertElement(pi, PathElementType.LINE_TO, 15.0, 6.3671875, -0.0546875);
			assertElement(pi, PathElementType.LINE_TO, 15.375, 6.3125, 0.8125);
			assertElement(pi, PathElementType.LINE_TO, 15.75, 6.078125, 1.3125);
			assertElement(pi, PathElementType.LINE_TO, 16.125, 5.359375, 1.421875);
			assertElement(pi, PathElementType.LINE_TO, 16.5, 3.8515625, 1.1171875);
			assertElement(pi, PathElementType.LINE_TO, 16.875, 1.25, 0.375);
			assertElement(pi, PathElementType.LINE_TO, 17.0, 0.0, 0.0);
			assertNoElement(pi);
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
			assertEpsilonEquals(25.40382315, getS().getLength());
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
			assertEpsilonEquals(645.354230636, getS().getLengthSquared());
		}
	}

	@DisplayName("lineTo")
	@Nested
	public class LineTo {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.lineTo(15, 145, 0);
			});
		}

		@DisplayName("(double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().lineTo(123.456, 456.789, 0);
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertElement(pi, PathElementType.LINE_TO, 123.456, 456.789, 0);
			assertNoElement(pi);	
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.lineTo(createPoint(15, 145, 0));
			});
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().lineTo(createPoint(123.456, 456.789, -42));
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertElement(pi, PathElementType.LINE_TO, 123.456, 456.789, -42);
			assertNoElement(pi);
		}
	}

	@DisplayName("moveTo")
	@Nested
	public class MoveTo {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().moveTo(123.456, 456.789, 0);
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789, 0);
			assertNoElement(pi);
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().moveTo(createPoint(123.456, 456.789, 0));
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789, 0);
			assertNoElement(pi);
		}
	}

	@DisplayName("quadTo")
	@Nested
	public class QuadTo {

		@DisplayName("(double,double,double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledoubledoubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.quadTo(15, 145, 0, 50, 20, 0);
			});
		}

		@DisplayName("(double,double,double,double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledoubledoubledoubledouble_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().quadTo(123.456, 456.789, 0, 789.123, 159.753, 62);
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertElement(pi, PathElementType.QUAD_TO, 123.456, 456.789, 0, 789.123, 159.753, 62);
			assertNoElement(pi);
		}

		@DisplayName("(Point3D,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.quadTo(createPoint(15, 145, 0), createPoint(50, 20, 0));
			});
		}

		@DisplayName("(Point3D,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().quadTo(createPoint(123.456, 456.789, -4), createPoint(789.123, 159.753, 6));
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertElement(pi, PathElementType.QUAD_TO, 123.456, 456.789, -4, 789.123, 159.753, 6);
			assertNoElement(pi);
		}
	}

	@DisplayName("removeLast")
	@Nested
	public class RemoveLast {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().removeLast();
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().removeLast();
			getS().removeLast();
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertNoElement(pi);
		}
	}

	@DisplayName("remove")
	@Nested
	public class Remove {

		@DisplayName("(double,double,double) w/ outside")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_outside(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);

			assertFalse(getS().remove(1005, -1, 0));

			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertNoElement(pi);
		}

		@DisplayName("(double,double,double) w/ lineTo")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_lineTo(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);

			assertTrue(getS().remove(1, .5, -5));

			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertNoElement(pi);
		}

		@DisplayName("(double,double,double) w/ moveTo")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_moveTo(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);

			assertTrue(getS().remove(0., 0., 0.));

			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertNoElement(pi);
		}

		@DisplayName("(double,double,double) w/ quadTo - control")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_quadTo_ctrl(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);

			assertTrue(getS().remove(3, 0, 2));

			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.LINE_TO, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertNoElement(pi);
		}

		@DisplayName("(double,double,double) w/ quadTo - end")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_quadTo_end(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);

			assertTrue(getS().remove(4, 3, -2));

			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertNoElement(pi);
		}

		@DisplayName("(double,double,double) w/ curveTo - control1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_curveTo_ctrl1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);

			assertTrue(getS().remove(5, -1, 3));

			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.QUAD_TO, 6, 5, 5, 7, -5, 2);
			assertNoElement(pi);
		}

		@DisplayName("(double,double,double) w/ curveTo - control2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_curveTo_ctrl2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);

			assertTrue(getS().remove(6, 5, 5));

			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.QUAD_TO, 5, -1, 3, 7, -5, 2);
			assertNoElement(pi);
		}

		@DisplayName("(double,double,double) w/ curveTo - end")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_curveTo_end(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);

			assertTrue(getS().remove(7, -5, 2));

			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertNoElement(pi);
		}
	}

	@DisplayName("setLastPoint(double,double,double)")
	@Nested
	public class SetLastPoint {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCurrentPoint().equals(createPoint(7, -5, 2)));
			getS().setLastPoint(2, 2, 4);
			assertTrue(getS().getCurrentPoint().equals(createPoint(2, 2, 4)));
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 2, 2, 4);
			assertNoElement(pi);
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCurrentPoint().equals(createPoint(7, -5, 2)));
			getS().setLastPoint(createPoint(2, 2, -42));
			assertTrue(getS().getCurrentPoint().equals(createPoint(2, 2, -42)));
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 2, 2, -42);
			assertNoElement(pi);
		}
	}

	@DisplayName("transform")
	@Nested
	public class Transform {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPath3dTestCase#proposeArguments7Points")
		public final void test_1(CoordinateSystem3D cs, Point3D p1, Point3D p2, Point3D p3, Point3D p4,
				Point3D p5, Point3D p6, Point3D p7) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp path = createPath();
			path.moveTo(p1.getX(), p1.getY(), p1.getZ());
			path.lineTo(p2.getX(), p2.getY(), p2.getZ());
			path.quadTo(p3.getX(), p3.getY(), p3.getZ(), p4.getX(),p4.getY(), p4.getZ());
			path.curveTo(p5.getX(), p5.getY(), p5.getZ(), p6.getX(), p6.getY(), p6.getZ(), p7.getX(), p7.getY(), p7.getZ());
			path.closePath();

			Transform3D trans = new Transform3D(randomMatrix4d());

			trans.transform(p1);
			trans.transform(p2);
			trans.transform(p3);
			trans.transform(p4);
			trans.transform(p5);
			trans.transform(p6);
			trans.transform(p7);

			Path3afp pathTrans = createPath();
			pathTrans.moveTo(p1.getX(), p1.getY(), p1.getZ());
			pathTrans.lineTo(p2.getX(), p2.getY(), p2.getZ());
			pathTrans.quadTo(p3.getX(), p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ());
			pathTrans.curveTo(p5.getX(), p5.getY(), p5.getZ(), p6.getX(), p6.getY(), p6.getZ(), p7.getX(), p7.getY(), p7.getZ());
			pathTrans.closePath();

			path.transform(trans);

			assertNotSame(path, pathTrans);
			assertTrue(path.equalsToPathIterator(pathTrans.getPathIterator()));
		}
	}

	@DisplayName("createTransformedShape")
	@Nested
	public class CreateTransformedShape {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPath3dTestCase#proposeArguments7Points()")
		public final void test_1(CoordinateSystem3D cs, Point3D p1, Point3D p2, Point3D p3, Point3D p4,
				Point3D p5, Point3D p6, Point3D p7) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp path = createPath();
			path.moveTo(p1.getX(), p1.getY(), p1.getZ());
			path.lineTo(p2.getX(), p2.getY(), p2.getZ());
			path.quadTo(p3.getX(), p3.getY(), p3.getZ(), p4.getX(),p4.getY(), p4.getZ());
			path.curveTo(p5.getX(), p5.getY(), p5.getZ(), p6.getX(), p6.getY(), p6.getZ(), p7.getX(), p7.getY(), p7.getZ());
			path.closePath();

			Transform3D trans = new Transform3D(randomMatrix4d());

			trans.transform(p1);
			trans.transform(p2);
			trans.transform(p3);
			trans.transform(p4);
			trans.transform(p5);
			trans.transform(p6);
			trans.transform(p7);

			Path3afp expectedTrans = createPath();
			expectedTrans.moveTo(p1.getX(), p1.getY(), p1.getZ());
			expectedTrans.lineTo(p2.getX(), p2.getY(), p2.getZ());
			expectedTrans.quadTo(p3.getX(), p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ());
			expectedTrans.curveTo(p5.getX(), p5.getY(), p5.getZ(), p6.getX(), p6.getY(), p6.getZ(), p7.getX(), p7.getY(), p7.getZ());
			expectedTrans.closePath();

			Path3afp pathTrans = path.createTransformedShape(trans);

			assertNotSame(path, pathTrans);
			assertTrue(expectedTrans.equalsToPathIterator(pathTrans.getPathIterator()));
		}
	}

	@DisplayName("clone")
	@Nested
	public class Clone {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp clone = getS().clone();
			PathIterator3afp pi = (PathIterator3afp) clone.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
			assertNoElement(pi);
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
			assertFalse(getS().equals(createPath()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSegment(5, 8, 0, 5, 10, 0)));
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
			assertTrue(getS().equals(getS().clone()));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(getS().getPathIterator()));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(getS().clone().getPathIterator()));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createPath().getPathIterator()));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = getS().getGeomFactory().newPath();
			path.moveTo(5, 8, 0);
			path.lineTo(5, 10, 0);
			assertFalse(getS().equals(path.getPathIterator()));
		}
	}

	@DisplayName("equalsToPathIterator")
	@Nested
	public class EqualsToPathIterator {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToPathIterator((PathIterator3afp) null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToPathIterator(getS().clone().getPathIterator()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToPathIterator(createPath().getPathIterator()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var path = getS().getGeomFactory().newPath();
			path.moveTo(5, 8, 0);
			path.lineTo(5, 10, 0);
			assertFalse(getS().equalsToPathIterator(path.getPathIterator()));
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
			assertFalse(getS().equalsToShape((T) createPath()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape(getS()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape(getS().clone()));
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
			PathIterator3afp pi = getS().getPathIterator();
			assertNoElement(pi);
		}
	}

	@DisplayName("contains")
	@Nested
	public class Contains {

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
			assertTrue(getS().contains(1, .5, -5));
		}

		@DisplayName("(double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Control point
			assertFalse(getS().contains(3, 0, 2));
		}

		@DisplayName("(double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(4, 3, -2));
		}

		@DisplayName("(double,double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Control point
			assertFalse(getS().contains(5, -1, 3));
		}

		@DisplayName("(double,double,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Control point
			assertFalse(getS().contains(6, 5, 5));
		}

		@DisplayName("(double,double,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(7, -5, 2));
		}

		@DisplayName("(double,double,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(1.484375, 0.4296875, -3.421875));
		}

		@DisplayName("(double,double,double) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(1.9375, 0.46875, -2.1875));
		}

		@DisplayName("(double,double,double) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(2.359375, 0.6171875, -1.296875));
		}

		@DisplayName("(double,double,double) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(2.75, 0.875, -0.75));
		}

		@DisplayName("(double,double,double) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(3.109375, 1.2421875, -0.546875));
		}

		@DisplayName("(double,double,double) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(3.4375, 1.71875, -0.6875));
		}

		@DisplayName("(double,double,double) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(3.734375, 2.3046875, -1.171875));
		}

		@DisplayName("(double,double,double) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(4.0, 3.0, -2.0));
		}

		@DisplayName("(double,double,double) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(4.25, 2.15625, -0.796875));
		}

		@DisplayName("(double,double,double) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(4.625, 1.546875, 0.734375));
		}

		@DisplayName("(double,double,double) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(5.0, 1.3671875, 1.9453125));
		}

		@DisplayName("(double,double,double) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(5.375, 1.3125, 2.8125));
		}

		@DisplayName("(double,double,double) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(5.75, 1.078125, 3.3125));
		}

		@DisplayName("(double,double,double) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(6.125, 0.359375, 3.421875));
		}

		@DisplayName("(double,double,double) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(6.5, -1.1484375, 3.1171875));
		}

		@DisplayName("(double,double,double) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(6.875, -3.75, 2.375));
		}

		@DisplayName("(double,double,double) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(7.0, -5.0, 2.0));
		}

		@DisplayName("(double,double,double) #25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(-5, 1, 0));
		}

		@DisplayName("(double,double,double) #26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_26(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(3, 6, 0));
		}

		@DisplayName("(double,double,double) #27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_27(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(3, -10, 0));
		}

		@DisplayName("(double,double,double) #28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_28(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(11, 1, 0));
		}

		@DisplayName("(double,double,double) #29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_29(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(4, 1, 0));
		}

		@DisplayName("(double,double,double) #30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_30(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(4, 3, 0));
		}

		@DisplayName("(double,double,double) #31")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_31(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(0, 0, 0));
		}

		@DisplayName("(double,double,double) #32")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_32(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(1, .5, -5));
		}

		@DisplayName("(double,double,double) #33")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_33(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			// Control point
			assertFalse(getS().contains(3, 0, 2));
		}

		@DisplayName("(double,double,double) #34")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_34(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(4, 3, -2));
		}

		@DisplayName("(double,double,double) #35")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_35(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			// Control point
			assertFalse(getS().contains(5, -1, 3));
		}

		@DisplayName("(double,double,double) #36")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_36(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			// Control point
			assertFalse(getS().contains(6, 5, 5));
		}

		@DisplayName("(double,double,double) #37")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_37(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(7, -5, 2));
		}

		@DisplayName("(double,double,double) #38")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_38(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(1.484375, 0.4296875, -3.421875));
		}

		@DisplayName("(double,double,double) #39")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_39(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(1.9375, 0.46875, -2.1875));
		}

		@DisplayName("(double,double,double) #40")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_40(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(2.359375, 0.6171875, -1.296875));
		}

		@DisplayName("(double,double,double) #41")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_41(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(2.75, 0.875, -0.75));
		}

		@DisplayName("(double,double,double) #42")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_42(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(3.109375, 1.2421875, -0.546875));
		}

		@DisplayName("(double,double,double) #43")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_43(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(3.4375, 1.71875, -0.6875));
		}

		@DisplayName("(double,double,double) #44")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_44(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(3.734375, 2.3046875, -1.171875));
		}

		@DisplayName("(double,double,double) #45")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_45(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(4.0, 3.0, -2.0));
		}

		@DisplayName("(double,double,double) #46")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_46(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(4.25, 2.15625, -0.796875));
		}

		@DisplayName("(double,double,double) #47")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_47(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(4.625, 1.546875, 0.734375));
		}

		@DisplayName("(double,double,double) #48")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_48(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(5.0, 1.3671875, 1.9453125));
		}

		@DisplayName("(double,double,double) #49")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_49(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(5.375, 1.3125, 2.8125));
		}

		@DisplayName("(double,double,double) #50")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_50(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(5.75, 1.078125, 3.3125));
		}

		@DisplayName("(double,double,double) #51")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_51(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(6.125, 0.359375, 3.421875));
		}

		@DisplayName("(double,double,double) #52")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_52(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(6.5, -1.1484375, 3.1171875));
		}

		@DisplayName("(double,double,double) #53")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_53(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(6.875, -3.75, 2.375));
		}

		@DisplayName("(double,double,double) #54")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_54(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(7.0, -5.0, 2.0));
		}

		@DisplayName("(double,double,double) #55")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_55(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(-5, 1, 0));
		}

		@DisplayName("(double,double,double) #56")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_56(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(3, 6, 0));
		}

		@DisplayName("(double,double,double) #57")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_57(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(3, -10, 0));
		}

		@DisplayName("(double,double,double) #58")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_58(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(11, 1, 0));
		}

		@DisplayName("(double,double,double) #59")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_59(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(4, 1, 0));
		}

		@DisplayName("(double,double,double) #60")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_60(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(4, 3, 0));
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
			assertTrue(getS().contains(createPoint(1, .5, -5)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Control point
			assertFalse(getS().contains(createPoint(3, 0, 2)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(4, 3, -2)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Control point
			assertFalse(getS().contains(createPoint(5, -1, 3)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Control point
			assertFalse(getS().contains(createPoint(6, 5, 5)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(7, -5, 2)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(1.484375, 0.4296875, -3.421875)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(1.9375, 0.46875, -2.1875)));
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(2.359375, 0.6171875, -1.296875)));
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(2.75, 0.875, -0.75)));
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(3.109375, 1.2421875, -0.546875)));
		}

		@DisplayName("(Point3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(3.4375, 1.71875, -0.6875)));
		}

		@DisplayName("(Point3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(3.734375, 2.3046875, -1.171875)));
		}

		@DisplayName("(Point3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(4.0, 3.0, -2.0)));
		}

		@DisplayName("(Point3D) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(4.25, 2.15625, -0.796875)));
		}

		@DisplayName("(Point3D) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(4.625, 1.546875, 0.734375)));
		}

		@DisplayName("(Point3D) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(5.0, 1.3671875, 1.9453125)));
		}

		@DisplayName("(Point3D) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(5.375, 1.3125, 2.8125)));
		}

		@DisplayName("(Point3D) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(5.75, 1.078125, 3.3125)));
		}

		@DisplayName("(Point3D) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(6.125, 0.359375, 3.421875)));
		}

		@DisplayName("(Point3D) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(6.5, -1.1484375, 3.1171875)));
		}

		@DisplayName("(Point3D) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(6.875, -3.75, 2.375)));
		}

		@DisplayName("(Point3D) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(7.0, -5.0, 2.0)));
		}

		@DisplayName("(Point3D) #25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(-5, 1, 0)));
		}

		@DisplayName("(Point3D) #26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_26(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(3, 6, 0)));
		}

		@DisplayName("(Point3D) #27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_27(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(3, -10, 0)));
		}

		@DisplayName("(Point3D) #28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_28(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(11, 1, 0)));
		}

		@DisplayName("(Point3D) #29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_29(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(4, 1, 0)));
		}

		@DisplayName("(Point3D) #30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_30(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(4, 3, 0)));
		}

		@DisplayName("(Point3D) #31")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_31(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #32")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_32(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(1, .5, -5)));
		}

		@DisplayName("(Point3D) #33")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_33(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			// Control point
			assertFalse(getS().contains(createPoint(3, 0, 2)));
		}

		@DisplayName("(Point3D) #34")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_34(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(4, 3, -2)));
		}

		@DisplayName("(Point3D) #35")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_35(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			// Control point
			assertFalse(getS().contains(createPoint(5, -1, 3)));
		}

		@DisplayName("(Point3D) #36")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_36(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			// Control point
			assertFalse(getS().contains(createPoint(6, 5, 5)));
		}

		@DisplayName("(Point3D) #37")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_37(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(7, -5, 2)));
		}

		@DisplayName("(Point3D) #38")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_38(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(1.484375, 0.4296875, -3.421875)));
		}

		@DisplayName("(Point3D) #39")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_39(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(1.9375, 0.46875, -2.1875)));
		}

		@DisplayName("(Point3D) #40")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_40(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(2.359375, 0.6171875, -1.296875)));
		}

		@DisplayName("(Point3D) #41")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_41(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(2.75, 0.875, -0.75)));
		}

		@DisplayName("(Point3D) #42")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_42(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(3.109375, 1.2421875, -0.546875)));
		}

		@DisplayName("(Point3D) #43")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_43(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(3.4375, 1.71875, -0.6875)));
		}

		@DisplayName("(Point3D) #44")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_44(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(3.734375, 2.3046875, -1.171875)));
		}

		@DisplayName("(Point3D) #45")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_45(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(4.0, 3.0, -2.0)));
		}

		@DisplayName("(Point3D) #46")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_46(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(4.25, 2.15625, -0.796875)));
		}

		@DisplayName("(Point3D) #47")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_47(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(4.625, 1.546875, 0.734375)));
		}

		@DisplayName("(Point3D) #48")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_48(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(5.0, 1.3671875, 1.9453125)));
		}

		@DisplayName("(Point3D) #49")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_49(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(5.375, 1.3125, 2.8125)));
		}

		@DisplayName("(Point3D) #50")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_50(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(5.75, 1.078125, 3.3125)));
		}

		@DisplayName("(Point3D) #51")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_51(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(6.125, 0.359375, 3.421875)));
		}

		@DisplayName("(Point3D) #52")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_52(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(6.5, -1.1484375, 3.1171875)));
		}

		@DisplayName("(Point3D) #53")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_53(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(6.875, -3.75, 2.375)));
		}

		@DisplayName("(Point3D) #54")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_54(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createPoint(7.0, -5.0, 2.0)));
		}

		@DisplayName("(Point3D) #55")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_55(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createPoint(-5, 1, 0)));
		}

		@DisplayName("(Point3D) #56")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_56(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createPoint(3, 6, 0)));
		}

		@DisplayName("(Point3D) #57")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_57(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createPoint(3, -10, 0)));
		}

		@DisplayName("(Point3D) #58")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_58(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createPoint(11, 1, 0)));
		}

		@DisplayName("(Point3D) #59")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_59(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createPoint(4, 1, 0)));
		}

		@DisplayName("(Point3D) #60")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_60(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createPoint(4, 3, 0)));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(1.484375, 0.4296875, -3.421875, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(1.9375, 0.46875, -2.1875, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(2.359375, 0.6171875, -1.296875, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(2.75, 0.875, -0.75, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(3.109375, 1.2421875, -0.546875, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(3.4375, 1.71875, -0.6875, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(3.734375, 2.3046875, -1.171875, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(4.0, 3.0, -2.0, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(4.25, 2.15625, -0.796875, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(4.625, 1.546875, 0.734375, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(5.0, 1.3671875, 1.9453125, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(5.375, 1.3125, 2.8125, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(5.75, 1.078125, 3.3125, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(6.125, 0.359375, 3.421875, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(6.5, -1.1484375, 3.1171875, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(6.875, -3.75, 2.375, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBox(7.0, -5.0, 2.0, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBox(-5, 1, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBox(3, 6, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBox(3, -10, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBox(11, 1, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBox(3, 1, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBox(4, 3, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createAlignedBox(-5, 1, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createAlignedBox(3, 6, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_26(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createAlignedBox(3, -10, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_27(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createAlignedBox(11, 1, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_28(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createAlignedBox(3, 0, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_30(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createAlignedBox(4, 3, 0, 2, 1, 0)));
		}
	}

	@DisplayName("containsControlPoint")
	@Nested
	public class ContainsControlPoint {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsControlPoint(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsControlPoint(createPoint(1, .5, -5)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Control point
			assertTrue(getS().containsControlPoint(createPoint(3, 0, 2)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsControlPoint(createPoint(4, 3, -2)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Control point
			assertTrue(getS().containsControlPoint(createPoint(5, -1, 3)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Control point
			assertTrue(getS().containsControlPoint(createPoint(6, 5, 5)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsControlPoint(createPoint(7, -5, 2)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(1.484375, 0.4296875, -3.421875)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(1.9375, 0.46875, -2.1875)));
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(2.359375, 0.6171875, -1.296875)));
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(2.75, 0.875, -0.75)));
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(3.109375, 1.2421875, -0.546875)));
		}

		@DisplayName("(Point3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(3.4375, 1.71875, -0.6875)));
		}

		@DisplayName("(Point3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(3.734375, 2.3046875, -1.171875)));
		}

		@DisplayName("(Point3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsControlPoint(createPoint(4.0, 3.0, -2.0)));
		}

		@DisplayName("(Point3D) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(4.25, 2.15625, -0.796875)));
		}

		@DisplayName("(Point3D) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(4.625, 1.546875, 0.734375)));
		}

		@DisplayName("(Point3D) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(5.0, 1.3671875, 1.9453125)));
		}

		@DisplayName("(Point3D) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(5.375, 1.3125, 2.8125)));
		}

		@DisplayName("(Point3D) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(5.75, 1.078125, 3.3125)));
		}

		@DisplayName("(Point3D) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(6.125, 0.359375, 3.421875)));
		}

		@DisplayName("(Point3D) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(6.5, -1.1484375, 3.1171875)));
		}

		@DisplayName("(Point3D) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(6.875, -3.75, 2.375)));
		}

		@DisplayName("(Point3D) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsControlPoint(createPoint(7.0, -5.0, 2.0)));
		}

		@DisplayName("(Point3D) #25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(-5, 1, 0)));
		}

		@DisplayName("(Point3D) #26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_26(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(3, 6, 0)));
		}

		@DisplayName("(Point3D) #27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_27(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(3, -10, 0)));
		}

		@DisplayName("(Point3D) #28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_28(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(11, 1, 0)));
		}

		@DisplayName("(Point3D) #29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_29(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(4, 1, 0)));
		}

		@DisplayName("(Point3D) #30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_30(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().containsControlPoint(createPoint(4, 3, 0)));
		}

		@DisplayName("(Point3D) #31")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_31(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().containsControlPoint(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #32")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_32(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().containsControlPoint(createPoint(1, .5, -5)));
		}

		@DisplayName("(Point3D) #33")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_33(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			// Control point
			assertTrue(getS().containsControlPoint(createPoint(3, 0, 2)));
		}

		@DisplayName("(Point3D) #34")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_34(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().containsControlPoint(createPoint(4, 3, -2)));
		}

		@DisplayName("(Point3D) #35")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_35(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			// Control point
			assertTrue(getS().containsControlPoint(createPoint(5, -1, 3)));
		}

		@DisplayName("(Point3D) #36")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_36(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			// Control point
			assertTrue(getS().containsControlPoint(createPoint(6, 5, 5)));
		}

		@DisplayName("(Point3D) #37")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_37(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().containsControlPoint(createPoint(7, -5, 2)));
		}

		@DisplayName("(Point3D) #38")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_38(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(1.484375, 0.4296875, -3.421875)));
		}

		@DisplayName("(Point3D) #39")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_39(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(1.9375, 0.46875, -2.1875)));
		}

		@DisplayName("(Point3D) #40")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_40(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(2.359375, 0.6171875, -1.296875)));
		}

		@DisplayName("(Point3D) #41")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_41(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(2.75, 0.875, -0.75)));
		}

		@DisplayName("(Point3D) #42")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_42(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(3.109375, 1.2421875, -0.546875)));
		}

		@DisplayName("(Point3D) #43")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_43(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(3.4375, 1.71875, -0.6875)));
		}

		@DisplayName("(Point3D) #44")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_44(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(3.734375, 2.3046875, -1.171875)));
		}

		@DisplayName("(Point3D) #45")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_45(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().containsControlPoint(createPoint(4.0, 3.0, -2.0)));
		}

		@DisplayName("(Point3D) #46")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_46(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(4.25, 2.15625, -0.796875)));
		}

		@DisplayName("(Point3D) #47")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_47(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(4.625, 1.546875, 0.734375)));
		}

		@DisplayName("(Point3D) #48")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_48(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(5.0, 1.3671875, 1.9453125)));
		}

		@DisplayName("(Point3D) #49")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_49(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(5.375, 1.3125, 2.8125)));
		}

		@DisplayName("(Point3D) #50")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_50(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(5.75, 1.078125, 3.3125)));
		}

		@DisplayName("(Point3D) #51")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_51(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(6.125, 0.359375, 3.421875)));
		}

		@DisplayName("(Point3D) #52")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_52(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(6.5, -1.1484375, 3.1171875)));
		}

		@DisplayName("(Point3D) #53")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_53(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(6.875, -3.75, 2.375)));
		}

		@DisplayName("(Point3D) #54")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_54(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().containsControlPoint(createPoint(7.0, -5.0, 2.0)));
		}

		@DisplayName("(Point3D) #55")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_55(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(-5, 1, 0)));
		}

		@DisplayName("(Point3D) #56")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_56(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(3, 6, 0)));
		}

		@DisplayName("(Point3D) #57")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_57(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(3, -10, 0)));
		}

		@DisplayName("(Point3D) #58")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_58(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(11, 1, 0)));
		}

		@DisplayName("(Point3D) #59")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_59(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(4, 1, 0)));
		}

		@DisplayName("(Point3D) #60")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_60(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().containsControlPoint(createPoint(4, 3, 0)));
		}
	}

	@DisplayName("findsClosestPointToAlignedBox(PathIterator3afp, double,double,double,double,double,double, Point3D)")
	@Nested
	public class FindsClosestPointToPathIteratorAlignedBox {

		private InnerComputationPoint3D resultOnPath;

		private InnerComputationPoint3D resultOnBox;

		@BeforeEach
		public void setUp() {
			this.resultOnPath = new InnerComputationPoint3D();
			this.resultOnBox = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, 1, 0, -1, 2, 1, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(0, 0, 0), resultOnPath);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, 1, 5, -1, 2, 6, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(0, 0, 0), resultOnPath);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, 1, -5, -1, 2, -4, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(0.74285714286,0.371428571429,-3.714285714286), resultOnPath);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, 0, 0, 2, 1, 1, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(0.0384615384615,0.0192307692308,-0.192307692308), resultOnPath);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, 0, 5, 2, 1, 6, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), resultOnPath);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, 0, -5, 2, 1, -4, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(1.0,0.5,-5.0), resultOnPath);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, 1, 0, 2, 2, 1, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(2.75,0.875,-0.75), resultOnPath);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, 1, 5, 2, 2, 6, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), resultOnPath);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, 1, -5, 2, 2, -4, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(1, .5, -5), resultOnPath);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					3, 0, 0, 4, 1, 1, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(3.109375,1.2421875,-0.546875), resultOnPath);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					3, 0, 5, 4, 1, 6, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), resultOnPath);
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					3, 0, -5, 4, 1, -4, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(1.45269664935,0.43428597026,-3.5250851102), resultOnPath);
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, -4, 0, 2, -3, 1, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(0, 0, 0), resultOnPath);
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, -4, 5, 2, -3, 6, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(6.58707187157,-1.752498609,2.94485775418), resultOnPath);
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, -4, -5, 2, -3, -4, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(0.74285714286,0.371428571429,-3.71428571429), resultOnPath);
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, 1, 0, 1, 1.1, .1, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(0.019801980198,0.009900990099,-0.09900990099), resultOnPath);
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, 1, 0, 1, 3.1, .1, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(0.019801980198,0.009900990099,-0.09900990099), resultOnPath);
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, 1, 0, 3, 1.1, .1, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(3.109375,1.2421875,-0.546875), resultOnPath);
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, 1, 0, 5, 1.1, .1, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(3.109375,1.2421875,-0.546875), resultOnPath);
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, 1, 0, 7, 1.1, .1, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(3.109375,1.2421875,-0.546875), resultOnPath);
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, 1, 0, 13, 1.1, .1, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(3.109375,1.2421875,-0.546875), resultOnPath);
		}

		@DisplayName("#22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp.findsClosestPointToPathIteratorAlignedBox(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					5, -2, 0.5, 10, 4, 5, resultOnPath, resultOnBox);
			assertEpsilonEquals(createPoint(5.0,1.3671875,1.9453125), resultOnPath);
		}

	}

	@DisplayName("findsClosestPointToPath")
	@Nested
	public class FindsClosestPointToPath {

		private InnerComputationPoint3D result1;

		private InnerComputationPoint3D result2;

		@BeforeEach
		public void setUp() {
			this.result1 = new InnerComputationPoint3D();
			this.result2 = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			Path3afp.findsClosestPointToPath(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					p.getPathIterator(SPLINE_APPROXIMATION_RATIO),
					result1, result2);
			assertEpsilonEquals(createPoint(5.624911238, 1.15630547637, 3.14571498375), result1);
			assertEpsilonEquals(createPoint(4.7242981755, 1.2071636258, 3.845014538), result2);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			p.lineTo(4, 6, 12);
			Path3afp.findsClosestPointToPath(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					p.getPathIterator(SPLINE_APPROXIMATION_RATIO),
					result1, result2);
			assertEpsilonEquals(createPoint(5.624911238, 1.15630547637, 3.14571498375), result1);
			assertEpsilonEquals(createPoint(4.7242981755, 1.2071636258, 3.845014538), result2);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			p.lineTo(4, 6, 12);
			p.closePath();
			Path3afp.findsClosestPointToPath(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					p.getPathIterator(SPLINE_APPROXIMATION_RATIO),
					result1, result2);
			assertEpsilonEquals(createPoint(5.624911238, 1.15630547637, 3.14571498375), result1);
			assertEpsilonEquals(createPoint(4.7242981755, 1.2071636258, 3.845014538), result2);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.quadTo(10, 10, 10, 4, 6, 12);
			Path3afp.findsClosestPointToPath(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					p.getPathIterator(SPLINE_APPROXIMATION_RATIO),
					result1, result2);
			assertEpsilonEquals(createPoint(5.667936956, 1.12941440234, 3.20308260833), result1);
			assertEpsilonEquals(createPoint(4.1505541713, 0.9817654216, 4.2719092373), result2);
		}

		@DisplayName("#5 - identical polylines => zero distance, same closest point")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var p1 = createPath();
			p1.moveTo(0, 0, 0);
			p1.lineTo(10, 0, 0);

			final var p2 = createPath();
			p2.moveTo(0, 0, 0);
			p2.lineTo(10, 0, 0);

			assertTrue(Path3afp.findsClosestPointToPath(
					p1.getPathIterator(),
					p2.getPathIterator(),
					result1, result2));

			assertEpsilonEquals(result1, result2);
		}

		@DisplayName("#6 - parallel disjoint segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var p1 = createPath();
			p1.moveTo(0, 0, 0);
			p1.lineTo(10, 0, 0);

			final var p2 = createPath();
			p2.moveTo(0, 2, 0);
			p2.lineTo(10, 2, 0);

			assertTrue(Path3afp.findsClosestPointToPath(
					p1.getPathIterator(),
					p2.getPathIterator(),
					result1, result2));

			// Closest pair has same x, fixed y gap = 2
			assertEpsilonEquals(result1.getX(), result2.getX());
			assertEpsilonEquals(0, result1.getY());
			assertEpsilonEquals(2, result2.getY());
			assertEpsilonEquals(0, result1.getZ());
			assertEpsilonEquals(0, result2.getZ());
		}

		@DisplayName("#7 - skew orthogonal segments")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var p1 = createPath();
			p1.moveTo(-2, 0, 0);
			p1.lineTo(2, 0, 0);

			final var p2 = createPath();
			p2.moveTo(0, -2, 1);
			p2.lineTo(0, 2, 1);

			assertTrue(Path3afp.findsClosestPointToPath(
					p1.getPathIterator(),
					p2.getPathIterator(),
					result1, result2));

			assertEpsilonEquals(createPoint(0, 0, 0), result1);
			assertEpsilonEquals(createPoint(0, 0, 1), result2);
		}

		@DisplayName("#8 - intersecting polylines")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var p1 = createPath();
			p1.moveTo(-1, 0, 0);
			p1.lineTo(1, 0, 0);

			final var p2 = createPath();
			p2.moveTo(0, -1, 0);
			p2.lineTo(0, 1, 0);

			assertTrue(Path3afp.findsClosestPointToPath(
					p1.getPathIterator(),
					p2.getPathIterator(),
					result1, result2));

			assertEpsilonEquals(createPoint(0, 0, 0), result1);
			assertEpsilonEquals(createPoint(0, 0, 0), result2);
		}

		@DisplayName("#9 - endpoint-to-endpoint closest configuration")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var p1 = createPath();
			p1.moveTo(0, 0, 0);
			p1.lineTo(1, 0, 0);

			final var p2 = createPath();
			p2.moveTo(3, 4, 0);
			p2.lineTo(3, 5, 0);

			assertTrue(Path3afp.findsClosestPointToPath(
					p1.getPathIterator(),
					p2.getPathIterator(),
					result1, result2));

			assertEpsilonEquals(createPoint(1, 0, 0), result1);
			assertEpsilonEquals(createPoint(3, 4, 0), result2);
		}

		@DisplayName("#10 - first path has only moveTo => no drawable element")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var p1 = createPath();
			p1.moveTo(0, 0, 0);

			final var p2 = createPath();
			p2.moveTo(0, 0, 1);
			p2.lineTo(1, 0, 1);

			assertFalse(Path3afp.findsClosestPointToPath(
					p1.getPathIterator(),
					p2.getPathIterator(),
					result1, result2));
		}

		@DisplayName("#11 - second path has only moveTo => no drawable element")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var p1 = createPath();
			p1.moveTo(0, 0, 0);
			p1.lineTo(1, 0, 0);

			final var p2 = createPath();
			p2.moveTo(0, 0, 1);

			assertFalse(Path3afp.findsClosestPointToPath(
					p1.getPathIterator(),
					p2.getPathIterator(),
					result1, result2));
		}

		@DisplayName("#12 - both paths empty => no point found")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var p1 = createPath();
			final var p2 = createPath();

			assertFalse(Path3afp.findsClosestPointToPath(
					p1.getPathIterator(),
					p2.getPathIterator(),
					result1, result2));
		}

	}

	@DisplayName("getFarthestPointTo")
	@Nested
	public class GetFarthestPointTo {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(-2, 1, 0));
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(-2, 1, 5));
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(-2, 1, -5));
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(1, 0, 0));
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(1, 0, 5));
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(1, 0, -5));
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(1, 1, 0));
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(1, 1, 5));
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(1, 1, -5));
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(3, 0, 0));
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(3, 0, 5));
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(3, 0, -5));
			assertEpsilonEquals(createPoint(7, -5, 2), result);
		}

		@DisplayName("(Point3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(1, -4, 0));
			assertEpsilonEquals(createPoint(4, 3, -2), result);
		}

		@DisplayName("(Point3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(1, -4, 5));
			assertEpsilonEquals(createPoint(1, .5, -5), result);
		}

		@DisplayName("(Point3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getFarthestPointTo(createPoint(1, -4, -5));
			assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
		}
	}

	@DisplayName("getDistanceSquared")
	@Nested
	public class GetDistanceSquared {

		private InnerComputationPoint3D result;

		@BeforeEach
		public void setUp() {
			this.result = new InnerComputationPoint3D();
		}

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.1568627451, getS().getDistanceSquared(createTriangle(-2, 2, 2, -1, -5, 3, -4, 5, 6)));
		}

		@DisplayName("(Triangle3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_2(CoordinateSystem3D cs) {
			// triangle containing path start point
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Path starts at (0,0,0), triangle contains that point => distance = 0
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(0, 0, 0, 2, 0, 0, 0, 2, 0)));
		}

		@DisplayName("(Triangle3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_3(CoordinateSystem3D cs) {
			// triangle containing path end point
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Path ends at (7,-5,2), triangle contains that point => distance = 0
			assertEpsilonEquals(0., getS().getDistanceSquared(createTriangle(7, -5, 2, 9, -5, 2, 7, -3, 2)));
		}

		@DisplayName("(Triangle3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_4(CoordinateSystem3D cs) {
			// far triangle near start side, closest to path start
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle in plane x=-3, nearest to path should be close to/start at (0,0,0)
			// nearest triangle point to (0,0,0): (-3,0,0), distance = 3
			assertEpsilonEquals(9., getS().getDistanceSquared(createTriangle(-3, 0, 0, -3, 1, 0, -3, 0, 1)));
		}

		@DisplayName("(Triangle3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_5(CoordinateSystem3D cs) {
			// far triangle near end side, closest to path end
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle in plane x=10, nearest to path should be end (7,-5,2)
			// nearest triangle point: (10,-5,2), distance = 3
			assertEpsilonEquals(9., getS().getDistanceSquared(createTriangle(10, -5, 2, 10, -4, 2, 10, -5, 3)));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2., getS().getDistanceSquared(createAlignedBoxFromPoints(-2, 1, 0, -1, 2, 1)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(27., getS().getDistanceSquared(createAlignedBoxFromPoints(-2, 1, 5, -1, 2, 6)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.5142857143, getS().getDistanceSquared(createAlignedBoxFromPoints(-2, 1, -5, -1, 2, -4)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.96153846154, getS().getDistanceSquared(createAlignedBoxFromPoints(1, 0, 0, 2, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16.2734375, getS().getDistanceSquared(createAlignedBoxFromPoints(1, 0, 5, 2, 1, 6)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createAlignedBoxFromPoints(1, 0, -5, 2, 1, -4)));
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.140625, getS().getDistanceSquared(createAlignedBoxFromPoints(1, 1, 0, 2, 2, 1)));
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16.17578125, getS().getDistanceSquared(createAlignedBoxFromPoints(1, 1, 5, 2, 2, 6)));
		}

		@DisplayName("(AlignedBox3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.25, getS().getDistanceSquared(createAlignedBoxFromPoints(1, 1, -5, 2, 2, -4)));
		}

		@DisplayName("(AlignedBox3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.3577270508, getS().getDistanceSquared(createAlignedBoxFromPoints(3, 0, 0, 4, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.91625977, getS().getDistanceSquared(createAlignedBoxFromPoints(3, 0, 5, 4, 1, 6)));
		}

		@DisplayName("(AlignedBox3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.6196918115, getS().getDistanceSquared(createAlignedBoxFromPoints(3, 0, -5, 4, 1, -4)));
		}

		@DisplayName("(AlignedBox3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10.0, getS().getDistanceSquared(createAlignedBoxFromPoints(1, -4, 0, 2, -3, 1)));
		}

		@DisplayName("(AlignedBox3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(26.821097726, getS().getDistanceSquared(createAlignedBoxFromPoints(1, -4, 5, 2, -3, 6)));
		}

		@DisplayName("(AlignedBox3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.5142857143, getS().getDistanceSquared(createAlignedBoxFromPoints(1, -4, -5, 2, -3, -4)));
		}

		@DisplayName("(AlignedBox3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.9900990099, getS().getDistanceSquared(createAlignedBoxFromPoints(-2, 1, 0, 1, 1.1, .1)));
		}

		@DisplayName("(AlignedBox3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.9900990099, getS().getDistanceSquared(createAlignedBoxFromPoints(-2, 1, 0, 1, 3.1, .1)));
		}

		@DisplayName("(AlignedBox3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.331252441, getS().getDistanceSquared(createAlignedBoxFromPoints(-2, 1, 0, 3, 1.1, .1)));
		}

		@DisplayName("(AlignedBox3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.319289551, getS().getDistanceSquared(createAlignedBoxFromPoints(-2, 1, 0, 5, 1.1, .1)));
		}

		@DisplayName("(AlignedBox3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.319289551, getS().getDistanceSquared(createAlignedBoxFromPoints(-2, 1, 0, 7, 1.1, .1)));
		}

		@DisplayName("(AlignedBox3afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.319289551, getS().getDistanceSquared(createAlignedBoxFromPoints(-2, 1, 0, 13, 1.1, .1)));
		}

		@DisplayName("(AlignedBox3afp) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createAlignedBoxFromPoints(5, -2, 0.5, 10, 4, 5)));
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.5627864045, getS().getDistanceSquared((Shape3d) createSphere(-2, 1, 0, .1)));
		}

		@DisplayName("(Shape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.3076923077, getS().getDistanceSquared((Shape3d) createSegment(-2, 1, 0, 10, 10, 10)));
		}

		@DisplayName("(Shape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			assertEpsilonEquals(1.3027103061, getS().getDistanceSquared((Shape3d) p));
		}

		@DisplayName("(Shape3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp ms = createMultiShape();
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			ms.add(p);
			ms.add(createSegment(-2, 1, 0, 10, 10, 10));
			assertEpsilonEquals(1.3027103061, getS().getDistanceSquared((Shape3d) ms));
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.5627864045, getS().getDistanceSquared(createSphere(-2, 1, 0, .1)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(28.914554885, getS().getDistanceSquared(createSphere(-2, 1, 5, .1)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.3731759498, getS().getDistanceSquared(createSphere(-2, 1, -5, .1)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.7757512749, getS().getDistanceSquared(createSphere(1, 0, 0, .1)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(24.645551771, getS().getDistanceSquared(createSphere(1, 0, 5, .1)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.1580963773, getS().getDistanceSquared(createSphere(1, 0, -5, .1)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.6475702854, getS().getDistanceSquared(createSphere(1, 1, 0, .1)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(23.0531633037, getS().getDistanceSquared(createSphere(1, 1, 5, .1)));
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.16, getS().getDistanceSquared(createSphere(1, 1, -5, .1)));
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.1647754717, getS().getDistanceSquared(createSphere(3, 0, 0, .1)));
		}

		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10.9021419308, getS().getDistanceSquared(createSphere(3, 0, 5, .1)));
		}

		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.4968410856, getS().getDistanceSquared(createSphere(3, 0, -5, .1)));
		}

		@DisplayName("(Sphere3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16.1853788749, getS().getDistanceSquared(createSphere(1, -4, 0, .1)));
		}

		@DisplayName("(Sphere3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(38.5735017233, getS().getDistanceSquared(createSphere(1, -4, 5, .1)));
		}

		@DisplayName("(Sphere3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(19.1714388244, getS().getDistanceSquared(createSphere(1, -4, -5, .1)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.3076923077, getS().getDistanceSquared(createSegment(-2, 1, 0, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(30., getS().getDistanceSquared(createSegment(-2, 1, 5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.532046125, getS().getDistanceSquared(createSegment(-2, 1, -5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.9619047619, getS().getDistanceSquared(createSegment(1, 0, 0, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16.701411275, getS().getDistanceSquared(createSegment(1, 0, 5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.0332776837, getS().getDistanceSquared(createSegment(1, 0, -5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.9142857143, getS().getDistanceSquared(createSegment(1, 1, 0, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(18.7356522899, getS().getDistanceSquared(createSegment(1, 1, 5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.25, getS().getDistanceSquared(createSegment(1, 1, -5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.5596314668, getS().getDistanceSquared(createSegment(3, 0, 0, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.8926819549, getS().getDistanceSquared(createSegment(3, 0, 5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.3014563976, getS().getDistanceSquared(createSegment(3, 0, -5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.8469508609, getS().getDistanceSquared(createSegment(1, -4, 0, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(14.4078490403, getS().getDistanceSquared(createSegment(1, -4, 5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.0146116938, getS().getDistanceSquared(createSegment(1, -4, -5, 10, 10, 10)));
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp ms;
			Path3afp p;
			ms = createMultiShape();
			p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			ms.add(p);
			ms.add(createSegment(-2, 1, 0, 10, 10, 10));
			assertEpsilonEquals(1.3027103061, getS().getDistanceSquared(ms));
		}

		@DisplayName("(MultiShape3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp ms;
			Path3afp p;
			ms = createMultiShape();
			p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			p.lineTo(4, 6, 12);
			ms.add(p);
			ms.add(createSegment(-2, 1, 0, 10, 10, 10));
			assertEpsilonEquals(1.3027103061, getS().getDistanceSquared(ms));
		}

		@DisplayName("(MultiShape3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp ms;
			Path3afp p;
			ms = createMultiShape();
			p = createPath();
			p.moveTo(-2, -10, -4);
			p.quadTo(10, 10, 10, 4, 6, 12);
			ms.add(p);
			ms.add(createSegment(-2, 1, 0, 10, 10, 10));
			assertEpsilonEquals(3.4666411003, getS().getDistanceSquared(ms));
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			assertEpsilonEquals(1.3027103061, getS().getDistanceSquared(p));
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			p.lineTo(4, 6, 12);
			assertEpsilonEquals(1.3027103061, getS().getDistanceSquared(p));
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			p.lineTo(4, 6, 12);
			p.closePath();
			assertEpsilonEquals(1.3027103061, getS().getDistanceSquared(p));
		}

		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.quadTo(10, 10, 10, 4, 6, 12);
			assertEpsilonEquals(3.4666411003, getS().getDistanceSquared(p));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(-2, 1, 0)); //(0, 0, 0)
			assertEpsilonEquals(2.236067977 * 2.236067977, result);
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(-2, 1, 5)); //(0, 0, 0)
			assertEpsilonEquals(5.477225575 * 5.477225575, result);
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(-2, 1, -5)); //(0.8952380952380953, 0.44761904761904764, -4.476190476190476)
			assertEpsilonEquals(2.993644061 * 2.993644061, result);
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(1, 0, 0)); //(0.0380952380952381, 0.01904761904761905, -0.190476190476190)
			assertEpsilonEquals(.98076743 * .98076743, result);
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(1, 0, 5)); //(5.375, 1.3125, 2.8125)
			assertEpsilonEquals(5.06442864 * 5.06442864, result);
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(1, 0, -5)); //(0.9904761904761905, 0.49523809523809526, -4.9523809523809526)
			assertEpsilonEquals(.4976133515 * .4976133515, result);
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(1, 1, 0)); //(0.05714285714285714, 0.02857142857142857, -0.2857142857142857)
			assertEpsilonEquals(1.3835771443 * 1.3835771443, result);
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(1, 1, 5)); //(5.375, 1.3125, 2.8125)
			assertEpsilonEquals(4.9013709817 * 4.9013709817, result);
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(1, 1, -5)); //(1, .5, -5)
			assertEpsilonEquals(.5 * .5, result);
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(3, 0, 0)); //(2.75, 0.875, -0.75)
			assertEpsilonEquals(1.1792476415 * 1.1792476415, result);
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(3, 0, 5)); //(5.75, 1.078125, 3.3125)
			assertEpsilonEquals(3.401839174 * 3.401839174, result);
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(3, 0, -5)); //(1.178117105233741, 0.4741442911757473, -4.419682979722328)
			assertEpsilonEquals(1.9699842474 * 1.9699842474, result);
		}

		@DisplayName("(Point3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(1, -4, 0)); //(0, 0, 0)
			assertEpsilonEquals(4.1231056256 * 4.1231056256, result);
		}

		@DisplayName("(Point3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(1, -4, 5)); //(6.699003011013108, -2.529020888903435, 2.7233273740365576)
			assertEpsilonEquals(6.3107569364 * 6.3107569364, result);
		}

		@DisplayName("(Point3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceSquared(createPoint(1, -4, -5)); //(0.9142857142857143, 0.45714285714285713, -4.571428571428571)
			assertEpsilonEquals(4.4785201638 * 4.4785201638, result);
		}
	}

	@DisplayName("getDistance")
	@Nested
	public class GetDistance {

		private InnerComputationPoint3D result;

		@BeforeEach
		public void setUp() {
			this.result = new InnerComputationPoint3D();
		}

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.8560221892, getS().getDistance(createTriangle(-2, 2, 2, -1, -5, 3, -4, 5, 6)));
		}

		@DisplayName("(Triangle3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_2(CoordinateSystem3D cs) {
			// triangle containing path start point
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Path starts at (0,0,0), triangle contains that point => distance = 0
			assertEpsilonEquals(0., getS().getDistance(createTriangle(0, 0, 0, 2, 0, 0, 0, 2, 0)));
		}

		@DisplayName("(Triangle3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_3(CoordinateSystem3D cs) {
			// triangle containing path end point
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Path ends at (7,-5,2), triangle contains that point => distance = 0
			assertEpsilonEquals(0., getS().getDistance(createTriangle(7, -5, 2, 9, -5, 2, 7, -3, 2)));
		}

		@DisplayName("(Triangle3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_4(CoordinateSystem3D cs) {
			// far triangle near start side, closest to path start
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle in plane x=-3, nearest to path should be close to/start at (0,0,0)
			// nearest triangle point to (0,0,0): (-3,0,0), distance = 3
			assertEpsilonEquals(3., getS().getDistance(createTriangle(-3, 0, 0, -3, 1, 0, -3, 0, 1)));
		}

		@DisplayName("(Triangle3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_5(CoordinateSystem3D cs) {
			// far triangle near end side, closest to path end
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle in plane x=10, nearest to path should be end (7,-5,2)
			// nearest triangle point: (10,-5,2), distance = 3
			assertEpsilonEquals(3., getS().getDistance(createTriangle(10, -5, 2, 10, -4, 2, 10, -5, 3)));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.414213562, getS().getDistance(createAlignedBoxFromPoints(-2, 1, 0, -1, 2, 1)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.196152423, getS().getDistance(createAlignedBoxFromPoints(-2, 1, 5, -1, 2, 6)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.87464282312, getS().getDistance(createAlignedBoxFromPoints(-2, 1, -5, -1, 2, -4)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.98058067569, getS().getDistance(createAlignedBoxFromPoints(1, 0, 0, 2, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.0340348908, getS().getDistance(createAlignedBoxFromPoints(1, 0, 5, 2, 1, 6)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createAlignedBoxFromPoints(1, 0, -5, 2, 1, -4)));
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.06800046816, getS().getDistance(createAlignedBoxFromPoints(1, 1, 0, 2, 2, 1)));
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.0219126358, getS().getDistance(createAlignedBoxFromPoints(1, 1, 5, 2, 2, 6)));
		}

		@DisplayName("(AlignedBox3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.5, getS().getDistance(createAlignedBoxFromPoints(1, 1, -5, 2, 2, -4)));
		}

		@DisplayName("(AlignedBox3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.59810287642, getS().getDistance(createAlignedBoxFromPoints(3, 0, 0, 4, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.43233627725, getS().getDistance(createAlignedBoxFromPoints(3, 0, 5, 4, 1, 6)));
		}

		@DisplayName("(AlignedBox3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.61854620308, getS().getDistance(createAlignedBoxFromPoints(3, 0, -5, 4, 1, -4)));
		}

		@DisplayName("(AlignedBox3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.16227766017, getS().getDistance(createAlignedBoxFromPoints(1, -4, 0, 2, -3, 1)));
		}

		@DisplayName("(AlignedBox3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.178908932, getS().getDistance(createAlignedBoxFromPoints(1, -4, 5, 2, -3, 6)));
		}

		@DisplayName("(AlignedBox3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.39327065149, getS().getDistance(createAlignedBoxFromPoints(1, -4, -5, 2, -3, -4)));
		}

		@DisplayName("(AlignedBox3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.99503719021, getS().getDistance(createAlignedBoxFromPoints(-2, 1, 0, 1, 1.1, .1)));
		}

		@DisplayName("(AlignedBox3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.99503719021, getS().getDistance(createAlignedBoxFromPoints(-2, 1, 0, 1, 3.1, .1)));
		}

		@DisplayName("(AlignedBox3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.57554534262, getS().getDistance(createAlignedBoxFromPoints(-2, 1, 0, 3, 1.1, .1)));
		}

		@DisplayName("(AlignedBox3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.5650571217, getS().getDistance(createAlignedBoxFromPoints(-2, 1, 0, 5, 1.1, .1)));
		}

		@DisplayName("(AlignedBox3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.5650571217, getS().getDistance(createAlignedBoxFromPoints(-2, 1, 0, 7, 1.1, .1)));
		}

		@DisplayName("(AlignedBox3afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.5650571217, getS().getDistance(createAlignedBoxFromPoints(-2, 1, 0, 13, 1.1, .1)));
		}

		@DisplayName("(AlignedBox3afp) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createAlignedBoxFromPoints(5, -2, 0.5, 10, 4, 5)));
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.1360679775, getS().getDistance((Shape3d) createSphere(-2, 1, 0, .1)));
		}

		@DisplayName("(Shape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.0754980867, getS().getDistance((Shape3d) createSegment(-2, 1, 0, 10, 10, 10)));
		}

		@DisplayName("(Shape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			assertEpsilonEquals(1.1413633541, getS().getDistance((Shape3d) p));
		}

		@DisplayName("(Shape3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp ms = createMultiShape();
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			ms.add(p);
			ms.add(createSegment(-2, 1, 0, 10, 10, 10));
			assertEpsilonEquals(1.1413633541, getS().getDistance((Shape3d) ms));
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.1360679775, getS().getDistance(createSphere(-2, 1, 0, .1)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.3772255751, getS().getDistance(createSphere(-2, 1, 5, .1)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.8936440607, getS().getDistance(createSphere(-2, 1, -5, .1)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.8807674352, getS().getDistance(createSphere(1, 0, 0, .1)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.964428645, getS().getDistance(createSphere(1, 0, 5, .1)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.3976133515, getS().getDistance(createSphere(1, 0, -5, .1)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.2835771443, getS().getDistance(createSphere(1, 1, 0, .1)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.8013709817, getS().getDistance(createSphere(1, 1, 5, .1)));
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.4, getS().getDistance(createSphere(1, 1, -5, .1)));
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.0792476415, getS().getDistance(createSphere(3, 0, 0, .1)));
		}

		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.301839174, getS().getDistance(createSphere(3, 0, 5, .1)));
		}

		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.8699842474, getS().getDistance(createSphere(3, 0, -5, .1)));
		}

		@DisplayName("(Sphere3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.0231056256, getS().getDistance(createSphere(1, -4, 0, .1)));
		}

		@DisplayName("(Sphere3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.2107569364, getS().getDistance(createSphere(1, -4, 5, .1)));
		}

		@DisplayName("(Sphere3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.3785201638, getS().getDistance(createSphere(1, -4, -5, .1)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.0754980867, getS().getDistance(createSegment(-2, 1, 0, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.4772255751, getS().getDistance(createSegment(-2, 1, 5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.1288602878, getS().getDistance(createSegment(-2, 1, -5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.9807674352, getS().getDistance(createSegment(1, 0, 0, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.0867360173, getS().getDistance(createSegment(1, 0, 5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.1824217194, getS().getDistance(createSegment(1, 0, -5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.3835771443, getS().getDistance(createSegment(1, 1, 0, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.328469971, getS().getDistance(createSegment(1, 1, 5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, getS().getDistance(createSegment(1, 1, -5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.7480852002, getS().getDistance(createSegment(3, 0, 0, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.9820600187, getS().getDistance(createSegment(3, 0, 5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		}

		@DisplayName("(Segment3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.5490504509, getS().getDistance(createSegment(3, 0, -5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.3590257028, getS().getDistance(createSegment(1, -4, 0, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.7957672532, getS().getDistance(createSegment(1, -4, 5, 10, 10, 10)));
		}

		@DisplayName("(Segment3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void segment_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.1208788395, getS().getDistance(createSegment(1, -4, -5, 10, 10, 10)));
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp ms;
			Path3afp p;
			ms = createMultiShape();
			p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			ms.add(p);
			ms.add(createSegment(-2, 1, 0, 10, 10, 10));
			assertEpsilonEquals(1.1413633541, getS().getDistance(ms));
		}

		@DisplayName("(MultiShape3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp ms;
			Path3afp p;
			ms = createMultiShape();
			p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			p.lineTo(4, 6, 12);
			ms.add(p);
			ms.add(createSegment(-2, 1, 0, 10, 10, 10));
			assertEpsilonEquals(1.1413633541, getS().getDistance(ms));
		}

		@DisplayName("(MultiShape3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			MultiShape3afp ms;
			Path3afp p;
			ms = createMultiShape();
			p = createPath();
			p.moveTo(-2, -10, -4);
			p.quadTo(10, 10, 10, 4, 6, 12);
			ms.add(p);
			ms.add(createSegment(-2, 1, 0, 10, 10, 10));
			assertEpsilonEquals(1.8618918068, getS().getDistance(ms));
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			assertEpsilonEquals(1.1413633541, getS().getDistance(p));
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			p.lineTo(4, 6, 12);
			assertEpsilonEquals(1.1413633541, getS().getDistance(p));
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.lineTo(10, 10, 10);
			p.lineTo(4, 6, 12);
			p.closePath();
			assertEpsilonEquals(1.1413633541, getS().getDistance(p));
		}

		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-2, -10, -4);
			p.quadTo(10, 10, 10, 4, 6, 12);
			assertEpsilonEquals(1.8618918068, getS().getDistance(p));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(-2, 1, 0)); //(0, 0, 0)
			assertEpsilonEquals(2.236067977, result);
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(-2, 1, 5)); //(0, 0, 0)
			assertEpsilonEquals(5.477225575, result);
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(-2, 1, -5)); //(0.8952380952380953, 0.44761904761904764, -4.476190476190476)
			assertEpsilonEquals(2.993644061, result);
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(1, 0, 0)); //(0.0380952380952381, 0.01904761904761905, -0.190476190476190)
			assertEpsilonEquals(.98076743, result);
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(1, 0, 5)); //(5.375, 1.3125, 2.8125)
			assertEpsilonEquals(5.06442864, result);
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(1, 0, -5)); //(0.9904761904761905, 0.49523809523809526, -4.9523809523809526)
			assertEpsilonEquals(.4976133515, result);
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(1, 1, 0)); //(0.05714285714285714, 0.02857142857142857, -0.2857142857142857)
			assertEpsilonEquals(1.3835771443, result);
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(1, 1, 5)); //(5.375, 1.3125, 2.8125)
			assertEpsilonEquals(4.9013709817, result);
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(1, 1, -5)); //(1, .5, -5)
			assertEpsilonEquals(.5, result);
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(3, 0, 0)); //(2.75, 0.875, -0.75)
			assertEpsilonEquals(1.1792476415, result);
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(3, 0, 5)); //(5.75, 1.078125, 3.3125)
			assertEpsilonEquals(3.401839174, result);
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(3, 0, -5)); //(1.178117105233741, 0.4741442911757473, -4.419682979722328)
			assertEpsilonEquals(1.9699842474, result);
		}

		@DisplayName("(Point3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(1, -4, 0)); //(0, 0, 0)
			assertEpsilonEquals(4.1231056256, result);
		}

		@DisplayName("(Point3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(1, -4, 5)); //(6.699003011013108, -2.529020888903435, 2.7233273740365576)
			assertEpsilonEquals(6.3107569364, result);
		}

		@DisplayName("(Point3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistance(createPoint(1, -4, -5)); //(0.9142857142857143, 0.45714285714285713, -4.571428571428571)
			assertEpsilonEquals(4.4785201638, result);
		}
	}

	@DisplayName("getDistanceL1")
	@Nested
	public class GetDistanceL1 {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(-2, 1, 0)); //(0, 0, 0)
			assertEpsilonEquals(2 + 1 + 0, result);
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(-2, 1, 5)); //(0, 0, 0)
			assertEpsilonEquals(2 + 1 + 5, result);
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(-2, 1, -5)); //(0.8952380952380953, 0.44761904761904764, -4.476190476190476)
			assertEpsilonEquals(2.8952380952 + 0.5523809524 + 0.5238095238, result);
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(1, 0, 0)); //(0.0380952380952381, 0.01904761904761905, -0.190476190476190)
			assertEpsilonEquals(0.9619047619 + 0.019047619 + 0.1904761905, result);
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(1, 0, 5)); //(5.375, 1.3125, 2.8125)
			assertEpsilonEquals(4.375 + 1.3125 + 2.1875, result);
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(1, 0, -5)); //(0.9904761904761905, 0.49523809523809526, -4.9523809523809526)
			assertEpsilonEquals(0.0095238095 + 0.4952380952 + 0.0476190476, result);
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(1, 1, 0)); //(0.05714285714285714, 0.02857142857142857, -0.2857142857142857)
			assertEpsilonEquals(0.9428571429 + 0.9714285714 + 0.2857142857, result);
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(1, 1, 5)); //(5.375, 1.3125, 2.8125)
			assertEpsilonEquals(4.375 + 0.3125 + 2.1875, result);
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(1, 1, -5)); //(1, .5, -5)
			assertEpsilonEquals(0 + .5 + 0, result);
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(3, 0, 0)); //(2.75, 0.875, -0.75)
			assertEpsilonEquals(.25 + .875 + .75, result);
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(3, 0, 5)); //(5.75, 1.078125, 3.3125)
			assertEpsilonEquals(2.75 + 1.078125 + 1.6875, result);
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(3, 0, -5)); //(1.178117105233741, 0.4741442911757473, -4.419682979722328)
			assertEpsilonEquals(1.8218828948 + .4741442912 + .5803170203, result);
		}

		@DisplayName("(Point3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(1, -4, 0)); //(0, 0, 0)
			assertEpsilonEquals(1 + 4 + 0, result);
		}

		@DisplayName("(Point3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(1, -4, 5)); //(6.699003011013108, -2.529020888903435, 2.7233273740365576)
			assertEpsilonEquals(5.699003011 + 1.4709791111 + 2.276672626, result);
		}

		@DisplayName("(Point3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceL1(createPoint(1, -4, -5)); //(0.9142857142857143, 0.45714285714285713, -4.571428571428571)
			assertEpsilonEquals(0.0857142857 + 4.4571428571 + 0.4285714286, result);
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
			var result = getS().getDistanceLinf(createPoint(-2, 1, 0)); //(0, 0, 0)
			assertEpsilonEquals(2, result);
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceLinf(createPoint(-2, 1, 5)); //(0, 0, 0)
			assertEpsilonEquals(5, result);
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceLinf(createPoint(-2, 1, -5)); //(0.8952380952380953, 0.44761904761904764, -4.476190476190476)
			assertEpsilonEquals(2.8952380952, result);
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceLinf(createPoint(1, 0, 0)); //(0.0380952380952381, 0.01904761904761905, -0.190476190476190)
			assertEpsilonEquals(0.9619047619, result);
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceLinf(createPoint(1, 0, 5)); //(5.375, 1.3125, 2.8125)
			assertEpsilonEquals(4.375, result);
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceLinf(createPoint(1, 0, -5)); //(0.9904761904761905, 0.49523809523809526, -4.9523809523809526)
			assertEpsilonEquals(0.4952380952, result);
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceLinf(createPoint(1, 1, 0)); //(0.05714285714285714, 0.02857142857142857, -0.2857142857142857)
			assertEpsilonEquals(0.9714285714, result);
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceLinf(createPoint(1, 1, 5)); //(5.375, 1.3125, 2.8125)
			assertEpsilonEquals(4.375, result);
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceLinf(createPoint(1, 1, -5)); //(1, .5, -5)
			assertEpsilonEquals(.5, result);
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceLinf(createPoint(3, 0, 0)); //(2.75, 0.875, -0.75)
			assertEpsilonEquals(.875, result);
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceLinf(createPoint(3, 0, 5)); //(5.75, 1.078125, 3.3125)
			assertEpsilonEquals(2.75, result);
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceLinf(createPoint(3, 0, -5)); //(1.178117105233741, 0.4741442911757473, -4.419682979722328)
			assertEpsilonEquals(1.8218828948, result);
		}

		@DisplayName("(Point3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceLinf(createPoint(1, -4, 0)); //(0, 0, 0)
			assertEpsilonEquals(4, result);
		}

		@DisplayName("(Point3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceLinf(createPoint(1, -4, 5)); //(6.699003011013108, -2.529020888903435, 2.7233273740365576)
			assertEpsilonEquals(5.699003011, result);
		}

		@DisplayName("(Point3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().getDistanceLinf(createPoint(1, -4, -5)); //(0.9142857142857143, 0.45714285714285713, -4.571428571428571)
			assertEpsilonEquals(4.4571428571, result);
		}
	}

	@DisplayName("set(Shape3D)")
	@Nested
	public class SetShape3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set((T) createPath());
			PathIterator3afp pi = getS().getPathIterator();
			assertNoElement(pi);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set((T) createPath());
			PathIterator3afp pi = getS().getPathIterator();
			assertNoElement(pi);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp path = createPath();
			path.moveTo(123.456, 456.789, 0);
			path.lineTo(789.123, 159.753, 0);
			getS().set(path);
			var pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789, 0);
			assertElement(pi, PathElementType.LINE_TO, 789.123, 159.753, 0);
			assertNoElement(pi);
		}
	}

	@DisplayName("translate")
	@Nested
	public class Translate {

		@DisplayName("(double,double,double)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPath3dTestCase#proposeArguments3Coords")
		public final void test_1(CoordinateSystem3D cs, Double dx, Double dy, Double dz) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp p2 = createPath();
			p2.moveTo(dx, dy, dz);
			p2.lineTo(1 + dx, .5 + dy, -5 + dz);
			p2.quadTo(3 + dx, 0 + dy, 2 + dz, 4 + dx, 3 + dy, -2 + dz);
			p2.curveTo(5 + dx, -1 + dy, 3 + dz, 6 + dx, 5 + dy, 5 + dz, 7 + dx, -5 + dy, 2 + dz);
			getS().translate(dx, dy, dz);
			assertTrue(getS().equals(p2));		
		}

		@DisplayName("(Vector3D)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPath3dTestCase#proposeArguments3Coords")
		public final void test_2(CoordinateSystem3D cs, Double dx, Double dy, Double dz) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(createVector(dx, dy, dz));
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, dx, dy, dz);
			assertElement(pi, PathElementType.LINE_TO, dx + 1, dy + .5, dz - 5);
			assertElement(pi, PathElementType.QUAD_TO, dx + 3, dy, dz + 2, dx + 4, dy + 3, dz - 2);
			assertElement(pi, PathElementType.CURVE_TO, dx + 5, dy - 1,  dz + 3, dx + 6, dy + 5, dz + 5, dx + 7, dy - 5, dz + 2);
			assertNoElement(pi);
		}
	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createTriangle(-2, 2, 2, -1, -5, 3, -4, 5, 6)));
		}

		@DisplayName("(Triangle3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_2(CoordinateSystem3D cs) {
			// triangle containing path start point
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Path starts at (0,0,0), triangle contains that point => distance = 0
			assertTrue(getS().intersects(createTriangle(0, 0, 0, 2, 0, 0, 0, 2, 0)));
		}

		@DisplayName("(Triangle3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_3(CoordinateSystem3D cs) {
			// triangle containing path end point
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Path ends at (7,-5,2), triangle contains that point => distance = 0
			assertTrue(getS().intersects(createTriangle(7, -5, 2, 9, -5, 2, 7, -3, 2)));
		}

		@DisplayName("(Triangle3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_4(CoordinateSystem3D cs) {
			// far triangle near start side, closest to path start
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle in plane x=-3, nearest to path should be close to/start at (0,0,0)
			// nearest triangle point to (0,0,0): (-3,0,0), distance = 3
			assertFalse(getS().intersects(createTriangle(-3, 0, 0, -3, 1, 0, -3, 0, 1)));
		}

		@DisplayName("(Triangle3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_5(CoordinateSystem3D cs) {
			// far triangle near end side, closest to path end
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle in plane x=10, nearest to path should be end (7,-5,2)
			// nearest triangle point: (10,-5,2), distance = 3
			assertFalse(getS().intersects(createTriangle(10, -5, 2, 10, -4, 2, 10, -5, 3)));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createAlignedBox(1, -2, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createAlignedBox(1.5, 1.5, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createAlignedBox(7, 3, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createAlignedBox(-4, -0.5, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createAlignedBox(1, -2, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createAlignedBox(1.5, 1.5, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createAlignedBox(7, 3, 0, 2, 1, 0)));
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createAlignedBox(-4, -0.5, 0, 2, 1, 0)));
		}

		@DisplayName("(Capsule3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(-2, -2, 0, 2)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(2, -2, 0, 2)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(2.5, -1.5, 0, 2)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(10, 0, 0, 2)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(4, 0, 0, 0.5)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			// FIX: original center/radius (2.5,1,0 / 0.5) leaves a true min-distance of
			// ~0.743 > 0.5, so it never intersected. Recentered exactly on the midpoint
			// (0.5, 0.25, -2.5) of the path's initial straight edge -> guaranteed distance 0.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(.5, .25, -2.5, 0.5)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createSphere(-2, -2, 0, 2)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSphere(2, -2, 0, 2)));
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSphere(2.5, -1.5, 0, 2)));
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createSphere(10, 0, 0, 2)));
		}

		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_11(CoordinateSystem3D cs) {
			// FIX: original center/radius (4,0,0 / 0.5) leaves a true min-distance of
			// ~1.60 > 0.5. Recentered exactly on the closing edge (7,-5,2)-(0,0,0)
			// at parameter s=0.4 -> point (4.2, -3, 1.2), guaranteed distance 0.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSphere(4.2, -3, 1.2, 0.5)));
		}

		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_12(CoordinateSystem3D cs) {
			// FIX: original center/radius (2.5,1,0 / 0.5) leaves a true min-distance of
			// ~0.743 > 0.5. Recentered exactly on the closing edge (7,-5,2)-(0,0,0)
			// at parameter s=0.7 -> point (2.1, -1.5, 0.6), guaranteed distance 0.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSphere(2.1, -1.5, 0.6, 0.5)));
		}

		@DisplayName("(Sphere3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_13(CoordinateSystem3D cs) {
			// sphere just short of touching the path
			// Same center as #13, but radius 1.0 < sqrt(26)/5 (~1.0198) -> must not intersect.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(.2, -1, 0, 1.0)));
		}

		@DisplayName("(Sphere3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_14(CoordinateSystem3D cs) {
			// sphere centered exactly on a path vertex
			// (4,3,-2) is the shared vertex between the quadTo and curveTo segments.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(4, 3, -2, 0.1)));
		}

		@DisplayName("(Sphere3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_15(CoordinateSystem3D cs) {
			// zero-radius sphere exactly on the path
			// Degenerate point-sphere located exactly on the path's start point.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(0, 0, 0, 0)));
		}

		@DisplayName("(Sphere3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_16(CoordinateSystem3D cs) {
			// zero-radius sphere off the path
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(-5, -5, -5, 0)));
		}

		@DisplayName("(Sphere3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_17(CoordinateSystem3D cs) {
			// large bounding sphere encompassing the whole path
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(3.5, -1, 0, 20)));
		}

		@DisplayName("(Sphere3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_18(CoordinateSystem3D cs) {
			// closing edge only exists once the path is closed
			// Same center/radius as #11, but WITHOUT closePath(): the closing edge
			// (7,-5,2)-(0,0,0) doesn't exist yet, and the true min-distance to the
			// remaining open-path segments is ~2.93, well above radius 0.5 -> must not intersect.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(4.2, -3, 1.2, 0.5)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-1, -1, -1, 1, 1, 1)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(1, 0, -5, 1, 1, -5)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(4, 2, -2, 4, 4, -2)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(7, -6, 2, 7, -4, 2)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(0, 0, -3, 1, 0.5, -2)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(2, 0, -1, 3.5, 1.75, -0.5)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(5, 0, 2, 5.803571429, 1.707589286, 3.517857143)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(0, 0, 0, 1, 0.5, -5)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(100, 100, 100, 101, 101, 101)));
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(0, 1, 0, 1, 1.5, -5)));
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(2, -10, 0, 2, 10, 0)));
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(0, 10, 0, 1, 10, 0)));
		}

		@DisplayName("(Segment3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(-1, -1, -1, 1, 1, 1)));
		}

		@DisplayName("(Segment3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(1, 0, -5, 1, 1, -5)));
		}

		@DisplayName("(Segment3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(4, 2, -2, 4, 4, -2)));
		}

		@DisplayName("(Segment3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(7, -6, 2, 7, -4, 2)));
		}

		@DisplayName("(Segment3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(0, 0, -3, 1, 0.5, -2)));
		}

		@DisplayName("(Segment3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(2, 0, -1, 3.5, 1.75, -0.5)));
		}

		@DisplayName("(Segment3afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(5, 0, 2, 5.803571429, 1.707589286, 3.517857143)));
		}

		@DisplayName("(Segment3afp) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(0, 0, 0, 1, 0.5, -5)));
		}

		@DisplayName("(Segment3afp) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createSegment(100, 100, 100, 101, 101, 101)));
		}

		@DisplayName("(Segment3afp) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createSegment(0, 1, 0, 1, 1.5, -5)));
		}

		@DisplayName("(Segment3afp) #25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createSegment(2, -10, 0, 2, 10, 0)));
		}

		@DisplayName("(Segment3afp) #26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_26(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createSegment(0, 10, 0, 1, 10, 0)));
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createPolyline(1, -1, 0, 4, -3, 0)));
		}

		@DisplayName("(Path3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createPolyline(1, -1, 0, 5, -3, 0)));
		}

		@DisplayName("(Path3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createPolyline(1, -1, 0, 4, 1, 0)));
		}

		@DisplayName("(Path3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_4(CoordinateSystem3D cs) {
			// FIX: crosses exactly through (0.8, 0.4, -4), the point at parameter s=0.8
			// along the path's initial straight edge (0,0,0)-(1,.5,-5).
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createPolyline(.8, .4, -5, .8, .4, -3)));
		}

		@DisplayName("(Path3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createPolyline(1, -1, 0, 4, -3, 0)));
		}

		@DisplayName("(Path3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_6(CoordinateSystem3D cs) {
			// FIX: crosses exactly through (4.9, -3.5, 1.4), the point at parameter s=0.3
			// along the closing edge (7,-5,2)-(0,0,0) added by closePath().
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createPolyline(4.9, -3.5, .4, 4.9, -3.5, 2.4)));
		}

		@DisplayName("(Path3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_7(CoordinateSystem3D cs) {
			// FIX: crosses exactly through (2.8, -2, 0.8), the point at parameter s=0.6
			// along the closing edge (7,-5,2)-(0,0,0).
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createPolyline(2.8, -2, -.2, 2.8, -2, 1.8)));
		}

		@DisplayName("(Path3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_8(CoordinateSystem3D cs) {
			// FIX: crosses exactly through (0.7, -0.5, 0.2), the point at parameter s=0.9
			// along the closing edge (7,-5,2)-(0,0,0).
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createPolyline(.7, -.5, -.8, .7, -.5, 1.2)));
		}


		@DisplayName("(Path3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_9(CoordinateSystem3D cs) {
			// crosses the initial straight edge (open path)
			// (0.5, 0.25, -3.5)-(0.5, 0.25, -1.5) passes exactly through (0.5, 0.25, -2.5),
			// the midpoint of the path's straight edge (0,0,0)-(1,.5,-5).
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createPolyline(.5, .25, -3.5, .5, .25, -1.5)));
		}

		@DisplayName("(Path3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_10(CoordinateSystem3D cs) {
			// crosses the closing edge (closed path)
			// After closePath(), the segment (7,-5,2)-(0,0,0) is added; this polyline
			// passes exactly through its midpoint (3.5, -2.5, 1).
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createPolyline(3.5, -2.5, 0, 3.5, -2.5, 2)));
		}

		@DisplayName("(Path3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_11(CoordinateSystem3D cs) {
			// shares a path vertex (open path)
			// The polyline starts exactly at the path's first point (0,0,0): touching at
			// a shared endpoint must count as intersecting.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createPolyline(0, 0, 0, -5, -5, -5)));
		}

		@DisplayName("(Path3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_12(CoordinateSystem3D cs) {
			// overlaps the initial straight edge exactly (open path)
			// The query polyline IS the path's own first segment: trivially overlapping.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createPolyline(0, 0, 0, 1, .5, -5)));
		}

		@DisplayName("(Path3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_13(CoordinateSystem3D cs) {
			// far away from the path (open path)
			// The path's bounding box is roughly x:[0,7], y:[-5,5], z:[-5,5];
			// this polyline is far outside it and must not intersect.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createPolyline(100, 100, 100, 200, 200, 200)));
		}

		@DisplayName("(Path3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_14(CoordinateSystem3D cs) {
			// far away from the path (closed path)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createPolyline(100, 100, 100, 200, 200, 200)));
		}

		@DisplayName("(PathIterator3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createPolyline(1, -1, 0, 4, -3, 0).getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("(PathIterator3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createPolyline(1, -1, 0, 5, -3, 0).getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("(PathIterator3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createPolyline(1, -1, 0, 4, 1, 0).getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("(PathIterator3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_4(CoordinateSystem3D cs) {
			// FIX: crosses exactly through (0.8, 0.4, -4), the point at parameter s=0.8
			// along the path's initial straight edge (0,0,0)-(1,.5,-5).
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createPolyline(.8, .4, -5, .8, .4, -3).getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("(PathIterator3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createPolyline(1, -1, 0, 4, -3, 0).getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("(PathIterator3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_6(CoordinateSystem3D cs) {
			// FIX: crosses exactly through (4.9, -3.5, 1.4), the point at parameter s=0.3
			// along the closing edge (7,-5,2)-(0,0,0) added by closePath().
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createPolyline(4.9, -3.5, .4, 4.9, -3.5, 2.4).getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("(PathIterator3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_7(CoordinateSystem3D cs) {
			// FIX: crosses exactly through (2.8, -2, 0.8), the point at parameter s=0.6
			// along the closing edge (7,-5,2)-(0,0,0).
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createPolyline(2.8, -2, -.2, 2.8, -2, 1.8).getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("(PathIterator3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_8(CoordinateSystem3D cs) {
			// FIX: crosses exactly through (0.7, -0.5, 0.2), the point at parameter s=0.9
			// along the closing edge (7,-5,2)-(0,0,0).
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createPolyline(.7, -.5, -.8, .7, -.5, 1.2).getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("(PathIterator3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_9(CoordinateSystem3D cs) {
			// crosses the initial straight edge (open path)
			// (0.5, 0.25, -3.5)-(0.5, 0.25, -1.5) passes exactly through (0.5, 0.25, -2.5),
			// the midpoint of the path's straight edge (0,0,0)-(1,.5,-5).
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createPolyline(.5, .25, -3.5, .5, .25, -1.5).getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("(PathIterator3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_10(CoordinateSystem3D cs) {
			// crosses the closing edge (closed path)
			// After closePath(), the segment (7,-5,2)-(0,0,0) is added; this polyline
			// passes exactly through its midpoint (3.5, -2.5, 1).
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createPolyline(3.5, -2.5, 0, 3.5, -2.5, 2).getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("(PathIterator3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_11(CoordinateSystem3D cs) {
			// shares a path vertex (open path)
			// The polyline starts exactly at the path's first point (0,0,0): touching at
			// a shared endpoint must count as intersecting.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createPolyline(0, 0, 0, -5, -5, -5).getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("(PathIterator3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_12(CoordinateSystem3D cs) {
			// overlaps the initial straight edge exactly (open path)
			// The query polyline IS the path's own first segment: trivially overlapping.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createPolyline(0, 0, 0, 1, .5, -5).getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("(PathIterator3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_13(CoordinateSystem3D cs) {
			// far away from the path (open path)
			// The path's bounding box is roughly x:[0,7], y:[-5,5], z:[-5,5];
			// this polyline is far outside it and must not intersect.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createPolyline(100, 100, 100, 200, 200, 200).getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("(PathIterator3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_14(CoordinateSystem3D cs) {
			// far away from the path (closed path)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createPolyline(100, 100, 100, 200, 200, 200).getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects((Shape3D) createSegment(-1, -1, -1, 1, 1, 1)));
		}

		@DisplayName("(Shape3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects((Shape3D)createAlignedBox(1.5, 1.5, 0, 2, 1, 0)));
		}

		@DisplayName("(Shape3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects((Shape3D) createSphere(0, 0, 0, 0)));
		}
	}

	@DisplayName("intersectsPathIteratorSegment")
	@Nested
	public class IntersectsPathIteratorSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(-1, -1, -1, 1, 1, 1)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(1, 0, -5, 1, 1, -5)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(4, 2, -2, 4, 4, -2)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(7, -6, 2, 7, -4, 2)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(0, 0, -3, 1, 0.5, -2)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(2, 0, -1, 3.5, 1.75, -0.5)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(5, 0, 2, 5.803571429, 1.707589286, 3.517857143)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(0, 0, 0, 1, 0.5, -5)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(100, 100, 100, 101, 101, 101)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(0, 1, 0, 1, 1.5, -5)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(2, -10, 0, 2, 10, 0)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(0, 10, 0, 1, 10, 0)));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(-1, -1, -1, 1, 1, 1)));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(1, 0, -5, 1, 1, -5)));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(4, 2, -2, 4, 4, -2)));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(7, -6, 2, 7, -4, 2)));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(0, 0, -3, 1, 0.5, -2)));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(2, 0, -1, 3.5, 1.75, -0.5)));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(5, 0, 2, 5.803571429, 1.707589286, 3.517857143)));
		}

		@DisplayName("#22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().intersects(createSegment(0, 0, 0, 1, 0.5, -5)));
		}

		@DisplayName("#23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createSegment(100, 100, 100, 101, 101, 101)));
		}

		@DisplayName("#24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createSegment(0, 1, 0, 1, 1.5, -5)));
		}

		@DisplayName("#25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createSegment(2, -10, 0, 2, 10, 0)));
		}

		@DisplayName("#26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_26(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().intersects(createSegment(0, 10, 0, 1, 10, 0)));
		}
	}

	@DisplayName("intersectsPathIteratorSphere")
	@Nested
	public class IntersectsPathIteratorSphere {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, -2, 0, 2,
					DISTANCE_EPSILON));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					2, -2, 0, 2,
					DISTANCE_EPSILON));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					2.5, -1.5, 0, 2,
					DISTANCE_EPSILON));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					10, 0, 0, 2,
					DISTANCE_EPSILON));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					4, 0, 0, 0.5,
					DISTANCE_EPSILON));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// FIX: original center/radius (2.5,1,0 / 0.5) leaves a true min-distance of
			// ~0.743 > 0.5, so it never intersected. Recentered exactly on the midpoint
			// (0.5, 0.25, -2.5) of the path's initial straight edge -> guaranteed distance 0.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					.5, .25, -2.5, 0.5,
					DISTANCE_EPSILON));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, -2, 0, 2,
					DISTANCE_EPSILON));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					2, -2, 0, 2,
					DISTANCE_EPSILON));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					2.5, -1.5, 0, 2,
					DISTANCE_EPSILON));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					10, 0, 0, 2,
					DISTANCE_EPSILON));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			// FIX: original center/radius (4,0,0 / 0.5) leaves a true min-distance of
			// ~1.60 > 0.5. Recentered exactly on the closing edge (7,-5,2)-(0,0,0)
			// at parameter s=0.4 -> point (4.2, -3, 1.2), guaranteed distance 0.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					4.2, -3, 1.2, 0.5,
					DISTANCE_EPSILON));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			// FIX: original center/radius (2.5,1,0 / 0.5) leaves a true min-distance of
			// ~0.743 > 0.5. Recentered exactly on the closing edge (7,-5,2)-(0,0,0)
			// at parameter s=0.7 -> point (2.1, -1.5, 0.6), guaranteed distance 0.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					2.1, -1.5, 0.6, 0.5,
					DISTANCE_EPSILON));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			// sphere just short of touching the path
			// Same center as #13, but radius 1.0 < sqrt(26)/5 (~1.0198) -> must not intersect.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					.2, -1, 0, 1.0,
					DISTANCE_EPSILON));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			// sphere centered exactly on a path vertex
			// (4,3,-2) is the shared vertex between the quadTo and curveTo segments.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					4, 3, -2, 0.1,
					DISTANCE_EPSILON));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			// zero-radius sphere exactly on the path
			// Degenerate point-sphere located exactly on the path's start point.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					0, 0, 0, 0,
					DISTANCE_EPSILON));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			// zero-radius sphere off the path
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-5, -5, -5, 0,
					DISTANCE_EPSILON));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			// large bounding sphere encompassing the whole path
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					3.5, -1, 0, 20,
					DISTANCE_EPSILON));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			// closing edge only exists once the path is closed
			// Same center/radius as #11, but WITHOUT closePath(): the closing edge
			// (7,-5,2)-(0,0,0) doesn't exist yet, and the true min-distance to the
			// remaining open-path segments is ~2.93, well above radius 0.5 -> must not intersect.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Path3afp.intersectsPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					4.2, -3, 1.2, 0.5,
					DISTANCE_EPSILON));
		}
	}

	@DisplayName("calculatesDistanceSquaredPathIteratorSegment")
	@Nested
	public class CalculatesDistanceSquaredPathIteratorSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.99047619048, Path3afp.calculatesDistanceSquaredPathIteratorSegment(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, -1, 0, 4, -3, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.99047619048, Path3afp.calculatesDistanceSquaredPathIteratorSegment(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, -1, 0, 5, -3, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.7828509991, Path3afp.calculatesDistanceSquaredPathIteratorSegment(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, -1, 0, 4, 1, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.064317376619, Path3afp.calculatesDistanceSquaredPathIteratorSegment(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					5, 2, 0, 4, 1, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0.153846153846, Path3afp.calculatesDistanceSquaredPathIteratorSegment(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, -1, 0, 4, -3, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0.153846153846, Path3afp.calculatesDistanceSquaredPathIteratorSegment(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, -1, 0, 5, -3, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0.111982082867, Path3afp.calculatesDistanceSquaredPathIteratorSegment(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					1, -1, 0, 4, 1, 0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0.064317376619, Path3afp.calculatesDistanceSquaredPathIteratorSegment(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					5, 2, 0, 4, 1, 0));
		}
	}

	@DisplayName("calculatesDistanceSquaredPathIteratorSphere")
	@Nested
	public class CalculatesDistanceSquaredPathIteratorSphere {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1., Path3afp.calculatesDistanceSquaredPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					0, -2, 0, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// path outside sphere (closed path, closing edge is the nearest feature)
			// min dist^2 from (2,-3,0) to the closing edge (7,-5,2)-(0,0,0) is exactly 173/78;
			// dist ~= 1.4893, radius = 1 => (1.4893-1)^2 ~= 0.239392881340.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0.239392881340, Path3afp.calculatesDistanceSquaredPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					2, -3, 0, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// same sphere, open path (closing edge doesn't exist yet)
			// Same center/radius as #2, but WITHOUT closePath(): the nearest feature is now
			// the initial straight edge, min dist^2 = 1364/105, dist ~= 3.6042,
			// radius = 1 => (3.6042-1)^2 ~= 6.782015553010. Demonstrates closePath()'s effect.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.782015553010, Path3afp.calculatesDistanceSquaredPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					2, -3, 0, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// sphere intersecting the path (center on the initial edge)
			// Center is exactly the midpoint of the straight edge (0,0,0)-(1,.5,-5)
			// => distance to path is 0 <= radius => squared distance to sphere is 0.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Path3afp.calculatesDistanceSquaredPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					.5, .25, -2.5, 0.3));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// sphere intersecting the closing edge (closed path only)
			// Center is exactly on the closing edge (7,-5,2)-(0,0,0) at parameter s=0.4.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0., Path3afp.calculatesDistanceSquaredPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					4.2, -3, 1.2, 0.3));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// sphere exactly tangent to the path (boundary case)
			// (0.2,-1,0) is at exact squared distance 26/25 from the initial edge;
			// radius = sqrt(26)/5 makes the sphere exactly tangent => result is 0 (<=, not <).
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Path3afp.calculatesDistanceSquaredPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					.2, -1, 0, 1.019803902718557));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// sphere just short of touching the path
			// Same center as #6, radius = 1.0 (slightly less than the tangent radius
			// sqrt(26)/5 ~= 1.0198039) => (d-r)^2 ~= 0.000392194563.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.000392194563, Path3afp.calculatesDistanceSquaredPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					.2, -1, 0, 1.0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			// zero-radius sphere (degenerate point) far from the path
			// With radius 0, the result reduces to the plain path-to-point squared distance:
			// min dist^2 from (-2,-2,0) to the initial edge is exactly 8.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8., Path3afp.calculatesDistanceSquaredPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, -2, 0, 0.));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			// zero-radius sphere exactly on the path
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Path3afp.calculatesDistanceSquaredPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					0, 0, 0, 0.));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			// large bounding sphere fully containing the whole path
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0., Path3afp.calculatesDistanceSquaredPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					3.5, -1, 0, 20.));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			// sphere centered exactly on a path vertex
			// (4,3,-2) is the shared vertex between the quadTo and curveTo segments.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Path3afp.calculatesDistanceSquaredPathIteratorSphere(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					4, 3, -2, 0.1));
		}
	}

	@DisplayName("calculatesDistanceSquaredPathIteratorPathIterator")
	@Nested
	public class CalculatesDistanceSquaredPathIteratorPathIterator {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.99047619048, Path3afp.calculatesDistanceSquaredPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					createPolyline(1, -1, 0, 4, -3, 0).getPathIterator()));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.99047619048, Path3afp.calculatesDistanceSquaredPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					createPolyline(1, -1, 0, 5, -3, 0).getPathIterator()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.7828509991, Path3afp.calculatesDistanceSquaredPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					createPolyline(1, -1, 0, 4, 1, 0).getPathIterator()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.064317376619, Path3afp.calculatesDistanceSquaredPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					createPolyline(5, 2, 0, 4, 1, 0).getPathIterator()));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0.153846153846, Path3afp.calculatesDistanceSquaredPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					createPolyline(1, -1, 0, 4, -3, 0).getPathIterator()));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0.153846153846, Path3afp.calculatesDistanceSquaredPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					createPolyline(1, -1, 0, 5, -3, 0).getPathIterator()));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0.111982082867, Path3afp.calculatesDistanceSquaredPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					createPolyline(1, -1, 0, 4, 1, 0).getPathIterator()));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0.064317376619, Path3afp.calculatesDistanceSquaredPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					createPolyline(5, 2, 0, 4, 1, 0).getPathIterator()));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			// Reversed polyline point order gives same distance.
			// The squared distance between two curves must not depend on the direction
			// in which the query polyline's points are listed.
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.99047619048, Path3afp.calculatesDistanceSquaredPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					createPolyline(4, -3, 0, 1, -1, 0).getPathIterator()));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			// Symmetry of the distance function (open path).
			// distance(A, B) must equal distance(B, A).
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.99047619048, Path3afp.calculatesDistanceSquaredPathIteratorPathIterator(
					createPolyline(1, -1, 0, 4, -3, 0).getPathIterator(),
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			// Symmetry of the distance function (closed path)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0.153846153846, Path3afp.calculatesDistanceSquaredPathIteratorPathIterator(
					createPolyline(1, -1, 0, 4, -3, 0).getPathIterator(),
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			// Null first path iterator
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () ->
			Path3afp.calculatesDistanceSquaredPathIteratorPathIterator(
					null,
					createPolyline(1, -1, 0, 4, -3, 0).getPathIterator()));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			// Null second path iterator
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () ->
			Path3afp.calculatesDistanceSquaredPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					null));
		}
	}

	@DisplayName("intersectsPathIteratorAlignedBox")
	@Nested
	public class IntersectsPathIteratorAlignedBox {

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("#1")
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Path3afp.intersectsPathIteratorAlignedBox(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-2, 1, 0, -1, 2, 1,
					DISTANCE_EPSILON));
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("#2 - box containing the start point (0,0,0)")
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Path3afp.intersectsPathIteratorAlignedBox(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-.1, -.1, -.1, .1, .1, .1,
					DISTANCE_EPSILON));
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("#3 - box around first line interior point")
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// First segment: (0,0,0) -> (1,.5,-5). Midpoint is (0.5,0.25,-2.5)
			assertTrue(Path3afp.intersectsPathIteratorAlignedBox(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					.45, .20, -2.6, .55, .30, -2.4,
					DISTANCE_EPSILON));
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("#4 - box around line endpoint / quad start (1,.5,-5)")
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Path3afp.intersectsPathIteratorAlignedBox(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					.9, .4, -5.1, 1.1, .6, -4.9,
					DISTANCE_EPSILON));
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("#5 - box around quad endpoint / cubic start (4,3,-2)")
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Path3afp.intersectsPathIteratorAlignedBox(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					3.9, 2.9, -2.1, 4.1, 3.1, -1.9,
					DISTANCE_EPSILON));
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("#6 - box around final endpoint (7,-5,2)")
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Path3afp.intersectsPathIteratorAlignedBox(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					6.9, -5.1, 1.9, 7.1, -4.9, 2.1,
					DISTANCE_EPSILON));
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("#7 - far away positive octant (no intersection)")
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Path3afp.intersectsPathIteratorAlignedBox(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					20, 20, 20, 30, 30, 30,
					DISTANCE_EPSILON));
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("#8 - far away negative octant (no intersection)")
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Path3afp.intersectsPathIteratorAlignedBox(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-30, -30, -30, -20, -20, -20,
					DISTANCE_EPSILON));
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("#9 - box enclosing whole drawable range")
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Path3afp.intersectsPathIteratorAlignedBox(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					-1, -6, -6, 8, 4, 4,
					DISTANCE_EPSILON));
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("#10 - thin slab crossing expected cubic region")
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Cubic part spans x in [4,7], with z reaching positive values around ~2..3 along approximation.
			assertTrue(Path3afp.intersectsPathIteratorAlignedBox(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					5.5, -2.0, 1.0, 6.5, 1.5, 3.5,
					DISTANCE_EPSILON));
		}

	}

	@DisplayName("p += Vector3D")
	@Nested
	public class OperatorAddVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPath3dTestCase#proposeArguments3Coords")
		public final void test_1(CoordinateSystem3D cs, Double dx, Double dy, Double dz) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_add(createVector(dx, dy, dz));
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0 + dx, 0 + dy, 0 + dz);
			assertElement(pi, PathElementType.LINE_TO, 1 + dx, .5 + dy, -5 + dz);
			assertElement(pi, PathElementType.QUAD_TO, 3 + dx, 0 + dy, 2 + dz, 4 + dx, 3 + dy, -2 + dz);
			assertElement(pi, PathElementType.CURVE_TO, 5 + dx, -1 + dy, 3 + dz, 6 + dx, 5 + dy, 5 + dz, 7 + dx, -5 + dy, 2 + dz);
			assertNoElement(pi);
		}
	}

	@DisplayName("p + Vector3D")
	@Nested
	public class OperatorPlusVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPath3dTestCase#proposeArguments3Coords")
		public final void test_1(CoordinateSystem3D cs, Double dx, Double dy, Double dz) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			T shape = getS().operator_plus(createVector(dx, dy, dz));
			PathIterator3afp pi = shape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0 + dx, 0 + dy, 0 + dz);
			assertElement(pi, PathElementType.LINE_TO, 1 + dx, .5 + dy, -5 + dz);
			assertElement(pi, PathElementType.QUAD_TO, 3 + dx, 0 + dy, 2 + dz, 4 + dx, 3 + dy, -2 + dz);
			assertElement(pi, PathElementType.CURVE_TO, 5 + dx, -1 + dy, 3 + dz, 6 + dx, 5 + dy, 5 + dz, 7 + dx, -5 + dy, 2 + dz);
			assertNoElement(pi);
		}
	}

	@DisplayName("p -= Vector3D")
	@Nested
	public class OperatorRemoveVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPath3dTestCase#proposeArguments3Coords")
		public final void test_1(CoordinateSystem3D cs, Double dx, Double dy, Double dz) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().operator_remove(createVector(dx, dy, dz));
			PathIterator3afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0 - dx, 0 - dy, 0 - dz);
			assertElement(pi, PathElementType.LINE_TO, 1 - dx, .5 - dy, -5 - dz);
			assertElement(pi, PathElementType.QUAD_TO, 3 - dx, 0 - dy, 2 - dz, 4 - dx, 3 - dy, -2 - dz);
			assertElement(pi, PathElementType.CURVE_TO, 5 - dx, -1 - dy, 3 - dz, 6 - dx, 5 - dy, 5 - dz, 7 - dx, -5 - dy, 2 - dz);
			assertNoElement(pi);
		}
	}

	@DisplayName("p - Vector3D")
	@Nested
	public class OperatorMinusVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPath3dTestCase#proposeArguments3Coords")
		public final void test_1(CoordinateSystem3D cs, Double dx, Double dy, Double dz) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			T shape = getS().operator_minus(createVector(dx, dy, dz));
			PathIterator3afp pi = shape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0 - dx, 0 - dy, 0 - dz);
			assertElement(pi, PathElementType.LINE_TO, 1 - dx, .5 - dy, -5 - dz);
			assertElement(pi, PathElementType.QUAD_TO, 3 - dx, 0 - dy, 2 - dz, 4 - dx, 3 - dy, -2 - dz);
			assertElement(pi, PathElementType.CURVE_TO, 5 - dx, -1 - dy, 3 - dz, 6 - dx, 5 - dy, 5 - dz, 7 - dx, -5 - dy, 2 - dz);
			assertNoElement(pi);
		}
	}

	@DisplayName("p && Point3D")
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
			assertTrue(getS().operator_and(createPoint(1, .5, -5)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Control point
			assertFalse(getS().operator_and(createPoint(3, 0, 2)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(4, 3, -2)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Control point
			assertFalse(getS().operator_and(createPoint(5, -1, 3)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Control point
			assertFalse(getS().operator_and(createPoint(6, 5, 5)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(7, -5, 2)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(1.484375, 0.4296875, -3.421875)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(1.9375, 0.46875, -2.1875)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(2.359375, 0.6171875, -1.296875)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(2.75, 0.875, -0.75)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(3.109375, 1.2421875, -0.546875)));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(3.4375, 1.71875, -0.6875)));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(3.734375, 2.3046875, -1.171875)));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(4.0, 3.0, -2.0)));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(4.25, 2.15625, -0.796875)));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(4.625, 1.546875, 0.734375)));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(5.0, 1.3671875, 1.9453125)));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(5.375, 1.3125, 2.8125)));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(5.75, 1.078125, 3.3125)));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(6.125, 0.359375, 3.421875)));
		}

		@DisplayName("#22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(6.5, -1.1484375, 3.1171875)));
		}

		@DisplayName("#23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(6.875, -3.75, 2.375)));
		}

		@DisplayName("#24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(7.0, -5.0, 2.0)));
		}

		@DisplayName("#25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(-5, 1, 0)));
		}

		@DisplayName("#26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_26(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(3, 6, 0)));
		}

		@DisplayName("#27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_27(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(3, -10, 0)));
		}

		@DisplayName("#28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_28(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(11, 1, 0)));
		}

		@DisplayName("#29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_29(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(4, 1, 0)));
		}

		@DisplayName("#30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_30(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(4, 3, 0)));
		}

		@DisplayName("#31")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_31(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(0, 0, 0)));
		}

		@DisplayName("#32")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_32(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(1, .5, -5)));
		}

		@DisplayName("#33")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_33(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			// Control point
			assertFalse(getS().operator_and(createPoint(3, 0, 2)));
		}

		@DisplayName("#34")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_34(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(4, 3, -2)));
		}

		@DisplayName("#35")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_35(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			// Control point
			assertFalse(getS().operator_and(createPoint(5, -1, 3)));
		}

		@DisplayName("#36")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_36(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			// Control point
			assertFalse(getS().operator_and(createPoint(6, 5, 5)));
		}

		@DisplayName("#37")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_37(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(7, -5, 2)));
		}

		@DisplayName("#38")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_38(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(1.484375, 0.4296875, -3.421875)));
		}

		@DisplayName("#39")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_39(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(1.9375, 0.46875, -2.1875)));
		}

		@DisplayName("#40")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_40(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(2.359375, 0.6171875, -1.296875)));
		}

		@DisplayName("#41")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_41(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(2.75, 0.875, -0.75)));
		}

		@DisplayName("#42")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_42(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(3.109375, 1.2421875, -0.546875)));
		}

		@DisplayName("#43")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_43(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(3.4375, 1.71875, -0.6875)));
		}

		@DisplayName("#44")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_44(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(3.734375, 2.3046875, -1.171875)));
		}

		@DisplayName("#45")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_45(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(4.0, 3.0, -2.0)));
		}

		@DisplayName("#46")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_46(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(4.25, 2.15625, -0.796875)));
		}

		@DisplayName("#47")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_47(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(4.625, 1.546875, 0.734375)));
		}

		@DisplayName("#48")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_48(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(5.0, 1.3671875, 1.9453125)));
		}

		@DisplayName("#49")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_49(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(5.375, 1.3125, 2.8125)));
		}

		@DisplayName("#50")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_50(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(5.75, 1.078125, 3.3125)));
		}

		@DisplayName("#51")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_51(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(6.125, 0.359375, 3.421875)));
		}

		@DisplayName("#52")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_52(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(6.5, -1.1484375, 3.1171875)));
		}

		@DisplayName("#53")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_53(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(6.875, -3.75, 2.375)));
		}

		@DisplayName("#54")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_54(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(7.0, -5.0, 2.0)));
		}

		@DisplayName("#55")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_55(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().operator_and(createPoint(-5, 1, 0)));
		}

		@DisplayName("#56")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_56(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().operator_and(createPoint(3, 6, 0)));
		}

		@DisplayName("#57")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_57(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().operator_and(createPoint(3, -10, 0)));
		}

		@DisplayName("#58")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_58(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().operator_and(createPoint(11, 1, 0)));
		}

		@DisplayName("#59")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_59(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().operator_and(createPoint(4, 1, 0)));
		}

		@DisplayName("#60")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_60(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().operator_and(createPoint(4, 3, 0)));
		}
	}

	@DisplayName("p && Shape3D")
	@Nested
	public class OperatorAndShape3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createSegment(5, 0, 2, 5.803571429, 1.707589286, 3.517857143)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createAlignedBox(1.5, 1.5, 0, 2, 1, 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createSphere(1.5, 1.5, 0, 2)));
		}
	}

	@DisplayName("p .. Point3D")
	@Nested
	public class OperatorUpToPoint3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(-2, 1, 0)); //(0, 0, 0)
			assertEpsilonEquals(2.236067977, result);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(-2, 1, 5)); //(0, 0, 0)
			assertEpsilonEquals(5.477225575, result);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(-2, 1, -5)); //(0.8952380952380953, 0.44761904761904764, -4.476190476190476)
			assertEpsilonEquals(2.993644061, result);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(1, 0, 0)); //(0.0380952380952381, 0.01904761904761905, -0.190476190476190)
			assertEpsilonEquals(.98076743, result);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(1, 0, 5)); //(5.375, 1.3125, 2.8125)
			assertEpsilonEquals(5.06442864, result);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(1, 0, -5)); //(0.9904761904761905, 0.49523809523809526, -4.9523809523809526)
			assertEpsilonEquals(.4976133515, result);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(1, 1, 0)); //(0.05714285714285714, 0.02857142857142857, -0.2857142857142857)
			assertEpsilonEquals(1.3835771443, result);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(1, 1, 5)); //(5.375, 1.3125, 2.8125)
			assertEpsilonEquals(4.9013709817, result);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(1, 1, -5)); //(1, .5, -5)
			assertEpsilonEquals(.5, result);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(3, 0, 0)); //(2.75, 0.875, -0.75)
			assertEpsilonEquals(1.1792476415, result);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(3, 0, 5)); //(5.75, 1.078125, 3.3125)
			assertEpsilonEquals(3.401839174, result);
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(3, 0, -5)); //(1.178117105233741, 0.4741442911757473, -4.419682979722328)
			assertEpsilonEquals(1.9699842474, result);
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(1, -4, 0)); //(0, 0, 0)
			assertEpsilonEquals(4.1231056256, result);
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(1, -4, 5)); //(6.699003011013108, -2.529020888903435, 2.7233273740365576)
			assertEpsilonEquals(6.3107569364, result);
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var result = getS().operator_upTo(createPoint(1, -4, -5)); //(0.9142857142857143, 0.45714285714285713, -4.571428571428571)
			assertEpsilonEquals(4.4785201638, result);
		}
	}

	@DisplayName("isCurved")
	@Nested
	public class IsCurved {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().isCurved());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertFalse(getS().isCurved());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			assertFalse(getS().isCurved());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			assertFalse(getS().isCurved());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			assertFalse(getS().isCurved());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().closePath();
			assertFalse(getS().isCurved());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(3, 4, 0);
			getS().closePath();
			getS().curveTo(7, 8, 0, 9, 10, 0, 11, 12, 0);
			assertTrue(getS().isCurved());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().quadTo(7, 8, 0, 9, 10, 0);
			assertTrue(getS().isCurved());
		}
	}

	@DisplayName("isMultiParts")
	@Nested
	public class IsMultiParts {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().closePath();
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().curveTo(7, 8, 0, 9, 10, 0, 11, 12, 0);
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().quadTo(7, 8, 0, 9, 10, 0);
			assertFalse(getS().isMultiParts());
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().lineTo(3, 4, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().quadTo(7, 8, 0, 9, 10, 0);
			assertTrue(getS().isMultiParts());
		}
	}

	@DisplayName("isPolygon")
	@Nested
	public class IsPolygon {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().isPolygon());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertFalse(getS().isPolygon());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			assertFalse(getS().isPolygon());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			assertFalse(getS().isPolygon());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			assertFalse(getS().isPolygon());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().closePath();
			assertTrue(getS().isPolygon());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().curveTo(7, 8, 0, 9, 10, 0, 11, 12, 0);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().curveTo(7, 8, 0, 9, 10, 0, 11, 12, 0);
			getS().closePath();
			assertTrue(getS().isPolygon());
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().quadTo(7, 8, 0, 9, 10, 0);
			assertFalse(getS().isPolygon());
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().quadTo(7, 8, 0, 9, 10, 0);
			getS().closePath();
			assertTrue(getS().isPolygon());
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().quadTo(7, 8, 0, 9, 10, 0);
			getS().closePath();
			getS().moveTo(1, 2, 0);
			assertFalse(getS().isPolygon());
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().quadTo(7, 8, 0, 9, 10, 0);
			getS().closePath();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			assertFalse(getS().isPolygon());
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().quadTo(7, 8, 0, 9, 10, 0);
			getS().closePath();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().closePath();
			assertFalse(getS().isPolygon());
		}
	}

	@DisplayName("isPolyline")
	@Nested
	public class IsPolyline {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().isPolyline());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertFalse(getS().isPolyline());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			assertFalse(getS().isPolyline());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			assertFalse(getS().isPolyline());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			assertTrue(getS().isPolyline());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().closePath();
			assertFalse(getS().isPolyline());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().curveTo(7, 8, 0, 9, 10, 0, 11, 12, 0);
			assertFalse(getS().isPolyline());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().curveTo(7, 8, 0, 9, 10, 0, 11, 12, 0);
			getS().closePath();
			assertFalse(getS().isPolyline());
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().quadTo(7, 8, 0, 9, 10, 0);
			assertFalse(getS().isPolyline());
			getS().closePath();
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().moveTo(3, 4, 0);
			getS().lineTo(5, 6, 0);
			getS().quadTo(7, 8, 0, 9, 10, 0);
			assertFalse(getS().isPolyline());
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2, 0);
			getS().lineTo(3, 4, 0);
			getS().moveTo(5, 6, 0);
			getS().lineTo(7, 8, 0);
			assertFalse(getS().isPolyline());
		}
	}

	@DisplayName("getCurrentX")
	@Nested
	public class GetCurrentX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7, getS().getCurrentX());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().lineTo(154, 485, 0);
			assertEpsilonEquals(154, getS().getCurrentX());
		}
	}

	@DisplayName("getCurrentY")
	@Nested
	public class GetCurrentY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-5, getS().getCurrentY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().lineTo(154, 485, 0);
			assertEpsilonEquals(485, getS().getCurrentY());
		}
	}

	@DisplayName("getCurrentZ")
	@Nested
	public class GetCurrentZ {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, getS().getCurrentZ());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().lineTo(154, 485, 10);
			assertEpsilonEquals(10, getS().getCurrentZ());
		}
	}

	@DisplayName("getCurrentPoint")
	@Nested
	public class GetCurrentPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7, -5, 2, getS().getCurrentPoint());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().lineTo(154, 485, 0);
			assertFpPointEquals(154, 485, 0, getS().getCurrentPoint());
		}
	}

	@DisplayName("toBoundingBoxWithCtrlPoints")
	@Nested
	public class ToBoundingBoxWithCtrlPoints {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var box = getS().toBoundingBoxWithCtrlPoints();
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(-5, box.getMinY());
			assertEpsilonEquals(-5, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(5, box.getMaxY());
			assertEpsilonEquals(5, box.getMaxZ());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var box = getS().getGeomFactory().newBox();
			getS().toBoundingBoxWithCtrlPoints(box);
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(-5, box.getMinY());
			assertEpsilonEquals(-5, box.getMinZ());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(5, box.getMaxY());
			assertEpsilonEquals(5, box.getMaxZ());
		}
	}

	@DisplayName("toCollection")
	@Nested
	public class ToCollection {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Collection<? extends Point3D> collection = getS().toCollection();
			assertEquals(7, collection.size());
			Iterator<? extends Point3D> iterator = collection.iterator();
			assertEpsilonEquals(createPoint(0, 0, 0), iterator.next());
			assertEpsilonEquals(createPoint(1, .5, -5), iterator.next());
			assertEpsilonEquals(createPoint(3, 0, 2), iterator.next());
			assertEpsilonEquals(createPoint(4, 3, -2), iterator.next());
			assertEpsilonEquals(createPoint(5, -1, 3), iterator.next());
			assertEpsilonEquals(createPoint(6, 5, 5), iterator.next());
			assertEpsilonEquals(createPoint(7, -5, 2), iterator.next());
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("toIntArray")
	@Nested
	public class ToIntArray {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var actual = getS().toIntArray();
			assertArrayEquals(new int[] {
					0, 0, 0,
					1, 0, -5,
					3, 0, 2,
					4, 3, -2,
					5, -1, 3,
					6, 5, 5,
					7, -5, 2}, actual);
	    }

		@DisplayName("(Transform3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void transform_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var actual = getS().toIntArray(null);
			assertArrayEquals(new int[] {
					0, 0, 0,
					1, 0, -5,
					3, 0, 2,
					4, 3, -2,
					5, -1, 3,
					6, 5, 5,
					7, -5, 2}, actual);
	    }

		@DisplayName("(Transform3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void transform_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.setIdentity();
			var actual = getS().toIntArray(tr);
			assertArrayEquals(new int[] {
					0, 0, 0,
					1, 0, -5,
					3, 0, 2,
					4, 3, -2,
					5, -1, 3,
					6, 5, 5,
					7, -5, 2}, actual);
	    }

		@DisplayName("(Transform3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void transform_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.setIdentity();
			tr.translate(1, -2, 3);
			var actual = getS().toIntArray(tr);
			assertArrayEquals(new int[] {
					1, -2, 3,
					2, -1, -2,
					4, -2, 5,
					5, 1, 1,
					6, -3, 6,
					7, 3, 8,
					8, -7, 5}, actual);
	    }
	}

	@DisplayName("toFloatArray")
	@Nested
	public class ToFloatArray {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var actual = getS().toFloatArray();
			assertEpsilonArrayEquals(new float[] {
					0, 0, 0,
					1, .5f, -5,
					3, 0, 2,
					4, 3, -2,
					5, -1, 3,
					6, 5, 5,
					7, -5, 2}, actual);
	    }

		@DisplayName("(Transform3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void transform_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var actual = getS().toFloatArray(null);
			assertEpsilonArrayEquals(new float[] {
					0, 0, 0,
					1, .5f, -5,
					3, 0, 2,
					4, 3, -2,
					5, -1, 3,
					6, 5, 5,
					7, -5, 2}, actual);
	    }

		@DisplayName("(Transform3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void transform_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.setIdentity();
			var actual = getS().toFloatArray(tr);
			assertEpsilonArrayEquals(new float[] {
					0, 0, 0,
					1, .5f, -5,
					3, 0, 2,
					4, 3, -2,
					5, -1, 3,
					6, 5, 5,
					7, -5, 2}, actual);
	    }

		@DisplayName("(Transform3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void transform_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.setIdentity();
			tr.translate(1, -2, 3);
			var actual = getS().toFloatArray(tr);
			assertEpsilonArrayEquals(new float[] {
					1, -2, 3,
					2, -1.5f, -2,
					4, -2, 5,
					5, 1, 1,
					6, -3, 6,
					7, 3, 8,
					8, -7, 5}, actual);
	    }
	}

	@DisplayName("toDoubleArray")
	@Nested
	public class ToDoubleArray {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var actual = getS().toDoubleArray();
			assertEpsilonArrayEquals(new double[] {
					0, 0, 0,
					1, .5, -5,
					3, 0, 2,
					4, 3, -2,
					5, -1, 3,
					6, 5, 5,
					7, -5, 2}, actual);
	    }

		@DisplayName("(Transform3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void transform_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var actual = getS().toDoubleArray(null);
			assertEpsilonArrayEquals(new double[] {
					0, 0, 0,
					1, .5, -5,
					3, 0, 2,
					4, 3, -2,
					5, -1, 3,
					6, 5, 5,
					7, -5, 2}, actual);
	    }

		@DisplayName("(Transform3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void transform_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.setIdentity();
			var actual = getS().toDoubleArray(tr);
			assertEpsilonArrayEquals(new double[] {
					0, 0, 0,
					1, .5, -5,
					3, 0, 2,
					4, 3, -2,
					5, -1, 3,
					6, 5, 5,
					7, -5, 2}, actual);
	    }

		@DisplayName("(Transform3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void transform_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.setIdentity();
			tr.translate(1, -2, 3);
			var actual = getS().toDoubleArray(tr);
			assertEpsilonArrayEquals(new double[] {
					1, -2, 3,
					2, -1.5, -2,
					4, -2, 5,
					5, 1, 1,
					6, -3, 6,
					7, 3, 8,
					8, -7, 5}, actual);
	    }
	}

	@DisplayName("toPointArray")
	@Nested
	public class ToPointArray {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var actual = getS().toPointArray();
			assertEquals(7, actual.length);
			assertEpsilonEquals(createPoint(0, 0, 0), actual[0]);
			assertEpsilonEquals(createPoint(1,  .5, -5), actual[1]);
			assertEpsilonEquals(createPoint(3, 0, 2), actual[2]);
			assertEpsilonEquals(createPoint(4, 3, -2), actual[3]);
			assertEpsilonEquals(createPoint(5, -1, 3), actual[4]);
			assertEpsilonEquals(createPoint(6, 5, 5), actual[5]);
			assertEpsilonEquals(createPoint(7, -5, 2), actual[6]);
	    }

		@DisplayName("(Transform3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void transform_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var actual = getS().toPointArray(null);
			assertEquals(7, actual.length);
			assertEpsilonEquals(createPoint(0, 0, 0), actual[0]);
			assertEpsilonEquals(createPoint(1,  .5, -5), actual[1]);
			assertEpsilonEquals(createPoint(3, 0, 2), actual[2]);
			assertEpsilonEquals(createPoint(4, 3, -2), actual[3]);
			assertEpsilonEquals(createPoint(5, -1, 3), actual[4]);
			assertEpsilonEquals(createPoint(6, 5, 5), actual[5]);
			assertEpsilonEquals(createPoint(7, -5, 2), actual[6]);
	    }

		@DisplayName("(Transform3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void transform_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.setIdentity();
			var actual = getS().toPointArray(tr);
			assertEquals(7, actual.length);
			assertEpsilonEquals(createPoint(0, 0, 0), actual[0]);
			assertEpsilonEquals(createPoint(1,  .5, -5), actual[1]);
			assertEpsilonEquals(createPoint(3, 0, 2), actual[2]);
			assertEpsilonEquals(createPoint(4, 3, -2), actual[3]);
			assertEpsilonEquals(createPoint(5, -1, 3), actual[4]);
			assertEpsilonEquals(createPoint(6, 5, 5), actual[5]);
			assertEpsilonEquals(createPoint(7, -5, 2), actual[6]);
	    }

		@DisplayName("(Transform3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void transform_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var tr = new Transform3D();
			tr.setIdentity();
			tr.translate(1, -2, 3);
			var actual = getS().toPointArray(tr);
			assertEquals(7, actual.length);
			assertEpsilonEquals(createPoint(1, -2, 3), actual[0]);
			assertEpsilonEquals(createPoint(2, -1.5, -2), actual[1]);
			assertEpsilonEquals(createPoint(4, -2, 5), actual[2]);
			assertEpsilonEquals(createPoint(5, 1, 1), actual[3]);
			assertEpsilonEquals(createPoint(6, -3, 6), actual[4]);
			assertEpsilonEquals(createPoint(7, 3, 8), actual[5]);
			assertEpsilonEquals(createPoint(8, -7, 5), actual[6]);
	    }
	}

}
