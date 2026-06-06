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

package org.arakhne.afc.math.geometry.base.d1;

import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Factory of immutable geometrical primitives.
 *
 * @param <S> is the type of the segments.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class ImmutableGeomFactory1D<S extends Segment1D<?, ?>>
	extends AbstractGeomFactory1D<ImmutableVector1D<S>, ImmutablePoint1D<S>> {

	/** Singleton of the factory.
	 */
	@SuppressWarnings("rawtypes")
	public static final ImmutableGeomFactory1D SINGLETON = new ImmutableGeomFactory1D();

	/** Constructor, but prefer to use the {@link #SINGLETON singleton}.
	 */
	public ImmutableGeomFactory1D() {
		//
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImmutablePoint1D<S> convertToPoint(Point1D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		if (pt instanceof ImmutablePoint1D pts) {
			return pts;
		}
		final double x;
		final double y;
		x = pt.getX();
		y = pt.getY();
		return new ImmutablePoint1D<>((S) pt.getSegment(), x, y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImmutablePoint1D<S> convertToPoint(Vector1D<?, ?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		final var x = v.getX();
		final var y = v.getY();
		return new ImmutablePoint1D<>((S) v.getSegment(), x, y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImmutableVector1D<S> convertToVector(Point1D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final var x = pt.getX();
		final var y = pt.getY();
		return new ImmutableVector1D<>((S) pt.getSegment(), x, y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImmutableVector1D<S> convertToVector(Vector1D<?, ?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		if (v instanceof ImmutableVector1D pts) {
			return pts;
		}
		final var x = v.getX();
		final var y = v.getY();
		return new ImmutableVector1D<>((S) v.getSegment(), x, y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImmutablePoint1D<S> newPoint(Segment1D<?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		return new ImmutablePoint1D<>((S) segment, 0, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImmutablePoint1D<S> newPoint(Segment1D<?, ?> segment, double x, double y) {
		assert segment != null : AssertMessages.notNullParameter();
		return new ImmutablePoint1D<>((S) segment, x, y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImmutablePoint1D<S> newPoint(Segment1D<?, ?> segment, int x, int y) {
		assert segment != null : AssertMessages.notNullParameter();
		return new ImmutablePoint1D<>((S) segment, x, y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImmutableVector1D<S> newVector(Segment1D<?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		return new ImmutableVector1D<>((S) segment, 0, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImmutableVector1D<S> newVector(Segment1D<?, ?> segment, double x, double y) {
		assert segment != null : AssertMessages.notNullParameter();
		return new ImmutableVector1D<>((S) segment, x, y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImmutableVector1D<S> newVector(Segment1D<?, ?> segment, int x, int y) {
		assert segment != null : AssertMessages.notNullParameter();
		return new ImmutableVector1D<>((S) segment, x, y);
	}

}
