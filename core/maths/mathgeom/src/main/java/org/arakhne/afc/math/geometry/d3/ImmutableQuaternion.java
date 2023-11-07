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

package org.arakhne.afc.math.geometry.d3;

import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Immutable quaternion.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public final class ImmutableQuaternion implements UnmodifiableQuaternion<ImmutablePoint3D, ImmutableVector3D, ImmutableQuaternion> {

	private static final long serialVersionUID = -8128913884006426535L;

	private final double x;

	private final double y;

	private final double z;

	private final double w;

	/** Constructor.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @param z z coordinate.
	 * @param w w coordinate.
	 */
	public ImmutableQuaternion(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/** Constructor.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @param z z coordinate.
	 * @param w w coordinate.
	 */
	public ImmutableQuaternion(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/** Constructor.
	 * @param axisangle the rotation specification with an axis and a rotation angle around this axis.
	 */
	public ImmutableQuaternion(AxisAngle axisangle) {
		assert axisangle != null;
		final QuaternionComponents comps = Quaternion.computeWithAxisAngle(axisangle.x(), axisangle.y(), axisangle.z(), axisangle.angle());
		this.x = comps.x();
		this.y = comps.y();
		this.z = comps.z();
		this.w = comps.w();
	}

	@Pure
	@Override
	public boolean equals(Object object) {
		try {
			final Quaternion<?, ?, ?> tuple = (Quaternion<?, ?, ?>) object;
			return tuple.getX() == getX() && tuple.getY() == getY() && tuple.getZ() == getZ ()&& tuple.getW() == getW();
        } catch (AssertionError e) {
            throw e;
        } catch (Throwable e2) {
            return false;
        }
	}

	@Override
	public GeomFactory3D<ImmutableVector3D, ImmutablePoint3D, ImmutableQuaternion> getGeomFactory() {
		return ImmutableGeomFactory3D.SINGLETON;
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.x);
		bits = 31 * bits + Double.doubleToLongBits(this.y);
		bits = 31 * bits + Double.doubleToLongBits(this.z);
		bits = 31 * bits + Double.doubleToLongBits(this.w);
		final int b = (int) bits;
		return b ^ (b >> 31);
	}

	@Pure
	@Override
	public String toString() {
		final JsonBuffer objectDescription = new JsonBuffer();
		toJson(objectDescription);
        return objectDescription.toString();
	}

	@Override
	public ImmutableQuaternion clone() {
		try {
			return (ImmutableQuaternion) super.clone();
		} catch (CloneNotSupportedException exception) {
			throw new InternalError(exception);
		}
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
	public double getY() {
		return this.y;
	}

	@Override
	public int iy() {
		return (int) this.y;
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
	public double getW() {
		return this.w;
	}

	@Override
	public int iw() {
		return (int) this.w;
	}

	@Override
	public UnmodifiableQuaternion<ImmutablePoint3D, ImmutableVector3D, ImmutableQuaternion> toUnmodifiable() {
		return this;
	}

}
