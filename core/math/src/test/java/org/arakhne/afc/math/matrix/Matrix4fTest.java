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

import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.junit.Test;

/**
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Matrix4fTest extends AbstractMathTestCase{
	
	@Test
	public void toStringTest() {
		Matrix4f matrix = new Matrix4f(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
		String s = "0.0, 1.0, 2.0, 3.0\n4.0, 5.0, 6.0, 7.0\n8.0, 9.0, 10.0, 11.0\n12.0, 13.0, 14.0, 15.0\n";
				
		assertTrue(s.equals(matrix.toString()));
	}
	
	@Test
	public void setIdentity() {
		Matrix4f matrix = this.randomMatrix4f();
		matrix.setIdentity();
		
		assertTrue((new Matrix4f(1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1)).equals(matrix));
	}
	
	@Test
	public void addDouble() {
		Matrix4f m1 = this.randomMatrix4f();
		Matrix4f m2 = m1.clone();
		double s = this.random.nextDouble();
		
		m2.set(m2.m00+s, m2.m01+s, m2.m02+s, m2.m03+s, m2.m10+s, m2.m11+s, m2.m12+s, m2.m13+s, m2.m20+s, m2.m21+s, m2.m22+s, m2.m23+s, m2.m30+s, m2.m31+s, m2.m32+s, m2.m33+s);
		m1.add(s);
		
		assertTrue(m2.equals(m1));
	}
	
	@Test
	public void addDoubleMatrix4D() {
		Matrix4f m1 = this.randomMatrix4f();
		Matrix4f m2 = m1.clone();
		double s = this.random.nextDouble();
		
		m2.set(m2.m00+s, m2.m01+s, m2.m02+s, m2.m03+s, m2.m10+s, m2.m11+s, m2.m12+s, m2.m13+s, m2.m20+s, m2.m21+s, m2.m22+s, m2.m23+s, m2.m30+s, m2.m31+s, m2.m32+s, m2.m33+s);
		m1.add(s,m1);
		
		assertTrue(m2.equals(m1));
	}
	
	@Test
	public void addMatrix4DMatrix4D() {
		Matrix4f m1 = this.randomMatrix4f();
		Matrix4f m2 = this.randomMatrix4f();
		Matrix4f m3 = m2.clone();
		
		m2.set(m2.m00+m1.m00, m2.m01+m1.m01, m2.m02+m1.m02, m2.m03+m1.m03, m2.m10+m1.m10, m2.m11+m1.m11, m2.m12+m1.m12, m2.m13+m1.m13, m2.m20+m1.m20, m2.m21+m1.m21, m2.m22+m1.m22, m2.m23+m1.m23, m2.m30+m1.m30, m2.m31+m1.m31, m2.m32+m1.m32, m2.m33+m1.m33);
		
		m3.add(m1,m3);
		
		assertTrue(m2.equals(m3));
	}
	
	@Test
	public void addMatrix4D() {
		Matrix4f m1 = this.randomMatrix4f();
		Matrix4f m2 = this.randomMatrix4f();
		Matrix4f m3 = m2.clone();
		
		m2.set(m2.m00+m1.m00, m2.m01+m1.m01, m2.m02+m1.m02, m2.m03+m1.m03, m2.m10+m1.m10, m2.m11+m1.m11, m2.m12+m1.m12, m2.m13+m1.m13, m2.m20+m1.m20, m2.m21+m1.m21, m2.m22+m1.m22, m2.m23+m1.m23, m2.m30+m1.m30, m2.m31+m1.m31, m2.m32+m1.m32, m2.m33+m1.m33);
		
		m3.add(m1);
		
		assertTrue(m2.equals(m3));
	}
	
	@Test
	public void subMatrix4DMatrix4D() {
		Matrix4f m1 = this.randomMatrix4f();
		Matrix4f m2 = this.randomMatrix4f();
		Matrix4f m3 = m2.clone();
		
		m2.set(m2.m00-m1.m00, m2.m01-m1.m01, m2.m02-m1.m02, m2.m03-m1.m03, m2.m10-m1.m10, m2.m11-m1.m11, m2.m12-m1.m12, m2.m13-m1.m13, m2.m20-m1.m20, m2.m21-m1.m21, m2.m22-m1.m22, m2.m23-m1.m23, m2.m30-m1.m30, m2.m31-m1.m31, m2.m32-m1.m32, m2.m33-m1.m33);
		
		m3.sub(m3,m1);
		
		assertTrue(m2.equals(m3));
	}
	
	@Test
	public void subMatrix4D() {
		Matrix4f m1 = this.randomMatrix4f();
		Matrix4f m2 = this.randomMatrix4f();
		Matrix4f m3 = m2.clone();
		
		m2.set(m2.m00-m1.m00, m2.m01-m1.m01, m2.m02-m1.m02, m2.m03-m1.m03, m2.m10-m1.m10, m2.m11-m1.m11, m2.m12-m1.m12, m2.m13-m1.m13, m2.m20-m1.m20, m2.m21-m1.m21, m2.m22-m1.m22, m2.m23-m1.m23, m2.m30-m1.m30, m2.m31-m1.m31, m2.m32-m1.m32, m2.m33-m1.m33);
		
		m3.sub(m1);
		
		assertTrue(m2.equals(m3));
	}
	
	@Test
	public void transpose() {
		Matrix4f m1 = this.randomMatrix4f();
		Matrix4f transpose = new Matrix4f();
		
		double [] v = new double[4];
		
		m1.getRow(0, v);
		transpose.setColumn(0, v);
		
		m1.getRow(1, v);
		transpose.setColumn(1, v);
		
		m1.getRow(2, v);
		transpose.setColumn(2, v);
		
		m1.getRow(3, v);
		transpose.setColumn(3, v);
		
		m1.transpose();
		
		assertTrue(transpose.equals(m1));
	}
	
	@Test
	public void transposeMatrix4D() {
		Matrix4f m1 = this.randomMatrix4f();
		Matrix4f transpose = new Matrix4f();
		
		double [] v = new double[4];
		
		m1.getRow(0, v);
		transpose.setColumn(0, v);
		
		m1.getRow(1, v);
		transpose.setColumn(1, v);
		
		m1.getRow(2, v);
		transpose.setColumn(2, v);
		
		m1.getRow(3, v);
		transpose.setColumn(3, v);
		
		m1.transpose(m1);
		
		assertTrue(transpose.equals(m1));
	}
	
	@Test
	public void determinant() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		double e = this.random.nextDouble();
		double f = this.random.nextDouble();
		double g = this.random.nextDouble();
		double h = this.random.nextDouble();
		double i = this.random.nextDouble();
		double j = this.random.nextDouble();
		double k = this.random.nextDouble();
		double l = this.random.nextDouble();
		double m = this.random.nextDouble();
		double n = this.random.nextDouble();
		double o = this.random.nextDouble();
		double p = this.random.nextDouble();
		Matrix4f m1 = new Matrix4f(a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p);
		
		
		double determinant = d*g*j*m - c*h*j*m - d*f*k*m + b*h*k*m + c*f*l*m - b*g*l*m - d*g*i*n + c*h*i*n + d*e*k*n - a*h*k*n - c*e*l*n + a*g*l*n + d*f*i*o - b*h*i*o - d*e*j*o + a*h*j*o + b*e*l*o - a*f*l*o - c*f*i*p + b*g*i*p + c*e*j*p - a*g*j*p - b*e*k*p + a*f*k*p;
	
		assertEpsilonEquals(determinant,m1.determinant());
	}
	
	@Test
	public void mulDouble() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void mulDoubleMatrix4D() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void mulMatrix4D() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void mulMatrix4DMatrix4D() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void equals() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void setZero() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void setDiagonalDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void negate() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void negateMatrix4D() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void testClone() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void isSymmetric() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void isIdentity() {
		throw new UnsupportedOperationException();
	}
	
}