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

/** Factory of immutable geometrical primitives.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class InnerComputationGeomFactory1D extends AbstractGeomFactory1D<InnerComputationVector1D, InnerComputationPoint1D> {

	/** Singleton of the factory.
	 */
	public static final InnerComputationGeomFactory1D SINGLETON = new InnerComputationGeomFactory1D();

	/** Constructor, but prefer to use the {@link #SINGLETON singleton}.
	 */
	public InnerComputationGeomFactory1D() {
		//
	}

	@Override
	public InnerComputationPoint1D convertToPoint(Point1D<?, ?, ?> pt) {
		if (pt instanceof InnerComputationPoint1D pts) {
			return pts;
		}
		final var x = pt.getX();
		final var y = pt.getY();
		return new InnerComputationPoint1D(pt.getSegment(), x, y);
	}

	@Override
	public InnerComputationPoint1D convertToPoint(Vector1D<?, ?, ?> v) {
		final var x = v.getX();
		final var y = v.getY();
		return new InnerComputationPoint1D(v.getSegment(), x, y);
	}

	@Override
	public InnerComputationVector1D convertToVector(Point1D<?, ?, ?> pt) {
		final var x = pt.getX();
		final var y = pt.getY();
		return new InnerComputationVector1D(pt.getSegment(), x, y);
	}

	@Override
	public InnerComputationVector1D convertToVector(Vector1D<?, ?, ?> v) {
		if (v instanceof InnerComputationVector1D pts) {
			return pts;
		}
		final var x = v.getX();
		final var y = v.getY();
		return new InnerComputationVector1D(v.getSegment(), x, y);
	}

	@Override
	public InnerComputationPoint1D newPoint(Segment1D<?, ?> segment) {
		return new InnerComputationPoint1D(segment, 0, 0);
	}

	@Override
	public InnerComputationPoint1D newPoint(Segment1D<?, ?> segment, double x, double y) {
		return new InnerComputationPoint1D(segment, x, y);
	}

	@Override
	public InnerComputationPoint1D newPoint(Segment1D<?, ?> segment, int x, int y) {
		return new InnerComputationPoint1D(segment, x, y);
	}

	@Override
	public InnerComputationVector1D newVector(Segment1D<?, ?> segment) {
		return new InnerComputationVector1D(segment, 0, 0);
	}

	@Override
	public InnerComputationVector1D newVector(Segment1D<?, ?> segment, double x, double y) {
		return new InnerComputationVector1D(segment, x, y);
	}

	@Override
	public InnerComputationVector1D newVector(Segment1D<?, ?> segment, int x, int y) {
		return new InnerComputationVector1D(segment, x, y);
	}

}
