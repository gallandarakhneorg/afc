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

package org.arakhne.afc.math.geometry.d2.fx;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/** A triangle with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Triangle2fx
		extends AbstractShape2fx<Triangle2fx>
		implements Triangle2afp<Shape2fx<?>, Triangle2fx, PathElement2fx, Point2fx, Vector2fx, Rectangle2fx> {

	private static final long serialVersionUID = -1872758222696617883L;

	private DoubleProperty x1;
	
	private DoubleProperty y1;

	private DoubleProperty x2;
	
	private DoubleProperty y2;

	private DoubleProperty x3;
	
	private DoubleProperty y3;
	
	private ReadOnlyBooleanWrapper ccw;

	/**
	 */
	public Triangle2fx() {
		//
	}

	/**
	 * @param p1
	 * @param p2
	 * @param p3
	 */
	public Triangle2fx(Point2D<?, ?> p1, Point2D<?, ?> p2, Point2D<?, ?> p3) {
		assert (p1 != null) : "Point 1 must not be null"; //$NON-NLS-1$
		assert (p2 != null) : "Point 1 must not be null"; //$NON-NLS-1$
		assert (p3 != null) : "Point 1 must not be null"; //$NON-NLS-1$
		set(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 */
	public Triangle2fx(double x1, double y1, double x2, double y2, double x3, double y3) {
		set(x1, y1, x2, y2, x3, y3);
	}
	
	/** Construct a triangle from a triangle.
	 * @param t
	 */
	public Triangle2fx(Triangle2afp<?, ?, ?, ?, ?, ?> t) {
		assert (t != null) : "Triangle must be not null"; //$NON-NLS-1$
		set(t.getX1(), t.getY1(), t.getX2(), t.getY2(), t.getX3(), t.getY3());
	}
	
	@Override
	public Triangle2fx clone() {
		Triangle2fx clone = super.clone();
		if (clone.x1 != null) {
			clone.x1 = null;
			clone.x1Property().set(getX1());
		}
		if (clone.y1 != null) {
			clone.y1 = null;
			clone.y1Property().set(getY1());
		}
		if (clone.x2 != null) {
			clone.x2 = null;
			clone.x2Property().set(getX2());
		}
		if (clone.y2 != null) {
			clone.y2 = null;
			clone.y2Property().set(getY2());
		}
		if (clone.x3 != null) {
			clone.x3 = null;
			clone.x3Property().set(getX3());
		}
		if (clone.y3 != null) {
			clone.y3 = null;
			clone.y3Property().set(getY3());
		}
		clone.ccw = null;
		return clone;
	}
	
	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(getX1());
		bits = 31 * bits + Double.doubleToLongBits(getY1());
		bits = 31 * bits + Double.doubleToLongBits(getX2());
		bits = 31 * bits + Double.doubleToLongBits(getY2());
		bits = 31 * bits + Double.doubleToLongBits(getX3());
		bits = 31 * bits + Double.doubleToLongBits(getY3());
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
		b.append(getX2());
		b.append(";"); //$NON-NLS-1$
		b.append(getY2());
		b.append(";"); //$NON-NLS-1$
		b.append(getX3());
		b.append(";"); //$NON-NLS-1$
		b.append(getY3());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Pure
	@Override
	public Triangle2fx createTransformedShape(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return clone();
		}
		Point2fx point = new Point2fx(getX1(), getY1());
		transform.transform(point);
		double x1 = point.getX();
		double y1 = point.getY();
		point.set(getX2(), getY2());
		transform.transform(point);
		double x2 = point.getX();
		double y2 = point.getY();
		point.set(getX3(), getY3());
		transform.transform(point);
		return new Triangle2fx(x1, y1, x2, y2, point.getX(), point.getY());
	}

	@Override
	public double getX1() {
		return this.x1 == null ? 0 : this.x1.get();
	}

	/** Replies the property that is the x coordinate of the first triangle point.
	 *
	 * @return the x1 property.
	 */
	@Pure
	public DoubleProperty x1Property() {
		if (this.x1 == null) {
			this.x1 = new SimpleDoubleProperty(this, "x1"); //$NON-NLS-1$
		}
		return this.x1;
	}

	@Override
	public double getY1() {
		return this.y1 == null ? 0 : this.y1.get();
	}

	/** Replies the property that is the y coordinate of the first triangle point.
	 *
	 * @return the y1 property.
	 */
	@Pure
	public DoubleProperty y1Property() {
		if (this.y1 == null) {
			this.y1 = new SimpleDoubleProperty(this, "y1"); //$NON-NLS-1$
		}
		return this.y1;
	}

	@Override
	public double getX2() {
		return this.x2 == null ? 0 : this.x2.get();
	}

	/** Replies the property that is the x coordinate of the second triangle point.
	 *
	 * @return the x2 property.
	 */
	@Pure
	public DoubleProperty x2Property() {
		if (this.x2 == null) {
			this.x2 = new SimpleDoubleProperty(this, "x2"); //$NON-NLS-1$
		}
		return this.x2;
	}

	@Override
	public double getY2() {
		return this.y2 == null ? 0 : this.y2.get();
	}

	/** Replies the property that is the y coordinate of the second triangle point.
	 *
	 * @return the y2 property.
	 */
	@Pure
	public DoubleProperty y2Property() {
		if (this.y2 == null) {
			this.y2 = new SimpleDoubleProperty(this, "y2"); //$NON-NLS-1$
		}
		return this.y2;
	}

	@Override
	public double getX3() {
		return this.x3 == null ? 0 : this.x3.get();
	}

	/** Replies the property that is the x coordinate of the third triangle point.
	 *
	 * @return the x3 property.
	 */
	@Pure
	public DoubleProperty x3Property() {
		if (this.x3 == null) {
			this.x3 = new SimpleDoubleProperty(this, "x3"); //$NON-NLS-1$
		}
		return this.x3;
	}

	@Override
	public double getY3() {
		return this.y3 == null ? 0 : this.y3.get();
	}

	/** Replies the property that is the y coordinate of the third triangle point.
	 *
	 * @return the y3 property.
	 */
	@Pure
	public DoubleProperty y3Property() {
		if (this.y3 == null) {
			this.y3 = new SimpleDoubleProperty(this, "y3"); //$NON-NLS-1$
		}
		return this.y3;
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
	public void setX2(double x) {
		x2Property().set(x);
	}

	@Override
	public void setY2(double y) {
		y2Property().set(y);
	}

	@Override
	public void setX3(double x) {
		x3Property().set(x);
	}

	@Override
	public void setY3(double y) {
		y3Property().set(y);
	}

	@Override
	public void set(double x1, double y1, double x2, double y2, double x3, double y3) {
		x1Property().set(x1);
		y1Property().set(y1);
		x2Property().set(x2);
		y2Property().set(y2);
		x3Property().set(x3);
		y3Property().set(y3);
	}
	
	@Override
	public boolean isCCW() {
		return ccwProperty().get();
	}

	/** Replies the property that indictes if the triangle's points are defined in a counter-clockwise order.
	 *
	 * @return the ccw property.
	 */
	@Pure
	public ReadOnlyBooleanProperty ccwProperty() {
		if (this.ccw == null) {
			this.ccw = new ReadOnlyBooleanWrapper(this, "ccw"); //$NON-NLS-1$
			this.ccw.bind(Bindings.createBooleanBinding(
					() -> {
						return Triangle2afp.isCCWOrderDefinition(
								getX1(), getY1(), getX2(), getY2(),
								getX3(), getY3());
					},
					x1Property(), y1Property(),
					x2Property(), y2Property(),
					x3Property(), y3Property()));
		}
		return this.ccw.getReadOnlyProperty();
	}

	@Override
	public ObjectProperty<Rectangle2fx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, "boundingBox"); //$NON-NLS-1$
			this.boundingBox.bind(Bindings.createObjectBinding(
					() -> {
						return toBoundingBox();
					},
					x1Property(), y1Property(),
					x2Property(), y2Property(),
					x3Property(), y3Property()));
		}
		return this.boundingBox;
	}

}
