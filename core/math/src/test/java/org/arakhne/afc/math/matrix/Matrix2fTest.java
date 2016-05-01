/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math.matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public class Matrix2fTest extends AbstractMathTestCase {
	
	@Rule
	public CoordinateSystem2DTestRule csTestRule = new CoordinateSystem2DTestRule();

	@Test
	public void toStringTest() {
		Matrix2f matrix = new Matrix2f(0,1,2,3);
		String s = "0.0, 1.0\n2.0, 3.0\n"; //$NON-NLS-1$
				
		assertEquals(s, matrix.toString());
	}
	
	@Test
	public void setIdentity() {
		Matrix2f matrix = this.randomMatrix2f();
		matrix.setIdentity();
		
		assertEpsilonEquals(new Matrix2f(1,0,0,1), matrix);
	}
	
	@Test
	public void addDouble() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		double s = this.random.nextDouble();
		
		m2.set(m2.m00+s, m2.m01+s, m2.m10+s, m2.m11+s);
		m1.add(s);
		
		assertEpsilonEquals(m2, m1);
	}
	
	@Test
	public void addDoubleMatrix2D() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		double s = this.random.nextDouble();
		
		m2.set(m2.m00+s, m2.m01+s, m2.m10+s, m2.m11+s);
		m1.add(s,m1);
		
		assertEpsilonEquals(m2, m1);
	}
	
	@Test
	public void addMatrix2DMatrix2D() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f m3 = new Matrix2f();
		
		m3.set(m1.m00+m2.m00, m1.m01+m2.m01, m1.m10+m2.m10, m1.m11+m2.m11);
		m1.add(m1,m2);
		
		assertEpsilonEquals(m3, m1);
	}
	
	@Test
	public void addMatrix2D() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f m3 = new Matrix2f();
		
		m3.set(m1.m00+m2.m00, m1.m01+m2.m01, m1.m10+m2.m10, m1.m11+m2.m11);
		m1.add(m2);
		
		assertEpsilonEquals(m3, m1);
	}
	
	@Test
	public void subMatrix2DMatrix2D() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f m3 = new Matrix2f();
		
		m3.set(m1.m00-m2.m00, m1.m01-m2.m01, m1.m10-m2.m10, m1.m11-m2.m11);
		m1.sub(m1,m2);
		
		assertEpsilonEquals(m3, m1);
	}
	
	@Test
	public void subMatrix2D() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f m3 = new Matrix2f();
		
		m3.set(m1.m00-m2.m00, m1.m01-m2.m01, m1.m10-m2.m10, m1.m11-m2.m11);
		m1.sub(m2);
		
		assertEpsilonEquals(m3, m1);
	}
	
	@Test
	public void transpose() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f transpose = new Matrix2f();
		
		double [] v = new double[3];
		
		m1.getRow(0, v);
		transpose.setColumn(0, v);
		
		m1.getRow(1, v);
		transpose.setColumn(1, v);
		
		m1.transpose();
		
		assertEpsilonEquals(transpose,m1);
	}
	
	@Test
	public void transposeMatrix2D() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f transpose = new Matrix2f();
		
		double [] v = new double[3];
		
		m1.getRow(0, v);
		transpose.setColumn(0, v);
		
		m1.getRow(1, v);
		transpose.setColumn(1, v);
		
		m1.transpose(m1);
		
		assertEpsilonEquals(transpose, m1);
	}
	
	@Test
	public void determinant() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		
		Matrix2f matrix = new Matrix2f(a,b,c,d);
		
		double determinant = a*d-c*b;
		
		assertEpsilonEquals(determinant, matrix.determinant());
	}
	
	@Test
	public void mulDouble() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		
		Matrix2f matrix = new Matrix2f(a,b,c,d);
		
		double s = this.random.nextDouble();
		Matrix2f prodScal = new Matrix2f(a*s,b*s,c*s,d*s);
		
		matrix.mul(s);
		assertEpsilonEquals(prodScal, matrix);
	}
	
	@Test
	public void mulDoubleMatrix2D() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		
		Matrix2f matrix = new Matrix2f(a,b,c,d);
		
		double s = this.random.nextDouble();
		Matrix2f prodScal = new Matrix2f(a*s,b*s,c*s,d*s);
		
		matrix.mul(s,matrix);
		assertEpsilonEquals(prodScal, matrix);
	}
	
	@Test
	public void mulMatrix2D() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		
		//double i = this.random.nextDouble();
		Matrix2f m1 = new Matrix2f(a,b,c,d);
		
		double e = this.random.nextDouble();
		double f = this.random.nextDouble();
		double g = this.random.nextDouble();
		double h = this.random.nextDouble();
		
		Matrix2f m2= new Matrix2f(e,f,g,h);
		
		Matrix2f prod = new Matrix2f(
				a*e+b*g,
				a*f+b*h,
				c*e+d*g,
				c*f+d*h);
				
		m1.mul(m2);
		
		assertEpsilonEquals(prod, m1);
	}
	
	@Test
	public void mulVector2D() {
		Vector2d vector = this.randomVector2f();
		Matrix2f matrix = new Matrix2f(1,2,4,-1);
		
		Vector2d product = new Vector2d(vector.getX()+2*vector.getY(),4*vector.getX()-vector.getY());
		
		Vector2d result = new Vector2d();
		matrix.mul(vector, result);
		
		assertEpsilonEquals(product, result);
	}
	
	@Test
	public void mulMatrix2DMatrix2D() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		
		//double i = this.random.nextDouble();
		Matrix2f m1 = new Matrix2f(a,b,c,d);
		
		double e = this.random.nextDouble();
		double f = this.random.nextDouble();
		double g = this.random.nextDouble();
		double h = this.random.nextDouble();
		
		Matrix2f m2= new Matrix2f(e,f,g,h);
		
		Matrix2f prod = new Matrix2f(
				a*e+b*g,
				a*f+b*h,
				c*e+d*g,
				c*f+d*h);
				
		m1.mul(m1,m2);
		
		assertEpsilonEquals(prod, m1);
	}
	
	@Test
	public void mulTransposeBoth() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f multrans = new Matrix2f();
		
		multrans.mulTransposeBoth(m1, m2);
		m1.transpose();
		m2.transpose();
		m1.mul(m2);
		
		assertEpsilonEquals(multrans, m1);
	}
	
	@Test
	public void mulTransposeRight() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f multrans = new Matrix2f();
		
		multrans.mulTransposeRight(m1, m2);
		//m1.transpose();
		m2.transpose();
		m1.mul(m2);
		
		assertEpsilonEquals(multrans, m1);
	}
	
	@Test
	public void mulTransposeLeft() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f multrans = new Matrix2f();
		
		multrans.mulTransposeLeft(m1, m2);
		m1.transpose();
		//m2.transpose();
		m1.mul(m2);
		
		assertEpsilonEquals(multrans, m1);
	}
	
	@Test
	public void normalizeCP_zero() {
		Matrix2f m = new Matrix2f();
		m.normalizeCP();
		assertNaN(m.getM00());
		assertNaN(m.getM01());
		assertNaN(m.getM10());
		assertNaN(m.getM10());
	}
	
	@Test
	public void normalizeCP_identity() {
		Matrix2f m = new Matrix2f();
		m.setIdentity();
		m.normalizeCP();
		assertEpsilonEquals(1, m.getM00());
		assertEpsilonEquals(0, m.getM01());
		assertEpsilonEquals(0, m.getM10());
		assertEpsilonEquals(1, m.getM11());
	}

	@Test
	public void normalizeCP_std() {
		Matrix2f m = new Matrix2f(1, 2, 3, 4);
		m.normalizeCP();
		assertEpsilonEquals(1/Math.sqrt(10), m.getM00());
		assertEpsilonEquals(2/Math.sqrt(20), m.getM01());
		assertEpsilonEquals(3/Math.sqrt(10), m.getM10());
		assertEpsilonEquals(4/Math.sqrt(20), m.getM11());
	}

	@Test
	public void normalizeCPMatrix2D_zero() {
		Matrix2f m = new Matrix2f();
		Matrix2f r = new Matrix2f();
		r.normalizeCP(m);
		assertNaN(r.getM00());
		assertNaN(r.getM01());
		assertNaN(r.getM00());
		assertNaN(r.getM01());
	}
	
	@Test
	public void normalizeCPMatrix2D_identity() {
		Matrix2f m = new Matrix2f();
		m.setIdentity();
		Matrix2f r = new Matrix2f();
		r.normalizeCP(m);
		assertEpsilonEquals(1, r.getM00());
		assertEpsilonEquals(0, r.getM01());
		assertEpsilonEquals(0, r.getM10());
		assertEpsilonEquals(1, r.getM11());
	}

	@Test
	public void normalizeCPMatrix2D_std() {
		Matrix2f m = new Matrix2f(1, 2, 3, 4);
		Matrix2f r = new Matrix2f();
		r.normalizeCP(m);
		assertEpsilonEquals(1/Math.sqrt(10), r.getM00());
		assertEpsilonEquals(2/Math.sqrt(20), r.getM01());
		assertEpsilonEquals(3/Math.sqrt(10), r.getM10());
		assertEpsilonEquals(4/Math.sqrt(20), r.getM11());
	}

	@Test
	public void setZero() {
		Matrix2f m = this.randomMatrix2f();
		m.setZero();
		assertEpsilonEquals(new Matrix2f(0,0,0,0), m);
	}
	
	@Test
	public void setDiagonal() {
		Matrix2f m = this.randomMatrix2f();
		double s = this.random.nextDouble();
		m.setDiagonal(s,s);
		assertEpsilonEquals(new Matrix2f(s,0,0,s), m);
	}
	
	@Test
	public void negate() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		Matrix2f temp = new Matrix2f();
		Matrix2f temp2 = new Matrix2f();
		
		m1.negate();
		temp.add(m1, m2);
		assertEpsilonEquals(new Matrix2f(0,0,0,0), temp);
		temp.mul(m1, m2);
		
		m2.negate();
		assertEpsilonEquals(m1, m2);
		
		m1.negate();
		temp2.mul(m1, m2);
		assertEpsilonEquals(temp, temp2);
	}
	
	@Test
	public void negateMatrix2D() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		Matrix2f temp = new Matrix2f();
		Matrix2f temp2 = new Matrix2f();
		
		m1.negate(m1);
		temp.add(m1, m2);
		assertEpsilonEquals(new Matrix2f(0,0,0,0), temp);
		temp.mul(m1, m2);
		
		m2.negate(m2);
		assertEpsilonEquals(m1, m2);
		
		m1.negate(m1);
		temp2.mul(m1, m2);
		assertEpsilonEquals(temp, temp2);
	}
	
	@Test
	public void cloneTest() {
		Matrix2f m = this.randomMatrix2f();
		Matrix2f m2 = m.clone();
		assertEpsilonEquals(m, m2);
	}
	
	@Test
	public void isSymmetric() {
		Matrix2f m = this.randomMatrix2f();
		boolean b = m.m01==m.m10;
		assertEquals(b, m.isSymmetric());
		
		m.setZero();
		assertTrue(m.isSymmetric());
		
		m.setIdentity();
		assertTrue(m.isSymmetric());
		
		m.set(1, 1, 2, 2);
		assertFalse(m.isSymmetric());
	}
	
	@Test
	public void eigenVectorsOfSymmetricMatrix_zero() {
		Matrix2f m = new Matrix2f();
		Matrix2f eigenVectors = new Matrix2f();
		double[] eigenValues = m.eigenVectorsOfSymmetricMatrix(eigenVectors);

		assertEpsilonEquals(0, eigenValues[0]);
		assertEpsilonEquals(0, eigenValues[1]);

		Vector2d vector1 = new Vector2d();
		Vector2d vector2 = new Vector2d();
		eigenVectors.getColumn(0, vector1);
		eigenVectors.getColumn(1, vector2);
		assertFpVectorEquals(1, 0, vector1);
		assertFpVectorEquals(0, 1, vector2);
	}
	
	@Test
	public void eigenVectorsOfSymmetricMatrix_identity() {
		Matrix2f m = new Matrix2f();
		m.setIdentity();
		Matrix2f eigenVectors = new Matrix2f();
		double[] eigenValues = m.eigenVectorsOfSymmetricMatrix(eigenVectors);

		assertEpsilonEquals(1, eigenValues[0]);
		assertEpsilonEquals(1, eigenValues[1]);

		Vector2d vector1 = new Vector2d();
		Vector2d vector2 = new Vector2d();
		eigenVectors.getColumn(0, vector1);
		eigenVectors.getColumn(1, vector2);
		assertFpVectorEquals(1, 0, vector1);
		assertFpVectorEquals(0, 1, vector2);
	}

	@Test
	public void eigenVectorsOfSymmetricMatrix_sym() {
		Matrix2f m = new Matrix2f(1, 5, 5, 2);
		Matrix2f eigenVectors = new Matrix2f();
		double[] eigenValues = m.eigenVectorsOfSymmetricMatrix(eigenVectors);
		
		assertEpsilonEquals(-3.5249, eigenValues[0]);
		assertEpsilonEquals(6.5249, eigenValues[1]);
		
		Vector2d vector1 = new Vector2d();
		Vector2d vector2 = new Vector2d();
		eigenVectors.getColumn(0, vector1);
		eigenVectors.getColumn(1, vector2);
		
		assertFpVectorEquals(0.74145, -0.67101, vector1);
		assertFpVectorEquals(0.67101, 0.74145, vector2);
	}

	@Test
	public void isIdentity() {
		Matrix2f m = this.randomMatrix2f();
		boolean b = m.m00==1 && m.m01==0 && m.m10==0 && m.m11==1;
		assertTrue(b==m.isIdentity());
		
		m.setIdentity();
		assertTrue(m.isIdentity());
		
		m.setZero();
		assertFalse(m.isIdentity());
		
		m.set(1, 1, 2, 2);
		assertFalse(m.isIdentity());
	}
	
	@Test
	public void covMatrix2Tuple2dArray_theory() {
		Vector2d v1 = new Vector2d(1, 3);
		Vector2d v2 = new Vector2d(4, -2);
		Vector2d m = new Vector2d();
		m.add(v1,v2);
		m.scale(.5f);
		
		Matrix2f expected = new Matrix2f();
		expected.m00 = ((v1.getX()-m.getX()) * (v1.getX()-m.getX()) + (v2.getX()-m.getX()) * (v2.getX()-m.getX())) / 2f;
		expected.m01 = ((v1.getX()-m.getX()) * (v1.getY()-m.getY()) + (v2.getX()-m.getX()) * (v2.getY()-m.getY())) / 2f;
		expected.m10 = ((v1.getY()-m.getY()) * (v1.getX()-m.getX()) + (v2.getY()-m.getY()) * (v2.getX()-m.getX())) / 2f;
		expected.m11 = ((v1.getY()-m.getY()) * (v1.getY()-m.getY()) + (v2.getY()-m.getY()) * (v2.getY()-m.getY())) / 2f;
		
		Matrix2f mat = new Matrix2f();
		Vector2d mean = new Vector2d(); 
		mat.cov(mean, v1, v2);
		
		assertEpsilonEquals(m, mean);
		for(int i=0; i<2; ++i) {
			for(int j=0; j<2; ++j) {
				assertEpsilonEquals("i="+i+"; j="+j, //$NON-NLS-1$ //$NON-NLS-2$
						expected.getElement(i, j), 
						mat.getElement(i, j));
			}
		}
	}
	
	/**
	 */
	@Test
	public void covMatrix2Tuple2dArray_example() {
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
		
		Point2d p1 = new Point2d(-1, -2);
		Point2d p2 = new Point2d(1, 0);
		Point2d p3 = new Point2d(2, -1);
		Point2d p4 = new Point2d(2, -1);
		
		Matrix2f cov = new Matrix2f();
		Vector2d mean = new Vector2d();
		cov.cov(mean, p1, p2, p3, p4);
		
		Point2d expectedMean = new Point2d(1, -1); 
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
	
	@Test
	public void operator_addMatrix2f() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f m3 = new Matrix2f();
		
		m3.set(m1.m00+m2.m00, m1.m01+m2.m01, m1.m10+m2.m10, m1.m11+m2.m11);
		m1.operator_add(m2);
		
		assertEpsilonEquals(m3, m1);
	}

	@Test
	public void operator_addDouble() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		double s = this.random.nextDouble() * 100;
		
		m2.set(m2.m00+s, m2.m01+s, m2.m10+s, m2.m11+s);
		m1.operator_add(s);
		
		assertEpsilonEquals(m2, m1);
	}

	@Test
	public void operator_removeMatrix2f() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f m3 = new Matrix2f();
		
		m3.set(m1.m00-m2.m00, m1.m01-m2.m01, m1.m10-m2.m10, m1.m11-m2.m11);
		m1.operator_remove(m2);
		
		assertEpsilonEquals(m3, m1);
	}

	@Test
	public void operator_removeDouble() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		double s = this.random.nextDouble() * 100;
		
		m2.set(m2.m00-s, m2.m01-s, m2.m10-s, m2.m11-s);
		m1.operator_remove(s);
		
		assertEpsilonEquals(m2, m1);
	}

	@Test
	public void operator_plusMatrix2f() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f m3 = new Matrix2f();
		
		m3.set(m1.m00+m2.m00, m1.m01+m2.m01, m1.m10+m2.m10, m1.m11+m2.m11);
		Matrix2f r = m1.operator_plus(m2);
		
		assertEpsilonEquals(m3, r);
	}

	@Test
	public void operator_plusDouble() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		double s = this.random.nextDouble() * 100;
		
		m2.set(m2.m00+s, m2.m01+s, m2.m10+s, m2.m11+s);
		Matrix2f r = m1.operator_plus(s);
		
		assertEpsilonEquals(m2, r);
	}

	@Test
	public void operator_minusMatrix2f() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f m3 = new Matrix2f();
		
		m3.set(m1.m00-m2.m00, m1.m01-m2.m01, m1.m10-m2.m10, m1.m11-m2.m11);
		Matrix2f r = m1.operator_minus(m2);
		
		assertEpsilonEquals(m3, r);
	}

	@Test
	public void operator_minusDouble() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		double s = this.random.nextDouble() * 100;
		
		m2.set(m2.m00-s, m2.m01-s, m2.m10-s, m2.m11-s);
		Matrix2f r = m1.operator_minus(s);
		
		assertEpsilonEquals(m2, r);
	}

	@Test
	public void operator_minus() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		
		m2.set(-m2.m00, -m2.m01, -m2.m10, -m2.m11);
		Matrix2f r = m1.operator_minus();
		assertEpsilonEquals(m2, r);
	}

	@Test
	public void operator_multiplyMatrix2f() {
		double a = this.random.nextDouble() * 100;
		double b = this.random.nextDouble() * 100;
		double c = this.random.nextDouble() * 100;
		double d = this.random.nextDouble() * 100;
		
		//double i = this.random.nextDouble();
		Matrix2f m1 = new Matrix2f(a,b,c,d);
		
		double e = this.random.nextDouble() * 100;
		double f = this.random.nextDouble() * 100;
		double g = this.random.nextDouble() * 100;
		double h = this.random.nextDouble() * 100;
		
		Matrix2f m2= new Matrix2f(e,f,g,h);
		
		Matrix2f prod = new Matrix2f(
				a*e+b*g,
				a*f+b*h,
				c*e+d*g,
				c*f+d*h);
				
		Matrix2f r = m1.operator_multiply(m2);
		
		assertEpsilonEquals(prod, r);
	}

	@Test
	public void operator_multiplyDouble() {
		double a = this.random.nextDouble() * 100;
		double b = this.random.nextDouble() * 100;
		double c = this.random.nextDouble() * 100;
		double d = this.random.nextDouble() * 100;
		
		Matrix2f matrix = new Matrix2f(a,b,c,d);
		
		double s = this.random.nextDouble() * 100;
		Matrix2f prodScal = new Matrix2f(a*s,b*s,c*s,d*s);
		
		Matrix2f r = matrix.operator_multiply(s);
		assertEpsilonEquals(prodScal, r);
	}

	@Test
	public void operator_divideDouble() {
		double a = this.random.nextDouble() * 100;
		double b = this.random.nextDouble() * 100;
		double c = this.random.nextDouble() * 100;
		double d = this.random.nextDouble() * 100;
		
		Matrix2f matrix = new Matrix2f(a,b,c,d);
		
		double s = this.random.nextDouble() * 100;
		Matrix2f prodScal = new Matrix2f(a/s,b/s,c/s,d/s);
		
		Matrix2f r = matrix.operator_divide(s);
		assertEpsilonEquals(prodScal, r);
	}

	@Test
	public void operator_plusPlus() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		
		m2.set(m2.m00+1, m2.m01+1, m2.m10+1, m2.m11+1);
		m1.operator_plusPlus();
		
		assertEpsilonEquals(m2, m1);
	}

	@Test
	public void operator_moinsMoins() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		
		m2.set(m2.m00-1, m2.m01-1, m2.m10-1, m2.m11-1);
		m1.operator_moinsMoins();
		
		assertEpsilonEquals(m2, m1);
	}

	@Test
	public void operator_not() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f transpose = new Matrix2f();
		
		double [] v = new double[3];
		
		m1.getRow(0, v);
		transpose.setColumn(0, v);
		
		m1.getRow(1, v);
		transpose.setColumn(1, v);
		
		Matrix2f r = m1.operator_not();
		
		assertEpsilonEquals(transpose, r);
	}

}