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

package org.arakhne.afc.math.geometry.d1.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d1.GeomFactory1D;
import org.arakhne.afc.math.geometry.d1.Point1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.UnmodifiableVector1D;
import org.arakhne.afc.math.geometry.d1.Vector1D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 1.5D Vector with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class Vector1d extends Tuple1d<Vector1d> implements Vector1D<Vector1d, Point1d, Segment1D<?, ?>> {

	private static final long serialVersionUID = 6219852825470812955L;

	/** Construct a zero vector.
	 *
	 * @param tuple the tuple to copy.
	 */
	public Vector1d(Vector1D<?, ?, ?> tuple) {
		super(tuple.getSegment(), tuple.getX(), tuple.getY());
	}

	/** Construct a zero vector.
	 *
	 * @param tuple the tuple to copy.
	 */
	public Vector1d(Point1D<?, ?, ?> tuple) {
		super(tuple.getSegment(), tuple.getX(), tuple.getY());
	}

	/** Construct a zero vector.
	 *
	 * @param segment the segment associated to the vector.
	 */
	public Vector1d(Segment1D<?, ?> segment) {
		super(segment);
	}

	/** Construct a zero vector.
	 */
	public Vector1d() {
		super();
	}

	/** Constructor by copy.
	 *
	 * @param segment the segment associated to the vector.
	 * @param tuple is the tuple to copy.
	 */
	public Vector1d(Segment1D<?, ?> segment, Tuple2D<?> tuple) {
		super(segment, tuple);
	}

	/** Constructor by copy.
	 * @param segment the segment associated to the vector.
	 * @param tuple is the tuple to copy.
	 */
	public Vector1d(Segment1D<?, ?> segment, int[] tuple) {
		super(segment, tuple);
	}

	/** Constructor by copy.
	 * @param segment the segment associated to the vector.
	 * @param tuple is the tuple to copy.
	 */
	public Vector1d(Segment1D<?, ?> segment, double[] tuple) {
		super(segment, tuple);
	}

	/** Construct a vector with the given coordinates.
	 * @param segment the segment associated to the vector.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector1d(Segment1D<?, ?> segment, int x, int y) {
		super(segment, x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param segment the segment associated to the vector.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector1d(Segment1D<?, ?> segment, float x, float y) {
		super(segment, x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param segment the segment associated to the vector.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector1d(Segment1D<?, ?> segment, double x, double y) {
		super(segment, x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param segment the segment associated to the vector.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector1d(Segment1D<?, ?> segment, long x, long y) {
		super(segment, x, y);
	}

	/** Convert the given tuple to a real Vector1d.
	 *
	 * <p>If the given tuple is already a Vector1d, it is replied.
	 *
	 * @param tuple the tuple.
	 * @return the Vector1d.
	 * @since 14.0
	 */
	public static Vector1d convert(Tuple1d<?> tuple) {
		if (tuple instanceof Vector1d) {
			return (Vector1d) tuple;
		}
		return new Vector1d(tuple.getSegment(), tuple.getX(), tuple.getY());
	}

	@Pure
	@Override
	public double getLength() {
		return Math.hypot(this.x, this.y);
	}

	@Pure
	@Override
	public double getLengthSquared() {
		return this.x * this.x + this.y * this.y;
	}

	@Override
	public void add(Vector1D<?, ?, ?> vector1, Vector1D<?, ?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		this.x = vector1.getX() + vector2.getX();
		this.y = vector1.getY() + vector2.getY();
	}

	@Override
	public void add(Vector1D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = this.x + vector.getX();
		this.y = this.y + vector.getY();
	}

	@Override
	public void scaleAdd(int scale, Vector1D<?, ?, ?> vector1, Vector1D<?, ?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		this.x = scale * vector1.getX() + vector2.getX();
		this.y = scale * vector1.getY() + vector2.getY();
	}

	@Override
	public void scaleAdd(double scale, Vector1D<?, ?, ?> vector1, Vector1D<?, ?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		this.x = scale * vector1.getX() + vector2.getX();
		this.y = scale * vector1.getY() + vector2.getY();
	}

	@Override
	public void scaleAdd(int scale, Vector1D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = scale * this.x + vector.getX();
		this.y = scale * this.y + vector.getY();
	}

	@Override
	public void scaleAdd(double scale, Vector1D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = scale * this.x + vector.getX();
		this.y = scale * this.y + vector.getY();
	}

	@Override
	public void sub(Vector1D<?, ?, ?> vector1, Vector1D<?, ?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		this.x = vector1.getX() - vector2.getX();
		this.y = vector1.getY() - vector2.getY();
	}

	@Override
	public void sub(Point1D<?, ?, ?> point1, Point1D<?, ?, ?> point2) {
		assert point1 != null : AssertMessages.notNullParameter(0);
		assert point2 != null : AssertMessages.notNullParameter(1);
		this.x = point1.getX() - point2.getX();
		this.y = point1.getY() - point2.getY();
	}

	@Override
	public void sub(Vector1D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x -= vector.getX();
		this.y -= vector.getY();
	}

	@Override
	public void setLength(double newLength) {
		final double l = getLength();
		if (l != 0) {
			final double f = newLength / l;
			this.x *= f;
			this.y *= f;
		} else {
			this.x = newLength;
			this.y = 0;
		}
	}

	@Override
	public UnmodifiableVector1D<Vector1d, Point1d, Segment1D<?, ?>> toUnmodifiable() {
		return new UnmodifiableVector1D<>() {

			private static final long serialVersionUID = 6848610371671516804L;

			@Override
			public Vector1d toUnitVector() {
				return Vector1d.this.toUnitVector();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Vector1d clone() {
				return Vector1d.this.getGeomFactory().newVector(Vector1d.this.getSegment(),
						Vector1d.this.getX(), Vector1d.this.getY());
			}

			@Override
			public double getX() {
				return Vector1d.this.getX();
			}

			@Override
			public int ix() {
				return Vector1d.this.ix();
			}

			@Override
			public double getY() {
				return Vector1d.this.getY();
			}

			@Override
			public int iy() {
				return Vector1d.this.iy();
			}

			@Override
			public Segment1D<?, ?> getSegment() {
				return Vector1d.this.getSegment();
			}

			@Override
			public GeomFactory1D<Vector1d, Point1d> getGeomFactory() {
				return Vector1d.this.getGeomFactory();
			}

			@Override
			public String toString() {
				return Vector1d.this.toString();
			}

			@Override
			public void toJson(JsonBuffer buffer) {
				Vector1d.this.toJson(buffer);
			}

		};
	}

}
