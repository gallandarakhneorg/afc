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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;

@SuppressWarnings("all")
public abstract class AbstractUnmodifiablePoint2DTestCase<P extends Point2D<? super P, ? super V>, V extends Vector2D<? super V, ? super P>>
		extends AbstractPoint2DTestCase<P, V, Point2D> {

	@DisplayName("add(Point2D, Vector2D)")
	@Nested
	public class AddPoint2DVector2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().add(createPoint(1,2), createVector(3,4)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().add(createPoint(1,2), createVector(3,4)));
		}

	}

	@DisplayName("add(Vector2D, Point2D)")
	@Nested
	public class AddVector2DPoint2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().add(createVector(3,4), createPoint(1,2)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().add(createVector(3,4), createPoint(1,2)));
		}

	}

	@DisplayName("scaleAdd(double,Vector2D,Point2D)")
	@Nested
	public class ScaleAddDoubleVector2DPoint2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1., createVector(3,4), createPoint(1,2)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1., createVector(3,4), createPoint(1,2)));
		}

	}

	@DisplayName("scaleAdd(int,Vector2D,Point2D)")
	@Nested
	public class ScaleAddIntVector2DPoint2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1, createVector(3,4), createPoint(1,2)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1, createVector(3,4), createPoint(1,2)));
		}

	}

	@DisplayName("scaleAdd(int,Point2D,Vector2D)")
	@Nested
	public class ScaleAddIntPoint2DVector2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1, createPoint(1,2), createVector(3,4)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1, createPoint(1,2), createVector(3,4)));
		}

	}

	@DisplayName("scaleAdd(double,Point2D,Vector2D)")
	@Nested
	public class ScaleAddDoublePoint2DVector2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1., createPoint(1,2), createVector(3,4)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1., createPoint(1,2), createVector(3,4)));
		}

	}

	@DisplayName("scaleAdd(int,Vector2D)")
	@Nested
	public class ScaleAddIntVector2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1, createVector(3,4)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1, createVector(3,4)));
		}

	}

	@DisplayName("scaleAdd(double,Vector2D)")
	@Nested
	public class ScaleAddDoubleVector2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1., createVector(3,4)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().scaleAdd(1., createVector(3,4)));
		}

	}

	@DisplayName("sub(Point3D,Vector2D)")
	@Nested
	public class SubPoint3DVector2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().sub(createPoint(1,2), createVector(3,4)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().sub(createPoint(1,2), createVector(3,4)));
		}

	}

	@DisplayName("sub(Vector2D)")
	@Nested
	public class SubVector2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().sub(createVector(3,4)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().sub(createVector(3,4)));
		}

	}

	@DisplayName("this += Vector2D")
	@Nested
	public class OperatorAddVector2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().operator_add(createVector(3,4)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().operator_add(createVector(3,4)));
		}

	}

	@DisplayName("this -= Vector2D")
	@Nested
	public class OperatorRemoveVector2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().operator_remove(createVector(3,4)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().operator_remove(createVector(3,4)));
		}

	}

	@DisplayName("turn(double)")
	@Nested
	public class TurnDouble {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().turn(1.));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().turn(1.));
		}

	}

	@DisplayName("turn(double,Point2D,Point2D)")
	@Nested
	public class TurnDoublePoint2DPoint2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().turn(1., createPoint(1,2), createPoint(1,2)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().turn(1., createPoint(1,2), createPoint(1,2)));
		}

	}

	@DisplayName("turnLeft(double)")
	@Nested
	public class TurnLeftDouble {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().turnLeft(1.));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().turnLeft(1.));
		}

	}

	@DisplayName("turnLeft(double,Point2D)")
	@Nested
	public class TurnLeftDoublePoint2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().turnLeft(1., createPoint(1, 2)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().turnLeft(1., createPoint(1, 2)));
		}

	}

	@DisplayName("turnLeft(double,Point2D,Point2D)")
	@Nested
	public class TurnLeftDoublePoint2DPoint2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().turnLeft(1., createPoint(1, 2), createPoint(1, 2)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().turnLeft(1., createPoint(1, 2), createPoint(1, 2)));
		}

	}

	@DisplayName("turnRight(double)")
	@Nested
	public class TurnRightDouble {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().turnRight(1.));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().turnRight(1.));
		}

	}

	@DisplayName("turnRight(double,Point2D)")
	@Nested
	public class TurnRightDoublePoint2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().turnRight(1., createPoint(1, 2)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().turnRight(1., createPoint(1, 2)));
		}

	}

	@DisplayName("turnRight(double,Point2D,Point2D)")
	@Nested
	public class TurnRightDoublePoint2DPoint2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().turnRight(1., createPoint(1, 2), createPoint(1, 2)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().turnRight(1., createPoint(1, 2), createPoint(1, 2)));
		}

	}

	@DisplayName("turn(double,Point2D)")
	@Nested
	public class TurnDoublePoint2D {

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
			assertThrows(UnsupportedOperationException.class, () -> getT().turn(1., createPoint(1, 2)));
		}
		
		@DisplayName("With int coords")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public final void int_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(isIntCoordinates());
			assertThrows(UnsupportedOperationException.class, () -> getT().turn(1., createPoint(1, 2)));
		}

	}

}
