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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Tuple3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("all")
public abstract class AbstractQuaternionTestCase<TQ extends Quaternion<?, ? super TV, ?>, TV extends Vector3D<?, ?, ? super TQ>>
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

	public TQ createQuaternionFromAxisAngle(double x, double y, double z, double angle) {
		final var q = createQuaternion();
		q.setAxisAngle(x, y, z, angle);
		return q;
	}
	
	public TQ createQuaternionFromAxisAngle(Tuple3D<?> axis, double angle) {
		return createQuaternionFromAxisAngle(axis.getX(), axis.getY(), axis.getZ(), angle);
	}

	public Quaternion.EulerAngles createEulerAngles(double heading, double attitude, double bank, CoordinateSystem3D system) {
		return new Quaternion.EulerAngles(attitude, bank, heading, system);
	}

	public Quaternion.AxisAngle createAxisAngle(double x, double y, double z, double angle) {
		return new Quaternion.AxisAngle(x, y, z, angle, createVector(x, y ,z).toUnmodifiable());
	}

	public abstract TV createVector(double x, double y, double z);

	public abstract boolean isIntCoordinates();

	private static Stream<Arguments> proposeArguments() {
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

	private static Stream<Arguments> proposeArguments2() {
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

	private static Stream<Arguments> proposeArguments3() {
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
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
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
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments2")
		public void double_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double v) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d); 
			q1.setX(v.doubleValue());
			assertEpsilonEquals(v.doubleValue(),q1.getX());
		}
		
		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments2")
		public void int_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double v) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d); 
			q1.setX(v.intValue());
			assertEpsilonEquals(v.intValue(),q1.getX());
		}
	}

	@DisplayName("getY")
	@Nested
	public class GetY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
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
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments2")
		public void double_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double v) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d); 
			q1.setY(v.doubleValue());
			assertEpsilonEquals(v.doubleValue(),q1.getY());
		}
		
		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments2")
		public void int_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double v) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d); 
			q1.setY(v.intValue());
			assertEpsilonEquals(v.intValue(),q1.getY());
		}
	}

	@DisplayName("getZ")
	@Nested
	public class GetZ {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
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
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments2")
		public void double_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double v) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d); 
			q1.setZ(v.doubleValue());
			assertEpsilonEquals(v.doubleValue(),q1.getZ());
		}
		
		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments2")
		public void int_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double v) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d); 
			q1.setZ(v.intValue());
			assertEpsilonEquals(v.intValue(),q1.getZ());
		}
	}

	@DisplayName("getW")
	@Nested
	public class GetW {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
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
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments2")
		public void double_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double v) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d); 
			q1.setW(v.doubleValue());
			assertEpsilonEquals(v.doubleValue(),q1.getW());
		}
		
		@DisplayName("(int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments2")
		public void int_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double v) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d); 
			q1.setW(v.intValue());
			assertEpsilonEquals(v.intValue(),q1.getW());
		}
	}

	@DisplayName("set")
	@Nested
	public class Set {

		@DisplayName("(double,double,double,double)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double x, Double y, Double z, Double w) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var mag = 1. / Math.sqrt(x * x + y * y + z * z + w * w);
			var q = createQuaternion(); 
			q.set(x, y, z, w);
			assertEpsilonEquals(x * mag, q.getX());
			assertEpsilonEquals(y * mag, q.getY());
			assertEpsilonEquals(z * mag, q.getZ());
			assertEpsilonEquals(w * mag, q.getW());
		}
	
		@DisplayName("(Quaternion4d)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void test_2(CoordinateSystem3D cs, Double x, Double y, Double z, Double w) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var mag = 1. / Math.sqrt(x * x + y * y + z * z + w * w);
			var q = createQuaternion(); 
			q.set(createQuaternion(x, y, z, w));
			assertEpsilonEquals(x * mag, q.getX());
			assertEpsilonEquals(y * mag, q.getY());
			assertEpsilonEquals(z * mag, q.getZ());
			assertEpsilonEquals(w * mag, q.getW());
		}
	}

	@DisplayName("equals")
	@Nested
	public class EqualsObject {
		
		@DisplayName("(Quaternion4d) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var q2 = createQuaternion(a,b,c,d);
			assertTrue(q1.equals(q2));
		}

		@DisplayName("(Quaternion4d) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
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

		private static double noise(double value) {
			return value + EPSILON * (getRandom().nextBoolean() ? .75 : -.75);
		}
		
		@DisplayName("(Quaternion4d) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var q2 = createQuaternion(noise(a), noise(b), noise(c), noise(d));
			assertTrue(q1.epsilonEquals(q2, EPSILON));
		}

		@DisplayName("(Quaternion4d) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void test_2(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var q2 = createQuaternion(noise(a), noise(b), noise(c), noise(d));
			q2.set(b,a,d,c);
			assertFalse(q1.epsilonEquals(q2, EPSILON));
		}
	}

	@DisplayName("normalize")
	@Nested
	public class Normalize {

		@DisplayName("(Quaternion4d)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var mag = 1. / Math.sqrt(a * a + b * b + c * c + d * d);
			var q1 = createQuaternion();
			q1.setX(a);
			q1.setY(b);
			q1.setZ(c);
			q1.setW(d);
			var q3 = createQuaternion();
			q3.normalize(q1);
			assertEpsilonEquals(a * mag, q3.getX());
			assertEpsilonEquals(b * mag, q3.getY());
			assertEpsilonEquals(c * mag, q3.getZ());
			assertEpsilonEquals(d * mag, q3.getW());
		}
	
		@DisplayName("()")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void test_2(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var mag = 1. / Math.sqrt(a * a + b * b + c * c + d * d);
			var q1 = createQuaternion();
			q1.setX(a);
			q1.setY(b);
			q1.setZ(c);
			q1.setW(d);
			q1.normalize();
			assertEpsilonEquals(a * mag, q1.getX());
			assertEpsilonEquals(b * mag, q1.getY());
			assertEpsilonEquals(c * mag, q1.getZ());
			assertEpsilonEquals(d * mag, q1.getW());
		}
	}

	@DisplayName("inverse")
	@Nested
	public class Inverse {

		@DisplayName("(Quaternion4d)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var mag = 1. / Math.sqrt(a * a + b * b + c * c + d * d);
			var q1 = createQuaternion(a, b, c, d);
			var inv = createQuaternion();
			inv.inverse(q1);
			assertEpsilonEquals(-a * mag, inv.getX());
			assertEpsilonEquals(-b * mag, inv.getY());
			assertEpsilonEquals(-c * mag, inv.getZ());
			assertEpsilonEquals(d * mag, inv.getW());
		}
	
		@DisplayName("()")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void test_2(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var mag = 1. / Math.sqrt(a * a + b * b + c * c + d * d);
			var q1 = createQuaternion(a, b, c, d);
			q1.inverse();
			assertEpsilonEquals(-a * mag, q1.getX());
			assertEpsilonEquals(-b * mag, q1.getY());
			assertEpsilonEquals(-c * mag, q1.getZ());
			assertEpsilonEquals(d * mag, q1.getW());
		}
	}

	@DisplayName("conjugate")
	@Nested
	public class Conjugate {

		@DisplayName("(Quaternion4d)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion();
			q1.setX(a);
			q1.setY(b);
			q1.setZ(c);
			q1.setW(d);
			var inv = createQuaternion();
			inv.conjugate(q1);
			assertEpsilonEquals(-a, inv.getX());
			assertEpsilonEquals(-b, inv.getY());
			assertEpsilonEquals(-c, inv.getZ());
			assertEpsilonEquals(d, inv.getW());
		}
	
		@DisplayName("()")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void test_2(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion();
			q1.setX(a);
			q1.setY(b);
			q1.setZ(c);
			q1.setW(d);
			q1.conjugate();
			assertEpsilonEquals(-a, q1.getX());
			assertEpsilonEquals(-b, q1.getY());
			assertEpsilonEquals(-c, q1.getZ());
			assertEpsilonEquals(d, q1.getW());
		}
	}

	@DisplayName("mul")
	@Nested
	public class Mul {

		@DisplayName("(Quaternion4d,Quaternion4d) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments3")
		public void quatquat_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double x, Double y, Double z, Double w) {
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
		
		@DisplayName("(Quaternion4d,Quaternion4d) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quatquat_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(1,0,0,0);
			var q2 = createQuaternion(0,1,0,0);
			var result = createQuaternion();
			result.mul(q1,q2);
			assertEpsilonEquals(0., result.getX());
			assertEpsilonEquals(0., result.getY());
			assertEpsilonEquals(1., result.getZ());
			assertEpsilonEquals(0., result.getW());
		}
		
		@DisplayName("(Quaternion4d,Quaternion4d) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quatquat_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var identity = createQuaternion(0, 0, 0, 1);
			var q = createQuaternion(0.5, -0.5, 0.5, -0.5); // already unit
			var result = createQuaternion();
			result.mul(identity, q);
			assertEpsilonEquals(q.getX(), result.getX());
			assertEpsilonEquals(q.getY(), result.getY());
			assertEpsilonEquals(q.getZ(), result.getZ());
			assertEpsilonEquals(q.getW(), result.getW());
		}

		@DisplayName("(Quaternion4d,Quaternion4d) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quatquat_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var identity = createQuaternion(0, 0, 0, 1);
			var q = createQuaternion(-0.5, 0.5, -0.5, 0.5); // already unit
			var result = createQuaternion();
			result.mul(q, identity);
			assertEpsilonEquals(q.getX(), result.getX());
			assertEpsilonEquals(q.getY(), result.getY());
			assertEpsilonEquals(q.getZ(), result.getZ());
			assertEpsilonEquals(q.getW(), result.getW());
		}

		@DisplayName("(Quaternion4d,Quaternion4d) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quatquat_5(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(1, 0, 0, 0); // i
			var result = createQuaternion();
			result.mul(q, q); // i*i = -1 => (0,0,0,-1)
			assertEpsilonEquals(0., result.getX());
			assertEpsilonEquals(0., result.getY());
			assertEpsilonEquals(0., result.getZ());
			assertEpsilonEquals(-1., result.getW());
		}

		@DisplayName("(Quaternion4d,Quaternion4d) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quatquat_6(CoordinateSystem3D cs) {
			// non commutative test
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(1, 0, 0, 0); // i
			var q2 = createQuaternion(0, 1, 0, 0); // j

			var r12 = createQuaternion();
			r12.mul(q1, q2); // k

			var r21 = createQuaternion();
			r21.mul(q2, q1); // -k

			assertEpsilonEquals(0., r12.getX());
			assertEpsilonEquals(0., r12.getY());
			assertEpsilonEquals(1., r12.getZ());
			assertEpsilonEquals(0., r12.getW());

			assertEpsilonEquals(0., r21.getX());
			assertEpsilonEquals(0., r21.getY());
			assertEpsilonEquals(-1., r21.getZ());
			assertEpsilonEquals(0., r21.getW());
		}

		@DisplayName("(Quaternion4d,Quaternion4d) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quatquat_7(CoordinateSystem3D cs) {
			// conjugate-product-gives-identity
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// Unit quaternion: (0.5, 0.5, 0.5, 0.5)
			var q = createQuaternion(0.5, 0.5, 0.5, 0.5);
			var qc = createQuaternion(-0.5, -0.5, -0.5, 0.5); // conjugate of unit quaternion

			var result = createQuaternion();
			result.mul(q, qc);

			assertEpsilonEquals(0., result.getX());
			assertEpsilonEquals(0., result.getY());
			assertEpsilonEquals(0., result.getZ());
			assertEpsilonEquals(1., result.getW());
		}

		@DisplayName("(Quaternion4d,Quaternion4d) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quatquat_8(CoordinateSystem3D cs) {
			// norm-preservation-for-unit-inputs
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(0.5, 0.5, 0.5, 0.5);
			var q2 = createQuaternion(0.5, -0.5, 0.5, -0.5);

			var result = createQuaternion();
			result.mul(q1, q2);

			var n2 = result.getX() * result.getX()
					+ result.getY() * result.getY()
					+ result.getZ() * result.getZ()
					+ result.getW() * result.getW();
			assertEpsilonEquals(1., n2);
		}

		@DisplayName("(Quaternion4d) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments3")
		public void quat_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double x, Double y, Double z, Double w) {
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

		@DisplayName("(Quaternion4d) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quat_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(1,0,0,0);
			var q2 = createQuaternion(0,1,0,0);
			q1.mul(q2);
			assertEpsilonEquals(0., q1.getX());
			assertEpsilonEquals(0., q1.getY());
			assertEpsilonEquals(1., q1.getZ());
			assertEpsilonEquals(0., q1.getW());
		}

		@DisplayName("(Quaternion4d) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quat_3(CoordinateSystem3D cs) {
			// in-place identity-right
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0.5, -0.5, 0.5, -0.5); // unit
			var identity = createQuaternion(0, 0, 0, 1);
			q.mul(identity); // q = q * 1 = q
			assertEpsilonEquals(0.5, q.getX());
			assertEpsilonEquals(-0.5, q.getY());
			assertEpsilonEquals(0.5, q.getZ());
			assertEpsilonEquals(-0.5, q.getW());
		}

		@DisplayName("(Quaternion4d) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quat_4(CoordinateSystem3D cs) {
			// in-place identity-left-equivalent-via-target
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0, 0, 0, 1); // identity as target (this)
			var r = createQuaternion(-0.5, 0.5, -0.5, 0.5); // unit
			q.mul(r); // q = 1 * r = r
			assertEpsilonEquals(r.getX(), q.getX());
			assertEpsilonEquals(r.getY(), q.getY());
			assertEpsilonEquals(r.getZ(), q.getZ());
			assertEpsilonEquals(r.getW(), q.getW());
		}

		@DisplayName("(Quaternion4d) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quat_5(CoordinateSystem3D cs) {
			// in-place self-times-self-i
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(1, 0, 0, 0); // i
			q.mul(createQuaternion(1, 0, 0, 0));  // i*i = -1
			assertEpsilonEquals(0., q.getX());
			assertEpsilonEquals(0., q.getY());
			assertEpsilonEquals(0., q.getZ());
			assertEpsilonEquals(-1., q.getW());
		}

		@DisplayName("(Quaternion4d) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quat_6(CoordinateSystem3D cs) {
			// in-place non-commutative
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);

			var q1 = createQuaternion(1, 0, 0, 0); // i
			q1.mul(createQuaternion(0, 1, 0, 0));  // i*j = k

			var q2 = createQuaternion(0, 1, 0, 0); // j
			q2.mul(createQuaternion(1, 0, 0, 0));  // j*i = -k

			assertEpsilonEquals(0., q1.getX());
			assertEpsilonEquals(0., q1.getY());
			assertEpsilonEquals(1., q1.getZ());
			assertEpsilonEquals(0., q1.getW());

			assertEpsilonEquals(0., q2.getX());
			assertEpsilonEquals(0., q2.getY());
			assertEpsilonEquals(-1., q2.getZ());
			assertEpsilonEquals(0., q2.getW());
		}

		@DisplayName("(Quaternion4d) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quat_7(CoordinateSystem3D cs) {
			// in-place conjugate-product-gives-identity
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0.5, 0.5, 0.5, 0.5);     // unit
			var qc = createQuaternion(-0.5, -0.5, -0.5, 0.5); // conjugate
			q.mul(qc); // q = q * qc = identity
			assertEpsilonEquals(0., q.getX());
			assertEpsilonEquals(0., q.getY());
			assertEpsilonEquals(0., q.getZ());
			assertEpsilonEquals(1., q.getW());
		}

		@DisplayName("(Quaternion4d) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quat_8(CoordinateSystem3D cs) {
			// in-place norm-preservation-for-unit-inputs
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0.5, 0.5, 0.5, 0.5);
			var r = createQuaternion(0.5, -0.5, 0.5, -0.5);
			q.mul(r);

			var n2 = q.getX() * q.getX()
					+ q.getY() * q.getY()
					+ q.getZ() * q.getZ()
					+ q.getW() * q.getW();
			assertEpsilonEquals(1., n2);
		}
	}

	@DisplayName("mulInverse")
	@Nested
	public class MulInverse {

		@DisplayName("(Quaternion4d,Quaternion4d) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments3")
		public void quatquat_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double x, Double y, Double z, Double w) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var q2 = createQuaternion(x,y,z,w);
			var mulInv = createQuaternion();
			mulInv.mulInverse(q1, q2);
			q2.inverse();
			q1.mul(q2);
			assertEpsilonEquals(q1, mulInv);
		}
	
		@DisplayName("(Quaternion4d,Quaternion4d) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quatquat_2(CoordinateSystem3D cs) {
			// mulInverse #1: same quaternion gives identity
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(0.5, 0.5, 0.5, 0.5); // unit
			var q2 = createQuaternion(0.5, 0.5, 0.5, 0.5); // same
			var result = createQuaternion();

			result.mulInverse(q1, q2); // q * q^-1 = identity

			assertEpsilonEquals(0., result.getX());
			assertEpsilonEquals(0., result.getY());
			assertEpsilonEquals(0., result.getZ());
			assertEpsilonEquals(1., result.getW());
		}

		@DisplayName("(Quaternion4d,Quaternion4d) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quatquat_3(CoordinateSystem3D cs) {
			// mulInverse #2: right identity
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(0.5, -0.5, 0.5, -0.5); // unit
			var q2 = createQuaternion(0., 0., 0., 1.);       // identity
			var result = createQuaternion();

			result.mulInverse(q1, q2); // q1 * 1^-1 = q1

			assertEpsilonEquals(q1.getX(), result.getX());
			assertEpsilonEquals(q1.getY(), result.getY());
			assertEpsilonEquals(q1.getZ(), result.getZ());
			assertEpsilonEquals(q1.getW(), result.getW());
		}

		@DisplayName("(Quaternion4d,Quaternion4d) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quatquat_4(CoordinateSystem3D cs) {
			// mulInverse #3: left identity gives inverse of q2
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(0., 0., 0., 1.);       // identity
			var q2 = createQuaternion(0.5, 0.5, 0.5, 0.5);   // unit
			var result = createQuaternion();

			result.mulInverse(q1, q2); // 1 * q2^-1 = q2^-1 = conjugate for unit quaternion

			assertEpsilonEquals(-0.5, result.getX());
			assertEpsilonEquals(-0.5, result.getY());
			assertEpsilonEquals(-0.5, result.getZ());
			assertEpsilonEquals(0.5, result.getW());
		}

		@DisplayName("(Quaternion4d,Quaternion4d) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quatquat_5(CoordinateSystem3D cs) {
			// mulInverse #4: i * j^-1 = -k
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(1., 0., 0., 0.); // i
			var q2 = createQuaternion(0., 1., 0., 0.); // j
			var result = createQuaternion();

			result.mulInverse(q1, q2); // i * j^-1 = i * (-j) = -k

			assertEpsilonEquals(0., result.getX());
			assertEpsilonEquals(0., result.getY());
			assertEpsilonEquals(-1., result.getZ());
			assertEpsilonEquals(0., result.getW());
		}

		@DisplayName("(Quaternion4d,Quaternion4d) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quatquat_6(CoordinateSystem3D cs) {
			// mulInverse #5: q2 preserved
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(0.5, 0.5, 0.5, 0.5);
			var q2 = createQuaternion(0.5, -0.5, 0.5, -0.5);
			var q2x = q2.getX();
			var q2y = q2.getY();
			var q2z = q2.getZ();
			var q2w = q2.getW();

			var result = createQuaternion();
			result.mulInverse(q1, q2);

			assertEpsilonEquals(q2x, q2.getX());
			assertEpsilonEquals(q2y, q2.getY());
			assertEpsilonEquals(q2z, q2.getZ());
			assertEpsilonEquals(q2w, q2.getW());
		}

		@DisplayName("(Quaternion4d,Quaternion4d) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quatquat_7(CoordinateSystem3D cs) {
			// mulInverse #6: q1 preserved
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(0.5, 0.5, -0.5, 0.5);
			var q2 = createQuaternion(0.5, -0.5, 0.5, -0.5);
			var q1x = q1.getX();
			var q1y = q1.getY();
			var q1z = q1.getZ();
			var q1w = q1.getW();

			var result = createQuaternion();
			result.mulInverse(q1, q2);

			assertEpsilonEquals(q1x, q1.getX());
			assertEpsilonEquals(q1y, q1.getY());
			assertEpsilonEquals(q1z, q1.getZ());
			assertEpsilonEquals(q1w, q1.getW());
		}

		@DisplayName("(Quaternion4d,Quaternion4d) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quatquat_8(CoordinateSystem3D cs) {
			// mulInverse #7: norm-preservation for unit inputs
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(0.5, 0.5, 0.5, 0.5);
			var q2 = createQuaternion(0.5, -0.5, 0.5, -0.5);
			var result = createQuaternion();

			result.mulInverse(q1, q2);

			var n2 = result.getX() * result.getX()
					+ result.getY() * result.getY()
					+ result.getZ() * result.getZ()
					+ result.getW() * result.getW();
			assertEpsilonEquals(1., n2);
		}
		
		@DisplayName("(Quaternion4d) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments3")
		public void quat_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double x, Double y, Double z, Double w) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var q2 = createQuaternion(x,y,z,w);
			var cloneQ1 = q1.clone();
			q1.mulInverse(q2);
			q2.inverse();
			cloneQ1.mul(q2);
			assertEpsilonEquals(q1, cloneQ1);
		}

		@DisplayName("(Quaternion4d) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quat_2(CoordinateSystem3D cs) {
			// mulInverse in-place #1: self mulInverse self = identity
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0.5, 0.5, 0.5, 0.5); // unit
			q.mulInverse(createQuaternion(0.5, 0.5, 0.5, 0.5)); // q * q^-1

			assertEpsilonEquals(0., q.getX());
			assertEpsilonEquals(0., q.getY());
			assertEpsilonEquals(0., q.getZ());
			assertEpsilonEquals(1., q.getW());
		}

		@DisplayName("(Quaternion4d) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quat_3(CoordinateSystem3D cs) {
			// mulInverse in-place #2: right identity leaves quaternion unchanged
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0.5, -0.5, 0.5, -0.5); // unit
			var identity = createQuaternion(0., 0., 0., 1.);

			q.mulInverse(identity); // q * 1^-1 = q

			assertEpsilonEquals(0.5, q.getX());
			assertEpsilonEquals(-0.5, q.getY());
			assertEpsilonEquals(0.5, q.getZ());
			assertEpsilonEquals(-0.5, q.getW());
		}

		@DisplayName("(Quaternion4d) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quat_4(CoordinateSystem3D cs) {
			// mulInverse in-place #3: identity mulInverse q gives inverse(q)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0., 0., 0., 1.);      // this = identity
			var other = createQuaternion(0.5, 0.5, 0.5, 0.5); // unit

			q.mulInverse(other); // 1 * other^-1

			assertEpsilonEquals(-0.5, q.getX());
			assertEpsilonEquals(-0.5, q.getY());
			assertEpsilonEquals(-0.5, q.getZ());
			assertEpsilonEquals(0.5, q.getW());
		}

		@DisplayName("(Quaternion4d) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quat_5(CoordinateSystem3D cs) {
			// mulInverse in-place #4: i mulInverse j = -k
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(1., 0., 0., 0.);      // i
			var other = createQuaternion(0., 1., 0., 0.);  // j

			q.mulInverse(other); // i * j^-1 = i * (-j) = -k

			assertEpsilonEquals(0., q.getX());
			assertEpsilonEquals(0., q.getY());
			assertEpsilonEquals(-1., q.getZ());
			assertEpsilonEquals(0., q.getW());
		}

		@DisplayName("(Quaternion4d) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quat_6(CoordinateSystem3D cs) {
			// mulInverse in-place #5: argument quaternion is preserved
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0.5, 0.5, -0.5, 0.5);
			var other = createQuaternion(0.5, -0.5, 0.5, -0.5);

			var ox = other.getX();
			var oy = other.getY();
			var oz = other.getZ();
			var ow = other.getW();

			q.mulInverse(other);

			assertEpsilonEquals(ox, other.getX());
			assertEpsilonEquals(oy, other.getY());
			assertEpsilonEquals(oz, other.getZ());
			assertEpsilonEquals(ow, other.getW());
		}

		@DisplayName("(Quaternion4d) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quat_7(CoordinateSystem3D cs) {
			// mulInverse in-place #6: norm preservation for unit inputs
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0.5, 0.5, 0.5, 0.5);
			var other = createQuaternion(0.5, -0.5, 0.5, -0.5);

			q.mulInverse(other);

			var n2 = q.getX() * q.getX()
					+ q.getY() * q.getY()
					+ q.getZ() * q.getZ()
					+ q.getW() * q.getW();
			assertEpsilonEquals(1., n2);
		}
	}

	@DisplayName("computeWithAxisAngle")
	@Nested
	public class ComputeWithAxisAngle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var components = Quaternion.computeWithAxisAngle(a, b, c, d);
			var mag0 = 1. / Math.sqrt(a * a + b * b + c * c);
			var mag1 = Math.sin(d / 2.);
			assertEpsilonEquals(a * mag0 * mag1, components.x());
			assertEpsilonEquals(b * mag0 * mag1, components.y());
			assertEpsilonEquals(c * mag0 * mag1, components.z());
			assertEpsilonEquals(Math.cos(d / 2.), components.w());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			// zero angle on X axis
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var c = Quaternion.computeWithAxisAngle(1., 0., 0., 0.);
			assertEpsilonEquals(0., c.x());
			assertEpsilonEquals(0., c.y());
			assertEpsilonEquals(0., c.z());
			assertEpsilonEquals(1., c.w());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			// #2 +PI around X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var c = Quaternion.computeWithAxisAngle(1., 0., 0., Math.PI);
			assertEpsilonEquals(1., c.x());
			assertEpsilonEquals(0., c.y());
			assertEpsilonEquals(0., c.z());
			assertEpsilonEquals(0., c.w());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			// #3 +PI around Y
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var c = Quaternion.computeWithAxisAngle(0., 1., 0., Math.PI);
			assertEpsilonEquals(0., c.x());
			assertEpsilonEquals(1., c.y());
			assertEpsilonEquals(0., c.z());
			assertEpsilonEquals(0., c.w());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			// #4 +PI around Z
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var c = Quaternion.computeWithAxisAngle(0., 0., 1., Math.PI);
			assertEpsilonEquals(0., c.x());
			assertEpsilonEquals(0., c.y());
			assertEpsilonEquals(1., c.z());
			assertEpsilonEquals(0., c.w());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			// #5 +PI/2 around X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var c = Quaternion.computeWithAxisAngle(1., 0., 0., Math.PI / 2.);
			assertEpsilonEquals(Math.sqrt(0.5), c.x());
			assertEpsilonEquals(0., c.y());
			assertEpsilonEquals(0., c.z());
			assertEpsilonEquals(Math.sqrt(0.5), c.w());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			// #6 axis scaling invariance
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			double angle = Math.PI / 3.;
			var c1 = Quaternion.computeWithAxisAngle(1., 0., 0., angle);
			var c2 = Quaternion.computeWithAxisAngle(2., 0., 0., angle); // same direction, different magnitude
			assertEpsilonEquals(c1.x(), c2.x());
			assertEpsilonEquals(c1.y(), c2.y());
			assertEpsilonEquals(c1.z(), c2.z());
			assertEpsilonEquals(c1.w(), c2.w());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			// #7 opposite axis/opposite angle equivalence
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			double angle = 1.234;
			var c1 = Quaternion.computeWithAxisAngle(1., 2., 3., angle);
			var c2 = Quaternion.computeWithAxisAngle(-1., -2., -3., -angle);
			assertEpsilonEquals(c1.x(), c2.x());
			assertEpsilonEquals(c1.y(), c2.y());
			assertEpsilonEquals(c1.z(), c2.z());
			assertEpsilonEquals(c1.w(), c2.w());
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_9(CoordinateSystem3D cs) {
			// #8 unit norm for typical axis/angle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var c = Quaternion.computeWithAxisAngle(1., 2., 3., 0.75);
			var n2 = c.x() * c.x() + c.y() * c.y() + c.z() * c.z() + c.w() * c.w();
			assertEpsilonEquals(1., n2);
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_10(CoordinateSystem3D cs) {
			// #9 angle periodicity
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			double angle = 0.8;
			var c1 = Quaternion.computeWithAxisAngle(0., 0., 1., angle);
			var c2 = Quaternion.computeWithAxisAngle(0., 0., 1., angle + 2. * Math.PI);
			// q and -q represent the same rotation
			boolean same =
					Math.abs(c1.x() - c2.x()) <= EPSILON
					&& Math.abs(c1.y() - c2.y()) <= EPSILON
					&& Math.abs(c1.z() - c2.z()) <= EPSILON
					&& Math.abs(c1.w() - c2.w()) <= EPSILON;
			boolean opposite =
					Math.abs(c1.x() + c2.x()) <= EPSILON
					&& Math.abs(c1.y() + c2.y()) <= EPSILON
					&& Math.abs(c1.z() + c2.z()) <= EPSILON
					&& Math.abs(c1.w() + c2.w()) <= EPSILON;
			assertTrue(same || opposite);
		}
	}

	@DisplayName("computeAxisAngle")
	@Nested
	public class ComputeAxisAngle {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var aa = Quaternion.computeAxisAngle(a, b, c, d);
			var mag = Math.sqrt(a * a + b * b + c * c);
			var imag = 1. / mag;
			assertEpsilonEquals(a * imag, aa.x());
			assertEpsilonEquals(b * imag, aa.y());
			assertEpsilonEquals(c * imag, aa.z());
			assertEpsilonEquals(2. * Math.atan2(mag, d), aa.angle());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			// #1 identity quaternion
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var aa = Quaternion.computeAxisAngle(0., 0., 0., 1.);
			assertEpsilonEquals(0., aa.angle());
			assertEpsilonEquals(0., aa.x());
			assertEpsilonEquals(0., aa.y());
			assertEpsilonEquals(1., aa.z()); // default axis convention for zero rotation
			assertNotNull(aa.axis());
			assertEpsilonEquals(aa.x(), aa.axis().getX());
			assertEpsilonEquals(aa.y(), aa.axis().getY());
			assertEpsilonEquals(aa.z(), aa.axis().getZ());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			// #2 +PI around X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var aa = Quaternion.computeAxisAngle(1., 0., 0., 0.);
			assertEpsilonEquals(Math.PI, aa.angle());
			assertEpsilonEquals(1., aa.x());
			assertEpsilonEquals(0., aa.y());
			assertEpsilonEquals(0., aa.z());
			assertNotNull(aa.axis());
			assertEpsilonEquals(1., aa.axis().getX());
			assertEpsilonEquals(0., aa.axis().getY());
			assertEpsilonEquals(0., aa.axis().getZ());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			// #3 +PI around Y
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var aa = Quaternion.computeAxisAngle(0., 1., 0., 0.);
			assertEpsilonEquals(Math.PI, aa.angle());
			assertEpsilonEquals(0., aa.x());
			assertEpsilonEquals(1., aa.y());
			assertEpsilonEquals(0., aa.z());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			// #4 +PI around Z
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var aa = Quaternion.computeAxisAngle(0., 0., 1., 0.);
			assertEpsilonEquals(Math.PI, aa.angle());
			assertEpsilonEquals(0., aa.x());
			assertEpsilonEquals(0., aa.y());
			assertEpsilonEquals(1., aa.z());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			// #5 +PI/2 around X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			double s = Math.sqrt(0.5); // sin(pi/4), cos(pi/4)
			var aa = Quaternion.computeAxisAngle(s, 0., 0., s);
			assertEpsilonEquals(Math.PI / 2., aa.angle());
			assertEpsilonEquals(1., aa.x());
			assertEpsilonEquals(0., aa.y());
			assertEpsilonEquals(0., aa.z());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			// #6 axis is unit for non-zero rotation
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var aa = Quaternion.computeAxisAngle(0.5, 0.5, 0.5, 0.5); // unit quaternion
			double n2 = aa.x() * aa.x() + aa.y() * aa.y() + aa.z() * aa.z();
			assertEpsilonEquals(1., n2);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			// #8 axis record consistency
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var aa = Quaternion.computeAxisAngle(0., Math.sqrt(0.5), 0., Math.sqrt(0.5)); // +PI/2 around Y
			assertNotNull(aa.axis());
			assertEpsilonEquals(aa.x(), aa.axis().getX());
			assertEpsilonEquals(aa.y(), aa.axis().getY());
			assertEpsilonEquals(aa.z(), aa.axis().getZ());
		}
	}

	@DisplayName("computeWithEulerAngles")
	@Nested
	public class computeWithEulerAngles {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			// zero angles -> identity
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = Quaternion.computeWithEulerAngles(0., 0., 0., cs);
			assertEpsilonEquals(0., q.x());
			assertEpsilonEquals(0., q.y());
			assertEpsilonEquals(0., q.z());
			assertEpsilonEquals(1., q.w());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			// attitude only, PI
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = Quaternion.computeWithEulerAngles(Math.PI, 0., 0., cs);
			// 180 degrees rotation => w=0 and unit vector part on one axis.
			assertEpsilonEquals(0., q.w());
			var vn2 = q.x() * q.x() + q.y() * q.y() + q.z() * q.z();
			assertEpsilonEquals(1., vn2);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			// bank only, PI
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = Quaternion.computeWithEulerAngles(0., Math.PI, 0., cs);
			assertEpsilonEquals(0., q.w());
			var vn2 = q.x() * q.x() + q.y() * q.y() + q.z() * q.z();
			assertEpsilonEquals(1., vn2);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			// heading only, PI
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = Quaternion.computeWithEulerAngles(0., 0., Math.PI, cs);
			assertEpsilonEquals(0., q.w());
			var vn2 = q.x() * q.x() + q.y() * q.y() + q.z() * q.z();
			assertEpsilonEquals(1., vn2);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			// norm is 1 for typical angles
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = Quaternion.computeWithEulerAngles(0.3, -0.7, 1.2, cs);
			var n2 = q.x() * q.x() + q.y() * q.y() + q.z() * q.z() + q.w() * q.w();
			assertEpsilonEquals(1., n2);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			// periodicity on attitude (+2PI)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			double a = 0.41;
			double b = -0.83;
			double h = 1.37;
			var q1 = Quaternion.computeWithEulerAngles(a, b, h, cs);
			var q2 = Quaternion.computeWithEulerAngles(a + 2. * Math.PI, b, h, cs);
			// Equivalent rotation: q or -q
			boolean same =
					Math.abs(q1.x() - q2.x()) <= EPSILON
					&& Math.abs(q1.y() - q2.y()) <= EPSILON
					&& Math.abs(q1.z() - q2.z()) <= EPSILON
					&& Math.abs(q1.w() - q2.w()) <= EPSILON;
			boolean opposite =
					Math.abs(q1.x() + q2.x()) <= EPSILON
					&& Math.abs(q1.y() + q2.y()) <= EPSILON
					&& Math.abs(q1.z() + q2.z()) <= EPSILON
					&& Math.abs(q1.w() + q2.w()) <= EPSILON;
			assertTrue(same || opposite);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			// periodicity on bank (+2PI)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			double a = -0.2;
			double b = 0.9;
			double h = -1.1;
			var q1 = Quaternion.computeWithEulerAngles(a, b, h, cs);
			var q2 = Quaternion.computeWithEulerAngles(a, b + 2. * Math.PI, h, cs);
			boolean same =
					Math.abs(q1.x() - q2.x()) <= EPSILON
					&& Math.abs(q1.y() - q2.y()) <= EPSILON
					&& Math.abs(q1.z() - q2.z()) <= EPSILON
					&& Math.abs(q1.w() - q2.w()) <= EPSILON;
			boolean opposite =
					Math.abs(q1.x() + q2.x()) <= EPSILON
					&& Math.abs(q1.y() + q2.y()) <= EPSILON
					&& Math.abs(q1.z() + q2.z()) <= EPSILON
					&& Math.abs(q1.w() + q2.w()) <= EPSILON;
			assertTrue(same || opposite);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			// periodicity on heading (+2PI)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			double a = 0.66;
			double b = -0.12;
			double h = 0.48;
			var q1 = Quaternion.computeWithEulerAngles(a, b, h, cs);
			var q2 = Quaternion.computeWithEulerAngles(a, b, h + 2. * Math.PI, cs);
			boolean same =
					Math.abs(q1.x() - q2.x()) <= EPSILON
					&& Math.abs(q1.y() - q2.y()) <= EPSILON
					&& Math.abs(q1.z() - q2.z()) <= EPSILON
					&& Math.abs(q1.w() - q2.w()) <= EPSILON;
			boolean opposite =
					Math.abs(q1.x() + q2.x()) <= EPSILON
					&& Math.abs(q1.y() + q2.y()) <= EPSILON
					&& Math.abs(q1.z() + q2.z()) <= EPSILON
					&& Math.abs(q1.w() + q2.w()) <= EPSILON;
			assertTrue(same || opposite);
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_9(CoordinateSystem3D cs) {
			// deterministic for same inputs
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			double a = 0.123;
			double b = -0.456;
			double h = 0.789;
			var q1 = Quaternion.computeWithEulerAngles(a, b, h, cs);
			var q2 = Quaternion.computeWithEulerAngles(a, b, h, cs);
			assertEpsilonEquals(q1.x(), q2.x());
			assertEpsilonEquals(q1.y(), q2.y());
			assertEpsilonEquals(q1.z(), q2.z());
			assertEpsilonEquals(q1.w(), q2.w());
		}
	}

	@DisplayName("computeEulerAngles")
	@Nested
	public class computeEulerAngles {
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			// identity -> zero angles
			assumeTrue(cs == CoordinateSystem3D.XZY_RIGHT_HAND);
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var e = Quaternion.computeEulerAngles(0., 0., 0., 1., cs);
			assertEpsilonEquals(createEulerAngles(0., 0., 0., cs), e);
		}
		
		private void doTest(double x, double y, double z, double w, CoordinateSystem3D cs,
				Quaternion.EulerAngles expectedXZYRight, Quaternion.EulerAngles expectedXZYLeft,
				Quaternion.EulerAngles expectedXYZRight, Quaternion.EulerAngles expectedXYZLeft) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var e = Quaternion.computeEulerAngles(x, y, z, w, cs);
			double ebank = 0.;
			double eheading = 0.;
			double eattitude = 0.;
			switch(cs) {
			case XZY_RIGHT_HAND:
				ebank = expectedXZYRight.bank();
				eheading = expectedXZYRight.heading();
				eattitude = expectedXZYRight.attitude();
				break;
			case XZY_LEFT_HAND:
				ebank = expectedXZYLeft.bank();
				eheading = expectedXZYLeft.heading();
				eattitude = expectedXZYLeft.attitude();
				break;
			case XYZ_RIGHT_HAND:
				ebank = expectedXYZRight.bank();
				eheading = expectedXYZRight.heading();
				eattitude = expectedXYZRight.attitude();
				break;
			case XYZ_LEFT_HAND:
				ebank = expectedXYZLeft.bank();
				eheading = expectedXYZLeft.heading();
				eattitude = expectedXYZLeft.attitude();
				break;
			default:
				throw new IllegalArgumentException();
			}
			assertEpsilonEquals(createEulerAngles(eheading, eattitude, ebank, cs), e);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			// precomputed +PI around Z
			// q: (0,0,1,0)
			// canonical Euler commonly used: (0,0,PI)
			doTest(0., 0., 1., 0., cs,
					// attitude, bank, heading
					new Quaternion.EulerAngles(0., Math.PI, Math.PI, cs),
					new Quaternion.EulerAngles(0., -Math.PI, -Math.PI, cs),
					new Quaternion.EulerAngles(0., 0., -Math.PI, cs),
					new Quaternion.EulerAngles(0., 0., Math.PI, cs));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			// precomputed #2 +PI/2 around X
			// q from axis-angle X, +PI/2: (sqrt(1/2),0,0,sqrt(1/2))
			// expected Euler (attitude,bank,heading) = (+PI/2,0,0)
			doTest(0.7071067811865476, 0.0, 0.0, 0.7071067811865476, cs,
					// attitude, bank, heading
					new Quaternion.EulerAngles(0., 1.5707963267948966, 0., cs),
					new Quaternion.EulerAngles(0., -1.5707963267948966, 0., cs),
					new Quaternion.EulerAngles(0., 1.5707963267948968, 0., cs),
					new Quaternion.EulerAngles(0., -1.5707963267948968, 0., cs));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			//precomputed #3 -PI/2 around X
			// q: (-sqrt(1/2),0,0,sqrt(1/2))
			// expected Euler = (-PI/2,0,0)
			doTest(-0.7071067811865476, 0.0, 0.0, 0.7071067811865476, cs,
					// attitude, bank, heading
					new Quaternion.EulerAngles(0., -1.5707963267948966, 0., cs),
					new Quaternion.EulerAngles(0., 1.5707963267948966, 0., cs),
					new Quaternion.EulerAngles(0., -1.5707963267948966, 0., cs),
					new Quaternion.EulerAngles(0., 1.5707963267948966, 0., cs));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			//precomputed #4 +PI/2 around Y
			// q: (0,sqrt(1/2),0,sqrt(1/2))
			// expected Euler = (0,+PI/2,0)
			doTest(0.0, 0.7071067811865476, 0.0, 0.7071067811865476, cs,
					// attitude, bank, heading
					new Quaternion.EulerAngles(0., 0., 1.5707963267948966, cs),
					new Quaternion.EulerAngles(0., 0., -1.5707963267948966, cs),
					new Quaternion.EulerAngles(1.5707963267948966, 0., 0., cs),
					new Quaternion.EulerAngles(-1.5707963267948966, 0., 0., cs));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			// precomputed #5 -PI/2 around Y
			// q: (0,-sqrt(1/2),0,sqrt(1/2))
			// expected Euler = (0,-PI/2,0)
			doTest(0.0, -0.7071067811865476, 0.0, 0.7071067811865476, cs,
					// attitude, bank, heading
					new Quaternion.EulerAngles(0., 0., -1.5707963267948966, cs),
					new Quaternion.EulerAngles(0., 0., 1.5707963267948966, cs),
					new Quaternion.EulerAngles(-1.5707963267948966, 0., 0., cs),
					new Quaternion.EulerAngles(1.5707963267948966, 0., 0., cs));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			// precomputed #6 +PI/2 around Z
			// q: (0,0,sqrt(1/2),sqrt(1/2))
			// expected Euler = (0,0,+PI/2)
			doTest(0.0, 0.0, 0.7071067811865476, 0.7071067811865476, cs,
					// attitude, bank, heading
					new Quaternion.EulerAngles(1.5707963267948966, 0., 0., cs),
					new Quaternion.EulerAngles(-1.5707963267948966, 0., 0., cs),
					new Quaternion.EulerAngles(0., 0., 1.5707963267948968, cs),
					new Quaternion.EulerAngles(0., 0., -1.5707963267948968, cs));
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			// precomputed #8 +PI around X
			// q: (1,0,0,0)
			// canonical Euler commonly used: (PI,0,0)
			doTest(1.0, 0.0, 0.0, 0.0, cs,
					// attitude, bank, heading
					new Quaternion.EulerAngles(0., 3.141592653589793, 0., cs),
					new Quaternion.EulerAngles(0., -3.141592653589793, 0., cs),
					new Quaternion.EulerAngles(0., -3.141592653589793, 0., cs),
					new Quaternion.EulerAngles(0., 3.141592653589793, 0., cs));
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_9(CoordinateSystem3D cs) {
			// precomputed #9 +PI around Y
			// q: (0,1,0,0)
			// canonical Euler commonly used: (0,PI,0)
			doTest(0.0, 1.0, 0.0, 0.0, cs,
					// attitude, bank, heading
					new Quaternion.EulerAngles(0., 0., 3.141592653589793, cs),
					new Quaternion.EulerAngles(0., 0., -3.141592653589793, cs),
					new Quaternion.EulerAngles(0., -3.141592653589793, -3.141592653589793, cs),
					new Quaternion.EulerAngles(0., 3.141592653589793, 3.141592653589793, cs));
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_10(CoordinateSystem3D cs) {
			// precomputed #7 -PI/2 around Z
			// q: (0,0,-sqrt(1/2),sqrt(1/2))
			// expected Euler = (0,0,-PI/2)
			doTest(0.0, 0.0, -0.7071067811865476, 0.7071067811865476, cs,
					// attitude, bank, heading
					new Quaternion.EulerAngles(-1.5707963267948966, 0., 0., cs),
					new Quaternion.EulerAngles(1.5707963267948966, 0., 0., cs),
					new Quaternion.EulerAngles(0., 0., -1.5707963267948968, cs),
					new Quaternion.EulerAngles(0., 0., 1.5707963267948968, cs));
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
			assertNotNull(getQ().getGeomFactory());
		}
	}

	@DisplayName("setAxisAngle")
	@Nested
	public class SetAxisAngle {

		@DisplayName("(double,double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void doubledoubledoubledouble_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(0., 0., 0., 0.);
			assertEpsilonEquals(createQuaternion(0., 0., 0., 0.), getQ());
		}

		@DisplayName("(double,double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void doubledoubledoubledouble_2(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(a, b, c, d);
			var mag0 = 1. / Math.sqrt(a * a + b * b + c * c);
			var mag1 = Math.sin(d / 2.);
			assertEpsilonEquals(a * mag0 * mag1, getQ().getX());
			assertEpsilonEquals(b * mag0 * mag1, getQ().getY());
			assertEpsilonEquals(c * mag0 * mag1, getQ().getZ());
			assertEpsilonEquals(Math.cos(d / 2.), getQ().getW());
		}

		@DisplayName("(double,double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledoubledouble_3(CoordinateSystem3D cs) {
			// zero angle on X axis
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(1., 0., 0., 0.);
			assertEpsilonEquals(0., getQ().getX());
			assertEpsilonEquals(0., getQ().getY());
			assertEpsilonEquals(0., getQ().getZ());
			assertEpsilonEquals(1., getQ().getW());
		}

		@DisplayName("(double,double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledoubledouble_4(CoordinateSystem3D cs) {
			// #2 +PI around X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(1., 0., 0., Math.PI);
			assertEpsilonEquals(1., getQ().getX());
			assertEpsilonEquals(0., getQ().getY());
			assertEpsilonEquals(0., getQ().getZ());
			assertEpsilonEquals(0., getQ().getW());
		}

		@DisplayName("(double,double,double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledoubledouble_5(CoordinateSystem3D cs) {
			// #3 +PI around Y
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(0., 1., 0., Math.PI);
			assertEpsilonEquals(0., getQ().getX());
			assertEpsilonEquals(1., getQ().getY());
			assertEpsilonEquals(0., getQ().getZ());
			assertEpsilonEquals(0., getQ().getW());
		}

		@DisplayName("(double,double,double,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledoubledouble_6(CoordinateSystem3D cs) {
			// #4 +PI around Z
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(0., 0., 1., Math.PI);
			assertEpsilonEquals(0., getQ().getX());
			assertEpsilonEquals(0., getQ().getY());
			assertEpsilonEquals(1., getQ().getZ());
			assertEpsilonEquals(0., getQ().getW());
		}

		@DisplayName("(double,double,double,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledoubledouble_7(CoordinateSystem3D cs) {
			// #5 +PI/2 around X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(1., 0., 0., Math.PI / 2.);
			assertEpsilonEquals(Math.sqrt(0.5), getQ().getX());
			assertEpsilonEquals(0., getQ().getY());
			assertEpsilonEquals(0., getQ().getZ());
			assertEpsilonEquals(Math.sqrt(0.5), getQ().getW());
		}

		@DisplayName("(double,double,double,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledoubledouble_8(CoordinateSystem3D cs) {
			// #8 unit norm for typical axis/angle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(1., 2., 3., 0.75);
			var n2 = getQ().getX() * getQ().getX() + getQ().getY() * getQ().getY() + getQ().getZ() * getQ().getZ() + getQ().getW() * getQ().getW();
			assertEpsilonEquals(1., n2);
		}

		@DisplayName("(Vector3D,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void vectordouble_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createVector(0., 0., 0.), 0.);
			assertEpsilonEquals(createQuaternion(0., 0., 0., 0.), getQ());
		}

		@DisplayName("(Vector3D,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void vectordouble_2(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createVector(a, b, c), d);
			var mag0 = 1. / Math.sqrt(a * a + b * b + c * c);
			var mag1 = Math.sin(d / 2.);
			assertEpsilonEquals(a * mag0 * mag1, getQ().getX());
			assertEpsilonEquals(b * mag0 * mag1, getQ().getY());
			assertEpsilonEquals(c * mag0 * mag1, getQ().getZ());
			assertEpsilonEquals(Math.cos(d / 2.), getQ().getW());
		}

		@DisplayName("(Vector3D,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void vectordouble_3(CoordinateSystem3D cs) {
			// zero angle on X axis
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createVector(1., 0., 0.), 0.);
			assertEpsilonEquals(0., getQ().getX());
			assertEpsilonEquals(0., getQ().getY());
			assertEpsilonEquals(0., getQ().getZ());
			assertEpsilonEquals(1., getQ().getW());
		}

		@DisplayName("(Vector3D,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void vectordouble_4(CoordinateSystem3D cs) {
			// #2 +PI around X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createVector(1., 0., 0.), Math.PI);
			assertEpsilonEquals(1., getQ().getX());
			assertEpsilonEquals(0., getQ().getY());
			assertEpsilonEquals(0., getQ().getZ());
			assertEpsilonEquals(0., getQ().getW());
		}

		@DisplayName("(Vector3D,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void vectordouble_5(CoordinateSystem3D cs) {
			// #3 +PI around Y
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createVector(0., 1., 0.), Math.PI);
			assertEpsilonEquals(0., getQ().getX());
			assertEpsilonEquals(1., getQ().getY());
			assertEpsilonEquals(0., getQ().getZ());
			assertEpsilonEquals(0., getQ().getW());
		}

		@DisplayName("(Vector3D,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void vectordouble_6(CoordinateSystem3D cs) {
			// #4 +PI around Z
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createVector(0., 0., 1.), Math.PI);
			assertEpsilonEquals(0., getQ().getX());
			assertEpsilonEquals(0., getQ().getY());
			assertEpsilonEquals(1., getQ().getZ());
			assertEpsilonEquals(0., getQ().getW());
		}

		@DisplayName("(Vector3D,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void vectordouble_7(CoordinateSystem3D cs) {
			// #5 +PI/2 around X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createVector(1., 0., 0.), Math.PI / 2.);
			assertEpsilonEquals(Math.sqrt(0.5), getQ().getX());
			assertEpsilonEquals(0., getQ().getY());
			assertEpsilonEquals(0., getQ().getZ());
			assertEpsilonEquals(Math.sqrt(0.5), getQ().getW());
		}

		@DisplayName("(Vector3D,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void vectordouble_8(CoordinateSystem3D cs) {
			// #8 unit norm for typical axis/angle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createVector(1., 2., 3.), 0.75);
			var n2 = getQ().getX() * getQ().getX() + getQ().getY() * getQ().getY() + getQ().getZ() * getQ().getZ() + getQ().getW() * getQ().getW();
			assertEpsilonEquals(1., n2);
		}

		@DisplayName("(AxisAngle) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void axisangle_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createAxisAngle(0., 0., 0., 0.));
			assertEpsilonEquals(createQuaternion(0., 0., 0., 0.), getQ());
		}

		@DisplayName("(AxisAngle) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void axisangle_2(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createAxisAngle(a, b, c, d));
			var mag0 = 1. / Math.sqrt(a * a + b * b + c * c);
			var mag1 = Math.sin(d / 2.);
			assertEpsilonEquals(a * mag0 * mag1, getQ().getX());
			assertEpsilonEquals(b * mag0 * mag1, getQ().getY());
			assertEpsilonEquals(c * mag0 * mag1, getQ().getZ());
			assertEpsilonEquals(Math.cos(d / 2.), getQ().getW());
		}

		@DisplayName("(AxisAngle) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void axisangle_3(CoordinateSystem3D cs) {
			// zero angle on X axis
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createAxisAngle(1., 0., 0., 0.));
			assertEpsilonEquals(0., getQ().getX());
			assertEpsilonEquals(0., getQ().getY());
			assertEpsilonEquals(0., getQ().getZ());
			assertEpsilonEquals(1., getQ().getW());
		}

		@DisplayName("(AxisAngle) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void axisangle_4(CoordinateSystem3D cs) {
			// #2 +PI around X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createAxisAngle(1., 0., 0., Math.PI));
			assertEpsilonEquals(1., getQ().getX());
			assertEpsilonEquals(0., getQ().getY());
			assertEpsilonEquals(0., getQ().getZ());
			assertEpsilonEquals(0., getQ().getW());
		}

		@DisplayName("(AxisAngle) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void axisangle_5(CoordinateSystem3D cs) {
			// #3 +PI around Y
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createAxisAngle(0., 1., 0., Math.PI));
			assertEpsilonEquals(0., getQ().getX());
			assertEpsilonEquals(1., getQ().getY());
			assertEpsilonEquals(0., getQ().getZ());
			assertEpsilonEquals(0., getQ().getW());
		}

		@DisplayName("(AxisAngle) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void axisangle_6(CoordinateSystem3D cs) {
			// #4 +PI around Z
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createAxisAngle(0., 0., 1., Math.PI));
			assertEpsilonEquals(0., getQ().getX());
			assertEpsilonEquals(0., getQ().getY());
			assertEpsilonEquals(1., getQ().getZ());
			assertEpsilonEquals(0., getQ().getW());
		}

		@DisplayName("(AxisAngle) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void axisangle_7(CoordinateSystem3D cs) {
			// #5 +PI/2 around X
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createAxisAngle(1., 0., 0., Math.PI / 2.));
			assertEpsilonEquals(Math.sqrt(0.5), getQ().getX());
			assertEpsilonEquals(0., getQ().getY());
			assertEpsilonEquals(0., getQ().getZ());
			assertEpsilonEquals(Math.sqrt(0.5), getQ().getW());
		}

		@DisplayName("(AxisAngle) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void axisangle_8(CoordinateSystem3D cs) {
			// #8 unit norm for typical axis/angle
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setAxisAngle(createAxisAngle(1., 2., 3., 0.75));
			var n2 = getQ().getX() * getQ().getX() + getQ().getY() * getQ().getY() + getQ().getZ() * getQ().getZ() + getQ().getW() * getQ().getW();
			assertEpsilonEquals(1., n2);
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
			assertEpsilonEquals(createVector(0.447213595499958, -0.894427190999916, 0.), getQ().getAxis());
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
			assertEpsilonEquals(1.281044625358849, getQ().getAngle());
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
			var aa = getQ().getAxisAngle();
			assertEpsilonEquals(createVector(0.447213595499958, -0.894427190999916, 0.), aa.axis());
			assertEpsilonEquals(1.281044625358849, aa.angle());
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
			getQ().interpolate(createQuaternion(0., 0., 0., 0.), 0.);
			assertEpsilonEquals(createQuaternion(0., 0., 0., 0.), getQ());
		}

		@DisplayName("(Quaternion,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaterniondouble_2(CoordinateSystem3D cs) {
			// alpha=0 keeps current quaternion
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().interpolate(createQuaternion(0.2, 0.1, -0.3, 0.9), 0.);
			assertEpsilonEquals(createQuaternion(0.2672612419, -0.5345224838, 0, 0.8017837257), getQ());
		}

		@DisplayName("(Quaternion,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaterniondouble_3(CoordinateSystem3D cs) {
			// alpha=1 reaches target (normalized)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var target = createQuaternion(2., -1., 4., 0.5);
			getQ().interpolate(target, 1.);
			assertEpsilonEquals(target, getQ());
		}

		@DisplayName("(Quaternion,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaterniondouble_4(CoordinateSystem3D cs) {
			// interpolation with itself is invariant
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var self = createQuaternion(getQ().getX(), getQ().getY(), getQ().getZ(), getQ().getW());
			getQ().interpolate(self, 0.37);

			assertEpsilonEquals(self, getQ());
		}

		@DisplayName("(Quaternion,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaterniondouble_5(CoordinateSystem3D cs) {
			// interpolation toward antipode keeps same rotation
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var self = createQuaternion(getQ().getX(), getQ().getY(), getQ().getZ(), getQ().getW());
			final var antipode = createQuaternion(-self.getX(), -self.getY(), -self.getZ(), -self.getW());
			getQ().interpolate(antipode, 0.5);
			// q and -q represent same rotation: result should be equivalent to self
			assertTrue(getQ().epsilonEquals(self, 1e-12) || getQ().epsilonEquals(antipode, 1e-12));
		}

		@DisplayName("(Quaternion,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaterniondouble_6(CoordinateSystem3D cs) {
			// result remains normalized for mid alpha
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var target = createQuaternion(-0.5, 0.7, 0.1, 2.0);
			getQ().interpolate(target, 0.5);
			final double n = Math.sqrt(
					getQ().getX() * getQ().getX()
					+ getQ().getY() * getQ().getY()
					+ getQ().getZ() * getQ().getZ()
					+ getQ().getW() * getQ().getW());
			assertEpsilonEquals(1., n);
		}

		@DisplayName("(Quaternion,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaterniondouble_7(CoordinateSystem3D cs) {
			// near-identical quaternions (nlerp branch) stays stable
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var target = createQuaternion(
					getQ().getX() + 1e-13,
					getQ().getY() - 1e-13,
					getQ().getZ() + 2e-13,
					getQ().getW() - 1e-13);
			getQ().interpolate(target, 0.25);
			final double n = Math.sqrt(
					getQ().getX() * getQ().getX()
					+ getQ().getY() * getQ().getY()
					+ getQ().getZ() * getQ().getZ()
					+ getQ().getW() * getQ().getW());
			assertEpsilonEquals(1., n);
		}

		@DisplayName("(Quaternion,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaterniondouble_8(CoordinateSystem3D cs) {
			// alpha<0 extrapolation still normalized
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var target = createQuaternion(0.3, -0.4, 0.5, 0.6);
			getQ().interpolate(target, -0.5);
			final double n = Math.sqrt(
					getQ().getX() * getQ().getX()
					+ getQ().getY() * getQ().getY()
					+ getQ().getZ() * getQ().getZ()
					+ getQ().getW() * getQ().getW());
			assertEpsilonEquals(1., n);
		}

		@DisplayName("(Quaternion,double) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaterniondouble_9(CoordinateSystem3D cs) {
			// alpha>1 extrapolation still normalized
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var target = createQuaternion(-0.8, 0.2, 0.1, 1.5);
			getQ().interpolate(target, 1.75);
			final double n = Math.sqrt(
					getQ().getX() * getQ().getX()
					+ getQ().getY() * getQ().getY()
					+ getQ().getZ() * getQ().getZ()
					+ getQ().getW() * getQ().getW());
			assertEpsilonEquals(1., n);
		}

		@DisplayName("(Quaternion,double) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaterniondouble_10(CoordinateSystem3D cs) {
			// halfway to identity gives expected slerp value
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var identity = createQuaternion(0., 0., 0., 1.);
			// Compute expected with same formula for strong regression lock
			final double x1 = getQ().getX(), y1 = getQ().getY(), z1 = getQ().getZ(), w1 = getQ().getW();
			double x2 = identity.getX(), y2 = identity.getY(), z2 = identity.getZ(), w2 = identity.getW();
			double dot = x1 * x2 + y1 * y2 + z1 * z2 + w1 * w2;
			if (dot < 0.) { 
				dot = -dot;
				x2 = -x2;
				y2 = -y2;
				z2 = -z2;
				w2 = -w2;
			}
			double s0, s1;
			if (1. - dot < 1e-8) {
				s0 = 0.5;
				s1 = 0.5;
			} else {
				final double omega = Math.acos(dot);
				final double sinOmega = Math.sin(omega);
				s0 = Math.sin(0.5 * omega) / sinOmega;
				s1 = Math.sin(0.5 * omega) / sinOmega;
			}
			final var expected = createQuaternion(s0 * x1 + s1 * x2, s0 * y1 + s1 * y2, s0 * z1 + s1 * z2, s0 * w1 + s1 * w2);
			expected.normalize();

			getQ().interpolate(identity, 0.5);

			assertEpsilonEquals(expected, getQ());
		}

		@DisplayName("(Quaternion,double) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaterniondouble_11(CoordinateSystem3D cs) {
			// chaining interpolation equals direct for same target/alpha composition
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);

			final var target = createQuaternion(0.9, -0.1, 0.3, 0.2);

			final var qA = createQuaternion(1., -2., 0., 3.);
			final var qB = createQuaternion(1., -2., 0., 3.);

			// Two-step: alpha a then b toward same target
			final double a = 0.4, b = 0.5;
			qA.interpolate(target, a);
			qA.interpolate(target, b);

			// Equivalent effective alpha: a + (1-a)*b
			final double effective = a + (1. - a) * b;
			qB.interpolate(target, effective);

			assertEpsilonEquals(qB, qA);
		}

		@DisplayName("(Quaternion,Quaternion,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaternionquaterniondouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().interpolate(createQuaternion(0., 0., 0., 0.), createQuaternion(1., 2., 3., 3.), 0.);
			assertEpsilonEquals(createQuaternion(0., 0., 0., 0.), getQ());
		}

		@DisplayName("(Quaternion,Quaternion,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaternionquaterniondouble_2(CoordinateSystem3D cs) {
			// alpha=0 returns q1
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var q1 = createQuaternion(1., -2., 0., 3.);
			final var q2 = createQuaternion(0.3, -0.4, 0.5, 0.6);
			getQ().interpolate(q1, q2, 0.);
			assertEpsilonEquals(q1, getQ());
		}

		@DisplayName("(Quaternion,Quaternion,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaternionquaterniondouble_3(CoordinateSystem3D cs) {
			// alpha=1 returns q2
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var q1 = createQuaternion(1., -2., 0., 3.);
			final var q2 = createQuaternion(-0.2, 0.7, 0.1, 1.2);
			getQ().interpolate(q1, q2, 1.);
			assertEpsilonEquals(q2, getQ());
		}

		@DisplayName("(Quaternion,Quaternion,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaternionquaterniondouble_4(CoordinateSystem3D cs) {
			// q1=q2 invariant for any alpha
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var q1 = createQuaternion(1., -2., 0., 3.);
			getQ().interpolate(q1, q1, 0.37);
			assertEpsilonEquals(q1, getQ());
		}

		@DisplayName("(Quaternion,Quaternion,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaternionquaterniondouble_5(CoordinateSystem3D cs) {
			// q2=-q1 (same rotation), shortest path handling
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var q1 = createQuaternion(1., -2., 0., 3.);
			final var q2 = createQuaternion(-q1.getX(), -q1.getY(), -q1.getZ(), -q1.getW());
			getQ().interpolate(q1, q2, 0.5);
			assertTrue(getQ().epsilonEquals(q1, 1e-12) || getQ().epsilonEquals(q2, 1e-12));
		}

		@DisplayName("(Quaternion,Quaternion,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaternionquaterniondouble_6(CoordinateSystem3D cs) {
			// alpha=0.5 result is normalized
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var q1 = createQuaternion(1., -2., 0., 3.);
			final var q2 = createQuaternion(2.0, 1.0, -1.0, 0.5);
			getQ().interpolate(q1, q2, 0.5);
			final double n = Math.sqrt(
					getQ().getX() * getQ().getX()
					+ getQ().getY() * getQ().getY()
					+ getQ().getZ() * getQ().getZ()
					+ getQ().getW() * getQ().getW());
			assertEpsilonEquals(1., n);
		}

		@DisplayName("(Quaternion,Quaternion,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaternionquaterniondouble_7(CoordinateSystem3D cs) {
			// near-identical inputs (nlerp branch robustness
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var q1 = createQuaternion(1., -2., 0., 3.);
			final var q2 = createQuaternion(
					q1.getX() + 1e-13,
					q1.getY() - 1e-13,
					q1.getZ() + 2e-13,
					q1.getW() - 1e-13);
			getQ().interpolate(q1, q2, 0.25);
			final double n = Math.sqrt(
					getQ().getX() * getQ().getX()
					+ getQ().getY() * getQ().getY()
					+ getQ().getZ() * getQ().getZ()
					+ getQ().getW() * getQ().getW());
			assertEpsilonEquals(1., n);
		}

		@DisplayName("(Quaternion,Quaternion,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaternionquaterniondouble_8(CoordinateSystem3D cs) {
			// alpha<0 extrapolation remains normalized
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var q1 = createQuaternion(1., -2., 0., 3.);
			final var q2 = createQuaternion(0.6, -0.1, 0.2, 0.7);
			getQ().interpolate(q1, q2, -0.5);
			final double n = Math.sqrt(
					getQ().getX() * getQ().getX()
					+ getQ().getY() * getQ().getY()
					+ getQ().getZ() * getQ().getZ()
					+ getQ().getW() * getQ().getW());
			assertEpsilonEquals(1., n);
		}

		@DisplayName("(Quaternion,Quaternion,double) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaternionquaterniondouble_9(CoordinateSystem3D cs) {
			// alpha>1 extrapolation remains normalized
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var q1 = createQuaternion(1., -2., 0., 3.);
			final var q2 = createQuaternion(-0.9, 0.3, 0.1, 0.4);
			getQ().interpolate(q1, q2, 1.75);
			final double n = Math.sqrt(
					getQ().getX() * getQ().getX()
					+ getQ().getY() * getQ().getY()
					+ getQ().getZ() * getQ().getZ()
					+ getQ().getW() * getQ().getW());
			assertEpsilonEquals(1., n);
		}

		@DisplayName("(Quaternion,Quaternion,double) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaternionquaterniondouble_10(CoordinateSystem3D cs) {
			// symmetry: slerp(q1,q2,a) == slerp(q2,q1,1-a)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var q1 = createQuaternion(1., -2., 0., 3.);
			final var q2 = createQuaternion(0.1, 0.9, -0.2, 0.3);
			final double a = 0.3;
			final var left = createQuaternion();
			left.interpolate(q1, q2, a);
			final var right = createQuaternion();
			right.interpolate(q2, q1, 1. - a);
			assertTrue(left.epsilonEquals(right, 1e-12)
					|| left.epsilonEquals(createQuaternion(-right.getX(), -right.getY(), -right.getZ(), -right.getW()), 1e-12));
		}

		@DisplayName("(Quaternion,Quaternion,double) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void quaternionquaterniondouble_11(CoordinateSystem3D cs) {
			// endpoint continuity near alpha=0 and alpha=1
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var q1 = createQuaternion(1., -2., 0., 3.);
			final var q2 = createQuaternion(-0.4, 0.5, 0.2, 0.7);
			final var near0 = createQuaternion();
			near0.interpolate(q1, q2, 1e-12);
			final var near1 = createQuaternion();
			near1.interpolate(q1, q2, 1. - 1e-12);
			assertTrue(near0.epsilonEquals(q1, 1e-9) || near0.epsilonEquals(createQuaternion(-q1.getX(), -q1.getY(), -q1.getZ(), -q1.getW()), 1e-9));
			assertTrue(near1.epsilonEquals(q2, 1e-9) || near1.epsilonEquals(createQuaternion(-q2.getX(), -q2.getY(), -q2.getZ(), -q2.getW()), 1e-9));
		}
	}

	@DisplayName("setEulerAngles")
	@Nested
	public class SetEulerAngles {

		private void assertEpsEquals(CoordinateSystem3D cs, boolean rightIsPositive, Quaternion expected, Quaternion actual) {
			Quaternion expected0;
			switch (cs) {
			case XYZ_LEFT_HAND:
			case XZY_RIGHT_HAND:
				if (rightIsPositive) {
					expected0 = expected;
				} else {
					expected0 = createQuaternion();
					expected0.inverse(expected);
				}
				break;
			case XYZ_RIGHT_HAND:
			case XZY_LEFT_HAND:
				if (rightIsPositive) {
					expected0 = createQuaternion();
					expected0.inverse(expected);
				} else {
					expected0 = expected;
				}
				break;
			default:
				fail("Invalid coordinate system in the test code");
				return;
			}
			assertEpsilonEquals(expected0, actual);
		}

		@DisplayName("(double,double,double,CoordinateSystem3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledoublecoordinatesystem_1(CoordinateSystem3D cs) {
			// zero angles -> identity
			getQ().setEulerAngles(0., 0., 0., cs);
			assertEpsilonEquals(createQuaternion(0., 0., 0., 1.), getQ());
		}
		
		@DisplayName("(double,double,double,CoordinateSystem3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledoublecoordinatesystem_2(CoordinateSystem3D cs) {
			// heading only
			final double heading = 0.73; // around top
			getQ().setEulerAngles(0., 0., heading, cs);
			final var expected = createQuaternionFromAxisAngle(cs.getUpVector(), heading);
			assertEpsEquals(cs, true, expected, getQ());
		}

		@DisplayName("(double,double,double,CoordinateSystem3D) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledoublecoordinatesystem_3(CoordinateSystem3D cs) {
			// attitude only
			final double attitude = -0.41; // around left
			getQ().setEulerAngles(attitude, 0., 0., cs);
			final var expected = createQuaternionFromAxisAngle(cs.getLeftVector(), attitude);
			assertEpsEquals(cs, false, expected, getQ());
		}

		@DisplayName("(double,double,double,CoordinateSystem3D) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledoublecoordinatesystem_4(CoordinateSystem3D cs) {
			// bank only
			final double bank = 1.12; // around front
			getQ().setEulerAngles(0., bank, 0., cs);
			final var expected = createQuaternionFromAxisAngle(createVector(1, 0, 0), bank);
			assertEpsEquals(cs, true, expected, getQ());
		}

		@DisplayName("(double,double,double,CoordinateSystem3D) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledoublecoordinatesystem_5(CoordinateSystem3D cs) {
			// result is normalized
			getQ().setEulerAngles(0.4, -1.1, 2.2, cs);
			final double n = Math.sqrt(
					getQ().getX() * getQ().getX()
					+ getQ().getY() * getQ().getY()
					+ getQ().getZ() * getQ().getZ()
					+ getQ().getW() * getQ().getW());
			assertEpsilonEquals(1., n);
		}
		
		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledouble_1(CoordinateSystem3D cs) {
			// zero angles -> identity
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setEulerAngles(0., 0., 0.);
			assertEpsilonEquals(createQuaternion(0., 0., 0., 1.), getQ());
		}
		
		@DisplayName("(double,double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledouble_2(CoordinateSystem3D cs) {
			// heading only
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final double heading = 0.73; // around top
			getQ().setEulerAngles(0., 0., heading);
			final var expected = createQuaternionFromAxisAngle(cs.getUpVector(), heading);
			assertEpsEquals(cs, true, expected, getQ());
		}

		@DisplayName("(double,double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledouble_3(CoordinateSystem3D cs) {
			// attitude only
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final double attitude = -0.41; // around left
			getQ().setEulerAngles(attitude, 0., 0.);
			final var expected = createQuaternionFromAxisAngle(cs.getLeftVector(), attitude);
			assertEpsEquals(cs, false, expected, getQ());
		}

		@DisplayName("(double,double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledouble_4(CoordinateSystem3D cs) {
			// bank only
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final double bank = 1.12; // around front
			getQ().setEulerAngles(0., bank, 0.);
			final var expected = createQuaternionFromAxisAngle(createVector(1, 0, 0), bank);
			assertEpsEquals(cs, true, expected, getQ());
		}

		@DisplayName("(double,double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledouble_5(CoordinateSystem3D cs) {
			// result is normalized
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setEulerAngles(0.4, -1.1, 2.2);
			final double n = Math.sqrt(
					getQ().getX() * getQ().getX()
					+ getQ().getY() * getQ().getY()
					+ getQ().getZ() * getQ().getZ()
					+ getQ().getW() * getQ().getW());
			assertEpsilonEquals(1., n);
		}

		@DisplayName("(EulerAngles) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void eulerangles_1(CoordinateSystem3D cs) {
			// zero angles -> identity
			getQ().setEulerAngles(createEulerAngles(0., 0., 0., cs));
			assertEpsilonEquals(createQuaternion(0., 0., 0., 1.), getQ());
		}
		
		@DisplayName("(EulerAngles) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void eulerangles_2(CoordinateSystem3D cs) {
			// heading only
			final double heading = 0.73; // around top
			getQ().setEulerAngles(createEulerAngles(heading, 0., 0., cs));
			final var expected = createQuaternionFromAxisAngle(cs.getUpVector(), heading);
			assertEpsEquals(cs, true, expected, getQ());
		}

		@DisplayName("(EulerAngles) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void eulerangles_3(CoordinateSystem3D cs) {
			// attitude only
			final double attitude = -0.41; // around left
			getQ().setEulerAngles(createEulerAngles(0., attitude, 0., cs));
			final var expected = createQuaternionFromAxisAngle(cs.getLeftVector(), attitude);
			assertEpsEquals(cs, false, expected, getQ());
		}

		@DisplayName("(EulerAngles) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void eulerangles_4(CoordinateSystem3D cs) {
			// bank only
			final double bank = 1.12; // around front
			getQ().setEulerAngles(createEulerAngles(0., 0., bank, cs));
			final var expected = createQuaternionFromAxisAngle(createVector(1, 0, 0), bank);
			assertEpsEquals(cs, true, expected, getQ());
		}

		@DisplayName("(EulerAngles) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void eulerangles_5(CoordinateSystem3D cs) {
			// result is normalized
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getQ().setEulerAngles(createEulerAngles(2.2, 0.4, -1.1, cs));
			final double n = Math.sqrt(
					getQ().getX() * getQ().getX()
					+ getQ().getY() * getQ().getY()
					+ getQ().getZ() * getQ().getZ()
					+ getQ().getW() * getQ().getW());
			assertEpsilonEquals(1., n);
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
			double heading, attitude, bank;
			switch (cs) {
			case XYZ_LEFT_HAND:
				heading = -0.5880026035475677;
				attitude = 1.029696800837751;
				bank = -0.9827937232473293;
				break;
			case XYZ_RIGHT_HAND:
				heading = 0.5880026035475677;
				attitude = -1.029696800837751;
				bank = 0.9827937232473293;
				break;
			case XZY_LEFT_HAND:
				heading = 1.1071487177940906;
				attitude = -0.2897517014360475;
				bank = -0.46364760900080615;
				break;
			case XZY_RIGHT_HAND:
				heading = -1.1071487177940906;
				attitude = 0.2897517014360475;
				bank = 0.46364760900080615;
				break;
			default:
				throw new UnsupportedOperationException();
			}
			var expected = createEulerAngles(heading, attitude, bank, cs);
			var actual = getQ().getEulerAngles();
			assertEpsilonEquals(expected, actual);
		}

		@DisplayName("(CoordinateSystem) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void cs_1(CoordinateSystem3D cs) {
			double heading, attitude, bank;
			switch (cs) {
			case XYZ_LEFT_HAND:
				heading = -0.5880026035475677;
				attitude = 1.029696800837751;
				bank = -0.9827937232473293;
				break;
			case XYZ_RIGHT_HAND:
				heading = 0.5880026035475677;
				attitude = -1.029696800837751;
				bank = 0.9827937232473293;
				break;
			case XZY_LEFT_HAND:
				heading = 1.1071487177940906;
				attitude = -0.2897517014360475;
				bank = -0.46364760900080615;
				break;
			case XZY_RIGHT_HAND:
				heading = -1.1071487177940906;
				attitude = 0.2897517014360475;
				bank = 0.46364760900080615;
				break;
			default:
				throw new UnsupportedOperationException();
			}
			var expected = createEulerAngles(heading, attitude, bank, cs);
			var actual = getQ().getEulerAngles(cs);
			assertEpsilonEquals(expected, actual);
		}
	}

	@DisplayName("toUnmodifiable")
	@Nested
	public class ToUnmodifiable {

		private TQ origin;
		
		@BeforeEach
		public void setUp() {
			this.origin = createQuaternion(1, 0, 0, 0);
			assumeMutable(origin);
		}

		@DisplayName("With exception")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void toUnmodifiable_exception(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				var immutable = origin.toUnmodifiable();
				assertEpsilonEquals(origin, immutable);
				immutable.conjugate();
			});
		}
	
		@DisplayName("Change in origin")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void toUnmodifiable_changeInOrigin(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var immutable = origin.toUnmodifiable();
			assertNotSame(origin, immutable);
			assertEpsilonEquals(origin, immutable);
			origin.conjugate();
			assertEpsilonEquals(createQuaternion(1, 0, 0, 0), immutable);
		}
	}

	@DisplayName("this * Quaternion")
	@Nested
	public class OperatorMultiplyQuaternion {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments3")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d, Double x, Double y, Double z, Double w) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(a,b,c,d);
			var q2 = createQuaternion(x,y,z,w);
			var mul = createQuaternion();
			mul.setW(q1.getW()*q2.getW()-q1.getX()*q2.getX()-q1.getY()*q2.getY()-q1.getZ()*q2.getZ());
			mul.setX(q1.getW()*q2.getX()+q1.getX()*q2.getW()+q1.getY()*q2.getZ()-q1.getZ()*q2.getY());
			mul.setY(q1.getW()*q2.getY()+q1.getY()*q2.getW()-q1.getX()*q2.getZ()+q1.getZ()*q2.getX());
			mul.setZ(q1.getZ()*q2.getW()+q1.getW()*q2.getZ()+q1.getX()*q2.getY()-q1.getY()*q2.getX());
			var result = q1.operator_multiply(q2);
			assertEpsilonEquals(mul, result);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q1 = createQuaternion(1,0,0,0);
			var q2 = createQuaternion(0,1,0,0);
			var result = q1.operator_multiply(q2);
			assertEpsilonEquals(createQuaternion(0., 0., 1., 0.), result);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			// in-place identity-right
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0.5, -0.5, 0.5, -0.5); // unit
			var identity = createQuaternion(0, 0, 0, 1);
			var result = q.operator_multiply(identity); // q = q * 1 = q
			assertEpsilonEquals(createQuaternion(0.5, -0.5, 0.5, -0.5), result);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			// in-place identity-left-equivalent-via-target
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0, 0, 0, 1); // identity as target (this)
			var r = createQuaternion(-0.5, 0.5, -0.5, 0.5); // unit
			var result = q.operator_multiply(r); // q = 1 * r = r
			assertEpsilonEquals(r, result);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			// in-place self-times-self-i
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(1, 0, 0, 0); // i
			var result = q.operator_multiply(createQuaternion(1, 0, 0, 0));  // i*i = -1
			assertEpsilonEquals(createQuaternion(0., 0., 0., -1.), result);
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_6(CoordinateSystem3D cs) {
			// in-place non-commutative
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);

			var q1 = createQuaternion(1, 0, 0, 0); // i
			var result1 = q1.operator_multiply(createQuaternion(0, 1, 0, 0));  // i*j = k

			var q2 = createQuaternion(0, 1, 0, 0); // j
			var result2 = q2.operator_multiply(createQuaternion(1, 0, 0, 0));  // j*i = -k

			assertEpsilonEquals(createQuaternion(0., 0., 1., 0.), result1);

			assertEpsilonEquals(createQuaternion(0., 0., -1., 0.), result2);
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_7(CoordinateSystem3D cs) {
			// in-place conjugate-product-gives-identity
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0.5, 0.5, 0.5, 0.5);     // unit
			var qc = createQuaternion(-0.5, -0.5, -0.5, 0.5); // conjugate
			var result = q.operator_multiply(qc); // q = q * qc = identity
			assertEpsilonEquals(createQuaternion(0., 0., 0., 1.), result);
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_8(CoordinateSystem3D cs) {
			// in-place norm-preservation-for-unit-inputs
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0.5, 0.5, 0.5, 0.5);
			var r = createQuaternion(0.5, -0.5, 0.5, -0.5);
			var result = q.operator_multiply(r);

			var n2 = result.getX() * result.getX()
					+ result.getY() * result.getY()
					+ result.getZ() * result.getZ()
					+ result.getW() * result.getW();
			assertEpsilonEquals(1., n2);
		}
	}

	@DisplayName("this / Quaternion")
	@Nested
	public class OperatorDivideQuaternion {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_1(CoordinateSystem3D cs) {
			// mulInverse in-place #1: self mulInverse self = identity
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0.5, 0.5, 0.5, 0.5); // unit
			var result = q.operator_divide(createQuaternion(0.5, 0.5, 0.5, 0.5)); // q * q^-1
			assertEpsilonEquals(createQuaternion(0., 0., 0., 1.), result);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			// mulInverse in-place #2: right identity leaves quaternion unchanged
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0.5, -0.5, 0.5, -0.5); // unit
			var identity = createQuaternion(0., 0., 0., 1.);

			var result = q.operator_divide(identity); // q * 1^-1 = q

			assertEpsilonEquals(createQuaternion(0.5, -0.5, 0.5, -0.5), result);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_3(CoordinateSystem3D cs) {
			// mulInverse in-place #3: identity mulInverse q gives inverse(q)
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0., 0., 0., 1.);      // this = identity
			var other = createQuaternion(0.5, 0.5, 0.5, 0.5); // unit

			var result = q.operator_divide(other); // 1 * other^-1

			assertEpsilonEquals(createQuaternion(-0.5, -0.5, -0.5, 0.5), result);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_4(CoordinateSystem3D cs) {
			// mulInverse in-place #4: i mulInverse j = -k
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(1., 0., 0., 0.);      // i
			var other = createQuaternion(0., 1., 0., 0.);  // j

			var result = q.operator_divide(other); // i * j^-1 = i * (-j) = -k

			assertEpsilonEquals(createQuaternion(0., 0., -1., 0.), result);
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_5(CoordinateSystem3D cs) {
			// mulInverse in-place #6: norm preservation for unit inputs
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var q = createQuaternion(0.5, 0.5, 0.5, 0.5);
			var other = createQuaternion(0.5, -0.5, 0.5, -0.5);

			var result = q.operator_divide(other);

			var n2 = result.getX() * result.getX()
					+ result.getY() * result.getY()
					+ result.getZ() * result.getZ()
					+ result.getW() * result.getW();
			assertEpsilonEquals(1., n2);
		}
	}

	@DisplayName("-this")
	@Nested
	public class OperatorMinus {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.geometry.base.tests.AbstractQuaternionTestCase#proposeArguments")
		public void test_1(CoordinateSystem3D cs, Double a, Double b, Double c, Double d) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var mag = 1. / Math.sqrt(a * a + b * b + c * c + d * d);
			var q1 = createQuaternion(a, b, c, d);
			var inv = q1.operator_minus();
			assertEpsilonEquals(createQuaternion(-a * mag, -b * mag, -c * mag, d * mag), inv);
		}
	}

}
