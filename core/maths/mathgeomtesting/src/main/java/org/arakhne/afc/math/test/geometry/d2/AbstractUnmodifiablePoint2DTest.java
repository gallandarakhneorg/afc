/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.test.geometry.d2;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Tuple2d;

@SuppressWarnings("all")
public abstract class AbstractUnmodifiablePoint2DTest<P extends Point2D<? super P, ? super V>, V extends Vector2D<? super V, ? super P>>
		extends AbstractPoint2DTest<P, V, Point2D> {
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void absolute(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().absolute());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void absoluteT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new Tuple2d();
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().absolute(c);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().add(6, 7));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().add(6.5, 7.5));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addXInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().addX(6));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addXDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().addX(6.5));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addYInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().addY(6));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addYDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().addY(6.5));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void negateT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new Tuple2d();
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().negate(c);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void negate(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().negate());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleIntT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new Tuple2d(2, -1);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().scale(4, c);
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleDoubleT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new Tuple2d(2, -1);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().scale(4.5, c);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().scale(4));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleDouble() {
		assertThrows(UnsupportedOperationException.class, () -> getT().scale(4.5));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setTuple2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new Tuple2d(-45, 78);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().set(c);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().set(-45, 78));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setDoubleDouble() {
		assertThrows(UnsupportedOperationException.class, () -> getT().set(-45.5, 78.5));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setIntArray(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().set(new int[]{-45, 78}));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setDoubleArray() {
		assertThrows(UnsupportedOperationException.class, () -> getT().set(new double[]{-45.5, 78.5}));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setXInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().setX(45));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setXDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().setX(45.5));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setYInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().setY(45));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setYDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().setY(45.5));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().sub(45, 78));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subXInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().subX(45));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subYInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().subY(78));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().sub(45.5, 78.5));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subXDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().subX(45.5));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subYDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().subY(78.5));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addDoubleDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().add(12.3, 4.56);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addDoubleDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().add(12.3, 4.56);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addXDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().addX(12.3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addXDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().addX(12.3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addYDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().addY(12.3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addYDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().addY(12.3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleDoubleT_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().scale(12.3, createTuple(1,2));
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleDoubleT_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().scale(12.3, createTuple(1,2));
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().scale(12.3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().scale(12.3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setDoubleDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().set(12.3, 4.56);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setDoubleDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().set(12.3, 4.56);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setDoubleArray_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().set(new double[] {12.3, 4.56});
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setDoubleArray_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().set(new double[] {12.3, 4.56});
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setXDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().setX(12.3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setXDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().setX(12.3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setYDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().setY(12.3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setYDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().setY(12.3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subDoubleDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().sub(12.3, 4.56);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subDoubleDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().sub(12.3, 4.56);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subXDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().subX(12.3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subXDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().subX(12.3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subYDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().subY(12.3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subYDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().subY(12.3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addPoint2DVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.add(point, vector1);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addVector2DPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.add(vector1, point);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.add(vector1);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddDoubleVector2DPoint2D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.scaleAdd(2.5, vector1, point);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddDoubleVector2DPoint2D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.scaleAdd(2.5, vector1, point);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddIntVector2DPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.scaleAdd(2, vector1, point);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddIntPoint2DVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.scaleAdd(2, point, vector1);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddDoublePoint2DVector2D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.scaleAdd(2.5, point, vector1);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddDoublePoint2DVector2D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.scaleAdd(2.5, point, vector1);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddIntVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.scaleAdd(2, vector1);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddDoubleVector2D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.scaleAdd(2.5, vector1);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddDoubleVector2D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.scaleAdd(2.5, vector1);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subPoint2DVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.sub(point, vector1);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.sub(vector1);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void operator_addVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.operator_add(vector1);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void operator_removeVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D point = createTuple(1, 2);
		Vector2D vector1 = createVector(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			point.operator_remove(vector1);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turn(Math.PI/2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnDoublePoint2D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turn(Math.PI/2, createPoint(1, 0));
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnDoublePoint2DPoint2D_origin_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Point2D origin = createPoint(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turn(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnDoublePoint2DPoint2D_aroundP_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Point2D origin = createPoint(-45, 12);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turn(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDouble_iffp_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDouble_iffp_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDoublePoint2D_iffp_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2, createPoint(1, 0));
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDoublePoint2D_iffp_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2, createPoint(1, 0));
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDoublePoint2DPoint2D_origin_iffp_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDoublePoint2DPoint2D_origin_iffp_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDoublePoint2DPoint2D_aroundP_iffp_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDoublePoint2DPoint2D_aroundP_iffp_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDouble_iffp_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDouble_iffp_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDoublePoint2D_iffp_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2, createPoint(1, 0));
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDoublePoint2D_iffp_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		assumeFalse(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2, createPoint(1, 0));
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDoublePoint2DPoint2D_origin_iffp_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDoublePoint2DPoint2D_origin_iffp_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDoublePoint2DPoint2D_aroundP_iffp_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDoublePoint2DPoint2D_aroundP_iffp_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turn(Math.PI/2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnDoublePoint2D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turn(Math.PI/2, createPoint(1, 0));
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnDoublePoint2DPoint2D_origin_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Point2D origin = createPoint(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turn(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnDoublePoint2DPoint2D_aroundP_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Point2D origin = createPoint(-45, 12);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turn(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDouble_ifi_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDouble_ifi_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDoublePoint2D_ifi_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2, createPoint(1, 0));
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDoublePoint2D_ifi_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2, createPoint(1, 0));
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDoublePoint2DPoint2D_origin_ifi_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDoublePoint2DPoint2D_origin_ifi_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDoublePoint2DPoint2D_aroundP_ifi_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftDoublePoint2DPoint2D_aroundP_ifi_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDouble_ifi_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDouble_ifi_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDoublePoint2D_ifi_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2, createPoint(1, 0));
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDoublePoint2D_ifi_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		assumeFalse(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2, createPoint(1, 0));
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDoublePoint2DPoint2D_origin_ifi_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDoublePoint2DPoint2D_origin_ifi_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(0, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDoublePoint2DPoint2D_aroundP_ifi_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightDoublePoint2DPoint2D_aroundP_ifi_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());		
		assumeFalse(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());		
		Point2D origin = createPoint(-45, 12);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(Math.PI/2, createPoint(1, 0), origin);
		});
	}

}
