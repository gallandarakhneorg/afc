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

package org.arakhne.afc.math.geometry.d3.ai;

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.UnmodifiableQuaternion;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.eclipse.xtext.xbase.lib.Pure;

/** A point that is used for internal computations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class InnerComputationQuaternionai implements Quaternion<InnerComputationPoint3ai, InnerComputationVector3ai, InnerComputationQuaternionai> {

	private static final long serialVersionUID = 1150448862627564407L;

	private int x;

	private int y;

	private int z;

	private int w;

	/** Construct point.
     */
	public InnerComputationQuaternionai() {
		//
	}

	/** Construct point.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     * @param w w coordinate of the point.
     */
	public InnerComputationQuaternionai(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/** Construct point.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     * @param w w coordinate of the point.
     */
	public InnerComputationQuaternionai(double x, double y, double z, double w) {
		this.x = (int) Math.round(x);
		this.y = (int) Math.round(y);
		this.z = (int) Math.round(z);
		this.w = (int) Math.round(w);
	}

	@Override
	public InnerComputationQuaternionai clone() {
	    try {
            return (InnerComputationQuaternionai) super.clone();
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
		return this.x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setX(double x) {
		this.x = (int) Math.round(x);
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public int iy() {
		return this.y;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void setY(double y) {
		this.y = (int) Math.round(y);
	}

	@Override
	public double getZ() {
		return this.z;
	}

	@Override
	public int iz() {
		return this.z;
	}

	@Override
	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public void setZ(double z) {
		this.z = (int) Math.round(z);
	}

	@Override
	public double getW() {
		return this.w;
	}

	@Override
	public int iw() {
		return this.w;
	}

	@Override
	public void setW(int w) {
		this.w = w;
	}

	@Override
	public void setW(double w) {
		this.w = (int) Math.round(w);
	}

	@Override
	public UnmodifiableQuaternion<InnerComputationPoint3ai, InnerComputationVector3ai, InnerComputationQuaternionai> toUnmodifiable() {
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
	public void conjugate(Quaternion<?, ?, ?> q) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void conjugate() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void mul(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void mul(Quaternion<?, ?, ?> q) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void mulInverse(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void mulInverse(Quaternion<?, ?, ?> q) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void inverse(Quaternion<?, ?, ?> q) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void inverse() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void normalize(Quaternion<?, ?, ?> q) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void normalize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void set(double x, double y, double z, double w) {
		this.x = (int) Math.round(x);
		this.y = (int) Math.round(y);
		this.z = (int) Math.round(z);
		this.w = (int) Math.round(w);
	}

	@Override
	public void interpolate(Quaternion<?, ?, ?> q, double alpha) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void interpolate(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2, double alpha) {
		throw new UnsupportedOperationException();
	}

	@Override
	public GeomFactory3D<InnerComputationVector3ai, InnerComputationPoint3ai, InnerComputationQuaternionai> getGeomFactory() {
		return InnerComputationGeomFactory.SINGLETON;
	}

}
