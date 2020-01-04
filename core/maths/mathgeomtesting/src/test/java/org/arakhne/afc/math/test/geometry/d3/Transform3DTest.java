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

package org.arakhne.afc.math.test.geometry.d3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.d.Point3d;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.arakhne.afc.math.test.AbstractMathTestCase;

@SuppressWarnings("all")
@Disabled("temporary")
public class Transform3DTest extends AbstractMathTestCase {

	private static final double ANGLE = -MathConstants.DEMI_PI;

	private static final double COS = 0;

	private static final double SIN = -1;

	private static final double TRANSX = 4;

	private static final double TRANSY = 5;

	private Transform3D transform;

	@BeforeEach
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void setUp(CoordinateSystem3D cs) throws Exception {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform = new Transform3D(
				COS, -SIN, TRANSX, 0,
				SIN,  COS, TRANSY, 0,
				  0,    0,      0, 0);
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.transform = null;
	}

	@Test
	public void setDoubleDoubleDoubleDoubleDoubleDouble() {
		this.transform.set(1, 2, 0, 0, 3, 4, 0, 0, 5, 6, 0, 0);
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
		this.transform.setTranslation(123.456, 789.123, 0);
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
		this.transform.setTranslation(new Vector3d(123.456, 789.123, 0));
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
		this.transform.translate(120, 780, 0);
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
	public void translateVector3D() {
		// Values computed with GNU Octave
		this.transform.translate(new Vector3d(120, 780, 0));
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
		double z = 0;
		this.transform.makeTranslationMatrix(x, y, z);
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
	public void transformTuple2D_translationOnly() {
		this.transform.makeTranslationMatrix(123, 456, 0);
		Point3D p = new Point3d(-584, 5647, 0);
		this.transform.transform(p);
		assertEpsilonEquals(-461, p.getX());
		assertEpsilonEquals(6103, p.getY());
	}

	@Test
	public void transformTuple2D_rotationOnly() {
		this.transform.makeRotationMatrix(ANGLE);
		Point3D p = new Point3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(-1, p.getY());
	}

	@Test
	public void transformTuple2D_translationRotation() {
		this.transform.makeTranslationMatrix(123, 456, 0);
		this.transform.setRotation(ANGLE);
		Point3D p = new Point3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(123, p.getX());
		assertEpsilonEquals(455, p.getY());
	}

	@Test
	public void transformTuple2DTuple2D_translationOnly() {
		this.transform.makeTranslationMatrix(123, 456, 0);
		Point3D result = new Point3d();
		Point3D p = new Point3d(-584, 5647, 0);
		this.transform.transform(p, result);
		assertEpsilonEquals(-584, p.getX());
		assertEpsilonEquals(5647, p.getY());
		assertEpsilonEquals(-461, result.getX());
		assertEpsilonEquals(6103, result.getY());
	}

	@Test
	public void transformTuple2DTuple2D_rotationOnly() {
		this.transform.makeRotationMatrix(ANGLE);
		Point3D result = new Point3d();
		Point3D p = new Point3d(1, 0, 0);
		this.transform.transform(p, result);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(-1, result.getY());
	}

	@Test
	public void transformTuple2DTuple2D_translationRotation() {
		this.transform.makeTranslationMatrix(123, 456, 0);
		this.transform.setRotation(ANGLE);
		Point3D result = new Point3d();
		Point3D p = new Point3d(1, 0, 0);
		this.transform.transform(p, result);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(123, result.getX());
		assertEpsilonEquals(455, result.getY());
	}

}
