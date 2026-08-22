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

package org.arakhne.afc.math.geometry.d3.d;

import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.Capsule3afp;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.math.geometry.d3.afp.Triangle3afp;
import org.arakhne.afc.math.geometry.d3.general.Shape3DType;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A bounding capsule is a swept sphere (i.e. the volume that a sphere takes as it moves
 * along a straight line segment) containing the object. Capsules can be represented
 * by the radius of the swept sphere and the segment that the sphere is swept across).
 * It has traits similar to a cylinder, but is easier to use, because the intersection test
 * is simpler. A capsule and another object intersect if the distance between the
 * capsule's defining segment and some feature of the other object is smaller than the
 * capsule's radius. For example, two capsules intersect if the distance
 * between the capsules' segments is smaller than the sum of their radii. This holds for
 * arbitrarily rotated capsules, which is why they're more appealing than cylinders in practice.
 *
 * @author $Author: hjaffali$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public class Capsule3d
	extends AbstractShape3d<Capsule3d>
	implements Capsule3afp<Capsule3d, PathElement3d, Point3d, Vector3d, Quaternion4d, AlignedBox3d> {

	private static final long serialVersionUID = -242099273133205228L;

	//	private double centerX;
	//
	//	private double centerY;
	//
	//	private double centerZ;
	//
	//	private double radius;

	/** Construct an empty circle.
	 */
	public Capsule3d() {
		//
	}

	/** Construct a capsule at the given position, and with the given radius.
	 *
	 * @param medial1 the first medial point of the capsule.
	 * @param medial2 the first medial point of the capsule.
	 * @param radius the radius of the capsule.
	 */
	public Capsule3d(Point3D<?, ?, ?> medial1, Point3D<?, ?, ?> medial2, double radius) {
		assert medial1 != null : AssertMessages.notNullParameter(0);
		assert medial2 != null : AssertMessages.notNullParameter(1);
		//		this(
		//				medial1.getX(), medial1.getY(), medial1.getZ(),
		//				medial2.getX(), medial2.getY(), medial2.getZ(),
		//				radius);
	}

	/** Construct a capsule at the given position, and with the given radius.
	 *
	 * @param medial1x x coordinate of the first medial point of the capsule.
	 * @param medial1y y coordinate of the first medial point of the capsule.
	 * @param medial1z z coordinate of the first medial point of the capsule.
	 * @param medial2x x coordinate of the first medial point of the capsule.
	 * @param medial2y y coordinate of the first medial point of the capsule.
	 * @param medial2z z coordinate of the first medial point of the capsule.
	 * @param radius the radius of the capsule.
	 */
	public Capsule3d(double medial1x, double medial1y, double medial1z,
			double medial2x, double medial2y, double medial2z, double radius) {
		//set(x, y, z, radius);
	}

	/** Construct a capsule from a capsule.
	 *
	 * @param capsule the capsule to copy.
	 */
	public Capsule3d(Capsule3afp<?, ?, ?, ?, ?, ?> capsule) {
		assert capsule != null : AssertMessages.notNullParameter();
		// set(sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius());
	}

	@Override
	public double getMinX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMinX(double x) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getMaxX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxX(double x) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getMinY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMinY(double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getMaxY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxY(double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getMinZ() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMinZ(double z) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getMaxZ() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxZ(double z) {
		// TODO Auto-generated method stub

	}

	@Override
	public Shape3DType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(AlignedBox3afp<?, ?, ?, ?, ?, ?> AlignedBox) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(double x, double y, double z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(Capsule3afp<?, ?, ?, ?, ?, ?> capsule) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(AlignedBox3afp<?, ?, ?, ?, ?, ?> prism) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(Triangle3afp<?, ?, ?, ?, ?, ?, ?> triangle) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(PathIterator3afp<?> iterator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Point3d getClosestPointTo(Capsule3afp<?, ?, ?, ?, ?, ?> capsule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point3d getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point3d getClosestPointTo(AlignedBox3afp<?, ?, ?, ?, ?, ?> AlignedBox) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point3d getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point3d getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point3d getClosestPointTo(Triangle3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point3d getClosestPointTo(Point3D<?, ?, ?> point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equalsToShape(Capsule3d shape) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Point3d getFarthestPointTo(Point3D<?, ?, ?> point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDistanceSquared(Point3D<?, ?, ?> point) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDistanceL1(Point3D<?, ?, ?> point) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDistanceLinf(Point3D<?, ?, ?> point) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toGeogebra() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFromCorners(double x1, double y1, double z1, double x2, double y2, double z2) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("checkstyle:equalshashcode")
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	//	@Pure
	//	@Override
	//	@SuppressWarnings("checkstyle:equalshashcode")
	//	public int hashCode() {
	//		var bits = 1L;
	//		bits = 31 * bits + Double.hashCode(this.centerX);
	//		bits = 31 * bits + Double.hashCode(this.centerY);
	//		bits = 31 * bits + Double.hashCode(this.centerZ);
	//		bits = 31 * bits + Double.hashCode(this.radius);
	//		return (int) (bits ^ (bits >> 31));
	//	}
	//
	//	@Pure
	//	@Override
	//	public double getX() {
	//		return this.centerX;
	//	}
	//
	//	@Pure
	//	@Override
	//	public double getY() {
	//		return this.centerY;
	//	}
	//
	//	@Pure
	//	@Override
	//	public double getZ() {
	//		return this.centerZ;
	//	}
	//
	//	@Override
	//	public void setX(double x) {
	//		if (this.centerX != x) {
	//			this.centerX = x;
	//			fireGeometryChange();
	//		}
	//	}
	//
	//	@Override
	//	public void setY(double y) {
	//		if (this.centerY != y) {
	//			this.centerY = y;
	//			fireGeometryChange();
	//		}
	//	}
	//
	//	@Override
	//	public void setZ(double z) {
	//		if (this.centerZ != z) {
	//			this.centerZ = z;
	//			fireGeometryChange();
	//		}
	//	}
	//
	//	@Pure
	//	@Override
	//	public double getRadius() {
	//		return this.radius;
	//	}
	//
	//	@Override
	//	public void setRadius(double radius) {
	//		if (this.radius != radius) {
	//			this.radius = radius;
	//			fireGeometryChange();
	//		}
	//	}
	//
	//	@Override
	//	public void set(double x, double y, double z, double radius) {
	//		assert radius >= 0. : AssertMessages.positiveOrZeroParameter(3);
	//		if (this.centerX != x || this.centerY != y || this.centerZ != z || this.radius != radius) {
	//			this.centerX = x;
	//			this.centerY = y;
	//			this.centerZ = z;
	//			this.radius = radius;
	//			fireGeometryChange();
	//		}
	//	}

}
