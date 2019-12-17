/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.afp;

import org.arakhne.afc.math.geometry.d2.AbstractGeomFactory2D;
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
final class InnerComputationGeomFactory extends AbstractGeomFactory2D<InnerComputationVector2afp, InnerComputationPoint2afp> {

	/** Singleton of the factory.
	 */
	public static final InnerComputationGeomFactory SINGLETON = new InnerComputationGeomFactory();

	private InnerComputationGeomFactory() {
		//
	}

	@Override
	public InnerComputationPoint2afp convertToPoint(Point2D<?, ?> pt) {
		if (pt instanceof InnerComputationPoint2afp) {
			return (InnerComputationPoint2afp) pt;
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
		return new InnerComputationPoint2afp(x, y);
	}

	@Override
	public InnerComputationPoint2afp convertToPoint(Vector2D<?, ?> v) {
		final double x;
		final double y;
		if (v == null) {
			x = 0;
			y = 0;
		} else {
			x = v.getX();
			y = v.getY();
		}
		return new InnerComputationPoint2afp(x, y);
	}

	@Override
	public InnerComputationVector2afp convertToVector(Point2D<?, ?> pt) {
		final double x;
		final double y;
		if (pt == null) {
			x = 0;
			y = 0;
		} else {
			x = pt.getX();
			y = pt.getY();
		}
		return new InnerComputationVector2afp(x, y);
	}

	@Override
	public InnerComputationVector2afp convertToVector(Vector2D<?, ?> v) {
		if (v instanceof InnerComputationVector2afp) {
			return (InnerComputationVector2afp) v;
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
		return new InnerComputationVector2afp(x, y);
	}

	@Override
	public InnerComputationPoint2afp newPoint() {
		return new InnerComputationPoint2afp(0, 0);
	}

	@Override
	public InnerComputationPoint2afp newPoint(double x, double y) {
		return new InnerComputationPoint2afp(x, y);
	}

	@Override
	public InnerComputationPoint2afp newPoint(int x, int y) {
		return new InnerComputationPoint2afp(x, y);
	}

	@Override
	public InnerComputationVector2afp newVector() {
		return new InnerComputationVector2afp(0, 0);
	}

	@Override
	public InnerComputationVector2afp newVector(double x, double y) {
		return new InnerComputationVector2afp(x, y);
	}

	@Override
	public InnerComputationVector2afp newVector(int x, int y) {
		return new InnerComputationVector2afp(x, y);
	}

}
