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

package org.arakhne.afc.math.geometry.d1;

import java.lang.ref.WeakReference;
import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Tuple2D;

/**
 * Immutable vector 1.5D.
 *
 * @param <S> the type of the segment.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class ImmutableVector1D<S extends Segment1D<?, ?>>
	implements UnmodifiableVector1D<ImmutableVector1D<S>, ImmutablePoint1D<S>, S> {

	private static final long serialVersionUID = 7547438426195490640L;

	private final WeakReference<S> segment;

	private final double x;

	private final double y;

	/**
	 * @param segment the segment.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public ImmutableVector1D(S segment, double x, double y) {
		this.segment = new WeakReference<>(segment);
		this.x = x;
		this.y = y;
	}

	/**
	 * @param segment the segment.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public ImmutableVector1D(S segment, int x, int y) {
		this.segment = new WeakReference<>(segment);
		this.x = x;
		this.y = y;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImmutableGeomFactory<S> getGeomFactory() {
		return ImmutableGeomFactory.SINGLETON;
	}

	@Override
	public S getSegment() {
		return this.segment.get();
	}

	@Pure
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (object instanceof Vector1D<?, ?, ?>) {
			final Point1D<?, ?, ?> tuple = (Point1D<?, ?, ?>) object;
			return tuple.getSegment() == getSegment() && tuple.getX() == getX() && tuple.getY() == getY();
		}
		if (object instanceof Point1D<?, ?, ?>) {
			final Point1D<?, ?, ?> tuple = (Point1D<?, ?, ?>) object;
			return tuple.getSegment() == getSegment() && tuple.getX() == getX() && tuple.getY() == getY();
		}
		if (object instanceof Tuple2D<?>) {
			final Tuple2D<?> tuple = (Tuple2D<?>) object;
			return tuple.getX() == getX() && tuple.getY() == getY();
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Objects.hashCode(getSegment());
		bits = 31 * bits + Double.hashCode(getX());
		bits = 31 * bits + Double.hashCode(getY());
		final int b = (int) bits;
		return b ^ (b >> 31);
	}

	@Pure
	@Override
	public String toString() {
		return Point1D.toString(getSegment(), getX(), getY());
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public ImmutableVector1D<S> clone() {
		try {
			return (ImmutableVector1D<S>) super.clone();
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

}
