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

package org.arakhne.afc.math.geometry.d1.dfx;

import java.lang.ref.WeakReference;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;

import org.arakhne.afc.math.geometry.d1.GeomFactory1D;
import org.arakhne.afc.math.geometry.d1.Point1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.UnmodifiablePoint1D;
import org.arakhne.afc.math.geometry.d1.Vector1D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 1.5D Point with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class Point1dfx extends Tuple1dfx<Point1dfx> implements Point1D<Point1dfx, Vector1dfx, Segment1D<?, ?>> {

	private static final long serialVersionUID = -1676607316269267712L;

	/** Construct a zero point.
	 *
	 * @param segment the segment associated to the tuple.
	 * @param x curviline coordinate.
	 * @param y shift distance.
	 */
	public Point1dfx(ObjectProperty<WeakReference<Segment1D<?, ?>>> segment, DoubleProperty x, DoubleProperty y) {
		super(segment, x, y);
	}

	/** Construct a zero vector.
	 *
	 * @param tuple the tuple to copy.
	 */
	public Point1dfx(Vector1D<?, ?, ?> tuple) {
		super(tuple.getSegment(), tuple.getX(), tuple.getY());
	}

	/** Construct a zero vector.
	 *
	 * @param tuple the tuple to copy.
	 */
	public Point1dfx(Point1D<?, ?, ?> tuple) {
		super(tuple.getSegment(), tuple.getX(), tuple.getY());
	}

	/** Construct a zero vector.
	 *
	 * @param segment the segment associated to the vector.
	 */
	public Point1dfx(Segment1D<?, ?> segment) {
		super(segment);
	}

	/** Construct a zero vector.
	 */
	public Point1dfx() {
		super();
	}

	/** Constructor by copy.
	 *
	 * @param segment the segment associated to the vector.
	 * @param tuple is the tuple to copy.
	 */
	public Point1dfx(Segment1D<?, ?> segment, Tuple2D<?> tuple) {
		super(segment, tuple);
	}

	/** Constructor by copy.
	 * @param segment the segment associated to the vector.
	 * @param tuple is the tuple to copy.
	 */
	public Point1dfx(Segment1D<?, ?> segment, int[] tuple) {
		super(segment, tuple);
	}

	/** Constructor by copy.
	 * @param segment the segment associated to the vector.
	 * @param tuple is the tuple to copy.
	 */
	public Point1dfx(Segment1D<?, ?> segment, double[] tuple) {
		super(segment, tuple);
	}

	/** Construct a vector with the given coordinates.
	 * @param segment the segment associated to the vector.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point1dfx(Segment1D<?, ?> segment, int x, int y) {
		super(segment, x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param segment the segment associated to the vector.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point1dfx(Segment1D<?, ?> segment, float x, float y) {
		super(segment, x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param segment the segment associated to the vector.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point1dfx(Segment1D<?, ?> segment, double x, double y) {
		super(segment, x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param segment the segment associated to the vector.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point1dfx(Segment1D<?, ?> segment, long x, long y) {
		super(segment, x, y);
	}

	/** Convert the given tuple to a real Point1dfx.
	 *
	 * <p>If the given tuple is already a Point1dfx, it is replied.
	 *
	 * @param tuple the tuple.
	 * @return the Point1dfx.
	 * @since 14.0
	 */
	public static Point1dfx convert(Tuple1dfx<?> tuple) {
		if (tuple instanceof Point1dfx) {
			return (Point1dfx) tuple;
		}
		return new Point1dfx(tuple.getSegment(), tuple.getX(), tuple.getY());
	}

	@Override
	public UnmodifiablePoint1D<Point1dfx, Vector1dfx, Segment1D<?, ?>> toUnmodifiable() {
		return new UnmodifiablePoint1D<Point1dfx, Vector1dfx, Segment1D<?, ?>>() {

			private static final long serialVersionUID = 7256619908484020358L;

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Point1dfx clone() {
				return Point1dfx.this.getGeomFactory().newPoint(Point1dfx.this.getSegment(),
						Point1dfx.this.getX(), Point1dfx.this.getY());
			}

			@Override
			public double getX() {
				return Point1dfx.this.getX();
			}

			@Override
			public int ix() {
				return Point1dfx.this.ix();
			}

			@Override
			public double getY() {
				return Point1dfx.this.getY();
			}

			@Override
			public int iy() {
				return Point1dfx.this.iy();
			}

			@Override
			public Segment1D<?, ?> getSegment() {
				return Point1dfx.this.getSegment();
			}

			@Override
			public GeomFactory1D<Vector1dfx, Point1dfx> getGeomFactory() {
				return Point1dfx.this.getGeomFactory();
			}

			@Override
			public String toString() {
				return Point1dfx.this.toString();
			}

			@Override
			public void toJson(JsonBuffer buffer) {
				Point1dfx.this.toJson(buffer);
			}

		};
	}

}
