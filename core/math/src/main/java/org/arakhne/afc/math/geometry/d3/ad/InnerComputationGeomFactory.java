/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.d3.ad;

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Vector3D;

/** Factory of immutable geometrical primitives.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
final class InnerComputationGeomFactory implements GeomFactory3D<InnerComputationVector3ad, InnerComputationPoint3ad> {

	/** Singleton of the factory.
	 */
	public static final InnerComputationGeomFactory SINGLETON = new InnerComputationGeomFactory();
	
	private InnerComputationGeomFactory() {
		//
	}

	@Override
	public InnerComputationPoint3ad convertToPoint(Point3D<?, ?> p) {
		if (p instanceof InnerComputationPoint3ad) {
			return (InnerComputationPoint3ad) p;
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
		return new InnerComputationPoint3ad(x, y, z);
	}

	@Override
	public InnerComputationVector3ad convertToVector(Point3D<?, ?> p) {
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
		return new InnerComputationVector3ad(x, y, z);
	}

	@Override
	public InnerComputationPoint3ad convertToPoint(Vector3D<?, ?> v) {
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
		return new InnerComputationPoint3ad(x, y, z);
	}

	@Override
	public InnerComputationVector3ad convertToVector(Vector3D<?, ?> v) {
		if (v instanceof InnerComputationVector3ad) {
			return (InnerComputationVector3ad) v;
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
		return new InnerComputationVector3ad(x, y, z);
	}

	@Override
	public InnerComputationPoint3ad newPoint() {
		return new InnerComputationPoint3ad(0, 0, 0);
	}

	@Override
	public InnerComputationVector3ad newVector() {
		return new InnerComputationVector3ad(0, 0, 0);
	}

	@Override
	public InnerComputationPoint3ad newPoint(double x, double y, double z) {
		return new InnerComputationPoint3ad(x, y, z);
	}

	@Override
	public InnerComputationVector3ad newVector(double x, double y, double z) {
		return new InnerComputationVector3ad(x, y, z);
	}

	@Override
	public InnerComputationPoint3ad newPoint(int x, int y, int z) {
		return new InnerComputationPoint3ad(x, y, z);
	}

	@Override
	public InnerComputationVector3ad newVector(int x, int y, int z) {
		return new InnerComputationVector3ad(x, y, z);
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