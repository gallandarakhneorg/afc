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
package org.arakhne.afc.math.geometry.d2;

import static org.junit.Assert.*;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.arakhne.afc.math.geometry.d2.fp.Point2fp;
import org.arakhne.afc.math.geometry.d2.fp.Vector2fp;
import org.arakhne.afc.math.matrix.Matrix3f;
import org.arakhne.afc.math.matrix.SingularMatrixException;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public class Transform2DTest extends AbstractMathTestCase {

	private static final double ANGLE = -MathConstants.DEMI_PI;

	private static final double COS = 0;

	private static final double SIN = -1;

	private static final double TRANSX = 4;

	private static final double TRANSY = 5;

	@Rule
	public CoordinateSystem2DTestRule csTestRule = new CoordinateSystem2DTestRule();

	private Transform2D transform;

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
		this.transform.setTranslation(new Vector2fp(123.456, 789.123));
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
		this.transform.translate(new Vector2fp(120, 780));
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
		Vector2D v = new Vector2fp();
		assertSame(v, this.transform.getTranslationVector(v));
		assertEpsilonEquals(TRANSX, v.getX());
		assertEpsilonEquals(TRANSY, v.getY());
	}

	@Test
	public void getRotation() {
		assertEpsilonEquals(-MathConstants.DEMI_PI, this.transform.getRotation());
	}

	@Test
	public void setRotation() {
		// Values computed with GNU Octave
		double angle = 9.87654321;
		double cos = -0.89968;
		double sin = -0.43655;
		this.transform.setRotation(angle);
		assertEpsilonEquals(cos, this.transform.getM00());
		assertEpsilonEquals(-sin, this.transform.getM01());
		assertEpsilonEquals(TRANSX, this.transform.getM02());
		assertEpsilonEquals(sin, this.transform.getM10());
		assertEpsilonEquals(cos, this.transform.getM11());
		assertEpsilonEquals(TRANSY, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void rotate() {
		// Values computed with GNU Octave
		double angle = 9.87654321;
		double cos = -0.89968;
		double sin = -0.43655;
		this.transform.rotate(angle);
		assertEpsilonEquals(sin, this.transform.getM00());
		assertEpsilonEquals(cos, this.transform.getM01());
		assertEpsilonEquals(TRANSX, this.transform.getM02());
		assertEpsilonEquals(-cos, this.transform.getM10());
		assertEpsilonEquals(sin, this.transform.getM11());
		assertEpsilonEquals(TRANSY, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void setScaleDoubleDouble() {
		// Values computed with GNU Octave
		this.transform.setScale(1.2, 3.4);
		assertEpsilonEquals(1.2, this.transform.getM00());
		assertEpsilonEquals(-SIN, this.transform.getM01());
		assertEpsilonEquals(TRANSX, this.transform.getM02());
		assertEpsilonEquals(SIN, this.transform.getM10());
		assertEpsilonEquals(3.4, this.transform.getM11());
		assertEpsilonEquals(TRANSY, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void setScaleTuple2D() {
		// Values computed with GNU Octave
		this.transform.setScale(new Vector2fp(1.2, 3.4));
		assertEpsilonEquals(1.2, this.transform.getM00());
		assertEpsilonEquals(-SIN, this.transform.getM01());
		assertEpsilonEquals(TRANSX, this.transform.getM02());
		assertEpsilonEquals(SIN, this.transform.getM10());
		assertEpsilonEquals(3.4, this.transform.getM11());
		assertEpsilonEquals(TRANSY, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void scaleDoubleDouble() {
		// Values computed with GNU Octave
		this.transform.scale(1.2, 3.4);
		assertEpsilonEquals(0, this.transform.getM00());
		assertEpsilonEquals(3.4, this.transform.getM01());
		assertEpsilonEquals(TRANSX, this.transform.getM02());
		assertEpsilonEquals(-1.2, this.transform.getM10());
		assertEpsilonEquals(0, this.transform.getM11());
		assertEpsilonEquals(TRANSY, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void scaleTuple2D() {
		// Values computed with GNU Octave
		this.transform.scale(new Vector2fp(1.2, 3.4));
		assertEpsilonEquals(0, this.transform.getM00());
		assertEpsilonEquals(3.4, this.transform.getM01());
		assertEpsilonEquals(TRANSX, this.transform.getM02());
		assertEpsilonEquals(-1.2, this.transform.getM10());
		assertEpsilonEquals(0, this.transform.getM11());
		assertEpsilonEquals(TRANSY, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void getScaleX() {
		assertEpsilonEquals(0, this.transform.getScaleX());
		this.transform.setScale(123.456, 789.123);
		assertEpsilonEquals(123.456, this.transform.getScaleX());
	}

	@Test
	public void getScaleY() {
		assertEpsilonEquals(0, this.transform.getScaleY());
		this.transform.setScale(123.456, 789.123);
		assertEpsilonEquals(789.123, this.transform.getScaleY());
	}

	@Test
	public void getScaleVector() {
		Vector2D v = new Vector2fp();
		this.transform.getScaleVector(v);
		assertEpsilonEquals(0, v.getX());
		assertEpsilonEquals(0, v.getY());

		this.transform.setScale(123.456, 789.123);
		this.transform.getScaleVector(v);
		assertEpsilonEquals(123.456, v.getX());
		assertEpsilonEquals(789.123, v.getY());
	}

	@Test
	public void setShearDoubleDouble() {
		this.transform.setShear(1.2, 3.4);
		assertEpsilonEquals(COS, this.transform.getM00());
		assertEpsilonEquals(1.2, this.transform.getM01());
		assertEpsilonEquals(TRANSX, this.transform.getM02());
		assertEpsilonEquals(3.4, this.transform.getM10());
		assertEpsilonEquals(COS, this.transform.getM11());
		assertEpsilonEquals(TRANSY, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void setShearTuple2D() {
		this.transform.setShear(new Vector2fp(1.2, 3.4));
		assertEpsilonEquals(COS, this.transform.getM00());
		assertEpsilonEquals(1.2, this.transform.getM01());
		assertEpsilonEquals(TRANSX, this.transform.getM02());
		assertEpsilonEquals(3.4, this.transform.getM10());
		assertEpsilonEquals(COS, this.transform.getM11());
		assertEpsilonEquals(TRANSY, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
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
		this.transform.shear(new Vector2fp(1.2, 3.4));
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
	public void getShearX() {
		assertEpsilonEquals(-SIN, this.transform.getShearX());
		this.transform.setShear(123.456, 789.123);
		assertEpsilonEquals(123.456, this.transform.getShearX());
	}

	@Test
	public void getShearY() {
		assertEpsilonEquals(SIN, this.transform.getShearY());
		this.transform.setShear(123.456, 789.123);
		assertEpsilonEquals(789.123, this.transform.getShearY());
	}

	@Test
	public void getShearVector() {
		Vector2D v = new Vector2fp();
		this.transform.getShearVector(v);
		assertEpsilonEquals(-SIN, v.getX());
		assertEpsilonEquals(SIN, v.getY());

		this.transform.setShear(123.456, 789.123);
		this.transform.getShearVector(v);
		assertEpsilonEquals(123.456, v.getX());
		assertEpsilonEquals(789.123, v.getY());
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
	public void makeShearMatrix() {
		double x = -0.59471;
		double y = -0.80394;
		this.transform.makeShearMatrix(x, y);
		assertEpsilonEquals(1, this.transform.getM00());
		assertEpsilonEquals(x, this.transform.getM01());
		assertEpsilonEquals(0, this.transform.getM02());
		assertEpsilonEquals(y, this.transform.getM10());
		assertEpsilonEquals(1, this.transform.getM11());
		assertEpsilonEquals(0, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
	}

	@Test
	public void transformTuple2D_translationOnly() {
		this.transform.makeTranslationMatrix(123, 456);
		Point2D p = new Point2fp(-584, 5647);
		this.transform.transform(p);
		assertEpsilonEquals(-461, p.getX());
		assertEpsilonEquals(6103, p.getY());
	}

	@Test
	public void transformTuple2D_rotationOnly() {
		this.transform.makeRotationMatrix(ANGLE);
		Point2D p = new Point2fp(1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(-1, p.getY());
	}

	@Test
	public void transformTuple2D_translationRotation() {
		this.transform.makeTranslationMatrix(123, 456);
		this.transform.setRotation(ANGLE);
		Point2D p = new Point2fp(1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(123, p.getX());
		assertEpsilonEquals(455, p.getY());
	}

	@Test
	public void transformTuple2D_scaleOnly() {
		this.transform.makeScaleMatrix(123.456, 789.123);
		Point2D p = new Point2fp(1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(123.456, p.getX());
		assertEpsilonEquals(0, p.getY());

		this.transform.makeScaleMatrix(123.456, 789.123);
		p = new Point2fp(0, 1);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(789.123, p.getY());

		this.transform.makeScaleMatrix(123.456, 789.123);
		p = new Point2fp(0.5, 2);
		this.transform.transform(p);
		assertEpsilonEquals(61.728, p.getX());
		assertEpsilonEquals(1578.246, p.getY());
	}

	@Test
	public void transformTuple2D_shearOnly() {
		this.transform.makeShearMatrix(123.456, 789.123);
		Point2D p = new Point2fp(1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(789.123, p.getY());

		this.transform.makeShearMatrix(123.456, 789.123);
		p = new Point2fp(0, 1);
		this.transform.transform(p);
		assertEpsilonEquals(123.456, p.getX());
		assertEpsilonEquals(1, p.getY());

		this.transform.makeShearMatrix(123.456, 789.123);
		p = new Point2fp(0.5, 2);
		this.transform.transform(p);
		assertEpsilonEquals(247.412, p.getX());
		assertEpsilonEquals(396.5615, p.getY());
	}

	@Test
	public void transformTuple2DTuple2D_translationOnly() {
		this.transform.makeTranslationMatrix(123, 456);
		Point2D result = new Point2fp();
		Point2D p = new Point2fp(-584, 5647);
		this.transform.transform(p, result);
		assertEpsilonEquals(-584, p.getX());
		assertEpsilonEquals(5647, p.getY());
		assertEpsilonEquals(-461, result.getX());
		assertEpsilonEquals(6103, result.getY());
	}

	@Test
	public void transformTuple2DTuple2D_rotationOnly() {
		this.transform.makeRotationMatrix(ANGLE);
		Point2D result = new Point2fp();
		Point2D p = new Point2fp(1, 0);
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
		Point2D result = new Point2fp();
		Point2D p = new Point2fp(1, 0);
		this.transform.transform(p, result);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(123, result.getX());
		assertEpsilonEquals(455, result.getY());
	}

	@Test
	public void transformTuple2DTuple2D_scaleOnly() {
		this.transform.makeScaleMatrix(123.456, 789.123);
		Point2D result = new Point2fp();
		Point2D p = new Point2fp(1, 0);
		this.transform.transform(p, result);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(123.456, result.getX());
		assertEpsilonEquals(0, result.getY());

		this.transform.makeScaleMatrix(123.456, 789.123);
		p = new Point2fp(0, 1);
		this.transform.transform(p, result);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(1, p.getY());
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(789.123, result.getY());

		this.transform.makeScaleMatrix(123.456, 789.123);
		p = new Point2fp(0.5, 2);
		this.transform.transform(p, result);
		assertEpsilonEquals(0.5, p.getX());
		assertEpsilonEquals(2, p.getY());
		assertEpsilonEquals(61.728, result.getX());
		assertEpsilonEquals(1578.246, result.getY());
	}

	@Test
	public void transformTuple2DTuple2D_shearOnly() {
		this.transform.makeShearMatrix(123.456, 789.123);
		Point2D result = new Point2fp();
		Point2D p = new Point2fp(1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(789.123, p.getY());

		this.transform.makeShearMatrix(123.456, 789.123);
		p = new Point2fp(0, 1);
		this.transform.transform(p);
		assertEpsilonEquals(123.456, p.getX());
		assertEpsilonEquals(1, p.getY());

		this.transform.makeShearMatrix(123.456, 789.123);
		p = new Point2fp(0.5, 2);
		this.transform.transform(p);
		assertEpsilonEquals(247.412, p.getX());
		assertEpsilonEquals(396.5615, p.getY());
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

}