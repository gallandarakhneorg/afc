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

package org.arakhne.afc.math.geometry.d3.ifx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.ai.Segment3ai;

/** A 3D segment/line with 3 integer FX properties.
 *
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment3ifx extends AbstractShape3ifx<Segment3ifx>
	implements Segment3ai<Shape3ifx<?>, Segment3ifx, PathElement3ifx, Point3ifx, Vector3ifx, RectangularPrism3ifx> {

	private static final long serialVersionUID = -1406743357357708790L;

	private Point3ifx p1 = new Point3ifx();

	private Point3ifx p2 = new Point3ifx();

	/** Construct an empty segment.
     */
	public Segment3ifx() {
		//
	}

	/** Construct a segment with the two given points.
	 * @param p1 first point.
	 * @param p2 second point.
	 */
	public Segment3ifx(Point3D<?, ?> p1, Point3D<?, ?> p2) {
	    this(p1.ix(), p1.iy(), p1.iz(), p2.ix(), p2.iy(), p2.iz());
	}

	/** Construct a segment with the two given points.
     * @param p1 first point.
     * @param p2 second point.
     */
	public Segment3ifx(Point3ifx p1, Point3ifx p2) {
	    this.p1 = p1;
	    this.p2 = p2;
	}

	/** Construct by copy.
	 * @param segment the segment to copy.
	 */
	public Segment3ifx(Segment3ai<?, ?, ?, ?, ?, ?> segment) {
	    this(segment.getX1(), segment.getY1(), segment.getZ1(), segment.getX2(), segment.getY2(), segment.getZ2());
	}

	/** Construct by copy.
     * @param segment the segment to copy.
     */
	public Segment3ifx(Segment3ifx segment) {
	    this.p1 = segment.p1;
	    this.p2 = segment.p2;
	}

	/** Construct a segment with the two given points.
     * @param x1 x coordinate of the first point.
     * @param y1 y coordinate of the first point.
     * @param z1 z coordinate of the first point.
     * @param x2 x coordinate of the second point.
     * @param y2 y coordinate of the second point.
     * @param z2 z coordinate of the second point.
     */
	public Segment3ifx(int x1, int y1, int z1, int x2, int y2, int z2) {
		set(x1, y1, z1, x2, y2, z2);
	}

	@Override
	public Segment3ifx clone() {
		final Segment3ifx clone = super.clone();
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
		int bits = 1;
		bits = 31 * bits + getX1();
		bits = 31 * bits + getY1();
		bits = 31 * bits + getZ1();
		bits = 31 * bits + getX2();
		bits = 31 * bits + getY2();
		bits = 31 * bits + getZ2();
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public Shape3ifx<?> createTransformedShape(Transform3D transform) {
        if (transform == null || transform.isIdentity()) {
            return clone();
        }
		final Point3ifx point = getGeomFactory().newPoint(getX1(), getY1(), getZ1());
		transform.transform(point);
		final int x1 = point.ix();
		final int y1 = point.iy();
		final int z1 = point.iz();
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
		return this.p1.x == null ? 0 : this.p1.x.get();
	}

	/** Replies the property that is the x coordinate of the first segment point.
	 *
	 * @return the x1 property.
	 */
	@Pure
	public IntegerProperty x1Property() {
		if (this.p1.x == null) {
			this.p1.x = new SimpleIntegerProperty(this, MathFXAttributeNames.X1);
		}
		return this.p1.x;
	}

	@Pure
	@Override
	public int getY1() {
		return this.p1.y == null ? 0 : this.p1.y.get();
	}

	/** Replies the property that is the y coordinate of the first segment point.
	 *
	 * @return the y1 property.
	 */
	@Pure
	public IntegerProperty y1Property() {
		if (this.p1.y == null) {
			this.p1.y = new SimpleIntegerProperty(this, MathFXAttributeNames.Y1);
		}
		return this.p1.y;
	}

	@Pure
	@Override
	public int getZ1() {
		return this.p1.z == null ? 0 : this.p1.z.get();
	}

	/** Replies the property that is the z coordinate of the first segment point.
	 *
	 * @return the z1 property.
	 */
	@Pure
	public IntegerProperty z1Property() {
		if (this.p1.z == null) {
			this.p1.z = new SimpleIntegerProperty(this, MathFXAttributeNames.Z1);
		}
		return this.p1.z;
	}

	@Pure
	@Override
	public int getX2() {
		return this.p2.x == null ? 0 : this.p2.x.get();
	}

	/** Replies the property that is the x coordinate of the second segment point.
	 *
	 * @return the x2 property.
	 */
	@Pure
	public IntegerProperty x2Property() {
		if (this.p2.x == null) {
			this.p2.x = new SimpleIntegerProperty(this, MathFXAttributeNames.X2);
		}
		return this.p2.x;
	}

	@Pure
	@Override
	public int getY2() {
		return this.p2.y == null ? 0 : this.p2.y.get();
	}

	/** Replies the property that is the y coordinate of the second segment point.
	 *
	 * @return the y2 property.
	 */
	@Pure
	public IntegerProperty y2Property() {
		if (this.p2.y == null) {
			this.p2.y = new SimpleIntegerProperty(this, MathFXAttributeNames.Y2);
		}
		return this.p2.y;
	}

	@Pure
	@Override
	public int getZ2() {
		return this.p2.z == null ? 0 : this.p2.z.get();
	}

	/** Replies the property that is the z coordinate of the second segment point.
	 *
	 * @return the z2 property.
	 */
	@Pure
	public IntegerProperty z2Property() {
		if (this.p2.z == null) {
			this.p2.z = new SimpleIntegerProperty(this, MathFXAttributeNames.Z2);
		}
		return this.p2.z;
	}

	@Override
	public Point3ifx getP1() {
		return this.p1;
	}

	@Override
	public void setP1(int x, int y, int z) {
	    this.p1.set(x, y, z);
	}

	@Override
	public void setP1(Point3D<?, ?> point) {
	    this.p1.setX(point.getX());
	    this.p1.setY(point.getY());
	    this.p1.setZ(point.getZ());
	}

	/** Set the point as the first point of this segment.
	 *
	 * @param point the point to set.
	 */
	public void setP1(Point3ifx point) {
	    this.p1 = point;
	}

	@Override
	public Point3ifx getP2() {
		return this.p2;
	}

	@Override
	public void setP2(int x, int y, int z) {
	    this.p2.set(x, y, z);
	}

	@Override
	public void setP2(Point3D<?, ?> point) {
	    this.p2.setX(point.getX());
	    this.p2.setY(point.getY());
	    this.p2.setZ(point.getZ());
	}

	/** Set the point as the second point of this segment.
	 *
	 * @param point the point to set.
	 */
	public void setP2(Point3ifx point) {
	    this.p2 = point;
	}

	@Override
	public ObjectProperty<RectangularPrism3ifx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() ->
			    toBoundingBox(),
			        x1Property(), y1Property(), z1Property(),
			        x2Property(), y2Property(), z2Property()));
		}
		return this.boundingBox;
	}

}
