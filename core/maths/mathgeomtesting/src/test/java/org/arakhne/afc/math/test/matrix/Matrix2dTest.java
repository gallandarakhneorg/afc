/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.matrix.Matrix2d;
import org.arakhne.afc.math.test.AbstractMathTestCase;

@SuppressWarnings("all")
public class Matrix2dTest extends AbstractMathTestCase {
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setIdentity(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d matrix = this.randomMatrix2d();
		matrix.setIdentity();
		
		assertEpsilonEquals(new Matrix2d(1,0,0,1), matrix);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = m1.clone();
		double s = getRandom().nextDouble();
		
		m2.set(m2.getM00()+s, m2.getM01()+s, m2.getM10()+s, m2.getM11()+s);
		m1.add(s);
		
		assertEpsilonEquals(m2, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addDoubleMatrix2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = m1.clone();
		double s = getRandom().nextDouble();
		
		m2.set(m2.getM00()+s, m2.getM01()+s, m2.getM10()+s, m2.getM11()+s);
		m1.add(s,m1);
		
		assertEpsilonEquals(m2, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addMatrix2DMatrix2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = this.randomMatrix2d();
		Matrix2d m3 = new Matrix2d();
		
		m3.set(m1.getM00()+m2.getM00(), m1.getM01()+m2.getM01(), m1.getM10()+m2.getM10(), m1.getM11()+m2.getM11());
		m1.add(m1,m2);
		
		assertEpsilonEquals(m3, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addMatrix2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = this.randomMatrix2d();
		Matrix2d m3 = new Matrix2d();
		
		m3.set(m1.getM00()+m2.getM00(), m1.getM01()+m2.getM01(), m1.getM10()+m2.getM10(), m1.getM11()+m2.getM11());
		m1.add(m2);
		
		assertEpsilonEquals(m3, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void subMatrix2DMatrix2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = this.randomMatrix2d();
		Matrix2d m3 = new Matrix2d();
		
		m3.set(m1.getM00()-m2.getM00(), m1.getM01()-m2.getM01(), m1.getM10()-m2.getM10(), m1.getM11()-m2.getM11());
		m1.sub(m1,m2);
		
		assertEpsilonEquals(m3, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void subMatrix2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = this.randomMatrix2d();
		Matrix2d m3 = new Matrix2d();
		
		m3.set(m1.getM00()-m2.getM00(), m1.getM01()-m2.getM01(), m1.getM10()-m2.getM10(), m1.getM11()-m2.getM11());
		m1.sub(m2);
		
		assertEpsilonEquals(m3, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void transpose(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d transpose = new Matrix2d();
		
		double [] v = new double[3];
		
		m1.getRow(0, v);
		transpose.setColumn(0, v);
		
		m1.getRow(1, v);
		transpose.setColumn(1, v);
		
		m1.transpose();
		
		assertEpsilonEquals(transpose,m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void transposeMatrix2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d transpose = new Matrix2d();
		
		double [] v = new double[3];
		
		m1.getRow(0, v);
		transpose.setColumn(0, v);
		
		m1.getRow(1, v);
		transpose.setColumn(1, v);
		
		m1.transpose(m1);
		
		assertEpsilonEquals(transpose, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void determinant(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		
		Matrix2d matrix = new Matrix2d(a,b,c,d);
		
		double determinant = a*d-c*b;
		
		assertEpsilonEquals(determinant, matrix.determinant());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		
		Matrix2d matrix = new Matrix2d(a,b,c,d);
		
		double s = getRandom().nextDouble();
		Matrix2d prodScal = new Matrix2d(a*s,b*s,c*s,d*s);
		
		matrix.mul(s);
		assertEpsilonEquals(prodScal, matrix);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulDoubleMatrix2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		
		Matrix2d matrix = new Matrix2d(a,b,c,d);
		
		double s = getRandom().nextDouble();
		Matrix2d prodScal = new Matrix2d(a*s,b*s,c*s,d*s);
		
		matrix.mul(s,matrix);
		assertEpsilonEquals(prodScal, matrix);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulMatrix2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		
		//double i = getRandom().nextDouble();
		Matrix2d m1 = new Matrix2d(a,b,c,d);
		
		double e = getRandom().nextDouble();
		double f = getRandom().nextDouble();
		double g = getRandom().nextDouble();
		double h = getRandom().nextDouble();
		
		Matrix2d m2= new Matrix2d(e,f,g,h);
		
		Matrix2d prod = new Matrix2d(
				a*e+b*g,
				a*f+b*h,
				c*e+d*g,
				c*f+d*h);
				
		m1.mul(m2);
		
		assertEpsilonEquals(prod, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2d vector = this.randomVector2f();
		Matrix2d matrix = new Matrix2d(1,2,4,-1);
		
		Vector2d product = new Vector2d(vector.getX()+2*vector.getY(),4*vector.getX()-vector.getY());
		
		Vector2d result = new Vector2d();
		matrix.mul(vector, result);
		
		assertEpsilonEquals(product, result);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulMatrix2DMatrix2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		
		//double i = getRandom().nextDouble();
		Matrix2d m1 = new Matrix2d(a,b,c,d);
		
		double e = getRandom().nextDouble();
		double f = getRandom().nextDouble();
		double g = getRandom().nextDouble();
		double h = getRandom().nextDouble();
		
		Matrix2d m2= new Matrix2d(e,f,g,h);
		
		Matrix2d prod = new Matrix2d(
				a*e+b*g,
				a*f+b*h,
				c*e+d*g,
				c*f+d*h);
				
		m1.mul(m1,m2);
		
		assertEpsilonEquals(prod, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulTransposeBoth(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = this.randomMatrix2d();
		Matrix2d multrans = new Matrix2d();
		
		multrans.mulTransposeBoth(m1, m2);
		m1.transpose();
		m2.transpose();
		m1.mul(m2);
		
		assertEpsilonEquals(multrans, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulTransposeRight(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = this.randomMatrix2d();
		Matrix2d multrans = new Matrix2d();
		
		multrans.mulTransposeRight(m1, m2);
		//m1.transpose();
		m2.transpose();
		m1.mul(m2);
		
		assertEpsilonEquals(multrans, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void mulTransposeLeft(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = this.randomMatrix2d();
		Matrix2d multrans = new Matrix2d();
		
		multrans.mulTransposeLeft(m1, m2);
		m1.transpose();
		//m2.transpose();
		m1.mul(m2);
		
		assertEpsilonEquals(multrans, m1);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeCP_zero(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m = new Matrix2d();
		m.normalizeCP();
		assertNaN(m.getM00());
		assertNaN(m.getM01());
		assertNaN(m.getM10());
		assertNaN(m.getM10());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeCP_identity(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m = new Matrix2d();
		m.setIdentity();
		m.normalizeCP();
		assertEpsilonEquals(1, m.getM00());
		assertEpsilonEquals(0, m.getM01());
		assertEpsilonEquals(0, m.getM10());
		assertEpsilonEquals(1, m.getM11());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeCP_std(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m = new Matrix2d(1, 2, 3, 4);
		m.normalizeCP();
		assertEpsilonEquals(1/Math.sqrt(10), m.getM00());
		assertEpsilonEquals(2/Math.sqrt(20), m.getM01());
		assertEpsilonEquals(3/Math.sqrt(10), m.getM10());
		assertEpsilonEquals(4/Math.sqrt(20), m.getM11());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeCPMatrix2D_zero(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m = new Matrix2d();
		Matrix2d r = new Matrix2d();
		r.normalizeCP(m);
		assertNaN(r.getM00());
		assertNaN(r.getM01());
		assertNaN(r.getM00());
		assertNaN(r.getM01());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeCPMatrix2D_identity(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m = new Matrix2d();
		m.setIdentity();
		Matrix2d r = new Matrix2d();
		r.normalizeCP(m);
		assertEpsilonEquals(1, r.getM00());
		assertEpsilonEquals(0, r.getM01());
		assertEpsilonEquals(0, r.getM10());
		assertEpsilonEquals(1, r.getM11());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void normalizeCPMatrix2D_std(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m = new Matrix2d(1, 2, 3, 4);
		Matrix2d r = new Matrix2d();
		r.normalizeCP(m);
		assertEpsilonEquals(1/Math.sqrt(10), r.getM00());
		assertEpsilonEquals(2/Math.sqrt(20), r.getM01());
		assertEpsilonEquals(3/Math.sqrt(10), r.getM10());
		assertEpsilonEquals(4/Math.sqrt(20), r.getM11());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setZero(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m = this.randomMatrix2d();
		m.setZero();
		assertEpsilonEquals(new Matrix2d(0,0,0,0), m);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setDiagonal(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m = this.randomMatrix2d();
		double s = getRandom().nextDouble();
		m.setDiagonal(s,s);
		assertEpsilonEquals(new Matrix2d(s,0,0,s), m);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void negate(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = m1.clone();
		Matrix2d temp = new Matrix2d();
		Matrix2d temp2 = new Matrix2d();
		
		m1.negate();
		temp.add(m1, m2);
		assertEpsilonEquals(new Matrix2d(0,0,0,0), temp);
		temp.mul(m1, m2);
		
		m2.negate();
		assertEpsilonEquals(m1, m2);
		
		m1.negate();
		temp2.mul(m1, m2);
		assertEpsilonEquals(temp, temp2);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void negateMatrix2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = m1.clone();
		Matrix2d temp = new Matrix2d();
		Matrix2d temp2 = new Matrix2d();
		
		m1.negate(m1);
		temp.add(m1, m2);
		assertEpsilonEquals(new Matrix2d(0,0,0,0), temp);
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
		Matrix2d m = this.randomMatrix2d();
		Matrix2d m2 = m.clone();
		assertEpsilonEquals(m, m2);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void isSymmetric(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m = this.randomMatrix2d();
		boolean b = m.getM01()==m.getM10();
		assertEquals(b, m.isSymmetric());
		
		m.setZero();
		assertTrue(m.isSymmetric());
		
		m.setIdentity();
		assertTrue(m.isSymmetric());
		
		m.set(1, 1, 2, 2);
		assertFalse(m.isSymmetric());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void eigenVectorsOfSymmetricMatrix_zero(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m = new Matrix2d();
		Matrix2d eigenVectors = new Matrix2d();
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void eigenVectorsOfSymmetricMatrix_identity(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m = new Matrix2d();
		m.setIdentity();
		Matrix2d eigenVectors = new Matrix2d();
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void eigenVectorsOfSymmetricMatrix_sym(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m = new Matrix2d(1, 5, 5, 2);
		Matrix2d eigenVectors = new Matrix2d();
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void isIdentity(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m = this.randomMatrix2d();
		boolean b = m.getM00()==1 && m.getM01()==0 && m.getM10()==0 && m.getM11()==1;
		assertTrue(b==m.isIdentity());
		
		m.setIdentity();
		assertTrue(m.isIdentity());
		
		m.setZero();
		assertFalse(m.isIdentity());
		
		m.set(1, 1, 2, 2);
		assertFalse(m.isIdentity());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void covMatrix2Tuple2dArray_theory(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2d v1 = new Vector2d(1, 3);
		Vector2d v2 = new Vector2d(4, -2);
		Vector2d m = new Vector2d();
		m.add(v1,v2);
		m.scale(.5f);
		
		Matrix2d expected = new Matrix2d();
		expected.setM00(((v1.getX()-m.getX()) * (v1.getX()-m.getX()) + (v2.getX()-m.getX()) * (v2.getX()-m.getX())) / 2f);
		expected.setM01(((v1.getX()-m.getX()) * (v1.getY()-m.getY()) + (v2.getX()-m.getX()) * (v2.getY()-m.getY())) / 2f);
		expected.setM10(((v1.getY()-m.getY()) * (v1.getX()-m.getX()) + (v2.getY()-m.getY()) * (v2.getX()-m.getX())) / 2f);
		expected.setM11(((v1.getY()-m.getY()) * (v1.getY()-m.getY()) + (v2.getY()-m.getY()) * (v2.getY()-m.getY())) / 2f);
		
		Matrix2d mat = new Matrix2d();
		Vector2d mean = new Vector2d(); 
		mat.cov(mean, v1, v2);
		
		assertEpsilonEquals(m, mean);
		for(int i=0; i<2; ++i) {
			for(int j=0; j<2; ++j) {
				assertEpsilonEquals("i="+i+"; j="+j,   //$NON-NLS-1$ //$NON-NLS-2$
						expected.getElement(i, j), 
						mat.getElement(i, j));
			}
		}
	}
	
	/**
	 */
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void covMatrix2Tuple2dArray_example(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
		
		Matrix2d cov = new Matrix2d();
		Vector2d mean = new Vector2d();
		cov.cov(mean, p1, p2, p3, p4);
		
		Point2d expectedMean = new Point2d(1, -1); 
		Matrix2d expectedCov = new Matrix2d();
		expectedCov.setM00(1.5f);
		expectedCov.setM01(.5f);
		expectedCov.setM10(.5f);
		expectedCov.setM11(.5f);
		
		assertEpsilonEquals(expectedMean, mean);
		for(int i=0; i<2; ++i) {
			for(int j=0; j<2; ++j) {
				assertEpsilonEquals("i="+i+"; j="+j,   //$NON-NLS-1$ //$NON-NLS-2$
						expectedCov.getElement(i, j), 
						cov.getElement(i, j));
			}
		}
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_addMatrix2f(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = this.randomMatrix2d();
		Matrix2d m3 = new Matrix2d();
		
		m3.set(m1.getM00()+m2.getM00(), m1.getM01()+m2.getM01(), m1.getM10()+m2.getM10(), m1.getM11()+m2.getM11());
		m1.operator_add(m2);
		
		assertEpsilonEquals(m3, m1);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_addDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = m1.clone();
		double s = getRandom().nextDouble() * 100;
		
		m2.set(m2.getM00()+s, m2.getM01()+s, m2.getM10()+s, m2.getM11()+s);
		m1.operator_add(s);
		
		assertEpsilonEquals(m2, m1);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_removeMatrix2f(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = this.randomMatrix2d();
		Matrix2d m3 = new Matrix2d();
		
		m3.set(m1.getM00()-m2.getM00(), m1.getM01()-m2.getM01(), m1.getM10()-m2.getM10(), m1.getM11()-m2.getM11());
		m1.operator_remove(m2);
		
		assertEpsilonEquals(m3, m1);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_removeDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = m1.clone();
		double s = getRandom().nextDouble() * 100;
		
		m2.set(m2.getM00()-s, m2.getM01()-s, m2.getM10()-s, m2.getM11()-s);
		m1.operator_remove(s);
		
		assertEpsilonEquals(m2, m1);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_plusMatrix2f(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = this.randomMatrix2d();
		Matrix2d m3 = new Matrix2d();
		
		m3.set(m1.getM00()+m2.getM00(), m1.getM01()+m2.getM01(), m1.getM10()+m2.getM10(), m1.getM11()+m2.getM11());
		Matrix2d r = m1.operator_plus(m2);
		
		assertEpsilonEquals(m3, r);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_plusDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = m1.clone();
		double s = getRandom().nextDouble() * 100;
		
		m2.set(m2.getM00()+s, m2.getM01()+s, m2.getM10()+s, m2.getM11()+s);
		Matrix2d r = m1.operator_plus(s);
		
		assertEpsilonEquals(m2, r);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_minusMatrix2f(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = this.randomMatrix2d();
		Matrix2d m3 = new Matrix2d();
		
		m3.set(m1.getM00()-m2.getM00(), m1.getM01()-m2.getM01(), m1.getM10()-m2.getM10(), m1.getM11()-m2.getM11());
		Matrix2d r = m1.operator_minus(m2);
		
		assertEpsilonEquals(m3, r);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_minusDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = m1.clone();
		double s = getRandom().nextDouble() * 100;
		
		m2.set(m2.getM00()-s, m2.getM01()-s, m2.getM10()-s, m2.getM11()-s);
		Matrix2d r = m1.operator_minus(s);
		
		assertEpsilonEquals(m2, r);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_minus(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = m1.clone();
		
		m2.set(-m2.getM00(), -m2.getM01(), -m2.getM10(), -m2.getM11());
		Matrix2d r = m1.operator_minus();
		assertEpsilonEquals(m2, r);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_multiplyMatrix2f(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble() * 100;
		double b = getRandom().nextDouble() * 100;
		double c = getRandom().nextDouble() * 100;
		double d = getRandom().nextDouble() * 100;
		
		//double i = getRandom().nextDouble();
		Matrix2d m1 = new Matrix2d(a,b,c,d);
		
		double e = getRandom().nextDouble() * 100;
		double f = getRandom().nextDouble() * 100;
		double g = getRandom().nextDouble() * 100;
		double h = getRandom().nextDouble() * 100;
		
		Matrix2d m2= new Matrix2d(e,f,g,h);
		
		Matrix2d prod = new Matrix2d(
				a*e+b*g,
				a*f+b*h,
				c*e+d*g,
				c*f+d*h);
				
		Matrix2d r = m1.operator_multiply(m2);
		
		assertEpsilonEquals(prod, r);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_multiplyDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double a = getRandom().nextDouble() * 100;
		double b = getRandom().nextDouble() * 100;
		double c = getRandom().nextDouble() * 100;
		double d = getRandom().nextDouble() * 100;
		
		Matrix2d matrix = new Matrix2d(a,b,c,d);
		
		double s = getRandom().nextDouble() * 100;
		Matrix2d prodScal = new Matrix2d(a*s,b*s,c*s,d*s);
		
		Matrix2d r = matrix.operator_multiply(s);
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
		
		Matrix2d matrix = new Matrix2d(a,b,c,d);
		
		double s = getRandom().nextDouble() * 100;
		Matrix2d prodScal = new Matrix2d(a/s,b/s,c/s,d/s);
		
		Matrix2d r = matrix.operator_divide(s);
		assertEpsilonEquals(prodScal, r);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_plusPlus(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = m1.clone();
		
		m2.set(m2.getM00()+1, m2.getM01()+1, m2.getM10()+1, m2.getM11()+1);
		m1.operator_plusPlus();
		
		assertEpsilonEquals(m2, m1);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_moinsMoins(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d m2 = m1.clone();
		
		m2.set(m2.getM00()-1, m2.getM01()-1, m2.getM10()-1, m2.getM11()-1);
		m1.operator_moinsMoins();
		
		assertEpsilonEquals(m2, m1);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_not(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Matrix2d m1 = this.randomMatrix2d();
		Matrix2d transpose = new Matrix2d();
		
		double [] v = new double[3];
		
		m1.getRow(0, v);
		transpose.setColumn(0, v);
		
		m1.getRow(1, v);
		transpose.setColumn(1, v);
		
		Matrix2d r = m1.operator_not();
		
		assertEpsilonEquals(transpose, r);
	}

}