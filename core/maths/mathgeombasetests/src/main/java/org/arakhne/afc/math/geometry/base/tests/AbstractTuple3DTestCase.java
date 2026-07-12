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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationVector3D;
import org.arakhne.afc.math.geometry.base.d3.Tuple3D;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractTuple3DTestCase<TT extends Tuple3D> extends AbstractMathTestCase {
	
	protected TT t;
	
	public TT getT() {
		return this.t;
	}
	
	@BeforeEach
	public void setUp() {
		this.t = createTuple(1, -2, 0);
	}
	
	@AfterEach
	public void tearDown() {
		this.t = null;
	}

	public abstract TT createTuple(double x, double y, double z);
	
	public abstract boolean isIntCoordinates();

	@DisplayName("getX")
	@Nested
	public class GetX {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1., getT().getX());
		}

	}

	@DisplayName("ix")
	@Nested
	public class IX {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(1, getT().ix());
		}

	}
	
	@DisplayName("getY")
	@Nested
	public class GetY {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-2., getT().getY());
		}

	}

	@DisplayName("iy")
	@Nested
	public class IY {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, getT().iy());
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
			assertEpsilonEquals(0., getT().getZ());
		}

	}

	@DisplayName("iz")
	@Nested
	public class IZ {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEquals(0, getT().iz());
		}

	}

	@DisplayName("equals(Tuple3D)")
	@Nested
	public class EqualsTuple3D {
	
		@DisplayName("Inequality")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Tuple3D c = new InnerComputationVector3D();
			assertFalse(getT().equals(c));
		}
		
		@DisplayName("Equality")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Tuple3D c = new InnerComputationVector3D(1, -2, 0);
			assertTrue(getT().equals(c));
		}
	
		@DisplayName("Same")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertTrue(getT().equals(getT()));
		}
	
		@DisplayName("Null")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_4(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getT().equals(null));
		}

	}

	@DisplayName("clone")
	@Nested
	public class CloneTest {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Tuple3D clone = getT().clone();
			assertNotNull(clone);
			assertNotSame(getT(), clone);
			assertEpsilonEquals(getT().getX(), clone.getX());
			assertEpsilonEquals(getT().getY(), clone.getY());
			assertEpsilonEquals(getT().getZ(), clone.getZ());
		}

	}

	@DisplayName("absolute")
	@Nested
	public class Absolute {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().absolute();
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(2, getT().getY());
			assertEpsilonEquals(0, getT().getZ());
		}

	}

	@DisplayName("absolute(Tuple3D)")
	@Nested
	public class AbsoluteTuple3D {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Tuple3D c = new InnerComputationVector3D();
			getT().absolute(c);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(-2, getT().getY());
			assertEpsilonEquals(0, getT().getZ());
			assertEpsilonEquals(1, c.getX());
			assertEpsilonEquals(2, c.getY());
			assertEpsilonEquals(0, c.getZ());
		}

	}

	@DisplayName("add(int,int,int)")
	@Nested
	public class AddIntIntInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().add(6, 7, 1);
			assertEpsilonEquals(7, getT().getX());
			assertEpsilonEquals(5, getT().getY());
			assertEpsilonEquals(1, getT().getZ());
		}

	}

	@DisplayName("add(double,double,double)")
	@Nested
	public class AddDoubleDoubleDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().add(6.5, 7.5, 5.5);
			assertEpsilonEquals(7.5, getT().getX());
			assertEpsilonEquals(5.5, getT().getY());
			assertEpsilonEquals(5.5, getT().getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeMutable(getT());
			assumeTrue(isIntCoordinates());
			getT().add(6.5, 7.5, 5.5);
			assertEquals(8, getT().ix());
			assertEquals(6, getT().iy());
			assertEquals(11, getT().iz());
		}

	}

	@DisplayName("addX(int)")
	@Nested
	public class AddXInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().addX(6);
			assertEpsilonEquals(7, getT().getX());
			assertEpsilonEquals(-2, getT().getY());
			assertEpsilonEquals(0, getT().getZ());
		}

	}

	@DisplayName("addX(double)")
	@Nested
	public class AddXDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().addX(6.5);
			assertEpsilonEquals(7.5, getT().getX());
			assertEpsilonEquals(-2, getT().getY());
			assertEpsilonEquals(0, getT().getZ());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().addX(6.5);
			assertEquals(8, getT().ix());
			assertEquals(-2, getT().iy());
			assertEquals(0, getT().iz());
		}

	}
	
	@DisplayName("addY(int)")
	@Nested
	public class AddYInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().addY(6);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(4, getT().getY());
			assertEpsilonEquals(0, getT().getZ());
		}

	}
	
	@DisplayName("addY(double)")
	@Nested
	public class AddYDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().addY(6.5);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(4.5, getT().getY());
			assertEpsilonEquals(0, getT().getZ());
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeMutable(getT());
			assumeTrue(isIntCoordinates());
			getT().addY(6.5);
			assertEquals(1, getT().ix());
			assertEquals(5, getT().iy());
			assertEquals(0, getT().iz());
		}

	}

	@DisplayName("addZ(int)")
	@Nested
	public class AddZInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().addZ(5);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(-2, getT().getY());
			assertEpsilonEquals(5, getT().getZ());
		}

	}

	@DisplayName("addZ(double)")
	@Nested
	public class AddZDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().addZ(5.5);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(-2, getT().getY());
			assertEpsilonEquals(5.5, getT().getZ());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().addZ(5.5);
			assertEquals(1, getT().ix());
			assertEquals(-2, getT().iy());
			assertEquals(6, getT().iz());
		}

	}

	@DisplayName("negate(Tuple3D)")
	@Nested
	public class NegateTuple3D {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Tuple3D c = new InnerComputationVector3D();
			getT().negate(c);
			assertEpsilonEquals(0, getT().getX());
			assertEpsilonEquals(0, getT().getY());
			assertEpsilonEquals(0, getT().getZ());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var c = new InnerComputationVector3D(25, -45, -17);
			getT().negate(c);
			assertEpsilonEquals(-25, getT().getX());
			assertEpsilonEquals(45, getT().getY());
			assertEpsilonEquals(17, getT().getZ());
		}

	}

	@DisplayName("negate")
	@Nested
	public class Negate {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().negate();
			assertEpsilonEquals(-1, getT().getX());
			assertEpsilonEquals(2, getT().getY());
			assertEpsilonEquals(-0, getT().getZ());
		}

	}

	@DisplayName("scale(int,Tuple3D)")
	@Nested
	public class ScaleIntTuple3D {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Tuple3D c = new InnerComputationVector3D(2, -1, -2);
			getT().scale(4, c);
			assertEpsilonEquals(8, getT().getX());
			assertEpsilonEquals(-4, getT().getY());
			assertEpsilonEquals(-8, getT().getZ());
		}

	}

	@DisplayName("scale(double,Tuple3D)")
	@Nested
	public class ScaleDoubleTuple3D {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Tuple3D c = new InnerComputationVector3D(2, -1, 0);
			getT().scale(4.5, c);
			assertEpsilonEquals(9, getT().getX());
			assertEpsilonEquals(-4.5, getT().getY());
			assertEpsilonEquals(0, getT().getZ());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Tuple3D c = new InnerComputationVector3D(2, -1, 0);
			getT().scale(4.5, c);
			assertEquals(9, getT().ix());
			assertEquals(-4, getT().iy());
			assertEquals(0, getT().iz());
		}

	}

	@DisplayName("scale(int)")
	@Nested
	public class ScaleInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().scale(4);
			assertEpsilonEquals(4, getT().getX());
			assertEpsilonEquals(-8, getT().getY());
			assertEpsilonEquals(0, getT().getZ());
		}

	}

	@DisplayName("scale(double)")
	@Nested
	public class ScaleDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().scale(4.5);
			assertEpsilonEquals(4.5, getT().getX());
			assertEpsilonEquals(-9, getT().getY());
			assertEpsilonEquals(0, getT().getZ());
		}
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().scale(4.5);
			assertEquals(5, getT().ix());
			assertEquals(-9, getT().iy());
			assertEquals(0, getT().iz());
		}

	}

	@DisplayName("set(Tuple3D)")
	@Nested
	public class SetTuple3D {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Tuple3D c = new InnerComputationVector3D(-45, 78, 1);
			getT().set(c);
			assertEpsilonEquals(-45, getT().getX());
			assertEpsilonEquals(78, getT().getY());
			assertEpsilonEquals(1, getT().getZ());
		}

	}

	@DisplayName("set(int,int,int)")
	@Nested
	public class SetIntIntInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().set(-45, 78, 1);
			assertEpsilonEquals(-45, getT().getX());
			assertEpsilonEquals(78, getT().getY());
			assertEpsilonEquals(1, getT().getZ());
		}

	}

	@DisplayName("set(double,double,double)")
	@Nested
	public class SetDoubleDoubleDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().set(-45.5, 78.5, 1.1);
			assertEpsilonEquals(-45.5, getT().getX());
			assertEpsilonEquals(78.5, getT().getY());
			assertEpsilonEquals(1.1, getT().getZ());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().set(-45.5, 78.5, 1.1);
			assertEquals(-45, getT().ix());
			assertEquals(79, getT().iy());
			assertEquals(1, getT().iz());
		}

	}

	@DisplayName("set(int[])")
	@Nested
	public class SetIntArray {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().set(new int[]{-45, 78, 1});
			assertEpsilonEquals(-45, getT().getX());
			assertEpsilonEquals(78, getT().getY());
		}

	}

	@DisplayName("set(double[])")
	@Nested
	public class SetDoubleArray {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().set(new double[]{-45.5, 78.5, 1.1});
			assertEpsilonEquals(-45.5, getT().getX());
			assertEpsilonEquals(78.5, getT().getY());
			assertEpsilonEquals(1.1, getT().getZ());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().set(new double[]{-45.5, 78.5, 1.1});
			assertEquals(-45, getT().ix());
			assertEquals(79, getT().iy());
			assertEquals(1, getT().iz());
		}

	}

	@DisplayName("setX(int)")
	@Nested
	public class SetXInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setX(45);
			assertEpsilonEquals(45, getT().getX());
			assertEpsilonEquals(-2, getT().getY());
			assertEpsilonEquals(0, getT().getZ());
		}

	}

	@DisplayName("setX(double)")
	@Nested
	public class SetXDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().setX(45.5);
			assertEpsilonEquals(45.5, getT().getX());
			assertEpsilonEquals(-2, getT().getY());
			assertEpsilonEquals(0, getT().getZ());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().setX(45.5);
			assertEquals(46, getT().ix());
			assertEquals(-2, getT().iy());
			assertEquals(0, getT().iz());
		}

	}

	@DisplayName("setY(int)")
	@Nested
	public class SetYInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setY(45);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(45, getT().getY());
		}

	}

	@DisplayName("setY(double)")
	@Nested
	public class SetYDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().setY(45.5);
			assertEquals(1, getT().ix());
			assertEquals(46, getT().iy());
		}
	
		@DisplayName("setY(double) with int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void setYDouble_iffp(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().setY(45.5);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(45.5, getT().getY());
		}

	}

	@DisplayName("setZ(int)")
	@Nested
	public class SetZInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().setZ(45);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(45, getT().getZ());
		}

	}

	@DisplayName("setZ(double)")
	@Nested
	public class SetZDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().setZ(45.5);
			assertEquals(1, getT().ix());
			assertEquals(46, getT().iz());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeMutable(getT());
			assumeFalse(isIntCoordinates());
			getT().setZ(45.5);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(45.5, getT().getZ());
		}

	}

	@DisplayName("sub(int,int,int)")
	@Nested
	public class SubIntIntInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().sub(45, 78, 0);
			assertEpsilonEquals(-44, getT().getX());
			assertEpsilonEquals(-80, getT().getY());
		}

	}

	@DisplayName("subX(int)")
	@Nested
	public class SubXInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().subX(45);
			assertEpsilonEquals(-44, getT().getX());
			assertEpsilonEquals(-2, getT().getY());
		}

	}

	@DisplayName("subY(int)")
	@Nested
	public class SubYInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().subY(78);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(-80, getT().getY());
		}

	}

	@DisplayName("subZ(int)")
	@Nested
	public class SubZInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getT().subZ(78);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(-78, getT().getZ());
		}

	}

	@DisplayName("sub(double,double,double)")
	@Nested
	public class SubDoubleDoubleDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().sub(45.5, 78.5, 0);
			assertEpsilonEquals(-44.5, getT().getX());
			assertEpsilonEquals(-80.5, getT().getY());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void subDoubleDoubleDouble_ifi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().sub(45.5, 78.5, 0);
			assertEquals(-44, getT().ix());
			assertEquals(-80, getT().iy());
		}

	}

	@DisplayName("subX(double)")
	@Nested
	public class SubXDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().subX(45.5);
			assertEpsilonEquals(-44.5, getT().getX());
			assertEpsilonEquals(-2, getT().getY());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeMutable(getT());
			assumeTrue(isIntCoordinates());
			getT().subX(45.5);
			assertEquals(-44, getT().ix());
			assertEquals(-2, getT().iy());
		}

	}

	@DisplayName("subY(double)")
	@Nested
	public class SubYDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().subY(78.5);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(-80.5, getT().getY());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().subY(78.5);
			assertEquals(1, getT().ix());
			assertEquals(-80, getT().iy());
		}

	}

	@DisplayName("subZ(double)")
	@Nested
	public class SubZDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().subZ(78.5);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(-78.5, getT().getZ());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assumeMutable(getT());
			assumeTrue(isIntCoordinates());
			getT().subZ(78.5);
			assertEquals(1, getT().ix());
			assertEquals(-79, getT().iz());
		}

	}

}
