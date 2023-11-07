/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.test.geometry;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.d.Tuple3d;

@SuppressWarnings("all")
public abstract class AbstractUnmodifiableTuple3DTest<TT extends Tuple3D> extends AbstractTuple3DTest<TT> {
	
	@Override
	public final void absolute(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().absolute());
	}

	@Override
	public final void absoluteT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			Tuple3D c = new Tuple3d();
			getT().absolute(c);
		});
	}

	@Override
	public final void addIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().add(6, 7, 0));
	}

	@Override
	public final void addXInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().addX(6));
	}

	@Override
	public final void addYInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().addY(6));
	}

	@Override
	public final void negateT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			Tuple3D c = new Tuple3d();
			getT().negate(c);
		});
	}

	@Override
	public final void negate(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().negate());
	}

	@Override
	public final void scaleIntT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			Tuple3D c = new Tuple3d(2, -1, 0);
			getT().scale(4, c);
		});
	}

	@Override
	public final void scaleInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().scale(4));
	}

	@Override
	public final void setTuple3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			Tuple3D c = new Tuple3d(-45, 78, 0);
			getT().set(c);
		});
	}

	@Override
	public final void setIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().set(-45, 78, 0));
	}

	@Override
	public final void setIntArray(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().set(new int[]{-45, 78, 0}));
	}

	@Override
	public final void setXInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().setX(45));
	}

	@Override
	public final void setYInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().setY(45));
	}

	@Override
	public final void subIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().sub(45, 78, 0));
	}

	@Override
	public final void subXInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().subX(45));
	}

	@Override
	public final void subYInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().subY(78));
	}

	@Override
	public final void addDoubleDoubleDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().add(12.3, 4.56, 0);
		});
	}

	@Override
	public final void addDoubleDoubleDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().add(12.3, 4.56, 0);
		});
	}

	@Override
	public final void addXDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().addX(12.3);
		});
	}

	@Override
	public final void addXDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().addX(12.3);
		});
	}

	@Override
	public final void addYDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().addY(12.3);
		});
	}

	@Override
	public final void addYDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().addY(12.3);
		});
	}

	@Override
	public final void scaleDoubleT_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().scale(12.3, createTuple(1,2, 0));
		});
	}

	@Override
	public final void scaleDoubleT_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().scale(12.3, createTuple(1,2, 0));
		});
	}

	@Override
	public final void scaleDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().scale(12.3);
		});
	}

	@Override
	public final void scaleDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().scale(12.3);
		});
	}

	@Override
	public final void setDoubleDoubleDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().set(12.3, 4.56, 0);
		});
	}

	@Override
	public final void setDoubleDoubleDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().set(12.3, 4.56, 0);
		});
	}

	@Override
	public final void setDoubleArray_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().set(new double[] {12.3, 4.56, 0});
		});
	}

	@Override
	public final void setDoubleArray_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().set(new double[] {12.3, 4.56, 0});
		});
	}

	@Override
	public final void setXDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().setX(12.3);
		});
	}

	@Override
	public final void setXDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().setX(12.3);
		});
	}

	@Override
	public final void setYDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().setY(12.3);
		});
	}

	@Override
	public final void setYDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().setY(12.3);
		});
	}

	@Override
	public final void subDoubleDoubleDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().sub(12.3, 4.56, 0);
		});
	}

	@Override
	public final void subDoubleDoubleDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().sub(12.3, 4.56, 0);
		});
	}

	@Override
	public final void subXDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().subX(12.3);
		});
	}

	@Override
	public final void subXDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().subX(12.3);
		});
	}

	@Override
	public final void subYDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().subY(12.3);
		});
	}

	@Override
	public final void subYDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().subY(12.3);
		});
	}

}
