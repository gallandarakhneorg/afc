/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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
import org.arakhne.afc.math.geometry.d1.UnmodifiableVector1D;
import org.arakhne.afc.math.geometry.d1.Vector1D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 1.5D Vector with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class Vector1dfx extends Tuple1dfx<Vector1dfx> implements Vector1D<Vector1dfx, Point1dfx, Segment1D<?, ?>> {

	private static final long serialVersionUID = -1551173291533274098L;

	/** Construct a zero vector.
	 *
	 * @param segment the segment associated to the tuple.
	 * @param x curviline coordinate.
	 * @param y shift distance.
	 */
	public Vector1dfx(ObjectProperty<WeakReference<Segment1D<?, ?>>> segment, DoubleProperty x, DoubleProperty y) {
		super(segment, x, y);
	}

	/** Construct a zero vector.
	 *
	 * @param tuple the tuple to copy.
	 */
	public Vector1dfx(Vector1D<?, ?, ?> tuple) {
		super(tuple.getSegment(), tuple.getX(), tuple.getY());
	}

	/** Construct a zero vector.
	 *
	 * @param tuple the tuple to copy.
	 */
	public Vector1dfx(Point1D<?, ?, ?> tuple) {
		super(tuple.getSegment(), tuple.getX(), tuple.getY());
	}

	/** Construct a zero vector.
	 *
	 * @param segment the segment associated to the vector.
	 */
	public Vector1dfx(Segment1D<?, ?> segment) {
		super(segment);
	}

	/** Construct a zero vector.
	 */
	public Vector1dfx() {
		super();
	}

	/** Constructor by copy.
	 *
	 * @param segment the segment associated to the vector.
	 * @param tuple is the tuple to copy.
	 */
	public Vector1dfx(Segment1D<?, ?> segment, Tuple2D<?> tuple) {
		super(segment, tuple);
	}

	/** Constructor by copy.
	 * @param segment the segment associated to the vector.
	 * @param tuple is the tuple to copy.
	 */
	public Vector1dfx(Segment1D<?, ?> segment, int[] tuple) {
		super(segment, tuple);
	}

	/** Constructor by copy.
	 * @param segment the segment associated to the vector.
	 * @param tuple is the tuple to copy.
	 */
	public Vector1dfx(Segment1D<?, ?> segment, double[] tuple) {
		super(segment, tuple);
	}

	/** Construct a vector with the given coordinates.
	 * @param segment the segment associated to the vector.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector1dfx(Segment1D<?, ?> segment, int x, int y) {
		super(segment, x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param segment the segment associated to the vector.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector1dfx(Segment1D<?, ?> segment, float x, float y) {
		super(segment, x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param segment the segment associated to the vector.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector1dfx(Segment1D<?, ?> segment, double x, double y) {
		super(segment, x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param segment the segment associated to the vector.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector1dfx(Segment1D<?, ?> segment, long x, long y) {
		super(segment, x, y);
	}

	/** Convert the given tuple to a real Vector1dfx.
	 *
	 * <p>If the given tuple is already a Vector1dfx, it is replied.
	 *
	 * @param tuple the tuple.
	 * @return the Vector1dfx.
	 * @since 14.0
	 */
	public static Vector1dfx convert(Tuple1dfx<?> tuple) {
		if (tuple instanceof Vector1dfx) {
			return (Vector1dfx) tuple;
		}
		return new Vector1dfx(tuple.getSegment(), tuple.getX(), tuple.getY());
	}

	@Override
	public UnmodifiableVector1D<Vector1dfx, Point1dfx, Segment1D<?, ?>> toUnmodifiable() {
		return new UnmodifiableVector1D<>() {

			private static final long serialVersionUID = 6848610371671516804L;

			@Override
			public Vector1dfx toUnitVector() {
				return Vector1dfx.this.toUnitVector();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Vector1dfx clone() {
				return Vector1dfx.this.getGeomFactory().newVector(Vector1dfx.this.getSegment(),
						Vector1dfx.this.getX(), Vector1dfx.this.getY());
			}

			@Override
			public double getX() {
				return Vector1dfx.this.getX();
			}

			@Override
			public int ix() {
				return Vector1dfx.this.ix();
			}

			@Override
			public double getY() {
				return Vector1dfx.this.getY();
			}

			@Override
			public int iy() {
				return Vector1dfx.this.iy();
			}

			@Override
			public Segment1D<?, ?> getSegment() {
				return Vector1dfx.this.getSegment();
			}

			@Override
			public GeomFactory1D<Vector1dfx, Point1dfx> getGeomFactory() {
				return Vector1dfx.this.getGeomFactory();
			}

			@Override
			public String toString() {
				return Vector1dfx.this.toString();
			}

			@Override
			public void toJson(JsonBuffer buffer) {
				Vector1dfx.this.toJson(buffer);
			}

		};
	}

}
