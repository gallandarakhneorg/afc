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

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystemConstants;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link CoordinateSystem2D}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@DisplayName("CoordinateSystem2D")
@SuppressWarnings("all")
public class CoordinateSystem2DTest extends AbstractMathTestCase {

	@DisplayName("getDimensions")
	@Nested
	public class GetDimensions {

		@BeforeEach
		public void setUp() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XY_RIGHT_HAND")
		@Test
		public void test_1() {
			assertEquals(2, CoordinateSystem2D.XY_RIGHT_HAND.getDimensions());
		}

		@DisplayName("XY_LEFT_HAND")
		@Test
		public void test_2() {
			assertEquals(2, CoordinateSystem2D.XY_LEFT_HAND.getDimensions());
		}

	}

	@DisplayName("getViewVector")
	@Nested
	public class GetViewVector {

		@BeforeEach
		public void setUp() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("()")
		@Test
		public void test_1() {
			Vector2D v;
			v = CoordinateSystem2D.getViewVector();
			assertFpVectorEquals(1, 0, v);
		}

		@DisplayName("Inline")
		@Test
		public void test_2() {
			assertInlineParameterUsage(CoordinateSystem2D.class, "getViewVector"); //$NON-NLS-1$
		}
		
		@DisplayName("(Tuple2D)")
		@Test
		public void getViewVectorTuple2D() {
			Vector2D t = new InnerComputationVector2D();
			Vector2D v;
			v = CoordinateSystem2D.getViewVector(t);
			assertSame(t, v);
			assertFpVectorEquals(1, 0, t);
		}

	}

	@DisplayName("getLeftVector")
	@Nested
	public class GetLeftVector {

		@BeforeEach
		public void setUp() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("() XY_RIGHT_HAND")
		@Test
		public void test_1() {
			Vector2D v;
			v = CoordinateSystem2D.XY_RIGHT_HAND.getLeftVector();
			assertFpVectorEquals(0, 1, v);
		}
		
		@DisplayName("() XY_LEFT_HAND")
		@Test
		public void test_2() {
			Vector2D v;
			v = CoordinateSystem2D.XY_LEFT_HAND.getLeftVector();
			assertFpVectorEquals(0, -1, v);
		}

		@DisplayName("(Tuple2D) XY_RIGHT_HAND")
		@Test
		public void test_3() {
			Vector2D t = new InnerComputationVector2D();
			Vector2D v;
			v = CoordinateSystem2D.XY_RIGHT_HAND.getLeftVector(t);
			assertSame(t, v);
			assertFpVectorEquals(0, 1, t);
		}

		@DisplayName("(Tuple2D) XY_LEFT_HAND")
		@Test
		public void test_4() {
			Vector2D t = new InnerComputationVector2D();
			Vector2D v;
			v = CoordinateSystem2D.XY_LEFT_HAND.getLeftVector(t);
			assertSame(t, v);
			assertFpVectorEquals(0, -1, t);
		}

	}

	@DisplayName("getRightVector")
	@Nested
	public class GetRightVector {

		@BeforeEach
		public void setUp() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("() XY_RIGHT_HAND")
		@Test
		public void test_1() {
			Vector2D v;
			v = CoordinateSystem2D.XY_RIGHT_HAND.getRightVector();
			assertFpVectorEquals(0, -1, v);
		}

		@DisplayName("() XY_LEFT_HAND")
		@Test
		public void test_2() {
			Vector2D v;
			v = CoordinateSystem2D.XY_LEFT_HAND.getRightVector();
			assertFpVectorEquals(0, 1, v);
		}
		
		@DisplayName("(Tuple2D) XY_RIGHT_HAND")
		@Test
		public void test_3() {
			Vector2D t = new InnerComputationVector2D();
			Vector2D v;
			v = CoordinateSystem2D.XY_RIGHT_HAND.getRightVector(t);
			assertSame(t, v);
			assertFpVectorEquals(0, -1, t);
		}

		@DisplayName("(Tuple2D) XY_LEFT_HAND")
		@Test
		public void test_4() {
			Vector2D t = new InnerComputationVector2D();
			Vector2D v;
			v = CoordinateSystem2D.XY_LEFT_HAND.getRightVector(t);
			assertSame(t, v);
			assertFpVectorEquals(0, 1, t);
		}

	}

