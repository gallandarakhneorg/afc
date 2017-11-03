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

package org.arakhne.afc.math.geometry.d1.afp;

import org.arakhne.afc.math.geometry.d1.GeomFactory1D;
import org.arakhne.afc.math.geometry.d1.Point1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.Vector1D;

/** Factory of immutable geometrical primitives.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
final class InnerComputationGeomFactory implements GeomFactory1D<InnerComputationVector1afp, InnerComputationPoint1afp> {

	/** Singleton of the factory.
	 */
	public static final InnerComputationGeomFactory SINGLETON = new InnerComputationGeomFactory();

	private InnerComputationGeomFactory() {
		//
	}

	@Override
	public InnerComputationPoint1afp convertToPoint(Point1D<?, ?, ?> pt) {
		if (pt instanceof InnerComputationPoint1afp) {
			return (InnerComputationPoint1afp) pt;
		}
		final double x;
		final double y;
		x = pt.getX();
		y = pt.getY();
		return new InnerComputationPoint1afp(pt.getSegment(), x, y);
	}

	@Override
	public InnerComputationPoint1afp convertToPoint(Vector1D<?, ?, ?> v) {
		final double x;
		final double y;
		x = v.getX();
		y = v.getY();
		return new InnerComputationPoint1afp(v.getSegment(), x, y);
	}

	@Override
	public InnerComputationVector1afp convertToVector(Point1D<?, ?, ?> pt) {
		final double x;
		final double y;
		x = pt.getX();
		y = pt.getY();
		return new InnerComputationVector1afp(pt.getSegment(), x, y);
	}

	@Override
	public InnerComputationVector1afp convertToVector(Vector1D<?, ?, ?> v) {
		if (v instanceof InnerComputationVector1afp) {
			return (InnerComputationVector1afp) v;
		}
		final double x;
		final double y;
		x = v.getX();
		y = v.getY();
		return new InnerComputationVector1afp(v.getSegment(), x, y);
	}

	@Override
	public InnerComputationPoint1afp newPoint(Segment1D<?, ?> segment) {
		return new InnerComputationPoint1afp(segment, 0, 0);
	}

	@Override
	public InnerComputationPoint1afp newPoint(Segment1D<?, ?> segment, double x, double y) {
		return new InnerComputationPoint1afp(segment, x, y);
	}

	@Override
	public InnerComputationPoint1afp newPoint(Segment1D<?, ?> segment, int x, int y) {
		return new InnerComputationPoint1afp(segment, x, y);
	}

	@Override
	public InnerComputationVector1afp newVector(Segment1D<?, ?> segment) {
		return new InnerComputationVector1afp(segment, 0, 0);
	}

	@Override
	public InnerComputationVector1afp newVector(Segment1D<?, ?> segment, double x, double y) {
		return new InnerComputationVector1afp(segment, x, y);
	}

	@Override
	public InnerComputationVector1afp newVector(Segment1D<?, ?> segment, int x, int y) {
		return new InnerComputationVector1afp(segment, x, y);
	}

}
