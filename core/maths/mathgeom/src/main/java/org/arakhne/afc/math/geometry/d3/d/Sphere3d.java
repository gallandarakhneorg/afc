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

package org.arakhne.afc.math.geometry.d3.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A sphere with 3 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Sphere3d
		extends AbstractShape3d<Sphere3d>
		implements Sphere3afp<Shape3d<?>, Sphere3d, PathElement3d, Point3d, Vector3d, Quaternion4d, AlignedBox3d> {

	private static final long serialVersionUID = -8532584773530573738L;

	private double centerX;

	private double centerY;

	private double centerZ;

	private double radius;

	/** Construct an empty circle.
	 */
	public Sphere3d() {
		//
	}

	/** Construct a circle at the given position, and with the given radius.
	 * @param center the center of the circle.
	 * @param radius the radius of the circle.
	 */
	public Sphere3d(Point3D<?, ?, ?> center, double radius) {
		set(center.getX(), center.getY(), center.getZ(), radius);
	}

	/** Construct a circle at the given position, and with the given radius.
	 * @param x x coordinate of the center of the circle.
	 * @param y y coordinate of the center of the circle.
	 * @param z z coordinate of the center of the circle.
	 * @param radius the radius of the circle.
	 */
	public Sphere3d(double x, double y, double z, double radius) {
		set(x, y, z, radius);
	}

	/** Construct a sphere from a sphere.
	 * @param sphere the sphere to copy.
	 */
	public Sphere3d(Sphere3afp<?, ?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		set(sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius());
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(this.centerX);
		bits = 31 * bits + Double.hashCode(this.centerY);
		bits = 31 * bits + Double.hashCode(this.centerZ);
		bits = 31 * bits + Double.hashCode(this.radius);
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public double getX() {
		return this.centerX;
	}

	@Pure
	@Override
	public double getY() {
		return this.centerY;
	}

	@Pure
	@Override
	public double getZ() {
		return this.centerZ;
	}

	@Override
	public void setX(double x) {
		if (this.centerX != x) {
			this.centerX = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY(double y) {
		if (this.centerY != y) {
			this.centerY = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setZ(double z) {
		if (this.centerZ != z) {
			this.centerZ = z;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getRadius() {
		return this.radius;
	}

	@Override
	public void setRadius(double radius) {
		if (this.radius != radius) {
			this.radius = radius;
			fireGeometryChange();
		}
	}

	@Override
	public void set(double x, double y, double z, double radius) {
		assert radius >= 0. : AssertMessages.positiveOrZeroParameter(3);
		if (this.centerX != x || this.centerY != y || this.centerZ != z || this.radius != radius) {
			this.centerX = x;
			this.centerY = y;
			this.centerZ = z;
			this.radius = radius;
			fireGeometryChange();
		}
	}

}
