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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractUnmodifiableTuple2DTestCase<TT extends Tuple2D> extends AbstractTuple2DTestCase<TT> {

	@DisplayName("absolute")
	@Nested
	public class Absolute {
		
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().absolute();
			});
		}

	}

	@DisplayName("absolute(T)")
	@Nested
	public class AbsoluteT {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D c = new InnerComputationVector2D();
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().absolute(c);
			});
		}

	}

	@DisplayName("add(int,int)")
	@Nested
	public class AddIntInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().add(6, 7);
			});
		}

	}

	@DisplayName("add(double,double)")
	@Nested
	public class AddDoubleDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().add(6.5, 7.5);
			});
		}

	}

	@DisplayName("addX(int)")
	@Nested
	public class AddXInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().addX(6);
			});
		}

	}

	@DisplayName("addX(double)")
	@Nested
	public class AddXDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().addX(6.5);
			});
		}

	}

	@DisplayName("addY(int)")
	@Nested
	public class AddYInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().addY(6);
			});
		}

	}

	@DisplayName("addY(double)")
	@Nested
	public class AddYDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().addY(6.5);
			});
		}

	}

	@DisplayName("negate(Tuple2D)")
	@Nested
	public class NegateTuple2D {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D c = new InnerComputationVector2D();
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().negate(c);
			});
		}

	}

	@DisplayName("negate")
	@Nested
	public class Negate {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().negate();
			});
		}

	}

	@DisplayName("scale(int,Tuple2D)")
	@Nested
	public class ScaleIntTuple2D {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D c = new InnerComputationVector2D(2, -1);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().scale(4, c);
			});
		}

	}

	@DisplayName("scale(double,Tuple2D)")
	@Nested
	public class ScaleDoubleTuple2D {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			Tuple2D c = new InnerComputationVector2D(2, -1);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().scale(4.5, c);
			});
		}

	}

	@DisplayName("scale(int)")
	@Nested
	public class ScaleInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().scale(4);
			});
		}

	}

	@DisplayName("scale(double)")
	@Nested
	public class ScaleDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().scale(4.5);
			});
		}

	}

	@DisplayName("set(Tuple2D)")
	@Nested
	public class SetTuple2D {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Tuple2D c = new InnerComputationVector2D(-45, 78);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().set(c);
			});
		}

	}

	@DisplayName("set(int,int)")
	@Nested
	public class SetIntInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().set(-45, 78);
			});
		}

	}

	@DisplayName("set(double,double)")
	@Nested
	public class SetDoubleDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().set(-45.5, 78.5);
			});
		}

	}

	@DisplayName("set(int[])")
	@Nested
	public class SetIntArray {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().set(new int[]{-45, 78});
			});
		}

	}

	@DisplayName("set(double[])")
	@Nested
	public class SetDoubleArray {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().set(new double[]{-45.5, 78.5});
			});
		}

	}

	@DisplayName("setX(int)")
	@Nested
	public class SetXInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().setX(45);
			});
		}

	}

	@DisplayName("setX(double)")
	@Nested
	public class SetXDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().setX(45.5);
			});
		}

	}

	@DisplayName("setY(int)")
	@Nested
	public class SetYInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().setY(45);
			});
		}

	}

	@DisplayName("setY(double)")
	@Nested
	public class SetYDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().setY(45.5);
			});
		}

	}

	@DisplayName("sub(int,int)")
	@Nested
	public class SubIntInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().sub(45, 78);
			});
		}

	}

	@DisplayName("subX(int)")
	@Nested
	public class SubXInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().subX(45);
			});
		}

	}

	@DisplayName("subY(int)")
	@Nested
	public class SubYInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().subY(78);
			});
		}

	}

	@DisplayName("sub(double,double)")
	@Nested
	public class SubDoubleDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().sub(45.5, 78.5);
			});
		}

	}
	
	@DisplayName("subX(double)")
	@Nested
	public class SubXDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().subX(45.5);
			});
		}

	}

	@DisplayName("subY(double)")
	@Nested
	public class SubYDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().subY(78.5);
			});
		}

	}

}
