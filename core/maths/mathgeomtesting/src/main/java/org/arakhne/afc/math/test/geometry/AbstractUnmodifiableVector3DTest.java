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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import org.junit.jupiter.api.Test;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.d.Tuple3d;

@SuppressWarnings("all")
public abstract class AbstractUnmodifiableVector3DTest<V extends Vector3D<? super V, ? super P, ? super Q>,
		P extends Point3D<? super P, ? super V, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>>
		extends AbstractVector3DTest<V, P, Q, Vector3D> {

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
	public final void addZInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().addZ(6));
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
	public final void setZInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().setZ(45));
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
	public final void subZInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> getT().subZ(78));
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
	public final void addZDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().addZ(12.3);
		});
	}

	@Override
	public final void addZDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().addZ(12.3);
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
	public final void setZDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().setZ(12.3);
		});
	}

	@Override
	public final void setZDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().setZ(12.3);
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

	@Override
	public final void subZDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			getT().subZ(12.3);
		});
	}

	@Override
	public final void subZDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			getT().subZ(12.3);
		});
	}

	@Override
	public final void addVector3DVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			Vector3D vector = createVector(0, 0, 0);
			Vector3D vector3 = createVector(1.2, 1.2, 0);
			Vector3D vector5 = createTuple(0.0, 0.0, 0);
			vector5.add(vector3,vector);
		});
	}

	@Override
	public final void addVector3DVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			Vector3D vector = createVector(0, 0, 0);
			Vector3D vector3 = createVector(1.2, 1.2, 0);
			Vector3D vector5 = createTuple(0.0, 0.0, 0);
			vector5.add(vector3,vector);
		});
	}

	@Override
	public final void addVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			Vector3D vector = createTuple(0,0, 0);
			Vector3D vector3 = createVector(1.2,1.2, 0);
			vector.add(vector3);
		});
	}

	@Override
	public final void addVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			Vector3D vector = createTuple(0,0, 0);
			Vector3D vector3 = createVector(1.2,1.2, 0);
			vector.add(vector3);
		});
	}

	@Override
	public final void scaleAddIntVector3DVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			Vector3D vector = createVector(-1,0, 0);
			Vector3D vector2 = createVector(1.0,1.2, 0);
			Vector3D vector3 = createTuple(0.0,0.0, 0);
			vector3.scaleAdd(0,vector2,vector);
		});
	}

	@Override
	public final void scaleAddIntVector3DVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			Vector3D vector = createVector(-1,0, 0);
			Vector3D vector2 = createVector(1.0,1.2, 0);
			Vector3D vector3 = createTuple(0.0,0.0, 0);
			vector3.scaleAdd(0,vector2,vector);
		});
	}

	@Override
	public final void scaleAddDoubleVector3DVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			Vector3D vect = createVector(1,0, 0);
			Vector3D vector = createVector(-1,1, 0);
			Vector3D newVector = createTuple(0.0,0.0, 0);
			newVector.scaleAdd(0.0, vector, vect);
		});
	}

	@Override
	public final void scaleAddDoubleVector3DVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			Vector3D vect = createVector(1,0, 0);
			Vector3D vector = createVector(-1,1, 0);
			Vector3D newVector = createTuple(0.0,0.0, 0);
			newVector.scaleAdd(0.0, vector, vect);
		});
	}

	@Override
	public final void scaleAddIntVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			Vector3D vector = createVector(1,0, 0);
			Vector3D newVector = createTuple(0,0, 0);
			newVector.scaleAdd(0,vector);
		});
	}

	@Override
	public final void scaleAddDoubleVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			Vector3D vector = createVector(1,0, 0);
			Vector3D newVector = createTuple(0.0,0.0, 0);
			newVector.scaleAdd(0.5,vector);
		});
	}

	@Override
	public final void scaleAddDoubleVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			Vector3D vector = createVector(1,0, 0);
			Vector3D newTuple = createTuple(0.0,0.0, 0);
			newTuple.scaleAdd(0.5,vector);
		});
	}

	@Override
	public final void subVector3DVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			Vector3D vect = createVector(0, 0, 0);
			Vector3D vector = createVector(-1.2, -1.2, 0);
			Vector3D newVector = createTuple(0.0, 0.0, 0);
			newVector.sub(vect,vector);
		});
	}

	@Override
	public final void subVector3DVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			Vector3D vect = createVector(0, 0, 0);
			Vector3D vector = createVector(-1.2, -1.2, 0);
			Vector3D newVector = createTuple(0.0, 0.0, 0);
			newVector.sub(vect,vector);
		});
	}

	@Override
	public final void subPoint3DPoint3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			Point3D point = createPoint(0, 0, 0);
			Point3D vector = createPoint(-1.2, -1.2, 0);
			Vector3D newVector = createTuple(0.0, 0.0, 0);
			newVector.sub(point,vector);
		});
	}

	@Override
	public final void subPoint3DPoint3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			Point3D point = createPoint(0, 0, 0);
			Point3D vector = createPoint(-1.2, -1.2, 0);
			Vector3D newPoint = createTuple(0.0, 0.0, 0);
			newPoint.sub(point,vector);
		});
	}

	@Override
	public final void subVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			Vector3D vect = createTuple(0, 0, 0);
			Vector3D vector = createVector(-1.2, -1.2, 0);
			vect.sub(vector);
		});
	}

	@Override
	public final void subVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			Vector3D vect = createTuple(0, 0, 0);
			Vector3D vector = createVector(-1.2, -1.2, 0);
			vect.sub(vector);
		});
	}

	@Override
	public final void normalize_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			Vector3D vector = createTuple(1,2, 0);
			vector.normalize();
		});
	}

	@Override
	public final void normalize_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			Vector3D vector = createTuple(1,2, 0);
			vector.normalize();
		});
	}

	@Override
	public final void normalizeVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			Vector3D vector = createTuple(0,0, 0);
			vector.normalize(createVector(1,2, 0));
		});
	}

	@Override
	public final void normalizeVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			Vector3D vector = createTuple(0,0, 0);
			vector.normalize(createVector(1,2, 0));
		});
	}

	@Override
	public final void setLength_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			Vector3D vector = createTuple(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());
			double newLength = getRandom().nextDouble();
			vector.setLength(newLength);
		});
	}

	@Override
	public final void setLength_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			Vector3D vector = createTuple(0, 2, 0);
			int newLength = 5;
			vector.setLength(newLength);
		});
	}

	@Override
	public final void operator_addVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			Vector3D vector = createTuple(0,0, 0);
			Vector3D vector3 = createVector(1.2,1.2, 0);
			vector.operator_add(vector3);
		});
	}

	@Override
	public final void operator_addVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			Vector3D vector = createTuple(0,0, 0);
			Vector3D vector3 = createVector(1.2,1.2, 0);
			vector.operator_add(vector3);
		});
	}

	@Override
	public final void operator_removeVector3D_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeFalse(isIntCoordinates());
			Vector3D vect = createTuple(0, 0, 0);
			Vector3D vector = createVector(-1.2, -1.2, 0);
			vect.operator_remove(vector);
		});
	}

	@Override
	public final void operator_removeVector3D_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(UnsupportedOperationException.class, () -> {
			assumeTrue(isIntCoordinates());
			Vector3D vect = createTuple(0, 0, 0);
			Vector3D vector = createVector(-1.2, -1.2, 0);
			vect.operator_remove(vector);
		});
	}

}
