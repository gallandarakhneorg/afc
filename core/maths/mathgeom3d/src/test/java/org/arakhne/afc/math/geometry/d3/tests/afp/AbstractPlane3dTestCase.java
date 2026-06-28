/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d3.tests.afp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Random;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.PlaneClassification;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.PointVector3DReceiver;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.Plane3afp;
import org.arakhne.afc.math.geometry.d3.afp.PlaneXY3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.d.Plane3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneXY3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneXZ3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneYZ3d;
import org.arakhne.afc.math.geometry.d3.d.Point3d;
import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;
import org.arakhne.afc.math.geometry.d3.d.Segment3d;
import org.arakhne.afc.math.geometry.d3.d.Sphere3d;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;

@SuppressWarnings("all")
public abstract class AbstractPlane3dTestCase<T extends Plane3afp<T, ?, ?, ?, ?>, B extends AlignedBox3afp<?, ?, ?, ?, ?, B>> extends AbstractPlane3DTestCase<T, B> {

	private static final double A = 2.;

	private static final double NORMAL_X = .8164965809277261;

	private static final double B = 1.;

	private static final double NORMAL_Y = .4082482904638631;

	private static final double C = 1.;

	private static final double NORMAL_Z = .4082482904638631;

	private static final double D = 4;

	private T plane;

	protected final T getP() {
		return this.plane;
	}

	protected abstract T createTestPlane(double a, double b, double c, double d);

	@BeforeEach
	public final void setUp() {
		super.setUp();
		this.plane = createTestPlane(A, B, C, D);
	}

	@AfterEach
	public final void tearDown() throws Exception {
		this.plane = null;
	}

	@DisplayName("toGeogebra")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void toGeogebra(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals("0.8164965809277261*x+0.4082482904638631*y+0.4082482904638631*z+4.0=0.0", getP().toGeogebra());
	}

