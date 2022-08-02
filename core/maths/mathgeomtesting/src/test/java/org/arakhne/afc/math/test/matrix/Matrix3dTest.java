/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.test.matrix;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.arakhne.afc.math.matrix.Matrix3d;
import org.arakhne.afc.math.test.AbstractMathTestCase;

@SuppressWarnings("all")
public class Matrix3dTest extends AbstractMathTestCase {

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setIdentity(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d matrix = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		matrix.setIdentity();
		
		assertEpsilonEquals(new Matrix3d(1,0,0,0,1,0,0,0,1), matrix);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = m1.clone();
		double s = getRandom().nextDouble() * 100;
		
		m2.set(m2.getM00()+s, m2.getM01()+s, m2.getM02()+s, m2.getM10()+s, m2.getM11()+s, m2.getM12()+s, m2.getM20()+s, m2.getM21()+s, m2.getM22()+s);
		m1.add(s);
		
		assertEpsilonEquals(m2, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addDoubleMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = new Matrix3d();
		double s = getRandom().nextDouble() * 100;
		
		m2.set(m1.getM00()+s, m1.getM01()+s, m1.getM02()+s, m1.getM10()+s, m1.getM11()+s, m1.getM12()+s, m1.getM20()+s, m1.getM21()+s, m1.getM22()+s);
		m1.add(s,m1);
		
		assertEpsilonEquals(m2, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addMatrix3DMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m3 = new Matrix3d();
		
		m3.set(m1.getM00()+m2.getM00(), m1.getM01()+m2.getM01(), m1.getM02()+m2.getM02(), m1.getM10()+m2.getM10(), m1.getM11()+m2.getM11(), m1.getM12()+m2.getM12(), m1.getM20()+m2.getM20(), m1.getM21()+m2.getM21(), m1.getM22()+m2.getM22());
		m1.add(m1,m2);
		
		assertEpsilonEquals(m3, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m3 = new Matrix3d();
		
		m3.set(m1.getM00()+m2.getM00(), m1.getM01()+m2.getM01(), m1.getM02()+m2.getM02(), m1.getM10()+m2.getM10(), m1.getM11()+m2.getM11(), m1.getM12()+m2.getM12(), m1.getM20()+m2.getM20(), m1.getM21()+m2.getM21(), m1.getM22()+m2.getM22());
		m1.add(m2);
		
		assertEpsilonEquals(m3, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void subMatrix3DMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m3 = new Matrix3d();
		
		m3.set(m1.getM00()-m2.getM00(), m1.getM01()-m2.getM01(), m1.getM02()-m2.getM02(), m1.getM10()-m2.getM10(), m1.getM11()-m2.getM11(), m1.getM12()-m2.getM12(), m1.getM20()-m2.getM20(), m1.getM21()-m2.getM21(), m1.getM22()-m2.getM22());
		m1.sub(m1,m2);
		
		assertEpsilonEquals(m3, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void subMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m3 = new Matrix3d();
		
		m3.set(m1.getM00()-m2.getM00(), m1.getM01()-m2.getM01(), m1.getM02()-m2.getM02(), m1.getM10()-m2.getM10(), m1.getM11()-m2.getM11(), m1.getM12()-m2.getM12(), m1.getM20()-m2.getM20(), m1.getM21()-m2.getM21(), m1.getM22()-m2.getM22());
		m1.sub(m2);
		
		assertEpsilonEquals(m3, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void transpose(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d transpose = new Matrix3d();
		
		double [] v = new double[3];
		
		m1.getRow(0, v);
		transpose.setColumn(0, v);
		
		m1.getRow(1, v);
		transpose.setColumn(1, v);
		
		m1.getRow(2, v);
		transpose.setColumn(2, v);
		
		m1.transpose();
		
		assertEpsilonEquals(transpose, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void transposeMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d transpose = new Matrix3d();
		
		double [] v = new double[3];
		
		m1.getRow(0, v);
		transpose.setColumn(0, v);
		
		m1.getRow(1, v);
		transpose.setColumn(1, v);
		
		m1.getRow(2, v);
		transpose.setColumn(2, v);
		
		m1.transpose(m1);
		
		assertEpsilonEquals(transpose, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void invert(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(1,0,0,0,2,0,0,0,3);
		Matrix3d i1 = new Matrix3d(6,0,0,0,3,0,0,0,2);
		i1.mul(1/6.);
		
		Matrix3d m2 = new Matrix3d(1,-1,1,1,1,-1,-1,-1,-1);
		Matrix3d i2 = new Matrix3d(1,1,0,-1,0,-1,0,-1,-1);
		i2.mul(1/2.);
			
		
		Matrix3d m3 = new Matrix3d(1.5, 2.5, 1.5, -2.5, 1.5, -1.5, -1.5, -1, -1);
		Matrix3d i3 = new Matrix3d(-1.5,0.5,-3,-0.125,0.375,-0.75,2.375,-1.125,4.25);
		
		
		m1.invert();
		assertTrue(i1.equals(m1));
		
		m2.invert();
		assertTrue(i2.equals(m2));
		
		m3.invert();
		assertEpsilonEquals(i3, m3);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void invertMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(1,0,0,0,2,0,0,0,3);
		Matrix3d i1 = new Matrix3d(6,0,0,0,3,0,0,0,2);
		i1.mul(1/6.);
		
		Matrix3d m2 = new Matrix3d(1,-1,1,1,1,-1,-1,-1,-1);
		Matrix3d i2 = new Matrix3d(1,1,0,-1,0,-1,0,-1,-1);
		i2.mul(1/2.);
			
		
		Matrix3d m3 = new Matrix3d(1.5, 2.5, 1.5, -2.5, 1.5, -1.5, -1.5, -1, -1);
		Matrix3d i3 = new Matrix3d(-1.5,0.5,-3,-0.125,0.375,-0.75,2.375,-1.125,4.25);
		
		
		m1.invert(m1);
		assertTrue(i1.equals(m1));
		
		m2.invert(m2);
		assertTrue(i2.equals(m2));
		
		m3.invert(m3);

		assertEpsilonEquals(i3, m3);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void determinant(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble() * 100;
		double b = getRandom().nextDouble() * 100;
		double c = getRandom().nextDouble() * 100;
		double d = getRandom().nextDouble() * 100;
		double e = getRandom().nextDouble() * 100;
		double f = getRandom().nextDouble() * 100;
		double g = getRandom().nextDouble() * 100;
		double h = getRandom().nextDouble() * 100;
		double i = getRandom().nextDouble() * 100;
		Matrix3d matrix = new Matrix3d(a,b,c,d,e,f,g,h,i);
		
		double determinant = a*e*i -a*f*h -b*d*i + b*f*g + c*d*h - c*e*g;
		
		assertEpsilonEquals(determinant, matrix.determinant());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble() * 100;
		double b = getRandom().nextDouble() * 100;
		double c = getRandom().nextDouble() * 100;
		double d = getRandom().nextDouble() * 100;
		double e = getRandom().nextDouble() * 100;
		double f = getRandom().nextDouble() * 100;
		double g = getRandom().nextDouble() * 100;
		double h = getRandom().nextDouble() * 100;
		double i = getRandom().nextDouble() * 100;
		Matrix3d matrix = new Matrix3d(a,b,c,d,e,f,g,h,i);
		
		double s = getRandom().nextDouble() * 100;
		Matrix3d prodScal = new Matrix3d(a*s,b*s,c*s,d*s,e*s,f*s,g*s,h*s,i*s);
		
		matrix.mul(s);
		assertEpsilonEquals(prodScal, matrix);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulVector3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector3d vector = new Vector3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d matrix = new Matrix3d(1,2,4,5,1,3,9,-2,1);
		
		Vector3d product = new Vector3d(vector.getX()+2*vector.getY()+4*vector.getZ(),5*vector.getX()+vector.getY()+3*vector.getZ(),9*vector.getX()-2*vector.getY()+vector.getZ());
		
		Vector3d result = new Vector3d();
		matrix.mul(vector, result);
		assertEpsilonEquals(product, result);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulTransposeLeftVector3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector3d vector = new Vector3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d matrix = new Matrix3d(1,2,4,5,1,3,9,-2,1);
		
		Vector3d product = new Vector3d(vector.getX()+5*vector.getY()+9*vector.getZ(),2*vector.getX()+vector.getY()-2*vector.getZ(),4*vector.getX()+3*vector.getY()+vector.getZ());
		
		matrix.transpose();
		Vector3d result = new Vector3d();
		matrix.mul(vector, result);
		assertEpsilonEquals(product, result);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulDoubleMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble() * 100;
		double b = getRandom().nextDouble() * 100;
		double c = getRandom().nextDouble() * 100;
		double d = getRandom().nextDouble() * 100;
		double e = getRandom().nextDouble() * 100;
		double f = getRandom().nextDouble() * 100;
		double g = getRandom().nextDouble() * 100;
		double h = getRandom().nextDouble() * 100;
		double i = getRandom().nextDouble() * 100;
		Matrix3d matrix = new Matrix3d(a,b,c,d,e,f,g,h,i);
		
		double s = getRandom().nextDouble() * 100;
		Matrix3d prodScal = new Matrix3d(a*s,b*s,c*s,d*s,e*s,f*s,g*s,h*s,i*s);
		
		matrix.mul(s,matrix);
		assertEpsilonEquals(prodScal, matrix);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble() * 100;
		double b = getRandom().nextDouble() * 100;
		double c = getRandom().nextDouble() * 100;
		double d = getRandom().nextDouble() * 100;
		double e = getRandom().nextDouble() * 100;
		double f = getRandom().nextDouble() * 100;
		double g = getRandom().nextDouble() * 100;
		double h = getRandom().nextDouble() * 100;
		double i = getRandom().nextDouble() * 100;
		Matrix3d m1 = new Matrix3d(a,b,c,d,e,f,g,h,i);
		
		double j = getRandom().nextDouble() * 100;
		double k = getRandom().nextDouble() * 100;
		double l = getRandom().nextDouble() * 100;
		double m = getRandom().nextDouble() * 100;
		double n = getRandom().nextDouble() * 100;
		double o = getRandom().nextDouble() * 100;
		double p = getRandom().nextDouble() * 100;
		double q = getRandom().nextDouble() * 100;
		double r = getRandom().nextDouble() * 100;
		Matrix3d m2 = new Matrix3d(j,k,l,m,n,o,p,q,r);
		
		Matrix3d prod = new Matrix3d(
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
		
		assertEpsilonEquals(prod, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulMatrix3DMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble() * 100;
		double b = getRandom().nextDouble() * 100;
		double c = getRandom().nextDouble() * 100;
		double d = getRandom().nextDouble() * 100;
		double e = getRandom().nextDouble() * 100;
		double f = getRandom().nextDouble() * 100;
		double g = getRandom().nextDouble() * 100;
		double h = getRandom().nextDouble() * 100;
		double i = getRandom().nextDouble() * 100;
		Matrix3d m1 = new Matrix3d(a,b,c,d,e,f,g,h,i);
		
		double j = getRandom().nextDouble() * 100;
		double k = getRandom().nextDouble() * 100;
		double l = getRandom().nextDouble() * 100;
		double m = getRandom().nextDouble() * 100;
		double n = getRandom().nextDouble() * 100;
		double o = getRandom().nextDouble() * 100;
		double p = getRandom().nextDouble() * 100;
		double q = getRandom().nextDouble() * 100;
		double r = getRandom().nextDouble() * 100;
		Matrix3d m2 = new Matrix3d(j,k,l,m,n,o,p,q,r);
		
		Matrix3d prod = new Matrix3d(
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
		
		assertEpsilonEquals(prod, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulNormalizeMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d(
				0.030612, 0.061224, 0.051020,
				-0.061224, 1.377551, -0.102041,
				-0.153061, 2.693878, -0.255102);
		Matrix3d m2 = new Matrix3d(
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulNormalizeMatrix3DMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d();
		Matrix3d m1 = new Matrix3d(
				0.030612, 0.061224, 0.051020,
				-0.061224, 1.377551, -0.102041,
				-0.153061, 2.693878, -0.255102);
		Matrix3d m2 = new Matrix3d(
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulTransposeBothMatrix3DMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d mulTrans = new Matrix3d();
		
		mulTrans.mulTransposeBoth(m1, m2);
		m1.transpose();
		m2.transpose();
		m1.mul(m1, m2);
		
		assertEpsilonEquals(m1, mulTrans);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulTransposeRightMatrix3DMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d mulTrans = new Matrix3d();
		
		mulTrans.mulTransposeRight(m1, m2);
		m2.transpose();
		m1.mul(m1, m2);
		
		assertEpsilonEquals(m1, mulTrans);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulTransposeLeftMatrix3DMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d mulTrans = new Matrix3d();
		
		mulTrans.mulTransposeLeft(m1, m2);
		m1.transpose();
		m1.mul(m1, m2);
		
		assertEpsilonEquals(m1, mulTrans);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeCP_zero(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d();
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeCP_identity(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d();
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeCP_std(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d(
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeCPMatrix3D_zero(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d();
		Matrix3d r = new Matrix3d();
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeCPMatrix3D_identity(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d();
		m.setIdentity();
		Matrix3d r = new Matrix3d();
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeCPMatrix3D_std(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d(
				1, 2, 3,
				4, 5, 6,
				7, 8, 9);
		Matrix3d r = new Matrix3d();
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void equalsMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = new Matrix3d(m1);
		
		assertEpsilonEquals(m1, m2);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setZero(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		m1.setZero();
		
		assertEpsilonEquals(new Matrix3d(0,0,0,0,0,0,0,0,0), m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setDiagonalDoubleDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		double a = getRandom().nextDouble() * 100;
		double b = getRandom().nextDouble() * 100;
		double c = getRandom().nextDouble() * 100;
				
		m1.setDiagonal(a,b,c);
		
		assertEpsilonEquals(new Matrix3d(a,0,0,0,b,0,0,0,c), m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void negate(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = m1.clone();
		Matrix3d temp = new Matrix3d();
		Matrix3d temp2 = new Matrix3d();
		
		m1.negate();
		temp.add(m1, m2);
		assertEpsilonEquals(new Matrix3d(0,0,0,0,0,0,0,0,0), temp);
		temp.mul(m1, m2);
		
		m2.negate();
		assertEpsilonEquals(m1, m2);
		
		m1.negate();
		temp2.mul(m1, m2);
		assertEpsilonEquals(temp, temp2);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void negateMatrix3D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = m1.clone();
		Matrix3d temp = new Matrix3d();
		Matrix3d temp2 = new Matrix3d();
		
		m1.negate(m1);
		temp.add(m1, m2);
		assertEpsilonEquals(new Matrix3d(0,0,0,0,0,0,0,0,0), temp);
		temp.mul(m1, m2);
		
		m2.negate(m2);
		assertEpsilonEquals(m1, m2);
		
		m1.negate(m1);
		temp2.mul(m1, m2);
		assertEpsilonEquals(temp, temp2);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)

	public void cloneTest(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = m1.clone();
		
		assertEpsilonEquals(m1, m2);
		
		m1.add(1f);
		
		assertNotEpsilonEquals(m1, m2);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalize_zero(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d();
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalize_identity(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d();
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalize_std(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d(
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeMatrix3D_zero(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d();
		Matrix3d r = new Matrix3d();
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeMatrix3D_identity(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d();
		m.setIdentity();
		Matrix3d r = new Matrix3d();
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeMatrix3D_std(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d(
				1, 2, 3,
				4, 5, 6,
				7, 8, 9);
		Matrix3d r = new Matrix3d();
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void cov(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		//Verification of the function, by verifying all  the properties of the covariant matrix
		Matrix3d covMatrix = new Matrix3d();
		
		Vector3D v1 = randomVector3d();
		Vector3D v2 = this.randomVector3d();
		Vector3D v3 = this.randomVector3d();
		Vector3D v4 = this.randomVector3d();
		
		Vector3d meanTest = new Vector3d((v1.getX()+v2.getX()+v3.getX()+v4.getX())/4.,(v1.getY()+v2.getY()+v3.getY()+v4.getY())/4.,(v1.getZ()+v2.getZ()+v3.getZ()+v4.getZ())/4.);
		Vector3d mean = new Vector3d();
		covMatrix.cov(mean, v1,v2,v3,v4);
		
		//equality of the means
		assertEpsilonEquals(mean, meanTest);
		
		//verification of symmetry property
		assertTrue(covMatrix.isSymmetric());
		
		//verification of positive-definite property
		Vector3D vector = this.randomVector3d();
		Vector3D temp = (Vector3D) vector.clone();
		covMatrix.mul(vector, temp);
		double s = vector.dot(temp);
		assertTrue(s>0);
		
		//verification of derivability property
		assertNotEpsilonEquals(0, covMatrix.determinant());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void isSymmetric(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble() * 100;
		double b = getRandom().nextDouble() * 100;
		double c = getRandom().nextDouble() * 100;
		double d = getRandom().nextDouble() * 100;
		double e = getRandom().nextDouble() * 100;
		double f = getRandom().nextDouble() * 100;
		Matrix3d m1 = new Matrix3d(a,d,e,d,b,f,e,f,c);
		
		assertTrue(m1.isSymmetric());
		
		m1.setM01(f);
		
		assertFalse(m1.isSymmetric());
		
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void eigenVectorsOfSymmetricMatrix_zero(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d();
		Matrix3d eigenVectors = new Matrix3d();
		double[] eigenValues = m.eigenVectorsOfSymmetricMatrix(eigenVectors);

		assertEpsilonEquals(0, eigenValues[0]);
		assertEpsilonEquals(0, eigenValues[1]);
		assertEpsilonEquals(0, eigenValues[2]);

		Vector3D vector1 = new Vector3d();
		Vector3D vector2 = new Vector3d();
		Vector3D vector3 = new Vector3d();
		eigenVectors.getColumn(0, vector1);
		eigenVectors.getColumn(1, vector2);
		eigenVectors.getColumn(2, vector3);
		assertFpVectorEquals(1, 0, 0, vector1);
		assertFpVectorEquals(0, 1, 0, vector2);
		assertFpVectorEquals(0, 0, 1, vector3);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void eigenVectorsOfSymmetricMatrix_identity(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d();
		m.setIdentity();
		Matrix3d eigenVectors = new Matrix3d();
		double[] eigenValues = m.eigenVectorsOfSymmetricMatrix(eigenVectors);

		assertEpsilonEquals(1, eigenValues[0]);
		assertEpsilonEquals(1, eigenValues[1]);
		assertEpsilonEquals(1, eigenValues[2]);

		Vector3D vector1 = new Vector3d();
		Vector3D vector2 = new Vector3d();
		Vector3D vector3 = new Vector3d();
		eigenVectors.getColumn(0, vector1);
		eigenVectors.getColumn(1, vector2);
		eigenVectors.getColumn(2, vector3);
		assertFpVectorEquals(1, 0, 0, vector1);
		assertFpVectorEquals(0, 1, 0, vector2);
		assertFpVectorEquals(0, 0, 1, vector3);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void eigenVectorsOfSymmetricMatrix_sym(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m = new Matrix3d(1, 5, 0, 5, 2, 3, 0, 3, 1);
		Matrix3d eigenVectors = new Matrix3d();
		double[] eigenValues = m.eigenVectorsOfSymmetricMatrix(eigenVectors);
		
		assertEpsilonEquals(-4.3523, eigenValues[0]);
		assertEpsilonEquals(7.3523, eigenValues[1]);
		assertEpsilonEquals(1, eigenValues[2]);
		
		Vector3D vector1 = new Vector3d();
		Vector3D vector2 = new Vector3d();
		Vector3D vector3 = new Vector3d();
		eigenVectors.getColumn(0, vector1);
		eigenVectors.getColumn(1, vector2);
		eigenVectors.getColumn(2, vector3);
		
		assertFpVectorEquals(6.3171e-01, -6.7623e-01, 3.7903e-01, vector1);
		assertFpVectorEquals(5.7986e-01, 7.3669e-01, 3.4792e-01, vector2);
		assertFpVectorEquals(-5.1450e-01, 6.9389e-17, 8.5749e-01, vector3);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void isIdentity(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		assertFalse(m1.isIdentity());
		
		m1.setIdentity();
		assertTrue(m1.isIdentity());
		
		m1.setZero();
		assertFalse(m1.isIdentity());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_addMatrix3d(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m3 = new Matrix3d();
		
		m3.set(m1.getM00()+m2.getM00(), m1.getM01()+m2.getM01(), m1.getM02()+m2.getM02(), m1.getM10()+m2.getM10(), m1.getM11()+m2.getM11(), m1.getM12()+m2.getM12(), m1.getM20()+m2.getM20(), m1.getM21()+m2.getM21(), m1.getM22()+m2.getM22());
		m1.operator_add(m2);
		
		assertEpsilonEquals(m3, m1);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_addDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = m1.clone();
		double s = getRandom().nextDouble() * 100;
		
		m2.set(m2.getM00()+s, m2.getM01()+s, m2.getM02()+s, m2.getM10()+s, m2.getM11()+s, m2.getM12()+s, m2.getM20()+s, m2.getM21()+s, m2.getM22()+s);
		m1.operator_add(s);
		
		assertEpsilonEquals(m2, m1);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_removeMatrix3d(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m3 = new Matrix3d();
		
		m3.set(m1.getM00()-m2.getM00(), m1.getM01()-m2.getM01(), m1.getM02()-m2.getM02(), m1.getM10()-m2.getM10(), m1.getM11()-m2.getM11(), m1.getM12()-m2.getM12(), m1.getM20()-m2.getM20(), m1.getM21()-m2.getM21(), m1.getM22()-m2.getM22());
		m1.operator_remove(m2);
		
		assertEpsilonEquals(m3, m1);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_removeDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = m1.clone();
		double s = getRandom().nextDouble() * 100;
		
		m2.set(m2.getM00()-s, m2.getM01()-s, m2.getM02()-s, m2.getM10()-s, m2.getM11()-s, m2.getM12()-s, m2.getM20()-s, m2.getM21()-s, m2.getM22()-s);
		m1.operator_remove(s);
		
		assertEpsilonEquals(m2, m1);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_plusMatrix3d(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m3 = new Matrix3d();
		
		m3.set(m1.getM00()+m2.getM00(), m1.getM01()+m2.getM01(), m1.getM02()+m2.getM02(), m1.getM10()+m2.getM10(), m1.getM11()+m2.getM11(), m1.getM12()+m2.getM12(), m1.getM20()+m2.getM20(), m1.getM21()+m2.getM21(), m1.getM22()+m2.getM22());
		Matrix3d r = m1.operator_plus(m2);
		
		assertEpsilonEquals(m3, r);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_plusDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = m1.clone();
		double s = getRandom().nextDouble() * 100;
		
		m2.set(m2.getM00()+s, m2.getM01()+s, m2.getM02()+s, m2.getM10()+s, m2.getM11()+s, m2.getM12()+s, m2.getM20()+s, m2.getM21()+s, m2.getM22()+s);
		Matrix3d r = m1.operator_plus(s);
		
		assertEpsilonEquals(m2, r);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_minusMatrix3d(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = m1.clone();
		double s = getRandom().nextDouble() * 100;
		
		m2.set(m2.getM00()-s, m2.getM01()-s, m2.getM02()-s, m2.getM10()-s, m2.getM11()-s, m2.getM12()-s, m2.getM20()-s, m2.getM21()-s, m2.getM22()-s);
		Matrix3d r = m1.operator_minus(s);
		
		assertEpsilonEquals(m2, r);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_minusDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = m1.clone();
		double s = getRandom().nextDouble() * 100;
		
		m2.set(m2.getM00()-s, m2.getM01()-s, m2.getM02()-s, m2.getM10()-s, m2.getM11()-s, m2.getM12()-s, m2.getM20()-s, m2.getM21()-s, m2.getM22()-s);
		Matrix3d r = m1.operator_minus(s);
		
		assertEpsilonEquals(m2, r);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_minus(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = m1.clone();
		
		m2.set(-m2.getM00(), -m2.getM01(), -m2.getM02(), -m2.getM10(), -m2.getM11(), -m2.getM12(), -m2.getM20(), -m2.getM21(), -m2.getM22());
		Matrix3d r = m1.operator_minus();
		
		assertEpsilonEquals(m2, r);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_multiplyMatrix3d(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble() * 100;
		double b = getRandom().nextDouble() * 100;
		double c = getRandom().nextDouble() * 100;
		double d = getRandom().nextDouble() * 100;
		double e = getRandom().nextDouble() * 100;
		double f = getRandom().nextDouble() * 100;
		double g = getRandom().nextDouble() * 100;
		double h = getRandom().nextDouble() * 100;
		double i = getRandom().nextDouble() * 100;
		Matrix3d m1 = new Matrix3d(a,b,c,d,e,f,g,h,i);
		
		double j = getRandom().nextDouble() * 100;
		double k = getRandom().nextDouble() * 100;
		double l = getRandom().nextDouble() * 100;
		double m = getRandom().nextDouble() * 100;
		double n = getRandom().nextDouble() * 100;
		double o = getRandom().nextDouble() * 100;
		double p = getRandom().nextDouble() * 100;
		double q = getRandom().nextDouble() * 100;
		double r = getRandom().nextDouble() * 100;
		Matrix3d m2 = new Matrix3d(j,k,l,m,n,o,p,q,r);
		
		Matrix3d prod = new Matrix3d(
				a*j+b*m+c*p,
				a*k+b*n+c*q,
				a*l+b*o+c*r,
				d*j+e*m+f*p,
				d*k+e*n+f*q,
				d*l+e*o+f*r,
				g*j+h*m+i*p,
				g*k+h*n+i*q,
				g*l+h*o+i*r);
				
		Matrix3d result = m1.operator_multiply(m2);
		
		assertEpsilonEquals(prod, result);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_multiplyDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble() * 100;
		double b = getRandom().nextDouble() * 100;
		double c = getRandom().nextDouble() * 100;
		double d = getRandom().nextDouble() * 100;
		double e = getRandom().nextDouble() * 100;
		double f = getRandom().nextDouble() * 100;
		double g = getRandom().nextDouble() * 100;
		double h = getRandom().nextDouble() * 100;
		double i = getRandom().nextDouble() * 100;
		Matrix3d matrix = new Matrix3d(a,b,c,d,e,f,g,h,i);
		
		double s = getRandom().nextDouble() * 100;
		Matrix3d prodScal = new Matrix3d(a*s,b*s,c*s,d*s,e*s,f*s,g*s,h*s,i*s);
		
		Matrix3d r = matrix.operator_multiply(s);
		assertEpsilonEquals(prodScal, r);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_divideDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble() * 100;
		double b = getRandom().nextDouble() * 100;
		double c = getRandom().nextDouble() * 100;
		double d = getRandom().nextDouble() * 100;
		double e = getRandom().nextDouble() * 100;
		double f = getRandom().nextDouble() * 100;
		double g = getRandom().nextDouble() * 100;
		double h = getRandom().nextDouble() * 100;
		double i = getRandom().nextDouble() * 100;
		Matrix3d matrix = new Matrix3d(a,b,c,d,e,f,g,h,i);
		
		double s = getRandom().nextDouble() * 100 + 2;
		Matrix3d prodScal = new Matrix3d(a/s,b/s,c/s,d/s,e/s,f/s,g/s,h/s,i/s);
		
		Matrix3d r = matrix.operator_divide(s);
		assertEpsilonEquals(prodScal, r);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_plusPlus(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = m1.clone();
		
		m2.set(m2.getM00()+1, m2.getM01()+1, m2.getM02()+1, m2.getM10()+1, m2.getM11()+1, m2.getM12()+1, m2.getM20()+1, m2.getM21()+1, m2.getM22()+1);
		m1.operator_plusPlus();
		
		assertEpsilonEquals(m2, m1);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_moinsMoins(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d m2 = m1.clone();
		
		m2.set(m2.getM00()-1, m2.getM01()-1, m2.getM02()-1, m2.getM10()-1, m2.getM11()-1, m2.getM12()-1, m2.getM20()-1, m2.getM21()-1, m2.getM22()-1);
		m1.operator_moinsMoins();
		
		assertEpsilonEquals(m2, m1);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_not(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix3d m1 = new Matrix3d(getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble(),getRandom().nextDouble());
		Matrix3d transpose = new Matrix3d();
		
		double [] v = new double[3];
		
		m1.getRow(0, v);
		transpose.setColumn(0, v);
		
		m1.getRow(1, v);
		transpose.setColumn(1, v);
		
		m1.getRow(2, v);
		transpose.setColumn(2, v);
		
		Matrix3d r = m1.operator_not();
		
		assertEpsilonEquals(transpose, r);
	}
	
}
