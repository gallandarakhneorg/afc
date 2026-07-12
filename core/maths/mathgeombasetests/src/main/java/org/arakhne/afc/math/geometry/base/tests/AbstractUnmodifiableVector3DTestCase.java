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
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractUnmodifiableVector3DTestCase<V extends Vector3D<? super V, ? super P, ? super Q>,
		P extends Point3D<? super P, ? super V, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>>
		extends AbstractVector3DTestCase<V, P, Q, Vector3D> {

	@DisplayName("add(Vector3D,Vector3D)")
	@Nested
	public class AddVector3DVector3D {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("With double coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().add(createVector(1,2,3), createVector(4,5,6)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().add(createVector(1,2,3), createVector(4,5,6)));
		}

	}

	@DisplayName("add(Vector3D)")
	@Nested
	public class AddVector3D {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("With double coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().add(createVector(1,2,3)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().add(createVector(1,2,3)));
		}

	}

	@DisplayName("scaleAdd(int,Vector3D,Vector3D)")
	@Nested
	public class ScaleAddIntVector3DVector3D {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("With double coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1, createVector(1,2,3), createVector(4,5,6)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1, createVector(1,2,3), createVector(4,5,6)));
		}

	}

	@DisplayName("scaleAdd(double,Vector3D,Vector3D)")
	@Nested
	public class ScaleAddDoubleVector3DVector3D {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("With double coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1., createVector(1,2,3), createVector(4,5,6)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1., createVector(1,2,3), createVector(4,5,6)));
		}

	}

	@DisplayName("scaleAdd(int,Vector3D)")
	@Nested
	public class ScaleAddIntVector3D {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("With double coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1, createVector(4,5,6)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1, createVector(4,5,6)));
		}

	}

	@DisplayName("scaleAdd(double,Vector3D)")
	@Nested
	public class ScaleAddDoubleVector3D {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("With double coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1., createVector(4,5,6)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1., createVector(4,5,6)));
		}

	}

	@DisplayName("sub(Vector3D,Vector3D)")
	@Nested
	public class SubVector3DVector3D {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("With double coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().sub(createVector(4,5,6), createVector(1,2,3)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().sub(createVector(4,5,6), createVector(1,2,3)));
		}

	}

	@DisplayName("sub(Point3D,Point3D)")
	@Nested
	public class SubPoint3DPoint3D {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("With double coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().sub(createPoint(4,5,6), createPoint(1,2,3)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().sub(createPoint(4,5,6), createPoint(1,2,3)));
		}

	}

	@DisplayName("sub(Vector3D)")
	@Nested
	public class SubVector3D {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("With double coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().sub(createVector(1,2,3)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().sub(createVector(1,2,3)));
		}

	}

	@DisplayName("normalize")
	@Nested
	public class Normalize {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("With double coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().normalize());
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().normalize());
		}

	}

	@DisplayName("normalize(Vector3D)")
	@Nested
	public class NormalizeVector3D {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("With double coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().normalize(createVector(1,2,3)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().normalize(createVector(1,2,3)));
		}

	}

	@DisplayName("setLength")
	@Nested
	public class SetLength {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("With double coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().setLength(1.));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().setLength(1.));
		}

	}

	@DisplayName("v += Vector3D")
	@Nested
	public class OperatorAddVector3D {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("With double coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().operator_add(createVector(1,2,3)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().operator_add(createVector(1,2,3)));
		}

	}

	@DisplayName("v -= Vector3D")
	@Nested
	public class OperatorSubVector3D {

		@BeforeEach
		public void setUp() {
			assumeImmutable(getT());
		}
		
		@DisplayName("With double coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void double_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeFalse(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().operator_remove(createVector(1,2,3)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().operator_remove(createVector(1,2,3)));
		}

	}

}
