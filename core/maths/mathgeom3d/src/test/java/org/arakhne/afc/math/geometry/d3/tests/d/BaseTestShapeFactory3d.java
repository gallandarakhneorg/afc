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

package org.arakhne.afc.math.geometry.d3.tests.d;

import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.Plane3afp;
import org.arakhne.afc.math.geometry.d3.afp.PlaneXY3afp;
import org.arakhne.afc.math.geometry.d3.afp.PlaneXZ3afp;
import org.arakhne.afc.math.geometry.d3.afp.PlaneYZ3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.math.geometry.d3.d.AlignedBox3d;
import org.arakhne.afc.math.geometry.d3.d.MultiShape3d;
import org.arakhne.afc.math.geometry.d3.d.Path3d;
import org.arakhne.afc.math.geometry.d3.d.Plane3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneXY3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneXZ3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneYZ3d;
import org.arakhne.afc.math.geometry.d3.d.Point3d;
import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;
import org.arakhne.afc.math.geometry.d3.d.Segment3d;
import org.arakhne.afc.math.geometry.d3.d.Sphere3d;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.arakhne.afc.math.geometry.d3.tests.afp.TestShapeFactory3d;

@SuppressWarnings("all")
class BaseTestShapeFactory3d implements TestShapeFactory3d<Point3d, Vector3d, Quaternion4d, AlignedBox3d>{

	@Override
	public Point3d createPoint(double x, double y, double z) {
		return new Point3d(x, y, z);
	}

	@Override
	public AlignedBox3d createAlignedBox(double x, double y, double z, double width, double height, double depth) {
		return new AlignedBox3d(x, y, z, width, height, depth);
	}

	@Override
	public Vector3d createVector(double x, double y, double z) {
		return new Vector3d(x, y, z);
	}

	@Override
	public Segment3afp<?, ?, ?, Point3d, Vector3d, Quaternion4d, AlignedBox3d> createSegment(double x1, double y1,
			double z1, double x2, double y2, double z2) {
		return new Segment3d(x1, y1, z1, x2, y2, z2);
	}

	@Override
	public Sphere3afp<?, ?, Point3d, Vector3d, Quaternion4d, AlignedBox3d> createSphere(double x, double y, double z, double radius) {
		return new Sphere3d(x, y, z, radius);
	}

	@Override
	public Path3afp<?, ?, Point3d, Vector3d, Quaternion4d, AlignedBox3d> createPath() {
		return new Path3d();
	}

	@Override
	public MultiShape3afp<?, ?, ?, Point3d, Vector3d, Quaternion4d, AlignedBox3d> createMultiShape() {
		return new MultiShape3d<>();
	}

	@Override
	public Quaternion4d createQuaternion(double x, double y, double z, double w) {
		return new Quaternion4d(x, y, z, w);
	}

	@Override
	public Quaternion4d createAxisAngle(double x, double y, double z, double angle) {
		final var aa = Quaternion.computeWithAxisAngle(x, y, z, angle);
		return new Quaternion4d(aa.x(), aa.y(), aa.z(), aa.w());
	}

	@Override
	public Transform3D createTransform() {
		return new Transform3D();
	}

	@Override
	public Plane3afp createPlane(double a, double b, double c, double d) {
		return new Plane3d(a, b, c, d);
	}

	@Override
	public PlaneXY3afp createPlaneXY(boolean positive, double z) {
		return new PlaneXY3d(positive, z);
	}

	@Override
	public PlaneXZ3afp createPlaneXZ(boolean positive, double y) {
		return new PlaneXZ3d(positive, y);
	}

	@Override
	public PlaneYZ3afp createPlaneYZ(boolean positive, double x) {
		return new PlaneYZ3d(positive, x);
	}
}
