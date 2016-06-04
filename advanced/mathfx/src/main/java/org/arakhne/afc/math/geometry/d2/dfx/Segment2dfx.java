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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Segment with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment2dfx extends AbstractShape2dfx<Segment2dfx>
		implements Segment2afp<Shape2dfx<?>, Segment2dfx, PathElement2dfx, Point2dfx, Vector2dfx, Rectangle2dfx> {

	private static final long serialVersionUID = -5603953934276693947L;

	private DoubleProperty ax;

	private DoubleProperty ay;

	private DoubleProperty bx;

	private DoubleProperty by;

	/** Construct an empty segment.
	 */
	public Segment2dfx() {
		//
	}

	/** Construct a segment with the two given points.
	 * @param p1 first point.
	 * @param p2 second point.
	 */
	public Segment2dfx(Point2D<?, ?> p1, Point2D<?, ?> p2) {
		this(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	/** Constructor by copy.
	 * @param segment the segment to copy.
	 */
	public Segment2dfx(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
		this(segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2());
	}

	/** Construct a segment with the two given points.
	 * @param x1 x coordinate of the first point.
	 * @param y1 y coordinate of the first point.
	 * @param x2 x coordinate of the second point.
	 * @param y2 y coordinate of the second point.
	 */
	public Segment2dfx(double x1, double y1, double x2, double y2) {
		set(x1, y1, x2, y2);
	}

	@Override
	public Segment2dfx clone() {
		final Segment2dfx clone = super.clone();
		if (clone.ax != null) {
			clone.ax = null;
			clone.x1Property().set(getX1());
		}
		if (clone.ay != null) {
			clone.ay = null;
			clone.y1Property().set(getY1());
		}
		if (clone.bx != null) {
			clone.bx = null;
			clone.x2Property().set(getX2());
		}
		if (clone.by != null) {
			clone.by = null;
			clone.y2Property().set(getY2());
		}
		return clone;
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.hashCode(getX1());
		bits = 31 * bits + Double.hashCode(getY1());
		bits = 31 * bits + Double.hashCode(getX2());
		bits = 31 * bits + Double.hashCode(getY2());
		final int b = (int) bits;
		return b ^ (b >> 31);
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("("); //$NON-NLS-1$
		b.append(getX1());
		b.append(", "); //$NON-NLS-1$
		b.append(getY1());
		b.append(")-("); //$NON-NLS-1$
		b.append(getX2());
		b.append(", "); //$NON-NLS-1$
		b.append(getY2());
		b.append(")"); //$NON-NLS-1$
		return b.toString();
	}

	@Pure
	@Override
	public Segment2dfx createTransformedShape(Transform2D transform) {
		assert transform != null : AssertMessages.notNullParameter();
		final Point2dfx point = getGeomFactory().newPoint(getX1(), getY1());
		transform.transform(point);
		final double x1 = point.getX();
		final double y1 = point.getY();
		point.set(getX2(), getY2());
		transform.transform(point);
		return getGeomFactory().newSegment(x1, y1, point.getX(), point.getY());
	}

	@Override
	public void set(double x1, double y1, double x2, double y2) {
		setX1(x1);
		setY1(y1);
		setX2(x2);
		setY2(y2);
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
			this.ax = new SimpleDoubleProperty(this, MathFXAttributeNames.X1);
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
			this.ay = new SimpleDoubleProperty(this, MathFXAttributeNames.Y1);
		}
		return this.ay;
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
			this.bx = new SimpleDoubleProperty(this, MathFXAttributeNames.X2);
		}
		return this.bx;
	}

	@Pure
	@Override
	public double getY2() {
		return this.by == null ? 0 : this.by.get();
	}

	/** Replies the property that is the x coordinate of the second segment point.
	 *
	 * @return the x2 property.
	 */
	@Pure
	public DoubleProperty y2Property() {
		if (this.by == null) {
			this.by = new SimpleDoubleProperty(this, MathFXAttributeNames.Y2);
		}
		return this.by;
	}

	@Override
	public Point2dfx getP1() {
		return getGeomFactory().newPoint(this.ax, this.ay);
	}

	@Override
	public Point2dfx getP2() {
		return getGeomFactory().newPoint(this.bx, this.by);
	}

	@Override
	public ObjectProperty<Rectangle2dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, "boundingBox"); //$NON-NLS-1$
			this.boundingBox.bind(Bindings.createObjectBinding(() -> {
				return toBoundingBox();
			},
					x1Property(), y1Property(),
					x2Property(), y2Property()));
		}
		return this.boundingBox;
	}

}
