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

package org.arakhne.afc.math.geometry.d3.d;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.eclipse.xtext.xbase.lib.Pure;

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
	
	/**
	 */
	public Segment3d() {
		//
	}

	/**
	 * @param a
	 * @param b
	 */
	public Segment3d(Point3D<?, ?> a, Point3D<?, ?> b) {
		this(a.getX(), a.getY(), a.getZ(), b.getX(), b.getY(), b.getZ());
	}

	/**
	 * @param s
	 */
	public Segment3d(Segment3afp<?, ?, ?, ?, ?, ?> s) {
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
	public Segment3d(double x1, double y1, double z1, double x2, double y2, double z2) { 
		set(x1, y1, z1, x2, y2, z2);
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.ax);
		bits = 31 * bits + Double.doubleToLongBits(this.ay);
		bits = 31 * bits + Double.doubleToLongBits(this.az);
		bits = 31 * bits + Double.doubleToLongBits(this.bx);
		bits = 31 * bits + Double.doubleToLongBits(this.by);
		bits = 31 * bits + Double.doubleToLongBits(this.bz);
		int b = (int) bits;
		return b ^ (b >> 32);
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
	public Segment3d createTransformedShape(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return clone();
		}
		Point3d point = getGeomFactory().newPoint(getX1(), getY1(), getZ1());
		transform.transform(point);
		double x1 = point.getX();
		double y1 = point.getY();
		double z1 = point.getZ();
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
