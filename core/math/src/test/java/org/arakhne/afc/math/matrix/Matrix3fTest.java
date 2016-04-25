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
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public class Matrix3fTest extends AbstractMathTestCase {

	@Rule
	public CoordinateSystem2DTestRule csTestRule = new CoordinateSystem2DTestRule();
	
	@Test
	public void toStringTest() {
		Matrix3f matrix = new Matrix3f(0,1,2,3,4,5,6,7,8);
		String s = "0.0, 1.0, 2.0\n3.0, 4.0, 5.0\n6.0, 7.0, 8.0\n"; //$NON-NLS-1$
				
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
		assertTrue(i3.epsilonEquals(m3,EPSILON));
	}
	
	@Test
	public void invertMatrix3D() {
		Matrix3f m1 = new Matrix3f(1,0,0,0,2,0,0,0,3);
		Matrix3f i1 = new Matrix3f(6,0,0,0,3,0,0,0,2);
		i1.mul(1/6.);
		
		Matrix3f m2 = new Matrix3f(1,-1,1,1,1,-1,-1,-1,-1);
		Matrix3f i2 = new Matrix3f(1,1,0,-1,0,-1,0,-1,-1);
		i2.mul(1/2.);
			
		
		Matrix3f m3 = new Matrix3f(1.5, 2.5, 1.5, -2.5, 1.5, -1.5, -1.5, -1, -1);
		Matrix3f i3 = new Matrix3f(-1.5,0.5,-3,-0.125,0.375,-0.75,2.375,-1.125,4.25);
		
		
		m1.invert(m1);
		assertTrue(i1.equals(m1));
		
		m2.invert(m2);
		assertTrue(i2.equals(m2));
		
		m3.invert(m3);

		assertTrue(i3.epsilonEquals(m3,EPSILON));
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
	@Ignore
	public void mulVector3D() {
		//TODO: Fix the code
//		Vector3f vector = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
//		Matrix3f matrix = new Matrix3f(1,2,4,5,1,3,9,-2,1);
//		
//		Vector3f product = new Vector3f(vector.getX()+2*vector.getY()+4*vector.getZ(),5*vector.getX()+vector.getY()+3*vector.getZ(),9*vector.getX()-2*vector.getY()+vector.getZ());
//		
//		Vector3f result = new Vector3f();
//		matrix.mul(vector, result);
//		assertEpsilonEquals(product, result);
	}
	
	@Test
	@Ignore
	public void mulTransposeLeftVector3D() {
		//TODO: Fix the code
//		Vector3f vector = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
//		Matrix3f matrix = new Matrix3f(1,2,4,5,1,3,9,-2,1);
//		
//		Vector3f product = new Vector3f(vector.getX()+5*vector.getY()+9*vector.getZ(),2*vector.getX()+vector.getY()-2*vector.getZ(),4*vector.getX()+3*vector.getY()+vector.getZ());
//		
//		matrix.transpose();
//		Vector3f result = new Vector3f();
//		matrix.mul(vector, result);
//		assertEpsilonEquals(product, result);
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
		Matrix3f m = new Matrix3f(
				0.030612, 0.061224, 0.051020,
				-0.061224, 1.377551, -0.102041,
				-0.153061, 2.693878, -0.255102);
		Matrix3f m2 = new Matrix3f(
				18, 45, 2,
			    4, 6, 8,
			    4, 5, 48);
		m.mulNormalize(m2);
		assertEpsilonEquals(-0.41938, m.getM00());
		assertEpsilonEquals(-0.27752, m.getM01());
		assertEpsilonEquals(0.86435, m.getM02());
		assertEpsilonEquals(-0.27752, m.getM10());
		assertEpsilonEquals(0.94574, m.getM11());
		assertEpsilonEquals(0.169, m.getM12());
		assertEpsilonEquals(0.86435, m.getM20());
		assertEpsilonEquals(0.169, m.getM21());
		assertEpsilonEquals(0.47365, m.getM22());
	}
	
	@Test
	public void mulNormalizeMatrix3DMatrix3D() {
		Matrix3f m = new Matrix3f();
		Matrix3f m1 = new Matrix3f(
				0.030612, 0.061224, 0.051020,
				-0.061224, 1.377551, -0.102041,
				-0.153061, 2.693878, -0.255102);
		Matrix3f m2 = new Matrix3f(
				18, 45, 2,
			    4, 6, 8,
			    4, 5, 48);
		m.mulNormalize(m1, m2);
		assertEpsilonEquals(-0.41938, m.getM00());
		assertEpsilonEquals(-0.27752, m.getM01());
		assertEpsilonEquals(0.86435, m.getM02());
		assertEpsilonEquals(-0.27752, m.getM10());
		assertEpsilonEquals(0.94574, m.getM11());
		assertEpsilonEquals(0.169, m.getM12());
		assertEpsilonEquals(0.86435, m.getM20());
		assertEpsilonEquals(0.169, m.getM21());
		assertEpsilonEquals(0.47365, m.getM22());
	}
	
	@Test
	public void mulTransposeBothMatrix3DMatrix3D() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m2 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f mulTrans = new Matrix3f();
		
		mulTrans.mulTransposeBoth(m1, m2);
		m1.transpose();
		m2.transpose();
		m1.mul(m1, m2);
		
		assertTrue(m1.equals(mulTrans));
	}
	
	@Test
	public void mulTransposeRightMatrix3DMatrix3D() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m2 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f mulTrans = new Matrix3f();
		
		mulTrans.mulTransposeRight(m1, m2);
		m2.transpose();
		m1.mul(m1, m2);
		
		assertTrue(m1.equals(mulTrans));
	}
	
	@Test
	public void mulTransposeLeftMatrix3DMatrix3D() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m2 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f mulTrans = new Matrix3f();
		
		mulTrans.mulTransposeLeft(m1, m2);
		m1.transpose();
		m1.mul(m1, m2);
		
		assertTrue(m1.equals(mulTrans));
	}
	
	@Test
	public void normalizeCP_zero() {
		Matrix3f m = new Matrix3f();
		m.normalizeCP();
		assertNaN(m.getM00());
		assertNaN(m.getM01());
		assertNaN(m.getM02());
		assertNaN(m.getM10());
		assertNaN(m.getM11());
		assertNaN(m.getM12());
		assertNaN(m.getM20());
		assertNaN(m.getM21());
		assertNaN(m.getM22());
	}
	
	@Test
	public void normalizeCP_identity() {
		Matrix3f m = new Matrix3f();
		m.setIdentity();
		m.normalizeCP();
		assertEpsilonEquals(1, m.getM00());
		assertEpsilonEquals(0, m.getM01());
		assertEpsilonEquals(0, m.getM02());
		assertEpsilonEquals(0, m.getM10());
		assertEpsilonEquals(1, m.getM11());
		assertEpsilonEquals(0, m.getM12());
		assertEpsilonEquals(0, m.getM20());
		assertEpsilonEquals(0, m.getM21());
		assertEpsilonEquals(1, m.getM22());
	}

	@Test
	public void normalizeCP_std() {
		Matrix3f m = new Matrix3f(
				1, 2, 3,
				4, 5, 6,
				7, 8, 9);
		m.normalizeCP();
		assertEpsilonEquals(1/Math.sqrt(1*1+4*4+7*7), m.getM00());
		assertEpsilonEquals(2/Math.sqrt(2*2+5*5+8*8), m.getM01());
		assertEpsilonEquals(m.getM00()*m.getM11()-m.getM01()*m.getM10(), m.getM02());
		assertEpsilonEquals(4/Math.sqrt(1*1+4*4+7*7), m.getM10());
		assertEpsilonEquals(5/Math.sqrt(2*2+5*5+8*8), m.getM11());
		assertEpsilonEquals(m.getM01()*m.getM20()-m.getM00()*m.getM21(), m.getM12());
		assertEpsilonEquals(7/Math.sqrt(1*1+4*4+7*7), m.getM20());
		assertEpsilonEquals(8/Math.sqrt(2*2+5*5+8*8), m.getM21());
		assertEpsilonEquals(m.getM00()*m.getM11()-m.getM01()*m.getM10(), m.getM22());
	}
	
	@Test
	public void normalizeCPMatrix3D_zero() {
		Matrix3f m = new Matrix3f();
		Matrix3f r = new Matrix3f();
		r.normalizeCP(m);
		assertNaN(r.getM00());
		assertNaN(r.getM01());
		assertNaN(r.getM02());
		assertNaN(r.getM10());
		assertNaN(r.getM11());
		assertNaN(r.getM12());
		assertNaN(r.getM20());
		assertNaN(r.getM21());
		assertNaN(r.getM22());
	}
	
	@Test
	public void normalizeCPMatrix3D_identity() {
		Matrix3f m = new Matrix3f();
		m.setIdentity();
		Matrix3f r = new Matrix3f();
		r.normalizeCP(m);
		assertEpsilonEquals(1, r.getM00());
		assertEpsilonEquals(0, r.getM01());
		assertEpsilonEquals(0, r.getM02());
		assertEpsilonEquals(0, r.getM10());
		assertEpsilonEquals(1, r.getM11());
		assertEpsilonEquals(0, r.getM12());
		assertEpsilonEquals(0, r.getM20());
		assertEpsilonEquals(0, r.getM21());
		assertEpsilonEquals(1, r.getM22());
	}

	@Test
	public void normalizeCPMatrix3D_std() {
		Matrix3f m = new Matrix3f(
				1, 2, 3,
				4, 5, 6,
				7, 8, 9);
		Matrix3f r = new Matrix3f();
		r.normalizeCP(m);
		assertEpsilonEquals(1/Math.sqrt(1*1+4*4+7*7), r.getM00());
		assertEpsilonEquals(2/Math.sqrt(2*2+5*5+8*8), r.getM01());
		assertEpsilonEquals(r.getM00()*r.getM11()-r.getM01()*r.getM10(), r.getM02());
		assertEpsilonEquals(4/Math.sqrt(1*1+4*4+7*7), r.getM10());
		assertEpsilonEquals(5/Math.sqrt(2*2+5*5+8*8), r.getM11());
		assertEpsilonEquals(r.getM01()*r.getM20()-r.getM00()*r.getM21(), r.getM12());
		assertEpsilonEquals(7/Math.sqrt(1*1+4*4+7*7), r.getM20());
		assertEpsilonEquals(8/Math.sqrt(2*2+5*5+8*8), r.getM21());
		assertEpsilonEquals(r.getM00()*r.getM11()-r.getM01()*r.getM10(), r.getM22());
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
		Matrix3f m2 = m1.clone();
		Matrix3f temp = new Matrix3f();
		Matrix3f temp2 = new Matrix3f();
		
		m1.negate();
		temp.add(m1, m2);
		assertTrue(temp.equals(new Matrix3f(0,0,0,0,0,0,0,0,0)));
		temp.mul(m1, m2);
		
		m2.negate();
		assertTrue(m1.equals(m2));
		
		m1.negate();
		temp2.mul(m1, m2);
		assertTrue(temp.equals(temp2));
	}
	
	@Test
	public void negateMatrix3D() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f m2 = m1.clone();
		Matrix3f temp = new Matrix3f();
		Matrix3f temp2 = new Matrix3f();
		
		m1.negate(m1);
		temp.add(m1, m2);
		assertTrue(temp.equals(new Matrix3f(0,0,0,0,0,0,0,0,0)));
		temp.mul(m1, m2);
		
		m2.negate(m2);
		assertTrue(m1.equals(m2));
		
		m1.negate(m1);
		temp2.mul(m1, m2);
		assertTrue(temp.equals(temp2));
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
	public void normalize_zero() {
		Matrix3f m = new Matrix3f();
		m.normalize();
		assertEpsilonEquals(1, m.getM00());
		assertEpsilonEquals(0, m.getM01());
		assertEpsilonEquals(0, m.getM02());
		assertEpsilonEquals(0, m.getM10());
		assertEpsilonEquals(1, m.getM11());
		assertEpsilonEquals(0, m.getM12());
		assertEpsilonEquals(0, m.getM20());
		assertEpsilonEquals(0, m.getM21());
		assertEpsilonEquals(1, m.getM22());
	}
	
	@Test
	public void normalize_identity() {
		Matrix3f m = new Matrix3f();
		m.setIdentity();
		m.normalize();
		assertEpsilonEquals(1, m.getM00());
		assertEpsilonEquals(0, m.getM01());
		assertEpsilonEquals(0, m.getM02());
		assertEpsilonEquals(0, m.getM10());
		assertEpsilonEquals(1, m.getM11());
		assertEpsilonEquals(0, m.getM12());
		assertEpsilonEquals(0, m.getM20());
		assertEpsilonEquals(0, m.getM21());
		assertEpsilonEquals(1, m.getM22());
	}

	@Test
	public void normalize_std() {
		Matrix3f m = new Matrix3f(
				1, 2, 3,
				4, 5, 6,
				7, 8, 9);
		m.normalize();
		assertEpsilonEquals(-0.41938, m.getM00());
		assertEpsilonEquals(-0.27752, m.getM01());
		assertEpsilonEquals(0.86435, m.getM02());
		assertEpsilonEquals(-0.27752, m.getM10());
		assertEpsilonEquals(0.94574, m.getM11());
		assertEpsilonEquals(0.169, m.getM12());
		assertEpsilonEquals(0.86435, m.getM20());
		assertEpsilonEquals(0.169, m.getM21());
		assertEpsilonEquals(0.47365, m.getM22());
	}

	@Test
	public void normalizeMatrix3D_zero() {
		Matrix3f m = new Matrix3f();
		Matrix3f r = new Matrix3f();
		r.normalize(m);
		assertEpsilonEquals(1, r.getM00());
		assertEpsilonEquals(0, r.getM01());
		assertEpsilonEquals(0, r.getM02());
		assertEpsilonEquals(0, r.getM10());
		assertEpsilonEquals(1, r.getM11());
		assertEpsilonEquals(0, r.getM12());
		assertEpsilonEquals(0, r.getM20());
		assertEpsilonEquals(0, r.getM21());
		assertEpsilonEquals(1, r.getM22());
	}
	
	@Test
	public void normalizeMatrix3D_identity() {
		Matrix3f m = new Matrix3f();
		m.setIdentity();
		Matrix3f r = new Matrix3f();
		r.normalize(m);
		assertEpsilonEquals(1, r.getM00());
		assertEpsilonEquals(0, r.getM01());
		assertEpsilonEquals(0, r.getM02());
		assertEpsilonEquals(0, r.getM10());
		assertEpsilonEquals(1, r.getM11());
		assertEpsilonEquals(0, r.getM12());
		assertEpsilonEquals(0, r.getM20());
		assertEpsilonEquals(0, r.getM21());
		assertEpsilonEquals(1, r.getM22());
	}

	@Test
	public void normalizeMatrix3D_std() {
		Matrix3f m = new Matrix3f(
				1, 2, 3,
				4, 5, 6,
				7, 8, 9);
		Matrix3f r = new Matrix3f();
		r.normalize(m);
		assertEpsilonEquals(-0.41938, r.getM00());
		assertEpsilonEquals(-0.27752, r.getM01());
		assertEpsilonEquals(0.86435, r.getM02());
		assertEpsilonEquals(-0.27752, r.getM10());
		assertEpsilonEquals(0.94574, r.getM11());
		assertEpsilonEquals(0.169, r.getM12());
		assertEpsilonEquals(0.86435, r.getM20());
		assertEpsilonEquals(0.169, r.getM21());
		assertEpsilonEquals(0.47365, r.getM22());
	}

	@Test
	@Ignore
	public void cov() {
		//TODO: Fix the code
		//Verification of the function, by verifying all  the properties of the covariant matrix
//		Matrix3f covMatrix = new Matrix3f();
//		
//		Vector3D v1 = randomVector3f();
//		Vector3D v2 = this.randomVector3f();
//		Vector3D v3 = this.randomVector3f();
//		Vector3D v4 = this.randomVector3f();
//		
//		Vector3f meanTest = new Vector3f((v1.getX()+v2.getX()+v3.getX()+v4.getX())/4.,(v1.getY()+v2.getY()+v3.getY()+v4.getY())/4.,(v1.getZ()+v2.getZ()+v3.getZ()+v4.getZ())/4.);
//		Vector3f mean = new Vector3f();
//		covMatrix.cov(mean, v1,v2,v3,v4);
//		
//		//equality of the means
//		assertTrue(mean.equals(meanTest));
//		
//		//verification of symmetry property
//		assertTrue(covMatrix.isSymmetric());
//		
//		//verification of positive-definite property
//		Vector3D vector = this.randomVector3f();
//		Vector3D temp = vector.clone();
//		covMatrix.mul(vector, temp);
//		double s = vector.dot(temp);
//		assertTrue(s>0);
//		
//		//verification of derivability property
//		assertTrue(covMatrix.determinant()!=0);
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
	@Ignore
	public void eigenVectorsOfSymmetricMatrix_zero() {
		//TODO: Fix the code
//		Matrix3f m = new Matrix3f();
//		Matrix3f eigenVectors = new Matrix3f();
//		double[] eigenValues = m.eigenVectorsOfSymmetricMatrix(eigenVectors);
//
//		assertEpsilonEquals(0, eigenValues[0]);
//		assertEpsilonEquals(0, eigenValues[1]);
//		assertEpsilonEquals(0, eigenValues[2]);
//
//		Vector3D vector1 = new Vector3f();
//		Vector3D vector2 = new Vector3f();
//		Vector3D vector3 = new Vector3f();
//		eigenVectors.getColumn(0, vector1);
//		eigenVectors.getColumn(1, vector2);
//		eigenVectors.getColumn(2, vector3);
//		assertFpVectorEquals(1, 0, 0, vector1);
//		assertFpVectorEquals(0, 1, 0, vector2);
//		assertFpVectorEquals(0, 0, 1, vector3);
	}
	
	@Test
	@Ignore
	public void eigenVectorsOfSymmetricMatrix_identity() {
//		//TODO: Fix the code
//		Matrix3f m = new Matrix3f();
//		m.setIdentity();
//		Matrix3f eigenVectors = new Matrix3f();
//		double[] eigenValues = m.eigenVectorsOfSymmetricMatrix(eigenVectors);
//
//		assertEpsilonEquals(1, eigenValues[0]);
//		assertEpsilonEquals(1, eigenValues[1]);
//		assertEpsilonEquals(1, eigenValues[2]);
//
//		Vector3D vector1 = new Vector3f();
//		Vector3D vector2 = new Vector3f();
//		Vector3D vector3 = new Vector3f();
//		eigenVectors.getColumn(0, vector1);
//		eigenVectors.getColumn(1, vector2);
//		eigenVectors.getColumn(2, vector3);
//		assertFpVectorEquals(1, 0, 0, vector1);
//		assertFpVectorEquals(0, 1, 0, vector2);
//		assertFpVectorEquals(0, 0, 1, vector3);
	}

	@Test
	@Ignore
	public void eigenVectorsOfSymmetricMatrix_sym() {
		//TODO: Fix the code
//		Matrix3f m = new Matrix3f(1, 5, 0, 5, 2, 3, 0, 3, 1);
//		Matrix3f eigenVectors = new Matrix3f();
//		double[] eigenValues = m.eigenVectorsOfSymmetricMatrix(eigenVectors);
//		
//		assertEpsilonEquals(-4.3523, eigenValues[0]);
//		assertEpsilonEquals(7.3523, eigenValues[1]);
//		assertEpsilonEquals(1, eigenValues[2]);
//		
//		Vector3D vector1 = new Vector3f();
//		Vector3D vector2 = new Vector3f();
//		Vector3D vector3 = new Vector3f();
//		eigenVectors.getColumn(0, vector1);
//		eigenVectors.getColumn(1, vector2);
//		eigenVectors.getColumn(2, vector3);
//		
//		assertFpVectorEquals(6.3171e-01, -6.7623e-01, 3.7903e-01, vector1);
//		assertFpVectorEquals(5.7986e-01, 7.3669e-01, 3.4792e-01, vector2);
//		assertFpVectorEquals(-5.1450e-01, 6.9389e-17, 8.5749e-01, vector3);
	}
	
	@Test
	public void isIdentity() {
		Matrix3f m1 = new Matrix3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		assertFalse(m1.isIdentity());
		
		m1.setIdentity();
		assertTrue(m1.isIdentity());
		
		m1.setZero();
		assertFalse(m1.isIdentity());
	}
	
	
}
