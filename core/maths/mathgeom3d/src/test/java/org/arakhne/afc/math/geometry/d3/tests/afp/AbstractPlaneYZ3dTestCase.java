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

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.PlaneClassification;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.PointVector3DReceiver;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.PlaneYZ3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.d.Plane3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneXY3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneXZ3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneYZ3d;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

@SuppressWarnings("all")
public abstract class AbstractPlaneYZ3dTestCase<T extends PlaneYZ3afp<T, ?, ?, ?, ?>, B extends AlignedBox3afp<?, ?, ?, ?, ?, B>> extends AbstractPlane3DTestCase<T, B> {

	private T plane;

	protected final T getP() {
		return plane;
	}

	protected abstract T createTestPlane(double z, boolean positive);

	@BeforeEach
	public final void setUp() {
		super.setUp();
		plane = createTestPlane(1.25, false);
	}

	@AfterEach
	public final void tearDown() throws Exception {
		plane = null;
	}

	@DisplayName("toGeogebra")
	@Nested
	public class ToGeogebra {

		@DisplayName("With negative normal")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void toGeogebra_negative(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals("-1.0*x+0.0*y+0.0*z+1.25=0.0", getP().toGeogebra());
		}
	
		@DisplayName("With positive normal")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void toGeogebra_positive(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			PlaneYZ3d np = new PlaneYZ3d(true, 1.25);
			assertEquals("1.0*x+0.0*y+0.0*z-1.25=0.0", np.toGeogebra());
		}
	}

