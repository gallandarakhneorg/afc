/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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
public abstract class AbstractUnmodifiablePoint3DTest<P extends Point3D<? super P, ? super V>, V extends Vector3D<? super V, ? super P>>
               extends AbstractPoint3DTest<P, V, Point3D> {
	
	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void absolute() {
		getT().absolute();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void absoluteT() {
		Tuple3D c = new Tuple3d();
		getT().absolute(c);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addIntIntInt() {
		getT().add(6, 7, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addDoubleDoubleDouble() {
		getT().add(6.5, 7.5, 0);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addXInt() {
		getT().addX(6);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addXDouble() {
		getT().addX(6.5);
	}
	
	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addYInt() {
	    getT().addY(6);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public final void addYDouble() {
	    getT().addY(6.5);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addZInt() {
		getT().addZ(6);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void addZDouble() {
		getT().addZ(6.5);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void negateT() {
		Tuple3D c = new Tuple3d();
		getT().negate(c);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void negate() {
		getT().negate();
	}

	@Override
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

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void scaleInt() {
		getT().scale(4);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDouble() {
		getT().scale(4.5);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void setTuple3D() {
		Tuple3D c = new Tuple3d(-45, 78, 0);
		getT().set(c);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void setIntIntInt() {
		getT().set(-45, 78, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleDoubleDouble() {
		getT().set(-45.5, 78.5, 0);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void setIntArray() {
		getT().set(new int[]{-45, 78, 0});
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleArray() {
		getT().set(new double[]{-45.5, 78.5, 0});
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void setXInt() {
		getT().setX(45);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setXDouble() {
		getT().setX(45.5);
	}
	
	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void setYInt() {
	    getT().setY(45);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public final void setYDouble() {
	    getT().setY(45.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setZInt() {
		getT().setZ(45);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setZDouble() {
		getT().setZ(45.5);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void subIntIntInt() {
		getT().sub(45, 78, 0);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void subXInt() {
		getT().subX(45);
	}
	
	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void subYInt() {
	    getT().subY(78);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subZInt() {
		getT().subZ(78);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subDoubleDoubleDouble() {
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
	public final void subZDouble() {
		getT().subZ(78.5);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().add(12.3, 4.56, 0);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().add(12.3, 4.56, 0);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().addX(12.3);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().addX(12.3);
	}
	
	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addYDouble_iffp() {
	    Assume.assumeFalse(isIntCoordinates());
	    getT().addY(12.3);
	}
	
	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addYDouble_ifi() {
	    Assume.assumeTrue(isIntCoordinates());
	    getT().addY(12.3);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addZDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().addZ(12.3);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addZDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().addZ(12.3);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDoubleT_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().scale(12.3, createTuple(1,2, 0));
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDoubleT_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().scale(12.3, createTuple(1,2, 0));
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().scale(12.3);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void scaleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().scale(12.3);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().set(12.3, 4.56, 0);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().set(12.3, 4.56, 0);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleArray_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().set(new double[] {12.3, 4.56, 0});
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void setDoubleArray_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().set(new double[] {12.3, 4.56, 0});
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void setXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().setX(12.3);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void setXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().setX(12.3);
	}
	
	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void setYDouble_ifi() {
	    Assume.assumeTrue(isIntCoordinates());
	    getT().setY(12.3);
	}
	
	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void setYDouble_iffp() {
	    Assume.assumeFalse(isIntCoordinates());
	    getT().setY(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setZDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().setZ(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void setZDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().setZ(12.3);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void subDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().sub(12.3, 4.56, 0);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void subDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().sub(12.3, 4.56, 0);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void subXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().subX(12.3);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void subXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().subX(12.3);
	}
	
	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void subYDouble_iffp() {
	    Assume.assumeFalse(isIntCoordinates());
	    getT().subY(12.3);
	}
	
	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void subYDouble_ifi() {
	    Assume.assumeTrue(isIntCoordinates());
	    getT().subY(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subZDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().subZ(12.3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void subZDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().subZ(12.3);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addPoint3DVector3D() {
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.add(point, vector1);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addVector3DPoint3D() {
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.add(vector1, point);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void addVector3D() {
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.add(vector1);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector3DPoint3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.scaleAdd(2.5, vector1, point);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector3DPoint3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.scaleAdd(2.5, vector1, point);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddIntVector3DPoint3D() {
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.scaleAdd(2, vector1, point);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddIntPoint3DVector3D() {
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.scaleAdd(2, point, vector1);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoublePoint3DVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.scaleAdd(2.5, point, vector1);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoublePoint3DVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.scaleAdd(2.5, point, vector1);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddIntVector3D() {
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.scaleAdd(2, vector1);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector3D_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.scaleAdd(2.5, vector1);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void scaleAddDoubleVector3D_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.scaleAdd(2.5, vector1);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void subPoint3DVector3D() {
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.sub(point, vector1);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void subVector3D() {
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.sub(vector1);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void operator_addVector3D() {
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.operator_add(vector1);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public final void operator_removeVector3D() {
		Point3D point = createTuple(1, 2, 0);
		Vector3D vector1 = createVector(0, 0, 0);
		point.operator_remove(vector1);
	}

}
