/* 
 * $Id$
 * 
 * Copyright (c) 2016, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports (SET)
 * of Universite de Technologie de Belfort-Montbeliard.
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SET.
 *
 * http://www.multiagent.fr/
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
final class ImmutableGeomFactory3D implements GeomFactory3D<ImmutableVector3D, ImmutablePoint3D> {

	/** Singleton of the factory
	 */
	public static final ImmutableGeomFactory3D SINGLETON = new ImmutableGeomFactory3D();
	
	private ImmutableGeomFactory3D() {
		//
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.GeomFactory3D#convertToPoint(org.arakhne.afc.math.geometry.d3.Point3D)
	 */
	@Override
	public ImmutablePoint3D convertToPoint(Point3D<?, ?> p) {
		if (p instanceof ImmutablePoint3D) {
			return (ImmutablePoint3D) p;
		}
		double x, y, z;
		if (p == null) {
			x = 0;
			y = 0;
			z = 0;
		} else {
			x = p.getX();
			y = p.getY();
			z = p.getZ();
		}
		return new ImmutablePoint3D(x, y, z);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.GeomFactory3D#convertToVector(org.arakhne.afc.math.geometry.d3.Point3D)
	 */
	@Override
	public ImmutableVector3D convertToVector(Point3D<?, ?> p) {
		double x, y, z;
		if (p == null) {
			x = 0;
			y = 0;
			z = 0;
		} else {
			x = p.getX();
			y = p.getY();
			z = p.getZ();
		}
		return new ImmutableVector3D(x, y, z);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.GeomFactory3D#convertToPoint(org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	public ImmutablePoint3D convertToPoint(Vector3D<?, ?> v) {
		double x, y, z;
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

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.GeomFactory3D#convertToVector(org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	public ImmutableVector3D convertToVector(Vector3D<?, ?> v) {
		if (v instanceof ImmutableVector3D) {
			return (ImmutableVector3D) v;
		}
		double x, y, z;
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
	public ImmutableVector3D newVector() {
		return new ImmutableVector3D(0, 0, 0);
	}

	@Override
	public ImmutablePoint3D newPoint(double x, double y, double z) {
		return new ImmutablePoint3D(x, y, z);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.GeomFactory3D#newVector(double, double, double)
	 */
	@Override
	public ImmutableVector3D newVector(double x, double y, double z) {
		return new ImmutableVector3D(x, y, z);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.GeomFactory3D#newPoint(int, int, int)
	 */
	@Override
	public ImmutablePoint3D newPoint(int x, int y, int z) {
		return new ImmutablePoint3D(x, y, z);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.GeomFactory3D#newVector(int, int, int)
	 */
	@Override
	public ImmutableVector3D newVector(int x, int y, int z) {
		return new ImmutableVector3D(x, y, z);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.GeomFactory3D#newQuaternion(org.arakhne.afc.math.geometry.d3.Vector3D, double)
	 */
	@Override
	public Quaternion newQuaternion(Vector3D<?, ?> axis, double angle) {
		throw new UnsupportedOperationException("Not yet implemented"); //$NON-NLS-1$ // TODO
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.GeomFactory3D#newQuaternion(double, double, double)
	 */
	@Override
	public Quaternion newQuaternion(double attitude, double bank, double heading) {
		throw new UnsupportedOperationException("Not yet implemented"); //$NON-NLS-1$ // TODO
	}

}
