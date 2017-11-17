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

package org.arakhne.afc.math.geometry.d2.dfx;

import javafx.beans.property.DoubleProperty;

import org.arakhne.afc.math.geometry.d2.GeomFactory2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiablePoint2D;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 2D Point with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point2dfx extends Tuple2dfx<Point2dfx> implements Point2D<Point2dfx, Vector2dfx> {

	private static final long serialVersionUID = 2543935341943572211L;

	/** Construct a zero point.
	 */
	public Point2dfx() {
		//
	}

	/** Construct a point with the given properties for its coordinates.
	 * @param x property for the x coordinate.
	 * @param y property for the y coordinate.
	 */
	public Point2dfx(DoubleProperty x, DoubleProperty y) {
		super(x, y);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Point2dfx(Tuple2D<?> tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Point2dfx(int[] tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Point2dfx(double[] tuple) {
		super(tuple);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2dfx(int x, int y) {
		super(x, y);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2dfx(float x, float y) {
		super(x, y);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2dfx(double x, double y) {
		super(x, y);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2dfx(long x, long y) {
		super(x, y);
	}

	/** Convert the given tuple to a real Point2dfx.
	 *
	 * <p>If the given tuple is already a Point2dfx, it is replied.
	 *
	 * @param tuple the tuple.
	 * @return the Point2dfx.
	 * @since 14.0
	 */
	public static Point2dfx convert(Tuple2D<?> tuple) {
		if (tuple instanceof Point2dfx) {
			return (Point2dfx) tuple;
		}
		return new Point2dfx(tuple.getX(), tuple.getY());
	}

	@Override
	public GeomFactory2dfx getGeomFactory() {
		return GeomFactory2dfx.SINGLETON;
	}

	@Override
	public UnmodifiablePoint2D<Point2dfx, Vector2dfx> toUnmodifiable() {
		return new UnmodifiablePoint2D<Point2dfx, Vector2dfx>() {

			private static final long serialVersionUID = 5419032367247268556L;

			@Override
			public GeomFactory2D<Vector2dfx, Point2dfx> getGeomFactory() {
				return Point2dfx.this.getGeomFactory();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Point2dfx clone() {
				return Point2dfx.this.getGeomFactory().newPoint(
						Point2dfx.this.getX(),
						Point2dfx.this.getY());
			}

			@Override
			public int iy() {
				return Point2dfx.this.iy();
			}

			@Override
			public int ix() {
				return Point2dfx.this.ix();
			}

			@Override
			public double getY() {
				return Point2dfx.this.getY();
			}

			@Override
			public double getX() {
				return Point2dfx.this.getX();
			}

			@Override
			public String toString() {
				return Point2dfx.this.toString();
			}

			@Override
			public void toJson(JsonBuffer buffer) {
				Point2dfx.this.toJson(buffer);
			}

		};
	}

}
