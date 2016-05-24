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

package org.arakhne.afc.math.geometry.d2;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Immutable vector 2D.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public final class ImmutableVector2D implements UnmodifiableVector2D<ImmutableVector2D, ImmutablePoint2D> {

	private static final long serialVersionUID = 1590949485248939642L;

	private final double x;

	private final double y;

	/**
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public ImmutableVector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public ImmutableVector2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Pure
	@Override
	public boolean equals(Object object) {
		try {
			final Tuple2D<?> tuple = (Tuple2D<?>) object;
			return tuple.getX() == getX() && tuple.getY() == getY();
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable e2) {
			return false;
		}
	}

	@Override
	public GeomFactory<ImmutableVector2D, ImmutablePoint2D> getGeomFactory() {
		return ImmutableGeomFactory.SINGLETON;
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.x);
		bits = 31 * bits + Double.doubleToLongBits(this.y);
		final int b = (int) bits;
		return b ^ (b >> 32);
	}

	@Pure
	@Override
	public String toString() {
		return "(" //$NON-NLS-1$
				+ this.x
				+ ";" //$NON-NLS-1$
				+ this.y
				+ ")"; //$NON-NLS-1$
	}

	@Override
	public ImmutableVector2D clone() {
		try {
			return (ImmutableVector2D) super.clone();
		} catch (CloneNotSupportedException exception) {
			throw new InternalError(exception);
		}
	}

	@Pure
	@Override
	public double getX() {
		return this.x;
	}

	@Pure
	@Override
	public int ix() {
		return (int) this.x;
	}

	@Pure
	@Override
	public double getY() {
		return this.y;
	}

	@Pure
	@Override
	public int iy() {
		return (int) this.y;
	}

	@Pure
	@Override
	public ImmutableVector2D toUnitVector() {
		final double length = getLength();
		if (length == 0.) {
			return new ImmutableVector2D(0, 0);
		}
		return new ImmutableVector2D(getX() / length, getY() / length);
	}

	@Pure
	@Override
	public ImmutableVector2D toOrthogonalVector() {
		return new ImmutableVector2D(-getY(), getX());
	}

}
