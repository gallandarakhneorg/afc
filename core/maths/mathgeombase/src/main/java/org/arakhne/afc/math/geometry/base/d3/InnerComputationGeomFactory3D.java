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

package org.arakhne.afc.math.geometry.base.d3;

/** Factory of immutable geometrical primitives.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public final class InnerComputationGeomFactory3D
		extends AbstractGeomFactory3D<InnerComputationVector3D, InnerComputationPoint3D, InnerComputationQuaternion> {

	/** Singleton of the factory.
	 */
	public static final InnerComputationGeomFactory3D SINGLETON = new InnerComputationGeomFactory3D();

	/** Construct, but prefer to use {@link #SINGLETON singleton}.
	 */
	public InnerComputationGeomFactory3D() {
		//
	}

	@Override
	public InnerComputationPoint3D convertToPoint(Point3D<?, ?, ?> pt) {
		if (pt instanceof InnerComputationPoint3D pt0) {
			return pt0;
		}
		final double x;
		final double y;
		final double z;
		if (pt == null) {
			x = 0;
			y = 0;
			z = 0;
		} else {
			x = pt.getX();
			y = pt.getY();
			z = pt.getZ();
		}
		return new InnerComputationPoint3D(x, y, z);
	}

	@Override
	public InnerComputationPoint3D convertToPoint(Vector3D<?, ?, ?> v) {
	    final double x;
	    final double y;
	    final double z;
	    if (v == null) {
	        x = 0;
	        y = 0;
	        z = 0;
	    } else {
	        x = v.getX();
	        y = v.getY();
	        z = v.getZ();
	    }
	    return new InnerComputationPoint3D(x, y, z);
	}

	@Override
	public InnerComputationVector3D convertToVector(Point3D<?, ?, ?> pt) {
	    final double x;
        final double y;
        final double z;
		if (pt == null) {
			x = 0;
			y = 0;
			z = 0;
		} else {
			x = pt.getX();
			y = pt.getY();
			z = pt.getZ();
		}
		return new InnerComputationVector3D(x, y, z);
	}

	@Override
	public InnerComputationVector3D convertToVector(Vector3D<?, ?, ?> vector) {
		if (vector instanceof InnerComputationVector3D vec0) {
			return vec0;
		}
		final double x;
        final double y;
        final double z;
		if (vector == null) {
			x = 0;
			y = 0;
			z = 0;
		} else {
			x = vector.getX();
			y = vector.getY();
			z = vector.getZ();
		}
		return new InnerComputationVector3D(x, y, z);
	}

	@Override
	public InnerComputationPoint3D newPoint() {
		return new InnerComputationPoint3D(0, 0, 0);
	}

	@Override
	public InnerComputationPoint3D newPoint(double x, double y, double z) {
	    return new InnerComputationPoint3D(x, y, z);
	}

	@Override
	public InnerComputationPoint3D newPoint(int x, int y, int z) {
	    return new InnerComputationPoint3D(x, y, z);
	}

	@Override
	public InnerComputationVector3D newVector() {
		return new InnerComputationVector3D(0, 0, 0);
	}

	@Override
	public InnerComputationVector3D newVector(double x, double y, double z) {
		return new InnerComputationVector3D(x, y, z);
	}

	@Override
	public InnerComputationVector3D newVector(int x, int y, int z) {
		return new InnerComputationVector3D(x, y, z);
	}

	@Override
	public InnerComputationQuaternion newQuaternion(double x, double y, double z, double w) {
		return new InnerComputationQuaternion(x, y, z, w);
	}

	@Override
	public InnerComputationQuaternion newQuaternion(int x, int y, int z, int w) {
		return new InnerComputationQuaternion(x, y, z, w);
	}

	@Override
	public InnerComputationQuaternion newQuaternionFromAxisAngle(double x, double y, double z, double angle) {
		final var comps = Quaternion.computeWithAxisAngle(x, y, z, angle);
		return new InnerComputationQuaternion(comps.x(), comps.y(), comps.z(), comps.w());
	}

	@Override
	public InnerComputationQuaternion newQuaternionFromAxisAngle(int x, int y, int z, int angle) {
		final var comps = Quaternion.computeWithAxisAngle(x, y, z, angle);
		return new InnerComputationQuaternion(comps.x(), comps.y(), comps.z(), comps.w());
	}

}
