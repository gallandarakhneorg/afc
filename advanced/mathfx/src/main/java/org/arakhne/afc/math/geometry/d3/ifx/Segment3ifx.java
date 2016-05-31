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

package org.arakhne.afc.math.geometry.d3.ifx;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.ai.Segment3ai;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/** A 3D segment/line with 3 integer FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment3ifx extends AbstractShape3ifx<Segment3ifx>
	implements Segment3ai<Shape3ifx<?>, Segment3ifx, PathElement3ifx, Point3ifx, Vector3ifx, RectangularPrism3ifx> {

	private static final long serialVersionUID = -1406743357357708790L;

	private IntegerProperty ax;
	
	private IntegerProperty ay;

	private IntegerProperty az;

	private IntegerProperty bx;
	
	private IntegerProperty by;

	private IntegerProperty bz;
	
	/**
	 */
	public Segment3ifx() {
		//
	}

	/**
	 * @param a
	 * @param b
	 */
	public Segment3ifx(Point3D<?, ?> a, Point3D<?, ?> b) {
		this(a.ix(), a.iy(), a.iz(), b.ix(), b.iy(), b.iz());
	}

	/**
	 * @param s
	 */
	public Segment3ifx(Segment3ai<?, ?, ?, ?, ?, ?> s) {
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
	public Segment3ifx(int x1, int y1, int z1, int x2, int y2, int z2) {
		set(x1, y1, z1, x2, y2, z2);
	}
	
	@Override
	public Segment3ifx clone() {
		Segment3ifx clone = super.clone();
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
		int bits = 1;
		bits = 31 * bits + getX1();
		bits = 31 * bits + getY1();
		bits = 31 * bits + getZ1();
		bits = 31 * bits + getX2();
		bits = 31 * bits + getY2();
		bits = 31 * bits + getZ2();
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
	public Shape3ifx<?> createTransformedShape(Transform3D transform) {
		assert (transform != null) : "Transformation must be not null"; //$NON-NLS-1$
		Point3ifx point = getGeomFactory().newPoint(getX1(), getY1(), getZ1());
		transform.transform(point);
		int x1 = point.ix();
		int y1 = point.iy();
		int z1 = point.iz();
		point.set(getX2(), getY2(), getZ2());
		transform.transform(point);
		return getGeomFactory().newSegment(x1, y1, z1, point.ix(), point.iy(), point.iz());
	}
	
	@Override
	public void set(int x1, int y1, int z1, int x2, int y2, int z2) {
		setX1(x1);
		setY1(y1);
		setZ1(z1);
		setX2(x2);
		setY2(y2);
		setZ2(z2);
	}

	@Override
	public void setX1(int x) {
		x1Property().set(x);
	}
	
	@Override
	public void setY1(int y) {
		y1Property().set(y);
	}

	@Override
	public void setZ1(int z) {
		z1Property().set(z);
	}

	@Override
	public void setX2(int x) {
		x2Property().set(x);
	}
	
	@Override
	public void setY2(int y) {
		y2Property().set(y);
	}

	@Override
	public void setZ2(int z) {
		z2Property().set(z);
	}

	@Pure
	@Override
	public int getX1() {
		return this.ax == null ? 0 : this.ax.get();
	}

	/** Replies the property that is the x coordinate of the first segment point.
	 *
	 * @return the x1 property.
	 */
	@Pure
	public IntegerProperty x1Property() {
		if (this.ax == null) {
			this.ax = new SimpleIntegerProperty(this, "x1"); //$NON-NLS-1$
		}
		return this.ax;
	}
	
	@Pure
	@Override
	public int getY1() {
		return this.ay == null ? 0 : this.ay.get();
	}
	
	/** Replies the property that is the y coordinate of the first segment point.
	 *
	 * @return the y1 property.
	 */
	@Pure
	public IntegerProperty y1Property() {
		if (this.ay == null) {
			this.ay = new SimpleIntegerProperty(this, "y1"); //$NON-NLS-1$
		}
		return this.ay;
	}
	
	@Pure
	@Override
	public int getZ1() {
		return this.az == null ? 0 : this.az.get();
	}

	/** Replies the property that is the z coordinate of the first segment point.
	 *
	 * @return the z1 property.
	 */
	@Pure
	public IntegerProperty z1Property() {
		if (this.az == null) {
			this.az = new SimpleIntegerProperty(this, "z1"); //$NON-NLS-1$
		}
		return this.az;
	}

	@Pure
	@Override
	public int getX2() {
		return this.bx == null ? 0 : this.bx.get();
	}

	/** Replies the property that is the x coordinate of the second segment point.
	 *
	 * @return the x2 property.
	 */
	@Pure
	public IntegerProperty x2Property() {
		if (this.bx == null) {
			this.bx = new SimpleIntegerProperty(this, "x2"); //$NON-NLS-1$
		}
		return this.bx;
	}
	
	@Pure
	@Override
	public int getY2() {
		return this.by == null ? 0 : this.by.get();
	}
	
	/** Replies the property that is the y coordinate of the second segment point.
	 *
	 * @return the y2 property.
	 */
	@Pure
	public IntegerProperty y2Property() {
		if (this.by == null) {
			this.by = new SimpleIntegerProperty(this, "y2"); //$NON-NLS-1$
		}
		return this.by;
	}

	@Pure
	@Override
	public int getZ2() {
		return this.bz == null ? 0 : this.bz.get();
	}

	/** Replies the property that is the z coordinate of the second segment point.
	 *
	 * @return the z2 property.
	 */
	@Pure
	public IntegerProperty z2Property() {
		if (this.bz == null) {
			this.bz = new SimpleIntegerProperty(this, "z2"); //$NON-NLS-1$
		}
		return this.bz;
	}

	@Override
	public Point3ifx getP1() {
		return getGeomFactory().newPoint(this.ax, this.ay, this.az);
	}

	@Override
	public Point3ifx getP2() {
		return getGeomFactory().newPoint(this.bx, this.by, this.bz);
	}

	@Override
	public ObjectProperty<RectangularPrism3ifx> boundingBoxProperty() {
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