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
		final var x = quaternion.getX();
		final var y = quaternion.getY();
		final var z = quaternion.getZ();
		final var w = quaternion.getW();
		this.x = -x;
		this.y = -y;
		this.z = -z;
		this.w = w;
	}

	@Override
	public void mul(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2) {
		final var x1 = q1.getX();
		final var y1 = q1.getY();
		final var z1 = q1.getZ();
		final var w1 = q1.getW();
		final var x2 = q2.getX();
		final var y2 = q2.getY();
		final var z2 = q2.getZ();
		final var w2 = q2.getW();
		this.w = w1 * w2 - x1 * x2 - y1 * y2 - z1 * z2;
		this.x = w1 * x2 + w2 * x1 + y1 * z2 - z1 * y2;
		this.y = w1 * y2 + w2 * y1 - x1 * z2 + z1 * x2;
		this.z = w1 * z2 + w2 * z1 + x1 * y2 - y1 * x2;
	}

	@Override
	public void mulInverse(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2) {
		final var tempQuat = new InnerComputationQuaternion(
				q2.getX(), q2.getY(), q2.getZ(), q2.getW());
		tempQuat.inverse();
		mul(q1, tempQuat);
	}

	@Override
	public void inverse(Quaternion<?, ?, ?> quaternion) {
		final var x = quaternion.getX();
		final var y = quaternion.getY();
		final var z = quaternion.getZ();
		final var w = quaternion.getW();
		final var norm = 1. / (w * w + x * x + y * y + z * z);
		this.w =  norm * w;
		this.x = -norm * x;
		this.y = -norm * y;
		this.z = -norm * z;
	}

	@Override
	public void normalize(Quaternion<?, ?, ?> quaternion) {
		var norm = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
		if (norm > 0.) {
			norm = 1. / Math.sqrt(norm);
			this.x *= norm;
			this.y *= norm;
			this.z *= norm;
			this.w *= norm;
		} else {
			this.x = 0.;
			this.y = 0.;
			this.z = 0.;
			this.w = 0.;
		}
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
