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

package org.arakhne.afc.math.geometry.base.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.OrientedPoint3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @param <P> the type for the points to be tested.
 * @param <V> the type for the vectors to be used during tests.
 * @param <Q> the type for the quaternions to be used during the tests.
 */
@SuppressWarnings("all")
public abstract class AbstractOrientedPoint3DTestCase<P extends OrientedPoint3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>>
		extends AbstractPoint3DTestCase<P, V, Q> {

	@BeforeEach
	public final void setUp() {
		super.setUp();
		var o = getT();
		o.set(o.getX(), o.getY(), o.getZ(), -4.5, -6., 7.);
	}
	
	private static Stream<Arguments> proposeArguments() {
		final List<Arguments> args = new ArrayList<>();
		for (final CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			for (int i = 0; i < 100; ++i) {
				double x = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double y = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double z = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				args.add(Arguments.of(cs, x, y, z));
			}
		}
		return args.stream();
	}

	private static Stream<Arguments> proposeArguments2() {
		final List<Arguments> args = new ArrayList<>();
		for (final CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			for (int i = 0; i < 100; ++i) {
				double x = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double y = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double z = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double a = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double b = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double c = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				args.add(Arguments.of(cs, x, y, z, a, b, c));
			}
		}
		return args.stream();
	}

	@DisplayName("setTangentX")
	@Nested
	public class SetTangentX {

		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setTangentX(1254);
			assertEpsilonEquals(1254, getT().getTangentX());
			assertEquals(1254, getT().itx());
	    }
	
		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setTangentX(1254.26);
			assertEpsilonEquals(1254.26, getT().getTangentX());
			assertEquals(1254, getT().itx());
	    }
	}

	@DisplayName("GetTangentX")
	@Nested
	public class GetTangentX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-4.5, getT().getTangentX());
	    }
	}

	@DisplayName("itx")
	@Nested
	public class Itx {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(-4, getT().itx());
	    }
	}

	@DisplayName("setTangentY")
	@Nested
	public class SetTangentY {

		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setTangentY(1254);
			assertEpsilonEquals(1254, getT().getTangentY());
			assertEquals(1254, getT().ity());
	    }
	
		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setTangentY(1254.26);
			assertEpsilonEquals(1254.26, getT().getTangentY());
			assertEquals(1254, getT().ity());
	    }
	}

	@DisplayName("GetTangentY")
	@Nested
	public class GetTangentY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-6., getT().getTangentY());
	    }
	}

	@DisplayName("ity")
	@Nested
	public class Ity {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(-6, getT().ity());
	    }
	}

	@DisplayName("setTangentZ")
	@Nested
	public class SetTangentZ {

		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setTangentZ(1254);
			assertEpsilonEquals(1254, getT().getTangentZ());
			assertEquals(1254, getT().itz());
	    }
	
		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setTangentZ(1254.26);
			assertEpsilonEquals(1254.26, getT().getTangentZ());
			assertEquals(1254, getT().itz());
	    }
	}

	@DisplayName("GetTangentZ")
	@Nested
	public class GetTangentZ {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(7., getT().getTangentZ());
	    }
	}

	@DisplayName("itz")
	@Nested
	public class Itz {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(7, getT().itz());
	    }
	}

	@DisplayName("setTangent")
	@Nested
	public class SetTangent {

		@DisplayName("(double,double,double)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractOrientedPoint3DTestCase#proposeArguments")
		public void doubledoubledouble_1(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setTangent(x, y , z);
			assertEpsilonEquals(x, getT().getTangentX());
			assertEpsilonEquals(y, getT().getTangentY());
			assertEpsilonEquals(z, getT().getTangentZ());
	    }

		@DisplayName("(Vector3D)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractOrientedPoint3DTestCase#proposeArguments")
		public void vector_1(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setTangent(createVector(x, y, z));
			assertEpsilonEquals(x, getT().getTangentX());
			assertEpsilonEquals(y, getT().getTangentY());
			assertEpsilonEquals(z, getT().getTangentZ());
	    }
	}

	@DisplayName("getTangent")
	@Nested
	public class GetTangent {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractOrientedPoint3DTestCase#proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setTangentX(x);
			getT().setTangentY(y);
			getT().setTangentZ(z);
			assertEpsilonEquals(createVector(x, y, z), getT().getTangent());
	    }
	}

	@DisplayName("setNormalX")
	@Nested
	public class SetNormalX {

		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setNormalX(1254);
			assertEpsilonEquals(1254, getT().getNormalX());
			assertEquals(1254, getT().inx());
	    }
	
		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setNormalX(1254.26);
			assertEpsilonEquals(1254.26, getT().getNormalX());
			assertEquals(1254, getT().inx());
	    }
	}

	@DisplayName("GetNormalX")
	@Nested
	public class GetNormalX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.0, getT().getNormalX());
	    }
	}

	@DisplayName("inx")
	@Nested
	public class Inx {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(0, getT().inx());
	    }
	}

	@DisplayName("setNormalY")
	@Nested
	public class SetNormalY {

		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setNormalY(1254);
			assertEpsilonEquals(1254, getT().getNormalY());
			assertEquals(1254, getT().iny());
	    }
	
		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setNormalY(1254.26);
			assertEpsilonEquals(1254.26, getT().getNormalY());
			assertEquals(1254, getT().iny());
	    }
	}

	@DisplayName("GetNormalY")
	@Nested
	public class GetNormalY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.0, getT().getNormalY());
	    }
	}

	@DisplayName("iny")
	@Nested
	public class Iny {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(0, getT().iny());
	    }
	}

	@DisplayName("setNormalZ")
	@Nested
	public class SetNormalZ {

		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setNormalZ(1254);
			assertEpsilonEquals(1254, getT().getNormalZ());
			assertEquals(1254, getT().inz());
	    }
	
		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setNormalZ(1254.26);
			assertEpsilonEquals(1254.26, getT().getNormalZ());
			assertEquals(1254, getT().inz());
	    }
	}

	@DisplayName("GetNormalZ")
	@Nested
	public class GetNormalZ {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.0, getT().getNormalZ());
	    }
	}

	@DisplayName("inz")
	@Nested
	public class Inz {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(0, getT().inz());
	    }
	}

	@DisplayName("setNormal")
	@Nested
	public class SetNormal {

		@DisplayName("(double,double,double)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractOrientedPoint3DTestCase#proposeArguments")
		public void doubledoubledouble_1(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setNormal(x, y , z);
			assertEpsilonEquals(x, getT().getNormalX());
			assertEpsilonEquals(y, getT().getNormalY());
			assertEpsilonEquals(z, getT().getNormalZ());
	    }

		@DisplayName("(Vector3D)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractOrientedPoint3DTestCase#proposeArguments")
		public void vector_1(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setNormal(createVector(x, y, z));
			assertEpsilonEquals(x, getT().getNormalX());
			assertEpsilonEquals(y, getT().getNormalY());
			assertEpsilonEquals(z, getT().getNormalZ());
	    }
	}

	@DisplayName("getNormal")
	@Nested
	public class GetNormal {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractOrientedPoint3DTestCase#proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setNormalX(x);
			getT().setNormalY(y);
			getT().setNormalZ(z);
			assertEpsilonEquals(createVector(x, y, z), getT().getNormal());
	    }
	}

	
	
	
	
	@DisplayName("GetSwayX")
	@Nested
	public class GetSwayX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.0, getT().getSwayX());
	    }
	}

	@DisplayName("isx")
	@Nested
	public class Isx {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(0, getT().isx());
	    }
	}

	@DisplayName("GetSwayY")
	@Nested
	public class GetSwayY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.0, getT().getSwayY());
	    }
	}

	@DisplayName("isy")
	@Nested
	public class Isy {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(0, getT().isy());
	    }
	}

	@DisplayName("GetSwayZ")
	@Nested
	public class GetSwayZ {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.0, getT().getSwayZ());
	    }
	}

	@DisplayName("isz")
	@Nested
	public class Isz {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(0, getT().isz());
	    }
	}

	@DisplayName("getSway")
	@Nested
	public class GetSway {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractOrientedPoint3DTestCase#proposeArguments2")
		public void test_1(CoordinateSystem3D cs, Double x, Double y, Double z, Double a, Double b, Double c) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var n = createVector(x, y, z);
			getT().setNormal(n);
			var t = createVector(a, b ,c);
			getT().setTangent(t);
			var s = t.cross(n);
			assertEpsilonEquals(s, getT().getSway());
	    }
	}

}
