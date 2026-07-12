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

package org.arakhne.afc.math.geometry.base.tests.coordinatesystem;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystemConstants;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationQuaternion;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationVector3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit test for {@link CoordinateSystem3D}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.18
 */
@DisplayName("CoordinateSystem3D")
@SuppressWarnings("all")
public class CoordinateSystem3DTest extends AbstractMathTestCase {

	private static InnerComputationQuaternion newAxisAngle(double x, double y, double z, double angle) {
		final var q = new InnerComputationQuaternion();
		q.setAxisAngle(x, y, z, angle);
		return q;
	}

	@DisplayName("getDimensions")
	@Nested
	public class GetDimensions {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_1() {
			assertEquals(3, CoordinateSystem3D.XYZ_RIGHT_HAND.getDimensions());
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_2() {
			assertEquals(3, CoordinateSystem3D.XYZ_LEFT_HAND.getDimensions());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_3() {
			assertEquals(3, CoordinateSystem3D.XZY_RIGHT_HAND.getDimensions());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_4() {
			assertEquals(3, CoordinateSystem3D.XZY_RIGHT_HAND.getDimensions());
		}

	}

	@DisplayName("getViewVector")
	@Nested
	public class GetViewVector {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("()")
		@Test
		public void test_1() {
			Vector3D<?, ?, ?> v;
			v = CoordinateSystem3D.getViewVector();
			assertEpsilonEquals(new InnerComputationVector3D(1, 0, 0), v);
		}

		@DisplayName("Inline")
		@Test
		public void test_2() {
			assertInlineParameterUsage(CoordinateSystem3D.class, "getViewVector"); //$NON-NLS-1$
		}

		@DisplayName("(Tuple3D)")
		@Test
		public void test_3() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.getViewVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(1, 0, 0), t);
		}

	}

	@DisplayName("getBackVector")
	@Nested
	public class GetBackVector {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("()")
		@Test
		public void test_1() {
			Vector3D<?, ?, ?> v;
			v = CoordinateSystem3D.getBackVector();
			assertEpsilonEquals(new InnerComputationVector3D(-1, 0, 0), v);
		}

		@DisplayName("Inline")
		@Test
		public void test_2() {
			assertInlineParameterUsage(CoordinateSystem3D.class, "getBackVector"); //$NON-NLS-1$
		}
	
