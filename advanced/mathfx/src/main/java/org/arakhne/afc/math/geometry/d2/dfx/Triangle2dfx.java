/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

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

	private Point2dfx p1 = new Point2dfx();

	private Point2dfx p2 = new Point2dfx();

	private Point2dfx p3 = new Point2dfx();

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
	    assert p1 != null : AssertMessages.notNullParameter(0);
	    assert p2 != null : AssertMessages.notNullParameter(1);
	    assert p3 != null : AssertMessages.notNullParameter(2);
	    set(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
	}

	/** Construct a triangle by setting the three given points.
	 * @param point1 the point to set as first point.
	 * @param point2 the point to set as second point.
	 * @param point3 the point to set as third point.
	 */
	public Triangle2dfx(Point2dfx point1, Point2dfx point2, Point2dfx point3) {
		assert point1 != null : AssertMessages.notNullParameter(0);
		assert point2 != null : AssertMessages.notNullParameter(1);
		assert point3 != null : AssertMessages.notNullParameter(2);
		this.p1 = point1;
		this.p2 = point2;
		this.p3 = point3;
	}

	/** Constructor by copy.
	 * @param triangle the triangle to copy.
	 */
	public Triangle2dfx(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
	    assert triangle != null : AssertMessages.notNullParameter();
	    set(triangle.getX1(), triangle.getY1(), triangle.getX2(), triangle.getY2(), triangle.getX3(), triangle.getY3());
	}

	/** Constructor by setting.
	 * @param triangle the triangle to set.
	 */
	public Triangle2dfx(Triangle2dfx triangle) {
		assert triangle != null : AssertMessages.notNullParameter();
		this.p1 = triangle.p1;
		this.p2 = triangle.p2;
		this.p3 = triangle.p3;
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

	@Override
	public Triangle2dfx clone() {
		final Triangle2dfx clone = super.clone();
		if (clone.p1 != null) {
			clone.p1 = null;
			clone.p1 = this.p1.clone();
		}
		if (clone.p2 != null) {
			clone.p2 = null;
			clone.p2 = this.p2.clone();
		}
		if (clone.p3 != null) {
			clone.p3 = null;
			clone.p3 = this.p3.clone();
		}
		clone.ccw = null;
		return clone;
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(getX1());
		bits = 31 * bits + Double.hashCode(getY1());
		bits = 31 * bits + Double.hashCode(getX2());
		bits = 31 * bits + Double.hashCode(getY2());
		bits = 31 * bits + Double.hashCode(getX3());
		bits = 31 * bits + Double.hashCode(getY3());
        return bits ^ (bits >> 31);
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
		return this.p1.getX();
	}

	/** Replies the property that is the x coordinate of the first triangle point.
	 *
	 * @return the x1 property.
	 */
	@Pure
	public DoubleProperty x1Property() {
		return this.p1.xProperty();
	}

	@Override
	public double getY1() {
		return this.p1.getY();
	}

	/** Replies the property that is the y coordinate of the first triangle point.
	 *
	 * @return the y1 property.
	 */
	@Pure
	public DoubleProperty y1Property() {
		return this.p1.yProperty();
	}

	@Override
	public double getX2() {
		return this.p2.getX();
	}

	/** Replies the property that is the x coordinate of the second triangle point.
	 *
	 * @return the x2 property.
	 */
	@Pure
	public DoubleProperty x2Property() {
		return this.p2.xProperty();
	}

	@Override
	public double getY2() {
		return this.p2.getY();
	}

	/** Replies the property that is the y coordinate of the second triangle point.
	 *
	 * @return the y2 property.
	 */
	@Pure
	public DoubleProperty y2Property() {
		return this.p2.yProperty();
	}

	@Override
	public double getX3() {
		return this.p3.getX();
	}

	/** Replies the property that is the x coordinate of the third triangle point.
	 *
	 * @return the x3 property.
	 */
	@Pure
	public DoubleProperty x3Property() {
		return this.p3.xProperty();
	}

	@Override
	public double getY3() {
		return this.p3.getY();
	}

	/** Replies the property that is the y coordinate of the third triangle point.
	 *
	 * @return the y3 property.
	 */
	@Pure
	public DoubleProperty y3Property() {
		return this.p3.yProperty();
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
			this.ccw = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.CCW);
			this.ccw.bind(Bindings.createBooleanBinding(() ->
				Triangle2afp.isCCW(
						getX1(), getY1(), getX2(), getY2(),
						getX3(), getY3()),
					x1Property(), y1Property(),
					x2Property(), y2Property(),
					x3Property(), y3Property()));
		}
		return this.ccw.getReadOnlyProperty();
	}

	@Override
	public ObjectProperty<Rectangle2dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() ->
				toBoundingBox(),
					x1Property(), y1Property(),
					x2Property(), y2Property(),
					x3Property(), y3Property()));
		}
		return this.boundingBox;
	}

}
