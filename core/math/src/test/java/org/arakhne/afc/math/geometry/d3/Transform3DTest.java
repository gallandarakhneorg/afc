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
package org.arakhne.afc.math.geometry.d3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3DTestRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.matrix.SingularMatrixException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public class Transform3DTest extends AbstractMathTestCase {

	private static final double ANGLE = -MathConstants.DEMI_PI;

	private static final double COS = 0;

	private static final double SIN = -1;

	private static final double TRANSX = 4;

	private static final double TRANSY = 5;

	@Rule
	public CoordinateSystem3DTestRule csTestRule = new CoordinateSystem3DTestRule();

	private Transform3D transform;

	@Before
	public void setUp() throws Exception {
		this.transform = new Transform2D(
				COS, -SIN, TRANSX,
				SIN,  COS, TRANSY);
	}

	@After
	public void tearDown() throws Exception {
		this.transform = null;
	}

	@Test
	public void setDoubleDoubleDoubleDoubleDoubleDouble() {
		this.transform.set(1, 2, 3, 4, 5, 6);
		assertEpsilonEquals(1, this.transform.getM00());
		assertEpsilonEquals(2, this.transform.getM01());
		assertEpsilonEquals(3, this.transform.getM02());
		assertEpsilonEquals(4, this.transform.getM10());
		assertEpsilonEquals(5, this.transform.getM11());
		assertEpsilonEquals(6, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void setTranslationDoubleDouble() {
		// Values computed with GNU Octave
		this.transform.setTranslation(123.456, 789.123);
		assertEpsilonEquals(COS, this.transform.getM00());
		assertEpsilonEquals(-SIN, this.transform.getM01());
		assertEpsilonEquals(123.456, this.transform.getM02());
		assertEpsilonEquals(SIN, this.transform.getM10());
		assertEpsilonEquals(COS, this.transform.getM11());
		assertEpsilonEquals(789.123, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void setTranslationTuple2D() {
		// Values computed with GNU Octave
		this.transform.setTranslation(new Vector2d(123.456, 789.123));
		assertEpsilonEquals(COS, this.transform.getM00());
		assertEpsilonEquals(-SIN, this.transform.getM01());
		assertEpsilonEquals(123.456, this.transform.getM02());
		assertEpsilonEquals(SIN, this.transform.getM10());
		assertEpsilonEquals(COS, this.transform.getM11());
		assertEpsilonEquals(789.123, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void translateDoubleDouble() {
		// Values computed with GNU Octave
		this.transform.translate(120, 780);
		assertEpsilonEquals(COS, this.transform.getM00());
		assertEpsilonEquals(-SIN, this.transform.getM01());
		assertEpsilonEquals(784, this.transform.getM02());
		assertEpsilonEquals(SIN, this.transform.getM10());
		assertEpsilonEquals(COS, this.transform.getM11());
		assertEpsilonEquals(-115, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void translateVector2D() {
		// Values computed with GNU Octave
		this.transform.translate(new Vector2d(120, 780));
		assertEpsilonEquals(COS, this.transform.getM00());
		assertEpsilonEquals(-SIN, this.transform.getM01());
		assertEpsilonEquals(784, this.transform.getM02());
		assertEpsilonEquals(SIN, this.transform.getM10());
		assertEpsilonEquals(COS, this.transform.getM11());
		assertEpsilonEquals(-115, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void getTranslationX() {
		assertEpsilonEquals(TRANSX, this.transform.getTranslationX());
	}

	@Test
	public void getTranslationY() {
		assertEpsilonEquals(TRANSY, this.transform.getTranslationY());
	}

	@Test
	public void getTranslationVector() {
		Vector2D v = new Vector2d();
		this.transform.getTranslationVector(v);
		assertEpsilonEquals(TRANSX, v.getX());
		assertEpsilonEquals(TRANSY, v.getY());
	}

	@Test
	public void shearDoubleDouble() {
		// Values computed with GNU Octave
		this.transform.shear(1.2, 3.4);
		assertEpsilonEquals(3.4, this.transform.getM00());
		assertEpsilonEquals(1, this.transform.getM01());
		assertEpsilonEquals(TRANSX, this.transform.getM02());
		assertEpsilonEquals(-1, this.transform.getM10());
		assertEpsilonEquals(-1.2, this.transform.getM11());
		assertEpsilonEquals(TRANSY, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void shearTuple2D() {
		// Values computed with GNU Octave
		this.transform.shear(new Vector2d(1.2, 3.4));
		assertEpsilonEquals(3.4, this.transform.getM00());
		assertEpsilonEquals(1, this.transform.getM01());
		assertEpsilonEquals(TRANSX, this.transform.getM02());
		assertEpsilonEquals(-1, this.transform.getM10());
		assertEpsilonEquals(-1.2, this.transform.getM11());
		assertEpsilonEquals(TRANSY, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void makeRotationMatrix() {
		// Values computed with GNU Octave
		double angle = 123.456;
		double cos = -0.59471;
		double sin = -0.80394;
		this.transform.makeRotationMatrix(angle);
		assertEpsilonEquals(cos, this.transform.getM00());
		assertEpsilonEquals(-sin, this.transform.getM01());
		assertEpsilonEquals(0, this.transform.getM02());
		assertEpsilonEquals(sin, this.transform.getM10());
		assertEpsilonEquals(cos, this.transform.getM11());
		assertEpsilonEquals(0, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void makeTranslationMatrix() {
		double x = -0.59471;
		double y = -0.80394;
		this.transform.makeTranslationMatrix(x, y);
		assertEpsilonEquals(1, this.transform.getM00());
		assertEpsilonEquals(0, this.transform.getM01());
		assertEpsilonEquals(x, this.transform.getM02());
		assertEpsilonEquals(0, this.transform.getM10());
		assertEpsilonEquals(1, this.transform.getM11());
		assertEpsilonEquals(y, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void makeScaleMatrix() {
		double x = -0.59471;
		double y = -0.80394;
		this.transform.makeScaleMatrix(x, y);
		assertEpsilonEquals(x, this.transform.getM00());
		assertEpsilonEquals(0, this.transform.getM01());
		assertEpsilonEquals(0, this.transform.getM02());
		assertEpsilonEquals(0, this.transform.getM10());
		assertEpsilonEquals(y, this.transform.getM11());
		assertEpsilonEquals(0, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void transformTuple2D_translationOnly() {
		this.transform.makeTranslationMatrix(123, 456);
		Point2D p = new Point2d(-584, 5647);
		this.transform.transform(p);
		assertEpsilonEquals(-461, p.getX());
		assertEpsilonEquals(6103, p.getY());
	}

	@Test
	public void transformTuple2D_rotationOnly() {
		this.transform.makeRotationMatrix(ANGLE);
		Point2D p = new Point2d(1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(-1, p.getY());
	}

	@Test
	public void transformTuple2D_translationRotation() {
		this.transform.makeTranslationMatrix(123, 456);
		this.transform.setRotation(ANGLE);
		Point2D p = new Point2d(1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(123, p.getX());
		assertEpsilonEquals(455, p.getY());
	}

	@Test
	public void transformTuple2D_scaleOnly() {
		this.transform.makeScaleMatrix(123.456, 789.123);
		Point2D p = new Point2d(1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(123.456, p.getX());
		assertEpsilonEquals(0, p.getY());

		this.transform.makeScaleMatrix(123.456, 789.123);
		p = new Point2d(0, 1);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(789.123, p.getY());

		this.transform.makeScaleMatrix(123.456, 789.123);
		p = new Point2d(0.5, 2);
		this.transform.transform(p);
		assertEpsilonEquals(61.728, p.getX());
		assertEpsilonEquals(1578.246, p.getY());
	}

	@Test
	public void transformTuple2DTuple2D_translationOnly() {
		this.transform.makeTranslationMatrix(123, 456);
		Point2D result = new Point2d();
		Point2D p = new Point2d(-584, 5647);
		this.transform.transform(p, result);
		assertEpsilonEquals(-584, p.getX());
		assertEpsilonEquals(5647, p.getY());
		assertEpsilonEquals(-461, result.getX());
		assertEpsilonEquals(6103, result.getY());
	}

	@Test
	public void transformTuple2DTuple2D_rotationOnly() {
		this.transform.makeRotationMatrix(ANGLE);
		Point2D result = new Point2d();
		Point2D p = new Point2d(1, 0);
		this.transform.transform(p, result);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(-1, result.getY());
	}

	@Test
	public void transformTuple2DTuple2D_translationRotation() {
		this.transform.makeTranslationMatrix(123, 456);
		this.transform.setRotation(ANGLE);
		Point2D result = new Point2d();
		Point2D p = new Point2d(1, 0);
		this.transform.transform(p, result);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(123, result.getX());
		assertEpsilonEquals(455, result.getY());
	}

	@Test
	public void transformTuple2DTuple2D_scaleOnly() {
		this.transform.makeScaleMatrix(123.456, 789.123);
		Point2D result = new Point2d();
		Point2D p = new Point2d(1, 0);
		this.transform.transform(p, result);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(123.456, result.getX());
		assertEpsilonEquals(0, result.getY());

		this.transform.makeScaleMatrix(123.456, 789.123);
		p = new Point2d(0, 1);
		this.transform.transform(p, result);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(1, p.getY());
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(789.123, result.getY());

		this.transform.makeScaleMatrix(123.456, 789.123);
		p = new Point2d(0.5, 2);
		this.transform.transform(p, result);
		assertEpsilonEquals(0.5, p.getX());
		assertEpsilonEquals(2, p.getY());
		assertEpsilonEquals(61.728, result.getX());
		assertEpsilonEquals(1578.246, result.getY());
	}

	@Test
	public void invert() {
		// Values computed with GNU octave
		this.transform.invert();
		assertEpsilonEquals(COS, this.transform.getM00());
		assertEpsilonEquals(SIN, this.transform.getM01());
		assertEpsilonEquals(TRANSY, this.transform.getM02());
		assertEpsilonEquals(-SIN, this.transform.getM10());
		assertEpsilonEquals(COS, this.transform.getM11());
		assertEpsilonEquals(-TRANSX, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void createInverse() {
		// Values computed with GNU octave
		Transform2D inverse = this.transform.createInverse();
		assertNotNull(inverse);
		assertNotSame(this.transform, inverse);
		assertEpsilonEquals(COS, inverse.getM00());
		assertEpsilonEquals(SIN, inverse.getM01());
		assertEpsilonEquals(TRANSY, inverse.getM02());
		assertEpsilonEquals(-SIN, inverse.getM10());
		assertEpsilonEquals(COS, inverse.getM11());
		assertEpsilonEquals(-TRANSX, inverse.getM12());
		assertEpsilonEquals(0, inverse.getM20());
		assertEpsilonEquals(0, inverse.getM21());
		assertEpsilonEquals(1, inverse.getM22());
	}
	
	/**
	 * Set this matrix with the invert transformation of the givene matrix.
	 * The inverse transform Tx' of this transform Tx
	 * maps coordinates transformed by Tx back
	 * to their original coordinates.
	 * In other words, Tx'(Tx(p)) = p = Tx(Tx'(p)).
	 * <p>
	 * If this transform maps all coordinates onto a point or a line
	 * then it will not have an inverse, since coordinates that do
	 * not lie on the destination point or line will not have an inverse
	 * mapping.
	 * The <code>determinant</code> method can be used to determine if this
	 * transform has no inverse, in which case an exception will be
	 * thrown if the <code>createInverse</code> method is called.
	 * @param matrix is the matrix to invert
	 * @see #determinant()
	 * @throws SingularMatrixException if the matrix cannot be inverted.
	 */
	@Test
	public void invertMatrix3f() {
		Transform2D inverse = new Transform2D();
		inverse.invert(this.transform);
		assertEpsilonEquals(COS, inverse.getM00());
		assertEpsilonEquals(SIN, inverse.getM01());
		assertEpsilonEquals(TRANSY, inverse.getM02());
		assertEpsilonEquals(-SIN, inverse.getM10());
		assertEpsilonEquals(COS, inverse.getM11());
		assertEpsilonEquals(-TRANSX, inverse.getM12());
		assertEpsilonEquals(0, inverse.getM20());
		assertEpsilonEquals(0, inverse.getM21());
		assertEpsilonEquals(1, inverse.getM22());
	}

	@Test
	public void getScaleVector_alone() {
		this.transform.setIdentity();
		Vector2d v = new Vector2d();
		
		this.transform.getScaleVector(v);
		assertFpVectorEquals(1, 1, v);

		this.transform.scale(1.256, -25);
		this.transform.getScaleVector(v);
		assertFpVectorEquals(1.256, 25, v);

		this.transform.scale(32, -47);
		this.transform.getScaleVector(v);
		assertFpVectorEquals(1.256 * 32, 25 * 47, v);
	}

	@Test
	public void getScaleVector_withRotation() {
		this.transform.setIdentity();
		this.transform.rotate(2.214523);
		Vector2d v = new Vector2d();
		
		this.transform.getScaleVector(v);
		assertFpVectorEquals(1, 1, v);

		this.transform.scale(1.256, -25);
		this.transform.getScaleVector(v);
		assertFpVectorEquals(1.256, 25, v);

		this.transform.scale(32, -47);
		this.transform.getScaleVector(v);
		assertFpVectorEquals(1.256 * 32, 25 * 47, v);
	}

	@Test
	public void getScaleX_alone() {
		this.transform.setIdentity();
		assertEpsilonEquals(1, this.transform.getScaleX());
		this.transform.scale(1.256, -25);
		assertEpsilonEquals(1.256, this.transform.getScaleX());
		this.transform.scale(32, -47);
		assertEpsilonEquals(1.256 * 32, this.transform.getScaleX());
	}

	@Test
	public void getScaleX_withRotation() {
		this.transform.setIdentity();
		this.transform.rotate(2.214523);
		assertEpsilonEquals(1, this.transform.getScaleX());
		this.transform.scale(1.256, 1);
		assertEpsilonEquals(1.256, this.transform.getScaleX());
		this.transform.scale(32, 1);
		assertEpsilonEquals(1.256 * 32, this.transform.getScaleX());
	}

	@Test
	public void getScaleY_alone() {
		this.transform.setIdentity();
		assertEpsilonEquals(1, this.transform.getScaleY());
		this.transform.scale(1.256, -25);
		assertEpsilonEquals(25, this.transform.getScaleY());
		this.transform.scale(32, 47);
		assertEpsilonEquals(25 * 47, this.transform.getScaleY());
	}

	@Test
	public void getScaleY_withrotation() {
		this.transform.setIdentity();
		this.transform.rotate(2.214523);
		assertEpsilonEquals(1, this.transform.getScaleY());
		this.transform.scale(1.256, -25);
		assertEpsilonEquals(25, this.transform.getScaleY());
		this.transform.scale(32, 47);
		assertEpsilonEquals(25 * 47, this.transform.getScaleY());
	}

	@Test
	public void scaleDoubleDouble_alone() {
		this.transform.setIdentity();
		this.transform.scale(5.2365, 4.586);
		assertEpsilonEquals(5.2365, this.transform.getScaleX());
		assertEpsilonEquals(4.586, this.transform.getScaleY());
		this.transform.scale(0.123, 0.568);
		assertEpsilonEquals(5.2365 * 0.123, this.transform.getScaleX());
		assertEpsilonEquals(4.586 * 0.568, this.transform.getScaleY());
	}

	@Test
	public void scaleDoubleDouble_withRotation() {
		this.transform.setIdentity();
		this.transform.rotate(2.214523);
		this.transform.scale(5.2365, 4.586);
		assertEpsilonEquals(5.2365, this.transform.getScaleX());
		assertEpsilonEquals(4.586, this.transform.getScaleY());
		this.transform.scale(0.123, 0.568);
		assertEpsilonEquals(5.2365 * 0.123, this.transform.getScaleX());
		assertEpsilonEquals(4.586 * 0.568, this.transform.getScaleY());
	}

	@Test
	public void scaleTuple2D_alone() {
		this.transform.setIdentity();
		this.transform.scale(new Vector2d(5.2365, 4.586));
		assertEpsilonEquals(5.2365, this.transform.getScaleX());
		assertEpsilonEquals(4.586, this.transform.getScaleY());
		this.transform.scale(new Vector2d(0.123, 0.568));
		assertEpsilonEquals(5.2365 * 0.123, this.transform.getScaleX());
		assertEpsilonEquals(4.586 * 0.568, this.transform.getScaleY());
	}

	@Test
	public void scaleTuple2D_withRotation() {
		this.transform.setIdentity();
		this.transform.rotate(2.214523);
		this.transform.scale(new Vector2d(5.2365, 4.586));
		assertEpsilonEquals(5.2365, this.transform.getScaleX());
		assertEpsilonEquals(4.586, this.transform.getScaleY());
		this.transform.scale(new Vector2d(0.123, 0.568));
		assertEpsilonEquals(5.2365 * 0.123, this.transform.getScaleX());
		assertEpsilonEquals(4.586 * 0.568, this.transform.getScaleY());
	}

	@Test
	public void scaleDouble_alone() {
		this.transform.setIdentity();
		this.transform.scale(5.2365);
		assertEpsilonEquals(5.2365, this.transform.getScaleX());
		assertEpsilonEquals(5.2365, this.transform.getScaleY());
		this.transform.scale(0.123);
		assertEpsilonEquals(5.2365 * 0.123, this.transform.getScaleX());
		assertEpsilonEquals(5.2365 * 0.123, this.transform.getScaleY());
	}

	@Test
	public void scaleDouble_withRotation() {
		this.transform.setIdentity();
		this.transform.rotate(2.214523);
		this.transform.scale(5.2365);
		assertEpsilonEquals(5.2365, this.transform.getScaleX());
		assertEpsilonEquals(5.2365, this.transform.getScaleY());
		this.transform.scale(0.123);
		assertEpsilonEquals(5.2365 * 0.123, this.transform.getScaleX());
		assertEpsilonEquals(5.2365 * 0.123, this.transform.getScaleY());
	}

	@Test
	public void getScale_alone() {
		this.transform.setIdentity();
		assertEpsilonEquals(1, this.transform.getScale());
		this.transform.scale(5.2365, 4.586);
		assertEpsilonEquals(5.2365, this.transform.getScale());
		this.transform.scale(0.123, 0.568);
		assertEpsilonEquals(4.586 * 0.568, this.transform.getScale());
	}

	@Test
	public void getScale_withRotation() {
		this.transform.setIdentity();
		this.transform.rotate(2.214523);
		assertEpsilonEquals(1, this.transform.getScale());
		this.transform.scale(5.2365, 4.586);
		assertEpsilonEquals(5.2365, this.transform.getScale());
		this.transform.scale(0.123, 0.568);
		assertEpsilonEquals(4.586 * 0.568, this.transform.getScale());
	}

	@Test
	public void setScaleDoubleDouble_alone() {
		this.transform.setIdentity();
		this.transform.setScale(5.2365, 4.586);
		assertEpsilonEquals(5.2365, this.transform.getScaleX());
		assertEpsilonEquals(4.586, this.transform.getScaleY());
		this.transform.setScale(0.123, 0.568);
		assertEpsilonEquals(0.123, this.transform.getScaleX());
		assertEpsilonEquals(0.568, this.transform.getScaleY());
	}

	@Test
	public void setScaleDoubleDouble_withRotation() {
		this.transform.setIdentity();
		this.transform.rotate(2.214523);
		this.transform.setScale(5.2365, 4.586);
		assertEpsilonEquals(5.2365, this.transform.getScaleX());
		assertEpsilonEquals(4.586, this.transform.getScaleY());
		this.transform.setScale(0.123, 0.568);
		assertEpsilonEquals(0.123, this.transform.getScaleX());
		assertEpsilonEquals(0.568, this.transform.getScaleY());
	}

	@Test
	public void setScaleTuple2D_alone() {
		this.transform.setIdentity();
		this.transform.setScale(new Vector2d(5.2365, 4.586));
		assertEpsilonEquals(5.2365, this.transform.getScaleX());
		assertEpsilonEquals(4.586, this.transform.getScaleY());
		this.transform.setScale(new Vector2d(0.123, 0.568));
		assertEpsilonEquals(0.123, this.transform.getScaleX());
		assertEpsilonEquals(0.568, this.transform.getScaleY());
	}

	@Test
	public void setScaleTuple2D_withRotation() {
		this.transform.setIdentity();
		this.transform.rotate(2.214523);
		this.transform.setScale(new Vector2d(5.2365, 4.586));
		assertEpsilonEquals(5.2365, this.transform.getScaleX());
		assertEpsilonEquals(4.586, this.transform.getScaleY());
		this.transform.setScale(new Vector2d(0.123, 0.568));
		assertEpsilonEquals(0.123, this.transform.getScaleX());
		assertEpsilonEquals(0.568, this.transform.getScaleY());
	}

	@Test
	public void getRotation_alone() {
		this.transform.setIdentity();
		assertEpsilonEquals(0, this.transform.getRotation());
		this.transform.rotate(2.124548);
		assertEpsilonEquals(2.124548, this.transform.getRotation());
		this.transform.rotate(-2.124548);
		assertEpsilonEquals(0, this.transform.getRotation());
		this.transform.rotate(0.265);
		assertEpsilonEquals(0.265, this.transform.getRotation());
		this.transform.rotate(0.1);
		assertEpsilonEquals(0.265 + 0.1, this.transform.getRotation());
		this.transform.scale(2);
		assertEpsilonEquals(0.265 + 0.1, this.transform.getRotation());
	}
	
	@Test
	public void getRotation_withScale() {
		this.transform.setIdentity();
		this.transform.scale(3.56);
		assertEpsilonEquals(0, this.transform.getRotation());
		this.transform.rotate(2.124548);
		assertEpsilonEquals(2.124548, this.transform.getRotation());
		this.transform.rotate(-2.124548);
		assertEpsilonEquals(0, this.transform.getRotation());
		this.transform.rotate(0.265);
		assertEpsilonEquals(0.265, this.transform.getRotation());
		this.transform.rotate(0.1);
		assertEpsilonEquals(0.265 + 0.1, this.transform.getRotation());
		this.transform.scale(2);
		assertEpsilonEquals(0.265 + 0.1, this.transform.getRotation());
	}

	@Test
	public void rotateDouble_alone() {
		this.transform.setIdentity();
		assertEpsilonEquals(0, this.transform.getRotation());
		this.transform.rotate(2.124548);
		assertEpsilonEquals(2.124548, this.transform.getRotation());
		this.transform.rotate(-2.124548);
		assertEpsilonEquals(0, this.transform.getRotation());
		this.transform.rotate(0.265);
		assertEpsilonEquals(0.265, this.transform.getRotation());
		this.transform.rotate(0.1);
		assertEpsilonEquals(0.265 + 0.1, this.transform.getRotation());
		this.transform.scale(2);
		assertEpsilonEquals(0.265 + 0.1, this.transform.getRotation());
	}

	@Test
	public void setRotationDouble_alone() {
		this.transform.setIdentity();
		this.transform.setRotation(0.265);
		assertEpsilonEquals(0.265, this.transform.getRotation());
		this.transform.setRotation(0.1);
		assertEpsilonEquals(0.1, this.transform.getRotation());
	}
	
	@Test
	public void setRotationDouble_withScale() {
		this.transform.setIdentity();
				
		this.transform.scale(2.101, 4.52);
		this.transform.setRotation(0.265);
		assertEpsilonEquals(2.101, this.transform.getScaleX());
		assertEpsilonEquals(4.52, this.transform.getScaleY());
		assertEpsilonEquals(0.265, this.transform.getRotation());

		this.transform.setRotation(0.1);
		assertEpsilonEquals(2.101, this.transform.getScaleX());
		assertEpsilonEquals(4.52, this.transform.getScaleY());
		assertEpsilonEquals("CS: " + CoordinateSystem2D.getDefaultCoordinateSystem(),
				0.1, this.transform.getRotation());
	}

	@Test
	public void setRotationDouble_withScale2() {
		this.transform.setIdentity();
		
		this.transform.setRotation(0.265);
		this.transform.scale(2.101, 4.52);
		assertEpsilonEquals(2.101, this.transform.getScaleX());
		assertEpsilonEquals(4.52, this.transform.getScaleY());
		assertEpsilonEquals(0.265, this.transform.getRotation());
		
		this.transform.setRotation(0.1);
		assertEpsilonEquals(2.101, this.transform.getScaleX());
		assertEpsilonEquals(4.52, this.transform.getScaleY());
		assertEpsilonEquals("CS: " + CoordinateSystem2D.getDefaultCoordinateSystem(),
				0.1, this.transform.getRotation());
	}

}