	@DisplayName("equals(Plane3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void equalsPlane3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertTrue(getP().equals(createPlane(A, B, C, D)));
		assertTrue(getP().equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		assertFalse(getP().equals(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		assertFalse(getP().equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		assertFalse(getP().equals(createPlane(1., 2., 0., 5.)));

		//
		getP().negate();

		assertFalse(getP().equals(createPlane(A, B, C, D)));
		assertFalse(getP().equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		assertTrue(getP().equals(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		assertFalse(getP().equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		assertFalse(getP().equals(createPlane(1., 2., 0., 5.)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	@DisplayName("absolute")
	public final void absolute(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().absolute();
		assertEpsilonEquals(0.8164965809277261, getP().getEquationComponentA());
		assertEpsilonEquals(0.4082482904638631, getP().getEquationComponentB());
		assertEpsilonEquals(0.4082482904638631, getP().getEquationComponentC());
		assertEpsilonEquals(4, getP().getEquationComponentD());
		//
		var p = createPlane(-1, 2, -3, 4);
		// length = 3.741657387
		// x = -0.267261242
		// y = 0.534522484
		// z = -0.801783726
		p.absolute();
		assertEpsilonEquals(.267261242, p.getEquationComponentA());
		assertEpsilonEquals(.534522484, p.getEquationComponentB());
		assertEpsilonEquals(.801783726, p.getEquationComponentC());
		assertEpsilonEquals(4, p.getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("clear")
	@Override
	public final void clear(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().clear();
		assertEpsilonEquals(1., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getEquationComponentA")
	@Override
	public final void getEquationComponentA(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getEquationComponentB")
	@Override
	public final void getEquationComponentB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getEquationComponentC")
	@Override
	public final void getEquationComponentC(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getEquationComponentD")
	@Override
	public final void getEquationComponentD(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(D, getP().getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getNormal")
	@Override
	public final void getNormal(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(createVector(NORMAL_X, NORMAL_Y, NORMAL_Z), getP().getNormal());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getNormalX")
	@Override
	public final void getNormalX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(NORMAL_X, getP().getNormalX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getNormalY")
	@Override
	public final void getNormalY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(NORMAL_Y, getP().getNormalY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getNormalZ")
	@Override
	public final void getNormalZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(NORMAL_Z, getP().getNormalZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getPivot")
	@Override
	public final void getPivot(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523),
				getP().getPivot());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("translate(double)")
	@Override
	public final void translateDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().translate(7);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(11, getP().getEquationComponentD());
		//
		getP().translate(-25);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(-14, getP().getEquationComponentD());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("translate(double,double,double)")
	@Override
	public final void translateDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().translate(1, 2, 3);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(1.1422619667529594, getP().getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("translate(Vector3D)")
	@Override
	public final void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().translate(createVector(1, 2, 3));
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(1.1422619667529594, getP().getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("negate")
	@Override
	public final void negate(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().negate();
		assertEpsilonEquals(-NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(-NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(-NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(-D, getP().getEquationComponentD());
		//
		getP().negate();
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(D, getP().getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("normalize")
	@Override
	public final void normalize(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(this.plane, getP().normalize());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getProjection(x,y,z)")
	@Override
	public final void getProjectionDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523),
				getP().getProjection(0, 0, 0));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814),
				getP().getProjection(-5, -7, 0));
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852),
				getP().getProjection(-1, 8, -2));

		//
		getP().negate();
		
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523),
				getP().getProjection(0, 0, 0));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814),
				getP().getProjection(-5, -7, 0));
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852),
				getP().getProjection(-1, 8, -2));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getProjection(Point3D)")
	@Override
	public final void getProjectionPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523),
				getP().getProjection(createPoint(0, 0, 0)));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814),
				getP().getProjection(createPoint(-5, -7, 0)));
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852),
				getP().getProjection(createPoint(-1, 8, -2)));

		//
		getP().negate();
		
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523),
				getP().getProjection(createPoint(0, 0, 0)));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814),
				getP().getProjection(createPoint(-5, -7, 0)));
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852),
				getP().getProjection(createPoint(-1, 8, -2)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("setPivot(double,double,double)")
	@Override
	public final void setPivotDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().setPivot(0, 0, 0);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());
		//
		getP().setPivot(1, 2, 3);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(-2.8577380332470415, getP().getEquationComponentD());
		//
		getP().setPivot(-5, 4, -1);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(2.857738033247041, getP().getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("setPivot(Point3D)")
	@Override
	public final void setPivotPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().setPivot(createPoint(0, 0, 0));
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());
		//
		getP().setPivot(createPoint(1, 2, 3));
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(-2.8577380332470415, getP().getEquationComponentD());
		//
		getP().setPivot(createPoint(-5, 4, -1));
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(2.857738033247041, getP().getEquationComponentD());
	}

	@DisplayName("getDistanceTo(Plane3D) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceToPlane3D_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getP().getDistanceTo(createPlane(A, B, C, D)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		assertEpsilonEquals(18., getP().getDistanceTo(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 18)));
		assertEpsilonEquals(18., getP().getDistanceTo(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D + 18)));
		//
		assertEpsilonEquals(0., getP().getDistanceTo(createPlane(NORMAL_X - 1, NORMAL_Y, NORMAL_Z, D)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlane(NORMAL_X, NORMAL_Y - 1, NORMAL_Z, D)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z - 1, D)));
	}

	@DisplayName("getDistanceTo(Plane3D) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceToPlane3D_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(4.5, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(1.25, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-4.5, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-1.25, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(4.5, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(1.25, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-4.5, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-1.25, false)));
		//
		getP().negate();
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(4.5, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(1.25, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-4.5, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-1.25, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(4.5, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(1.25, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-4.5, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-1.25, false)));
	}

	@DisplayName("getDistanceTo(Plane3D) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceToPlane3D_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, false)));
		//
		getP().negate();
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, false)));
	}

	@DisplayName("getDistanceTo(Plane3D) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceToPlane3D_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, false)));
		//
		getP().negate();
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, true)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, false)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, false)));
	}

	@DisplayName("getIntersection(a,b,c,d) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getIntersectionDoubleDoubleDoubleDouble_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp<?, ?, ?, ?, ?, ?, ?> s;

		s = getP().getIntersection(0., 1., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(0., 1., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(0., -1., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(0., -1., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(0., 1., 0., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(0., -1., 0., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		//
		getP().negate();

		s = getP().getIntersection(0., 1., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(0., 1., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(0., -1., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(0., -1., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(0., 1., 0., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(0., -1., 0., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
	}

	@DisplayName("getIntersection(a, b, c, d) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getIntersectionDoubleDoubleDoubleDouble_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;

		s = getP().getIntersection(0., 0., 1., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		}

		s = getP().getIntersection(0., 0., 1., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		}

		s = getP().getIntersection(0., 0., -1., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		}

		s = getP().getIntersection(0., 0., -1., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		}

		s = getP().getIntersection(0., 0., 1., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		}

		s = getP().getIntersection(0., 0., -1., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		}

		//
		getP().negate();

		s = getP().getIntersection(0., 0., 1., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		}

		s = getP().getIntersection(0., 0., 1., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		}

		s = getP().getIntersection(0., 0., -1., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		}

		s = getP().getIntersection(0., 0., -1., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		}

		s = getP().getIntersection(0., 0., 1., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		}

		s = getP().getIntersection(0., 0., -1., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		}
	}

	@DisplayName("getIntersection(a, b, c, d) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getIntersectionDoubleDoubleDoubleDouble_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;

		s = getP().getIntersection(1., 0., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(1., 0., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(-1., 0., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(-1., 0., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(1., 0., 0., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(-1., 0., 0., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		//
		getP().negate();

		s = getP().getIntersection(1., 0., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(1., 0., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(-1., 0., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(-1., 0., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(1., 0., 0., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(-1., 0., 0., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
	}

	@DisplayName("getIntersection(a, b, c, d) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getIntersectionDoubleDoubleDoubleDouble_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;

		s = getP().getIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		assertNull(s);

		s = getP().getIntersection(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D);
		assertNull(s);
		
		s = getP().getIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1);
		assertNull(s);

		var vx = 1.;
		var vy = 2.;
		var vz = 0.;
		var vl = Math.sqrt(vx * vx + vy * vy + vz * vz);
		s = getP().getIntersection(vx / vl, vy / vl, vz / vl, 5.);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.60193386084, -4.28920301333, -0.304888236119), s.getP1());
		assertEpsilonColinear(createVector(0.53452248382, -0.26726124191, -0.80178372574), s.getDirection());

		//
		getP().negate();

		s = getP().getIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		assertNull(s);

		s = getP().getIntersection(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D);
		assertNull(s);
		
		s = getP().getIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1);
		assertNull(s);

		s = getP().getIntersection(vx / vl, vy / vl, vz / vl, 5.);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.60193386084, -4.28920301333, -0.304888236119), s.getP1());
		assertEpsilonColinear(createVector(-0.53452248382, 0.26726124191, 0.80178372574), s.getDirection());
	}

	@DisplayName("getIntersection(Plane3D) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getIntersectionPlane3D_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;

		s = getP().getIntersection(createPlaneXZ(4.5, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(createPlaneXZ(-4.5, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(createPlaneXZ(-4.5, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(createPlaneXZ(4.5, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(createPlaneXZ(1.25, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(createPlaneXZ(1.25, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		//
		getP().negate();

		s = getP().getIntersection(createPlaneXZ(4.5, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(createPlaneXZ(-4.5, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(createPlaneXZ(-4.5, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(createPlaneXZ(4.5, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(createPlaneXZ(1.25, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = getP().getIntersection(createPlaneXZ(1.25, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
	}

	@DisplayName("getIntersection(Plane3D) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getIntersectionPlane3D_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;

		s = getP().getIntersection(createPlaneXY(4.5, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		}

		s = getP().getIntersection(createPlaneXY(-4.5, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		}

		s = getP().getIntersection(createPlaneXY(-4.5, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		}

		s = getP().getIntersection(createPlaneXY(4.5, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		}

		s = getP().getIntersection(createPlaneXY(1.25, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		}

		s = getP().getIntersection(createPlaneXY(1.25, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		}

		//
		getP().negate();

		s = getP().getIntersection(createPlaneXY(4.5, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		}

		s = getP().getIntersection(createPlaneXY(-4.5, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		}

		s = getP().getIntersection(createPlaneXY(-4.5, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		}

		s = getP().getIntersection(createPlaneXY(4.5, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		}

		s = getP().getIntersection(createPlaneXY(1.25, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		}

		s = getP().getIntersection(createPlaneXY(1.25, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		}
	}

	@DisplayName("getIntersection(Plane3D) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getIntersectionPlane3D_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;

		s = getP().getIntersection(createPlaneYZ(4.5, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(createPlaneYZ(-4.5, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(createPlaneYZ(-4.5, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(createPlaneYZ(4.5, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(createPlaneYZ(1.25, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(createPlaneYZ(1.25, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		//
		getP().negate();

		s = getP().getIntersection(createPlaneYZ(4.5, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(createPlaneYZ(-4.5, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(createPlaneYZ(-4.5, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(createPlaneYZ(4.5, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(createPlaneYZ(1.25, true));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = getP().getIntersection(createPlaneYZ(1.25, false));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
	}

	@DisplayName("getIntersection(Plane3D) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getIntersectionPlane3D_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;

		s = getP().getIntersection(createPlane(A, B, C, D));
		assertNull(s);

		s = getP().getIntersection(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		assertNull(s);

		s = getP().getIntersection(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D));
		assertNull(s);
		
		s = getP().getIntersection(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1));
		assertNull(s);

		s = getP().getIntersection(createPlane(1., 2., 0., 5.));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.6019338608427405, -4.2892030133281045, -0.30488823611912585), s.getP1());
		assertEpsilonColinear(createVector(-0.82, 0.41, 1.22), s.getDirection());

		//
		getP().negate();

		s = getP().getIntersection(createPlane(A, B, C, D));
		assertNull(s);

		s = getP().getIntersection(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		assertNull(s);

		s = getP().getIntersection(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D));
		assertNull(s);
		
		s = getP().getIntersection(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1));
		assertNull(s);

		s = getP().getIntersection(createPlane(1., 2., 0., 5.));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.6019338608427405, -4.2892030133281045, -0.30488823611912585), s.getP1());
		assertEpsilonColinear(createVector(-0.82, 0.41, 1.22), s.getDirection());
	}

	@DisplayName("rotate(x, y, z, angle)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void rotateDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		getP().rotate(-1, 1, -1, Math.PI / 3.);

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(2.44444444444, getP().getEquationComponentD());
	}

	@DisplayName("rotate(Vector3D, double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void rotateVector3DDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		getP().rotate(createVector(-1, 1, -1), Math.PI / 3.);

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(2.44444444444, getP().getEquationComponentD());
	}

	@DisplayName("rotate(Vector3D, double, Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void rotateVector3DDoublePoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		// Null Pivot
		var q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		getP().rotate(createVector(-1, 1, -1), Math.PI / 3., null);

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(2.44444444444, getP().getEquationComponentD());
		
		// getPivot()
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		var pv = getP().getPivot().clone();
		getP().rotate(createVector(-1, 1, -1), Math.PI / 3., pv);

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(2.44444444444, getP().getEquationComponentD());
		
		// Rotate around origin (0,0,0)
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		getP().setPivotToDefault();
		q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		getP().rotate(createVector(-1, 1, -1), Math.PI / 3., createPoint(0,0,0));

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(4., getP().getEquationComponentD());
		
		// Rotate around (1,-2,3)
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		getP().rotate(createVector(-1, 1, -1), Math.PI / 3., createPoint(1,-2,3));

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(4.8164965809, getP().getEquationComponentD());
	}

	@DisplayName("rotate(Quaternion)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void rotateQuaternion(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		var q = createQuaternion(0, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);
		
		getP().rotate(q);

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(2.44444444444, getP().getEquationComponentD());
	}

	@DisplayName("rotate(Quaternion,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void rotateQuaternionPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		// Null Pivot
		var q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		getP().rotate(q, null);

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(2.44444444444, getP().getEquationComponentD());
		
		// getPivot()
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		var pv = getP().getPivot().clone();
		getP().rotate(q, pv);

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(2.44444444444, getP().getEquationComponentD());
		
		// Rotate around origin (0,0,0)
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		getP().setPivotToDefault();
		q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		getP().rotate(q, createPoint(0,0,0));

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(4., getP().getEquationComponentD());
		
		// Rotate around (1,-2,3)
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		getP().rotate(q, createPoint(1,-2,3));

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(4.8164965809, getP().getEquationComponentD());
	}

	@DisplayName("set(double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void setDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().set(1, -2, 3, -4);
		// Length = 3.741657387
		// x = 0.267261242
		// y = -0.534522484
		// z = 0.801783726
		assertEpsilonEquals(.267261242, getP().getEquationComponentA());
		assertEpsilonEquals(-.534522484, getP().getEquationComponentB());
		assertEpsilonEquals(.801783726, getP().getEquationComponentC());
		assertEpsilonEquals(-4, getP().getEquationComponentD());

		getP().set(1, 0, 0, -4);
		assertEpsilonEquals(1, getP().getEquationComponentA());
		assertEpsilonEquals(0, getP().getEquationComponentB());
		assertEpsilonEquals(0, getP().getEquationComponentC());
		assertEpsilonEquals(-4, getP().getEquationComponentD());

		getP().set(0, 1, 0, -4);
		assertEpsilonEquals(0, getP().getEquationComponentA());
		assertEpsilonEquals(1, getP().getEquationComponentB());
		assertEpsilonEquals(0, getP().getEquationComponentC());
		assertEpsilonEquals(-4, getP().getEquationComponentD());

		getP().set(0, 0, 1, -4);
		assertEpsilonEquals(0, getP().getEquationComponentA());
		assertEpsilonEquals(0, getP().getEquationComponentB());
		assertEpsilonEquals(1, getP().getEquationComponentC());
		assertEpsilonEquals(-4, getP().getEquationComponentD());
	}

	@DisplayName("set(x1,y1,z1, x2,y2,z2, x3,y3,z3)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void setDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;

		// --- Case 1: Plane y = 0 (reference orientation)
		getP().set(0, 0, 0, 1, 0, 0, 0, 0, 1);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(u, getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 2: Same plane y = 0 with different point order (normal may flip)
		getP().set(0, 0, 0, 0, 0, 1, 1, 0, 0);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(-u, getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 3: Plane x = 0
		getP().set(0, 0, 0, 0, 1, 0, 0, 0, 1);
		assertEpsilonEquals(-u, getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 4: Plane z = 0
		getP().set(0, 0, 0, 1, 0, 0, 0, 1, 0);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-u, getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 5: Translated plane y = 1.25  -> 0*x -1*y +0*z +1.25 = 0
		getP().set(0, 1.25, 0, 1, 1.25, 0, 0, 1.25, 1);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(u, getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(-u * 1.25, getP().getEquationComponentD());

		// --- Case 6: Oblique plane x + y + z - 3 = 0 (normal can be scaled internally)
		getP().set(3, 0, 0, 0, 3, 0, 0, 0, 3);
		// Check that the 3 defining points satisfy the computed equation
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * 3
				+ getP().getEquationComponentB() * 0
				+ getP().getEquationComponentC() * 0
				+ getP().getEquationComponentD());
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * 0
				+ getP().getEquationComponentB() * 3
				+ getP().getEquationComponentC() * 0
				+ getP().getEquationComponentD());
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * 0
				+ getP().getEquationComponentB() * 0
				+ getP().getEquationComponentC() * 3
				+ getP().getEquationComponentD());

		// --- Case 7: Generic non-axis-aligned plane (robustness)
		final Point3D p1 = createPoint(1, 2, 3);
		final Point3D p2 = createPoint(4, -1, 2);
		final Point3D p3 = createPoint(-2, 5, 0);
		getP().set(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ());
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * p1.getX()
				+ getP().getEquationComponentB() * p1.getY()
				+ getP().getEquationComponentC() * p1.getZ()
				+ getP().getEquationComponentD());
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * p2.getX()
				+ getP().getEquationComponentB() * p2.getY()
				+ getP().getEquationComponentC() * p2.getZ()
				+ getP().getEquationComponentD());
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * p3.getX()
				+ getP().getEquationComponentB() * p3.getY()
				+ getP().getEquationComponentC() * p3.getZ()
				+ getP().getEquationComponentD());

		// Point not in plane should not satisfy equation
		final Point3D out = createPoint(10, 10, 10);
		final double evalOut = getP().getEquationComponentA() * out.getX()
				+ getP().getEquationComponentB() * out.getY()
				+ getP().getEquationComponentC() * out.getZ()
				+ getP().getEquationComponentD();
		assertNotEpsilonEquals(0., evalOut);
	}

	@DisplayName("set(Point3D, Point3D, Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void setPoint3DPoint3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;

		// --- Case 1: Plane y = 0 (reference orientation)
		getP().set(createPoint(0, 0, 0), createPoint(1, 0, 0), createPoint(0, 0, 1));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(u, getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 2: Same plane y = 0 with different point order (normal may flip)
		getP().set(createPoint(0, 0, 0), createPoint(0, 0, 1), createPoint(1, 0, 0));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(-u, getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 3: Plane x = 0
		getP().set(createPoint(0, 0, 0), createPoint(0, 1, 0), createPoint(0, 0, 1));
		assertEpsilonEquals(-u, getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 4: Plane z = 0
		getP().set(createPoint(0, 0, 0), createPoint(1, 0, 0), createPoint(0, 1, 0));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-u, getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 5: Translated plane y = 1.25  -> 0*x -1*y +0*z +1.25 = 0
		getP().set(createPoint(0, 1.25, 0), createPoint(1, 1.25, 0), createPoint(0, 1.25, 1));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(u, getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(-u * 1.25, getP().getEquationComponentD());

		// --- Case 6: Oblique plane x + y + z - 3 = 0 (normal can be scaled internally)
		getP().set(createPoint(3, 0, 0), createPoint(0, 3, 0), createPoint(0, 0, 3));
		// Check that the 3 defining points satisfy the computed equation
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * 3
				+ getP().getEquationComponentB() * 0
				+ getP().getEquationComponentC() * 0
				+ getP().getEquationComponentD());
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * 0
				+ getP().getEquationComponentB() * 3
				+ getP().getEquationComponentC() * 0
				+ getP().getEquationComponentD());
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * 0
				+ getP().getEquationComponentB() * 0
				+ getP().getEquationComponentC() * 3
				+ getP().getEquationComponentD());

		// --- Case 7: Generic non-axis-aligned plane (robustness)
		final Point3D p1 = createPoint(1, 2, 3);
		final Point3D p2 = createPoint(4, -1, 2);
		final Point3D p3 = createPoint(-2, 5, 0);
		getP().set(p1, p2, p3);
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * p1.getX()
				+ getP().getEquationComponentB() * p1.getY()
				+ getP().getEquationComponentC() * p1.getZ()
				+ getP().getEquationComponentD());
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * p2.getX()
				+ getP().getEquationComponentB() * p2.getY()
				+ getP().getEquationComponentC() * p2.getZ()
				+ getP().getEquationComponentD());
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * p3.getX()
				+ getP().getEquationComponentB() * p3.getY()
				+ getP().getEquationComponentC() * p3.getZ()
				+ getP().getEquationComponentD());

		// Optional: point not in plane should not satisfy equation
		final Point3D out = createPoint(10, 10, 10);
		final double evalOut = getP().getEquationComponentA() * out.getX()
				+ getP().getEquationComponentB() * out.getY()
				+ getP().getEquationComponentC() * out.getZ()
				+ getP().getEquationComponentD();
		assertNotEpsilonEquals(0., evalOut);
	}

	@DisplayName("set(Plane3D) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void setPlane3D_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().set(createPlaneXZ(-4, true));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(1., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(4., getP().getEquationComponentD());
		//
		getP().set(createPlaneXZ(4, true));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(1., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(-4., getP().getEquationComponentD());
		//
		getP().set(createPlaneXZ(4, false));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(-1., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(4., getP().getEquationComponentD());
		//
		getP().set(createPlaneXZ(-4, false));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(-1., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(-4., getP().getEquationComponentD());
	}

	@DisplayName("set(Plane3D) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setPlane3D_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().set(createPlaneXY(-4, true));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(4, getP().getEquationComponentD());
		//
		getP().set(createPlaneXY(4, true));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-4, getP().getEquationComponentD());
		//
		getP().set(createPlaneXY(4, false));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(4, getP().getEquationComponentD());
		//
		getP().set(createPlaneXY(-4, false));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(-4, getP().getEquationComponentD());
	}

	@DisplayName("set(Plane3D) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setPlane3D_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().set(createPlaneYZ(-4, true));
		assertEpsilonEquals(1., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(4., getP().getEquationComponentD());
		//
		getP().set(createPlaneYZ(4, true));
		assertEpsilonEquals(1., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(-4., getP().getEquationComponentD());
		//
		getP().set(createPlaneYZ(4, false));
		assertEpsilonEquals(-1., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(4., getP().getEquationComponentD());
		//
		getP().set(createPlaneYZ(-4, false));
		assertEpsilonEquals(-1., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(-4., getP().getEquationComponentD());
	}

	@DisplayName("set(Plane3D) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setPlane3D_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().set(createPlane(1, -2, 3, -4));
		// Length = 3.741657387
		// x = 0.267261242
		// y = -0.534522484
		// z = 0.801783726
		assertEpsilonEquals(.267261242, getP().getEquationComponentA());
		assertEpsilonEquals(-.534522484, getP().getEquationComponentB());
		assertEpsilonEquals(.801783726, getP().getEquationComponentC());
		assertEpsilonEquals(-4, getP().getEquationComponentD());
	}

	@DisplayName("set(Point3D,Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void setPoint3DVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;

		// --- Case 1: Plane y = 0 (reference orientation)
		getP().set(createPoint(0, 0, 0), createVector(0, u, 0));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(u, getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 2: Same plane y = 0 with different point order (normal may flip)
		getP().set(createPoint(0, 0, 0), createVector(0, -u, 0));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(-u, getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 3: Plane x = 0
		getP().set(createPoint(0, 0, 0), createVector(-u, 0, 0));
		assertEpsilonEquals(-u, getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 4: Plane z = 0
		getP().set(createPoint(0, 0, 0), createVector(0, 0, -u));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-u, getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 5: Translated plane y = 1.25  -> 0*x -1*y +0*z +1.25 = 0
		getP().set(createPoint(0, 1.25, 0), createVector(0, u, 0));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(u, getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(-u * 1.25, getP().getEquationComponentD());

		// --- Case 6: Oblique plane x + y + z - 3 = 0 (normal can be scaled internally)
		getP().set(createPoint(3, 0, 0), createVector(-u, 0, 0));
		assertEpsilonEquals(-u, getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(u * 3, getP().getEquationComponentD());
	}

	@DisplayName("set(Point3D,Vector3D,Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void setPoint3DVector3DVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;

		// --- Case 1: Plane y = 0 (reference orientation)
		getP().set(createPoint(0, 0, 0), createVector(1, 0, 0), createVector(0, 0, 1));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(u, getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 2: Same plane y = 0 with different point order (normal may flip)
		getP().set(createPoint(0, 0, 0), createVector(0, 0, 1), createVector(1, 0, 0));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(-u, getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 3: Plane x = 0
		getP().set(createPoint(0, 0, 0), createVector(0, 1, 0), createVector(0, 0, 1));
		assertEpsilonEquals(-u, getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 4: Plane z = 0
		getP().set(createPoint(0, 0, 0), createVector(1, 0, 0), createVector(0, 1, 0));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-u, getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());

		// --- Case 5: Translated plane y = 1.25  -> 0*x -1*y +0*z +1.25 = 0
		getP().set(createPoint(0, 1.25, 0), createVector(1, 0, 0), createVector(0, 0, 1));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(u, getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(-u * 1.25, getP().getEquationComponentD());

		// --- Case 6: Oblique plane x + y + z - 3 = 0 (normal can be scaled internally)
		getP().set(createPoint(3, 0, 0), createVector(0, 3, 0), createVector(0, 0, 3));
		assertEpsilonEquals(-u, getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(0., getP().getEquationComponentC());
		assertEpsilonEquals(u * 3, getP().getEquationComponentD());

		// --- Case 7: Generic non-axis-aligned plane (robustness)
		final Point3D p1 = createPoint(1, 2, 3);
		final Point3D p2 = createPoint(4, -1, 2);
		final Point3D p3 = createPoint(-2, 5, 0);
		getP().set(p1, p2.operator_minus(p1), p3.operator_minus(p1));
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * p1.getX()
				+ getP().getEquationComponentB() * p1.getY()
				+ getP().getEquationComponentC() * p1.getZ()
				+ getP().getEquationComponentD());
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * p2.getX()
				+ getP().getEquationComponentB() * p2.getY()
				+ getP().getEquationComponentC() * p2.getZ()
				+ getP().getEquationComponentD());
		assertEpsilonEquals(0.,
				getP().getEquationComponentA() * p3.getX()
				+ getP().getEquationComponentB() * p3.getY()
				+ getP().getEquationComponentC() * p3.getZ()
				+ getP().getEquationComponentD());

		// Optional: point not in plane should not satisfy equation
		final Point3D out = createPoint(10, 10, 10);
		final double evalOut = getP().getEquationComponentA() * out.getX()
				+ getP().getEquationComponentB() * out.getY()
				+ getP().getEquationComponentC() * out.getZ()
				+ getP().getEquationComponentD();
		assertNotEpsilonEquals(0., evalOut);
	}

	@DisplayName("transform(Transform3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void transformTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			
		// Rotation
		var q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);
		
		getP().transform(transform);

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(2.4444444444, getP().getEquationComponentD());

		// Translation
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		transform = new Transform3D();
		transform.makeTranslationMatrix(6, -3, 1);
		
		getP().transform(transform);

		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(-0.0824829046, getP().getEquationComponentD());

		// Rotation Translation
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		transform = new Transform3D();
		transform.makeRotationMatrix(q);
		transform.setTranslation(6, -3, 1);
		
		getP().transform(transform);

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(-3.4071143855375916, getP().getEquationComponentD());
	}

	@DisplayName("transform(Transform3D, Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void transformTransform3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		// Null Pivot, Rotation
		var q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);
		
		getP().transform(transform, null);

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(2.4444444444, getP().getEquationComponentD());

		// Null Pivot, Translation
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		getP().setPivotToDefault();
		transform = new Transform3D();
		transform.makeTranslationMatrix(6, -3, 1);
		
		getP().transform(transform, null);

		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(-0.0824829046, getP().getEquationComponentD());

		// Null Pivot, Rotation Translation
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		getP().setPivotToDefault();
		q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		transform = new Transform3D();
		transform.makeRotationMatrix(q);
		transform.setTranslation(6, -3, 1);
		
		getP().transform(transform, null);

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(-3.4071143855375916, getP().getEquationComponentD());
		
		// Current Pivot, Rotation
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		getP().setPivotToDefault();
		q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		transform = new Transform3D();
		transform.makeRotationMatrix(q);
		
		getP().transform(transform, getP().getPivot().clone());

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(2.4444444444, getP().getEquationComponentD());

		// Current Pivot, Translation
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		getP().setPivotToDefault();
		transform = new Transform3D();
		transform.makeTranslationMatrix(6, -3, 1);
		
		getP().transform(transform, getP().getPivot().clone());

		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(-0.0824829046, getP().getEquationComponentD());

		// Current Pivot, Rotation Translation
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		getP().setPivotToDefault();
		q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		transform = new Transform3D();
		transform.makeRotationMatrix(q);
		transform.setTranslation(6, -3, 1);
		
		getP().transform(transform, getP().getPivot().clone());

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(-3.4071143855375916, getP().getEquationComponentD());

		// Pivot around (0,0,0), Rotation
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		getP().setPivotToDefault();
		q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		transform = new Transform3D();
		transform.makeRotationMatrix(q);

		getP().transform(transform, createPoint(0,0,0));

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(4, getP().getEquationComponentD());

		// Pivot around (0,0,0), Translation
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		getP().setPivotToDefault();

		transform = new Transform3D();
		transform.makeTranslationMatrix(6, -3, 1);

		getP().transform(transform, createPoint(0,0,0));

		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(-0.0824829046, getP().getEquationComponentD());

		// Pivot around (0,0,0), Rotation Translation
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		getP().setPivotToDefault();
		q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		transform = new Transform3D();
		transform.makeRotationMatrix(q);
		transform.setTranslation(6, -3, 1);
		
		getP().transform(transform, createPoint(0, 0, 0));

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(-1.85155883, getP().getEquationComponentD());

		// Pivot (1,-2,3), Rotation
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		getP().setPivotToDefault();
		q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		transform = new Transform3D();
		transform.makeRotationMatrix(q);
		
		getP().transform(transform, createPoint(1,-2,3));

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(4.8164965809, getP().getEquationComponentD());

		// Pivot (1,-2,3), Translation
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		getP().setPivotToDefault();
		transform = new Transform3D();
		transform.makeTranslationMatrix(6, -3, 1);
		
		getP().transform(transform, createPoint(1, -2, 3));

		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(-0.0824829046, getP().getEquationComponentD());


		// Pivot (1,-2,3), Rotation Translation
		getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		getP().setPivotToDefault();
		q = createQuaternion(1, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		transform = new Transform3D();
		transform.makeRotationMatrix(q);
		transform.setTranslation(6, -3, 1);
		
		getP().transform(transform, createPoint(1, -2, 3));

		assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
		assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
		assertEpsilonEquals(-1.0350622491, getP().getEquationComponentD());
	}

	@DisplayName("calculatesPlaneAlignedBoxDistance")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void calculatesPlaneAlignedBoxDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(4., Plane3afp.calculatesPlaneAlignedBoxDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., 2., 2., 2.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneAlignedBoxDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5., -2., -1., -3., 0., 1.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneAlignedBoxDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -6., -3., -1., -4., -1., 1.));
		assertEpsilonEquals(-1.307227776, Plane3afp.calculatesPlaneAlignedBoxDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -8., -3., -2., -6., -1., 0.));

		assertEpsilonEquals(-4., Plane3afp.calculatesPlaneAlignedBoxDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, 0., 0., 0., 2., 2., 2.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneAlignedBoxDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -5., -2., -1., -3., 0., 1.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneAlignedBoxDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -6., -3., -1., -4., -1., 1.));
		assertEpsilonEquals(1.307227776, Plane3afp.calculatesPlaneAlignedBoxDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -8., -3., -2., -6., -1., 0.));
	}

	@DisplayName("calculatesPlanePointDistance")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void calculatesPlanePointDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(4, Plane3afp.calculatesPlanePointDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0, 0, 0));
		assertEpsilonEquals(-4, Plane3afp.calculatesPlanePointDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, 0, 0, 0));
		assertEpsilonEquals(-2.94022093788, Plane3afp.calculatesPlanePointDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0));
		assertEpsilonEquals(2.940220937885, Plane3afp.calculatesPlanePointDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -5, -7, 0));
		assertEpsilonEquals(5.6329931618, Plane3afp.calculatesPlanePointDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -1, 8, -2));
		assertEpsilonEquals(-5.6329931618, Plane3afp.calculatesPlanePointDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -1, 8, -2));
	}

	@DisplayName("calculatesPlaneSphereDistance")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void calculatesPlaneSphereDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., 2.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -1.6329931618554523, -0.8164965809277261, -0.8164965809277261, 2.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.45, -1.22, -1.22, 2.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -4.08, -2.04, -2.04, 2.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -4.8989794856, -2.4494897428, -2.4494897428, 2.));
		assertEpsilonEquals(-1., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5.7154760665, -2.8577380332, -2.8577380332, 2.));
	}

	@DisplayName("calculatesPlanePlaneDistance")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void calculatesPlanePlaneDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D));
		assertEpsilonEquals(18., Plane3afp.calculatesPlanePlaneDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 18, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		assertEpsilonEquals(18., Plane3afp.calculatesPlanePlaneDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D + 18, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		//
		var x = NORMAL_X - 1.;
		var y = NORMAL_Y;
		var z = NORMAL_Z;
		var l = Math.sqrt(x * x + y * y + z * z);
		assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(x / l, y / l, z / l, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		//
		x = NORMAL_X;
		y = NORMAL_Y - 1.;
		z = NORMAL_Z;
		l = Math.sqrt(x * x + y * y + z * z);
		assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(x / l, y / l, z / l, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		//
		x = NORMAL_X;
		y = NORMAL_Y;
		z = NORMAL_Z - 1.;
		l = Math.sqrt(x * x + y * y + z * z);
		assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(x / l, y / l, z / l, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
	}

	@DisplayName("classifies(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void classifiesPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPoint(0, 0, 0)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createPoint(-8, -4, -4)));
		//
		getP().negate();
		assertSame(PlaneClassification.BEHIND, getP().classifies(createPoint(0, 0, 0)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPoint(-8, -4, -4)));
	}

	@DisplayName("classifiesPlaneAlignedBox")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void classifiesPlaneAlignedBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneAlignedBox(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., 2., 2., 2.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneAlignedBox(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5., -2., -1., -3., 0., 1.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneAlignedBox(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -6., -3., -1., -4., -1., 1.));
		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneAlignedBox(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -8., -3., -2., -6., -1., 0.));

		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneAlignedBox(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, 0., 0., 0., 2., 2., 2.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneAlignedBox(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -5., -2., -1., -3., 0., 1.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneAlignedBox(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -6., -3., -1., -4., -1., 1.));
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneAlignedBox(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -8., -3., -2., -6., -1., 0.));
	}

	@DisplayName("classifiesPlanePlane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void classifiesPlanePlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 1., -3., 4., -4.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -1., 3., -4., 4.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));

		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlanePlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D + 2));
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlanePlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D + 4));

		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlanePlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D - 2));
		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlanePlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D - 4));
		
		//
		
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, 1., -3., 4., -4.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -1., 3., -4., 4.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));

		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlanePlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, NORMAL_X, NORMAL_Y, NORMAL_Z, D + 2));
		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlanePlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, NORMAL_X, NORMAL_Y, NORMAL_Z, D + 4));

		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlanePlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, NORMAL_X, NORMAL_Y, NORMAL_Z, D - 2));
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlanePlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, NORMAL_X, NORMAL_Y, NORMAL_Z, D - 4));
	}

	@DisplayName("classifiesPlanePoint")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void classifiesPlanePoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlanePoint(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0, 0, 0));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePoint(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlanePoint(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -8, -4, -4));
		//
		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlanePoint(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, 0, 0, 0));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePoint(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlanePoint(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -8, -4, -4));
	}

	@DisplayName("classifiesPlaneSegment")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void classifiesPlaneSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2., 2., -2.));
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2., 2., -2., 0., 0., 0.));

		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.));

		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -5, -7, 0));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, 0., 0., 0.));

		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -9, -2., -1, -5, -7, 0));
		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, -9, -2., -1));
		
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814));
	}

	@DisplayName("classifiesPlaneSphere")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void classifiesPlaneSphere(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., 2.));
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -1.6329931618554523, -0.8164965809277261, -0.8164965809277261, 2.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.45, -1.22, -1.22, 2.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -4.08, -2.04, -2.04, 2.));
		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -4.8989794856, -2.4494897428, -2.4494897428, 2.));
		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5.7154760665, -2.8577380332, -2.8577380332, 2.));
	}

	private void assertReceiverInvoked(Point3D<?, ?, ?> p, Vector3D<?, ?, ?> v, Point3D<?, ?, ?> a1, Point3D<?, ?, ?> a2) {
		assertEpsilonEquals(p, a1);
		var v0 = a2.operator_minus(a1);
		assertEpsilonColinear(v, v0);
	}

	@DisplayName("findsPlanePlaneIntersection(..., Point3D, Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void findsPlanePlaneIntersectionDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoublePoint3DPoint3D(
			CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p1;
		Point3D p2;

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0.707106781373, 0., -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0.707106781373, 0., 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0., 0.707106781373, 0.707106781373, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0., 0.707106781373, 0.707106781373, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0, 0.707106781373, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0, 0.707106781373, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.577350269, 0.577350269, 0.577350269, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(1., -1., 0.), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.577350269, 0.577350269, 0.577350269, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(1., -1., 0.), p1, p2);

		//

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0.707106781373, 0, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0.707106781373, 0., 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0., 0.707106781373, 0.707106781373, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0., 0.707106781373, 0.707106781373, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), p1, p2);
		
		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0, 0.707106781373, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0, 0.707106781373, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.577350269, 0.577350269, 0.577350269, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.577350269, 0.577350269, 0.577350269, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), p1, p2);


		//

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0.707106781373, 0, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(-3.26598632371, 5.0337532762, -8.2997395999),
				createVector(0.28867513467, -0.28867513467, -0.28867513467), p1, p2);
		
		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0.707106781373, 0., 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-3.26598632371, 1.49821937121, -4.764205695),
				createVector(.28867513467, -.28867513467, -.28867513467), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0., 0.707106781373, 0.707106781373, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(-5.7828629618, .88388347625, .88388347625),
				createVector(0., .57735026934, -.57735026934), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0., 0.707106781373, 0.707106781373, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-4.01509600932, -.88388347625, -.88388347625),
				createVector(0., .57735026934, -.57735026934), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0, 0.707106781373, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(-3.26598632371, -8.2997395999, 5.0337532762),
				createVector(-.28867513467, .28867513467, .28867513467), p1, p2);
		
		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0, 0.707106781373, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-3.2659863237109032, -4.76420569492157, 1.4982193712106662),
				createVector(-.28867513467, .28867513467, .28867513467), p1, p2);

		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.577350269, 0.577350269, 0.577350269, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(-11.963022481304906, 7.064042995738549, 7.064042995738549),
				createVector(0.0, 0.23570226031810146, -0.23570226031810146), p1, p2);
		
		p1 = createPoint(0, 0, 0);
		p2 = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.577350269, 0.577350269, 0.577350269, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-7.632895460960519, 2.733915975394163, 2.733915975394163),
				createVector(0.0, 0.23570226031810146, -0.23570226031810146), p1, p2);
	}

	private void assertReceiverInvoked(Point3D<?, ?, ?> p, Vector3D<?, ?, ?> v, Point3D<?, ?, ?> a1, Vector3D<?, ?, ?> a2) {
		assertEpsilonEquals(p, a1);
		assertEpsilonColinear(v, a2);
	}

	@DisplayName("findsPlanePlaneIntersection(..., Point3D, Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void findsPlanePlaneIntersectionDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoublePoint3DVector3D(
			CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		Vector3D v;

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0.707106781373, 0., -1.25, p, v));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0.707106781373, 0., 1.25, p, v));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0., 0.707106781373, 0.707106781373, -1.25, p, v));
		assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0., 0.707106781373, 0.707106781373, 1.25, p, v));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0, 0.707106781373, -1.25, p, v));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0, 0.707106781373, 1.25, p, v));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.577350269, 0.577350269, 0.577350269, -1.25, p, v));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(1., -1., 0.), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.577350269, 0.577350269, 0.577350269, 1.25, p, v));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(1., -1., 0.), p, v);

		//

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0.707106781373, 0, -1.25, p, v));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0.707106781373, 0., 1.25, p, v));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0., 0.707106781373, 0.707106781373, -1.25, p, v));
		assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0., 0.707106781373, 0.707106781373, 1.25, p, v));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), p, v);
		
		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0, 0.707106781373, -1.25, p, v));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0, 0.707106781373, 1.25, p, v));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.577350269, 0.577350269, 0.577350269, -1.25, p, v));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.577350269, 0.577350269, 0.577350269, 1.25, p, v));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), p, v);


		//

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0.707106781373, 0, -1.25, p, v));
		assertReceiverInvoked(createPoint(-3.26598632371, 5.0337532762, -8.2997395999),
				createVector(0.28867513467, -0.28867513467, -0.28867513467), p, v);
		
		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0.707106781373, 0., 1.25, p, v));
		assertReceiverInvoked(createPoint(-3.26598632371, 1.49821937121, -4.764205695),
				createVector(.28867513467, -.28867513467, -.28867513467), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0., 0.707106781373, 0.707106781373, -1.25, p, v));
		assertReceiverInvoked(createPoint(-5.7828629618, .88388347625, .88388347625),
				createVector(0., .57735026934, -.57735026934), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0., 0.707106781373, 0.707106781373, 1.25, p, v));
		assertReceiverInvoked(createPoint(-4.01509600932, -.88388347625, -.88388347625),
				createVector(0., .57735026934, -.57735026934), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0, 0.707106781373, -1.25, p, v));
		assertReceiverInvoked(createPoint(-3.26598632371, -8.2997395999, 5.0337532762),
				createVector(-.28867513467, .28867513467, .28867513467), p, v);
		
		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0, 0.707106781373, 1.25, p, v));
		assertReceiverInvoked(createPoint(-3.2659863237109032, -4.76420569492157, 1.4982193712106662),
				createVector(-.28867513467, .28867513467, .28867513467), p, v);

		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.577350269, 0.577350269, 0.577350269, -1.25, p, v));
		assertReceiverInvoked(createPoint(-11.963022481304906, 7.064042995738549, 7.064042995738549),
				createVector(0.0, 0.23570226031810146, -0.23570226031810146), p, v);
		
		p = createPoint(0, 0, 0);
		v = createVector(0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.577350269, 0.577350269, 0.577350269, 1.25, p, v));
		assertReceiverInvoked(createPoint(-7.632895460960519, 2.733915975394163, 2.733915975394163),
				createVector(0.0, 0.23570226031810146, -0.23570226031810146), p, v);
	}

	private void assertReceiverInvoked(Point3D<?, ?, ?> p, Vector3D<?, ?, ?> v, PointVector3DReceiver r) {
		final var px = ArgumentCaptor.forClass(double.class);
		final var py = ArgumentCaptor.forClass(double.class);
		final var pz = ArgumentCaptor.forClass(double.class);
		final var vx = ArgumentCaptor.forClass(double.class);
		final var vy = ArgumentCaptor.forClass(double.class);
		final var vz = ArgumentCaptor.forClass(double.class);
		verify(r).set(px.capture(), py.capture(), pz.capture(), vx.capture(), vy.capture(), vz.capture());
		assertEpsilonEquals(p, createPoint(px.getValue(), py.getValue(), pz.getValue()));
		assertEpsilonColinear(v, createVector(vx.getValue(), vy.getValue(), vz.getValue()));
	}

	@DisplayName("findsPlanePlaneIntersection(..., PointVector3DReceiver)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void findsPlanePlaneIntersectionDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoublePointVector3DReceiver(
			CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		var r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0.707106781373, 0., -1.25, r));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0.707106781373, 0., 1.25, r));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0., 0.707106781373, 0.707106781373, -1.25, r));
		assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0., 0.707106781373, 0.707106781373, 1.25, r));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0, 0.707106781373, -1.25, r));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0, 0.707106781373, 1.25, r));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.577350269, 0.577350269, 0.577350269, -1.25, r));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(1., -1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.577350269, 0.577350269, 0.577350269, 1.25, r));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(1., -1., 0.), r);

		//

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0.707106781373, 0, -1.25, r));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0.707106781373, 0., 1.25, r));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0., 0.707106781373, 0.707106781373, -1.25, r));
		assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0., 0.707106781373, 0.707106781373, 1.25, r));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), r);
		
		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0, 0.707106781373, -1.25, r));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0, 0.707106781373, 1.25, r));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.577350269, 0.577350269, 0.577350269, -1.25, r));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.577350269, 0.577350269, 0.577350269, 1.25, r));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), r);


		//

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0.707106781373, 0, -1.25, r));
		assertReceiverInvoked(createPoint(-3.26598632371, 5.0337532762, -8.2997395999),
				createVector(0.28867513467, -0.28867513467, -0.28867513467), r);
		
		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0.707106781373, 0., 1.25, r));
		assertReceiverInvoked(createPoint(-3.26598632371, 1.49821937121, -4.764205695),
				createVector(.28867513467, -.28867513467, -.28867513467), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0., 0.707106781373, 0.707106781373, -1.25, r));
		assertReceiverInvoked(createPoint(-5.7828629618, .88388347625, .88388347625),
				createVector(0., .57735026934, -.57735026934), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0., 0.707106781373, 0.707106781373, 1.25, r));
		assertReceiverInvoked(createPoint(-4.01509600932, -.88388347625, -.88388347625),
				createVector(0., .57735026934, -.57735026934), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0, 0.707106781373, -1.25, r));
		assertReceiverInvoked(createPoint(-3.26598632371, -8.2997395999, 5.0337532762),
				createVector(-.28867513467, .28867513467, .28867513467), r);
		
		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0, 0.707106781373, 1.25, r));
		assertReceiverInvoked(createPoint(-3.2659863237109032, -4.76420569492157, 1.4982193712106662),
				createVector(-.28867513467, .28867513467, .28867513467), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.577350269, 0.577350269, 0.577350269, -1.25, r));
		assertReceiverInvoked(createPoint(-11.963022481304906, 7.064042995738549, 7.064042995738549),
				createVector(0.0, 0.23570226031810146, -0.23570226031810146), r);
		
		r = mock(PointVector3DReceiver.class);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.577350269, 0.577350269, 0.577350269, 1.25, r));
		assertReceiverInvoked(createPoint(-7.632895460960519, 2.733915975394163, 2.733915975394163),
				createVector(0.0, 0.23570226031810146, -0.23570226031810146), r);
	}

	private void assertReceiverInvoked(Point3D<?, ?, ?> p, Vector3D<?, ?, ?> v, Segment3afp<?, ?, ?, ?, ?, ?, ?> s) {
		assertEpsilonEquals(p, s.getP1());
		var v0 = s.getP2().operator_minus(s.getP1());
		assertEpsilonColinear(v, v0);
	}

	@DisplayName("findsPlanePlaneIntersection(..., Segment)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void findsPlanePlaneIntersectionDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleSegment3afp(
			CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0.707106781373, 0., -1.25, s));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0.707106781373, 0., 1.25, s));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0., 0.707106781373, 0.707106781373, -1.25, s));
		assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0., 0.707106781373, 0.707106781373, 1.25, s));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0, 0.707106781373, -1.25, s));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.707106781373, 0, 0.707106781373, 1.25, s));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.577350269, 0.577350269, 0.577350269, -1.25, s));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(1., -1., 0.), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., 1., -1.25,
				0.577350269, 0.577350269, 0.577350269, 1.25, s));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(1., -1., 0.), s);

		//

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0.707106781373, 0, -1.25, s));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0.707106781373, 0., 1.25, s));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0., 0.707106781373, 0.707106781373, -1.25, s));
		assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0., 0.707106781373, 0.707106781373, 1.25, s));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), s);
		
		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0, 0.707106781373, -1.25, s));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.707106781373, 0, 0.707106781373, 1.25, s));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.577350269, 0.577350269, 0.577350269, -1.25, s));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				0., 0., -1., 1.25,
				0.577350269, 0.577350269, 0.577350269, 1.25, s));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), s);


		//

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0.707106781373, 0, -1.25, s));
		assertReceiverInvoked(createPoint(-3.26598632371, 5.0337532762, -8.2997395999),
				createVector(0.28867513467, -0.28867513467, -0.28867513467), s);
		
		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0.707106781373, 0., 1.25, s));
		assertReceiverInvoked(createPoint(-3.26598632371, 1.49821937121, -4.764205695),
				createVector(.28867513467, -.28867513467, -.28867513467), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0., 0.707106781373, 0.707106781373, -1.25, s));
		assertReceiverInvoked(createPoint(-5.7828629618, .88388347625, .88388347625),
				createVector(0., .57735026934, -.57735026934), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0., 0.707106781373, 0.707106781373, 1.25, s));
		assertReceiverInvoked(createPoint(-4.01509600932, -.88388347625, -.88388347625),
				createVector(0., .57735026934, -.57735026934), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0, 0.707106781373, -1.25, s));
		assertReceiverInvoked(createPoint(-3.26598632371, -8.2997395999, 5.0337532762),
				createVector(-.28867513467, .28867513467, .28867513467), s);
		
		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.707106781373, 0, 0.707106781373, 1.25, s));
		assertReceiverInvoked(createPoint(-3.2659863237109032, -4.76420569492157, 1.4982193712106662),
				createVector(-.28867513467, .28867513467, .28867513467), s);

		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.577350269, 0.577350269, 0.577350269, -1.25, s));
		assertReceiverInvoked(createPoint(-11.963022481304906, 7.064042995738549, 7.064042995738549),
				createVector(0.0, 0.23570226031810146, -0.23570226031810146), s);
		
		s = createSegment(0, 0, 0, 0, 0, 0);
		assertTrue(Plane3afp.findsPlanePlaneIntersection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0.577350269, 0.577350269, 0.577350269, 1.25, s));
		assertReceiverInvoked(createPoint(-7.632895460960519, 2.733915975394163, 2.733915975394163),
				createVector(0.0, 0.23570226031810146, -0.23570226031810146), s);
	}

	@DisplayName("findsPlanePointProjection")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void findsPlanePointProjection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D<?, ?, ?> p;

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjection(
			NORMAL_X, NORMAL_Y, NORMAL_Z, D,
			0, 0, 0,
			p);
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjection(
			-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
			0, 0, 0,
			p);
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjection(
			NORMAL_X, NORMAL_Y, NORMAL_Z, D,
			-5, -7, 0,
			p);
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjection(
			-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
			-5, -7, 0,
			p);
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjection(
			NORMAL_X, NORMAL_Y, NORMAL_Z, D,
			-1, 8, -2,
			p);
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjection(
			-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
			-1, 8, -2,
			p);
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852), p);

		//

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjection(
			1.53 * NORMAL_X, 1.53 * NORMAL_Y, 1.53 * NORMAL_Z, D,
			0, 0, 0,
			p);
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjection(
			-1.53 * NORMAL_X, -1.53 * NORMAL_Y, -1.53 * NORMAL_Z, -D,
			0, 0, 0,
			p);
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjection(
			1.53 * NORMAL_X, 1.53 * NORMAL_Y, 1.53 * NORMAL_Z, D,
			-5, -7, 0,
			p);
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjection(
			-1.53 * NORMAL_X, -1.53 * NORMAL_Y, -1.53 * NORMAL_Z, -D,
			-5, -7, 0,
			p);
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjection(
			1.53 * NORMAL_X, 1.53 * NORMAL_Y, 1.53 * NORMAL_Z, D,
			-1, 8, -2,
			p);
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjection(
			-1.53 * NORMAL_X, -1.53 * NORMAL_Y, -1.53 * NORMAL_Z, -D,
			-1, 8, -2,
			p);
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852), p);
	}

	@DisplayName("findsPlanePointProjectionWithPlaneNormal")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void findsPlanePointProjectionWithPlaneNormal(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D<?, ?, ?> p;

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjectionWithPlaneNormal(
			NORMAL_X, NORMAL_Y, NORMAL_Z, D,
			0, 0, 0,
			p);
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjectionWithPlaneNormal(
			-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
			0, 0, 0,
			p);
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjectionWithPlaneNormal(
			NORMAL_X, NORMAL_Y, NORMAL_Z, D,
			-5, -7, 0,
			p);
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjectionWithPlaneNormal(
			-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
			-5, -7, 0,
			p);
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjectionWithPlaneNormal(
			NORMAL_X, NORMAL_Y, NORMAL_Z, D,
			-1, 8, -2,
			p);
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852), p);

		p = createPoint(0, 0, 0);
		Plane3afp.findsPlanePointProjectionWithPlaneNormal(
			-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
			-1, 8, -2,
			p);
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852), p);
	}

	@DisplayName("findsPlaneSegmentIntersection")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void findsPlaneSegmentIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D<?, ?, ?> p;
		
		p = createPoint(0, 0, 0);
		assertFalse(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2., 2., -2., p));

		p = createPoint(0, 0, 0);
		assertFalse(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2., 2., -2., 0., 0., 0., p));

		p = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814, p));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0., p));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -5, -7, 0, p));
		assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);

		p = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, 0., 0., 0., p));
		assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);

		p = createPoint(0, 0, 0);
		assertFalse(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -9, -2., -1, -5, -7, 0, p));

		p = createPoint(0, 0, 0);
		assertFalse(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, -9, -2., -1, p));

		p = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523, p));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = createPoint(0, 0, 0);
		assertTrue(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814, p));
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);
	}

	@DisplayName("calculatesPlaneSegmentIntersectionFactor")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void calculatesPlaneSegmentIntersectionFactor(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertNaN(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2., 2., -2.));
		assertNaN(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2., 2., -2., 0., 0., 0.));

		assertEpsilonEquals(1., Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.));

		assertEpsilonEquals(.576350527713689, Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -5, -7, 0));
		assertEpsilonEquals(0.423649472286, Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, 0., 0., 0.));

		assertNaN(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -9, -2., -1, -5, -7, 0));
		assertNaN(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, -9, -2., -1));
		
		assertInfinity(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertInfinity(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814));
	}

	@DisplayName("classifies(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void classifiesBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(0., 0., 0., 2., 2., 2.)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(-5., -2., -1., -3., 0., 1.)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(-6., -3., -1., -4., -1., 1.)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(-8., -3., -2., -6., -1., 0.)));
		//
		getP().negate();
		assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(0., 0., 0., 2., 2., 2.)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(-5., -2., -1., -3., 0., 1.)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(-6., -3., -1., -4., -1., 1.)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(-8., -3., -2., -6., -1., 0.)));
	}

	@DisplayName("classifies(x, y, z)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void classifiesDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 0));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertSame(PlaneClassification.BEHIND, getP().classifies(-8, -4, -4));
		//
		getP().negate();
		assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 0));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(-8, -4, -4));
	}

	@DisplayName("classifies(x, y, z, radius)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void classifiesDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0., 0., 0., 2.));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(-2.45, -1.22, -1.22, 2.));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(-3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(-4.08, -2.04, -2.04, 2.));
		assertSame(PlaneClassification.BEHIND, getP().classifies(-5.7154760665, -2.8577380332, -2.8577380332, 2.));
		//
		getP().negate();

		assertSame(PlaneClassification.BEHIND, getP().classifies(0., 0., 0., 2.));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(-2.45, -1.22, -1.22, 2.));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(-3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(-4.08, -2.04, -2.04, 2.));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(-5.7154760665, -2.8577380332, -2.8577380332, 2.));
	}

	@DisplayName("classifies(x1, y1, z1, x2, y2, z2)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void classifiesDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0., 0., 0., 2., 2., 2.));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(-5., -2., -1., -3., 0., 1.));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(-6., -3., -1., -4., -1., 1.));
		assertSame(PlaneClassification.BEHIND, getP().classifies(-8., -3., -2., -6., -1., 0.));
		//
		getP().negate();
		assertSame(PlaneClassification.BEHIND, getP().classifies(0., 0., 0., 2., 2., 2.));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(-5., -2., -1., -3., 0., 1.));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(-6., -3., -1., -4., -1., 1.));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(-8., -3., -2., -6., -1., 0.));
	}

	@DisplayName("classifies(Plane3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void classifiesPlane3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPlane(1., -3., 4., -4.)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPlane(-1., 3., -4., 4.)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));

		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 2)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 4)));

		assertSame(PlaneClassification.BEHIND, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D - 2)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D - 4)));
		
		//
		getP().negate();
		
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPlane(1., -3., 4., -4.)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPlane(-1., 3., -4., 4.)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));

		assertSame(PlaneClassification.BEHIND, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 2)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 4)));

		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D - 2)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D - 4)));
	}

	@DisplayName("classifies(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void classifiesSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(0., 0., 0., -2., 2., -2.)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(-2., 2., -2., 0., 0., 0.)));

		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));

		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(0., 0., 0., -5, -7, 0)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-5, -7, 0, 0., 0., 0.)));

		assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(-9, -2., -1, -5, -7, 0)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(-5, -7, 0, -9, -2., -1)));
		
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));

		//
		getP().negate();

		assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(0., 0., 0., -2., 2., -2.)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(-2., 2., -2., 0., 0., 0.)));

		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));

		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(0., 0., 0., -5, -7, 0)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-5, -7, 0, 0., 0., 0.)));

		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(-9, -2., -1, -5, -7, 0)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(-5, -7, 0, -9, -2., -1)));
		
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
	}

	@DisplayName("classifies(Shepre3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void classifiesSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSphere(0., 0., 0., 2.)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(-2.45, -1.22, -1.22, 2.)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(-4.08, -2.04, -2.04, 2.)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createSphere(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
		//
		getP().negate();

		assertSame(PlaneClassification.BEHIND, getP().classifies(createSphere(0., 0., 0., 2.)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(-2.45, -1.22, -1.22, 2.)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(-4.08, -2.04, -2.04, 2.)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSphere(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
	}

	@DisplayName("getDistanceTo(x, y ,z)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceToDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(4, getP().getDistanceTo(0, 0, 0));
		assertEpsilonEquals(-2.94022093788, getP().getDistanceTo(-5, -7, 0));
		assertEpsilonEquals(5.6329931618, getP().getDistanceTo(-1, 8, -2));
		getP().negate();
		assertEpsilonEquals(-4, getP().getDistanceTo(0, 0, 0));
		assertEpsilonEquals(2.940220937885, getP().getDistanceTo(-5, -7, 0));
		assertEpsilonEquals(-5.6329931618, getP().getDistanceTo(-1, 8, -2));
	}

	@DisplayName("getDistanceTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(4, getP().getDistanceTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(-2.94022093788, getP().getDistanceTo(createPoint(-5, -7, 0)));
		assertEpsilonEquals(5.6329931618, getP().getDistanceTo(createPoint(-1, 8, -2)));
		getP().negate();
		assertEpsilonEquals(-4, getP().getDistanceTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(2.940220937885, getP().getDistanceTo(createPoint(-5, -7, 0)));
		assertEpsilonEquals(-5.6329931618, getP().getDistanceTo(createPoint(-1, 8, -2)));
	}

	@DisplayName("getDistanceTo(a, b, c, d) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceToDoubleDoubleDoubleDouble_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., 1., 0., -1.25));
		assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., 4.5));
		assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., -1., 0., 1.25));
		assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., 4.5));
		//
		getP().negate();
		assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., 1., 0., -1.25));
		assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., 4.5));
		assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., -1., 0., 1.25));
		assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., 4.5));
	}

	@DisplayName("getDistanceTo(a, b, c, d) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceToDoubleDoubleDoubleDouble_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, getP().getDistanceTo(0., 0., 1., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., 0., 1., -1.25));
		assertEpsilonEquals(0, getP().getDistanceTo(0., 0., 1., 4.5));
		assertEpsilonEquals(0, getP().getDistanceTo(0., 0., -1., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., 0., -1., 1.25));
		assertEpsilonEquals(0, getP().getDistanceTo(0., 0., -1., 4.5));
		//
		getP().negate();
		assertEpsilonEquals(0, getP().getDistanceTo(0., 0., 1., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., 0., 1., -1.25));
		assertEpsilonEquals(0, getP().getDistanceTo(0., 0., 1., 4.5));
		assertEpsilonEquals(0, getP().getDistanceTo(0., 0., -1., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., 0., -1., 1.25));
		assertEpsilonEquals(0, getP().getDistanceTo(0., 0., -1., 4.5));
	}

	@DisplayName("getDistanceTo(a, b, c, d) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceToDoubleDoubleDoubleDouble_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -1.25));
		assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., 4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 1.25));
		assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 4.5));
		//
		getP().negate();
		assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -1.25));
		assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., 4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 1.25));
		assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 4.5));
	}

	@DisplayName("getDistanceTo(a, b, c, d) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceToDoubleDoubleDoubleDouble_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getP().getDistanceTo(NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		assertEpsilonEquals(0., getP().getDistanceTo(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D));
		assertEpsilonEquals(18., getP().getDistanceTo(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 18));
		assertEpsilonEquals(18., getP().getDistanceTo(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D + 18));
		//
		var x = NORMAL_X + 1;
		var y = NORMAL_Y;
		var z = NORMAL_Z;
		var l = Math.sqrt(x * x + y * y + z * z);
		assertEpsilonEquals(0., getP().getDistanceTo(x / l, y / l, z / l, D));
		//
		x = NORMAL_X;
		y = NORMAL_Y + 1;
		z = NORMAL_Z;
		l = Math.sqrt(x * x + y * y + z * z);
		assertEpsilonEquals(0., getP().getDistanceTo(x / l, y / l, z / l, D));
		//
		x = NORMAL_X;
		y = NORMAL_Y;
		z = NORMAL_Z + 1;
		l = Math.sqrt(x * x + y * y + z * z);
		assertEpsilonEquals(0., getP().getDistanceTo(x / l, y / l, z / l, D));
	}

	@DisplayName("getIntersection(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getIntersectionSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;

		assertNull(getP().getIntersection(createSegment(0., 0., 0., -2., 2., -2.)));

		assertNull(getP().getIntersection(createSegment(-2., 2., -2., 0., 0., 0.)));

		p = getP().getIntersection(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = getP().getIntersection(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = getP().getIntersection(createSegment(0., 0., 0., -5, -7, 0));
		assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);

		p = getP().getIntersection(createSegment(-5, -7, 0, 0., 0., 0.));
		assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);

		assertNull(getP().getIntersection(createSegment(-9, -2., -1, -5, -7, 0)));

		assertNull(getP().getIntersection(createSegment(-5, -7, 0, -9, -2., -1)));

		p = getP().getIntersection(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = getP().getIntersection(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);

		//
		getP().negate();

		assertNull(getP().getIntersection(createSegment(0., 0., 0., -2., 2., -2.)));

		assertNull(getP().getIntersection(createSegment(-2., 2., -2., 0., 0., 0.)));

		p = getP().getIntersection(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = getP().getIntersection(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = getP().getIntersection(createSegment(0., 0., 0., -5, -7, 0));
		assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);

		p = getP().getIntersection(createSegment(-5, -7, 0, 0., 0., 0.));
		assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);

		assertNull(getP().getIntersection(createSegment(-9, -2., -1, -5, -7, 0)));

		assertNull(getP().getIntersection(createSegment(-5, -7, 0, -9, -2., -1)));

		p = getP().getIntersection(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = getP().getIntersection(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);
	}

	@DisplayName("intersects(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().intersects(createAlignedBoxFromPoints(0., 0., 0., 2., 2., 2.)));
		assertTrue(getP().intersects(createAlignedBoxFromPoints(-5., -2., -1., -3., 0., 1.)));
		assertTrue(getP().intersects(createAlignedBoxFromPoints(-6., -3., -1., -4., -1., 1.)));
		assertFalse(getP().intersects(createAlignedBoxFromPoints(-8., -3., -2., -6., -1., 0.)));
		//
		getP().negate();
		assertFalse(getP().intersects(createAlignedBoxFromPoints(0., 0., 0., 2., 2., 2.)));
		assertTrue(getP().intersects(createAlignedBoxFromPoints(-5., -2., -1., -3., 0., 1.)));
		assertTrue(getP().intersects(createAlignedBoxFromPoints(-6., -3., -1., -4., -1., 1.)));
		assertFalse(getP().intersects(createAlignedBoxFromPoints(-8., -3., -2., -6., -1., 0.)));
	}

	@DisplayName("intersects(x, y ,z)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().intersects(0, 0, 0));
		assertTrue(getP().intersects(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertFalse(getP().intersects(-8, -4, -4));
		//
		getP().negate();
		assertFalse(getP().intersects(0, 0, 0));
		assertTrue(getP().intersects(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertFalse(getP().intersects(-8, -4, -4));
	}

	@DisplayName("intersects(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().intersects(createPoint(0, 0, 0)));
		assertTrue(getP().intersects(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertFalse(getP().intersects(createPoint(-8, -4, -4)));
		//
		getP().negate();
		assertFalse(getP().intersects(createPoint(0, 0, 0)));
		assertTrue(getP().intersects(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertFalse(getP().intersects(createPoint(-8, -4, -4)));
	}

	@DisplayName("intersects(lx1,ly1,lz1, ux2,uy2,uz2)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().intersects(0., 0., 0., 2., 2., 2.));
		assertTrue(getP().intersects(-5., -2., -1., -3., 0., 1.));
		assertTrue(getP().intersects(-6., -3., -1., -4., -1., 1.));
		assertFalse(getP().intersects(-8., -3., -2., -6., -1., 0.));
		//
		getP().negate();
		assertFalse(getP().intersects(0., 0., 0., 2., 2., 2.));
		assertTrue(getP().intersects(-5., -2., -1., -3., 0., 1.));
		assertTrue(getP().intersects(-6., -3., -1., -4., -1., 1.));
		assertFalse(getP().intersects(-8., -3., -2., -6., -1., 0.));
	}

	@DisplayName("intersects(x, y, z, radius)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().intersects(0., 0., 0., 2.));
		assertTrue(getP().intersects(-2.45, -1.22, -1.22, 2.));
		assertTrue(getP().intersects(-3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		assertTrue(getP().intersects(-4.08, -2.04, -2.04, 2.));
		assertFalse(getP().intersects(-5.7154760665, -2.8577380332, -2.8577380332, 2.));
		//
		getP().negate();

		assertFalse(getP().intersects(0., 0., 0., 2.));
		assertTrue(getP().intersects(-2.45, -1.22, -1.22, 2.));
		assertTrue(getP().intersects(-3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		assertTrue(getP().intersects(-4.08, -2.04, -2.04, 2.));
		assertFalse(getP().intersects(-5.7154760665, -2.8577380332, -2.8577380332, 2.));
	}

	@DisplayName("intersects(Plane3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsPlane3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertTrue(getP().intersects(createPlane(A, B, C, D)));
		assertTrue(getP().intersects(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		assertTrue(getP().intersects(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		assertFalse(getP().intersects(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		assertTrue(getP().intersects(createPlane(1., 2., 0., 5.)));

		//
		getP().negate();

		assertTrue(getP().intersects(createPlane(A, B, C, D)));
		assertTrue(getP().intersects(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		assertTrue(getP().intersects(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		assertFalse(getP().intersects(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		assertTrue(getP().intersects(createPlane(1., 2., 0., 5.)));
	}

	@DisplayName("intersects(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertFalse(getP().intersects(createSegment(0., 0., 0., -2., 2., -2.)));
		assertFalse(getP().intersects(createSegment(-2., 2., -2., 0., 0., 0.)));
		assertTrue(getP().intersects(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		assertTrue(getP().intersects(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));
		assertTrue(getP().intersects(createSegment(0., 0., 0., -5, -7, 0)));
		assertTrue(getP().intersects(createSegment(-5, -7, 0, 0., 0., 0.)));
		assertFalse(getP().intersects(createSegment(-9, -2., -1, -5, -7, 0)));
		assertFalse(getP().intersects(createSegment(-5, -7, 0, -9, -2., -1)));
		assertTrue(getP().intersects(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertTrue(getP().intersects(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		
		//
		getP().negate();

		assertFalse(getP().intersects(createSegment(0., 0., 0., -2., 2., -2.)));
		assertFalse(getP().intersects(createSegment(-2., 2., -2., 0., 0., 0.)));
		assertTrue(getP().intersects(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		assertTrue(getP().intersects(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));
		assertTrue(getP().intersects(createSegment(0., 0., 0., -5, -7, 0)));
		assertTrue(getP().intersects(createSegment(-5, -7, 0, 0., 0., 0.)));
		assertFalse(getP().intersects(createSegment(-9, -2., -1, -5, -7, 0)));
		assertFalse(getP().intersects(createSegment(-5, -7, 0, -9, -2., -1)));
		assertTrue(getP().intersects(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertTrue(getP().intersects(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
	}

	@DisplayName("intersects(Sphere3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().intersects(createSphere(0., 0., 0., 2.)));
		assertTrue(getP().intersects(createSphere(-2.45, -1.22, -1.22, 2.)));
		assertTrue(getP().intersects(createSphere(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		assertTrue(getP().intersects(createSphere(-4.08, -2.04, -2.04, 2.)));
		assertFalse(getP().intersects(createSphere(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
		//
		getP().negate();

		assertFalse(getP().intersects(createSphere(0., 0., 0., 2.)));
		assertTrue(getP().intersects(createSphere(-2.45, -1.22, -1.22, 2.)));
		assertTrue(getP().intersects(createSphere(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		assertTrue(getP().intersects(createSphere(-4.08, -2.04, -2.04, 2.)));
		assertFalse(getP().intersects(createSphere(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
	}

	@DisplayName("p && Box3afp")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_andBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().operator_and(createAlignedBoxFromPoints(0., 0., 0., 2., 2., 2.)));
		assertTrue(getP().operator_and(createAlignedBoxFromPoints(-5., -2., -1., -3., 0., 1.)));
		assertTrue(getP().operator_and(createAlignedBoxFromPoints(-6., -3., -1., -4., -1., 1.)));
		assertFalse(getP().operator_and(createAlignedBoxFromPoints(-8., -3., -2., -6., -1., 0.)));
		//
		getP().negate();
		assertFalse(getP().operator_and(createAlignedBoxFromPoints(0., 0., 0., 2., 2., 2.)));
		assertTrue(getP().operator_and(createAlignedBoxFromPoints(-5., -2., -1., -3., 0., 1.)));
		assertTrue(getP().operator_and(createAlignedBoxFromPoints(-6., -3., -1., -4., -1., 1.)));
		assertFalse(getP().operator_and(createAlignedBoxFromPoints(-8., -3., -2., -6., -1., 0.)));
	}

	@DisplayName("p && Segment3afp")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_andSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertFalse(getP().operator_and(createSegment(0., 0., 0., -2., 2., -2.)));
		assertFalse(getP().operator_and(createSegment(-2., 2., -2., 0., 0., 0.)));
		assertTrue(getP().operator_and(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		assertTrue(getP().operator_and(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));
		assertTrue(getP().operator_and(createSegment(0., 0., 0., -5, -7, 0)));
		assertTrue(getP().operator_and(createSegment(-5, -7, 0, 0., 0., 0.)));
		assertFalse(getP().operator_and(createSegment(-9, -2., -1, -5, -7, 0)));
		assertFalse(getP().operator_and(createSegment(-5, -7, 0, -9, -2., -1)));
		assertTrue(getP().operator_and(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertTrue(getP().operator_and(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		
		//
		getP().negate();

		assertFalse(getP().operator_and(createSegment(0., 0., 0., -2., 2., -2.)));
		assertFalse(getP().operator_and(createSegment(-2., 2., -2., 0., 0., 0.)));
		assertTrue(getP().operator_and(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		assertTrue(getP().operator_and(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));
		assertTrue(getP().operator_and(createSegment(0., 0., 0., -5, -7, 0)));
		assertTrue(getP().operator_and(createSegment(-5, -7, 0, 0., 0., 0.)));
		assertFalse(getP().operator_and(createSegment(-9, -2., -1, -5, -7, 0)));
		assertFalse(getP().operator_and(createSegment(-5, -7, 0, -9, -2., -1)));
		assertTrue(getP().operator_and(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertTrue(getP().operator_and(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
	}

	@DisplayName("p && Sphere3afp")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_andSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().operator_and(createSphere(0., 0., 0., 2.)));
		assertTrue(getP().operator_and(createSphere(-2.45, -1.22, -1.22, 2.)));
		assertTrue(getP().operator_and(createSphere(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		assertTrue(getP().operator_and(createSphere(-4.08, -2.04, -2.04, 2.)));
		assertFalse(getP().operator_and(createSphere(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
		//
		getP().negate();

		assertFalse(getP().intersects(createSphere(0., 0., 0., 2.)));
		assertTrue(getP().intersects(createSphere(-2.45, -1.22, -1.22, 2.)));
		assertTrue(getP().intersects(createSphere(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		assertTrue(getP().intersects(createSphere(-4.08, -2.04, -2.04, 2.)));
		assertFalse(getP().intersects(createSphere(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
	}

	@DisplayName("p += double")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_addDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().operator_add(7);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(11, getP().getEquationComponentD());
		//
		getP().operator_add(-25);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(-14, getP().getEquationComponentD());
	}

	@DisplayName("p += Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().operator_add(createVector(1, 2, 3));
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(1.1422619667529594, getP().getEquationComponentD());
	}

	@DisplayName("p && Plane3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_andPlane3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertTrue(getP().operator_and(createPlane(A, B, C, D)));
		assertTrue(getP().operator_and(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		assertTrue(getP().operator_and(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		assertFalse(getP().operator_and(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		assertTrue(getP().operator_and(createPlane(1., 2., 0., 5.)));

		//
		getP().negate();

		assertTrue(getP().operator_and(createPlane(A, B, C, D)));
		assertTrue(getP().operator_and(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		assertTrue(getP().operator_and(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		assertFalse(getP().operator_and(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		assertTrue(getP().operator_and(createPlane(1., 2., 0., 5.)));
	}

	@DisplayName("p && Point3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_andPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().operator_and(createPoint(0, 0, 0)));
		assertTrue(getP().operator_and(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertFalse(getP().operator_and(createPoint(-8, -4, -4)));
		//
		getP().negate();
		assertFalse(getP().operator_and(createPoint(0, 0, 0)));
		assertTrue(getP().operator_and(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertFalse(getP().operator_and(createPoint(-8, -4, -4)));
	}

	@DisplayName("p == Plane3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_equalsPlane3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertTrue(getP().operator_equals(createPlane(A, B, C, D)));
		assertTrue(getP().operator_equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		assertFalse(getP().operator_equals(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		assertFalse(getP().operator_equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		assertFalse(getP().operator_equals(createPlane(1., 2., 0., 5.)));

		//
		getP().negate();

		assertFalse(getP().operator_equals(createPlane(A, B, C, D)));
		assertFalse(getP().operator_equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		assertTrue(getP().operator_equals(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		assertFalse(getP().operator_equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		assertFalse(getP().operator_equals(createPlane(1., 2., 0., 5.)));
	}

	@DisplayName("p != Plane3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_notEqualsPlane3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertFalse(getP().operator_notEquals(createPlane(A, B, C, D)));
		assertFalse(getP().operator_notEquals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		assertTrue(getP().operator_notEquals(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		assertTrue(getP().operator_notEquals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		assertTrue(getP().operator_notEquals(createPlane(1., 2., 0., 5.)));

		//
		getP().negate();

		assertTrue(getP().operator_notEquals(createPlane(A, B, C, D)));
		assertTrue(getP().operator_notEquals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		assertFalse(getP().operator_notEquals(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		assertTrue(getP().operator_notEquals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		assertTrue(getP().operator_notEquals(createPlane(1., 2., 0., 5.)));
	}

	@DisplayName("-p")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_minus(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var p = getP().operator_minus();
		assertNotSame(this.plane, p);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(D, getP().getEquationComponentD());
		assertEpsilonEquals(-NORMAL_X, p.getEquationComponentA());
		assertEpsilonEquals(-NORMAL_Y, p.getEquationComponentB());
		assertEpsilonEquals(-NORMAL_Z, p.getEquationComponentC());
		assertEpsilonEquals(-D, p.getEquationComponentD());
	}

	@DisplayName("p - double")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_minusDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		var p = getP().operator_minus(-7);
		assertNotSame(this.plane, p);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(D, getP().getEquationComponentD());
		assertEpsilonEquals(NORMAL_X, p.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, p.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, p.getEquationComponentC());
		assertEpsilonEquals(11, p.getEquationComponentD());
		//
		p = getP().operator_minus(25);
		assertNotSame(this.plane, p);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(D, getP().getEquationComponentD());
		assertEpsilonEquals(NORMAL_X, p.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, p.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, p.getEquationComponentC());
		assertEpsilonEquals(-21, p.getEquationComponentD());
	}

	@DisplayName("p - Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		var p = getP().operator_minus(createVector(-1, -2, -3));
		assertNotSame(this.plane, p);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(D, getP().getEquationComponentD());
		assertEpsilonEquals(NORMAL_X, p.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, p.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, p.getEquationComponentC());
		assertEpsilonEquals(1.1422619667529594, p.getEquationComponentD());
	}

	@DisplayName("p * Quaternion")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_multiplyQuaternion(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		var q = createQuaternion(0, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);
		
		var p = getP().operator_multiply(q);

		assertNotSame(this.plane, p);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(D, getP().getEquationComponentD());
		assertEpsilonEquals(0.9525793444, p.getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, p.getEquationComponentB());
		assertEpsilonEquals(-0.272165527, p.getEquationComponentC());
		assertEpsilonEquals(2.44444444444, p.getEquationComponentD());
	}

	@DisplayName("p * Transform3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_multiplyTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		var q = createQuaternion(0, 0, 0, 0);
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);
		transform.setTranslation(6, -3, 1);
		
		var p = getP().operator_multiply(transform);

		assertNotSame(this.plane, p);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(D, getP().getEquationComponentD());
		assertEpsilonEquals(0.9525793444, p.getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, p.getEquationComponentB());
		assertEpsilonEquals(-0.272165527, p.getEquationComponentC());
		assertEpsilonEquals(-3.4071143855375916, p.getEquationComponentD());
	}

	@DisplayName("p + double")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_plusDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		var p = getP().operator_plus(7);
		assertNotSame(this.plane, p);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(D, getP().getEquationComponentD());
		assertEpsilonEquals(NORMAL_X, p.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, p.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, p.getEquationComponentC());
		assertEpsilonEquals(11, p.getEquationComponentD());
		//
		p = getP().operator_plus(-25);
		assertNotSame(this.plane, p);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(D, getP().getEquationComponentD());
		assertEpsilonEquals(NORMAL_X, p.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, p.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, p.getEquationComponentC());
		assertEpsilonEquals(-21, p.getEquationComponentD());
	}

	@DisplayName("p + Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var p = getP().operator_plus(createVector(1, 2, 3));
		assertNotSame(this.plane, p);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(D, getP().getEquationComponentD());
		assertEpsilonEquals(NORMAL_X, p.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, p.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, p.getEquationComponentC());
		assertEpsilonEquals(1.1422619667529594, p.getEquationComponentD());
	}

	@DisplayName("p -= double")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_removeDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().operator_remove(-7);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(11, getP().getEquationComponentD());
		//
		getP().operator_remove(25);
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(-14, getP().getEquationComponentD());
	}

	@DisplayName("p -= Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().operator_remove(createVector(-1, -2, -3));
		assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		assertEpsilonEquals(1.1422619667529594, getP().getEquationComponentD());
	}

	@DisplayName("p .. Plane3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_upToPlane3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getP().operator_upTo(createPlane(A, B, C, D)));
		assertEpsilonEquals(0., getP().operator_upTo(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		assertEpsilonEquals(0., getP().operator_upTo(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		assertEpsilonEquals(18., getP().operator_upTo(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 18)));
		assertEpsilonEquals(18., getP().operator_upTo(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D + 18)));
		//
		assertEpsilonEquals(0., getP().operator_upTo(createPlane(NORMAL_X - 1, NORMAL_Y, NORMAL_Z, D)));
		assertEpsilonEquals(0., getP().operator_upTo(createPlane(NORMAL_X, NORMAL_Y - 1, NORMAL_Z, D)));
		assertEpsilonEquals(0., getP().operator_upTo(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z - 1, D)));
	}

	@DisplayName("p .. Point3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_upToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(4, getP().operator_upTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(-2.94022093788, getP().operator_upTo(createPoint(-5, -7, 0)));
		assertEpsilonEquals(5.6329931618, getP().operator_upTo(createPoint(-1, 8, -2)));
		getP().negate();
		assertEpsilonEquals(-4, getP().operator_upTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(2.940220937885, getP().operator_upTo(createPoint(-5, -7, 0)));
		assertEpsilonEquals(-5.6329931618, getP().operator_upTo(createPoint(-1, 8, -2)));
	}

}
