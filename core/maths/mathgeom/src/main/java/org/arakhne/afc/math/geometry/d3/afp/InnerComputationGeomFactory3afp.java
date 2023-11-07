/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d3.afp;

import org.arakhne.afc.math.geometry.d3.AbstractGeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Quaternion.QuaternionComponents;
import org.arakhne.afc.math.geometry.d3.Vector3D;

/** Factory of immutable geometrical primitives.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public final class InnerComputationGeomFactory3afp extends AbstractGeomFactory3D<InnerComputationVector3afp, InnerComputationPoint3afp, InnerComputationQuaternionafp> {

	/** Singleton of the factory.
	 */
	public static final InnerComputationGeomFactory3afp SINGLETON = new InnerComputationGeomFactory3afp();

	private InnerComputationGeomFactory3afp() {
		//
	}

	@Override
	public InnerComputationPoint3afp convertToPoint(Point3D<?, ?, ?> pt) {
		if (pt instanceof InnerComputationPoint3afp) {
			return (InnerComputationPoint3afp) pt;
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
		return new InnerComputationPoint3afp(x, y, z);
	}

	@Override
	public InnerComputationPoint3afp convertToPoint(Vector3D<?, ?, ?> v) {
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
	    return new InnerComputationPoint3afp(x, y, z);
	}

	@Override
	public InnerComputationVector3afp convertToVector(Point3D<?, ?, ?> pt) {
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
		return new InnerComputationVector3afp(x, y, z);
	}

	@Override
	public InnerComputationVector3afp convertToVector(Vector3D<?, ?, ?> vector) {
		if (vector instanceof InnerComputationVector3afp) {
			return (InnerComputationVector3afp) vector;
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
		return new InnerComputationVector3afp(x, y, z);
	}

	@Override
	public InnerComputationPoint3afp newPoint() {
		return new InnerComputationPoint3afp(0, 0, 0);
	}

	@Override
	public InnerComputationPoint3afp newPoint(double x, double y, double z) {
	    return new InnerComputationPoint3afp(x, y, z);
	}

	@Override
	public InnerComputationPoint3afp newPoint(int x, int y, int z) {
	    return new InnerComputationPoint3afp(x, y, z);
	}

	@Override
	public InnerComputationVector3afp newVector() {
		return new InnerComputationVector3afp(0, 0, 0);
	}

	@Override
	public InnerComputationVector3afp newVector(double x, double y, double z) {
		return new InnerComputationVector3afp(x, y, z);
	}

	@Override
	public InnerComputationVector3afp newVector(int x, int y, int z) {
		return new InnerComputationVector3afp(x, y, z);
	}

	@Override
	public InnerComputationQuaternionafp newQuaternion(double x, double y, double z, double w) {
		return new InnerComputationQuaternionafp(x, y, z, w);
	}

	@Override
	public InnerComputationQuaternionafp newQuaternion(int x, int y, int z, int w) {
		return new InnerComputationQuaternionafp(x, y, z, w);
	}

	@Override
	public InnerComputationQuaternionafp newQuaternionFromAxisAngle(double x, double y, double z, double angle) {
		final QuaternionComponents comps = Quaternion.computeWithAxisAngle(x, y, z, angle);
		return new InnerComputationQuaternionafp(comps.x(), comps.y(), comps.z(), comps.w());
	}

	@Override
	public InnerComputationQuaternionafp newQuaternionFromAxisAngle(int x, int y, int z, int angle) {
		final QuaternionComponents comps = Quaternion.computeWithAxisAngle(x, y, z, angle);
		return new InnerComputationQuaternionafp(comps.x(), comps.y(), comps.z(), comps.w());
	}

}
