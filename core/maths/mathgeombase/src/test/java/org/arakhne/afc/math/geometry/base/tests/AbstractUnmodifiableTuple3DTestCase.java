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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationVector3D;
import org.arakhne.afc.math.geometry.base.d3.Tuple3D;

@SuppressWarnings("all")
public abstract class AbstractUnmodifiableTuple3DTestCase<TT extends Tuple3D> extends AbstractTuple3DTestCase<TT> {

	@DisplayName("absolute")
	@Nested
	public class Absolute {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().absolute();
			});
		}

	}

	@DisplayName("absolute(Tuple3D)")
	@Nested
	public class AbsoluteTuple3D {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Tuple3D c = new InnerComputationVector3D();
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().absolute(c);
			});
		}

	}

	@DisplayName("add(int,int,int)")
	@Nested
	public class AddIntIntInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().add(6, 7, 1);
			});
		}

	}

	@DisplayName("add(double,double,double)")
	@Nested
	public class AddDoubleDoubleDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().add(6.5, 7.5, 5.5);
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().addY(6.5);
			});
		}

	}

	@DisplayName("addZ(int)")
	@Nested
	public class AddZInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().addZ(5);
			});
		}

	}

	@DisplayName("addZ(double)")
	@Nested
	public class AddZDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().addZ(5.5);
			});
		}

	}

	@DisplayName("negate(Tuple3D)")
	@Nested
	public class NegateTuple3D {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Tuple3D c = new InnerComputationVector3D();
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().negate();
			});
		}

	}

	@DisplayName("scale(int,Tuple3D)")
	@Nested
	public class ScaleIntTuple3D {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Tuple3D c = new InnerComputationVector3D(2, -1, -2);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().scale(4, c);
			});
		}

	}

	@DisplayName("scale(double,Tuple3D)")
	@Nested
	public class ScaleDoubleTuple3D {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Tuple3D c = new InnerComputationVector3D(2, -1, 0);
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().scale(4.5);
			});
		}

	}

	@DisplayName("set(Tuple3D)")
	@Nested
	public class SetTuple3D {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Tuple3D c = new InnerComputationVector3D(-45, 78, 1);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().set(c);
			});
		}

	}

	@DisplayName("set(int,int,int)")
	@Nested
	public class SetIntIntInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().set(-45, 78, 1);
			});
		}

	}

	@DisplayName("set(double,double,double)")
	@Nested
	public class SetDoubleDoubleDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().set(-45.5, 78.5, 1.1);
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().set(new int[]{-45, 78, 1});
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().set(new double[]{-45.5, 78.5, 1.1});
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().setY(45.5);
			});
		}

	}

	@DisplayName("setZ(int)")
	@Nested
	public class SetZInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().setZ(45);
			});
		}

	}

	@DisplayName("setZ(double)")
	@Nested
	public class SetZDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().setZ(45.5);
			});
		}

	}

	@DisplayName("sub(int,int,int)")
	@Nested
	public class SubIntIntInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().sub(45, 78, 0);
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().subY(78);
			});
		}

	}

	@DisplayName("subZ(int)")
	@Nested
	public class SubZInt {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().subZ(78);
			});
		}

	}

	@DisplayName("sub(double,double,double)")
	@Nested
	public class SubDoubleDoubleDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().sub(45.5, 78.5, 0);
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().subY(78.5);
			});
		}

	}

	@DisplayName("subZ(double)")
	@Nested
	public class SubZDouble {
	
		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertThrows(UnsupportedOperationException.class, () -> {
				getT().subZ(78.5);
			});
		}
	
	}

}
