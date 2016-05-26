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

package org.arakhne.afc.math.geometry.d3.i;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.ai.Segment3ai;
import org.eclipse.xtext.xbase.lib.Pure;

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
	
	/**
	 */
	public Segment3i() {
		//
	}

	/**
	 * @param a
	 * @param b
	 */
	public Segment3i(Point3D<?, ?> a, Point3D<?, ?> b) {
		this(a.ix(), a.iy(), a.iz(), b.ix(), b.iy(), b.iz());
	}

	/**
	 * @param s
	 */
	public Segment3i(Segment3ai<?, ?, ?, ?, ?, ?> s) {
		this(s.getX1(), s.getY1(), s.getZ1(), s.getX2(), s.getY2(), s.getZ2());
	}

	/**
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
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
		return bits ^ (bits >> 32);
	}

	@Pure
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(getX1());
		b.append(";"); //$NON-NLS-1$
		b.append(getY1());
		b.append(";"); //$NON-NLS-1$
		b.append(getZ1());
		b.append("|"); //$NON-NLS-1$
		b.append(getX2());
		b.append(";"); //$NON-NLS-1$
		b.append(getY2());
		b.append(";"); //$NON-NLS-1$
		b.append(getZ2());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Pure
	@Override
	public Shape3i<?> createTransformedShape(Transform3D transform) {
		assert (transform != null) : "Transformation must be not null"; //$NON-NLS-1$
		Point3i point = getGeomFactory().newPoint(getX1(), getY1(), getZ1());
		transform.transform(point);
		int x1 = point.ix();
		int y1 = point.iy();
		int z1 = point.iz();
		point.set(getX2(), getY2(), getZ2());
		transform.transform(point);
		return getGeomFactory().newSegment(x1, y1,z1, point.ix(), point.iy(), point.iz());
	}
	
	@Override
	public void set(int x1, int y1, int z1, int x2, int y2, int z2) {
		if (this.ax != x1 || this.ay != y1 || this.az != z1 || this.bx != x2 || this.by != y2 || this.bz != z2 ) {
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
