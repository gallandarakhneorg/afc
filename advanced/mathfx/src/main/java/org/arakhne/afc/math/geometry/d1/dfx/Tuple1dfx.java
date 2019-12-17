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

package org.arakhne.afc.math.geometry.d1.dfx;

import java.lang.ref.WeakReference;
import java.util.Objects;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d1.GeomFactory1D;
import org.arakhne.afc.math.geometry.d1.Point1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.Vector1D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 1.5D tuple with 2 double precision floating-point FX properties.
 *
 * @param <RT> is the type of the data returned by the tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class Tuple1dfx<RT extends Tuple1dfx<? super RT>> implements Tuple2D<RT> {

	private static final long serialVersionUID = 7855806823055588137L;

	/** segment reference.
	 */
	ObjectProperty<WeakReference<Segment1D<?, ?>>> segment;

	/** x coordinate.
	 */
	DoubleProperty x;

	/** y coordinate.
	 */
	DoubleProperty y;

	/** Construct a zero tuple.
	 */
	public Tuple1dfx() {
		//
	}

	/** Construct a zero tuple.
	 *
	 * @param segment the segment associated to the tuple.
	 * @param x curviline coordinate.
	 * @param y shift distance.
	 */
	public Tuple1dfx(ObjectProperty<WeakReference<Segment1D<?, ?>>> segment, DoubleProperty x, DoubleProperty y) {
		this.segment = segment;
		this.x = x;
		this.y = y;
	}

	/** Construct a zero tuple.
	 *
	 * @param segment the segment.
	 */
	public Tuple1dfx(Segment1D<?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		segmentProperty().set(new WeakReference<>(segment));
	}

	/** Constructor.
	 * @param segment the segment.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple1dfx(Segment1D<?, ?> segment, Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert segment != null : AssertMessages.notNullParameter();
		segmentProperty().set(new WeakReference<>(segment));
		xProperty().set(tuple.getX());
		yProperty().set(tuple.getY());
	}

	/** Constructor.
	 * @param segment the segment.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple1dfx(Segment1D<?, ?> segment, int[] tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert tuple.length >= 2 : AssertMessages.tooSmallArrayParameter(tuple.length, 2);
		assert segment != null : AssertMessages.notNullParameter();
		segmentProperty().set(new WeakReference<>(segment));
		xProperty().set(tuple[0]);
		yProperty().set(tuple[1]);
	}

	/** Constructor.
	 * @param segment the segment.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple1dfx(Segment1D<?, ?> segment, double[] tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert tuple.length >= 2 : AssertMessages.tooSmallArrayParameter(tuple.length, 2);
		assert segment != null : AssertMessages.notNullParameter();
		segmentProperty().set(new WeakReference<>(segment));
		xProperty().set(tuple[0]);
		yProperty().set(tuple[1]);
	}

	/** Construct a tuple with the given coordinates.
	 * @param segment the segment.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Tuple1dfx(Segment1D<?, ?> segment, int x, int y) {
		assert segment != null : AssertMessages.notNullParameter();
		segmentProperty().set(new WeakReference<>(segment));
		xProperty().set(x);
		yProperty().set(y);
	}

	/** Construct a tuple with the given coordinates.
	 * @param segment the segment.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Tuple1dfx(Segment1D<?, ?> segment, double x, double y) {
		assert segment != null : AssertMessages.notNullParameter();
		segmentProperty().set(new WeakReference<>(segment));
		xProperty().set(x);
		yProperty().set(y);
	}

	/** Replies the segment property.
	 *
	 * @return the segment property.
	 */
	@Pure
	public ObjectProperty<WeakReference<Segment1D<?, ?>>> segmentProperty() {
		if (this.segment == null) {
			this.segment = new SimpleObjectProperty<>(this, MathFXAttributeNames.SEGMENT);
		}
		return this.segment;
	}

	/** Replies the x property.
	 *
	 * @return the x property.
	 */
	@Pure
	public DoubleProperty xProperty() {
		if (this.x == null) {
			this.x = new SimpleDoubleProperty(this, MathFXAttributeNames.X);
		}
		return this.x;
	}

	/** Replies the y property.
	 *
	 * @return the y property.
	 */
	@Pure
	public DoubleProperty yProperty() {
		if (this.y == null) {
			this.y = new SimpleDoubleProperty(this, MathFXAttributeNames.Y);
		}
		return this.y;
	}

	/** Replies the segment.
	 *
	 * @return the segment or <code>null</code> if the weak reference has lost the segment.
	 */
	@Pure
	public Segment1D<?, ?> getSegment() {
		if (this.segment == null) {
			return null;
		}
		final WeakReference<Segment1D<?, ?>> ref = segmentProperty().get();
		if (ref == null) {
			return null;
		}
		return ref.get();
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public RT clone() {
		try {
			final RT clone = (RT) super.clone();
			if (clone.segment != null) {
				clone.segment = null;
				clone.segmentProperty().set(new WeakReference<>(getSegment()));
			} else {
				clone.segment = null;
			}
			if (clone.x != null) {
				clone.x = null;
				clone.xProperty().set(getX());
			}
			if (clone.y != null) {
				clone.y = null;
				clone.yProperty().set(getY());
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	/** Replies the geometry factory.
	 *
	 * @return the factory.
	 */
	@SuppressWarnings("static-method")
	public GeomFactory1D<Vector1dfx, Point1dfx> getGeomFactory() {
		return GeomFactory1dfx.SINGLETON;
	}

	/** Set the segment.
	 *
	 * @param segment is the segment.
	 */
	public void setSegment(Segment1D<?, ?> segment) {
		segmentProperty().set(new WeakReference<>(segment));
	}

	/** Change the x and y properties.
	 *
	 * @param segment the new segment property.
	 * @param x the new x property.
	 * @param y the new y property.
	 */
	void set(ObjectProperty<WeakReference<Segment1D<?, ?>>> segment, DoubleProperty x, DoubleProperty y) {
		this.segment = segment;
		this.x = x;
		this.y = y;
	}

	/** Change the attributes of the tuple.
	 *
	 * @param segment the segment.
	 * @param curviline the curviline coordinate.
	 * @param shift the shift distance.
	 */
	public void set(Segment1D<?, ?> segment, double curviline, double shift) {
		assert segment != null : AssertMessages.notNullParameter(0);
		segmentProperty().set(new WeakReference<>(segment));
		xProperty().set(curviline);
		yProperty().set(shift);
	}

	@Override
	public void set(Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		DoubleProperty prop;
		prop = xProperty();
		prop.set(tuple.getX());
		prop = yProperty();
		prop.set(tuple.getY());
	}

	@Override
	public void set(int x, int y) {
		DoubleProperty prop;
		prop = xProperty();
		prop.set(x);
		prop = yProperty();
		prop.set(y);
	}

	@Override
	public void set(double x, double y) {
		DoubleProperty prop;
		prop = xProperty();
		prop.set(x);
		prop = yProperty();
		prop.set(y);
	}

	@Override
	public void set(int[] tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert tuple.length >= 2 : AssertMessages.tooSmallArrayParameter(tuple.length, 2);
		DoubleProperty prop;
		prop = xProperty();
		prop.set(tuple[0]);
		prop = yProperty();
		prop.set(tuple[1]);
	}

	@Override
	public void set(double[] tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert tuple.length >= 2 : AssertMessages.tooSmallArrayParameter(tuple.length, 2);
		DoubleProperty prop;
		prop = xProperty();
		prop.set(tuple[0]);
		prop = yProperty();
		prop.set(tuple[1]);
	}

	@Pure
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (object instanceof Vector1D<?, ?, ?>) {
			final Point1D<?, ?, ?> tuple = (Point1D<?, ?, ?>) object;
			return tuple.getSegment() == getSegment() && tuple.getX() == getX() && tuple.getY() == getY();
		}
		if (object instanceof Point1D<?, ?, ?>) {
			final Point1D<?, ?, ?> tuple = (Point1D<?, ?, ?>) object;
			return tuple.getSegment() == getSegment() && tuple.getX() == getX() && tuple.getY() == getY();
		}
		if (object instanceof Tuple2D<?>) {
			final Tuple2D<?> tuple = (Tuple2D<?>) object;
			return tuple.getX() == getX() && tuple.getY() == getY();
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Objects.hashCode(getSegment());
		bits = 31 * bits + Double.hashCode(getX());
		bits = 31 * bits + Double.hashCode(getY());
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public String toString() {
		final JsonBuffer objectDescription = new JsonBuffer();
		toJson(objectDescription);
        return objectDescription.toString();
	}

	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add("x", getX()); //$NON-NLS-1$
		buffer.add("y", getY()); //$NON-NLS-1$
	}

	@Override
	public double getX() {
		return this.x == null ? 0 : this.x.doubleValue();
	}

	@Override
	public int ix() {
		return this.x == null ? 0 : this.x.intValue();
	}

	@Override
	public void setX(int x) {
		xProperty().set(x);
	}

	@Override
	public void setX(double x) {
		xProperty().set(x);
	}

	@Override
	public double getY() {
		return this.y == null ? 0 : this.y.doubleValue();
	}

	@Override
	public int iy() {
		return this.y == null ? 0 : this.y.intValue();
	}

	@Override
	public void setY(int y) {
		yProperty().set(y);
	}

	@Override
	public void setY(double y) {
		yProperty().set(y);
	}

	@Override
	public void absolute() {
		if (this.x != null) {
			final DoubleProperty prop = xProperty();
			prop.set(Math.abs(prop.get()));
		}
		if (this.y != null) {
			final DoubleProperty prop = yProperty();
			prop.set(Math.abs(prop.get()));
		}
	}

	@Override
	public void absolute(Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		tuple.set(Math.abs(xProperty().get()), Math.abs(yProperty().get()));
	}

	@Override
	public void add(int x, int y) {
		DoubleProperty prop;
		prop = xProperty();
		prop.set(prop.get() + x);
		prop = yProperty();
		prop.set(prop.get() + y);
	}

	@Override
	public void add(double x, double y) {
		DoubleProperty prop;
		prop = xProperty();
		prop.set(prop.get() + x);
		prop = yProperty();
		prop.set(prop.get() + y);
	}

	@Override
	public void addX(int x) {
		final DoubleProperty prop = xProperty();
		prop.set(prop.get() + x);
	}

	@Override
	public void addX(double x) {
		final DoubleProperty prop = xProperty();
		prop.set(prop.get() + x);
	}

	@Override
	public void addY(int y) {
		final DoubleProperty prop = yProperty();
		prop.set(prop.get() + y);
	}

	@Override
	public void addY(double y) {
		final DoubleProperty prop = yProperty();
		prop.set(prop.get() + y);
	}

	@Override
	public void negate(Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		DoubleProperty prop;
		prop = xProperty();
		prop.set(-tuple.getX());
		prop = yProperty();
		prop.set(-tuple.getY());
	}

	@Override
	public void negate() {
		DoubleProperty prop;
		prop = xProperty();
		prop.set(-prop.get());
		prop = yProperty();
		prop.set(-prop.get());
	}

	@Override
	public void scale(int scale, Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter(1);
		DoubleProperty prop;
		prop = xProperty();
		prop.set(scale * tuple.getX());
		prop = yProperty();
		prop.set(scale * tuple.getY());
	}

	@Override
	public void scale(double scale, Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter(1);
		DoubleProperty prop;
		prop = xProperty();
		prop.set(scale * tuple.getX());
		prop = yProperty();
		prop.set(scale * tuple.getY());
	}

	@Override
	public void scale(int scale) {
		DoubleProperty prop;
		prop = xProperty();
		prop.set(scale * prop.get());
		prop = yProperty();
		prop.set(scale * prop.get());
	}

	@Override
	public void scale(double scale) {
		DoubleProperty prop;
		prop = xProperty();
		prop.set(scale * prop.get());
		prop = yProperty();
		prop.set(scale * prop.get());
	}

	@Override
	public void sub(int x, int y) {
		DoubleProperty prop;
		prop = xProperty();
		prop.set(prop.get() - x);
		prop = yProperty();
		prop.set(prop.get() - y);
	}

	@Override
	public void sub(double x, double y) {
		DoubleProperty prop;
		prop = xProperty();
		prop.set(prop.get() - x);
		prop = yProperty();
		prop.set(prop.get() - y);
	}

	@Override
	public void subX(int x) {
		final DoubleProperty prop = xProperty();
		prop.set(prop.get() - x);
	}

	@Override
	public void subX(double x) {
		final DoubleProperty prop = xProperty();
		prop.set(prop.get() - x);
	}

	@Override
	public void subY(int y) {
		final DoubleProperty prop = yProperty();
		prop.set(prop.get() - y);
	}

	@Override
	public void subY(double y) {
		final DoubleProperty prop = yProperty();
		prop.set(prop.get() - y);
	}

}
