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
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;

/** A 2D segment/line encapsulating points with 2 integer FX properties.
 *
 *  <p>This segment is defined by its two extremities. It should not differ from
 *  the original Segment2ifx except from storage type.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment2ifx extends AbstractShape2ifx<Segment2ifx>
	implements Segment2ai<Shape2ifx<?>, Segment2ifx, PathElement2ifx, Point2ifx, Vector2ifx, Rectangle2ifx> {

	private static final long serialVersionUID = -5603953934276693947L;

	private Point2ifx p1 = new Point2ifx();

	private Point2ifx p2 = new Point2ifx();

	/** Construct an empty segment.
	 */
	public Segment2ifx() {
		//
	}

	/** Construct a segment with the two given points.
	 * @param p1 first point.
	 * @param p2 second point.
	 */
	public Segment2ifx(Point2ifx p1, Point2ifx p2) {
	    this.p1 = p1;
	    this.p2 = p2;
	}

	/** Construct a segment with the two given points.
     * @param p1 first point.
     * @param p2 second point.
     */
	public Segment2ifx(Point2D<?, ?> p1, Point2D<?, ?> p2) {
		this(p1.ix(), p1.iy(), p2.ix(), p2.iy());
	}

	/** Constructor by copy.
	 * @param segment the segment to copy.
	 */
	public Segment2ifx(Segment2ifx segment) {
	    this.p1 = segment.p1;
	    this.p2 = segment.p2;
	}

	/** Constructor by copy.
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
		if (clone.p1 != null) {
			clone.p1 = null;
			clone.p1 = this.p1.clone();
		}
		if (clone.p2 != null) {
			clone.p2 = null;
			clone.p2 = this.p2.clone();
		}
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
		final int b = (int) bits;
		return b ^ (b >> 32);
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(getX1());
		b.append(";"); //$NON-NLS-1$
		b.append(getY1());
		b.append("|"); //$NON-NLS-1$
		b.append(getX2());
		b.append(";"); //$NON-NLS-1$
		b.append(getY2());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Pure
	@Override
	public Segment2ifx createTransformedShape(Transform2D transform) {
		assert transform != null : "Transformation must be not null"; //$NON-NLS-1$
		final Point2ifx point = getGeomFactory().newPoint(getX1(), getY1());
		transform.transform(point);
		final int x1 = point.ix();
		final int y1 = point.iy();
		point.set(getX2(), getY2());
		transform.transform(point);
		return new Segment2ifx(x1, y1, point.ix(), point.iy());
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
		return this.p1.ix();
	}

	/** Replies the property that is the x coordinate of the first segment point.
	 *
	 * @return the x1 property.
	 */
	@Pure
	public IntegerProperty x1Property() {
		return this.p1.xProperty();
	}

	@Pure
	@Override
	public int getY1() {
		return this.p1.iy();
	}

	/** Replies the property that is the y coordinate of the first segment point.
	 *
	 * @return the y1 property.
	 */
	@Pure
	public IntegerProperty y1Property() {
		return this.p1.yProperty();
	}

	@Pure
	@Override
	public int getX2() {
		return this.p2.ix();
	}

	/** Replies the property that is the x coordinate of the second segment point.
	 *
	 * @return the x2 property.
	 */
	@Pure
	public IntegerProperty x2Property() {
		return this.p2.xProperty();
	}

	@Pure
	@Override
	public int getY2() {
		return this.p2.iy();
	}

	/** Replies the property that is the x coordinate of the second segment point.
	 *
	 * @return the x2 property.
	 */
	@Pure
	public IntegerProperty y2Property() {
		return this.p2.yProperty();
	}

	@Override
	public Point2ifx getP1() {
		return this.p1;
	}

	@Override
	public Point2ifx getP2() {
		return this.p2;
	}

	@Override
	public void setP1(int x, int y) {
		this.p1.set(x, y);
	}

	@Override
	public void setP1(Point2D<?, ?> pt) {
		this.p1.setX(pt.ix());
		this.p1.setY(pt.iy());
	}

	/** Set the oriented point as the first point of this SegmentPoint2dfx to preserve length.
	 * @param pt the point.
	 */
	public void setP1(OrientedPoint2ifx pt) {
	    this.p1 = pt;
	}

	@Override
	public void setP2(int x, int y) {
		this.p2.set(x, y);
	}

	@Override
	public void setP2(Point2D<?, ?> pt) {
		this.p2.setX(pt.ix());
		this.p2.setY(pt.iy());
	}

	/** Set the oriented point as the second point of this SegmentPoint2dfx to preserve length.
	 * @param pt the point.
	 */
	public void setP2(OrientedPoint2ifx pt) {
	    this.p2 = pt;
	}

	@Override
	public ObjectProperty<Rectangle2ifx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, "boundingBox"); //$NON-NLS-1$
			this.boundingBox.bind(Bindings.createObjectBinding(() -> {
			    return toBoundingBox();
			}, x1Property(), y1Property(),
			   x2Property(), y2Property()));
		}
		return this.boundingBox;
	}

}
