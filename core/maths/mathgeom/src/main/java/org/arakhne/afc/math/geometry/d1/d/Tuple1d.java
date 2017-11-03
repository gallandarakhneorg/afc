/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d1.d;

import java.lang.ref.WeakReference;
import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d1.GeomFactory1D;
import org.arakhne.afc.math.geometry.d1.Point1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.Vector1D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** 1.5D tuple with 2 double precision floating-point numbers.
 *
 * @param <RT> is the type of the data returned by the tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class Tuple1d<RT extends Tuple1d<? super RT>> implements Tuple2D<RT> {

	private static final long serialVersionUID = -7422015889188383352L;

	/** x coordinate.
	 */
	protected WeakReference<Segment1D<?, ?>> segment;

	/** x coordinate.
	 */
	protected double x;

	/** y coordinate.
	 */
	protected double y;

	/** Construct a zero tuple.
	 */
	public Tuple1d() {
		this.segment = new WeakReference<>(null);
	}

	/** Construct a zero tuple.
	 *
	 * @param segment the segment.
	 */
	public Tuple1d(Segment1D<?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		this.segment = new WeakReference<>(segment);
	}

	/** Constructor.
	 * @param segment the segment.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple1d(Segment1D<?, ?> segment, Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert segment != null : AssertMessages.notNullParameter();
		this.segment = new WeakReference<>(segment);
		this.x = tuple.getX();
		this.y = tuple.getY();
	}

	/** Constructor.
	 * @param segment the segment.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple1d(Segment1D<?, ?> segment, int[] tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert tuple.length >= 2 : AssertMessages.tooSmallArrayParameter(tuple.length, 2);
		assert segment != null : AssertMessages.notNullParameter();
		this.segment = new WeakReference<>(segment);
		this.x = tuple[0];
		this.y = tuple[1];
	}

	/** Constructor.
	 * @param segment the segment.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple1d(Segment1D<?, ?> segment, double[] tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert tuple.length >= 2 : AssertMessages.tooSmallArrayParameter(tuple.length, 2);
		assert segment != null : AssertMessages.notNullParameter();
		this.segment = new WeakReference<>(segment);
		this.x = tuple[0];
		this.y = tuple[1];
	}

	/** Construct a tuple with the given coordinates.
	 * @param segment the segment.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Tuple1d(Segment1D<?, ?> segment, int x, int y) {
		assert segment != null : AssertMessages.notNullParameter();
		this.segment = new WeakReference<>(segment);
		this.x = x;
		this.y = y;
	}

	/** Construct a tuple with the given coordinates.
	 * @param segment the segment.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Tuple1d(Segment1D<?, ?> segment, double x, double y) {
		assert segment != null : AssertMessages.notNullParameter();
		this.segment = new WeakReference<>(segment);
		this.x = x;
		this.y = y;
	}

	/** Replies the geometry factory.
	 *
	 * @return the factory.
	 */
	@SuppressWarnings("static-method")
	public GeomFactory1D<Vector1d, Point1d> getGeomFactory() {
		return GeomFactory1d.SINGLETON;
	}

	/** Replies the segment.
	 *
	 * @return the segment or <code>null</code> if the weak reference has lost the segment.
	 */
	@Pure
	public Segment1D<?, ?> getSegment() {
		return this.segment.get();
	}

	/** Set the segment.
	 *
	 * @param segment is the segment.
	 */
	public void setSegment(Segment1D<?, ?> segment) {
		this.segment = new WeakReference<>(segment);
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public RT clone() {
		try {
			return (RT) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public void absolute() {
		this.x = Math.abs(this.x);
		this.y = Math.abs(this.y);
	}

	@Override
	public void absolute(Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		tuple.set(Math.abs(this.x), Math.abs(this.y));
	}

	@Override
	public void add(int x, int y) {
		this.x += x;
		this.y += y;
	}

	@Override
	public void add(double x, double y) {
		this.x += x;
		this.y += y;
	}

	@Override
	public void addX(int x) {
		this.x += x;
	}

	@Override
	public void addX(double x) {
		this.x += x;
	}

	@Override
	public void addY(int y) {
		this.y += y;
	}

	@Override
	public void addY(double y) {
		this.y += y;
	}

	@Override
	public void negate(Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		this.x = -tuple.getX();
		this.y = -tuple.getY();
	}

	@Override
	public void negate() {
		this.x = -this.x;
		this.y = -this.y;
	}

	@Override
	public void scale(int scale, Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter(1);
		this.x = scale * tuple.getX();
		this.y = scale * tuple.getY();
	}

	@Override
	public void scale(double scale, Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter(1);
		this.x = scale * tuple.getX();
		this.y = scale * tuple.getY();
	}

	@Override
	public void scale(int scale) {
		this.x = scale * this.x;
		this.y = scale * this.y;
	}

	@Override
	public void scale(double scale) {
		this.x = scale * this.x;
		this.y = scale * this.y;
	}

	/** Change the attributes of the tuple.
	 *
	 * @param segment the segment.
	 * @param curviline the curviline coordinate.
	 * @param shift the shift distance.
	 */
	public void set(Segment1D<?, ?> segment, double curviline, double shift) {
		assert segment != null : AssertMessages.notNullParameter(0);
		this.segment = new WeakReference<>(segment);
		this.x = curviline;
		this.y = shift;
	}

	@Override
	public void set(Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		this.x = tuple.getX();
		this.y = tuple.getY();
	}

	@Override
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void set(int[] tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert tuple.length >= 2 : AssertMessages.tooSmallArrayParameter(tuple.length, 2);
		this.x = tuple[0];
		this.y = tuple[1];
	}

	@Override
	public void set(double[] tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert tuple.length >= 2 : AssertMessages.tooSmallArrayParameter(tuple.length, 2);
		this.x = tuple[0];
		this.y = tuple[1];
	}

	@Pure
	@Override
	public double getX() {
		return this.x;
	}

	@Pure
	@Override
	public int ix() {
		return (int) this.x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Pure
	@Override
	public double getY() {
		return this.y;
	}

	@Pure
	@Override
	public int iy() {
		return (int) this.y;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public void sub(int x, int y) {
		this.x -= x;
		this.y -= y;
	}

	@Override
	public void sub(double x, double y) {
		this.x -= x;
		this.y -= y;
	}

	@Override
	public void subX(int x) {
		this.x -= x;
	}

	@Override
	public void subX(double x) {
		this.x -= x;
	}

	@Override
	public void subY(int y) {
		this.y -= y;
	}

	@Override
	public void subY(double y) {
		this.y -= y;
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
		bits = 31 * bits + Objects.hashCode(this.segment.get());
		bits = 31 * bits + Double.hashCode(this.x);
		bits = 31 * bits + Double.hashCode(this.y);
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public String toString() {
		return Point1D.toString(this.segment.get(), this.x, this.y);
	}

}
