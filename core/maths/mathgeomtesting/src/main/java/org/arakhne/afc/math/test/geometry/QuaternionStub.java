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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiableQuaternion;
import org.arakhne.afc.math.geometry.d3.UnmodifiableVector3D;
import org.arakhne.afc.vmutil.json.JsonBuffer;

@SuppressWarnings("all")
public final class QuaternionStub implements UnmodifiableQuaternion<Point3DStub, Vector3DStub, QuaternionStub> {

	private double x;
	
	private double y;
	
	private double z;
	
	private double w;
	
	/**
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @param z z coordinate.
	 * @param w w coordinate.
	 */
	public QuaternionStub(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Quaternion<?, ?, ?>) {
			Quaternion<?, ?, ?> v = (Quaternion<?, ?, ?>) obj;
			return v.getX() == getX() && v.getY() == getY() && v.getZ() == getZ() && v.getW() == getW();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.x);
		bits = 31 * bits + Double.doubleToLongBits(this.y);
		bits = 31 * bits + Double.doubleToLongBits(this.z);
		bits = 31 * bits + Double.doubleToLongBits(this.w);
		int b = (int) bits;
		return b ^ (b >> 32);
	}

	@Override
	public QuaternionStub clone() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public int ix() {
		return (int) this.x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}
	
	@Override
	public double getY() {
		return this.y;
	}
	
	@Override
	public int iy() {
		return (int) this.y;
	}
	
	@Override
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public double getZ() {
		return this.z;
	}

	@Override
	public int iz() {
		return (int) this.z;
	}

	@Override
	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public void setZ(double z) {
		this.z = z;
	}

	@Override
	public double getW() {
		return this.w;
	}

	@Override
	public int iw() {
		return (int) this.w;
	}

	@Override
	public void setW(int w) {
		this.w = w;
	}

	@Override
	public void setW(double w) {
		this.w = w;
	}

	@Override
	public UnmodifiableQuaternion<Point3DStub, Vector3DStub, QuaternionStub> toUnmodifiable() {
		throw new UnsupportedOperationException();
	}
	
	@Pure
	@Override
	public String toString() {
		final JsonBuffer objectDescription = new JsonBuffer();
		toJson(objectDescription);
        return objectDescription.toString();
	}

	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add("x", getX()); //$NON-NLS-1$
		buffer.add("y", getY()); //$NON-NLS-1$
		buffer.add("z", getZ()); //$NON-NLS-1$
		buffer.add("w", getW()); //$NON-NLS-1$
	}

	@Override
	public GeomFactory3D<Vector3DStub, Point3DStub, QuaternionStub> getGeomFactory() {
		return new GeomFactory3DStub();
	}

}
