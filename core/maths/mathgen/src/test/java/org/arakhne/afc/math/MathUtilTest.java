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

package org.arakhne.afc.math;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.testtools.AbstractTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class MathUtilTest extends AbstractTestCase {

	@DisplayName("sign")
	@Nested
	public class Sign {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(-1, MathUtil.sign(-145.25));
		}

		@DisplayName("#1")
		@Test
		public void test_2() {
			assertEquals(-1, MathUtil.sign(-145.25));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(-1, MathUtil.sign(-0.25));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEquals(0, MathUtil.sign(-0.));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEquals(0, MathUtil.sign(0.));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEquals(0, MathUtil.sign(+0.));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertEquals(1, MathUtil.sign(145.25));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertEquals(1, MathUtil.sign(0.25));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertInlineParameterUsage(MathUtil.class, "sign", double.class); //$NON-NLS-1$
		}
	}
	
	@DisplayName("signNoZero")
	@Nested
	public class SignNoZero {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(-1, MathUtil.signNoZero(-145.25));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals(-1, MathUtil.signNoZero(-0.25));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(1, MathUtil.signNoZero(-0.));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEquals(1, MathUtil.signNoZero(0.));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEquals(1, MathUtil.signNoZero(+0.));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEquals(1, MathUtil.signNoZero(145.25));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertEquals(1, MathUtil.signNoZero(0.25));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertInlineParameterUsage(MathUtil.class, "sign", double.class); //$NON-NLS-1$
		}

	}

	@Test
	public void clampDoubleDoubleDouble() {
		// NaN is lower than all the other floating-point values 
		
		double min = (getRandom().nextDouble() - getRandom().nextDouble()) * 1000.;
		double max = min + getRandom().nextDouble() * 1000.;
		
		assertEpsilonEquals(min, MathUtil.clamp(Double.NEGATIVE_INFINITY, min, max));
		assertEpsilonEquals(max, MathUtil.clamp(Double.POSITIVE_INFINITY, min, max));
		assertEpsilonEquals((min+max)/2.f, MathUtil.clamp((min+max)/2.f, min, max));
		assertEpsilonEquals(max, MathUtil.clamp(Math.abs(max)*2.f, min, max));
	} 

	@DisplayName("clamp(int,int,int)")
	@Nested
	public class ClampIntIntInt {
		
		private int min = 693;
		private int max = 1240;

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(924, MathUtil.clamp(924, min, max));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals(min, MathUtil.clamp(min, min, max));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(max, MathUtil.clamp(max, min, max));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEquals(max, MathUtil.clamp(max+1, min, max));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEquals(max, MathUtil.clamp(9000, min, max));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEquals(min, MathUtil.clamp(min-1, min, max));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertEquals(min, MathUtil.clamp(-124, min, max));
		}
	}

	@DisplayName("clampCyclic(int,int,int)")
	@Nested
	public class clampCyclicntIntInt {
		
		private double min = 50;
		private double max = 90;

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(89, MathUtil.clampCyclic(9, min, max));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEpsilonEquals(50, MathUtil.clampCyclic(10, min, max));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEpsilonEquals(60, MathUtil.clampCyclic(20, min, max));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEpsilonEquals(89, MathUtil.clampCyclic(49, min, max));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEpsilonEquals(50, MathUtil.clampCyclic(50, min, max));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEpsilonEquals(70, MathUtil.clampCyclic(70, min, max));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertEpsilonEquals(50, MathUtil.clampCyclic(90, min, max));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertEpsilonEquals(51, MathUtil.clampCyclic(91, min, max));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertEpsilonEquals(70, MathUtil.clampCyclic(110, min, max));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertEpsilonEquals(50, MathUtil.clampCyclic(130, min, max));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertThrows(AssertionError.class, () -> MathUtil.clampCyclic(100, 123, 56));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertThrows(AssertionError.class, () -> MathUtil.clampCyclic(-100, -56, -123));
		}

	}

	@DisplayName("isEpsilonZero(double)")
	@Nested
	public class IsEpsilonZeroDouble {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(MathUtil.isEpsilonZero(0.));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertFalse(MathUtil.isEpsilonZero(0.1));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertFalse(MathUtil.isEpsilonZero(Double.NaN));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertFalse(MathUtil.isEpsilonZero(Double.NEGATIVE_INFINITY));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertFalse(MathUtil.isEpsilonZero(Double.POSITIVE_INFINITY));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertInlineParameterUsage(MathUtil.class, "isEpsilonZero", double.class); //$NON-NLS-1$
		}

	}

	@DisplayName("isEpsilonZero(double,double)")
	@Nested
	public class IsEpsilonZeroDoubleDouble {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(MathUtil.isEpsilonZero(0., Math.ulp(0.)));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertFalse(MathUtil.isEpsilonZero(0.1, Math.ulp(0.1)));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertFalse(MathUtil.isEpsilonZero(Double.NaN, Math.ulp(Double.NaN)));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertFalse(MathUtil.isEpsilonZero(Double.NEGATIVE_INFINITY, Math.ulp(0)));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertFalse(MathUtil.isEpsilonZero(Double.POSITIVE_INFINITY, Math.ulp(0)));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertInlineParameterUsage(MathUtil.class, "isEpsilonZero", double.class, double.class); //$NON-NLS-1$
		}

	}

	@DisplayName("isEpsilonEqual(double,double)")
	@Nested
	public class IsEpsilonEqualDoubleDouble {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(MathUtil.isEpsilonEqual(0., 0.));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertFalse(MathUtil.isEpsilonEqual(0.1, 0.));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertFalse(MathUtil.isEpsilonEqual(Double.NaN, 0.));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertFalse(MathUtil.isEpsilonEqual(Double.NEGATIVE_INFINITY, 0.));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertFalse(MathUtil.isEpsilonEqual(Double.POSITIVE_INFINITY, 0.));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertInlineParameterUsage(MathUtil.class, "isEpsilonEqual", double.class, double.class); //$NON-NLS-1$
		}

	}

	@DisplayName("isEpsilonEqual(double,double,double)")
	@Nested
	public class IsEpsilonEqualDoubleDoubleDouble {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(MathUtil.isEpsilonEqual(0., 0., Math.ulp(0.)));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertFalse(MathUtil.isEpsilonEqual(0.1, 0., Math.ulp(0.1)));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertFalse(MathUtil.isEpsilonEqual(Double.NaN, 0., Math.ulp(Double.NaN)));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertFalse(MathUtil.isEpsilonEqual(Double.NEGATIVE_INFINITY, 0., Math.ulp(0.)));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertFalse(MathUtil.isEpsilonEqual(Double.POSITIVE_INFINITY, 0., Math.ulp(0.)));
		}

	}

	@DisplayName("max(double[])")
	@Nested
	public class MaxDoubleArray {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertNaN(MathUtil.max((double[]) null));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertNaN(MathUtil.max(new double[0]));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEpsilonEquals(3455.f, MathUtil.max(3., 5., 7., 8., 3455., 3245., 45., 0., -10., 45.));
		}

	}

	@DisplayName("max(float[])")
	@Nested
	public class MaxFloatArray {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertNaN(MathUtil.max((float[]) null));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertNaN(MathUtil.max(new float[0]));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEpsilonEquals(3455.f, MathUtil.max(3.f, 5.f, 7.f, 8.f, 3455.f, 3245.f, 45.f, 0.f, -10.f, 45.f));
		}
	}

	@DisplayName("max(int[])")
	@Nested
	public class MaxIntArray {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertZero(MathUtil.max((int[]) null));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertZero(MathUtil.max(new int[0]));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(3455, MathUtil.max(3, 5, 7, 8, 3455, 3245, 45, 0, -10, 45));
		}

	}

	@DisplayName("max(long[])")
	@Nested
	public class MaxLongArray {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertZero(MathUtil.max((long[]) null));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertZero(MathUtil.max(new long[0]));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(3455l, MathUtil.max(3l, 5l, 7l, 8l, 3455l, 3245l, 45l, 0l, -10l, 45l));
		}

	}

	@DisplayName("max(short[])")
	@Nested
	public class MaxShortArray {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertZero(MathUtil.max((short[]) null));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertZero(MathUtil.max(new short[0]));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(3455, MathUtil.max(new short[] {
					3, 5, 7, 8, 3455, 3245, 45, 0, -10, 45
				}));
		}
	}

	@DisplayName("min(double[])")
	@Nested
	public class MinDoubleArray {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertNaN(MathUtil.min((double[]) null));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertNaN(MathUtil.min(new double[0]));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEpsilonEquals(-10., MathUtil.min(3., 5., 7., 8., 3455., 3245., 45., 0., -10., 45.));
		}
	}

	@DisplayName("min(float[])")
	@Nested
	public class MinFloatArray {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertNaN(MathUtil.min((float[]) null));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertNaN(MathUtil.min(new float[0]));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEpsilonEquals(-10.f, MathUtil.min(3.f, 5.f, 7.f, 8.f, 3455.f, 3245.f, 45.f, 0.f, -10.f, 45.f));
		}
	}

	@DisplayName("min(int[])")
	@Nested
	public class MinIntArray {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertZero(MathUtil.min((int[]) null));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertZero(MathUtil.min(new int[0]));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(-10, MathUtil.min(3, 5, 7, 8, 3455, 3245, 45, 0, -10, 45));

		}

	}

	@DisplayName("min(long[])")
	@Nested
	public class MinLongArray {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertZero(MathUtil.min((long[]) null));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertZero(MathUtil.min(new long[0]));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(-10l, MathUtil.min(3l, 5l, 7l, 8l, 3455l, 3245l, 45l, 0l, -10l, 45l));
		}
	}

	@DisplayName("min(short[])")
	@Nested
	public class MinShortArray {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertZero(MathUtil.min((short[]) null));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertZero(MathUtil.min(new short[0]));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(-10, MathUtil.min(new short[] {
					3, 5, 7, 8, 3455, 3245, 45, 0, -10, 45
				}));
		}
	}

	@DisplayName("clampToNearestBounds")
	@Nested
	public class ClampToNearestBounds {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(2., MathUtil.clampToNearestBounds(0., 2., 10.));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEpsilonEquals(2., MathUtil.clampToNearestBounds(1., 2., 10.));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEpsilonEquals(2., MathUtil.clampToNearestBounds(2., 2., 10.));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEpsilonEquals(2., MathUtil.clampToNearestBounds(3., 2., 10.));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEpsilonEquals(2., MathUtil.clampToNearestBounds(4., 2., 10.));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEpsilonEquals(2., MathUtil.clampToNearestBounds(5., 2., 10.));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertEpsilonEquals(2., MathUtil.clampToNearestBounds(6., 2., 10.));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertEpsilonEquals(10., MathUtil.clampToNearestBounds(7., 2., 10.));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertEpsilonEquals(10., MathUtil.clampToNearestBounds(8., 2., 10.));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertEpsilonEquals(10., MathUtil.clampToNearestBounds(9., 2., 10.));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertEpsilonEquals(10., MathUtil.clampToNearestBounds(10., 2., 10.));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertEpsilonEquals(10., MathUtil.clampToNearestBounds(11., 2., 10.));
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			assertEpsilonEquals(10., MathUtil.clampToNearestBounds(13., 2., 10.));
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			assertEpsilonEquals(10., MathUtil.clampToNearestBounds(14., 2., 10.));
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			assertEpsilonEquals(10., MathUtil.clampToNearestBounds(15., 2., 10.));
		}

	}

	@DisplayName("getCohenSutherlandCode(int,int,int,int,int,int)")
	@Nested
	public class GetCohenSutherlandCodeInt {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(5, MathUtil.getCohenSutherlandCode(0, 0, 5, 5, 15, 15));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals(4, MathUtil.getCohenSutherlandCode(10, 0, 5, 5, 15, 15));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(6, MathUtil.getCohenSutherlandCode(20, 0, 5, 5, 15, 15));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEquals(1, MathUtil.getCohenSutherlandCode(0, 10, 5, 5, 15, 15));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEquals(0, MathUtil.getCohenSutherlandCode(10, 10, 5, 5, 15, 15));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEquals(2, MathUtil.getCohenSutherlandCode(20, 10, 5, 5, 15, 15));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertEquals(9, MathUtil.getCohenSutherlandCode(0, 20, 5, 5, 15, 15));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertEquals(8, MathUtil.getCohenSutherlandCode(10, 20, 5, 5, 15, 15));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertEquals(10, MathUtil.getCohenSutherlandCode(20, 20, 5, 5, 15, 15));
		}

	}

	@DisplayName("getCohenSutherlandCode(double,double,double,double,double,double)")
	@Nested
	public class GetCohenSutherlandCodeDouble {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(5, MathUtil.getCohenSutherlandCode(0., 0., 5., 5., 15., 15.));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals(4, MathUtil.getCohenSutherlandCode(10., 0., 5., 5., 15., 15.));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(6, MathUtil.getCohenSutherlandCode(20., 0., 5., 5., 15., 15.));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEquals(1, MathUtil.getCohenSutherlandCode(0., 10., 5., 5., 15., 15.));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEquals(0, MathUtil.getCohenSutherlandCode(10., 10., 5., 5., 15., 15.));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEquals(2, MathUtil.getCohenSutherlandCode(20., 10., 5., 5., 15., 15.));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertEquals(9, MathUtil.getCohenSutherlandCode(0., 20., 5., 5., 15., 15.));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertEquals(8, MathUtil.getCohenSutherlandCode(10., 20., 5., 5., 15., 15.));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertEquals(10, MathUtil.getCohenSutherlandCode(20., 20., 5., 5., 15., 15.));
		}

	}

	@DisplayName("compareEpsilon(double,double)")
	@Nested
	public class CompareEpsilonDoubleDouble {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(0, MathUtil.compareEpsilon(50., 50.));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals(-1, MathUtil.compareEpsilon(0., 50.));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(1, MathUtil.compareEpsilon(50., 0.));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEquals(0, MathUtil.compareEpsilon(50. + Math.ulp(50.) / 2., 50.));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEquals(0, MathUtil.compareEpsilon(50. - Math.ulp(50.) / 2., 50.));
		}

	}

	@DisplayName("compareEpsilon(double,double,double)")
	@Nested
	public class CompareEpsilonDoubleDoubleDouble {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(0, MathUtil.compareEpsilon(50., 50., Math.ulp(50.)));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals(-1, MathUtil.compareEpsilon(0., 50., Math.ulp(0.)));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(1, MathUtil.compareEpsilon(50., 0., Math.ulp(50.)));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEquals(0, MathUtil.compareEpsilon(50. + Math.ulp(50.) / 2., 50., Math.ulp(50.)));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEquals(0, MathUtil.compareEpsilon(50. - Math.ulp(50.) / 2., 50., Math.ulp(50.)));
		}

	}

	@DisplayName("getMinMax(double,double,double)")
	@Nested
	public class GetMinMaxDoubleDoubleDouble {

		@DisplayName("#1")
		@Test
		public void test_1() {
			DoubleRange range = MathUtil.getMinMax(3., 3455., -10.);
			assertNotNull(range);
			assertEpsilonEquals(-10., range.getMin());
			assertEpsilonEquals(3455., range.getMax());
		}
	
		@DisplayName("#2")
		@Test
		public void test_2() {
			DoubleRange range = MathUtil.getMinMax(Double.NEGATIVE_INFINITY, 3455., -10.);
			assertNotNull(range);
			assertTrue(Double.isInfinite(range.getMin()));
			assertEpsilonEquals(3455., range.getMax());
		}
		
		@DisplayName("#3")
		@Test
		public void test_3() {
			DoubleRange range = MathUtil.getMinMax(Double.POSITIVE_INFINITY, 3455., -10.);
			assertNotNull(range);
			assertEpsilonEquals(-10., range.getMin());
			assertTrue(Double.isInfinite(range.getMax()));
		}
		
		@DisplayName("#4")
		@Test
		public void test_4() {
			DoubleRange range = MathUtil.getMinMax(Double.NaN, 3455., -10.);
			assertNotNull(range);
			assertEpsilonEquals(-10., range.getMin());
			assertEpsilonEquals(3455, range.getMax());
		}
		
		@DisplayName("#5")
		@Test
		public void test_5() {
			DoubleRange range = MathUtil.getMinMax(3455., Double.NaN, -10.);
			assertNotNull(range);
			assertEpsilonEquals(-10., range.getMin());
			assertEpsilonEquals(3455, range.getMax());
		}
		
		@DisplayName("#6")
		@Test
		public void test_6() {
			DoubleRange range = MathUtil.getMinMax(3455., -10., Double.NaN);
			assertNotNull(range);
			assertEpsilonEquals(-10., range.getMin());
			assertEpsilonEquals(3455, range.getMax());
		}
		
		@DisplayName("#7")
		@Test
		public void test_7() {
			DoubleRange range = MathUtil.getMinMax(Double.NaN, -10., 3455.);
			assertNotNull(range);
			assertEpsilonEquals(-10., range.getMin());
			assertEpsilonEquals(3455, range.getMax());
		}
		
		@DisplayName("#8")
		@Test
		public void test_8() {
			DoubleRange range = MathUtil.getMinMax(-10., Double.NaN, 3455.);
			assertNotNull(range);
			assertEpsilonEquals(-10., range.getMin());
			assertEpsilonEquals(3455, range.getMax());
		}
		
		@DisplayName("#9")
		@Test
		public void test_9() {
			DoubleRange range = MathUtil.getMinMax(-10., 3455., Double.NaN);
			assertNotNull(range);
			assertEpsilonEquals(-10., range.getMin());
			assertEpsilonEquals(3455, range.getMax());
		}
		
		@DisplayName("#10")
		@Test
		public void test_10() {
			DoubleRange range = MathUtil.getMinMax(-10., Double.NaN, Double.NaN);
			assertNotNull(range);
			assertEpsilonEquals(-10., range.getMin());
			assertEpsilonEquals(-10, range.getMax());
		}
		
		@DisplayName("#11")
		@Test
		public void test_11() {
			DoubleRange range = MathUtil.getMinMax(Double.NaN, -10., Double.NaN);
			assertNotNull(range);
			assertEpsilonEquals(-10., range.getMin());
			assertEpsilonEquals(-10, range.getMax());
		}
		
		@DisplayName("#12")
		@Test
		public void test_12() {
			DoubleRange range = MathUtil.getMinMax(Double.NaN, Double.NaN, -10.);
			assertNotNull(range);
			assertEpsilonEquals(-10., range.getMin());
			assertEpsilonEquals(-10, range.getMax());
		}
		
		@DisplayName("#13")
		@Test
		public void test_13() {
			DoubleRange range = MathUtil.getMinMax(Double.NaN, Double.NaN, Double.NaN);
			assertNull(range);
		}

	}

	@DisplayName("cot")
	@Test
	public void cot() {
		assertInlineParameterUsage(MathUtil.class, "cot", double.class); //$NON-NLS-1$
	}

	@DisplayName("crd")
	@Test
	public void crd() {
		assertInlineParameterUsage(MathUtil.class, "crd", double.class); //$NON-NLS-1$
	}

	@DisplayName("csc")
	@Test
	public void csc() {
		assertInlineParameterUsage(MathUtil.class, "csc", double.class); //$NON-NLS-1$
	}

	@DisplayName("exsec")
	@Test
	public void exsec() {
		assertInlineParameterUsage(MathUtil.class, "exsec", double.class); //$NON-NLS-1$
	}

	@DisplayName("sec")
	@Test
	public void sec() {
		assertInlineParameterUsage(MathUtil.class, "sec", double.class); //$NON-NLS-1$
	}

	@DisplayName("versin")
	@Test
	public void versin() {
		assertInlineParameterUsage(MathUtil.class, "versin", double.class); //$NON-NLS-1$
	}

}
