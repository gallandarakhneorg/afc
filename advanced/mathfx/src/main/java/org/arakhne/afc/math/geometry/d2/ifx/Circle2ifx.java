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

package org.arakhne.afc.math.geometry.d2.ifx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.ai.Circle2ai;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A circle with 2 integer FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Circle2ifx
		extends AbstractShape2ifx<Circle2ifx>
		implements Circle2ai<Shape2ifx<?>, Circle2ifx, PathElement2ifx, Point2ifx, Vector2ifx, Rectangle2ifx> {

	private static final long serialVersionUID = 3750916959512063017L;

	private Point2ifx center = new Point2ifx();

	private IntegerProperty radius;

	/** Construct an empty circle.
	 */
	public Circle2ifx() {
		//
	}

	/** Construct a circle at the given position and with the given radius.
	 * @param center the center of the circle.
	 * @param radius the radius of the circle.
	 */
	public Circle2ifx(Point2D<?, ?> center, int radius) {
		set(center.ix(), center.iy(), radius);
	}

	/** Construct a circle at the given position and with the given radius.
	 * @param x x coordinate of the center of the circle.
	 * @param y y coordinate of the center of the circle.
	 * @param radius the radius of the circle.
	 */
	public Circle2ifx(int x, int y, int radius) {
		set(x, y, radius);
	}

	/** Construct a circle from a circle.
	 * @param circle the circle to copy.
	 */
	public Circle2ifx(Circle2ai<?, ?, ?, ?, ?, ?> circle) {
		assert circle != null : AssertMessages.notNullParameter();
		set(circle.getX(), circle.getY(), circle.getRadius());
	}

	@Override
	public Circle2ifx clone() {
		final Circle2ifx clone = super.clone();
		if (clone.center != null) {
			clone.center = this.center.clone();
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
		int bits = 1;
		bits = 31 * bits + Integer.hashCode(getX());
		bits = 31 * bits + Integer.hashCode(getY());
		bits = 31 * bits + Integer.hashCode(getRadius());
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public int getX() {
		return this.center.ix();
	}

	/** Replies the property that is the x coordinate of the circle center.
	 *
	 * @return the x property.
	 */
	@Pure
	public IntegerProperty xProperty() {
		return this.center.xProperty();
	}

	@Pure
	@Override
	public int getY() {
		return this.center.iy();
	}

	/** Replies the property that is the y coordinate of the circle center.
	 *
	 * @return the y property.
	 */
	@Pure
	public IntegerProperty yProperty() {
		return this.center.yProperty();
	}

	@Override
	public void setX(int x) {
		this.center.setX(x);
	}

	@Override
	public void setY(int y) {
		this.center.setY(y);
	}

	@Override
	public Point2ifx getCenter() {
	    return this.center;
	}

	@Override
	public void setCenter(int x, int y) {
	    this.center.set(x, y);
	}

	@Override
	public void setCenter(Point2D<?, ?> center) {
	    this.center = getGeomFactory().convertToPoint(center);
	}

	@Pure
	@Override
	public int getRadius() {
		return this.radius == null ? 0 : this.radius.get();
	}

	@Override
	public void setRadius(int radius) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter();
		radiusProperty().set(radius);
	}

	/** Replies the property that is the radius of the circle.
	 *
	 * @return the radius property.
	 */
	@Pure
	public IntegerProperty radiusProperty() {
		if (this.radius == null) {
			this.radius = new SimpleIntegerProperty(this, MathFXAttributeNames.RADIUS) {
				@Override
				protected void invalidated() {
					if (get() < 0) {
						set(0);
					}
				}
			};
		}
		return this.radius;
	}

	@Override
	public void set(int x, int y, int radius) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(2);
		xProperty().set(x);
		yProperty().set(y);
		radiusProperty().set(radius);
	}

	@Override
	public ObjectProperty<Rectangle2ifx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() ->
				toBoundingBox(), xProperty(), yProperty(), radiusProperty()));
		}
		return this.boundingBox;
	}

}