	@DisplayName("isLeftHanded")
	@Nested
	public class IsLeftHanded {

		@BeforeEach
		public void setUp() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("() XY_RIGHT_HAND")
		@Test
		public void test_1() {
			assertFalse(CoordinateSystem2D.XY_RIGHT_HAND.isLeftHanded());
		}

		@DisplayName("() XY_LEFT_HAND")
		@Test
		public void test_2() {
			assertTrue(CoordinateSystem2D.XY_LEFT_HAND.isLeftHanded());
		}
	}
	
	@DisplayName("isRightHanded")
	@Nested
	public class IsRightHanded {

		@DisplayName("() XY_RIGHT_HAND")
		@Test
		public void test_1() {
			assertTrue(CoordinateSystem2D.XY_RIGHT_HAND.isRightHanded());
		}

		@DisplayName("() XY_LEFT_HAND")
		@Test
		public void test_2() {
			assertFalse(CoordinateSystem2D.XY_LEFT_HAND.isRightHanded());
		}
	}

	@DisplayName("getDefaultCoordinateSystem")
	@Nested
	public class GetDefaultCoordinateSystem {

		@BeforeEach
		public void setUp() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}
		
		@DisplayName("isRightHanded")
		@Test
		public void isRightHanded() {
			assertTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		}

