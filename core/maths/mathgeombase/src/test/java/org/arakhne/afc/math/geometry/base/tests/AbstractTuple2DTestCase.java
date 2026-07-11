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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;

@SuppressWarnings("all")
public abstract class AbstractTuple2DTestCase<TT extends Tuple2D> extends AbstractMathTestCase {
	
	protected TT t;
	
	public TT getT() {
		return this.t;
	}
	
	@BeforeEach
	public void setUp() {
		this.t = createTuple(1, -2);
	}
	
	@AfterEach
	public void tearDown() {
		this.t = null;
	}

	public abstract TT createTuple(double x, double y);
	
	public abstract boolean isIntCoordinates();

	@DisplayName("getX")
	@Nested
	public class GetX {
	
		@DisplayName("getX")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void getX(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1., getT().getX());
		}

	}

	@DisplayName("ix")
	@Nested
	public class IX {
	
		@DisplayName("ix")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void ix(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(1, getT().ix());
		}

	}

	@DisplayName("getY")
	@Nested
	public class GetY {
	
		@DisplayName("getY")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void getY(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(-2., getT().getY());
		}

	}

	@DisplayName("iy")
	@Nested
	public class IY {
	
		@DisplayName("iy")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void iy(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, getT().iy());
		}

	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsObject {
	
		@DisplayName("Not equals")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D c = new InnerComputationVector2D();
			assertFalse(getT().equals(c));
		}

		@DisplayName("Equals")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D c = new InnerComputationVector2D(1, -2);
			assertTrue(getT().equals(c));
		}

		@DisplayName("Same")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void equals_same(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getT().equals(getT()));
		}

		@DisplayName("Null")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void equals_null(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getT().equals(null));
		}

	}

	@DisplayName("clone")
	@Nested
	public class CloneTest {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D clone = getT().clone();
			assertNotNull(clone);
			assertNotSame(getT(), clone);
			assertEpsilonEquals(getT().getX(), clone.getX());
			assertEpsilonEquals(getT().getY(), clone.getY());
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getT().absolute();
			assertEpsilonEquals(createTuple(1, 2), getT());
		}

	}

	@DisplayName("absolute(T)")
	@Nested
	public class AbsoluteT {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D c = new InnerComputationVector2D();
			getT().absolute(c);
			assertEpsilonEquals(createTuple(1, -2), getT());
			assertEpsilonEquals(createTuple(1, 2), c);
		}

	}

	@DisplayName("add(int,int)")
	@Nested
	public class AddIntInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().add(6, 7);
			assertEpsilonEquals(7, getT().getX());
			assertEpsilonEquals(5, getT().getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().add(6, 7);
			assertEpsilonEquals(7, getT().ix());
			assertEpsilonEquals(5, getT().iy());
		}

	}

	@DisplayName("add(double,double)")
	@Nested
	public class AddDoubleDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().add(6.5, 7.5);
			assertEpsilonEquals(7.5, getT().getX());
			assertEpsilonEquals(5.5, getT().getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().add(6.5, 7.5);
			assertEquals(8, getT().ix());
			assertEquals(6, getT().iy());
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getT().addX(6);
			assertEpsilonEquals(7, getT().getX());
			assertEpsilonEquals(-2, getT().getY());
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().addX(6.5);
			assertEpsilonEquals(7.5, getT().getX());
			assertEpsilonEquals(-2, getT().getY());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().addX(6.5);
			assertEquals(8, getT().ix());
			assertEquals(-2, getT().iy());
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getT().addY(6);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(4, getT().getY());
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().addY(6.5);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(4.5, getT().getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().addY(6.5);
			assertEquals(1, getT().ix());
			assertEquals(5, getT().iy());
		}

	}

	@DisplayName("negate(Tuple2D)")
	@Nested
	public class NegateTuple2D {

		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D c = new InnerComputationVector2D();
			getT().negate(c);
			assertEpsilonEquals(0, getT().getX());
			assertEpsilonEquals(0, getT().getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var c = new InnerComputationVector2D(25, -45);
			getT().negate(c);
			assertEpsilonEquals(-25, getT().getX());
			assertEpsilonEquals(45, getT().getY());
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getT().negate();
			assertEpsilonEquals(-1, getT().getX());
			assertEpsilonEquals(2, getT().getY());
		}

	}

	@DisplayName("scale(int,Tuple2D)")
	@Nested
	public class ScaleIntTuple2D {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D c = new InnerComputationVector2D(2, -1);
			getT().scale(4, c);
			assertEpsilonEquals(8, getT().getX());
			assertEpsilonEquals(-4, getT().getY());
		}

	}

	@DisplayName("scale(double,Tuple2D)")
	@Nested
	public class ScaleDoubleTuple2D {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Tuple2D c = new InnerComputationVector2D(2, -1);
			getT().scale(4.5, c);
			assertEpsilonEquals(9, getT().getX());
			assertEpsilonEquals(-4.5, getT().getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			Tuple2D c = new InnerComputationVector2D(2, -1);
			getT().scale(4.5, c);
			assertEquals(9, getT().ix());
			assertEquals(-4, getT().iy());
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getT().scale(4);
			assertEpsilonEquals(4, getT().getX());
			assertEpsilonEquals(-8, getT().getY());
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().scale(4.5);
			assertEpsilonEquals(4.5, getT().getX());
			assertEpsilonEquals(-9, getT().getY());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().scale(4.5);
			assertEquals(5, getT().ix());
			assertEquals(-9, getT().iy());
		}

	}

	@DisplayName("set(Tuple2D)")
	@Nested
	public class SetTuple2D {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D c = new InnerComputationVector2D(-45, 78);
			getT().set(c);
			assertEpsilonEquals(-45, getT().getX());
			assertEpsilonEquals(78, getT().getY());
		}

	}

	@DisplayName("set(int,int)")
	@Nested
	public class SetIntInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getT().set(-45, 78);
			assertEpsilonEquals(-45, getT().getX());
			assertEpsilonEquals(78, getT().getY());
		}

	}

	@DisplayName("set(double,double)")
	@Nested
	public class SetDoubleDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().set(-45.5, 78.5);
			assertEpsilonEquals(-45.5, getT().getX());
			assertEpsilonEquals(78.5, getT().getY());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void setDoubleDouble_ifi(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().set(-45.5, 78.5);
			assertEquals(-45, getT().ix());
			assertEquals(79, getT().iy());
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getT().set(new int[]{-45, 78});
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().set(new double[]{-45.5, 78.5});
			assertEpsilonEquals(-45.5, getT().getX());
			assertEpsilonEquals(78.5, getT().getY());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void setDoubleArray_ifi(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().set(new double[]{-45.5, 78.5});
			assertEquals(-45, getT().ix());
			assertEquals(79, getT().iy());
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getT().setX(45);
			assertEpsilonEquals(45, getT().getX());
			assertEpsilonEquals(-2, getT().getY());
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().setX(45.5);
			assertEpsilonEquals(45.5, getT().getX());
			assertEpsilonEquals(-2, getT().getY());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void setXDouble_ifi(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().setX(45.5);
			assertEquals(46, getT().ix());
			assertEquals(-2, getT().iy());
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().setY(45.5);
			assertEquals(1, getT().ix());
			assertEquals(46, getT().iy());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().setY(45.5);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(45.5, getT().getY());
		}

	}

	@DisplayName("sub(int,int)")
	@Nested
	public class SubIntInt {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getT().sub(45, 78);
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getT().subY(78);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(-80, getT().getY());
		}

	}

	@DisplayName("sub(double,double)")
	@Nested
	public class SubDoubleDouble {
	
		@BeforeEach
		public void setUp() {
			assumeMutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().sub(45.5, 78.5);
			assertEpsilonEquals(-44.5, getT().getX());
			assertEpsilonEquals(-80.5, getT().getY());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().sub(45.5, 78.5);
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().subX(45.5);
			assertEpsilonEquals(-44.5, getT().getX());
			assertEpsilonEquals(-2, getT().getY());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			getT().subY(78.5);
			assertEpsilonEquals(1, getT().getX());
			assertEpsilonEquals(-80.5, getT().getY());
		}
	
		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void subYDouble_ifi(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			getT().subY(78.5);
			assertEquals(1, getT().ix());
			assertEquals(-80, getT().iy());
		}

	}

}
