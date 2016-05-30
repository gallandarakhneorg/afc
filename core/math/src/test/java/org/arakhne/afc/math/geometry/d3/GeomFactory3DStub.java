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
package org.arakhne.afc.math.geometry.d3;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

@SuppressWarnings("all")
public final class GeomFactory3DStub implements GeomFactory3D<Vector3DStub, Point3DStub> {

	public GeomFactory3DStub() {
		//
	}

	@Override
	public Point3DStub convertToPoint(Point3D<?, ?> p) {
		if (p instanceof Point3DStub) {
			return (Point3DStub) p;
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
		return new Point3DStub(x, y, z);
	}

	@Override
	public Vector3DStub convertToVector(Point3D<?, ?> p) {
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
		return new Vector3DStub(x, y, z);
	}

	@Override
	public Point3DStub convertToPoint(Vector3D<?, ?> v) {
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
		return new Point3DStub(x, y, z);
	}

	@Override
	public Vector3DStub convertToVector(Vector3D<?, ?> v) {
		if (v instanceof Vector3DStub) {
			return (Vector3DStub) v;
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
		return new Vector3DStub(x, y, z);
	}

	@Override
	public Point3DStub newPoint() {
		return new Point3DStub(0, 0, 0);
	}

	@Override
	public Vector3DStub newVector() {
		return new Vector3DStub(0, 0, 0);
	}

	@Override
	public Point3DStub newPoint(double x, double y, double z) {
		return new Point3DStub(x, y, z);
	}

	@Override
	public Vector3DStub newVector(double x, double y, double z) {
		return new Vector3DStub(x, y, z);
	}

	@Override
	public Point3DStub newPoint(int x, int y, int z) {
		return new Point3DStub(x, y, z);
	}

	@Override
	public Vector3DStub newVector(int x, int y, int z) {
		return new Vector3DStub(x, y, z);
	}
	
}