	@DisplayName("getX")
	@Nested
	public class GetX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.25, getP().getX());
		}
	}

	@DisplayName("getEquationComponentA")
	@Nested
	public class GetEquationComponentA {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
		}
	}

	@DisplayName("getEquationComponentB")
	@Nested
	public class GetEquationComponentB {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getEquationComponentB());
		}
	}

	@DisplayName("getEquationComponentC")
	@Nested
	public class GetEquationComponentC {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getEquationComponentC());
		}
	}

	@DisplayName("getEquationComponentD")
	@Nested
	public class GetEquationComponentD {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.25, getP().getEquationComponentD());
		}
	}

	@DisplayName("setX")
	@Nested
	public class SetX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setX(123.589);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(123.589, getP().getEquationComponentD());
			assertEpsilonEquals(createPoint(123.589, 0, 0), getP().getPivot());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setX(-453.154);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-453.154, getP().getEquationComponentD());
			assertEpsilonEquals(createPoint(-453.154, 0, 0), getP().getPivot());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			getP().setX(123.589);
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-123.589, getP().getEquationComponentD());
			assertEpsilonEquals(createPoint(123.589, 0, 0), getP().getPivot());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			getP().setX(-453.154);
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(453.154, getP().getEquationComponentD());
			assertEpsilonEquals(createPoint(-453.154, 0, 0), getP().getPivot());
		}
	}

	@DisplayName("setPositive")
	@Nested
	public class SetPositive {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(createPoint(1.25, 0, 0), getP().getPivot());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(1.25, getP().getEquationComponentD());
			assertEpsilonEquals(createPoint(1.25, 0, 0), getP().getPivot());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			getP().setPositive(false);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(1.25, getP().getEquationComponentD());
			assertEpsilonEquals(createPoint(1.25, 0, 0), getP().getPivot());
		}
	}

	@DisplayName("getNormal")
	@Nested
	public class GetNormal {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createVector(-1., 0., 0.), getP().getNormal());
		}
	}

	@DisplayName("getNormalX")
	@Nested
	public class GetNormalX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-1, getP().getNormalX());
		}
	}

	@DisplayName("getNormalY")
	@Nested
	public class GetNormalY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonZero(getP().getNormalY());
		}
	}

	@DisplayName("getNormalZ")
	@Nested
	public class GetNormalZ {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonZero(getP().getNormalZ());
		}
	}

	@DisplayName("getPivot")
	@Nested
	public class GetPivot {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1.25, 0., 0.), getP().getPivot());
		}
	}

	@DisplayName("absolute")
	@Nested
	public class Absolute {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().absolute();
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		}
	}

	@DisplayName("clear")
	@Nested
	public class Clear {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().clear();
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
		}
	}

	@DisplayName("negate")
	@Nested
	public class Negate {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			getP().negate();
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(1.25, getP().getEquationComponentD());
		}
	}

	@DisplayName("calculatesDistancePlaneYZPlane")
	@Nested
	public class CalculatesDistancePlaneYZPlane {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.25, PlaneYZ3afp.calculatesPlaneYZPlaneDistance(true, 1.25, 1., 0., 0., -4.5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneYZ3afp.calculatesPlaneYZPlaneDistance(true, 1.25, 1., 0., 0., -1.25));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-5.75, PlaneYZ3afp.calculatesPlaneYZPlaneDistance(true, 1.25, 1., 0., 0., 4.5));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-5.75, PlaneYZ3afp.calculatesPlaneYZPlaneDistance(true, 1.25, -1., 0., 0., -4.5));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneYZ3afp.calculatesPlaneYZPlaneDistance(true, 1.25, -1., 0., 0., 1.25));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.25, PlaneYZ3afp.calculatesPlaneYZPlaneDistance(true, 1.25, -1., 0., 0., 4.5));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3.25, PlaneYZ3afp.calculatesPlaneYZPlaneDistance(false, 1.25, 1., 0., 0., -4.5));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneYZ3afp.calculatesPlaneYZPlaneDistance(false, 1.25, 1., 0., 0., -1.25));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.75, PlaneYZ3afp.calculatesPlaneYZPlaneDistance(false, 1.25, 1., 0., 0., 4.5));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.75, PlaneYZ3afp.calculatesPlaneYZPlaneDistance(false, 1.25, -1., 0., 0., -4.5));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneYZ3afp.calculatesPlaneYZPlaneDistance(false, 1.25, -1., 0., 0., 1.25));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3.25, PlaneYZ3afp.calculatesPlaneYZPlaneDistance(false, 1.25, -1., 0., 0., 4.5));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneYZ3afp.calculatesPlaneYZPlaneDistance(true, 1.25, 1., 0., 1., -4.5));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneYZ3afp.calculatesPlaneYZPlaneDistance(false, 1.25, 1., 0., 1., -4.5));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneYZ3afp.calculatesPlaneYZPlaneDistance(true, 1.25, 1., 1., 0., -4.5));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneYZ3afp.calculatesPlaneYZPlaneDistance(false, 1.25, 1., 1., 0., -4.5));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneYZ3afp.calculatesPlaneYZPlaneDistance(true, 1.25, 0., 1., 1., -4.5));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneYZ3afp.calculatesPlaneYZPlaneDistance(false, 1.25, 0., 1., 1., -4.5));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneYZ3afp.calculatesPlaneYZPlaneDistance(true, 1.25, 1., 1., 1., -4.5));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneYZ3afp.calculatesPlaneYZPlaneDistance(false, 1.25, 1., 1., 1., -4.5));
		}
	}

	@DisplayName("calculatesDistancePlaneYZPoint")
	@Nested
	public class CalculatesDistancePlaneYZPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-1.25, PlaneYZ3afp.calculatesPlaneYZPointDistance(true, 1.25, 0, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneYZ3afp.calculatesPlaneYZPointDistance(true, 1.25, 1.25, 0, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-11.25, PlaneYZ3afp.calculatesPlaneYZPointDistance(true, 1.25, -10, 0, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.75, PlaneYZ3afp.calculatesPlaneYZPointDistance(true, 1.25, 10, 0, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.25, PlaneYZ3afp.calculatesPlaneYZPointDistance(false, 1.25, 0, 0, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneYZ3afp.calculatesPlaneYZPointDistance(false, 1.25, 1.25, 0, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.25, PlaneYZ3afp.calculatesPlaneYZPointDistance(false, 1.25, -10, 0, 0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-8.75, PlaneYZ3afp.calculatesPlaneYZPointDistance(false, 1.25, 10, 0, 0));
		}
	}

	@DisplayName("findsPlaneYZSegmentIntersection")
	@Nested
	public class FindsPlaneYZSegmentIntersection {

		private Point3D<?, ?, ?> p;

		@BeforeEach
		public void setUp() {
			p = createPoint(0, 0, 0);
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					true, 1.25,
					-51.2, -5.6, 32.5,
					.5, -7.9, 47.1,
					p));
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					true, 1.25,
					47.2, -5.6, 32.5,
					2., -7.9, 47.1,
					p));
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					true, 1.25,
					0., -5.6, 32.5,
					2., -7.9, 47.1,
					p));
			assertEpsilonEquals(createPoint(1.25, -7.0375, 41.625), p);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					true, 1.25,
					2., -5.6, 32.5,
					0., -7.9, 47.1,
					p));
			assertEpsilonEquals(createPoint(1.25, -6.4625, 37.975), p);
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					true, 1.25,
					1.25, -5.6, 32.5,
					0., -7.9, 47.1,
					p));
			assertEpsilonEquals(createPoint(1.25, -5.6, 32.5), p);
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					true, 1.25,
					0., -5.6, 32.5,
					1.25, -7.9, 47.1,
					p));
			assertEpsilonEquals(createPoint(1.25, -7.9, 47.1), p);
		}
		
		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					true, 1.25,
					1.25, -5.6, 32.5,
					2., -7.9, 47.1,
					p));
			assertEpsilonEquals(createPoint(1.25, -5.6, 32.5), p);
		}
		
		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					true, 1.25,
					2., -5.6, 32.5,
					1.25, -7.9, 47.1,
					p));
			assertEpsilonEquals(createPoint(1.25, -7.9, 47.1), p);
		}
		
		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					true, 1.25,
					1.25, -5.6, 32.5,
					1.25, -7.9, 47.1,
					p));
			assertEpsilonEquals(createPoint(1.25, -5.6, 32.5), p);
		}
		
		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					false, 1.25,
					-51.2, -5.6, 32.5,
					.5, -7.9, 47.1,
					p));
		}
		
		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					false, 1.25,
					47.2, -5.6, 32.5,
					2., -7.9, 47.1,
					p));
		}
		
		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					false, 1.25,
					0., -5.6, 32.5,
					2., -7.9, 47.1,
					p));
			assertEpsilonEquals(createPoint(1.25, -7.0375, 41.625), p);
		}
		
		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					false, 1.25,
					2., -5.6, 32.5,
					0., -7.9, 47.1,
					p));
			assertEpsilonEquals(createPoint(1.25, -6.4625, 37.975), p);
		}
		
		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					false, 1.25,
					1.25, -5.6, 32.5,
					0., -7.9, 47.1,
					p));
			assertEpsilonEquals(createPoint(1.25, -5.6, 32.5), p);
		}
		
		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					false, 1.25,
					0., -5.6, 32.5,
					1.25, -7.9, 47.1,
					p));
			assertEpsilonEquals(createPoint(1.25, -7.9, 47.1), p);
		}
		
		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					false, 1.25,
					1.25, -5.6, 32.5,
					2., -7.9, 47.1,
					p));
			assertEpsilonEquals(createPoint(1.25, -5.6, 32.5), p);
		}
		
		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					true, 1.25,
					2., -5.6, 32.5,
					1.25, -7.9, 47.1,
					p));
			assertEpsilonEquals(createPoint(1.25, -7.9, 47.1), p);
		}
		
		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZSegmentIntersection(
					false, 1.25,
					1.25, -5.6, 32.5,
					1.25, -7.9, 47.1,
					p));
			assertEpsilonEquals(createPoint(1.25, -5.6, 32.5), p);
		}
	}

	@DisplayName("calculatesIntersectionFactorPlaneYZSegment")
	@Nested
	public class CalculatesIntersectionFactorPlaneYZSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNaN(PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
					true, 1.25,
					-51.2, -5.6, 32.5,
					.5, -7.9, 47.1));
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNaN(PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
					true, 1.25,
					47.2, -5.6, 32.5,
					2., -7.9, 47.1));
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.625,
					PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
							true, 1.25,
							0., -5.6, 32.5,
							2., -7.9, 47.1));
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.375,
					PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
							true, 1.25,
							2., -5.6, 32.5,
							0., -7.9, 47.1));
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.,
					PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
							true, 1.25,
							1.25, -5.6, 32.5,
							0., -7.9, 47.1));
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.,
					PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
							true, 1.25,
							0., -5.6, 32.5,
							1.25, -7.9, 47.1));
		}
		
		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.,
					PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
							true, 1.25,
							1.25, -5.6, 32.5,
							2., -7.9, 47.1));
		}
		
		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.,
					PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
							true, 1.25,
							2., -5.6, 32.5,
							1.25, -7.9, 47.1));
		}
		
		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertInfinity(PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
					true, 1.25,
					1.25, -5.6, 32.5,
					1.25, -7.9, 47.1));
		}
		
		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNaN(PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
					false, 1.25,
					-51.2, -5.6, 32.5,
					.5, -7.9, 47.1));
		}
		
		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNaN(PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
					false, 1.25,
					47.2, -5.6, 32.5,
					2., -7.9, 47.1));
		}
		
		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.625,
					PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
							false, 1.25,
							0., -5.6, 32.5,
							2., -7.9, 47.1));
		}
		
		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.375,
					PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
							false, 1.25,
							2., -5.6, 32.5,
							0., -7.9, 47.1));
		}
		
		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.,
					PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
							false, 1.25,
							1.25, -5.6, 32.5,
							0., -7.9, 47.1));
		}
		
		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.,
					PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
							false, 1.25,
							0., -5.6, 32.5, 
							1.25, -7.9, 47.1));
		}
		
		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.,
					PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
							false, 1.25,
							1.25, -5.6, 32.5,
							2., -7.9, 47.1));
		}
		
		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.,
					PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
							true, 1.25,
							2., -5.6, 32.5,
							1.25, -7.9, 47.1));
		}
		
		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertInfinity(PlaneYZ3afp.calculatesPlaneYZSegmentIntersectionFactor(
					false, 1.25,
					1.25, -5.6, 32.5,
					1.25, -7.9, 47.1));
		}
	}

	@DisplayName("classifiesPlaneYZAlignedBox")
	@Nested
	public class ClassifiesPlaneYZAlignedBox {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZAlignedBox(true, 1.25, 0, 0, 0, 1, 1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZAlignedBox(true, 1.25, 0, 0, 0, 1.25, 1, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZAlignedBox(true, 1.25, 0, 0, 0, 2, 1, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZAlignedBox(true, 1.25, 2, 2, 2, 3, 3, 3));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZAlignedBox(true, 1.25, 1.25, 2, 2, 3, 3, 3));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZAlignedBox(true, 1.25, 1, 2, 2, 3, 3, 3));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZAlignedBox(false, 1.25, 0, 0, 0, 1, 1, 1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZAlignedBox(false, 1.25, 0, 0, 0, 1.25, 1, 1));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZAlignedBox(false, 1.25, 0, 0, 0, 2, 1, 1));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZAlignedBox(false, 1.25, 2, 2, 2, 3, 3, 3));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZAlignedBox(false, 1.25, 1.25, 2, 2, 3, 3, 3));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZAlignedBox(false, 1.25, 1, 2, 2, 3, 3, 3));
		}
	}

	@DisplayName("classifiesPlaneYZPlane")
	@Nested
	public class ClassifiesPlaneYZPlane {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZPlane(true, 4, 4., -3., 1., -4.));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZPlane(true, 4, 1., 0., 0., 4.));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZPlane(true, 4, 1., 0., 0., -4.));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZPlane(true, 4, -1., 0., 0., -4.));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZPlane(true, 4, -1., 0., 0., 4.));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZPlane(true, 4, 1., 0., 0., 6.));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZPlane(true, 4, 1., 0., 0., -6.));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZPlane(true, 4, -1., 0., 0., -6.));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZPlane(true, 4, -1., 0., 0., 6.));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZPlane(false, 4, 4., -3., 1., -4.));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZPlane(false, 4, 1., 0., 0., 4.));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZPlane(false, 4, 1., 0., 0., -4.));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZPlane(false, 4, -1., 0., 0., -4.));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZPlane(false, 4, -1., 0., 0., 4.));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZPlane(false, 4, 1., 0., 0., 6.));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZPlane(false, 4, 1., 0., 0., -6.));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZPlane(false, 4, -1., 0., 0., -6.));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZPlane(false, 4, -1., 0., 0., 6.));
		}
	}

	@DisplayName("classifiesPlaneYZPoint")
	@Nested
	public class ClassifiesPlaneYZPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZPoint(true, 1.25, 0, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZPoint(true, 1.25, 1.25, 0, 0));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZPoint(true, 1.25, -10, 0, 0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZPoint(true, 1.25, 10, 0, 0));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZPoint(false, 1.25, 0, 0, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZPoint(false, 1.25, 1.25, 0, 0));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZPoint(false, 1.25, -10, 0, 0));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZPoint(false, 1.25, 10, 0, 0));
		}
	}

	@DisplayName("classifiesPlaneYZSegment")
	@Nested
	public class ClassifiesPlaneYZSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZSegment(true, 1.25, 0, 0, 0, 1, 1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZSegment(true, 1.25, 0, 0, 0, 1.25, 1, 1));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZSegment(true, 1.25, 0, 0, 0, 2, 1, 1));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZSegment(true, 1.25, 3, 3, 3, 2, 2, 2));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZSegment(true, 1.25, 3, 3, 3, 1.25, 2, 2));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZSegment(true, 1.25, 3, 3, 3, 1, 2, 2));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZSegment(false, 1.25, 0, 0, 0, 1, 1, 1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZSegment(false, 1.25, 0, 0, 0, 1.25, 1, 1));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZSegment(false, 1.25, 0, 0, 0, 2, 1, 1));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZSegment(false, 1.25, 3, 3, 3, 2, 2, 2));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZSegment(false, 1.25, 3, 3, 3, 1.25, 2, 2));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZSegment(false, 1.25, 3, 3, 3, 1, 2, 2));
		}
	}

	@DisplayName("classifiesPlaneYZSphere")
	@Nested
	public class ClassifiesPlaneYZSphere {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZSphere(true, 1.25, 0, 0, 0, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZSphere(true, 1.25, 0, 0, 0, 1.25));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZSphere(true, 1.25, 0, 0, 0, 2));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZSphere(true, 1.25, 3, 0, 0, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZSphere(true, 1.25, 3, 0, 0, 1.75));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZSphere(true, 1.25, 3, 0, 0, 2));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZSphere(false, 1.25, 0, 0, 0, 1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneYZ3afp.classifiesPlaneYZSphere(false, 1.25, 0, 0, 0, 1.25));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZSphere(false, 1.25, 0, 0, 0, 2));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZSphere(false, 1.25, 3, 0, 0, 1));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneYZ3afp.classifiesPlaneYZSphere(false, 1.25, 3, 0, 0, 1.75));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneYZ3afp.classifiesPlaneYZSphere(false, 1.25, 3, 0, 0, 2));
		}
	}

	@DisplayName("classifies")
	@Nested
	public class Classifies {

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void box_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1.25, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 2, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void box_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(AlignedBox3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void box_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(1.25, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(AlignedBox3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void box_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(1, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(AlignedBox3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void box_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void box_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1.25, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void box_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 2, 1, 1)));
		}

		@DisplayName("(AlignedBox3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void box_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(AlignedBox3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void box_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(1.25, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(AlignedBox3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void box_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(1, 2, 2, 3, 3, 3)));
		}

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
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPoint(1.25, 0, 0)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPoint(-10, 0, 0)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createPoint(10, 0, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPoint(1.25, 0, 0)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createPoint(-10, 0, 0)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPoint(10, 0, 0)));
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSphere(0, 0, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSphere(0, 0, 0, 1.25)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(0, 0, 0, 2)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSphere(3, 0, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSphere(3, 0, 0, 1.75)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(3, 0, 0, 2)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSphere(0, 0, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSphere(0, 0, 0, 1.25)));
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(0, 0, 0, 2)));
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSphere(3, 0, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSphere(3, 0, 0, 1.75)));
		}

		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(3, 0, 0, 2)));
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
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(1.25, 0, 0));
		}

		@DisplayName("(x,y,z) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(-10, 0, 0));
		}

		@DisplayName("(x,y,z) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(10, 0, 0));
		}

		@DisplayName("(x,y,z) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 0));
		}

		@DisplayName("(x,y,z) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(1.25, 0, 0));
		}

		@DisplayName("(x,y,z) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(-10, 0, 0));
		}

		@DisplayName("(x,y,z) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(10, 0, 0));
		}

		@DisplayName("(x,y,z,radius) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 0, 1));
		}

		@DisplayName("(x,y,z,radius) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 0, 1.25));
		}

		@DisplayName("(x,y,z,radius) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 0, 2));
		}

		@DisplayName("(x,y,z,radius) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(3, 0, 0, 1));
		}

		@DisplayName("(x,y,z,radius) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(3, 0, 0, 1.75));
		}

		@DisplayName("(x,y,z,radius) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(3, 0, 0, 2));
		}

		@DisplayName("(x,y,z,radius) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 0, 1));
		}

		@DisplayName("(x,y,z,radius) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 0, 1.25));
		}

		@DisplayName("(x,y,z,radius) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 0, 2));
		}

		@DisplayName("(x,y,z,radius) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(3, 0, 0, 1));
		}

		@DisplayName("(x,y,z,radius) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(3, 0, 0, 1.75));
		}

		@DisplayName("(x,y,z,radius) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(3, 0, 0, 2));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 0, 1, 1, 1));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 0, 1.25, 1, 1));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 0, 2, 1, 1));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(2, 2, 2, 3, 3, 3));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(1.25, 2, 2, 3, 3, 3));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(1, 2, 2, 3, 3, 3));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 0, 1, 1, 1));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 0, 1.25, 1, 1));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 0, 2, 1, 1));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(2, 2, 2, 3, 3, 3));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(1.25, 2, 2, 3, 3, 3));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(1, 2, 2, 3, 3, 3));
		}

		@DisplayName("(Plane3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(4., -3., 1., -4.)));
		}

		@DisplayName("(Plane3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(1., 0., 0., -1.25)));
		}

		@DisplayName("(Plane3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(1., 0., 0., 1.25)));
		}

		@DisplayName("(Plane3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(-1., 0., 0., 1.25)));
		}

		@DisplayName("(Plane3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(-1., 0., 0., -1.25)));
		}

		@DisplayName("(Plane3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(1., 0., 0., 6.)));
		}

		@DisplayName("(Plane3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(1., 0., 0., -6.)));
		}

		@DisplayName("(Plane3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(-1., 0., 0., -6.)));
		}

		@DisplayName("(Plane3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(-1., 0., 0., 6.)));
		}

		@DisplayName("(Plane3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(4., -3., 1., -4.)));
		}

		@DisplayName("(Plane3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(1., 0., 0., -1.25)));
		}

		@DisplayName("(Plane3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(1., 0., 0., 1.25)));
		}

		@DisplayName("(Plane3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(-1., 0., 0., 1.25)));
		}

		@DisplayName("(Plane3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(-1., 0., 0., -1.25)));
		}

		@DisplayName("(Plane3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(1., 0., 0., 6.)));
		}

		@DisplayName("(Plane3D) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(1., 0., 0., -6.)));
		}

		@DisplayName("(Plane3D) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(-1., 0., 0., -6.)));
		}

		@DisplayName("(Plane3D) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(-1., 0., 0., 6.)));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(0, 0, 0, 1.25, 1, 1)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(0, 0, 0, 2, 1, 1)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(3, 3, 3, 2, 2, 2)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(3, 3, 3, 1.25, 2, 2)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(3, 3, 3, 1, 2, 2)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(0, 0, 0, 1.25, 1, 1)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(0, 0, 0, 2, 1, 1)));
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(3, 3, 3, 2, 2, 2)));
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(3, 3, 3, 1.25, 2, 2)));
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(3, 3, 3, 1, 2, 2)));
		}
	}

	@DisplayName("findsPlaneYZPlaneIntersection")
	@Nested
	public class FindsPlaneYZPlaneIntersection {

		private Point3D<?, ?, ?> p;
		private Vector3D<?, ?, ?> v;
		private Segment3afp s;
		private Point3D<?, ?, ?> p1;
		private Point3D<?, ?, ?> p2;

		@BeforeEach
		public void setUp() {
			p = createPoint(0., 0., 0.);
			v = createVector(0., 0., 0.);
			s = createSegment(0, 0, 0, 0, 0, 0);
			p1 = createPoint(0, 0, 0);
			p2 = createPoint(0, 0, 0);

		}

		private void assertReceiverInvoked(Point3D<?, ?, ?> p, Vector3D<?, ?, ?> v, Segment3afp<?, ?, ?, ?, ?, ?, ?> s) {
			assertEpsilonEquals(p, s.getP1());
			var v0 = s.getP2().operator_minus(s.getP1());
			assertEpsilonColinear(v, v0);
		}

		private void assertReceiverInvoked(Point3D<?, ?, ?> p, Vector3D<?, ?, ?> v, Point3D<?, ?, ?> a1, Point3D<?, ?, ?> a2) {
			assertEpsilonEquals(p, a1);
			var v0 = a2.operator_minus(a1);
			assertEpsilonColinear(v, v0);
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
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 1., 0., 0., -4.5, p, v));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 1., 0., 0., 4.5, p, v));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, -1., 0., 0., -4.5, p, v));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, -1., 0., 0., 4.5, p, v));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 1., 0., 0., -1.25, p, v));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, -1., 0., 0., 1.25, p, v));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, 0, 0.517766953), p);
			assertEpsilonColinear(createVector(0., -1., 0.), v);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, 0., -3.017766953), p);
			assertEpsilonColinear(createVector(0., -1., 0.), v);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, 0.517766953, 0), p);
			assertEpsilonColinear(createVector(0., 0., 1.), v);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, -3.017766953, 0), p);
			assertEpsilonColinear(createVector(0., 0., 1.), v);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0, 0.707106781373, 0.707106781373, -1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, 0.8838834765, 0.8838834765), p);
			assertEpsilonColinear(createVector(0., 1., -1.), v);
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, -0.8838834765, -0.8838834765), p);
			assertEpsilonColinear(createVector(0., 1., -1.), v);
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, 0.4575317547, 0.4575317547), p);
			assertEpsilonColinear(createVector(0., -1., 1.), v);
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, -1.7075317547, -1.7075317547), p);
			assertEpsilonColinear(createVector(0., -1., 1.), v);
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 1., 0., 0., -4.5, p, v));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 1., 0., 0., 4.5, p, v));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, -1., 0., 0., -4.5, p, v));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, -1., 0., 0., 4.5, p, v));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 1., 0., 0., -1.25, p, v));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, -1., 0., 0., 1.25, p, v));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, 0, 0.517766953), p);
			assertEpsilonColinear(createVector(0., 1., 0.), v);
		}

		@DisplayName("#22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, 0, -3.017766953), p);
			assertEpsilonColinear(createVector(0., 1., 0.), v);
		}

		@DisplayName("#23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, 0.517766953, 0.), p);
			assertEpsilonColinear(createVector(0., 0., -1.), v);
		}

		@DisplayName("#24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, -3.017766953, 0.), p);
			assertEpsilonColinear(createVector(0., 0., -1.), v);
		}

		@DisplayName("#25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, 0.8838834765, 0.8838834765), p);
			assertEpsilonColinear(createVector(0., 1., -1.), v);
		}

		@DisplayName("#26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_26(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, -0.8838834765, -0.8838834765), p);
			assertEpsilonColinear(createVector(0., 1., -1.), v);
		}

		@DisplayName("#27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_27(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, 0.4575317547, 0.4575317547), p);
			assertEpsilonColinear(createVector(0., 1., -1.), v);
		}

		@DisplayName("#28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_28(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, p, v));
			assertEpsilonEquals(createPoint(1.25, -1.7075317547, -1.7075317547), p);
			assertEpsilonColinear(createVector(0., 1., -1.), v);
		}

		@DisplayName("#29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_29(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, s));
			assertReceiverInvoked(createPoint(1.25, 0.8838834765, 0.8838834765), createVector(0., 1., -1.), s);
		}

		@DisplayName("#30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_30(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, s));
			assertReceiverInvoked(createPoint(1.25, -0.8838834765, -0.8838834765), createVector(0., 1., -1.), s);
		}

		@DisplayName("#31")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_31(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, s));
			assertReceiverInvoked(createPoint(1.25, 0.517766953, 0.), createVector(0., 0., 1.), s);
		}

		@DisplayName("#32")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_32(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, s));
			assertReceiverInvoked(createPoint(1.25, -3.017766953, 0.), createVector(0., 0., 1.), s);
		}

		@DisplayName("#33")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_33(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, s));
			assertReceiverInvoked(createPoint(1.25, 0, 0.517766953), createVector(0., -1., 0.), s);
		}

		@DisplayName("#34")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_34(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, s));
			assertReceiverInvoked(createPoint(1.25, 0, -3.017766953), createVector(0., -1., 0.), s);
		}

		@DisplayName("#35")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_35(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, s));
			assertReceiverInvoked(createPoint(1.25, 0.4575317547, 0.4575317547), createVector(0., -1., 1.), s);
		}

		@DisplayName("#36")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_36(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, s));
			assertReceiverInvoked(createPoint(1.25, -1.7075317547, -1.7075317547), createVector(0., -1., 1.), s);
		}

		@DisplayName("#37")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_37(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, s));
			assertReceiverInvoked(createPoint(1.25, 0.8838834765, 0.8838834765), createVector(0., 1., -1.), s);
		}

		@DisplayName("#38")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_38(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, s));
			assertReceiverInvoked(createPoint(1.25, -0.8838834765, -0.8838834765), createVector(0., 1., -1.), s);
		}

		@DisplayName("#39")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_39(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, s));
			assertReceiverInvoked(createPoint(1.25, 0.517766953, 0.), createVector(0., 0., 1.), s);
		}

		@DisplayName("#40")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_40(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, s));
			assertReceiverInvoked(createPoint(1.25, -3.017766953, 0.), createVector(0., 0., -1.), s);
		}

		@DisplayName("#41")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_41(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, s));
			assertReceiverInvoked(createPoint(1.25, 0, 0.517766953), createVector(0., 1., 0.), s);
		}

		@DisplayName("#42")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_42(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, s));
			assertReceiverInvoked(createPoint(1.25, 0, -3.017766953), createVector(0., 1., 0.), s);
		}

		@DisplayName("#43")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_43(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, s));
			assertReceiverInvoked(createPoint(1.25, 0.4575317547, 0.4575317547), createVector(0., 1., -1.), s);
		}

		@DisplayName("#44")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_44(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, s));
			assertReceiverInvoked(createPoint(1.25, -1.7075317547, -1.7075317547), createVector(0., 1., -1.), s);
		}

		@DisplayName("#45")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_45(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, 0.8838834765, 0.8838834765), createVector(0., 1., -1.), p1, p2);
		}

		@DisplayName("#46")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_46(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, -0.8838834765, -0.8838834765), createVector(0., 1., -1.), p1, p2);
		}

		@DisplayName("#47")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_47(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, 0.517766953, 0.), createVector(0., 0., 1.), p1, p2);
		}

		@DisplayName("#48")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_48(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, -3.017766953, 0.), createVector(0., 0., 1.), p1, p2);
		}

		@DisplayName("#49")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_49(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, 0, 0.517766953), createVector(0., -1., 0.), p1, p2);
		}

		@DisplayName("#50")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_50(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, 0, -3.017766953), createVector(0., -1., 0.), p1, p2);
		}

		@DisplayName("#51")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_51(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, 0.4575317547, 0.4575317547), createVector(0., -1., 1.), p1, p2);
		}

		@DisplayName("#52")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_52(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, -1.7075317547, -1.7075317547), createVector(0., -1., 1.), p1, p2);
		}

		@DisplayName("#53")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_53(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, 0.8838834765, 0.8838834765), createVector(0., 1., -1.), p1, p2);
		}

		@DisplayName("#54")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_54(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, -0.8838834765, -0.8838834765), createVector(0., 1., -1.), p1, p2);
		}

		@DisplayName("#55")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_55(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, 0.517766953, 0.), createVector(0., 0., -1.), p1, p2);
		}

		@DisplayName("#56")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_56(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, -3.017766953, 0.), createVector(0., 0., -1.), p1, p2);
		}

		@DisplayName("#57")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_57(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, 0, 0.517766953), createVector(0., 1., 0.), p1, p2);
		}

		@DisplayName("#58")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_58(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, 0, -3.017766953), createVector(0., 1., 0.), p1, p2);
		}

		@DisplayName("#59")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_59(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, 0.4575317547, 0.4575317547), createVector(0., 1., -1.), p1, p2);
		}

		@DisplayName("#60")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_60(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(1.25, -1.7075317547, -1.7075317547), createVector(0., 1., -1.), p1, p2);
		}

		@DisplayName("#61")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_61(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, r));
			assertReceiverInvoked(createPoint(1.25, 0.8838834765, 0.8838834765), createVector(0., 1., -1.), r);
		}

		@DisplayName("#62")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_62(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, r));
			assertReceiverInvoked(createPoint(1.25, -0.8838834765, -0.8838834765), createVector(0., 1., -1.), r);
		}

		@DisplayName("#63")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_63(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, r));
			assertReceiverInvoked(createPoint(1.25, 0.517766953, 0.), createVector(0., 0., 1.), r);
		}

		@DisplayName("#64")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_64(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, r));
			assertReceiverInvoked(createPoint(1.25, -3.017766953, 0.), createVector(0., 0., 1.), r);
		}

		@DisplayName("#65")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_65(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0, 0.707106781373, -1.25, r));
			assertReceiverInvoked(createPoint(1.25, 0, 0.517766953), createVector(0., -1., 0.), r);
		}

		@DisplayName("#66")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_66(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.707106781373, 0, 0.707106781373, 1.25, r));
			assertReceiverInvoked(createPoint(1.25, 0, -3.017766953), createVector(0., -1., 0.), r);
		}

		@DisplayName("#67")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_67(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, r));
			assertReceiverInvoked(createPoint(1.25, 0.4575317547, 0.4575317547), createVector(0., -1., 1.), r);
		}

		@DisplayName("#68")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_68(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, r));
			assertReceiverInvoked(createPoint(1.25, -1.7075317547, -1.7075317547), createVector(0., -1., 1.), r);
		}

		@DisplayName("#69")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_69(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0, 0.707106781373, 0.707106781373, -1.25, r));
			assertReceiverInvoked(createPoint(1.25, 0.8838834765, 0.8838834765), createVector(0., 1., -1.), r);
		}

		@DisplayName("#70")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_70(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, r));
			assertReceiverInvoked(createPoint(1.25, -0.8838834765, -0.8838834765), createVector(0., 1., -1.), r);
		}

		@DisplayName("#71")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_71(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, r));
			assertReceiverInvoked(createPoint(1.25, 0.517766953, 0.), createVector(0., 0., -1.), r);
		}

		@DisplayName("#72")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_72(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, r));
			assertReceiverInvoked(createPoint(1.25, -3.017766953, 0.), createVector(0., 0., -1.), r);
		}

		@DisplayName("#73")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_73(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0, 0.707106781373, -1.25, r));
			assertReceiverInvoked(createPoint(1.25, 0, 0.517766953), createVector(0., 1., 0.), r);
		}

		@DisplayName("#74")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_74(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.707106781373, 0, 0.707106781373, 1.25, r));
			assertReceiverInvoked(createPoint(1.25, 0, -3.017766953), createVector(0., 1., 0.), r);
		}

		@DisplayName("#75")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_75(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, r));
			assertReceiverInvoked(createPoint(1.25, 0.4575317547, 0.4575317547), createVector(0., 1., -1.), r);
		}

		@DisplayName("#76")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_76(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneYZ3afp.findsPlaneYZPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, r));
			assertReceiverInvoked(createPoint(1.25, -1.7075317547, -1.7075317547), createVector(0., 1., -1.), r);
		}
	}

	@DisplayName("getDistanceTo")
	@Nested
	public class GetDistanceTo {

		@DisplayName("(Plane3D) with XZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(true, 4.5)));
		}

		@DisplayName("(Plane3D) with XZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(true, 1.25)));
		}

		@DisplayName("(Plane3D) with XZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(true, -4.5)));
		}

		@DisplayName("(Plane3D) with XZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(false, -4.5)));
		}

		@DisplayName("(Plane3D) with XZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(false, 1.25)));
		}

		@DisplayName("(Plane3D) with XZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(false, 4.5)));
		}

		@DisplayName("(Plane3D) with XZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(true, 4.5)));
		}

		@DisplayName("(Plane3D) with XZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(true, 1.25)));
		}

		@DisplayName("(Plane3D) with XZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(true, -4.5)));
		}

		@DisplayName("(Plane3D) with XZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(false, -4.5)));
		}

		@DisplayName("(Plane3D) with XZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(false, 1.25)));
		}

		@DisplayName("(Plane3D) with XZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(false, 4.5)));
		}

		@DisplayName("(Plane3D) with YZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3.25, getP().getDistanceTo(new PlaneYZ3d(true, 4.5)));
		}

		@DisplayName("(Plane3D) with YZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(true, 1.25)));
		}

		@DisplayName("(Plane3D) with YZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.75, getP().getDistanceTo(new PlaneYZ3d(true, -4.5)));
		}

		@DisplayName("(Plane3D) with YZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.75, getP().getDistanceTo(new PlaneYZ3d(false, -4.5)));
		}

		@DisplayName("(Plane3D) with YZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(false, 1.25)));
		}

		@DisplayName("(Plane3D) with YZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3.25, getP().getDistanceTo(new PlaneYZ3d(false, 4.5)));
		}

		@DisplayName("(Plane3D) with YZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(3.25, getP().getDistanceTo(new PlaneYZ3d(true, 4.5)));
		}

		@DisplayName("(Plane3D) with YZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(true, 1.25)));
		}

		@DisplayName("(Plane3D) with YZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(-5.75, getP().getDistanceTo(new PlaneYZ3d(true, -4.5)));
		}

		@DisplayName("(Plane3D) with YZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(-5.75, getP().getDistanceTo(new PlaneYZ3d(false, -4.5)));
		}

		@DisplayName("(Plane3D) with YZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(false, 1.25)));
		}

		@DisplayName("(Plane3D) with YZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(3.25, getP().getDistanceTo(new PlaneYZ3d(false, 4.5)));
		}
	
		@DisplayName("(Plane3D) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(true, 4.5)));
		}
		
		@DisplayName("(Plane3D) with XY plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(true, 1.25)));
		}
		
		@DisplayName("(Plane3D) with XY plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(true, -4.5)));
		}
		
		@DisplayName("(Plane3D) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(false, -4.5)));
		}
		
		@DisplayName("(Plane3D) with XY plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(false, 1.25)));
		}
		
		@DisplayName("(Plane3D) with XY plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(false, 4.5)));
		}
		
		@DisplayName("(Plane3D) with XY plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(true, 4.5)));
		}
		
		@DisplayName("(Plane3D) with XY plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(true, 1.25)));
		}
		
		@DisplayName("(Plane3D) with XY plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(true, -4.5)));
		}
		
		@DisplayName("(Plane3D) with XY plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(false, -4.5)));
		}
		
		@DisplayName("(Plane3D) with XY plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(false, 1.25)));
		}
		
		@DisplayName("(Plane3D) with XY plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(false, 4.5)));
		}

		@DisplayName("(Plane3D) with general plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(1., 0., 1., -4.5)));
		}

		@DisplayName("(Plane3D) with general plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(1., 1., 0., -4.5)));
		}

		@DisplayName("(Plane3D) with general plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(0., 1., 1., -4.5)));
		}

		@DisplayName("(Plane3D) with general plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(1., 1., 1., -4.5)));
		}

		@DisplayName("(Plane3D) with general plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(1., 0., 1., -4.5)));
		}

		@DisplayName("(Plane3D) with general plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(1., 1., 0., -4.5)));
		}

		@DisplayName("(Plane3D) with general plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(0., 1., 1., -4.5)));
		}

		@DisplayName("(Plane3D) with general plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(1., 1., 1., -4.5)));
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.25, getP().getDistanceTo(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPoint(1.25, 0, 0)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.25, getP().getDistanceTo(createPoint(-10, 0, 0)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-8.75, getP().getDistanceTo(createPoint(10, 0, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.25, getP().getDistanceTo(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(createPoint(1.25, 0, 0)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.25, getP().getDistanceTo(createPoint(-10, 0, 0)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-8.75, getP().getDistanceTo(createPoint(10, 0, 0)));
		}

		@DisplayName("(double,double,double,double) with XZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., -4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(0., 1., 0., -1.25));
		}

		@DisplayName("(double,double,double,double) with XZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., 4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., -4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(0., -1., 0., 1.25));
		}

		@DisplayName("(double,double,double,double) with XZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., 4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., -4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(0., 1., 0., -1.25));
		}

		@DisplayName("(double,double,double,double) with XZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., 4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., -4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(0., -1., 0., 1.25));
		}

		@DisplayName("(double,double,double,double) with XZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., 4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3.25, getP().getDistanceTo(1., 0., 0., -4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -1.25));
		}

		@DisplayName("(double,double,double,double) with XY plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.75, getP().getDistanceTo(1., 0., 0., 4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.75, getP().getDistanceTo(-1., 0., 0., -4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 1.25));
		}

		@DisplayName("(double,double,double,double) with XY plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3.25, getP().getDistanceTo(-1., 0., 0., 4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(3.25, getP().getDistanceTo(1., 0., 0., -4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -1.25));
		}

		@DisplayName("(double,double,double,double) with XY plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(-5.75, getP().getDistanceTo(1., 0., 0., 4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(-5.75, getP().getDistanceTo(-1., 0., 0., -4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 1.25));
		}

		@DisplayName("(double,double,double,double) with XY plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(3.25, getP().getDistanceTo(-1., 0., 0., 4.5));
		}

		@DisplayName("(double,double,double,double) with YZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., 1., -4.5));
		}

		@DisplayName("(double,double,double,double) with YZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., 1., -1.25));
		}

		@DisplayName("(double,double,double,double) with YZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., 1., 4.5));
		}

		@DisplayName("(double,double,double,double) with YZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., -1., -4.5));
		}

		@DisplayName("(double,double,double,double) with YZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., -1., 1.25));
		}

		@DisplayName("(double,double,double,double) with YZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., -1., 4.5));
		}

		@DisplayName("(double,double,double,double) with YZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., 1., -4.5));
		}

		@DisplayName("(double,double,double,double) with YZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., 1., -1.25));
		}

		@DisplayName("(double,double,double,double) with YZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., 1., 4.5));
		}

		@DisplayName("(double,double,double,double) with YZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., -1., -4.5));
		}

		@DisplayName("(double,double,double,double) with YZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., -1., 1.25));
		}

		@DisplayName("(double,double,double,double) with YZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., -1., 4.5));
		}

		@DisplayName("(double,double,double,double) with general plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 1., -4.5));
		}

		@DisplayName("(double,double,double,double) with general plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(1., 1., 0., -4.5));
		}

		@DisplayName("(double,double,double,double) with general plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(0., 1., 1., -4.5));
		}

		@DisplayName("(double,double,double,double) with general plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(1., 1., 1., -4.5));
		}

		@DisplayName("(double,double,double,double) with general plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 1., -4.5));
		}

		@DisplayName("(double,double,double,double) with general plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(1., 1., 0., -4.5));
		}

		@DisplayName("(double,double,double,double) with general plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(0., 1., 1., -4.5));
		}

		@DisplayName("(double,double,double,double) with general plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(1., 1., 1., -4.5));
		}
	}

	@DisplayName("this .. Plane3D")
	@Nested
	public class OperatorUpToPlane3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(-1., 0., 0., 1.25)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 0., 0., -1.25)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3.25, getP().operator_upTo(new Plane3d(1., 0., 0., -4.5)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.75, getP().operator_upTo(new Plane3d(1., 0., 0., 4.5)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.75, getP().operator_upTo(new Plane3d(-1., 0., 0., -4.5)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3.25, getP().operator_upTo(new Plane3d(-1., 0., 0., 4.5)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 0., 1., -4.5)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 1., 0., -4.5)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(0., 1., 1., -4.5)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 1., 1., -4.5)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 0., 1., -4.5)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 1., 0., -4.5)));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(0., 1., 1., -4.5)));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 1., 1., -4.5)));
		}
	}

	@DisplayName("this .. Point3D")
	@Nested
	public class OperatorUpToPoint3D {
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.25, getP().operator_upTo(createPoint(0, 0, 0)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(createPoint(1.25, 0, 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.25, getP().operator_upTo(createPoint(-10, 0, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-8.75, getP().operator_upTo(createPoint(10, 0, 0)));
		}
	}

	@DisplayName("getIntersection(double,double,double,double) with general plane")
	@Nested
	public class GetIntersection {

		@DisplayName("(double,double,double,double) with general plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0.707106781373, 0.707106781373, -1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0.8838834765, 0.8838834765), s.getP1());
			assertEpsilonColinear(createVector(0., 1., -1.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with general plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0.707106781373, 0.707106781373, 1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, -0.8838834765, -0.8838834765), s.getP1());
			assertEpsilonColinear(createVector(0., 1., -1.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with general plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0.707106781373, 0.707106781373, 0., -1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0.517766953, 0.), s.getP1());
			assertEpsilonColinear(createVector(0., 0., -1.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with general plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0.707106781373, 0.707106781373, 0., 1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, -3.017766953, 0.), s.getP1());
			assertEpsilonColinear(createVector(0., 0., -1.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with general plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0.707106781373, 0., 0.707106781373, -1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0, 0.517766953), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with general plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0.707106781373, 0., 0.707106781373, 1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., -3.017766953), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with general plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0.577350269, 0.577350269, 0.577350269, -1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0.4575317547, 0.4575317547), s.getP1());
			assertEpsilonColinear(createVector(0., 1., -1.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with general plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0.577350269, 0.577350269, 0.577350269, 1.25);
			assertEpsilonEquals(createPoint(1.25, -1.7075317547, -1.7075317547), s.getP1());
			assertEpsilonColinear(createVector(0., 1., -1.), s.getDirection());
		}
	
		@DisplayName("(double,double,double,double) with XZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(1., 0., 0., -4.5));
		}
		
		@DisplayName("(double,double,double,double) with XZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(1., 0., 0., 4.5));
		}
		
		@DisplayName("(double,double,double,double) with XZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(-1., 0., 0., -4.5));
		}
		
		@DisplayName("(double,double,double,double) with XZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(-1., 0., 0., -4.5));
		}
		
		@DisplayName("(double,double,double,double) with XZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(1., 0., 0., -1.25));
		}
		
		@DisplayName("(double,double,double,double) with XZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(-1., 0., 0., 1.25));
		}
		
		@DisplayName("(double,double,double,double) with XZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(1., 0., 0., -4.5));
		}
		
		@DisplayName("(double,double,double,double) with XZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(1., 0., 0., 4.5));
		}
		
		@DisplayName("(double,double,double,double) with XZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(-1., 0., 0., 4.5));
		}
		
		@DisplayName("(double,double,double,double) with XZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(-1., 0., 0., -4.5));
		}
		
		@DisplayName("(double,double,double,double) with XZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(1., 0., 0., -1.25));
		}
		
		@DisplayName("(double,double,double,double) with XZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(-1., 0., 0., 1.25));
		}
	
		@DisplayName("(double,double,double,double) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(1., 0., 0., -4.5));
		}
		
		@DisplayName("(double,double,double,double) with XY plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(1., 0., 0., 4.5));
		}
		
		@DisplayName("(double,double,double,double) with XY plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(-1., 0., 0., -4.5));
		}
		
		@DisplayName("(double,double,double,double) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(-1., 0., 0., -4.5));
		}
		
		@DisplayName("(double,double,double,double) with XY plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(1., 0., 0., -1.25));
		}
		
		@DisplayName("(double,double,double,double) with XY plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(-1., 0., 0., 1.25));
		}
		
		@DisplayName("(double,double,double,double) with XY plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(1., 0., 0., -4.5));
		}
		
		@DisplayName("(double,double,double,double) with XY plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(1., 0., 0., 4.5));
		}
		
		@DisplayName("(double,double,double,double) with XY plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(-1., 0., 0., 4.5));
		}
		
		@DisplayName("(double,double,double,double) with XY plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(-1., 0., 0., -4.5));
		}
		
		@DisplayName("(double,double,double,double) with XY plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(1., 0., 0., -1.25));
		}
		
		@DisplayName("(double,double,double,double) with XY plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(-1., 0., 0., 1.25));
		}
	
		@DisplayName("(double,double,double,double) with YZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0., 1., -4.5);
			assertEpsilonEquals(createPoint(1.25, 0, 4.5), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}
		
		@DisplayName("(double,double,double,double) with YZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0., 1., 4.5);
			assertEpsilonEquals(createPoint(1.25, 0, -4.5), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}
		
		@DisplayName("(double,double,double,double) with YZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0., -1., -4.5);
			assertEpsilonEquals(createPoint(1.25, 0, -4.5), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}
		
		@DisplayName("(double,double,double,double) with YZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0., -1., -4.5);
			assertEpsilonEquals(createPoint(1.25, 0, -4.5), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}
		
		@DisplayName("(double,double,double,double) with YZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0., 1., -1.25);
			assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}
		
		@DisplayName("(double,double,double,double) with YZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0., -1., 1.25);
			assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}
		
		@DisplayName("(double,double,double,double) with YZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 0., 1., -4.5);
			assertEpsilonEquals(createPoint(1.25, 0, 4.5), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}
		
		@DisplayName("(double,double,double,double) with YZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 0., 1., 4.5);
			assertEpsilonEquals(createPoint(1.25, 0, -4.5), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}
		
		@DisplayName("(double,double,double,double) with YZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 0., -1., 4.5);
			assertEpsilonEquals(createPoint(1.25, 0, 4.5), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}
		
		@DisplayName("(double,double,double,double) with YZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 0., -1., -4.5);
			assertEpsilonEquals(createPoint(1.25, 0, -4.5), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}
		
		@DisplayName("(double,double,double,double) with YZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 0., 1., -1.25);
			assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}
		
		@DisplayName("(double,double,double,double) with YZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(0., 0., -1., 1.25);
			assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}
	
		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(createSegment(
					-51.2, -5.6, 32.5,
					.5, -7.9, 47.1)));
		}
		
		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(createSegment(
					47.2, -5.6, 32.5,
					2., -7.9, 47.1)));
		}
		
		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(
					0., -5.6, 32.5,
					2., -7.9, 47.1));
			assertEpsilonEquals(createPoint(1.25, -7.0375, 41.625), p);
		}
		
		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(
					2., -5.6, 32.5,
					0., -7.9, 47.1));
			assertEpsilonEquals(createPoint(1.25, -6.4625, 37.975), p);
		}
		
		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(
					1.25, -5.6, 32.5,
					0., -7.9, 47.1));
			assertEpsilonEquals(createPoint(1.25, -5.6, 32.5), p);
		}
		
		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(
					0., -5.6, 32.5,
					1.25, -7.9, 47.1));
			assertEpsilonEquals(createPoint(1.25, -7.9, 47.1), p);
		}
		
		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(
					1.25, -5.6, 32.5,
					2., -7.9, 47.1));
			assertEpsilonEquals(createPoint(1.25, -5.6, 32.5), p);
		}
		
		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(
					1.25, -5.6, 32.5,
					1.25, -7.9, 47.1));
			assertEpsilonEquals(createPoint(1.25, -5.6, 32.5), p);
		}
		
		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(createSegment(
					-51.2, -5.6, 32.5,
					.5, -7.9, 47.1)));
		}
		
		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(createSegment(
					47.2, -5.6, 32.5,
					2., -7.9, 47.1)));
		}
		
		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					0., -5.6, 32.5,
					2., -7.9, 47.1));
			assertEpsilonEquals(createPoint(1.25, -7.0375, 41.625), p);
		}
		
		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					2., -5.6, 32.5,
					0., -7.9, 47.1));
			assertEpsilonEquals(createPoint(1.25, -6.4625, 37.975), p);
		}
		
		@DisplayName("(Segment3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					1.25, -5.6, 32.5,
					0., -7.9, 47.1));
			assertEpsilonEquals(createPoint(1.25, -5.6, 32.5), p);
		}
		
		@DisplayName("(Segment3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					0., -5.6, 32.5,
					1.25, -7.9, 47.1));
			assertEpsilonEquals(createPoint(1.25, -7.9, 47.1), p);
		}
		
		@DisplayName("(Segment3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					1.25, -5.6, 32.5,
					2., -7.9, 47.1));
			assertEpsilonEquals(createPoint(1.25, -5.6, 32.5), p);
		}
		
		@DisplayName("(Segment3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					2., -5.6, 32.5,
					1.25, -7.9, 47.1));
			assertEpsilonEquals(createPoint(1.25, -7.9, 47.1), p);
		}
		
		@DisplayName("(Segment3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					1.25, -5.6, 32.5,
					1.25, -7.9, 47.1));
			assertEpsilonEquals(createPoint(1.25, -5.6, 32.5), p);
		}
		
		@DisplayName("(Segment3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					2., -5.6, 32.5,
					1.25, -7.9, 47.1));
			assertEpsilonEquals(createPoint(1.25, -7.9, 47.1), p);
		}

		@DisplayName("(Plane3D) with XZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXZ3d(true, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 4.5, 0.), s.getP1());
			assertEpsilonColinear(createVector(0, 0, 1), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXZ3d(true, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, -4.5, 0.), s.getP1());
			assertEpsilonColinear(createVector(0, 0, 1), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXZ3d(false, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 4.5, 0.), s.getP1());
			assertEpsilonColinear(createVector(0, 0, 1), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXZ3d(false, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, -4.5, 0.), s.getP1());
			assertEpsilonColinear(createVector(0, 0, 1), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXZ3d(true, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 1.25, 0.), s.getP1());
			assertEpsilonColinear(createVector(0, 0, 1), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXZ3d(false, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 1.25, 0.), s.getP1());
			assertEpsilonColinear(createVector(0, 0, 1), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXZ3d(true, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 4.5, 0.), s.getP1());
			assertEpsilonColinear(createVector(0, 0, 1), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXZ3d(true, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, -4.5, 0.), s.getP1());
			assertEpsilonColinear(createVector(0, 0, 1), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXZ3d(false, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 4.5, 0.), s.getP1());
			assertEpsilonColinear(createVector(0, 0, 1), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXZ3d(false, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, -4.5, 0.), s.getP1());
			assertEpsilonColinear(createVector(0, 0, 1), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXZ3d(true, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 1.25, 0.), s.getP1());
			assertEpsilonColinear(createVector(0, 0, 1), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXZ3d(false, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 1.25, 0.), s.getP1());
			assertEpsilonColinear(createVector(0, 0, 1), s.getDirection());
		}
	
		@DisplayName("(Plane3D) with YZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(new PlaneYZ3d(true, 4.5)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(new PlaneYZ3d(true, -4.5)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(new PlaneYZ3d(false, 4.5)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(new PlaneYZ3d(false, -4.5)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(new PlaneYZ3d(true, 1.25)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(new PlaneYZ3d(false, 1.25)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(new PlaneYZ3d(true, 4.5)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(new PlaneYZ3d(true, -4.5)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(new PlaneYZ3d(false, 4.5)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(new PlaneYZ3d(false, -4.5)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(new PlaneYZ3d(true, 1.25)));
		}
		
		@DisplayName("(Plane3D) with YZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(new PlaneYZ3d(false, 1.25)));
		}
	
		@DisplayName("(Plane3D) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXY3d(true, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., 4.5), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XY plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXY3d(true, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., -4.5), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XY plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXY3d(false, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., 4.5), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXY3d(false, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., -4.5), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XY plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXY3d(true, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XY plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXY3d(false, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XY plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXY3d(true, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., 4.5), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XY plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXY3d(true, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., -4.5), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XY plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXY3d(false, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., 4.5), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XY plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXY3d(false, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., -4.5), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XY plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXY3d(true, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}
		
		@DisplayName("(Plane3D) with XY plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXY3d(false, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}

		private void assertReceiverInvoked(Point3D<?, ?, ?> p, Vector3D<?, ?, ?> v, Segment3afp<?, ?, ?, ?, ?, ?, ?> s) {
			assertEpsilonEquals(p, s.getP1());
			var v0 = s.getP2().operator_minus(s.getP1());
			assertEpsilonColinear(v, v0);
		}

		@DisplayName("(Plane3D) with general plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(0., 1., 1., -1.25));
			assertReceiverInvoked(createPoint(1.25, 0.8838834765, 0.8838834765), createVector(0., 1., -1.), s);
		}

		@DisplayName("(Plane3D) with general plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(0., 1., 1., 1.25));
			assertReceiverInvoked(createPoint(1.25, -0.8838834765, -0.8838834765), createVector(0., 1., -1.), s);
		}

		@DisplayName("(Plane3D) with general plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(1., 1., 0., -1.25));
			assertReceiverInvoked(createPoint(1.25, 0.517766953, 0.), createVector(0., 0., 1.), s);
		}

		@DisplayName("(Plane3D) with general plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(1., 1., 0., 1.25));
			assertReceiverInvoked(createPoint(1.25, -3.017766953, 0.), createVector(0., 0., 1.), s);
		}

		@DisplayName("(Plane3D) with general plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(1., 0., 1., -1.25));
			assertReceiverInvoked(createPoint(1.25, 0, 0.517766953), createVector(0., -1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(1., 0., 1., 1.25));
			assertReceiverInvoked(createPoint(1.25, 0, -3.017766953), createVector(0., -1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(1., 1., 1., -1.25));
			assertReceiverInvoked(createPoint(1.25, 0.4575317547, 0.4575317547), createVector(0., 1., -1.), s);
		}

		@DisplayName("(Plane3D) with general plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(1., 1., 1., 1.25));
			assertReceiverInvoked(createPoint(1.25, -1.7075317547, -1.7075317547), createVector(0., 1., -1.), s);
		}

		@DisplayName("(Plane3D) with general plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(0., 1., 1., -1.25));
			assertReceiverInvoked(createPoint(1.25, 0.8838834765, 0.8838834765), createVector(0., 1., -1.), s);
		}

		@DisplayName("(Plane3D) with general plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(0., 1., 1., 1.25));
			assertReceiverInvoked(createPoint(1.25, -0.8838834765, -0.8838834765), createVector(0., 1., -1.), s);
		}

		@DisplayName("(Plane3D) with general plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(1., 1., 0., -1.25));
			assertReceiverInvoked(createPoint(1.25, 0.517766953, 0.), createVector(0., 0., -1.), s);
		}

		@DisplayName("(Plane3D) with general plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(1., 1., 0., 1.25));
			assertReceiverInvoked(createPoint(1.25, -3.017766953, 0.), createVector(0., 0., -1.), s);
		}

		@DisplayName("(Plane3D) with general plane #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(1., 0., 1., -1.25));
			assertReceiverInvoked(createPoint(1.25, 0, 0.517766953), createVector(0., 1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(1., 0., 1., 1.25));
			assertReceiverInvoked(createPoint(1.25, 0, -3.017766953), createVector(0., 1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(1., 1., 1., -1.25));
			assertReceiverInvoked(createPoint(1.25, 0.4575317547, 0.4575317547), createVector(0., 1., -1.), s);
		}

		@DisplayName("(Plane3D) with general plane #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(1., 1., 1., 1.25));
			assertReceiverInvoked(createPoint(1.25, -1.7075317547, -1.7075317547), createVector(0., 1., -1.), s);
		}
	}

	@DisplayName("this && Box3afp")
	@Nested
	public class OperatorAndBox3afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 1.25, 1, 1)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 2, 1, 1)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(1.25, 2, 2, 3, 3, 3)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createAlignedBoxFromPoints(1, 2, 2, 3, 3, 3)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 1.25, 1, 1)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 2, 1, 1)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(1.25, 2, 2, 3, 3, 3)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createAlignedBoxFromPoints(1, 2, 2, 3, 3, 3)));
		}
	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

		@DisplayName("(Box3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Box3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 1.25, 1, 1)));
		}

		@DisplayName("(Box3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 2, 1, 1)));
		}

		@DisplayName("(Box3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(Box3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createAlignedBoxFromPoints(1.25, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(Box3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createAlignedBoxFromPoints(1, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(Box3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Box3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 1.25, 1, 1)));
		}

		@DisplayName("(Box3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 2, 1, 1)));
		}

		@DisplayName("(Box3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(Box3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createAlignedBoxFromPoints(1.25, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(Box3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createAlignedBoxFromPoints(1, 2, 2, 3, 3, 3)));
		}
	
		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, 0));
		}
		
		@DisplayName("(double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(1.25, 0, 0));
		}
		
		@DisplayName("(double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(-10, 0, 0));
		}
		
		@DisplayName("(double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(10, 0, 0));
		}
		
		@DisplayName("(double,double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, 0));
		}
		
		@DisplayName("(double,double,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(1.25, 0, 0));
		}
		
		@DisplayName("(double,double,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(-10, 0, 0));
		}
		
		@DisplayName("(double,double,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(10, 0, 0));
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
			assertTrue(getP().intersects(createPoint(1.25, 0, 0)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createPoint(-10, 0, 0)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createPoint(10, 0, 0)));
		}

		@DisplayName("(Point3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createPoint(1.25, 0, 0)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createPoint(-10, 0, 0)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createPoint(10, 0, 0)));
		}

		@DisplayName("(double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, 0, 1));
		}

		@DisplayName("(double,double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, 0, 1.25));
		}

		@DisplayName("(double,double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(0, 0, 0, 2));
		}

		@DisplayName("(double,double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(3, 0, 0, 1));
		}

		@DisplayName("(double,double,double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(3, 0, 0, 1.75));
		}

		@DisplayName("(double,double,double,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(3, 0, 0, 2));
		}

		@DisplayName("(double,double,double,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, 0, 1));
		}

		@DisplayName("(double,double,double,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, 0, 1.25));
		}

		@DisplayName("(double,double,double,double) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(0, 0, 0, 2));
		}

		@DisplayName("(double,double,double,double) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(3, 0, 0, 1));
		}

		@DisplayName("(double,double,double,double) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(3, 0, 0, 1.75));
		}

		@DisplayName("(double,double,double,double) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(3, 0, 0, 2));
		}

		@DisplayName("(double,double,double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, 0, 1, 1, 1));
		}

		@DisplayName("(double,double,double,double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, 0, 1.25, 1, 1));
		}

		@DisplayName("(double,double,double,double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(0, 0, 0, 2, 1, 1));
		}

		@DisplayName("(double,double,double,double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(2, 2, 2, 3, 3, 3));
		}

		@DisplayName("(double,double,double,double,double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(1.25, 2, 2, 3, 3, 3));
		}

		@DisplayName("(double,double,double,double,double,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(1, 2, 2, 3, 3, 3));
		}

		@DisplayName("(double,double,double,double,double,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, 0, 1, 1, 1));
		}

		@DisplayName("(double,double,double,double,double,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, 0, 1.25, 1, 1));
		}

		@DisplayName("(double,double,double,double,double,double) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(0, 0, 0, 2, 1, 1));
		}

		@DisplayName("(double,double,double,double,double,double) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(2, 2, 2, 3, 3, 3));
		}

		@DisplayName("(double,double,double,double,double,double) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(1.25, 2, 2, 3, 3, 3));
		}

		@DisplayName("(double,double,double,double,double,double) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(1, 2, 2, 3, 3, 3));
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSegment(0, 0, 0, 1.25, 1, 1)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSegment(0, 0, 0, 2, 1, 1)));
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSegment(3, 3, 3, 2, 2, 2)));
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSegment(3, 3, 3, 1.25, 2, 2)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSegment(3, 3, 3, 1, 2, 2)));
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSegment(0, 0, 0, 1.25, 1, 1)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSegment(0, 0, 0, 2, 1, 1)));
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSegment(3, 3, 3, 2, 2, 2)));
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSegment(3, 3, 3, 1.25, 2, 2)));
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSegment(3, 3, 3, 1, 2, 2)));
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSphere(0, 0, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSphere(0, 0, 0, 1.25)));
		}

		@DisplayName("(Sphere3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSphere(0, 0, 0, 2)));
		}

		@DisplayName("(Sphere3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSphere(3, 0, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSphere(3, 0, 0, 1.75)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSphere(3, 0, 0, 2)));
		}

		@DisplayName("(Sphere3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createSphere(0, 0, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createSphere(0, 0, 0, 1.25)));
		}

		@DisplayName("(Sphere3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSphere(0, 0, 0, 2)));
		}

		@DisplayName("(Sphere3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createSphere(3, 0, 0, 1)));
		}

		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createSphere(3, 0, 0, 1.75)));
		}

		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSphere(3, 0, 0, 2)));
		}

		@DisplayName("(Plane3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// 1) Parallel, distinct plane -> no intersection
			//    x = 2  => -1*x+2 = 0
			assertFalse(getP().intersects(createPlane(-1, 0, 0, 2)));
		}

		@DisplayName("(Plane3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// 2) Opposite normal, same geometric plane -> intersects (coincident)
			//    x = 1.25 => 1*x-1.25 = 0
			assertTrue(getP().intersects(createPlane(1, 0, 0, -1.25)));
		}

		@DisplayName("(Plane3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// 3) Orthogonal plane Y=0 -> intersects (line parallel to Z axis at x=1.25)
			assertTrue(getP().intersects(createPlane(0, 1, 0, 0)));
		}

		@DisplayName("(Plane3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// 4) Orthogonal plane Z=0 -> intersects (line parallel to Y axis at x=1.25)
			assertTrue(getP().intersects(createPlane(0, 0, 1, 0)));
		}

		@DisplayName("(Plane3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// 5) Oblique plane -> intersects
			//    x + y + z - 3 = 0
			assertTrue(getP().intersects(createPlane(1, 1, 1, -3)));
		}

		@DisplayName("(Plane3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// 6) Another parallel, distinct plane (same normal direction)
			//    x = -4 => -1*x-4 = 0
			assertFalse(getP().intersects(createPlane(-1, 0, 0, -4)));
		}

		@DisplayName("(Plane3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// 7) Nearly parallel but not parallel (small y component in normal) -> intersects
			//    -x + 1e-12*y + 1.25 = 0
			assertTrue(getP().intersects(createPlane(-1, 1e-12, 0, 1.25)));
		}
	}

	@DisplayName("this && Point3D")
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
			assertTrue(getP().operator_and(createPoint(1.25, 0, 0)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createPoint(-10, 0, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createPoint(10, 0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createPoint(0, 0, 0)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createPoint(1.25, 0, 0)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createPoint(-10, 0, 0)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createPoint(10, 0, 0)));
		}
	}

	@DisplayName("this && Segment3afp")
	@Nested
	public class OperatorAndSegment3afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSegment(0, 0, 0, 1.25, 1, 1)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSegment(0, 0, 0, 2, 1, 1)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSegment(3, 3, 3, 2, 2, 2)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSegment(3, 3, 3, 1.25, 2, 2)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSegment(3, 3, 3, 1, 2, 2)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSegment(0, 0, 0, 1.25, 1, 1)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSegment(0, 0, 0, 2, 1, 1)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSegment(3, 3, 3, 2, 2, 2)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSegment(3, 3, 3, 1.25, 2, 2)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSegment(3, 3, 3, 1, 2, 2)));
		}
	}

	@DisplayName("this && Sphere3afp")
	@Nested
	public class OperatorAndSphere3afp {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSphere(0, 0, 0, 1)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSphere(0, 0, 0, 1.25)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSphere(0, 0, 0, 2)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSphere(3, 0, 0, 1)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSphere(3, 0, 0, 1.75)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSphere(3, 0, 0, 2)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createSphere(0, 0, 0, 1)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createSphere(0, 0, 0, 1.25)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createSphere(0, 0, 0, 2)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createSphere(3, 0, 0, 1)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createSphere(3, 0, 0, 1.75)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createSphere(3, 0, 0, 2)));
		}
	}

	@DisplayName("this && Plane3D")
	@Nested
	public class OperatorAndPlane3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			assertTrue(getP().operator_and(new Plane3d(0., 1., 1., -1.25)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			assertTrue(getP().operator_and(new Plane3d(0., 1., 1., 1.25)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			assertTrue(getP().operator_and(new Plane3d(1., 1., 0., -1.25)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			assertTrue(getP().operator_and(new Plane3d(1., 1., 0., 1.25)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			assertTrue(getP().operator_and(new Plane3d(1., 0., 1., -1.25)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			assertTrue(getP().operator_and(new Plane3d(1., 0., 1., 1.25)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			assertTrue(getP().operator_and(new Plane3d(1., 1., 1., -1.25)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			assertTrue(getP().operator_and(new Plane3d(1., 1., 1., 1.25)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			assertTrue(getP().operator_and(new Plane3d(0., 1., 1., -1.25)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			assertTrue(getP().operator_and(new Plane3d(0., 1., 1., 1.25)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			assertTrue(getP().operator_and(new Plane3d(1., 1., 0., -1.25)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			assertTrue(getP().operator_and(new Plane3d(1., 1., 0., 1.25)));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			assertTrue(getP().operator_and(new Plane3d(1., 0., 1., -1.25)));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			assertTrue(getP().operator_and(new Plane3d(1., 0., 1., 1.25)));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			assertTrue(getP().operator_and(new Plane3d(1., 1., 1., -1.25)));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			assertTrue(getP().operator_and(new Plane3d(1., 1., 1., 1.25)));
		}
	}

	@DisplayName("isPositive")
	@Nested
	public class IsPositive {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().isPositive());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			assertTrue(getP().isPositive());
		}
	}

	@DisplayName("set")
	@Nested
	public class Set {

		@DisplayName("(Plane3D) with XZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXZ3d(true, -4));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4., getP().getEquationComponentD());
			assertEpsilonEquals(-4., getP().getX());
		}

		@DisplayName("(Plane3D) with XZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXZ3d(true, 4));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-4., getP().getEquationComponentD());
			assertEpsilonEquals(4., getP().getX());
		}

		@DisplayName("(Plane3D) with XZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXZ3d(false, 4));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4., getP().getEquationComponentD());
			assertEpsilonEquals(-4., getP().getX());
		}

		@DisplayName("(Plane3D) with XZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXZ3d(false, -4));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-4., getP().getEquationComponentD());
			assertEpsilonEquals(4., getP().getX());
		}
	
		@DisplayName("(Plane3D) with YZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneYZ3d(true, -4));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4, getP().getEquationComponentD());
			assertEpsilonEquals(-4, getP().getX());
		}
		
		@DisplayName("(Plane3D) with YZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneYZ3d(true, 4));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
			assertEpsilonEquals(4, getP().getX());
		}
		
		@DisplayName("(Plane3D) with YZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneYZ3d(false, 4));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4, getP().getEquationComponentD());
			assertEpsilonEquals(4, getP().getX());
		}
		
		@DisplayName("(Plane3D) with YZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneYZ3d(false, -4));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
			assertEpsilonEquals(-4, getP().getX());
		}
	
		@DisplayName("(Plane3D) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXY3d(true, -4));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4., getP().getEquationComponentD());
			assertEpsilonEquals(-4., getP().getX());
		}
		
		@DisplayName("(Plane3D) with XY plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXY3d(true, 4));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-4., getP().getEquationComponentD());
			assertEpsilonEquals(4., getP().getX());
		}
		
		@DisplayName("(Plane3D) with XY plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXY3d(false, 4));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4., getP().getEquationComponentD());
			assertEpsilonEquals(-4., getP().getX());
		}
		
		@DisplayName("(Plane3D) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXY3d(false, -4));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-4., getP().getEquationComponentD());
			assertEpsilonEquals(4., getP().getX());
		}
	
		@DisplayName("(Plane3D) with general plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new Plane3d(3, -1, 2, -4));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
			assertEpsilonEquals(4, getP().getX());
		}
		
		@DisplayName("(Plane3D) with general plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new Plane3d(1, 0, 0, -18));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-18, getP().getEquationComponentD());
			assertEpsilonEquals(18, getP().getX());
		}
	
		@DisplayName("(double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(3, 1, 2, 4);
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4, getP().getEquationComponentD());
			assertEpsilonEquals(-4, getP().getX());
		}
		
		@DisplayName("(double,double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(3, 1, 2, -4);
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
			assertEpsilonEquals(4, getP().getX());
		}
		
		@DisplayName("(double,double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(-3, -1, -2, 4);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4, getP().getEquationComponentD());
			assertEpsilonEquals(4, getP().getX());
		}
		
		@DisplayName("(double,double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(-3, -1, -2, -4);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
			assertEpsilonEquals(-4, getP().getX());
		}
	
		@DisplayName("(double,double,double,double,double,double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyzxyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(3, 2, 1, -42, 18, 4, -6, 1, 57);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-15.0, getP().getEquationComponentD());
			assertEpsilonEquals(-15.0, getP().getX());
		}
		
		@DisplayName("(double,double,double,double,double,double,double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyzxyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(3, 2, 1, 3, 2, 1, 3, 2, 1);
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-3., getP().getEquationComponentD());
			assertEpsilonEquals(3, getP().getX());
		}
	
		@DisplayName("(Point3D,Point3D,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(3, 2, 1), createPoint(-42, 18, 4), createPoint(-6, 1, 57));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-15.0, getP().getEquationComponentD());
			assertEpsilonEquals(-15.0, getP().getX());
		}
		
		@DisplayName("(Point3D,Point3D,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(3, 2, 1), createPoint(3, 2, 1), createPoint(3, 2, 1));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-3., getP().getEquationComponentD());
			assertEpsilonEquals(3, getP().getX());
		}
	
		@DisplayName("(Point3D,Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(3, 2, 1), createVector(-42, 1, 0));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(3, getP().getEquationComponentD());
			assertEpsilonEquals(3, getP().getX());
		}
		
		@DisplayName("(Point3D,Vector3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(3, 2, 1), createVector(42, 1, 0));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-3, getP().getEquationComponentD());
			assertEpsilonEquals(3, getP().getX());
		}
	
		@DisplayName("(Point3D,Vector3D,Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(3, 2, 1), createVector(-45, 16, 3), createVector(-9, -1, 56));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(3.0, getP().getEquationComponentD());
			assertEpsilonEquals(3.0, getP().getX());
		}
		
		@DisplayName("(Point3D,Vector3D,Vector3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(3, 2, 1), createVector(-9, -1, 56), createVector(-45, 16, 3));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-3.0, getP().getEquationComponentD());
			assertEpsilonEquals(3.0, getP().getX());
		}
		
		@DisplayName("(Point3D,Vector3D,Vector3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(3, 2, 1), createVector(0, 0, 0), createVector(3, 2, 1));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-3., getP().getEquationComponentD());
			assertEpsilonEquals(3, getP().getX());
		}
		
		@DisplayName("(Point3D,Vector3D,Vector3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(3, 2, 1), createVector(3, 2, 1), createVector(0, 0, 0));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-3., getP().getEquationComponentD());
			assertEpsilonEquals(3, getP().getX());
		}
		
		@DisplayName("(Point3D,Vector3D,Vector3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(3, 2, 1), createVector(0, 0, 0), createVector(0, 0, 0));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-3., getP().getEquationComponentD());
			assertEpsilonEquals(3, getP().getX());
		}
	}

	@DisplayName("-this")
	@Nested
	public class OperatorMinus {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getP().operator_minus();
			assertNotSame(getP(), r);
			assertEpsilonEquals(1., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(0., r.getEquationComponentC());
			assertEpsilonEquals(-1.25, r.getEquationComponentD());
		}
	}

	@DisplayName("this += double")
	@Nested
	public class OperatorAddDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().operator_add(5.69);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(6.94, getP().getEquationComponentD());
		}
	}

	@DisplayName("this -= double")
	@Nested
	public class OperatorRemoveDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().operator_remove(5.69);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-4.44, getP().getEquationComponentD());
		}
	}

	@DisplayName("this + double")
	@Nested
	public class OperatorPlusDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getP().operator_plus(5.69);
			assertNotSame(getP(), r);
			assertEpsilonEquals(-1., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(0., r.getEquationComponentC());
			assertEpsilonEquals(6.94, r.getEquationComponentD());
		}
	}

	@DisplayName("this - double")
	@Nested
	public class OperatorMinusDouble {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getP().operator_minus(5.69);
			assertNotSame(getP(), r);
			assertEpsilonEquals(-1., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(0., r.getEquationComponentC());
			assertEpsilonEquals(-4.44, r.getEquationComponentD());
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
			getP().translate(5.69);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(6.94, getP().getEquationComponentD());
		}

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().translate(569, 5.69, 148);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(570.25, getP().getEquationComponentD());
		}

		@DisplayName("(Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().translate(createVector(569, 5.69, 148));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(570.25, getP().getEquationComponentD());
		}
	}

	@DisplayName("this += Vector3D")
	@Nested
	public class OperatorAddVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().operator_add(createVector(569, 5.69, 148));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(570.25, getP().getEquationComponentD());
		}
	}

	@DisplayName("this -= Vector3D")
	@Nested
	public class OperatorRemoveVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().operator_remove(createVector(569, 5.69, 148));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-567.75, getP().getEquationComponentD());
		}
	}

	@DisplayName("this + Vector3D")
	@Nested
	public class OperatorPlusVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getP().operator_plus(createVector(569, 5.69, 148));
			assertNotSame(getP(), r);
			assertEpsilonEquals(-1., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(0., r.getEquationComponentC());
			assertEpsilonEquals(570.25, r.getEquationComponentD());
		}
	}

	@DisplayName("this - Vector3D")
	@Nested
	public class OperatorMinusVector3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = getP().operator_minus(createVector(569, 5.69, 148));
			assertNotSame(getP(), r);
			assertEpsilonEquals(-1., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(0., r.getEquationComponentC());
			assertEpsilonEquals(-567.75, r.getEquationComponentD());
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
			assertSame(plane, getP().normalize());
		}
	}

	@DisplayName("setPivot")
	@Nested
	public class SetPivot {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(0, 0, 0);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0, getP().getEquationComponentD());
		}

		@DisplayName("(double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(3, 2, 1);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(3, getP().getEquationComponentD());
		}

		@DisplayName("(double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(63, 5, 4);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(63, getP().getEquationComponentD());
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(createPoint(0, 0, 0));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(0, getP().getEquationComponentD());
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(createPoint(3, 2, 1));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(3, getP().getEquationComponentD());
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(createPoint(63, 5, 4));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(63, getP().getEquationComponentD());
		}
	}

	@DisplayName("this * Transform3D")
	@Nested
	public class OperatorMultiplyTransfor3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation X, no rotation
			var tr = new Transform3D();
			tr.setTranslation(5.69, 0, 0);
			var r = getP().operator_multiply(tr);
			assertNotSame(getP(), r);
			assertEpsilonEquals(-1., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(0., r.getEquationComponentC());
			assertEpsilonEquals(6.94, r.getEquationComponentD());
			assertEpsilonEquals(6.94, r.getX());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation XYZ, no rotation
			var tr = new Transform3D();
			tr.setTranslation(3.69, 6, 5);
			var r = getP().operator_multiply(tr);
			assertNotSame(getP(), r);
			assertEpsilonEquals(-1., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(0., r.getEquationComponentC());
			assertEpsilonEquals(4.94, r.getEquationComponentD());
			assertEpsilonEquals(4.94, r.getX());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation X, Rotation with quadrant flip
			var tr = new Transform3D();
			tr.makeRotationMatrix(-1.,  2.,  1., Math.PI / 7.);
			tr.setTranslation(5.69, 0, 0);
			var r = getP().operator_multiply(tr);
			assertNotSame(getP(), r);
			assertEpsilonEquals(1., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(0., r.getEquationComponentC());
			assertEpsilonEquals(-6.94, r.getEquationComponentD());
			assertEpsilonEquals(6.94, r.getX());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation XYZ, Rotation without quadrant flip
			var tr = new Transform3D();
			tr.makeRotationMatrix(-1.,  0.,  0., Math.PI / 7.);
			tr.setTranslation(3.69, 6, 5);
			var r = getP().operator_multiply(tr);
			assertNotSame(getP(), r);
			assertEpsilonEquals(-1., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(0., r.getEquationComponentC());
			assertEpsilonEquals(4.94, r.getEquationComponentD());
			assertEpsilonEquals(4.94, r.getX());
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
			// Translation X, no rotation
			var tr = new Transform3D();
			tr.setTranslation(5.69, 0, 0);
			getP().transform(tr);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(6.94, getP().getEquationComponentD());
			assertEpsilonEquals(6.94, getP().getX());
		}

		@DisplayName("(Transform3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transform_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation XYZ, no rotation
			var tr = new Transform3D();
			tr.setTranslation(3.69, 6, 5);
			getP().transform(tr);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4.94, getP().getEquationComponentD());
			assertEpsilonEquals(4.94, getP().getX());
		}

		@DisplayName("(Transform3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transform_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation X, Rotation with quadrant flip
			var tr = new Transform3D();
			tr.makeRotationMatrix(-1.,  2.,  1., Math.PI / 7.);
			tr.setTranslation(5.69, 0, 0);
			getP().transform(tr);
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-6.94, getP().getEquationComponentD());
			assertEpsilonEquals(6.94, getP().getX());
		}

		@DisplayName("(Transform3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transform_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation XYZ, Rotation without quadrant flip
			var tr = new Transform3D();
			tr.makeRotationMatrix(-1.,  0.,  0., Math.PI / 7.);
			tr.setTranslation(3.69, 6, 5);
			getP().transform(tr);
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4.94, getP().getEquationComponentD());
			assertEpsilonEquals(4.94, getP().getX());
		}

		@DisplayName("(Transform3D,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation X, no rotation
			var tr = new Transform3D();
			tr.setTranslation(5.69, 0, 0);
			getP().transform(tr, createPoint(-42, 6, -45));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(6.94, getP().getEquationComponentD());
			assertEpsilonEquals(6.94, getP().getX());
		}

		@DisplayName("(Transform3D,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation XYZ, no rotation
			var tr = new Transform3D();
			tr.setTranslation(3.69, 6, 5);
			getP().transform(tr, createPoint(18, -5, -2));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4.94, getP().getEquationComponentD());
			assertEpsilonEquals(4.94, getP().getX());
		}

		@DisplayName("(Transform3D,Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation X, Rotation with quadrant flip
			var tr = new Transform3D();
			tr.makeRotationMatrix(-1.,  2.,  1., Math.PI / 7.);
			tr.setTranslation(5.69, 0, 0);
			getP().transform(tr, createPoint(0, 6.5, 5));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-6.94, getP().getEquationComponentD());
			assertEpsilonEquals(6.94, getP().getX());
		}

		@DisplayName("(Transform3D,Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation XYZ, Rotation without quadrant flip
			var tr = new Transform3D();
			tr.makeRotationMatrix(-1.,  0., 0., Math.PI / 7.);
			tr.setTranslation(3.69, 6, 5);
			getP().transform(tr, createPoint(0.5, -1, 9));
			assertEpsilonEquals(-1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(4.94, getP().getEquationComponentD());
			assertEpsilonEquals(4.94, getP().getX());
		}
	}

	@DisplayName("getProjection")
	@Nested
	public class GetProjection {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1.25, 0, 0), getP().getProjection(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1.25, -458, 125), getP().getProjection(createPoint(-145, -458, 125)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1.25, 458, -145), getP().getProjection(createPoint(18, 458, -145)));
		}

		private static Stream<Arguments> providePointsArguments() {
			final var arguments = new ArrayList<Arguments>();
			final var rnd = new Random();
			for (final CoordinateSystem3D s1 : CoordinateSystem3D.values()) {
				for (int i = 0; i < 100; ++i) {
					var x = rnd.nextDouble(1000) * (rnd.nextBoolean() ? 1. : -1.);
					var y = rnd.nextDouble(1000) * (rnd.nextBoolean() ? 1. : -1.);
					var z = rnd.nextDouble(1000) * (rnd.nextBoolean() ? 1. : -1.);
					arguments.add(Arguments.of(s1, x, y, z));
				}
			}
			return arguments.stream();
		}
	
		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("providePointsArguments")
		public final void point_4(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1.25, y, z), getP().getProjection(createPoint(x, y, z)));
		}
	
		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1.25, 0, 0), getP().getProjection(0, 0, 0));
		}
		
		@DisplayName("(double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1.25, -458, 125), getP().getProjection(-145, -458, 125));
		}
		
		@DisplayName("(double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1.25, 458, -145), getP().getProjection(18, 458, -145));
		}
		
		@DisplayName("(double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("providePointsArguments")
		public final void doubledoubledouble_4(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(1.25, y, z), getP().getProjection(x, y, z));
		}
	}

	@DisplayName("rotate")
	@Nested
	public class Rotate {

		@DisplayName("(double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzw_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation with quadrant flip
			getP().rotate(0.,  1., 1., 1.2 * Math.PI);
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getX());
		}

		@DisplayName("(double,double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzw_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation without quadrant flip
			getP().rotate(0.,  1., 0., 1.2 * Math.PI + Math.PI / 7.);
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getX());
		}

		@DisplayName("(Quaternion) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternion_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation with quadrant flip
			getP().rotate(createAxisAngle(0.,  1., 1., 1.2 * Math.PI));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getX());
		}

		@DisplayName("(Quaternion) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternion_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation without quadrant flip
			getP().rotate(createAxisAngle(0.,  1., 0., 1.2 * Math.PI + Math.PI / 7.));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getX());
		}

		@DisplayName("(Quaternion,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternionpoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation with quadrant flip
			getP().rotate(createAxisAngle(0.,  1., 1., 1.2 * Math.PI), createPoint(3, 2, 1));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getX());
		}

		@DisplayName("(Quaternion,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternionpoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation without quadrant flip
			getP().rotate(createAxisAngle(0.,  1., 0., 1.2 * Math.PI + Math.PI / 7.), createPoint(3, 2, 1));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getX());
		}

		@DisplayName("(Vector3D,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vectordouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation with quadrant flip
			getP().rotate(createVector(0.,  1., 1.), 1.2 * Math.PI);
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getX());
		}

		@DisplayName("(Vector3D,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vectordouble_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation without quadrant flip
			getP().rotate(createVector(0.,  1., 0.), 1.2 * Math.PI + Math.PI / 7.);
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getX());
		}

		@DisplayName("(Vector3D,double,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vectordoublepoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation with quadrant flip
			getP().rotate(createVector(0.,  1., 1.), 1.2 * Math.PI, createPoint(3, 2, 1));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getX());
		}

		@DisplayName("(Vector3D,double,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vectordoublepoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation without quadrant flip
			getP().rotate(createVector(0.,  1., 0.), 1.2 * Math.PI + Math.PI / 7., createPoint(0, 2, 1));
			assertEpsilonEquals(1., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(0., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getX());
		}
	}

	@DisplayName("this * Quaternion")
	@Nested
	public class OperatorMultiplyQuaternion {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation with quadrant flip
			var r = getP().operator_multiply(createAxisAngle(0.,  1., 1., 1.2 * Math.PI));
			assertNotSame(getP(), r);
			assertEpsilonEquals(1., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(0., r.getEquationComponentC());
			assertEpsilonEquals(-1.25, r.getEquationComponentD());
			assertEpsilonEquals(1.25, r.getX());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation without quadrant flip
			var r = getP().operator_multiply(createAxisAngle(0.,  1., 0., Math.PI / 7.));
			assertNotSame(getP(), r);
			assertEpsilonEquals(-1., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(0., r.getEquationComponentC());
			assertEpsilonEquals(1.25, r.getEquationComponentD());
			assertEpsilonEquals(1.25, r.getX());
		}
	}


	@DisplayName("this == Plane3D")
	@Nested
	public class OperatorEqualsPlane3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_equals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_equals(null));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_equals(getP()));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_equals(createPlaneYZ(1.25, false)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_equals(createPlaneYZ(1.25, true)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_equals(createPlane(-1, 0, 0, 1.25)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_equals(createPlane(-1, 0, 0, -1.25)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_equals(createPlane(1, 0, 0, -1.25)));
		}
	}

	@DisplayName("this != Plane3D")
	@Nested
	public class OperatorNotEqualsPlane3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_notEquals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_notEquals(getP()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_notEquals(createPlaneYZ(1.25, false)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_notEquals(createPlaneYZ(1.25, true)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_notEquals(createPlane(-1, 0, 0, 1.25)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_notEquals(createPlane(-1, 0, 0, -1.25)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_notEquals(createPlane(1, 0, 0, -1.25)));
		}
	}

	@DisplayName("findsClosestPointRectangleYZSegment")
	@Nested
	public class FindsClosestPointRectangleYZSegment {
		
		private InnerComputationPoint3D onSegment;
		private InnerComputationPoint3D onPlane;

		@BeforeEach
		public void setUp() {
			onSegment = new InnerComputationPoint3D();
			onPlane = new InnerComputationPoint3D();
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 1: Segment entirely below and outside the rectangle.
			// Rectangle [1,2]x[2,3] at x=3. Segment (0,0,0)->(-5,0.5,1).
			// Both points are below x=3 and outside YZ bounds.
			// Closest segment point = S1=(0,0,0), closest rect point = corner (3,2,1).
			// sqDist = (0-3)^2+(0-1)^2+(0-2)^2 = 9+4+1 = 14.
			assertEpsilonEquals(14., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					2., 1., 3., 2., 3.,
					0., 0., 0., -5., 0.5, 1.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(0., 0., 0.), onSegment);
			assertEpsilonEquals(createPoint(3., 2., 1.), onPlane);
		}
		
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 2: Segment pierces the rectangle interior - distance must be 0.
			// Rectangle [-1,1]x[-1,1] at x=0. Segment (1,0,0)->(-1,0,0).
			// Parametric intersection at t=0.5 -> point (0,0,0) inside rectangle.
			assertEpsilonEquals(0., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					-1., -1., 1., 1., 0.,
					1., 0., 0., -1., 0., 0.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(0., 0., 0.), onSegment);
			assertEpsilonEquals(createPoint(0., 0., 0.), onPlane);
		}
		
		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 3: Segment pierces the rectangle at a corner - distance must be 0.
			// Rectangle [0,2]x[0,2] at x=0. Segment (1,0,0)->(-1,0,0).
			// Intersection at t=0.5 -> point (0,0,0) which is exactly corner (0,0,0).
			assertEpsilonEquals(0., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					0., 0., 2., 2., 0.,
					1., 0., 0., -1., 0., 0.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(0., 0., 0.), onSegment);
			assertEpsilonEquals(createPoint(0., 0., 0.), onPlane);
		}
		
		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 4: Segment pierces the plane but OUTSIDE the rectangle bounds.
			// Rectangle [1,2]x[1,2] at x=0. Segment (1,5,5)->(-1,5,5).
			// Intersection at (0,5,5) - outside YZ. Closest rect point = corner (0,2,2).
			// Closest segment point is where the segment is closest to (0,2,2).
			// The segment is vertical at y=5,z=5. Closest point on segment to (0,2,2) is (0,5,5).
			// sqDist = (0-0)^2+(5-2)^2+(5-2)^2 = 9+9 = 18.
			assertEpsilonEquals(18., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					1., 1., 2., 2., 0.,
					1., 5., 5., -1., 5., 5.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(0., 5., 5.), onSegment);
			assertEpsilonEquals(createPoint(0., 2., 2.), onPlane);
		}
		
		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 5: Segment coplanar with the rectangle plane, inside rectangle bounds - distance 0.
			// Rectangle [-1,1]x[-1,1] at x=0. Segment (0,0,-0.5)->(0,0,0.5) lies in x=0.
			// Both points are inside the rectangle -> distance = 0.
			assertEpsilonEquals(0., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					-1., -1., 1., 1., 0.,
					0., 0., -0.5, 0., 0., 0.5,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(0., 0., -.5), onSegment);
			assertEpsilonEquals(createPoint(0., 0., -.5), onPlane);
		}
		
		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 6: Segment coplanar with the rectangle plane, entirely outside rectangle bounds.
			// Rectangle [1,2]x[1,2] at x=0. Segment (0,0,4)->(0,0,5).
			// Closest rect point = (0,1,2) (corner), closest segment point = (0,0,4) (S1).
			// sqDist = (0-0)^2+(0-1)^2+(4-2)^2 = 4+1 = 5.
			assertEpsilonEquals(5., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					1., 1., 2., 2., 0.,
					0., 0., 4., 0., 0., 5.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(0., 0., 4.), onSegment);
			assertEpsilonEquals(createPoint(0., 1., 2.), onPlane);
		}
		
		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 7: Segment parallel to the YZ plane (no x change), hovering above.
			// Rectangle [-1,1]x[-1,1] at x=0. Segment (3,0,-0.5)->(3,0,0.5).
			// Infinite number of solution points on segment -> select the first found (3,0,-0.5).
			// Closest point on the rectangle surface (0, 0, -0.5).
			// sqDist = 0^2+0^2+3^2 = 9.
			assertEpsilonEquals(9., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					-1., -1., 1., 1., 0.,
					3., 0., -0.5, 3., 0., 0.5,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(3., 0., -.5), onSegment);
			assertEpsilonEquals(createPoint(0., 0., -.5), onPlane);
		}
		
		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 8: Segment parallel to YZ plane, outside YZ bounds, hovering above.
			// Rectangle [0,1]x[0,1] at x=0. Segment (2,0.5,-3)->(2,0.5,-1).
			// Closest rect point = (0,0.5,0) (edge z=0 point clamped to rect).
			// Closest segment point = (2,0.5,-1) = S2 (closest end to rect).
			// sqDist = (2-0)^2+(0.5-0.5)^2+(-1-0)^2 = 4+0+1 = 5.
			assertEpsilonEquals(5., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					0., 0., 1., 1., 0.,
					2., 0.5, -3., 2., 0.5, -1.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(2., 0.5, -1.), onSegment);
			assertEpsilonEquals(createPoint(0., 0.5, 0.), onPlane);
		}
		
		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 9: Degenerate segment (both endpoints coincide) - point above interior.
			// Rectangle [-1,1]x[-1,1] at x=0. Segment (4,0,0)->(4,0,0) (point).
			// Closest rect point = (0,0,0), sqDist = 4^2+0^2+0^2 = 16.
			assertEpsilonEquals(16., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					-1., -1., 1., 1., 0.,
					4., 0., 0., 4., 0., 0.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(4., 0., 0.), onSegment);
			assertEpsilonEquals(createPoint(0., 0., 0.), onPlane);
		}
		
		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 10: Degenerate segment - point above a corner of the rectangle.
			// Rectangle [0,2]x[0,2] at x=0. Segment (3,0,0)->(3,0,0).
			// Closest rect point = (0,0,0) (corner), sqDist = 3^2+0^2+0^2 = 9.
			assertEpsilonEquals(9., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					0., 0., 2., 2., 0.,
					3., 0., 0., 3., 0., 0.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(3., 0., 0.), onSegment);
			assertEpsilonEquals(createPoint(0., 0., 0.), onPlane);
		}
		
		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 11: Segment endpoint on the rectangle plane, YZ inside rect - dist 0.
			// Rectangle [-1,1]x[-1,1] at x=0. Segment (0,0,0)->(5,0,0).
			// S1=(0,0,0) is exactly on the plane and inside the rect -> distance = 0.
			assertEpsilonEquals(0., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					-1., -1., 1., 1., 0.,
					0., 0., 0., 5., 0., 0.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(0., 0., 0.), onSegment);
			assertEpsilonEquals(createPoint(0., 0., 0.), onPlane);
		}
		
		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 12: Segment endpoint exactly on a rect corner - dist 0.
			// Rectangle [0,2]x[0,2] at x=0. Segment (0,2,2)->(5,5,5).
			// S1=(0,2,2) is exactly at corner (0,2,2) -> distance = 0.
			assertEpsilonEquals(0., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					0., 0., 2., 2., 0.,
					0., 2., 2., 5., 5., 5.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(0., 2., 2.), onSegment);
			assertEpsilonEquals(createPoint(0., 2., 2.), onPlane);
		}
		
		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 13: Segment entirely above the rectangle (no x change, x > rx).
			// Rectangle [0,4]x[0,4] at x=5. Segment (10,1,1)->(10,3,3).
			// Closest rect point = interior projection of midpoint (10,2,2) -> (5,2,2).
			// sqDist = (10-5)^2+(2-2)^2+(2-2)^2 = 25.
			assertEpsilonEquals(25., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					0., 0., 4., 4., 5.,
					10., 1., 1., 10., 3., 3.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(10., 1., 1.), onSegment);
			assertEpsilonEquals(createPoint(5., 1., 1.), onPlane);
		}
		
		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 14: Segment above rect, closest point is near an edge (not corner).
			// Rectangle [0,4]x[0,4] at x=0. Segment (2,-3,2)->(2,3,2) spans across
			// the rect in Y. Closest segment point to plane interior is (2,0,2).
			// Closest rect point = (0,0,2) on bottom edge.
			// sqDist = (2-0)^2+(0-0)^2+(2-2)^2 = 4.
			assertEpsilonEquals(4., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					0., 0., 4., 4., 0.,
					2., -3., 2., 2., 3., 2.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(2., 0., 2.), onSegment);
			assertEpsilonEquals(createPoint(0., 0., 2.), onPlane);
		}
		
		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 15: Segment approaches the rectangle from one side in 3D (skewed).
			// Rectangle [0,4]x[0,4] at x=0. Segment (3,2,2)->(-3,2,2).
			// Segment crosses the plane at (0,2,2) which is inside the rect -> dist=0.
			assertEpsilonEquals(0., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					0., 0., 4., 4., 0.,
					3., 2., 2., -3., 2., 2.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(0., 2., 2.), onSegment);
			assertEpsilonEquals(createPoint(0., 2., 2.), onPlane);
		}
		
		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// CASE 16: Segment entirely below the rectangle plane (x1<rx, x2<rx), outside YZ.
			// Rectangle [1,3]x[1,3] at x=5. Segment (-1,0,0)->(-3,0,0).
			// Both endpoints below plane, both outside YZ. Closest seg point = S1=(-1,0,0).
			// Closest rect point = corner (5,1,1).
			// sqDist = (-1-5)^2+(0-1)^2+(0-1)^2 = 36+1+1 = 38.
			assertEpsilonEquals(38., PlaneYZ3afp.findsClosestPointToRectangleYZSegment(
					1., 1., 3., 3., 5.,
					-1., 0., 0., -3., 0., 0.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(-1., 0., 0.), onSegment);
			assertEpsilonEquals(createPoint(5., 1., 1.), onPlane);
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
			assertFalse(getP().equals(null));
		}

		@DisplayName("(Plane3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().equals(getP()));
		}

		@DisplayName("(Plane3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(1, 0, 0, 0)));
		}

		@DisplayName("(Plane3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(-1, 0, 0, 0)));
		}

		@DisplayName("(Plane3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(0, 1, 0, 0)));
		}

		@DisplayName("(Plane3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(0, -1, 0, 0)));
		}

		@DisplayName("(Plane3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(0, 0, 1, 0)));
		}

		@DisplayName("(Plane3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(0, 0, -1, 0)));
		}

		@DisplayName("(Plane3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(1, 0, 0, 1.25)));
		}

		@DisplayName("(Plane3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().equals(createPlane(-1, 0, 0, 1.25)));
		}

		@DisplayName("(Plane3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(0, 1, 0, 1.25)));
		}

		@DisplayName("(Plane3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(0, -1, 0, 1.25)));
		}

		@DisplayName("(Plane3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(0, 0, 1, 1.25)));
		}

		@DisplayName("(Plane3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(0, 0, -1, 1.25)));
		}

		@DisplayName("(Plane3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(1, 0, 0, -1.25)));
		}

		@DisplayName("(Plane3D) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(-1, 0, 0, -1.25)));
		}

		@DisplayName("(Plane3D) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(0, 1, 0, -1.25)));
		}

		@DisplayName("(Plane3D) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(0, -1, 0, -1.25)));
		}

		@DisplayName("(Plane3D) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(0, 0, 1, -1.25)));
		}

		@DisplayName("(Plane3D) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().equals(createPlane(0, 0, -1, -1.25)));
		}
	}

}
