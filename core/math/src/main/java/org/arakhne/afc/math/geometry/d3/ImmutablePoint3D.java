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

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Immutable point 3D.
 *
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
final class ImmutablePoint3D implements UnmodifiablePoint3D<ImmutablePoint3D, ImmutableVector3D> {

	private static final long serialVersionUID = 407348048685709808L;

	private final double x;

	private final double y;

	private final double z;


	/**
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @param z z coordinate.
	 */
	ImmutablePoint3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @param z z coordinate.
	 */
	ImmutablePoint3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		try {
			final Tuple3D<?> tuple = (Tuple3D<?>) obj;
			return tuple.getX() == getX() && tuple.getY() == getY() && tuple.getZ() == getZ();
        } catch (AssertionError e) {
            throw e;
        } catch (Throwable e1) {
            return false;
        }
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.x);
		bits = 31 * bits + Double.doubleToLongBits(this.y);
		bits = 31 * bits + Double.doubleToLongBits(this.z);
		final int b = (int) bits;
		return b ^ (b >> 31);
	}

	@Pure
	@Override
	public String toString() {
        return Tuple3D.toString(this.x, this.y, this.z);
	}

	@Pure
	@Override
	public ImmutablePoint3D clone() {
		try {
			return (ImmutablePoint3D) super.clone();
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
	public GeomFactory3D<ImmutableVector3D, ImmutablePoint3D> getGeomFactory() {
		return ImmutableGeomFactory3D.SINGLETON;
	}
}
