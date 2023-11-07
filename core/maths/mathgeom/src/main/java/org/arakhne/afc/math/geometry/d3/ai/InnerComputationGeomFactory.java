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

package org.arakhne.afc.math.geometry.d3.ai;

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
final class InnerComputationGeomFactory extends AbstractGeomFactory3D<InnerComputationVector3ai, InnerComputationPoint3ai, InnerComputationQuaternionai> {

	/** Singleton of the factory.
	 */
	public static final InnerComputationGeomFactory SINGLETON = new InnerComputationGeomFactory();

	private InnerComputationGeomFactory() {
		//
	}

	@Override
	public InnerComputationPoint3ai convertToPoint(Point3D<?, ?, ?> pt) {
		if (pt instanceof InnerComputationPoint3ai) {
			return (InnerComputationPoint3ai) pt;
		}
		final int x;
		final int y;
		final int z;
		if (pt == null) {
			x = 0;
			y = 0;
			z = 0;
		} else {
			x = pt.ix();
			y = pt.iy();
			z = pt.iz();
		}
		return new InnerComputationPoint3ai(x, y, z);
	}

	@Override
	public InnerComputationPoint3ai convertToPoint(Vector3D<?, ?, ?> vector) {
	    final int x;
	    final int y;
	    final int z;
	    if (vector == null) {
	        x = 0;
	        y = 0;
	        z = 0;
	    } else {
	        x = vector.ix();
	        y = vector.iy();
	        z = vector.iz();
	    }
	    return new InnerComputationPoint3ai(x, y, z);
	}

	@Override
	public InnerComputationVector3ai convertToVector(Point3D<?, ?, ?> pt) {
		final int x;
		final int y;
		final int z;
		if (pt == null) {
			x = 0;
			y = 0;
			z = 0;
		} else {
			x = pt.ix();
			y = pt.iy();
			z = pt.iz();
		}
		return new InnerComputationVector3ai(x, y, z);
	}

	@Override
	public InnerComputationVector3ai convertToVector(Vector3D<?, ?, ?> v) {
		if (v instanceof InnerComputationVector3ai) {
			return (InnerComputationVector3ai) v;
		}
		final int x;
		final int y;
		final int z;
		if (v == null) {
			x = 0;
			y = 0;
			z = 0;
		} else {
			x = v.ix();
			y = v.iy();
			z = v.iz();
		}
		return new InnerComputationVector3ai(x, y, z);
	}

	@Override
	public InnerComputationPoint3ai newPoint() {
		return new InnerComputationPoint3ai(0, 0, 0);
	}

	@Override
	public InnerComputationPoint3ai newPoint(double x, double y, double z) {
	    return new InnerComputationPoint3ai((int) Math.round(x), (int) Math.round(y), (int) Math.round(z));
	}

	@Override
	public InnerComputationPoint3ai newPoint(int x, int y, int z) {
	    return new InnerComputationPoint3ai(x, y, z);
	}

	@Override
	public InnerComputationVector3ai newVector() {
		return new InnerComputationVector3ai(0, 0, 0);
	}

	@Override
	public InnerComputationVector3ai newVector(double x, double y, double z) {
		return new InnerComputationVector3ai((int) Math.round(x), (int) Math.round(y), (int) Math.round(z));
	}

	@Override
	public InnerComputationVector3ai newVector(int x, int y, int z) {
		return new InnerComputationVector3ai(x, y, z);
	}

	@Override
	public InnerComputationQuaternionai newQuaternion(double x, double y, double z, double w) {
		return new InnerComputationQuaternionai(x, y, z, w);
	}

	@Override
	public InnerComputationQuaternionai newQuaternion(int x, int y, int z, int w) {
		return new InnerComputationQuaternionai(x, y, z, w);
	}

	@Override
	public InnerComputationQuaternionai newQuaternionFromAxisAngle(double x, double y, double z, double angle) {
		final QuaternionComponents comps = Quaternion.computeWithAxisAngle(x, y, z, angle);
		return new InnerComputationQuaternionai(comps.x(), comps.y(), comps.z(), comps.w());
	}

	@Override
	public InnerComputationQuaternionai newQuaternionFromAxisAngle(int x, int y, int z, int angle) {
		final QuaternionComponents comps = Quaternion.computeWithAxisAngle(x, y, z, angle);
		return new InnerComputationQuaternionai(comps.x(), comps.y(), comps.z(), comps.w());
	}

}
