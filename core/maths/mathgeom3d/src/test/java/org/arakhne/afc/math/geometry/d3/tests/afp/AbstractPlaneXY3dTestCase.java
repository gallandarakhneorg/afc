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
import org.arakhne.afc.math.geometry.d3.afp.PlaneXY3afp;
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
public abstract class AbstractPlaneXY3dTestCase<T extends PlaneXY3afp<T, ?, ?, ?, ?>, B extends AlignedBox3afp<?, ?, ?, ?, ?, B>> extends AbstractPlane3DTestCase<T, B> {

	private T plane;

	protected final T getP() {
		return this.plane;
	}

	protected abstract T createTestPlane(double z, boolean positive);

	@BeforeEach
	public final void setUp() {
		super.setUp();
		this.plane = createTestPlane(1.25, false);
	}

	@AfterEach
	public final void tearDown() throws Exception {
		this.plane = null;
	}

	@DisplayName("toGeogebra")
	@Nested
	public class ToGeogebra {

		@DisplayName("With negative normal")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void toGeogebra_negative(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals("0.0*x+0.0*y-1.0*z+1.25=0.0", getP().toGeogebra());
		}

		@DisplayName("With positive normal")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void toGeogebra_positive(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			PlaneXY3d np = new PlaneXY3d(true, 1.25);
			assertEquals("0.0*x+0.0*y+1.0*z-1.25=0.0", np.toGeogebra());
		}
	}

