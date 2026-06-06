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

package org.arakhne.afc.math.geometry.base.d3;

import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.eclipse.xtext.xbase.lib.Pure;

/** A quaternion that is used for internal computations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public class InnerComputationQuaternion implements Quaternion<InnerComputationPoint3D, InnerComputationVector3D, InnerComputationQuaternion> {

	private static final long serialVersionUID = 3386613801312202167L;

	private double x;

	private double y;

	private double z;

	private double w;

	/** Construct vector.
	 */
	public InnerComputationQuaternion() {
		//
	}

	/** Construct vector.
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 * @param w w coordinate
	 */
	public InnerComputationQuaternion(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/** Construct the quaternion.
	 * @param quaternion the quaternion to copy.
	 */
	public InnerComputationQuaternion(Quaternion<?, ?, ?> quaternion) {
		this.x = quaternion.getX();
		this.y = quaternion.getY();
		this.z = quaternion.getZ();
		this.w = quaternion.getW();
	}

	@Override
	public boolean equals(Object obj) {
		try {
			return equals((Quaternion<?, ?, ?>) obj);
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable e2) {
			return false;
		}
	}

	@Pure
	@Override
	public int hashCode() {
		var bits = 1L;
		bits = 31 * bits + Double.hashCode(this.x);
		bits = 31 * bits + Double.hashCode(this.y);
		bits = 31 * bits + Double.hashCode(this.z);
		bits = 31 * bits + Double.hashCode(this.w);
		return (int) (bits ^ (bits >> 31));
	}

	@Pure
	@Override
	public String toString() {
		final var objectDescription = new JsonBuffer();
		toJson(objectDescription);
        return objectDescription.toString();
	}

	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add("x", Double.valueOf(getX())); //$NON-NLS-1$
		buffer.add("y", Double.valueOf(getY())); //$NON-NLS-1$
		buffer.add("z", Double.valueOf(getZ())); //$NON-NLS-1$
		buffer.add("w", Double.valueOf(getW())); //$NON-NLS-1$
	}

	@Override
	public InnerComputationQuaternion clone() {
		try {
			return (InnerComputationQuaternion) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public int ix() {
		return (int) Math.round(this.x);
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
		return (int) Math.round(this.y);
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
		return (int) Math.round(this.z);
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
		return (int) Math.round(this.w);
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
	public void set(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	@Override
	public void conjugate(Quaternion<?, ?, ?> quaternion) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void mul(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void mulInverse(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void inverse(Quaternion<?, ?, ?> quaternion) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void normalize(Quaternion<?, ?, ?> quaternion) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void interpolate(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2, double alpha) {
		throw new UnsupportedOperationException();
	}

	@Override
	public UnmodifiableQuaternion<InnerComputationPoint3D, InnerComputationVector3D, InnerComputationQuaternion> toUnmodifiable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public GeomFactory3D<InnerComputationVector3D, InnerComputationPoint3D, InnerComputationQuaternion> getGeomFactory() {
		throw new UnsupportedOperationException();
	}

}
