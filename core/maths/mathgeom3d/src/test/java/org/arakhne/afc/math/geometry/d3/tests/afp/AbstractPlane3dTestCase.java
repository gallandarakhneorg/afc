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

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.PlaneClassification;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.PointVector3DReceiver;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.Plane3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
	@Nested
	public class ToGeogebra {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals("0.8164965809277261*x+0.4082482904638631*y+0.4082482904638631*z+4.0=0.0", getP().toGeogebra());
		}
	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsObject {

		@DisplayName("(Plane3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().equals(createPlane(A, B, C, D)));
		}

		@DisplayName("(Plane3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		}

		@DisplayName("(Plane3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		}

		@DisplayName("(Plane3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		}

		@DisplayName("(Plane3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(1., 2., 0., 5.)));
		}

		@DisplayName("(Plane3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().equals(createPlane(A, B, C, D)));
		}

		@DisplayName("(Plane3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		}

		@DisplayName("(Plane3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().equals(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		}

		@DisplayName("(Plane3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		}

		@DisplayName("(Plane3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().equals(createPlane(1., 2., 0., 5.)));
		}
	}

	@DisplayName("absolute")
	@Nested
	public class Absolute {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().absolute();
			assertEpsilonEquals(0.8164965809277261, getP().getEquationComponentA());
			assertEpsilonEquals(0.4082482904638631, getP().getEquationComponentB());
			assertEpsilonEquals(0.4082482904638631, getP().getEquationComponentC());
			assertEpsilonEquals(4, getP().getEquationComponentD());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
	}

	@DisplayName("clear")
	@Nested
	public class Clear {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().clear();
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
	}

	@DisplayName("getEquationComponentA")
	@Nested
	public class GetEquationComponentA {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
		}
	}

	@DisplayName("getEquationComponentB")
	@Nested
	public class GetEquationComponentB {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
		}
	}

	@DisplayName("getEquationComponentC")
	@Nested
	public class GetEquationComponentC {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
		}
	}

	@DisplayName("getEquationComponentD")
	@Nested
	public class GetEquationComponentD {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(D, getP().getEquationComponentD());
		}
	}

	@DisplayName("getNormal")
	@Nested
	public class GetNormal {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createVector(NORMAL_X, NORMAL_Y, NORMAL_Z), getP().getNormal());
		}
	}

	@DisplayName("getNormalX")
	@Nested
	public class GetNormalX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(NORMAL_X, getP().getNormalX());
		}
	}

	@DisplayName("getNormalY")
	@Nested
	public class GetNormalY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(NORMAL_Y, getP().getNormalY());
		}
	}

	@DisplayName("getNormalZ")
	@Nested
	public class GetNormalZ {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(NORMAL_Z, getP().getNormalZ());
		}
	}

	@DisplayName("getPivot")
	@Nested
	public class GetPivot {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523),
					getP().getPivot());
		}
	}

	@DisplayName("translate")
	@Nested
	public class Translate {

		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().translate(7);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(11, getP().getEquationComponentD());
		}

		@DisplayName("(double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void double_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().translate(-18);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(-14, getP().getEquationComponentD());
		}
		
		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().translate(1, 2, 3);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(1.1422619667529594, getP().getEquationComponentD());
		}
	
		@DisplayName("(Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().translate(createVector(1, 2, 3));
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(1.1422619667529594, getP().getEquationComponentD());
		}
	}

	@DisplayName("negate")
	@Nested
	public class Negate {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(-NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(-NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(-NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(-D, getP().getEquationComponentD());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			getP().negate();
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(D, getP().getEquationComponentD());
		}
	}

	@DisplayName("normalize")
	@Nested
	public class Normalize {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(getP(), getP().normalize());
		}
	}

	@DisplayName("getProjection")
	@Nested
	public class GetProjection {

		@DisplayName("(x,y,z) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523),
					getP().getProjection(0, 0, 0));
		}

		@DisplayName("(x,y,z) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814),
					getP().getProjection(-5, -7, 0));
		}

		@DisplayName("(x,y,z) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852),
					getP().getProjection(-1, 8, -2));
		}

		@DisplayName("(x,y,z) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523),
					getP().getProjection(0, 0, 0));
		}

		@DisplayName("(x,y,z) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814),
					getP().getProjection(-5, -7, 0));
		}

		@DisplayName("(x,y,z) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852),
					getP().getProjection(-1, 8, -2));
		}
	
		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523),
					getP().getProjection(createPoint(0, 0, 0)));
		}
		
		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814),
					getP().getProjection(createPoint(-5, -7, 0)));
			assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852),
					getP().getProjection(createPoint(-1, 8, -2)));
		}
		
		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523),
					getP().getProjection(createPoint(0, 0, 0)));
		}
		
		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814),
					getP().getProjection(createPoint(-5, -7, 0)));
		}
		
		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852),
					getP().getProjection(createPoint(-1, 8, -2)));
		}
	}

	@DisplayName("setPivot")
	@Nested
	public class SetPivot {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(0, 0, 0);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}

		@DisplayName("(double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(1, 2, 3);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(-2.8577380332470415, getP().getEquationComponentD());
		}

		@DisplayName("(double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(-5, 4, -1);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(2.857738033247041, getP().getEquationComponentD());
		}
	
		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(createPoint(0, 0, 0));
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(createPoint(1, 2, 3));
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(-2.8577380332470415, getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(createPoint(-5, 4, -1));
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(2.857738033247041, getP().getEquationComponentD());
		}
	}

	@DisplayName("getDistanceTo")
	@Nested
	public class GetDistanceTo {

		@DisplayName("(Plane3D) with general plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlane(A, B, C, D)));
		}

		@DisplayName("(Plane3D) with general plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		}

		@DisplayName("(Plane3D) with general plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		}

		@DisplayName("(Plane3D) with general plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(18., getP().getDistanceTo(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 18)));
		}

		@DisplayName("(Plane3D) with general plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(18., getP().getDistanceTo(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D + 18)));
		}

		@DisplayName("(Plane3D) with general plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlane(NORMAL_X - 1, NORMAL_Y, NORMAL_Z, D)));
		}

		@DisplayName("(Plane3D) with general plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlane(NORMAL_X, NORMAL_Y - 1, NORMAL_Z, D)));
		}

		@DisplayName("(Plane3D) with general plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z - 1, D)));
		}

		@DisplayName("(Plane3D) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(4.5, true)));
		}

		@DisplayName("(Plane3D) with XY plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(1.25, true)));
		}

		@DisplayName("(Plane3D) with XY plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-4.5, true)));
		}

		@DisplayName("(Plane3D) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-1.25, true)));
		}

		@DisplayName("(Plane3D) with XY plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(4.5, false)));
		}

		@DisplayName("(Plane3D) with XY plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(1.25, false)));
		}

		@DisplayName("(Plane3D) with XY plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-4.5, false)));
		}

		@DisplayName("(Plane3D) with XY plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-1.25, false)));
		}

		@DisplayName("(Plane3D) with XY plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(4.5, true)));
		}

		@DisplayName("(Plane3D) with XY plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(1.25, true)));
		}

		@DisplayName("(Plane3D) with XY plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-4.5, true)));
		}

		@DisplayName("(Plane3D) with XY plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-1.25, true)));
		}

		@DisplayName("(Plane3D) with XY plane #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(4.5, false)));
		}

		@DisplayName("(Plane3D) with XY plane #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(1.25, false)));
		}

		@DisplayName("(Plane3D) with XY plane #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-4.5, false)));
		}

		@DisplayName("(Plane3D) with XY plane #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void plane_xy_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneXY(-1.25, false)));
		}
	
		@DisplayName("(Plane3D) with XZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, true)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, true)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, true)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, true)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, false)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, false)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, false)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, false)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, true)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, true)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, true)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, true)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, false)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, false)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, false)));
		}
		
		@DisplayName("(Plane3D) with XZ plane #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, false)));
		}
	
		@DisplayName("(Plane3D) with YZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, true)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, true)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, true)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, true)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, false)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, false)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, false)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, false)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, true)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, true)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, true)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, true)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(4.5, false)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(1.25, false)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-4.5, false)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(createPlaneYZ(-1.25, false)));
		}

		@DisplayName("(x,y,z) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, getP().getDistanceTo(0, 0, 0));
		}

		@DisplayName("(x,y,z) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-2.94022093788, getP().getDistanceTo(-5, -7, 0));
		}

		@DisplayName("(x,y,z) #3		}\n"
				+ "\n"
				+ "		@DisplayName(\"(x,y,z) #\")\n"
				+ "		@ParameterizedTest(name = \"{index} => {0}\")\n"
				+ "		@EnumSource(CoordinateSystem3D.class)\n"
				+ "		public final void xyz_(CoordinateSystem3D cs) {\n"
				+ "			CoordinateSystem3D.setDefaultCoordinateSystem(cs);\n"
				+ "			getP().negate();\n"
				+ "")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.6329931618, getP().getDistanceTo(-1, 8, -2));
		}

		@DisplayName("(x,y,z) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(-4, getP().getDistanceTo(0, 0, 0));
		}

		@DisplayName("(x,y,z) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(2.940220937885, getP().getDistanceTo(-5, -7, 0));
		}

		@DisplayName("(x,y,z) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(-5.6329931618, getP().getDistanceTo(-1, 8, -2));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, getP().getDistanceTo(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-2.94022093788, getP().getDistanceTo(createPoint(-5, -7, 0)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.6329931618, getP().getDistanceTo(createPoint(-1, 8, -2)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(-4, getP().getDistanceTo(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(2.940220937885, getP().getDistanceTo(createPoint(-5, -7, 0)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(-5.6329931618, getP().getDistanceTo(createPoint(-1, 8, -2)));
		}

		@DisplayName("(a,b,c,d) with XZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., -4.5));
		}

		@DisplayName("(a,b,c,d) with XZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(0., 1., 0., -1.25));
		}

		@DisplayName("(a,b,c,d) with XZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., 4.5));
		}

		@DisplayName("(a,b,c,d) with XZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., -4.5));
		}

		@DisplayName("(a,b,c,d) with XZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(0., -1., 0., 1.25));
		}

		@DisplayName("(a,b,c,d) with XZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., 4.5));
		}

		@DisplayName("(a,b,c,d) with XZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., -4.5));
		}

		@DisplayName("(a,b,c,d) with XZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(0., 1., 0., -1.25));
		}

		@DisplayName("(a,b,c,d) with XZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., 4.5));
		}

		@DisplayName("(a,b,c,d) with XZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., -4.5));
		}

		@DisplayName("(a,b,c,d) with XZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(0., -1., 0., 1.25));
		}

		@DisplayName("(a,b,c,d) with XZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., 4.5));
		}

		@DisplayName("(a,b,c,d) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getP().getDistanceTo(0., 0., 1., -4.5));
		}

		@DisplayName("(a,b,c,d) with XY plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., 1., -1.25));
		}

		@DisplayName("(a,b,c,d) with XY plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getP().getDistanceTo(0., 0., 1., 4.5));
		}

		@DisplayName("(a,b,c,d) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getP().getDistanceTo(0., 0., -1., -4.5));
		}

		@DisplayName("(a,b,c,d) with XY plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., -1., 1.25));
		}

		@DisplayName("(a,b,c,d) with XY plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getP().getDistanceTo(0., 0., -1., 4.5));
		}

		@DisplayName("(a,b,c,d) with XY plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0, getP().getDistanceTo(0., 0., 1., -4.5));
		}

		@DisplayName("(a,b,c,d) with XY plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., 1., -1.25));
		}

		@DisplayName("(a,b,c,d) with XY plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0, getP().getDistanceTo(0., 0., 1., 4.5));
		}

		@DisplayName("(a,b,c,d) with XY plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0, getP().getDistanceTo(0., 0., -1., -4.5));
		}

		@DisplayName("(a,b,c,d) with XY plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., -1., 1.25));
		}

		@DisplayName("(a,b,c,d) with XY plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0, getP().getDistanceTo(0., 0., -1., 4.5));
		}

		@DisplayName("(a,b,c,d) with YZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -4.5));
		}

		@DisplayName("(a,b,c,d) with YZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -1.25));
		}

		@DisplayName("(a,b,c,d) with YZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., 4.5));
		}

		@DisplayName("(a,b,c,d) with YZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., -4.5));
		}

		@DisplayName("(a,b,c,d) with YZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 1.25));
		}

		@DisplayName("(a,b,c,d) with YZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 4.5));
		}

		@DisplayName("(a,b,c,d) with YZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -4.5));
		}

		@DisplayName("(a,b,c,d) with YZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -1.25));
		}

		@DisplayName("(a,b,c,d) with YZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., 4.5));
		}

		@DisplayName("(a,b,c,d) with YZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., -4.5));
		}

		@DisplayName("(a,b,c,d) with YZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 1.25));
		}

		@DisplayName("(a,b,c,d) with YZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 4.5));
		}

		@DisplayName("(a,b,c,d) with general plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		}

		@DisplayName("(a,b,c,d) with general plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D));
		}

		@DisplayName("(a,b,c,d) with general plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(18., getP().getDistanceTo(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 18));
		}

		@DisplayName("(a,b,c,d) with general plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(18., getP().getDistanceTo(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D + 18));
		}

		@DisplayName("(a,b,c,d) with general plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var x = NORMAL_X + 1;
			var y = NORMAL_Y;
			var z = NORMAL_Z;
			var l = Math.sqrt(x * x + y * y + z * z);
			assertEpsilonEquals(0., getP().getDistanceTo(x / l, y / l, z / l, D));
		}

		@DisplayName("(a,b,c,d) with general plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var x = NORMAL_X;
			var y = NORMAL_Y + 1;
			var z = NORMAL_Z;
			var l = Math.sqrt(x * x + y * y + z * z);
			assertEpsilonEquals(0., getP().getDistanceTo(x / l, y / l, z / l, D));
		}

		@DisplayName("(a,b,c,d) with general plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var x = NORMAL_X;
			var y = NORMAL_Y;
			var z = NORMAL_Z + 1;
			var l = Math.sqrt(x * x + y * y + z * z);
			assertEpsilonEquals(0., getP().getDistanceTo(x / l, y / l, z / l, D));
		}
	}

	@DisplayName("getIntersection")
	@Nested
	public class GetIntersection {

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(createSegment(0., 0., 0., -2., 2., -2.)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(createSegment(-2., 2., -2., 0., 0., 0.)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814));
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.));
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(0., 0., 0., -5, -7, 0));
			assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(-5, -7, 0, 0., 0., 0.));
			assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(createSegment(-9, -2., -1, -5, -7, 0)));
		}

		@DisplayName("(Segment3afp) #")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(createSegment(-5, -7, 0, -9, -2., -1)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814));
			assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(createSegment(0., 0., 0., -2., 2., -2.)));
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(createSegment(-2., 2., -2., 0., 0., 0.)));
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814));
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("(Segment3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.));
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("(Segment3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(0., 0., 0., -5, -7, 0));
			assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);
		}

		@DisplayName("(Segment3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(-5, -7, 0, 0., 0., 0.));
			assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);
		}

		@DisplayName("(Segment3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(createSegment(-9, -2., -1, -5, -7, 0)));
		}

		@DisplayName("(Segment3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(createSegment(-5, -7, 0, -9, -2., -1)));
		}

		@DisplayName("(Segment3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("(Segment3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814));
			assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);
		}

		@DisplayName("(a,b,c,d) with XZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 1., 0., -4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}

		@DisplayName("(a,b,c,d) with XZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 1., 0., 4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}

		@DisplayName("(a,b,c,d) with XZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., -1., 0., -4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}

		@DisplayName("(a,b,c,d) with XZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., -1., 0., 4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}

		@DisplayName("(a,b,c,d) with XZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 1., 0., -1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}

		@DisplayName("(a,b,c,d) with XZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., -1., 0., 1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}

		@DisplayName("(a,b,c,d) with XZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 1., 0., -4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}

		@DisplayName("(a,b,c,d) with XZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 1., 0., 4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}

		@DisplayName("(a,b,c,d) with XZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., -1., 0., -4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}

		@DisplayName("(a,b,c,d) with XZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., -1., 0., 4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}

		@DisplayName("(a,b,c,d) with XZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 1., 0., -1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}

		@DisplayName("(a,b,c,d) with XZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., -1., 0., 1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}
	
		@DisplayName("(a,b,c,d) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0., 1., -4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
			}
		}
		
		@DisplayName("(a,b,c,d) with XY plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0., 1., 4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
			}
		}
		
		@DisplayName("(a,b,c,d) with XY plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0., -1., -4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
			}
		}
		
		@DisplayName("(a,b,c,d) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0., -1., 4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
			}
		}
		
		@DisplayName("(a,b,c,d) with XY plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0., 1., -1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
			}
		}
		
		@DisplayName("(a,b,c,d) with XY plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0., -1., 1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
			}
		}
		
		@DisplayName("(a,b,c,d) with XY plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 0., 1., -4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
			}
		}
		
		@DisplayName("(a,b,c,d) with XY plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 0., 1., 4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
			}
		}
		
		@DisplayName("(a,b,c,d) with XY plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 0., -1., -4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
			}
		}
		
		@DisplayName("(a,b,c,d) with XY plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 0., -1., 4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
			}
		}
		
		@DisplayName("(a,b,c,d) with XY plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 0., 1., -1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
			}
		}
		
		@DisplayName("(a,b,c,d) with XY plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 0., -1., 1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
			}
		}
	
		@DisplayName("(a,b,c,d) with YZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(1., 0., 0., -4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(a,b,c,d) with YZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(1., 0., 0., 4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(a,b,c,d) with YZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(-1., 0., 0., -4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(a,b,c,d) with YZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(-1., 0., 0., 4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(a,b,c,d) with YZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(1., 0., 0., -1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(a,b,c,d) with YZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(-1., 0., 0., 1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(a,b,c,d) with YZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(1., 0., 0., -4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(a,b,c,d) with YZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(1., 0., 0., 4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(a,b,c,d) with YZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(-1., 0., 0., -4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(a,b,c,d) with YZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(-1., 0., 0., 4.5);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(a,b,c,d) with YZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(1., 0., 0., -1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(a,b,c,d) with YZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(-1., 0., 0., 1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
	
		@DisplayName("(a,b,c,d) with general plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			assertNull(s);
		}
		
		@DisplayName("(a,b,c,d) with general plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D);
			assertNull(s);
		}
		
		@DisplayName("(a,b,c,d) with general plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1);
			assertNull(s);
		}
		
		@DisplayName("(a,b,c,d) with general plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var vx = 1.;
			var vy = 2.;
			var vz = 0.;
			var vl = Math.sqrt(vx * vx + vy * vy + vz * vz);
			var s = getP().getIntersection(vx / vl, vy / vl, vz / vl, 5.);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.60193386084, -4.28920301333, -0.304888236119), s.getP1());
			assertEpsilonColinear(createVector(0.53452248382, -0.26726124191, -0.80178372574), s.getDirection());
		}
		
		@DisplayName("(a,b,c,d) with general plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			assertNull(s);
		}
		
		@DisplayName("(a,b,c,d) with general plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D);
			assertNull(s);
		}
		
		@DisplayName("(a,b,c,d) with general plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1);
			assertNull(s);
		}
		
		@DisplayName("(a,b,c,d) with general plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var vx = 1.;
			var vy = 2.;
			var vz = 0.;
			var vl = Math.sqrt(vx * vx + vy * vy + vz * vz);
			var s = getP().getIntersection(vx / vl, vy / vl, vz / vl, 5.);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.60193386084, -4.28920301333, -0.304888236119), s.getP1());
			assertEpsilonColinear(createVector(-0.53452248382, 0.26726124191, 0.80178372574), s.getDirection());
		}
	
		@DisplayName("(Plane3D) with XZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneXZ(4.5, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneXZ(-4.5, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneXZ(-4.5, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneXZ(4.5, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneXZ(1.25, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneXZ(1.25, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneXZ(4.5, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneXZ(-4.5, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneXZ(-4.5, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneXZ(4.5, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneXZ(1.25, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneXZ(1.25, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
			assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
		}
	
		@DisplayName("(Plane3D) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneXY(4.5, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
			}
		}
		
		@DisplayName("(Plane3D) with XY plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneXY(-4.5, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
			}
		}
		
		@DisplayName("(Plane3D) with XY plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneXY(-4.5, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
			}
		}
		
		@DisplayName("(Plane3D) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneXY(4.5, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
			}
		}
		
		@DisplayName("(Plane3D) with XY plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneXY(1.25, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
			}
		}
		
		@DisplayName("(Plane3D) with XY plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneXY(1.25, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
			}
		}
		
		@DisplayName("(Plane3D) with XY plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneXY(4.5, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
			}
		}
		
		@DisplayName("(Plane3D) with XY plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneXY(-4.5, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
			}
		}
		
		@DisplayName("(Plane3D) with XY plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneXY(-4.5, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
			}
		}
		
		@DisplayName("(Plane3D) with XY plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneXY(4.5, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
			}
		}
		
		@DisplayName("(Plane3D) with XY plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneXY(1.25, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
			}
		}
		
		@DisplayName("(Plane3D) with XY plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneXY(1.25, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
			if (cs.isLeftHanded()) {
				assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
			} else {
				assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
			}
		}
	
		@DisplayName("(Plane3D) with YZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneYZ(4.5, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with YZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneYZ(-4.5, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with YZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneYZ(-4.5, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with YZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneYZ(4.5, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with YZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneYZ(1.25, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with YZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlaneYZ(1.25, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with YZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneYZ(4.5, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with YZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneYZ(-4.5, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with YZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneYZ(-4.5, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with YZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneYZ(4.5, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with YZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneYZ(1.25, true));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with YZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlaneYZ(1.25, false));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
			assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
		}
	
		@DisplayName("(Plane3D) with general plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlane(A, B, C, D));
			assertNull(s);
		}
		
		@DisplayName("(Plane3D) with general plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D));
			assertNull(s);
		}
		
		@DisplayName("(Plane3D) with general plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D));
			assertNull(s);
		}
		
		@DisplayName("(Plane3D) with general plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1));
			assertNull(s);
		}
		
		@DisplayName("(Plane3D) with general plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(createPlane(1., 2., 0., 5.));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.6019338608427405, -4.2892030133281045, -0.30488823611912585), s.getP1());
			assertEpsilonColinear(createVector(-0.82, 0.41, 1.22), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with general plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlane(A, B, C, D));
			assertNull(s);
		}
		
		@DisplayName("(Plane3D) with general plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D));
			assertNull(s);
		}
		
		@DisplayName("(Plane3D) with general plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D));
			assertNull(s);
		}
		
		@DisplayName("(Plane3D) with general plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1));
			assertNull(s);
		}
		
		@DisplayName("(Plane3D) with general plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(createPlane(1., 2., 0., 5.));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-2.6019338608427405, -4.2892030133281045, -0.30488823611912585), s.getP1());
			assertEpsilonColinear(createVector(-0.82, 0.41, 1.22), s.getDirection());
		}
	}

	@DisplayName("rotate")
	@Nested
	public class Rotate {

		@DisplayName("(x,y,z,angle) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().rotate(-1, 1, -1, Math.PI / 3.);
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(2.44444444444, getP().getEquationComponentD());
		}
	
		@DisplayName("(Vector3D,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void vectordouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().rotate(createVector(-1, 1, -1), Math.PI / 3.);
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(2.44444444444, getP().getEquationComponentD());
		}
	
		@DisplayName("(Vector3D,double,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vectordoublepoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Null Pivot
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			getP().rotate(createVector(-1, 1, -1), Math.PI / 3., null);
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(2.44444444444, getP().getEquationComponentD());
		}
		
		@DisplayName("(Vector3D,double,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vectordoublepoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// getPivot()
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			var pv = getP().getPivot().clone();
			getP().rotate(createVector(-1, 1, -1), Math.PI / 3., pv);
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(2.44444444444, getP().getEquationComponentD());
		}
		
		@DisplayName("(Vector3D,double,Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vectordoublepoint_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotate around origin (0,0,0)
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			getP().setPivotToDefault();
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			getP().rotate(createVector(-1, 1, -1), Math.PI / 3., createPoint(0,0,0));
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(4., getP().getEquationComponentD());
		}
		
		@DisplayName("(Vector3D,double,Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vectordoublepoint_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotate around (1,-2,3)
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			getP().rotate(createVector(-1, 1, -1), Math.PI / 3., createPoint(1,-2,3));
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(4.8164965809, getP().getEquationComponentD());
		}
	
		@DisplayName("(Quaternion) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternion_1(CoordinateSystem3D cs) {
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
	
		@DisplayName("(Quaternion,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternionpoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Null Pivot
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			getP().rotate(q, null);
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(2.44444444444, getP().getEquationComponentD());
		}
		
		@DisplayName("(Quaternion,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternionpoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// getPivot()
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			var pv = getP().getPivot().clone();
			getP().rotate(q, pv);
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(2.44444444444, getP().getEquationComponentD());
		}
		
		@DisplayName("(Quaternion,Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternionpoint_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotate around origin (0,0,0)
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			getP().setPivotToDefault();
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			getP().rotate(q, createPoint(0,0,0));
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(4., getP().getEquationComponentD());
		}
		
		@DisplayName("(Quaternion,Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternionpoint_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotate around (1,-2,3)
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			getP().rotate(q, createPoint(1,-2,3));
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(4.8164965809, getP().getEquationComponentD());
		}
	}

	@DisplayName("set")
	@Nested
	public class Set {

		@DisplayName("(double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_1(CoordinateSystem3D cs) {
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
		}

		@DisplayName("(double,double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(1, 0, 0, -4);
			assertEpsilonEquals(1, getP().getEquationComponentA());
			assertEpsilonEquals(0, getP().getEquationComponentB());
			assertEpsilonEquals(0, getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
		}

		@DisplayName("(double,double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(0, 1, 0, -4);
			assertEpsilonEquals(0, getP().getEquationComponentA());
			assertEpsilonEquals(1, getP().getEquationComponentB());
			assertEpsilonEquals(0, getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
		}

		@DisplayName("(double,double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(0, 0, 1, -4);
			assertEpsilonEquals(0, getP().getEquationComponentA());
			assertEpsilonEquals(0, getP().getEquationComponentB());
			assertEpsilonEquals(1, getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
		}
	
		@DisplayName("(x1,y1,z1, x2,y2,z2, x3,y3,z3) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyzxyzxyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 1: Plane y = 0 (reference orientation)
			getP().set(0, 0, 0, 1, 0, 0, 0, 0, 1);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(u, getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(x1,y1,z1, x2,y2,z2, x3,y3,z3) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyzxyzxyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 2: Same plane y = 0 with different point order (normal may flip)
			getP().set(0, 0, 0, 0, 0, 1, 1, 0, 0);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(-u, getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(x1,y1,z1, x2,y2,z2, x3,y3,z3) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyzxyzxyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 3: Plane x = 0
			getP().set(0, 0, 0, 0, 1, 0, 0, 0, 1);
			assertEpsilonEquals(-u, getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(x1,y1,z1, x2,y2,z2, x3,y3,z3) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyzxyzxyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 4: Plane z = 0
			getP().set(0, 0, 0, 1, 0, 0, 0, 1, 0);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-u, getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(x1,y1,z1, x2,y2,z2, x3,y3,z3) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyzxyzxyz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 5: Translated plane y = 1.25  -> 0*x -1*y +0*z +1.25 = 0
			getP().set(0, 1.25, 0, 1, 1.25, 0, 0, 1.25, 1);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(u, getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-u * 1.25, getP().getEquationComponentD());
		}
		
		@DisplayName("(x1,y1,z1, x2,y2,z2, x3,y3,z3) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyzxyzxyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
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
		}
		
		@DisplayName("(x1,y1,z1, x2,y2,z2, x3,y3,z3) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyzxyzxyz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
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
		}
		
		@DisplayName("(x1,y1,z1, x2,y2,z2, x3,y3,z3) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyzxyzxyz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// Point not in plane should not satisfy equation
			final Point3D out = createPoint(10, 10, 10);
			final double evalOut = getP().getEquationComponentA() * out.getX()
					+ getP().getEquationComponentB() * out.getY()
					+ getP().getEquationComponentC() * out.getZ()
					+ getP().getEquationComponentD();
			assertNotEpsilonEquals(0., evalOut);
		}
	
		@DisplayName("(Point3D,Point3D,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 1: Plane y = 0 (reference orientation)
			getP().set(createPoint(0, 0, 0), createPoint(1, 0, 0), createPoint(0, 0, 1));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(u, getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Point3D,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 2: Same plane y = 0 with different point order (normal may flip)
			getP().set(createPoint(0, 0, 0), createPoint(0, 0, 1), createPoint(1, 0, 0));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(-u, getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Point3D,Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 3: Plane x = 0
			getP().set(createPoint(0, 0, 0), createPoint(0, 1, 0), createPoint(0, 0, 1));
			assertEpsilonEquals(-u, getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Point3D,Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 4: Plane z = 0
			getP().set(createPoint(0, 0, 0), createPoint(1, 0, 0), createPoint(0, 1, 0));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-u, getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Point3D,Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 5: Translated plane y = 1.25  -> 0*x -1*y +0*z +1.25 = 0
			getP().set(createPoint(0, 1.25, 0), createPoint(1, 1.25, 0), createPoint(0, 1.25, 1));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(u, getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-u * 1.25, getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Point3D,Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
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
		}
		
		@DisplayName("(Point3D,Point3D,Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
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
	
		@DisplayName("(Plane3D) with XZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPlaneXZ(-4, true));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(1., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4., getP().getEquationComponentD());
		}
		
		@DisplayName("(Plane3D) with XZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPlaneXZ(4, true));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(1., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-4., getP().getEquationComponentD());
		}
		
		@DisplayName("(Plane3D) with XZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPlaneXZ(4, false));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(-1., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4., getP().getEquationComponentD());
		}
		
		@DisplayName("(Plane3D) with XZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPlaneXZ(-4, false));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(-1., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-4., getP().getEquationComponentD());
		}
	
		@DisplayName("(Plane3D) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPlaneXY(-4, true));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(4, getP().getEquationComponentD());
		}
		
		@DisplayName("(Plane3D) with XY plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPlaneXY(4, true));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
		}
		
		@DisplayName("(Plane3D) with XY plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPlaneXY(4, false));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(4, getP().getEquationComponentD());
		}
		
		@DisplayName("(Plane3D) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPlaneXY(-4, false));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
		}
	
		@DisplayName("(Plane3D) with YZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPlaneYZ(-4, true));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4., getP().getEquationComponentD());
		}
		
		@DisplayName("(Plane3D) with YZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPlaneYZ(4, true));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-4., getP().getEquationComponentD());
		}
		
		@DisplayName("(Plane3D) with YZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPlaneYZ(4, false));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4., getP().getEquationComponentD());
		}
		
		@DisplayName("(Plane3D) with YZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPlaneYZ(-4, false));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-4., getP().getEquationComponentD());
		}
	
		@DisplayName("(Plane3D) with general plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_1(CoordinateSystem3D cs) {
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
	
		@DisplayName("(Point3D,Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 1: Plane y = 0 (reference orientation)
			getP().set(createPoint(0, 0, 0), createVector(0, u, 0));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(u, getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Vector3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 2: Same plane y = 0 with different point order (normal may flip)
			getP().set(createPoint(0, 0, 0), createVector(0, -u, 0));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(-u, getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Vector3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 3: Plane x = 0
			getP().set(createPoint(0, 0, 0), createVector(-u, 0, 0));
			assertEpsilonEquals(-u, getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Vector3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 4: Plane z = 0
			getP().set(createPoint(0, 0, 0), createVector(0, 0, -u));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-u, getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Vector3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 5: Translated plane y = 1.25  -> 0*x -1*y +0*z +1.25 = 0
			getP().set(createPoint(0, 1.25, 0), createVector(0, u, 0));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(u, getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-u * 1.25, getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Vector3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 6: Oblique plane x + y + z - 3 = 0 (normal can be scaled internally)
			getP().set(createPoint(3, 0, 0), createVector(-u, 0, 0));
			assertEpsilonEquals(-u, getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(u * 3, getP().getEquationComponentD());
		}
	
		@DisplayName("(Point3D,Vector3D,Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 1: Plane y = 0 (reference orientation)
			getP().set(createPoint(0, 0, 0), createVector(1, 0, 0), createVector(0, 0, 1));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(u, getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Vector3D,Vector3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 2: Same plane y = 0 with different point order (normal may flip)
			getP().set(createPoint(0, 0, 0), createVector(0, 0, 1), createVector(1, 0, 0));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(-u, getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Vector3D,Vector3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 3: Plane x = 0
			getP().set(createPoint(0, 0, 0), createVector(0, 1, 0), createVector(0, 0, 1));
			assertEpsilonEquals(-u, getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Vector3D,Vector3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 4: Plane z = 0
			getP().set(createPoint(0, 0, 0), createVector(1, 0, 0), createVector(0, 1, 0));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-u, getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Vector3D,Vector3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 5: Translated plane y = 1.25  -> 0*x -1*y +0*z +1.25 = 0
			getP().set(createPoint(0, 1.25, 0), createVector(1, 0, 0), createVector(0, 0, 1));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(u, getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-u * 1.25, getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Vector3D,Vector3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
			// --- Case 6: Oblique plane x + y + z - 3 = 0 (normal can be scaled internally)
			getP().set(createPoint(3, 0, 0), createVector(0, 3, 0), createVector(0, 0, 3));
			assertEpsilonEquals(-u, getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(u * 3, getP().getEquationComponentD());
		}
		
		@DisplayName("(Point3D,Vector3D,Vector3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var u = CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded() ? 1. : -1.;
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
	}

	@DisplayName("transform")
	@Nested
	public class Transform {

		@DisplayName("(Transform3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transform_1(CoordinateSystem3D cs) {
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
		}

		@DisplayName("(Transform3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transform_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			var transform = new Transform3D();
			transform.makeTranslationMatrix(6, -3, 1);
			
			getP().transform(transform);
	
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(-0.0824829046, getP().getEquationComponentD());
		}

		@DisplayName("(Transform3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transform_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation Translation
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			var transform = new Transform3D();
			transform.makeRotationMatrix(q);
			transform.setTranslation(6, -3, 1);
			
			getP().transform(transform);
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(-3.4071143855375916, getP().getEquationComponentD());
		}
	
		@DisplayName("(Transform3D,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_1(CoordinateSystem3D cs) {
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
		}
		
		@DisplayName("(Transform3D,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Null Pivot, Translation
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			getP().setPivotToDefault();
			var transform = new Transform3D();
			transform.makeTranslationMatrix(6, -3, 1);
			
			getP().transform(transform, null);
	
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(-0.0824829046, getP().getEquationComponentD());
		}
		
		@DisplayName("(Transform3D,Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Null Pivot, Rotation Translation
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			getP().setPivotToDefault();
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			var transform = new Transform3D();
			transform.makeRotationMatrix(q);
			transform.setTranslation(6, -3, 1);
			
			getP().transform(transform, null);
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(-3.4071143855375916, getP().getEquationComponentD());
		}
		
		@DisplayName("(Transform3D,Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Current Pivot, Rotation
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			getP().setPivotToDefault();
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			var transform = new Transform3D();
			transform.makeRotationMatrix(q);
			
			getP().transform(transform, getP().getPivot().clone());
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(2.4444444444, getP().getEquationComponentD());
		}
		
		@DisplayName("(Transform3D,Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Current Pivot, Translation
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			getP().setPivotToDefault();
			var transform = new Transform3D();
			transform.makeTranslationMatrix(6, -3, 1);
			
			getP().transform(transform, getP().getPivot().clone());
	
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(-0.0824829046, getP().getEquationComponentD());
		}
		
		@DisplayName("(Transform3D,Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Current Pivot, Rotation Translation
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			getP().setPivotToDefault();
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			var transform = new Transform3D();
			transform.makeRotationMatrix(q);
			transform.setTranslation(6, -3, 1);
			
			getP().transform(transform, getP().getPivot().clone());
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(-3.4071143855375916, getP().getEquationComponentD());
		}
		
		@DisplayName("(Transform3D,Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Pivot around (0,0,0), Rotation
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			getP().setPivotToDefault();
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			var transform = new Transform3D();
			transform.makeRotationMatrix(q);
	
			getP().transform(transform, createPoint(0,0,0));
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(4, getP().getEquationComponentD());
		}
		
		@DisplayName("(Transform3D,Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Pivot around (0,0,0), Translation
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			getP().setPivotToDefault();
	
			var transform = new Transform3D();
			transform.makeTranslationMatrix(6, -3, 1);
	
			getP().transform(transform, createPoint(0,0,0));
	
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(-0.0824829046, getP().getEquationComponentD());
		}
		
		@DisplayName("(Transform3D,Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Pivot around (0,0,0), Rotation Translation
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			getP().setPivotToDefault();
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			var transform = new Transform3D();
			transform.makeRotationMatrix(q);
			transform.setTranslation(6, -3, 1);
			
			getP().transform(transform, createPoint(0, 0, 0));
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(-1.85155883, getP().getEquationComponentD());
		}
		
		@DisplayName("(Transform3D,Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Pivot (1,-2,3), Rotation
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			getP().setPivotToDefault();
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			var transform = new Transform3D();
			transform.makeRotationMatrix(q);
			
			getP().transform(transform, createPoint(1,-2,3));
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(4.8164965809, getP().getEquationComponentD());
		}
		
		@DisplayName("(Transform3D,Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Pivot (1,-2,3), Translation
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			getP().setPivotToDefault();
			var transform = new Transform3D();
			transform.makeTranslationMatrix(6, -3, 1);
			
			getP().transform(transform, createPoint(1, -2, 3));
	
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(-0.0824829046, getP().getEquationComponentD());
		}
		
		@DisplayName("(Transform3D,Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Pivot (1,-2,3), Rotation Translation
			getP().set(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
			getP().setPivotToDefault();
			var q = createQuaternion(1, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			var transform = new Transform3D();
			transform.makeRotationMatrix(q);
			transform.setTranslation(6, -3, 1);
			
			getP().transform(transform, createPoint(1, -2, 3));
	
			assertEpsilonEquals(0.9525793444, getP().getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, getP().getEquationComponentB());
			assertEpsilonEquals(-0.272165527, getP().getEquationComponentC());
			assertEpsilonEquals(-1.0350622491, getP().getEquationComponentD());
		}
	}

	@DisplayName("calculatesPlaneAlignedBoxDistance")
	@Nested
	public class CalculatesPlaneAlignedBoxDistance {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4., Plane3afp.calculatesPlaneAlignedBoxDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., 2., 2., 2.));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Plane3afp.calculatesPlaneAlignedBoxDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5., -2., -1., -3., 0., 1.));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Plane3afp.calculatesPlaneAlignedBoxDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -6., -3., -1., -4., -1., 1.));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-1.307227776, Plane3afp.calculatesPlaneAlignedBoxDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -8., -3., -2., -6., -1., 0.));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-4., Plane3afp.calculatesPlaneAlignedBoxDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, 0., 0., 0., 2., 2., 2.));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Plane3afp.calculatesPlaneAlignedBoxDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -5., -2., -1., -3., 0., 1.));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Plane3afp.calculatesPlaneAlignedBoxDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -6., -3., -1., -4., -1., 1.));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.307227776, Plane3afp.calculatesPlaneAlignedBoxDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -8., -3., -2., -6., -1., 0.));
		}
	}

	@DisplayName("calculatesPlanePointDistance")
	@Nested
	public class CalculatesPlanePointDistance {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, Plane3afp.calculatesPlanePointDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-4, Plane3afp.calculatesPlanePointDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, 0, 0, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-2.94022093788, Plane3afp.calculatesPlanePointDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.940220937885, Plane3afp.calculatesPlanePointDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -5, -7, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.6329931618, Plane3afp.calculatesPlanePointDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -1, 8, -2));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-5.6329931618, Plane3afp.calculatesPlanePointDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -1, 8, -2));
		}
	}

	@DisplayName("calculatesPlaneSphereDistance")
	@Nested
	public class CalculatesPlaneSphereDistance {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., 2.));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -1.6329931618554523, -0.8164965809277261, -0.8164965809277261, 2.));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.45, -1.22, -1.22, 2.));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -4.08, -2.04, -2.04, 2.));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -4.8989794856, -2.4494897428, -2.4494897428, 2.));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-1., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5.7154760665, -2.8577380332, -2.8577380332, 2.));
		}
	}

	@DisplayName("calculatesPlanePlaneDistance")
	@Nested
	public class CalculatesPlanePlaneDistance {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(18., Plane3afp.calculatesPlanePlaneDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 18, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(18., Plane3afp.calculatesPlanePlaneDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D + 18, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var x = NORMAL_X - 1.;
			var y = NORMAL_Y;
			var z = NORMAL_Z;
			var l = Math.sqrt(x * x + y * y + z * z);
			assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(x / l, y / l, z / l, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var x = NORMAL_X;
			var y = NORMAL_Y - 1.;
			var z = NORMAL_Z;
			var l = Math.sqrt(x * x + y * y + z * z);
			assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(x / l, y / l, z / l, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var x = NORMAL_X;
			var y = NORMAL_Y;
			var z = NORMAL_Z - 1.;
			var l = Math.sqrt(x * x + y * y + z * z);
			assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(x / l, y / l, z / l, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		}
	}

	@DisplayName("classifies")
	@Nested
	public class Classifies {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createPoint(-8, -4, -4)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPoint(-8, -4, -4)));
		}

		@DisplayName("(Box3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(0., 0., 0., 2., 2., 2.)));
		}

		@DisplayName("(Box3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(-5., -2., -1., -3., 0., 1.)));
		}

		@DisplayName("(Box3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(-6., -3., -1., -4., -1., 1.)));
		}

		@DisplayName("(Box3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(-8., -3., -2., -6., -1., 0.)));
		}

		@DisplayName("(Box3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(0., 0., 0., 2., 2., 2.)));
		}

		@DisplayName("(Box3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(-5., -2., -1., -3., 0., 1.)));
		}

		@DisplayName("(Box3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(-6., -3., -1., -4., -1., 1.)));
		}

		@DisplayName("(Box3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(-8., -3., -2., -6., -1., 0.)));
		}

		@DisplayName("(x,y,z) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 0));
		}

		@DisplayName("(x,y,z) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		}

		@DisplayName("(x,y,z) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(-8, -4, -4));
		}

		@DisplayName("(x,y,z) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 0));
		}

		@DisplayName("(x,y,z) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		}

		@DisplayName("(x,y,z) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(-8, -4, -4));
		}

		@DisplayName("(x,y,z,radius) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0., 0., 0., 2.));
		}

		@DisplayName("(x,y,z,radius) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(-2.45, -1.22, -1.22, 2.));
		}

		@DisplayName("(x,y,z,radius) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(-3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		}

		@DisplayName("(x,y,z,radius) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(-4.08, -2.04, -2.04, 2.));
		}

		@DisplayName("(x,y,z,radius) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(-5.7154760665, -2.8577380332, -2.8577380332, 2.));
		}

		@DisplayName("(x,y,z,radius) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(0., 0., 0., 2.));
		}

		@DisplayName("(x,y,z,radius) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(-2.45, -1.22, -1.22, 2.));
		}

		@DisplayName("(x,y,z,radius) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(-3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		}

		@DisplayName("(x,y,z,radius) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(-4.08, -2.04, -2.04, 2.));
		}

		@DisplayName("(x,y,z,radius) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(-5.7154760665, -2.8577380332, -2.8577380332, 2.));
		}

		@DisplayName("(x1,y1,z1, x2,y2,z2) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0., 0., 0., 2., 2., 2.));
		}

		@DisplayName("(x1,y1,z1, x2,y2,z2) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(-5., -2., -1., -3., 0., 1.));
		}

		@DisplayName("(x1,y1,z1, x2,y2,z2) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(-6., -3., -1., -4., -1., 1.));
		}

		@DisplayName("(x1,y1,z1, x2,y2,z2) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(-8., -3., -2., -6., -1., 0.));
		}

		@DisplayName("(x1,y1,z1, x2,y2,z2) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(0., 0., 0., 2., 2., 2.));
		}

		@DisplayName("(x1,y1,z1, x2,y2,z2) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(-5., -2., -1., -3., 0., 1.));
		}

		@DisplayName("(x1,y1,z1, x2,y2,z2) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(-6., -3., -1., -4., -1., 1.));
		}

		@DisplayName("(x1,y1,z1, x2,y2,z2) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(-8., -3., -2., -6., -1., 0.));
		}

		@DisplayName("(Plane3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPlane(1., -3., 4., -4.)));
		}

		@DisplayName("(Plane3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPlane(-1., 3., -4., 4.)));
		}

		@DisplayName("(Plane3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		}

		@DisplayName("(Plane3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 2)));
		}

		@DisplayName("(Plane3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 4)));
		}

		@DisplayName("(Plane3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D - 2)));
		}

		@DisplayName("(Plane3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D - 4)));
		}

		@DisplayName("(Plane3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPlane(1., -3., 4., -4.)));
		}

		@DisplayName("(Plane3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPlane(-1., 3., -4., 4.)));
		}

		@DisplayName("(Plane3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		}

		@DisplayName("(Plane3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 2)));
		}

		@DisplayName("(Plane3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 4)));
		}

		@DisplayName("(Plane3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D - 2)));
		}

		@DisplayName("(Plane3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D - 4)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(0., 0., 0., -2., 2., -2.)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(-2., 2., -2., 0., 0., 0.)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(0., 0., 0., -5, -7, 0)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-5, -7, 0, 0., 0., 0.)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(-9, -2., -1, -5, -7, 0)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(-5, -7, 0, -9, -2., -1)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(0., 0., 0., -2., 2., -2.)));
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(-2., 2., -2., 0., 0., 0.)));
		}

		@DisplayName("(Segment3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		}

		@DisplayName("(Segment3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));
		}

		@DisplayName("(Segment3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(0., 0., 0., -5, -7, 0)));
		}

		@DisplayName("(Segment3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-5, -7, 0, 0., 0., 0.)));
		}

		@DisplayName("(Segment3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(-9, -2., -1, -5, -7, 0)));
		}

		@DisplayName("(Segment3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(-5, -7, 0, -9, -2., -1)));
		}

		@DisplayName("(Segment3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		}

		@DisplayName("(Segment3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		}

		@DisplayName("(Shepre3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSphere(0., 0., 0., 2.)));
		}

		@DisplayName("(Shepre3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(-2.45, -1.22, -1.22, 2.)));
		}

		@DisplayName("(Shepre3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		}

		@DisplayName("(Shepre3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(-4.08, -2.04, -2.04, 2.)));
		}

		@DisplayName("(Shepre3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSphere(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
		}

		@DisplayName("(Shepre3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSphere(0., 0., 0., 2.)));
		}

		@DisplayName("(Shepre3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(-2.45, -1.22, -1.22, 2.)));
		}

		@DisplayName("(Shepre3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		}

		@DisplayName("(Shepre3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(-4.08, -2.04, -2.04, 2.)));
		}

		@DisplayName("(Shepre3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSphere(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
		}
	}

	@DisplayName("classifiesPlaneAlignedBox")
	@Nested
	public class ClassifiesPlaneAlignedBox {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneAlignedBox(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., 2., 2., 2.));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneAlignedBox(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5., -2., -1., -3., 0., 1.));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneAlignedBox(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -6., -3., -1., -4., -1., 1.));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneAlignedBox(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -8., -3., -2., -6., -1., 0.));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneAlignedBox(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, 0., 0., 0., 2., 2., 2.));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneAlignedBox(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -5., -2., -1., -3., 0., 1.));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneAlignedBox(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -6., -3., -1., -4., -1., 1.));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneAlignedBox(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -8., -3., -2., -6., -1., 0.));
		}
	}

	@DisplayName("classifiesPlanePlane")
	@Nested
	public class ClassifiesPlanePlane {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 1., -3., 4., -4.));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -1., 3., -4., 4.));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlanePlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D + 2));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlanePlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D + 4));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlanePlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D - 2));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlanePlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D - 4));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, 1., -3., 4., -4.));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -1., 3., -4., 4.));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlanePlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, NORMAL_X, NORMAL_Y, NORMAL_Z, D + 2));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlanePlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, NORMAL_X, NORMAL_Y, NORMAL_Z, D + 4));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlanePlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, NORMAL_X, NORMAL_Y, NORMAL_Z, D - 2));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlanePlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, NORMAL_X, NORMAL_Y, NORMAL_Z, D - 4));
		}
	}

	@DisplayName("classifiesPlanePoint")
	@Nested
	public class ClassifiesPlanePoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlanePoint(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePoint(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlanePoint(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -8, -4, -4));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlanePoint(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, 0, 0, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePoint(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlanePoint(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -8, -4, -4));
		}
	}

	@DisplayName("classifiesPlaneSegment")
	@Nested
	public class ClassifiesPlaneSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2., 2., -2.));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2., 2., -2., 0., 0., 0.));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -5, -7, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, 0., 0., 0.));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -9, -2., -1, -5, -7, 0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, -9, -2., -1));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		}
	}

	@DisplayName("classifiesPlaneSphere")
	@Nested
	public class ClassifiesPlaneSphere {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., 2.));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -1.6329931618554523, -0.8164965809277261, -0.8164965809277261, 2.));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.45, -1.22, -1.22, 2.));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -4.08, -2.04, -2.04, 2.));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -4.8989794856, -2.4494897428, -2.4494897428, 2.));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5.7154760665, -2.8577380332, -2.8577380332, 2.));
		}
	}

	@DisplayName("findsPlanePlaneIntersection")
	@Nested
	public class FindsPlanePlaneIntersection {

		private Point3D p1;
		private Point3D p2;
		private Point3D p;
		private Vector3D v;

		@BeforeEach
		public void setUp() {
			p1 = createPoint(0, 0, 0);
			p2 = createPoint(0, 0, 0);
			p = createPoint(0, 0, 0);
			v = createVector(0, 0, 0);
		}
		
		private void assertReceiverInvoked(Point3D<?, ?, ?> p, Vector3D<?, ?, ?> v, Point3D<?, ?, ?> a1, Point3D<?, ?, ?> a2) {
			assertEpsilonEquals(p, a1);
			var v0 = a2.operator_minus(a1);
			assertEpsilonColinear(v, v0);
		}
	
		private void assertReceiverInvoked(Point3D<?, ?, ?> p, Vector3D<?, ?, ?> v, Point3D<?, ?, ?> a1, Vector3D<?, ?, ?> a2) {
			assertEpsilonEquals(p, a1);
			assertEpsilonColinear(v, a2);
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

		private void assertReceiverInvoked(Point3D<?, ?, ?> p, Vector3D<?, ?, ?> v, Segment3afp<?, ?, ?, ?, ?, ?, ?> s) {
			assertEpsilonEquals(p, s.getP1());
			var v0 = s.getP2().operator_minus(s.getP1());
			assertEpsilonColinear(v, v0);
		}

		@DisplayName("(..., Point3D, Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0.707106781373, 0., -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0.707106781373, 0., 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0., 0.707106781373, 0.707106781373, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0., 0.707106781373, 0.707106781373, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0, 0.707106781373, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0, 0.707106781373, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.577350269, 0.577350269, 0.577350269, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(1., -1., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.577350269, 0.577350269, 0.577350269, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(1., -1., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0.707106781373, 0, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0.707106781373, 0., 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0., 0.707106781373, 0.707106781373, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0., 0.707106781373, 0.707106781373, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0, 0.707106781373, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0, 0.707106781373, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.577350269, 0.577350269, 0.577350269, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.577350269, 0.577350269, 0.577350269, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0.707106781373, 0, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(-3.26598632371, 5.0337532762, -8.2997395999),
					createVector(0.28867513467, -0.28867513467, -0.28867513467), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0.707106781373, 0., 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-3.26598632371, 1.49821937121, -4.764205695),
					createVector(.28867513467, -.28867513467, -.28867513467), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0., 0.707106781373, 0.707106781373, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(-5.7828629618, .88388347625, .88388347625),
					createVector(0., .57735026934, -.57735026934), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0., 0.707106781373, 0.707106781373, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-4.01509600932, -.88388347625, -.88388347625),
					createVector(0., .57735026934, -.57735026934), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0, 0.707106781373, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(-3.26598632371, -8.2997395999, 5.0337532762),
					createVector(-.28867513467, .28867513467, .28867513467), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0, 0.707106781373, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-3.2659863237109032, -4.76420569492157, 1.4982193712106662),
					createVector(-.28867513467, .28867513467, .28867513467), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.577350269, 0.577350269, 0.577350269, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(-11.963022481304906, 7.064042995738549, 7.064042995738549),
					createVector(0.0, 0.23570226031810146, -0.23570226031810146), p1, p2);
		}

		@DisplayName("(..., Point3D, Point3D) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpoint_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.577350269, 0.577350269, 0.577350269, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-7.632895460960519, 2.733915975394163, 2.733915975394163),
					createVector(0.0, 0.23570226031810146, -0.23570226031810146), p1, p2);
		}

		@DisplayName("(..., Point3D, Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0.707106781373, 0., -1.25, p, v));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0.707106781373, 0., 1.25, p, v));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0., 0.707106781373, 0.707106781373, -1.25, p, v));
			assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0., 0.707106781373, 0.707106781373, 1.25, p, v));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0, 0.707106781373, -1.25, p, v));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0, 0.707106781373, 1.25, p, v));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.577350269, 0.577350269, 0.577350269, -1.25, p, v));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(1., -1., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.577350269, 0.577350269, 0.577350269, 1.25, p, v));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(1., -1., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0.707106781373, 0, -1.25, p, v));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0.707106781373, 0., 1.25, p, v));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0., 0.707106781373, 0.707106781373, -1.25, p, v));
			assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0., 0.707106781373, 0.707106781373, 1.25, p, v));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0, 0.707106781373, -1.25, p, v));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0, 0.707106781373, 1.25, p, v));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.577350269, 0.577350269, 0.577350269, -1.25, p, v));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.577350269, 0.577350269, 0.577350269, 1.25, p, v));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0.707106781373, 0, -1.25, p, v));
			assertReceiverInvoked(createPoint(-3.26598632371, 5.0337532762, -8.2997395999),
					createVector(0.28867513467, -0.28867513467, -0.28867513467), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0.707106781373, 0., 1.25, p, v));
			assertReceiverInvoked(createPoint(-3.26598632371, 1.49821937121, -4.764205695),
					createVector(.28867513467, -.28867513467, -.28867513467), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0., 0.707106781373, 0.707106781373, -1.25, p, v));
			assertReceiverInvoked(createPoint(-5.7828629618, .88388347625, .88388347625),
					createVector(0., .57735026934, -.57735026934), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0., 0.707106781373, 0.707106781373, 1.25, p, v));
			assertReceiverInvoked(createPoint(-4.01509600932, -.88388347625, -.88388347625),
					createVector(0., .57735026934, -.57735026934), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0, 0.707106781373, -1.25, p, v));
			assertReceiverInvoked(createPoint(-3.26598632371, -8.2997395999, 5.0337532762),
					createVector(-.28867513467, .28867513467, .28867513467), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0, 0.707106781373, 1.25, p, v));
			assertReceiverInvoked(createPoint(-3.2659863237109032, -4.76420569492157, 1.4982193712106662),
					createVector(-.28867513467, .28867513467, .28867513467), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.577350269, 0.577350269, 0.577350269, -1.25, p, v));
			assertReceiverInvoked(createPoint(-11.963022481304906, 7.064042995738549, 7.064042995738549),
					createVector(0.0, 0.23570226031810146, -0.23570226031810146), p, v);
		}

		@DisplayName("(..., Point3D, Vector3D) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.577350269, 0.577350269, 0.577350269, 1.25, p, v));
			assertReceiverInvoked(createPoint(-7.632895460960519, 2.733915975394163, 2.733915975394163),
					createVector(0.0, 0.23570226031810146, -0.23570226031810146), p, v);
		}

		@DisplayName("(..., PointVector3DReceiver) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0.707106781373, 0., -1.25, r));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0.707106781373, 0., 1.25, r));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0., 0.707106781373, 0.707106781373, -1.25, r));
			assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0., 0.707106781373, 0.707106781373, 1.25, r));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0, 0.707106781373, -1.25, r));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0, 0.707106781373, 1.25, r));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.577350269, 0.577350269, 0.577350269, -1.25, r));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(1., -1., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.577350269, 0.577350269, 0.577350269, 1.25, r));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(1., -1., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0.707106781373, 0, -1.25, r));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0.707106781373, 0., 1.25, r));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0., 0.707106781373, 0.707106781373, -1.25, r));
			assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0., 0.707106781373, 0.707106781373, 1.25, r));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0, 0.707106781373, -1.25, r));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0, 0.707106781373, 1.25, r));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.577350269, 0.577350269, 0.577350269, -1.25, r));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.577350269, 0.577350269, 0.577350269, 1.25, r));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0.707106781373, 0, -1.25, r));
			assertReceiverInvoked(createPoint(-3.26598632371, 5.0337532762, -8.2997395999),
					createVector(0.28867513467, -0.28867513467, -0.28867513467), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0.707106781373, 0., 1.25, r));
			assertReceiverInvoked(createPoint(-3.26598632371, 1.49821937121, -4.764205695),
					createVector(.28867513467, -.28867513467, -.28867513467), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0., 0.707106781373, 0.707106781373, -1.25, r));
			assertReceiverInvoked(createPoint(-5.7828629618, .88388347625, .88388347625),
					createVector(0., .57735026934, -.57735026934), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0., 0.707106781373, 0.707106781373, 1.25, r));
			assertReceiverInvoked(createPoint(-4.01509600932, -.88388347625, -.88388347625),
					createVector(0., .57735026934, -.57735026934), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0, 0.707106781373, -1.25, r));
			assertReceiverInvoked(createPoint(-3.26598632371, -8.2997395999, 5.0337532762),
					createVector(-.28867513467, .28867513467, .28867513467), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0, 0.707106781373, 1.25, r));
			assertReceiverInvoked(createPoint(-3.2659863237109032, -4.76420569492157, 1.4982193712106662),
					createVector(-.28867513467, .28867513467, .28867513467), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.577350269, 0.577350269, 0.577350269, -1.25, r));
			assertReceiverInvoked(createPoint(-11.963022481304906, 7.064042995738549, 7.064042995738549),
					createVector(0.0, 0.23570226031810146, -0.23570226031810146), r);
		}

		@DisplayName("(..., PointVector3DReceiver) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorreceiver_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.577350269, 0.577350269, 0.577350269, 1.25, r));
			assertReceiverInvoked(createPoint(-7.632895460960519, 2.733915975394163, 2.733915975394163),
					createVector(0.0, 0.23570226031810146, -0.23570226031810146), r);
		}

		@DisplayName("(..., Segment) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0.707106781373, 0., -1.25, s));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("(..., Segment) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0.707106781373, 0., 1.25, s));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("(..., Segment) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0., 0.707106781373, 0.707106781373, -1.25, s));
			assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), s);
		}

		@DisplayName("(..., Segment) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0., 0.707106781373, 0.707106781373, 1.25, s));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), s);
		}

		@DisplayName("(..., Segment) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0, 0.707106781373, -1.25, s));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), s);
		}

		@DisplayName("(..., Segment) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.707106781373, 0, 0.707106781373, 1.25, s));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), s);
		}

		@DisplayName("(..., Segment) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.577350269, 0.577350269, 0.577350269, -1.25, s));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(1., -1., 0.), s);
		}

		@DisplayName("(..., Segment) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., 1., -1.25,
					0.577350269, 0.577350269, 0.577350269, 1.25, s));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(1., -1., 0.), s);
		}

		@DisplayName("(..., Segment) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0.707106781373, 0, -1.25, s));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("(..., Segment) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0.707106781373, 0., 1.25, s));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("(..., Segment) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0., 0.707106781373, 0.707106781373, -1.25, s));
			assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), s);
		}

		@DisplayName("(..., Segment) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0., 0.707106781373, 0.707106781373, 1.25, s));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), s);
		}

		@DisplayName("(..., Segment) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0, 0.707106781373, -1.25, s));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), s);
		}

		@DisplayName("(..., Segment) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.707106781373, 0, 0.707106781373, 1.25, s));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), s);
		}

		@DisplayName("(..., Segment) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.577350269, 0.577350269, 0.577350269, -1.25, s));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("(..., Segment) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					0., 0., -1., 1.25,
					0.577350269, 0.577350269, 0.577350269, 1.25, s));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("(..., Segment) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0.707106781373, 0, -1.25, s));
			assertReceiverInvoked(createPoint(-3.26598632371, 5.0337532762, -8.2997395999),
					createVector(0.28867513467, -0.28867513467, -0.28867513467), s);
		}

		@DisplayName("(..., Segment) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0.707106781373, 0., 1.25, s));
			assertReceiverInvoked(createPoint(-3.26598632371, 1.49821937121, -4.764205695),
					createVector(.28867513467, -.28867513467, -.28867513467), s);
		}

		@DisplayName("(..., Segment) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0., 0.707106781373, 0.707106781373, -1.25, s));
			assertReceiverInvoked(createPoint(-5.7828629618, .88388347625, .88388347625),
					createVector(0., .57735026934, -.57735026934), s);
		}

		@DisplayName("(..., Segment) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0., 0.707106781373, 0.707106781373, 1.25, s));
			assertReceiverInvoked(createPoint(-4.01509600932, -.88388347625, -.88388347625),
					createVector(0., .57735026934, -.57735026934), s);
		}

		@DisplayName("(..., Segment) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0, 0.707106781373, -1.25, s));
			assertReceiverInvoked(createPoint(-3.26598632371, -8.2997395999, 5.0337532762),
					createVector(-.28867513467, .28867513467, .28867513467), s);
		}

		@DisplayName("(..., Segment) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.707106781373, 0, 0.707106781373, 1.25, s));
			assertReceiverInvoked(createPoint(-3.2659863237109032, -4.76420569492157, 1.4982193712106662),
					createVector(-.28867513467, .28867513467, .28867513467), s);
		}

		@DisplayName("(..., Segment) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.577350269, 0.577350269, 0.577350269, -1.25, s));
			assertReceiverInvoked(createPoint(-11.963022481304906, 7.064042995738549, 7.064042995738549),
					createVector(0.0, 0.23570226031810146, -0.23570226031810146), s);
		}

		@DisplayName("(..., Segment) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = createSegment(0, 0, 0, 0, 0, 0);
			assertTrue(Plane3afp.findsPlanePlaneIntersection(
					NORMAL_X, NORMAL_Y, NORMAL_Z, D,
					0.577350269, 0.577350269, 0.577350269, 1.25, s));
			assertReceiverInvoked(createPoint(-7.632895460960519, 2.733915975394163, 2.733915975394163),
					createVector(0.0, 0.23570226031810146, -0.23570226031810146), s);
		}
	}

	@DisplayName("findsPlanePointProjection")
	@Nested
	public class FindsPlanePointProjection {

		private Point3D<?, ?, ?> p;
		
		@BeforeEach
		public void setUp() {
			p = createPoint(0, 0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0, 0, 0,
				p);
			assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjection(
				-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
				0, 0, 0,
				p);
			assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				-5, -7, 0,
				p);
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjection(
				-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
				-5, -7, 0,
				p);
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjection(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				-1, 8, -2,
				p);
			assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852), p);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjection(
				-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
				-1, 8, -2,
				p);
			assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852), p);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjection(
				1.53 * NORMAL_X, 1.53 * NORMAL_Y, 1.53 * NORMAL_Z, D,
				0, 0, 0,
				p);
			assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjection(
				-1.53 * NORMAL_X, -1.53 * NORMAL_Y, -1.53 * NORMAL_Z, -D,
				0, 0, 0,
				p);
			assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjection(
				1.53 * NORMAL_X, 1.53 * NORMAL_Y, 1.53 * NORMAL_Z, D,
				-5, -7, 0,
				p);
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjection(
				-1.53 * NORMAL_X, -1.53 * NORMAL_Y, -1.53 * NORMAL_Z, -D,
				-5, -7, 0,
				p);
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjection(
				1.53 * NORMAL_X, 1.53 * NORMAL_Y, 1.53 * NORMAL_Z, D,
				-1, 8, -2,
				p);
			assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852), p);
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjection(
				-1.53 * NORMAL_X, -1.53 * NORMAL_Y, -1.53 * NORMAL_Z, -D,
				-1, 8, -2,
				p);
			assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852), p);
		}
	}

	@DisplayName("findsPlanePointProjectionWithPlaneNormal")
	@Nested
	public class FindsPlanePointProjectionWithPlaneNormal {

		private Point3D<?, ?, ?> p;
		
		@BeforeEach
		public void setUp() {
			p = createPoint(0, 0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjectionWithPlaneNormal(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				0, 0, 0,
				p);
			assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjectionWithPlaneNormal(
				-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
				0, 0, 0,
				p);
			assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjectionWithPlaneNormal(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				-5, -7, 0,
				p);
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjectionWithPlaneNormal(
				-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
				-5, -7, 0,
				p);
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjectionWithPlaneNormal(
				NORMAL_X, NORMAL_Y, NORMAL_Z, D,
				-1, 8, -2,
				p);
			assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852), p);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Plane3afp.findsPlanePointProjectionWithPlaneNormal(
				-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
				-1, 8, -2,
				p);
			assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852), p);
		}
	}

	@DisplayName("findsPlaneSegmentIntersection")
	@Nested
	public class FindsPlaneSegmentIntersection {

		private Point3D<?, ?, ?> p;
		
		@BeforeEach
		public void setUp() {
			p = createPoint(0, 0, 0);
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2., 2., -2., p));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2., 2., -2., 0., 0., 0., p));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814, p));
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0., p));
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -5, -7, 0, p));
			assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, 0., 0., 0., p));
			assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -9, -2., -1, -5, -7, 0, p));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, -9, -2., -1, p));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523, p));
			assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(Plane3afp.findsPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814, p));
			assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);
		}
	}

	@DisplayName("calculatesPlaneSegmentIntersectionFactor")
	@Nested
	public class CalculatesPlaneSegmentIntersectionFactor {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNaN(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2., 2., -2.));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNaN(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2., 2., -2., 0., 0., 0.));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1., Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.576350527713689, Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -5, -7, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.423649472286, Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, 0., 0., 0.));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNaN(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -9, -2., -1, -5, -7, 0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNaN(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, -9, -2., -1));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertInfinity(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertInfinity(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		}
	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

		@DisplayName("(x,y,z) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, 0));
		}

		@DisplayName("(x,y,z) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		}

		@DisplayName("(x,y,z) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(-8, -4, -4));
		}

		@DisplayName("(x,y,z) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, 0));
		}

		@DisplayName("(x,y,z) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		}

		@DisplayName("(x,y,z) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(-8, -4, -4));
		}
	
		@DisplayName("(Box3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createAlignedBoxFromPoints(0., 0., 0., 2., 2., 2.)));
		}
		
		@DisplayName("(Box3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createAlignedBoxFromPoints(-5., -2., -1., -3., 0., 1.)));
		}
		
		@DisplayName("(Box3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createAlignedBoxFromPoints(-6., -3., -1., -4., -1., 1.)));
		}
		
		@DisplayName("(Box3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createAlignedBoxFromPoints(-8., -3., -2., -6., -1., 0.)));
		}
		
		@DisplayName("(Box3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createAlignedBoxFromPoints(0., 0., 0., 2., 2., 2.)));
		}
		
		@DisplayName("(Box3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createAlignedBoxFromPoints(-5., -2., -1., -3., 0., 1.)));
		}
		
		@DisplayName("(Box3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createAlignedBoxFromPoints(-6., -3., -1., -4., -1., 1.)));
		}
		
		@DisplayName("(Box3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createAlignedBoxFromPoints(-8., -3., -2., -6., -1., 0.)));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createPoint(-8, -4, -4)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createPoint(-8, -4, -4)));
		}
	
		@DisplayName("(lx1,ly1,lz1, ux2,uy2,uz2) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0., 0., 0., 2., 2., 2.));
		}
		
		@DisplayName("(lx1,ly1,lz1, ux2,uy2,uz2) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(-5., -2., -1., -3., 0., 1.));
		}
		
		@DisplayName("(lx1,ly1,lz1, ux2,uy2,uz2) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(-6., -3., -1., -4., -1., 1.));
		}
		
		@DisplayName("(lx1,ly1,lz1, ux2,uy2,uz2) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(-8., -3., -2., -6., -1., 0.));
		}
		
		@DisplayName("(lx1,ly1,lz1, ux2,uy2,uz2) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzyz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0., 0., 0., 2., 2., 2.));
		}
		
		@DisplayName("(lx1,ly1,lz1, ux2,uy2,uz2) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(-5., -2., -1., -3., 0., 1.));
		}
		
		@DisplayName("(lx1,ly1,lz1, ux2,uy2,uz2) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzyz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(-6., -3., -1., -4., -1., 1.));
		}
		
		@DisplayName("(lx1,ly1,lz1, ux2,uy2,uz2) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzyz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(-8., -3., -2., -6., -1., 0.));
		}
	
		@DisplayName("(x,y,z,radius) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0., 0., 0., 2.));
		}
		
		@DisplayName("(x,y,z,radius) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(-2.45, -1.22, -1.22, 2.));
		}
		
		@DisplayName("(x,y,z,radius) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(-3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		}
		
		@DisplayName("(x,y,z,radius) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(-4.08, -2.04, -2.04, 2.));
		}
		
		@DisplayName("(x,y,z,radius) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(-5.7154760665, -2.8577380332, -2.8577380332, 2.));
		}
		
		@DisplayName("(x,y,z,radius) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0., 0., 0., 2.));
		}
		
		@DisplayName("(x,y,z,radius) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(-2.45, -1.22, -1.22, 2.));
		}
		
		@DisplayName("(x,y,z,radius) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(-3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		}
		
		@DisplayName("(x,y,z,radius) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(-4.08, -2.04, -2.04, 2.));
		}
		
		@DisplayName("(x,y,z,radius) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(-5.7154760665, -2.8577380332, -2.8577380332, 2.));
		}
	
		@DisplayName("(Plane3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createPlane(A, B, C, D)));
		}
		
		@DisplayName("(Plane3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		}
		
		@DisplayName("(Plane3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		}
		
		@DisplayName("(Plane3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		}
		
		@DisplayName("(Plane3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createPlane(1., 2., 0., 5.)));
		}
		
		@DisplayName("(Plane3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createPlane(A, B, C, D)));
		}
		
		@DisplayName("(Plane3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		}
		
		@DisplayName("(Plane3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		}
		
		@DisplayName("(Plane3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		}
		
		@DisplayName("(Plane3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createPlane(1., 2., 0., 5.)));
		}
	
		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSegment(0., 0., 0., -2., 2., -2.)));
		}
		
		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSegment(-2., 2., -2., 0., 0., 0.)));
		}
		
		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		}
		
		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));
		}
		
		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSegment(0., 0., 0., -5, -7, 0)));
		}
		
		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSegment(-5, -7, 0, 0., 0., 0.)));
		}
		
		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSegment(-9, -2., -1, -5, -7, 0)));
		}
		
		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSegment(-5, -7, 0, -9, -2., -1)));
		}
		
		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		}
		
		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		}
		
		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createSegment(0., 0., 0., -2., 2., -2.)));
		}
		
		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createSegment(-2., 2., -2., 0., 0., 0.)));
		}
		
		@DisplayName("(Segment3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		}
		
		@DisplayName("(Segment3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));
		}
		
		@DisplayName("(Segment3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSegment(0., 0., 0., -5, -7, 0)));
		}
		
		@DisplayName("(Segment3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSegment(-5, -7, 0, 0., 0., 0.)));
		}
		
		@DisplayName("(Segment3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createSegment(-9, -2., -1, -5, -7, 0)));
		}
		
		@DisplayName("(Segment3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createSegment(-5, -7, 0, -9, -2., -1)));
		}
		
		@DisplayName("(Segment3afp) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		}
		
		@DisplayName("(Segment3afp) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		}
	
		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSphere(0., 0., 0., 2.)));
		}
		
		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSphere(-2.45, -1.22, -1.22, 2.)));
		}
		
		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSphere(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		}
		
		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSphere(-4.08, -2.04, -2.04, 2.)));
		}
		
		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSphere(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
		}
		
		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createSphere(0., 0., 0., 2.)));
		}
		
		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSphere(-2.45, -1.22, -1.22, 2.)));
		}
		
		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSphere(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		}
		
		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSphere(-4.08, -2.04, -2.04, 2.)));
		}
		
		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createSphere(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
		}
	}

	@DisplayName("p && Box3afp")
	@Nested
	public class OperatorAndBox3afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(0., 0., 0., 2., 2., 2.)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createAlignedBoxFromPoints(-5., -2., -1., -3., 0., 1.)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createAlignedBoxFromPoints(-6., -3., -1., -4., -1., 1.)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(-8., -3., -2., -6., -1., 0.)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(0., 0., 0., 2., 2., 2.)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createAlignedBoxFromPoints(-5., -2., -1., -3., 0., 1.)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createAlignedBoxFromPoints(-6., -3., -1., -4., -1., 1.)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(-8., -3., -2., -6., -1., 0.)));
		}
	}

	@DisplayName("p && Segment3afp")
	@Nested
	public class OperatorAndSegment3afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSegment(0., 0., 0., -2., 2., -2.)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSegment(-2., 2., -2., 0., 0., 0.)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSegment(0., 0., 0., -5, -7, 0)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSegment(-5, -7, 0, 0., 0., 0.)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSegment(-9, -2., -1, -5, -7, 0)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSegment(-5, -7, 0, -9, -2., -1)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createSegment(0., 0., 0., -2., 2., -2.)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createSegment(-2., 2., -2., 0., 0., 0.)));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createSegment(0., 0., 0., -5, -7, 0)));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createSegment(-5, -7, 0, 0., 0., 0.)));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createSegment(-9, -2., -1, -5, -7, 0)));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createSegment(-5, -7, 0, -9, -2., -1)));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		}
	}

	@DisplayName("p && Sphere3afp")
	@Nested
	public class OperatorAndSphere3afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSphere(0., 0., 0., 2.)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSphere(-2.45, -1.22, -1.22, 2.)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSphere(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSphere(-4.08, -2.04, -2.04, 2.)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSphere(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createSphere(0., 0., 0., 2.)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSphere(-2.45, -1.22, -1.22, 2.)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSphere(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSphere(-4.08, -2.04, -2.04, 2.)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createSphere(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
		}
	}

	@DisplayName("p += double")
	@Nested
	public class OperatorAddDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().operator_add(7);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(11, getP().getEquationComponentD());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().operator_add(-18);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(-14, getP().getEquationComponentD());
		}
	}

	@DisplayName("p += Vector3D")
	@Nested
	public class OperatorAddVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().operator_add(createVector(1, 2, 3));
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(1.1422619667529594, getP().getEquationComponentD());
		}
	}

	@DisplayName("p && Plane3D")
	@Nested
	public class OperatorAndPlane3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createPlane(A, B, C, D)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createPlane(1., 2., 0., 5.)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createPlane(A, B, C, D)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createPlane(1., 2., 0., 5.)));
		}
	}

	@DisplayName("p && Point3D")
	@Nested
	public class OperatorAndPoint3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createPoint(0, 0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createPoint(-8, -4, -4)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createPoint(0, 0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createPoint(-8, -4, -4)));
		}
	}

	@DisplayName("p == Plane3D")
	@Nested
	public class OperatorEqualsPlane3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_equals(createPlane(A, B, C, D)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_equals(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_equals(createPlane(1., 2., 0., 5.)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();	
			assertFalse(getP().operator_equals(createPlane(A, B, C, D)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_equals(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_equals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_equals(createPlane(1., 2., 0., 5.)));
		}
	}

	@DisplayName("p != Plane3D")
	@Nested
	public class OperatorNotEqualsPlane3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_notEquals(createPlane(A, B, C, D)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_notEquals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_notEquals(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_notEquals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_notEquals(createPlane(1., 2., 0., 5.)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_notEquals(createPlane(A, B, C, D)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_notEquals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_notEquals(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_notEquals(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_notEquals(createPlane(1., 2., 0., 5.)));
		}
	}

	@DisplayName("-p")
	@Nested
	public class OperatorMinus {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().operator_minus();
			assertNotSame(getP(), p);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(D, getP().getEquationComponentD());
			assertEpsilonEquals(-NORMAL_X, p.getEquationComponentA());
			assertEpsilonEquals(-NORMAL_Y, p.getEquationComponentB());
			assertEpsilonEquals(-NORMAL_Z, p.getEquationComponentC());
			assertEpsilonEquals(-D, p.getEquationComponentD());
		}
	}

	@DisplayName("p - double")
	@Nested
	public class OperatorMinusDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().operator_minus(-7);
			assertNotSame(getP(), p);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(D, getP().getEquationComponentD());
			assertEpsilonEquals(NORMAL_X, p.getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, p.getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, p.getEquationComponentC());
			assertEpsilonEquals(11, p.getEquationComponentD());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().operator_minus(-18);
			assertNotSame(getP(), p);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(D, getP().getEquationComponentD());
			assertEpsilonEquals(NORMAL_X, p.getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, p.getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, p.getEquationComponentC());
			assertEpsilonEquals(-21, p.getEquationComponentD());
		}
	}

	@DisplayName("p - Vector3D")
	@Nested
	public class OperatorMinusVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().operator_minus(createVector(-1, -2, -3));
			assertNotSame(getP(), p);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(D, getP().getEquationComponentD());
			assertEpsilonEquals(NORMAL_X, p.getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, p.getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, p.getEquationComponentC());
			assertEpsilonEquals(1.1422619667529594, p.getEquationComponentD());
		}
	}

	@DisplayName("p * Quaternion")
	@Nested
	public class OperatorMultiplyQuaternion {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			Transform3D transform = new Transform3D();
			transform.makeRotationMatrix(q);
			
			var p = getP().operator_multiply(q);
	
			assertNotSame(getP(), p);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(D, getP().getEquationComponentD());
			assertEpsilonEquals(0.9525793444, p.getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, p.getEquationComponentB());
			assertEpsilonEquals(-0.272165527, p.getEquationComponentC());
			assertEpsilonEquals(2.44444444444, p.getEquationComponentD());
		}
	}

	@DisplayName("p * Transform3D")
	@Nested
	public class OperatorMultiplyTransform3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0, 0, 0, 0);
			q.setAxisAngle(-1, 1, -1, Math.PI / 3.);
	
			Transform3D transform = new Transform3D();
			transform.makeRotationMatrix(q);
			transform.setTranslation(6, -3, 1);
			
			var p = getP().operator_multiply(transform);
	
			assertNotSame(getP(), p);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(D, getP().getEquationComponentD());
			assertEpsilonEquals(0.9525793444, p.getEquationComponentA());
			assertEpsilonEquals(-0.1360827635, p.getEquationComponentB());
			assertEpsilonEquals(-0.272165527, p.getEquationComponentC());
			assertEpsilonEquals(-3.4071143855375916, p.getEquationComponentD());
		}
	}

	@DisplayName("p + double")
	@Nested
	public class OperatorPlusDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().operator_plus(7);
			assertNotSame(getP(), p);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(D, getP().getEquationComponentD());
			assertEpsilonEquals(NORMAL_X, p.getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, p.getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, p.getEquationComponentC());
			assertEpsilonEquals(11, p.getEquationComponentD());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().operator_plus(-18);
			assertNotSame(getP(), p);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(D, getP().getEquationComponentD());
			assertEpsilonEquals(NORMAL_X, p.getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, p.getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, p.getEquationComponentC());
			assertEpsilonEquals(-21, p.getEquationComponentD());
		}
	}

	@DisplayName("p + Vector3D")
	@Nested
	public class OperatorPlusVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().operator_plus(createVector(1, 2, 3));
			assertNotSame(getP(), p);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(D, getP().getEquationComponentD());
			assertEpsilonEquals(NORMAL_X, p.getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, p.getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, p.getEquationComponentC());
			assertEpsilonEquals(1.1422619667529594, p.getEquationComponentD());
		}
	}

	@DisplayName("p -= double")
	@Nested
	public class OperatorRemoveDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().operator_remove(-7);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(11, getP().getEquationComponentD());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().operator_remove(-18);
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(-14, getP().getEquationComponentD());
		}
	}

	@DisplayName("p -= Vector3D")
	@Nested
	public class OperatorRemoveVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().operator_remove(createVector(-1, -2, -3));
			assertEpsilonEquals(NORMAL_X, getP().getEquationComponentA());
			assertEpsilonEquals(NORMAL_Y, getP().getEquationComponentB());
			assertEpsilonEquals(NORMAL_Z, getP().getEquationComponentC());
			assertEpsilonEquals(1.1422619667529594, getP().getEquationComponentD());
		}
	}

	@DisplayName("p .. Plane3D")
	@Nested
	public class OperatorUpToPlane3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(createPlane(A, B, C, D)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(18., getP().operator_upTo(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 18)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(18., getP().operator_upTo(createPlane(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D + 18)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(createPlane(NORMAL_X - 1, NORMAL_Y, NORMAL_Z, D)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(createPlane(NORMAL_X, NORMAL_Y - 1, NORMAL_Z, D)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(createPlane(NORMAL_X, NORMAL_Y, NORMAL_Z - 1, D)));
		}
	}

	@DisplayName("p .. Point3D")
	@Nested
	public class OperatorUpToPoint3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, getP().operator_upTo(createPoint(0, 0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-2.94022093788, getP().operator_upTo(createPoint(-5, -7, 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.6329931618, getP().operator_upTo(createPoint(-1, 8, -2)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(-4, getP().operator_upTo(createPoint(0, 0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(2.940220937885, getP().operator_upTo(createPoint(-5, -7, 0)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(-5.6329931618, getP().operator_upTo(createPoint(-1, 8, -2)));
		}
	}

}
