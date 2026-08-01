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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.arakhne.afc.math.GeogebraUtil;
import org.arakhne.afc.math.GnuOctaveUtil;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.base.d3.ImmutableVector3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.UnmodifiableQuaternion;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion.AxisAngle;
import org.arakhne.afc.math.geometry.base.d3.Quaternion.EulerAngles;
import org.arakhne.afc.math.geometry.base.d3.Quaternion.QuaternionComponents;
import org.arakhne.afc.vmutil.annotations.ScalaOperator;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("all")
public abstract class AbstractQuaternionTestCase<TQ extends Quaternion<?, ?, ?>>
		extends AbstractMathTestCase {
	
	protected TQ q;

	public TQ getQ() {
		return this.q;
	}

	@BeforeEach
	public void setUp() {
		this.q = createQuaternion(1, -2, 0, 3);
	}
	
	@AfterEach
	public void tearDown() {
		this.q = null;
	}

	public abstract TQ createQuaternion(double x, double y, double z, double w);

	public abstract TQ createQuaternion();

	public abstract boolean isIntCoordinates();

	private Stream<Arguments> proposeArguments() {
		final List<Arguments> args = new ArrayList<>();
		for (final CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			for (int i = 0; i < 100; ++i) {
				double a = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double b = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double c = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double d = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				args.add(Arguments.of(cs, a, b, c, d));
			}
		}
		return args.stream();
	}

	private Stream<Arguments> proposeArguments2() {
		final List<Arguments> args = new ArrayList<>();
		for (final CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			for (int i = 0; i < 100; ++i) {
				double a = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double b = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double c = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double d = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double v = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				args.add(Arguments.of(cs, a, b, c, d, v));
			}
		}
		return args.stream();
	}

	private Stream<Arguments> proposeArguments3() {
		final List<Arguments> args = new ArrayList<>();
		for (final CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			for (int i = 0; i < 100; ++i) {
				double a = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double b = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double c = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double d = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double x = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double y = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double z = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				double w = getRandom().nextDouble() * 1000. * (getRandom().nextBoolean() ? 1. : -1);
				args.add(Arguments.of(cs, a, b, c, d, x, y, z, w));
			}
		}
		return args.stream();
	}

	@DisplayName("clone")
	@Nested
	public class Clone {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());
			var q2 = q1.clone();
			assertEpsilonEquals(q1, q2);
		}
	}

	@DisplayName("getX")
	@Nested
	public class GetX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			assertEpsilonEquals(a, q1.getX() * Math.sqrt(a * a + b * b + c * c + d * d));
		}
	}

	@DisplayName("setX")
	@Nested
	public class SetX {

		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments2")
		public void double_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double v) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d); 
			q1.setX(v);
			assertEpsilonEquals(v,q1.getX());
		}
		
		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("getY")
	@Nested
	public class GetY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			assertEpsilonEquals(b, q1.getY() * Math.sqrt(a * a + b * b + c * c + d * d));
		}
	}

	@DisplayName("setY")
	@Nested
	public class SetY {

		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments2")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double v) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d); 
			q1.setY(v);
			assertEpsilonEquals(v,q1.getY());
		}
		
		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("getZ")
	@Nested
	public class GetZ {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			assertEpsilonEquals(c, q1.getZ() * Math.sqrt(a * a + b * b + c * c + d * d));
		}
	}

	@DisplayName("setZ")
	@Nested
	public class SetZ {

		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments2")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double v) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d); 
			q1.setZ(v);
			assertEpsilonEquals(v,q1.getZ());
		}
		
		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("getW")
	@Nested
	public class GetW {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			assertEpsilonEquals(d, q1.getW() * Math.sqrt(a * a + b * b + c * c + d * d));
		}
	}

	@DisplayName("setW")
	@Nested
	public class SetW {

		@DisplayName("(double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments2")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double v) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d); 
			q1.setW(v);
			assertEpsilonEquals(v,q1.getW());
		}
		
		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void int_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("set(double,double,double,double)")
	@Nested
	public class Set {

		@DisplayName("(double,double,double,double)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double x, Double y, Double z, Double w) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(); 
			q.set(x, y, z, w);
			assertEpsilonEquals(x, q.getX());
			assertEpsilonEquals(y, q.getY());
			assertEpsilonEquals(z, q.getZ());
			assertEpsilonEquals(w, q.getW());
		}
	
		@DisplayName("(Quaternion4d)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_2(CoordinateSystem3D cs, Double x, Double y, Double z, Double w) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(); 
			q.set(createQuaternion(x, y, z, w));
			assertEpsilonEquals(createQuaternion(x, y, z, w), q);
		}
	}

	@DisplayName("equals(Quaternion4d)")
	@Nested
	public class EqualsObject {
		
		@DisplayName("(Quaternion4d) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var q2 = createQuaternion(a,b,c,d);
			assertTrue(q1.equals(q2));
		}

		@DisplayName("(Quaternion4d) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_2(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var q2 = createQuaternion(a,b,c,d);
			q2.set(b,a,d,c);
			assertFalse(q1.equals(q2));
		}
	}

	@DisplayName("epsilonEquals(Quaternion4d)")
	@Nested
	public class EpsilonEquals {
		
		@DisplayName("(Quaternion4d) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var q2 = createQuaternion(a,b,c,d);
			assertTrue(q1.epsilonEquals(q2, EPSILON));
		}

		@DisplayName("(Quaternion4d) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_2(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var q2 = createQuaternion(a,b,c,d);
			q2.set(b,a,d,c);
			assertFalse(q1.epsilonEquals(q2, EPSILON));
		}
	}

	@DisplayName("normalize(Quaternion4d)")
	@Nested
	public class Normalize {

		@DisplayName("(Quaternion4d)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion();
			q1.setW(a * 50.);
			q1.setX(b * 50.);
			q1.setY(c * 50.);
			q1.setZ(d * 50.);
			var q2 = createQuaternion(b,c,d,a);
			var q3 = createQuaternion();
			q3.normalize(q1);
			assertEpsilonEquals(q2, q3);
		}
	
		@DisplayName("()")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_2(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion();
			q1.setW(a * 50.);
			q1.setX(b * 50.);
			q1.setY(c * 50.);
			q1.setZ(d * 50.);
			var q2 = createQuaternion(b,c,d,a);
			q1.normalize();
			assertEpsilonEquals(q2, q1);
		}
	}

	@DisplayName("inverse")
	@Nested
	public class Inverse {

		@DisplayName("(Quaternion4d)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var expected = createQuaternion(-q1.getX(), -q1.getY(), -q1.getZ(), q1.getW());
			var inv = createQuaternion();
			inv.inverse(q1);
			assertEpsilonEquals(expected, inv);
		}
	
		@DisplayName("()")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_2(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var expected = createQuaternion(-q1.getX(), -q1.getY(), -q1.getZ(), q1.getW());
			var inv = createQuaternion(a,b,c,d);
			inv.inverse();
			assertEpsilonEquals(expected, inv);
		}
	}

	@DisplayName("conjugate")
	@Nested
	public class Conjugate {

		@DisplayName("(Quaternion4d)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var expected = createQuaternion(-q1.getX(), -q1.getY(), -q1.getZ(), q1.getW());
			var inv = createQuaternion();
			inv.conjugate(q1);
			assertEpsilonEquals(expected, inv);
		}
	
		@DisplayName("()")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments")
		public void test_2(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var expected = createQuaternion(-q1.getX(), -q1.getY(), -q1.getZ(), q1.getW());
			var inv = createQuaternion(a,b,c,d);
			inv.conjugate();
			assertEpsilonEquals(expected, inv);
		}
	}

	@DisplayName("mul")
	@Nested
	public class Mul {

		@DisplayName("(Quaternion4d,Quaternion4d)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments3")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double x, Double y, Double z, Double w) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var q2 = createQuaternion(a,b,c,d);
			var q3 = createQuaternion(x,y,z,w);
			var mul = createQuaternion();
			mul.setW(q1.getW()*q3.getW()-q1.getX()*q3.getX()-q1.getY()*q3.getY()-q1.getZ()*q3.getZ());
			mul.setX(q1.getW()*q3.getX()+q1.getX()*q3.getW()+q1.getY()*q3.getZ()-q1.getZ()*q3.getY());
			mul.setY(q1.getW()*q3.getY()+q1.getY()*q3.getW()-q1.getX()*q3.getZ()+q1.getZ()*q3.getX());
			mul.setZ(q1.getZ()*q3.getW()+q1.getW()*q3.getZ()+q1.getX()*q3.getY()-q1.getY()*q3.getX());
			q2.mul(q1,q3);
			assertEpsilonEquals(mul, q2);
		}
	
		@DisplayName("(Quaternion4d)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments3")
		public void test_2(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double x, Double y, Double z, Double w) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var q2 = createQuaternion(x,y,z,w);
			var mul = createQuaternion();
			mul.setW(q1.getW()*q2.getW()-q1.getX()*q2.getX()-q1.getY()*q2.getY()-q1.getZ()*q2.getZ());
			mul.setX(q1.getW()*q2.getX()+q1.getX()*q2.getW()+q1.getY()*q2.getZ()-q1.getZ()*q2.getY());
			mul.setY(q1.getW()*q2.getY()+q1.getY()*q2.getW()-q1.getX()*q2.getZ()+q1.getZ()*q2.getX());
			mul.setZ(q1.getZ()*q2.getW()+q1.getW()*q2.getZ()+q1.getX()*q2.getY()-q1.getY()*q2.getX());
			q1.mul(q2);
			assertEpsilonEquals(mul, q1);
		}
	}

	@DisplayName("mulInverse")
	@Nested
	public class MulInverse {

		@DisplayName("(Quaternion4d,Quaternion4d)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments3")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double x, Double y, Double z, Double w) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var q2 = createQuaternion(x,y,z,w);
			var mulInv = createQuaternion();
			mulInv.mulInverse(q1, q2);
			q2.inverse();
			q1.mul(q2);
			assertEpsilonEquals(q1, mulInv);
		}
	
		@DisplayName("(Quaternion4d)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("proposeArguments3")
		public void test_2(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double x, Double y, Double z, Double w) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var q2 = createQuaternion(x,y,z,w);
			var cloneQ1 = q1.clone();
			q1.mulInverse(q2);
			q2.inverse();
			cloneQ1.mul(q2);
			assertEpsilonEquals(q1, cloneQ1);
		}
	}

	@DisplayName("isEpsilonEquals")
	@Nested
	public class IsEpsilonEquals {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("computeWithAxisAngle")
	@Nested
	public class ComputeWithAxisAngle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("computeAxisAngle")
	@Nested
	public class ComputeAxisAngle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("computeWithEulerAngles")
	@Nested
	public class computeWithEulerAngles {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("computeEulerAngles")
	@Nested
	public class computeEulerAngles {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("ix")
	@Nested
	public class Ix {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("iy")
	@Nested
	public class Iy {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("iz")
	@Nested
	public class Iz {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("iw")
	@Nested
	public class Iw {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("getGeomFactory")
	@Nested
	public class GetGeomFactory {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("setAxisAngle")
	@Nested
	public class SetAxisAngle {

		@DisplayName("(double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(Vector3D,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void vectordouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(AxisAngle) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void axisangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("getAxis")
	@Nested
	public class GetAxis {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("getAngle")
	@Nested
	public class GetAngle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("getAxisAngle")
	@Nested
	public class GetAxisAngle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("interpolate")
	@Nested
	public class Interpolate {

		@DisplayName("(Quaternion,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaterniondouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(Quaternion,Quaternion,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaternionquaterniondouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("setEulerAngles")
	@Nested
	public class SetEulerAngles {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(double,double,double,CoordinateSystem) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledoublecoordinatesystem_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(EulerAngles) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void eulerangles_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("getEulerAngles")
	@Nested
	public class GetEulerAngles {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}

		@DisplayName("(CoordinateSystem) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void cs_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("toUnmodifiable")
	@Nested
	public class ToUnmodifiable {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("this * Quaternion")
	@Nested
	public class OperatorMultiplyQuaternion {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

	@DisplayName("this / Quaternion")
	@Nested
	public class OperatorDivideQuaternion {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			throw new UnsupportedOperationException();
		}
	}

}
