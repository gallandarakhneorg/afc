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

import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Tuple2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;
import org.arakhne.afc.math.geometry.continuous.object3d.Point3f;
import org.arakhne.afc.math.geometry.continuous.object3d.Tuple3f;
import org.arakhne.afc.math.geometry.continuous.object3d.Vector3f;
import org.arakhne.afc.math.matrix.Matrix2f;
import org.arakhne.afc.math.matrix.Matrix3f;
import org.arakhne.util.ref.AbstractTestCase;


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
		assertNaN(MathUtil.max((Float) null));
		assertNaN(MathUtil.max(new float[0]));
		assertEquals(3455.f, MathUtil.max(3.f, 5.f, 7.f, 8.f, 3455.f, 3245.f, 45.f, 0.f, -10.f, 45.f));
	}

	/**
	 */
	public void testMinFloatArray() {
		assertNaN(MathUtil.min((Float) null));
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
	public void testDeterminantFloatFloatFloatFloatFloatFloat() {
		/* det(A,B) = det( [ x1 x2 1 ]
		 *                 [ y1 y2 1 ]
		 *                 [ z1 z2 1 ] )
		 */
		Vector3f v1, v2;
		
		v1 = new Vector3f(0, 0, 0);
		v2 = new Vector3f(0, 0, 0);
		assertEpsilonEquals(0, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ()));

		v1 = new Vector3f(1, 0, 0);
		v2 = new Vector3f(0, 1, 0);
		assertEpsilonEquals(1, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ()));

		v1 = new Vector3f(0, 1, 0);
		v2 = new Vector3f(0, 0, 1);
		assertEpsilonEquals(1, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ()));

		v1 = new Vector3f(0, 0, 1);
		v2 = new Vector3f(1, 0, 0);
		assertEpsilonEquals(1, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ()));

		v1 = new Vector3f(0, 1, 0);
		v2 = new Vector3f(1, 0, 0);
		assertEpsilonEquals(-1, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ()));

		v1 = new Vector3f(0, 0, 1);
		v2 = new Vector3f(0, 1, 0);
		assertEpsilonEquals(-1, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ()));

		v1 = new Vector3f(1, 3, -5);
		v2 = new Vector3f(4, -2, -1);
		assertEpsilonEquals(-46, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ()));
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
	public void testDeterminantFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		Vector3f v1, v2, v3;
		
		v1 = new Vector3f(0, 0, 0);
		v2 = new Vector3f(0, 0, 0);
		v3 = new Vector3f(1, 1, 1);
		assertEpsilonEquals(0, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), v3.getX(), v3.getY(), v3.getZ()));

		v1 = new Vector3f(1, 0, 0);
		v2 = new Vector3f(0, 1, 0);
		v3 = new Vector3f(1, 1, 1);
		assertEpsilonEquals(1, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), v3.getX(), v3.getY(), v3.getZ()));

		v1 = new Vector3f(0, 1, 0);
		v2 = new Vector3f(0, 0, 1);
		v3 = new Vector3f(1, 1, 1);
		assertEpsilonEquals(1, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), v3.getX(), v3.getY(), v3.getZ()));

		v1 = new Vector3f(0, 0, 1);
		v2 = new Vector3f(1, 0, 0);
		v3 = new Vector3f(1, 1, 1);
		assertEpsilonEquals(1, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), v3.getX(), v3.getY(), v3.getZ()));

		v1 = new Vector3f(0, 1, 0);
		v2 = new Vector3f(1, 0, 0);
		v3 = new Vector3f(1, 1, 1);
		assertEpsilonEquals(-1, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), v3.getX(), v3.getY(), v3.getZ()));

		v1 = new Vector3f(0, 0, 1);
		v2 = new Vector3f(0, 1, 0);
		v3 = new Vector3f(1, 1, 1);
		assertEpsilonEquals(-1, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), v3.getX(), v3.getY(), v3.getZ()));

		v1 = new Vector3f(1, 3, -5);
		v2 = new Vector3f(4, -2, -1);
		v3 = new Vector3f(1, 1, 1);
		assertEpsilonEquals(-46, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), v3.getX(), v3.getY(), v3.getZ()));

		v1 = new Vector3f(0, 0, 0);
		v2 = new Vector3f(0, 0, 0);
		v3 = new Vector3f(0, 0, 0);
		assertEpsilonEquals(0, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), v3.getX(), v3.getY(), v3.getZ()));
	
		v1 = new Vector3f(1, 0, 0);
		v2 = new Vector3f(0, 1, 0);
		v3 = new Vector3f(0, 0, 1);
		assertEpsilonEquals(1, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), v3.getX(), v3.getY(), v3.getZ()));

		v1 = new Vector3f(0, 0, 1);
		v2 = new Vector3f(0, 1, 0);
		v3 = new Vector3f(1, 0, 0);
		assertEpsilonEquals(-1, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), v3.getX(), v3.getY(), v3.getZ()));

		v1 = new Vector3f(0, 1, 0);
		v2 = new Vector3f(0, 0, 1);
		v3 = new Vector3f(0, 0, 0);
		assertEpsilonEquals(0, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), v3.getX(), v3.getY(), v3.getZ()));

		v1 = new Vector3f(0, 0, 0);
		v2 = new Vector3f(1, 0, 0);
		v3 = new Vector3f(0, 1, 0);
		assertEpsilonEquals(0, MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), v3.getX(), v3.getY(), v3.getZ()));
	}

	/**
	 */
	public void testClampFloatFloatFloat() {
		// NaN is lower than all the other floating-point values 
		assertNaN(MathUtil.clamp(0, Float.NaN, Float.NaN));
		
		float min = (this.RANDOM.nextFloat() - this.RANDOM.nextFloat()) * 1000.f;
		float max = min + this.RANDOM.nextFloat() * 1000.f;
		
		assertEpsilonEquals(Float.NEGATIVE_INFINITY, MathUtil.clamp(Float.NEGATIVE_INFINITY, Float.NaN, max));
		assertEpsilonEquals(max, MathUtil.clamp(Float.POSITIVE_INFINITY, Float.NaN, max));
		assertEpsilonEquals(max<0 ? max : max/2.f, MathUtil.clamp(max/2.f, Float.NaN, max));
		assertEpsilonEquals(max<0 ? max*2.f : max, MathUtil.clamp(max*2.f, Float.NaN, max));
		
		assertEpsilonEquals(min, MathUtil.clamp(Float.NEGATIVE_INFINITY, min, max));
		assertEpsilonEquals(max, MathUtil.clamp(Float.POSITIVE_INFINITY, min, max));
		assertEpsilonEquals((min+max)/2.f, MathUtil.clamp((min+max)/2.f, min, max));
		assertEpsilonEquals(max, MathUtil.clamp(Math.abs(max)*2.f, min, max));
	} 
		
	/**
	 */
	public void testCovMatrix2Tuple2dArray_example() {
		// From "Mathematics for 3D Game Programming and Computer Graphics" pp.220
		// Adapted to 2D by Stephane Galland
		//
		// P1 = [ -1, -2 ]
		// P2 = [ 1, 0 ]
		// P3 = [ 2, -1 ]
		// P4 = [ 2, -1 ]
		//
		// average: m = [ 1, -1 ]
		//
		// Cov = [ 1.5 ,  .5 ]
		//       [  .5 ,  .5 ]
		
		Point2f p1 = new Point2f(-1., -2.);
		Point2f p2 = new Point2f(1., 0.);
		Point2f p3 = new Point2f(2., -1.);
		Point2f p4 = new Point2f(2., -1.);
		
		Matrix2f cov = new Matrix2f();
		Tuple2f mean = MathUtil.cov(cov, p1, p2, p3, p4);
		
		Point2f expectedMean = new Point2f(1., -1.); 
		Matrix2f expectedCov = new Matrix2f();
		expectedCov.m00 = 1.5f;
		expectedCov.m01 = .5f;
		expectedCov.m10 = .5f;
		expectedCov.m11 = .5f;
		
		assertEpsilonEquals(expectedMean, mean);
		for(int i=0; i<2; ++i) {
			for(int j=0; j<2; ++j) {
				assertEpsilonEquals("i="+i+"; j="+j, //$NON-NLS-1$ //$NON-NLS-2$
						expectedCov.getElement(i, j), 
						cov.getElement(i, j));
			}
		}
	}

	/**
	 */
	public void testCovMatrix3fTuple3dArray_theory() {
		Vector3f v1 = new Vector3f(1, 3, -5);
		Vector3f v2 = new Vector3f(4, -2, -1);
		Vector3f m = new Vector3f();
		m.add(v1,v2);
		m.scale(.5f);
		
		Matrix3f expected = new Matrix3f();
		expected.m00 = ((v1.getX()-m.getX()) * (v1.getX()-m.getX()) + (v2.getX()-m.getX()) * (v2.getX()-m.getX())) / 2.f;
		expected.m01 = ((v1.getX()-m.getX()) * (v1.getY()-m.getY()) + (v2.getX()-m.getX()) * (v2.getY()-m.getY())) / 2.f;
		expected.m02 = ((v1.getX()-m.getX()) * (v1.getZ()-m.getZ()) + (v2.getX()-m.getX()) * (v2.getZ()-m.getZ())) / 2.f;
		expected.m10 = ((v1.getY()-m.getY()) * (v1.getX()-m.getX()) + (v2.getY()-m.getY()) * (v2.getX()-m.getX())) / 2.f;
		expected.m11 = ((v1.getY()-m.getY()) * (v1.getY()-m.getY()) + (v2.getY()-m.getY()) * (v2.getY()-m.getY())) / 2.f;
		expected.m12 = ((v1.getY()-m.getY()) * (v1.getZ()-m.getZ()) + (v2.getY()-m.getY()) * (v2.getZ()-m.getZ())) / 2.f;
		expected.m20 = ((v1.getZ()-m.getZ()) * (v1.getX()-m.getX()) + (v2.getZ()-m.getZ()) * (v2.getX()-m.getX())) / 2.f;
		expected.m21 = ((v1.getZ()-m.getZ()) * (v1.getY()-m.getY()) + (v2.getZ()-m.getZ()) * (v2.getY()-m.getY())) / 2.f;
		expected.m22 = ((v1.getZ()-m.getZ()) * (v1.getZ()-m.getZ()) + (v2.getZ()-m.getZ()) * (v2.getZ()-m.getZ())) / 2.f;
		
		Matrix3f mat = new Matrix3f();
		Tuple3f mean = MathUtil.cov(mat, v1, v2);
		
		assertEpsilonEquals(m, mean);
		for(int i=0; i<3; ++i) {
			for(int j=0; j<3; ++j) {
				assertEpsilonEquals("i="+i+"; j="+j, //$NON-NLS-1$ //$NON-NLS-2$
						expected.getElement(i, j), 
						mat.getElement(i, j));
			}
		}
	}
	
	/**
	 */
	public void testCovMatrix3fTuple3dArray_example() {
		// From "Mathematics for 3D Game Programming and Computer Graphics" pp.220
		//
		// P1 = [ -1, -2, 1 ]
		// P2 = [ 1, 0, 2 ]
		// P3 = [ 2, -1, 3 ]
		// P4 = [ 2, -1, 2 ]
		//
		// average: m = [ 1, -1, 2 ]
		//
		// Cov = [ 1.5 ,  .5 ,  .75 ]
		//       [  .5 ,  .5 ,  .25 ]
		//       [  .75,  .25,  .5  ]
		
		Point3f p1 = new Point3f(-1., -2., 1.);
		Point3f p2 = new Point3f(1., 0., 2.);
		Point3f p3 = new Point3f(2., -1., 3.);
		Point3f p4 = new Point3f(2., -1., 2.);
		
		Matrix3f cov = new Matrix3f();
		Tuple3f mean = MathUtil.cov(cov, p1, p2, p3, p4);
		
		Point3f expectedMean = new Point3f(1., -1., 2.); 
		Matrix3f expectedCov = new Matrix3f();
		expectedCov.m00 = 1.5f;
		expectedCov.m01 = .5f;
		expectedCov.m02 = .75f;
		expectedCov.m10 = .5f;
		expectedCov.m11 = .5f;
		expectedCov.m12 = .25f;
		expectedCov.m20 = .75f;
		expectedCov.m21 = .25f;
		expectedCov.m22 = .5f;
		
		assertEpsilonEquals(expectedMean, mean);
		for(int i=0; i<3; ++i) {
			for(int j=0; j<3; ++j) {
				assertEpsilonEquals("i="+i+"; j="+j, //$NON-NLS-1$ //$NON-NLS-2$
						expectedCov.getElement(i, j), 
						cov.getElement(i, j));
			}
		}
	}

	/**
	 */
	public void testCovMatrix3fdTuple3dArray_theory() {
		//
		// Test according to known theory
		//
		Vector3f v1 = new Vector3f(1, 3, -5);
		Vector3f v2 = new Vector3f(4, -2, -1);
		Vector3f m = new Vector3f();
		m.add(v1,v2);
		m.scale(.5f);
		
		Matrix3f expected = new Matrix3f();
		expected.m00 = ((v1.getX()-m.getX()) * (v1.getX()-m.getX()) + (v2.getX()-m.getX()) * (v2.getX()-m.getX())) / 2.f;
		expected.m01 = ((v1.getX()-m.getX()) * (v1.getY()-m.getY()) + (v2.getX()-m.getX()) * (v2.getY()-m.getY())) / 2.f;
		expected.m02 = ((v1.getX()-m.getX()) * (v1.getZ()-m.getZ()) + (v2.getX()-m.getX()) * (v2.getZ()-m.getZ())) / 2.f;
		expected.m10 = ((v1.getY()-m.getY()) * (v1.getX()-m.getX()) + (v2.getY()-m.getY()) * (v2.getX()-m.getX())) / 2.f;
		expected.m11 = ((v1.getY()-m.getY()) * (v1.getY()-m.getY()) + (v2.getY()-m.getY()) * (v2.getY()-m.getY())) / 2.f;
		expected.m12 = ((v1.getY()-m.getY()) * (v1.getZ()-m.getZ()) + (v2.getY()-m.getY()) * (v2.getZ()-m.getZ())) / 2.f;
		expected.m20 = ((v1.getZ()-m.getZ()) * (v1.getX()-m.getX()) + (v2.getZ()-m.getZ()) * (v2.getX()-m.getX())) / 2.f;
		expected.m21 = ((v1.getZ()-m.getZ()) * (v1.getY()-m.getY()) + (v2.getZ()-m.getZ()) * (v2.getY()-m.getY())) / 2.f;
		expected.m22 = ((v1.getZ()-m.getZ()) * (v1.getZ()-m.getZ()) + (v2.getZ()-m.getZ()) * (v2.getZ()-m.getZ())) / 2.f;
		
		Matrix3f mat = new Matrix3f();
		Tuple3f mean = MathUtil.cov(mat, v1, v2);
		
		assertEpsilonEquals(m, mean);
		for(int i=0; i<3; ++i) {
			for(int j=0; j<3; ++j) {
				assertEpsilonEquals("i="+i+"; j="+j, //$NON-NLS-1$ //$NON-NLS-2$
						expected.getElement(i, j), 
						mat.getElement(i, j));
			}
		}
	}

	/**
	 */
	public void testCovMatrix3fdTuple3dArray_example() {
		// From "Mathematics for 3D Game Programming and Computer Graphics" pp.220
		//
		// P1 = [ -1, -2, 1 ]
		// P2 = [ 1, 0, 2 ]
		// P3 = [ 2, -1, 3 ]
		// P4 = [ 2, -1, 2 ]
		//
		// average: m = [ 1, -1, 2 ]
		//
		// Cov = [ 1.5 ,  .5 ,  .75 ]
		//       [  .5 ,  .5 ,  .25 ]
		//       [  .75,  .25,  .5  ]
		
		Point3f p1 = new Point3f(-1., -2., 1.);
		Point3f p2 = new Point3f(1., 0., 2.);
		Point3f p3 = new Point3f(2., -1., 3.);
		Point3f p4 = new Point3f(2., -1., 2.);
		
		Matrix3f cov = new Matrix3f();
		Tuple3f mean = MathUtil.cov(cov, p1, p2, p3, p4);
		
		Point3f expectedMean = new Point3f(1., -1., 2.); 
		Matrix3f expectedCov = new Matrix3f();
		expectedCov.m00 = 1.5f;
		expectedCov.m01 = .5f;
		expectedCov.m02 = .75f;
		expectedCov.m10 = .5f;
		expectedCov.m11 = .5f;
		expectedCov.m12 = .25f;
		expectedCov.m20 = .75f;
		expectedCov.m21 = .25f;
		expectedCov.m22 = .5f;
		
		assertEpsilonEquals(expectedMean, mean);
		for(int i=0; i<3; ++i) {
			for(int j=0; j<3; ++j) {
				assertEpsilonEquals("i="+i+"; j="+j, //$NON-NLS-1$ //$NON-NLS-2$
						expectedCov.getElement(i, j), 
						cov.getElement(i, j));
			}
		}
	}
		
	/**
	 */
	public void testSolvesQuadraticPolynomialsFloatFloatFloat() {
		
		for(int testIndex=0; testIndex<1000; ++testIndex) {
			float a = this.RANDOM.nextFloat() * 1000.f;
			float b = this.RANDOM.nextFloat() * 1000.f;
			float c = this.RANDOM.nextFloat() * 1000.f;
			
			float[] roots = MathUtil.solvesQuadraticPolynomials(a, b, c);

			assertTrue(roots.length>=0 && roots.length<=2);

			setDecimalPrecision(5);
			for(int i=0; i<roots.length; ++i) {
				assertEpsilonEquals("root #"+i,0.f, //$NON-NLS-1$
						a * roots[i]*roots[i]
						+ b * roots[i]
						+ c);
			}
			setDefaultDecimalPrecision();
		}
	}

	/**
	 */
	public void testCurtFloat() {
		for(int testIndex=0; testIndex<1000; ++testIndex) {
			float n = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 1000.f;
			float cubicRoot = MathUtil.curt(n);
			assertEpsilonEquals(n, cubicRoot*cubicRoot*cubicRoot);
		}
	}

	/**
	 */
	public void testSolvesCubicPolynomialsFloatFloatFloat() {
		for(int testIndex=0; testIndex<1000; ++testIndex) {
			float a = this.RANDOM.nextFloat() * 10.f;
			float b = this.RANDOM.nextFloat() * 10.f;
			float c = this.RANDOM.nextFloat() * 10.f;
			
			float[] roots = MathUtil.solvesCubicPolynomials(a, b, c);
	
			assertTrue(roots.length>=1 && roots.length<=3);

			for(int i=0; i<roots.length; ++i) {
				assertEpsilonEquals("root #"+i,0f, //$NON-NLS-1$
						roots[i]*roots[i]*roots[i]
						+ a * roots[i]*roots[i]
						+ b * roots[i]
						+ c);
			}
		}
	}

}