	@DisplayName("getZ")
	@Nested
	public class GetZ {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.25, getP().getZ());
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
			assertEpsilonEquals(0., getP().getEquationComponentA());
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
			assertEpsilonEquals(0., getP().getEquationComponentB());
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
			assertEpsilonEquals(-1., getP().getEquationComponentC());
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
			assertEpsilonEquals(1.25, getP().getEquationComponentD());
		}
	}

	@DisplayName("setZ")
	@Nested
	public class SetZ {

		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setZ(123.589);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(123.589, getP().getEquationComponentD());
			assertEpsilonEquals(createPoint(0, 0, 123.589), getP().getPivot());
		}

		@DisplayName("(double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setZ(-453.154);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(-453.154, getP().getEquationComponentD());
			assertEpsilonEquals(createPoint(0, 0, -453.154), getP().getPivot());
		}

		@DisplayName("(double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			getP().setZ(123.589);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-123.589, getP().getEquationComponentD());
			assertEpsilonEquals(createPoint(0, 0, 123.589), getP().getPivot());
		}

		@DisplayName("(double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			getP().setZ(-453.154);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(453.154, getP().getEquationComponentD());
			assertEpsilonEquals(createPoint(0, 0, -453.154), getP().getPivot());
		}
	}

	@DisplayName("setPositive")
	@Nested
	public class SetPositive {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(createPoint(0, 0, 1.25), getP().getPivot());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(1.25, getP().getEquationComponentD());
			assertEpsilonEquals(createPoint(0, 0, 1.25), getP().getPivot());
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
			assertEpsilonEquals(createVector(0., 0., -1.), getP().getNormal());
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
			assertEpsilonZero(getP().getNormalX());
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
			assertEpsilonZero(getP().getNormalY());
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
			assertEpsilonEquals(-1, getP().getNormalZ());
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
			assertEpsilonEquals(createPoint(0., 0., 1.25), getP().getPivot());
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
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
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
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(0., getP().getEquationComponentD());
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
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			getP().negate();
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(1.25, getP().getEquationComponentD());
		}
	}

	@DisplayName("calculatesPlaneXYPlaneDistance")
	@Nested
	public class CalculatesPlaneXYPlaneDistance {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.25, PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 0., 0., 1., -4.5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 0., 0., 1., -1.25));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-5.75, PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 0., 0., 1., 4.5));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-5.75, PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 0., 0., -1., -4.5));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 0., 0., -1., 1.25));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.25, PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 0., 0., -1., 4.5));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3.25, PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 0., 0., 1., -4.5));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 0., 0., 1., -1.25));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.75, PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 0., 0., 1., 4.5));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.75, PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 0., 0., -1., -4.5));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 0., 0., -1., 1.25));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3.25, PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 0., 0., -1., 4.5));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 1., 0., 1., -4.5));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 1., 0., 1., -4.5));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 0., 1., 1., -4.5));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 0., 1., 1., -4.5));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 1., 1., 0., -4.5));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 1., 1., 0., -4.5));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 1., 1., 1., -4.5));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 1., 1., 1., -4.5));
		}
	}

	@DisplayName("calculatesPlaneXYPointDistance")
	@Nested
	public class CalculatesPlaneXYPointDistance {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-1.25, PlaneXY3afp.calculatesPlaneXYPointDistance(true, 1.25, 0, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPointDistance(true, 1.25, 0, 0, 1.25));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-11.25, PlaneXY3afp.calculatesPlaneXYPointDistance(true, 1.25, 0, 0, -10));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8.75, PlaneXY3afp.calculatesPlaneXYPointDistance(true, 1.25, 0, 0, 10));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.25, PlaneXY3afp.calculatesPlaneXYPointDistance(false, 1.25, 0, 0, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPointDistance(false, 1.25, 0, 0, 1.25));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.25, PlaneXY3afp.calculatesPlaneXYPointDistance(false, 1.25, 0, 0, -10));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-8.75, PlaneXY3afp.calculatesPlaneXYPointDistance(false, 1.25, 0, 0, 10));
		}
	}

	@DisplayName("findsPlaneXYSegmentIntersection")
	@Nested
	public class FindsPlaneXYSegmentIntersection {

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
			assertFalse(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					true, 1.25,
					32.5, -5.6, -51.2,
					47.1, -7.9, .5,
					p));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					true, 1.25,
					32.5, -5.6, 47.2,
					47.1, -7.9, 2.,
					p));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					true, 1.25,
					32.5, -5.6, 0.,
					47.1, -7.9, 2.,
					p));
			assertEpsilonEquals(createPoint(41.625, -7.0375, 1.25), p);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					true, 1.25,
					32.5, -5.6, 2.,
					47.1, -7.9, 0.,
					p));
			assertEpsilonEquals(createPoint(37.975, -6.4625, 1.25), p);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					true, 1.25,
					32.5, -5.6, 1.25,
					47.1, -7.9, 0.,
					p));
			assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					true, 1.25,
					32.5, -5.6, 0.,
					47.1, -7.9, 1.25,
					p));
			assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					true, 1.25,
					32.5, -5.6, 1.25,
					47.1, -7.9, 2.,
					p));
			assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					true, 1.25,
					32.5, -5.6, 2.,
					47.1, -7.9, 1.25,
					p));
			assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					true, 1.25,
					32.5, -5.6, 1.25,
					47.1, -7.9, 1.25,
					p));
			assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					false, 1.25,
					32.5, -5.6, -51.2,
					47.1, -7.9, .5,
					p));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					false, 1.25,
					32.5, -5.6, 47.2,
					47.1, -7.9, 2.,
					p));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					false, 1.25,
					32.5, -5.6, 0.,
					47.1, -7.9, 2.,
					p));
			assertEpsilonEquals(createPoint(41.625, -7.0375, 1.25), p);
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					false, 1.25,
					32.5, -5.6, 2.,
					47.1, -7.9, 0.,
					p));
			assertEpsilonEquals(createPoint(37.975, -6.4625, 1.25), p);
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					false, 1.25,
					32.5, -5.6, 1.25,
					47.1, -7.9, 0.,
					p));
			assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					false, 1.25,
					32.5, -5.6, 0.,
					47.1, -7.9, 1.25,
					p));
			assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					false, 1.25,
					32.5, -5.6, 1.25,
					47.1, -7.9, 2.,
					p));
			assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					true, 1.25,
					32.5, -5.6, 2.,
					47.1, -7.9, 1.25,
					p));
			assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
					false, 1.25,
					32.5, -5.6, 1.25,
					47.1, -7.9, 1.25,
					p));
			assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);
		}
	}

	@DisplayName("calculatesPlaneXYSegmentIntersectionFactor")
	@Nested
	public class CalculatesPlaneXYSegmentIntersectionFactor {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNaN(PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
					true, 1.25,
					32.5, -5.6, -51.2,
					47.1, -7.9, .5));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNaN(PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
					true, 1.25,
					32.5, -5.6, 47.2,
					47.1, -7.9, 2.));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.625,
					PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
							true, 1.25,
							32.5, -5.6, 0.,
							47.1, -7.9, 2.));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.375,
					PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
							true, 1.25,
							32.5, -5.6, 2.,
							47.1, -7.9, 0.));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.,
					PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
							true, 1.25,
							32.5, -5.6, 1.25,
							47.1, -7.9, 0.));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.,
					PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
							true, 1.25,
							32.5, -5.6, 0.,
							47.1, -7.9, 1.25));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.,
					PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
							true, 1.25,
							32.5, -5.6, 1.25,
							47.1, -7.9, 2.));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.,
					PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
							true, 1.25,
							32.5, -5.6, 2.,
							47.1, -7.9, 1.25));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertInfinity(PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
					true, 1.25,
					32.5, -5.6, 1.25,
					47.1, -7.9, 1.25));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNaN(PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
					false, 1.25,
					32.5, -5.6, -51.2,
					47.1, -7.9, .5));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNaN(PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
					false, 1.25,
					32.5, -5.6, 47.2,
					47.1, -7.9, 2.));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.625,
					PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
							false, 1.25,
							32.5, -5.6, 0.,
							47.1, -7.9, 2.));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.375,
					PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
							false, 1.25,
							32.5, -5.6, 2.,
							47.1, -7.9, 0.));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.,
					PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
							false, 1.25,
							32.5, -5.6, 1.25,
							47.1, -7.9, 0.));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.,
					PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
							false, 1.25,
							32.5, -5.6, 0., 
							47.1, -7.9, 1.25));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.,
					PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
							false, 1.25,
							32.5, -5.6, 1.25,
							47.1, -7.9, 2.));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.,
					PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
							true, 1.25,
							32.5, -5.6, 2.,
							47.1, -7.9, 1.25));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertInfinity(PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
					false, 1.25,
					32.5, -5.6, 1.25,
					47.1, -7.9, 1.25));
		}
	}

	@DisplayName("classifiesPlaneXYAlignedBox")
	@Nested
	public class ClassifiesPlaneXYAlignedBox {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYAlignedBox(true, 1.25, 0, 0, 0, 1, 1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYAlignedBox(true, 1.25, 0, 0, 0, 1, 1, 1.25));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYAlignedBox(true, 1.25, 0, 0, 0, 1, 1, 2));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYAlignedBox(true, 1.25, 2, 2, 2, 3, 3, 3));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYAlignedBox(true, 1.25, 2, 2, 1.25, 3, 3, 3));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYAlignedBox(true, 1.25, 2, 2, 1, 3, 3, 3));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYAlignedBox(false, 1.25, 0, 0, 0, 1, 1, 1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYAlignedBox(false, 1.25, 0, 0, 0, 1, 1, 1.25));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYAlignedBox(false, 1.25, 0, 0, 0, 1, 1, 2));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYAlignedBox(false, 1.25, 2, 2, 2, 3, 3, 3));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYAlignedBox(false, 1.25, 2, 2, 1.25, 3, 3, 3));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYAlignedBox(false, 1.25, 2, 2, 1, 3, 3, 3));
		}
	}

	@DisplayName("classifiesPlaneXYPlane")
	@Nested
	public class ClassifiesPlaneXYPlane {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 1., -3., 4., -4.));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., 1., 4.));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., 1., -4.));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., -1., -4.));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., -1., 4.));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., 1., 6.));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., 1., -6.));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., -1., -6.));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., -1., 6.));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 1., -3., 4., -4.));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., 1., 4.));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., 1., -4.));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., -1., -4.));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., -1., 4.));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., 1., 6.));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., 1., -6.));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., -1., -6.));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., -1., 6.));
		}
	}

	@DisplayName("classifiesPlaneXYPoint")
	@Nested
	public class ClassifiesPlaneXYPoint {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPoint(true, 1.25, 0, 0, 0));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPoint(true, 1.25, 0, 0, 1.25));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPoint(true, 1.25, 0, 0, -10));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPoint(true, 1.25, 0, 0, 10));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPoint(false, 1.25, 0, 0, 0));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPoint(false, 1.25, 0, 0, 1.25));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPoint(false, 1.25, 0, 0, -10));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPoint(false, 1.25, 0, 0, 10));
		}
	}

	@DisplayName("classifiesPlaneXYSegment")
	@Nested
	public class ClassifiesPlaneXYSegment {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSegment(true, 1.25, 0, 0, 0, 1, 1, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSegment(true, 1.25, 0, 0, 0, 1, 1, 1.25));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSegment(true, 1.25, 0, 0, 0, 1, 1, 2));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSegment(true, 1.25, 3, 3, 3, 2, 2, 2));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSegment(true, 1.25, 3, 3, 3, 2, 2, 1.25));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSegment(true, 1.25, 3, 3, 3, 2, 2, 1));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSegment(false, 1.25, 0, 0, 0, 1, 1, 1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSegment(false, 1.25, 0, 0, 0, 1, 1, 1.25));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSegment(false, 1.25, 0, 0, 0, 1, 1, 2));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSegment(false, 1.25, 3, 3, 3, 2, 2, 2));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSegment(false, 1.25, 3, 3, 3, 2, 2, 1.25));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSegment(false, 1.25, 3, 3, 3, 2, 2, 1));
		}
	}

	@DisplayName("classifiesPlaneXYSphere")
	@Nested
	public class ClassifiesPlaneXYSphere {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSphere(true, 1.25, 0, 0, 0, 1));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSphere(true, 1.25, 0, 0, 0, 1.25));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSphere(true, 1.25, 0, 0, 0, 2));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSphere(true, 1.25, 0, 0, 3, 1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSphere(true, 1.25, 0, 0, 3, 1.75));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSphere(true, 1.25, 0, 0, 3, 2));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSphere(false, 1.25, 0, 0, 0, 1));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSphere(false, 1.25, 0, 0, 0, 1.25));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSphere(false, 1.25, 0, 0, 0, 2));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSphere(false, 1.25, 0, 0, 3, 1));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSphere(false, 1.25, 0, 0, 3, 1.75));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSphere(false, 1.25, 0, 0, 3, 2));
		}
	}

	@DisplayName("classifies")
	@Nested
	public class Classifies {

		@DisplayName("(Box3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Box3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1.25)));
		}

		@DisplayName("(Box3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 2)));
		}

		@DisplayName("(Box3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(Box3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(2, 2, 1.25, 3, 3, 3)));
		}

		@DisplayName("(Box3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(2, 2, 1, 3, 3, 3)));
		}

		@DisplayName("(Box3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("(Box3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1.25)));
		}

		@DisplayName("(Box3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 2)));
		}

		@DisplayName("(Box3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		}

		@DisplayName("(Box3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(2, 2, 1.25, 3, 3, 3)));
		}

		@DisplayName("(Box3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(2, 2, 1, 3, 3, 3)));
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
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPoint(0, 0, 1.25)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPoint(0, 0, -10)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createPoint(0, 0, 10)));
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
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPoint(0, 0, 1.25)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(createPoint(0, 0, -10)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPoint(0, 0, 10)));
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
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSphere(0, 0, 3, 1)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSphere(0, 0, 3, 1.75)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(0, 0, 3, 2)));
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
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSphere(0, 0, 3, 1)));
		}

		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSphere(0, 0, 3, 1.75)));
		}

		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(0, 0, 3, 2)));
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
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 1.25));
		}

		@DisplayName("(x,y,z) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, -10));
		}

		@DisplayName("(x,y,z) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 10));
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
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 1.25));
		}

		@DisplayName("(x,y,z) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, -10));
		}

		@DisplayName("(x,y,z) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 10));
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
			assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 3, 1));
		}

		@DisplayName("(x,y,z,radius) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 3, 1.75));
		}

		@DisplayName("(x,y,z,radius) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 3, 2));
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
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 3, 1));
		}

		@DisplayName("(x,y,z,radius) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 3, 1.75));
		}

		@DisplayName("(x,y,z,radius) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 3, 2));
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
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 0, 1, 1, 1.25));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 0, 1, 1, 2));
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
			assertSame(PlaneClassification.BEHIND, getP().classifies(2, 2, 1.25, 3, 3, 3));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(2, 2, 1, 3, 3, 3));
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
			assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 0, 1, 1, 1.25));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 0, 1, 1, 2));
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
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(2, 2, 1.25, 3, 3, 3));
		}

		@DisplayName("(x1,y1,z1,x2,y2,z2) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(2, 2, 1, 3, 3, 3));
		}

		@DisplayName("(Plane3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(1., -3., 4., -4.)));
		}

		@DisplayName("(Plane3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(0., 0., 1., -1.25)));
		}

		@DisplayName("(Plane3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(0., 0., 1., 1.25)));
		}

		@DisplayName("(Plane3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(0., 0., -1., 1.25)));
		}

		@DisplayName("(Plane3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(0., 0., -1., -1.25)));
		}

		@DisplayName("(Plane3D) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(0., 0., 1., 6.)));
		}

		@DisplayName("(Plane3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(0., 0., 1., -6.)));
		}

		@DisplayName("(Plane3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(0., 0., -1., -6.)));
		}

		@DisplayName("(Plane3D) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(0., 0., -1., 6.)));
		}

		@DisplayName("(Plane3D) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(1., -3., 4., -4.)));
		}

		@DisplayName("(Plane3D) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(0., 0., 1., -1.25)));
		}

		@DisplayName("(Plane3D) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(0., 0., 1., 1.25)));
		}

		@DisplayName("(Plane3D) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(0., 0., -1., 1.25)));
		}

		@DisplayName("(Plane3D) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(0., 0., -1., -1.25)));
		}

		@DisplayName("(Plane3D) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(0., 0., 1., 6.)));
		}

		@DisplayName("(Plane3D) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(0., 0., 1., -6.)));
		}

		@DisplayName("(Plane3D) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(0., 0., -1., -6.)));
		}

		@DisplayName("(Plane3D) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(0., 0., -1., 6.)));
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
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(0, 0, 0, 1, 1, 1.25)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(0, 0, 0, 1, 1, 2)));
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
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(3, 3, 3, 2, 2, 1.25)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(3, 3, 3, 2, 2, 1)));
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
			assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(0, 0, 0, 1, 1, 1.25)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(0, 0, 0, 1, 1, 2)));
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
			assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(3, 3, 3, 2, 2, 1.25)));
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(3, 3, 3, 2, 2, 1)));
		}
	}

	@DisplayName("findsPlaneXYPlaneIntersection")
	@Nested
	public class FindsPlaneXYPlaneIntersection {

		private Point3D p;
		private Vector3D v;
		private Segment3afp s;
		private Point3D p1;
		private Point3D p2;

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
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0., 1., -4.5, p, v));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0., 1., 4.5, p, v));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0., -1., -4.5, p, v));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0., -1., 4.5, p, v));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0., 1., -1.25, p, v));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0., -1., 1.25, p, v));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, p, v));
			assertEpsilonEquals(createPoint(0.517766953, 0, 1.25), p);
			assertEpsilonColinear(createVector(0., -1., 0.), v);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, p, v));
			assertEpsilonEquals(createPoint(-3.017766953, 0., 1.25), p);
			assertEpsilonColinear(createVector(0., -1., 0.), v);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, p, v));
			assertEpsilonEquals(createPoint(0, 0.517766953, 1.25), p);
			assertEpsilonColinear(createVector(1., 0., 0.), v);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, p, v));
			assertEpsilonEquals(createPoint(0., -3.017766953, 1.25), p);
			assertEpsilonColinear(createVector(1., 0., 0.), v);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, p, v));
			assertEpsilonEquals(createPoint(0.8838834765, 0.8838834765, 1.25), p);
			assertEpsilonColinear(createVector(-1., 1., 0.), v);
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, p, v));
			assertEpsilonEquals(createPoint(-0.8838834765, -0.8838834765, 1.25), p);
			assertEpsilonColinear(createVector(-1., 1., 0.), v);
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, p, v));
			assertEpsilonEquals(createPoint(0.4575317547, 0.4575317547, 1.25), p);
			assertEpsilonColinear(createVector(1., -1., 0.), v);
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, p, v));
			assertEpsilonEquals(createPoint(-1.7075317547, -1.7075317547, 1.25), p);
			assertEpsilonColinear(createVector(1., -1., 0.), v);
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0., 1., -4.5, p, v));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0., 1., 4.5, p, v));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0., -1., -4.5, p, v));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0., -1., 4.5, p, v));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0., 1., -1.25, p, v));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0., -1., 1.25, p, v));
		}

		@DisplayName("#21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_21(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, p, v));
			assertEpsilonEquals(createPoint(0.517766953, 0, 1.25), p);
			assertEpsilonColinear(createVector(0., 1., 0.), v);
		}

		@DisplayName("#22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_22(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, p, v));
			assertEpsilonEquals(createPoint(-3.017766953, 0, 1.25), p);
			assertEpsilonColinear(createVector(0., 1., 0.), v);
		}

		@DisplayName("#23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_23(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, p, v));
			assertEpsilonEquals(createPoint(0., 0.517766953, 1.25), p);
			assertEpsilonColinear(createVector(-1., 0., 0.), v);
		}

		@DisplayName("#24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_24(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, p, v));
			assertEpsilonEquals(createPoint(0., -3.017766953, 1.25), p);
			assertEpsilonColinear(createVector(-1., 0., 0.), v);
		}

		@DisplayName("#25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_25(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, p, v));
			assertEpsilonEquals(createPoint(0.8838834765, 0.8838834765, 1.25), p);
			assertEpsilonColinear(createVector(-1., 1., 0.), v);
		}

		@DisplayName("#26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_26(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, p, v));
			assertEpsilonEquals(createPoint(-0.8838834765, -0.8838834765, 1.25), p);
			assertEpsilonColinear(createVector(-1., 1., 0.), v);
		}

		@DisplayName("#27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_27(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, p, v));
			assertEpsilonEquals(createPoint(0.4575317547, 0.4575317547, 1.25), p);
			assertEpsilonColinear(createVector(-1., 1., 0.), v);
		}

		@DisplayName("#28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_28(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, p, v));
			assertEpsilonEquals(createPoint(-1.7075317547, -1.7075317547, 1.25), p);
			assertEpsilonColinear(createVector(-1., 1., 0.), v);
		}

		@DisplayName("#29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_29(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, s));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("#30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_30(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, s));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("#31")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_31(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, s));
			assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), s);
		}

		@DisplayName("#32")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_32(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, s));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), s);
		}

		@DisplayName("#33")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_33(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, s));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), s);
		}

		@DisplayName("#34")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_34(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, s));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), s);
		}

		@DisplayName("#35")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_35(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, s));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(1., -1., 0.), s);
		}

		@DisplayName("#36")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_36(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, s));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(1., -1., 0.), s);
		}

		@DisplayName("#37")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_37(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, s));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("#38")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_38(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, s));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("#39")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_39(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, s));
			assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), s);
		}

		@DisplayName("#40")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_40(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, s));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), s);
		}

		@DisplayName("#41")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_41(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, s));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), s);
		}

		@DisplayName("#42")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_42(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, s));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), s);
		}

		@DisplayName("#43")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_43(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, s));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("#44")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_44(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, s));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("#45")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_45(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);
		}

		@DisplayName("#46")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_46(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);
		}

		@DisplayName("#47")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_47(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), p1, p2);
		}

		@DisplayName("#48")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_48(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), p1, p2);
		}

		@DisplayName("#49")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_49(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), p1, p2);
		}

		@DisplayName("#50")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_50(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), p1, p2);
		}

		@DisplayName("#51")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_51(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(1., -1., 0.), p1, p2);
		}

		@DisplayName("#52")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_52(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(1., -1., 0.), p1, p2);
		}

		@DisplayName("#53")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_53(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);
		}

		@DisplayName("#54")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_54(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);
		}

		@DisplayName("#55")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_55(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), p1, p2);
		}

		@DisplayName("#56")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_56(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), p1, p2);
		}

		@DisplayName("#57")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_57(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), p1, p2);
		}

		@DisplayName("#58")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_58(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), p1, p2);
		}

		@DisplayName("#59")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_59(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, p1, p2));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), p1, p2);
		}

		@DisplayName("#60")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_60(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, p1, p2));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), p1, p2);
		}

		@DisplayName("#61")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_61(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, r));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), r);
		}

		@DisplayName("#62")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_62(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, r));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), r);
		}

		@DisplayName("#63")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_63(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, r));
			assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), r);
		}

		@DisplayName("#64")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_64(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, r));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), r);
		}

		@DisplayName("#65")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_65(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0, 0.707106781373, -1.25, r));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), r);
		}

		@DisplayName("#66")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_66(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0, 0.707106781373, 1.25, r));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), r);
		}

		@DisplayName("#67")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_67(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, r));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(1., -1., 0.), r);
		}

		@DisplayName("#68")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_68(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, r));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(1., -1., 0.), r);
		}

		@DisplayName("#69")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_69(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0, -1.25, r));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), r);
		}

		@DisplayName("#70")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_70(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, r));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), r);
		}

		@DisplayName("#71")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_71(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, r));
			assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), r);
		}

		@DisplayName("#72")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_72(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, r));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), r);
		}

		@DisplayName("#73")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_73(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0, 0.707106781373, -1.25, r));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), r);
		}

		@DisplayName("#74")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_74(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0, 0.707106781373, 1.25, r));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), r);
		}

		@DisplayName("#75")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_75(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, r));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), r);
		}

		@DisplayName("#76")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_76(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var r = mock(PointVector3DReceiver.class);
			assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, r));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), r);
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

		@DisplayName("(Plane3D) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3.25, getP().getDistanceTo(new PlaneXY3d(true, 4.5)));
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
			assertEpsilonEquals(5.75, getP().getDistanceTo(new PlaneXY3d(true, -4.5)));
		}

		@DisplayName("(Plane3D) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.75, getP().getDistanceTo(new PlaneXY3d(false, -4.5)));
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
			assertEpsilonEquals(-3.25, getP().getDistanceTo(new PlaneXY3d(false, 4.5)));
		}

		@DisplayName("(Plane3D) with XY plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(3.25, getP().getDistanceTo(new PlaneXY3d(true, 4.5)));
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
			assertEpsilonEquals(-5.75, getP().getDistanceTo(new PlaneXY3d(true, -4.5)));
		}

		@DisplayName("(Plane3D) with XY plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(-5.75, getP().getDistanceTo(new PlaneXY3d(false, -4.5)));
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
			assertEpsilonEquals(3.25, getP().getDistanceTo(new PlaneXY3d(false, 4.5)));
		}

		@DisplayName("(Plane3D) with YZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(true, 4.5)));
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
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(true, -4.5)));
		}

		@DisplayName("(Plane3D) with YZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(false, -4.5)));
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
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(false, 4.5)));
		}

		@DisplayName("(Plane3D) with YZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(true, 4.5)));
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
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(true, -4.5)));
		}

		@DisplayName("(Plane3D) with YZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(false, -4.5)));
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
			assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(false, 4.5)));
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
			assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(0., 1., 1., -4.5)));
		}

		@DisplayName("(Plane3D) with general plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(1., 1., 0., -4.5)));
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
			assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(0., 1., 1., -4.5)));
		}

		@DisplayName("(Plane3D) with general plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(1., 1., 0., -4.5)));
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
			assertEpsilonEquals(0., getP().getDistanceTo(createPoint(0, 0, 1.25)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.25, getP().getDistanceTo(createPoint(0, 0, -10)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-8.75, getP().getDistanceTo(createPoint(0, 0, 10)));
		}

		@DisplayName("(x, y, z) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.25, getP().getDistanceTo(0, 0, 0));
		}

		@DisplayName("(x, y, z) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(0, 0, 1.25));
		}

		@DisplayName("(x, y, z) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.25, getP().getDistanceTo(0, 0, -10));
		}

		@DisplayName("(x, y, z) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-8.75, getP().getDistanceTo(0, 0, 10));
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

		@DisplayName("(double,double,double,double) with XY plane #")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3.25, getP().getDistanceTo(0., 0., 1., -4.5));
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., 1., -1.25));
			assertEpsilonEquals(5.75, getP().getDistanceTo(0., 0., 1., 4.5));
			assertEpsilonEquals(5.75, getP().getDistanceTo(0., 0., -1., -4.5));
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., -1., 1.25));
			assertEpsilonEquals(-3.25, getP().getDistanceTo(0., 0., -1., 4.5));
			//
			getP().negate();
			assertEpsilonEquals(3.25, getP().getDistanceTo(0., 0., 1., -4.5));
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., 1., -1.25));
			assertEpsilonEquals(-5.75, getP().getDistanceTo(0., 0., 1., 4.5));
			assertEpsilonEquals(-5.75, getP().getDistanceTo(0., 0., -1., -4.5));
			assertEpsilonEquals(0., getP().getDistanceTo(0., 0., -1., 1.25));
			assertEpsilonEquals(3.25, getP().getDistanceTo(0., 0., -1., 4.5));
		}

		@DisplayName("getDistanceTo(double,double,double,double) with YZ plane")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void getDistanceToDoubleDoubleDoubleDouble_yz(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -1.25));
		}

		@DisplayName("(double,double,double,double) with XY plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., 4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., -4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 1.25));
		}

		@DisplayName("(double,double,double,double) with XY plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -1.25));
		}

		@DisplayName("(double,double,double,double) with XY plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., 4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., -4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 1.25));
		}

		@DisplayName("(double,double,double,double) with XY plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 4.5));
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
			assertEpsilonEquals(0., getP().getDistanceTo(0., 1., 1., -4.5));
		}

		@DisplayName("(double,double,double,double) with general plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().getDistanceTo(1., 1., 0., -4.5));
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
			assertEpsilonEquals(0., getP().getDistanceTo(0., 1., 1., -4.5));
		}

		@DisplayName("(double,double,double,double) with general plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertEpsilonEquals(0., getP().getDistanceTo(1., 1., 0., -4.5));
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
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(0., 0., -1., 1.25)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(0., 0., 1., -1.25)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3.25, getP().operator_upTo(new Plane3d(0., 0., 1., -4.5)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.75, getP().operator_upTo(new Plane3d(0., 0., 1., 4.5)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5.75, getP().operator_upTo(new Plane3d(0., 0., -1., -4.5)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-3.25, getP().operator_upTo(new Plane3d(0., 0., -1., 4.5)));
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
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(0., 1., 1., -4.5)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 1., 0., -4.5)));
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
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(0., 1., 1., -4.5)));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 1., 0., -4.5)));
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
			assertEpsilonEquals(0., getP().operator_upTo(createPoint(0, 0, 1.25)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(11.25, getP().operator_upTo(createPoint(0, 0, -10)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-8.75, getP().operator_upTo(createPoint(0, 0, 10)));
		}
	}

	@DisplayName("getIntersection")
	@Nested
	public class GetIntersection {

		private void assertReceiverInvoked(Point3D<?, ?, ?> p, Vector3D<?, ?, ?> v, Segment3afp<?, ?, ?, ?, ?, ?, ?> s) {
			assertEpsilonEquals(p, s.getP1());
			var v0 = s.getP2().operator_minus(s.getP1());
			assertEpsilonColinear(v, v0);
		}

		@DisplayName("(double,double,double,double) with general plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0.707106781373, 0.707106781373, 0., -1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0.8838834765, 0.8838834765, 1.25), s.getP1());
			assertEpsilonColinear(createVector(-1., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with general plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0.707106781373, 0.707106781373, 0., 1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-0.8838834765, -0.8838834765, 1.25), s.getP1());
			assertEpsilonColinear(createVector(-1., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with general plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0.707106781373, 0.707106781373, -1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0., 0.517766953, 1.25), s.getP1());
			assertEpsilonColinear(createVector(-1., 0., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with general plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0., 0.707106781373, 0.707106781373, 1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0., -3.017766953, 1.25), s.getP1());
			assertEpsilonColinear(createVector(-1., 0., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with general plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0.707106781373, 0., 0.707106781373, -1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0.517766953, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with general plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0.707106781373, 0., 0.707106781373, 1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-3.017766953, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with general plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0.577350269, 0.577350269, 0.577350269, -1.25);
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0.4575317547, 0.4575317547, 1.25), s.getP1());
			assertEpsilonColinear(createVector(-1., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with general plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_general_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(0.577350269, 0.577350269, 0.577350269, 1.25);
			assertEpsilonEquals(createPoint(-1.7075317547, -1.7075317547, 1.25), s.getP1());
			assertEpsilonColinear(createVector(-1., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with XZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(0., 0., 1., -4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(0., 0., 1., 4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(0., 0., -1., -4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(0., 0., -1., -4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(0., 0., 1., -1.25));
		}

		@DisplayName("(double,double,double,double) with XZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(0., 0., -1., 1.25));
		}

		@DisplayName("(double,double,double,double) with XZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(0., 0., 1., -4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(0., 0., 1., 4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(0., 0., -1., 4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(0., 0., -1., -4.5));
		}

		@DisplayName("(double,double,double,double) with XZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(0., 0., 1., -1.25));
		}

		@DisplayName("(double,double,double,double) with XZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(0., 0., -1., 1.25));
		}

		@DisplayName("(double,double,double,double) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(0., 0., 1., -4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(0., 0., 1., 4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(0., 0., -1., -4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(0., 0., -1., -4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(0., 0., 1., -1.25));
		}

		@DisplayName("(double,double,double,double) with XY plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(0., 0., -1., 1.25));
		}

		@DisplayName("(double,double,double,double) with XY plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(0., 0., 1., -4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(0., 0., 1., 4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(0., 0., -1., 4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(0., 0., -1., -4.5));
		}

		@DisplayName("(double,double,double,double) with XY plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(0., 0., 1., -1.25));
		}

		@DisplayName("(double,double,double,double) with XY plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_xy_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(0., 0., -1., 1.25));
		}

		@DisplayName("(double,double,double,double) with YZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(1., 0., 0., -4.5);
			assertEpsilonEquals(createPoint(4.5, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with YZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(1., 0., 0., 4.5);
			assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with YZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(-1., 0., 0., -4.5);
			assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with YZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(-1., 0., 0., -4.5);
			assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with YZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(1., 0., 0., -1.25);
			assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with YZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(-1., 0., 0., 1.25);
			assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with YZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(1., 0., 0., -4.5);
			assertEpsilonEquals(createPoint(4.5, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with YZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(1., 0., 0., 4.5);
			assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with YZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(-1., 0., 0., 4.5);
			assertEpsilonEquals(createPoint(4.5, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with YZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(-1., 0., 0., -4.5);
			assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with YZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(1., 0., 0., -1.25);
			assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(double,double,double,double) with YZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_yz_(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(-1., 0., 0., 1.25);
			assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
			assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(createSegment(
					32.5, -5.6, -51.2,
					47.1, -7.9, .5)));
		}

		@DisplayName("(Segment3afp) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(createSegment(
					32.5, -5.6, 47.2,
					47.1, -7.9, 2.)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(
					32.5, -5.6, 0.,
					47.1, -7.9, 2.));
			assertEpsilonEquals(createPoint(41.625, -7.0375, 1.25), p);
		}

		@DisplayName("(Segment3afp) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(
					32.5, -5.6, 2.,
					47.1, -7.9, 0.));
			assertEpsilonEquals(createPoint(37.975, -6.4625, 1.25), p);
		}

		@DisplayName("(Segment3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(
					32.5, -5.6, 1.25,
					47.1, -7.9, 0.));
			assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(
					32.5, -5.6, 0.,
					47.1, -7.9, 1.25));
			assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);
		}

		@DisplayName("(Segment3afp) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(
					32.5, -5.6, 1.25,
					47.1, -7.9, 2.));
			assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);
		}

		@DisplayName("(Segment3afp) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var p = getP().getIntersection(createSegment(
					32.5, -5.6, 1.25,
					47.1, -7.9, 1.25));
			assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(createSegment(
					32.5, -5.6, -51.2,
					47.1, -7.9, .5)));
		}

		@DisplayName("(Segment3afp) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(createSegment(
					32.5, -5.6, 47.2,
					47.1, -7.9, 2.)));
		}

		@DisplayName("(Segment3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					32.5, -5.6, 0.,
					47.1, -7.9, 2.));
			assertEpsilonEquals(createPoint(41.625, -7.0375, 1.25), p);
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					32.5, -5.6, 2.,
					47.1, -7.9, 0.));
			assertEpsilonEquals(createPoint(37.975, -6.4625, 1.25), p);
		}

		@DisplayName("(Segment3afp) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					32.5, -5.6, 1.25,
					47.1, -7.9, 0.));
			assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);
		}

		@DisplayName("(Segment3afp) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					32.5, -5.6, 0.,
					47.1, -7.9, 1.25));
			assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);
		}

		@DisplayName("(Segment3afp) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					32.5, -5.6, 1.25,
					47.1, -7.9, 2.));
			assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);
		}

		@DisplayName("(Segment3afp) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					32.5, -5.6, 2.,
					47.1, -7.9, 1.25));
			assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);
		}

		@DisplayName("(Segment3afp) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					32.5, -5.6, 1.25,
					47.1, -7.9, 1.25));
			assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);
		}

		@DisplayName("(Segment3afp) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var p = getP().getIntersection(createSegment(
					32.5, -5.6, 2.,
					47.1, -7.9, 1.25));
			assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);
		}

		@DisplayName("(Plane3D) with XZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXZ3d(true, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0., 4.5, 1.25), s.getP1());
			assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXZ3d(true, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0., -4.5, 1.25), s.getP1());
			assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXZ3d(false, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0., 4.5, 1.25), s.getP1());
			assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXZ3d(false, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0., -4.5, 1.25), s.getP1());
			assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXZ3d(true, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0., 1.25, 1.25), s.getP1());
			assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneXZ3d(false, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0., 1.25, 1.25), s.getP1());
			assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXZ3d(true, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0., 4.5, 1.25), s.getP1());
			assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXZ3d(true, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0., -4.5, 1.25), s.getP1());
			assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXZ3d(false, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0., 4.5, 1.25), s.getP1());
			assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXZ3d(false, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0., -4.5, 1.25), s.getP1());
			assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXZ3d(true, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0., 1.25, 1.25), s.getP1());
			assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with XZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneXZ3d(false, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(0., 1.25, 1.25), s.getP1());
			assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(new PlaneXY3d(true, 4.5)));
		}

		@DisplayName("(Plane3D) with XY plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(new PlaneXY3d(true, -4.5)));
		}

		@DisplayName("(Plane3D) with XY plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(new PlaneXY3d(false, 4.5)));
		}

		@DisplayName("(Plane3D) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(new PlaneXY3d(false, -4.5)));
		}

		@DisplayName("(Plane3D) with XY plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(new PlaneXY3d(true, 1.25)));
		}

		@DisplayName("(Plane3D) with XY plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertNull(getP().getIntersection(new PlaneXY3d(false, 1.25)));
		}

		@DisplayName("(Plane3D) with XY plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(new PlaneXY3d(true, 4.5)));
		}

		@DisplayName("(Plane3D) with XY plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(new PlaneXY3d(true, -4.5)));
		}

		@DisplayName("(Plane3D) with XY plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(new PlaneXY3d(false, 4.5)));
		}

		@DisplayName("(Plane3D) with XY plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(new PlaneXY3d(false, -4.5)));
		}

		@DisplayName("(Plane3D) with XY plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(new PlaneXY3d(true, 1.25)));
		}

		@DisplayName("(Plane3D) with XY plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertNull(getP().getIntersection(new PlaneXY3d(false, 1.25)));
		}

		@DisplayName("(Plane3D) with YZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneYZ3d(true, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(4.5, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with YZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneYZ3d(true, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.5, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with YZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneYZ3d(false, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(4.5, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with YZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneYZ3d(false, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.5, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with YZ plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneYZ3d(true, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with YZ plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var s = getP().getIntersection(new PlaneYZ3d(false, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with YZ plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneYZ3d(true, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(4.5, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with YZ plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneYZ3d(true, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.5, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with YZ plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneYZ3d(false, 4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(4.5, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with YZ plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneYZ3d(false, -4.5));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(-4.5, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with YZ plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneYZ3d(true, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with YZ plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			var s = getP().getIntersection(new PlaneYZ3d(false, 1.25));
			assertNotNull(s);
			assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
			assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
		}

		@DisplayName("(Plane3D) with general plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(1., 1., 0., -1.25));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(1., 1., 0., 1.25));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(0., 1., 1., -1.25));
			assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(0., 1., 1., 1.25));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(1., 0., 1., -1.25));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(1., 0., 1., 1.25));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(1., 1., 1., -1.25));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			var s = getP().getIntersection(new Plane3d(1., 1., 1., 1.25));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(1., 1., 0., -1.25));
			assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(1., 1., 0., 1.25));
			assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(0., 1., 1., -1.25));
			assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(0., 1., 1., 1.25));
			assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(1., 0., 1., -1.25));
			assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(1., 0., 1., 1.25));
			assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(1., 1., 1., -1.25));
			assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), s);
		}

		@DisplayName("(Plane3D) with general plane #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			var s = getP().getIntersection(new Plane3d(1., 1., 1., 1.25));
			assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), s);
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
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1.25)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 2)));
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
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(2, 2, 1.25, 3, 3, 3)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createAlignedBoxFromPoints(2, 2, 1, 3, 3, 3)));
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
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1.25)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 2)));
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
			assertFalse(getP().operator_and(createAlignedBoxFromPoints(2, 2, 1.25, 3, 3, 3)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createAlignedBoxFromPoints(2, 2, 1, 3, 3, 3)));
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
			assertFalse(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1.25)));
		}

		@DisplayName("(Box3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 2)));
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
			assertFalse(getP().intersects(createAlignedBoxFromPoints(2, 2, 1.25, 3, 3, 3)));
		}

		@DisplayName("(Box3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createAlignedBoxFromPoints(2, 2, 1, 3, 3, 3)));
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
			assertFalse(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1.25)));
		}

		@DisplayName("(Box3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 2)));
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
			assertFalse(getP().intersects(createAlignedBoxFromPoints(2, 2, 1.25, 3, 3, 3)));
		}

		@DisplayName("(Box3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createAlignedBoxFromPoints(2, 2, 1, 3, 3, 3)));
		}

		@DisplayName("(Plane3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// 1) Parallel, distinct plane -> no intersection
			//    z = 2  => 0*x+0*y-1*z+2 = 0
			assertFalse(getP().intersects(createPlane(0, 0, -1, 2)));
		}

		@DisplayName("(Plane3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// 2) Opposite normal, same geometric plane -> intersects (coincident)
			//    z = 1.25 => 0*x+0*y+1*z-1.25 = 0
			assertTrue(getP().intersects(createPlane(0, 0, 1, -1.25)));
		}

		@DisplayName("(Plane3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// 2) Orthogonal plane X=0 -> intersects (line parallel to Y axis at z=1.25)
			assertTrue(getP().intersects(createPlane(1, 0, 0, 0)));
		}

		@DisplayName("(Plane3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// 4) Orthogonal plane Y=0 -> intersects (line parallel to X axis at z=1.25)
			assertTrue(getP().intersects(createPlane(0, 1, 0, 0)));
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
			//    z = -4 => 0*x+0*y-1*z-4 = 0
			assertFalse(getP().intersects(createPlane(0, 0, -1, -4)));
		}

		@DisplayName("(Plane3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// 7) Nearly parallel but not parallel (small x component in normal) -> intersects
			//    1e-12*x - z + 1.25 = 0
			assertTrue(getP().intersects(createPlane(1e-12, 0, -1, 1.25)));
		}

		@DisplayName("(x, y, z) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, 0));
		}

		@DisplayName("(x, y, z) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(0, 0, 1.25));
		}

		@DisplayName("(x, y, z) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, -10));
		}

		@DisplayName("(x, y, z) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, 10));
		}

		@DisplayName("(x, y, z) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, 0));
		}

		@DisplayName("(x, y, z) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(0, 0, 1.25));
		}

		@DisplayName("(x, y, z) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, -10));
		}

		@DisplayName("(x, y, z) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, 10));
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
			assertTrue(getP().intersects(createPoint(0, 0, 1.25)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createPoint(0, 0, -10)));
		}

		@DisplayName("(Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createPoint(0, 0, 10)));
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
			assertTrue(getP().intersects(createPoint(0, 0, 1.25)));
		}

		@DisplayName("(Point3D) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createPoint(0, 0, -10)));
		}

		@DisplayName("(Point3D) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createPoint(0, 0, 10)));
		}

		@DisplayName("(x, y, z, radius) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, 0, 1));
		}

		@DisplayName("(x, y, z, radius) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, 0, 1.25));
		}

		@DisplayName("(x, y, z, radius) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(0, 0, 0, 2));
		}

		@DisplayName("(x, y, z, radius) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, 3, 1));
		}

		@DisplayName("(x, y, z, radius) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, 3, 1.75));
		}

		@DisplayName("(x, y, z, radius) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(0, 0, 3, 2));
		}

		@DisplayName("(x, y, z, radius) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, 0, 1));
		}

		@DisplayName("(x, y, z, radius) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, 0, 1.25));
		}

		@DisplayName("(x, y, z, radius) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(0, 0, 0, 2));
		}

		@DisplayName("(x, y, z, radius) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, 3, 1));
		}

		@DisplayName("(x, y, z, radius) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, 3, 1.75));
		}

		@DisplayName("(x, y, z, radius) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzradius_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(0, 0, 3, 2));
		}

		@DisplayName("(lx,ly,lz, ux,uy,uz) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, 0, 1, 1, 1));
		}

		@DisplayName("(lx,ly,lz, ux,uy,uz) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(0, 0, 0, 1, 1, 1.25));
		}

		@DisplayName("(lx,ly,lz, ux,uy,uz) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(0, 0, 0, 1, 1, 2));
		}

		@DisplayName("(lx,ly,lz, ux,uy,uz) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(2, 2, 2, 3, 3, 3));
		}

		@DisplayName("(lx,ly,lz, ux,uy,uz) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(2, 2, 1.25, 3, 3, 3));
		}

		@DisplayName("(lx,ly,lz, ux,uy,uz) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(2, 2, 1, 3, 3, 3));
		}

		@DisplayName("(lx,ly,lz, ux,uy,uz) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, 0, 1, 1, 1));
		}

		@DisplayName("(lx,ly,lz, ux,uy,uz) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(0, 0, 0, 1, 1, 1.25));
		}

		@DisplayName("(lx,ly,lz, ux,uy,uz) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(0, 0, 0, 1, 1, 2));
		}

		@DisplayName("(lx,ly,lz, ux,uy,uz) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(2, 2, 2, 3, 3, 3));
		}

		@DisplayName("(lx,ly,lz, ux,uy,uz) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(2, 2, 1.25, 3, 3, 3));
		}

		@DisplayName("(lx,ly,lz, ux,uy,uz) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyz_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(2, 2, 1, 3, 3, 3));
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
			assertFalse(getP().intersects(createSegment(0, 0, 0, 1, 1, 1.25)));
		}

		@DisplayName("(Segment3afp) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSegment(0, 0, 0, 1, 1, 2)));
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
			assertFalse(getP().intersects(createSegment(3, 3, 3, 2, 2, 1.25)));
		}

		@DisplayName("(Segment3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSegment(3, 3, 3, 2, 2, 1)));
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
			assertFalse(getP().intersects(createSegment(0, 0, 0, 1, 1, 1.25)));
		}

		@DisplayName("(Segment3afp) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSegment(0, 0, 0, 1, 1, 2)));
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
			assertFalse(getP().intersects(createSegment(3, 3, 3, 2, 2, 1.25)));
		}

		@DisplayName("(Segment3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSegment(3, 3, 3, 2, 2, 1)));
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
			assertFalse(getP().intersects(createSphere(0, 0, 3, 1)));
		}

		@DisplayName("(Sphere3afp) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().intersects(createSphere(0, 0, 3, 1.75)));
		}

		@DisplayName("(Sphere3afp) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().intersects(createSphere(0, 0, 3, 2)));
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
			assertFalse(getP().intersects(createSphere(0, 0, 3, 1)));
		}

		@DisplayName("(Sphere3afp) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().intersects(createSphere(0, 0, 3, 1.75)));
		}

		@DisplayName("(Sphere3afp) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().intersects(createSphere(0, 0, 3, 2)));
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
			assertTrue(getP().operator_and(createPoint(0, 0, 1.25)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createPoint(0, 0, -10)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createPoint(0, 0, 10)));
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
			assertTrue(getP().operator_and(createPoint(0, 0, 1.25)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createPoint(0, 0, -10)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createPoint(0, 0, 10)));
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
			assertFalse(getP().operator_and(createSegment(0, 0, 0, 1, 1, 1.25)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSegment(0, 0, 0, 1, 1, 2)));
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
			assertFalse(getP().operator_and(createSegment(3, 3, 3, 2, 2, 1.25)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSegment(3, 3, 3, 2, 2, 1)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createSegment(0, 0, 0, 1, 1, 1)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createSegment(0, 0, 0, 1, 1, 1.25)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createSegment(0, 0, 0, 1, 1, 2)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createSegment(3, 3, 3, 2, 2, 2)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createSegment(3, 3, 3, 2, 2, 1.25)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createSegment(3, 3, 3, 2, 2, 1)));
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
			assertFalse(getP().operator_and(createSphere(0, 0, 3, 1)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getP().operator_and(createSphere(0, 0, 3, 1.75)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getP().operator_and(createSphere(0, 0, 3, 2)));
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
			assertFalse(getP().operator_and(createSphere(0, 0, 3, 1)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertFalse(getP().operator_and(createSphere(0, 0, 3, 1.75)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().negate();
			assertTrue(getP().operator_and(createSphere(0, 0, 3, 2)));
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
			assertTrue(getP().operator_and(new Plane3d(1., 1., 0., -1.25)));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			assertTrue(getP().operator_and(new Plane3d(1., 1., 0., 1.25)));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			assertTrue(getP().operator_and(new Plane3d(0., 1., 1., -1.25)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(true);
			assertTrue(getP().operator_and(new Plane3d(0., 1., 1., 1.25)));
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
			assertTrue(getP().operator_and(new Plane3d(1., 1., 0., -1.25)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			assertTrue(getP().operator_and(new Plane3d(1., 1., 0., 1.25)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			assertTrue(getP().operator_and(new Plane3d(0., 1., 1., -1.25)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPositive(false);
			assertTrue(getP().operator_and(new Plane3d(0., 1., 1., 1.25)));
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
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(4., getP().getEquationComponentD());
			assertEpsilonEquals(-4., getP().getZ());
		}

		@DisplayName("(Plane3D) with XZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXZ3d(true, 4));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-4., getP().getEquationComponentD());
			assertEpsilonEquals(4., getP().getZ());
		}

		@DisplayName("(Plane3D) with XZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXZ3d(false, 4));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(4., getP().getEquationComponentD());
			assertEpsilonEquals(-4., getP().getZ());
		}

		@DisplayName("(Plane3D) with XZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXZ3d(false, -4));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-4., getP().getEquationComponentD());
			assertEpsilonEquals(4., getP().getZ());
		}

		@DisplayName("(Plane3D) with XY plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXY3d(true, -4));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(4, getP().getEquationComponentD());
			assertEpsilonEquals(-4, getP().getZ());
		}

		@DisplayName("(Plane3D) with XY plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXY3d(true, 4));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
			assertEpsilonEquals(4, getP().getZ());
		}

		@DisplayName("(Plane3D) with XY plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXY3d(false, 4));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(4, getP().getEquationComponentD());
			assertEpsilonEquals(4, getP().getZ());
		}

		@DisplayName("(Plane3D) with XY plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_xy_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneXY3d(false, -4));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
			assertEpsilonEquals(-4, getP().getZ());
		}

		@DisplayName("(Plane3D) with YZ plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneYZ3d(true, -4));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(4., getP().getEquationComponentD());
			assertEpsilonEquals(-4., getP().getZ());
		}

		@DisplayName("(Plane3D) with YZ plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneYZ3d(true, 4));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-4., getP().getEquationComponentD());
			assertEpsilonEquals(4., getP().getZ());
		}

		@DisplayName("(Plane3D) with YZ plane #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneYZ3d(false, 4));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(4., getP().getEquationComponentD());
			assertEpsilonEquals(-4., getP().getZ());
		}

		@DisplayName("(Plane3D) with YZ plane #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_yz_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new PlaneYZ3d(false, -4));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-4., getP().getEquationComponentD());
			assertEpsilonEquals(4., getP().getZ());
		}

		@DisplayName("(Plane3D) with general plane #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new Plane3d(2, -1, 3, -4));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
			assertEpsilonEquals(4, getP().getZ());
		}

		@DisplayName("(Plane3D) with general plane #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void plane_general_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(new Plane3d(0, 0, 1, -18));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-18, getP().getEquationComponentD());
			assertEpsilonEquals(18, getP().getZ());
		}

		@DisplayName("(double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(1, 2, 3, 4);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(4, getP().getEquationComponentD());
			assertEpsilonEquals(-4, getP().getZ());
		}

		@DisplayName("(double,double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(1, 2, 3, -4);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
			assertEpsilonEquals(4, getP().getZ());
		}

		@DisplayName("(double,double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(-1, -2, -3, 4);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(4, getP().getEquationComponentD());
			assertEpsilonEquals(4, getP().getZ());
		}

		@DisplayName("(double,double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void abcd_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(-1, -2, -3, -4);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(-4, getP().getEquationComponentD());
			assertEpsilonEquals(-4, getP().getZ());
		}

		@DisplayName("(double,double,double,double,double,double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyzxyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(1, 2, 3, 4, 18, -42, 57, 1, -6);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(-15.0, getP().getEquationComponentD());
			assertEpsilonEquals(-15.0, getP().getZ());
		}

		@DisplayName("(double,double,double,double,double,double,double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzxyzxyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(1, 2, 3, 1, 2, 3, 1, 2, 3);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-3., getP().getEquationComponentD());
			assertEpsilonEquals(3, getP().getZ());
		}

		@DisplayName("(Point3D,Point3D,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(1, 2, 3), createPoint(4, 18, -42), createPoint(57, 1, -6));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(-15.0, getP().getEquationComponentD());
			assertEpsilonEquals(-15.0, getP().getZ());
		}

		@DisplayName("(Point3D,Point3D,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointpointpoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(1, 2, 3), createPoint(1, 2, 3), createPoint(1, 2, 3));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-3., getP().getEquationComponentD());
			assertEpsilonEquals(3, getP().getZ());
		}

		@DisplayName("(Point3D,Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(1, 2, 3), createVector(0, 1, -42));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(3, getP().getEquationComponentD());
			assertEpsilonEquals(3, getP().getZ());
		}

		@DisplayName("(Point3D,Vector3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvector_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(1, 2, 3), createVector(0, 1, 42));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-3, getP().getEquationComponentD());
			assertEpsilonEquals(3, getP().getZ());
		}

		@DisplayName("(Point3D,Vector3D,Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(1, 2, 3), createVector(3, 16, -45), createVector(56, -1, -9));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(3.0, getP().getEquationComponentD());
			assertEpsilonEquals(3.0, getP().getZ());
		}

		@DisplayName("(Point3D,Vector3D,Vector3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(1, 2, 3), createVector(56, -1, -9), createVector(3, 16, -45));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-3.0, getP().getEquationComponentD());
			assertEpsilonEquals(3.0, getP().getZ());
		}

		@DisplayName("(Point3D,Vector3D,Vector3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(1, 2, 3), createVector(0, 0, 0), createVector(1, 2, 3));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-3., getP().getEquationComponentD());
			assertEpsilonEquals(3, getP().getZ());
		}

		@DisplayName("(Point3D,Vector3D,Vector3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(1, 2, 3), createVector(1, 2, 3), createVector(0, 0, 0));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-3., getP().getEquationComponentD());
			assertEpsilonEquals(3, getP().getZ());
		}

		@DisplayName("(Point3D,Vector3D,Vector3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pointvectorvector_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().set(createPoint(1, 2, 3), createVector(0, 0, 0), createVector(0, 0, 0));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-3., getP().getEquationComponentD());
			assertEpsilonEquals(3, getP().getZ());
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
			assertEpsilonEquals(0., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(1., r.getEquationComponentC());
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
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
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
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
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
			assertEpsilonEquals(0., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(-1., r.getEquationComponentC());
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
			assertEpsilonEquals(0., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(-1., r.getEquationComponentC());
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
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(6.94, getP().getEquationComponentD());
		}

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().translate(148, 5.69, 569);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(570.25, getP().getEquationComponentD());
		}

		@DisplayName("(Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().translate(createVector(148, 5.69, 569));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
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
			getP().operator_add(createVector(148, 5.69, 569));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
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
			getP().operator_remove(createVector(148, 5.69, 569));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
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
			var r = getP().operator_plus(createVector(148, 5.69, 569));
			assertNotSame(getP(), r);
			assertEpsilonEquals(0., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(-1., r.getEquationComponentC());
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
			var r = getP().operator_minus(createVector(148, 5.69, 569));
			assertNotSame(getP(), r);
			assertEpsilonEquals(0., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(-1., r.getEquationComponentC());
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
			assertSame(getP(), getP().normalize());
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
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(0, getP().getEquationComponentD());
		}

		@DisplayName("(double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(1, 2, 3);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(3, getP().getEquationComponentD());
		}

		@DisplayName("(double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(4, 5, 63);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(63, getP().getEquationComponentD());
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(createPoint(0, 0, 0));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(0, getP().getEquationComponentD());
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(createPoint(1, 2, 3));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(3, getP().getEquationComponentD());
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getP().setPivot(createPoint(4, 5, 63));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(63, getP().getEquationComponentD());
		}
	}

	@DisplayName("this * Tranform3D")
	@Nested
	public class OperatorMultiplyTransform3D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation Z, no rotation
			var tr = new Transform3D();
			tr.setTranslation(0, 0, 5.69);
			var r = getP().operator_multiply(tr);
			assertNotSame(getP(), r);
			assertEpsilonEquals(0., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(-1., r.getEquationComponentC());
			assertEpsilonEquals(6.94, r.getEquationComponentD());
			assertEpsilonEquals(6.94, r.getZ());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation XYZ, no rotation
			var tr = new Transform3D();
			tr.setTranslation(5, 6, 3.69);
			var r = getP().operator_multiply(tr);
			assertNotSame(getP(), r);
			assertEpsilonEquals(0., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(-1., r.getEquationComponentC());
			assertEpsilonEquals(4.94, r.getEquationComponentD());
			assertEpsilonEquals(4.94, r.getZ());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			// Translation Z, Rotation with quadrant flip
			var tr = new Transform3D();
			tr.makeRotationMatrix(1.,  2.,  -1., Math.PI / 7.);
			tr.setTranslation(0, 0, 5.69);
			var r = getP().operator_multiply(tr);
			assertNotSame(getP(), r);
			assertEpsilonEquals(0., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(1., r.getEquationComponentC());
			assertEpsilonEquals(-6.94, r.getEquationComponentD());
			assertEpsilonEquals(6.94, r.getZ());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			// Translation XYZ, Rotation without quadrant flip
			var tr = new Transform3D();
			tr.makeRotationMatrix(0.,  0.,  -1., Math.PI / 7.);
			tr.setTranslation(5, 6, 3.69);
			var r = getP().operator_multiply(tr);
			assertNotSame(getP(), r);
			assertEpsilonEquals(0., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(-1., r.getEquationComponentC());
			assertEpsilonEquals(4.94, r.getEquationComponentD());
			assertEpsilonEquals(4.94, r.getZ());
		}
	}

	@DisplayName("transform")
	@Nested
	public class Transform {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation Z, no rotation
			var tr = new Transform3D();
			tr.setTranslation(0, 0, 5.69);
			getP().transform(tr);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(6.94, getP().getEquationComponentD());
			assertEpsilonEquals(6.94, getP().getZ());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation XYZ, no rotation
			var tr = new Transform3D();
			tr.setTranslation(5, 6, 3.69);
			getP().transform(tr);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(10.63, getP().getEquationComponentD());
			assertEpsilonEquals(10.63, getP().getZ());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation Z, Rotation with quadrant flip
			var tr = new Transform3D();
			tr.makeRotationMatrix(1.,  2.,  -1., Math.PI / 7.);
			tr.setTranslation(0, 0, 5.69);
			getP().transform(tr);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-16.32, getP().getEquationComponentD());
			assertEpsilonEquals(16.32, getP().getZ());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation XYZ, Rotation without quadrant flip
			var tr = new Transform3D();
			tr.makeRotationMatrix(0.,  0.,  -1., Math.PI / 7.);
			tr.setTranslation(5, 6, 3.69);
			getP().transform(tr);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-20.01, getP().getEquationComponentD());
			assertEpsilonEquals(20.01, getP().getZ());
		}

		@DisplayName("(Transform3D,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation Z, no rotation
			var tr = new Transform3D();
			tr.setTranslation(0, 0, 5.69);
			getP().transform(tr, createPoint(-45, 6, -42));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(6.94, getP().getEquationComponentD());
			assertEpsilonEquals(6.94, getP().getZ());
		}

		@DisplayName("(Transform3D,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation XYZ, no rotation
			var tr = new Transform3D();
			tr.setTranslation(5, 6, 3.69);
			getP().transform(tr, createPoint(-2, -5, 18));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(-1., getP().getEquationComponentC());
			assertEpsilonEquals(10.63, getP().getEquationComponentD());
			assertEpsilonEquals(10.63, getP().getZ());
		}

		@DisplayName("(Transform3D,Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation Z, Rotation with quadrant flip
			var tr = new Transform3D();
			tr.makeRotationMatrix(1.,  2.,  -1., Math.PI / 7.);
			tr.setTranslation(0, 0, 5.69);
			getP().transform(tr, createPoint(5, 6.5, 0));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-16.32, getP().getEquationComponentD());
			assertEpsilonEquals(16.32, getP().getZ());
		}

		@DisplayName("(Transform3D,Point3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void transformpoint_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Translation XYZ, Rotation without quadrant flip
			var tr = new Transform3D();
			tr.makeRotationMatrix(0.,  0.,  -1., Math.PI / 7.);
			tr.setTranslation(5, 6, 3.69);
			getP().transform(tr, createPoint(9, -1, 0.5));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-20.01, getP().getEquationComponentD());
			assertEpsilonEquals(20.01, getP().getZ());
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
			assertEpsilonEquals(createPoint(0, 0, 1.25), getP().getProjection(createPoint(0, 0, 0)));
		}

		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(125, -458, 1.25), getP().getProjection(createPoint(125, -458, -145)));
		}

		@DisplayName("(Point3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(-145, 458, 1.25), getP().getProjection(createPoint(-145, 458, 18)));
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
			assertEpsilonEquals(createPoint(x, y, 1.25), getP().getProjection(createPoint(x, y, z)));
		}

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(0, 0, 1.25), getP().getProjection(0, 0, 0));
		}

		@DisplayName("(double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(125, -458, 1.25), getP().getProjection(125, -458, -145));
		}

		@DisplayName("(double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyz_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(-145, 458, 1.25), getP().getProjection(-145, 458, 18));
		}

		@DisplayName("(double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("providePointsArguments")
		public final void xyz_4(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(createPoint(x, y, 1.25), getP().getProjection(x, y, z));
		}
	}

	@DisplayName("rotate")
	@Nested
	public class Rotate {

		@DisplayName("(double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation with quadrant flip
			getP().rotate(1.,  1., 0., 1.2 * Math.PI);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getZ());
		}

		@DisplayName("(double,double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void xyzangle_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation without quadrant flip
			getP().rotate(0.,  1., 0., Math.PI / 7.);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getZ());
		}

		@DisplayName("(Quaternion) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternion_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation with quadrant flip
			getP().rotate(createAxisAngle(1.,  1., 0., 1.2 * Math.PI));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getZ());
		}

		@DisplayName("(Quaternion) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternion_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation without quadrant flip
			getP().rotate(createAxisAngle(0.,  1., 0., Math.PI / 7.));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getZ());
		}

		@DisplayName("(Quaternion,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternionpoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation with quadrant flip
			getP().rotate(createAxisAngle(1.,  1., 0., 1.2 * Math.PI), createPoint(1, 2, 3));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getZ());
		}

		@DisplayName("(Quaternion,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void quaternionpoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation without quadrant flip
			getP().rotate(createAxisAngle(0.,  1., 0., Math.PI / 7.), createPoint(1, 2, 3));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getZ());
		}

		@DisplayName("(Vector3D,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vectordouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation with quadrant flip
			getP().rotate(createVector(1.,  1., 0.), 1.2 * Math.PI);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getZ());
		}

		@DisplayName("(Vector3D,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vectordouble_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation without quadrant flip
			getP().rotate(createVector(0.,  1., 0.), Math.PI / 7.);
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getZ());
		}

		@DisplayName("(Vector3D,double,Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vectordoublepoint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation with quadrant flip
			getP().rotate(createVector(1.,  1., 0.), 1.2 * Math.PI, createPoint(1, 2, 3));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getZ());
		}

		@DisplayName("(Vector3D,double,Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vectordoublepoint_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation without quadrant flip
			getP().rotate(createVector(0.,  1., 0.), Math.PI / 7., createPoint(1, 2, 3));
			assertEpsilonEquals(0., getP().getEquationComponentA());
			assertEpsilonEquals(0., getP().getEquationComponentB());
			assertEpsilonEquals(1., getP().getEquationComponentC());
			assertEpsilonEquals(-1.25, getP().getEquationComponentD());
			assertEpsilonEquals(1.25, getP().getZ());
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
			var r = getP().operator_multiply(createAxisAngle(1.,  1., 0., 1.2 * Math.PI));
			assertNotSame(getP(), r);
			assertEpsilonEquals(0., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(1., r.getEquationComponentC());
			assertEpsilonEquals(-1.25, r.getEquationComponentD());
			assertEpsilonEquals(1.25, r.getZ());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Rotation without quadrant flip
			var r = getP().operator_multiply(createAxisAngle(0.,  1., 0., Math.PI / 7.));
			assertNotSame(getP(), r);
			assertEpsilonEquals(0., r.getEquationComponentA());
			assertEpsilonEquals(0., r.getEquationComponentB());
			assertEpsilonEquals(-1., r.getEquationComponentC());
			assertEpsilonEquals(1.25, r.getEquationComponentD());
			assertEpsilonEquals(1.25, r.getZ());
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
			assertTrue(getP().operator_equals(getP()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertTrue(getP().operator_equals(createPlaneXY(1.25, false)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().operator_equals(createPlaneXY(1.25, true)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertTrue(getP().operator_equals(createPlane(0, 0, -1, 1.25)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().operator_equals(createPlane(0, 0, -1, -1.25)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().operator_equals(createPlane(0, 0, 1, -1.25)));
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
			assertFalse(getP().operator_notEquals(createPlaneXY(1.25, false)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertTrue(getP().operator_notEquals(createPlaneXY(1.25, true)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().operator_notEquals(createPlane(0, 0, -1, 1.25)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertTrue(getP().operator_notEquals(createPlane(0, 0, -1, -1.25)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertTrue(getP().operator_notEquals(createPlane(0, 0, 1, -1.25)));
		}
	}

	@DisplayName("findsClosestPointRectangleXYSegment")
	@Nested
	public class FindsClosestPointRectangleXYSegment {

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
			// Rectangle [1,2]x[2,3] at z=3. Segment (0,0,0)->(1,0.5,-5).
			// Both points are below z=3 and outside XY bounds.
			// Closest segment point = S1=(0,0,0), closest rect point = corner (1,2,3).
			// sqDist = (0-1)^2+(0-2)^2+(0-3)^2 = 1+4+9 = 14.
			assertEpsilonEquals(14., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					1., 2., 2., 3., 3.,
					0., 0., 0., 1., 0.5, -5.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(0., 0., 0.), onSegment);
			assertEpsilonEquals(createPoint(1., 2., 3.), onPlane);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			// CASE 2: Segment pierces the rectangle interior - distance must be 0.
			// Rectangle [-1,1]x[-1,1] at z=0. Segment (0,0,1)->(0,0,-1).
			// Parametric intersection at t=0.5 -> point (0,0,0) inside rectangle.
			assertEpsilonEquals(0., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					-1., -1., 1., 1., 0.,
					0., 0., 1., 0., 0., -1.,
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
			// Rectangle [0,2]x[0,2] at z=0. Segment (0,0,1)->(0,0,-1).
			// Intersection at t=0.5 -> point (0,0,0) which is exactly corner (0,0,0).
			assertEpsilonEquals(0., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					0., 0., 2., 2., 0.,
					0., 0., 1., 0., 0., -1.,
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
			// Rectangle [1,2]x[1,2] at z=0. Segment (5,5,1)->(5,5,-1).
			// Intersection at (5,5,0) - outside XY. Closest rect point = corner (2,2,0).
			// Closest segment point is where the segment is closest to (2,2,0).
			// The segment is vertical at x=5,y=5. Closest point on segment to (2,2,0) is (5,5,0).
			// sqDist = (5-2)^2+(5-2)^2+(0-0)^2 = 9+9 = 18.
			assertEpsilonEquals(18., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					1., 1., 2., 2., 0.,
					5., 5., 1., 5., 5., -1.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(5., 5., 0.), onSegment);
			assertEpsilonEquals(createPoint(2., 2., 0.), onPlane);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			// CASE 5: Segment coplanar with the rectangle plane, inside rectangle bounds - distance 0.
			// Rectangle [-1,1]x[-1,1] at z=0. Segment (-0.5,0,0)->(0.5,0,0) lies in z=0.
			// Both points are inside the rectangle -> distance = 0.
			assertEpsilonEquals(0., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					-1., -1., 1., 1., 0.,
					-0.5, 0., 0., 0.5, 0., 0.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(-.5, 0., 0.), onSegment);
			assertEpsilonEquals(createPoint(-.5, 0., 0.), onPlane);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			// CASE 6: Segment coplanar with the rectangle plane, entirely outside rectangle bounds.
			// Rectangle [1,2]x[1,2] at z=0. Segment (4,0,0)->(5,0,0).
			// Closest rect point = (2,1,0) (corner), closest segment point = (4,0,0) (S1).
			// sqDist = (4-2)^2+(0-1)^2+(0-0)^2 = 4+1 = 5.
			assertEpsilonEquals(5., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					1., 1., 2., 2., 0.,
					4., 0., 0., 5., 0., 0.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(4., 0., 0.), onSegment);
			assertEpsilonEquals(createPoint(2., 1., 0.), onPlane);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			// CASE 7: Segment parallel to the XY plane (no z change), hovering above.
			// Rectangle [-1,1]x[-1,1] at z=0. Segment (-0.5,0,3)->(0.5,0,3).
			// Infinite number of solution points on segment -> select the first found (-0.5,0,3).
			// Closest point on the rectangle surface (-0.5, 0, 0).
			// sqDist = 0^2+0^2+3^2 = 9.
			assertEpsilonEquals(9., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					-1., -1., 1., 1., 0.,
					-0.5, 0., 3., 0.5, 0., 3.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(-.5, 0., 3.), onSegment);
			assertEpsilonEquals(createPoint(-.5, 0., 0.), onPlane);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			// CASE 8: Segment parallel to XY plane, outside XY bounds, hovering above.
			// Rectangle [0,1]x[0,1] at z=0. Segment (-3,0.5,2)->(-1,0.5,2).
			// Closest rect point = (0,0.5,0) (edge x=0 point clamped to rect).
			// Closest segment point = (-1,0.5,2) = S2 (closest end to rect).
			// sqDist = (-1-0)^2+(0.5-0.5)^2+(2-0)^2 = 1+0+4 = 5.
			assertEpsilonEquals(5., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					0., 0., 1., 1., 0.,
					-3., 0.5, 2., -1., 0.5, 2.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(-1., 0.5, 2.), onSegment);
			assertEpsilonEquals(createPoint(0., 0.5, 0.), onPlane);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			// CASE 9: Degenerate segment (both endpoints coincide) - point above interior.
			// Rectangle [-1,1]x[-1,1] at z=0. Segment (0,0,4)->(0,0,4) (point).
			// Closest rect point = (0,0,0), sqDist = 0^2+0^2+4^2 = 16.
			assertEpsilonEquals(16., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					-1., -1., 1., 1., 0.,
					0., 0., 4., 0., 0., 4.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(0., 0., 4.), onSegment);
			assertEpsilonEquals(createPoint(0., 0., 0.), onPlane);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			// CASE 10: Degenerate segment - point above a corner of the rectangle.
			// Rectangle [0,2]x[0,2] at z=0. Segment (0,0,3)->(0,0,3).
			// Closest rect point = (0,0,0) (corner), sqDist = 0^2+0^2+3^2 = 9.
			assertEpsilonEquals(9., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					0., 0., 2., 2., 0.,
					0., 0., 3., 0., 0., 3.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(0., 0., 3.), onSegment);
			assertEpsilonEquals(createPoint(0., 0., 0.), onPlane);
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			// CASE 11: Segment endpoint on the rectangle plane, XY inside rect - dist 0.
			// Rectangle [-1,1]x[-1,1] at z=0. Segment (0,0,0)->(0,0,5).
			// S1=(0,0,0) is exactly on the plane and inside the rect -> distance = 0.
			assertEpsilonEquals(0., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					-1., -1., 1., 1., 0.,
					0., 0., 0., 0., 0., 5.,
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
			// Rectangle [0,2]x[0,2] at z=0. Segment (2,2,0)->(5,5,5).
			// S1=(2,2,0) is exactly at corner (2,2,0) -> distance = 0.
			assertEpsilonEquals(0., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					0., 0., 2., 2., 0.,
					2., 2., 0., 5., 5., 5.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(2., 2., 0.), onSegment);
			assertEpsilonEquals(createPoint(2., 2., 0.), onPlane);
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			// CASE 13: Segment entirely above the rectangle (no z change, z > rz).
			// Rectangle [0,4]x[0,4] at z=5. Segment (1,1,10)->(3,3,10).
			// Closest rect point = interior projection of midpoint (2,2,10) -> (2,2,5).
			// sqDist = (2-2)^2+(2-2)^2+(10-5)^2 = 25.
			assertEpsilonEquals(25., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					0., 0., 4., 4., 5.,
					1., 1., 10., 3., 3., 10.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(1., 1., 10.), onSegment);
			assertEpsilonEquals(createPoint(1., 1., 5.), onPlane);
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			// CASE 14: Segment above rect, closest point is near an edge (not corner).
			// Rectangle [0,4]x[0,4] at z=0. Segment (2,-3,2)->(2,3,2) spans across
			// the rect in Y. Closest segment point to plane interior is (2,0,2).
			// Closest rect point = (2,0,0) on bottom edge.
			// sqDist = (2-2)^2+(0-0)^2+(2-0)^2 = 4.
			assertEpsilonEquals(4., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					0., 0., 4., 4., 0.,
					2., -3., 2., 2., 3., 2.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(2., 0., 2.), onSegment);
			assertEpsilonEquals(createPoint(2., 0., 0.), onPlane);
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			// CASE 15: Segment approaches the rectangle from one side in 3D (skewed).
			// Rectangle [0,4]x[0,4] at z=0. Segment (2,2,3)->(2,2,-3).
			// Segment crosses the plane at (2,2,0) which is inside the rect -> dist=0.
			assertEpsilonEquals(0., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					0., 0., 4., 4., 0.,
					2., 2., 3., 2., 2., -3.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(2., 2., 0.), onSegment);
			assertEpsilonEquals(createPoint(2., 2., 0.), onPlane);
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			// CASE 16: Segment entirely below the rectangle plane (z1<rz, z2<rz), outside XY.
			// Rectangle [1,3]x[1,3] at z=5. Segment (0,0,-1)->(0,0,-3).
			// Both endpoints below plane, both outside XY. Closest seg point = S1=(0,0,-1).
			// Closest rect point = corner (1,1,5).
			// sqDist = (0-1)^2+(0-1)^2+(-1-5)^2 = 1+1+36 = 38.
			assertEpsilonEquals(38., PlaneXY3afp.findsClosestPointRectangleXYSegment(
					1., 1., 3., 3., 5.,
					0., 0., -1., 0., 0., -3.,
					onPlane, onSegment));
			assertEpsilonEquals(createPoint(0., 0., -1.), onSegment);
			assertEpsilonEquals(createPoint(1., 1., 5.), onPlane);
		}
	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsObject {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertTrue(getP().equals(getP()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(1, 0, 0, 0)));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(-1, 0, 0, 0)));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(0, 1, 0, 0)));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_6(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(0, -1, 0, 0)));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_7(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(0, 0, 1, 0)));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_8(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(0, 0, -1, 0)));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_9(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(1, 0, 0, 1.25)));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_10(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(-1, 0, 0, 1.25)));
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_11(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(0, 1, 0, 1.25)));
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_12(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(0, -1, 0, 1.25)));
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_13(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(0, 0, 1, 1.25)));
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_14(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertTrue(getP().equals(createPlane(0, 0, -1, 1.25)));
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_15(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(1, 0, 0, -1.25)));
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_16(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(-1, 0, 0, -1.25)));
		}

		@DisplayName("#17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_17(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(0, 1, 0, -1.25)));
		}

		@DisplayName("#18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_18(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(0, -1, 0, -1.25)));
		}

		@DisplayName("#19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_19(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(0, 0, 1, -1.25)));
		}

		@DisplayName("#20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_20(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);		
			assertFalse(getP().equals(createPlane(0, 0, -1, -1.25)));
		}
	}

}
