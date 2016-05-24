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

package org.arakhne.afc.math.geometry.d3.dfx;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/** Segment with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment3dfx extends AbstractShape3dfx<Segment3dfx>
	implements Segment3afp<Shape3dfx<?>, Segment3dfx, PathElement3dfx, Point3dfx, Vector3dfx, RectangularPrism3dfx> {

	private static final long serialVersionUID = -5603953934276693947L;

	private DoubleProperty ax;

	private DoubleProperty ay;

	private DoubleProperty az;

	private DoubleProperty bx;

	private DoubleProperty by;

	private DoubleProperty bz;
	
	/**
	 */
	public Segment3dfx() {
		//
	}

	/**
	 * @param a
	 * @param b
	 */
	public Segment3dfx(Point3D<?, ?> a, Point3D<?, ?> b) {
		this(a.getX(), a.getY(), a.getZ(), b.getX(), b.getY(), b.getZ());
	}

	/**
	 * @param s
	 */
	public Segment3dfx(Segment3afp<?, ?, ?, ?, ?, ?> s) {
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
	public Segment3dfx(double x1, double y1, double z1, double x2, double y2, double z2) {
		set(x1, y1, z1, x2, y2, z2);
	}
	
	@Override
	public Segment3dfx clone() {
		Segment3dfx clone = super.clone();
		if (clone.ax != null) {
			clone.ax = null;
			clone.x1Property().set(getX1());
		}
		if (clone.ay != null) {
			clone.ay = null;
			clone.y1Property().set(getY1());
		}
		if (clone.az != null) {
			clone.az = null;
			clone.z1Property().set(getZ1());
		}
		if (clone.bx != null) {
			clone.bx = null;
			clone.x2Property().set(getX2());
		}
		if (clone.by != null) {
			clone.by = null;
			clone.y2Property().set(getY2());
		}
		if (clone.bz != null) {
			clone.bz = null;
			clone.z2Property().set(getZ2());
		}
		return clone;
	}
	

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(getX1());
		bits = 31 * bits + Double.doubleToLongBits(getY1());
		bits = 31 * bits + Double.doubleToLongBits(getZ1());
		bits = 31 * bits + Double.doubleToLongBits(getX2());
		bits = 31 * bits + Double.doubleToLongBits(getY2());
		bits = 31 * bits + Double.doubleToLongBits(getZ2());
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
	public Segment3dfx createTransformedShape(Transform3D transform) {
		assert (transform != null) : "Transformation must be not null"; //$NON-NLS-1$
		Point3dfx point = getGeomFactory().newPoint(getX1(), getY1(), getZ1());
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
		setX1(x1);
		setY1(y1);
		setZ1(z1);
		setX2(x2);
		setY2(y2);
		setZ2(z2);
	}

	@Override
	public void setX1(double x) {
		x1Property().set(x);
	}
	
	@Override
	public void setY1(double y) {
		y1Property().set(y);
	}

	@Override
	public void setZ1(double z) {
		z1Property().set(z);
	}

	@Override
	public void setX2(double x) {
		x2Property().set(x);
	}
	
	@Override
	public void setY2(double y) {
		y2Property().set(y);
	}

	@Override
	public void setZ2(double z) {
		z2Property().set(z);
	}

	@Pure
	@Override
	public double getX1() {
		return this.ax == null ? 0 : this.ax.get();
	}
	
	/** Replies the property that is the x coordinate of the first segment point.
	 *
	 * @return the x1 property.
	 */
	@Pure
	public DoubleProperty x1Property() {
		if (this.ax == null) {
			this.ax = new SimpleDoubleProperty(this, "x1"); //$NON-NLS-1$
		}
		return this.ax;
	}
	
	@Pure
	@Override
	public double getY1() {
		return this.ay == null ? 0 : this.ay.get();
	}
	
	/** Replies the property that is the y coordinate of the first segment point.
	 *
	 * @return the y1 property.
	 */
	@Pure
	public DoubleProperty y1Property() {
		if (this.ay == null) {
			this.ay = new SimpleDoubleProperty(this, "y1"); //$NON-NLS-1$
		}
		return this.ay;
	}

	@Pure
	@Override
	public double getZ1() {
		return this.az == null ? 0 : this.az.get();
	}

	/** Replies the property that is the z coordinate of the first segment point.
	 *
	 * @return the z1 property.
	 */
	@Pure
	public DoubleProperty z1Property() {
		if (this.az == null) {
			this.az = new SimpleDoubleProperty(this, "z1"); //$NON-NLS-1$
		}
		return this.az;
	}

	@Pure
	@Override
	public double getX2() {
		return this.bx == null ? 0 : this.bx.get();
	}

	/** Replies the property that is the x coordinate of the second segment point.
	 *
	 * @return the x2 property.
	 */
	@Pure
	public DoubleProperty x2Property() {
		if (this.bx == null) {
			this.bx = new SimpleDoubleProperty(this, "x2"); //$NON-NLS-1$
		}
		return this.bx;
	}
	
	@Pure
	@Override
	public double getY2() {
		return this.by == null ? 0 : this.by.get();
	}
	
	/** Replies the property that is the y coordinate of the second segment point.
	 *
	 * @return the y2 property.
	 */
	@Pure
	public DoubleProperty y2Property() {
		if (this.by == null) {
			this.by = new SimpleDoubleProperty(this, "y2"); //$NON-NLS-1$
		}
		return this.by;
	}

	@Pure
	@Override
	public double getZ2() {
		return this.bz == null ? 0 : this.bz.get();
	}

	/** Replies the property that is the z coordinate of the second segment point.
	 *
	 * @return the z2 property.
	 */
	@Pure
	public DoubleProperty z2Property() {
		if (this.bz == null) {
			this.bz = new SimpleDoubleProperty(this, "z2"); //$NON-NLS-1$
		}
		return this.bz;
	}

	@Override
	public Point3dfx getP1() {
		return getGeomFactory().newPoint(this.ax, this.ay, this.az);
	}

	@Override
	public Point3dfx getP2() {
		return getGeomFactory().newPoint(this.bx, this.by, this.bz);
	}

	@Override
	public ObjectProperty<RectangularPrism3dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, "boundingBox"); //$NON-NLS-1$
			this.boundingBox.bind(Bindings.createObjectBinding(
					() -> {
						return toBoundingBox();
					},
					x1Property(), y1Property(), z1Property(),
					x2Property(), y2Property(), z2Property()));
		}
		return this.boundingBox;
	}

}
