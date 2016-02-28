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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.continuous.Point2f;
import org.arakhne.afc.math.geometry.d2.continuous.Tuple2f;
import org.arakhne.afc.math.geometry.d2.continuous.Vector2f;
import org.junit.Test;

/**
 * @author $Author: galland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("static-method")
public class Matrix2fTest extends AbstractMathTestCase {
	
	
	
	@Test
	public void toStringTest() {
		Matrix2f matrix = new Matrix2f(0,1,2,3);
		String s = "0.0, 1.0\n2.0, 3.0\n"; //$NON-NLS-1$
				
		assertTrue(s.equals(matrix.toString()));
	}
	
	@Test
	public void setIdentity() {
		Matrix2f matrix = this.randomMatrix2f();
		matrix.setIdentity();
		
		assertTrue((new Matrix2f(1,0,0,1)).equals(matrix));
	}
	
	@Test
	public void addDouble() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		double s = this.random.nextDouble();
		
		m2.set(m2.m00+s, m2.m01+s, m2.m10+s, m2.m11+s);
		m1.add(s);
		
		assertTrue(m2.equals(m1));
	}
	
	@Test
	public void addDoubleMatrix2D() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		double s = this.random.nextDouble();
		
		m2.set(m2.m00+s, m2.m01+s, m2.m10+s, m2.m11+s);
		m1.add(s,m1);
		
		assertTrue(m2.equals(m1));
	}
	
	@Test
	public void addMatrix2DMatrix2D() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f m3 = new Matrix2f();
		
		m3.set(m1.m00+m2.m00, m1.m01+m2.m01, m1.m10+m2.m10, m1.m11+m2.m11);
		m1.add(m1,m2);
		
		assertTrue(m3.equals(m1));
	}
	
	@Test
	public void addMatrix2D() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f m3 = new Matrix2f();
		
		m3.set(m1.m00+m2.m00, m1.m01+m2.m01, m1.m10+m2.m10, m1.m11+m2.m11);
		m1.add(m2);
		
		assertTrue(m3.equals(m1));
	}
	
	@Test
	public void subMatrix2DMatrix2D() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f m3 = new Matrix2f();
		
		m3.set(m1.m00-m2.m00, m1.m01-m2.m01, m1.m10-m2.m10, m1.m11-m2.m11);
		m1.sub(m1,m2);
		
		assertTrue(m3.equals(m1));
	}
	
	@Test
	public void subMatrix2D() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = this.randomMatrix2f();
		Matrix2f m3 = new Matrix2f();
		
		m3.set(m1.m00-m2.m00, m1.m01-m2.m01, m1.m10-m2.m10, m1.m11-m2.m11);
		m1.sub(m2);
		
		assertTrue(m3.equals(m1));
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
		
		assertTrue(transpose.equals(m1));
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
		
		assertTrue(transpose.equals(m1));
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
		assertTrue(prodScal.equals(matrix));
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
		assertTrue(prodScal.equals(matrix));
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
		
		assertTrue(prod.equals(m1));
	}
	
	@Test
	public void mulVector2D() {
		Vector2f vector = this.randomVector2f();
		Matrix2f matrix = new Matrix2f(1,2,4,-1);
		
		Vector2f product = new Vector2f(vector.getX()+2*vector.getY(),4*vector.getX()-vector.getY());
		
		
		assertTrue(product.equals(matrix.mul(vector)));
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
		
		assertTrue(prod.equals(m1));
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
		
		assertTrue(multrans.equals(m1));
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
		
		assertTrue(multrans.equals(m1));
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
		
		assertTrue(multrans.equals(m1));
	}
	
	@Test
	public void normalizeCP() {
		// it's okay for this one
	}
	
	@Test
	public void normalizeCPMatrix2D() {
		// it's okay for this one
	}
	
	@Test
	public void setZero() {
		Matrix2f m = this.randomMatrix2f();
		m.setZero();
		assertTrue(m.equals(new Matrix2f(0,0,0,0)));
	}
	
	@Test
	public void setDiagonal() {
		Matrix2f m = this.randomMatrix2f();
		double s = this.random.nextDouble();
		m.setDiagonal(s,s);
		assertTrue(m.equals(new Matrix2f(s,0,0,s)));
	}
	
	@Test
	public void negate() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		Matrix2f temp = new Matrix2f();
		Matrix2f temp2 = new Matrix2f();
		
		m1.negate();
		temp.add(m1, m2);
		assertTrue(temp.equals(new Matrix2f(0,0,0,0)));
		temp.mul(m1, m2);
		
		m2.negate();
		assertTrue(m1.equals(m2));
		
		m1.negate();
		temp2.mul(m1, m2);
		assertTrue(temp.equals(temp2));
	}
	
	@Test
	public void negateMatrix2D() {
		Matrix2f m1 = this.randomMatrix2f();
		Matrix2f m2 = m1.clone();
		Matrix2f temp = new Matrix2f();
		Matrix2f temp2 = new Matrix2f();
		
		m1.negate(m1);
		temp.add(m1, m2);
		assertTrue(temp.equals(new Matrix2f(0,0,0,0)));
		temp.mul(m1, m2);
		
		m2.negate(m2);
		assertTrue(m1.equals(m2));
		
		m1.negate(m1);
		temp2.mul(m1, m2);
		assertTrue(temp.equals(temp2));
	}
	
	@Test
	public void cloneTest() {
		Matrix2f m = this.randomMatrix2f();
		Matrix2f m2 = m.clone();
		assertTrue(m.equals(m2));
	}
	
	@Test
	public void isSymmetric() {
		Matrix2f m = this.randomMatrix2f();
		boolean b = m.m01==m.m10;
		assertTrue(b==m.isSymmetric());
		
		m.setZero();
		assertTrue(m.isSymmetric());
		
		m.setIdentity();
		assertTrue(m.isSymmetric());
		
		m.set(1, 1, 2, 2);
		assertFalse(m.isSymmetric());
	}
	
	@Test
	public void eigenVectorsOfSymmetricMatrix() {
		throw new UnsupportedOperationException();
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
	
	
	
	/**
	 */
	@Test
	public void covMatrix2Tuple2dArray_theory() {
		Vector2f v1 = new Vector2f(1, 3);
		Vector2f v2 = new Vector2f(4, -2);
		Vector2f m = new Vector2f();
		m.add(v1,v2);
		m.scale(.5f);
		
		Matrix2f expected = new Matrix2f();
		expected.m00 = ((v1.getX()-m.getX()) * (v1.getX()-m.getX()) + (v2.getX()-m.getX()) * (v2.getX()-m.getX())) / 2f;
		expected.m01 = ((v1.getX()-m.getX()) * (v1.getY()-m.getY()) + (v2.getX()-m.getX()) * (v2.getY()-m.getY())) / 2f;
		expected.m10 = ((v1.getY()-m.getY()) * (v1.getX()-m.getX()) + (v2.getY()-m.getY()) * (v2.getX()-m.getX())) / 2f;
		expected.m11 = ((v1.getY()-m.getY()) * (v1.getY()-m.getY()) + (v2.getY()-m.getY()) * (v2.getY()-m.getY())) / 2f;
		
		Matrix2f mat = new Matrix2f();
		Tuple2f<?> mean = mat.cov(v1, v2);
		
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
		
		Point2f p1 = new Point2f(-1, -2);
		Point2f p2 = new Point2f(1, 0);
		Point2f p3 = new Point2f(2, -1);
		Point2f p4 = new Point2f(2, -1);
		
		Matrix2f cov = new Matrix2f();
		Tuple2f<?> mean = cov.cov(p1, p2, p3, p4);
		
		Point2f expectedMean = new Point2f(1, -1); 
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
	
}