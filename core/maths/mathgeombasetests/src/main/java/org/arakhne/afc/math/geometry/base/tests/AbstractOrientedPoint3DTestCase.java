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
import org.arakhne.afc.math.geometry.base.d3.Tuple3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("all")
public abstract class AbstractOrientedPoint3DTestCase<P extends OrientedPoint3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>,
		TT extends Tuple3D>
		extends AbstractPoint3DTestCase<P, V, Q, TT> {

	private P orientedPoint;

	@BeforeEach
	public final void setUp() {
		this.orientedPoint = createPoint(0, 0, 0);
		this.orientedPoint.set(12.354, -457.4, 124.5, -4.5, -6., 7.);
	}

	protected P getOP() {
		return this.orientedPoint;
	}
	
	private Stream<Arguments> proposeArguments() {
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

	private Stream<Arguments> proposeArguments2() {
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
			getOP().setTangentX(1254);
			assertEpsilonEquals(1254, getOP().getTangentX());
			assertEquals(1254, getOP().itx());
	    }
	
		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getOP().setTangentX(1254.26);
			assertEpsilonEquals(1254.26, getOP().getTangentX());
			assertEquals(1254, getOP().itx());
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
			assertEpsilonEquals(.0, getOP().getTangentX());
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
			assertEquals(0, getOP().itx());
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
			getOP().setTangentY(1254);
			assertEpsilonEquals(1254, getOP().getTangentY());
			assertEquals(1254, getOP().ity());
	    }
	
		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getOP().setTangentY(1254.26);
			assertEpsilonEquals(1254.26, getOP().getTangentY());
			assertEquals(1254, getOP().ity());
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
			assertEpsilonEquals(.0, getOP().getTangentY());
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
			assertEquals(0, getOP().ity());
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
			getOP().setTangentZ(1254);
			assertEpsilonEquals(1254, getOP().getTangentZ());
			assertEquals(1254, getOP().itz());
	    }
	
		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getOP().setTangentZ(1254.26);
			assertEpsilonEquals(1254.26, getOP().getTangentZ());
			assertEquals(1254, getOP().itz());
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
			assertEpsilonEquals(.0, getOP().getTangentZ());
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
			assertEquals(0, getOP().itz());
	    }
	}

	@DisplayName("setTangent")
	@Nested
	public class SetTangent {

		@DisplayName("(double,double,double)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void doubledoubledouble_1(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getOP().setTangent(x, y , z);
			assertEpsilonEquals(x, getOP().getTangentX());
			assertEpsilonEquals(y, getOP().getTangentY());
			assertEpsilonEquals(z, getOP().getTangentZ());
	    }

		@DisplayName("(Vector3D)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void vector_1(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getOP().setTangent(createVector(x, y, z));
			assertEpsilonEquals(x, getOP().getTangentX());
			assertEpsilonEquals(y, getOP().getTangentY());
			assertEpsilonEquals(z, getOP().getTangentZ());
	    }
	}

	@DisplayName("getTangent")
	@Nested
	public class GetTangent {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getOP().setTangentX(x);
			getOP().setTangentY(y);
			getOP().setTangentZ(z);
			assertEpsilonEquals(createVector(x, y, z), getOP().getTangent());
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
			getOP().setNormalX(1254);
			assertEpsilonEquals(1254, getOP().getNormalX());
			assertEquals(1254, getOP().inx());
	    }
	
		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getOP().setNormalX(1254.26);
			assertEpsilonEquals(1254.26, getOP().getNormalX());
			assertEquals(1254, getOP().inx());
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
			assertEpsilonEquals(.0, getOP().getNormalX());
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
			assertEquals(0, getOP().inx());
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
			getOP().setNormalY(1254);
			assertEpsilonEquals(1254, getOP().getNormalY());
			assertEquals(1254, getOP().iny());
	    }
	
		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getOP().setNormalY(1254.26);
			assertEpsilonEquals(1254.26, getOP().getNormalY());
			assertEquals(1254, getOP().iny());
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
			assertEpsilonEquals(.0, getOP().getNormalY());
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
			assertEquals(0, getOP().iny());
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
			getOP().setNormalZ(1254);
			assertEpsilonEquals(1254, getOP().getNormalZ());
			assertEquals(1254, getOP().inz());
	    }
	
		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void double_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getOP().setNormalZ(1254.26);
			assertEpsilonEquals(1254.26, getOP().getNormalZ());
			assertEquals(1254, getOP().inz());
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
			assertEpsilonEquals(.0, getOP().getNormalZ());
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
			assertEquals(0, getOP().inz());
	    }
	}

	@DisplayName("setNormal")
	@Nested
	public class SetNormal {

		@DisplayName("(double,double,double)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void doubledoubledouble_1(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getOP().setNormal(x, y , z);
			assertEpsilonEquals(x, getOP().getNormalX());
			assertEpsilonEquals(y, getOP().getNormalY());
			assertEpsilonEquals(z, getOP().getNormalZ());
	    }

		@DisplayName("(Vector3D)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void vector_1(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getOP().setNormal(createVector(x, y, z));
			assertEpsilonEquals(x, getOP().getNormalX());
			assertEpsilonEquals(y, getOP().getNormalY());
			assertEpsilonEquals(z, getOP().getNormalZ());
	    }
	}

	@DisplayName("getNormal")
	@Nested
	public class GetNormal {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double x, Double y, Double z) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getOP().setNormalX(x);
			getOP().setNormalY(y);
			getOP().setNormalZ(z);
			assertEpsilonEquals(createVector(x, y, z), getOP().getNormal());
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
			assertEpsilonEquals(.0, getOP().getSwayX());
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
			assertEquals(0, getOP().isx());
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
			assertEpsilonEquals(.0, getOP().getSwayY());
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
			assertEquals(0, getOP().isy());
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
			assertEpsilonEquals(.0, getOP().getSwayZ());
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
			assertEquals(0, getOP().isz());
	    }
	}

	@DisplayName("getSway")
	@Nested
	public class GetSway {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments2")
		public void test_1(CoordinateSystem3D cs, Double x, Double y, Double z, Double a, Double b, Double c) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var n = createVector(x, y, z);
			getOP().setNormal(n);
			var t = createVector(a, b ,c);
			getOP().setTangent(t);
			var s = t.cross(n);
			assertEpsilonEquals(s, getOP().getSway());
	    }
	}

}