		@DisplayName("(Tuple3D)")
		@Test
		public void test_3() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.getBackVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(-1, 0, 0), t);
		}

	}

	@DisplayName("getLeftVector")
	@Nested
	public class GetLeftVector {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("() XYZ_RIGHT_HAND")
		@Test
		public void test_1() {
			Vector3D v;
			v = CoordinateSystem3D.XYZ_RIGHT_HAND.getLeftVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, 1, 0), v);
		}

		@DisplayName("() XYZ_LEFT_HAND")
		@Test
		public void test_2() {
			Vector3D v;
			v = CoordinateSystem3D.XYZ_LEFT_HAND.getLeftVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, -1, 0), v);
		}

		@DisplayName("() XZY_RIGHT_HAND")
		@Test
		public void test_3() {
			Vector3D v;
			v = CoordinateSystem3D.XZY_RIGHT_HAND.getLeftVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, -1), v);
		}

		@DisplayName("() XZY_LEFT_HAND")
		@Test
		public void test_4() {
			Vector3D v;
			v = CoordinateSystem3D.XZY_LEFT_HAND.getLeftVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, 1), v);
		}

		@DisplayName("(Tuple3D) XYZ_RIGHT_HAND")
		@Test
		public void test_5() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XYZ_RIGHT_HAND.getLeftVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, 1, 0), t);
		}

		@DisplayName("(Tuple3D) XYZ_LEFT_HAND")
		@Test
		public void test_6() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XYZ_LEFT_HAND.getLeftVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, -1, 0), t);
		}

		@DisplayName("(Tuple3D) XZY_RIGHT_HAND")
		@Test
		public void test_7() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XZY_RIGHT_HAND.getLeftVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, -1), t);
		}

		@DisplayName("(Tuple3D) XZY_LEFT_HAND")
		@Test
		public void test_8() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XZY_LEFT_HAND.getLeftVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, 1), t);
		}

	}

	@DisplayName("getRightVector")
	@Nested
	public class GetRightVector {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("() XYZ_RIGHT_HAND")
		@Test
		public void test_1() {
			Vector3D v;
			v = CoordinateSystem3D.XYZ_RIGHT_HAND.getRightVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, -1, 0), v);
		}

		@DisplayName("() XYZ_LEFT_HAND")
		@Test
		public void test_2() {
			Vector3D v;
			v = CoordinateSystem3D.XYZ_LEFT_HAND.getRightVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, 1, 0), v);
		}

		@DisplayName("() XZY_RIGHT_HAND")
		@Test
		public void test_3() {
			Vector3D v;
			v = CoordinateSystem3D.XZY_RIGHT_HAND.getRightVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, 1), v);
		}

		@DisplayName("() XZY_LEFT_HAND")
		@Test
		public void test_4() {
			Vector3D v;
			v = CoordinateSystem3D.XZY_LEFT_HAND.getRightVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, -1), v);
		}

		@DisplayName("(Tuple3D) XYZ_RIGHT_HAND")
		@Test
		public void test_5() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XYZ_RIGHT_HAND.getRightVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, -1, 0), t);
		}

		@DisplayName("(Tuple3D) XYZ_LEFT_HAND")
		@Test
		public void test_6() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XYZ_LEFT_HAND.getRightVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, 1, 0), t);
		}

		@DisplayName("(Tuple3D) XZY_RIGHT_HAND")
		@Test
		public void test_7() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XZY_RIGHT_HAND.getRightVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, 1), t);
		}

		@DisplayName("(Tuple3D) XZY_LEFT_HAND")
		@Test
		public void test_8() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XZY_LEFT_HAND.getRightVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, -1), t);
		}

	}

	@DisplayName("getUpVector")
	@Nested
	public class GetUpVector {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("() XYZ_RIGHT_HAND")
		@Test
		public void test_1() {
			Vector3D v;
			v = CoordinateSystem3D.XYZ_RIGHT_HAND.getUpVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, 1), v);
		}

		@DisplayName("() XYZ_LEFT_HAND")
		@Test
		public void test_2() {
			Vector3D v;
			v = CoordinateSystem3D.XYZ_LEFT_HAND.getUpVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, 1), v);
		}

		@DisplayName("() XZY_RIGHT_HAND")
		@Test
		public void test_3() {
			Vector3D v;
			v = CoordinateSystem3D.XZY_RIGHT_HAND.getUpVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, 1, 0), v);
		}

		@DisplayName("() XZY_LEFT_HAND")
		@Test
		public void test_4() {
			Vector3D v;
			v = CoordinateSystem3D.XZY_LEFT_HAND.getUpVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, 1, 0), v);
		}
	
		@DisplayName("(Tuple3D) XYZ_RIGHT_HAND")
		@Test
		public void test_5() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XYZ_RIGHT_HAND.getUpVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, 1), t);
		}
		
		@DisplayName("(Tuple3D) XYZ_LEFT_HAND")
		@Test
		public void test_6() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XYZ_LEFT_HAND.getUpVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, 1), t);
		}
		
		@DisplayName("(Tuple3D) XZY_RIGHT_HAND")
		@Test
		public void test_7() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XZY_RIGHT_HAND.getUpVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, 1, 0), t);
		}
		
		@DisplayName("(Tuple3D) XZY_LEFT_HAND")
		@Test
		public void test_8() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XZY_LEFT_HAND.getUpVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, 1, 0), t);
		}

	}

	@DisplayName("getDownVector")
	@Nested
	public class GetDownVector {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("() XYZ_RIGHT_HAND")
		@Test
		public void test_1() {
			Vector3D v;
			v = CoordinateSystem3D.XYZ_RIGHT_HAND.getDownVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, -1), v);
		}

		@DisplayName("() XYZ_LEFT_HAND")
		@Test
		public void test_2() {
			Vector3D v;
			v = CoordinateSystem3D.XYZ_LEFT_HAND.getDownVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, -1), v);
		}

		@DisplayName("() XZY_RIGHT_HAND")
		@Test
		public void test_3() {
			Vector3D v;
			v = CoordinateSystem3D.XZY_RIGHT_HAND.getDownVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, -1, 0), v);
		}

		@DisplayName("() XZY_LEFT_HAND")
		@Test
		public void test_4() {
			Vector3D v;
			v = CoordinateSystem3D.XZY_LEFT_HAND.getDownVector();
			assertEpsilonEquals(new InnerComputationVector3D(0, -1, 0), v);
		}
	
		@DisplayName("(Tuple3D) XYZ_RIGHT_HAND")
		@Test
		public void test_5() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XYZ_RIGHT_HAND.getDownVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, -1), t);
		}
		
		@DisplayName("(Tuple3D) XYZ_LEFT_HAND")
		@Test
		public void test_6() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XYZ_LEFT_HAND.getDownVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, 0, -1), t);
		}
		
		@DisplayName("(Tuple3D) XZY_RIGHT_HAND")
		@Test
		public void test_7() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XZY_RIGHT_HAND.getDownVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, -1, 0), t);
		}
		
		@DisplayName("(Tuple3D) XZY_LEFT_HAND")
		@Test
		public void test_8() {
			Vector3D t = new InnerComputationVector3D();
			Vector3D v;
			v = CoordinateSystem3D.XZY_LEFT_HAND.getDownVector(t);
			assertSame(t, v);
			assertEpsilonEquals(new InnerComputationVector3D(0, -1, 0), t);
		}
	
	}

	@DisplayName("isLeftHanded")
	@Nested
	public class IsLeftHanded {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_1() {
			assertFalse(CoordinateSystem3D.XYZ_RIGHT_HAND.isLeftHanded());
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_2() {
			assertTrue(CoordinateSystem3D.XYZ_LEFT_HAND.isLeftHanded());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_3() {
			assertFalse(CoordinateSystem3D.XZY_RIGHT_HAND.isLeftHanded());
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_4() {
			assertTrue(CoordinateSystem3D.XZY_LEFT_HAND.isLeftHanded());
		}

	}

	@DisplayName("isRightHanded")
	@Nested
	public class IsRightHanded {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_1() {
			assertTrue(CoordinateSystem3D.XYZ_RIGHT_HAND.isRightHanded());
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_2() {
			assertFalse(CoordinateSystem3D.XYZ_LEFT_HAND.isRightHanded());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_3() {
			assertTrue(CoordinateSystem3D.XZY_RIGHT_HAND.isRightHanded());
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_4() {
			assertFalse(CoordinateSystem3D.XZY_LEFT_HAND.isRightHanded());
		}

	}

	@DisplayName("isZOnUp")
	@Nested
	public class IsZOnUp {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_1() {
			assertTrue(CoordinateSystem3D.XYZ_RIGHT_HAND.isZOnUp());
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_2() {
			assertTrue(CoordinateSystem3D.XYZ_LEFT_HAND.isZOnUp());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_3() {
			assertFalse(CoordinateSystem3D.XZY_RIGHT_HAND.isZOnUp());
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_4() {
			assertFalse(CoordinateSystem3D.XZY_LEFT_HAND.isZOnUp());
		}

	}

	@DisplayName("isYOnUp")
	@Nested
	public class IsYOnUp {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_1() {
			assertFalse(CoordinateSystem3D.XYZ_RIGHT_HAND.isYOnUp());
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_2() {
			assertFalse(CoordinateSystem3D.XYZ_LEFT_HAND.isYOnUp());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_3() {
			assertTrue(CoordinateSystem3D.XZY_RIGHT_HAND.isYOnUp());
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_4() {
			assertTrue(CoordinateSystem3D.XZY_LEFT_HAND.isYOnUp());
		}

	}

	@DisplayName("isZOnSide")
	@Nested
	public class IsZOnSide {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_1() {
			assertFalse(CoordinateSystem3D.XYZ_RIGHT_HAND.isZOnSide());
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_2() {
			assertFalse(CoordinateSystem3D.XYZ_LEFT_HAND.isZOnSide());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_3() {
			assertTrue(CoordinateSystem3D.XZY_RIGHT_HAND.isZOnSide());
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_4() {
			assertTrue(CoordinateSystem3D.XZY_LEFT_HAND.isZOnSide());
		}

	}

	@DisplayName("isYOnSide")
	@Nested
	public class IsUOnSide {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_1() {
			assertTrue(CoordinateSystem3D.XYZ_RIGHT_HAND.isYOnSide());
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_2() {
			assertTrue(CoordinateSystem3D.XYZ_LEFT_HAND.isYOnSide());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_3() {
			assertFalse(CoordinateSystem3D.XZY_RIGHT_HAND.isYOnSide());
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_4() {
			assertFalse(CoordinateSystem3D.XZY_LEFT_HAND.isYOnSide());
		}

	}

	@DisplayName("getDefaultCoordinateSystem")
	@Nested
	public class GetDefaultCoordinateSystem {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("isRightHanded")
		@Test
		public void isRightHanded() {
			assertTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isRightHanded());
		}

		@DisplayName("Instance")
		@Test
		public void instance() {
			CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
			assertSame(CoordinateSystemConstants.SIMULATION_3D, cs);
		}

	}

	@DisplayName("SIMULATION_3D")
	@Nested
	public class SIMULATION_3D {

		@DisplayName("#1")
		@Test
		public void test_1() {
			CoordinateSystem3D cs = CoordinateSystemConstants.SIMULATION_3D;
			assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND,cs);
		}

	}

	@DisplayName("toCoordinateSystem2D")
	@Nested
	public class ToCoordinateSystem2D {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_1() {
			assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D());
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_2() {
			assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_3() {
			assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D());
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_4() {
			assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D());
		}

	}

	@DisplayName("fromVectors")
	@Nested
	public class FromVectors {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("(double,double) with invalid params")
		@Test
		public void test_1() {
			assertThrows(AssertionError.class, () -> CoordinateSystem3D.fromVectors(0., 0.));
		}

		@DisplayName("(double,double) #1")
		@Test
		public void test_2() {
			assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.fromVectors(1., Double.NaN));
		}

		@DisplayName("(double,double) #2")
		@Test
		public void test_3() {
			assertSame(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.fromVectors(-1., Double.NaN));
		}

		@DisplayName("(double,double) #3")
		@Test
		public void test_4() {
			assertSame(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.fromVectors(0., 1.));
		}

		@DisplayName("(double,double) #4")
		@Test
		public void test_5() {
			assertSame(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.fromVectors(0., -1.));
		}
	
		@DisplayName("(int,int) with invalid params")
		@Test
		public void test_6() {
			assertThrows(AssertionError.class, () -> CoordinateSystem3D.fromVectors(0, 0));
		}
	
		@DisplayName("(int,int) #1")
		@Test
		public void test_7() {
			assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.fromVectors(1, 0));
		}
		
		@DisplayName("(int,int) #2")
		@Test
		public void test_8() {
			assertSame(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.fromVectors(-1, 0));
		}
		
		@DisplayName("(int,int) #3")
		@Test
		public void test_9() {
			assertSame(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.fromVectors(0, 1));
		}
		
		@DisplayName("(int,int) #4")
		@Test
		public void test_10() {
			assertSame(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.fromVectors(0, -1));
		}

	}

	@DisplayName("toSystem(Tuple3D)")
	@Nested
	public class ToSystemTuple3D {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		private void doTestToSystem(CoordinateSystem3D input, CoordinateSystem3D output, Point3D[] points) {
			// Even index: points to convert
			// Odd index: expected result of the conversion for the previous even index point
			Point3D p;
			for (int i = 0; i < points.length; i += 2) {
				p = new InnerComputationPoint3D(points[i]);
				input.toSystem(p, output);
				assertEpsilonEquals(points[i + 1], p, "index=" + i + "; Original point=" + points[i]);
			}		
		}
	
		@DisplayName("XYZ-LH => XYZ-LH")
		@Test
		public void toSystemTuple3D_XYZL_XYZL() {
			doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, 78, 36),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, -78, 36),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, -78, 36),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, 78, 36),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, 78, -36),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, -78, -36),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, -78, -36),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, 78, -36),
			});
		}
	
		@DisplayName("XYZ-LH => XYZ-RH")
		@Test
		public void toSystemTuple3D_XYZL_XYZR() {
			doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, -78, 36),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, 78, 36),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, 78, 36),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, -78, 36),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, -78, -36),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, 78, -36),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, 78, -36),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, -78, -36),
			});
		}
	
		@DisplayName("XYZ-LH => XZY-LH")
		@Test
		public void toSystemTuple3D_XYZL_XZYL() {
			doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, 36, -78),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, 36, 78),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, 36, 78),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, 36, -78),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, -36, -78),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, -36, 78),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, -36, 78),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, -36, -78),
			});
		}
	
		@DisplayName("XYZ-LH => XZY-RH")
		@Test
		public void toSystemTuple3D_XYZL_XZYR() {
			doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, 36, 78),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, 36, -78),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, 36, -78),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, 36, 78),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, -36, 78),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, -36, -78),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, -36, -78),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, -36, 78),
			});
		}
	
		@DisplayName("XYZ-RH => XYZ-RH")
		@Test
		public void toSystemTuple3D_XYZR_XYZR() {
			doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, 78, 36),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, -78, 36),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, -78, 36),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, 78, 36),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, 78, -36),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, -78, -36),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, -78, -36),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, 78, -36),
			});
		}
	
		@DisplayName("XYZ-RH => XYZ-LH")
		@Test
		public void toSystemTuple3D_XYZR_XYZL() {
			doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, -78, 36),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, 78, 36),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, 78, 36),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, -78, 36),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, -78, -36),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, 78, -36),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, 78, -36),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, -78, -36),
			});
		}
	
		@DisplayName("XYZ-RH => XZY-LH")
		@Test
		public void toSystemTuple3D_XYZR_XZYR() {
			doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, 36, -78),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, 36, 78),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, 36, 78),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, 36, -78),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, -36, -78),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, -36, 78),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, -36, 78),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, -36, -78),
			});
		}
	
		@DisplayName("XYZ-RH => XZY-LH")
		@Test
		public void toSystemTuple3D_XYZR_XZYL() {
			doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, 36, 78),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, 36, -78),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, 36, -78),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, 36, 78),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, -36, 78),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, -36, -78),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, -36, -78),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, -36, 78),
			});
		}
	
		@DisplayName("XZY-LH => XZY-LH")
		@Test
		public void toSystemTuple3D_XZYL_XZYL() {
			doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, 78, 36),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, -78, 36),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, -78, 36),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, 78, 36),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, 78, -36),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, -78, -36),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, -78, -36),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, 78, -36),
			});
		}
	
		@DisplayName("XZY-LH => XZY-RH")
		@Test
		public void toSystemTuple3D_XZYL_XZYR() {
			doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, -78, 36),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, 78, 36),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, 78, 36),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, -78, 36),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, -78, -36),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, 78, -36),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, 78, -36),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, -78, -36),
			});
		}
	
		@DisplayName("XZY-LH => XYZ-LH")
		@Test
		public void toSystemTuple3D_XZYL_XYZL() {
			doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, 36, -78),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, 36, 78),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, 36, 78),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, 36, -78),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, -36, -78),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, -36, 78),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, -36, 78),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, -36, -78),
			});
		}
	
		@DisplayName("XZY-LH => XXY-RH")
		@Test
		public void toSystemTuple3D_XZYL_XYZR() {
			doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, 36, 78),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, 36, -78),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, 36, -78),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, 36, 78),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, -36, 78),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, -36, -78),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, -36, -78),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, -36, 78),
			});
		}
	
		@DisplayName("XZY-RH => XZY-RH")
		@Test
		public void toSystemTuple3D_XZYR_XZYL() {
			doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, 78, 36),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, -78, 36),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, -78, 36),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, 78, 36),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, 78, -36),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, -78, -36),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, -78, -36),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, 78, -36),
			});
		}
	
		@DisplayName("XZY-RH => XZY-LH")
		@Test
		public void toSystemTuple3D_XZYR_XZYR() {
			doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, -78, 36),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, 78, 36),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, 78, 36),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, -78, 36),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, -78, -36),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, 78, -36),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, 78, -36),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, -78, -36),
			});
		}
	
		@DisplayName("XZY-RH => XYZ-RH")
		@Test
		public void toSystemTuple3D_XZYR_XYZL() {
			doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, 36, -78),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, 36, 78),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, 36, 78),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, 36, -78),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, -36, -78),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, -36, 78),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, -36, 78),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, -36, -78),
			});
		}
	
		@DisplayName("XZY-RH => XZY-LH")
		@Test
		public void toSystemTuple3D_XZYR_XYZR() {
			doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, 36, 78),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, 36, -78),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, 36, -78),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, 36, 78),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, -36, 78),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, -36, -78),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, -36, -78),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, -36, 78),
			});
		}

	}
	
	@DisplayName("toSystem(Quaternion)")
	@Nested
	public class ToSystemQuaternion {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		private void doTestToSystem(CoordinateSystem3D input, CoordinateSystem3D output, Quaternion<?, ?, ?>[] rotations) {
			// Even index: points to convert
			// Odd index: expected result of the conversion for the previous even index point
			Quaternion<?, ?, ?> q;
			for (int i = 0; i < rotations.length; i += 2) {
				q = new InnerComputationQuaternion(rotations[i]);
				input.toSystem(q, output);
				assertEpsilonEquals(rotations[i + 1], q, "index=" + i + "; Original quaternion=" + rotations[i]);
			}		
		}
	
		@DisplayName("XYZ-LH => XYZ-LH")
		@Test
		public void toSystemQuaternion_XYZL_XYZL() {
			doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 78, 36, 12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 78, 36, -12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 78, -36, 12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 78, -36, -12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -78, 36, 12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -78, 36, -12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -78, -36, 12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -78, -36, -12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 78, 36, 12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 78, 36, -12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 78, -36, 12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 78, -36, -12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -78, 36, 12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -78, 36, -12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -78, -36, 12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -78, -36, -12),
			});
		}
	
		@DisplayName("XYZ-LH => XYZ-RH")
		@Test
		public void toSystemQuaternion_XYZL_XYZR() {
			doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, -78, 36, -12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, -78, 36, 12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -78, -36, -12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -78, -36, 12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 78, 36, -12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 78, 36, 12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, 78, -36, -12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, 78, -36, 12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, -78, 36, -12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, -78, 36, 12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -78, -36, -12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -78, -36, 12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 78, 36, -12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 78, 36, 12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, 78, -36, -12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, 78, -36, 12),
			});
		}
	
		@DisplayName("XYZ-LH => XZY-LH")
		@Test
		public void toSystemQuaternion_XYZL_XZYL() {
			doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 36, -78, -12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 36, -78, 12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -36, -78, -12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -36, -78, 12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 36, 78, -12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 36, 78, 12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -36, 78, -12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -36, 78, 12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 36, -78, -12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 36, -78, 12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -36, -78, -12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -36, -78, 12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 36, 78, -12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 36, 78, 12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -36, 78, -12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -36, 78, 12),
			});
		}
	
		@DisplayName("XYZ-LH => XZY-RH")
		@Test
		public void toSystemQuaternion_XYZL_XZYR() {
			doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 36, 78, 12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 36, 78, -12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -36, 78, 12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -36, 78, -12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 36, -78, 12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 36, -78, -12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -36, -78, 12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -36, -78, -12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 36, 78, 12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 36, 78, -12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -36, 78, 12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -36, 78, -12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 36, -78, 12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 36, -78, -12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -36, -78, 12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -36, -78, -12),
			});
		}
	
		@DisplayName("XYZ-RH => XYZ-RH")
		@Test
		public void toSystemQuaternion_XYZR_XYZR() {
			doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 78, 36, 12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 78, 36, -12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 78, -36, 12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 78, -36, -12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -78, 36, 12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -78, 36, -12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -78, -36, 12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -78, -36, -12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 78, 36, 12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 78, 36, -12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 78, -36, 12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 78, -36, -12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -78, 36, 12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -78, 36, -12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -78, -36, 12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -78, -36, -12),
			});
		}
	
		@DisplayName("XYZ-RH => XYZ-LH")
		@Test
		public void toSystemQuaternion_XYZR_XYZL() {
			doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, -78, 36, -12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, -78, 36, 12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -78, -36, -12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -78, -36, 12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 78, 36, -12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 78, 36, 12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, 78, -36, -12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, 78, -36, 12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, -78, 36, -12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, -78, 36, 12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -78, -36, -12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -78, -36, 12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 78, 36, -12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 78, 36, 12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, 78, -36, -12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, 78, -36, 12),
			});
		}
	
		@DisplayName("XYZ-RH => XZY-RH")
		@Test
		public void toSystemQuaternion_XYZR_XZYR() {
			doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 36, -78, -12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 36, -78, 12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -36, -78, -12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -36, -78, 12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 36, 78, -12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 36, 78, 12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -36, 78, -12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -36, 78, 12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 36, -78, -12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 36, -78, 12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -36, -78, -12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -36, -78, 12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 36, 78, -12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 36, 78, 12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -36, 78, -12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -36, 78, 12),
			});
		}
	
		@DisplayName("XYZ-RH => XZY-LH")
		@Test
		public void toSystemQuaternion_XYZR_XZYL() {
			doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 36, 78, 12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 36, 78, -12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -36, 78, 12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -36, 78, -12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 36, -78, 12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 36, -78, -12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -36, -78, 12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -36, -78, -12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 36, 78, 12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 36, 78, -12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -36, 78, 12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -36, 78, -12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 36, -78, 12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 36, -78, -12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -36, -78, 12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -36, -78, -12),
			});
		}
	
		@DisplayName("XZY-LH => XZY-LH")
		@Test
		public void toSystemQuaternion_XZYL_XZYL() {
			doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 78, 36, 12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 78, 36, -12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 78, -36, 12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 78, -36, -12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -78, 36, 12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -78, 36, -12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -78, -36, 12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -78, -36, -12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 78, 36, 12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 78, 36, -12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 78, -36, 12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 78, -36, -12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -78, 36, 12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -78, 36, -12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -78, -36, 12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -78, -36, -12),
			});
		}
	
		@DisplayName("XZY-LH => XZY-RH")
		@Test
		public void toSystemQuaternion_XZYL_XZYR() {
			doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 78, -36, -12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 78, -36, 12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 78, 36, -12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 78, 36, 12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -78, -36, -12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -78, -36, 12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -78, 36, -12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -78, 36, 12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 78, -36, -12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 78, -36, 12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 78, 36, -12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 78, 36, 12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -78, -36, -12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -78, -36, 12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -78, 36, -12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -78, 36, 12),
			});
		}
	
		@DisplayName("XZY-LH => XYZ-LH")
		@Test
		public void toSystemQuaternion_XZYL_XYZL() {
			doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, -36, 78, -12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, -36, 78, 12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 36, 78, -12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 36, 78, 12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -36, -78, -12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -36, -78, 12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, 36, -78, -12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, 36, -78, 12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, -36, 78, -12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, -36, 78, 12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 36, 78, -12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 36, 78, 12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -36, -78, -12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -36, -78, 12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, 36, -78, -12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, 36, -78, 12),
			});
		}
	
		@DisplayName("XZY-LH => XYZ-RH")
		@Test
		public void toSystemQuaternion_XZYL_XYZR() {
			doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 36, 78, 12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 36, 78, -12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -36, 78, 12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -36, 78, -12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 36, -78, 12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 36, -78, -12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -36, -78, 12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -36, -78, -12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 36, 78, 12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 36, 78, -12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -36, 78, 12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -36, 78, -12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 36, -78, 12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 36, -78, -12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -36, -78, 12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -36, -78, -12),
			});
		}
	
		@DisplayName("XZY-RH => XZY-RH")
		@Test
		public void toSystemQuaternion_XZYR_XZYR() {
			doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 78, 36, 12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 78, 36, -12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 78, -36, 12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 78, -36, -12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -78, 36, 12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -78, 36, -12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -78, -36, 12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -78, -36, -12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 78, 36, 12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 78, 36, -12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 78, -36, 12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 78, -36, -12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -78, 36, 12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -78, 36, -12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -78, -36, 12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -78, -36, -12),
			});
		}
	
		@DisplayName("XZY-RH => XZY-LH")
		@Test
		public void toSystemQuaternion_XZYR_XZYL() {
			doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 78, -36, -12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 78, -36, 12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 78, 36, -12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 78, 36, 12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -78, -36, -12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -78, -36, 12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -78, 36, -12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -78, 36, 12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 78, -36, -12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 78, -36, 12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 78, 36, -12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 78, 36, 12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -78, -36, -12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -78, -36, 12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -78, 36, -12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -78, 36, 12),
			});
		}
	
		@DisplayName("XZY-RH => XYZ-RH")
		@Test
		public void toSystemQuaternion_XZYR_XYZR() {
			doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, -36, 78, -12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, -36, 78, 12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 36, 78, -12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 36, 78, 12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -36, -78, -12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -36, -78, 12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, 36, -78, -12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, 36, -78, 12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, -36, 78, -12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, -36, 78, 12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 36, 78, -12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 36, 78, 12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -36, -78, -12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -36, -78, 12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, 36, -78, -12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, 36, -78, 12),
			});
		}
	
		@DisplayName("XZY-RH => XYZ-LH")
		@Test
		public void toSystemQuaternion_XZYR_XYZL() {
			doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 36, 78, 12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 36, 78, -12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -36, 78, 12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -36, 78, -12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 36, -78, 12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 36, -78, -12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -36, -78, 12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -36, -78, -12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 36, 78, 12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 36, 78, -12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -36, 78, 12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -36, 78, -12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 36, -78, 12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 36, -78, -12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -36, -78, 12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -36, -78, -12),
			});
		}

	}

	@DisplayName("toSystem(Transform3D)")
	@Nested
	public class ToSystemTransform3D {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		private void doTestToSystem(CoordinateSystem3D input, CoordinateSystem3D output, Quaternion<?, ?, ?>[] rotations, Point3D[] translations) {
			// Even index: points to convert
			// Odd index: expected result of the conversion for the previous even index point
			for (int i = 0; i < rotations.length; ++i) {
				final InnerComputationQuaternion q = new InnerComputationQuaternion(rotations[i]);
				final InnerComputationQuaternion qt = new InnerComputationQuaternion(q);
				input.toSystem(qt, output);
	
				for (int j = 0; j < translations.length; ++j) {
					final Point3D p = new InnerComputationPoint3D(translations[j]);
					final Point3D pt = new InnerComputationPoint3D(p);
					input.toSystem(pt, output);
	
					Transform3D transform = new Transform3D();
					transform.setTranslation(p);
					transform.setRotation(q);
					input.toSystem(transform, output);
	
					Transform3D transformt = new Transform3D();
					transformt.setTranslation(pt);
					transformt.setRotation(qt);
	
					assertEpsilonEquals(transformt, transform, () -> "Original quaternion=" + q + "; Original vector=" + p);
				}
			}		
		}
	
		private static Stream<Arguments> providePairsOfCoordinateSystem3D() {
			final List<Arguments> arguments = new ArrayList<>();
			for (final CoordinateSystem3D s1 : CoordinateSystem3D.values()) {
				for (final CoordinateSystem3D s2 : CoordinateSystem3D.values()) {
					arguments.add(Arguments.of(s1, s2));
				}
			}
			return arguments.stream();
		}
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index}: {0} => {1}")
		@MethodSource("providePairsOfCoordinateSystem3D")
		public void toSystemTransform3D(CoordinateSystem3D csIn, CoordinateSystem3D csOut) {
			doTestToSystem(csIn, csOut,
					new InnerComputationQuaternion[] {
							newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 36, 78, 12),
							newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 36, 78, -12),
							newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -36, 78, 12),
							newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -36, 78, -12),
							newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 36, -78, 12),
							newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 36, -78, -12),
							newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -36, -78, 12),
							newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -36, -78, -12),
							newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 36, 78, 12),
							newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 36, 78, -12),
							newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -36, 78, 12),
							newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -36, 78, -12),
							newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 36, -78, 12),
							newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 36, -78, -12),
							newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -36, -78, 12),
							newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -36, -78, -12),
			},
					new InnerComputationPoint3D[] {
							new InnerComputationPoint3D(-45, 78, 36), new InnerComputationPoint3D(-45, -78, 36),
							new InnerComputationPoint3D(45, -78, 36), new InnerComputationPoint3D(45, 78, 36),
							new InnerComputationPoint3D(-45, -78, 36), new InnerComputationPoint3D(-45, 78, 36),
							new InnerComputationPoint3D(45, 78, 36), new InnerComputationPoint3D(45, -78, 36),
							new InnerComputationPoint3D(-45, 78, -36), new InnerComputationPoint3D(-45, -78, -36),
							new InnerComputationPoint3D(45, -78, -36), new InnerComputationPoint3D(45, 78, -36),
							new InnerComputationPoint3D(-45, -78, -36), new InnerComputationPoint3D(-45, 78, -36),
							new InnerComputationPoint3D(45, 78, -36), new InnerComputationPoint3D(45, -78, -36),
			});
		}

	}

	@DisplayName("toCoordinateSystem2D(Tuple3D,Tuple2D)")
	@Nested
	public class ToCoordinateSystem2DTuple3DTuple2D {

		private Point3D in;
		private Point2D out;

		@BeforeEach
		public void setUp() {
			this.in = new InnerComputationPoint3D(459, 658, 145);
			this.out = new InnerComputationPoint2D();
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(in, out);
			assertEpsilonEquals(459, out.getX());
			assertEpsilonEquals(658, out.getY());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(in, out);
			assertEpsilonEquals(459, out.getX());
			assertEpsilonEquals(658, out.getY());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(in, out);
			assertEpsilonEquals(459, out.getX());
			assertEpsilonEquals(145, out.getY());
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(in, out);
			assertEpsilonEquals(459, out.getX());
			assertEpsilonEquals(145, out.getY());
		}

	}

	@DisplayName("toCoordinateSystem2D(Quaternion)")
	@Nested
	public class ToCoordinateSystem2DQuaternion {

		private Quaternion<?, ?, ?> in;

		@BeforeEach
		public void stUp() {
			this.in = new InnerComputationQuaternion();
			in.setAxisAngle(1, 1, 1, MathConstants.QUARTER_PI);
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_1() {
			assertEpsilonEquals(.5612027282726586, CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(in));
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_2() {
			assertEpsilonEquals(.5612027282726586, CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(in));
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_3() {
			assertEpsilonEquals(.36836692032349705, CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(in));
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_4() {
			assertEpsilonEquals(.36836692032349705, CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(in));
		}

	}

	@DisplayName("fromCoordinateSystem2D(Tuple2D,Tuple3D)")
	@Nested
	public class FromCoordinateSystem2DTuple2DTuple3D {

		private Point2D in;
		private Point3D out;

		@BeforeEach
		public void setUp() {
			this.in = new InnerComputationPoint2D(459, 658);
			this.out = new InnerComputationPoint3D();
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_1() {
			CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(in, 145, out);
			assertEpsilonEquals(459, out.getX());
			assertEpsilonEquals(658, out.getY());
			assertEpsilonEquals(145, out.getZ());
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_2() {
			CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(in, 145, out);
			assertEpsilonEquals(459, out.getX());
			assertEpsilonEquals(658, out.getY());
			assertEpsilonEquals(145, out.getZ());
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_3() {
			CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(in, 145, out);
			assertEpsilonEquals(459, out.getX());
			assertEpsilonEquals(145, out.getY());
			assertEpsilonEquals(658, out.getZ());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_4() {
			CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(in, 145, out);
			assertEpsilonEquals(459, out.getX());
			assertEpsilonEquals(145, out.getY());
			assertEpsilonEquals(658, out.getZ());
		}

	}

	@DisplayName("fromCoordinateSystem2D(double,Quaternion)")
	@Nested
	public class FromCoordinateSystem2DDoubleQuaternion {

		private Point2D in;
		private Quaternion out;

		@BeforeEach
		public void setUp() {
			this.in = new InnerComputationPoint2D(459, 658);
			this.out = new InnerComputationQuaternion();
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_1() {
			CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(MathConstants.QUARTER_PI, out);
			assertEpsilonEquals(0, out.getX());
			assertEpsilonEquals(0, out.getY());
			assertEpsilonEquals(0.382683432, out.getZ());
			assertEpsilonEquals(0.9238795, out.getW());
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_2() {
			CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(MathConstants.QUARTER_PI, out);
			assertEpsilonEquals(0, out.getX());
			assertEpsilonEquals(0, out.getY());
			assertEpsilonEquals(0.382683432, out.getZ());
			assertEpsilonEquals(0.9238795, out.getW());
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_3() {
			CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(MathConstants.QUARTER_PI, out);
			assertEpsilonEquals(0, out.getX());
			assertEpsilonEquals(0.382683432, out.getY());
			assertEpsilonEquals(0, out.getZ());
			assertEpsilonEquals(0.9238795, out.getW());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_4() {
			CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(MathConstants.QUARTER_PI, out);
			assertEpsilonEquals(0, out.getX());
			assertEpsilonEquals(0.382683432, out.getY());
			assertEpsilonEquals(0, out.getZ());
			assertEpsilonEquals(0.9238795, out.getW());
		}

	}

	@DisplayName("getSideCoordinate(double,double,double)")
	@Nested
	public class GetSideCoordinateDoubleDoubleDouble {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_1() {
			assertEpsilonEquals(658, CoordinateSystem3D.XYZ_LEFT_HAND.getSideCoordinate(459, 658, 149));
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_2() {
			assertEpsilonEquals(658, CoordinateSystem3D.XYZ_RIGHT_HAND.getSideCoordinate(459, 658, 149));
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_3() {
			assertEpsilonEquals(149, CoordinateSystem3D.XZY_LEFT_HAND.getSideCoordinate(459, 658, 149));
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_4() {
			assertEpsilonEquals(149, CoordinateSystem3D.XZY_RIGHT_HAND.getSideCoordinate(459, 658, 149));
		}

	}
	
	@DisplayName("getVerticalCoordinate(double,double,double)")
	@Nested
	public class GetVerticalCoordinateDoubleDoubleDouble {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_1() {
			assertEpsilonEquals(149, CoordinateSystem3D.XYZ_LEFT_HAND.getVerticalCoordinate(459, 658, 149));
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_2() {
			assertEpsilonEquals(149, CoordinateSystem3D.XYZ_RIGHT_HAND.getVerticalCoordinate(459, 658, 149));
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_3() {
			assertEpsilonEquals(658, CoordinateSystem3D.XZY_LEFT_HAND.getVerticalCoordinate(459, 658, 149));
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_4() {
			assertEpsilonEquals(658, CoordinateSystem3D.XZY_RIGHT_HAND.getVerticalCoordinate(459, 658, 149));
		}

	}

	@DisplayName("setSideCoordinate(Tuple3D,double)")
	@Nested
	public class SetSideCoordinateTuple3DDouble {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_1() {
			Point3D p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XYZ_LEFT_HAND.setSideCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(459, p.getY());
			assertEpsilonEquals(9875, p.getZ());
		}
	
		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_2() {
			var p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XYZ_RIGHT_HAND.setSideCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(459, p.getY());
			assertEpsilonEquals(9875, p.getZ());
		}
		
		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_3() {
			var p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XZY_LEFT_HAND.setSideCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(1456, p.getY());
			assertEpsilonEquals(459, p.getZ());
		}
		
		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_4() {
			var p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XZY_RIGHT_HAND.setSideCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(1456, p.getY());
			assertEpsilonEquals(459, p.getZ());
		}

	}

	@DisplayName("setVerticalCoordinate(Tuple3D,double)")
	@Nested
	public class SetVerticalCoordinateTuple3DDouble {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_1() {
			Point3D p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XYZ_LEFT_HAND.setVerticalCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(1456, p.getY());
			assertEpsilonEquals(459, p.getZ());
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_2() {
			var p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XYZ_RIGHT_HAND.setVerticalCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(1456, p.getY());
			assertEpsilonEquals(459, p.getZ());
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_3() {
			var p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XZY_LEFT_HAND.setVerticalCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(459, p.getY());
			assertEpsilonEquals(9875, p.getZ());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_4() {
			var p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XZY_RIGHT_HAND.setVerticalCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(459, p.getY());
			assertEpsilonEquals(9875, p.getZ());
		}

	}

	@DisplayName("addSideCoordinate(Tuple3D,double)")
	@Nested
	public class AddSideCoordinateTuple3DDouble {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_1() {
			Point3D p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XYZ_LEFT_HAND.addSideCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(1915, p.getY());
			assertEpsilonEquals(9875, p.getZ());
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_2() {
			var p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XYZ_RIGHT_HAND.addSideCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(1915, p.getY());
			assertEpsilonEquals(9875, p.getZ());
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_3() {
			var p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XZY_LEFT_HAND.addSideCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(1456, p.getY());
			assertEpsilonEquals(10334, p.getZ());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_4() {
			var p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XZY_RIGHT_HAND.addSideCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(1456, p.getY());
			assertEpsilonEquals(10334, p.getZ());
		}
	}

	@DisplayName("addVerticalCoordinate(Tuple3D,double)")
	@Nested
	public class AddVerticalCoordinateTuple3DDouble {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_1() {
			Point3D p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XYZ_LEFT_HAND.addVerticalCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(1456, p.getY());
			assertEpsilonEquals(10334, p.getZ());
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_2() {
			var p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XYZ_RIGHT_HAND.addVerticalCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(1456, p.getY());
			assertEpsilonEquals(10334, p.getZ());
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_3() {
			var p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XZY_LEFT_HAND.addVerticalCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(1915, p.getY());
			assertEpsilonEquals(9875, p.getZ());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_4() {
			var p = new InnerComputationPoint3D(1236, 1456, 9875);
			CoordinateSystem3D.XZY_RIGHT_HAND.addVerticalCoordinate(p, 459);
			assertEpsilonEquals(1236, p.getX());
			assertEpsilonEquals(1915, p.getY());
			assertEpsilonEquals(9875, p.getZ());
		}

	}

	@DisplayName("setDefaultCoordinateSystem")
	@Nested
	public class SetDefaultCoordinateSystem {

		@BeforeEach
		public void setUp() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XYZ_LEFT_HAND")
		@Test
		public void test_1() {
			CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XYZ_LEFT_HAND);
			assertSame(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.getDefaultCoordinateSystem());
		}

		@DisplayName("XZY_RIGHT_HAND")
		@Test
		public void test_2() {
			CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XZY_RIGHT_HAND);
			assertSame(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.getDefaultCoordinateSystem());
		}

		@DisplayName("XZY_LEFT_HAND")
		@Test
		public void test_3() {
			CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XZY_LEFT_HAND);
			assertSame(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.getDefaultCoordinateSystem());
		}

		@DisplayName("XYZ_RIGHT_HAND")
		@Test
		public void test_4() {
			CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XYZ_RIGHT_HAND);
			assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.getDefaultCoordinateSystem());
		}

		@DisplayName("null")
		@Test
		public void test_5() {
			CoordinateSystem3D.setDefaultCoordinateSystem(null);
			assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.getDefaultCoordinateSystem());
		}

	}

}
