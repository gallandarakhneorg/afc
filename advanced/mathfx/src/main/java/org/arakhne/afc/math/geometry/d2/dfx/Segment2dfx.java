/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A 2D segment/line encapsulating points with 2 double precision FX properties.
 *
 *  <p>This segment is defined by its two extremities. It should not differ from
 *  the original Segment2dfx except from storage type.
 * @author $Author: tpiotrow$
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

	private Point2dfx p1 = new Point2dfx();

	private Point2dfx p2 = new Point2dfx();

	/** Construct an empty segment.
	 */
	public Segment2dfx() {
		//
	}

	/** Constructor by setting the two given points.
	 *
	 * @param p1 the point to set as first point.
	 * @param p2 the point to set as second point.
	 */
	public Segment2dfx(Point2dfx p1, Point2dfx p2) {
	    assert p1 != null : AssertMessages.notNullParameter(0);
	    assert p2 != null : AssertMessages.notNullParameter(1);
	    this.p1 = p1;
	    this.p2 = p2;
	}

	/** Construct a segment with the two given points.
     * @param p1 first point.
     * @param p2 second point.
     */
	public Segment2dfx(Point2D<?, ?> p1, Point2D<?, ?> p2) {
	    assert p1 != null : AssertMessages.notNullParameter(0);
	    assert p2 != null : AssertMessages.notNullParameter(1);
		set(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	/** Constructor by setting.
	 * @param segment the segment to set.
	 */
	public Segment2dfx(Segment2dfx segment) {
	    assert segment != null : AssertMessages.notNullParameter();
	    this.p1 = segment.p1;
	    this.p2 = segment.p2;
	}

	/** Constructor by copy.
     * @param segment the segment to copy.
     */
	public Segment2dfx(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
	    assert segment != null : AssertMessages.notNullParameter();
		set(segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2());
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
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(getX1());
		bits = 31 * bits + Double.doubleToLongBits(getY1());
		bits = 31 * bits + Double.doubleToLongBits(getX2());
		bits = 31 * bits + Double.doubleToLongBits(getY2());
		final int b = (int) bits;
		return  b ^ (b >> 31);
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
	public Segment2dfx createTransformedShape(Transform2D transform) {
        if (transform == null || transform.isIdentity()) {
            return clone();
        }
		final Point2dfx point = getGeomFactory().newPoint(getX1(), getY1());
		transform.transform(point);
		final double x1 = point.getX();
		final double y1 = point.getY();
		point.set(getX2(), getY2());
		transform.transform(point);
		return new Segment2dfx(x1, y1, point.getX(), point.getY());
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
		return this.p1.getX();
	}

	/** Replies the property that is the x coordinate of the first segment point.
	 *
	 * @return the x1 property.
	 */
	@Pure
	public DoubleProperty x1Property() {
		return this.p1.xProperty();
	}

	@Pure
	@Override
	public double getY1() {
		return this.p1.getY();
	}

	/** Replies the property that is the y coordinate of the first segment point.
	 *
	 * @return the y1 property.
	 */
	@Pure
	public DoubleProperty y1Property() {
		return this.p1.yProperty();
	}

	@Pure
	@Override
	public double getX2() {
		return this.p2.getX();
	}

	/** Replies the property that is the x coordinate of the second segment point.
	 *
	 * @return the x2 property.
	 */
	@Pure
	public DoubleProperty x2Property() {
		return this.p2.xProperty();
	}

	@Pure
	@Override
	public double getY2() {
		return this.p2.getY();
	}

	/** Replies the property that is the x coordinate of the second segment point.
	 *
	 * @return the x2 property.
	 */
	@Pure
	public DoubleProperty y2Property() {
		return this.p2.yProperty();
	}

	@Override
	public Point2dfx getP1() {
		return this.p1;
	}

	@Override
	public void setP1(double x, double y) {
	    this.p1.set(x, y);
	}

	@Override
	public void setP1(Point2D<?, ?> pt) {
	    this.p1.setX(pt.getX());
	    this.p1.setY(pt.getY());
	}

	/** Set the oriented point as the first point of this Segment2dfx to preserve length.
	 * @param pt the point.
	 */
	public void setP1(Point2dfx pt) {
	    this.p1 = pt;
	}

	@Override
	public Point2dfx getP2() {
		return this.p2;
	}

	@Override
	public void setP2(double x, double y) {
		this.p2.set(x, y);
	}

	@Override
	public void setP2(Point2D<?, ?> pt) {
		this.p2.setX(pt.getX());
		this.p2.setY(pt.getY());
	}

	/** Set the oriented point as the second point of this Segment2dfx to preserve length.
	 * @param pt the point.
	 */
	public void setP2(Point2dfx pt) {
	    this.p2 = pt;
	}

	@Override
	public ObjectProperty<Rectangle2dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() -> {
			    return toBoundingBox();
			}, x1Property(), y1Property(),
			   x2Property(), y2Property()));
		}
		return this.boundingBox;
	}

}
