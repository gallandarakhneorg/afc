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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.util.Pair;
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
	public void clampCyclic_0() {
		double min = 56;
		double max = 123;
		double range = max - min;
		
		for(double a = min; a <= max; a += range / 50.) {
			assertEpsilonEquals("value = " + a, a, MathUtil.clampCyclic(a, min, max));
			double ap = a + range;
			assertEpsilonEquals("value = " + ap, a, MathUtil.clampCyclic(ap, min, max));
			double am = a - range;
			assertEpsilonEquals("value = " + am, a, MathUtil.clampCyclic(am, min, max));
		}
	}

	@Test
	public void clampCyclic_1() {
		double min = -123;
		double max = 56;
		double range = max - min;
		
		for(double a = min; a <= max; a += range / 50.) {
			assertEpsilonEquals("value = " + a, a, MathUtil.clampCyclic(a, min, max));
			double ap = a + range;
			assertEpsilonEquals("value = " + ap, a, MathUtil.clampCyclic(ap, min, max));
			double am = a - range;
			assertEpsilonEquals("value = " + am, a, MathUtil.clampCyclic(am, min, max));
		}
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
		assertTrue(MathUtil.isEpsilonZero(0., MathConstants.EPSILON));
		assertFalse(MathUtil.isEpsilonZero(0.1, MathConstants.EPSILON));
		assertFalse(MathUtil.isEpsilonZero(Double.NaN, MathConstants.EPSILON));
		assertFalse(MathUtil.isEpsilonZero(Double.NEGATIVE_INFINITY, MathConstants.EPSILON));
		assertFalse(MathUtil.isEpsilonZero(Double.POSITIVE_INFINITY, MathConstants.EPSILON));
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
		assertTrue(MathUtil.isEpsilonEqual(0., 0., MathConstants.EPSILON));
		assertFalse(MathUtil.isEpsilonEqual(0.1, 0., MathConstants.EPSILON));
		assertFalse(MathUtil.isEpsilonEqual(Double.NaN, 0., MathConstants.EPSILON));
		assertFalse(MathUtil.isEpsilonEqual(Double.NEGATIVE_INFINITY, 0., MathConstants.EPSILON));
		assertFalse(MathUtil.isEpsilonEqual(Double.POSITIVE_INFINITY, 0., MathConstants.EPSILON));
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
		assertNaN(MathUtil.max((int[]) null));
		assertNaN(MathUtil.max(new int[0]));
		assertEquals(3455, MathUtil.max(3, 5, 7, 8, 3455, 3245, 45, 0, -10, 45));
	}

	@Test
	public void maxLongArray() {
		assertNaN(MathUtil.max((long[]) null));
		assertNaN(MathUtil.max(new long[0]));
		assertEquals(3455l, MathUtil.max(3l, 5l, 7l, 8l, 3455l, 3245l, 45l, 0l, -10l, 45l));
	}

	@Test
	public void maxShortArray() {
		assertNaN(MathUtil.max((short[]) null));
		assertNaN(MathUtil.max(new short[0]));
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
		assertNaN(MathUtil.min((int[]) null));
		assertNaN(MathUtil.min(new int[0]));
		assertEquals(-10, MathUtil.min(3, 5, 7, 8, 3455, 3245, 45, 0, -10, 45));
	}

	@Test
	public void minLongArray() {
		assertNaN(MathUtil.min((long[]) null));
		assertNaN(MathUtil.min(new long[0]));
		assertEquals(-10l, MathUtil.min(3l, 5l, 7l, 8l, 3455l, 3245l, 45l, 0l, -10l, 45l));
	}

	@Test
	public void minShortArray() {
		assertNaN(MathUtil.min((short[]) null));
		assertNaN(MathUtil.min(new short[0]));
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
		assertEquals(51, MathUtil.compareEpsilon(0., 50.));
		assertEquals(-51, MathUtil.compareEpsilon(50., 0.));
		//
		assertEquals(0, MathUtil.compareEpsilon(50. + MathConstants.EPSILON / 2., 50.));
		assertEquals(0, MathUtil.compareEpsilon(50. - MathConstants.EPSILON / 2., 50.));
	}

	@Test
	public void compareEpsilonDoubleDoubleDouble() {
		assertEquals(0, MathUtil.compareEpsilon(50., 50., MathConstants.EPSILON));
		assertEquals(51, MathUtil.compareEpsilon(0., 50., MathConstants.EPSILON));
		assertEquals(-51, MathUtil.compareEpsilon(50., 0., MathConstants.EPSILON));
		//
		assertEquals(0, MathUtil.compareEpsilon(50. + MathConstants.EPSILON / 2., 50., MathConstants.EPSILON));
		assertEquals(0, MathUtil.compareEpsilon(50. - MathConstants.EPSILON / 2., 50., MathConstants.EPSILON));
	}

	@Test
	public void getMinMaxDoubleDoubleDoublePair() {
		throw new UnsupportedOperationException();
	}

}