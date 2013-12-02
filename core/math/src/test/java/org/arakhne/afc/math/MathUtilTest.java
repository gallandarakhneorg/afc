/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math;

import org.arakhne.afc.math.geometry2d.continuous.Vector2f;
import org.arakhne.afc.math.geometry3d.continuous.Vector3f;


/**
 * Test for {@link MathUtil}
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MathUtilTest extends AbstractMathTestCase {

	@Override
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 */

	public void testMaxFloatArray() {
		assertNaN(MathUtil.max((float[]) null));
		assertNaN(MathUtil.max(new float[0]));
		assertEquals(3455.f, MathUtil.max(3.f, 5.f, 7.f, 8.f, 3455.f, 3245.f, 45.f, 0.f, -10.f, 45.f));
	}

	/**
	 */
	public void testMinFloatArray() {
		assertNaN(MathUtil.min((float[]) null));
		assertNaN(MathUtil.min(new float[0]));
		assertEquals(-10.f, MathUtil.min(3.f, 5.f, 7.f, 8.f, 3455.f, 3245.f, 45.f, 0.f, -10.f, 45.f));
	}

	/** 
	 */
	public void testDotProductFloatFloatFloatFloatFloatFloat() {
		Vector3f v1 = new Vector3f(1, 3, -5);
		Vector3f v2 = new Vector3f(4, -2, -1);		
		assertEpsilonEquals(v1.dot(v2), 
				MathUtil.dotProduct(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ()));
	}

	/**
	 */
	public void testDotProductFloatFloatFloatFloat() {
		Vector2f v1 = new Vector2f(1, 3);
		Vector2f v2 = new Vector2f(4, -2);		
		assertEpsilonEquals(v1.dot(v2), 
				MathUtil.dotProduct(v1.getX(), v1.getY(), v2.getX(), v2.getY()));
	}

	/**
	 */
	public void testDeterminantFloatFloatFloatFloat() {
		Vector2f v1, v2;

		v1 = new Vector2f(0, 0);
		v2 = new Vector2f(0, 0);		
		assertEpsilonEquals(0, MathUtil.determinant(v1.getX(), v1.getY(), v2.getX(), v2.getY()));

		v1 = new Vector2f(1, 1);
		v2 = new Vector2f(1, 1);		
		assertEpsilonEquals(0, MathUtil.determinant(v1.getX(), v1.getY(), v2.getX(), v2.getY()));

		v1 = new Vector2f(1, 0);
		v2 = new Vector2f(0, 1);		
		assertEpsilonEquals(1, MathUtil.determinant(v1.getX(), v1.getY(), v2.getX(), v2.getY()));

		v1 = new Vector2f(1, 0);
		v2 = new Vector2f(1, 1);		
		assertEpsilonEquals(1, MathUtil.determinant(v1.getX(), v1.getY(), v2.getX(), v2.getY()));

		v1 = new Vector2f(1, 3);
		v2 = new Vector2f(4, -2);		
		assertEpsilonEquals(-14, MathUtil.determinant(v1.getX(), v1.getY(), v2.getX(), v2.getY()));
	}

	/**
	 */
	public void testClampFloatFloatFloat() {
		// NaN is lower than all the other floating-point values 
		
		float min = (AbstractMathTestCase.RANDOM.nextFloat() - AbstractMathTestCase.RANDOM.nextFloat()) * 1000.f;
		float max = min + AbstractMathTestCase.RANDOM.nextFloat() * 1000.f;
		
		assertEpsilonEquals(min, MathUtil.clamp(Float.NEGATIVE_INFINITY, min, max));
		assertEpsilonEquals(max, MathUtil.clamp(Float.POSITIVE_INFINITY, min, max));
		assertEpsilonEquals((min+max)/2.f, MathUtil.clamp((min+max)/2.f, min, max));
		assertEpsilonEquals(max, MathUtil.clamp(Math.abs(max)*2.f, min, max));
	} 

	/**
	 */
	public void testEpsilonCompareTofloatfloatfloat(){
		
		float value1,value2;
		
		value1 = RANDOM.nextFloat();
		value2 = RANDOM.nextFloat();
		
		assertEquals(Float.compare(Float.NaN, value2), MathUtil.epsilonCompareTo( Float.NaN, value2, epsilon));
		assertEquals(Float.compare(Float.POSITIVE_INFINITY, value2), MathUtil.epsilonCompareTo( Float.POSITIVE_INFINITY, value2, epsilon));
		assertEquals(Float.compare(Float.NEGATIVE_INFINITY, value2), MathUtil.epsilonCompareTo( Float.NEGATIVE_INFINITY, value2, epsilon));
		
		assertEquals(Float.compare(value1, Float.NaN), MathUtil.epsilonCompareTo(value1, Float.NaN, epsilon));
		assertEquals(Float.compare(value1, Float.POSITIVE_INFINITY), MathUtil.epsilonCompareTo(value1,  Float.POSITIVE_INFINITY, epsilon));
		assertEquals(Float.compare(value1, Float.NEGATIVE_INFINITY), MathUtil.epsilonCompareTo(value1,  Float.NEGATIVE_INFINITY, epsilon));
		
		assertEquals(0, MathUtil.epsilonCompareTo(value1,  value1+epsilon, epsilon));
		assertEquals(0, MathUtil.epsilonCompareTo(value1+epsilon, value1, epsilon));

		
		value2 = (Math.abs(value1 - value2) <= epsilon)?value2+2*epsilon: value2;
		assertEpsilonEquals(Float.compare(value1, value2), MathUtil.epsilonCompareTo(value1,  value2, epsilon));
	}
	
	public void testClampCyclicFloatFloatFloat(){
		
		float min,max;
		
		min = 5;
		max = 13;
		
		assertEpsilonEquals(min , MathUtil.clampCyclic(min, min, min));
		assertEpsilonEquals(min , MathUtil.clampCyclic(min-1, min, min));
		assertEpsilonEquals(min , MathUtil.clampCyclic(min+1, min, min));
		assertEpsilonEquals((min+max)/2, MathUtil.clampCyclic((min+max)/2,  min, max));
		assertEpsilonEquals(max-1, MathUtil.clampCyclic(min-1,  min, max));
		assertEpsilonEquals(min+1, MathUtil.clampCyclic(max+1,  min, max));
		
		assertEpsilonEquals((min+max)/2, MathUtil.clampCyclic((min+max)/2+(min-max),  min, max));
		assertEpsilonEquals(max-1, MathUtil.clampCyclic(min-1+(min-max),  min, max));
		assertEpsilonEquals(min+1, MathUtil.clampCyclic(max+1+(min-max),  min, max));
		
		assertEpsilonEquals((min+max)/2, MathUtil.clampCyclic((min+max)/2-(min-max),  min, max));
		assertEpsilonEquals(max-1, MathUtil.clampCyclic(min-1-(min-max),  min, max));
		assertEpsilonEquals(min+1, MathUtil.clampCyclic(max+1-(min-max),  min, max));
	
	}
}

