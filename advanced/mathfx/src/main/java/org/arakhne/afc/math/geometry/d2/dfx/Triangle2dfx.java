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

package org.arakhne.afc.math.geometry.d2.dfx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;

/** A triangle with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Triangle2dfx
		extends AbstractShape2dfx<Triangle2dfx>
		implements Triangle2afp<Shape2dfx<?>, Triangle2dfx, PathElement2dfx, Point2dfx, Vector2dfx, Rectangle2dfx> {
	private static final long serialVersionUID = -1872758222696617883L;

	/**
	 * Literal constant.
	 */
	private static final String POINT_ONE_NOT_NULL = "Point 1 must not be null"; //$NON-NLS-1$

	private DoubleProperty x1;

	private DoubleProperty y1;

	private DoubleProperty x2;

	private DoubleProperty y2;

	private DoubleProperty x3;

	private DoubleProperty y3;

	private ReadOnlyBooleanWrapper ccw;

	/** Construct an empty triangle.
	 */
	public Triangle2dfx() {
		//
	}

	/** Construct a triangle with the three given points.
	 * @param p1 first point.
	 * @param p2 second point.
	 * @param p3 third point.
	 */
	public Triangle2dfx(Point2D<?, ?> p1, Point2D<?, ?> p2, Point2D<?, ?> p3) {
		assert p1 != null : POINT_ONE_NOT_NULL;
		assert p2 != null : POINT_ONE_NOT_NULL;
		assert p3 != null : POINT_ONE_NOT_NULL;
		set(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
	}

	/** Construct a triangle with the three given points.
	 * @param x1 x coordinate of the first point.
	 * @param y1 y coordinate of the first point.
	 * @param x2 x coordinate of the second point.
	 * @param y2 y coordinate of the second point.
	 * @param x3 x coordinate of the third point.
	 * @param y3 y coordinate of the third point.
	 */
	public Triangle2dfx(double x1, double y1, double x2, double y2, double x3, double y3) {
		set(x1, y1, x2, y2, x3, y3);
	}

	/** Construct a triangle from a triangle.
	 * @param triangle the triangle to copy.
	 */
	public Triangle2dfx(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
		assert triangle != null : "Triangle must be not null"; //$NON-NLS-1$
		set(triangle.getX1(), triangle.getY1(), triangle.getX2(), triangle.getY2(), triangle.getX3(), triangle.getY3());
	}

	@Override
	public Triangle2dfx clone() {
		final Triangle2dfx clone = super.clone();
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
		final int b = (int) bits;
		return b ^ (b >> 31);
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
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
	public Triangle2dfx createTransformedShape(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return clone();
		}
		final Point2dfx point = getGeomFactory().newPoint(getX1(), getY1());
		transform.transform(point);
		final double x1 = point.getX();
		final double y1 = point.getY();
		point.set(getX2(), getY2());
		transform.transform(point);
		final double x2 = point.getX();
		final double y2 = point.getY();
		point.set(getX3(), getY3());
		transform.transform(point);
		return getGeomFactory().newTriangle(x1, y1, x2, y2, point.getX(), point.getY());
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
			this.ccw.bind(Bindings.createBooleanBinding(() -> {
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
	public ObjectProperty<Rectangle2dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, "boundingBox"); //$NON-NLS-1$
			this.boundingBox.bind(Bindings.createObjectBinding(() -> {
				return toBoundingBox();
			},
					x1Property(), y1Property(),
					x2Property(), y2Property(),
					x3Property(), y3Property()));
		}
		return this.boundingBox;
	}

}
