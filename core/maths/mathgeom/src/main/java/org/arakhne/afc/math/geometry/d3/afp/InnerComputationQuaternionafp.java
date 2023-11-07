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

package org.arakhne.afc.math.geometry.d3.afp;

import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.UnmodifiableQuaternion;
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
public class InnerComputationQuaternionafp implements Quaternion<InnerComputationPoint3afp, InnerComputationVector3afp, InnerComputationQuaternionafp> {

	private static final long serialVersionUID = 5532810772531503141L;

	private double x;

	private double y;

	private double z;

	private double w;

	/** Construct the point.
	 */
	public InnerComputationQuaternionafp() {
		//
	}

	/** Construct the point.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @param z z coordinate.
	 * @param w w coordinate.
	 */
	public InnerComputationQuaternionafp(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	@Override
	public InnerComputationGeomFactory3afp getGeomFactory() {
		return InnerComputationGeomFactory3afp.SINGLETON;
	}

	@Pure
	@Override
	public String toString() {
		final JsonBuffer objectDescription = new JsonBuffer();
		toJson(objectDescription);
        return objectDescription.toString();
	}

	@Override
	public InnerComputationQuaternionafp clone() {
	    try {
            return (InnerComputationQuaternionafp) super.clone();
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
	public UnmodifiableQuaternion<InnerComputationPoint3afp, InnerComputationVector3afp, InnerComputationQuaternionafp> toUnmodifiable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void conjugate(Quaternion<?, ?, ?> q) {
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
	public void inverse(Quaternion<?, ?, ?> q) {
		final double x = q.getX();
		final double y = q.getY();
		final double z = q.getZ();
		final double w = q.getW();
		final double norm = 1. / (w * w + x * x + y * y + z * z);
		this.w =  norm * w;
		this.x = -norm * x;
		this.y = -norm * y;
		this.z = -norm * z;
	}

	@Override
	public void normalize(Quaternion<?, ?, ?> q) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void set(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	@Override
	public void interpolate(Quaternion<?, ?, ?> q, double alpha) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void interpolate(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2, double alpha) {
		throw new UnsupportedOperationException();
	}

}
