/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3;

/** Factory of 3D immutable geometrical primitives.
 *
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
final class ImmutableGeomFactory3D extends AbstractGeomFactory3D<ImmutableVector3D, ImmutablePoint3D> {

	/** Singleton of the factory.
	 */
	public static final ImmutableGeomFactory3D SINGLETON = new ImmutableGeomFactory3D();

	private ImmutableGeomFactory3D() {
		//
	}

	@Override
	public ImmutablePoint3D convertToPoint(Point3D<?, ?> pt) {
		if (pt instanceof ImmutablePoint3D) {
			return (ImmutablePoint3D) pt;
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
		return new ImmutablePoint3D(x, y, z);
	}

    @Override
    public ImmutablePoint3D convertToPoint(Vector3D<?, ?> v) {
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
        return new ImmutablePoint3D(x, y, z);
    }

	@Override
	public ImmutableVector3D convertToVector(Point3D<?, ?> pt) {
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
		return new ImmutableVector3D(x, y, z);
	}

	@Override
	public ImmutableVector3D convertToVector(Vector3D<?, ?> v) {
		if (v instanceof ImmutableVector3D) {
			return (ImmutableVector3D) v;
		}
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
		return new ImmutableVector3D(x, y, z);
	}

	@Override
	public ImmutablePoint3D newPoint() {
		return new ImmutablePoint3D(0, 0, 0);
	}

    @Override
    public ImmutablePoint3D newPoint(double x, double y, double z) {
        return new ImmutablePoint3D(x, y, z);
    }

    @Override
    public ImmutablePoint3D newPoint(int x, int y, int z) {
        return new ImmutablePoint3D(x, y, z);
    }

	@Override
	public ImmutableVector3D newVector() {
		return new ImmutableVector3D(0, 0, 0);
	}

	@Override
	public ImmutableVector3D newVector(double x, double y, double z) {
		return new ImmutableVector3D(x, y, z);
	}

	@Override
	public ImmutableVector3D newVector(int x, int y, int z) {
		return new ImmutableVector3D(x, y, z);
	}

	@Override
	public Quaternion newQuaternion(Vector3D<?, ?> axis, double angle) {
		throw new UnsupportedOperationException("Not yet implemented"); //$NON-NLS-1$
	}

	@Override
	public Quaternion newQuaternion(double attitude, double bank, double heading) {
		throw new UnsupportedOperationException("Not yet implemented"); //$NON-NLS-1$
	}

}
