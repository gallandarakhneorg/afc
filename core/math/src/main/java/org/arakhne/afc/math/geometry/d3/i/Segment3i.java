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
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A 2D segment/line with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment3i extends AbstractShape3i<Segment3i>
	implements Segment3ai<Shape3i<?>, Segment3i, PathElement3i, Point3i, Vector3i, RectangularPrism3i> {

	private static final long serialVersionUID = 4069080422632034507L;

	private int ax;

	private int ay;

	private int az;

	private int bx;

	private int by;

	private int bz;

	/** Construct an empty segment.
     */
	public Segment3i() {
	    //
	}

	/** Construct a segment with the two given points.
     * @param p1 first point.
     * @param p2 second point.
     */
	public Segment3i(Point3D<?, ?> p1, Point3D<?, ?> p2) {
		this(p1.ix(), p1.iy(), p1.iz(), p2.ix(), p2.iy(), p2.iz());
	}

	/** Constructor by copy.
     * @param segment the segment to copy.
     */
	public Segment3i(Segment3ai<?, ?, ?, ?, ?, ?> segment) {
		this(segment.getX1(), segment.getY1(), segment.getZ1(), segment.getX2(), segment.getY2(), segment.getZ2());
	}

	/** Construct a segment with the two given points.
     * @param x1 x coordinate of the first point.
     * @param y1 y coordinate of the first point.
     * @param z1 z coordinate of the first point.
     * @param x2 x coordinate of the second point.
     * @param y2 y coordinate of the second point.
     * @param z2 z coordinate of the second point.
     */
	public Segment3i(int x1, int y1, int z1, int x2, int y2, int z2) {
		set(x1, y1, z1, x2, y2, z2);
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + this.ax;
		bits = 31 * bits + this.ay;
		bits = 31 * bits + this.az;
		bits = 31 * bits + this.bx;
		bits = 31 * bits + this.by;
		bits = 31 * bits + this.bz;
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public Shape3i<?> createTransformedShape(Transform3D transform) {
		assert transform != null : AssertMessages.notNullParameter();
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
	public void setX1(int x) {
		if (this.ax != x) {
			this.ax = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY1(int y) {
		if (this.ay != y) {
			this.ay = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setZ1(int z) {
		if (this.az != z) {
			this.az = z;
			fireGeometryChange();
		}
	}

	@Override
	public void setX2(int x) {
		if (this.bx != x) {
			this.bx = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY2(int y) {
		if (this.by != y) {
			this.by = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setZ2(int z) {
		if (this.bz != z) {
			this.bz = z;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getX1() {
		return this.ax;
	}

	@Pure
	@Override
	public int getY1() {
		return this.ay;
	}

	@Pure
	@Override
	public int getZ1() {
		return this.az;
	}

	@Pure
	@Override
	public int getX2() {
		return this.bx;
	}

	@Pure
	@Override
	public int getY2() {
		return this.by;
	}

	@Pure
	@Override
	public int getZ2() {
		return this.bz;
	}

	@Override
	public Point3i getP1() {
		return getGeomFactory().newPoint(this.ax, this.ay, this.az);
	}

	@Override
	public Point3i getP2() {
		return getGeomFactory().newPoint(this.bx, this.by, this.bz);
	}

}
