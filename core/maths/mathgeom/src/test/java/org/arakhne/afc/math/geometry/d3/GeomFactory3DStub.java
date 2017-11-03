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

package org.arakhne.afc.math.geometry.d3;

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

	@Override
	public Quaternion newQuaternion(Vector3D<?, ?> axis, double angle) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Quaternion newQuaternion(double attitude, double bank, double heading) {
		throw new UnsupportedOperationException();
	}
	
}
