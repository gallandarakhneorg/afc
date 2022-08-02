/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Tuple2d;

@SuppressWarnings("all")
public abstract class AbstractUnmodifiableVector2DTest<V extends Vector2D<? super V, ? super P>, P extends Point2D<? super P, ? super V>>
		extends AbstractVector2DTest<V, P, Vector2D> {

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
		assertThrows(UnsupportedOperationException.class, () -> {
			Tuple2D c = new Tuple2d();
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
		assertThrows(UnsupportedOperationException.class, () -> {
			Tuple2D c = new Tuple2d(2, -1);
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
	public final void scaleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().scale(4.5));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setTuple2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			Tuple2D c = new Tuple2d(-45, 78);
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
	public final void setDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	public final void setDoubleArray(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	public final void addVector2DVector2D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector2D vector = createVector(0, 0);
		Vector2D vector3 = createVector(1.2, 1.2);
		Vector2D vector5 = createTuple(0.0, 0.0);
		assertThrows(UnsupportedOperationException.class, () -> {
			vector5.add(vector3,vector);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addVector2DVector2D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector2D vector = createVector(0, 0);
		Vector2D vector3 = createVector(1.2, 1.2);
		Vector2D vector5 = createTuple(0.0, 0.0);
		assertThrows(UnsupportedOperationException.class, () -> {
			vector5.add(vector3,vector);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addVector2D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector2D vector = createTuple(0,0);
		Vector2D vector3 = createVector(1.2,1.2);
		assertThrows(UnsupportedOperationException.class, () -> {
			vector.add(vector3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void addVector2D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector2D vector = createTuple(0,0);
		Vector2D vector3 = createVector(1.2,1.2);
		assertThrows(UnsupportedOperationException.class, () -> {
			vector.add(vector3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddIntVector2DVector2D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector2D vector = createVector(-1,0);
		Vector2D vector2 = createVector(1.0,1.2);
		Vector2D vector3 = createTuple(0.0,0.0);
		assertThrows(UnsupportedOperationException.class, () -> {
			vector3.scaleAdd(0,vector2,vector);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddIntVector2DVector2D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector2D vector = createVector(-1,0);
		Vector2D vector2 = createVector(1.0,1.2);
		Vector2D vector3 = createTuple(0.0,0.0);
		assertThrows(UnsupportedOperationException.class, () -> {
			vector3.scaleAdd(0,vector2,vector);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddDoubleVector2DVector2D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector2D vect = createVector(1,0);
		Vector2D vector = createVector(-1,1);
		Vector2D newVector = createTuple(0.0,0.0);
		assertThrows(UnsupportedOperationException.class, () -> {
			newVector.scaleAdd(0.0, vector, vect);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddDoubleVector2DVector2D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector2D vect = createVector(1,0);
		Vector2D vector = createVector(-1,1);
		Vector2D newVector = createTuple(0.0,0.0);
		assertThrows(UnsupportedOperationException.class, () -> {
			newVector.scaleAdd(0.0, vector, vect);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddIntVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D vector = createVector(1,0);
		Vector2D newVector = createTuple(0,0);
		assertThrows(UnsupportedOperationException.class, () -> {
			newVector.scaleAdd(0,vector);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddDoubleVector2D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector2D vector = createVector(1,0);
		Vector2D newVector = createTuple(0.0,0.0);
		assertThrows(UnsupportedOperationException.class, () -> {
			newVector.scaleAdd(0.5,vector);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void scaleAddDoubleVector2D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector2D vector = createVector(1,0);
		Vector2D newTuple = createTuple(0.0,0.0);
		assertThrows(UnsupportedOperationException.class, () -> {
			newTuple.scaleAdd(0.5,vector);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subVector2DVector2D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector2D vect = createVector(0, 0);
		Vector2D vector = createVector(-1.2, -1.2);
		Vector2D newVector = createTuple(0.0, 0.0);
		assertThrows(UnsupportedOperationException.class, () -> {
			newVector.sub(vect,vector);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subVector2DVector2D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector2D vect = createVector(0, 0);
		Vector2D vector = createVector(-1.2, -1.2);
		Vector2D newVector = createTuple(0.0, 0.0);
		assertThrows(UnsupportedOperationException.class, () -> {
			newVector.sub(vect,vector);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subPoint2DPoint2D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Point2D point = createPoint(0, 0);
		Point2D vector = createPoint(-1.2, -1.2);
		Vector2D newVector = createTuple(0.0, 0.0);
		assertThrows(UnsupportedOperationException.class, () -> {
			newVector.sub(point,vector);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subPoint2DPoint2D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Point2D point = createPoint(0, 0);
		Point2D vector = createPoint(-1.2, -1.2);
		Vector2D newPoint = createTuple(0.0, 0.0);
		assertThrows(UnsupportedOperationException.class, () -> {
			newPoint.sub(point,vector);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subVector2D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector2D vect = createTuple(0, 0);
		Vector2D vector = createVector(-1.2, -1.2);
		assertThrows(UnsupportedOperationException.class, () -> {
			vect.sub(vector);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void subVector2D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector2D vect = createTuple(0, 0);
		Vector2D vector = createVector(-1.2, -1.2);
		assertThrows(UnsupportedOperationException.class, () -> {
			vect.sub(vector);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void makeOrthogonal(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D vector = createTuple(1,2);
		assertThrows(UnsupportedOperationException.class, () -> {
			vector.makeOrthogonal();
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void normalize_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector2D vector = createTuple(1,2);
		assertThrows(UnsupportedOperationException.class, () -> {
			vector.normalize();
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)  
	public final void normalize_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector2D vector = createTuple(1,2);
		assertThrows(UnsupportedOperationException.class, () -> {
			vector.normalize();
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void normalizeVector3D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector2D vector = createTuple(0,0);
		assertThrows(UnsupportedOperationException.class, () -> {
			vector.normalize(createVector(1,2));
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)  
	public final void normalizeVector3D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector2D vector = createTuple(0,0);
		assertThrows(UnsupportedOperationException.class, () -> {
			vector.normalize(createVector(1,2));
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turn_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turn(MathConstants.DEMI_PI);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turn_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turn(MathConstants.DEMI_PI);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnVector_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turn(MathConstants.DEMI_PI, vector2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnVector_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turn(MathConstants.DEMI_PI, vector2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeft_iffp_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(MathConstants.DEMI_PI);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeft_iffp_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(MathConstants.DEMI_PI);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeft_ifi_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(MathConstants.DEMI_PI);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeft_ifi_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(MathConstants.DEMI_PI);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRight_iffp_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(MathConstants.DEMI_PI);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRight_iffp_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(MathConstants.DEMI_PI);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRight_ifi_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(MathConstants.DEMI_PI);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRight_ifi_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(MathConstants.DEMI_PI);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftVector_iffp_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(MathConstants.DEMI_PI, vector2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftVector_iffp_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(MathConstants.DEMI_PI, vector2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftVector_ifi_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(MathConstants.DEMI_PI, vector2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnLeftVector_ifi_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnLeft(MathConstants.DEMI_PI, vector2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightVector_iffp_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(MathConstants.DEMI_PI, vector2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightVector_iffp_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(MathConstants.DEMI_PI, vector2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightVector_ifi_rightHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(MathConstants.DEMI_PI, vector2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void turnRightVector_ifi_leftHanded(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isLeftHanded());
		Vector2D vector2;
		vector2 = createVector(2, 0);
		assertThrows(UnsupportedOperationException.class, () -> {
			getT().turnRight(MathConstants.DEMI_PI, vector2);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setLength_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector2D vector = createTuple(getRandom().nextDouble(), getRandom().nextDouble());
		double newLength = getRandom().nextDouble();
		assertThrows(UnsupportedOperationException.class, () -> {
			vector.setLength(newLength);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void setLength_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector2D vector = createTuple(0, 2);
		int newLength = 5;
		assertThrows(UnsupportedOperationException.class, () -> {
			vector.setLength(newLength);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void operator_addVector2D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector2D vector = createTuple(0,0);
		Vector2D vector3 = createVector(1.2,1.2);
		assertThrows(UnsupportedOperationException.class, () -> {
			vector.operator_add(vector3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void operator_addVector2D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector2D vector = createTuple(0,0);
		Vector2D vector3 = createVector(1.2,1.2);
		assertThrows(UnsupportedOperationException.class, () -> {
			vector.operator_add(vector3);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void operator_removeVector2D_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Vector2D vect = createTuple(0, 0);
		Vector2D vector = createVector(-1.2, -1.2);
		assertThrows(UnsupportedOperationException.class, () -> {
			vect.operator_remove(vector);
		});
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void operator_removeVector2D_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Vector2D vect = createTuple(0, 0);
		Vector2D vector = createVector(-1.2, -1.2);
		assertThrows(UnsupportedOperationException.class, () -> {
			vect.operator_remove(vector);
		});
	}

}
