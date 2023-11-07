/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Quaternion.QuaternionComponents;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.d.GeomFactory3d;
import org.arakhne.afc.math.geometry.d3.d.Point3d;
import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.arakhne.afc.math.test.AbstractMathTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public class Transform3DTest extends AbstractMathTestCase {

	private static final double TRANSX = 4;

	private static final double TRANSY = 5;

	private static final double TRANSZ = 6;

	private static final double ROT00 = 0.3333333331143723;

	private static final double ROT01 = 0.9106836021143723;

	private static final double ROT02 = 0.24401693588562773;

	private static final double ROT10 = -0.24401693588562773;

	private static final double ROT11 = 0.3333333331143723;

	private static final double ROT12 = 0.9106836021143723;

	private static final double ROT20 = 0.9106836021143723;

	private static final double ROT21 = -0.24401693588562773;

	private static final double ROT22 = 0.3333333331143723;

	private static final double ROTAXISX = 0.577350269;

	private static final double ROTAXISY = 0.577350269;

	private static final double ROTAXISZ = 0.577350269;

	private static final double ROTANGLE = -1.5707963268;

	private static final double QUATX;

	private static final double QUATY;

	private static final double QUATZ;

	private static final double QUATW;

	static {
		final QuaternionComponents comps = Quaternion.computeWithAxisAngle(ROTAXISX, ROTAXISY, ROTAXISZ, ROTANGLE);
		QUATX = comps.x();
		QUATY = comps.y();
		QUATZ = comps.z();
		QUATW = comps.w();
	}

	private Transform3D transform;

	private static GeomFactory3d FACTORY = new GeomFactory3d();

	@BeforeEach
	public void setUp() throws Exception {
		this.transform = new Transform3D(
				ROT00, ROT01, ROT02, TRANSX,
				ROT10, ROT11, ROT12, TRANSY,
				ROT20, ROT21, ROT22, TRANSZ);
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.transform = null;
	}

	@DisplayName("set(double,double,double,double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void setDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.set(1, 2, 0, 0, 3, 4, 0, 0, 5, 6, 0, 0);
		assertEpsilonEquals(1, this.transform.getM00());
		assertEpsilonEquals(2, this.transform.getM01());
		assertEpsilonEquals(0, this.transform.getM02());
		assertEpsilonEquals(0, this.transform.getM03());
		assertEpsilonEquals(3, this.transform.getM10());
		assertEpsilonEquals(4, this.transform.getM11());
		assertEpsilonEquals(0, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM13());
		assertEpsilonEquals(5, this.transform.getM20());
		assertEpsilonEquals(6, this.transform.getM21());
		assertEpsilonEquals(0, this.transform.getM22());
		assertEpsilonEquals(0, this.transform.getM23());
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());
	}

	@DisplayName("setTranslation(double,double,double)")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void setTranslationDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		// Values computed with GNU Octave
		this.transform.setTranslation(123.456, 789.123, 57);
		assertEpsilonEquals(ROT00, this.transform.getM00());
		assertEpsilonEquals(ROT01, this.transform.getM01());
		assertEpsilonEquals(ROT02, this.transform.getM02());
		assertEpsilonEquals(123.456, this.transform.getM03());
		assertEpsilonEquals(ROT10, this.transform.getM10());
		assertEpsilonEquals(ROT11, this.transform.getM11());
		assertEpsilonEquals(ROT12, this.transform.getM12());
		assertEpsilonEquals(789.123, this.transform.getM13());
		assertEpsilonEquals(ROT20, this.transform.getM20());
		assertEpsilonEquals(ROT21, this.transform.getM21());
		assertEpsilonEquals(ROT22, this.transform.getM22());
		assertEpsilonEquals(57, this.transform.getM23());
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());
	}

	@DisplayName("setTranslation(Tuple3D)")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void setTranslationTuple3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		// Values computed with GNU Octave
		this.transform.setTranslation(new Vector3d(123.456, 789.123, 57));
		assertEpsilonEquals(ROT00, this.transform.getM00());
		assertEpsilonEquals(ROT01, this.transform.getM01());
		assertEpsilonEquals(ROT02, this.transform.getM02());
		assertEpsilonEquals(123.456, this.transform.getM03());
		assertEpsilonEquals(ROT10, this.transform.getM10());
		assertEpsilonEquals(ROT11, this.transform.getM11());
		assertEpsilonEquals(ROT12, this.transform.getM12());
		assertEpsilonEquals(789.123, this.transform.getM13());
		assertEpsilonEquals(ROT20, this.transform.getM20());
		assertEpsilonEquals(ROT21, this.transform.getM21());
		assertEpsilonEquals(ROT22, this.transform.getM22());
		assertEpsilonEquals(57, this.transform.getM23());
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());
	}

	@DisplayName("translate(double,double,double)")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void translateDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		// Values computed with GNU Octave
		this.transform.translate(120, 780, 0);
		assertEpsilonEquals(ROT00, this.transform.getM00());
		assertEpsilonEquals(ROT01, this.transform.getM01());
		assertEpsilonEquals(ROT02, this.transform.getM02());
		assertEpsilonEquals(124, this.transform.getM03());
		assertEpsilonEquals(ROT10, this.transform.getM10());
		assertEpsilonEquals(ROT11, this.transform.getM11());
		assertEpsilonEquals(ROT12, this.transform.getM12());
		assertEpsilonEquals(785, this.transform.getM13());
		assertEpsilonEquals(ROT20, this.transform.getM20());
		assertEpsilonEquals(ROT21, this.transform.getM21());
		assertEpsilonEquals(ROT22, this.transform.getM22());
		assertEpsilonEquals(6, this.transform.getM23());
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());
	}

	@DisplayName("translate(double,double,double)")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		// Values computed with GNU Octave
		this.transform.translate(new Vector3d(120, 780, 0));
		assertEpsilonEquals(ROT00, this.transform.getM00());
		assertEpsilonEquals(ROT01, this.transform.getM01());
		assertEpsilonEquals(ROT02, this.transform.getM02());
		assertEpsilonEquals(124, this.transform.getM03());
		assertEpsilonEquals(ROT10, this.transform.getM10());
		assertEpsilonEquals(ROT11, this.transform.getM11());
		assertEpsilonEquals(ROT12, this.transform.getM12());
		assertEpsilonEquals(785, this.transform.getM13());
		assertEpsilonEquals(ROT20, this.transform.getM20());
		assertEpsilonEquals(ROT21, this.transform.getM21());
		assertEpsilonEquals(ROT22, this.transform.getM22());
		assertEpsilonEquals(6, this.transform.getM23());
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());
	}

	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getTranslationX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(TRANSX, this.transform.getTranslationX());
	}

	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getTranslationY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(TRANSY, this.transform.getTranslationY());
	}

	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getTranslationZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(TRANSZ, this.transform.getTranslationZ());
	}

	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getTranslation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(new Vector3d(TRANSX, TRANSY, TRANSZ), this.transform.getTranslation(this.FACTORY));
	}

	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void makeTranslationMatrix(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double x = -0.59471;
		double y = -0.80394;
		double z = 0;
		this.transform.makeTranslationMatrix(x, y, z);
		assertEpsilonEquals(1, this.transform.getM00());
		assertEpsilonEquals(0, this.transform.getM01());
		assertEpsilonEquals(0, this.transform.getM02());
		assertEpsilonEquals(x, this.transform.getM03());
		assertEpsilonEquals(0, this.transform.getM10());
		assertEpsilonEquals(1, this.transform.getM11());
		assertEpsilonEquals(0, this.transform.getM12());
		assertEpsilonEquals(y, this.transform.getM13());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
		assertEpsilonEquals(z, this.transform.getM23());
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());
	}

	@DisplayName("makeRotationMatrix not rotation")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void makeRotationMatrix_reset(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(new Quaternion4d());
		assertEpsilonEquals(1, this.transform.getM00());
		assertEpsilonEquals(0, this.transform.getM01());
		assertEpsilonEquals(0, this.transform.getM02());
		assertEpsilonEquals(0, this.transform.getM03());
		assertEpsilonEquals(0, this.transform.getM10());
		assertEpsilonEquals(1, this.transform.getM11());
		assertEpsilonEquals(0, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM13());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
		assertEpsilonEquals(0, this.transform.getM23());
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());
	}

	@DisplayName("makeRotationMatrix(Quaternion)")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void makeRotationMatrixQuaternion(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d q = newAxisAngleYd(45);

		final double cos = Math.cos(Math.toRadians(45));
		final double sin = Math.sin(Math.toRadians(45));
		
		this.transform.makeRotationMatrix(q);
		
		assertEpsilonEquals(cos, this.transform.getM00());
		assertEpsilonEquals(0, this.transform.getM01());
		assertEpsilonEquals(sin, this.transform.getM02());
		assertEpsilonEquals(0, this.transform.getM03());

		assertEpsilonEquals(0, this.transform.getM10());
		assertEpsilonEquals(1, this.transform.getM11());
		assertEpsilonEquals(0, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM13());

		assertEpsilonEquals(-sin, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(cos, this.transform.getM22());
		assertEpsilonEquals(0, this.transform.getM23());
		
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());
	}

	@DisplayName("makeRotationMatrix(double,double,double,double)")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void makeRotationMatrixDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d q = newAxisAngleYd(45);

		final double cos = Math.cos(Math.toRadians(45));
		final double sin = Math.sin(Math.toRadians(45));
		
		this.transform.makeRotationMatrix(q.getX(), q.getY(), q.getZ(), q.getW());
		
		assertEpsilonEquals(cos, this.transform.getM00());
		assertEpsilonEquals(0, this.transform.getM01());
		assertEpsilonEquals(sin, this.transform.getM02());
		assertEpsilonEquals(0, this.transform.getM03());

		assertEpsilonEquals(0, this.transform.getM10());
		assertEpsilonEquals(1, this.transform.getM11());
		assertEpsilonEquals(0, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM13());

		assertEpsilonEquals(-sin, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(cos, this.transform.getM22());
		assertEpsilonEquals(0, this.transform.getM23());
		
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());
	}

	@DisplayName("setRotation(Quaternion)")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void setRotationQuaternion(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		Quaternion4d q = newAxisAngleZd(-80);
		
		final double cos = Math.cos(Math.toRadians(-80));
		final double sin = Math.sin(Math.toRadians(-80));

		this.transform.setRotation(q);

		assertEpsilonEquals(cos, this.transform.getM00());
		assertEpsilonEquals(-sin, this.transform.getM01());
		assertEpsilonEquals(0, this.transform.getM02());
		assertEpsilonEquals(TRANSX, this.transform.getM03());

		assertEpsilonEquals(sin, this.transform.getM10());
		assertEpsilonEquals(cos, this.transform.getM11());
		assertEpsilonEquals(0, this.transform.getM12());
		assertEpsilonEquals(TRANSY, this.transform.getM13());

		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
		assertEpsilonEquals(TRANSZ, this.transform.getM23());
		
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());
	}

	@DisplayName("setRotation(double,double,double,double)")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void setRotationDoubledoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		Quaternion4d q = newAxisAngleZd(-80);
		
		final double cos = Math.cos(Math.toRadians(-80));
		final double sin = Math.sin(Math.toRadians(-80));

		this.transform.setRotation(q.getX(), q.getY(), q.getZ(), q.getW());

		assertEpsilonEquals(cos, this.transform.getM00());
		assertEpsilonEquals(-sin, this.transform.getM01());
		assertEpsilonEquals(0, this.transform.getM02());
		assertEpsilonEquals(TRANSX, this.transform.getM03());

		assertEpsilonEquals(sin, this.transform.getM10());
		assertEpsilonEquals(cos, this.transform.getM11());
		assertEpsilonEquals(0, this.transform.getM12());
		assertEpsilonEquals(TRANSY, this.transform.getM13());

		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
		assertEpsilonEquals(TRANSZ, this.transform.getM23());
		
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());
	}

	@DisplayName("rotate(Quaternion)")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void rotateQuaternion(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		this.transform.makeRotationMatrix(newAxisAngleZd(45));
		
		Quaternion4d q = newAxisAngleYd(-45);

		this.transform.rotate(q);

		assertEpsilonEquals(.5, this.transform.getM00());
		assertEpsilonEquals(-.707106781186, this.transform.getM01());
		assertEpsilonEquals(-.5, this.transform.getM02());
		assertEpsilonEquals(0, this.transform.getM03());
		assertEpsilonEquals(.5, this.transform.getM10());
		assertEpsilonEquals(.707106781186, this.transform.getM11());
		assertEpsilonEquals(-.5, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM13());
		assertEpsilonEquals(.707106781186, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(.707106781186, this.transform.getM22());
		assertEpsilonEquals(0, this.transform.getM23());
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());
	}

	@DisplayName("transform(Point3D) with translation")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_translationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeTranslationMatrix(123, 456, 0);
		Point3D p = new Point3d(-584, 5647, 0);
		this.transform.transform(p);
		assertEpsilonEquals(-461, p.getX());
		assertEpsilonEquals(6103, p.getY());
	}

	protected static Quaternion4d newAxisAngleXd(double angle) {
		return newAxisAngleXr(Math.toRadians(angle));
	}

	protected static Quaternion4d newAxisAngleXr(double angle) {
		final Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(1,  0,  0, angle);
		return q;
	}

	protected static Quaternion4d newAxisAngleYd(double angle) {
		return newAxisAngleYr(Math.toRadians(angle));
	}

	protected static Quaternion4d newAxisAngleYr(double angle) {
		final Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(0,  1,  0, angle);
		return q;
	}

	protected static Quaternion4d newAxisAngleZd(double angle) {
		return newAxisAngleZr(Math.toRadians(angle));
	}

	protected static Quaternion4d newAxisAngleZr(double angle) {
		final Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(0,  0,  1, angle);
		return q;
	}

	@DisplayName("transform(Point3D) with 90 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_90XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(90));
		Point3D p = new Point3d(0, 1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(1, p.getZ());
	}

	@DisplayName("transform(Point3D) with -90 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_m90XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(-90));
		Point3D p = new Point3d(0, 1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(-1, p.getZ());
	}

	@DisplayName("transform(Point3D) with 45 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_45XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(45));
		Point3D p = new Point3d(0, 1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0.7071067811865475, p.getY());
		assertEpsilonEquals(0.7071067811865475, p.getZ());
	}

	@DisplayName("transform(Point3D) with -45 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_m45XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(-45));
		Point3D p = new Point3d(0, 1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0.7071067811865475, p.getY());
		assertEpsilonEquals(-0.7071067811865475, p.getZ());
	}

	@DisplayName("transform(Point3D) with 90 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_90YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(90));
		Point3D p = new Point3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(-1, p.getZ());
	}

	@DisplayName("transform(Point3D) with -90 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_m90YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(-90));
		Point3D p = new Point3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(1, p.getZ());
	}

	@DisplayName("transform(Point3D) with 45 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_45YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(45));
		Point3D p = new Point3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0.7071067811865475, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(-0.7071067811865475, p.getZ());
	}

	@DisplayName("transform(Point3D) with -45 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_m45YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(-45));
		Point3D p = new Point3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0.7071067811865475, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0.7071067811865475, p.getZ());
	}

	@DisplayName("transform(Point3D) with 90 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_90ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(90));
		Point3D p = new Point3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(1, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	@DisplayName("transform(Point3D) with -90 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_m90ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(-90));
		Point3D p = new Point3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(-1, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	@DisplayName("transform(Point3D) with 45 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_45ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(45));
		Point3D p = new Point3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0.7071067811865475, p.getX());
		assertEpsilonEquals(0.7071067811865475, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	@DisplayName("transform(Point3D) with -45 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_m45ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(-45));
		Point3D p = new Point3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0.7071067811865475, p.getX());
		assertEpsilonEquals(-0.7071067811865475, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	@DisplayName("transform(Point3D,Point3D) with 90 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3DPoint3D_90XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(90));
		Point3D p = new Point3d(0, 1, 0);
		Point3D r = new Point3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(0, r.getY());
		assertEpsilonEquals(1, r.getZ());
	}

	@DisplayName("transform(Point3D,Point3D) with -90 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3DPoint3D_m90XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(-90));
		Point3D p = new Point3d(0, 1, 0);
		Point3D r = new Point3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(0, r.getY());
		assertEpsilonEquals(-1, r.getZ());
	}

	@DisplayName("transform(Point3D,Point3D) with 45 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3DPoint3D_45XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(45));
		Point3D p = new Point3d(0, 1, 0);
		Point3D r = new Point3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(0.7071067811865475, r.getY());
		assertEpsilonEquals(0.7071067811865475, r.getZ());
	}

	@DisplayName("transform(Point3D,Point3D) with -45 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3DPoint3D_m45XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(-45));
		Point3D p = new Point3d(0, 1, 0);
		Point3D r = new Point3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(0.7071067811865475, r.getY());
		assertEpsilonEquals(-0.7071067811865475, r.getZ());
	}

	@DisplayName("transform(Point3D,Point3D) with 90 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3DPoint3D_90YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(90));
		Point3D p = new Point3d(1, 0, 0);
		Point3D r = new Point3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(0, r.getY());
		assertEpsilonEquals(-1, r.getZ());
	}

	@DisplayName("transform(Point3D,Point3D) with -90 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3DPoint3D_m90YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(-90));
		Point3D p = new Point3d(1, 0, 0);
		Point3D r = new Point3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(0, r.getY());
		assertEpsilonEquals(1, r.getZ());
	}

	@DisplayName("transform(Point3D,Point3D) with 45 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3DPoint3D_45YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(45));
		Point3D p = new Point3d(1, 0, 0);
		Point3D r = new Point3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0.7071067811865475, r.getX());
		assertEpsilonEquals(0, r.getY());
		assertEpsilonEquals(-0.7071067811865475, r.getZ());
	}

	@DisplayName("transform(Point3D,Point3D) with -45 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3DPoint3D_m45YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(-45));
		Point3D p = new Point3d(1, 0, 0);
		Point3D r = new Point3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0.7071067811865475, r.getX());
		assertEpsilonEquals(0, r.getY());
		assertEpsilonEquals(0.7071067811865475, r.getZ());
	}

	@DisplayName("transform(Point3D,Point3D) with 90 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3DPoint3D_90ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(90));
		Point3D p = new Point3d(1, 0, 0);
		Point3D r = new Point3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(1, r.getY());
		assertEpsilonEquals(0, r.getZ());
	}

	@DisplayName("transform(Point3D,Point3D) with -90 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3DPoint3D_m90ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(-90));
		Point3D p = new Point3d(1, 0, 0);
		Point3D r = new Point3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(-1, r.getY());
		assertEpsilonEquals(0, r.getZ());
	}

	@DisplayName("transform(Point3D,Point3D) with 45 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3DPoint3D_45ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(45));
		Point3D p = new Point3d(1, 0, 0);
		Point3D r = new Point3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0.7071067811865475, r.getX());
		assertEpsilonEquals(0.7071067811865475, r.getY());
		assertEpsilonEquals(0, r.getZ());
	}

	@DisplayName("transform(Point3D,Point3D) with -45 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3DPoint3D_m45ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(-45));
		Point3D p = new Point3d(1, 0, 0);
		Point3D r = new Point3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0.7071067811865475, r.getX());
		assertEpsilonEquals(-0.7071067811865475, r.getY());
		assertEpsilonEquals(0, r.getZ());
	}

	@DisplayName("transform(Point3D,Point3D) with translation")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3DPoint3D_translationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeTranslationMatrix(123, 456, 0);
		Point3D result = new Point3d();
		Point3D p = new Point3d(-584, 5647, 0);
		this.transform.transform(p, result);
		assertEpsilonEquals(-584, p.getX());
		assertEpsilonEquals(5647, p.getY());
		assertEpsilonEquals(-461, result.getX());
		assertEpsilonEquals(6103, result.getY());
	}

	@DisplayName("transform(Point3D) with 2 translations")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_2translations(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		final Transform3D translation1 = new Transform3D();
		translation1.makeTranslationMatrix(123, 456, 57);
		final Transform3D translation2 = new Transform3D();
		translation2.makeTranslationMatrix(496, -47456, -63);
		this.transform.mul(translation1, translation2);
		assertEpsilonEquals(1, this.transform.getM00());
		assertEpsilonEquals(0, this.transform.getM01());
		assertEpsilonEquals(0, this.transform.getM02());
		assertEpsilonEquals(619, this.transform.getM03());
		assertEpsilonEquals(0, this.transform.getM10());
		assertEpsilonEquals(1, this.transform.getM11());
		assertEpsilonEquals(0, this.transform.getM12());
		assertEpsilonEquals(-47000, this.transform.getM13());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
		assertEpsilonEquals(-6, this.transform.getM23());
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());

		Point3D p = new Point3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(620, p.getX());
		assertEpsilonEquals(-47000, p.getY());
		assertEpsilonEquals(-6, p.getZ());
	}

	@DisplayName("transform(Point3D) with 2 rotations")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformPoint3D_2rotations(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		final Transform3D rotation1 = new Transform3D();
		rotation1.makeRotationMatrix(newAxisAngleZd(45));
		
		final Transform3D rotation2 = new Transform3D();
		rotation2.makeRotationMatrix(newAxisAngleYd(-45));

		this.transform.mul(rotation1, rotation2);

		Point3D p = new Point3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(.5, p.getX());
		assertEpsilonEquals(.5, p.getY());
		assertEpsilonEquals(.707106781186, p.getZ());
	}

	@DisplayName("getRotation with 2 rotations")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getRotation_2rotations(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		final Transform3D rotation1 = new Transform3D();
		rotation1.makeRotationMatrix(newAxisAngleZd(45));
		
		final Transform3D rotation2 = new Transform3D();
		rotation2.makeRotationMatrix(newAxisAngleYd(-45));

		this.transform.mul(rotation1, rotation2);

		assertEpsilonEquals(.5, this.transform.getM00());
		assertEpsilonEquals(-.707106781186, this.transform.getM01());
		assertEpsilonEquals(-.5, this.transform.getM02());
		assertEpsilonEquals(0, this.transform.getM03());
		assertEpsilonEquals(.5, this.transform.getM10());
		assertEpsilonEquals(.707106781186, this.transform.getM11());
		assertEpsilonEquals(-.5, this.transform.getM12());
		assertEpsilonEquals(0, this.transform.getM13());
		assertEpsilonEquals(.707106781186, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(.707106781186, this.transform.getM22());
		assertEpsilonEquals(0, this.transform.getM23());
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());

		Quaternion4d q = this.transform.getRotation(FACTORY);
		assertEpsilonEquals(-.1464, q.getX());
		assertEpsilonEquals(.3536, q.getY());
		assertEpsilonEquals(-0.3536, q.getZ());
		assertEpsilonEquals(-.8536, q.getW());
	}

	@DisplayName("getRotation with 90 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getRotation_90z(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d original = newAxisAngleZd(90);
		this.transform.makeRotationMatrix(original);

		Quaternion4d q = this.transform.getRotation(FACTORY);
		assertEpsilonEqualsAbs(original, q);
	}

	@DisplayName("getRotation with -90 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getRotation_m90z(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d original = newAxisAngleZd(-90);
		this.transform.makeRotationMatrix(original);

		Quaternion4d q = this.transform.getRotation(FACTORY);
		assertEpsilonEqualsAbs(original, q);
	}

	@DisplayName("getRotation with 45 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getRotation_45z(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d original = newAxisAngleZd(45);
		this.transform.makeRotationMatrix(original);

		Quaternion4d q = this.transform.getRotation(FACTORY);
		assertEpsilonEqualsAbs(original, q);
	}

	@DisplayName("getRotation with -45 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getRotation_m45z(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d original = newAxisAngleZd(-45);
		this.transform.makeRotationMatrix(original);

		Quaternion4d q = this.transform.getRotation(FACTORY);
		assertEpsilonEqualsAbs(original, q);
	}

	@DisplayName("getRotation with 90 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getRotation_90y(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d original = newAxisAngleYd(90);
		this.transform.makeRotationMatrix(original);

		Quaternion4d q = this.transform.getRotation(FACTORY);
		assertEpsilonEqualsAbs(original, q);
	}

	@DisplayName("getRotation with -90 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getRotation_m90y(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d original = newAxisAngleYd(-90);
		this.transform.makeRotationMatrix(original);

		Quaternion4d q = this.transform.getRotation(FACTORY);
		assertEpsilonEqualsAbs(original, q);
	}

	@DisplayName("getRotation with 45 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getRotation_45y(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d original = newAxisAngleYd(45);
		this.transform.makeRotationMatrix(original);

		Quaternion4d q = this.transform.getRotation(FACTORY);
		assertEpsilonEqualsAbs(original, q);
	}

	@DisplayName("getRotation with -45 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getRotation_m45y(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d original = newAxisAngleYd(-45);
		this.transform.makeRotationMatrix(original);

		Quaternion4d q = this.transform.getRotation(FACTORY);
		assertEpsilonEqualsAbs(original, q);
	}

	@DisplayName("getRotation with 90 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getRotation_90x(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d original = newAxisAngleXd(90);
		this.transform.makeRotationMatrix(original);

		Quaternion4d q = this.transform.getRotation(FACTORY);
		assertEpsilonEqualsAbs(original, q);
	}

	@DisplayName("getRotation with -90 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getRotation_m90x(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d original = newAxisAngleXd(-90);
		this.transform.makeRotationMatrix(original);

		Quaternion4d q = this.transform.getRotation(FACTORY);
		assertEpsilonEqualsAbs(original, q);
	}

	@DisplayName("getRotation with 45 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getRotation_45x(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d original = newAxisAngleXd(45);
		this.transform.makeRotationMatrix(original);

		Quaternion4d q = this.transform.getRotation(FACTORY);
		assertEpsilonEqualsAbs(original, q);
	}

	@DisplayName("getRotation with -45 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void getRotation_m45x(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d original = newAxisAngleXd(-45);
		this.transform.makeRotationMatrix(original);

		Quaternion4d q = this.transform.getRotation(FACTORY);
		assertEpsilonEqualsAbs(original, q);
	}

	@DisplayName("transform(Vector3D) with 2 rotations")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_2rotations(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		final Transform3D rotation1 = new Transform3D();
		rotation1.makeRotationMatrix(newAxisAngleZd(45));
		
		final Transform3D rotation2 = new Transform3D();
		rotation2.makeRotationMatrix(newAxisAngleYd(-45));

		this.transform.mul(rotation1, rotation2);

		Vector3D p = new Vector3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(.5, p.getX());
		assertEpsilonEquals(.5, p.getY());
		assertEpsilonEquals(.707106781186, p.getZ());
	}

	@DisplayName("transform(Vector3D) with 2 translations")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_2translations(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		final Transform3D translation1 = new Transform3D();
		translation1.makeTranslationMatrix(123, 456, 57);
		final Transform3D translation2 = new Transform3D();
		translation2.makeTranslationMatrix(496, -47456, -63);
		this.transform.mul(translation1, translation2);
		assertEpsilonEquals(1, this.transform.getM00());
		assertEpsilonEquals(0, this.transform.getM01());
		assertEpsilonEquals(0, this.transform.getM02());
		assertEpsilonEquals(619, this.transform.getM03());
		assertEpsilonEquals(0, this.transform.getM10());
		assertEpsilonEquals(1, this.transform.getM11());
		assertEpsilonEquals(0, this.transform.getM12());
		assertEpsilonEquals(-47000, this.transform.getM13());
		assertEpsilonEquals(0, this.transform.getM20());
		assertEpsilonEquals(0, this.transform.getM21());
		assertEpsilonEquals(1, this.transform.getM22());
		assertEpsilonEquals(-6, this.transform.getM23());
		assertEpsilonEquals(0, this.transform.getM30());
		assertEpsilonEquals(0, this.transform.getM31());
		assertEpsilonEquals(0, this.transform.getM32());
		assertEpsilonEquals(1, this.transform.getM33());

		Vector3D p = new Vector3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(1, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	@DisplayName("transform(Vector3D) with 90 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_90XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(90));
		Vector3D p = new Vector3d(0, 1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(1, p.getZ());
	}

	@DisplayName("transform(Vector3D) with -90 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_m90XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(-90));
		Vector3D p = new Vector3d(0, 1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(-1, p.getZ());
	}

	@DisplayName("transform(Vector3D) with 45 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_45XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(45));
		Vector3D p = new Vector3d(0, 1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0.7071067811865475, p.getY());
		assertEpsilonEquals(0.7071067811865475, p.getZ());
	}

	@DisplayName("transform(Vector3D) with -45 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_m45XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(-45));
		Vector3D p = new Vector3d(0, 1, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0.7071067811865475, p.getY());
		assertEpsilonEquals(-0.7071067811865475, p.getZ());
	}

	@DisplayName("transform(Vector3D) with 90 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_90YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(90));
		Vector3D p = new Vector3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(-1, p.getZ());
	}

	@DisplayName("transform(Vector3D) with -90 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_m90YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(-90));
		Vector3D p = new Vector3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(1, p.getZ());
	}

	@DisplayName("transform(Vector3D) with 45 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_45YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(45));
		Vector3D p = new Vector3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0.7071067811865475, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(-0.7071067811865475, p.getZ());
	}

	@DisplayName("transform(Vector3D) with -45 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_m45YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(-45));
		Vector3D p = new Vector3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0.7071067811865475, p.getX());
		assertEpsilonEquals(0, p.getY());
		assertEpsilonEquals(0.7071067811865475, p.getZ());
	}

	@DisplayName("transform(Vector3D) with 90 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_90ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(90));
		Vector3D p = new Vector3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(1, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	@DisplayName("transform(Vector3D) with -90 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_m90ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(-90));
		Vector3D p = new Vector3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0, p.getX());
		assertEpsilonEquals(-1, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	@DisplayName("transform(Vector3D) with 45 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_45ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(45));
		Vector3D p = new Vector3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0.7071067811865475, p.getX());
		assertEpsilonEquals(0.7071067811865475, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	@DisplayName("transform(Vector3D) with -45 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_m45ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(-45));
		Vector3D p = new Vector3d(1, 0, 0);
		this.transform.transform(p);
		assertEpsilonEquals(0.7071067811865475, p.getX());
		assertEpsilonEquals(-0.7071067811865475, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	@DisplayName("transform(Vector3D) with translation")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3D_translationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeTranslationMatrix(123, 456, 0);
		Vector3D p = new Vector3d(-584, 5647, 0);
		this.transform.transform(p);
		assertEpsilonEquals(-584, p.getX());
		assertEpsilonEquals(5647, p.getY());
	}

	@DisplayName("transform(Vector3D,Vector3D) with 90 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3DVector3D_90XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(90));
		Vector3D p = new Vector3d(0, 1, 0);
		Vector3D r = new Vector3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(0, r.getY());
		assertEpsilonEquals(1, r.getZ());
	}

	@DisplayName("transform(Vector3D,Vector3D) with -90 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3DVector3D_m90XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(-90));
		Vector3D p = new Vector3d(0, 1, 0);
		Vector3D r = new Vector3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(0, r.getY());
		assertEpsilonEquals(-1, r.getZ());
	}

	@DisplayName("transform(Vector3D,Vector3D) with 45 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3DVector3D_45XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(45));
		Vector3D p = new Vector3d(0, 1, 0);
		Vector3D r = new Vector3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(0.7071067811865475, r.getY());
		assertEpsilonEquals(0.7071067811865475, r.getZ());
	}

	@DisplayName("transform(Vector3D,Vector3D) with -45 rotation X")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3DVector3D_m45XRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleXd(-45));
		Vector3D p = new Vector3d(0, 1, 0);
		Vector3D r = new Vector3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(0.7071067811865475, r.getY());
		assertEpsilonEquals(-0.7071067811865475, r.getZ());
	}

	@DisplayName("transform(Vector3D,Vector3D) with 90 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3DVector3D_90YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(90));
		Vector3D p = new Vector3d(1, 0, 0);
		Vector3D r = new Vector3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(0, r.getY());
		assertEpsilonEquals(-1, r.getZ());
	}

	@DisplayName("transform(Vector3D,Vector3D) with -90 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3DVector3D_m90YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(-90));
		Vector3D p = new Vector3d(1, 0, 0);
		Vector3D r = new Vector3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(0, r.getY());
		assertEpsilonEquals(1, r.getZ());
	}

	@DisplayName("transform(Vector3D,Vector3D) with 45 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3DVector3D_45YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(45));
		Vector3D p = new Vector3d(1, 0, 0);
		Vector3D r = new Vector3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0.7071067811865475, r.getX());
		assertEpsilonEquals(0, r.getY());
		assertEpsilonEquals(-0.7071067811865475, r.getZ());
	}

	@DisplayName("transform(Vector3D,Vector3D) with -45 rotation Y")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3DVector3D_m45YRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleYd(-45));
		Vector3D p = new Vector3d(1, 0, 0);
		Vector3D r = new Vector3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0.7071067811865475, r.getX());
		assertEpsilonEquals(0, r.getY());
		assertEpsilonEquals(0.7071067811865475, r.getZ());
	}

	@DisplayName("transform(Vector3D,Vector3D) with 90 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3DVector3D_90ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(90));
		Vector3D p = new Vector3d(1, 0, 0);
		Vector3D r = new Vector3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(1, r.getY());
		assertEpsilonEquals(0, r.getZ());
	}

	@DisplayName("transform(Vector3D,Vector3D) with -90 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3DVector3D_m90ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(-90));
		Vector3D p = new Vector3d(1, 0, 0);
		Vector3D r = new Vector3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0, r.getX());
		assertEpsilonEquals(-1, r.getY());
		assertEpsilonEquals(0, r.getZ());
	}

	@DisplayName("transform(Vector3D,Vector3D) with 45 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3DVector3D_45ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(45));
		Vector3D p = new Vector3d(1, 0, 0);
		Vector3D r = new Vector3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0.7071067811865475, r.getX());
		assertEpsilonEquals(0.7071067811865475, r.getY());
		assertEpsilonEquals(0, r.getZ());
	}

	@DisplayName("transform(Vector3D,Vector3D) with -45 rotation Z")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3DVector3D_m45ZRotationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeRotationMatrix(newAxisAngleZd(-45));
		Vector3D p = new Vector3d(1, 0, 0);
		Vector3D r = new Vector3d(0, 0, 0);
		this.transform.transform(p, r);
		assertEpsilonEquals(0.7071067811865475, r.getX());
		assertEpsilonEquals(-0.7071067811865475, r.getY());
		assertEpsilonEquals(0, r.getZ());
	}

	@DisplayName("transform(Vector3D,Vector3D) with translation")
	@ParameterizedTest
	@EnumSource(CoordinateSystem3D.class)
	public void transformVector3DVector3D_translationOnly(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.transform.makeTranslationMatrix(123, 456, 0);
		Vector3D result = new Vector3d();
		Vector3D p = new Vector3d(-584, 5647, 0);
		this.transform.transform(p, result);
		assertEpsilonEquals(-584, p.getX());
		assertEpsilonEquals(5647, p.getY());
		assertEpsilonEquals(-584, result.getX());
		assertEpsilonEquals(5647, result.getY());
	}

}
