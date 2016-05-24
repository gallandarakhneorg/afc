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

package org.arakhne.afc.math.geometry.d2.ai;

import org.arakhne.afc.math.geometry.d2.GeomFactory;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** Factory of immutable geometrical primitives.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
final class InnerComputationGeomFactory implements GeomFactory<InnerComputationVector2ai, InnerComputationPoint2ai> {

	/** Singleton of the factory.
	 */
	public static final InnerComputationGeomFactory SINGLETON = new InnerComputationGeomFactory();

	private InnerComputationGeomFactory() {
		//
	}

	@Override
	public InnerComputationPoint2ai convertToPoint(Point2D<?, ?> pt) {
		if (pt instanceof InnerComputationPoint2ai) {
			return (InnerComputationPoint2ai) pt;
		}
		final int x;
		final int y;
		if (pt == null) {
			x = 0;
			y = 0;
		} else {
			x = pt.ix();
			y = pt.iy();
		}
		return new InnerComputationPoint2ai(x, y);
	}

	@Override
	public InnerComputationPoint2ai convertToPoint(Vector2D<?, ?> v) {
		final int x;
		final int y;
		if (v == null) {
			x = 0;
			y = 0;
		} else {
			x = v.ix();
			y = v.iy();
		}
		return new InnerComputationPoint2ai(x, y);
	}

	@Override
	public InnerComputationVector2ai convertToVector(Point2D<?, ?> pt) {
		final int x;
		final int y;
		if (pt == null) {
			x = 0;
			y = 0;
		} else {
			x = pt.ix();
			y = pt.iy();
		}
		return new InnerComputationVector2ai(x, y);
	}

	@Override
	public InnerComputationVector2ai convertToVector(Vector2D<?, ?> v) {
		if (v instanceof InnerComputationVector2ai) {
			return (InnerComputationVector2ai) v;
		}
		final int x;
		final int y;
		if (v == null) {
			x = 0;
			y = 0;
		} else {
			x = v.ix();
			y = v.iy();
		}
		return new InnerComputationVector2ai(x, y);
	}

	@Override
	public InnerComputationPoint2ai newPoint() {
		return new InnerComputationPoint2ai(0, 0);
	}

	@Override
	public InnerComputationPoint2ai newPoint(double x, double y) {
		return new InnerComputationPoint2ai((int) Math.round(x), (int) Math.round(y));
	}

	@Override
	public InnerComputationPoint2ai newPoint(int x, int y) {
		return new InnerComputationPoint2ai(x, y);
	}

	@Override
	public InnerComputationVector2ai newVector() {
		return new InnerComputationVector2ai(0, 0);
	}

	@Override
	public InnerComputationVector2ai newVector(double x, double y) {
		return new InnerComputationVector2ai((int) Math.round(x), (int) Math.round(y));
	}

	@Override
	public InnerComputationVector2ai newVector(int x, int y) {
		return new InnerComputationVector2ai(x, y);
	}

}
