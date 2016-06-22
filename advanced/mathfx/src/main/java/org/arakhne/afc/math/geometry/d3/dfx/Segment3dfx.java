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

package org.arakhne.afc.math.geometry.d3.dfx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;

/** A 3D segment/line with 3 double precision FX properties.
 *
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment3dfx extends AbstractShape3dfx<Segment3dfx>
	implements Segment3afp<Shape3dfx<?>, Segment3dfx, PathElement3dfx, Point3dfx, Vector3dfx, RectangularPrism3dfx> {

	private static final long serialVersionUID = -5603953934276693947L;

	private Point3dfx p1 = new Point3dfx();

	private Point3dfx p2 = new Point3dfx();

	/** Construct an empty segment.
     */
	public Segment3dfx() {
		//
	}

	/** Construct a segment with the two given points.
	 * @param p1 first point.
	 * @param p2 second point.
	 */
	public Segment3dfx(Point3D<?, ?> p1, Point3D<?, ?> p2) {
	    this(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ());
	}

	/** Construct a segment with the two given points.
     * @param p1 first point.
     * @param p2 second point.
     */
	public Segment3dfx(Point3dfx p1, Point3dfx p2) {
	    this.p1 = p1;
	    this.p2 = p2;
	}

	/** Constructor by copy.
	 * @param segment the segment to copy.
	 */
	public Segment3dfx(Segment3afp<?, ?, ?, ?, ?, ?> segment) {
	    this(segment.getX1(), segment.getY1(), segment.getZ1(), segment.getX2(), segment.getY2(), segment.getZ2());
	}

	/** Constructor by copy.
     * @param segment the segment to copy.
     */
	public Segment3dfx(Segment3dfx segment) {
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
	public Segment3dfx(double x1, double y1, double z1, double x2, double y2, double z2) {
		set(x1, y1, z1, x2, y2, z2);
	}

	@Override
	public Segment3dfx clone() {
		final Segment3dfx clone = super.clone();
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
		bits = 31 * bits + Double.hashCode(getX1());
		bits = 31 * bits + Double.hashCode(getY1());
		bits = 31 * bits + Double.hashCode(getZ1());
		bits = 31 * bits + Double.hashCode(getX2());
		bits = 31 * bits + Double.hashCode(getY2());
		bits = 31 * bits + Double.hashCode(getZ2());
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public Segment3dfx createTransformedShape(Transform3D transform) {
        if (transform == null || transform.isIdentity()) {
            return clone();
        }
		final Point3dfx point = getGeomFactory().newPoint(getX1(), getY1(), getZ1());
		transform.transform(point);
		final double x1 = point.getX();
		final double y1 = point.getY();
		final double z1 = point.getZ();
		point.set(getX2(), getY2(), getZ2());
		transform.transform(point);
		return new Segment3dfx(x1, y1, z1, point.getX(), point.getY(), point.getZ());
	}

	@Override
	public void set(double x1, double y1, double z1, double x2, double y2, double z2) {
		setX1(x1);
		setY1(y1);
		setZ1(z1);
		setX2(x2);
		setY2(y2);
		setZ2(z2);
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
	public void setZ1(double z) {
		z1Property().set(z);
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
	public void setZ2(double z) {
		z2Property().set(z);
	}

	@Pure
	@Override
	public double getX1() {
		return this.p1.x == null ? 0 : this.p1.x.get();
	}

	/** Replies the property that is the x coordinate of the first segment point.
	 *
	 * @return the x1 property.
	 */
	@Pure
	public DoubleProperty x1Property() {
		if (this.p1.x == null) {
			this.p1.x = new SimpleDoubleProperty(this, MathFXAttributeNames.X1);
		}
		return this.p1.x;
	}

	@Pure
	@Override
	public double getY1() {
		return this.p1.y == null ? 0 : this.p1.y.get();
	}

	/** Replies the property that is the y coordinate of the first segment point.
	 *
	 * @return the y1 property.
	 */
	@Pure
	public DoubleProperty y1Property() {
		if (this.p1.y == null) {
			this.p1.y = new SimpleDoubleProperty(this, MathFXAttributeNames.Y1);
		}
		return this.p1.y;
	}

	@Pure
	@Override
	public double getZ1() {
		return this.p1.z == null ? 0 : this.p1.z.get();
	}

	/** Replies the property that is the z coordinate of the first segment point.
	 *
	 * @return the z1 property.
	 */
	@Pure
	public DoubleProperty z1Property() {
		if (this.p1.z == null) {
			this.p1.z = new SimpleDoubleProperty(this, MathFXAttributeNames.Z1);
		}
		return this.p1.z;
	}

	@Pure
	@Override
	public double getX2() {
		return this.p2.x == null ? 0 : this.p2.x.get();
	}

	/** Replies the property that is the x coordinate of the second segment point.
	 *
	 * @return the x2 property.
	 */
	@Pure
	public DoubleProperty x2Property() {
		if (this.p2.x == null) {
			this.p2.x = new SimpleDoubleProperty(this, MathFXAttributeNames.X2);
		}
		return this.p2.x;
	}

	@Pure
	@Override
	public double getY2() {
		return this.p2.y == null ? 0 : this.p2.y.get();
	}

	/** Replies the property that is the y coordinate of the second segment point.
	 *
	 * @return the y2 property.
	 */
	@Pure
	public DoubleProperty y2Property() {
		if (this.p2.y == null) {
			this.p2.y = new SimpleDoubleProperty(this, MathFXAttributeNames.Y2);
		}
		return this.p2.y;
	}

	@Pure
	@Override
	public double getZ2() {
		return this.p2.z == null ? 0 : this.p2.z.get();
	}

	/** Replies the property that is the z coordinate of the second segment point.
	 *
	 * @return the z2 property.
	 */
	@Pure
	public DoubleProperty z2Property() {
		if (this.p2.z == null) {
			this.p2.z = new SimpleDoubleProperty(this, MathFXAttributeNames.Z2);
		}
		return this.p2.z;
	}

	@Override
	public Point3dfx getP1() {
		return this.p1;
	}

    @Override
    public void setP1(double x, double y, double z) {
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
    public void setP1(Point3dfx point) {
        this.p1 = point;
    }

	@Override
	public Point3dfx getP2() {
		return this.p2;
	}

    @Override
    public void setP2(double x, double y, double z) {
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
    public void setP2(Point3dfx point) {
        this.p2 = point;
    }

	@Override
	public ObjectProperty<RectangularPrism3dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() -> toBoundingBox(),
			        x1Property(), y1Property(), z1Property(),
			        x2Property(), y2Property(), z2Property()));
		}
		return this.boundingBox;
	}

}
