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
import org.arakhne.afc.math.geometry.d3.ad.Sphere3ad;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/** Circle with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Sphere3dfx
		extends AbstractShape3dfx<Sphere3dfx>
		implements Sphere3ad<Shape3dfx<?>, Sphere3dfx, PathElement3dfx, Point3dfx, Vector3dfx, RectangularPrism3dfx> {

	private static final long serialVersionUID = 837592010117981823L;

	private DoubleProperty centerX;
	
	private DoubleProperty centerY;

	private DoubleProperty centerZ;

	private DoubleProperty radius;

	/**
	 */
	public Sphere3dfx() {
		//
	}

	/**
	 * @param center
	 * @param radius
	 */
	public Sphere3dfx(Point3D<?, ?> center, double radius) {
		assert (center != null) : "Center must be not null"; //$NON-NLS-1$
		set(center.getX(), center.getY(), center.getZ(), radius);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 * @param radius
	 */
	public Sphere3dfx(double x, double y, double z, double radius) {
		set(x, y, z, radius);
	}
	
	/** Construct a circle from a circle.
	 * @param c
	 */
	public Sphere3dfx(Sphere3ad<?, ?, ?, ?, ?, ?> c) {
		assert (c != null) : "Circle must be not null"; //$NON-NLS-1$
		set(c.getX(), c.getY(), c.getZ(), c.getRadius());
	}

	@Pure
	@Override
	public Sphere3dfx clone() {
		Sphere3dfx clone = super.clone();
		if (clone.centerX != null) {
			clone.centerX = null;
			clone.xProperty().set(getX());
		}
		if (clone.centerY != null) {
			clone.centerY = null;
			clone.yProperty().set(getY());
		}
		if (clone.centerZ != null) {
			clone.centerZ = null;
			clone.zProperty().set(getZ());
		}
		if (clone.radius != null) {
			clone.radius = null;
			clone.radiusProperty().set(getRadius());
		}
		return clone;
	}
	
	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(getX());
		bits = 31 * bits + Double.doubleToLongBits(getY());
		bits = 31 * bits + Double.doubleToLongBits(getZ());
		bits = 31 * bits + Double.doubleToLongBits(getRadius());
		int b = (int) bits;
		return b ^ (b >> 32);
	}

	@Pure
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(getX());
		b.append(";"); //$NON-NLS-1$
		b.append(getY());
		b.append(";"); //$NON-NLS-1$
		b.append(getZ());
		b.append(";"); //$NON-NLS-1$
		b.append(getRadius());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Pure
	@Override
	public double getX() {
		return this.centerX == null ? 0 : this.centerX.get();
	}
	
	@Pure
	@Override
	public double getY() {
		return this.centerY == null ? 0 : this.centerY.get();
	}

	@Pure
	@Override
	public double getZ() {
		return this.centerZ == null ? 0 : this.centerZ.get();
	}

	@Pure
	@Override
	public Point3dfx getCenter() {
		return getGeomFactory().newPoint(xProperty(), yProperty(), zProperty());
	}

	@Override
	public void setX(double x) {
		xProperty().set(x);
	}
	
	@Override
	public void setY(double y) {
		yProperty().set(y);
	}

	@Override
	public void setZ(double z) {
		zProperty().set(z);
	}

	/** Replies the property that is the x coordinate of the circle center.
	 *
	 * @return the x property.
	 */
	@Pure
	public DoubleProperty xProperty() {
		if (this.centerX == null) {
			this.centerX = new SimpleDoubleProperty(this, "x"); //$NON-NLS-1$
		}
		return this.centerX;
	}
	
	/** Replies the property that is the y coordinate of the circle center.
	 *
	 * @return the y property.
	 */
	@Pure
	public DoubleProperty yProperty() {
		if (this.centerY == null) {
			this.centerY = new SimpleDoubleProperty(this, "y"); //$NON-NLS-1$
		}
		return this.centerY;
	}

	/** Replies the property that is the z coordinate of the circle center.
	 *
	 * @return the z property.
	 */
	@Pure
	public DoubleProperty zProperty() {
		if (this.centerZ == null) {
			this.centerZ = new SimpleDoubleProperty(this, "z"); //$NON-NLS-1$
		}
		return this.centerZ;
	}

	@Pure
	@Override
	public double getRadius() {
		return this.radius == null ? 0 : this.radius.get();
	}

	@Override
	public void setRadius(double radius) {
		assert (radius >= 0) : "Radius must be positive or zero"; //$NON-NLS-1$
		radiusProperty().set(radius);
	}

	/** Replies the property that is the radius of the circle.
	 *
	 * @return the radius property.
	 */
	@Pure
	public DoubleProperty radiusProperty() {
		if (this.radius == null) {
			this.radius = new SimpleDoubleProperty(this, "radius") { //$NON-NLS-1$
				@Override
				protected void invalidated() {
					if (get() < 0.) {
						set(0.);
					}
				}
			};
		}
		return this.radius;
	}

	@Override
	public void set(double x, double y, double z, double radius) {
		assert (radius >= 0) : "Radius must be positive or zero"; //$NON-NLS-1$
		xProperty().set(x);
		yProperty().set(y);
		zProperty().set(z);
		radiusProperty().set(radius);
	}

	@Override
	public ObjectProperty<RectangularPrism3dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, "boundingBox"); //$NON-NLS-1$
			this.boundingBox.bind(Bindings.createObjectBinding(
					() -> {
						return toBoundingBox();
					},
					xProperty(), yProperty(), zProperty(), radiusProperty()));
		}
		return this.boundingBox;
	}

}