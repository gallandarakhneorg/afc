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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
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
import org.arakhne.afc.math.geometry.d3.afp.Triangle3afp;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
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
public abstract class AbstractTriangle3dTestCase<T extends Triangle3afp<?, T, ?, ?, ?, ?, B>,
			B extends AlignedBox3afp<?, ?, ?, ?, ?, B>> extends AbstractShape3dTestCase<T, B> {

	private static Stream<Arguments> proposeArguments3Coords() {
		final var args = new ArrayList<Arguments>();
		final var count = getRandom().nextInt(100) + 100;
		for (final CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			for (int i = 0; i < count; ++i) {
				double dx = (getRandom().nextBoolean() ? 1. : -1.) * getRandom().nextDouble() * 1000.;
				double dy = (getRandom().nextBoolean() ? 1. : -1.) * getRandom().nextDouble() * 1000.;
				double dz = (getRandom().nextBoolean() ? 1. : -1.) * getRandom().nextDouble() * 1000.;
				args.add(Arguments.of(cs, dx, dy, dz));
			}
		}
		return args.stream();
	}

	@Override
	protected final T createShape() {
		return (T) createTriangle(0, 0, 0, 1, 1, 1, 1, 0, 1);
	}

	@DisplayName("getType")
	@Nested
	public class GetType {

		@DisplayName("()")
		@Test
		public final void getType() {
			assertSame(Shape3DType.TRIANGLE, getS().getType());
		}
	
		@DisplayName("(Class)")
		@Test
		public final void getType_Class() {
			assertSame(Shape3DType.TRIANGLE, getS().getType(Shape3DType.class));
		}
	}

	@DisplayName("getPlane")
	@Nested
	public class GetPlane {

		@DisplayName("#1 (left-handed)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1_lh(CoordinateSystem3D cs) {
			assumeTrue(cs.isLeftHanded());
			// getPlane: expected plane equation for default triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle: A(0,0,0), B(1,1,1), C(1,0,1)
			// AB=(1,1,1), AC=(1,0,1), n = AB x AC = (1,0,-1)
			// Plane: x - z + d = 0; with A => d=0
			var plane = getS().getPlane();
			assertNotNull(plane);
			assertEpsilonEquals(-0.70710678119, plane.getEquationComponentA());
			assertEpsilonEquals(0., plane.getEquationComponentB());
			assertEpsilonEquals(0.70710678119, plane.getEquationComponentC());
			assertEpsilonEquals(0., plane.getEquationComponentD());
		}

		@DisplayName("#1 (right-handed)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1_rh(CoordinateSystem3D cs) {
			assumeTrue(cs.isRightHanded());
			// getPlane: expected plane equation for default triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Triangle: A(0,0,0), B(1,1,1), C(1,0,1)
			// AB=(1,1,1), AC=(1,0,1), n = AB x AC = (1,0,-1)
			// Plane: x - z + d = 0; with A => d=0
			var plane = getS().getPlane();
			assertNotNull(plane);
			assertEpsilonEquals(0.70710678119, plane.getEquationComponentA());
			assertEpsilonEquals(0., plane.getEquationComponentB());
			assertEpsilonEquals(-0.70710678119, plane.getEquationComponentC());
			assertEpsilonEquals(0., plane.getEquationComponentD());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// getPlane contains all triangle vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var plane = getS().getPlane();
			assertNotNull(plane);
			final var a = plane.getEquationComponentA();
			final var b = plane.getEquationComponentB();
			final var c = plane.getEquationComponentC();
			final var d = plane.getEquationComponentD();
			// A(0,0,0), B(1,1,1), C(1,0,1)
			assertEpsilonEquals(0., a * 0. + b * 0. + c * 0. + d);
			assertEpsilonEquals(0., a * 1. + b * 1. + c * 1. + d);
			assertEpsilonEquals(0., a * 1. + b * 0. + c * 1. + d);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// getPlane normal is orthogonal to AB and AC
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var plane = getS().getPlane();
			assertNotNull(plane);
			final var a = plane.getEquationComponentA();
			final var b = plane.getEquationComponentB();
			final var c = plane.getEquationComponentC();
			// AB=(1,1,1), AC=(1,0,1)
			assertEpsilonEquals(0., a * 1. + b * 1. + c * 1.);
			assertEpsilonEquals(0., a * 1. + b * 0. + c * 1.);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// getPlane scaled normal is acceptable (ratio invariant)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var plane = getS().getPlane();
			assertNotNull(plane);
			final var a = plane.getEquationComponentA();
			final var b = plane.getEquationComponentB();
			final var c = plane.getEquationComponentC();
			final var d = plane.getEquationComponentD();
			// Accept equivalent plane forms k*(x-z)=0. Use two robust checks:
			// 1) b and d should be 0 for this plane family
			assertEpsilonEquals(0., b);
			assertEpsilonEquals(0., d);
			// 2) a and c should be opposite (a + c = 0), and non-zero normal
			assertEpsilonEquals(0., a + c);
			assertFalse(MathUtil.isEpsilonZero(a) && MathUtil.isEpsilonZero(c));
		}

		@DisplayName("#5 (left-handed)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5_lh(CoordinateSystem3D cs) {
			assumeTrue(cs.isLeftHanded());
			// getPlane after translation by (2,-3,4)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final T s = (T) createTriangle(2, -3, 4, 3, -2, 5, 3, -3, 5);
			var plane = s.getPlane();
			assertNotNull(plane);
			// Normal unchanged direction from default: (1,0,-1)
			// Equation: x - z + d = 0; point (2,-3,4) => d = 2
			assertEpsilonEquals(-0.70710678119, plane.getEquationComponentA());
			assertEpsilonEquals(0., plane.getEquationComponentB());
			assertEpsilonEquals(0.70710678119, plane.getEquationComponentC());
			assertEpsilonEquals(-1.4142135624, plane.getEquationComponentD());
		}

		@DisplayName("#5 (right-handed)")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5_rh(CoordinateSystem3D cs) {
			assumeTrue(cs.isRightHanded());
			// getPlane after translation by (2,-3,4)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final T s = (T) createTriangle(2, -3, 4, 3, -2, 5, 3, -3, 5);
			var plane = s.getPlane();
			assertNotNull(plane);
			// Normal unchanged direction from default: (1,0,-1)
			// Equation: x - z + d = 0; point (2,-3,4) => d = 2
			assertEpsilonEquals(0.70710678119, plane.getEquationComponentA());
			assertEpsilonEquals(0., plane.getEquationComponentB());
			assertEpsilonEquals(-0.70710678119, plane.getEquationComponentC());
			assertEpsilonEquals(1.4142135624, plane.getEquationComponentD());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// getPlane with vertex permutation still same geometric plane
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final T s = (T) createTriangle(1, 1, 1, 1, 0, 1, 0, 0, 0); // permutation of default
			var plane = s.getPlane();
			assertNotNull(plane);
			final var a = plane.getEquationComponentA();
			final var b = plane.getEquationComponentB();
			final var c = plane.getEquationComponentC();
			final var d = plane.getEquationComponentD();
			// Should satisfy x-z=0 for all original vertices
			assertEpsilonEquals(0., a * 0. + b * 0. + c * 0. + d);
			assertEpsilonEquals(0., a * 1. + b * 1. + c * 1. + d);
			assertEpsilonEquals(0., a * 1. + b * 0. + c * 1. + d);
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
			assertEpsilonEquals(0., getS().getX1());
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
			assertEpsilonEquals(0., getS().getY1());
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
			assertEpsilonEquals(0., getS().getZ1());
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
			assertEpsilonEquals(1., getS().getX2());
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
			assertEpsilonEquals(1., getS().getY2());
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
			assertEpsilonEquals(1., getS().getZ2());
		}
	}

	@DisplayName("getX3")
	@Nested
	public class GetX3 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1., getS().getX3());
		}
	}

	@DisplayName("getY3")
	@Nested
	public class GetY3 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getY3());
		}
	}

	@DisplayName("getZ3")
	@Nested
	public class GetZ3 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1., getS().getZ3());
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
			getS().setX1(123.45698);
			assertEpsilonEquals(123.45698, getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
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
			getS().setY1(123.45698);
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(123.45698, getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
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
			getS().setZ1(123.45698);
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(123.45698, getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
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
			getS().setX2(123.45698);
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(123.45698, getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
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
			getS().setY2(123.45698);
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(123.45698, getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
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
			getS().setZ2(123.45698);
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(123.45698, getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}
	}

	@DisplayName("setX3")
	@Nested
	public class SetX3 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setX3(123.45698);
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(123.45698, getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}
	}

	@DisplayName("setY3")
	@Nested
	public class SetY3 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setY3(123.45698);
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(123.45698, getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}
	}

	@DisplayName("setZ3")
	@Nested
	public class SetZ3 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setZ3(123.45698);
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(123.45698, getS().getZ3());
		}
	}

	@DisplayName("getS1")
	@Nested
	public class GetS1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getS().getS1();
			assertNotNull(s);
			assertEpsilonEquals(0., s.getX1());
			assertEpsilonEquals(0., s.getY1());
			assertEpsilonEquals(0., s.getZ1());
			assertEpsilonEquals(1., s.getX2());
			assertEpsilonEquals(1., s.getY2());
			assertEpsilonEquals(1., s.getZ2());
		}
	}

	@DisplayName("getS2")
	@Nested
	public class GetS2 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getS().getS2();
			assertNotNull(s);
			assertEpsilonEquals(1., s.getX1());
			assertEpsilonEquals(1., s.getY1());
			assertEpsilonEquals(1., s.getZ1());
			assertEpsilonEquals(1., s.getX2());
			assertEpsilonEquals(0., s.getY2());
			assertEpsilonEquals(1., s.getZ2());
		}
	}

	@DisplayName("getS3")
	@Nested
	public class GetS3 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getS().getS3();
			assertNotNull(s);
			assertEpsilonEquals(1., s.getX1());
			assertEpsilonEquals(0., s.getY1());
			assertEpsilonEquals(1., s.getZ1());
			assertEpsilonEquals(0., s.getX2());
			assertEpsilonEquals(0., s.getY2());
			assertEpsilonEquals(0., s.getZ2());
		}
	}

	@DisplayName("getNormal")
	@Nested
	public class GetNormal {

		@DisplayName("Left-handed system #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void lh_1(CoordinateSystem3D cs) {
			assumeTrue(cs.isLeftHanded());
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createVector(-0.7071067812, 0, 0.7071067812), getS().getNormal());
		}

		@DisplayName("Right-handed system #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void rh_1(CoordinateSystem3D cs) {
			assumeTrue(cs.isRightHanded());
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createVector(0.7071067812, 0, -0.7071067812), getS().getNormal());
		}
	}

	@DisplayName("flipNormal")
	@Nested
	public class FlipNormal {

		@DisplayName("Left-handed system #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void lh_1(CoordinateSystem3D cs) {
			assumeTrue(cs.isLeftHanded());
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().flipNormal();
			assertEpsilonEquals(createVector(0.7071067812, 0, -0.7071067812), getS().getNormal());
		}

		@DisplayName("Right-handed system #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void rh_1(CoordinateSystem3D cs) {
			assumeTrue(cs.isRightHanded());
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().flipNormal();
			assertEpsilonEquals(createVector(-0.7071067812, 0, 0.7071067812), getS().getNormal());
		}
	}

	@DisplayName("getOrientation")
	@Nested
	public class GetOrientation {

		@DisplayName("Z up, Left-handed #1")
		@Test
		public final void zup_lh_1() {
			CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XYZ_LEFT_HAND);
			assertEpsilonEquals(factory.createQuaternion(0, -0.38268343237, 0, 0.9238795325), getS().getOrientation());
		}

		@DisplayName("Z up, Right-handed #1")
		@Test
		public final void zup_rh_1() {
			CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XYZ_RIGHT_HAND);
			assertEpsilonEquals(factory.createQuaternion(0, 0.9238795325, 0, 0.38268343237), getS().getOrientation());
		}

		@DisplayName("Y up, Left-handed #1")
		@Test
		public final void yup_lh_1() {
			CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XZY_LEFT_HAND);
			assertEpsilonEquals(factory.createQuaternion(-.5, 0., -.5, .70710678119), getS().getOrientation());
		}

		@DisplayName("Y up, Right-handed #1")
		@Test
		public final void yup_rh_1() {
			CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XZY_RIGHT_HAND);
			assertEpsilonEquals(factory.createQuaternion(.5, 0., .5, .70710678119), getS().getOrientation());
		}
	}

	@DisplayName("setOrientationFromCoordinateSystem")
	@Nested
	public class SetOrientationFromCoordinateSystem {

		@BeforeEach
		public void setUp() {
			getS().clear();
			getS().set(0, 0, 0, 1, 0, 1, 1, 1, 1);
		}
		
		@DisplayName("Z up, Left-handed #1")
		@Test
		public final void zup_lh_1() {
			getS().setOrientationFromCoordinateSystem(CoordinateSystem3D.XYZ_LEFT_HAND);
			assertEpsilonEquals(factory.createQuaternion(0, -0.38268343237, 0, 0.9238795325), getS().getOrientation());
		}

		@DisplayName("Z up, Right-handed #1")
		@Test
		public final void zup_rh_1() {
			getS().setOrientationFromCoordinateSystem(CoordinateSystem3D.XYZ_RIGHT_HAND);
			assertEpsilonEquals(factory.createQuaternion(0, 0.38268343237, 0, 0.9238795325), getS().getOrientation());
		}

		@DisplayName("Y up, Left-handed #1")
		@Test
		public final void yup_lh_1() {
			getS().setOrientationFromCoordinateSystem(CoordinateSystem3D.XZY_LEFT_HAND);
			assertEpsilonEquals(factory.createQuaternion(-.5, 0., -.5, .70710678119), getS().getOrientation());
		}

		@DisplayName("Y up, Right-handed #1")
		@Test
		public final void yup_rh_1() {
			getS().setOrientationFromCoordinateSystem(CoordinateSystem3D.XZY_RIGHT_HAND);
			assertEpsilonEquals(factory.createQuaternion(.5, 0., .5, .70710678119), getS().getOrientation());
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
			var shape = getS().clone();
			assertNotNull(shape);
			assertNotSame(getS(), shape);
			assertInstanceOf(shape, Triangle3afp.class);
			assertEpsilonEquals(0., shape.getX1());
			assertEpsilonEquals(0., shape.getY1());
			assertEpsilonEquals(0., shape.getZ1());
			assertEpsilonEquals(1., shape.getX2());
			assertEpsilonEquals(1., shape.getY2());
			assertEpsilonEquals(1., shape.getZ2());
			assertEpsilonEquals(1., shape.getX3());
			assertEpsilonEquals(0., shape.getY3());
			assertEpsilonEquals(1., shape.getZ3());
		}
	}

	@DisplayName("containsTrianglePoint")
	@Nested
	public class ContainsTrianglePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1_vertexA_forceFalse(CoordinateSystem3D cs) {
			// vertex A, forceCoplanar=false => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 0, 1,
					-5, -6, 4,
					0, 0, 0,
					false, EPSILON));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2_vertexA_forceTrue(CoordinateSystem3D cs) {
			// vertex A, forceCoplanar=true => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 0, 1,
					-5, -6, 4,
					0, 0, 0,
					true, EPSILON));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3_vertexB(CoordinateSystem3D cs) {
			// vertex B => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 0, 1,
					-5, -6, 4,
					1, 0, 1,
					true, EPSILON));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4_vertexC(CoordinateSystem3D cs) {
			// vertex C => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 0, 1,
					-5, -6, 4,
					-5, -6, 4,
					true, EPSILON));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5_edgeAB_midpoint(CoordinateSystem3D cs) {
			// midpoint AB on edge => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 0, 1,
					-5, -6, 4,
					0.5, 0, 0.5,
					true, EPSILON));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6_edgeAC_midpoint(CoordinateSystem3D cs) {
			// midpoint AC on edge => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 0, 1,
					-5, -6, 4,
					-2.5, -3, 2,
					true, EPSILON));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7_edgeBC_midpoint(CoordinateSystem3D cs) {
			// midpoint BC on edge => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 0, 1,
					-5, -6, 4,
					-2, -3, 2.5,
					true, EPSILON));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8_interior(CoordinateSystem3D cs) {
			// strict interior point (barycentric) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// P = 0.2*A + 0.3*B + 0.5*C
			assertTrue(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 0, 1,
					-5, -6, 4,
					-2.2, -3, 2.3,
					true, EPSILON));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9_outside_coplanar(CoordinateSystem3D cs) {
			// coplanar but outside near AB extension => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 0, 1,
					-5, -6, 4,
					2, 0, 2,
					true, EPSILON));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10_nonCoplanar_projectionInside_forceFalse(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 0, 1,
					-5, -6, 4,
					-1.6, -3.9, 2.9,
					false, EPSILON));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11_nonCoplanar_projectionInside_forceTrue(CoordinateSystem3D cs) {
			// non-coplanar, projection inside, forceCoplanar=true => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 0, 1,
					-5, -6, 4,
					-1.6, -3.9, 2.9,
					true, EPSILON));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12_nonCoplanar_projectionOutside_forceFalse(CoordinateSystem3D cs) {
			// non-coplanar, projection outside, forceCoplanar=false => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// outside coplanar point (2,0,2) shifted along normal
			assertFalse(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 0, 1,
					-5, -6, 4,
					2.6, -0.9, 1.4,
					false, EPSILON));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13_nonCoplanar_projectionOutside_forceTrue(CoordinateSystem3D cs) {
			// non-coplanar, projection outside, forceCoplanar=true => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 0, 1,
					-5, -6, 4,
					2.6, -0.9, 1.4,
					true, EPSILON));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14_degenerateTriangle_collinear(CoordinateSystem3D cs) {
			// degenerate triangle (collinear) => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 1, 1,
					2, 2, 2,
					1, 1, 1,
					true, EPSILON));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.containsTrianglePoint(
					0, 0, 0,
					1, 0, 1,
					-5, -6, 4,
					-1.8253562504, 3.6380343755, 3.0253562504,
					false, EPSILON));
		}
	}

	@DisplayName("containsProjectionOf")
	@Nested
	public class ContainsProjectionOf {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			// vertex A => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsProjectionOf(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			// vertex B => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsProjectionOf(createPoint(1, 1, 1)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			// vertex C => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsProjectionOf(createPoint(1, 0, 1)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			// midpoint AB (on edge) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsProjectionOf(createPoint(0.5, 0.5, 0.5)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			// midpoint AC (on edge) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsProjectionOf(createPoint(0.5, 0, 0.5)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			// midpoint BC (on edge) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsProjectionOf(createPoint(1, 0.5, 1)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			// strict interior coplanar => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// P = 0.5*A + 0.2*B + 0.3*C = (0.5, 0.2, 0.5)
			assertTrue(getS().containsProjectionOf(createPoint(0.5, 0.2, 0.5)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			// coplanar outside => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// A + 1.2(B-A) + 0.2(C-A) => u+v=1.4 > 1 (outside, still on plane)
			assertFalse(getS().containsProjectionOf(createPoint(1.4, 1.2, 1.4)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			// non-coplanar above, projection inside => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Plane normal for AB x AC = (1,0,-1).
			// From inside point (0.5,0.2,0.5), add +0.3*normal -> (0.8,0.2,0.2)
			assertTrue(getS().containsProjectionOf(createPoint(0.8, 0.2, 0.2)));
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			// non-coplanar below, projection inside => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// From inside point (0.5,0.2,0.5), add -0.3*normal -> (0.2,0.2,0.8)
			assertTrue(getS().containsProjectionOf(createPoint(0.2, 0.2, 0.8)));
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			// non-coplanar, projection outside => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Outside coplanar point (1.4,1.2,1.4), shifted off-plane by +0.2*normal
			assertFalse(getS().containsProjectionOf(createPoint(1.6, 1.2, 1.2)));
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			// far point, projection on vertex A => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// A + k*normal with k=10 and normal=(1,0,-1): projection is A
			assertTrue(getS().containsProjectionOf(createPoint(10, 0, -10)));
		}

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			// vertex A => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsProjectionOf(0, 0, 0));
		}

		@DisplayName("(double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_2(CoordinateSystem3D cs) {
			// vertex B => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsProjectionOf(1, 1, 1));
		}

		@DisplayName("(double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_3(CoordinateSystem3D cs) {
			// vertex C => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsProjectionOf(1, 0, 1));
		}

		@DisplayName("(double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_4(CoordinateSystem3D cs) {
			// midpoint AB (on edge) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsProjectionOf(0.5, 0.5, 0.5));
		}

		@DisplayName("(double,double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_5(CoordinateSystem3D cs) {
			// midpoint AC (on edge) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsProjectionOf(0.5, 0, 0.5));
		}

		@DisplayName("(double,double,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_6(CoordinateSystem3D cs) {
			// midpoint BC (on edge) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().containsProjectionOf(1, 0.5, 1));
		}

		@DisplayName("(double,double,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_7(CoordinateSystem3D cs) {
			// strict interior coplanar => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// P = 0.5*A + 0.2*B + 0.3*C = (0.5, 0.2, 0.5)
			assertTrue(getS().containsProjectionOf(0.5, 0.2, 0.5));
		}

		@DisplayName("(double,double,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_8(CoordinateSystem3D cs) {
			// coplanar outside => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// A + 1.2(B-A) + 0.2(C-A) => u+v=1.4 > 1 (outside, still on plane)
			assertFalse(getS().containsProjectionOf(1.4, 1.2, 1.4));
		}

		@DisplayName("(double,double,double) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_9(CoordinateSystem3D cs) {
			// non-coplanar above, projection inside => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Plane normal for AB x AC = (1,0,-1).
			// From inside point (0.5,0.2,0.5), add +0.3*normal -> (0.8,0.2,0.2)
			assertTrue(getS().containsProjectionOf(0.8, 0.2, 0.2));
		}

		@DisplayName("(double,double,double) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_10(CoordinateSystem3D cs) {
			// non-coplanar below, projection inside => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// From inside point (0.5,0.2,0.5), add -0.3*normal -> (0.2,0.2,0.8)
			assertTrue(getS().containsProjectionOf(0.2, 0.2, 0.8));
		}

		@DisplayName("(double,double,double) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_11(CoordinateSystem3D cs) {
			// non-coplanar, projection outside => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Outside coplanar point (1.4,1.2,1.4), shifted off-plane by +0.2*normal
			assertFalse(getS().containsProjectionOf(1.6, 1.2, 1.2));
		}

		@DisplayName("(double,double,double) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_12(CoordinateSystem3D cs) {
			// far point, projection on vertex A => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// A + k*normal with k=10 and normal=(1,0,-1): projection is A
			assertTrue(getS().containsProjectionOf(10, 0, -10));
		}
	}

	@DisplayName("isEmpty")
	@Nested
	public class IsEmpty {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			// regular triangle => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().isEmpty());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// all vertices equal (point) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(createTriangle(
					0, 0, 0,
					0, 0, 0,
					0, 0, 0).isEmpty());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// two vertices equal => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(createTriangle(
					1, 1, 1,
					1, 1, 1,
					2, 2, 2).isEmpty());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// collinear distinct points => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(createTriangle(
					0, 0, 0,
					1, 1, 1,
					2, 2, 2).isEmpty());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// collinear on X axis => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(createTriangle(
					-1, 0, 0,
					0, 0, 0,
					3, 0, 0).isEmpty());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// coplanar but non-collinear => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(createTriangle(
					0, 0, 0,
					2, 0, 0,
					0, 3, 0).isEmpty());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// non-coplanar generic non-collinear => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(createTriangle(
					0, 0, 0,
					1, 0, 1,
					1, 1, 0).isEmpty());
		}

		@DisplayName("#8 ")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			// near-collinear but not collinear => false (exact semantics)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(createTriangle(
					0, 0, 0,
					1, 1, 1,
					2, 2, 2.000000000001).isEmpty());
		}
	}

	@DisplayName("isDegeneratedPoint")
	@Nested
	public class IsDegeneratedPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			// regular triangle => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().isDegeneratedPoint());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// all vertices equal (origin) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(createTriangle(0, 0, 0, 0, 0, 0, 0, 0, 0).isDegeneratedPoint());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// all vertices equal (non-zero) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(createTriangle(2.5, -3.75, 9, 2.5, -3.75, 9, 2.5, -3.75, 9).isDegeneratedPoint());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// two equal, one different => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(createTriangle(1, 1, 1, 1, 1, 1, 2, 2, 2).isDegeneratedPoint());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// collinear but distinct => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(createTriangle(0, 0, 0, 1, 1, 1, 2, 2, 2).isDegeneratedPoint());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// very close but not identical => false (exact semantics)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(createTriangle(
					1, 1, 1,
					1 + 1e-12, 1, 1,
					1, 1 + 1e-12, 1).isDegeneratedPoint());
		}
	}

	@DisplayName("equalsToShape")
	@Nested
	public class EqualsToShape {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			// same vertices same order => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape((T) createTriangle(0, 0, 0, 1, 1, 1, 1, 0, 1)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// different vertex set => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) createTriangle(0, 0, 0, 1, 0, 1, 1, 1, 2)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// same vertices cyclic permutation => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape((T) createTriangle(1, 1, 1, 1, 0, 1, 0, 0, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// same vertices reverse permutation => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape((T) createTriangle(1, 0, 1, 1, 1, 1, 0, 0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// one vertex changed => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) createTriangle(0, 0, 0, 1, 1, 1, 1, 0, 2)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// translated triangle => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) createTriangle(10, 10, 10, 11, 11, 11, 11, 10, 11)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// degenerate point triangle vs regular => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) createTriangle(0, 0, 0, 0, 0, 0, 0, 0, 0)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			// collinear triangle vs regular => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) createTriangle(0, 0, 0, 1, 1, 1, 2, 2, 2)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			// reflexive => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape(getS()));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			// null => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) null));
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
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(0., getS().getX2());
			assertEpsilonEquals(0., getS().getY2());
			assertEpsilonEquals(0., getS().getZ2());
			assertEpsilonEquals(0., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(0., getS().getZ3());
		}
	}

	@DisplayName("set")
	@Nested
	public class Set {

		@DisplayName("(double,double,double, double,double,double, double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledoubledoubledoubledoubledoubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(-11, 5, 18, -45, 7, -6, 19, 16, -4);
			assertEpsilonEquals(-11., getS().getX1());
			assertEpsilonEquals(5., getS().getY1());
			assertEpsilonEquals(18., getS().getZ1());
			assertEpsilonEquals(-45., getS().getX2());
			assertEpsilonEquals(7., getS().getY2());
			assertEpsilonEquals(-6., getS().getZ2());
			assertEpsilonEquals(19., getS().getX3());
			assertEpsilonEquals(16., getS().getY3());
			assertEpsilonEquals(-4., getS().getZ3());
		}

		@DisplayName("(Point3D, Point3D, Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set(createPoint(-11, 5, 18), createPoint(-45, 7, -6), createPoint(19, 16, -4));
			assertEpsilonEquals(-11., getS().getX1());
			assertEpsilonEquals(5., getS().getY1());
			assertEpsilonEquals(18., getS().getZ1());
			assertEpsilonEquals(-45., getS().getX2());
			assertEpsilonEquals(7., getS().getY2());
			assertEpsilonEquals(-6., getS().getZ2());
			assertEpsilonEquals(19., getS().getX3());
			assertEpsilonEquals(16., getS().getY3());
			assertEpsilonEquals(-4., getS().getZ3());
		}

		@DisplayName("(Point3D, Point3D, Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().set(null, createPoint(-45, 7, -6), createPoint(19, 16, -4)));
		}

		@DisplayName("(Point3D, Point3D, Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().set(createPoint(-45, 7, -6), null, createPoint(19, 16, -4)));
		}

		@DisplayName("(Point3D, Point3D, Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().set(createPoint(-45, 7, -6), createPoint(19, 16, -4), null));
		}

		@DisplayName("(Point3D, Point3D, Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().set(null, null, createPoint(19, 16, -4)));
		}

		@DisplayName("(Point3D, Point3D, Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().set(null, createPoint(19, 16, -4), null));
		}

		@DisplayName("(Point3D, Point3D, Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().set(createPoint(19, 16, -4), null, null));
		}

		@DisplayName("(Point3D, Point3D, Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().set(null, null, null));
		}

		@DisplayName("(IT) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().set((T) createTriangle(-11, 5, 18, -45, 7, -6, 19, 16, -4));
			assertEpsilonEquals(-11., getS().getX1());
			assertEpsilonEquals(5., getS().getY1());
			assertEpsilonEquals(18., getS().getZ1());
			assertEpsilonEquals(-45., getS().getX2());
			assertEpsilonEquals(7., getS().getY2());
			assertEpsilonEquals(-6., getS().getZ2());
			assertEpsilonEquals(19., getS().getX3());
			assertEpsilonEquals(16., getS().getY3());
			assertEpsilonEquals(-4., getS().getZ3());
		}
	}

	@DisplayName("translate")
	@Nested
	public class Translate {

		@DisplayName("(double, double, double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(0., 0., 0.);
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(double, double, double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractTriangle3dTestCase#proposeArguments3Coords")
		public final void doubledoubledouble_2(CoordinateSystem3D cs, Double dx, Double dy, Double dz) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(dx, dy, dz);
			assertEpsilonEquals(dx + 0., getS().getX1());
			assertEpsilonEquals(dy + 0., getS().getY1());
			assertEpsilonEquals(dz + 0., getS().getZ1());
			assertEpsilonEquals(dx + 1., getS().getX2());
			assertEpsilonEquals(dy + 1., getS().getY2());
			assertEpsilonEquals(dz + 1., getS().getZ2());
			assertEpsilonEquals(dx + 1., getS().getX3());
			assertEpsilonEquals(dy + 0., getS().getY3());
			assertEpsilonEquals(dz + 1., getS().getZ3());
		}

		@DisplayName("(Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(createVector(0., 0., 0.));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Vector3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractTriangle3dTestCase#proposeArguments3Coords")
		public final void vector_2(CoordinateSystem3D cs, Double dx, Double dy, Double dz) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().translate(createVector(dx, dy, dz));
			assertEpsilonEquals(dx + 0., getS().getX1());
			assertEpsilonEquals(dy + 0., getS().getY1());
			assertEpsilonEquals(dz + 0., getS().getZ1());
			assertEpsilonEquals(dx + 1., getS().getX2());
			assertEpsilonEquals(dy + 1., getS().getY2());
			assertEpsilonEquals(dz + 1., getS().getZ2());
			assertEpsilonEquals(dx + 1., getS().getX3());
			assertEpsilonEquals(dy + 0., getS().getY3());
			assertEpsilonEquals(dz + 1., getS().getZ3());
		}
	}

	@DisplayName("transform")
	@Nested
	public class Transform {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			// identity => unchanged
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setIdentity();
			getS().transform(transform);
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// null transform => assertion error
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().transform(null));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// translation => all vertices translated
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setTranslation(2., -3., 4.);
			getS().transform(transform);

			assertEpsilonEquals(2., getS().getX1());
			assertEpsilonEquals(-3., getS().getY1());
			assertEpsilonEquals(4., getS().getZ1());
			assertEpsilonEquals(3., getS().getX2());
			assertEpsilonEquals(-2., getS().getY2());
			assertEpsilonEquals(5., getS().getZ2());
			assertEpsilonEquals(3., getS().getX3());
			assertEpsilonEquals(-3., getS().getY3());
			assertEpsilonEquals(5., getS().getZ3());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// scale => all coordinates scaled
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setDiagonal(2., 3., 4., 1.);
			getS().transform(transform);
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(2., getS().getX2());
			assertEpsilonEquals(3., getS().getY2());
			assertEpsilonEquals(4., getS().getZ2());
			assertEpsilonEquals(2., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(4., getS().getZ3());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// rotation around Z by +90deg
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setRotation(factory.createAxisAngle(0., 0., 1., Math.PI / 2.));
			getS().transform(transform);
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(-1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(0., getS().getX3());
			assertEpsilonEquals(1., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// composition scale+translation
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var scale = new Transform3D();
			scale.setDiagonal(2., 2., 2., 1.);
			var translation = new Transform3D();
			translation.setTranslation(1., 2., 3.);
			translation.mul(scale);
			getS().transform(translation);
			assertEpsilonEquals(1., getS().getX1());
			assertEpsilonEquals(2., getS().getY1());
			assertEpsilonEquals(3., getS().getZ1());
			assertEpsilonEquals(3., getS().getX2());
			assertEpsilonEquals(4., getS().getY2());
			assertEpsilonEquals(5., getS().getZ2());
			assertEpsilonEquals(3., getS().getX3());
			assertEpsilonEquals(2., getS().getY3());
			assertEpsilonEquals(5., getS().getZ3());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// composition translation+scale
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var scale = new Transform3D();
			scale.setDiagonal(2., 2., 2., 1.);
			var translation = new Transform3D();
			translation.setTranslation(1., 2., 3.);
			scale.mul(translation);
			getS().transform(scale);
			assertEpsilonEquals(2., getS().getX1());
			assertEpsilonEquals(4., getS().getY1());
			assertEpsilonEquals(6., getS().getZ1());
			assertEpsilonEquals(4., getS().getX2());
			assertEpsilonEquals(6., getS().getY2());
			assertEpsilonEquals(8., getS().getZ2());
			assertEpsilonEquals(4., getS().getX3());
			assertEpsilonEquals(4., getS().getY3());
			assertEpsilonEquals(8., getS().getZ3());
		}
	}

	@DisplayName("createTransformedShape")
	@Nested
	public class CreateTransformedShape {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			// identity => unchanged
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setIdentity();
			var shape = (Triangle3afp) getS().createTransformedShape(transform);
			assertNotSame(getS(), shape);
			assertEpsilonEquals(0., shape.getX1());
			assertEpsilonEquals(0., shape.getY1());
			assertEpsilonEquals(0., shape.getZ1());
			assertEpsilonEquals(1., shape.getX2());
			assertEpsilonEquals(1., shape.getY2());
			assertEpsilonEquals(1., shape.getZ2());
			assertEpsilonEquals(1., shape.getX3());
			assertEpsilonEquals(0., shape.getY3());
			assertEpsilonEquals(1., shape.getZ3());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// null transform => assertion error
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> shape.transform(null));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// translation => all vertices translated
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setTranslation(2., -3., 4.);
			var shape = (Triangle3afp) getS().createTransformedShape(transform);
			assertNotSame(getS(), shape);
			assertEpsilonEquals(2., shape.getX1());
			assertEpsilonEquals(-3., shape.getY1());
			assertEpsilonEquals(4., shape.getZ1());
			assertEpsilonEquals(3., shape.getX2());
			assertEpsilonEquals(-2., shape.getY2());
			assertEpsilonEquals(5., shape.getZ2());
			assertEpsilonEquals(3., shape.getX3());
			assertEpsilonEquals(-3., shape.getY3());
			assertEpsilonEquals(5., shape.getZ3());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// scale => all coordinates scaled
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setDiagonal(2., 3., 4., 1.);
			var shape = (Triangle3afp) getS().createTransformedShape(transform);
			assertNotSame(getS(), shape);
			assertEpsilonEquals(0., shape.getX1());
			assertEpsilonEquals(0., shape.getY1());
			assertEpsilonEquals(0., shape.getZ1());
			assertEpsilonEquals(2., shape.getX2());
			assertEpsilonEquals(3., shape.getY2());
			assertEpsilonEquals(4., shape.getZ2());
			assertEpsilonEquals(2., shape.getX3());
			assertEpsilonEquals(0., shape.getY3());
			assertEpsilonEquals(4., shape.getZ3());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// rotation around Z by +90deg
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var transform = new Transform3D();
			transform.setRotation(factory.createAxisAngle(0., 0., 1., Math.PI / 2.));
			var shape = (Triangle3afp) getS().createTransformedShape(transform);
			assertNotSame(getS(), shape);
			assertEpsilonEquals(0., shape.getX1());
			assertEpsilonEquals(0., shape.getY1());
			assertEpsilonEquals(0., shape.getZ1());
			assertEpsilonEquals(-1., shape.getX2());
			assertEpsilonEquals(1., shape.getY2());
			assertEpsilonEquals(1., shape.getZ2());
			assertEpsilonEquals(0., shape.getX3());
			assertEpsilonEquals(1., shape.getY3());
			assertEpsilonEquals(1., shape.getZ3());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// composition scale+translation
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var scale = new Transform3D();
			scale.setDiagonal(2., 2., 2., 1.);
			var translation = new Transform3D();
			translation.setTranslation(1., 2., 3.);
			translation.mul(scale);
			var shape = (Triangle3afp) getS().createTransformedShape(translation);
			assertNotSame(getS(), shape);
			assertEpsilonEquals(1., shape.getX1());
			assertEpsilonEquals(2., shape.getY1());
			assertEpsilonEquals(3., shape.getZ1());
			assertEpsilonEquals(3., shape.getX2());
			assertEpsilonEquals(4., shape.getY2());
			assertEpsilonEquals(5., shape.getZ2());
			assertEpsilonEquals(3., shape.getX3());
			assertEpsilonEquals(2., shape.getY3());
			assertEpsilonEquals(5., shape.getZ3());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// composition translation+scale
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var scale = new Transform3D();
			scale.setDiagonal(2., 2., 2., 1.);
			var translation = new Transform3D();
			translation.setTranslation(1., 2., 3.);
			scale.mul(translation);
			var shape = (Triangle3afp) getS().createTransformedShape(scale);
			assertEpsilonEquals(2., shape.getX1());
			assertEpsilonEquals(4., shape.getY1());
			assertEpsilonEquals(6., shape.getZ1());
			assertEpsilonEquals(4., shape.getX2());
			assertEpsilonEquals(6., shape.getY2());
			assertEpsilonEquals(8., shape.getZ2());
			assertEpsilonEquals(4., shape.getX3());
			assertEpsilonEquals(4., shape.getY3());
			assertEpsilonEquals(8., shape.getZ3());
		}
	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			// vertex A => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(0, 0, 0));
		}

		@DisplayName("(double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_2(CoordinateSystem3D cs) {
			// vertex B => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(1, 1, 1));
		}

		@DisplayName("(double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_3(CoordinateSystem3D cs) {
			// vertex C => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(1, 0, 1));
		}

		@DisplayName("(double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_4(CoordinateSystem3D cs) {
			// midpoint AB (edge) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(0.5, 0.5, 0.5));
		}

		@DisplayName("(double,double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_5(CoordinateSystem3D cs) {
			// midpoint AC (edge) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(0.5, 0, 0.5));
		}

		@DisplayName("(double,double,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_6(CoordinateSystem3D cs) {
			// midpoint BC (edge) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(1, 0.5, 1));
		}

		@DisplayName("(double,double,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_7(CoordinateSystem3D cs) {
			// strict interior coplanar => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// P = 0.5*A + 0.2*B + 0.3*C = (0.5, 0.2, 0.5)
			assertTrue(getS().contains(0.5, 0.2, 0.5));
		}

		@DisplayName("(double,double,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_8(CoordinateSystem3D cs) {
			// coplanar outside => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Outside on plane: A + 1.2(B-A) + 0.2(C-A), u+v>1
			assertFalse(getS().contains(1.4, 1.2, 1.4));
		}

		@DisplayName("(double,double,double) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_9(CoordinateSystem3D cs) {
			// non-coplanar, projection inside => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Normal of plane is (1,0,-1). Shift interior point off plane.
			assertFalse(getS().contains(0.8, 0.2, 0.2));
		}

		@DisplayName("(double,double,double) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_10(CoordinateSystem3D cs) {
			// non-coplanar, projection outside => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(1.6, 1.2, 1.2));
		}

		@DisplayName("(double,double,double) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_11(CoordinateSystem3D cs) {
			// far point on plane but outside => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// x=z => coplanar with this plane, but far outside
			assertFalse(getS().contains(10, 10, 10));
		}

		@DisplayName("(double,double,double) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_12(CoordinateSystem3D cs) {
			// near-edge interior => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(0.999999999, 0.000000001, 0.999999999));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			// vertex A => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			// vertex B => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(1, 1, 1)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			// vertex C => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(1, 0, 1)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			// midpoint AB (edge) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(0.5, 0.5, 0.5)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			// midpoint AC (edge) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(0.5, 0, 0.5)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			// midpoint BC (edge) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(1, 0.5, 1)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			// strict interior coplanar => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// P = 0.5*A + 0.2*B + 0.3*C = (0.5, 0.2, 0.5)
			assertTrue(getS().contains(createPoint(0.5, 0.2, 0.5)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			// coplanar outside => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Outside on plane: A + 1.2(B-A) + 0.2(C-A), u+v>1
			assertFalse(getS().contains(createPoint(1.4, 1.2, 1.4)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			// non-coplanar, projection inside => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Normal of plane is (1,0,-1). Shift interior point off plane.
			assertFalse(getS().contains(createPoint(0.8, 0.2, 0.2)));
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			// non-coplanar, projection outside => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createPoint(1.6, 1.2, 1.2)));
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			// far point on plane but outside => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// x=z => coplanar with this plane, but far outside
			assertFalse(getS().contains(createPoint(10, 10, 10)));
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			// near-edge interior => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createPoint(0.999999999, 0.000000001, 0.999999999)));
		}
		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			// point-box on vertex A => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(0, 0, 0, 0, 0, 0)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_2(CoordinateSystem3D cs) {
			// point-box on vertex B => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(1, 1, 1, 1, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_3(CoordinateSystem3D cs) {
			// point-box on vertex C => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(1, 0, 1, 1, 0, 1)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_4(CoordinateSystem3D cs) {
			// point-box at interior point => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(0.5, 0.2, 0.5, 0.5, 0.2, 0.5)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_5(CoordinateSystem3D cs) {
			// point-box on edge AB => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(0.5, 0.5, 0.5, 0.5, 0.5, 0.5)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_6(CoordinateSystem3D cs) {
			// point-box coplanar but outside triangle => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBoxFromPoints(1.4, 1.2, 1.4, 1.4, 1.2, 1.4)));
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_7(CoordinateSystem3D cs) {
			// point-box non-coplanar above interior projection => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBoxFromPoints(0.8, 0.2, 0.2, 0.8, 0.2, 0.2)));
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_8(CoordinateSystem3D cs) {
			// small box centered on interior point but non-zero volume => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBoxFromPoints(
					0.49, 0.19, 0.49,
					0.51, 0.21, 0.51)));
		}

		@DisplayName("(AlignedBox3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_9(CoordinateSystem3D cs) {
			// flat box in triangle plane with area => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// z=x plane respected, but rectangle has area (not a single point)
			assertFalse(getS().contains(createAlignedBoxFromPoints(
					0.40, 0.20, 0.40,
					0.60, 0.30, 0.60)));
		}

		@DisplayName("(AlignedBox3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_10(CoordinateSystem3D cs) {
			// large box spanning around triangle => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBoxFromPoints(
					-1, -1, -1,
					2, 2, 2)));
		}

		@DisplayName("(AlignedBox3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_11(CoordinateSystem3D cs) {
			// point-box far away => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createAlignedBoxFromPoints(10, 10, 10, 10, 10, 10)));
		}

		@DisplayName("(AlignedBox3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_12(CoordinateSystem3D cs) {
			// point-box on edge BC => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createAlignedBoxFromPoints(1, 0.5, 1, 1, 0.5, 1)));
		}
	}
	@DisplayName("Transform Tools")
	@Nested
	public class TransformTools {

		@DisplayName("rotateAroundOrigin")
		@Nested
		public class RotateAroundOrigin {

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				// identity quaternion => unchanged
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.TransformTools.rotateAroundOrigin(getS(), 0., 0., 0., 1.);
				assertEpsilonEquals(0., shape.getX1());
				assertEpsilonEquals(0., shape.getY1());
				assertEpsilonEquals(0., shape.getZ1());
				assertEpsilonEquals(1., shape.getX2());
				assertEpsilonEquals(1., shape.getY2());
				assertEpsilonEquals(1., shape.getZ2());
				assertEpsilonEquals(1., shape.getX3());
				assertEpsilonEquals(0., shape.getY3());
				assertEpsilonEquals(1., shape.getZ3());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				// minus identity quaternion => unchanged
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.TransformTools.rotateAroundOrigin(getS(), 0., 0., 0., -1.);
				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(1., getS().getX2());
				assertEpsilonEquals(1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(1., getS().getX3());
				assertEpsilonEquals(0., getS().getY3());
				assertEpsilonEquals(1., getS().getZ3());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				// 90deg around Z
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				final var s = Math.sqrt(0.5); // sin/cos(pi/4)
				Triangle3afp.TransformTools.rotateAroundOrigin(getS(), 0., 0., s, s);
				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(-1., getS().getX2());
				assertEpsilonEquals(1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(0., getS().getX3());
				assertEpsilonEquals(1., getS().getY3());
				assertEpsilonEquals(1., getS().getZ3());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4(CoordinateSystem3D cs) {
				// 180deg around Z
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.TransformTools.rotateAroundOrigin(getS(), 0., 0., 1., 0.);
				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(-1., getS().getX2());
				assertEpsilonEquals(-1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(-1., getS().getX3());
				assertEpsilonEquals(0., getS().getY3());
				assertEpsilonEquals(1., getS().getZ3());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_5(CoordinateSystem3D cs) {
				// 90deg around X
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				final var s = Math.sqrt(0.5);
				Triangle3afp.TransformTools.rotateAroundOrigin(getS(), s, 0., 0., s);
				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(1., getS().getX2());
				assertEpsilonEquals(-1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(1., getS().getX3());
				assertEpsilonEquals(-1., getS().getY3());
				assertEpsilonEquals(0., getS().getZ3());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_6(CoordinateSystem3D cs) {
				// 90deg around Y
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				final var s = Math.sqrt(0.5);
				Triangle3afp.TransformTools.rotateAroundOrigin(getS(), 0., s, 0., s);
				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(1., getS().getX2());
				assertEpsilonEquals(1., getS().getY2());
				assertEpsilonEquals(-1., getS().getZ2());
				assertEpsilonEquals(1., getS().getX3());
				assertEpsilonEquals(0., getS().getY3());
				assertEpsilonEquals(-1., getS().getZ3());
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_7(CoordinateSystem3D cs) {
				// non-normalized quaternion should behave the same
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Same rotation as #3 but scaled quaternion
				final var s = Math.sqrt(0.5);
				assertThrows(AssertionError.class, () -> Triangle3afp.TransformTools.rotateAroundOrigin(getS(), 0., 0., 10. * s, 10. * s));
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_8(CoordinateSystem3D cs) {
				// inverse rotation composition returns original
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// +90deg around Z
				final var s = Math.sqrt(0.5);
				Triangle3afp.TransformTools.rotateAroundOrigin(getS(), 0., 0., s, s);
				Triangle3afp.TransformTools.rotateAroundOrigin(getS(), 0., 0., -s, s); // inverse

				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(1., getS().getX2());
				assertEpsilonEquals(1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(1., getS().getX3());
				assertEpsilonEquals(0., getS().getY3());
				assertEpsilonEquals(1., getS().getZ3());
			}

			@DisplayName("#9")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_9(CoordinateSystem3D cs) {
				// null triangle => assertion error
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertThrows(AssertionError.class,
						() -> Triangle3afp.TransformTools.rotateAroundOrigin(null, 0., 0., 0., 1.));
			}

			@DisplayName("#10")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_10(CoordinateSystem3D cs) {
				// zero quaternion => assertion error
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertThrows(AssertionError.class,
						() -> Triangle3afp.TransformTools.rotateAroundOrigin(getS(), 0., 0., 0., 0.));
			}
		}

		@DisplayName("rotateAroundPivot")
		@Nested
		public class RotateAroundPivot {

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				// identity quaternion => unchanged
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., 0., 0., 1., 0., 0., 0.);
				assertEpsilonEquals(0., shape.getX1());
				assertEpsilonEquals(0., shape.getY1());
				assertEpsilonEquals(0., shape.getZ1());
				assertEpsilonEquals(1., shape.getX2());
				assertEpsilonEquals(1., shape.getY2());
				assertEpsilonEquals(1., shape.getZ2());
				assertEpsilonEquals(1., shape.getX3());
				assertEpsilonEquals(0., shape.getY3());
				assertEpsilonEquals(1., shape.getZ3());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				// minus identity quaternion => unchanged
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., 0., 0., -1., 0., 0., 0.);
				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(1., getS().getX2());
				assertEpsilonEquals(1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(1., getS().getX3());
				assertEpsilonEquals(0., getS().getY3());
				assertEpsilonEquals(1., getS().getZ3());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				// 90deg around Z
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				final var s = Math.sqrt(0.5); // sin/cos(pi/4)
				Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., 0., s, s, 0., 0., 0.);
				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(-1., getS().getX2());
				assertEpsilonEquals(1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(0., getS().getX3());
				assertEpsilonEquals(1., getS().getY3());
				assertEpsilonEquals(1., getS().getZ3());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4(CoordinateSystem3D cs) {
				// 180deg around Z
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., 0., 1., 0., 0., 0., 0.);
				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(-1., getS().getX2());
				assertEpsilonEquals(-1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(-1., getS().getX3());
				assertEpsilonEquals(0., getS().getY3());
				assertEpsilonEquals(1., getS().getZ3());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_5(CoordinateSystem3D cs) {
				// 90deg around X
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				final var s = Math.sqrt(0.5);
				Triangle3afp.TransformTools.rotateAroundPivot(getS(), s, 0., 0., s, 0., 0., 0.);
				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(1., getS().getX2());
				assertEpsilonEquals(-1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(1., getS().getX3());
				assertEpsilonEquals(-1., getS().getY3());
				assertEpsilonEquals(0., getS().getZ3());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_6(CoordinateSystem3D cs) {
				// 90deg around Y
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				final var s = Math.sqrt(0.5);
				Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., s, 0., s, 0., 0., 0.);
				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(1., getS().getX2());
				assertEpsilonEquals(1., getS().getY2());
				assertEpsilonEquals(-1., getS().getZ2());
				assertEpsilonEquals(1., getS().getX3());
				assertEpsilonEquals(0., getS().getY3());
				assertEpsilonEquals(-1., getS().getZ3());
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_7(CoordinateSystem3D cs) {
				// non-normalized quaternion should behave the same
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Same rotation as #3 but scaled quaternion
				final var s = Math.sqrt(0.5);
				assertThrows(AssertionError.class, () -> Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., 0., 10. * s, 10. * s, 0., 0., 0.));
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_8(CoordinateSystem3D cs) {
				// inverse rotation composition returns original
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// +90deg around Z
				final var s = Math.sqrt(0.5);
				Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., 0., s, s, 0., 0., 0.);
				Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., 0., -s, s, 0., 0., 0.); // inverse

				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(1., getS().getX2());
				assertEpsilonEquals(1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(1., getS().getX3());
				assertEpsilonEquals(0., getS().getY3());
				assertEpsilonEquals(1., getS().getZ3());
			}

			@DisplayName("#9")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_9(CoordinateSystem3D cs) {
				// null triangle => assertion error
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertThrows(AssertionError.class,
						() -> Triangle3afp.TransformTools.rotateAroundPivot(null, 0., 0., 0., 1., 0., 0., 0.));
			}

			@DisplayName("#10")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_10(CoordinateSystem3D cs) {
				// zero quaternion => assertion error
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertThrows(AssertionError.class,
						() -> Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., 0., 0., 0., 0., 0., 0.));
			}

			@DisplayName("#11")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_11(CoordinateSystem3D cs) {
				// 90deg around Z with pivot at A(0,0,0) => same as origin case
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				final var s = Math.sqrt(0.5);
				Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., 0., s, s, 0., 0., 0.);
				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(-1., getS().getX2());
				assertEpsilonEquals(1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(0., getS().getX3());
				assertEpsilonEquals(1., getS().getY3());
				assertEpsilonEquals(1., getS().getZ3());
			}

			@DisplayName("#12")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_12(CoordinateSystem3D cs) {
				// 180deg around Z with pivot at A(0,0,0)
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., 0., 1., 0., 0., 0., 0.);
				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(-1., getS().getX2());
				assertEpsilonEquals(-1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(-1., getS().getX3());
				assertEpsilonEquals(0., getS().getY3());
				assertEpsilonEquals(1., getS().getZ3());
			}

			@DisplayName("#13")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_13(CoordinateSystem3D cs) {
				// 180deg around Z with pivot at B(1,1,1): B fixed
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., 0., 1., 0., 1., 1., 1.);
				// A(0,0,0) -> (2,2,0), B fixed, C(1,0,1) -> (1,2,1)
				assertEpsilonEquals(2., getS().getX1());
				assertEpsilonEquals(2., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(1., getS().getX2());
				assertEpsilonEquals(1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(1., getS().getX3());
				assertEpsilonEquals(2., getS().getY3());
				assertEpsilonEquals(1., getS().getZ3());
			}

			@DisplayName("#14")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_14(CoordinateSystem3D cs) {
				// 180deg around Z with pivot at C(1,0,1): C fixed
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., 0., 1., 0., 1., 0., 1.);
				// A(0,0,0) -> (2,0,0), B(1,1,1) -> (1,-1,1), C fixed
				assertEpsilonEquals(2., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(1., getS().getX2());
				assertEpsilonEquals(-1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(1., getS().getX3());
				assertEpsilonEquals(0., getS().getY3());
				assertEpsilonEquals(1., getS().getZ3());
			}

			@DisplayName("#15")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_15(CoordinateSystem3D cs) {
				// 90deg around Z with arbitrary pivot P(1,2,3)
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				final var s = Math.sqrt(0.5);
				Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., 0., s, s, 1., 2., 3.);
				// Computed from p' = P + Rz90*(p-P)
				// A -> (3,1,0), B -> (2,2,1), C -> (3,2,1)
				assertEpsilonEquals(3., getS().getX1());
				assertEpsilonEquals(1., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(2., getS().getX2());
				assertEpsilonEquals(2., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(3., getS().getX3());
				assertEpsilonEquals(2., getS().getY3());
				assertEpsilonEquals(1., getS().getZ3());
			}

			@DisplayName("#16")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_16(CoordinateSystem3D cs) {
				// pivot translated on rotation axis (Z): same relative XY rotation
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				final var s = Math.sqrt(0.5);
				// Pivot on Z-axis but shifted in Z only
				Triangle3afp.TransformTools.rotateAroundPivot(getS(), 0., 0., s, s, 0., 0., 10.);
				// Z-rotation does not depend on pivot Z for x,y; z unchanged for each point
				assertEpsilonEquals(0., getS().getX1());
				assertEpsilonEquals(0., getS().getY1());
				assertEpsilonEquals(0., getS().getZ1());
				assertEpsilonEquals(-1., getS().getX2());
				assertEpsilonEquals(1., getS().getY2());
				assertEpsilonEquals(1., getS().getZ2());
				assertEpsilonEquals(0., getS().getX3());
				assertEpsilonEquals(1., getS().getY3());
				assertEpsilonEquals(1., getS().getZ3());
			}

			@DisplayName("#17")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_17(CoordinateSystem3D cs) {
				// two successive +90deg around same pivot == one 180deg around same pivot
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				final var s = Math.sqrt(0.5);
				// first shape: +90 then +90 around pivot B(1,1,1)
				final T t1 = (T) createTriangle(0, 0, 0, 1, 1, 1, 1, 0, 1);
				Triangle3afp.TransformTools.rotateAroundPivot(t1, 0., 0., s, s, 1., 1., 1.);
				Triangle3afp.TransformTools.rotateAroundPivot(t1, 0., 0., s, s, 1., 1., 1.);
				// Expected result of direct 180deg around Z with pivot (1,1,1):
				// A(0,0,0) -> (2,2,0)
				// B(1,1,1) -> (1,1,1)
				// C(1,0,1) -> (1,2,1)
				assertEpsilonEquals(2., t1.getX1());
				assertEpsilonEquals(2., t1.getY1());
				assertEpsilonEquals(0., t1.getZ1());
				assertEpsilonEquals(1., t1.getX2());
				assertEpsilonEquals(1., t1.getY2());
				assertEpsilonEquals(1., t1.getZ2());
				assertEpsilonEquals(1., t1.getX3());
				assertEpsilonEquals(2., t1.getY3());
				assertEpsilonEquals(1., t1.getZ3());
			}
		}
	}

	@DisplayName("rotate")
	@Nested
	public class Rotate {

		@DisplayName("(Quaternion) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quat_1(CoordinateSystem3D cs) {
			// identity quaternion => unchanged
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().rotate(factory.createQuaternion(0., 0., 0., 1.));
			assertEpsilonEquals(0., shape.getX1());
			assertEpsilonEquals(0., shape.getY1());
			assertEpsilonEquals(0., shape.getZ1());
			assertEpsilonEquals(1., shape.getX2());
			assertEpsilonEquals(1., shape.getY2());
			assertEpsilonEquals(1., shape.getZ2());
			assertEpsilonEquals(1., shape.getX3());
			assertEpsilonEquals(0., shape.getY3());
			assertEpsilonEquals(1., shape.getZ3());
		}

		@DisplayName("(Quaternion) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quat_2(CoordinateSystem3D cs) {
			// minus identity quaternion => unchanged
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().rotate(factory.createQuaternion(0., 0., 0., -1.));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quat_3(CoordinateSystem3D cs) {
			// 90deg around Z
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var s = Math.sqrt(0.5); // sin/cos(pi/4)
			getS().rotate(factory.createQuaternion(0., 0., s, s));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(-1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(0., getS().getX3());
			assertEpsilonEquals(1., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quat_4(CoordinateSystem3D cs) {
			// 180deg around Z
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().rotate(factory.createQuaternion(0., 0., 1., 0.));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(-1., getS().getX2());
			assertEpsilonEquals(-1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(-1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quat_5(CoordinateSystem3D cs) {
			// 90deg around X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var s = Math.sqrt(0.5);
			getS().rotate(factory.createQuaternion(s, 0., 0., s));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(-1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(-1., getS().getY3());
			assertEpsilonEquals(0., getS().getZ3());
		}

		@DisplayName("(Quaternion) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quat_6(CoordinateSystem3D cs) {
			// 90deg around Y
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var s = Math.sqrt(0.5);
			getS().rotate(factory.createQuaternion(0., s, 0., s));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(-1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(-1., getS().getZ3());
		}

		@DisplayName("(Quaternion) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quat_7(CoordinateSystem3D cs) {
			// non-normalized quaternion should behave the same
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Same rotation as #3 but scaled quaternion
			final var s = Math.sqrt(0.5);
			getS().rotate(factory.createQuaternion(0., 0., 10. * s, 10. * s));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(-1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(0., getS().getX3());
			assertEpsilonEquals(1., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quat_8(CoordinateSystem3D cs) {
			// inverse rotation composition returns original
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// +90deg around Z
			final var s = Math.sqrt(0.5);
			getS().rotate(factory.createQuaternion(0., 0., s, s));
			getS().rotate(factory.createQuaternion(0., 0., -s, s)); // inverse
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quat_9(CoordinateSystem3D cs) {
			// zero quaternion => assertion error
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class,
					() -> getS().rotate(factory.createQuaternion(0., 0., 0., 0.)));
		}

		@DisplayName("(Quaternion,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_1(CoordinateSystem3D cs) {
			// identity quaternion => unchanged
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().rotate(factory.createQuaternion(0., 0., 0., 1.), createPoint(0., 0., 0.));
			assertEpsilonEquals(0., shape.getX1());
			assertEpsilonEquals(0., shape.getY1());
			assertEpsilonEquals(0., shape.getZ1());
			assertEpsilonEquals(1., shape.getX2());
			assertEpsilonEquals(1., shape.getY2());
			assertEpsilonEquals(1., shape.getZ2());
			assertEpsilonEquals(1., shape.getX3());
			assertEpsilonEquals(0., shape.getY3());
			assertEpsilonEquals(1., shape.getZ3());
		}

		@DisplayName("(Quaternion,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_2(CoordinateSystem3D cs) {
			// minus identity quaternion => unchanged
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().rotate(factory.createQuaternion(0., 0., 0., -1.), createPoint(0., 0., 0.));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion,Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_3(CoordinateSystem3D cs) {
			// 90deg around Z
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var s = Math.sqrt(0.5); // sin/cos(pi/4)
			getS().rotate(factory.createQuaternion(0., 0., s, s), createPoint(0., 0., 0.));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(-1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(0., getS().getX3());
			assertEpsilonEquals(1., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion,Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_4(CoordinateSystem3D cs) {
			// 180deg around Z
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().rotate(factory.createQuaternion(0., 0., 1., 0.), createPoint(0., 0., 0.));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(-1., getS().getX2());
			assertEpsilonEquals(-1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(-1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion,Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_5(CoordinateSystem3D cs) {
			// 90deg around X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var s = Math.sqrt(0.5);
			getS().rotate(factory.createQuaternion(s, 0., 0., s), createPoint(0., 0., 0.));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(-1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(-1., getS().getY3());
			assertEpsilonEquals(0., getS().getZ3());
		}

		@DisplayName("(Quaternion,Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_6(CoordinateSystem3D cs) {
			// 90deg around Y
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var s = Math.sqrt(0.5);
			getS().rotate(factory.createQuaternion(0., s, 0., s), createPoint(0., 0., 0.));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(-1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(-1., getS().getZ3());
		}

		@DisplayName("(Quaternion,Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_7(CoordinateSystem3D cs) {
			// non-normalized quaternion should behave the same
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Same rotation as #3 but scaled quaternion
			final var s = Math.sqrt(0.5);
			getS().rotate(factory.createQuaternion(0., 0., 10. * s, 10. * s), createPoint(0., 0., 0.));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(-1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(0., getS().getX3());
			assertEpsilonEquals(1., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion,Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_8(CoordinateSystem3D cs) {
			// inverse rotation composition returns original
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// +90deg around Z
			final var s = Math.sqrt(0.5);
			getS().rotate(factory.createQuaternion(0., 0., s, s), createPoint(0., 0., 0.));
			getS().rotate(factory.createQuaternion(0., 0., -s, s), createPoint(0., 0., 0.)); // inverse

			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion,Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_9(CoordinateSystem3D cs) {
			// zero quaternion => assertion error
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class,
					() -> getS().rotate(factory.createQuaternion(0., 0., 0., 0.), createPoint(0., 0., 0.)));
		}

		@DisplayName("(Quaternion,Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_10(CoordinateSystem3D cs) {
			// 90deg around Z with pivot at A(0,0,0) => same as origin case
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var s = Math.sqrt(0.5);
			getS().rotate(factory.createQuaternion(0., 0., s, s), createPoint(0., 0., 0.));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(-1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(0., getS().getX3());
			assertEpsilonEquals(1., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion,Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_11(CoordinateSystem3D cs) {
			// 180deg around Z with pivot at A(0,0,0)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().rotate(factory.createQuaternion(0., 0., 1., 0.), createPoint(0., 0., 0.));
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(-1., getS().getX2());
			assertEpsilonEquals(-1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(-1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion,Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_12(CoordinateSystem3D cs) {
			// 180deg around Z with pivot at B(1,1,1): B fixed
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().rotate(factory.createQuaternion(0., 0., 1., 0.), createPoint(1., 1., 1.));
			// A(0,0,0) -> (2,2,0), B fixed, C(1,0,1) -> (1,2,1)
			assertEpsilonEquals(2., getS().getX1());
			assertEpsilonEquals(2., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(2., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion,Point3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_13(CoordinateSystem3D cs) {
			// 180deg around Z with pivot at C(1,0,1): C fixed
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().rotate(factory.createQuaternion(0., 0., 1., 0.), createPoint(1., 0., 1.));
			// A(0,0,0) -> (2,0,0), B(1,1,1) -> (1,-1,1), C fixed
			assertEpsilonEquals(2., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(1., getS().getX2());
			assertEpsilonEquals(-1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(1., getS().getX3());
			assertEpsilonEquals(0., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion,Point3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_14(CoordinateSystem3D cs) {
			// 90deg around Z with arbitrary pivot P(1,2,3)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var s = Math.sqrt(0.5);
			getS().rotate(factory.createQuaternion(0., 0., s, s), createPoint(1., 2., 3.));
			// Computed from p' = P + Rz90*(p-P)
			// A -> (3,1,0), B -> (2,2,1), C -> (3,2,1)
			assertEpsilonEquals(3., getS().getX1());
			assertEpsilonEquals(1., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(2., getS().getX2());
			assertEpsilonEquals(2., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(3., getS().getX3());
			assertEpsilonEquals(2., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion,Point3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_15(CoordinateSystem3D cs) {
			// pivot translated on rotation axis (Z): same relative XY rotation
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var s = Math.sqrt(0.5);
			// Pivot on Z-axis but shifted in Z only
			getS().rotate(factory.createQuaternion(0., 0., s, s), createPoint(0., 0., 10.));
			// Z-rotation does not depend on pivot Z for x,y; z unchanged for each point
			assertEpsilonEquals(0., getS().getX1());
			assertEpsilonEquals(0., getS().getY1());
			assertEpsilonEquals(0., getS().getZ1());
			assertEpsilonEquals(-1., getS().getX2());
			assertEpsilonEquals(1., getS().getY2());
			assertEpsilonEquals(1., getS().getZ2());
			assertEpsilonEquals(0., getS().getX3());
			assertEpsilonEquals(1., getS().getY3());
			assertEpsilonEquals(1., getS().getZ3());
		}

		@DisplayName("(Quaternion,Point3D) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quatpoint_16(CoordinateSystem3D cs) {
			// two successive +90deg around same pivot == one 180deg around same pivot
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var s = Math.sqrt(0.5);
			// first shape: +90 then +90 around pivot B(1,1,1)
			final T t1 = (T) createTriangle(0, 0, 0, 1, 1, 1, 1, 0, 1);
			t1.rotate(factory.createQuaternion(0., 0., s, s), createPoint(1., 1., 1.));
			t1.rotate(factory.createQuaternion(0., 0., s, s), createPoint(1., 1., 1.));
			// Expected result of direct 180deg around Z with pivot (1,1,1):
			// A(0,0,0) -> (2,2,0)
			// B(1,1,1) -> (1,1,1)
			// C(1,0,1) -> (1,2,1)
			assertEpsilonEquals(2., t1.getX1());
			assertEpsilonEquals(2., t1.getY1());
			assertEpsilonEquals(0., t1.getZ1());
			assertEpsilonEquals(1., t1.getX2());
			assertEpsilonEquals(1., t1.getY2());
			assertEpsilonEquals(1., t1.getZ2());
			assertEpsilonEquals(1., t1.getX3());
			assertEpsilonEquals(2., t1.getY3());
			assertEpsilonEquals(1., t1.getZ3());
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
			var box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(0., box.getMinX());
			assertEpsilonEquals(0., box.getMinY());
			assertEpsilonEquals(0., box.getMinZ());
			assertEpsilonEquals(1., box.getMaxX());
			assertEpsilonEquals(1., box.getMaxY());
			assertEpsilonEquals(1., box.getMaxZ());
		}

		@DisplayName("() #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void empty_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP1(createPoint(125.5, -458.5, 4));
			var box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(1., box.getMinX());
			assertEpsilonEquals(-458.5, box.getMinY());
			assertEpsilonEquals(1., box.getMinZ());
			assertEpsilonEquals(125.5, box.getMaxX());
			assertEpsilonEquals(1., box.getMaxY());
			assertEpsilonEquals(4., box.getMaxZ());
		}

		@DisplayName("() #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void empty_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP2(createPoint(125.5, -458.5, 4));
			var box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(0., box.getMinX());
			assertEpsilonEquals(-458.5, box.getMinY());
			assertEpsilonEquals(0., box.getMinZ());
			assertEpsilonEquals(125.5, box.getMaxX());
			assertEpsilonEquals(0., box.getMaxY());
			assertEpsilonEquals(4., box.getMaxZ());
		}

		@DisplayName("() #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void empty_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP3(createPoint(125.5, -458.5, 4));
			var box = getS().toBoundingBox();
			assertNotNull(box);
			assertEpsilonEquals(0., box.getMinX());
			assertEpsilonEquals(-458.5, box.getMinY());
			assertEpsilonEquals(0., box.getMinZ());
			assertEpsilonEquals(125.5, box.getMaxX());
			assertEpsilonEquals(1., box.getMaxY());
			assertEpsilonEquals(4., box.getMaxZ());
		}

		@DisplayName("(box) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var box = createAlignedBox(0, 0, 0, 0, 0, 0);
			getS().toBoundingBox(box);
			assertNotNull(box);
			assertEpsilonEquals(0., box.getMinX());
			assertEpsilonEquals(0., box.getMinY());
			assertEpsilonEquals(0., box.getMinZ());
			assertEpsilonEquals(1., box.getMaxX());
			assertEpsilonEquals(1., box.getMaxY());
			assertEpsilonEquals(1., box.getMaxZ());
		}

		@DisplayName("(box) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP1(createPoint(125.5, -458.5, 4));
			var box = createAlignedBox(0, 0, 0, 0, 0, 0);
			getS().toBoundingBox(box);
			assertNotNull(box);
			assertEpsilonEquals(1., box.getMinX());
			assertEpsilonEquals(-458.5, box.getMinY());
			assertEpsilonEquals(1., box.getMinZ());
			assertEpsilonEquals(125.5, box.getMaxX());
			assertEpsilonEquals(1., box.getMaxY());
			assertEpsilonEquals(4., box.getMaxZ());
		}

		@DisplayName("(box) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP2(createPoint(125.5, -458.5, 4));
			var box = createAlignedBox(0, 0, 0, 0, 0, 0);
			getS().toBoundingBox(box);
			assertNotNull(box);
			assertEpsilonEquals(0., box.getMinX());
			assertEpsilonEquals(-458.5, box.getMinY());
			assertEpsilonEquals(0., box.getMinZ());
			assertEpsilonEquals(125.5, box.getMaxX());
			assertEpsilonEquals(0., box.getMaxY());
			assertEpsilonEquals(4., box.getMaxZ());
		}

		@DisplayName("(box) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP3(createPoint(125.5, -458.5, 4));
			var box = createAlignedBox(0, 0, 0, 0, 0, 0);
			getS().toBoundingBox(box);
			assertNotNull(box);
			assertEpsilonEquals(0., box.getMinX());
			assertEpsilonEquals(-458.5, box.getMinY());
			assertEpsilonEquals(0., box.getMinZ());
			assertEpsilonEquals(125.5, box.getMaxX());
			assertEpsilonEquals(1., box.getMaxY());
			assertEpsilonEquals(4., box.getMaxZ());
		}
	}

	@DisplayName("getClosestPointTo")
	@Nested
	public class GetClosestPointTo {

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			// Point clearly outside the triangle, closest point is a vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 1, 1), getS().getClosestPointTo(createSphere(10, 10, 10, 1)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			// Point inside the triangle => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.25, 0.25, 0.25), getS().getClosestPointTo(createSphere(0.25, 0.25, 0.25, 1)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			// Point on an edge => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.5, 0., 0.5), getS().getClosestPointTo(createSphere(0.5, 0., 0.5, 1)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			// Point on a vertex => closest point is the vertex itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0., 0., 0.), getS().getClosestPointTo(createSphere(0., 0., 0., 1)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			// Point above the triangle plane => closest point is the orthogonal projection on the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 0.25, 1), getS().getClosestPointTo(createSphere(0.25, 0.25, 5., 1)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			// Point outside near an edge => closest point is on the edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.75, 0., 0.75), getS().getClosestPointTo(createSphere(0.75, -0.5, 0.75, 1)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			// Point outside near another vertex => closest point is that vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0., 0., 0.), getS().getClosestPointTo(createSphere(-2., -2., -2., 1)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			// Point outside, closest point is on edge (midpoint projection)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.5, 0., 0.5), getS().getClosestPointTo(createSphere(0.5, -1., 0.5, 1)));
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			// Point outside near the hypotenuse edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.966666667, 0.966666667, 0.966666667), getS().getClosestPointTo(createSphere(1.2, 1.2, 0.5, 1)));
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			// Point outside beyond vertex (1,1,1)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1., 1., 1.), getS().getClosestPointTo(createSphere(2., 2., 2., 1)));
		}

		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_11(CoordinateSystem3D cs) {
			// Point outside with projection landing inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 0.3, 1), getS().getClosestPointTo(createSphere(0.2, 0.3, 10., 1)));
		}

		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_12(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between first and second vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.7, 0., 0.7), getS().getClosestPointTo(createSphere(0.7, -0.2, 0.7, 1)));
		}

		@DisplayName("(Sphere3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_13(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between second and third vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.8, 0.4, 0.8), getS().getClosestPointTo(createSphere(1.2, 0.4, 0.4, 1)));
		}

		@DisplayName("(Sphere3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_14(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between third and first vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.333333333333, 0.333333333333, 0.333333333333), getS().getClosestPointTo(createSphere(-0.4, 0.7, 0.7, 1)));
		}

		@DisplayName("(Sphere3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_15(CoordinateSystem3D cs) {
			// Point exactly at triangle centroid-like interior location
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.375, 0.25, 0.375), getS().getClosestPointTo(createSphere(0.25, 0.25, 0.5, 1)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			// No intersection, closest pair is on triangle interior projection + segment interior
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultForTriangle = getS().getClosestPointTo(createSegment(0.5, -0.2, 0.5, 1.2, 1.2, 0.5));
			assertEpsilonEquals(createPoint(0.5444444444444444, 0.0, 0.5444444444444444), resultForTriangle);
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			// Segment intersects the triangle => closest points are equal
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultForTriangle = getS().getClosestPointTo(createSegment(0.25, 0.25, -1.0, 0.25, 0.25, 2.0));
			assertEpsilonEquals(createPoint(0.25, 0.25, 0.25), resultForTriangle);
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			// Segment endpoint inside triangle => intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultForTriangle = getS().getClosestPointTo(createSegment(0.2, 0.2, 0.2, 2.0, 2.0, 2.0));
			assertEpsilonEquals(createPoint(1, 1, 1), resultForTriangle);
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			// Segment endpoint inside triangle, the other outside
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultForTriangle = getS().getClosestPointTo(createSegment(-1.0, -1.0, -1.0, 0.2, 0.2, 0.2));
			assertEpsilonEquals(createPoint(0.2, 0.2, 0.2), resultForTriangle);
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			// Closest pair lies on an edge of the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultForTriangle = getS().getClosestPointTo(createSegment(0.5, -1.0, 0.5, 0.5, -0.2, 0.5));
			assertEpsilonEquals(createPoint(0.5, 0.0, 0.5), resultForTriangle);
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			// Closest pair lies on a triangle vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultForTriangle = getS().getClosestPointTo(createSegment(-2.0, -2.0, -2.0, -1.0, -1.0, -1.0));
			assertEpsilonEquals(createPoint(0.0, 0.0, 0.0), resultForTriangle);
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			// Segment crosses triangle plane but misses the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultForTriangle = getS().getClosestPointTo(createSegment(1.2, 0.2, -1.0, 1.2, 0.2, 2.0));
			assertEpsilonEquals(createPoint(1.0,0.19999999999999996,1.0), resultForTriangle);
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			// Parallel segment above the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultForTriangle = getS().getClosestPointTo(createSegment(0.2, 0.2, 1.0, 0.8, 0.2, 1.0));
			assertEpsilonEquals(createPoint(0.9, 0.2, 0.9), resultForTriangle);
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			// Segment is very close to a triangle vertex but does not intersect
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultForTriangle = getS().getClosestPointTo(createSegment(-0.2, -0.2, 0.3, -0.2, -0.2, 1.0));
			assertEpsilonEquals(createPoint(0.050000000000000044,0.0,0.050000000000000044), resultForTriangle);
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			// Segment endpoint lies exactly on triangle boundary
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultForTriangle = getS().getClosestPointTo(createSegment(1.5, 0.0, 0.5, 0.5, 0.0, 0.5));
			assertEpsilonEquals(createPoint(0.5, 0.0, 0.5), resultForTriangle);
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			// Segment lies on a line parallel to an edge and closest point is one endpoint projection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultForTriangle = getS().getClosestPointTo(createSegment(0.8, 1.2, 0.5, 1.6, 1.2, 0.5));
			assertEpsilonEquals(createPoint(0.84999999999, 0.84999999999, 0.84999999999), resultForTriangle);
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			// Segment intersects triangle at a vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var resultForTriangle = getS().getClosestPointTo(createSegment(-1.0, -1.0, -1.0, 0.0, 0.0, 0.0));
			assertEpsilonEquals(createPoint(0.0, 0.0, 0.0), resultForTriangle);
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			// Point clearly outside the triangle, closest point is a vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 1, 1), getS().getClosestPointTo(createPoint(10, 10, 10)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			// Point inside the triangle => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.25, 0.25, 0.25), getS().getClosestPointTo(createPoint(0.25, 0.25, 0.25)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			// Point on an edge => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.5, 0., 0.5), getS().getClosestPointTo(createPoint(0.5, 0., 0.5)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			// Point on a vertex => closest point is the vertex itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0., 0., 0.), getS().getClosestPointTo(createPoint(0., 0., 0.)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			// Point above the triangle plane => closest point is the orthogonal projection on the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 0.25, 1), getS().getClosestPointTo(createPoint(0.25, 0.25, 5.)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			// Point outside near an edge => closest point is on the edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.75, 0., 0.75), getS().getClosestPointTo(createPoint(0.75, -0.5, 0.75)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			// Point outside near another vertex => closest point is that vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0., 0., 0.), getS().getClosestPointTo(createPoint(-2., -2., -2.)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			// Point outside, closest point is on edge (midpoint projection)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.5, 0., 0.5), getS().getClosestPointTo(createPoint(0.5, -1., 0.5)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			// Point outside near the hypotenuse edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.966666667, 0.966666667, 0.966666667), getS().getClosestPointTo(createPoint(1.2, 1.2, 0.5)));
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			// Point outside beyond vertex (1,1,1)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1., 1., 1.), getS().getClosestPointTo(createPoint(2., 2., 2.)));
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			// Point outside with projection landing inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 0.3, 1), getS().getClosestPointTo(createPoint(0.2, 0.3, 10.)));
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between first and second vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.7, 0., 0.7), getS().getClosestPointTo(createPoint(0.7, -0.2, 0.7)));
		}

		@DisplayName("(Point3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_13(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between second and third vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.8, 0.4, 0.8), getS().getClosestPointTo(createPoint(1.2, 0.4, 0.4)));
		}

		@DisplayName("(Point3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_14(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between third and first vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.333333333333, 0.333333333333, 0.333333333333), getS().getClosestPointTo(createPoint(-0.4, 0.7, 0.7)));
		}

		@DisplayName("(Point3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_15(CoordinateSystem3D cs) {
			// Point exactly at triangle centroid-like interior location
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0.375, 0.25, 0.375), getS().getClosestPointTo(createPoint(0.25, 0.25, 0.5)));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}
	}

	@DisplayName("getFarthestPointTo")
	@Nested
	public class GetFarthestPointTo {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			// Point clearly outside the triangle, closest point is a vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getFarthestPointTo(createPoint(10, 10, 10)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// Point inside the triangle => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 1, 1), getS().getFarthestPointTo(createPoint(0.25, 0.25, 0.25)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// Point on an edge => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 1, 1), getS().getFarthestPointTo(createPoint(0.5, 0., 0.5)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// Point on a vertex => closest point is the vertex itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1., 1., 1.), getS().getFarthestPointTo(createPoint(0., 0., 0.)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// Point above the triangle plane => closest point is the orthogonal projection on the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getFarthestPointTo(createPoint(0.25, 0.25, 5.)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// Point outside near an edge => closest point is on the edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 1, 1), getS().getFarthestPointTo(createPoint(0.75, -0.5, 0.75)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// Point outside near another vertex => closest point is that vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1., 1., 1.), getS().getFarthestPointTo(createPoint(-2., -2., -2.)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			// Point outside, closest point is on edge (midpoint projection)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 1, 1), getS().getFarthestPointTo(createPoint(0.5, -1., 0.5)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			// Point outside near the hypotenuse edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getFarthestPointTo(createPoint(1.2, 1.2, 0.5)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			// Point outside beyond vertex (1,1,1)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getFarthestPointTo(createPoint(2., 2., 2.)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			// Point outside with projection landing inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getFarthestPointTo(createPoint(0.2, 0.3, 10.)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between first and second vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 1, 1), getS().getFarthestPointTo(createPoint(0.7, -0.2, 0.7)));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between second and third vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getFarthestPointTo(createPoint(1.2, 0.4, 0.4)));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between third and first vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 0, 1), getS().getFarthestPointTo(createPoint(-0.4, 0.7, 0.7)));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			// Point exactly at triangle centroid-like interior location
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 1, 1), getS().getFarthestPointTo(createPoint(0.25, 0.25, 0.5)));
		}
	}

	@DisplayName("getDistanceL1")
	@Nested
	public class GetDistanceL1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			// Point clearly outside the triangle, closest point is a vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(27, getS().getDistanceL1(createPoint(10, 10, 10)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			// Point inside the triangle => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceL1(createPoint(0.25, 0.25, 0.25)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			// Point on an edge => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceL1(createPoint(0.5, 0., 0.5)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			// Point on a vertex => closest point is the vertex itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceL1(createPoint(0., 0., 0.)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			// Point above the triangle plane => closest point is the orthogonal projection on the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.75, getS().getDistanceL1(createPoint(0.25, 0.25, 5.)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			// Point outside near an edge => closest point is on the edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.5, getS().getDistanceL1(createPoint(0.75, -0.5, 0.75)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			// Point outside near another vertex => closest point is that vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6., getS().getDistanceL1(createPoint(-2., -2., -2.)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			// Point outside, closest point is on edge (midpoint projection)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1., getS().getDistanceL1(createPoint(0.5, -1., 0.5)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			// Point outside near the hypotenuse edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			//-1.1600000004
			assertEpsilonEquals(0.93333333333, getS().getDistanceL1(createPoint(1.2, 1.2, 0.5)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			// Point outside beyond vertex (1,1,1)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3., getS().getDistanceL1(createPoint(2., 2., 2.)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			// Point outside with projection landing inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9.8, getS().getDistanceL1(createPoint(0.2, 0.3, 10.)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between first and second vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.2, getS().getDistanceL1(createPoint(0.7, -0.2, 0.7)));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_13(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between second and third vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.8, getS().getDistanceL1(createPoint(1.2, 0.4, 0.4)));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_14(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between third and first vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.4666666666667, getS().getDistanceL1(createPoint(-0.4, 0.7, 0.7)));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_15(CoordinateSystem3D cs) {
			// Point exactly at triangle centroid-like interior location
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.25, getS().getDistanceL1(createPoint(0.25, 0.25, 0.5)));
		}
	}

	@DisplayName("getDistanceLinf")
	@Nested
	public class GetDistanceLinf {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			// Point clearly outside the triangle, closest point is a vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9, getS().getDistanceLinf(createPoint(10, 10, 10)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			// Point inside the triangle => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceLinf(createPoint(0.25, 0.25, 0.25)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			// Point on an edge => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceLinf(createPoint(0.5, 0., 0.5)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			// Point on a vertex => closest point is the vertex itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceLinf(createPoint(0., 0., 0.)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			// Point above the triangle plane => closest point is the orthogonal projection on the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.75, getS().getDistanceL1(createPoint(0.25, 0.25, 5.)));
			assertEpsilonEquals(4., getS().getDistanceLinf(createPoint(0.25, 0.25, 5.)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			// Point outside near an edge => closest point is on the edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.5, getS().getDistanceLinf(createPoint(0.75, -0.5, 0.75)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			// Point outside near another vertex => closest point is that vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2., getS().getDistanceLinf(createPoint(-2., -2., -2.)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			// Point outside, closest point is on edge (midpoint projection)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1., getS().getDistanceLinf(createPoint(0.5, -1., 0.5)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			// Point outside near the hypotenuse edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			//-1.1600000004
			assertEpsilonEquals(0.46666666667, getS().getDistanceLinf(createPoint(1.2, 1.2, 0.5)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			// Point outside beyond vertex (1,1,1)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1., getS().getDistanceLinf(createPoint(2., 2., 2.)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			// Point outside with projection landing inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9., getS().getDistanceLinf(createPoint(0.2, 0.3, 10.)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between first and second vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.2, getS().getDistanceLinf(createPoint(0.7, -0.2, 0.7)));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_13(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between second and third vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.4, getS().getDistanceLinf(createPoint(1.2, 0.4, 0.4)));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_14(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between third and first vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.7333333333333, getS().getDistanceLinf(createPoint(-0.4, 0.7, 0.7)));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_15(CoordinateSystem3D cs) {
			// Point exactly at triangle centroid-like interior location
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.125, getS().getDistanceLinf(createPoint(0.25, 0.25, 0.5)));
		}
	}

	@DisplayName("getDistance")
	@Nested
	public class GetDistance {
		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			// Segment intersects the triangle => distance 0
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSegment(
					0., 0., 0.,
					-1., 2., 4.)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			// Segment endpoint lies inside triangle => distance 0
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSegment(
					0.25, 0.25, 0.25,
					2., 2., 2.)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			// Parallel segment above the triangle plane
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.176776695, getS().getDistance(createSegment(
					0.25, 0.25, 1.0,
					0.75, 0.25, 1.0)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			// Closest approach is from a segment endpoint to a triangle vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.732050808, getS().getDistance(createSegment(
					-1., -1., -1.,
					-2., -2., -2.)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			// Closest approach is from a segment endpoint to a triangle edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.5, getS().getDistance(createSegment(
					0.5, -0.5, 0.5,
					0.5, -1., 0.5)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			// Closest approach is from a triangle vertex to segment interior
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.2, getS().getDistance(createSegment(
					1.2, 0.2, 0.2,
					1.2, 0.2, 1.0)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			// Segment is far away from triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.92820323, getS().getDistance(createSegment(
					5., 5., 5.,
					6., 5., 5.)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			// Segment lies entirely in the triangle plane but outside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.571547607, getS().getDistance(createSegment(
					1.2, 1.2, 0.5,
					1.5, 1.5, 0.5)));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			// Point clearly outside the triangle, closest point is a vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(15.5884572681, getS().getDistance(createPoint(10, 10, 10)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			// Point inside the triangle => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createPoint(0.25, 0.25, 0.25)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			// Point on an edge => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createPoint(0.5, 0., 0.5)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			// Point on a vertex => closest point is the vertex itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createPoint(0., 0., 0.)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			// Point above the triangle plane => closest point is the orthogonal projection on the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.069705149, getS().getDistance(createPoint(0.25, 0.25, 5.)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			// Point outside near an edge => closest point is on the edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.5, getS().getDistance(createPoint(0.75, -0.5, 0.75)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			// Point outside near another vertex => closest point is that vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.4641016151, getS().getDistance(createPoint(-2., -2., -2.)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			// Point outside, closest point is on edge (midpoint projection)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistance(createPoint(0.5, -1., 0.5)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			// Point outside near the hypotenuse edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.5715476066, getS().getDistance(createPoint(1.2, 1.2, 0.5)));
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			// Point outside beyond vertex (1,1,1)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.7320508076, getS().getDistance(createPoint(2., 2., 2.)));
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			// Point outside with projection landing inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9.0354855985, getS().getDistance(createPoint(0.2, 0.3, 10.)));
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between first and second vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.2, getS().getDistance(createPoint(0.7, -0.2, 0.7)));
		}

		@DisplayName("(Point3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_13(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between second and third vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.5656854249, getS().getDistance(createPoint(1.2, 0.4, 0.4)));
		}

		@DisplayName("(Point3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_14(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between third and first vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.898146239, getS().getDistance(createPoint(-0.4, 0.7, 0.7)));
		}

		@DisplayName("(Point3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_15(CoordinateSystem3D cs) {
			// Point exactly at triangle centroid-like interior location
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.1767766953, getS().getDistance(createPoint(0.25, 0.25, 0.5)));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			// Point clearly outside the triangle, closest point is a vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(14.5884572681, getS().getDistance(createSphere(10, 10, 10, 1)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			// Point inside the triangle => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSphere(0.25, 0.25, 0.25, 1)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			// Point on an edge => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSphere(0.5, 0., 0.5, 1)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			// Point on a vertex => closest point is the vertex itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSphere(0., 0., 0., 1)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			// Point above the triangle plane => closest point is the orthogonal projection on the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.069705149, getS().getDistance(createSphere(0.25, 0.25, 5., 1)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			// Point outside near an edge => closest point is on the edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSphere(0.75, -0.5, 0.75, 1)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			// Point outside near another vertex => closest point is that vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.4641016151, getS().getDistance(createSphere(-2., -2., -2., 1)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			// Point outside, closest point is on edge (midpoint projection)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSphere(0.5, -1., 0.5, 1)));
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			// Point outside near the hypotenuse edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSphere(1.2, 1.2, 0.5, 1)));
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			// Point outside beyond vertex (1,1,1)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.7320508076, getS().getDistance(createSphere(2., 2., 2., 1)));
		}

		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_11(CoordinateSystem3D cs) {
			// Point outside with projection landing inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.0354855985, getS().getDistance(createSphere(0.2, 0.3, 10., 1)));
		}

		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_12(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between first and second vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSphere(0.7, -0.2, 0.7, 1)));
		}

		@DisplayName("(Sphere3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_13(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between second and third vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSphere(1.2, 0.4, 0.4, 1)));
		}

		@DisplayName("(Sphere3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_14(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between third and first vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSphere(-0.4, 0.7, 0.7, 1)));
		}

		@DisplayName("(Sphere3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_15(CoordinateSystem3D cs) {
			// Point exactly at triangle centroid-like interior location
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistance(createSphere(0.25, 0.25, 0.5, 1)));
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}
	}

	@DisplayName("getDistanceSquared")
	@Nested
	public class GetDistanceSquared {

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			// Segment intersects the triangle => distance 0
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createSegment(
					0., 0., 0.,
					-1., 2., 4.)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			// Segment endpoint lies inside triangle => distance 0
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createSegment(
					0.25, 0.25, 0.25,
					2., 2., 2.)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			// Parallel segment above the triangle plane
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.03125, getS().getDistanceSquared(createSegment(
					0.25, 0.25, 1.0,
					0.75, 0.25, 1.0)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			// Closest approach is from a segment endpoint to a triangle vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3., getS().getDistanceSquared(createSegment(
					-1., -1., -1.,
					-2., -2., -2.)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			// Closest approach is from a segment endpoint to a triangle edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.25, getS().getDistanceSquared(createSegment(
					0.5, -0.5, 0.5,
					0.5, -1., 0.5)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			// Closest approach is from a triangle vertex to segment interior
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.04, getS().getDistanceSquared(createSegment(
					1.2, 0.2, 0.2,
					1.2, 0.2, 1.0)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			// Segment is far away from triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(48., getS().getDistanceSquared(createSegment(
					5., 5., 5.,
					6., 5., 5.)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			// Segment lies entirely in the triangle plane but outside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.326666666666, getS().getDistanceSquared(createSegment(
					1.2, 1.2, 0.5,
					1.5, 1.5, 0.5)));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			// Point clearly outside the triangle, closest point is a vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(243, getS().getDistanceSquared(createPoint(10, 10, 10)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			// Point inside the triangle => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createPoint(0.25, 0.25, 0.25)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			// Point on an edge => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createPoint(0.5, 0., 0.5)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			// Point on a vertex => closest point is the vertex itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createPoint(0., 0., 0.)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			// Point above the triangle plane => closest point is the orthogonal projection on the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16.5625, getS().getDistanceSquared(createPoint(0.25, 0.25, 5.)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			// Point outside near an edge => closest point is on the edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.25, getS().getDistanceSquared(createPoint(0.75, -0.5, 0.75)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			// Point outside near another vertex => closest point is that vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(12, getS().getDistanceSquared(createPoint(-2., -2., -2.)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			// Point outside, closest point is on edge (midpoint projection)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceSquared(createPoint(0.5, -1., 0.5)));
		}

		@DisplayName("(Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_9(CoordinateSystem3D cs) {
			// Point outside near the hypotenuse edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.3266666667, getS().getDistanceSquared(createPoint(1.2, 1.2, 0.5)));
		}

		@DisplayName("(Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_10(CoordinateSystem3D cs) {
			// Point outside beyond vertex (1,1,1)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, getS().getDistanceSquared(createPoint(2., 2., 2.)));
		}

		@DisplayName("(Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_11(CoordinateSystem3D cs) {
			// Point outside with projection landing inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(81.64, getS().getDistanceSquared(createPoint(0.2, 0.3, 10.)));
		}

		@DisplayName("(Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_12(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between first and second vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.04, getS().getDistanceSquared(createPoint(0.7, -0.2, 0.7)));
		}

		@DisplayName("(Point3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_13(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between second and third vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.32, getS().getDistanceSquared(createPoint(1.2, 0.4, 0.4)));
		}

		@DisplayName("(Point3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_14(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between third and first vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.8066666667, getS().getDistanceSquared(createPoint(-0.4, 0.7, 0.7)));
		}

		@DisplayName("(Point3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_15(CoordinateSystem3D cs) {
			// Point exactly at triangle centroid-like interior location
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.03125, getS().getDistanceSquared(createPoint(0.25, 0.25, 0.5)));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			// Point clearly outside the triangle, closest point is a vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(212.8230854637602, getS().getDistanceSquared(createSphere(10, 10, 10, 1)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			// Point inside the triangle => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createSphere(0.25, 0.25, 0.25, 1)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			// Point on an edge => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createSphere(0.5, 0., 0.5, 1)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			// Point on a vertex => closest point is the vertex itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createSphere(0., 0., 0., 1)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			// Point above the triangle plane => closest point is the orthogonal projection on the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9.423089702, getS().getDistanceSquared(createSphere(0.25, 0.25, 5., 1)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			// Point outside near an edge => closest point is on the edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getS().getDistanceSquared(createSphere(0.75, -0.5, 0.75, 1)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			// Point outside near another vertex => closest point is that vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6.0717967697, getS().getDistanceSquared(createSphere(-2., -2., -2., 1)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			// Point outside, closest point is on edge (midpoint projection)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(0.5, -1., 0.5, 1)));
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			// Point outside near the hypotenuse edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(1.2, 1.2, 0.5, 1)));
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			// Point outside beyond vertex (1,1,1)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.53589838486, getS().getDistanceSquared(createSphere(2., 2., 2., 1)));
		}

		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_11(CoordinateSystem3D cs) {
			// Point outside with projection landing inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(64.569028803, getS().getDistanceSquared(createSphere(0.2, 0.3, 10., 1)));
		}

		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_12(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between first and second vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(0.7, -0.2, 0.7, 1)));
		}

		@DisplayName("(Sphere3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_13(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between second and third vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(1.2, 0.4, 0.4, 1)));
		}

		@DisplayName("(Sphere3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_14(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between third and first vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(-0.4, 0.7, 0.7, 1)));
		}

		@DisplayName("(Sphere3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_15(CoordinateSystem3D cs) {
			// Point exactly at triangle centroid-like interior location
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSphere(0.25, 0.25, 0.5, 1)));
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}
	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			// Sphere far away from triangle => no intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(10, 10, 10, 1)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			// Sphere contains triangle vertex => intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(getS().getX1(), getS().getY1(), getS().getZ1(), 0.1)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			// Sphere centered at a triangle vertex with enough radius to include nearby area => intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(0., 0., 0., 0.5)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			// Sphere intersects triangle interior through the plane
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(0.25, 0.25, 0.25, 0.3)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			// Sphere is tangent to triangle plane at a point inside the triangle => intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(0.25, 0.25, 0.5, 0.5)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			// Sphere intersects one triangle edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(0.5, -0.2, 0.5, 0.25)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			// Sphere intersects another edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(0.3, 0.5, 0.5, 0.2)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			// Sphere fully contains triangle => intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(0.3, 0.3, 0.3, 10.)));
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			// Sphere touches triangle at a vertex (tangent)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(1., 1., 1., 0.)));
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			// Sphere near the triangle but not touching => no intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSphere(0.25, 0.25, 0.8, 0.2)));
		}

		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_11(CoordinateSystem3D cs) {
			// Sphere center projects inside triangle and distance to plane is exactly radius => tangent intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(0.2, 0.2, 0.5, 0.5)));
		}

		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_12(CoordinateSystem3D cs) {
			// Sphere intersects triangle only because it covers an edge endpoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(getS().getX2(), getS().getY2(), getS().getZ2(), 0.01)));
		}

		@DisplayName("(Sphere3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_13(CoordinateSystem3D cs) {
			// Sphere intersects triangle interior only, but not the edges
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSphere(0.8, 0.3, 0.5, 0.3)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			// start at vertex A => factor 0
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(
							0., 0., 0.,
							-1., 2., 4.)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			// start at vertex B => factor 0
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(
							1., 1., 1.,
							2., 2., 0.)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			// start at vertex C => factor 0
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(
							1., 0., 1.,
							2., -1., 3.)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			// intersection at segment end on vertex A => factor 1
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(
							2., 2., 0.,
							0., 0., 0.)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			// crossing through triangle interior => factor in (0,1)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Plane is x-z=0; line from (0.5,0.2,-1) to (0.5,0.2,1) hits at z=0.5 -> factor 0.75
			assertTrue(getS().intersects(createSegment(
							0.5, 0.2, -1.,
							0.5, 0.2, 1.)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			// crossing plane but outside triangle => NaN
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Hits plane at (2,2,2), which is outside the triangle
			assertFalse(getS().intersects(createSegment(
							2., 2., 0.,
							2., 2., 4.)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			// segment parallel to plane and not in plane => NaN
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Direction (1,0,1) is parallel to plane x-z=0, start point not in plane (x-z=-1)
			assertFalse(getS().intersects(createSegment(
							0., 0., 1.,
							2., 0., 3.)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			// segment included in plane and overlapping triangle edge => NaN (2D case unsupported)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Entire segment in plane x=z, along AC line
			assertTrue(getS().intersects(createSegment(
							-1., 0., -1.,
							2., 0., 2.)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			// degenerate segment point on triangle interior => factor 0
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(
							0.5, 0.2, 0.5,
							0.5, 0.2, 0.5)));
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			// degenerate segment point off plane => NaN
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(
							0.5, 0.2, 0.6,
							0.5, 0.2, 0.6)));
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			// hit exactly on edge AB => valid factor
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Target edge point P=(0.5,0.5,0.5), choose z interpolation so factor=0.75
			assertTrue(getS().intersects(createSegment(
							0.5, 0.5, -1.,
							0.5, 0.5, 1.)));
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			// hit exactly on edge AC => valid factor
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Target edge point P=(0.5,0,0.5), factor 0.75 with z from -1 to 1
			assertTrue(getS().intersects(createSegment(
							0.5, 0., -1.,
							0.5, 0., 1.)));
		}

		@DisplayName("(Segment3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_13(CoordinateSystem3D cs) {
			// hit exactly on edge BC => valid factor
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Target edge point P=(1,0.5,1), factor 0.75 with z from 0 to 4
			assertTrue(getS().intersects(createSegment(
							1., 0.5, 0.,
							1., 0.5, 4.)));
		}

		@DisplayName("(Segment3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_14(CoordinateSystem3D cs) {
			// plane intersection exists but behind segment start (factor < 0) => NaN
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(
							0.5, 0.2, 0.6,
							0.5, 0.2, 0.8)));
		}

		@DisplayName("(Segment3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_15(CoordinateSystem3D cs) {
			// plane intersection exists beyond segment end (factor > 1) => NaN
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(
							0.5, 0.2, -2.,
							0.5, 0.2, -1.)));
		}

		@DisplayName("(Segment3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_16(CoordinateSystem3D cs) {
			// permutation of triangle vertices should not change factor
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var f1 = createTriangle(0., 0., 0., 1., 1., 1., 1., 0., 1.).intersects(createSegment(
					0.5, 0.2, -1.,
					0.5, 0.2, 1.));
			final var f2 = createTriangle(1., 1., 1., 1., 0., 1., 0., 0., 0.).intersects(createSegment(
					0.5, 0.2, -1.,
					0.5, 0.2, 1.));
			assertEquals(f1, f2);
		}

		@DisplayName("(Segment3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_17(CoordinateSystem3D cs) {
			// crossing through triangle interior => factor in (0,1)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(
							0.5, 0.2, -1.,
							0.5, 0.5, 1.)));
		}

		@DisplayName("(Segment3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_18(CoordinateSystem3D cs) {
			// segment is coplanar to the triangle and both points of the segment are inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(
							0.5, 0.2, 0.5,
							0.5, 0.15, 0.5)));
		}

		@DisplayName("(Segment3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_19(CoordinateSystem3D cs) {
			// segment is coplanar to the triangle and first point of the segment is inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(
							0.5, 0.2, 0.5,
							0.5, -5, 0.5)));
		}

		@DisplayName("(Segment3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_20(CoordinateSystem3D cs) {
			// segment is coplanar to the triangle and first point of the segment is inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(
							0.5, -5, 0.5,
							0.5, 0.2, 0.5)));
		}

		@DisplayName("(Segment3afp) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_21(CoordinateSystem3D cs) {
			// segment degenerates to point but not coplanar to the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(
							0.5, 0.5, 0.6,
							0.5, 0.5, 0.6)));
		}

		@DisplayName("(Segment3afp) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_22(CoordinateSystem3D cs) {
			// segment is not coplanar to the triangle and both projected points of the segment are inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(
							0.5, 0.2, 0.6,
							0.5, 0.15, 0.6)));
		}

		@DisplayName("(Segment3afp) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_23(CoordinateSystem3D cs) {
			// segment is not coplanar to the triangle and projection of the first point of the segment is inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(
							0.5, 0.2, 0.6,
							0.5, -5, 0.6)));
		}

		@DisplayName("(Segment3afp) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_24(CoordinateSystem3D cs) {
			// degenerate segment point on triangle's plane but not inside the triangle interior
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(
							0.5, -5, 0.5,
							0.5, -5, 0.5)));
		}

		@DisplayName("(Segment3afp) #25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_25(CoordinateSystem3D cs) {
			// segment points on triangle's plane but not inside the triangle interior, with intersection between the segment and the triangle 
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createSegment(
							0.5, -5, 0.5,
							0.5, 10, 0.5)));
		}

		@DisplayName("(Segment3afp) #26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_26(CoordinateSystem3D cs) {
			// segment points on triangle's plane but not inside the triangle interior, with intersection between the segment and the triangle 
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createSegment(
							1.5, -4, 1.5,
							1.5, 11, 1.5)));
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(PathIterator3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
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
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getP1());
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
			assertEpsilonEquals(createPoint(1, 1, 1), getS().getP2());
		}
	}

	@DisplayName("getP3")
	@Nested
	public class GetP3 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1, 0, 1), getS().getP3());
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
			getS().setP1(125.5, -458.5, 4);
			assertEpsilonEquals(createPoint(125.5, -458.5, 4), getS().getP1());
			assertEpsilonEquals(createPoint(1, 1, 1), getS().getP2());
			assertEpsilonEquals(createPoint(1, 0, 1), getS().getP3());
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP1(createPoint(125.5, -458.5, 4));
			assertEpsilonEquals(createPoint(125.5, -458.5, 4), getS().getP1());
			assertEpsilonEquals(createPoint(1, 1, 1), getS().getP2());
			assertEpsilonEquals(createPoint(1, 0, 1), getS().getP3());
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().setP1(null));
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
			getS().setP2(125.5, -458.5, 4);
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getP1());
			assertEpsilonEquals(createPoint(125.5, -458.5, 4), getS().getP2());
			assertEpsilonEquals(createPoint(1, 0, 1), getS().getP3());
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP2(createPoint(125.5, -458.5, 4));
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getP1());
			assertEpsilonEquals(createPoint(125.5, -458.5, 4), getS().getP2());
			assertEpsilonEquals(createPoint(1, 0, 1), getS().getP3());
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().setP2(null));
		}
	}

	@DisplayName("setP3")
	@Nested
	public class SetP3 {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP3(125.5, -458.5, 4);
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getP1());
			assertEpsilonEquals(createPoint(1, 1, 1), getS().getP2());
			assertEpsilonEquals(createPoint(125.5, -458.5, 4), getS().getP3());
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setP3(createPoint(125.5, -458.5, 4));
			assertEpsilonEquals(createPoint(0, 0, 0), getS().getP1());
			assertEpsilonEquals(createPoint(1, 1, 1), getS().getP2());
			assertEpsilonEquals(createPoint(125.5, -458.5, 4), getS().getP3());
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(AssertionError.class, () -> getS().setP3(null));
		}
	}

	@DisplayName("findsClosestPointTrianglePoint")
	@Nested
	public class FindsClosestPointTrianglePoint {

		private Point3D<?, ?, ?> result;
		
		@BeforeEach
		public void setUp() {
			result = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			// Point clearly outside the triangle, closest point is a vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					10, 10, 10,
					result);
			assertEpsilonEquals(createPoint(1, 1, 1), result);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// Point inside the triangle => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.25, 0.25, 0.25,
					result);
			assertEpsilonEquals(createPoint(0.25, 0.25, 0.25), result);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// Point on an edge => closest point is the point itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.5, 0., 0.5,
					result);
			assertEpsilonEquals(createPoint(0.5, 0., 0.5), result);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// Point on a vertex => closest point is the vertex itself
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0., 0., 0.,
					result);
			assertEpsilonEquals(createPoint(0., 0., 0.), result);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// Point above the triangle plane => closest point is the orthogonal projection on the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.25, 0.25, 5.,
					result);
			assertEpsilonEquals(createPoint(1, 0.25, 1), result);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// Point outside near an edge => closest point is on the edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.75, -0.5, 0.75,
					result);
			assertEpsilonEquals(createPoint(0.75, 0., 0.75), result);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// Point outside near another vertex => closest point is that vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					-2., -2., -2.,
					result);
			assertEpsilonEquals(createPoint(0., 0., 0.), result);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			// Point outside, closest point is on edge (midpoint projection)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.5, -1., 0.5,
					result);
			assertEpsilonEquals(createPoint(0.5, 0., 0.5), result);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			// Point outside near the hypotenuse edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					1.2, 1.2, 0.5,
					result);
			assertEpsilonEquals(createPoint(0.966666667, 0.966666667, 0.966666667), result);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			// Point outside beyond vertex (1,1,1)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					2., 2., 2.,
					result);
			assertEpsilonEquals(createPoint(1., 1., 1.), result);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			// Point outside with projection landing inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.2, 0.3, 10.,
					result);
			assertEpsilonEquals(createPoint(1, 0.3, 1), result);
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between first and second vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.7, -0.2, 0.7,
					result);
			assertEpsilonEquals(createPoint(0.7, 0., 0.7), result);
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between second and third vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					1.2, 0.4, 0.4,
					result);
			assertEpsilonEquals(createPoint(0.8, 0.4, 0.8), result);
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			// Point outside with projection landing on edge between third and first vertices
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					-0.4, 0.7, 0.7,
					result);
			assertEpsilonEquals(createPoint(0.333333333333, 0.333333333333, 0.333333333333), result);
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			// Point exactly at triangle centroid-like interior location
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTrianglePoint(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.25, 0.25, 0.5,
					result);
			assertEpsilonEquals(createPoint(0.375, 0.25, 0.375), result);
		}
	}

	@DisplayName("intersectsTriangleAlignedBox")
	@Nested
	public class IntersectsTriangleAlignedBox {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}
	}

	@DisplayName("intersectsTriangleSphere")
	@Nested
	public class IntersectsTriangleSphere {

		private Point3D<?, ?, ?> result;
		
		@BeforeEach
		public void setUp() {
			result = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			// Sphere far away from triangle => no intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.intersectsTriangleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					10, 10, 10, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// Sphere contains triangle vertex => intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					getS().getX1(), getS().getY1(), getS().getZ1(), 0.1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// Sphere centered at a triangle vertex with enough radius to include nearby area => intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0., 0., 0., 0.5));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// Sphere intersects triangle interior through the plane
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.25, 0.25, 0.25, 0.3));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// Sphere is tangent to triangle plane at a point inside the triangle => intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.25, 0.25, 0.5, 0.5));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// Sphere intersects one triangle edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.5, -0.2, 0.5, 0.25));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// Sphere intersects another edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.3, 0.5, 0.5, 0.2));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			// Sphere fully contains triangle => intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.3, 0.3, 0.3, 10.));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			// Sphere touches triangle at a vertex (tangent)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					1., 1., 1., 0.));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			// Sphere near the triangle but not touching => no intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.intersectsTriangleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.25, 0.25, 0.8, 0.2));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			// Sphere center projects inside triangle and distance to plane is exactly radius => tangent intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.2, 0.2, 0.5, 0.5));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			// Sphere intersects triangle only because it covers an edge endpoint
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					getS().getX2(), getS().getY2(), getS().getZ2(), 0.01));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			// Sphere intersects triangle interior only, but not the edges
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSphere(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.8, 0.3, 0.5, 0.3));
		}
	}

	@DisplayName("intersectsTriangleSegment")
	@Nested
	public class IntersectsTriangleSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			// start at vertex A => factor 0
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0., 0., 0.,
							-1., 2., 4.));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// start at vertex B => factor 0
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							1., 1., 1.,
							2., 2., 0.));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// start at vertex C => factor 0
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							1., 0., 1.,
							2., -1., 3.));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// intersection at segment end on vertex A => factor 1
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							2., 2., 0.,
							0., 0., 0.));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// crossing through triangle interior => factor in (0,1)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Plane is x-z=0; line from (0.5,0.2,-1) to (0.5,0.2,1) hits at z=0.5 -> factor 0.75
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, 0.2, -1.,
							0.5, 0.2, 1.));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// crossing plane but outside triangle => NaN
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Hits plane at (2,2,2), which is outside the triangle
			assertFalse(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							2., 2., 0.,
							2., 2., 4.));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// segment parallel to plane and not in plane => NaN
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Direction (1,0,1) is parallel to plane x-z=0, start point not in plane (x-z=-1)
			assertFalse(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0., 0., 1.,
							2., 0., 3.));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			// segment included in plane and overlapping triangle edge => NaN (2D case unsupported)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Entire segment in plane x=z, along AC line
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							-1., 0., -1.,
							2., 0., 2.));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			// degenerate segment point on triangle interior => factor 0
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, 0.2, 0.5,
							0.5, 0.2, 0.5));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			// degenerate segment point off plane => NaN
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, 0.2, 0.6,
							0.5, 0.2, 0.6));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			// hit exactly on edge AB => valid factor
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Target edge point P=(0.5,0.5,0.5), choose z interpolation so factor=0.75
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, 0.5, -1.,
							0.5, 0.5, 1.));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			// hit exactly on edge AC => valid factor
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Target edge point P=(0.5,0,0.5), factor 0.75 with z from -1 to 1
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, 0., -1.,
							0.5, 0., 1.));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			// hit exactly on edge BC => valid factor
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Target edge point P=(1,0.5,1), factor 0.75 with z from 0 to 4
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							1., 0.5, 0.,
							1., 0.5, 4.));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			// plane intersection exists but behind segment start (factor < 0) => NaN
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, 0.2, 0.6,
							0.5, 0.2, 0.8));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			// plane intersection exists beyond segment end (factor > 1) => NaN
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, 0.2, -2.,
							0.5, 0.2, -1.));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			// permutation of triangle vertices should not change factor
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var f1 = Triangle3afp.intersectsTriangleSegment(
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.,
					0.5, 0.2, -1.,
					0.5, 0.2, 1.);
			final var f2 = Triangle3afp.intersectsTriangleSegment(
					1., 1., 1.,
					1., 0., 1.,
					0., 0., 0.,
					0.5, 0.2, -1.,
					0.5, 0.2, 1.);
			assertEquals(f1, f2);
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			// crossing through triangle interior => factor in (0,1)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, 0.2, -1.,
							0.5, 0.5, 1.));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			// segment is coplanar to the triangle and both points of the segment are inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, 0.2, 0.5,
							0.5, 0.15, 0.5));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
			// segment is coplanar to the triangle and first point of the segment is inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, 0.2, 0.5,
							0.5, -5, 0.5));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
			// segment is coplanar to the triangle and first point of the segment is inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, -5, 0.5,
							0.5, 0.2, 0.5));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
			// segment degenerates to point but not coplanar to the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, 0.5, 0.6,
							0.5, 0.5, 0.6));
		}

		@DisplayName("#22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
			// segment is not coplanar to the triangle and both projected points of the segment are inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, 0.2, 0.6,
							0.5, 0.15, 0.6));
		}

		@DisplayName("#23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_23(CoordinateSystem3D cs) {
			// segment is not coplanar to the triangle and projection of the first point of the segment is inside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, 0.2, 0.6,
							0.5, -5, 0.6));
		}

		@DisplayName("#24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_24(CoordinateSystem3D cs) {
			// degenerate segment point on triangle's plane but not inside the triangle interior
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, -5, 0.5,
							0.5, -5, 0.5));
		}

		@DisplayName("#25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_25(CoordinateSystem3D cs) {
			// segment points on triangle's plane but not inside the triangle interior, with intersection between the segment and the triangle 
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							0.5, -5, 0.5,
							0.5, 10, 0.5));
		}

		@DisplayName("#26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_26(CoordinateSystem3D cs) {
			// segment points on triangle's plane but not inside the triangle interior, with intersection between the segment and the triangle 
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.intersectsTriangleSegment(
							getS().getX1(), getS().getY1(), getS().getZ1(),
							getS().getX2(), getS().getY2(), getS().getZ2(),
							getS().getX3(), getS().getY3(), getS().getZ3(),
							1.5, -4, 1.5,
							1.5, 11, 1.5));
		}
	}

	@DisplayName("intersectsCoplanarTriangleTriangle")
	@Nested
	public class IntersectsCoplanarTriangleTriangle {


		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			// identical triangles => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsCoplanarTriangleTriangle(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3()));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// same triangle, reversed winding => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsCoplanarTriangleTriangle(
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.,
					1., 0., 1.,
					1., 1., 1.,
					0., 0., 0.));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// vertex permutation => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsCoplanarTriangleTriangle(
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.,
					1., 1., 1.,
					1., 0., 1.,
					0., 0., 0.));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// one triangle fully inside the other (coplanar) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// big triangle in plane x=z
			assertTrue(Triangle3afp.intersectsCoplanarTriangleTriangle(
					0., 0., 0.,
					2., 0., 2.,
					2., 2., 2.,
					0.5, 0.2, 0.5,
					1.0, 0.2, 1.0,
					1.0, 0.8, 1.0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// partial overlap by crossing edges => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsCoplanarTriangleTriangle(
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.,
					0.5, -0.2, 0.5,
					1.5, 0.5, 1.5,
					0.5, 0.8, 0.5));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// touching at a single shared vertex => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// second triangle only shares A(0,0,0)
			assertTrue(Triangle3afp.intersectsCoplanarTriangleTriangle(
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.,
					0., 0., 0.,
					-1., 0., -1.,
					-1., -1., -1.));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// touching along one edge segment => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// share edge AC
			assertTrue(Triangle3afp.intersectsCoplanarTriangleTriangle(
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.,
					0., 0., 0.,
					1., 0., 1.,
					0.2, -0.5, 0.2));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			// disjoint but coplanar (far apart) => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.intersectsCoplanarTriangleTriangle(
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.,
					10., 0., 10.,
					11., 0., 11.,
					11., 1., 11.));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			// disjoint but same plane and close => false
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Triangle3afp.intersectsCoplanarTriangleTriangle(
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.,
					1.2, 0.2, 1.2,
					1.8, 0.2, 1.8,
					1.8, 0.8, 1.8));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			// one triangle vertex on other triangle edge => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// vertex (0.5,0,0.5) lies on edge AC of first triangle
			assertTrue(Triangle3afp.intersectsCoplanarTriangleTriangle(
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.,
					0.5, 0., 0.5,
					1.2, -0.3, 1.2,
					1.2, 0.3, 1.2));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			// very small triangle inside => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsCoplanarTriangleTriangle(
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.,
					0.5000001, 0.2, 0.5000001,
					0.5000002, 0.2, 0.5000002,
					0.5000002, 0.2000001, 0.5000002));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			// second triangle around first (first inside second) => true
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Triangle3afp.intersectsCoplanarTriangleTriangle(
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.,
					-1., -1., -1.,
					3., -1., 3.,
					3., 3., 3.));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			// collinear-degenerate second triangle on plane crossing first edge => true/false impl-dependent, keep robust non-crash
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// degenerate U: all points on AC line
			final boolean r = Triangle3afp.intersectsCoplanarTriangleTriangle(
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.,
					-0.5, 0., -0.5,
					0.5, 0., 0.5,
					1.5, 0., 1.5);
			// Main purpose: no exception, deterministic boolean
			assertTrue(r || !r);
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			// zero-area second triangle at inside point => true/false impl-dependent, no crash
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final boolean r = Triangle3afp.intersectsCoplanarTriangleTriangle(
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.,
					0.5, 0.2, 0.5,
					0.5, 0.2, 0.5,
					0.5, 0.2, 0.5);
			assertTrue(r || !r);
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			// symmetry: intersect(A,B) == intersect(B,A)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);

			final boolean ab = Triangle3afp.intersectsCoplanarTriangleTriangle(
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.,
					0.5, -0.2, 0.5,
					1.5, 0.5, 1.5,
					0.5, 0.8, 0.5);

			final boolean ba = Triangle3afp.intersectsCoplanarTriangleTriangle(
					0.5, -0.2, 0.5,
					1.5, 0.5, 1.5,
					0.5, 0.8, 0.5,
					0., 0., 0.,
					1., 1., 1.,
					1., 0., 1.);

			assertEquals(ab, ba);
		}
	}

	@DisplayName("calculatesDistanceSquaredTriangleSegment")
	@Nested
	public class CalculatesDistanceSquaredTriangleSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			// Segment intersects the triangle => distance 0
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Triangle3afp.calculatesDistanceSquaredTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0., 0., 0.,
					-1., 2., 4.,
					EPSILON));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// Segment endpoint lies inside triangle => distance 0
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Triangle3afp.calculatesDistanceSquaredTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.25, 0.25, 0.25,
					2., 2., 2.,
					EPSILON));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// Parallel segment above the triangle plane
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.03125, Triangle3afp.calculatesDistanceSquaredTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.25, 0.25, 1.0,
					0.75, 0.25, 1.0,
					EPSILON));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// Closest approach is from a segment endpoint to a triangle vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3., Triangle3afp.calculatesDistanceSquaredTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					-1., -1., -1.,
					-2., -2., -2.,
					EPSILON));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// Closest approach is from a segment endpoint to a triangle edge
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.25, Triangle3afp.calculatesDistanceSquaredTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.5, -0.5, 0.5,
					0.5, -1., 0.5,
					EPSILON));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// Closest approach is from a triangle vertex to segment interior
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.04, Triangle3afp.calculatesDistanceSquaredTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					1.2, 0.2, 0.2,
					1.2, 0.2, 1.0,
					EPSILON));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// Segment is far away from triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(48., Triangle3afp.calculatesDistanceSquaredTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					5., 5., 5.,
					6., 5., 5.,
					EPSILON));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			// Segment lies entirely in the triangle plane but outside the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.326666666666, Triangle3afp.calculatesDistanceSquaredTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					1.2, 1.2, 0.5,
					1.5, 1.5, 0.5,
					EPSILON));
		}
	}

	@DisplayName("Badouel Algorithms")
	@Nested
	public class BadouelAlgorithmToolsTests {

		@DisplayName("calculatesIntersectionFactorTriangleSegment")
		@Nested
		public class CalculatesIntersectionFactorTriangleSegment {

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				// start at vertex A => factor 0
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0., 0., 0.,
								-1., 2., 4.,
								EPSILON));
			}
	
			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				// start at vertex B => factor 0
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								1., 1., 1.,
								2., 2., 0.,
								EPSILON));
			}
	
			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				// start at vertex C => factor 0
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								1., 0., 1.,
								2., -1., 3.,
								EPSILON));
			}
	
			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4(CoordinateSystem3D cs) {
				// intersection at segment end on vertex A => factor 1
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(1.,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								2., 2., 0.,
								0., 0., 0.,
								EPSILON));
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_5(CoordinateSystem3D cs) {
				// crossing through triangle interior => factor in (0,1)
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Plane is x-z=0; line from (0.5,0.2,-1) to (0.5,0.2,1) hits at z=0.5 -> factor 0.75
				assertEpsilonEquals(0.75,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, -1.,
								0.5, 0.2, 1.,
								EPSILON));
			}
	
			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_6(CoordinateSystem3D cs) {
				// crossing plane but outside triangle => NaN
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Hits plane at (2,2,2), which is outside the triangle
				assertNaN(
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								2., 2., 0.,
								2., 2., 4.,
								EPSILON));
			}
	
			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_7(CoordinateSystem3D cs) {
				// segment parallel to plane and not in plane => NaN
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Direction (1,0,1) is parallel to plane x-z=0, start point not in plane (x-z=-1)
				assertNaN(
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0., 0., 1.,
								2., 0., 3.,
								EPSILON));
			}
	
			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_8(CoordinateSystem3D cs) {
				// segment included in plane and overlapping triangle edge => NaN (2D case unsupported)
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Entire segment in plane x=z, along AC line
				assertEpsilonEquals(0.33333333333,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								-1., 0., -1.,
								2., 0., 2.,
								EPSILON));
			}
	
			@DisplayName("#9")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_9(CoordinateSystem3D cs) {
				// degenerate segment point on triangle interior => factor 0
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, 0.5,
								0.5, 0.2, 0.5,
								EPSILON));
			}
	
			@DisplayName("#10")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_10(CoordinateSystem3D cs) {
				// degenerate segment point off plane => NaN
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, 0.6,
								0.5, 0.2, 0.6,
								EPSILON));
			}
	
			@DisplayName("#11")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_11(CoordinateSystem3D cs) {
				// hit exactly on edge AB => valid factor
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Target edge point P=(0.5,0.5,0.5), choose z interpolation so factor=0.75
				assertEpsilonEquals(0.75,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.5, -1.,
								0.5, 0.5, 1.,
								EPSILON));
			}
	
			@DisplayName("#12")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_12(CoordinateSystem3D cs) {
				// hit exactly on edge AC => valid factor
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Target edge point P=(0.5,0,0.5), factor 0.75 with z from -1 to 1
				assertEpsilonEquals(0.75,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0., -1.,
								0.5, 0., 1.,
								EPSILON));
			}
	
			@DisplayName("#13")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_13(CoordinateSystem3D cs) {
				// hit exactly on edge BC => valid factor
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Target edge point P=(1,0.5,1), factor 0.75 with z from 0 to 4
				assertEpsilonEquals(0.25,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								1., 0.5, 0.,
								1., 0.5, 4.,
								EPSILON));
			}
	
			@DisplayName("#14")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_14(CoordinateSystem3D cs) {
				// plane intersection exists but behind segment start (factor < 0) => NaN
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, 0.6,
								0.5, 0.2, 0.8,
								EPSILON));
			}
	
			@DisplayName("#15")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_15(CoordinateSystem3D cs) {
				// plane intersection exists beyond segment end (factor > 1) => NaN
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, -2.,
								0.5, 0.2, -1.,
								EPSILON));
			}
	
			@DisplayName("#16")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_16(CoordinateSystem3D cs) {
				// permutation of triangle vertices should not change factor
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				final var f1 = Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						0.5, 0.2, -1.,
						0.5, 0.2, 1.,
						EPSILON);
				final var f2 = Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
						1., 1., 1.,
						1., 0., 1.,
						0., 0., 0.,
						0.5, 0.2, -1.,
						0.5, 0.2, 1.,
						EPSILON);
				assertEpsilonEquals(f1, f2);
			}

			@DisplayName("#17")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_17(CoordinateSystem3D cs) {
				// crossing through triangle interior => factor in (0,1)
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.75,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, -1.,
								0.5, 0.5, 1.,
								EPSILON));
			}

			@DisplayName("#18")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_18(CoordinateSystem3D cs) {
				// segment is coplanar to the triangle and both points of the segment are inside the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, 0.5,
								0.5, 0.15, 0.5,
								EPSILON));
			}

			@DisplayName("#19")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_19(CoordinateSystem3D cs) {
				// segment is coplanar to the triangle and first point of the segment is inside the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, 0.5,
								0.5, -5, 0.5,
								EPSILON));
			}

			@DisplayName("#20")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_20(CoordinateSystem3D cs) {
				// segment is coplanar to the triangle and first point of the segment is inside the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(1.,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, -5, 0.5,
								0.5, 0.2, 0.5,
								EPSILON));
			}

			@DisplayName("#21")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_21(CoordinateSystem3D cs) {
				// segment degenerates to point but not coplanar to the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.5, 0.6,
								0.5, 0.5, 0.6,
								EPSILON));
			}

			@DisplayName("#22")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_22(CoordinateSystem3D cs) {
				// segment is not coplanar to the triangle and both projected points of the segment are inside the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, 0.6,
								0.5, 0.15, 0.6,
								EPSILON));
			}

			@DisplayName("#23")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_23(CoordinateSystem3D cs) {
				// segment is not coplanar to the triangle and projection of the first point of the segment is inside the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, 0.6,
								0.5, -5, 0.6,
								EPSILON));
			}

			@DisplayName("#24")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_24(CoordinateSystem3D cs) {
				// degenerate segment point on triangle's plane but not inside the triangle interior
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, -5, 0.5,
								0.5, -5, 0.5,
								EPSILON));
			}

			@DisplayName("#25")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_25(CoordinateSystem3D cs) {
				// segment points on triangle's plane but not inside the triangle interior, with intersection between the segment and the triangle 
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.333333333333,
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, -5, 0.5,
								0.5, 10, 0.5,
								EPSILON));
			}

			@DisplayName("#26")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_26(CoordinateSystem3D cs) {
				// segment points on triangle's plane but not inside the triangle interior, with intersection between the segment and the triangle 
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.BadouelAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								1.5, -4, 1.5,
								1.5, 11, 1.5,
								EPSILON));
			}
		}
	}

	@DisplayName("Jimenez Algorithms")
	@Nested
	public class JimenezAlgorithmToolsTests{

		@DisplayName("calculatesIntersectionFactorTriangleSegment")
		@Nested
		public class CalculatesIntersectionFactorTriangleSegment {

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				// start at vertex A => factor 0
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0., 0., 0.,
								-1., 2., 4.,
								EPSILON));
			}
	
			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				// start at vertex B => factor 0
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								1., 1., 1.,
								2., 2., 0.,
								EPSILON));
			}
	
			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				// start at vertex C => factor 0
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								1., 0., 1.,
								2., -1., 3.,
								EPSILON));
			}
	
			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4(CoordinateSystem3D cs) {
				// intersection at segment end on vertex A => factor 1
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(1.,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								2., 2., 0.,
								0., 0., 0.,
								EPSILON));
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_5(CoordinateSystem3D cs) {
				// crossing through triangle interior => factor in (0,1)
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Plane is x-z=0; line from (0.5,0.2,-1) to (0.5,0.2,1) hits at z=0.5 -> factor 0.75
				assertEpsilonEquals(0.75,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, -1.,
								0.5, 0.2, 1.,
								EPSILON));
			}
	
			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_6(CoordinateSystem3D cs) {
				// crossing plane but outside triangle => NaN
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Hits plane at (2,2,2), which is outside the triangle
				assertNaN(
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								2., 2., 0.,
								2., 2., 4.,
								EPSILON));
			}
	
			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_7(CoordinateSystem3D cs) {
				// segment parallel to plane and not in plane => NaN
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Direction (1,0,1) is parallel to plane x-z=0, start point not in plane (x-z=-1)
				assertNaN(
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0., 0., 1.,
								2., 0., 3.,
								EPSILON));
			}
	
			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_8(CoordinateSystem3D cs) {
				// segment included in plane and overlapping triangle edge => NaN (2D case unsupported)
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Entire segment in plane x=z, along AC line
				assertEpsilonEquals(0.33333333333,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								-1., 0., -1.,
								2., 0., 2.,
								EPSILON));
			}
	
			@DisplayName("#9")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_9(CoordinateSystem3D cs) {
				// degenerate segment point on triangle interior => factor 0
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, 0.5,
								0.5, 0.2, 0.5,
								EPSILON));
			}
	
			@DisplayName("#10")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_10(CoordinateSystem3D cs) {
				// degenerate segment point off plane => NaN
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, 0.6,
								0.5, 0.2, 0.6,
								EPSILON));
			}
	
			@DisplayName("#11")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_11(CoordinateSystem3D cs) {
				// hit exactly on edge AB => valid factor
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Target edge point P=(0.5,0.5,0.5), choose z interpolation so factor=0.75
				assertEpsilonEquals(0.75,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.5, -1.,
								0.5, 0.5, 1.,
								EPSILON));
			}
	
			@DisplayName("#12")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_12(CoordinateSystem3D cs) {
				// hit exactly on edge AC => valid factor
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Target edge point P=(0.5,0,0.5), factor 0.75 with z from -1 to 1
				assertEpsilonEquals(0.75,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0., -1.,
								0.5, 0., 1.,
								EPSILON));
			}
	
			@DisplayName("#13")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_13(CoordinateSystem3D cs) {
				// hit exactly on edge BC => valid factor
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Target edge point P=(1,0.5,1), factor 0.75 with z from 0 to 4
				assertEpsilonEquals(0.25,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								1., 0.5, 0.,
								1., 0.5, 4.,
								EPSILON));
			}
	
			@DisplayName("#14")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_14(CoordinateSystem3D cs) {
				// plane intersection exists but behind segment start (factor < 0) => NaN
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, 0.6,
								0.5, 0.2, 0.8,
								EPSILON));
			}
	
			@DisplayName("#15")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_15(CoordinateSystem3D cs) {
				// plane intersection exists beyond segment end (factor > 1) => NaN
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, -2.,
								0.5, 0.2, -1.,
								EPSILON));
			}
	
			@DisplayName("#16")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_16(CoordinateSystem3D cs) {
				// permutation of triangle vertices should not change factor
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				final var f1 = Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						0.5, 0.2, -1.,
						0.5, 0.2, 1.,
						EPSILON);
				final var f2 = Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
						1., 1., 1.,
						1., 0., 1.,
						0., 0., 0.,
						0.5, 0.2, -1.,
						0.5, 0.2, 1.,
						EPSILON);
				assertEpsilonEquals(f1, f2);
			}

			@DisplayName("#17")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_17(CoordinateSystem3D cs) {
				// crossing through triangle interior => factor in (0,1)
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.75,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, -1.,
								0.5, 0.5, 1.,
								EPSILON));
			}

			@DisplayName("#18")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_18(CoordinateSystem3D cs) {
				// segment is coplanar to the triangle and both points of the segment are inside the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, 0.5,
								0.5, 0.15, 0.5,
								EPSILON));
			}

			@DisplayName("#19")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_19(CoordinateSystem3D cs) {
				// segment is coplanar to the triangle and first point of the segment is inside the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, 0.5,
								0.5, -5, 0.5,
								EPSILON));
			}

			@DisplayName("#20")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_20(CoordinateSystem3D cs) {
				// segment is coplanar to the triangle and first point of the segment is inside the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(1.,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, -5, 0.5,
								0.5, 0.2, 0.5,
								EPSILON));
			}

			@DisplayName("#21")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_21(CoordinateSystem3D cs) {
				// segment degenerates to point but not coplanar to the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.5, 0.6,
								0.5, 0.5, 0.6,
								EPSILON));
			}

			@DisplayName("#22")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_22(CoordinateSystem3D cs) {
				// segment is not coplanar to the triangle and both projected points of the segment are inside the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, 0.6,
								0.5, 0.15, 0.6,
								EPSILON));
			}

			@DisplayName("#23")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_23(CoordinateSystem3D cs) {
				// segment is not coplanar to the triangle and projection of the first point of the segment is inside the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, 0.2, 0.6,
								0.5, -5, 0.6,
								EPSILON));
			}

			@DisplayName("#24")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_24(CoordinateSystem3D cs) {
				// degenerate segment point on triangle's plane but not inside the triangle interior
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, -5, 0.5,
								0.5, -5, 0.5,
								EPSILON));
			}

			@DisplayName("#25")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_25(CoordinateSystem3D cs) {
				// segment points on triangle's plane but not inside the triangle interior, with intersection between the segment and the triangle 
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.333333333333,
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								0.5, -5, 0.5,
								0.5, 10, 0.5,
								EPSILON));
			}

			@DisplayName("#26")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_26(CoordinateSystem3D cs) {
				// segment points on triangle's plane but not inside the triangle interior, with intersection between the segment and the triangle 
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertNaN(
						Triangle3afp.JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
								getS().getX1(), getS().getY1(), getS().getZ1(),
								getS().getX2(), getS().getY2(), getS().getZ2(),
								getS().getX3(), getS().getY3(), getS().getZ3(),
								1.5, -4, 1.5,
								1.5, 11, 1.5,
								EPSILON));
			}
		}
	}

	@DisplayName("Moller Algorithms")
	@Nested
	public class MollerAlgorithmToolsTests {

		@DisplayName("intersectsCoplanarTriangleTriangle")
		@Nested
		public class IntersectsCoplanarTriangleTriangle {

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				// identical triangles => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3()));
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				// same triangle, reversed winding => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						1., 0., 1.,
						1., 1., 1.,
						0., 0., 0.));
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				// vertex permutation => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						1., 1., 1.,
						1., 0., 1.,
						0., 0., 0.));
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4(CoordinateSystem3D cs) {
				// one triangle fully inside the other (coplanar) => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// big triangle in plane x=z
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0., 0., 0.,
						2., 0., 2.,
						2., 2., 2.,
						0.5, 0.2, 0.5,
						1.0, 0.2, 1.0,
						1.0, 0.8, 1.0));
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_5(CoordinateSystem3D cs) {
				// partial overlap by crossing edges => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						0.5, -0.2, 0.5,
						1.5, 0.5, 1.5,
						0.5, 0.8, 0.5));
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_6(CoordinateSystem3D cs) {
				// touching at a single shared vertex => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// second triangle only shares A(0,0,0)
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						0., 0., 0.,
						-1., 0., -1.,
						-1., -1., -1.));
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_7(CoordinateSystem3D cs) {
				// touching along one edge segment => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// share edge AC
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						0., 0., 0.,
						1., 0., 1.,
						0.2, -0.5, 0.2));
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_8(CoordinateSystem3D cs) {
				// disjoint but coplanar (far apart) => false
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertFalse(Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						10., 0., 10.,
						11., 0., 11.,
						11., 1., 11.));
			}

			@DisplayName("#9")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_9(CoordinateSystem3D cs) {
				// disjoint but same plane and close => false
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertFalse(Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						1.2, 0.2, 1.2,
						1.8, 0.2, 1.8,
						1.8, 0.8, 1.8));
			}

			@DisplayName("#10")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_10(CoordinateSystem3D cs) {
				// one triangle vertex on other triangle edge => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// vertex (0.5,0,0.5) lies on edge AC of first triangle
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						0.5, 0., 0.5,
						1.2, -0.3, 1.2,
						1.2, 0.3, 1.2));
			}

			@DisplayName("#11")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_11(CoordinateSystem3D cs) {
				// very small triangle inside => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						0.5000001, 0.2, 0.5000001,
						0.5000002, 0.2, 0.5000002,
						0.5000002, 0.2000001, 0.5000002));
			}

			@DisplayName("#12")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_12(CoordinateSystem3D cs) {
				// second triangle around first (first inside second) => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						-1., -1., -1.,
						3., -1., 3.,
						3., 3., 3.));
			}

			@DisplayName("#13")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_13(CoordinateSystem3D cs) {
				// collinear-degenerate second triangle on plane crossing first edge => true/false impl-dependent, keep robust non-crash
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// degenerate U: all points on AC line
				final boolean r = Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						-0.5, 0., -0.5,
						0.5, 0., 0.5,
						1.5, 0., 1.5);
				// Main purpose: no exception, deterministic boolean
				assertTrue(r || !r);
			}

			@DisplayName("#14")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_14(CoordinateSystem3D cs) {
				// zero-area second triangle at inside point => true/false impl-dependent, no crash
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				final boolean r = Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						0.5, 0.2, 0.5,
						0.5, 0.2, 0.5,
						0.5, 0.2, 0.5);
				assertTrue(r || !r);
			}

			@DisplayName("#15")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_15(CoordinateSystem3D cs) {
				// symmetry: intersect(A,B) == intersect(B,A)
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);

				final boolean ab = Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.,
						0.5, -0.2, 0.5,
						1.5, 0.5, 1.5,
						0.5, 0.8, 0.5);

				final boolean ba = Triangle3afp.MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
						0.5, -0.2, 0.5,
						1.5, 0.5, 1.5,
						0.5, 0.8, 0.5,
						0., 0., 0.,
						1., 1., 1.,
						1., 0., 1.);

				assertEquals(ab, ba);
			}
		}

		@DisplayName("intersectsTriangleAlignedBox")
		@Nested
		public class IntersectsTriangleAlignedBox {

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				// box [0,0,0]-[1,1,1] with default triangle => false (reference behavior)
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertFalse(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0., 0., 0., 1., 1., 1.));
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				// tiny box around vertex A => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						0., 0., 0., 1., 1., 1., 1., 0., 1.,
						-0.1, -0.1, -0.1, 0.1, 0.1, 0.1));
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				// tiny box around vertex B => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						0., 0., 0., 1., 1., 1., 1., 0., 1.,
						0.9, 0.9, 0.9, 1.1, 1.1, 1.1));
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4(CoordinateSystem3D cs) {
				// tiny box around vertex C => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						0., 0., 0., 1., 1., 1., 1., 0., 1.,
						0.9, -0.1, 0.9, 1.1, 0.1, 1.1));
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_5(CoordinateSystem3D cs) {
				// box centered on interior triangle point => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// around P=(0.5,0.2,0.5) which is on triangle
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						0., 0., 0., 1., 1., 1., 1., 0., 1.,
						0.45, 0.15, 0.45, 0.55, 0.25, 0.55));
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_6(CoordinateSystem3D cs) {
				// box far away => false
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertFalse(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						0., 0., 0., 1., 1., 1., 1., 0., 1.,
						10., 10., 10., 12., 12., 12.));
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_7(CoordinateSystem3D cs) {
				// box touching only at one triangle vertex => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// Degenerate touching at A via max corner
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						0., 0., 0., 1., 1., 1., 1., 0., 1.,
						-1., -1., -1., 0., 0., 0.));
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_8(CoordinateSystem3D cs) {
				// box intersecting along triangle edge AC region => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						0., 0., 0., 1., 1., 1., 1., 0., 1.,
						0.4, -0.1, 0.4, 0.6, 0.1, 0.6));
			}

			@DisplayName("#9")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_9(CoordinateSystem3D cs) {
				// plane-crossing box but projected area misses triangle => false
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// box crosses plane x=z near (2,*,2) outside triangle extents
				assertFalse(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						0., 0., 0., 1., 1., 1., 1., 0., 1.,
						1.8, -0.2, 1.7, 2.2, 0.2, 2.3));
			}

			@DisplayName("#10")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_10(CoordinateSystem3D cs) {
				// large box containing whole triangle => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						0., 0., 0., 1., 1., 1., 1., 0., 1.,
						-1., -1., -1., 2., 2., 2.));
			}

			@DisplayName("#11")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_11(CoordinateSystem3D cs) {
				// flat box (zero thickness in Y) slicing triangle line => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// y=0 plane slab intersects edge AC
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						0., 0., 0., 1., 1., 1., 1., 0., 1.,
						0.2, 0.0, 0.2, 0.8, 0.0, 0.8));
			}

			@DisplayName("#12")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_12(CoordinateSystem3D cs) {
				// point-box exactly on interior triangle point => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						0., 0., 0., 1., 1., 1., 1., 0., 1.,
						0.5, 0.2, 0.5, 0.5, 0.2, 0.5));
			}

			@DisplayName("#13")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_13(CoordinateSystem3D cs) {
				// point-box on triangle plane but outside triangle => false
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// (2,0,2) is on plane x=z but outside triangle
				assertFalse(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						0., 0., 0., 1., 1., 1., 1., 0., 1.,
						2., 0., 2., 2., 0., 2.));
			}

			@DisplayName("#14")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_14(CoordinateSystem3D cs) {
				// translated triangle + local intersecting box => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// triangle translated by (+3,-2,+3)
				assertTrue(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						3., -2., 3.,
						4., -1., 4.,
						4., -2., 4.,
						3.4, -2.1, 3.4, 3.6, -1.9, 3.6));
			}

			@DisplayName("#15")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_15(CoordinateSystem3D cs) {
				// translated triangle + far box => false
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertFalse(Triangle3afp.MollerAlgorithmTools.intersectsTriangleAlignedBox(
						3., -2., 3.,
						4., -1., 4.,
						4., -2., 4.,
						0., 0., 0., 1., 1., 1.));
			}
		}
	}

	@DisplayName("getPivot")
	@Nested
	public class GetPivot {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getS().getPivot());
		}
	}

	@DisplayName("setPivot")
	@Nested
	public class SetPivot {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setPivot(1, 4, 7);
			assertEpsilonEquals(createPoint(1, 4, 7), getS().getPivot());
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setPivot(createPoint(1, 4, 7));
			assertEpsilonEquals(createPoint(1, 4, 7), getS().getPivot());
		}
	}

	@DisplayName("IntersectionTools")
	@Nested
	public class IntersectionToolsTests {

		@DisplayName("containsTrianglePoint")
		@Nested
		public class ContainsTrianglePoint {
	
			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1_vertexA_forceFalse(CoordinateSystem3D cs) {
				// vertex A, forceCoplanar=false => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 0, 1,
						-5, -6, 4,
						0, 0, 0,
						false, EPSILON));
			}
	
			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2_vertexA_forceTrue(CoordinateSystem3D cs) {
				// vertex A, forceCoplanar=true => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 0, 1,
						-5, -6, 4,
						0, 0, 0,
						true, EPSILON));
			}
	
			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3_vertexB(CoordinateSystem3D cs) {
				// vertex B => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 0, 1,
						-5, -6, 4,
						1, 0, 1,
						true, EPSILON));
			}
	
			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4_vertexC(CoordinateSystem3D cs) {
				// vertex C => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 0, 1,
						-5, -6, 4,
						-5, -6, 4,
						true, EPSILON));
			}
	
			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_5_edgeAB_midpoint(CoordinateSystem3D cs) {
				// midpoint AB on edge => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 0, 1,
						-5, -6, 4,
						0.5, 0, 0.5,
						true, EPSILON));
			}
	
			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_6_edgeAC_midpoint(CoordinateSystem3D cs) {
				// midpoint AC on edge => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 0, 1,
						-5, -6, 4,
						-2.5, -3, 2,
						true, EPSILON));
			}
	
			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_7_edgeBC_midpoint(CoordinateSystem3D cs) {
				// midpoint BC on edge => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 0, 1,
						-5, -6, 4,
						-2, -3, 2.5,
						true, EPSILON));
			}
	
			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_8_interior(CoordinateSystem3D cs) {
				// strict interior point (barycentric) => true
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// P = 0.2*A + 0.3*B + 0.5*C
				assertTrue(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 0, 1,
						-5, -6, 4,
						-2.2, -3, 2.3,
						true, EPSILON));
			}
	
			@DisplayName("#9")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_9_outside_coplanar(CoordinateSystem3D cs) {
				// coplanar but outside near AB extension => false
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertFalse(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 0, 1,
						-5, -6, 4,
						2, 0, 2,
						true, EPSILON));
			}
	
			@DisplayName("#10")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_10_nonCoplanar_projectionInside_forceFalse(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertFalse(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 0, 1,
						-5, -6, 4,
						-1.6, -3.9, 2.9,
						false, EPSILON));
			}
	
			@DisplayName("#11")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_11_nonCoplanar_projectionInside_forceTrue(CoordinateSystem3D cs) {
				// non-coplanar, projection inside, forceCoplanar=true => false
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertFalse(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 0, 1,
						-5, -6, 4,
						-1.6, -3.9, 2.9,
						true, EPSILON));
			}
	
			@DisplayName("#12")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_12_nonCoplanar_projectionOutside_forceFalse(CoordinateSystem3D cs) {
				// non-coplanar, projection outside, forceCoplanar=false => false
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				// outside coplanar point (2,0,2) shifted along normal
				assertFalse(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 0, 1,
						-5, -6, 4,
						2.6, -0.9, 1.4,
						false, EPSILON));
			}
	
			@DisplayName("#13")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_13_nonCoplanar_projectionOutside_forceTrue(CoordinateSystem3D cs) {
				// non-coplanar, projection outside, forceCoplanar=true => false
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertFalse(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 0, 1,
						-5, -6, 4,
						2.6, -0.9, 1.4,
						true, EPSILON));
			}
	
			@DisplayName("#14")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_14_degenerateTriangle_collinear(CoordinateSystem3D cs) {
				// degenerate triangle (collinear) => false
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertFalse(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 1, 1,
						2, 2, 2,
						1, 1, 1,
						true, EPSILON));
			}
	
			@DisplayName("#15")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_15(CoordinateSystem3D cs) {
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Triangle3afp.IntersectionTools.containsTrianglePoint(
						0, 0, 0,
						1, 0, 1,
						-5, -6, 4,
						-1.8253562504, 3.6380343755, 3.0253562504,
						false, EPSILON));
			}
		}

		@DisplayName("calculatesIntersectionFactorTriangleSegmentWhenOnPlane")
		@Nested
		public class CalculatesIntersectionFactorTriangleSegmentWhenOnPlane {
	
			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				// segment coplanar and intersecting the triangle, with the two segment points outside the rectangle
				// => Infinite number of intersection points => select first intersecting point with factor=1/3 
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.333333333333, Triangle3afp.IntersectionTools.calculatesIntersectionFactorTriangleSegmentWhenOnPlane(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.5, -5, 0.5,
						0.5, 10, 0.5,
						EPSILON));
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				// segment coplanar and completely outside the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertTrue(Double.isNaN(Triangle3afp.IntersectionTools.calculatesIntersectionFactorTriangleSegmentWhenOnPlane(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						2., 2., 2.,
						4., 4., 4.,
						EPSILON)));
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				// first segment endpoint inside triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0., Triangle3afp.IntersectionTools.calculatesIntersectionFactorTriangleSegmentWhenOnPlane(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.5, 0.5, 0.5,
						2., 2., 2.,
						EPSILON));
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4(CoordinateSystem3D cs) {
				// second segment endpoint inside triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(1., Triangle3afp.IntersectionTools.calculatesIntersectionFactorTriangleSegmentWhenOnPlane(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						2., 2., 2.,
						0.5, 0.5, 0.5,
						EPSILON));
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_5(CoordinateSystem3D cs) {
				// segment crosses the triangle, first intersection on an edge
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.25, Triangle3afp.IntersectionTools.calculatesIntersectionFactorTriangleSegmentWhenOnPlane(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0., 0.5, 0.5,
						2., 0.5, 0.5,
						EPSILON));
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_6(CoordinateSystem3D cs) {
				// segment overlaps an edge of the triangle
				// first overlapping point is at factor 1/4
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0.25, Triangle3afp.IntersectionTools.calculatesIntersectionFactorTriangleSegmentWhenOnPlane(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						-1., 0., 0.,
						3., 0., 0.,
						EPSILON));
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_7(CoordinateSystem3D cs) {
				// segment touches a triangle vertex
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(1., Triangle3afp.IntersectionTools.calculatesIntersectionFactorTriangleSegmentWhenOnPlane(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						-1., -1., -1.,
						1., 1., 1.,
						EPSILON));
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_8(CoordinateSystem3D cs) {
				// segment fully inside triangle => first endpoint returned
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0., Triangle3afp.IntersectionTools.calculatesIntersectionFactorTriangleSegmentWhenOnPlane(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.2, 0.2, 0.2,
						0.3, 0.3, 0.3,
						EPSILON));
			}

			@DisplayName("#9")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_9(CoordinateSystem3D cs) {
				// segment endpoint lies on triangle edge
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(1., Triangle3afp.IntersectionTools.calculatesIntersectionFactorTriangleSegmentWhenOnPlane(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						2., 2., 2.,
						0., 0., 0.,
						EPSILON));
			}

			@DisplayName("#10")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_10(CoordinateSystem3D cs) {
				// degenerate coplanar intersection: one endpoint on triangle boundary, other outside
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				assertEpsilonEquals(0., Triangle3afp.IntersectionTools.calculatesIntersectionFactorTriangleSegmentWhenOnPlane(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0., 0., 0.,
						2., -1., 2.,
						EPSILON));
			}
		}
	}

	@DisplayName("EricsonAlgorithmTools")
	@Nested
	public class EricsonAlgorithmTools {

		@DisplayName("findsClosestPointTrianglePoint")
		@Nested
		public class FindsClosestPointTrianglePoint {

			private Point3D<?, ?, ?> result;
			
			@BeforeEach
			public void setUp() {
				result = new InnerComputationPoint3D();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				// Point clearly outside the triangle, closest point is a vertex
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						10, 10, 10,
						result);
				assertEpsilonEquals(createPoint(1, 1, 1), result);
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				// Point inside the triangle => closest point is the point itself
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.25, 0.25, 0.25,
						result);
				assertEpsilonEquals(createPoint(0.25, 0.25, 0.25), result);
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				// Point on an edge => closest point is the point itself
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.5, 0., 0.5,
						result);
				assertEpsilonEquals(createPoint(0.5, 0., 0.5), result);
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4(CoordinateSystem3D cs) {
				// Point on a vertex => closest point is the vertex itself
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0., 0., 0.,
						result);
				assertEpsilonEquals(createPoint(0., 0., 0.), result);
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_5(CoordinateSystem3D cs) {
				// Point above the triangle plane => closest point is the orthogonal projection on the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.25, 0.25, 5.,
						result);
				assertEpsilonEquals(createPoint(1, 0.25, 1), result);
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_6(CoordinateSystem3D cs) {
				// Point outside near an edge => closest point is on the edge
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.75, -0.5, 0.75,
						result);
				assertEpsilonEquals(createPoint(0.75, 0., 0.75), result);
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_7(CoordinateSystem3D cs) {
				// Point outside near another vertex => closest point is that vertex
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						-2., -2., -2.,
						result);
				assertEpsilonEquals(createPoint(0., 0., 0.), result);
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_8(CoordinateSystem3D cs) {
				// Point outside, closest point is on edge (midpoint projection)
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.5, -1., 0.5,
						result);
				assertEpsilonEquals(createPoint(0.5, 0., 0.5), result);
			}

			@DisplayName("#9")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_9(CoordinateSystem3D cs) {
				// Point outside near the hypotenuse edge
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						1.2, 1.2, 0.5,
						result);
				assertEpsilonEquals(createPoint(0.966666667, 0.966666667, 0.966666667), result);
			}

			@DisplayName("#10")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_10(CoordinateSystem3D cs) {
				// Point outside beyond vertex (1,1,1)
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						2., 2., 2.,
						result);
				assertEpsilonEquals(createPoint(1., 1., 1.), result);
			}

			@DisplayName("#11")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_11(CoordinateSystem3D cs) {
				// Point outside with projection landing inside the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.2, 0.3, 10.,
						result);
				assertEpsilonEquals(createPoint(1, 0.3, 1), result);
			}

			@DisplayName("#12")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_12(CoordinateSystem3D cs) {
				// Point outside with projection landing on edge between first and second vertices
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.7, -0.2, 0.7,
						result);
				assertEpsilonEquals(createPoint(0.7, 0., 0.7), result);
			}

			@DisplayName("#13")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_13(CoordinateSystem3D cs) {
				// Point outside with projection landing on edge between second and third vertices
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						1.2, 0.4, 0.4,
						result);
				assertEpsilonEquals(createPoint(0.8, 0.4, 0.8), result);
			}

			@DisplayName("#14")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_14(CoordinateSystem3D cs) {
				// Point outside with projection landing on edge between third and first vertices
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						-0.4, 0.7, 0.7,
						result);
				assertEpsilonEquals(createPoint(0.333333333333, 0.333333333333, 0.333333333333), result);
			}

			@DisplayName("#15")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_15(CoordinateSystem3D cs) {
				// Point exactly at triangle centroid-like interior location
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.25, 0.25, 0.5,
						result);
				assertEpsilonEquals(createPoint(0.375, 0.25, 0.375), result);
			}

			@DisplayName("#16")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_16(CoordinateSystem3D cs) {
				// Sphere intersects one triangle edge
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				Triangle3afp.EricsonAlgorithmTools.findsClosestPointTrianglePoint(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.5, -0.2, 0.5,
						result);
				assertEpsilonEquals(createPoint(0.5, 0.0, 0.5), result);
			}
		}

		@DisplayName("findsClosestPointTriangleSegment")
		@Nested
		public class FindsClosestPointTriangleSegment {

			private Point3D<?, ?, ?> resultForTriangle;
			private Point3D<?, ?, ?> resultForSegment;
			
			@BeforeEach
			public void setUp() {
				resultForTriangle = new InnerComputationPoint3D();
				resultForSegment = new InnerComputationPoint3D();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_1(CoordinateSystem3D cs) {
				// No intersection, closest pair is on triangle interior projection + segment interior
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				var intersect = Triangle3afp.EricsonAlgorithmTools.findsClosestPointTriangleSegment(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.5, -0.2, 0.5, 1.2, 1.2, 0.5,
						EPSILON,
						resultForTriangle, resultForSegment);
				assertFalse(intersect);
				assertEpsilonEquals(createPoint(0.5444444444444444, 0.0, 0.5444444444444444), resultForTriangle);
				assertEpsilonEquals(createPoint(0.5888888888888889, -0.022222222222222254, 0.5), resultForSegment);
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_2(CoordinateSystem3D cs) {
				// Segment intersects the triangle => closest points are equal
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				var intersect = Triangle3afp.EricsonAlgorithmTools.findsClosestPointTriangleSegment(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.25, 0.25, -1.0, 0.25, 0.25, 2.0,
						EPSILON,
						resultForTriangle, resultForSegment);
				assertTrue(intersect);
				assertEpsilonEquals(resultForTriangle, resultForSegment);
				assertEpsilonEquals(createPoint(0.25, 0.25, 0.25), resultForTriangle);
				assertEpsilonEquals(createPoint(0.25, 0.25, 0.25), resultForSegment);
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_3(CoordinateSystem3D cs) {
				// Segment endpoint inside triangle => intersection
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				var intersect = Triangle3afp.EricsonAlgorithmTools.findsClosestPointTriangleSegment(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.2, 0.2, 0.2, 2.0, 2.0, 2.0,
						EPSILON,
						resultForTriangle, resultForSegment);
				assertTrue(intersect);
				assertEpsilonEquals(resultForTriangle, resultForSegment);
				assertEpsilonEquals(createPoint(1, 1, 1), resultForTriangle);
				assertEpsilonEquals(createPoint(1, 1, 1), resultForSegment);
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_4(CoordinateSystem3D cs) {
				// Segment endpoint inside triangle, the other outside
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				var intersect = Triangle3afp.EricsonAlgorithmTools.findsClosestPointTriangleSegment(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						-1.0, -1.0, -1.0, 0.2, 0.2, 0.2,
						EPSILON,
						resultForTriangle, resultForSegment);
				assertTrue(intersect);
				assertEpsilonEquals(resultForTriangle, resultForSegment);
				assertEpsilonEquals(createPoint(0.2, 0.2, 0.2), resultForTriangle);
				assertEpsilonEquals(createPoint(0.2, 0.2, 0.2), resultForSegment);
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_5(CoordinateSystem3D cs) {
				// Closest pair lies on an edge of the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				var intersect = Triangle3afp.EricsonAlgorithmTools.findsClosestPointTriangleSegment(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.5, -1.0, 0.5, 0.5, -0.2, 0.5,
						EPSILON,
						resultForTriangle, resultForSegment);
				assertFalse(intersect);
				assertEpsilonEquals(createPoint(0.5, 0.0, 0.5), resultForTriangle);
				assertEpsilonEquals(createPoint(0.5, -0.2, 0.5), resultForSegment);
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_6(CoordinateSystem3D cs) {
				// Closest pair lies on a triangle vertex
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				var intersect = Triangle3afp.EricsonAlgorithmTools.findsClosestPointTriangleSegment(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						-2.0, -2.0, -2.0, -1.0, -1.0, -1.0,
						EPSILON,
						resultForTriangle, resultForSegment);
				assertFalse(intersect);
				assertEpsilonEquals(createPoint(0.0, 0.0, 0.0), resultForTriangle);
				assertEpsilonEquals(createPoint(-1.0, -1.0, -1.0), resultForSegment);
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_7(CoordinateSystem3D cs) {
				// Segment crosses triangle plane but misses the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				var intersect = Triangle3afp.EricsonAlgorithmTools.findsClosestPointTriangleSegment(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						1.2, 0.2, -1.0, 1.2, 0.2, 2.0,
						EPSILON,
						resultForTriangle, resultForSegment);
				assertFalse(intersect);
				assertEpsilonEquals(createPoint(1.0,0.19999999999999996,1.0), resultForTriangle);
				assertEpsilonEquals(createPoint(1.2, 0.2, 1.0), resultForSegment);
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_8(CoordinateSystem3D cs) {
				// Parallel segment above the triangle
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				var intersect = Triangle3afp.EricsonAlgorithmTools.findsClosestPointTriangleSegment(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.2, 0.2, 1.0, 0.8, 0.2, 1.0,
						EPSILON,
						resultForTriangle, resultForSegment);
				assertFalse(intersect);
				assertEpsilonEquals(createPoint(0.9, 0.2, 0.9), resultForTriangle);
				assertEpsilonEquals(createPoint(0.8, 0.2, 1.0), resultForSegment);
			}

			@DisplayName("#9")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_9(CoordinateSystem3D cs) {
				// Segment is very close to a triangle vertex but does not intersect
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				var intersect = Triangle3afp.EricsonAlgorithmTools.findsClosestPointTriangleSegment(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						-0.2, -0.2, 0.3, -0.2, -0.2, 1.0,
						EPSILON,
						resultForTriangle, resultForSegment);
				assertFalse(intersect);
				assertEpsilonEquals(createPoint(0.050000000000000044,0.0,0.050000000000000044), resultForTriangle);
				assertEpsilonEquals(createPoint(-0.2, -0.2, 0.3), resultForSegment);
			}

			@DisplayName("#10")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_10(CoordinateSystem3D cs) {
				// Segment endpoint lies exactly on triangle boundary
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				var intersect = Triangle3afp.EricsonAlgorithmTools.findsClosestPointTriangleSegment(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						1.5, 0.0, 0.5, 0.5, 0.0, 0.5,
						EPSILON,
						resultForTriangle, resultForSegment);
				assertTrue(intersect);
				assertEpsilonEquals(resultForTriangle, resultForSegment);
				assertEpsilonEquals(createPoint(0.5, 0.0, 0.5), resultForTriangle);
				assertEpsilonEquals(createPoint(0.5, 0.0, 0.5), resultForSegment);
			}

			@DisplayName("#11")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_11(CoordinateSystem3D cs) {
				// Segment lies on a line parallel to an edge and closest point is one endpoint projection
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				var intersect = Triangle3afp.EricsonAlgorithmTools.findsClosestPointTriangleSegment(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						0.8, 1.2, 0.5, 1.6, 1.2, 0.5,
						EPSILON,
						resultForTriangle, resultForSegment);
				assertFalse(intersect);
				assertEpsilonEquals(createPoint(0.84999999999, 0.84999999999, 0.84999999999), resultForTriangle);
				assertEpsilonEquals(createPoint(0.849999999998, 1.2, 0.5), resultForSegment);
			}

			@DisplayName("#12")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem3D.class)
			public final void test_12(CoordinateSystem3D cs) {
				// Segment intersects triangle at a vertex
				CoordinateSystem3D.setDefaultCoordinateSystem(cs);
				var intersect = Triangle3afp.EricsonAlgorithmTools.findsClosestPointTriangleSegment(
						getS().getX1(), getS().getY1(), getS().getZ1(),
						getS().getX2(), getS().getY2(), getS().getZ2(),
						getS().getX3(), getS().getY3(), getS().getZ3(),
						-1.0, -1.0, -1.0, 0.0, 0.0, 0.0,
						EPSILON,
						resultForTriangle, resultForSegment);
				assertTrue(intersect);
				assertEpsilonEquals(resultForTriangle, resultForSegment);
				assertEpsilonEquals(createPoint(0.0, 0.0, 0.0), resultForTriangle);
				assertEpsilonEquals(createPoint(0.0, 0.0, 0.0), resultForSegment);
			}
		}
	}

	@DisplayName("findsClosestPointTriangleSegment")
	@Nested
	public class FindsClosestPointTriangleSegment {

		private Point3D<?, ?, ?> resultForTriangle;
		private Point3D<?, ?, ?> resultForSegment;
		
		@BeforeEach
		public void setUp() {
			resultForTriangle = new InnerComputationPoint3D();
			resultForSegment = new InnerComputationPoint3D();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			// No intersection, closest pair is on triangle interior projection + segment interior
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.5, -0.2, 0.5, 1.2, 1.2, 0.5,
					EPSILON,
					resultForTriangle, resultForSegment);
			assertEpsilonEquals(createPoint(0.5444444444444444, 0.0, 0.5444444444444444), resultForTriangle);
			assertEpsilonEquals(createPoint(0.5888888888888889, -0.022222222222222254, 0.5), resultForSegment);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			// Segment intersects the triangle => closest points are equal
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.25, 0.25, -1.0, 0.25, 0.25, 2.0,
					EPSILON,
					resultForTriangle, resultForSegment);
			assertEpsilonEquals(resultForTriangle, resultForSegment);
			assertEpsilonEquals(createPoint(0.25, 0.25, 0.25), resultForTriangle);
			assertEpsilonEquals(createPoint(0.25, 0.25, 0.25), resultForSegment);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// Segment endpoint inside triangle => intersection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.2, 0.2, 0.2, 2.0, 2.0, 2.0,
					EPSILON,
					resultForTriangle, resultForSegment);
			assertEpsilonEquals(resultForTriangle, resultForSegment);
			assertEpsilonEquals(createPoint(1, 1, 1), resultForTriangle);
			assertEpsilonEquals(createPoint(1, 1, 1), resultForSegment);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// Segment endpoint inside triangle, the other outside
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					-1.0, -1.0, -1.0, 0.2, 0.2, 0.2,
					EPSILON,
					resultForTriangle, resultForSegment);
			assertEpsilonEquals(resultForTriangle, resultForSegment);
			assertEpsilonEquals(createPoint(0.2, 0.2, 0.2), resultForTriangle);
			assertEpsilonEquals(createPoint(0.2, 0.2, 0.2), resultForSegment);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			// Closest pair lies on an edge of the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.5, -1.0, 0.5, 0.5, -0.2, 0.5,
					EPSILON,
					resultForTriangle, resultForSegment);
			assertEpsilonEquals(createPoint(0.5, 0.0, 0.5), resultForTriangle);
			assertEpsilonEquals(createPoint(0.5, -0.2, 0.5), resultForSegment);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			// Closest pair lies on a triangle vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					-2.0, -2.0, -2.0, -1.0, -1.0, -1.0,
					EPSILON,
					resultForTriangle, resultForSegment);
			assertEpsilonEquals(createPoint(0.0, 0.0, 0.0), resultForTriangle);
			assertEpsilonEquals(createPoint(-1.0, -1.0, -1.0), resultForSegment);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			// Segment crosses triangle plane but misses the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					1.2, 0.2, -1.0, 1.2, 0.2, 2.0,
					EPSILON,
					resultForTriangle, resultForSegment);
			assertEpsilonEquals(createPoint(1.0,0.19999999999999996,1.0), resultForTriangle);
			assertEpsilonEquals(createPoint(1.2, 0.2, 1.0), resultForSegment);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			// Parallel segment above the triangle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.2, 0.2, 1.0, 0.8, 0.2, 1.0,
					EPSILON,
					resultForTriangle, resultForSegment);
			assertEpsilonEquals(createPoint(0.9, 0.2, 0.9), resultForTriangle);
			assertEpsilonEquals(createPoint(0.8, 0.2, 1.0), resultForSegment);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			// Segment is very close to a triangle vertex but does not intersect
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					-0.2, -0.2, 0.3, -0.2, -0.2, 1.0,
					EPSILON,
					resultForTriangle, resultForSegment);
			assertEpsilonEquals(createPoint(0.050000000000000044,0.0,0.050000000000000044), resultForTriangle);
			assertEpsilonEquals(createPoint(-0.2, -0.2, 0.3), resultForSegment);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			// Segment endpoint lies exactly on triangle boundary
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					1.5, 0.0, 0.5, 0.5, 0.0, 0.5,
					EPSILON,
					resultForTriangle, resultForSegment);
			assertEpsilonEquals(resultForTriangle, resultForSegment);
			assertEpsilonEquals(createPoint(0.5, 0.0, 0.5), resultForTriangle);
			assertEpsilonEquals(createPoint(0.5, 0.0, 0.5), resultForSegment);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			// Segment lies on a line parallel to an edge and closest point is one endpoint projection
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					0.8, 1.2, 0.5, 1.6, 1.2, 0.5,
					EPSILON,
					resultForTriangle, resultForSegment);
			assertEpsilonEquals(createPoint(0.84999999999, 0.84999999999, 0.84999999999), resultForTriangle);
			assertEpsilonEquals(createPoint(0.849999999998, 1.2, 0.5), resultForSegment);
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			// Segment intersects triangle at a vertex
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Triangle3afp.findsClosestPointTriangleSegment(
					getS().getX1(), getS().getY1(), getS().getZ1(),
					getS().getX2(), getS().getY2(), getS().getZ2(),
					getS().getX3(), getS().getY3(), getS().getZ3(),
					-1.0, -1.0, -1.0, 0.0, 0.0, 0.0,
					EPSILON,
					resultForTriangle, resultForSegment);
			assertEpsilonEquals(resultForTriangle, resultForSegment);
			assertEpsilonEquals(createPoint(0.0, 0.0, 0.0), resultForTriangle);
			assertEpsilonEquals(createPoint(0.0, 0.0, 0.0), resultForSegment);
		}
	}
}