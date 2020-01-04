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

package org.arakhne.afc.math.geometry.d3.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;

/** A 3D segment/line encapsulating points with 3 double precision numbers.
 *
 *  <p>This segment is defined by its two extremities. It should not differ from
 *  the original Segment3d except from storage type.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class SegmentPoint3d extends AbstractShape3d<SegmentPoint3d>
	implements Segment3afp<Shape3d<?>, SegmentPoint3d, PathElement3d, Point3d, Vector3d, RectangularPrism3d> {

	private static final long serialVersionUID = -5667213589442134247L;

	private Point3d p1 = new Point3d();

	private Point3d p2 = new Point3d();

	/** Construct an empty segment.
     */
	public SegmentPoint3d() {
		//
	}

	/** Construct a segment from the two given points.
	 * @param p1 first point.
	 * @param p2 second point.
	 */
	public SegmentPoint3d(Point3D<?, ?> p1, Point3D<?, ?> p2) {
	    this(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ());
	}

	/** Construct a segment from the two given points.
     * @param p1 first point.
     * @param p2 second point.
     */
	public SegmentPoint3d(Point3d p1, Point3d p2) {
	    this.p1 = p1;
	    this.p2 = p2;
	}

	/** Constructor by copy.
	 * @param segment the segment to copy.
	 */
	public SegmentPoint3d(Segment3afp<?, ?, ?, ?, ?, ?> segment) {
	    this(segment.getX1(), segment.getY1(), segment.getZ1(), segment.getX2(), segment.getY2(), segment.getZ2());
	}

	/** Constructor by copy.
     * @param segment the segment to copy.
     */
	public SegmentPoint3d(SegmentPoint3d segment) {
	    this.p1 = segment.p1;
	    this.p2 = segment.p2;
	}

	/** Construct a segment from the two given points.
     * @param x1 x coordinate of the first point.
     * @param y1 y coordinate of the first point.
     * @param z1 z coordinate of the first point.
     * @param x2 x coordinate of the second point.
     * @param y2 y coordinate of the second point.
     * @param z2 z coordinate of the second point.
     */
	public SegmentPoint3d(double x1, double y1, double z1, double x2, double y2, double z2) {
		set(x1, y1, z1, x2, y2, z2);
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + this.p1.hashCode();
		bits = 31 * bits + this.p2.hashCode();
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public SegmentPoint3d createTransformedShape(Transform3D transform) {
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
		return new SegmentPoint3d(x1, y1, z1, point.getX(), point.getY(), point.getZ());
	}

	@Override
	public void set(double x1, double y1, double z1, double x2, double y2, double z2) {
		if (this.p1.x != x1 || this.p1.y != y1 || this.p1.z != z1 || this.p2.x != x2 || this.p2.y != y2 || this.p2.z != z2) {
			this.p1.x = x1;
			this.p1.y = y1;
			this.p1.z = z1;
			this.p2.x = x2;
			this.p2.y = y2;
			this.p2.z = z2;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getX1() {
	    return this.p1.x;
	}

	@Override
	public void setX1(double x) {
		if (this.p1.x != x) {
			this.p1.x = x;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getY1() {
	    return this.p1.y;
	}

	@Override
	public void setY1(double y) {
		if (this.p1.y != y) {
			this.p1.y = y;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getZ1() {
	    return this.p1.z;
	}

	@Override
	public void setZ1(double z) {
		if (this.p1.z != z) {
			this.p1.z = z;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getX2() {
	    return this.p2.x;
	}

	@Override
	public void setX2(double x) {
		if (this.p2.x != x) {
			this.p2.x = x;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getY2() {
	    return this.p2.y;
	}

	@Override
	public void setY2(double y) {
		if (this.p2.y != y) {
			this.p2.y = y;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getZ2() {
	    return this.p2.z;
	}

	@Override
	public void setZ2(double z) {
		if (this.p2.z != z) {
			this.p2.z = z;
			fireGeometryChange();
		}
	}

	@Override
	public Point3d getP1() {
		return this.p1;
	}

	@Override
	public void setP1(double x, double y, double z) {
	    this.p1.x = x;
	    this.p1.y = y;
	    this.p1.z = z;
	    fireGeometryChange();
	}

	/** Sets the first point of the segment.
	 *
	 * @param point the point to set.
	 */
	public void setP1(Point3d point) {
	    this.p1 = point;
	    fireGeometryChange();
	}

	@Override
	public Point3d getP2() {
		return this.p2;
	}

	@Override
	public void setP2(double x, double y, double z) {
	    this.p2.x = x;
	    this.p2.y = y;
	    this.p2.z = z;
	    fireGeometryChange();
	}

	/** Sets the second point of the segment.
	 *
	 * @param point the point to set.
	 */
	public void setP2(Point3d point) {
	    this.p2 = point;
	    fireGeometryChange();
	}

}
