/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3;

import org.junit.Assume;
import org.junit.Test;

import org.arakhne.afc.math.geometry.d3.d.Tuple3d;

@SuppressWarnings("all")
public abstract class AbstractUnmodifiableVector3DTest<V extends Vector3D<? super V, ? super P>, P extends Point3D<? super P, ? super V>>
		extends AbstractVector3DTest<V, P, Vector3D> {

	@Test(expected = UnsupportedOperationException.class)
	public final void absolute() {
		getT().absolute();
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void absoluteT() {
		Tuple3D c = new Tuple3d();
		getT().absolute(c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addIntInt() {
		getT().add(6, 7, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addDoubleDouble() {
		getT().add(6.5, 7.5, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addXInt() {
		getT().addX(6);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addXDouble() {
		getT().addX(6.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addYInt() {
		getT().addY(6);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addYDouble() {
		getT().addY(6.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void negateT() {
		Tuple3D c = new Tuple3d();
		getT().negate(c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void negate() {
		getT().negate();
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleIntT() {
		Tuple3D c = new Tuple3d(2, -1, 0);
		getT().scale(4, c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDoubleT() {
		Tuple3D c = new Tuple3d(2, -1, 0);
		getT().scale(4.5, c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleInt() {
		getT().scale(4);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDouble() {
		getT().scale(4.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setTuple3D() {
		Tuple3D c = new Tuple3d(-45, 78, 0);
		getT().set(c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setIntInt() {
		getT().set(-45, 78, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleDouble() {
		getT().set(-45.5, 78.5, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setIntArray() {
		getT().set(new int[]{-45, 78, 0});
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleArray() {
		getT().set(new double[]{-45.5, 78.5, 0});
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setXInt() {
		getT().setX(45);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setXDouble() {
		getT().setX(45.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setYInt() {
		getT().setY(45);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setYDouble() {
		getT().setY(45.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subIntInt() {
		getT().sub(45, 78, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subXInt() {
		getT().subX(45);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subYInt() {
		getT().subY(78);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subDoubleDouble() {
		getT().sub(45.5, 78.5, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subXDouble() {
		getT().subX(45.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subYDouble() {
		getT().subY(78.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().add(12.3, 4.56, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().add(12.3, 4.56, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().addX(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().addX(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addYDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().addY(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addYDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().addY(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDoubleT_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().scale(12.3, createTuple(1,2, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDoubleT_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().scale(12.3, createTuple(1,2, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().scale(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().scale(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().set(12.3, 4.56, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().set(12.3, 4.56, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleArray_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().set(new double[] {12.3, 4.56, 0});
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleArray_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().set(new double[] {12.3, 4.56, 0});
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().setX(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().setX(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setYDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().setY(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setYDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().setY(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().sub(12.3, 4.56, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().sub(12.3, 4.56, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().subX(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().subX(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subYDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().subY(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subYDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().subY(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addVector3DVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(0, 0, 0);
		Vector3D vector3 = createVector(1.2, 1.2, 0);
		Vector3D vector5 = createTuple(0.0, 0.0, 0);
		vector5.add(vector3,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addVector3DVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(0, 0, 0);
		Vector3D vector3 = createVector(1.2, 1.2, 0);
		Vector3D vector5 = createTuple(0.0, 0.0, 0);
		vector5.add(vector3,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createTuple(0,0, 0);
		Vector3D vector3 = createVector(1.2,1.2, 0);
		vector.add(vector3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createTuple(0,0, 0);
		Vector3D vector3 = createVector(1.2,1.2, 0);
		vector.add(vector3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddIntVector3DVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(-1,0, 0);
		Vector3D vector2 = createVector(1.0,1.2, 0);
		Vector3D vector3 = createTuple(0.0,0.0, 0);
		vector3.scaleAdd(0,vector2,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddIntVector3DVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(-1,0, 0);
		Vector3D vector2 = createVector(1.0,1.2, 0);
		Vector3D vector3 = createTuple(0.0,0.0, 0);
		vector3.scaleAdd(0,vector2,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector3DVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vect = createVector(1,0, 0);
		Vector3D vector = createVector(-1,1, 0);
		Vector3D newVector = createTuple(0.0,0.0, 0);
		newVector.scaleAdd(0.0, vector, vect);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector3DVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vect = createVector(1,0, 0);
		Vector3D vector = createVector(-1,1, 0);
		Vector3D newVector = createTuple(0.0,0.0, 0);
		newVector.scaleAdd(0.0, vector, vect);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddIntVector3D() {
		Vector3D vector = createVector(1,0, 0);
		Vector3D newVector = createTuple(0,0, 0);
		newVector.scaleAdd(0,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createVector(1,0, 0);
		Vector3D newVector = createTuple(0.0,0.0, 0);
		newVector.scaleAdd(0.5,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createVector(1,0, 0);
		Vector3D newTuple = createTuple(0.0,0.0, 0);
		newTuple.scaleAdd(0.5,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subVector3DVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vect = createVector(0, 0, 0);
		Vector3D vector = createVector(-1.2, -1.2, 0);
		Vector3D newVector = createTuple(0.0, 0.0, 0);
		newVector.sub(vect,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subVector3DVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vect = createVector(0, 0, 0);
		Vector3D vector = createVector(-1.2, -1.2, 0);
		Vector3D newVector = createTuple(0.0, 0.0, 0);
		newVector.sub(vect,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subPoint3DPoint3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point3D point = createPoint(0, 0, 0);
		Point3D vector = createPoint(-1.2, -1.2, 0);
		Vector3D newVector = createTuple(0.0, 0.0, 0);
		newVector.sub(point,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subPoint3DPoint3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point3D point = createPoint(0, 0, 0);
		Point3D vector = createPoint(-1.2, -1.2, 0);
		Vector3D newPoint = createTuple(0.0, 0.0, 0);
		newPoint.sub(point,vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vect = createTuple(0, 0, 0);
		Vector3D vector = createVector(-1.2, -1.2, 0);
		vect.sub(vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vect = createTuple(0, 0, 0);
		Vector3D vector = createVector(-1.2, -1.2, 0);
		vect.sub(vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void normalize_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createTuple(1,2, 0);
		vector.normalize();
	}

	@Test(expected = UnsupportedOperationException.class)  
	public final void normalize_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createTuple(1,2, 0);
		vector.normalize();
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void normalizeVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createTuple(0,0, 0);
		vector.normalize(createVector(1,2, 0));
	}

	@Test(expected = UnsupportedOperationException.class)  
	public final void normalizeVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createTuple(0,0, 0);
		vector.normalize(createVector(1,2, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setLength_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createTuple(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());
		double newLength = getRandom().nextDouble();
		vector.setLength(newLength);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setLength_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createTuple(0, 2, 0);
		int newLength = 5;
		vector.setLength(newLength);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void operator_addVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vector = createTuple(0,0, 0);
		Vector3D vector3 = createVector(1.2,1.2, 0);
		vector.operator_add(vector3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void operator_addVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vector = createTuple(0,0, 0);
		Vector3D vector3 = createVector(1.2,1.2, 0);
		vector.operator_add(vector3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void operator_removeVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Vector3D vect = createTuple(0, 0, 0);
		Vector3D vector = createVector(-1.2, -1.2, 0);
		vect.operator_remove(vector);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void operator_removeVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Vector3D vect = createTuple(0, 0, 0);
		Vector3D vector = createVector(-1.2, -1.2, 0);
		vect.operator_remove(vector);
	}

}