		@DisplayName("getDefaultCoordinateSystem")
		@Test
		public void getDefaultCoordinateSystem() {
			CoordinateSystem2D cs = CoordinateSystem2D.getDefaultCoordinateSystem();
			assertSame(CoordinateSystemConstants.SIMULATION_2D, cs);
		}

	}

	@DisplayName("toCoordinateSystem3D")
	@Nested
	public class ToCoordinateSystem3D {

		@BeforeEach
		public void setUp() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XY_RIGHT_HAND")
		@Test
		public void test_1() {
			assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem2D.XY_RIGHT_HAND.toCoordinateSystem3D());
		}

		@DisplayName("XY_LEFT_HAND")
		@Test
		public void test_2() {
			assertSame(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem2D.XY_LEFT_HAND.toCoordinateSystem3D());
		}

	}

	@DisplayName("fromVectors")
	@Nested
	public class FromVectors {

		@BeforeEach
		public void setUp() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("(double) invalid")
		@Test
		public void fromVectorsDouble_invalidParameter() {
			assertThrows(AssertionError.class, () -> CoordinateSystem2D.fromVectors(0.));
		}
	
		@DisplayName("(double) #1")
		@Test
		public void fromVectorsDouble_1() {
			assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem2D.fromVectors(1.));
		}

		@DisplayName("(double) #2")
		@Test
		public void fromVectorsDouble_2() {
			assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem2D.fromVectors(-1.));
		}
	
		@DisplayName("(int) invalid")
		@Test
		public void fromVectorsInt_invalidParameter() {
			assertThrows(AssertionError.class, () -> CoordinateSystem2D.fromVectors(0));
		}
	
		@DisplayName("(int) #1")
		@Test
		public void fromVectorsInt_1() {
			assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem2D.fromVectors(1));
		}

		@DisplayName("(int) #2")
		@Test
		public void fromVectorsInt_2() {
			assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem2D.fromVectors(-1));
		}

	}

	@DisplayName("toDefault(Point2D)")
	@Nested
	public class ToDefaultPoint2D {

		private Point2D p1;
		private Point2D p2;
		private Point2D p3;
		private Point2D p4;
		private Point2D p;

		@BeforeEach
		public void setUp() {
			this.p1 = new InnerComputationPoint2D(-45, 78);
			p2 = new InnerComputationPoint2D(45, -78);
			p3 = new InnerComputationPoint2D(-45, -78);
			p4 = new InnerComputationPoint2D(45, 78);
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("default #1")
		@Test
		public void default_1() {
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpPointEquals(-45, 78, p);
		}

		@DisplayName("default #2")
		@Test
		public void default_2() {
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpPointEquals(45, -78, p);
		}

		@DisplayName("default #3")
		@Test
		public void default_3() {
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpPointEquals(-45, -78, p);
		}

		@DisplayName("default #4")
		@Test
		public void default_4() {
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpPointEquals(45, 78, p);
		}

		@DisplayName("default #5")
		@Test
		public void default_5() {
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpPointEquals(-45, -78, p);
		}

		@DisplayName("default #6")
		@Test
		public void default_6() {
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpPointEquals(45, 78, p);
		}

		@DisplayName("default #7")
		@Test
		public void default_7() {
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpPointEquals(-45, 78, p);
		}

		@DisplayName("default #8")
		@Test
		public void default_8() {
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpPointEquals(45, -78, p);
		}
	
		@DisplayName("right #1")
		@Test
		public void right_1() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpPointEquals(-45, 78, p);
		}
		
		@DisplayName("right #2")
		@Test
		public void right_2() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpPointEquals(45, -78, p);
		}
		
		@DisplayName("right #3")
		@Test
		public void right_3() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpPointEquals(-45, -78, p);
		}
		
		@DisplayName("right #4")
		@Test
		public void right_4() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpPointEquals(45, 78, p);
		}
		
		@DisplayName("right #5")
		@Test
		public void right_5() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpPointEquals(-45, -78, p);
		}
		
		@DisplayName("right #6")
		@Test
		public void right_6() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpPointEquals(45, 78, p);
		}
		
		@DisplayName("right #7")
		@Test
		public void right_7() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpPointEquals(-45, 78, p);
		}
		
		@DisplayName("right #8")
		@Test
		public void right_8() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpPointEquals(45, -78, p);
		}
		
		@DisplayName("left #1")
		@Test
		public void left_1() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpPointEquals(-45, -78, p);
		}
		
		@DisplayName("left #2")
		@Test
		public void left_2() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpPointEquals(45, 78, p);
		}
		
		@DisplayName("left #3")
		@Test
		public void left_3() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpPointEquals(-45, 78, p);
		}
		
		@DisplayName("left #4")
		@Test
		public void left_4() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpPointEquals(45, -78, p);
		}
		
		@DisplayName("left #5")
		@Test
		public void left_5() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpPointEquals(-45, 78, p);
		}
		
		@DisplayName("left #6")
		@Test
		public void left_6() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpPointEquals(45, -78, p);
		}
		
		@DisplayName("left #7")
		@Test
		public void left_7() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpPointEquals(-45, -78, p);
		}
		
		@DisplayName("left #8")
		@Test
		public void left_8() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpPointEquals(45, 78, p);
		}

	}

	@DisplayName("fromDefault(Point2D)")
	@Nested
	public class FromDefaultPoint2D {

		private Point2D p1;
		private Point2D p2;
		private Point2D p3;
		private Point2D p4;
		private Point2D p;

		@BeforeEach
		public void setUp() {
			this.p1 = new InnerComputationPoint2D(-45, 78);
			p2 = new InnerComputationPoint2D(45, -78);
			p3 = new InnerComputationPoint2D(-45, -78);
			p4 = new InnerComputationPoint2D(45, 78);
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("default #1")
		@Test
		public void default_1() {
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpPointEquals(-45, 78, p);
		}

		@DisplayName("default #2")
		@Test
		public void default_2() {
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpPointEquals(45, -78, p);
		}

		@DisplayName("default #3")
		@Test
		public void default_3() {
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpPointEquals(-45, -78, p);
		}

		@DisplayName("default #4")
		@Test
		public void default_4() {
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpPointEquals(45, 78, p);
		}

		@DisplayName("default #5")
		@Test
		public void default_5() {
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpPointEquals(-45, -78, p);
		}

		@DisplayName("default #6")
		@Test
		public void default_6() {
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpPointEquals(45, 78, p);
		}

		@DisplayName("default #7")
		@Test
		public void default_7() {
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpPointEquals(-45, 78, p);
		}

		@DisplayName("default #8")
		@Test
		public void default_8() {
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpPointEquals(45, -78, p);
		}

		@DisplayName("right #1")
		@Test
		public void right_1() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpPointEquals(-45, 78, p);
		}

		@DisplayName("right #2")
		@Test
		public void right_2() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpPointEquals(45, -78, p);
		}

		@DisplayName("right #3")
		@Test
		public void right_3() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpPointEquals(-45, -78, p);
		}

		@DisplayName("right #4")
		@Test
		public void right_4() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpPointEquals(45, 78, p);
		}

		@DisplayName("right #5")
		@Test
		public void right_5() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpPointEquals(-45, -78, p);
		}

		@DisplayName("right #6")
		@Test
		public void right_6() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpPointEquals(45, 78, p);
		}

		@DisplayName("right #7")
		@Test
		public void right_7() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpPointEquals(-45, 78, p);
		}

		@DisplayName("right #8")
		@Test
		public void right_8() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpPointEquals(45, -78, p);
		}

		@DisplayName("left #1")
		@Test
		public void left_1() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpPointEquals(-45, -78, p);
		}

		@DisplayName("left #2")
		@Test
		public void left_2() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpPointEquals(45, 78, p);
		}

		@DisplayName("left #3")
		@Test
		public void left_3() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpPointEquals(-45, 78, p);
		}

		@DisplayName("left #4")
		@Test
		public void left_4() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpPointEquals(45, -78, p);
		}

		@DisplayName("left #5")
		@Test
		public void left_5() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpPointEquals(-45, 78, p);
		}

		@DisplayName("left #6")
		@Test
		public void left_6() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpPointEquals(45, -78, p);
		}

		@DisplayName("left #7")
		@Test
		public void left_7() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpPointEquals(-45, -78, p);
		}

		@DisplayName("left #8")
		@Test
		public void left_8() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpPointEquals(45, 78, p);
		}

	}

	@DisplayName("toDefault(Vector2D)")
	@Nested
	public class ToDefaultVector2D {

		private Vector2D p1;
		private Vector2D p2;
		private Vector2D p3;
		private Vector2D p4;
		private Vector2D p;

		@BeforeEach
		public void setUp() {
			p1 = new InnerComputationVector2D(-45, 78);
			p2 = new InnerComputationVector2D(45, -78);
			p3 = new InnerComputationVector2D(-45, -78);
			p4 = new InnerComputationVector2D(45, 78);
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("default #1")
		@Test
		public void default_1() {
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpVectorEquals(-45, 78, p);
		}

		@DisplayName("default #2")
		@Test
		public void default_2() {
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpVectorEquals(45, -78, p);
		}

		@DisplayName("default #3")
		@Test
		public void default_3() {
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpVectorEquals(-45, -78, p);
		}

		@DisplayName("default #4")
		@Test
		public void default_4() {
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpVectorEquals(45, 78, p);
		}

		@DisplayName("default #5")
		@Test
		public void default_5() {
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpVectorEquals(-45, -78, p);
		}

		@DisplayName("default #6")
		@Test
		public void default_6() {
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpVectorEquals(45, 78, p);
		}

		@DisplayName("default #7")
		@Test
		public void default_7() {
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpVectorEquals(-45, 78, p);
		}

		@DisplayName("default #8")
		@Test
		public void default_8() {
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpVectorEquals(45, -78, p);
		}
	
		@DisplayName("XY_RIGHT_HAND #1")
		@Test
		public void rh_1() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpVectorEquals(-45, 78, p);
		}
		
		@DisplayName("XY_RIGHT_HAND #2")
		@Test
		public void rh_2() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpVectorEquals(45, -78, p);
		}
		
		@DisplayName("XY_RIGHT_HAND #3")
		@Test
		public void rh_3() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpVectorEquals(-45, -78, p);
		}
		
		@DisplayName("XY_RIGHT_HAND #4")
		@Test
		public void rh_4() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpVectorEquals(45, 78, p);
		}
		
		@DisplayName("XY_RIGHT_HAND #5")
		@Test
		public void rh_5() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpVectorEquals(-45, -78, p);
		}
		
		@DisplayName("XY_RIGHT_HAND #6")
		@Test
		public void rh_6() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpVectorEquals(45, 78, p);
		}
		
		@DisplayName("XY_RIGHT_HAND #7")
		@Test
		public void rh_7() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpVectorEquals(-45, 78, p);
		}
		
		@DisplayName("XY_RIGHT_HAND #8")
		@Test
		public void rh_8() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpVectorEquals(45, -78, p);
		}
	
		@DisplayName("XY_LEFT_HAND #1")
		@Test
		public void lh_1() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpVectorEquals(-45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #2")
		@Test
		public void lh_2() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpVectorEquals(45, 78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #3")
		@Test
		public void lh_3() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpVectorEquals(-45, 78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #4")
		@Test
		public void lh_4() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
			assertFpVectorEquals(45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #5")
		@Test
		public void lh_5() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpVectorEquals(-45, 78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #6")
		@Test
		public void lh_6() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpVectorEquals(45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #7")
		@Test
		public void lh_7() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpVectorEquals(-45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #8")
		@Test
		public void lh_8() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
			assertFpVectorEquals(45, 78, p);
		}

	}

	@DisplayName("fromDefault(Vector2D)")
	@Nested
	public class FromDefaultVector2D {

		private Vector2D p1;
		private Vector2D p2;
		private Vector2D p3;
		private Vector2D p4;
		private Vector2D p;

		@BeforeEach
		public void setUp() {
			p1 = new InnerComputationVector2D(-45, 78);
			p2 = new InnerComputationVector2D(45, -78);
			p3 = new InnerComputationVector2D(-45, -78);
			p4 = new InnerComputationVector2D(45, 78);
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("default #1")
		@Test
		public void default_1() {
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpVectorEquals(-45, 78, p);
		}

		@DisplayName("default #2")
		@Test
		public void default_2() {
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpVectorEquals(45, -78, p);
		}

		@DisplayName("default #3")
		@Test
		public void default_3() {
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpVectorEquals(-45, -78, p);
		}

		@DisplayName("default #4")
		@Test
		public void default_4() {
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpVectorEquals(45, 78, p);
		}

		@DisplayName("default #5")
		@Test
		public void default_5() {
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpVectorEquals(-45, -78, p);
		}

		@DisplayName("default #6")
		@Test
		public void default_6() {
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpVectorEquals(45, 78, p);
		}

		@DisplayName("default #7")
		@Test
		public void default_7() {
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpVectorEquals(-45, 78, p);
		}

		@DisplayName("default #8")
		@Test
		public void default_8() {
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpVectorEquals(45, -78, p);
		}
	
		@DisplayName("XY_RIGHT_HAND #1")
		@Test
		public void rh_1() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpVectorEquals(-45, 78, p);
		}
		
		@DisplayName("XY_RIGHT_HAND #2")
		@Test
		public void rh_2() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpVectorEquals(45, -78, p);
		}
		
		@DisplayName("XY_RIGHT_HAND #3")
		@Test
		public void rh_3() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpVectorEquals(-45, -78, p);
		}
		
		@DisplayName("XY_RIGHT_HAND #4")
		@Test
		public void rh_4() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpVectorEquals(45, 78, p);
		}
		
		@DisplayName("XY_RIGHT_HAND #5")
		@Test
		public void rh_5() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpVectorEquals(-45, -78, p);
		}
		
		@DisplayName("XY_RIGHT_HAND #6")
		@Test
		public void rh_6() {
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpVectorEquals(45, 78, p);
		}
		
		@DisplayName("XY_RIGHT_HAND #7")
		@Test
		public void rh_7() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpVectorEquals(-45, 78, p);
		}
		
		@DisplayName("XY_RIGHT_HAND #8")
		@Test
		public void rh_8() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpVectorEquals(45, -78, p);
		}
	
		@DisplayName("XY_LEFT_HAND #1")
		@Test
		public void lh_1() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpVectorEquals(-45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #2")
		@Test
		public void lh_2() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpVectorEquals(45, 78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #3")
		@Test
		public void lh_3() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpVectorEquals(-45, 78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #4")
		@Test
		public void lh_4() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
			assertFpVectorEquals(45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #5")
		@Test
		public void lh_5() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpVectorEquals(-45, 78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #6")
		@Test
		public void lh_6() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpVectorEquals(45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #7")
		@Test
		public void lh_7() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpVectorEquals(-45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #8")
		@Test
		public void lh_8() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
			assertFpVectorEquals(45, 78, p);
		}

	}

	@DisplayName("toDefault(double)")
	@Nested
	public class ToDefaultDouble {

		@BeforeEach
		public void setUp() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("default #1")
		@Test
		public void default_1() {
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.toDefault(-45));
		}

		@DisplayName("default #2")
		@Test
		public void default_2() {
			assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.toDefault(45));
		}

		@DisplayName("default #3")
		@Test
		public void default_3() {
			assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.toDefault(-45));
		}

		@DisplayName("default #4")
		@Test
		public void default_4() {
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.toDefault(45));
		}
	
		@DisplayName("XY_RIGHT_HAND #1")
		@Test
		public void rh_1() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.toDefault(-45));
		}
		
		@DisplayName("XY_RIGHT_HAND #2")
		@Test
		public void rh_2() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.toDefault(-45));
		}
		
		@DisplayName("XY_RIGHT_HAND #3")
		@Test
		public void rh_3() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.toDefault(45));
		}
	
		@DisplayName("XY_RIGHT_HAND #4")
		@Test
		public void rh_4() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.toDefault(45));
		}
		
		@DisplayName("XY_LEFT_HAND #1")
		@Test
		public void lh_1() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.toDefault(-45));
		}
		
		@DisplayName("XY_LEFT_HAND #2")
		@Test
		public void lh_2() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.toDefault(45));
		}
		
		@DisplayName("XY_LEFT_HAND #3")
		@Test
		public void lh_3() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.toDefault(-45));
		}
		
		@DisplayName("XY_LEFT_HAND #4")
		@Test
		public void lh_4() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.toDefault(45));
		}

	}
	
	@DisplayName("FromDefault(double)")
	@Nested
	public class FromDefaultDouble {

		@BeforeEach
		public void setUp() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("default #1")
		@Test
		public void default_1() {
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(-45));
		}
		
		@DisplayName("default #2")
		@Test
		public void default_2() {
			assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(45));
		}
		
		@DisplayName("default #3")
		@Test
		public void default_3() {
			assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.fromDefault(-45));
		}
		
		@DisplayName("default #4")
		@Test
		public void default_4() {
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.fromDefault(45));
		}
	
		@DisplayName("XY_RIGHT_HAND #1")
		@Test
		public void rh_1() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(-45));
		}
		
		@DisplayName("XY_RIGHT_HAND #2")
		@Test
		public void rh_2() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(45));
		}
		
		@DisplayName("XY_RIGHT_HAND #3")
		@Test
		public void rh_3() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.fromDefault(-45));
		}
		
		@DisplayName("XY_RIGHT_HAND #4")
		@Test
		public void rh_4() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.fromDefault(45));
		}
	
		@DisplayName("XY_LEFT_HAND #1")
		@Test
		public void lh_1() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(-45));
		}
		
		@DisplayName("XY_LEFT_HAND #2")
		@Test
		public void lh_2() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(45));
		}
		
		@DisplayName("XY_LEFT_HAND #3")
		@Test
		public void lh_3() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.fromDefault(-45));
		}
		
		@DisplayName("XY_LEFT_HAND #4")
		@Test
		public void lh_4() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.fromDefault(45));
		}

	}

	@DisplayName("toSystem(Point2D)")
	@Nested
	public class ToSystemPoint2D {

		private Point2D p1;
		private Point2D p2;
		private Point2D p3;
		private Point2D p4;
		private Point2D p;

		@BeforeEach
		public void setUp() {
			p1 = new InnerComputationPoint2D(-45, 78);
			p2 = new InnerComputationPoint2D(45, -78);
			p3 = new InnerComputationPoint2D(-45, -78);
			p4 = new InnerComputationPoint2D(45, 78);
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XY_RIGHT_HAND #1")
		@Test
		public void rh_1() {
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpPointEquals(-45, 78, p);
		}

		@DisplayName("XY_RIGHT_HAND #2")
		@Test
		public void rh_2() {
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpPointEquals(45, -78, p);
		}

		@DisplayName("XY_RIGHT_HAND #3")
		@Test
		public void rh_3() {
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpPointEquals(-45, -78, p);
		}

		@DisplayName("XY_RIGHT_HAND #4")
		@Test
		public void rh_4() {
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpPointEquals(45, 78, p);
		}

		@DisplayName("XY_RIGHT_HAND #5")
		@Test
		public void rh_5() {
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpPointEquals(-45, -78, p);
		}

		@DisplayName("XY_RIGHT_HAND #6")
		@Test
		public void rh_6() {
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpPointEquals(45, 78, p);
		}

		@DisplayName("XY_RIGHT_HAND #7")
		@Test
		public void rh_7() {
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpPointEquals(-45, 78, p);
		}

		@DisplayName("XY_RIGHT_HAND #8")
		@Test
		public void rh_8() {
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpPointEquals(45, -78, p);
		}
	
		@DisplayName("XY_LEFT_HAND #1")
		@Test
		public void lh_1() {
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpPointEquals(-45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #2")
		@Test
		public void lh_2() {
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpPointEquals(45, 78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #3")
		@Test
		public void lh_3() {
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpPointEquals(-45, 78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #4")
		@Test
		public void lh_4() {
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpPointEquals(45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #5")
		@Test
		public void lh_5() {
			p = new InnerComputationPoint2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpPointEquals(-45, 78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #6")
		@Test
		public void lh_6() {
			p = new InnerComputationPoint2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpPointEquals(45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #7")
		@Test
		public void lh_7() {
			p = new InnerComputationPoint2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpPointEquals(-45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #8")
		@Test
		public void lh_8() {
			p = new InnerComputationPoint2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpPointEquals(45, 78, p);
		}

	}

	@DisplayName("toSystem(Vector2D)")
	@Nested
	public class ToSystemVector2D {

		private Vector2D p1;
		private Vector2D p2;
		private Vector2D p3;
		private Vector2D p4;
		private Vector2D p;

		@BeforeEach
		public void setUp() {
			p1 = new InnerComputationVector2D(-45, 78);
			p2 = new InnerComputationVector2D(45, -78);
			p3 = new InnerComputationVector2D(-45, -78);
			p4 = new InnerComputationVector2D(45, 78);
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XY_RIGHT_HAND #1")
		@Test
		public void rh_1() {
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpVectorEquals(-45, 78, p);
		}

		@DisplayName("XY_RIGHT_HAND #2")
		@Test
		public void rh_2() {
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpVectorEquals(45, -78, p);
		}

		@DisplayName("XY_RIGHT_HAND #3")
		@Test
		public void rh_3() {
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpVectorEquals(-45, -78, p);
		}

		@DisplayName("XY_RIGHT_HAND #4")
		@Test
		public void rh_4() {
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpVectorEquals(45, 78, p);
		}

		@DisplayName("XY_RIGHT_HAND #5")
		@Test
		public void rh_5() {
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpVectorEquals(-45, -78, p);
		}

		@DisplayName("XY_RIGHT_HAND #6")
		@Test
		public void rh_6() {
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpVectorEquals(45, 78, p);
		}

		@DisplayName("XY_RIGHT_HAND #7")
		@Test
		public void rh_7() {
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpVectorEquals(-45, 78, p);
		}

		@DisplayName("XY_RIGHT_HAND #8")
		@Test
		public void rh_8() {
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
			assertFpVectorEquals(45, -78, p);
		}
	
		@DisplayName("XY_LEFT_HAND #1")
		@Test
		public void lh_1() {
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpVectorEquals(-45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #2")
		@Test
		public void lh_2() {
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpVectorEquals(45, 78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #3")
		@Test
		public void lh_3() {
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpVectorEquals(-45, 78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #4")
		@Test
		public void lh_4() {
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpVectorEquals(45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #5")
		@Test
		public void lh_5() {
			p = new InnerComputationVector2D(p1);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpVectorEquals(-45, 78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #6")
		@Test
		public void lh_6() {
			p = new InnerComputationVector2D(p2);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpVectorEquals(45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #7")
		@Test
		public void lh_7() {
			p = new InnerComputationVector2D(p3);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpVectorEquals(-45, -78, p);
		}
		
		@DisplayName("XY_LEFT_HAND #8")
		@Test
		public void lh_8() {
			p = new InnerComputationVector2D(p4);
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
			assertFpVectorEquals(45, 78, p);
		}

	}

	@DisplayName("toSystem(double)")
	@Nested
	public class ToSystemDouble {

		@BeforeEach
		public void setUp() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("XY_RIGHT_HAND #1")
		@Test
		public void rh_1() {
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.toSystem(-45, CoordinateSystem2D.XY_RIGHT_HAND));
		}


		@DisplayName("XY_RIGHT_HAND #2")
		@Test
		public void rh_2() {
			assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.toSystem(45, CoordinateSystem2D.XY_RIGHT_HAND));
		}


		@DisplayName("XY_RIGHT_HAND #3")
		@Test
		public void rh_3() {
			assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.toSystem(-45, CoordinateSystem2D.XY_RIGHT_HAND));
		}


		@DisplayName("XY_RIGHT_HAND #4")
		@Test
		public void rh_4() {
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.toSystem(45, CoordinateSystem2D.XY_RIGHT_HAND));
		}
	
		@DisplayName("XY_LEFT_HAND #1")
		@Test
		public void lh_1() {
			assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.toSystem(-45, CoordinateSystem2D.XY_LEFT_HAND));
		}
		
		@DisplayName("XY_LEFT_HAND #2")
		@Test
		public void lh_2() {
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.toSystem(45, CoordinateSystem2D.XY_LEFT_HAND));
		}
		
		@DisplayName("XY_LEFT_HAND #3")
		@Test
		public void lh_3() {
			assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.toSystem(-45, CoordinateSystem2D.XY_LEFT_HAND));
		}
		
		@DisplayName("XY_LEFT_HAND #4")
		@Test
		public void lh_4() {
			assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.toSystem(45, CoordinateSystem2D.XY_LEFT_HAND));
		}

	}

	@DisplayName("toSystem(Transform2D)")
	@Nested
	public class ToSystemTransform2D {

		private Transform2D p1;
		private Transform2D p2;
		private Transform2D p3;
		private Transform2D p4;
		private Transform2D t;
		
		@BeforeEach
		public void setUp() {
			p1 = new Transform2D();
			p1.setIdentity();
			p2 = new Transform2D();
			p2.makeTranslationMatrix(-45, 89);
			p3 = new Transform2D();
			p3.makeRotationMatrix(-MathConstants.DEMI_PI);
			p4 = new Transform2D();
			p4.makeScaleMatrix(.5, 2);
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			t = p1.clone();
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(new Transform2D(
					1, 0, 0,
					0, 1, 0), t);
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			t = p2.clone();
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(new Transform2D(
					1, 0, -45,
					0, 1, 89), t);
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			t = p3.clone();
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(new Transform2D(
					0, 1, 0,
					-1, 0, 0), t);
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			t = p4.clone();
			CoordinateSystem2D.XY_RIGHT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(new Transform2D(
					.5, 0, 0,
					0, 2, 0), t);
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			t = p1.clone();
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(new Transform2D(
					1, 0, 0,
					0, 1, 0), t);
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			t = p2.clone();
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(new Transform2D(
					1, 0, -45,
					0, 1, -89), t);
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			t = p3.clone();
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(new Transform2D(
					0, -1, 0,
					1, 0, 0), t);
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			t = p4.clone();
			CoordinateSystem2D.XY_LEFT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
			assertEpsilonEquals(new Transform2D(
					.5, 0, 0,
					0, 2, 0), t);
		}

	}

	@DisplayName("getBackVector")
	@Nested
	public class GetBackVector {

		@BeforeEach
		public void setUp() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertFpVectorEquals(-1, 0, CoordinateSystem2D.getBackVector());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertInlineParameterUsage(CoordinateSystem2D.class, "getBackVector"); //$NON-NLS-1$
		}

	}

	@DisplayName("SIMULATION_2D")
	@Nested
	public class SIMULATION_2D {

		@BeforeEach
		public void setUp() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("getDefaultSimulationCoordinateSystem")
		@Test
		public void getDefaultSimulationCoordinateSystem() {
			CoordinateSystem2D cs = CoordinateSystemConstants.SIMULATION_2D;
			assertSame(CoordinateSystem2D.XY_RIGHT_HAND,cs);
		}

	}

	@DisplayName("setDefaultCoordinateSystem")
	@Nested
	public class SetDefaultCoordinateSystem {

		@BeforeEach
		public void setUp() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
		}

		@DisplayName("(XY_LEFT_HAND)")
		@Test
		public void setDefaultCoordinateSystem_1() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
			assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem2D.getDefaultCoordinateSystem());
		}
	
		@DisplayName("(XY_RIGHT_HAND)")
		@Test
		public void setDefaultCoordinateSystem_2() {
			CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
			assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem2D.getDefaultCoordinateSystem());
		}
	
		@DisplayName("(null)")
		@Test
		public void setDefaultCoordinateSystem_3() {
			CoordinateSystem2D.setDefaultCoordinateSystem(null);
			assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem2D.getDefaultCoordinateSystem());
		}

	}

}
