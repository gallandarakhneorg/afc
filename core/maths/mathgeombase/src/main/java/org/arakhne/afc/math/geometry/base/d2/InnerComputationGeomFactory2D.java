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

package org.arakhne.afc.math.geometry.base.d2;

/** Factory of immutable geometrical primitives.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public final class InnerComputationGeomFactory2D extends AbstractGeomFactory2D<InnerComputationVector2D, InnerComputationPoint2D> {

	/** Singleton of the factory.
	 */
	public static final InnerComputationGeomFactory2D SINGLETON = new InnerComputationGeomFactory2D();

	/** Constructor, but prefer to use the {@link #SINGLETON singleton}.
	 */
	public InnerComputationGeomFactory2D() {
		//
	}

	@Override
	public InnerComputationPoint2D convertToPoint(Point2D<?, ?> pt) {
		if (pt instanceof InnerComputationPoint2D pts) {
			return pts;
		}
		final double x;
		final double y;
		if (pt == null) {
			x = 0;
			y = 0;
		} else {
			x = pt.getX();
			y = pt.getY();
		}
		return new InnerComputationPoint2D(x, y);
	}

	@Override
	public InnerComputationPoint2D convertToPoint(Vector2D<?, ?> v) {
		final double x;
		final double y;
		if (v == null) {
			x = 0;
			y = 0;
		} else {
			x = v.getX();
			y = v.getY();
		}
		return new InnerComputationPoint2D(x, y);
	}

	@Override
	public InnerComputationVector2D convertToVector(Point2D<?, ?> pt) {
		final double x;
		final double y;
		if (pt == null) {
			x = 0;
			y = 0;
		} else {
			x = pt.getX();
			y = pt.getY();
		}
		return new InnerComputationVector2D(x, y);
	}

	@Override
	public InnerComputationVector2D convertToVector(Vector2D<?, ?> v) {
		if (v instanceof InnerComputationVector2D v0) {
			return v0;
		}
		final double x;
		final double y;
		if (v == null) {
			x = 0;
			y = 0;
		} else {
			x = v.getX();
			y = v.getY();
		}
		return new InnerComputationVector2D(x, y);
	}

	@Override
	public InnerComputationPoint2D newPoint() {
		return new InnerComputationPoint2D(0, 0);
	}

	@Override
	public InnerComputationPoint2D newPoint(double x, double y) {
		return new InnerComputationPoint2D(x, y);
	}

	@Override
	public InnerComputationPoint2D newPoint(int x, int y) {
		return new InnerComputationPoint2D(x, y);
	}

	@Override
	public InnerComputationVector2D newVector() {
		return new InnerComputationVector2D(0, 0);
	}

	@Override
	public InnerComputationVector2D newVector(double x, double y) {
		return new InnerComputationVector2D(x, y);
	}

	@Override
	public InnerComputationVector2D newVector(int x, int y) {
		return new InnerComputationVector2D(x, y);
	}

}
