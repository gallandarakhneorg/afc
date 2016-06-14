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
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A 2D segment/line with 2 integer FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment2ifx extends AbstractShape2ifx<Segment2ifx>
		implements Segment2ai<Shape2ifx<?>, Segment2ifx, PathElement2ifx, Point2ifx, Vector2ifx, Rectangle2ifx> {

	private static final long serialVersionUID = -1406743357357708790L;

	private IntegerProperty ax;

	private IntegerProperty ay;

	private IntegerProperty bx;

	private IntegerProperty by;

	/** Construct an empty segment.
	 */
	public Segment2ifx() {
		//
	}

	/** Construct a segment with the two given points.
	 * @param p1 first point.
	 * @param p2 second point.
	 */
	public Segment2ifx(Point2D<?, ?> p1, Point2D<?, ?> p2) {
		this(p1.ix(), p1.iy(), p2.ix(), p2.iy());
	}

	/** Construct by copy.
	 * @param segment the segment to copy.
	 */
	public Segment2ifx(Segment2ai<?, ?, ?, ?, ?, ?> segment) {
		this(segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2());
	}

	/** Construct a segment with the two given points.
	 * @param x1 x coordinate of the first point.
	 * @param y1 y coordinate of the first point.
	 * @param x2 x coordinate of the second point.
	 * @param y2 y coordinate of the second point.
	 */
	public Segment2ifx(int x1, int y1, int x2, int y2) {
		set(x1, y1, x2, y2);
	}

	@Override
	public Segment2ifx clone() {
		final Segment2ifx clone = super.clone();
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
		int bits = 1;
		bits = 31 * bits + Integer.hashCode(getX1());
		bits = 31 * bits + Integer.hashCode(getY1());
		bits = 31 * bits + Integer.hashCode(getX2());
		bits = 31 * bits + Integer.hashCode(getY2());
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public Shape2ifx<?> createTransformedShape(Transform2D transform) {
		assert transform != null : AssertMessages.notNullParameter();
		final Point2ifx point = getGeomFactory().newPoint(getX1(), getY1());
		transform.transform(point);
		final int x1 = point.ix();
		final int y1 = point.iy();
		point.set(getX2(), getY2());
		transform.transform(point);
		return getGeomFactory().newSegment(x1, y1, point.ix(), point.iy());
	}

	@Override
	public void set(int x1, int y1, int x2, int y2) {
		setX1(x1);
		setY1(y1);
		setX2(x2);
		setY2(y2);
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
	public void setX2(int x) {
		x2Property().set(x);
	}

	@Override
	public void setY2(int y) {
		y2Property().set(y);
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
			this.ax = new SimpleIntegerProperty(this, MathFXAttributeNames.X1);
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
			this.ay = new SimpleIntegerProperty(this, MathFXAttributeNames.Y1);
		}
		return this.ay;
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
			this.bx = new SimpleIntegerProperty(this, MathFXAttributeNames.X2);
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
			this.by = new SimpleIntegerProperty(this, MathFXAttributeNames.Y2);
		}
		return this.by;
	}

	@Override
	public Point2ifx getP1() {
		return getGeomFactory().newPoint(this.ax, this.ay);
	}

	@Override
	public Point2ifx getP2() {
		return getGeomFactory().newPoint(this.bx, this.by);
	}

	@Override
	public ObjectProperty<Rectangle2ifx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() -> toBoundingBox(),
					x1Property(), y1Property(),
					x2Property(), y2Property()));
		}
		return this.boundingBox;
	}

}
