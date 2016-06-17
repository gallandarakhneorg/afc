/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3.i;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.ai.Segment3ai;

/** A 3D segment/line encapsulating points with 3 integer numbers.
 *
 *  <p>This segment is defined by its two extremities. It should not differ from
 *  the original Segment3i except from storage type.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class SegmentPoint3i extends AbstractShape3i<SegmentPoint3i>
	implements Segment3ai<Shape3i<?>, SegmentPoint3i, PathElement3i, Point3i, Vector3i, RectangularPrism3i> {

	private static final long serialVersionUID = 4069080422632034507L;

	private Point3i p1 = new Point3i();

	private Point3i p2 = new Point3i();

	/** Construct an empty segment.
     */
	public SegmentPoint3i() {
	    //
	}

	/** Construct a segment with the two given points.
	 * @param p1 first point.
	 * @param p2 second point.
	 */
	public SegmentPoint3i(Point3D<?, ?> p1, Point3D<?, ?> p2) {
	    this(p1.ix(), p1.iy(), p1.iz(), p2.ix(), p2.iy(), p2.iz());
	}

	/** Construct a segment with the two given points.
     * @param p1 first point.
     * @param p2 second point.
     */
	public SegmentPoint3i(Point3i p1, Point3i p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	/** Constructor by copy.
	 * @param segment the segment to copy.
	 */
	public SegmentPoint3i(Segment3ai<?, ?, ?, ?, ?, ?> segment) {
	    this(segment.getX1(), segment.getY1(), segment.getZ1(), segment.getX2(), segment.getY2(), segment.getZ2());
	}

	/** Constructor by copy.
     * @param segment the segment to copy.
     */
	public SegmentPoint3i(SegmentPoint3i segment) {
	    this.p1 = segment.p1;
	    this.p2 = segment.p2;
	}

	/** Construct a segment with the two given points.
     * @param x1 x coordinate of the first point.
     * @param y1 y coordinate of the first point.
     * @param z1 z coordinate of the first point.
     * @param x2 x coordinate of the second point.
     * @param y2 y coordinate of the second point.
     * @param z2 z coordinate of the second point.
     */
	public SegmentPoint3i(int x1, int y1, int z1, int x2, int y2, int z2) {
		set(x1, y1, z1, x2, y2, z2);
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + this.p1.hashCode();
		bits = 31 * bits + this.p2.hashCode();
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public Shape3i<?> createTransformedShape(Transform3D transform) {
	    if (transform == null || transform.isIdentity()) {
	        return clone();
	    }
		final Point3i point = getGeomFactory().newPoint(getX1(), getY1(), getZ1());
		transform.transform(point);
		final int x1 = point.ix();
		final int y1 = point.iy();
		final int z1 = point.iz();
		point.set(getX2(), getY2(), getZ2());
		transform.transform(point);
		return getGeomFactory().newSegment(x1, y1, z1, point.ix(), point.iy(), point.iz());
	}

	@Override
	public void set(int x1, int y1, int z1, int x2, int y2, int z2) {
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
	public int getX1() {
	    return this.p1.x;
	}

	@Override
	public void setX1(int x) {
		if (this.p1.x != x) {
			this.p1.x = x;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getY1() {
	    return this.p1.y;
	}

	@Override
	public void setY1(int y) {
		if (this.p1.y != y) {
			this.p1.y = y;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getZ1() {
	    return this.p1.z;
	}

	@Override
	public void setZ1(int z) {
		if (this.p1.z != z) {
			this.p1.z = z;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getX2() {
	    return this.p2.x;
	}

	@Override
	public void setX2(int x) {
		if (this.p2.x != x) {
			this.p2.x = x;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getY2() {
	    return this.p2.y;
	}

	@Override
	public void setY2(int y) {
		if (this.p2.y != y) {
			this.p2.y = y;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getZ2() {
	    return this.p2.z;
	}

	@Override
	public void setZ2(int z) {
		if (this.p2.z != z) {
			this.p2.z = z;
			fireGeometryChange();
		}
	}

	@Override
	public Point3i getP1() {
		return this.p1;
	}

	@Override
	public void setP1(int x, int y, int z) {
	    this.p1.x = x;
	    this.p1.y = y;
	    this.p1.z = z;
	    fireGeometryChange();
	}

	/** Sets the first point of the segment.
	 *
	 * @param point the point to set.
	 */
	public void setP1(Point3i point) {
	    this.p1 = point;
	    fireGeometryChange();
	}

	@Override
	public Point3i getP2() {
		return this.p2;
	}

    @Override
    public void setP2(int x, int y, int z) {
        this.p2.x = x;
        this.p2.y = y;
        this.p2.z = z;
        fireGeometryChange();
    }

    /** Sets the second point of the segment.
     *
     * @param point the point to set.
     */
    public void setP2(Point3i point) {
        this.p2 = point;
        fireGeometryChange();
    }

}
