/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2012 Stéphane GALLAND
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.arakhne.afc.math;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class MathUtilTest extends AbstractMathTestCase {

	@Test
	public void clampDoubleDoubleDouble() {
		// NaN is lower than all the other floating-point values 
		
		double min = (this.random.nextDouble() - this.random.nextDouble()) * 1000.;
		double max = min + this.random.nextDouble() * 1000.;
		
		assertEpsilonEquals(min, MathUtil.clamp(Double.NEGATIVE_INFINITY, min, max));
		assertEpsilonEquals(max, MathUtil.clamp(Double.POSITIVE_INFINITY, min, max));
		assertEpsilonEquals((min+max)/2.f, MathUtil.clamp((min+max)/2.f, min, max));
		assertEpsilonEquals(max, MathUtil.clamp(Math.abs(max)*2.f, min, max));
	} 

	@Test
	public void clampIntIntInt_minmax() {
		int min = 693;
		int max = 1240;
		assertEquals(924, MathUtil.clamp(924, min, max));
		assertEquals(min, MathUtil.clamp(min, min, max));
		assertEquals(max, MathUtil.clamp(max, min, max));
		assertEquals(max, MathUtil.clamp(max+1, min, max));
		assertEquals(max, MathUtil.clamp(9000, min, max));
		assertEquals(min, MathUtil.clamp(min-1, min, max));
		assertEquals(min, MathUtil.clamp(-124, min, max));
	} 

	@Test
	public void clampIntIntInt_maxmin() {
		int min = 693;
		int max = 1240;
		assertEquals(924, MathUtil.clamp(924, max, min));
		assertEquals(min, MathUtil.clamp(min, max, min));
		assertEquals(max, MathUtil.clamp(max, max, min));
		assertEquals(max, MathUtil.clamp(max+1, max, min));
		assertEquals(max, MathUtil.clamp(9000, max, min));
		assertEquals(min, MathUtil.clamp(min-1, max, min));
		assertEquals(min, MathUtil.clamp(-124, max, min));
	} 

	@Test
	public void clampCyclic() {
		double min = 50;
		double max = 90;
		assertEpsilonEquals(89, MathUtil.clampCyclic(9, min, max));
		assertEpsilonEquals(50, MathUtil.clampCyclic(10, min, max));
		assertEpsilonEquals(60, MathUtil.clampCyclic(20, min, max));
		assertEpsilonEquals(89, MathUtil.clampCyclic(49, min, max));
		assertEpsilonEquals(50, MathUtil.clampCyclic(50, min, max));
		assertEpsilonEquals(70, MathUtil.clampCyclic(70, min, max));
		assertEpsilonEquals(50, MathUtil.clampCyclic(90, min, max));
		assertEpsilonEquals(51, MathUtil.clampCyclic(91, min, max));
		assertEpsilonEquals(70, MathUtil.clampCyclic(110, min, max));
		assertEpsilonEquals(50, MathUtil.clampCyclic(130, min, max));
	}

	@Test(expected = AssertionError.class)
	public void clampCyclic_2() {
		MathUtil.clampCyclic(100, 123, 56);
	}

	@Test(expected = AssertionError.class)
	public void clampCyclic_3() {
		MathUtil.clampCyclic(-100, -56, -123);
	}

	@Test
	public void isEpsilonZeroDouble() {
		assertTrue(MathUtil.isEpsilonZero(0.));
		assertFalse(MathUtil.isEpsilonZero(0.1));
		assertFalse(MathUtil.isEpsilonZero(Double.NaN));
		assertFalse(MathUtil.isEpsilonZero(Double.NEGATIVE_INFINITY));
		assertFalse(MathUtil.isEpsilonZero(Double.POSITIVE_INFINITY));
	}

	@Test
	public void isEpsilonZeroDoubleDouble() {
		assertTrue(MathUtil.isEpsilonZero(0., Math.ulp(0.)));
		assertFalse(MathUtil.isEpsilonZero(0.1, Math.ulp(0.1)));
		assertFalse(MathUtil.isEpsilonZero(Double.NaN, Math.ulp(Double.NaN)));
		assertFalse(MathUtil.isEpsilonZero(Double.NEGATIVE_INFINITY, Math.ulp(0)));
		assertFalse(MathUtil.isEpsilonZero(Double.POSITIVE_INFINITY, Math.ulp(0)));
	}

	@Test
	public void isEpsilonEqualDoubleDouble() {
		assertTrue(MathUtil.isEpsilonEqual(0., 0.));
		assertFalse(MathUtil.isEpsilonEqual(0.1, 0.));
		assertFalse(MathUtil.isEpsilonEqual(Double.NaN, 0.));
		assertFalse(MathUtil.isEpsilonEqual(Double.NEGATIVE_INFINITY, 0.));
		assertFalse(MathUtil.isEpsilonEqual(Double.POSITIVE_INFINITY, 0.));
	}

	@Test
	public void isEpsilonEqualDoubleDoubleDouble() {
		assertTrue(MathUtil.isEpsilonEqual(0., 0., Math.ulp(0.)));
		assertFalse(MathUtil.isEpsilonEqual(0.1, 0., Math.ulp(0.1)));
		assertFalse(MathUtil.isEpsilonEqual(Double.NaN, 0., Math.ulp(Double.NaN)));
		assertFalse(MathUtil.isEpsilonEqual(Double.NEGATIVE_INFINITY, 0., Math.ulp(0.)));
		assertFalse(MathUtil.isEpsilonEqual(Double.POSITIVE_INFINITY, 0., Math.ulp(0.)));
	}

	@Test
	public void maxDoubleArray() {
		assertNaN(MathUtil.max((double[]) null));
		assertNaN(MathUtil.max(new double[0]));
		assertEpsilonEquals(3455.f, MathUtil.max(3., 5., 7., 8., 3455., 3245., 45., 0., -10., 45.));
	}

	@Test
	public void maxFloatArray() {
		assertNaN(MathUtil.max((float[]) null));
		assertNaN(MathUtil.max(new float[0]));
		assertEpsilonEquals(3455.f, MathUtil.max(3.f, 5.f, 7.f, 8.f, 3455.f, 3245.f, 45.f, 0.f, -10.f, 45.f));
	}

	@Test
	public void maxIntArray() {
		assertZero(MathUtil.max((int[]) null));
		assertZero(MathUtil.max(new int[0]));
		assertEquals(3455, MathUtil.max(3, 5, 7, 8, 3455, 3245, 45, 0, -10, 45));
	}

	@Test
	public void maxLongArray() {
		assertZero(MathUtil.max((long[]) null));
		assertZero(MathUtil.max(new long[0]));
		assertEquals(3455l, MathUtil.max(3l, 5l, 7l, 8l, 3455l, 3245l, 45l, 0l, -10l, 45l));
	}

	@Test
	public void maxShortArray() {
		assertZero(MathUtil.max((short[]) null));
		assertZero(MathUtil.max(new short[0]));
		assertEquals(3455, MathUtil.max(new short[] {
				3, 5, 7, 8, 3455, 3245, 45, 0, -10, 45
			}));
	}

	@Test
	public void minDoubleArray() {
		assertNaN(MathUtil.min((double[]) null));
		assertNaN(MathUtil.min(new double[0]));
		assertEpsilonEquals(-10., MathUtil.min(3., 5., 7., 8., 3455., 3245., 45., 0., -10., 45.));
	}

	@Test
	public void minFloatArray() {
		assertNaN(MathUtil.min((float[]) null));
		assertNaN(MathUtil.min(new float[0]));
		assertEpsilonEquals(-10.f, MathUtil.min(3.f, 5.f, 7.f, 8.f, 3455.f, 3245.f, 45.f, 0.f, -10.f, 45.f));
	}

	@Test
	public void minIntArray() {
		assertZero(MathUtil.min((int[]) null));
		assertZero(MathUtil.min(new int[0]));
		assertEquals(-10, MathUtil.min(3, 5, 7, 8, 3455, 3245, 45, 0, -10, 45));
	}

	@Test
	public void minLongArray() {
		assertZero(MathUtil.min((long[]) null));
		assertZero(MathUtil.min(new long[0]));
		assertEquals(-10l, MathUtil.min(3l, 5l, 7l, 8l, 3455l, 3245l, 45l, 0l, -10l, 45l));
	}

	@Test
	public void minShortArray() {
		assertZero(MathUtil.min((short[]) null));
		assertZero(MathUtil.min(new short[0]));
		assertEquals(-10, MathUtil.min(new short[] {
				3, 5, 7, 8, 3455, 3245, 45, 0, -10, 45
			}));
	}

	@Test
	public void clampToNearestBounds_toLowerBound() {
		assertEpsilonEquals(2., MathUtil.clampToNearestBounds(0., 2., 10.));
		assertEpsilonEquals(2., MathUtil.clampToNearestBounds(1., 2., 10.));
		assertEpsilonEquals(2., MathUtil.clampToNearestBounds(2., 2., 10.));
		assertEpsilonEquals(2., MathUtil.clampToNearestBounds(3., 2., 10.));
		assertEpsilonEquals(2., MathUtil.clampToNearestBounds(4., 2., 10.));
		assertEpsilonEquals(2., MathUtil.clampToNearestBounds(5., 2., 10.));
		assertEpsilonEquals(2., MathUtil.clampToNearestBounds(6., 2., 10.));
	}

	@Test
	public void clampToNearestBounds_toUpperBound() {
		assertEpsilonEquals(10., MathUtil.clampToNearestBounds(7., 2., 10.));
		assertEpsilonEquals(10., MathUtil.clampToNearestBounds(8., 2., 10.));
		assertEpsilonEquals(10., MathUtil.clampToNearestBounds(9., 2., 10.));
		assertEpsilonEquals(10., MathUtil.clampToNearestBounds(10., 2., 10.));
		assertEpsilonEquals(10., MathUtil.clampToNearestBounds(11., 2., 10.));
		assertEpsilonEquals(10., MathUtil.clampToNearestBounds(13., 2., 10.));
		assertEpsilonEquals(10., MathUtil.clampToNearestBounds(14., 2., 10.));
		assertEpsilonEquals(10., MathUtil.clampToNearestBounds(15., 2., 10.));
	}

	@Test
	public void getCohenSutherlandCodeIntIntIntIntIntInt() {
		assertEquals(5, MathUtil.getCohenSutherlandCode(0, 0, 5, 5, 15, 15));
		assertEquals(4, MathUtil.getCohenSutherlandCode(10, 0, 5, 5, 15, 15));
		assertEquals(6, MathUtil.getCohenSutherlandCode(20, 0, 5, 5, 15, 15));

		assertEquals(1, MathUtil.getCohenSutherlandCode(0, 10, 5, 5, 15, 15));
		assertEquals(0, MathUtil.getCohenSutherlandCode(10, 10, 5, 5, 15, 15));
		assertEquals(2, MathUtil.getCohenSutherlandCode(20, 10, 5, 5, 15, 15));

		assertEquals(9, MathUtil.getCohenSutherlandCode(0, 20, 5, 5, 15, 15));
		assertEquals(8, MathUtil.getCohenSutherlandCode(10, 20, 5, 5, 15, 15));
		assertEquals(10, MathUtil.getCohenSutherlandCode(20, 20, 5, 5, 15, 15));
	}

	@Test
	public void getCohenSutherlandCodeDoubleDoubleDoubleDoubleDoubleDouble() {
		assertEquals(5, MathUtil.getCohenSutherlandCode(0., 0., 5., 5., 15., 15.));
		assertEquals(4, MathUtil.getCohenSutherlandCode(10., 0., 5., 5., 15., 15.));
		assertEquals(6, MathUtil.getCohenSutherlandCode(20., 0., 5., 5., 15., 15.));

		assertEquals(1, MathUtil.getCohenSutherlandCode(0., 10., 5., 5., 15., 15.));
		assertEquals(0, MathUtil.getCohenSutherlandCode(10., 10., 5., 5., 15., 15.));
		assertEquals(2, MathUtil.getCohenSutherlandCode(20., 10., 5., 5., 15., 15.));

		assertEquals(9, MathUtil.getCohenSutherlandCode(0., 20., 5., 5., 15., 15.));
		assertEquals(8, MathUtil.getCohenSutherlandCode(10., 20., 5., 5., 15., 15.));
		assertEquals(10, MathUtil.getCohenSutherlandCode(20., 20., 5., 5., 15., 15.));
	}

	@Test
	public void compareEpsilonDoubleDouble() {
		assertEquals(0, MathUtil.compareEpsilon(50., 50.));
		assertEquals(-1, MathUtil.compareEpsilon(0., 50.));
		assertEquals(1, MathUtil.compareEpsilon(50., 0.));
		//
		assertEquals(0, MathUtil.compareEpsilon(50. + Math.ulp(50.) / 2., 50.));
		assertEquals(0, MathUtil.compareEpsilon(50. - Math.ulp(50.) / 2., 50.));
	}

	@Test
	public void compareEpsilonDoubleDoubleDouble() {
		assertEquals(0, MathUtil.compareEpsilon(50., 50., Math.ulp(50.)));
		assertEquals(-1, MathUtil.compareEpsilon(0., 50., Math.ulp(0.)));
		assertEquals(1, MathUtil.compareEpsilon(50., 0., Math.ulp(50.)));
		
		assertEquals(0, MathUtil.compareEpsilon(50. + Math.ulp(50.) / 2., 50., Math.ulp(50.)));
		assertEquals(0, MathUtil.compareEpsilon(50. - Math.ulp(50.) / 2., 50., Math.ulp(50.)));
	}

	@Test
	public void getMinMaxDoubleDoubleDoublePair_0() {
		DoubleRange range = MathUtil.getMinMax(3., 3455., -10.);
		assertNotNull(range);
		assertEpsilonEquals(-10., range.getMin());
		assertEpsilonEquals(3455., range.getMax());
	}

	@Test
	public void getMinMaxDoubleDoubleDoublePair_1() {
		DoubleRange range = MathUtil.getMinMax(Double.NEGATIVE_INFINITY, 3455., -10.);
		assertNotNull(range);
		assertTrue(Double.isInfinite(range.getMin()));
		assertEpsilonEquals(3455., range.getMax());
	}

	@Test
	public void getMinMaxDoubleDoubleDoublePair_2() {
		DoubleRange range = MathUtil.getMinMax(Double.POSITIVE_INFINITY, 3455., -10.);
		assertNotNull(range);
		assertEpsilonEquals(-10., range.getMin());
		assertTrue(Double.isInfinite(range.getMax()));
	}

	@Test
	public void getMinMaxDoubleDoubleDoublePair_3() {
		DoubleRange range = MathUtil.getMinMax(Double.NaN, 3455., -10.);
		assertNotNull(range);
		assertEpsilonEquals(-10., range.getMin());
		assertEpsilonEquals(3455, range.getMax());
	}

	@Test
	public void getMinMaxDoubleDoubleDoublePair_4() {
		DoubleRange range = MathUtil.getMinMax(3455., Double.NaN, -10.);
		assertNotNull(range);
		assertEpsilonEquals(-10., range.getMin());
		assertEpsilonEquals(3455, range.getMax());
	}

	@Test
	public void getMinMaxDoubleDoubleDoublePair_5() {
		DoubleRange range = MathUtil.getMinMax(3455., -10., Double.NaN);
		assertNotNull(range);
		assertEpsilonEquals(-10., range.getMin());
		assertEpsilonEquals(3455, range.getMax());
	}

	@Test
	public void getMinMaxDoubleDoubleDoublePair_6() {
		DoubleRange range = MathUtil.getMinMax(Double.NaN, -10., 3455.);
		assertNotNull(range);
		assertEpsilonEquals(-10., range.getMin());
		assertEpsilonEquals(3455, range.getMax());
	}

	@Test
	public void getMinMaxDoubleDoubleDoublePair_7() {
		DoubleRange range = MathUtil.getMinMax(-10., Double.NaN, 3455.);
		assertNotNull(range);
		assertEpsilonEquals(-10., range.getMin());
		assertEpsilonEquals(3455, range.getMax());
	}

	@Test
	public void getMinMaxDoubleDoubleDoublePair_8() {
		DoubleRange range = MathUtil.getMinMax(-10., 3455., Double.NaN);
		assertNotNull(range);
		assertEpsilonEquals(-10., range.getMin());
		assertEpsilonEquals(3455, range.getMax());
	}

	@Test
	public void getMinMaxDoubleDoubleDoublePair_9() {
		DoubleRange range = MathUtil.getMinMax(-10., Double.NaN, Double.NaN);
		assertNotNull(range);
		assertEpsilonEquals(-10., range.getMin());
		assertEpsilonEquals(-10, range.getMax());
	}

	@Test
	public void getMinMaxDoubleDoubleDoublePair_10() {
		DoubleRange range = MathUtil.getMinMax(Double.NaN, -10., Double.NaN);
		assertNotNull(range);
		assertEpsilonEquals(-10., range.getMin());
		assertEpsilonEquals(-10, range.getMax());
	}

	@Test
	public void getMinMaxDoubleDoubleDoublePair_11() {
		DoubleRange range = MathUtil.getMinMax(Double.NaN, Double.NaN, -10.);
		assertNotNull(range);
		assertEpsilonEquals(-10., range.getMin());
		assertEpsilonEquals(-10, range.getMax());
	}

	@Test
	public void getMinMaxDoubleDoubleDoublePair_12() {
		DoubleRange range = MathUtil.getMinMax(Double.NaN, Double.NaN, Double.NaN);
		assertNull(range);
	}

}