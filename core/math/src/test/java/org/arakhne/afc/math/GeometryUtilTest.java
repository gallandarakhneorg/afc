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
 * Foundation, Inc.f, 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math;

import org.arakhne.afc.math.geometry.GeometryUtil;
import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;
import org.arakhne.afc.math.geometry.continuous.object3d.EulerAngle;
import org.arakhne.afc.math.geometry.continuous.object3d.Point3f;
import org.arakhne.afc.math.geometry.continuous.object3d.Quaternion;
import org.arakhne.afc.math.geometry.continuous.object3d.Vector3f;
import org.arakhne.afc.math.geometry.continuous.object3d.plane.Plane;
import org.arakhne.afc.math.geometry.continuous.object3d.plane.Plane4f;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem3D;
import org.arakhne.afc.math.matrix.Matrix4f;
import org.arakhne.afc.util.OutputParameter;

/**
 * Test for {@link MathUtil}
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GeometryUtilTest extends AbstractMathTestCase {

	private static final float epsilon = 1e-12f;
	/**
	 * 
	 */
	public void testEpsilonColinearVector3dVector3d() {
		Vector3f v1, v2;
		
		v1 = new Vector3f();
		v2 = new Vector3f();
		assertTrue(MathUtil.epsilonColinear(v1, v2));
		assertTrue(MathUtil.epsilonColinear(v2, v1));

		v1 = new Vector3f(1,0,0);
		v2 = new Vector3f();
		assertTrue(MathUtil.epsilonColinear(v1, v2));
		assertTrue(MathUtil.epsilonColinear(v2, v1));

		v1 = new Vector3f(1,0,0);
		v2 = new Vector3f(1,0,0);
		assertTrue(MathUtil.epsilonColinear(v1, v2));
		assertTrue(MathUtil.epsilonColinear(v2, v1));

		v1 = new Vector3f(1,0,0);
		v2 = new Vector3f(-1,0,0);
		assertTrue(MathUtil.epsilonColinear(v1, v2));
		assertTrue(MathUtil.epsilonColinear(v2, v1));

		v1 = new Vector3f(1,0,0);
		v2 = new Vector3f(Float.POSITIVE_INFINITY,0,0);
		assertFalse(MathUtil.epsilonColinear(v1, v2));
		assertFalse(MathUtil.epsilonColinear(v2, v1));

		v1 = new Vector3f(1,0,0);
		v2 = new Vector3f(Float.MAX_VALUE,0,0);
		assertFalse(MathUtil.epsilonColinear(v1, v2)); // Capacity exceeded
		assertFalse(MathUtil.epsilonColinear(v2, v1));

		v1 = new Vector3f(0,1,0);
		v2 = new Vector3f(1,0,0);
		assertFalse(MathUtil.epsilonColinear(v1, v2));
		assertFalse(MathUtil.epsilonColinear(v2, v1));

		v1 = new Vector3f(1,1,1);
		v2 = new Vector3f(1,0,0);
		assertFalse(MathUtil.epsilonColinear(v1, v2));
		assertFalse(MathUtil.epsilonColinear(v2, v1));
	}

	/**
	 */
	public void testEpsilonColinearVector2fVector2f() {
		Vector2f v1, v2;
		
		v1 = new Vector2f();
		v2 = new Vector2f();
		assertTrue(MathUtil.epsilonColinear(v1, v2));
		assertTrue(MathUtil.epsilonColinear(v2, v1));

		v1 = new Vector2f(1,0);
		v2 = new Vector2f();
		assertTrue(MathUtil.epsilonColinear(v1, v2));
		assertTrue(MathUtil.epsilonColinear(v2, v1));

		v1 = new Vector2f(1,0);
		v2 = new Vector2f(1,0);
		assertTrue(MathUtil.epsilonColinear(v1, v2));
		assertTrue(MathUtil.epsilonColinear(v2, v1));

		v1 = new Vector2f(1,0);
		v2 = new Vector2f(-1,0);
		assertTrue(MathUtil.epsilonColinear(v1, v2));
		assertTrue(MathUtil.epsilonColinear(v2, v1));

		v1 = new Vector2f(1,0);
		v2 = new Vector2f(Float.POSITIVE_INFINITY,0);
		assertFalse(MathUtil.epsilonColinear(v1, v2));
		assertFalse(MathUtil.epsilonColinear(v2, v1));

		v1 = new Vector2f(1,0);
		v2 = new Vector2f(Float.MAX_VALUE,0);
		assertFalse(MathUtil.epsilonColinear(v1, v2)); // Capacity exceeded
		assertFalse(MathUtil.epsilonColinear(v2, v1)); // Capacity exceeded

		v1 = new Vector2f(0,1);
		v2 = new Vector2f(1,0);
		assertFalse(MathUtil.epsilonColinear(v1, v2));
		assertFalse(MathUtil.epsilonColinear(v2, v1));

		v1 = new Vector2f(1,1);
		v2 = new Vector2f(1,0);
		assertFalse(MathUtil.epsilonColinear(v1, v2));
		assertFalse(MathUtil.epsilonColinear(v2, v1));
	}

	/**
	 */
	public void testEpsilonEqualsVector3dVector3d() {
		Vector3f v1, v2;
		
		v1 = new Vector3f();
		v2 = new Vector3f();
		assertTrue(MathUtil.epsilonEquals(v1, v2));
		assertTrue(MathUtil.epsilonEquals(v2, v1));

		v1 = new Vector3f(1,0,0);
		v2 = new Vector3f();
		assertFalse(MathUtil.epsilonEquals(v1, v2));
		assertFalse(MathUtil.epsilonEquals(v2, v1));

		v1 = new Vector3f(1,0,0);
		v2 = new Vector3f(1,0,0);
		assertTrue(MathUtil.epsilonEquals(v1, v2));
		assertTrue(MathUtil.epsilonEquals(v2, v1));

		v1 = new Vector3f(1,0,0);
		v2 = new Vector3f(-1,0,0);
		assertFalse(MathUtil.epsilonEquals(v1, v2));
		assertFalse(MathUtil.epsilonEquals(v2, v1));

		v1 = new Vector3f(1,0,0);
		v2 = new Vector3f(Float.POSITIVE_INFINITY,0,0);
		assertFalse(MathUtil.epsilonEquals(v1, v2));
		assertFalse(MathUtil.epsilonEquals(v2, v1));

		v1 = new Vector3f(1,0,0);
		v2 = new Vector3f(Float.MAX_VALUE,0,0); // Capacity exceeded
		assertFalse(MathUtil.epsilonEquals(v1, v2));
		assertFalse(MathUtil.epsilonEquals(v2, v1));

		v1 = new Vector3f(0,1,0);
		v2 = new Vector3f(1,0,0);
		assertFalse(MathUtil.epsilonEquals(v1, v2));
		assertFalse(MathUtil.epsilonEquals(v2, v1));

		v1 = new Vector3f(1,1,1);
		v2 = new Vector3f(1,0,0);
		assertFalse(MathUtil.epsilonEquals(v1, v2));
		assertFalse(MathUtil.epsilonEquals(v2, v1));
	}

	/**
	 */
	public void testEpsilonEqualsVector2fVector2f() {
		Vector2f v1, v2;
		
		v1 = new Vector2f();
		v2 = new Vector2f();
		assertTrue(MathUtil.epsilonEquals(v1, v2));
		assertTrue(MathUtil.epsilonEquals(v2, v1));

		v1 = new Vector2f(1,0);
		v2 = new Vector2f();
		assertFalse(MathUtil.epsilonEquals(v1, v2));
		assertFalse(MathUtil.epsilonEquals(v2, v1));

		v1 = new Vector2f(1,0);
		v2 = new Vector2f(1,0);
		assertTrue(MathUtil.epsilonEquals(v1, v2));
		assertTrue(MathUtil.epsilonEquals(v2, v1));

		v1 = new Vector2f(1,0);
		v2 = new Vector2f(-1,0);
		assertFalse(MathUtil.epsilonEquals(v1, v2));
		assertFalse(MathUtil.epsilonEquals(v2, v1));

		v1 = new Vector2f(1,0);
		v2 = new Vector2f(Float.POSITIVE_INFINITY,0);
		assertFalse(MathUtil.epsilonEquals(v1, v2));
		assertFalse(MathUtil.epsilonEquals(v2, v1));

		v1 = new Vector2f(1,0);
		v2 = new Vector2f(Float.MAX_VALUE,0); // This vector will be normalize to (0,0,0)
		assertFalse(MathUtil.epsilonEquals(v1, v2));
		assertFalse(MathUtil.epsilonEquals(v2, v1));

		v1 = new Vector2f(0,1);
		v2 = new Vector2f(1,0);
		assertFalse(MathUtil.epsilonEquals(v1, v2));
		assertFalse(MathUtil.epsilonEquals(v2, v1));

		v1 = new Vector2f(1,1);
		v2 = new Vector2f(1,0);
		assertFalse(MathUtil.epsilonEquals(v1, v2));
		assertFalse(MathUtil.epsilonEquals(v2, v1));
	}

	/**
	 */
	public void testEpsilonEqualsPoint3dPoint3d() {
		Point3f p1, p2;
		
		p1 = this.randomPoint3D();
		p2 = this.randomPoint3D();
		
		float distance = p1.distance(p2);
		
		assertEquals(distance<=MathUtil.getDistanceEpsilon(), MathUtil.epsilonEquals(p1, p2));
		assertEquals(distance<=MathUtil.getDistanceEpsilon(), MathUtil.epsilonEquals(p2, p1));
	}

	/**
	 */
	public void testEpsilonEqualsPoint3dPoint3dFloat() {
		Point3f p1, p2;
		
		float eps = this.RANDOM.nextFloat() / 1000;
		
		p1 = this.randomPoint3D();
		p2 = this.randomPoint3D();
		
		float distance = p1.distance(p2);
		
		assertEquals(distance<=eps, MathUtil.epsilonEquals(p1, p2, eps));
		assertEquals(distance<=eps, MathUtil.epsilonEquals(p2, p1, eps));
	}

	/**
	 */
	public void testEpsilonEqualsPoint2dPoint2d() {
		Point2f p1, p2;
		
		p1 = this.randomPoint2D();
		p2 = this.randomPoint2D();
		
		float distance = p1.distance(p2);
		
		assertEquals(distance<=MathUtil.getDistanceEpsilon(), MathUtil.epsilonEquals(p1, p2));
		assertEquals(distance<=MathUtil.getDistanceEpsilon(), MathUtil.epsilonEquals(p2, p1));
	}

	/**
	 */
	public void testEpsilonEqualsPoint2dPoint2dFloat() {
		Point2f p1, p2;
		
		float eps = this.RANDOM.nextFloat() / 1000;
		
		p1 = this.randomPoint2D();
		p2 = this.randomPoint2D();
		
		float distance = p1.distance(p2);
		
		assertEquals(distance<=eps, MathUtil.epsilonEquals(p1, p2, eps));
		assertEquals(distance<=eps, MathUtil.epsilonEquals(p2, p1, eps));
	}

	/**
	 */
	public void testEpsilonEqualsFloatFloatFloatFloatFloatFloat() {
		Point3f p1, p2;
		
		p1 = this.randomPoint3D();
		p2 = this.randomPoint3D();
		
		float distance = p1.distance(p2);
		
		assertEquals(distance<=MathUtil.getDistanceEpsilon(), MathUtil.epsilonEquals(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ()));
		assertEquals(distance<=MathUtil.getDistanceEpsilon(), MathUtil.epsilonEquals(p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ()));
	}

	/**
	 */
	public void testEpsilonEqualsFloatFloatFloatFloat() {
		Point2f p1, p2;
		
		p1 = this.randomPoint2D();
		p2 = this.randomPoint2D();
		
		float distance = p1.distance(p2);
		
		assertEquals(distance<=MathUtil.getDistanceEpsilon(), MathUtil.epsilonEquals(p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(distance<=MathUtil.getDistanceEpsilon(), MathUtil.epsilonEquals(p2.getX(), p2.getY(), p1.getX(), p1.getY()));
	}

	/**
	 */
	public void testEpsilonEqualsDistancePoint3dPoint3d() {
		Point3f p1, p2;
		
		p1 = this.randomPoint3D();
		p2 = this.randomPoint3D();
		
		float distance = p1.distance(p2);
		
		assertEquals(distance<=MathUtil.getDistanceEpsilon(), MathUtil.epsilonEqualsDistance(p1, p2));
		assertEquals(distance<=MathUtil.getDistanceEpsilon(), MathUtil.epsilonEqualsDistance(p2, p1));
	}

	/**
	 */
	public void testEpsilonEqualsDistancePoint2dPoint2d() {
		Point2f p1, p2;
		
		p1 = this.randomPoint2D();
		p2 = this.randomPoint2D();
		
		float distance = p1.distance(p2);
		
		assertEquals(distance<=MathUtil.getDistanceEpsilon(), MathUtil.epsilonEqualsDistance(p1, p2));
		assertEquals(distance<=MathUtil.getDistanceEpsilon(), MathUtil.epsilonEqualsDistance(p2, p1));
	}

	/**
	 */
	public void testClampRadian0To2PI() {
		assertEquals(Float.NaN, MathUtil.clampRadian0To2PI(Float.NaN));
		assertEquals(Float.POSITIVE_INFINITY, MathUtil.clampRadian0To2PI(Float.POSITIVE_INFINITY));
		assertEquals(Float.NEGATIVE_INFINITY, MathUtil.clampRadian0To2PI(Float.NEGATIVE_INFINITY));
		
		float a = this.RANDOM.nextFloat() * MathConstants.PI * this.RANDOM.nextInt();
		
		float clamped = MathUtil.clampRadian0To2PI(a);
		
		assertPositive(clamped);
		assertTrue(clamped<MathConstants.TWO_PI);
		
		if (a<0) {
			assertEquals(MathConstants.TWO_PI + (a % MathConstants.TWO_PI), clamped);
		}
		else {
			assertEquals(a % MathConstants.TWO_PI, clamped);
		}
	}

	/**
	 */
	public void testClampRadianMinusPIToPI() {
		assertEquals(Float.NaN, MathUtil.clampRadianMinusPIToPI(Float.NaN));
		assertEquals(Float.POSITIVE_INFINITY, MathUtil.clampRadianMinusPIToPI(Float.POSITIVE_INFINITY));
		assertEquals(Float.NEGATIVE_INFINITY, MathUtil.clampRadianMinusPIToPI(Float.NEGATIVE_INFINITY));
		
		float a = this.RANDOM.nextFloat() * MathConstants.PI * this.RANDOM.nextInt();
		
		float clamped = MathUtil.clampRadianMinusPIToPI(a);
		
		assertTrue(clamped>=-MathConstants.PI);
		assertTrue(clamped<MathConstants.PI);
		
		if (a<0) {
			a = (a+MathConstants.PI) % MathConstants.TWO_PI;
			assertEpsilonEquals(MathConstants.PI+a, clamped);
		}
		else {
			a = (a+MathConstants.PI) % MathConstants.TWO_PI;
			assertEpsilonEquals(a-MathConstants.PI, clamped);
		}
	}

	/**
	 */
	public void testClampDegree0To360() {
		assertEquals(Float.NaN, MathUtil.clampDegree0To360(Float.NaN));
		assertEquals(Float.POSITIVE_INFINITY, MathUtil.clampDegree0To360(Float.POSITIVE_INFINITY));
		assertEquals(Float.NEGATIVE_INFINITY, MathUtil.clampDegree0To360(Float.NEGATIVE_INFINITY));
		
		float a = this.RANDOM.nextFloat() * 360.f * this.RANDOM.nextInt();
		
		float clamped = MathUtil.clampDegree0To360(a);
		
		assertPositive(clamped);
		assertTrue(clamped<360.f);
		
		if (a<0) {
			assertEquals(360.f + (a % 360.f), clamped);
		}
		else {
			assertEquals(a % 360.f, clamped);
		}
	}

	/**
	 */
	public void testEpsilonEqualsZeroRadianFloat() {
		float a, expected;
		float epsilon = MathUtil.getRadianEpsilon();
		
		assertFalse(MathUtil.epsilonEqualsZeroRadian(Float.NaN));
		assertFalse(MathUtil.epsilonEqualsZeroRadian(Float.POSITIVE_INFINITY));
		assertFalse(MathUtil.epsilonEqualsZeroRadian(Float.NEGATIVE_INFINITY));
		assertTrue(MathUtil.epsilonEqualsZeroRadian(0.f));
		assertTrue(MathUtil.epsilonEqualsZeroRadian(0.f-epsilon));
		assertTrue(MathUtil.epsilonEqualsZeroRadian(0.f+epsilon));
		
		a = this.RANDOM.nextFloat() * MathConstants.TWO_PI * this.RANDOM.nextInt();
		expected = Math.abs(MathConstants.PI - Math.abs(a%MathConstants.TWO_PI));
		
		assertEquals(expected>=(MathConstants.PI-epsilon), MathUtil.epsilonEqualsZeroRadian(a));
	}

	/**
	 */
	public void testEpsilonEqualsRadianFloatFloat() {
		float a1, a2, expected;
		float epsilon = MathUtil.getRadianEpsilon();
		
		assertFalse(MathUtil.epsilonEqualsRadian(Float.NaN, this.RANDOM.nextFloat()));
		assertFalse(MathUtil.epsilonEqualsRadian(Float.POSITIVE_INFINITY, this.RANDOM.nextFloat()));
		assertFalse(MathUtil.epsilonEqualsRadian(Float.NEGATIVE_INFINITY, this.RANDOM.nextFloat()));
		assertFalse(MathUtil.epsilonEqualsRadian(this.RANDOM.nextFloat(), Float.NaN));
		assertFalse(MathUtil.epsilonEqualsRadian(this.RANDOM.nextFloat(), Float.POSITIVE_INFINITY));
		assertFalse(MathUtil.epsilonEqualsRadian(this.RANDOM.nextFloat(), Float.NEGATIVE_INFINITY));
		
		a1 = this.RANDOM.nextFloat() * (epsilon / 4.f); 
		a2 = this.RANDOM.nextFloat() * (-epsilon / 4.f); 
		
		assertTrue(MathUtil.epsilonEqualsRadian(a1, a1));
		assertTrue(MathUtil.epsilonEqualsRadian(a2, a2));
		assertTrue(MathUtil.epsilonEqualsRadian(a1, a2));
		assertTrue(MathUtil.epsilonEqualsRadian(a2, a1));
		
		a1 = this.RANDOM.nextFloat() * MathConstants.TWO_PI * this.RANDOM.nextInt();
		a2 = this.RANDOM.nextFloat() * MathConstants.TWO_PI * this.RANDOM.nextInt();
		expected = Math.abs(MathConstants.PI - Math.abs((a1-a2)%MathConstants.TWO_PI));
		
		assertTrue(MathUtil.epsilonEqualsRadian(a1,a1));
		assertTrue(MathUtil.epsilonEqualsRadian(a2,a2));
		assertEquals(expected>=(MathConstants.PI-MathUtil.getRadianEpsilon()), MathUtil.epsilonEqualsRadian(a1,a2));
		assertEquals(expected>=(MathConstants.PI-MathUtil.getRadianEpsilon()), MathUtil.epsilonEqualsRadian(a2,a1));
	}

	/**
	 */
	public void testEpsilonEqualsZeroDegreeFloat() {
		float a, expected;
		float epsilon = (float) Math.toDegrees(MathUtil.getRadianEpsilon());
		
		assertFalse(MathUtil.epsilonEqualsZeroDegree(Float.NaN));
		assertFalse(MathUtil.epsilonEqualsZeroDegree(Float.POSITIVE_INFINITY));
		assertFalse(MathUtil.epsilonEqualsZeroDegree(Float.NEGATIVE_INFINITY));
		assertTrue(MathUtil.epsilonEqualsZeroDegree(0.f));
		assertTrue(MathUtil.epsilonEqualsZeroDegree(0.f-epsilon));
		assertTrue(MathUtil.epsilonEqualsZeroDegree(0.f+epsilon));
		
		a = this.RANDOM.nextFloat() * 360.f * this.RANDOM.nextInt();
		expected = Math.abs(180.f - Math.abs(a%360.f));
		
		assertEquals(expected>=(180.f-epsilon), MathUtil.epsilonEqualsZeroDegree(a));
	}

	/**
	 */
	public void testEpsilonEqualsDegreeFloatFloat() {
		float a1, a2, expected;
		float epsilon = MathUtil.getRadianEpsilon();
		float epsilonDeg = (float) Math.toDegrees(epsilon);

		assertFalse(MathUtil.epsilonEqualsDegree(Float.NaN, this.RANDOM.nextFloat()));
		assertFalse(MathUtil.epsilonEqualsDegree(Float.POSITIVE_INFINITY, this.RANDOM.nextFloat()));
		assertFalse(MathUtil.epsilonEqualsDegree(Float.NEGATIVE_INFINITY, this.RANDOM.nextFloat()));
		assertFalse(MathUtil.epsilonEqualsDegree(this.RANDOM.nextFloat(), Float.NaN));
		assertFalse(MathUtil.epsilonEqualsDegree(this.RANDOM.nextFloat(), Float.POSITIVE_INFINITY));
		assertFalse(MathUtil.epsilonEqualsDegree(this.RANDOM.nextFloat(), Float.NEGATIVE_INFINITY));
		
		a1 = this.RANDOM.nextFloat() * epsilon / 4.f; 
		a2 = -this.RANDOM.nextFloat() * epsilon / 4.f; 
		
		assertTrue(MathUtil.epsilonEqualsRadian(a1, a1));
		assertTrue(MathUtil.epsilonEqualsRadian(a2, a2));
		assertTrue(MathUtil.epsilonEqualsRadian(a1, a2));
		assertTrue(MathUtil.epsilonEqualsRadian(a2, a1));

		a1 = this.RANDOM.nextFloat() * 360.f * this.RANDOM.nextInt();
		a2 = this.RANDOM.nextFloat() * 360.f * this.RANDOM.nextInt();
		
		assertTrue(MathUtil.epsilonEqualsDegree(a1,a1));
		assertTrue(MathUtil.epsilonEqualsDegree(a2,a2));

		expected = Math.abs(180.f - Math.abs((a1-a2)%360.f));
		assertEquals(expected>=(180.f-epsilonDeg), MathUtil.epsilonEqualsDegree(a1,a2));
		assertEquals(expected>=(180.f-epsilonDeg), MathUtil.epsilonEqualsDegree(a2,a1));
	}

	/**
	 */
	public void testEpsilonRadianSignFloat() {
		assertEquals(1,MathUtil.epsilonRadianSign(Float.NaN));
		assertEquals(1,MathUtil.epsilonRadianSign(Float.POSITIVE_INFINITY));
		assertEquals(-1,MathUtil.epsilonRadianSign(Float.NEGATIVE_INFINITY));
		
		float a = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * MathConstants.TWO_PI * this.RANDOM.nextInt();

		int expectedInt;
		float rest = a % MathConstants.TWO_PI;
		if ((rest>=-MathUtil.getRadianEpsilon() && rest<=MathUtil.getRadianEpsilon())
			||
			(rest>=MathConstants.TWO_PI-MathUtil.getRadianEpsilon() && rest<=MathConstants.TWO_PI)
			||
			(rest<=-MathConstants.TWO_PI+MathUtil.getRadianEpsilon() && rest>=-MathConstants.TWO_PI))
			expectedInt = 0;
		else
			expectedInt = (int)Math.signum(rest);
		
		assertEquals(expectedInt, MathUtil.epsilonRadianSign(a));
	}

	/**
	 */
	public void testEpsilonDegreeSignFloat() {
		assertEquals(1,MathUtil.epsilonDegreeSign(Float.NaN));
		assertEquals(1,MathUtil.epsilonDegreeSign(Float.POSITIVE_INFINITY));
		assertEquals(-1,MathUtil.epsilonDegreeSign(Float.NEGATIVE_INFINITY));
		
		float a = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * 360.f * this.RANDOM.nextInt();

		int expectedInt;
		float rest = a % 360.f;
		float epsilon = (float) Math.toDegrees(MathUtil.getRadianEpsilon());
		if ((rest>=-epsilon && rest<=epsilon)
			||
			(rest>=360.f-epsilon && rest<=360.f)
			||
			(rest<=-360.f+epsilon && rest>=-360.f))
			expectedInt = 0;
		else
			expectedInt = (int)Math.signum(rest);
		
		assertEquals(expectedInt, MathUtil.epsilonDegreeSign(a));
	}

	/**
	 */
	public void testEpsilonCompareToRadianFloatFloat() {
		assertEquals(1,MathUtil.epsilonCompareToRadian(Float.NaN, this.RANDOM.nextFloat()));
		assertEquals(-1,MathUtil.epsilonCompareToRadian(this.RANDOM.nextFloat(), Float.NaN));
		assertEquals(1,MathUtil.epsilonCompareToRadian(Float.POSITIVE_INFINITY, this.RANDOM.nextFloat()));
		assertEquals(-1,MathUtil.epsilonCompareToRadian(this.RANDOM.nextFloat(), Float.POSITIVE_INFINITY));
		assertEquals(-1,MathUtil.epsilonCompareToRadian(Float.NEGATIVE_INFINITY, this.RANDOM.nextFloat()));
		assertEquals(1,MathUtil.epsilonCompareToRadian(this.RANDOM.nextFloat(), Float.NEGATIVE_INFINITY));
		
		float a1 = this.RANDOM.nextFloat() * MathConstants.TWO_PI * this.RANDOM.nextInt();
		float a2 = this.RANDOM.nextFloat() * MathConstants.TWO_PI * this.RANDOM.nextInt();
		
		assertEquals(0, MathUtil.epsilonCompareToRadian(a1,a1));
		assertEquals(0, MathUtil.epsilonCompareToRadian(a2,a2));

		int expectedInt;
		if (MathUtil.epsilonEqualsRadian(a1, a2))
			expectedInt = 0;
		else {
			float c1 = MathUtil.clampRadian0To2PI(a1);
			float c2 = MathUtil.clampRadian0To2PI(a2);
			expectedInt = Float.compare(c1, c2);
		}

		assertEquals(a1+"<=>"+a2, expectedInt, MathUtil.epsilonCompareToRadian(a1,a2)); //$NON-NLS-1$
		assertEquals(a1+"<=>"+a2, -expectedInt, MathUtil.epsilonCompareToRadian(a2,a1)); //$NON-NLS-1$
	}

	/**
	 */
	public void testEpsilonCompareToDegreeFloatFloat() {
		assertEquals(1,MathUtil.epsilonCompareToDegree(Float.NaN, this.RANDOM.nextFloat()));
		assertEquals(-1,MathUtil.epsilonCompareToDegree(this.RANDOM.nextFloat(), Float.NaN));
		assertEquals(1,MathUtil.epsilonCompareToDegree(Float.POSITIVE_INFINITY, this.RANDOM.nextFloat()));
		assertEquals(-1,MathUtil.epsilonCompareToDegree(this.RANDOM.nextFloat(), Float.POSITIVE_INFINITY));
		assertEquals(-1,MathUtil.epsilonCompareToDegree(Float.NEGATIVE_INFINITY, this.RANDOM.nextFloat()));
		assertEquals(1,MathUtil.epsilonCompareToDegree(this.RANDOM.nextFloat(), Float.NEGATIVE_INFINITY));
		
		float a1 = this.RANDOM.nextFloat() * 360.f * this.RANDOM.nextInt();
		float a2 = this.RANDOM.nextFloat() * 360.f * this.RANDOM.nextInt();
		
		assertEquals(0, MathUtil.epsilonCompareToDegree(a1,a1));
		assertEquals(0, MathUtil.epsilonCompareToDegree(a2,a2));

		int expectedInt;
		if (MathUtil.epsilonEqualsDegree(a1, a2))
			expectedInt = 0;
		else {
			float c1 = MathUtil.clampDegree0To360(a1);
			float c2 = MathUtil.clampDegree0To360(a2);
			expectedInt = Float.compare(c1, c2);
		}
		
		assertEquals(a1+"<=>"+a2, expectedInt, MathUtil.epsilonCompareToDegree(a1,a2)); //$NON-NLS-1$
		assertEquals(a1+"<=>"+a2, -expectedInt, MathUtil.epsilonCompareToDegree(a2,a1)); //$NON-NLS-1$
	}

	/**
	 */
	public void testEpsilonEqualsZeroTrigoFloat() {
		float a;
		float epsilon = MathUtil.getTrigonometricEpsilon();
		boolean expected;
		
		assertFalse(MathUtil.epsilonEqualsZeroTrigo(Float.NaN));
		assertFalse(MathUtil.epsilonEqualsZeroTrigo(Float.POSITIVE_INFINITY));
		assertFalse(MathUtil.epsilonEqualsZeroTrigo(Float.NEGATIVE_INFINITY));
		assertTrue(MathUtil.epsilonEqualsZeroTrigo(0.f));
		assertTrue(MathUtil.epsilonEqualsZeroTrigo(0.f-epsilon));
		assertTrue(MathUtil.epsilonEqualsZeroTrigo(0.f+epsilon));
		
		a = this.RANDOM.nextFloat();
		expected = (a>=-epsilon) && (a<=epsilon);
		
		assertEquals(expected, MathUtil.epsilonEqualsZeroTrigo(a));
	}

	/**
	 */
	public void testEpsilonEqualsTrigoFloatFloat() {
		float a1, a2;
		float epsilon = MathUtil.getTrigonometricEpsilon();
		boolean expected;
		
		assertFalse(MathUtil.epsilonEqualsTrigo(Float.NaN, this.RANDOM.nextFloat()));
		assertFalse(MathUtil.epsilonEqualsTrigo(Float.POSITIVE_INFINITY, this.RANDOM.nextFloat()));
		assertFalse(MathUtil.epsilonEqualsTrigo(Float.NEGATIVE_INFINITY, this.RANDOM.nextFloat()));
		assertFalse(MathUtil.epsilonEqualsTrigo(this.RANDOM.nextFloat(), Float.NaN));
		assertFalse(MathUtil.epsilonEqualsTrigo(this.RANDOM.nextFloat(), Float.POSITIVE_INFINITY));
		assertFalse(MathUtil.epsilonEqualsTrigo(this.RANDOM.nextFloat(), Float.NEGATIVE_INFINITY));
		
		a1 = this.RANDOM.nextFloat() * (epsilon / 4.f); 
		a2 = this.RANDOM.nextFloat() * (-epsilon / 4.f); 
		
		assertTrue(MathUtil.epsilonEqualsTrigo(a1, a1));
		assertTrue(MathUtil.epsilonEqualsTrigo(a2, a2));
		assertTrue(MathUtil.epsilonEqualsTrigo(a1, a2));
		assertTrue(MathUtil.epsilonEqualsTrigo(a2, a1));
		
		a1 = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat());
		a2 = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat());
		expected = (Math.abs(a2-a1)<=epsilon);
		
		assertTrue(MathUtil.epsilonEqualsTrigo(a1,a1));
		assertTrue(MathUtil.epsilonEqualsTrigo(a2,a2));
		assertEquals(expected, MathUtil.epsilonEqualsTrigo(a1,a2));
		assertEquals(expected, MathUtil.epsilonEqualsTrigo(a2,a1));
	}

	/**
	 */
	public void testEpsilonTrigoSignFloat() {
		assertEquals(1,MathUtil.epsilonTrigoSign(Float.NaN));
		assertEquals(1,MathUtil.epsilonTrigoSign(Float.POSITIVE_INFINITY));
		assertEquals(-1,MathUtil.epsilonTrigoSign(Float.NEGATIVE_INFINITY));
		
		float a = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat());
		int expected;
		if (Math.abs(a)<=MathUtil.getTrigonometricEpsilon())
			expected = 0;
		else
			expected = (int)Math.signum(a);

		assertEquals(expected, MathUtil.epsilonTrigoSign(a));
	}

	/**
	 */
	public void testEpsilonCompareToTrigoFloatFloat() {
		assertEquals(1,MathUtil.epsilonCompareToTrigo(Float.NaN, this.RANDOM.nextFloat()));
		assertEquals(-1,MathUtil.epsilonCompareToTrigo(this.RANDOM.nextFloat(), Float.NaN));
		assertEquals(1,MathUtil.epsilonCompareToTrigo(Float.POSITIVE_INFINITY, this.RANDOM.nextFloat()));
		assertEquals(-1,MathUtil.epsilonCompareToTrigo(this.RANDOM.nextFloat(), Float.POSITIVE_INFINITY));
		assertEquals(-1,MathUtil.epsilonCompareToTrigo(Float.NEGATIVE_INFINITY, this.RANDOM.nextFloat()));
		assertEquals(1,MathUtil.epsilonCompareToTrigo(this.RANDOM.nextFloat(), Float.NEGATIVE_INFINITY));
		
		float a1 = this.RANDOM.nextFloat()-this.RANDOM.nextFloat();
		float a2 = this.RANDOM.nextFloat()-this.RANDOM.nextFloat();
		
		assertEquals(0, MathUtil.epsilonCompareToTrigo(a1,a1));
		assertEquals(0, MathUtil.epsilonCompareToTrigo(a2,a2));

		int expectedInt;
		if (Math.abs(a2-a1)<=MathUtil.getTrigonometricEpsilon())
			expectedInt = 0;
		else {
			expectedInt = Float.compare(a1, a2);
		}

		assertEquals(a1+"<=>"+a2, expectedInt, MathUtil.epsilonCompareToTrigo(a1,a2)); //$NON-NLS-1$
		assertEquals(a1+"<=>"+a2, -expectedInt, MathUtil.epsilonCompareToTrigo(a2,a1)); //$NON-NLS-1$
	}

	/**
	 */
	public void testEpsilonEqualsZeroDistanceFloat() {
		float a;
		float epsilon = MathUtil.getDistanceEpsilon();
		boolean expected;
		
		assertFalse(MathUtil.epsilonEqualsZeroDistance(Float.NaN));
		assertFalse(MathUtil.epsilonEqualsZeroDistance(Float.POSITIVE_INFINITY));
		assertFalse(MathUtil.epsilonEqualsZeroDistance(Float.NEGATIVE_INFINITY));
		assertTrue(MathUtil.epsilonEqualsZeroDistance(0.f));
		assertTrue(MathUtil.epsilonEqualsZeroDistance(0.f-epsilon));
		assertTrue(MathUtil.epsilonEqualsZeroDistance(0.f+epsilon));
		
		a = this.RANDOM.nextFloat();
		expected = (a>=-epsilon) && (a<=epsilon);
		
		assertEquals(expected, MathUtil.epsilonEqualsZeroDistance(a));
	}

	/**
	 */
	public void testEpsilonEqualsDistanceFloatFloat() {
		float a1, a2;
		float epsilon = MathUtil.getDistanceEpsilon();
		boolean expected;
		
		assertFalse(MathUtil.epsilonEqualsDistance(Float.NaN, this.RANDOM.nextFloat()));
		assertFalse(MathUtil.epsilonEqualsDistance(Float.POSITIVE_INFINITY, this.RANDOM.nextFloat()));
		assertFalse(MathUtil.epsilonEqualsDistance(Float.NEGATIVE_INFINITY, this.RANDOM.nextFloat()));
		assertFalse(MathUtil.epsilonEqualsDistance(this.RANDOM.nextFloat(), Float.NaN));
		assertFalse(MathUtil.epsilonEqualsDistance(this.RANDOM.nextFloat(), Float.POSITIVE_INFINITY));
		assertFalse(MathUtil.epsilonEqualsDistance(this.RANDOM.nextFloat(), Float.NEGATIVE_INFINITY));
		
		a1 = this.RANDOM.nextFloat() * (epsilon / 4.f); 
		a2 = this.RANDOM.nextFloat() * (-epsilon / 4.f); 
		
		assertTrue(MathUtil.epsilonEqualsDistance(a1, a1));
		assertTrue(MathUtil.epsilonEqualsDistance(a2, a2));
		assertTrue(MathUtil.epsilonEqualsDistance(a1, a2));
		assertTrue(MathUtil.epsilonEqualsDistance(a2, a1));
		
		a1 = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat());
		a2 = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat());
		expected = (Math.abs(a2-a1)<=epsilon);
		
		assertTrue(MathUtil.epsilonEqualsDistance(a1,a1));
		assertTrue(MathUtil.epsilonEqualsDistance(a2,a2));
		assertEquals(expected, MathUtil.epsilonEqualsDistance(a1,a2));
		assertEquals(expected, MathUtil.epsilonEqualsDistance(a2,a1));
	}

	/**
	 */
	public void testEpsilonDistanceSignFloat() {
		assertEquals(1,MathUtil.epsilonDistanceSign(Float.NaN));
		assertEquals(1,MathUtil.epsilonDistanceSign(Float.POSITIVE_INFINITY));
		assertEquals(-1,MathUtil.epsilonDistanceSign(Float.NEGATIVE_INFINITY));
		
		float a = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat());
		int expected;
		if (Math.abs(a)<=MathUtil.getDistanceEpsilon())
			expected = 0;
		else
			expected = (int)Math.signum(a);

		assertEquals(expected, MathUtil.epsilonDistanceSign(a));
	}

	/**
	 */
	public void testEpsilonCompareToDistanceFloatFloat() {
		assertEquals(1,MathUtil.epsilonCompareToDistance(Float.NaN, this.RANDOM.nextFloat()));
		assertEquals(-1,MathUtil.epsilonCompareToDistance(this.RANDOM.nextFloat(), Float.NaN));
		assertEquals(1,MathUtil.epsilonCompareToDistance(Float.POSITIVE_INFINITY, this.RANDOM.nextFloat()));
		assertEquals(-1,MathUtil.epsilonCompareToDistance(this.RANDOM.nextFloat(), Float.POSITIVE_INFINITY));
		assertEquals(-1,MathUtil.epsilonCompareToDistance(Float.NEGATIVE_INFINITY, this.RANDOM.nextFloat()));
		assertEquals(1,MathUtil.epsilonCompareToDistance(this.RANDOM.nextFloat(), Float.NEGATIVE_INFINITY));
		
		float a1 = this.RANDOM.nextFloat()-this.RANDOM.nextFloat();
		float a2 = this.RANDOM.nextFloat()-this.RANDOM.nextFloat();
		
		assertEquals(0, MathUtil.epsilonCompareToDistance(a1,a1));
		assertEquals(0, MathUtil.epsilonCompareToDistance(a2,a2));

		int expectedInt;
		if (Math.abs(a2-a1)<=MathUtil.getDistanceEpsilon())
			expectedInt = 0;
		else {
			expectedInt = Float.compare(a1, a2);
		}

		assertEquals(a1+"<=>"+a2, expectedInt, MathUtil.epsilonCompareToDistance(a1,a2)); //$NON-NLS-1$
		assertEquals(a1+"<=>"+a2, -expectedInt, MathUtil.epsilonCompareToDistance(a2,a1)); //$NON-NLS-1$
	}

	/**
	 */
	public void testDistanceFloatFloatFloatFloatFloatFloat() {
		Point3f p1 = randomPoint3D();
		Point3f p2 = randomPoint3D();
		
		assertZero(MathUtil.distancePointPoint(p1.getX(),p1.getY(),p1.getZ(), p1.getX(),p1.getY(),p1.getZ()));
		assertZero(MathUtil.distancePointPoint(p2.getX(),p2.getY(),p2.getZ(), p2.getX(),p2.getY(),p2.getZ()));

		float dx = p1.getX() - p2.getX();
		float dy = p1.getY() - p2.getY();
		float dz = p1.getZ() - p2.getZ();		
		float expectedDistance = (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
		
		assertEquals(expectedDistance, MathUtil.distancePointPoint(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertEquals(expectedDistance, MathUtil.distancePointPoint(p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));
	}

	/**
	 */
	public void testDistanceFloatFloat() {
		Vector2f p1 = randomVector2D();		
		assertEquals(p1.length(), MathUtil.distance(p1.getX(),p1.getY()));
	}

	/**
	 */
	public void testDistanceFloatFloatFloat() {
		Vector3f p1 = randomVector3D();		
		assertEquals(p1.length(), MathUtil.distance(p1.getX(),p1.getY(),p1.getZ()));
	}

	/**
	 */
	public void testDistanceFloatFloatFloatFloat() {
		Point2f p1 = randomPoint2D();
		Point2f p2 = randomPoint2D();
		
		assertZero(MathUtil.distance(p1.getX(),p1.getY(), p1.getX(),p1.getY()));
		assertZero(MathUtil.distance(p2.getX(),p2.getY(), p2.getX(),p2.getY()));

		float dx = p1.getX() - p2.getX();
		float dy = p1.getY() - p2.getY();
		float expectedDistance = (float) Math.sqrt((dx * dx) + (dy * dy));
		
		assertEquals(expectedDistance, MathUtil.distance(p1.getX(),p1.getY(), p2.getX(),p2.getY()));
		assertEquals(expectedDistance, MathUtil.distance(p2.getX(),p2.getY(), p1.getX(),p1.getY()));
	}

	/**
	 */
	public void testDistanceTuple3dTuple3d() {
		Point3f p1 = randomPoint3D();
		Point3f p2 = randomPoint3D();
		
		assertZero(MathUtil.distance(p1, p1));
		assertZero(MathUtil.distance(p2, p2));
		
		assertEquals(p1.distance(p2), MathUtil.distance(p1, p2));
		assertEquals(p1.distance(p2), MathUtil.distance(p2, p1));
	}

	/**
	 */
	public void testDistanceTuple2dTuple2d() {
		Point2f p1 = randomPoint2D();
		Point2f p2 = randomPoint2D();
		
		assertZero(MathUtil.distance(p1, p1));
		assertZero(MathUtil.distance(p2, p2));
		
		assertEquals(p1.distance(p2), MathUtil.distance(p1, p2));
		assertEquals(p1.distance(p2), MathUtil.distance(p2, p1));
	}

	/**
	 */
	public void testDistanceSquaredFloatFloatFloatFloatFloatFloat() {
		Point3f p1 = randomPoint3D();
		Point3f p2 = randomPoint3D();
		
		assertZero(MathUtil.distanceSquaredPointPoint(p1.getX(),p1.getY(),p1.getZ(), p1.getX(),p1.getY(),p1.getZ()));
		assertZero(MathUtil.distanceSquaredPointPoint(p2.getX(),p2.getY(),p2.getZ(), p2.getX(),p2.getY(),p2.getZ()));

		float dx = p1.getX() - p2.getX();
		float dy = p1.getY() - p2.getY();
		float dz = p1.getZ() - p2.getZ();		
		float expectedDistance = (dx * dx) + (dy * dy) + (dz * dz);
		
		assertEquals(expectedDistance, MathUtil.distanceSquaredPointPoint(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertEquals(expectedDistance, MathUtil.distanceSquaredPointPoint(p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));
	}

	/**
	 */
	public void testDistanceSquaredFloatFloatFloatFloat() {
		Point2f p1 = randomPoint2D();
		Point2f p2 = randomPoint2D();
		
		assertZero(MathUtil.distanceSquared(p1.getX(),p1.getY(), p1.getX(),p1.getY()));
		assertZero(MathUtil.distanceSquared(p2.getX(),p2.getY(), p2.getX(),p2.getY()));

		float dx = p1.getX() - p2.getX();
		float dy = p1.getY() - p2.getY();
		float expectedDistance = (dx * dx) + (dy * dy);
		
		assertEquals(expectedDistance, MathUtil.distanceSquared(p1.getX(),p1.getY(), p2.getX(),p2.getY()));
		assertEquals(expectedDistance, MathUtil.distanceSquared(p2.getX(),p2.getY(), p1.getX(),p1.getY()));
	}

	/**
	 */
	public void testDistanceSquaredTuple3dTuple3d() {
		Point3f p1 = randomPoint3D();
		Point3f p2 = randomPoint3D();
		
		assertZero(MathUtil.distanceSquared(p1, p1));
		assertZero(MathUtil.distanceSquared(p2, p2));
		
		assertEquals(p1.distanceSquared(p2), MathUtil.distanceSquared(p1, p2));
		assertEquals(p1.distanceSquared(p2), MathUtil.distanceSquared(p2, p1));
	}

	/**
	 */
	public void testDistanceSquaredTuple2dTuple2d() {
		Point2f p1 = randomPoint2D();
		Point2f p2 = randomPoint2D();
		
		assertZero(MathUtil.distanceSquared(p1, p1));
		assertZero(MathUtil.distanceSquared(p2, p2));
		
		assertEquals(p1.distanceSquared(p2), MathUtil.distanceSquared(p1, p2));
		assertEquals(p1.distanceSquared(p2), MathUtil.distanceSquared(p2, p1));
	}

	/**
	 */
	public void testDistancePointLineFloatFloatFloatFloatFloatFloat() {
		Point2f p1 = randomPoint2D();
		Point2f p2 = randomPoint2D();
		Point2f p3 = randomPoint2D();
		
		float a1 = (p3.getY()-p2.getY()) / (p3.getX()-p2.getX());
		float b1 = p2.getY() - a1 * p2.getX();
		
		float a2 = -1.f/a1;
		float b2 = p1.getY() - a2 * p1.getX();
		
		float yi = (b1 - ((a1*b2)/a2)) / (1.f - (a1/a2));
		float xi = (yi - b2) / a2;
		
		float xx = xi - p1.getX();
		float yy = yi - p1.getY();
		float distance = (float) Math.sqrt(xx*xx+yy*yy);
		
		assertEpsilonEquals(distance, MathUtil.distancePointLine(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));
	}

	/**
	 */
	public void testDistancePointLineFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		Point3f p1 = randomPoint3D();
		Point3f p2 = randomPoint3D();
		Point3f p3 = randomPoint3D();
		
		Vector3f v1 = new Vector3f(p3.getX()-p2.getX(),p3.getY()-p2.getY(),p3.getZ()-p2.getZ());
		Vector3f v2 = new Vector3f(p1.getX()-p2.getX(),p1.getY()-p2.getY(),p1.getZ()-p2.getZ());
		
		Vector3f v3 = new Vector3f();
		v3.cross(v1, v2);
		
		float distance = v3.length() / v1.length();
				
		assertEpsilonEquals(distance, MathUtil.distancePointLine(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ()));
	}

	/**
	 */
	public void testDistancePointSegmentFloatFloatFloatFloatFloatFloat() {
		Point2f p1 = randomPoint2D();
		Point2f p2 = randomPoint2D();
		Point2f p3 = randomPoint2D();
				
		float d1 = p2.distance(p1);
		float d2 = p3.distance(p1);
		float d3 = MathUtil.distancePointLine(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
		
		float r = MathUtil.getPointProjectionFactorOnSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
		
		float expectedDistance;
		if (r<0.f)
			expectedDistance = d1;
		else if (r>1.f)
			expectedDistance = d2;
		else
			expectedDistance = d3;

		assertEpsilonEquals(expectedDistance, MathUtil.distancePointSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));
	}
	
	/**
	 */
	public void testProjectsPointOnSegmentFloatFloatFloatFloatFloatFloat() {
		Point2f p1 = randomPoint2D();
		Point2f p2 = randomPoint2D();
		Point2f p3 = randomPoint2D();
		
		float expected;

		/*
		 * L = sqrt( (Bx-Ax)^2 + (By-Ay)^2 )
		 * 
		 *     (Cx-Ax)(Bx-Ax) + (Cy-Ay)(By-Ay)
		 * r = -------------------------------
		 *                  L^2
		 */
		float L = (float) Math.sqrt(Math.pow(p3.getX()-p2.getX(), 2.f) + Math.pow(p3.getY()-p2.getY(), 2.f));
		expected = ((p1.getX()-p2.getX())*(p3.getX()-p2.getX()) + (p1.getY()-p2.getY())*(p3.getY()-p2.getY())) / (L*L);
		
		float actual = MathUtil.getPointProjectionFactorOnSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
		
		assertEpsilonEquals(expected, actual);

	
		p1 = new Point2f(-100.f,-100.f);
		p2 = new Point2f(100.f,100.f);
		
		p3 = new Point2f(p1);
		assertZero(MathUtil.getPointProjectionFactorOnSegment(p3.getX(), p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(1.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(p2);
		assertEquals(1.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(), p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(-0.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(0.f, 0.f);
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(), p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(-100.f, 100.f);
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(), p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(100.f, -100.f);
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(), p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(-300, -300.f);
		assertEquals(-1.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(), p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(2.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(300, 300.f);
		assertEquals(2.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(), p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(-1.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));
	}

	/**
	 */
	public void testDistancePointSegmentFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		Point3f p1 = randomPoint3D();
		Point3f p2 = randomPoint3D();
		Point3f p3 = randomPoint3D();
				
		float d1 = p2.distance(p1);
		float d2 = p3.distance(p1);
		float d3 = MathUtil.distancePointLine(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ());
		
		Vector3f v1 = new Vector3f(p3.getX()-p2.getX(), p3.getY()-p2.getY(), p3.getZ()-p2.getZ());
		Vector3f v2 = new Vector3f(p1.getX()-p2.getX(), p1.getY()-p2.getY(), p1.getZ()-p2.getZ());

		Vector3f v1n = new Vector3f(v1);
		v1n.normalize();
		Vector3f v2n = new Vector3f(v2);
		v2n.normalize();
		
		float expectedDistance;
		
		if (v1n.dot(v2n)<=0) { // Before first point
			expectedDistance = d1;
		}
		else {
			v1n.negate();
			v2 = new Vector3f(p1.getX()-p3.getX(), p1.getY()-p3.getY(), p1.getZ()-p3.getZ());
			v2n = new Vector3f(v2);
			v2n.normalize();

			if (v1n.dot(v2n)<=0) { // After first point
				expectedDistance = d2;
			}
			else {
				expectedDistance = d3;
			}
		}

		assertEpsilonEquals(expectedDistance, MathUtil.distancePointSegment(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ()));
	}

	/**
	 */
	public void testProjectsPointOnPlaneIn3dTuple3dPlane() {
		float a = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * 50.f;
		float b = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * 50.f;
		float c = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * 50.f;
		float d = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * 50.f;
		Plane plane = new Plane4f(a,b,c,d);
		Vector3f normal = new Vector3f(a,b,c);
		Point3f p = randomPoint3D();
		
		Point3f result = MathUtil.projectsPointOnPlaneIn3d(p, plane);
		
		// Point on plane
		assertEpsilonEquals(0.f, a*result.getX() + b*result.getY() + c*result.getZ() + d);

		// Vector from p to the projection must be parallel to the normal
		Vector3f projectVector = new Vector3f(p);
		projectVector.sub(result);
		
		normal.normalize();
		projectVector.normalize();
		
		float dot = Math.abs(normal.dot(projectVector));
		
		assertEpsilonEquals(1.f, dot);
	}

	/**
	 */
	public void testProjectsPointOnPlaneIn3dFloatFloatFloatPlane() {
		float a = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * 50.f;
		float b = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * 50.f;
		float c = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * 50.f;
		float d = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * 50.f;
		Plane plane = new Plane4f(a,b,c,d);
		Vector3f normal = new Vector3f(a,b,c);
		Point3f p = randomPoint3D();
		
		Point3f result = MathUtil.projectsPointOnPlaneIn3d(p, plane);
		
		// Point on plane
		assertEpsilonEquals(0.f, a*result.getX() + b*result.getY() + c*result.getZ() + d);

		// Vector from p to the projection must be parallel to the normal
		Vector3f projectVector = new Vector3f(p);
		projectVector.sub(result);
		
		normal.normalize();
		projectVector.normalize();
		
		float dot = Math.abs(normal.dot(projectVector));
		
		assertEpsilonEquals(1.f, dot);
	}

	/**
	 */
	public void testSignedAngleFloatFloatFloatFloat() {
		Vector2f v1 = randomVector2D();
		Vector2f v2 = randomVector2D();
		
		assertEpsilonEquals(0.f, GeometryUtil.signedAngle(v1.getX(), v1.getY(), v1.getX(), v1.getY()));
		assertEpsilonEquals(0.f, GeometryUtil.signedAngle(v2.getX(), v2.getY(), v2.getX(), v2.getY()));
		
		float sAngle1 = GeometryUtil.signedAngle(v1.getX(), v1.getY(), v2.getX(), v2.getY());
		float sAngle2 = GeometryUtil.signedAngle(v2.getX(), v2.getY(), v1.getX(), v1.getY());
		
		assertEpsilonEquals(-sAngle1, sAngle2);
		assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
		assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
		
		float sin = v1.getX()*v2.getY() - v1.getY()*v2.getX();
		
		if (sin<0) {
			assertNegative(sAngle1);
			assertPositive(sAngle2);
		}
		else {
			assertPositive(sAngle1);
			assertNegative(sAngle2);
		}
	}

	/**
	 */
	public void testTurnVectorVector2DFloat_orientation() {
		Vector2f v, expected;
		
		v = new Vector2f(1.f, 0.f);
		MathUtil.turnVector(v, MathConstants.TWO_PI);
		assertEpsilonEquals(new Vector2f(1.f, 0.f), v);

		v = new Vector2f(1.f, 0.f);
		MathUtil.turnVector(v, MathConstants.PI);
		assertEpsilonEquals(new Vector2f(-1.f, 0.f), v);

		v = new Vector2f(1.f, 0.f);
		MathUtil.turnVector(v, MathConstants.DEMI_PI);
		assertEpsilonEquals(new Vector2f(0.f, 1.f), v);

		v = new Vector2f(1.f, 0.f);
		MathUtil.turnVector(v, -MathConstants.DEMI_PI);
		assertEpsilonEquals(new Vector2f(0.f, -1.f), v);

		v = new Vector2f(1.f, 1.f);
		v.normalize();
		MathUtil.turnVector(v, -MathConstants.QUARTER_PI);
		assertEpsilonEquals(new Vector2f(1.f, 0.f), v);

		v = new Vector2f(1.f, 1.f);
		v.normalize();
		MathUtil.turnVector(v, -MathConstants.DEMI_PI);
		expected = new Vector2f(1.f, -1.f);
		expected.normalize();
		assertEpsilonEquals(expected, v);
	}

	/**
	 */
	public void testTurnVectorVector2DFloat_length() {
		Vector2f v = randomVector2D();
		v.scale(this.RANDOM.nextFloat() * 100.f);
		
		float expectedLength = v.length();
		
		MathUtil.turnVector(v, MathConstants.PI*(this.RANDOM.nextFloat() - this.RANDOM.nextFloat()));

		assertEpsilonEquals(expectedLength, v.length());
	}

	/**
	 */
	public void testSignedAngleFloatFloatFloatFloatFloatFloat() {
		{
			// top-right quarter
			Vector3f v1 = new Vector3f(this.RANDOM.nextFloat(), this.RANDOM.nextFloat(), 0);
			// top-left quarter
			Vector3f v2 = new Vector3f(-this.RANDOM.nextFloat(), this.RANDOM.nextFloat(), 0);
			
			float sAngle1 = GeometryUtil.signedAngle(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ());
			float sAngle2 = GeometryUtil.signedAngle(v2.getX(), v2.getY(), v2.getZ(), v1.getX(), v1.getY(), v1.getZ());

			assertEpsilonEquals(-sAngle1, sAngle2);
			assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
			assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
			
			assertPositive(sAngle1);
			assertNegative(sAngle2);
		}
		
		{
			// top-right quarter
			Vector3f v1 = new Vector3f(this.RANDOM.nextFloat(), this.RANDOM.nextFloat(), 0);
			// bottom-right quarter
			Vector3f v2 = new Vector3f(this.RANDOM.nextFloat(), -this.RANDOM.nextFloat(), 0);
			
			float sAngle1 = GeometryUtil.signedAngle(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ());
			float sAngle2 = GeometryUtil.signedAngle(v2.getX(), v2.getY(), v2.getZ(), v1.getX(), v1.getY(), v1.getZ());

			assertEpsilonEquals(-sAngle1, sAngle2);
			assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
			assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
			
			assertNegative(sAngle1);
			assertPositive(sAngle2);
		}

		{
			Vector3f v1 = randomVector3D();
			Vector3f v2 = randomVector3D();
			
			assertEpsilonEquals(0.f, GeometryUtil.signedAngle(v1.getX(), v1.getY(), v1.getZ(), v1.getX(), v1.getY(), v1.getZ()));
			assertEpsilonEquals(0.f, GeometryUtil.signedAngle(v2.getX(), v2.getY(), v2.getZ(), v2.getX(), v2.getY(), v2.getZ()));
			
			float sAngle1 = GeometryUtil.signedAngle(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ());
			float sAngle2 = GeometryUtil.signedAngle(v2.getX(), v2.getY(), v2.getZ(), v1.getX(), v1.getY(), v1.getZ());
			
			assertEpsilonEquals(-sAngle1, sAngle2);
			assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
			assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
			
			if (MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ())<0.f) {
				assertNegative(sAngle1);
				assertPositive(sAngle2);
			}
			else {
				assertPositive(sAngle1);
				assertNegative(sAngle2);
			}
		}
	}

	/**
	 */
	public void testDistanceSegmentSegmentFloatFloatFloatFloatFloatFloatFloatFloat() {
		Point2f s11 = randomPoint2D();
		Point2f s12 = randomPoint2D();
		Point2f s21 = randomPoint2D();
		Point2f s22 = randomPoint2D();
		
		// Distance between the same segment twice times is zero
		assertEpsilonEquals(0.f, MathUtil.distanceSegmentSegment(s11.getX(), s11.getY(), s12.getX(), s12.getY(), s11.getX(), s11.getY(), s12.getX(), s12.getY()));
		assertEpsilonEquals(0.f, MathUtil.distanceSegmentSegment(s21.getX(), s21.getY(), s22.getX(), s22.getY(), s21.getX(), s21.getY(), s22.getX(), s22.getY()));
				
		float distance = MathUtil.distanceSegmentSegment(s11.getX(), s11.getY(), s12.getX(), s12.getY(), s21.getX(), s21.getY(), s22.getX(), s22.getY());

		// The order of the segments' points is not significant
		assertEpsilonEquals(distance, MathUtil.distanceSegmentSegment(s12.getX(), s12.getY(), s11.getX(), s11.getY(), s21.getX(), s21.getY(), s22.getX(), s22.getY()));
		assertEpsilonEquals(distance, MathUtil.distanceSegmentSegment(s11.getX(), s11.getY(), s12.getX(), s12.getY(), s22.getX(), s22.getY(), s21.getX(), s21.getY()));
		assertEpsilonEquals(distance, MathUtil.distanceSegmentSegment(s12.getX(), s12.getY(), s11.getX(), s11.getY(), s22.getX(), s22.getY(), s21.getX(), s21.getY()));
		
		// Does the segments intersecting?
		float pCD = (s21.getY()-s22.getY())/(s21.getX()-s22.getX());
		float pAB = (s11.getY()-s12.getY())/(s11.getX()-s12.getX());
		float oCD = s21.getY()-pCD*s21.getX();
		float oAB = s11.getY()-pAB*s11.getX();
		float Sx = (oAB-oCD)/(pCD-pAB);
		float Sy = pCD*Sx+oCD;

		if((Sx<s11.getX() && Sx<s12.getX())|(Sx>s11.getX() && Sx>s12.getX()) | (Sx<s21.getX() && Sx<s22.getX())|(Sx>s21.getX() && Sx>s22.getX())
			| (Sy<s11.getY() && Sy<s12.getY())|(Sy>s11.getY() && Sy>s12.getY()) | (Sy<s21.getY() && Sy<s22.getY())|(Sy>s21.getY() && Sy>s22.getY())) {
			// Compute the expected distance
			float expectedDistance = MathUtil.min(
					MathUtil.distancePointSegment(s11.getX(), s11.getY(), s21.getX(), s21.getY(), s22.getX(), s22.getY()), 
					MathUtil.distancePointSegment(s12.getX(), s12.getY(), s21.getX(), s21.getY(), s22.getX(), s22.getY()),
					MathUtil.distancePointSegment(s21.getX(), s21.getY(), s11.getX(), s11.getY(), s12.getX(), s12.getY()), 
					MathUtil.distancePointSegment(s22.getX(), s22.getY(), s11.getX(), s11.getY(), s12.getX(), s12.getY()));
			
			// Test if the expected distance is equal to the computed distance.
			assertEpsilonEquals(expectedDistance, distance);
		}
		else {
			assertEpsilonEquals(0.f, distance);
		}
	}

	/**
	 */
	public void testAngleFloatFloatFloatFloatFloatFloat() {
		Vector3f v1 = randomVector3D();
		Vector3f v2 = randomVector3D();
		
		float angle1 = MathUtil.angle(v1.getX(),v1.getY(),v1.getZ(), v2.getX(),v2.getY(),v2.getZ());
		float angle2 = MathUtil.angle(v2.getX(),v2.getY(),v2.getZ(), v1.getX(),v1.getY(),v1.getZ());
		
		assertPositive(angle1);
		assertEpsilonEquals(angle1, angle2);
		assertEpsilonEquals(angle1, v1.angle(v2));
		assertEpsilonEquals(angle2, v2.angle(v1));
	}

	/**
	 */
	public void testAngleFloatFloatFloatFloat() {
		Vector2f v1 = randomVector2D();
		Vector2f v2 = randomVector2D();
		
		float angle1 = MathUtil.angle(v1.getX(),v1.getY(), v2.getX(),v2.getY());
		float angle2 = MathUtil.angle(v2.getX(),v2.getY(), v1.getX(),v1.getY());
		
		assertPositive(angle1);
		assertEpsilonEquals(angle1, angle2);
		assertEpsilonEquals(angle1, v1.angle(v2));
		assertEpsilonEquals(angle2, v2.angle(v1));
	}

	/**
	 */
	public void testEuler2quatFloatFloatFloatCoordinateSystem3D_XZY_RIGHT_HAND() {
		// Values given by the formular on http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm
		// Be carrefull about the standards used on this website. They are mostly different from our standards. 
		Quaternion q;
		
		q = MathUtil.euler2quat(0.f,0.f,0.f,CoordinateSystem3D.XZY_RIGHT_HAND);
		q.normalize();
		assertEpsilonEquals(0.f, q.getX());		
		assertEpsilonEquals(0.f, q.getY());
		assertEpsilonEquals(0.f, q.getZ());
		assertEpsilonEquals(1.f, q.getW());

		q = MathUtil.euler2quat((float) Math.toRadians(2.f),0.f,0.f,CoordinateSystem3D.XZY_RIGHT_HAND);
		q.normalize();
		assertEpsilonEquals(0.f, q.getX());		
		assertEpsilonEquals(0.01745240643728351f, q.getY());
		assertEpsilonEquals(0.f, q.getZ());
		assertEpsilonEquals(0.9998476951563913f, q.getW());

		q = MathUtil.euler2quat(0.f,(float) (float) Math.toRadians(2.f),0.f,CoordinateSystem3D.XZY_RIGHT_HAND);
		q.normalize();
		assertEpsilonEquals(0.f, q.getX());		
		assertEpsilonEquals(0.f, q.getY());
		assertEpsilonEquals(0.01745240643728351f, q.getZ());
		assertEpsilonEquals(0.9998476951563913f, q.getW());

		q = MathUtil.euler2quat(0.f,0.f,(float) Math.toRadians(2.f),CoordinateSystem3D.XZY_RIGHT_HAND);
		q.normalize();
		assertEpsilonEquals(0.01745240643728351f, q.getX());		
		assertEpsilonEquals(0.f, q.getY());
		assertEpsilonEquals(0.f, q.getZ());
		assertEpsilonEquals(0.9998476951563913f, q.getW());

		q = MathUtil.euler2quat((float) Math.toRadians(1.f),(float) Math.toRadians(2.f),(float) Math.toRadians(3.f),CoordinateSystem3D.XZY_RIGHT_HAND);
		q.normalize();
		assertEpsilonEquals(0.02632421170091035f, q.getX());		
		assertEpsilonEquals(0.009179049840112f, q.getY());
		assertEpsilonEquals(0.01721736235000767f, q.getZ());
		assertEpsilonEquals(0.999463027508305f, q.getW());

		q = MathUtil.euler2quat((float) Math.toRadians(1.f),(float) Math.toRadians(-2.f),(float) Math.toRadians(3.f),CoordinateSystem3D.XZY_RIGHT_HAND);
		q.normalize();
		assertEpsilonEquals(0.0260197179904538f, q.getX());		
		assertEpsilonEquals(0.008265383148751184f, q.getY());
		assertEpsilonEquals(-0.017674160904072977f, q.getZ());
		assertEpsilonEquals(0.9994710009567254f, q.getW());
	}

	/**
	 */
	public void testEuler2quatFloatFloatFloatCoordinateSystem3D_XYZ_RIGHT_HAND() {
		// Values given by the formular on http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm
		// Be carrefull about the standards used on this website. They are mostly different from our standards. 
		Quaternion q;
		
		q = MathUtil.euler2quat(0.f,0.f,0.f,CoordinateSystem3D.XYZ_RIGHT_HAND);
		q.normalize();
		assertEpsilonEquals(0.f, q.getX());		
		assertEpsilonEquals(0.f, q.getY());
		assertEpsilonEquals(0.f, q.getZ());
		assertEpsilonEquals(1.f, q.getW());

		q = MathUtil.euler2quat((float) Math.toRadians(2.f),0.f,0.f,CoordinateSystem3D.XYZ_RIGHT_HAND);
		q.normalize();
		assertEpsilonEquals(0.f, q.getX());		
		assertEpsilonEquals(0.f, q.getY());
		assertEpsilonEquals(0.01745240643728351f, q.getZ());
		assertEpsilonEquals(0.9998476951563913f, q.getW());

		q = MathUtil.euler2quat(0.f,(float) Math.toRadians(2.f),0.f,CoordinateSystem3D.XYZ_RIGHT_HAND);
		q.normalize();
		assertEpsilonEquals(0.f, q.getX());		
		assertEpsilonEquals(-0.01745240643728351f, q.getY());
		assertEpsilonEquals(0.f, q.getZ());
		assertEpsilonEquals(0.9998476951563913f, q.getW());

		q = MathUtil.euler2quat(0.f,0.f,(float) Math.toRadians(2.f),CoordinateSystem3D.XYZ_RIGHT_HAND);
		q.normalize();
		assertEpsilonEquals(0.01745240643728351f, q.getX());		
		assertEpsilonEquals(0.f, q.getY());
		assertEpsilonEquals(0.f, q.getZ());
		assertEpsilonEquals(0.9998476951563913f, q.getW());

		q = MathUtil.euler2quat((float) Math.toRadians(1.f),(float) Math.toRadians(2.f),(float) Math.toRadians(3.f),CoordinateSystem3D.XYZ_RIGHT_HAND);
		q.normalize();
		assertEpsilonEquals(0.02632421170091035f, q.getX());		
		assertEpsilonEquals(-0.01721736235000767f, q.getY());
		assertEpsilonEquals(0.009179049840112f, q.getZ());
		assertEpsilonEquals(0.999463027508305f, q.getW());

		q = MathUtil.euler2quat((float) Math.toRadians(1.f),(float) Math.toRadians(-2.f),(float) Math.toRadians(3.f),CoordinateSystem3D.XYZ_RIGHT_HAND);
		q.normalize();
		assertEpsilonEquals(0.0260197179904538f, q.getX());		
		assertEpsilonEquals(0.017674160904072977f, q.getY());
		assertEpsilonEquals(0.008265383148751184f, q.getZ());
		assertEpsilonEquals(0.9994710009567254f, q.getW());
	}

	/**
	 */
	public void testEuler2quatEulerAngle() {
		// Values given by the formular on http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm
		// Be carrefull about the standards used on this website. They are mostly different from our standards. 
		Quaternion q;
		
		q = MathUtil.euler2quat(new EulerAngle(0.f,0.f,0.f));
		q.normalize();
		assertEpsilonEquals(0.f, q.getX());		
		assertEpsilonEquals(0.f, q.getY());
		assertEpsilonEquals(0.f, q.getZ());
		assertEpsilonEquals(1.f, q.getW());

		q = MathUtil.euler2quat(new EulerAngle((float) Math.toRadians(2.f),0.f,0.f));
		q.normalize();
		assertEpsilonEquals(0.f, q.getX());		
		assertEpsilonEquals(0.f, q.getY());
		assertEpsilonEquals(0.01745240643728351f, q.getZ());
		assertEpsilonEquals(0.9998476951563913f, q.getW());

		q = MathUtil.euler2quat(new EulerAngle(0.f,(float) Math.toRadians(2.f),0.f));
		q.normalize();
		assertEpsilonEquals(0.f, q.getX());		
		assertEpsilonEquals(-0.01745240643728351f, q.getY());
		assertEpsilonEquals(0.f, q.getZ());
		assertEpsilonEquals(0.9998476951563913f, q.getW());

		q = MathUtil.euler2quat(new EulerAngle(0.f,0.f,(float) Math.toRadians(2.f)));
		q.normalize();
		assertEpsilonEquals(0.01745240643728351f, q.getX());		
		assertEpsilonEquals(0.f, q.getY());
		assertEpsilonEquals(0.f, q.getZ());
		assertEpsilonEquals(0.9998476951563913f, q.getW());

		q = MathUtil.euler2quat(new EulerAngle((float) Math.toRadians(1.f),(float) Math.toRadians(2.f),(float) Math.toRadians(3.f)));
		q.normalize();
		assertEpsilonEquals(0.02632421170091035f, q.getX());		
		assertEpsilonEquals(-0.01721736235000767f, q.getY());
		assertEpsilonEquals(0.009179049840112f, q.getZ());
		assertEpsilonEquals(0.999463027508305f, q.getW());

		q = MathUtil.euler2quat(new EulerAngle((float) Math.toRadians(1.f),(float) Math.toRadians(-2.f),(float) Math.toRadians(3.f)));
		q.normalize();
		assertEpsilonEquals(0.0260197179904538f, q.getX());		
		assertEpsilonEquals(0.017674160904072977f, q.getY());
		assertEpsilonEquals(0.008265383148751184f, q.getZ());
		assertEpsilonEquals(0.9994710009567254f, q.getW());
	}

	/**
	 */
	public void testEuler2quatFloatFloatFloat() {
		// Values given by the formular on http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm
		// Be carrefull about the standards used on this website. They are mostly different from our standards. 
		Quaternion q;
		
		q = MathUtil.euler2quat(0.f,0.f,0.f);
		q.normalize();
		assertEpsilonEquals(0.f, q.getX());		
		assertEpsilonEquals(0.f, q.getY());
		assertEpsilonEquals(0.f, q.getZ());
		assertEpsilonEquals(1.f, q.getW());

		q = MathUtil.euler2quat((float) Math.toRadians(2.f),0.f,0.f);
		q.normalize();
		assertEpsilonEquals(0.f, q.getX());		
		assertEpsilonEquals(0.f, q.getY());
		assertEpsilonEquals(0.01745240643728351f, q.getZ());
		assertEpsilonEquals(0.9998476951563913f, q.getW());

		q = MathUtil.euler2quat(0.f,(float) Math.toRadians(2.f),0.f);
		q.normalize();
		assertEpsilonEquals(0.f, q.getX());		
		assertEpsilonEquals(-0.01745240643728351f, q.getY());
		assertEpsilonEquals(0.f, q.getZ());
		assertEpsilonEquals(0.9998476951563913f, q.getW());

		q = MathUtil.euler2quat(0.f,0.f,(float) Math.toRadians(2.f));
		q.normalize();
		assertEpsilonEquals(0.01745240643728351f, q.getX());		
		assertEpsilonEquals(0.f, q.getY());
		assertEpsilonEquals(0.f, q.getZ());
		assertEpsilonEquals(0.9998476951563913f, q.getW());

		q = MathUtil.euler2quat((float) Math.toRadians(1.f),(float) Math.toRadians(2.f),(float) Math.toRadians(3.f));
		q.normalize();
		assertEpsilonEquals(0.02632421170091035f, q.getX());		
		assertEpsilonEquals(-0.01721736235000767f, q.getY());
		assertEpsilonEquals(0.009179049840112f, q.getZ());
		assertEpsilonEquals(0.999463027508305f, q.getW());

		q = MathUtil.euler2quat((float) Math.toRadians(1.f),(float) Math.toRadians(-2.f),(float) Math.toRadians(3.f));
		q.normalize();
		assertEpsilonEquals(0.0260197179904538f, q.getX());		
		assertEpsilonEquals(0.017674160904072977f, q.getY());
		assertEpsilonEquals(0.008265383148751184f, q.getZ());
		assertEpsilonEquals(0.9994710009567254f, q.getW());
	}

	/**
	 */
	public void testQuat2eulerQuaternionEulerAngleCoordinateSystem3D_XZY_RIGHT_HAND() {
		// Values given by the formular on http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
		// Be carrefull about the standards used on this website. They are mostly different from our standards. 
		EulerAngle ea = new EulerAngle();
		
		MathUtil.quat2euler(new Quaternion(0.f,0.f,0.f,1),ea,CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(0.f, ea.heading);		
		assertEpsilonEquals(0.f, ea.attitude);
		assertEpsilonEquals(0.f, ea.bank);

		MathUtil.quat2euler(new Quaternion(0.f,0.01745240643728351f,0.f,0.9998476951563913f),ea,CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.heading);
		assertEpsilonEquals(0.f, ea.attitude);
		assertEpsilonEquals(0.f, ea.bank);

		MathUtil.quat2euler(new Quaternion(0.f,0.f,0.01745240643728351f,0.9998476951563913f),ea,CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(0.f, ea.heading);		
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.attitude);
		assertEpsilonEquals(0.f, ea.bank);

		MathUtil.quat2euler(new Quaternion(0.01745240643728351f,0.f,0.f,0.9998476951563913f),ea,CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(0.f, ea.heading);		
		assertEpsilonEquals(0.f, ea.attitude);
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.bank);

		MathUtil.quat2euler(new Quaternion(0.02632421170091035f,0.009179049840112f,0.01721736235000767f,0.999463027508305f),ea,CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals((float) Math.toRadians(1.f), ea.heading);		
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.attitude);
		assertEpsilonEquals((float) Math.toRadians(3.f), ea.bank);

		MathUtil.quat2euler(new Quaternion(0.0260197179904538f,0.008265383148751184f,-0.017674160904072977f,0.9994710009567254f),ea,CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals((float) Math.toRadians(1.f), ea.heading);		
		assertEpsilonEquals((float) Math.toRadians(-2.f), ea.attitude);
		assertEpsilonEquals((float) Math.toRadians(3.f), ea.bank);
	}

	/**
	 */
	public void testQuat2eulerQuaternionEulerAngleCoordinateSystem3D_XYZ_RIGHT_HAND() {
		// Values given by the formular on http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
		// Be carrefull about the standards used on this website. They are mostly different from our standards. 
		EulerAngle ea = new EulerAngle();
		
		MathUtil.quat2euler(new Quaternion(0.f,0.f,0.f,1.f),ea,CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(0.f, ea.heading);		
		assertEpsilonEquals(0.f, ea.attitude);
		assertEpsilonEquals(0.f, ea.bank);

		MathUtil.quat2euler(new Quaternion(0.f,0.f,0.01745240643728351f,0.9998476951563913f),ea,CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.heading);		
		assertEpsilonEquals(0.f, ea.attitude);
		assertEpsilonEquals(0.f, ea.bank);

		MathUtil.quat2euler(new Quaternion(0.f,-0.01745240643728351f,0.f,0.9998476951563913f),ea,CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(0.f, ea.heading);		
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.attitude);
		assertEpsilonEquals(0.f, ea.bank);

		MathUtil.quat2euler(new Quaternion(0.01745240643728351f,0.f,0.f,0.9998476951563913f),ea,CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(0.f, ea.heading);		
		assertEpsilonEquals(0.f, ea.attitude);
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.bank);

		MathUtil.quat2euler(new Quaternion(0.02632421170091035f,-0.01721736235000767f,0.009179049840112f,0.999463027508305f),ea,CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals((float) Math.toRadians(1.f), ea.heading);		
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.attitude);
		assertEpsilonEquals((float) Math.toRadians(3.f), ea.bank);

		MathUtil.quat2euler(new Quaternion(00.0260197179904538f,0.017674160904072977f,0.008265383148751184f,0.9994710009567254f),ea,CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals((float) Math.toRadians(1.f), ea.heading);		
		assertEpsilonEquals((float) Math.toRadians(-2.f), ea.attitude);
		assertEpsilonEquals((float) Math.toRadians(3.f), ea.bank);
	}

	/**
	 */
	public void testQuat2eulerQuaternionEulerAngle() {
		// Values given by the formular on http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
		// Be carrefull about the standards used on this website. They are mostly different from our standards. 
		EulerAngle ea = new EulerAngle();
		
		MathUtil.quat2euler(new Quaternion(0.f,0.f,0.f,1.f),ea);
		assertEpsilonEquals(0.f, ea.heading);		
		assertEpsilonEquals(0.f, ea.attitude);
		assertEpsilonEquals(0.f, ea.bank);

		MathUtil.quat2euler(new Quaternion(0.f,0.f,0.01745240643728351f,0.9998476951563913f),ea);
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.heading);		
		assertEpsilonEquals(0.f, ea.attitude);
		assertEpsilonEquals(0.f, ea.bank);

		MathUtil.quat2euler(new Quaternion(0.f,-0.01745240643728351f,0.f,0.9998476951563913f),ea);
		assertEpsilonEquals(0.f, ea.heading);		
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.attitude);
		assertEpsilonEquals(0.f, ea.bank);

		MathUtil.quat2euler(new Quaternion(0.01745240643728351f,0.f,0.f,0.9998476951563913f),ea);
		assertEpsilonEquals(0.f, ea.heading);		
		assertEpsilonEquals(0.f, ea.attitude);
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.bank);

		MathUtil.quat2euler(new Quaternion(0.02632421170091035f,-0.01721736235000767f,0.009179049840112f,0.999463027508305f),ea);
		assertEpsilonEquals((float) Math.toRadians(1.f), ea.heading);		
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.attitude);
		assertEpsilonEquals((float) Math.toRadians(3.f), ea.bank);

		MathUtil.quat2euler(new Quaternion(00.0260197179904538f,0.017674160904072977f,0.008265383148751184f,0.9994710009567254f),ea);
		assertEpsilonEquals((float) Math.toRadians(1.f), ea.heading);		
		assertEpsilonEquals((float) Math.toRadians(-2.f), ea.attitude);
		assertEpsilonEquals((float) Math.toRadians(3.f), ea.bank);
	}

	/**
	 */
	public void testQuat2eulerQuaternion() {
		// Values given by the formular on http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
		// Be carrefull about the standards used on this website. They are mostly different from our standards. 
		EulerAngle ea;
		
		ea = MathUtil.quat2euler(new Quaternion(0.f,0.f,0.f,1.f));
		assertNotNull(ea);
		assertEpsilonEquals(0.f, ea.heading);		
		assertEpsilonEquals(0.f, ea.attitude);
		assertEpsilonEquals(0.f, ea.bank);

		ea = MathUtil.quat2euler(new Quaternion(0.f,0.f,0.01745240643728351f,0.9998476951563913f));
		assertNotNull(ea);
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.heading);		
		assertEpsilonEquals(0.f, ea.attitude);
		assertEpsilonEquals(0.f, ea.bank);

		ea = MathUtil.quat2euler(new Quaternion(0.f,-0.01745240643728351f,0.f,0.9998476951563913f));
		assertNotNull(ea);
		assertEpsilonEquals(0.f, ea.heading);		
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.attitude);
		assertEpsilonEquals(0.f, ea.bank);

		ea = MathUtil.quat2euler(new Quaternion(0.01745240643728351f,0.f,0.f,0.9998476951563913f));
		assertNotNull(ea);
		assertEpsilonEquals(0.f, ea.heading);		
		assertEpsilonEquals(0.f, ea.attitude);
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.bank);

		ea = MathUtil.quat2euler(new Quaternion(0.02632421170091035f,-0.01721736235000767f,0.009179049840112f,0.999463027508305f));
		assertNotNull(ea);
		assertEpsilonEquals((float) Math.toRadians(1.f), ea.heading);		
		assertEpsilonEquals((float) Math.toRadians(2.f), ea.attitude);
		assertEpsilonEquals((float) Math.toRadians(3.f), ea.bank);

		ea = MathUtil.quat2euler(new Quaternion(00.0260197179904538f,0.017674160904072977f,0.008265383148751184f,0.9994710009567254f));
		assertNotNull(ea);
		assertEpsilonEquals((float) Math.toRadians(1.f), ea.heading);		
		assertEpsilonEquals((float) Math.toRadians(-2.f), ea.attitude);
		assertEpsilonEquals((float) Math.toRadians(3.f), ea.bank);
	}

	/**
	 */
	public void testRotation2viewVector1DQuaternionCoordinateSystem3D_XYZ_RIGHT_HAND() {
		Quaternion q;
		AxisAngle4f aa;
		float viewVector, angle;
		
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		// Around 0Z
		aa = new AxisAngle4f(0.f,0.f,1.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(MathConstants.DEMI_PI, viewVector);

		aa = new AxisAngle4f(0.f,0.f,1.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(-MathConstants.DEMI_PI, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,0.f,1.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(MathUtil.clampRadianMinusPIToPI(angle), MathUtil.clampRadianMinusPIToPI(viewVector));

		// Around 0Y
		aa = new AxisAngle4f(0.f,1.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		aa = new AxisAngle4f(0.f,1.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,1.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		angle = MathUtil.clampRadianMinusPIToPI(angle);
		if (angle<-MathConstants.DEMI_PI || angle>MathConstants.DEMI_PI)
			assertEpsilonEquals(MathConstants.PI, MathUtil.clampRadianMinusPIToPI(viewVector));
		else
			assertEpsilonEquals(0.f, MathUtil.clampRadianMinusPIToPI(viewVector));

		// Around 0X
		aa = new AxisAngle4f(1.f,0.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		aa = new AxisAngle4f(1.f,0.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(1.f,0.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(0.f, viewVector);
	}
	
	/**
	 */
	public void testRotation2viewVector1DQuaternionCoordinateSystem3D_XZY_RIGHT_HAND() {
		Quaternion q;
		AxisAngle4f aa;
		float viewVector, angle;
		
		// Around 0Y
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		aa = new AxisAngle4f(0.f,1.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(-MathConstants.DEMI_PI, viewVector);

		aa = new AxisAngle4f(0.f,1.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(MathConstants.DEMI_PI, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,1.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(MathUtil.clampRadianMinusPIToPI(-angle), MathUtil.clampRadianMinusPIToPI(viewVector));

		// Around 0Z
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		aa = new AxisAngle4f(0.f,0.f,1.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		aa = new AxisAngle4f(0.f,0.f,1.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,0.f,1.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		angle = MathUtil.clampRadianMinusPIToPI(angle);
		if (angle<-MathConstants.DEMI_PI || angle>MathConstants.DEMI_PI)
			assertEpsilonEquals(MathConstants.PI, MathUtil.clampRadianMinusPIToPI(viewVector));
		else
			assertEpsilonEquals(0.f, MathUtil.clampRadianMinusPIToPI(viewVector));

		// Around 0X
		aa = new AxisAngle4f(1.f,0.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		aa = new AxisAngle4f(1.f,0.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(1.f,0.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(0.f, viewVector);
	}

	/**
	 */
	public void testRotation2viewVector1DQuaternionCoordinateSystem3D_XYZ_LEFT_HAND() {
		Quaternion q;
		AxisAngle4f aa;
		float viewVector, angle;

		// Around 0Z
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		aa = new AxisAngle4f(0.f,0.f,1.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(MathConstants.DEMI_PI, viewVector);

		aa = new AxisAngle4f(0.f,0.f,1.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(-MathConstants.DEMI_PI, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,0.f,1.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(MathUtil.clampRadianMinusPIToPI(angle), MathUtil.clampRadianMinusPIToPI(viewVector));

		// Around 0Y
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		aa = new AxisAngle4f(0.f,1.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		aa = new AxisAngle4f(0.f,1.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,1.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		angle = MathUtil.clampRadianMinusPIToPI(angle);
		if (angle<-MathConstants.DEMI_PI || angle>MathConstants.DEMI_PI)
			assertEpsilonEquals(MathConstants.PI, MathUtil.clampRadianMinusPIToPI(viewVector));
		else
			assertEpsilonEquals(0.f, MathUtil.clampRadianMinusPIToPI(viewVector));

		// Around 0X
		aa = new AxisAngle4f(1.f,0.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		aa = new AxisAngle4f(1.f,0.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(1.f,0.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(0.f, viewVector);
	}

	/**
	 */
	public void testRotation2viewVector1DQuaternionCoordinateSystem3D_XZY_LEFT_HAND() {
		Quaternion q;
		AxisAngle4f aa;
		float viewVector, angle;
		
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		// Around OY
		aa = new AxisAngle4f(0.f,1.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(-MathConstants.DEMI_PI, viewVector);

		aa = new AxisAngle4f(0.f,1.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(MathConstants.DEMI_PI, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,1.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(MathUtil.clampRadianMinusPIToPI(-angle), MathUtil.clampRadianMinusPIToPI(viewVector));

		// Around 0Z
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		aa = new AxisAngle4f(0.f,0.f,1.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		aa = new AxisAngle4f(0.f,0.f,1.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,0.f,1.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		angle = MathUtil.clampRadianMinusPIToPI(angle);
		if (angle<-MathConstants.DEMI_PI || angle>MathConstants.DEMI_PI)
			assertEpsilonEquals(MathConstants.PI, MathUtil.clampRadianMinusPIToPI(viewVector));
		else
			assertEpsilonEquals(0.f, MathUtil.clampRadianMinusPIToPI(viewVector));

		// Around 0X
		aa = new AxisAngle4f(1.f,0.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		aa = new AxisAngle4f(1.f,0.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(0.f, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(1.f,0.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector1D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(0.f, viewVector);
	}

	/**
	 */
	public void testRotation2viewVector2DQuaternionDCoordinateSystem3D_XYZ_RIGHT_HAND() {
		Quaternion q;
		AxisAngle4f aa;
		Vector2f viewVector;
		Vector2f originalViewVector, originalBackVector;
		float angle;
		
		originalViewVector = CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(CoordinateSystem3D.XYZ_RIGHT_HAND.getViewVector());
		originalBackVector = new Vector2f();
		originalBackVector.negate(originalViewVector);
		
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		// Around 0Z
		aa = new AxisAngle4f(0.f,0.f,1.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(new Vector2f(0.f,1.f), viewVector);

		aa = new AxisAngle4f(0.f,0.f,1.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(new Vector2f(0.f,-1.f), viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,0.f,1.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(
				new Vector2f(Math.cos(angle), Math.sin(angle)), viewVector);

		// Around 0Y
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		aa = new AxisAngle4f(0.f,1.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		aa = new AxisAngle4f(0.f,1.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,1.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		angle = MathUtil.clampRadianMinusPIToPI(angle);
		if (angle<-MathConstants.DEMI_PI || angle>MathConstants.DEMI_PI)
			assertEpsilonEquals(originalBackVector, viewVector);
		else
			assertEpsilonEquals(originalViewVector, viewVector);

		// Around 0X
		aa = new AxisAngle4f(1.f,0.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		aa = new AxisAngle4f(1.f,0.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(1.f,0.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);
	}

	/**
	 */
	public void testRotation2viewVector2DQuaternionDCoordinateSystem3D_XZY_RIGHT_HAND() {
		Quaternion q;
		AxisAngle4f aa;
		Vector2f viewVector;
		Vector2f originalViewVector, originalBackVector;
		float angle;
		
		originalViewVector = CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(CoordinateSystem3D.XZY_RIGHT_HAND.getViewVector());
		originalBackVector = new Vector2f();
		originalBackVector.negate(originalViewVector);
		
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		// Around 0Y
		aa = new AxisAngle4f(0.f,1.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(new Vector2f(0.f,-1.f), viewVector);

		aa = new AxisAngle4f(0.f,1.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(new Vector2f(0.f,1.f), viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,1.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(
				new Vector2f(Math.cos(-angle), Math.sin(-angle)), viewVector);

		// Around 0Z
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		aa = new AxisAngle4f(0.f,0.f,1.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		aa = new AxisAngle4f(0.f,0.f,1.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,0.f,1.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		angle = MathUtil.clampRadianMinusPIToPI(angle);
		if (angle<-MathConstants.DEMI_PI || angle>MathConstants.DEMI_PI)
			assertEpsilonEquals(originalBackVector, viewVector);
		else
			assertEpsilonEquals(originalViewVector, viewVector);

		// Around 0X
		aa = new AxisAngle4f(1.f,0.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		aa = new AxisAngle4f(1.f,0.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(1.f,0.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);
	}

	/**
	 */
	public void testRotation2viewVector2DQuaternionDCoordinateSystem3D_XYZ_LEFT_HAND() {
		Quaternion q;
		AxisAngle4f aa;
		Vector2f viewVector;
		Vector2f originalViewVector, originalBackVector;
		float angle;
		
		originalViewVector = CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(CoordinateSystem3D.XYZ_LEFT_HAND.getViewVector());
		originalBackVector = new Vector2f();
		originalBackVector.negate(originalViewVector);
		
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		// Around 0Z
		aa = new AxisAngle4f(0.f,0.f,1.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(new Vector2f(0.f,1.f), viewVector);

		aa = new AxisAngle4f(0.f,0.f,1.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(new Vector2f(0.f,-1.f), viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,0.f,1.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(
				new Vector2f(Math.cos(angle), Math.sin(angle)), viewVector);

		// Around 0Y
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		aa = new AxisAngle4f(0.f,1.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		aa = new AxisAngle4f(0.f,1.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,1.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		angle = MathUtil.clampRadianMinusPIToPI(angle);
		if (angle<-MathConstants.DEMI_PI || angle>MathConstants.DEMI_PI)
			assertEpsilonEquals(originalBackVector, viewVector);
		else
			assertEpsilonEquals(originalViewVector, viewVector);

		// Around 0X
		aa = new AxisAngle4f(1.f,0.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		aa = new AxisAngle4f(1.f,0.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(1.f,0.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);
	}

	/**
	 */
	public void testRotation2viewVector2DQuaternionDCoordinateSystem3D_XZY_LEFT_HAND() {
		Quaternion q;
		AxisAngle4f aa;
		Vector2f viewVector;
		Vector2f originalViewVector, originalBackVector;
		float angle;
		
		originalViewVector = CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(CoordinateSystem3D.XZY_LEFT_HAND.getViewVector());
		originalBackVector = new Vector2f();
		originalBackVector.negate(originalViewVector);
		
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		// Around 0Y
		aa = new AxisAngle4f(0.f,1.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(new Vector2f(0.f,-1.f), viewVector);

		aa = new AxisAngle4f(0.f,1.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(new Vector2f(0.f,1.f), viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,1.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(
				new Vector2f(Math.cos(-angle), Math.sin(-angle)), viewVector);

		// Around 0Z
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		aa = new AxisAngle4f(0.f,0.f,1.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		aa = new AxisAngle4f(0.f,0.f,1.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,0.f,1.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		angle = MathUtil.clampRadianMinusPIToPI(angle);
		if (angle<-MathConstants.DEMI_PI || angle>MathConstants.DEMI_PI)
			assertEpsilonEquals(originalBackVector, viewVector);
		else
			assertEpsilonEquals(originalViewVector, viewVector);

		// Around 0X
		aa = new AxisAngle4f(1.f,0.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		aa = new AxisAngle4f(1.f,0.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(1.f,0.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector2D(q, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(originalViewVector, viewVector);
	}

	/**
	 */
	public void testRotation2viewVector3DQuaternionDCoordinateSystem3D_XYZ_RIGHT_HAND() {
		Quaternion q;
		AxisAngle4f aa;
		AxisAngle4f viewVector, originalView;
		Vector3f originalViewVector;
		float angle;
		
		originalViewVector = CoordinateSystem3D.XYZ_RIGHT_HAND.getViewVector();
		originalView = new AxisAngle4f(originalViewVector, 0.f);
		
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector3D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		viewVector.angle = MathUtil.clampRadianMinusPIToPI(viewVector.angle);
		assertEpsilonEquals(originalView, viewVector);

		// Around 0Z
		aa = new AxisAngle4f(0.f,0.f,1.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector3D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		viewVector.angle = MathUtil.clampRadianMinusPIToPI(viewVector.angle);
		assertEpsilonEquals(new AxisAngle4f(0.f,1.f,0.f,0.f), viewVector);

		aa = new AxisAngle4f(0.f,0.f,1.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector3D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		viewVector.angle = MathUtil.clampRadianMinusPIToPI(viewVector.angle);
		assertEpsilonEquals(new AxisAngle4f(0.f,-1.f,0.f,0.f), viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(0.f,0.f,1.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector3D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		viewVector.angle = MathUtil.clampRadianMinusPIToPI(viewVector.angle);
		assertEpsilonEquals(
				new AxisAngle4f((float) Math.cos(angle), (float) Math.sin(angle), 0.f, 0.f), viewVector);

		// Around 0X
		aa = new AxisAngle4f(1.f,0.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector3D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		viewVector.angle = MathUtil.clampRadianMinusPIToPI(viewVector.angle);
		assertEpsilonEquals(new AxisAngle4f(originalViewVector, -MathConstants.DEMI_PI), viewVector);

		aa = new AxisAngle4f(1.f,0.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector3D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		viewVector.angle = MathUtil.clampRadianMinusPIToPI(viewVector.angle);
		assertEpsilonEquals(new AxisAngle4f(originalViewVector, MathConstants.DEMI_PI), viewVector);

		angle = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new AxisAngle4f(1.f,0.f,0.f,angle);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector3D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		viewVector.angle = MathUtil.clampRadianMinusPIToPI(viewVector.angle);
		assertEpsilonEquals(new AxisAngle4f(originalViewVector, 
				MathUtil.clampRadianMinusPIToPI(-angle)), viewVector);

		// Around 0Y
		q = new Quaternion();
		viewVector = MathUtil.rotation2viewVector3D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		viewVector.angle = MathUtil.clampRadianMinusPIToPI(viewVector.angle);
		assertEpsilonEquals(originalView, viewVector);

		aa = new AxisAngle4f(0.f,1.f,0.f,MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector3D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		viewVector.angle = MathUtil.clampRadianMinusPIToPI(viewVector.angle);
		assertEpsilonEquals(new AxisAngle4f(0.f, 0.f, -1.f, 0.f), viewVector);

		aa = new AxisAngle4f(0.f,1.f,0.f,-MathConstants.DEMI_PI);
		q = new Quaternion();
		q.set(aa);
		viewVector = MathUtil.rotation2viewVector3D(q, CoordinateSystem3D.XYZ_RIGHT_HAND);
		viewVector.angle = MathUtil.clampRadianMinusPIToPI(viewVector.angle);
		assertEpsilonEquals(new AxisAngle4f(0.f, 0.f, 1.f, 0.f), viewVector);
	}
	
	private void rotateForLookAt(Matrix4f mat, Vector3f expectedView, Vector3f expectedUp, CoordinateSystem3D system) {
		Vector3f actualView = new Vector3f();
		system.getViewVector(actualView);		
		mat.transform(actualView);
		actualView.normalize();

		expectedView.normalize();
		assertEpsilonEquals(expectedView, actualView);

		Vector3f actualUp = new Vector3f();
		system.getUpVector(actualUp);		
		mat.transform(actualUp);
		actualUp.normalize();
		
		expectedUp.normalize();
		assertEpsilonEquals(expectedUp, actualUp);
	}
	
	/**
	 */
	public void testLookAtVector3dVector3dCoordinateSystem3D_XYZ_RIGHT_HAND() {
		Matrix4f mat;
		CoordinateSystem3D system = CoordinateSystem3D.XYZ_RIGHT_HAND;
		float angle;
		
		// No rotation
		mat = MathUtil.lookAt(system.getViewVector(), system.getUpVector(), system);
		rotateForLookAt(mat, system.getViewVector(), system.getUpVector(), system);

		// Rotation of PI/2 around the up vector
		mat = MathUtil.lookAt(new Vector3f(0.f,1.f,0.f), system.getUpVector(), system);
		rotateForLookAt(mat, new Vector3f(0.f,1.f,0.f), system.getUpVector(), system);

		// Rotation of -PI/2 around the up vector
		mat = MathUtil.lookAt(new Vector3f(0.f,-1.f,0.f), system.getUpVector(), system);
		rotateForLookAt(mat, new Vector3f(0.f,-1.f,0.f), system.getUpVector(), system);

		// Rotation of around the up vector
		angle = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		mat = MathUtil.lookAt(new Vector3f(Math.cos(angle),Math.sin(angle),0.f), system.getUpVector(), system);
		rotateForLookAt(mat, new Vector3f(Math.cos(angle),Math.sin(angle),0.f), system.getUpVector(), system);

		// Rotation of PI/2 around the left vector
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,-1.f), system.getViewVector(), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,-1.f), system.getViewVector(), system);
		
		// Rotation of -PI/2 around the left vector
		Vector3f vv = system.getViewVector();
		vv.negate();
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,1.f), vv, system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,1.f), vv, system);

		// Rotation of PI/2 around the front vector
		mat = MathUtil.lookAt(system.getViewVector(), new Vector3f(0.f,0.f,-1.f), system);
		rotateForLookAt(mat, system.getViewVector(), new Vector3f(0.f,0.f,-1.f), system);

		// View vector and up vector are same
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,-1.f), new Vector3f(0.f,0.f,1.f), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,-1.f), new Vector3f(-1.f,0.f,0.f), system);

		mat = MathUtil.lookAt(new Vector3f(1.f,0.f,1.f), new Vector3f(1.f,0.f,1.f), system);
		rotateForLookAt(mat, new Vector3f(1.f,0.f,1.f), new Vector3f(0.f,-1.f,0.f), system);

		// View vector and up vector are opposite
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,-1.f), new Vector3f(0.f,0.f,1.f), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,-1.f), new Vector3f(-1.f,0.f,0.f), system);

		mat = MathUtil.lookAt(new Vector3f(1.f,0.f,1.f), new Vector3f(-1.f,0.f,-1.f), system);
		rotateForLookAt(mat, new Vector3f(1.f,0.f,1.f), new Vector3f(0.f,-1.f,0.f), system);
	}

	/**
	 */
	public void testLookAtVector3dVector3dCoordinateSystem3D_XZY_RIGHT_HAND() {
		Matrix4f mat;
		CoordinateSystem3D system = CoordinateSystem3D.XZY_RIGHT_HAND;
		float angle;
		
		// No rotation
		mat = MathUtil.lookAt(system.getViewVector(), system.getUpVector(), system);
		rotateForLookAt(mat, system.getViewVector(), system.getUpVector(), system);

		// Rotation of PI/2 around the up vector
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,-1.f), system.getUpVector(), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,-1.f), system.getUpVector(), system);

		// Rotation of -PI/2 around the up vector
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,1.f), system.getUpVector(), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,1.f), system.getUpVector(), system);

		// Rotation of around the up vector
		angle = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		mat = MathUtil.lookAt(new Vector3f(Math.cos(angle),0.f,Math.sin(angle)), system.getUpVector(), system);
		rotateForLookAt(mat, new Vector3f(Math.cos(angle),0.f,Math.sin(angle)), system.getUpVector(), system);

		// Rotation of PI/2 around the right vector
		mat = MathUtil.lookAt(new Vector3f(0.f,1.f,0.f), new Vector3f(-1.f,0.f,0.f), system);
		rotateForLookAt(mat, new Vector3f(0.f,1.f,0.f), new Vector3f(-1.f,0.f,0.f), system);
		
		// Rotation of -PI/2 around the right vector
		mat = MathUtil.lookAt(new Vector3f(0.f,-1.f,0.f), new Vector3f(1.f,0.f,0.f), system);
		rotateForLookAt(mat, new Vector3f(0.f,-1.f,0.f), new Vector3f(1.f,0.f,0.f), system);

		// Rotation of PI/2 around the front vector
		mat = MathUtil.lookAt(system.getViewVector(), new Vector3f(0.f,-1.f,0.f), system);
		rotateForLookAt(mat, system.getViewVector(), new Vector3f(0.f,-1.f,0.f), system);

		// View vector and up vector are same
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,1.f), new Vector3f(0.f,0.f,1.f), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,1.f), new Vector3f(-1.f,0.f,0.f), system);

		mat = MathUtil.lookAt(new Vector3f(1.f,0.f,1.f), new Vector3f(1.f,0.f,1.f), system);
		rotateForLookAt(mat, new Vector3f(1.f,0.f,1.f), new Vector3f(0.f,-1.f,0.f), system);

		// View vector and up vector are opposite
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,-1.f), new Vector3f(0.f,0.f,1.f), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,-1.f), new Vector3f(-1.f,0.f,0.f), system);

		mat = MathUtil.lookAt(new Vector3f(1.f,0.f,1.f), new Vector3f(-1.f,0.f,-1.f), system);
		rotateForLookAt(mat, new Vector3f(1.f,0.f,1.f), new Vector3f(0.f,-1.f,0.f), system);
	}

	/**
	 */
	public void testLookAtVector3dVector3dCoordinateSystem3D_XYZ_LEFT_HAND() {
		Matrix4f mat;
		CoordinateSystem3D system = CoordinateSystem3D.XYZ_LEFT_HAND;
		float angle;
		
		// No rotation
		mat = MathUtil.lookAt(system.getViewVector(), system.getUpVector(), system);
		rotateForLookAt(mat, system.getViewVector(), system.getUpVector(), system);

		// Rotation of PI/2 around the up vector
		mat = MathUtil.lookAt(new Vector3f(0.f,1.f,0.f), system.getUpVector(), system);
		rotateForLookAt(mat, new Vector3f(0.f,1.f,0.f), system.getUpVector(), system);

		// Rotation of -PI/2 around the up vector
		mat = MathUtil.lookAt(new Vector3f(0.f,-1.f,0.f), system.getUpVector(), system);
		rotateForLookAt(mat, new Vector3f(0.f,-1.f,0.f), system.getUpVector(), system);

		// Rotation of around the up vector
		angle = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		mat = MathUtil.lookAt(new Vector3f(Math.cos(-angle),Math.sin(-angle),0.f), system.getUpVector(), system);
		rotateForLookAt(mat, new Vector3f(Math.cos(-angle),Math.sin(-angle),0.f), system.getUpVector(), system);

		// Rotation of PI/2 around the right vector
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,1.f), new Vector3f(-1.f,0.f,0.f), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,1.f), new Vector3f(-1.f,0.f,0.f), system);
		
		// Rotation of -PI/2 around the right vector
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,-1.f), new Vector3f(1.f,0.f,0.f), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,-1.f), new Vector3f(1.f,0.f,0.f), system);

		// Rotation of PI/2 around the front vector
		mat = MathUtil.lookAt(system.getViewVector(), new Vector3f(0.f,1.f,0.f), system);
		rotateForLookAt(mat, system.getViewVector(), new Vector3f(0.f,1.f,0.f), system);

		// View vector and up vector are same
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,1.f), new Vector3f(0.f,0.f,1.f), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,1.f), new Vector3f(1.f,0.f,0.f), system);

		mat = MathUtil.lookAt(new Vector3f(1.f,0.f,1.f), new Vector3f(1.f,0.f,1.f), system);
		rotateForLookAt(mat, new Vector3f(1.f,0.f,1.f), new Vector3f(0.f,1.f,0.f), system);

		// View vector and up vector are opposite
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,-1.f), new Vector3f(0.f,0.f,1.f), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,-1.f), new Vector3f(1.f,0.f,0.f), system);

		mat = MathUtil.lookAt(new Vector3f(1.f,0.f,1.f), new Vector3f(-1.f,0.f,-1.f), system);
		rotateForLookAt(mat, new Vector3f(1.f,0.f,1.f), new Vector3f(0.f,1.f,0.f), system);
	}

	/**
	 */
	public void testLookAtVector3dVector3dCoordinateSystem3D_XZY_LEFT_HAND() {
		Matrix4f mat;
		CoordinateSystem3D system = CoordinateSystem3D.XZY_LEFT_HAND;
		float angle;
		
		// No rotation
		mat = MathUtil.lookAt(system.getViewVector(), system.getUpVector(), system);
		rotateForLookAt(mat, system.getViewVector(), system.getUpVector(), system);

		// Rotation of PI/2 around the up vector
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,1.f), system.getUpVector(), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,1.f), system.getUpVector(), system);

		// Rotation of -PI/2 around the up vector
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,-1.f), system.getUpVector(), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,-1.f), system.getUpVector(), system);

		// Rotation of around the up vector
		angle = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		mat = MathUtil.lookAt(new Vector3f(Math.cos(-angle),0.f,Math.sin(-angle)), system.getUpVector(), system);
		rotateForLookAt(mat, new Vector3f(Math.cos(-angle),0.f,Math.sin(-angle)), system.getUpVector(), system);

		// Rotation of PI/2 around the left vector
		mat = MathUtil.lookAt(new Vector3f(0.f,-1.f,0.f), new Vector3f(1.f,0.f,0.f), system);
		rotateForLookAt(mat, new Vector3f(0.f,-1.f,0.f), new Vector3f(1.f,0.f,0.f), system);
		
		// Rotation of -PI/2 around the left vector
		mat = MathUtil.lookAt(new Vector3f(0.f,1.f,0.f), new Vector3f(-1.f,0.f,0.f), system);
		rotateForLookAt(mat, new Vector3f(0.f,1.f,0.f), new Vector3f(-1.f,0.f,0.f), system);

		// Rotation of PI/2 around the front vector
		mat = MathUtil.lookAt(system.getViewVector(), new Vector3f(0.f,0.f,-1.f), system);
		rotateForLookAt(mat, system.getViewVector(), new Vector3f(0.f,0.f,-1.f), system);

		// View vector and up vector are same
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,1.f), new Vector3f(0.f,0.f,1.f), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,1.f), new Vector3f(1.f,0.f,0.f), system);

		mat = MathUtil.lookAt(new Vector3f(1.f,0.f,1.f), new Vector3f(1.f,0.f,1.f), system);
		rotateForLookAt(mat, new Vector3f(1.f,0.f,1.f), new Vector3f(0.f,1.f,0.f), system);

		// View vector and up vector are opposite
		mat = MathUtil.lookAt(new Vector3f(0.f,0.f,-1.f), new Vector3f(0.f,0.f,1.f), system);
		rotateForLookAt(mat, new Vector3f(0.f,0.f,-1.f), new Vector3f(1.f,0.f,0.f), system);

		mat = MathUtil.lookAt(new Vector3f(1.f,0.f,1.f), new Vector3f(-1.f,0.f,-1.f), system);
		rotateForLookAt(mat, new Vector3f(1.f,0.f,1.f), new Vector3f(0.f,1.f,0.f), system);
	}

	/**
	 */
	public void testLookAtDirection3DCoordinateSystem3D() {
		Matrix4f mat;
		CoordinateSystem3D system = CoordinateSystem3D.XYZ_RIGHT_HAND;
		Direction3D aa;
		float angle;
		
		// No rotation
		aa = new Direction3D(1.f,0.f,0.f,0.f);
		mat = MathUtil.lookAt(aa, system);
		rotateForLookAt(mat, system.getViewVector(), system.getUpVector(), system);

		// Rotation of PI/2 around the up vector
		aa = new Direction3D(0.f,1.f,0.f,0.f);
		mat = MathUtil.lookAt(aa, system);
		rotateForLookAt(mat, new Vector3f(0.f,1.f,0.f), system.getUpVector(), system);

		// Rotation of -PI/2 around the up vector
		aa = new Direction3D(0.f,-1.f,0.f,0.f);
		mat = MathUtil.lookAt(aa, system);
		rotateForLookAt(mat, new Vector3f(0.f,-1.f,0.f), system.getUpVector(), system);

		// Rotation of around the up vector
		angle = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new Direction3D((float) Math.cos(angle), (float) Math.sin(angle),0.f,0.f);
		mat = MathUtil.lookAt(aa, system);
		rotateForLookAt(mat, new Vector3f(Math.cos(angle),Math.sin(angle),0.f), system.getUpVector(), system);

		// Rotation of around the up vector with rotation around view vector.
		angle = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * MathConstants.TWO_PI;
		aa = new Direction3D(0.f,1.f,0.f,angle);
		mat = MathUtil.lookAt(aa, system);
		Vector3f up = new Vector3f(0.f,0.f,1.f);
		Matrix4f mm = new Matrix4f();
		mm.set(new AxisAngle4f(0.f,1.f,0.f,angle));
		mm.transform(up);
		rotateForLookAt(mat, new Vector3f(0.f,1.f,0.f), up, system);
	}

	/**
	 */
	public void testRotateAroundQuaternionPoint3d_Origin() {
		Point3f refPoint = randomPoint3D();
		Quaternion rotation = randomQuaternion();
		
		Matrix4f mat = MathUtil.rotateAround(rotation, new Point3f());
		
		Point3f expectedPoint = new Point3f(refPoint);
		Matrix4f refMatrix = new Matrix4f();
		refMatrix.set(rotation);
		refMatrix.transform(expectedPoint);
		
		Point3f actualPoint = new Point3f(refPoint);
		mat.transform(actualPoint);
		
		assertNotNull(mat);
		assertEpsilonEquals(expectedPoint, actualPoint);
	}

	/**
	 */
	public void testRotateAroundQuaternionPoint3d_Point() {
		Point3f refPoint = randomPoint3D();
		Point3f pivot = randomPoint3D();
		Quaternion rotation = randomQuaternion();
		
		Matrix4f mat = MathUtil.rotateAround(rotation, pivot);
		
		assertNotNull(mat);

		Point3f expectedPoint = new Point3f(refPoint);
		expectedPoint.sub(pivot);
		Matrix4f refMatrix = new Matrix4f();
		refMatrix.set(rotation);
		refMatrix.transform(expectedPoint);
		expectedPoint.add(pivot);
	
		Point3f actualPoint = new Point3f(refPoint);
		mat.transform(actualPoint);

		assertEpsilonEquals(expectedPoint, actualPoint);
	}
	
	/**
	 */
	public void testIsParallelLinesPoint2dPoint2dPoint2dPoint2d() {
		Point2f p1 = new Point2f(-100.f,-100.f);
		Point2f p2 = new Point2f(100.f,100.f);
		Point2f p3, p4;
		
		p3 = new Point2f(p1);
		p4 = new Point2f(p2);
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p3 = new Point2f(p1.getX()+100.f, p1.getY()+100.f);
		p4 = new Point2f(p2.getX()+100.f, p2.getY()+100.f);
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p3 = new Point2f(p1.getX()-100.f, p1.getY()-100.f);
		p4 = new Point2f(p2.getX()-100.f, p2.getY()-100.f);
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p3 = new Point2f(p1.getX()+100.f, p1.getY());
		p4 = new Point2f(p2);
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p3 = new Point2f(p1.getX()-100.f, p1.getY());
		p4 = new Point2f(p2);
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p3 = new Point2f(0.f, 0.f);
		p4 = new Point2f(-100.f, 100.f);
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p3 = new Point2f(100.f, -10.f);
		p4 = new Point2f(-100.f, 100.f);
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p3 = new Point2f(p1.getX()-100.f, p1.getY());
		p4 = new Point2f(p2.getX()-100.f, p2.getY());
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p3 = new Point2f(p1.getX(), p1.getY()+100);
		p4 = new Point2f(p2.getX(), p2.getY()+100);
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p4, p3));
	}

	/**
	 */
	public void testIsParallelLinesFloatFloatFloatFloatFloatFloatFloatFloat() {
		Point2f p1 = new Point2f(-100.f,-100.f);
		Point2f p2 = new Point2f(100.f,100.f);
		Point2f p3, p4;
		
		p3 = new Point2f(p1);
		p4 = new Point2f(p2);
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(p1.getX()+100.f, p1.getY()+100.f);
		p4 = new Point2f(p2.getX()+100.f, p2.getY()+100.f);
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(p1.getX()-100.f, p1.getY()-100.f);
		p4 = new Point2f(p2.getX()-100.f, p2.getY()-100.f);
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(p1.getX()+100.f, p1.getY());
		p4 = new Point2f(p2);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(p1.getX()-100.f, p1.getY());
		p4 = new Point2f(p2);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(0.f, 0.f);
		p4 = new Point2f(-100.f, 100.f);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(100.f, -10.f);
		p4 = new Point2f(-100.f, 100.f);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(p1.getX()-100.f, p1.getY());
		p4 = new Point2f(p2.getX()-100.f, p2.getY());
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(p1.getX(), p1.getY()+100);
		p4 = new Point2f(p2.getX(), p2.getY()+100);
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));
	}

	/**
	 */
	public void testIsParallelLinesFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		Point3f p1 = new Point3f(-100.f,-100.f,50.f);
		Point3f p2 = new Point3f(100.f,100.f, 50.f);
		Point3f p3, p4;
		
		p3 = new Point3f(p1);
		p4 = new Point3f(p2);
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(p1.getX()+100.f, p1.getY()+100.f, p1.getZ());
		p4 = new Point3f(p2.getX()+100.f, p2.getY()+100.f, p2.getZ());
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(p1.getX()-100.f, p1.getY()-100.f, p1.getZ());
		p4 = new Point3f(p2.getX()-100.f, p2.getY()-100.f, p2.getZ());
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(p1.getX()+100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(p1.getX()-100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(0.f, 0.f, 0.f);
		p4 = new Point3f(-100.f, 100.f, 50.f);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(100.f, -10.f, 50.f);
		p4 = new Point3f(-100.f, 100.f, 50.f);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p1 = new Point3f(1.f, 1.f, 0.f);
		p2 = new Point3f(2.f, 2.f, 0.f);
		p3 = new Point3f(1.f, 1.f, 0.f);
		p4 = new Point3f(2.f, 2.f, 1.f);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p1 = new Point3f(1.f, 1.f, 0.f);
		p2 = new Point3f(2.f, 2.f, 0.f);
		p3 = new Point3f(1.f, 1.f, 1.f);
		p4 = new Point3f(2.f, 2.f, 1.f);
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));
	}

	/**
	 */
	public void testIsParallelLinesPoint3dPoint3dPoint3dPoint3d() {
		Point3f p1 = new Point3f(-100.f,-100.f,50.f);
		Point3f p2 = new Point3f(100.f,100.f, 50.f);
		Point3f p3, p4;
		
		p3 = new Point3f(p1);
		p4 = new Point3f(p2);
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		
		p3 = new Point3f(p1.getX()+100.f, p1.getY()+100.f, p1.getZ());
		p4 = new Point3f(p2.getX()+100.f, p2.getY()+100.f, p2.getZ());
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p3 = new Point3f(p1.getX()-100.f, p1.getY()-100.f, p1.getZ());
		p4 = new Point3f(p2.getX()-100.f, p2.getY()-100.f, p2.getZ());
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p3 = new Point3f(p1.getX()+100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2);
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p3 = new Point3f(p1.getX()-100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2);
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p3 = new Point3f(0.f, 0.f, 0.f);
		p4 = new Point3f(-100.f, 100.f, 50.f);
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p3 = new Point3f(100.f, -10.f, 50.f);
		p4 = new Point3f(-100.f, 100.f, 50.f);
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p1 = new Point3f(1.f, 1.f, 0.f);
		p2 = new Point3f(2.f, 2.f, 0.f);
		p3 = new Point3f(1.f, 1.f, 0.f);
		p4 = new Point3f(2.f, 2.f, 1.f);
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isParallelLines(p1, p2, p4, p3));

		p1 = new Point3f(1.f, 1.f, 0.f);
		p2 = new Point3f(2.f, 2.f, 0.f);
		p3 = new Point3f(1.f, 1.f, 1.f);
		p4 = new Point3f(2.f, 2.f, 1.f);
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isParallelLines(p1, p2, p4, p3));
	}
	
	/**
	 */
	public void testIsCollinearPointsFloatFloatFloatFloatFloatFloat() {
		Point2f p1 = new Point2f(100.f,100.f);
		Point2f p2 = new Point2f(-100.f,-100.f);
		Point2f p3;

		p3 = new Point2f(p1);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(p2);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(0.f,0.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(-50.f,50.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(50.f,-50.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(-1000.f,-1000.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(256.f,256.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(), p3.getY()));
	}

	/**
	 */
	public void testIsCollinearPointsPoint2dPoint2dPoint2d() {
		Point2f p1 = new Point2f(100.f,100.f);
		Point2f p2 = new Point2f(-100.f,-100.f);
		Point2f p3;

		p3 = new Point2f(p1);
		assertTrue(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point2f(p2);
		assertTrue(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point2f(0.f,0.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point2f(-50.f,50.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point2f(50.f,-50.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point2f(-1000.f,-1000.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point2f(256.f,256.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1, p2, p3));
	}

	/**
	 */
	public void testIsCollinearLinesFloatFloatFloatFloatFloatFloatFloatFloat() {
		Point2f p1 = new Point2f(-100.f,-100.f);
		Point2f p2 = new Point2f(100.f,100.f);
		Point2f p3, p4;
		
		p3 = new Point2f(p1);
		p4 = new Point2f(p2);
		assertTrue(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertTrue(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(p1.getX()+100.f, p1.getY()+100.f);
		p4 = new Point2f(p2.getX()+100.f, p2.getY()+100.f);
		assertTrue(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertTrue(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(p1.getX()-100.f, p1.getY()-100.f);
		p4 = new Point2f(p2.getX()-100.f, p2.getY()-100.f);
		assertTrue(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertTrue(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(p1.getX()+100.f, p1.getY());
		p4 = new Point2f(p2);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(p1.getX()-100.f, p1.getY());
		p4 = new Point2f(p2);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(0.f, 0.f);
		p4 = new Point2f(-100.f, 100.f);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(100.f, -10.f);
		p4 = new Point2f(-100.f, 100.f);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(p1.getX()-100.f, p1.getY());
		p4 = new Point2f(p2.getX()-100.f, p2.getY());
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));

		p3 = new Point2f(p1.getX(), p1.getY()+100);
		p4 = new Point2f(p2.getX(), p2.getY()+100);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p3.getX(),p3.getY(), p4.getX(),p4.getY()));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(), p2.getX(),p2.getY(), p4.getX(),p4.getY(), p3.getX(),p3.getY()));
	}

	/**
	 */
	public void testIsCollinearLinesPoint2dPoint2dPoint2dPoint2d() {
		Point2f p1 = new Point2f(-100.f,-100.f);
		Point2f p2 = new Point2f(100.f,100.f);
		Point2f p3, p4;
		
		p3 = new Point2f(p1);
		p4 = new Point2f(p2);
		assertTrue(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point2f(p1.getX()+100.f, p1.getY()+100.f);
		p4 = new Point2f(p2.getX()+100.f, p2.getY()+100.f);
		assertTrue(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point2f(p1.getX()-100.f, p1.getY()-100.f);
		p4 = new Point2f(p2.getX()-100.f, p2.getY()-100.f);
		assertTrue(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point2f(p1.getX()+100.f, p1.getY());
		p4 = new Point2f(p2);
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point2f(p1.getX()-100.f, p1.getY());
		p4 = new Point2f(p2);
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point2f(0.f, 0.f);
		p4 = new Point2f(-100.f, 100.f);
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point2f(100.f, -10.f);
		p4 = new Point2f(-100.f, 100.f);
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point2f(p1.getX()-100.f, p1.getY());
		p4 = new Point2f(p2.getX()-100.f, p2.getY());
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point2f(p1.getX(), p1.getY()+100);
		p4 = new Point2f(p2.getX(), p2.getY()+100);
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p4, p3));
	}

	/**
	 */
	public void testIsCollinearPointsFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		Point3f p1 = new Point3f(100.f,100.f,50.f);
		Point3f p2 = new Point3f(-100.f,-100.f,50.f);
		Point3f p3;

		// Numerical application from:
		// http://www.jtaylor1142001.net/calcjat/Solutions/3Dim/Collinear.htm
		assertTrue(GeometryUtil.isCollinearPoints(2.f,-1.f,4.f, -4.f, -4.f, 2.f, 14.f, 5.f, 8.f));
		assertFalse(GeometryUtil.isCollinearPoints(2.f,-1.f,4.f, -4.f, -4.f, 2.f, 14.f, 5.f, 7.f));

		p3 = new Point3f(p1);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(p2);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(0.f,0.f,50.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(-50.f,50.f,50.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(50.f,-50.f,50.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(-1000.f,-1000.f,50.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(256.f,256.f,50.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(p1);
		p3.setZ(0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(p2);
		p3.setZ(0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(0.f,0.f,0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(-50.f,50.f,0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(50.f,-50.f,0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(-1000.f,-1000.f,0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(256.f,256.f,0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ()));
	}

	/**
	 */
	public void testIsCollinearPointsPoint3dPoint3dPoint3d() {
		Point3f p1 = new Point3f(100.f,100.f,50.f);
		Point3f p2 = new Point3f(-100.f,-100.f,50.f);
		Point3f p3;

		// Numerical application from:
		// http://www.jtaylor1142001.net/calcjat/Solutions/3Dim/Collinear.htm
		assertTrue(GeometryUtil.isCollinearPoints(new Point3f(2.f,-1.f,4.f), new Point3f(-4.f, -4.f, 2.f), new Point3f(14.f, 5.f, 8.f)));
		assertFalse(GeometryUtil.isCollinearPoints(new Point3f(2.f,-1.f,4.f), new Point3f(-4.f, -4.f, 2.f), new Point3f(14.f, 5.f, 7.f)));

		p3 = new Point3f(p1);
		assertTrue(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point3f(p2);
		assertTrue(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point3f(0.f,0.f,50.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point3f(-50.f,50.f,50.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point3f(50.f,-50.f,50.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point3f(-1000.f,-1000.f,50.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point3f(256.f,256.f,50.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point3f(p1);
		p3.setZ(0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point3f(p2);
		p3.setZ(0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point3f(0.f,0.f,0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point3f(-50.f,50.f,0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point3f(50.f,-50.f,0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point3f(-1000.f,-1000.f,0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1, p2, p3));

		p3 = new Point3f(256.f,256.f,0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1, p2, p3));
	}
	
	/**
	 */
	public void testIsCollinearVectorsFloatFloatFloatFloat() {
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f));
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, 2.f, 2.f));
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, -1.f, -1.f));
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, -2.f, -2.f));
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, 0.f, 0.f));

		assertTrue(GeometryUtil.isCollinearVectors(1.f, -1.f, 1.f, -1.f));
		assertTrue(GeometryUtil.isCollinearVectors(1.f, -1.f, -1.f, 1.f));
		assertTrue(GeometryUtil.isCollinearVectors(-1.f, 1.f, -1.f, 1.f));
		assertTrue(GeometryUtil.isCollinearVectors(-1.f, 1.f, 1.f, -1.f));
		assertTrue(GeometryUtil.isCollinearVectors(-1.f, -1.f, -1.f, -1.f));
		assertTrue(GeometryUtil.isCollinearVectors(-1.f, -1.f, 1.f, 1.f));

		assertFalse(GeometryUtil.isCollinearVectors(1.f, -1.f, 1.f, 1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 2.f, -234.f));
		assertFalse(GeometryUtil.isCollinearVectors(234.f, -345.f, -3456.f, 2348.f));
		assertFalse(GeometryUtil.isCollinearVectors(-53.f, 7853.f, -4562.f, -234.f));
	}

	/**
	 */
	public void testIsCollinearVectorsVector2fVector2f() {
		assertTrue(GeometryUtil.isCollinearVectors(new Vector2f(1.f, 1.f), new Vector2f(1.f, 1.f)));
		assertTrue(GeometryUtil.isCollinearVectors(new Vector2f(1.f, 1.f), new Vector2f(2.f, 2.f)));
		assertTrue(GeometryUtil.isCollinearVectors(new Vector2f(1.f, 1.f), new Vector2f(-1.f, -1.f)));
		assertTrue(GeometryUtil.isCollinearVectors(new Vector2f(1.f, 1.f), new Vector2f(-2.f, -2.f)));
		assertTrue(GeometryUtil.isCollinearVectors(new Vector2f(1.f, 1.f), new Vector2f(0.f, 0.f)));

		assertTrue(GeometryUtil.isCollinearVectors(new Vector2f(1.f, -1.f), new Vector2f(1.f, -1.f)));
		assertTrue(GeometryUtil.isCollinearVectors(new Vector2f(1.f, -1.f), new Vector2f(-1.f, 1.f)));
		assertTrue(GeometryUtil.isCollinearVectors(new Vector2f(-1.f, 1.f), new Vector2f(-1.f, 1.f)));
		assertTrue(GeometryUtil.isCollinearVectors(new Vector2f(-1.f, 1.f), new Vector2f(1.f, -1.f)));
		assertTrue(GeometryUtil.isCollinearVectors(new Vector2f(-1.f, -1.f), new Vector2f(-1.f, -1.f)));
		assertTrue(GeometryUtil.isCollinearVectors(new Vector2f(-1.f, -1.f), new Vector2f(1.f, 1.f)));

		assertFalse(GeometryUtil.isCollinearVectors(new Vector2f(1.f, -1.f), new Vector2f(1.f, 1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector2f(1.f, 1.f), new Vector2f(2.f, -234.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector2f(234.f, -345.f), new Vector2f(-3456.f, 2348.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector2f(-53.f, 7853.f), new Vector2f(-4562.f, -234.f)));
	}

	/**
	 */
	public void testIsCollinearVectorsFloatFloatFloatFloatFloatFloat() {
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, -1.f, -1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, -1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, -1.f, 1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, 0.f, -1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, 0.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, 0.f, 1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, 1.f, -1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, 1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, 1.f, 1.f));

		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, -1.f, -1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, -1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, -1.f, 1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, 0.f, -1.f));
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, 0.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, 0.f, 1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, 1.f, -1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, 1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, 1.f, 1.f));

		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, -1.f, -1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, -1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, -1.f, 1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, 0.f, -1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, 0.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, 0.f, 1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, 1.f, -1.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, 1.f, 0.f));
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, 1.f, 1.f));

		assertTrue(GeometryUtil.isCollinearVectors(1234.f, 3245.f, 456.f, 2468.f, 6490.f, 912.f));
		assertFalse(GeometryUtil.isCollinearVectors(1234.f, 3245.f, 456.f, 2468.f, 6490.f, 914.f));

		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 0.f, 1.f, 1.f, 2.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 0.f, 1.f, 1.f, 2.f, 1.f));
		assertFalse(GeometryUtil.isCollinearVectors(0.f, 1.f, 1.f, 2.f, 1.f, 1.f));
	}

	/**
	 */
	public void testIsCollinearVectorsVector3dVector3d() {
		assertTrue(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(-1.f, -1.f, -1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(-1.f, -1.f, 0.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(-1.f, -1.f, 1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(-1.f, 0.f, -1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(-1.f, 0.f, 0.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(-1.f, 0.f, 1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(-1.f, 1.f, -1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(-1.f, 1.f, 0.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(-1.f, 1.f, 1.f)));

		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(0.f, -1.f, -1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(0.f, -1.f, 0.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(0.f, -1.f, 1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(0.f, 0.f, -1.f)));
		assertTrue(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(0.f, 0.f, 0.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(0.f, 0.f, 1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(0.f, 1.f, -1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(0.f, 1.f, 0.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(0.f, 1.f, 1.f)));

		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(1.f, -1.f, -1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(1.f, -1.f, 0.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(1.f, -1.f, 1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(1.f, 0.f, -1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(1.f, 0.f, 0.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f),new Vector3f( 1.f, 0.f, 1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(1.f, 1.f, -1.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(1.f, 1.f, 0.f)));
		assertTrue(GeometryUtil.isCollinearVectors(new Vector3f(1.f, 1.f, 1.f), new Vector3f(1.f, 1.f, 1.f)));

		assertTrue(GeometryUtil.isCollinearVectors(new Vector3f(1234.f, 3245.f, 456.f), new Vector3f(2468.f, 6490.f, 912.f)));
		assertFalse(GeometryUtil.isCollinearVectors(new Vector3f(1234.f, 3245.f, 456.f), new Vector3f(2468.f, 6490.f, 914.f)));
	}

	/**
	 */
	public void testIsCollinearLinesFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		Point3f p1 = new Point3f(-100.f,-100.f,50.f);
		Point3f p2 = new Point3f(100.f,100.f,50.f);
		Point3f p3, p4;
		
		p3 = new Point3f(p1);
		p4 = new Point3f(p2);
		assertTrue(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertTrue(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(p1.getX()+100.f, p1.getY()+100.f,p1.getZ());
		p4 = new Point3f(p2.getX()+100.f, p2.getY()+100.f,p2.getZ());
		assertTrue(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertTrue(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(p1.getX()-100.f, p1.getY()-100.f,p1.getZ());
		p4 = new Point3f(p2.getX()-100.f, p2.getY()-100.f,p2.getZ());
		assertTrue(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertTrue(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(p1.getX()+100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(p1.getX()-100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(0.f, 0.f, 50.f);
		p4 = new Point3f(-100.f, 100.f, 50.f);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(100.f, -10.f, 50.f);
		p4 = new Point3f(-100.f, 100.f, 50.f);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(p1.getX()-100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2.getX()-100.f, p2.getY(), p2.getZ());
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p3 = new Point3f(p1.getX(), p1.getY()+100, p1.getZ());
		p4 = new Point3f(p2.getX(), p2.getY()+100, p2.getZ());
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));
	
		p1 = new Point3f(1.f, 1.f, 0.f);
		p2 = new Point3f(2.f, 2.f, 0.f);
		p3 = new Point3f(1.f, 1.f, 0.f);
		p4 = new Point3f(2.f, 2.f, 1.f);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));

		p1 = new Point3f(1.f, 1.f, 0.f);
		p2 = new Point3f(2.f, 2.f, 0.f);
		p3 = new Point3f(1.f, 1.f, 1.f);
		p4 = new Point3f(2.f, 2.f, 1.f);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p3.getX(),p3.getY(),p3.getZ(), p4.getX(),p4.getY(),p4.getZ()));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ(), p4.getX(),p4.getY(),p4.getZ(), p3.getX(),p3.getY(),p3.getZ()));
	}

	/**
	 */
	public void testIsCollinearLinesPoint3dPoint3dPoint3dPoint3d() {
		Point3f p1 = new Point3f(-100.f,-100.f,50.f);
		Point3f p2 = new Point3f(100.f,100.f,50.f);
		Point3f p3, p4;
		
		p3 = new Point3f(p1);
		p4 = new Point3f(p2);
		assertTrue(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point3f(p1.getX()+100.f, p1.getY()+100.f,p1.getZ());
		p4 = new Point3f(p2.getX()+100.f, p2.getY()+100.f,p2.getZ());
		assertTrue(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point3f(p1.getX()-100.f, p1.getY()-100.f,p1.getZ());
		p4 = new Point3f(p2.getX()-100.f, p2.getY()-100.f,p2.getZ());
		assertTrue(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertTrue(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point3f(p1.getX()+100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2);
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point3f(p1.getX()-100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2);
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point3f(0.f, 0.f, 50.f);
		p4 = new Point3f(-100.f, 100.f, 50.f);
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point3f(100.f, -10.f, 50.f);
		p4 = new Point3f(-100.f, 100.f, 50.f);
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point3f(p1.getX()-100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2.getX()-100.f, p2.getY(), p2.getZ());
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p3 = new Point3f(p1.getX(), p1.getY()+100, p1.getZ());
		p4 = new Point3f(p2.getX(), p2.getY()+100, p2.getZ());
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p1 = new Point3f(1.f, 1.f, 0.f);
		p2 = new Point3f(2.f, 2.f, 0.f);
		p3 = new Point3f(1.f, 1.f, 0.f);
		p4 = new Point3f(2.f, 2.f, 1.f);
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p4, p3));

		p1 = new Point3f(1.f, 1.f, 0.f);
		p2 = new Point3f(2.f, 2.f, 0.f);
		p3 = new Point3f(1.f, 1.f, 1.f);
		p4 = new Point3f(2.f, 2.f, 1.f);
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p3, p4));
		assertFalse(GeometryUtil.isCollinearLines(p1, p2, p4, p3));
	}
	
	/** 
	 */
	public static void testRelativeSegmentCCWFloatFloatFloatFloatFloatFloatFalse() {
		Point2f p1 = new Point2f(-100.f,-100.f);
		Point2f p2 = new Point2f(100.f,100.f);
		Point2f p3;
		
		p3 = new Point2f(p1);
		assertEquals(0, MathUtil.relativeSegmentCCW(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), false));
		assertEquals(0, MathUtil.relativeSegmentCCW(p2.getX(), p2.getY(), p1.getX(), p1.getY(), p3.getX(), p3.getY(), false));

		p3 = new Point2f(p2);
		assertEquals(0, MathUtil.relativeSegmentCCW(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), false));
		assertEquals(0, MathUtil.relativeSegmentCCW(p2.getX(), p2.getY(), p1.getX(), p1.getY(), p3.getX(), p3.getY(), false));

		p3 = new Point2f(0.f, 0.f);
		assertEquals(0, MathUtil.relativeSegmentCCW(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), false));
		assertEquals(0, MathUtil.relativeSegmentCCW(p2.getX(), p2.getY(), p1.getX(), p1.getY(), p3.getX(), p3.getY(), false));

		p3 = new Point2f(MathConstants.EPSILON, 0.f);
		assertEquals(0, MathUtil.relativeSegmentCCW(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), false));
		assertEquals(0, MathUtil.relativeSegmentCCW(p2.getX(), p2.getY(), p1.getX(), p1.getY(), p3.getX(), p3.getY(), false));

		p3 = new Point2f(-200.f, -200.f);
		assertEquals(-1, MathUtil.relativeSegmentCCW(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), false));
		assertEquals(1, MathUtil.relativeSegmentCCW(p2.getX(), p2.getY(), p1.getX(), p1.getY(), p3.getX(), p3.getY(), false));

		p3 = new Point2f(200.f, 200.f);
		assertEquals(1, MathUtil.relativeSegmentCCW(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), false));
		assertEquals(-1, MathUtil.relativeSegmentCCW(p2.getX(), p2.getY(), p1.getX(), p1.getY(), p3.getX(), p3.getY(), false));

		p3 = new Point2f(-200.f, 200.f);
		assertEquals(-1, MathUtil.relativeSegmentCCW(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), false));
		assertEquals(1, MathUtil.relativeSegmentCCW(p2.getX(), p2.getY(), p1.getX(), p1.getY(), p3.getX(), p3.getY(), false));

		p3 = new Point2f(200.f, -200.f);
		assertEquals(1, MathUtil.relativeSegmentCCW(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), false));
		assertEquals(-1, MathUtil.relativeSegmentCCW(p2.getX(), p2.getY(), p1.getX(), p1.getY(), p3.getX(), p3.getY(), false));
	}
	
	/**
	 */
	public void testProjectsPointOnSegmentFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		Point3f p1 = new Point3f(-100.f,-100.f,50.f);
		Point3f p2 = new Point3f(100.f,100.f,50.f);
		Point3f p3;
		
		p3 = new Point3f(p1);
		assertZero(MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertEquals(1.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));

		p3 = new Point3f(p2);
		assertEquals(1.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertZero(MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));

		p3 = new Point3f(0.f, 0.f,50.f);
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));

		p3 = new Point3f(-100.f, 100.f,50.f);
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));

		p3 = new Point3f(100.f, -100.f,50.f);
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));

		p3 = new Point3f(-300, -300.f,50.f);
		assertEquals(-1.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertEquals(2.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));

		p3 = new Point3f(300, 300.f,50.f);
		assertEquals(2.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertEquals(-1.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));

		p3 = new Point3f(p1);
		p3.setZ(0.f);
		assertZero(MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertEquals(1.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));

		p3 = new Point3f(p2);
		p3.setZ(0.f);
		assertEquals(1.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertZero(MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));

		p3 = new Point3f(0.f, 0.f,0.f);
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));

		p3 = new Point3f(-100.f, 100.f,0.f);
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));

		p3 = new Point3f(100.f, -100.f,0.f);
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertEquals(.5f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));

		p3 = new Point3f(-300, -300.f,0.f);
		assertEquals(-1.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertEquals(2.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));

		p3 = new Point3f(300, 300.f,0.f);
		assertEquals(2.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p1.getX(),p1.getY(),p1.getZ(), p2.getX(),p2.getY(),p2.getZ()));
		assertEquals(-1.f, MathUtil.getPointProjectionFactorOnSegment(p3.getX(),p3.getY(),p3.getZ(), p2.getX(),p2.getY(),p2.getZ(), p1.getX(),p1.getY(),p1.getZ()));
	}

	/**
	 */
	public void testGetLineLineIntersectionFactorFloatFloatFloatFloatFloatFloatFloatFloat() {
		assertNaN(GeometryUtil.getIntersectionFactorLineLine(
				1.f, 1.f, 2.f, 2.f,
				1.f, 1.f, 2.f, 2.f));

		assertNaN(GeometryUtil.getIntersectionFactorLineLine(
				1.f, 1.f, 2.f, 2.f,
				1.f, 4.f, 2.f, 5.f));
		
		assertEpsilonEquals(.5f,
				GeometryUtil.getIntersectionFactorLineLine(
						-1.f, -1.f, 1.f, 1.f,
						-1.f, 1.f, 1.f, -1.f));


		assertEpsilonEquals(.579545455f,
				GeometryUtil.getIntersectionFactorLineLine(
						2.f, -32.f, 2.f, 100.f,
						101.f, 44.5f, 100, 44.5f));

		// Equation resolution with GNU Octave.
		//
		// Linear Equation Resolution:
		// a.getX() + b.getY() = c
		// d.getX() + e.getY() = f
		//
		// A = [ a  b ] 
		//     [ d  e ]
		//
		// b = [ c ]
		//     [ f ]
		//
		// A \ b = [ x ]
		//         [ y ]
		//
		// Back to line intersection problem:
		//
		// P1 + m.(P2-P1) = I
		// P3 + n.(P4-P3.f) = I
		//
		// m.(x2-x1) - n.(x4-x3.f) = x3-x1
		// m.(y2-y1) - n.(y4-y3.f) = y3-y1
		//
		// A = [ (x2-x1)  (x3-x4) ]
		//     [ (y2-y1)  (y3-y4) ]
		//
		// b = [ x3-x1 ]
		//     [ y3-y1 ]
		//
		// R = A \ b = [ m ]
		//             [ n ]
		//
		// [ x ] = P1 + m * (P2 - P1) = [ x1 ] + m * [ x2-x1 ] 
		// [ y ]                        [ y1 ]       [ y2-y1 ]
		
		// Octave Code:
		// output_precision(16)
		// P = { -2.f, 3.f, -1.f, 6f, -3.f, 28.f, 0.f, 24. }
		// x1=P{1}, y1=P{2}, x2=P{3}, y2=P{4}, x3=P{5}, y3=P{6}, x4=P{7}, y4=P{8}
		// A = [ x2-x1, x3-x4; y2-y1, y3-y4 ]
		// b = [ x3-x1; y3-y1 ]
		// R = A \ b
		// I = [ float(x1 + R(1) * (x2-x1)), float(y1 + R(1) * (y2-y1)) ]
		assertEpsilonEquals(5.461538461538462e+00f,
				GeometryUtil.getIntersectionFactorLineLine(
						-2.f, 3.f, -1.f, 6f,
						-3.f, 28.f, 0.f, 24.f));
	}

	/**
	 */
	public void testGetLineLineIntersectionFactorPoint2dPoint2dPoint2dPoint2d() {
		assertNaN(GeometryUtil.getLineLineIntersectionFactor(
				new Point2f(1.f, 1.f), new Point2f(2.f, 2.f),
				new Point2f(1.f, 1.f), new Point2f(2.f, 2.f)));

		assertNaN(GeometryUtil.getLineLineIntersectionFactor(
				new Point2f(1.f, 1.f), new Point2f(2.f, 2.f),
				new Point2f(1.f, 4.f), new Point2f(2.f, 5.f)));
		
		assertEpsilonEquals(.5f,
				GeometryUtil.getLineLineIntersectionFactor(
						new Point2f(-1.f, -1.f), new Point2f(1.f, 1.f),
						new Point2f(-1.f, 1.f), new Point2f(1.f, -1.f)));


		assertEpsilonEquals(.579545455f,
				GeometryUtil.getLineLineIntersectionFactor(
						new Point2f(2.f, -32.f), new Point2f(2.f, 100.f),
						new Point2f(101.f, 44.5f), new Point2f(100, 44.5f)));

		// Equation resolution with GNU Octave.
		//
		// Linear Equation Resolution:
		// a.getX() + b.getY() = c
		// d.getX() + e.getY() = f
		//
		// A = [ a  b ] 
		//     [ d  e ]
		//
		// b = [ c ]
		//     [ f ]
		//
		// A \ b = [ x ]
		//         [ y ]
		//
		// Back to line intersection problem:
		//
		// P1 + m.(P2-P1) = I
		// P3 + n.(P4-P3.f) = I
		//
		// m.(x2-x1) - n.(x4-x3.f) = x3-x1
		// m.(y2-y1) - n.(y4-y3.f) = y3-y1
		//
		// A = [ (x2-x1)  (x3-x4) ]
		//     [ (y2-y1)  (y3-y4) ]
		//
		// b = [ x3-x1 ]
		//     [ y3-y1 ]
		//
		// R = A \ b = [ m ]
		//             [ n ]
		//
		// [ x ] = P1 + m * (P2 - P1) = [ x1 ] + m * [ x2-x1 ] 
		// [ y ]                        [ y1 ]       [ y2-y1 ]
		
		// Octave Code:
		// output_precision(16)
		// P = { -2.f, 3.f, -1.f, 6f, -3.f, 28.f, 0.f, 24. }
		// x1=P{1}, y1=P{2}, x2=P{3}, y2=P{4}, x3=P{5}, y3=P{6}, x4=P{7}, y4=P{8}
		// A = [ x2-x1, x3-x4; y2-y1, y3-y4 ]
		// b = [ x3-x1; y3-y1 ]
		// R = A \ b
		// I = [ float(x1 + R(1) * (x2-x1)), float(y1 + R(1) * (y2-y1)) ]
		assertEpsilonEquals(5.461538461538462e+00f,
				GeometryUtil.getLineLineIntersectionFactor(
						new Point2f(-2.f, 3.f), new Point2f(-1.f, 6),
						new Point2f(-3.f, 28.f), new Point2f(0.f, 24.f)));
	}

	/**
	 */
	public void testGetLineLineIntersectionPointFloatFloatFloatFloatFloatFloatFloatFloat() {
		assertNull(MathUtil.getLineLineIntersectionPoint(
				1.f, 1.f, 2.f, 2.f,
				1.f, 1.f, 2.f, 2.f));

		assertNull(MathUtil.getLineLineIntersectionPoint(
				1.f, 1.f, 2.f, 2.f,
				1.f, 4.f, 2.f, 5.f));
		
		assertEpsilonEquals(new Point2f(),
				MathUtil.getLineLineIntersectionPoint(
						-1.f, -1.f, 1.f, 1.f,
						-1.f, 1.f, 1.f, -1.f));


		assertEpsilonEquals(new Point2f(2.f,44.5f),
				MathUtil.getLineLineIntersectionPoint(
						2.f, -32.f, 2.f, 100.f,
						101.f, 44.5f, 100.f, 44.5f));

		// Equation resolution with GNU Octave.
		//
		// Linear Equation Resolution:
		// a.getX() + b.getY() = c
		// d.getX() + e.getY() = f
		//
		// A = [ a  b ] 
		//     [ d  e ]
		//
		// b = [ c ]
		//     [ f ]
		//
		// A \ b = [ x ]
		//         [ y ]
		//
		// Back to line intersection problem:
		//
		// P1 + m.(P2-P1) = I
		// P3 + n.(P4-P3.f) = I
		//
		// m.(x2-x1) - n.(x4-x3.f) = x3-x1
		// m.(y2-y1) - n.(y4-y3.f) = y3-y1
		//
		// A = [ (x2-x1)  (x3-x4) ]
		//     [ (y2-y1)  (y3-y4) ]
		//
		// b = [ x3-x1 ]
		//     [ y3-y1 ]
		//
		// R = A \ b = [ m ]
		//             [ n ]
		//
		// [ x ] = P1 + m * (P2 - P1) = [ x1 ] + m * [ x2-x1 ] 
		// [ y ]                        [ y1 ]       [ y2-y1 ]
		
		// Octave Code:
		// output_precision(16)
		// P = { -2.f, 3.f, -1.f, 6f, -3.f, 28.f, 0.f, 24. }
		// x1=P{1}, y1=P{2}, x2=P{3}, y2=P{4}, x3=P{5}, y3=P{6}, x4=P{7}, y4=P{8}
		// A = [ x2-x1, x3-x4; y2-y1, y3-y4 ]
		// b = [ x3-x1; y3-y1 ]
		// R = A \ b
		// I = [ float(x1 + R(1) * (x2-x1)), float(y1 + R(1) * (y2-y1)) ]
		assertEpsilonEquals(new Point2f(3.461538461538462e+00, 1.938461538461539e+01),
				MathUtil.getLineLineIntersectionPoint(
						-2.f, 3.f, -1.f, 6.f,
						-3.f, 28.f, 0.f, 24.f));
	}

	/**
	 */
	public void testGetLineLineIntersectionFactorFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		assertNaN(GeometryUtil.getLineLineIntersectionFactor(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f));

		assertNaN(GeometryUtil.getLineLineIntersectionFactor(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 4.f, 0.f, 2.f, 5.f, 0.f));

		assertEpsilonEquals(.5f,
				GeometryUtil.getLineLineIntersectionFactor(
						-1.f, -1.f, 0.f, 1.f, 1.f, 0.f,
						-1.f, 1.f, 0.f, 1.f, -1.f, 0.f));


		assertEpsilonEquals(.579545455f,
				GeometryUtil.getLineLineIntersectionFactor(
						2.f, -32.f, 0.f, 2.f, 100.f, 0.f,
						101.f, 44.5f, 0.f, 100.f, 44.5f, 0.f));

		assertEpsilonEquals(
				0.f,
				GeometryUtil.getLineLineIntersectionFactor(
				0.f, 1.f, 1.f, 0.f, 2.f, 2.f,
				0.f, 1.f, 1.f, 1.f, 2.f, 2.f));

		assertEpsilonEquals(
				0.f,
				GeometryUtil.getLineLineIntersectionFactor(
				1.f, 0.f, 1.f, 2.f, 0.f, 2.f,
				1.f, 0.f, 1.f, 2.f, 1.f, 2.f));

		assertEpsilonEquals(
				0.f,
				GeometryUtil.getLineLineIntersectionFactor(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 0.f, 2.f, 2.f, 1.f));

		assertNaN(
				GeometryUtil.getLineLineIntersectionFactor(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 1.f, 2.f, 2.f, 1.f));

		// Equation resolution with GNU Octave.
		//
		// Linear Equation Resolution:
		// a.getX() + b.getY() + c.getZ() = d
		// e.getX() + f.getY() + g.getZ() = h
		// i.getX() + j.getY() + k.getZ() = l
		//
		// A = [ a  b  c ] 
		//     [ e  f  g ]
		//     [ i  j  k ]
		//
		// b = [ d ]
		//     [ h ]
		//     [ l ]
		//
		// A \ b = [ x ]
		//         [ y ]
		//         [ z ]
		//
		// Back to line intersection problem:
		//
		// P1 + m.(P2-P1) = I
		// P3 + n.(P4-P3.f) = I
		//
		// m.(x2-x1) - n.(x4-x3.f) = x3-x1
		// m.(y2-y1) - n.(y4-y3.f) = y3-y1
		// m.(z2-z1) - n.(z4-z3.f) = z3-z1
		//
		// A = [ (x2-x1)  (x3-x4) 0 ]
		//     [ (y2-y1)  (y3-y4) 0 ]
		//     [ (z2-z1)  (z3-z4) 0 ]
		//
		// b = [ x3-x1 ]
		//     [ y3-y1 ]
		//     [ z3-z1 ]
		//
		// R = A \ b = [ m ]
		//             [ n ]
		//             [ _ ]
		//
		// [ x ] = P1 + m * (P2 - P1) = [ x1 ] + m * [ x2-x1 ] 
		// [ y ]                        [ y1 ]       [ y2-y1 ]
		// [ z ]                        [ z1 ]       [ z2-z1 ]
		
		// Octave Code:
		// output_precision(16)
		// P = { -2.f, 3.f, 0.f, -1.f, 6f, 0.f, -3.f, 28.f, 0.f, 0.f, 24.f, 0. }
		// x1=P{1}, y1=P{2}, z1=P{3}, x2=P{4}, y2=P{5}, z2=P{6}, x3=P{7}, y3=P{8}, z3=P{9}, x4=P{10}, y4=P{11}, z4=P{12}
		// A = [ x2-x1, x3-x4f, 0.; y2-y1, y3-y4f, 0.; z2-z1, z3-z4f, 0. ]
		// b = [ x3-x1; y3-y1; z3-z1 ]
		// R = A \ b
		// I = [ x1 + R(1) * (x2-x1), y1 + R(1) * (y2-y1), z1 + R(1) * (z2-z1) ]
		assertEpsilonEquals(5.461538461538462e+00f,
				GeometryUtil.getLineLineIntersectionFactor(
						-2.f, 3.f, 0.f, -1.f, 6f, 0.f,
						-3.f, 28.f, 0.f, 0.f, 24.f, 0.f));
	}

	/**
	 */
	public void testGetLineLineIntersectionPointFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		assertNull(MathUtil.getLineLineIntersectionPoint(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f));

		assertNull(MathUtil.getLineLineIntersectionPoint(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 4.f, 0.f, 2.f, 5.f, 0.f));

		assertEpsilonEquals(new Point3f(),
				MathUtil.getLineLineIntersectionPoint(
						-1.f, -1.f, 0.f, 1.f, 1.f, 0.f,
						-1.f, 1.f, 0.f, 1.f, -1.f, 0.f));


		assertEpsilonEquals(new Point3f(2.f,44.5f,0.f),
				MathUtil.getLineLineIntersectionPoint(
						2.f, -32.f, 0.f, 2.f, 100.f, 0.f,
						101.f, 44.5f, 0.f, 100.f, 44.5f, 0.f));

		assertEpsilonEquals(
				new Point3f(0.f, 1.f, 1.f),
				MathUtil.getLineLineIntersectionPoint(
				0.f, 1.f, 1.f, 0.f, 2.f, 2.f,
				0.f, 1.f, 1.f, 1.f, 2.f, 2.f));

		assertEpsilonEquals(
				new Point3f(1.f, 0.f, 1.f),
				MathUtil.getLineLineIntersectionPoint(
				1.f, 0.f, 1.f, 2.f, 0.f, 2.f,
				1.f, 0.f, 1.f, 2.f, 1.f, 2.f));

		assertEpsilonEquals(
				new Point3f(1.f, 1.f, 0.f),
				MathUtil.getLineLineIntersectionPoint(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 0.f, 2.f, 2.f, 1.f));

		assertNull(
				MathUtil.getLineLineIntersectionPoint(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 1.f, 2.f, 2.f, 1.f));

		// Equation resolution with GNU Octave.
		//
		// Linear Equation Resolution:
		// a.getX() + b.getY() + c.getZ() = d
		// e.getX() + f.getY() + g.getZ() = h
		// i.getX() + j.getY() + k.getZ() = l
		//
		// A = [ a  b  c ] 
		//     [ e  f  g ]
		//     [ i  j  k ]
		//
		// b = [ d ]
		//     [ h ]
		//     [ l ]
		//
		// A \ b = [ x ]
		//         [ y ]
		//         [ z ]
		//
		// Back to line intersection problem:
		//
		// P1 + m.(P2-P1) = I
		// P3 + n.(P4-P3.f) = I
		//
		// m.(x2-x1) - n.(x4-x3.f) = x3-x1
		// m.(y2-y1) - n.(y4-y3.f) = y3-y1
		// m.(z2-z1) - n.(z4-z3.f) = z3-z1
		//
		// A = [ (x2-x1)  (x3-x4) 0 ]
		//     [ (y2-y1)  (y3-y4) 0 ]
		//     [ (z2-z1)  (z3-z4) 0 ]
		//
		// b = [ x3-x1 ]
		//     [ y3-y1 ]
		//     [ z3-z1 ]
		//
		// R = A \ b = [ m ]
		//             [ n ]
		//             [ _ ]
		//
		// [ x ] = P1 + m * (P2 - P1) = [ x1 ] + m * [ x2-x1 ] 
		// [ y ]                        [ y1 ]       [ y2-y1 ]
		// [ z ]                        [ z1 ]       [ z2-z1 ]
		
		// Octave Code:
		// output_precision(16)
		// P = { -2.f, 3.f, 0.f, -1.f, 6f, 0.f, -3.f, 28.f, 0.f, 0.f, 24.f, 0. }
		// x1=P{1}, y1=P{2}, z1=P{3}, x2=P{4}, y2=P{5}, z2=P{6}, x3=P{7}, y3=P{8}, z3=P{9}, x4=P{10}, y4=P{11}, z4=P{12}
		// A = [ x2-x1, x3-x4f, 0.; y2-y1, y3-y4f, 0.; z2-z1, z3-z4f, 0. ]
		// b = [ x3-x1; y3-y1; z3-z1 ]
		// R = A \ b
		// I = [ x1 + R(1) * (x2-x1), y1 + R(1) * (y2-y1), z1 + R(1) * (z2-z1) ]
		assertEpsilonEquals(new Point3f(3.461538461538461e+00, 1.938461538461538e+01, 0.f),
				MathUtil.getLineLineIntersectionPoint(
						-2.f, 3.f, 0.f, -1.f, 6f, 0.f,
						-3.f, 28.f, 0.f, 0.f, 24.f, 0.f));

		// P = { 1.f, 1.f, 0.f, 2.f, 2.f, 0.f, 1.f, 1.f, 0.f, 2.f, 2.f, 1. }
		assertEpsilonEquals(new Point3f(1.f, 1.f, 0.f),
				MathUtil.getLineLineIntersectionPoint(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 0.f, 2.f, 2.f, 1.f));

		//
		// Values from Olivier L. (Points are not coplanars)
		//
		assertNull(MathUtil.getLineLineIntersectionPoint(
						-10,-10,0,
						1, 1, 3,
						-3, 5f, 3,
						3, -5f, 3.f));
		assertNull(MathUtil.getLineLineIntersectionPoint(
						1, 1, 3,
						-10,-10,0,
						-3, 5f, 3,
						3, -5f, 3.f));
		assertNull(MathUtil.getLineLineIntersectionPoint(
						1, 1, 3,
						-10,-10,0,
						3, -5f, 3,
						-3, 5f, 3.f));
		assertNull(MathUtil.getLineLineIntersectionPoint(
						-10,-10,0,
						1, 1, 3,
						3, -5f, 3,
						-3, 5f, 3.f));
		//
		assertNull(MathUtil.getLineLineIntersectionPoint(
				-3, 5f, 3,
				3, -5f, 3,
				-10,-10,0,
				1, 1, 3.f));
		assertNull(MathUtil.getLineLineIntersectionPoint(
				3, -5f, 3,
				-3, 5f, 3,
				-10,-10,0,
				1, 1, 3.f));
		assertNull(MathUtil.getLineLineIntersectionPoint(
				3, -5f, 3,
				-3, 5f, 3,
				1, 1, 3,
				-10,-10,0));
		assertNull(MathUtil.getLineLineIntersectionPoint(
				-3, 5f, 3,
				3, -5f, 3,
				1, 1, 3,
				-10,-10,0));
	}
	
	/**
	 */
	public void testIsCoplanarPointsFloatFloatArray() {
		//
		// Values from Olivier L. (Points are not coplanars)
		//
		Point3f s1 = new Point3f(-10,-10,0);
		Point3f e1 = new Point3f(1, 1, 3.f);
		Point3f s2 = new Point3f(-3, 5f, 3.f);
		Point3f e2 = new Point3f(3, -5f, 3.f);
		
		assertTrue(MathUtil.isCoplanarPoints(
				0.f, //epsilon
				s1));

		assertTrue(MathUtil.isCoplanarPoints(
				0.f, //epsilon
				s1, e1));

		assertTrue(MathUtil.isCoplanarPoints(
				0.f, //epsilon
				s1, e1,
				s2));
		assertTrue(MathUtil.isCoplanarPoints(
				0.f, //epsilon
				s1, e1,
				e2));

		assertFalse(MathUtil.isCoplanarPoints(
				0.f, //epsilon
				s1, e1,
				s2, e2));
		assertFalse(MathUtil.isCoplanarPoints(
				0.f, //epsilon
				e1, s1,
				s2, e2));
		assertFalse(MathUtil.isCoplanarPoints(
				0.f, //epsilon
				e1, s1,
				e2, s2));
		assertFalse(MathUtil.isCoplanarPoints(
				0.f, //epsilon
				s1, e1,
				e2, s2));
	}

	/**
	 */
	public void testGetSegmentSegmentIntersectionFactorFloatFloatFloatFloatFloatFloatFloatFloat() {
		assertNaN(GeometryUtil.getIntersectionFactorSegmentSegment(
				1.f, 1.f, 2.f, 2.f,
				1.f, 1.f, 2.f, 2.f));

		assertNaN(GeometryUtil.getIntersectionFactorSegmentSegment(
				1.f, 1.f, 2.f, 2.f,
				1.f, 1.f, -2.f, -2.f));

		assertNaN(GeometryUtil.getIntersectionFactorSegmentSegment(
				1.f, 1.f, 2.f, 2.f,
				2.f, 2.f, 1.f, 1.f));

		assertNaN(GeometryUtil.getIntersectionFactorSegmentSegment(
				1.f, 1.f, 2.f, 2.f,
				1.f, 2.f, 2.f, 3.f));

		assertNaN(GeometryUtil.getIntersectionFactorSegmentSegment(
				1.f, 1.f, 2.f, 2.f,
				1.f, 1.f, 3.f, 3.f));

		assertEpsilonEquals(0.f,
				GeometryUtil.getIntersectionFactorSegmentSegment(
				1.f, 1.f, 2.f, 2.f,
				1.f, 1.f, 3.f, 9.f));

		assertEpsilonEquals(1.f,
				GeometryUtil.getIntersectionFactorSegmentSegment(
				1.f, 1.f, 2.f, 2.f,
				10.f, 0.f, 2.f, 2.f));

		assertEpsilonEquals(.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(
				-1.f, -1.f, 1.f, 1.f,
				-1.f, 1.f, 1.f, -1.f));

		assertNaN(GeometryUtil.getIntersectionFactorSegmentSegment(
				0.f, 0.f, 1.f, 0.f,
				1.5f, 0.f, 10.f, 0.f));

		assertNaN(GeometryUtil.getIntersectionFactorSegmentSegment(
				-1.f, -1.f, 1.f, 1.f,
				2.f, 10.f, 20.f, -20.f));

		assertEpsilonEquals(
				.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(
				-50.f, -50.f, 50.f, 50.f,
				-50.f, 50.f, 50.f, -50.f));

		assertEpsilonEquals(
				.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(
				-50.f, -50.f, 50.f, 50.f,
				50.f, -50.f, -50.f, 50.f));
	}

	/**
	 */
	public void testGetSegmentSegmentIntersectionPointFloatFloatFloatFloatFloatFloatFloatFloat() {
		assertNull(GeometryUtil.getIntersectionPointSegmentSegment(
				1.f, 1.f, 2.f, 2.f,
				1.f, 1.f, 2.f, 2.f));

		assertNull(GeometryUtil.getIntersectionPointSegmentSegment(
				1.f, 1.f, 2.f, 2.f,
				1.f, 1.f, -2.f, -2.f));

		assertNull(GeometryUtil.getIntersectionPointSegmentSegment(
				1.f, 1.f, 2.f, 2.f,
				2.f, 2.f, 1.f, 1.f));

		assertNull(GeometryUtil.getIntersectionPointSegmentSegment(
				1.f, 1.f, 2.f, 2.f,
				1.f, 2.f, 2.f, 3.f));

		assertNull(GeometryUtil.getIntersectionPointSegmentSegment(
				1.f, 1.f, 2.f, 2.f,
				1.f, 1.f, 3.f, 3.f));

		assertEpsilonEquals(new Point2f(1.f, 1.f),
				GeometryUtil.getIntersectionPointSegmentSegment(
				1.f, 1.f, 2.f, 2.f,
				1.f, 1.f, 3.f, 9.f));

		assertEpsilonEquals(new Point2f(2.f, 2.f),
				GeometryUtil.getIntersectionPointSegmentSegment(
				1.f, 1.f, 2.f, 2.f,
				10.f, 0.f, 2.f, 2.f));

		assertEpsilonEquals(new Point2f(0.f,0.f),
				GeometryUtil.getIntersectionPointSegmentSegment(
				-1.f, -1.f, 1.f, 1.f,
				-1.f, 1.f, 1.f, -1.f));

		assertNull(GeometryUtil.getIntersectionPointSegmentSegment(
				0.f, 0.f, 1.f, 0.f,
				1.5f, 0.f, 10.f, 0.f));

		assertNull(GeometryUtil.getIntersectionPointSegmentSegment(
				-1.f, -1.f, 1.f, 1.f,
				2.f, 10.f, 20.f, -20.f));

		assertEpsilonEquals(
				new Point2f(0.f, 0.f),
				GeometryUtil.getIntersectionPointSegmentSegment(
				-50.f, -50.f, 50.f, 50.f,
				-50.f, 50.f, 50.f, -50.f));

		assertEpsilonEquals(
				new Point2f(0.f, 0.f),
				GeometryUtil.getIntersectionPointSegmentSegment(
				-50.f, -50.f, 50.f, 50.f,
				50.f, -50.f, -50.f, 50.f));
	}

	/**
	 */
	public void testGetSegmentSegmentIntersectionFactorFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f));

		assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 0.f, -2.f, -2.f, 0.f));

		assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				2.f, 2.f, 0.f, 1.f, 1.f, 0.f));

		assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 2.f, 0.f, 2.f, 3.f, 0.f));

		assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 0.f, 3.f, 3.f, 0.f));

		assertEpsilonEquals(0.f,
				GeometryUtil.getSegmentSegmentIntersectionFactor(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 0.f, 3.f, 9.f, 0.f));

		assertEpsilonEquals(1.f,
				GeometryUtil.getSegmentSegmentIntersectionFactor(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				10.f, 0.f, 0.f, 2.f, 2.f, 0.f));

		assertEpsilonEquals(.5f,
				GeometryUtil.getSegmentSegmentIntersectionFactor(
				-1.f, -1.f, 0.f, 1.f, 1.f, 0.f,
				-1.f, 1.f, 0.f, 1.f, -1.f, 0.f));

		assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
				0.f, 0.f, 0.f, 1.f, 0.f, 0.f,
				1.5f, 0.f, 0.f, 10.f, 0.f, 0.f));

		assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
				-1.f, -1.f, 0.f, 1.f, 1.f, 0.f,
				2.f, 10.f, 0.f, 20.f, -20.f, 0.f));

		assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
				1.f, 0.f, 1.f, 2.f, 0.f, 2.f,
				1.f, 0.f, 1.f, -2.f, 0.f, -2.f));

		assertEpsilonEquals(0.f,
				GeometryUtil.getSegmentSegmentIntersectionFactor(
				1.f, 1.f, 1.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 1.f, 2.f, 2.f, 2.f));

		assertEpsilonEquals(0.f,
				GeometryUtil.getSegmentSegmentIntersectionFactor(
				1.f, 1.f, 1.f, 2.f, 0.f, 2.f,
				1.f, 1.f, 1.f, 2.f, 2.f, 2.f));

		assertEpsilonEquals(0.f,
				GeometryUtil.getSegmentSegmentIntersectionFactor(
				1.f, 1.f, 1.f, 0.f, 2.f, 2.f,
				1.f, 1.f, 1.f, 2.f, 2.f, 2.f));

		assertEpsilonEquals(.5f,
				GeometryUtil.getSegmentSegmentIntersectionFactor(
				-50.f, -50.f, 0.f, 50.f, 50.f, 0.f,
				-50.f, 50.f, 0.f, 50.f, -50.f, 0.f));

		assertEpsilonEquals(.5f,
				GeometryUtil.getSegmentSegmentIntersectionFactor(
				-50.f, -50.f, 0.f, 50.f, 50.f, 0.f,
				50.f, -50.f, 0.f, -50.f, 50.f, 0.f));
	}

	/**
	 */
	public void testGetSegmentSegmentIntersectionPointFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f));

		assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 0.f, -2.f, -2.f, 0.f));

		assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				2.f, 2.f, 0.f, 1.f, 1.f, 0.f));

		assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 2.f, 0.f, 2.f, 3.f, 0.f));

		assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 0.f, 3.f, 3.f, 0.f));

		assertEpsilonEquals(new Point3f(1.f, 1.f, 0.f),
				GeometryUtil.getSegmentSegmentIntersectionPoint(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 0.f, 3.f, 9.f, 0.f));

		assertEpsilonEquals(new Point3f(2.f, 2.f, 0.f),
				GeometryUtil.getSegmentSegmentIntersectionPoint(
				1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
				10.f, 0.f, 0.f, 2.f, 2.f, 0.f));

		assertEpsilonEquals(new Point3f(0.f, 0.f, 0.f),
				GeometryUtil.getSegmentSegmentIntersectionPoint(
				-1.f, -1.f, 0.f, 1.f, 1.f, 0.f,
				-1.f, 1.f, 0.f, 1.f, -1.f, 0.f));

		assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
				0.f, 0.f, 0.f, 1.f, 0.f, 0.f,
				1.5f, 0.f, 0.f, 10.f, 0.f, 0.f));

		assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
				-1.f, -1.f, 0.f, 1.f, 1.f, 0.f,
				2.f, 10.f, 0.f, 20.f, -20.f, 0.f));

		assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
				1.f, 0.f, 1.f, 2.f, 0.f, 2.f,
				1.f, 0.f, 1.f, -2.f, 0.f, -2.f));

		assertEpsilonEquals(new Point3f(1.f, 1.f, 1.f),
				GeometryUtil.getSegmentSegmentIntersectionPoint(
				1.f, 1.f, 1.f, 2.f, 2.f, 0.f,
				1.f, 1.f, 1.f, 2.f, 2.f, 2.f));

		assertEpsilonEquals(new Point3f(1.f, 1.f, 1.f),
				GeometryUtil.getSegmentSegmentIntersectionPoint(
				1.f, 1.f, 1.f, 2.f, 0.f, 2.f,
				1.f, 1.f, 1.f, 2.f, 2.f, 2.f));

		assertEpsilonEquals(new Point3f(1.f, 1.f, 1.f),
				GeometryUtil.getSegmentSegmentIntersectionPoint(
				1.f, 1.f, 1.f, 0.f, 2.f, 2.f,
				1.f, 1.f, 1.f, 2.f, 2.f, 2.f));

		assertEpsilonEquals(
				new Point3f(0.f, 0.f, 0.f),
				GeometryUtil.getSegmentSegmentIntersectionPoint(
				-50.f, -50.f, 0.f, 50.f, 50.f, 0.f,
				-50.f, 50.f, 0.f, 50.f, -50.f, 0.f));

		assertEpsilonEquals(
				new Point3f(0.f, 0.f, 0.f),
				GeometryUtil.getSegmentSegmentIntersectionPoint(
				-50.f, -50.f, 0.f, 50.f, 50.f, 0.f,
				50.f, -50.f, 0.f, -50.f, 50.f, 0.f));
	}

	/**
	 */
	public void testInterpolateLineFloatFloatFloatFloatFloatFloatFloat() {
		assertEpsilonEquals(new Point3f(-1,-3,8), MathUtil.interpolateLine(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, -1.f));
		assertEpsilonEquals(new Point3f(0,-.5f,6), MathUtil.interpolateLine(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, -.5f));
		assertEpsilonEquals(new Point3f(1,2,4), MathUtil.interpolateLine(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, 0.f));
		assertEpsilonEquals(new Point3f(1.5f,3.25f,3.f), MathUtil.interpolateLine(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, .25f));
		assertEpsilonEquals(new Point3f(2,4.5f,2), MathUtil.interpolateLine(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, .5f));
		assertEpsilonEquals(new Point3f(2.5f,5.75f,1), MathUtil.interpolateLine(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, .75f));
		assertEpsilonEquals(new Point3f(3,7f,0), MathUtil.interpolateLine(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, 1));
		assertEpsilonEquals(new Point3f(4f,9.5f,-2), MathUtil.interpolateLine(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, 1.5f));
		assertEpsilonEquals(new Point3f(5f,12,-4), MathUtil.interpolateLine(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, 2.f));
	}

	/**
	 */
	public void testInterpolateSegmentFloatFloatFloatFloatFloatFloatFloat() {
		assertEpsilonEquals(new Point3f(1,2,4), MathUtil.interpolateSegment(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, -1.f));
		assertEpsilonEquals(new Point3f(1,2,4), MathUtil.interpolateSegment(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, -.5f));
		assertEpsilonEquals(new Point3f(1,2,4), MathUtil.interpolateSegment(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, 0.f));
		assertEpsilonEquals(new Point3f(1.5f,3.25f,3.f), MathUtil.interpolateSegment(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, .25f));
		assertEpsilonEquals(new Point3f(2,4.5f,2), MathUtil.interpolateSegment(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, .5f));
		assertEpsilonEquals(new Point3f(2.5f,5.75f,1), MathUtil.interpolateSegment(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, .75f));
		assertEpsilonEquals(new Point3f(3,7f,0), MathUtil.interpolateSegment(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, 1));
		assertEpsilonEquals(new Point3f(3,7f,0), MathUtil.interpolateSegment(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, 1.5f));
		assertEpsilonEquals(new Point3f(3,7f,0), MathUtil.interpolateSegment(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, 2.f));
	}

	/**
	 */
	public void testInterpolateLineFloatFloatFloatFloatFloat() {
		assertEpsilonEquals(new Point2f(-1,0), MathUtil.interpolateLine(1.f, 2.f, 3.f, 4.f, -1.f));
		assertEpsilonEquals(new Point2f(0,1), MathUtil.interpolateLine(1.f, 2.f, 3.f, 4.f, -.5f));
		assertEpsilonEquals(new Point2f(1,2), MathUtil.interpolateLine(1.f, 2.f, 3.f, 4.f, 0.f));
		assertEpsilonEquals(new Point2f(1.5f,2.5f), MathUtil.interpolateLine(1.f, 2.f, 3.f, 4.f, .25f));
		assertEpsilonEquals(new Point2f(2,3.f), MathUtil.interpolateLine(1.f, 2.f, 3.f, 4.f, .5f));
		assertEpsilonEquals(new Point2f(2.5f,3.5f), MathUtil.interpolateLine(1.f, 2.f, 3.f, 4.f, .75f));
		assertEpsilonEquals(new Point2f(3,4), MathUtil.interpolateLine(1.f, 2.f, 3.f, 4.f, 1.f));
		assertEpsilonEquals(new Point2f(4f,5f), MathUtil.interpolateLine(1.f, 2.f, 3.f, 4.f, 1.5f));
		assertEpsilonEquals(new Point2f(5f,6), MathUtil.interpolateLine(1.f, 2.f, 3.f, 4.f, 2.f));
	}

	/**
	 */
	public void testInterpolateSegmentFloatFloatFloatFloatFloat() {
		assertEpsilonEquals(new Point2f(1,2), MathUtil.interpolateSegment(1.f, 2.f, 3.f, 4.f, -1.f));
		assertEpsilonEquals(new Point2f(1,2), MathUtil.interpolateSegment(1.f, 2.f, 3.f, 4.f, -.5f));
		assertEpsilonEquals(new Point2f(1,2), MathUtil.interpolateSegment(1.f, 2.f, 3.f, 4.f, 0.f));
		assertEpsilonEquals(new Point2f(1.5f,2.5f), MathUtil.interpolateSegment(1.f, 2.f, 3.f, 4.f, .25f));
		assertEpsilonEquals(new Point2f(2,3.f), MathUtil.interpolateSegment(1.f, 2.f, 3.f, 4.f, .5f));
		assertEpsilonEquals(new Point2f(2.5f,3.5f), MathUtil.interpolateSegment(1.f, 2.f, 3.f, 4.f, .75f));
		assertEpsilonEquals(new Point2f(3,4), MathUtil.interpolateSegment(1.f, 2.f, 3.f, 4.f, 1.f));
		assertEpsilonEquals(new Point2f(3,4), MathUtil.interpolateSegment(1.f, 2.f, 3.f, 4.f, 1.5f));
		assertEpsilonEquals(new Point2f(3,4), MathUtil.interpolateSegment(1.f, 2.f, 3.f, 4.f, 2.f));
	}
	
	
	
	/**
	 */
	public void testClosestPointSegmentSegment() {
		Point3f nearest1 = new Point3f();
		Point3f nearest2 = new Point3f();
		OutputParameter<Float> s = new OutputParameter<Float>();
		OutputParameter<Float> t = new OutputParameter<Float>();
		
		float d = MathUtil.closestPointSegmentSegment(new Point3f(-3,3,0), new Point3f(-1,1,0), new Point3f(3,3,0), new Point3f(1,1,0), s, t, nearest1, nearest2);
		assertEpsilonEquals(4.0f,d);		
		assertEpsilonEquals(new Point3f(-1,1,0),nearest1);
		assertEpsilonEquals(new Point3f(1,1,0),nearest2);
		
		d = MathUtil.closestPointSegmentSegment(new Point3f(-4f,5f,0), new Point3f(1,5f,0), new Point3f(-3.5f,7f,0), new Point3f(-1,6f,0), s, t, nearest1, nearest2);
		assertEpsilonEquals(1.0f,d);		
		assertEpsilonEquals(new Point3f(-1,5f,0),nearest1);
		assertEpsilonEquals(new Point3f(-1,6f,0),nearest2);
				
		d = MathUtil.closestPointSegmentSegment(new Point3f(-1,-3.5f,0), new Point3f(1.5f,-3.5f,0), new Point3f(1,-1,0), new Point3f(4f,-4f,0), s, t, nearest1, nearest2);
		assertEpsilonEquals(2.0f,d);		
		assertEpsilonEquals(new Point3f(1.5f,-3.5f,0),nearest1);
		assertEpsilonEquals(new Point3f(2.5f,-2.5f,0),nearest2);
				
		d = MathUtil.closestPointSegmentSegment(new Point3f(-4f,-1,0), new Point3f(-1,-2.5f,0), new Point3f(-5f,-2,0), new Point3f(-1,-1,0), s, t, nearest1, nearest2);
		assertEpsilonEquals(0.0f,d);		
		assertEpsilonEquals(new Point3f(-3,-1.5f,0),nearest1);
		assertEpsilonEquals(new Point3f(-3,-1.5f,0),nearest2);
	}

}
