/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;

/** A 2D segment/line with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment3d extends AbstractShape3d<Segment3d>
	implements Segment3afp<Shape3d<?>, Segment3d, PathElement3d, Point3d, Vector3d, RectangularPrism3d> {

	private static final long serialVersionUID = -5667213589442134247L;

	private double ax;

	private double ay;

	private double az;

	private double bx;

	private double by;

	private double bz;

	/** Construct an empty segment.
     */
	public Segment3d() {
		//
	}

	/** Construct a segment from the two given points.
     * @param p1 first point.
     * @param p2 second point.
     */
	public Segment3d(Point3D<?, ?> p1, Point3D<?, ?> p2) {
		this(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ());
	}

	/** Constructor by copy.
     * @param segment the segment to copy.
     */
	public Segment3d(Segment3afp<?, ?, ?, ?, ?, ?> segment) {
		this(segment.getX1(), segment.getY1(), segment.getZ1(), segment.getX2(), segment.getY2(), segment.getZ2());
	}

	/** Construct a segment from the two given points.
     * @param x1 x coordinate of the first point.
     * @param y1 y coordinate of the first point.
     * @param z1 z coordinate of the first point.
     * @param x2 x coordinate of the second point.
     * @param y2 y coordinate of the second point.
     * @param z2 z coordinate of the second point.
     */
	public Segment3d(double x1, double y1, double z1, double x2, double y2, double z2) {
		set(x1, y1, z1, x2, y2, z2);
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(this.ax);
		bits = 31 * bits + Double.hashCode(this.ay);
		bits = 31 * bits + Double.hashCode(this.az);
		bits = 31 * bits + Double.hashCode(this.bx);
		bits = 31 * bits + Double.hashCode(this.by);
		bits = 31 * bits + Double.hashCode(this.bz);
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public Segment3d createTransformedShape(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return clone();
		}
		final Point3d point = getGeomFactory().newPoint(getX1(), getY1(), getZ1());
		transform.transform(point);
		final double x1 = point.getX();
		final double y1 = point.getY();
		final double z1 = point.getZ();
		point.set(getX2(), getY2(), getZ2());
		transform.transform(point);
		return getGeomFactory().newSegment(x1, y1, z1, point.getX(), point.getY(), point.getZ());
	}

	@Override
	public void set(double x1, double y1, double z1, double x2, double y2, double z2) {
		if (this.ax != x1 || this.ay != y1 || this.az != z1 || this.bx != x2 || this.by != y2 || this.bz != z2) {
			this.ax = x1;
			this.ay = y1;
			this.az = z1;
			this.bx = x2;
			this.by = y2;
			this.bz = z2;
			fireGeometryChange();
		}
	}

	@Override
	public void setX1(double x) {
		if (this.ax != x) {
			this.ax = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY1(double y) {
		if (this.ay != y) {
			this.ay = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setZ1(double z) {
		if (this.az != z) {
			this.az = z;
			fireGeometryChange();
		}
	}

	@Override
	public void setX2(double x) {
		if (this.bx != x) {
			this.bx = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY2(double y) {
		if (this.by != y) {
			this.by = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setZ2(double z) {
		if (this.bz != z) {
			this.bz = z;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getX1() {
		return this.ax;
	}

	@Pure
	@Override
	public double getY1() {
		return this.ay;
	}

	@Pure
	@Override
	public double getZ1() {
		return this.az;
	}

	@Pure
	@Override
	public double getX2() {
		return this.bx;
	}

	@Pure
	@Override
	public double getY2() {
		return this.by;
	}

	@Pure
	@Override
	public double getZ2() {
		return this.bz;
	}

	@Override
	public Point3d getP1() {
		return getGeomFactory().newPoint(this.ax, this.ay, this.az);
	}

	@Override
	public Point3d getP2() {
		return getGeomFactory().newPoint(this.bx, this.by, this.bz);
	}

}
