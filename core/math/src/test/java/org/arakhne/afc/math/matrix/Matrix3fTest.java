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

import static org.junit.Assert.*;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d3.continuous.Vector3f;
import org.junit.Test;

public class Matrix3fTest extends AbstractMathTestCase{

	@Test
	public void toStringTest() {
		Matrix3f matrix = new Matrix3f(0,1,2,3,4,5,6,7,8);
		String s = "0.0, 1.0, 2.0\n3.0, 4.0, 5.0\n6.0, 7.0, 8.0\n";
				
		assertTrue(s.equals(matrix.toString()));
	}
	
	@Test
	public void setIdentity() {
		Matrix3f matrix = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		matrix.setIdentity();
		
		assertTrue((new Matrix3f(1,0,0,0,1,0,0,0,1)).equals(matrix));
	
	}
	
	@Test
	public void addDouble() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m2 = m1.clone();
		double s = this.random.nextDouble();
		
		m2.set(m2.m00+s, m2.m01+s, m2.m02+s, m2.m10+s, m2.m11+s, m2.m12+s, m2.m20+s, m2.m21+s, m2.m22+s);
		m1.add(s);
		
		assertTrue(m2.equals(m1));
	}
	
	@Test
	public void addDoubleMatrix3D() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m2 = new Matrix3f();
		double s = this.random.nextDouble();
		
		m2.set(m1.m00+s, m1.m01+s, m1.m02+s, m1.m10+s, m1.m11+s, m1.m12+s, m1.m20+s, m1.m21+s, m1.m22+s);
		m1.add(s,m1);
		
		assertTrue(m2.equals(m1));
	}
	
	@Test
	public void addMatrix3DMatrix3D() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m2 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m3 = new Matrix3f();
		
		m3.set(m1.m00+m2.m00, m1.m01+m2.m01, m1.m02+m2.m02, m1.m10+m2.m10, m1.m11+m2.m11, m1.m12+m2.m12, m1.m20+m2.m20, m1.m21+m2.m21, m1.m22+m2.m22);
		m1.add(m1,m2);
		
		assertTrue(m3.equals(m1));
	}
	
	@Test
	public void addMatrix3D() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m2 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m3 = new Matrix3f();
		
		m3.set(m1.m00+m2.m00, m1.m01+m2.m01, m1.m02+m2.m02, m1.m10+m2.m10, m1.m11+m2.m11, m1.m12+m2.m12, m1.m20+m2.m20, m1.m21+m2.m21, m1.m22+m2.m22);
		m1.add(m2);
		
		assertTrue(m3.equals(m1));
	}
	
	@Test
	public void subMatrix3DMatrix3D() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m2 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m3 = new Matrix3f();
		
		m3.set(m1.m00-m2.m00, m1.m01-m2.m01, m1.m02-m2.m02, m1.m10-m2.m10, m1.m11-m2.m11, m1.m12-m2.m12, m1.m20-m2.m20, m1.m21-m2.m21, m1.m22-m2.m22);
		m1.sub(m1,m2);
		
		assertTrue(m3.equals(m1));
	}
	
	@Test
	public void subMatrix3D() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m2 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m3 = new Matrix3f();
		
		m3.set(m1.m00-m2.m00, m1.m01-m2.m01, m1.m02-m2.m02, m1.m10-m2.m10, m1.m11-m2.m11, m1.m12-m2.m12, m1.m20-m2.m20, m1.m21-m2.m21, m1.m22-m2.m22);
		m1.sub(m2);
		
		assertTrue(m3.equals(m1));
	}
	
	@Test
	public void transpose() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f transpose = new Matrix3f();
		
		double [] v = new double[3];
		
		m1.getRow(0, v);
		transpose.setColumn(0, v);
		
		m1.getRow(1, v);
		transpose.setColumn(1, v);
		
		m1.getRow(2, v);
		transpose.setColumn(2, v);
		
		m1.transpose();
		
		assertTrue(transpose.equals(m1));
		
	}
	
	@Test
	public void transposeMatrix3D() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f transpose = new Matrix3f();
		
		double [] v = new double[3];
		
		m1.getRow(0, v);
		transpose.setColumn(0, v);
		
		m1.getRow(1, v);
		transpose.setColumn(1, v);
		
		m1.getRow(2, v);
		transpose.setColumn(2, v);
		
		m1.transpose(m1);
		
		assertTrue(transpose.equals(m1));
	}
	
	@Test
	public void invert() {
		Matrix3f m1 = new Matrix3f(1,0,0,0,2,0,0,0,3);
		Matrix3f i1 = new Matrix3f(6,0,0,0,3,0,0,0,2);
		i1.mul(1/6.);
		
		Matrix3f m2 = new Matrix3f(1,-1,1,1,1,-1,-1,-1,-1);
		Matrix3f i2 = new Matrix3f(1,1,0,-1,0,-1,0,-1,-1);
		i2.mul(1/2.);
			
		
		Matrix3f m3 = new Matrix3f(1.5, 2.5, 1.5, -2.5, 1.5, -1.5, -1.5, -1, -1);
		Matrix3f i3 = new Matrix3f(-1.5,0.5,-3,-0.125,0.375,-0.75,2.375,-1.125,4.25);
		
		
		m1.invert();
		assertTrue(i1.equals(m1));
		
		m2.invert();
		assertTrue(i2.equals(m2));
		
		m3.invert();
		assertTrue(i3.equals(m3));
		
	}
	
	@Test
	public void invertMatrix3D() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void luDecomposition() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void luBacksubstitution() {
		throw new UnsupportedOperationException();
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
		Matrix3f matrix = new Matrix3f(a,b,c,d,e,f,g,h,i);
		
		double determinant = a*e*i -a*f*h -b*d*i + b*f*g + c*d*h - c*e*g;
		
		assertEpsilonEquals(determinant, matrix.determinant());
	}
	
	@Test
	public void mulDouble() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		double e = this.random.nextDouble();
		double f = this.random.nextDouble();
		double g = this.random.nextDouble();
		double h = this.random.nextDouble();
		double i = this.random.nextDouble();
		Matrix3f matrix = new Matrix3f(a,b,c,d,e,f,g,h,i);
		
		double s = this.random.nextDouble();
		Matrix3f prodScal = new Matrix3f(a*s,b*s,c*s,d*s,e*s,f*s,g*s,h*s,i*s);
		
		matrix.mul(s);
		assertTrue(prodScal.equals(matrix));
	}
	
	@Test
	public void mulVector3D() {
		Vector3f vector = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f matrix = new Matrix3f(1,2,4,5,1,3,9,-2,1);
		
		Vector3f product = new Vector3f(vector.getX()+2*vector.getY()+4*vector.getZ(),5*vector.getX()+vector.getY()+3*vector.getZ(),9*vector.getX()-2*vector.getY()+vector.getZ());
		
		
		assertTrue(product.equals(matrix.mul(vector)));
	}
	
	@Test
	public void mulTransposeLeftVector3D() {
		Vector3f vector = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f matrix = new Matrix3f(1,2,4,5,1,3,9,-2,1);
		
		Vector3f product = new Vector3f(vector.getX()+5*vector.getY()+9*vector.getZ(),2*vector.getX()+vector.getY()-2*vector.getZ(),4*vector.getX()+3*vector.getY()+vector.getZ());
		
		matrix.transpose();
		assertTrue(product.equals(matrix.mul(vector)));
	}
	
	@Test
	public void mulDoubleMatrix3D() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		double e = this.random.nextDouble();
		double f = this.random.nextDouble();
		double g = this.random.nextDouble();
		double h = this.random.nextDouble();
		double i = this.random.nextDouble();
		Matrix3f matrix = new Matrix3f(a,b,c,d,e,f,g,h,i);
		
		double s = this.random.nextDouble();
		Matrix3f prodScal = new Matrix3f(a*s,b*s,c*s,d*s,e*s,f*s,g*s,h*s,i*s);
		
		matrix.mul(s,matrix);
		assertTrue(prodScal.equals(matrix));
	}
	
	@Test
	public void mulMatrix3D() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		double e = this.random.nextDouble();
		double f = this.random.nextDouble();
		double g = this.random.nextDouble();
		double h = this.random.nextDouble();
		double i = this.random.nextDouble();
		Matrix3f m1 = new Matrix3f(a,b,c,d,e,f,g,h,i);
		
		double j = this.random.nextDouble();
		double k = this.random.nextDouble();
		double l = this.random.nextDouble();
		double m = this.random.nextDouble();
		double n = this.random.nextDouble();
		double o = this.random.nextDouble();
		double p = this.random.nextDouble();
		double q = this.random.nextDouble();
		double r = this.random.nextDouble();
		Matrix3f m2 = new Matrix3f(j,k,l,m,n,o,p,q,r);
		
		Matrix3f prod = new Matrix3f(
				a*j+b*m+c*p,
				a*k+b*n+c*q,
				a*l+b*o+c*r,
				d*j+e*m+f*p,
				d*k+e*n+f*q,
				d*l+e*o+f*r,
				g*j+h*m+i*p,
				g*k+h*n+i*q,
				g*l+h*o+i*r);
				
		m1.mul(m2);
		
		assertTrue(prod.equals(m1));
	}
	
	@Test
	public void mulMatrix3DMatrix3D() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		double e = this.random.nextDouble();
		double f = this.random.nextDouble();
		double g = this.random.nextDouble();
		double h = this.random.nextDouble();
		double i = this.random.nextDouble();
		Matrix3f m1 = new Matrix3f(a,b,c,d,e,f,g,h,i);
		
		double j = this.random.nextDouble();
		double k = this.random.nextDouble();
		double l = this.random.nextDouble();
		double m = this.random.nextDouble();
		double n = this.random.nextDouble();
		double o = this.random.nextDouble();
		double p = this.random.nextDouble();
		double q = this.random.nextDouble();
		double r = this.random.nextDouble();
		Matrix3f m2 = new Matrix3f(j,k,l,m,n,o,p,q,r);
		
		Matrix3f prod = new Matrix3f(
				a*j+b*m+c*p,
				a*k+b*n+c*q,
				a*l+b*o+c*r,
				d*j+e*m+f*p,
				d*k+e*n+f*q,
				d*l+e*o+f*r,
				g*j+h*m+i*p,
				g*k+h*n+i*q,
				g*l+h*o+i*r);
				
		m1.mul(m1,m2);
		
		assertTrue(prod.equals(m1));
	}
	
	@Test
	public void mulNormalizeMatrix3D() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void mulNormalizeMatrix3DMatrix3D() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void mulTransposeBothMatrix3DMatrix3D() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void mulTransposeRightMatrix3DMatrix3D() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void mulTransposeLeftMatrix3DMatrix3D() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void normalizeMatrix3D() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void normalizeCrossProduct() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void normalizeCrossProductMatrix3D() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void equalsMatrix3D() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m2 = new Matrix3f(m1);
		
		assertTrue(m1.equals(m2));
		}
	
	@Test
	public void setZero() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		m1.setZero();
		
		assertTrue(m1.equals(new Matrix3f(0,0,0,0,0,0,0,0,0)));
		
	}
	
	@Test
	public void setDiagonalDoubleDoubleDouble() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
				
		m1.setDiagonal(a,b,c);
		
		assertTrue(m1.equals(new Matrix3f(a,0,0,0,b,0,0,0,c)));
	}
	
	@Test
	public void negate() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		m1.negate();
		assertTrue(m1.equals(new Matrix3f(-m1.m00,-m1.m01,-m1.m02,-m1.m10,-m1.m11,-m1.m12,-m1.m20,-m1.m21,-m1.m22)));
	}
	
	@Test
	public void negateMatrix3D() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		m1.negate(m1);
		assertTrue(m1.equals(new Matrix3f(-m1.m00,-m1.m01,-m1.m02,-m1.m10,-m1.m11,-m1.m12,-m1.m20,-m1.m21,-m1.m22)));
	}
	
	@Test

	public void cloneTest() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m2 = m1.clone();
		
		assertTrue(m1.equals(m2));
		
		m1.add(1f);
		
		assertFalse(m1.equals(m2));
		
	}
	
	@Test
	public void normalize() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void cov() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void isSymmetric() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		double e = this.random.nextDouble();
		double f = this.random.nextDouble();
		Matrix3f m1 = new Matrix3f(a,d,e,d,b,f,e,f,c);
		
		assertTrue(m1.isSymmetric());
		
		m1.setM01(f);
		
		assertFalse(m1.isSymmetric());
		
	}
	
	@Test
	public void eigenVectorsOfSymmetricMatrix() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void isIdentity() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		m1.setIdentity();
		assertTrue(m1.isIdentity());
		
		m1.setZero();
		assertFalse(m1.isIdentity());
	}
	
	
}
