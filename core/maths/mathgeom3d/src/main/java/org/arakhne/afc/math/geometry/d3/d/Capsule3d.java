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
import org.arakhne.afc.math.geometry.d3.afp.Capsule3afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;

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
	implements Capsule3afp<Capsule3d, Capsule3d, PathElement3d, Point3d, Vector3d, Quaternion4d, AlignedBox3d> {

	private static final long serialVersionUID = -242099273133205228L;

	private double x1;

	private double y1;

	private double z1;

	private double x2;

	private double y2;

	private double z2;

	private double radius;

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
		this(
				medial1.getX(), medial1.getY(), medial1.getZ(),
				medial2.getX(), medial2.getY(), medial2.getZ(),
				radius);
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
		set(medial1x, medial1y, medial1z, medial2x, medial2y, medial2z, radius);
	}

	/** Construct a capsule from a capsule.
	 *
	 * @param capsule the capsule to copy.
	 */
	public Capsule3d(Capsule3afp<?, ?, ?, ?, ?, ?, ?> capsule) {
		assert capsule != null : AssertMessages.notNullParameter();
		set(
				capsule.getX1(), capsule.getY1(), capsule.getZ1(),
				capsule.getX2(), capsule.getY2(), capsule.getZ2(),
				capsule.getRadius());
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		var bits = 1L;
		bits = 31 * bits + Double.hashCode(this.x1);
		bits = 31 * bits + Double.hashCode(this.y1);
		bits = 31 * bits + Double.hashCode(this.z1);
		bits = 31 * bits + Double.hashCode(this.x2);
		bits = 31 * bits + Double.hashCode(this.y2);
		bits = 31 * bits + Double.hashCode(this.z2);
		bits = 31 * bits + Double.hashCode(this.radius);
		return (int) (bits ^ (bits >> 31));
	}

	@Override
	@Pure
	public double getX1() {
		return this.x1;
	}

	@Override
	@Pure
	public double getY1() {
		return this.y1;
	}

	@Override
	@Pure
	public double getZ1() {
		return this.z1;
	}

	@Override
	@Pure
	public double getX2() {
		return this.x2;
	}

	@Override
	@Pure
	public double getY2() {
		return this.y2;
	}

	@Override
	@Pure
	public double getZ2() {
		return this.z2;
	}

	@Override
	@Pure
	public double getRadius() {
		return this.radius;
	}

	@Override
	public void setX1(double x) {
		if (this.x1 != x) {
			this.x1 = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY1(double y) {
		if (this.y1 != y) {
			this.y1 = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setZ1(double z) {
		if (this.z1 != z) {
			this.z1 = z;
			fireGeometryChange();
		}
	}

	@Override
	public void setX2(double x) {
		if (this.x2 != x) {
			this.x2 = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY2(double y) {
		if (this.y2 != y) {
			this.y2 = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setZ2(double z) {
		if (this.z2 != z) {
			this.z2 = z;
			fireGeometryChange();
		}
	}

	@Override
	public void setRadius(double radius) {
		if (this.radius != radius) {
			this.radius = radius;
			fireGeometryChange();
		}
	}

	@Override
	public void set(double x1, double y1, double z1, double x2, double y2, double z2, double radius) {
		if (this.x1 != x1 || this.y1 != y1 || this.z1 != z1
				|| this.x2 != x2 || this.y2 != y2 || this.z2 != z2
				|| this.radius != radius) {
			this.x1 = x1;
			this.y1 = y1;
			this.z1 = z1;
			this.x2 = x2;
			this.y2 = y2;
			this.z2 = z2;
			this.radius = radius;
			fireGeometryChange();
		}
	}
}
